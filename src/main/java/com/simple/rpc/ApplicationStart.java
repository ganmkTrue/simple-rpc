package com.simple.rpc;

import com.simple.rpc.factory.BeanFactory;
import com.simple.rpc.factory.DefaultBeanFactory;

public class ApplicationStart {

    private BeanFactory beanFactory;

    public void run() {
        //获取当前启动类的目录
        run(new String[]{""});
    }

    public void run(String[] basePackages) {
        this.beanFactory = DefaultBeanFactory.getInstance();
        //server start
        //scan
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
