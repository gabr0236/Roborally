package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import org.jetbrains.annotations.NotNull;

/**
 * @author Gabriel, Sebastian
 */
public class Conveyor extends ActivatableBoardElement {
    public final Heading heading;
    public final Command command;

    Conveyor(Heading heading, Command command) {
        this.heading = heading;
        this.command = command;
    }

    @Override
    public void activateElement(@NotNull Player player, @NotNull GameController gameController) {
        gameController.executeCommand(player,heading,command);
        //TODO: @Gab evt conveyor move metode der checker for getneighbour == null før den kalder direction move
        Space target = player.board.getNeighbour(player.getSpace(), heading);
        if(target == null){
            player.setSpace(null);
        }
    }
}
