# uzy-notice
柚子云通知服务

## 架构
- JDK 1.8
- MongoDB 3.4
- SpringBoot 2.3.2
- swagger 3.0.0

- 前端
    + thymeleaf模版
    + layui
    + jQuery
    + bootstrap

## 功能描述
- 测试接口数据流出
- 接口数据JSON格式化
- 流出数据通知

## 测试地址
[通知列表](http://127.0.0.1:9100/notice)

## 接口参考
[restApi](http://127.0.0.1:9100/notice/swagger-ui/index.html)

## 部署方案

### shell脚本
```shell script
bash rebuild.sh
```

### docker
```shell script
# 1.创建镜像
docker build -t uzy-notice .

# 2.启动
docker run -d -p 9100:9100 uzy-notice

# 3.查看状态
docker ps
docker logs -f uzy-notice
```
