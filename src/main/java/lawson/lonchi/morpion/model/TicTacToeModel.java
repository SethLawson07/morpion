package lawson.lonchi.morpion.model;

import javafx.beans.property.*;
import javafx.beans.binding.*;

public class TicTacToeModel {
    private final static int BOARD_WIDTH = 3;
    private final static int BOARD_HEIGHT = 3;
    private final static int WINNING_COUNT = 3;

    private final IntegerProperty xScore = new SimpleIntegerProperty(0);
private final IntegerProperty oScore = new SimpleIntegerProperty(0);
private final IntegerProperty freeSquares = new SimpleIntegerProperty(9);



    private final ObjectProperty<Owner> turn = new SimpleObjectProperty<>(Owner.FIRST);
    private final ObjectProperty<Owner> winner = new SimpleObjectProperty<>(Owner.NONE);
    private final ObjectProperty<Owner>[][] board = new SimpleObjectProperty[BOARD_WIDTH][BOARD_HEIGHT];
    private final BooleanProperty[][] winningBoard = new BooleanProperty[BOARD_WIDTH][BOARD_HEIGHT];

    private TicTacToeModel() {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                board[i][j] = new SimpleObjectProperty<>(Owner.NONE);
                winningBoard[i][j] = new SimpleBooleanProperty(false);
            }
        }
    }

    public static TicTacToeModel getInstance() {
        return TicTacToeModelHolder.INSTANCE;
    }

    private static class TicTacToeModelHolder {
        private static final TicTacToeModel INSTANCE = new TicTacToeModel();
    }

    // public void restart() {
    //     for (int i = 0; i < BOARD_WIDTH; i++) {
    //         for (int j = 0; j < BOARD_HEIGHT; j++) {
    //             board[i][j].set(Owner.NONE);
    //             winningBoard[i][j].set(false);
    //         }
    //     }
    //     turn.set(Owner.FIRST);
    //     winner.set(Owner.NONE);
    //     System.out.println("Modèle réinitialisé.");
    // }
    
    public void restart(){
            for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                board[i][j].set(Owner.NONE);
                winningBoard[i][j].set(false);
            }
        }
        turn.set(Owner.FIRST);
        winner.set(Owner.NONE);
        System.out.println("Modèle réinitialisé.");
        xScore.set(0);
        oScore.set(0);
        freeSquares.set(9);

    }

    public ObjectProperty<Owner> turnProperty() {
        return turn;
    }

    public ObjectProperty<Owner> getSquare(int row, int column) {
        return board[row][column];
    }

    public BooleanProperty getWinningSquare(int row, int column) {
        return winningBoard[row][column];
    }

    // public StringExpression getEndOfGameMessage() {
    //     return Bindings.when(winner.isEqualTo(Owner.NONE))
    //         .then("Match nul")
    //         .otherwise(Bindings.concat(winner.get().toString(), " a gagné !"));
    // }
    // public StringExpression getEndOfGameMessage() {
    //     return Bindings.when(winner.isEqualTo(Owner.NONE))
    //         .then("") // Ne rien afficher si le jeu est en cours
    //         .otherwise(Bindings.when(winner.isEqualTo(Owner.FIRST))
    //             .then("Game over: Le gagnant est le premier joueur") // X gagne
    //             .otherwise(Bindings.when(winner.isEqualTo(Owner.SECOND))
    //                 .then("Game over: Le gagnant est le deuxième joueur") // O gagne
    //                 .otherwise("Game over: Match nul"))); // Match nul
    // }
    public StringExpression getEndOfGameMessage() {
        return Bindings.when(winner.isEqualTo(Owner.NONE).and(getFreeSquares().greaterThan(0)))
            .then("")
            .otherwise(
                Bindings.when(winner.isEqualTo(Owner.NONE))
                    .then("Game over: Match nul")
                    .otherwise(Bindings.createStringBinding(
                        () -> "Game over: Le gagnant est le " +
                              (winner.get() == Owner.FIRST ? "premier joueur" : "second joueur"),
                        winner
                    ))
            );
    }
    
    
    
    

    public void setWinner(Owner winner) {
        this.winner.set(winner);
        System.out.println("Gagnant défini : " + winner);
    }

    public boolean validSquare(int row, int column) {
        return board[row][column].get() == Owner.NONE && winner.get() == Owner.NONE;
    }

    public void nextPlayer() {
        turn.set(turn.get().opposite());
        System.out.println("Tour suivant : " + turn.get());
    }
    

    // public void play(int row, int column) {
    //     if (validSquare(row, column)) {
    //         board[row][column].set(turn.get());
    //         System.out.println("Joueur " + turn.get() + " joue en (" + row + ", " + column + ")");

    //         // Affiche les scores et les cases libres après chaque coup
    //         System.out.println(getScore(Owner.FIRST).intValue() + " case pour X");
    //         System.out.println(getScore(Owner.SECOND).intValue() + " case pour O");
    //         System.out.println(getFreeSquares().intValue() + " cases libres");

    //         checkForWinner(row, column);
    //         nextPlayer();
    //     }
    // }

    public void play(int row, int column) {
        if (validSquare(row, column)) {
            board[row][column].set(turn.get());
            
            if (turn.get() == Owner.FIRST) {
                xScore.set(xScore.get() + 1);
            } else {
                oScore.set(oScore.get() + 1);
            }
    
            freeSquares.set(freeSquares.get() - 1);
    
            checkForWinner(row, column);
            nextPlayer();
        }
    }
    
    

    public BooleanBinding legalMove(int row, int column) {
        return Bindings.createBooleanBinding(() -> validSquare(row, column), board[row][column], winner);
    }

    // public NumberExpression getScore(Owner owner) {
    //     int count = 0;
    //     for (int i = 0; i < BOARD_WIDTH; i++) {
    //         for (int j = 0; j < BOARD_HEIGHT; j++) {
    //             if (board[i][j].get() == owner) {
    //                 count++;
    //             }
    //         }
    //     }
    //     return new SimpleIntegerProperty(count);
    // }
    public IntegerProperty getScore(Owner owner) {
        return (owner == Owner.FIRST) ? xScore : oScore;
    }

    // public NumberExpression getFreeSquares() {
    //     int count = 0;
    //     for (int i = 0; i < BOARD_WIDTH; i++) {
    //         for (int j = 0; j < BOARD_HEIGHT; j++) {
    //             if (board[i][j].get() == Owner.NONE) {
    //                 count++;
    //             }
    //         }
    //     }
    //     return new SimpleIntegerProperty(count);
    // }

    public IntegerProperty getFreeSquares() {
        return freeSquares;
    }

    public BooleanBinding gameOver() {
        return winner.isNotEqualTo(Owner.NONE);
    }

    private void checkForWinner(int row, int column) {
        Owner currentPlayer = board[row][column].get();
        if (checkLine(row, currentPlayer) || checkColumn(column, currentPlayer) || checkDiagonals(currentPlayer)) {
            setWinner(currentPlayer);
        } else if (isBoardFull()) {
            setWinner(Owner.NONE);
        }
    }

 
    private boolean checkLine(int row, Owner player) {
        for (int col = 0; col < BOARD_WIDTH; col++) {
            if (board[row][col].get() != player) {
                return false;
            }
        }
        return true;
    }

    

    private boolean checkColumn(int column, Owner player) {
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            if (board[row][column].get() != player) {
                return false;
            }
        }
        return true;
    }


    private boolean checkDiagonals(Owner player) {
        boolean diagonal1 = true, diagonal2 = true;
        for (int i = 0; i < BOARD_WIDTH; i++) {
            if (board[i][i].get() != player) {
                diagonal1 = false;
            }
            if (board[i][BOARD_WIDTH - 1 - i].get() != player) {
                diagonal2 = false;
            }
        }
        return diagonal1 || diagonal2;
    }


    
    private boolean isBoardFull() {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                if (board[i][j].get() == Owner.NONE) {
                    return false;
                }
            }
        }
        return true;
    }
}