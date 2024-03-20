package com.mountblue.blogapp.Controller;

import com.mountblue.blogapp.Entity.Comment;
import com.mountblue.blogapp.Entity.Post;
import com.mountblue.blogapp.Entity.Tag;
import com.mountblue.blogapp.Entity.User;
import com.mountblue.blogapp.Service.CommentService;
import com.mountblue.blogapp.Service.PostService;
import com.mountblue.blogapp.Service.PostServiceImpl;
import com.mountblue.blogapp.Service.TagService;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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

    @GetMapping("/")
    public String getListOfPosts(Model model) {
        return displayPost(model);
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
        List<User> userList = postService.getAllUsers();
        List<Tag> tagList = tagService.getAllTags();
        model.addAttribute("allusers", userList);
        model.addAttribute("alltags", tagList);
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

    @GetMapping("/sort")
    public String sortBy(Model model, @RequestParam("direction") String direction, @RequestParam("search") String search) {
        List<Post> postList = postService.getListByTitleOrContentOrTagOrAuthor(search);
        Comparator<Post> comparator1 = new Comparator<Post>() {
            @Override
            public int compare(Post o1, Post o2) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String Time1 = o1.getPublishedAt().format(formatter);
                String Time2 = o2.getPublishedAt().format(formatter);
                return Time1.compareTo(Time2);
            }
        };
        Comparator<Post> comparator2 = new Comparator<Post>() {
            @Override
            public int compare(Post o1, Post o2) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String Time1 = o1.getPublishedAt().format(formatter);
                String Time2 = o2.getPublishedAt().format(formatter);
                return Time2.compareTo(Time1);
            }
        };
        if (direction.equals("ASC")) {
            Collections.sort(postList, comparator1);
        } else {
            Collections.sort(postList, comparator2);
        }
        List<User> userList = postService.getAllUsers();
        List<Tag> tagList = tagService.getAllTags();
        model.addAttribute("allusers", userList);
        model.addAttribute("alltags", tagList);
        model.addAttribute("allposts", postList);
        return "listall_posts";
    }

    @GetMapping("/search")
    public String search(@RequestParam("search") String search, Model model) {
        List<Post> searchResult = postService.getListByTitleOrContentOrTagOrAuthor(search);
        model.addAttribute("search", search);
        model.addAttribute("allposts", searchResult);
        List<User> userList = postService.getAllUsers();
        List<Tag> tagList = tagService.getAllTags();
        model.addAttribute("allusers", userList);
        model.addAttribute("alltags", tagList);
        return "listall_posts";
    }

    @GetMapping("/filter")
    public String filter(@RequestParam(value = "tag", required = false) List<String> tags,
                         @RequestParam(value = "author", required = false) List<String> author,
                         @RequestParam(value = "start", required = false) LocalDateTime startTime,
                         @RequestParam(value = "end", required = false) LocalDateTime endTime,
                         @RequestParam("search") String search, Model model) {
        List<Post> postList = postService.filtering(author, tags, startTime, endTime, search);
        List<User> userList = postService.getAllUsers();
        List<Tag> tagList = tagService.getAllTags();
        model.addAttribute("allusers", userList);
        model.addAttribute("alltags", tagList);
        model.addAttribute("allposts", postList);
        return "listall_posts";
    }

}
