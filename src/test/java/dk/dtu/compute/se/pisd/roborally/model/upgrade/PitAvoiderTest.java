package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PitAvoiderTest {

    private final int TEST_WIDTH = 12;
    private final int TEST_HEIGHT = 12;

    private GameController gameController;

    @BeforeEach
    void setUp() {
        Board board = new Board(TEST_WIDTH, TEST_HEIGHT);
        gameController = new GameController(board);
        for (int i = 0; i < 6; i++) {
            Player player = new Player(board, "Black", "Player " + i);
            board.addPlayer(player);
            player.setSpace(board.getSpace(i, i));
            player.setHeading(Heading.values()[i % Heading.values().length]);
        }
        board.setCurrentPlayer(board.getPlayer(0));
    }

    @AfterEach
    void tearDown() {
        gameController = null;
    }

    /**
     * @author Sebastian
     */
    @Test
    void walkOverPit() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.getUpgrades().add(new PitAvoider());
        current.setSpace(board.getSpace(0, 0));
        current.setHeading(Heading.EAST);
        board.getSpace(1, 0).setPit(true);
        gameController.executeCommand(current, current.getHeading(), Command.FAST_FORWARD);

        Assertions.assertEquals(current, board.getSpace(2, 0).getPlayer(), "Player " + current.getName() + " should beSpace (2,0)!, but was actually" + current.getSpace());
        Assertions.assertEquals(Heading.EAST, current.getHeading(), "Player 0 should be heading EAST!");
    }

    /**
     * @author Sebastian
     */
    @Test
    void fallIntoPit() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.getUpgrades().add(new PitAvoider());
        current.setSpace(board.getSpace(0, 0));
        current.setHeading(Heading.EAST);
        board.getSpace(2, 0).setPit(true);
        gameController.executeCommand(current, current.getHeading(), Command.FAST_FORWARD);

        Assertions.assertNull(current.getSpace(), "Player " + current.getName() + " should beSpace null!, but was actually" + current.getSpace());
    }

    /**
     * @author Sebastian
     */
    @Test
    void pushAndFallIntoPit() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Player second = board.getPlayer(1);
        current.getUpgrades().add(new PitAvoider());
        current.setSpace(board.getSpace(0, 0));
        current.setHeading(Heading.EAST);
        second.setSpace(board.getSpace(1, 0));
        board.getSpace(2, 0).setPit(true);
        gameController.executeCommand(current, current.getHeading(), Command.FAST_FORWARD);

        Assertions.assertNull(current.getSpace(), "Player " + current.getName() + " should beSpace null!, but was actually" + current.getSpace());
        Assertions.assertNull(second.getSpace(), "Player " + second.getName() + " should beSpace null!, but was actually" + second.getSpace());

    }

    /**
     * @author Sebastian
     */
    @Test
    void walkOver2Pits() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.getUpgrades().add(new PitAvoider());
        current.setSpace(board.getSpace(0, 0));
        current.setHeading(Heading.EAST);
        board.getSpace(1, 0).setPit(true);
        board.getSpace(2, 0).setPit(true);
        gameController.executeCommand(current, current.getHeading(), Command.MOVE_x3);

        Assertions.assertEquals(current, board.getSpace(3, 0).getPlayer(), "Player " + current.getName() + " should beSpace (3,0)!, but was actually" + current.getSpace());
        Assertions.assertEquals(Heading.EAST, current.getHeading(), "Player 0 should be heading EAST!");
    }

    @Test
    void fallIntoPit2() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.getUpgrades().add(new PitAvoider());
        current.setSpace(board.getSpace(0, 0));
        current.setHeading(Heading.EAST);
        board.getSpace(1, 0).setPit(true);
        board.getSpace(3, 0).setPit(true);
        gameController.executeCommand(current, current.getHeading(), Command.MOVE_x3);

        Assertions.assertNull(current.getSpace(), "Player " + current.getName() + " should beSpace null!, but was actually" + current.getSpace());
    }

    /**
     * @author Sebastian
     */
    @Test
    void pushAndFallIntoPit2() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Player second = board.getPlayer(1);
        Player third = board.getPlayer(2);
        current.getUpgrades().add(new PitAvoider());
        current.setSpace(board.getSpace(0, 0));
        current.setHeading(Heading.EAST);
        second.setSpace(board.getSpace(1, 0));
        third.setSpace(board.getSpace(2, 0));
        board.getSpace(3, 0).setPit(true);
        gameController.executeCommand(current, current.getHeading(), Command.MOVE_x3);

        Assertions.assertNull(current.getSpace(), "Player " + current.getName() + " should beSpace null!, but was actually" + current.getSpace());
        Assertions.assertNull(second.getSpace(), "Player " + second.getName() + " should beSpace null!, but was actually" + second.getSpace());
        Assertions.assertNull(second.getSpace(), "Player " + third.getName() + " should beSpace null!, but was actually" + third.getSpace());
    }
}
