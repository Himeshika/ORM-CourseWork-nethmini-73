package edu.ijse.controller;

import edu.ijse.BO.BOFactory;
import edu.ijse.BO.custom.UserBO;
import edu.ijse.dto.UserDto;
import edu.ijse.exception.InvalidPasswordException;
import edu.ijse.exception.InvalidUserNameException;
import edu.ijse.util.AlertHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class LoginPageController {

    public TextField txtPasswordVisible;
    public Button btnTogglePassword;
    private UserBO userBO=(UserBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.USER);

    @FXML
    private AnchorPane apLoginPage;

    @FXML
    private Button btnLogin;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;

    private boolean isPasswordVisible=false;


    public void togglePassword(ActionEvent actionEvent) {
        if(isPasswordVisible){
            txtPassword.setText(txtPasswordVisible.getText());
            txtPasswordVisible.setVisible(false);
            txtPasswordVisible.setManaged(false);
            txtPassword.setManaged(true);
            txtPassword.setVisible(true);
            btnTogglePassword.setText("👁");
            isPasswordVisible=false;
        }else{
            txtPasswordVisible.setText(txtPassword.getText());
            txtPassword.setVisible(false);
            txtPassword.setManaged(false);
            txtPasswordVisible.setManaged(true);
            txtPasswordVisible.setVisible(true);
            btnTogglePassword.setText("=");
            isPasswordVisible=true;

        }
    }


    @FXML
    void btnLogInloginPage(ActionEvent event) {
        checkLogins();
    }

    public void checkLogins(){
         String username=txtUsername.getText().trim();
         String password=txtPassword.getText().trim();

        if(txtUsername.getText().isEmpty()){
            AlertHelper.showError("Error","Username is empty");
        }
        if(txtPassword.getText().isEmpty()){
            AlertHelper.showError("Error","Password is empty");
        }

        try {
            UserDto user = userBO.login(username, password);

            switch (user.getRole()) {
                case "ADMIN":
                    loadDashboard(user);
                    break;
                case "RECEPTIONIST":
                    loadDashboard(user);
                    break;
                default:
                    AlertHelper.showError("Error", "Unknown role: " + user.getRole());
            }

        } catch (InvalidUserNameException e) {
            AlertHelper.showError("Login Failed", e.getMessage());
        } catch (InvalidPasswordException e) {
            AlertHelper.showError("Login Failed", e.getMessage());
        } catch (Exception e) {
            AlertHelper.showError("Error", "Something went wrong!");
            e.printStackTrace();
        }
    }

    public void loadDashboard(UserDto user){

        try {
            apLoginPage.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Dashboard.fxml"));
            AnchorPane pane = loader.load();
            DashboardManagementPage controller=loader.getController();
            controller.setUser(user);
            apLoginPage.getChildren().add(pane);
        } catch (IOException e) {
            AlertHelper.showError("Login Failed", e.getMessage());
        }

    }



}
