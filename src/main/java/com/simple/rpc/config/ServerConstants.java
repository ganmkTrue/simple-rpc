package com.simple.rpc.config;

/**
 * @author yanhao
 * @date 2020/3/8
 * @description:
 */
public class ServerConstants {

    /**
     * 远程注册地址
     */
    private String remoteHost;

    /**
     * 远程注册端口
     */
    private int remotePort;

    /**
     * 应用端口
     */
    private int applicationPort;

    /**
     * 本地ip地址
     */
    private String localHost;


    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public int getApplicationPort() {
        return applicationPort;
    }

    public void setApplicationPort(int applicationPort) {
        this.applicationPort = applicationPort;
    }

    public String getLocalHost() {
        return localHost;
    }

    public void setLocalHost(String localHost) {
        this.localHost = localHost;
    }
}
