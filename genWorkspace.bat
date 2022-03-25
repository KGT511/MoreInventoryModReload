@echo off
gradlew --refresh-dependencies genEclipseRuns && gradlew eclipse
pause
