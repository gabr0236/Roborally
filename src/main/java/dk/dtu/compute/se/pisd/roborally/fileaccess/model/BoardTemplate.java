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
package dk.dtu.compute.se.pisd.roborally.fileaccess.model;

import javax.swing.plaf.LabelUI;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates the template for the board and has:
 * width/height - the size of the board
 * spaces - a list of spaces that represents each space on the board
 * rebootBorderXValues - a list used to identify were players should reboot
 * numberOfCheckpoints - used to identify the amount of checkpoints a player needs in order to win
 * boardName - used to identify each board
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class BoardTemplate {
    public int width;
    public int height;
    public List<SpaceTemplate> spaces = new ArrayList<>();
    public List<Integer> rebootBorderXValues = new ArrayList<>();
    public int numberOfCheckpoints;
    public String boardName;
}
