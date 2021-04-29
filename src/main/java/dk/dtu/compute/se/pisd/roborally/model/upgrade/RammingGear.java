package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * Upgrade that deals damage to players when pushing them
 * @author @Daniel
 */

public class RammingGear extends Upgrade{
    @Override
    public boolean responsible(UpgradeResponsibility upgradeResponsibility) {
        return upgradeResponsibility == UpgradeResponsibility.RAMMING_GEAR;
    }

    @Override
    public void doAction(Player player, GameController gameController) {

    }
}
