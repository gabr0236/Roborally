package dk.dtu.compute.se.pisd.roborally.model;

public class Conveyor {
    private final Heading heading;

    public Heading getHeading() {
        return heading;
    }

    public Command getCommand() {
        return command;
    }

    private final Command command;

    Conveyor(Heading heading, Command command){
        this.heading=heading;
        this.command=command;
    }



}
