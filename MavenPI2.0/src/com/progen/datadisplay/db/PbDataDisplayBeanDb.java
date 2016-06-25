/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.datadisplay.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 * @filename PbDataDisplayBeanDb
 *
 * @author santhosh.kumar@progenbusiness.com @date Aug 17, 2009, 6:46:51 PM
 */
public class PbDataDisplayBeanDb extends PbDb {

    public static Logger logger = Logger.getLogger(PbDataDisplayBeanDb.class);
    PbDataDisplayResourceBundle resBundle = new PbDataDisplayResourceBundle();

    public ArrayList viewTable(String tableIds) {
        PbReturnObject retObj = null;
        Connection connection = null;

        ArrayList alist = new ArrayList();

        String getDataDisplayColumnsQuery = resBundle.getString("getDataDisplayColumns");
        String getDataDisplayQuery = resBundle.getString("getDataDisplay");
        String getRelatedTablesQuery = resBundle.getString("getRelatedTables");
        String finalQuery = "";
        Object[] Obj = null;
        String colNames = "";
        String tableNames = "";
        String temp = null;

        String[] tableIdArray = null;
        String[] queryTableColumns = null;
        try {


            tableIdArray = tableIds.split(",");

            Obj = new Object[1];
            Obj[0] = tableIds;
//            if(ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)){
            String caseStr = " case ";
            for (int j = 0; j < tableIdArray.length; j++) {
                caseStr += " when pmtd.table_id =" + tableIdArray[j] + " then " + (j + 1);
            }
            caseStr += " else 10000 end ";
            getDataDisplayColumnsQuery = getDataDisplayColumnsQuery + caseStr;
//            }
//            for (int j = 0; j < tableIdArray.length; j++) {
//                getDataDisplayColumnsQuery = getDataDisplayColumnsQuery + "," + tableIdArray[j] + "," + (j + 1);
//            }
//            getDataDisplayColumnsQuery = getDataDisplayColumnsQuery + " )";


            finalQuery = buildQuery(getDataDisplayColumnsQuery, Obj);

            retObj = super.execSelectSQL(finalQuery);

//            retObj = new PbReturnObject(resultSet);
            queryTableColumns = retObj.getColumnNames();
            for (int index = 0; index < retObj.getRowCount(); index++) {
                colNames = colNames + "," + retObj.getFieldValueString(index, queryTableColumns[1]);

                if (temp == null) {
                    tableNames = tableNames + "," + retObj.getFieldValueString(index, queryTableColumns[0]);
                    temp = retObj.getFieldValueString(index, queryTableColumns[0]);
                } else {
                    if ((retObj.getFieldValueString(index, queryTableColumns[0]).equalsIgnoreCase(temp))) {
                    } else {
                        temp = null;
                        tableNames = tableNames + "," + retObj.getFieldValueString(index, queryTableColumns[0]);
                    }
                }
            }
            if (!(colNames.equalsIgnoreCase(""))) {
                colNames = colNames.substring(1);
            }
            if (!(tableNames.equalsIgnoreCase(""))) {
                tableNames = tableNames.substring(1);
            }
            //for getting related tables from PRG_DB_TABLE_RLT_MASTER
            Obj = new Object[2];
            Obj[0] = tableIds;
            Obj[1] = tableIds;

            finalQuery = buildQuery(getRelatedTablesQuery, Obj);


            retObj = super.execSelectSQL(finalQuery);

            //retObj.writeString();





            alist.add(retObj);//adding retaed tables
            if (!(colNames.equalsIgnoreCase(""))) {
                Obj = new Object[2];
                Obj[0] = colNames;
                Obj[1] = tableNames;
                finalQuery = buildQuery(getDataDisplayQuery, Obj);

                //connection adding start here added by bharathi reddy 0n 27-08-09

                connection = ProgenConnection.getInstance().getConnectionByTable(tableIds);

//            dynamicPreparedStatement = dynamicConnection.prepareStatement(finalQuery);
                retObj = super.execSelectSQL(finalQuery, connection);
            }
            connection = null;
            //retObj.writeString();
            //end here

            // retObj = execSelectSQL(finalQuery);

            alist.add(retObj);//adding related tables data


            //return retObj;
            return alist;
        } catch (Exception exception) {

            logger.error("Exception:", exception);

            return alist;
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }



            } catch (Exception innerException) {
                logger.error("Exception:", innerException);

            }
        }
    }

    public ArrayList viewTable2(String actualtableid, String tableIds) {
        PbReturnObject retObj = null;
        Connection connection = null;
        ProgenConnection prgMetadataCon = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        ArrayList alist = new ArrayList();

        String getDataDisplayColumnsQuery = resBundle.getString("getDataDisplayColumns");
        String getDataDisplayQuery = resBundle.getString("getDataDisplay1");
        String getRelatedTablesQuery = resBundle.getString("getRelatedTables");
        String buildTableCondsQuery = resBundle.getString("buildTableConds");
        String finalQuery = "";
        Object[] Obj = null;
        String colNames = "";
        String tableNames = "";
        String temp = null;

        String[] tableIdArray = null;
        String[] queryTableColumns = null;
        try {
//            prgMetadataCon = new ProgenConnection();
            connection = ProgenConnection.getInstance().getConnection();

            tableIds = actualtableid + "," + tableIds;
            tableIdArray = tableIds.split(",");

            Obj = new Object[1];
            Obj[0] = tableIds;

            for (int j = 0; j < tableIdArray.length; j++) {
                getDataDisplayColumnsQuery = getDataDisplayColumnsQuery + "," + tableIdArray[j] + "," + (j + 1);
            }
            getDataDisplayColumnsQuery = getDataDisplayColumnsQuery + " )";

            finalQuery = buildQuery(getDataDisplayColumnsQuery, Obj);

            preparedStatement = connection.prepareStatement(finalQuery);
            resultSet = preparedStatement.executeQuery();

            retObj = new PbReturnObject(resultSet);
            queryTableColumns = retObj.getColumnNames();
            for (int index = 0; index < retObj.getRowCount(); index++) {
                colNames = colNames + ", " + retObj.getFieldValueString(index, queryTableColumns[0]) + "." + retObj.getFieldValueString(index, queryTableColumns[1]);

                if (temp == null) {
                    tableNames = tableNames + "," + retObj.getFieldValueString(index, queryTableColumns[0]);
                    temp = retObj.getFieldValueString(index, queryTableColumns[0]);

                } else {
                    if ((retObj.getFieldValueString(index, queryTableColumns[0]).equalsIgnoreCase(temp))) {
                    } else {
                        temp = retObj.getFieldValueString(index, queryTableColumns[0]);
                        tableNames = tableNames + "," + retObj.getFieldValueString(index, queryTableColumns[0]);
                    }
                }
            }
            colNames = colNames.substring(1);

            if (!(tableNames.equalsIgnoreCase(""))) {
                tableNames = tableNames.substring(1);
            }
            //for getting related tables from PRG_DB_TABLE_RLT_MASTER
            Obj = new Object[2];
            Obj[0] = tableIdArray[0];
            Obj[1] = tableIdArray[0];

            finalQuery = buildQuery(getRelatedTablesQuery, Obj);

            preparedStatement = connection.prepareStatement(finalQuery);
            resultSet = preparedStatement.executeQuery();
            retObj = new PbReturnObject(resultSet);

            alist.add(retObj);//adding retaed tables


            for (int i = 1; i < tableIdArray.length; i++) {
                if (i != (tableIdArray.length - 1)) {
                    buildTableCondsQuery = buildTableCondsQuery + " ( ( TABLE_ID = " + tableIdArray[i] + " and table_id2=" + tableIdArray[0] + " ) or  ( TABLE_ID = " + tableIdArray[0] + " and table_id2=" + tableIdArray[i] + " ) ) or ";
                } else {
                    buildTableCondsQuery = buildTableCondsQuery + " ( ( TABLE_ID = " + tableIdArray[i] + " and table_id2=" + tableIdArray[0] + " ) or  ( TABLE_ID = " + tableIdArray[0] + " and table_id2=" + tableIdArray[i] + " ) )";
                }
            }


            preparedStatement = connection.prepareStatement(buildTableCondsQuery);
            resultSet = preparedStatement.executeQuery();
            retObj = new PbReturnObject(resultSet);
            // retObj = execSelectSQL(buildTableCondsQuery);


            String sqlwhere = "";//for sql
            for (int i = 0; i < retObj.getRowCount(); i++) {
                if (i != (retObj.getRowCount() - 1)) {
                    getDataDisplayQuery = getDataDisplayQuery + retObj.getFieldValueString(i, 0) + " and ";
                    sqlwhere += retObj.getFieldValueString(i, 0);//for sql
                } else {
                    getDataDisplayQuery = getDataDisplayQuery + retObj.getFieldValueString(i, 0);
                    sqlwhere += retObj.getFieldValueString(i, 0);//for sql
                }
            }
            // getDataDisplayQuery += " and rownum<200";

            Obj = new Object[2];
            Obj[0] = colNames;
            Obj[1] = tableNames;
            //   finalQuery = buildQuery(getDataDisplayQuery, Obj);
            // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("final query------------->"+finalQuery);
            //newly added by bharathi reddy on 27-08-09
            connection = getConnection(actualtableid);

            String dbType = getConnectionDbType(actualtableid);
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-----------dbType-----------------"+dbType);
            if (dbType.equalsIgnoreCase("ORACLE")) {
                finalQuery = buildQuery(getDataDisplayQuery, Obj);
                finalQuery += " and  rownum<200 ";
            } else if (dbType.equalsIgnoreCase("SQL")) {
                String getDataDisplaySQL = resBundle.getString("getDataDisplaySQL");
                finalQuery = buildQuery(getDataDisplaySQL, Obj);
                finalQuery += " " + sqlwhere;

            } else if (dbType.equalsIgnoreCase("mysql")) {
                finalQuery = buildQuery(getDataDisplayQuery, Obj);
                finalQuery += "   LIMIT 200 ";
            }
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("finalQuery is - " + finalQuery);
            preparedStatement = connection.prepareStatement(finalQuery);
            resultSet = preparedStatement.executeQuery();
            retObj = new PbReturnObject(resultSet);

            //retObj = execSelectSQL(finalQuery);

            alist.add(retObj);//adding related tables data


            //return retObj;
            return alist;
        } catch (Exception exception) {

            logger.error("Exception:", exception);

            return alist;
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception innerException) {
                logger.error("Exception:", innerException);

            }
        }
    }

    public Connection getConnection(String tableId) {
        Connection connection = null;
        try {
            connection = ProgenConnection.getInstance().getConnectionByTable(tableId);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return connection;
    }

    public Connection getSqlServerConnection(String tableId) {
        Connection connection = null;
        PbReturnObject retObj = null;
        String getDataDisplayColumnsQuery = resBundle.getString("getConnectionId");
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
        Obj[0] = tableId;

        String[] tableColumnNames = null;

        try {
            finalQuery = buildQuery(getDataDisplayColumnsQuery, Obj);
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("query in connection===" + finalQuery);
            retObj = execSelectSQL(finalQuery);
            tableColumnNames = retObj.getColumnNames();

            // serverName = retObj.getFieldValueString(0,tableColumnNames[4]);
            serverPort = retObj.getFieldValueString(0, tableColumnNames[5]);
            serviceId = retObj.getFieldValueString(0, tableColumnNames[3]);
            userName = retObj.getFieldValueString(0, tableColumnNames[0]);
            password = retObj.getFieldValueString(0, tableColumnNames[1]);
            server = retObj.getFieldValueString(0, tableColumnNames[2]);

            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            connection = DriverManager.getConnection("jdbc:odbc:SQL_SERVER", "", "");

            return connection;
        } catch (Exception exp) {
            logger.error("Exception:", exp);
            return connection;
        }
    }

    public String getConnectionDbType(String tableId) {
        Connection connection = null;
        PbReturnObject retObj = null;
        String getDataDisplayColumnsQuery = resBundle.getString("getConnectionId");
        String finalQuery = "";

        String serverName = "";
        String dsnName = "";
        String connType = "";
        String serverPort = "";
        String serviceId = "";
        String userName = "";
        String password = "";
        String server = "";
        String dbType = "";
        String dbName = "";
        Object[] Obj = new Object[1];
        Obj[0] = tableId;

        String[] tableColumnNames = null;

        try {
            finalQuery = buildQuery(getDataDisplayColumnsQuery, Obj);
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("query in connection===" + finalQuery);
            retObj = execSelectSQL(finalQuery);
            tableColumnNames = retObj.getColumnNames();

            // serverName = retObj.getFieldValueString(0,tableColumnNames[4]);
            serverPort = retObj.getFieldValueString(0, tableColumnNames[5]);
            serviceId = retObj.getFieldValueString(0, tableColumnNames[3]);
            userName = retObj.getFieldValueString(0, tableColumnNames[0]);
            password = retObj.getFieldValueString(0, tableColumnNames[1]);
            server = retObj.getFieldValueString(0, tableColumnNames[2]);
            dbType = retObj.getFieldValueString(0, tableColumnNames[7]);
            dbName = retObj.getFieldValueString(0, tableColumnNames[8]);
            /*
             * if (dbType != null && dbType.equalsIgnoreCase("oracle")) {
             * Class.forName("oracle.jdbc.driver.OracleDriver"); connection =
             * DriverManager.getConnection("jdbc:oracle:thin:@" + server + ":" +
             * serverPort + ":" + serviceId + "", userName, password); } else if
             * (dbType != null && dbType.equalsIgnoreCase("mysql")) {
             * Class.forName("com.mysql.jdbc.Driver");
             * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("jdbc:mysql://"
             * + server + ":" + serverPort + "/" + dbName + "," + userName + ","
             * + password); connection =
             * DriverManager.getConnection("jdbc:mysql://" + server + ":" +
             * serverPort + "/" + dbName, userName, password); } else if (dbType
             * != null && dbType.equalsIgnoreCase("SQL")) {
             * Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); connection =
             * DriverManager.getConnection("jdbc:odbc:SQL_SERVER","","");
            }
             */
            return dbType;
        } catch (Exception exp) {
            logger.error("Exception:", exp);
            return dbType;
        }
    }

    public static void main(String[] args) {
        PbDataDisplayBeanDb db = new PbDataDisplayBeanDb();


    }

    //added by sreekanth
    public PbReturnObject getNetworkConnDetails(String connectionId) {

        PbReturnObject retObj = null;
        String getNetworkConnectionDetails = resBundle.getString("getNetworkConnectionDetails");
        String finalQuery = "";

        Object[] Obj = new Object[1];
        Obj[0] = connectionId;

        try {
            finalQuery = buildQuery(getNetworkConnectionDetails, Obj);
            //////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("query in connection===" + finalQuery);
            retObj = execSelectSQL(finalQuery);
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
        return retObj;
    }
}
