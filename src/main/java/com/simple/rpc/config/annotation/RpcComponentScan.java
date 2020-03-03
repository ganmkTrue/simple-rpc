package com.simple.rpc.config.annotation;


import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcComponentScan {

    String[] basePackages() default {};
}
