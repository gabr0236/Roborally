package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;

/**
 * @author @Gabriel
 */
public abstract class Upgrade {
    public abstract boolean responsible(UpgradeResponsibility upgradeResponsibility);
    public abstract void doAction(Player player, GameController gameController);
    public String toString(){
        return this.getClass().getSimpleName();
    }
    public String getName(){
        return this.getClass().getSimpleName();
    }
}
