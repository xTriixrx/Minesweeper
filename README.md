# Minesweeper

<p align="center">
This is a Minesweeper implementation utilizing the JavaFX GUI framework. The initial commit of this project
was the original code from a beginner Java college course's project I had written. I added the JavaFX GUI, extended
the Board framework to support the new graphical interface, enhanced, & cleaned up the code. The game operates
identically to the original game.
</p>

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
