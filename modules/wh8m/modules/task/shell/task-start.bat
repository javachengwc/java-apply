@echo off
setlocal enabledelayedexpansion
SET JAVA_LIBS=D:/standalone_project/task/lib
SET JAVA_CLASS=D:\standalone_project\task\classes
set CLASSPATH=%CLASSPATH%;%JAVA_CLASS%
echo CLASSPATH: %CLASSPATH%
for /r %JAVA_LIBS% %%i in (*.jar) do set CLASSPATH=!CLASSPATH!;%%i
echo =======================================================
echo .
echo JAVA_HOME: %JAVA_HOME%
echo .
echo CLASSPATH: %CLASSPATH%
echo .
echo =======================================================
echo .
java -classpath %CLASSPATH% -Xms256m -Xmx256m com.task.server.TaskServer