package com.progen.querylayer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;
/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

/**
 *
 * @author Administrator
 */
public class MyDAO extends PbDb {

    public static Logger logger = Logger.getLogger(MyDAO.class);

    public ArrayList getTableNames() {
        ArrayList list = new ArrayList();
        try {
            Connection con = null;

            Class.forName("oracle.jdbc.driver.OracleDriver");
            String parentName = "";
            con = ProgenConnection.getInstance().getConnection();

            Statement allTablesSt = con.createStatement();
            // ResultSet allTableRs = allTablesSt.executeQuery(" select  * from tab");
            ResultSet allTableRs = allTablesSt.executeQuery(" select table_name from prg_database_master_table where table_type='Table' and connection_id=21");
            PbReturnObject allTableObject = new PbReturnObject(allTableRs);

            Statement stmt = con.createStatement();
            ResultSet rs = null;
            String sql = "SELECT column_name from all_tab_cols where table_name='";
            //String sql = "select * from";
            //String[] tab=new String[]{"PRG_BUSINESS_AREA","PRG_GBL_BUCKET","PRG_USERS"};
            String[] tab = new String[allTableObject.getRowCount()];
            for (int i = 0; i < allTableObject.getRowCount(); i++) {
                tab[i] = allTableObject.getFieldValueString(i, "TNAME");

            }
            String[] colNames = null;
            String colName = "";
            String tabName = "";
            for (int i = 0; i < tab.length; i++) {
                tabName = tab[i];

                rs = stmt.executeQuery(sql + tab[i] + "'");

                // rs=stmt.executeQuery(sql+tab[i]);
                //rs=stmt.executeQuery(query);
                int rowCountFlag = 0;
                PbReturnObject pbReturnObject = new PbReturnObject(rs);


                Table table = new Table();
                table.setTableName(tabName);

                list.add(table);
                colNames = pbReturnObject.getColumnNames();
                for (int j = 0; j < pbReturnObject.getRowCount(); j++) {
                    colName = pbReturnObject.getFieldValueString(j, colNames[0]);
                    Table secTable = new Table();
                    if (j + 1 == pbReturnObject.getRowCount()) {
                        secTable.setEndTable("true");

                    }
                    secTable.setColumnName(colName);
                    list.add(secTable);
                }
            }

            stmt.close();
            con.close();

        } catch (ClassNotFoundException e) {
            logger.error("Exception:", e);
        } catch (SQLException e) {
            logger.error("Exception:", e);
        }

        return list;

    }

    public /*
             * ArrayList
             */ String getConnections() {

        StringBuffer buildingDbConnections = new StringBuffer();

        String connectinsstr = null;

        ArrayList finalList = new ArrayList();

        PbReturnObject conePbReturnObject = new PbReturnObject();
        try {

            String sql = "select * from PRG_USER_CONNECTIONS";
            conePbReturnObject = super.execSelectSQL(sql);
            buildingDbConnections.append("<div style='height:553px;overflow-y:auto;width:auto;'>");
            buildingDbConnections.append("<ul id='myList' class='filetree' style='width:300px;'>");
            buildingDbConnections.append("<li  class='closed' style='background-image:url('images/treeViewImages/plus.gif')'><img src='images/treeViewImages/database_connect.png'><span id='123' class='openmenu1'><font size='1px' face='verdana'> &nbsp;Database Connections</font></span>");
            buildingDbConnections.append("<ul>");

            for (int i = 0; i < conePbReturnObject.getRowCount(); i++) {

                buildingDbConnections.append("<li id=" + conePbReturnObject.getFieldValueString(i, 0) + " class='closed' onclick='gettablesndviews(this.id)'><img src='images/treeViewImages/database_connect.png'><span id=" + conePbReturnObject.getFieldValueString(i, 0) + " class='connMenu'><font size='1px' face='verdana'>&nbsp;" + conePbReturnObject.getFieldValueString(i, 1) + "</font></span>");
                buildingDbConnections.append("<ul>");
//                viewconnections.append("<li class='closed'><span class='folder'><font size='1px' face='verdana'>&nbsp;Views</font></span>");
//            viewconnections.append("<ul id='tableList'>");
                // buildingDbConnections.append("<li class='closed'><span class='folder'><font size='1px' face='verdana'>&nbsp;Tables</font></span>");


                DatabaseConnection databaseConnection = new DatabaseConnection();
                databaseConnection.setConnectionName(conePbReturnObject.getFieldValueString(i, 1));
                databaseConnection.setConnectionId(conePbReturnObject.getFieldValueString(i, 0));
                databaseConnection.setConnectionType(conePbReturnObject.getFieldValueString(i, 11));
                databaseConnection.setOpenConnection("no");
                ArrayList tablelist = new ArrayList();
                ArrayList viewList = new ArrayList();
                ArrayList dimensionList = new ArrayList();
                if (conePbReturnObject.getFieldValueString(i, 2) != null) {

                    buildingDbConnections.append("<li class='closed'><span class='folder'><font size='1px' face='verdana'>&nbsp;Tables</font></span>");
                    String tables = getTableNamesConnection(conePbReturnObject.getFieldValueString(i, 2), conePbReturnObject.getFieldValueString(i, 3), conePbReturnObject.getFieldValueString(i, 7), conePbReturnObject.getFieldValueString(i, 4), conePbReturnObject.getFieldValueString(i, 5), conePbReturnObject.getFieldValueString(i, 0));
                    buildingDbConnections.append(tables);
                    //////////////////////.println("tables\t"+tables);
                    buildingDbConnections.append("</li>");
                    //tablelist = getTableNamesConnection(rs.getString("USER_NAME"), rs.getString("PASSWORD"), rs.getString("PORT"), rs.getString("SERVER"), rs.getString("SERVICE_ID"), rs.getString("CONNECTION_ID"));
                    buildingDbConnections.append("<li class='closed'><span class='folder'><font size='1px' face='verdana'>&nbsp;Views</font></span>");
                    String views = getViewNamesConnection(conePbReturnObject.getFieldValueString(i, 2), conePbReturnObject.getFieldValueString(i, 3), conePbReturnObject.getFieldValueString(i, 7), conePbReturnObject.getFieldValueString(i, 4), conePbReturnObject.getFieldValueString(i, 5), conePbReturnObject.getFieldValueString(i, 0));
//                    viewList = getViewNamesConnection(rs.getString("USER_NAME"), rs.getString("PASSWORD"), rs.getString("PORT"), rs.getString("SERVER"), rs.getString("SERVICE_ID"), rs.getString("CONNECTION_ID"));
                    buildingDbConnections.append(views);
                    buildingDbConnections.append("</li>");


                    databaseConnection.setTableList(tablelist);
                    databaseConnection.setViewList(viewList);
                    //  databaseConnection.setDimensionList(dimensionList);
                    finalList.add(databaseConnection);
                } else if (conePbReturnObject.getFieldValueString(i, 2) == null || conePbReturnObject.getFieldValueString(i, 2).equals("")) {
                    String tables = getTableNamesConnection(conePbReturnObject.getFieldValueString(i, 2), conePbReturnObject.getFieldValueString(i, 3), conePbReturnObject.getFieldValueString(i, 7), conePbReturnObject.getFieldValueString(i, 4), conePbReturnObject.getFieldValueString(i, 5), conePbReturnObject.getFieldValueString(i, 0));
                    buildingDbConnections.append("<li class='closed'><span class='folder'><font size='1px' face='verdana'>&nbsp;Tables</font></span>");
                    buildingDbConnections.append(tables);
                    buildingDbConnections.append("</li>");
                    //tablelist = getTableNamesConnection(rs.getString("USER_NAME"), rs.getString("PASSWORD"), rs.getString("PORT"), rs.getString("SERVER"), rs.getString("SERVICE_ID"), rs.getString("CONNECTION_ID"));

                    databaseConnection.setTableList(tablelist);
                    //databaseConnection.setViewList(null);
                    finalList.add(databaseConnection);
                }
                buildingDbConnections.append("</ul>");
                buildingDbConnections.append("</li>");
            }

            buildingDbConnections.append("</ul></li></ul></div>");


        } catch (SQLException e) {
            logger.error("Exception:", e);
        }
        connectinsstr = buildingDbConnections.toString();
//        return finalList;
        return connectinsstr;
    }

    public /*
             * ArrayList
             */ String getTableNamesConnection(String userName, String password, String portNo, String server, String sId, String cid) {

        StringBuffer buildingtables = new StringBuffer();
        String buildedtables = null;
        ArrayList list = new ArrayList();
        PbReturnObject allTableObject = new PbReturnObject();

        try {

            buildingtables.append("<ul id='tableList'>");

//            Class.forName("oracle.jdbc.driver.OracleDriver");
            String parentName = "";
            allTableObject = super.execSelectSQL(" select TABLE_NAME,TABLE_ID from prg_db_master_table where table_type='Table' and connection_id=" + cid);

            String sql = "SELECT TABLE_COL_NAME,COLUMN_ID,IS_PRIMARY_KEY,IS_ACTIVE from prg_db_master_table_details where TABLE_ID=";
            String[] tab = new String[allTableObject.getRowCount()];
            String[] tab1 = new String[allTableObject.getRowCount()];
            for (int i = 0; i < allTableObject.getRowCount(); i++) {
                int flag = 0;
                for (int j = 0; j < i; j++) {
                    if (allTableObject.getFieldValueString(i, 0).equals(allTableObject.getFieldValueString(j, 0))) {
                        flag = 1;
                    }
                }
                if (flag == 0) {
                    if (allTableObject.getFieldValueString(i, 0) != null && !(allTableObject.getFieldValueString(i, 0).equals(""))) {
                        buildingtables.append("<li onclick='getTableId(this)' id=" + allTableObject.getFieldValueInt(i, 1) + " class='closed'><img src='images/treeViewImages/database_table.png'>&nbsp;<span id='tableId-" + String.valueOf(allTableObject.getFieldValueInt(i, 1)) + "' class='openmenu'  onclick='colDelete(\"" + allTableObject.getFieldValueInt(i, 1) + "\")'>" + allTableObject.getFieldValueString(i, 0) + "</span>");
                    }

                    buildingtables.append("<ul id='" + cid + allTableObject.getFieldValueString(i, 0) + "'>");

                    String columns = getcolumnNames(sql, String.valueOf(allTableObject.getFieldValueInt(i, 1)), allTableObject.getFieldValueString(i, 0));
                    buildingtables.append(columns);
                    buildingtables.append("</ul>");
                    buildingtables.append("</li>");
                    tab[i] = String.valueOf(allTableObject.getFieldValueInt(i, 1));
                    tab1[i] = allTableObject.getFieldValueString(i, 0);
                }
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        buildingtables.append("</ul>");
        buildedtables = buildingtables.toString();
        return buildedtables;
    }

    public /*
             * ArrayList
             */ String getViewNamesConnection(String userName, String password, String portNo, String server, String sId, String cid) {

        StringBuffer viewconnections = new StringBuffer();
        String viewstr = null;
        ArrayList list = new ArrayList();
        PbReturnObject allTableObject = new PbReturnObject();
        try {
            viewconnections.append("<ul id='tableList'>");
            allTableObject = super.execSelectSQL("select table_name,table_id from prg_db_master_table where table_type='View' and connection_id=" + cid);

//            Statement stmt = con.createStatement();
//            ResultSet rs = null;

            String sql = "SELECT table_col_name,column_id,is_primary_key,is_active from prg_db_master_table_details where table_id=";
            String[] tab = new String[allTableObject.getRowCount()];
            String[] tab1 = new String[allTableObject.getRowCount()];
//            viewconnections.append("<li class='closed'><span class='folder'><font size='1px' face='verdana'>&nbsp;Views</font></span>");
            for (int i = 0; i < allTableObject.getRowCount(); i++) {


                if (allTableObject.getFieldValueString(i, 0) != null && !(allTableObject.getFieldValueString(i, 0).equals(""))) {
                    viewconnections.append("<li onclick='getTableId(this)' id=" + allTableObject.getFieldValueInt(i, 1) + " class='closed'><img src='images/treeViewImages/database_table.png'>&nbsp;<span id='tableId-" + String.valueOf(allTableObject.getFieldValueInt(i, 1)) + "' class='openmenu'  onclick='colDelete('" + allTableObject.getFieldValueInt(i, 1) + "')'>" + allTableObject.getFieldValueString(i, 0) + "</span>");
                    viewconnections.append("<ul id='" + allTableObject.getFieldValueString(i, 0) + "'>");
                    String columns = getcolumnNames(sql, String.valueOf(allTableObject.getFieldValueInt(i, "TABLE_ID")), allTableObject.getFieldValueString(i, 0));
                    viewconnections.append(columns);
                    //viewconnections.append("<li class='closed'><img src='images/treeViewImages/database_table.png'><span id='tableId-" + allTableObject.getFieldValueInt(i, "TABLE_ID") + "' onclick=colDelete(" + allTableObject.getFieldValueInt(i, "TABLE_ID") + ")><font size='1px' face='verdana'>" + allTableObject.getFieldValueString(i, "TABLE_NAME") + "</font></span>");
                    viewconnections.append("</li>");
                    viewconnections.append("</ul>");
                }




                tab[i] = String.valueOf(allTableObject.getFieldValueInt(i, 1));
                tab1[i] = allTableObject.getFieldValueString(i, 0);
            }
            viewconnections.append("</ul>");
            /*
             * String[] colNames = null; String colName = ""; String tabName =
             * ""; String tabId = ""; String is_pk = ""; String is_available =
             * ""; for (int i = 0; i < tab.length; i++) { tabName = tab1[i];
             * tabId = tab[i]; rs = stmt.executeQuery(sql + tab[i]); int
             * rowCountFlag = 0; PbReturnObject pbReturnObject = new
             * PbReturnObject(rs);
             *
             * Table table = new Table(); table.setTableName(tabName);
             * table.setTableId(tabId); list.add(table); colNames =
             * pbReturnObject.getColumnNames(); String columnId = ""; for (int j
             * = 0; j < pbReturnObject.getRowCount(); j++) { colName =
             * pbReturnObject.getFieldValueString(j, colNames[0]); columnId =
             * String.valueOf(pbReturnObject.getFieldValueInt(j, colNames[1])) +
             * "," + tabId; is_pk = pbReturnObject.getFieldValueString(j,
             * colNames[2]); is_available =
             * pbReturnObject.getFieldValueString(j, colNames[3]);
             *
             * viewconnections.append("<li class='closed'><span
             * class='folder'><font size='1px'
             * face='verdana'>&nbsp;Views</font></span>");
             * viewconnections.append("<ul id='tableList'>");
             *
             * if (colName != null && !colName.equals("")) {
             * viewconnections.append("<li id='columns'><img id='colId-" +
             * columnId + "'>"); } if (is_available != null &&
             * is_available.equals("Y")) { viewconnections.append("<input
             * type='checkbox' name='chk2' checked value=" + columnId +
             * "><span><font size='1px' face='verdana'>" + colName +
             * "</font></span>"); } if (is_available != null &&
             * is_available.equals("N")) { viewconnections.append("<input
             * type='checkbox' name='chk2' checked value=" + columnId +
             * "><span><font size='1px' face='verdana'>" + colName +
             * "</font></span>"); } if (is_pk != null && is_pk.equals("Y")) {
             * viewconnections.append("<img
             * src='images/treeViewImages/key.png'>"); }
             *
             *
             * Table secTable = new Table(); if (j + 1 ==
             * pbReturnObject.getRowCount()) { secTable.setEndTable("true");
             * viewconnections.append("</ul>"); viewconnections.append("</li>");
             *
             * }
             * secTable.setColumnName(colName); secTable.setColumnId(columnId);
             * secTable.setIsPk(is_pk); secTable.setIsAvailable(is_available);
             * list.add(secTable); viewconnections.append("</ul>");
             * viewconnections.append("</li>"); } }
             */

            viewstr = viewconnections.toString();




        } catch (Exception e) {
            logger.error("Exception:", e);
        }

//        return list;
        return viewstr;

    }

    public String getcolumnNames(String sql, String tableid, String tablename) throws Exception {

        StringBuffer columnbuffer = new StringBuffer();
        String columnstr = null;
        PbReturnObject pbReturnObject = new PbReturnObject();
        try {
            String[] colNames = null;
            String colName = "";
            String tabName = "";
            String tabId = "";
            String is_pk = "";
            String is_available = "";
            //for (int i = 0; i < tab.length; i++) {
            tabName = tablename;
            tabId = tableid;
            String colqry = sql + tableid + " order by table_col_name";
            pbReturnObject = super.execSelectSQL(colqry);
            Table table = new Table();
            table.setTableName(tabName);
            table.setTableId(tabId);

            colNames = pbReturnObject.getColumnNames();
            String columnId = "";

            //From here we are getting column names
            if (pbReturnObject.getRowCount() != 0) {
                for (int j = 0; j < pbReturnObject.getRowCount(); j++) {

                    colName = pbReturnObject.getFieldValueString(j, colNames[0]);
                    columnId = String.valueOf(pbReturnObject.getFieldValueInt(j, colNames[1])) + "," + tabId;
                    is_pk = pbReturnObject.getFieldValueString(j, colNames[2]);
                    is_available = pbReturnObject.getFieldValueString(j, colNames[3]);


                    if (colName != null && !(colName.equals(""))) {
                        columnbuffer.append("<li id='columns'><img id='colId-" + columnId + "'>");
                    }
                    if (is_available != null && is_available.equals("Y")) {
                        columnbuffer.append("<input type='checkbox' name='chk2' checked value=" + columnId + ">");
                        columnbuffer.append("<span>" + colName + "</span>");
                    }
                    if (is_available != null && is_available.equals("N")) {
                        columnbuffer.append("<input type='checkbox' name='chk2' value=" + columnId + ">");
                        columnbuffer.append("<span>" + colName + "</span>");
                    }
                    if (is_pk != null && is_pk.equals("Y")) {
                        columnbuffer.append("<img src='images/treeViewImages/key.png' >");
                    }

                    Table secTable = new Table();
                    if (j + 1 == pbReturnObject.getRowCount()) {
                        secTable.setEndTable("true");
                        columnbuffer.append("</li>");
                        // columnbuffer.append("</ul>");
                        // columnbuffer.append("</li>");

                    }
                    secTable.setColumnName(colName);
                    secTable.setColumnId(columnId);
                    secTable.setIsPk(is_pk);
                    secTable.setIsAvailable(is_available);
                }
            }

        } catch (SQLException e) {
            logger.error("Exception:", e);
        }
        columnstr = columnbuffer.toString();
        return columnstr;
    }
}
