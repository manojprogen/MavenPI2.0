/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.connection;

import com.google.gson.Gson;
import com.progen.contypes.GetConnectionType;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author Administrator
 */
public class ConnectionDAO extends PbDb {

    ConnectionResourceBundle resBundle = new ConnectionResourceBundle();
    public static Logger logger = Logger.getLogger(ConnectionDAO.class);

    boolean upDateUserConn(String[] userList) {
        ArrayList alist = new ArrayList();
        String upDateUserConnQuery = resBundle.getString("upDateUserConn");

        String finalQuery = "";
        boolean result = false;

        try {
            finalQuery = buildQuery(upDateUserConnQuery, userList);

            execModifySQL(finalQuery);

            result = true;
            return result;
        } catch (Exception exception) {
            logger.error("Exception:", exception);
            return result;
        }

    }

    public Connection getConnection(String conId) {
        Connection connection = null;
        PbReturnObject retObj = null;
        String getconQuery = resBundle.getString("getConnectionId");
        String finalQuery = "";

        String serverName = "";
        String dsnName = "";
        String connType = "";
        String serverPort = "";
        String serviceId = "";
        String userName = "";
        String password = "";
        String server = "";

        Object[] Obj = new Object[1];
        Obj[0] = conId;

        String[] tableColumnNames = null;

        try {
            finalQuery = buildQuery(getconQuery, Obj);
            retObj = execSelectSQL(finalQuery);
            tableColumnNames = retObj.getColumnNames();

            // serverName = retObj.getFieldValueString(0,tableColumnNames[4]);
            serverPort = retObj.getFieldValueString(0, tableColumnNames[5]);
            serviceId = retObj.getFieldValueString(0, tableColumnNames[3]);
            userName = retObj.getFieldValueString(0, tableColumnNames[0]);
            password = retObj.getFieldValueString(0, tableColumnNames[1]);
            server = retObj.getFieldValueString(0, tableColumnNames[2]);

            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@" + server + ":" + serverPort + ":" + serviceId + "", userName, password);
            //"jdbc:oracle:thin:@192.168.0.107: 1521 : XE , metadata, metadata"


            //return connection;
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
        return connection;
    }

    public HashMap checkTableName(String conId, String tableName, String query) throws Exception {
        HashMap details = new HashMap();
        ResultSetMetaData rmData = null;
        boolean tableFound = false;
        String checkTableName = resBundle.getString("checkTableName");
        Object conObj[] = new Object[1];
        conObj[0] = conId;
        String fincheckTableName = buildQuery(checkTableName, conObj);

        PbReturnObject tabNames = execSelectSQL(fincheckTableName);
        for (int p = 0; p < tabNames.getRowCount(); p++) {
            if (tabNames.getFieldValueString(p, "TABLE_NAME").equalsIgnoreCase(tableName)) {
                tableFound = true;
            }
            if (tableFound) {
                break;
            }
        }
        if (tableFound) {
            details.put("IsTable", "true");
        } else {
            details.put("IsTable", "false");
        }

        PbReturnObject pbro = null;
        boolean correctQuery = false;
        try {
            Connection con = getConnection(conId);//new ProgenConnection().getCustomerConn();
            PreparedStatement ps = con.prepareCall(query);
            ResultSet rs = ps.executeQuery();
            rmData = rs.getMetaData();
            pbro = new PbReturnObject(rs);
            correctQuery = true;
            con.close();
        } catch (Exception ex) {
            correctQuery = false;
        }
        if (correctQuery == true) {
            details.put("isCorrectQuery", "true");
        } else {
            details.put("isCorrectQuery", "false");
        }
        details.put("TabDetails", pbro);
        details.put("DbMetadata", rmData);

        return details;
    }

    boolean saveQueryTable(String conId, String tableName, String query, PbReturnObject tabDetails) throws Exception {

        String CONNECTION_ID = conId;
        String USER_SCHEMA = tableName;
        String TABLE_NAME = tableName;
        String TABLE_ALIAS = tableName;
        String TABLE_TYPE = "Table";
        String DB_QUERY = query;
        String[] userList = new String[11];

        userList[0] = CONNECTION_ID;
        userList[1] = USER_SCHEMA;
        userList[2] = TABLE_NAME;
        userList[3] = TABLE_ALIAS;
        userList[4] = TABLE_TYPE;
        userList[5] = "";
        userList[6] = "";
        userList[7] = "";
        userList[8] = "";
        userList[9] = DB_QUERY;

        //Insert into  prg_db_master_table(CONNECTION_ID,USER_SCHEMA,TABLE_ID,TABLE_NAME,TABLE_ALIAS,TABLE_TYPE,CREATED_BY,UPDATED_BY,CREATED_ON,UPDATED_ON,DB_QUERY)
        //values ('&','&',PRG_DATABASE_MASTER_SEQ.nextval,'&','&','&','&','&','&','&','&')"}


        String saveQueryTableQuery = resBundle.getString("saveQueryTable");
        String saveQueryDetailsTableQuery = resBundle.getString("saveQueryDetailsTable");
        ArrayList queries = new ArrayList();

        String cols[] = tabDetails.getColumnNames();
        String colTypes[] = tabDetails.getColumnTypes();
        int colsSizes[] = tabDetails.getColumnSizes();

        String finalQuery = "";
        boolean result = false;

        try {
            finalQuery = buildQuery(saveQueryTableQuery, userList);
            queries.add(finalQuery);

            for (int l = 0; l < cols.length; l++) {

                //Insert into  PRG_DB_MASTER_TABLE_DETAILS(column_id, table_id, table_col_name, table_col_cust_name, col_type, col_length, is_primary_key, is_active)
                //values (PRG_DB_MASTER_TABLE_DTLS_SEQ.nextval,PRG_DATABASE_MASTER_SEQ.currval,'&','&','&',&,'N','Y')

                userList = new String[4];
                userList[0] = cols[l];
                userList[1] = cols[l];
                userList[2] = colTypes[l];
                userList[3] = String.valueOf(colsSizes[l]);
                finalQuery = buildQuery(saveQueryDetailsTableQuery, userList);
                queries.add(finalQuery);
            }


            result = executeMultiple(queries);
        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return result;
    }

    //added by susheela start
    public boolean checkForUserConnecionOnDelete(String connectionId) throws Exception {
        String checkUserConnection = resBundle.getString("checkUserConnection");
        Object conOb[] = new Object[1];
        conOb[0] = connectionId;
        String fincheckUserConnection = buildQuery(checkUserConnection, conOb);

        PbReturnObject conGrpsObj = execSelectSQL(fincheckUserConnection);
        boolean isGroup = false;
        if (conGrpsObj.getRowCount() > 0) {
            isGroup = true;
        }
        return isGroup;
    }

    public void deleteConnection(String connectionId) throws Exception {
        ArrayList deleteDet = new ArrayList();
        String deleteDbRelDetails = resBundle.getString("deleteDbRelDetails");
        String deleteDbRelDetails2 = resBundle.getString("deleteDbRelDetails2");
        String deleteDbRelMaster1 = resBundle.getString("deleteDbRelMaster1");
        String deleteDbRelMaster2 = resBundle.getString("deleteDbRelMaster2");
        String deleteDbTablesDetails = resBundle.getString("deleteDbTablesDetails");
        String deleteDbTables = resBundle.getString("deleteDbTables");
        String deleteUserConnection = resBundle.getString("deleteUserConnection");
        Object conObj[] = new Object[1];
        conObj[0] = connectionId;
        String findeleteDbRelDetails = buildQuery(deleteDbRelDetails, conObj);

        deleteDet.add(findeleteDbRelDetails);
        // execModifySQL(findeleteDbRelDetails);
        String findeleteDbRelDetails2 = buildQuery(deleteDbRelDetails2, conObj);
        deleteDet.add(findeleteDbRelDetails2);
        // execModifySQL(findeleteDbRelDetails2);


        String findeleteDbRelMaster1 = buildQuery(deleteDbRelMaster1, conObj);
        deleteDet.add(findeleteDbRelMaster1);

        //execModifySQL(findeleteDbRelMaster);

        String findeleteDbRelMaster2 = buildQuery(deleteDbRelMaster2, conObj);

        deleteDet.add(findeleteDbRelMaster2);
        // execModifySQL(findeleteDbRelMaster2);

        String findeleteDbTablesDetails = buildQuery(deleteDbTablesDetails, conObj);

        deleteDet.add(findeleteDbTablesDetails);
        // execModifySQL(findeleteDbRelMaster);

        String findeleteDbTables = buildQuery(deleteDbTables, conObj);

        deleteDet.add(findeleteDbTables);
        // execModifySQL(findeleteDbRelMaster);

        String findeleteUserConnection = buildQuery(deleteUserConnection, conObj);

        deleteDet.add(findeleteUserConnection);
        // execModifySQL(findeleteUserConnection);
        try {
            executeMultiple(deleteDet);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public PbReturnObject getRelatedDbTables(String dbTableId) throws Exception {
        PbReturnObject all = new PbReturnObject();
        String getRelatedDbTables = resBundle.getString("getRelatedDbTables");
        String getDbCols = resBundle.getString("getDbCols");
        String relTabInfo = resBundle.getString("relTabInfo");
        String dbTableDetales = resBundle.getString("dbTableDetales");
        Object dbTabObj[] = new Object[2];
        dbTabObj[0] = dbTableId;
        dbTabObj[1] = dbTableId;
        String fingetRelatedDbTables = buildQuery(getRelatedDbTables, dbTabObj);

        String finrelTabInfo = buildQuery(relTabInfo, dbTabObj);

        PbReturnObject dbDetails = execSelectSQL(fingetRelatedDbTables);
        String fingetDbCols = buildQuery(getDbCols, dbTabObj);

        PbReturnObject fingetDbColsObj = execSelectSQL(fingetDbCols);
        PbReturnObject relTabInfoObj = execSelectSQL(finrelTabInfo);
//

        Object dbTabObj2[] = new Object[1];
        dbTabObj2[0] = dbTableId;
        String findbTableDetales = buildQuery(dbTableDetales, dbTabObj2);
        PbReturnObject dbTabInfoObj = execSelectSQL(findbTableDetales);
        all.setObject("dbDetails", dbDetails);
        all.setObject("dbCols", fingetDbColsObj);
        all.setObject("relTabInfo", relTabInfoObj);
        all.setObject("dbTabInfo", dbTabInfoObj);
        return all;
    }

    public PbReturnObject getRelatedDbTables1(String dbTableId) throws Exception {
        PbReturnObject all = new PbReturnObject();
        String getRelatedDbTables = resBundle.getString("getRelatedDbTables");
        String relTabInfo = resBundle.getString("relTabInfo");
        String dbTableDetales = resBundle.getString("dbTableDetales");
        String getRelatedGroups = resBundle.getString("getRelatedGroups");
        String getGroupNames = resBundle.getString("getGroupNames");
        Object dbTabObj[] = new Object[2];
        dbTabObj[0] = dbTableId;
        dbTabObj[1] = dbTableId;
        String fingetRelatedDbTables = buildQuery(getRelatedDbTables, dbTabObj);
        String fingetRelatedGroups = buildQuery(getRelatedGroups, dbTabObj);


        String finrelTabInfo = buildQuery(relTabInfo, dbTabObj);
        PbReturnObject dbDetails = execSelectSQL(fingetRelatedDbTables);
        PbReturnObject relGrpInfo = execSelectSQL(fingetRelatedGroups);
        PbReturnObject relGrpNames = execSelectSQL(getGroupNames);
        PbReturnObject relTabInfoObj = execSelectSQL(finrelTabInfo);
        Object dbTabObj2[] = new Object[1];
        dbTabObj2[0] = dbTableId;
        String findbTableDetales = buildQuery(dbTableDetales, dbTabObj2);
        PbReturnObject dbTabInfoObj = execSelectSQL(findbTableDetales);
        all.setObject("dbDetails", dbDetails);
        all.setObject("relTabInfo", relTabInfoObj);
        all.setObject("relGrpInfo", relGrpInfo);
        all.setObject("relGrpNames", relGrpNames);
        all.setObject("dbTabInfo", dbTabInfoObj);
        return all;
    }

    public boolean editRelatedDbTables(String connId, String dbTableId, String tabVals, String allTabValues) throws Exception {
        boolean status = false;
        ArrayList tabValsList = new ArrayList();
        ArrayList delList = new ArrayList();
        ArrayList masterList = new ArrayList();
        ArrayList tabs = new ArrayList();
        String deleteDbTableRelDetails = resBundle.getString("deleteDbTableRelDetails");
        String getRelatedDbTabForEdit1 = resBundle.getString("getRelatedDbTabForEdit1");
//        String getRelatedDbTabForEdit2 = resBundle.getString("getRelatedDbTabForEdit2");
        String deleteDbRelMaster = resBundle.getString("deleteDbRelMaster");
        String tabValsNew[] = tabVals.split(",");

//            if (!tabValsList.contains(tabValsNew[m])) {
//                tabValsList.add(tabValsNew[m]);
//            }
//        }
        String allTabValuesNew[] = allTabValues.split(",");
        for (int g = 0; g < allTabValuesNew.length; g++) {
            int flag = 0;
            for (int m = 0; m < tabValsNew.length; m++) {
                if (allTabValuesNew[g].equals(tabValsNew[m])) {
                    flag = 1;
                }
            }

            Object tabObjs[] = new Object[1];
            if (flag == 0) {
                tabObjs[0] = allTabValuesNew[g];

                String findeleteDbTableRelDetails = buildQuery(deleteDbTableRelDetails, tabObjs);
//                
                delList.add(findeleteDbTableRelDetails);

                String findeleteRelatedDbTab = buildQuery(deleteDbRelMaster, tabObjs);
                masterList.add(findeleteRelatedDbTab);
            }
        }
        try {
            executeMultiple(delList);
            executeMultiple(masterList);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

//        ArrayList al = new ArrayList();
//        for (int y = 0; y < masterList.size(); y++) {
//            String details = masterList.get(y).toString();
//            PbReturnObject pbro = execSelectSQL(details);
//            String tabsVal = tabs.get(y).toString();
//            String tabArr[] = tabsVal.split(":");
//            String tabP = tabArr[0];
//            String tabS = tabArr[1];
//            Object delMaster[] = new Object[2];
//            delMaster[0] = tabP;
//            delMaster[1] = tabS;
//            if (pbro.getRowCount() == 0) {
//                String fin = buildQuery(deleteDbRelMaster, delMaster);
//
//                al.add(fin);
//            }
//        }
//        try {
//            executeMultiple(al);
//        } catch (Exception ex) {
//            logger.error("Exception:",ex);
//        }
        return status;
    }
    //added by susheela over

    public ConnectionMetadata getConnectionForElement(String element) {

        PbDb pbDb = new PbDb();
        String getConnectionIdByElementIdQuery = resBundle.getString("getConnectionIdByElementId");
        String finalQuery = "";
        Object[] Obj = new Object[1];
        Obj[0] = element;
        finalQuery = pbDb.buildQuery(getConnectionIdByElementIdQuery, Obj);
        ConnectionMetadata conMetaData = this.getConnectionMetadata(finalQuery);
        return conMetaData;

    }

    public ConnectionMetadata getConnectionByTable(String tabID) {

        PbDb pbDb = new PbDb();
        String getConnectionIdByElementIdQuery = resBundle.getString("getConnectionByTable");
        String finalQuery = "";
        Object[] Obj = new Object[1];
        Obj[0] = tabID;

        finalQuery = pbDb.buildQuery(getConnectionIdByElementIdQuery, Obj);
        ConnectionMetadata conMetaData = this.getConnectionMetadata(finalQuery);
        return conMetaData;
    }

    public ConnectionMetadata getConnectionByConId(String connId) {
        PbDb pbDb = new PbDb();
        String getConnectionIdByConnQuery = resBundle.getString("getConnectionByConId");
        String finalQuery = "";

        Object[] Obj = new Object[1];
        Obj[0] = connId;

        finalQuery = pbDb.buildQuery(getConnectionIdByConnQuery, Obj);
        ConnectionMetadata conMetaData = this.getConnectionMetadata(finalQuery);
        return conMetaData;

    }

    private ConnectionMetadata getConnectionMetadata(String connQuery) {

        PbDb pbDb = new PbDb();

        PbReturnObject retObj;
        try {
            retObj = pbDb.execSelectSQL(connQuery);

            String[] tableColumnNames = retObj.getColumnNames();

            String serverName = retObj.getFieldValueString(0, tableColumnNames[4]);
            String serverPort = retObj.getFieldValueString(0, tableColumnNames[5]);
            String serviceId = retObj.getFieldValueString(0, tableColumnNames[3]);
            String userName = retObj.getFieldValueString(0, tableColumnNames[0]);
            String password = retObj.getFieldValueString(0, tableColumnNames[1]);
            String server = retObj.getFieldValueString(0, tableColumnNames[2]);
            String dsnName = retObj.getFieldValueString(0, tableColumnNames[6]);
            String connType = retObj.getFieldValueString(0, tableColumnNames[7]);
            String dbName = retObj.getFieldValueString(0, tableColumnNames[8]);
            int connectionId = retObj.getFieldValueInt(0, tableColumnNames[9]);
            int maxActive = retObj.getFieldValueInt(0, tableColumnNames[10]);
            int maxWait = retObj.getFieldValueInt(0, tableColumnNames[11]);


            ConnectionMetadata conMetaData = new ConnectionMetadata(connectionId,
                    serverName,
                    userName,
                    password,
                    server,
                    serviceId,
                    connType, dbName,
                    Integer.parseInt(serverPort), dsnName, maxActive, maxWait);
            return conMetaData;
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public String savesqlConnection(ArrayList arrayList) {
        String queryString = resBundle.getString("savesqlConnection");

        return null;

    }

    public ConnectionMetadata getConnectionByGroupId(String grpID) {

        PbDb pbDb = new PbDb();
        String getConnectionIdBygrpID = resBundle.getString("getConnectionByGroupId");
        String finalQuery = "";
        Object[] Obj = new Object[1];
        Obj[0] = grpID;
        String conectionID = "";

        try {
            finalQuery = pbDb.buildQuery(getConnectionIdBygrpID, Obj);
            ////.println("finalQuery\t"+finalQuery);
            PbReturnObject object = execSelectSQL(finalQuery);

            conectionID = object.getFieldValueString(0, 0);
//        ConnectionMetadata conMetaData = this.getConnectionMetadata(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception:", ex);

        }

        return getConnectionByConId(conectionID);
    }

    public String addColumnToTable(String tableId, String tableName) {
        String resultString = "";
        Connection connection = null;
        String finalQuery = "";
        Object[] object = new Object[1];
        String connType = "";
        String getColumnnameFromCustDB = resBundle.getString("getColumnnameFromCustDB");
        String getColumnnameFromOrgDB = resBundle.getString("getColumnnameFromOrgDB");
        try {

            GetConnectionType gettypeofconn = new GetConnectionType();
            connType = gettypeofconn.getConnTypeByTableId(tableId);
//              if (connType.equalsIgnoreCase("PostgreSQL")){
//              getColumnnameFromCustDB=resBundle.getString("getColumnnameFromCustDBportgres");
//              }
//              else if(connType.equalsIgnoreCase("Mysql"))
//              {
//                  getColumnnameFromCustDB="SELECT COLUMN_NAME ,* FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME= '&'";
//              }
//              else
//              {
//               getColumnnameFromCustDB="SELECT COLUMN_NAME ,* FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='&' ";
//              }
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                getColumnnameFromCustDB = "SELECT  COLUMN_NAME ,DATA_TYPE,CHARACTER_MAXIMUM_LENGTH FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='&' ";
            }
            object[0] = tableName;
            finalQuery = super.buildQuery(getColumnnameFromCustDB, object);
            //
            PbReturnObject retObjOrgDB = new PbReturnObject();
            ArrayList columnCustDbList = new ArrayList();
            ArrayList datalengthList = new ArrayList();
            ArrayList datatypeList = new ArrayList();
            ArrayList columnNamesFromOrgDB = new ArrayList();
            if (connType.equalsIgnoreCase("Mysql")) {
                connection = ProgenConnection.getInstance().getConnectionByTable(tableId);
                DatabaseMetaData dbm = connection.getMetaData();
                String[] types = {"TABLE"};
                ResultSet rs = dbm.getTables(null, null, "%", types);
                ResultSet rsmd = dbm.getColumns(null, null, tableName, "%");


//            PbReturnObject retObjCusDb = super.execSelectSQL(finalQuery, connection);
//              PreparedStatement ps = connection.prepareStatement(finalQuery);
//              ResultSet rs = ps.executeQuery();
//              ResultSetMetaData rsmd = rs.getMetaData();
//              int numColumns = rsmd.getColumnCount();
                while (rsmd.next()) {
                    columnCustDbList.add("\"" + rsmd.getString("COLUMN_NAME") + "\"");
                    datatypeList.add("\"" + rsmd.getString("TYPE_NAME") + "\"");
                    datalengthList.add("\"" + rsmd.getString("COLUMN_SIZE") + "\"");
                }


//                ps.close();
//            ps = null;
                rs.close();

                rs = null;
                connection.close();
                connection = null;
                connection = null;
                object[0] = tableId;
                finalQuery = super.buildQuery(getColumnnameFromOrgDB, object);

                retObjOrgDB = super.execSelectSQL(finalQuery);
            } else {
                connection = ProgenConnection.getInstance().getConnectionByTable(tableId);

                PbReturnObject retObjCusDb = super.execSelectSQL(finalQuery, connection);




                for (int i = 0; i < retObjCusDb.getRowCount(); i++) {

//              if (connType.equalsIgnoreCase("PostgreSQL")){
//              columnCustDbList.add("\""+retObjCusDb.getFieldValueString(i, "column_name")+"\"");
//                datalengthList.add("\""+retObjCusDb.getFieldValueString(i, "numeric_precision")+"\"");
//                datatypeList.add("\""+retObjCusDb.getFieldValueString(i, "data_type")+"\"");
//              }else{
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        columnCustDbList.add("\"" + retObjCusDb.getFieldValueString(i, "COLUMN_NAME") + "\"");
                        datalengthList.add("\"" + retObjCusDb.getFieldValueString(i, "CHARACTER_MAXIMUM_LENGTH") + "\"");
                        datatypeList.add("\"" + retObjCusDb.getFieldValueString(i, "DATA_TYPE") + "\"");
                    } else {
                        columnCustDbList.add("\"" + retObjCusDb.getFieldValueString(i, "COLUMN_NAME") + "\"");
                        datalengthList.add("\"" + retObjCusDb.getFieldValueString(i, "DATA_LENGTH") + "\"");
                        datatypeList.add("\"" + retObjCusDb.getFieldValueString(i, "DATA_TYPE") + "\"");
                    }


                }
            }

            connection = null;
            object[0] = tableId;
            finalQuery = super.buildQuery(getColumnnameFromOrgDB, object);

            retObjOrgDB = super.execSelectSQL(finalQuery);
            // }
            for (int j = 0; j < retObjOrgDB.getRowCount(); j++) {
                columnNamesFromOrgDB.add("\"" + retObjOrgDB.getFieldValueString(j, "TABLE_COL_NAME") + "\"");
            }
            for (int k = 0; k < columnCustDbList.size(); k++) {
                if (columnNamesFromOrgDB.contains(columnCustDbList.get(k))) {

                    columnCustDbList.remove(k);
                    datalengthList.remove(k);
                    datatypeList.remove(k);
                    k--;

                }
            }

            // columnCustDbList.removeAll(columnNamesFromOrgDB);
            //
            // 
            resultString += "{custDbColumns:" + columnCustDbList + ",custDatatype:" + datatypeList + ",custdataLength:" + datalengthList + ",columnsNamesOrgDb:" + columnNamesFromOrgDB + "}";
            //

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    logger.error("Exception:", ex);
                }
            }
        }
        return resultString;
    }

    public boolean deleteTable(String tableId) {
        boolean retunBoolean = false;
        String queryString = resBundle.getString("checkinBussTable");
        String deleteDbMaster = resBundle.getString("deleteDbMaster");
        String deleteDbMasterTabDetails = resBundle.getString("deleteDbMasterTabDetails");
        String deleteDbTabRelation = resBundle.getString("deleteDbTabRelation");
        String deleteDbTabRelationDetails = resBundle.getString("deleteDbTabRelationDetails");
        Object object[] = null;
        PbReturnObject pbReturnObject = new PbReturnObject();
        String finalquery = null;
        ArrayList queryList = new ArrayList();
        try {
            object = new Object[1];
            object[0] = tableId;
            finalquery = super.buildQuery(queryString, object);
            pbReturnObject = super.execSelectSQL(finalquery);
            if (pbReturnObject.getRowCount() == 0) {
                finalquery = super.buildQuery(deleteDbMaster, object);
                queryList.add(finalquery);
                finalquery = super.buildQuery(deleteDbMasterTabDetails, object);
                queryList.add(finalquery);
                finalquery = super.buildQuery(deleteDbTabRelation, object);
                queryList.add(finalquery);
                finalquery = super.buildQuery(deleteDbTabRelationDetails, object);
                queryList.add(finalquery);
                retunBoolean = super.executeMultiple(queryList);
            } else {
                retunBoolean = false;
            }


        } catch (Exception e) {
            retunBoolean = false;
            logger.error("Exception:", e);
        }

        return retunBoolean;
    }
    //by sunita

    public String getRelatedInfo(String grpId, String tabId) {
        String jsonString = null;
        Gson json = new Gson();
        PbReturnObject all = new PbReturnObject();

        String getRelatedInfo = resBundle.getString("getRelatedInfo");
        Object dbTabObj[] = new Object[8];
        dbTabObj[0] = grpId;
        dbTabObj[1] = grpId;
        dbTabObj[2] = tabId;
        dbTabObj[3] = tabId;
        dbTabObj[4] = grpId;
        dbTabObj[5] = grpId;
        dbTabObj[6] = tabId;
        dbTabObj[7] = tabId;
        String fingetRelatedInfo = buildQuery(getRelatedInfo, dbTabObj);

        try {
            PbReturnObject retObj = execSelectSQL(fingetRelatedInfo);

            Map segmentationmap = new HashMap();

            List<String> relationshipid = new ArrayList<String>();
            List<String> bussrelationid = new ArrayList<String>();
            List<String> dbTableName = new ArrayList<String>();
            List<String> tableid = new ArrayList<String>();
            List<String> tableid2 = new ArrayList<String>();
            List<String> actualclause = new ArrayList<String>();
            List<String> relationship = new ArrayList<String>();
            List<String> equal = new ArrayList<String>();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                relationshipid.add(retObj.getFieldValueString(i, 0));
                bussrelationid.add(retObj.getFieldValueString(i, 1));
                dbTableName.add(retObj.getFieldValueString(i, 2));
                tableid.add(retObj.getFieldValueString(i, 3));

                tableid2.add(retObj.getFieldValueString(i, 4));
                actualclause.add(retObj.getFieldValueString(i, 5));
                relationship.add(retObj.getFieldValueString(i, 6));
                equal.add(retObj.getFieldValueString(i, 7));
            }
            segmentationmap.put("relationshipid", relationshipid);
            segmentationmap.put("bussrelationid", bussrelationid);
            segmentationmap.put("dbTableName", dbTableName);
            segmentationmap.put("tableid", tableid);
            segmentationmap.put("tableid2", tableid2);
            segmentationmap.put("actualclause", actualclause);
            segmentationmap.put("relationship", relationship);
            segmentationmap.put("equal", equal);

            jsonString = json.toJson(segmentationmap);

        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return jsonString;
    }

    public int updateBussTable(String bussrltId, String actualClause) throws Exception {



        String getRelatedInfo = resBundle.getString("updatebussTable");
        Object dbTabObj[] = new Object[2];
        dbTabObj[0] = actualClause;
        dbTabObj[1] = bussrltId;

        String fingetRelatedInfo = buildQuery(getRelatedInfo, dbTabObj);
        //
        return execUpdateSQL(fingetRelatedInfo);

    }

    public int InsertBussTable(String rltId, String grpid) throws Exception {


        String getRelatedInfo = "";

        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            getRelatedInfo = resBundle.getString("insertbussTableForSql");
        } else {
            getRelatedInfo = resBundle.getString("insertbussTableForOracle");
        }
        Object dbTabObj[] = new Object[3];
        dbTabObj[0] = grpid;
        dbTabObj[1] = grpid;
        dbTabObj[2] = rltId;

        String fingetRelatedInfo = buildQuery(getRelatedInfo, dbTabObj);
        // 
        return execUpdateSQL(fingetRelatedInfo);

    }

    public boolean deleteNetWorkConnection(String connId) {
        String checkInBussMaster = resBundle.getString("checkInBussMaster");
        String deleteUserConnection = resBundle.getString("deleteUserConnection");
        String deletedbMaster = resBundle.getString("deletedbMastforCon");
        String deletedbMastDetails = resBundle.getString("deletedbMastDetails");
        String finalQuery = "";
        PbReturnObject resultObject = new PbReturnObject();
        Object[] objects = null;
        ArrayList queryList = new ArrayList();
        boolean checkBussMaster = true;
        try {

            objects = new Object[1];
            objects[0] = connId;
            finalQuery = super.buildQuery(checkInBussMaster, objects);
            resultObject = super.execSelectSQL(finalQuery);
            if (resultObject.getRowCount() == 0) {
                checkBussMaster = false;
            }
            if (!checkBussMaster) {
                finalQuery = super.buildQuery(deleteUserConnection, objects);
                queryList.add(finalQuery);
                finalQuery = super.buildQuery(deletedbMastDetails, objects);
                queryList.add(finalQuery);
                finalQuery = super.buildQuery(deletedbMaster, objects);
                queryList.add(finalQuery);
                checkBussMaster = super.executeMultiple(queryList);
            } else {
                checkBussMaster = false;
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return checkBussMaster;
    }
}
