package edu.ijse.DAO.custom.impl;

import edu.ijse.DAO.custom.CourseDAO;
import edu.ijse.config.FactoryConfiguration;
import edu.ijse.entity.Course;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.List;

public class CourseDAOImpl implements CourseDAO {
    private FactoryConfiguration factoryConfiguration =FactoryConfiguration.getInstance();


    public boolean save(Course course,Session session) {
        try {
            session.persist(course);
            return true;
        }catch (Exception e){
            throw e;
        }

    }

    @Override
    public boolean update(Course course,Session session) {

        try {
            session.merge(course);

            return true;
        }catch (Exception e){
            return false;
        }

    }

    @Override
    public boolean delete(int id) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            Course course = (Course) session.get(Course.class, id);
            session.remove(course);
            transaction.commit();
            return true;
        }catch (Exception e){
            transaction.rollback();
            return false;
        }finally {
            session.close();
        }
    }

    @Override
    public List<Course> getAll() {
        Session session = factoryConfiguration.getSession();
        Transaction tx = session.beginTransaction();
        try {
            List<Course> list=session.createQuery("from Course",Course.class).list();
            tx.commit();
            return list;
        }catch (Exception e){
            tx.rollback();
            return Collections.emptyList();
        }finally {
            session.close();
        }

    }

    @Override
    public Course findById(int id,Session session) {
        Course course=session.get(Course.class, id);
        return course;
    }

    @Override
    public Course getCourseById(int id,Session session) {
        return session.get(Course.class, id);
    }

    @Override
    public boolean getCourseByName(String name, Session session) {
        Course course = session.createQuery(
                        "FROM Course c WHERE c.courseName = :name", Course.class)
                .setParameter("name", name)
                .uniqueResult();

        return course != null;
    }

}