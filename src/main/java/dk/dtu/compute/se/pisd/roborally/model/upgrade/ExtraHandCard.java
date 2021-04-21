package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;

public class ExtraHandCard extends Upgrade {
    @Override
    public boolean responsible(UpgradeResponsibility upgradeResponsibility) {
        return upgradeResponsibility==UpgradeResponsibility.EXTRA_HAND_CARD;
    }

    @Override
    public void doAction(Player player, GameController gameController) {
        player.setNumberOfCards(9);

    }
}
