@echo off
setlocal
REM Run all three parts of the Day 3 capstone in order. Requires Java 25 (JDK).
REM Fetches the H2 jar first if lib\ is empty.
REM Exits non-zero while any part still has a failing test, so CI can gate on it.
cd /d "%~dp0"
set FAILS=0

REM cmd expands %errorlevel% for the whole block at once, so the check cannot live inside
REM the brackets: it would read the value from before the call. Test for the jars instead.
if not exist "lib\*.jar" call "%~dp0get-deps.cmd"
if not exist "lib\*.jar" (
  echo get-deps could not fetch the H2 jar. Copy h2-2.3.232.jar into lib\ by hand and run this again.
  exit /b 1
)

echo.
echo ======================================================================
echo   PART 1 - connect, create, insert, query
echo ======================================================================
call "%~dp0part1-connect\run.cmd"
if not "%errorlevel%"=="0" set /a FAILS+=1

echo.
echo ======================================================================
echo   PART 2 - transfer money, and survive hostile input
echo ======================================================================
call "%~dp0part2-transfer\run.cmd"
if not "%errorlevel%"=="0" set /a FAILS+=1

echo.
echo ======================================================================
echo   FINALE - one repository interface, two implementations
echo ======================================================================
call "%~dp0part3-finale\run.cmd"
if not "%errorlevel%"=="0" set /a FAILS+=1

echo.
if %FAILS%==0 echo All three parts green. That is the whole of Day 3 in working code.
if not %FAILS%==0 echo %FAILS% of 3 parts are not green yet. Those failures are your to-do list.

REM Hold the window open for a double-click. Set NO_PAUSE=1 (or CI) to skip it.
if defined CI goto :nopause
if defined NO_PAUSE goto :nopause
pause
:nopause
exit /b %FAILS%
