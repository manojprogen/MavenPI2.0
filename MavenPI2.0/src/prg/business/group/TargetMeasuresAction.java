/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.business.group;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.progen.userlayer.action.GenerateDragAndDrophtml;
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

public class TargetMeasuresAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(TargetMeasuresAction.class);

    public ActionForward getTargetFacts(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("++++++++++++++++++++++++++++++++++");
        String grpId = request.getParameter("grpId");
        ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("grpId is:: "+grpId);
        // BusinessGroupListDAO bgList = new BusinessGroupListDAO();
        TargetMeasureParametersDAO bgList = new TargetMeasureParametersDAO();
        String tabView = bgList.getMeasureTables(grpId);
        request.setAttribute("targetMeasures", tabView);
        ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" tabView.. "+tabView);
        PrintWriter out = response.getWriter();
        out.print(tabView);

        //  return mapping.findForward("targetmeasuresaction");
        return mapping.findForward("success");
    }

    public ActionForward saveTargetFacts(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("++++++++++++++++++++++++++++++++++");
        String AllSelectedTabCols = request.getParameter("AllSelectedTabCols");
        String busGroup = request.getParameter("busGroup");
        ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(busGroup+" AllSelectedTabCols is:: "+AllSelectedTabCols);
        BusinessGroupListDAO bgList = new BusinessGroupListDAO();
        // bgList.addTargetMeasuresColumns(busGroup,AllSelectedTabCols);

        PrintWriter out = response.getWriter();
        // ut.print(tabView);

        //  return mapping.findForward("targetmeasuresaction");
        return mapping.findForward("null");
    }

    public ActionForward getSavedTargetFacts(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("++++++++++++++++++++++++++++++++++");
        // String AllSelectedTabCols = request.getParameter("AllSelectedTabCols");
        // ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("AllSelectedTabCols is:: "+AllSelectedTabCols);
        BusinessGroupListDAO bgList = new BusinessGroupListDAO();
        String busGroup = request.getParameter("busGroup");

        ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(busGroup+" busGroup --");
        // String value = bgList.getAddedTargetMeasures(busGroup);//getAddedMeasureColList();//AllSelectedTabCols);
        // ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" value ."+value);
        // PrintWriter out = response.getWriter();
        // out.print(value);

        //  return mapping.findForward("targetmeasuresaction");
        return mapping.findForward("null");
    }

    public ActionForward getTargetFactsParameters(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("++++++++++++++++++++++++++++++++++");
        // String AllSelectedTabCols = request.getParameter("AllSelectedTabCols");
        // ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("AllSelectedTabCols is:: "+AllSelectedTabCols);
        BusinessGroupListDAO bgList = new BusinessGroupListDAO();
        String busGroup = request.getParameter("busGroup");
        String AllSelectedTabCols = request.getParameter("AllSelectedTabCols");
        ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(busGroup+" busGroup --");
        request.setAttribute("busGroup", busGroup);
        request.setAttribute("AllSelectedTabCols", AllSelectedTabCols);

        return mapping.findForward("targetmeasuresparameters");
        // return mapping.findForward("null");
    }

    public ActionForward saveTargetFactsParameters(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("++++++++++++++++++++++++++++++++++");
        // String AllSelectedTabCols = request.getParameter("AllSelectedTabCols");
        // ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("AllSelectedTabCols is:: "+AllSelectedTabCols);


        String busGroup = request.getParameter("busGroup");
        String AllSelectedTabCols = request.getParameter("AllSelectedTabCols");
        String allValues = request.getParameter("allValues");
        String eleIds = request.getParameter("eleIds");
        ////////////////////////////////////////////////////////////////////////////////.println.println(eleIds+" allValues .. "+allValues);
        allValues = allValues.substring(1);
        eleIds = eleIds.substring(1);
        ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(busGroup+" busGroup -- AllSelectedTabCols "+AllSelectedTabCols);
        AllSelectedTabCols = AllSelectedTabCols.substring(1);
        ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" after sub "+AllSelectedTabCols);
        TargetMeasureParametersDAO bgList = new TargetMeasureParametersDAO();
        // BusinessGroupListDAO bgList = new BusinessGroupListDAO();
        String tabColCombo[] = AllSelectedTabCols.split(",");
        for (int t = 0; t < tabColCombo.length; t++) {
            ////////////////////////////////////////////////////////////////////////.println.println(" tabColCombo[t] --- "+tabColCombo[t]);
            bgList.addTargetMeasuresColumns(busGroup, "," + tabColCombo[t], allValues, eleIds);
        }

        // return mapping.findForward("targetmeasuresparameters");
        return mapping.findForward("null");
    }

    public ActionForward addTargetFactsParameters(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("++++++++++++++++++++++++++++++++++");
        // String AllSelectedTabCols = request.getParameter("AllSelectedTabCols");
        // ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("AllSelectedTabCols is:: "+AllSelectedTabCols);

        String busGroup = request.getParameter("busGroup");
        String measureId = request.getParameter("measureId");
        String allValues = request.getParameter("allValues");
        ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(busGroup+" allValues .. "+measureId+" allValues "+allValues);
        allValues = allValues.substring(1);

        TargetMeasureParametersDAO bgList = new TargetMeasureParametersDAO();
        bgList.addExtraColumns(busGroup, allValues, measureId);


        // return mapping.findForward("targetmeasuresparameters");
        return mapping.findForward("null");
    }

    public ActionForward updateFactColumnFlag(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("++++++++++++++++++++++++++++++++++");
        // String AllSelectedTabCols = request.getParameter("AllSelectedTabCols");
        // ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("AllSelectedTabCols is:: "+AllSelectedTabCols);

        String tableId = request.getParameter("tableId");
        String columnId = request.getParameter("columnId");
        ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(columnId+" columnId -,tableId "+tableId);

        BusinessGroupListDAO bgList = new BusinessGroupListDAO();
        bgList.updateFactColumnFlag(tableId, columnId.substring(1));



        // return mapping.findForward("targetmeasuresparameters");
        return mapping.findForward("null");
    }

    public ActionForward getDisplayNames(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        int tabId = Integer.parseInt(request.getParameter("tabid"));
        BusinessGroupListDAO adddisplaynames = new BusinessGroupListDAO();
        String s = adddisplaynames.getAddQuickForumla(tabId);


        response.getWriter().print(s);

        return mapping.findForward("null");
    }

    public ActionForward getCustomerTableDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String dimentionID = request.getParameter("dimID");

        String grpID = request.getParameter("grpID");

        BusinessGroupListDAO businessGroupListDAO = new BusinessGroupListDAO();
        String dimentionJson = businessGroupListDAO.getCustomerTableDetails(dimentionID, grpID);

        response.getWriter().print(dimentionJson);

        return mapping.findForward("null");
    }

    public ActionForward dimensions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String groupid = request.getParameter("groupId");
        String bussTableId = request.getParameter("bussTableId");

        BusinessGroupListDAO businessGroupListDAO = new BusinessGroupListDAO();
        String dim = businessGroupListDAO.getallDimensions(groupid, bussTableId);
//        String dimid=businessGroupListDAO.getDimId(groupid);
//         String s=businessGroupListDAO.getBussTableName(dim);
        response.getWriter().print(dim);


        return null;
    }

    public ActionForward saveTragetDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String connectionId = (String) request.getSession(false).getAttribute("connId");
        String[] durationDetails = request.getParameterValues("durationDetails");
        String[] dimentionvalue = request.getParameterValues("dimentionvalue");
        String[] targetvalue = request.getParameterValues("targetvalue");
        String grupId = request.getParameter("grupId");
        String dimAndMemId = request.getParameter("dimentionList");
        String busscolid = request.getParameter("busscolid");
        BusinessGroupListDAO businessGroupListDAO = new BusinessGroupListDAO();
        String tableNameforDimentions = businessGroupListDAO.saveTragetDetails(connectionId, durationDetails, dimentionvalue, targetvalue, grupId, dimAndMemId);
        if (!tableNameforDimentions.equalsIgnoreCase("")) {
            businessGroupListDAO.pushBusinessGroup(dimAndMemId, grupId, tableNameforDimentions, connectionId, busscolid);
        }
        return null;
    }
    //start by E

    public ActionForward getConnectionNames(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BusinessGroupListDAO businessGroupListDAO = new BusinessGroupListDAO();
        String connectionNames = businessGroupListDAO.getConnectionNames();
        response.getWriter().print(connectionNames);

        return null;
    }

    public ActionForward getDisplayTableNames(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String conId = request.getParameter("conId");
        BusinessGroupListDAO businessGroupListDAO = new BusinessGroupListDAO();
        String tableNames = businessGroupListDAO.getDisplayTableNames(conId);
//        String dimid=businessGroupListDAO.getDimId(groupid);
//         String s=businessGroupListDAO.getBussTableName(dim);
        response.getWriter().print(tableNames);


        return null;
    }

    public ActionForward getTableColumns(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tabName = request.getParameter("tabname");
        String conId = request.getParameter("connectionID");

        BusinessGroupListDAO businessGroupListDAO = new BusinessGroupListDAO();
        String columnNames = businessGroupListDAO.getTableColumns(tabName, conId);
//        String dimid=businessGroupListDAO.getDimId(groupid);
//         String s=businsegColNamesessGroupListDAO.getBussTableName(dim);
        response.getWriter().print(columnNames);
        return null;
    }

    public ActionForward getSegmentColumns(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tablename = request.getParameter("tablename");
        String connectionId = request.getParameter("connectionId");
        String segcolumns = request.getParameter("segcolumns");
        String membervalue = request.getParameter("membervalue");
        ArrayList memberdroplist = new ArrayList();
        memberdroplist.add(membervalue);
        BusinessGroupListDAO businessGroupListDAO = new BusinessGroupListDAO();
        ArrayList segColNames = businessGroupListDAO.getSegmentColumns(tablename, segcolumns, connectionId);
        GenerateDragAndDrophtml generateDragAndDrophtml = null;
        if (membervalue != null) {
            generateDragAndDrophtml = new GenerateDragAndDrophtml("select columns from below", "drop columns here", memberdroplist, segColNames, request.getContextPath());
        } else {
            generateDragAndDrophtml = new GenerateDragAndDrophtml("select columns from below", "drop columns here", null, segColNames, request.getContextPath());
        }
        String dragndrop = generateDragAndDrophtml.getDragAndDropDiv();

        response.getWriter().print(dragndrop);
        return null;
    }

    public ActionForward getDependColumns(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tablename = request.getParameter("tablename");
        String connectionId = request.getParameter("connectionId");
        String dependCols = request.getParameter("dependCols");
        BusinessGroupListDAO businessGroupListDAO = new BusinessGroupListDAO();
        String dependColumns = businessGroupListDAO.getDependColumns(tablename, dependCols, connectionId);

        response.getWriter().print(dependColumns);
        return null;
    }
//end by E

//added by ramesh
    public ActionForward saveSegmentationValues(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String relationName = request.getParameter("relationName");
        String connId = request.getParameter("connId");
        String tableName = request.getParameter("tableName");
        String dependColumn = request.getParameter("dependColumn");
        String baseColumn = request.getParameter("baseColumn");
        String grpmembers[] = request.getParameterValues("membergrp");
        String grproles[] = request.getParameterValues("selectedroles");
        String grpmembervalues[] = request.getParameterValues("membervalues");
        List<String> grpmember = new ArrayList<String>();
        List<String> grprole = new ArrayList<String>();
        List<String> grpmembervalue = new ArrayList<String>();
        grpmember.addAll(Arrays.asList(grpmembers));
        grprole.addAll(Arrays.asList(grproles));
        grpmembervalue.addAll(Arrays.asList(grpmembervalues));
        RuleHelper helper = null;
        List<RuleHelper> helpers = new ArrayList<RuleHelper>();
        for (int i = 0; i < grpmember.size(); i++) {
            helper = new RuleHelper();
            helper.setBaseColmnName(baseColumn);
            helper.setDependColumnName(dependColumn);
            helper.setTableName(tableName);
            helper.setConnectionid(connId);
            helper.setGroupmember(grpmember.get(i));
            helper.setRule(grprole.get(i));
            helper.setGroupvalues(Arrays.asList(grpmembervalue.get(i).split(",")));
            helpers.add(helper);
        }
        Gson json = new Gson();
        String groupjson = json.toJson(helpers);

        BusinessGroupListDAO groupList = new BusinessGroupListDAO();
        groupList.saveSegmentationValues(relationName, tableName, connId, dependColumn, baseColumn, groupjson, grpmember, grpmembervalue, grprole);

        return null;
    }

    public ActionForward getFlexiSegmentationTableNames(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String conid = request.getParameter("conId");
        BusinessGroupListDAO businessGroupListDAO = new BusinessGroupListDAO();
        String flexitablenames = businessGroupListDAO.getFlexiSegmentationTableNames(conid);
        response.getWriter().print(flexitablenames);
        return null;
    }
    //by sunita

    public ActionForward moveFlexiSegmentationTableData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String conid = request.getParameter("conId");
        String statictable = request.getParameter("statictable");
        String staticview = request.getParameter("staticview");
        BusinessGroupListDAO businessGroupListDAO = new BusinessGroupListDAO();
        businessGroupListDAO.moveFlexiSegmentationTableData(conid, statictable, staticview);

        return null;
    }

    public ActionForward getFlexiColumns(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tablename = request.getParameter("tablename");
        String flextableid = request.getParameter("flextableid");
//        
        String conid = request.getParameter("conId");
        BusinessGroupListDAO flexigrouplistdao = new BusinessGroupListDAO();
        HashMap segColNames = flexigrouplistdao.flexigrouplistdao(flextableid, conid);
        String flexiname = flexigrouplistdao.getFlexiName(flextableid);
        if (tablename.equalsIgnoreCase("country")) {
            GenerateDragAndDrophtml generateDragAndDrophtml = new GenerateDragAndDrophtml("select columns from below", "drop columns here", null, (ArrayList<String>) segColNames.get("NameList"), request.getContextPath());
            generateDragAndDrophtml.setDragableListNames((ArrayList<String>) segColNames.get("NameList"));
            String dragndrop = generateDragAndDrophtml.getDragAndDropDiv();
            response.getWriter().print(dragndrop);
        } else {
            GenerateDragAndDrophtml generateDragAndDrophtml = new GenerateDragAndDrophtml("select columns from below", "drop columns here", null, (ArrayList<String>) segColNames.get("IdList"), request.getContextPath());
            generateDragAndDrophtml.setDragableListNames((ArrayList<String>) segColNames.get("NameList"));
            String dragndrop = generateDragAndDrophtml.getDragAndDropDiv();
            response.getWriter().print(dragndrop);
        }

        return null;
    }

    public ActionForward saveFlexiSegmentationTableNames(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String conId = request.getParameter("conId");
        String tablename = request.getParameter("tablename");
        String grpName = request.getParameter("grpName");
        String flexiid = request.getParameter("flexiid");
        BusinessGroupListDAO dao = new BusinessGroupListDAO();
        String membergrp[] = request.getParameterValues("membergrp");
        String selectedroles[] = request.getParameterValues("selectedroles");
        String membervalues[] = request.getParameterValues("membervalues");
        String memberIds[] = request.getParameterValues("hiddenmembervalueid");
        String flexitableid = request.getParameter("flexitableid");
        String membergrppath = request.getParameter("membergrouptotalpath");
        String membervaluepath = request.getParameter("membervaluepath");
        String hiddenmemid = request.getParameter("hiddenmempath");
        String tabid = request.getParameter("tabid");
        List<String> grpmember = new ArrayList<String>();
        List<String> grprole = new ArrayList<String>();
        grpmember.addAll(Arrays.asList(membergrp));
        grprole.addAll(Arrays.asList(selectedroles));
        if (membergrppath != null && membervaluepath != null) {
            dao.insertFlexiSegmentation(conId, tablename, grpName, grprole, memberIds, flexiid, flexitableid, membergrppath, membervaluepath, hiddenmemid, tabid);

        } else {
            dao.saveFlexiSegmentation(conId, tablename, grpName, grpmember, grprole, membervalues, memberIds, flexiid, flexitableid);

        }
        return null;

    }

    public ActionForward getSegmentationNames(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BusinessGroupListDAO segmntationListDao = new BusinessGroupListDAO();
        String segmentaiondetails = segmntationListDao.getSegmentationNames();
        response.getWriter().print(segmentaiondetails);

        return null;
    }

    public ActionForward updateSegmentationValues(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String conid = request.getParameter("conid");
        String tableName = request.getParameter("tableName");
        String dependColumn = request.getParameter("dependColumn");
        String baseColumn = request.getParameter("baseColumn");
        String grpmembers[] = request.getParameterValues("membergrp");
        String grproles[] = request.getParameterValues("selectedroles");
        String grpmembervalues[] = request.getParameterValues("membervalues");
        List<String> grpmember = new ArrayList<String>();
        List<String> grprole = new ArrayList<String>();
        List<String> grpmembervalue = new ArrayList<String>();
        grpmember.addAll(Arrays.asList(grpmembers));
        grprole.addAll(Arrays.asList(grproles));
        grpmembervalue.addAll(Arrays.asList(grpmembervalues));
        BusinessGroupListDAO segmntationListDao = new BusinessGroupListDAO();
        segmntationListDao.updateSegmentationValues(conid, tableName, dependColumn, baseColumn, grpmember, grpmembervalue, grprole);
        return null;
    }

    public ActionForward deleteSegmentationValues(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String segmentid = request.getParameter("segmentid");
        String conid = request.getParameter("connId");
        String tablename = request.getParameter("tableName");
        String dependColumn = request.getParameter("dependentcolumnname");
        String baseColumn = request.getParameter("basecolumnname");
        String grpmembers = request.getParameter("membergrpid");
        String grproles = request.getParameter("selectedroles");
        String grpmembervalues = request.getParameter("membervalueid");
        String relationname = request.getParameter("relation");
        String grpmembers1[] = request.getParameterValues("membergrp");
        String grproles1[] = request.getParameterValues("selectedroles");
        String grpmembervalues1[] = request.getParameterValues("membervalues");
        List<String> grpmember = new ArrayList<String>();
        List<String> grprole = new ArrayList<String>();
        List<String> grpmembervalue = new ArrayList<String>();
        grpmember.addAll(Arrays.asList(grpmembers1));
        grprole.addAll(Arrays.asList(grproles1));
        grpmembervalue.addAll(Arrays.asList(grpmembervalues1));
        List<String> memvalues = new ArrayList<String>();
        for (int i = 0; i < grpmembervalue.size(); i++) {
            if (grpmembervalues.equals(grpmembervalue.get(i))) {
                String members = "";
                memvalues.add(members);
            } else {
                String member = grpmembervalue.get(i);
                memvalues.add(member);
            }
        }
        RuleHelper helper = null;
        List<RuleHelper> helpers = new ArrayList<RuleHelper>();

        helper = new RuleHelper();
        helper.setBaseColmnName(baseColumn);
        helper.setDependColumnName(dependColumn);
        helper.setTableName(tablename);
        helper.setConnectionid(conid);
        helper.setGroupmember(grpmember.get(0));
        helper.setRule(grprole.get(0));
        helper.setGroupvalues(Arrays.asList(Joiner.on(",").join(memvalues).split(",")));
        helpers.add(helper);


        Gson json = new Gson();
        String groupjson = json.toJson(helpers);
        BusinessGroupListDAO deletesegmentationDao = new BusinessGroupListDAO();
        deletesegmentationDao.deleteSegmentationValues(segmentid, relationname, tablename, conid, dependColumn, baseColumn, groupjson, grpmember, grpmembervalue, grprole);


        return null;
    }

    public ActionForward getFlexiSegmentationTableColumns(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tablename = request.getParameter("tablename");
        String flexioriginalid = request.getParameter("flexioriginalid");
        String fileximapid = request.getParameter("fileximapid");
        String conid = request.getParameter("conId");
        String membervalues[] = request.getParameterValues("membervalues");
        BusinessGroupListDAO getFlexiTableListDao = new BusinessGroupListDAO();
        String flexistring = null;
        for (int i = 0; i < membervalues.length; i++) {
            String grpvalues = membervalues[i];
            flexistring = getFlexiTableListDao.getFlexiSegmentationTableColumns(tablename, conid, fileximapid, flexioriginalid, grpvalues);
        }
        response.getWriter().print(flexistring);

        return null;
    }

    public ActionForward saveFlexiSegmentationTableColumnNames(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String grpname = request.getParameter("grpname");
        String groupId = request.getParameter("groupId");
        String tablename = request.getParameter("tablename");
        String connId = request.getParameter("connId");
        String[] checkvalues = request.getParameterValues("flexiname");
        BusinessGroupListDAO dao = new BusinessGroupListDAO();
        if (checkvalues != null) {
            for (String checkkey : checkvalues) {
                String checkStr = checkkey.replace("flexiname", "flexicolumnvalue");
                String idStr = checkkey.replace("flexiname", "idlist");
                String dispName = request.getParameter(checkStr.trim()).toString();
                String Ids = request.getParameter(idStr.trim()).toString();

                dao.saveFlexiSegmentationTableColumnNames(tablename, grpname, groupId, dispName, Ids, connId);

            }
        }
        return null;
    }

    public ActionForward getFlexiNames(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String conid = request.getParameter("conid");
        BusinessGroupListDAO flexilistDAO = new BusinessGroupListDAO();
        String flexinames = flexilistDAO.getFlexiNames(conid);
        response.getWriter().print(flexinames);
        return null;
    }

    public ActionForward getFlexiMemberNames(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String groupname = request.getParameter("groupname");
        BusinessGroupListDAO flexilistDAO = new BusinessGroupListDAO();
        String flexinames = flexilistDAO.getFlexiMemberNames(groupname);
        response.getWriter().print(flexinames);
        return null;
    }

    public ActionForward deleteFlexiValues(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String grupmember = request.getParameter("membergrpid");
        String grpvalues = request.getParameter("membervalueid");
        String flexiidnum = request.getParameter("flexiidnum");
        BusinessGroupListDAO flexilistDAO = new BusinessGroupListDAO();
        flexilistDAO.deleteFlexiValues(grupmember, grpvalues, flexiidnum);
        return null;
    }

    public ActionForward getGroupnames(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String connId = request.getParameter("connId");
        BusinessGroupListDAO grouplist = new BusinessGroupListDAO();
        String groupnames = grouplist.getGroupnames(connId);
        response.getWriter().print(groupnames);


        return null;

    }

    public ActionForward getGroupnames1(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String connId = request.getParameter("connId");
        String tabname = request.getParameter("tabname");

        BusinessGroupListDAO grouplist = new BusinessGroupListDAO();
        String groupnames = grouplist.getGroupnames1(connId, tabname);
        response.getWriter().print(groupnames);


        return null;

    }

    public ActionForward getRecords(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String connId = request.getParameter("connId");
        String typesname = request.getParameter("typesname");

        BusinessGroupListDAO grouplist = new BusinessGroupListDAO();
        String flexinames = grouplist.getRecords(connId, typesname);
        response.getWriter().print(flexinames);


        return null;

    }

    public ActionForward getTablenames(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String connId = request.getParameter("connId");
        BusinessGroupListDAO grouplist = new BusinessGroupListDAO();
        String tablenames = grouplist.getTablenames(connId);
        response.getWriter().print(tablenames);


        return null;

    }

    public ActionForward getTypenames(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String connId = request.getParameter("connId");
        String grpname = request.getParameter("groupsname");

        BusinessGroupListDAO typelist = new BusinessGroupListDAO();
        String typenames = typelist.getTypenames(connId, grpname);
        response.getWriter().print(typenames);


        return null;

    }

    public ActionForward deleteGroupNames(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String connId = request.getParameter("connId");
        String groupnameurl = request.getParameter("grpnameurl");
        String groupnames[] = groupnameurl.split(",");
        BusinessGroupListDAO deletegrouplist = new BusinessGroupListDAO();
        for (int i = 0; i < groupnames.length; i++) {
            String groupname = groupnames[i];
            deletegrouplist.deleteGroupNames(connId, groupname);
        }



        return null;
    }
//by sunita

    public ActionForward deleteTypeNames(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String connId = request.getParameter("connId");
        String typenameurl = request.getParameter("typnameurl");
        String typenames[] = typenameurl.split(",");
        BusinessGroupListDAO deletetypelist = new BusinessGroupListDAO();
        for (int i = 0; i < typenames.length; i++) {
            String typename = typenames[i];
            deletetypelist.deleteTypeNames(connId, typename);
        }



        return null;
    }

    public ActionForward deleteRecord(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String connId = request.getParameter("connId");
        String tablename = request.getParameter("tablename");
        String groupname = request.getParameter("groupname");
        String typename = request.getParameter("typename");
        String flexiname = request.getParameter("flexiname");
        String flexitext = request.getParameter("flexitext");
        String flexinum = request.getParameter("flexinum");
        String flexitable = request.getParameter("flexitable");

        BusinessGroupListDAO deleteRecordlist = new BusinessGroupListDAO();
        deleteRecordlist.deleteRecord(connId, tablename, groupname, typename, flexiname, flexitext, flexinum, flexitable);
        return null;
    }

    public ActionForward deleteTableNames(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String connId = request.getParameter("connId");
        String tablenameurl = request.getParameter("tabnameurl");
        String tablenames[] = tablenameurl.split(",");
        BusinessGroupListDAO deletetablelist = new BusinessGroupListDAO();
        for (int i = 0; i < tablenames.length; i++) {
            String tablename = tablenames[i];
            deletetablelist.deleteTableNames(connId, tablename);
        }



        return null;
    }

    public ActionForward editRelations(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String segmentid = request.getParameter("segmentid");
        String relationname = request.getParameter("relationId");
        BusinessGroupListDAO deleteRelationslist = new BusinessGroupListDAO();
        String relation = deleteRelationslist.deleteRelations(segmentid, relationname);
        return null;

    }

    public ActionForward refreshjs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("SaveQuickForm");

    }

    protected Map getKeyMethodMap() {
        ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" in map --");
        Map map = new HashMap(); //
        map.put("getTargetFacts", "getTargetFacts");
        map.put("saveTargetFacts", "saveTargetFacts");
        map.put("getSavedTargetFacts", "getSavedTargetFacts"); //
        map.put("getTargetFactsParameters", "getTargetFactsParameters"); //saveTargetFactsParameters
        map.put("saveTargetFactsParameters", "saveTargetFactsParameters");
        map.put("addTargetFactsParameters", "addTargetFactsParameters");
        map.put("updateFactColumnFlag", "updateFactColumnFlag");
        map.put("getDisplayNames", "getDisplayNames");
        map.put("getCustomerTableDetails", "getCustomerTableDetails");
        map.put("dimensions", "dimensions");
        map.put("saveTragetDetails", "saveTragetDetails");
        map.put("getDisplayTableNames", "getDisplayTableNames");
        map.put("getTableColumns", "getTableColumns");
        map.put("getConnectionNames", "getConnectionNames");
        map.put("getSegmentColumns", "getSegmentColumns");
        map.put("getDependColumns", "getDependColumns");
        map.put("saveSegmentationValues", "saveSegmentationValues");
        map.put("getFlexiSegmentationTableNames", "getFlexiSegmentationTableNames");
        map.put("getFlexiColumns", "getFlexiColumns");
        map.put("saveFlexiSegmentationTableNames", "saveFlexiSegmentationTableNames");
        map.put("getSegmentationNames", "getSegmentationNames");
        map.put("updateSegmentationValues", "updateSegmentationValues");
        map.put("deleteSegmentationValues", "deleteSegmentationValues");
        map.put("getFlexiSegmentationTableColumns", "getFlexiSegmentationTableColumns");
        map.put("saveFlexiSegmentationTableColumnNames", "saveFlexiSegmentationTableColumnNames");
        map.put("getFlexiNames", "getFlexiNames");
        map.put("getFlexiMemberNames", "getFlexiMemberNames");
        map.put("deleteFlexiValues", "deleteFlexiValues");
        map.put("getGroupnames", "getGroupnames");
        map.put("getGroupnames1", "getGroupnames1");
        map.put("getRecords", "getRecords");
        map.put("getTablenames", "getTablenames");
        map.put("getTypenames", "getTypenames");
        map.put("deleteGroupNames", "deleteGroupNames");
        map.put("deleteTableNames", "deleteTableNames");
        map.put("deleteTypeNames", "deleteTypeNames");
        map.put("deleteRecord", "deleteRecord");
        map.put("editRelations", "editRelations");
        map.put("refreshjs", "refreshjs");
        map.put("moveFlexiSegmentationTableData", "moveFlexiSegmentationTableData");
        return map;
    }
}