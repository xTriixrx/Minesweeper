echo "Starting Minesweeper..."

LOG4J_CONFIG_LOC="log4j2.xml"

java -Dlog4j.configurationFile=$LOG4J_CONFIG_LOC \
-cp "target/minesweeper-1.0.0.jar:lib/*" com.qfi.minesweeper.GUIDriverRunner

exit 0
