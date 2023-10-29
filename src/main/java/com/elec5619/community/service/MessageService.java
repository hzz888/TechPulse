package com.elec5619.community.service;

import com.elec5619.community.dao.MessageMapper;
import com.elec5619.community.entity.Message;
import com.elec5619.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
/**
 * @author Kaixuan Yu
 */
@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<Message> findConversations(int userId, String keyword, int offset, int limit) {
        return messageMapper.selectConversations(userId, keyword, offset, limit);
    }

    public int findConversationCount(int userId, String keyword) {
        return messageMapper.selectConversationCount(userId, keyword);
    }

    public List<Message> findLetters(String conversationId, String keyword, int offset, int limit) {
        return messageMapper.selectLetters(conversationId, keyword, offset, limit);
    }

    public int findLetterCount(String conversationId, String keyword) {
        return messageMapper.selectLetterCount(conversationId, keyword);
    }

    public int findLetterUnreadCount(int userId, String conversationId) {
        return messageMapper.selectLetterUnreadCount(userId, conversationId);
    }

    public int addMessage(Message message) {
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));
        return messageMapper.insertMessage(message);
    }

    public int readMessage(List<Integer> ids) {
        return messageMapper.updateStatus(ids, 1);
    }

}
