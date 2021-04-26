package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;

public class VirusModule extends Upgrade{
    @Override
    public boolean responsible(UpgradeResponsibility upgradeResponsibility) {
        return upgradeResponsibility == UpgradeResponsibility.VIRUS_MODULE;
    }

    @Override
    public void doAction(Player player, GameController gameController) {

    }

    @Override
    public String toString() {
        return "VirusModule";
    }
}
