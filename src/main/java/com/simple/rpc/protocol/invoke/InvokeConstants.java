package com.simple.rpc.protocol.invoke;

import com.simple.rpc.protocol.invoke.DefaultFuture;
import com.simple.rpc.protocol.invoke.ResponseFuture;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
