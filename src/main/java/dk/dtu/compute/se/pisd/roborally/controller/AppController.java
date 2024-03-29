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

import dk.dtu.compute.se.pisd.roborally.RoboRally;

import dk.dtu.compute.se.pisd.roborally.dal.GameInDB;
import dk.dtu.compute.se.pisd.roborally.dal.IRepository;
import dk.dtu.compute.se.pisd.roborally.dal.RepositoryAccess;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.*;

import dk.dtu.compute.se.pisd.roborally.model.boardElements.Checkpoint;
import dk.dtu.compute.se.pisd.roborally.model.upgrade.*;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class AppController {

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);

    private List<String> playerColors = new LinkedList<>(Arrays.asList("Crimson", "CornflowerBlue", "PaleVioletRed", "PapayaWhip", "PLUM", "DarkCyan", "DarkGoldenRod", "DarkKhaki", "DarkMagenta", "DeepPink", "Coral"));

    final private List<String> BOARDS = Arrays.asList("CORRIDOR BLITZ", "ChopShopChallenge");

    final private RoboRally roboRally;

    private GameController gameController;

    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;
    }

    /**
     * Creates a new game by creating a board, gamecontroller, players, view. Also starts the programming phase.
     * IRepository creates the game in the DB.
     */
    public void newGame() {
        playerColors = new LinkedList<>(Arrays.asList("Crimson", "CornflowerBlue", "PaleVioletRed", "PapayaWhip", "PLUM", "DarkCyan", "DarkGoldenRod", "DarkKhaki", "DarkMagenta", "DeepPink", "Coral"));

        String gameName = choseGameName();
        if (gameName == null) return;
        String gameBoard = choseBoard();
        if (gameBoard == null) return;
        Integer no = chosePlayerCount();
        if (no == null) return;

        Board board = LoadBoard.loadBoard(gameBoard);
        board.setGameName(gameName);
        gameController = new GameController(board);

        for (int i = 0; i < no; i++) {
            String name = chosePlayerName();
            if (name == null) {
                gameController = null;
                return;
            }

            String color = choseColor(name);
            if (color == null) {
                gameController = null;
                return;
            }

            Player player = new Player(board, color, name);
            board.addPlayer(player);
            player.setSpace(board.getRebootSpaceList().get(i));
            player.setRebootSpace(board.getRebootSpaceList().get(i));
        }
        setPlayerIDs();
        gameController.startProgrammingPhase();

        IRepository repository = RepositoryAccess.getRepository();
        repository.createGameInDB(board);


        roboRally.createBoardView(gameController);
    }

    /**
     * Shows dialog in which a player choses their color
     * @param playerName name for display on dialog
     * @return string color or null
     * @author @Gabriel
     */
    private String choseColor(String playerName) {
        boolean isColorChosen = false;
        while (!isColorChosen) {
            ChoiceDialog<String> dialog = new ChoiceDialog<>(playerColors.get(0), playerColors);
            dialog.setTitle("Player color");
            dialog.setHeaderText(playerName + " select a color");
            Optional<String> resultColor = dialog.showAndWait();
            resultColor.ifPresent(s -> playerColors.remove(s));
            if (resultColor.isPresent()) {
                return resultColor.get();
            } else {
                if (cancelGameSetup()) return null;
            }
        }
        return null;
    }

    /**
     * Shows dialog in which a player choses their name
     * @return the chosen name or null
     * @author @Gabriel
     */
    private String chosePlayerName() {
        boolean validName = false;
        while (!validName) {
            TextInputDialog textInputDialog = new TextInputDialog();
            textInputDialog.setTitle("Player name selector");
            textInputDialog.getDialogPane().setContentText("Player name:");
            textInputDialog.setHeaderText("Write your name:");
            Optional<String> playerName = textInputDialog.showAndWait();

            if (playerName.isPresent() && playerName.get().matches("^([ 0-9\\u00c0-\\u01ffa-zA-Z'\\-]){3,20}$")) {
                return playerName.get();
            } else if (playerName.isPresent()) {
                invalidName();
            } else {
                if (cancelGameSetup()) return null;
            }
        }
        return null;
    }

    /**
     * Shows dialog in which the players can pick between diffrent boards
     * @return string with boardname
     * @author @Gabriel
     */
    private String choseBoard() {
        boolean validBoard = false;
        while (!validBoard) {
            ChoiceDialog<String> boardDialog = new ChoiceDialog<>(BOARDS.get(1), BOARDS);
            boardDialog.setTitle("Board selector");
            boardDialog.setHeaderText("Select game board");
            Optional<String> resultBoard = boardDialog.showAndWait();
            if (resultBoard.isPresent()) {
                return resultBoard.get();
            } else {
                if (cancelGameSetup()) return null;
            }
        }
        return null;
    }

    /**
     * Shows dialog in which the players can pick how many players the game should have (min 2, max 6)
     * @return returns the number of players chosen
     * @author @Gabriel
     */
    private Integer chosePlayerCount() {
        boolean playerCount = false;
        while (!playerCount) {
            ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
            dialog.setTitle("Player count selector");
            dialog.setHeaderText("Select number of players");
            Optional<Integer> result = dialog.showAndWait();
            if (result.isPresent()) {
                return result.get();
            } else {
                if (cancelGameSetup()) return null;
            }
        }
        return null;
    }

    /**
     * Shows dialog in which the players can pick a name for the game used for storing in DB and when loading a game
     * @return string with gamename
     * @author @Gabriel
     */
    private String choseGameName() {
        boolean validName = false;

        while (!validName) {
            TextInputDialog textInputDialog = new TextInputDialog();
            textInputDialog.setTitle("Game naming selector");
            textInputDialog.getDialogPane().setContentText("Game name:");
            textInputDialog.setHeaderText("Write a name for your game:");
            Optional<String> gameName = textInputDialog.showAndWait();

            if (gameName.isPresent() && gameName.get().matches("^([0-9\\u00c0-\\u01ffa-zA-Z'\\-]){3,20}$")) {
                return gameName.get();
            } else if (gameName.isPresent()) {
                invalidName();
            } else {
                if (cancelGameSetup()) return null;
            }
        }
        return null;
    }

    /**
     * Used as errorhandler when a player enters a name that doesnt fit the regex (used both when picking playername and boardname)
     * @author @Gabriel
     */
    private void invalidName() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Oh No!");
        alert.setHeaderText(null);
        alert.setContentText("Enter a valid name between 3-20 characters");
        alert.showAndWait();
    }

    /**
     * Alert box which appears whenever a player tries to exit the gamesetup. To make sure a player doesnt exit the setup accidentally.
     * @return boolean. True if player presses OK to exit setup
     * @author @Gabriel
     */
    private boolean cancelGameSetup() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Exit game setup");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to exit the game setup?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    /**
     * Updates game in DB
     */
    public void saveGame() {
        IRepository repository = RepositoryAccess.getRepository();
        repository.updateGameInDB(this.gameController.board);
    }

    /**
     * Loads game from DB, if no game is found this method creates a new game using newGame(); from above.
     * @author @Gabriel
     */
    public void loadGame() {
        IRepository repository = RepositoryAccess.getRepository();

        boolean gameChosen = false;

        while (!gameChosen) {

            List<GameInDB> gameIDList = repository.getGames();
            if (gameIDList!=null) {
                ChoiceDialog<GameInDB> dialog = new ChoiceDialog<>(gameIDList.get(0), gameIDList);
                dialog.setTitle("Game selector");
                dialog.setHeaderText("Select a game you want to continue");
                Optional<GameInDB> result = dialog.showAndWait();
                if (result.isPresent()) {
                    gameController = new GameController(repository.loadGameFromDB(result.get().id));
                    gameChosen = true;
                } else {
                    if (cancelGameSetup()) return;
                }
            }
            else {
                return;
            }
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




    /**
     * For testing a upgrade quickly
     * @author @Gabriel
     */
    public void newTestGameCorridorBlitz() {
        Board board = LoadBoard.loadBoard("CORRIDOR BLITZ");
        gameController = new GameController(board);

        for (int i = 0; i < 3; i++) {
            Player player = new Player(board, playerColors.get(i), "Player " + i);
            board.addPlayer(player);
            player.setSpace(board.getRebootSpaceList().get(i));
            player.setRebootSpace(board.getRebootSpaceList().get(i));
        }
        setPlayerIDs();
        gameController.startProgrammingPhase();
        IRepository repository = RepositoryAccess.getRepository();
        repository.createGameInDB(board);

        roboRally.createBoardView(gameController);
    }

    /**
     * For testing a game quickly
     * @author @Gabriel
     */
    public void newTestGameChopShopChallenge() {
        Board board = LoadBoard.loadBoard("ChopShopChallenge");
        gameController = new GameController(board);

        for (int i = 0; i < 3; i++) {
            Player player = new Player(board, playerColors.get(i), "Player " + i);
            board.addPlayer(player);
            player.setSpace(board.getRebootSpaceList().get(i));
            player.setRebootSpace(board.getRebootSpaceList().get(i));
        }
        RearLaser rearLaser = new RearLaser();
        RailGun railGun = new RailGun();
        LuckyShield luckyShield = new LuckyShield();
        board.getPlayers().get(1).getUpgrades().add(rearLaser);
        board.getPlayers().get(1).getUpgrades().add(railGun);
        board.getPlayers().get(1).getUpgrades().add(luckyShield);
        board.getPlayer(1).getUpgrades().add(new PushLeftOrRight());
        board.getPlayer(1).getUpgrades().add(new BlueScreenDeath());

        board.getSpace(1,3).setPlayer(board.getPlayer(1));
        board.getPlayer(1).setHeading(Heading.SOUTH);
        board.getPlayer(1).getProgramField(0).setCard(new CommandCard(Command.FORWARD));
        setPlayerIDs();
        gameController.startProgrammingPhase();

        IRepository repository = RepositoryAccess.getRepository();
        repository.createGameInDB(board);

        roboRally.createBoardView(gameController);
    }

    /**
     * For testing a game quickly
     * @author @Gabriel
     */
    public void newTestGameWin() {
        Board board = LoadBoard.loadBoard("CORRIDOR BLITZ");
        gameController = new GameController(board);

        for (int i = 0; i < 3; i++) {
            Player player = new Player(board, playerColors.get(i), "Player " + i);
            board.addPlayer(player);
            player.setSpace(board.getRebootSpaceList().get(i));
            player.setRebootSpace(board.getRebootSpaceList().get(i));
        }
        board.getSpace(2,1).getActivatableBoardElements().add(new Checkpoint(2));
        board.setNumberOfCheckpoints(2);
        board.getPlayers().get(0).setLastCheckpointVisited(1);
        board.setCurrentPlayer(board.getPlayer(0));
        setPlayerIDs();

        gameController.startProgrammingPhase();

        IRepository repository = RepositoryAccess.getRepository();
        repository.createGameInDB(board);

        roboRally.createBoardView(gameController);
    }

    /**
     * For testing a upgrade quickly
     * @author @Gabriel
     */
    public void PushLeftOrRightTest() {
        Board board = LoadBoard.loadBoard("CORRIDOR BLITZ");
        gameController = new GameController(board);

        for (int i = 0; i < 4; i++) {
            Player player = new Player(board, playerColors.get(i), "Player " + i);
            board.addPlayer(player);
            player.setSpace(board.getRebootSpaceList().get(i));
            player.setRebootSpace(board.getRebootSpaceList().get(i));
        }

        board.getPlayer(2).getUpgrades().add(new PushLeftOrRight());
        board.getPlayer(2).setHeading(Heading.SOUTH);

        setPlayerIDs();
        gameController.startProgrammingPhase();
        board.getPlayer(0).getProgramField(0).setCard(new CommandCard(Command.FORWARD));
        IRepository repository = RepositoryAccess.getRepository();
        repository.createGameInDB(board);

        roboRally.createBoardView(gameController);
    }

    /**
     * Sets the database ID's for all players
     * @author @Gabriel
     */
    private void setPlayerIDs() {
        gameController.updatePlayersAntennaDistance();
        for (int i = 0; i < gameController.board.getPlayersNumber(); i++) {
            gameController.board.getPlayer(i).setPlayerID(i);
        }
    }
}
