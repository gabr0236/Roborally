package dk.dtu.compute.se.pisd.roborally.model;

/**
 * @author Gabriel
 */
public class Reboot{

    public final Heading REBOOT_HEADING;
    private final boolean startField;
    public final int REBOOT_NUMBER;

    public Reboot(Heading REBOOT_HEADING, boolean startField, int number){
        this.REBOOT_NUMBER=number;
        this.REBOOT_HEADING=REBOOT_HEADING;
        this.startField=startField;
    }

    public boolean isStartField() {
        return startField;
    }
}
