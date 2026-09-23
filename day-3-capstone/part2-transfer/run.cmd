@echo off
setlocal
REM Compile this part and run the tests. Requires Java 25 (JDK) and the jars in ..\lib (run ..\get-deps.cmd once).
cd /d "%~dp0"
if not exist "..\lib\*.jar" (
  echo No jars in lib\ yet - run get-deps.cmd in the capstone folder first.
  exit /b 1
)
javac -cp "..\lib\*" transfer\Account.java transfer\AccountSearch.java transfer\Db.java transfer\InsufficientFundsException.java transfer\RunTests.java transfer\TransferService.java
if %errorlevel%==0 java -cp "..\lib\*;." transfer.RunTests
exit /b %errorlevel%
