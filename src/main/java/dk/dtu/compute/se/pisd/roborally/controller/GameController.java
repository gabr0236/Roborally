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
     * This is just some dummy controller operation to make a simple move to see something
     * happening on the board. This method should eventually be deleted!
     *
     * @param space the space to which the current player should move
     */

    public void moveCurrentPlayerToSpace(@NotNull Space space) {
    }

    /**
     * Ændre spillet til programmeringsfasen og itererer igennem for hver spiller og laver commandcard field og command cards
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
     * vælger et tilfældigt command ud af commandarray
     *
     * @return et commandcard med den tilfældige command
     */
    // XXX: V2
    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    /**
     * stopper programmeringsfasen gøre de lagte kort usynlige og ændre fase til activation phase
     */
    // XXX: V2
    public void finishProgrammingPhase() {
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        board.setPhase(Phase.ACTIVATION);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

    }

    // XXX: V2
    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    // XXX: V2
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
     * sætter stepMode til false og kører continuePrograms()
     */
    // XXX: V2
    public void executePrograms() {
        board.setStepMode(false);
        continuePrograms();
    }

    /**
     * sætter stepMode til true og kører continuePrograms()
     */
    // XXX: V2
    public void executeStep() {
        board.setStepMode(true);
        continuePrograms();
    }

    /**
     * kører metoden executeNextStep() mens spillet er activation phase og stepMode er false
     */
    // XXX: V2
    private void continuePrograms() {
        do {
            executeNextStep();
        } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());
    }

    /**
     * hvis det næste kort er et interaktionskort og spillet er i activation phase ændres fasen til interaktion og returnere.
     * er kortet ikke interaktionskort bruges metoden executeCommand() på spilleren.
     * sætter efterfølgende currentPlayer til den næste spiller. Hvis det ikke er den sidste spiller gøres kortene usynlige.
     * til sidst startes progrmmeringsfasen.
     */
    // XXX: V2
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

    // XXX: V2
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
     * Rykker spiller et felt frem i en specifik retning
     *
     * @param player
     */
    public void directionMove(@NotNull Player player, @NotNull Heading heading) {
        Space current = player.getSpace();
        if (current != null && player.board == current.board) {
            Space target = current.board.getNeighbour(current, heading);
            if (target != null) {
                if (isWallBlock(player, heading)) {
                    try{
                        moveToSpace(player, target, heading);
                    } catch (ImpossibleMoveException e){

                    }
                }
            }

        }
    }


    void moveToSpace(@NotNull Player player, @NotNull Space space, @NotNull Heading heading) throws ImpossibleMoveException {
        assert board.getNeighbour(player.getSpace(), heading) == space; // make sure the move to here is possible in principle
        Player other = space.getPlayer();
        Space target = board.getNeighbour(space, heading);
            if (other != null) {

                if (target != null) {
                    if (isWallBlock(other, heading)) {
                        // XXX Note that there might be additional problems with
                        //     infinite recursion here (in some special cases)!
                        //     We will come back to that!

                        moveToSpace(other, target, heading);

                        // Note that we do NOT embed the above statement in a try catch block, since
                        // the thrown exception is supposed to be passed on to the caller

                        //assert target.getPlayer() == null : target; // make sure target is free now
                    } else {
                        throw new ImpossibleMoveException(player, space, heading);
                    }
                }
            }
            player.setSpace(space);
        }

    private boolean isWallBlock(@NotNull Player player, Heading heading) {
        return (!isCurrentSpaceWallBlockingDirection(player, heading)
                && !isHeadingNeighbourWallBlockingDirection(player, heading));
    }

    public boolean isCurrentSpaceWallBlockingDirection(@NotNull Player player, Heading heading) {
        Walls tempWalls = player.getSpace().getWalls();
        if (tempWalls != null && player.getSpace() != null) {
            return tempWalls.getBlockingDirection().contains(heading);
        }
        return false;
    }

    public boolean isHeadingNeighbourWallBlockingDirection(@NotNull Player player, Heading heading) {
        Space neighbour = player.board.getNeighbour(player.getSpace(), heading);
        if (neighbour != null && neighbour.getWalls() != null) {
            Heading oppositeHeading = heading.oppositeHeading();
            return neighbour.getWalls().getBlockingDirection().contains(oppositeHeading);
        }
        return false;
    }


    /**
     * Rykker spiller to felter frem i den retning spilleren vender
     *
     * @param player
     */
    public void fastForward(@NotNull Player player, @NotNull Heading heading) {
        directionMove(player, heading);
        directionMove(player, heading);
    }

    /**
     * vender spillerens retning mod højre
     *
     * @param player
     */
    public void turnRight(@NotNull Player player) {
        player.setHeading(player.getHeading().next());

    }

    /**
     * vender spillerens retning mod venstre
     *
     * @param player
     */
    public void turnLeft(@NotNull Player player) {
        player.setHeading(player.getHeading().prev());
    }

    /**
     * Hvis spillet er i interaktionsfasen kaldes metoden executeCommand() og ændre spillets fase til aktieringsfasen
     * sætter efterfølgende currentPlayer til den næste spiller. Hvis det ikke er den sidste spiller gøres kortene usynlige.
     * til sidst startes programmeringsfasen.
     *
     * @param option er en af de muligheder som kortet tillader
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
     * hvis spilleren har et kort på hånden og spiller dette kort på et field der ikke allereder har et kort
     * byttes værdierne af disse kort. hermed bliver tagetCard = sourceCard og sourcecard = null.
     *
     * @param source er det commandCardField som spilleren har på hånden
     * @param target er det commandCardField hvor spilleren kan spille sine kort.
     * @return hvis de to fields bytter kortværdier returneres true og ellers false
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

    public void executeBoardElements() {
        if (board.getSpacesList() != null) {
            for (Space space : board.getSpacesList()) {
                Player player = space.getPlayer();
                if (player != null && space.getActivatableBoardElement() != null) {
                    space.getActivatableBoardElement().activateElement(space.getPlayer(), this);
                }
            }
        }
    }

    public void registerCheckpoint(@NotNull Player player, int checkpointNumber) {
        if (player != null) {
            if (checkpointNumber == player.getLastCheckpointVisited() + 1) {
                player.setLastCheckpointVisited(checkpointNumber);
                findWinner(player);
            }
        }
    }

    public void findWinner(@NotNull Player player) {
        if (player.getLastCheckpointVisited() == Checkpoint.getNumberOfCheckpoints()) {
            player.setPlayerWin(true);
            System.out.println(player.getColor() + " har vundet!!");
        }
    }

    private void updateAllReboot(){
        for (Player player :board.getPlayers()) {
            updatePlayerRebootSpace(player);
        }
    }
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

    public void respawnPlayers(){
        for (Player player:board.getPlayers()) {
            if(player.getSpace()==null){
                teleportPlayerToReboot(player);
            }
        }
    }

    private void teleportPlayerToReboot(@NotNull Player player){
        player.setSpace(player.getRebootSpace());
        player.setHeading(player.getRebootSpace().getReboot().REBOOT_HEADING);
    }
}
    //TODO: vis hvor mange checkpoints spiller har
    //lav afslutningsfase når spiller har vundet


