package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;

public class PushPanelDodger extends Upgrade {
    @Override
    public boolean responsible(UpgradeResponsibility upgradeResponsibility) {
        return upgradeResponsibility == upgradeResponsibility.PUSH_PANEL_DODGER;
    }

    @Override
    public void doAction(Player player, GameController gameController) { ;
    }
}
