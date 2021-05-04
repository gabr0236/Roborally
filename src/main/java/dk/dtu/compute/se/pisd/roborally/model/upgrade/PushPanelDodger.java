package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;

/**
 * Allows player to not get affected by pushpanels
 * @author @Sebastian
 */
public class PushPanelDodger extends Upgrade {

    public PushPanelDodger(){
        this.upgradeResponsibility=UpgradeResponsibility.PUSHPANELDODGER;
    }

    @Override
    public void doAction(Player player, GameController gameController) { ;
    }
}
