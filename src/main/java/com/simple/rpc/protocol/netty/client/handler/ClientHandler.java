package com.simple.rpc.protocol.netty.client.handler;

import com.simple.rpc.protocol.invoke.InvokeConstants;
import com.simple.rpc.protocol.netty.Message;
import com.simple.rpc.protocol.invoke.DefaultFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


/**
 * @author yanhao
 * @date 2020/3/7
 * @description:
 */
public class ClientHandler extends SimpleChannelInboundHandler<Message.RpcMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message.RpcMessage msg) throws Exception {
        Message.Response response = msg.getResponse();
        DefaultFuture future = InvokeConstants.getFuture(response.getId());
        future.received(response);
    }

}
