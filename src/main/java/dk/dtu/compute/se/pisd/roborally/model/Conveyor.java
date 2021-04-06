package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import org.jetbrains.annotations.NotNull;

/**
 * @author Gabriel
 */
public class Conveyor extends ActivatableBoardElement {
    //TODO: public final?
    private final Heading heading;
    private final Command command;

    Conveyor(Heading heading, Command command) {
        this.heading = heading;
        this.command = command;
    }

    @Override
    public void activateElement(@NotNull Player player, @NotNull GameController gameController) {
        gameController.executeCommand(player,heading,command);
    }

    public Heading getHeading() {
        return heading;
    }

    public Command getCommand() {
        return command;
    }


}
