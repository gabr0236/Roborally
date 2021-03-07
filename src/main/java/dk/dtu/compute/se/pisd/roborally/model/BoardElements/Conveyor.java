package dk.dtu.compute.se.pisd.roborally.model.BoardElements;

import dk.dtu.compute.se.pisd.roborally.model.Command;
import dk.dtu.compute.se.pisd.roborally.model.Heading;

public class Conveyor extends BoardElement {
    public final Heading heading;
    public final Command command;

    Conveyor(Heading heading, Command command){
        super(true);
        this.heading=heading;
        this.command=command;
    }



}
