package com.simple.rpc.config;

import java.util.HashSet;
import java.util.List;
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



    public static void add(String providerName,String url){
        boolean containsKey = provider.containsKey(providerName);
        if(!containsKey){
            synchronized (ProviderCache.class){
                if(!provider.containsKey(providerName)){
                    provider.put(providerName,new HashSet<>());
                }
            }
        }
        provider.get(providerName).add(url);
    }

    public static void batchAdd(String providerName, List<String> urls){
        boolean containsKey = provider.containsKey(providerName);
        if(!containsKey){
            synchronized (ProviderCache.class){
                if(!provider.containsKey(providerName)){
                    provider.put(providerName,new HashSet<>());
                }
            }
        }
        provider.get(providerName).addAll(urls);
    }

    public static void remove(String providerName,String url){
        get(providerName).remove(url);
    }


    public static Set<String> get(String providerName){
        return provider.get(providerName);
    }


}
