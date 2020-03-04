package com.simple.rpc.ioc;

/**
 * @author yanhao
 * @date 2020/3/4
 * @description:
 */
public class BeanDefinition {

    private String className;

    private Class<?> clazz;


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }
}
