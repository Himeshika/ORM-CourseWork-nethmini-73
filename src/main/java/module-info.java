module orm.coursework.himeshika {

    requires jakarta.persistence;
    requires org.hibernate.orm.core;


    requires static lombok;


    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    requires bcrypt;
    requires java.naming;


    exports edu.ijse.entity;
    exports edu.ijse.dto;
    exports edu.ijse.DAO;
    exports edu.ijse.DAO.custom;
    exports edu.ijse.BO.custom;
    exports edu.ijse.config;
    exports edu.ijse.controller;


    opens edu.ijse.tm to javafx.base;

    opens edu.ijse.entity to org.hibernate.orm.core, jakarta.persistence;
    opens edu.ijse to javafx.fxml, javafx.graphics;
    opens edu.ijse.controller to javafx.fxml;
}
