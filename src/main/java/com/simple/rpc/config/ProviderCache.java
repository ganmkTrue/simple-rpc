package com.simple.rpc.config;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yanhao
 * @date 2020/3/6
 * @description:
 */
public class ProviderCache {


    private static Map<String, Set<String>> provider = new ConcurrentHashMap<>(128);



    public static void add(String providerName,String value){
        boolean containsKey = provider.containsKey(providerName);
        if(!containsKey){
            synchronized (ProviderCache.class){
                if(!provider.containsKey(providerName)){
                    provider.put(providerName,new HashSet<>());
                }
            }
        }
        provider.get(providerName).add(value);
    }

    public static void reomve(String providerName,String value){
        get(providerName).remove(value);
    }


    public static Set<String> get(String providerName){
        return provider.get(providerName);
    }


}
