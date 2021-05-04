/* Need to switch of FK check for MySQL since there are crosswise FK references */
SET FOREIGN_KEY_CHECKS = 0;;

CREATE TABLE IF NOT EXISTS Game (
  gameID int NOT NULL UNIQUE AUTO_INCREMENT,
  
  name varchar(20),

  phase tinyint,
  step tinyint,
  currentPlayer tinyint NULL,
  board varchar(50),
  
  PRIMARY KEY (gameID),
  FOREIGN KEY (gameID, currentPlayer) REFERENCES Player(gameID, playerID) on delete cascade
);;
  
CREATE TABLE IF NOT EXISTS Player (
  gameID int NOT NULL,
  playerID tinyint NOT NULL,

  name varchar(20),
  colour varchar(35),
  
  positionX int,
  positionY int,
  heading tinyint,

  rebootPosX int,
  rebootPosY int,
  
  PRIMARY KEY (gameID, playerID),
  FOREIGN KEY (gameID) REFERENCES Game(gameID) on delete cascade
);;

CREATE TABLE IF NOT EXISTS Cards (
  gameID int NOT NULL,
  playerID tinyint NOT NULL,

  position int NOT NULL,
  commandHand tinyint,
  commandRegister tinyint,

  PRIMARY KEY (gameID, playerID, position),
  FOREIGN KEY (gameID, playerID) references Player(gameID, playerID) on delete cascade
);;

CREATE TABLE IF NOT EXISTS Upgrades (
  gameID int NOT NULL,
  playerID tinyint NOT NULL,
  upgrade tinyint,
  PRIMARY KEY (gameID, playerID, upgrade),
  FOREIGN KEY (gameID, playerID) references Player(gameID, playerID) on delete cascade
);;

SET FOREIGN_KEY_CHECKS = 1;;
