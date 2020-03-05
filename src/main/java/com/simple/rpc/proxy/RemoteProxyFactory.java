package com.simple.rpc.proxy;

import java.lang.reflect.Proxy;

public class RemoteProxyFactory {


    public static <T> T createRemoteProxy(Class<T> clazz){
        RemoteProxy proxy = RemoteProxy.instance();
        ClassLoader classLoader = clazz.getClassLoader();
        Class<?>[] interfaces = clazz.getInterfaces();
        T instance = (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] {clazz}, proxy);
        return  instance;
    }

}
