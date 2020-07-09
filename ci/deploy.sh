#!bin/bash
DATES=`date +%Y%m%d%H%M`
DATE=$DATES
SERVERLISTS=(web admin)
SERVICE=`git log -p -2 | grep diff | awk -F 'feel-' '{print $2}' | awk -F '/' '{print $1}' | sort -u |awk '{{printf"%s ",$0}}' | awk '{sub(/.$/,"")}1'`
for test in $SERVICE;do
    RESULT=`echo ${SERVERLISTS[*]} | grep -w ${test}`
    if [ ! -n "$RESULT" ]; then
        echo "${test}未存在前端服务列表中"
    else
        if [ ${test} == web ]; then
            ssh root@47.240.230.252 "sh /mnt/work/wallet-manage/wallet-manage/ci/npm.sh"
        else
            mvn clean install
            cd feel-${test}/
            mvn clean install
            /usr/bin/docker build -t docker.corebrew.net/feel-fast/${test}:$DATE .
            /usr/bin/docker push docker.corebrew.net/feel-fast/${test}:$DATE
            ssh root@47.240.230.252 "docker stop feel-${test} && docker rm feel-${test} && docker run -it -d --name=feel-${test} -p 10080:10080 docker.corebrew.net/feel-fast/feel-${test}:$DATE"
        fi
    fi
done
