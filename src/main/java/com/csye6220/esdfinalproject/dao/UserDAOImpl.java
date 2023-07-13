package com.csye6220.esdfinalproject.dao;

import com.csye6220.esdfinalproject.model.User;
import com.csye6220.esdfinalproject.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class UserDAOImpl implements UserDAO{

    private SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();

    @Override
    public void save(User user) {
        try(Session session = sessionFactory.openSession()){
            Transaction transaction = session.getTransaction();
            transaction.begin();
            session.persist(user);
            transaction.commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void update(User user) {
        try(Session session = sessionFactory.openSession()){
            Transaction transaction = session.getTransaction();
            transaction.begin();
            session.merge(user);
            transaction.commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void delete(User user) {
        if(user == null)
            return;
        try(Session session = sessionFactory.openSession()){
            Transaction transaction = session.getTransaction();
            transaction.begin();
            session.remove(user);
            transaction.commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(Long id) {
        try(Session session = sessionFactory.openSession()){
            Transaction transaction = session.getTransaction();
            transaction.begin();
            String queryString = "delete from User where id= :id";
            Query query = session.createQuery(queryString);
            query.setParameter("id",id);
            query.executeUpdate();
            transaction.commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteByEmail(String email) {
        try(Session session = sessionFactory.openSession()){
            Transaction transaction = session.getTransaction();
            transaction.begin();
            String queryString = "delete from User where email= :email";
            Query query = session.createQuery(queryString);
            query.setParameter("email",email);
            query.executeUpdate();
            transaction.commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public User getById(Long id) {
        try(Session session = sessionFactory.openSession()) {
            String queryString = "FROM User where id="+id;
            Query query = session.createQuery(queryString, User.class);
            List<User> users = query.list();
            return users.size() == 1 ? users.get(0) : null;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getByEmail(String email) {
        try(Session session = sessionFactory.openSession()) {
            String queryString = "FROM User u where u.email=\""+email+"\"";
            Query query = session.createQuery(queryString, User.class);
            List<User> users = query.list();
            return users.size() == 1 ? users.get(0) : null;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        try(Session session = sessionFactory.openSession()) {
            String queryString = "FROM User";
            Query query = session.createQuery(queryString, User.class);
            List<User> users = query.list();
            return users;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private Session getSession(){
        ApplicationContext context = new AnnotationConfigApplicationContext(this.getClass());
        System.out.println("Session factory: "+context.getBean("sessionFactory"));
        sessionFactory = (SessionFactory) context.getBean("sessionFactory");
        return sessionFactory.openSession();
    }
}
