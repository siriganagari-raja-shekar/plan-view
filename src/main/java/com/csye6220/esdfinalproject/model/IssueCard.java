package com.csye6220.esdfinalproject.model;

import jakarta.persistence.Column;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class IssueCard extends Card{

    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @Column(name = "description")
    private String description;

    @Column(name = "priority")
    private String priority;

    @Column(name = "severity")
    private String severity;

    public IssueCard(){
    }

    public IssueCard(String status, User createdBy, LocalDateTime timeCreated, LocalDateTime lastUpdated, Board board, String title, User assignedTo, String description, String priority, String severity) {
        super(status, createdBy, timeCreated, lastUpdated, board);
        this.title = title;
        this.assignedTo = assignedTo;
        this.description = description;
        this.priority = priority;
        this.severity = severity;
    }

    public IssueCard(String status, User createdBy, Board board, String title, User assignedTo, String description, String priority, String severity) {
        super(status, createdBy, board);
        this.title = title;
        this.assignedTo = assignedTo;
        this.description = description;
        this.priority = priority;
        this.severity = severity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

}
