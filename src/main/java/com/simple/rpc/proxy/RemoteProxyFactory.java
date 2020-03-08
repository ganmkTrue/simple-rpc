package com.simple.rpc.proxy;

import java.lang.reflect.Proxy;

/**
 * @author yanhao
 * @date 2020/3/4
 * @description:
 */
public class RemoteProxyFactory {


    @SuppressWarnings("unchecked")
    public static <T> T createRemoteProxy(Class<T> clazz) {
        RemoteProxy proxy = RemoteProxy.instance();
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, proxy);
    }

}
