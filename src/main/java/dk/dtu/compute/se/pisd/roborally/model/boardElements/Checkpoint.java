package dk.dtu.compute.se.pisd.roborally.model.boardElements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.ActivatableBoardElement;
import dk.dtu.compute.se.pisd.roborally.model.Player;

/**
 * @author @Gabriel
 */
public class Checkpoint extends ActivatableBoardElement {

    private final int checkpointNumber;

    /**
     * sets the checkpoint number
     * @param checkpointNumber current number
     */
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
