package com.simple.rpc.ioc;


/**
 * @author yanhao
 * @date 2020/3/3
 * @description:
 */
public interface BeanFactory {

    /**
     * 从容器中获取bean
     * @param requiredType
     * @param <T>
     * @return
     */
    <T> T getBean(Class<T> requiredType);

    /**
     * 将bean注入到容器中
     * @param type
     * @param bean
     * @return
     */
    void registerBean(Class type,Object bean);

}
