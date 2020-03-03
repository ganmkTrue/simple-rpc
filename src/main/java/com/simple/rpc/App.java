package com.simple.rpc;

import com.simple.rpc.config.annotation.RpcComponentScan;

/**
 * Hello world!
 */
@RpcComponentScan(basePackages = "com.simple.rpc")
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
