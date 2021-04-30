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
import dk.dtu.compute.se.pisd.roborally.model.boardElements.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class SpaceView extends StackPane implements ViewObserver {

    final public static int SPACE_HEIGHT = 45;
    final public static int SPACE_WIDTH = 45;

    public final Space space;

    StackPane dynamic;

    /**
     * Creates a SpaceView with the specified width and height and set the color of the spaceView
     * calls staticElements() and creates a stackPane for dynamic elements
     * @param space
     */

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
     * clears and draws the robot and energyCubes and adds these to the stackPane
     */
    private void updatePlayer() {
            for (ActivatableBoardElement a: space.getActivatableBoardElements()) {
                if(a instanceof EnergySpace){
                    if(((EnergySpace) a).isEnergyAvailable()) {
                        Rectangle energyCube = new Rectangle(0, 0, 25, 25);
                        energyCube.setFill(Color.ORANGE);
                        dynamic.getChildren().add(energyCube);
                        Text text = new Text();
                        text.setText("E");
                        dynamic.getChildren().add(text);
                    }
                }
            }
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

    /**
     * draws all static elements such as pits, antenna, reboots spaces, laser and activatableBoardElements.
     */
    private void staticElements() {
        this.getChildren().clear();

        if (space.getPit()) {
            this.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #424341, #090703)");
            Text text = new Text();
            text.setText("PIT");
            this.getChildren().add(text);
        }

        else if (space.getIsAntenna()) {
            this.setStyle("-fx-background-color: #25b5b5");
            Polygon arrow = new Polygon(0.0, 0.0,
                    16.0, 30.0,
                    30.0, 0.0);
            arrow.setFill(Color.POWDERBLUE);
            arrow.setRotate(270);
            this.getChildren().add(arrow);
            Text text = new Text();
            text.setText("Antenna");
            text.setFont(Font.font("Arial, Helvetica, sans-serif", FontWeight.BOLD, FontPosture.REGULAR, 10));
            this.getChildren().add(text);
        }

        else if (space.getReboot() != null) {
            this.setStyle("-fx-background-color: greenyellow");
            Polygon arrow = new Polygon(0.0, 0.0,
                    16.0, 30.0,
                    30.0, 0.0);
            arrow.setFill(Color.LAWNGREEN);
            arrow.setRotate((90 * (space.getReboot().REBOOT_HEADING.ordinal()) % 360));
            this.getChildren().add(arrow);
            Text text = new Text();
            text.setText("R: " + space.getReboot().REBOOT_NUMBER);
            this.getChildren().add(text);
        }

        if (!space.getActivatableBoardElements().isEmpty()) {
            for (ActivatableBoardElement activatableBoardElement:space.getActivatableBoardElements()) {

                // Draw Checkpoint
                if (activatableBoardElement instanceof Checkpoint checkpoint) {
                    Circle circle = new Circle();
                    circle.setFill(Color.YELLOW);
                    circle.setRadius(18);
                    this.setStyle("-fx-background-color: Black");
                    this.getChildren().add(circle);
                    Text text = new Text();
                    text.setText("C: " + checkpoint.getCheckpointNumber());
                    this.getChildren().add(text);

                // Draw Conveyor
                } else if (activatableBoardElement instanceof Conveyor conveyor) {
                    Polygon arrow = new Polygon(0.0, 0.0,
                            16.0, 30.0,
                            30.0, 0.0);
                    if (conveyor.command == Command.FAST_FORWARD) {
                        arrow.setFill(Color.LIGHTSKYBLUE);
                    } else {
                        arrow.setFill(Color.LIMEGREEN);
                    }
                    arrow.setRotate((90 * conveyor.heading.ordinal()) % 360);
                    this.setStyle("-fx-background-color: Black");
                    this.getChildren().add(arrow);

                // Draw Gear
                } else if (activatableBoardElement instanceof Gear gear) {
                    // @author Tobias s205358
                    Image img;
                    ImageView iView;

                        if (gear.isClockwise()) {
                            img = new Image("file:dk/dtu/compute/se/pisd/Pictures/ClockwiseArrow.png");
                            iView = new ImageView(img);
                        } else {
                            img = new Image("file:dk/dtu/compute/se/pisd/Pictures/CounterClockwiseArrow.png");
                            iView = new ImageView(img);
                        }
                    Pane pane = new Pane(iView);
                        this.getChildren().add(pane);


                }

                //Draw EnergySpace
                else if (activatableBoardElement instanceof EnergySpace energySpace){
                    Rectangle energySpaceView = new Rectangle(0,0,35,35);
                    energySpaceView.setFill(Color.DIMGRAY);
                    this.getChildren().add(energySpaceView);
                    Text text = new Text();
                    text.setText("E");
                    this.getChildren().add(text);
                }

                else if (activatableBoardElement instanceof PushPanel){
                    PushPanel pushPanel = (PushPanel) activatableBoardElement;
                    if(pushPanel.getActivatingTurns()!=null) {
                        Canvas canvas = new Canvas(SPACE_WIDTH, SPACE_HEIGHT);
                        GraphicsContext gc = canvas.getGraphicsContext2D();
                        gc.setStroke(Color.YELLOW);
                        gc.setLineWidth(18);
                        gc.setLineCap(StrokeLineCap.ROUND);
                        switch (pushPanel.pushingDirection.oppositeHeading()) {
                            case NORTH -> gc.strokeLine(2, 2, SPACE_WIDTH - 2, 2);
                            case EAST -> gc.strokeLine(SPACE_WIDTH - 2, 2, SPACE_WIDTH - 2, SPACE_HEIGHT - 2);
                            case SOUTH -> gc.strokeLine(2, SPACE_HEIGHT - 2, SPACE_WIDTH - 2, SPACE_HEIGHT - 2);
                            case WEST -> gc.strokeLine(2, 2, 2, SPACE_HEIGHT - 2);
                        }
                        this.getChildren().add(canvas);
                        this.getChildren().add(combinePushPanelListToString(pushPanel.getActivatingTurns()));
                    }
                }
            }
        }

        if (space.getLaser()==null && !space.getWallList().isEmpty()){
            this.getChildren().add(drawWalls(space));
        }
        // draws laser
        else if (space.getLaser()!=null && space.getWallList().contains(space.getLaser().getShootingDirection().next().next())){
            Canvas canvas = new Canvas(SPACE_WIDTH, SPACE_HEIGHT);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setStroke(Color.DARKRED);
            gc.setLineWidth(7);
            gc.setLineCap(StrokeLineCap.ROUND);
            Heading shootingDirection = space.getLaser().getShootingDirection();

            //to get the direction from where the laser is placed on a wall
            switch (shootingDirection.next().next()) {
                case NORTH -> {gc.strokeLine(SPACE_WIDTH/2,2,SPACE_WIDTH/2,SPACE_HEIGHT/4);
                    gc.setStroke(Color.LIME);
                    gc.setLineWidth(4);
                    gc.strokeLine(SPACE_WIDTH/2,SPACE_HEIGHT/4.5,SPACE_WIDTH/2,SPACE_HEIGHT/4);}
                case WEST -> {gc.strokeLine(2, SPACE_HEIGHT/2, SPACE_WIDTH/4, SPACE_HEIGHT/2);
                    gc.setStroke(Color.LIME);
                    gc.setLineWidth(4);
                    gc.strokeLine(SPACE_WIDTH/4.5, SPACE_HEIGHT/2, SPACE_WIDTH/4, SPACE_HEIGHT/2);}

                case SOUTH -> {gc.strokeLine(SPACE_WIDTH/2,SPACE_HEIGHT,SPACE_WIDTH/2,SPACE_HEIGHT-(SPACE_HEIGHT/4));
                    gc.setStroke(Color.LIME);
                    gc.setLineWidth(4);
                    gc.strokeLine(SPACE_WIDTH/2,SPACE_HEIGHT-(SPACE_HEIGHT/4),SPACE_WIDTH/2,SPACE_HEIGHT-(SPACE_HEIGHT/4.5));}

                case EAST -> {gc.strokeLine(SPACE_WIDTH, SPACE_HEIGHT/2, SPACE_WIDTH-(SPACE_WIDTH/4), SPACE_HEIGHT/2);
                    gc.setStroke(Color.LIME);
                    gc.setLineWidth(4);
                    gc.strokeLine(SPACE_WIDTH-(SPACE_WIDTH/4.5), SPACE_HEIGHT/2, SPACE_WIDTH-(SPACE_WIDTH/4), SPACE_HEIGHT/2);}
            }
            this.getChildren().add(canvas);
            this.getChildren().add(drawWalls(space));
        }
    }

    /**
     * Writes the step on which a pushPanel is active on the PushPanel
     * @param integerList is the list of steps the PushPanel is active on
     * @return the text being written
     */
    private Text combinePushPanelListToString(List<Integer> integerList){
        List<Integer> pushPanelNumbers = new ArrayList<>();
        for(int i = 0; i < integerList.size(); i++){
            pushPanelNumbers.add(integerList.get(i)+1);
        }
       Text text = new Text(pushPanelNumbers.stream().map(i->(i).toString()).collect(Collectors.joining(", ")));
       text.setFont(new Font(SPACE_HEIGHT/5));
       return text;
    }

    /**
     * draws the walls of a space
     * @param space is the space which walls are drawn
     * @returns the canvas with the wall drawing if the space has walls
     */
    private Canvas drawWalls(Space space) {
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
            return canvas;
        }
        return null;
    }

    @Override
    public void updateView(Subject subject) {
        if (subject == this.space) {
            dynamic.getChildren().clear();
            updatePlayer();
        }
    }

}