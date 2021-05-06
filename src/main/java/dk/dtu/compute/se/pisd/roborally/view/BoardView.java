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
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Blend;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.jetbrains.annotations.NotNull;


/**
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * <p>
 */
public class BoardView extends VBox implements ViewObserver{

    private final Board board;

    private final GridPane mainBoardPane;
    private final SpaceView[][] spaces;

    private final PlayersView playersView;

    /**
     * Creates GUI for Board, therefore also the drawn spaces and PlayerView by using the gameController
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

        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                Space space = board.getSpace(x, y);
                SpaceView spaceView = new SpaceView(space);
                spaces[x][y] = spaceView;
                mainBoardPane.add(spaceView, x, y);
            }
        }
        board.attach(this);
    }

    /**
     * Checks if a player has won, if a player has won a winning screen appears
     * @param subject - used to upadte board, playerview and spaces
     */
    @Override
    public void updateView(Subject subject) {
        if(subject==board && board.getPhase()==Phase.GAME_WON){
            Player player = null;
            for (Player p:board.getPlayers()) {
                if(p.isPlayerWin()) player=p;
            }
            Canvas canvas = new Canvas((board.height*SpaceView.SPACE_HEIGHT*2), (board.width*SpaceView.SPACE_WIDTH*2));
            this.getChildren().add(canvas);
            this.setStyle("-fx-background-color: #25b5b5");
            GraphicsContext gc = canvas.getGraphicsContext2D();
            DropShadow ds = new DropShadow();
            ds.setOffsetX(3.0f);
            ds.setColor(Color.color(0.4f,0.4f,0.4f));
            gc.setEffect(ds);
            gc.setFill(Paint.valueOf(player.getColor()));
            gc.setStroke(Paint.valueOf("White"));
            gc.setLineWidth(1);
            Font theFont = Font.font("Arial", FontWeight.BOLD, SpaceView.SPACE_HEIGHT);
            gc.setFont(theFont);
            gc.fillText( "CONGRATULATIONS!!!!",40,50);
            gc.strokeText("CONGRATULATIONS!!!!",40,50);
            gc.fillText( player.getName()+" has won",120,120);
            gc.strokeText(player.getName()+" has won",120,120);
            gc.setFill(Paint.valueOf("Gold"));
            gc.setStroke(Paint.valueOf("White"));
            gc.fillText( "\uD83C\uDFC6 \uD83C\uDFC6 \uD83C\uDFC6 \uD83C\uDFC6 \uD83C\uDFC6 \uD83C\uDFC6",120,190);
            gc.strokeText("\uD83C\uDFC6 \uD83C\uDFC6 \uD83C\uDFC6 \uD83C\uDFC6 \uD83C\uDFC6 \uD83C\uDFC6",120,190);
        }
    }
}
