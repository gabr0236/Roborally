package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

/**
 * @author Gabriel
 */
public abstract class ActivatableBoardElement {
    public abstract void activateElement(Player player, GameController gameController);
}
