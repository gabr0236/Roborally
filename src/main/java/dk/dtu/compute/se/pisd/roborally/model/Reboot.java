package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

/**
 * @author Gabriel
 */
public class Reboot{

    public final Heading REBOOT_HEADING;
    private final boolean startField;

    public Reboot(Heading REBOOT_HEADING, boolean startField){
        this.REBOOT_HEADING=REBOOT_HEADING;
        this.startField=startField;
    }

    public boolean isStartField() {
        return startField;
    }
}
