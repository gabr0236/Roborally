package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class RailGun extends Upgrade {

    public boolean responsible(UpgradeResponsibility upgradeResponsibility){
        if (upgradeResponsibility == UpgradeResponsibility.laser)
            return true;
        else
            return false;
    }

    @Override
    public void doAction(Player player, GameController gameController) {


    }
}
