package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

class PushLeftOrRightTest {
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

    //TODO: @Gab, lav når tiden er inde
    /**
     * @author Gabriel
     */

    /*@Test
    void pushPlayer() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Player pushedPlayer = board.getPlayer(2);
        current.getUpgrades().add(new PushLeftOrRight());
        board.setStep(0);
        board.setPhase(Phase.PLAYER_INTERACTION);
        current.getProgramField(0).setCard(new CommandCard(Command.FORWARD));
        board.setLasersActive(false);

        pushedPlayer.setSpace(board.getSpace(1,1));
        current.setSpace(board.getSpace(1,0));
        current.setHeading(Heading.SOUTH);
        PushLeftOrRight pushLeftOrRight = (PushLeftOrRight) current.getUpgrades().get(0);
        pushLeftOrRight.doAction(pushedPlayer,gameController,Command.FORWARD);
        Assertions.assertEquals(current, board.getSpace(1, 1).getPlayer(), "Player " + current.getName() + " should beSpace (1,1)!");
        Assertions.assertEquals(pushedPlayer, board.getSpace(1, 2).getPlayer(), "Player " + pushedPlayer.getName() + " should beSpace (1,2)!");
    }*/

    /**
     * @author Gabriel
     */
    /*@Test
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
*/
}