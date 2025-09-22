package edu.ijse.BO;

import edu.ijse.BO.custom.impl.*;

public class BOFactory {

    private static BOFactory boFactory;

    private BOFactory() {
    }

    public static BOFactory getInstance() {
        if (boFactory == null) {
            boFactory = new BOFactory();
        }
        return boFactory;
    }

    public enum BOTypes {
        COURSE, INSTRUCTOR, LESSONS, PAYMENT, STUDENT, USER
    }

    public SuperBO getBO(BOTypes boTypes) {
        switch (boTypes) {
            case COURSE:
                return new CourseBOImpl();
            case INSTRUCTOR:
                return new InstructorBOImpl();
            case LESSONS:
                return new LessonsBOImpl();
            case PAYMENT:
                return new PaymentBOImpl();
            case STUDENT:
                return new StudentBOImpl();
            case USER:
                return new UserBOImpl();
            default:
                return null;
        }
    }
}
