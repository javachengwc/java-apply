CONF=/data/deploy/confbak/app
SRC=/data/program/app
DEPLOY=/data/deploy/app
PROJECTS=(app-core app-service app-manage app-task)

cd ${SRC}
git pull origin master
for i in ${PROJECTS[*]}; do
    cd ${SRC}/modules/${i}
    echo `pwd`"----------------------------"
    if [[ $i == "app-task" ]] ; then
        cp -avf ${CONF}/log4j_bak.properties ${SRC}/modules/${i}/src/main/profiles/dev/log4j.properties
        mvn clean compile package dependency:copy-dependencies install -Dmaven.test.skip -Pdev

        rm -rf ${DEPLOY}/classes
        rm -rf ${DEPLOY}/dependency
        cp -ravf ${SRC}/modules/app-task/target/classes/ ${DEPLOY}/classes
        cp -ravf ${SRC}/modules/app-task/target/dependency/ ${DEPLOY}/dependency
    else
        mvn clean compile package dependency:copy-dependencies install -Dmaven.test.skip -Pdev
    fi
done