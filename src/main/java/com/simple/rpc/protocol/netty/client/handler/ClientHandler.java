package com.simple.rpc.protocol.netty.client.handler;

import com.simple.rpc.protocol.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<Message.RpcMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message.RpcMessage msg) throws Exception {
        com.google.protobuf.ByteString data = msg.getResponse().getData();
        data.toByteArray();
        System.out.println("服务器消息"+data);
    }

}
