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
import java.util.Collections;
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


    private int numberOfCheckpoints;

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

    public Player getCurrentPlayer() {
        return current;
    }

    public void setCurrentPlayer(Player player) {
        if (player != this.current && players.contains(player)) {
            this.current = player;
            notifyChange();
        }
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        if (phase != this.phase) {
            this.phase = phase;
            notifyChange();
        }
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        if (step != this.step) {
            this.step = step;
            notifyChange();
        }
    }

    public boolean isStepMode() {
        return stepMode;
    }

    public void setStepMode(boolean stepMode) {
        if (stepMode != this.stepMode) {
            this.stepMode = stepMode;
            notifyChange();
        }
    }

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
    public Space getNeighbour(@NotNull Space space, @NotNull Heading heading) {
        int x = space.x;
        int y = space.y;
        switch (heading) {
            case SOUTH -> y = (y + 1);
            case WEST -> x = (x - 1);
            case NORTH -> y = (y - 1);
            case EAST -> x = (x + 1);
        }

        return getSpace(x, y);
    }

    public String getStatusMessage(Player player) {
        return "Phase: " + getPhase().name() +
                ", Player: " + player.getName() +
                ", Step: " + getStep() +
                ", Next checkpoint: " + (player.getLastCheckpointVisited()+1) +
                ", R: " + player.getRebootSpace().getReboot().REBOOT_NUMBER;
    }


    public List<Player> getPlayers() {
        return players;
    }

    public List<Space> getSpacesList() {
        return Collections.unmodifiableList(spacesList);
    }

    public List<Space> getRebootSpaceList() { return rebootSpaceList; }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getNumberOfCheckpoints() {
        return numberOfCheckpoints;
    }

    public void setNumberOfCheckpoints(int numberOfCheckpoints) {
        this.numberOfCheckpoints = numberOfCheckpoints;
    }

    public List<Space> getLaserSpaceList() { return laserSpaceList; }

    public List<Integer> getRebootBorderXValues() { return rebootBorderXValues; }

}
