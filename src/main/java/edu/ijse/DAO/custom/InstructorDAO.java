package edu.ijse.DAO.custom;

import edu.ijse.DAO.CrudDAO;
import edu.ijse.entity.Instructor;
import org.hibernate.Session;

public interface InstructorDAO extends CrudDAO<Instructor> {
    public Instructor findINsById(int id, Session session);
    boolean findInsByEmail(String email, Session session);
}
