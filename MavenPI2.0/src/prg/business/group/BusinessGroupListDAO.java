package prg.business.group;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.progen.contypes.GetConnectionType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author Bharathi reddy
 *
 * this is for displaying business group existed
 */
public class BusinessGroupListDAO extends PbDb {

//    PbBussGrpResourceBundle resBundle = new PbBussGrpResourceBundle();
    ResourceBundle resourceBundle;
    public static Logger logger = Logger.getLogger(BusinessGroupListDAO.class);

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

    public ArrayList getBusinessGroups(String connId) {
        ArrayList finalList = new ArrayList();
//        PbBussGrpResourceBundle resBundle = new PbBussGrpResourceBundle();
        try {
            PbReturnObject retObj = null;
            String[] colNames = null;
            String sql = getResourceBundle().getString("getBusinessGroupList");
            Object obj[] = new Object[1];
            obj[0] = connId;

            //susheela start 11-12-09
            String timeMapQ = "";
            String timeFlag = "!";

            String finalQuery = buildQuery(sql, obj);

            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                Businessgrps businessgrps = new Businessgrps();
                businessgrps.setBusinessGrpName(retObj.getFieldValueString(i, colNames[1]));
                businessgrps.setBusinessGrpId(String.valueOf(retObj.getFieldValueInt(i, colNames[0])));

                //susheela start 11-12-09
                timeFlag = "!";
                timeMapQ = "SELECT DIM_ID, DIM_NAME, GRP_ID FROM PRG_GRP_DIMENSIONS where grp_id=" + String.valueOf(retObj.getFieldValueInt(i, colNames[0])) + " and dim_name='Time'";
                //////////////////////////////////////////////////////////////////////////////////.println.println(" timeMapQ "+timeMapQ);
                PbReturnObject timObj = execSelectSQL(timeMapQ);
                if (timObj.getRowCount() > 0) {
                    timeFlag = "";
                }
                businessgrps.setGrpTimeFlag(timeFlag);



                ArrayList dimensionList = new ArrayList();
                ArrayList allTablesList = new ArrayList();
                ArrayList factsList = new ArrayList();
                ArrayList bucketsList = new ArrayList();
                ArrayList roleList = new ArrayList();
                dimensionList = getDimensions(String.valueOf(retObj.getFieldValueInt(i, colNames[0])));
                allTablesList = getAllTables(String.valueOf(retObj.getFieldValueInt(i, colNames[0])));
                factsList = getFacts(String.valueOf(retObj.getFieldValueInt(i, colNames[0])));

                //added by susheela 05-oct-09 start
                // ArrayList targetFactsList = new ArrayList();
                // targetFactsList = getAddedTargetMeasures(String.valueOf(retObj.getFieldValueInt(i, colNames[0])));
                ArrayList grpTargetMeasuresList = new ArrayList();
                grpTargetMeasuresList = getAddedTargets(String.valueOf(retObj.getFieldValueInt(i, colNames[0])));
                //added by susheela 05-oct-09 over

                bucketsList = getBuckets(String.valueOf(retObj.getFieldValueInt(i, colNames[0])));
                roleList = getBusinessRoles(String.valueOf(retObj.getFieldValueInt(i, colNames[0])));
                businessgrps.setDimensionList(dimensionList);
                businessgrps.setAllTablesList(allTablesList);
                businessgrps.setFactsList(factsList);
                businessgrps.setBucketsList(bucketsList);
                businessgrps.setRoleList(roleList);
                //added by susheela 05-oct-09 start

                // businessgrps.setTargetFactsList(targetFactsList);
                businessgrps.setGrpTargetMeasuresList(grpTargetMeasuresList);

                //added by susheela 05-oct-09 over

                finalList.add(businessgrps);

            }

        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return finalList;
    }

    //added by susheela start  28-12-09
    public ArrayList getAddedTargets(String busGroupId) throws Exception {
        ArrayList tableName = new ArrayList();
        String getAddedTargetMeas = getResourceBundle().getString("getAddedTargetMeas");
        String getAddedTargets = getResourceBundle().getString("getAddedTargets");

        Object grpObj[] = new Object[1];
        grpObj[0] = busGroupId;
        String fgetAddedTargetMeas = buildQuery(getAddedTargetMeas, grpObj);
        PbReturnObject tarMeas = execSelectSQL(fgetAddedTargetMeas);
        Object measure[] = new Object[1];
        String measureId = "";
        String measureName = "";
        String targetId = "";
        String targetName = "";
        for (int m = 0; m < tarMeas.getRowCount(); m++) {
            BusinessGrpsTreeTable table = new BusinessGrpsTreeTable();
            measureId = tarMeas.getFieldValueString(m, "PRG_MEASURE_ID");
            measureName = tarMeas.getFieldValueString(m, "MEASURE_NAME");
            measure[0] = measureId;
            table.setTargetMeasId(measureId);
            table.setTargetMeasName(measureName);
            String fingetAddedTargets = buildQuery(getAddedTargets, measure);
            PbReturnObject tarObj = execSelectSQL(fingetAddedTargets);
            if (tarObj.getRowCount() == 0) {
                table.setEndTable("true");
            }
            // tableName.add(table);
            tableName.add(table);
            for (int y = 0; y < tarObj.getRowCount(); y++) {
                BusinessGrpsTreeTable sectable = new BusinessGrpsTreeTable();
                targetId = tarObj.getFieldValueString(y, "TARGET_ID");
                targetName = tarObj.getFieldValueString(y, "TARGET_NAME");
                sectable.setTargetId(targetId);
                sectable.setTargetMinTimeLevel(tarObj.getFieldValueString(y, "MIN_TIME_LEVEL"));
                sectable.setTargetName(targetName);

                if (y + 1 == tarObj.getRowCount()) {
                    sectable.setEndTable("true");
                }
                tableName.add(sectable);
            }


        }

        return tableName;

    }

    public void updateFactColumnFlag(String tableId, String columnId) throws Exception {   ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" in update--");
        String currentStatusQ = "select role_flag from prg_grp_buss_table_details where buss_column_id=" + columnId + " and buss_table_id=" + tableId;
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" currentStatusQ "+currentStatusQ);
        PbReturnObject pbro = execSelectSQL(currentStatusQ);
        String oldSt = pbro.getFieldValueString(0, "ROLE_FLAG");
        String newSt = "";
        if (oldSt.equalsIgnoreCase("Y")) {
            newSt = "N";
        } else {
            newSt = "Y";
        }
        String updateQ = "update prg_grp_buss_table_details set role_flag='" + newSt + "' where buss_column_id=" + columnId + " and buss_table_id=" + tableId;
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" updateQ -"+updateQ);
        execModifySQL(updateQ);
    }

    public ArrayList getAddedTargetMeasures(String busGroupId) throws Exception {
        String finalStr = "";
        String getSavedTargetMeasure = getResourceBundle().getString("getSavedTargetMeasure");//getSavedMeasure
        String getSavedMeasure = getResourceBundle().getString("getSavedMeasure");

        ArrayList tableName = new ArrayList();
        Object TabObj[] = new Object[1];
        TabObj[0] = busGroupId;
        String fingetSavedTargetMeasure = buildQuery(getSavedMeasure, TabObj);
        String tabQ = "select col_id,col_name,pgbt.buss_table_name original_table,buss_original_table_id from target_master_col_details tmcd,prg_target_master ptm,prg_grp_buss_table pgbt where tmcd.buss_table_id=ptm.target_table_id and tmcd.selected_measure='Y' and buss_original_table_id = pgbt.buss_table_id and ptm.business_group=" + busGroupId;
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" fingetSavedTargetMeasure tabQ "+tabQ);
// PbReturnObject factRetOb= execSelectSQL(fingetSavedTargetMeasure);
        PbReturnObject factRetOb = execSelectSQL(tabQ);

        for (int o = 0; o < factRetOb.getRowCount(); o++) {
            BusinessGrpsTreeTable table = new BusinessGrpsTreeTable();
            table.setTargetMeasureId(factRetOb.getFieldValueString(o, "COL_ID"));
            table.setTargetMeasureName(factRetOb.getFieldValueString(o, "COL_NAME"));
// table.setMColName(factRetOb.getFieldValueString(o,"COL_NAME"));
            BusinessGrpsTreeTable sectable = new BusinessGrpsTreeTable();
            BusinessGrpsTreeTable terTable = new BusinessGrpsTreeTable();
            for (int h = 0; h < 1; h++) {
                sectable.setTargetFactTableId(factRetOb.getFieldValueString(o, "BUSS_ORIGINAL_TABLE_ID"));
                sectable.setTargetFactTableName(factRetOb.getFieldValueString(o, "ORIGINAL_TABLE"));

                if (h == 0) {
                    sectable.setEndTable("true");
                }
                String getMeasureMembersQ = "select * from prg_target_measure_members where measure_id in(select prg_measure_id from prg_target_master where measure_name='" + factRetOb.getFieldValueString(o, "COL_NAME") + "' and business_group=" + busGroupId + ")";
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" getMeasureMemebersQ "+getMeasureMembersQ);
                PbReturnObject MeasureMemOb = execSelectSQL(getMeasureMembersQ);
                Businessgrps businessgrps = new Businessgrps();
                ArrayList measureMemList = new ArrayList();
                BusinessGrpsTreeTable tempTable = new BusinessGrpsTreeTable();

                tableName.add(table);
                tableName.add(sectable);

            }

        }

        return tableName;
    }

//added by susheela over
    public ArrayList getDimensions(String grpId) {
        ArrayList finalList = new ArrayList();
//        PbBussGrpResourceBundle resBundle = new PbBussGrpResourceBundle();
        try {

            PbReturnObject retObj = null;
            String finalQuery = null;
            String[] colNames = null;
            Object obj[] = new Object[1];
            obj[0] = grpId;
            String sql = getResourceBundle().getString("getBusinessGroupDimList");
            finalQuery = buildQuery(sql, obj);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                BusinessGrpsTreeTable dimension = new BusinessGrpsTreeTable();
                dimension.setDimensionName(retObj.getFieldValueString(i, colNames[1]));
                dimension.setDimensionId(String.valueOf(retObj.getFieldValueInt(i, colNames[0])));
                ArrayList tableList = new ArrayList();

                tableList = getTableList(String.valueOf(retObj.getFieldValueInt(i, colNames[0])), grpId);
                dimension.setDimTableList(tableList);
                ArrayList membersList = new ArrayList();
                membersList = getMembersList(String.valueOf(retObj.getFieldValueInt(i, colNames[0])), grpId);
                dimension.setDimMembersList(membersList);
                ArrayList hierarchyList = new ArrayList();
                hierarchyList = getHeirarchyList(String.valueOf(retObj.getFieldValueInt(i, colNames[0])), grpId);
                dimension.setDimHierarchyList(hierarchyList);
                //.println.println("hierarchy list==" + hierarchyList.size());
                finalList.add(dimension);
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return finalList;
    }

    public ArrayList getTableList(String dimId, String grpId) {

        ArrayList list = new ArrayList();
//        PbBussGrpResourceBundle resBundle = new PbBussGrpResourceBundle();
        try {
            String finalQuery = null;
            String sql = getResourceBundle().getString("getBusinessGroupDimTableList");

            Object obj[] = new Object[1];
            obj[0] = dimId;
            finalQuery = buildQuery(sql, obj);
            PbReturnObject allTableObject = execSelectSQL(finalQuery);
            String[] tableNames = null;
            tableNames = allTableObject.getColumnNames();
            String[] tabId = new String[allTableObject.getRowCount()];
            String[] tabName = new String[allTableObject.getRowCount()];
            String[] originaltabId = new String[allTableObject.getRowCount()];
            for (int i = 0; i < allTableObject.getRowCount(); i++) {
                originaltabId[i] = String.valueOf(allTableObject.getFieldValueInt(i, tableNames[0]));
                tabId[i] = String.valueOf(allTableObject.getFieldValueInt(i, tableNames[3]));
                tabName[i] = allTableObject.getFieldValueString(i, tableNames[1]);
                //.println.println("table name==" + tabName[i] + "  table id===" + tabId[i]);
            }
            String[] colNames = null;
            String colName = "";
            String tabName1 = "";
            String tabId1 = "";
            String sqlcolumns = getResourceBundle().getString("getBusinessGroupDimTableColList");
            for (int i = 0; i < tabId.length; i++) {
                tabName1 = tabName[i];
                tabId1 = tabId[i];
                Object obj1[] = new Object[2];
                obj1[0] = dimId;
                obj1[1] = originaltabId[i];


                finalQuery = buildQuery(sqlcolumns, obj1);
                PbReturnObject pbReturnObject = execSelectSQL(finalQuery);
                BusinessGrpsTreeTable table = new BusinessGrpsTreeTable();
                table.setDimTableName(tabName1);
                table.setDimTableId(tabId1 + "," + originaltabId[i] + "," + grpId);
                //.println.println("sqlcolumns is "+tabName1+"---"+finalQuery);
                list.add(table);
                colNames = pbReturnObject.getColumnNames();
                String columnId = "";
                String is_pk = "";
                String is_available = "";
                String colType = "";
                for (int j = 0; j < pbReturnObject.getRowCount(); j++) {
                    colName = pbReturnObject.getFieldValueString(j, colNames[1]);
                    columnId = String.valueOf(pbReturnObject.getFieldValueInt(j, colNames[0])) + "," + originaltabId[i] + "," + tabId1 + "," + dimId;
                    is_pk = pbReturnObject.getFieldValueString(j, colNames[2]);
                    is_available = pbReturnObject.getFieldValueString(j, colNames[3]);
                    colType = pbReturnObject.getFieldValueString(j, colNames[5]).toUpperCase();
                    //.println.println("table col id in dims-->" + columnId + "col colName--->" + colName);
                    BusinessGrpsTreeTable secTable = new BusinessGrpsTreeTable();
                    if (j + 1 == pbReturnObject.getRowCount()) {
                        secTable.setEndTable("true");
//                //.println.println(""+j);
                    }

                    secTable.setDimColumnName(colName);
                    secTable.setDimColumnId(columnId);
                    secTable.setIsPk(is_pk);
                    secTable.setIsAvailable(is_available);
                    secTable.setColType(colType);
                    list.add(secTable);
                }
            }
        } 
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }       return list;
        }

    public ArrayList getMembersList(String dimId, String grpId) {

        ArrayList list = new ArrayList();
//        PbBussGrpResourceBundle resBundle = new PbBussGrpResourceBundle();
        try {
            String finalQuery = null;
            Object obj1[] = new Object[1];
            obj1[0] = dimId;

            String sql = getResourceBundle().getString("getBusinessGroupDimMemList");
            finalQuery = buildQuery(sql, obj1);
            PbReturnObject allMembersObject = execSelectSQL(finalQuery);
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
                BusinessGrpsTreeTable table = new BusinessGrpsTreeTable();
                table.setDimMemberName(memName1);
                table.setDimMemberId(memId1);
                list.add(table);
            }
        } 
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }       return list;
        }

    public ArrayList getHeirarchyList(String dimId, String grpId) {
        ArrayList list = new ArrayList();
//        PbBussGrpResourceBundle resBundle = new PbBussGrpResourceBundle();
        try {
            String finalQuery = null;
            String sql = getResourceBundle().getString("getBusinessGroupDimHieList");


            Object obj[] = new Object[1];
            obj[0] = dimId;
            finalQuery = buildQuery(sql, obj);
            PbReturnObject allHierachyObject = execSelectSQL(finalQuery);
            String[] tableNames = null;
            tableNames = allHierachyObject.getColumnNames();
            String[] relId = new String[allHierachyObject.getRowCount()];
            String[] relName = new String[allHierachyObject.getRowCount()];
            for (int i = 0; i < allHierachyObject.getRowCount(); i++) {

                relId[i] = String.valueOf(allHierachyObject.getFieldValueInt(i, tableNames[0]));
                relName[i] = allHierachyObject.getFieldValueString(i, tableNames[1]);
                // //.println.println("REL name==" + relName[i] + " REL id===" + relId[i]);
            }
            String[] colNames = null;
            String colName = "";
            String relName1 = "";
            String relId1 = "";
            String sqlcolumns = getResourceBundle().getString("getBusinessGroupDimHieColList");
            for (int i = 0; i < relId.length; i++) {
                relName1 = relName[i];
                relId1 = relId[i];
                Object obj1[] = new Object[1];
                obj1[0] = relId[i];
                finalQuery = buildQuery(sqlcolumns, obj1);
                PbReturnObject pbReturnObject = execSelectSQL(finalQuery);

                BusinessGrpsTreeTable table = new BusinessGrpsTreeTable();
                table.setDimRelationName(relName1);
                table.setDimRelationId(relId1);
                if (pbReturnObject.getRowCount() == 0) {
                    table.setEndTable("true");
                }
                list.add(table);
                colNames = pbReturnObject.getColumnNames();
                String columnId = "";
                for (int j = 0; j < pbReturnObject.getRowCount(); j++) {
                    colName = pbReturnObject.getFieldValueString(j, colNames[1]);
                    columnId = String.valueOf(pbReturnObject.getFieldValueInt(j, colNames[0])) + "," + relId;
                    //     //.println.println("rel col namee-->" + colName);
                    BusinessGrpsTreeTable secTable = new BusinessGrpsTreeTable();
                    secTable.setEndColumn("true");
                    if (j + 1 == pbReturnObject.getRowCount()) {
                        secTable.setEndTable("true");
                        secTable.setEndColumn("false");
//                //.println.println(""+j);
                    }
                    secTable.setDimRelColumnName(colName);
                    secTable.setDimRelColumnId(columnId);
                    list.add(secTable);
                }
            }
        } 
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }       ////.println.println("size of hierachies--->"+list.size());
        return list;
    }

    public ArrayList getAllTables(String grpId) {
        ArrayList list = new ArrayList();
//        PbBussGrpResourceBundle resBundle = new PbBussGrpResourceBundle();
        try {
            String finalQuery = null;
            String sql = getResourceBundle().getString("getBusinessGroupAllTablesList");

            Object obj[] = new Object[1];
            obj[0] = grpId;
            finalQuery = buildQuery(sql, obj);
            PbReturnObject allTableObject = execSelectSQL(finalQuery);
            String[] tableNames = null;
            tableNames = allTableObject.getColumnNames();
            String[] tabId = new String[allTableObject.getRowCount()];
            String[] tabName = new String[allTableObject.getRowCount()];
            String[] dbtabId = new String[allTableObject.getRowCount()];
            for (int i = 0; i < allTableObject.getRowCount(); i++) {
                dbtabId[i] = String.valueOf(allTableObject.getFieldValueInt(i, tableNames[2]));
                tabId[i] = String.valueOf(allTableObject.getFieldValueInt(i, tableNames[0]));
                tabName[i] = allTableObject.getFieldValueString(i, tableNames[1]);
            }
            String[] colNames = null;
            String colName = "";
            String tabName1 = "";
            String tabId1 = "";
            String sqlcolumns = getResourceBundle().getString("getBusinessGroupAllTablesColList");
            for (int i = 0; i < tabId.length; i++) {
                tabName1 = tabName[i];
                tabId1 = tabId[i];
                Object obj1[] = new Object[1];
                obj1[0] = tabId[i];
                finalQuery = buildQuery(sqlcolumns, obj1);

                PbReturnObject pbReturnObject = execSelectSQL(finalQuery);
                BusinessGrpsTreeTable table = new BusinessGrpsTreeTable();
                table.setAllTableName(tabName1);
                table.setAllTableId(tabId1 + "," + dbtabId[i] + "," + grpId);
                table.setActTabId(dbtabId[i] + "-" + grpId);
                table.setBussTblId(tabId1);
                list.add(table);
                colNames = pbReturnObject.getColumnNames();
                String columnId = "";
                String colType = "";
                String tableName = "";
                ArrayList newSrcList = new ArrayList();
                int tableId = 0;
//                if (i == allTableObject.getRowCount() - 1) {
//                   .println.println("");
//                }
                for (int j = 0; j < pbReturnObject.getRowCount(); j++) {
                    colName = pbReturnObject.getFieldValueString(j, colNames[1]);
                    columnId = String.valueOf(pbReturnObject.getFieldValueInt(j, colNames[0])) + "," + String.valueOf(pbReturnObject.getFieldValueInt(j, colNames[2]));
                    colType = pbReturnObject.getFieldValueString(j, colNames[3]).toUpperCase();
                    //10-12-09
                    String preTabColType = "";
                    if (colType.equalsIgnoreCase("NUMBER")) {
                        preTabColType = "N";
                    }
                    if (colType.equalsIgnoreCase("VARCHAR2")) {
                        preTabColType = "T";
                    }
                    if (colType.equalsIgnoreCase("DATE")) {
                        preTabColType = "D";
                    }

                    tableName = pbReturnObject.getFieldValueString(j, colNames[5]).toUpperCase();
                    ////.println.println("table col id in dims-->" + columnId);
                    BusinessGrpsTreeTable secTable = new BusinessGrpsTreeTable();


                    int tempTblId = 0;
                    tempTblId = pbReturnObject.getFieldValueInt(j, 4);
                    if (tableId == 0 || tempTblId != tableId) {
                        if (tableId == 0) {
                            secTable.setStartTbl("first");
                        } else {
                            secTable.setStartTbl("next");
                        }
                        tableId = tempTblId;
                        secTable.setAllTableName(tableName);
                        secTable.setAllTableId("" + tableId);
                    }

                    secTable.setAllTableColName(colName);
                    secTable.setAllTableColId(columnId);
                    secTable.setAllTableColType(colType);

                    //susheela start 10-12-09
                    secTable.setPreTableColType(preTabColType);

                    if (j + 1 == pbReturnObject.getRowCount()) {
                        secTable.setEndTable("true");
                    }
                    newSrcList.add(secTable);

                }
                BusinessGrpsTreeTable tempTable = new BusinessGrpsTreeTable();
                tempTable.setEndTable("true");
                tempTable.setAllSrcTableList(newSrcList);
                list.add(tempTable);
            }
        }
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        }  catch (Exception e) {
            logger.error("Exception:", e);
        }       return list;
        }

    public ArrayList getFacts(String grpId) {
        ArrayList list = new ArrayList();
//        PbBussGrpResourceBundle resBundle = new PbBussGrpResourceBundle();
        try {
            String finalQuery = null;
            String sql = getResourceBundle().getString("getBusinessGroupFactsList");

            Object obj[] = new Object[2];
            obj[0] = grpId;
            obj[1] = grpId;
            finalQuery = buildQuery(sql, obj);
            //  ////////////////////////////////////////////////////////////////////////.println.println("facts Query---" + finalQuery);
            PbReturnObject allTableObject = execSelectSQL(finalQuery);
            String[] tableNames = null;
            tableNames = allTableObject.getColumnNames();
            String[] tabId = new String[allTableObject.getRowCount()];
            String[] tabName = new String[allTableObject.getRowCount()];
            String[] dbtabId = new String[allTableObject.getRowCount()];
            String sql1 = getResourceBundle().getString("getBusinessGroupFactsformulaList");
            Object obj2[] = new Object[2];
            obj2[0] = grpId;
            obj2[1] = grpId;
            finalQuery = buildQuery(sql1, obj2);
            //////////////////////////////////////////////////////////////////////////.println.println("facts Query-formula--" + finalQuery);
            PbReturnObject allTableObjectformula = execSelectSQL(finalQuery);
            String sqlcolumns = getResourceBundle().getString("getBusinessGroupFactsColList");
            String sqlcolumns1 = getResourceBundle().getString("getBusinessGroupFactsformulaColList");
            PbReturnObject pbReturnObjectformula = null;
            String[] colNamesformula = null;
            String tabNameformula1 = "";
            String tabIdformula1 = "";
            String dbtabIdformula = "";
            if (allTableObjectformula.getRowCount() > 0) {
                tabNameformula1 = allTableObjectformula.getFieldValueString(0, 1);
                tabIdformula1 = String.valueOf(allTableObjectformula.getFieldValueInt(0, 0));
                dbtabIdformula = String.valueOf(allTableObjectformula.getFieldValueInt(0, 2));
                Object obj3[] = new Object[1];
                obj3[0] = tabIdformula1;

                String finalQuery1 = buildQuery(sqlcolumns1, obj3);
                // //////////////////////////////////////////////////////////////////////.println.println("finalQuery1--"+finalQuery1);
                pbReturnObjectformula = execSelectSQL(finalQuery1);
                colNamesformula = pbReturnObjectformula.getColumnNames();
            }

            for (int i = 0; i < allTableObject.getRowCount(); i++) {
                dbtabId[i] = String.valueOf(allTableObject.getFieldValueInt(i, tableNames[2]));
                tabId[i] = String.valueOf(allTableObject.getFieldValueInt(i, tableNames[0]));
                tabName[i] = allTableObject.getFieldValueString(i, tableNames[1]);
                //  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("table name==" + tabName[i] + "  table id===" + tabId[i]);
            }



            String[] colNames = null;

            String colName = "";
            String tabName1 = "";
            String tabId1 = "";


            for (int i = 0; i < tabId.length; i++) {
                ////////////////////////////////////////////////////////////////////////.println.println("-----------------------------------------------------");
                tabName1 = tabName[i];
                tabId1 = tabId[i];
                Object obj1[] = new Object[1];
                obj1[0] = tabId[i];

                finalQuery = buildQuery(sqlcolumns, obj1);
                PbReturnObject pbReturnObject = execSelectSQL(finalQuery);
                //   ////////////////////////////////////////////////////////////////////////.println.println(" finalQuery .."+finalQuery);
                BusinessGrpsTreeTable table = new BusinessGrpsTreeTable();
                table.setFactTableName(tabName1);
                table.setFactTableId(tabId1 + "," + dbtabId[i] + "," + grpId);
                list.add(table);
                colNames = pbReturnObject.getColumnNames();
                String columnId = "";
                // addedby susheela start
                String roleFlag = "";
                // added by susheela over

                //added by bharathi for display formulas


                String colType;
                //  ////////////////////////////////////////////////////////////////////////.println.println("pbReturnObject.getRowCount()--"+pbReturnObject.getRowCount());
                BusinessGrpsTreeTable secTablefinal = new BusinessGrpsTreeTable();
                for (int j = 0; j < pbReturnObject.getRowCount(); j++) {

                    colName = pbReturnObject.getFieldValueString(j, colNames[1]);
                    columnId = String.valueOf(pbReturnObject.getFieldValueInt(j, colNames[0])) + "," + String.valueOf(pbReturnObject.getFieldValueInt(j, colNames[2]) + "," + dbtabId[i] + "," + grpId + "~" + tabName1 + "~" + tabId1);
                    colType = pbReturnObject.getFieldValueString(j, colNames[3]).toUpperCase();
                    // addedby susheela start
                    roleFlag = pbReturnObject.getFieldValueString(j, colNames[4]).toUpperCase();
                    if (roleFlag.equalsIgnoreCase("Y")) {
                        roleFlag = "checked";
                    }
                    // added by susheela over

                    BusinessGrpsTreeTable secTable = new BusinessGrpsTreeTable();
                    if (j + 1 == pbReturnObject.getRowCount() && !(allTableObjectformula.getRowCount() > 0)) {
                        // //////////////////////////////////////////////////////////////////////.println.println("end in main");
                        secTablefinal.setEndTable("true");

                    }

                    //susheela 10-12-09 start
                    String preCol = "";
                    String colNamenew = colName;
                    if (colType.equalsIgnoreCase("Date")) {
                        preCol = "D";
                    } else if (colType.equalsIgnoreCase("NUMBER")) {
                        preCol = "N";
                    } else if (colType.equalsIgnoreCase("VARCHAR2")) {
                        preCol = "T";
                    } else {
                        colName = colNamenew.replaceAll("_", " ");

                    }
                    if (j + 1 == pbReturnObject.getRowCount()) {

                        secTablefinal.setFactTableColName(colName);
                        secTablefinal.setFactTableColId(columnId);
                        secTablefinal.setFactColType(colType);
                        //susheela 10-12-09
                        secTablefinal.setPreColFactType(preCol);
                        secTablefinal.setRoleFlag(roleFlag);
                        if (!(allTableObjectformula.getRowCount() > 0)) {
                            list.add(secTablefinal);
                        }

                    } else {
                        secTable.setFactTableColName(colName);
                        secTable.setFactTableColId(columnId);
                        secTable.setFactColType(colType);
                        //susheela 10-12-09
                        secTable.setPreColFactType(preCol);
                        secTable.setRoleFlag(roleFlag);
                        list.add(secTable);
                    }


                }
                String refElements = "";
                String colList = "";
                if (pbReturnObjectformula != null) {
                    //  ////////////////////////////////////////////////////////////////////////.println.println("pbReturnObjectformula.getRowCount()--"+pbReturnObjectformula.getRowCount());
                    for (int j = 0; j < pbReturnObjectformula.getRowCount(); j++) {

                        colName = pbReturnObjectformula.getFieldValueString(j, colNamesformula[1]);
                        columnId = String.valueOf(pbReturnObjectformula.getFieldValueInt(j, colNamesformula[0])) + "," + String.valueOf(pbReturnObjectformula.getFieldValueInt(j, colNamesformula[2]) + "," + dbtabIdformula + "," + grpId + "~" + tabNameformula1 + "~" + tabIdformula1);
                        colType = pbReturnObjectformula.getFieldValueString(j, colNamesformula[3]).toUpperCase();
                        roleFlag = pbReturnObjectformula.getFieldValueString(j, colNamesformula[4]).toUpperCase();
                        refElements = pbReturnObjectformula.getFieldValueString(j, "REFFERED_ELEMENTS").toUpperCase();
                        // ////////////////////////////////////////////////////////////////////////.println.println("refElements--"+refElements);
                        if (!refElements.equalsIgnoreCase("")) {
                            String refEleList[] = refElements.split(",");
                            for (int j1 = 0; j1 < refEleList.length; j1++) {
                                if (refEleList[j1].startsWith("M-")) {
                                    String detlist[] = refEleList[j1].substring(2).split("-");
                                    String bussTableId = detlist[0];
                                    String bussColId = detlist[1];
                                    String memId = detlist[2];
                                    colList += "," + bussColId;
                                } else {
                                    //  //////////////////////////////////////////////////////////////////////.println.println("IN ELSE PART");
                                    String detlist[] = refEleList[j1].split("-");
                                    String bussTableId = detlist[0];
                                    String bussColId = detlist[1];
                                    colList += "," + bussColId;
                                }
                            }
                            if (!colList.equalsIgnoreCase("")) {
                                colList = colList.substring(1);

                            }
                        }

                        if (!colList.equalsIgnoreCase("")) {

                            String testQuery = "SELECT distinct BUSS_TABLE_ID FROM PRG_GRP_BUSS_TABLE_details where BUSS_COLUMN_ID in(" + colList + ") and BUSS_TABLE_ID=" + tabId1;
                            // //////////////////////////////////////////////////////////////////////.println.println("sql-----" + testQuery);
                            PbReturnObject testpbro = execSelectSQL(testQuery);
                            if (testpbro.getRowCount() > 0) {
                                if (roleFlag.equalsIgnoreCase("Y")) {
                                    roleFlag = "checked";
                                }


                                BusinessGrpsTreeTable secTable = new BusinessGrpsTreeTable();
                                if (j + 1 == pbReturnObjectformula.getRowCount()) {
                                    // //////////////////////////////////////////////////////////////////////.println.println("end ");
                                    secTable.setEndTable("true");

                                }

                                String preCol = "";
                                String colNamenew = colName;
                                if (colType.equalsIgnoreCase("Date")) {
                                    preCol = "D";
                                } else if (colType.equalsIgnoreCase("NUMBER")) {
                                    preCol = "N";
                                } else if (colType.equalsIgnoreCase("VARCHAR2")) {
                                    preCol = "T";
                                } else {
                                    colName = colNamenew.replaceAll("_", " ");

                                }

                                secTable.setFactTableColName(colName);
                                secTable.setFactTableColId(columnId);
                                secTable.setFactColType(colType);
                                //susheela 10-12-09
                                secTable.setPreColFactType(preCol);
                                secTable.setRoleFlag(roleFlag);

                                list.add(secTable);
                            } else {
                                // //////////////////////////////////////////////////////////////////////.println.println("in else end");
                                secTablefinal.setEndTable("true");
                                list.add(secTablefinal);


                            }
                        }

                    }
                }


            }
        } 
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }       return list;
        }

    public ArrayList getBusinessRoles(String grpId) {
        ArrayList list = new ArrayList();
//        PbBussGrpResourceBundle resBundle = new PbBussGrpResourceBundle();
        try {
            String finalQuery = null;
            String sql = getResourceBundle().getString("getBusinessRolesList");

            Object obj[] = new Object[1];
            obj[0] = grpId;

            finalQuery = buildQuery(sql, obj);
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("business Roles --->"+finalQuery);
            //susheela start 11-12-09
            String getExtraDimsForRole = getResourceBundle().getString("getExtraDimsForRole");
            String getExtraBussTable = getResourceBundle().getString("getExtraBussTable");
            String roleColor = "Green";


            PbReturnObject allTableObject = execSelectSQL(finalQuery);
            String[] tableNames = null;
            tableNames = allTableObject.getColumnNames();
            String[] roleId = new String[allTableObject.getRowCount()];
            String[] roleName = new String[allTableObject.getRowCount()];

            for (int i = 0; i < allTableObject.getRowCount(); i++) {
                //susheela start 11-12-09
                roleColor = "==";
                Object updateObj[] = new Object[2];
                updateObj[0] = grpId;
                updateObj[1] = String.valueOf(allTableObject.getFieldValueInt(i, tableNames[0]));
                String fingetExtraDimsForRole = buildQuery(getExtraDimsForRole, updateObj);
                String fingetExtraBussTable = buildQuery(getExtraBussTable, updateObj);
                //////////////////////////////////////////////////////////////////////////////////.println.println(" fingetExtraDimsForRole " +fingetExtraDimsForRole);
                //////////////////////////////////////////////////////////////////////////////////.println.println(" fingetExtraBussTable " +fingetExtraBussTable);
                PbReturnObject extraDimObj = execSelectSQL(fingetExtraDimsForRole);
                PbReturnObject extraBussTabObj = execSelectSQL(fingetExtraBussTable);
                if (extraDimObj.getRowCount() > 0 || extraBussTabObj.getRowCount() > 0) {
                    roleColor = "!=";
                }
                //////////////////////////////////////////////////////////////////////////////////.println.println(roleName[i]+" roleColor "+roleColor);
                //susheela 11-12-09 over

                roleId[i] = String.valueOf(allTableObject.getFieldValueInt(i, tableNames[0]));
                roleName[i] = allTableObject.getFieldValueString(i, tableNames[1]);

                BusinessGrpsTreeTable table = new BusinessGrpsTreeTable();
                table.setRoleName(roleName[i]);
                table.setRoleId(roleId[i]);
                //susheela start 11-12-09
                table.setRoleColor(roleColor);
                //susheela 11-12-09 over
                list.add(table);
            }


        } 
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }       return list;
        }

    public ArrayList getBuckets(String grpId) {
        Connection con = null;
        ArrayList list = new ArrayList();
//        PbBussGrpResourceBundle resBundle = new PbBussGrpResourceBundle();
        try {
            String finalQuery = null;
            String sqlc = getResourceBundle().getString("getBucketConnection");
            Object objc[] = new Object[1];
            objc[0] = grpId;
            finalQuery = buildQuery(sqlc, objc);
            // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("bucket query--"+finalQuery);
            PbReturnObject allBussTablesIds = execSelectSQL(finalQuery);
            String[] bussTableNames = null;
            bussTableNames = allBussTablesIds.getColumnNames();
            //.println.println("final query in first bucket=="+finalQuery);
            for (int c = 0; c < allBussTablesIds.getRowCount(); c++) {
                if (c == 0) {


                    BusinessGroupDAO b = new BusinessGroupDAO();
                    con = b.getConnectionIdConnection(String.valueOf(allBussTablesIds.getFieldValueInt(c, bussTableNames[0])));
                    String sql = getResourceBundle().getString("getBucketList");
                    // .println.println("grpId==="+grpId);
                    Object obj[] = new Object[1];
                    obj[0] = grpId;
                    finalQuery = buildQuery(sql, obj);
                    Statement st = con.createStatement();
                    PbReturnObject allTableObject = new PbReturnObject(st.executeQuery(finalQuery));
                    //.println.println("------in if----->"+finalQuery);
                    String[] tableNames = null;
                    tableNames = allTableObject.getColumnNames();
                    String[] tabId = new String[allTableObject.getRowCount()];
                    String[] tabName = new String[allTableObject.getRowCount()];
                    //  String[] buckettabId=new String[allTableObject.getRowCount()];
                    // String[] bucketcolId=new String[allTableObject.getRowCount()];
                    for (int i = 0; i < allTableObject.getRowCount(); i++) {
                        tabId[i] = String.valueOf(allTableObject.getFieldValueInt(i, tableNames[0]));
                        tabName[i] = allTableObject.getFieldValueString(i, tableNames[1]);
                        //buckettabId[i] = String.valueOf(allTableObject.getFieldValueInt(i, tableNames[3]));
                        //  bucketcolId[i] = String.valueOf(allTableObject.getFieldValueInt(i, tableNames[4]));
                    }
                    String[] colNames = null;
                    String colName = "";
                    String tabName1 = "";
                    String tabId1 = "";
                    String sqlcolumns = getResourceBundle().getString("getBucketColList");
                    for (int i = 0; i < tabId.length; i++) {
                        tabName1 = tabName[i];
                        tabId1 = tabId[i];
                        Object obj1[] = new Object[1];
                        obj1[0] = tabId[i];
                        // obj1[1]=grpId;
                        //obj1[2]=bucketcolId[i];
                        //  obj1[3]=tabId1;


                        finalQuery = buildQuery(sqlcolumns, obj1);
                        PbReturnObject pbReturnObject = new PbReturnObject(st.executeQuery(finalQuery));
                        BusinessGrpsTreeTable table = new BusinessGrpsTreeTable();
                        table.setBucketTableName(tabName1.replaceAll("_", " "));
                        table.setBucketTableId(tabId1);
                        list.add(table);
                        colNames = pbReturnObject.getColumnNames();
                        String columnId = "";
                        String tabNamecol = "";
                        String startLim = "";
                        String endlim = "";
                        // String colType;
                        for (int j = 0; j < pbReturnObject.getRowCount(); j++) {
                            colName = pbReturnObject.getFieldValueString(j, colNames[1]);
                            columnId = String.valueOf(pbReturnObject.getFieldValueInt(j, colNames[0]) + "," + tabId1);
                            // colType=pbReturnObject.getFieldValueString(j, colNames[3]).toUpperCase();
                            //tabNamecol = pbReturnObject.getFieldValueString(j, colNames[3]);
                            startLim = pbReturnObject.getFieldValueString(j, colNames[4]);
                            endlim = pbReturnObject.getFieldValueString(j, colNames[5]);
                            //.println.println("table col id in dims-->" + tabNamecol);
                            BusinessGrpsTreeTable secTable = new BusinessGrpsTreeTable();
                            if (j + 1 == pbReturnObject.getRowCount()) {
                                secTable.setEndTable("true");

                            }
                            secTable.setBucketColName(colName);
                            secTable.setBucketColId(columnId);
                            secTable.setBucketStartLimit(startLim);
                            secTable.setBucketEndLimit(endlim);
                            // secTable.setBucketcolTableName(tabNamecol);
                            // secTable.setFactColType(colType);
                            list.add(secTable);
                        }

                    }
                } else {
                    if (!(allBussTablesIds.getFieldValueString(c - 1, bussTableNames[1]).equals(allBussTablesIds.getFieldValueString(c, bussTableNames[1])))) {
                        BusinessGroupDAO b = new BusinessGroupDAO();
                        con = b.getConnectionIdConnection(String.valueOf(allBussTablesIds.getFieldValueInt(c, bussTableNames[0])));
                        String sql = getResourceBundle().getString("getBucketList");
                        // .println.println("grpId==="+grpId);
                        Object obj[] = new Object[1];
                        obj[0] = grpId;
                        finalQuery = buildQuery(sql, obj);
                        Statement st = con.createStatement();
                        PbReturnObject allTableObject = new PbReturnObject(st.executeQuery(finalQuery));
                        //.println.println("-------in else---->"+finalQuery);
                        String[] tableNames = null;
                        tableNames = allTableObject.getColumnNames();
                        String[] tabId = new String[allTableObject.getRowCount()];
                        String[] tabName = new String[allTableObject.getRowCount()];
                        //  String[] buckettabId=new String[allTableObject.getRowCount()];
                        // String[] bucketcolId=new String[allTableObject.getRowCount()];
                        for (int i = 0; i < allTableObject.getRowCount(); i++) {
                            tabId[i] = String.valueOf(allTableObject.getFieldValueInt(i, tableNames[0]));
                            tabName[i] = allTableObject.getFieldValueString(i, tableNames[1]);
                            //buckettabId[i] = String.valueOf(allTableObject.getFieldValueInt(i, tableNames[3]));
                            //  bucketcolId[i] = String.valueOf(allTableObject.getFieldValueInt(i, tableNames[4]));
                        }
                        String[] colNames = null;
                        String colName = "";
                        String tabName1 = "";
                        String tabId1 = "";
                        String sqlcolumns = getResourceBundle().getString("getBucketColList");
                        for (int i = 0; i < tabId.length; i++) {
                            tabName1 = tabName[i];
                            tabId1 = tabId[i];
                            Object obj1[] = new Object[1];
                            obj1[0] = tabId[i];
                            // obj1[1]=grpId;
                            //obj1[2]=bucketcolId[i];
                            //  obj1[3]=tabId1;


                            finalQuery = buildQuery(sqlcolumns, obj1);
                            // .println.println("final bucket query---->"+finalQuery);
                            PbReturnObject pbReturnObject = new PbReturnObject(st.executeQuery(finalQuery));
                            BusinessGrpsTreeTable table = new BusinessGrpsTreeTable();
                            table.setBucketTableName(tabName1.replaceAll("_", " "));
                            table.setBucketTableId(tabId1);
                            list.add(table);
                            colNames = pbReturnObject.getColumnNames();
                            String columnId = "";
                            String tabNamecol = "";
                            String startLim = "";
                            String endlim = "";
                            // String colType;
                            for (int j = 0; j < pbReturnObject.getRowCount(); j++) {
                                colName = pbReturnObject.getFieldValueString(j, colNames[1]);
                                columnId = String.valueOf(pbReturnObject.getFieldValueInt(j, colNames[0]) + "," + tabId1);
                                // colType=pbReturnObject.getFieldValueString(j, colNames[3]).toUpperCase();
                                //tabNamecol = pbReturnObject.getFieldValueString(j, colNames[3]);
                                startLim = pbReturnObject.getFieldValueString(j, colNames[4]);
                                endlim = pbReturnObject.getFieldValueString(j, colNames[5]);
                                // .println.println("table col id in dims-->" + tabNamecol);
                                BusinessGrpsTreeTable secTable = new BusinessGrpsTreeTable();
                                if (j + 1 == pbReturnObject.getRowCount()) {
                                    secTable.setEndTable("true");

                                }
                                secTable.setBucketColName(colName);
                                secTable.setBucketColId(columnId);
                                secTable.setBucketStartLimit(startLim);
                                secTable.setBucketEndLimit(endlim);
                                // secTable.setBucketcolTableName(tabNamecol);
                                // secTable.setFactColType(colType);
                                list.add(secTable);
                            }

                        }
                    }

                }
                con.close();
            }

        } 
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        }catch (Exception e) {
            logger.error("Exception:", e);
        }        ////.println.println("size of fzcts--->"+list.size());
        return list;
    }

    /*
     * public static ArrayList getBusinessGroups() { ArrayList finalList = new
     * ArrayList(); PbBussGrpResourceBundle resBundle = new
     * PbBussGrpResourceBundle(); try { //.println.println("before con");
     * PbReturnObject retObj = null; ProgenConnection prgMetadataCon = null;
     * String[] colNames = null; //.println.println("aft con" + prgMetadataCon);
     * ResultSet rs = null; Connection connection = null; String parentName =
     * ""; prgMetadataCon = new ProgenConnection(); connection =
     * prgMetadataCon.getConnection(); Statement allGrps =
     * connection.createStatement(); String sql =
     * resBundle.getString("getBusinessGroupList"); //.println.println("business
     * grp list====>" + sql); rs = allGrps.executeQuery(sql); retObj = new
     * PbReturnObject(rs); colNames = retObj.getColumnNames(); for (int i = 0; i
     * < retObj.getRowCount(); i++) { Businessgrps businessgrps = new
     * Businessgrps();
     * businessgrps.setBusinessGrpName(retObj.getFieldValueString(i,
     * colNames[1]));
     * businessgrps.setBusinessGrpId(String.valueOf(retObj.getFieldValueInt(i,
     * colNames[0]))); ArrayList dimensionList = new ArrayList(); ArrayList
     * allTablesList = new ArrayList(); ArrayList factsList = new ArrayList();
     * ArrayList bucketsList = new ArrayList(); dimensionList =
     * getDimensions(String.valueOf(retObj.getFieldValueInt(i, colNames[0])));
     * allTablesList = getAllTables(String.valueOf(retObj.getFieldValueInt(i,
     * colNames[0]))); factsList =
     * getFacts(String.valueOf(retObj.getFieldValueInt(i, colNames[0])));
     * bucketsList = getBuckets(String.valueOf(retObj.getFieldValueInt(i,
     * colNames[0]))); businessgrps.setDimensionList(dimensionList);
     * businessgrps.setAllTablesList(allTablesList);
     * businessgrps.setFactsList(factsList);
     * businessgrps.setBucketsList(bucketsList); finalList.add(businessgrps);
     *
     * }
     * //.println.println("final list--->"+finalList); rs.close();
     * allGrps.close(); connection.close();
     *
     * } catch (Exception e) { logger.error("Exception:",e); }
     *
     * return finalList; }
     *
     * //added by susheela start
     *
     * public ArrayList getAddedTargetMeasures(String busGroupId) throws
     * Exception{ String finalStr = ""; String getSavedTargetMeasure =
     * resBundle.getString("getSavedTargetMeasure");//getSavedMeasure String
     * getSavedMeasure = resBundle.getString("getSavedMeasure");
     *
     * ArrayList tableName = new ArrayList(); Object TabObj[] = new Object[1];
     * TabObj[0] = busGroupId; String fingetSavedTargetMeasure =
     * buildQuery(getSavedMeasure,TabObj); String tabQ = "select
     * col_id,col_name,pgbt.buss_table_name
     * original_table,buss_original_table_id from target_master_col_details
     * tmcd,prg_target_master ptm,prg_grp_buss_table pgbt where
     * tmcd.buss_table_id=ptm.target_table_id and tmcd.selected_measure='Y' and
     * buss_original_table_id = pgbt.buss_table_id and
     * ptm.business_group="+busGroupId;
     * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("
     * fingetSavedTargetMeasure tabQ "+tabQ); // PbReturnObject factRetOb=
     * execSelectSQL(fingetSavedTargetMeasure); PbReturnObject factRetOb=
     * execSelectSQL(tabQ);
     *
     * for(int o=0;o<factRetOb.getRowCount();o++) { BusinessGrpsTreeTable table
     * = new BusinessGrpsTreeTable();
     * table.setTargetMeasureId(factRetOb.getFieldValueString(o,"COL_ID"));
     * table.setTargetMeasureName(factRetOb.getFieldValueString(o,"COL_NAME"));
     * // table.setMColName(factRetOb.getFieldValueString(o,"COL_NAME"));
     * BusinessGrpsTreeTable sectable = new BusinessGrpsTreeTable();
     * BusinessGrpsTreeTable terTable = new BusinessGrpsTreeTable(); for(int
     * h=0;h<1;h++){
     * sectable.setTargetFactTableId(factRetOb.getFieldValueString(o,"BUSS_ORIGINAL_TABLE_ID"));
     * sectable.setTargetFactTableName(factRetOb.getFieldValueString(o,"ORIGINAL_TABLE"));
     *
     * if(h==0) { sectable.setEndTable("true"); } String getMeasureMembersQ =
     * "select * from prg_target_measure_members where measure_id in(select
     * prg_measure_id from prg_target_master where
     * measure_name='"+factRetOb.getFieldValueString(o,"COL_NAME")+"' and
     * business_group="+busGroupId+")";
     * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("
     * getMeasureMemebersQ "+getMeasureMembersQ); PbReturnObject MeasureMemOb=
     * execSelectSQL(getMeasureMembersQ); Businessgrps businessgrps = new
     * Businessgrps(); ArrayList measureMemList = new ArrayList();
     * tableName.add(table); tableName.add(sectable);
     *
     *
     * }
     *
     * }
     * return tableName; }
     *
     *
     * //added by susheela over
     *
     *
     * public static ArrayList getDimensions(String grpId) { ArrayList finalList
     * = new ArrayList(); PbBussGrpResourceBundle resBundle = new
     * PbBussGrpResourceBundle(); try { Connection con = null; ResultSet rs =
     * null; PbReturnObject retObj = null; String finalQuery = null; String[]
     * colNames = null; PreparedStatement allDimensions = null; con = new
     * utils.db.ProgenConnection().getConnection(); Object obj[] = new
     * Object[1]; obj[0] = grpId; String sql =
     * resBundle.getString("getBusinessGroupDimList"); finalQuery = new
     * prg.db.PbDb().buildQuery(sql, obj); //.println.println("finalQuery is " +
     * finalQuery); allDimensions = con.prepareStatement(finalQuery); rs =
     * allDimensions.executeQuery(); retObj = new PbReturnObject(rs); colNames =
     * retObj.getColumnNames(); for (int i = 0; i < retObj.getRowCount(); i++) {
     * BusinessGrpsTreeTable dimension = new BusinessGrpsTreeTable();
     * dimension.setDimensionName(retObj.getFieldValueString(i, colNames[1]));
     * dimension.setDimensionId(String.valueOf(retObj.getFieldValueInt(i,
     * colNames[0]))); //.println.println("DIMENSION NAME==" +
     * retObj.getFieldValueString(i, colNames[1])); ArrayList tableList = new
     * ArrayList();
     *
     * tableList = getTableList(String.valueOf(retObj.getFieldValueInt(i,
     * colNames[0])), grpId); dimension.setDimTableList(tableList); ArrayList
     * membersList = new ArrayList(); membersList =
     * getMembersList(String.valueOf(retObj.getFieldValueInt(i, colNames[0])),
     * grpId); dimension.setDimMembersList(membersList); ArrayList hierarchyList
     * = new ArrayList(); hierarchyList =
     * getHeirarchyList(String.valueOf(retObj.getFieldValueInt(i, colNames[0])),
     * grpId); dimension.setDimHierarchyList(hierarchyList);
     * //.println.println("hierarchy list==" + hierarchyList.size());
     * finalList.add(dimension); } allDimensions.close(); con.close(); } catch
     * (Exception e) { logger.error("Exception:",e); }
     *
     * return finalList; } public static ArrayList getTableList(String dimId,
     * String grpId) { Connection con = null; ArrayList list = new ArrayList();
     * PbBussGrpResourceBundle resBundle = new PbBussGrpResourceBundle(); try {
     * ResultSet rs = null; PbReturnObject retObj = null; String finalQuery =
     * null;
     *
     * PreparedStatement allTables = null; String sql =
     * resBundle.getString("getBusinessGroupDimTableList"); con = new
     * utils.db.ProgenConnection().getConnection();
     *
     * Object obj[] = new Object[1]; obj[0] = dimId; finalQuery = new
     * prg.db.PbDb().buildQuery(sql, obj); //.println.println("finalQuery is " +
     * finalQuery); allTables = con.prepareStatement(finalQuery); rs =
     * allTables.executeQuery(); PbReturnObject allTableObject = new
     * PbReturnObject(rs); String[] tableNames = null; tableNames =
     * allTableObject.getColumnNames(); String[] tabId = new
     * String[allTableObject.getRowCount()]; String[] tabName = new
     * String[allTableObject.getRowCount()]; String[] originaltabId = new
     * String[allTableObject.getRowCount()]; for (int i = 0; i <
     * allTableObject.getRowCount(); i++) { originaltabId[i] =
     * String.valueOf(allTableObject.getFieldValueInt(i, tableNames[0]));
     * tabId[i] = String.valueOf(allTableObject.getFieldValueInt(i,
     * tableNames[3])); tabName[i] = allTableObject.getFieldValueString(i,
     * tableNames[1]); //.println.println("table name==" + tabName[i] + " table
     * id===" + tabId[i]); } String[] colNames = null; String colName = "";
     * String tabName1 = ""; String tabId1 = ""; Statement stmt =
     * con.createStatement(); ResultSet columnsList = null; String sqlcolumns =
     * resBundle.getString("getBusinessGroupDimTableColList"); for (int i = 0; i
     * < tabId.length; i++) { tabName1 = tabName[i]; tabId1 = tabId[i]; int
     * rowCountFlag = 0; Object obj1[] = new Object[1]; obj1[0] =
     * originaltabId[i]; finalQuery = new prg.db.PbDb().buildQuery(sqlcolumns,
     * obj1); //.println.println("finalQuery col table list is " + finalQuery);
     * allTables = con.prepareStatement(finalQuery); rs =
     * allTables.executeQuery(); PbReturnObject pbReturnObject = new
     * PbReturnObject(rs); BusinessGrpsTreeTable table = new
     * BusinessGrpsTreeTable(); table.setDimTableName(tabName1);
     * table.setDimTableId(tabId1 + "," + originaltabId[i] + "," + grpId);
     * list.add(table); colNames = pbReturnObject.getColumnNames(); String
     * columnId = ""; String is_pk = ""; String is_available = ""; String
     * colType = ""; for (int j = 0; j < pbReturnObject.getRowCount(); j++) {
     * colName = pbReturnObject.getFieldValueString(j, colNames[1]); columnId =
     * String.valueOf(pbReturnObject.getFieldValueInt(j, colNames[0])) + "," +
     * originaltabId[i] + "," + tabId1 + "," + dimId; is_pk =
     * pbReturnObject.getFieldValueString(j, colNames[2]); is_available =
     * pbReturnObject.getFieldValueString(j, colNames[3]); colType =
     * pbReturnObject.getFieldValueString(j, colNames[5]).toUpperCase();
     * //.println.println("table col id in dims-->" + columnId+"col
     * type--->"+colType); BusinessGrpsTreeTable secTable = new
     * BusinessGrpsTreeTable(); if (j + 1 == pbReturnObject.getRowCount()) {
     * secTable.setEndTable("true"); // //.println.println(""+j); }
     *
     * secTable.setDimColumnName(colName); secTable.setDimColumnId(columnId);
     * secTable.setIsPk(is_pk); secTable.setIsAvailable(is_available);
     * secTable.setColType(colType); list.add(secTable); }
     *
     *
     * }
     *
     * rs.close(); allTables.close(); con.close(); } catch (Exception e) {
     * logger.error("Exception:",e); } return list; } public static ArrayList
     * getMembersList(String dimId, String grpId) { Connection con = null;
     * ArrayList list = new ArrayList(); PbBussGrpResourceBundle resBundle = new
     * PbBussGrpResourceBundle(); try {
     *
     *
     * ResultSet rs = null; PbReturnObject retObj = null; String finalQuery =
     * null; PreparedStatement allMems = null; Object obj1[] = new Object[1];
     * obj1[0] = dimId; con = new utils.db.ProgenConnection().getConnection();
     *
     * String sql = resBundle.getString("getBusinessGroupDimMemList");
     * finalQuery = new prg.db.PbDb().buildQuery(sql, obj1);
     * //.println.println("mem finalQuery col is " + finalQuery); //allMems =
     * con.prepareStatement(finalQuery); Statement st = con.createStatement();
     * //.println.println("alll mems"+allMems); rs =
     * st.executeQuery(finalQuery); //.println.println("rs--->"+rs);
     * PbReturnObject allMembersObject = new PbReturnObject(rs); String[]
     * MemberNames = null; MemberNames = allMembersObject.getColumnNames();
     * String[] memId = new String[allMembersObject.getRowCount()]; String[]
     * memName = new String[allMembersObject.getRowCount()]; for (int i = 0; i <
     * allMembersObject.getRowCount(); i++) {
     *
     * memId[i] = String.valueOf(allMembersObject.getFieldValueInt(i,
     * MemberNames[0])); memName[i] = allMembersObject.getFieldValueString(i,
     * MemberNames[1]); //.println.println("member name==" + memName[i] + "
     * member id===" + memId[i]); }
     *
     * String memName1 = ""; String memId1 = "";
     *
     * for (int i = 0; i < memId.length; i++) { memName1 = memName[i]; memId1 =
     * memId[i]; BusinessGrpsTreeTable table = new BusinessGrpsTreeTable();
     * table.setDimMemberName(memName1); table.setDimMemberId(memId1);
     * list.add(table); } rs.close(); st.close(); con.close(); } catch
     * (Exception e) { logger.error("Exception:",e); } return list; } public
     * static ArrayList getHeirarchyList(String dimId, String grpId) {
     * Connection con = null; ArrayList list = new ArrayList();
     * PbBussGrpResourceBundle resBundle = new PbBussGrpResourceBundle(); try {
     * ResultSet rs = null; PbReturnObject retObj = null; String finalQuery =
     * null;
     *
     * PreparedStatement allHiess = null; String sql =
     * resBundle.getString("getBusinessGroupDimHieList"); con = new
     * utils.db.ProgenConnection().getConnection();
     *
     * Object obj[] = new Object[1]; obj[0] = dimId; finalQuery = new
     * prg.db.PbDb().buildQuery(sql, obj); //.println.println(" rel finalQuery
     * is " + finalQuery); allHiess = con.prepareStatement(finalQuery); rs =
     * allHiess.executeQuery(); PbReturnObject allHierachyObject = new
     * PbReturnObject(rs); String[] tableNames = null; tableNames =
     * allHierachyObject.getColumnNames(); String[] relId = new
     * String[allHierachyObject.getRowCount()]; String[] relName = new
     * String[allHierachyObject.getRowCount()]; for (int i = 0; i <
     * allHierachyObject.getRowCount(); i++) {
     *
     * relId[i] = String.valueOf(allHierachyObject.getFieldValueInt(i,
     * tableNames[0])); relName[i] = allHierachyObject.getFieldValueString(i,
     * tableNames[1]); //.println.println("REL name==" + relName[i] + " REL
     * id===" + relId[i]); } String[] colNames = null; String colName = "";
     * String relName1 = ""; String relId1 = ""; Statement stmt =
     * con.createStatement(); ResultSet columnsList = null; String sqlcolumns =
     * resBundle.getString("getBusinessGroupDimHieColList"); for (int i = 0; i <
     * relId.length; i++) { relName1 = relName[i]; relId1 = relId[i]; int
     * rowCountFlag = 0;
     *
     * Object obj1[] = new Object[1]; obj1[0] = relId[i]; finalQuery = new
     * prg.db.PbDb().buildQuery(sqlcolumns, obj1); //.println.println(" rel
     * finalQuery col is " + finalQuery); allHiess =
     * con.prepareStatement(finalQuery); rs = allHiess.executeQuery();
     * PbReturnObject pbReturnObject = new PbReturnObject(rs);
     *
     * BusinessGrpsTreeTable table = new BusinessGrpsTreeTable();
     * table.setDimRelationName(relName1); table.setDimRelationId(relId1); if
     * (pbReturnObject.getRowCount() == 0) { table.setEndTable("true"); }
     * list.add(table); colNames = pbReturnObject.getColumnNames(); String
     * columnId = ""; for (int j = 0; j < pbReturnObject.getRowCount(); j++) {
     * colName = pbReturnObject.getFieldValueString(j, colNames[1]); columnId =
     * String.valueOf(pbReturnObject.getFieldValueInt(j, colNames[0])) + "," +
     * relId; //.println.println("rel col namee-->" + colName);
     * BusinessGrpsTreeTable secTable = new BusinessGrpsTreeTable(); if (j + 1
     * == pbReturnObject.getRowCount()) { secTable.setEndTable("true"); //
     * //.println.println(""+j); } secTable.setDimRelColumnName(colName);
     * secTable.setDimRelColumnId(columnId); list.add(secTable); }
     *
     *
     * }
     * } catch (Exception e) { logger.error("Exception:",e); }
     * //.println.println("size of hierachies--->"+list.size()); return list; }
     * public static ArrayList getAllTables(String grpId) { Connection con =
     * null; ArrayList list = new ArrayList(); PbBussGrpResourceBundle resBundle
     * = new PbBussGrpResourceBundle(); try { ResultSet rs = null;
     * PbReturnObject retObj = null; String finalQuery = null;
     *
     * PreparedStatement allTables = null; String sql =
     * resBundle.getString("getBusinessGroupAllTablesList"); con = new
     * utils.db.ProgenConnection().getConnection();
     *
     * Object obj[] = new Object[1]; obj[0] = grpId; finalQuery = new
     * prg.db.PbDb().buildQuery(sql, obj); //.println.println("finalQuery is all
     * tables is------>" + finalQuery); allTables =
     * con.prepareStatement(finalQuery); rs = allTables.executeQuery();
     * PbReturnObject allTableObject = new PbReturnObject(rs); String[]
     * tableNames = null; tableNames = allTableObject.getColumnNames(); String[]
     * tabId = new String[allTableObject.getRowCount()]; String[] tabName = new
     * String[allTableObject.getRowCount()]; String[] dbtabId = new
     * String[allTableObject.getRowCount()]; for (int i = 0; i <
     * allTableObject.getRowCount(); i++) { dbtabId[i] =
     * String.valueOf(allTableObject.getFieldValueInt(i, tableNames[2]));
     * tabId[i] = String.valueOf(allTableObject.getFieldValueInt(i,
     * tableNames[0])); tabName[i] = allTableObject.getFieldValueString(i,
     * tableNames[1]); //.println.println("table name==" + tabName[i] + " table
     * id===" + tabId[i]); } String[] colNames = null; String colName = "";
     * String tabName1 = ""; String tabId1 = ""; Statement stmt =
     * con.createStatement(); ResultSet columnsList = null; String sqlcolumns =
     * resBundle.getString("getBusinessGroupAllTablesColList"); for (int i = 0;
     * i < tabId.length; i++) { tabName1 = tabName[i]; tabId1 = tabId[i]; int
     * rowCountFlag = 0; Object obj1[] = new Object[1]; obj1[0] = tabId[i];
     * finalQuery = new prg.db.PbDb().buildQuery(sqlcolumns, obj1);
     * //.println.println(" all tabs finalQuery col is----------> " +
     * finalQuery); allTables = con.prepareStatement(finalQuery); rs =
     * allTables.executeQuery(); PbReturnObject pbReturnObject = new
     * PbReturnObject(rs); BusinessGrpsTreeTable table = new
     * BusinessGrpsTreeTable(); table.setAllTableName(tabName1);
     * table.setAllTableId(tabId1 + "," + dbtabId[i] + "," + grpId);
     * list.add(table); colNames = pbReturnObject.getColumnNames(); String
     * columnId = ""; String colType = ""; for (int j = 0; j <
     * pbReturnObject.getRowCount(); j++) { colName =
     * pbReturnObject.getFieldValueString(j, colNames[1]); columnId =
     * String.valueOf(pbReturnObject.getFieldValueInt(j, colNames[0])) + "," +
     * String.valueOf(pbReturnObject.getFieldValueInt(j, colNames[2])); colType
     * = pbReturnObject.getFieldValueString(j, colNames[3]).toUpperCase();
     * //.println.println("table col id in dims-->" + columnId);
     * BusinessGrpsTreeTable secTable = new BusinessGrpsTreeTable(); if (j + 1
     * == pbReturnObject.getRowCount()) { secTable.setEndTable("true");
     *
     * }
     *
     * secTable.setAllTableColName(colName);
     * secTable.setAllTableColId(columnId);
     * secTable.setAllTableColType(colType); list.add(secTable); }
     *
     *
     * }
     *
     * rs.close(); allTables.close(); con.close(); } catch (Exception e) {
     * logger.error("Exception:",e); } //.println.println("size of all
     * tabls---->"+list.size()); return list; } public static ArrayList
     * getFacts(String grpId) { Connection con = null; ArrayList list = new
     * ArrayList(); PbBussGrpResourceBundle resBundle = new
     * PbBussGrpResourceBundle(); try { ResultSet rs = null; PbReturnObject
     * retObj = null; String finalQuery = null;
     *
     * PreparedStatement allTables = null; String sql =
     * resBundle.getString("getBusinessGroupFactsList"); con = new
     * utils.db.ProgenConnection().getConnection();
     *
     * Object obj[] = new Object[2]; obj[0] = grpId; obj[1] = grpId; finalQuery
     * = new prg.db.PbDb().buildQuery(sql, obj); //.println.println("finalQuery
     * is " + finalQuery); allTables = con.prepareStatement(finalQuery); rs =
     * allTables.executeQuery(); PbReturnObject allTableObject = new
     * PbReturnObject(rs); String[] tableNames = null; tableNames =
     * allTableObject.getColumnNames(); String[] tabId = new
     * String[allTableObject.getRowCount()]; String[] tabName = new
     * String[allTableObject.getRowCount()]; String[] dbtabId = new
     * String[allTableObject.getRowCount()]; for (int i = 0; i <
     * allTableObject.getRowCount(); i++) { dbtabId[i] =
     * String.valueOf(allTableObject.getFieldValueInt(i, tableNames[2]));
     * tabId[i] = String.valueOf(allTableObject.getFieldValueInt(i,
     * tableNames[0])); tabName[i] = allTableObject.getFieldValueString(i,
     * tableNames[1]); //.println.println("table name==" + tabName[i] + " table
     * id===" + tabId[i]); } String[] colNames = null; String colName = "";
     * String tabName1 = ""; String tabId1 = ""; Statement stmt =
     * con.createStatement(); ResultSet columnsList = null; String sqlcolumns =
     * resBundle.getString("getBusinessGroupFactsColList"); for (int i = 0; i <
     * tabId.length; i++) { tabName1 = tabName[i]; tabId1 = tabId[i]; int
     * rowCountFlag = 0; Object obj1[] = new Object[1]; obj1[0] = tabId[i];
     *
     * finalQuery = new prg.db.PbDb().buildQuery(sqlcolumns, obj1);
     * //.println.println(" all tabs finalQuery col is " + finalQuery);
     * allTables = con.prepareStatement(finalQuery); rs =
     * allTables.executeQuery(); PbReturnObject pbReturnObject = new
     * PbReturnObject(rs); BusinessGrpsTreeTable table = new
     * BusinessGrpsTreeTable(); table.setFactTableName(tabName1);
     * table.setFactTableId(tabId1 + "," + dbtabId[i] + "," + grpId);
     * list.add(table); colNames = pbReturnObject.getColumnNames(); String
     * columnId = ""; String colType; for (int j = 0; j <
     * pbReturnObject.getRowCount(); j++) { colName =
     * pbReturnObject.getFieldValueString(j, colNames[1]); columnId =
     * String.valueOf(pbReturnObject.getFieldValueInt(j, colNames[0])) + "," +
     * String.valueOf(pbReturnObject.getFieldValueInt(j, colNames[2])); colType
     * = pbReturnObject.getFieldValueString(j, colNames[3]).toUpperCase();
     * //.println.println("table col id in dims-->" + columnId);
     * BusinessGrpsTreeTable secTable = new BusinessGrpsTreeTable(); if (j + 1
     * == pbReturnObject.getRowCount()) { secTable.setEndTable("true");
     *
     * }
     *
     * secTable.setFactTableColName(colName);
     * secTable.setFactTableColId(columnId); secTable.setFactColType(colType);
     * list.add(secTable); }
     *
     *
     * }
     *
     * rs.close(); allTables.close(); con.close(); } catch (Exception e) {
     * logger.error("Exception:",e); } //.println.println("size of
     * fzcts--->"+list.size()); return list; } public static ArrayList
     * getBuckets(String grpId) { Connection con = null; ArrayList list = new
     * ArrayList(); PbBussGrpResourceBundle resBundle = new
     * PbBussGrpResourceBundle(); try { public ArrayList getBuckets(String
     * grpId) { ResultSet rs = null; PbReturnObject retObj = null; String
     * finalQuery = null;
     *
     * PreparedStatement allTables = null; String sql =
     * resBundle.getString("getBucketList"); con = new
     * utils.db.ProgenConnection().getConnection();
     *
     * Object obj[] = new Object[2]; obj[0] = grpId; obj[1] = grpId;
     * //finalQuery = new prg.db.PbDb().buildQuery(sql, obj); finalQuery = sql;
     * //.println.println("finalQuery is " + finalQuery); allTables =
     * con.prepareStatement(finalQuery); rs = allTables.executeQuery();
     * PbReturnObject allTableObject = new PbReturnObject(rs); String[]
     * tableNames = null; tableNames = allTableObject.getColumnNames(); String[]
     * tabId = new String[allTableObject.getRowCount()]; String[] tabName = new
     * String[allTableObject.getRowCount()]; //String[] dbtabId = new
     * String[allTableObject.getRowCount()]; for (int i = 0; i <
     * allTableObject.getRowCount(); i++) { // dbtabId[i] =
     * String.valueOf(allTableObject.getFieldValueInt(i, tableNames[2]));
     * tabId[i] = String.valueOf(allTableObject.getFieldValueInt(i,
     * tableNames[0])); tabName[i] = allTableObject.getFieldValueString(i,
     * tableNames[1]); //.println.println("table name==" + tabName[i] + " table
     * id===" + tabId[i]); } String[] colNames = null; String colName = "";
     * String tabName1 = ""; String tabId1 = ""; Statement stmt =
     * con.createStatement(); ResultSet columnsList = null; String sqlcolumns =
     * resBundle.getString("getBucketColList"); for (int i = 0; i <
     * tabId.length; i++) { tabName1 = tabName[i]; tabId1 = tabId[i]; int
     * rowCountFlag = 0; Object obj1[] = new Object[1]; obj1[0] = tabId[i];
     *
     * finalQuery = new prg.db.PbDb().buildQuery(sqlcolumns, obj1);
     * //.println.println(" all tabs finalQuery col is " + finalQuery);
     * allTables = con.prepareStatement(finalQuery); rs =
     * allTables.executeQuery(); PbReturnObject pbReturnObject = new
     * PbReturnObject(rs); BusinessGrpsTreeTable table = new
     * BusinessGrpsTreeTable(); table.setBucketTableName(tabName1);
     * table.setBucketTableId(tabId1); list.add(table); colNames =
     * pbReturnObject.getColumnNames(); String columnId = ""; // String colType;
     * for (int j = 0; j < pbReturnObject.getRowCount(); j++) { colName =
     * pbReturnObject.getFieldValueString(j, colNames[1]); columnId =
     * String.valueOf(pbReturnObject.getFieldValueInt(j, colNames[0]) + "," +
     * tabId1); // colType=pbReturnObject.getFieldValueString(j,
     * colNames[3]).toUpperCase(); //.println.println("table col id in dims-->"
     * + columnId); BusinessGrpsTreeTable secTable = new
     * BusinessGrpsTreeTable(); if (j + 1 == pbReturnObject.getRowCount()) {
     * secTable.setEndTable("true");
     *
     * }
     *
     * secTable.setBucketColName(colName); secTable.setBucketColId(columnId); //
     * secTable.setFactColType(colType); list.add(secTable); }
     *
     *
     * }
     *
     * rs.close(); allTables.close(); con.close(); } catch (Exception e) {
     * logger.error("Exception:",e); } //.println.println("size of
     * fzcts--->"+list.size()); return list; }
     */
    public String getAllColumns(String columnid, String columntype, String defaultaggregation, String columndisplaydesc) {
        PbReturnObject returnObject = null;
        String finalQuery1 = null;
        try {
            String columns = getResourceBundle().getString("getallDetails");
            PbDb pbDb = new PbDb();
            Object ojb[] = new Object[1];
            ojb[0] = columnid;
            String finalQuery = buildQuery(columns, ojb);
            returnObject = pbDb.execSelectSQL(finalQuery);
            String buss_column_id = null;
            String buss_table_id = null;
            String db_table_id = null;
            String db_column_id = null;
            String buss_table_name = null;
            String columnname = null;
            for (int i = 0; i < returnObject.getRowCount(); i++) {
                buss_column_id = returnObject.getFieldValueString(i, "BUSS_COLUMN_ID");
                buss_table_id = returnObject.getFieldValueString(i, "BUSS_TABLE_ID");
                columnname = returnObject.getFieldValueString(i, "COLUMN_NAME");
                db_table_id = returnObject.getFieldValueString(i, "DB_TABLE_ID");
                db_column_id = returnObject.getFieldValueString(i, "DB_COLUMN_ID");
                buss_table_name = returnObject.getFieldValueString(i, "BUSS_TABLE_NAME");
            }
            String actualcolumnFormula = buss_table_name.concat("." + columnname);
            String insert;
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                insert = getResourceBundle().getString("insertQuickFormulaMysql");
//              String insert1=getResourceBundle().getString("getSeq");
//              returnObject= pbDb.execSelectSQL(insert1);
//              String seqno=returnObject.getFieldValueString(0, 0);
//              String columndispname="B_".concat(seqno) ;
                returnObject = pbDb.execSelectSQL("select last_insert_id(BUSS_COLUMN_ID) from PRG_GRP_BUSS_TABLE_DETAILS order by 1 desc limit 1");
                int bussColId = returnObject.getFieldValueInt(0, 0);
                String rollflag = "Y";
                Object obj1[] = new Object[11];
//              obj1[0]=seqno;
                obj1[0] = buss_table_id;
                obj1[1] = columnname;
                obj1[2] = db_table_id;
                obj1[3] = db_column_id;
                obj1[4] = columntype;
                obj1[5] = actualcolumnFormula;
                obj1[6] = defaultaggregation;
//              obj1[8]=columndispname;
                obj1[7] = bussColId;
                obj1[8] = columndisplaydesc;
                obj1[9] = rollflag;
                obj1[10] = columnid;
                finalQuery1 = buildQuery(insert, obj1);
            } else {
                insert = getResourceBundle().getString("insertQuickFormula");
                //              String insert1=getResourceBundle().getString("getSeq");
//              returnObject= pbDb.execSelectSQL(insert1);
//              String seqno=returnObject.getFieldValueString(0, 0);
//              String columndispname="B_".concat(seqno) ;
                String rollflag = "Y";
                Object obj1[] = new Object[10];
//              obj1[0]=seqno;
                obj1[0] = buss_table_id;
                obj1[1] = columnname;
                obj1[2] = db_table_id;
                obj1[3] = db_column_id;
                obj1[4] = columntype;
                obj1[5] = actualcolumnFormula;
                obj1[6] = defaultaggregation;
//              obj1[8]=columndispname;
                obj1[7] = columndisplaydesc;
                obj1[8] = rollflag;
                obj1[9] = columnid;
                finalQuery1 = buildQuery(insert, obj1);
            }

            int j = pbDb.execUpdateSQL(finalQuery1);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public String getAddQuickForumla(int tabId) {
        String jsonString = null;
        PbReturnObject returnObject = null;
        try {
            String s2 = getResourceBundle().getString("getaddQuickQuerry2");
            returnObject = new PbReturnObject();
            PbDb pbDb = new PbDb();
            Object ojb[] = new Object[1];
            ojb[0] = tabId;
            String finalQuery = buildQuery(s2, ojb);
            returnObject = pbDb.execSelectSQL(finalQuery);
            LinkedList<String> displayNames = new LinkedList<String>();
            LinkedList<String> displayNameIds = new LinkedList<String>();
            LinkedList<String> defaultAggregation = new LinkedList<String>();
            LinkedList<String> roleflag = new LinkedList<String>();
            for (int i = 0; i < returnObject.getRowCount(); i++) {
                displayNames.add(returnObject.getFieldValueString(i, 14));
                displayNameIds.add(returnObject.getFieldValueString(i, 0));
                defaultAggregation.add(returnObject.getFieldValueString(i, 1));
                roleflag.add(returnObject.getFieldValueString(i, 15));
            }
            HashMap DisplayNames = new HashMap();
            DisplayNames.put("columndisplayname", displayNames);
            DisplayNames.put("id", displayNameIds);
            DisplayNames.put("defaultAggregationvalues", defaultAggregation);
            DisplayNames.put("roleflag", roleflag);
            Gson gson = new Gson();
            jsonString = gson.toJson(DisplayNames);
        } 
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        }catch (Exception e) {
            logger.error("Exception:", e);
        }        return jsonString;
        }

    public String getAllFields(int tabId) {
        String jsonString = null;
        PbReturnObject returnObject = null;
        try {
            String Query = getResourceBundle().getString("getDetails");
            returnObject = new PbReturnObject();
            PbDb pbDb = new PbDb();
            Object ojb[] = new Object[1];
            ojb[0] = tabId;

            String finalQuery = buildQuery(Query, ojb);
            returnObject = pbDb.execSelectSQL(finalQuery);
//            ArrayList<String> displayNameIds=new ArrayList<String>();

            LinkedHashMap defaultaggr = new LinkedHashMap();
            for (int i = 0; i < returnObject.getRowCount(); i++) {
                ArrayList<String> agr = new ArrayList<String>();
                agr.add(returnObject.getFieldValueString(i, 14));

                if ((returnObject.getFieldValueString(i, 5)).equals("Y")) {
                    agr.add("sum");
                } else {
                    agr.add("");
                }
                if ((returnObject.getFieldValueString(i, 2)).equals("Y")) {
                    agr.add("avg");
                } else {
                    agr.add("");
                }
                if ((returnObject.getFieldValueString(i, 3)).equals("Y")) {
                    agr.add("min");
                } else {
                    agr.add("");
                }
                if ((returnObject.getFieldValueString(i, 4)).equals("Y")) {
                    agr.add("max");
                } else {
                    agr.add("");
                }
                if ((returnObject.getFieldValueString(i, 6)).equals("Y")) {
                    agr.add("count");
                } else {
                    agr.add("");
                }
                if ((returnObject.getFieldValueString(i, 7)).equals("Y")) {
                    agr.add("COUNTDISTINCT");
                } else {
                    agr.add("");
                }
                if (returnObject.getFieldValueString(i, 15).equals("Y")) {
                    agr.add("roleflag");
                } else {
                    agr.add("");
                }
                agr.add(returnObject.getFieldValueString(i, 1));
                //Changed by Nazneen
                defaultaggr.put(returnObject.getFieldValueString(i, 0), agr);
//                defaultaggr.put(i, agr);

            }

//             defaultaggr.put("Ids",displayNameIds);
            Gson gson = new Gson();
            jsonString = gson.toJson(defaultaggr);

        } 
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        }catch (Exception e) {
            logger.error("Exception:", e);
        }        return jsonString;
        }

    public String getallDimensions(String grpid, String bussTableId) {
        String jsonString = null;
        PbReturnObject returnObject = null;
        try {
            String query = getResourceBundle().getString("getBusinessGroupDimList1");
            returnObject = new PbReturnObject();
            PbDb pbDb = new PbDb();

            Object ojb[] = new Object[3];
            ojb[0] = grpid;
            ojb[1] = bussTableId;
            ojb[2] = bussTableId;
            String finalQuery = buildQuery(query, ojb);
            returnObject = pbDb.execSelectSQL(finalQuery);
//            Connection con= ProgenConnection.getInstance().getConnectionByGroupId(grpid);
            HashMap map = new HashMap();


            for (int i = 0; i < returnObject.getRowCount(); i++) {
                ArrayList arrayList = new ArrayList();
                arrayList.add(returnObject.getFieldValueString(i, 3));
                map.put(returnObject.getFieldValueString(i, 0) + "~" + returnObject.getFieldValueString(i, 2), arrayList);

            }
            Gson gson = new Gson();
            jsonString = gson.toJson(map);
        } 
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        }catch (Exception e) {
            logger.error("Exception:", e);
        }
        return jsonString;
    }

    public String getBussTableName(String dimid) {
        PbReturnObject returnObject = null;
        String busstablename = null;
        try {
            String query = getResourceBundle().getString("getBusinessGroupDimTableList");
            returnObject = new PbReturnObject();
            PbDb pbDb = new PbDb();
            Object ojb[] = new Object[1];
            ojb[0] = dimid;
            String finalQuery = buildQuery(query, ojb);
            returnObject = pbDb.execSelectSQL(finalQuery);
//           ProgenConnection.getInstance().getConnectionByGroupId(grpId);
            for (int i = 0; i < returnObject.getRowCount(); i++) {
                busstablename = returnObject.getFieldValueString(i, 1);
            }
        }
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }        return null;
        }

    public String getCustomerTableDetails(String dimentionID, String grpID) {
        String dimDetailsJson = "";
        String getCustomerTableDetails = getResourceBundle().getString("getCustomerTableDetails");
        Object[] object = new Object[2];
        object[0] = dimentionID.split("~")[1];
        object[1] = grpID;
        String finalQuery = super.buildQuery(getCustomerTableDetails, object);
        Connection connection = ProgenConnection.getInstance().getConnectionByGroupId(grpID);
        try {
            PbReturnObject dimObject = super.execSelectSQL(finalQuery);
            finalQuery = "select distinct " + dimObject.getFieldValueString(0, 0) + " from " + dimObject.getFieldValueString(0, 6);
            try {
                HashMap<String, ArrayList> tempHashMap = new HashMap<String, ArrayList>();
                ArrayList<String> list = new ArrayList<String>();
                dimObject = super.execSelectSQL(finalQuery, connection);
                for (int i = 0; i < dimObject.getRowCount(); i++) {
                    list.add(dimObject.getFieldValueString(i, 0));

                }
                tempHashMap.put("DimValues", list);

                dimDetailsJson = new Gson().toJson(tempHashMap);

            } catch (Exception ex) {
                logger.error("Exception: ", ex);
            }
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        } finally {
            if (connection != null) {
                connection = null;
            }
        }
        return dimDetailsJson;
    }
//start by E

    public String getConnectionNames() {
        PbReturnObject returnObject = null;
        String connectionNamesJson = null;
        HashMap map = new HashMap();
        LinkedList li = new LinkedList();
        LinkedList li2 = new LinkedList();
        try {


            String getConnectionNames = getResourceBundle().getString("getConnectionNames");
            returnObject = new PbReturnObject();
            PbDb pbDb = new PbDb();
            returnObject = pbDb.execSelectSQL(getConnectionNames);
            for (int i = 0; i < returnObject.getRowCount(); i++) {
                li.add(returnObject.getFieldValueString(i, 0));

                li2.add(returnObject.getFieldValueString(i, 1));
            }
            map.put("connids", li);
            map.put("connnames", li2);


            Gson gson = new Gson();
            connectionNamesJson = gson.toJson(map);

        } 
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        }catch (Exception e) {
            logger.error("Exception:", e);
        }
        return connectionNamesJson;
    }

    public String getDisplayTableNames(String conId) {

        PbReturnObject returnObject = null;
        String displyTabNamesJson = null;
        HashMap map = new HashMap();
        ArrayList al = new ArrayList();
        try {


            String getDisplayTableNames = getResourceBundle().getString("getDisplayTableNames");
            Connection connection = ProgenConnection.getInstance().getConnectionByConId(conId);
            GetConnectionType getConnectionType = new GetConnectionType();
            String connectionType = getConnectionType.getConTypeByConnID(conId);
            if (!connectionType.equalsIgnoreCase("Mysql")) {
//           connection.
                returnObject = new PbReturnObject();
                PbDb pbDb = new PbDb();
                returnObject = pbDb.execSelectSQL(getDisplayTableNames, connection);
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    al.add(returnObject.getFieldValueString(i, 0));


                }
            } else {
                DatabaseMetaData dbm = connection.getMetaData();
                String[] types = {"TABLE"};
                ResultSet rs = dbm.getTables(null, null, "%", types);
                while (rs.next()) {
                    al.add(rs.getString("TABLE_NAME"));
                }
            }

            map.put("tablenames", al);

            Gson gson = new Gson();
            displyTabNamesJson = gson.toJson(map);
        }
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return displyTabNamesJson;
    }

    public String getTableColumns(String tabname, String connID) {
        PbReturnObject returnObject = null;
        String displyTabNamesJson = "";
        HashMap map = new HashMap();
        ArrayList al = new ArrayList();
        try {

            String getDisplayTableNames = getResourceBundle().getString("getTableColumns");
            returnObject = new PbReturnObject();
            PbDb pbDb = new PbDb();
            Object ojb[] = new Object[1];
            ojb[0] = tabname;
            String finalQuery = buildQuery(getDisplayTableNames, ojb);
            GetConnectionType getConnectionType = new GetConnectionType();
            String connectionType = getConnectionType.getConTypeByConnID(connID);
            if (!connectionType.equalsIgnoreCase("Mysql")) {
                returnObject = pbDb.execSelectSQL(finalQuery, ProgenConnection.getInstance().getConnectionByConId(connID));
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    al.add(returnObject.getFieldValueString(i, 0));

                }
            } else {
                DatabaseMetaData dbm = ProgenConnection.getInstance().getConnectionByConId(connID).getMetaData();
                String[] types = {"TABLE"};
                ResultSet rs = dbm.getTables(null, null, "%", types);
                ResultSet tempRs = dbm.getColumns(null, null, tabname, "%");
                while (tempRs.next()) {
                    al.add(tempRs.getString("COLUMN_NAME"));
                }
            }
            map.put("columnnames", al);
            Gson gson = new Gson();
            displyTabNamesJson = gson.toJson(map);
        } 
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        }catch (Exception e) {
            logger.error("Exception:", e);
        }
        return displyTabNamesJson;
    }

    public String saveTragetDetails(String connectionId, String[] durationDetails, String[] dimentionvalue, String[] targetvalue, String grpid, String dimAndMemId) {
        String query = getResourceBundle().getString("saveTragetDetails");
        String insertquery = getResourceBundle().getString("saveTragetDetailsinsertquery");
        boolean status = false;
        PbReturnObject checkTableExistenca = null;
        try {
            checkTableExistenca = super.execSelectSQL("SELECT TRGT_BUS_TAB_NAME FROM PRG_GRP_TARGET_MAPPING WHERE MEM_ID=" + dimAndMemId.split("~")[1] + " AND  DIM_ID=" + dimAndMemId.split("~")[0]);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }

        String tempTableName = getTableName(grpid, dimAndMemId);
        ArrayList queryList = new ArrayList();
        Object[] objects = new Object[1];
        int seq = -1;
        PbReturnObject pbReturnObject = new PbReturnObject();
        try {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                pbReturnObject = super.execSelectSQL("SELECT IDENT_CURRENT('TARGET_DETAILS_SEQ')");
            } else {
                pbReturnObject = super.execSelectSQL("SELECT TARGET_DETAILS_SEQ.NEXTVAL FROM DUAL ");
            }
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        if (pbReturnObject.getRowCount() > 0) {
            seq = pbReturnObject.getFieldValueInt(0, 0);
        }
        tempTableName = tempTableName + "_TARGET" + seq;
        objects[0] = tempTableName;
        try {
            if (checkTableExistenca != null && checkTableExistenca.getRowCount() == 0) {
                queryList.add(super.buildQuery(query, objects));
            } else {
                tempTableName = "";
            }

            Object[] insertObject = new Object[4];
            for (int count = 0; count < dimentionvalue.length; count++) {
                if (checkTableExistenca != null && checkTableExistenca.getRowCount() == 0) {
                    insertObject[0] = tempTableName;
                } else {
                    insertObject[0] = checkTableExistenca.getFieldValueString(0, 0);
                }
                Calendar calendar = Calendar.getInstance();
                int month = Calendar.FEBRUARY;
                int date = 1;
                insertObject[1] = "STR_TO_DATE('01-" + durationDetails[count] + "', '%d-%M-%Y') ";
                insertObject[2] = dimentionvalue[count];
                if (!targetvalue[count].equalsIgnoreCase("")) {
                    insertObject[3] = targetvalue[count];
                } else {
                    insertObject[3] = "null";
                }
                queryList.add(super.buildQuery(insertquery, insertObject));
            }
            status = super.executeMultiple(queryList, ProgenConnection.getInstance().getConnectionByConId(connectionId));
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        if (status) {
            return tempTableName;
        } else {
            return "";
        }
    }

    private String getTableName(String grpid, String dimAndMemId) {
        String getCustomerTableDetails = getResourceBundle().getString("getCustomerTableDetails");
        Object[] object = new Object[2];
        object[0] = dimAndMemId.split("~")[1];
        object[1] = grpid;
        String finalQuery = super.buildQuery(getCustomerTableDetails, object);
        PbReturnObject dimObject = new PbReturnObject();
        try {
            dimObject = super.execSelectSQL(finalQuery);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }

        return dimObject.getFieldValueString(0, 6);
    }

    public boolean pushBusinessGroup(String dimAndMemId, String grupId, String tableNameforDimentions, String connectionId, String busscolid) {
        boolean returnStatus = false;
        String insertBussTable = getResourceBundle().getString("insertBussTableforTrgt");
        String insertGrpBussTableSrcForTrgt = getResourceBundle().getString("insertGrpBussTableSrcForTrgt");
        String insertintoBusstableSrcDetailsForTrgt = getResourceBundle().getString("insertintoBusstableSrcDetailsForTrgt");
        String insertintoBussTabDetails = getResourceBundle().getString("insertintoBussTabDetails");
        String updateBussTableDetails = getResourceBundle().getString("updateBussTableDetails");
        ArrayList queryList = new ArrayList();
        Object[] busTableDetails = new Object[6];
        String[] trgtColumnNames = {"TARGET_DURATION", "TARGET_MEMBERS", "TARGET_VALUE"};

        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
        } else {
            busTableDetails[0] = "(PRG_GRP_BUSS_TABLE_SEQ.nextval)";
            busTableDetails[1] = tableNameforDimentions;
            busTableDetails[2] = tableNameforDimentions;
            busTableDetails[3] = grupId;
            busTableDetails[4] = tableNameforDimentions.replace("_", " ");
            busTableDetails[5] = tableNameforDimentions.replace("_", " ");
            queryList.add(super.buildQuery(insertBussTable, busTableDetails));
            busTableDetails = new Object[3];
            busTableDetails[0] = "(PRG_GRP_BUSS_TABLE_SRC_seq.nextval)";
            busTableDetails[1] = "(PRG_GRP_BUSS_TABLE_SEQ.currval)";
            busTableDetails[2] = connectionId;
            queryList.add(super.buildQuery(insertGrpBussTableSrcForTrgt, busTableDetails));
            String[] columnTypes = {"DATE", "VARCHAR2", "NUMBER"};
            Object[] busstabDetails = new Object[7];
            for (int i = 0; i < trgtColumnNames.length; i++) {
                busTableDetails = new Object[5];
                busTableDetails[0] = "(PRG_GRP_BUSS_TAB_SRC_DTLS_SEQ.NEXTVAL)";
                busTableDetails[1] = "(PRG_GRP_BUSS_TABLE_SRC_seq.currval)";
                busTableDetails[2] = "(PRG_GRP_BUSS_TABLE_SEQ.currval)";
                busTableDetails[3] = trgtColumnNames[i];
                busTableDetails[4] = columnTypes[i];
                queryList.add(super.buildQuery(insertintoBusstableSrcDetailsForTrgt, busTableDetails));
                busstabDetails[0] = "(PRG_GRP_BUSS_TABLE_DETAILS_seq.nextval)";
                busstabDetails[1] = "(PRG_GRP_BUSS_TABLE_SEQ.currval)";
                busstabDetails[2] = trgtColumnNames[i];
                busstabDetails[3] = "(PRG_GRP_BUSS_TAB_SRC_DTLS_SEQ.currval)";
                busstabDetails[4] = trgtColumnNames[i];
                busstabDetails[5] = trgtColumnNames[i];
                busstabDetails[6] = columnTypes[i];
                queryList.add(super.buildQuery(insertintoBussTabDetails, busstabDetails));

            }
            queryList.add(super.buildQuery(updateBussTableDetails, new Object[]{grupId}));


        }
        boolean status = super.executeMultiple(queryList);
        String getCustomerTableDetails = getResourceBundle().getString("getCustomerTableDetails");
        Object[] object = new Object[2];
        object[0] = dimAndMemId.split("~")[1];
        object[1] = grupId;
        String finalQuery = super.buildQuery(getCustomerTableDetails, object);
        try {
            PbReturnObject pbReturnObject = super.execSelectSQL(finalQuery);
            PbReturnObject trgtObject = super.execSelectSQL("SELECT PB.COLUMN_NAME,PT.BUSS_TABLE_NAME,PT.BUSS_TABLE_ID,PB.BUSS_COLUMN_ID FROM PRG_GRP_BUSS_TABLE_DETAILS PB , PRG_GRP_BUSS_TABLE PT WHERE PT.BUSS_TABLE_NAME ='" + tableNameforDimentions + "' and PT.BUSS_TABLE_ID=PB.BUSS_TABLE_ID and PB.COLUMN_NAME='TARGET_VALUE'");
            Object[] trgrObject = new Object[10];
            if (trgtObject != null && pbReturnObject != null) {
                trgrObject[0] = dimAndMemId.split("~")[1];
                trgrObject[1] = dimAndMemId.split("~")[0];
                trgrObject[2] = pbReturnObject.getFieldValueString(0, 7);
                trgrObject[3] = pbReturnObject.getFieldValueString(0, 3);
                trgrObject[4] = pbReturnObject.getFieldValueString(0, 0);
                trgrObject[5] = pbReturnObject.getFieldValueString(0, 6);
                trgrObject[6] = trgtObject.getFieldValueString(0, 2);
                trgrObject[7] = trgtObject.getFieldValueString(0, 3);
                trgrObject[8] = trgtObject.getFieldValueString(0, 0);
                trgrObject[9] = trgtObject.getFieldValueString(0, 1);

            }
            finalQuery = super.buildQuery("INSERT INTO PRG_GRP_TARGET_MAPPING VALUES(TARGET_DETAILS_SEQ.NEXTVAL,&,&,&,&,'&','&',&,&,'&','&')", trgrObject);

            super.execModifySQL(finalQuery);
            pbReturnObject = super.execSelectSQL("SELECT PB.COLUMN_NAME,PT.BUSS_TABLE_NAME,PT.BUSS_TABLE_ID,PB.BUSS_COLUMN_ID FROM PRG_GRP_BUSS_TABLE_DETAILS PB , PRG_GRP_BUSS_TABLE PT WHERE PT.BUSS_TABLE_NAME ='" + tableNameforDimentions + "' AND PB.COLUMN_TYPE='DATE' and PT.BUSS_TABLE_ID=PB.BUSS_TABLE_ID ");
            object = new Object[3];
            object[0] = pbReturnObject.getFieldValueString(0, 2);
            object[1] = pbReturnObject.getFieldValueString(0, 3);
            object[2] = busscolid;
            finalQuery = super.buildQuery("INSERT INTO PRG_TIME_DIM_INFO select  prg_time_dim_info_SEQ.NEXTVAL,BUSINESS_TABLE_ID,&,&,MIN_LEVEL,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_BY,IS_MAIN,IS_AS_OF_DATE_JOIN,BUSINESS_COL_ID from prg_time_dim_info where main_fact_id =(select BUSS_TABLE_ID from PRG_GRP_BUSS_TABLE_DETAILS WHERE BUSS_COLUMN_ID=&)", object);
            queryList.clear();
            queryList.add(finalQuery);
            object = new Object[4];
            object[0] = pbReturnObject.getFieldValueString(0, 2);
            object[1] = pbReturnObject.getFieldValueString(0, 3);
            object[2] = "trunc(" + pbReturnObject.getFieldValueString(0, 1) + "." + pbReturnObject.getFieldValueString(0, 0) + ")";
            object[3] = busscolid;
            finalQuery = super.buildQuery("INSERT INTO PRG_TIME_DIM_INFO_RLT_DETAILS SELECT PRG_TIME_INFO_RLT_DETAILS_SEQ.NEXTVAL,prg_time_dim_info_SEQ.CURRVAL,&,&,S_BUSS_TABLE_ID,S_BUSS_COL_ID1,JOIN_TYPE,JOIN_OPERATOR,'& = PROGEN_TIME.DDATE',P_BUSS_COL_ID2,S_BUSS_COL_ID2,S_COL1_NAME,S_COL1_FORMAT,S_COL2_NAME,S_COL2_FORMAT FROM PRG_TIME_DIM_INFO_RLT_DETAILS WHERE P_BUSS_TABLE_ID=(select BUSS_TABLE_ID from PRG_GRP_BUSS_TABLE_DETAILS WHERE BUSS_COLUMN_ID=&)", object);
            queryList.add(finalQuery);
            super.executeMultiple(queryList);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return returnStatus;
    }

    public ArrayList getSegmentColumns(String tablename, String segcolumns, String conid) {
        PbReturnObject returnObject = null;
        String displySegColNamesJson = null;
        HashMap map = new HashMap();
        ArrayList al = new ArrayList();
        try {
//       String finalQuery1="select distinct " + segcolumns + "  from " + tablename + " order by " + segcolumns;
            String getSegmentColumns = getResourceBundle().getString("getSegmentColumns");
            returnObject = new PbReturnObject();
            PbDb pbDb = new PbDb();
            Object ojb[] = new Object[3];
            ojb[0] = segcolumns;
            ojb[1] = tablename;
            ojb[2] = segcolumns;
            String finalQuery = buildQuery(getSegmentColumns, ojb);
            returnObject = pbDb.execSelectSQL(finalQuery, ProgenConnection.getInstance().getConnectionByConId(conid));
//      returnObject= pbDb.execSelectSQL(finalQuery1,ProgenConnection.getInstance().getConnectionByConId(conid));
            for (int i = 0; i < returnObject.getRowCount(); i++) {
                if (returnObject.getFieldValueString(i, 0) != null && !returnObject.getFieldValueString(i, 0).equalsIgnoreCase("")) {
                    al.add(returnObject.getFieldValueString(i, 0));
                }
            }
//           map.put("segcolnames",al);
//             Gson gson=new Gson();
//             displySegColNamesJson=gson.toJson(map);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return al;
    }

    public String getDependColumns(String tablename, String dependCols, String conid) {
        PbReturnObject returnObject = null;
        String displayDependColNamesJson = null;
        HashMap map = new HashMap();
        ArrayList al = new ArrayList();
        try {
            String getDependColumns = getResourceBundle().getString("getDependColumns");
            returnObject = new PbReturnObject();
            PbDb pbDb = new PbDb();
            Object ojb[] = new Object[2];
            ojb[0] = dependCols;
            ojb[1] = tablename;
            String finalQuery = buildQuery(getDependColumns, ojb);
            returnObject = pbDb.execSelectSQL(finalQuery, ProgenConnection.getInstance().getConnectionByConId(conid));
            for (int i = 0; i < returnObject.getRowCount(); i++) {
                al.add(returnObject.getFieldValueString(i, 0));
            }
            map.put("dependcolnames", al);
            Gson gson = new Gson();
            displayDependColNamesJson = gson.toJson(map);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return displayDependColNamesJson;
    }
    //end by E

    //added by ramesh
    public Integer saveSegmentationValues(String relationName, String tableName, String connId, String dependColumn, String baseColumn, String groupjson, List grpmember, List grpmembervalue, List grprole) {
        PbDb pbDb = new PbDb();
        String targetupdatequery = null;
        PbReturnObject returnObject = null;
        String grpvales = null;
        try {
            String savesegmentationquery = getResourceBundle().getString("insertsegmentationvalues");
            Object segmentationobj1[] = new Object[6];
            segmentationobj1[0] = relationName;
            segmentationobj1[1] = tableName;
            segmentationobj1[2] = connId;
            segmentationobj1[3] = dependColumn;
            segmentationobj1[4] = baseColumn;
            segmentationobj1[5] = groupjson;
            String finalQuery = super.buildQuery(savesegmentationquery, segmentationobj1);
//                  pbDb.execModifySQL(finalQuery );
            super.execUpdateSQL(finalQuery);
            for (int i = 0; i < grpmember.size(); i++) {
                String groupmembers = (String) grpmember.get(i);
                String groupvalues = (String) grpmembervalue.get(i);
                String grouprules = (String) grprole.get(i);
                try {
//                                
                    Connection connection = ProgenConnection.getInstance().getConnectionByConId(connId);
                    if (grouprules.equals("IN")) {
                        Object segmentationobj[] = new Object[6];
                        segmentationobj[0] = tableName;
                        segmentationobj[1] = dependColumn;
                        segmentationobj[2] = groupmembers;
                        segmentationobj[3] = baseColumn;
                        segmentationobj[4] = grouprules;
                        String str = "','";
                        segmentationobj[5] = Joiner.on(str).join(grpmembervalue.get(i).toString().split(",")).toString();
                        String updatesegquery = getResourceBundle().getString("updatesegmentationtable");
                        String finalQuery1 = buildQuery(updatesegquery, segmentationobj);
                        int j = pbDb.execUpdateSQL(finalQuery1, connection);
                    } else {
                        Object segmentationobj2[] = new Object[6];
                        segmentationobj2[0] = tableName;
                        segmentationobj2[1] = dependColumn;
                        segmentationobj2[2] = groupmembers;
                        segmentationobj2[3] = baseColumn;
                        segmentationobj2[4] = grouprules;
                        segmentationobj2[5] = groupvalues;
                        String updatesegquery = getResourceBundle().getString("updatesegmentationtable");
                        String finalQuery1 = buildQuery(updatesegquery, segmentationobj2);
                        pbDb.execUpdateSQL(finalQuery1, connection);
                    }
                } catch (Exception e) {
                    logger.error("Exception:", e);
                }
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return 1;
    }

    public String getFlexiSegmentationTableNames(String conid) {
        PbReturnObject flexiObject = null;
        String FlexiTableNamesJson = null;
        LinkedList<String> flexiids = new LinkedList<String>();
        LinkedList<String> flexitablename = new LinkedList<String>();
        LinkedList<String> flexigroupnames = new LinkedList<String>();
        LinkedList<String> flexioriginal = new LinkedList<String>();
        LinkedList<String> fleximapid = new LinkedList<String>();
        LinkedList<String> flexiid = new LinkedList<String>();
        LinkedList<String> statictable = new LinkedList<String>();
        LinkedList<String> staticview = new LinkedList<String>();
        HashMap fleximap = new HashMap();
        try {
            String flexiquery = getResourceBundle().getString("getflexitablenames");
            String flexigroupnamesquery = getResourceBundle().getString("getgroupnames");
            flexiObject = new PbReturnObject();
//       Connection connection=ProgenConnection.getInstance().getConnectionByConId(conid);
            flexiObject = super.execSelectSQL(flexiquery);
            if (flexiObject != null) {
                for (int i = 0; i < flexiObject.getRowCount(); i++) {

                    flexiids.add(flexiObject.getFieldValueString(i, 0));
                    flexitablename.add(flexiObject.getFieldValueString(i, 1));
                    flexioriginal.add(flexiObject.getFieldValueString(i, 3));
                    fleximapid.add(flexiObject.getFieldValueString(i, 4));
                    flexiid.add(flexiObject.getFieldValueString(i, 2));
                    statictable.add(flexiObject.getFieldValueString(i, 5));
                    staticview.add(flexiObject.getFieldValueString(i, 6));


                }
            }
            fleximap.put("flexitablename", flexitablename);
            fleximap.put("flexiids", flexiids);
            fleximap.put("flexioriginal", flexioriginal);
            fleximap.put("fleximapid", fleximapid);
            fleximap.put("flexiid", flexiid);
            fleximap.put("statictable", statictable);
            fleximap.put("staticview", staticview);

            Gson gson = new Gson();
            FlexiTableNamesJson = gson.toJson(fleximap);

        } 
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        }catch (Exception e) {
            logger.error("Exception:", e);
        }        return FlexiTableNamesJson;
        }
//by sunita

    public void moveFlexiSegmentationTableData(String conid, String statictable, String staticview) {
        PbReturnObject returnObject = null;
        Connection connection = null;
        Statement st = null;
        String connid = null;
        try {
            if (conid != null) {
                connection = ProgenConnection.getInstance().getConnectionByConId(conid);
            } else {
                String conquery = "SELECT CONNECTION_ID, CONNECTION_NAME FROM PRG_USER_CONNECTIONS";
                returnObject = super.execSelectSQL(conquery);
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    connid = returnObject.getFieldValueString(i, 0);

                }
                connection = ProgenConnection.getInstance().getConnectionByConId(conid);
            }
            st = connection.createStatement();
            st.execute("truncate table " + statictable);
            st.execute("insert into " + statictable + " select * from " + staticview);

        } 
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        }catch (Exception e) {
            logger.error("Exception:", e);
        }        return;
        }

    public HashMap flexigrouplistdao(String flextableid, String conid) {
        HashMap resultMap = new HashMap();
        PbReturnObject returnObject = null;
        String displySegColNamesJson = null;
        HashMap map = new HashMap();
        ArrayList nameList = new ArrayList();
        ArrayList idList = new ArrayList();
        Connection connection = null;
        String connid = null;
        try {
            PbReturnObject pbReturnObject = super.execSelectSQL("select * from  PRG_FLEXI_SEGMENTATION WHERE ID='" + flextableid + "'");
            String tempquery = getResourceBundle().getString("getflexicolumns");
            returnObject = new PbReturnObject();
            PbDb pbDb = new PbDb();
            Object ojb[] = new Object[3];
            ojb[0] = pbReturnObject.getFieldValueString(0, 3);
            ojb[1] = pbReturnObject.getFieldValueString(0, 4);
            ojb[2] = pbReturnObject.getFieldValueString(0, 1);

            String finalQuery = buildQuery(tempquery, ojb);
            if (conid != null) {
                connection = ProgenConnection.getInstance().getConnectionByConId(conid);
            } else {
                String conquery = "SELECT CONNECTION_ID, CONNECTION_NAME FROM PRG_USER_CONNECTIONS";
                returnObject = super.execSelectSQL(conquery);
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    connid = returnObject.getFieldValueString(i, 0);

                }
                connection = ProgenConnection.getInstance().getConnectionByConId(conid);
            }
            returnObject = pbDb.execSelectSQL(finalQuery, connection);
            if (pbReturnObject.getFieldValueString(0, 3).equalsIgnoreCase("country")) {
                PbReturnObject retobj = new PbReturnObject();
                String query = "select distinct & from &";
                String name = pbReturnObject.getFieldValueString(0, 3);
                String tablename1 = pbReturnObject.getFieldValueString(0, 1);
                Object obj[] = new Object[2];
                obj[0] = name;
                obj[1] = tablename1;
                String finalsql = buildQuery(query, obj);

                Connection conn = ProgenConnection.getInstance().getConnectionByConId(conid);
                retobj = super.execSelectSQL(finalsql, conn);
                for (int j = 0; j < retobj.getRowCount(); j++) {
                    int flag = 0;
                    for (int k = 0; k < j; k++) {
                        if (returnObject.getFieldValueString(j, 0).equals(returnObject.getFieldValueString(k, 0))) {
                            flag = 1;
                        }
                    }
                    if (flag == 0) {
                        nameList.add(retobj.getFieldValueString(j, "country"));
                        // idList.add(returnObject.getFieldValueString(j,1));
                    }
                }
            } else {
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    int flag = 0;
                    for (int j = 0; j < i; j++) {
                        if (returnObject.getFieldValueString(i, 0).equals(returnObject.getFieldValueString(j, 0))) {
                            flag = 1;
                        }
                    }
                    if (flag == 0) {
                        nameList.add(returnObject.getFieldValueString(i, 0));
                        idList.add(returnObject.getFieldValueString(i, 1));
                    }
                }
            }
            resultMap.put("NameList", nameList);
            resultMap.put("IdList", idList);

        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return resultMap;
    }

    public String getFlexiName(String flextableid) {
        PbReturnObject pbreturnObject = null;
        try {
            pbreturnObject = super.execSelectSQL("select * from  PRG_FLEXI_SEGMENTATION WHERE ID='" + flextableid + "'");

        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return pbreturnObject.getFieldValueString(0, 3);
    }

    public void saveFlexiSegmentation(String conId, String tablename, String grpName, List<String> grpmember, List<String> grprole, String[] grpmembervalue, String[] memberIds, String flexiid, String flexitableid) throws Exception {

        if (flexiid.equals("flexi_id_text")) {
            String savesegmentationquery = "INSERT INTO prg_flexi_segment_supp (flexi_table, flexi_Group, flexi_Type, flexi_Name,flexi_id_text,flexi_table_id) "
                    + " VALUES('&','&','&','&','&',&)";
            Object obj[] = new Object[6];
            int i = 0;
            ArrayList queryList = new ArrayList();
            for (String str : grpmembervalue) {
                int count = 0;
                for (String member : str.split(",")) {
                    String tempArrS[] = memberIds[i].split(",");
                    obj[0] = tablename;
                    obj[1] = grpName;
                    obj[2] = grpmember.get(i);
                    String[] tempDetails = tempArrS[count].split("~");
                    obj[3] = tempDetails[1];

                    if (tempArrS[count] != null && !tempArrS[count].equals("")) {
                        obj[4] = tempDetails[0];
                    } else {
                        obj[4] = "-1";
                    }
                    obj[5] = flexitableid;
                    count++;
                    queryList.add(super.buildQuery(savesegmentationquery, obj));

                }
                i++;
            }
            super.executeMultiple(queryList, ProgenConnection.getInstance().getConnectionByConId(conId));
        } else {
            String savesegmentationquery = "INSERT INTO prg_flexi_segment_supp (flexi_table, flexi_Group, flexi_Type, flexi_Name, flexi_id_num,flexi_table_id ) "
                    + " VALUES('&','&','&','&',&,&)";
            Object obj[] = new Object[6];
            int i = 0;
            ArrayList queryList = new ArrayList();
            for (String str : grpmembervalue) {
                int count = 0;
                for (String member : str.split(",")) {
                    String tempArrS[] = memberIds[i].split(",");
                    obj[0] = tablename;
                    obj[1] = grpName;
                    obj[2] = grpmember.get(i);
                    String[] tempDetails = tempArrS[count].split("~");
                    obj[3] = tempDetails[1];
                    if (tempArrS[count] != null && !tempArrS[count].equals("")) {
                        obj[4] = tempDetails[0];
                    } else {
                        obj[4] = "-1";
                    }
                    obj[5] = flexitableid;
                    count++;
                    queryList.add(super.buildQuery(savesegmentationquery, obj));
                }
                i++;
            }
            super.executeMultiple(queryList, ProgenConnection.getInstance().getConnectionByConId(conId));

        }
    }

    public void insertFlexiSegmentation(String conId, String tablename, String grpName, List<String> grprole, String[] memberIds, String flexiid, String flexitableid, String membergrppath, String membervaluepath, String hiddenmemid, String tabid) throws Exception {
        String savesegmentationquery = "INSERT INTO prg_flexi_segment_supp (flexi_table, flexi_Group, flexi_Type, flexi_Name, flexi_id_num,flexi_table_id ) "
                + " VALUES('&','&','&','&',&,&)";
        Object obj[] = new Object[6];
        ArrayList queryList = new ArrayList();
        String membergrp[] = membergrppath.split(",");
        String membervalue[] = membervaluepath.split(",");
        String hideenmemvalue[] = hiddenmemid.split(",");
        String orginalhiddenvalue[] = null;
        String hiddenid = null;

        for (int i = 0; i < membergrp.length; i++) {
            String temphiddenvalue = hideenmemvalue[i];
            orginalhiddenvalue = temphiddenvalue.split("~");
            hiddenid = orginalhiddenvalue[0];
            String membergrpid = membergrp[i];
            String membervalueid = membervalue[i];
            obj[0] = tablename;
            obj[1] = grpName;
            obj[2] = membergrpid;
            obj[3] = membervalueid;
            obj[4] = hiddenid;
            obj[5] = tabid;
            String finalquery = super.buildQuery(savesegmentationquery, obj);

            super.execUpdateSQL(finalquery, ProgenConnection.getInstance().getConnectionByConId(conId));

        }

    }

    public String getSegmentationNames() {
        String jsonString = null;
        PbReturnObject returnObject = null;
        Gson json = new Gson();
        try {
            String query = getResourceBundle().getString("getsegmentationJsonvalues");
            returnObject = super.execSelectSQL(query);
            List<RuleHelper> helpers = null;
            Map segmentationmap = new HashMap();
            Type listType = new TypeToken<ArrayList<RuleHelper>>() {
            }.getType();
            if (returnObject.getRowCount() != 0) {
                List<String> segmentid = new ArrayList<String>();
                List<String> relationname = new ArrayList<String>();
                List<String> groupmemberlist = new ArrayList<String>();
                List<String> rulesList = new ArrayList<String>();
                List<String> groupValues = new ArrayList<String>();
                List<String> tablenamesList = new ArrayList<String>();
                List<String> DependColumnlist = new ArrayList<String>();
                List<String> BaseColumnList = new ArrayList<String>();
                List<String> conid = new ArrayList<String>();
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    segmentid.add(returnObject.getFieldValueString(i, "SEGMENT_ID"));
                    relationname.add(returnObject.getFieldValueString(i, "RELATION_NAME"));
                    helpers = json.fromJson(returnObject.getFieldValueString(i, "RELATION_DETAILS"), listType);
                    for (int j = 0; j < helpers.size(); j++) {
                        conid.add(helpers.get(j).getConnectionid());
                        groupmemberlist.add(helpers.get(j).getGroupmember());
                        rulesList.add(helpers.get(j).getRule());
                        groupValues.add(helpers.get(j).getGroupvalues().toString());
                        tablenamesList.add(helpers.get(j).getTableName());
                        DependColumnlist.add(helpers.get(j).getDependColumnName());
                        BaseColumnList.add(helpers.get(j).getBaseColmnName());
                    }
                }
                segmentationmap.put("segmentid", segmentid);
                segmentationmap.put("conid", conid);
                segmentationmap.put("relationname", relationname);
                segmentationmap.put("groupmemberlist", groupmemberlist);
                segmentationmap.put("rulesList", rulesList);
                segmentationmap.put("groupValues", groupValues);
                segmentationmap.put("tablenamesList", tablenamesList);
                segmentationmap.put("DependColumnlist", DependColumnlist);
                segmentationmap.put("BaseColumnList", BaseColumnList);
                jsonString = json.toJson(segmentationmap);
            }
        } 
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        }catch (Exception e) {
            logger.error("Exception:", e);
        }        return jsonString;
        }

    public Integer updateSegmentationValues(String conid, String tablename, String dependcolumn, String basecolumn, List grpmember, List grpmembervalue, List rules) {
        PbReturnObject returnObject = null;
        try {
            for (int i = 0; i < grpmember.size(); i++) {
                String groupmembers = (String) grpmember.get(i);
                String groupvalues = (String) grpmembervalue.get(i);
                String grouprules = (String) rules.get(i);
                if (grouprules.equals("IN")) {
                    Object segmentationobj[] = new Object[6];
                    segmentationobj[0] = tablename;
                    segmentationobj[1] = dependcolumn;
                    segmentationobj[2] = groupmembers;
                    segmentationobj[3] = basecolumn;
                    segmentationobj[4] = grouprules;
                    String str = "','";
                    String temp = Joiner.on(str).join(grpmembervalue.get(i).toString().split(",")).toString();
                    String tempstr = temp.substring(0);
                    segmentationobj[5] = tempstr;
                    String updatesegquery = getResourceBundle().getString("updatesegmentationtable");
                    String finalQuery1 = buildQuery(updatesegquery, segmentationobj);
                    int j = super.execUpdateSQL(finalQuery1, ProgenConnection.getInstance().getConnectionByConId(conid));
                }
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public String deleteSegmentationValues(String segmentid, String relationName, String tableName, String connId, String dependColumn, String baseColumn, String groupjson, List grpmember, List grpmembervalue, List grprole) {
        try {
            String query = getResourceBundle().getString("updatesegvalues");
            Object deletesegmentationobj[] = new Object[7];
            deletesegmentationobj[0] = relationName;
            deletesegmentationobj[1] = tableName;
            deletesegmentationobj[2] = connId;
            deletesegmentationobj[3] = dependColumn;
            deletesegmentationobj[4] = baseColumn;
            deletesegmentationobj[5] = groupjson;
            deletesegmentationobj[6] = segmentid;
            String finalQuery1 = buildQuery(query, deletesegmentationobj);
            int j = super.execUpdateSQL(finalQuery1);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public String getFlexiSegmentationTableColumns(String tablename, String conid, String fileximapid, String flexioriginalid, String memvalues) {
        String jsonString = null;
        PbReturnObject returnObject = null;
        Gson json = new Gson();
        Map map = new HashMap();
        try {
            String query = getResourceBundle().getString("getflexitablecolumns");
            Object flexisegmentationobj[] = new Object[5];
            flexisegmentationobj[0] = flexioriginalid;
            flexisegmentationobj[1] = fileximapid;
            flexisegmentationobj[2] = tablename;
            flexisegmentationobj[3] = flexioriginalid;
            flexisegmentationobj[4] = memvalues;
            String finalQuery1 = buildQuery(query, flexisegmentationobj);
            returnObject = super.execSelectSQL(finalQuery1, ProgenConnection.getInstance().getConnectionByConId(conid));
            LinkedList<String> name = new LinkedList<String>();
            LinkedList<String> idlist = new LinkedList<String>();
            for (int i = 0; i < returnObject.getRowCount(); i++) {
                name.add(returnObject.getFieldValueString(i, 0));
                idlist.add(returnObject.getFieldValueString(i, 1));
            }
            map.put("name", name);
            map.put("idlist", idlist);
            Gson gson = new Gson();
            jsonString = gson.toJson(map);


        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return jsonString;
    }

    public String saveFlexiSegmentationTableColumnNames(String tablename, String groupname, String groupmembers, String grplikevalues, String likeid, String conid) {
        try {
            String savesegmentationquery = "INSERT INTO prg_flexi_segment_supp (flexi_table, flexi_Group, flexi_Type, flexi_Name, flexi_id_num ) "
                    + " VALUES('&','&','&','&',&)";
            Object obj[] = new Object[5];
            obj[0] = tablename;
            obj[1] = groupname;
            obj[2] = groupmembers;
            obj[3] = grplikevalues;
            obj[4] = likeid;
            String finalQuery = super.buildQuery(savesegmentationquery, obj);
            super.execUpdateSQL(finalQuery, ProgenConnection.getInstance().getConnectionByConId(conid));


        } catch (Exception e) {
            logger.error("Exception:", e);
        }



        return null;
    }

    public String getFlexiNames(String conid) throws Exception {
        String jsonString = null;
        PbReturnObject returnObject = null;
        PbReturnObject returnObject1 = null;
        Gson json = new Gson();
//        String conquery="SELECT CONNECTION_ID, CONNECTION_NAME FROM PRG_USER_CONNECTIONS";
//       String conid=null;
//
//        returnObject=super.execSelectSQL(conquery);
//        for(int i=0;i<returnObject.getRowCount();i++)
//        {
//            conid=returnObject.getFieldValueString(i,0);
//
//        }
        String query = "select distinct flexi_table,flexi_Group,flexi_table_id from prg_flexi_segment_supp";
        String grpquery = "select flexi_Type,flexi_Name from prg_flexi_segment_supp";

        List<String> tablenames = new ArrayList<String>();
        List<String> groupnames = new ArrayList<String>();
        List<String> flexitableid = new ArrayList<String>();
        List<String> grpmember = new ArrayList<String>();
        List<String> grpvalues = new ArrayList<String>();
        Map fliexsegmentationmap = new HashMap();
        Connection connection = ProgenConnection.getInstance().getConnectionByConId(conid);
        try {
            returnObject = super.execSelectSQL(query, connection);
            for (int i = 0; i < returnObject.getRowCount(); i++) {
                int flag = 0;
                for (int j = 0; j < i; j++) {
                    if (returnObject.getFieldValueString(i, 0).equals(returnObject.getFieldValueString(j, 0))) {
                        flag = 1;
                    }
                }
                if (flag == 0) {
                    tablenames.add(returnObject.getFieldValueString(i, 0));
                    groupnames.add(returnObject.getFieldValueString(i, "flexi_Group"));
                    flexitableid.add(returnObject.getFieldValueString(i, "flexi_table_id"));
                }
            }

        } catch (Exception e) {
            connection.close();
        }
        try {
            Connection connection1 = ProgenConnection.getInstance().getConnectionByConId(conid);
            returnObject1 = super.execSelectSQL(grpquery, connection1);
            for (int i = 0; i < returnObject1.getRowCount(); i++) {
                grpmember.add(returnObject1.getFieldValueString(i, 0));
                grpvalues.add(returnObject1.getFieldValueString(i, 1));
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        fliexsegmentationmap.put("tablenames", tablenames);
        fliexsegmentationmap.put("groupnames", groupnames);
        fliexsegmentationmap.put("grpmember", grpmember);
        fliexsegmentationmap.put("grpvalues", grpvalues);
        fliexsegmentationmap.put("flexitableid", flexitableid);

        Gson gson = new Gson();
        jsonString = gson.toJson(fliexsegmentationmap);
        return jsonString;

    }

    public String getFlexiMemberNames(String groupname) {
        String jsonString = null;
        PbReturnObject returnObject = null;
        Gson json = new Gson();
        String conquery = "SELECT CONNECTION_ID, CONNECTION_NAME FROM PRG_USER_CONNECTIONS";
        String conid = null;
        try {
            returnObject = super.execSelectSQL(conquery);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        for (int i = 0; i < returnObject.getRowCount(); i++) {
            conid = returnObject.getFieldValueString(i, 0);

        }

        String query = "select flexi_Type,flexi_Name,flexi_id_num from prg_flexi_segment_supp where flexi_Group='" + groupname + "'";
        List<String> grpmember = new ArrayList<String>();
        List<String> grpvalues = new ArrayList<String>();
        List<String> fliexidnum = new ArrayList<String>();
        Map fliexsegmentationmap = new HashMap();
        Connection connection = ProgenConnection.getInstance().getConnectionByConId(conid);
        try {
            returnObject = super.execSelectSQL(query, connection);
            for (int i = 0; i < returnObject.getRowCount(); i++) {
                grpmember.add(returnObject.getFieldValueString(i, 0));
                grpvalues.add(returnObject.getFieldValueString(i, 1));
                fliexidnum.add(returnObject.getFieldValueString(i, 2));
            }
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        fliexsegmentationmap.put("grpmember", grpmember);
        fliexsegmentationmap.put("grpvalues", grpvalues);
        fliexsegmentationmap.put("fliexidnum", fliexidnum);
        jsonString = json.toJson(fliexsegmentationmap);


        return jsonString;
    }

    public String deleteFlexiValues(String grpmember, String grpvalues, String flexiidnum) {
        String str = null;
        PbReturnObject returnObject = null;
        Gson json = new Gson();
        String conquery = "SELECT CONNECTION_ID, CONNECTION_NAME FROM PRG_USER_CONNECTIONS";
        String conid = null;
        try {
            returnObject = super.execSelectSQL(conquery);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        for (int i = 0; i < returnObject.getRowCount(); i++) {
            conid = returnObject.getFieldValueString(i, 0);
        }
        String query = "delete from prg_flexi_segment_supp where flexi_Type='&' and flexi_Name='&' and flexi_id_num=&";
        Object obj[] = new Object[3];
        obj[0] = grpmember;
        obj[1] = grpvalues;
        obj[2] = flexiidnum;
        String finalquery = super.buildQuery(query, obj);
        try {
            super.execUpdateSQL(finalquery, ProgenConnection.getInstance().getConnectionByConId(conid));
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return null;
    }

    public String getGroupnames(String connId) {
        String jsonString = null;
        PbReturnObject returnObject = null;
        Gson json = new Gson();
        List<String> grplilst = new ArrayList<String>();
        HashMap grpmap = new HashMap();

        String query = "select distinct flexi_Group from prg_flexi_segment_supp";
        try {
            returnObject = super.execSelectSQL(query, ProgenConnection.getInstance().getConnectionByConId(connId));
            for (int i = 0; i < returnObject.getRowCount(); i++) {
                grplilst.add(returnObject.getFieldValueString(i, 0));
            }
            grpmap.put("grplilst", grplilst);
            jsonString = json.toJson(grpmap);

        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return jsonString;
    }

    public String getGroupnames1(String connId, String tabname) {
        String jsonString = null;
        PbReturnObject returnObject = null;
        Gson json = new Gson();
        List<String> grplilst = new ArrayList<String>();
        HashMap grpmap = new HashMap();

        String query = "select distinct flexi_Group from prg_flexi_segment_supp where flexi_table='&'";
        Object obj[] = new Object[1];
        obj[0] = tabname;
        String finalquery = super.buildQuery(query, obj);
        try {
            returnObject = super.execSelectSQL(finalquery, ProgenConnection.getInstance().getConnectionByConId(connId));
            for (int i = 0; i < returnObject.getRowCount(); i++) {
                grplilst.add(returnObject.getFieldValueString(i, 0));
            }
            grpmap.put("grplilst", grplilst);
            jsonString = json.toJson(grpmap);

        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return jsonString;
    }

    public String getRecords(String connId, String typesname) {
        String jsonString = null;
        PbReturnObject returnObject = null;
        Gson gson = new Gson();

        List<String> tablenames = new ArrayList<String>();
        List<String> groupnames = new ArrayList<String>();
        List<String> typenames = new ArrayList<String>();
        List<String> flexiName = new ArrayList<String>();
        List<String> flexiIdtext = new ArrayList<String>();
        List<String> flexiIdnum = new ArrayList<String>();
        List<String> flexitableid = new ArrayList<String>();
        Map fliexsegmentationmap = new HashMap();
        String query = "select * from prg_flexi_segment_supp where flexi_Type='&'";
        Object obj[] = new Object[1];
        obj[0] = typesname;
        String finalquery = super.buildQuery(query, obj);

//        

        try {
            returnObject = super.execSelectSQL(finalquery, ProgenConnection.getInstance().getConnectionByConId(connId));
            for (int i = 0; i < returnObject.getRowCount(); i++) {
                tablenames.add(returnObject.getFieldValueString(i, 0));
                groupnames.add(returnObject.getFieldValueString(i, "flexi_Group"));
                typenames.add(returnObject.getFieldValueString(i, "flexi_Type"));
                flexiName.add(returnObject.getFieldValueString(i, "flexi_Name"));
                flexiIdtext.add(returnObject.getFieldValueString(i, "flexi_id_text"));
                flexiIdnum.add(returnObject.getFieldValueString(i, "flexi_id_num"));
                flexitableid.add(returnObject.getFieldValueString(i, "flexi_table_id"));
            }

        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        fliexsegmentationmap.put("tablenames", tablenames);
        fliexsegmentationmap.put("groupnames", groupnames);
        fliexsegmentationmap.put("typenames", typenames);
        fliexsegmentationmap.put("flexiName", flexiName);
        fliexsegmentationmap.put("flexiIdtext", flexiIdtext);
        fliexsegmentationmap.put("flexiIdnum", flexiIdnum);
        fliexsegmentationmap.put("flexitableid", flexitableid);


        jsonString = gson.toJson(fliexsegmentationmap);
        return jsonString;

    }

    public String getTablenames(String connId) {
        String jsonString = null;
        PbReturnObject returnObject = null;
        Gson json = new Gson();
        List<String> tablilst = new ArrayList<String>();
        HashMap tabmap = new HashMap();

        String query = "select distinct flexi_table from prg_flexi_segment_supp";
        try {
            returnObject = super.execSelectSQL(query, ProgenConnection.getInstance().getConnectionByConId(connId));
            for (int i = 0; i < returnObject.getRowCount(); i++) {
                tablilst.add(returnObject.getFieldValueString(i, 0));
            }
            tabmap.put("tablilst", tablilst);
            jsonString = json.toJson(tabmap);

        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return jsonString;
    }
    //by sunita

    public String getTypenames(String connId, String grpname) {
        String jsonString = null;
        PbReturnObject returnObject = null;
        Gson json = new Gson();
        List<String> typlist = new ArrayList<String>();
        HashMap typmap = new HashMap();

        String query = "select distinct flexi_Type from prg_flexi_segment_supp where flexi_Group='&' ";
        Object obj[] = new Object[1];
        obj[0] = grpname;
        String finalquery = super.buildQuery(query, obj);
        try {
            returnObject = super.execSelectSQL(finalquery, ProgenConnection.getInstance().getConnectionByConId(connId));
            for (int i = 0; i < returnObject.getRowCount(); i++) {
                typlist.add(returnObject.getFieldValueString(i, 0));
            }
            typmap.put("typlist", typlist);
            jsonString = json.toJson(typmap);

        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return jsonString;
    }

    public String deleteGroupNames(String connId, String groupname) {
        String query = "delete from prg_flexi_segment_supp where flexi_Group='&'";
        Object obj[] = new Object[1];
        obj[0] = groupname;
        String finalquery = super.buildQuery(query, obj);
        try {
            int i = super.execUpdateSQL(finalquery, ProgenConnection.getInstance().getConnectionByConId(connId));

        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }


        return null;
    }
    //sunita

    public String deleteTypeNames(String connId, String typename) {
        String query = "delete from prg_flexi_segment_supp where flexi_Type='&'";

        Object obj[] = new Object[1];
        obj[0] = typename;
        String finalquery = super.buildQuery(query, obj);
        try {
            int i = super.execUpdateSQL(finalquery, ProgenConnection.getInstance().getConnectionByConId(connId));

        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }


        return null;
    }

    public String deleteRecord(String connId, String tablename, String groupname, String typename, String flexiname, String flexitext, String flexinum, String flexitable) {
//        
        String query = "delete from prg_flexi_segment_supp where flexi_table='&' and flexi_Group='&' and flexi_Type='&' and flexi_Name='&' and flexi_id_text='&' and flexi_id_num='&' and flexi_table_id='&'";

        Object obj[] = new Object[7];
        obj[0] = tablename;
        obj[1] = groupname;
        obj[2] = typename;
        obj[3] = flexiname;
        obj[4] = flexitext;
        obj[5] = flexinum;
        obj[6] = flexitable;
        String finalquery = super.buildQuery(query, obj);
//        
        try {
            int i = super.execUpdateSQL(finalquery, ProgenConnection.getInstance().getConnectionByConId(connId));

        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }


        return null;
    }

    public String deleteTableNames(String connId, String tablename) {
        String query = "delete from prg_flexi_segment_supp where flexi_table='&'";
        Object obj[] = new Object[1];
        obj[0] = tablename;
        String finalquery = super.buildQuery(query, obj);
        try {
            int i = super.execUpdateSQL(finalquery, ProgenConnection.getInstance().getConnectionByConId(connId));

        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }


        return null;
    }

    public String deleteRelations(String segmentid, String relationname) {
        try {
            String relation = null;
            if (relationname != null) {
                relation = "";
            }
            String query = "UPDATE PRG_DIM_SEGMENTATION_RULES SET RELATION_NAME='&' WHERE SEGMENT_ID=& ";
            Object[] obj = new Object[2];
//             String temp = Joiner.on("','").join(relation.split(",")).toString();
            obj[0] = Joiner.on("','").join(relation.split(",")).toString();
            obj[1] = segmentid;
            String finalquery = super.buildQuery(query, obj);
            super.execUpdateSQL(finalquery);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return null;
    }
}
