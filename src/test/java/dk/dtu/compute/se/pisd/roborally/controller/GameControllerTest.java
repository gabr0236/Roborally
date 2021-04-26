package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.model.upgrade.ModularChassis;
import dk.dtu.compute.se.pisd.roborally.model.upgrade.RailGun;
import dk.dtu.compute.se.pisd.roborally.model.upgrade.RammingGear;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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


    /**
     * @author @Gabriel
     */
    @Test
    void moveForward() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.directionMove(current, current.getHeading());

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

        gameController.fastForward(current, current.getHeading());

        Assertions.assertEquals(current, board.getSpace(0, 2).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
    }



    /**
     * @author @Gabriel
     */
    @Test
    void turnRight() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.turnRight(current);

        Assertions.assertEquals(current, board.getSpace(0, 0).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        Assertions.assertEquals(Heading.WEST, current.getHeading(), "Player 0 should be heading WEST!");
    }

    /**
     * @author @Gabriel
     */
    @Test
    void turnLeft() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.turnLeft(current);

        Assertions.assertEquals(current, board.getSpace(0, 0).getPlayer(), "Player " + current.getName() + " should beSpace (0,0)!");
        Assertions.assertEquals(Heading.EAST, current.getHeading(), "Player 0 should be heading EAST!");
    }

    /**
     * @author Sebastian
     */
    @Test
    void pushPlayer() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Player pushedPlayer = board.getPlayer(2);

        pushedPlayer.setSpace(board.getSpace(1,1));
        current.setSpace(board.getSpace(1,0));
        current.setHeading(Heading.SOUTH);
        gameController.directionMove(current, current.getHeading());

        Assertions.assertEquals(current, board.getSpace(1, 1).getPlayer(), "Player " + current.getName() + " should beSpace (1,1)!");
        Assertions.assertEquals(pushedPlayer, board.getSpace(1, 2).getPlayer(), "Player " + pushedPlayer.getName() + " should beSpace (1,1)!");
    }

    /**
     * @author Sebastian
     */
    @Test
    void pushPlayerWallBlock() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Player pushedPlayer = board.getPlayer(2);

        pushedPlayer.setSpace(board.getSpace(1,1));
        current.setSpace(board.getSpace(1,0));
        current.setHeading(Heading.SOUTH);
        pushedPlayer.getSpace().setWallList(new ArrayList<Heading>() {{ add(Heading.SOUTH); }});
        gameController.directionMove(current, current.getHeading());

        Assertions.assertEquals(current, board.getSpace(1, 0).getPlayer(), "Player " + current.getName() + " should beSpace (1,0)!");
        Assertions.assertEquals(pushedPlayer, board.getSpace(1, 1).getPlayer(), "Player " + pushedPlayer.getName() + " should beSpace (1,1)!");
    }


    /**
     * @author @Gabriel
     */
    @Test
    void push5Players() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.setSpace(board.getSpace(1,0));
        Player player1 = board.getPlayer(1);
        Player player2 = board.getPlayer(2);
        Player player3 = board.getPlayer(3);
        Player player4 = board.getPlayer(4);
        Player player5 = board.getPlayer(5);

        player1.setSpace(board.getSpace(1,1));
        player2.setSpace(board.getSpace(1,2));
        player3.setSpace(board.getSpace(1,3));
        player4.setSpace(board.getSpace(1,4));
        player5.setSpace(board.getSpace(1,5));

        current.setHeading(Heading.SOUTH);
        gameController.directionMove(current, current.getHeading());

        Assertions.assertEquals(current, board.getSpace(1, 1).getPlayer(), "Player " + current.getName() + " should beSpace (1,1)!");
        Assertions.assertEquals(player1, board.getSpace(1, 2).getPlayer(), "Player " + player1.getName() + " should beSpace (1,2)!");
        Assertions.assertEquals(player2, board.getSpace(1, 3).getPlayer(), "Player " + player2.getName() + " should beSpace (1,3)!");
        Assertions.assertEquals(player3, board.getSpace(1, 4).getPlayer(), "Player " + player3.getName() + " should beSpace (1,4)!");
        Assertions.assertEquals(player4, board.getSpace(1, 5).getPlayer(), "Player " + player4.getName() + " should beSpace (1,5)!");
        Assertions.assertEquals(player5, board.getSpace(1, 6).getPlayer(), "Player " + player5.getName() + " should beSpace (1,6)!");
        Assertions.assertNull(board.getSpace(1,7).getPlayer());
        Assertions.assertNull(board.getSpace(1,0).getPlayer());
    }

    /**
     * @author @Daniel
     */
    @Test
    void pitTest(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.setSpace(board.getSpace(1,0));
        board.getSpace(1,1).setPit(true);
        current.setHeading(Heading.SOUTH);
        gameController.directionMove(current, current.getHeading());
        Assertions.assertNull(current.getSpace());
    }

    /**
     * @author @Daniel
     */
    @Test
    void pushToPitTest(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.setSpace(board.getSpace(1,0));
        Player player1 = board.getPlayer(1);
        player1.setSpace(board.getSpace(1,1));
        board.getSpace(1,2).setPit(true);
        current.setHeading(Heading.SOUTH);
        gameController.directionMove(current, current.getHeading());
        gameController.directionMove(current, current.getHeading());
        Assertions.assertNull(current.getSpace());
        Assertions.assertNull(player1.getSpace());
    }


    /**
     * @author Sebastian
     */
    @Test
    void moveThreeStepsForward() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.tripleForward(current, Heading.SOUTH);

        Assertions.assertEquals(current, board.getSpace(0, 3).getPlayer(), "Player " + current.getName() + " should beSpace (0,3)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
    }

    /**
     * @author Sebastian
     */
    @Test
    void turnAround() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.turnAround(current);

        Assertions.assertEquals(current, board.getSpace(0, 0).getPlayer(), "Player " + current.getName() + " should beSpace (0,0)!");
        Assertions.assertEquals(Heading.NORTH, current.getHeading(), "Player 0 should be heading South!");
    }

    /**
     * @author @Gabriel
     */
    @Test
    void antennaPlayerOrderingTest(){
         Board board = gameController.board;

         board.getSpace(2,0).setAntenna(true);

         gameController.updatePlayersAntennaDistance();

         Assertions.assertEquals("Player 2",board.getPlayers().get(0).getName());
         Assertions.assertEquals("Player 1",board.getPlayers().get(1).getName());
         Assertions.assertEquals("Player 0",board.getPlayers().get(2).getName());
         Assertions.assertEquals("Player 3",board.getPlayers().get(3).getName());
         Assertions.assertEquals("Player 4",board.getPlayers().get(4).getName());
         Assertions.assertEquals("Player 5",board.getPlayers().get(5).getName());

     }

    /**
     * @author @Gabriel
     */
    @Test
    void antennaPlayerOrderingTest2(){
        Board board = gameController.board;

        board.getSpace(4,5).setAntenna(true);

        gameController.updatePlayersAntennaDistance();

        Assertions.assertEquals("Player 5",board.getPlayers().get(0).getName());
        Assertions.assertEquals("Player 4",board.getPlayers().get(1).getName());
        Assertions.assertEquals("Player 3",board.getPlayers().get(2).getName());
        Assertions.assertEquals("Player 2",board.getPlayers().get(3).getName());
        Assertions.assertEquals("Player 1",board.getPlayers().get(4).getName());
        Assertions.assertEquals("Player 0",board.getPlayers().get(5).getName());

    }

    /**
     * All equal distance, deciding order from y value
     * @author @Gabriel
     */
    @Test
    void antennaPlayerOrderingTest3(){
        Board board = gameController.board;

        board.getSpace(5,0).setAntenna(true);

        gameController.updatePlayersAntennaDistance();

        Assertions.assertEquals("Player 5",board.getPlayers().get(0).getName());
        Assertions.assertEquals("Player 4",board.getPlayers().get(1).getName());
        Assertions.assertEquals("Player 3",board.getPlayers().get(2).getName());
        Assertions.assertEquals("Player 2",board.getPlayers().get(3).getName());
        Assertions.assertEquals("Player 1",board.getPlayers().get(4).getName());
        Assertions.assertEquals("Player 0",board.getPlayers().get(5).getName());

    }
    /**
     * @author Daniel
     */
    @Test
    void modularChassisPush(){
        Board board = gameController.board;
        ModularChassis modularChassis = new ModularChassis();
        RailGun railGun = new RailGun();

        Player current = board.getCurrentPlayer();
        current.setSpace(board.getSpace(1,0));
        current.setHeading(Heading.SOUTH);
        current.getUpgrades().add(modularChassis);

        Player player1 = board.getPlayer(1);
        player1.setSpace(board.getSpace(1,1));
        player1.getUpgrades().add(railGun);

        gameController.directionMove(current, current.getHeading());

        Assertions.assertTrue(player1.getUpgrades().contains(modularChassis));
        Assertions.assertTrue(current.getUpgrades().contains(railGun));
    }
    /**
     * @author Daniel
     */
    @Test
    void modularChassisNoPushedPlayer(){
        Board board = gameController.board;
        ModularChassis modularChassis = new ModularChassis();

        Player current = board.getCurrentPlayer();
        current.setSpace(board.getSpace(1,0));
        current.setHeading(Heading.SOUTH);
        current.getUpgrades().add(modularChassis);

        gameController.directionMove(current, current.getHeading());

        Assertions.assertTrue(current.getUpgrades().contains(modularChassis));
    }

    /**
     * @author Daniel
     */
    @Test
    void modularChassisPushedPlayerNoUpgrades(){
        Board board = gameController.board;
        ModularChassis modularChassis = new ModularChassis();

        Player current = board.getCurrentPlayer();
        current.setSpace(board.getSpace(1,0));
        current.setHeading(Heading.SOUTH);
        current.getUpgrades().add(modularChassis);

        Player player1 = board.getPlayer(1);
        player1.setSpace(board.getSpace(1,1));

        gameController.directionMove(current, current.getHeading());

        Assertions.assertFalse(player1.getUpgrades().contains(modularChassis));
    }

    /**
     * @author Daniel
     */
    @Test
    void rammingGearPushTest(){
        Board board = gameController.board;
        RammingGear rammingGear = new RammingGear();

        Player current = board.getCurrentPlayer();
        current.setSpace(board.getSpace(1,0));
        current.setHeading(Heading.SOUTH);
        current.getUpgrades().add(rammingGear);

        Player player1 = board.getPlayer(1);
        player1.setSpace(board.getSpace(1,1));

        gameController.directionMove(current, current.getHeading());

        Assertions.assertTrue(player1.getSavedDamageCards().contains(Command.SPAM));
        Assertions.assertTrue(board.getSpace(1,2) == player1.getSpace());
    }


}