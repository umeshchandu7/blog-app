package com.mountblue.blogapp.Repository;

import com.mountblue.blogapp.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("SELECT DISTINCT e FROM Post e  ORDER BY " + "CASE WHEN :direction = 'ASC' THEN e.publishedAt END ASC, "
            + "CASE WHEN :direction = 'DESC' THEN e.publishedAt END DESC")
    List<Post> getSortedPosts(@Param("direction") String direction);

    @Query("SELECT DISTINCT p FROM Post p " +
            "JOIN p.author a " +
            "JOIN p.tags t " +
            "WHERE (:authors IS NULL OR a.name IN :authors) " +
            "AND (:tags IS NULL OR t.name IN :tags) " +
            "AND (p.publishedAt BETWEEN :startTime AND :endTime)")
    List<Post> filteringPosts(@Param("authors") List<String> authors,
                              @Param("tags") List<String> tags,
                              @Param("startTime") LocalDateTime startTime,
                              @Param("endTime") LocalDateTime endtime);

    @Query("SELECT p FROM Post p " +
            "WHERE p.isPublished = true " +
            "AND p.publishedAt = (SELECT MIN(p2.publishedAt) FROM Post p2 WHERE p2.isPublished = true)")
    Post findOldestPost();

    @Query("SELECT DISTINCT p FROM Post p " +
            "JOIN p.author a " +
            "JOIN p.tags t " +
            "WHERE (:authors IS NULL OR a.name IN :authors) " +
            "AND (:tags IS NULL OR t.name IN :tags) " +
            "AND (p.publishedAt BETWEEN :startTime AND :endTime) " +
            "AND (p.title LIKE %:search% OR p.content LIKE %:search% OR t.name LIKE %:search% OR a.name LIKE %:search% )")
    List<Post> filteringPostsonSearch(@Param("authors") List<String> authors,
                                      @Param("tags") List<String> tags,
                                      @Param("startTime") LocalDateTime startTime,
                                      @Param("endTime") LocalDateTime endtime,
                                      @Param("search") String search);

    @Query("SELECT DISTINCT p from Post p " + "JOIN p.tags t " +
            "WHERE p.author.name LIKE %:search% OR p.content LIKE %:search% OR p.title LIKE %:search% OR t.name LIKE %:search%")
    List<Post> searchWithAuthorOrContentOrTitleOrTags(@Param("search") String search);
}
