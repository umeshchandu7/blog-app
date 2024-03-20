package com.mountblue.blogapp.Service;

import com.mountblue.blogapp.Entity.Post;
import com.mountblue.blogapp.Entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface PostService  {
    public Post getPostById(int id);
    public void savePost(Post post);
    public User getUser();
    public void deletePost(Post post);
    public List<Post> getAllPosts();
    public List<Post> getSortedList(String direction);
    public List<Post> getListByTitleOrContentOrTagOrAuthor(String data);
    public List<User> getAllUsers();
    public List<Post> filtering(List<String> authors, List<String> tags, LocalDateTime startTime,LocalDateTime endTime,String search);
}
