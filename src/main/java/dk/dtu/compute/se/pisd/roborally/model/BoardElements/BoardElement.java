package dk.dtu.compute.se.pisd.roborally.model.BoardElements;

public abstract class BoardElement {
    public final boolean activatable;

    BoardElement(boolean activatable){
        this.activatable=activatable;
    }
}
