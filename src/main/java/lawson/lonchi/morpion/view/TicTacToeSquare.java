package lawson.lonchi.morpion.view;

import javafx.scene.control.TextField;

import java.util.List;

import javafx.beans.property.*;
import lawson.lonchi.morpion.model.TicTacToeModel;
import lawson.lonchi.morpion.controller.TicTacToeController;
import lawson.lonchi.morpion.model.Owner;

/**
 * Représente une case du plateau de jeu de Morpion (Tic-Tac-Toe).
 * Cette classe étend `TextField` pour afficher le contenu de la case (X, O ou vide)
 * et gère les interactions utilisateur (clic, survol).
 */
public class TicTacToeSquare extends TextField {
    /** Instance du modèle du jeu. */
    private static TicTacToeModel model = TicTacToeModel.getInstance();

    /** Propriété pour stocker le propriétaire de la case (X, O ou NONE). */
    private final ObjectProperty<Owner> ownerProperty = new SimpleObjectProperty<>(Owner.NONE);

    /** Propriété pour indiquer si la case fait partie d'une combinaison gagnante. */
    private final BooleanProperty winnerProperty = new SimpleBooleanProperty(false);


    /**
     * Constructeur pour créer une case du plateau de jeu.
     *
     * @param row    La ligne de la case.
     * @param column La colonne de la case.
     */
    public TicTacToeSquare(final int row, final int column) {
       
        
        // La case n'est pas éditable
        setEditable(false);
        // La case prend toute la place disponible
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        // Style pour centrer le texte, le mettre en noir, en gras, et augmenter la taille
        setStyle("-fx-background-color: white; " +
                 "-fx-font-size: 36px; " +
                 "-fx-font-weight: bold; " +
                 "-fx-text-fill: black; " +
                 "-fx-alignment: center;");

        // Lier la propriété ownerProperty au modèle
        ownerProperty.bind(model.getSquare(row, column));

        // Mettre à jour le texte en fonction de ownerProperty
        textProperty().bind(ownerProperty.asString());

        // Désactiver la case si elle est déjà occupée ou si le jeu est terminé
        disableProperty().bind(model.gameOver().or(model.getSquare(row, column).isNotEqualTo(Owner.NONE)));

        // Gestion de la souris : changement de couleur au survol
        setOnMouseEntered(event -> {
            if (model.validSquare(row, column)) {
                setStyle("-fx-background-color: green; " +
                          "-fx-font-size: 36px; " +
                          "-fx-font-weight: bold; " +
                          "-fx-text-fill: black; " +
                          "-fx-alignment: center;");
            }
        });

        // Gestion de la souris : retour à la couleur d'origine lorsque la souris quitte la case
        setOnMouseExited(event -> {
            if (ownerProperty.get() == Owner.NONE) {
                setStyle("-fx-background-color: white; " +
                          "-fx-font-size: 36px; " +
                          "-fx-font-weight: bold; " +
                          "-fx-text-fill: black; " +
                          "-fx-alignment: center;");
            } else {
                setStyle("-fx-background-color: red; " +
                          "-fx-font-size: 36px; " +
                          "-fx-font-weight: bold; " +
                          "-fx-text-fill: black; " +
                          "-fx-alignment: center;");
            }
        });

        // Gestion du clic : jouer dans la case et mettre à jour la couleur
        setOnMouseClicked(event -> {
            model.play(row, column);
            setStyle("-fx-background-color: red; " +
                      "-fx-font-size: 36px; " +
                      "-fx-font-weight: bold; " +
                      "-fx-text-fill: black; " +
                      "-fx-alignment: center;");
        });

    }
   

    /**
     * Retourne la propriété du propriétaire de la case (X, O ou NONE).
     *
     * @return La propriété du propriétaire de la case.
     */
    public ObjectProperty<Owner> ownerProperty() {
        return ownerProperty;
    }

    /**
     * Retourne la propriété indiquant si la case fait partie d'une combinaison gagnante.
     *
     * @return La propriété indiquant si la case est gagnante.
     */
    public BooleanProperty winnerProperty() {
        return winnerProperty;
    }

    
}

   


