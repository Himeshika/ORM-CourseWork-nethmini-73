package edu.ijse.DAO;

import edu.ijse.DAO.custom.impl.*;

public class DAOFactory {

    private static DAOFactory daoFactory;

    private DAOFactory() {

    }

    private static DAOFactory getInstance() {
        if (daoFactory == null) {
            daoFactory = new DAOFactory();
        }
        return daoFactory;
    }

    public enum DAOTypes{
        COURSE,INSTRUCTOR,LESSONS,PAYMENT,STUDENT,USER
    }

    public static SuperDAO getDAO(DAOTypes daoTypes){
        switch (daoTypes){
            case COURSE:
                return new CourseDAOImpl();
            case INSTRUCTOR:
                return new InstructorDAOImpl();
            case LESSONS:
                return new LessonsDAOImpl();
            case PAYMENT:
                return new PaymentDAOImpl();
            case STUDENT:
                return new StudentDAOImpl();
            case USER:
                return new UserDAOImpl();
            default:
                return null;
        }
    }
}
