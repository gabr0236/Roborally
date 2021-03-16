package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RebootTest {


    private final int TEST_WIDTH = 12;
    private final int TEST_HEIGHT = 12;

    private List<Space> rebootSpaceList = new ArrayList<>();

    private GameController gameController;



    @BeforeEach
    void setUp() {
        Board board = new Board(TEST_WIDTH, TEST_HEIGHT);

        board.getSpace(1,1).setReboot(new Reboot(Heading.EAST,true));
        board.getSpace(0,3).setReboot(new Reboot(Heading.EAST,true));
        board.getSpace(1,4).setReboot(new Reboot(Heading.EAST,true));
        board.getSpace(1,5).setReboot(new Reboot(Heading.EAST,true));
        board.getSpace(0,6).setReboot(new Reboot(Heading.EAST,true));
        board.getSpace(1,8).setReboot(new Reboot(Heading.EAST,true));
        board.getSpace(5,4).setReboot(new Reboot(Heading.EAST,false));
        rebootSpaceList=(Arrays.asList(board.getSpace(1,1), board.getSpace(0,3), board.getSpace(1,4), board.getSpace(1,5),
                board.getSpace(0,6), board.getSpace(1,8),board.getSpace(5,4)));

        gameController = new GameController(board);
        for (int i = 0; i < 6; i++) {
            Player player = new Player(board, "Black","Player " + i);
            board.addPlayer(player);
            player.setSpace(board.getSpace(i, i));
            player.setHeading(Heading.values()[i % Heading.values().length]);
            player.setRebootSpace(board.getRebootSpaceList().get(i));

            }

        board.setCurrentPlayer(board.getPlayer(0));
    }

    @AfterEach
    void tearDown() {
        gameController = null;
    }



    @Test
    void teleportToStartBoardReboot() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        current.setSpace(null);
        //current.setHeading(Heading.NORTH);
        //gameController.directionMove(current, current.getHeading());
        //gameController.executeCommandOptionAndContinue(Command.FORWARD);
        gameController.respawnPlayers();



        Assertions.assertEquals(current, board.getSpace(1, 1).getPlayer(), "Player " + current.getName() + " should beSpace (1,1)!");
        //Assertions.assertEquals(pushedPlayer, board.getSpace(1, 2).getPlayer(), "Player " + pushedPlayer.getName() + " should beSpace (0,1)!");

    }

}
