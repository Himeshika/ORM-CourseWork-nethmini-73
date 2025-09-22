package edu.ijse.BO.custom.impl;

import at.favre.lib.crypto.bcrypt.BCrypt;
import edu.ijse.BO.custom.UserBO;
import edu.ijse.DAO.DAOFactory;
import edu.ijse.DAO.custom.UserDAO;
import edu.ijse.config.FactoryConfiguration;
import edu.ijse.dto.UserDto;
import edu.ijse.entity.User;
import edu.ijse.exception.DuplicateEntryException;
import edu.ijse.exception.InvalidPasswordException;
import edu.ijse.exception.InvalidUserNameException;
import edu.ijse.util.RegExChecker;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.stream.Collectors;

public class UserBOImpl implements UserBO {
    private UserDAO userDAO=(UserDAO) DAOFactory.getDAO(DAOFactory.DAOTypes.USER);
    @Override
    public boolean saveUser(UserDto user) {

        if (!RegExChecker.isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (!RegExChecker.isValidName(user.getUsername())) {
            throw new IllegalArgumentException("Invalid username format");
        }

        Session session= FactoryConfiguration.getInstance().getSession();
        Transaction tx= session.beginTransaction();
        try {
            User existing = userDAO.findUserByEmail(user.getEmail(),session);
            if (existing != null) {
                throw new DuplicateEntryException("User already exists");
            }

            String hashedPassword= BCrypt.withDefaults().hashToString(12,user.getPassword().toCharArray());

            User newUser = new User();
            newUser.setEmail(user.getEmail());
            newUser.setPassword(hashedPassword);
            newUser.setRole(user.getRole());
            newUser.setUsername(user.getUsername());
            newUser.setActive(true);

            boolean userSaved=userDAO.save(newUser,session);
            tx.commit();
            return userSaved;

        }catch(Exception e){
            tx.rollback();
            throw e;
        }
    }

    @Override
    public boolean updateUser(UserDto user) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try {
            User existing = userDAO.findById(user.getUserId(), session);
            if (existing == null) {
                throw new IllegalArgumentException("User not found with ID: " + user.getUserId());
            }

            if (user.getEmail() != null && RegExChecker.isValidEmail(user.getEmail())) {
                existing.setEmail(user.getEmail());
            }
            if (user.getUsername() != null && RegExChecker.isValidName(user.getUsername())) {
                existing.setUsername(user.getUsername());
            }
            if (user.getPassword() != null && !user.getPassword().isBlank()) {
                String hashedPassword = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
                existing.setPassword(hashedPassword);
            }
            if (user.getRole() != null) {
                existing.setRole(user.getRole());
            }
            existing.setActive(user.isActive());

            boolean updated = userDAO.update(existing, session);
            tx.commit();
            return updated;

        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean deleteUser(int id) {
        return userDAO.delete(id);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userDAO.getAll();
        return users.stream().map(u -> new UserDto(
                u.getUserId(),
                u.getUsername(),
                null,
                u.getRole(),
                u.getEmail(),
                u.isActive()
        )).collect(Collectors.toList());
    }

    public UserDto login(String username, String password) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try {
            User exist=userDAO.findUserByName(username,session);
            if(exist==null){
                throw new InvalidUserNameException("Invalid username");
            }

            boolean passwordMatch=BCrypt.verifyer().verify(password.toCharArray(),exist.getPassword()).verified;

            if(!passwordMatch){
                throw new InvalidPasswordException("Invalid password");
            }

            return new UserDto(exist.getUserId(),exist.getUsername(),null,exist.getRole(),exist.getEmail(),exist.isActive());
        } catch (Exception e) {
            throw e;
        }
    }
}
