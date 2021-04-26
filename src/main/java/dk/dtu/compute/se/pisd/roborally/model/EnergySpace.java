package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

/**
 * EnergySpace. Is an ActivatableBoardElement. Used for giving players energy for
 * upgrades when ending a turn/round on this space
 *
 * @author @Sebastian
 */
public class EnergySpace extends ActivatableBoardElement {

    private boolean energyAvailable= true;

    @Override
    public void activateElement(Player player, GameController gameController) {
        gameController.registerEnergySpace(player,energyAvailable,this);
    }

    public void setEnergyAvailable(boolean energyAvailable) {
        this.energyAvailable = energyAvailable;
    }

    public boolean isEnergyAvailable() {
        return energyAvailable;
    }
}
