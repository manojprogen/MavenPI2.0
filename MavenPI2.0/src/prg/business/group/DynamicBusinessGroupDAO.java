package prg.business.group;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

public class DynamicBusinessGroupDAO extends PbDb {

    public static Logger logger = Logger.getLogger(DynamicBusinessGroupDAO.class);
    //PbBussGrpResourceBundle resBundle = (prg.business.group.PbBussGrpResourceBundle)ResourceBundle.getBundle("prg.business.group.PbBussGrpResourceBundle");
//    PbBussGrpResourceBundle resBundle = new PbBussGrpResourceBundle();
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

    public String getBusinessGroups(String connId) {

        StringBuffer busgrpbfer = new StringBuffer();
        String busgrpstr = null;
        ArrayList finalList = new ArrayList();

        busgrpbfer.append("<div style='height:555px;overflow-y:auto;'>");
        busgrpbfer.append("<ul id='myList2' class='filetree'>");
        busgrpbfer.append("<li  class='closed' style=background-image:url('images/treeViewImages/plus.gif')><img src='images/treeViewImages/bricks.png'><span class='bussGroupMenu' >&nbsp; Business Groups</span>");
        busgrpbfer.append("<ul id='bussGrps'>");

        try {
            ////////.println("START IN JAVA BUSGRP LIST");
            PbReturnObject retObj = null;
            String[] colNames = null;
            String sql = getResourceBundle().getString("getBusinessGroupList");
            Object obj[] = new Object[1];
            obj[0] = connId;

            //susheela start 11-12-09
            String timeMapQ = "";
            String timeFlag = "!";

            String finalQuery = buildQuery(sql, obj);
            ////.println("getBusinessGroupList\t" + finalQuery);

            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {


                Businessgrps businessgrps = new Businessgrps();
                businessgrps.setBusinessGrpName(retObj.getFieldValueString(i, colNames[1]));
                businessgrps.setBusinessGrpId(String.valueOf(retObj.getFieldValueInt(i, colNames[0])));

                //susheela start 11-12-09
                timeFlag = "!";
                timeMapQ = "SELECT DIM_ID, DIM_NAME, GRP_ID FROM PRG_GRP_DIMENSIONS where grp_id=" + String.valueOf(retObj.getFieldValueInt(i, colNames[0])) + " and dim_name='Time'";
                ////.println(" timeMapQ " + timeMapQ);
                PbReturnObject timObj = execSelectSQL(timeMapQ);
                if (timObj.getRowCount() > 0) {
                    timeFlag = "";
                }
                businessgrps.setGrpTimeFlag(timeFlag);

                /*
                 *
                 * ArrayList dimensionList = new ArrayList(); ArrayList
                 * allTablesList = new ArrayList(); ArrayList factsList = new
                 * ArrayList(); ArrayList bucketsList = new ArrayList();
                 * ArrayList roleList = new ArrayList(); dimensionList =
                 * getDimensions(String.valueOf(retObj.getFieldValueInt(i,
                 * colNames[0]))); allTablesList =
                 * getAllTables(String.valueOf(retObj.getFieldValueInt(i,
                 * colNames[0]))); factsList =
                 * getFacts(String.valueOf(retObj.getFieldValueInt(i,
                 * colNames[0])));
                 *
                 * //added by susheela 05-oct-09 start // ArrayList
                 * targetFactsList = new ArrayList(); // targetFactsList =
                 * getAddedTargetMeasures(String.valueOf(retObj.getFieldValueInt(i,
                 * colNames[0]))); ArrayList grpTargetMeasuresList = new
                 * ArrayList(); grpTargetMeasuresList =
                 * getAddedTargets(String.valueOf(retObj.getFieldValueInt(i,
                 * colNames[0]))); //added by susheela 05-oct-09 over
                 *
                 * bucketsList =
                 * getBuckets(String.valueOf(retObj.getFieldValueInt(i,
                 * colNames[0]))); roleList =
                 * getBusinessRoles(String.valueOf(retObj.getFieldValueInt(i,
                 * colNames[0]))); businessgrps.setDimensionList(dimensionList);
                 * businessgrps.setAllTablesList(allTablesList);
                 * businessgrps.setFactsList(factsList);
                 * businessgrps.setBucketsList(bucketsList);
                 * businessgrps.setRoleList(roleList); //added by susheela
                 * 05-oct-09 start
                 *
                 * // businessgrps.setTargetFactsList(targetFactsList);
                businessgrps.setGrpTargetMeasuresList(grpTargetMeasuresList);
                 */

                //added by susheela 05-oct-09 over

                finalList.add(businessgrps);

                busgrpbfer.append("<li id=" + businessgrps.getBusinessGrpId() + " class='closed'><img src='images/treeViewImages/bricks.png'><span class='createFolderMenu'>" + businessgrps.getGrpTimeFlag() + "&nbsp;" + businessgrps.getBusinessGrpName() + "</span>");
                busgrpbfer.append("<ul>");
                busgrpbfer.append("<li class='closed' id='dimGrp-" + String.valueOf(retObj.getFieldValueInt(i, colNames[0])) + "' onclick='getdimensions(this.id)'><img src='images/treeViewImages/Dim.gif'><span id='123' class='addGrpDims' ><font size='1px' face='verdana'>&nbsp;Dimensions</font></span>");
                busgrpbfer.append("<ul id=dimsfor-" + businessgrps.getBusinessGrpId() + ">");
                busgrpbfer.append("</ul>");
                busgrpbfer.append("</li>");
                busgrpbfer.append("<li class='closed' id=" + businessgrps.getBusinessGrpId() + " onclick='getBuckets(this.id)'><img src='images/beaker-empty.png'><span id='bkt-" + businessgrps.getBusinessGrpId() + "_span'  class='bucketEditMenu'>&nbsp;Buckets</span>");
                busgrpbfer.append("<ul id=bckt-" + businessgrps.getBusinessGrpId() + ">");
                busgrpbfer.append("</ul>");
                busgrpbfer.append("</li>");
                busgrpbfer.append("<li class='closed' id=" + businessgrps.getBusinessGrpId() + " onclick='getFacts(this.id)'><span id=" + businessgrps.getBusinessGrpId() + " class='folder formulaMenu'>&nbsp;Facts</span>");
                busgrpbfer.append("<ul id=facts-" + businessgrps.getBusinessGrpId() + ">");
                busgrpbfer.append("</ul>");
                busgrpbfer.append("</li>");
                busgrpbfer.append("<li class='closed' onclick='targetMeasure(this.id),addTrgetMsr(this.id);' id=" + businessgrps.getBusinessGrpId() + "><img src='images/treeViewImages/folder-closed.gif'>&nbsp;<span  class='targetDiv'>Add Target Measures</span>");
//                id='TargetMeasuresFactTree'
                busgrpbfer.append("<ul id=trgtmsr-" + businessgrps.getBusinessGrpId() + ">");
                busgrpbfer.append("</ul>");
                busgrpbfer.append("</li>");
                busgrpbfer.append("<li class='closed' id='AllTab-" + businessgrps.getBusinessGrpId() + "' onclick='getAllTables(this.id)'><span class='folder addGrpTables'>&nbsp;AllTables</span>");
                busgrpbfer.append("<ul id='ul-" + businessgrps.getBusinessGrpId() + "'>");
                busgrpbfer.append("</ul>");
                busgrpbfer.append("</li>");
                busgrpbfer.append("<li class='closed' id=" + businessgrps.getBusinessGrpId() + " onclick=getbusroles(this.id)><img src='images/treeViewImages/folder-closed.gif'><span >&nbsp;Business Roles</span>");
                busgrpbfer.append("<ul id='busroles-" + businessgrps.getBusinessGrpId() + "'>");
                busgrpbfer.append("</ul>");
                busgrpbfer.append("</li>");
//                busgrpbfer.append("<li class='closed' id=" + businessgrps.getBusinessGrpId() + " onclick=getGroupHierarchy(this.id)><img src='images/treeViewImages/folder-closed.gif'><span class='groupHierarechyclick'>&nbsp;Groups Hierarchy</span>");
//                busgrpbfer.append("<ul id='busgroup-" + businessgrps.getBusinessGrpId() + "'>");
//                busgrpbfer.append("</ul>");
//                busgrpbfer.append("</li>");
                busgrpbfer.append("<li class='closed' id=" + businessgrps.getBusinessGrpId() + " onclick=getGroupHierarchy(this.id)><img src='images/treeViewImages/folder-closed.gif'><span class='groupHierarechyclick'>&nbsp;Groups</span>");
                busgrpbfer.append("<ul id='busgroup-" + businessgrps.getBusinessGrpId() + "'>");
                busgrpbfer.append("</ul>");
                busgrpbfer.append("</li>");
                busgrpbfer.append("</ul>");
                busgrpbfer.append("</li>");





                /*
                 * <li id="<bean:write name="BusinessGroupList"
                 * property="businessGrpId"/>" class="closed" ><img
                 * src="images/treeViewImages/bricks.png"><span
                 * class="createFolderMenu"><bean:write name="BusinessGroupList"
                 * property="grpTimeFlag"/>&nbsp;<bean:write
                 * name="BusinessGroupList" property="businessGrpName"/></span>
                 * <ul> <li class="closed" id='dimGrp-<bean:write
                 * name="BusinessGroupList" property="businessGrpId"/>'><img
                 * src="images/treeViewImages/Dim.gif"><span id="123"
                 * class="addGrpDims" ><font size="1px"
                 * face="verdana">&nbsp;Dimensions</font></span> <ul>
                 */

            }

        } 
        catch (SQLException ex) {
            logger.error("Exception:", ex);
        }catch (Exception e) {
            logger.error("Exception:", e);
        }        busgrpstr = busgrpbfer.toString();

        return busgrpstr;
    }

    //added by susheela start  28-12-09
    public String getAddedTargets(String busGroupId) throws Exception {

        StringBuffer measuerbfer = new StringBuffer();
        String meauserstr = null;


        ArrayList tableName = new ArrayList();
        String getAddedTargetMeas = getResourceBundle().getString("getAddedTargetMeas");
        String getAddedTargets = getResourceBundle().getString("getAddedTargets");

        Object grpObj[] = new Object[1];
        grpObj[0] = busGroupId;
        String fgetAddedTargetMeas = buildQuery(getAddedTargetMeas, grpObj);
        ////.println("fgetAddedTargetMeas\t" + fgetAddedTargetMeas);
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

            if (!(measureName.equals(""))) {
                measuerbfer.append("<li class='closed'><img src='images/treeViewImages/database_table.png'>&nbsp;<span class='createTargetDiv' id=" + measureId + ">" + measureName + "</span>");
                measuerbfer.append("<ul>");
            }

            String fingetAddedTargets = buildQuery(getAddedTargets, measure);
            ////.println("fingetAddedTargets\t" + fingetAddedTargets);
            PbReturnObject tarObj = execSelectSQL(fingetAddedTargets);
            if (tarObj.getRowCount() == 0) {
                table.setEndTable("true");
                measuerbfer.append("</ul></li>");
            }
            // tableName.add(table);
            tableName.add(table);
            for (int y = 0; y < tarObj.getRowCount(); y++) {
                BusinessGrpsTreeTable sectable = new BusinessGrpsTreeTable();
                targetId = tarObj.getFieldValueString(y, "TARGET_ID");
                targetName = tarObj.getFieldValueString(y, "TARGET_NAME");

                if (!(targetName.equals(""))) {
                    measuerbfer.append("<li><span class='targetIdDiv' id='" + targetId + ":" + tarObj.getFieldValueString(y, "MIN_TIME_LEVEL") + ">" + targetName + "</span></li>");
                }

                sectable.setTargetId(targetId);
                sectable.setTargetMinTimeLevel(tarObj.getFieldValueString(y, "MIN_TIME_LEVEL"));
                sectable.setTargetName(targetName);


                if (y + 1 == tarObj.getRowCount()) {
                    sectable.setEndTable("true");
                }
                tableName.add(sectable);
            }
        }
        meauserstr = measuerbfer.toString();
        return meauserstr;

    }

    public void updateFactColumnFlag(String tableId, String columnId) throws Exception {   ////////.println(" in update--");
        String currentStatusQ = "select role_flag from prg_grp_buss_table_details where buss_column_id=" + columnId + " and buss_table_id=" + tableId;
        ////////////.println(" currentStatusQ "+currentStatusQ);
        PbReturnObject pbro = execSelectSQL(currentStatusQ);
        String oldSt = pbro.getFieldValueString(0, "ROLE_FLAG");
        String newSt = "";
        if (oldSt.equalsIgnoreCase("Y")) {
            newSt = "N";
        } else {
            newSt = "Y";
        }
        String updateQ = "update prg_grp_buss_table_details set role_flag='" + newSt + "' where buss_column_id=" + columnId + " and buss_table_id=" + tableId;
        ////////////.println(" updateQ -"+updateQ);
        execModifySQL(updateQ);
    }

    public String getAddedTargetMeasures(String busGroupId) throws Exception {

        StringBuffer measuerbfer = new StringBuffer();
        String meauserstr = null;

        String getSavedTargetMeasure = getResourceBundle().getString("getSavedTargetMeasure");//getSavedMeasure
        String getSavedMeasure = getResourceBundle().getString("getSavedMeasure");

        ArrayList tableName = new ArrayList();
        Object TabObj[] = new Object[1];
        TabObj[0] = busGroupId;
        String fingetSavedTargetMeasure = buildQuery(getSavedMeasure, TabObj);
        String tabQ = "select col_id,col_name,pgbt.buss_table_name original_table,buss_original_table_id from target_master_col_details tmcd,prg_target_master ptm,prg_grp_buss_table pgbt where tmcd.buss_table_id=ptm.target_table_id and tmcd.selected_measure='Y' and buss_original_table_id = pgbt.buss_table_id and ptm.business_group=" + busGroupId;
        ////.println(" fingetSavedTargetMeasure tabQ " + tabQ);
// PbReturnObject factRetOb= execSelectSQL(fingetSavedTargetMeasure);
        PbReturnObject factRetOb = execSelectSQL(tabQ);

        for (int o = 0; o < factRetOb.getRowCount(); o++) {
            BusinessGrpsTreeTable table = new BusinessGrpsTreeTable();
            table.setTargetMeasureId(factRetOb.getFieldValueString(o, "COL_ID"));
            table.setTargetMeasureName(factRetOb.getFieldValueString(o, "COL_NAME"));
            measuerbfer.append("<li class='closed'><img src='images/treeViewImages/database_table.png'>&nbsp;<span class='createTargetDiv' id=" + factRetOb.getFieldValueString(o, "COL_ID") + ">" + factRetOb.getFieldValueString(o, "COL_NAME") + "</span>");
            measuerbfer.append("<ul>");
// table.setMColName(factRetOb.getFieldValueString(o,"COL_NAME"));
            BusinessGrpsTreeTable sectable = new BusinessGrpsTreeTable();
            BusinessGrpsTreeTable terTable = new BusinessGrpsTreeTable();
            for (int h = 0; h < 1; h++) {
                sectable.setTargetFactTableId(factRetOb.getFieldValueString(o, "BUSS_ORIGINAL_TABLE_ID"));
                sectable.setTargetFactTableName(factRetOb.getFieldValueString(o, "ORIGINAL_TABLE"));

                if (h == 0) {
                    sectable.setEndTable("true");
                    measuerbfer.append("</ul></li>");
                }
                String getMeasureMembersQ = "select * from prg_target_measure_members where measure_id in(select prg_measure_id from prg_target_master where measure_name='" + factRetOb.getFieldValueString(o, "COL_NAME") + "' and business_group=" + busGroupId + ")";
                ////.println(" getMeasureMemebersQ " + getMeasureMembersQ);
                PbReturnObject MeasureMemOb = execSelectSQL(getMeasureMembersQ);
                Businessgrps businessgrps = new Businessgrps();
                ArrayList measureMemList = new ArrayList();
                BusinessGrpsTreeTable tempTable = new BusinessGrpsTreeTable();

                tableName.add(table);
                tableName.add(sectable);

            }

        }
        meauserstr = measuerbfer.toString();
        return meauserstr;
//        return tableName;
    }

    //added by susheela over
    public String getDimensions(String grpId) {

        StringBuffer dimbuffer = new StringBuffer();
        String dimensionstr = null;

        ArrayList finalList = new ArrayList();

        try {

            PbReturnObject retObj = null;
            String finalQuery = null;
            String[] colNames = null;
            Object obj[] = new Object[1];
            obj[0] = grpId;
            String sql = getResourceBundle().getString("getBusinessGroupDimList");
            finalQuery = buildQuery(sql, obj);
            ////.println("finalQuery\t" + finalQuery);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                BusinessGrpsTreeTable dimension = new BusinessGrpsTreeTable();
                dimension.setDimensionName(retObj.getFieldValueString(i, colNames[1]));
                dimension.setDimensionId(String.valueOf(retObj.getFieldValueInt(i, colNames[0])));

                dimbuffer.append("<li class='closed'><img src='images/treeViewImages/Dim.gif' ><span class='BgDimensionDiv' id=" + grpId + "~" + dimension.getDimensionId() + "><font size='1px' face='verdana'>&nbsp;" + dimension.getDimensionName() + "</font></span>");
                dimbuffer.append("<ul>");


//                ArrayList tableList = new ArrayList();
//                tableList = getTableList(String.valueOf(retObj.getFieldValueInt(i, colNames[0])), grpId);
//                dimension.setDimTableList(tableList);

                dimbuffer.append(getTableList(String.valueOf(retObj.getFieldValueInt(i, colNames[0])), grpId));
                if (getTableList(String.valueOf(retObj.getFieldValueInt(i, colNames[0])), grpId) != null) {
//                    ////.println(getTableList(String.valueOf(retObj.getFieldValueInt(i, colNames[0])), grpId));
                }

//                ArrayList membersList = new ArrayList();
//                membersList = getMembersList(String.valueOf(retObj.getFieldValueInt(i, colNames[0])), grpId);
//                dimension.setDimMembersList(membersList);

                dimbuffer.append(getMembersList(String.valueOf(retObj.getFieldValueInt(i, colNames[0])), grpId));
                if (getMembersList(String.valueOf(retObj.getFieldValueInt(i, colNames[0])), grpId) != null) {
//                    ////.println(getMembersList(String.valueOf(retObj.getFieldValueInt(i, colNames[0])), grpId));
                }
//                ArrayList hierarchyList = new ArrayList();
//                hierarchyList = getHeirarchyList(String.valueOf(retObj.getFieldValueInt(i, colNames[0])), grpId);
//                dimension.setDimHierarchyList(hierarchyList);

                dimbuffer.append(getHeirarchyList(String.valueOf(retObj.getFieldValueInt(i, colNames[0])), grpId));
                if (getHeirarchyList(String.valueOf(retObj.getFieldValueInt(i, colNames[0])), grpId) != null) {
//                    ////.println(getHeirarchyList(String.valueOf(retObj.getFieldValueInt(i, colNames[0])), grpId));
                }
                //////////.println("hierarchy list==" + hierarchyList.size());

                /*
                 * if (dimension.getDimTableName() != null &&
                 * !(dimension.getDimTableName().equals(""))) {
                 * dimbuffer.append("<li class='closed'><img
                 * src='images/treeViewImages/database_table.png'><span ><font
                 * size='1px' face='verdana'>&nbsp;" +
                 * dimension.getDimTableName() + "</font></span>");
                 * dimbuffer.append("<ul>"); } if (dimension.getDimColumnName()
                 * != null && !(dimension.getDimColumnName().equals(""))) {
                 * dimbuffer.append("<li id=" + dimension.getDimColumnId() +
                 * "/>~" + dimension.getDimColumnName() + " class='closed'>");
                 * if (dimension.getIsAvailable().equals("Y")) { if
                 * (dimension.getColType().equals("NUMBER")) {
                 * dimbuffer.append("<span ><font size='1px'
                 * face='verdana'>&nbsp;" + dimension.getDimColumnName() +
                 * "</font></span>"); } if
                 * (!(dimension.getColType().equals("NUMBER"))) {
                 * dimbuffer.append("<span ><font size='1px'
                 * face='verdana'>&nbsp;" + dimension.getDimColumnName() +
                 * "</font></span>"); } } if
                 * (dimension.getIsAvailable().equals("N")) { if
                 * (dimension.getColType().equals("NUMBER")) {
                 * dimbuffer.append("<span ><font size='1px'
                 * face='verdana'>&nbsp;" + dimension.getDimColumnName() + "," +
                 * dimension.getColType() + "</font></span>"); } if
                 * (!(dimension.getColType().equals("NUMBER"))) {
                 * dimbuffer.append("<span ><font size='1px'
                 * face='verdana'>&nbsp;" + dimension.getDimColumnName() + "," +
                 * dimension.getColType() + "</font></span>"); } } if
                 * (dimension.getIsPk().equals("Y")) { dimbuffer.append("<img
                 * src='images/treeViewImages/key.png' >"); }
                 * dimbuffer.append("</li>"); } if (dimension.getEndTable() !=
                 * null && dimension.getEndTable().equals("true")) {
                 * dimbuffer.append("</ul></li>"); }
                 */
                dimbuffer.append("</ul></li>");

                finalList.add(dimension);
                dimensionstr = dimbuffer.toString();
            }
//             dimbuffer.append("</ul></li>");
        } 
        catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return dimensionstr;
    }

    public String getTableList(String dimId, String grpId) {

        ArrayList list = new ArrayList();
        StringBuffer tablebufer = new StringBuffer();
        String tablestr = null;

        try {
            String finalQuery = null;
            String sql;
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                sql = getResourceBundle().getString("getBusinessGroupDimTableListMysql");
            } else {
                sql = getResourceBundle().getString("getBusinessGroupDimTableList");
            }

            tablebufer.append("<li class='closed'><span class='folder'><font size='1px' face='verdana'>Tables</font></span>");
            tablebufer.append("<ul>");

            Object obj[] = new Object[1];
            obj[0] = dimId;
            finalQuery = buildQuery(sql, obj);
            ////.println("getBusinessGroupDimTableList\t" + finalQuery);
            PbReturnObject allTableObject = execSelectSQL(finalQuery);
            String[] tableNames = null;
            tableNames = allTableObject.getColumnNames();
            String[] tabId = new String[allTableObject.getRowCount()];
            String[] tabName = new String[allTableObject.getRowCount()];
            String[] originaltabId = new String[allTableObject.getRowCount()];
            String[] tabdispName = new String[allTableObject.getRowCount()];
            String[] tabtooltipName = new String[allTableObject.getRowCount()];
            for (int i = 0; i < allTableObject.getRowCount(); i++) {
                originaltabId[i] = String.valueOf(allTableObject.getFieldValueInt(i, tableNames[0]));
                tabId[i] = String.valueOf(allTableObject.getFieldValueInt(i, tableNames[3]));
                tabName[i] = allTableObject.getFieldValueString(i, tableNames[1]);
                tabtooltipName[i] = allTableObject.getFieldValueString(i, tableNames[4]);
                //////////.println("table name==" + tabName[i] + "  table id===" + tabId[i]);
            }
            String[] colNames = null;
            String colName = "";
            String tabName1 = "";
            String tabtooltipName1 = "";
            String tabId1 = "";
            String sqlcolumns = getResourceBundle().getString("getBusinessGroupDimTableColList");
            for (int i = 0; i < tabId.length; i++) {
                tabName1 = tabName[i];
                tabtooltipName1 = tabtooltipName[i];
                tabId1 = tabId[i];
                Object obj1[] = new Object[2];
                obj1[0] = dimId;
                obj1[1] = originaltabId[i];

                finalQuery = buildQuery(sqlcolumns, obj1);
                ////.println("getBusinessGroupDimTableColList \t" + finalQuery);
                PbReturnObject pbReturnObject = execSelectSQL(finalQuery);
                BusinessGrpsTreeTable table = new BusinessGrpsTreeTable();
                table.setDimTableName(tabName1);
                table.setDimTableId(tabId1 + "," + originaltabId[i] + "," + grpId);
                // ////////.println("sqlcolumns is "+tabName1+"---"+finalQuery);
                list.add(table);
                colNames = pbReturnObject.getColumnNames();

                if (tabName1 != null && !(tabName1.equals(""))) {
                    tablebufer.append("<li  class='closed'><img src='images/treeViewImages/database_table.png'><span title='" + tabtooltipName1 + "'><font size='1px' face='verdana'>&nbsp;" + tabName1 + "</font></span>");
                    tablebufer.append("<ul>");
                }

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
                    //////////.println("table col id in dims-->" + columnId + "col colName--->" + colName);
                    BusinessGrpsTreeTable secTable = new BusinessGrpsTreeTable();
                    if (colName != null && !(colName.equals(""))) {
                        tablebufer.append("<li id=" + columnId + "~" + colName + " class='closed'>");
                        if (is_available.equals("Y")) {
                            if (colType.equals("NUMBER")) {
                                tablebufer.append("<span ><font size='1px' face='verdana'>&nbsp;" + colName + "</font></span>");
                            }
                            if (!(colType.equals("NUMBER"))) {
                                tablebufer.append("<span ><font size='1px' face='verdana'>&nbsp;" + colName + "</font></span>");
                            }
                        }
                        if (is_available.equals("N")) {
                            if (colType.equals("NUMBER")) {
                                tablebufer.append("<span ><font size='1px' face='verdana'>&nbsp;" + colName + "," + colType + "</font></span>");
                            }
                            if (!(colType.equals("NUMBER"))) {
                                tablebufer.append("<span ><font size='1px' face='verdana'>&nbsp;" + colName + "," + colType + "</font></span>");
                            }
                        }
                        if (is_pk.equals("Y")) {
                            tablebufer.append("<img src='images/treeViewImages/key.png' >");
                        }
                        tablebufer.append("</li>");
                    }

                    if (j + 1 == pbReturnObject.getRowCount()) {
                        secTable.setEndTable("true");
//                 //////////.println(""+j);

                        tablebufer.append("</ul></li>");
                    }

                    secTable.setDimColumnName(colName);
                    secTable.setDimColumnId(columnId);
                    secTable.setIsPk(is_pk);
                    secTable.setIsAvailable(is_available);
                    secTable.setColType(colType);
                    list.add(secTable);
                }
            }
            tablebufer.append("</ul></li>");
        } 
        catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }catch (Exception e) {
            logger.error("Exception: ", e);
        }        tablestr = tablebufer.toString();
        return tablestr;
    }

    public String getMembersList(String dimId, String grpId) {

        ArrayList list = new ArrayList();
        StringBuffer memberbufer = new StringBuffer();
        String memberstr = null;

        try {

            memberbufer.append("<li class='closed'><span class='folder'><font size='1px' face='verdana'>&nbsp;Members</font></span>");
            memberbufer.append("<ul>");

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
                if (!(memName1.equals(""))) {
                    memberbufer.append("<li class='closed' ><img src='images/treeViewImages/database_table.png'><span ><font size='1px' face='verdana'>&nbsp;" + memName1 + "</font></span>");
                    memberbufer.append("</li>");
                }
            }
            memberbufer.append("</ul></li>");
        } 
        catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }catch (Exception e) {
            logger.error("Exception: ", e);
        }        memberstr = memberbufer.toString();
        return memberstr;
    }

    public String getHeirarchyList(String dimId, String grpId) {

        ArrayList list = new ArrayList();
        StringBuffer hierarcybufr = new StringBuffer();
        String hierarcystr = null;

        try {
            String finalQuery = null;
            String sql = getResourceBundle().getString("getBusinessGroupDimHieList");

            hierarcybufr.append("<li class='closed'><span class='folder' ><font size='1px' face='verdana'>&nbsp;Hierarchies</font></span>");
            hierarcybufr.append("<ul>");

            Object obj[] = new Object[1];
            obj[0] = dimId;
            finalQuery = buildQuery(sql, obj);
            ////.println("getBusinessGroupDimHieList\t" + finalQuery);
            PbReturnObject allHierachyObject = execSelectSQL(finalQuery);
            String[] tableNames = null;
            tableNames = allHierachyObject.getColumnNames();
            String[] relId = new String[allHierachyObject.getRowCount()];
            String[] relName = new String[allHierachyObject.getRowCount()];
            for (int i = 0; i < allHierachyObject.getRowCount(); i++) {

                relId[i] = String.valueOf(allHierachyObject.getFieldValueInt(i, tableNames[0]));
                relName[i] = allHierachyObject.getFieldValueString(i, tableNames[1]);
                //  //////////.println("REL name==" + relName[i] + " REL id===" + relId[i]);
            }
            String[] colNames = null;
            String colName = "";
            String relName1 = "";
            String relId1 = "";
            String sqlcolumns = getResourceBundle().getString("getBusinessGroupDimHieColList");
            String clsname = "closed";
            for (int i = 0; i < relId.length; i++) {
                relName1 = relName[i];
                relId1 = relId[i];
                Object obj1[] = new Object[1];
                obj1[0] = relId[i];
                finalQuery = buildQuery(sqlcolumns, obj1);
                ////.println("getBusinessGroupDimHieColList hirrrr\t" + finalQuery);
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
                if (relName1.equals("")) {
                    hierarcybufr.append(" <li  class='closed'><img src='images/dim.png'><span id='heirarchy'><font size='1px' face='verdana'>&nbsp;" + relName1 + "</font></span>");
                    hierarcybufr.append("<ul >");
                    hierarcybufr.append("<li  class='closed'>");
                }
                for (int j = 0; j < pbReturnObject.getRowCount(); j++) {
                    colName = pbReturnObject.getFieldValueString(j, colNames[1]);
                    columnId = String.valueOf(pbReturnObject.getFieldValueInt(j, colNames[0])) + "," + relId;
                    //      //////////.println("rel col namee-->" + colName);
                    if (!(colName.equals(""))) {
                        hierarcybufr.append("<span><font size='1px' face='verdana'>&nbsp;" + colName + "</font></span>");
                        if (j + 1 != pbReturnObject.getRowCount()) {
                            hierarcybufr.append("<ul><li class='closed'>");
                        }
                    }
                    BusinessGrpsTreeTable secTable = new BusinessGrpsTreeTable();
                    secTable.setEndColumn("true");

                    if (j + 1 == pbReturnObject.getRowCount()) {
                        secTable.setEndTable("true");
                        secTable.setEndColumn("false");
                        hierarcybufr.append(" </li></ul>");
//                 //////////.println(""+j);
                    }
                    if (j + 1 == pbReturnObject.getRowCount()) {
                        for (int endli = pbReturnObject.getRowCount(); endli > 1; endli--) {
                            hierarcybufr.append(" </li></ul></li>");
                        }
                    }
                    secTable.setDimRelColumnName(colName);
                    secTable.setDimRelColumnId(columnId);
                    list.add(secTable);
                }
            }
            hierarcybufr.append("</ul></li>");
        } 
        catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }catch (Exception e) {
            logger.error("Exception: ", e);
        }        hierarcystr = hierarcybufr.toString();

        // ////.println("size of hierachies--->"+hierarcystr);
        return hierarcystr;
    }

    public String getAllTables(String grpId) {
        ArrayList list = new ArrayList();
        StringBuffer alltablesbfr = new StringBuffer();
        String alltablesstr = null;
        try {
            String finalQuery = null;
            String sql = getResourceBundle().getString("getBusinessGroupAllTablesList");

            Object obj[] = new Object[1];
            obj[0] = grpId;
            finalQuery = buildQuery(sql, obj);
            ////.println("getBusinessGroupAllTablesList\t" + finalQuery);
            PbReturnObject allTableObject = execSelectSQL(finalQuery);
            String[] tableNames = null;
            tableNames = allTableObject.getColumnNames();
            String[] tabId = new String[allTableObject.getRowCount()];
            String[] tabName = new String[allTableObject.getRowCount()];
            String[] dbtabId = new String[allTableObject.getRowCount()];
            String[] tabtooltipName = new String[allTableObject.getRowCount()];
            for (int i = 0; i < allTableObject.getRowCount(); i++) {
                dbtabId[i] = String.valueOf(allTableObject.getFieldValueInt(i, tableNames[2]));
                tabId[i] = String.valueOf(allTableObject.getFieldValueInt(i, tableNames[0]));
                tabName[i] = allTableObject.getFieldValueString(i, tableNames[1]);
                tabtooltipName[i] = allTableObject.getFieldValueString(i, tableNames[3]);
            }
            String[] colNames = null;
            String colName = "";
            String tabName1 = "";
            String tabId1 = "";
            String tabtooltipName1 = "";
            String sqlcolumns = getResourceBundle().getString("getBusinessGroupAllTablesColList");
            for (int i = 0; i < tabId.length; i++) {
                tabName1 = tabName[i];
                tabId1 = tabId[i];
                tabtooltipName1 = tabtooltipName[i];
                Object obj1[] = new Object[1];
                obj1[0] = tabId[i];
                finalQuery = buildQuery(sqlcolumns, obj1);
                ////.println("getBusinessGroupAllTablesColList\t" + finalQuery);
                PbReturnObject pbReturnObject = execSelectSQL(finalQuery);
                BusinessGrpsTreeTable table = new BusinessGrpsTreeTable();
                table.setAllTableName(tabName1);
                table.setAllTableId(tabId1 + "," + dbtabId[i] + "," + grpId);
                table.setActTabId(dbtabId[i] + "-" + grpId);
                table.setBussTblId(tabId1);
                list.add(table);


                if (!(tabName1.equals(""))) {
                    alltablesbfr.append("<li onclick='getbusTableId(this)' id=" + tabId1 + "," + dbtabId[i] + "," + grpId + "   class='closed'><img src='images/treeViewImages/database_table.png'>");
                    alltablesbfr.append("<span class='openmenubusgrp' title='" + tabtooltipName1 + "'>" + tabName1 + "</span>");
                    alltablesbfr.append("<ul>");
                    alltablesbfr.append("<li id='dbTabId-" + dbtabId[i] + "-" + grpId + "' class='closed'>");
                    alltablesbfr.append("<span id='bussTb-" + tabId1 + "' class='addNewRelTabs'>Source</span>");
                    alltablesbfr.append("<ul id='src-" + dbtabId[i] + "-" + grpId + "'>");
                }
                /*
                 * <li onclick="getbusTableId(this)" id="<bean:write
                 * name="allTablesList"
                 * property="allTableId"/>"class="closed"><img
                 * src="images/treeViewImages/database_table.png"> <span
                 * class="openmenubusgrp"><bean:write name="allTablesList"
                 * property="allTableName"/></span> <ul> <li
                 * id='dbTabId-<bean:write name="allTablesList"
                 * property="actTabId"/>' class="closed"> <span
                 * id='bussTb-<bean:write name="allTablesList"
                 * property="bussTblId"/>' class="addNewRelTabs">Source</span>
                 * <ul id='src-<bean:write name="allTablesList"
                 * property="actTabId"/>'>
                 */
                colNames = pbReturnObject.getColumnNames();
                String columnId = "";
                String colType = "";
                String tableName = "";
                ArrayList newSrcList = new ArrayList();
                int tableId = 0;


                if (i == allTableObject.getRowCount() - 1) {
                    ////////.println("");
                }
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
                    // //////////.println("table col id in dims-->" + columnId);
                    BusinessGrpsTreeTable secTable = new BusinessGrpsTreeTable();


                    int tempTblId = 0;
                    tempTblId = pbReturnObject.getFieldValueInt(j, 4);
                    if (tableId == 0 || tempTblId != tableId) {
                        if (tableId == 0) {
                            secTable.setStartTbl("first");
                            alltablesbfr.append("<li><span>" + tabName1 + "</span><ul>");
                        } else {
                            secTable.setStartTbl("next");
                            alltablesbfr.append("</ul></li><li><span>&nbsp;" + tabName1 + "</span><ul>");
                        }

                        tableId = tempTblId;
                        secTable.setAllTableName(tableName);
//                        secTable.setAllTableId("" + tableId);
                        secTable.setAllTableId(String.valueOf(tableId));
                    }

                    secTable.setAllTableColName(colName);
                    secTable.setAllTableColId(columnId);
                    secTable.setAllTableColType(colType);
                    if (!(colName.equals(""))) {
                        alltablesbfr.append("<li><span>" + colType + "&nbsp;" + colName + "</span></li>");
                    }


                    //susheela start 10-12-09
                    secTable.setPreTableColType(preTabColType);

                    if (j + 1 == pbReturnObject.getRowCount()) {
                        secTable.setEndTable("true");

                    }
                    alltablesbfr.append("</ul></li>");
                    newSrcList.add(secTable);

                }
                BusinessGrpsTreeTable tempTable = new BusinessGrpsTreeTable();
                tempTable.setEndTable("true");
                alltablesbfr.append("</ul></li>");
                alltablesbfr.append("</ul></li>");
                tempTable.setAllSrcTableList(newSrcList);
                list.add(tempTable);
            }
        } 
        catch (SQLException ex) {
            logger.error("Exception: ", ex);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }       alltablesstr = alltablesbfr.toString();
//        return list;
        return alltablesstr;
    }

    public String getFacts(String grpId) {
        ArrayList list = new ArrayList();
        StringBuffer factsbfer = new StringBuffer();
        String factsstr = null;
        String sql = getResourceBundle().getString("getBusinessGroupFactsList");
        String sql1 = getResourceBundle().getString("getBusinessGroupFactsformulaList");
        String sqlcolumns = getResourceBundle().getString("getBusinessGroupFactsColList");
        String sqlcolumns1 = getResourceBundle().getString("getBusinessGroupFactsformulaColList");
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            sql = getResourceBundle().getString("getBusinessGroupFactsList1");
            sql1 = getResourceBundle().getString("getBusinessGroupFactsformulaList1");

        }
        try {
            String finalQuery = null;

            Object obj[] = new Object[2];
            obj[0] = grpId;
            obj[1] = grpId;
            finalQuery = buildQuery(sql, obj);

            ////.println("getBusinessGroupFactsLists---" + finalQuery);
            PbReturnObject allTableObject = execSelectSQL(finalQuery);
            String[] tableNames = null;
            tableNames = allTableObject.getColumnNames();
            String[] tabId = new String[allTableObject.getRowCount()];
            String[] tabName = new String[allTableObject.getRowCount()];
            String[] dbtabId = new String[allTableObject.getRowCount()];
            String[] tabtooltipname = new String[allTableObject.getRowCount()];

            Object obj2[] = new Object[2];
            obj2[0] = grpId;
            obj2[1] = grpId;
            finalQuery = buildQuery(sql1, obj2);
            ////.println("getBusinessGroupFactsformulaList" + finalQuery);
            PbReturnObject allTableObjectformula = execSelectSQL(finalQuery);


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
                //////.println("finalQuery1--"+finalQuery1);
                pbReturnObjectformula = execSelectSQL(finalQuery1);
                colNamesformula = pbReturnObjectformula.getColumnNames();
            }

            for (int i = 0; i < allTableObject.getRowCount(); i++) {
                dbtabId[i] = String.valueOf(allTableObject.getFieldValueInt(i, tableNames[2]));
                tabId[i] = String.valueOf(allTableObject.getFieldValueInt(i, tableNames[0]));
                tabName[i] = allTableObject.getFieldValueString(i, tableNames[1]);
                tabtooltipname[i] = allTableObject.getFieldValueString(i, tableNames[3]);
                //////.println("table name==" + tabName[i] + "  table id===" + tabId[i]);
            }



            String[] colNames = null;

            String colName = "";
            String tabName1 = "";
            String tabId1 = "";
            String formula = "";
            String tabtooltipName1 = "";

            for (int i = 0; i < tabId.length; i++) {
                tabName1 = tabName[i];
                tabId1 = tabId[i];
                tabtooltipName1 = tabtooltipname[i];
                Object obj1[] = new Object[1];
                obj1[0] = tabId[i];

                finalQuery = buildQuery(sqlcolumns, obj1);
                PbReturnObject pbReturnObject = execSelectSQL(finalQuery);

                BusinessGrpsTreeTable table = new BusinessGrpsTreeTable();
                table.setFactTableName(tabName1);
                table.setFactTableId(tabId1 + "," + dbtabId[i] + "," + grpId);
                list.add(table);
                colNames = pbReturnObject.getColumnNames();
                String columnId = "";
                // addedby susheela start
                String roleFlag = "";
                // added by susheela over

                if (!(tabName1.equals(""))) {
                    factsbfer.append("<li class='closed' id=" + tabId1 + "," + dbtabId[i] + "," + grpId + "><img src='images/treeViewImages/database_table.png'><span class='tableProperty' title='" + tabtooltipName1 + "'>&nbsp;" + tabName1 + "</span>");
                    factsbfer.append("<ul>");
                }
                //added by bharathi for display formulas


                String colType;
                //  .println("pbReturnObject.getRowCount()--"+pbReturnObject.getRowCount());
                BusinessGrpsTreeTable secTablefinal = new BusinessGrpsTreeTable();
                // String viewFormulaClass = "";
                if (pbReturnObject.getRowCount() == 0) {
                    factsbfer.append("</li></ul>");
                }
                for (int j = 0; j < pbReturnObject.getRowCount(); j++) {
                    //  viewFormulaClass = "";
                    colName = pbReturnObject.getFieldValueString(j, colNames[1]);
                    columnId = String.valueOf(pbReturnObject.getFieldValueInt(j, colNames[0])) + "," + String.valueOf(pbReturnObject.getFieldValueInt(j, colNames[2]) + "," + dbtabId[i] + "," + grpId + "~" + tabName1 + "~" + tabId1);
                    colType = pbReturnObject.getFieldValueString(j, colNames[3]).toUpperCase();
                    formula = pbReturnObject.getFieldValueString(j, colNames[5]).toUpperCase();
                    //if (!formula.equalsIgnoreCase("")) {

                    //     viewFormulaClass = "formulaViewMenu";
                    //}else{
                    //viewFormulaClass = "";
                    // }

                    roleFlag = pbReturnObject.getFieldValueString(j, colNames[4]).toUpperCase();
                    if (roleFlag.equalsIgnoreCase("Y")) {
                        roleFlag = "checked";
                    }
                    // added by susheela over



                    //susheela 10-12-09 start
                    String preCol = "";
                    String colNamenew = colName;
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        if (colType.equalsIgnoreCase("DATETIME")) {
                            preCol = "D";//datetime
                        } else if (colType.equalsIgnoreCase("NUMBER")
                                || colType.equalsIgnoreCase("NUMERIC")
                                || colType.equalsIgnoreCase("FLOAT")
                                || colType.equalsIgnoreCase("DOUBLE")
                                || colType.equalsIgnoreCase("DECIMAL")
                                || colType.equalsIgnoreCase("BIGINT")
                                || colType.equalsIgnoreCase("INTEGER")
                                || colType.equalsIgnoreCase("INT")) {
                            preCol = "N";
                        } else if (colType.equalsIgnoreCase("nvarchar")) {
                            preCol = "T";
                        } else {
//                             colName = colNamenew.replaceAll("_", " ");
                        }

                    } else {
                        if (colType.equalsIgnoreCase("Date")) {
                            preCol = "D";
                        } else if (colType.equalsIgnoreCase("NUMBER")) {
                            preCol = "N";
                        } else if (colType.equalsIgnoreCase("VARCHAR2")) {
                            preCol = "T";
                        } else {
                            colName = colNamenew.replaceAll("_", " ");
                        }
                    }

                    if (!(colName.equals(""))) {
                        factsbfer.append("<li id=" + columnId + "~" + colName + "~" + colType + " class='closed'>" + preCol);

                        if (colType.equals("NUMBER")) {
                            factsbfer.append("<span class='bucketMenu'  title='" + formula + "'>&nbsp;" + colName + "</span>");
                        }
                        if (!(colType.equals("NUMBER"))) {
                            factsbfer.append("<span class='bucketMenu'  title='" + formula + "'>&nbsp;" + colName + "</span>");
                        }
                        factsbfer.append("</li>");
                    }
                    BusinessGrpsTreeTable secTable = new BusinessGrpsTreeTable();
                    if (j + 1 == pbReturnObject.getRowCount() && !(allTableObjectformula.getRowCount() > 0)) {
                      
                        secTablefinal.setEndTable("true");
                        factsbfer.append("</ul></li>");

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

                StringBuilder colList = new StringBuilder();
//                String colList = "";
                if (pbReturnObjectformula != null) {
                    //  .println("pbReturnObjectformula.getRowCount()--"+pbReturnObjectformula.getRowCount());
                    for (int j = 0; j < pbReturnObjectformula.getRowCount(); j++) {

                        colName = pbReturnObjectformula.getFieldValueString(j, colNamesformula[1]);
                        columnId = String.valueOf(pbReturnObjectformula.getFieldValueInt(j, colNamesformula[0])) + "," + String.valueOf(pbReturnObjectformula.getFieldValueInt(j, colNamesformula[2]) + "," + dbtabIdformula + "," + grpId + "~" + tabNameformula1 + "~" + tabIdformula1);
                        colType = pbReturnObjectformula.getFieldValueString(j, colNamesformula[3]).toUpperCase();
                        roleFlag = pbReturnObjectformula.getFieldValueString(j, colNamesformula[4]).toUpperCase();
                        refElements = pbReturnObjectformula.getFieldValueString(j, "REFFERED_ELEMENTS").toUpperCase();
                        formula = pbReturnObjectformula.getFieldValueString(j, "DISPLAY_FORMULA").toUpperCase();


                        // viewFormulaClass = "formulaViewMenu";

                        if (!refElements.equalsIgnoreCase("")) {
                            String refEleList[] = refElements.split(",");
                            for (int j1 = 0; j1 < refEleList.length; j1++) {
                                if (refEleList[j1].startsWith("M-")) {
                                    String detlist[] = refEleList[j1].substring(2).split("-");
                                    String bussColId = detlist[0];
                                    String memId = detlist[2];
//                                    colList += "," + bussColId;
                                    colList.append(  ",").append(bussColId);
                                } else {
                                     String detlist[] = refEleList[j1].split("-");
                                    String bussColId = detlist[0];
//                                    colList += "," + bussColId;
                                    colList.append( "," ).append( bussColId);
                                }
                            }
                            if (colList!=null && colList.length()>0) {
                                colList =new StringBuilder( colList.substring(1));

                            }
                        }

//                        if (!colList.equalsIgnoreCase("")) {
                        if (colList!=null && colList.length()>0) {
                            String testQuery = "SELECT distinct BUSS_TABLE_ID FROM PRG_GRP_BUSS_TABLE_details where BUSS_COLUMN_ID in(" + colList + ") and BUSS_TABLE_ID=" + tabId1;
                            PbReturnObject testpbro = execSelectSQL(testQuery);
                            if (testpbro.getRowCount() > 0) {
                                if (roleFlag.equalsIgnoreCase("Y")) {
                                    roleFlag = "checked";
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
                                if (!(colName.equals(""))) {
                                    factsbfer.append("<li id=" + columnId + "~" + colName + "~" + colType + " class='closed'>" + preCol);

                                    if (colType.equals("NUMBER")) {
                                        factsbfer.append("<span class='bucketMenu' title='" + formula + "'>&nbsp;" + colName + "</span>");
                                    }
                                    if (!(colType.equals("NUMBER"))) {
                                        factsbfer.append("<span class='bucketMenu'  title='" + formula + "'>&nbsp;" + colName + "</span>");
                                    }
                                    factsbfer.append("</li>");
                                }
                                BusinessGrpsTreeTable secTable = new BusinessGrpsTreeTable();
                                if (j + 1 == pbReturnObjectformula.getRowCount()) {
                                    factsbfer.append("</ul></li>");
                                    secTable.setEndTable("true");

                                }

                                secTable.setFactTableColName(colName);
                                secTable.setFactTableColId(columnId);
                                secTable.setFactColType(colType);
                                //susheela 10-12-09
                                secTable.setPreColFactType(preCol);
                                secTable.setRoleFlag(roleFlag);

                                list.add(secTable);
                            } else {
                                 secTablefinal.setEndTable("true");
                                factsbfer.append("</ul></li>");
                                list.add(secTablefinal);
                            }
                        }

                    }
                }


            }
            factsstr = factsbfer.toString();
        } catch (SQLException e) {
            logger.error("Exception: ", e);
        }
//        return list;
        return factsstr;
    }

    public String getBusinessRoles(String grpId) {
        ArrayList list = new ArrayList();
        StringBuffer bussrolesbfr = new StringBuffer();
        String bussrolestr = null;
        try {
            String finalQuery = null;
            String sql = getResourceBundle().getString("getBusinessRolesList");

            Object obj[] = new Object[1];
            obj[0] = grpId;

            finalQuery = buildQuery(sql, obj);
            ////.println("business Roles --->"+finalQuery);
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
                ////.println(" fingetExtraDimsForRole " + fingetExtraDimsForRole);
                ////.println(" fingetExtraBussTable " + fingetExtraBussTable);
                PbReturnObject extraDimObj = execSelectSQL(fingetExtraDimsForRole);
                PbReturnObject extraBussTabObj = execSelectSQL(fingetExtraBussTable);
                if (extraDimObj.getRowCount() > 0 || extraBussTabObj.getRowCount() > 0) {
                    roleColor = "!=";
                }
                //////////.println(roleName[i]+" roleColor "+roleColor);
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
                if (!(roleName[i]).equals("")) {
                    bussrolesbfr.append("<li class='closed'><img src='images/treeViewImages/folder-closed.gif'><span>" + roleColor + "&nbsp;" + roleName[i] + "</span></li>");
                }
            }
        } 
        catch (SQLException ex) {
            logger.error("Exception: ", ex);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }       bussrolestr = bussrolesbfr.toString();
        return bussrolestr;
//        return list;
    }

    public String getBuckets(String grpId) {
        Connection con = null;
        StringBuffer bucktsbufer = new StringBuffer();
        String bucktstr = null;
        ArrayList list = new ArrayList();

        try {
            String finalQuery = null;
            String sqlc = getResourceBundle().getString("getBucketConnection");
            Object objc[] = new Object[1];
            objc[0] = grpId;
            finalQuery = buildQuery(sqlc, objc);
            // ////.println("bucket query--"+finalQuery);
            PbReturnObject allBussTablesIds = execSelectSQL(finalQuery);
            String[] bussTableNames = null;
            bussTableNames = allBussTablesIds.getColumnNames();
            ////.println("final query in first bucket==" + finalQuery);
            for (int c = 0; c < allBussTablesIds.getRowCount(); c++) {
                if (c == 0) {
                    BusinessGroupDAO b = new BusinessGroupDAO();
                    con = b.getConnectionIdConnection(String.valueOf(allBussTablesIds.getFieldValueInt(c, bussTableNames[0])));
                    String sql = getResourceBundle().getString("getBucketList");
                    //  ////////.println("grpId==="+grpId);
                    Object obj[] = new Object[1];
                    obj[0] = grpId;
                    finalQuery = buildQuery(sql, obj);
                    Statement st = con.createStatement();
                    PbReturnObject allTableObject = new PbReturnObject(st.executeQuery(finalQuery));
                    //////////.println("------in if----->"+finalQuery);
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

                        if (!(tabName1.equals(""))) {
                            bucktsbufer.append("<li class='closed'><img src='images/beaker-empty.png'><span>&nbsp;" + tabName1 + "</span>");
                            bucktsbufer.append("<ul>");
                        }


                        finalQuery = buildQuery(sqlcolumns, obj1);
                        ////////.println("final bucket query---->"+finalQuery);
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
                            // ////////.println("table col id in dims-->" + tabNamecol);
                            if (!(colName.equals(""))) {
                                bucktsbufer.append("<li class='closed'>&nbsp;" + colName);
                                bucktsbufer.append("</li>");
                            }

                            BusinessGrpsTreeTable secTable = new BusinessGrpsTreeTable();

                            if (j + 1 == pbReturnObject.getRowCount()) {
                                secTable.setEndTable("true");
                                bucktsbufer.append(" </ul></li>");
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
                        //  ////////.println("grpId==="+grpId);
                        Object obj[] = new Object[1];
                        obj[0] = grpId;
                        finalQuery = buildQuery(sql, obj);
                        Statement st = con.createStatement();
                        PbReturnObject allTableObject = new PbReturnObject(st.executeQuery(finalQuery));
                        //////////.println("-------in else---->"+finalQuery);
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

                            if (!(tabName1.equals(""))) {
                                bucktsbufer.append("<li class='closed'><img src='images/beaker-empty.png'><span>&nbsp;" + tabName1 + "</span>");
                                bucktsbufer.append("<ul>");
                            }


                            finalQuery = buildQuery(sqlcolumns, obj1);
                            //  ////////.println("final bucket query---->"+finalQuery);
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
                                //  ////////.println("table col id in dims-->" + tabNamecol);

                                if (!(colName.equals(""))) {
                                    bucktsbufer.append("<li class='closed'>&nbsp;" + colName);
                                    bucktsbufer.append("</li>");
                                }

                                BusinessGrpsTreeTable secTable = new BusinessGrpsTreeTable();
                                if (j + 1 == pbReturnObject.getRowCount()) {
                                    secTable.setEndTable("true");
                                    bucktsbufer.append(" </ul></li>");

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
            logger.error("Exception: ", ex);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }       // //////////.println("size of fzcts--->"+list.size());
//        return list;
        bucktstr = bucktsbufer.toString();
        return bucktstr;
    }

    public PbReturnObject getBucketEditDetails(String groupId) {
        String querybucketDetails = getResourceBundle().getString("querybucketDetails");
        Connection con = null;
        String finalQuery = "";
        Object[] objParms = new Object[1];
        objParms[0] = groupId;
        PbReturnObject pbReturnObject = new PbReturnObject();
        try {
            con = ProgenConnection.getInstance().getConnectionByGroupId(groupId);
            finalQuery = buildQuery(querybucketDetails, objParms);
            pbReturnObject = execSelectSQL(finalQuery, con);

        } catch (Exception e) {

            logger.error("Exception: ", e);

        } finally {
            if (con != null) {
                con = null;
            }
        }
        return pbReturnObject;
    }

    public String getBucketChildDetails(String grpId, String bktId, String bussColId) throws Exception {
        Connection con = null;
        String getBucketChildDetailsQry = getResourceBundle().getString("getBucketChildDetailsQry");
        String getElementId = "SELECT ELEMENT_ID,AVG_MIN_MAX_DESC FROM PRG_GRP_BUCKET_MASTER WHERE BUCKET_ID = " + bktId;
        String selectDbTableID = getResourceBundle().getString("selectDbTableID");
        String selMaxAndMinValues = getResourceBundle().getString("selMaxAndMinValues");
        String finalQuery = "";
        String eleColType = "";
        String actualColFormula = "";
        String dispName = "";
        String bussColName = "";
        DateFormat dateFormat = null;
        Calendar cal = null;
        String sys_date = null;
        String buckAvgMinMaxDetails = "";

        PbReturnObject getBucketChildDetailsQryEleObj = null;
        PbReturnObject getEleObj = null;



//        if(bussColId==null || bussColId.equalsIgnoreCase("") || bussColId==" " || bussColId.equalsIgnoreCase("0")){
        try {
            con = ProgenConnection.getInstance().getConnectionByGroupId(grpId);
            getEleObj = execSelectSQL(getElementId, con);
            if (getEleObj.rowCount > 0) {
                String elementId = getEleObj.getFieldValueString(0, 0);
                buckAvgMinMaxDetails = getEleObj.getFieldValueString(0, 1);
                if (elementId != null && !elementId.equalsIgnoreCase("")) {
                    String getBucketChildDetailsQryEle = "SELECT BUSS_COL_ID,USER_COL_TYPE,ACTUAL_COL_FORMULA,DISP_NAME,BUSS_COL_NAME FROM PRG_USER_ALL_INFO_DETAILS WHERE ELEMENT_ID = " + elementId;
                    getBucketChildDetailsQryEleObj = execSelectSQL(getBucketChildDetailsQryEle);
                    if (getBucketChildDetailsQryEleObj.getRowCount() > 0) {
                        bussColId = getBucketChildDetailsQryEleObj.getFieldValueString(0, 0);
                        eleColType = getBucketChildDetailsQryEleObj.getFieldValueString(0, 1);
                        actualColFormula = getBucketChildDetailsQryEleObj.getFieldValueString(0, 2);
                        dispName = getBucketChildDetailsQryEleObj.getFieldValueString(0, 3);
                        bussColName = getBucketChildDetailsQryEleObj.getFieldValueString(0, 4);
                    }
                }
            }

        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
//        }

        Object[] objectsCusIDl = new Object[2];
        objectsCusIDl[0] = grpId;
        objectsCusIDl[1] = bussColId;

        Object[] objParms = new Object[1];
        objParms[0] = bktId;
        PbReturnObject pbReturnObject = new PbReturnObject();
        StringBuffer stringBuffer = null;
        PbReturnObject selValOfMaxAndMin = null;
        String maxNo = "0";
        String minN0 = "0";
        String avgVal = "0";
        String tempMeasureName = "";
        String measureName = "";
        String cusTableName = "";
        Object[] maxAmndMinObj = new Object[4];
        PbReturnObject pbreMInobject = null;
        try {
            finalQuery = buildQuery(selectDbTableID, objectsCusIDl);
            selValOfMaxAndMin = execSelectSQL(finalQuery);
            cusTableName = selValOfMaxAndMin.getFieldValueString(0, 2);
            measureName = selValOfMaxAndMin.getFieldValueString(0, 1);
            if (eleColType.equalsIgnoreCase("calculated")) {
                measureName = actualColFormula;
                cusTableName = dispName;
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
                    dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    Date date = new Date();
                    sys_date = dateFormat.format(date);
                    measureName = measureName.replace("@PROGENTIME@@ST_DATE", "to_date('" + sys_date + "','mm/dd/yyyy Hh24:mi:ss ')");
                    measureName = measureName.replace("@PROGENTIME@@ED_DATE", "to_date('" + sys_date + "','mm/dd/yyyy Hh24:mi:ss ')");
                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    dateFormat = new SimpleDateFormat("%m/%d/%Y");
                    Date date = new Date();
                    sys_date = dateFormat.format(date);
                    measureName = measureName.replace("@PROGENTIME@@ST_DATE", "str_to_date('" + sys_date + "','%m/%d/%Y %H:%i:%s ')");
                    measureName = measureName.replace("@PROGENTIME@@ED_DATE", "str_to_date('" + sys_date + "','%m/%d/%Y %H:%i:%s ')");
                } else {
                    dateFormat = new SimpleDateFormat("mm/dd/yyyy");
                    Date date = new Date();
                    sys_date = dateFormat.format(date);
                    measureName = measureName.replace("@PROGENTIME@@ST_DATE", "convert(datetime,'" + sys_date + "',120)");
                    measureName = measureName.replace("@PROGENTIME@@ED_DATE", "convert(datetime,'" + sys_date + "',120)");
                }
            }

            con = ProgenConnection.getInstance().getConnectionByGroupId(grpId);
            finalQuery = buildQuery(getBucketChildDetailsQry, objParms);
            pbReturnObject = execSelectSQL(finalQuery, con);
            // con.close();
            maxAmndMinObj[0] = measureName;
            maxAmndMinObj[1] = measureName;
            maxAmndMinObj[2] = measureName;
            maxAmndMinObj[3] = cusTableName;
            finalQuery = buildQuery(selMaxAndMinValues, maxAmndMinObj);
            con = ProgenConnection.getInstance().getConnectionByGroupId(grpId);
            pbreMInobject = execSelectSQL(finalQuery, con);
            //con.close();
//            maxNo = pbreMInobject.getFieldValueInt(0, "COL_MAXMUM");
//            minN0 = pbreMInobject.getFieldValueInt(0, "COL_MINMUM");
//            avgVal = (float) pbreMInobject.getFieldValueInt(0, "COL_AVG");
            if (buckAvgMinMaxDetails == "" || buckAvgMinMaxDetails == " ") {
                maxNo = pbreMInobject.getFieldValueString(0, "COL_MAXMUM");
                minN0 = pbreMInobject.getFieldValueString(0, "COL_MINMUM");
                avgVal = pbreMInobject.getFieldValueString(0, "COL_AVG");
            } else {
                String[] buckAvgMinMaxDetailsArr = buckAvgMinMaxDetails.split(",");
                maxNo = buckAvgMinMaxDetailsArr[2];
                minN0 = buckAvgMinMaxDetailsArr[1];
                avgVal = buckAvgMinMaxDetailsArr[0];
            }
            if (eleColType.equalsIgnoreCase("calculated")) {
                measureName = bussColName;
            }
            stringBuffer = new StringBuffer();

//            stringBuffer.append(" <table align='center'><tr><td  > <label class='label' >Number of Buckets</label> </td>  <td> " +
//                    " <input type='text' name='number'  id='number' size='2' maxlength='2' autocomplete='off' onkeyup='addRows('addTable'),checkNumber(this)' id='number' value='"+ pbReturnObject.getRowCount()+"' > </td> </tr></table> <br><br>");
//            stringBuffer.append(pbReturnObject.getRowCount() + "-" + maxNo + "-" + minN0 + "-" + measureName + "-" + avgVal + "~");
            stringBuffer.append(pbReturnObject.getRowCount() + "::" + maxNo + "::" + minN0 + "::" + measureName + "::" + avgVal + "~");
            if (pbReturnObject.getRowCount() != 0) {
                stringBuffer.append("<br> <tr>"
                        + "<th  style='background-color:#b4d9ee;font-size: small'>Display Value</th>"
                        + " <th  style='background-color:#b4d9ee;font-size: small' >Start Limit</th><th  style='background-color:#b4d9ee;font-size: small'>End Limit</th></tr>");

                for (int roI = 0; roI < pbReturnObject.getRowCount(); roI++) {

                    stringBuffer.append("<tr> <td><input type='text' name='inputValue" + roI + "' id='inputValue" + roI + "'  value='" + pbReturnObject.getFieldValueString(roI, "BUCKET_DISP_VALUE") + "'></td> ");
                    if (roI == 0) {
//                        stringBuffer.append("<td><input type='text' name='inputStartlimt" + roI + "' id='inputStartlimt" + roI + "'  value='" + minN0 + "'  onkeypress='return isNumberKey(event)' onblur='return checkMinValaue(this)' ></td> ");
                        stringBuffer.append("<td><input type='text' name='inputStartlimt" + roI + "' id='inputStartlimt" + roI + "'  value='" + pbReturnObject.getFieldValueString(roI, "START_LIMIT") + "'  onkeypress='return isNumberKey(event)' onblur='return checkMinValaue(this)' ></td> ");
                    } else {
                        stringBuffer.append("<td><input type='text' name='inputStartlimt" + roI + "' id='inputStartlimt" + roI + "'  value='" + pbReturnObject.getFieldValueString(roI, "START_LIMIT") + "' readonly></td> ");
                    }
                    stringBuffer.append("<td><input type='text' name='inputEndlimt" + roI + "' id='inputEndlimt" + roI + "'  value='" + pbReturnObject.getFieldValueString(roI, "END_LIMIT") + "' onkeypress='return isNumberKey(event)' onblur=' displayStariLimt(this)' > </td></tr>");


                }
            }
//           stringBuffer.append("</table>");



        } catch (Exception ex) {
            try {
                logger.error("Exception: ", ex);
                con.close();
            } catch (SQLException ex1) {
                logger.error("Exception: ", ex1);
            }
        }
        return stringBuffer.toString();
    }

    public void updateBucketDetails(ArrayList paramlistAl) {
        ArrayList queryList = new ArrayList();
//        String bktDetailsQueryS = getResourceBundle().getString("getBucketChildDetailsQry");
        String updatebktDetailsQueryS = getResourceBundle().getString("updatebktDetailsQueryS");
        String deleteBktDetails = getResourceBundle().getString("deleteBktDetails");
        String updateBktMaster = getResourceBundle().getString("updateBktMaster");
        Object[] updateMaster = new Object[4];
        updateMaster[0] = paramlistAl.get(2);
        updateMaster[1] = paramlistAl.get(3);
        updateMaster[2] = paramlistAl.get(7);
        updateMaster[3] = paramlistAl.get(1);
        Object[] deletObj = new Object[1];
        deletObj[0] = paramlistAl.get(1);
        Object[] bktobj = new Object[10];
        String finalQuery = "";
//        PbReturnObject bktDetailsRetobj = null;
        Connection connection = null;
        int rowcount = Integer.parseInt(paramlistAl.get(8).toString());
        String[] displayValue = (String[]) paramlistAl.get(4);
        String[] startLimt = (String[]) paramlistAl.get(5);
        String[] endLimt = (String[]) paramlistAl.get(6);
        try {
            finalQuery = buildQuery(updateBktMaster, updateMaster);
            queryList.add(finalQuery);
            finalQuery = buildQuery(deleteBktDetails, deletObj);
            queryList.add(finalQuery);
            int no = rowcount;
            for (int count = 0; count < rowcount; count++) {
                String tempValue = displayValue[count].trim();
                for (int j = 0; j < no; j++) {
                    tempValue = " " + tempValue;
                }
                no--;
//                bktobj[0] = displayValue[count];
//                bktobj[1] = displayValue[count];
                bktobj[0] = tempValue;
                bktobj[1] = tempValue;
                bktobj[2] = count;
                bktobj[3] = startLimt[count];
                bktobj[4] = endLimt[count];
                bktobj[5] = "null";
                bktobj[6] = "null";
                bktobj[7] = "null";
                bktobj[8] = "null";
                bktobj[9] = paramlistAl.get(1);
                finalQuery = buildQuery(updatebktDetailsQueryS, bktobj);
                queryList.add(finalQuery);
            }
            ////.println("queryList\t" + queryList);
            connection = ProgenConnection.getInstance().getConnectionByGroupId(paramlistAl.get(7).toString());
            executeMultiple(queryList, connection);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }

    }

    public PbReturnObject getMxminValofMesu(String colName, String grpID, String tabName, String eleColType, String elementId) {
        String selMaxAndMinValues = getResourceBundle().getString("selMaxAndMinValues");
        String selActualFormula = "select ACTUAL_COL_FORMULA from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID = " + elementId;
//        String selTabName = "select ACTUAL_COL_FORMULA from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID = "+elementId;
        String finalQuery = "";
        DateFormat dateFormat = null;
//        Calendar cal = null;
        String sys_date = null;

        Object[] maxAmndMinObj = new Object[4];
        Connection connection = null;
        PbReturnObject pbReturnObject = null;
        pbReturnObject = new PbReturnObject();
        try {
            if (eleColType.equalsIgnoreCase("calculated")) {
                connection = ProgenConnection.getInstance().getConnection();
                pbReturnObject = execSelectSQL(selActualFormula, connection);
                if (pbReturnObject.getRowCount() > 0) {
                    colName = pbReturnObject.getFieldValueString(0, 0);
                }
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
                    dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    Date date = new Date();
                    sys_date = dateFormat.format(date);
                    colName = colName.replace("@PROGENTIME@@ST_DATE", "to_date('" + sys_date + "','mm/dd/yyyy Hh24:mi:ss ')");
                    colName = colName.replace("@PROGENTIME@@ED_DATE", "to_date('" + sys_date + "','mm/dd/yyyy Hh24:mi:ss ')");
                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    dateFormat = new SimpleDateFormat("%m/%d/%Y");
                    Date date = new Date();
                    sys_date = dateFormat.format(date);
                    colName = colName.replace("@PROGENTIME@@ST_DATE", "str_to_date('" + sys_date + "','%m/%d/%Y %H:%i:%s ')");
                    colName = colName.replace("@PROGENTIME@@ED_DATE", "str_to_date('" + sys_date + "','%m/%d/%Y %H:%i:%s ')");
                } else {
                    dateFormat = new SimpleDateFormat("mm/dd/yyyy");
                    Date date = new Date();
                    sys_date = dateFormat.format(date);
                    colName = colName.replace("@PROGENTIME@@ST_DATE", "convert(datetime,'" + sys_date + "',120)");
                    colName = colName.replace("@PROGENTIME@@ED_DATE", "convert(datetime,'" + sys_date + "',120)");
                }
            }
            maxAmndMinObj[0] = colName;
            maxAmndMinObj[1] = colName;
            maxAmndMinObj[2] = colName;
            maxAmndMinObj[3] = tabName;
            connection = ProgenConnection.getInstance().getConnectionByGroupId(grpID);
            finalQuery = buildQuery(selMaxAndMinValues, maxAmndMinObj);
            ////.println("finalQuery\t" + finalQuery);
            pbReturnObject = execSelectSQL(finalQuery, connection);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return pbReturnObject;
    }

    public String getBussGrpDetails(String bussTableId) {
        String query = getResourceBundle().getString("getBussGrpDetails");
        Object object[] = new Object[1];
        object[0] = bussTableId;
        PbReturnObject pbReturnObject = new PbReturnObject();
        String finalQuery = "";
        String resultString = "";
        try {
            finalQuery = super.buildQuery(query, object);
            pbReturnObject = super.execSelectSQL(finalQuery);
//           resultString+="{ columnsNames:[";
//            String tempColNames = "[";
            StringBuilder tempColNames = new StringBuilder();
            tempColNames.append( "[");
//            String tempColIds = "[";
            StringBuilder tempColIds =  new StringBuilder(); tempColNames.append( "[");
            for (int i = 0; i < pbReturnObject.getRowCount(); i++) {
//                tempColNames += "\"" + pbReturnObject.getFieldValueString(i, "COLUMN_NAME") + "\"";
                tempColNames.append( "\"").append( pbReturnObject.getFieldValueString(i, "COLUMN_NAME")).append( "\"");
//                tempColIds += "\"" + pbReturnObject.getFieldValueString(i, "BUSS_COLUMN_ID") + "\"";
                tempColIds.append("\"").append( pbReturnObject.getFieldValueString(i, "BUSS_COLUMN_ID") ).append("\"");
                if (i < pbReturnObject.getRowCount() - 1) {
//                    tempColNames += ",";
                    tempColNames.append(",");
//                    tempColIds += ",";
                    tempColIds.append(",");
                }
            }
//            tempColNames += "]";
            tempColNames.append("]");
//            tempColIds += "]";
            tempColIds.append("]");
            resultString = "{bussColNames:" + tempColNames + ",bussColIds:" + tempColIds + "}";
        } catch (SQLException e) {
            resultString = "{bussColNames:[],bussColIds:[]}";
            logger.error("Exception: ", e);
        }
        return resultString;
    }

    public boolean saveAdditionalTimeDim(String bussTabId, String[] buscolNameArray, String bussColuIds) {
        String insertbussTabMaster = getResourceBundle().getString("insertbussTabMaster");
        String insertbussTabdetails = getResourceBundle().getString("insertbussTabdetails");
        String insertBussTabSrc = getResourceBundle().getString("insertBussTabSrc");
        String insertinBussSrcDetails = getResourceBundle().getString("insertinBussSrcDetails");
        String insertBussRelMaster1 = getResourceBundle().getString("insertBussRelMaster1");
        String insertBussRelMaster2 = getResourceBundle().getString("insertBussRelMaster2");
        String insertIntoUserSubFolderTables = getResourceBundle().getString("insertIntoUserSubFolderTables");
        String finalQuery = "";
        Object[] object = null;
        ArrayList queryList = new ArrayList();
        int bussTabseqNo = 0;
        boolean result = false;
        try {
            object = new Object[2];
            bussTabseqNo = super.getCurrentSequenceNumber("SELECT PRG_GRP_BUSS_TABLE_SEQ.NEXTVAL FROM DUAL ");
//            
            object[0] = bussTabseqNo;
            object[1] = bussTabId;
            finalQuery = super.buildQuery(insertbussTabMaster, object);
            queryList.add(finalQuery);
            object = new Object[2];
            object[0] = bussTabseqNo;
            object[1] = bussTabId;
            finalQuery = super.buildQuery(insertBussTabSrc, object);
            queryList.add(finalQuery);
            object = new Object[3];
            object[0] = bussTabseqNo;
            object[1] = bussTabId;
//            String tempStr = "AND COLUMN_NAME NOT IN ( ";
            StringBuilder tempStr =new StringBuilder();
            tempStr.append("AND COLUMN_NAME NOT IN ( ");
            if (buscolNameArray != null) {
                for (int i = 0; i < buscolNameArray.length; i++) {
//                    tempStr += "'" + buscolNameArray[i].trim() + "'";
                    tempStr.append("'").append( buscolNameArray[i].trim()).append("'");
                    if (i < buscolNameArray.length - 1) {
//                        tempStr += ",";
                        tempStr.append( ",");
                    }
                }
//                tempStr += ")";
                tempStr.append( ")");
                object[2] = tempStr;
            }
            finalQuery = super.buildQuery(insertbussTabdetails, object);
            queryList.add(finalQuery);
            object = new Object[3];
            object[0] = bussTabseqNo;
            object[1] = bussTabId;
            object[2] = bussColuIds;
            finalQuery = super.buildQuery(insertinBussSrcDetails, object);
            queryList.add(finalQuery);
            object = new Object[2];
            object[0] = bussTabseqNo;
            object[1] = bussTabId;
            finalQuery = super.buildQuery(insertBussRelMaster1, object);
            queryList.add(finalQuery);
            finalQuery = super.buildQuery(insertBussRelMaster2, object);
            queryList.add(finalQuery);
            finalQuery = super.buildQuery(insertIntoUserSubFolderTables, object);
            queryList.add(finalQuery);
            // 
            result = super.executeMultiple(queryList);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }

        return result;
    }
    //added by nazneen for col type of a measure

    public PbReturnObject getColType(String elementId) throws SQLException {
        String getColTypeQry = "SELECT user_col_type FROM prg_user_all_info_details  where element_id IN (" + elementId + ")";
        Connection conSource = ProgenConnection.getInstance().getConnection();
        PbReturnObject pbReturnObject = new PbReturnObject();
        try {
            pbReturnObject = execSelectSQL(getColTypeQry, conSource);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return pbReturnObject;
    }
    //added by Nazneen to get max,min amd acg value of a measure for segmentation

    public String getMinMaxAvgVal(Container container, String tempBussColName) throws SQLException {
        String minMaxAvgVal = "";
        BigDecimal avg = null;
        BigDecimal min = null;
        BigDecimal max = null;
        if (tempBussColName == null) {
            minMaxAvgVal = "";
        } else {
            if (container.getRetObj().getColumnAverageValue(tempBussColName) != null) {
                avg = container.getRetObj().getColumnAverageValue(tempBussColName);
                avg = avg.setScale(2, RoundingMode.CEILING);
            }
            if (container.getRetObj().getColumnMinimumValue(tempBussColName) != null) {
                min = container.getRetObj().getColumnMinimumValue(tempBussColName);
                min = min.setScale(2, RoundingMode.CEILING);
            }
            if (container.getRetObj().getColumnMaximumValue(tempBussColName) != null) {
                max = container.getRetObj().getColumnMaximumValue(tempBussColName);
                max = max.setScale(2, RoundingMode.CEILING);
            }
            minMaxAvgVal = avg + "," + min + "," + max;
        }
        return minMaxAvgVal;
    }
}
