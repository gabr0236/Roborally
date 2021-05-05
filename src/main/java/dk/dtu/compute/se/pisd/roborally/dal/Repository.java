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
package dk.dtu.compute.se.pisd.roborally.dal;

import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.model.upgrade.CreateUpgrade;
import dk.dtu.compute.se.pisd.roborally.model.upgrade.Upgrade;
import dk.dtu.compute.se.pisd.roborally.model.upgrade.UpgradeResponsibility;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
class Repository implements IRepository {

	private static final String GAME_GAMEID = "gameID";

	private static final String GAME_NAME = "name";

	private static final String GAME_CURRENTPLAYER = "currentPlayer";

	private static final String GAME_PHASE = "phase";

	private static final String GAME_STEP = "step";

	private static final String GAME_BOARD = "board";

	private static final String PLAYER_PLAYERID = "playerID";

	private static final String PLAYER_NAME = "name";

	private static final String PLAYER_COLOUR = "colour";

	private static final String PLAYER_GAMEID = "gameID";

	private static final String PLAYER_POSITION_X = "positionX";

	private static final String PLAYER_POSITION_Y = "positionY";

	private static final String PLAYER_HEADING = "heading";

	private static final String PLAYER_REBOOT_POSITION_X = "rebootPosX";

	private static final String PLAYER_REBOOT_POSITION_Y = "rebootPosY";

	private static final String CARDS_GAMEID = "gameID";

	private static final String CARDS_PLAYERID = "playerID";

	private static final String CARDS_POSITION = "position";

	private static final String CARDS_COMMAND_HAND = "commandHand";

	private static final String CARDS_COMMAND_REGISTER = "commandRegister";

	private static final String UPGRADE_GAMEID = "gameID";

	private static final String UPGRADE_PLAYERID = "playerID";

	private static final String UPGRADE = "upgrade";


	private Connector connector;

	Repository(Connector connector) {
		this.connector = connector;
	}

	/**
	 * creates data in DB corresponding to the current game when starting a new game
	 *
	 * @param game which is created in database (Board)
	 * @return true if game is created successfully in database
	 */
	@Override
	public boolean createGameInDB(Board game) {
		if (game.getGameId() == null) {
			Connection connection = connector.getConnection();
			try {
				connection.setAutoCommit(false);

				PreparedStatement ps = getInsertGameStatementRGK();
				// TODO: the name should eventually set by the user
				//       for the game and should be then used 
				//       game.getName();
				ps.setString(1, game.getGameName()); // instead of name
				ps.setNull(2, Types.TINYINT); // game.getPlayerNumber(game.getCurrentPlayer())); is inserted after players!
				ps.setInt(3, game.getPhase().ordinal());
				ps.setInt(4, game.getStep());
				ps.setString(5, game.boardName);

				// If you have a foreign key constraint for current players,
				// the check would need to be temporarily disabled, since
				// MySQL does not have a per transaction validation, but
				// validates on a per row basis.
				Statement statement = connection.createStatement();
				statement.execute("SET foreign_key_checks = 0");

				int affectedRows = ps.executeUpdate();
				ResultSet generatedKeys = ps.getGeneratedKeys();
				if (affectedRows == 1 && generatedKeys.next()) {
					game.setGameId(generatedKeys.getInt(1));
				}
				generatedKeys.close();

				// Enable foreign key constraint check again:
				statement.execute("SET foreign_key_checks = 1");
				statement.close();

				createPlayersInDB(game);
				createCardsInDB(game);
				createUpgradesInDB(game);


				// since current player is a foreign key, it can oly be
				// inserted after the players are created, since MySQL does
				// not have a per transaction validation, but validates on
				// a per row basis.
				ps = getSelectGameStatementU();
				ps.setInt(1, game.getGameId());

				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					rs.updateInt(GAME_CURRENTPLAYER, game.getPlayerNumber(game.getCurrentPlayer()));
					rs.updateRow();
				} else {
					// TODO error handling
				}
				rs.close();

				connection.commit();
				connection.setAutoCommit(true);
				return true;
			} catch (SQLException e) {
				// TODO error handling
				e.printStackTrace();
				System.err.println("Some DB error");

				try {
					connection.rollback();
					connection.setAutoCommit(true);
				} catch (SQLException e1) {
					// TODO error handling
					e1.printStackTrace();
				}
			}
		} else {
			System.err.println("Game cannot be created in DB, since it has a game id already!");
		}
		return false;
	}

	/**
	 * Updates game table in database
	 *
	 * @param game which is being updated in database
	 * @return true if the game was successfully updated
	 */
	@Override
	public boolean updateGameInDB(Board game) {
		assert game.getGameId() != null;

		Connection connection = connector.getConnection();
		try {
			connection.setAutoCommit(false);

			PreparedStatement ps = getSelectGameStatementU();
			ps.setInt(1, game.getGameId());

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				rs.updateInt(GAME_CURRENTPLAYER, game.getPlayerNumber(game.getCurrentPlayer()));
				rs.updateInt(GAME_PHASE, game.getPhase().ordinal());
				rs.updateInt(GAME_STEP, game.getStep());
				rs.updateRow();
			} else {
				// TODO error handling
			}
			rs.close();

			updatePlayersInDB(game);
			updateCardsInDB(game);
			deletePlayerUpgradesInDB(game);
			createUpgradesInDB(game);


			connection.commit();
			connection.setAutoCommit(true);
			return true;
		} catch (SQLException e) {
			// TODO error handling
			e.printStackTrace();
			System.err.println("Some DB error");

			try {
				connection.rollback();
				connection.setAutoCommit(true);
			} catch (SQLException e1) {
				// TODO error handling
				e1.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * Loads a game from the database
	 *
	 * @param id the game id
	 * @return board loaded from database
	 */
	@Override
	public Board loadGameFromDB(int id) {
		Board game;
		try {
			// TODO here, we could actually use a simpler statement
			//      which is not updatable, but reuse the one from
			//      above for the pupose
			PreparedStatement ps = getSelectGameStatementU();
			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();
			int playerNo = -1;
			if (rs.next()) {
				// TODO and we should also store the used game board in the database
				//      for now, we use the default game board
				String board = rs.getString(GAME_BOARD);
				game = LoadBoard.loadBoard(board);
				if (game == null) {
					return null;
				}
				playerNo = rs.getInt(GAME_CURRENTPLAYER);
				// TODO currently we do not set the games name (needs to be added)
				game.setPhase(Phase.values()[rs.getInt(GAME_PHASE)]);
				game.setStep(rs.getInt(GAME_STEP));
			} else {
				// TODO error handling
				return null;
			}
			rs.close();

			game.setGameId(id);
			loadPlayersFromDB(game);

			if (playerNo >= 0 && playerNo < game.getPlayersNumber()) {
				game.setCurrentPlayer(game.getPlayer(playerNo));
			} else {
				// TODO  error handling
				return null;
			}
			loadCardFieldsFromDB(game);
			loadUpgradesFromDB(game);


			return game;
		} catch (SQLException e) {
			// TODO error handling
			e.printStackTrace();
			System.err.println("Some DB error");
		}
		return null;
	}


	/**
	 * Gets all game names from database, used for choosing what game to load
	 *
	 * @return list of game names
	 */
	@Override
	public List<GameInDB> getGames() {
		// TODO when there many games in the DB, fetching all available games
		//      from the DB is a bit extreme; eventually there should a
		//      methods that can filter the returned games in order to
		//      reduce the number of the returned games.
		List<GameInDB> result = new ArrayList<>();
		try {
			PreparedStatement ps = getSelectGameIdsStatement();
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(GAME_GAMEID);
				String name = rs.getString(GAME_NAME);
				result.add(new GameInDB(id, name));
			}
			rs.close();
		} catch (SQLException e) {
			// TODO proper error handling
			e.printStackTrace();
		}

		return result.isEmpty() ? null : result;
	}

	/**
	 * creates data corresponding to the current games players
	 *
	 * @param game used to get information and send to database
	 * @throws SQLException
	 */
	private void createPlayersInDB(Board game) throws SQLException {
		// TODO code should be more defensive
		PreparedStatement ps = getSelectPlayersStatementU();
		ps.setInt(1, game.getGameId());

		ResultSet rs = ps.executeQuery();
		for (int i = 0; i < game.getPlayersNumber(); i++) {
			Player player = game.getPlayer(i);
			rs.moveToInsertRow();
			rs.updateInt(PLAYER_GAMEID, game.getGameId());
			rs.updateInt(PLAYER_PLAYERID, i);
			rs.updateString(PLAYER_NAME, player.getName());
			rs.updateString(PLAYER_COLOUR, player.getColor());
			rs.updateInt(PLAYER_POSITION_X, player.getSpace().x);
			rs.updateInt(PLAYER_POSITION_Y, player.getSpace().y);
			rs.updateInt(PLAYER_HEADING, player.getHeading().ordinal());
			rs.updateInt(PLAYER_REBOOT_POSITION_X, player.getRebootSpace().x);
			rs.updateInt(PLAYER_REBOOT_POSITION_Y, player.getRebootSpace().y);
			rs.insertRow();
		}
		rs.close();
	}

	/**
	 * creates data corresponding to the current game players cards
	 * when starting a new game
	 *
	 * @param game used to get information and send to database
	 * @throws SQLException
	 * @author Gabriel
	 */
	private void createCardsInDB(Board game) throws SQLException {
		// TODO code should be more defensive
		PreparedStatement ps = getSelectCardsStatementU();
		ps.setInt(1, game.getGameId());

		ResultSet rs = ps.executeQuery();
		for (int i = 0; i < game.getPlayersNumber(); i++) {
			Player player = game.getPlayer(i);
			for (int j = 0; j < player.getNumberOfCards(); j++) {
				rs.moveToInsertRow();
				rs.updateInt(CARDS_GAMEID, game.getGameId());
				rs.updateInt(CARDS_PLAYERID, i);
				rs.updateInt(CARDS_POSITION, j);
				rs.updateInt(CARDS_COMMAND_HAND, player.getCards().get(j).getCard().command.ordinal());
				if (j < Player.NO_REGISTERS && player.getProgramField(j).getCard() != null) {
					rs.updateInt(CARDS_COMMAND_REGISTER, player.getProgramField(j).getCard().command.ordinal());
				}
				rs.insertRow();
			}
		}
		rs.close();
	}

	/**
	 * creates data corresponding to the current game players upgrades
	 * when starting a new game
	 *
	 * @param game used to get information and send to database
	 * @throws SQLException
	 * @author Gabriel
	 */
	private void createUpgradesInDB(Board game) throws SQLException {
		// TODO code should be more defensive
		PreparedStatement ps = getSelectUpgradesStatementU();
		ps.setInt(1, game.getGameId());

		ResultSet rs = ps.executeQuery();
		for (int i = 0; i < game.getPlayersNumber(); i++) {
			Player player = game.getPlayer(i);
			for (Upgrade u:player.getUpgrades()) {
				rs.moveToInsertRow();
				rs.updateInt(UPGRADE_GAMEID, game.getGameId());
				rs.updateInt(UPGRADE_PLAYERID, i);
				rs.updateInt(UPGRADE, u.getUpgradeResponsibility().ordinal());
				rs.insertRow();
			}
		}
		rs.close();
	}

	/**
	 * loads player data corresponding to the current game id
	 *
	 * @param game used to get the game id
	 * @throws SQLException
	 */
	private void loadPlayersFromDB(Board game) throws SQLException {
		PreparedStatement ps = getSelectPlayersASCStatement();
		ps.setInt(1, game.getGameId());

		ResultSet rs = ps.executeQuery();
		int i = 0;
		while (rs.next()) {
			int playerId = rs.getInt(PLAYER_PLAYERID);
			if (i++ == playerId) {
				// TODO this should be more defensive
				String name = rs.getString(PLAYER_NAME);
				String colour = rs.getString(PLAYER_COLOUR);
				Player player = new Player(game, colour, name);
				game.addPlayer(player);

				int x = rs.getInt(PLAYER_POSITION_X);
				int y = rs.getInt(PLAYER_POSITION_Y);
				if (!rs.wasNull()) {
					if (x >= 0 || y >= 0) {
						player.setSpace(game.getSpace(x, y));
					} else {
						player.setSpace(null);
					}
				} else {
					player.setSpace(null);
				}
				int rebootPosX = rs.getInt(PLAYER_REBOOT_POSITION_X);
				int rebootPosY = rs.getInt(PLAYER_REBOOT_POSITION_Y);
				player.setRebootSpace(game.getSpace(rebootPosX, rebootPosY));

				int heading = rs.getInt(PLAYER_HEADING);
				player.setHeading(Heading.values()[heading]);
			} else {
				// TODO error handling
				System.err.println("Game in DB does not have a player with id " + i + "!");
			}
		}
		rs.close();
	}

	/**
	 * loads cardField data corresponding to the current game id
	 *
	 * @param game used to get the game id
	 * @author Gabriel
	 */
	private void loadCardFieldsFromDB(Board game) throws SQLException {
		PreparedStatement ps = getSelectCardsASCStatement();
		ps.setInt(1, game.getGameId());

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			int playerId = rs.getInt(CARDS_PLAYERID);
			int position = rs.getInt(CARDS_POSITION);
			Player player = game.getPlayer(playerId);

			int cardHand = rs.getInt(CARDS_COMMAND_HAND);
			if (!rs.wasNull()) {
				player.getCards().get(position).setCard(new CommandCard(Command.values()[cardHand]));
			}

			int cardRegister = rs.getInt(CARDS_COMMAND_REGISTER);
			if (!rs.wasNull()) {
				player.getProgramField(position).setCard(new CommandCard(Command.values()[cardRegister]));
			}
		}
		rs.close();
	}

	/**
	 * loads Upgrade data corresponding to the current game id
	 *
	 * @param game used to get the game id
	 * @author Gabriel
	 */
	private void loadUpgradesFromDB(Board game) throws SQLException {
		PreparedStatement ps = getSelectUpgradesASCStatement();
		ps.setInt(1, game.getGameId());

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			int playerId = rs.getInt(UPGRADE_PLAYERID);
			Player player = game.getPlayer(playerId);

			int upgrade = rs.getInt(UPGRADE);

			if (!rs.wasNull()) {
				player.getUpgrades().add(CreateUpgrade.getUpgrade(UpgradeResponsibility.values()[upgrade]));
			}
		}
		rs.close();
	}

	/**
	 * updates player data corresponding to the current game id
	 *
	 * @param game used to get the current game id
	 * @throws SQLException
	 */
	private void updatePlayersInDB(Board game) throws SQLException {
		PreparedStatement ps = getSelectPlayersStatementU();
		ps.setInt(1, game.getGameId());

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			int playerId = rs.getInt(PLAYER_PLAYERID);
			// TODO should be more defensive
			Player player = game.getPlayer(playerId);
			if (player.getSpace() != null) {
				rs.updateInt(PLAYER_POSITION_X, player.getSpace().x);
				rs.updateInt(PLAYER_POSITION_Y, player.getSpace().y);
			} else {
				rs.updateInt(PLAYER_POSITION_X, -1);
				rs.updateInt(PLAYER_POSITION_Y, -1);
			}
			rs.updateInt(PLAYER_HEADING, player.getHeading().ordinal());
			rs.updateInt(PLAYER_REBOOT_POSITION_X, player.getRebootSpace().x);
			rs.updateInt(PLAYER_REBOOT_POSITION_Y, player.getRebootSpace().y);
			rs.updateRow();
		}
		rs.close();

		// TODO error handling/consistency check: check whether all players were updated
	}


	/**
	 * updates register card data corresponding to the current game id
	 *
	 * @param game used to get the current game id
	 * @author Gabriel
	 */
	private void updateCardsInDB(Board game) throws SQLException {
		PreparedStatement ps = getSelectCardsStatementU();
		ps.setInt(1, game.getGameId());

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			int playerId = rs.getInt(CARDS_PLAYERID);
			Player player = game.getPlayer(playerId);
			int position = rs.getInt(CARDS_POSITION);

			if (player.getCards().get(position).getCard() != null) {
				rs.updateInt(CARDS_COMMAND_HAND, player.getCards().get(position).getCard().command.ordinal());
			} else {
				rs.updateNull(CARDS_COMMAND_HAND);
			}
			if (position < Player.NO_REGISTERS && player.getProgramField(position).getCard() != null) {
				rs.updateInt(CARDS_COMMAND_REGISTER, player.getProgramField(position).getCard().command.ordinal());
			} else {
				rs.updateNull(CARDS_COMMAND_REGISTER);
			}
			rs.updateRow();
		}
		rs.close();

		// TODO error handling/consistency check: check whether all cards were updated
	}

	/**
	 * Deletes a game in the database
	 *
	 * @param game to be deleted
	 * @Author @Gabriel
	 */
	@Override
	public void deleteGameInDB(@NotNull Board game) {
		if (game.getPhase() == Phase.GAME_WON) {
			try {
				PreparedStatement ps = deleteGameStatement();
				ps.setInt(1, game.getGameId());

				ps.executeUpdate();

				ps.close();
			} catch (SQLException e) {
				//TODO: errorhandling
				e.printStackTrace();
			}
		}
	}

	/**
	 * Deletes every players upgrades
	 *
	 * @param game in which upgrades gets deleted
	 * @Author @Gabriel
	 */
	public void deletePlayerUpgradesInDB(@NotNull Board game) {
		try {
			PreparedStatement ps = deleteUpgradesStatement();
			ps.setInt(1, game.getGameId());
			int rowsDeleted=ps.executeUpdate();
			System.out.println(rowsDeleted);
			//ps.close();
		} catch (SQLException e) {
			//TODO: errorhandling
			e.printStackTrace();
		}
	}

	private static final String SQL_INSERT_GAME =
			"INSERT INTO Game(name, currentPlayer, phase, step,board) VALUES (?, ?, ?, ?, ?)";

	private PreparedStatement insert_game_stmt = null;

	/**
	 * @return a prepared statement for inserting a game
	 */
	private PreparedStatement getInsertGameStatementRGK() {
		if (insert_game_stmt == null) {
			Connection connection = connector.getConnection();
			try {
				insert_game_stmt = connection.prepareStatement(
						SQL_INSERT_GAME,
						Statement.RETURN_GENERATED_KEYS);
			} catch (SQLException e) {
				// TODO error handling
				e.printStackTrace();
			}
		}
		return insert_game_stmt;
	}

	private static final String SQL_SELECT_GAME =
			"SELECT * FROM Game WHERE gameID = ?";

	private PreparedStatement select_game_stmt = null;

	/**
	 * @return a prepared statement for selecting a specific game
	 */
	private PreparedStatement getSelectGameStatementU() {
		if (select_game_stmt == null) {
			Connection connection = connector.getConnection();
			try {
				select_game_stmt = connection.prepareStatement(
						SQL_SELECT_GAME,
						ResultSet.TYPE_FORWARD_ONLY,
						ResultSet.CONCUR_UPDATABLE);
			} catch (SQLException e) {
				// TODO error handling
				e.printStackTrace();
			}
		}
		return select_game_stmt;
	}

	private static final String SQL_SELECT_PLAYERS =
			"SELECT * FROM Player WHERE gameID = ?";
	private PreparedStatement select_players_stmt = null;

	/**
	 * @return a prepared statement for selecting players from a specific game id
	 */
	private PreparedStatement getSelectPlayersStatementU() {
		if (select_players_stmt == null) {
			Connection connection = connector.getConnection();
			try {
				select_players_stmt = connection.prepareStatement(
						SQL_SELECT_PLAYERS,
						ResultSet.TYPE_FORWARD_ONLY,
						ResultSet.CONCUR_UPDATABLE);
			} catch (SQLException e) {
				// TODO error handling
				e.printStackTrace();
			}
		}
		return select_players_stmt;
	}

	private static final String SQL_SELECT_CARDS =
			"SELECT * FROM Cards WHERE gameID = ?";

	private PreparedStatement select_card_stmt = null;


	/**
	 *
	 * @return a prepared statement for selecting cards corresponding to a specific game id
	 * @author Gabriel
	 */
	private PreparedStatement getSelectCardsStatementU() {
		if (select_card_stmt == null) {
			Connection connection = connector.getConnection();
			try {
				select_card_stmt = connection.prepareStatement(
						SQL_SELECT_CARDS,
						ResultSet.TYPE_FORWARD_ONLY,
						ResultSet.CONCUR_UPDATABLE);
			} catch (SQLException e) {
				// TODO error handling
				e.printStackTrace();
			}
		}
		return select_card_stmt;
	}

	private static final String SQL_SELECT_PLAYERS_ASC =
			"SELECT * FROM Player WHERE gameID = ? ORDER BY playerID ASC";
	private PreparedStatement select_players_asc_stmt = null;

	/**
	 * @return a prepared statement for selecting all players
	 * from a specific gameid in ascending order
	 */
	private PreparedStatement getSelectPlayersASCStatement() {
		if (select_players_asc_stmt == null) {
			Connection connection = connector.getConnection();
			try {
				// This statement does not need to be updatable
				select_players_asc_stmt = connection.prepareStatement(
						SQL_SELECT_PLAYERS_ASC);
			} catch (SQLException e) {
				// TODO error handling
				e.printStackTrace();
			}
		}
		return select_players_asc_stmt;
	}


	private static final String SQL_SELECT_CARDS_ASC =
			"SELECT * FROM Cards WHERE gameID = ? ORDER BY playerID ASC";
	private PreparedStatement select_cards_asc_stmt = null;

	/**
	 * @return a prepared statement for selecting all players cards
	 * from a specific gameid in ascending order
	 * @author Gabriel
	 */
	private PreparedStatement getSelectCardsASCStatement() {
		if (select_cards_asc_stmt == null) {
			Connection connection = connector.getConnection();
			try {
				// This statement does not need to be updatable
				select_cards_asc_stmt = connection.prepareStatement(
						SQL_SELECT_CARDS_ASC);
			} catch (SQLException e) {
				// TODO error handling
				e.printStackTrace();
			}
		}
		return select_cards_asc_stmt;
	}


	private static final String SQL_SELECT_GAMES =
			"SELECT gameID, name FROM Game";

	private PreparedStatement select_games_stmt = null;

	/**
	 * @return a prepared statement for selecting all games from the table
	 */
	private PreparedStatement getSelectGameIdsStatement() {
		if (select_games_stmt == null) {
			Connection connection = connector.getConnection();
			try {
				select_games_stmt = connection.prepareStatement(
						SQL_SELECT_GAMES);
			} catch (SQLException e) {
				// TODO error handling
				e.printStackTrace();
			}
		}
		return select_games_stmt;
	}

	private static final String SQL_DELETE_GAME =
			"DELETE FROM GAME where gameID = ?";

	private PreparedStatement sql_delete_game = null;

	/**
	 * Deletes a game from the DB
	 */
	private PreparedStatement deleteGameStatement() {
		if (sql_delete_game == null) {
			Connection connection = connector.getConnection();
			try {
				sql_delete_game = connection.prepareStatement(
						SQL_DELETE_GAME);
			} catch (SQLException e) {
				// TODO error handling
				e.printStackTrace();
			}
		}
		return sql_delete_game;
	}

	private static final String SQL_SELECT_UPGRADES =
			"SELECT * FROM Upgrades WHERE gameID = ?";

	private PreparedStatement select_upgrades_stmt = null;


	/**
	 *
	 * @return a prepared statement for selecting cards corresponding to a specific game id
	 * @author Gabriel
	 */
	private PreparedStatement getSelectUpgradesStatementU() {
		if (select_upgrades_stmt == null) {
			Connection connection = connector.getConnection();
			try {
				select_upgrades_stmt = connection.prepareStatement(
						SQL_SELECT_UPGRADES,
						ResultSet.TYPE_FORWARD_ONLY,
						ResultSet.CONCUR_UPDATABLE);
			} catch (SQLException e) {
				// TODO error handling
				e.printStackTrace();
			}
		}
		return select_upgrades_stmt;
	}

	private static final String SQL_SELECT_UPGRADES_ASC =
			"SELECT * FROM Upgrades WHERE gameID = ? ORDER BY playerID ASC";
	private PreparedStatement select_upgrades_asc_stmt = null;

	/**
	 * @return a prepared statement for selecting all players cards
	 * from a specific gameid in ascending order
	 * @author Gabriel
	 */
	private PreparedStatement getSelectUpgradesASCStatement() {
		if (select_upgrades_asc_stmt == null) {
			Connection connection = connector.getConnection();
			try {
				// This statement does not need to be updatable
				select_upgrades_asc_stmt = connection.prepareStatement(
						SQL_SELECT_UPGRADES_ASC);
			} catch (SQLException e) {
				// TODO error handling
				e.printStackTrace();
			}
		}
		return select_upgrades_asc_stmt;
	}

	private static final String SQL_DELETE_UPGRADES =
			"DELETE FROM Upgrades where gameID = ?";

	private PreparedStatement sql_delete_upgrades = null;

	/**
	 * Deletes a game from the DB
	 * @author @Gabriel
	 */
	private PreparedStatement deleteUpgradesStatement() {
		if (sql_delete_upgrades == null) {
			Connection connection = connector.getConnection();
			try {
				sql_delete_upgrades = connection.prepareStatement(
						SQL_DELETE_UPGRADES);
			} catch (SQLException e) {
				// TODO error handling
				e.printStackTrace();
			}
		}
		return sql_delete_upgrades;
	}

}
