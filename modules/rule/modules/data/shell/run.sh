#!/bin/sh

WORKER_HOME=`pwd`

WORKER_LIB="$WORKER_HOME/lib"

CLASS_PATH="$WORKER_HOME/bin"

JAVA_OPTS="-Xms256m -Xmx8096m -XX:+UseConcMarkSweepGC"

CLASS_PATH="$CLASS_PATH:`find "$WORKER_LIB" -name "*.jar" | sed 's/$/:/g' | tr -d '\n'`:$CLASSPATH"

cd $WORKER_HOME
java $JAVA_OPTS -classpath $CLASS_PATH com.rule.data.Main 3000 4000 &
