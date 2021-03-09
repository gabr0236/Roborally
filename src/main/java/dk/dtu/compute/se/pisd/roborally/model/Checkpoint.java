package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

public class Checkpoint extends ActivatableBoardElement{

    public static int numberOfCheckpoints = 0;

    private int checkpointNumber;

    Checkpoint(){
        if(numberOfCheckpoints==0){
            numberOfCheckpoints++;
            checkpointNumber=numberOfCheckpoints;
        } else {
            numberOfCheckpoints++;
            checkpointNumber=numberOfCheckpoints;
        }
    }

    @Override
    public void activateElement(Player player, GameController gameController) {
        gameController.registerCheckpoint(player, checkpointNumber);
    }


}
