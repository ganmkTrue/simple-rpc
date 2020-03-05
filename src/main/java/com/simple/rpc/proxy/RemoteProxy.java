package com.simple.rpc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RemoteProxy implements InvocationHandler {


    private RemoteProxy() {
    }

    public static RemoteProxy instance() {
        return Holder.proxy;
    }

    private static class Holder {
        private static final RemoteProxy proxy = new RemoteProxy();
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("方法："+method.getName()+"已经动态增强");
        return null;
    }


}
