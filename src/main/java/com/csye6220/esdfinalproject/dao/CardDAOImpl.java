package com.csye6220.esdfinalproject.dao;

import com.csye6220.esdfinalproject.model.Card;
import com.csye6220.esdfinalproject.model.Comment;
import com.csye6220.esdfinalproject.model.IssueCard;
import com.csye6220.esdfinalproject.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CardDAOImpl implements CardDAO {

    private SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();

    @Override
    public void saveCard(Card card) {
        try(Session session = sessionFactory.openSession()){
            Transaction transaction = session.getTransaction();
            transaction.begin();
            session.persist(card);
            transaction.commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateCard(Card card) {
        try(Session session = sessionFactory.openSession()){
            Transaction transaction = session.getTransaction();
            transaction.begin();
            session.merge(card);
            transaction.commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCard(Card card) {
        try(Session session = sessionFactory.openSession()){
            Transaction transaction = session.getTransaction();
            transaction.begin();
            session.remove(card);
            transaction.commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Card getCardById(long id) {
        try(Session session = sessionFactory.openSession()){
            Card card = session.get(Card.class, id);
            return card;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Comment> getCommentsByCardId(long id) {
        try(Session session = sessionFactory.openSession()){
            String queryString = "FROM Comment c WHERE c.card.id = :cardId";
            Query query = session.createQuery(queryString, Comment.class);
            query.setParameter("cardId", id);
            return query.list();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<IssueCard> getCardsAssignedToUser(long userId) {
        try(Session session = sessionFactory.openSession()){
            String queryString = "FROM IssueCard i WHERE i.assignedTo.id = :userId";
            Query query = session.createQuery(queryString, IssueCard.class);
            query.setParameter("userId", userId);
            return query.list();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public long getAllCardsCount() {
        try(Session session = sessionFactory.openSession()){
            Query queryOne = session.createQuery("SELECT count(c) FROM IssueCard c", Long.class);
            Query queryTwo = session.createQuery("SELECT count(c) FROM NoteCard c", Long.class);
            return (Long)queryOne.list().get(0) + (Long)queryTwo.list().get(0);
        }
        catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

}

