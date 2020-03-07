package com.simple.rpc.config;

/**
 * @author yanhao
 * @date 2020/3/5
 * @description:
 */
public class ServerConfig {

    /**
     * 远程注册地址
     */
    private String host;

    /**
     * 远程注册端口
     */
    private int port;

    /**
     * 应用端口
     */
    private int applicationPort;

    private ServerConfig(Builder builder) {
        setHost(builder.host);
        setPort(builder.port);
        setApplicationPort(builder.applicationPort);
    }


    public String getHost() {
        return host;
    }

    private void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    private void setPort(int port) {
        this.port = port;
    }

    public int getApplicationPort() {
        return applicationPort;
    }

    private void setApplicationPort(int applicationPort) {
        this.applicationPort = applicationPort;
    }


    public static final class Builder {

        private String host;
        private int port;
        private int applicationPort;

        public Builder() {
        }

        public Builder host(String val) {
            host = val;
            return this;
        }

        public Builder port(int val) {
            port = val;
            return this;
        }

        public Builder applicationPort(int val) {
            applicationPort = val;
            return this;
        }

        public ServerConfig build() {
            return new ServerConfig(this);
        }
    }
}
