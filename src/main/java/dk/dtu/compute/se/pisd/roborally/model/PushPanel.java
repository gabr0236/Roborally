package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import org.jetbrains.annotations.NotNull;

/**
 * @author Gabriel
 */
public class PushPanel extends ActivatableBoardElement {
    public final Heading heading;
    private Boolean[] activatingTurns = new Boolean[4];

    PushPanel(Heading heading, Boolean...activatingTurns) {
        this.heading = heading;
        this.activatingTurns = activatingTurns;
    }

    @Override
    public void activateElement(@NotNull Player player, @NotNull GameController gameController) {
        gameController.activatePushPanel(player, heading, activatingTurns);
    }
}
