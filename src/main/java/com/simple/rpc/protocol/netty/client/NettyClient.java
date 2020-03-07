package com.simple.rpc.protocol.netty.client;

import com.simple.rpc.protocol.netty.client.handler.ClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {

    private String host;
    private int port;

    Bootstrap bootstrap = new Bootstrap();

    NioEventLoopGroup workerGroup = new NioEventLoopGroup();

    public NettyClient(String host,int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {



        bootstrap
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ClientInitializer())
        ;

    }

    public Channel connect() throws InterruptedException{
        ChannelFuture future = bootstrap.connect(host, port).await();
        return future.channel();
    }
    public  void close(){
        workerGroup.shutdownGracefully();
    }

}
