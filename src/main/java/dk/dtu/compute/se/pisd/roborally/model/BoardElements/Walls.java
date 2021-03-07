package dk.dtu.compute.se.pisd.roborally.model.BoardElements;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Walls extends BoardElement {

    private final List<Heading> blockingDirection;

    public Walls(@NotNull Heading... blockingDirection) {
        super(false);
        this.blockingDirection = Collections.unmodifiableList(Arrays.asList(blockingDirection));
    }

    public List<Heading> getBlockingDirection() {
        return blockingDirection;
    }






}
