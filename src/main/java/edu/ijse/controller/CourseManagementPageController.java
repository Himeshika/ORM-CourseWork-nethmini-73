package edu.ijse.controller;

import edu.ijse.BO.BOFactory;
import edu.ijse.BO.custom.CourseBO;
import edu.ijse.BO.custom.InstructorBO;
import edu.ijse.dto.CourseDto;
import edu.ijse.dto.InstructorDto;
import edu.ijse.dto.UserDto;
import edu.ijse.tm.CourseTM;
import edu.ijse.util.AlertHelper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CourseManagementPageController implements Initializable,SuperController {

    private UserDto logedUser;

    @FXML
    private AnchorPane apCoursePage;

    @FXML
    private Button btnAddInstructor, btnDeleteCourse, btnSaveCourse, btnUpdateCourse;

    @FXML
    private TableColumn<CourseTM, Integer> colCourseID;

    @FXML
    private TableColumn<CourseTM, String> colCourseName, colDuration, colInstructors;

    @FXML
    private TableColumn<CourseTM, BigDecimal> colFee;

    @FXML
    private ComboBox<InstructorDto> comboInstructor;

    @FXML
    private TableView<CourseTM> tblCourses;

    @FXML
    private TextField txtCourseName, txtDuration, txtFee;

    private final CourseBO courseBO = (CourseBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.COURSE);
    private final InstructorBO instructorBO = (InstructorBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.INSTRUCTOR);

    private ObservableList<CourseTM> courseTMS = FXCollections.observableArrayList();
    private ArrayList<InstructorDto> selectedInstructors = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        loadInstructors();
        loadCourses();
        btnDeleteCourse.setDisable(true);
        btnUpdateCourse.setDisable(true);
    }

    public void setUser(UserDto logedUser) {
        this.logedUser = logedUser;
    }

    private void setupTable() {
        colCourseID.setCellValueFactory(new PropertyValueFactory<>("courseID"));
        colCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("fee"));

        colInstructors.setCellValueFactory(data -> {
            List<InstructorDto> list = data.getValue().getInstructorList();
            if (list == null || list.isEmpty()) {
                return new SimpleStringProperty("");
            }

            StringBuilder names = new StringBuilder();
            for (InstructorDto instructor : list) {
                if (instructor.getInstructorName() != null) {
                    if (names.length() > 0) {
                        names.append(", ");
                    }
                    names.append(instructor.getInstructorName());
                }
            }

            return new SimpleStringProperty(names.toString());
        });

        tblCourses.setItems(courseTMS);
    }

    private void loadInstructors() {
        try {
            List<InstructorDto> instructors = instructorBO.getAllInstructors();
            comboInstructor.setItems(FXCollections.observableArrayList(instructors));
        } catch (Exception e) {
            AlertHelper.showError("Error loading instructors", e.getMessage());
        }
    }

    private void loadCourses() {
        courseTMS.clear();
        try {
            List<CourseDto> courses = courseBO.getAllCourses();
            for (CourseDto dto : courses) {
                courseTMS.add(new CourseTM(
                        dto.getCourseID(),
                        dto.getCourseName(),
                        dto.getDuration(),
                        dto.getFee(),
                        null,
                        dto.getInstructors()
                ));
            }
        } catch (Exception e) {
            AlertHelper.showError("Error loading courses", e.getMessage());
        }
    }

    @FXML
    void btnAddInstructor(ActionEvent event) {
        InstructorDto selected = comboInstructor.getValue();
        if (selected != null && !selectedInstructors.contains(selected)) {
            selectedInstructors.add(selected);
            AlertHelper.showInfo("Instructor added", selected.getInstructorName() + " added to course");
        }
    }

    @FXML
    void btnSaveCourse(ActionEvent event) {
        if (selectedInstructors.isEmpty()) {
            AlertHelper.showError("Selection error", "Add at least one instructor");
            return;
        }

        try {
            String name = txtCourseName.getText();
            String duration = txtDuration.getText();
            BigDecimal fee = new BigDecimal(txtFee.getText());

            CourseDto dto = new CourseDto();
            dto.setCourseName(name);
            dto.setDuration(duration);
            dto.setFee(fee);
            dto.setInstructors(new ArrayList<>(selectedInstructors));

            if (courseBO.saveCourse(dto, selectedInstructors)) {
                AlertHelper.showInfo("Success", "Course saved successfully!");
                loadCourses();
                clearForm();
                selectedInstructors.clear();
            }
        } catch (Exception e) {
            AlertHelper.showError("Error saving course", e.getMessage());
        }
    }

    @FXML
    void btnUpdateCourse(ActionEvent event) {
        CourseTM selected = tblCourses.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("Selection error", "Select a course first");
            return;
        }

        try {
            CourseDto dto = new CourseDto(
                    selected.getCourseID(),
                    txtCourseName.getText(),
                    txtDuration.getText(),
                    new BigDecimal(txtFee.getText())
            );
            dto.setInstructors(new ArrayList<>(selectedInstructors));

            if (courseBO.updateCourse(dto)) {
                AlertHelper.showInfo("Success", "Course updated successfully!");
                loadCourses();
                clearForm();
                selectedInstructors.clear();
            }
        } catch (Exception e) {
            AlertHelper.showError("Error updating course", e.getMessage());
        }
    }

    @FXML
    void btnDeleteCourse(ActionEvent event) {
        CourseTM selected = tblCourses.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("Selection error", "Select a course first");
            return;
        }

        try {
            if (courseBO.deleteCourse(selected.getCourseID())) {
                AlertHelper.showInfo("Success", "Course deleted successfully!");
                loadCourses();
                clearForm();
                selectedInstructors.clear();
            }
        } catch (Exception e) {
            AlertHelper.showError("Error deleting course", e.getMessage());
        }
    }

    private void clearForm() {
        txtCourseName.clear();
        txtDuration.clear();
        txtFee.clear();
        comboInstructor.getSelectionModel().clearSelection();
    }

    @FXML
    void btnGoBack(ActionEvent event) {
        try {
            apCoursePage.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Dashboard.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            DashboardManagementPage controller = (DashboardManagementPage) loader.getController();
            controller.setUser(logedUser);
            apCoursePage.getChildren().add(page);
        } catch (IOException e) {
            AlertHelper.showError("Error loading Dashboard", e.getMessage());
        }
    }


    public void tableonClick(MouseEvent mouseEvent) {
        CourseTM selected = tblCourses.getSelectionModel().getSelectedItem();
        if (selected != null) {

            txtCourseName.setText(selected.getCourseName());
            txtDuration.setText(selected.getDuration());
            txtFee.setText(selected.getFee().toString());


            selectedInstructors.clear();
            if (selected.getInstructorList() != null) {
                selectedInstructors.addAll(selected.getInstructorList());
            }

            if (!selectedInstructors.isEmpty()) {
                comboInstructor.getSelectionModel().select(selectedInstructors.get(0));
            }

            btnSaveCourse.setDisable(true);
            btnDeleteCourse.setDisable(false);
            btnUpdateCourse.setDisable(false);
        }
    }
}
