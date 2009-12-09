@echo off
set CURR_DIR=%~dp0
cd "%CURR_DIR%\.."
set CATALINA_HOME=%CD%
cd "%CURR_DIR%"
set EXECUTABLE=%CATALINA_HOME%\bin\_catalina.bat

cd "%TEMP%\tomcat_logs"
del /q *.log*

cd "%CURR_DIR%"
call "%EXECUTABLE%" start
