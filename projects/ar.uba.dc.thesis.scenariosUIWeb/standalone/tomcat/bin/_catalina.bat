@echo off
rem Start/Stop Script for the CATALINA Server
rem
rem Environment Variable Prequisites:
rem
rem JAVA_HOME           (Optional) May point to your JDK or JRE.
rem CATALINA_NO_CONSOLE (Optional) Uses javaw instead of java.
rem CATALINA_OPTS       (Optional) Java runtime options used when the "start", or "stop" command is
rem                     executed.
rem CATALINA_HOME       Must point at your Catalina "build" directory.

if exist "%CATALINA_HOME%\bin\_catalina.bat" goto okHome
echo The CATALINA_HOME environment variable is not defined correctly.
echo This environment variable is needed to run this program.
exit /b 1
goto end

:okHome
rem Get standard Java environment variables
if "%JAVA_HOME%" == "" goto tryDefaultJre
set _RUNJAVA=%JAVA_HOME%\bin\java.exe
set _RUNJAVAW=%JAVA_HOME%\bin\javaw.exe
goto gotJavaHome
:tryDefaultJre
set _RUNJAVA=%WINDIR%\system32\java.exe
set _RUNJAVAW=%WINDIR%\system32\javaw.exe
:gotJavaHome
if not exist "%_RUNJAVA%" goto tryJreHome
if not exist "%_RUNJAVAW%" goto tryJreHome
goto okSetclasspath
:tryJreHome
if "%JRE_HOME%" == "" goto noJavaHome
set _RUNJAVA=%JRE_HOME%\bin\java.exe
set _RUNJAVAW=%JRE_HOME%\bin\javaw.exe
goto okSetclasspath
:noJavaHome
echo The JAVA_HOME environment variable is not defined correctly.
echo This environment variable is needed to run this program.
exit /b 1
goto end

:okSetclasspath
set MAINCLASS=org.apache.catalina.startup.Bootstrap
set CATALINA_BASE=%CATALINA_HOME%
set LOGGING_OPTS=-Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager -Djava.util.logging.config.file="%CATALINA_BASE%\conf\logging.properties"
set _EXECJAVA="%_RUNJAVA%"
if "%CATALINA_NO_CONSOLE%" == "" goto done_catalina_console
set _EXECJAVA=start "" "%_RUNJAVAW%"
:done_catalina_console
rem ----- Execute The Requested Command -----
echo Using JAVA_HOME           : %JAVA_HOME%
echo Using Java executable     : %_RUNJAVA%
echo Using CATALINA_NO_CONSOLE : %CATALINA_NO_CONSOLE%
echo Using CATALINA_OPTS       : %CATALINA_OPTS%
echo Using CATALINA_HOME       : %CATALINA_HOME%

if ""%1"" == ""start"" goto execCmd
if ""%1"" == ""stop"" goto execCmd
echo Usage: _catalina.bat command
echo command:
echo     start    Start Catalina in a separate window
echo     stop     Stop Catalina
exit /b 1
goto end

:execCmd
title Tomcat
set ACTION=%1
%_EXECJAVA% %CATALINA_OPTS% %LOGGING_OPTS% -Djava.ext.dirs="%CATALINA_HOME%\lib" -Dcatalina.base="%CATALINA_BASE%" -Dcatalina.home="%CATALINA_HOME%" %MAINCLASS% %ACTION%
:end
