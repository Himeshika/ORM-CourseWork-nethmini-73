package edu.ijse.DAO.custom;

import edu.ijse.DAO.CrudDAO;
import edu.ijse.entity.Course;
import org.hibernate.Session;


public interface CourseDAO extends CrudDAO<Course> {
    Course getCourseById(int id,Session session);
    boolean getCourseByName(String name,Session session);
}
