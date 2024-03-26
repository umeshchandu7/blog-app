package com.mountblue.blogapp.Repository;

import com.mountblue.blogapp.Entity.Post;
import com.mountblue.blogapp.Entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag,Integer> {
    Optional<Tag> findDistinctByName(String name);
    Optional<Tag> findByName(String name);
}
