/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.datadisplay.client;

import com.progen.datadisplay.bean.PbDataDisplayBean;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import prg.db.PbReturnObject;

/**
 * @filename PbDataDisplayManager
 *
 * @author santhosh.kumar@progenbusiness.com @date Aug 17, 2009, 6:46:43 PM
 */
public class PbDataDisplayManager {

    public static Logger logger = Logger.getLogger(PbDataDisplayManager.class);

    public ArrayList viewTable(String tableIds) {
        PbDataDisplayBean bean = new PbDataDisplayBean();
        PbReturnObject retObj = null;
        ArrayList alist = new ArrayList();
        try {
            alist = bean.viewTable(tableIds);
            return alist;
        } catch (Exception exception) {
            logger.error("Exception:", exception);
            return alist;
        }
    }

    public ArrayList viewTable2(String actualtableid, String tableIds) {
        PbDataDisplayBean bean = new PbDataDisplayBean();
        PbReturnObject retObj = null;
        ArrayList alist = new ArrayList();
        try {
            alist = bean.viewTable2(actualtableid, tableIds);
            return alist;
        } catch (Exception exception) {
            logger.error("Exception:", exception);
            return alist;
        }
    }

    //added by sreekanth
    public PbReturnObject getNetworkConnDetails(String connectionId) {
        PbDataDisplayBean bean = new PbDataDisplayBean();
        PbReturnObject retObj = null;

        try {
            retObj = bean.getNetworkConnDetails(connectionId);

        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return retObj;
    }
}
