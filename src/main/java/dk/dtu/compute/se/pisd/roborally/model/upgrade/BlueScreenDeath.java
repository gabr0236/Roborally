package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;

/**
 * Upgrade allowing player to deal worm cards instead of spam cards
 * @author @Daniel
 */

public class BlueScreenDeath extends Upgrade{
    @Override
    public boolean responsible(UpgradeResponsibility upgradeResponsibility) {
        return upgradeResponsibility == UpgradeResponsibility.BLUE_SCREEN_DEATH;
    }

    @Override
    public void doAction(Player player, GameController gameController) {

    }

    @Override
    public String toString() {
        return "BlueScreenDeath";
    }
}
