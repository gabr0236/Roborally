package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;

/**
 * A player with this upgrade can hold an extra programmingcard in his hand.
 * @author @Gabriel
 */
public class ExtraHandCard extends Upgrade {
    @Override
    public boolean responsible(UpgradeResponsibility upgradeResponsibility) {
        return upgradeResponsibility==UpgradeResponsibility.EXTRA_HAND_CARD;
    }

    @Override
    public void doAction(Player player, GameController gameController) {
        if (player.getNumberOfCards() == 8) {
            player.setExtraHandCard();
        }
    }

    @Override
    public String toString() {
        return "ExtraHandCard";
    }
}
