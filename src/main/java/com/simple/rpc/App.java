package com.simple.rpc;

import com.simple.rpc.config.annotation.RpcComponentScan;
import com.simple.rpc.config.annotation.RpcReference;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Hello world!
 */
@RpcComponentScan(basePackages = "com.simple.rpc")
public class App {


    @RpcReference
    private String name;

    public static void main(String[] args) throws Exception {

        Class<?> clazz = Class.forName("com.simple.rpc.App");
        Field[] fields = clazz.getDeclaredFields();
        Object newInstance = clazz.newInstance();
        for (Field field : fields) {
            System.out.println(field.getName());
            System.out.println(field.getType());
            RpcReference annotation = field.getAnnotation(RpcReference.class);
            System.out.println(annotation);
            field.setAccessible(true);
            field.set(newInstance,"1568");
        }
        System.out.println(newInstance);
    }

    @Override
    public String toString() {
        return "App{" +
                "name='" + name + '\'' +
                '}';
    }
}
