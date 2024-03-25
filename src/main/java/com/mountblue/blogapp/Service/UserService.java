package com.mountblue.blogapp.Service;

import com.mountblue.blogapp.Entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    public User findByUserName(String userName);
    public void saveUser(User user);
}
