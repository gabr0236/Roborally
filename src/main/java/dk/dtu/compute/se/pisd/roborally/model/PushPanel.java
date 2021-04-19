package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * PushPanel. Pushes a player 1 space in the pushing direction.
 * Only activates when the step is equal to one of the Integers in the activating turns list.
 *
 * @author Gabriel
 */
public class PushPanel extends ActivatableBoardElement {
    public final Heading pushingDirection;

    private final List<Integer> activatingTurns;

    public PushPanel(Heading pushingDirection, Integer... activatingTurns) {
        this.pushingDirection = pushingDirection;
        this.activatingTurns = Arrays.asList(activatingTurns);
    }

    @Override
    public void activateElement(@NotNull Player player, @NotNull GameController gameController) {
        gameController.activatePushPanel(player, pushingDirection, activatingTurns);
    }

    public List<Integer> getActivatingTurns() {
        return activatingTurns;
    }
}
