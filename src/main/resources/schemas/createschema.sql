/* Need to switch of FK check for MySQL since there are crosswise FK references */
SET FOREIGN_KEY_CHECKS = 0;;

CREATE TABLE IF NOT EXISTS Game (
  gameID int NOT NULL UNIQUE AUTO_INCREMENT,
  
  name varchar(255),

  phase tinyint,
  step tinyint,
  currentPlayer tinyint NULL,
  
  PRIMARY KEY (gameID),
  FOREIGN KEY (gameID, currentPlayer) REFERENCES Player(gameID, playerID)
);;
  
CREATE TABLE IF NOT EXISTS Player (
  gameID int NOT NULL,
  playerID tinyint NOT NULL,

  name varchar(255),
  colour varchar(31),
  
  positionX int,
  positionY int,
  heading tinyint,

  rebootPosX int,
  rebootPosY int,
  
  PRIMARY KEY (gameID, playerID),
  FOREIGN KEY (gameID) REFERENCES Game(gameID)
);;

CREATE TABLE IF NOT EXISTS Cards (
  gameID int NOT NULL,
  playerID tinyint NOT NULL,

  position int NOT NULL,
  command tinyint,

  PRIMARY KEY (gameID, playerID, position),
  FOREIGN KEY (gameID, playerID) references Player(gameID, playerID)
);;

SET FOREIGN_KEY_CHECKS = 1;;

# TODO still some stuff missing here