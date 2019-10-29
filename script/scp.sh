#!/bin/bash
# wbw-2018-1-19
#-----------目录可自己查看---------------
str1="/home/user/.config/witness/"
name="witness1"
path=$str1'/backup/'
SRC=$path
DIP=172.18.0.83
DEC=/data/file/qukuai/
date >>./rsync.log
echo "开始远程备份..."
#cd $SRC
#rsync -avz --exclude=".*" -e 'ssh -p 10022' nginx@$SIP:$SRC  $DEC  >>$logdir/rsync.log
rsync -avz --exclude=".*" -e 'ssh -p 10022' $SRC  rsync@$DIP:$DEC   >>./rsync.log
if [[ $? = 0 ]];
	then
	    curl http://fsafe.emaoedu.com:10035/v1/backup/message/$name/remote/0
		echo "success"
	else
	    curl http://fsafe.emaoedu.com:10035/v1/backup/message/$name/remote/1
		echo 'error'
fi
echo '远程備份文件結束...'
