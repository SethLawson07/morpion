package lawson.lonchi.morpion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import lawson.lonchi.morpion.model.Owner;
import lawson.lonchi.morpion.model.TicTacToeModel;
import static org.junit.jupiter.api.Assertions.*;

class TicTacToeModelTestV1 {

    private TicTacToeModel model;

    @BeforeEach
    public void setUp() {
        model = TicTacToeModel.getInstance();
        model.restart();
    }

    @Test
    public void testInitialBoardIsEmpty() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(Owner.NONE, model.getSquare(i, j).get(), "La case doit être vide à l'initialisation");
            }
        }
    }

    @Test
    public void testNextPlayer(){
        assertEquals(Owner.FIRST, model.turnProperty().get(),"Le premier joueur doit être FIRST(x)");
        model.nextPlayer();
        assertEquals(Owner.SECOND, model.turnProperty().get(),"Le second joueur doit être SECOND(O)");
        model.nextPlayer();
        assertEquals(Owner.FIRST, model.turnProperty().get(), "Le joueur doit revenir à FIRST (X)");

    }

    @Test
    void testValidSquare() {
        assertTrue(model.validSquare(0, 0), "La case (0, 0) doit être valide (libre)");
        model.play(0, 0);
        assertFalse(model.validSquare(0, 0), "La case (0, 0) ne doit plus être valide (occupée)");
    }

    @Test
    void testCheckForWinner() {
        model.play(0, 0); // X
        model.play(1, 0); // O
        model.play(0, 1); // X
        model.play(1, 1); // O
        model.play(0, 2); // X

        assertEquals(Owner.FIRST, model.winnerProperty().get(), "Le gagnant doit être FIRST (X)");
    }

    @Test
    void testCheckForDraw() {
        model.play(0, 0); // X
        model.play(0, 1); // O
        model.play(0, 2); // X
        model.play(1, 0); // O
        model.play(1, 2); // X
        model.play(1, 1); // O
        model.play(2, 0); // X
        model.play(2, 2); // O
        model.play(2, 1); // X

        assertEquals(Owner.NONE, model.winnerProperty().get(), "Le jeu doit se terminer par un match nul");
        assertTrue(model.gameOver().get(), "Le jeu doit être terminé");
    }

    @Test
    void testRestart() {
        model.play(0, 0);
        model.restart();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(Owner.NONE, model.getSquare(i, j).get(), "Le plateau doit être vide après réinitialisation");
            }
        }

        assertEquals(Owner.FIRST, model.turnProperty().get(), "Le joueur courant doit être FIRST (X) après réinitialisation");
        assertEquals(Owner.NONE, model.winnerProperty().get(), "Il ne doit pas y avoir de gagnant après réinitialisation");
    }

    @Test
    void testOpposite() {
        assertEquals(Owner.SECOND, Owner.FIRST.opposite(), "L'opposé de FIRST doit être SECOND");
        assertEquals(Owner.FIRST, Owner.SECOND.opposite(), "L'opposé de SECOND doit être FIRST");
        assertEquals(Owner.NONE, Owner.NONE.opposite(), "L'opposé de NONE doit être NONE");
    }

    @Test
    void testGameOverBinding() {
        assertFalse(model.gameOver().get(), "Le jeu ne doit pas être terminé à l'initialisation");

        model.play(0, 0); // X
        model.play(0, 1); // O
        model.play(0, 2); // X
        model.play(1, 0); // O
        model.play(1, 2); // X
        model.play(1, 1); // O
        model.play(2, 0); // X
        model.play(2, 2); // O
        model.play(2, 1); // X

        assertTrue(model.gameOver().get(), "Le jeu doit être terminé après un match nul");
    }
    
}