# Minesweeper

<p align="center">
This is a Minesweeper implementation utilizing the JavaFX GUI framework. The initial commit of 
this project was the original code from a beginner Java college course's project I had written. I
added the JavaFX GUI, extended the Board framework to support the new graphical interface,enhanced, & cleaned up the code. The game operates identically to the original game.

For instructions on how to play Minesweeper, here is a <a href="https://minesweepergame.com/strategy/how-to-play-minesweeper.php">guide</a> on the rules.
</p>

## Minesweeper Board Examples

<p align="center">
Below are images from the game play of each mode: Beginner, Intermediate, & Expert.
</p>

### Beginner

<p align="center"> <img src="https://github.com/xTriixrx/Minesweeper/blob/master/imgs/beginner.png"/> </p>

### Intermediate

<p align="center"> <img src="https://github.com/xTriixrx/Minesweeper/blob/master/imgs/intermediate.png"/> </p>

### Expert

<p align="center"> <img src="https://github.com/xTriixrx/Minesweeper/blob/master/imgs/expert.png"/> </p>

### Linux Instruction's

```Bash
# Create a local lib directory
mkdir lib

# Copy required dependencies from maven to the local lib directory
mvn -DoutputDirectory=lib dependency:copy-dependencies

# Perform mvn clean package to build jar
mvn clean package

# Make startup game scripts executable if not already executable
chmod +x scripts/*.sh

# Run beginner game instance
./scripts/start.sh beginner

# Run intermediate game instance
./scripts/start.sh intermediate

# Run expert game instance
./scripts/start.sh expert

# Run command line game version (ensure log4j2.xml file is updated to support console output)
./scripts/cmdline.sh
```
