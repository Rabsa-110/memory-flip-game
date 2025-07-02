package com.memorygame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/memorygame/welcome.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Memory Flip Game");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
