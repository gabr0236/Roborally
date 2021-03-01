package dk.dtu.compute.se.pisd.roborally.model;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Wall {
    public List<Heading> getBlockingDirection() {
        return blockingDirection;
    }

    public void setBlockingDirection(List<Heading> blockingDirection) {
        this.blockingDirection = blockingDirection;
    }

    private List<Heading> blockingDirection;

    public Wall(@NotNull Heading...blockingDirection) {
        this.blockingDirection = Collections.unmodifiableList(Arrays.asList(blockingDirection));
    }





}
