@echo off
echo ============================================
echo   MediTrack - Building Application...
echo ============================================

:: Create output directories
if not exist "out" mkdir out
if not exist "lib" mkdir lib

:: Check if MySQL connector exists
if not exist "lib\mysql-connector-j.jar" (
    echo.
    echo [WARNING] MySQL Connector JAR not found!
    echo Please download it from: https://dev.mysql.com/downloads/connector/j/
    echo Place the JAR file in the "lib" folder and rename it to "mysql-connector-j.jar"
    echo.
    pause
    exit /b 1
)

:: Compile all Java files
echo Compiling Java files...
javac -cp "lib\mysql-connector-j.jar" -d out src\model\*.java src\dao\*.java src\service\*.java src\ui\*.java src\Main.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Compilation failed! Check for errors above.
    pause
    exit /b 1
)

:: Create JAR file
echo Creating MediTrack.jar...
cd out
jar cfm ..\MediTrack.jar ..\MANIFEST.MF *.class model\*.class dao\*.class service\*.class ui\*.class
cd ..

echo.
echo ============================================
echo   Build Successful!
echo   Run "MediTrack.jar" or double-click "run.bat"
echo ============================================
pause
