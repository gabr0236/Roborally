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
 * <p>
 */
public enum Command {

    /**
     * Enumeration constants inclusive display names. Used in CommandCards for moving a player.
     */
    FORWARD("Fwd"),
    RIGHT("Turn Right"),
    LEFT("Turn Left"),
    FAST_FORWARD("Fast Fwd"),
    MOVE_x3("Move x3"),
    U_TURN("U-Turn"),
    OPTION_LEFT_RIGHT("Left OR Right", LEFT, RIGHT),
    SPAM("Spam"),
    TROJAN("Trojan"),
    WORM("Worm"),
    VIRUS("Virus");

    final public String displayName;

    final private List<Command> options;

    /**
     * Setter for params, used for initialising the enum constants
     * @param displayName string shown on commandcard
     * @param options one or more commands in case of optional command like "Left OR Right"
     */
    Command(String displayName, Command... options) {
        this.displayName = displayName;
        this.options = List.of(options);
    }

    public boolean isInteractive() {
        return !options.isEmpty();
    }
    public List<Command> getOptions() {
        return options;
    }
}
