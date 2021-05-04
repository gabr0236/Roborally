package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;

/**
 * @author @Gabriel
 */
public abstract class Upgrade {
    private boolean activatedThisStep = false;

    protected UpgradeResponsibility upgradeResponsibility;

    public boolean responsible(UpgradeResponsibility upgradeResponsibility){
        return upgradeResponsibility==this.upgradeResponsibility;
    };

    public abstract void doAction(Player player, GameController gameController);

    public String getName(){
        return this.getClass().getSimpleName();
    }

    public boolean isActivatedThisStep() {
        return activatedThisStep;
    }

    public void setActivatedThisStep(boolean activatedThisStep) {
        this.activatedThisStep = activatedThisStep;
    }

    public UpgradeResponsibility getUpgradeResponsibility(){
        return upgradeResponsibility;
    }
}
