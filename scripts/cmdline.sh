#!/bin/sh

java -Dlog4j.configurationFile="log4j2.xml" \
    -cp "target/minesweeper-1.0.0.jar:lib/*" com.qfi.minesweeper.Main

exit 0
