package com.progen.contypes;

import java.sql.Connection;
import java.sql.DriverManager;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;

public class GetConnectionType {

    public static Logger logger = Logger.getLogger(GetConnectionType.class);
    Connection connection = null;
    PbReturnObject retObj = new PbReturnObject();
    PbDb pbdb = new PbDb();

    public String getConTypeByElementId(String elementId) {

        String connType = null;
        try {
            String finalQuery = "select DISTINCT a.USER_NAME,  a.PASSWORD,  a.SERVER,  a.SERVICE_ID,  a.SERVICE_NAME,  a.PORT, a.DSN_NAME, a.DB_CONNECTION_TYPE  "
                    + "from PRG_USER_CONNECTIONS a,PRG_USER_ALL_INFO_DETAILS b  where a.connection_id=b.connection_id and b.element_id= " + elementId + " ";

            retObj = pbdb.execSelectSQL(finalQuery);
            connType = retObj.getFieldValueString(0, "DB_CONNECTION_TYPE");
            //////////.println("connType is" + connType);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
            //////////.println("Exception caught in getcontypebyelementid");
        }
        return connType;
    }

    public String getConTypeByConnID(String connID) {

        String connType = null;
        try {
            String finalQuery = "select DB_CONNECTION_TYPE from PRG_USER_CONNECTIONS  where connection_id=" + connID;

            retObj = pbdb.execSelectSQL(finalQuery);
            connType = retObj.getFieldValueString(0, "DB_CONNECTION_TYPE");
            //////////.println("connType is" + connType);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
            //////////.println("Exception caught in getcontypebyelementid");
        }
        return connType;
    }

    public Connection getConnectionByTableId(String tableId) {

        String getDataDisplayColumnsQuery = "SELECT  PUC.USER_NAME, PUC.PASSWORD, PUC.SERVER, PUC.SERVICE_ID, PUC.SERVICE_NAME, PUC.PORT,PUC.DSN_NAME, PUC.DB_CONNECTION_TYPE  FROM PRG_USER_CONNECTIONS PUC,prg_db_master_table PDMT WHERE PDMT.connection_id=PUC.connection_id AND pdmt.table_id=&";
        String finalQuery = "";
        String serverPort = "";
        String serviceId = "";
        String userName = "";
        String password = "";
        String server = "";

        Object[] Obj = new Object[1];
        Obj[0] = tableId;

        String[] tableColumnNames = null;

        try {
            finalQuery = pbdb.buildQuery(getDataDisplayColumnsQuery, Obj);
            retObj = pbdb.execSelectSQL(finalQuery);
            tableColumnNames = retObj.getColumnNames();
            serverPort = retObj.getFieldValueString(0, tableColumnNames[5]);
            serviceId = retObj.getFieldValueString(0, tableColumnNames[3]);
            userName = retObj.getFieldValueString(0, tableColumnNames[0]);
            password = retObj.getFieldValueString(0, tableColumnNames[1]);
            server = retObj.getFieldValueString(0, tableColumnNames[2]);

            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@" + server + ":" + serverPort + ":" + serviceId + "", userName, password);
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
        return connection;
    }

    public String getConnTypeByTableId(String tableId) throws Exception {
        String dbType = null;
        String query = "select db_connection_type FROM prg_user_connections where connection_id in (select connection_id from prg_db_master_table where table_id =" + tableId + ")";
        retObj = pbdb.execSelectSQL(query);
        dbType = retObj.getFieldValueString(0, 0);
        //////////.println("Type of Database is"+dbType);
        return dbType;
    }

    public String getConDbTypeByBussTableId(String bussTableIds) {
        String getDataDisplayColumnsQuery = "select puc.user_name,puc.password,puc.server,puc.service_id,puc.service_name,puc.port,puc.dsn_name,puc.db_connection_type,puc.dbname FROM PRG_USER_CONNECTIONS puc,"
                + " prg_db_master_table pdmt, prg_grp_buss_table pgbt, prg_grp_buss_table_src pgbts where puc.connection_id= pdmt.connection_id and "
                + "pgbt.buss_table_id= pgbts.buss_table_id and pgbts.db_table_id=pdmt.table_id and pgbt.buss_table_id='&'";
        String finalQuery = "";
        String dbType = "";
        Object[] Obj = new Object[1];
        Obj[0] = bussTableIds;
        String[] tableColumnNames = null;
        try {
            finalQuery = pbdb.buildQuery(getDataDisplayColumnsQuery, Obj);
            retObj = pbdb.execSelectSQL(finalQuery);
            tableColumnNames = retObj.getColumnNames();
            dbType = retObj.getFieldValueString(0, tableColumnNames[7]);
            return dbType;
        } catch (Exception exp) {
            logger.error("Exception:", exp);
            return dbType;
        }
    }
}
