package com.progen.report.display;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.progen.connection.ConnectionDAO;
import com.progen.connection.ConnectionMetadata;
import com.progen.report.PbReportCollection;
import com.progen.report.ReportManagementDAO;
import com.progen.report.ReportObjectQuery;
import com.progen.report.query.PbTimeRanges;
import com.progen.report.query.ProgenAOQuery;
import com.progen.reportview.db.PbReportViewerDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import prg.business.group.BusinessGroupDAO;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;
//import com.progen.target.display.DisplayTargetParameters;

public class GetParameterLov extends HttpServlet {

    public static Logger logger = Logger.getLogger(GetParameterLov.class);
    private boolean isCompanyValid = false;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //Added by Ram 29Nov2015 for Language Lookup
        String reportID = request.getParameter("REPORTID");
        Container container = Container.getContainerFromSession(request, reportID);
        ReportManagementDAO dao1 = new ReportManagementDAO();
        String aoAsGoId=container.aoAsGoId;
//        ReportTemplateDAO rto=new ReportTemplateDAO();
//        PbReturnObject retObject = null;
//        String lookupViewBys=container.getLookupViewBys();
//        retObject=rto.getLookupData(lookupViewBys);
//        HashMap lookup=new HashMap();
//        HashMap filterLookupData=new HashMap();
//
//        if(retObject !=null){
//        for(int i=0;i<retObject.getRowCount();i++)
//        {
//            lookup.put(retObject.getFieldValueString(i, 0), retObject.getFieldValueString(i, 1));
//        }
//        }
        HashMap lookup = new HashMap();
        HashMap filterLookupData = new HashMap();
        lookup = container.getFilterLookupOriginalToNew();
        filterLookupData = container.getFilterLookupData();
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        ResultSet totalrs = null;
        String piVersion = "piVersion2014";  // added by amar
        String startValue = request.getParameter("startValue");
        String allParamIds = request.getParameter("allParamIds"); //.replaceAll("CBO", "");
        String parArrVals = request.getParameter("parArrVals");
        //  String reportID = request.getParameter("REPORTID");
        String orderVal = request.getParameter("orderVal");
        String fromglobal = request.getParameter("fromglobal");//added by sandeep
        String tabtype = request.getParameter("tabtype");//added by sandeep
        ArrayList filterListGraph;
         String ao_Name="";
        ProgenAOQuery pbAoQuery = new ProgenAOQuery();
         if (aoAsGoId != null && !aoAsGoId.equalsIgnoreCase("")) {
                ao_Name="M_AO_" + aoAsGoId;
            } else {
                ao_Name="R_GO_" + reportID;
            }
        
//        String ao_Name = "R_GO_" + reportID;
        String reportData = "";
//        if(fromglobal!=null && fromglobal.equalsIgnoreCase("true")){
//parArrVals="["+parArrVals+"];";
//        }
        request.getSession().getAttribute("REPORTID");
        Gson gson = new Gson();
        Type typeList = new TypeToken<List<String>>() {
        }.getType();
        // HashMap myHashMap = (HashMap) request.getSession().getAttribute("objectName-" + reportID);
        ArrayList elementIds = new ArrayList();
        ArrayList values = new ArrayList();
        HashMap valuesMap = new HashMap();
        if (allParamIds != null && parArrVals != null) {
            if (allParamIds != null && parArrVals != null) {
                if ((!allParamIds.equalsIgnoreCase("")) && (!parArrVals.equalsIgnoreCase(""))) {
                    String[] paramId = allParamIds.split(",");
                    String[] paramVal = parArrVals.split(";");
                    /*
                     * modified by Srikanth.p :start
                     */
                    ArrayList<List<String>> filterList = new ArrayList<List<String>>();
                    for (int i = 0; i < paramVal.length; i++) {
                        // added by sandeep for global filter with multiselect
                        if (fromglobal != null && fromglobal.equalsIgnoreCase("true")) {
                            List<String> list = new LinkedList();
                            if (paramVal[i].equalsIgnoreCase("All")) {
                                list.add(paramVal[i]);
                            } else {
                                String[] runavalues = paramVal[i].split(",");
                                for (int iv = 0; iv < runavalues.length; iv++) {
                                    list.add(runavalues[iv]);
                                    //Added by Ram
                                    if (filterLookupData.containsKey(runavalues[iv])) {
                                        list.set(iv, filterLookupData.get(runavalues[iv]).toString());
                                    }
                                    //Ended by Ram
                                }
                            }

                            filterList.add(list);
                        } else {
                            List<String> list = gson.fromJson(paramVal[i], typeList);
                            filterList.add(list);
                        }
                        // end of sandeep code
                    }

//                    ArrayList paramList = null;
                    for (int y = 0; y < paramId.length; y++) {
                        elementIds.add(paramId[y]);
                    }
                    for (int n = 0; n < filterList.size(); n++) {
                        values.add(filterList.get(n));
                        // valuesMap.put(paramId[n],paramVal[n]);
                        //changed on 18thNov start
                        valuesMap.put(paramId[n], filterList.get(n));
                        //changed on 18thNov over
                    }
                }
            }
        }

        //modified by susheela start 03-12-09
        String userId = (String) request.getSession().getAttribute("USERID");
//        String roleQ = " select * from prg_ar_report_details where report_id=" + reportID;
        //////////////////////////////////////////////////////////////////////////////////.println.println(" roleQ "+roleQ);
        PbDb pbDb = new PbDb();
        HashMap membersEle = new HashMap();
        HashMap bussTablIds = new HashMap();
        HashMap mems = new HashMap();
        HashMap memberFilters = new HashMap();
        try {
//            PbReturnObject roleObj = pbDb.execSelectSQL(roleQ);
            ArrayList filterMemIds = new ArrayList();
//            String roleId = "";
//            for (int m = 0; m < roleObj.getRowCount(); m++) {
//                roleId = roleId + "," + roleObj.getFieldValueString(m, 3);
//            }
//
//            if (roleId.length() > 1) {
//                roleId = roleId.substring(1);
//            }
            String reportFilterQ = "";
            PbReturnObject filterObj = new PbReturnObject();

            reportFilterQ = "select * from prg_ar_parameter_security where report_id=" + reportID;
            //////////////////////////////////////////////////////////////////////////.println.println(" reportFilterQ 989 lov "+reportFilterQ);
            if (filterObj.getRowCount() == 0) {
                filterObj = pbDb.execSelectSQL(reportFilterQ);
            }
            for (int m = 0; m < filterObj.getRowCount(); m++) {
                String memId2 = (filterObj.getFieldValueString(m, "MEMBER_ID"));
                String prevClause = "";
                String newClause = "";
                String totalClause = "";
                if (memberFilters.containsKey(memId2)) {
                    prevClause = (String) memberFilters.get(memId2);
                    newClause = filterObj.getFieldValueClobString(m, "MEMBER_VALUE");
                    totalClause = prevClause + "," + newClause;
                } else {
                    memberFilters.put(filterObj.getFieldValueString(m, "MEMBER_ID"), filterObj.getFieldValueClobString(m, "MEMBER_VALUE"));
                    filterMemIds.add(filterObj.getFieldValueString(m, "MEMBER_ID"));
                }
            }

            //uncommented by bhharathi
            if (isCompanyValid) {
                String filterQueryAccount = "select * from prg_account_member_filter where account_no in(select account_type from prg_ar_users where pu_id=" + userId + ") ";
                if (filterObj.getRowCount() == 0) {
                    filterObj = pbDb.execSelectSQL(filterQueryAccount);
                    for (int m = 0; m < filterObj.getRowCount(); m++) {
                        String memId2 = (filterObj.getFieldValueString(m, "MEMBER_ID"));
                        String prevClause = "";
                        String newClause = "";
                        String totalClause = "";
                        if (memberFilters.containsKey(memId2)) {
                            prevClause = (String) memberFilters.get(memId2);
                            newClause = filterObj.getFieldValueClobString(m, "MEMBER_VALUE");
                            totalClause = prevClause + "," + newClause;
                        } else {
                            memberFilters.put(filterObj.getFieldValueString(m, "MEMBER_ID"), filterObj.getFieldValueClobString(m, "MEMBER_VALUE"));
                            filterMemIds.add(filterObj.getFieldValueString(m, "MEMBER_ID"));
                        }

                    }

                }
            }
            //ends
//             CubeInterface cubeInterface = new CubeInterface();
//            Cube cube = cubeInterface.getCube(Integer.parseInt(roleId));
//            HashMap filterMembersAtUserHM = cube.getWhereClauseMembers(Integer.parseInt(userId));
//            Set<String> memBerKeysSet = filterMembersAtUserHM.keySet();
//            for (String keyStr : memBerKeysSet) {
//                 String prevClause = "";
//                 String newClause = "";
//                 String totalClause = "";
//                if (memberFilters.containsKey(keyStr)) {
//                      prevClause = (String) filterMembersAtUserHM.get(keyStr);
//                      newClause = (String) memberFilters.get(keyStr);
//                    totalClause = prevClause + "," + newClause;
//                } else {
//                    memberFilters.put(keyStr,(String) filterMembersAtUserHM.get(keyStr));
//                    if(!filterMemIds.contains(keyStr))
//                    filterMemIds.add(keyStr);
//                }
//
//            }
//             HashMap filterMembersAtRoleHM = cube.getWhereClauseMembers(-1);
//             memBerKeysSet=filterMembersAtRoleHM.keySet();
//             for(String keyStr:memBerKeysSet){
//                String prevClause = "";
//                String newClause = "";
//                String totalClause = "";
//                 if (memberFilters.containsKey(keyStr)) {
//                      prevClause = (String) filterMembersAtRoleHM.get(keyStr);
//                      newClause = (String) memberFilters.get(keyStr);
//                     totalClause = prevClause + "," + newClause;
//                } else {
//                    memberFilters.put(keyStr,(String) filterMembersAtRoleHM.get(keyStr));
//                     if(!filterMemIds.contains(keyStr))
//                    filterMemIds.add(keyStr);
//                }
//
//
//             }
//            String filterQuery = "select * from prg_user_role_member_filter where user_id=" + userId + " and folder_id in(" + roleId + ")";
//
//            ////////////////////////////////////.println.println("filterQuery is " + filterQuery);
//            filterObj = pbDb.execSelectSQL(filterQuery);
//            for (int m = 0; m < filterObj.getRowCount(); m++) {
//                String memId2 = (filterObj.getFieldValueString(m, "MEMBER_ID"));
//                String prevClause = "";
//                String newClause = "";
//                String totalClause = "";
//                if (memberFilters.containsKey(memId2)) {
//                    prevClause = (String) memberFilters.get(memId2);
//                    newClause = filterObj.getFieldValueClobString(m, "MEMBER_VALUE");
//                    totalClause = prevClause + "," + newClause;
//                } else {
//                    memberFilters.put(filterObj.getFieldValueString(m, "MEMBER_ID"), filterObj.getFieldValueClobString(m, "MEMBER_VALUE"));
//                    filterMemIds.add(filterObj.getFieldValueString(m, "MEMBER_ID"));
//                }
//            }

//            String filterQueryRole = "select * from prg_role_member_filter where folder_id in (" + roleId + ")";
//              for (int m = 0; m < filterObj.getRowCount(); m++) {
//                String memId2 = (filterObj.getFieldValueString(m, "MEMBER_ID"));
//                String prevClause = "";
//                String newClause = "";
//                String totalClause = "";
//                if (memberFilters.containsKey(memId2)) {
//                    prevClause = (String) memberFilters.get(memId2);
//                    newClause = filterObj.getFieldValueClobString(m, "MEMBER_VALUE");
//                    totalClause = prevClause + "," + newClause;
//                } else {
//                    memberFilters.put(filterObj.getFieldValueString(m, "MEMBER_ID"), filterObj.getFieldValueClobString(m, "MEMBER_VALUE"));
//                    filterMemIds.add(filterObj.getFieldValueString(m, "MEMBER_ID"));
//                }
//            }
            StringBuilder allEleIds = new StringBuilder();
            String eleIds[] = (String[]) valuesMap.keySet().toArray(new String[0]);
            for (int m = 0; m < eleIds.length; m++) {
                if(eleIds[m].equalsIgnoreCase("Time")){
                    
                }else{
                     if(allEleIds.length()==0){
                         allEleIds.append(eleIds[m]);
                    }else{
                allEleIds.append(",").append(eleIds[m]);
            }
                }
            }
            String allEleIdsStr = allEleIds.substring(1);
            String memQ = "select * from prg_user_all_adim_details where info_element_id in (" + allEleIdsStr + ")";
            String tabQ = "select * from prg_user_all_info_details  where ELEMENT_ID in (" + allEleIdsStr + ")";
            PbReturnObject memObj = pbDb.execSelectSQL(memQ);
            PbReturnObject tabObj = pbDb.execSelectSQL(tabQ);

            String filterClause = "";

            for (int m = 0; m < memObj.getRowCount(); m++) {
                String memId = memObj.getFieldValueString(m, 6);
                if (filterMemIds.contains(memId)) {
                    membersEle.put(memObj.getFieldValueString(m, 1), memId);
                    mems.put(memId, memObj.getFieldValueString(m, 1));
                }
            }
            for (int m = 0; m < tabObj.getRowCount(); m++) {
                String eleId = tabObj.getFieldValueString(m, 15);
                ArrayList dets = new ArrayList();
                dets.add(0, tabObj.getFieldValueString(m, 16));
                dets.add(1, tabObj.getFieldValueString(m, 17));
                dets.add(2, tabObj.getFieldValueString(m, 27));
                dets.add(3, tabObj.getFieldValueString(m, 18));
                if (membersEle.containsKey(eleId)) {
                    bussTablIds.put(eleId, dets);
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

        //modified by susheela over 03-12-09
        String endValue = "";
        int exceptionFlag = 0;
        int rowStart = 0;
        if (startValue == null || startValue.equalsIgnoreCase("")) {
            startValue = "1";
            endValue = "20";
        } else {
            rowStart = Integer.parseInt(request.getParameter("startValue"));
//            startValue = "" + rowStart;
            startValue = Integer.toString(rowStart);
//            endValue = "" + (rowStart - 1 + 20);
            endValue = Integer.toString((rowStart - 1 + 20));
        }

        /*
         * hm.put("Region",getregion); hm.put("State",getstate);
         * hm.put("Location",getlocation); hm.put("Shade",getshade);
         * hm.put("ShadesCodes",getshadecode);//Dealer
         * hm.put("Dealer",getdealer);
         *
         * String getregion = request.getParameter("cboregion") ; String
         * getstate = request.getParameter("cbostate") ; String getlocation =
         * request.getParameter("cbolocation") ;
         *
         * //StartDate
         *
         * getcollection = request.getParameter("cbocollection"); //StartDate
         * getproduct = request.getParameter("cboproduct"); //StartDate getshade
         * = request.getParameter("cboshade"); getdealer =
         * request.getParameter("cbodealer"); //StartDate
         *
         * getshadecode = request.getParameter("cboshadecode");
         */
        ArrayList a = new ArrayList();
        ArrayList totalvalues = new ArrayList();
        DisplayParameters dispParam = new DisplayParameters();

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String parentName = "";
            PbTimeRanges pb = new PbTimeRanges();
            ConnectionMetadata conMetadata = null;
            ConnectionDAO connectionDAO = new ConnectionDAO();
            if (tabtype != null && tabtype.equalsIgnoreCase("graph") && request.getParameter("query").toString().equalsIgnoreCase("TIME")) {

                String elementid = (String) elementIds.get(0);
                if (elementIds.get(0).toString().equalsIgnoreCase("TIME")) {
                    if (elementIds.size() > 3) {
                        elementid = (String) elementIds.get(1);
                    }
                }
                con = ProgenConnection.getInstance().getConnectionForElement(elementid);
                conMetadata = connectionDAO.getConnectionForElement(elementid);
                st = con.createStatement();
            } else {
                con = ProgenConnection.getInstance().getConnectionForElement(request.getParameter("query"));

//            con = pb.getConnection(request.getParameter("query"));
                conMetadata = connectionDAO.getConnectionForElement(request.getParameter("query"));
                st = con.createStatement();
            }

            //added by susheela start 03-12-09
            String filterClause = "";
            String memberId = "";
            //bussTablIds
            String value = "";
            String eleId = request.getParameter("query");
            if (membersEle.containsKey(eleId)) {
                memberId = membersEle.get(eleId).toString();
            }
            ArrayList detVal = new ArrayList();
            String colName = "";
            String tabName = "";
            if (bussTablIds.containsKey(request.getParameter("query"))) {
                detVal = (ArrayList) bussTablIds.get(eleId);
                tabName = detVal.get(2).toString();
                colName = detVal.get(3).toString();
            }

            if (memberFilters.containsKey(memberId)) {
                value = (String) memberFilters.get(memberId);
            }
            if (value.length() > 0) {
                //modified by susheela start 11-12-09
                value = value.replace("'", "''");
                value = "'" + value + "'";
                value = value.replace(",", "','");
                value = value.replace("'||chr(38)||'", "||chr(38)||");
                filterClause = " and " + tabName + "." + colName + " in(" + value + ")";
            }
//              Start of code by Nazneen in March2014 for Dimension Segmentation
            String filesPath = "";
            HttpSession session = request.getSession(false);
            if (session != null) {
                PbReportViewerDAO rv = new PbReportViewerDAO();
                filesPath = rv.getFilePath(session);
                dispParam.filePath = filesPath;
            }
//         End of code by Nazneen in March2014 for Dimension Segmentation
//  time query
            String Query = dispParam.getParameterQuery(request.getParameter("query"));
            PbReportCollection prc = new PbReportCollection();
            HashMap det = prc.getParentElementId(request.getParameter("query"), reportID);
            ArrayList parentId = (ArrayList) det.get("ParentList");
            ArrayList aggElementList = new ArrayList();
            aggElementList.add(request.getParameter("query"));
            ArrayList parentBussTab = (ArrayList) det.get("ParentBussTables");
            HashMap ParentBussCol = (HashMap) det.get("ParentBussCol");
            HashMap lMap = new HashMap();
            ArrayList lBusCols = new ArrayList();
            String ssoStatus = String.valueOf(session.getAttribute("status"));
//            ssoStatus = "OK";
if (tabtype != null && tabtype.equalsIgnoreCase("graph") && ssoStatus!=null && ssoStatus.equalsIgnoreCase("OK") ) {
for (int y = 0; y < elementIds.size(); y++) {
                        String parId = elementIds.get(y).toString();
                         if (valuesMap.containsKey(parId)) {

                              List paramValues = (List) valuesMap.get(parId);
                               if (!paramValues.contains("All") && !parId.equalsIgnoreCase(eleId)) {
                                   aggElementList.add(parId);
                                lMap.put(parId, paramValues);
                                lBusCols.add(parId);
                                 if (!parentId.contains(parId)) {
                                   parentId.add(parId);
                                 }
                         }
                         }
                    }
        }else{
            for (int p = 0; p < parentId.size(); p++) {
                String parId = parentId.get(p).toString();
                if (valuesMap.containsKey(parId)) {

//                    String paramValues = (String) valuesMap.get(parId);
                    List paramValues = (List) valuesMap.get(parId);
                    if (!paramValues.contains("All")) {
                        lMap.put(parId, paramValues);
                        if (ParentBussCol.containsKey(parId)) {
                            String pBussCol = (String) ParentBussCol.get(parId);
                            lBusCols.add(pBussCol);
                        }
                    } else if (membersEle.containsKey(parId)) {
                        String pBussCol = (String) ParentBussCol.get(parId);
                        lBusCols.add(pBussCol);
                        String newSecuredVal = "";
                        String mId = "";
                        mId = membersEle.get(parId).toString();

                        newSecuredVal = memberFilters.get(mId).toString();
                        lMap.put(parId, newSecuredVal);
                    }
                }
            }
        }
            //added by Nazneen paramSecurity clause
            String paramSecurityClause = "";

            String crossDimSecurityClause = "";
            if (tabtype != null && tabtype.equalsIgnoreCase("graph") && request.getParameter("query").toString().equalsIgnoreCase("TIME")) {
            } else {
                paramSecurityClause = dispParam.getParamFilter(userId, reportID, eleId);

                crossDimSecurityClause = dispParam.getCrossDimFilter(userId, reportID, eleId, valuesMap);
            }
            ReportObjectQuery objectQuery = new ReportObjectQuery();
            String filtercluase = objectQuery.getDimSecClause(elementIds, userId,new HashMap<String, List>());
            if (tabtype != null && tabtype.equalsIgnoreCase("graph")) {
                String fileLocation = "";
                PbReportViewerDAO dao = new PbReportViewerDAO();
//     HttpSession session = request.getSession(false);
                if (session != null) {
                    fileLocation = dao.getFilePath(session);
                } else {
                    fileLocation = "/usr/local/cache";
                }

//String wherecluase="";
                StringBuilder wherecluase = new StringBuilder();
    String orderSeq = "asc";
            if(piVersion =="piVersion2014"){

                } else {
                if(orderVal.contains("Desc")){
                    orderSeq = "desc";
                }
            }
if(lBusCols.size()>0){
 String searchString=null;
      String[] searchWord=null;

                
     int flag=0;
                    for (int p = 0; p < parentId.size(); p++) {
                        String parId = parentId.get(p).toString();
                        if (valuesMap.containsKey(parId)) {
                            StringBuilder result = new StringBuilder();
                            List<String> list = (List<String>) lMap.get(parId);
                            if (list != null) {
                                if (list.size() > 0) {
                                    int flag1 = 0;
                                    for (String str : list) {
                                        if (list.size() > 1) {
                                            if (flag1 == 0) {
                                                result.append("'").append(str).append("'");
                                            } else {
                                                result.append(",'").append(str).append("'");
                                            }
                                        } else {
                                            result.append("'").append(str).append("'");
                                        }
                                        flag1++;
                                    }

                                }
                                if (flag == 0) {
                                    wherecluase.append("A_").append(parId).append(" in (").append(result).append(")");
                                } else {
                                    wherecluase.append(" and A_").append(parId).append(" in (").append(result).append(")");
                                }
                                flag++;
                            }

                        }
                    }
// if(wherecluase.length()!=null && wherecluase.toString()!=""){
                    if (wherecluase != null && wherecluase.length() > 0) {
                        wherecluase.append(" " + filtercluase + "");
                    } else {
                        wherecluase.append(" 1 = 1 " + filtercluase + "");
                    }

                    String qrytotal = "";
                    String elmntid = "A_" + eleId;
                    filterListGraph = new ArrayList<String>();
                    filterListGraph.add(elmntid);
                    ao_Name = pbAoQuery.aoReplace(ao_Name, aggElementList);
         String qry="";
                    if (wherecluase != null && wherecluase.length() > 0) {
 qry="select  distinct("+elmntid+") A1 from  "+ao_Name+" where  "+wherecluase+" ";
         }else{
           qry="select  distinct("+elmntid+") A1  from "+ao_Name+" ";  
                    }
   String tempSWord = request.getParameter("q");
                tempSWord = tempSWord.replace("%20", " ").replace("%26", "&").replace("'''", ",").replace("%3A", ":").replace("%2F", "/").replace("%3F", "?").replace("%40", "@");
                tempSWord = tempSWord.replace("%23", "#").replace("%24", "$").replace("%25", "%").replace("%5E", "^").replace("%60", "`").replace("%3C", "<").replace("%3E", ">").replace("%2C", ",");
String qry1="";
searchWord = tempSWord.split(",");
                searchString = searchWord[searchWord.length - 1];   
      if(conMetadata.getDbType().equalsIgnoreCase("Mysql")){
   qry1="select /*+ CURSOR_SHARING_EXACT RESULT_CACHE */ A1 from (select A1 from (" +qry+ ") O1 ";
            }else{
          qry1="select /*+ CURSOR_SHARING_EXACT RESULT_CACHE */ A1 from (select RANK() over(order by A1) AS num1,A1 from (" +qry+ ") O1 ";
    }
                if (searchString != null && !(searchString.equalsIgnoreCase("@"))) {
                 qry1+=" WHERE upper(A1) LIKE upper('" + searchString + "%') or upper(A1) LIKE upper('%" + searchString + "%') or upper(A1) LIKE upper('%" + searchString + "') ";
            }
                   if(conMetadata.getDbType().equalsIgnoreCase("Mysql")){
             if(startValue.equalsIgnoreCase("1"))
                    startValue ="0";
            qry1+=") O2 order by 1 " +orderSeq+ " Limit " + startValue + " ," + endValue + "";
             }
             else
              qry1+=") O2 where num1 between " + startValue + " and " + endValue + " order by 1 " +orderSeq+" ";
 rs = st.executeQuery(qry1);
                    while (rs.next()) {
                        //Added by Ram 29Nov2015 for Language Lookup
                        if (lookup.containsKey(rs.getString(1))) {
                            a.add(lookup.get(rs.getString(1)));
                            totalvalues.add(lookup.get(rs.getString(1)));
                        } else {
// endded by ram
                            a.add(rs.getString(1));
                            totalvalues.add(rs.getString(1));
                        }
                    }

                    rs.close();
                    rs = null;
                    st.close();
                    st = null;
                    con.close();
                    con = null;
                } else {
                    tabtype = "dependent";
                    String searchString = null;
                    String[] searchWord = null;
                    String elmntid = "";
                    if (eleId.equalsIgnoreCase("TIME")) {
                        elmntid = eleId;
                    } else {
                        elmntid = "A_" + eleId;
                    }
                    orderSeq = "asc";
                    if (piVersion == "piVersion2014") {
                    } else {
                        if (orderVal.contains("Desc")) {
                            orderSeq = "desc";
                        }
                    }
                    filterListGraph = new ArrayList<String>();
                    filterListGraph.add(elmntid);
                    ao_Name = pbAoQuery.aoReplace(ao_Name, filterListGraph);
                    String qry = "";
                    if (filtercluase != null && filtercluase != "") {
                        qry = "select  distinct(" + elmntid + ") A1 from " + ao_Name + " where 1=1 " + filtercluase + " ";
                    } else {
                        qry = "select distinct(" + elmntid + ") A1 from " + ao_Name + " ";
                    }
                    String tempSWord = request.getParameter("q");
                    tempSWord = tempSWord.replace("%20", " ").replace("%26", "&").replace("'''", ",").replace("%3A", ":").replace("%2F", "/").replace("%3F", "?").replace("%40", "@");
                    tempSWord = tempSWord.replace("%23", "#").replace("%24", "$").replace("%25", "%").replace("%5E", "^").replace("%60", "`").replace("%3C", "<").replace("%3E", ">").replace("%2C", ",");
                    String qry1 = "";
                    String qrytotal = "";
                    searchWord = tempSWord.split(",");
                    searchString = searchWord[searchWord.length - 1];
                    if (conMetadata.getDbType().equalsIgnoreCase("Mysql")) {
                        qry1 = "select /*+ CURSOR_SHARING_EXACT RESULT_CACHE */ A1 from (select A1 from (" + qry + ") O1 ";
                        qrytotal = "select /*+ CURSOR_SHARING_EXACT RESULT_CACHE */ A1 from (select A1 from (" + qry + ") O1 ";
                    } else {
                        qry1 = "select /*+ CURSOR_SHARING_EXACT RESULT_CACHE */ A1 from (select RANK() over(order by A1) AS num1,A1 from (" + qry + ") O1 ";
                        qrytotal = "select /*+ CURSOR_SHARING_EXACT RESULT_CACHE */ A1 from (select RANK() over(order by A1) AS num1,A1 from (" + qry + ") O1 ";
                    }
                    if (searchString != null && !(searchString.equalsIgnoreCase("@"))) {
                        qry1 += " WHERE upper(A1) LIKE upper('" + searchString + "%') or upper(A1) LIKE upper('%" + searchString + "%') or upper(A1) LIKE upper('%" + searchString + "') ";
                        qrytotal += " WHERE upper(A1) LIKE upper('" + searchString + "%') or upper(A1) LIKE upper('%" + searchString + "%') or upper(A1) LIKE upper('%" + searchString + "') ";
                    }

                    if (conMetadata.getDbType().equalsIgnoreCase("Mysql")) {
                        if (startValue.equalsIgnoreCase("1")) {
                            startValue = "0";
                        }
//            changedQuery.append(") O2 order by 1  Limit " + startValue + " ," + endValue + "");
                        qry1 += ") O2 order by 1 " + orderSeq + " Limit " + startValue + " ," + endValue + "";
                        qrytotal += ") O2 order by 1 " + orderSeq + "";
                    } else //               changedQuery.append(") O2 where num1 between " + startValue + " and " + endValue + " order by 1");
                    {
                        qry1 += ") O2 where num1 between " + startValue + " and " + endValue + " order by 1 " + orderSeq + " ";
                    }
                    qrytotal += ") O2 order by 1 " + orderSeq + " ";

                  PbReturnObject roleObj=null;
              roleObj =   (PbReturnObject) dao1.getReturnObjectBasedOnLov(reportID,elmntid,qry1,fileLocation,userId);
              if(roleObj==null){
                 System.out.println(".....................................query Fired  ......................."+qry1);
//                 rs = st.executeQuery(qry1);
                if(eleId!=null && eleId.equalsIgnoreCase("time")){
                 eleId = (String) elementIds.get(0);
                if (elementIds.get(0).toString().equalsIgnoreCase("TIME")) {
                    if (elementIds.size() > 3) {
                        eleId = (String) elementIds.get(1);
                    }
                }
                }
                   con=ProgenConnection.getInstance().getConnectionForElement(eleId);
                roleObj = pbDb.execSelectSQL(qry1,con);
//                System.out.println("ddd"+roleObj.getFieldValueString(0, 0));
//               roleObj.prepareObject(rs);
//                   roleObj =  st.executeQuery(qry1);
              dao1.setReturnObjectBasedOnLov(reportID,elmntid,qry1,roleObj,fileLocation,userId);
              }
 for (int m = 0; m < roleObj.rowCount; m++)  {
                a.add(roleObj.getFieldValueString(m,0));
            }
//st = con.createStatement();
           //  totalrs = st.executeQuery(qrytotal);
 for (int m = 0; m < roleObj.rowCount; m++)  {
//      System.out.println(".....................................Retobj  ......................."+roleObj.getFieldValueString(m,0));
   if(lookup.containsKey(roleObj.getFieldValueString(m,0)))
                    totalvalues.add(lookup.get(roleObj.getFieldValueString(m,0)));
    else
                    totalvalues.add(roleObj.getFieldValueString(m,0));
 }
         

 //Added by Ram 29Nov2015 for Language Lookup
            for(int k=0;k<a.size();k++){
                                     if(lookup.containsKey(a.get(k)))
                                           a.set(k, lookup.get(a.get(k)));
            }
// endded by ram

//            rs.close();
//            rs = null;
//            st.close();
//            st = null;
//            con.close();
//            con = null;
                    String s[] = new String[a.size()];
                    a.toArray(s);
//                     String value1 = "";
                    StringBuilder value1 = new StringBuilder(100);
                    for (int i = 0; i < s.length; i++) {
//                        value1 = value1 + s[i] + "\n";
                        value1.append(s[i]).append("\n");
                    }
                    reportData = value1.toString();
//  reportData = adapter.getAjaxFilters(reportID, fileLocation, request,startValue);
                }
            } else {
                String mainCaluse = "";
                mainCaluse = dispParam.getParameterQuery(request.getParameter("query"));
                String whereClause = prc.getWhereClause(lMap, lBusCols);
                String fullQuery = "";
                // fullQuery = mainCaluse+whereClause;

//   if(filtercluase!=null && filtercluase!=""){
//        whereClause+=" "+filtercluase+"";
//    }else{
//        whereClause+=" 1 = 1 "+filtercluase+"";
//    }
                //modified by susheela start 03-12-09
                fullQuery = mainCaluse + whereClause + filterClause + crossDimSecurityClause + paramSecurityClause;

                //modified by susheela over 03-12-
                BusinessGroupDAO bgDao = new BusinessGroupDAO();
                String q = bgDao.viewBussDataWithouCol(parentBussTab);
                //added by Nazneen to Decode the String
                String[] searchWord = null;
                String searchString = null;
//            if(piVersion =="piVersion2014"){
//
//            } else {
                String tempSWord = request.getParameter("q");
                tempSWord = tempSWord.replace("%20", " ").replace("%26", "&").replace("'''", ",").replace("%3A", ":").replace("%2F", "/").replace("%3F", "?").replace("%40", "@");
                tempSWord = tempSWord.replace("%23", "#").replace("%24", "$").replace("%25", "%").replace("%5E", "^").replace("%60", "`").replace("%3C", "<").replace("%3E", ">").replace("%2C", ",");

                searchWord = tempSWord.split(",");
                searchString = searchWord[searchWord.length - 1];
//            }
                StringBuffer changedQuery = new StringBuffer();
                StringBuffer totalchangedQuery = new StringBuffer();
                if (conMetadata.getDbType().equalsIgnoreCase("Mysql")) {
                    changedQuery.append("select A1,A2 from (select A1,A2 from ( ");
                    totalchangedQuery.append("select A1,A2 from (select A1,A2 from ( ");
                } else {
                    changedQuery.append("select A1,A2 from (select RANK() over(order by A1,A2) AS num1,A1,A2 from ( ");
                    totalchangedQuery.append("select A1,A2 from (select RANK() over(order by A1,A2) AS num1,A1,A2 from ( ");
                }
                changedQuery.append(fullQuery);
                totalchangedQuery.append(fullQuery);
                changedQuery.append(" ) O1 ");
                totalchangedQuery.append(" ) O1 ");
                if (searchString != null && !(searchString.equalsIgnoreCase("@"))) {
                    changedQuery.append(" WHERE upper(A1) LIKE upper('" + searchString + "%') or upper(A1) LIKE upper('%" + searchString + "%') or upper(A1) LIKE upper('%" + searchString + "')");
                    totalchangedQuery.append(" WHERE upper(A1) LIKE upper('" + searchString + "%') or upper(A1) LIKE upper('%" + searchString + "%') or upper(A1) LIKE upper('%" + searchString + "')");
                }
                //added by nazneen for ordering
                String orderSeq = "asc";
                if (piVersion == "piVersion2014") {
                } else {
                    if (orderVal.contains("Desc")) {
                        orderSeq = "desc";
                    }
                }

                if (conMetadata.getDbType().equalsIgnoreCase("Mysql")) {
                    if (startValue.equalsIgnoreCase("1")) {
                        startValue = "0";
                    }
//            changedQuery.append(") O2 order by 1  Limit " + startValue + " ," + endValue + "");

                    changedQuery.append(") O2 order by 1 " + orderSeq + " Limit " + startValue + " ," + endValue + "");
                    totalchangedQuery.append(") O2 order by 1 " + orderSeq);
                } else {
//               changedQuery.append(") O2 where num1 between " + startValue + " and " + endValue + " order by 1");
                    changedQuery.append(") O2 where num1 between " + startValue + " and " + endValue + " order by 1 " + orderSeq);
                    totalchangedQuery.append(") O2 order by 1 " + orderSeq);
                    //
                }
                rs = st.executeQuery(changedQuery.toString());

                if (startValue.equalsIgnoreCase("1") || startValue.equalsIgnoreCase("0")) {
                    if (fromglobal != null && fromglobal.equalsIgnoreCase("true")) {
                    } else {
                        a.add("All");
                        totalvalues.add("All");
                    }
                }

                while (rs.next()) {
                    a.add(rs.getString(1));
                    totalvalues.add(rs.getString(1));
                }
                st = con.createStatement();
                ///totalrs = st.executeQuery(totalchangedQuery.toString());
//            while (totalrs.next()) {
//                totalvalues.add(totalrs.getString(1));
//            }
//Added by Ram 29Nov2015 for Language Lookup
                for (int k = 0; k < a.size(); k++) {
                    if (lookup.containsKey(a.get(k))) {
                        a.set(k, lookup.get(a.get(k)));
                    }
                }
// endded by ram 

                rs.close();
                rs = null;
                st.close();
                st = null;
                con.close();
                con = null;
            }

        } catch (Exception ex) {

            logger.error("Exception:", ex);
            exceptionFlag = 1;

        } finally {
            if (tabtype != null && tabtype.equalsIgnoreCase("dependent")) {
                HashMap map = new HashMap();
                String s1[] = new String[totalvalues.size()];
                totalvalues.toArray(s1);
//                    String totalvalues1 = "";
                StringBuilder totalvalues1 = new StringBuilder();
                for (int i = 0; i < s1.length; i++) {
                    totalvalues1.append(s1[i]).append("\n");

                }
                map.put("data", reportData);
                map.put("totalvalues", totalvalues1.toString());
                map.put("dependtype", "dependent");
                String report = gson.toJson(map);
                out.print(report);
                out.close();
            } else {
                if (exceptionFlag == 0) {

                    if (piVersion == "piVersion2014") {
                        String s[] = new String[a.size()];
                        a.toArray(s);
//                    String value = "";
                        StringBuilder value = new StringBuilder();
                        for (int i = 0; i < s.length; i++) {
                            value.append(s[i]).append("\n");

                        }
                        String s1[] = new String[totalvalues.size()];
                        totalvalues.toArray(s1);
//                    String totalvalues1 = "";
                        StringBuilder totalvalues1 = new StringBuilder();
                        for (int i = 0; i < s1.length; i++) {
                            totalvalues1.append(s1[i]).append("\n");

                        }
                        if (fromglobal != null && fromglobal.equalsIgnoreCase("true")) {
                            HashMap map = new HashMap();
                            map.put("data", value.toString());
                            map.put("totalvalues", totalvalues1.toString());
                            map.put("dependtype", "undependent");
                            String report = gson.toJson(map);
                            out.print(report);
                            out.close();
                        } else {

                            out.print(value);
                            out.close();
                        }
                    } else {
                        String name = request.getParameter("q");
                        if (name.equals("@") != true) {

                            String s[] = new String[a.size()];
                            a.toArray(s);

                            //Comparison logic
                            name = name.substring(name.lastIndexOf(",") + 1);

//                    String value = ""
                            StringBuilder value = new StringBuilder();
                            if (name.equals("@") != true) {
                                for (int i = 0; i < s.length; i++) {
                                    if (name.isEmpty()) {
                                        break;
                                    }
                                    if ((s[i].toUpperCase()).startsWith((name.toUpperCase()))) {
                                        value.append(s[i]).append("\n");

                                    }

                                }

                            } else {
                                String s12[] = new String[a.size()];
                                a.toArray(s12);

                                for (int i = 0; i < s.length; i++) {
                                    value.append(s[i]).append("\n");

                                }
                            }
                            String s1[] = new String[totalvalues.size()];
                            totalvalues.toArray(s1);
//                    String totalvalues1 = "";
                            StringBuilder totalvalues1 = new StringBuilder();
                            for (int i = 0; i < s1.length; i++) {
                                totalvalues1.append(s1[i]).append("\n");

                            }
                            if (fromglobal != null && fromglobal.equalsIgnoreCase("true")) {
                                HashMap map = new HashMap();
                                map.put("data", value.toString());
                                map.put("totalvalues", totalvalues1.toString());
                                map.put("dependtype", "undependent");
                                String report = gson.toJson(map);
                                out.print(report);
                                out.close();
                            } else {
                                out.print(value);
                                out.close();
                            }

                        } else {
                            String s[] = new String[a.size()];
                            a.toArray(s);
                            StringBuilder value = new StringBuilder();
                            for (int i = 0; i < s.length; i++) {
                                value.append(s[i]).append("\n");

                            }
                            String s1[] = new String[totalvalues.size()];
                            totalvalues.toArray(s1);
//                    String totalvalues1 = "";
                            StringBuilder totalvalues1 = new StringBuilder();
                            for (int i = 0; i < s1.length; i++) {
                                totalvalues1.append(s1[i]).append("\n");

                            }
                            if (fromglobal != null && fromglobal.equalsIgnoreCase("true")) {
                                HashMap map = new HashMap();
                                map.put("data", value.toString());
                                map.put("totalvalues", totalvalues1.toString());
                                map.put("dependtype", "undependent");
                                String report = gson.toJson(map);
                                out.print(report);
                                out.close();
                            } else {

                                out.print(value);
                                out.close();
                            }
                        }
                    }

                } else {
                    out.print("Search Exception");
                    out.close();
                }
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (st != null) {
                        st.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } catch (Exception e) {
                    logger.error("Exception:", e);
                }
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>
}
