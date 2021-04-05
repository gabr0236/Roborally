package dk.dtu.compute.se.pisd.roborally.model;

import javax.swing.*;

public class PushingWall {

    private Heading heading;
    private Space space;

    private PushingWall(Heading heading, Space space){
        this.heading = heading;
        this.space = space;
    }

    public void createPushingWall(Heading heading, Space space, Space neighborSpace){
        if(space.getWallList().isEmpty()) {
            PushingWall pushingWall = new PushingWall(heading, space);

        }
    }
}
