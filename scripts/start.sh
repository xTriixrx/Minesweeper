LEVEL=""
EXPERT_LEVEL="EXPERT"
BEGINNER_LEVEL="BEGINNER"
INTERMEDIATE_LEVEL="INTERMEDIATE"
LOG4J_CONFIG_LOC="log4j2.xml"

echo $1

if [ $# -lt 1 ] || [ $# -gt 1 ]; then
    LEVEL=$BEGINNER_LEVEL;
fi

# Checks level passed to script by uppercasing & checking against known levels
if [ $# -eq 1 ]; then
    if [[ ${1^^} =~ $BEGINNER_LEVEL ]]; then
        LEVEL=$BEGINNER_LEVEL;
    elif [[ ${1^^} == $INTERMEDIATE_LEVEL ]]; then
        LEVEL=$INTERMEDIATE_LEVEL;
    elif [[ ${1^^} == $EXPERT_LEVEL ]]; then
        LEVEL=$EXPERT_LEVEL;
    else
        LEVEL=$BEGINNER_LEVEL;
    fi
fi

echo "Starting Minesweeper with level: $LEVEL...";

java -Dlog4j.configurationFile=$LOG4J_CONFIG_LOC -DgameLevel="$LEVEL" \
-cp "target/minesweeper-1.0.0.jar:lib/*" com.qfi.minesweeper.GUIDriverRunner

exit 0
