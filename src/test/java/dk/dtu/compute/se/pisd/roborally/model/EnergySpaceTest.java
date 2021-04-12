package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EnergySpaceTest {
    private final int TEST_WIDTH = 12;
    private final int TEST_HEIGHT = 12;

    private GameController gameController;


    @BeforeEach
    void setUp () {
        Board board = new Board(TEST_WIDTH, TEST_HEIGHT);
        gameController = new GameController(board);
        for (int i = 0; i < 6; i++) {
            Player player = new Player(board, "Black", "Player " + i);
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
    void landOnEnergySpace(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        board.getSpace(0,1).getActivatableBoardElements().add(new EnergySpace());
        gameController.directionMove(current, current.getHeading());
        gameController.executeBoardElements();

        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertEquals(1,current.getEnergyBank(),"Player 0 should have 1 energyCube in his energyBank");
    }

    @Test
    void landOnEnergySpaceEnergycubeNotAvailable(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Player second = board.getPlayer(2);
        current.setSpace(board.getSpace(0,1));
        second.setSpace(board.getSpace(0,0));
        second.setHeading(Heading.SOUTH);
        board.getSpace(0,2).getActivatableBoardElements().add(new EnergySpace());
        gameController.directionMove(current, current.getHeading());
        gameController.executeBoardElements();
        gameController.directionMove(current,current.getHeading());
        gameController.fastForward(second,second.getHeading());
        gameController.executeBoardElements();

        Assertions.assertEquals(current, board.getSpace(0, 3).getPlayer(), "Player " + current.getName() + " should beSpace (0,3)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertEquals(1,current.getEnergyBank(),"Player 0 should have 1 energyCube in his energyBank");
        Assertions.assertEquals(0,second.getEnergyBank(),"Player 1 should have 0 energyCube in his energyBank");

    }

    @Test
    void landOnEnergySpaceWithOneEnergy(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.addEnergy();
        board.getSpace(0,1).getActivatableBoardElements().add(new EnergySpace());
        gameController.directionMove(current, current.getHeading());
        gameController.executeBoardElements();

        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertEquals(2,current.getEnergyBank(),"Player 0 should have 2 energyCubes in his energyBank");
    }

}
