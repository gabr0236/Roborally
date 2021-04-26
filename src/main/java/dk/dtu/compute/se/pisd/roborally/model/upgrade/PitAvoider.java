package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Command;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * Allows player to jump over pits
 * @author @Sebastian
 */
public class PitAvoider extends Upgrade {
    @Override
    public boolean responsible(UpgradeResponsibility upgradeResponsibility) {
        return upgradeResponsibility == upgradeResponsibility.PIT_AVOIDER;
    }

    @Override
    public void doAction(Player player, GameController gameController) { ;
    }

    @Override
    public String toString() {
        return "PitAvoider";
    }
}
