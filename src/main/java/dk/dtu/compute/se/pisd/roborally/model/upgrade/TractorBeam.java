package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;

/**
 * Allows player holding this upgrade to pull players one space when shooting with laser
 * @author @Sebastian
 */

public class TractorBeam extends Upgrade {
    @Override
    public boolean responsible(UpgradeResponsibility upgradeResponsibility) {
        return upgradeResponsibility == UpgradeResponsibility.TRACTOR_BEAM;
    }

    @Override
    public void doAction(Player player, GameController gameController) {
    }
}

