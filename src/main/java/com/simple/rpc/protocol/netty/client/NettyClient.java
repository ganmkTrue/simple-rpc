package com.simple.rpc.protocol.netty.client;

import com.simple.rpc.protocol.netty.client.handler.ClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.TimeUnit;

public class NettyClient {

    private ChannelHandler childHandler;
    private Channel channel;
    private String host = "127.0.0.1";
    private int port = 8000;

    public NettyClient(ChannelHandler childHandler) {
        this.childHandler = childHandler;
        start();
    }

    private void start() {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ClientInitializer()
                )
                .connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                Channel channel = ((ChannelFuture) future).channel();
                this.channel = channel;
                System.out.println("连接通道:" + channel);
            }

        });
    }

    public Channel getChannel() {
        while (channel == null) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {

            }

        }
        return channel;
    }
}
