#!/bin/bash
# wbw_201812171600

kill_jar(){

       kill -9 `ps -aux | grep "$jar_name" | grep -v "grep" | awk -F" " '{print $2}'`

}

jar_name=$1

kill_jar
ps -aux |grep java