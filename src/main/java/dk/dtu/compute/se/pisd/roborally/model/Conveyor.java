package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import org.jetbrains.annotations.NotNull;

/**
 * Conveyor class. Holds a heading and a Command.
 *
 * @author @Gabriel, @Sebastian
 */
public class Conveyor extends ActivatableBoardElement {
    public final Heading heading;
    public final Command command;

    public Conveyor(Heading heading, Command command) {
        this.heading = heading;
        this.command = command;
    }

    @Override
    public void activateElement(@NotNull Player player, @NotNull GameController gameController) {
        gameController.conveyorMove(player,heading,command); }
}
