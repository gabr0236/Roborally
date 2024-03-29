package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * Fires Rear laser with or without rail gun depending on whether the player has the rail gun upgrade
 * @author @Daniel
 */

public class RearLaser extends Upgrade{
    public RearLaser(){
        this.upgradeResponsibility=UpgradeResponsibility.REARLASER;
    }

    @Override
    public void doAction(Player player, GameController gameController) {
        boolean hasRailGun = false;
        for(Upgrade u : player.getUpgrades()){
            if(u.responsible(UpgradeResponsibility.RAILGUN))
                hasRailGun = true;
        }

        Space rearNeighbour = gameController.board.getNeighbour(player.getSpace(), player.getHeading().oppositeHeading());
        if(hasRailGun)
            gameController.fireRailGun(rearNeighbour, player.getHeading().oppositeHeading());
        else
            if(gameController.notWallsBlock(player.getSpace(), player.getHeading().oppositeHeading())) {
                gameController.fireLaser(rearNeighbour, player.getHeading().oppositeHeading());
            }
    }
}
