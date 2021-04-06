package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

/**
 * @author Gabriel
 */
public class Checkpoint extends ActivatableBoardElement {


    //TODO @gab anden løsning end static :P
    private static int numberOfCheckpoints = 0;

    private final int checkpointNumber;

    Checkpoint() {
        if (numberOfCheckpoints == 0) {
            numberOfCheckpoints++;
            checkpointNumber = numberOfCheckpoints;
        } else {
            numberOfCheckpoints++;
            checkpointNumber = numberOfCheckpoints;
        }
    }

    @Override
    public void activateElement(Player player, GameController gameController) {
        gameController.registerCheckpoint(player, checkpointNumber);
    }

    public static void setNumberOfCheckpoints(int numberOfCheckpoints) {
        Checkpoint.numberOfCheckpoints = numberOfCheckpoints;
    }
    public static int getNumberOfCheckpoints() {
        return numberOfCheckpoints;
    }

}
