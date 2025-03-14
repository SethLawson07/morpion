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
     * Cette méthode est appelée automatiquement après le chargement du fichier
     * FXML.
     */
    @FXML
    public void initialize() {
        // Bindings pour les labels
        xScoreLabel.textProperty().bind(model.getScore(Owner.FIRST).asString().concat(" cases pour X"));
        oScoreLabel.textProperty().bind(model.getScore(Owner.SECOND).asString().concat(" cases pour O"));
        freeSquaresLabel.textProperty().bind(model.getFreeSquares().asString().concat(" cases libres"));
        endOfGameMessage.textProperty().bind(model.getEndOfGameMessage());

        // Initialisation de la grille avec TicTacToeSquare
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                TicTacToeSquare square = new TicTacToeSquare(i, j, this);
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
        freeSquaresLabel.setVisible(true);
        xScoreLabel.setStyle("-fx-background-color: cyan;");
        model.restart(); // Réinitialise le modèle

        // Réinitialiser les cases à blanc
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                TicTacToeSquare square = (TicTacToeSquare) gridPane.getChildren().get(i * 3 + j);
                square.setStyle("-fx-background-color: white; " +
                        "-fx-font-size: 25px; " +
                        // "-fx-font-weight: bold; " +
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
    int i = 0;

    public void handleButtonClick(int row, int column) {
        model.play(row, column);
        updateView();

    }

   
    /**
     * Met à jour la vue en fonction de l'état actuel du modèle.
     */
    public void updateView() {
        freeSquaresLabel.setVisible(!model.gameOver().get());
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                TicTacToeSquare square = (TicTacToeSquare) gridPane.getChildren().get(i * 3 + j);

                Owner owner = model.getSquare(i, j).get();
                if (model.getWinningSquare(i, j).get()) {
                    square.setStyle("-fx-background-color: orange; " +
                            "-fx-font-size: 25px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-text-fill: black; " +
                            "-fx-alignment: center;");

                } else if (model.gameOver().get() && !model.getWinningSquare(i, j).get()) {
                    square.setStyle("-fx-background-color: white; ");
                    xScoreLabel.setStyle("-fx-background-color: red;");
                }

                else if (owner != Owner.NONE) {
                    square.setStyle("-fx-background-color: red;");
                }
            }
        }
    }
}