/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.etl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

/**
 *
 * @author Administrator
 */
public class EtlUploadAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(EtlUploadAction.class);

    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("lastUploadTime", "lastUploadTime");
        map.put("getListOfTables", "getListOfTables");
        map.put("showDates", "showDates");
        map.put("checkDates", "checkDates");
        map.put("loadData", "loadData");
        return map;
    }

    public ActionForward loadData(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
//        ProgenLog.log(ProgenLog.FINE, this, "loadData", "Enter loadData");
        logger.info("Enter loadData");
        EtlUploadBd uploadBd = new EtlUploadBd();

        String tableName = request.getParameter("selTab");
        String stdate = request.getParameter("stDate");
        String enddate = request.getParameter("endDate");

        if ("null".equalsIgnoreCase(stdate)) {
            stdate = null;
            enddate = null;
        }

        String uploadStatus = uploadBd.loadData(tableName, stdate, enddate);

        try {
            PrintWriter out = response.getWriter();
            out.print(uploadStatus);
        } catch (IOException e) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "loadData", "Exception loadData " + e.getMessage());
            logger.error("Exception loadData ", e);
        }
//        ProgenLog.log(ProgenLog.FINE, this, "loadData", "Exit loadData");
        logger.info("Exit loadData");
        return null;
    }

    public ActionForward lastUploadTime(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
//        ProgenLog.log(ProgenLog.FINE, this, "lastUploadTime", "Enter lastUploadTime");
        logger.info("Enter lastUploadTime");
        String tableName = request.getParameter("selTab");
        EtlUploadBd etlUpload = new EtlUploadBd();
        String lastUploadTime = etlUpload.lastUploadTime(tableName);

        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception ", ex);
        }
        out.print(lastUploadTime);

        return null;
    }

    public ActionForward getListOfTables(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
//        ProgenLog.log(ProgenLog.FINE, this, "getListOfTables", "Enter lastUploadTime");
        logger.info("Enter lastUploadTime");
        EtlUploadBd etlUpload = new EtlUploadBd();
        StringBuffer sb = etlUpload.getListOfTables();
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception ", ex);
        }
        out.print(sb);
//        ProgenLog.log(ProgenLog.FINE, this, "getListOfTables", "Exit getListOfTables");
        logger.info("Exit getListOfTables");
        return null;
    }

    public ActionForward showDates(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
//        ProgenLog.log(ProgenLog.FINE, this, "showDates", "Enter showDates");
        logger.info("Enter showDates");
        String tableName = "";
        tableName = request.getParameter("selTab");
        PrintWriter out;
        EtlUploadBd etlUpload = new EtlUploadBd();
        try {
            out = response.getWriter();
            // if(etlUpload.isHeirTable(tableName))
            if (etlUpload.showDates(tableName)) {
                out.print("y");
            }
        } catch (Exception e) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "showDates", "Exception showDates " + e.getMessage());
            logger.error("Exception showDates", e);
        }
//        ProgenLog.log(ProgenLog.FINE, this, "showDates", "Exit showDates");
        logger.info("Exit showDates");
        return null;

    }

    public ActionForward checkDates(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
//        ProgenLog.log(ProgenLog.FINE, this, "checkDates", "Enter checkDates");
        logger.info("Enter checkDates");
        String stDate = request.getParameter("stDate");
        String endDate = request.getParameter("endDate");
        String tableName = request.getParameter("selTab");
        EtlUploadBd etlUpload = new EtlUploadBd();
        String message = etlUpload.checkDates(tableName, stDate, endDate);
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception ", ex);
        }
        out.print(message);
        return null;
    }
}
