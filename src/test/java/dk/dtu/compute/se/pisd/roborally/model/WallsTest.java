package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class WallsTest {
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
    void wallOnCurrentSpaceBlockingMoveForward(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.getSpace().setWallList(new ArrayList<Heading>() {{ add(Heading.SOUTH); }});
        gameController.directionMove(current, current.getHeading());

        Assertions.assertEquals(current, board.getSpace(0, 0).getPlayer(), "Player " + current.getName() + " should beSpace (0,0)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
    }

    /**
     * @author @Gabriel
     */
    @Test
    void wallOnCurrentSpaceNotBlockingMoveForward(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.getSpace().setWallList(new ArrayList<Heading>() {{ add(Heading.EAST); }});
        gameController.directionMove(current, current.getHeading());

        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,0)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
    }

    /**
     * @author @Gabriel
     */
    @Test
    void wallOnCurrentSpaceNotBlockingMoveForward2(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.getSpace().setWallList(new ArrayList<Heading>() {{ add(Heading.NORTH); }});
        gameController.directionMove(current, current.getHeading());

        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,0)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
    }

    /**
     * @author @Gabriel
     */
    @Test
    void wallOnNextSpaceBlockingMoveForward(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.board.getNeighbour(current.getSpace(),current.getHeading()).setWallList(new ArrayList<Heading>() {{ add(Heading.NORTH); }});
        gameController.directionMove(current, current.getHeading());

        Assertions.assertEquals(current, board.getSpace(0, 0).getPlayer(), "Player " + current.getName() + " should beSpace (0,0)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
    }

    /**
     * @author @Gabriel
     */
    @Test
    void wallOnNextSpaceNotBlockingMoveForward(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.board.getNeighbour(current.getSpace(),current.getHeading()).setWallList(new ArrayList<Heading>() {{ add(Heading.WEST); }});
        gameController.directionMove(current, current.getHeading());

        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
    }

    /**
     * @author @Gabriel
     */
    @Test
    void wallOnNextSpaceNotBlockingMoveForward2(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.board.getNeighbour(current.getSpace(),current.getHeading()).setWallList(new ArrayList<Heading>() {{ add(Heading.SOUTH); }});
        gameController.directionMove(current, current.getHeading());

        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
    }
}