package dk.dtu.compute.se.pisd.roborally.model;

public class Conveyor {
    public final Heading heading;
    public final Command command;

    Conveyor(Heading heading, Command command){
        this.heading=heading;
        this.command=command;
    }
}
