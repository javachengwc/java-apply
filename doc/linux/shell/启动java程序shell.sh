#!/bin/sh
export LANG="en_US.UTF-8"
source /etc/profile
cd /data/deploy/app
export clazzpath="classes"

PID=$(jps -ml | grep AppMain | awk '{print $1}')

if [[ ! -z $PID ]] ; then
        echo "program PID is:$PID"
        kill -9 $PID
fi

for perjar in dependency/*.jar
do
        clazzpath=$clazzpath:$perjar
done
echo "execute AppMain start..................."
  cmd="java -Xms256m -Xmx1024m -XX:PermSize=128m -XX:MaxPermSize=256m -classpath "${clazzpath}" com.app.AppMain $1 $2 $3"
  exec ${cmd}
echo "execute AppMain finish..................."

-------------------------------------------------
#!/bin/sh
export LANG="en_US.UTF-8"
source /etc/profile
cd `dirname $0`
export clazzpath="classes"

for perjar in dependency/*.jar
do
        clazzpath=$clazzpath:$perjar
done

PID=($(jps -ml | grep 'AppMain' | awk '{print $1}'))
cnt=${#PID[@]}
echo "AppMain has $cnt program is running........"
for pg in ${PID[@]}
do
        echo "program PID is:$pg"
        kill -9 $pg
done

echo "execute AppMain start..................."
  cmd="java -Xms256m -Xmx1024m -XX:PermSize=128m -XX:MaxPermSize=256m -classpath "${clazzpath}" com.app.AppMain $1 $2 $3 1>/dev/null 2>&1"
  exec ${cmd}
echo "execute AppMain finish..................."

------------------------------------------------------
#!/bin/sh
export LANG="en_US.UTF-8"
source /etc/profile
cd $1
v=$2
port=$3
echo "---------------git v is:$v-----port:$port----------------"
PID=`jps -ml| grep AppMain | grep $v | awk '{print $1}'`
cnt=${#PID[@]}
echo "AppMain has $cnt program is running........"
for pg in ${PID[@]}
do
    echo "program PID is:$pg"
    kill -9 $pg
done

echo "execute AppMain start..................."
    nohup java -Xms256m -Xmx512m -jar AppMain.jar --server.port=$port  -Dv=$v 1>/dev/null 2>&1  &
echo "execute AppMain finish..................."

------------------------------------------------------