package lawson.lonchi.morpion.view;

import javafx.scene.control.TextField;
import javafx.beans.property.*;
import lawson.lonchi.morpion.model.TicTacToeModel;
import lawson.lonchi.morpion.model.Owner;

public class TicTacToeSquare extends TextField {
    private static TicTacToeModel model = TicTacToeModel.getInstance();

    private final ObjectProperty<Owner> ownerProperty = new SimpleObjectProperty<>(Owner.NONE);
    private final BooleanProperty winnerProperty = new SimpleBooleanProperty(false);

    public TicTacToeSquare(final int row, final int column) {
        setEditable(false);
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

        // Gestion de la souris
        setOnMouseEntered(event -> {
            if (model.validSquare(row, column)) {
                setStyle("-fx-background-color: green; " +
                          "-fx-font-size: 36px; " +
                          "-fx-font-weight: bold; " +
                          "-fx-text-fill: black; " +
                          "-fx-alignment: center;");
            }
        });

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

        setOnMouseClicked(event -> {
            model.play(row, column);
            setStyle("-fx-background-color: red; " +
                      "-fx-font-size: 36px; " +
                      "-fx-font-weight: bold; " +
                      "-fx-text-fill: black; " +
                      "-fx-alignment: center;");
        });
    }

    public ObjectProperty<Owner> ownerProperty() {
        return ownerProperty;
    }

    public BooleanProperty winnerProperty() {
        return winnerProperty;
    }
}