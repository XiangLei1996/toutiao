package com.nowcoder.service;

import com.nowcoder.dao.CommentDAO;
import com.nowcoder.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: XiangL
 * Date: 2019/5/26 9:31
 * Version 1.0
 */
@Service
public class CommentService {

    @Autowired
    CommentDAO commentDAO;

    public List<Comment> getCommentsByEntity(int entityId, int entityType){
        return commentDAO.selectByEntity(entityId, entityType);
    }

    public int addComment(Comment comment){
        return commentDAO.addComment(comment);
    }

    public int getCommentCount(int entityId, int entityType){
        return commentDAO.getCommentCount(entityId, entityType);
    }
}

