package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;

/**
 * Allows player to jump over pits
 * @author @Sebastian
 */
public class PitAvoider extends Upgrade {

    public PitAvoider(){
        this.upgradeResponsibility=UpgradeResponsibility.PITAVOIDER;
    }

    @Override
    public void doAction(Player player, GameController gameController) { ;
    }

}
