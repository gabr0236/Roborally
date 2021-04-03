package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

public class Laser {
    private Heading direction;
    private Space location;
    private Board board;

    public Laser(Heading direction, Space location, Board board) {
        this.direction = direction;
        this.location = location;
        if (location != null) {
            this.board = location.getBoard();
        }
    }

    public void fire(GameController gameController) {
        Space projectile = board.getNeighbour(location, direction);
        boolean hit = false;

        do {
            if (gameController.notWallsBlock(projectile, direction)) {
                if (projectile.getPlayer() != null) {
                    Player player = projectile.getPlayer();
                    // TODO: Add reboot.
                    // gameController.reboot(player);
                } else {
                    projectile = board.getNeighbour(projectile, direction);
                }
            } else {
               hit = !hit;
            }
        } while(!hit);
    }
}
