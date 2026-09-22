@echo off
REM Compile the capstone and run the tests. Requires Java 25 (JDK).
cd /d "%~dp0"
javac report\Report.java report\ReportFormat.java report\CsvFormat.java report\JsonFormat.java report\ReportService.java report\RunTests.java
if %errorlevel%==0 java report.RunTests
exit /b %errorlevel%
