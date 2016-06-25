/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.dimensions;

import com.progen.userlayer.action.GenerateDragAndDrophtml;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author Administrator
 */
public class DimensionsDAO extends PbDb {

    public static Logger logger = Logger.getLogger(DimensionsDAO.class);
    PbDimensionResourceBundle rsrcBundle = new PbDimensionResourceBundle();
    int memIndex = 1;

    public ArrayList getDimensionsList(String connId) {
        String getDimensionQry;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            getDimensionQry = rsrcBundle.getString("getDimensionDetMysql");
        } else {
            getDimensionQry = rsrcBundle.getString("getDimensionDet");
        }
        ////////////////////////////////////////////////////////////////////////.println("getDimensionDet----->"+getDimensionQry);
        Object obj[] = new Object[1];
        obj[0] = connId;
        String finalQuery = buildQuery(getDimensionQry, obj);
        ////////////////////////////////////////////////////////////////////////.println("final query--->"+finalQuery);

        PbReturnObject retObj = new PbReturnObject();
        String[] columnsArr = null;
        ArrayList dimensionList = new ArrayList();
        ArrayList tabList = null;
        Dimension dimension = null;
        int k = 0;
        try {
            //retObj = execSelectSQL(getDimensionQry);
            retObj = execSelectSQL(finalQuery);
            columnsArr = retObj.getColumnNames();

            for (int i = 0; i < retObj.getRowCount(); i++) {
                if (k == 0) {
                    tabList = new ArrayList();
                    dimension = new Dimension();
                    dimension.setDimensionId(retObj.getFieldValueString(i, columnsArr[0]));
                    dimension.setDimensionName(retObj.getFieldValueString(i, columnsArr[1]));
                }
                k++;
                Dimension dimTable = new Dimension();

                dimTable.setTableId(retObj.getFieldValueString(i, columnsArr[2]));
                dimTable.setTableName(retObj.getFieldValueString(i, columnsArr[3]));
                dimTable.setDimensionTblId(retObj.getFieldValueString(i, columnsArr[5]));
                tabList.add(dimTable);

                if (k == retObj.getFieldValueInt(i, columnsArr[6])) {
                    dimension.setTableList(tabList);
                    dimension.setEndDimension("true");
                    dimensionList.add(dimension);
                    k = 0;
                }
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return dimensionList;
    }

    public String getDimensionsListBsgrp(String connId) {

        String dimstr = null;
        StringBuffer dimbuffer = new StringBuffer();
        String getDimensionQry;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            getDimensionQry = rsrcBundle.getString("getDimensionDetMysql");
        } else {
            getDimensionQry = rsrcBundle.getString("getDimensionDet");
        }
        ////////////////////////////////////////////////////////////////////////.println("getDimensionDet----->"+getDimensionQry);
        Object obj[] = new Object[1];
        obj[0] = connId;
        String finalQuery = buildQuery(getDimensionQry, obj);
        ////////////////////////////////////////////////////////////////////////.println("final query--->"+finalQuery);

        PbReturnObject retObj = new PbReturnObject();
        String[] columnsArr = null;
        ArrayList dimensionList = new ArrayList();
        ArrayList tabList = null;
        Dimension dimension = null;
        int k = 0;
        try {
            //retObj = execSelectSQL(getDimensionQry);
            retObj = execSelectSQL(finalQuery);
            columnsArr = retObj.getColumnNames();

            String dimid = null, dimname = null;
            for (int i = 0; i < retObj.getRowCount(); i++) {
                if (k == 0) {
                    tabList = new ArrayList();
                    dimension = new Dimension();
                    dimension.setDimensionId(retObj.getFieldValueString(i, columnsArr[0]));
                    dimid = retObj.getFieldValueString(i, columnsArr[0]);
                    dimension.setDimensionName(retObj.getFieldValueString(i, columnsArr[1]));
                    dimname = retObj.getFieldValueString(i, columnsArr[1]);
                    dimbuffer.append("<li class='closed'><img src='images/treeViewImages/database_table.png'><span class='dimensions' id='dimId-" + dimid + "'>" + dimname + "</span>");
                    dimbuffer.append("<ul id='ul-dimId-" + dimid + "'>");
                    dimbuffer.append(getDimenstionListBsgrpTables(connId, dimid));
                    dimbuffer.append("</ul></li>");
                }
                k++;
                /*
                 * Dimension dimTable = new Dimension();
                 *
                 * dimTable.setTableId(retObj.getFieldValueString(i,
                 * columnsArr[2]));
                 * dimTable.setTableName(retObj.getFieldValueString(i,
                 * columnsArr[3]));
                 * dimTable.setDimensionTblId(retObj.getFieldValueString(i,
                 * columnsArr[5]));
                tabList.add(dimTable);
                 */
                if (k == retObj.getFieldValueInt(i, columnsArr[6])) {
                    dimension.setTableList(tabList);
                    dimension.setEndDimension("true");
                    dimensionList.add(dimension);
                    k = 0;
                }

            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        dimstr = dimbuffer.toString();
        //  return dimensionList;
        return dimstr;
    }

    public String getDimenstionListBsgrpTables(String connId, String dimid) {
        String dimstr = null;
        StringBuffer dimbuffer = new StringBuffer();
        String getDimensionQry;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            getDimensionQry = rsrcBundle.getString("getDimensiontablesMysql");
        } else {
            getDimensionQry = rsrcBundle.getString("getDimensiontables");
        }
        Object obj[] = new Object[2];
        obj[0] = connId;
        obj[1] = dimid;
        String[] columnsArr = null;
        String finalQuery = buildQuery(getDimensionQry, obj);
        PbReturnObject retObj = new PbReturnObject();
        try {
            //retObj = execSelectSQL(getDimensionQry);
            retObj = execSelectSQL(finalQuery);
            columnsArr = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                retObj.getFieldValueString(i, columnsArr[2]);
                retObj.getFieldValueString(i, columnsArr[3]);
                retObj.getFieldValueString(i, columnsArr[5]);
                dimbuffer.append("<li id='dimTable-" + retObj.getFieldValueString(i, columnsArr[2]) + "'><img src='images/treeViewImages/bullet_star.png'><span>" + retObj.getFieldValueString(i, columnsArr[3]) + "</span></li>");
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        dimstr = dimbuffer.toString();
        // //////.println("dimstr in java\t" + dimstr);
        return dimstr;
    }

    public void saveTimeDimensionMembers(String[] memberIds, String dimension_Id) throws Exception {
        String activeConnection = "";
        int dim_tab_id = 0;
        int dim_id = Integer.parseInt(dimension_Id);
        int tab_id = 0;
        int nextVal = 0;
        String finalQuery = "select DIM_TAB_ID,TAB_ID from  PRG_QRY_DIM_TABLES where DIM_ID=" + dimension_Id;
        PbReturnObject retObj = new PbReturnObject();

        retObj = execSelectSQL(finalQuery);
        if (retObj.rowCount > 0) {
            dim_tab_id = Integer.parseInt(retObj.getFieldValueString(0, 0));
            tab_id = Integer.parseInt(retObj.getFieldValueString(0, 1));
        }


        for (int i = 0; i < memberIds.length; i++) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                retObj = execSelectSQL("select Last_Insert_id(MEMBER_ID) From PRG_QRY_DIM_MEMBER order by 1 desc limit 1");
                nextVal = retObj.getFieldValueInt(0, 0);
                finalQuery = "insert into PRG_QRY_DIM_MEMBER (MEMBER_NAME,DIM_ID,DIM_TAB_ID,USE_DENOM_TABLE,DENOM_TAB_ID,DENOM_QUERY,MEMBER_DESC) ";
                finalQuery = finalQuery + "values('" + memberIds[i] + "'," + dim_id + "," + dim_tab_id + ",'Y'," + tab_id + ",'select * from PRG_DAY_DENOM','" + memberIds[i] + "')";
                execInsert(finalQuery);
                finalQuery = "insert into PRG_QRY_DIM_MEMBER_DETAILS (MEM_ID,COL_TYPE) values(" + nextVal + ",'KEY')";
                execInsert(finalQuery);
                finalQuery = "insert into PRG_QRY_DIM_MEMBER_DETAILS (MEM_ID,COL_TYPE) values(" + nextVal + ",'VALUE')";

            } else {
                retObj = execSelectSQL("select PRG_QRY_DIM_MEMBER_SEQ.nextval from dual");
                nextVal = retObj.getFieldValueInt(0, "NEXTVAL");
                finalQuery = "insert into PRG_QRY_DIM_MEMBER (MEMBER_ID,MEMBER_NAME,DIM_ID,DIM_TAB_ID,USE_DENOM_TABLE,DENOM_TAB_ID,DENOM_QUERY,MEMBER_DESC) ";
                finalQuery = finalQuery + "values(" + nextVal + ",'" + memberIds[i] + "'," + dim_id + "," + dim_tab_id + ",'Y'," + tab_id + ",'select * from PRG_DAY_DENOM','" + memberIds[i] + "')";
                execInsert(finalQuery);
                finalQuery = "insert into PRG_QRY_DIM_MEMBER_DETAILS (MEM_DETAIL_ID,MEM_ID,COL_TYPE) values(PRG_QRY_DIM_MEMBER_DETAILS_SEQ.nextval," + nextVal + ",'KEY')";
                execSelectSQL(finalQuery);
                finalQuery = "insert into PRG_QRY_DIM_MEMBER_DETAILS (MEM_DETAIL_ID,MEM_ID,COL_TYPE) values(PRG_QRY_DIM_MEMBER_DETAILS_SEQ.nextval," + nextVal + ",'VALUE')";
            }
            execInsert(finalQuery);
        }
    }

    public String setUserTimeDimension(String connId) {
        String dimension_id = "";
        String table_id = "";
        PbReturnObject retObj = null;
        ArrayList queryList = new ArrayList();
        String finalQuery = "";
        finalQuery = "select DIMENSION_NAME from PRG_QRY_DIMENSIONS where CONNECTION_ID=" + connId;
        boolean flag = false;
        try {
            retObj = execSelectSQL(finalQuery);
            for (int i = 0; i < retObj.getRowCount(); i++) {
                if (retObj.getFieldValueString(i, 0).equalsIgnoreCase("UserDayInfo(Time)")) {
                    flag = true;
                }
            }
            if (flag == false) {
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    finalQuery = "insert into prg_qry_dimensions(DIMENSION_NAME,DIMENSION_DESC,PRG_DIM_ACTIVE,DEFAULT_HIERARCHY_ID,CONNECTION_ID) values('UserDayInfo(Time)','UserDayInfo(Time)','Y',null," + connId + ")";
                } else {
                    finalQuery = "insert into prg_qry_dimensions(DIMENSION_ID,DIMENSION_NAME,DIMENSION_DESC,PRG_DIM_ACTIVE,DEFAULT_HIERARCHY_ID,CONNECTION_ID) values(PRG_QRY_DIMENSIONS_SEQ.nextval,'UserDayInfo(Time)','UserDayInfo(Time)','Y',null," + connId + ")";
                }
                execInsert(finalQuery);
                finalQuery = "select DIMENSION_ID from PRG_QRY_DIMENSIONS where  DIMENSION_NAME='UserDayInfo(Time)' and CONNECTION_ID = " + connId;
                retObj = execSelectSQL(finalQuery);
                if (retObj.rowCount > 0) {
                    dimension_id = retObj.getFieldValueString(0, 0);
                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    dimension_id = "0";
                }
                finalQuery = "select TABLE_ID from prg_db_master_table where CONNECTION_ID=" + connId + " and TABLE_NAME ='PR_DAY_INFO'";
                retObj = execSelectSQL(finalQuery);
                //table_id = retObj.getFieldValueString(0,0);
                if (retObj.rowCount == 1) {
                    table_id = retObj.getFieldValueString(0, 0);
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        finalQuery = "insert into prg_qry_dim_tables (DIM_ID,TAB_ID) values('" + dimension_id + "','" + table_id + "')";
                    } else {
                        finalQuery = "insert into prg_qry_dim_tables (DIM_TAB_ID,DIM_ID,TAB_ID) values(PRG_QRY_DIM_TABLES_SEQ.nextVal,'" + dimension_id + "','" + table_id + "')";
                    }
                    queryList.add(finalQuery);
                }

                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    finalQuery = "insert into PRG_QRY_DIM_TAB_DETAILS (DIM_TAB_ID,COL_ID,IS_AVAILABLE,IS_PK_KEY) ";
                    finalQuery = finalQuery + "select (select Last_Insert_Id(dim_tab_id) From PRG_QRY_DIM_TABLES order by 1 desc limit 1),dd.COLUMN_ID ,'Y','N' from prg_db_master_table dt,prg_db_master_table_details dd where dt.connection_id = " + connId + " and dt.table_name = 'PR_DAY_INFO' and dt.table_id = dd.table_id";
                } else {
                    finalQuery = "insert into PRG_QRY_DIM_TAB_DETAILS (DIM_TAB_COL_ID,DIM_TAB_ID,COL_ID,IS_AVAILABLE,IS_PK_KEY) ";
                    finalQuery = finalQuery + "select PRG_QRY_DIM_TAB_DETAILS_SEQ.nextval,PRG_QRY_DIM_TABLES_SEQ.CURRVAL,dd.COLUMN_ID ,'Y','N' from prg_db_master_table dt,prg_db_master_table_details dd where dt.connection_id = " + connId + " and dt.table_name = 'PR_DAY_INFO' and dt.table_id = dd.table_id";
                }
                queryList.add(finalQuery);
                executeMultiple(queryList);
            }


        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return dimension_id;

    }

    public boolean isUserDefinedTimeDimensionEnabled(String connId) {
        boolean isUserTimeDimEnabled = false;
        PbReturnObject retObj = null;
        String finalQuery = "";
        finalQuery = "select DIMENSION_NAME from PRG_QRY_DIMENSIONS where CONNECTION_ID=" + connId;

        try {
            retObj = execSelectSQL(finalQuery);
            for (int i = 0; i < retObj.getRowCount(); i++) {
                if (retObj.getFieldValueString(i, 0).equalsIgnoreCase("UserDayInfo(Time)")) {
                    isUserTimeDimEnabled = true;
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }


        return isUserTimeDimEnabled;
    }

    public String getDimensionMembersDropDown(String connId, String dimensionName, String tableName, String contextPath, int dim_tab_id, int col_id) {
        String finalQuery = "select " + dimensionName + " from " + tableName;
        ArrayList<String> dimensionMembers = new ArrayList<String>();
        ArrayList<String> selectedDimMembers = new ArrayList<String>();
        String query = "select dim_members from prg_qry_dim_tab_details where dim_tab_id=" + dim_tab_id + " and col_id=" + col_id;
        PbReturnObject retObj = new PbReturnObject();

        Connection con = null;
        String divhtml = "";
        try {
            con = ProgenConnection.getInstance().getConnectionByConId(connId);
            if (con != null) {
                retObj = execSelectSQL(query);
                String dim_members = retObj.getFieldValueClobString(0, "DIM_MEMBERS");
                if (dim_members != null) {
                    String dimMemArray[] = dim_members.split(",");
                    for (String str : dimMemArray) {
                        selectedDimMembers.add(str);
                    }

                }
                retObj = execSelectSQL(finalQuery, con);
                for (int i = 0; i < retObj.rowCount; i++) {
                    dimensionMembers.add(retObj.getFieldValueString(i, 0));
                }
                GenerateDragAndDrophtml html = new GenerateDragAndDrophtml("Select Columns from below", "Drag Columns to here", selectedDimMembers, dimensionMembers, contextPath);
                divhtml = html.getDragAndDropDiv();
            }
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
        return divhtml;
    }

    public void saveAccessEnabledDimMembers(String dimMemString, int dim_tab_id, int col_id) {
        String finalQuery = "update prg_qry_dim_tab_details set is_enabled=\'Y\' , dim_members= \'" + dimMemString + "\' where dim_tab_id=" + dim_tab_id + " and col_id=" + col_id;
        try {
            execModifySQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

    }
    //by sunita

    public void migrateDimensionGrp(String tabId, String dimtabId, String dimId, String grparr) {
        PbDb pbDb = new PbDb();
        int dimTabId = 0;
        PbReturnObject grpDimTabSeq = null;
        insertGrpDim(tabId, dimtabId, dimId, grparr);
    }

    private void insertGrpDim(String tabId, String dimtabId, String dimIds, String grpId) {

        boolean flag = false;
        String finalQuery = "";
        ArrayList queries = new ArrayList();
        Object[] Obj = null;
        HashMap dimTabMap = new HashMap();
        HashMap memIdMap = new HashMap();
        try {
            Obj = new Object[3];


            queries = insertGrpDimTables(dimtabId, dimIds, queries, dimTabMap, grpId);
            flag = execMultiple(queries);
            queries.clear();
            queries = insertGrpDimMembers(tabId, dimIds, queries, dimTabMap, memIdMap, grpId);
            flag = execMultiple(queries);
            queries.clear();
            queries = insertGrpDimRel(dimIds, queries, memIdMap, grpId);


            // 
            flag = executeMultiple(queries);
            queries = new ArrayList();


        } catch (Exception e) {
            logger.error("Exception:", e);
            flag = false;
        }
        return;
    }

    private ArrayList insertGrpDimTables(String dimtabId, String dimId, ArrayList queries, HashMap dimTabMap, String grpId) {
        String getQryDimTablesQuery = "select BTMV.buss_table_id ,qdt.dim_tab_id from PRG_QRY_DIM_TABLES QDT, PRG_GRP_BUSS_TABLE_MASTER_VIEW BTMV WHERE "
                + "BTMV.db_table_id= qdt.tab_id AND QDT.dim_id=& and BTMV.grp_id=& and qdt.dim_tab_id= " + dimtabId;
        String finalQuery = "";
        Object[] Obj = null;
        PbReturnObject retObj = null;
        PbReturnObject retObj1 = null;
        String[] tabColNames = null;
        PbDb pbDb = new PbDb();

        try {
            Obj = new Object[2];
            ////.println("dimId\t"+dimId+"\ngrpId\t"+grpId);
            Obj[0] = dimId;
            Obj[1] = grpId;
            finalQuery = buildQuery(getQryDimTablesQuery, Obj);

            retObj = execSelectSQL(finalQuery);

            //modified by Nazneen
//            retObj1 = execSelectSQL("select DIM_TAB_ID from PRG_GRP_DIM_TABLES where DIM_ID in (select DIM_ID from PRG_GRP_DIMENSIONS where QRY_DIM_ID="+dimId+" and GRP_ID="+grpId+")");
            String q1 = "select DIM_TAB_ID from PRG_GRP_DIM_TABLES where DIM_ID in (select DIM_ID from PRG_GRP_DIMENSIONS where QRY_DIM_ID=& and GRP_ID=&)";
            String finalQuery1 = buildQuery(q1, Obj);
            retObj1 = execSelectSQL(finalQuery1);


            tabColNames = retObj.getColumnNames();
            int dimTabId = retObj1.getFieldValueInt(0, 0);
            int oldDimTabId = 0;
            for (int index = 0; index < retObj.getRowCount(); index++) {
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {

                    oldDimTabId = retObj.getFieldValueInt(index, tabColNames[1]);
                    Obj = new Object[2];
                    Obj[0] = dimId;
                    Obj[1] = retObj.getFieldValueString(index, tabColNames[0]);
                    dimTabMap.put(String.valueOf(oldDimTabId), dimTabId);

                    ////.println("insertGrpDimTablesQuery\t" + insertGrpDimTablesQuery);
                } else {
                    oldDimTabId = retObj.getFieldValueInt(index, tabColNames[1]);
                    Obj = new Object[3];
                    Obj[0] = dimTabId;
                    Obj[1] = dimId;
                    Obj[2] = retObj.getFieldValueString(index, tabColNames[0]);
                    //PRG_GRP_DIM_TAB_DETAILS_SEQ.NEXTVAL
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("dimTabId is " + dimTabId + "  oldDimTabId is " + oldDimTabId);
                    dimTabMap.put(String.valueOf(oldDimTabId), dimTabId);

                }
                queries = insertGrpDimTablesDetails(dimtabId, queries, dimTabId, oldDimTabId, grpId);

            }


        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        //return errorFlag;
        return queries;
    }

    private ArrayList insertGrpDimTablesDetails(String dimtabId, ArrayList queries, int dimTabId, int oldDimTabId, String grpId) {
        //String getDimensionQry = rsrcBundle.getString("getDimensionDet");
        String insertGrpDimTablesDetailsQuery = rsrcBundle.getString("insertGrpDimTablesDetails");
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {

            insertGrpDimTablesDetailsQuery = rsrcBundle.getString("insertGrpDimTablesDetails1");
        }
        String finalQuery = "";
        Object[] Obj = null;

        try {
            //commented by Nazneen
//            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)||ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
//                Obj = new Object[2];
//                Obj[0] = oldDimTabId;
//                Obj[1] = grpId;
//            } else {
            Obj = new Object[3];
            Obj[0] = dimTabId;
            Obj[1] = oldDimTabId;
            Obj[2] = grpId;
//            }

            finalQuery = buildQuery(insertGrpDimTablesDetailsQuery, Obj);

            queries.add(finalQuery);

            //queries = insertGrpDimMembers(dimId, queries);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return queries;
    }

    private ArrayList insertGrpDimMembers(String tabId, String dimId, ArrayList queries, HashMap dimTabMap, HashMap memIdMap, String grpId) {
        String insertGrpDimMembersQuery = rsrcBundle.getString("insertGrpDimMembers");
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            insertGrpDimMembersQuery = rsrcBundle.getString("insertGrpDimMembers1");
        }
        String getQryDimMbrsInfoQuery = rsrcBundle.getString("getQryDimMbrsInfo");
        String finalQuery = "";
        PbReturnObject retObj = null;
        PbReturnObject retObj1 = null;
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
            Object[] Obj1 = null;
            Obj1 = new Object[1];
            Obj1[0] = dimId;
            //modified by Nazneen
//          retObj1 = execSelectSQL("select dim_id from PRG_GRP_DIMENSIONS where QRY_DIM_ID="+dimId+");
            String getQryDimId = "select dim_id from PRG_GRP_DIMENSIONS where QRY_DIM_ID=&";
            String finalQryGetQryDimId = buildQuery(getQryDimId, Obj1);
            retObj1 = execSelectSQL(finalQryGetQryDimId);

            Obj = new Object[2];
            Obj[0] = dimId;
            Obj[1] = retObj1.getFieldValueString(0, 0);
            finalQuery = buildQuery(getQryDimMbrsInfoQuery, Obj);

            retObj = execSelectSQL(finalQuery);

            tabColNames = retObj.getColumnNames();
            int newMemId = 0;
            dimId = retObj1.getFieldValueString(0, 0);
            PbReturnObject grpMemIdSeq = null;
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                grpMemIdSeq = super.execSelectSQL("select ident_current('PRG_GRP_DIM_MEMBER')");
            }

            for (int index = 0; index < retObj.getRowCount(); index++) {
                //added by Nazneen
                PbReturnObject retObj2 = null;
                Object[] Obj2 = null;
                Obj2 = new Object[1];
                Obj2[0] = dimId;
                String queryGetDimTabId = "select dim_tab_id from PRG_GRP_DIM_MEMBER where dim_id=&";
                String finalQueryGetDimTabId = buildQuery(queryGetDimTabId, Obj2);
                retObj2 = execSelectSQL(finalQueryGetDimTabId);
                if (retObj2.getRowCount() > 0) {
                    dim_tab_id = retObj2.getFieldValueString(0, 0);
                }

                member_name = retObj.getFieldValueString(index, tabColNames[0]);
                use_denom_table = retObj.getFieldValueString(index, tabColNames[1]);
                denom_tab_id = retObj.getFieldValueString(index, tabColNames[2]);
                denom_query = retObj.getFieldValueString(index, tabColNames[3]);
                //modified by Nazneen
//                dim_tab_id = retObj.getFieldValueString(index, tabColNames[4]);
                mem_id = retObj.getFieldValueString(index, tabColNames[5]);
                memOrderBy = getBussColID(retObj.getFieldValueString(index, tabColNames[6]), grpId);
                mem_isNullVal = retObj.getFieldValueString(index, tabColNames[7]);

                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    //newMemId = grpMemIdSeq.getFieldValueInt(0, 0) + memIndex;
                    memIndex++;
                    Obj = new Object[10];
                    Obj[0] = member_name;
                    Obj[1] = dimId;
                    //modified by Nazneen
//                    Obj[2] =String.valueOf(dimTabMap.get(dim_tab_id));
                    Obj[2] = dim_tab_id;
                    Obj[3] = use_denom_table;
                    Obj[4] = tabId;
                    Obj[5] = grpId;
                    Obj[6] = denom_query;
                    Obj[7] = member_name;
                    if (memOrderBy != null || memOrderBy != "") {
                        Obj[8] = memOrderBy;
                    } else {
                        Obj[8] = null;
                    }
                    Obj[9] = mem_isNullVal;
                } else {
                    newMemId = getSequenceNumber("select PRG_GRP_DIM_MEMBER_seq.nextval from dual");
                    Obj = new Object[11];
                    Obj[0] = newMemId;
                    Obj[1] = member_name;
                    Obj[2] = dimId;
                    //modified by Nazneen
//                    Obj[3] = String.valueOf(dimTabMap.get(dim_tab_id));
                    Obj[3] = dim_tab_id;
                    Obj[4] = use_denom_table;
                    Obj[5] = tabId;
                    Obj[6] = grpId;
                    Obj[7] = denom_query;
                    Obj[8] = member_name;
                    if (memOrderBy != null || memOrderBy != "") {
                        Obj[9] = memOrderBy;
                    } else {
                        Obj[9] = null;
                    }
                    Obj[10] = mem_isNullVal;

                }
                memIdMap.put(String.valueOf(mem_id), newMemId);

                finalQuery = buildQuery(insertGrpDimMembersQuery, Obj);

                queries.add(finalQuery);

                queries = insertGrpDimMembersDtls(mem_id, queries, newMemId, grpId);

            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return queries;
    }

    private ArrayList insertGrpDimMembersDtls(String mem_id, ArrayList queries, int newMemId, String grpId) {
        String insertGrpDimMembersDtlsQuery = rsrcBundle.getString("insertGrpDimMembersDtls");
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            insertGrpDimMembersDtlsQuery = rsrcBundle.getString("insertGrpDimMembersDtls1");
        }
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            insertGrpDimMembersDtlsQuery = rsrcBundle.getString("insertGrpDimMembersDtls2");
        }
        String finalQuery = "";
        Object[] Obj = null;

        try {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                Obj = new Object[2];
                Obj[0] = mem_id;
                Obj[1] = grpId;
                finalQuery = buildQuery(insertGrpDimMembersDtlsQuery, Obj);
            } else {
                Obj = new Object[3];
                Obj[0] = newMemId;
                Obj[1] = mem_id;
                Obj[2] = grpId;
                finalQuery = buildQuery(insertGrpDimMembersDtlsQuery, Obj);
            }


            queries.add(finalQuery);

            //queries = insertGrpDimRel(dimId, queries, mem_id);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return queries;
    }

    private ArrayList insertGrpDimRel(String dimId, ArrayList queries, HashMap memIdMap, String grpId) {
        String getQryDimRelQuery = rsrcBundle.getString("getQryDimRel");
        //String insertGrpDimRelQuery = rsrcBundle.getString("insertGrpDimRel");
        String finalQuery = "";
        Object[] Obj = null;
        PbReturnObject retObj = null;
        String[] tableColNames = null;
        String[] relIds = null;

        try {
            Obj = new Object[1];
            Obj[0] = dimId;
            finalQuery = buildQuery(getQryDimRelQuery, Obj);
            //
            retObj = execSelectSQL(finalQuery);
            tableColNames = retObj.getColumnNames();

            relIds = new String[retObj.getRowCount()];
            for (int index = 0; index < retObj.getRowCount(); index++) {
                relIds[index] = retObj.getFieldValueString(index, tableColNames[3]);
                Obj = new Object[3];
                Obj[0] = dimId;
                Obj[1] = dimId;
                Obj[2] = relIds[index];

                //finalQuery = buildQuery(insertGrpDimRelQuery, Obj);

                //queries.add(finalQuery);
                queries = insertGrpDimRelDtls(dimId, queries, relIds[index], memIdMap, grpId);
            }

        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return queries;
    }

    private ArrayList insertGrpDimRelDtls(String dimId, ArrayList queries, String relId, HashMap memIdMap, String grpId) {
        String getQryDimRelDtlsQuery = rsrcBundle.getString("getQryDimRelDtls");
        String finalQuery = "";
        String insertGrpDimRelDtlsQuery = rsrcBundle.getString("insertGrpDimRelDtls");
        Object[] Obj = null;
        PbReturnObject retObj = null;
        PbReturnObject retObj1 = null;
        String[] tableColNames = null;

        try {
            retObj1 = execSelectSQL("select REL_ID from PRG_GRP_DIM_REL where DIM_ID=(select DIM_ID from PRG_GRP_DIMENSIONS where QRY_DIM_ID=" + dimId + " and GRP_ID=" + grpId + ")");
            Obj = new Object[3];
            Obj[0] = dimId;
            Obj[1] = relId;
            Obj[2] = retObj1.getFieldValueString(0, 0);
            finalQuery = buildQuery(getQryDimRelDtlsQuery, Obj);

            retObj = execSelectSQL(finalQuery);
            tableColNames = retObj.getColumnNames();

            for (int i = 0; i < retObj.getRowCount(); i++) {
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {

                    Obj = new Object[4];
                    Obj[0] = retObj1.getFieldValueString(0, 0);
                    //modified by Nazneen
//                    Obj[1] = memIdMap.get(retObj.getFieldValueString(i, tableColNames[1]));
                    Obj[1] = retObj.getFieldValueString(i, 1);
                    Obj[2] = relId;
//                    Obj[3] = retObj.getFieldValueString(i, tableColNames[1]);
                    Obj[3] = retObj.getFieldValueString(i, 0);

                    finalQuery = buildQuery(insertGrpDimRelDtlsQuery, Obj);
                    //
                    queries.add(finalQuery);
                } else {

                    Obj = new Object[4];
                    Obj[0] = retObj1.getFieldValueString(0, 0);
//                    Obj[1] = memIdMap.get(retObj.getFieldValueString(i, tableColNames[1]));
                    Obj[1] = retObj.getFieldValueString(i, 1);
                    Obj[2] = relId;
//                    Obj[3] = retObj.getFieldValueString(i, tableColNames[1]);
                    Obj[3] = retObj.getFieldValueString(i, 0);
                    // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("finalQuery minor is "+finalQuery);
                    finalQuery = buildQuery(insertGrpDimRelDtlsQuery, Obj);

                    queries.add(finalQuery);
                }



            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return queries;
    }

    private String getBussColID(String memOrderBy, String grpId) {
        PbReturnObject pbReturnObject = new PbReturnObject();
        String bussColId = "";
        try {
            if (!memOrderBy.equalsIgnoreCase("")) {
                pbReturnObject = super.execSelectSQL("select BTD.BUSS_COLUMN_ID from PRG_GRP_BUSS_TABLE_DETAILS BTD , PRG_GRP_BUSS_TABLE BT where BTD.DB_COLUMN_ID = " + memOrderBy + "  AND BT.GRP_ID =" + grpId + " and bt.db_table_id   =btd.db_table_id");
                if (pbReturnObject == null) {
                    bussColId = null;
                } else {
                    bussColId = pbReturnObject.getFieldValueString(0, 0);
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return bussColId;
    }
    public static int refId = 0;

    public void migrateDimensionToRole(String tabId, String dimtabId, String dimId, String grparr) {
        try {
            int foldDtlId = 0;
            PbDb pbDb = new PbDb();
            String[] folderDetailNames = {"Dimensions", "Facts", "Buckets"};
            boolean result = false;

            PbReturnObject retObj1 = null;
            PbReturnObject retObj2 = null;

            retObj1 = execSelectSQL("select FOLDER_ID from PRG_USER_FOLDER where grp_id =" + grparr);
            for (int i = 0; i < retObj1.getRowCount(); i++) {
                ArrayList alist = new ArrayList();

                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {

                    PbReturnObject pbref1 = null;
                    pbref1 = execSelectSQL("select LAST_INSERT_ID(ELEMENT_ID) from PRG_USER_SUB_FOLDER_ELEMENTS order by 1 desc limit 1");
                    refId = pbref1.getFieldValueInt(0, 0);

                }
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {

                    PbReturnObject pbref1 = null;
                    pbref1 = execSelectSQL("select MAX(ELEMENT_ID) from PRG_USER_SUB_FOLDER_ELEMENTS");
                    refId = pbref1.getFieldValueInt(0, 0);

                } else {
                    foldDtlId = getSequenceNumber("select PRG_USER_FOLDER_DETAIL_SEQ.NEXTVAL from dual");
                }
                retObj2 = execSelectSQL("select SUB_FOLDER_ID,SUB_FOLDER_NAME from PRG_USER_FOLDER_DETAIL where FOLDER_ID =" + retObj1.getFieldValueString(i, 0) + " and SUB_FOLDER_NAME='Dimensions'");
                alist = addUserSubFolderTables(alist, grparr, folderDetailNames[0], foldDtlId, dimId, dimtabId, retObj2.getFieldValueString(0, 0));

//            for(int ialist=0;ialist<alist.size();ialist++){
//                
//            execModifySQL((String) alist.get(ialist));
//            }
                result = executeMultiple(alist);
                partialPublishDimentions(dimId, retObj2.getFieldValueString(0, 0), retObj1.getFieldValueString(i, 0), grparr);
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

    }

    public ArrayList addUserSubFolderTables(ArrayList alist, String grpId, String folderType, int folderDtlId, String dimId, String dimtabId, String subFolderId) throws Exception {
        //String addUserSubFolderTablesQuery = rsrcBundle.getString("addUserSubFolderTables");
        String addUserSubFolderTablesForDimensionsQuery = rsrcBundle.getString("addUserSubFolderTablesForDimensions");

        String getBussDimInfoByGrpIdQuery = rsrcBundle.getString("getBussDimInfoByGrpId");
        // String getBussFactsInfoByGrpIdQuery = rsrcBundle.getString("getBussFactsInfoByGrpId");
        // String getBussBucketInfoByGrpIdQuery = rsrcBundle.getString("getBussBucketInfoByGrpId");

        String finalQuery = "";
        PbReturnObject retObj = null;
        PbReturnObject retObj1 = null;
        PbReturnObject retObj2 = null;
        String[] tableColNames = null;
        // 
        //
        //retObj = execSelectSQL("select SUB_FOLDER_TAB_ID from PRG_USER_SUB_FOLDER_TABLES where SUB_FOLDER_ID =" + subFolderId+" and DIM_TAB_ID="+dimtabId);
        retObj1 = execSelectSQL("select DIM_ID from PRG_GRP_DIMENSIONS where QRY_DIM_ID=" + dimId + " and GRP_ID=" + grpId);

        String subFoldTabId = "";
        //subFoldTabId= retObj.getFieldValueString(0, 0);
        //For Dimensions
        if (folderType.equalsIgnoreCase("Dimensions")) {
            Object[] Obj = new Object[4];
            Obj[0] = grpId;
            Obj[1] = retObj1.getFieldValueString(0, 0);
            Obj[2] = retObj1.getFieldValueString(0, 0);
            Obj[3] = subFolderId;
            finalQuery = buildQuery(getBussDimInfoByGrpIdQuery, Obj);

            retObj = execSelectSQL(finalQuery);
            tableColNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {

                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                    this.subFolderTabId += i;
                    Obj = new Object[16];
                    Obj[0] = "IDENT_CURRENT('PRG_USER_FOLDER_DETAIL')";

                    Obj[1] = retObj.getFieldValueString(i, tableColNames[3]);
                    Obj[2] = "Y";
                    Obj[3] = "N";
                    Obj[4] = "N";
                    if (retObj.getFieldValueString(i, tableColNames[1]).equalsIgnoreCase("")) {
                        Obj[5] = "null";
                    } else {
                        Obj[5] = retObj.getFieldValueString(i, tableColNames[1]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[0]).equalsIgnoreCase("")) {
                        Obj[6] = "null";
                    } else {
                        Obj[6] = retObj.getFieldValueString(i, tableColNames[0]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[2]).equalsIgnoreCase("")) {
                        Obj[7] = "null";
                    } else {
                        Obj[7] = retObj.getFieldValueString(i, tableColNames[2]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[4]).equalsIgnoreCase("")) {
                        Obj[8] = "null";
                    } else {
                        Obj[8] = retObj.getFieldValueString(i, tableColNames[4]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[5]).equalsIgnoreCase("")) {
                        Obj[9] = "null";
                    } else {
                        Obj[9] = retObj.getFieldValueString(i, tableColNames[5]);
                    }

                    if (retObj.getFieldValueString(i, tableColNames[6]).equalsIgnoreCase("")) {
                        Obj[10] = "null";
                    } else {
                        Obj[10] = retObj.getFieldValueString(i, tableColNames[6]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[7]).equalsIgnoreCase("")) {
                        Obj[11] = "null";
                    } else {
                        Obj[11] = retObj.getFieldValueString(i, tableColNames[7]);
                    }

                    if (retObj.getFieldValueString(i, tableColNames[8]).equalsIgnoreCase("")) {
                        Obj[12] = "null";
                    } else {
                        Obj[12] = retObj.getFieldValueString(i, tableColNames[8]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[9]).equalsIgnoreCase("")) {
                        Obj[13] = "null";
                    } else {
                        Obj[13] = retObj.getFieldValueString(i, tableColNames[9]);
                    }

                    //modified for TABLE_DISP_NAME and TABLE_TOOLTIP_NAME
                    if (retObj.getFieldValueString(i, tableColNames[1]).equalsIgnoreCase("")) {
                        Obj[14] = "null";
                    } else {
                        Obj[14] = retObj.getFieldValueString(i, tableColNames[1]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[1]).equalsIgnoreCase("")) {
                        Obj[15] = "null";
                    } else {
                        Obj[15] = retObj.getFieldValueString(i, tableColNames[1]);
                    }

                    //end
                    finalQuery = buildQuery(addUserSubFolderTablesForDimensionsQuery, Obj);
//                    subFoldTabId = this.subFolderTabId;

                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
//                    this.subFolderTabId += i;
                    Obj = new Object[16];
                    Obj[0] = "(select LAST_INSERT_ID(sub_folder_id) from PRG_USER_FOLDER_DETAIL order by 1 desc limit 1)";

                    Obj[1] = retObj.getFieldValueString(i, tableColNames[3]);
                    Obj[2] = "Y";
                    Obj[3] = "N";
                    Obj[4] = "N";
                    if (retObj.getFieldValueString(i, tableColNames[1]).equalsIgnoreCase("")) {
                        Obj[5] = "null";
                    } else {
                        Obj[5] = retObj.getFieldValueString(i, tableColNames[1]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[0]).equalsIgnoreCase("")) {
                        Obj[6] = "null";
                    } else {
                        Obj[6] = retObj.getFieldValueString(i, tableColNames[0]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[2]).equalsIgnoreCase("")) {
                        Obj[7] = "null";
                    } else {
                        Obj[7] = retObj.getFieldValueString(i, tableColNames[2]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[4]).equalsIgnoreCase("")) {
                        Obj[8] = "null";
                    } else {
                        Obj[8] = retObj.getFieldValueString(i, tableColNames[4]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[5]).equalsIgnoreCase("")) {
                        Obj[9] = "null";
                    } else {
                        Obj[9] = retObj.getFieldValueString(i, tableColNames[5]);
                    }

                    if (retObj.getFieldValueString(i, tableColNames[6]).equalsIgnoreCase("")) {
                        Obj[10] = "null";
                    } else {
                        Obj[10] = retObj.getFieldValueString(i, tableColNames[6]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[7]).equalsIgnoreCase("")) {
                        Obj[11] = "null";
                    } else {
                        Obj[11] = retObj.getFieldValueString(i, tableColNames[7]);
                    }

                    if (retObj.getFieldValueString(i, tableColNames[8]).equalsIgnoreCase("")) {
                        Obj[12] = "null";
                    } else {
                        Obj[12] = retObj.getFieldValueString(i, tableColNames[8]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[9]).equalsIgnoreCase("")) {
                        Obj[13] = "null";
                    } else {
                        Obj[13] = retObj.getFieldValueString(i, tableColNames[9]);
                    }

                    //modified for TABLE_DISP_NAME and TABLE_TOOLTIP_NAME
                    if (retObj.getFieldValueString(i, tableColNames[1]).equalsIgnoreCase("")) {
                        Obj[14] = "null";
                    } else {
                        Obj[14] = retObj.getFieldValueString(i, tableColNames[1]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[1]).equalsIgnoreCase("")) {
                        Obj[15] = "null";
                    } else {
                        Obj[15] = retObj.getFieldValueString(i, tableColNames[1]);
                    }

                    //end
                    finalQuery = buildQuery(addUserSubFolderTablesForDimensionsQuery, Obj);
//                    subFoldTabId = this.subFolderTabId;

                } else {
                    retObj2 = execSelectSQL("select PRG_USER_SUB_FOLDER_TABLES_SEQ.NEXTVAL from dual");
                    //subFoldTabId = getSequenceNumber("select PRG_USER_SUB_FOLDER_TABLES_SEQ.NEXTVAL from dual");
                    subFoldTabId = retObj2.getFieldValueString(0, 0);

                    Obj = new Object[17];
                    Obj[0] = subFoldTabId;
                    Obj[1] = subFolderId;

                    Obj[2] = retObj.getFieldValueString(i, tableColNames[3]);
                    Obj[3] = "Y";
                    Obj[4] = "N";
                    Obj[5] = "N";
                    Obj[6] = retObj.getFieldValueString(i, tableColNames[1]);
                    Obj[7] = retObj.getFieldValueString(i, tableColNames[0]);
                    Obj[8] = retObj.getFieldValueString(i, tableColNames[2]);
                    Obj[9] = retObj.getFieldValueString(i, tableColNames[4]);
                    Obj[10] = retObj.getFieldValueString(i, tableColNames[5]);
                    Obj[11] = retObj.getFieldValueString(i, tableColNames[6]);
                    Obj[12] = retObj.getFieldValueString(i, tableColNames[7]);
                    Obj[13] = "";
                    Obj[13] = retObj.getFieldValueString(i, tableColNames[8]);
                    Obj[14] = retObj.getFieldValueString(i, tableColNames[9]);
                    //modified for TABLE_DISP_NAME and TABLE_TOOLTIP_NAME
                    Obj[15] = retObj.getFieldValueString(i, tableColNames[1]);

                    Obj[16] = retObj.getFieldValueString(i, tableColNames[1]);


                    //end
                    finalQuery = buildQuery(addUserSubFolderTablesForDimensionsQuery, Obj);
                    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("finalQuery of Dimensions is " + finalQuery);
                }

                alist.add(finalQuery);
                alist = addUserSubFolderElements(alist, retObj.getFieldValueString(i, tableColNames[3]), subFolderId, String.valueOf(subFoldTabId), grpId, String.valueOf(folderDtlId));
            }
            //added by sunita start. To insert in custom Drill
            String getAllMemRel = rsrcBundle.getString("getAllMemRel");
            Object grpObj[] = new Object[1];
            grpObj[0] = grpId;
            String fingetAllMemRel = buildQuery(getAllMemRel, grpObj);
            String addRoleCustomDrill = rsrcBundle.getString("getRoleCustomDrill");
            String insertRoleCustomDrillData = null;
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                insertRoleCustomDrillData = rsrcBundle.getString("addRoleCustomDrill1");
            } else {
                insertRoleCustomDrillData = rsrcBundle.getString("addRoleCustomDrill");
            }


            String finaddRoleCustomDrill = buildQuery(addRoleCustomDrill, grpObj);
            PbReturnObject roleDrillobj = execSelectSQL(finaddRoleCustomDrill);
            Vector insertable = new Vector();
            // for (int m = 0; m < roleDrillobj.getRowCount(); m++) {
            //    insertable.add(roleDrillobj.getFieldValueString(m, "MEMBER_ID"));
            // }
            String finALl = buildQuery(addRoleCustomDrill, grpObj);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" finALl --.."+finALl);
            PbReturnObject allDims = execSelectSQL(finALl);
            for (int m = 0; m < roleDrillobj.getRowCount(); m++) {
//                insertable.add(roleDrillobj.getFieldValueString(m, "MEMBER_ID"));
                insertable.add(roleDrillobj.getFieldValueString(m, 5));
            }
            ////.println("fingetAllMemRel\t" + fingetAllMemRel);
            PbReturnObject fingetAllMemRelOb = execSelectSQL(fingetAllMemRel);

            HashMap childs = new HashMap();
            HashMap dimMem = null;
            if (fingetAllMemRelOb.rowCount != 0) {
//                String oldDimId = fingetAllMemRelOb.getFieldValueString(0, "DIM_ID");
                String oldDimId = fingetAllMemRelOb.getFieldValueString(0, 0);
                for (int n = 0; n < fingetAllMemRelOb.getRowCount(); n++) {

//                    String memId = fingetAllMemRelOb.getFieldValueString(n, "MEM_ID");
                    String memId = fingetAllMemRelOb.getFieldValueString(n, 2);
                    String dimid = "";
                    String childId = "";
                    int next = n + 1;
//                    String chElementId = fingetAllMemRelOb.getFieldValueString(n, "MEM_ID");
                    String chElementId = fingetAllMemRelOb.getFieldValueString(n, 2);
                    if (next < fingetAllMemRelOb.getRowCount()) {
//                        dimid = fingetAllMemRelOb.getFieldValueString(next, "DIM_ID");
                        dimid = fingetAllMemRelOb.getFieldValueString(next, 3);
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(chElementId+" oldDimId- "+oldDimId+" dimId "+dimId);
                        if (oldDimId.equalsIgnoreCase(dimid)) {
                            // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" in if 2");
//                            chElementId = fingetAllMemRelOb.getFieldValueString(next, "MEM_ID");
                            chElementId = fingetAllMemRelOb.getFieldValueString(next, 2);

                            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" chElementId "+chElementId);
                        }
                    }
                    oldDimId = dimid;
                    childs.put(memId, chElementId);
                }
                dimMem = new HashMap();
                ArrayList det2 = new ArrayList();

                for (int n = 0; n < fingetAllMemRelOb.getRowCount(); n++) {

//                    String memId = fingetAllMemRelOb.getFieldValueString(n, "MEM_ID");
//                    String dimid = fingetAllMemRelOb.getFieldValueString(n, "DIM_ID");
//                    String chElementId = fingetAllMemRelOb.getFieldValueString(n, "MEM_ID");
                    String memId = fingetAllMemRelOb.getFieldValueString(n, 2);
                    String dimid = fingetAllMemRelOb.getFieldValueString(n, 3);
                    String chElementId = fingetAllMemRelOb.getFieldValueString(n, 2);
                    if (dimMem.containsKey(dimid)) {
                        det2 = (ArrayList) dimMem.get(dimid);
                        det2.add(memId);
                        dimMem.put(dimid, det2);
                    } else {
                        det2 = new ArrayList();
                        det2.add(memId);
                        dimMem.put(dimid, det2);
                    }
                }

            }


            //   ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" childs ..."+childs);
            for (int n = 0; n < roleDrillobj.getRowCount(); n++) {
//                String dimid = roleDrillobj.getFieldValueString(n, "DIM_ID");
//                String memId = roleDrillobj.getFieldValueString(n, "MEMBER_ID");
                String dimid = roleDrillobj.getFieldValueString(n, 0);
                String memId = roleDrillobj.getFieldValueString(n, 5);
                if (childs.containsKey(memId)) {
                    String chId = childs.get(memId).toString();
                    if (dimMem.containsKey(dimid)) {
                        ArrayList det = (ArrayList) dimMem.get(dimid);
                        if (!det.contains(chId)) {
                            childs.put(memId, memId);
                        }
                    }
                }
            }
            //  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(dimMem+" childs "+childs);//folderDtlId
            ArrayList insertRoleDrillList = new ArrayList();
            for (int k = 0; k < insertable.size(); k++) {
                String memId = insertable.get(k).toString();
                String childId = memId;
                if (childs.containsKey(memId)) {
                    childId = childs.get(memId).toString();
                }
                Object insertOb[] = null;
                String finaddRoleCustomDrillData = "";
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    insertOb = new Object[4];
                    insertOb[0] = "null";
//                    if (String.valueOf(folderDtlId) != null) {
//                        insertOb[1] = folderDtlId;
//                    } else {
                    insertOb[1] = "IDENT_CURRENT('PRG_USER_FOLDER_DETAIL')";
//                    }
                    if (memId != null) {
                        insertOb[2] = memId;
                    } else {
                        insertOb[2] = "null";
                    }
                    if (childId != null) {
                        insertOb[3] = childId;
                    } else {
                        insertOb[3] = "null";
                    }

                    finaddRoleCustomDrillData = buildQuery(insertRoleCustomDrillData, insertOb);
                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    insertOb = new Object[4];
                    insertOb[0] = "null";
                    if (String.valueOf(folderDtlId) != null) {
                        insertOb[1] = folderDtlId;
                    } else {
                        insertOb[1] = "(select Last_Insert_Id(FOLDER_ID) from PRG_USER_FOLDER_DETAIL order by 1 desc limit 1)";
                    }
                    if (memId != null) {
                        insertOb[2] = memId;
                    } else {
                        insertOb[2] = "null";
                    }
                    if (childId != null) {
                        insertOb[3] = childId;
                    } else {
                        insertOb[3] = "null";
                    }

                    finaddRoleCustomDrillData = buildQuery(insertRoleCustomDrillData, insertOb);
                } else {
                    insertOb = new Object[4];
                    insertOb[0] = "";
                    insertOb[1] = folderDtlId;
                    insertOb[2] = memId;
                    insertOb[3] = childId;
                    finaddRoleCustomDrillData = buildQuery(insertRoleCustomDrillData, insertOb);
                }

                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" finaddRoleCustomDrillData "+finaddRoleCustomDrillData);
                insertRoleDrillList.add(finaddRoleCustomDrillData);
            }
            //
            executeMultiple(insertRoleDrillList);
            //added by suneeta over. To insert in custom Drill


        }
        return alist;
    }

    public ArrayList addUserSubFolderElements(ArrayList alist, String bussTableId, String subFolderId, String subFoldTabId, String grpId, String folderDtlId) throws Exception {
        String addUserSubFolderElementsQuery = rsrcBundle.getString("addUserSubFolderElements");
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            addUserSubFolderElementsQuery = rsrcBundle.getString("addUserSubFolderElementsMysql");
        }
        String finalQuery;
        String getUserSubFolderElementsQuery = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            getUserSubFolderElementsQuery = rsrcBundle.getString("getUserSubFolderElements");
        }
//        PbReturnObject retObj1 = null;
//retObj1 = execSelectSQL("select distinct SUB_FOLDER_TAB_ID from PRG_USER_SUB_FOLDER_ELEMENTS where  SUB_FOLDER_ID="+subFolderId);

        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//            this.subFolderEleId = this.subFolderEleId + 1;
            Object[] Obj = new Object[5];
            Obj[0] = "IDENT_CURRENT('PRG_USER_FOLDER_DETAIL')";//
            Obj[1] = "IDENT_CURRENT('PRG_USER_SUB_FOLDER_TABLES')";
            Obj[2] = "IDENT_CURRENT('PRG_USER_SUB_FOLDER_ELEMENTS')";//
            Obj[3] = bussTableId;
            Obj[4] = "IDENT_CURRENT('PRG_USER_SUB_FOLDER_TABLES')";
            finalQuery = buildQuery(addUserSubFolderElementsQuery, Obj);
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
//            this.subFolderEleId = this.subFolderEleId + 1;
            //  PbReturnObject pbref = execSelectSQL("select LAST_INSERT_ID(ELEMENT_ID) from PRG_USER_SUB_FOLDER_ELEMENTS order by 1 desc limit 1");
            PbReturnObject pbref = null;
            //modified by Nazneen
            Object[] Obj = new Object[1];
//                    Object[] Obj = new Object[2];
            // Obj[0] = "(select LAST_INSERT_ID(sub_folder_id) from PRG_USER_FOLDER_DETAIL order by 1 desc limit 1)";//
            //Obj[1] = "(select LAST_INSERT_ID(SUB_FOLDER_TAB_ID) from PRG_USER_SUB_FOLDER_TABLES order by 1 desc limit 1)";
            //Obj[2] = refid ;
            // "(select LAST_INSERT_ID(ELEMENT_ID) from PRG_USER_SUB_FOLDER_ELEMENTS order by 1 desc limit 1)";//
            Obj[0] = bussTableId;
//            Obj[1] = subFolderId;
            finalQuery = buildQuery(getUserSubFolderElementsQuery, Obj);

            pbref = execSelectSQL(finalQuery);

            for (int k = 0; k < pbref.getRowCount(); k++) {
                Obj = new Object[13];
                Obj[0] = "(select LAST_INSERT_ID(sub_folder_id) from PRG_USER_FOLDER_DETAIL order by 1 desc limit 1)";//
                Obj[1] = pbref.getFieldValue(k, 0);
                Obj[2] = pbref.getFieldValue(k, 1);
                Obj[3] = pbref.getFieldValue(k, 2);
                Obj[4] = pbref.getFieldValue(k, 3);
                Obj[5] = pbref.getFieldValue(k, 4);
                Obj[6] = pbref.getFieldValue(k, 5);
                Obj[7] = "(select LAST_INSERT_ID(SUB_FOLDER_TAB_ID) from PRG_USER_SUB_FOLDER_TABLES order by 1 desc limit 1)";
                Obj[8] = String.valueOf(refId + 1);
                Obj[9] = pbref.getFieldValue(k, 6);
                if (pbref.getFieldValue(k, 8) == null || pbref.getFieldValue(k, 7).equals("")) {
                    Obj[10] = null;
                } else {
                    Obj[10] = pbref.getFieldValue(k, 7);
                }
                Obj[11] = 'Y';
                if (pbref.getFieldValue(k, 8) == null) {
                    Obj[12] = null;
                } else {
                    Obj[12] = pbref.getFieldValue(k, 8);
                }
                finalQuery = buildQuery(addUserSubFolderElementsQuery, Obj);

                alist.add(finalQuery);
                refId++;
            }
        } else {
            //modified by Nazneen
//            Object[] Obj = new Object[4];
            Object[] Obj = new Object[3];
            Obj[0] = subFolderId;
            Obj[1] = subFoldTabId;
            Obj[2] = bussTableId;
//            Obj[3] = subFolderId;
            finalQuery = buildQuery(addUserSubFolderElementsQuery, Obj);
        }

        alist.add(finalQuery);

        return alist;

    }

    public boolean partialPublishDimentions(String dimid, String subfolderid, String folderid, String grpId) {
        PbReturnObject returnObject = null;
        PbReturnObject retObj1 = null;
        String busstableid = null;
        boolean result = false;

        //String query="select distinct buss_table_id from PRG_USER_SUB_FOLDER_TABLES where dim_id="+dimid;
        try {
            retObj1 = execSelectSQL("select DIM_ID from PRG_GRP_DIMENSIONS where QRY_DIM_ID=" + dimid + " and GRP_ID=" + grpId);
            String query = "select distinct buss_table_id from PRG_USER_SUB_FOLDER_TABLES where dim_id=" + retObj1.getFieldValueString(0, 0);
            returnObject = super.execSelectSQL(query);
            busstableid = returnObject.getFieldValueString(0, 0);
            partialPublish(busstableid, folderid);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return result;
    }

    public void partialPublish(String busstableid, String folderid) {
        String finalquery = null;
        String insertinUserAllInfoDetails = rsrcBundle.getString("insertinUserAllInfoDetails");
        String insertintoallAdimDetails = rsrcBundle.getString("insertintoallAdimDetails");
        String insertintoallAdimKeyValueElements = rsrcBundle.getString("insertintoallAdimKeyValueElements");
        String insertintoallAdimMasterdetails = rsrcBundle.getString("insertintoallAdimMasterdetails");
        String insertallDdimDetails = rsrcBundle.getString("insertallDdimDetails");
        String insertallDdimkeyValueElement = rsrcBundle.getString("insertallDdimkeyValueElement");
        String insertallDdimMasterDetails = rsrcBundle.getString("insertallDdimMasterDetails");
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            insertinUserAllInfoDetails = rsrcBundle.getString("insertinUserAllInfoDetailsMysql");
            insertintoallAdimDetails = rsrcBundle.getString("insertintoallAdimDetailsMysql");
            insertintoallAdimKeyValueElements = rsrcBundle.getString("insertintoallAdimKeyValueElementsMysql");
            insertintoallAdimMasterdetails = rsrcBundle.getString("insertintoallAdimMasterdetailsMysql");
            insertallDdimDetails = rsrcBundle.getString("insertallDdimDetailsMysql");
            insertallDdimkeyValueElement = rsrcBundle.getString("insertallDdimkeyValueElementMysql");
            insertallDdimMasterDetails = rsrcBundle.getString("insertallDdimMasterDetailsMysql");
        }

        //added by Nazneen
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            insertinUserAllInfoDetails = rsrcBundle.getString("insertinUserAllInfoDetailsSqlServer");
            insertintoallAdimDetails = rsrcBundle.getString("insertintoallAdimDetailsSqlServer");
            insertintoallAdimKeyValueElements = rsrcBundle.getString("insertintoallAdimKeyValueElementsSqlServer");
            insertintoallAdimMasterdetails = rsrcBundle.getString("insertintoallAdimMasterdetailsSqlServer");
            insertallDdimDetails = rsrcBundle.getString("insertallDdimDetailsSqlServer");
            insertallDdimkeyValueElement = rsrcBundle.getString("insertallDdimkeyValueElementSqlServer");
            insertallDdimMasterDetails = rsrcBundle.getString("insertallDdimMasterDetailsSqlServer");
        }

        Object obj[] = new Object[2];
        obj[0] = folderid;
        obj[1] = busstableid;
        Object keyvalueeleobj[] = new Object[4];
        keyvalueeleobj[0] = folderid;
        keyvalueeleobj[1] = folderid;
        keyvalueeleobj[2] = busstableid;
        keyvalueeleobj[3] = busstableid;
        ArrayList partialpublishquerylist = new ArrayList();
        finalquery = buildQuery(insertinUserAllInfoDetails, obj);

        partialpublishquerylist.add(finalquery);
        finalquery = buildQuery(insertintoallAdimDetails, obj);

        partialpublishquerylist.add(finalquery);
        finalquery = buildQuery(insertintoallAdimKeyValueElements, keyvalueeleobj);

        partialpublishquerylist.add(finalquery);
        finalquery = buildQuery(insertintoallAdimMasterdetails, obj);

        partialpublishquerylist.add(finalquery);
        finalquery = buildQuery(insertallDdimDetails, obj);

        partialpublishquerylist.add(finalquery);
        finalquery = buildQuery(insertallDdimkeyValueElement, keyvalueeleobj);

        partialpublishquerylist.add(finalquery);
        finalquery = buildQuery(insertallDdimMasterDetails, obj);

        partialpublishquerylist.add(finalquery);
        executeMultiple(partialpublishquerylist);

        return;

    }
//end of sunita code
}
