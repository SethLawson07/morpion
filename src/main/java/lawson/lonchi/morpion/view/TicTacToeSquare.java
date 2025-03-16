package lawson.lonchi.morpion.view;

import javafx.scene.control.TextField;
import javafx.beans.property.*;
import lawson.lonchi.morpion.model.TicTacToeModel;
import lawson.lonchi.morpion.controller.TicTacToeController;
import lawson.lonchi.morpion.model.Owner;


public class TicTacToeSquare extends TextField {
    
    private static TicTacToeModel model = TicTacToeModel.getInstance();
    
    private final ObjectProperty<Owner> ownerProperty = new SimpleObjectProperty<>(Owner.NONE);
    
    private final BooleanProperty winnerProperty = new SimpleBooleanProperty(false);
    
    
    /**
    * Constructeur pour créer une case du plateau de jeu.
    *
    * @param row    La ligne de la case.
    * @param column La colonne de la case.
    */
    public TicTacToeSquare(final int row, final int column,TicTacToeController controller) {
        
        setEditable(false);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        
        setStyle("-fx-background-color: white; " +
        "-fx-font-size: 25px; " +
        "-fx-text-fill: black; " +
        "-fx-alignment: center;");
        
        ownerProperty.bind(model.getSquare(row, column));
        
        textProperty().bind(ownerProperty.asString());
        
        disableProperty().bind(model.gameOver().or(model.getSquare(row, column).isNotEqualTo(Owner.NONE)));
        
        setOnMouseEntered(event -> {
            if (model.validSquare(row, column)) {
                setStyle("-fx-background-color: green; " +
                "-fx-font-size: 25px; " +
                "-fx-text-fill: black; " +
                "-fx-alignment: center;");
            }
        });
        
        setOnMouseExited(event -> {
            if (ownerProperty.get() == Owner.NONE) {
                setStyle("-fx-background-color: white; " +
                "-fx-font-size: 25px; " +
                "-fx-text-fill: black; " +
                "-fx-alignment: center;");
            }
        });
        
        setOnMouseClicked(event -> {
            controller.handleButtonClick(row, column);
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




