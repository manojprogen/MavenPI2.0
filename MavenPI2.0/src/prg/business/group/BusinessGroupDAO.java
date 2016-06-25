package prg.business.group;

import com.progen.querylayer.Table;
import java.sql.*;
import java.util.*;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author Administrator
 */
public class BusinessGroupDAO extends PbDb {

    public static Logger logger = Logger.getLogger(BusinessGroupDAO.class);
    private boolean isDebugEnable = false;
    Connection con = null;
    PreparedStatement pstmt = null;
    PreparedStatement secPstmt = null;
    PreparedStatement thirdPstmt = null;
    Statement stmt = null;
    Statement stmt_1 = null;
    Statement stmt_2 = null;
    ResultSet rs = null;
    ResultSet rs_1 = null;
    ResultSet rs_2 = null;
    int memIndex = 1;
    int dimIndex = 1;
    public boolean isummSupport = false;
    public HashMap altFact = new HashMap();
    ProgenConnection progenConnection = null;
    public boolean istrendSupport = false;
    public boolean isCustomBucket = false;
    public String customBucketTablesql = "";
    public ArrayList FinalBussTableIdRlt = new ArrayList();
//    PbBussGrpResourceBundle resourceBundle = new PbBussGrpResourceBundle();
    static HttpSession session = null;

    public static HttpSession getSession() {
        return session;
    }

    public static void setSession(HttpSession Session) {
        session = Session;
    }
    ResourceBundle resourceBundle;

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new PbBussGrpResBundleSqlServer();
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                resourceBundle = new PbBussGrpResBundleMysql();
            } else {
                resourceBundle = new PbBussGrpResourceBundle();
            }
        }

        return resourceBundle;
    }

    public int addBusinessGroup(String grpName, String grpDesc, String connId) {
        int bussGrpId = 0;
        String[] tableColNames = null;
        String getBussGrpId = getResourceBundle().getString("getBussGrpId");
        String createBussGrp = getResourceBundle().getString("createBussGrp");
        PbReturnObject retObj = new PbReturnObject();
        try {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                Object[] obj = new Object[3];
                obj[0] = grpDesc;
                obj[1] = grpName;
                obj[2] = connId;
                createBussGrp = buildQuery(createBussGrp, obj);
                bussGrpId = 1;

            } else {
                if (isDebugEnable) {
                    logger.info("query fired inside try block");
                }
                retObj = execSelectSQL(getBussGrpId);
                tableColNames = retObj.getColumnNames();
                bussGrpId = retObj.getFieldValueInt(0, tableColNames[0]);
                Object[] obj = new Object[4];
                obj[0] = bussGrpId;
                obj[1] = grpDesc;
                obj[2] = grpName;
                obj[3] = connId;
                createBussGrp = buildQuery(createBussGrp, obj);

            }
            bussGrpId = 1;
            if (isDebugEnable) {
                logger.info("query fired");
            }
            execModifySQL(createBussGrp);

        } catch (Exception e) {
            logger.error("Exception:", e);

        }
        return bussGrpId;
    }

    public String getRelatedTables(String tableId) {
        // tableId="3";
        StringBuffer relTab = new StringBuffer();
        int i = 0;

        try {
            con = ProgenConnection.getInstance().getConnection();
            StringBuffer sql = new StringBuffer();
            sql.append("select mstr.table_id,mstr.table_name, det.column_id,det.table_col_name,det.table_col_cust_name from ");
            sql.append(" prg_db_master_table_details det, prg_db_master_table mstr ");
            sql.append(" where det.is_active='Y' AND mstr.table_id =det.table_id and  det.table_id in ( ");
            sql.append(" select  table_id2 as reltable from prg_db_table_rlt_master where (table_id=" + tableId + ") ");
            sql.append(" union select table_id as reltable  from prg_db_table_rlt_master ");
            sql.append(" where  (table_id2=" + tableId + ")) order by mstr.table_id,det.table_col_name");
            stmt = con.createStatement();
            if (isDebugEnable) {
                logger.info("query fired inside try block");
            }
            rs = stmt.executeQuery(sql.toString());
            String tabName = "";
            relTab.append("<root>");
            while (rs.next()) {
                i = 1;
                if (tabName.equalsIgnoreCase("")) {
                    relTab.append("<relatedTable>");
                    relTab.append("<table>" + rs.getString("TABLE_NAME") + "</table>");
                    relTab.append("<table-id>" + rs.getString("TABLE_ID") + "</table-id>");
                    tabName = rs.getString("TABLE_NAME");
                } else {
                    if (!(tabName.equalsIgnoreCase(rs.getString("TABLE_NAME")))) {
                        relTab.append("</relatedTable>");
                        relTab.append("<relatedTable>");
                        relTab.append("<table>" + rs.getString("TABLE_NAME") + "</table>");
                        relTab.append("<table-id>" + rs.getString("TABLE_ID") + "</table-id>");

                        tabName = rs.getString("TABLE_NAME");
                    }
                }
                relTab.append("<column-id>" + rs.getString("COLUMN_ID") + "</column-id>");
                relTab.append("<column-name>" + rs.getString("TABLE_COL_NAME") + "</column-name>");

            }
            rs.close();
            stmt.close();
            con.close();
            relTab.append("</relatedTable>");
            relTab.append("</root>");
        } catch (SQLException e) {
            logger.error("Exception:", e);
        }
        String xy = relTab.toString();
        if (i == 0) {
            xy = "noData";
        }

        return xy;
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

    public ArrayList viewBussData(String bussTableId) {
        ArrayList alist = new ArrayList();
        PbReturnObject retObj = null;
        Object[] Obj = null;
        String getALlLayerTableNamesQuery = getResourceBundle().getString("getALlLayerTableNames");
        String getALlLayerColNamesQuery = getResourceBundle().getString("getALlLayerColNames");

        String srcTabName = "";
        String bussTabName = "";
        String dbTabName = "";

        // String srcColName = "";
        //String bussColName = "";
        //String dbColName = "";
        String[] colNames = null;

        //String sqlStr = "";
        String finalQuery = "";

        Connection connection = null;
        try {
            Obj = new Object[1];
            Obj[0] = bussTableId;
            finalQuery = buildQuery(getALlLayerTableNamesQuery, Obj);
            if (isDebugEnable) //                    ProgenLog.log(ProgenLog.FINE, this, "viewBussData","query fired inside try block");
            {
                logger.info("query fired inside try block");
            }
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();

            if (retObj != null && retObj.getRowCount() != 0) {
                bussTabName = retObj.getFieldValueString(0, colNames[0]);
                srcTabName = retObj.getFieldValueString(0, colNames[1]);
                dbTabName = retObj.getFieldValueString(0, colNames[2]);
            }

            Obj = new Object[1];
            Obj[0] = bussTableId;
            finalQuery = buildQuery(getALlLayerColNamesQuery, Obj);
            if (isDebugEnable) //                    ProgenLog.log(ProgenLog.FINE, this, "viewBussData","query fired");
            {
                logger.info("query fired");
            }
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();

//            String osql = "select ";
//            String insql = "select ";
            StringBuilder osql = new StringBuilder();
            osql.append("select ");
            StringBuilder insql = new StringBuilder();
            insql.append("select ");

            int psize = retObj.getRowCount();
            if (psize > 0) {
                for (int looper = 0; looper < psize; looper++) {
                    // columnNames[looper] = retObj.getFieldValueString(looper,"DISPLAY_NAME");
                    // tableColumnsNames[looper] =retObj.getFieldValueString(looper,"COLUMN_NAME").toUpperCase();
                    if (looper == 0) {
//                        osql += " " + bussTabName + "." + retObj.getFieldValueString(looper, colNames[1]) + "  as  \"" + retObj.getFieldValueString(looper, colNames[3]).toUpperCase() + "\" ";
                        osql.append(" ").append(bussTabName).append(".").append(retObj.getFieldValueString(looper, colNames[1])).append("  as  \"").append(retObj.getFieldValueString(looper, colNames[3]).toUpperCase()).append("\" ");
//                        insql += " " + dbTabName + "." + retObj.getFieldValueString(looper, colNames[2]) + "  as  " + retObj.getFieldValueString(looper, colNames[1]).toUpperCase() + " ";
                        insql.append(" ").append(dbTabName).append(".").append(retObj.getFieldValueString(looper, colNames[2])).append("  as  ").append(retObj.getFieldValueString(looper, colNames[1]).toUpperCase()).append(" ");

                    } else {
//                        osql += " ," + bussTabName + "." + retObj.getFieldValueString(looper, colNames[1]) + "   as \"" + retObj.getFieldValueString(looper, colNames[3]).toUpperCase() + "\" ";
                        osql.append(" ,").append(bussTabName).append(".").append(retObj.getFieldValueString(looper, colNames[1])).append("   as \"").append(retObj.getFieldValueString(looper, colNames[3]).toUpperCase()).append("\" ");
//                        insql += " , " + dbTabName + "." + retObj.getFieldValueString(looper, colNames[2]) + "  as  " + retObj.getFieldValueString(looper, colNames[1]).toUpperCase() + " ";
                        insql.append(" , ").append(dbTabName).append(".").append(retObj.getFieldValueString(looper, colNames[2])).append("  as  ").append(retObj.getFieldValueString(looper, colNames[1]).toUpperCase()).append(" ");

                    }
                }
//                osql += " from ( " + insql + " from " + dbTabName + " ) " + bussTabName + " ";
                osql.append(" from ( ").append(insql).append(" from ").append(dbTabName).append(" ) ").append(bussTabName).append(" ");
            }
            connection = getConnection(bussTableId);
            retObj = execSelectSQL(osql.toString(), connection);

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    logger.error("Exception:", ex);
                }
            }
        }
        return alist;
    }

    public String viewBussDataWithColSingle(String bussTableId) {
        ArrayList alist = new ArrayList();
        PbReturnObject retObj = null;
        Object[] Obj = null;
        String getALlLayerTableNamesQuery = getResourceBundle().getString("getALlLayerTableNames");
        String getALlLayerColNamesQuery = getResourceBundle().getString("getALlLayerColNames");

        String srcTabName = "";
        String bussTabName = "";
        String dbTabName = "";
        String osql = "";
        String insql = "";

        // String srcColName = "";
        //String bussColName = "";
        //String dbColName = "";
        String[] colNames = null;

        //String sqlStr = "";
        String finalQuery = "";

        Connection connection = null;
        try {
            Obj = new Object[1];
            Obj[0] = bussTableId;
            finalQuery = buildQuery(getALlLayerTableNamesQuery, Obj);
            if (isDebugEnable) {
                logger.info("query fired inside try block");
            }
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();

            if (retObj != null && retObj.getRowCount() != 0) {
                bussTabName = retObj.getFieldValueString(0, colNames[0]);
                srcTabName = retObj.getFieldValueString(0, colNames[1]);
                dbTabName = retObj.getFieldValueString(0, colNames[2]);
            }

            Obj = new Object[1];
            Obj[0] = bussTableId;
            finalQuery = buildQuery(getALlLayerColNamesQuery, Obj);
            if (isDebugEnable) {
                logger.info("query fired");
            }
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();

            osql = "select ";
            insql = "select ";

            int psize = retObj.getRowCount();

            ///amit commeted for time being 4oct2010
            if (psize > 0) {
//                for (int looper = 0; looper < psize; looper++) {
//                    // columnNames[looper] = retObj.getFieldValueString(looper,"DISPLAY_NAME");
//                    // tableColumnsNames[looper] =retObj.getFieldValueString(looper,"COLUMN_NAME").toUpperCase();
//                    if (looper == 0) {
//                        //osql += " " + bussTabName + "." + retObj.getFieldValueString(looper, colNames[1]) + "  as  \"" + retObj.getFieldValueString(looper, colNames[3]).toUpperCase() + "\" ";
//                        insql += " " + dbTabName + "." + retObj.getFieldValueString(looper, colNames[2]) + "  as  " + retObj.getFieldValueString(looper, colNames[1]).toUpperCase() + " ";
//
//                    } else {
//                        //osql += " ," + bussTabName + "." + retObj.getFieldValueString(looper, colNames[1]) + "   as \"" + retObj.getFieldValueString(looper, colNames[3]).toUpperCase() + "\" ";
//                        insql += " , " + dbTabName + "." + retObj.getFieldValueString(looper, colNames[2]) + "  as  " + retObj.getFieldValueString(looper, colNames[1]).toUpperCase() + " ";
//
//                    }
//                }
                osql = " from  " + dbTabName + "  " + bussTabName + " ";
            }
            //connection = getConnection(bussTableId);
            //retObj = execSelectSQL(osql, connection);
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    logger.error("Exception:", ex);
                }
            }
        }
        return (osql);
    }

    //Function to Query for multiple business table //
    public void viewBussData(ArrayList bussTableIds) {
        ArrayList alist = new ArrayList();
        PbReturnObject retObj = null;
        Vector bussTableIdRlt = new Vector();
        FinalBussTableIdRlt = new ArrayList();
        Object[] Obj = null;
//        String getALlLayerTableNamesQuery = getResourceBundle().getString("getALlLayerTableNames");
//        String getALlLayerColNamesQuery = getResourceBundle().getString("getALlLayerColNames");

//        String qryTable = null;
        StringBuilder qryTable = new StringBuilder();
        for (int i = 0; i < bussTableIds.size(); i++) {
            if (qryTable == null) {
//                qryTable = bussTableIds.get(i).toString();
//                qryTable = bussTableIds.get(i).toString();
                qryTable.append(bussTableIds.get(i).toString());
                bussTableIdRlt.add(bussTableIds.get(i).toString());
            } else {
//                qryTable += "," + bussTableIds.get(i).toString();
                qryTable.append(",").append(bussTableIds.get(i).toString());
            }
        }

        String sqlstr = "Select BUSS_TABLE_ID , BUSS_TABLE_NAME  , DB_TABLE_NAME , SOURCE_TABLE_NAME  , NO_OF_NODES ";
        sqlstr += " from PRG_GRP_BUSS_TABLE_MASTER_VIEW where BUSS_TABLE_ID in (" + qryTable + ") ";
        String[] srcTabName;
        String[] bussTabName;
        String[] dbTabName;

        // String srcColName = "";
        //String bussColName = "";
        //String dbColName = "";
        String[] colNames = null;

        //String sqlStr = "";
        String finalQuery = "";
//        String node1 = null;
        StringBuilder node1 = new StringBuilder();
        ArrayList node2 = new ArrayList();

        Connection connection = null;
        try {
            Obj = new Object[1];
            Obj[0] = bussTableIds;
            //finalQuery = buildQuery(getALlLayerTableNamesQuery, Obj);
            finalQuery = sqlstr;
            if (isDebugEnable) {
                logger.info("query fired inside try block");
            }
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            int rowCnt = retObj.getRowCount();
            HashMap businessTable = new HashMap();
            HashMap dbTable = new HashMap();

            srcTabName = new String[rowCnt];
            bussTabName = new String[rowCnt];
            dbTabName = new String[rowCnt];
            if (retObj != null && retObj.getRowCount() != 0) {
                for (int i = 0; i < rowCnt; i++) {
                    bussTabName[i] = retObj.getFieldValueString(i, colNames[1]);
                    srcTabName[i] = retObj.getFieldValueString(i, colNames[3]);
                    dbTabName[i] = retObj.getFieldValueString(i, colNames[2]);
                    if (retObj.getFieldValueInt(i, colNames[4]) == 1) {
                        if (i == 0) {
   //                            node1 = retObj.getFieldValueString(i, colNames[0]);
                            node1.append(retObj.getFieldValueString(i, colNames[0]));
                        } else {
//                            node1 += "," + retObj.getFieldValueString(i, colNames[0]);
                            node1.append(",").append(retObj.getFieldValueString(i, colNames[0]));
                        }
                    } else {
                        node2.add(retObj.getFieldValue(0, colNames[0]).toString());
                    }

                    businessTable.put(retObj.getFieldValueString(i, colNames[0]), retObj.getFieldValueString(i, colNames[1]));
                    dbTable.put(retObj.getFieldValueString(i, colNames[0]), retObj.getFieldValueString(i, colNames[3]));
                }
            }

            sqlstr = "Select BUSS_TABLE_ID";
            sqlstr += " , BUSINESS_COL_NAME  ";
            sqlstr += " , BUSINESS_DISP_NAME ";
            sqlstr += " , SCR_COLUMN_NAME  ";
            sqlstr += " , TABLE_COL_NAME  ";
            sqlstr += " , count(TABLE_COL_NAME) over(PARTITION by BUSS_TABLE_ID )  COLCNT ";
            sqlstr += " from PRG_GRP_BUSS_TABLE_DTLS_VIEW where BUSS_TABLE_ID in (" + qryTable + ") ";
            sqlstr += " order by BUSS_TABLE_ID ";

            Obj = new Object[1];
            Obj[0] = bussTableIds;
            //finalQuery = buildQuery(getALlLayerColNamesQuery, Obj);
            finalQuery = sqlstr;
            if (isDebugEnable) //               
            {
                logger.info("query fired");
            }
                retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();

//            String osql = "select ";
            StringBuilder osql = new StringBuilder(600);
            osql.append("select ");
            String[] insql = new String[rowCnt];
            int j = 0;
            int breakat = 0;
            int breakat1 = 0;

            String bussTableL = "";
            String dbTableL = "";

            int psize = retObj.getRowCount();
            if (psize > 0) {
                for (int looper = 0; looper < psize; looper++) {
                    // columnNames[looper] = retObj.getFieldValueString(looper,"DISPLAY_NAME");
                    // tableColumnsNames[looper] =retObj.getFieldValueString(looper,"COLUMN_NAME").toUpperCase();
                    if (looper == 0) {
                        breakat += retObj.getFieldValueInt(looper, colNames[5]);
                        breakat1 = breakat;
                        bussTableL = (String) businessTable.get(retObj.getFieldValue(looper, colNames[0]).toString());
                        dbTableL = (String) dbTable.get(retObj.getFieldValue(looper, colNames[0]).toString());
                        j = 0;
                        insql[j] = " ( Select ";
                    } else if (looper == breakat) {
                        insql[j] += " from  " + dbTableL + " )  " + bussTableL + " ";
                        breakat1 = breakat;
                        breakat += retObj.getFieldValueInt(looper, colNames[5]);
                        bussTableL = (String) businessTable.get(retObj.getFieldValue(looper, colNames[0]).toString());
                        dbTableL = (String) dbTable.get(retObj.getFieldValue(looper, colNames[0]).toString());
                        j++;
                        insql[j] = " , ( Select ";
                    }
                    if (looper == 0) {
//                        osql += " " + bussTableL + "." + retObj.getFieldValueString(looper, colNames[1]) + "  as  \"" + retObj.getFieldValueString(looper, colNames[3]).toUpperCase() + "\" ";
                        osql.append(" ").append(bussTableL).append(".").append(retObj.getFieldValueString(looper, colNames[1])).append("  as  \"" + retObj.getFieldValueString(looper, colNames[3]).toUpperCase()).append("\" ");

                        insql[j] += " " + dbTableL + "." + retObj.getFieldValueString(looper, colNames[2]) + "  as  " + retObj.getFieldValueString(looper, colNames[1]).toUpperCase() + " ";
                    } else if (looper != 0 && looper == breakat1) {
//                        osql += " , " + bussTableL + "." + retObj.getFieldValueString(looper, colNames[1]) + "  as  \"" + retObj.getFieldValueString(looper, colNames[3]).toUpperCase() + "\" ";
                        osql.append(" , ").append(bussTableL).append(".").append(retObj.getFieldValueString(looper, colNames[1])).append("  as  \"").append(retObj.getFieldValueString(looper, colNames[3]).toUpperCase()).append("\" ");
                        insql[j] += " " + dbTableL + "." + retObj.getFieldValueString(looper, colNames[2]) + "  as  " + retObj.getFieldValueString(looper, colNames[1]).toUpperCase() + " ";

                    } else {
//                        osql += " ," + bussTableL + "." + retObj.getFieldValueString(looper, colNames[1]) + "   as \"" + retObj.getFieldValueString(looper, colNames[3]).toUpperCase() + "\" ";
                        osql.append(" ,").append(bussTableL).append(".").append(retObj.getFieldValueString(looper, colNames[1])).append("   as \"").append(retObj.getFieldValueString(looper, colNames[3]).toUpperCase()).append("\" ");
                        insql[j] += " , " + dbTableL + "." + retObj.getFieldValueString(looper, colNames[2]) + "  as  " + retObj.getFieldValueString(looper, colNames[1]).toUpperCase() + " ";
                    }
                }
                insql[j] += " from   " + dbTableL + " )  " + bussTableL + " ";
                //Write code to add the table with two nodes or more nodes
//                osql += " from  ";
                osql.append(" from  ");
                for (int i = 0; i < insql.length; i++) {
//                    osql += insql[i].toString() + " ";
                    osql.append(insql[i].toString()).append(" ");
                }
            }

            StringBuilder whereClause = new StringBuilder();
            {
                sqlstr = "Select ACTUAL_CLAUSE  , BUSS_TABLE_ID  , BUSS_TABLE_ID2 from PRG_GRP_BUSS_TABLE_RLT_MASTER where BUSS_TABLE_ID in (" + qryTable + ")  OR BUSS_TABLE_ID2 in (" + qryTable + ") ";

                finalQuery = sqlstr;
                retObj = execSelectSQL(finalQuery);
                colNames = retObj.getColumnNames();//bussTableIdRlt
                psize = retObj.getRowCount();
                if (psize > 0) {
                    for (int looper = 0; looper < psize; looper++) {
                        if (bussTableIdRlt.contains(retObj.getFieldValueString(looper, colNames[1])) && bussTableIdRlt.contains(retObj.getFieldValueString(looper, colNames[2]))) {
//                            whereClause += retObj.getFieldValueString(looper, colNames[0]);
                            whereClause.append("  ").append(retObj.getFieldValueString(looper, colNames[0]));
                            if (FinalBussTableIdRlt.isEmpty() || (!FinalBussTableIdRlt.contains(retObj.getFieldValueString(looper, colNames[1])))) {
                                FinalBussTableIdRlt.add(retObj.getFieldValueString(looper, colNames[1]));
                            }

                            if (FinalBussTableIdRlt.isEmpty() || (!FinalBussTableIdRlt.contains(retObj.getFieldValueString(looper, colNames[2])))) {
                                FinalBussTableIdRlt.add(retObj.getFieldValueString(looper, colNames[2]));
                            }
                        }

                    }
                }

            }

//            osql += " where 1=1 ";
            osql.append(" where 1=1 ").append(" and  ").append(whereClause);
//            osql += " and  " + whereClause;

            //connection = getConnection(bussTableId);
            // retObj = execSelectSQL(osql, connection);
        } catch (SQLException exp) {
            logger.error("Exception:", exp);
        }
        catch (NullPointerException e) {
            logger.error("Exception:", e);
        }finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    logger.error("Exception:", ex);
                }
            }
        }
        //return alist;
    }

    //added by santhosh.kumar@progenbusiness.com on 28/08/09 for  inserting data in business related tables
    //added by santhosh.kumar@progenbusiness.com on 28/08/09 for  inserting data in business related tables
    public boolean insertBusinessTable(String[] tableId, String[] noOfNodes, String grpId, StringBuffer newAddedBussTabIds) {
        boolean flag = false;
        //insert into PRG_GRP_BUSS_TABLE(BUSS_TABLE_ID, BUSS_TABLE_NAME, BUSS_DESC, BUSS_TYPE, NO_OF_NODES, DB_TABLE_ID, GRP_ID)
        //          SELECT (PRG_GRP_BUSS_TABLE_SiEQ.nextval), TABLE_NAME,TABLE_NAME,TABLE_TYPE,& as \"NO_OF_NODES\",TABLE_ID,& as \"GRP_ID\" FROM PRG_DB_MASTER_TABLE where TABLE_ID=&
        String insertBussTableQuery = getResourceBundle().getString("insertBussTable");
        String updateBussTableDetails = getResourceBundle().getString("updateBussTableDetails");
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            updateBussTableDetails = getResourceBundle().getString("updateBussTableDetails1");
        }
        String finalQuery = "";
        ArrayList queries = new ArrayList();
        ArrayList queries1 = new ArrayList();
        Object[] Obj = null;
//        String groupedtabIds = "";
        StringBuilder groupedtabIds = new StringBuilder();
        try {
            Obj = new Object[3];
            for (int i = 0; i < tableId.length; i++) {

                Obj[0] = noOfNodes[i];
                Obj[1] = grpId;
                Obj[2] = tableId[i];
                finalQuery = buildQuery(insertBussTableQuery, Obj);
//                System.out.println("insertBussTableQuery----"+finalQuery);
                queries.add(finalQuery);
                queries = insertGrpBussTableSrc(tableId[i], queries, grpId);
            }

            Obj = new Object[1];
            Obj[0] = grpId;
            finalQuery = buildQuery(updateBussTableDetails, Obj);
//             System.out.println("updateBussTableDetails----"+finalQuery);
            queries.add(finalQuery);
            if (isDebugEnable) //                    ProgenLog.log(ProgenLog.FINE, this, "insertBussinessTable"," executing multiple queries");
            {
                logger.info(" executing multiple queries");
            }
            flag = executeMultiple(queries);

            for (int i = 0; i < tableId.length; i++) {
//                groupedtabIds = groupedtabIds + "," + tableId[i];
                groupedtabIds.append(",").append(tableId[i]);
            }
//             if (!(groupedtabIds.equalsIgnoreCase(""))) {
//                groupedtabIds = groupedtabIds.substring(1);
//            }
            if (groupedtabIds.length() > 0) {
                groupedtabIds = new StringBuilder(groupedtabIds.substring(1));
            }
            queries1 = insertBusinessRelations(groupedtabIds.toString(), queries1, grpId);
            flag = executeMultiple(queries1);
            PbReturnObject retObj = new PbReturnObject();
            String getCurrentBussTabId = getResourceBundle().getString("getCurrentBussTabId");
            int currentBussTabId = 0;
            retObj = execSelectSQL(getCurrentBussTabId);
            currentBussTabId = retObj.getFieldValueInt(0, 0);
            currentBussTabId -= tableId.length - 1;
            for (int j = 0; j < tableId.length; j++) {
//                newAddedBussTabIds.append("" + (currentBussTabId + j));
                newAddedBussTabIds.append(Integer.toString(currentBussTabId + j));
                if (j + 1 != tableId.length) {
                    newAddedBussTabIds.append("-");
                }
            }
        } catch (SQLException e) {
            logger.error("Exception:", e);
            flag = false;

        }
        return flag;
    }

    public ArrayList insertGrpBussTableSrc(String tableId, ArrayList queries, String grpId) {
        // INSERT INTO PRG_GRP_BUSS_TABLE_SRC  (BUSS_SOURCE_ID,BUSS_TABLE_ID ,CONNECTION_ID ,DB_TABLE_ID,SOURCE_TYPE)
        // SELECT (PRG_GRP_BUSS_TABLE_SRC_seq.nextval),(PRG_GRP_BUSS_TABLE_SEQ.currval),connection_id,table_id ,table_type  FROM prg_db_master_table WHERE table_id= &
        String insertBussTableQuery;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            insertBussTableQuery = getResourceBundle().getString("insertBussSrcTableMysql");
        } else {
            insertBussTableQuery = getResourceBundle().getString("insertBussSrcTable");
        }
        String finalQuery = "";
        Object[] Obj = null;

        try {
            Obj = new Object[1];
            Obj[0] = tableId;
            if (isDebugEnable) //                    ProgenLog.log(ProgenLog.FINE, this, "insertGrpBussTableSrc","query fired inside try block");
            {
                logger.info("query fired inside try block");
            }
            finalQuery = buildQuery(insertBussTableQuery, Obj);
//            System.out.println("insertBussTableQuery---"+finalQuery);
            queries.add(finalQuery);
            queries = insertGrpBussTableSrcDetails(tableId, queries, grpId);

        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        //return errorFlag;
        return queries;
    }

    public ArrayList insertGrpBussTableSrcDetails(String tableId, ArrayList queries, String grpId) {
        //insert into PRG_GRP_BUSS_TABLE_SRC_DETAILS (BUSS_SRC_TABLE_DTL_ID,BUSS_SRC_ID,DB_TABLE_ID, BUSS_TABLE_ID,DB_COLUMN_ID,COLUMN_ALIAS,COLUMN_TYPE)
        //SELECT (PRG_GRP_BUSS_TBL_SRC_DTLS_SEQ.NEXTVAL),(PRG_GRP_BUSS_TABLE_SRC_seq.currval),table_id,(PRG_GRP_BUSS_TABLE_SEQ.currval),column_id, table_col_name ,
        //col_type FROM prg_db_master_table_details WHERE table_id=&
        String insertBussSrcTableDetailsQuery = getResourceBundle().getString("insertBussSrcTableDetails");
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            insertBussSrcTableDetailsQuery = getResourceBundle().getString("insertBussSrcTableDetails1");
        }
        String finalQuery = "";
        Object[] Obj = null;

        try {
            Obj = new Object[1];
            Obj[0] = tableId;
            // Obj[1] = tableId;
            if (isDebugEnable) //                    ProgenLog.log(ProgenLog.FINE, this, "insertGrpBussTableSrcDetails","query fired inside try block");
            {
                logger.info("query fired inside try block");
            }
            finalQuery = buildQuery(insertBussSrcTableDetailsQuery, Obj);
//            System.out.println("insertBussSrcTableDetailsQuery--"+finalQuery);
            queries.add(finalQuery);

            queries = insertGrpBussTableDetails(tableId, queries, grpId);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return queries;
    }

    public ArrayList insertGrpBussTableDetails(String tableId, ArrayList queries, String grpId) {

        // insert into PRG_GRP_BUSS_TABLE_DETAILS (BUSS_COLUMN_ID, BUSS_TABLE_ID, COLUMN_NAME, DB_TABLE_ID, DB_COLUMN_ID, BUSS_SRC_TABLE_DTL_ID,
        // IS_UNION, COLUMN_TYPE, ACTUAL_COL_FORMULA,ACTUAL_COL_FORMULA1, DEFAULT_AGGREGATION, BUCKET_ATTACHED, COLUMN_DISP_NAME)
        // SELECT (PRG_GRP_BUSS_TABLE_DETAILS_seq.nextval),(PRG_GRP_BUSS_TABLE_SEQ.currval),TABLE_COL_NAME,DB_TABLE_ID,COLUMN_ID,
        // (PRG_GRP_BUSS_TABLE_DETAILS_seq.currval) , 'N',COL_TYPE,'','','','',TABLE_COL_NAME FROM PRG_DB_MASTER_TABLE_DETAILS where DB_TABLE_ID=&
        String insertBussTableDetailsQuery = getResourceBundle().getString("insertBussTableDetails");
        String finalQuery = "";
        Object[] Obj = null;

        try {
            Obj = new Object[2];
            Obj[0] = tableId;
            Obj[1] = grpId;
            if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "insertGrpBussTableDetails", "query fired"+System.currentTimeMillis());
            {
                logger.info("query fired" + System.currentTimeMillis());
            }
            finalQuery = buildQuery(insertBussTableDetailsQuery, Obj);
//            System.out.println("insertBussTableDetailsQuery---"+finalQuery);
            queries.add(finalQuery);

            //queries = insertBusinessRelations(tableId, queries);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return queries;
    }

    public ArrayList insertBusinessRelations(String dbTableIds, ArrayList queries, String grpId) {
        //boolean flag = false;
        PbReturnObject retObj = null;
        //ArrayList queriesList = new ArrayList();

        String getDbMasterRelationsQuery = getResourceBundle().getString("getDbMasterRelations");
        String insertBussMasterRelationsQuery = getResourceBundle().getString("insertBussMasterRelations");
        String insertBussDetailsRelationsQuery;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            insertBussDetailsRelationsQuery = getResourceBundle().getString("insertBussDetailsRelationsMysql");
        } else {
            insertBussDetailsRelationsQuery = getResourceBundle().getString("insertBussDetailsRelations");
        }
        String RELATIONSHIP_ID = "";
        String finalQuery = "";
        Object[] Obj = null;

        String[] tableColumns = null;
        if (dbTableIds == null || "".equalsIgnoreCase(dbTableIds)) {
            dbTableIds = "null";
        }

        try {
            Obj = new Object[2];
            Obj[0] = dbTableIds;
            Obj[1] = dbTableIds;

            finalQuery = buildQuery(getDbMasterRelationsQuery, Obj);

            if (isDebugEnable) // ProgenLog.log(ProgenLog.FINE, this, "insertBussinessRelations", "query fired inside try block"+System.currentTimeMillis());
            {
                if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "insertBussinessRelations", "query fired inside try block"+System.currentTimeMillis());
                {
                    logger.info("query fired inside try block" + System.currentTimeMillis());
                }
            }
            retObj = execSelectSQL(finalQuery);
            tableColumns = retObj.getColumnNames();

            for (int outerIndex = 0; outerIndex < retObj.getRowCount(); outerIndex++) {
                RELATIONSHIP_ID = retObj.getFieldValueString(outerIndex, tableColumns[0]);
                Obj = new Object[1];
                Obj[0] = RELATIONSHIP_ID;
                finalQuery = buildQuery(insertBussMasterRelationsQuery, Obj);
                queries.add(finalQuery);

                Obj = new Object[5];
//                Obj[0] = RELATIONSHIP_ID;
//                Obj[1] = grpId;
//                Obj[2] = grpId;
//                Obj[3] = grpId;
//                Obj[4] = grpId;
                Obj[0] = grpId;
                Obj[1] = grpId;
                Obj[2] = RELATIONSHIP_ID;
                Obj[3] = grpId;
                Obj[4] = grpId;
                finalQuery = buildQuery(insertBussDetailsRelationsQuery, Obj);
                queries.add(finalQuery);
            }
            //flag = executeMultiple(queriesList);
            return queries;
        } catch (SQLException exp) {
            logger.error("Exception:", exp);
            return queries;
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
            return queries;
        }
    }

    //end of code added by santhosh.kumar@progenbusiness.com
    //added by santhosh.kumar@progenbusiness.com on 29/08/09 for inserting into dimension related tables of grp layer
    public boolean insertGrpDim(String[] dimIds, String grpId, String[] factIds) {

        boolean result = checkAndInsertBussTables(dimIds, grpId, factIds);

        boolean flag = false;
        String insertGrpDimQuery = getResourceBundle().getString("insertGrpDim");
        String finalQuery = "";
        ArrayList queries = new ArrayList();
        Object[] Obj = null;
        HashMap dimTabMap = new HashMap();
        HashMap memIdMap = new HashMap();
        try {
            Obj = new Object[3];
            for (int i = 0; i < dimIds.length; i++) {
                Obj[0] = grpId;
                Obj[1] = dimIds[i];
                Obj[2] = dimIds[i];
                finalQuery = buildQuery(insertGrpDimQuery, Obj);
                queries.add(finalQuery);
                queries = insertGrpDimTables(dimIds[i], queries, dimTabMap, grpId);

                queries = insertGrpDimMembers(dimIds[i], queries, dimTabMap, memIdMap, grpId);

                queries = insertGrpDimRel(dimIds[i], queries, memIdMap);
            }

//            System.out.println("queries\t"+queries);
            if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "insertGrpDim", "query fired inside try block"+System.currentTimeMillis());
            {
                logger.info("query fired inside try block" + System.currentTimeMillis());
            }
            Connection conn = null;
            conn = ProgenConnection.getInstance().getConnection();
            flag = executeMultiple(queries, conn);
            queries = new ArrayList();

        } catch (Exception e) {
            logger.error("Exception:", e);
            flag = false;
        }
        return flag;
    }

    private ArrayList insertGrpDimTables(String dimId, ArrayList queries, HashMap dimTabMap, String grpId) {
        String getQryDimTablesQuery = getResourceBundle().getString("getQryDimTables");
        String insertGrpDimTablesQuery = getResourceBundle().getString("insertGrpDimTables");
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            insertGrpDimTablesQuery = getResourceBundle().getString("insertGrpDimTables1");
        }
        String finalQuery = "";
        Object[] Obj = null;
        PbReturnObject retObj = null;
        String[] tabColNames = null;
        PbDb pbDb = new PbDb();

        try {
            Obj = new Object[2];
            ////.println("dimId\t"+dimId+"\ngrpId\t"+grpId);
            Obj[0] = dimId;
            Obj[1] = grpId;
            finalQuery = buildQuery(getQryDimTablesQuery, Obj);
            ////.println("finalQuery33--\t"+finalQuery);
            if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "insertGrpDimsTables", "query fired"+System.currentTimeMillis());
            {
                logger.info("query fired" + System.currentTimeMillis());
            }
            retObj = execSelectSQL(finalQuery);

            PbReturnObject grpDimTabSeq = null;
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                grpDimTabSeq = pbDb.execSelectSQL("select ident_current('PRG_GRP_DIM_TABLES')");
            }
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                grpDimTabSeq = pbDb.execSelectSQL("select LAST_INSERT_ID(dim_tab_id) from PRG_GRP_DIM_TABLES order by 1 desc limit 1");
            }
            tabColNames = retObj.getColumnNames();
            int dimTabId = 0;
            int oldDimTabId = 0;
            for (int index = 0; index < retObj.getRowCount(); index++) {
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    dimTabId = grpDimTabSeq.getFieldValueInt(0, 0) + dimIndex;
                    dimIndex++;
                    oldDimTabId = retObj.getFieldValueInt(index, tabColNames[1]);
                    Obj = new Object[2];
                    Obj[0] = dimId;
                    Obj[1] = retObj.getFieldValueString(index, tabColNames[0]);
                    dimTabMap.put(String.valueOf(oldDimTabId), dimTabId);
                    finalQuery = buildQuery(insertGrpDimTablesQuery, Obj);

                } else {
                    dimTabId = getSequenceNumber("select PRG_GRP_DIM_TAB_DETAILS_SEQ.NEXTVAL from dual");
                    oldDimTabId = retObj.getFieldValueInt(index, tabColNames[1]);
                    Obj = new Object[3];
                    Obj[0] = dimTabId;
                    Obj[1] = dimId;
                    Obj[2] = retObj.getFieldValueString(index, tabColNames[0]);
                    //PRG_GRP_DIM_TAB_DETAILS_SEQ.NEXTVAL
                    dimTabMap.put(String.valueOf(oldDimTabId), dimTabId);
                    finalQuery = buildQuery(insertGrpDimTablesQuery, Obj);

                }

                queries.add(finalQuery);

                queries = insertGrpDimTablesDetails(queries, dimTabId, oldDimTabId, grpId);

            }

        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        //return errorFlag;
        return queries;
    }

    private ArrayList insertGrpDimTablesDetails(ArrayList queries, int dimTabId, int oldDimTabId, String grpId) {
        String insertGrpDimTablesDetailsQuery = getResourceBundle().getString("insertGrpDimTablesDetails");
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            insertGrpDimTablesDetailsQuery = getResourceBundle().getString("insertGrpDimTablesDetails1");
        }
        String finalQuery = "";
        Object[] Obj = null;

        try {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {

                Obj = new Object[2];
                Obj[0] = oldDimTabId;
                Obj[1] = grpId;
            } else {
                Obj = new Object[3];
                Obj[0] = dimTabId;
                Obj[1] = oldDimTabId;
                Obj[2] = grpId;

            }

            if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "insertGrpDimTablesDetails", "query fired inside try block"+System.currentTimeMillis());
            {
                logger.info("query fired inside try block" + System.currentTimeMillis());
            }
            finalQuery = buildQuery(insertGrpDimTablesDetailsQuery, Obj);
            ////.println("finalQuery in insertGrpDimTablesDetails is " + finalQuery);
            queries.add(finalQuery);

            //queries = insertGrpDimMembers(dimId, queries);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return queries;
    }

    private ArrayList insertGrpDimMembers(String dimId, ArrayList queries, HashMap dimTabMap, HashMap memIdMap, String grpId) {
        String insertGrpDimMembersQuery = getResourceBundle().getString("insertGrpDimMembers");
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            insertGrpDimMembersQuery = getResourceBundle().getString("insertGrpDimMembers1");
        }
        String getQryDimMbrsInfoQuery = getResourceBundle().getString("getQryDimMbrsInfo");
        String finalQuery = "";
        PbReturnObject retObj = null;
        String[] tabColNames = null;
        Object[] Obj = null;

        String member_name = "";
        String use_denom_table = "";
        String denom_tab_id = "";
        String denom_query = "";
        String dim_tab_id = "";
        String mem_id = "";
        String memOrderBy = "";
        String mem_isNullVal = "";

        try {
            Obj = new Object[1];
            Obj[0] = dimId;
            finalQuery = buildQuery(getQryDimMbrsInfoQuery, Obj);
            ////.println("finalQuery for dim Details\t"+finalQuery);

            if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "insertGrpDimMembers", "query fired inside try block"+System.currentTimeMillis());
            {
                logger.info("query fired inside try block" + System.currentTimeMillis());
            }
            retObj = execSelectSQL(finalQuery);
            tabColNames = retObj.getColumnNames();
            int newMemId = 0;

            PbReturnObject grpMemIdSeq = null;
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                grpMemIdSeq = super.execSelectSQL("select ident_current('PRG_GRP_DIM_MEMBER')");
            }
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                grpMemIdSeq = super.execSelectSQL("select LAST_INSERT_ID(MEMBER_ID) FROM PRG_GRP_DIM_MEMBER ORDER BY 1 DESC LIMIT 1");
            }
            for (int index = 0; index < retObj.getRowCount(); index++) {

                member_name = retObj.getFieldValueString(index, tabColNames[0]);
                use_denom_table = retObj.getFieldValueString(index, tabColNames[1]);
                denom_tab_id = retObj.getFieldValueString(index, tabColNames[2]);
                denom_query = retObj.getFieldValueString(index, tabColNames[3]);
                dim_tab_id = retObj.getFieldValueString(index, tabColNames[4]);
                mem_id = retObj.getFieldValueString(index, tabColNames[5]);
                memOrderBy = getBussColID(retObj.getFieldValueString(index, tabColNames[6]), grpId);
                mem_isNullVal = retObj.getFieldValueString(index, tabColNames[7]);

                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {

                    newMemId = grpMemIdSeq.getFieldValueInt(0, 0) + memIndex;
                    memIndex++;
                    Obj = new Object[9];
                    Obj[0] = member_name;
                    Obj[1] = String.valueOf(dimTabMap.get(dim_tab_id));//"ident_current('PRG_GRP_DIM_TAB_DETAILS')";
                    Obj[2] = use_denom_table;
                    if (denom_tab_id == null || denom_tab_id.equalsIgnoreCase("NULL") || denom_tab_id.trim().equals("")) {
                        Obj[3] = "0";
                    } else {
                        Obj[3] = denom_tab_id;
                    }
                    Obj[4] = grpId;
                    Obj[5] = denom_query;
                    Obj[6] = member_name;
                    if (memOrderBy != null || memOrderBy != "") {
                        Obj[7] = memOrderBy;
                    } else {
                        Obj[7] = null;
                    }
                    Obj[8] = mem_isNullVal;

                } else {
                    newMemId = getSequenceNumber("select PRG_GRP_DIM_MEMBER_seq.nextval from dual");

                    //modified by susheela start
                    Obj = new Object[10];
                    Obj[0] = newMemId;
                    Obj[1] = member_name;
                    Obj[2] = String.valueOf(dimTabMap.get(dim_tab_id));
                    Obj[3] = use_denom_table;
                    Obj[4] = denom_tab_id;
                    Obj[5] = grpId;
                    Obj[6] = denom_query;
                    Obj[7] = member_name;
                    if (memOrderBy != null || memOrderBy != "") {
                        Obj[8] = memOrderBy;
                    } else {
                        Obj[8] = null;
                    }
                    Obj[9] = mem_isNullVal;

                }

                //modified by susheela start
                //modified by susheela over
                memIdMap.put(String.valueOf(mem_id), newMemId);

                finalQuery = buildQuery(insertGrpDimMembersQuery, Obj);
                ////.println("finalQuery in insertGrpDimMembers is " + finalQuery);
                queries.add(finalQuery);

                queries = insertGrpDimMembersDtls(mem_id, queries, newMemId, grpId);

            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return queries;
    }

    private ArrayList insertGrpDimMembersDtls(String mem_id, ArrayList queries, int newMemId, String grpId) {
        String insertGrpDimMembersDtlsQuery = getResourceBundle().getString("insertGrpDimMembersDtls");
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            insertGrpDimMembersDtlsQuery = getResourceBundle().getString("insertGrpDimMembersDtls1");
        }
        String finalQuery = "";
        Object[] Obj = null;

        try {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                Obj = new Object[2];
                Obj[0] = mem_id;
                Obj[1] = grpId;
                if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "insertGrpDimMembersDtls", "query fired inside try block"+System.currentTimeMillis());
                {
                    logger.info("query fired inside try block" + System.currentTimeMillis());
                }
                finalQuery = buildQuery(insertGrpDimMembersDtlsQuery, Obj);
            } else {
                Obj = new Object[3];
                Obj[0] = newMemId;
                Obj[1] = mem_id;
                Obj[2] = grpId;
                if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "insertGrpDimMembersDtls", "query fired inside try block"+System.currentTimeMillis());
                {
                    logger.info("query fired inside try block" + System.currentTimeMillis());
                }
                finalQuery = buildQuery(insertGrpDimMembersDtlsQuery, Obj);
            }

            //////.println("finalQuery in insertGrpDimMembersDtls is " + finalQuery);
            queries.add(finalQuery);

            //queries = insertGrpDimRel(dimId, queries, mem_id);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return queries;
    }

    private ArrayList insertGrpDimRel(String dimId, ArrayList queries, HashMap memIdMap) {
        String getQryDimRelQuery = getResourceBundle().getString("getQryDimRel");
        String insertGrpDimRelQuery = getResourceBundle().getString("insertGrpDimRel");
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            insertGrpDimRelQuery = getResourceBundle().getString("insertGrpDimRel1");
        }
        String finalQuery = "";
        Object[] Obj = null;
        PbReturnObject retObj = null;
        String[] tableColNames = null;
        String[] relIds = null;

        try {
            Obj = new Object[1];
            Obj[0] = dimId;
            finalQuery = buildQuery(getQryDimRelQuery, Obj);
            ////.println("finalQuery parent main is "+finalQuery);
            if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "insertGrpDimRel", "query fired inside try block"+System.currentTimeMillis());
            {
                logger.info("query fired inside try block" + System.currentTimeMillis());
            }
            retObj = execSelectSQL(finalQuery);
            tableColNames = retObj.getColumnNames();

            relIds = new String[retObj.getRowCount()];
            for (int index = 0; index < retObj.getRowCount(); index++) {
                relIds[index] = retObj.getFieldValueString(index, tableColNames[3]);
                Obj = new Object[2];
                Obj[0] = dimId;
                Obj[1] = relIds[index];

                finalQuery = buildQuery(insertGrpDimRelQuery, Obj);
                ////.println("finalQuery parent minor is "+finalQuery);
                queries.add(finalQuery);
                queries = insertGrpDimRelDtls(dimId, queries, relIds[index], memIdMap);
            }

        } 
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        }catch (Exception e) {
            logger.error("Exception:", e);
        }        return queries;
        }

    private ArrayList insertGrpDimRelDtls(String dimId, ArrayList queries, String relId, HashMap memIdMap) {
        String getQryDimRelDtlsQuery = getResourceBundle().getString("getQryDimRelDtls");
        String finalQuery = "";
        String insertGrpDimRelDtlsQuery = getResourceBundle().getString("insertGrpDimRelDtls");
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            insertGrpDimRelDtlsQuery = getResourceBundle().getString("insertGrpDimRelDtls1");
        }
        Object[] Obj = null;
        PbReturnObject retObj = null;
        String[] tableColNames = null;

        try {
            Obj = new Object[2];
            Obj[0] = dimId;
            Obj[1] = relId;
            finalQuery = buildQuery(getQryDimRelDtlsQuery, Obj);
            ////.println("finalQuery child main is "+finalQuery);
            if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "insertGrpDimRlDtls", "query fired inside try block"+System.currentTimeMillis());
            {
                logger.info("query fired inside try block" + System.currentTimeMillis());
            }
            retObj = execSelectSQL(finalQuery);
            tableColNames = retObj.getColumnNames();

            for (int i = 0; i < retObj.getRowCount(); i++) {
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {

                    Obj = new Object[3];
                    Obj[0] = memIdMap.get(retObj.getFieldValueString(i, tableColNames[1]));
                    Obj[1] = relId;
                    Obj[2] = retObj.getFieldValueString(i, tableColNames[1]);

                    finalQuery = buildQuery(insertGrpDimRelDtlsQuery, Obj);
                    ////.println("finalQuery minor is " + finalQuery);
                    queries.add(finalQuery);
                } else {

                    Obj = new Object[3];
                    Obj[0] = memIdMap.get(retObj.getFieldValueString(i, tableColNames[1]));
                    Obj[1] = relId;
                    Obj[2] = retObj.getFieldValueString(i, tableColNames[1]);
                    finalQuery = buildQuery(insertGrpDimRelDtlsQuery, Obj);

                    queries.add(finalQuery);
                }

            }
        } 
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        }catch (Exception e) {
            logger.error("Exception:", e);
        }        return queries;
        }

    public void insertCLOBData() {
    }

    public boolean checkAndInsertBussTables(String[] dimIds, String grpId) {
        boolean flag = false;
        String getBussTabIdsByDimQuery = getResourceBundle().getString("getBussTabIdsByDim");
        String finalQuery = "";
        PbReturnObject retObj = null;
        Object[] Obj = null;
        Vector bussTabVector = new Vector();
        String[] tabcolNames = null;

        String[] tableId = null;
        String[] noOfNodes = null;
        StringBuffer tempBuffer = new StringBuffer();//Added By Santhosh
        try {
            Obj = new Object[2];
            for (int i = 0; i < dimIds.length; i++) {
                Obj[0] = dimIds[i];
                Obj[1] = grpId;
                finalQuery = buildQuery(getBussTabIdsByDimQuery, Obj);
                if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "checkAndInsertBussTables", "query fired inside try block"+System.currentTimeMillis());
                {
                    logger.info("query fired inside try block" + System.currentTimeMillis());
                }
                retObj = execSelectSQL(finalQuery);
                tabcolNames = retObj.getColumnNames();
                for (int j = 0; j < retObj.getRowCount(); j++) {
                    if ((retObj.getFieldValueString(j, tabcolNames[0]) == null) || ("".equalsIgnoreCase(retObj.getFieldValueString(j, tabcolNames[0])))) {
                        if ((!bussTabVector.contains(retObj.getFieldValueString(j, tabcolNames[1])))) {
                            bussTabVector.add(retObj.getFieldValueString(j, tabcolNames[1]));
                        }
                    }
                }
            }

            tableId = new String[bussTabVector.size()];
            noOfNodes = new String[bussTabVector.size()];

            for (int i = 0; i < bussTabVector.size(); i++) {
                tableId[i] = String.valueOf(bussTabVector.get(i));
                noOfNodes[i] = "1";
            }

            flag = insertBusinessTable(tableId, noOfNodes, grpId, tempBuffer);

        } catch (Exception e) {
            logger.error("Exception:", e);
            flag = false;
        }
        return flag;
    }

    public boolean checkAndInsertBussTablesForFacts(String[] tableIds, String grpId, String[] noOfNodes) {
        boolean flag = false;
        String getBussTabIdsByDBTabIdsQuery = getResourceBundle().getString("getBussTabIdsByDBTabIds");
        String finalQuery = "";
        PbReturnObject retObj = null;
        Object[] Obj = null;
        Vector bussTabVector = new Vector();
        String[] tabcolNames = null;
        String[] tableId = null;
        try {
            for (int i = 0; i < tableIds.length; i++) {
                bussTabVector.add(tableIds[i]);
            }
            Obj = new Object[2];
            for (int i = 0; i < tableIds.length; i++) {
                Obj[0] = tableIds[i];
                Obj[1] = grpId;
                finalQuery = buildQuery(getBussTabIdsByDBTabIdsQuery, Obj);
                if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "checkAndInsertBussTablesForFacts", "query fired inside try block"+System.currentTimeMillis());
                {
                    logger.info("query fired inside try block" + System.currentTimeMillis());
                }
                retObj = execSelectSQL(finalQuery);
                tabcolNames = retObj.getColumnNames();

                for (int j = 0; j < retObj.getRowCount(); j++) {
                    if ((retObj.getFieldValueString(j, tabcolNames[0]) == null) || ("".equalsIgnoreCase(retObj.getFieldValueString(j, tabcolNames[0])))) {
                        if ((!bussTabVector.contains(retObj.getFieldValueString(j, tabcolNames[1])))) {
                            bussTabVector.add(retObj.getFieldValueString(j, tabcolNames[1]));
                        }
                    }
                }
            }
            tableId = new String[bussTabVector.size()];
            noOfNodes = new String[bussTabVector.size()];
            for (int i = 0; i < bussTabVector.size(); i++) {
                tableId[i] = String.valueOf(bussTabVector.get(i));
                noOfNodes[i] = "1";
            }
            StringBuffer tempBuffer = new StringBuffer();
            flag = insertBusinessTable(tableId, noOfNodes, grpId, tempBuffer);

        } 
        catch (SQLException ex) {
            logger.error("Exception:", ex);
            flag = false;
        }catch (Exception e) {
            logger.error("Exception:", e);
            flag = false;
        }        return flag;
        }

    public boolean checkAndInsertBussTables(String[] dimIds, String grpId, String[] factIds) {
        //   System.out.println("1");
        boolean flag = false;
        String getBussTabIdsByDimQuery = getResourceBundle().getString("getBussTabIdsByDim");
        String getBussTabIdsByDBTabIdsQuery = getResourceBundle().getString("getBussTabIdsByDBTabIds");
        String finalQuery = "";
        PbReturnObject retObj = null;
        Object[] Obj = null;
        Vector bussTabVector = new Vector();
        String[] tabcolNames = null;

        String[] tableId = null;
        String[] noOfNodes = null;
        try {
            Obj = new Object[2];
            for (int i = 0; i < dimIds.length; i++) {
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    Obj[0] = grpId;
                    Obj[1] = dimIds[i];

                } else {
                    Obj[0] = dimIds[i];
                    Obj[1] = grpId;

                }

                finalQuery = buildQuery(getBussTabIdsByDimQuery, Obj);
//                System.out.println("getBussTabIdsByDimQuery\t"+finalQuery);
                if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "checkAndInsertBussTables", "query fired inside try block"+System.currentTimeMillis());
                {
                    logger.info("query fired inside try block" + System.currentTimeMillis());
                }
                retObj = execSelectSQL(finalQuery);
                tabcolNames = retObj.getColumnNames();
                for (int j = 0; j < retObj.getRowCount(); j++) {
                    if ((retObj.getFieldValue(j, tabcolNames[0]) == null) || ("".equalsIgnoreCase(retObj.getFieldValueString(j, tabcolNames[0])))
                            || ("NULL".equalsIgnoreCase(retObj.getFieldValueString(j, tabcolNames[0])))) {
                        if ((!bussTabVector.contains(retObj.getFieldValueString(j, tabcolNames[1])))) {
                            bussTabVector.add(retObj.getFieldValueString(j, tabcolNames[1]));
                        }
                    }
                }
            }

            for (int i = 0; i < factIds.length; i++) {
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    if (grpId == null) {
                        grpId = "0";
                    }
                    if (factIds[i] == null || factIds[i].equalsIgnoreCase("NULL") || factIds[i].trim().equals("")) {
                        factIds[i] = "0";
                    }
                    Obj[0] = grpId;
                    Obj[1] = factIds[i];
                } else {
                    Obj[0] = factIds[i];
                    Obj[1] = grpId;

                }

                finalQuery = buildQuery(getBussTabIdsByDBTabIdsQuery, Obj);
//                System.out.println("getBussTabIdsByDBTabIdsQuery\t"+finalQuery);
                if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "checkAndInsertBussTables", "query fired"+System.currentTimeMillis());
                {
                    logger.info("query fired" + System.currentTimeMillis());
                }
                retObj = execSelectSQL(finalQuery);
                tabcolNames = retObj.getColumnNames();

                for (int j = 0; j < retObj.getRowCount(); j++) {
                    if ((retObj.getFieldValueString(j, tabcolNames[0]) == null) || ("".equalsIgnoreCase(retObj.getFieldValueString(j, tabcolNames[0])))) {
                        if ((!bussTabVector.contains(retObj.getFieldValueString(j, tabcolNames[1])))) {
                            bussTabVector.add(retObj.getFieldValueString(j, tabcolNames[1]));
                        }
                    }
                }
            }

            tableId = new String[bussTabVector.size()];
            noOfNodes = new String[bussTabVector.size()];

            for (int i = 0; i < bussTabVector.size(); i++) {
                tableId[i] = String.valueOf(bussTabVector.get(i));
                noOfNodes[i] = "1";
            }
            StringBuffer tempBuffer = new StringBuffer();
            flag = insertBusinessTable(tableId, noOfNodes, grpId, tempBuffer);

        } 
        catch (SQLException ex) {
           logger.error("Exception:", ex);
            flag = false;
        }catch (Exception e) {
            logger.error("Exception:", e);
            flag = false;
        }        return flag;
        }
    //end of code added by santhosh.kumar@progenbusiness.com on 01/09/09

    public String getRelatedDimTables(String[] tableId, String dimensionId) {
        // tableId="3";
        StringBuffer relTab = new StringBuffer();
        int i = 0;

        try {
            con = ProgenConnection.getInstance().getConnection();

            relTab.append("<root>");
            for (int k = 0; k < tableId.length; k++) {
                StringBuffer sql = new StringBuffer();
                sql.append("select mstr.table_id,mstr.table_name, det.column_id,det.table_col_name,det.table_col_cust_name from ");
                sql.append(" prg_db_master_table_details det, prg_db_master_table mstr ");
                sql.append(" where det.is_active='Y' AND mstr.table_id =det.table_id and  det.table_id in ( ");
                sql.append(" select  table_id2 as reltable from prg_db_table_rlt_master where (table_id=" + tableId[k] + ") ");
                sql.append(" union select table_id as reltable  from prg_db_table_rlt_master ");
                sql.append(" where  (table_id2=" + tableId[k] + ")) and det.table_id not in " + "(select tab_id from prg_qry_dim_tables where dim_id=" + dimensionId + ") order by " + "mstr.table_id,det.table_col_name");
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("my SQL " + sql);
                stmt = con.createStatement();
                if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "getRelatedDimTables", "query fired inside try block"+System.currentTimeMillis());
                {
                    logger.info("query fired inside try block" + System.currentTimeMillis());
                }
                rs = stmt.executeQuery(sql.toString());
                String tabName = "";

                while (rs.next()) {
                    i = 1;
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("" + rs.getString("TABLE_NAME"));
                    if (tabName.equalsIgnoreCase("")) {
                        relTab.append("<relatedTable>");
                        relTab.append("<table>" + rs.getString("TABLE_NAME") + "</table>");
                        relTab.append("<table-id>" + rs.getString("TABLE_ID") + "</table-id>");
                        // relTab.append("<reltableName>"+tabNames[k]+"</reltableName>");
                        tabName = rs.getString("TABLE_NAME");
                    } else {
                        if (!(tabName.equalsIgnoreCase(rs.getString("TABLE_NAME")))) {
                            relTab.append("</relatedTable>");
                            relTab.append("<relatedTable>");
                            relTab.append("<table>" + rs.getString("TABLE_NAME") + "</table>");
                            relTab.append("<table-id>" + rs.getString("TABLE_ID") + "</table-id>");

                            tabName = rs.getString("TABLE_NAME");
                        }
                    }
                    relTab.append("<column-id>" + rs.getString("COLUMN_ID") + "</column-id>");
                    relTab.append("<column-name>" + rs.getString("TABLE_COL_NAME") + "</column-name>");

                }
                if (i == 1) {
                    relTab.append("</relatedTable>");
                }
            }
            rs.close();
            stmt.close();
            con.close();

            relTab.append("</root>");
        } 
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        }catch (Exception e) {
            logger.error("Exception:", e);
        }        String xy = relTab.toString();
        if (i == 0) {
            xy = "noData";
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("" + xy);
        return xy;
    }

    public ArrayList getTableNames() {
        ArrayList list = new ArrayList();
        int endtableFlag = 0;
        try {
            //Connection con = null;
            //Class.forName("oracle.jdbc.driver.OracleDriver");
            //String parentName = "";
            //Statement stmt = con.createStatement();
            //ResultSet rs = null;

            String sql = "SELECT t1.TABLE_ID,t1.TABLE_NAME,t2.COLUMN_ID," + "t2.TABLE_COL_NAME, count(t2.column_id) over (partition by t2.TABLE_ID) as column_count " + " ,t2.IS_ACTIVE " + " FROM PRG_DB_MASTER_TABLE t1,PRG_DB_MASTER_TABLE_DETAILS t2  WHERE t1.TABLE_ID=t2.TABLE_ID " + "  AND t2.IS_ACTIVE='Y' ";

            String sql2 = "SELECT gbt.BUSS_TABLE_ID, gbt.BUSS_TABLE_NAME,gbtd.BUSS_COLUMN_ID,gbtd.column_disp_name,count(gbtd.BUSS_COLUMN_ID) " + "over (partition by gbtd.BUSS_TABLE_ID) as column_count FROM PRG_GRP_BUSS_TABLE gbt," + "PRG_GRP_BUSS_TABLE_DETAILS gbtd where gbt.buss_table_id=gbtd.buss_table_id";

            //String colName = "";
            //String tabName = "";
            String tableId = "";

            if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "getTableNames", "query fired inside try block"+System.currentTimeMillis());
            {
                logger.info("query fired inside try block" + System.currentTimeMillis());
            }
            PbReturnObject pbReturnObject = execSelectSQL(sql);
            //rs = stmt.executeQuery(sql);

            //PbReturnObject pbReturnObject = new PbReturnObject(rs);
            //Table table2 = new Table();
            for (int j = 0; j < pbReturnObject.getRowCount(); j++) {
                Table table = new Table();
                boolean b = tableId.equalsIgnoreCase(pbReturnObject.getFieldValueString(j, "TABLE_ID"));
                if ((tableId.length() == 0) || (!b)) {

                    Table table1 = new Table();
                    table1.setTableName(pbReturnObject.getFieldValueString(j, "TABLE_NAME"));
                    table1.setTableId(pbReturnObject.getFieldValueString(j, "TABLE_ID"));
                    tableId = pbReturnObject.getFieldValueString(j, "TABLE_ID");
                    table.setColumnName(pbReturnObject.getFieldValueString(j, "TABLE_COL_NAME"));
                    table.setColumnId(pbReturnObject.getFieldValueString(j, "COLUMN_ID"));
                    list.add(table1);
                    list.add(table);
                    endtableFlag = 1;
                } else {
                    table.setColumnName(pbReturnObject.getFieldValueString(j, "TABLE_COL_NAME"));
                    table.setColumnId(pbReturnObject.getFieldValueString(j, "COLUMN_ID"));
                    endtableFlag++;
                    if (endtableFlag == Integer.parseInt(pbReturnObject.getFieldValueString(j, "COLUMN_COUNT"))) {
                        table.setEndTable("true");
                    }
                    list.add(table);
                }
            }
        } 
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        }catch (Exception e) {
            logger.error("Exception:", e);
        }
        return list;

    }

    public ArrayList viewBussData(String bussTableId, String grpId) {
//        PbBussGrpResourceBundle resourceBundle = new PbBussGrpResourceBundle();
        ArrayList alist = new ArrayList();
        PbReturnObject retObj = null;
        Object[] Obj = null;
//        ProgenConnection prgMetadataCon = null;
        String getALlLayerTableNamesQuery = getResourceBundle().getString("getALlLayerTableNames");
        String getALlLayerColNamesQuery = getResourceBundle().getString("getALlLayerColNames");
        String getRelatedBussTablesQuery = getResourceBundle().getString("getRelatedBussTables");
        String srcTabName = "";
        String bussTabName = "";
        String dbTabName = "";

        // String srcColName = "";
        //String bussColName = "";
        //String dbColName = "";
        String[] colNames = null;

        //String sqlStr = "";
        String finalQuery = "";

        Connection connection = null;
        try {
            Obj = new Object[1];
            Obj[0] = bussTableId;
            finalQuery = buildQuery(getALlLayerTableNamesQuery, Obj);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("final query in view data is" + finalQuery);

            if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "viewBussData", "query fired inside try block"+System.currentTimeMillis());
            {
                logger.info("query fired inside try block" + System.currentTimeMillis());
            }
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();

            if (retObj != null && retObj.getRowCount() != 0) {
                bussTabName = retObj.getFieldValueString(0, colNames[0]);
                srcTabName = retObj.getFieldValueString(0, colNames[1]);
                dbTabName = retObj.getFieldValueString(0, colNames[2]);
            }

            Obj = new Object[1];
            Obj[0] = bussTableId;
            finalQuery = buildQuery(getALlLayerColNamesQuery, Obj);
            if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "viewBussData", "query fired"+System.currentTimeMillis());
            {
                logger.info("query fired " + System.currentTimeMillis());
            }
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                String osql = "select top 100 ";
//                String insql = "select ";
                StringBuilder osql = new StringBuilder();
                osql.append("select top 100 ");
                StringBuilder insql = new StringBuilder();
                insql.append("select ");

                int psize = retObj.getRowCount();
                if (psize > 0) {
                    for (int looper = 0; looper < psize; looper++) {
                        // columnNames[looper] = retObj.getFieldValueString(looper,"DISPLAY_NAME");
                        // tableColumnsNames[looper] =retObj.getFieldValueString(looper,"COLUMN_NAME").toUpperCase();
                        if (looper == 0) {
//                            osql += " " + bussTabName + "." + retObj.getFieldValueString(looper, colNames[1]) + "  as  \"" + retObj.getFieldValueString(looper, colNames[3]).toUpperCase() + "\" ";
                            osql.append(" ").append(bussTabName).append(".").append(retObj.getFieldValueString(looper, colNames[1])).append("  as  \"").append(retObj.getFieldValueString(looper, colNames[3]).toUpperCase()).append("\" ");
//                            insql += " " + dbTabName + "." + retObj.getFieldValueString(looper, colNames[2]) + "  as  " + retObj.getFieldValueString(looper, colNames[1]).toUpperCase() + " ";
                            insql.append(" ").append(dbTabName).append(".").append(retObj.getFieldValueString(looper, colNames[2])).append("  as  ").append(retObj.getFieldValueString(looper, colNames[1]).toUpperCase()).append(" ");

                        } else {
//                            osql += " ," + bussTabName + "." + retObj.getFieldValueString(looper, colNames[1]) + "   as \"" + retObj.getFieldValueString(looper, colNames[3]).toUpperCase() + "\" ";
                            osql.append(" ,").append(bussTabName).append(".").append(retObj.getFieldValueString(looper, colNames[1]) + "   as \"" + retObj.getFieldValueString(looper, colNames[3]).toUpperCase()).append("\" ");
                            insql.append(" , ").append(dbTabName).append(".").append(retObj.getFieldValueString(looper, colNames[2])).append("  as  ").append(retObj.getFieldValueString(looper, colNames[1]).toUpperCase()).append(" ");

                        }
                    }
//                    osql += " from ( " + insql + " from " + dbTabName + " ) " + bussTabName + " ";
                    osql.append(" from ( ").append(insql).append(" from ").append(dbTabName).append(" ) ").append(bussTabName).append(" ");

                    connection = getBussTableConnection(bussTableId);
                    retObj = execSelectSQL(osql.toString(), connection);
                }
            } else {
//                String osql = "select ";
//                String insql = "select ";
                StringBuilder osql = new StringBuilder();
                osql.append("select ");
                StringBuilder insql = new StringBuilder();
                insql.append("select ");

                int psize = retObj.getRowCount();
                if (psize > 0) {
                    for (int looper = 0; looper < psize; looper++) {
                        // columnNames[looper] = retObj.getFieldValueString(looper,"DISPLAY_NAME");
                        // tableColumnsNames[looper] =retObj.getFieldValueString(looper,"COLUMN_NAME").toUpperCase();
                        if (looper == 0) {
//                            osql += " " + bussTabName + "." + retObj.getFieldValueString(looper, colNames[1]) + "  as  \"" + retObj.getFieldValueString(looper, colNames[3]).toUpperCase() + "\" ";
//                            insql += " " + dbTabName + "." + retObj.getFieldValueString(looper, colNames[2]) + "  as  " + retObj.getFieldValueString(looper, colNames[1]).toUpperCase() + " ";
                            osql.append(" ").append(bussTabName).append(".").append(retObj.getFieldValueString(looper, colNames[1])).append("  as  \"").append(retObj.getFieldValueString(looper, colNames[3]).toUpperCase()).append("\" ");
                            insql.append(" ").append(dbTabName).append(".").append(retObj.getFieldValueString(looper, colNames[2])).append("  as  ").append(retObj.getFieldValueString(looper, colNames[1]).toUpperCase()).append(" ");

                        } else {
//                            osql += " ," + bussTabName + "." + retObj.getFieldValueString(looper, colNames[1]) + "   as \"" + retObj.getFieldValueString(looper, colNames[3]).toUpperCase() + "\" ";
//                            insql += " , " + dbTabName + "." + retObj.getFieldValueString(looper, colNames[2]) + "  as  " + retObj.getFieldValueString(looper, colNames[1]).toUpperCase() + " ";
                            osql.append(" ,").append(bussTabName).append(".").append(retObj.getFieldValueString(looper, colNames[1])).append("   as \"").append(retObj.getFieldValueString(looper, colNames[3]).toUpperCase()).append("\" ");
                            insql.append(" , ").append(dbTabName).append(".").append(retObj.getFieldValueString(looper, colNames[2]) + "  as  " + retObj.getFieldValueString(looper, colNames[1]).toUpperCase()).append(" ");

                        }
                    }
//                    osql += " from ( " + insql + " from " + dbTabName + " ) " + bussTabName + " where rownum <=100 ";
                    osql.append(" from ( ").append(insql).append(" from ").append(dbTabName).append(" ) ").append(bussTabName).append(" where rownum <=100 ");
                    connection = getBussTableConnection(bussTableId);
                    retObj = execSelectSQL(osql.toString(), connection);
                }
            }
            // com.progen.datadisplay.db.PbDataDisplayBeanDb ddbd = new com.progen.datadisplay.db.PbDataDisplayBeanDb();
//            connection = getBussTableConnection(bussTableId);
//            retObj = execSelectSQL(osql, connection);
            // alist.add(retObj);//added by bharathi reddy
            //for getting related tables from PRG_DB_TABLE_RLT_MASTER
            Obj = new Object[3];
            Obj[0] = bussTableId;
            Obj[1] = bussTableId;
            Obj[2] = grpId;
            PreparedStatement preparedStatement = null;
//            prgMetadataCon = new ProgenConnection();
            connection = ProgenConnection.getInstance().getConnection();
            finalQuery = buildQuery(getRelatedBussTablesQuery, Obj);
            preparedStatement = connection.prepareStatement(finalQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            PbReturnObject retObj1 = new PbReturnObject(resultSet);
            retObj1.writeString();
            alist.add(retObj1);//adding retaed tables
            alist.add(retObj);

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception ex) {
                    logger.error("Exception:", ex);
                }
            }
        }
        return alist;
    }

    //Function to Query for multiple business table //
    //Function to Query for multiple business table //
    public ArrayList viewBussData(String orgBussTableId, ArrayList bussTableIds, String grpId) {
//        PbBussGrpResourceBundle resourceBundle = new PbBussGrpResourceBundle();
        ArrayList alist = new ArrayList();
        PbReturnObject retObj = null;
        Vector bussTableIdRlt = new Vector();
        Object[] Obj = null;
//        ProgenConnection prgMetadataCon = null;
//        String getALlLayerTableNamesQuery = getResourceBundle().getString("getALlLayerTableNames");
//        String getALlLayerColNamesQuery = getResourceBundle().getString("getALlLayerColNames");
        String getRelatedBussTablesQuery = getResourceBundle().getString("getRelatedBussTables");

//        String qryTable = null;
        StringBuilder qryTable = new StringBuilder();
        for (int i = 0; i < bussTableIds.size(); i++) {
            if (qryTable == null) {
//                qryTable = bussTableIds.get(i).toString();
                qryTable.append( bussTableIds.get(i).toString());
                bussTableIdRlt.add(bussTableIds.get(i).toString());
            } else {
                qryTable.append( ",").append( bussTableIds.get(i).toString());
                bussTableIdRlt.add(bussTableIds.get(i).toString());
            }
        }

        String sqlstr = "Select BUSS_TABLE_ID, BUSS_TABLE_NAME  , DB_TABLE_NAME  , SOURCE_TABLE_NAME  , NO_OF_NODES  from PRG_GRP_BUSS_TABLE_MASTER_VIEW where BUSS_TABLE_ID in (" + qryTable + ") ";
        String[] srcTabName;
        String[] bussTabName;
        String[] dbTabName;

        // String srcColName = "";
        //String bussColName = "";
        //String dbColName = "";
        String[] colNames = null;

        //String sqlStr = "";
        String finalQuery = "";
//        String node1 = null;
        StringBuilder node1 = new StringBuilder();
        ArrayList node2 = new ArrayList();

        Connection connection = null;
        try {
            Obj = new Object[1];
            Obj[0] = bussTableIds;
            //finalQuery = buildQuery(getALlLayerTableNamesQuery, Obj);
            finalQuery = sqlstr;
            if (isDebugEnable) {
                if (isDebugEnable) {
                    logger.info("query fired inside try block" + System.currentTimeMillis());
                }
            }
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            int rowCnt = retObj.getRowCount();
            HashMap businessTable = new HashMap();
            HashMap dbTable = new HashMap();
            srcTabName = new String[rowCnt];
            bussTabName = new String[rowCnt];
            dbTabName = new String[rowCnt];
            if (retObj != null && retObj.getRowCount() != 0) {
                for (int i = 0; i < rowCnt; i++) {
                    bussTabName[i] = retObj.getFieldValueString(i, colNames[1]);
                    srcTabName[i] = retObj.getFieldValueString(i, colNames[3]);
                    dbTabName[i] = retObj.getFieldValueString(i, colNames[2]);
                    if (retObj.getFieldValueInt(i, colNames[4]) == 1) {
                        if (i == 0) {
//                            node1 = retObj.getFieldValueString(i, colNames[0]);
                            node1.append( retObj.getFieldValueString(i, colNames[0]));
                        } else {
//                            node1 += "," + retObj.getFieldValueString(i, colNames[0]);
                            node1.append(",").append( retObj.getFieldValueString(i, colNames[0]));
                        }
                    } else {
                        node2.add(retObj.getFieldValue(0, colNames[0]).toString());
                    }

                    businessTable.put(retObj.getFieldValueString(i, colNames[0]), retObj.getFieldValueString(i, colNames[1]));
                    dbTable.put(retObj.getFieldValueString(i, colNames[0]), retObj.getFieldValueString(i, colNames[2]));//modified on 3rd sep
                }
            }

            sqlstr = "Select BUSS_TABLE_ID , BUSINESS_COL_NAME   , BUSINESS_DISP_NAME  , SCR_COLUMN_NAME  , TABLE_COL_NAME  , count(TABLE_COL_NAME) over(PARTITION by BUSS_TABLE_ID )  COLCNT  from PRG_GRP_BUSS_TABLE_DTLS_VIEW where BUSS_TABLE_ID in (" + qryTable + ")  order by BUSS_TABLE_ID ";

            Obj = new Object[1];
            Obj[0] = bussTableIds;
            //finalQuery = buildQuery(getALlLayerColNamesQuery, Obj);
            finalQuery = sqlstr;
            if (isDebugEnable) {
                if (isDebugEnable) {
                    logger.info("query fired" + System.currentTimeMillis());
                }
            }
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();

            String dbType = getBussTableConnectionDbType(orgBussTableId);
//            String osql = "";
            StringBuilder osql = new StringBuilder(300);
            if (dbType.equalsIgnoreCase("ORACLE")) {
//                osql = "select ";
                osql.append("select ");
            } else if (dbType.equalsIgnoreCase("SQL")) {
//                osql = "select TOP 200  ";
                osql.append("select TOP 200  ");
            } else if (dbType.equalsIgnoreCase("mysql")) {
//                osql = "select ";
                osql.append("select ");
            } else if (dbType.equalsIgnoreCase("EXCEL")) {
//                osql = "select ";
                osql.append("select ");
            }
            // String osql = "select ";
            String[] insql = new String[rowCnt];
            int j = 0;
            int breakat = 0;
            int breakat1 = 0;

            String bussTableL = "";
            String dbTableL = "";

            int psize = retObj.getRowCount();
            if (psize > 0) {
                for (int looper = 0; looper < psize; looper++) {
                    // columnNames[looper] = retObj.getFieldValueString(looper,"DISPLAY_NAME");
                    // tableColumnsNames[looper] =retObj.getFieldValueString(looper,"COLUMN_NAME").toUpperCase();
                    if (looper == 0) {
                        breakat += retObj.getFieldValueInt(looper, colNames[5]);
                        breakat1 = breakat;
                        bussTableL = (String) businessTable.get(retObj.getFieldValue(looper, colNames[0]).toString());
                        dbTableL = (String) dbTable.get(retObj.getFieldValue(looper, colNames[0]).toString());
                        j = 0;
                        insql[j] = " ( Select ";
                    } else if (looper == breakat) {
                        insql[j] += " from  " + dbTableL + " )  " + bussTableL + " ";
                        breakat1 = breakat;
                        breakat += retObj.getFieldValueInt(looper, colNames[5]);
                        bussTableL = (String) businessTable.get(retObj.getFieldValue(looper, colNames[0]).toString());
                        dbTableL = (String) dbTable.get(retObj.getFieldValue(looper, colNames[0]).toString());
                        j++;
                        insql[j] = " , ( Select ";
                    }
                    if (looper == 0) {
//                        osql += " " + bussTableL + "." + retObj.getFieldValueString(looper, colNames[1]) + "  as  \"" + retObj.getFieldValueString(looper, colNames[3]).toUpperCase() + "\" ";
                        osql.append(" ").append(bussTableL).append(".").append(retObj.getFieldValueString(looper, colNames[1])).append("  as  \"").append(retObj.getFieldValueString(looper, colNames[3]).toUpperCase()).append("\" ");
                        insql[j] += " " + dbTableL + "." + retObj.getFieldValueString(looper, colNames[2]) + "  as  " + retObj.getFieldValueString(looper, colNames[1]).toUpperCase() + " ";
                    } else if (looper != 0 && looper == breakat1) {
//                        osql += " , " + bussTableL + "." + retObj.getFieldValueString(looper, colNames[1]) + "  as  \"" + retObj.getFieldValueString(looper, colNames[3]).toUpperCase() + "\" ";
                        osql.append(" , ").append(bussTableL).append(".").append(retObj.getFieldValueString(looper, colNames[1])).append("  as  \"").append(retObj.getFieldValueString(looper, colNames[3]).toUpperCase()).append("\" ");
                        insql[j] += " " + dbTableL + "." + retObj.getFieldValueString(looper, colNames[2]) + "  as  " + retObj.getFieldValueString(looper, colNames[1]).toUpperCase() + " ";

                    } else {
//                        osql += " ," + bussTableL + "." + retObj.getFieldValueString(looper, colNames[1]) + "   as \"" + retObj.getFieldValueString(looper, colNames[3]).toUpperCase() + "\" ";
                        osql.append(" ,").append(bussTableL).append(".").append(retObj.getFieldValueString(looper, colNames[1])).append("   as \"").append(retObj.getFieldValueString(looper, colNames[3]).toUpperCase()).append("\" ");
                        insql[j] += " , " + dbTableL + "." + retObj.getFieldValueString(looper, colNames[2]) + "  as  " + retObj.getFieldValueString(looper, colNames[1]).toUpperCase() + " ";
                    }
                }
                insql[j] += " from   " + dbTableL + " )  " + bussTableL + " ";

                //Write code to add the table with two nodes or more nodes
                /**
                 * Missing Code
                 *
                 */
//                osql += " from  ";
                osql.append(" from  ");
                for (int i = 0; i < insql.length; i++) {
//                    osql += insql[i].toString() + " ";
                    osql.append(insql[i].toString()).append(" ");
                }

                ///Get Join Clause
//                String whereClause = "";
                StringBuilder whereClause = new StringBuilder();
                {
                    sqlstr = "Select ACTUAL_CLAUSE  , BUSS_TABLE_ID  , BUSS_TABLE_ID2  from PRG_GRP_BUSS_TABLE_RLT_MASTER where BUSS_TABLE_ID in (" + qryTable + ")  OR BUSS_TABLE_ID2 in (" + qryTable + ") ";
                    finalQuery = sqlstr;
                    if (isDebugEnable) {
                        if (isDebugEnable) {
                            logger.info("query fired" + System.currentTimeMillis());
                        }
                    }
                    retObj = execSelectSQL(finalQuery);
                    colNames = retObj.getColumnNames();//bussTableIdRlt

                    psize = retObj.getRowCount();
                    if (psize > 0) {
                        for (int looper = 0; looper < psize; looper++) {
                            if (bussTableIdRlt.contains(retObj.getFieldValueString(looper, colNames[1])) && bussTableIdRlt.contains(retObj.getFieldValueString(looper, colNames[2]))) {
//                                whereClause += " and " + retObj.getFieldValue(looper, colNames[0]).toString();
                                whereClause.append(" and " ).append( retObj.getFieldValue(looper, colNames[0]).toString());
                            }

                        }
                    }

                }

//                osql += " where 1=1 ";
                osql.append(" where 1=1 ");
                if (!("".equals(whereClause))) {
//                    osql += "   " + whereClause;
                    osql.append("   ").append(whereClause);
                } else {
//                    osql += " and   1=1 ";
                    osql.append(" and   1=1 ");
                }

                if (dbType.equalsIgnoreCase("ORACLE")) {
//                    osql += " and  rownum<200 ";
                    osql.append(" and  rownum<200 ");
                } else if (dbType.equalsIgnoreCase("SQL")) {
                    osql = osql;

                } else if (dbType.equalsIgnoreCase("mysql")) {
//                    osql += " LIMIT 200 ";
                    osql.append(" LIMIT 200 ");
                }

                // osql += " where rownum <=100 ";
            }
            // com.progen.datadisplay.db.PbDataDisplayBeanDb ddbd = new com.progen.datadisplay.db.PbDataDisplayBeanDb();
            connection = getBussTableConnection(orgBussTableId);
            PreparedStatement preparedStatement1 = connection.prepareStatement(osql.toString());
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            retObj = new PbReturnObject(resultSet1);
            // retObj = execSelectSQL(osql, connection);
            Obj = new Object[3];
            Obj[0] = orgBussTableId;
            Obj[1] = orgBussTableId;
            Obj[2] = grpId;
            PreparedStatement preparedStatement = null;
//            prgMetadataCon = new ProgenConnection();
            connection = ProgenConnection.getInstance().getConnection();
            finalQuery = buildQuery(getRelatedBussTablesQuery, Obj);
            preparedStatement = connection.prepareStatement(finalQuery);
            if (isDebugEnable) {
                logger.info("query fired" + System.currentTimeMillis());
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            PbReturnObject retObj1 = new PbReturnObject(resultSet);
            retObj1.writeString();
            alist.add(retObj1);//adding retaed tables
            alist.add(retObj);

        } catch (SQLException exp) {
            logger.error("Exception:", exp);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception ex) {
                    logger.error("Exception:", ex);
                }
            }
        }
        return alist;
    }

    //Function to Query for multiple business tableviewBussDataWithouCol //
    public String viewBussDataWithouCol(ArrayList bussTableIds) {
//        ArrayList alist = new ArrayList();
        PbReturnObject retObj = null;
        Vector bussTableIdRlt = new Vector();
        FinalBussTableIdRlt = new ArrayList();
        Object[] Obj = null;
//        String getALlLayerTableNamesQuery = getResourceBundle().getString("getALlLayerTableNames");
//        String getALlLayerColNamesQuery = getResourceBundle().getString("getALlLayerColNames");
        String osql = "";
//        CLOB clb;
//        String qryTable = null;
        StringBuilder qryTable = new StringBuilder();
        boolean isProgenTimeDimThere = false;
//        String ansiFromClause = "";
        StringBuilder ansiFromClause = new StringBuilder();
        String progenTimeDimId = "";
        for (int i = 0; i < bussTableIds.size(); i++) {
            if (qryTable.length()==0) {
//                qryTable = bussTableIds.get(i).toString();
                qryTable.append( bussTableIds.get(i).toString());
                bussTableIdRlt.add(bussTableIds.get(i).toString().trim());
            } else {
//                qryTable += "," + bussTableIds.get(i).toString();
                qryTable.append("," ).append( bussTableIds.get(i).toString());
//                qryTable.trim();
                bussTableIdRlt.add(bussTableIds.get(i).toString().trim());
            }
        }
        if (qryTable.length() > 0) {
            int length = qryTable.length();
            char lastchar = qryTable.charAt(length - 1);
            if (lastchar == ',') {
                qryTable =new StringBuilder( qryTable.substring(0, length - 1));
            }
        }else{
          qryTable =new StringBuilder("null");
        }
        String sqlstr = " ";
        sqlstr += "  SELECT   pbgts.BUSS_TABLE_ID      ,    ";
        sqlstr += "   pgbt.BUSS_TABLE_NAME              ,           ";
        sqlstr += "     pdmt.table_name as DB_TABLE_NAME,         ";
        sqlstr += "   pbgts.db_table_name AS SOURCE_TABLE_NAME,   ";
        sqlstr += "    pgbt.NO_OF_NODES                  ,          ";
        sqlstr += "   nvl(pgbt.IS_PURE_QUERY,'N')                ,           ";
        sqlstr += "   nvl(pbgts.db_query,pgbt.db_query)  ,                     ";
        sqlstr += "   pgbt.DB_LEVEL_HINT                            ";
        sqlstr += "    FROM PRG_GRP_BUSS_TABLE pgbt,                ";
        sqlstr += "   PRG_GRP_BUSS_TABLE_SRC pbgts,                 ";
        sqlstr += "   prg_db_master_table pdmt                      ";
        sqlstr += "   WHERE pgbt.buss_table_id=pbgts.buss_table_id  ";
        sqlstr += "   and pdmt.table_id(+)=pbgts.DB_TABLE_ID and  pgbt.BUSS_TABLE_ID in (" + qryTable + ")       ";
        sqlstr += " ORDER BY pbgts.buss_table_id   ";

        if (!ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
            sqlstr = " ";
            sqlstr += "  SELECT   pbgts.BUSS_TABLE_ID      ,    ";
            sqlstr += "   pgbt.BUSS_TABLE_NAME              ,           ";
            sqlstr += "     pdmt.table_name as DB_TABLE_NAME,         ";
            sqlstr += "   pbgts.db_table_name AS SOURCE_TABLE_NAME,   ";
            sqlstr += "    pgbt.NO_OF_NODES                  ,          ";
            sqlstr += "   COALESCE(pgbt.IS_PURE_QUERY,'N')  IS_QRY               ,           ";
            sqlstr += "   COALESCE(pbgts.db_query,pgbt.db_query)  QRY_STR,                     ";
            sqlstr += "   pgbt.DB_LEVEL_HINT                            ";
            sqlstr += "    FROM                 ";
            sqlstr += "   PRG_GRP_BUSS_TABLE_SRC pbgts left outer join                  ";
            sqlstr += "   prg_db_master_table pdmt on (pdmt.table_id =pbgts.DB_TABLE_ID)                     ";
            sqlstr += "    inner join PRG_GRP_BUSS_TABLE pgbt on (pgbt.buss_table_id=pbgts.buss_table_id)                ";
            sqlstr += "   where  pgbt.BUSS_TABLE_ID in (" + qryTable + ")       ";
            sqlstr += " ORDER BY pbgts.buss_table_id   ";

        }

        ////.println("amit sqlstr=" + sqlstr);
        String[] srcTabName;
        String[] bussTabName;
        String[] dbTabName;
//        String fromClause = null;
        StringBuilder fromClause = new StringBuilder();

        // String srcColName = "";
        //String bussColName = "";
        //String dbColName = "";
        String[] colNames = null;

        //String sqlStr = "";
        String finalQuery = "";
//        String node1 = null;
        StringBuilder node1 = new StringBuilder();
        ArrayList node2 = new ArrayList();

        Connection connection = null;
        try {
            Obj = new Object[1];
            Obj[0] = bussTableIds;
            //finalQuery = buildQuery(getALlLayerTableNamesQuery, Obj);
            finalQuery = sqlstr;
//            System.out.println("finalQuery new is inviewsource "+finalQuery);

            if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "viewBussDataWithouCol", "query fired inside try block"+System.currentTimeMillis());
            {
                logger.info("query fired inside try block" + System.currentTimeMillis());
            }
            retObj = execSelectSQL(finalQuery);

            colNames = retObj.getColumnNames();
            int rowCnt = retObj.getRowCount();
            HashMap businessTable = new HashMap();
            HashMap<String, String> businessTableDeatils = new HashMap<String, String>();
            HashMap dbTable = new HashMap();
            HashMap dbQuery = new HashMap();

            srcTabName = new String[rowCnt];
            bussTabName = new String[rowCnt];
            dbTabName = new String[rowCnt];
            if (retObj != null && retObj.getRowCount() != 0) {
                for (int i = 0; i < rowCnt; i++) {
                    bussTabName[i] = retObj.getFieldValueString(i, colNames[1]);
                    srcTabName[i] = retObj.getFieldValueString(i, colNames[3]);
                    if (retObj.getFieldValueString(i, colNames[5]).equalsIgnoreCase("N")) {
                        dbTabName[i] = retObj.getFieldValueString(i, colNames[2]);
                    } else {

                        dbTabName[i] = " ( " + retObj.getFieldUnknown(i, 6) + " ) ";
                    }
                    if (retObj.getFieldValueInt(i, colNames[4]) == 1) {
                        if (i == 0) {
//                            node1 = retObj.getFieldValueString(i, colNames[0]);
                            node1.append( retObj.getFieldValueString(i, colNames[0]));
                        } else {
//                            node1 += "," + retObj.getFieldValueString(i, colNames[0]);
                            node1.append(",").append( retObj.getFieldValueString(i, colNames[0]));
                        }
                    } else {
                        node2.add(retObj.getFieldValue(0, colNames[0]).toString());
                    }
                    if (retObj.getFieldValueString(i, colNames[1]).toString().toUpperCase().equals("PROGEN_TIME")) {
                        isProgenTimeDimThere = true;
                        progenTimeDimId = retObj.getFieldValueString(i, colNames[0]);
                    }
                    businessTable.put(retObj.getFieldValueString(i, colNames[0]), retObj.getFieldValueString(i, colNames[1]));
                    if (retObj.getFieldValueString(i, colNames[5]).equalsIgnoreCase("N")) {
                        dbTable.put(retObj.getFieldValueString(i, colNames[0]), retObj.getFieldValueString(i, colNames[2]));
                        dbQuery.put(retObj.getFieldValueString(i, colNames[0]), "N");
                        if (fromClause == null) {
                            if ((altFact == null || altFact.get(retObj.getFieldValueString(i, colNames[1])) == null)) {
//                                fromClause = " " + retObj.getFieldValueString(i, colNames[2]) + " " + retObj.getFieldValueString(i, colNames[1]);
                                fromClause.append(" ").append( retObj.getFieldValueString(i, colNames[2]) ).append( " " ).append( retObj.getFieldValueString(i, colNames[1]));
                                businessTableDeatils.put(retObj.getFieldValueString(i, colNames[0]), " " + retObj.getFieldValueString(i, colNames[2]) + " " + retObj.getFieldValueString(i, colNames[1]) + " ");
                            } else {
//                                fromClause = " " + altFact.get(retObj.getFieldValueString(i, colNames[1])) + " " + retObj.getFieldValueString(i, colNames[1]);
                                fromClause.append( " " ).append( altFact.get(retObj.getFieldValueString(i, colNames[1])) ).append( " " ).append( retObj.getFieldValueString(i, colNames[1]));
                                businessTableDeatils.put(retObj.getFieldValueString(i, colNames[0]), " " + altFact.get(retObj.getFieldValueString(i, colNames[1])) + " " + retObj.getFieldValueString(i, colNames[1]) + " ");
                            }
                        } else {
                            if (altFact == null || altFact.get(retObj.getFieldValueString(i, colNames[1])) == null) {
//                                fromClause += " , " + retObj.getFieldValueString(i, colNames[2]) + " " + retObj.getFieldValueString(i, colNames[1]);
                                fromClause.append(" , " ).append( retObj.getFieldValueString(i, colNames[2]) ).append( " " ).append( retObj.getFieldValueString(i, colNames[1]));
                                businessTableDeatils.put(retObj.getFieldValueString(i, colNames[0]), " " + retObj.getFieldValueString(i, colNames[2]) + " " + retObj.getFieldValueString(i, colNames[1]) + " ");

                            } else {
                                fromClause.append( " , " ).append( altFact.get(retObj.getFieldValueString(i, colNames[1])) ).append( " " ).append( retObj.getFieldValueString(i, colNames[1]));
                                businessTableDeatils.put(retObj.getFieldValueString(i, colNames[0]), " " + altFact.get(retObj.getFieldValueString(i, colNames[1])) + " " + retObj.getFieldValueString(i, colNames[1]) + " ");

                            }
                        }

                    } else {

                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            ////.println("retObj.getFieldUnknown(i, 6) "+retObj.getFieldUnknown(i, 6) );
                            dbTable.put(retObj.getFieldValueString(i, colNames[0]), " ( " + retObj.getFieldUnknown(i, 6) + ") ");
                        } else {
                            dbTable.put(retObj.getFieldValueString(i, colNames[0]), " ( " + retObj.getFieldValueClobString(i, colNames[6]) + ") ");
                        }

                        dbQuery.put(retObj.getFieldValueString(i, colNames[0]), "Y");

                        if (fromClause == null) {
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
//                                fromClause = " ( " + retObj.getFieldUnknown(i, 6) + ") " + " " + retObj.getFieldValueString(i, colNames[1]);
                                fromClause.append(  " ( " ).append( retObj.getFieldUnknown(i, 6) ).append( ")   " ).append( retObj.getFieldValueString(i, colNames[1]));
                                businessTableDeatils.put(retObj.getFieldValueString(i, colNames[0]), " " + " ( " + retObj.getFieldUnknown(i, 6) + ") " + " " + retObj.getFieldValueString(i, colNames[1]) + " ");

                            } else {
//                                fromClause = " ( " + retObj.getFieldValueClobString(i, colNames[6]) + ") " + " " + retObj.getFieldValueString(i, colNames[1]);
                                fromClause.append(" ( " ).append( retObj.getFieldValueClobString(i, colNames[6]) ).append( ") " ).append( retObj.getFieldValueString(i, colNames[1]));
                                businessTableDeatils.put(retObj.getFieldValueString(i, colNames[0]), " " + " ( " + retObj.getFieldValueClobString(i, colNames[6]) + ") " + " " + retObj.getFieldValueString(i, colNames[1]) + " ");

                            }

                        } else {

                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
//                                fromClause += " , ( " + retObj.getFieldUnknown(i, 6) + ") " + " " + retObj.getFieldValueString(i, colNames[1]);
                                fromClause.append( " , ( " ).append( retObj.getFieldUnknown(i, 6) + ")  " ).append( retObj.getFieldValueString(i, colNames[1]));
                                businessTableDeatils.put(retObj.getFieldValueString(i, colNames[0]), " " + " ( " + retObj.getFieldUnknown(i, 6) + ") " + " " + retObj.getFieldValueString(i, colNames[1]) + " ");

                            } else {

//                                fromClause += " , ( " + retObj.getFieldValueClobString(i, colNames[6]) + ") " + " " + retObj.getFieldValueString(i, colNames[1]);
                                fromClause.append( " , ( " ).append( retObj.getFieldValueClobString(i, colNames[6]) ).append( ") " ).append( retObj.getFieldValueString(i, colNames[1]));
                                businessTableDeatils.put(retObj.getFieldValueString(i, colNames[0]), " " + " ( " + retObj.getFieldValueClobString(i, colNames[6]) + ") " + " " + retObj.getFieldValueString(i, colNames[1]) + " ");

                            }

                        }

                    }

                }

            }

            /*
             * sqlstr = "Select distinct BUSS_TABLE_ID"; sqlstr += " ,
             * BUSINESS_COL_NAME "; sqlstr += " , BUSINESS_DISP_NAME "; sqlstr
             * += " , SCR_COLUMN_NAME "; sqlstr += " , TABLE_COL_NAME "; sqlstr
             * += " , count( distinct TABLE_COL_NAME) over(PARTITION by
             * BUSS_TABLE_ID ) COLCNT "; sqlstr += " from
             * PRG_GRP_BUSS_TABLE_DTLS_VIEW where BUSS_TABLE_ID in (" + qryTable
             * + ") "; sqlstr += " order by BUSS_TABLE_ID ";
             *
             * Obj = new Object[1]; Obj[0] = bussTableIds; //finalQuery =
             * buildQuery(getALlLayerColNamesQuery, Obj); finalQuery = sqlstr;
             * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("finalQuery
             * is "+finalQuery); retObj = execSelectSQL(finalQuery); colNames =
             * retObj.getColumnNames();
             *
             * osql = "select "; String[] insql = new String[rowCnt]; int j = 0;
             * int breakat = 0; int breakat1 = 0;
             *
             * String bussTableL =""; String dbTableL =""; String dbQueryL ="";
             *
             * int psize = retObj.getRowCount(); if (psize > 0) { for (int
             * looper = 0; looper < psize; looper++) { // columnNames[looper] =
             * retObj.getFieldValueString(looper,"DISPLAY_NAME"); //
             * tableColumnsNames[looper]
             * =retObj.getFieldValueString(looper,"COLUMN_NAME").toUpperCase();
             * if (looper == 0) { breakat += retObj.getFieldValueInt(looper,
             * colNames[5]); breakat1 = breakat; bussTableL = (String)
             * businessTable.get(retObj.getFieldValue(looper,
             * colNames[0]).toString()); dbTableL = (String)
             * dbTable.get(retObj.getFieldValue(looper,
             * colNames[0]).toString());
             * dbQueryL=(String)dbQuery.get(retObj.getFieldValue(looper,
             * colNames[0]).toString());
             *
             * j = 0; insql[j] = " ( Select "; //
             * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("insql["+j+"]
             * is "+insql[j]); } else if (looper == breakat) { insql[j] += "
             * from " + dbTableL + " ) " + bussTableL + " "; breakat1 = breakat;
             * breakat += retObj.getFieldValueInt(looper, colNames[5]);
             * bussTableL = (String)
             * businessTable.get(retObj.getFieldValue(looper,
             * colNames[0]).toString()); dbTableL = (String)
             * dbTable.get(retObj.getFieldValue(looper,
             * colNames[0]).toString());
             * dbQueryL=(String)dbQuery.get(retObj.getFieldValue(looper,
             * colNames[0]).toString());
             *
             * j++; insql[j] = " , ( Select "; //
             * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("insql["+j+"]
             * is "+insql[j]); } if (looper == 0) { //osql += " " + bussTableL +
             * "." + retObj.getFieldValueString(looper, colNames[1]) + " as \""
             * + retObj.getFieldValueString(looper, colNames[3]).toUpperCase() +
             * "\" "; if (dbQueryL.equalsIgnoreCase("N") ) { insql[j] += " " +
             * dbTableL + "." + retObj.getFieldValueString(looper, colNames[2])
             * + " as " + retObj.getFieldValueString(looper,
             * colNames[1]).toUpperCase() + " "; } else { insql[j] += " " +
             * retObj.getFieldValueString(looper, colNames[1]).toUpperCase() + "
             * "; } //
             * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("insql["+j+"]
             * is "+insql[j]); } else if (looper != 0 && looper == breakat1) {
             * // osql += " , " + bussTableL + "." +
             * retObj.getFieldValueString(looper, colNames[1]) + " as \"" +
             * retObj.getFieldValueString(looper, colNames[3]).toUpperCase() +
             * "\" "; if (dbQueryL.equalsIgnoreCase("N")) { insql[j] += " " +
             * dbTableL + "." + retObj.getFieldValueString(looper, colNames[2])
             * + " as " + retObj.getFieldValueString(looper,
             * colNames[1]).toUpperCase() + " "; } else { insql[j] += " " +
             * retObj.getFieldValueString(looper, colNames[1]).toUpperCase() + "
             * "; } } else { //osql += " ," + bussTableL + "." +
             * retObj.getFieldValueString(looper, colNames[1]) + " as \"" +
             * retObj.getFieldValueString(looper, colNames[3]).toUpperCase() +
             * "\" "; if (dbQueryL.equalsIgnoreCase("N")) { insql[j] += " , " +
             * dbTableL + "." + retObj.getFieldValueString(looper, colNames[2])
             * + " as " + retObj.getFieldValueString(looper,
             * colNames[1]).toUpperCase() + " "; } else { insql[j] += " , " +
             * retObj.getFieldValueString(looper, colNames[1]).toUpperCase() + "
             * "; }
             *
             *
             * //
             * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("insql["+j+"]
             * is "+insql[j]); } } insql[j] += " from " + dbTableL + " ) " +
             * bussTableL + " "; //
             * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("insql["
             * + j + "] is " + insql[j]);
             *
             * //Write code to add the table with two nodes or more nodes osql =
             * " from ";//removed += as we don't need columns for (int i = 0; i
             * < insql.length; i++) { osql += insql[i].toString() + " ";
             * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("insql"
             * + i + "--" + insql[i].toString()); }
             *
             * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(osql);
             * }
             *
             * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(osql);
             }
             */
            int iloop = 0;
            osql = " from  ";
            osql += fromClause;//removed += as we don't need columns
            ////.println("From clause "+osql);

            StringBuilder whereClause = new StringBuilder();
//            String whereClause = "";
            {
                sqlstr = "Select ACTUAL_CLAUSE";
                sqlstr += " , BUSS_TABLE_ID ";
                sqlstr += " , BUSS_TABLE_ID2 , case when clause_type is null then ";
                sqlstr += " 'inner' when clause_type = '' then 'inner' else clause_type end clause_type ";
                sqlstr += " from PRG_GRP_BUSS_TABLE_RLT_MASTER where BUSS_TABLE_ID in (" + qryTable + ") ";
                sqlstr += " OR BUSS_TABLE_ID2 in (" + qryTable + ") ";

                finalQuery = sqlstr;
                if (isDebugEnable) {
                    logger.info("query fired" + System.currentTimeMillis());
                }
                retObj = execSelectSQL(finalQuery);
                colNames = retObj.getColumnNames();//bussTableIdRlt
                ArrayList<String> bussTableUsed = new ArrayList<String>();
                ArrayList<String> bussCombinationUsed = new ArrayList<String>();
                int psize = retObj.getRowCount();

                if (psize > 0) {
                    for (int looper = 0; looper < psize; looper++) {
                        if (bussTableIdRlt.contains(retObj.getFieldValueString(looper, colNames[1])) && bussTableIdRlt.contains(retObj.getFieldValueString(looper, colNames[2]))) {
                            {
                                String str = retObj.getFieldValueString(looper, colNames[0]).toString();
                                if (str != null && !str.equalsIgnoreCase("null") && !str.equalsIgnoreCase("")) {
//                                    whereClause += " and " + retObj.getFieldValue(looper, colNames[0]).toString();
                                    whereClause.append("  and ").append( retObj.getFieldValue(looper, colNames[0]).toString());
                                    if (iloop == 0) {
//                                        ansiFromClause = " from " + businessTableDeatils.get(retObj.getFieldValueString(looper, colNames[1]));
                                        ansiFromClause.append( " from ").append( businessTableDeatils.get(retObj.getFieldValueString(looper, colNames[1])));
                                        if (retObj.getFieldValueString(looper, colNames[3]).equals("inner")) {
//                                            ansiFromClause += " inner join " + businessTableDeatils.get(retObj.getFieldValueString(looper, colNames[2])) + " ";
                                            ansiFromClause.append( " inner join " ).append( businessTableDeatils.get(retObj.getFieldValueString(looper, colNames[2])) ).append( " ");
//                                            ansiFromClause += " on  ( " + retObj.getFieldValue(looper, colNames[0]).toString() + " ) ";
                                            ansiFromClause.append(" on  ( " ).append( retObj.getFieldValue(looper, colNames[0]).toString() ).append( " ) ");
                                        } else if (retObj.getFieldValueString(looper, colNames[3]).equals("left outer")) {
//                                            ansiFromClause += " left outer join " + businessTableDeatils.get(retObj.getFieldValueString(looper, colNames[2])) + " ";
                                            ansiFromClause.append(  " left outer join " ).append( businessTableDeatils.get(retObj.getFieldValueString(looper, colNames[2])) ).append( " ");
//                                            ansiFromClause += " on  ( " + retObj.getFieldValue(looper, colNames[0]).toString() + "  ) ";
                                            ansiFromClause.append( " on  ( " ).append( retObj.getFieldValue(looper, colNames[0]).toString() ).append( "  ) ");
                                        } else if (retObj.getFieldValueString(looper, colNames[3]).equals("right outer")) {
//                                            ansiFromClause += " right outer join " + businessTableDeatils.get(retObj.getFieldValueString(looper, colNames[2])) + " ";
                                            ansiFromClause.append( " right outer join " ).append( businessTableDeatils.get(retObj.getFieldValueString(looper, colNames[2])) ).append( " ");
//                                            ansiFromClause += " on  ( " + retObj.getFieldValue(looper, colNames[0]).toString() + " ) ";
                                            ansiFromClause.append( " on  ( " ).append( retObj.getFieldValue(looper, colNames[0]).toString()).append(" ) ");

                                        }
                                        bussTableUsed.add(retObj.getFieldValueString(looper, colNames[1]));
                                        bussTableUsed.add(retObj.getFieldValueString(looper, colNames[2]));
                                    } else {

                                        if (bussTableUsed.contains(retObj.getFieldValueString(looper, colNames[1]))
                                                && bussTableUsed.contains(retObj.getFieldValueString(looper, colNames[2]))) {
                                            ansiFromClause.append(" ");
                                        } else if (bussTableUsed.contains(retObj.getFieldValueString(looper, colNames[1]))
                                                && !bussTableUsed.contains(retObj.getFieldValueString(looper, colNames[2]))) {

                                            if (retObj.getFieldValueString(looper, colNames[3]).equals("inner")) {
//                                                ansiFromClause += " inner join " + businessTableDeatils.get(retObj.getFieldValueString(looper, colNames[2])) + " ";
                                                ansiFromClause.append( " inner join " ).append( businessTableDeatils.get(retObj.getFieldValueString(looper, colNames[2]))).append( " ");
//                                                ansiFromClause += " on  ( " + retObj.getFieldValue(looper, colNames[0]).toString() + " ) ";
                                                ansiFromClause.append( " on  ( " ).append( retObj.getFieldValue(looper, colNames[0]).toString()).append( " ) ");
                                            } else if (retObj.getFieldValueString(looper, colNames[3]).equals("left outer")) {
//                                                ansiFromClause += " left outer join " + businessTableDeatils.get(retObj.getFieldValueString(looper, colNames[2])) + " ";
                                                ansiFromClause.append( " left outer join " ).append( businessTableDeatils.get(retObj.getFieldValueString(looper, colNames[2])) ).append( " ");
//                                                ansiFromClause += " on  ( " + retObj.getFieldValue(looper, colNames[0]).toString() + "  ) ";
                                                ansiFromClause.append(" on  ( " ).append( retObj.getFieldValue(looper, colNames[0]).toString() ).append("  ) ");
                                            } else if (retObj.getFieldValueString(looper, colNames[3]).equals("right outer")) {
//                                                ansiFromClause += " right outer join " + businessTableDeatils.get(retObj.getFieldValueString(looper, colNames[2])) + " ";
                                                ansiFromClause.append(" right outer join " ).append( businessTableDeatils.get(retObj.getFieldValueString(looper, colNames[2])) ).append( " ");
//                                                ansiFromClause += " on  ( " + retObj.getFieldValue(looper, colNames[0]).toString() + " ) ";
                                                ansiFromClause.append( " on  ( " ).append( retObj.getFieldValue(looper, colNames[0]).toString()).append( " ) ");

                                            }

                                            bussTableUsed.add(retObj.getFieldValueString(looper, colNames[2]));

                                        } else {

                                            if (retObj.getFieldValueString(looper, colNames[3]).equals("inner")) {
//                                                ansiFromClause += " inner join " + businessTableDeatils.get(retObj.getFieldValueString(looper, colNames[1])) + " ";
                                                ansiFromClause.append( " inner join " ).append( businessTableDeatils.get(retObj.getFieldValueString(looper, colNames[1])) ).append( " ");
//                                                ansiFromClause += " on  ( " + retObj.getFieldValue(looper, colNames[0]).toString() + " )";
                                                ansiFromClause.append( " on  ( " ).append(retObj.getFieldValue(looper, colNames[0]).toString()).append(" )");
                                            } else if (retObj.getFieldValueString(looper, colNames[3]).equals("right outer")) {
//                                                ansiFromClause += " left outer join " + businessTableDeatils.get(retObj.getFieldValueString(looper, colNames[1])) + "  ";
                                                ansiFromClause.append(  " left outer join " ).append( businessTableDeatils.get(retObj.getFieldValueString(looper, colNames[1])) ).append( "  ");
//                                                ansiFromClause += " on  ( " + retObj.getFieldValue(looper, colNames[0]).toString() + " ) ";
                                                ansiFromClause.append( " on  ( " ).append( retObj.getFieldValue(looper, colNames[0]).toString() ).append(" ) ");
                                            } else if (retObj.getFieldValueString(looper, colNames[3]).equals("left outer")) {
//                                                ansiFromClause += " right outer join " + businessTableDeatils.get(retObj.getFieldValueString(looper, colNames[1])) + "  ";
                                                ansiFromClause.append(" right outer join " ).append( businessTableDeatils.get(retObj.getFieldValueString(looper, colNames[1])) ).append( "  ");
//                                                ansiFromClause += " on  ( " + retObj.getFieldValue(looper, colNames[0]).toString() + " ) ";
                                                ansiFromClause.append( " on  ( " ).append( retObj.getFieldValue(looper, colNames[0]).toString() ).append( " ) ");

                                            }

                                            bussTableUsed.add(retObj.getFieldValueString(looper, colNames[1]));

                                        }

                                    }
                                    iloop++;
                                }
                            }
                            if (FinalBussTableIdRlt.isEmpty() || (!FinalBussTableIdRlt.contains(retObj.getFieldValueString(looper, colNames[1])))) {
                                FinalBussTableIdRlt.add(retObj.getFieldValueString(looper, colNames[1]));
                            }
                            if (FinalBussTableIdRlt.isEmpty() || (!FinalBussTableIdRlt.contains(retObj.getFieldValueString(looper, colNames[2])))) {
                                FinalBussTableIdRlt.add(retObj.getFieldValueString(looper, colNames[2]));
                            }

                        }

                    }
                }

            }
            if (iloop == 0) {
                 if (bussTableIds != null && !bussTableIds.isEmpty()) {
//                ansiFromClause = " from " + businessTableDeatils.get(bussTableIds.get(0)) + " ";
                ansiFromClause.append(" from " ).append( businessTableDeatils.get(bussTableIds.get(0)) ).append( " ");
            }
            }
            if (isProgenTimeDimThere) {
              
//                ansiFromClause += " inner join " + businessTableDeatils.get(progenTimeDimId) + " ";
                ansiFromClause.append( " inner join " ).append( businessTableDeatils.get(progenTimeDimId) ).append( " ");
          
            }
            if (istrendSupport) {
                osql += " , PR_DAY_DENOM PROGEN_TIME ";
            }
            if (isummSupport) {
                osql += " , (@@PROGEN_TIME_SUMM@@) summTab0001 "; //Can not be added here now
            }

            if (isCustomBucket) {
                if (!customBucketTablesql.equalsIgnoreCase("")) {
                    osql += " ,  " + customBucketTablesql;
                }
            }

            osql += " where 1=1 ";
            if (!(whereClause == null || whereClause.equals(""))) {
                osql += "   " + whereClause;
            }

        } catch (SQLException exp) {
            logger.error("Exception:", exp);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    logger.error("Exception:", ex);
                }
            }
        }
        return (ansiFromClause.toString());
    }

    //getBussTableConnDetails
    public Connection getBussTableConnection(String bussTableId) {
        Connection conn = null;
//        PbBussGrpResourceBundle resBundle = new PbBussGrpResourceBundle();
        try {
            String masterTableId = this.getMasterTableId(bussTableId);

            conn = ProgenConnection.getInstance().getConnectionByTable(masterTableId);
            return conn;
        } catch (Exception exp) {
            logger.error("Exception:", exp);
            return conn;
        }
    }

    public String getMasterTableId(String bussTableIds) {

//        PbBussGrpResourceBundle resBundle = new PbBussGrpResourceBundle();
        String masterTableId = null;
        PbReturnObject retObj = null;
        String masterTableQry = getResourceBundle().getString("getMasterTableIdDetails");
        String finalQuery = "";

        Object[] Obj = new Object[1];
        Obj[0] = bussTableIds;

        try {
            finalQuery = buildQuery(masterTableQry, Obj);
            if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "getMasterTaleId", "query fired inside try block"+System.currentTimeMillis());
            {
                logger.info("query fired inside try block" + System.currentTimeMillis());
            }
            retObj = execSelectSQL(finalQuery);

            // serverName = retObj.getFieldValueString(0,tableColumnNames[4]);
            masterTableId = retObj.getFieldValueString(0, 0);
        } catch (SQLException exp) {
            logger.error("Exception:", exp);
            masterTableId = "";
        }
        return masterTableId;
    }

    public String addTabsToSrc(String tableId, String grpId, String bussTabid) {
//        PbBussGrpResourceBundle resorceBundle = new PbBussGrpResourceBundle();
        PbReturnObject retObj = new PbReturnObject();
        String dbTblIdQry = getResourceBundle().getString("getDBTableId");
        Object[] buddIdObj = new Object[1];
        buddIdObj[0] = bussTabid;
        String newDbIdQry = buildQuery(dbTblIdQry, buddIdObj);
        PbReturnObject tempRetObj = new PbReturnObject();
        String[] tableColumnNames = null;
        StringBuffer myString = new StringBuffer();
        try {
            if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "addTabsToSrc", "query fired inside try block"+System.currentTimeMillis());
            {
                logger.info("query fired inside try block" + System.currentTimeMillis());
            }
            tempRetObj = execSelectSQL(newDbIdQry);
            for (int k = 0; k < tempRetObj.getRowCount(); k++) {
                tableId = tempRetObj.getFieldValueString(k, 0);
            }
            String query = getResourceBundle().getString("addTabsToSrc");
            Object[] ob = new Object[4];
            ob[0] = tableId;
            ob[1] = grpId;
            ob[2] = tableId;
            ob[3] = tableId;

            String changedQry = buildQuery(query, ob);
            if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "addTabsToSrc", "query fired"+System.currentTimeMillis());
            {
                logger.info("query fired" + System.currentTimeMillis());
            }
            retObj = execSelectSQL(changedQry);
            tableColumnNames = retObj.getColumnNames();
            String tempTabId = "";
            String colName = "";
            for (int i = 0; i < retObj.getRowCount(); i++) {
                if (tempTabId.equalsIgnoreCase("") || !(tempTabId.equalsIgnoreCase(retObj.getFieldValueString(i, 1)))) {
                    if (!(tempTabId.equalsIgnoreCase(""))) {
                        myString.append("</ul></li>");
                    }
                    myString.append("<li class='closed' id='srcTabLi-" + retObj.getFieldValueString(i, 1) + "'>");
                    myString.append("<span id='srcTabID-" + retObj.getFieldValueString(i, 1) + "' class='dragTableName'>" + retObj.getFieldValueString(i, 0) + "</span>");
                    myString.append("<ul id='srcTabUL-" + retObj.getFieldValueString(i, 1) + "'>");
                    tempTabId = retObj.getFieldValueString(i, 1);
                }
                colName = retObj.getFieldValueString(i, 3);
                if (colName != null) {
                    if (colName.length() > 0) {
                        colName = colName.toUpperCase();
                    }
                }
                myString.append("<li><span id='srcCol-" + retObj.getFieldValueString(i, 2) + "'>" + colName + "</span></li>");
                if (i == retObj.getRowCount() - 1) {
                    myString.append("</li></ul></li>");

                }
            }

        } catch (SQLException e) {
            logger.error("Exception:", e);
        }
        return myString.toString();

    }

    public String viewBussData(String bussTableId, String grpId, int i) {
        String osql = "select ";
//        PbBussGrpResourceBundle resBundle = new PbBussGrpResourceBundle();
        ArrayList alist = new ArrayList();
        PbReturnObject retObj = null;
        Object[] Obj = null;
//        ProgenConnection prgMetadataCon = null;
        String getALlLayerTableNamesQuery = getResourceBundle().getString("getALlLayerTableNames");
        String getALlLayerColNamesQuery = getResourceBundle().getString("getALlLayerColNames");
        String getRelatedBussTablesQuery = getResourceBundle().getString("getRelatedBussTables");
        String srcTabName = "";
        String bussTabName = "";
        String dbTabName = "";

        // String srcColName = "";
        //String bussColName = "";
        //String dbColName = "";
        String[] colNames = null;

        //String sqlStr = "";
        String finalQuery = "";

        Connection connection = null;
        try {
            Obj = new Object[1];
            Obj[0] = bussTableId;
            finalQuery = buildQuery(getALlLayerTableNamesQuery, Obj);
            if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "viewBussData", "query fired inside try block"+System.currentTimeMillis());
            {
                logger.info("query fired inside try block" + System.currentTimeMillis());
            }
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();

            if (retObj != null && retObj.getRowCount() != 0) {
                bussTabName = retObj.getFieldValueString(0, colNames[0]);
                srcTabName = retObj.getFieldValueString(0, colNames[1]);
                dbTabName = retObj.getFieldValueString(0, colNames[2]);
            }

            Obj = new Object[1];
            Obj[0] = bussTableId;
            finalQuery = buildQuery(getALlLayerColNamesQuery, Obj);
            if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "viewBussData", "query fired"+System.currentTimeMillis());
            {
                logger.info("query fired" + System.currentTimeMillis());
            }
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();

            String insql = "select ";

            int psize = retObj.getRowCount();
            if (psize > 0) {
                for (int looper = 0; looper < psize; looper++) {
                    // columnNames[looper] = retObj.getFieldValueString(looper,"DISPLAY_NAME");
                    // tableColumnsNames[looper] =retObj.getFieldValueString(looper,"COLUMN_NAME").toUpperCase();
                    if (looper == 0) {
                        osql += " " + bussTabName + "." + retObj.getFieldValueString(looper, colNames[1]) + "  as  \"" + retObj.getFieldValueString(looper, colNames[3]).toUpperCase() + "\" ";
                        insql += " " + dbTabName + "." + retObj.getFieldValueString(looper, colNames[2]) + "  as  " + retObj.getFieldValueString(looper, colNames[1]).toUpperCase() + " ";

                    } else {
                        osql += " ," + bussTabName + "." + retObj.getFieldValueString(looper, colNames[1]) + "   as \"" + retObj.getFieldValueString(looper, colNames[3]).toUpperCase() + "\" ";
                        insql += " , " + dbTabName + "." + retObj.getFieldValueString(looper, colNames[2]) + "  as  " + retObj.getFieldValueString(looper, colNames[1]).toUpperCase() + " ";

                    }
                }
                osql += " from ( " + insql + " from " + dbTabName + " ) " + bussTabName + " where rownum <=100 ";
            }
            com.progen.datadisplay.db.PbDataDisplayBeanDb ddbd = new com.progen.datadisplay.db.PbDataDisplayBeanDb();
            connection = ddbd.getConnection(bussTableId);
            retObj = execSelectSQL(osql, connection);
            finalQuery = buildQuery(getRelatedBussTablesQuery, Obj);
            alist.add(osql);//adding retaed tables
            alist.add(finalQuery);
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    logger.error("Exception:", ex);
                }
            }
        }
        return osql;
    }

    public Connection getConnectionIdConnection(String getConnectionIdDetails) {
         Connection connection = null;
        try {
            connection = ProgenConnection.getInstance().getConnectionByConId(getConnectionIdDetails);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return connection;
    }

    public void createTblSrcRelations(String bussTblId, String noOfNodes,
            String relationClauses, String relCodedCls, String relTableIds, String relTypes) {
        String firstQry = getResourceBundle().getString("updateBussTabs");
        String secondQry = getResourceBundle().getString("insertSrcBussTableDetails");
        String thirdQry = getResourceBundle().getString("insertSubBussSrcTable");
        String fourthQry = getResourceBundle().getString("insertSubBussSrcTableDetails");
        String[] dbTblIds = null;
        Object[] firstObj = new Object[2];
        firstObj[0] = noOfNodes;
        firstObj[1] = bussTblId;
        firstQry = buildQuery(firstQry, firstObj);
        PbReturnObject retObj = null;
        ArrayList multipleQrys = new ArrayList();
        try {
            //execModifySQL(firstQry);
            multipleQrys.add(firstQry);
            if (relTableIds != null) {
                if (relTableIds.length() > 0) {
                    dbTblIds = relTableIds.split(",");
                }
            }
            for (int i = 0; i < dbTblIds.length; i++) {
                String tempTab = dbTblIds[i];
                Object[] bussSrcObj = new Object[2];
                bussSrcObj[0] = bussTblId;
                bussSrcObj[1] = tempTab;
                String tempThirdQry = buildQuery(thirdQry, bussSrcObj);
                multipleQrys.add(tempThirdQry);
                String tempFourthQry = buildQuery(fourthQry, bussSrcObj);
                multipleQrys.add(tempFourthQry);
                String tempSecondQry = buildQuery(secondQry, bussSrcObj);
                multipleQrys.add(tempSecondQry);
            }
            String[] codedRelations = null;
            String[] relClauses = null;
            String[] relType = null;
            String join = "";
            if (relCodedCls != null) {
                if (relCodedCls.length() > 0) {
                    codedRelations = relCodedCls.split("next");
                    relClauses = relationClauses.split("AND <br>");
                    relType = relTypes.split(",");

                }
            }
            if (relCodedCls != null) {
                for (int j = 0; j < codedRelations.length; j++) {
                    String firstTbl = "";
                    String firstCol = "";
                    String secTbl = "";
                    String secCol = "";
                    String thirdTbl = "";
                    String thirdCol = "";
                    String BussSrcRelIdQry = getResourceBundle().getString("getBussSrcRelId");
                    retObj = new PbReturnObject();
                    if (codedRelations[j].indexOf(">=") != -1) {
                        join = ">=";
                    } else if (codedRelations[j].indexOf("<=") != -1) {
                        join = "<=";
                    } else if (codedRelations[j].indexOf(">") != -1) {
                        join = ">";
                    } else if (codedRelations[j].indexOf("<") != -1) {
                        join = "<";
                    } else if (codedRelations[j].indexOf("=") != -1) {
                        join = "=";
                    } else if (codedRelations[j].indexOf("BETWEEN") != -1) {
                        join = "BETWEEN";
                        codedRelations[j] = codedRelations[j].replaceFirst("AND", "BETWEEN");
                    }

                    String[] temp = codedRelations[j].split(join);
                    for (int k = 0; k < temp.length; k++) {
                        String[] tempids = temp[k].split("-");
                        if (k == 0) {

                            firstTbl = tempids[0];
                            firstCol = tempids[1];

                        }
                        if (k == 1) {
                            secTbl = tempids[0];
                            secCol = tempids[1];
                        }

                        if (k == 2) {
                            thirdTbl = tempids[0];
                            thirdCol = tempids[1];
                        }

                    }
                    Object[] bussRelId = new Object[5];
                    bussRelId[0] = firstTbl;
                    bussRelId[1] = secTbl;
                    bussRelId[2] = firstTbl;
                    bussRelId[3] = secTbl;
                    bussRelId[4] = bussTblId;
                    //String bussSrcRelId="";
                    BussSrcRelIdQry = buildQuery(BussSrcRelIdQry, bussRelId);
                    if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "createTblSrcRelations", "query fired"+System.currentTimeMillis());
                    {
                        logger.info("query fired" + System.currentTimeMillis());
                    }
                    retObj = execSelectSQL(BussSrcRelIdQry);
                    if (retObj.getRowCount() == 0) {
                        String insertRelMstrSrcQry = getResourceBundle().getString("insertRelMstrSrcQry");
                        Object[] relMstr = new Object[6];
                        relMstr[0] = firstTbl;
                        relMstr[1] = secTbl;
                        relMstr[2] = "1";
                        relMstr[3] = relClauses[j];
                        relMstr[4] = relType[j];
                        relMstr[5] = bussTblId;
                        insertRelMstrSrcQry = buildQuery(insertRelMstrSrcQry, relMstr);
                        execModifySQL(insertRelMstrSrcQry);

                    } else {
                        Object[] obj = new Object[6];
                        obj[0] = " AND " + relClauses[j];
                        obj[1] = firstTbl;
                        obj[2] = secTbl;
                        obj[3] = firstTbl;
                        obj[4] = secTbl;
                        obj[5] = bussTblId;
                        String updateMstrQry = buildQuery(getResourceBundle().getString("updateBussTblRltMstr"), obj);
                        execModifySQL(updateMstrQry);

                    }

                    String updatePrevTblrel = getResourceBundle().getString("updatePrevTblrel");
                    Object[] updateObj = new Object[11];
                    updateObj[0] = relType[j];
                    updateObj[1] = join;
                    updateObj[2] = relClauses[j];
                    updateObj[3] = firstCol;
                    updateObj[4] = secCol;
                    updateObj[5] = firstCol;
                    updateObj[6] = secCol;
                    updateObj[7] = firstTbl;
                    updateObj[8] = secTbl;
                    updateObj[9] = firstTbl;
                    updateObj[10] = secTbl;
                    String newUpdateQry = buildQuery(updatePrevTblrel, updateObj);
                    int flag = 0;
                    flag = execUpdateSQL(newUpdateQry);
                    if (flag == 0) {
                        String insertRelDtlsQry = getResourceBundle().getString("insertRelDtlSrcQry");
                        Object[] relDtls = new Object[9];
                        relDtls[0] = firstTbl;
                        relDtls[1] = firstCol;
                        relDtls[2] = secTbl;
                        relDtls[3] = secCol;
                        relDtls[4] = thirdCol;
                        relDtls[5] = relType[j];
                        relDtls[6] = join;
                        relDtls[7] = relClauses[j];
                        relDtls[8] = retObj.getFieldValueString(0, 0);
                        insertRelDtlsQry = buildQuery(insertRelDtlsQry, relDtls);
                        multipleQrys.add(insertRelDtlsQry);
                    }

                }
            }
            executeMultiple(multipleQrys);

        } catch (Exception e) {
            logger.error("Exception:", e);

        }
    }

    public String getGroupTableIds(String grpId) {

        Object[] ob = new Object[1];
        ob[0] = grpId;
        String grpTableIdsQry = buildQuery(getResourceBundle().getString("grpTableIds"), ob);
        StringBuffer dbIds = new StringBuffer();
        try {
            PbReturnObject pb = new PbReturnObject();
            if (isDebugEnable) // ProgenLog.log(ProgenLog.FINE, this, "getGroupTableIds", "query fired inside try block"+System.currentTimeMillis());
            {
                if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "getGroupTableIds", "query fired inside try block"+System.currentTimeMillis());
                {
                    logger.info("query fired inside try block" + System.currentTimeMillis());
                }
            }
            pb = execSelectSQL(grpTableIdsQry);
            for (int i = 0; i < pb.getRowCount(); i++) {
                dbIds.append(pb.getFieldValueString(i, 0));
                if (i + 1 != pb.getRowCount()) {
                    dbIds.append("-");
                }
            }
            // String changedQry=
        } catch (SQLException e) {
            logger.error("Exception:", e);
        }
        return dbIds.toString();
    }

    //start by uday
    public String getRelatedTablesList(String tableId) {

        PbReturnObject pbro = new PbReturnObject();
        String query = getResourceBundle().getString("getRelatedTablesList");

        Object[] obj = new Object[4];
        obj[0] = tableId;
        obj[1] = tableId;
        obj[2] = tableId;
        //changed by susheela start 18-12-09
        obj[3] = tableId;
        String finalQry = buildQuery(query, obj);
        StringBuffer outterBuffer = new StringBuffer();
        //outterBuffer.append("<li style=\"background-image:url('images/treeViewImages/plus.gif')\"><img src=\"images/treeViewImages/database.png\"><span  id=\"123\">Related Tables</span>");
        //outterBuffer.append("<ul id=\"relatedTablesTree\">");
        try {

            if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "getRelatedTablesList", "query fired inside try block"+System.currentTimeMillis());
            {
                logger.info("query fired inside try block" + System.currentTimeMillis());
            }
            pbro = execSelectSQL(finalQry);
            int count = pbro.getRowCount();
            for (int i = 0; i < count; i++) {
                String relTableName = pbro.getFieldValueString(i, "BUSS_TABLE_NAME");
                String relTableId = pbro.getFieldValueString(i, "BUSS_TABLE_ID");
                outterBuffer.append("<li class=\"closed\" id=\"" + relTableId + "\">");
                outterBuffer.append("<img src=\"images/treeViewImages/database_table.png\"></img> ");
                outterBuffer.append("<span>" + relTableName + "</span>");
                outterBuffer.append("<ul>");
                outterBuffer.append(getSubFolderList(relTableId, relTableName));
                outterBuffer.append("</ul>");
                outterBuffer.append("</li>");
            }

            //outterBuffer.append("</ul>");
            //outterBuffer.append("</li>");
        } catch (SQLException e) {
            logger.error("Exception:", e);
        }

        return outterBuffer.toString();
    }

    public String getEditRelationSection(String tableId, String grpId) {
        StringBuffer str = new StringBuffer();
        PbReturnObject pbro = new PbReturnObject();

        try {
            String query = getResourceBundle().getString("getTableRelationDetails");
            //changed by susheela start 18-12-09
            Object[] obj = new Object[3];
            obj[0] = tableId;
            obj[1] = tableId;
            obj[2] = grpId;
            String finalQry = buildQuery(query, obj);
            if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "getEditRelationSection", "query fired inside try block"+System.currentTimeMillis());
            {
                logger.info("query fired inside try block" + System.currentTimeMillis());
            }
            pbro = execSelectSQL(finalQry);
            int count = pbro.getRowCount();

            String primaryTableId = "";
            String primTableColId = "";
            String primTableName = "";
            String secondaryTableId = "";
            String secTableColId = "";
            String secTableName = "";

            String sqlstr = "";

            str.append("<tbody>");

            LinkedHashMap relationshipDetailsHashMap = new LinkedHashMap();
            for (int i = 0; i < count; i++) {
                str.append("<tr id=relationship:" + i + ">");
                ArrayList ValuesArray = new ArrayList();
                String relationDetailsId = String.valueOf(pbro.getFieldValueInt(i, "BUSS_RELATIONSHIP_DETAIL_ID"));
                ValuesArray.add(relationDetailsId);
                primaryTableId = String.valueOf(pbro.getFieldValueInt(i, "P_BUSS_TABLE_ID"));
                primTableColId = String.valueOf(pbro.getFieldValueInt(i, "P_BUSS_COL_ID1"));
                primTableName = pbro.getFieldValueString(i, "P_TABLE_NAME");
                secTableName = pbro.getFieldValueString(i, "S_TABLE_NAME");
                String relationId = String.valueOf(pbro.getFieldValueInt(i, "BUSS_RELATIONSHIP_ID"));
                String joinType = String.valueOf(pbro.getFieldValueString(i, "JOIN_TYPE"));
                String joinOperator = String.valueOf(pbro.getFieldValueString(i, "JOIN_OPERATOR"));
                str.append("<td onclick=\"editThisCell(this)\" id=\"" + relationId + ":" + relationDetailsId + "\" class=\"ui-corner-all\" style=\"border:0.5px solid black;width:20%;\">");
                sqlstr = "select DISTINCT column_name from prg_grp_buss_table_details where buss_table_id =" + primaryTableId + " " + "and buss_column_id =" + primTableColId;
                if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "getEditRelationsSection", "query fired"+System.currentTimeMillis());
                {
                    logger.info("query fired" + System.currentTimeMillis());
                }
                PbReturnObject retObj = execSelectSQL(sqlstr);
                String columnName = retObj.getFieldValueString(0, "COLUMN_NAME").trim().toUpperCase();
                ValuesArray.add(columnName);

                // str.append(primTableName+"."+columnName);
                if (joinType.equalsIgnoreCase("inner")) {
                    str.append(primTableName + "." + columnName);
                }
                if (joinType.equalsIgnoreCase("Left Outer")) {
                    str.append(primTableName + "." + columnName + "(+)");
                }
                if (joinType.equalsIgnoreCase("Right Outer")) {
                    str.append(primTableName + "." + columnName);
                }

                str.append("</td>");
                str.append("<td id=\"sel:" + i + "\" class=\"ui-corner-all\" style=\"border:0.5px solid black;width:10%;\">");
                str.append("<select onchange=\"checkBetweenForSrc(this)\" id=\"selectBox\" name=\"selectBox\" style=\"width:100%;\">");
                if (joinOperator.equalsIgnoreCase("=")) {
                    str.append("<option selected value=\"=\">=</option>");
                } else {
                    str.append("<option value=\"=\">=</option>");
                }

                if (joinOperator.equalsIgnoreCase("<")) {
                    str.append("<option selected value=\"<\"><</option>");
                } else {
                    str.append("<option value=\"<\"><</option>");
                }

                if (joinOperator.equalsIgnoreCase("<=")) {
                    str.append("<option selected value=\"<=\"><=</option>");
                } else {
                    str.append("<option value=\"<=\"><=</option>");
                }

                if (joinOperator.equalsIgnoreCase(">")) {
                    str.append("<option selected value=\">\">></option>");
                } else {
                    str.append("<option value=\">\">></option>");
                }

                if (joinOperator.equalsIgnoreCase(">=")) {
                    str.append("<option selected value=\">=\">>=</option>");
                } else {
                    str.append("<option value=\">=\">>=</option>");
                }

                if (joinOperator.equalsIgnoreCase("BETWEEN")) {
                    str.append("<option selected value=\"BETWEEN\">BETWEEN</option>");
                } else {
                    str.append("<option value=\"BETWEEN\">BETWEEN</option>");
                }
                str.append("</select></td>");

                ValuesArray.add(joinType);

                secondaryTableId = String.valueOf(pbro.getFieldValueInt(i, "S_BUSS_TABLE_ID"));
                secTableColId = String.valueOf(pbro.getFieldValueInt(i, "S_BUSS_COL_ID1"));

                //  sqlstr = "select DISTINCT column_name from prg_grp_buss_table_details where buss_table_id ="+secondaryTableId+" " +
                //         "and buss_column_id ="+secTableColId;
                sqlstr = "select DISTINCT column_name from prg_grp_buss_table_details where buss_column_id =" + secTableColId;

                if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "insertGrpBussTableDetails", "query fired"+System.currentTimeMillis());
                {
                    logger.info("query fired" + System.currentTimeMillis());
                }
                retObj = execSelectSQL(sqlstr);
                columnName = retObj.getFieldValueString(0, "COLUMN_NAME");
                ValuesArray.add(columnName);

                ValuesArray.add(joinOperator);

                str.append("<td onclick=\"editThisCell(this)\" id=\"" + relationId + ":" + relationDetailsId + "\" class=\"ui-corner-all\" style=\"border:0.5px solid black;width:20%;\">");
                if (joinType.equalsIgnoreCase("inner")) {
                    str.append(secTableName + "." + columnName);
                }
                if (joinType.equalsIgnoreCase("Left Outer")) {
                    str.append(secTableName + "." + columnName);
                }
                if (joinType.equalsIgnoreCase("Right Outer")) {
                    str.append(secTableName + "." + columnName + "(+)");
                }

                str.append("</td>");
                str.append("<td class=\"ui-corner-all\" style=\"width:2%;visibility:hidden;display:none;\">");
                str.append("</td>");
                str.append("<td class=\"ui-corner-all\" style=\"border:0.5px solid black;width:20%;visibility:hidden;display:none;\"></td>");
                str.append("<td name=\"sel:" + i + "\" id=\"sel:" + i + "\" class=\"ui-corner-all\" style=\"border:0.5px solid black;width:10%;\">");
                str.append("<select onchange=\"changeSrcRel(this)\" id=\"joinSelectBox\" name=\"joinSelectBox\" style=\"width:100%;\">");
                if (joinType.equalsIgnoreCase("inner")) {
                    str.append("<option selected value=\"equijoin\">Equi Join</option>");
                } else {
                    str.append("<option value=\"equijoin\">Equi Join</option>");
                }
                if (joinType.equalsIgnoreCase("Left Outer")) {
                    str.append("<option selected value=\"leftouter\">Left Outer</option>");
                } else {
                    str.append("<option value=\"leftouter\">Left Outer</option>");
                }

                if (joinType.equalsIgnoreCase("Right Outer")) {
                    str.append("<option selected value=\"rightouter\">Right Outer</option>");
                } else {
                    str.append("<option value=\"rightouter\">Right Outer</option>");
                }
                str.append("</select></td>");
                str.append("<td onclick=\"deleteColumnRelation(this)\" id=\"" + relationId + ":" + relationDetailsId + "\" style=\"width:1%;cursor:pointer\">");
                str.append("<img class=\"ui-icon ui-icon-close\" alt=\"Delete\" title=\"Delete\"/>");
                str.append("</td>");
                str.append("</tr>");

                relationshipDetailsHashMap.put(relationId, ValuesArray);
            }
            str.append("</tbody>");
           
        } catch (SQLException e) {
            logger.error("Exception:", e);
        }

        return str.toString();
    }

    public String getSubFolderList(String relTabId, String relTableName) {
        String relatedTableName = relTableName;
        StringBuffer str = new StringBuffer();
        PbReturnObject pbro = new PbReturnObject();
        try {
            String query = getResourceBundle().getString("getRelatedTableColumns");
            Object[] obj = new Object[1];
            obj[0] = relTabId;

            String finalQry = buildQuery(query, obj);
            if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "getSubFolderList", "query fired inside try block"+System.currentTimeMillis());
            {
                logger.info("query fired inside try block" + System.currentTimeMillis());
            }
            pbro = execSelectSQL(finalQry);
            int count = pbro.getRowCount();
            for (int i = 0; i < count; i++) {
                String columnName = pbro.getFieldValueString(i, "COLUMN_NAME").trim().toUpperCase();
                String columnId = pbro.getFieldValueString(i, "BUSS_COLUMN_ID").trim().toUpperCase();
                str.append("<li id=\"" + relTabId + ":" + columnId + "\">");
                //str.append("<img src=\"images/treeViewImages/bullet_star.png\"></img>");
                str.append(" <span id=\"" + relTabId + ":" + columnId + ":" + relatedTableName + "\" onmouseover=\"javascript:changeColor()\" onmousedown=\"javascript:checkDrag()\" class=\"draggableColumn\">" + columnName + "</span>");
                str.append("</li>");
            }

        } catch (SQLException e) {
            logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }

        return str.toString();
    }

    /*
     * public String getEditRelationSection(String tableId) { StringBuffer str =
     * new StringBuffer(); PbReturnObject pbro = new PbReturnObject();
     *
     * try { String query =
     * getResourceBundle().getString("getTableRelationDetails"); Object[] obj =
     * new Object[2]; obj[0] = tableId; obj[1] = tableId;
     *
     * String finalQry = buildQuery(query, obj);
     * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("finalQry
     * is:: " + finalQry);
     *
     * pbro = execSelectSQL(finalQry); int count = pbro.getRowCount();
     *
     * String primaryTableId =""; String primTableColId =""; String
     * primTableName =""; String secondaryTableId =""; String secTableColId ="";
     * String secTableName ="";
     *
     * String sqlstr ="";
     *
     * str.append("<tbody>");
     *
     *
     * LinkedHashMap relationshipDetailsHashMap = new LinkedHashMap(); for(int
     * i=0;i<count;i++) { str.append("<tr id=relationship:"+i+">");
     *
     * ArrayList ValuesArray = new ArrayList(); String relationDetailsId =
     * String.valueOf(pbro.getFieldValueInt(i,"BUSS_RELATIONSHIP_DETAIL_ID"));
     * ValuesArray.add(relationDetailsId); primaryTableId =
     * String.valueOf(pbro.getFieldValueInt(i,"P_BUSS_TABLE_ID"));
     * primTableColId =
     * String.valueOf(pbro.getFieldValueInt(i,"P_BUSS_COL_ID1")); primTableName
     * = pbro.getFieldValueString(i,"P_TABLE_NAME"); secTableName =
     * pbro.getFieldValueString(i,"S_TABLE_NAME"); String relationId =
     * String.valueOf(pbro.getFieldValueInt(i,"BUSS_RELATIONSHIP_ID")); String
     * joinType = String.valueOf(pbro.getFieldValueString(i,"JOIN_TYPE"));
     * String joinOperator =
     * String.valueOf(pbro.getFieldValueString(i,"JOIN_OPERATOR"));
     *
     * str.append("<td onclick=\"editThisCell(this)\"
     * id=\""+relationId+":"+relationDetailsId+"\" class=\"ui-corner-all\"
     * style=\"border:0.5px solid black;width:20%;\">");
     *
     * sqlstr = "select DISTINCT column_name from prg_grp_buss_table_details
     * where buss_table_id ="+primaryTableId+" " + "and buss_column_id
     * ="+primTableColId;
     * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("sqlstr
     * is:: "+sqlstr); PbReturnObject retObj = execSelectSQL(sqlstr); String
     * columnName =
     * retObj.getFieldValueString(0,"COLUMN_NAME").trim().toUpperCase();
     * ValuesArray.add(columnName);
     *
     * str.append(primTableName+"."+columnName); str.append("</td>");
     * str.append("<td id=\"sel:"+i+"\" class=\"ui-corner-all\"
     * style=\"border:0.5px solid black;width:10%;\">"); str.append("<select
     * onchange=\"checkBetweenForSrc(this)\" id=\"selectBox\" name=\"selectBox\"
     * style=\"width:100%;\">"); str.append("<option value=\"=\">=</option>");
     * str.append("<option value=\"<\"><</option>"); str.append("<option
     * value=\"<=\"><=</option>"); str.append("<option value=\">\">></option>");
     * str.append("<option value=\">=\">>=</option>"); str.append("<option
     * value=\"BETWEEN\">BETWEEN</option>"); str.append("</select></td>");
     *
     *
     * String joinType =
     * String.valueOf(pbro.getFieldValueString(i,"JOIN_TYPE"));
     * ValuesArray.add(joinType);
     *
     * secondaryTableId =
     * String.valueOf(pbro.getFieldValueInt(i,"S_BUSS_TABLE_ID")); secTableColId
     * = String.valueOf(pbro.getFieldValueInt(i,"S_BUSS_COL_ID1"));
     *
     * sqlstr = "select DISTINCT column_name from prg_grp_buss_table_details
     * where buss_table_id ="+secondaryTableId+" " + "and buss_column_id
     * ="+secTableColId;
     * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("sqlstr
     * is:: "+sqlstr); retObj = execSelectSQL(sqlstr); columnName =
     * retObj.getFieldValueString(0,"COLUMN_NAME"); ValuesArray.add(columnName);
     *
     * String joinOperator =
     * String.valueOf(pbro.getFieldValueString(i,"JOIN_OPERATOR"));
     * ValuesArray.add(joinOperator);
     *
     * str.append("<td onclick=\"editThisCell(this)\"
     * id=\""+relationId+":"+relationDetailsId+"\" class=\"ui-corner-all\"
     * style=\"border:0.5px solid black;width:20%;\">");
     * str.append(secTableName+"."+columnName); str.append("</td>");
     * str.append("<td class=\"ui-corner-all\"
     * style=\"width:2%;visibility:hidden;display:none;\">");
     * str.append("</td>"); str.append("<td class=\"ui-corner-all\"
     * style=\"border:0.5px solid
     * black;width:20%;visibility:hidden;display:none;\"></td>");
     * str.append("<td name=\"sel:"+i+"\" id=\"sel:"+i+"\"
     * class=\"ui-corner-all\" style=\"border:0.5px solid black;width:10%;\">");
     * str.append("<select onchange=\"changeSrcRel(this)\" id=\"joinSelectBox\"
     * name=\"joinSelectBox\" style=\"width:100%;\">"); str.append("<option
     * value=\"equijoin\">Equi Join</option>"); str.append("<option
     * value=\"leftouter\">Left Outer</option>"); str.append("<option
     * value=\"rightouter\">Right Outer</option>");
     * str.append("</select></td>"); str.append("<td
     * onclick=\"deleteColumnRelation(this)\" id=\""+i+"\"
     * style=\"width:1%;cursor:pointer\">"); str.append("<img class=\"ui-icon
     * ui-icon-close\" alt=\"Delete\" title=\"Delete\"/>"); str.append("</td>");
     * str.append("</tr>");
     *
     *
     * relationshipDetailsHashMap.put(relationId, ValuesArray); }
     * str.append("</tbody>");
     * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("relationshipDetailsHashMap
     * is:: "+relationshipDetailsHashMap);
     *
     *
     * }
     * catch (Exception e) { logger.error("Exception:",e); }
     *
     * return str.toString(); }
     *
     */

    /*
     * public void updateRelatedTablesRelations(String relIds,String
     * relDetailsIds,String commitRelationTxt,String joinTypeStr) { try {
     * ArrayList queries = new ArrayList(); String[] commitRelationText =
     * commitRelationTxt.split("<br>");
     *
     * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("commitRelationText
     * lgn is:: "+commitRelationText.length); String relationIds = relIds;
     * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("relationIds
     * are:: "+relationIds); String[] xy = relationIds.split(",");
     * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("joinTypeStr
     * are:: "+joinTypeStr);
     *
     * String newRelIds = relIds; String[] newIds = newRelIds.split(",");
     * String[] newDetailsIds = relDetailsIds.split(","); String[] joinTypes =
     * joinTypeStr.split(",");
     *
     * String joinTypeNum ="";
     *
     * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("newRelationIds
     * are:: "+newRelIds);
     *
     * String query = getResourceBundle().getString("updateBussTableRltMaster");
     * Object[] obj = new Object[3]; for(int y=0;y<newIds.length;y++) {
     * if(joinTypes[y].equalsIgnoreCase("equijoin")) { joinTypeNum = "1"; } else
     * if(joinTypes[y].equalsIgnoreCase("leftouter")) { joinTypeNum = "2"; }
     * else if(joinTypes[y].equalsIgnoreCase("rightouter")) { joinTypeNum = "3";
     * }
     *
     *
     * obj[0] = joinTypeNum; obj[1] = commitRelationText[y].trim(); obj[2] =
     * newIds[y];
     *
     * String finalQry = buildQuery(query, obj);
     * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("finalQry
     * is:: " + finalQry);
     *
     * queries.add(finalQry); //master queries }
     *
     * String queryDel =
     * getResourceBundle().getString("updateBussTableRltDetails"); Object[]
     * objDel = new Object[4]; for(int y=0;y<newDetailsIds.length;y++) {
     * objDel[0] = joinTypeNum; //get join_operator objDel[1] =
     * commitRelationText[y].trim(); objDel[2] = newIds[y]; objDel[3] =
     * newDetailsIds[y]; }
     *
     * executeMultiple(queries); } catch(Exception ex) {
     * logger.error("Exception:",ex); } }
     */
    public void updateRelatedTablesRelations(String relIds, String relDetailsIds, String commitRelationTxt, String joinTypeStr, String allSelectTypes) {
        try {
            ArrayList queries = new ArrayList();
            String[] commitRelationText = commitRelationTxt.split("<br>");
            allSelectTypes = allSelectTypes.substring(1);
            String allTypes[] = allSelectTypes.split(",");
            String relationIds = relIds;
            String[] xy = relationIds.split(",");

            relIds = relIds.replace(":", "");
            String allIds[] = relIds.split(",");
            String rel = "";
//            String detRel = "";
            StringBuilder detRel = new StringBuilder();
            for (int m = 0; m < allIds.length; m++) {
                if (m % 2 == 0) {
                    rel = rel + "," + allIds[m];
                } else {
                    detRel.append(",").append(allIds[m]);
//                    detRel = detRel + "," + allIds[m];
                }
            }
            if (detRel.length() > 0) {
//                detRel = detRel.substring(1);
                detRel = new StringBuilder(detRel.substring(1));
                rel = rel.substring(1);
            }
            String newRelIds = relIds;
            String[] newIds = rel.split(",");//newRelIds.split(",");
            String[] newDetailsIds = detRel.toString().split(",");//relDetailsIds.split(",");
            String[] joinTypes = joinTypeStr.split(",");

            String joinTypeNum = "";

            String query = getResourceBundle().getString("updateBussTableRltMaster");
            Object[] obj = new Object[4];
            String clauseType = "";

            for (int y = 0; y < newIds.length; y++) {
                if (joinTypes[y].equalsIgnoreCase("equijoin")) {
                    joinTypeNum = "1";
                    clauseType = "inner";
                } else if (joinTypes[y].equalsIgnoreCase("leftouter")) {
                    joinTypeNum = "2";
                    clauseType = "Left Outer";
                } else if (joinTypes[y].equalsIgnoreCase("rightouter")) {
                    joinTypeNum = "3";
                    clauseType = "Right Outer";
                }
                obj[0] = joinTypeNum;
                obj[1] = commitRelationText[y].trim();
                obj[2] = clauseType;
                obj[3] = newIds[y];
                String finalQry = buildQuery(query, obj);
                queries.add(finalQry); //master queries
            }

            String queryDel = getResourceBundle().getString("updateBussTableRltDetails");
            Object[] objDel = new Object[5];
            for (int y = 0; y < newDetailsIds.length; y++) {
                if (joinTypes[y].equalsIgnoreCase("equijoin")) {
                    joinTypeNum = "1";
                    clauseType = "inner";
                } else if (joinTypes[y].equalsIgnoreCase("leftouter")) {
                    joinTypeNum = "2";
                    clauseType = "Left Outer";
                } else if (joinTypes[y].equalsIgnoreCase("rightouter")) {
                    joinTypeNum = "3";
                    clauseType = "Right Outer";
                }

                objDel[0] = allTypes[y]; //get join_operator
                objDel[1] = commitRelationText[y].trim();
                objDel[2] = clauseType;
                objDel[3] = newIds[y];
                objDel[4] = newDetailsIds[y];
                String finqueryDel = buildQuery(queryDel, objDel);
                queries.add(finqueryDel);
            }
            if (isDebugEnable)     {
                logger.info(" executing multiple queries" + System.currentTimeMillis());
            }
            executeMultiple(queries);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }
    //end by uday

    public String getBussTableConnectionDbType(String bussTableIds) {
//        PbBussGrpResourceBundle resBundle = new PbBussGrpResourceBundle();
        Connection connection = null;
        PbReturnObject retObj = null;
        String getDataDisplayColumnsQuery = getResourceBundle().getString("getBussTableConnDetails");
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
        Obj[0] = bussTableIds;

        String[] tableColumnNames = null;

        try {
            finalQuery = buildQuery(getDataDisplayColumnsQuery, Obj);
            if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "getBussTableConnectionDbType", "query fired inside try block"+System.currentTimeMillis());
            {
                logger.info("query fired inside try block" + System.currentTimeMillis());
            }
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
             * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("jdbc:mysql://"
             * + server + ":" + serverPort + "/" + dbName + "," + userName + ","
             * + password); connection =
             * DriverManager.getConnection("jdbc:mysql://" + server + ":" +
             * serverPort + "/" + dbName, userName, password); } else if (dbType
             * != null && dbType.equalsIgnoreCase("SQL")) {
             * Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); connection =
             * DriverManager.getConnection("jdbc:odbc:SQL_SERVER","",""); }
             */
            return dbType;
        } catch (SQLException exp) {
            logger.error("Exception:", exp);
            return dbType;
        } catch (NullPointerException exp) {
            logger.error("Exception:", exp);
            return dbType;
        }
    }

    public String getuserFolders(String userid, String deletingid) {
        Deletingbusrolesresourcebundle delresBundle = new Deletingbusrolesresourcebundle();
        String status = "";
        try {
            PbReturnObject pbretobj = new PbReturnObject();

            Object[] getfolderobj = new Object[2];
            getfolderobj[0] = userid;
            getfolderobj[1] = deletingid;

            String query = delresBundle.getString("getuserfolders");
            String finalquery = buildQuery(query, getfolderobj);
            if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "getuserFolders", "query fired inside try block"+System.currentTimeMillis());
            {
                logger.info("query fired inside try block" + System.currentTimeMillis());
            }
            pbretobj = execSelectSQL(finalquery);
            if (pbretobj.getRowCount() != 0) {
                status = chkuserreports(userid);
            } else if (pbretobj.getRowCount() == 0) {
                status = "Failure";
            }

        } catch (SQLException e) {
            logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return status;
    }

    public String chkuserreports(String userfolder) {
        Deletingbusrolesresourcebundle delresBundle = new Deletingbusrolesresourcebundle();
        String status = "";
//        String repids = "";
        StringBuilder repids = new StringBuilder(300);
        try {
            PbReturnObject pbretobj = new PbReturnObject();

            Object[] getfolderobj = new Object[1];
            getfolderobj[0] = userfolder;

            String query = delresBundle.getString("getfolderids");
            String finalquery = buildQuery(query, getfolderobj);
            if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "checkerreports", "query fired inside try block"+System.currentTimeMillis());
            {
                logger.info("query fired inside try block" + System.currentTimeMillis());
            }
            pbretobj = execSelectSQL(finalquery);
            Object[] repidobj = new Object[1];
            if (pbretobj.getRowCount() != 0) {
                for (int i = 0; i < pbretobj.getRowCount(); i++) {
                    if (!pbretobj.getFieldValueString(i, "FOLDER_ID").equalsIgnoreCase("0")) {
//                        repids = repids + "," + (pbretobj.getFieldValueString(i, "FOLDER_ID")).toString();
                        repids.append(",").append((pbretobj.getFieldValueString(i, "FOLDER_ID")).toString());
                    }
                }
//                if (!repids.equalsIgnoreCase("")) {
//                    repids = repids.substring(1, repids.length());
//                }
                if (repids.length() > 0) {
//                    repids = repids.substring(1, repids.length());
                    repids = new StringBuilder(repids.substring(1, repids.length()));
                }
                if (repids.length() != 0 && repids != null) {
                    repidobj[0] = repids;
                    String repidquery = delresBundle.getString("getreportids");
                    String lastquery = buildQuery(repidquery, repidobj);
                    if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "checkerreports", "query fired"+System.currentTimeMillis());
                    {
                        logger.info("query fired " + System.currentTimeMillis());
                    }
                    pbretobj = execSelectSQL(lastquery);
                    if (pbretobj.getRowCount() != 0 && pbretobj != null) {
                        status = "Success";
                    } else {
                        status = "Failure";
                    }
                } else {
                    status = "Success";

                }
            } else if (pbretobj.getRowCount() == 0) {
                status = "Failure";
            }

        } catch (SQLException e) {
            logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return status;
    }

    public String DeletBusinessRoles(String busroleid) {
        Deletingbusrolesresourcebundle delresBundle = new Deletingbusrolesresourcebundle();
        String status = "success";
        try {
            PbReturnObject pbretobj = new PbReturnObject();

            ArrayList deletequerieslist = new ArrayList();
            String query = "";
            String finalquery = "";

            status = DeleteReports(busroleid);

            Object[] getfolderobj = new Object[1];
            getfolderobj[0] = busroleid;

            for (int i = 1; i < 20; i++) {
                query = delresBundle.getString("deletebusroles" + i);
                finalquery = buildQuery(query, getfolderobj);
                deletequerieslist.add(finalquery);
            }

            executeMultiple(deletequerieslist);
            status = Deletebussgroups(busroleid);

        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return status;
    }

    public String DeleteReports(String busroleid) {
        Deletingbusrolesresourcebundle delresBundle = new Deletingbusrolesresourcebundle();
        String status = "success";
        try {
            ArrayList deletequerieslist = new ArrayList();
            String query = "";
            String finalquery = "";
            Object[] getfolderobj = new Object[1];
            getfolderobj[0] = busroleid;
            for (int i = 1; i < 13; i++) {
                query = delresBundle.getString("deletebusreports" + i);
                finalquery = buildQuery(query, getfolderobj);
                deletequerieslist.add(finalquery);
            }
            if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "DeleteReports", "query fired inside try block"+System.currentTimeMillis());
            {
                logger.info("query fired inside try block" + System.currentTimeMillis());
            }
            executeMultiple(deletequerieslist);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return status;
    }

    public String Deletebussgroups(String busroleid) {
        Deletingbusrolesresourcebundle delresBundle = new Deletingbusrolesresourcebundle();
        String status = "success";
        try {

            ArrayList deletequerieslist = new ArrayList();
            String query = "";
            String finalquery = "";

            Object[] getfolderobj = new Object[1];
            getfolderobj[0] = busroleid;

            for (int i = 1; i < 7; i++) {
                query = delresBundle.getString("deletebussgrp" + i);
                finalquery = buildQuery(query, getfolderobj);
                deletequerieslist.add(finalquery);
            }
            if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "Deletebussgroups", "query fired"+System.currentTimeMillis());
            {
                logger.info("query fired" + System.currentTimeMillis());
            }
            executeMultiple(deletequerieslist);

        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return status;
    }

    public String getTableNamesBsgrp() {
        PbReturnObject pbReturnObject = new PbReturnObject();
        try {
            String sql = "SELECT t1.TABLE_ID,t1.TABLE_NAME,t2.COLUMN_ID," + "t2.TABLE_COL_NAME, count(t2.column_id) over (partition by t2.TABLE_ID) as COLUMN_COUNT " + " ,t2.IS_ACTIVE FROM PRG_DB_MASTER_TABLE t1,PRG_DB_MASTER_TABLE_DETAILS t2  WHERE t1.TABLE_ID=t2.TABLE_ID " + "  AND t2.IS_ACTIVE='Y' ";
            String sql2 = "SELECT gbt.BUSS_TABLE_ID, gbt.BUSS_TABLE_NAME,gbtd.BUSS_COLUMN_ID,gbtd.column_disp_name,count(gbtd.BUSS_COLUMN_ID) " + "over (partition by gbtd.BUSS_TABLE_ID) as column_count FROM PRG_GRP_BUSS_TABLE gbt," + "PRG_GRP_BUSS_TABLE_DETAILS gbtd where gbt.buss_table_id=gbtd.buss_table_id";

            if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "getTableNamesBsgrp", "query fired inside try block"+System.currentTimeMillis());
            {
                logger.info("query fired inside try block" + System.currentTimeMillis());
            }
            pbReturnObject = execSelectSQL(sql);

        } catch (SQLException e) {
            logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return this.buildTableDetails(pbReturnObject);
    }

    public String getTableNamesBsgrp(String connID) {

        PbReturnObject pbReturnObject = new PbReturnObject();
        try {
            String sql = "";
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                sql = "SELECT t1.TABLE_ID, t1.TABLE_NAME, t2.COLUMN_ID, t2.TABLE_COL_NAME, COUNT(t2.column_id) AS COLUMN_COUNT , t2.IS_ACTIVE FROM PRG_DB_MASTER_TABLE t1, PRG_DB_MASTER_TABLE_DETAILS t2 WHERE t1.TABLE_ID =t2.TABLE_ID AND t2.IS_ACTIVE ='Y' AND t1.table_id NOT IN ( SELECT tab_id FROM prg_qry_dim_tables WHERE tab_id IS NOT NULL UNION SELECT db_table_id FROM prg_grp_buss_table WHERE db_table_id IS NOT NULL ) AND t1.connection_id = " + connID + " GROUP BY t1.TABLE_ID, t1.TABLE_NAME, t2.COLUMN_ID, t2.TABLE_COL_NAME, t2.IS_ACTIVE";
            } else {
                sql = "SELECT t1.TABLE_ID,t1.TABLE_NAME,t2.COLUMN_ID,t2.TABLE_COL_NAME, COUNT(t2.column_id) over (partition BY t2.TABLE_ID) AS COLUMN_COUNT ,t2.IS_ACTIVE FROM PRG_DB_MASTER_TABLE t1, PRG_DB_MASTER_TABLE_DETAILS t2 WHERE t1.TABLE_ID=t2.TABLE_ID AND t2.IS_ACTIVE ='Y' AND t1.table_id NOT IN "
                        + "( SELECT tab_id FROM prg_qry_dim_tables WHERE tab_id IS NOT NULL  UNION  SELECT db_table_id FROM prg_grp_buss_table WHERE db_table_id IS NOT NULL ) and t1.connection_id = " + connID;
            }
            if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "getTableNamesBsgrp", "query fired inside try block"+System.currentTimeMillis());
            {
                logger.info("query fired inside try block" + System.currentTimeMillis());
            }
            pbReturnObject = execSelectSQL(sql);
        } catch (SQLException e) {
            logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return this.buildTableDetails(pbReturnObject);
    }

    public String buildTableDetails(PbReturnObject pbReturnObject) {
        String tableName = null, colName = null, colId = null;
        String tableId = "";
        int endtableFlag = 0;
        String tablestr = null;
        StringBuffer tablebufr = new StringBuffer();

        try {
            if (pbReturnObject != null) {
                for (int j = 0; j < pbReturnObject.getRowCount(); j++) {

                    Table table = new Table();
                    boolean b = tableId.equalsIgnoreCase(pbReturnObject.getFieldValueString(j, "TABLE_ID"));
                    if ((tableId.length() == 0) || (!b)) {

                        Table table1 = new Table();
                        table1.setTableName(pbReturnObject.getFieldValueString(j, "TABLE_NAME"));
                        tableName = pbReturnObject.getFieldValueString(j, "TABLE_NAME");
                        table1.setTableId(pbReturnObject.getFieldValueString(j, "TABLE_ID"));
                        tableId = pbReturnObject.getFieldValueString(j, "TABLE_ID");
                        table.setColumnName(pbReturnObject.getFieldValueString(j, "TABLE_COL_NAME"));
                        colName = pbReturnObject.getFieldValueString(j, "TABLE_COL_NAME");
                        table.setColumnId(pbReturnObject.getFieldValueString(j, "COLUMN_ID"));
                        colId = pbReturnObject.getFieldValueString(j, "COLUMN_ID");
                        endtableFlag = 1;
                        if (tableName != null && (!tableName.equals(""))) {
                            tablebufr.append("<li id='dbLi-" + tableId + "' class='closed'><img src='images/treeViewImages/database_table.png'><span id='tableId-" + tableId + "'>" + tableName + "</span>");
                            tablebufr.append("<ul id='" + tableName + "'>");
                        }
                        if ((!colName.equals(""))) {
                            tablebufr.append("<li id='columns'><img id='colId-" + colId + "' src='images/treeViewImages/bullet_star.png'><span>" + colName + "</span></li>");
                        }
                    } else {
                        table.setColumnName(pbReturnObject.getFieldValueString(j, "TABLE_COL_NAME"));
                        colName = pbReturnObject.getFieldValueString(j, "TABLE_COL_NAME");
                        table.setColumnId(pbReturnObject.getFieldValueString(j, "COLUMN_ID"));
                        colId = pbReturnObject.getFieldValueString(j, "COLUMN_ID");
                        if ((!colName.equals(""))) {
                            tablebufr.append("<li id='columns'><img id='colId-" + colId + "' src='images/treeViewImages/bullet_star.png'><span>" + colName + "</span></li>");
                        }
                        endtableFlag++;
                        if (endtableFlag == Integer.parseInt(pbReturnObject.getFieldValueString(j, "COLUMN_COUNT"))) {
                            table.setEndTable("true");
                            tablebufr.append("</ul></li>");
                        }
                    }
                    tablestr = tablebufr.toString();
                }
            }
        } catch (NumberFormatException e) {
            logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }

        return tablestr;
    }

    private String getBussColID(String memOrderBy, String grpId) {
        PbReturnObject pbReturnObject = new PbReturnObject();
        String bussColId = "";
        try {
            if (!memOrderBy.equalsIgnoreCase("")) {
                if (isDebugEnable) //                ProgenLog.log(ProgenLog.FINE, this, "getBussColID", "query fired"+System.currentTimeMillis());
                {
                    logger.info("query fired: " + System.currentTimeMillis());
                }
                pbReturnObject = super.execSelectSQL("select BTD.BUSS_COLUMN_ID from PRG_GRP_BUSS_TABLE_DETAILS BTD , PRG_GRP_BUSS_TABLE BT where BTD.DB_COLUMN_ID = " + memOrderBy + "  AND BT.GRP_ID =" + grpId + " and bt.buss_table_id=btd.buss_table_id");
                bussColId = pbReturnObject.getFieldValueString(0, 0);
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return bussColId;
    }

    /**
     * @param isDebugEnable the isDebugEnable to set
     */
    public void setIsDebugEnable(boolean isDebugEnable) {
        this.isDebugEnable = isDebugEnable;
    }
}
