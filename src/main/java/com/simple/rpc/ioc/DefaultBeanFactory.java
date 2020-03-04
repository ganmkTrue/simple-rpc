package com.simple.rpc.ioc;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanFactory implements BeanFactory,Registry {

    private final Map<Class<?>, Object> beanContainer = new ConcurrentHashMap<>(64);

    private Registry registry;

    public static DefaultBeanFactory getInstance(){ return Holder.instance; }

    public DefaultBeanFactory(){
        registry = new ClassPathRegistry();
    }

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
        beanContainer.put(type,bean);
    }

    private static class Holder{
        private static DefaultBeanFactory instance = new DefaultBeanFactory();
    }


    @Override
    public void register(BeanFactory beanFactory) {
        registry.register(beanFactory);
    }

    @Override
    public void scan(String... basePackages) {
        registry.scan(basePackages);
    }
}
