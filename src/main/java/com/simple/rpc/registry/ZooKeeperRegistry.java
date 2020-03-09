package com.simple.rpc.registry;

import com.simple.rpc.config.ProviderCache;
import com.simple.rpc.config.ServerConfig;
import com.simple.rpc.registry.listener.ProviderChangeListener;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.xml.bind.Element;
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

    private String url ;



    public static void init(ServerConfig serverConfig) {
        zooKeeperRegistry = new ZooKeeperRegistry(serverConfig);

    }

    public static ZooKeeperRegistry instance() {
        Objects.requireNonNull(zooKeeperRegistry);
        return zooKeeperRegistry;
    }


    private ZooKeeperRegistry(ServerConfig serverConfig) {
        zkClient = new ZkClient(serverConfig.getHost(), serverConfig.getPort());
        url = serverConfig.getHost()+":"+serverConfig.getApplicationPort();
    }


    @Override
    public void registry(Class<?> service) {
        String name = "/"+service.getName();
        logger.info("registry service : {} path : {}",service.getName(),"/"+service.getName() + "/" + providers);
        try {
            zkClient.createPersistentNodeIfAbsent(name);
            zkClient.createPersistentNodeIfAbsent(name + "/" + providers);
            zkClient.createEphemeralNodeIfAbsent(name + "/" + providers + "/" + url);
        } catch (Exception e) {
            logger.error("provide : {} registry remote fail",name,e);
        }
    }

    @Override
    public void batchRegistry(List<Class<?>> services) {
        if (services == null || services.size() == 0){
            return;
        }
        for(Class<?> service :services){
            registry(service);
        }
    }

    @Override
    public void subscriberService(Class<?> service, Class<?> consumer) {

        try {
            String path = "/" + service.getName();
            zkClient.createPersistentNodeIfAbsent(path);
            zkClient.createPersistentNodeIfAbsent(path+ "/" + consumers);
            zkClient.createEphemeralNodeIfAbsent(path + "/" + consumers + "/" + url);
            logger.info("subscriber service : {} path : {}",service.getName(),path + "/" + providers);
            PathChildrenCache pathChildrenCache = zkClient
                    .createPathChildrenCacheIfNotExists("/"+service.getName() + "/" + providers);
            zkClient.addPathChildrenCacheListener(pathChildrenCache, new ProviderChangeListener(service.getName()));
        } catch (Exception e) {
            logger.error("consumer : {} subscriber provide : {}  fail",consumer.getName(),service.getName(),e);
        }

    }

    @Override
    public void pullProvider(Class<?> service) {
        String path = "/" + service.getName()+"/"+providers;
        try {
            if (!zkClient.checkExists(path)){
                return;
            }
            List<String> children = zkClient.getChildren(path);
            if(children != null && children.size() > 0){
                children = children.stream().map(c->path+ "/"+c).collect(Collectors.toList());
                ProviderCache.batchAdd(service.getName(),children);
            }

        } catch (Exception e) {
            logger.error("pullProvider : {}  fail",service.getName(),e);
        }
    }
}