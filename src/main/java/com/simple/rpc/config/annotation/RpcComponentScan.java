package com.simple.rpc.config.annotation;

import com.simple.rpc.config.spring.SimpleRpcComponentScanRegistrar;
import org.springframework.context.annotation.Import;
import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(SimpleRpcComponentScanRegistrar.class)
public @interface RpcComponentScan {

    String[] basePackages() default {};
}
