package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import org.jetbrains.annotations.NotNull;

public abstract class ActivatableBoardElement {
    //TODO andre params? dvs skal alle params defineres fra start?
    public abstract void activateElement(Player player, GameController gameController);
}
