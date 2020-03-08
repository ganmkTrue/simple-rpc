package com.simple.rpc.proxy;

import com.simple.rpc.protocol.invoke.Invoker;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author yanhao
 * @date 2020/3/4
 * @description:
 */
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
        Invoker invoker = new Invoker(method,args);

        return invoker.doInvoke().get();
    }


}
