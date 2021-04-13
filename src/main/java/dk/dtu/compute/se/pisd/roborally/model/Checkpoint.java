package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

/**
 * @author Gabriel
 */
public class Checkpoint extends ActivatableBoardElement {

    private final int checkpointNumber;

    public Checkpoint(int checkpointNumber) {
        this.checkpointNumber=checkpointNumber;
    }

    @Override
    public void activateElement(Player player, GameController gameController) {
        gameController.registerCheckpoint(player, checkpointNumber);
    }

    public int getCheckpointNumber() {
        return checkpointNumber;
    }
}
