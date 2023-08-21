@echo off
gradlew genEclipseRuns && gradlew eclipse
@REM gradlew --refresh-dependencies genEclipseRuns && gradlew eclipse
pause
