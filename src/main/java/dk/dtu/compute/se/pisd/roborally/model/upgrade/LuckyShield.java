package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;

/**
 * A player with this upgrade has a 50% chance of not getting hit by a laser.
 * @author @Gabriel
 */
public class LuckyShield extends Upgrade {
    boolean lucky = false;

    public LuckyShield(){this.upgradeResponsibility=UpgradeResponsibility.LUCKYSHIELD;}
    @Override
    public boolean responsible(UpgradeResponsibility upgradeResponsibility) {
        if(upgradeResponsibility!=this.upgradeResponsibility) return false;
        else return (int)(Math.random()*100)<=50 ? true : false;
    }

    @Override
    public void doAction(Player player, GameController gameController) { }
    
}
