package com.memorygame;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;

public class LevelController {

    public static int gridSize = 4; // Default

    @FXML
    public void selectEasy(ActionEvent event) throws Exception {
        gridSize = 4; // 4x4
        goToGame(event);
    }

    @FXML
    public void selectMedium(ActionEvent event) throws Exception {
        gridSize = 5; // ✅ Use 5x5 now
        goToGame(event);
    }

    @FXML
    public void selectHard(ActionEvent event) throws Exception {
        gridSize = 6; // 6x6
        goToGame(event);
    }

    private void goToGame(ActionEvent event) throws Exception {
        Parent gameRoot = FXMLLoader.load(getClass().getResource("/com/memorygame/game.fxml"));
        Scene gameScene = new Scene(gameRoot, 800, 600);
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(gameScene);
        stage.setResizable(false);
        stage.show();
    }
}
