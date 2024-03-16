package com.mountblue.blogapp.Service;

import com.mountblue.blogapp.Entity.Comment;
import com.mountblue.blogapp.Entity.Post;
import com.mountblue.blogapp.Entity.Tag;
import com.mountblue.blogapp.Entity.User;
import com.mountblue.blogapp.Repository.CommentRepository;
import com.mountblue.blogapp.Repository.PostRepository;
import com.mountblue.blogapp.Repository.TagRepository;
import com.mountblue.blogapp.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private PostRepository postRepository;

    private TagRepository tagRepository;
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    public PostService(PostRepository postRepository, TagRepository tagRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
    }

    public Post getPostById(int id) {
        return postRepository.findById(id).get();
    }

    public void savePost(Post post) {
        postRepository.save(post);
    }

    public List<Tag> checkForTags(List<Tag> tags) {
        List<Tag> newTags = new ArrayList<>();
        for (Tag tag : tags) {
            Optional<Tag> tagName = tagRepository.findByName(tag.getName());
            if (!tagName.isEmpty()) {
                newTags.add(tagName.get());
            } else {
                newTags.add(tag);
            }
        }
        return newTags;
    }

    public User getUser() {
        return userRepository.findById(2).get();
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
    public void deletePost(Post post)
    {
        postRepository.delete(post);
    }

    public Optional<Comment> getCommentByComment(String comment)
    {
       return   commentRepository.findByComment(comment);
    }
    public Comment getCommentById(Integer id)
    {
        return commentRepository.findById(id).get();
    }

    public void saveComment(Comment comment)
    {
        commentRepository.save(comment);
    }

    public void deleteCommentById(Integer id)
    {
        commentRepository.deleteById(id);
    }
}
