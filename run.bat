@echo off
echo Starting MediTrack...
java -jar MediTrack.jar
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Failed to start. Make sure:
    echo   1. Java is installed (java -version)
    echo   2. MySQL is running
    echo   3. Database is created (run meditrack.sql)
    echo   4. You ran build.bat first
    pause
)
