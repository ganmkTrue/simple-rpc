package com.simple.rpc.ioc;

public interface BeanFactory {

    /**
     * 从容器中获取bean
     * @param requiredType
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> T getBean(Class<T> requiredType) throws Exception;

    /**
     * 将bean注入到容器中
     * @param type
     * @param bean
     * @return
     */
    void registerBean(Class type,Object bean);

}
