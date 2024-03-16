package com.mountblue.blogapp.Repository;

import com.mountblue.blogapp.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
   Optional<Comment> findByComment(String comment);
}
