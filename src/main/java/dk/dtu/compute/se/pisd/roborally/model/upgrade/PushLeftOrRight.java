package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Command;
import dk.dtu.compute.se.pisd.roborally.model.Player;

public class PushLeftOrRight extends Upgrade {

    private final Command[] pushOptions = {Command.LEFT,Command.FORWARD,Command.RIGHT};

    @Override
    public boolean responsible(UpgradeResponsibility upgradeResponsibility) {
        return upgradeResponsibility == UpgradeResponsibility.PUSH_LEFT_OR_RIGHT;
    }

    //TODO @Gab spørg ekki om det her er ok
    @Override
    public void doAction(Player player, GameController gameController) {
    }

    public void doAction(Player playerPushed, GameController gameController, Command command){
        Player playerPushing = gameController.board.getCurrentPlayer();
        switch (command){
            case LEFT -> gameController.directionMove(playerPushed,playerPushed.getHeading().prev());
            case FORWARD -> gameController.directionMove(playerPushed,playerPushing.getHeading());
            case RIGHT -> gameController.directionMove(playerPushed,playerPushed.getHeading().next());
        }
        //TODO: @Gab det her er ikke en færdig løsning
        gameController.executeCommandOptionAndContinue(playerPushing.getProgramField(gameController.board.getStep()).getCard().command);
    }

    public Command[] getPushOptions() { return pushOptions; }

    @Override
    public String toString() {
        return "PushLeftOrRight";
    }
}
