package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Command;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * @author @Gabriel
 */
public class TeleportPlayer extends Upgrade {
    @Override
    public boolean responsible(UpgradeResponsibility upgradeResponsibility) {
        return upgradeResponsibility==UpgradeResponsibility.TELEPORT_PLAYER ? true : false;
    }

    @Override
    public void doAction(Player player, GameController gameController) {
        Space oldSpace = player.getSpace();
        if (player.getProgramField(oldSpace.board.getStep()).getCard() != null) {
            Command command = player.getProgramField(oldSpace.board.getStep()).getCard().command;
            if (command != null && oldSpace != null && gameController.board == player.board) {
                Space nextSpace = gameController.calculateDestination(player, player.getHeading(), command);

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

    @Override
    public String toString() {
        return "TeleportPlayer";
    }
}
