# Spring-cloud-alibaba
## 环境说明
### 版本信息
|groupId|artifactId|version|
|----|----|----|
|org.springframework.cloud|spring-cloud-dependencies|Hoxton.SR9|
|org.springframework.cloud|spring-boot-dependencies|Hoxton.SR9|
|com.alibaba.cloud|spring-cloud-alibaba-dependencies|Hoxton.SR9|

## Module简介
- hystrix-dashboard：Hystrix的监控仪表盘，端口为8090，访问地址: http://localhost:8090/hystrix
- nacos-config：从Nacos注册中心获取配置的一个demo，端口为8200
- nacos-server：搭建Nacos-Server的docker-compose脚本，详细参见该目录下的[README文档](./nacos-server/README.md)
- power：微服务集群（模拟【权限】微服务），端口为8000（引用了自定义组件**sentinel-persist-starter-zookeeper**）来实现sentinel配置的持久化
- power1：微服务集群（模拟【权限】微服务），端口为8001（引用了自定义组件**sentinel-persist-starter-nacos**）来实现sentinel配置的持久化
- user：微服务集群（模拟【用户】微服务），端口为8100，（引用了自定义组件**spring-cloud-starter-sentinel-persisit**）来实现sentinel配置的持久化
- sentinel-persistence-starter-nacos: 自定义spring-boot组件：sentinel持久化到Nacos注册中心
- sentinel-persistence-starter-zookeeper: 自定义spring-boot组件：sentinel持久化到Zookeeper注册中心
- spring-cloud-starter-sentinel-persisit: 自定义spring-boot组件：整合了上面两个组件，通过@EnablePersistence注解的type来决定持久化到哪里，目前支持Zookeeper和Nacos

## Nacos
官网地址: https://nacos.io/zh-cn/docs/what-is-nacos.html

### 启动方式
进到Nacos的bin目录，在bin目录下执行命令
#### windows

startup.cmd -m standalone

#### unix

startup -m standalone

### hystrix dashborad
访问地址：http://localhost:8100/actuator/hystrix.stream

添加依赖
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

添加配置
```yaml
management:
  endpoints:
    web:
      exposure:
        include: '*'
```