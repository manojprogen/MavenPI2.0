/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.cacheLayer;

/**
 *
 * @author Prabal Pratap Singh @Use Depends of Singleton pattern for
 * ProgenCacheMap so one instance will be created at a time
 */
public class CacheLayer {

    private static ProgenCacheMap map = new ProgenCacheMap();
    private static CacheLayer cacheLayer = null;

    private CacheLayer() {
        // for Singlton class
    }

    public void setdata(String s, Object o) {
        map.put(s, o);
    }

    public Object getdata(String s) {
        return map.get(s);
    }

    public static CacheLayer getCacheInstance() {
        if (cacheLayer == null) {
            cacheLayer = new CacheLayer();
        }
        return cacheLayer;
    }
}
