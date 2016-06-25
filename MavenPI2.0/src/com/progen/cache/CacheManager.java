/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.cache;

//import com.progen.log.ProgenLog;
import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.apache.log4j.Logger;

/**
 *
 * @author arun
 */
public class CacheManager {

    public static Logger logger = Logger.getLogger(CacheManager.class);
    private static volatile CacheManager manager;
    private static volatile JCS repQueryCache;
    private static volatile JCS repMetadataCache;
    private static int checkedOut = 0;

    enum CacheRegion {

        REPORT_QUERY_CACHE, REPORT_METADATA_CACHE
    };

    private CacheManager() {
        try {
            repQueryCache = JCS.getInstance("reportQuery");
            repMetadataCache = JCS.getInstance("metadata");
        } catch (CacheException ex) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "CacheManager()","Failed to initialize Cache");
            logger.error("Failed to initialize Cache", ex);
        }
    }

    public static CacheManager getInstance() {
        synchronized (CacheManager.class) {
            if (manager == null) {
                manager = new CacheManager();
            }
        }

        synchronized (manager) {
            CacheManager.checkedOut++;
        }

        return manager;
    }

    void storeObjectToCache(CacheRegion cacheRegion, String key, Object data) {
        try {
            if (cacheRegion == CacheRegion.REPORT_QUERY_CACHE) {
                repQueryCache.remove(key);
                repQueryCache.put(key, data);
            } else {
                repMetadataCache.remove(key);
                repMetadataCache.put(key, data);
            }
        } catch (CacheException ex) {
//            ProgenLog.log(ProgenLog.FINE, this, "removeObjectFromCache", "Key not in Cache "+key);
            logger.error("Key not in Cache: " + key, ex);
        }
    }

    Object retrieveObjectFromCache(CacheRegion cacheRegion, String key) {
        Object data = null;
        if (cacheRegion == CacheRegion.REPORT_QUERY_CACHE) {
            data = repQueryCache.get(key);
        } else {
            data = repMetadataCache.get(key);
        }
        return data;
    }

    boolean removeObjectFromCache(CacheRegion cacheRegion, String key) {
        boolean removed = true;
        try {
            if (cacheRegion == CacheRegion.REPORT_QUERY_CACHE) {
                repQueryCache.remove(key);
            } else {
                repMetadataCache.remove(key);
            }
        } catch (CacheException ex) {
//            ProgenLog.log(ProgenLog.FINE, this, "removeObjectFromCache", "Key not in Cache "+key);
            logger.error("Key not in Cache: " + key, ex);
            removed = false;
        }
        return removed;
    }
}
