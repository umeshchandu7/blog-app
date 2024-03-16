package com.mountblue.blogapp.Service;

import com.mountblue.blogapp.Entity.Comment;

public interface CommentService {
    public Comment getCommentById(Integer id);
    public void saveComment(Comment comment);
    public void deleteCommentById(Integer id);
}
