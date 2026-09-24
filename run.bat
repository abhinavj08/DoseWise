@echo off
echo ===================================================
echo   Starting DoseWise / MediTrack Application...
echo ===================================================
java -cp "MediTrack.jar;lib\mysql-connector-j.jar;out" Main
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Application failed to launch.
    pause
)
