package dk.dtu.compute.se.pisd.roborally.model;

import org.jetbrains.annotations.NotNull;

public class Conveyor {
    //public final?
    private final Heading heading;

    private final Command command;

    public Heading getHeading() {
        return heading;
    }

    public Command getCommand() {
        return command;
    }

    Conveyor(@NotNull Heading heading, @NotNull Command command){
        this.heading=heading;
        this.command=command;
    }



}
