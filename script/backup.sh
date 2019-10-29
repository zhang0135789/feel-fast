#!/usr/bin/env bash
str1="/home/user/.config/witness/"
name="witness1"
file=`date +"%Y/%m/%d"`'/'$name
path=$str1'/backup/'$file
echo '开始創建本地备份数据文件夾...',$path
mkdir -p $path
echo '开始本地备份数据文件...'
str=`cp $str1/edu.sqlite $path/edu.sqlite`
if [[ $? = 0 ]];
	then
	    curl http://fsafe.emaoedu.com:10035/v1/backup/message/$name/local/0
		echo "success"
	else
	    curl http://fsafe.emaoedu.com:10035/v1/backup/message/$name/local/1
		echo 'error'
fi
echo '本地备份结束備份文件結束...'
