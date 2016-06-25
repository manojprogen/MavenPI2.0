/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.business.group;

import com.google.gson.Gson;
import com.progen.i18n.TranslaterHelper;
import com.progen.metadata.MeasureProperty;
import com.progen.userlayer.db.SavePublishUserFolder;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import oracle.jdbc.OraclePreparedStatement;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

public class BusinessGroupEditAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(BusinessGroupEditAction.class);
    /*
     * forward name="success" path=""
     */
    private final static String SUCCESS = "success";

    /**
     * Provides the mapping from resource key to method name.
     *
     * @return Resource key / method name map.
     */
    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("button.add", "add");
        map.put("button.edit", "edit");
        map.put("button.delete", "delete");
        map.put("getGroupDimDeleteStatus", "getGroupDimDeleteStatus"); //
        map.put("deleteGroupDimension", "deleteGroupDimension");
        map.put("getGrpDimRoles", "getGrpDimRoles");
        map.put("checkGroupNameForConn", "checkGroupNameForConn");
        map.put("copyGroupDimForConn", "copyGroupDimForConn");
        map.put("updateGrpDimMemberStatus", "updateGrpDimMemberStatus");
        map.put("updateGrpDimStatus", "updateGrpDimStatus");
        map.put("updateGrpFactStatus", "updateGrpFactStatus");
        map.put("checkFactAndDimOnRoleCreation", "checkFactAndDimOnRoleCreation");
        map.put("checkTimeDimForBusGroup", "checkTimeDimForBusGroup");
        map.put("deleteGrpDimAndReport", "deleteGrpDimAndReport");
        map.put("editGrpAddDim", "editGrpAddDim");
        map.put("addMemberDetailsDesc", "addMemberDetailsDesc");
        map.put("timeBasedFormula", "timeBasedFormula");
        map.put("getMeasureUnits", "getMeasureUnits");
        map.put("saveColumnProperties", "saveColumnProperties");
        map.put("checkRelationTables", "checkRelationForTables");
        map.put("validateFormula", "validateFormula");
        map.put("saveGroupHierarchy", "saveGroupHierarchy");
        map.put("getGroupHierarchy", "getGroupHierarchy");
        map.put("getFolderidDetails", "getFolderidDetails");
        map.put("saveHierarchyGroups", "saveHierarchyGroups");
        map.put("getGroups", "getGroups");
        map.put("getChildsId", "getChildsId");
        map.put("saveGroupChildHierarchy", "saveGroupChildHierarchy");
        return map;
    }

    public ActionForward getGroupDimDeleteStatus(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String delGrpId = request.getParameter("delGrpId");
        String delDimId = request.getParameter("delDimId");
        String status = "";
        PbBusinessGroupEditDAO bgEditDao = new PbBusinessGroupEditDAO();
        status = bgEditDao.getGroupDimDeleteStatus(delGrpId, delDimId);
        String reportStatus = bgEditDao.getRelatedReportsForGroup(delGrpId, delDimId);
        PrintWriter out = response.getWriter();
        SavePublishUserFolder publish = new SavePublishUserFolder();
        try {
            if (status.equalsIgnoreCase("false") && reportStatus.equalsIgnoreCase("Not Exists")) {
                out.println("2");
                bgEditDao.deleteGroupDim(delGrpId, delDimId);
                ArrayList roles = bgEditDao.getGrpRoles(delGrpId);
                for (int m = 0; m < roles.size(); m++) {
                    publish.publishUserFolder(roles.get(m).toString());
                }

            } else if (status.equalsIgnoreCase("true") && reportStatus.equalsIgnoreCase("Not Exists")) {
                out.println("3");
            } else if (reportStatus.equalsIgnoreCase("Exists")) {
                out.println("1");
            }

        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return mapping.findForward("null");
    }

    public ActionForward deleteGroupDimension(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String delGrpId = request.getParameter("delGrpId");
        String delDimId = request.getParameter("delDimId");
        String status = "";
        //////////////////////////////////////////////////////////////////////////////.println.println(" delGrpId................ "+ delGrpId);
        //////////////////////////////////////////////////////////////////////////////.println.println(" delDimId --.."+delDimId);
        PbBusinessGroupEditDAO bgEditDao = new PbBusinessGroupEditDAO();
        bgEditDao.deleteGroupDim(delGrpId, delDimId);
        PrintWriter out = response.getWriter();
        out.println("1");
        return mapping.findForward("null");
    }

    public ActionForward getGrpDimRoles(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String grpId = request.getParameter("grpId");
        String status = "";
        PrintWriter out = response.getWriter();
        PbBusinessGroupEditDAO bgEditDao = new PbBusinessGroupEditDAO();
        status = bgEditDao.getGrpDimRoles(grpId);
        if (status.equalsIgnoreCase("exists")) {
            out.println("1");
        } else if (status.equalsIgnoreCase("Not exists")) {
            out.println("2");
        }
        return mapping.findForward("null");
    }

    public ActionForward checkGroupNameForConn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String grpName = request.getParameter("groupName");
        String connId = request.getParameter("connId");
        String status = "";
        PrintWriter out = response.getWriter();

        PbBusinessGroupEditDAO bgEditDao = new PbBusinessGroupEditDAO();
        status = bgEditDao.checkGroupNameForConn(grpName, connId);
        if (status.equalsIgnoreCase("false")) {
            out.println("1");
        } else if (status.equalsIgnoreCase("true")) {
            out.println("2");
        }

        return mapping.findForward("null");
    }

    public ActionForward copyGroupDimForConn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String grpName = request.getParameter("groupName");
//        String connId = request.getParameter("connId");
        String sourceGrpId = request.getParameter("sourceGrpId");
        String selectedGrpDimId = request.getParameter("selectedGrpDimId");
        String grpDesc = request.getParameter("grpDesc");
        if (selectedGrpDimId.length() > 1) {
            selectedGrpDimId = selectedGrpDimId.substring(1);
        }
        String grpDims[] = selectedGrpDimId.split(",");
        HashMap selectedBucketDims = new HashMap();
        for (int u = 0; u < grpDims.length; u++) {
            selectedBucketDims.put(grpDims[u], grpDims[u]);
        }
        String status = "";
        PbBusinessGroupEditDAO bgDao = new PbBusinessGroupEditDAO();
        PbReturnObject all = bgDao.getGroupDetails(sourceGrpId);
        PbReturnObject factObj = (PbReturnObject) all.getObject("factObj");
        PbReturnObject dimObj = (PbReturnObject) all.getObject("dimObj");
        HashMap dimDetails = new HashMap();
        //susheela added for the buckets dimension
//        String bucketDims = "";
        StringBuilder bucketDims = new StringBuilder();
        for (int m = 0; m < dimObj.getRowCount(); m++) {
            dimDetails.put(dimObj.getFieldValueString(m, "DIM_ID"), dimObj.getFieldValueString(m, "QRY_DIM_ID"));
            if (dimObj.getFieldValueString(m, "QRY_DIM_ID").equalsIgnoreCase("")) {
//                bucketDims = bucketDims + "," + dimObj.getFieldValueString(m, "DIM_ID");
                bucketDims.append( ",").append( dimObj.getFieldValueString(m, "DIM_ID"));
            }
        }
        PbBusinessGroupCopyDAO copyDao = new PbBusinessGroupCopyDAO();
        String newConId = copyDao.getConnectionId(sourceGrpId);
        int newGrpId = copyDao.addBusinessGroup(grpName, grpDesc, newConId);
//        String gId = "" + newGrpId + "";
        String gId = String.valueOf( newGrpId );
        String grpDim[] = (String[]) dimDetails.keySet().toArray(new String[0]);
        String dbDims = "";
        String dbDimId = "";
        for (int m = 0; m < grpDims.length; m++) {
            String dim = grpDims[m];
            if (dimDetails.containsKey(dim)) {
                dbDimId = dimDetails.get(dim).toString();
                dbDims = dbDims + "," + dbDimId;
            }
        }
        if (dbDims.length() > 1) {
            dbDims = dbDims.substring(1);
        }
        String dbDimVal[] = dbDims.split(",");
        /*
         * for (int t = 0; t < dbDimVal.length; t++) {
         * dbDimVal " + dbDimVal[t]); }
         */
        String getDbTabsQ = "select * from prg_grp_buss_table where buss_table_id in(select tab_id from prg_grp_dim_tables"
                + " where dim_id in(select dim_id from prg_grp_dimensions where grp_id=" + sourceGrpId + "  and "
                + " dim_id in(" + selectedGrpDimId + ")))  and buss_type in('Table')";
        PbDb pbDb = new PbDb();
        StringBuffer newAddedBussTabIds = new StringBuffer();
        PbReturnObject dimTabsOb = pbDb.execSelectSQL(getDbTabsQ);
//        String dbTabs = "";
        StringBuilder dbTabs = new StringBuilder();
        for (int m = 0; m < dimTabsOb.getRowCount(); m++) {
//            dbTabs = dbTabs + "," + dimTabsOb.getFieldValueString(m, "DB_TABLE_ID");
            dbTabs.append(",").append( dimTabsOb.getFieldValueString(m, "DB_TABLE_ID"));
        }
        if (dbTabs.length() > 1) {
            dbTabs = new StringBuilder(dbTabs.substring(1));
        }
        String tabIds[] = dbTabs.toString().split(",");
        String factTabQ = "select * from prg_grp_buss_table where grp_id=" + sourceGrpId + " and buss_table_id not in(select tab_id from prg_grp_dim_tables where dim_id in( select dim_id from prg_grp_dimensions where grp_id=" + sourceGrpId + ") ) and buss_type in('Table')";
        PbReturnObject factRetObj = pbDb.execSelectSQL(factTabQ);
//        String factIds = "";
        StringBuilder factIds = new StringBuilder();
        for (int m = 0; m < factRetObj.getRowCount(); m++) {
            factIds.append("," ).append( factRetObj.getFieldValueString(m, "DB_TABLE_ID"));
//            factIds = factIds + "," + factRetObj.getFieldValueString(m, "DB_TABLE_ID");
        }
        if (factIds.length() > 1) {
            factIds = new StringBuilder(factIds.substring(1));
        }
        String fact[] = factIds.toString().split(",");
        String noOfNodes[] = new String[fact.length];
        for (int m = 0; m < noOfNodes.length; m++) {
            noOfNodes[m] = "1";
        }
        // copyDao.insertBusinessTable(tabIds,noOfNodes,gId,newAddedBussTabIds);
        copyDao.insertGrpDim(dbDimVal, gId, tabIds);
        copyDao.insertBusinessTable(fact, noOfNodes, gId, newAddedBussTabIds);
        copyDao.updateBussTableDetails(sourceGrpId, gId);
        all = bgDao.getGroupDetailsWithBucket(sourceGrpId);
        dimObj = (PbReturnObject) all.getObject("dimObj");
        dimDetails = new HashMap();
//        String bucketDimNames = "";
        String bucketDimNames = "";
        for (int m = 0; m < dimObj.getRowCount(); m++) {
            dimDetails.put(dimObj.getFieldValueString(m, "DIM_ID"), dimObj.getFieldValueString(m, "QRY_DIM_ID"));
            if (dimObj.getFieldValueString(m, "QRY_DIM_ID").equalsIgnoreCase("")) {
                if (selectedBucketDims.containsKey(dimObj.getFieldValueString(m, "DIM_ID"))) {
//                    bucketDims = bucketDims + "," + dimObj.getFieldValueString(m, "DIM_ID");
                    bucketDims.append( bucketDims).append( ",").append( dimObj.getFieldValueString(m, "DIM_ID"));
                    bucketDimNames = bucketDimNames + "," + dimObj.getFieldValueString(m, "DIM_NAME");
                }
            }
        }
        if (bucketDims.length() > 0) {
            bucketDimNames = bucketDimNames.substring(1);
            bucketDimNames = bucketDimNames.toUpperCase();
            bucketDimNames = "'" + bucketDimNames + "'";
            bucketDimNames = bucketDimNames.replace(",", "','");
            bucketDims =new StringBuilder( bucketDims.substring(1));
            String buc[] = bucketDimNames.split(",");
            for (int l = 0; l < buc.length; l++) {
                copyDao.copyBucket(sourceGrpId, gId, bucketDims.toString(), buc[l]);
            }
        }
        copyDao.copyFormula(sourceGrpId, gId);
        PrintWriter out = response.getWriter();
        out.println("1");
        return mapping.findForward("null");
    }

    public ActionForward timeBasedFormula(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        PbBusinessGroupEditDAO pbeditdao = new PbBusinessGroupEditDAO();
        String selectedvalues = request.getParameter("selectedvalues");
//            String bussTableId1 = request.getParameter("bussTableId");
        String buscolid = request.getParameter("bussColId");
//            String colName1 = request.getParameter("colName");
        ArrayList<String> id = pbeditdao.getTableid(buscolid);
        String bussTableId = id.get(0);
        String colName = id.get(1);
        String tablename = request.getParameter("tablename");
        String[] splitselected = selectedvalues.split(",");
//            String tableid=pbeditdao.getAllFields(bussTableId);
        ArrayList selvalueslist = new ArrayList();
        ArrayList colnames = new ArrayList();
        String manipulatestring = null;
        for (int i = 0; i < splitselected.length; i++) {
            manipulatestring = request.getParameter(splitselected[i]);
            colnames.add(manipulatestring);
            manipulatestring = manipulatestring.replace("-", "(");
            manipulatestring = manipulatestring.concat(")");
            selvalueslist.add(manipulatestring);
        }
        pbeditdao.timebasedFormula(selvalueslist, bussTableId, colnames, buscolid, colName, tablename);
        return null;
    }

    public ActionForward addMemberDetailsDesc(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String grpId = request.getParameter("grpId");
        String memberId = request.getParameter("memberId");
//        String memberName = request.getParameter("memberName");
        String memberdesc = request.getParameter("memberdesc");
        try {
            Connection con = ProgenConnection.getInstance().getConnection();
            OraclePreparedStatement opstmt = null;
            String sql = "select * from  PRG_MEMBER_DETAILS  where grp_id=" + grpId + " and member_id=" + memberId;
            PbReturnObject pbro1 = new PbDb().execSelectSQL(sql);
            if (pbro1.getRowCount() > 0) {
                String sql1 = "DELETE FROM PRG_MEMBER_DETAILS  where grp_id=" + grpId + " and member_id=" + memberId;
                new PbDb().execUpdateSQL(sql1);
            }

            String query1 = "INSERT INTO PRG_MEMBER_DETAILS(MEM_DETS_ID, MEMBER_ID, GRP_ID, MEMBER_DESC) values(PRG_MEMBER_DETAILS_SEQ.nextval,?,?,?)";
            opstmt = (OraclePreparedStatement) con.prepareStatement(query1);
            opstmt.setString(1, memberId);
            opstmt.setString(2, grpId);
            //opstmt.setString(3, memberName);
            opstmt.setStringForClob(3, memberdesc);
            opstmt.executeUpdate();
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return mapping.findForward(SUCCESS);
    }

    public ActionForward add(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
        // TODO: implement add method
        return mapping.findForward(SUCCESS);
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        // TODO: implement edit method
        return mapping.findForward(SUCCESS);
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        // TODO:implement delete method
        return mapping.findForward(SUCCESS);
    }

    public ActionForward getMeasureUnits(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        Locale locale = (Locale) session.getAttribute("userLocale");
        String measureType = request.getParameter("measureType");
        HashMap<String, HashSet<String>> measureUnitDetails = (HashMap<String, HashSet<String>>) session.getAttribute("measureUnitDetails");
        HashSet<String> measureunits = measureUnitDetails.get(measureType);
//        String htmlString = "";
        StringBuilder htmlString = new StringBuilder();
        for (String str : measureunits) {
//            htmlString += "<option value=" + str + "> " + TranslaterHelper.getTranslatedString(str, locale) + " </option>";
            htmlString.append( "<option value=").append(str).append("> ").append(TranslaterHelper.getTranslatedString(str, locale) ).append( " </option>");
        }
        response.getWriter().print(htmlString.toString());
        return mapping.findForward("null");
    }

    public ActionForward saveColumnProperties(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String table_id = request.getParameter("tableid");
        String query = "";
        PbReturnObject pbReturnObject = new PbReturnObject();
        PbDb pbDb = new PbDb();
        ArrayList<String> queryList = new ArrayList<String>();
        Gson gson = new Gson();
        // return gson.toJson(autoSuggest);
        try {
            query = "select buss_column_id from prg_grp_buss_table_details where buss_table_id=" + table_id;
            pbReturnObject = pbDb.execSelectSQL(query);
            for (int i = 0; i < pbReturnObject.getRowCount(); i++) {
                String column_id = pbReturnObject.getFieldValueString(i, 0);
                String colID = request.getParameter(column_id);
                MeasureProperty measureProperty = new MeasureProperty();
                if (request.getParameter("measCategory_" + i) != null && !request.getParameter("measCategory_" + i).equalsIgnoreCase("")) {
                    measureProperty.setMeasureCategory(request.getParameter("measCategory_" + i));
                }
                if (request.getParameter("measureType_" + i) != null && !request.getParameter("measureType_" + i).equalsIgnoreCase("")) {
                    measureProperty.setMeasureType(request.getParameter("measureType_" + i));
                }
                if (request.getParameter("measureUnit_" + i) != null && !request.getParameter("measureUnit_" + i).equalsIgnoreCase("")) {
                    measureProperty.setMeasureUnits(request.getParameter("measureUnit_" + i));
                }

                if (request.getParameter("measureLable_" + i) != null && !request.getParameter("measureLable_" + i).equalsIgnoreCase("")) {
                    measureProperty.setMeasureLable(request.getParameter("measureLable_" + i));
                }
                if (request.getParameter("meaRoundingTypeSel_" + i) != null && !request.getParameter("meaRoundingTypeSel_" + i).equalsIgnoreCase("")) {
                    measureProperty.setMeasureRoundingType(request.getParameter("meaRoundingTypeSel_" + i));
                }
                if (request.getParameter("measuRoundin_" + i) != null && !request.getParameter("measuRoundin_" + i).equalsIgnoreCase("")) {
                    measureProperty.setMeasureRounding(Integer.parseInt(request.getParameter("measuRoundin_" + i)));
                } else {
                    measureProperty.setMeasureRounding(0);
                }
                if (request.getParameter("mesPreFix_" + i) != null && !request.getParameter("mesPreFix_" + i).equalsIgnoreCase("")) {
                    measureProperty.setMeasurePrefixDisplay(request.getParameter("mesPreFix_" + i));
                }
                if (request.getParameter("messufFix_" + i) != null && !request.getParameter("messufFix_" + i).equalsIgnoreCase("")) {
                    measureProperty.setMeasuresuffixDisplay(request.getParameter("messufFix_" + i));
                }

                if (request.getParameter("textvalueIn_" + i) != null && !request.getParameter("textvalueIn_" + i).equalsIgnoreCase("")) {
                    measureProperty.setValueIn(request.getParameter("textvalueIn_" + i));
                }
                if (request.getParameter("defaultDisplayIn_" + i) != null && !request.getParameter("defaultDisplayIn_" + i).equalsIgnoreCase("")) {
                    measureProperty.setDefaultDisplayin(request.getParameter("defaultDisplayIn_" + i));
                }
                if (request.getParameter("measureDisplay_" + i) != null && !request.getParameter("measureDisplay_" + i).equalsIgnoreCase("")) {
                    measureProperty.setMeasureDisplay(request.getParameter("measureDisplay_" + i));
                }

//                String gsonString = gson.toJson(measureProperty);
//                query = "update prg_grp_buss_table_details set COLUMN_PROPERTIES ='"+gsonString+"' where buss_table_id =" + table_id + " and buss_column_id =" + colID;

                query = "update prg_grp_buss_table_details set COLUMN_PROPERTIES ='" + gson.toJson(measureProperty) + "' where buss_table_id =" + table_id + " and buss_column_id =" + colID;
                queryList.add(query);
            }

            pbDb.executeMultiple(queryList);


        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return mapping.findForward("null");
    }

    public ActionForward checkRelationForTables(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String tabID = request.getParameter("tableID");
        PrintWriter out = null;
        try {
            PbBusinessGroupCopyDAO groupCopyDAO = new PbBusinessGroupCopyDAO();
            String outStr = groupCopyDAO.checkRelationForTables(tabID);
            out = response.getWriter();
            if (!outStr.equalsIgnoreCase("")) {
                out.print(outStr);
            } else {
                out.print(false);
            }
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }


        return null;
    }

    public ActionForward validateFormula(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String connId = request.getParameter("connId");
        String tableNames = request.getParameter("tableNames");
        String actFromula = request.getParameter("actFromula");
        String displayFormula = request.getParameter("displayFormula");
        String measures = request.getParameter("measures");
//        String operators=request.getParameter("operators");
//        String measureTypes=request.getParameter("measureTypes");
        String measureColumnType = request.getParameter("measureColumnType");//change to gsonArray
        String aggregationType = request.getParameter("aggregationType");
        String formulaName = request.getParameter("formulaName");
        String bussTableId = request.getParameter("bussTableId");
        JSONObject json = null;
        try {
            json = (JSONObject) new JSONParser().parse(measureColumnType);
        } catch (ParseException ex) {
            logger.error("Exception: ", ex);
        }
        SaveFormulaBD saveFormulaBD = new SaveFormulaBD();
        saveFormulaBD.setConnectionId(connId);
        saveFormulaBD.setTableName(tableNames);
        saveFormulaBD.setFormulaForSave(actFromula);
        saveFormulaBD.setDisplayFormula(displayFormula);
        saveFormulaBD.setMeasure(measures);
        saveFormulaBD.setMeasureType((String) json.get("measureType"));//send gson status
        saveFormulaBD.setFormulaName(formulaName);
        saveFormulaBD.setAggregationType(aggregationType);
        saveFormulaBD.setBussTableId(bussTableId);
        SaveFormulaDAO saveFormulaDAO = new SaveFormulaDAO(saveFormulaBD);
        boolean status = false;
        try {
            status = saveFormulaDAO.getStatusOfFormula();
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        if (status) {
            saveFormulaDAO.saveFormula();
            response.getWriter().print("true");
        } else {
            response.getWriter().print("false");
        }
        return null;
    }

    public HashMap getFactColumnsNames(HttpServletRequest request, String tabid, String groupId) {

//    HttpSession session = request.getSession(false);
        PbBusinessGroupEditDAO bgDao = new PbBusinessGroupEditDAO();
//      HashMap factcolnames=bgDao.getFactColumnsNames(tabid,groupId);
//      return factcolnames;
        return bgDao.getFactColumnsNames(tabid, groupId);
    }

// public String saveGroupHierarchy(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws IOException
// {
//      String perentmeasures=request.getParameter("selectedMeasuresliast");
//      String childmeasures=request.getParameter("childmeasures");
//      String busstabid=request.getParameter("busstabid");
//      PbBusinessGroupEditDAO groupDao = new PbBusinessGroupEditDAO();
//      groupDao.saveGroupHierarchy(busstabid, perentmeasures, childmeasures);
//      return null;
// }
    public String saveGroupHierarchy(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String perentmeasures = request.getParameter("selectedMeasuresliast");
        String childmeasures = request.getParameter("childmeasures");
        String busstabid = request.getParameter("busstabid");
        String groupId = request.getParameter("tabid");
        PbBusinessGroupEditDAO groupDao = new PbBusinessGroupEditDAO();
        groupDao.saveGroupHierarchy(groupId, busstabid, perentmeasures, childmeasures);
        return null;
    }

    public String saveGroupChildHierarchy(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String childMeasures = request.getParameter("selectedMeasuresliast");
        String tabId = request.getParameter("tabId");
        String parentId = request.getParameter("parentId");
        String groupId = request.getParameter("groupId");
        PbBusinessGroupEditDAO groupDao = new PbBusinessGroupEditDAO();
        groupDao.saveChildGroupHierarchy(childMeasures, tabId, parentId, groupId);
        return null;
    }
//ended by Nazneen
//public String getGroupHierarchy(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
//{
//    String hierid=request.getParameter("hid");
//    PbBusinessGroupEditDAO groupDao = new PbBusinessGroupEditDAO();
//    String str=  groupDao.getGroupHierarchy(hierid);
//    response.getWriter().print(str);
//    return null;
//}
//public ArrayList getChildColId(HttpServletRequest request,String perentid) throws Exception
//{
//     PbBusinessGroupEditDAO bgDao = new PbBusinessGroupEditDAO();
//      ArrayList list=  bgDao.getChildColId(perentid);
//
//    
//    return list;
//}
    public String getFolderidDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String groupid = request.getParameter("groupid");
        PbBusinessGroupEditDAO groupDao = new PbBusinessGroupEditDAO();
        String str = groupDao.getFolderidDetails(groupid);
//    response.getWriter().print(str);
        return null;
    }
//started by Nazneen
    public String saveHierarchyGroups(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tabId = request.getParameter("tabId");
        String GroupName = request.getParameter("GroupName");
        String GroupId = request.getParameter("groupsId");
//    PbBusinessGroupEditDAO groupDao = new PbBusinessGroupEditDAO();
//    int val=groupDao.saveHierarchyGroups(tabId,GroupName,GroupId);
        response.getWriter().print(new PbBusinessGroupEditDAO().saveHierarchyGroups(tabId, GroupName, GroupId));
        return null;
    }

    public String getGroups(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String hierid = request.getParameter("hid");
        PbBusinessGroupEditDAO groupDao = new PbBusinessGroupEditDAO();
        String str = groupDao.getGroups(hierid);
        response.getWriter().print(str);
        return null;
    }

    public String getChildsId(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        PbBusinessGroupEditDAO groupDao = new PbBusinessGroupEditDAO();
        String str = groupDao.getChildsId(id);
        response.getWriter().print(str);
        return null;
    }

    public String getGroupName(String tabid) throws Exception {
        PbBusinessGroupEditDAO groupDao = new PbBusinessGroupEditDAO();
        String str = groupDao.getGroupsName(tabid);
        return str;
    }

    public String getGroupTabId(String tabid) throws Exception {
        PbBusinessGroupEditDAO groupDao = new PbBusinessGroupEditDAO();
        String str = groupDao.getGroupTabId(tabid);
        return str;
    }

    public HashMap getChildList(String level, String grp_id) {

//      PbBusinessGroupEditDAO bgDao = new PbBusinessGroupEditDAO();
//      HashMap factcolnames=bgDao.getChildList(level,grp_id);
        return new PbBusinessGroupEditDAO().getChildList(level, grp_id);
    }

    public HashMap getHierarchyColumnsNames(String groupTabId) {
        PbBusinessGroupEditDAO bgDao = new PbBusinessGroupEditDAO();
        HashMap hierarchyColumnsNames = bgDao.getHierarchyColumnsNames(groupTabId);
        return hierarchyColumnsNames;
    }
// public HashMap getGroupId()
// {
//      PbBusinessGroupEditDAO bgDao = new PbBusinessGroupEditDAO();
//      HashMap groupId=bgDao.getGroupId();
//      return groupId;
// }
//ended by Nazneen
}
