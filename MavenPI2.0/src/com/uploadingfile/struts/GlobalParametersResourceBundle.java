/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * anitha.pallothu@progenbusiness.com
 */
package com.uploadingfile.struts;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 *
 * @author progen
 */
public class GlobalParametersResourceBundle extends ListResourceBundle implements Serializable {

    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"savedatepicker", "UPDATE PRG_GBL_SETUP_VALUES SET SETUP_DATE_VALUE ='&'  WHERE SETUP_KEY = 'REPORT_START_DATE' "},
        {"savedateformat", "UPDATE PRG_GBL_SETUP_VALUES SET SETUP_CHAR_VALUE ='&'  WHERE SETUP_KEY = 'DATE_FORMAT' "},
        {"savesessions", "UPDATE PRG_GBL_SETUP_VALUES SET SETUP_NUM_VALUE ='&' WHERE SETUP_KEY = 'SESSION_EXPIRY' "},
        {"savelanguage", "UPDATE PRG_GBL_SETUP_VALUES SET SETUP_CHAR_VALUE ='&' WHERE SETUP_KEY = 'DEFAULT_LANGUAGE' "},
        {"saveleftlogo", "UPDATE PRG_GBL_SETUP_VALUES SET SETUP_CHAR_VALUE ='&' WHERE SETUP_KEY = 'LEFT_LOGO' "},
        {"saverightlogo", "UPDATE PRG_GBL_SETUP_VALUES SET SETUP_CHAR_VALUE ='&' WHERE SETUP_KEY = 'RIGHT_LOGO' "},
        {"savefootertext", "UPDATE PRG_GBL_SETUP_VALUES SET SETUP_CHAR_VALUE ='&' WHERE SETUP_KEY = 'FOOTER_TEXT_ENABLED' "},
        {"saverecords", "UPDATE PRG_GBL_SETUP_VALUES SET SETUP_NUM_VALUE ='&' WHERE SETUP_KEY = 'RECORDS_IN_REPORT_TABLE' "},
        {"savequerycache", "UPDATE PRG_GBL_SETUP_VALUES SET SETUP_CHAR_VALUE ='&' WHERE SETUP_KEY = 'QUERY_CACHE_ENABLED' "},
        {"savequery", "UPDATE PRG_GBL_SETUP_VALUES SET SETUP_CHAR_VALUE ='&' WHERE SETUP_KEY = 'QUERY_CACHE' "},
        {"savedataset", "UPDATE PRG_GBL_SETUP_VALUES SET SETUP_CHAR_VALUE ='&' WHERE SETUP_KEY = 'DATA_SET_STORAGE' "},
        {"savegeoenable", "UPDATE PRG_GBL_SETUP_VALUES SET SETUP_CHAR_VALUE ='&' WHERE SETUP_KEY = 'GEO_SPATIAL_ANALYSIS' "},
        {"savedebug", "UPDATE PRG_GBL_SETUP_VALUES SET SETUP_CHAR_VALUE ='&' WHERE SETUP_KEY = 'DEBUG_ENABLED' "},
        {"savereport", "UPDATE PRG_GBL_SETUP_VALUES SET SETUP_CHAR_VALUE ='&' WHERE SETUP_KEY = 'ENABLE_REPORT_CACHE' "},
        {"saveduplicatesegmentation", "UPDATE PRG_GBL_SETUP_VALUES SET SETUP_CHAR_VALUE ='&' WHERE SETUP_KEY = 'DUPLICATE_SEGMENTATION' "}
    };
}
