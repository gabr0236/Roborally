package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class ConveyorTest {
    private final int TEST_WIDTH = 12;
    private final int TEST_HEIGHT = 12;

    private GameController gameController;

    @BeforeEach
    void setUp() {
        Board board = new Board(TEST_WIDTH, TEST_HEIGHT);
        gameController = new GameController(board);
        for (int i = 0; i < 6; i++) {
            Player player = new Player(board, "Black","Player " + i);
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
     * @author @Gabriel
     */
    @Test
    void moveForward() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        current.getSpace().getActivatableBoardElements().add(new Conveyor(Heading.SOUTH,Command.FORWARD));
        gameController.executeBoardElements();
        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
    }

    /**
     * @author @Gabriel
     */
    @Test
    void fastForward() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        current.getSpace().getActivatableBoardElements().add(new Conveyor(Heading.SOUTH,Command.FAST_FORWARD));
        gameController.executeBoardElements();
        Assertions.assertEquals(current, board.getSpace(0, 2).getPlayer(), "Player " + current.getName() + " should beSpace (0,2)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
    }

    /**
     * @author @Gabriel
     */
    @Test
    void moveForwardSouthFacingNorth() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.setHeading(Heading.NORTH);
        current.getSpace().getActivatableBoardElements().add(new Conveyor(Heading.SOUTH,Command.FORWARD));
        gameController.executeBoardElements();
        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        Assertions.assertEquals(Heading.NORTH, current.getHeading(), "Player 0 should be heading NORTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
    }

    /**
     * @author @Gabriel
     */
    @Test
    void conveyorMoveForwardSouthFacingNorthWallBlock() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.setHeading(Heading.NORTH);
        current.getSpace().getActivatableBoardElements().add(new Conveyor(Heading.SOUTH,Command.FORWARD));
        board.getNeighbour(current.getSpace(),Heading.SOUTH).setWallList(new ArrayList<Heading>() {{ add(Heading.NORTH); }});
        gameController.executeBoardElements();
        Assertions.assertEquals(current, board.getSpace(0, 0).getPlayer(), "Player " + current.getName() + " should beSpace (0,0)!");
        Assertions.assertEquals(Heading.NORTH, current.getHeading(), "Player 0 should be heading NORTH!");
        Assertions.assertNull(board.getSpace(0, 1).getPlayer(), "Space (0,0) should be empty!");
    }

    /**
     * @author Sebastian
     */
    @Test
    void conveyorPushingPlayerOutOfField() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.setSpace(board.getSpace(3,0));
        board.rebootBorderXValues.add(2);
        board.getSpace(3,5).setReboot(new Reboot(Heading.EAST,false,6));
        current.setRebootSpace(board.getSpace(3,5));
        current.getSpace().getActivatableBoardElements().add(new Conveyor(Heading.NORTH,Command.FORWARD));
        gameController.updateAllReboot();
        gameController.executeBoardElements();
        gameController.respawnPlayers();

        Assertions.assertNull(board.getSpace(3,0).getPlayer());
        Assertions.assertEquals(current, board.getSpace(3, 5).getPlayer(), "Player " + current.getName() + " should beSpace (3,5)!");
        Assertions.assertEquals(Heading.EAST, current.getHeading(), "Player 0 should be heading EAST!");
    }
}