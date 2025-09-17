package edu.ijse.DAO.custom;

import edu.ijse.DAO.CrudDAO;
import edu.ijse.entity.Student;
import org.hibernate.Session;

public interface StudentDAO extends CrudDAO<Student> {

    boolean checkStudent(String email,Session session);

    Student getStudentByEmail(String email, Session session);

    Student findSTById(int id,Session session);
}
