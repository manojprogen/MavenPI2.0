/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.scheduler.db;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;
import utils.db.ProgenParam;

/**
 *
 * @author arun
 */
public class TimeHelper {

    public static Logger logger = Logger.getLogger(TimeHelper.class);
    public String elementId;

    public String getLastDay() {
        ProgenParam pParam = new ProgenParam();
        String date = pParam.getdateforpage(1).toString();
        return date;
    }

    public String getCurrentDay() {
        ProgenParam pParam = new ProgenParam();
        String date = pParam.getdateforpage().toString();
        return date;
    }

    public String getLastMonthEndDate() {
        String sql = "select PM_END_DATE from pr_day_denom where ddate=";
        String retVal = getResult(sql);
        return retVal;
    }

    public String getLastMonthStartDate() {
        String sql = "select PM_ST_DATE from pr_day_denom where ddate=";
        String retVal = getResult(sql);
        return retVal;
    }

    public String getLastWeekStartDate() {
        String sql = "select PW_ST_DATE from pr_day_denom where ddate=";
        String retVal = getResult(sql);
        return retVal;
    }

    public String getLastWeekEndDate() {
        String sql = "select PW_END_DATE from pr_day_denom where ddate=";
        String retVal = getResult(sql);
        return retVal;
    }

    public String getCurrentWeekStartDate() {
        String sql = "select CW_ST_DATE from pr_day_denom where ddate=";
        String retVal = getResult(sql);
        return retVal;
    }

    public String getCurrentWeekEndDate() {
        String sql = "select CW_END_DATE from pr_day_denom where ddate=";
        String retVal = getResult(sql);
        return retVal;
    }

    public String getCurrentMonthEndDate() {
        String sql = "select CM_END_DATE from pr_day_denom where ddate=";
        String retVal = getResult(sql);
        return retVal;
    }

    public String getCurrentMonthStartDate() {
        String sql = "select CM_ST_DATE from pr_day_denom where ddate=";
        String retVal = getResult(sql);
        return retVal;
    }

    public String getCurrentQuarterStartDate() {
        String sql = "select CQ_ST_DATE from pr_day_denom where ddate=";
        String retVal = getResult(sql);
        return retVal;
    }

    public String getCurrentQuarterEndDate() {
        String sql = "select CQ_END_DATE from pr_day_denom where ddate=";
        String retVal = getResult(sql);
        return retVal;
    }

    public String getCurrentYearEndDate() {
        String sql = "select CY_END_DATE from pr_day_denom where ddate=";
        String retVal = getResult(sql);
        return retVal;
    }

    public String getCurrentYearStartDate() {
        String sql = "select CY_ST_DATE from pr_day_denom where ddate=";
        String retVal = getResult(sql);
        return retVal;
    }

    private Connection getConnection() {
        return ProgenConnection.getInstance().getConnectionForElement(elementId);
    }

    private String getResult(String sql) {
        ProgenParam pParam = new ProgenParam();
        String date = pParam.getdateforpage().toString();
        String retVal = null;

        Connection conn = getConnection();
        PbDb pbdb = new PbDb();
        PbReturnObject retObj = null;
        sql = sql + "to_date('" + date + "','MM/DD/YYYY') ";
        try {
            retObj = pbdb.execSelectSQL(sql, conn);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        if (retObj != null) {
            Date d = retObj.getFieldValueDate(0, 0);
            String dateFormat = "MM/dd/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            retVal = sdf.format(d);
        }
        return retVal;
    }
}
