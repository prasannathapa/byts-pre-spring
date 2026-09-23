@echo off
setlocal
REM Run the lost-update demo. Requires Java 25 (JDK) and the jar in lib (run get-deps.cmd once).
REM No argument: watch a deposit get lost. Or pass atomic, lock or version to see it fixed.
cd /d "%~dp0.."
if not exist "lib\*.jar" (
  echo No jars in lib\ yet - run get-deps.cmd first.
  exit /b 1
)
javac -cp "lib\*" demo\LostUpdate.java
if %errorlevel%==0 java -cp "lib\*;." demo.LostUpdate %*
exit /b %errorlevel%
