package dk.dtu.compute.se.pisd.roborally.model;

import org.jetbrains.annotations.NotNull;

public class Conveyor extends ActivatableBoardElement {
    //public final?
    private final Heading heading;
    private final Command command;

    Conveyor(@NotNull Heading heading, @NotNull Command command){
        this.heading=heading;
        this.command=command;
    }

    public void activateElement(@NotNull Player player) {
        if (command == Command.FAST_FORWARD) {
            directionMove(player);
            directionMove(player);
        } else {
            directionMove(player);
        }
    }

    private void directionMove(@NotNull Player player){
        Space current = player.getSpace();
        if (current != null && player.board == current.board) {
            Space target = current.board.getNeighbour(current, heading);
            if (target != null && target.getPlayer() == null) {
                if (!isCurrentSpaceWallBlockingDirection(player, heading)) {
                    if (!isHeadingNeighbourWallBlockingDirection(player, heading)) {
                        player.setSpace(target);
                    }
                }
            }
            //TODO: slå sammen med anden move metode
        }
    }

    public Heading getHeading() {
        return heading;
    }

    public Command getCommand() {
        return command;
    }





}
