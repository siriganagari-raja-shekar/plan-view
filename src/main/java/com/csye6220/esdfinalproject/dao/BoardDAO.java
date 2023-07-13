package com.csye6220.esdfinalproject.dao;

import com.csye6220.esdfinalproject.model.Board;

import java.util.List;

public interface BoardDAO {
    public void save(Board board);
    public void update(Board board);
    public void delete(Board board);
    public Board getBoardById(long id);
    public List<Board> getAllBoards();
}

