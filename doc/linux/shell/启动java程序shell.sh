#!/bin/sh
export LANG="en_US.UTF-8"
source /etc/profile
cd /data/deploy/app
export clazzpath="classes"

PID=$(jps | grep AppMain | awk '{print $1}')

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
  cmd="java -Xms256m -Xmx1024m -XX:PermSize=128m -XX:MaxPermSize=256m -classpath "${clazzpath}" com.app.AppMain $1 $2 $3"
  exec ${cmd}
echo "execute AppMain finish..................."

------------------------------------------------------