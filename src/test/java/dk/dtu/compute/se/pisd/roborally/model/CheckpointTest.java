package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CheckpointTest {
    private final int TEST_WIDTH = 12;
    private final int TEST_HEIGHT = 12;

    private GameController gameController;

    //TODO: midlertidig duplikation
    @BeforeEach
    void setUp () {
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
    void tearDown(){
        gameController = null;
        Checkpoint.setNumberOfCheckpoints(0);
    }

    @Test
    void oneCheckpoint() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        board.getSpace(0,1).setActivatableBoardElement(new Checkpoint());
        gameController.directionMove(current, current.getHeading());
        gameController.executeBoardElements();

        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
        Assertions.assertTrue(current.isPlayerWin(), "Playerwin should be true");
    }

    @Test
    void twoCheckpoints() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        board.getSpace(0,1).setActivatableBoardElement(new Checkpoint());
        board.getSpace(0,2).setActivatableBoardElement(new Checkpoint());
        gameController.directionMove(current, current.getHeading());
        gameController.executeBoardElements();
        gameController.directionMove(current, current.getHeading());
        gameController.executeBoardElements();

        Assertions.assertEquals(current, board.getSpace(0, 2).getPlayer(), "Player " + current.getName() + " should beSpace (0,2)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertTrue(current.isPlayerWin(), "Playerwin should be true");

    }

    @Test
    void threeCheckpointsOrder132() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        board.getSpace(0,1).setActivatableBoardElement(new Checkpoint());
        board.getSpace(0,3).setActivatableBoardElement(new Checkpoint());
        board.getSpace(0,2).setActivatableBoardElement(new Checkpoint());
        gameController.directionMove(current, current.getHeading());
        gameController.executeBoardElements();
        gameController.directionMove(current, current.getHeading());
        gameController.executeBoardElements();
        gameController.directionMove(current, current.getHeading());
        gameController.executeBoardElements();

        Assertions.assertEquals(current.getLastCheckpointVisited(),2);
        Assertions.assertEquals(current, board.getSpace(0, 3).getPlayer(), "Player " + current.getName() + " should beSpace (0,3)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertFalse(current.isPlayerWin(), "Playerwin should be false");
    }




}