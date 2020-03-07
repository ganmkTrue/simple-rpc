package com.simple.rpc.protocol.invoke;

import com.google.protobuf.ByteString;
import com.simple.rpc.protocol.Message;
import com.simple.rpc.protocol.SerializationUtils;
import com.simple.rpc.protocol.netty.client.NettyClient;
import io.netty.channel.Channel;
import java.lang.reflect.Method;
import java.util.UUID;

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
            NettyClient client = new NettyClient("127.0.0.1", 8899);
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
}
