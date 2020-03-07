package com.simple.rpc.protocol.invoke;


public interface ResponseFuture  {


    /**
     * 获取返回值
     * @return
     */
    Object get() ;

    /**
     * 指定时间获取返回值
     * @param timeout
     * @return
     */
    Object get(int timeout);

    /**
     * 是否完成
     * @return
     */
    boolean isDone();
}
