/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public enum Command {


    // This is a very simplistic way of realizing different commands.
    /**
     * TODO: er det her rigtig javaD stil?
     *
     * Enumeration konstanter ink. display names pr. konstant. Bruges i forbindelse med CommandCards
     * til at bevæge spillere
     */
    FORWARD("Fwd"),
    RIGHT("Turn Right"),
    LEFT("Turn Left"),
    FAST_FORWARD("Fast Fwd"),
    OPTION_LEFT_RIGHT("Left OR Right", LEFT, RIGHT);

    final public String displayName;

    final private List<Command> options;

    /**
     * Sætter for params nedenfor, bruges til at initialisere enumerations konstanterne
     * @param displayName
     * @param options
     */
    Command(String displayName, Command... options) {
        this.displayName = displayName;
        this.options = Collections.unmodifiableList(Arrays.asList(options));
    }

    /**
     * Returnere boolsk værdi der siger om et CommandCard er "Interactive" altså om spilleren bliver
     * givet en mulighed for at vælge imellem en eller flere handlinger
     * @return
     */
    public boolean isInteractive() {
        return !options.isEmpty();
    }

    /**
     * Returnerer handlinger for en given enumerations konstant
     * @return
     */
    public List<Command> getOptions() {
        return options;
    }

}
