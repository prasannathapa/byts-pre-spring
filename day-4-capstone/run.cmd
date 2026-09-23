@echo off
setlocal
REM Run all three parts of the Day 4 capstone in order. Requires Java 25 (JDK).
REM Fetches the jars first if lib\ is empty, then exits non-zero while any part still fails.
cd /d "%~dp0"
set FAILS=0

if not exist "lib\*.jar" call "%~dp0get-deps.cmd"
if not exist "lib\*.jar" (
  echo Could not get the jars. Check your network, then run get-deps.cmd by hand.
  exit /b 1
)

echo.
echo ======================================================================
echo   PART 1 - boot Tomcat, say hello, speak JSON
echo ======================================================================
call "%~dp0part1-hello\run.cmd"
if not "%errorlevel%"=="0" set /a FAILS+=1

echo.
echo ======================================================================
echo   PART 2 - a filter, a listener, a session, fifty threads
echo ======================================================================
call "%~dp0part2-state\run.cmd"
if not "%errorlevel%"=="0" set /a FAILS+=1

echo.
echo ======================================================================
echo   FINALE - App.java, the composition root of a bank over HTTP
echo ======================================================================
call "%~dp0part3-finale\run.cmd"
if not "%errorlevel%"=="0" set /a FAILS+=1

echo.
if %FAILS%==0 echo All three parts green. You wrote the container's application, and then you wired it.
if not %FAILS%==0 echo %FAILS% of 3 parts are not green yet. Those failures are your to-do list.

REM Hold the window open for a double-click. Set NO_PAUSE=1 (or CI) to skip it.
if defined CI goto :nopause
if defined NO_PAUSE goto :nopause
pause
:nopause
exit /b %FAILS%
