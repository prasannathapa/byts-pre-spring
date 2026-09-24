@echo off
REM Run one demo without an IDE:   run.cmd Demo00NoFrameworkAtAll
setlocal
cd /d "%~dp0"
set CLASS=%1
if "%CLASS%"=="" set CLASS=Demo00NoFrameworkAtAll
if not exist out mkdir out
javac -cp "jars\*" -d out src\*.java
if not "%errorlevel%"=="0" exit /b %errorlevel%
java -cp "jars\*;out" %CLASS%
