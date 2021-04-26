package dk.dtu.compute.se.pisd.roborally.model.upgrade;

public enum UpgradeResponsibility {
    TELEPORT_PLAYER,
    RAIL_GUN,
    REAR_LASER,
    PIT_AVOIDER,
    MODULAR_CHASSIS,
    PUSH_PANEL_DODGER,
    EXTRA_HAND_CARD,
    RAMMING_GEAR,
    PUSH_LEFT_OR_RIGHT,
    BLUE_SCREEN_DEATH,
    TROJAN_NEEDLER,
    VIRUS_MODULE,
    PRESSOR_BEAM,
    LUCKY_SHIELD,
    FIREWALL,
    DOUBLE_BARREL_LASER;

    public static int getRandom() {
        return (int) (Math.random() * values().length-1);
    }
}
