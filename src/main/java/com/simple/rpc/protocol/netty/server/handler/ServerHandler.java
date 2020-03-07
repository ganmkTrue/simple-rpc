package com.simple.rpc.protocol.netty.server.handler;

import com.google.protobuf.ByteString;
import com.simple.rpc.ioc.BeanFactory;
import com.simple.rpc.ioc.DefaultBeanFactory;
import com.simple.rpc.protocol.Message;
import com.simple.rpc.protocol.SerializationUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerHandler extends SimpleChannelInboundHandler<Message.RpcMessage> {


    private BeanFactory beanFactory = DefaultBeanFactory.getInstance();

    private ExecutorService executorService = Executors.newFixedThreadPool(4);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message.RpcMessage msg) throws Exception {
        executorService.execute(() -> invokeService(ctx, msg));
    }

    private void invokeService(ChannelHandlerContext ctx, Message.RpcMessage msg) {
        try {
            Message.Request request = msg.getRequest();
            String className = request.getClassName();
            Class<?> clazz = Class.forName(className);
            Object service = beanFactory.getBean(clazz);
            Class<?>[] parameterTypes = getParameterTypes(request.getParameterTypes().toByteArray());
            Object[] parameters = getParameters(request.getParameters().toByteArray());
            String methodName = request.getMethodName();
            Method method = clazz.getMethod(methodName, parameterTypes);
            Object invoke = method.invoke(service, parameters);

            byte[] data = new byte[0];
            if (!"void".equals(method.getReturnType().getName())) {
                data = SerializationUtils.serialize(invoke);
            }

            Message.RpcMessage rpcMessage = Message.RpcMessage.newBuilder()
                    .setDateType(Message.RpcMessage.Datatype.Response)
                    .setResponse(
                            Message.Response.newBuilder()
                                    .setId(request.getId())
                                    .setData(ByteString.copyFrom(data))
                            .build()
                    ).build();

            ctx.writeAndFlush(rpcMessage);

        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException
                | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }

    private Class<?>[] getParameterTypes(byte[] parameterTypes) {
        if (parameterTypes == null || parameterTypes.length == 0) {
            return null;
        }
        return SerializationUtils.deserialize(parameterTypes);
    }

    private Object[] getParameters(byte[] parameters) {
        if (parameters == null || parameters.length == 0) {
            return null;
        }
        return SerializationUtils.deserialize(parameters);
    }


}
