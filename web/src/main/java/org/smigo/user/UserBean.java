package org.smigo.user;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.AssertTrue;
import java.io.Serializable;
import java.util.Locale;

public class UserBean implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(UserBean.class);

    @SafeHtml(whitelistType = WhiteListType.NONE)
    private String displayName = null;

    @Email
    private String email = null;

    private String username = null;

    @SafeHtml(whitelistType = WhiteListType.NONE)
    private String about = null;

    private Locale locale = null;

    @AssertTrue
    private boolean termsOfService = false;

    public UserBean() {
    }


    public UserBean(String username, String displayName, String email, String about, Locale locale) {
        this.displayName = displayName;
        this.username = username;
        this.email = email;
        this.about = about;
        this.locale = locale;
        log.debug("Create user instance " + this.toString());
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", about='" + about + '\'' +
                ", locale=" + locale +
                ", termsOfService=" + termsOfService +
                '}';
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String firstname) {
        this.displayName = firstname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public boolean isTermsOfService() {
        return termsOfService;
    }

    public void setTermsOfService(boolean termsOfService) {
        this.termsOfService = termsOfService;
    }
}
