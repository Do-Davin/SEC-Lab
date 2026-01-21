#!/bin/bash

# Go to the directory of this script
cd "$(dirname "$0")"

# Clear console for clean output
clear

# Compile and run the project quietly
echo "Running Attendance System..."
mvn -q clean compile exec:java -Dexec.mainClass=com.davin.attendance.Main
