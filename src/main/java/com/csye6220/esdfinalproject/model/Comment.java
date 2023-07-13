package com.csye6220.esdfinalproject.model;

import jakarta.persistence.Column;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment implements Comparable<Comment>{

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User commentedUser;

    @CreationTimestamp
    private LocalDateTime timePosted;

    @UpdateTimestamp
    private LocalDateTime timeUpdated;

    @Column(name = "text")
    private String text;

    public Comment() {
    }

    public Comment(Long id, User commentedUser, LocalDateTime timePosted, LocalDateTime timeUpdated, String text) {
        this.id = id;
        this.commentedUser = commentedUser;
        this.timePosted = timePosted;
        this.timeUpdated = timeUpdated;
        this.text = text;
    }

    public Comment( User commentedUser, String text) {
        this.commentedUser = commentedUser;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getCommentedUser() {
        return commentedUser;
    }

    public void setCommentedUser(User commentedUser) {
        this.commentedUser = commentedUser;
    }

    public LocalDateTime getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(LocalDateTime timePosted) {
        this.timePosted = timePosted;
    }

    public LocalDateTime getTimeUpdated() {
        return timeUpdated;
    }

    public void setTimeUpdated(LocalDateTime timeUpdated) {
        this.timeUpdated = timeUpdated;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int compareTo(Comment o) {
        return -this.getTimePosted().compareTo(o.getTimePosted());
    }
}
