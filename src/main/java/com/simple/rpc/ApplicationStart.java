package com.simple.rpc;

import com.simple.rpc.config.ServerConfig;
import com.simple.rpc.ioc.BeanFactory;
import com.simple.rpc.ioc.ClassPathRegistry;
import com.simple.rpc.ioc.DefaultBeanFactory;
import com.simple.rpc.ioc.LocalRegistry;
import com.simple.rpc.registry.ZooKeeperRegistry;

public class ApplicationStart {

    private BeanFactory beanFactory;
    private LocalRegistry registry;


    public ApplicationStart(ServerConfig serverConfig){
        ZooKeeperRegistry.init(serverConfig);
        this.beanFactory = DefaultBeanFactory.getInstance();
        this.registry = new ClassPathRegistry(ZooKeeperRegistry.instance());

    }

    public void run(String[] basePackages) {
        //start Server
        registry.scan(basePackages);
        registry.register(beanFactory);
    }


}
