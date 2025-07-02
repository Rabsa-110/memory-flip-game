package com.memorygame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;

public class ResultController {

    @FXML
    private Label timeLabel;

    @FXML
    private Label starsLabel;

    // This variable gets set from GameController
    public static int finalTime = 0;

    @FXML
    public void initialize() {
        timeLabel.setText("You finished in " + finalTime + " seconds!");

        // Determine stars based on time
        String stars;
        if (finalTime <= 45)          stars = "★★★";
        else if (finalTime <= 100)    stars = "★★☆";
        else                          stars = "★☆☆";
        starsLabel.setText(stars);
        starsLabel.setStyle("-fx-font-size: 32px; -fx-text-fill: gold;");   // brighter & tighter
    }

    @FXML
    private void restartGame(ActionEvent event) throws Exception {
        // Go back to level selection
        Parent levelRoot = FXMLLoader.load(getClass().getResource("/com/memorygame/level.fxml"));
        Scene levelScene = new Scene(levelRoot);
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(levelScene);
    }

    @FXML
    private void finishGame(ActionEvent event) {
        // Exit the application
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
