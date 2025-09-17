package edu.ijse.DAO.custom;

import edu.ijse.DAO.CrudDAO;
import edu.ijse.entity.User;
import org.hibernate.Session;

public interface UserDAO extends CrudDAO<User> {
    User findUserByEmail(String email, Session session);

    User findUserByName(String name,Session session);
    
    
}
