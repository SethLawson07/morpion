package lawson.lonchi;

import static org.junit.jupiter.api.Assertions.*;

import lawson.lonchi.morpion.model.Owner;
import lawson.lonchi.morpion.model.TicTacToeModel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Classe de test pour le modèle du jeu de Morpion (Tic-Tac-Toe).
 * Utilise JUnit 5 pour tester les fonctionnalités essentielles du jeu.
 */
class TicTacToeModelTest {
    private TicTacToeModel model;

    /**
     * Initialise le modèle avant chaque test en le réinitialisant.
     */
    @BeforeEach
    void setUp() {
        model = TicTacToeModel.getInstance();
        model.restart();
    }

    /**
     * Vérifie que l'état initial du jeu est correct.
     */
    @Test
    void testInitialState() {
        assertEquals(Owner.FIRST, model.turnProperty().get(), "Le premier joueur doit commencer.");
        assertEquals(Owner.NONE, model.winnerProperty().get(), "Il ne doit pas y avoir de gagnant au début.");
        assertEquals(9, model.getFreeSquares().get(), "Le plateau doit être entièrement libre au début.");
    }

    /**
     * Vérifie qu'un coup valide est accepté et que la case est bien occupée.
     */
    @Test
    void testValidMove() {
        assertTrue(model.validSquare(0, 0), "La case (0,0) doit être valide au début.");
        model.play(0, 0);
        assertEquals(Owner.FIRST, model.getSquare(0, 0).get(), "La case (0,0) doit être occupée par le premier joueur.");
        assertFalse(model.validSquare(0, 0), "La case (0,0) ne doit plus être valide après un coup.");
    }

    /**
     * Vérifie que le tour change après un coup.
     */
    @Test
    void testTurnChange() {
        model.play(0, 0);
        assertEquals(Owner.SECOND, model.turnProperty().get(), "Après un coup, le tour doit changer pour le deuxième joueur.");
    }

    /**
     * Vérifie qu'une victoire par ligne est correctement détectée.
     */
    @Test
    void testWinningRow() {
        model.play(0, 0); // X
        model.play(1, 0); // O
        model.play(0, 1); // X
        model.play(1, 1); // O
        model.play(0, 2); // X gagne

        assertEquals(Owner.FIRST, model.winnerProperty().get(), "Le premier joueur doit gagner avec une ligne complète.");
        assertTrue(model.gameOver().get(), "Le jeu doit être terminé après une victoire.");
    }

    /**
     * Vérifie qu'une victoire par colonne est correctement détectée.
     */
    @Test
    void testWinningColumn() {
        model.play(0, 0); // X
        model.play(0, 1); // O
        model.play(1, 0); // X
        model.play(1, 1); // O
        model.play(2, 0); // X gagne

        assertEquals(Owner.FIRST, model.winnerProperty().get(), "Le premier joueur doit gagner avec une colonne complète.");
        assertTrue(model.gameOver().get(), "Le jeu doit être terminé après une victoire.");
    }

    /**
     * Vérifie qu'une victoire par diagonale est correctement détectée.
     */
    @Test
    void testWinningDiagonal() {
        model.play(0, 0); // X
        model.play(0, 1); // O
        model.play(1, 1); // X
        model.play(0, 2); // O
        model.play(2, 2); // X gagne

        assertEquals(Owner.FIRST, model.winnerProperty().get(), "Le premier joueur doit gagner avec une diagonale.");
        assertTrue(model.gameOver().get(), "Le jeu doit être terminé après une victoire.");
    }

    /**
     * Vérifie la détection d'un match nul.
     */
    @Test
    void testDrawGame() {
        model.play(0, 0); // X
        model.play(0, 1); // O
        model.play(0, 2); // X
        model.play(1, 1); // O
        model.play(1, 0); // X
        model.play(1, 2); // O
        model.play(2, 1); // X
        model.play(2, 0); // O
        model.play(2, 2); // X (dernier coup, match nul)

        assertEquals(Owner.NONE, model.winnerProperty().get(), "Le jeu doit se terminer sur un match nul.");
        assertTrue(model.gameOver().get(), "Le jeu doit être terminé après un match nul.");
    }

    /**
     * Vérifie que le jeu est bien réinitialisé après un restart.
     */
    @Test
    void testRestartGame() {
        model.play(0, 0);
        model.play(1, 1);
        model.restart();

        assertEquals(Owner.FIRST, model.turnProperty().get(), "Après un restart, le premier joueur doit commencer.");
        assertEquals(Owner.NONE, model.winnerProperty().get(), "Il ne doit pas y avoir de gagnant après un restart.");
        assertEquals(9, model.getFreeSquares().get(), "Toutes les cases doivent être libres après un restart.");

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(Owner.NONE, model.getSquare(i, j).get(), "Toutes les cases doivent être réinitialisées.");
            }
        }
    }
}
