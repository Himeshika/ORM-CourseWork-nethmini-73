package edu.ijse.DAO.custom.impl;

import edu.ijse.DAO.custom.UserDAO;
import edu.ijse.config.FactoryConfiguration;
import edu.ijse.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    private FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();


    public boolean save(User user,Session session) {
        try {
            session.persist(user);
            return true;
        }catch(Exception e){
            throw e;
        }
    }

    @Override
    public boolean update(User user,Session session) {

        try {
            session.merge(user);

            return true;
        } catch (Exception e) {

            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        Session session = factoryConfiguration.getSession();
        Transaction tx = session.beginTransaction();
        try {
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
            }
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public List<User> getAll() {
        Session session = factoryConfiguration.getSession();
        Transaction tx = session.beginTransaction();
        try {
            List<User> users = session.createQuery("from User", User.class).list();
            tx.commit();
            return users;
        } catch (Exception e) {
            tx.rollback();
            return Collections.emptyList();
        } finally {
            session.close();
        }
    }

    @Override
    public User findById(int id,Session session) {
        return session.get(User.class, id);
    }

    @Override
    public User findUserByEmail(String email,Session session) {
       try {
           User user = (User) session.createQuery("from User where email = :email").setParameter("email", email).uniqueResult();
           return user;
       }catch(Exception e){
           throw e;
       }
    }

    @Override
    public User findUserByName(String name,Session session) {
        try {
            User user = (User) session.createQuery("from User where username = :name").setParameter("name",name).uniqueResult();
            return user;
        }catch(Exception e){
            throw e;
        }
    }
}
