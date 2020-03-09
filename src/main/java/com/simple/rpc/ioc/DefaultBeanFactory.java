package com.simple.rpc.ioc;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yanhao
 * @date 2020/3/3
 * @description:
 */
public class DefaultBeanFactory implements BeanFactory {



    private final Map<String, Object> beanContainer = new ConcurrentHashMap<>(64);



    public static DefaultBeanFactory getInstance(){ return Holder.instance; }

    private DefaultBeanFactory(){

    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getBean(Class<T> requiredType)  {
        return (T) getBeanByName(requiredType.getName());
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T> T getBeanByName(String className) {
        Object bean = beanContainer.get(className);
        if (bean == null){
            throw new RuntimeException("can't find class : "+className);
        }
        return (T) bean;
    }

    @Override
    public void registerBean(Class type, Object bean) {
        Objects.requireNonNull(bean);
        beanContainer.put(type.getName(),bean);
    }

    private static class Holder{
        private static DefaultBeanFactory instance = new DefaultBeanFactory();
    }


}
