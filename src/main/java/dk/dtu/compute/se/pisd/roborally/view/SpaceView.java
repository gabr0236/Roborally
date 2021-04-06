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
import dk.dtu.compute.se.pisd.roborally.model.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class SpaceView extends StackPane implements ViewObserver {

    //TODO: midlertidig løsning for at spillet kan blive inden for skærmen på mac
    final public static int SPACE_HEIGHT = 45; // 60; // 75;
    final public static int SPACE_WIDTH = 45;  // 60; // 75;

    public final Space space;

    StackPane dynamic;

    public SpaceView(@NotNull Space space) {
        this.space = space;

        // XXX the following styling should better be done with styles
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth(SPACE_WIDTH);
        this.setMaxWidth(SPACE_WIDTH);

        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight(SPACE_HEIGHT);
        this.setMaxHeight(SPACE_HEIGHT);

        if ((space.x + space.y) % 2 == 0) {
            this.setStyle("-fx-background-color: Silver");
        } else {
            this.setStyle("-fx-background-color: SlateGrey");
        }
        staticElements();
        dynamic = new StackPane();
        this.getChildren().add(dynamic);
        // updatePlayer();
        // This space view should listen to changes of the space
        space.attach(this);
        update(space);
    }

    /**
     * @author Gabriel
     */
    private void updatePlayer() {
        dynamic.getChildren().clear();
        Player player = space.getPlayer();
        if (player != null) {
            Polygon arrow = new Polygon(0.0, 0.0,
                    10.0, 20.0,
                    20.0, 0.0);
            try {
                arrow.setFill(Color.valueOf(player.getColor()));
            } catch (Exception e) {
                arrow.setFill(Color.MEDIUMPURPLE);
            }
            arrow.setRotate((90 * player.getHeading().ordinal()) % 360);
            dynamic.getChildren().add(arrow);
        }
    }


    private void staticElements() {
        this.getChildren().clear();

        if (space.getPit()) {
            this.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #424341, #090703)");
            Text text = new Text();
            text.setText("PIT");
            this.getChildren().add(text);
        }

        if (space.getActivatableBoardElementList() != null) {
                if (space.getReboot() != null) {
                    this.setStyle("-fx-background-color: greenyellow");
                    Text text = new Text();
                    text.setText("R");
                    //text.setTabSize(12);
                    this.getChildren().add(text);
                }
        }

        if (space.getActivatableBoardElementList() != null) {
            for (ActivatableBoardElement activatableBoardElement:space.getActivatableBoardElementList()) {
                if (activatableBoardElement instanceof Checkpoint) {
                    //de er ikke helt centered 🤨🤨
                    Checkpoint checkpoint = (Checkpoint) activatableBoardElement;
                    Circle arrow = new Circle();
                    arrow.setFill(Color.YELLOW);
                    arrow.setRadius(18);
                    this.setStyle("-fx-background-color: Black");
                    this.getChildren().add(arrow);
                    Text text = new Text();
                    text.setText("C");
                    //text.setTabSize(12);
                    this.getChildren().add(text);
                }}
        }

        if (space.getActivatableBoardElementList() != null) {
            for (ActivatableBoardElement activatableBoardElement:space.getActivatableBoardElementList()) {

                if (activatableBoardElement instanceof Conveyor) {
                    //de er ikke helt centered 🤨🤨
                    Conveyor conveyor = (Conveyor) activatableBoardElement;
                    Polygon arrow = new Polygon(0.0, 0.0,
                            16.0, 30.0,
                            30.0, 0.0);
                    if (conveyor.getCommand() == Command.FAST_FORWARD) {
                        arrow.setFill(Color.LIGHTSKYBLUE);
                    } else {
                        arrow.setFill(Color.LIMEGREEN);
                    }
                    arrow.setRotate((90 * conveyor.getHeading().ordinal()) % 360);
                    this.setStyle("-fx-background-color: Black");
                    this.getChildren().add(arrow);
                }
            }
        }
        if (!space.getWallList().isEmpty()) {
            Canvas canvas = new Canvas(SPACE_WIDTH, SPACE_HEIGHT);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setStroke(Color.RED);
            gc.setLineWidth(5);
            gc.setLineCap(StrokeLineCap.ROUND);
            for (Heading wall : space.getWallList()) {
                switch (wall) {
                    case NORTH -> gc.strokeLine(2, 2, SPACE_WIDTH - 2, 2);
                    case EAST -> gc.strokeLine(SPACE_WIDTH - 2, 2, SPACE_WIDTH - 2, SPACE_HEIGHT - 2);
                    case SOUTH -> gc.strokeLine(2, SPACE_HEIGHT - 2, SPACE_WIDTH - 2, SPACE_HEIGHT - 2);
                    case WEST -> gc.strokeLine(2, 2, 2, SPACE_HEIGHT - 2);
                }
            }
            this.getChildren().add(canvas);
        }
    }

    @Override
    public void updateView(Subject subject) {
        if (subject == this.space) {
            updatePlayer();
        }
    }

}