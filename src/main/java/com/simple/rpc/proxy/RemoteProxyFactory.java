package com.simple.rpc.proxy;

import java.lang.reflect.Proxy;

public class RemoteProxyFactory {


    @SuppressWarnings("unchecked")
    public static <T> T createRemoteProxy(Class<T> clazz) {
        RemoteProxy proxy = RemoteProxy.instance();
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, proxy);
    }

}
