package dk.dtu.compute.se.pisd.roborally.model;

import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Tobias, s205358@student.dtu.dk
 */
public class Laser {
    private Board board;
    private Space location;
    private Heading direction;

    public Laser(@NotNull Board board, @NotNull Space location, @NotNull Heading direction) {
        this.board = board;
        this.location = location;
        this.direction = direction;
    }

    public void fire() {
        boolean collision = false;
        Space ray = board.getNeighbour(location, direction);
        do {
            if (ray == null) {
                collision = true;
                return;
            }

            Player player = ray.getPlayer();
            if (player != null) {
                collision = true;
                return;
            }

            Wall wall = ray.getWall();
            if (wall != null) {
                if (wall.getDirection().isOppositeOf(direction) || wall.getDirection() == direction) {
                    collision = true;
                    return;
                }
            }

            ray = board.getNeighbour(location, direction);
        } while(!collision);

        // TODO: Add damage to robot if collision exists.
        if (ray != null) {
            if (ray.getPlayer() != null) {
                if (ray.getWall() != null) {
                    if (!ray.getWall().getDirections().isOppositeOf(direction)) {
                        // Do Damage
                    }
                } else {
                    // Do Damage
                }
            }
        }
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Space getLocation() {
        return location;
    }

    public void setLocation(Space location) {
        this.location = location;
    }

    public Heading getDirection() {
        return direction;
    }

    public void setDirection(Heading direction) {
        this.direction = direction;
    }
}
