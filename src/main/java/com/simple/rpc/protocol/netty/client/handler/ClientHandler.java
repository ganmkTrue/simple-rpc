package com.simple.rpc.protocol.netty.client.handler;

import com.simple.rpc.protocol.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler extends SimpleChannelInboundHandler<Message.RpcMessage> {


    public static Map<String,Message.Response> che = new ConcurrentHashMap<>(1024);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message.RpcMessage msg) throws Exception {
        Message.Response response = msg.getResponse();
        che.put(response.getId(),response);
    }

}
