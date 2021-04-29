package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;

/**
 * Upgrade allowing player to deal virus cards instead of spam cards
 * @author @Daniel
 */

public class VirusModule extends Upgrade{
    @Override
    public boolean responsible(UpgradeResponsibility upgradeResponsibility) {
        return upgradeResponsibility == UpgradeResponsibility.VIRUS_MODULE;
    }

    @Override
    public void doAction(Player player, GameController gameController) {

    }
}
