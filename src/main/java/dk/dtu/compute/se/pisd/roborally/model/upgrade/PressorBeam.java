package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;

public class PressorBeam extends Upgrade{

    @Override
    public boolean responsible(UpgradeResponsibility upgradeResponsibility) {
        return upgradeResponsibility == UpgradeResponsibility.PRESSOR_BEAM;
    }

    @Override
    public void doAction(Player player, GameController gameController) {
    }

    @Override
    public String toString() { return "PressorBeam";
    }

}

