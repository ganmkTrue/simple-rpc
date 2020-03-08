package com.simple.rpc.protocol.invoke;

import com.google.protobuf.ByteString;
import com.simple.rpc.protocol.netty.Message;
import com.simple.rpc.protocol.SerializationUtils;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yanhao
 * @date 2020/3/7
 * @description:
 */
public class DefaultFuture implements ResponseFuture {

    private static final int DEFAULT_TIMEOUT = 1000;

    private volatile Message.Response response;

    private final Lock lock = new ReentrantLock();
    private final Condition done = lock.newCondition();



    @Override
    public Object get() {
        return get(DEFAULT_TIMEOUT);
    }

    @Override
    public Object get(int timeout) {
        if (timeout <= 0) {
            timeout = DEFAULT_TIMEOUT;
        }
        if (!isDone()) {
            long start = System.currentTimeMillis();
            lock.lock();
            try {
                while (!isDone()) {
                    done.await(timeout, TimeUnit.MILLISECONDS);
                    if (isDone() || System.currentTimeMillis() - start > timeout) {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
        return parsResponse();
    }

    public boolean isDone() {
        return response != null;
    }

    public void received(Message.Response response) {
        DefaultFuture defaultFuture = InvokeConstants.remove(response.getId());
        if (defaultFuture != null) {
            doReceived(response);
        }
    }


    private void doReceived(Message.Response res) {
        lock.lock();
        try {
            response = res;
            done.signal();
        } finally {
            lock.unlock();
        }
    }

    private Object parsResponse() {
        if (!isDone()) {
            //超时异常
            throw new RuntimeException("服务调用超时");
        }
        ByteString data = response.getData();
        byte[] bytes = data.toByteArray();
        if (bytes.length == 0) {
            return null;
        }
        return SerializationUtils.deserialize(bytes);
    }


}
