package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;


/**
 * fires the Rail gun unaffected by walls or formerly hit players
 * @author @Daniel
 */
public class RailGun extends Upgrade {

    public RailGun(){
        this.upgradeResponsibility=UpgradeResponsibility.RAILGUN;
    }

    @Override
    public void doAction(Player player, GameController gameController) {
        Space projectile = gameController.board.getNeighbour(player.getSpace(), player.getHeading());
        gameController.fireRailGun(projectile, player.getHeading());
    }
}
