package org.smigo.message;

import org.smigo.user.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageHandler {
    @Autowired
    private MessageDao messageDao;

    public List<Message> getMessages(Integer page, Integer size) {
        int from = page * size;
        return messageDao.getMessage("wall", from, size);
    }

    public int addMessage(Message message, AuthenticatedUser user) {
        return messageDao.addMessage(message, user);
    }
}
