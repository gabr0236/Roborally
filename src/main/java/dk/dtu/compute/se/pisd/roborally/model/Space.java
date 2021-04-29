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
import dk.dtu.compute.se.pisd.roborally.model.boardElements.Laser;
import dk.dtu.compute.se.pisd.roborally.model.boardElements.Reboot;

import java.util.ArrayList;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Space extends Subject {

    public final Board board;
    public final int x;
    public final int y;
    private Player player;
    private ArrayList<Heading> wallList;
    private Reboot reboot;
    private final ArrayList<ActivatableBoardElement> activatableBoardElements = new ArrayList<>();
    private boolean isPit;
    private Laser laser;
    private boolean isAntenna;

    public Space(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
        this.wallList = new ArrayList<>();
        player = null;
    }

    public Space(Board board, int x, int y, ArrayList<Heading> wallList) {
        this.board = board;
        this.x = x;
        this.y = y;
        this.wallList = wallList;
        player = null;
    }

    public Board getBoard() {
        return board;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Sets player to this space and removes player from old space.
     * Calls notifyChange() to update view.
     * @param player
     */
    public void setPlayer(Player player) {
        Player oldPlayer = this.player;
        if (player != oldPlayer &&
                (player == null || board == player.board)) {
            this.player = player;
            if (oldPlayer != null) {
                // this should actually not happen
                oldPlayer.setSpace(null);
            }
            if (player != null) {
                player.setSpace(this);
            }
            notifyChange();
        }
    }

    /**
     * This is a minor hack; since some views that are registered with the space
     * also need to update when some player attributes change, the player can
     * notify the space of these changes by calling this method.
     */
    public void playerChanged() {
        // This is a minor hack; since some views that are registered with the space
        //
        // also need to update when some player attributes change, the player can
        // notify the space of these changes by calling this method.
        notifyChange();
    }

    public ArrayList<Heading> getWallList() {
        return wallList;
    }

    public void setWallList(ArrayList<Heading> wallList) {
        this.wallList = wallList;
    }

    public Reboot getReboot() {
        return reboot;
    }

    public void setReboot(Reboot reboot) {
        this.reboot = reboot;
    }

    public void setPit(boolean value){
        this.isPit=value;
    }

    public boolean getPit(){ return isPit;}

    public boolean getIsAntenna(){return  isAntenna;}

    public ArrayList<ActivatableBoardElement> getActivatableBoardElements() {
        return activatableBoardElements;
    }

    public Laser getLaser() {
        return laser;
    }

    public void setAntenna(boolean antenna) {
        isAntenna = antenna;
        if(this.isAntenna==true) {
            wallList.add(Heading.EAST);
            wallList.add(Heading.WEST);
            wallList.add(Heading.NORTH);
            wallList.add(Heading.SOUTH);
            setWallList(wallList);
        }
    }

    public void setLaser(Laser laser) {
        this.laser = laser;
    }
}
