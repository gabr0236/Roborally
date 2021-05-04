package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Command;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * A player with this upgrade can teleport directly on top of a space instead of moving step by step.
 * This allows the player to move through walls. If another player is on the designated spot, players swap spaces.
 * @author @Gabriel
 */
public class TeleportPlayer extends Upgrade {
    public TeleportPlayer(){
        this.upgradeResponsibility=UpgradeResponsibility.TELEPORTPLAYER;
    }

    @Override
    public void doAction(Player player, GameController gameController) {
        Space oldSpace = player.getSpace();
        if (player.getProgramField(oldSpace.board.getStep()).getCard() != null) {
            Command command = player.getProgramField(oldSpace.board.getStep()).getCard().command;
            if (command != null && oldSpace != null && gameController.board == player.board) {
                Space nextSpace = gameController.getDestination(player, player.getHeading(), command);

                if (nextSpace != null && !nextSpace.getPit()) {
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
