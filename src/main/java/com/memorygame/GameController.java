package com.memorygame;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.*;

public class GameController {

    @FXML
    private GridPane grid;

    @FXML
    private Label timerLabel;

    private List<Image> imageList = new ArrayList<>();
    private StackPane firstCard = null;
    private StackPane secondCard = null;
    private Timer gameTimer;
    private int timePassed = 0;
    private int wrongAttempts = 0;

    private boolean allowClicks = false;
    private int gridSize;
    private final int CARD_SIZE = 75;

    @FXML
    public void initialize() {
        System.out.println("GameController initialized");
        gridSize = LevelController.gridSize;
        int totalCards = gridSize * gridSize;
        int numPairs = totalCards / 2;
        boolean hasBonusCard = (totalCards % 2 != 0);

        imageList.clear();
        String[] filenames = {
                "calculator.png", "pencil.png", "microscope.png", "big-ben.png", "programming.png",
                "storm.png", "shop.png", "male-user.png", "user.png", "cursor.png",
                "music.png", "linkedIn.png", "reddit.png", "spotify.png", "java.png",
                "python.png", "github.png", "messenger.png", "telegram.png", "chrome.png"
        };

        for (String name : filenames) {
            imageList.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/" + name)), CARD_SIZE, CARD_SIZE, true, true));
        }

        Collections.shuffle(imageList);
        List<String> selectedImageNames = new ArrayList<>();
        for (int i = 0; i < numPairs; i++) {
            selectedImageNames.add(filenames[i]);
            selectedImageNames.add(filenames[i]);
        }

        if (hasBonusCard && gridSize == 5) {
            selectedImageNames.add("joker");
        }

        Collections.shuffle(selectedImageNames);

        int index = 0;
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                String imageName = selectedImageNames.get(index++);
                StackPane card;
                if (imageName.equals("joker")) {
                    card = createCard(null, "joker");
                } else {
                    Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/" + imageName)), CARD_SIZE, CARD_SIZE, true, true);
                    card = createCard(img, imageName);
                }
                grid.add(card, col, row);
            }
        }

        allowClicks = false;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    hideAllCards();
                    allowClicks = true;
                    startTimer();
                });
            }
        }, 5000);
    }

    private StackPane createCard(Object frontContent, String imageName) {
        StackPane card = new StackPane();
        card.setPrefSize(CARD_SIZE, CARD_SIZE);

        Node frontNode;
        if (imageName.equals("joker")) {
            Label emojiLabel = new Label("\uD83E\uDD21");
            emojiLabel.setFont(new Font(48));
            emojiLabel.setStyle("-fx-font-weight: bold;");
            LinearGradient gradient = new LinearGradient(
                    0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.RED), new Stop(0.5, Color.YELLOW), new Stop(1, Color.GREEN));
            emojiLabel.setTextFill(gradient);
            frontNode = emojiLabel;
        } else {
            Image img = (Image) frontContent;
            ImageView frontView = new ImageView(img);
            frontView.setFitWidth(CARD_SIZE);
            frontView.setFitHeight(CARD_SIZE);
            frontView.setPreserveRatio(true);
            frontNode = frontView;
        }

        Rectangle backRect = new Rectangle(CARD_SIZE, CARD_SIZE);
        backRect.setArcWidth(15);
        backRect.setArcHeight(15);
        backRect.setFill(Color.rgb(255, 255, 255, 0.25));
        backRect.setStroke(Color.WHITE);

        card.getChildren().add(frontNode);
        card.setUserData(new Object[]{imageName, frontNode, backRect});

        card.setOnMouseClicked(e -> handleCardClick(card));
        return card;
    }

    private void hideAllCards() {
        for (var node : grid.getChildren()) {
            if (node instanceof StackPane card) {
                Object[] data = (Object[]) card.getUserData();
                Rectangle back = (Rectangle) data[2];
                card.getChildren().clear();
                card.getChildren().add(back);
            }
        }
    }

    private void handleCardClick(StackPane card) {
        if (!allowClicks || card.isDisabled()) return;

        Object[] data = (Object[]) card.getUserData();
        String imageName = (String) data[0];
        Node frontNode = (Node) data[1];

        if (card.getChildren().contains(frontNode)) return;

        card.getChildren().clear();
        card.getChildren().add(frontNode);

        if (imageName.equals("joker")) {
            showJokerPopup();
        }

        if (firstCard == null) {
            firstCard = card;
        } else if (secondCard == null && card != firstCard) {
            secondCard = card;
            allowClicks = false;

            String firstName = (String) ((Object[]) firstCard.getUserData())[0];
            String secondName = (String) ((Object[]) secondCard.getUserData())[0];

            int delay = (firstName.equals("joker") || secondName.equals("joker")) ? 3000 : 800;

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> checkMatch());
                }
            }, delay);
        }
    }

    private void showJokerPopup() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("🎉 Joker Card!");
        alert.setHeaderText(null);
        alert.setContentText("You've flipped the Joker card! Bonus time added.");
        alert.show();
    }

    private void checkMatch() {
        String img1 = (String) ((Object[]) firstCard.getUserData())[0];
        String img2 = (String) ((Object[]) secondCard.getUserData())[0];

        if (img1.equals(img2)) {
            firstCard.setDisable(true);
            secondCard.setDisable(true);
        } else {
            if (img1.equals("joker") || img2.equals("joker")) {
                if (img1.equals("joker")) firstCard.setDisable(true);
                if (img2.equals("joker")) secondCard.setDisable(true);
            } else {
                resetCard(firstCard);
                resetCard(secondCard);
                wrongAttempts++;
            }
        }

        firstCard = null;
        secondCard = null;
        allowClicks = true;

        if (isGameOver()) {
            stopTimer();
            goToResultScene();
        }
    }

    private void resetCard(StackPane card) {
        Object[] data = (Object[]) card.getUserData();
        Rectangle back = (Rectangle) data[2];
        card.getChildren().clear();
        card.getChildren().add(back);
    }

    private boolean isGameOver() {
        return grid.getChildren().stream().allMatch(node -> {
            if (node instanceof StackPane card) {
                Object[] data = (Object[]) card.getUserData();
                String name = (String) data[0];
                return name.equals("joker") || card.isDisabled();
            }
            return true;
        });
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
        if (gameTimer != null) gameTimer.cancel();
    }

    private void goToResultScene() {
        ResultController.finalTime = timePassed;
        ResultController.wrongAttempts = wrongAttempts;
        ResultController.gridSize = gridSize;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/memorygame/result.fxml"));
            Parent resultRoot = loader.load();
            Stage stage = (Stage) grid.getScene().getWindow();
            stage.setScene(new Scene(resultRoot, 800, 600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
