package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import org.jetbrains.annotations.NotNull;


/**
 * Upgrade that swaps this upgrade for a random upgrade from the pushed player
 * @author @Daniel
 */
public class ModularChassis extends Upgrade{

    public ModularChassis(){this.upgradeResponsibility=UpgradeResponsibility.MODULARCHASSIS;}

    @Override
    public void doAction(Player player, GameController gameController) {
    }

    /**
     * swap card when pushing this player if the pushing player has modular chassis upgrade.
     * @author @Daniel
     * @param player is the pushing player with modular chassis upgrad
     * @param pushedPlayer is the player swapping a random card for the pushing players' modular chassis upgrade
     */
    public void doAction(@NotNull Player player, @NotNull Player pushedPlayer){
        if(pushedPlayer != null && !pushedPlayer.getUpgrades().isEmpty()){
            for(Upgrade u : player.getUpgrades()){
                if(u.responsible(UpgradeResponsibility.MODULARCHASSIS) && !u.isActivatedThisStep()) {
                    player.getUpgrades().remove(u);

                    int randomUpgradeNumber = (int)Math.random()*pushedPlayer.getUpgrades().size();

                    pushedPlayer.getUpgrades().add(u);

                    player.getUpgrades().add(pushedPlayer.getUpgrades().get(randomUpgradeNumber));
                    pushedPlayer.getUpgrades().remove(randomUpgradeNumber);
                    setActivatedThisStep(true);
                }
            }
        }
    }

}
