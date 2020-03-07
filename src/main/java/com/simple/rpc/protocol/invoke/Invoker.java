package com.simple.rpc.protocol.invoke;

import com.google.protobuf.ByteString;
import com.simple.rpc.protocol.Message;
import com.simple.rpc.protocol.SerializationUtils;

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
                        .setParameterTypes(getParameterTypes(method.getParameterTypes()))
                        .setParameters(getParameters(parameters))
                        .build())
                .build();

    }

    private ByteString getParameterTypes(Class<?>[] parameterTypes) {
        if (parameterTypes == null || parameterTypes.length == 0) {
            return ByteString.copyFrom(new byte[0]);
        }
        return ByteString.copyFrom(SerializationUtils.serialize(parameterTypes));
    }

    private ByteString getParameters(Object[] parameters) {
        if (parameters == null || parameters.length == 0) {
            return ByteString.copyFrom(new byte[0]);
        }
        return ByteString.copyFrom(SerializationUtils.serialize(parameters));
    }

    public Message.Response doInvoke() {
        // netty 客户端
        //发送消息
        //同步、异步获取结果
        //返回信息

        return null;
    }
}
