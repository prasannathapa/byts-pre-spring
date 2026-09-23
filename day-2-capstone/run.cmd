@echo off
setlocal
REM Run all three parts of the Day 2 capstone in order. Requires Java 25 (JDK).
REM Exits non-zero while any part still has a failing test, so CI can gate on it.
cd /d "%~dp0"
set FAILS=0

echo.
echo ======================================================================
echo   PART 1 - a Version that can be a key (equals, hashCode, compareTo)
echo ======================================================================
call "%~dp0part1-keys\run.cmd"
if not "%errorlevel%"=="0" set /a FAILS+=1

echo.
echo ======================================================================
echo   PART 2 - wrap a Set, and build an LRU (composition over inheritance)
echo ======================================================================
call "%~dp0part2-wrappers\run.cmd"
if not "%errorlevel%"=="0" set /a FAILS+=1

echo.
echo ======================================================================
echo   FINALE - WordStats (merge, computeIfAbsent, a heap, two read-only maps)
echo ======================================================================
call "%~dp0part3-finale\run.cmd"
if not "%errorlevel%"=="0" set /a FAILS+=1

echo.
if %FAILS%==0 echo All three parts green. That is the Collections framework used the way it was meant to be.
if not %FAILS%==0 echo %FAILS% of 3 parts are not green yet. Those failures are your to-do list.

REM Hold the window open for a double-click. Set NO_PAUSE=1 (or CI) to skip it.
if defined CI goto :nopause
if defined NO_PAUSE goto :nopause
pause
:nopause
exit /b %FAILS%
