package com.simple.rpc.protocol.invoke;

import com.google.protobuf.ByteString;
import com.simple.rpc.config.ProviderCache;
import com.simple.rpc.protocol.netty.Message;
import com.simple.rpc.protocol.SerializationUtils;
import com.simple.rpc.protocol.netty.client.NettyClient;
import io.netty.channel.Channel;
import jdk.nashorn.internal.ir.ReturnNode;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

/**
 * @author yanhao
 * @date 2020/3/7
 * @description:
 */
public class Invoker {

    private Message.RpcMessage request;

    public Invoker(Method method, Object[] parameters) {
        request = Message.RpcMessage.newBuilder()
                .setDateType(Message.RpcMessage.Datatype.Request)
                .setRequest(Message.Request.newBuilder()
                        .setId(UUID.randomUUID().toString())
                        .setClassName(method.getDeclaringClass().getName())
                        .setMethodName(method.getName())
                        .setParameterTypes(convertByteString(method.getParameterTypes()))
                        .setParameters(convertByteString(parameters))
                        .build())
                .build();

    }

    private ByteString convertByteString(Object[] parameters) {
        if (parameters == null || parameters.length == 0) {
            return ByteString.copyFrom(new byte[0]);
        }
        return ByteString.copyFrom(SerializationUtils.serialize(parameters));
    }


    public DefaultFuture doInvoke() {
        try {
            NettyClient client = createClient();
            client.start();
            Channel channel = client.connect();
            DefaultFuture future = new DefaultFuture();
            InvokeConstants.putFuture(request.getRequest().getId(),future);
            channel.writeAndFlush(request);
            return future;
        } catch (Exception e) {
            InvokeConstants.remove(request.getRequest().getId());
            throw new RuntimeException(e);
        }
    }

    private NettyClient createClient(){
        String providerUrl = getProviderUrl();
        String[] split = providerUrl.split(":");
        int port = Integer.valueOf(split[1]);
        int i = split[0].lastIndexOf("/");
        String host = split[0].substring(i+1);
        return new NettyClient(host, port);
    }

    private String getProviderUrl(){
        Set<String> providerUrls = ProviderCache.get(request.getRequest().getClassName());
        if(providerUrls == null || providerUrls.size() == 0 ){
            throw new RuntimeException("no provider for service : " + request.getRequest().getClassName());
        }
        // todo 服务负载策略
        return providerUrls.iterator().next();
    }
}
