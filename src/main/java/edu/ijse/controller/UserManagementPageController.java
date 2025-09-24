package edu.ijse.controller;

import edu.ijse.BO.BOFactory;
import edu.ijse.BO.custom.UserBO;
import edu.ijse.dto.UserDto;
import edu.ijse.tm.UserTM;
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

public class UserManagementPageController implements Initializable{


    @FXML
    private AnchorPane apUserPage;

    @FXML
    private Button btnDeleteUser, btnSaveUser, btnUpdateUser;

    @FXML
    private TableColumn<UserTM, Boolean> colActive;

    @FXML
    private TableColumn<UserTM, String> colEmail;

    @FXML
    private TableColumn<UserTM, String> colRole;

    @FXML
    private TableColumn<UserTM, Integer> colUserID;

    @FXML
    private TableColumn<UserTM, String> colUsername;

    @FXML
    private ComboBox<String> comboRole;

    @FXML
    private TableView<UserTM> tblUsers;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;

    private final UserBO userBO = (UserBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.USER);
    private final ObservableList<UserTM> userTMS = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        loadRoles();
        loadUsers();
    }


    private void setupTable() {
        btnDeleteUser.setDisable(true);
        btnSaveUser.setDisable(false);
        btnUpdateUser.setDisable(true);
        colUserID.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colActive.setCellValueFactory(new PropertyValueFactory<>("active"));

        tblUsers.setItems(userTMS);

        tblUsers.setOnMouseClicked(event -> {
            UserTM selected = tblUsers.getSelectionModel().getSelectedItem();
            if (selected != null) {
                txtUsername.setText(selected.getUsername());
                txtEmail.setText(selected.getEmail());
                comboRole.setValue(selected.getRole());
                txtPassword.clear();
            }
            btnDeleteUser.setDisable(false);
            btnSaveUser.setDisable(true);
            btnUpdateUser.setDisable(false);
        });
    }

    private void loadRoles() {
        comboRole.setItems(FXCollections.observableArrayList("ADMIN","RECEPTIONIST"));
    }

    private void loadUsers() {
        userTMS.clear();
        try {
            List<UserDto> users = userBO.getAllUsers();
            for (UserDto dto : users) {
                userTMS.add(new UserTM(
                        dto.getUserId(),
                        dto.getUsername(),
                        dto.getPassword(),
                        dto.getRole(),
                        dto.getEmail(),
                        dto.isActive()
                ));
            }
        } catch (Exception e) {
            AlertHelper.showError("Error loading users", e.getMessage());
        }
    }

    @FXML
    void btnSaveUser(ActionEvent event) {
        try {
            UserDto dto = UserDto.builder()
                    .username(txtUsername.getText())
                    .email(txtEmail.getText())
                    .password(txtPassword.getText())
                    .role(comboRole.getValue())
                    .active(true)
                    .build();

            if (userBO.saveUser(dto)) {
                AlertHelper.showInfo("Success", "User saved successfully!");
                loadUsers();
                clearForm();
            }
        } catch (Exception e) {
            AlertHelper.showError("Error saving user", e.getMessage());
        }
    }

    @FXML
    void btnUpdateUser(ActionEvent event) {
        UserTM selected = tblUsers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("Selection error", "Select a user first");
            return;
        }

        try {
            UserDto dto = UserDto.builder()
                    .userId(selected.getUserId())
                    .username(txtUsername.getText())
                    .email(txtEmail.getText())
                    .password(txtPassword.getText().isEmpty() ? selected.getPassword() : txtPassword.getText())
                    .role(comboRole.getValue())
                    .active(selected.isActive())
                    .build();

            if (userBO.updateUser(dto)) {
                AlertHelper.showInfo("Success", "User updated successfully!");
                loadUsers();
                clearForm();
            }
        } catch (Exception e) {
            AlertHelper.showError("Error updating user", e.getMessage());
        }
    }

    @FXML
    void btnDeleteUser(ActionEvent event) {
        UserTM selected = tblUsers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("Selection error", "Select a user first");
            return;
        }

        try {
            if (userBO.deleteUser(selected.getUserId())) {
                AlertHelper.showInfo("Success", "User deleted successfully!");
                loadUsers();
                clearForm();
            }
        } catch (Exception e) {
            AlertHelper.showError("Error deleting user", e.getMessage());
        }
    }

    @FXML
    void btnGoBack(ActionEvent event) {
//        try {
//            apUserPage.getChildren().clear();
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Dashboard.fxml"));
//            AnchorPane page = (AnchorPane) loader.load();
//            DashboardManagementPage controller = (DashboardManagementPage) loader.getController();
//            controller.setUser(logedUser);
//            apUserPage.getChildren().add(page);
//        } catch (IOException e) {
//            AlertHelper.showError("Error loading Dashboard", e.getMessage());
//        }

    }

    private void clearForm() {
        txtUsername.clear();
        txtEmail.clear();
        txtPassword.clear();
        comboRole.getSelectionModel().clearSelection();
    }

    public void clickOnTable(SortEvent<TableView<UserTM>> tableViewSortEvent) {
    }
}
