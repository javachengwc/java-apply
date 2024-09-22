#!/usr/bin/env bash

mvn  -f ../pom.xml clean
mvn  -f ../pom.xml package -Dmaven.test.skip=true
## 复制jar包到当前目录
cp   ../target/*.jar  .

## 打镜像
docker build -t harbor-ip:port/zu/webcloudali-gateway:latest .
## 推送镜像到harbor镜像仓库
docker push harbor-ip:port/zu/webcloudali-gateway:latest

## 删除本地镜像
docker rmi harbor-ip:port/zu/webcloudali-gateway:latest
## 删除jar包
rm -rf *.jar