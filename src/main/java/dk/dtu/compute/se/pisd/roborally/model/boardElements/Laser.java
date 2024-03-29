package dk.dtu.compute.se.pisd.roborally.model.boardElements;

import dk.dtu.compute.se.pisd.roborally.model.Heading;

/**
 * laser that shoots players
 * @author Tobias s205358
 */
public class Laser {

    private Heading shootingDirection;

    public Laser(Heading shootingDirection) {
        this.shootingDirection = shootingDirection;
    }

    public Heading getShootingDirection() {
        return shootingDirection;
    }

}
