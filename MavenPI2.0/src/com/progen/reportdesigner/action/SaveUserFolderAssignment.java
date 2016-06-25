/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.reportdesigner.action;

import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author Saurabh
 */
public class SaveUserFolderAssignment extends org.apache.struts.action.Action {

    public static Logger logger = Logger.getLogger(SaveUserFolderAssignment.class);
    /*
     * forward name="success" path=""
     */
    private final static String SUCCESS = "success";
    /**
     * This is the action called from the Struts framework.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    private boolean isCompanyValid = false;
    ResourceBundle resourceBundle;

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new ReportAssignmentsResBundleSqlServer();
            } else {
                resourceBundle = new ReportAssignmentsResourceBundle();
            }
        }

        return resourceBundle;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {



            String grpIdList = request.getParameter("grpId");
            String userId = request.getParameter("userId");
            String userFolderIdList = request.getParameter("userFolderIds");
            ////////////////////////////.println.println("------grpIdList----------"+grpIdList);
            ////////////////////////////.println.println("------userFolderIdList----------"+userFolderIdList+"---"+userId);
            String grpIds[] = grpIdList.split(",");
            String userFolderIds[] = userFolderIdList.split(",");

            PbDb pbdb = new PbDb();

            String finalQuery = "";
            ArrayList list = new ArrayList();
            //     ReportAssignmentsResourceBundle resBundle = new ReportAssignmentsResourceBundle();
            String deleteUserFolderAssignment = getResourceBundle().getString("deleteUserFolderAssignment");
            Object delobj[] = new Object[1];
            delobj[0] = userId;
            finalQuery = pbdb.buildQuery(deleteUserFolderAssignment, delobj);
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("finalQuery------------"+finalQuery);
            pbdb.execModifySQL(finalQuery);
            if (grpIdList == null) {

                String deluserreps = "DELETE  FROM prg_ar_user_reports where USER_ID=" + userId;
                pbdb.execModifySQL(deluserreps);
            } else {
                grpIds = grpIdList.split(",");
                userFolderIds = userFolderIdList.split(",");

                String addUserFolderAssignment = getResourceBundle().getString("addUserFolderAssignment");
                String delfldr = getResourceBundle().getString("deleteFolder");
                Object delfold[] = new Object[2];
                delfold[0] = userFolderIdList;
                delfold[1] = userId;
                String delfolders = pbdb.buildQuery(delfldr, delfold);

                // delfolders = "DELETE  FROM PRG_GRP_USER_FOLDER_ASSIGNMENT where user_folder_id in(" + userFolderIdList + ")";
                ////////////////////////////.println.println("delfolders=="+delfolders);
                list.add(delfolders);

                for (int i = 0; i < userFolderIds.length; i++) {
                    if ((ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER))) {
                        Object obj[] = new Object[4];
                        obj[0] = userFolderIds[i];
                        obj[1] = userId;
                        obj[2] = grpIds[i];
                        obj[3] = "getdate()";
                        finalQuery = pbdb.buildQuery(addUserFolderAssignment, obj);
                        list.add(finalQuery);
                    } else {
                        Object obj[] = new Object[6];
                        PbReturnObject pbro = pbdb.execSelectSQL("select PRG_GRP_USER_FOLDER_ASSI_SEQ.nextval from dual");
                        String seqNum = String.valueOf(pbro.getFieldValueInt(0, 0));
                        obj[0] = seqNum;
                        obj[1] = userFolderIds[i];
                        obj[2] = userId;
                        obj[3] = grpIds[i];
                        obj[4] = "sysdate";
                        obj[5] = "";
                        finalQuery = pbdb.buildQuery(addUserFolderAssignment, obj);
                        list.add(finalQuery);
                        ///////////////////////.println.println("finalQuery----"+i+"---------"+finalQuery);
                    }
                }

                String getrepssqlqry = "";
                PbReturnObject pbr = null;
                if (isCompanyValid) {
                    getrepssqlqry = getResourceBundle().getString("getRprt");
                    Object user[] = new Object[1];
                    user[0] = userId;
                    String getrepssql = pbdb.buildQuery(getrepssqlqry, user);
                    //     getrepssql = "select ar.report_id, pm.report_name from account_report ar,prg_ar_users pu,prg_ar_report_master pm where pu.pu_id=" + userId + " and "
                    //           + "ar.org_id=pu.account_type and pm.report_id= ar.report_id";// where report_id not in(select pu.report_id from prg_ar_user_reports pu,prg_ar_report_master ar where user_id ="+userId+" and pu.report_id= ar.report_id)";
                    pbr = pbdb.execSelectSQL(getrepssql);
                } else {
                    /*
                     * getrepssql = "select ar.report_id, pm.report_name from
                     * account_report ar,prg_ar_users pu,prg_ar_report_master pm
                     * where pu.pu_id="+userId+" and " +
                     * "ar.org_id=pu.account_type and pm.report_id=
                     * ar.report_id";// where report_id not in(select
                     * pu.report_id from prg_ar_user_reports
                     * pu,prg_ar_report_master ar where user_id ="+userId+" and
                     * pu.report_id= ar.report_id)"; pbr =
                     * pbdb.execSelectSQL(getrepssql);
                     * //////.println("getrepssql==="+getrepssql);
                     * if(pbr.getRowCount()>0){ SELECT REPORT_ID,FOLDER_ID FROM
                     * PRG_AR_REPORT_DETAILS where FOLDER_ID in
                     *
                     * }else{
                     */
                    String sqlqry = getResourceBundle().getString("getRpid");
                    Object objid[] = new Object[1];
                    objid[0] = userFolderIdList;
                    String sql = pbdb.buildQuery(sqlqry, objid);
                    //  String sql = " SELECT REPORT_ID,FOLDER_ID FROM PRG_AR_REPORT_DETAILS where FOLDER_ID in(" + userFolderIdList + ")";
                    pbr = pbdb.execSelectSQL(sql);
                    //}
                }
                if (pbr != null) {
                    if (pbr.getRowCount() > 0) {
                        String repIdsdel = "";
                        for (int n = 0; n < pbr.getRowCount(); n++) {
                            repIdsdel += "," + String.valueOf(pbr.getFieldValueInt(n, 0));
                        }
                        if (!repIdsdel.equalsIgnoreCase("")) {
                            repIdsdel = repIdsdel.substring(1);

                            String deluserreps = "DELETE  FROM prg_ar_user_reports where USER_ID=" + userId + " and Report_id in(" + repIdsdel + ")";
                            ////////////////////////////.println.println("deluserreps=="+deluserreps);
                            list.add(deluserreps);
                        }
                        if ((ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER))) {
                            // String insertQ = "insert into prg_ar_user_reports(USER_ID,REPORT_ID,PUR_FAV_REPORT) values(&,&,'N')";
                            String insertQ = getResourceBundle().getString("insrtreptqry");
                            for (int n = 0; n < pbr.getRowCount(); n++) {
                                String rep = String.valueOf(pbr.getFieldValueInt(n, 0));
                                Object inObj[] = new Object[2];
                                inObj[0] = userId;
                                inObj[1] = rep;
                                String insertQfin = pbdb.buildQuery(insertQ, inObj);
                                list.add(insertQfin);
                            }
                        } else {
                            // String insertQ = "insert into prg_ar_user_reports(USER_REP_ID,USER_ID,REPORT_ID,PUR_REPORT_SEQUENCE,PUR_FAV_REPORT,PUR_CUST_REPORT_NAME) values(PRG_AR_USER_REPORTS_SEQ.nextval,'&','&','','N','')";
                            String insertQ = getResourceBundle().getString("insrtreptqry");
                            for (int n = 0; n < pbr.getRowCount(); n++) {
                                String rep = String.valueOf(pbr.getFieldValueInt(n, 0));
                                Object inObj[] = new Object[2];
                                inObj[0] = userId;
                                inObj[1] = rep;
                                String insertQfin = pbdb.buildQuery(insertQ, inObj);
                                ////////////////////////////.println.println(" insertQfin "+insertQfin);
                                list.add(insertQfin);
                            }
                        }

                    }
                }
                pbdb.executeMultiple(list);
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        response.getWriter().print("Success");
        return null;
    }
}
