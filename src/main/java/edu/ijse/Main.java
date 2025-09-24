package edu.ijse;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main  extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent loadingParent = FXMLLoader.load(getClass().getResource("/View/dashboard.fxml"));
        Scene loadingScene = new Scene(loadingParent);

        stage.setTitle("ttt");
        stage.setScene(loadingScene);

        stage.show();

    }
}