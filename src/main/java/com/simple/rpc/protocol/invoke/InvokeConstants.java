package com.simple.rpc.protocol.invoke;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yanhao
 * @date 2020/3/7
 * @description:
 */
public class InvokeConstants {

    private static final Map<String, DefaultFuture> FUTURES = new ConcurrentHashMap<>();


    public static void putFuture(String requestId, DefaultFuture responseFuture) {
        FUTURES.put(requestId, responseFuture);
    }

    public static DefaultFuture getFuture(String requestId) {
        return FUTURES.get(requestId);
    }

    public static DefaultFuture remove(String requestId) {
        return FUTURES.remove(requestId);
    }


}
