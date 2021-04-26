package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;

public class Firewall extends Upgrade {
    @Override
    public boolean responsible(UpgradeResponsibility upgradeResponsibility) {
        return upgradeResponsibility==UpgradeResponsibility.FIREWALL;
    }

    @Override
    public void doAction(Player player, GameController gameController) {

    }

    @Override
    public String toString() {
        return "Firewall";
    }
}
