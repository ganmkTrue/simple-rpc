package com.simple.rpc.test;


import com.simple.rpc.config.annotation.RpcService;

@RpcService
public class ServiceAImpl implements ServiceA {
    private String name = "a";

    @Override
    public void say() {
        System.out.println("我是A");
    }

}
