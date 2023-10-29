package com.elec5619.community.dao;

import com.elec5619.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: zpj
 */

@Mapper
public interface CommentMapper {

    List<Comment> selectCommentsByEntity(@Param("entityType") int entityType, @Param("keyword") String keyword, @Param("entityId") int entityId, @Param("offset") int offset, @Param("limit") int limit);

    int selectCountByEntity(@Param("entityType") int entityType,  @Param("keyword") String keyword,  @Param("entityId") int entityId);

    int insertComment(Comment comment);
}
