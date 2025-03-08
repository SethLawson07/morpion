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

        // Bindings pour le texte et la désactivation
        textProperty().bind(ownerProperty.asString());
        disableProperty().bind(model.gameOver().or(model.getSquare(row, column).isNotEqualTo(Owner.NONE)));

        // Gestion de la souris
        setOnMouseEntered(event -> {
            if (model.validSquare(row, column)) {
                setStyle("-fx-background-color: green;");
            } else {
                setStyle("-fx-background-color: red;");
            }
        });

        setOnMouseExited(event -> {
            if (ownerProperty.get() == Owner.NONE) {
                setStyle("-fx-background-color: cyan;");
            } else {
                setStyle("-fx-background-color: red;");
            }
        });

        setOnMouseClicked(event -> model.play(row, column));
    }

    public ObjectProperty<Owner> ownerProperty() {
        return ownerProperty;
    }

    public BooleanProperty winnerProperty() {
        return winnerProperty;
    }
}