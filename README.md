# Roborally

## About The Project
This is a digitized version of Avalon Hill’s board game, called “Roborally”.
Roborally is to be played by 2-6 players, and possesses many of the mechanics from the original Roborally game, such as lasers, upgrades, board elements as well as many others. 
In addition this digital version implements additional databasesupport for saving and loading games.

### Built with
- Java
- MariaDB/MySQL
- Json
- [Gson](https://github.com/google/gson)
- [Guava](https://github.com/google/guava)
- [JavaFX](https://openjfx.io/)
- [JUnit 5](https://junit.org/junit5/)

## How to play
### Rules
[Rules](https://media.wizards.com/2017/rules/roborally_rules.pdf)

Each turn, you’ll draw random
Program cards, each featuring an instruction for the movement of your robot. Choose
five of those cards to plan out your robot’s moves across a perilous course with the goal of being
the first to touch all the checkpoints in order.
 Getting to the checkpoints isn’t always easy, though, because you’ll execute your moves at
the same time as all the other players. Robots will get in each other’s way, push each other off
course, and shoot each other with lasers. Sometimes the biggest challenge is just surviving!

The order of each turn:
 1. Get programming cards (automatic)
 2. Arrange your Program cards among your
 five registers. (users)
 3. Complete each register in order: execute the Program
 cards, complete board movements, resolve all
 interactions, and touch flags and repair sites. (user)
 Repeat
 
 ### Boards 
 #### Course: ChopShopChallenge
 
 <img src="https://github.com/gabr0236/Roborally/blob/Development/ReadmeImages/ImgProgrammingPhase.png" width="700">

 #### Course: ChopShopChallenge

 <img src="https://github.com/gabr0236/Roborally/blob/Development/ReadmeImages/ImgCorridorBlitz.png" width="450">

#### Board Elements
- ▶️: player.

- 🟡: Checkpoint, stand on each checkpoint in order to win the game!

- 📡: Determines the order of which the robots moves by closest first.

- 🟩: Reboot, spawnpoint.

- 🟧: Energy, gives the player a random upgrade!

- 🔫 (small red stick on walls): Laser, shoots after each turn!

- 🟩 ▶️: Conveyor belt, moves robot 1 square in a direction.

- 🟦 ▶️: Conveyor belt, moves robot 2 squares in a direction.

- | 🟨: Push panel, pushes any player standing on the field on the registers described on the board.

- | 🟥: Wall.

- ⬛️: Pit, player dies if they step on it or over it.

- ↩️: Gear, rotates player.

### Upgrades
There are a total of 18 upgrade possibilities including RailGun which makes the player shoot through walls, BlueScreen of Death which makes the player deal "Worm Cards" instead of spam cards for damage, LuckyShield which may or may not protech the player from damage and Teleporter which ables the player to move through walls and over pits.

### Now lets get started!
Select game name:

 <img src="https://github.com/gabr0236/Roborally/blob/Development/ReadmeImages/ImgChoseGameName.png" width="200">

Chose map:

 <img src="https://github.com/gabr0236/Roborally/blob/Development/ReadmeImages/ImgChoseMap.png" width="200">

Chose number of players:

 <img src="https://github.com/gabr0236/Roborally/blob/Development/ReadmeImages/ImgChoseNumberOfPlayers.png" width="200">
 
Chose player name:
 
 <img src="https://github.com/gabr0236/Roborally/blob/Development/ReadmeImages/ImgChosePlayerName.png" width="200">
  
Chose player color:
  
 <img src="https://github.com/gabr0236/Roborally/blob/Development/ReadmeImages/ImgChoseColor.png" width="200">

### Saving and loading games

Save game:

 <img src="https://github.com/gabr0236/Roborally/blob/Development/ReadmeImages/ImgSaveGame.png" width="200">

Load game:

 <img src="https://github.com/gabr0236/Roborally/blob/Development/ReadmeImages/ImgLoadGame.png" width="200">
 
 <img src="https://github.com/gabr0236/Roborally/blob/Development/ReadmeImages/ImgLoadGame2.png" width="200">

## Installation

First download the project to your IDE.
Make sure to clean Maven after downoading. 
IMPORTANT the database connection must be started before running the app.

### Database setup

Download MySQL or MariaDB. This can be done easily using Homebrew ``brew install mariadb`` or ``brew install mysql``.

Download [MySQL Workbench](https://dev.mysql.com/downloads/workbench/).

Start MySQL or MariaDB on your computer.

Setup a new connection in MySQL workbench. 

<img src="https://github.com/gabr0236/Roborally/blob/Development/ReadmeImages/ImgCreateDBConnection.png" width="600">


Feel free to change the params in the Connector class. These params should match the above params from the setup.

<img src="https://github.com/gabr0236/Roborally/blob/Development/ReadmeImages/ImgConnectorClass.png" width="400">

Open a connection and chose "create new schema". Select "pisu" as the name (to match above).

<img src="https://github.com/gabr0236/Roborally/blob/Development/ReadmeImages/ImgCreateDBSchema.png" width="400">

Now run the project by running StartRoborally-Class.

## Authors
- Gabriel Haugbøl S205350: https://github.com/gabr0236
- Sebastian Sinding S205345: https://github.com/sebsinding
- Daniel Diamant S205336: https://github.com/dani8377
- Tobias S205358: https://github.com/s205358
- Ekkart Kindler (DTU)
