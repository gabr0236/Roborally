package dk.dtu.compute.se.pisd.roborally.model.boardElements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.*;
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

    /**
     * Moves a player on a conveyor belt
     * @param player is the player being moved
     * @author @Gabriel
     */
    @Override
    public void activateElement(@NotNull Player player, @NotNull GameController gameController) {
        Space target = player.board.getNeighbour(player.getSpace(), heading);

        if(target == null && gameController.notWallsBlock(player.getSpace(),heading)){
            player.setSpace(null);
        }else {
            gameController.executeCommand(player,heading,command);
        } }

}
