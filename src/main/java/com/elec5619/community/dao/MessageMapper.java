package com.elec5619.community.dao;

import com.elec5619.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
/**
 * @author Kaixuan Yu
 */
@Mapper
public interface MessageMapper {
    List<Message> selectConversations(int userId, String keyword, int offset, int limit);

    int selectConversationCount(int userId, String keyword);

    List<Message> selectLetters(String conversationId, String keyword, int offset, int limit);

    int selectLetterCount(String conversationId, String keyword);

    int selectLetterUnreadCount(int userId, String conversationId);

    // new messages
    int insertMessage(Message message);

    int updateStatus(List<Integer> ids, int status);

}
