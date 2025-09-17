package edu.ijse.DAO;

import org.hibernate.Session;

import java.util.List;

public interface CrudDAO <T> extends SuperDAO{

    boolean save(T t,Session session) ;
    boolean update(T t, Session session);
    boolean delete(int id);
    List<T> getAll();
    T findById(int id,Session session);
}
