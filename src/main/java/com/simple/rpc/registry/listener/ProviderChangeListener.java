package com.simple.rpc.registry.listener;

import com.simple.rpc.config.ProviderCache;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yanhao
 * @date 2020/3/6
 * @description:
 */
public class ProviderChangeListener implements PathChildrenCacheListener {

    private static final Logger logger = LoggerFactory.getLogger(ProviderChangeListener.class);

    private String providerName;



     public ProviderChangeListener(String providerName){
         this.providerName = providerName;
    }

    @Override
    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
        ChildData data = event.getData();
        String path = data.getPath();
        String[] split = path.split("/");
        String url = split[split.length - 1];
        switch (event.getType()) {
            case CHILD_ADDED:
                ProviderCache.add(providerName,path);
                logger.info("service : {} registry remote from : {}",providerName,url);
                break;
            case CHILD_REMOVED:
                ProviderCache.remove(providerName,path);
                logger.info("service : {} closed  from : {}",providerName,url);
                break;
            default:
                break;
        }
    }
}
