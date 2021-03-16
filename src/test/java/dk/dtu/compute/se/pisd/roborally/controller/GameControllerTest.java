package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

class GameControllerTest {

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


    @Test
    void moveForward() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.directionMove(current, current.getHeading());

        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
    }


    @Test
    void fastForward() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.fastForward(current, current.getHeading());

        Assertions.assertEquals(current, board.getSpace(0, 2).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
    }



    @Test
    void turnRight() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.turnRight(current);

        Assertions.assertEquals(current, board.getSpace(0, 0).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        Assertions.assertEquals(Heading.WEST, current.getHeading(), "Player 0 should be heading WEST!");
    }

    @Test
    void turnLeft() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.turnLeft(current);

        Assertions.assertEquals(current, board.getSpace(0, 0).getPlayer(), "Player " + current.getName() + " should beSpace (0,0)!");
        Assertions.assertEquals(Heading.EAST, current.getHeading(), "Player 0 should be heading EAST!");
    }

    @Test
    void multiSpawn(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Player pushedPlayer1 = board.getPlayer(1);
        Player pushedPlayer2 = board.getPlayer(2);

        current.setSpace(board.getSpace(3,2));
        current.setHeading(Heading.NORTH);
        pushedPlayer1.setSpace(board.getSpace(3,0));
        pushedPlayer2.setSpace(board.getSpace(3,1));

        gameController.fastForward(current,current.getHeading());

        Assertions.assertEquals(current, board.getSpace(3, 0).getPlayer(), "Player " + current.getName() + " should beSpace (3,0)!");
        Assertions.assertEquals(pushedPlayer1, board.getSpace(6, 4).getPlayer(), "Player " + current.getName() + " should beSpace (6,4)!");
        Assertions.assertEquals(pushedPlayer2, board.getSpace(5, 4).getPlayer(), "Player " + current.getName() + " should beSpace (5,4)!");
    }


    //TODO: Test om skubbefunktion virker
    /*@Test
    void pushPlayer() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Player pushedPlayer = board.getPlayer(2);

       pushedPlayer.setSpace(board.getSpace(1,1));
        current.setSpace(board.getSpace(1,0));
        current.setHeading(Heading.SOUTH);
        try {
            gameController.moveToSpace(current, board.getSpace(1, 1), current.getHeading());
        } catch (ImpossibleMoveException e){}

        Assertions.assertEquals(current, board.getSpace(1, 1).getPlayer(), "Player " + current.getName() + " should beSpace (1,1)!");
        //Assertions.assertEquals(pushedPlayer, board.getSpace(1, 2).getPlayer(), "Player " + pushedPlayer.getName() + " should beSpace (0,1)!");

    }
*/
}