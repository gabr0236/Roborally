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
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * <p>
 * Javadoc
 * @author Gabriel
 */
public class BoardView extends VBox implements ViewObserver {

    private final Board board;

    private final GridPane mainBoardPane;
    private final SpaceView[][] spaces;

    private final PlayersView playersView;

    private final SpaceEventHandler spaceEventHandler;

    /**
     * Skaber GUI for Board, herunder ternede felter, samt PlayerView
     * Bruger gameController til at opsætte dette.
     *
     * @param gameController
     */
    public BoardView(@NotNull GameController gameController) {
        board = gameController.board;

        mainBoardPane = new GridPane();
        playersView = new PlayersView(gameController);

        this.getChildren().add(mainBoardPane);
        this.getChildren().add(playersView);

        spaces = new SpaceView[board.width][board.height];

        spaceEventHandler = new SpaceEventHandler(gameController);

        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                Space space = board.getSpace(x, y);
                SpaceView spaceView = new SpaceView(space);
                spaces[x][y] = spaceView;
                mainBoardPane.add(spaceView, x, y);
                //kan nok slettes
                spaceView.setOnMouseClicked(spaceEventHandler);
            }
        }
        board.attach(this);
        update(board);
    }

    @Override
    public void updateView(Subject subject) {
        if (subject == board) {
        }
    }

    // XXX this handler and its uses should eventually be deleted! This is just to help test the
    //     behaviour of the game by being able to explicitly move the players on the board!
    private class SpaceEventHandler implements EventHandler<MouseEvent> {

        final public GameController gameController;

        public SpaceEventHandler(@NotNull GameController gameController) {
            this.gameController = gameController;
        }

        @Override
        public void handle(MouseEvent event) {
            Object source = event.getSource();
            if (source instanceof SpaceView spaceView) {
                Space space = spaceView.space;
                Board board = space.board;

                if (board == gameController.board) {
                    event.consume();
                }
            }
        }

    }

}
