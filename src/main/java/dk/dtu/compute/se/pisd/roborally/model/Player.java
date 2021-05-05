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
import dk.dtu.compute.se.pisd.roborally.model.upgrade.Upgrade;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.EAST;

/**
 * ...
 * @author Ekkart Kindler, ekki@dtu.dk
 * <p>
 */
public class Player extends Subject implements Comparable<Player> {

    final public static int NO_REGISTERS = 5;

    private int numberOfCards = 8;

    final public Board board;

    private String name;
    private String color;

    private Space space;
    private Heading heading = EAST;

    private final CommandCardField[] program;

    private final List<CommandCardField> cards = new ArrayList<>(8);

    private List<Command> damageCards = new ArrayList<>();
    private List<Command> savedDamageCards = new ArrayList<>(Arrays.asList(Command.WORM, Command.VIRUS));

    private boolean playerWin = false;

    private int lastCheckpointVisited = 0;

    private Space rebootSpace;

    public int antennaDistance;

    private int energyBank = 0;

    private List<Upgrade> upgrades = new ArrayList<>();

    private Space finalDestination;

    private int playerID;



    public Space getFinalDestination() {
        return finalDestination;
    }

    public void setFinalDestination(Space finalDestination) {
        this.finalDestination = finalDestination;
    }



    /**
     * Player constructor, Initializes player, defines the board of the player, set the color and name og the player.
     * <p>
     * Creates an CommandCardField array to hold the cards being played
     * creates CommandCardField array to hold the cards given at the start of each round.
     *  @param board is the players' board
     * @param color is the color of the player
     * @param name is the name of the player
     */
    public Player(@NotNull Board board, @NotNull String color, @NotNull String name) {
        this.board = board;
        this.name = name;
        this.color = color;

        this.space = null;

        program = new CommandCardField[NO_REGISTERS];
        for (int i = 0; i < program.length; i++) {
            program[i] = new CommandCardField(this);
        }

        for (int i = 0; i < numberOfCards; i++) {
            cards.add(new CommandCardField(this));
        }
    }

    /**
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * sets the name of a player and updates this in the GUI
     * @param name is the name given and displayed
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
     * sets the color of a player and updates this in the GUI
     * @param color is the color given and displayed
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
     * set the space of a player
     * @param space is the space the player i set to
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
     * sets the heading of a player
     * @param heading is the heading given to the player
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
     * get a specific CommandCardField that had been played
     * @param i is the CommandCardField returned
     * @return a CommandCardField specified by the param
     */
    public CommandCardField getProgramField(int i) {
        return program[i];
    }

    public Space getRebootSpace() {
        return rebootSpace;
    }

    public void setRebootSpace(Space rebootSpace) {
        this.rebootSpace=rebootSpace;
    }

    public void setAntennaDistance(int antennaDistance){this.antennaDistance = antennaDistance;}
    public int getAntennaDistance(){return antennaDistance;}

    public void addEnergy (){
        energyBank++;
    }

    public int getEnergyBank() { return energyBank; }

    public List<Upgrade> getUpgrades() {
        return upgrades;
    }

    public int getNumberOfCards() {
        return numberOfCards;
    }

    public void setExtraHandCard() {
            cards.add(new CommandCardField(this));
            numberOfCards++;
    }

    public List<CommandCardField> getCards() {
        return cards;
    }

    public List<Command> getDamageCards() {
        return damageCards;
    }

    public void setDamageCards(List<Command> damageCards) {
        this.damageCards = damageCards;
    }

    public List<Command> getSavedDamageCards() {
        return savedDamageCards;
    }

    public void setSavedDamageCards(List<Command> savedDamageCards) {
        this.savedDamageCards = savedDamageCards;
    }

    /**
     * @return String with every upgrade the player has.
     * @author @Gabriel
     */
    public String getAllUpgradeNames(){
        String s = "";
        for (Upgrade u:upgrades) {
            s=s+ u.getName() + ", ";
        }
        return s;
    }

    /**
     * compares two players in regards to their distance from the antenna
     * @param o is the player being compared to the player the method is called upon
     * @return the order direction the player should move in the list
     * @author @Gabriel
     */
    @Override
    public int compareTo(@NotNull Player o) {
        if(o.getAntennaDistance()>antennaDistance) return -1;
        else if(o.getAntennaDistance()==antennaDistance) return o.getSpace().y < space.y ? -1 : 1;
        else return 1;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

}
