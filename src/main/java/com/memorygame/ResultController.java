package com.memorygame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Node;

public class ResultController {

    @FXML private Label timeLabel;
    @FXML private Label starsLabel;
    @FXML private Label scoreLabel;
    @FXML private Button nextLevelButton;

    public static int finalTime = 0;
    public static int wrongAttempts = 0;
    public static int gridSize = 4;

    @FXML
    public void initialize() {
        timeLabel.setText("⏱ Time: " + finalTime + " seconds\n❌ Wrong Attempts: " + wrongAttempts);

        int score;
        if (gridSize == 4) {
            score = finalTime;
        } else {
            score = finalTime + (wrongAttempts * 5);
        }
        scoreLabel.setText("📊 Efficiency Score: " + score);

        String stars;
        if (gridSize == 4) {
            if (finalTime <= 45) stars = "★★★";
            else if (finalTime <= 90) stars = "★★☆";
            else stars = "★☆☆";
        } else if (gridSize == 5) {
            if (score <= 90) stars = "★★★";
            else if (score <= 150) stars = "★★☆";
            else stars = "★☆☆";
        } else {
            if (wrongAttempts <= 5) stars = "★★★";
            else if (wrongAttempts <= 10) stars = "★★☆";
            else stars = "★☆☆";
        }

        starsLabel.setText("⭐ Rank: " + stars);

        if (gridSize >= 6) {
            nextLevelButton.setVisible(false);
        }
    }

    @FXML
    private void retryGame(ActionEvent event) throws Exception {
        Parent levelRoot = FXMLLoader.load(getClass().getResource("/com/memorygame/level.fxml"));
        Scene levelScene = new Scene(levelRoot, 800, 600);
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(levelScene);
        stage.setResizable(false);
    }

    @FXML
    private void nextLevel(ActionEvent event) throws Exception {

        if (gridSize == 4) {
            LevelController.gridSize = 5;
        } else if (gridSize == 5) {
            LevelController.gridSize = 6;
        }

        Parent gameRoot = FXMLLoader.load(getClass().getResource("/com/memorygame/game.fxml"));
        Scene gameScene = new Scene(gameRoot, 800, 600);
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(gameScene);
        stage.setResizable(false);
    }

    @FXML
    private void finishGame(ActionEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
