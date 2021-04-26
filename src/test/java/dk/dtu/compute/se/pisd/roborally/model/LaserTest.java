package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.upgrade.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class LaserTest {
    private final int TEST_WIDTH = 12;
    private final int TEST_HEIGHT = 12;

    private GameController gameController;
    List<Command> DAMAGE_COMMANDS = Arrays.asList(Command.SPAM,Command.TROJAN,Command.WORM, Command.VIRUS);

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

        for (Command c:DAMAGE_COMMANDS) {
            if(player.getSavedDamageCards().contains(c)){
                Assertions.assertTrue(player.getSavedDamageCards().contains(c));
            }
        }
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

        for (Command c:DAMAGE_COMMANDS) {
            if(player.getSavedDamageCards().contains(c)){
                Assertions.assertTrue(player.getSavedDamageCards().contains(c));
            }
        }
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

        for (Command c:DAMAGE_COMMANDS) {
            if(player.getSavedDamageCards().contains(c)){
                Assertions.assertTrue(player.getSavedDamageCards().contains(c));
            }
            if(otherPlayer.getSavedDamageCards().contains(c)){
                Assertions.assertTrue(otherPlayer.getSavedDamageCards().contains(c));
            }
        }
    }

    /**
     * @author @Daniel
     */
    @Test
    void RailGunUpgradeThroughPlayer(){
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

        for (Command c:DAMAGE_COMMANDS) {
            if(killedPlayer.getSavedDamageCards().contains(c)){
                Assertions.assertTrue(killedPlayer.getSavedDamageCards().contains(c));
            }
            if(otherKilledPlayer.getSavedDamageCards().contains(c)){
                Assertions.assertTrue(otherKilledPlayer.getSavedDamageCards().contains(c));
            }
        }
    }

    /**
     * @author @Daniel
     */
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

        for (Command c:DAMAGE_COMMANDS) {
            if(killedPlayer.getSavedDamageCards().contains(c)){
                Assertions.assertTrue(killedPlayer.getSavedDamageCards().contains(c));
            }
        }
    }

    /**
     * @author @Daniel
     */
    @Test
    void rearLaserUpgrade(){
        RearLaser rearLaser = new RearLaser();

        Player shootingPlayer = gameController.board.getPlayer(0);
        shootingPlayer.setSpace(gameController.board.getSpace(0,0));
        shootingPlayer.setHeading(Heading.WEST);
        shootingPlayer.getUpgrades().add(rearLaser);

        Player killedPlayer = gameController.board.getPlayer(1);
        killedPlayer.setSpace(gameController.board.getSpace(1,0));
        killedPlayer.setHeading(Heading.EAST);


        gameController.fireAllLasers(gameController.board.getLaserSpaceList(), gameController.board.getPlayers());

        for (Command c:DAMAGE_COMMANDS) {
            if(killedPlayer.getSavedDamageCards().contains(c)){
                Assertions.assertTrue(killedPlayer.getSavedDamageCards().contains(c));
            }
        }
    }

    /**
     * @author @Daniel
     */
    @Test
    void rearLaserWithRailGunThroughPlayer(){
        RearLaser rearLaser = new RearLaser();
        RailGun railGun = new RailGun();

        Player shootingPlayer = gameController.board.getPlayer(0);
        shootingPlayer.setSpace(gameController.board.getSpace(1,3));
        shootingPlayer.setHeading(Heading.WEST);
        shootingPlayer.getUpgrades().add(rearLaser);
        shootingPlayer.getUpgrades().add(railGun);

        Player killedPlayer = gameController.board.getPlayer(1);
        killedPlayer.setSpace(gameController.board.getSpace(2,3));
        killedPlayer.setHeading(Heading.NORTH);

        Player otherKilledPlayer = gameController.board.getPlayer(1);
        otherKilledPlayer.setSpace(gameController.board.getSpace(3,3));
        otherKilledPlayer.setHeading(Heading.NORTH);


        gameController.fireAllLasers(gameController.board.getLaserSpaceList(), gameController.board.getPlayers());

        for (Command c:DAMAGE_COMMANDS) {
            if(killedPlayer.getSavedDamageCards().contains(c)){
                Assertions.assertTrue(killedPlayer.getSavedDamageCards().contains(c));
            }
            if(otherKilledPlayer.getSavedDamageCards().contains(c)){
                Assertions.assertTrue(otherKilledPlayer.getSavedDamageCards().contains(c));
            }
        }
    }

    /**
     * @author @Daniel
     */
    @Test
    void rearLaserWithRailGunThroughWall(){
        RearLaser rearLaser = new RearLaser();
        RailGun railGun = new RailGun();

        Player shootingPlayer = gameController.board.getPlayer(0);
        shootingPlayer.setSpace(gameController.board.getSpace(1,1));
        shootingPlayer.setHeading(Heading.WEST);
        shootingPlayer.getUpgrades().add(rearLaser);
        shootingPlayer.getUpgrades().add(railGun);
        shootingPlayer.getSpace().getWallList().add(Heading.EAST);

        Player killedPlayer = gameController.board.getPlayer(1);
        killedPlayer.setSpace(gameController.board.getSpace(2,1));
        killedPlayer.setHeading(Heading.NORTH);

        Player otherKilledPlayer = gameController.board.getPlayer(1);
        otherKilledPlayer.setSpace(gameController.board.getSpace(3,1));
        otherKilledPlayer.setHeading(Heading.NORTH);


        gameController.fireAllLasers(gameController.board.getLaserSpaceList(), gameController.board.getPlayers());

        for (Command c:DAMAGE_COMMANDS) {
            if(killedPlayer.getSavedDamageCards().contains(c)){
                Assertions.assertTrue(killedPlayer.getSavedDamageCards().contains(c));
            }
            if(otherKilledPlayer.getSavedDamageCards().contains(c)){
                Assertions.assertTrue(otherKilledPlayer.getSavedDamageCards().contains(c));
            }
        }
    }

    /**
     * @author @Daniel
     */
    @Test
    void rearLaserUpgradeThroughPlayer(){
        RearLaser rearLaser = new RearLaser();

        Player shootingPlayer = gameController.board.getPlayer(0);
        shootingPlayer.setSpace(gameController.board.getSpace(1,1));
        shootingPlayer.setHeading(Heading.WEST);
        shootingPlayer.getUpgrades().add(rearLaser);


        Player killedPlayer = gameController.board.getPlayer(1);
        killedPlayer.setSpace(gameController.board.getSpace(2,1));
        killedPlayer.setHeading(Heading.SOUTH);

        Player otherPlayer = gameController.board.getPlayer(2);
        otherPlayer.setSpace(gameController.board.getSpace(3,1));
        otherPlayer.setHeading(Heading.SOUTH);

        gameController.fireAllLasers(gameController.board.getLaserSpaceList(), gameController.board.getPlayers());

        for (Command c:DAMAGE_COMMANDS) {
            if(killedPlayer.getSavedDamageCards().contains(c)){
                Assertions.assertTrue(killedPlayer.getSavedDamageCards().contains(c));
            }
            if(otherPlayer.getSavedDamageCards().contains(c)){
                Assertions.assertTrue(otherPlayer.getSavedDamageCards().contains(c));
            }
        }

    }

    /**
     * @author @Daniel
     */
    @Test
    void rearLaserUpgradeThroughWall(){
        RearLaser rearLaser = new RearLaser();

        Player shootingPlayer = gameController.board.getPlayer(0);
        shootingPlayer.setSpace(gameController.board.getSpace(0,0));
        shootingPlayer.setHeading(Heading.WEST);
        shootingPlayer.getUpgrades().add(rearLaser);
        shootingPlayer.getSpace().getWallList().add(Heading.EAST);


        Player killedPlayer = gameController.board.getPlayer(1);
        killedPlayer.setSpace(gameController.board.getSpace(3,0));
        killedPlayer.setHeading(Heading.EAST);


        gameController.fireAllLasers(gameController.board.getLaserSpaceList(), gameController.board.getPlayers());


        for (Command c:DAMAGE_COMMANDS) {
            if(killedPlayer.getSavedDamageCards().contains(c)){
                Assertions.assertTrue(killedPlayer.getSavedDamageCards().contains(c));
            }
        };
    }


    /**
     * @author @Daniel
     */
    @Test
    void rearLaserKillForwardAndBackwards(){
        RearLaser rearLaser = new RearLaser();

        Player shootingPlayer = gameController.board.getPlayer(0);
        shootingPlayer.setSpace(gameController.board.getSpace(1,0));
        shootingPlayer.setHeading(Heading.WEST);
        shootingPlayer.getUpgrades().add(rearLaser);

        Player killedPlayer = gameController.board.getPlayer(1);
        killedPlayer.setSpace(gameController.board.getSpace(2,0));
        killedPlayer.setHeading(Heading.EAST);

        Player otherKilledPlayer = gameController.board.getPlayer(1);
        otherKilledPlayer.setSpace(gameController.board.getSpace(0,0));
        otherKilledPlayer.setHeading(Heading.SOUTH);

        gameController.fireAllLasers(gameController.board.getLaserSpaceList(), gameController.board.getPlayers());

        for (Command c:DAMAGE_COMMANDS) {
            if(killedPlayer.getSavedDamageCards().contains(c)){
                Assertions.assertTrue(killedPlayer.getSavedDamageCards().contains(c));
            }
            if(otherKilledPlayer.getSavedDamageCards().contains(c)){
                Assertions.assertTrue(otherKilledPlayer.getSavedDamageCards().contains(c));
            }
        }
    }

    /**
     * @author @Sebastian
     */
    @Test
    void laserDealWormDamage(){
        Space space = gameController.board.getSpace(0,5);
        space.setLaser(new Laser(Heading.NORTH));

        Player player = gameController.board.getPlayer(0);
        player.getSavedDamageCards().clear();
        player.getUpgrades().add(new BlueScreenDeath());

        gameController.fireLaser(space,space.getLaser().getShootingDirection());

        Assertions.assertTrue(player.getSavedDamageCards().contains(Command.WORM));
    }

    /**
     * @author @Sebastian
     */
    @Test
    void laserDealTrojanHorseDamage(){
        Space space = gameController.board.getSpace(0,5);
        space.setLaser(new Laser(Heading.NORTH));

        Player player = gameController.board.getPlayer(0);
        player.getSavedDamageCards().clear();
        player.getUpgrades().add(new TrojanNeedler());

        gameController.fireLaser(space,space.getLaser().getShootingDirection());

        Assertions.assertTrue(player.getSavedDamageCards().contains(Command.TROJAN));
    }

    /**
     * @author @Sebastian
     */
    @Test
    void laserDealTrojanAndWormDamage(){
        Space space = gameController.board.getSpace(0,5);
        space.setLaser(new Laser(Heading.NORTH));

        Player player = gameController.board.getPlayer(0);
        player.getSavedDamageCards().clear();
        player.getUpgrades().add(new TrojanNeedler());
        player.getUpgrades().add(new BlueScreenDeath());

        gameController.fireLaser(space,space.getLaser().getShootingDirection());

        Assertions.assertTrue(player.getSavedDamageCards().contains(Command.TROJAN));
        Assertions.assertTrue(player.getSavedDamageCards().contains(Command.WORM));
    }

    /**
     * @author @Daniel
     */
    @Test
    void PressorBeam(){
        Board board = gameController.board;
        PressorBeam pressorBeam = new PressorBeam();

        Player shootingPlayer = gameController.board.getPlayer(0);
        shootingPlayer.setSpace(gameController.board.getSpace(1,0));
        shootingPlayer.setHeading(Heading.EAST);
        shootingPlayer.getUpgrades().add(pressorBeam);

        Player hitPlayer = gameController.board.getPlayer(1);
        hitPlayer.setSpace(gameController.board.getSpace(2,0));
        hitPlayer.setHeading(Heading.EAST);

        gameController.fireAllLasers(gameController.board.getLaserSpaceList(), gameController.board.getPlayers());

        Assertions.assertTrue(hitPlayer.getSpace() == board.getSpace(3,0));

    }

    @Test
    void rearLaserPressorBeam(){
        Board board = gameController.board;
        PressorBeam pressorBeam = new PressorBeam();

        Player shootingPlayer = gameController.board.getPlayer(0);
        shootingPlayer.setSpace(gameController.board.getSpace(1,0));
        shootingPlayer.setHeading(Heading.WEST);
        shootingPlayer.getUpgrades().add(pressorBeam);

        Player hitPlayer = gameController.board.getPlayer(1);
        hitPlayer.setSpace(gameController.board.getSpace(2,0));
        hitPlayer.setHeading(Heading.EAST);

        gameController.fireAllLasers(gameController.board.getLaserSpaceList(), gameController.board.getPlayers());

        Assertions.assertTrue(hitPlayer.getSpace() == board.getSpace(3,0));

    }
}