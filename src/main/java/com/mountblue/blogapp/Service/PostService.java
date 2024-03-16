package com.mountblue.blogapp.Service;

import com.mountblue.blogapp.Entity.Post;
import com.mountblue.blogapp.Entity.User;

import java.util.List;

public interface PostService  {
    public Post getPostById(int id);
    public void savePost(Post post);
    public User getUser();
    public void deletePost(Post post);
    public List<Post> getAllPosts();
}
