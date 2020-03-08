package com.simple.rpc.test;


import com.simple.rpc.config.annotation.RpcService;

@RpcService
public class ServiceAImpl implements ServiceA {
    private String name = "a";

    @Override
    public String say() {
        return "ni hao wo shi "+name;
    }

    public void testMessage(String name,Integer num) {
        System.out.println("testMessage:"+name+num);
    }

    @Override
    public Person getPerson() {
        Person person = new Person();
        person.setAge(23);
        person.setName("zhangsan");
        person.setSex("man");
        return person;
    }
}
