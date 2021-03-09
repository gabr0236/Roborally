package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import org.jetbrains.annotations.NotNull;

public class Conveyor extends ActivatableBoardElement {
    //public final?
    private final Heading heading;
    private final Command command;

    Conveyor(@NotNull Heading heading, @NotNull Command command){
        this.heading=heading;
        this.command=command;
    }

    public void activateElement(@NotNull Player player, @NotNull GameController gameController) {
        gameController.activateDirectionMove(player,command,heading);
    }

    public Heading getHeading() {
        return heading;
    }

    public Command getCommand() {
        return command;
    }





}
