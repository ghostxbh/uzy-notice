#!/bin/bash

name=uzy-notice
port=9100
jar="$name.jar"

echo "拉取新代码"
git pull
echo "以下为最新推送日志:"
echo "===================="
git log | head -n 10
echo "===================="

mvn clean install

echo "停止 $name 容器"
docker ps | grep "$name"  | awk '{print $1}' | xargs docker stop

echo "删除 $name 容器"
docker ps | grep "$name"  | awk '{print $1}' | xargs docker rm

echo "删除 $name 镜像"
docker images | grep "$name"  | awk '{print $1}' | xargs docker rmi

echo "打包镜像"
docker build -t $name .

echo "运行容器"
docker run -d -p $port:$port $name

echo "启动notice成功"

