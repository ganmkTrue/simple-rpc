package com.simple.rpc;

import com.simple.rpc.config.ServerConfig;
import com.simple.rpc.ioc.BeanFactory;
import com.simple.rpc.ioc.ClassPathRegistry;
import com.simple.rpc.ioc.DefaultBeanFactory;
import com.simple.rpc.ioc.LocalRegistry;
import com.simple.rpc.protocol.netty.server.NettyServer;
import com.simple.rpc.protocol.netty.server.handler.ServerInitializer;
import com.simple.rpc.registry.ZooKeeperRegistry;

public class ApplicationStart {

    private BeanFactory beanFactory;

    private LocalRegistry registry;

    private ServerConfig serverConfig;

    public ApplicationStart(ServerConfig serverConfig){
        this.serverConfig = serverConfig;
        ZooKeeperRegistry.init(serverConfig);
        this.beanFactory = DefaultBeanFactory.getInstance();
        this.registry = new ClassPathRegistry(ZooKeeperRegistry.instance());

    }

    public void run(String[] basePackages) {
        new NettyServer(serverConfig.getApplicationPort(), new ServerInitializer()).start();
        registry.scan(basePackages);
        registry.register(beanFactory);
    }


}
