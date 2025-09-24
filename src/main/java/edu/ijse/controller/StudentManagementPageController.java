package edu.ijse.controller;

import edu.ijse.BO.BOFactory;
import edu.ijse.BO.custom.CourseBO;
import edu.ijse.BO.custom.StudentBO;
import edu.ijse.dto.CourseDto;
import edu.ijse.dto.PaymentDto;
import edu.ijse.dto.StudentDto;
import edu.ijse.dto.UserDto;
import edu.ijse.tm.StudentTM;
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
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StudentManagementPageController implements Initializable{

    public Button btnACoursesID;
    public TextField txtPaymentAmount;
    public ComboBox<String> comboPaymentType;
    public ComboBox<String> comboPaymentStatus;
    public DatePicker datePayment;
    private StudentBO studentBO = (StudentBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.STUDENT);
    private CourseBO courseBO = (CourseBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.COURSE);
    private ObservableList<StudentTM> studentTMS = FXCollections.observableArrayList();
    private List<CourseDto> courses = new ArrayList<>();


    @FXML
    private AnchorPane apMainSide;

    @FXML
    private AnchorPane apStudentPage;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<StudentTM, String> colAddress;

    @FXML
    private TableColumn<StudentTM, String> colContact;

    @FXML
    private TableColumn<StudentTM, LocalDate> colDob;

    @FXML
    private TableColumn<StudentTM, String> colEmail;

    @FXML
    private TableColumn<StudentTM, Integer> colID;

    @FXML
    private TableColumn<StudentTM, String> colName;

    @FXML
    private TableColumn<StudentTM, Integer> colProgress;

    @FXML
    private TableColumn<StudentTM, LocalDate> colRegDate;

    @FXML
    private ComboBox<CourseDto> comboCourses;

    @FXML
    private DatePicker dateDob;

    @FXML
    private DatePicker dateReg;

    @FXML
    private TableView<StudentTM> tblStudents;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtName;


    @FXML
    void btnDelete(ActionEvent event) {
        StudentTM selected = tblStudents.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("Selection error", "Select a student from the table first");
            return;
        }

        try {
            boolean deleted = studentBO.deleteStudent(selected.getStudentID());
            if (deleted) {
                AlertHelper.showInfo("Deleted", "Student deleted successfully");
                tblStudents.getItems().clear();
                loadAllToTable();
                clearFields();
                courses.clear();
            }
        } catch (Exception e) {
            AlertHelper.showError("Delete error", e.getMessage());
        }
    }

    @FXML
    void btnGoBack(ActionEvent event) {
//        try {
//            apStudentPage.getChildren().clear();
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Dashboard.fxml"));
//            AnchorPane page = (AnchorPane) loader.load();
//            DashboardManagementPage controller = (DashboardManagementPage) loader.getController();
//            controller.setUser(logedUser);
//            apStudentPage.getChildren().add(page);
//        } catch (IOException e) {
//            AlertHelper.showError("Error loading Dashboard", e.getMessage());
//        }

    }

    @FXML
    void btnAddCourses(ActionEvent event) {
        if (comboCourses.getSelectionModel().getSelectedItem() == null) {
            AlertHelper.showError("selection error", "select a course from combobox first");
        }
        CourseDto courseDto = comboCourses.getSelectionModel().getSelectedItem();
        courses.add(courseDto);
    }

    @FXML
    void btnSave(ActionEvent event) {
        if (courses.isEmpty()) {
            AlertHelper.showError("selection error", "select a course from combobox first");
            return;
        }
        try {
            StudentDto dto = new StudentDto(
                    0,
                    txtName.getText(),
                    txtEmail.getText(),
                    txtContact.getText(),
                    txtAddress.getText(),
                    dateDob.getValue(),
                    dateReg.getValue(),
                    0
            );
            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setPaymentType(comboPaymentType.getSelectionModel().getSelectedItem());

            try {
                paymentDto.setPaymentAmount(new BigDecimal(txtPaymentAmount.getText()));
            } catch (NumberFormatException e) {
                AlertHelper.showError("Invalid amount", "Please enter a valid amount");
                return;
            }

            paymentDto.setPaymentDate(LocalDate.now());
            paymentDto.setStatus(comboPaymentStatus.getSelectionModel().getSelectedItem());

            boolean saved = studentBO.saveStudent(dto, courses, paymentDto);
            if (saved) {
                AlertHelper.showInfo("Student successfully saved", "Student successfully saved");
                tblStudents.getItems().clear();
                loadAllToTable();
                clearFields();
                courses.clear();
            }

        } catch (Exception e) {
            AlertHelper.showError("Invalid data", e.getMessage());
        }
    }

    @FXML
    void btnUpdate(ActionEvent event) {
        StudentTM selected = tblStudents.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("Selection error", "Select a student from the table first");
            return;
        }

        try {
            StudentDto dto = new StudentDto(
                    selected.getStudentID(),
                    txtName.getText(),
                    txtEmail.getText(),
                    txtContact.getText(),
                    txtAddress.getText(),
                    dateDob.getValue(),
                    dateReg.getValue(),
                    selected.getProgress()
            );

            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setPaymentId(selected.getPayments().get(0).getPaymentId());
            paymentDto.setPaymentType(comboPaymentType.getSelectionModel().getSelectedItem());

            try {
                paymentDto.setPaymentAmount(new BigDecimal(txtPaymentAmount.getText()));
            } catch (NumberFormatException e) {
                AlertHelper.showError("Invalid amount", "Please enter a valid amount");
                return;
            }

            paymentDto.setPaymentDate(LocalDate.now());
            paymentDto.setStatus(comboPaymentStatus.getSelectionModel().getSelectedItem());

            boolean updated = studentBO.updateStudent(dto, courses, paymentDto);
            if (updated) {
                AlertHelper.showInfo("Updated", "Student updated successfully");
                tblStudents.getItems().clear();
                loadAllToTable();
                clearFields();
                courses.clear();

            }
        } catch (Exception e) {
            AlertHelper.showError("Update error", e.getMessage());
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        btnSave.setDisable(false);
        colID.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("studentEmail"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contactNO"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        colRegDate.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        colProgress.setCellValueFactory(new PropertyValueFactory<>("progress"));

        loadAllToTable();
        loadCoursesToCMB();
        comboPaymentType.getItems().addAll("Cash", "Card", "Online");
        comboPaymentStatus.getItems().addAll("Paid", "Pending", "Failed");

        tblStudents.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            fillform(newValue);
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
            btnSave.setDisable(true);
        });

    }

    public void loadAllToTable() {
        try {
            ArrayList<StudentDto> studentDtos = studentBO.loadAllStudent();
            studentTMS.clear();
            for (StudentDto studentDto : studentDtos) {
                StudentTM studentTM = StudentTM.builder()
                        .studentID(studentDto.getStudentID())
                        .studentName(studentDto.getStudentName())
                        .studentEmail(studentDto.getStudentEmail())
                        .contactNO(studentDto.getContactNO())
                        .address(studentDto.getAddress())
                        .dateOfBirth(studentDto.getDateOfBirth())
                        .registrationDate(studentDto.getRegistrationDate())
                        .progress(studentDto.getProgress())
                        .payments(studentDto.getPayments()).build();

                studentTMS.add(studentTM);
            }
            tblStudents.setItems(studentTMS);
        } catch (Exception e) {
            AlertHelper.showError("student load error", e.getMessage());
        }


    }

    private void clearFields() {
        txtName.clear();
        txtEmail.clear();
        txtContact.clear();
        txtAddress.clear();
        dateDob.setValue(null);
        dateReg.setValue(null);
        comboCourses.getSelectionModel().clearSelection();
    }

    private void fillform(StudentTM newSelection) {
        StudentDto fullStudent = studentBO.getStBYId(newSelection.getStudentID());

        txtName.setText(fullStudent.getStudentName());
        txtEmail.setText(fullStudent.getStudentEmail());
        txtContact.setText(fullStudent.getContactNO());
        txtAddress.setText(fullStudent.getAddress());
        dateDob.setValue(fullStudent.getDateOfBirth());
        dateReg.setValue(fullStudent.getRegistrationDate());


        courses.clear();
        courses.addAll(fullStudent.getCourses());
        if (!courses.isEmpty()) {
            comboCourses.getSelectionModel().select(courses.get(0));
        }


        if (!fullStudent.getPayments().isEmpty()) {
            PaymentDto lastPayment = fullStudent.getPayments().get(0);
            txtPaymentAmount.setText(lastPayment.getPaymentAmount().toString());
            comboPaymentType.getSelectionModel().select(lastPayment.getPaymentType());
            comboPaymentStatus.getSelectionModel().select(lastPayment.getStatus());
            datePayment.setValue(lastPayment.getPaymentDate());
        }

    }


//    public void tableClicked(MouseEvent mouseEvent) {
//        tblStudents.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
//            if (newSelection != null) {
//
//                txtName.setText(newSelection.getStudentName());
//                txtEmail.setText(newSelection.getStudentEmail());
//                txtContact.setText(newSelection.getContactNO());
//                txtAddress.setText(newSelection.getAddress());
//                dateDob.setValue(newSelection.getDateOfBirth());
//                dateReg.setValue(newSelection.getRegistrationDate());
//
//
//
//
//            }
//        });
//
//    }

    public void loadCoursesToCMB() {
        try {
            List<CourseDto> allCourses = courseBO.getAllCourses();
            comboCourses.getItems().clear();
            comboCourses.getItems().addAll(allCourses);
        } catch (Exception e) {
            AlertHelper.showError("Load courses error", e.getMessage());
        }
    }
}
