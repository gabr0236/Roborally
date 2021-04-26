package dk.dtu.compute.se.pisd.roborally.model.upgrade;

public enum UpgradeResponsibility {
    TELEPORT_PLAYER,
    RAIL_GUN,
    LASER,
    PIT_AVOIDER,
    MODULAR_CHASSIS,
    PUSH_PANEL_DODGER,
    EXTRA_HAND_CARD,
    RAMMING_GEAR,
    PUSH_LEFT_OR_RIGHT,
    BLUE_SCREEN_DEATH,
    TROJAN_NEEDLER,
    VIRUS_MODULE,
    PRESSOR_BEAM;

    public static int getRandom() {
        return (int) (Math.random() * values().length-1);
    }
}
