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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.EAST;

/**
 * ...
 * @author Ekkart Kindler, ekki@dtu.dk
 * <p>
 * Javadoc
 * @author Gabriel
 */
public class Player extends Subject implements Comparable<Player> {

    final public static int NO_REGISTERS = 5;
    final public static int NO_CARDS = 8;

    final public Board board;

    private String name;
    private String color;

    private Space space;
    private Heading heading = EAST;

    private final CommandCardField[] program;
    private final CommandCardField[] cards;

    private boolean playerWin = false;

    private int lastCheckpointVisited = 0;

    private Space rebootSpace;

    public int antennaDistance;

    private int energyBank = 0;


    /**
     * Player konstruktør, Initialisere spiller og tilknytter board, sætter farve og sætter navn.
     * <p>
     * Herefter oprettes først et CommandCardField Array med plads til at holde en robots registers
     * Der oprettes herefter CommandCardField Array med plads til at holde en robots tilfældige
     * programmeringskort der tildeles i starten af hver programmeringsfase.
     *
     * @param board
     * @param color
     * @param name
     */
    public Player(@NotNull Board board,@NotNull String color, @NotNull String name) {
        this.board = board;
        this.name = name;
        this.color = color;

        this.space = null;

        program = new CommandCardField[NO_REGISTERS];
        for (int i = 0; i < program.length; i++) {
            program[i] = new CommandCardField(this);
        }

        cards = new CommandCardField[NO_CARDS];
        for (int i = 0; i < cards.length; i++) {
            cards[i] = new CommandCardField(this);
        }
    }

    /**
     * Returnerer navn
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Sætter navn samt kalder observerens notifyChange() til at opdatere GUI
     *
     * @param name
     */
    public void setName(String name) {
        if (name != null && !name.equals(this.name)) {
            this.name = name;
            notifyChange();
            if (space != null) {
                space.playerChanged();
            }
        }
    }

    public String getColor() {
        return color;
    }

    /**
     * Sætter farve samt kalder observerens notifyChange() til at opdatere GUI
     *
     * @param color
     */
    public void setColor(String color) {
        this.color = color;
        notifyChange();
        if (space != null) {
            space.playerChanged();
        }
    }

    public Space getSpace() {
        return space;
    }

    /**
     * Metode til at tilknytte spiller til Space
     *
     * @param space
     */
    public void setSpace(Space space) {
        Space oldSpace = this.space;
        if (space != oldSpace &&
                (space == null || space.board == this.board)) {
            this.space = space;
            if (oldSpace != null) {
                oldSpace.setPlayer(null);
            }
            if (space != null) {
                space.setPlayer(this);
            }
            notifyChange();
        }
    }

    public void setPlayerWin(boolean playerWin) {
        this.playerWin = playerWin;
    }

    public boolean isPlayerWin() {
        return playerWin;
    }

    public Heading getHeading() {
        return heading;
    }

    /**
     * Metode til at sætte spillers "retning"
     *
     * @param heading
     */
    public void setHeading(@NotNull Heading heading) {
        if (heading != this.heading) {
            this.heading = heading;
            notifyChange();
            if (space != null) {
                space.playerChanged();
            }
        }
    }

    public int getLastCheckpointVisited() {
        return lastCheckpointVisited;
    }

    public void setLastCheckpointVisited(int lastCheckpointVisited) {
        this.lastCheckpointVisited = lastCheckpointVisited;
    }

    /**
     * Henter et specifikt registreringskort, vælges ud fra paramereten i
     *
     * @param i
     * @return
     */
    public CommandCardField getProgramField(int i) {
        return program[i];
    }

    /**
     * Henter et specifikt kort spilleren har på hånden, vælges ud fra paramereten i
     * @param i
     * @return
     */

    public CommandCardField getCardField(int i) {
        return cards[i];
    }

    public Space getRebootSpace() {
        return rebootSpace;
    }

    public void setRebootSpace(Space rebootSpace) {
        this.rebootSpace = rebootSpace;
    }

    public void setAntennaDistance(int antennaDistance){this.antennaDistance = antennaDistance;}
    public int getAntennaDistance(){return antennaDistance;}

    @Override
    public int compareTo(@NotNull Player o) {
        if(o.getAntennaDistance()<antennaDistance) return 1;
        //Order is random if equal distance from antenna
        else if(o.getAntennaDistance()==antennaDistance) return ((int)(Math.round(Math.random()*1))==1) ? -1 : 1;
        else return -1;
    }

    public int getEnergyBank() { return energyBank; }

    public void addEnergy (){
        energyBank++;
    }

    public void subtractEnergy (int i){
        if (i<0) {return;}
        energyBank=energyBank-i;
    }
}
