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

    /**
     *
     * @param board
     * @param location
     * @param direction
     */
    public Laser(@NotNull Board board, @NotNull Space location, @NotNull Heading direction) {
        this.board = board;
        this.location = location;
        this.direction = direction;
    }

    /**
     *
     */
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
            Player player = ray.getPlayer();
            if (player != null) {
                Wall wall = ray.getWall();
                if (wall != null) {
                    if (!wall.getDirection().isOppositeOf(direction)) {
                        // Do Damage
                    }
                } else {
                    // Do Damage
                }
            }
        }
    }

    /**
     *
     * @return
     */
    public Board getBoard() {
        return board;
    }

    /**
     *
     * @param board
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     *
     * @return
     */
    public Space getLocation() {
        return location;
    }

    /**
     *
     * @param location
     */
    public void setLocation(Space location) {
        this.location = location;
    }

    /**
     *
     * @return
     */
    public Heading getDirection() {
        return direction;
    }

    /**
     *
     * @param direction
     */
    public void setDirection(Heading direction) {
        this.direction = direction;
    }
}
