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
import java.util.Collections;
import java.util.List;

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
     * @param player the player being moved
     * @param heading the heading of the move
     * @param command the command being executed
     */
    public void executeCommand(@NotNull Player player, Heading heading, Command command) {
        if (player != null && player.board == board && command != null) {
            switch (command) {
                case FORWARD -> this.directionMove(player, heading);
                case RIGHT -> this.turnRight(player);
                case LEFT -> this.turnLeft(player);
                case FAST_FORWARD -> this.fastForward(player, heading);
                case MOVE_x3 -> this.tripleForward(player, heading);
                case U_TURN -> this.turnAround(player);
            }
        }
    }

    /**
     * Moves a player forward in a specific direction.
     * @param player the player being moved
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
                        //TODO: error handling?
                    }
                }
            }
        }
    }

    /**
     * Moves a player forward in a specific direction, and pushes, any player in the way, forward.
     * @param player the player being moved
     * @param space the space which the player is moving towards
     * @param heading the direction heading of the move
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
     * @param space the space checked
     * @param heading the direction checked
     * @return true if no wall(s) is blocking the path
     * @author Gabriel, Daniel, Sebastian
     */
    public boolean notWallsBlock(@NotNull Space space, Heading heading) {
        return (!isCurrentSpaceWallBlockingDirection(space, heading)
                && !isHeadingNeighbourWallBlockingDirection(space, heading));
    }

    /**
     *Returns true if a wall is blocking a player from moving on the players space.
     * @param space the space checked
     * @param heading the direction checked
     * @return true if a wall is blocking the path
     * @author Gabriel, Daniel, Sebastian
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
     * @param space the space checked
     * @param heading the direction checked
     * @return true if a wall is blocking the path
     * @author Gabriel, Daniel, Sebastian
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
     * @param player who is being moved
     * @param heading the direction of the move
     * @author Gabriel, Sebastian
     */
    public void fastForward(@NotNull Player player, @NotNull Heading heading) {
        directionMove(player, heading);
        directionMove(player, heading);
    }

    /**
     *
     * @param player who is being moved
     * @param heading the direction of the move
     * @author @Gabriel
     */
    public void tripleForward(@NotNull Player player, @NotNull Heading heading){
        directionMove(player, heading);
        directionMove(player, heading);
        directionMove(player, heading);
    }

    /**
     * Turns a player to the right.
     * @param player who is affected
     * @author Gabriel, Sebastian
     */
    public void turnRight(@NotNull Player player) {
        player.setHeading(player.getHeading().next());
    }

    /**
     * Turns a player to the left.
     * @param player who is affected
     * @author Gabriel, Sebastian
     */
    public void turnLeft(@NotNull Player player) {
        player.setHeading(player.getHeading().prev());
    }

    /**
     *
     * @param player who is affected
     */
    public void turnAround(@NotNull Player player) {
        player.setHeading(player.getHeading().oppositeHeading());
    }

    /**
     * Executes a option from the optional CommandCard chosen by the player on the GUI.
     * @param option list of different commands
     * @author Gabriel
     */
    public void executeCommandOptionAndContinue(@NotNull Command option) {
        Player currentPlayer = board.getCurrentPlayer();
        if (currentPlayer != null && Phase.PLAYER_INTERACTION == board.getPhase() && option != null) {
            board.setPhase(Phase.ACTIVATION);
            executeCommand(currentPlayer,currentPlayer.getHeading(), option);
            board.setStepMode(false);
            nextPlayerOrPhase();
        }
    }

    /**
     * Moves a players CommandCard to a different position in hand.
     * @param source the original position
     * @param target the new position
     * @return true if card is moved
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
                fireAllLasers(board.getLaserSpaceList(),board.getPlayers());
                respawnPlayers();
                sortPlayersAfterAntennaDistance();
                startProgrammingPhase();
            }
        }
    }

    /**
     * Executes all ActivatableBoardElements activateElement methods
     * @author Gabriel
     */
    public void executeBoardElements() {
        if (board.getPlayers() != null) {
            for (Player player : board.getPlayers()) {
                if(player.getSpace()!=null && !player.getSpace().getActivatableBoardElements().isEmpty()) {
                    for (ActivatableBoardElement activatableBoardElement : player.getSpace().getActivatableBoardElements()) {
                            activatableBoardElement.activateElement(player, this);
                        }
                    }
                }
            }
        }


    /**
     * Registers a players checkpoint in player, and calls findWinner() if player have gathered all checkpoints
     * @param player who landed on the checkpoint
     * @param checkpointNumber to compare with players next checkpoint number
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
     * Prints out the winning player
     * @param player who is is checked for winning
     * @author Gabriel
     */
    public void findWinner(@NotNull Player player) {
        if (player.getLastCheckpointVisited() == board.getNumberOfCheckpoints()) {
            player.setPlayerWin(true);
            System.out.println(player.getName() + " har vundet!!");
        }
    }

    /**
     * Updates every players reboot space if the player have left the startfield
     * @author Gabriel, Sebastian
     */
    public void updateAllReboot() {
        for (Player player : board.getPlayers()) {
            Space current = player.getSpace();
            if (current != null && current.x > current.board.rebootBorderX && current.x < current.board.getRebootBorderX2) {
                for (Space space : board.getRebootSpaceList()) {
                    if (!space.getReboot().isStartField() && space.x != 15) {
                        player.setRebootSpace(space);
                    }
                }
            }
            if (current != null && current.x > current.board.getRebootBorderX2) {
                for (Space space : board.getRebootSpaceList()) {
                    if (!space.getReboot().isStartField() && space.x != 4) {
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
     * @param player the "dead" player with space null, will be respawned on the reboot space
     * @author Gabriel, Sebastian
     */
    public void teleportPlayerToReboot(@NotNull Player player){
        if (player.getRebootSpace().getPlayer() != null){
            directionMove(player.getRebootSpace().getPlayer(), player.getRebootSpace().getReboot().REBOOT_HEADING);
        }
        player.setSpace(player.getRebootSpace());
        player.setHeading(player.getRebootSpace().getReboot().REBOOT_HEADING);
    }

    /**
     * Player falls into pit and is removed from the board
     * @param player who is affected
     */
    private void fallIntoPit(@NotNull Player player){
        if(player.getSpace().getPit())
            player.setSpace(null);
    }

    /**
     *
     * @param laserSpaces
     * @param players
     * @author @Gabriel
     */
    public void fireAllLasers(@NotNull List<Space> laserSpaces, List<Player> players) {
        if (!laserSpaces.isEmpty()) {
            for (Space space : laserSpaces) {
                fireLaser(space,space.getLaser().getShootingDirection());
            }
        }
        if(!players.isEmpty()){
            for (Player player : players) {
                if(player.getSpace()!=null) {
                    Space neighbourSpace = board.getNeighbour(player.getSpace(), player.getHeading());
                    if (neighbourSpace != null) fireLaser(neighbourSpace, player.getHeading());
                }
            }
        }
    }

    /**
     * @param projectile
     * @param shootingDirection
     * @author Tobias s205358
     */
    public void fireLaser(Space projectile, Heading shootingDirection) {
        if (projectile!=null) {
                boolean hit = false;
                do {
                    if (projectile == null) {
                        hit = true;
                    } else if (notWallsBlock(projectile, shootingDirection)) {
                        if (projectile.getPlayer() != null) {
                            Player player = projectile.getPlayer();
                            // Should be changed if players can take damage.
                            player.setSpace(null);
                            hit = true;
                        } else {
                            projectile = board.getNeighbour(projectile, shootingDirection);
                        }
                    } else {
                        hit = true;
                    }
                } while (!hit);
            }
    }


    /**
     * @author @Daniel
     */
    private void sortPlayersAfterAntennaDistance(){
        Space antennaSpace = null;
        for(Space space : board.getSpacesList()) {
            if (space.getIsAntenna())
                antennaSpace = space;
        }
        /*  trækker nuværende position fra koordinatset til antenne og tager positive værdi af dette.
            tættest på 0 er tættest på antenne
         */
        for(Player player : board.getPlayers()){
            player.setAntennaDistance(Math.abs(player.getSpace().x - antennaSpace.x) + Math.abs(player.getSpace().y - antennaSpace.y));
        }
        //sorts players after antenna distance
        Collections.sort(board.getPlayers());
    }

    public void registerEnergySpace (@NotNull Player player, boolean energyAvailable){
        if(energyAvailable || board.getStep() == 5) {
            player.addEnergy();
            energyAvailable=true;
        }
    }
}



