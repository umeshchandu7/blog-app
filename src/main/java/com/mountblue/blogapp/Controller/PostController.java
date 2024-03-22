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
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
        return filter(null, null, null, null, null, 1, "DESC", model);
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
        return "redirect:/post/filter/1";
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
        return "redirect:/post/filter/1";
    }


    @GetMapping("/filter/{pageNo}")
    public String filter(@RequestParam(value = "tag", required = false) List<String> tags,
                         @RequestParam(value = "author", required = false) List<String> author,
                         @RequestParam(value = "start", required = false) LocalDateTime startTime,
                         @RequestParam(value = "end", required = false) LocalDateTime endTime,
                         @RequestParam(value = "search", required = false) String search,
                         @PathVariable(value = "pageNo", required = false) Integer pageNo,
                         @RequestParam(value = "direction", required = false, defaultValue = "DESC") String direction,
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
        return "listall_posts";
    }


}
