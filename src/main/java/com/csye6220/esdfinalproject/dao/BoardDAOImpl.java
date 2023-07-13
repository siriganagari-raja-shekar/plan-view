package com.csye6220.esdfinalproject.dao;

import com.csye6220.esdfinalproject.model.Board;
import com.csye6220.esdfinalproject.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BoardDAOImpl implements BoardDAO {
    private SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();

    @Override
    public void save(Board board) {
        try(Session session = sessionFactory.openSession()){
            Transaction transaction = session.getTransaction();
            transaction.begin();
            session.persist(board);
            transaction.commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void update(Board board) {
        try(Session session = sessionFactory.openSession()){
            Transaction transaction = session.getTransaction();
            transaction.begin();
            session.merge(board);
            transaction.commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Board board) {
        try(Session session = sessionFactory.openSession()){
            Transaction transaction = session.getTransaction();
            transaction.begin();
            session.remove(board);
            transaction.commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Board getBoardById(long id) {
        try(Session session = sessionFactory.openSession()){
            Board board = session.get(Board.class, id);
            return board;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Board> getAllBoards() {

        try(Session session = sessionFactory.openSession()){
            List<Board> boardList = session.createQuery("from Board").list();
            return boardList;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}

