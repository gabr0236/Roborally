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
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.upgrade.Upgrade;
import javafx.scene.control.TabPane;

import java.util.*;
import java.util.stream.Stream;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */

public class PlayersView extends TabPane implements ViewObserver {

    private final Board board;

    private List<PlayerView> playerViews = new ArrayList<>();

    private GameController gameController;

    /**
     * Constructs a PlayerView for each player.
     *
     * @param gameController
     */
    public PlayersView(GameController gameController) {
        board = gameController.board;
        this.gameController=gameController;
        this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            playerViews.add(new PlayerView(gameController, board.getPlayer(i)));
            this.getTabs().add(playerViews.get(i));
        }
        board.attach(this);
        update(board);
    }

    /**
     * Updates the view with the player tabs sorted in antenna order (after the distance from antenna).
     */
    @Override
    public void updateView(Subject subject) {
        if (subject == board && Phase.PROGRAMMING==board.getPhase()) {
            Player current = board.getCurrentPlayer();
            this.getSelectionModel().select(board.getPlayerNumber(current));

                Collections.sort(playerViews);
                this.getTabs().clear();
                for (int i = 0; i < board.getPlayersNumber(); i++) {
                    this.getTabs().add(playerViews.get(i));
                }
        }
    }

}
