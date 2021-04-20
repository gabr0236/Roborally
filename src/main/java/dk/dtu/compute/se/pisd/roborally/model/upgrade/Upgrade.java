package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;

public abstract class Upgrade {
    public abstract boolean responsible(UpgradeResponsibility upgradeResponsibility);
    public abstract void doAction(Player player, GameController gameController);
}
