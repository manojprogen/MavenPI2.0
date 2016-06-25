/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.reportview.db;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author parsi Bhargavi
 */
public class PbAOViewerDAO extends PbDb {

    private ResourceBundle resourceBundle;
    public int memberId = 0;

    private ResourceBundle getResourceBundle() {
        if (resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
                resourceBundle = new PbReportViewerResourceBundle();
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                resourceBundle = new PbReportViewerResourceBundleMySql();
            } else {
                resourceBundle = new PbReportViewerResBunSqlServer();
            }
        }
        return resourceBundle;
    }

    public void getCompleteDateReturnObject(Container container, ArrayList reportMeasureIdsList) {
        PbReturnObject retObj;
        PbDb pbDb = new PbDb();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        SortedSet<Date> dates = new TreeSet<>();
        Connection conn;
        String elementIds = "";
        String qry;

        if (reportMeasureIdsList != null) {
            for (Object reportMeasureIdsList1 : reportMeasureIdsList) {
                elementIds = elementIds + "," + reportMeasureIdsList1;
            }
        }

        qry = " select MD.BUSS_COL_NAME, M.BUSINESS_TABLE_ID, M.MIN_LEVEL, B.BUSS_TABLE_NAME ";
        qry += ", COALESCE(B.REF_TABLE_NAME,'PR_DAY_DENOM') REF_TABLE_NAME ";
        qry += "from PRG_USER_SUB_FOLDER_ELEMENTS MD ";
        qry += " , PRG_TIME_DIM_INFO M ";
        qry += ", PRG_GRP_BUSS_TABLE B ";
        qry += "where M.MAIN_FACT_ID =     MD.BUSS_TABLE_ID ";
        qry += "and M.MAIN_FACT_COL_ID = MD.buss_COL_ID ";
        qry += "and MD.BUSS_TABLE_ID =     B.BUSS_TABLE_ID ";
        qry += "and B.BUSS_TABLE_NAME in (Select distinct BUSS_TABLE_NAME from prg_user_all_info_details where element_id in(" + elementIds.substring(1) + "))";

        try {
            retObj = pbDb.execSelectSQL(qry);
            for (int row = 0; row < retObj.rowCount; row++) {
                qry = "select min(" + retObj.getFieldValueString(row, 0) + ")  as min_date,max(" + retObj.getFieldValueString(row, 0) + ")  as max_date from " + retObj.getFieldValueString(row, 3);
                conn = ProgenConnection.getInstance().getConnectionForElement(reportMeasureIdsList.get(0).toString().replace("A_", ""));
                PbReturnObject obj = pbDb.execSelectSQL(qry, conn);
                //for (int count = 0; count < 2; count++) {
                dates.add(obj.getFieldValueDate(0, "min_date"));
                dates.add(obj.getFieldValueDate(0, "max_date"));
                //}
            }
            container.aoSdate = formatter.format(dates.first());
            container.aoEdate = formatter.format(dates.last());
        } catch (Exception ex) {
            Logger.getLogger(PbAOViewerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
