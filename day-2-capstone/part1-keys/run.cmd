@echo off
setlocal
REM Compile part 1 and run the tests. Requires Java 25 (JDK).
cd /d "%~dp0"
javac keys\Version.java keys\Registry.java keys\RunTests.java
if %errorlevel%==0 java keys.RunTests
exit /b %errorlevel%
