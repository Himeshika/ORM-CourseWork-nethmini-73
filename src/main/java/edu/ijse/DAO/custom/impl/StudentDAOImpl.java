package edu.ijse.DAO.custom.impl;

import edu.ijse.DAO.custom.StudentDAO;
import edu.ijse.config.FactoryConfiguration;
import edu.ijse.entity.Student;
import edu.ijse.exception.NotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.List;

public class StudentDAOImpl implements StudentDAO {

    private FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public boolean save(Student student,Session session) {
        try {
            session.persist(student);// session, st trx
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public boolean update(Student student,Session session) {

        try {
            session.merge(student);

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
            Student student = session.get(Student.class, id);
            if (student != null) {
                session.remove(student);
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
    public List<Student> getAll() {
        Session session = factoryConfiguration.getSession();
        Transaction tx = session.beginTransaction();
        try {
            List<Student> students = session.createQuery("from Student", Student.class).list();
            tx.commit();
            return students;
        } catch (Exception e) {
            tx.rollback();
            return Collections.emptyList();
        } finally {
            session.close();
        }
    }

    @Override
    public Student findById(int id,Session session) {
            Student student=session.get(Student.class, id);
           if (student == null) {
            throw new NotFoundException("Student not found with ID: " + id);
            }
            return student;
    }

    @Override
    public boolean checkStudent(String email,Session session) {
        try {
            Student existingStudent = session.createQuery(
                            "FROM Student s WHERE s.studentEmail = :email", Student.class)
                    .setParameter("email", email)
                    .uniqueResult();

            return existingStudent != null;

        }catch (Exception e){
            return false;
        }
    }

    public Student getStudentByEmail(String email, Session session) {
        return session.createQuery(
                        "FROM Student s WHERE s.studentEmail = :email", Student.class)
                .setParameter("email", email)
                .uniqueResult();
    }

    public Student findSTById(int id,Session session) {
        Student student=session.get(Student.class, id);
        if (student == null) {
            throw new NotFoundException("Student not found with ID: " + id);
        }
        return student;
    }


}
