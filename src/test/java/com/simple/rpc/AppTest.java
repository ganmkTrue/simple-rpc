package com.simple.rpc;


import com.simple.rpc.config.ServerConfig;
import com.simple.rpc.ioc.DefaultBeanFactory;
import com.simple.rpc.registry.ZkClient;
import com.simple.rpc.test.ServiceB;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {



    @Test
    public void iocTest() throws Exception{
        ServerConfig serverConfig =  new ServerConfig.Builder()
                .host("127.0.0.1")
                .port(2181)
                .build();
        ApplicationStart applicationStart = new ApplicationStart(serverConfig);
        applicationStart.run(new String[]{"com.simple.rpc.test"});
        ServiceB bean = DefaultBeanFactory.getInstance().getBean(ServiceB.class);
        Thread.sleep(2000);
        bean.say();
    }

    @Test
    public void zkClientTest() throws Exception {

        ZkClient zkClient = new ZkClient("127.0.0.1", 2181);

        String path = "/test/listener";
        zkClient.createPersistentNodeIfAbsent(path);

        PathChildrenCache pathChildrenCache = zkClient.createPathChildrenCacheIfNotExists(path);
        pathChildrenCache.start();
        zkClient.addPathChildrenCacheListener(pathChildrenCache,
                (client, event) -> {
                    ChildData data = event.getData();
                    switch (event.getType()) {
                        case CHILD_ADDED:
                            System.out.println("子节点增加, path=" + data.getPath());
                            break;

                        case CHILD_REMOVED:
                            System.out.println("子节点删除, path=" + data.getPath());
                            break;
                        default:
                            break;
                    }
                });

        Thread.sleep(1000);
        for (int i = 0; i < 3; i++) {
            zkClient.createEphemeralNode(path + "/id-" + i);
        }

        Thread.sleep(1000);
        for (int i = 0; i < 3; i++) {
            zkClient.delete(path + "/id-" + i);
        }

    }

}
