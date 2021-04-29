package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.boardElements.Checkpoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CheckpointTest {
    private final int TEST_WIDTH = 12;
    private final int TEST_HEIGHT = 12;

    private GameController gameController;


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
    }

    /**
     * @author @Gabriel
     */
    @Test
    void oneCheckpoint() {
        Board board = gameController.board;
        board.setNumberOfCheckpoints(1);
        Player current = board.getCurrentPlayer();
        board.getSpace(0,1).getActivatableBoardElements().add(new Checkpoint(1));
        gameController.directionMove(current, current.getHeading());
        gameController.executeBoardElements();

        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer()
                , "Player " + current.getName() + " should beSpace (0,1)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
        Assertions.assertTrue(current.isPlayerWin(), "Playerwin should be true");
    }

    /**
     * @author @Gabriel
     */
    @Test
    void twoCheckpoints() {
        Board board = gameController.board;
        board.setNumberOfCheckpoints(2);
        Player current = board.getCurrentPlayer();
        board.getSpace(0,1).getActivatableBoardElements().add(new Checkpoint(1));
        board.getSpace(0,2).getActivatableBoardElements().add(new Checkpoint(2));
        gameController.directionMove(current, current.getHeading());
        gameController.executeBoardElements();
        gameController.directionMove(current, current.getHeading());
        gameController.executeBoardElements();

        Assertions.assertEquals(current, board.getSpace(0, 2).getPlayer(), "Player " + current.getName() + " should beSpace (0,2)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertTrue(current.isPlayerWin(), "Playerwin should be true");

    }

    /**
     * @author @Gabriel
     */
    @Test
    void threeCheckpointsOrder132() {
        Board board = gameController.board;
        board.setNumberOfCheckpoints(3);
        Player current = board.getCurrentPlayer();
        board.getSpace(0,1).getActivatableBoardElements().add(new Checkpoint(1));
        board.getSpace(0,3).getActivatableBoardElements().add(new Checkpoint(2));
        board.getSpace(0,2).getActivatableBoardElements().add(new Checkpoint(3));
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