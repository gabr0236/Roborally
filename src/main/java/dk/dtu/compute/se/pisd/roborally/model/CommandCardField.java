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

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class CommandCardField extends Subject {

    //Gab,sab,dan
    final public Player player;

    private CommandCard card;

    private boolean visible;

    /**
     * en metode som tager paramereteren "player" og sætter denne "player" til at være "player
     * Sætter dette kort til at være "null"
     * Sætter visibiliteten af Commandcardet til at være true, dvs. synligt.
     *
     * @param player
     */
    public CommandCardField(Player player) {
        this.player = player;
        this. card = null;
        this.visible = true;
    }

    /**
     * En "getter" til CommandCardet, som går det muligt at tjekke værdien på et "CommandCard".
     *
     * @return
     */
    public CommandCard getCard() {
        return card;
    }

    /**
     * En "setter" til CommandCardet, som går det muligt at sætte værdien på et "CommandCard", til en ønsket værdi.
     *
     * @param card
     */
    public void setCard(CommandCard card) {
        if (card != this.card) {
            this.card = card;
            notifyChange();
        }
    }

    /**
     * En metode som retunerer en boolsk værdi om et "Commandocard" er syndeligt eller ej,
     * Et "Commandcard" som er i spil eller har været i spil er synligt,
     * Et "Commandcard" som ikke er i spil er ikke synligt
     *
     * @return
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * En metode som gør det muligt at ændre synligheden på guien for et "CommandCard"
     *
     * @param visible
     */
    public void setVisible(boolean visible) {
        if (visible != this.visible) {
            this.visible = visible;
            notifyChange();
        }
    }
}
