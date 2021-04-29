package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.boardElements.PushPanel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PushPanelDodgerTest {


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
     *
     * @author Sebastian
     *
     */
    @Test
    void playerDoesNotGetPushed(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.getUpgrades().add(new PushPanelDodger());
        current.setSpace(board.getSpace(0,0));
        current.setHeading(Heading.EAST);
        board.setStep(0);
        current.getSpace().getActivatableBoardElements().add(new PushPanel(Heading.SOUTH,0));
        gameController.executeBoardElements();
        Assertions.assertEquals(current, board.getSpace(0, 0).getPlayer(), "Player " + current.getName() + " should beSpace (0,0)!");
        Assertions.assertEquals(Heading.EAST, current.getHeading(), "Player 0 should be heading EAST!");
        Assertions.assertNull(board.getSpace(0, 1).getPlayer(), "Space (0,1) should be empty!");

    }

    /**
     *
     * @author Sebastian
     *
     */
    @Test
    void OtherPushPanelsWork(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Player second = board.getPlayer(1);
        current.getUpgrades().add(new PushPanelDodger());
        current.setSpace(board.getSpace(0,0));
        second.setSpace(board.getSpace(1,0));
        current.setHeading(Heading.EAST);
        second.setHeading(Heading.EAST);
        board.setStep(0);
        current.getSpace().getActivatableBoardElements().add(new PushPanel(Heading.SOUTH,0));
        second.getSpace().getActivatableBoardElements().add(new PushPanel(Heading.SOUTH,0));
        gameController.executeBoardElements();
        Assertions.assertEquals(current, board.getSpace(0, 0).getPlayer(), "Player " + current.getName() + " should beSpace (0,0)!");
        Assertions.assertEquals(Heading.EAST, current.getHeading(), "Player 0 should be heading EAST!");
        Assertions.assertNull(board.getSpace(0, 1).getPlayer(), "Space (0,1) should be empty!");
        Assertions.assertEquals(second, board.getSpace(1, 1).getPlayer(), "Player " + current.getName() + " should beSpace (1,1)!");
        Assertions.assertEquals(Heading.EAST, second.getHeading(), "Player 1 should be heading EAST!");
        Assertions.assertNull(board.getSpace(1, 0).getPlayer(), "Space (1,0) should be empty!");

    }
}
