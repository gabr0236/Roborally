package dk.dtu.compute.se.pisd.roborally.model.boardElements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.ActivatableBoardElement;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import org.jetbrains.annotations.NotNull;

/**
 * EnergySpace. Is an ActivatableBoardElement. Used for giving players energy for
 * upgrades when ending a turn/round on this space
 *
 * @author @Sebastian
 */
public class EnergySpace extends ActivatableBoardElement {

    private boolean energyAvailable= true;

    /**
     * Gives players energy
     * @author @Sebastian
     * @param player is the player being given energy
     */
    @Override
    public void activateElement(Player player, GameController gameController) {
            if (player != null) {
                if(energyAvailable && gameController.board.getStep() == 4){
                    gameController.givePlayerRandomUpgrade(player, this);
                    gameController.givePlayerRandomUpgrade(player,this);
                }
                else if (energyAvailable || gameController.board.getStep() == 4) {
                    gameController.givePlayerRandomUpgrade(player,this);
                }
            }
    }

    public void setEnergyAvailable(boolean energyAvailable) {
        this.energyAvailable = energyAvailable;
    }

    public boolean isEnergyAvailable() {
        return energyAvailable;
    }
}
