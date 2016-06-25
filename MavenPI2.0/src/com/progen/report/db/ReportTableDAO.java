/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.db;

import java.sql.SQLException;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 *
 * @author progen
 */
public class ReportTableDAO extends PbDb {

    public static Logger logger = Logger.getLogger(ReportTableDAO.class);

    public void insertTargetValues(String elementId, String timeLevel, String targetData) {
        //Check whether the data is available already for this elementId and timeLevel. If it's available, update the column
        //Else insert a new column

        String selectQry = "select * from prg_ar_target_data where element_id=" + elementId + " and time_level='" + timeLevel + "'";
        boolean targetAvailable = false;
        PbReturnObject retObj = null;

        try {
            retObj = execSelectSQL(selectQry);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

        if (retObj != null && retObj.getRowCount() > 0) {
            targetAvailable = true;
        }

        String updateQry = "";
        if (targetAvailable) {
            updateQry = "update prg_ar_target_data set target_data='" + targetData + "' where element_id=" + elementId + " and time_level='" + timeLevel + "'";
        } else {
            updateQry = "insert into prg_ar_target_data values(" + elementId + ",'" + timeLevel + "','" + targetData + "')";
        }

        try {
            execModifySQL(updateQry);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }
}
