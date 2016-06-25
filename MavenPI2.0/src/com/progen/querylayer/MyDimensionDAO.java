package com.progen.querylayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

public class MyDimensionDAO {

    public static Logger logger = Logger.getLogger(MyDimensionDAO.class);

    public static ArrayList getDimensions(String connId) {
        ArrayList finalList = new ArrayList();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement allDimensions=null;
        try {
            

//            String parentName = "";
            con = ProgenConnection.getInstance().getConnection();
            String sql = "select * from PRG_QRY_DIMENSIONS where connection_id in (?)";
//            Statement allDimensions = con.createStatement();
             allDimensions = con.prepareStatement(sql);
           allDimensions.setString(1, connId);  
            //  String sql = "select * from PRG_QRY_DIMENSIONS";
            // String connIdnew=connId;
            //    String sql ="SELECT DIMENSION_ID, DIMENSION_NAME  FROM PRG_QRY_DIMENSIONS where DIMENSION_ID";
            //    sql +="  in(select DIM_ID from PRG_QRY_DIM_TABLES where TAB_ID in";
            //  sql +="   (select TABLE_ID from PRG_DB_MASTER_TABLE where CONNECTION_ID in("+connId+")))";
            rs = allDimensions.executeQuery(sql);

            while (rs.next()) {
                Dimension dimension = new Dimension();
                dimension.setDimensionName(rs.getString("DIMENSION_NAME"));
                dimension.setDimensionId(rs.getString("DIMENSION_ID"));

                ArrayList list = new ArrayList();

                list = getTableList(rs.getString("DIMENSION_ID"));
                dimension.setTableList(list);
                ArrayList membersList = new ArrayList();
                membersList = getMembersList(rs.getString("DIMENSION_ID"));
                dimension.setMembersList(membersList);
                ArrayList hierarchyList = new ArrayList();
                hierarchyList = getHeirarchyList(rs.getString("DIMENSION_ID"));
                dimension.setHierarchyList(hierarchyList);

                finalList.add(dimension);
            }
             if(con!=null){
               con.close();
            }
            if(allDimensions!=null){
                allDimensions.close();
            }
            if(rs!=null){
                rs.close();
            }
            
            
        } catch (SQLException e) {
            logger.error("Exception:", e);
        }

        return finalList;
    }

    public static ArrayList getTableList(String dimId) {
        Connection con = null;
        ArrayList list = new ArrayList();
        try {

            String sql = "select q.tab_id,d.table_name, q.dim_id, q.dim_tab_id from prg_qry_dim_tables q , prg_db_master_table d where q.tab_id= d.table_id and q.dim_id=?";
            con = ProgenConnection.getInstance().getConnection();
            PreparedStatement st = con.prepareCall(sql);
            st.setString(1, dimId);
            ResultSet rs = st.executeQuery();
            PbReturnObject allTableObject = new PbReturnObject(rs);
            String[] tableNames = null;
            tableNames = allTableObject.getColumnNames();
            String[] tabId = new String[allTableObject.getRowCount()];
            String[] tabName = new String[allTableObject.getRowCount()];
            String[] originaltabId = new String[allTableObject.getRowCount()];
            for (int i = 0; i < allTableObject.getRowCount(); i++) {
                originaltabId[i] = String.valueOf(allTableObject.getFieldValueInt(i, tableNames[0]));
                tabId[i] = String.valueOf(allTableObject.getFieldValueInt(i, tableNames[3]));
                tabName[i] = allTableObject.getFieldValueString(i, tableNames[1]);

            }
            String[] colNames = null;
            String colName = "";
            String tabName1 = "";
            String tabId1 = "";
            ResultSet columnsList = null;
            String sqlcolumns = "select m.column_id,m.table_col_name,d.is_pk_key,d.is_available from prg_db_master_table_details m, prg_qry_dim_tab_details d where m.column_id= d.col_id and d.dim_tab_id=?";
            PreparedStatement stmt = con.prepareCall(sqlcolumns);
           
            for (int i = 0; i < tabId.length; i++) {
                tabName1 = tabName[i];
                tabId1 = tabId[i];
                 stmt.setString(1, tabId[i]);
                columnsList = stmt.executeQuery();

//                int rowCountFlag = 0;
                PbReturnObject pbReturnObject = new PbReturnObject(columnsList);
                DimensionTable table = new DimensionTable();
                table.setTableName(tabName1);
                table.setTableId(tabId1 + "," + originaltabId[i] + "," + dimId);
                list.add(table);
                colNames = pbReturnObject.getColumnNames();
                String columnId = "";
                String is_pk = "";
                String is_available = "";
                for (int j = 0; j < pbReturnObject.getRowCount(); j++) {
                    colName = pbReturnObject.getFieldValueString(j, colNames[1]);
                    columnId = String.valueOf(pbReturnObject.getFieldValueInt(j, colNames[0])) + "," + tabId1 + "," + originaltabId[i] + "," + dimId;
                    is_pk = pbReturnObject.getFieldValueString(j, colNames[2]);
                    is_available = pbReturnObject.getFieldValueString(j, colNames[3]);

                    DimensionTable secTable = new DimensionTable();
                    if (j + 1 == pbReturnObject.getRowCount()) {
                        secTable.setEndTable("true");
                    }
                    secTable.setColumnName(colName);
                    secTable.setColumnId(columnId);
                    secTable.setIsPk(is_pk);
                    secTable.setIsAvailable(is_available);
                    list.add(secTable);
                }
            }
            if (con != null) {
                con.close();
            }
            if (st != null) {
                st.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (columnsList != null) {
                columnsList.close();
            }
        } catch (SQLException e) {
            logger.error("Exception:", e);
        }
        return list;
    }

    public static ArrayList getMembersList(String dimId) {
        Connection con = null;
        ArrayList list = new ArrayList();
        try {
            String sql = "select member_id, member_name from prg_qry_dim_member where dim_id=?";
            con = ProgenConnection.getInstance().getConnection();
            PreparedStatement st = con.prepareCall(sql);
            st.setString(1, dimId);
            ResultSet rs = st.executeQuery();
            PbReturnObject allMembersObject = new PbReturnObject(rs);
            String[] MemberNames = null;
            MemberNames = allMembersObject.getColumnNames();
            String[] memId = new String[allMembersObject.getRowCount()];
            String[] memName = new String[allMembersObject.getRowCount()];
            for (int i = 0; i < allMembersObject.getRowCount(); i++) {
                memId[i] = String.valueOf(allMembersObject.getFieldValueInt(i, MemberNames[0]));
                memName[i] = allMembersObject.getFieldValueString(i, MemberNames[1]);
            }
            String memName1 = "";
            String memId1 = "";

            for (int i = 0; i < memId.length; i++) {
                memName1 = memName[i];
                memId1 = memId[i];
                DimensionTable table = new DimensionTable();
                table.setMemberName(memName1);
                table.setMemberId(memId1);
                list.add(table);
            }
            if (con != null) {
                con.close();
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            logger.error("Exception:", e);
        }
        return list;
    }

    public static ArrayList getHeirarchyList(String dimId) {
        Connection con = null;
        ArrayList list = new ArrayList();
        try {

            String sql = "select rel_id, rel_name,IS_DEFAULT from prg_qry_dim_rel where dim_id=?";
            con = ProgenConnection.getInstance().getConnection();
            PreparedStatement st = con.prepareCall(sql);
            st.setString(1, dimId);
            ResultSet rs = st.executeQuery(sql);
            PbReturnObject allHierachyObject = new PbReturnObject(rs);
            String[] tableNames = null;
            tableNames = allHierachyObject.getColumnNames();
            String[] relId = new String[allHierachyObject.getRowCount()];
            String[] relName = new String[allHierachyObject.getRowCount()];
            String[] relType = new String[allHierachyObject.getRowCount()];
            for (int i = 0; i < allHierachyObject.getRowCount(); i++) {

                relId[i] = String.valueOf(allHierachyObject.getFieldValueInt(i, tableNames[0]));
                relName[i] = allHierachyObject.getFieldValueString(i, tableNames[1]);
                relType[i] = allHierachyObject.getFieldValueString(i, tableNames[2]);
            }
            String[] colNames = null;
            String colName = "";
            String relName1 = "";
            String relId1 = "";
            String relType1 = "";
            Statement stmt = con.createStatement();
            ResultSet columnsList = null;
            String sqlcolumns = "select m.member_id,m.member_name from prg_qry_dim_member m, prg_qry_dim_rel_details d where m.member_id= d.mem_id and d.rel_id=";
            for (int i = 0; i < relId.length; i++) {
                relName1 = relName[i];
                relId1 = relId[i];
                relType1 = relType[i];
                columnsList = stmt.executeQuery(sqlcolumns + relId[i] + " order by d.rel_level");

                int rowCountFlag = 0;
                PbReturnObject pbReturnObject = new PbReturnObject(columnsList);


                DimensionTable table = new DimensionTable();
                table.setRelationName(relName1);
                table.setRelationId(relId1);
                table.setRelType(relType1);
                if (pbReturnObject.getRowCount() == 0) {
                    table.setEndTable("true");
                }
                list.add(table);
                colNames = pbReturnObject.getColumnNames();
                String columnId = "";
                for (int j = 0; j < pbReturnObject.getRowCount(); j++) {
                    colName = pbReturnObject.getFieldValueString(j, colNames[1]);
                    columnId = String.valueOf(pbReturnObject.getFieldValueInt(j, colNames[0])) + "," + relId1 + "," + dimId;

                    DimensionTable secTable = new DimensionTable();
                    secTable.setEndColumn("true");//newly added
                    if (j + 1 == pbReturnObject.getRowCount()) {
                        secTable.setEndTable("true");
                        secTable.setEndColumn("false");//newly added

                    }
                    secTable.setRelColumnName(colName);
                    secTable.setRelColumnId(columnId);
                    list.add(secTable);
                }


            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            logger.error("Exception:", e);
        }
        return list;
    }
}
