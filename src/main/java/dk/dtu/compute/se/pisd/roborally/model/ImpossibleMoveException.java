package dk.dtu.compute.se.pisd.roborally.model;

public class ImpossibleMoveException extends Exception {
    private Player player;
    private Space space;
    private Heading heading;


    /**
     * throws an exception if the move is impossible
     * @param player is the player moving
     * @param space is the space of the player
     * @param heading is the players heading
     */
    public ImpossibleMoveException(Player player, Space space, Heading heading){
        super("Moved impossible");
        this.player = player;
        this.space = space;
        this.heading = heading;
    }
}
