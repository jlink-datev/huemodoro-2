java -jar web/target/web-1.0-SNAPSHOT.jar --server.port=%1

REM @echo off
REM netstat -na | find "9999"
REM IF %ERRORLEVEL% equ 0 (
REM set port = 0
REM echo "Port already in use"
REM ) else (
REM set port = %1
REM echo "Port not in use"
REM)
