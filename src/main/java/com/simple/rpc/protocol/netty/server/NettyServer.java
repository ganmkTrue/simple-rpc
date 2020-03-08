package com.simple.rpc.protocol.netty.server;

import com.simple.rpc.config.ServerConstants;
import com.simple.rpc.ioc.BeanFactory;
import com.simple.rpc.ioc.DefaultBeanFactory;
import com.simple.rpc.protocol.netty.server.handler.ServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author yanhao
 * @date 2020/3/7
 * @description:
 */
public class NettyServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private int port;

    public NettyServer() {
        BeanFactory beanFactory = DefaultBeanFactory.getInstance();
        this.port = beanFactory.getBean(ServerConstants.class).getApplicationPort();
    }

    public void start() {
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        final ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(boosGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ServerInitializer())
                .bind(port).addListener(future -> {
            if (future.isSuccess()) {
                logger.info("{} : 服务启动端口 [{}] 绑定成功",new Date(),port);
            } else {
                logger.error("{} : 端口 [{}] 绑定失败",new Date(),port);
            }
        });
        ;

    }

}
