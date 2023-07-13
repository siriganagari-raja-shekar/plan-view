package com.csye6220.esdfinalproject.model;

import jakarta.persistence.Column;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "cards")
@Inheritance(strategy = InheritanceType.JOINED)
public class Card {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "status")
    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @CreationTimestamp
    private LocalDateTime timeCreated;

    @UpdateTimestamp
    private LocalDateTime lastUpdated;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("timePosted DESC")
    @JoinColumn(name = "card_id")
    private List<Comment> comments;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "board_id")
    private Board board;
    public Card(){
    }


    public Card(String status, User createdBy, LocalDateTime timeCreated, LocalDateTime lastUpdated, Board board) {
        this.status = status;
        this.createdBy = createdBy;
        this.timeCreated = timeCreated;
        this.lastUpdated = lastUpdated;
        this.board = board;
    }

    public Card(Long id, String status, User createdBy, LocalDateTime timeCreated, LocalDateTime lastUpdated, List<Comment> comments, Board board) {
        this.id = id;
        this.status = status;
        this.createdBy = createdBy;
        this.timeCreated = timeCreated;
        this.lastUpdated = lastUpdated;
        this.comments = comments;
        this.board = board;
    }

    public Card(String status, User createdBy, Board board) {
        this.status = status;
        this.createdBy = createdBy;
        this.board = board;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(LocalDateTime timeCreated) {
        this.timeCreated = timeCreated;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", createdBy=" + createdBy +
                ", timeCreated=" + timeCreated +
                ", lastUpdated=" + lastUpdated +
                ", comments=" + comments +
                ", board=" + board +
                '}';
    }
}
