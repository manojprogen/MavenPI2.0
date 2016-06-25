/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.reportdesigner.action;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 *
 * @author Saurabh
 */
public class SaveUserFolderAssignmentAll extends org.apache.struts.action.Action {

    public static Logger logger = Logger.getLogger(SaveUserFolderAssignmentAll.class);
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
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            //  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("in java save assign user");

            String grpIdList = request.getParameter("grpId");
            String userId = request.getParameter("userId");
            String userFolderIdList = request.getParameter("userFolderIds");
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("------grpIdList----------"+grpIdList);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("------userFolderIdList----------"+userFolderIdList);
            String grpIds[] = grpIdList.split(",");
            String userFolderIds[] = userFolderIdList.split(",");
            // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("grpId--"+grpId+"userListString"+userListString);

            PbDb pbdb = new PbDb();

            String finalQuery = "";
            ArrayList list = new ArrayList();
            ReportAssignmentsResourceBundle resBundle = new ReportAssignmentsResourceBundle();
            String deleteUserFolderAssignment = resBundle.getString("deleteUserFolderAssignment");
            Object delobj[] = new Object[1];
            delobj[0] = userId;
            finalQuery = pbdb.buildQuery(deleteUserFolderAssignment, delobj);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("finalQuery------------"+finalQuery);
            pbdb.execModifySQL(finalQuery);
            String addUserFolderAssignment = resBundle.getString("addUserFolderAssignment");


            Object obj[] = new Object[6];
            for (int i = 0; i < userFolderIds.length; i++) {
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
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("finalQuery----"+i+"---------"+finalQuery);
            }
            pbdb.executeMultiple(list);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        response.getWriter().print("Success");
        return null;
    }
}
