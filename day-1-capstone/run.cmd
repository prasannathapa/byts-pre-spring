@echo off
REM Run all three parts of the Day 1 capstone in order. Requires Java 25 (JDK).
REM Exits non-zero while any part still has a failing test, so CI can gate on it.
setlocal
cd /d "%~dp0"
set FAILS=0

echo.
echo ======================================================================
echo   PART 1 - a tiny bank (the four pillars)
echo ======================================================================
call "%~dp0part1-bank\run.cmd"
if errorlevel 1 set /a FAILS+=1

echo.
echo ======================================================================
echo   PART 2 - a report exporter (the five principles)
echo ======================================================================
call "%~dp0part2-report\run.cmd"
if errorlevel 1 set /a FAILS+=1

echo.
echo ======================================================================
echo   FINALE - export a bank statement (pillars + SOLID)
echo ======================================================================
call "%~dp0part3-finale\run.cmd"
if errorlevel 1 set /a FAILS+=1

echo.
if %FAILS%==0 echo All three parts green. That is the whole of Day 1 in working code.
if not %FAILS%==0 echo %FAILS% of 3 parts are not green yet. Those failures are your to-do list.

REM Wait for a keypress only when this was double-clicked, not when a shell or CI called it.
set "LAUNCH=%CMDCMDLINE:"=%"
if not "%LAUNCH%"=="%COMSPEC%" if not defined CI pause
exit /b %FAILS%
