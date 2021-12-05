package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Command;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

import java.util.ArrayList;
import java.util.List;

/**
 * A player with this upgrade has the ability to push players left or right.
 * @author @Gabriel
 */
public class PushLeftOrRight extends Upgrade {

    private final Command[] pushOptions = {Command.LEFT,Command.FORWARD,Command.RIGHT};
    private List<Command> situationalOptions = new ArrayList<>();

    public PushLeftOrRight(){
        this.upgradeResponsibility=UpgradeResponsibility.PUSHLEFTORRIGHT;
    }


    @Override
    public void doAction(Player player, GameController gameController) {
        String test = "Test";
    }

    public void doAction(Player playerPushed, GameController gameController, Command command){
        setActivatedThisStep(true);
        Player playerPushing = gameController.board.getCurrentPlayer();
        Space playerPushedSpace = playerPushed.getSpace();
        switch (command){
            case LEFT -> gameController.directionMove(playerPushed,playerPushing.getHeading().prev());
            case FORWARD -> gameController.executeCommand(playerPushed,playerPushing.getHeading(), playerPushing.getProgramField(gameController.board.getStep()).getCard().command);
            case RIGHT -> gameController.directionMove(playerPushed,playerPushing.getHeading().next());
        }
        if(playerPushedSpace!=playerPushed.getSpace()) {
            gameController.executeCommand(playerPushing,playerPushing.getHeading(),playerPushing.getProgramField(gameController.board.getStep()).getCard().command);
            gameController.continueProgram();
        } else {
            gameController.continueProgram();
        }
    }

    public Command[] getPushOptions() { return pushOptions; }

}
