#!/bin/bash
#--------------------------------------------------------------------------
host="127.0.0.1"
port=$1
#--------------------------------------------------------------------------
ok="200"
url="http://$host:$port"
dir=`dirname $0`
cd $dir

while [ "1" = "1" ]
do
	a=`curl -o /dev/null -s -w "%{http_code}\n" --connect-timeout 3 -m 3 "$url"`
	time=`date  '+%Y-%m-%d %H:%M:%S|'`
	#echo $time $a $dir $url
	if [ "$a" = "$ok" ]
	then
	    echo $time "$url ok    200" >> monitor.history
	else
		echo $time "$url error $a restart" >> monitor.history
		source restart.sh &
	fi
	sleep 30s
done
#--------------------------------------------------------------------------
