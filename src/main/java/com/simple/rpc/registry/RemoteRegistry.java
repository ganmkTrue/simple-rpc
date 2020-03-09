package com.simple.rpc.registry;

import java.util.List;

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
     * 批量服务注册到远程中心
     * @param services
     */
    void batchRegistry(List<Class<?>> services);


    /**
     * 订阅某个服务
     * @param service
     * @param consumer
     */
    void subscriberService(Class<?> service,Class<?> consumer);

    /**
     * 从远处拉取服务到本地缓存
     * @param service
     */
    void pullProvider(Class<?> service);


}
