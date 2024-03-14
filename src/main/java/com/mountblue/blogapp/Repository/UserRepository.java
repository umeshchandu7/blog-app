package com.mountblue.blogapp.Repository;

import com.mountblue.blogapp.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
}
