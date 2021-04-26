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

import dk.dtu.compute.se.pisd.roborally.dal.IRepository;
import dk.dtu.compute.se.pisd.roborally.dal.RepositoryAccess;
import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.model.ActivatableBoardElement;
import dk.dtu.compute.se.pisd.roborally.model.upgrade.Upgrade;
import dk.dtu.compute.se.pisd.roborally.model.upgrade.UpgradeResponsibility;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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
        respawnPlayers();
        updatePlayersAntennaDistance();
        board.setPhase(Phase.PROGRAMMING);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                player.getDamageCards().clear();
                for (Command damage: player.getSavedDamageCards()) {
                    player.getDamageCards().add(damage);
                }
                System.out.println(player);
                System.out.println(player.getSavedDamageCards());
                System.out.println(player.getDamageCards());
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    CommandCardField field = player.getProgramField(j);
                    field.setCard(null);
                    field.setVisible(true);
                }
                for (CommandCardField cardField:player.getCards()) {
                    cardField.setCard(generateRandomCommandCard(player));
                    cardField.setVisible(true);
                }
            }
        }
    }

    /**
     * If player has any damage cards, it will be added to the list of command cards.
     * A random card from the list of cards will be returned.
     * Whereof any damage card will be removed from the player only if returned. // moved to execute command
     *
     * @param player - the player who is handed the card
     * @return card - the command card
     *
     * @author Tobias, s205358@student.dtu.dk
     */
    private CommandCard generateRandomCommandCard(Player player) {
        int random;
        List<Command> commands = new ArrayList<>(Arrays.asList(
                Command.FORWARD,
                Command.RIGHT,
                Command.LEFT,
                Command.FAST_FORWARD,
                Command.MOVE_x3,
                Command.U_TURN,
                Command.OPTION_LEFT_RIGHT
        ));

        if (player != null) {
            if (!player.getDamageCards().isEmpty()) {
                for (Command damage : player.getDamageCards()) {
                    commands.add(damage);
                }
            }
        }

        random = (int) (Math.random() * commands.size());
        Command card = commands.get(random);

        if (player != null) {
            if (player.getDamageCards().contains(card)) player.getDamageCards().remove(card);
        }

        return new CommandCard(card);
    }

    /**
     * @return random CommandCard
     */
    /*
    private CommandCard generateRandomCommandCard() {
        int random = (int) (Math.random() * this.commands.size());
        return new CommandCard(commands.get(random));
    }
     */

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

    /**
     * Makes the CommandCardField specified by the parameter visible
     * @param register
     */
    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    /**
     * sets all CommandCardFields to invisible for all players
     */
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
        // this should not happen
        if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null) {
            int step = board.getStep();
            // this should not happen
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
                if(board.getPhase()!=Phase.PLAYER_INTERACTION) {
                    nextPlayerOrPhase();
                }
            } else return;
        } else {
            return;
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
            player.setFinalDestination(calculateDestination(player, heading, command));

            if (command == Command.SPAM || command == Command.TROJAN || command == Command.WORM || command == Command.VIRUS) {
                if (player.getSavedDamageCards().contains(command)) player.getSavedDamageCards().remove(command);
            }

            if (command == Command.FORWARD || command == Command.MOVE_x3 || command == Command.FAST_FORWARD){
                for (Upgrade u : player.getUpgrades()) {
                    if (u.responsible(UpgradeResponsibility.TELEPORT_PLAYER)) {
                        u.doAction(player, this);
                        return;
                    }
                }
            }
            switch (command) {
                case FORWARD -> this.directionMove(player, heading);
                case RIGHT -> this.turnRight(player);
                case LEFT -> this.turnLeft(player);
                case FAST_FORWARD -> this.fastForward(player, heading);
                case MOVE_x3 -> this.tripleForward(player, heading);
                case U_TURN -> this.turnAround(player);
                case SPAM -> this.doSpamDamage(player, heading);
                case TROJAN -> this.doTrojanDamage(player);
                case WORM -> this.doWormDamage(player);
                case VIRUS -> this.doVirusDamage(player);
            }
        }
    }

    /**
     *
     * @param player
     * @param heading
     *
     * @author Tobias, s205358@student.dtu.dk
     */
    private void doSpamDamage(Player player, Heading heading) {
        Command random = generateRandomCommandCard(null).command;
        executeCommand(player, heading, random);
    }

    /**
     * @param player
     *
     * @author Tobias, s205358@student.dtu.dk
     */
    private void doTrojanDamage(Player player) {
        for (int i = 0; i < 2; i++) {
            player.getSavedDamageCards().add(Command.SPAM);
        }
    }

    /**
     *
     * @param player
     *
     * @author Tobias, s205358@student.dtu.dk
     */
    private void doWormDamage(Player player) {
        this.teleportPlayerToReboot(player);
    }

    /**
     *
     * @param player
     *
     * @author Tobias, s205358@student.dtu.dk, @Gabriel
     */
    private void doVirusDamage(Player player) {
        Heading heading = player.getHeading();
        Space originalSpace = player.getSpace();
        Space nextSpace = null;
        Space virusSpace = null;
        for (int i = 0; i < Heading.values().length; i++) {
            heading=heading.next();
            for (int j = 0; j < 6; j++) {
                if(j==0) nextSpace=board.getNeighbour(originalSpace,heading);
                else nextSpace =board.getNeighbour(nextSpace,heading);

                for (int k = 0; k < 7; k++) {
                    if(k==0) virusSpace =nextSpace;
                    else virusSpace =board.getNeighbour(virusSpace,heading.prev());

                    if (virusSpace != null) {
                        if (virusSpace.getPlayer() != null) {
                            virusSpace.getPlayer().getSavedDamageCards().add(Command.VIRUS);
                            virusSpace.getPlayer().setColor("Pink");
                        }
                    }
                }
            }
        }
        doSpamDamage(player, player.getHeading());
    }

    /**
     * Moves a player forward in a specific direction.
     * @param player the player being moved
     * @author @Gabriel
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

                    for (Upgrade u : player.getUpgrades()) {
                        if (u.responsible(UpgradeResponsibility.PUSH_LEFT_OR_RIGHT)) {
                            board.setPhase(Phase.PLAYER_INTERACTION);
                            return;
                        }
                        else if(u.responsible(UpgradeResponsibility.MODULAR_CHASSIS) && other != null){
                            u.doAction(player, this);
                        }
                    }
                    dealPushDamage(player, other);
                    if(other.getSpace() != null) {
                        moveToSpace(other, target, heading);
                    }

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
     * @author @Gabriel, @Daniel, @Sebastian
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
     * @author @Gabriel, @Daniel, @Sebastian
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
     * @author @Gabriel, @Daniel, @Sebastian
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
     * @author @Gabriel, @Sebastian, @Daniel
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
     * @author @Gabriel, @Sebastian, @Daniel
     */
    public void turnRight(@NotNull Player player) {
        player.setHeading(player.getHeading().next());
    }

    /**
     * Turns a player to the left.
     * @param player who is affected
     * @author @Gabriel, @Sebastian, @Daniel
     */
    public void turnLeft(@NotNull Player player) {
        player.setHeading(player.getHeading().prev());
    }

    /**
     *
     * @param player who is affected
     * @author @Daniel
     */
    public void turnAround(@NotNull Player player) {
        player.setHeading(player.getHeading().oppositeHeading());
    }

    /**
     * Executes an option from the optional CommandCard chosen by the player on the GUI.
     * @param option list of different commands
     * @author @Gabriel
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
     * @author @Gabriel
     */
    private void nextPlayerOrPhase() {
        Player currentPlayer = board.getCurrentPlayer();
        int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
        int step = board.getStep();
        if (nextPlayerNumber < board.getPlayersNumber()) {
            board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
            continuePrograms();
        } else {
            updateAllReboot();
            executeBoardElements();
            updateAllReboot();
            step++;
            if (step < Player.NO_REGISTERS) {
                makeProgramFieldsVisible(step);
                board.setStep(step);
                board.setCurrentPlayer(board.getPlayer(0));
            } else {
                fireAllLasers(board.getLaserSpaceList(),board.getPlayers());

                //Should eventually be somewhere with upgrade phase logic
                for (Player p: board.getPlayers()) {
                    for (Upgrade u:p.getUpgrades()) {
                        if(u.responsible(UpgradeResponsibility.EXTRA_HAND_CARD)){
                            u.doAction(p,this);
                        }
                    }
                }
                startProgrammingPhase();
            }
        }
    }

    /**
     * Executes all ActivatableBoardElements activateElement methods
     * @author @Gabriel
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
     * @author @Gabriel
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
     * Prints out the winning player
     * @param player who is is checked for winning
     * @author @Gabriel
     */
    public void findWinner(@NotNull Player player) {
        if (player.getLastCheckpointVisited() == board.getNumberOfCheckpoints()) {
            player.setPlayerWin(true);
            board.setPhase(Phase.GAME_WON);
            if(board.getGameId()!=null) {
                IRepository repository = RepositoryAccess.getRepository();
                repository.deleteGameInDB(board);
            }
        }
    }

    /**
     * Updates every players reboot space if the player have left the startfield
     * @author @Gabriel, @Sebastian
     */
    public void updateAllReboot() {
        for (Player player : board.getPlayers()) {
            Space current = player.getSpace();
            if(current!=null) {
                for (Integer i: board.getRebootBorderXValues()) {
                  if(current.x>i && current.x<(i+10)){
                      for (Space rebootSpace:board.getRebootSpaceList()) {
                          if(rebootSpace.x>i && rebootSpace.x<(i+10) && !rebootSpace.getReboot().isStartField()){
                              player.setRebootSpace(rebootSpace);
                          }
                      }
                  }
                }
            }
        }
    }

    /**
     * Respawns all dead players
     * @author @Gabriel
     */
    public void respawnPlayers(){
        if(board.getStep()==4) {
            for (Player player : board.getPlayers()) {
                if (player.getSpace() == null) {
                    teleportPlayerToReboot(player);
                }
            }
        }
    }

    /**
     * Teleports players to reboot space and pushes players forward if multiple players is respawning on the same space
     * @param player the "dead" player with space null, will be respawned on the reboot space
     * @author Gabriel, Sebastian, Daniel
     */
    public void teleportPlayerToReboot(@NotNull Player player){
        if (player.getRebootSpace().getPlayer() != null){
            directionMove(player.getRebootSpace().getPlayer(), player.getRebootSpace().getReboot().REBOOT_HEADING);
        }
        if(player.getRebootSpace() != null) {
            player.setSpace(player.getRebootSpace());
            player.setHeading(player.getRebootSpace().getReboot().REBOOT_HEADING);
        }
        for (Upgrade u:player.getUpgrades()){
            if(u.responsible(UpgradeResponsibility.FIREWALL)) return;
        }
        player.getSavedDamageCards().add(Command.SPAM);
        player.getSavedDamageCards().add(Command.SPAM);
    }

    /**
     * Player falls into pit and is removed from the board
     * @param player who is affected
     * @author Daniel
     */
    private void fallIntoPit(@NotNull Player player){
        if(player != null) {
            for (Upgrade u:player.getUpgrades()) {
                if(u.responsible(UpgradeResponsibility.PIT_AVOIDER) && player.getSpace() != player.getFinalDestination()){
                    return;
                }

            }
            if (player.getSpace().getPit())
                player.setSpace(null);
        }
    }

    /**
     * fires all lasers and adjusting for laser upgrades
     * @param laserSpaces is the wall lasers being fired
     * @param players List of the players firing a laser
     * @author @Gabriel, @Daniel
     */
    public void fireAllLasers(@NotNull List<Space> laserSpaces, List<Player> players) {
        boolean hasRailGun = false;
        if (board.isLasersActive()) {

            if (!laserSpaces.isEmpty()) {
                for (Space space : laserSpaces) {
                    fireLaser(space, space.getLaser().getShootingDirection());
                }
            }
            if (!players.isEmpty()) {
                for (Player player : players) {
                    for (Upgrade u : player.getUpgrades()) {
                        if (u.responsible(UpgradeResponsibility.RAIL_GUN)) {
                            u.doAction(player, this);
                            hasRailGun = true;
                        }
                        if (u.responsible(UpgradeResponsibility.REAR_LASER)) {
                            u.doAction(player, this);
                        }
                    }
                    if(hasRailGun)
                        break;
                    if (player.getSpace() != null && notWallsBlock(player.getSpace(), player.getHeading())) {
                        Space neighbourSpace = board.getNeighbour(player.getSpace(), player.getHeading());
                        if (neighbourSpace != null) {
                            fireLaser(neighbourSpace, player.getHeading());
                        }
                    }
                }
            }
        }
    }

    /**
     * swap card when pushing this player if the pushing player har modular chassis upgrade.
     * @author Daniel
     * @param player is the pushing player with modular chassis upgrad
     * @param pushedPlayer is the player swapping a random card for the pushing players' modular chassis upgrade
     */
    public void stealUpgradeCard(@NotNull Player player, @NotNull Player pushedPlayer){
        if(pushedPlayer != null && !pushedPlayer.getUpgrades().isEmpty()){
            for(Upgrade u : player.getUpgrades()){
                if(u.responsible(UpgradeResponsibility.MODULAR_CHASSIS)) {
                    player.getUpgrades().remove(u);
                    pushedPlayer.getUpgrades().add(u);

                    int randomUpgradeNumber = (int)Math.random()*pushedPlayer.getUpgrades().size();

                    player.getUpgrades().add(pushedPlayer.getUpgrades().get(randomUpgradeNumber));
                    pushedPlayer.getUpgrades().remove(randomUpgradeNumber);
                }
            }


        }
    }

    /**
     * fires a single laser and checks for laser upgrades and adjusts accordingly
     * @param projectile the space of the projectile
     * @param shootingDirection is the direction the laser shoots
     * @author Tobias s205358, @Daniel
     */
    public void fireLaser(Space projectile, @NotNull Heading shootingDirection) {
        Player shootingPlayer = null;
        Space neighbour = board.getNeighbour(projectile, shootingDirection.oppositeHeading());
        if (neighbour != null) {
            if (neighbour.getLaser() == null) {
                shootingPlayer = board.getNeighbour(projectile, shootingDirection.oppositeHeading()).getPlayer();
            }
        }

        if (projectile != null) {
            boolean hit = false;
                do {
                    if (projectile == null) {
                        hit = true;
                    } else if (notWallsBlock(projectile, shootingDirection)) {
                        if (projectile.getPlayer() != null) {
                            Player player = projectile.getPlayer();
                            // Should be changed if players can take damage.
                            if(shootingPlayer != null){
                                boolean pressorBeam = false;
                                boolean tractorBeam = false;
                                dealLaserDamage(player, shootingPlayer);
                                for (Upgrade u : shootingPlayer.getUpgrades()){
                                    if(u.responsible(UpgradeResponsibility.PRESSOR_BEAM)) {
                                        directionMove(player, shootingDirection);
                                        projectile = board.getNeighbour(projectile, shootingDirection);
                                        pressorBeam = true;
                                    }
                                    if(u.responsible(UpgradeResponsibility.TRACTOR_BEAM)) {
                                        if(shootingPlayer.board.getNeighbour(shootingPlayer.getSpace(),shootingPlayer.getHeading()) != player.getSpace()) {
                                            directionMove(player, shootingDirection.oppositeHeading());
                                            tractorBeam = true;
                                        }
                                    }
                                }
                                if (tractorBeam && pressorBeam){
                                    int random = (int)(Math.random()*2);
                                    if(random == 0)
                                        directionMove(player, shootingDirection);
                                    else
                                        directionMove(player, shootingDirection.oppositeHeading());
                                }
                            }
                            else{
                                player.getSavedDamageCards().add(spamOrRandom());
                            }
                            hit = true;
                        } else {
                            projectile = board.getNeighbour(projectile, shootingDirection);
                        }
                    } else
                        hit = true;
                } while (!hit);
            }
        }

    /**
     * compares all players' distance to the antenna and orders them in a list from closest to the
     * antenna to furthest away from the antenna.
     * @author @Daniel
     */
    public void updatePlayersAntennaDistance(){
        Space antennaSpace = null;
        for(Space space : this.board.getSpacesList()) {
            if (space.getIsAntenna() && space != null)
                antennaSpace = space;
        }
        /*  trækker nuværende position fra koordinatset til antenne og tager positive værdi af dette.
            tættest på 0 er tættest på antenne
         */
        if(antennaSpace != null) {
            for (Player player : board.getPlayers()) {
                player.setAntennaDistance((Math.abs(player.getSpace().x - antennaSpace.x)) + (Math.abs(player.getSpace().y - antennaSpace.y)));

            }
            //sorts players after antenna distance
            board.sortPlayersAntennaDistance();
        }
    }

    /**
     * Sets off the push panel, and checks if a player the PushPanelDogdger upgrade in which case the player is not pushed
     * @param player is the player being pushed
     * @param heading is the heading of the pushpanel
     * @param activatingTurns is the steps in which the pushpanel activates
     * @author @Gabriel @Sebastian
     */
    public void activatePushPanel(Player player, Heading heading, List<Integer> activatingTurns) {
        if(!activatingTurns.isEmpty() && player!=null && player.getSpace()!=null){
            if(activatingTurns.contains(board.getStep())){
                for(Upgrade u : player.getUpgrades()){
                    if(u.responsible(UpgradeResponsibility.PUSH_PANEL_DODGER)){
                        return;
                    }
                }
            directionMove(player,heading);
            }
        }
    }
    /**
     * Gives players energy
     * @author Sebastian
     * @param player is the player being given energy
     * @param energyAvailable dertermines whether the player should get energy from the energySpace
     * @param energySpace is the space that rewards palyers with energy
     */
    public void registerEnergySpace(@NotNull Player player, boolean energyAvailable, EnergySpace energySpace){
        if (player != null) {
            if(energyAvailable && board.getStep() == 4){
               givePlayerRandomUpgrade(player,energySpace);
                givePlayerRandomUpgrade(player,energySpace);
            }
            else if (energyAvailable || board.getStep() == 4) {
                givePlayerRandomUpgrade(player,energySpace);
            }
        }
    }
    /**
     * Moves a player on a conveyor belt
     * @param player is the player being moved
     * @param heading is the direction the player is moved
     * @param command determines how many spaces the players is moved
     * @author @Gabriel
     */
    public void conveyorMove(Player player, Heading heading, Command command) {
        Space target = player.board.getNeighbour(player.getSpace(), heading);
        if(target == null && notWallsBlock(player.getSpace(),heading)){
            player.setSpace(null);
        }else {
            executeCommand(player,heading,command);
        }
    }

    /**
     * Fires rail gun laser which is unaffected by walls
     * @author @Daniel
     * @param projectile is the space in which the players laser begins
     * @param shootingDirection is the direction the laser is moving
     */
    public void fireRailGun(Space projectile, Heading shootingDirection) {
        boolean hit = false;
        Player shootingPlayer = null;
        Space neighbour = board.getNeighbour(projectile, shootingDirection.oppositeHeading());
        if (neighbour != null) {
            if (neighbour.getLaser() == null) {
                shootingPlayer = board.getNeighbour(projectile, shootingDirection.oppositeHeading()).getPlayer();
            }
            if (shootingPlayer != null) {
                while (!hit) {
                    if (projectile.getPlayer() != null) {
                        Player player = projectile.getPlayer();
                        // Should be changed if players can take damage.
                        dealLaserDamage(player, shootingPlayer);
                        boolean pressorBeam = false;
                        boolean tractorBeam = false;
                        for (Upgrade u : shootingPlayer.getUpgrades()){
                            if(u.responsible(UpgradeResponsibility.PRESSOR_BEAM)) {
                                directionMove(player, shootingDirection);
                                projectile = board.getNeighbour(projectile, shootingDirection);
                                pressorBeam = true;
                            }
                            if(u.responsible(UpgradeResponsibility.TRACTOR_BEAM)) {
                                if(shootingPlayer.board.getNeighbour(shootingPlayer.getSpace(),shootingPlayer.getHeading()) != player.getSpace()) {
                                    directionMove(player, shootingDirection.oppositeHeading());
                                    tractorBeam = true;
                                }
                            }
                        }
                        if (tractorBeam && pressorBeam){
                           Random rand = new Random();
                           int int_random = rand.nextInt(2);
                           if(int_random == 0)
                               directionMove(player, shootingDirection);
                           else
                               directionMove(player, shootingDirection.oppositeHeading());
                        }
                    }
                    projectile = board.getNeighbour(projectile, shootingDirection);

                    if (projectile == null)
                        hit = true;
                }
            }
        }
    }

    /**
     * This method calculates and returns the your ending position before you move
     * @author Sebastian
     * @param player
     * @param command
     * @return
     */
    public Space calculateDestination(Player player, Heading heading, Command command){
        Space move = board.getNeighbour(player.getSpace(),heading);
        Space movex2 = board.getNeighbour(move,heading);
        Space movex3 = board.getNeighbour(movex2,heading);
        Space finalmove = player.getSpace();
        switch (command) {
            case FORWARD -> finalmove=move;
            case FAST_FORWARD -> finalmove=movex2;
            case MOVE_x3 -> finalmove=movex3;
        };
        return  finalmove;
    }

    /**
     * Gives a player a random upgrade
     * @param player the player recieving upgrade
     * @param energySpace the space on which the upgrade is given
     */
    public void givePlayerRandomUpgrade(@NotNull Player player, EnergySpace energySpace){
        player.addEnergy();
        energySpace.setEnergyAvailable(false);
        player.getSpace().playerChanged();

        //TODO: @Gab, midlertidigt her
        if(board.getGameName()==null) {
            boolean b = false;
            int r = 0;
            do {
                Upgrade u = board.upgrades.get(UpgradeResponsibility.getRandom());
                if (!player.getUpgrades().contains(u) && player.getUpgrades().size() != UpgradeResponsibility.values().length) {
                    player.getUpgrades().add(u);
                    b = true;
                }
            } while (false);
        }
    }

    /**
     * Deals pushing damageCard depending on which upgrades the pushing player has
     * @author Daniel
     * @param player
     * @param pushedPlayer
     */
    public void dealPushDamage(Player player, Player pushedPlayer){
        for(Upgrade u : player.getUpgrades()){
            if(u.responsible(UpgradeResponsibility.BLUE_SCREEN_DEATH))
                pushedPlayer.getSavedDamageCards().add(Command.WORM);
            else if(u.responsible(UpgradeResponsibility.TROJAN_NEEDLER))
                pushedPlayer.getSavedDamageCards().add(Command.TROJAN);
            else if(u.responsible(UpgradeResponsibility.VIRUS_MODULE))
                pushedPlayer.getSavedDamageCards().add(Command.VIRUS);
            else if(u.responsible((UpgradeResponsibility.RAMMING_GEAR))){
                pushedPlayer.getSavedDamageCards().add(spamOrRandom());
            }

        }
    }

    /**
     * Returns a random damage card of Worm, Virus, Trojan 9% of the time. Else returns Spam.
     * @return
     * @author @Gabriel
     */
    public Command spamOrRandom(){
        int random = (int)(Math.random()*100);
        if(random>9) {
            return Command.SPAM;
        }else {
            random = (int)(Math.random()*100);
            if(random<=33) return (Command.WORM);
            else if(random<=66) return (Command.TROJAN);
            else return (Command.VIRUS);
        }
    }

    /**
     * A method used in all laser-related methods for doing damage, based on the players upgrade cards
     *
     * @author Sebastian
     * @param player
     */
    public void dealLaserDamage(Player player, Player shooter) {
        boolean loopAgain = false;
        int loopCount = 0;
        do {
            boolean upgradeUsed = false;
            for (Upgrade u : player.getUpgrades()) {
                if (u.responsible(UpgradeResponsibility.LUCKY_SHIELD)) {
                    return;
                }
            }
            for (Upgrade u : shooter.getUpgrades()) {
                if (u.responsible(UpgradeResponsibility.TROJAN_NEEDLER)) {
                    player.getSavedDamageCards().add(Command.TROJAN);
                    upgradeUsed = true;
                }
                else if (u.responsible(UpgradeResponsibility.BLUE_SCREEN_DEATH)) {
                    player.getSavedDamageCards().add(Command.WORM);
                    upgradeUsed = true;
                }
                else if (u.responsible(UpgradeResponsibility.DOUBLE_BARREL_LASER)) loopAgain = true;
            }
            if (!upgradeUsed) {
                player.getSavedDamageCards().add(spamOrRandom());
            }
            loopCount++;
        }while(loopCount<2 && loopAgain);
    }
}



