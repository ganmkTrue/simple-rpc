package com.simple.rpc.ioc;

import com.simple.rpc.config.annotation.RpcReference;
import com.simple.rpc.config.annotation.RpcService;
import com.simple.rpc.proxy.RemoteProxyFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.TypeVariable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yanhao
 * @date 2020/3/4
 * @description:
 */
public class ClassPathRegistry implements Registry {

    private final Map<Class<?>, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(64);

    @Override
    public void scan(String... basePackages) {

        for (String basePackage : basePackages) {
            doScan(basePackage);
        }
    }

    private void doScan(String basePackage) {
        String path = basePackage.replace(".", "/");
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resourceUrls = null;
        try {
            resourceUrls = cl != null ? cl.getResources(path)
                    : ClassLoader.getSystemResources(path);
        } catch (IOException e) {
            //异常
        }

        List<Class<?>> clazzs = new ArrayList<>();
        while (resourceUrls.hasMoreElements()) {
            URL url = resourceUrls.nextElement();
            addClass(clazzs, url.getPath(), basePackage);
        }

        clazzs.stream()
                .filter(this::filterByAnnotation)
                .forEach(ele -> {
                    BeanDefinition beanDefinition = new BeanDefinition();
                    beanDefinition.setClassName(ele.getName());
                    beanDefinitionMap.put(ele, beanDefinition);
                });
    }

    private boolean filterByAnnotation(Class<?> clazz) {
        return clazz.getAnnotation(RpcService.class) != null;
    }

    private void addClass(List<Class<?>> clazzs, String path, String basePackage) {
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        for (File subFile : files) {
            if (subFile.isDirectory()) {
                addClass(clazzs, subFile.getAbsolutePath(), basePackage + "." + subFile.getName());

            } else {
                String fileName = subFile.getName();
                String className = fileName.substring(0, fileName.indexOf('.'));
                try {
                    Class<?> aClass = Thread.currentThread().getContextClassLoader()
                            .loadClass(basePackage + "." + className);
                    clazzs.add(aClass);
                } catch (ClassNotFoundException e) {
                    //异常
                }
            }
        }
    }


    @Override
    public void register(BeanFactory beanFactory) {
        if (beanDefinitionMap.size() == 0) {
            return;
        }
        beanDefinitionMap.forEach((clazz, beanDefinition) -> {
            Object bean = createBean(clazz);
            beanFactory.registerBean(clazz, bean);
        });

    }

    private Object createBean(Class<?> clazz) {
        Object bean = null;
        try {
            bean = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                RpcReference annotation = field.getAnnotation(RpcReference.class);
                if (annotation != null) {
                    if (!Modifier.isStatic(field.getModifiers())) {
                        field.setAccessible(true);
                        //代理对象
                        Object proxy = getRemoteProxy(field.getType());
                        field.set(bean, proxy);
                    }
                }
            }

        } catch (InstantiationException | IllegalAccessException e) {
            //异常
        }
        return bean;
    }

    private Object getRemoteProxy(Class<?> clazz) {
        return RemoteProxyFactory.createRemoteProxy(clazz);
    }

}
