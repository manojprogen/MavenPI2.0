/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.business.group;

import com.progen.dimensions.DimensionsDAO;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Administrator
 */
public class BusinessGroupAction extends org.apache.struts.action.Action {

    public static Logger logger = Logger.getLogger(BusinessGroupAction.class);
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
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tableId = request.getParameter("tableId");
        String target = null;
        String action = request.getParameter("action");
        BusinessGroupDAO businessGroupDAO = new BusinessGroupDAO();
        HttpSession session;
        String connId1 = request.getParameter("connId");
        try {
            if (request.getSession() == null) {
                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("in if ");
                session = request.getSession(true);

            } else {
                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("in else");
                session = request.getSession(false);
            }
            if (connId1 == null || connId1 == "") {

                connId1 = String.valueOf(session.getAttribute("connId"));
                session.setAttribute("connId", connId1);
                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("conId in if action--->"+connId1);
            } else {
                session.setAttribute("connId", connId1);
            }

            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("conId in action--->"+connId1);

            PrintWriter out = response.getWriter();
            if (action == null) {
                DimensionsDAO dimensionsDAO = new DimensionsDAO();
                ArrayList tableList = businessGroupDAO.getTableNames();
                ArrayList dimensionList = dimensionsDAO.getDimensionsList(connId1);
                request.setAttribute("dimensionList", dimensionList);
                request.setAttribute("list", tableList);

                target = "success";
            } else if (action.equalsIgnoreCase("getRelTables")) {
                String childs = "noData";
                childs = businessGroupDAO.getRelatedTables(tableId);
                if (!(childs.equalsIgnoreCase("noData"))) {
                    response.setContentType("text/xml");
                    out.print(childs);
                } else {
                    out.print(childs);
                }
            } else if (action.equalsIgnoreCase("getRelDimTables")) {
                String childs = "noData";
                String dimensionId = request.getParameter("dimensionId");
                String[] tempTabid = tableId.split(",");
                childs = businessGroupDAO.getRelatedDimTables(tempTabid, dimensionId);
                if (!(childs.equalsIgnoreCase("noData"))) {
                    response.setContentType("text/xml");
                    out.print(childs);
                } else {
                    out.print(childs);
                }
            } else if (action.equalsIgnoreCase("saveData")) {
                boolean noErrorFlag = true;
                String tableIds = request.getParameter("tableIds");
                String grpId = request.getParameter("groupId");

                String[] tabId = tableIds.split(",");
                String[] noOfNodes = new String[tabId.length];
                for (int i = 0; i < tabId.length; i++) {
                    noOfNodes[i] = "1";
                }
                StringBuffer newAddedBussTabIds = new StringBuffer();
                //noErrorFlag = businessGroupDAO.insertBusinessTable(tabId, noOfNodes, grpId);
                noErrorFlag = businessGroupDAO.insertBusinessTable(tabId, noOfNodes, grpId, newAddedBussTabIds);
                if (noErrorFlag) {
                    out.print("success");
                } else {
                    out.print("failure");
                }
            } else if (action.equalsIgnoreCase("addGroup")) {
                String grpName = request.getParameter("grpName");
                String grpDesc = request.getParameter("grpDesc");
                String connId = request.getParameter("connId");
                int bussGrpId = 0;
                bussGrpId = businessGroupDAO.addBusinessGroup(grpName, grpDesc, connId);
                out.print(bussGrpId);
            } else if (action.equalsIgnoreCase("saveDimensions")) {
                String StrdimIds = request.getParameter("dimIds");
                String StrFactIds = request.getParameter("factIds");
                String StrGrpId = request.getParameter("groupId");
                String[] dimIds = new String[0];
                String[] factIds = new String[0];

                boolean noErrorFlag = false;

                if (StrdimIds != null) {
                    dimIds = StrdimIds.split(",");
                }
                if (StrFactIds != null) {
                    factIds = StrFactIds.split(",");
                }
                noErrorFlag = businessGroupDAO.insertGrpDim(dimIds, StrGrpId, factIds);

                if (noErrorFlag) {
                    out.print("success");
                } else {
                    out.print("failure");
                }
            } else if (action.equalsIgnoreCase("addTabsToSrc")) {
                String tabId = request.getParameter("tableId");
                String grpId = request.getParameter("groupId");
                String bussTabId = request.getParameter("bussTabId");
                out.print(businessGroupDAO.addTabsToSrc(tableId, grpId, bussTabId));
            } else if (action.equalsIgnoreCase("saveSrcTblRlts")) {
                String bussTabId = request.getParameter("bussTabId");
                String relClauseStr = request.getParameter("relClauseStr");
                String relCodedClause = request.getParameter("relCodedClause");
                String relTableIds = request.getParameter("relTableIds");
                String noOfNodes = request.getParameter("noOfNodes");
                String relTypes = request.getParameter("relTypes");
                businessGroupDAO.createTblSrcRelations(bussTabId, noOfNodes, relClauseStr, relCodedClause, relTableIds, relTypes);
                out.print("success");

            } else if (action.equalsIgnoreCase("getPrevDbTbls")) {
                String grpId = request.getParameter("groupId");

                out.print(businessGroupDAO.getGroupTableIds(grpId));
            } else if (action.equalsIgnoreCase("getBucketChildDetails")) {
                String grpId = request.getParameter("grpId");
                String bktId = request.getParameter("bktID");
                String bussColId = request.getParameter("bussColId");
                DynamicBusinessGroupDAO dynamicBusinessGroupDAO = new DynamicBusinessGroupDAO();
                String returnStr = dynamicBusinessGroupDAO.getBucketChildDetails(grpId, bktId, bussColId);
//            ////.println("pbReturnObject in Action \t"+pbReturnObject.getRowCount());
//        request.setAttribute("bkeChiDetails",pbReturnObject);
                out.print(returnStr);
            } else if (action.equalsIgnoreCase("updateBucketDetails")) {
                ArrayList paramlistAl = new ArrayList();
                String bucketName = request.getParameter("bktName");
                String bktID = request.getParameter("bktID");
                int rowCount = Integer.parseInt(request.getParameter("rowCount"));
                String upDatebucketName = request.getParameter("bucket");
                String upDatebucketdesc = request.getParameter("bdesc");
                String grpID = request.getParameter("grpId");
//            String upDatebucketName=request.getParameter("bucket");
//             inputValue
//             inputStartlimt
//             inputEndlimt
                String[] displayValue = new String[rowCount];
                String[] startLimt = new String[rowCount];
                String[] endLimt = new String[rowCount];
                for (int count = 0; count < rowCount; count++) {
                    displayValue[count] = request.getParameter("inputValue" + count);
                    startLimt[count] = request.getParameter("inputStartlimt" + count);
                    endLimt[count] = request.getParameter("inputEndlimt" + count);

                }
                ////.println("displayValue\t" + displayValue.length);
                ////.println("startLimt\t" + startLimt.length);
                ////.println("endLimt\t" + endLimt.length);
                paramlistAl.add(bucketName);
                paramlistAl.add(bktID);
                paramlistAl.add(upDatebucketName);
                paramlistAl.add(upDatebucketdesc);
                paramlistAl.add(displayValue);
                paramlistAl.add(startLimt);
                paramlistAl.add(endLimt);
                paramlistAl.add(grpID);
                paramlistAl.add(rowCount);
                DynamicBusinessGroupDAO dynamicBusinessGroupDAO = new DynamicBusinessGroupDAO();
                dynamicBusinessGroupDAO.updateBucketDetails(paramlistAl);

//            out.print(returnStr);
            }
            //updateBucketDetails


            return mapping.findForward(target);
        } catch (Exception e) {
            logger.error("Exception: ", e);
            return null;
        }
    }
}
