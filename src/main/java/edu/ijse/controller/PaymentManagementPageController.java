package edu.ijse.controller;

import edu.ijse.BO.BOFactory;
import edu.ijse.BO.custom.PaymentBO;
import edu.ijse.BO.custom.StudentBO;
import edu.ijse.dto.PaymentDto;
import edu.ijse.dto.StudentDto;
import edu.ijse.dto.UserDto;
import edu.ijse.entity.Student;
import edu.ijse.tm.PaymentTM;
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
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class PaymentManagementPageController implements Initializable,SuperController{

    public TextField stNameTxt;
    public ComboBox<String> cmbPaymentType;
    @FXML
    private AnchorPane apPaymentPage;

    @FXML
    private Button btnDeletePayment, btnSavePayment, btnUpdatePayment;

    @FXML
    private ComboBox<String> cmbStatus;

    @FXML
    private TableColumn<PaymentTM, Integer> colId;

    @FXML
    private TableColumn<PaymentTM, LocalDate> colDate;

    @FXML
    private TableColumn<PaymentTM, String> colType, colStatus;

    @FXML
    private TableColumn<PaymentTM, BigDecimal> colAmount;

    @FXML
    private TableColumn<PaymentTM, String> colStudent;

    @FXML
    private TableView<PaymentTM> tblPayments;

    @FXML
    private TextField txtAmount, txtStudentEmail;

    private final PaymentBO paymentBO = (PaymentBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.PAYMENT);
    private final StudentBO studentBO = (StudentBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.STUDENT);

    private ObservableList<PaymentTM> paymentTMS = FXCollections.observableArrayList();

    private UserDto logedUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        loadPayments();
        setupStatusCombo();
        setupPaymentTypeCombo();
    }

    private void setupTable() {
        btnSavePayment.setDisable(false);
        btnDeletePayment.setDisable(true);
        btnUpdatePayment.setDisable(true);
        colId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        colType.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        //colStudent.setCellValueFactory(new PropertyValueFactory<>("studentEmail")); // use extra getter in TM

        colStudent.setCellValueFactory(cellData -> {
            Student student = cellData.getValue().getStudent();
            return new SimpleStringProperty(
                    student != null ? student.getStudentName() : "N/A"
            );
        });

        tblPayments.setItems(paymentTMS);

        tblPayments.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                fillForm(newValue);
                btnSavePayment.setDisable(true);
                btnDeletePayment.setDisable(false);
                btnUpdatePayment.setDisable(false);
            }
        });
    }

    public void setUser(UserDto loggedUser) {
        this.logedUser = loggedUser;
    }

    private void setupPaymentTypeCombo(){
        cmbPaymentType.setItems(FXCollections.observableArrayList("Card","Cash","Online"));
    }

    private void setupStatusCombo() {
        cmbStatus.setItems(FXCollections.observableArrayList("PAID", "PENDING", "FAILED"));
    }

    private void loadPayments() {
        paymentTMS.clear();
        try {
            List<PaymentDto> list = paymentBO.getAllPayments();
            for (PaymentDto dto : list) {
                PaymentTM tm = new PaymentTM(
                        dto.getPaymentId(),
                        dto.getPaymentDate(),
                        dto.getPaymentType(),
                        dto.getPaymentAmount(),
                        dto.getStatus(),
                        null,
                        dto.getStudent()
                );
                paymentTMS.add(tm);
            }
        } catch (Exception e) {
            AlertHelper.showError("Error loading payments", e.getMessage());
        }
    }

    @FXML
    void savePayment(ActionEvent event) {
        try {
            String email = txtStudentEmail.getText();
            StudentDto student = studentBO.getStudentByEmail(email);

            if (student == null) {
                AlertHelper.showError("Not Found", "No student found with email: " + email);
                return;
            }

            PaymentDto dto = new PaymentDto();
            dto.setPaymentType(cmbPaymentType.getSelectionModel().getSelectedItem());
            dto.setPaymentAmount(new BigDecimal(txtAmount.getText()));
            dto.setPaymentDate(LocalDate.now());
            dto.setStatus(cmbStatus.getValue());

            if (paymentBO.savePayment(dto, student.getStudentID())) {
                AlertHelper.showInfo("Success", "Payment saved successfully!");
                loadPayments();
                clearForm();
            }
        } catch (Exception e) {
            AlertHelper.showError("Error saving payment", e.getMessage());
        }
    }

    @FXML
    void updatePayment(ActionEvent event) {
        PaymentTM selected = tblPayments.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("Selection error", "Select a payment first");
            return;
        }

        try {
            PaymentDto dto = new PaymentDto();
            dto.setPaymentId(selected.getPaymentId());
            dto.setPaymentType(cmbPaymentType.getSelectionModel().getSelectedItem());
            dto.setPaymentAmount(new BigDecimal(txtAmount.getText()));
            dto.setPaymentDate(selected.getPaymentDate());
            dto.setStatus(cmbStatus.getValue());
            dto.setStudent(selected.getStudent());

            if (paymentBO.updatePayment(dto)) {
                AlertHelper.showInfo("Success", "Payment updated successfully!");
                loadPayments();
                clearForm();
            }
        } catch (Exception e) {
            AlertHelper.showError("Error updating payment", e.getMessage());
        }
    }

    @FXML
    void deletePayment(ActionEvent event) {
        PaymentTM selected = tblPayments.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("Selection error", "Select a payment first");
            return;
        }

        try {
            if (paymentBO.deletePayment(selected.getPaymentId())) {
                AlertHelper.showInfo("Success", "Payment deleted successfully!");
                loadPayments();
                clearForm();
            }
        } catch (Exception e) {
            AlertHelper.showError("Error deleting payment", e.getMessage());
        }
    }

    private void fillForm(PaymentTM tm) {
        txtStudentEmail.setText(tm.getStudent().getStudentEmail());
        stNameTxt.setText(tm.getStudent().getStudentName() + " - " + tm.getStudent().getContactNO());
        cmbPaymentType.setValue(tm.getPaymentType());
        txtAmount.setText(tm.getPaymentAmount().toString());
        cmbStatus.setValue(tm.getStatus());
    }

    private void clearForm() {
        txtStudentEmail.clear();
        stNameTxt.clear();
        cmbPaymentType.getSelectionModel().clearSelection();
        txtAmount.clear();
        cmbStatus.getSelectionModel().clearSelection();
    }

    @FXML
    void btnGoBack(ActionEvent event) {
        try {
            apPaymentPage.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Dashboard.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            DashboardManagementPage controller = (DashboardManagementPage) loader.getController();
            controller.setUser(logedUser);
            apPaymentPage.getChildren().add(page);
        } catch (IOException e) {
            AlertHelper.showError("Error loading Dashboard", e.getMessage());
        }

    }

    public void checkStudentAvailability(ActionEvent actionEvent) {
        try {
            String email = txtStudentEmail.getText().trim();
            if (email.isEmpty()) {
                AlertHelper.showError("Input Error", "Enter a student email first");
                return;
            }

            StudentDto student = studentBO.getStudentByEmail(email);

            if (student != null) {
                stNameTxt.setText(student.getStudentName()+"-,-"+student.getContactNO());
                AlertHelper.showInfo("Student Found", "Student exists and is ready for payment.");
            } else {
                stNameTxt.clear();
                AlertHelper.showError("Not Found", "No student found with email: " + email);
            }

        } catch (Exception e) {
            stNameTxt.clear();
            AlertHelper.showError("Error", e.getMessage());
        }
    }
}
