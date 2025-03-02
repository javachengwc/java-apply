@echo off
echo.
echo [信息] 清理工程target生成路径。

%~d0
cd %~dp0

cd ..
call mvn clean

pause