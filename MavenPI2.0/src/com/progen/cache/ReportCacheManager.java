/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.cache;

import com.google.common.collect.ArrayListMultimap;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.apache.log4j.Logger;
import prg.db.PbReturnObject;
import prg.db.QueryResultValObj;

/**
 *
 * @author arun
 */
public enum ReportCacheManager {

    MANAGER;
    public static Logger logger = Logger.getLogger(ReportCacheManager.class);
    private CacheManager cacheManager;

    ReportCacheManager() {
        cacheManager = CacheManager.getInstance();
    }

    private String generateCacheKeyForReportQuery(String reportId, String repQuery) {
//        ProgenLog.log(ProgenLog.FINE, this, "generateCacheKeyForReportQuery", "Enter Method");
        logger.info("Enter Method");
        MessageDigest md;
        String key = reportId + repQuery;
        try {
            md = MessageDigest.getInstance("SHA1");
            String input = key;
            md.update(input.getBytes());
            byte[] output = md.digest();
            key = bytesToHex(output);
        } catch (NoSuchAlgorithmException ex) {
//            ProgenLog.log(ProgenLog.FINE, this, "generateCacheKeyForReportQuery", "NoSuchAlgorithmException");
            logger.info("NoSuchAlgorithmException");
        }
//        ProgenLog.log(ProgenLog.FINE, this, "generateCacheKeyForReportQuery", "Return Key " + key);
        logger.info("Return Key " + key);
        return key;
    }

    private String bytesToHex(byte[] b) {
        char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        StringBuffer buf = new StringBuffer();
        for (int j = 0; j < b.length; j++) {
            buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
            buf.append(hexDigit[b[j] & 0x0f]);
        }
        return buf.toString();
    }

    public PbReturnObject retrieveReportQry(String reportId, String query) {
//        ProgenLog.log(ProgenLog.FINE, this, "retrieveReportQry", "Enter Method");
        logger.info("Enter Method");
        String cacheKey = this.generateCacheKeyForReportQuery(reportId, query);
        QueryResultValObj cachedObject = (QueryResultValObj) cacheManager.retrieveObjectFromCache(CacheManager.CacheRegion.REPORT_QUERY_CACHE, cacheKey);
        if (cachedObject == null) {
            return null;
        } else {
            return cachedObject.getReturnObject();
        }
    }

    public void storeReportQry(String reportId, String query, PbReturnObject retObj) {
//        ProgenLog.log(ProgenLog.FINE, this, "storeReportQry", "Enter Method");
        logger.info("Enter Method");
        String cacheKey = this.generateCacheKeyForReportQuery(reportId, query);
        QueryResultValObj cachedObject = new QueryResultValObj(retObj);
        cacheManager.storeObjectToCache(CacheManager.CacheRegion.REPORT_QUERY_CACHE, cacheKey, cachedObject);
        this.updateReportQueryTracker(reportId, cacheKey);
    }

    public void updateReportQueryTracker(String reportId, String cacheKey) {
//        ProgenLog.log(ProgenLog.FINE, this, "updateReportQueryTracker", "Enter Method "+reportId+" "+cacheKey);
        logger.info("Enter Method" + reportId + " " + cacheKey);
        ArrayListMultimap<String, String> reportQryTracker;
        reportQryTracker = (ArrayListMultimap<String, String>) cacheManager.retrieveObjectFromCache(CacheManager.CacheRegion.REPORT_METADATA_CACHE, reportId);
        if (reportQryTracker == null) {
            reportQryTracker = ArrayListMultimap.create();
        }

        reportQryTracker.put(reportId, cacheKey);
        cacheManager.storeObjectToCache(CacheManager.CacheRegion.REPORT_METADATA_CACHE, reportId, reportQryTracker);
    }

    public void clearReportQueryCache(String reportId) {
//        ProgenLog.log(ProgenLog.FINE, this, "clearReportQueryCache", "Enter Method "+reportId+" "+reportId);
        logger.info("Enter Method" + reportId + " " + reportId);
        ArrayListMultimap<String, String> reportQryTracker;
        reportQryTracker = (ArrayListMultimap<String, String>) cacheManager.retrieveObjectFromCache(CacheManager.CacheRegion.REPORT_METADATA_CACHE, reportId);
        if (reportQryTracker != null) {
            List<String> cacheKeys = reportQryTracker.get(reportId);
            for (String cacheKey : cacheKeys) {
                cacheManager.removeObjectFromCache(CacheManager.CacheRegion.REPORT_QUERY_CACHE, cacheKey);
            }
            reportQryTracker.removeAll(reportId);
            cacheManager.storeObjectToCache(CacheManager.CacheRegion.REPORT_METADATA_CACHE, reportId, reportQryTracker);

        }

    }
}
