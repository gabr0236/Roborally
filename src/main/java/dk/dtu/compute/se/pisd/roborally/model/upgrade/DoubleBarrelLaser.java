package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;

/**
 * A player with this upgrade deals two spamcards when shooting laser.
 * @author @Gabriel
 */
public class DoubleBarrelLaser extends Upgrade {
    @Override
    public boolean responsible(UpgradeResponsibility upgradeResponsibility) {
        return upgradeResponsibility==UpgradeResponsibility.DOUBLE_BARREL_LASER;
    }

    @Override
    public void doAction(Player player, GameController gameController) {
    }

}
