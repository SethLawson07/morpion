package lawson.lonchi.morpion.controller;

import java.util.Arrays;
import java.util.List;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import lawson.lonchi.morpion.model.TicTacToeModel;
import lawson.lonchi.morpion.view.TicTacToeSquare;
import lawson.lonchi.morpion.model.Owner;

/**
 * Contrôleur pour le jeu de Morpion (Tic-Tac-Toe).
 * Ce contrôleur gère les interactions entre la vue et le modèle.
 */
public class TicTacToeController {
    /** Grille du jeu contenant les cases. */
    @FXML
    private GridPane gridPane;

    /** Label pour afficher le score du joueur X. */
    @FXML
    private Label xScoreLabel;

    /** Label pour afficher le score du joueur O. */
    @FXML
    private Label oScoreLabel;

    /** Label pour afficher le nombre de cases libres. */
    @FXML
    private Label freeSquaresLabel;

    /** Label pour afficher le message de fin de jeu. */
    @FXML
    private Label endOfGameMessage;

    /** Bouton pour redémarrer le jeu. */
    @FXML
    private Button restartButton;

    /** Instance du modèle du jeu. */
    private TicTacToeModel model = TicTacToeModel.getInstance();

    /**
     * Initialise le contrôleur et configure les bindings entre la vue et le modèle.
     * Cette méthode est appelée automatiquement après le chargement du fichier FXML.
     */
    @FXML
    public void initialize() {
        // Bindings pour les labels
        xScoreLabel.textProperty().bind(model.getScore(Owner.FIRST).asString().concat(" cases pour X"));
        oScoreLabel.textProperty().bind(model.getScore(Owner.SECOND).asString().concat(" cases pour O"));
        freeSquaresLabel.textProperty().bind(model.getFreeSquares().asString().concat(" cases libres"));
        endOfGameMessage.textProperty().bind(model.getEndOfGameMessage());

        // Ajout de messages de débogage pour vérifier les bindings
        xScoreLabel.textProperty().addListener((obs, oldVal, newVal) -> {
            // System.out.println("Mise à jour du label X : " + newVal);
        });

        oScoreLabel.textProperty().addListener((obs, oldVal, newVal) -> {
            // System.out.println("Mise à jour du label O : " + newVal);
        });

        freeSquaresLabel.textProperty().addListener((obs, oldVal, newVal) -> {
            // System.out.println("Mise à jour du label cases libres : " + newVal);
        });

        endOfGameMessage.textProperty().addListener((obs, oldVal, newVal) -> {
            // System.out.println("Mise à jour du message de fin de jeu : " + newVal);
        });

        // Initialisation de la grille avec TicTacToeSquare
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                TicTacToeSquare square = new TicTacToeSquare(i, j);
                square.getStyleClass().add("tic-tac-toe-button"); // Appliquer le style CSS
                gridPane.add(square, j, i);
               
            }
        }
       
        // Ajoute un margin-top de 20px au bouton "Restart"
        VBox.setMargin(restartButton, new Insets(20, 0, 0, 0));

    }

    /**
     * Gère l'action du bouton "Restart".
     * Réinitialise le modèle et met à jour la vue.
     */
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
  

    /**
     * Gère le clic sur une case du plateau.
     *
     * @param row    La ligne de la case cliquée.
     * @param column La colonne de la case cliquée.
     */
    public void handleButtonClick(int row, int column) {
        model.play(row, column);
        updateView();
    }

    /**
     * Met à jour la vue en fonction de l'état actuel du modèle.
     */
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