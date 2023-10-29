package com.elec5619.community.service;

import com.elec5619.community.dao.CommentMapper;
import com.elec5619.community.entity.Comment;
import com.elec5619.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

import static com.elec5619.community.util.CommunityConstant.ENTITY_TYPE_POST;

/**
 * @Author: zpj
 */

@Service
public class CommentService {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<Comment> findCommentsByEntity(int entityType, String keyword, int entityId, int offset, int limit) {
        return commentMapper.selectCommentsByEntity(entityType, keyword, entityId, offset, limit);
    }

    public int findCommentCount(int entityType, String keyword, int entityId) {
        return commentMapper.selectCountByEntity(entityType, keyword, entityId);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int addComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("Empty attributes");
        }
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        int rows = commentMapper.insertComment(comment);
        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            int count = commentMapper.selectCountByEntity(ENTITY_TYPE_POST, null, comment.getEntityId());
            discussPostService.updateCommentCount(comment.getEntityId(), count);
        }
        return rows;
    }
}
