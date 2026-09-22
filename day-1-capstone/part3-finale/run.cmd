@echo off
REM Compile the finale and run the tests. Requires Java 25 (JDK).
cd /d "%~dp0"
javac finale\Account.java finale\StatementFormat.java finale\PlainFormat.java finale\CsvFormat.java finale\StatementExporter.java finale\RunTests.java
if %errorlevel%==0 java finale.RunTests
exit /b %errorlevel%
