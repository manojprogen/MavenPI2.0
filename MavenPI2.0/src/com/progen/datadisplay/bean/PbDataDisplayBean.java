/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.datadisplay.bean;

import com.progen.datadisplay.db.PbDataDisplayBeanDb;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import prg.db.PbReturnObject;

/**
 * @filename PbDataDisplayBean
 *
 * @author santhosh.kumar@progenbusiness.com @date Aug 17, 2009, 6:46:17 PM
 */
public class PbDataDisplayBean {

    public static Logger logger = Logger.getLogger(PbDataDisplayBean.class);

    public ArrayList viewTable(String tableIds) {
        PbDataDisplayBeanDb db = new PbDataDisplayBeanDb();
        PbReturnObject retObj = null;
        ArrayList alist = new ArrayList();
        try {
            alist = db.viewTable(tableIds);
            return alist;
        } catch (Exception exception) {
            logger.error("Exception:", exception);
            return alist;
        }
    }

    public ArrayList viewTable2(String actualtableid, String tableIds) {
        PbDataDisplayBeanDb db = new PbDataDisplayBeanDb();
        PbReturnObject retObj = null;
        ArrayList alist = new ArrayList();
        try {
            alist = db.viewTable2(actualtableid, tableIds);
            return alist;
        } catch (Exception exception) {
            logger.error("Exception:", exception);
            return alist;
        }
    }

    //added by sreekanth
    public PbReturnObject getNetworkConnDetails(String connectionId) {
        PbDataDisplayBeanDb db = new PbDataDisplayBeanDb();
        PbReturnObject retObj = null;

        try {
            retObj = db.getNetworkConnDetails(connectionId);
        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return retObj;
    }
}
