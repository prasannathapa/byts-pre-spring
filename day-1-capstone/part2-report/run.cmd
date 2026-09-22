@echo off
setlocal
REM Compile the capstone and run the tests. Requires Java 25 (JDK).
cd /d "%~dp0"
javac report\*.java
if %errorlevel%==0 java report.RunTests
exit /b %errorlevel%
