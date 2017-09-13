@setlocal
rem Pfade fue Java und Maven
set JAVA_HOME=D:\QGIS_Dev\jdk1.8.0_131
set M2_HOME=D:\QGIS_Dev\apache-maven-3.5.0
set PATH=%M2_HOME%\bin;%JAVA_HOME%\bin;PATH%

rem Build without Tests
pushd src\parent
call mvn install -DskipTests
popd
pushd src\cli-app
call mvn assembly:assembly -DskipTests
popd

rem Test
call src\cli-app\target\geogig\bin\geogig.bat --version
if not exist %~dp0test md %~dp0test
call src\cli-app\target\geogig\bin\geogig.bat init %~dp0test
if exist %~dp0test rd /s/q %~dp0test