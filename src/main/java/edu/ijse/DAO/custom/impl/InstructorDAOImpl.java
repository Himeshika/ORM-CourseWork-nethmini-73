package edu.ijse.DAO.custom.impl;

import edu.ijse.DAO.custom.InstructorDAO;
import edu.ijse.config.FactoryConfiguration;
import edu.ijse.entity.Instructor;
import edu.ijse.exception.NotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.List;

public class InstructorDAOImpl implements InstructorDAO {

    private FactoryConfiguration factoryConfiguration=FactoryConfiguration.getInstance();
    @Override
    public boolean save(Instructor instructor,Session session) {

        try {
            session.persist(instructor);
            return true;
        }catch (Exception e){
            throw e;
        }

    }

    @Override
    public boolean update(Instructor instructor,Session session) {

        try {
            session.merge(instructor);

            return true;
        }catch (Exception e){

            return false;
        }
    }

    @Override
    public boolean delete(int id) {
       Session session=factoryConfiguration.getSession();
       Transaction tx=session.beginTransaction();
       try {
            Instructor instructor=session.get(Instructor.class,id);
            session.remove(instructor);
            tx.commit();
            return true;
       }catch (Exception e){
           tx.rollback();
           return false;
       }finally {
           session.close();
       }
    }

    @Override
    public List<Instructor> getAll() {
        Session session=factoryConfiguration.getSession();
        Transaction tx=session.beginTransaction();
        try{
            List<Instructor> instructors=session.createQuery("from Instructor",Instructor.class).list();
            tx.commit();
            return instructors;
        }catch (Exception e){
            tx.rollback();
            return Collections.emptyList();
        }finally {
            session.close();
        }
    }

    @Override
    public Instructor findById(int id,Session session) {
        Instructor instructor=session.get(Instructor.class,id);
        if(instructor==null){
            throw new NotFoundException("Instructor not found with ID: " + id);
        }
        return instructor;
    }

    public Instructor findINsById(int id,Session session) {
        Instructor instructor=session.get(Instructor.class,id);
        if(instructor==null){
            throw new NotFoundException("Instructor not found with ID: " + id);
        }
        return instructor;
    }

    @Override
    public boolean findInsByEmail(String email, Session session) {
        Instructor instructor=session.createQuery("from Instructor WHERE instructorEmail=:email",Instructor.class).setParameter("email",email).uniqueResult();
        if (instructor==null){
            return false;
        }else {
            return true;
        }
    }
}
