package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.upgrade.RailGun;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LaserTest {
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
    void laserTestPlayerKill(){
        Space space = gameController.board.getSpace(0,5);
        space.setLaser(new Laser(Heading.NORTH));

        Player player = gameController.board.getPlayer(0);

        gameController.fireLaser(space,space.getLaser().getShootingDirection());

        Assertions.assertNull(player.getSpace());
    }

    /**
     * @author @Gabriel
     */
    @Test
    void laserTestPlayerProtectedByWall(){
        Space space = gameController.board.getSpace(0,5);
        space.setLaser(new Laser(Heading.NORTH));

        space.getWallList().add(Heading.NORTH);
        Player player = gameController.board.getPlayer(0);

        gameController.fireLaser(space,space.getLaser().getShootingDirection());

        Assertions.assertNotNull(player.getSpace());
    }

    /**
     * @author @Gabriel
     */
    @Test
    void laserTestPlayerProtectedByOtherPlayer(){
        Space space = gameController.board.getSpace(0,5);
        space.setLaser(new Laser(Heading.NORTH));

        Player otherPlayer = gameController.board.getPlayer(1);
        otherPlayer.setSpace(gameController.board.getSpace(0,1));
        Player player = gameController.board.getPlayer(0);

        gameController.fireLaser(space,space.getLaser().getShootingDirection());

        Assertions.assertNotNull(player.getSpace());
        Assertions.assertNull(otherPlayer.getSpace());
    }

    @Test
    void LaserRailGunUpgradeThroughPlayer(){
        RailGun railGun = new RailGun();

        Player shootingPlayer = gameController.board.getPlayer(0);
        shootingPlayer.setSpace(gameController.board.getSpace(0,0));
        shootingPlayer.setHeading(Heading.SOUTH);
        shootingPlayer.getUpgrades().add(railGun);

        Player killedPlayer = gameController.board.getPlayer(1);
        killedPlayer.setSpace(gameController.board.getSpace(0,1));
        killedPlayer.setHeading(Heading.EAST);

        Player otherKilledPlayer = gameController.board.getPlayer(2);
        otherKilledPlayer.setSpace(gameController.board.getSpace(0,2));
        otherKilledPlayer.setHeading(Heading.EAST);

        gameController.fireAllLasers(gameController.board.getLaserSpaceList(), gameController.board.getPlayers());

        Assertions.assertNull(killedPlayer.getSpace());
        Assertions.assertNull(otherKilledPlayer.getSpace());
    }

    @Test
    void LaserRailGunUpgradeThroughWall(){
        RailGun railGun = new RailGun();

        Player shootingPlayer = gameController.board.getPlayer(0);
        shootingPlayer.setSpace(gameController.board.getSpace(0,0));
        shootingPlayer.setHeading(Heading.SOUTH);
        shootingPlayer.getUpgrades().add(railGun);
        shootingPlayer.getSpace().getWallList().add(Heading.SOUTH);


        Player killedPlayer = gameController.board.getPlayer(1);
        killedPlayer.setSpace(gameController.board.getSpace(0,2));
        killedPlayer.setHeading(Heading.EAST);


        gameController.fireAllLasers(gameController.board.getLaserSpaceList(), gameController.board.getPlayers());

        Assertions.assertNull(killedPlayer.getSpace());
    }
}