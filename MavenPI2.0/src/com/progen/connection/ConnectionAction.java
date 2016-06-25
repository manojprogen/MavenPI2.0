/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//
/**
 *
 * @author Administrator
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.connection;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import prg.db.PbReturnObject;

/**
 *
 * @author Saurabh
 */
public class ConnectionAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(ConnectionAction.class);
    /*
     * forward name="success" path=""
     */
    private final static String SUCCESS = "success";
    ConnectionDAO connectionDAO = new ConnectionDAO();

    /**
     * Provides the mapping from resource key to method name.
     *
     * @return Resource key / method name map.
     */
    public ActionForward updateUserConnection(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        // TODO: implement add method
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("IN ACTION CLASS");
        HttpSession session = request.getSession(false);

        String CONNECTION_NAME = request.getParameter("conName");
        String USER_NAME = request.getParameter("usrename");
        String PASSWORD = request.getParameter("password");
        String SERVER_NAME = request.getParameter("servername");
        String SERVICE_ID = request.getParameter("serid");
        String SERVICE_NAME = request.getParameter("servnane");
        String PORT = request.getParameter("port");
        String DSN_NAME = request.getParameter("DSNname");
        String CONNECTION_TYPE = request.getParameter("conntype");
        String DBNAME = request.getParameter("DBname");
        String connectionId = request.getParameter("connectionId");

        // PbDb pbdb = new PbDb();
        //String seqnumQuery = "SELECT max(PORTAL_TAB_ORDER) FROM PRG_PORTAL_TAB_MASTER where portal_id=" + PORTAL_ID;
        //PbReturnObject pbroseq = pbdb.execSelectSQL(seqnumQuery);
        ConnectionDAO ConnectionDAO = new ConnectionDAO();
        String[] userList = new String[12];
        userList[0] = CONNECTION_NAME;
        userList[1] = USER_NAME;
        userList[2] = PASSWORD;
        userList[3] = SERVER_NAME;
        userList[4] = SERVICE_ID;
        userList[5] = SERVICE_NAME;
        userList[6] = PORT;
        userList[7] = DSN_NAME;
        userList[8] = CONNECTION_TYPE;
        userList[9] = DBNAME;
        userList[10] = connectionId;


        boolean result = ConnectionDAO.upDateUserConn(userList);

        return null;
    }

    public ActionForward addTableAsQuery(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        PbReturnObject tabDetails = new PbReturnObject();
        ResultSetMetaData DbMetadata = null;
        String src = null;
        HttpSession session = request.getSession(false);
        ConnectionDAO ConnectionDAO = new ConnectionDAO();
        PrintWriter out = response.getWriter();

        String connectionId = request.getParameter("connectionId");
        String tableName = request.getParameter("tableName");
        String query = request.getParameter("query");


        HashMap details = ConnectionDAO.checkTableName(connectionId, tableName, query);//isCorrectQuery

        String isCorrectQuery = (String) details.get("isCorrectQuery");
        String IsTable = (String) details.get("IsTable");
        tabDetails = (PbReturnObject) details.get("TabDetails");

        if (IsTable.equalsIgnoreCase("true")) {
            request.setAttribute("IsTable", IsTable);
        } else {
            request.setAttribute("IsTable", IsTable);
        }


        if (IsTable.equalsIgnoreCase("true")) {
            out.println("<h3 align=\"center\" > <font color=\"red\"> Table Name already Exists. </font> </h3>");
            out.println("<center><input type= 'button' value= 'Back' onclick= 'window.history.go(-1);'></center>");
            return null;

        } else if (isCorrectQuery.equalsIgnoreCase("false")) {

            out.println("<h3 align=\"center\" > <font color=\"red\">  Wrong Query. </font> </h3>");
            out.println("<center><input type= 'button' value= 'Back' onclick= 'window.history.go(-1);'></center>");
            return null;
        } else {
            session.setAttribute("connectionId", connectionId);
            session.setAttribute("tableName", tableName);
            session.setAttribute("query", query);
            session.setAttribute("tabDetails", tabDetails);

            return mapping.findForward("dispDtls");
        }


    }

    public ActionForward saveDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        // TODO: implement add method

        HttpSession session = request.getSession(false);
        PbReturnObject tabDetails = null;
        String connectionId = null;
        String tableName = null;
        String query = null;
        String cols[] = null;
        String colTypes[] = null;
        int colsSizes[] = null;

        ConnectionDAO ConnectionDAO = new ConnectionDAO();
        if (session != null) {
            //String connectionId = String.valueOf(request.getAttribute("connectionId"));
            //String tableName = String.valueOf(request.getAttribute("tableName"));
            //String query = String.valueOf(request.getAttribute("query"));

            connectionId = String.valueOf(session.getAttribute("connectionId"));
            tableName = String.valueOf(session.getAttribute("tableName"));
            query = String.valueOf(session.getAttribute("query"));
            tabDetails = (PbReturnObject) session.getAttribute("tabDetails");


            ////////////////////////////////////////////////////////////////////////////////////////.println.println("connectionId is " + connectionId);
            ////////////////////////////////////////////////////////////////////////////////////////.println.println("tableName is " + tableName);
            ////////////////////////////////////////////////////////////////////////////////////////.println.println("query is " + query);
            ConnectionDAO.saveQueryTable(connectionId, tableName, query, tabDetails);
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }

    }

    //added by susheela start
    public ActionForward deleteUserConnection(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String connectionId = request.getParameter("connectionId");
        PrintWriter out = response.getWriter();
        //////////////////////////////////////////////////////////////////////////////////////.println.println(" in connectionId- "+connectionId);
        ConnectionDAO ConnectionDAO = new ConnectionDAO();
        boolean isGroup = ConnectionDAO.checkForUserConnecionOnDelete(connectionId);
        //////////////////////////////////////////////////////////////////////////////////////.println.println(" isGroup "+isGroup);
        if (isGroup == true) {
            out.println("1");
        } else if (isGroup == false) {
            //////////////////////////////////////////////////////////////////////////////////////.println.println(" in delete Db.");
            ConnectionDAO.deleteConnection(connectionId);
            out.println("2");
        }
        return null;
    }

    public ActionForward getRelatedDbTables(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String tableId = request.getParameter("tableId");
        PrintWriter out = response.getWriter();
        //////////////////////////////////////////////////////////////////////////////////////.println.println(" in db tableId- "+tableId);
        ConnectionDAO ConnectionDAO = new ConnectionDAO();

        return null;
    }

    public ActionForward getRelatedDbTables1(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String tableId = request.getParameter("tableId");
        PrintWriter out = response.getWriter();
        //////////////////////////////////////////////////////////////////////////////////////.println.println(" in db tableId- "+tableId);
        ConnectionDAO ConnectionDAO = new ConnectionDAO();

        return null;
    }

    public ActionForward editRelatedDbTables(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String dbTableId = request.getParameter("dbTableId");
        String values = request.getParameter("values");
        String allTabValues = request.getParameter("allTabValues");
        String connectionId = request.getParameter("connectionId");
        PrintWriter out = response.getWriter();
        ////////////////////////////////////////////////////////////////////////////////.println.println(dbTableId+"--action in allTabValues- "+allTabValues);
        ////////////////////////////////////////////////////////////////////////////////.println.println(connectionId+" connectionId "+values);
        ConnectionDAO ConnectionDAO = new ConnectionDAO();
        if (allTabValues.length() > 0) {
            allTabValues = allTabValues.substring(1);
        }
        if (values.length() > 0) {
            values = values.substring(1);
        }
        boolean status = ConnectionDAO.editRelatedDbTables(connectionId, dbTableId, values, allTabValues);
        ////////////////////////////////////////////////////////////////////////////////.println.println(" status "+status);
        out.println("1");
        return null;
    }

    //added by susheela over
    public ActionForward savesqlConnection(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PrintWriter out = response.getWriter();
        String sqldbname = request.getParameter("sqldbname");
        String userName = request.getParameter("sqlun");
        String password = request.getParameter("sqlpswd");
        String sqlport = request.getParameter("sqlport");
        String hostName = request.getParameter("sqlhstname");
        String connectionName = request.getParameter("conname");
        String check = "";
        ArrayList arrayList = new ArrayList();
        arrayList.add(sqldbname);
        arrayList.add(userName);
        arrayList.add(password);
        arrayList.add(sqlport);
        arrayList.add(hostName);
        arrayList.add(connectionName);
        ////.println("arrayList\t"+arrayList);
        ConnectionDAO ConnectionDAO = new ConnectionDAO();
        check = ConnectionDAO.savesqlConnection(arrayList);
        out.print(check);
        return null;
    }

    public ActionForward addColumnToTable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            String tableId = request.getParameter("tableId");
            String tableName = request.getParameter("tableName");

            String connType = "";
            String dataString = null;
//            
//            
            ConnectionDAO ConnectionDAO = new ConnectionDAO();
            dataString = ConnectionDAO.addColumnToTable(tableId, tableName);

            out.print(dataString);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        } catch (Exception e) {
            logger.error("Exception:", e);
        } finally {
            out.close();
        }
        return null;
    }

    public ActionForward insertInDbMasterDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        boolean result = false;
        HttpSession httpSession = request.getSession(false);
        PbReturnObject bussgrpObj = new PbReturnObject();
        PbReturnObject busSrcDetails = new PbReturnObject();
        HashMap columnDetails = new HashMap();
        try {
            out = response.getWriter();
            String tableId = request.getParameter("tableId");
            String colleName = request.getParameter("colleName");
            String colsTypes = request.getParameter("colsTypes");
            String colsLengths = request.getParameter("colsLengths");
            String tableName = request.getParameter("tableName");
            String colleNameArray[] = colleName.split(",");
            String colsTypesArray[] = colsTypes.split(",");
            String colsLengthsArray[] = colsLengths.split(",");
            FactColumn factColumn = new FactColumn();
            factColumn.setDbTableid(Integer.parseInt(tableId));
            factColumn.setColsNames(colleNameArray);
            factColumn.setColsTypes(colsTypesArray);
            FactColumnHelper factColumnHelper = new FactColumnHelper();
            result = factColumnHelper.insertColumnToDbTable(Integer.parseInt(tableId), colleNameArray, colsTypesArray, colsLengthsArray);
            if (result) {

                bussgrpObj = factColumnHelper.checkInBussGrp(Integer.parseInt(tableId));
                if (bussgrpObj.getRowCount() > 0) {
                    factColumn.setBussTableId(bussgrpObj.getFieldValueInt(0, "BUSS_TABLE_ID"));
                    factColumn.setBussTableName(bussgrpObj.getFieldValueString(0, "BUSS_TABLE_NAME"));
                    busSrcDetails = factColumnHelper.checkInBussSrc(bussgrpObj.getFieldValueInt(0, "BUSS_TABLE_ID"));
                } else {
                    factColumn.setIsavailableBussGrp(false);
                }
                if (busSrcDetails.getRowCount() > 0) {
                    factColumn.setBussGrpSrcId(busSrcDetails.getFieldValueInt(0, "BUSS_SOURCE_ID"));
                    factColumn.setIsavailableBussGrp(true);
                }
            }
            columnDetails = factColumnHelper.getColumnDetails(colleNameArray, tableId);
            factColumn.setColsDetails(columnDetails);
            httpSession.setAttribute("factColumn", factColumn);
            String grpString = factColumnHelper.getGrpDetails(bussgrpObj.getFieldValueInt(0, "BUSS_TABLE_ID"));
            out.print(grpString);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        } finally {
            out.close();
        }
        return null;
    }

    public ActionForward checkAndSaveInBussSrc(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        boolean check = false;
        PbReturnObject pbRoleObj = new PbReturnObject();
        HashMap pbBussColumnHM = new HashMap();
        HashMap subFolderAndTabHM = new HashMap();
        ArrayList subFolderList = new ArrayList();
        try {
            out = response.getWriter();
            HttpSession httpSession = request.getSession(false);
            FactColumn factColumn = (FactColumn) httpSession.getAttribute("factColumn");
            check = factColumn.getIsIsavailableBussGrp();
            FactColumnHelper factColumnHelper = new FactColumnHelper();
            if (check) {
                check = factColumnHelper.insertBussGrpSrc(factColumn);
                pbRoleObj = factColumnHelper.checkInBussRole(factColumn.getBussTableId());
                if (pbRoleObj.getRowCount() > 0) {
                    for (int i = 0; i < pbRoleObj.getRowCount(); i++) {
                        subFolderList.add(pbRoleObj.getFieldValueString(i, "SUB_FOLDER_ID"));
                        subFolderAndTabHM.put(pbRoleObj.getFieldValueString(i, "SUB_FOLDER_ID"), pbRoleObj.getFieldValueString(i, "SUB_FOLDER_TAB_ID"));
                    }
                    factColumn.setSubFolderIds(subFolderList);
                    factColumn.setIsavailableRole(true);
                    factColumn.setSubFolderDetails(subFolderAndTabHM);
                } else {
                    factColumn.setSubFolderIds(subFolderList);
                    factColumn.setIsavailableRole(false);
                    factColumn.setSubFolderDetails(subFolderAndTabHM);
                }
//                factColumnHelper.getBussGrpDetails(factColumn);
                String outString = factColumnHelper.getFolderDetails(factColumn);
                out.print(outString);
            } else {
                out.print(false);
            }

        } catch (IOException ex) {
            logger.error("Exception:", ex);
        } finally {
            out.close();
        }
        return null;
    }

    public ActionForward checkAndSaveInBussRole(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        HttpSession httpSession = request.getSession(false);
        Boolean check = false;
        String rolenames = request.getParameter("rolenames");
        String rolenamesArray[] = rolenames.split(",");
        try {
            out = response.getWriter();
            FactColumn factColumn = (FactColumn) httpSession.getAttribute("factColumn");
            FactColumnHelper factColumnHelper = new FactColumnHelper();
            check = factColumn.getIsIsavailableRole();
            if (check) {
                check = factColumnHelper.insertInBusRole(factColumn);
                String outString = factColumnHelper.getFolderDetails(factColumn);
                out.print(outString);
            } else {
                out.print(false);
            }

        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }

        return null;
    }
    //by sunita

    public ActionForward getRelatedInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String grpId = request.getParameter("groupId");
        String tabId = request.getParameter("dbTableId");

        ConnectionDAO ConnectionDAO = new ConnectionDAO();
        String tableName1 = ConnectionDAO.getRelatedInfo(grpId, tabId);
        response.getWriter().print(tableName1);

        return null;
    }

    public ActionForward updateBussTable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String actualClause = request.getParameter("actualClause");
        String bussrltId = request.getParameter("bussrltId");
        ConnectionDAO ConnectionDAO = new ConnectionDAO();
        ConnectionDAO.updateBussTable(bussrltId, actualClause);
        return null;
    }

    public ActionForward InsertBussTable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String grpid = request.getParameter("grpid");
        String rltId = request.getParameter("rltId");
        ConnectionDAO ConnectionDAO = new ConnectionDAO();
        ConnectionDAO.InsertBussTable(rltId, grpid);
        return null;
    }

    public ActionForward saveAndPublishRole(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        HttpSession httpSession = request.getSession(false);
        Boolean check = false;
        String rolenames = request.getParameter("roleNames");
        String rolenamesArray[] = rolenames.split(",");
        try {
            out = response.getWriter();
            FactColumn factColumn = (FactColumn) httpSession.getAttribute("factColumn");
            FactColumnHelper factColumnHelper = new FactColumnHelper();
            for (int i = 0; i < rolenamesArray.length; i++) {
                check = factColumnHelper.saveAndPublishRole(factColumn, rolenamesArray[i]);

            }

            out.print(check);

        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }

        return null;
    }

    public ActionForward deleteTable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter printWriter = null;
        String tableId = "";
        try {
            printWriter = response.getWriter();
            tableId = request.getParameter("tableId");
            ConnectionDAO ConnectionDAO = new ConnectionDAO();
            boolean returnResult = ConnectionDAO.deleteTable(tableId);
            printWriter.print(returnResult);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }




        return null;
    }

    public ActionForward deleteNetWorkConnection(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        String connId = "";
        HttpSession session = request.getSession(false);
        boolean checkResult = false;
        try {
            out = response.getWriter();
            connId = request.getParameter("connID");
            ConnectionDAO connectionDAO = new ConnectionDAO();
            checkResult = connectionDAO.deleteNetWorkConnection(connId);
            if (checkResult) {
                session.removeAttribute("connId");
            }
            out.print(checkResult);

        } catch (Exception e) {
            logger.error("Exception:", e);
        }


        return null;
    }

    public ActionForward tesetConnection(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        PrintWriter out = response.getWriter();
        Connection con = null;
        String status = "";
        int i = 0;
        try {

            String username = request.getParameter("usrename");
            String password = request.getParameter("password");
            String server = request.getParameter("hostName");
//            String sid=request.getParameter("sid");
            String port = request.getParameter("port");
            String seqlServer = request.getParameter("seqlServer");
            String sqlserverdbname = request.getParameter("sqlserverdbname");
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(username+password+server+sid+port);
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String url = "jdbc:jtds:sqlserver://" + server + ":" + port + "/" + sqlserverdbname;
            con = DriverManager.getConnection(url, username, password);

            if (con != null) {
                status = "Connection Successful";
            } else {
                status = "Connection Failed";
            }

            out.println(status);



        } catch (Exception e) {
//            i=1;
            logger.error("Exception:", e);
        } finally {
            if (con != null) {
                con.close();
                con = null;
            }
        }





        return null;
    }

    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("updateUserConnection", "updateUserConnection");
        map.put("deleteUserConnection", "deleteUserConnection");
        map.put("getRelatedDbTables", "getRelatedDbTables");
        map.put("getRelatedDbTables1", "getRelatedDbTables1");
        map.put("editRelatedDbTables", "editRelatedDbTables");

        map.put("addTableAsQuery", "addTableAsQuery");
        map.put("saveDetails", "saveDetails");
        map.put("savesqlConnection", "savesqlConnection");
        map.put("addColumnToTable", "addColumnToTable");
        map.put("insertInDbMasterDetails", "insertInDbMasterDetails");
        map.put("checkAndSaveInBussSrc", "checkAndSaveInBussSrc");
        map.put("checkAndSaveInBussRole", "checkAndSaveInBussRole");
        map.put("saveAndPublishRole", "saveAndPublishRole");
        map.put("getRelatedInfo", "getRelatedInfo");
        map.put("updateBussTable", "updateBussTable");
        map.put("InsertBussTable", "InsertBussTable");
        map.put("deleteTable", "deleteTable");
        map.put("deleteNetWorkConnection", "deleteNetWorkConnection");
        map.put("tesetConnection", "tesetConnection");

        return map;
    }
}
