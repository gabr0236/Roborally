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
            Player player = new Player(board, null, "Player " + i);
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

    @Test
    void moveForward() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        board.getSpace(0,1).setActivatableBoardElement(new Checkpoint());
        gameController.directionMove(current, current.getHeading());
        gameController.directionMove(current, current.getHeading());

        int i = 1;
        for (Space space: board.getSpacesList()) {
            if(space.getActivatableBoardElement()!=null){
                if(space.getActivatableBoardElement() instanceof Checkpoint){
                    System.out.println("check " + i + "  " + space.toString());
                    i++;
                }
            }
        }

        Assertions.assertEquals(current, board.getSpace(0, 2).getPlayer(), "Player " + current.getName() + " should beSpace (0,2)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
        Assertions.assertTrue(current.isPlayerWin(), "Playerwin should be true");
    }

}