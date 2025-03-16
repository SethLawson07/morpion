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

        /**
         * Initialise le contrôleur et configure les bindings entre la vue et le modèle.
         * Cette méthode est appelée automatiquement après le chargement du fichier
         * FXML.
         */
        @FXML
        public void initialize() {
            xScoreLabel.textProperty().bind(model.getScore(Owner.FIRST).asString().concat(" cases pour X"));
            oScoreLabel.textProperty().bind(model.getScore(Owner.SECOND).asString().concat(" cases pour O"));
            freeSquaresLabel.textProperty().bind(model.getFreeSquares().asString().concat(" cases libres"));
            endOfGameMessage.textProperty().bind(model.getEndOfGameMessage());

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    TicTacToeSquare square = new TicTacToeSquare(i, j, this);
                    square.getStyleClass().add("tic-tac-toe-button"); 
                    gridPane.add(square, j, i);

                }
            }
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
            model.restart(); 

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    TicTacToeSquare square = (TicTacToeSquare) gridPane.getChildren().get(i * 3 + j);
                    square.setStyle("-fx-background-color: white; " +
                            "-fx-font-size: 25px; " +
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
            Owner turnOwner = model.turnProperty().get();
                    if(turnOwner==Owner.FIRST){
                        xScoreLabel.setStyle("-fx-background-color: cyan;");
                        oScoreLabel.setStyle("-fx-background-color: red;");
                    }else if(turnOwner==Owner.SECOND){
                        xScoreLabel.setStyle("-fx-background-color: red;");
                        oScoreLabel.setStyle("-fx-background-color: cyan;");
                    }

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    
                    TicTacToeSquare square = (TicTacToeSquare) gridPane.getChildren().get(i * 3 + j);

                    Owner owner = model.getSquare(i, j).get();
                    if (model.getWinningSquare(i, j).get()) {
                        square.setStyle("-fx-background-color: brown; " +
                                "-fx-font-size: 25px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-text-fill: black; " +
                                "-fx-alignment: center;");

                    } else if (model.gameOver().get() && !model.getWinningSquare(i, j).get()) {
                        square.setStyle("-fx-background-color: white; ");
                        xScoreLabel.setStyle("-fx-background-color: red;");
                        oScoreLabel.setStyle("-fx-background-color: red;");
                    }

                    else if (owner != Owner.NONE) {
                        square.setStyle("-fx-background-color: red;");
                    }
                }
            }
        }
    }