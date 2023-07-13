package com.csye6220.esdfinalproject.model;

import jakarta.persistence.*;

@Entity
@Table(name = "columns")
public class Column implements Comparable<Column>{

    @Id
    @GeneratedValue
    private Long id;

    @jakarta.persistence.Column(name = "status")
    private String status;

    @jakarta.persistence.Column(name = "column_order")
    private Integer columnOrder;
    @jakarta.persistence.Column(name = "cards_limit")
    private Integer cardsLimit;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "board_id")
    private Board board;

    public Column() {
    }

    public Column(String status, Integer columnOrder, Integer cardsLimit, Board board) {
        this.status = status;
        this.columnOrder = columnOrder;
        this.cardsLimit = cardsLimit;
        this.board = board;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getColumnOrder() {
        return columnOrder;
    }

    public void setColumnOrder(Integer columnOrder) {
        this.columnOrder = columnOrder;
    }

    public Integer getCardsLimit() {
        return cardsLimit;
    }

    public void setCardsLimit(Integer cardsLimit) {
        this.cardsLimit = cardsLimit;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public int compareTo(Column o) {
        return Integer.compare(columnOrder, o.getColumnOrder());
    }
}
