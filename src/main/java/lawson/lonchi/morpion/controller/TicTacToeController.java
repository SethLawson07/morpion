package lawson.lonchi.morpion.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import lawson.lonchi.morpion.model.TicTacToeModel;
import lawson.lonchi.morpion.view.TicTacToeSquare;
import lawson.lonchi.morpion.model.Owner;

public class TicTacToeController {
    @FXML
    private GridPane gridPane;
    @FXML
    private Label xScoreLabel;
    @FXML
    private Label oScoreLabel;
    @FXML
    private Label freeSquaresLabel;
    @FXML
    private Label endOfGameMessage;
    @FXML
    private Button restartButton;

    private TicTacToeModel model = TicTacToeModel.getInstance();

    @FXML
    public void initialize() {
        // Bindings pour les labels
        // xScoreLabel.textProperty().bind(model.getScore(Owner.FIRST).asString().concat(" case pour X"));
        // oScoreLabel.textProperty().bind(model.getScore(Owner.SECOND).asString().concat(" case pour O"));
        // freeSquaresLabel.textProperty().bind(model.getFreeSquares().asString().concat(" cases libres"));
        // endOfGameMessage.textProperty().bind(model.getEndOfGameMessage());

        xScoreLabel.textProperty().bind(model.getScore(Owner.FIRST).asString().concat(" cases pour X"));
        oScoreLabel.textProperty().bind(model.getScore(Owner.SECOND).asString().concat(" cases pour O"));
        freeSquaresLabel.textProperty().bind(model.getFreeSquares().asString().concat(" cases libres"));
        endOfGameMessage.textProperty().bind(model.getEndOfGameMessage());

        // Ajout de messages de débogage pour vérifier les bindings
        xScoreLabel.textProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Mise à jour du label X : " + newVal);
        });

        oScoreLabel.textProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Mise à jour du label O : " + newVal);
        });

        freeSquaresLabel.textProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Mise à jour du label cases libres : " + newVal);
        });

        endOfGameMessage.textProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Mise à jour du message de fin de jeu : " + newVal);
        });

        // Initialisation de la grille avec TicTacToeSquare
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                TicTacToeSquare square = new TicTacToeSquare(i, j);
                square.getStyleClass().add("tic-tac-toe-button"); // Appliquer le style CSS
                gridPane.add(square, j, i);
            }
        }
            VBox.setMargin(restartButton, new Insets(20, 0, 0, 0)); // Ajoute un margin-top de 20px

    }

    @FXML
    private void handleRestart() {
        model.restart(); // Réinitialise le modèle

        // Réinitialise les cases à blanc
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                TicTacToeSquare square = (TicTacToeSquare) gridPane.getChildren().get(i * 3 + j);
                square.setStyle("-fx-background-color: white; " +
                                 "-fx-font-size: 36px; " +
                                 "-fx-font-weight: bold; " +
                                 "-fx-text-fill: black; " +
                                 "-fx-alignment: center;");
            }
        }
    }




    private void handleButtonClick(int row, int column) {
        model.play(row, column);
        updateView();
    }

    private void updateView() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button button = (Button) gridPane.getChildren().get(i * 3 + j);
                Owner owner = model.getSquare(i, j).get();
                button.setText(owner.toString());
                button.setDisable(!model.validSquare(i, j));

                // Mettre à jour la couleur de fond après un clic
                if (owner != Owner.NONE) {
                    button.setStyle("-fx-background-color: red;");
                } else {
                    button.setStyle("-fx-background-color: cyan;");
                }
            }
        }
    }
    
    
}