package edu.ijse.controller;

import edu.ijse.BO.BOFactory;
import edu.ijse.BO.custom.InstructorBO;
import edu.ijse.dto.InstructorDto;
import edu.ijse.dto.UserDto;
import edu.ijse.tm.InstructorTM;
import edu.ijse.util.AlertHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class InstructorManagementPageController implements Initializable,SuperController {

    private UserDto logedUser;
    @FXML
    private AnchorPane apInstructorPage;

    @FXML
    private Button btnDeleteInstructor, btnSaveInstructor, btnUpdateInstructor;

    @FXML
    private TableColumn<InstructorTM, String> colAvailability;

    @FXML
    private TableColumn<InstructorTM, String> colInstructorEmail;

    @FXML
    private TableColumn<InstructorTM, Integer> colInstructorID;

    @FXML
    private TableColumn<InstructorTM, String> colInstructorName;

    @FXML
    private ComboBox<String> comboAvailability;

    @FXML
    private TableView<InstructorTM> tblInstructors;

    @FXML
    private TextField txtInstructorEmail, txtInstructorName;

    private final InstructorBO instructorBO =
            (InstructorBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.INSTRUCTOR);

    private final ObservableList<InstructorTM> instructorTMS = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        loadAvailability();
        loadInstructors();
        btnDeleteInstructor.setDisable(true);
        btnUpdateInstructor.setDisable(true);

    }

    public void setUser(UserDto loggedUser) {
        this.logedUser = loggedUser;
    }

    private void setupTable() {
        colInstructorID.setCellValueFactory(new PropertyValueFactory<>("instructorID"));
        colInstructorName.setCellValueFactory(new PropertyValueFactory<>("instructorName"));
        colInstructorEmail.setCellValueFactory(new PropertyValueFactory<>("instructorEmail"));
        colAvailability.setCellValueFactory(new PropertyValueFactory<>("availability"));

        tblInstructors.setItems(instructorTMS);


       tblInstructors.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection)->{
           if(newSelection!=null){
               fillForm(newSelection);
               btnSaveInstructor.setDisable(true);
               btnDeleteInstructor.setDisable(false);
               btnUpdateInstructor.setDisable(false);
           }else {
               clearForm();
           }
       });
    }

    private void loadAvailability() {
        comboAvailability.setItems(FXCollections.observableArrayList("AVAILABLE", "UNAVAILABLE", "ON_LEAVE"));
    }

    private void loadInstructors() {
        instructorTMS.clear();
        try {
            List<InstructorDto> instructors = instructorBO.getAllInstructors();
            for (InstructorDto dto : instructors) {
                instructorTMS.add(new InstructorTM(
                        dto.getInstructorID(),
                        dto.getInstructorName(),
                        dto.getInstructorEmail(),
                        dto.getInstructorAvailability(),
                        dto.getLessons()
                ));
            }
        } catch (Exception e) {
            AlertHelper.showError("Error loading instructors", e.getMessage());
        }
    }

    private void fillForm(InstructorTM instructor) {
        txtInstructorName.setText(instructor.getInstructorName());
        txtInstructorEmail.setText(instructor.getInstructorEmail());
        comboAvailability.setValue(instructor.getAvailability());
    }


    @FXML
    void btnSaveInstructor(ActionEvent event) {
        try {
            String name = txtInstructorName.getText();
            String email = txtInstructorEmail.getText();
            String availability = comboAvailability.getValue();

            if (name.isEmpty() || email.isEmpty() || availability == null) {
                AlertHelper.showError("Validation error", "All fields are required");
                return;
            }

            InstructorDto dto = new InstructorDto(0, name, email, availability);

            if (instructorBO.saveInstructor(dto)) {
                AlertHelper.showInfo("Success", "Instructor saved successfully!");
                loadInstructors();
                clearForm();
            }
        } catch (Exception e) {
            AlertHelper.showError("Error saving instructor", e.getMessage());
        }
    }

    @FXML
    void btnUpdateInstructor(ActionEvent event) {
        InstructorTM selected = tblInstructors.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("Selection error", "Select an instructor first");
            return;
        }

        try {
            String name = txtInstructorName.getText();
            String email = txtInstructorEmail.getText();
            String availability = comboAvailability.getValue();

            if (name.isEmpty() || email.isEmpty() || availability == null) {
                AlertHelper.showError("Validation error", "All fields are required");
                return;
            }

            InstructorDto dto = new InstructorDto(
                    selected.getInstructorID(),
                    name,
                    email,
                    availability
            );

            if (instructorBO.updateInstructor(dto)) {
                AlertHelper.showInfo("Success", "Instructor updated successfully!");
                loadInstructors();
                clearForm();
            }
        } catch (Exception e) {
            AlertHelper.showError("Error updating instructor", e.getMessage());
        }
    }

    @FXML
    void btnDeleteInstructor(ActionEvent event) {
        InstructorTM selected = tblInstructors.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("Selection error", "Select an instructor first");
            return;
        }

        try {
            if (instructorBO.deleteInstructor(selected.getInstructorID())) {
                AlertHelper.showInfo("Success", "Instructor deleted successfully!");
                loadInstructors();
                clearForm();
            }
        } catch (Exception e) {
            AlertHelper.showError("Error deleting instructor", e.getMessage());
        }
    }


    private void clearForm() {
        txtInstructorName.clear();
        txtInstructorEmail.clear();
        comboAvailability.getSelectionModel().clearSelection();
        tblInstructors.getSelectionModel().clearSelection();
    }

    @FXML
    void btnGoBack(ActionEvent event) {
        try {
            apInstructorPage.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Dashboard.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            DashboardManagementPage controller = (DashboardManagementPage) loader.getController();
            controller.setUser(logedUser);
            apInstructorPage.getChildren().add(page);
        } catch (IOException e) {
            AlertHelper.showError("Error loading Dashboard", e.getMessage());
        }

    }

}
