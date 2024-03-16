package com.mountblue.blogapp.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "title")
    private String title;
    @Column(name = "excerpt")
    private String excerpt;
    @Column(name = "content")
    private String content;
    @ManyToOne
    @JoinColumn(name = "author")
    private User author;
    @Column(name = "published_at")
    private LocalDateTime publishedAt;
    @Column(name = "is_published")
    private Boolean isPublished;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private List<Comment> comments;
    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinTable(name = "post_tags",joinColumns = @JoinColumn(name = "post_id"),inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", excerpt='" + excerpt + '\'' +
                ", content='" + content + '\'' +
                ", author=" + author +
                ", publishedAt=" + publishedAt +
                ", isPublished=" + isPublished +
                ", comments=" + comments +
                ", tags=" + tags +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public void addComment(Comment comment)
    {
        if(comments==null)
        {
            comments = new ArrayList<>();
        }
        comments.add(comment);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Boolean getPublished() {
        return isPublished;
    }

    public void setPublished(Boolean published) {
        isPublished = published;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getTags() {
        StringBuilder tagsString = new StringBuilder();
        if(this.tags==null)
        {
            return null;
        }
        for(Tag tag:this.tags)
        {
            tagsString.append(tag.getName());
            tagsString.append(",");
        }
        tagsString.delete(tagsString.length()-1,tagsString.length()-1);
        return tagsString.toString();
    }

    public void setTags(String tag) {
        List<String> tagList = List.of(tag.split(","));
        List<Tag> tags = new ArrayList<>();
        for(String tagName:tagList)
        {
            tags.add(new Tag(tagName));
        }
        this.tags = tags;
    }

    public void setTagsList(List<Tag> tags)
    {
        this.tags = null;
        this.tags = tags;
    }
    public List<Tag> getTagList()
    {
        return tags;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
