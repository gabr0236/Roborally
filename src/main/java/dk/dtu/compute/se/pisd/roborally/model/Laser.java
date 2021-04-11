package dk.dtu.compute.se.pisd.roborally.model;

/**
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
