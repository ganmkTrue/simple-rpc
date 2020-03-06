package com.simple.rpc.registry;

import com.simple.rpc.config.ServerConfig;
import com.simple.rpc.registry.listener.ProviderChangeListener;
import java.util.Objects;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author yanhao
 * @date 2020/3/5
 * @description:
 */
public class ZooKeeperRegistry implements RemoteRegistry {

    private static final Logger logger = LoggerFactory.getLogger(ZooKeeperRegistry.class);


    private static ZooKeeperRegistry zooKeeperRegistry = null;

    private ZkClient zkClient;

    private String providers = "providers";

    private String consumers = "consumers";

    private String ip = "127.0.0.1:10000";



    public static void init(ServerConfig serverConfig) {
        zooKeeperRegistry = new ZooKeeperRegistry(serverConfig);

    }

    public static ZooKeeperRegistry instance() {
        Objects.requireNonNull(zooKeeperRegistry);
        return zooKeeperRegistry;
    }


    private ZooKeeperRegistry(ServerConfig serverConfig) {
        zkClient = new ZkClient(serverConfig.getHost(), serverConfig.getPort());
    }


    @Override
    public void registry(Class<?> service) {
        String name = "/"+service.getName();
        logger.info("registry service : {} path : {}",service.getName(),"/"+service.getName() + "/" + providers);
        try {
            zkClient.createPersistentNodeIfAbsent(name);
            zkClient.createPersistentNodeIfAbsent(name + "/" + providers);
            zkClient.createEphemeralNodeIfAbsent(name + "/" + providers + "/" + ip);
        } catch (Exception e) {
            logger.error("provide : {} registry remote fail",name,e);
        }
    }

    @Override
    public void subscriberService(Class<?> service, Class<?> consumer) {

        try {
            zkClient.createPersistentNodeIfAbsent("/"+service.getName() + "/" + consumers);
            zkClient.createEphemeralNodeIfAbsent("/"+service.getName() + "/" + consumers + "/" + ip);
            logger.info("subscriber service : {} path : {}",service.getName(),"/"+service.getName() + "/" + providers);
            PathChildrenCache pathChildrenCache = zkClient
                    .createPathChildrenCacheIfNotExists("/"+service.getName() + "/" + providers);
            zkClient.addPathChildrenCacheListener(pathChildrenCache, new ProviderChangeListener(service.getName()));
        } catch (Exception e) {
            logger.error("consumer : {} subscriber provide : {}  fail",consumer.getName(),service.getName(),e);
        }


    }
}