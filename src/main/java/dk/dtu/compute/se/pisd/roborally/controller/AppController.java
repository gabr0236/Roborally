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
package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

import dk.dtu.compute.se.pisd.roborally.RoboRally;

import dk.dtu.compute.se.pisd.roborally.dal.IRepository;
import dk.dtu.compute.se.pisd.roborally.dal.RepositoryAccess;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class AppController implements Observer{

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);

    final private List<String> PLAYER_COLORS = Arrays.asList("Crimson", "CornflowerBlue", "Aqua", "Aquamarine", "Magenta", "DarkCyan", "DarkGoldenRod", "DarkKhaki", "DarkMagenta", "DeepPink");

    final private List<String> BOARDS = Arrays.asList("defaultboard","ChopShopChallenge");

    final private RoboRally roboRally;

    private GameController gameController;

    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;
    }

    /**
     * Creates a new game by creating a board, gamecontroller, players, view. Also starts the programming phase.
     * Also IRepository to create a game in DB.
     */
    public void newGame() {
        ChoiceDialog<String> boardDialog = new ChoiceDialog<>(BOARDS.get(0), BOARDS);
        boardDialog.setTitle("Board selector");
        boardDialog.setHeaderText("Select game board");
        Optional<String> resultBoard = boardDialog.showAndWait();

        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player count selector");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> result = dialog.showAndWait();

        if (result.isPresent()) {
            if (gameController != null) {
                // The UI should not allow this, but in case this happens anyway.
                // give the user the option to save the game or abort this operation!
                if (!stopGame()) {
                    return;
                }
            }

            Board board = LoadBoard.loadBoard(resultBoard.get());
            gameController = new GameController(board);

            int no = result.get();
            for (int i = 0; i < no; i++) {
                Pair<String, String> playerChoice = costomizePlayer(i);
                Player player = new Player(board, playerChoice.getValue(),playerChoice.getKey());
                board.addPlayer(player);
                player.setSpace(board.getRebootSpaceList().get(i));
                player.setRebootSpace(board.getRebootSpaceList().get(i));
            }

            gameController.startProgrammingPhase();

            IRepository repository = RepositoryAccess.getRepository();
            repository.createGameInDB(board);

            roboRally.createBoardView(gameController);
        }
    }

    /**
     * TODO: @Gab do something with the cancel button, add javadoc
     * @param playerNumber
     * @return
     * @author Gabriel
     */
    private Pair<String, String> costomizePlayer(int playerNumber){

        boolean validName = false;
        String name = "";

        while (!validName) {
                TextInputDialog textInputDialog = new TextInputDialog();
                textInputDialog.setTitle("Naming selector");
                textInputDialog.getDialogPane().setContentText("Name:");
                textInputDialog.setHeaderText("Player " + (playerNumber + 1) + " write your name:");
                Optional<String> result = textInputDialog.showAndWait();
                TextField input = textInputDialog.getEditor();

                //TODO @Gab better inputvalidation
                if(input.getText().toString().length()>=1){
                    validName=true;
                    name=input.getText();
                }
            }

        boolean validColor = false;
        String color = "";

        while (!validColor) {
            ChoiceDialog<String> dialog = new ChoiceDialog<>(PLAYER_COLORS.get(0), PLAYER_COLORS);
            dialog.setTitle("Player color");
            dialog.setHeaderText(name + " select a color");
            Optional<String> resultColor = dialog.showAndWait();
            color= resultColor.get();

            if(gameController.board.getPlayers().isEmpty()) {
                validColor=true;
            } else {
                for (Player player:gameController.board.getPlayers()) {
                    if(!player.getColor().equals(color)) {
                        validColor=true;
                    }
                }
            }
        }

        return new Pair<String, String>(name, color);
    }


    public void saveGame() {
        IRepository repository = RepositoryAccess.getRepository();
        repository.updateGameInDB(this.gameController.board);
    }


    /**
     * Loads game from DB, if no game is found this method creates a new game using newGame(); from above.
     */
    public void loadGame() {
        IRepository repository = RepositoryAccess.getRepository();
        gameController=new GameController(repository.loadGameFromDB(4));
        if (gameController == null) {
            newGame();
        }
        roboRally.createBoardView(gameController);
    }

    /**
     * Stop playing the current game, giving the user the option to save
     * the game or to cancel stopping the game. The method returns true
     * if the game was successfully stopped (with or without saving the
     * game); returns false, if the current game was not stopped. In case
     * there is no current game, false is returned.
     *
     * @return true if the current game was stopped, false otherwise
     */
    public boolean stopGame() {
        if (gameController != null) {

            // here we save the game (without asking the user).
            saveGame();

            gameController = null;
            roboRally.createBoardView(null);
            return true;
        }
        return false;
    }

    /**
     * Prompts the user access to close the game entirely. This will not save the game in the DB.
     */
    public void exit() {
        if (gameController != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Exit RoboRally?");
            alert.setContentText("Are you sure you want to exit RoboRally?");
            Optional<ButtonType> result = alert.showAndWait();

            if (!result.isPresent() || result.get() != ButtonType.OK) {
                return; // return without exiting the application
            }
        }

        // If the user did not cancel, the RoboRally application will exit
        // after the option to save the game
        if (gameController == null || stopGame()) {
            Platform.exit();
        }
    }

    public boolean isGameRunning() {
        return gameController != null;
    }


    @Override
    public void update(Subject subject) {
        // XXX do nothing for now
    }

    /**
     * TODO: @Gab javadoc
     * @author Gabriel
     */
    public void newTestGame() {
        Board board = LoadBoard.loadBoard("defaultboard");
        gameController = new GameController(board);

        for (int i = 0; i < 3; i++) {
            Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + i);
            board.addPlayer(player);
            player.setSpace(board.getRebootSpaceList().get(i));
            player.setRebootSpace(board.getRebootSpaceList().get(i));
        }
        gameController.startProgrammingPhase();

        IRepository repository = RepositoryAccess.getRepository();
        repository.createGameInDB(board);

        roboRally.createBoardView(gameController);
    }
}
