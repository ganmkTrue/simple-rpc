package com.simple.rpc.ioc;

import com.simple.rpc.config.annotation.RpcReference;
import com.simple.rpc.config.annotation.RpcService;
import com.simple.rpc.proxy.RemoteProxyFactory;

import com.simple.rpc.registry.RemoteRegistry;
import com.simple.rpc.registry.ZooKeeperRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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
public class ClassPathRegistry implements LocalRegistry {

    private static final Logger logger = LoggerFactory.getLogger(ClassPathRegistry.class);

    private RemoteRegistry remoteRegistry;

    private List<Class<?>> clazzs = new ArrayList<>();

    public ClassPathRegistry(RemoteRegistry remoteRegistry) {
        this.remoteRegistry = remoteRegistry;
    }


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
            logger.error("scan package: {} error ",basePackage,e);
        }

        while (resourceUrls.hasMoreElements()) {
            URL url = resourceUrls.nextElement();
            addClass(clazzs, url.getPath(), basePackage);
        }

    }

    private boolean isProvider(Class<?> clazz) {
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
                    if (!aClass.isInterface()) {
                        clazzs.add(aClass);
                    }
                } catch (ClassNotFoundException e) {
                    logger.error("scan package: {} error",e);
                }
            }
        }
    }


    @Override
    public void register(BeanFactory beanFactory) {
        if (clazzs.size() == 0) {
            return;
        }
        for (Class<?> clazz : clazzs) {
            Object bean = createBean(clazz);
            Class<?>[] interfaces = clazz.getInterfaces();
            if (interfaces == null || interfaces.length == 0) {
                beanFactory.registerBean(clazz, bean);
            } else {
                for (Class<?> inter : interfaces) {
                    beanFactory.registerBean(inter, bean);
                }
            }
        }
        for (Class<?> clazz : clazzs) {
            Class<?>[] interfaces = clazz.getInterfaces();
            if (isProvider(clazz)) {
                if (interfaces == null || interfaces.length == 0) {
                    remoteRegistry.registry(clazz);
                } else {
                    for (Class<?> inter : interfaces) {
                        remoteRegistry.registry(inter);
                    }
                }
            }
        }
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
                        //create remote proxy
                        Object proxy = getRemoteProxy(field.getType());
                        field.set(bean, proxy);
                        remoteRegistry.subscriberService(field.getType(), clazz);
                    }
                }
            }

        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("create bean {} fail ", clazz.getName(), e);
            throw new RuntimeException(e);
        }
        return bean;
    }

    private Object getRemoteProxy(Class<?> clazz) {
        return RemoteProxyFactory.createRemoteProxy(clazz);
    }

}
