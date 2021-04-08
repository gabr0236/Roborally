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

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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


	private Connector connector;
	
	Repository(Connector connector){
		this.connector = connector;
	}


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
				ps.setString(1, "Date: " +  new Date()); // instead of name
				ps.setNull(2, Types.TINYINT); // game.getPlayerNumber(game.getCurrentPlayer())); is inserted after players!
				ps.setInt(3, game.getPhase().ordinal());
				ps.setInt(4, game.getStep());
				ps.setString(5,game.boardName);

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
			//TODO this method needs to be implemented first
			updateCardsInDB(game);


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

			//TODO this method needs to be implemented first
			loadCardFieldsFromDB(game);


			return game;
		} catch (SQLException e) {
			// TODO error handling
			e.printStackTrace();
			System.err.println("Some DB error");
		}
		return null;
	}


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
				result.add(new GameInDB(id,name));
			}
			rs.close();
		} catch (SQLException e) {
			// TODO proper error handling
			e.printStackTrace();
		}
		return result;		
	}

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
	 *
	 * @param game
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
			for (int j = 0; j < Player.NO_CARDS;j++) {
				rs.moveToInsertRow();
				rs.updateInt(CARDS_GAMEID, game.getGameId());
				rs.updateInt(CARDS_PLAYERID, i);
				rs.updateInt(CARDS_POSITION, j);
				rs.updateInt(CARDS_COMMAND_HAND, player.getCardField(j).getCard().command.ordinal());
				if(j<Player.NO_REGISTERS && player.getProgramField(j).getCard()!=null){
					rs.updateInt(CARDS_COMMAND_REGISTER, player.getProgramField(j).getCard().command.ordinal());
				}
				rs.insertRow();
			}
		}
		rs.close();
	}
	
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
				Player player = new Player(game, colour ,name);
				game.addPlayer(player);
				
				int x = rs.getInt(PLAYER_POSITION_X);
				int y = rs.getInt(PLAYER_POSITION_Y);
				int rebootPosX = rs.getInt(PLAYER_REBOOT_POSITION_X);
				int rebootPosY = rs.getInt(PLAYER_REBOOT_POSITION_Y);
				player.setSpace(game.getSpace(x,y));
				player.setRebootSpace(game.getSpace(rebootPosX,rebootPosY));
				int heading = rs.getInt(PLAYER_HEADING);
				player.setHeading(Heading.values()[heading]);

				// TODO  should also load players program and hand here
			} else {
				// TODO error handling
				System.err.println("Game in DB does not have a player with id " + i +"!");
			}
		}
		rs.close();
	}

	/**
	 *
	 * @param game
	 * @throws SQLException
	 * @author Gabriel
	 */
	private void loadCardFieldsFromDB(Board game) throws SQLException{
		PreparedStatement ps = getSelectCardsASCStatement();
		ps.setInt(1, game.getGameId());

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			int playerId = rs.getInt(CARDS_PLAYERID);
			int position = rs.getInt(CARDS_POSITION);

			int cardHand = rs.getInt(CARDS_COMMAND_HAND);
			boolean cardHandIsNull = rs.wasNull();
			int cardRegister = rs.getInt(CARDS_COMMAND_REGISTER);
			Player player =game.getPlayers().get(playerId);

			if(!cardHandIsNull){
				player.getCardField(position).setCard(new CommandCard(Command.values()[cardHand]));
			}
			else if (position<Player.NO_REGISTERS) {
				player.getProgramField(position).setCard(new CommandCard(Command.values()[cardRegister]));
			}

		}
		rs.close();
	}
	
	private void updatePlayersInDB(Board game) throws SQLException {
		PreparedStatement ps = getSelectPlayersStatementU();
		ps.setInt(1, game.getGameId());

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			int playerId = rs.getInt(PLAYER_PLAYERID);
			// TODO should be more defensive
			Player player = game.getPlayer(playerId);
			// rs.updateString(PLAYER_NAME, player.getName()); // not needed: player's names does not change
			rs.updateInt(PLAYER_POSITION_X, player.getSpace().x);
			rs.updateInt(PLAYER_POSITION_Y, player.getSpace().y);
			rs.updateInt(PLAYER_HEADING, player.getHeading().ordinal());
			rs.updateInt(PLAYER_REBOOT_POSITION_X, player.getRebootSpace().x);
			rs.updateInt(PLAYER_REBOOT_POSITION_Y, player.getRebootSpace().y);
			rs.updateRow();
		}
		rs.close();

		// TODO error handling/consistency check: check whether all players were updated
	}


	/**
	 *
	 * @param game
	 * @throws SQLException
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

			if(player.getCardField(position).getCard()!=null){
				rs.updateInt(CARDS_COMMAND_HAND, player.getCardField(position).getCard().command.ordinal());
			} else {
				rs.updateNull(CARDS_COMMAND_HAND);
			}
			if(position<Player.NO_REGISTERS && player.getProgramField(position).getCard()!=null) {
				rs.updateInt(CARDS_COMMAND_REGISTER, player.getProgramField(position).getCard().command.ordinal());
			}else {
				rs.updateNull(CARDS_COMMAND_REGISTER);
			}
			rs.updateRow();
		}
		rs.close();

		// TODO error handling/consistency check: check whether all cards were updated
	}

	private static final String SQL_INSERT_GAME =
			"INSERT INTO Game(name, currentPlayer, phase, step,board) VALUES (?, ?, ?, ?,?)";

	private PreparedStatement insert_game_stmt = null;

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
	 * @return
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
	 *
	 * @return
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
}
