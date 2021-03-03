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

import static dk.dtu.compute.se.pisd.roborally.model.Phase.INITIALISATION;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Board extends Subject {

    public final int width;

    public final int height;

    public final String boardName;

    private Integer gameId;

    private final Space[][] spaces;

    private final List<Player> players = new ArrayList<>();

    private Player current;

    private Phase phase = INITIALISATION;

    private int step = 0;

    private boolean stepMode;

    /**
     * This is the Board constructor, which uses the following parameters to create a board of specified size and name.
     *
     * @param width - the width of the board is the number of spaces on the x-axis and thus of type integer.
     * @param height - the height of the board is the number of spaces on the y-axis and thus of type integer.
     * @param boardName - the name of the board and thus of type string.
     */
    public Board(int width, int height, @NotNull String boardName) {
        this.boardName = boardName;
        this.width = width;
        this.height = height;
        spaces = new Space[width][height];
        for (int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                Space space = new Space(this, x, y);
                spaces[x][y] = space;
            }
        }
        this.stepMode = false;
    }

    /**
     * This is the default Board constructor, which uses the following parameters to create a board of specified size.
     * If name is unspecified this constructor will be used and the board will be named "defaultboard".
     *
     * @param width - the width of the board is the number of spaces on the x-axis and thus of type integer.
     * @param height - the height of the board is the number of spaces on the y-axis and thus of type integer.
     */
    public Board(int width, int height) {
        this(width, height, "defaultboard");
    }

    /**
     * This method returns the Id of the game.
     *
     * @return - the integer id is returned.
     */
    public Integer getGameId() {
        return gameId;
    }

    /**
     * This method sets the name of the id with the following parameter.
     * If the game already has the parsed id, the id will not be changed.
     *
     * @param gameId - the id the game should have of type integer.
     */
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
     * This method returns the space object with the coordinates specified by the following parameters.
     *
     * @param x - the x-coordinate of the space of type integer.
     * @param y - the y-coordinate of the space of type integer.
     * @return - the space object is returned.
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
     * This methods returns the number of players currently playing.
     *
     * @return - the integer number of players.
     */
    public int getPlayersNumber() {
        return players.size();
    }

    /**
     * This methods adds an player.
     *
     * @param player - the player object that needs to be added.
     */
    public void addPlayer(@NotNull Player player) {
        if (player.board == this && !players.contains(player)) {
            players.add(player);
            notifyChange();
        }
    }

    /**
     * This methods returns a player object with number specified by the following parameter.
     * If the player does not exist, the method returns null.
     *
     * @param i - the player number of type integer.
     * @return - the player object.
     */
    public Player getPlayer(int i) {
        if (i >= 0 && i < players.size()) {
            return players.get(i);
        } else {
            return null;
        }
    }

    /**
     * This methods returns the current player.
     *
     * @return - the player object.
     */
    public Player getCurrentPlayer() {
        return current;
    }

    /**
     * This method sets the current player object specified by the following parameter.
     * If the specified player is already the current player, no change is made.
     *
     * @param player - the player object.
     */
    public void setCurrentPlayer(Player player) {
        if (player != this.current && players.contains(player)) {
            this.current = player;
            notifyChange();
        }
    }

    /**
     * This methods returns the phase of the game.
     *
     * @return - the phase enum.
     */
    public Phase getPhase() {
        return phase;
    }

    /**
     * This methods sets the phase enum specified by the following parameter.
     * If the specified phase is already the phase, no change is made.
     *
     * @param phase - the phase enum.
     */
    public void setPhase(Phase phase) {
        if (phase != this.phase) {
            this.phase = phase;
            notifyChange();
        }
    }

    /**
     * This method gets the number of steps.
     *
     * @return - the integer steps.
     */
    public int getStep() {
        return step;
    }

    /**
     * This method sets the step integer specified by the following paramater.
     * If specified step is already the step, no change is made.
     *
     * @return - the integer steps.
     */
    public void setStep(int step) {
        if (step != this.step) {
            this.step = step;
            notifyChange();
        }
    }

    /**
     * This method returns the step mode.
     *
     * @return - true if step mode and false if not step mode.
     */
    public boolean isStepMode() {
        return stepMode;
    }

    /**
     * This methods sets the step mode.
     * If specified step mode is already the step mode, no change is made.
     *
     * @param stepMode - the boolean step mode.
     */
    public void setStepMode(boolean stepMode) {
        if (stepMode != this.stepMode) {
            this.stepMode = stepMode;
            notifyChange();
        }
    }

    /**
     * This methods returns the integer number of a given player specified by the following parameter.
     *
     * @param player - the player object.
     * @return - the integer number of the player.
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
     * @param space the space for which the neighbour should be computed
     * @param heading the heading of the neighbour
     * @return the space in the given direction; null if there is no (reachable) neighbour
     */
    public Space getNeighbour(@NotNull Space space, @NotNull Heading heading) {
        int x = space.x;
        int y = space.y;
        switch (heading) {
            case SOUTH:
                y = (y + 1) % height;
                break;
            case WEST:
                x = (x + width - 1) % width;
                break;
            case NORTH:
                y = (y + height - 1) % height;
                break;
            case EAST:
                x = (x + 1) % width;
                break;
        }

        return getSpace(x, y);
    }

    /**
     * This method returns a status message containing the phase, player and steps.
     *
     * @return - the string status message.
     */
    public String getStatusMessage() {
        // this is actually a view aspect, but for making assignment V1 easy for
        // the students, this method gives a string representation of the current
        // status of the game

        // XXX: V2 changed the status so that it shows the phase, the player and the step
        return "Phase: " + getPhase().name() +
                ", Player = " + getCurrentPlayer().getName() +
                ", Step: " + getStep();
    }
}
