package com.simple.rpc.test;

public class ServiceAImpl implements ServerA{
    private String name = "a";

    @Override
    public void say() {
        System.out.println("我是A");
    }

}
