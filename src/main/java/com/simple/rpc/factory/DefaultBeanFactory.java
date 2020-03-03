package com.simple.rpc.factory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanFactory implements BeanFactory {

    private final Map<Class<?>, Object> beanContainer = new ConcurrentHashMap<>(64);


    public static DefaultBeanFactory getInstance(){ return Holder.instance; }


    @Override
    public <T> T getBean(Class<T> requiredType) throws Exception {
        Object bean = beanContainer.get(requiredType);
        if (bean == null){
            //异常
        }
        return (T) bean;
    }

    @Override
    public void registerBean(Class type, Object bean) {
        Objects.requireNonNull(bean);
        beanContainer.putIfAbsent(type,bean);
    }

    private static class Holder{
        private static DefaultBeanFactory instance = new DefaultBeanFactory();
    }

}
