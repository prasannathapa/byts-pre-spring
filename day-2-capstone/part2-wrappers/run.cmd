@echo off
setlocal
REM Compile part 2 and run the tests. Requires Java 25 (JDK).
cd /d "%~dp0"
javac wrappers\ForwardingSet.java wrappers\InstrumentedHashSet.java wrappers\InstrumentedSet.java wrappers\LruCache.java wrappers\RunTests.java
if %errorlevel%==0 java wrappers.RunTests
exit /b %errorlevel%
