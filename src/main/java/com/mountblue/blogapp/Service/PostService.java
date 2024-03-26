package com.mountblue.blogapp.Service;

import com.mountblue.blogapp.Entity.Post;
import com.mountblue.blogapp.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface PostService  {
    public Post getPostById(int id);
    public Post savePost(Post post);
    public User getUser();
    public void deletePost(Post post);
    public List<Post> getSortedList(String direction);
    public List<Post> getListByTitleOrContentOrTagOrAuthor(String data);
    public List<User> getAllUsers();
    public Page<Post> paginated(int pageNo, int pageSize);
    public Page<Post> filtering(List<String> authors, List<String> tags, LocalDateTime startTime,LocalDateTime endTime,String search,Integer pageNo,String direction);

public List<Post> getAllPosts();
public void deletePostById(Integer postId);
}
