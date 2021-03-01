package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WallTest {
    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    private GameController gameController;

    //TODO: midlertidig duplikation
    @BeforeEach
    void setUp() {
        Board board = new Board(TEST_WIDTH, TEST_HEIGHT);
        gameController = new GameController(board);
        for (int i = 0; i < 6; i++) {
            Player player = new Player(board, null,"Player " + i);
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

    @Test
    void wallOnCurrentSpaceBlockingMoveForward(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.getSpace().setWall(Heading.SOUTH);
        gameController.moveForward(current);

        Assertions.assertEquals(current, board.getSpace(0, 0).getPlayer(), "Player " + current.getName() + " should beSpace (0,0)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
    }

    @Test
    void wallOnCurrentSpaceNotBlockingMoveForward(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.getSpace().setWall(Heading.EAST);
        gameController.moveForward(current);

        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,0)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
    }

    @Test
    void wallOnCurrentSpaceNotBlockingMoveForward2(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.getSpace().setWall(Heading.NORTH);
        gameController.moveForward(current);

        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,0)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
    }

    @Test
    void wallOnNextSpaceBlockingMoveForward(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.board.getNeighbour(current.getSpace(),current.getHeading()).setWall(Heading.NORTH);
        gameController.moveForward(current);

        Assertions.assertEquals(current, board.getSpace(0, 0).getPlayer(), "Player " + current.getName() + " should beSpace (0,0)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
    }

    @Test
    void wallOnNextSpaceNotBlockingMoveForward(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.board.getNeighbour(current.getSpace(),current.getHeading()).setWall(Heading.WEST);
        gameController.moveForward(current);

        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
    }

    @Test
    void wallOnNextSpaceNotBlockingMoveForward2(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.board.getNeighbour(current.getSpace(),current.getHeading()).setWall(Heading.SOUTH);
        gameController.moveForward(current);

        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
    }
}