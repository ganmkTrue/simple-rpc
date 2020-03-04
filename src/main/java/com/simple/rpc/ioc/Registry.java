package com.simple.rpc.ioc;

/**
 * @author yanhao
 * @date 2020/3/4
 * @description:
 */
public interface Registry {

    /**
     * 扫描指定包
     * @param basePackages
     */
    void scan(String... basePackages);

    /**
     * 将bean注册到指定工厂
     * @param beanFactory
     */
    void register(BeanFactory beanFactory);
}
