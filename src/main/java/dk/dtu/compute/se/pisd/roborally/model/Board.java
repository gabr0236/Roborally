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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.NORTH;
import static dk.dtu.compute.se.pisd.roborally.model.Phase.INITIALISATION;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Board extends Subject {

    //tobias
    public final int width;

    public final int height;

    public final String boardName;

    private Integer gameId;

    public Space[][] getSpaces() {
        return spaces;
    }

    private final List<Space> spacesList = new ArrayList<>();

    private final Space[][] spaces;

    private final List<Player> players = new ArrayList<>();

    private Player current;

    private Phase phase = INITIALISATION;

    private int step = 0;

    private boolean stepMode;

    public final int rebootBorderX;

    private List<Space> rebootSpaceList = new ArrayList<>();

    public Board(int width, int height, @NotNull String boardName) {
        this.boardName = boardName;
        this.width = width;
        this.height = height;
        this.rebootBorderX=2;
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

    public void setBoard(){
        spaces[1][2].setWallList(new ArrayList<Heading>() {{ add(Heading.NORTH); }});
        spaces[2][4].setWallList(new ArrayList<Heading>() {{ add(Heading.EAST); }});
        spaces[2][5].setWallList(new ArrayList<Heading>() {{ add(Heading.EAST); }});
        spaces[1][7].setWallList(new ArrayList<Heading>() {{ add(Heading.SOUTH); }});
        spaces[2][0].setActivatableBoardElement(new Conveyor(Heading.EAST, Command.FORWARD));
        spaces[5][5].setActivatableBoardElement(new Conveyor(NORTH, Command.FAST_FORWARD));
        spaces[2][9].setActivatableBoardElement(new Conveyor(Heading.EAST, Command.FORWARD));
        spaces[12][4].setActivatableBoardElement(new Checkpoint());
        spaces[0][1].setActivatableBoardElement(new Checkpoint());
        spaces[1][1].setReboot(new Reboot(Heading.EAST,true));
        spaces[0][3].setReboot(new Reboot(Heading.EAST,true));
        spaces[1][4].setReboot(new Reboot(Heading.EAST,true));
        spaces[1][5].setReboot(new Reboot(Heading.EAST,true));
        spaces[0][6].setReboot(new Reboot(Heading.EAST,true));
        spaces[1][8].setReboot(new Reboot(Heading.EAST,true));
        spaces[5][4].setReboot(new Reboot(Heading.EAST,false));
        spaces[6][4].setPit();
        spaces[10][7].setPit();
        spaces[7][1].setPit();
        //TODO: @gab reboot skal laves lidt på en anden måde mht. listen
        rebootSpaceList=(Arrays.asList(spaces[1][1], spaces[0][3], spaces[1][4], spaces[1][5],
                spaces[0][6], spaces[1][8],spaces[5][4]));
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
            case SOUTH:
                y = (y + 1);
                break;
            case WEST:
                x = (x - 1);
                break;
            case NORTH:
                y = (y - 1);
                break;
            case EAST:
                x = (x + 1);
                break;
        }

        Space neighbour = getSpace(x, y);
        return neighbour;
    }

    public String getStatusMessage() {
        // this is actually a view aspect, but for making assignment V1 easy for
        // the students, this method gives a string representation of the current
        // status of the game

        // XXX: V2 changed the status so that it shows the phase, the player and the step
        return "Phase: " + getPhase().name() +
                ", Player = " + getCurrentPlayer().getName() +
                ", Step: " + getStep();
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public List<Space> getSpacesList() {
        return Collections.unmodifiableList(spacesList);
    }

    public List<Space> getRebootSpaceList() {
        return Collections.unmodifiableList((rebootSpaceList));
    }
}
