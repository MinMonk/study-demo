# 自定义servlet实现反向代理

源码参考github https://github.com/mitre/HTTP-Proxy-Servlet/

## 当前Demo工程各模块功能介绍
- web1: springMvc + JSP工程实现的反向代理
- web2: 代理的目标地址
- web3: springBoot + thymeleaf实现的反向代理
- web4: 在springboot项目中借助controller中实现反向代理（在SpringBoot的项目中，如果需要代理Post请求，猜想应该也需要像**web5**一样，用wrapper类对request进行包装）
- web5: 在springMvc项目中借助controller中实现反向代理（由于请求经过SpringMVC的控制器，请求中的Body参数已经被读取走了，需要添加Filter，在Filter中通过Wrapper类包装下Request，将InputStreamCopy一份，以便于在ProxyService类中copy请求体的时候，拿到的请求体不为空）---->  这个工程下的ProxyService类才是最终的优化版，其他只是衍化的一个过程，已经测试的demo
