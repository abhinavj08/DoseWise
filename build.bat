@echo off
echo ============================================
echo   MediTrack / DoseWise - Build Script
echo ============================================

if not exist "out" mkdir out
if not exist "lib" mkdir lib

echo Compiling Java source files...
javac -cp "lib\mysql-connector-j.jar" -d out src\model\*.java src\dao\*.java src\service\*.java src\ui\*.java src\Main.java

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Compilation failed!
    pause
    exit /b 1
)

echo Packaging into MediTrack.jar...
set JAR_CMD=jar
where jar >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    if exist "C:\Program Files\Java\jdk-26.0.2\bin\jar.exe" (
        set "JAR_CMD=C:\Program Files\Java\jdk-26.0.2\bin\jar.exe"
    )
)

"%JAR_CMD%" cfm MediTrack.jar MANIFEST.MF -C out .

echo.
echo ============================================
echo   Build Successful!
echo   Double-click run.bat to start the app!
echo ============================================
pause
