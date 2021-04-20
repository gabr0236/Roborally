package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.model.Reboot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RebootTest {


    private final int TEST_WIDTH = 15;
    private final int TEST_HEIGHT = 15;

    private List<Space> rebootSpaceListTest = new ArrayList<>();

    private GameController gameController;



    @BeforeEach
    void setUp() {
        Board board = new Board(TEST_WIDTH, TEST_HEIGHT);

        board.getSpace(1,1).setReboot(new Reboot(Heading.EAST,true,1));
        board.getSpace(0,3).setReboot(new Reboot(Heading.EAST,true,2));
        board.getSpace(1,4).setReboot(new Reboot(Heading.EAST,true,3));
        board.getSpace(1,5).setReboot(new Reboot(Heading.EAST,true,4));
        board.getSpace(0,6).setReboot(new Reboot(Heading.EAST,true,5));
        board.getSpace(1,8).setReboot(new Reboot(Heading.EAST,true,6));
        board.getSpace(5,4).setReboot(new Reboot(Heading.EAST,false,7));
        board.getSpace(8,4).setReboot(new Reboot(Heading.EAST,false,8));

        board.getRebootSpaceList().addAll(Arrays.asList(board.getSpace(1,1), board.getSpace(0,3), board.getSpace(1,4), board.getSpace(1,5),
                board.getSpace(0,6), board.getSpace(1,8),board.getSpace(5,4)));

        gameController = new GameController(board);
        for (int i = 0; i < 6; i++) {
            Player player = new Player(board, "Black","Player " + i+1);
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
        Player pushedPlayer = board.getCurrentPlayer();
        Player current = board.getPlayer(1);
        board.getSpace(4,4).setAntenna(true);

        pushedPlayer.setSpace(board.getSpace(0,0));
        current.setSpace(board.getSpace(0,1));
        current.setHeading(Heading.NORTH);
        gameController.directionMove(current, current.getHeading());
        board.setPhase(Phase.ACTIVATION);
        gameController.executePrograms();

        Assertions.assertEquals(pushedPlayer, board.getSpace(1, 1).getPlayer(), "Player " + current.getName() + " should beSpace (1,1)!");

    }

    /**
     * @author @Gabriel
     */
    @Test
    void updateReboot() {
        Board board = gameController.board;
        board.rebootBorderXValues.add(2);
        board.rebootBorderXValues.add(5);

        Player player = board.getCurrentPlayer();
        board.getSpace(4,0).setPlayer(player);
        gameController.updateAllReboot();


        Assertions.assertEquals(player.getRebootSpace(),board.getSpace(5,4));
    }

    /**
     * @author @Gabriel
     */
    @Test
    void dontUpdateRebootWhenGoingBackToEarlierBoard() {
        Board board = gameController.board;
        board.rebootBorderXValues.add(2);

        Player player = board.getCurrentPlayer();
        board.getSpace(4,0).setPlayer(player);
        gameController.updateAllReboot();

        board.getSpace(0,0).setPlayer(player);
        updateReboot();

        Assertions.assertEquals(player.getRebootSpace(),board.getSpace(5,4));
    }

    /**
     * @author @Gabriel
     */
    @Test
    void updateRebootMultipleBorders() {
        Board board = gameController.board;
        board.rebootBorderXValues.add(2);
        board.rebootBorderXValues.add(5);
        board.getRebootSpaceList().add(board.getSpace(8,4));
        Player player = board.getCurrentPlayer();
        board.getSpace(4,0).setPlayer(player);
        gameController.updateAllReboot();
        board.getSpace(9,0).setPlayer(player);
        gameController.updateAllReboot();

        Assertions.assertEquals(player.getRebootSpace(),board.getSpace(8,4));
    }

    /**
     * @author @Gabriel
     */
    @Test
    void updateRebootMultipleBordersDontUpdateWhenGoingBack() {
        Board board = gameController.board;
        board.rebootBorderXValues.add(2);
        board.rebootBorderXValues.add(5);
        board.getRebootSpaceList().add(board.getSpace(8,4));
        Player player = board.getCurrentPlayer();
        board.getSpace(4,0).setPlayer(player);
        gameController.updateAllReboot();
        board.getSpace(9,0).setPlayer(player);
        gameController.updateAllReboot();

        board.getSpace(4,0).setPlayer(player);
        gameController.updateAllReboot();

        Assertions.assertEquals(player.getRebootSpace(),board.getSpace(8,4));
    }


}
