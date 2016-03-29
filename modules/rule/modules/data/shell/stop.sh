#!/bin/bash
declare -i i=0;
for pid in `jps -lm | grep "com.rule.data.Main 3030" | awk '{print $1}'`;
do  echo `kill -9 $pid` $pid 'killed' 
    i+=1
done
echo 'total=' $i
