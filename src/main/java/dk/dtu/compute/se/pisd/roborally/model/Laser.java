package dk.dtu.compute.se.pisd.roborally.model;

// TODO: Tilføj skade
public class Laser {
    private Board board;
    private Space location;
    private Heading heading;

    public Laser(Board board, Space location, Heading heading) {
        this.board = board;
        this.location = location;
        this.heading = heading;
    }

    public void fire() {
        boolean collision = false;
        Space rayLocation = board.getNeighbour(location, heading);
        do {
            // TODO: Tjek også for væg.
            // En spiller kan stå på et felt med en væg og ved siden af et felt med en væg, hvis blokeringsretning ikke
            // ville beskytte mod dette andet felt, hvis ikke man tjekker for modsatte retning.
            if (rayLocation.getPlayer() != null) {
                collision = true;
            } else {
                rayLocation = board.getNeighbour(rayLocation, heading);
            }
        } while(!collision);

        if (rayLocation.getPlayer() != null) {
            // do something
        }
    }
}
