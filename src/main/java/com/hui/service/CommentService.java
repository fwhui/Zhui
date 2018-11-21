package com.hui.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hui.dao.CommentDao;
import com.hui.model.Comment;

@Service
public class CommentService {
    @Autowired
    private CommentDao commentDao;

    public List<Comment> getCommentsByEntity(int entityId, int entityType) {
        return commentDao.selectByEntity(entityId, entityType);
    }

    public int addComment(Comment comment) {
        return commentDao.addComment(comment);
    }

    public int getCommentCount(int entityId, int entityType) {
        return commentDao.getCommentCount(entityId, entityType);
    }

    public void deleteComment(int entityId, int entityType) {
        commentDao.updateStatus(entityId, entityType, 1);
    }

	public Comment getCommentById(int commentId) {
		return  commentDao.getCommentById(commentId);
	}
	
	public int getUserCommentCount(int userId) {
        return commentDao.getUserCommentCount(userId);
    }
}
