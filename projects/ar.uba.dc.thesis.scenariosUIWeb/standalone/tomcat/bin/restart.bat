@echo off
set CURR_DIR=%~dp0
cd "%CURR_DIR%\.."
set CATALINA_HOME=%CD%
set EXECUTABLE=%CATALINA_HOME%\bin\_catalina.bat

cd "%CURR_DIR%"
call "%EXECUTABLE%" stop

ping -n 3 localhost >NUL
cd "%TEMP%\tomcat_logs"
del /q *.log*

cd "%CURR_DIR%"
call "%EXECUTABLE%" start
