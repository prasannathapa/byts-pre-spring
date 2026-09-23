@echo off
setlocal
REM Compile the finale and run the tests. Requires Java 25 (JDK).
cd /d "%~dp0"
javac finale\WordStats.java finale\RunTests.java
if %errorlevel%==0 java finale.RunTests
exit /b %errorlevel%
