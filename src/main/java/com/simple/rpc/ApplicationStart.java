package com.simple.rpc;

import com.simple.rpc.ioc.BeanFactory;
import com.simple.rpc.ioc.ClassPathRegistry;
import com.simple.rpc.ioc.DefaultBeanFactory;
import com.simple.rpc.ioc.Registry;

public class ApplicationStart {

    private BeanFactory beanFactory;
    private Registry registry;

    public void run() {
        //获取当前启动类的目录
        run(new String[]{""});
    }

    public ApplicationStart(){
        this.beanFactory = DefaultBeanFactory.getInstance();
        this.registry = new ClassPathRegistry();

    }

    public void run(String[] basePackages) {
        //start Server
        registry.scan(basePackages);
        registry.register(beanFactory);
    }

    private void scan(String[] basePackages) {
        for (String basePackage : basePackages) {
            doScan(basePackage);
        }
    }


    private void doScan(String basePackage) {
        //register Provider @service ? register
        registerProvider();
        //else
        setRemoteProxy();
    }

    private void registerProvider() {
        //init
        setRemoteProxy();
    }

    private void setRemoteProxy() {

    }


}
