package com.simple.rpc;


import com.google.protobuf.ByteString;
import com.simple.rpc.config.ServerConfig;
import com.simple.rpc.ioc.DefaultBeanFactory;
import com.simple.rpc.protocol.Message;
import com.simple.rpc.protocol.SerializationUtils;
import com.simple.rpc.registry.ZkClient;
import com.simple.rpc.test.ServiceAImpl;
import com.simple.rpc.test.ServiceB;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Unit test for simple rpc.
 */
public class RpcTest {



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

    @Test
    public void protobufTest() throws Exception{
        ServiceAImpl serviceA = new ServiceAImpl();
        Method method = serviceA.getClass().getMethod("testMessage", String.class,Integer.class);
        Method say = serviceA.getClass().getMethod("say");
        Class<?>[] parameterTypes1 = say.getParameterTypes();
        Class<?> returnType = say.getReturnType();
        System.out.println(parameterTypes1.length);
        System.out.println(returnType.getName());
         Object[] parameters = new Object[]{"ni hao",Integer.valueOf(5)};
        Message.RpcMessage request = Message.RpcMessage.newBuilder()
                .setDateType(Message.RpcMessage.Datatype.Request)
                .setRequest(Message.Request.newBuilder()
                        .setId(UUID.randomUUID().toString())
                        .setClassName(serviceA.getClass().getName())
                        .setMethodName(method.getName())
                        .setParameterTypes(ByteString.copyFrom(SerializationUtils.serialize(method.getParameterTypes())))
                        .setParameters(ByteString.copyFrom(SerializationUtils.serialize(parameters)))
                        .build())
                .build();

        Class<?>[] parameterTypes = SerializationUtils.deserialize(request.getRequest().getParameterTypes().toByteArray());

        Object[] deParameters = SerializationUtils.deserialize(request.getRequest().getParameters().toByteArray());

    }

    @Test
    public void rpcTest() {
        ServerConfig serverConfig =  new ServerConfig.Builder()
                .host("127.0.0.1")
                .port(2181)
                .applicationPort(8899)
                .build();
        ApplicationStart applicationStart = new ApplicationStart(serverConfig);
        applicationStart.run(new String[]{"com.simple.rpc.test"});
        ServiceB bean = DefaultBeanFactory.getInstance().getBean(ServiceB.class);
        bean.say();
    }

}
