#!/bin/bash
source /etc/profile
cd `dirname $0`
export clspath="../classes"
for k in ../dependency/*.jar
do
	clspath=$clspath:$k
done

echo "execute start..................."

java  -Xms128m -Xmx1024m -Xmn42m -XX:PermSize=64m -XX:MaxPermSize=128m -Dapp.name=$1 -classpath "$clspath" com.dubbo.server.DubboServer $1





