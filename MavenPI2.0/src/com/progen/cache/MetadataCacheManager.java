/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.cache;

//import com.progen.log.ProgenLog;
import com.progen.superadmin.UserAssignment;
import org.apache.log4j.Logger;

/**
 *
 * @author arun
 */
public enum MetadataCacheManager {

    MANAGER;
    public static Logger logger = Logger.getLogger(MetadataCacheManager.class);
    private CacheManager cacheManager;

    MetadataCacheManager() {
        cacheManager = CacheManager.getInstance();
    }

    public String retrieveUserName(Integer userId) {
        String cacheKey = "USERID_" + userId;
        String cachedObject = (String) cacheManager.retrieveObjectFromCache(CacheManager.CacheRegion.REPORT_METADATA_CACHE, cacheKey);
        if (cachedObject == null) {
            return null;
        } else {
            return cachedObject;
        }
    }

    public void storeUserName(Integer userId, String userName) {
//        ProgenLog.log(ProgenLog.FINE, this, "storeUserName", "Enter Method");
        logger.info("Enter Method");
        String cacheKey = "USERID_" + userId;
        cacheManager.storeObjectToCache(CacheManager.CacheRegion.REPORT_METADATA_CACHE, cacheKey, userName);

    }

    public UserAssignment retrieveUserAssignment(Integer userId) {
        String cacheKey = "USERASSIGNMENT_" + userId;
        UserAssignment cachedObject = (UserAssignment) cacheManager.retrieveObjectFromCache(CacheManager.CacheRegion.REPORT_METADATA_CACHE, cacheKey);
        return cachedObject;

    }

    public void storeUserAssignment(Integer userId, UserAssignment userAssignment) {
//       ProgenLog.log(ProgenLog.FINE, this, "storeUserAssignment", "Enter Method");
        logger.info("Enter Method");
        String cacheKey = "USERASSIGNMENT_" + userId;
        cacheManager.storeObjectToCache(CacheManager.CacheRegion.REPORT_METADATA_CACHE, cacheKey, userAssignment);

    }

    public void removeUserAssignment(Integer userId) {
        String cacheKey = "USERASSIGNMENT_" + userId;
        cacheManager.removeObjectFromCache(CacheManager.CacheRegion.REPORT_METADATA_CACHE, cacheKey);
    }
}
