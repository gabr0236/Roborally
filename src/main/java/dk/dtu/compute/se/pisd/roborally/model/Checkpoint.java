package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

public class Checkpoint extends ActivatableBoardElement {


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

    //TODO dårlig paksis?
    public static void setNumberOfCheckpoints(int numberOfCheckpoints) {
        Checkpoint.numberOfCheckpoints = numberOfCheckpoints;
    }

    public static int getNumberOfCheckpoints() {
        return numberOfCheckpoints;
    }


}
