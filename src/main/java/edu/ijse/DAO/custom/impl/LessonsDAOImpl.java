package edu.ijse.DAO.custom.impl;

import edu.ijse.DAO.custom.LessonsDAO;
import edu.ijse.config.FactoryConfiguration;
import edu.ijse.entity.Lessons;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

public class LessonsDAOImpl implements LessonsDAO {

    private FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public boolean save(Lessons lessons,Session session) {
       try {
           factoryConfiguration.getCurrentSession().persist(lessons);
           return true;
       }catch (Exception e){
           return false;
       }
    }

    @Override
    public boolean update(Lessons lessons,Session session) {

        try {
            session.merge(lessons);

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
            Lessons lessons = session.get(Lessons.class, id);
            if (lessons != null) {
                session.remove(lessons);
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
    public List<Lessons> getAll() {
        Session session = factoryConfiguration.getSession();
        Transaction tx = session.beginTransaction();
        try {
            List<Lessons> lessonsList = session.createQuery("from Lessons", Lessons.class).list();
            tx.commit();
            return lessonsList;
        } catch (Exception e) {
            tx.rollback();
            return Collections.emptyList();
        } finally {
            session.close();
        }
    }

    @Override
    public Lessons findById(int id,Session session) {
       return session.get(Lessons.class, id);
    }

    @Override
    public boolean checkLessonOverlapsByInstructorID(int instructorId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        Session session = factoryConfiguration.getCurrentSession();
        try {


            String hql = "SELECT count(l) FROM Lessons l WHERE l.instructor.instructorID=:instructorId AND l.lessonDate=:date AND (\n" +
                    "        (l.lessonStartTime <= :endTime AND l.lessonEndTime >= :startTime)\n" +
                    "      )";

            Long count = session.createQuery(hql, Long.class).setParameter("instructorId", instructorId).setParameter("date", date).setParameter("startTime", startTime).setParameter("endTime", endTime).uniqueResult();

            return count != null && count > 0;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
