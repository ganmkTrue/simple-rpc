package com.simple.rpc;

import com.simple.rpc.config.ServerConfig;
import com.simple.rpc.config.ServerConstants;
import com.simple.rpc.config.annotation.RpcService;
import com.simple.rpc.ioc.BeanFactory;
import com.simple.rpc.ioc.ClassPathRegistry;
import com.simple.rpc.ioc.DefaultBeanFactory;
import com.simple.rpc.ioc.LocalRegistry;
import com.simple.rpc.protocol.netty.server.NettyServer;
import com.simple.rpc.protocol.netty.server.handler.ServerInitializer;
import com.simple.rpc.registry.RemoteRegistry;
import com.simple.rpc.registry.ZooKeeperRegistry;

/**
 * @author yanhao
 * @date 2020/3/7
 * @description:
 */
public class ApplicationStart {

    private BeanFactory beanFactory;

    private LocalRegistry registry;


    public ApplicationStart(ServerConfig serverConfig){
        this.beanFactory = DefaultBeanFactory.getInstance();
        initServerConstants(serverConfig);
        ZooKeeperRegistry.init(serverConfig);
        this.registry = new ClassPathRegistry(ZooKeeperRegistry.instance());
    }

    private void initServerConstants(ServerConfig serverConfig) {
        ServerConstants serverConstants = new ServerConstants();
        serverConstants.setApplicationPort(serverConfig.getApplicationPort());
        serverConstants.setRemoteHost(serverConfig.getHost());
        serverConstants.setRemotePort(serverConfig.getPort());
        serverConstants.setLocalHost(serverConfig.getHost());
        beanFactory.registerBean(ServerConstants.class,serverConstants);
    }

    public void run(String[] basePackages) {
        new NettyServer().start();
        registry.scan(basePackages);
        registry.register(beanFactory);
    }



}
