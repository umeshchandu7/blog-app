package com.mountblue.blogapp.Service;

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
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;
    private UserRepository userRepository;


    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Post getPostById(int id) {
        return postRepository.findById(id).get();
    }

    @Override
    public void savePost(Post post) {
        postRepository.save(post);
    }


    @Override
    public User getUser() {
        return userRepository.findById(2).get();
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public void deletePost(Post post) {
        postRepository.delete(post);
    }


}
