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

import dk.dtu.compute.se.pisd.roborally.dal.GameInDB;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class AppController implements Observer{

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);

    private List<String> playerColors = new LinkedList<String>(Arrays.asList("Crimson", "CornflowerBlue", "PaleVioletRed", "PapayaWhip", "PLUM", "DarkCyan", "DarkGoldenRod", "DarkKhaki", "DarkMagenta", "DeepPink", "Coral"));

    final private List<String> BOARDS = Arrays.asList("CORRIDOR BLITZ","ChopShopChallenge");

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
        String gameName = choseGameName();
            if(gameName==null) return;
        String gameBoard = choseBoard();
            if(gameBoard==null) return;
        Integer no = chosePlayerCount();
            if(no==null) return;

        Board board = LoadBoard.loadBoard(gameBoard);
        board.setGameName(gameName);
        gameController = new GameController(board);

        for (int i = 0; i < no; i++) {
                String name = chosePlayerName();
                if(name == null) {
                    gameController=null;
                    return;
                }

                String color = choseColor(name);
                if(color == null) {
                    gameController=null;
                    return;
                 }

                Player player = new Player(board, name, color);
                board.addPlayer(player);
                player.setSpace(board.getRebootSpaceList().get(i));
                player.setRebootSpace(board.getRebootSpaceList().get(i));
            }

            gameController.startProgrammingPhase();

            IRepository repository = RepositoryAccess.getRepository();
            repository.createGameInDB(board);

            roboRally.createBoardView(gameController);
    }



    private String choseColor(String playerName){
            ChoiceDialog<String> dialog = new ChoiceDialog<>(playerColors.get(0), playerColors);
            dialog.setTitle("Player color");
            dialog.setHeaderText(playerName + " select a color");
            Optional<String> resultColor = dialog.showAndWait();
            resultColor.ifPresent(s -> playerColors.remove(s));
            return resultColor.orElse(null);
    }

    private String chosePlayerName(){
        boolean validName = false;

        while (!validName) {
            TextInputDialog textInputDialog = new TextInputDialog();
            textInputDialog.setTitle("Player name selector");
            textInputDialog.getDialogPane().setContentText("Player name:");
            textInputDialog.setHeaderText("Write your name:");
            Optional<String> result = textInputDialog.showAndWait();

            //TODO @Gab better inputvalidation, use regex
            if(result.isPresent() && result.get().toString().length() >= 1) {
                return result.get();
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     *
     * @return
     * @author Gabriel
     */
    private String choseBoard(){
        ChoiceDialog<String> boardDialog = new ChoiceDialog<>(BOARDS.get(0), BOARDS);
        boardDialog.setTitle("Board selector");
        boardDialog.setHeaderText("Select game board");
        Optional<String> resultBoard = boardDialog.showAndWait();
        return resultBoard.orElse(null);
    }

    /**
     *
     * @return
     * @author Gabriel
     */
    private Integer chosePlayerCount(){
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player count selector");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> result = dialog.showAndWait();
        return result.orElse(null);
    }

    /**
     *
     * @return
     * @auther @Gabriel
     */
    private String choseGameName(){
        boolean validName = false;

        while (!validName) {
            TextInputDialog textInputDialog = new TextInputDialog();
            textInputDialog.setTitle("Game naming selector");
            textInputDialog.getDialogPane().setContentText("Game name:");
            textInputDialog.setHeaderText("Write a name for your game:");
            Optional<String> result = textInputDialog.showAndWait();

            //TODO @Gab better inputvalidation, use regex
            if(result.isPresent() && result.get().toString().length() >= 1) {
                    return result.get();
                } else {
                    return null;
                }
        }
        return null;
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

        List<GameInDB> gameIDList = repository.getGames();
        ChoiceDialog<GameInDB> dialog = new ChoiceDialog<>(gameIDList.get(0), gameIDList);
        dialog.setTitle("Game selector");
        dialog.setHeaderText("Select a game you want to continue");
        Optional<GameInDB> result = dialog.showAndWait();

        gameController = new GameController(repository.loadGameFromDB(result.get().id));

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
     *
     * @author Gabriel
     */
    public void newTestGame() {
        Board board = LoadBoard.loadBoard("CORRIDOR BLITZ");
        gameController = new GameController(board);

        for (int i = 0; i < 3; i++) {
            Player player = new Player(board, playerColors.get(i), "Player " + i);
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
