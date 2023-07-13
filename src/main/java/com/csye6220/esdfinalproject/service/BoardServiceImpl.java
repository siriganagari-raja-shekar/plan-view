package com.csye6220.esdfinalproject.service;

import com.csye6220.esdfinalproject.dao.BoardDAO;
import com.csye6220.esdfinalproject.model.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardDAO boardDAO;
    @Override
    public void save(Board board) {
        this.boardDAO.save(board);
    }

    @Override
    public void update(Board board) {
        this.boardDAO.update(board);
    }

    @Override
    public void delete(Board board) {
        this.boardDAO.delete(board);
    }

    @Override
    public Board getBoardById(long id) {
        return this.boardDAO.getBoardById(id);
    }

    @Override
    public List<Board> getAllBoards() {
        return this.boardDAO.getAllBoards();
    }
}

