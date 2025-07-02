package com.memorygame;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.*;

public class GameController {

    @FXML
    private GridPane grid;

    @FXML
    private Label timerLabel;

    private List<String> cardValues = new ArrayList<>();
    private Button firstCard = null;
    private Button secondCard = null;
    private Timer gameTimer;
    private int timePassed = 0;

    private boolean allowClicks = false;
    private int gridSize;

    @FXML
    public void initialize() {
        // Get the grid size from previous screen
        gridSize = LevelController.gridSize;
        int numPairs = (gridSize * gridSize) / 2;

        // 🎯 Choose emoji pool based on level
        List<String> emojiPool;
        if (gridSize == 4) {
            emojiPool = List.of("😎","😅","🤩","💀","🐼","😸","❤️‍🔥","🌚");
        } else if (gridSize == 6) {
            emojiPool = List.of("😎","😅","🤩","💀","🐼","😸","❤️‍🔥","🌚",
                    "🌏","🤖","🧸","💐","💩","🤡","🙊","🕊️","🦚","🐞");
        } else {
            emojiPool = List.of("😎","😅","🤩","💀","🐼","😸","❤️‍🔥","🌚",
                    "🌏","🤖","🧸","💐","💩","🤡","🙊","🕊️",
                    "🦚","🐞","💙","💚","☃️","🌝","🏡","🚀",
                    "🍂","🥀","🌼","🌻","🍜","🍕","📸","🎹");
        }

        cardValues.clear();
        for (int i = 0; i < numPairs; i++) {
            String emoji = emojiPool.get(i);
            cardValues.add(emoji);
            cardValues.add(emoji);
        }

        Collections.shuffle(cardValues);

        int index = 0;
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                Button card = new Button();
                card.setPrefSize(80, 80);
                card.setStyle("""
                        -fx-background-color: rgba(255,255,255,0.25);
                        -fx-background-radius: 10;
                        -fx-font-size: 26px;
                        -fx-text-fill: white;
                        """);

                String value = cardValues.get(index++);
                card.setUserData(value);
                card.setText(value); // show for 5 seconds
                grid.add(card, col, row);
                card.setOnAction(e -> handleCardClick(card));
            }
        }

        // Hide cards after 5 seconds
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> hideCards());
            }
        }, 5000);
    }

    private void hideCards() {
        for (var node : grid.getChildren()) {
            if (node instanceof Button btn) {
                btn.setText("?");
            }
        }

        allowClicks = true;
        startTimer();
    }

    private void handleCardClick(Button card) {
        if (!allowClicks || card == firstCard || !card.getText().equals("?")) return;

        card.setText((String) card.getUserData());

        if (firstCard == null) {
            firstCard = card;
        } else {
            secondCard = card;
            allowClicks = false;

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> checkForMatch());
                }
            }, 700);
        }
    }

    private void checkForMatch() {
        if (firstCard.getUserData().equals(secondCard.getUserData())) {
            firstCard.setDisable(true);
            secondCard.setDisable(true);
        } else {
            firstCard.setText("?");
            secondCard.setText("?");
        }

        firstCard = null;
        secondCard = null;
        allowClicks = true;

        if (isGameOver()) {
            stopTimer();
            goToResultScene();
        }
    }

    private boolean isGameOver() {
        for (var node : grid.getChildren()) {
            if (node instanceof Button btn && !btn.isDisabled()) {
                return false;
            }
        }
        return true;
    }

    private void startTimer() {
        gameTimer = new Timer();
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timePassed++;
                Platform.runLater(() -> timerLabel.setText("Time: " + timePassed));
            }
        }, 1000, 1000);
    }

    private void stopTimer() {
        if (gameTimer != null) {
            gameTimer.cancel();
        }
    }

    private void goToResultScene() {
        ResultController.finalTime = timePassed;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/memorygame/result.fxml"));
            Parent resultRoot = loader.load();
            Stage stage = (Stage) grid.getScene().getWindow();
            stage.setScene(new Scene(resultRoot));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
