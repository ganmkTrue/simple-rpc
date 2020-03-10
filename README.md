# simple-rpc
基于netty作为通讯框架、zookeeper作为注册中心，实现rpc的注册、监控、服务调用。

# 功能
- 自定义注解、内置ioc容器，注册指定包下的服务。
- 服务监听、本地缓存
- netty作为通讯框架、protobuf作为传输协议的数据结构提高服务通讯性能
- 服务同步调用

# todo

- 消费者管理
- 服务异步调用
- 负载均衡


[详细介绍](https://www.jianshu.com/p/571822ae6a90)
