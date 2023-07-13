package com.csye6220.esdfinalproject.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "boards")
public class Board {

    @Id
    @GeneratedValue
    private Long id;

    @jakarta.persistence.Column(name = "name")
    private String name;

    @jakarta.persistence.Column(name = "board_type")
    private BoardType type;

    @jakarta.persistence.Column(name = "start_time")
    private LocalDateTime startTime;

    @jakarta.persistence.Column(name = "end_time")
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderColumn(name = "column_order")
    private List<Column> columns;

    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Card> cards;

    public Board() {
    }

    public Board(Long id, String name, BoardType type, Team team, User createdBy, List<Column> columns, Set<Card> cards) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.team = team;
        this.createdBy = createdBy;
        this.columns = columns;
        this.cards = cards;
    }

    public Board(String name, BoardType type, Team team, LocalDateTime startTime, LocalDateTime endTime, User createdBy) {
        this.name = name;
        this.type = type;
        this.team = team;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BoardType getType() {
        return type;
    }

    public void setType(BoardType type) {
        this.type = type;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
