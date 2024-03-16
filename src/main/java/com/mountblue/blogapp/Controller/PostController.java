package com.mountblue.blogapp.Controller;

import com.mountblue.blogapp.Entity.Comment;
import com.mountblue.blogapp.Entity.Post;
import com.mountblue.blogapp.Entity.Tag;
import com.mountblue.blogapp.Service.CommentService;
import com.mountblue.blogapp.Service.PostService;
import com.mountblue.blogapp.Service.PostServiceImpl;
import com.mountblue.blogapp.Service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/post")
public class PostController {

    private PostService postService;
    private CommentService commentService;
    private TagService tagService;

    @Autowired
    public PostController(PostService postService, CommentService commentService, TagService tagService) {
        this.postService = postService;
        this.commentService = commentService;
        this.tagService = tagService;
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
        List<Tag> tags = tagService.checkForTags(post.getTagList());
        post.setTagsList(tags);
        if (post.getPublished() == null) {
            post.setPublished(true);
            post.setPublishedAt(LocalDateTime.now());
            post.setAuthor(postService.getUser());
        } else {
            post.setUpdatedAt(LocalDateTime.now());
        }
        postService.savePost(post);
        return "redirect:/post/list";
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
        return "redirect:/post/list";
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
