package com.csye6220.esdfinalproject.model;

import jakarta.persistence.Column;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class NoteCard extends Card{

    @Column(name = "text")
    private String text;

    @Column(name = "votes")
    private Integer votes;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "card_votes",
            joinColumns = @JoinColumn(name = "card_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> votedBy;

    public NoteCard(){
    }

    public NoteCard(String status, User createdBy, LocalDateTime timeCreated, LocalDateTime lastUpdated, Board board, String text, Integer votes, Set<User> votedBy) {
        super(status, createdBy, timeCreated, lastUpdated, board);
        this.text = text;
        this.votes = votes;
        this.votedBy = votedBy;
    }

    public NoteCard(String status, User createdBy, Board board, String text, Integer votes) {
        super(status, createdBy, board);
        this.text = text;
        this.votes = votes;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public Set<User> getVotedBy() {
        return votedBy;
    }

    public void setVotedBy(Set<User> votedBy) {
        this.votedBy = votedBy;
    }

    @Override
    public String toString() {
        return "NoteCard{" +
                "id="+this.getId()+" "+
                "text='" + text + '\'' +
                ", votes=" + votes +
                ", votedBy=" + votedBy +
                '}';
    }
}
