package dk.dtu.compute.se.pisd.roborally.model;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Wall {
    private List<Heading> blockingDirection;

    public Wall(@NotNull Heading...blockingDirection) {
        this.blockingDirection = Collections.unmodifiableList(Arrays.asList(blockingDirection));
    }

    public List<Heading> getBlockingDirection() {
        return blockingDirection;
    }

    public void setBlockingDirection(List<Heading> blockingDirection) {
        this.blockingDirection = blockingDirection;
    }






}
