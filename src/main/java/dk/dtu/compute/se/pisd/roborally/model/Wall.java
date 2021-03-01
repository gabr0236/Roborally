package dk.dtu.compute.se.pisd.roborally.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Wall {
    private List<Heading> BlockingDirection;

    public void Wall(Heading...BlockingDirection) {
        this.BlockingDirection = Collections.unmodifiableList(Arrays.asList(BlockingDirection));
    }



}
