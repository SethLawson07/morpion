package lawson.lonchi.morpion.model;

import javafx.beans.property.*;
import lawson.lonchi.morpion.controller.TicTacToeController;

import java.util.ArrayList;
import java.util.List;

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
    
    @SuppressWarnings("unchecked")
    private final ObjectProperty<Owner>[][] board = new SimpleObjectProperty[BOARD_WIDTH][BOARD_HEIGHT];
    
    private final BooleanProperty[][] winningBoard = new BooleanProperty[BOARD_WIDTH][BOARD_HEIGHT];
    
    TicTacToeController controller;
    
    
    
    /**
    * Constructeur privé pour initialiser le plateau de jeu et les cases gagnantes.
    */
    private TicTacToeModel() {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                board[i][j] = new SimpleObjectProperty<>(Owner.NONE);
                winningBoard[i][j] = new SimpleBooleanProperty(false);
            }
        }
    }
    
    /**
    * @return L'instance unique du modèle.
    */
    public static TicTacToeModel getInstance() {
        return TicTacToeModelHolder.INSTANCE;
    }
    
    /**
    * Classe interne pour gérer l'instance unique du modèle.
    */
    private static class TicTacToeModelHolder {
        private static final TicTacToeModel INSTANCE = new TicTacToeModel();
    }
    
    /**
    * Réinitialise le jeu en remettant à zéro le plateau, les scores et les propriétés.
    */
    public void restart() {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                board[i][j].set(Owner.NONE);
                winningBoard[i][j].set(false);
            }
        }
        turn.set(Owner.FIRST);
        winner.set(Owner.NONE);
        // System.out.println("Modèle réinitialisé.");
        xScore.set(0);
        oScore.set(0);
        freeSquares.set(9);
    }
    
    /**
    * @return La propriété du tour actuel.
    */
    public ObjectProperty<Owner> turnProperty() {
        return turn;
    }
    
    public ObjectProperty<Owner> winnerProperty() {
        return winner;
    }
    
    /**
    * Retourne la propriété d'une case spécifique du plateau.
    *
    * @param row    La ligne de la case.
    * @param column La colonne de la case.
    * @return La propriété de la case spécifiée.
    */
    public ObjectProperty<Owner> getSquare(int row, int column) {
        return board[row][column];
    }
    
    /**
    * Retourne la propriété d'une case gagnante.
    *
    * @param row    La ligne de la case.
    * @param column La colonne de la case.
    * @return La propriété de la case gagnante spécifiée.
    */
    public BooleanProperty getWinningSquare(int row, int column) {
        return winningBoard[row][column];
    }
    
    /**
    * Retourne le message de fin de jeu sous forme de StringExpression.
    * Le message indique soit un match nul, soit le gagnant.
    *
    * @return Le message de fin de jeu.
    */
    public StringExpression getEndOfGameMessage() {
        return Bindings.when(winner.isEqualTo(Owner.NONE).and(getFreeSquares().greaterThan(0)))
        .then("")
        .otherwise(
        Bindings.when(winner.isEqualTo(Owner.NONE))
        .then("Game over: Match nul")
        .otherwise(Bindings.createStringBinding(
        () -> "Game over: Le gagnant est le " +
        (winner.get() == Owner.FIRST ? "premier joueur (X)" : "second joueur (O)"),
        winner
        ))
        );
    }
    
    /**
    * Définit le gagnant du jeu.
    *
    * @param winner Le gagnant du jeu (FIRST pour X, SECOND pour O, NONE pour match nul).
    */
    public void setWinner(Owner winner) {
        this.winner.set(winner);
    }
    
    /**
    * Vérifie si une case est valide (libre et jeu non terminé).
    *
    * @param row    La ligne de la case.
    * @param column La colonne de la case.
    * @return true si la case est valide, false sinon.
    */
    public boolean validSquare(int row, int column) {
        return board[row][column].get() == Owner.NONE && winner.get() == Owner.NONE;
    }
    
    /**
    * Passe au joueur suivant.
    */
    public void nextPlayer() {
        turn.set(turn.get().opposite());
    }
    
    /**
    * Joue dans une case spécifique si elle est valide.
    * Met à jour les scores et vérifie s'il y a un gagnant.
    *
    * @param row    La ligne de la case.
    * @param column La colonne de la case.
    */
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
    
    /**
    * Retourne un BooleanBinding qui vérifie si un mouvement est légal.
    *
    * @param row    La ligne de la case.
    * @param column La colonne de la case.
    * @return Un BooleanBinding qui vérifie si le mouvement est légal.
    */
    public BooleanBinding legalMove(int row, int column) {
        return Bindings.createBooleanBinding(() -> validSquare(row, column), board[row][column], winner);
    }
    
    /**
    * Retourne le score d'un joueur spécifique.
    *
    * @param owner Le joueur (FIRST pour X, SECOND pour O).
    * @return La propriété du score du joueur spécifié.
    */
    public IntegerProperty getScore(Owner owner) {
        return (owner == Owner.FIRST) ? xScore : oScore;
    }
    
    /**
    * Retourne le nombre de cases libres.
    *
    * @return La propriété du nombre de cases libres.
    */
    public IntegerProperty getFreeSquares() {
        return freeSquares;
    }
    
    /**
    * Retourne un BooleanBinding qui vérifie si le jeu est terminé.
    *
    * @return Un BooleanBinding qui vérifie si le jeu est terminé.
    */
    public BooleanBinding gameOver() {
        return winner.isNotEqualTo(Owner.NONE).or(getFreeSquares().isEqualTo(0));
    }
    
    
    /**
    * Vérifie s'il y a un gagnant après un coup.
    *
    * @param row    La ligne de la case jouée.
    * @param column La colonne de la case jouée.
    */
    private void checkForWinner(int row, int column) {
        Owner currentPlayer = board[row][column].get();
        List<int[]> winningSquares = getWinningSquares(currentPlayer);
        if (winningSquares != null) {
            markWinningSquares(winningSquares); 
            setWinner(currentPlayer); 
        } else if (isBoardFull()) {
            setWinner(Owner.NONE); 
        }
    }
    
    /**
    * Retourne les cases gagnantes pour un joueur donné.
    *
    * @param player Le joueur à vérifier.
    * @return Une liste des cases gagnantes, ou null si aucun gagnant.
    */
    private List<int[]> getWinningSquares(Owner player) {
        List<int[]> winningSquares = checkLineForWinner(player);
        if (winningSquares != null) return winningSquares;
        
        winningSquares = checkColumnForWinner(player);
        if (winningSquares != null) return winningSquares;
        
        winningSquares = checkDiagonalsForWinner(player);
        return winningSquares;
    }
    
    /**
    * Vérifie si une ligne est gagnante et retourne les cases gagnantes.
    *
    * @param player Le joueur à vérifier.
    * @return Une liste des cases gagnantes, ou null si aucune ligne gagnante.
    */
    private List<int[]> checkLineForWinner(Owner player) {
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            List<int[]> squares = new ArrayList<>();
            for (int col = 0; col < BOARD_WIDTH; col++) {
                if (board[row][col].get() == player) {
                    squares.add(new int[]{row, col});
                } else {
                    squares.clear();
                    break;
                }
            }
            if (squares.size() == WINNING_COUNT) {
                return squares;
            }
        }
        return null;
    }
    
    /**
    * Vérifie si une colonne est gagnante et retourne les cases gagnantes.
    *
    * @param player Le joueur à vérifier.
    * @return Une liste des cases gagnantes, ou null si aucune colonne gagnante.
    */
    private List<int[]> checkColumnForWinner(Owner player) {
        for (int col = 0; col < BOARD_WIDTH; col++) {
            List<int[]> squares = new ArrayList<>();
            for (int row = 0; row < BOARD_HEIGHT; row++) {
                if (board[row][col].get() == player) {
                    squares.add(new int[]{row, col});
                } else {
                    squares.clear();
                    break;
                }
            }
            if (squares.size() == WINNING_COUNT) {
                return squares;
            }
        }
        return null;
    }
    
    /**
    * Vérifie si une diagonale est gagnante et retourne les cases gagnantes.
    *
    * @param player Le joueur à vérifier.
    * @return Une liste des cases gagnantes, ou null si aucune diagonale gagnante.
    */
    private List<int[]> checkDiagonalsForWinner(Owner player) {
        List<int[]> squares = new ArrayList<>();
        for (int i = 0; i < BOARD_WIDTH; i++) {
            if (board[i][i].get() == player) {
                squares.add(new int[]{i, i});
            } else {
                squares.clear();
                break;
            }
        }
        if (squares.size() == WINNING_COUNT) {
            return squares;
        }
        
        squares.clear();
        for (int i = 0; i < BOARD_WIDTH; i++) {
            if (board[i][BOARD_WIDTH - 1 - i].get() == player) {
                squares.add(new int[]{i, BOARD_WIDTH - 1 - i});
            } else {
                squares.clear();
                break;
            }
        }
        if (squares.size() == WINNING_COUNT) {
            return squares;
        }
        
        return null;
    }
    
    
    /**
    * Vérifie si le plateau est plein (match nul).
    *
    * @return true si le plateau est plein, false sinon.
    */
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
    
    /**
    * Marque les cases gagnantes sur le plateau.
    *
    * @param winningSquares Les cases gagnantes à marquer.
    */
    public void markWinningSquares(List<int[]> winningSquares) {
        for (int[] square : winningSquares) {
            int row = square[0];
            int col = square[1];
            winningBoard[row][col].set(true);
        }
    }
}