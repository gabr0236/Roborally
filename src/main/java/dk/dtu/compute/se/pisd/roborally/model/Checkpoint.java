package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

public class Checkpoint extends ActivatableBoardElement{

    public static int numberOfCheckpoints = 1;

    private int checkpointNumber;

    Checkpoint(){
     checkpointNumber=numberOfCheckpoints;
     numberOfCheckpoints++;
    }

    @Override
    public void activateElement(Player player, GameController gameController) {
        gameController.registerCheckpoint(player, checkpointNumber);
    }


}
