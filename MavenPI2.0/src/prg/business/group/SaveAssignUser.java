/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.business.group;

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
public class SaveAssignUser extends org.apache.struts.action.Action {

    public static Logger logger = Logger.getLogger(SaveAssignUser.class);
    ResourceBundle resourceBundle;

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new PbBussGrpResBundleSqlServer();
            } else {
                resourceBundle = new PbBussGrpResourceBundle();
            }
        }

        return resourceBundle;
    }
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
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            //  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("in java save assign user");

            String grpId = request.getParameter("grpId");
            String userListString = request.getParameter("users");
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("grpId--"+grpId+"userListString"+userListString);

            String vals[] = userListString.split(",");
            String userIdsList[] = new String[vals.length];
            String userName[] = new String[vals.length];
            String delUserFolderList = "";
            for (int i = 0; i < vals.length; i++) {
                userIdsList[i] = vals[i].split("~")[0];
                // delUserFolderList+=","+userIdsList[i];
                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" --"+userIdsList[i]);
                userName[i] = vals[i].split("~")[1];
                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" --"+userName[i]);
            }
            //  delUserFolderList=delUserFolderList.substring(1);
            PbDb pbdb = new PbDb();

            String finalQuery = "";
            ArrayList list = new ArrayList();
//        PbBussGrpResourceBundle resBundle=new PbBussGrpResourceBundle();
            String deleteUserAssignment = getResourceBundle().getString("deleteUserAssignment");
            Object delobj[] = new Object[1];
            delobj[0] = grpId;
            finalQuery = pbdb.buildQuery(deleteUserAssignment, delobj);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("finalQuery------------"+finalQuery);
            pbdb.execModifySQL(finalQuery);
            String addUserAssignment = getResourceBundle().getString("addUserAssignment");
            Object obj[] = null;
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                obj = new Object[6];
            } else {
                obj = new Object[7];
            }

            for (int i = 0; i < userIdsList.length; i++) {
                PbReturnObject pbro = null;
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                   pbro=pbdb.execSelectSQL("select PRG_GRP_USER_ASSIGNMENT_SEQ.nextval from dual");
//                    obj[0] = seqNum;
                    obj[0] = userIdsList[i];
                    obj[1] = grpId;
                    obj[2] = "getdate()";
                    obj[3] = "";
                    obj[4] = "Y";
                    obj[5] = userName[i];
                    finalQuery = pbdb.buildQuery(addUserAssignment, obj);
                    list.add(finalQuery);

                } else {
                    pbro = pbdb.execSelectSQL("select PRG_GRP_USER_ASSIGNMENT_SEQ.nextval from dual");
                    String seqNum = String.valueOf(pbro.getFieldValueInt(0, 0));
                    obj[0] = seqNum;
                    obj[1] = userIdsList[i];
                    obj[2] = grpId;
                    obj[3] = "sysdate";
                    obj[4] = "";
                    obj[5] = "Y";
                    obj[6] = userName[i];
                    finalQuery = pbdb.buildQuery(addUserAssignment, obj);
                    list.add(finalQuery);
                }


                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("finalQuery----"+i+"---------"+finalQuery);
            }
            pbdb.executeMultiple(list);

            /*
             * String
             * addUserFolderAssignment=resBundle.getString("addUserFolderAssignment");
             *
             * String getfolderList ="SELECT FOLDER_ID, GRP_ID, FOLDER_NAME FROM
             * PRG_USER_FOLDER where GRP_ID in ("+grpId+")"; PbReturnObject
             * pbro1 = pbdb.execSelectSQL(getfolderList); pbro1.writeString();
             * if(pbro1.getRowCount()>0){ ArrayList list1=new ArrayList();
             * String userFolderIds[]=new String[pbro1.getRowCount()]; for(int
             * i=0;i<pbro1.getRowCount();i++){
             * userFolderIds[i]=String.valueOf(pbro1.getFieldValueInt(i, 0)); }
             * String
             * deleteUserFolderAssignment=resBundle.getString("deleteUserFolderAssignment");
             * Object delobj1[]=new Object[2]; delobj1[0]=delUserFolderList;
             * delobj1[1]=grpId;
             * finalQuery=pbdb.buildQuery(deleteUserFolderAssignment, delobj1);
             * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("finalQuery-----
             * folder del-------"+finalQuery);
             *
             * Object obj1[]=new Object[6]; for(int
             * j=0;j<userIdsList.length;j++){
             *
             * pbdb.execModifySQL(finalQuery); for(int
             * i1=0;i1<userFolderIds.length;i1++){ PbReturnObject
             * pbro2=pbdb.execSelectSQL("select
             * PRG_GRP_USER_FOLDER_ASSI_SEQ.nextval from dual"); String
             * seqNum=String.valueOf(pbro2.getFieldValueInt(0,0));
             * obj1[0]=seqNum; obj1[1]=userFolderIds[i1];
             * obj1[2]=userIdsList[j]; obj1[3]=grpId; obj1[4]="sysdate";
             * obj1[5]=""; finalQuery=pbdb.buildQuery(addUserFolderAssignment,
             * obj1); list1.add(finalQuery);
             * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("finalQuery---folders---"+i1+"---------"+finalQuery);
             * }
             *
             * }
             * pbdb.executeMultiple(list1); }
             *
             */

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return mapping.findForward(SUCCESS);
    }
}
