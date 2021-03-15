package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

public class Reboot{

    private Player player;
    public final Heading REBOOT_HEADING;


    private boolean startField;

    Reboot(Heading REBOOT_HEADING, boolean startField){
        this.REBOOT_HEADING=REBOOT_HEADING;
        this.startField=startField;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isStartField() {
        return startField;
    }
}
