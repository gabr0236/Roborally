package dk.dtu.compute.se.pisd.roborally.model.upgrade;

/**
 * Factory for loading upgrades from database
 * @author @Gabriel
 */
public class CreateUpgrade {

    /**
     * @param upgradeResponsibility
     * @return Upgrade corresponding to the param
     */
    public static Upgrade getUpgrade(UpgradeResponsibility upgradeResponsibility){
        switch (upgradeResponsibility) {
            case TELEPORTPLAYER -> { return new TeleportPlayer(); }
            case RAILGUN -> { return new RailGun(); }
            case PITAVOIDER -> { return new PitAvoider(); }
            case MODULARCHASSIS -> { return new ModularChassis(); }
            case PUSHPANELDODGER -> { return new PushPanelDodger(); }
            case EXTRAHANDCARD -> { return new ExtraHandCard(); }
            case RAMMINGGEAR -> { return new RammingGear(); }
            case PUSHLEFTORRIGHT -> { return new PushLeftOrRight(); }
            case BLUESCREENDEATH -> { return new BlueScreenDeath(); }
            case TROJANNEEDLER -> { return new TrojanNeedler(); }
            case VIRUSMODULE -> { return new VirusModule(); }
            case PRESSORBEAM -> { return new PressorBeam(); }
            case LUCKYSHIELD -> { return new LuckyShield(); }
            case FIREWALL -> { return new Firewall(); }
            case DOUBLEBARRELLASER -> { return new DoubleBarrelLaser(); }
            case REARLASER -> { return new RearLaser(); }
            case TRACTORBEAM -> { return new TractorBeam(); }
        }
        return null;
    }
}
