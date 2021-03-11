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

    @Override
    public void activateElement(@NotNull Player player, @NotNull GameController gameController) {
        //TODO: logik i model, skal laves på en anden måde
            if(command==Command.FAST_FORWARD){
                gameController.fastForward(player,heading);
            }
            else { gameController.directionMove(player,heading); }
    }

    public Heading getHeading() {
        return heading;
    }

    public Command getCommand() {
        return command;
    }





}
