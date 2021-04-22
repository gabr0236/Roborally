package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class TeleportPlayerTest {

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
    void teleportOverWallOnCurrentSpace(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.getUpgrades().add(new TeleportPlayer());
        board.setStep(0);
        current.getProgramField(0).setCard(new CommandCard(Command.FORWARD));
        current.getSpace().setWallList(new ArrayList<Heading>() {{ add(Heading.SOUTH); }});
        gameController.executeCommand(current,current.getHeading(),Command.FORWARD);
        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
    }

    /**
     * @author @Gabriel
     */
    @Test
    void teleportOverWallOnNextSpace(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.getUpgrades().add(new TeleportPlayer());
        board.setStep(0);
        current.getProgramField(0).setCard(new CommandCard(Command.FORWARD));
        current.board.getNeighbour(current.getSpace(),current.getHeading()).setWallList(new ArrayList<Heading>() {{ add(Heading.NORTH); }});
        gameController.executeCommand(current,current.getHeading(),Command.FORWARD);

        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
    }

    /**
     * @author @Gabriel
     */
    @Test
    void teleportOverPit(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.getUpgrades().add(new TeleportPlayer());
        board.setStep(0);
        current.getProgramField(0).setCard(new CommandCard(Command.FAST_FORWARD));
        current.board.getNeighbour(current.getSpace(),current.getHeading()).setPit(true);
        gameController.executeCommand(current,current.getHeading(),Command.FAST_FORWARD);

        Assertions.assertEquals(current, board.getSpace(0, 2).getPlayer(), "Player " + current.getName() + " should beSpace (0,2)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
    }

    /**
     * @author @Gabriel
     */
    @Test
    void teleportPlayerSwap(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.getUpgrades().add(new TeleportPlayer());
        board.setStep(0);
        current.getProgramField(0).setCard(new CommandCard(Command.FORWARD));
        current.board.getNeighbour(current.getSpace(),current.getHeading()).setPlayer(board.getPlayer(1));
        gameController.executeCommand(current,current.getHeading(),Command.FAST_FORWARD);

        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        Assertions.assertEquals(board.getPlayer(1), board.getSpace(0, 0).getPlayer(), "Player " + board.getPlayer(1).getName() + " should beSpace (0,0)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
    }

    /**
     * @author @Gabriel
     */
    @Test
    void teleportPlayerSwapMove3(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.getUpgrades().add(new TeleportPlayer());
        board.setStep(0);
        current.getProgramField(0).setCard(new CommandCard(Command.MOVE_x3));
        board.getSpace(0,3).setPlayer(board.getPlayer(1));
        gameController.executeCommand(current,current.getHeading(),Command.MOVE_x3);

        Assertions.assertEquals(current, board.getSpace(0, 3).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        Assertions.assertEquals(board.getPlayer(1), board.getSpace(0, 0).getPlayer(), "Player " + board.getPlayer(1).getName() + " should beSpace (0,0)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
    }
}