package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;


/**
 * A player with this upgrade deals two spamcards when shooting laser.
 * @author @Gabriel
 */
public class DoubleBarrelLaser extends Upgrade {

    public DoubleBarrelLaser(){
        this.upgradeResponsibility=UpgradeResponsibility.DOUBLEBARRELLASER;
    }

    @Override
    public void doAction(Player player, GameController gameController) {
    }

}
