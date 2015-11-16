package org.smigo.user.password;

/*
 * #%L
 * Smigo
 * %%
 * Copyright (C) 2015 Christian Nilsson
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.user.AuthenticatedUser;
import org.smigo.user.MailHandler;
import org.smigo.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
class PasswordHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PersistentTokenRepository tokenRepository;
    @Autowired
    private UserDao userDao;
    @Autowired
    private MailHandler emailHandler;
    @Value("${baseUrl}")
    private String baseUrl;

    private final Map<String, ResetKeyItem> resetKeyMap = new ConcurrentHashMap<String, ResetKeyItem>();


    public boolean setPassword(ResetKeyPasswordFormBean resetFormBean) {
        String resetKey = resetFormBean.getResetKey();
        ResetKeyItem resetKeyItem = resetKeyMap.get(resetKey);

        if (resetKeyItem == null) {
            log.warn("No valid resetPasswordKey found. Reset password not possible. " + resetFormBean);
            return false;
        }

        if (!resetKeyItem.isValid()) {
            log.info("Reset key has expired. Reset password not possible. " + resetKeyItem);
            emailHandler.sendAdminNotification("Reset password failed - key expired", resetKeyItem.toString());
            return false;
        }

        String email = resetKeyItem.getEmail();
        List<UserDetails> users = userDao.getUserByEmail(email);
        if (users.isEmpty()) {
            log.warn("User with this email not found. Reset password not possible. " + resetKeyItem);
            emailHandler.sendAdminNotification("Reset password failed - email not found", email);
            return false;
        }

        AuthenticatedUser user = (AuthenticatedUser) users.get(0);
        String password = resetFormBean.getPassword();
        updatePassword(user, password);
        resetKeyItem.invalidate();
        log.info("Password successfully reset by: " + user);
        return true;
    }

    public void updatePassword(AuthenticatedUser username, String newPassword) {
        final String encodedPassword = passwordEncoder.encode(newPassword);
        userDao.updatePassword(username.getId(), encodedPassword);
        tokenRepository.removeUserTokens(username.getUsername());
    }

    public void sendResetPasswordEmail(String emailAddress) {
        log.info("Sending reset email to: " + emailAddress);
        log.info("Size of resetKeyMap:" + resetKeyMap.size());

        final String subject = "reset password";

        List<UserDetails> users = userDao.getUserByEmail(emailAddress);
        if (users.isEmpty()) {
            final String text = "Can not reset password. No user with email " + emailAddress;
            emailHandler.sendClientMessage(emailAddress, subject, text);
        } else {

            final String id = UUID.randomUUID().toString();

            for (String s : resetKeyMap.keySet()) {
                ResetKeyItem resetKeyItem = resetKeyMap.get(s);
                if (resetKeyItem != null && emailAddress.equals(resetKeyItem.getEmail()) && resetKeyItem.isValid()) {
                    log.warn("Multiple occurrence of email in resetPasswordMap" + resetKeyItem);
                    resetKeyItem.invalidate();
                }
            }

            resetKeyMap.put(id, new ResetKeyItem(id, emailAddress));
            final String text = "Click link to reset password. " + baseUrl + "/login-reset/" + id;
            emailHandler.sendClientMessage(emailAddress, subject, text);
        }
    }


}