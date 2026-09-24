@echo off
title MediTrack - DoseWise
cd /d "%~dp0"
echo ===================================================
echo   Starting DoseWise / MediTrack Application...
echo ===================================================
java -cp "out;lib\mysql-connector-j.jar;MediTrack.jar" Main
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Application encountered an error or stopped.
    pause
)
