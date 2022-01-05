#!/bin/bash

BASE_HOME=/data/project
GIT_HOME=${BASE_HOME}/git
TOMCAT_PATH=/data/tomcat/

#创建目录
function makeDir(){

	PROJECT_HOME="${GIT_HOME}/$1"
	PROJECT_MAIN_HOME="${PROJECT_HOME}/src/main"
	CONFIG_HOME="${BASE_HOME}/config/"

	if [ ! -d "${CONFIG_HOME}" ]; then
		mkdir -p ${CONFIG_HOME}
	fi

	if [ ! -d "${GIT_HOME}" ]; then
		mkdir -p ${GIT_HOME}
	fi

}

#执行方法
case $1 in
    "app")
		makeDir $1

        ;;

	"data")
		makeDir $1

        ;;
    *)
    echo
    if [ ! -n "$1" ]; then
        echo "Usage:  app "
    else
        echo "Usage:  app "
    fi
    echo
    exit 1
esac


