package dk.dtu.compute.se.pisd.roborally.model.boardElements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.ActivatableBoardElement;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Gear. Is an ActivatableBoardElement. When a player ends a register on a
 * space with this element they should be turned right or left.
 *
 * @author Tobias s205358
 */
public class Gear extends ActivatableBoardElement {
    private boolean clockwise;

    public Gear(@NotNull boolean clockwise) {
        this.clockwise = clockwise;
    }

    @Override
    public void activateElement(Player player, GameController gameController) {
        if (clockwise) {
            gameController.turnRight(player);
        } else {
            gameController.turnLeft(player);
        }
    }

    public boolean isClockwise() {
        return clockwise;
    }
}
