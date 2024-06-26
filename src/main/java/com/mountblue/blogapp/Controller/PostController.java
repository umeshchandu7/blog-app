package com.mountblue.blogapp.Controller;

import com.mountblue.blogapp.Entity.Comment;
import com.mountblue.blogapp.Entity.Post;
import com.mountblue.blogapp.Entity.Tag;
import com.mountblue.blogapp.Entity.User;
import com.mountblue.blogapp.Service.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class PostController {

    private PostService postService;
    private CommentService commentService;
    private TagService tagService;
    private UserService userService;

    @Autowired
    public PostController(PostService postService, CommentService commentService, TagService tagService, UserService userService) {
        this.postService = postService;
        this.commentService = commentService;
        this.tagService = tagService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String getListOfPosts(Model model) {
        return filter(null, null, null, null, null, 1, "DESC", null,model);
    }

    @GetMapping("/newpost")
    public String createPost(Model model) {
        Post newPost = new Post();
        newPost.setCreatedAt(LocalDateTime.now());
        model.addAttribute("post", newPost);
        return "create_post";
    }

    @PostMapping("/publish")
    public String publish(@ModelAttribute("post") Post post, @ModelAttribute("tags") String tagString, @AuthenticationPrincipal UserDetails userDetails) {
        List<String> tagList = List.of(tagString.split(","));
        List<Tag> tags = new ArrayList<>();
        for (String tagName : tagList) {
            tags.add(new Tag(tagName));
        }
        List<Tag> newTags = tagService.checkForTags(tags);
        post.setTagsList(newTags);
        if (post.getPublished() == null) {
            post.setPublished(true);
            post.setPublishedAt(LocalDateTime.now());
            post.setAuthor(userService.findByUserName(userDetails.getUsername()));
        } else {
            post.setUpdatedAt(LocalDateTime.now());
        }
        postService.savePost(post);
        return "redirect:/filter/1";
    }

    @GetMapping("/readForm")
    public String readForm(Model model, @RequestParam("formId") Integer id, @AuthenticationPrincipal UserDetails userDetails) {
        Post post = postService.getPostById(id);
        model.addAttribute("post", post);
        Comment newComment = new Comment();
        model.addAttribute("comment", newComment);
        if (userDetails != null) {
            model.addAttribute("username", userDetails.getUsername());
        }
        return "read_Form";
    }

    @GetMapping("/updateForm")
    public String updateForm(Model model, @RequestParam("formId") Integer id, @AuthenticationPrincipal UserDetails userDetails) {
        Post post = postService.getPostById(id);
        model.addAttribute("post", post);
        if (userDetails != null) {
            model.addAttribute("username", userDetails.getUsername());
        }
        return "create_post";
    }

    @GetMapping("/deleteForm")
    public String deleteForm(Model model, @RequestParam("formId") Integer id, @AuthenticationPrincipal UserDetails userDetails) {
        Post post = postService.getPostById(id);
        postService.deletePost(post);
        if (userDetails != null) {
            model.addAttribute("username", userDetails.getUsername());
        }
        return "redirect:/filter/1";
    }


    @GetMapping("/filter/{pageNo}")
    public String filter(@RequestParam(value = "tag", required = false) List<String> tags,
                         @RequestParam(value = "author", required = false) List<String> author,
                         @RequestParam(value = "start", required = false) LocalDateTime startTime,
                         @RequestParam(value = "end", required = false) LocalDateTime endTime,
                         @RequestParam(value = "search", required = false) String search,
                         @PathVariable(value = "pageNo", required = false) Integer pageNo,
                         @RequestParam(value = "direction", required = false, defaultValue = "DESC") String direction,
                         @AuthenticationPrincipal UserDetails userDetails,
                         Model model) {
        Page<Post> page = postService.filtering(author, tags, startTime, endTime, search, pageNo, direction);
        List<Post> postList = page.getContent();
        if (author == null) {
            author = new ArrayList<String>();
        }
        if (tags == null) {
            tags = new ArrayList<String>();
        }
        List<User> userList = postService.getAllUsers();
        List<Tag> tagList = tagService.getAllTags();
        model.addAttribute("start", startTime);
        model.addAttribute("end", endTime);
        model.addAttribute("search", search);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("allusers", userList);
        model.addAttribute("alltags", tagList);
        model.addAttribute("allposts", postList);
        model.addAttribute("checkedTags", tags);
        model.addAttribute("direction", direction);
        model.addAttribute("checkedAuthors", author);
        if (userDetails != null) {
            model.addAttribute("username", userDetails.getUsername());
        }
        return "listall_posts";
    }


}
