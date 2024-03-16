package com.mountblue.blogapp.Controller;

import com.mountblue.blogapp.Entity.Comment;
import com.mountblue.blogapp.Entity.Post;
import com.mountblue.blogapp.Entity.Tag;
import com.mountblue.blogapp.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class PostController {

    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/newpost")
    public String createPost(Model model) {
        Post newPost = new Post();
        newPost.setCreatedAt(LocalDateTime.now());
        model.addAttribute("post", newPost);
        return "create_post";
    }

    @PostMapping("/publish")
    public String publish(@ModelAttribute("post") Post post) {
        List<Tag> tags = postService.checkForTags(post.getTagList());
        post.setTagsList(tags);
        if (post.getPublished() == null) {
            post.setPublished(true);
            post.setPublishedAt(LocalDateTime.now());
            post.setAuthor(postService.getUser());
        } else {
            post.setUpdatedAt(LocalDateTime.now());
        }
        postService.savePost(post);
        return "redirect:/list";
    }

    @GetMapping("/list")
    public String displayPost(Model model) {
        List<Post> postList = postService.getAllPosts();
        model.addAttribute("allposts", postList);
        return "listall_posts";
    }

    @GetMapping("/readForm")
    public String readForm(Model model, @RequestParam("formId") Integer id) {
        Post post = postService.getPostById(id);
        model.addAttribute("post", post);
        Comment newComment = new Comment();
        model.addAttribute("comment", newComment);
        return "read_Form";
    }

    @GetMapping("/updateForm")
    public String updateForm(Model model, @RequestParam("formId") Integer id) {
        Post post = postService.getPostById(id);
        model.addAttribute("post", post);
        return "create_post";
    }

    @GetMapping("/deleteForm")
    public String deleteForm(Model model, @RequestParam("formId") Integer id) {
        Post post = postService.getPostById(id);
        postService.deletePost(post);
        return "redirect:/list";
    }

    @PostMapping("/addComment")
    public String addComment(Model theModel, @RequestParam("id") Integer postId, @RequestParam("coId") Integer commentId, @RequestParam("comment") String comment) {

        Post currentPost = postService.getPostById(postId);
        if (commentId != 0) {
            Comment updateComment = postService.getCommentById(commentId);
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
        Comment comment = postService.getCommentById(id);
        model.addAttribute("post", currentPost);
        model.addAttribute("comment", comment);
        return "read_Form";
    }

    @GetMapping("deleteComment")
    public String deleteComment(@RequestParam("postId") Integer postId, @RequestParam("commentId") Integer id, Model model) {
        Post currentPost = postService.getPostById(postId);
        Comment comment = postService.getCommentById(id);
        postService.deleteCommentById(id);
        model.addAttribute("post", currentPost);
        model.addAttribute("comment", new Comment());
        return "read_Form";
    }

}
