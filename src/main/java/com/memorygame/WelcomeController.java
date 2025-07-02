package com.memorygame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

public class WelcomeController {
    @FXML
    private void startGame(ActionEvent event) throws Exception {
        Parent levelRoot = FXMLLoader.load(getClass().getResource("/com/memorygame/level.fxml"));
        Scene levelScene = new Scene(levelRoot);

        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(levelScene);
        stage.show();
    }
}
