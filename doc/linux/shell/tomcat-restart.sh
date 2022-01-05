#!/bin/bash

URL=http://localhost:8080
PID=$(ps -ef | grep /data/tomcat  | grep -v grep | grep -v tomcat-restart | awk '{print $2}')

if [ "${PID}X" != "X" ]; then

        httpStatus=$(curl -I -m 3 -o /dev/null -s -w %{http_code} ${URL}/unable)

        if [ ${httpStatus} != 200 ]; then
                echo "-----------项目设置不可用失败,退出发布------------"
                exit 1
        else
                echo "----------------项目设置不可用成功----------------"
                echo "----------------------等待5s----------------------"
                sleep 5s
        fi
else
        echo "-----------------未查找到tomcat服务---------------------"
fi

if [ "${PID}" ]; then
        kill -9 ${PID}
fi

 rm -rf /data/tomcat/webapps/ROOT*
 cp /tmp/ROOT.war /data/tomcat/webapps/ROOT.war

WAREXIST=`ls /data/tomcat/webapps | grep ROOT.war`
if [ "${WAREXIST}" != "ROOT.war" ];
then 
    echo "------------------war包不存在，退出发布-----------------"
    exit 1
fi

echo "--------------------开始发布----------------------"
cd /data/tomcat/bin/
./startup.sh

startTime=$(date +%s)
httpStatus=$(curl -I -m 10 -o /dev/null -s -w %{http_code} ${URL}/info)

while [ "X" = "X" ]
do
        if [ ${httpStatus} == 200 ];
        then
                echo "--------------------发布成功----------------------"
                break
        fi
        httpStatus=$(curl -I -m 10 -o /dev/null -s -w %{http_code} ${URL}/info)
        cost=$(($(date +%s) - $startTime))
        if [ $cost -gt 60 ];
        then
                echo "--------------------发布启动超过60s----------------------"
                break
        fi
done
