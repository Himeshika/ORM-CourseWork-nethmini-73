package edu.ijse.controller;

import edu.ijse.BO.BOFactory;
import edu.ijse.BO.custom.CourseBO;
import edu.ijse.BO.custom.StudentBO;
import edu.ijse.dto.UserDto;
import edu.ijse.util.AlertHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardManagementPage implements Initializable {

    public TextField txtCheckEmail;
    public Button Checkbtn;
    public TextField txtResult;
    public Label student_count;
    public Label course_count;
    public Label admin_count;
    public AnchorPane apDB;
    private UserDto logedUser;

    @FXML
    private Button btnCourse;

    @FXML
    private Button btnInstructor;

    @FXML
    private Button btnLesson;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnPayment;

    @FXML
    private Button btnStudent;

    @FXML
    private Button btnUser;

    @FXML
    private AnchorPane dashboardRoot;

    @FXML
    private LineChart<?, ?> mainLineChart;

    @FXML
    private PieChart modulePieChart;

    @FXML
    private PieChart statusPieChart;

    private CourseBO courseBO=(CourseBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.COURSE);
    private StudentBO studentBO=(StudentBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.STUDENT);


    public void setUser(UserDto logedUser){
        this.logedUser = logedUser;
        setUserRestrictions();
    }

    public void setUserRestrictions(){
        if(logedUser.getRole().equals("RECEPTIONIST")){
            btnUser.setDisable(true);
            btnInstructor.setDisable(true);
            btnCourse.setDisable(true);
        }
    }

    @FXML
    void handleLogout(ActionEvent event) {
        setPages("/View/LoginPage.fxml");
    }

    @FXML
    void openCoursePage(ActionEvent event) {
        setPages("/View/CoursePage.fxml");
    }

    @FXML
    void openInstructorPage(ActionEvent event) {
        setPages("/View/InstructorPage.fxml");
    }

    @FXML
    void openLessonPage(ActionEvent event) {
        setPages("/View/LessonPage.fxml");
    }

    @FXML
    void openPaymentPage(ActionEvent event) {
        setPages("/View/PaymentPage.fxml");
    }

    @FXML
    void openStudentPage(ActionEvent event) {
        setPages("/View/StudentPage.fxml");
    }

    @FXML
    void openUserPage(ActionEvent event) {
        setPages("/View/userpage.fxml");
    }

    public void setPages(String path){
        try {
            apDB.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            AnchorPane page = (AnchorPane) loader.load();
            apDB.getChildren().add(page);

        }catch (Exception e){
            AlertHelper.showError("Error",e.getMessage());
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}
