package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class ModularChassis extends Upgrade{

    @Override
    public boolean responsible(UpgradeResponsibility upgradeResponsibility) {
        return upgradeResponsibility == UpgradeResponsibility.MODULAR_CHASSIS;
    }

    @Override
    public void doAction(Player player, GameController gameController) {
        Space neighbour = gameController.board.getNeighbour(player.getSpace(), player.getHeading());
        gameController.stealUpgradeCard(player, neighbour.getPlayer());

    }
}
