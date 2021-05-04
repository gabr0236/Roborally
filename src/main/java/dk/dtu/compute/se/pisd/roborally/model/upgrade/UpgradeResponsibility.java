package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import java.util.stream.Stream;

public enum UpgradeResponsibility {
    TELEPORTPLAYER,
    RAILGUN,
    PITAVOIDER,
    MODULARCHASSIS,
    PUSHPANELDODGER,
    EXTRAHANDCARD,
    RAMMINGGEAR,
    PUSHLEFTORRIGHT,
    BLUESCREENDEATH,
    TROJANNEEDLER,
    VIRUSMODULE,
    PRESSORBEAM,
    LUCKYSHIELD,
    FIREWALL,
    DOUBLEBARRELLASER,
    REARLASER,
    TRACTORBEAM;

    public static UpgradeResponsibility getRandom() {
        return values()[((int) (Math.random() * values().length-1))];
    }
}
