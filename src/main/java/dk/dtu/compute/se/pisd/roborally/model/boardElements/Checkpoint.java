package dk.dtu.compute.se.pisd.roborally.model.boardElements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.ActivatableBoardElement;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import org.jetbrains.annotations.NotNull;

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

    /**
     * Registers a players checkpoint in player, and calls findWinner() if player have gathered all checkpoints
     * @param player who landed on the checkpoint
     * @author @Gabriel
     */
    @Override
    public void activateElement(Player player, GameController gameController) {
            if (player != null) {
                if (checkpointNumber == player.getLastCheckpointVisited() + 1) {
                    player.setLastCheckpointVisited(checkpointNumber);
                    gameController.findWinner(player);
                }
            }
    }

    public int getCheckpointNumber() {
        return checkpointNumber;
    }
}
