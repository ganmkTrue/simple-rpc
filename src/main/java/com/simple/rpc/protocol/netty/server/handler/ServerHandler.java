package com.simple.rpc.protocol.netty.server.handler;

import com.google.protobuf.ByteString;
import com.simple.rpc.ioc.BeanFactory;
import com.simple.rpc.ioc.DefaultBeanFactory;
import com.simple.rpc.protocol.Message;
import com.simple.rpc.protocol.SerializationUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;

public class ServerHandler extends SimpleChannelInboundHandler<Message.RpcMessage> {


    BeanFactory beanFactory = DefaultBeanFactory.getInstance();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message.RpcMessage msg) throws Exception {
        Message.Request request = msg.getRequest();
        String className = request.getClassName();
        Class<?> clazz = Class.forName(className);

        Object service = beanFactory.getBean(clazz);
        Class<?>[] parameterTypes = SerializationUtils.deserialize(request.getParameterTypes().toByteArray());
        Object[] parameters = SerializationUtils.deserialize(request.getParameters().toByteArray());
        String methodName = request.getMethodName();
        Method method = clazz.getMethod(methodName, parameterTypes);

        Object invoke = method.invoke(service, parameters);

        byte[] data = new byte[0];
        if(invoke == null){

        }

        Message.RpcMessage rpcMessage = Message.RpcMessage.newBuilder().setDateType(Message.RpcMessage.Datatype.Response)
                .setResponse(Message.Response.newBuilder().setId(request.getId()).setData(ByteString.copyFrom("你好".getBytes()))
                        .build()).build();

        ctx.writeAndFlush(rpcMessage);
    }

}
