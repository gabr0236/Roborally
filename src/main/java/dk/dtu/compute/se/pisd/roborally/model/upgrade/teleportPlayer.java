package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Command;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * @author @Gabriel
 */
public class teleportPlayer extends Upgrade {
    @Override
    public boolean responsible(UpgradeResponsibility upgradeResponsibility) {
        return upgradeResponsibility==UpgradeResponsibility.TELEPORT_PLAYER ? true : false;
    }

    @Override
    public void doAction(Player player, GameController gameController) {
        Space oldSpace = player.getSpace();
        Command command = player.getProgramField(oldSpace.board.getStep()).getCard().command;
    if(command!=null && oldSpace!= null && gameController.board==player.board) {
            Space forwardSpace = oldSpace.board.getNeighbour(oldSpace, player.getHeading());
            Space fastForwardSpace = forwardSpace.board.getNeighbour(forwardSpace, player.getHeading());
            Space tripleForwardSpace = fastForwardSpace.board.getNeighbour(fastForwardSpace, player.getHeading());
            Space nextSpace = null;

            switch (command) {
                case FORWARD -> nextSpace = forwardSpace;
                case FAST_FORWARD -> nextSpace = fastForwardSpace;
                case MOVE_x3 -> nextSpace = tripleForwardSpace;
            }

            if (nextSpace != null) {
                if (nextSpace.getPlayer() == null) {
                    player.setSpace(nextSpace);
                } else {
                    Player swappedPlayer = nextSpace.getPlayer();
                    player.setSpace(nextSpace);
                    swappedPlayer.setSpace(oldSpace);
                }
            } else {
                player.setSpace(null);
            }
        }
    }
}
