package dk.dtu.compute.se.pisd.roborally.model;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* TODO: Opdater vægge.
    Hvis muligt, bør man nok gøre som Ekkart foreslog, således der er flere vægge på et felt med enkelte retninger.
    Dette vil gøre det nemmere at arbejde med lasere mv.
    Herudover vil en metode som giver den modsatte retning også være nyttig.
    F.eks. hvis man har en væg der vender nord, skal metoden returnere syd, da væggen jo blokere i begge retninger,
    dog ikke nødvendigvis for den der står på feltet hvor væggen er.
    Hvis ikke man kan tjekke dette, kan man ikke tjekke om der en væg der dækker for feltet ved siden af.
 */
public class Walls {

    private final List<Heading> blockingDirection;

    Walls(@NotNull Heading... blockingDirection) {
        this.blockingDirection = Collections.unmodifiableList(Arrays.asList(blockingDirection));
    }

    public List<Heading> getBlockingDirection() {
        return blockingDirection;
    }
}
