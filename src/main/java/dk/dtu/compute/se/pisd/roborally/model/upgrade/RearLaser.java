package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class RearLaser extends Upgrade{
    @Override
    public boolean responsible(UpgradeResponsibility upgradeResponsibility) {
        return upgradeResponsibility == UpgradeResponsibility.REAR_LASER;
    }

    @Override
    public void doAction(Player player, GameController gameController) {
        Space rearNeighbour = gameController.board.getNeighbour(player.getSpace(), player.getHeading().oppositeHeading());
        gameController.fireLaser(rearNeighbour, player.getHeading().oppositeHeading());

    }
}
