package com.mountblue.blogapp.RestController;

import com.mountblue.blogapp.Entity.Post;
import com.mountblue.blogapp.Entity.Tag;
import com.mountblue.blogapp.Entity.User;
import com.mountblue.blogapp.Service.PostService;
import com.mountblue.blogapp.Service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class BlogController {
    PostService postService;
    TagService tagService;
@Autowired
    public BlogController(PostService postService, TagService tagService) {
        this.postService = postService;
        this.tagService = tagService;
    }

    @GetMapping("/Posts")
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/Posts/{postId}")
    public Post getPostById(@PathVariable(name = "postId") Integer postId) {
        return postService.getPostById(postId);
    }

    @PutMapping("/Posts")
    public Post updatePost(@RequestBody Post post) {
        List<String> tagList = List.of(post.getTags().split(","));
        Set<String> tags = new HashSet<>(tagList);
        List<Tag> resultList = new ArrayList<>();
        for (String tagName : tags) {
            resultList.add(new Tag(tagName));
        }
        List<Tag> newTags = tagService.checkForTags(resultList);
        post.setTagsList(newTags);
    return postService.savePost(post);
    }

    @PostMapping("/Posts")
    public Post createPost(@RequestBody Post post) {
        post.setId(0);
        List<String> tagList = List.of(post.getTags().split(","));
        Set<String> tags = new HashSet<>(tagList);
        List<Tag> resultList = new ArrayList<>();
        for (String tagName : tags) {
            resultList.add(new Tag(tagName));
        }
        List<Tag> newTags = tagService.checkForTags(resultList);
        post.setTagsList(newTags);
        Post newPost = postService.savePost(post);
        return newPost;
    }

    @DeleteMapping("/Posts/{postId}")
    public String deletePost(@PathVariable(name = "postId") Integer postId) {
        postService.deletePostById(postId);
        return "post deleted";
    }

    @PostMapping("/filter/{pageNo}")
    public List<Post> filter(@RequestParam(value = "tag", required = false) List<String> tags,
                             @RequestParam(value = "author", required = false) List<String> author,
                             @RequestParam(value = "start", required = false) LocalDateTime startTime,
                             @RequestParam(value = "end", required = false) LocalDateTime endTime,
                             @RequestParam(value = "search", required = false) String search,
                             @PathVariable(value = "pageNo", required = false) Integer pageNo,
                             @RequestParam(value = "direction", required = false, defaultValue = "DESC") String direction) {
        Page<Post> page = postService.filtering(author, tags, startTime, endTime, search, pageNo, direction);
        List<Post> postList = page.getContent();
        return postList;
    }
}
