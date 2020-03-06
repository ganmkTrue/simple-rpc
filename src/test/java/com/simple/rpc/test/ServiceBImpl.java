package com.simple.rpc.test;

import com.simple.rpc.config.annotation.RpcReference;
import com.simple.rpc.config.annotation.RpcService;

@RpcService
public class ServiceBImpl implements ServiceB {

    @RpcReference
    private ServiceA serviceA;


    @Override
    public void say() {
        serviceA.say();
        System.out.println("我是B");
    }
}