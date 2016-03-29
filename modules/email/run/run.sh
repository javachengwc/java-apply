export LANG=zh_CN
export JAVA_HOME=/usr/local/jdk
workdir=/data/service/mailserver
classpath=
files=`find $workdir/lib -type f -name "*.jar"`
for i in $files
do
	classpath=$classpath$i:
done
classpath=$classpath$workdir/conf/
export CLASSPATH=$classpath
echo $classpath

$JAVA_HOME/bin/java com.email.server.MailServer > stdout.log &
