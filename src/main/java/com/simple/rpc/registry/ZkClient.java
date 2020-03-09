package com.simple.rpc.registry;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * @author yanhao
 * @date 2020/3/6
 * @description:
 */
public class ZkClient {



    private CuratorFramework client;

    private Map<String,PathChildrenCache> childrenCacheMap = new ConcurrentHashMap<>();

    private final Object lock = new Object();


    public ZkClient(String host, int port) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder()
                .connectString(host + ":" + port)
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .namespace("simple-rpc")
                .retryPolicy(retryPolicy)
                .build();
        client.start();
    }


    /**
     * 创建永久节点
     */
    public void createPersistentNode(String path) throws Exception {
        client.create().withMode(CreateMode.PERSISTENT).forPath(path);

    }

    /**
     * 如果不存在创建永久节点
     */
    public void createPersistentNodeIfAbsent(String path) throws Exception {
        if (!checkExists(path)) {
            createPersistentNode(path);
        }

    }

    /**
     * 创建临时节点
     */
    public void createEphemeralNode(String path) throws Exception {
        client.create().withMode(CreateMode.EPHEMERAL).forPath(path);

    }

    /**
     * 创建临时节点
     */
    public void createEphemeralNodeIfAbsent(String path) throws Exception {
        if (!checkExists(path)) {
            createEphemeralNode(path);
        }
    }

    /**
     * 判断节点是否存在
     */
    public boolean checkExists(String path) throws Exception {
        Stat stat = client.checkExists().forPath(path);
        return stat != null;
    }


    /**
     * 删除节点
     */
    public void delete(String path) throws Exception {
        client.delete().forPath(path);
    }


    /**
     * 创建叶子节点缓存
     */
    public PathChildrenCache createPathChildrenCacheIfNotExists(String path) throws Exception {
        PathChildrenCache pathChildrenCache = childrenCacheMap.get(path);
        if (pathChildrenCache == null) {
            synchronized (lock) {
                if (childrenCacheMap.get(path) == null) {
                    pathChildrenCache = new PathChildrenCache(client, path, true);
                    pathChildrenCache.start(StartMode.BUILD_INITIAL_CACHE);
                    childrenCacheMap.putIfAbsent(path, pathChildrenCache);
                }
            }
        }
        return childrenCacheMap.get(path);

    }



    /**
     * 添加子节点监听
     */
    public void addPathChildrenCacheListener(PathChildrenCache cache,
            PathChildrenCacheListener listener) {
        cache.getListenable().addListener(listener);
    }

    /**
     * 获取某个节点的数据
     */
    public byte[] getData(String path) throws Exception {
        return client.getData().forPath(path);
    }

    /**
     * 获取某个目录的子节点
     * @param path
     * @return
     * @throws Exception
     */
    public List<String> getChildren(String path)throws Exception{
        return client.getChildren().forPath(path);
    }
}
