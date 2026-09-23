@echo off
setlocal
REM Download the jars this capstone needs into lib\ (one time, ~7 MB). Uses curl (built into Windows 10+).
REM Re-running is safe: jars already present are skipped.
cd /d "%~dp0"
if not exist lib mkdir lib
call :get "https://repo1.maven.org/maven2/org/apache/tomcat/embed/tomcat-embed-core/11.0.26/tomcat-embed-core-11.0.26.jar" tomcat-embed-core-11.0.26.jar
if not "%errorlevel%"=="0" exit /b 1
call :get "https://repo1.maven.org/maven2/org/apache/tomcat/tomcat-annotations-api/11.0.26/tomcat-annotations-api-11.0.26.jar" tomcat-annotations-api-11.0.26.jar
if not "%errorlevel%"=="0" exit /b 1
call :get "https://repo1.maven.org/maven2/com/h2database/h2/2.3.232/h2-2.3.232.jar" h2-2.3.232.jar
if not "%errorlevel%"=="0" exit /b 1
echo Done. Jars are in lib\ - now run run.cmd
exit /b 0

:get
if exist "lib\%~2" (
  echo   have   %~2
  exit /b 0
)
echo   fetch  %~2
curl -fL --retry 3 -o "lib\%~2" "%~1"
if not "%errorlevel%"=="0" (
  echo FAILED to download %~1
  del /q "lib\%~2" 2>nul
  exit /b 1
)
exit /b 0
