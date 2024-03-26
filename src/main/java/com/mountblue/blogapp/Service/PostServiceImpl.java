package com.mountblue.blogapp.Service;

import com.mountblue.blogapp.Entity.Post;
import com.mountblue.blogapp.Entity.Tag;
import com.mountblue.blogapp.Entity.User;
import com.mountblue.blogapp.Repository.CommentRepository;
import com.mountblue.blogapp.Repository.PostRepository;
import com.mountblue.blogapp.Repository.TagRepository;
import com.mountblue.blogapp.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public Post savePost(Post post) {
        return postRepository.save(post);
    }


    @Override
    public User getUser() {
        return userRepository.findById(3).get();
    }



    @Override
    public List<Post> getSortedList(String direction) {
        return postRepository.getSortedPosts(direction);
    }

    @Override
    public List<Post> getListByTitleOrContentOrTagOrAuthor(String search) {
        return postRepository.searchWithAuthorOrContentOrTitleOrTags(search);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Page<Post> paginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1,pageSize);
        return postRepository.findAll(pageable);
    }



    @Override
    public Page<Post> filtering(List<String> authors, List<String> tags, LocalDateTime startTime, LocalDateTime endTime,String search, Integer pageNo,String direction) {
        if (startTime == null) {
            startTime = postRepository.findOldestPost().getPublishedAt();
        }
        if (endTime == null) {
            endTime = LocalDateTime.now();
        }
        Pageable pageable = null;
        if(direction.equals("ASC"))
        {
            pageable = PageRequest.of(pageNo-1,2,Sort.by("publishedAt").ascending());
        }
        else
        {
            pageable = PageRequest.of(pageNo-1,2,Sort.by("publishedAt").descending());
        }
        return postRepository.filteringPostsonSearch(authors, tags, startTime, endTime, search, pageable);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public void deletePostById(Integer postId) {
         postRepository.deleteById(postId);
    }


    @Override
    public void deletePost(Post post) {
        postRepository.delete(post);
    }


}
