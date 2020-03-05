package com.simple.rpc;



import com.simple.rpc.ioc.DefaultBeanFactory;
import com.simple.rpc.test.ServerBImpl;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
    @Test
    public void iocTest() {
        ApplicationStart applicationStart = new ApplicationStart();
        applicationStart.run(new String[]{"com.simple.rpc"});
        ServerBImpl bean = DefaultBeanFactory.getInstance().getBean(ServerBImpl.class);
        bean.say();
    }
}
