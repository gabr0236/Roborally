package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;

/**
 * A player with this upgrade does not draw spam cards when respawning.
 * @author @Gabriel
 */
public class Firewall extends Upgrade {
    @Override
    public boolean responsible(UpgradeResponsibility upgradeResponsibility) {
        return upgradeResponsibility==UpgradeResponsibility.FIREWALL;
    }

    @Override
    public void doAction(Player player, GameController gameController) {
    }

}
