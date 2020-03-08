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



    private final Map<Class<?>, Object> beanContainer = new ConcurrentHashMap<>(64);



    public static DefaultBeanFactory getInstance(){ return Holder.instance; }

    private DefaultBeanFactory(){

    }

    @Override
    public <T> T getBean(Class<T> requiredType)  {
        Object bean = beanContainer.get(requiredType);
        if (bean == null){
            throw new RuntimeException("can't find class : "+requiredType.getName());
        }
        return (T) bean;
    }


    @Override
    public void registerBean(Class type, Object bean) {
        Objects.requireNonNull(bean);
        beanContainer.put(type,bean);
    }

    private static class Holder{
        private static DefaultBeanFactory instance = new DefaultBeanFactory();
    }


}
