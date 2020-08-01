#!/bin/bash

group=$1
name=uzy-notice
app=$group/$name
jar="$name.jar"

cd $app

echo "拉取新代码"
git pull
echo "以下为最新推送日志:"
echo "===================="
git log | head -n 10
echo "===================="

mvn clean package

port=`ps -ef | grep "$jar" | grep -v 'grep' | awk '{print $2}'`
echo "kill $port"
if [ -n "$port" ]; then
kill -9 $port
fi

cd $app
nohup java -Xmx1024m -Xms100m -jar $app/target/$jar &

sleep 1s
ps -ef | grep $jar | grep -v 'grep' | awk '{print $2}'

echo "启动notice成功"

