package edu.ijse.controller;

import edu.ijse.BO.BOFactory;
import edu.ijse.BO.custom.CourseBO;
import edu.ijse.BO.custom.InstructorBO;
import edu.ijse.BO.custom.LessonsBO;
import edu.ijse.BO.custom.StudentBO;
import edu.ijse.dto.*;
import edu.ijse.tm.LessonsTM;
import edu.ijse.util.AlertHelper;
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
import java.net.URL;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class LessonManagementPageController implements Initializable,SuperController {

    @FXML
    private AnchorPane apLessonPage;

    @FXML
    private Button btnDeleteLesson;

    @FXML
    private Button btnSaveLesson;

    @FXML
    private Button btnUpdateLesson;

    @FXML
    private TableView<LessonsTM> tblLessons;

    @FXML
    private TableColumn<LessonsTM, Integer> colLessonID;

    @FXML
    private TableColumn<LessonsTM, String> colLessonName;

    @FXML
    private TableColumn<LessonsTM, String> colStudent;

    @FXML
    private TableColumn<LessonsTM, String> colInstructor;

    @FXML
    private TableColumn<LessonsTM, String> colCourse;

    @FXML
    private TableColumn<LessonsTM, String> colStartTime;

    @FXML
    private TableColumn<LessonsTM, String> colEndTime;

    @FXML
    private TableColumn<LessonsTM, String> colDate;

    @FXML
    private ComboBox<StudentDto> comboStudent;

    @FXML
    private ComboBox<InstructorDto> comboInstructor;

    @FXML
    private ComboBox<CourseDto> comboCourse;

    @FXML
    private TextField txtLessonName;

    @FXML
    private TextField txtStartTime;

    @FXML
    private TextField txtEndTime;

    @FXML
    private DatePicker dateLesson;

    private LessonsBO lessonsBO = (LessonsBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.LESSONS);
    private StudentBO studentBO = (StudentBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.STUDENT);
    private InstructorBO instructorBO = (InstructorBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.INSTRUCTOR);
    private CourseBO courseBO = (CourseBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.COURSE);

    private ObservableList<LessonsTM> lessonTMS = FXCollections.observableArrayList();

    private UserDto logedUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colLessonID.setCellValueFactory(new PropertyValueFactory<>("lessonID"));
        colLessonName.setCellValueFactory(new PropertyValueFactory<>("lessonName"));
        colStudent.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        colInstructor.setCellValueFactory(new PropertyValueFactory<>("instructorName"));
        colCourse.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("lessonDate"));
        colStartTime.setCellValueFactory(new PropertyValueFactory<>("lessonStartTime"));
        colEndTime.setCellValueFactory(new PropertyValueFactory<>("lessonEndTime"));

        loadStudents();
        loadInstructors();
        loadCourses();
        loadLessonsToTable();

        btnSaveLesson.setDisable(false);
        btnDeleteLesson.setDisable(true);
        btnUpdateLesson.setDisable(true);
    }

    public void setUser(UserDto loggedUser) {
        this.logedUser = loggedUser;
    }

    private void loadStudents() {
        try {
            comboStudent.getItems().clear();
            comboStudent.getItems().addAll(studentBO.loadAllStudent());
        } catch (Exception e) {
            AlertHelper.showError("Load Students", e.getMessage());
        }
    }

    private void loadInstructors() {
        try {
            comboInstructor.getItems().clear();
            comboInstructor.getItems().addAll(instructorBO.getAllInstructors());
        } catch (Exception e) {
            AlertHelper.showError("Load Instructors", e.getMessage());
        }
    }

    private void loadCourses() {
        try {
            comboCourse.getItems().clear();
            comboCourse.getItems().addAll(courseBO.getAllCourses());
        } catch (Exception e) {
            AlertHelper.showError("Load Courses", e.getMessage());
        }
    }

    private void loadLessonsToTable() {
        try {
            List<LessonDto> lessons = lessonsBO.getAllLessons();
            lessonTMS.clear();
            for (LessonDto dto : lessons) {
                LessonsTM tm = LessonsTM.builder()
                        .lessonID(dto.getLessonId())
                        .lessonName(dto.getLessonName())
                        .studentID(dto.getStudent().getStudentID())
                        .studentName(dto.getStudent().getStudentName())
                        .instructorID(dto.getInstructor().getInstructorID())
                        .instructorName(dto.getInstructor().getInstructorName())
                        .courseID(dto.getCourse().getCourseID())
                        .courseName(dto.getCourse().getCourseName())
                        .lessonDate(dto.getLessonDate())
                        .lessonStartTime(dto.getLessonStartTime().toString())
                        .lessonEndTime(dto.getLessonEndTime().toString())
                        .build();
                lessonTMS.add(tm);
            }
            tblLessons.setItems(lessonTMS);
        } catch (Exception e) {
            AlertHelper.showError("Load Lessons", e.getMessage());
        }
    }

    @FXML
    void btnSaveLesson(ActionEvent event) {
        try {
            if (comboStudent.getSelectionModel().isEmpty() ||
                    comboInstructor.getSelectionModel().isEmpty() ||
                    comboCourse.getSelectionModel().isEmpty() ||
                    txtLessonName.getText().isEmpty() ||
                    dateLesson.getValue() == null ||
                    txtStartTime.getText().isEmpty() ||
                    txtEndTime.getText().isEmpty()) {
                AlertHelper.showError("Input Error", "Please fill all fields");
                return;
            }

            int studentID = comboStudent.getSelectionModel().getSelectedItem().getStudentID();
            int instructorID = comboInstructor.getSelectionModel().getSelectedItem().getInstructorID();
            int courseID = comboCourse.getSelectionModel().getSelectedItem().getCourseID();

            LessonDto dto = new LessonDto();
            dto.setLessonName(txtLessonName.getText());
            dto.setLessonDate(dateLesson.getValue());
            dto.setLessonStartTime(LocalTime.parse(txtStartTime.getText()));
            dto.setLessonEndTime(LocalTime.parse(txtEndTime.getText()));

            boolean saved = lessonsBO.saveLesson(studentID, instructorID, courseID, dto);
            if (saved) {
                AlertHelper.showInfo("Success", "Lesson saved successfully");
                loadLessonsToTable();
                clearFields();
            }

        } catch (Exception e) {
            AlertHelper.showError("Save Lesson", e.getMessage());
        }
    }

    @FXML
    void btnUpdateLesson(ActionEvent event) {
        LessonsTM selected = tblLessons.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("Selection Error", "Select a lesson from the table first");
            return;
        }

        try {
            if (comboStudent.getSelectionModel().isEmpty() ||
                    comboInstructor.getSelectionModel().isEmpty() ||
                    comboCourse.getSelectionModel().isEmpty() ||
                    txtLessonName.getText().isEmpty() ||
                    dateLesson.getValue() == null ||
                    txtStartTime.getText().isEmpty() ||
                    txtEndTime.getText().isEmpty()) {

                AlertHelper.showError("Input Error", "Please fill all fields");
                return;
            }

            int studentID = comboStudent.getSelectionModel().getSelectedItem().getStudentID();
            int instructorID = comboInstructor.getSelectionModel().getSelectedItem().getInstructorID();
            int courseID = comboCourse.getSelectionModel().getSelectedItem().getCourseID();

            LessonDto dto = new LessonDto();
            dto.setLessonId(selected.getLessonID());
            dto.setLessonName(txtLessonName.getText());
            dto.setLessonDate(dateLesson.getValue());
            dto.setLessonStartTime(LocalTime.parse(txtStartTime.getText()));
            dto.setLessonEndTime(LocalTime.parse(txtEndTime.getText()));

            boolean updated = lessonsBO.updateLesson(studentID, instructorID, courseID, dto);
            if (updated) {
                AlertHelper.showInfo("Success", "Lesson updated successfully");
                loadLessonsToTable();
                clearFields();
            }

        } catch (Exception e) {
            AlertHelper.showError("Update Lesson", e.getMessage());
        }
    }

    @FXML
    void btnDeleteLesson(ActionEvent event) {
        LessonsTM selected = tblLessons.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("Selection Error", "Select a lesson from the table first");
            return;
        }
        try {
            boolean deleted = lessonsBO.deleteLesson(selected.getLessonID());
            if (deleted) {
                AlertHelper.showInfo("Deleted", "Lesson deleted successfully");
                loadLessonsToTable();
            }
        } catch (Exception e) {
            AlertHelper.showError("Delete Lesson", e.getMessage());
        }
    }

    private void clearFields() {
        txtLessonName.clear();
        txtStartTime.clear();
        txtEndTime.clear();
        dateLesson.setValue(null);
        comboStudent.getSelectionModel().clearSelection();
        comboInstructor.getSelectionModel().clearSelection();
        comboCourse.getSelectionModel().clearSelection();
    }

    @FXML
    void tableClicked(MouseEvent event) {

        btnSaveLesson.setDisable(true);
        btnDeleteLesson.setDisable(false);
        btnUpdateLesson.setDisable(false);
        LessonsTM selected = tblLessons.getSelectionModel().getSelectedItem();
        if (selected != null) {
            txtLessonName.setText(selected.getLessonName());
            txtStartTime.setText(selected.getLessonStartTime());
            txtEndTime.setText(selected.getLessonEndTime());
            dateLesson.setValue(selected.getLessonDate());

            comboStudent.getSelectionModel().select(
                    comboStudent.getItems().stream()
                            .filter(s -> s.getStudentID() == selected.getStudentID())
                            .findFirst().orElse(null)
            );

            comboInstructor.getSelectionModel().select(
                    comboInstructor.getItems().stream()
                            .filter(i -> i.getInstructorID() == selected.getInstructorID())
                            .findFirst().orElse(null)
            );

            comboCourse.getSelectionModel().select(
                    comboCourse.getItems().stream()
                            .filter(c -> c.getCourseID() == selected.getCourseID())
                            .findFirst().orElse(null)
            );
        }
    }

    public void btnGoBack(ActionEvent actionEvent) {
        try {
            apLessonPage.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Dashboard.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            DashboardManagementPage controller = (DashboardManagementPage) loader.getController();
            controller.setUser(logedUser);
            apLessonPage.getChildren().add(page);
        } catch (IOException e) {
            AlertHelper.showError("Error loading Dashboard", e.getMessage());
        }
    }
}
