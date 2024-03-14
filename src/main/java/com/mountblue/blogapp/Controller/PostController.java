package com.mountblue.blogapp.Controller;

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
        return "listall_posts";
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
}
