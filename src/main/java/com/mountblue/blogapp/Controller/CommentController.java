package com.mountblue.blogapp.Controller;

import com.mountblue.blogapp.Entity.Comment;
import com.mountblue.blogapp.Entity.Post;
import com.mountblue.blogapp.Service.CommentService;
import com.mountblue.blogapp.Service.PostService;
import com.mountblue.blogapp.Service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@RequestMapping("/comment")
@Controller
public class CommentController {
    private PostService postService;
    private TagService tagService;
    private CommentService commentService;

    @Autowired
    public CommentController(PostService postService, TagService tagService, CommentService commentService) {
        this.postService = postService;
        this.tagService = tagService;
        this.commentService = commentService;
    }

    @PostMapping("/addComment")
    public String addComment(Model theModel, @RequestParam("id") Integer postId, @RequestParam("coId") Integer commentId, @RequestParam("comment") String comment) {

        Post currentPost = postService.getPostById(postId);
        if (commentId != 0) {
            Comment updateComment = commentService.getCommentById(commentId);
            updateComment.setComment(comment);
            updateComment.setUpdatedAt(LocalDateTime.now());
        } else {
            Comment currentComment = new Comment();
            currentComment.setComment(comment);
            currentComment.setName(currentPost.getAuthor().getName());
            currentComment.setEmail(currentPost.getAuthor().getEmail());
            currentComment.setCreatedAt(LocalDateTime.now());
            currentPost.addComment(currentComment);
        }
        postService.savePost(currentPost);
        theModel.addAttribute("post", currentPost);
        theModel.addAttribute("comment", new Comment());
        return "read_Form";
    }

    @GetMapping("/updateComment")
    public String updateComment(@RequestParam("postId") Integer postId, @RequestParam("commentId") Integer id, Model model) {
        Post currentPost = postService.getPostById(postId);
        Comment comment = commentService.getCommentById(id);
        model.addAttribute("post", currentPost);
        model.addAttribute("comment", comment);
        return "read_Form";
    }

    @GetMapping("deleteComment")
    public String deleteComment(@RequestParam("postId") Integer postId, @RequestParam("commentId") Integer id, Model model) {
        Post currentPost = postService.getPostById(postId);
        Comment comment = commentService.getCommentById(id);
        commentService.deleteCommentById(id);
        model.addAttribute("post", currentPost);
        model.addAttribute("comment", new Comment());
        return "read_Form";
    }
}
