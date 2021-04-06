package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import org.jetbrains.annotations.NotNull;

/**
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
