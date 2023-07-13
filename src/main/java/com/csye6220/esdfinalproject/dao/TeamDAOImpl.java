package com.csye6220.esdfinalproject.dao;

import com.csye6220.esdfinalproject.model.Team;
import com.csye6220.esdfinalproject.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TeamDAOImpl implements TeamDAO {

    private SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    @Override
    public List<Team> getAllTeams() {

        try(Session session = sessionFactory.openSession()){
            Query<Team> query = session.createQuery("from Team", Team.class);
            return query.list();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void saveTeam(Team team) {
        try(Session session = sessionFactory.openSession()){
            Transaction transaction = session.getTransaction();
            transaction.begin();
            session.persist(team);
            transaction.commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateTeam(Team team) {
        try(Session session = sessionFactory.openSession()){
            Transaction transaction = session.getTransaction();
            transaction.begin();
            session.merge(team);
            transaction.commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTeam(Team team) {
        try(Session session = sessionFactory.openSession()){
            Transaction transaction = session.getTransaction();
            transaction.begin();
            session.delete(team);
            transaction.commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Team getTeamById(Long id) {
        try(Session session = sessionFactory.openSession()){
            Team team = session.get(Team.class, id);
            return team;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
