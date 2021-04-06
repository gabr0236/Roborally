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

import dk.dtu.compute.se.pisd.roborally.model.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class GameController {

    final public Board board;

    public GameController(@NotNull Board board) {
        this.board = board;
    }



    /**
     * Sets phase to programming, currentplayer to 0, and step to 0.
     * Assigns random cards to each players hand.
     */
    public void startProgrammingPhase() {
        board.setPhase(Phase.PROGRAMMING);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    CommandCardField field = player.getProgramField(j);
                    field.setCard(null);
                    field.setVisible(true);
                }
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    CommandCardField field = player.getCardField(j);
                    field.setCard(generateRandomCommandCard());
                    field.setVisible(true);
                }
            }
        }
    }

    /**
     * @return random CommandCard
     */
    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    /**
     * Switches phase to activation
     */
    public void finishProgrammingPhase() {
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        board.setPhase(Phase.ACTIVATION);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

    }


    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    private void makeProgramFieldsInvisible() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                CommandCardField field = player.getProgramField(j);
                field.setVisible(false);
            }
        }
    }

    /**
     * Sets stepMode to false, and execute all registers
     */
    public void executePrograms() {
        board.setStepMode(false);
        continuePrograms();
    }

    /**
     * Executes a single register
     */
    public void executeStep() {
        board.setStepMode(true);
        continuePrograms();
    }

    /**
     *Executes a single register or executes all registers depending on whether board isStepMode is true
     */
    private void continuePrograms() {
        do {
            executeNextStep();
        } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());
    }

    /**
     * Executes the current register for the current player and calls nextPlayerOrPhase().
     */
    private void executeNextStep() {
        Player currentPlayer = board.getCurrentPlayer();
        if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null) {
            int step = board.getStep();
            if (step >= 0 && step < Player.NO_REGISTERS) {
                CommandCard card = currentPlayer.getProgramField(step).getCard();
                if (card != null && card.command.isInteractive()) {
                    board.setPhase(Phase.PLAYER_INTERACTION);
                    return;
                }
                if (card != null) {
                    Command command = card.command;
                    executeCommand(currentPlayer,currentPlayer.getHeading(), command);
                }
                nextPlayerOrPhase();
            } else {
                // this should not happen
                assert false;
            }
        } else {
            // this should not happen
            assert false;
        }
    }

    /**
     * Calls the move method for a player corresponding to the command passed as param.
     * @param player
     * @param heading
     * @param command
     */
    public void executeCommand(@NotNull Player player, Heading heading, Command command) {
        if (player != null && player.board == board && command != null) {
            // XXX This is a very simplistic way of dealing with some basic cards and
            //     their execution. This should eventually be done in a more elegant way
            //     (this concerns the way cards are modelled as well as the way they are executed).

            switch (command) {
                case FORWARD:
                    this.directionMove(player, heading);
                    break;
                case RIGHT:
                    this.turnRight(player);
                    break;
                case LEFT:
                    this.turnLeft(player);
                    break;
                case FAST_FORWARD:
                    this.fastForward(player, heading);
                    break;
                default:
                    // DO NOTHING (for now)
            }
        }
    }

    /**
     * Moves a player forward in a specific direction.
     * @param player
     * @author Gabriel
     */
    public void directionMove(@NotNull Player player, @NotNull Heading heading) {
        Space current = player.getSpace();
        if (current != null && player.board == current.board) {
            Space target = current.board.getNeighbour(current, heading);
            if (target != null) {
                if (notWallsBlock(player.getSpace(), heading)) {
                    try{
                        moveToSpace(player, target, heading);
                    } catch (ImpossibleMoveException e){

                    }
                }
            }
        }
    }
    //TODO: @gab optimise movetospace, direction move, og execute command, we can probably combine these methods somehow

    /**
     * Moves a player forward in a specific direction, and pushes, any player in the way, forward.
     * @param player
     * @param space
     * @param heading
     * @throws ImpossibleMoveException
     */
    public void moveToSpace(@NotNull Player player, @NotNull Space space, @NotNull Heading heading) throws ImpossibleMoveException {
        assert board.getNeighbour(player.getSpace(), heading) == space; // make sure the move to here is possible in principle
        Player other = space.getPlayer();
        Space target = board.getNeighbour(space, heading);
        if (other != null) {

            if (target != null) {
                if (notWallsBlock(other.getSpace(), heading)) {
                    // XXX Note that there might be additional problems with
                    //     infinite recursion here (in some special cases)!
                    //     We will come back to that!

                    moveToSpace(other, target, heading);

                    // Note that we do NOT embed the above statement in a try catch block, since
                    // the thrown exception is supposed to be passed on to the caller

                    assert space.getPlayer() == null : space; // make sure target is free now
                } else {
                   throw new ImpossibleMoveException(player, space, heading);
                }
            }
        }
        player.setSpace(space);
        fallIntoPit(player);
    }


    /**
     *Returns true if no walls is blocking a move in a specific direction
     * @param space
     * @param heading
     * @return
     * @author Gabriel
     */
    public boolean notWallsBlock(@NotNull Space space, Heading heading) {
        return (!isCurrentSpaceWallBlockingDirection(space, heading)
                && !isHeadingNeighbourWallBlockingDirection(space, heading));
    }

    /**
     *Returns true if a wall is blocking a player from moving on the players space.
     * @param space
     * @param heading
     * @return
     * @author Gabriel
     */
    public boolean isCurrentSpaceWallBlockingDirection(@NotNull Space space, Heading heading) {
        ArrayList<Heading> walls = space.getWallList();
        if (!walls.isEmpty()) {
            return walls.contains(heading);
        }
        return false;
    }

    /**
     *
     * Returns true if a wall is blocking a player from moving on the players neighbours space.
     * @param space
     * @param heading
     * @return
     * @author Gabriel
     */
    public boolean isHeadingNeighbourWallBlockingDirection(@NotNull Space space, Heading heading) {
        Space neighbour = board.getNeighbour(space, heading);
        if (neighbour != null && !neighbour.getWallList().isEmpty()) {
            Heading oppositeHeading = heading.oppositeHeading();
            return neighbour.getWallList().contains(oppositeHeading);
        }
        return false;
    }


    /**
     * Moves a player 2 spaces forward.
     * @param player
     * @author Gabriel
     */
    public void fastForward(@NotNull Player player, @NotNull Heading heading) {
        directionMove(player, heading);
        directionMove(player, heading);
    }

    /**
     * Turns a player to the right.
     * @param player
     * @author Gabriel
     */
    public void turnRight(@NotNull Player player) {
        player.setHeading(player.getHeading().next());

    }

    /**
     * Turns a player to the left.
     * @param player
     * @author Gabriel
     */
    public void turnLeft(@NotNull Player player) {
        player.setHeading(player.getHeading().prev());
    }

    /**
     * Executes a option from the optional CommandCard chosen by the player on the GUI.
     * @param option
     * @author Gabriel
     */
    public void executeCommandOptionAndContinue(@NotNull Command option) {
        Player currentPlayer = board.getCurrentPlayer();
        if (currentPlayer != null && Phase.PLAYER_INTERACTION == board.getPhase() && option != null) {
            board.setPhase(Phase.ACTIVATION);
            executeCommand(currentPlayer,currentPlayer.getHeading(), option);
            nextPlayerOrPhase();
        }
    }

    /**
     * Moves a players CommandCard to a different position in hand.
     * @param source
     * @param target
     * @return
     */
    public boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target) {
        CommandCard sourceCard = source.getCard();
        CommandCard targetCard = target.getCard();
        if (sourceCard != null && targetCard == null) {
            target.setCard(sourceCard);
            source.setCard(null);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Changes phase if all players have completed all of their programs steps,
     * otherwise change player to next player and change to next step
     * @author Gabriel
     */
    private void nextPlayerOrPhase() {
        Player currentPlayer = board.getCurrentPlayer();
        int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
        int step = board.getStep();
        if (nextPlayerNumber < board.getPlayersNumber()) {
            board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
            continuePrograms();
        } else {
            executeBoardElements();
            updateAllReboot();
            step++;
            if (step < Player.NO_REGISTERS) {
                makeProgramFieldsVisible(step);
                board.setStep(step);
                board.setCurrentPlayer(board.getPlayer(0));
            } else {
                respawnPlayers();
                startProgrammingPhase();
            }
        }
    }

    /**
     * Executes all ActivatableBoardElements activateElement methods
     * @author Gabriel
     */
    public void executeBoardElements() {
        if (board.getSpacesList() != null) {
            for (Space space : board.getSpacesList()) {
                Player player = space.getPlayer();
                for (ActivatableBoardElement activatableBoardElement:space.getActivatableBoardElementList()) {
                    if (player != null && activatableBoardElement != null) {
                        activatableBoardElement.activateElement(space.getPlayer(), this);
                    }
                }
            }
        }
    }

    /**
     * Registers a players checkpoint in player, and calls findWinner() if player have gathered all checkpoints
     * @param player
     * @param checkpointNumber
     * @author Gabriel
     */
    public void registerCheckpoint(@NotNull Player player, int checkpointNumber) {
        if (player != null) {
            if (checkpointNumber == player.getLastCheckpointVisited() + 1) {
                player.setLastCheckpointVisited(checkpointNumber);
                findWinner(player);
            }
        }
    }

    /**
     * TODO: this need to be updated at some point to show a winning screen and maybe delete game from database?
     * TODO: @Gab this is not pretty, i will redo later
     * Prints out the winning player
     * @param player
     * @author Gabriel
     */
    public void findWinner(@NotNull Player player) {
        if (player.getLastCheckpointVisited()==board.getNumberOfCheckpoints()) {
            player.setPlayerWin(true);
            System.out.println(player.getColor() + " har vundet!!");
        }
    }

    /**
     * TODO: @Gab merge with the method below
     * Calls updatePlayerRebootSpace for each player
     * @author Gabriel
     */
    private void updateAllReboot(){
        for (Player player :board.getPlayers()) {
            updatePlayerRebootSpace(player);
        }
    }

    /**
     * Updates a players reboot space if the player have left the startfield
     * @param player
     * @author Gabriel
     */
    private void updatePlayerRebootSpace(@NotNull Player player){
        Space current=player.getSpace();
        if(current!=null){
            if(current.x>current.board.rebootBorderX && player.getRebootSpace().getReboot().isStartField()){
                for (Space space :board.getRebootSpaceList()) {
                    if(!space.getReboot().isStartField()) {
                        player.setRebootSpace(space);
                    }
                }
            }
        }
    }

    /**
     * Respawns all players
     * @author Gabriel
     */
    public void respawnPlayers(){
        for (Player player:board.getPlayers()) {
            if(player.getSpace()==null){
                teleportPlayerToReboot(player);
            }
        }
    }

    /**
     * Teleports players to reboot space and pushes players forward if multiple players is respawning on the same space
     * @param player
     */
    private void teleportPlayerToReboot(@NotNull Player player){
        if (player.getRebootSpace().getPlayer() != null){
            directionMove(player.getRebootSpace().getPlayer(), player.getRebootSpace().getReboot().REBOOT_HEADING);
        }
        player.setSpace(player.getRebootSpace());
        player.setHeading(player.getRebootSpace().getReboot().REBOOT_HEADING);
    }

    /**
     * Player falls into pit and is removed from the board
     * @param player
     */
    private void fallIntoPit(@NotNull Player player){
        if(player.getSpace().getPit())
            player.setSpace(null);
    }
}
    //TODO: @GAB vis hvor mange checkpoints spiller har
    //lav afslutningsfase når spiller har vundet


