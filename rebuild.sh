#!/bin/bash

name=uzy-notice
jar="$name.jar"

echo "拉取新代码"
git pull
echo "以下为最新推送日志:"
echo "===================="
git log | head -n 10
echo "===================="

mvn clean install

port=`ps -ef | grep "$jar" | grep -v 'grep' | awk '{print $2}'`
echo "kill $port"
if [ -n "$port" ]; then
kill -9 $port
fi

cd $app
nohup java -Xmx=1g -Xms=100m -jar target/$jar --spring.profiles.active=prod > nohup.out 2>&1 &

sleep 1s
ps -ef | grep $jar | grep -v 'grep' | awk '{print $2}'

echo "启动notice成功"

