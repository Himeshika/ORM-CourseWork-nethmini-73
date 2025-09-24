module orm.coursework.himeshika {
    // Hibernate / JPA
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    // Lombok (compile-time only)
    requires static lombok;

    // JavaFX
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    // Other libs
    requires bcrypt;
    requires java.naming;

    // Export packages (accessible to other modules)
    exports edu.ijse.entity;
    exports edu.ijse.dto;
    exports edu.ijse.DAO;
    exports edu.ijse.DAO.custom;
    exports edu.ijse.BO.custom;
    exports edu.ijse.config;
    exports edu.ijse.controller;

    // For JavaFX table models
    opens edu.ijse.tm to javafx.base;

    // Open packages (needed for reflection by Hibernate / JavaFX)
    opens edu.ijse.entity to org.hibernate.orm.core, jakarta.persistence;
    opens edu.ijse to javafx.fxml, javafx.graphics;
    opens edu.ijse.controller to javafx.fxml;
}
