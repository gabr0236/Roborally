package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

public class EnergySpace extends ActivatableBoardElement{

    private boolean energyAvailable=true;

    @Override
    public void activateElement(Player player, GameController gameController) {
        gameController.registerEnergySpace(player,energyAvailable);
    }

}
