package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

/**
 * @author Gabriel
 */
public class Checkpoint extends ActivatableBoardElement {


    //TODO @gab anden løsning end static :P

    public int getCheckpointNumber() {
        return checkpointNumber;
    }

    private final int checkpointNumber;

    Checkpoint(int checkpointNumber) {
        this.checkpointNumber=checkpointNumber;
    }

    @Override
    public void activateElement(Player player, GameController gameController) {
        gameController.registerCheckpoint(player, checkpointNumber);
    }
}
