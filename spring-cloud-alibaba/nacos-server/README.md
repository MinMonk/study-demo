# Nacos-Server搭建

## 前言

**下述指令需要在安装了docker-compose组件的机器上方可运行**

看需求启动不同的版本，当前目录下主要有单机版和集群版两个版本。
启动命令：`docker-compose up -d`

## 目录说明
- cluster： 该目录下存放的是集群版本的docker-compose文件，添加了Mysql的持久化。引用了同级目录env下的mysql配置文件
- env：该目录下存放的是公共的环境配置文件，当前存放着持久化Mysql的docker环境变量配置文件
- standalone: 该目录下存放的是单机版的docker-compose文件，添加了Mysql的持久化。引用了同级目录env下的mysql配置文件

## 启动单机版
1. 创建docker容器运行需要的挂载目录
```sh
mkdir -p volumes/cluster-logs/nacos
mkdir -p volumes/mysql
```
2. 进入单机版的docker-compose目录 `cd standalone`
3. 执行启动命令 `docker-compose up -d`

## 启动集群版
1. 创建docker容器运行需要的挂载目录
```sh
mkdir -p volumes/cluster-logs/nacos1
mkdir -p volumes/cluster-logs/nacos2
mkdir -p volumes/cluster-logs/nacos3

mkdir -p volumes/mysql
```
2. 进入集群版的docker-compose目录 `cd cluster`
3. 执行启动命令 `docker-compose up -d`




