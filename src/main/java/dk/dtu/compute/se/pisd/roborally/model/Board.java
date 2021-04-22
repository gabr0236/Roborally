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
import dk.dtu.compute.se.pisd.roborally.model.upgrade.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static dk.dtu.compute.se.pisd.roborally.model.Phase.INITIALISATION;

/**
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Board extends Subject {

    public final int width;

    public final int height;

    public final String boardName;

    private Integer gameId;

    private String gameName;

    private final List<Space> spacesList = new ArrayList<>();

    private final Space[][] spaces;

    private List<Player> players = new ArrayList<>();

    private Player current;

    private Phase phase = INITIALISATION;

    private int step = 0;

    private boolean stepMode;

    public final List<Integer> rebootBorderXValues = new ArrayList<>();

    private List<Space> rebootSpaceList = new ArrayList<>();

    private final List<Space> laserSpaceList = new ArrayList<>();

    public final List<Upgrade> upgrades = Arrays.asList(
            new ExtraHandCard(),new ModularChassis(), new PitAvoider(),new RailGun(),
            new RammingGear(), new TeleportPlayer(), new PushPanelDodger(), new RearLaser()
            //,new PushLeftOrRight()
    );

    private int numberOfCheckpoints;



    private boolean lasersActive = true;
    /**
     * Creates a board with the size of width * height, and create spaces for the board
     * @param width
     * @param height
     * @param boardName
     */
    public Board(int width, int height, @NotNull String boardName) {
        this.boardName = boardName;
        this.width = width;
        this.height = height;
        spaces = new Space[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Space space = new Space(this, x, y);
                spaces[x][y] = space;
                spacesList.add(space);
            }
        }
        this.stepMode = false;
    }

    /**
     * Used to create a certain board, used for quick tests
     * @param width
     * @param height
     */
    public Board(int width, int height) {
        this(width, height, "defaultboard");
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        if (this.gameId == null) {
            this.gameId = gameId;
        } else {
            if (!this.gameId.equals(gameId)) {
                throw new IllegalStateException("A game with a set id may not be assigned a new id!");
            }
        }
    }

    /**
     * returns a specific space depending on the params.
     * @param x
     * @param y
     * @return
     */
    public Space getSpace(int x, int y) {
        if (x >= 0 && x < width &&
                y >= 0 && y < height) {
            return spaces[x][y];
        } else {
            return null;
        }
    }

    /**
     *
     * @return - returns the size of the players list which represents the amount of players playing
     */
    public int getPlayersNumber() {
        return players.size();
    }


    public void addPlayer(@NotNull Player player) {
        if (player.board == this && !players.contains(player)) {
            players.add(player);
            notifyChange();
        }
    }

    /**
     * returns a specific player depending on the params.
     * @param i
     * @return
     */
    public Player getPlayer(int i) {
        if (i >= 0 && i < players.size()) {
            return players.get(i);
        } else {
            return null;
        }
    }

    /**
     *
     * @return - returns the player currently moving
     */
    public Player getCurrentPlayer() {
        return current;
    }

    /**
     * Sets a player to be the player currently playing
     * @param player - used to decide which player to set as current
     */
    public void setCurrentPlayer(Player player) {
        if (player != this.current && players.contains(player)) {
            this.current = player;
            notifyChange();
        }
    }

    /**
     *
     * @return - returns the phase of the game
     */
    public Phase getPhase() {
        return phase;
    }

    /**
     *
     * @param phase - sets the phase of the game
     */
    public void setPhase(Phase phase) {
        if (phase != this.phase) {
            this.phase = phase;
            notifyChange();
        }
    }

    /**
     *
     * @return - returns a int representing the register
     */
    public int getStep() {
        return step;
    }

    /**
     *
     * @param step - sets the register to be a certain number
     */
    public void setStep(int step) {
        if (step != this.step) {
            this.step = step;
            notifyChange();
        }
    }

    /**
     *
     * @return - returns true or false depending on if players are on the move
     */
    public boolean isStepMode() {
        return stepMode;
    }

    /**
     *
     * @param stepMode - sets stepmode representing a phase in the game
     */
    public void setStepMode(boolean stepMode) {
        if (stepMode != this.stepMode) {
            this.stepMode = stepMode;
            notifyChange();
        }
    }

    /**
     * Returns the players number
     * @param player - used to identify which player to find
     * @return
     */
    public int getPlayerNumber(@NotNull Player player) {
        if (player.board == this) {
            return players.indexOf(player);
        } else {
            return -1;
        }
    }

    /**
     * Returns the neighbour of the given space of the board in the given heading.
     * The neighbour is returned only, if it can be reached from the given space
     * (no walls or obstacles in either of the involved spaces); otherwise,
     * null will be returned.
     *
     * @param space   the space for which the neighbour should be computed
     * @param heading the heading of the neighbour
     * @return the space in the given direction; null if there is no (reachable) neighbour
     */
    public Space getNeighbour(Space space, Heading heading) {
        if(space!=null) {
            int x = space.x;
            int y = space.y;
            switch (heading) {
                case SOUTH -> y = (y + 1);
                case WEST -> x = (x - 1);
                case NORTH -> y = (y - 1);
                case EAST -> x = (x + 1);
            }
            return getSpace(x, y);
        } else{
            return null;
        }
    }

    /**
     * Returns the information that is seen at the bottom of the screen seen when playing
     * makes it easy for the player to know his/hers status in the game
     * @param player - used to identify the player in question
     * @return
     */
    public String getStatusMessage(Player player) {
        return "Phase: " + getPhase().name() +
                ", Player: " + player.getName() +
                ", Step: " + getStep() +
                ", Next checkpoint: " + (player.getLastCheckpointVisited()+1) +
                ", R: " + player.getRebootSpace().getReboot().REBOOT_NUMBER +
                ", Energy cubes ⚡" + player.getEnergyBank() +
                ", Upgrade: " + (player.getUpgrades().isEmpty() ? "0" : player.getUpgrades().get(0).toString());
    }

    /**
     * sorts the list of players, depending on who is closet to the antenna by calling the sort method
     * and therefore decides the order in which players take their turns
     */
    public void sortPlayersAntennaDistance(){
        Collections.sort(players);
        notifyChange();
    }

    /**
     *
     * @return - returns the list containing the players playing
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     *
     * @return - returns the list of spaces on the board that the players are playing on
     */
    public List<Space> getSpacesList() {
        return Collections.unmodifiableList(spacesList);
    }

    /**
     *
     * @return - returns the list of all the reboot spaces where the player can respawn
     */
    public List<Space> getRebootSpaceList() { return rebootSpaceList; }

    /**
     *
     * @return - returns the gamename
     */
    public String getGameName() {
        return gameName;
    }

    /**
     *
     * @param gameName sets the gamename to the gamename of the instans selected
     */
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    /**
     *
     * @return - returns the number of checkpoints
     */
    public int getNumberOfCheckpoints() {
        return numberOfCheckpoints;
    }

    /**
     *
     * @param numberOfCheckpoints - sets the number of checkpoints to the instans selected
     */
    public void setNumberOfCheckpoints(int numberOfCheckpoints) {
        this.numberOfCheckpoints = numberOfCheckpoints;
    }

    /**
     *
     * @return - returns the spaces where a laser is located
     */
    public List<Space> getLaserSpaceList() { return laserSpaceList; }

    /**
     *
     * @return - returns the values in the rebootBorderXValues list
     */
    public List<Integer> getRebootBorderXValues() { return rebootBorderXValues; }

    public void setLasersActive(boolean lasersActive) {
        this.lasersActive = lasersActive;
    }

    public boolean isLasersActive(){
        return lasersActive;
    }
}
