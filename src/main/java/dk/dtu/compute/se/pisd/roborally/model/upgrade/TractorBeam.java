package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;

/**
 * @author Sebastian
 */

public class TractorBeam extends Upgrade {
    @Override
    public boolean responsible(UpgradeResponsibility upgradeResponsibility) {
        return upgradeResponsibility == UpgradeResponsibility.TRACTOR_BEAM;
    }

    @Override
    public void doAction(Player player, GameController gameController) {
    }

    @Override
    public String toString() {
        return null;
    }

}

