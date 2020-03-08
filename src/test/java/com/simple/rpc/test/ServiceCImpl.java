package com.simple.rpc.test;

import com.simple.rpc.config.annotation.RpcReference;

public class ServiceCImpl implements ServiceC{

    @RpcReference
    private ServiceA serviceA;

    @Override
    public Person getByRemote() {
        return serviceA.getPerson();
    }
}
