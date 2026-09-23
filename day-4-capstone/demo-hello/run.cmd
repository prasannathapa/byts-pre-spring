@echo off
setlocal
REM The mid-morning demo: one servlet, one main method, port 8080. Ctrl-C to stop.
cd /d "%~dp0"
if not exist "..\lib\*.jar" (
  echo No jars in lib\ yet - run get-deps.cmd in the capstone folder first.
  exit /b 1
)
javac -cp "..\lib\*" demo\*.java
if %errorlevel%==0 java -cp "..\lib\*;." demo.App
exit /b %errorlevel%
