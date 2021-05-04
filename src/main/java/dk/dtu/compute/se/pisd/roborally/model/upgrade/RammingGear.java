package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;

/**
 * Upgrade that deals damage to players when pushing them
 * @author @Daniel
 */

public class RammingGear extends Upgrade{

    public RammingGear(){
        this.upgradeResponsibility=UpgradeResponsibility.RAMMINGGEAR;
    }

    @Override
    public void doAction(Player player, GameController gameController) {
    }
}
