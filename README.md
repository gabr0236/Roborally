# Roborally

## About The Project
This is a digitized version of Avalon Hill’s board game, called “Roborally”.
Roborally is to be played by 2-6 players, and possesses many of the mechanics from the original Roborally game, such as lasers, upgrades, board elements as well as many others. 
In addition this digital version implements additional databasesupport for saving and loading games.

### Built with
- Java
- MariaDB
- MySQL
- Json
- [Gson](https://github.com/google/gson)
- [Guava](https://github.com/google/guava)
- [JavaFX](https://openjfx.io/)
- [JUnit 5](https://junit.org/junit5/)

## Getting Started

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


