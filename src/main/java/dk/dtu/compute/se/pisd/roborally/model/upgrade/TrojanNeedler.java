package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;

/**
 * Upgrade allowing player to deal trojan cards instead of spam cards
 * @author @Daniel
 */

public class TrojanNeedler extends Upgrade{
    public TrojanNeedler(){
        this.upgradeResponsibility=UpgradeResponsibility.TROJANNEEDLER;
    }

    @Override
    public void doAction(Player player, GameController gameController) {
    }


}
