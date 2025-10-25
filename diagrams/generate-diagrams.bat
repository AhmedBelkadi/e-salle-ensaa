@echo off
REM Generate PlantUML Diagrams - E-Salle ENSAA
REM This script generates PNG images from PlantUML source files

echo Generating PlantUML diagrams for E-Salle ENSAA...
echo.

REM Check if PlantUML is available
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java and try again
    pause
    exit /b 1
)

REM Check if plantuml.jar exists
if not exist "plantuml.jar" (
    echo ERROR: plantuml.jar not found
    echo Please download plantuml.jar from https://plantuml.com/download
    echo and place it in the diagrams directory
    pause
    exit /b 1
)

REM Create output directory
if not exist "output" mkdir output

echo Generating diagrams...
echo.

REM Generate PNG images
java -jar plantuml.jar -tpng -o output *.puml

if %errorlevel% equ 0 (
    echo.
    echo SUCCESS: All diagrams generated successfully!
    echo Output files are in the 'output' directory
    echo.
    echo Generated files:
    dir output\*.png /b
) else (
    echo.
    echo ERROR: Failed to generate some diagrams
    echo Check the error messages above
)

echo.
echo Press any key to exit...
pause >nul
