package dk.dtu.compute.se.pisd.roborally.model.boardElements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.ActivatableBoardElement;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.upgrade.Upgrade;
import dk.dtu.compute.se.pisd.roborally.model.upgrade.UpgradeResponsibility;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * PushPanel. Pushes a player 1 space in the pushing direction.
 * Only activates when the step is equal to one of the Integers in the activating turns list.
 *
 * @author @Gabriel
 */
public class PushPanel extends ActivatableBoardElement {
    public final Heading pushingDirection;

    private final List<Integer> activatingTurns;

    public PushPanel(Heading pushingDirection, Integer... activatingTurns) {
        this.pushingDirection = pushingDirection;
        this.activatingTurns = Arrays.asList(activatingTurns);
    }

    /**
     * Sets off the push panel, and checks if a player the PushPanelDogdger upgrade in which case the player is not pushed
     * @param player is the player being pushed
     * @author @Gabriel @Sebastian
     */
    @Override
    public void activateElement(@NotNull Player player, @NotNull GameController gameController) {
            if(!activatingTurns.isEmpty() && player!=null && player.getSpace()!=null){
                if(activatingTurns.contains(gameController.board.getStep())){
                    for(Upgrade u : player.getUpgrades()){
                        if(u.responsible(UpgradeResponsibility.PUSH_PANEL_DODGER)){
                            return;
                        }
                    }
                    gameController.directionMove(player,pushingDirection);
                }
            }
    }

    public List<Integer> getActivatingTurns() {
        return activatingTurns;
    }
}
