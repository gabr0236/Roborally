package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import org.jetbrains.annotations.NotNull;

public abstract class ActivatableBoardElement  {
    //TODO andre params?
    public abstract void activateElement(Player player, GameController gameController);
}
