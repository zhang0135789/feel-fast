#!/bin/bash
# wbw_201812171600

#------------1-sourse env--------------
source /etc/profile

#------------2-kill jar----------------
jar_name=$1
#bash ./stop.sh $jar_name

#-------------3-start jar----------------
filename=$(basename $jar_name .jar)
java -jar ./$jar_name 1>/data/logs/$filename.log 2>> /data/logs/err_log/$filename_err.log &
echo 4
#---------------4- echo------------------
ps -aux |grep java