package com.simple.rpc.registry;

/**
 * @author yanhao
 * @date 2020/3/5
 * @description:
 */
public interface RemoteRegistry {

    /**
     * 将服务注册到远程中心
     * @param service
     */
    void registry(Class<?> service);


    /**
     * 订阅某个服务
     * @param service
     * @param consumer
     */
    void subscriberService(Class<?> service,Class<?> consumer);


}
