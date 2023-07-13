package com.csye6220.esdfinalproject.service;

import com.csye6220.esdfinalproject.model.Board;

import java.util.List;

public interface BoardService {
    public void save(Board board);
    public void update(Board board);
    public void delete(Board board);
    public Board getBoardById(long id);
    public List<Board> getAllBoards();
}

