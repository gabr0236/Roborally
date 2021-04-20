package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;

/**
 * @author Gabriel
 */
public abstract class ActivatableBoardElement {

    /**
     * This abstract method specifies that every ActivatableBoardElement should override this method with
     * the childs needed method.
     * @param player the player standing on the ActivatableBoardElement
     * @param gameController for calling methods through the gamecontroller
     */
    public abstract void activateElement(Player player, GameController gameController);
}
