/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.business.group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author Saurabh
 */
public class TargetMeasureParametersDAO extends PbDb {

    public static Logger logger = Logger.getLogger(TargetMeasureParametersDAO.class);
    ResourceBundle resourceBundle;
    //for SQL Server insertion into PRG_USER_SUB_FOLDER_TABLES MAINTAIN PRIMARY KEY SEPARATE
//    int subFolderTabId;
    //for SQL Server insertion into PRG_USER_SUB_FLDR_ELEMENTS MAINTAIN PRIMARY KEY SEPARATE
//    int subFolderEleId;

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new PbTargetMeasureResourceBundleSqlServer();
            } else {
                resourceBundle = new PbTargetMeasureResourceBundle();
            }
        }

        return resourceBundle;
    }

//    PbTargetMeasureResourceBundle resBundle = new PbTargetMeasureResourceBundle();
    //02-oct-09 added by susheela
    public void recfindTable(String businessTableId, String Path) throws Exception {
        PbReturnObject retObj = null;
        String[] colNames;
        HashMap tableRelationship = new HashMap();

        String sqlstr;
        String finalQuery;
        /*
         * sqlstr = " select table_id from ( "; sqlstr += "select table_id from
         * PRG_DB_TABLE_RLT_MASTER where table_id2 = " + businessTableId; sqlstr
         * += " union select table_id2 from PRG_DB_TABLE_RLT_MASTER where
         * table_id = " + businessTableId; sqlstr += " ) where table_id not in (
         * " +Path +" )";
         */
        sqlstr = "SELECT distinct pgbtrm.buss_table_id FROM PRG_GRP_BUSS_TABLE_RLT_MASTER pgbtrm,prg_grp_buss_table pgbt WHERE "
                + " pgbtrm.buss_table_id2 =" + businessTableId + " and pgbt.buss_type='Table' and pgbt.buss_table_id = pgbtrm.buss_table_id and"
                + "  pgbtrm.buss_table_id NOT IN (" + Path + ") "
                + " UNION SELECT distinct pgbtrm.buss_table_id2 FROM PRG_GRP_BUSS_TABLE_RLT_MASTER pgbtrm,prg_grp_buss_table pgbt "
                + " WHERE pgbtrm.buss_table_id =" + businessTableId + " and pgbt.buss_type='Table' and pgbt.buss_table_id = pgbtrm.buss_table_id2 "
                + " and pgbtrm.buss_table_id NOT IN (" + Path + ")";


        //finalQuery = pbDb.buildQuery(sqlstr, Obj);
        finalQuery = sqlstr;
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("InQuery in param is" + finalQuery);
        retObj = execSelectSQL(finalQuery);

        colNames = retObj.getColumnNames();
        int psize = retObj.getRowCount();
        if (psize > 0) {
            for (int looper = 0; looper < psize; looper++) {
                String p = Path + "," + retObj.getFieldValueString(looper, colNames[0]);
                tableRelationship.put(retObj.getFieldValueString(looper, colNames[0]), Path.substring(Path.indexOf(",") + 1));
                recfindTable(retObj.getFieldValueString(looper, colNames[0]), p);
            }

        }

    }

    public HashMap getpathFromBusinessTable(String businessTableId) throws Exception {
        HashMap tableRelationship = new HashMap();
        /**
         * Get All the tables related with a fact then for each table find the
         * next node keep on doing till you reach last node need to handel
         * multiple node in between
         *
         */
        PbReturnObject retObj = null;
        String[] colNames;

        ArrayList mainTable = new ArrayList();
        ArrayList tempTable = new ArrayList();
        String sqlstr;
        String finalQuery;

        //   sqlstr = "select table_id from PRG_DB_TABLE_RLT_MASTER where table_id2 = " + businessTableId;
        //  sqlstr += " union select table_id2 from PRG_DB_TABLE_RLT_MASTER where table_id = " + businessTableId;
        // sqlstr= "select buss_table_id from PRG_GRP_BUSS_TABLE_RLT_MASTER where buss_table_id2 = "+businessTableId+" union select buss_table_id2 from PRG_GRP_BUSS_TABLE_RLT_MASTER where buss_table_id ="+businessTableId;
        sqlstr = "select distinct pgbtrm.BUSS_TABLE_ID from PRG_GRP_BUSS_TABLE_RLT_MASTER pgbtrm,prg_grp_buss_table pgbt where pgbtrm.BUSS_TABLE_ID2 =" + businessTableId + " "
                + " and pgbt.buss_type='Table' and pgbt.buss_table_id = pgbtrm.buss_table_id  union select pgbtrm.BUSS_TABLE_ID2 from "
                + " PRG_GRP_BUSS_TABLE_RLT_MASTER  pgbtrm,prg_grp_buss_table pgbt where pgbtrm.BUSS_TABLE_ID =" + businessTableId + "  and "
                + " pgbt.buss_type='Table'  and pgbt.buss_table_id = pgbtrm.buss_table_id2 ";
        //finalQuery = pbDb.buildQuery(sqlstr, Obj);
        finalQuery = sqlstr;
        //  ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("finalQuery in param is" + finalQuery);
        retObj = execSelectSQL(finalQuery);

        colNames = retObj.getColumnNames();
        int psize = retObj.getRowCount();
        if (psize > 0) {
            for (int looper = 0; looper < psize; looper++) {
                //mainTable.add(retObj.getFieldValueString(looper, colNames[0]));
                //////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("looper "+ looper +" table id " + retObj.getFieldValueString(looper, colNames[0]));
                tableRelationship.put(retObj.getFieldValueString(looper, colNames[0]), "");
                recfindTable(retObj.getFieldValueString(looper, colNames[0]), businessTableId + "," + retObj.getFieldValueString(looper, colNames[0]));
            }

        }
        // ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" tableRelationship -- "+tableRelationship);



        return (tableRelationship);
    }

    // to insert all measure details. The target table and its details in various tables
    public void addExtraColumns(String busGroup, String parameterIds, String measureId) {
    }
    //added by susheela on 21-12-09 for target actual formula

    public void addTargetActualFormula(String tabColumns, String busGroup, String draggedCol, String draggedColNewId, String draggedColName, int measure_id) throws Exception {
        if (tabColumns.length() > 1) {
            tabColumns = tabColumns.substring(1);
        }
        String det[] = tabColumns.split("~");
        String actualColId = "";
        String dependenteleids = "";
        String colName = "";
        String colIdDetails = "select * from prg_grp_buss_table_details where buss_column_id in(" + draggedCol + "," + draggedColNewId + ")";
        PbReturnObject colDetObj = execSelectSQL(colIdDetails);

        for (int m = 0; m < det.length; m++) {
            if (m == 1) {
                actualColId = det[m];
            }
            if (m == 3) {
                colName = det[m];
            }
        }
        dependenteleids = draggedColNewId + "," + draggedCol;

        ArrayList list = new ArrayList();
//        String column_formula = "";
        StringBuilder column_formula = new StringBuilder();
        String connId = "";
        String colNamenew = "TDM_" + colName;
        String agregationtype = "SUM";

        String conQuery = "select * from prg_grp_master where grp_id=" + busGroup;
        PbReturnObject conObj = execSelectSQL(conQuery);
        connId = conObj.getFieldValueString(0, "CONNECTION_ID");

        String alreadyCalFactForGrpQ = "select * from prg_grp_buss_table where grp_id=" + busGroup + " and buss_table_name='Calculated Facts'";
        PbReturnObject tabObj = execSelectSQL(alreadyCalFactForGrpQ);
        int seqaddFormulaBussMater = 0;
        if (tabObj.getRowCount() > 0) {
            seqaddFormulaBussMater = tabObj.getFieldValueInt(0, "BUSS_TABLE_ID");
        }
        String addFormulaBussMater = getResourceBundle().getString("addFormulaBussMater");
        String addFormulaSrc = getResourceBundle().getString("addFormulaSrc");
        int seqaddFormulaSrc = 0;
        String finalQuery = "";

        String alreadyBussTabSrcQ = "select * from prg_grp_buss_table_src where buss_table_id "
                + " in(select buss_table_id from prg_grp_buss_table where grp_id=" + busGroup + " and buss_table_name='Calculated Facts')";
         PbReturnObject alBussSrcObj = execSelectSQL(alreadyBussTabSrcQ);

        if (alBussSrcObj.getRowCount() > 0) {
            seqaddFormulaSrc = alBussSrcObj.getFieldValueInt(0, "BUSS_SOURCE_ID");
        }

        if (seqaddFormulaBussMater == 0) {
            seqaddFormulaBussMater = getSequenceNumber("select PRG_GRP_BUSS_TABLE_SEQ.nextval from dual");
            Object obj[];
            obj = new Object[8];
            obj[0] = seqaddFormulaBussMater;
            obj[1] = "Calculated Facts";
            obj[2] = "Calculated Facts";
            obj[3] = "Table";
            obj[4] = "1";
            obj[5] = "";
            obj[6] = "";
            obj[7] = busGroup;
            finalQuery = buildQuery(addFormulaBussMater, obj);
            list.add(finalQuery);
            ////////////////////////////////////////////////////////////////////////.println.println("finalQuery---1 " + finalQuery);

            seqaddFormulaSrc = getSequenceNumber("select PRG_GRP_BUSS_TABLE_SRC_SEQ.nextval from dual");
            Object obj1[];
            obj1 = new Object[7];
            obj1[0] = seqaddFormulaSrc;
            obj1[1] = seqaddFormulaBussMater;
            obj1[2] = "0";
            obj1[3] = "Table";
            obj1[4] = "";
            obj1[5] = connId;
            obj1[6] = "";
            finalQuery = buildQuery(addFormulaSrc, obj1);
            list.add(finalQuery);
            ////////////////////////////////////////////////////////////////////////.println.println("finalQuery---2 " + finalQuery);

        }

        String coldetssql = "select d.COLUMN_NAME,m.BUSS_TABLE_NAME,m.buss_type from PRG_GRP_BUSS_TABLE_DETAILS d,PRG_GRP_BUSS_TABLE m where d.buss_column_id in (" + dependenteleids + ") and d.BUSS_TABLE_ID=m.BUSS_TABLE_ID order by 3 desc";
        PbReturnObject coldetssqlpbro = execSelectSQL(coldetssql);
        String bussNamecolName = "";
        String onlyColName = "";
        /*
         * for(int i=0;i<coldetssqlpbro.getRowCount();i++){
         * bussNamecolName=coldetssqlpbro.getFieldValueString(i,1)+"."+coldetssqlpbro.getFieldValueString(i,0);
         * onlyColName=coldetssqlpbro.getFieldValueString(i,0);
         * column_formula=column_formula.replace(bussNamecolName,onlyColName); }
         */
        for (int i = 0; i < coldetssqlpbro.getRowCount(); i++) {
            if (i == 0) {
//                column_formula = coldetssqlpbro.getFieldValueString(i, "COLUMN_NAME");
                column_formula.append(coldetssqlpbro.getFieldValueString(i, "COLUMN_NAME"));
            } else {
//                column_formula = column_formula + "-" + coldetssqlpbro.getFieldValueString(i, "COLUMN_NAME");
                column_formula.append("-").append( coldetssqlpbro.getFieldValueString(i, "COLUMN_NAME"));
            }
        }
        //add grp buss src details
        int srcdetnextval = getSequenceNumber("select PRG_GRP_BUSS_TAB_SRC_DTLS_SEQ.nextval from dual");
        String addFormulasrcDetails = getResourceBundle().getString("addFormulasrcDetails");
        Object obj2[] = new Object[7];
        obj2[0] = srcdetnextval;
        obj2[1] = seqaddFormulaSrc;
        obj2[2] = "0";
        obj2[3] = seqaddFormulaBussMater;
        obj2[4] = colNamenew;
        obj2[5] = "calculated";
        obj2[6] = "0";
        finalQuery = buildQuery(addFormulasrcDetails, obj2);
        list.add(finalQuery);
        String addFormulaBussDetails = getResourceBundle().getString("addFormulaBussDetails");
        int seqaddFormulaBussDetails = getSequenceNumber("select PRG_GRP_BUSS_TABLE_DETAILS_SEQ.nextval from dual");
        Object obj3[] = new Object[15];
        obj3[0] = seqaddFormulaBussDetails;
        obj3[1] = seqaddFormulaBussMater;
        obj3[2] = colNamenew;
        obj3[3] = "0";
        obj3[4] = srcdetnextval;
        obj3[5] = "Calculated";
        obj3[6] = "0";
        obj3[7] = colNamenew;
        obj3[8] = "0";
        obj3[9] = "N";
        obj3[10] = column_formula;
        obj3[11] = agregationtype;
        obj3[12] = colNamenew;
        obj3[13] = dependenteleids;
        obj3[14] = "Y";
        finalQuery = buildQuery(addFormulaBussDetails, obj3);
        list.add(finalQuery);
      
        for (int y = 0; y < list.size(); y++)         {
            try {
                executeMultiple(list);
            } catch (Exception e) {
                logger.error("Exception:", e);
            }
        }
        ArrayList uList = new ArrayList();
        String updateQ = "update PRG_TARGET_MASTER set TARGET_COLID=" + draggedColNewId + ",ACTUAL_COLID=" + draggedCol + ",DEVIATION_COLID=" + seqaddFormulaBussDetails + "  where prg_measure_id=" + measure_id;
         uList.add(updateQ);
        try {
            executeMultiple(uList);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

    }

    public void addTargetMeasuresColumns(String busGroup, String tabColumns, String parameterIds, String eleIds) throws Exception {
        // to find the praameter ids names

        String parameterId[] = parameterIds.split(",");
        ArrayList createTableList = new ArrayList();
        String draggedCol = "";
        String draggedColNewId = "";
        String draggedColName = "";

        String elementIds[] = eleIds.split(",");


        String getTargetMemberNames = getResourceBundle().getString("getTargetMemberNames");
        Object Pobj[] = new Object[1];
        Pobj[0] = parameterIds;
        String fingetTargetMemberNames = buildQuery(getTargetMemberNames, Pobj);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" fingetTargetMemberNames . "+fingetTargetMemberNames);
        PbReturnObject paramNames = execSelectSQL(fingetTargetMemberNames);

        // to get the dim members
        String getDimTargetMemberNames = getResourceBundle().getString("getDimMemberDet");
        ////////////////////////////////////////////////////////////////////////.println.println(" tabColumns -.== "+tabColumns);
        // Object eleOb[] = new Object[1];
        //  eleOb[0] = eleIds;

        Object busObj[] = new Object[2];
        busObj[0] = busGroup;
        busObj[1] = busGroup;
        //  String dimMem = buildQuery(getDimTargetMemberNames,eleOb);
        String dimMem = buildQuery(getDimTargetMemberNames, busObj);
        //////////////////////////////////////////////////////////////////////////////////.println.println(" dimMem. "+dimMem);
        PbReturnObject dimObj = execSelectSQL(dimMem);

        // to store the selected parameter Ids
        HashMap colIdsMembers = new HashMap();
        ArrayList colNameParameters = new ArrayList();


        String keyValuesString = getResourceBundle().getString("getKeyMembers");
        String fingetKeyMembers = buildQuery(keyValuesString, Pobj);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" fingetKeyMembers ."+fingetKeyMembers);
        ArrayList paramColsKey = new ArrayList();
        PbReturnObject keyObj = execSelectSQL(fingetKeyMembers);
        for (int u = 0; u < keyObj.getRowCount(); u++) {
            paramColsKey.add(keyObj.getFieldValueString(u, "COL_ID"));
        }


        String getTargetDims = getResourceBundle().getString("getTargetDims");
        String getTargetColIds = getResourceBundle().getString("getTargetColIds");
        ArrayList tabIds = new ArrayList();
        ArrayList colIds = new ArrayList();
        ArrayList selectedCols = new ArrayList();
//        String commaCols = "";
        StringBuilder commaCols = new StringBuilder();
        HashMap tabColsHm = new HashMap();
        HashMap colsNamesTypes = new HashMap();
        HashMap colIdNames = new HashMap();
        ArrayList allTabs = new ArrayList();
//        String commaTabs = "";
        StringBuilder commaTabs = new StringBuilder();
        String finalColName = "";
        // get the business table id and column id
        String tabName = "";
        String colName = "";
        String tabColCombo[] = tabColumns.substring(1).split(",");
        for (int i = 0; i < tabColCombo.length; i++) {
            String value = tabColCombo[i];
            String tabs[] = tabColCombo[i].split("~");//value.split(".");
      for (int y = 0; y < tabs.length; y++) {
                if (y == 0) {
                    tabName = tabs[0];
                }
                if (y == 1) {
                    colName = tabs[1];
                    draggedCol = tabs[1];
                }
                if (y == 3) {
                    finalColName = tabs[3];
                }
            }
            String fiCol = "TARGETM_" + finalColName;
            if (fiCol.length() > 32) {
                fiCol = fiCol.substring(0, 31);
            }
            finalColName = fiCol;
            if (!colIdNames.containsKey(colName)) {
                colIdNames.put(colName, finalColName);
            }

            if (!selectedCols.contains(colName)) {
                selectedCols.add(colName);
                ArrayList val = new ArrayList();
                val.add(0, colName);
                val.add(1, "NUMBER");
                colIdsMembers.put(finalColName, val);
                if (!colNameParameters.contains(finalColName)) {
                    colNameParameters.add(finalColName);
                }
            }

            if (!colsNamesTypes.containsKey(finalColName)) {
                colsNamesTypes.put(finalColName, "NUMBER");
            }
            if (!allTabs.contains(tabName)) {
                allTabs.add(tabName);
            }

            if (!tabColsHm.containsKey(tabName)) {
//                commaTabs = commaTabs + "," + tabName;
                commaTabs.append( ",").append( tabName);
                if (!colIds.contains(colName)) {
                    String c = "," + colName;
                    tabColsHm.put(tabName, c);
                }

            } else if (tabColsHm.containsKey(tabName)) {
                if (!colIds.contains(colName)) {
                    String newAl = (String) tabColsHm.get(tabName);
                    newAl = newAl + "," + colName;
                    tabColsHm.put(tabName, newAl);
                }
            }
            if (!tabIds.contains(tabName)) {
                tabIds.add(tabName);

            }
            if (!colIds.contains(colName)) {
                colIds.add(colName);
//                commaCols = commaCols + "," + colName;
                commaCols.append( "," ).append(colName);
            }
        }
     
        ArrayList selectedMeasures = colIds;
        // for getting information to make joins with the related business tables
        for (int u = 0; u < tabIds.size(); u++) {
            String tab = tabIds.get(u).toString();
            BusinessTablePaths bt = new BusinessTablePaths();

            // modified on 4th nov-09
            //  HashMap hm = bt.getpathFromBusinessTable(tab);
            HashMap hm = getpathFromBusinessTable(tab);
            Set hmSet = hm.keySet();

            String keys[] = (String[]) hmSet.toArray(new String[0]);
            for (int v = 0; v < keys.length; v++) {
                String t = keys[v];
                if (!allTabs.contains(t)) {
                    allTabs.add(t);
                    commaTabs.append(",").append(t);
                }
            }
        }
        commaTabs = new StringBuilder(commaTabs.substring(1));
//        commaCols = commaCols.substring(1);
        // to get the dimension members
        for (int p = 0; p < tabIds.size(); p++) {
            String tId = tabIds.get(p).toString();
            Object tObj[] = new Object[1];
            tObj[0] = commaTabs;
//            String finalgetTargetDims = buildQuery(getTargetDims, tObj);

            // PbReturnObject tabOb = execSelectSQL(finalgetTargetDims);
            String allCols = "";
            if (tabColsHm.containsKey(tId)) {
                allCols = tabColsHm.get(tId).toString();

            }

            Object dimOb[] = new Object[1];
            dimOb[0] = commaTabs;
            String finalgetTargetColIds = buildQuery(getTargetColIds, dimOb);

            PbReturnObject allColsObj = execSelectSQL(finalgetTargetColIds);
            // allColsObj.writeString();
            //commented on 9th Nov
              /*
             * for(int d=0;d<allColsObj.getRowCount();d++){ String cName =
             * allColsObj.getFieldValueString(d,"COLUMN_NAME"); String cType =
             * allColsObj.getFieldValueString(d,"COLUMN_TYPE");
             * if(!colsNamesTypes.containsKey(cName.trim()))
             * colsNamesTypes.put(cName.trim(),cType.trim()); }
             */
            // colIds
        }
        String factQuery = "";
        String newTableName = "PRG_FACT_MEASURE_DET";
        int seq_id = 0;
        String finalfactQuery = "";
        int target_table_id = 0;
        ArrayList allQ = new ArrayList();
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            finalfactQuery = "select ident_current('PRG_TARGET_MEASURE')";
            PbReturnObject currentValObject = execSelectSQL(finalfactQuery);
            seq_id = currentValObject.getFieldValueInt(0, 0) + 1;
            String insertTrgtMes = getResourceBundle().getString("insertTrgtMes");
            Object relSeqObj[] = new Object[1];
            relSeqObj[0] = seq_id;
            finalfactQuery = buildQuery(insertTrgtMes, relSeqObj);
            execModifySQL(finalfactQuery);
            PbReturnObject seqReturnObj = execSelectSQL("select ident_current('PRG_DB_MASTER_TABLE')");//PRG_DB_MASTER_TABLE
            target_table_id = seqReturnObj.getFieldValueInt(0, 0) + 1;
            //
        } else {
            factQuery = getResourceBundle().getString("getSequenceNumber");
            Object relSeqObj[] = new Object[1];
            relSeqObj[0] = "PRG_TARGET_MEASURE_SEQ";
            finalfactQuery = buildQuery(factQuery, relSeqObj);
            seq_id = getSequenceNumber(finalfactQuery);
            Object relSeqObj2[] = new Object[1];
            relSeqObj2[0] = "PRG_DATABASE_MASTER_SEQ";
            String finalfactQuery2 = buildQuery(factQuery, relSeqObj2);
            target_table_id = getSequenceNumber(finalfactQuery2);
        }



        try {
            // execModifySQL(finalCreateTable);
        } catch (Exception ex) {
            logger.error("Exception:", ex);

        }

        // to insert into prg_target_master
        String addTargetMeasureMaster = getResourceBundle().getString("addTargetMeasureMaster");
        String addTargetMeasureDeatils = getResourceBundle().getString("addTargetMeasureDetails");
        String addTargetMeasureColDetails = getResourceBundle().getString("addTargetMasterColDetails");
        int bussTabId = 0;
        int tar_master = 0;
        Object bussTableObj[] = null;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            bussTableObj = new Object[9];
        } else {
            bussTableObj = new Object[10];
        }

        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            //PRG_GRP_BUSS_TABLE
            PbReturnObject objectsEq = execSelectSQL("select ident_current('PRG_GRP_BUSS_TABLE')");
            bussTabId = objectsEq.getFieldValueInt(0, 0) + 1;
//            PbReturnObject retTarMaster = execSelectSQL("select ident_current('PRG_TARGET_MASTER')");//PRG_TARGET_MASTER
//            tar_master = retTarMaster.getFieldValueInt(0, 0) + 1;
//            String insertTrgtMester = getResourceBundle().getString("insertTrgtMester");
//            Object relSeqObj[] = new Object[1];
//            relSeqObj[0] = tar_master;
//            finalfactQuery = buildQuery(insertTrgtMester, relSeqObj);
//            execModifySQL(finalfactQuery);


        } else {
            Object bussTabObj[] = new Object[1];
            bussTabObj[0] = "PRG_GRP_BUSS_TABLE_SEQ";
            String bussTabQuery = buildQuery(factQuery, bussTabObj);
            bussTabId = getSequenceNumber(bussTabQuery);
            // inserting in the target_measure_master
            Object tarSeqObj[] = new Object[1];
            tarSeqObj[0] = "PRG_TAR_MASTER_SEQ";
            String finalTarQuery = buildQuery(factQuery, tarSeqObj);
            tar_master = getSequenceNumber(finalTarQuery);
        }


        String cName = "";
        if (colIdNames.containsKey(selectedMeasures.get(0).toString())) {
            cName = colIdNames.get(selectedMeasures.get(0).toString()).toString();
        }
        String measureName = cName;
        // if(measureName.length()>32)
        // measureName = measureName.substring(0,31);
        String finAddTargetMeasureMaster = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            Object tarObj[] = new Object[4];
//            tarObj[0] = Integer.valueOf(tar_master);
            tarObj[0] = tabIds.get(0).toString();
            tarObj[1] = busGroup;
            // tarObj[3] = Integer.valueOf(target_table_id);
            tarObj[2] = Integer.valueOf(bussTabId);
            tarObj[3] = measureName;
            finAddTargetMeasureMaster = buildQuery(addTargetMeasureMaster, tarObj);
        } else {
            Object tarObj[] = new Object[5];
            tarObj[0] = Integer.valueOf(tar_master);
            tarObj[1] = tabIds.get(0).toString();
            tarObj[2] = busGroup;
            // tarObj[3] = Integer.valueOf(target_table_id);
            tarObj[3] = Integer.valueOf(bussTabId);
            tarObj[4] = measureName;
            finAddTargetMeasureMaster = buildQuery(addTargetMeasureMaster, tarObj);

        }
        allQ.add(finAddTargetMeasureMaster);
        // inserting in the target_measure_master over

        // to insert in the prg_target_measure_members
        String factQuery2 = "";
        String finalfactQuery3 = "";
        String addTargetMeasureMembers = getResourceBundle().getString("addTargetMeasureMembers");
        int seq_id2 = 0;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {

            for (int i = 0; i < parameterId.length; i++) {
                String eleId = "";//elementIds[i];
                String memberId = parameterId[i];
//                int seq_id2 = getSequenceNumber(finalfactQuery3);
                Object memObj[] = new Object[3];
//                memObj[0] = Integer.valueOf(seq_id2);
                memObj[0] = "ident_current('PRG_TARGET_MASTER')";
                memObj[1] = memberId;
                if (eleId.equalsIgnoreCase("")) {
                    eleId = "null";
                }
                memObj[2] = eleId;
                String finaddTargetMeasureMembers = buildQuery(addTargetMeasureMembers, memObj);
                allQ.add(finaddTargetMeasureMembers);
            }
        } else {
            factQuery2 = getResourceBundle().getString("getSequenceNumber");
            Object tmmSeqObj[] = new Object[1];
            tmmSeqObj[0] = "PRG_TARGET_MEAS_MEM_SEQ";
            finalfactQuery3 = buildQuery(factQuery2, tmmSeqObj);
            for (int i = 0; i < parameterId.length; i++) {
                String eleId = "";//elementIds[i];
                String memberId = parameterId[i];
                seq_id2 = getSequenceNumber(finalfactQuery3);
                Object memObj[] = new Object[4];
                memObj[0] = Integer.valueOf(seq_id2);
                memObj[1] = Integer.valueOf(tar_master);
                memObj[2] = memberId;
                memObj[3] = eleId;
                String finaddTargetMeasureMembers = buildQuery(addTargetMeasureMembers, memObj);
                 allQ.add(finaddTargetMeasureMembers);
            }

        }

        // to insert in the prg_target_measure_members

        String apTableName = newTableName + "_" + seq_id;
        HashMap columnLengths = new HashMap();

        // to insert into prg_target_master_details
        String finfinalTarDetQuery = "";
        int tar_master_det = 0;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            Object tarObjDet[] = new Object[3];
            tarObjDet[0] = "ident_current('PRG_TARGET_MASTER')";
//            tarObjDet[1] = Integer.valueOf(tar_master_det);
            tarObjDet[1] = Integer.valueOf(target_table_id);
            tarObjDet[2] = apTableName;
            finfinalTarDetQuery = buildQuery(addTargetMeasureDeatils, tarObjDet);
        } else {
            Object tarSeqDetObj[] = new Object[1];
            tarSeqDetObj[0] = "PRG_TAR_MSTER_DET_SEQ";
            String finalTarDetQuery = buildQuery(factQuery, tarSeqDetObj);
            tar_master_det = getSequenceNumber(finalTarDetQuery);
            Object tarObjDet[] = new Object[4];
            tarObjDet[0] = Integer.valueOf(tar_master);
            tarObjDet[1] = Integer.valueOf(tar_master_det);
            tarObjDet[2] = Integer.valueOf(target_table_id);
            tarObjDet[3] = apTableName;
            finfinalTarDetQuery = buildQuery(addTargetMeasureDeatils, tarObjDet);
        }
  allQ.add(finfinalTarDetQuery);
        // to insert into prg_target_master_details over
        // to insert into prg_target_master_details
        String finalTarColQuery = "";
        Object tarColDetObj[] = new Object[1];
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {

            selectedCols.add("TARGET_ID");
            colsNamesTypes.put("TARGET_ID", "NUMERIC");
            colIdNames.put("TARGET_ID", "TARGET_ID");

            selectedCols.add("FACT_ID");
            colsNamesTypes.put("FACT_ID", "NUMERIC");
            colIdNames.put("FACT_ID", "FACT_ID");

            selectedCols.add("VIEWBY");
            colsNamesTypes.put("VIEWBY", "VARCHAR(50)");
            colIdNames.put("VIEWBY", "VIEWBY");
            columnLengths.put("VIEWBY", "100");

            selectedCols.add("T_DATE");
            colsNamesTypes.put("T_DATE", "DATETIME");
            colIdNames.put("T_DATE", "T_DATE");

            selectedCols.add("T_MONTH");
            colsNamesTypes.put("T_MONTH", "VARCHAR(20)");
            colIdNames.put("T_MONTH", "T_MONTH");
            columnLengths.put("T_MONTH", "20");

            selectedCols.add("T_QTR");
            colsNamesTypes.put("T_QTR", "VARCHAR(20)");
            colIdNames.put("T_QTR", "T_QTR");
            columnLengths.put("T_QTR", "20");


            selectedCols.add("T_YEAR");
            colsNamesTypes.put("T_YEAR", "VARCHAR(20)");
            colIdNames.put("T_YEAR", "T_YEAR");
            columnLengths.put("T_YEAR", "20");

            selectedCols.add("SELECTED_MEASURE");
            colsNamesTypes.put("SELECTED_MEASURE", "VARCHAR(30)");
            colIdNames.put("SELECTED_MEASURE", "SELECTED_MEASURE");
            columnLengths.put("SELECTED_MEASURE", "30");

            selectedCols.add("SECVIEWBY");
            colsNamesTypes.put("SECVIEWBY", "VARCHAR(255)");
            colIdNames.put("SECVIEWBY", "VIEWBY");
            columnLengths.put("SECVIEWBY", "255");
        } else {

            tarColDetObj[0] = "PRG_TAR_MSTER_COLDET_SEQ";
            finalTarColQuery = buildQuery(factQuery, tarColDetObj);
            //tabColsHm
            //extra columns whish are the default columns for the  target table
            selectedCols.add("TARGET_ID");
            colsNamesTypes.put("TARGET_ID", "NUMBER");
            colIdNames.put("TARGET_ID", "TARGET_ID");

            selectedCols.add("FACT_ID");
            colsNamesTypes.put("FACT_ID", "NUMBER");
            colIdNames.put("FACT_ID", "FACT_ID");

            selectedCols.add("VIEWBY");
            colsNamesTypes.put("VIEWBY", "VARCHAR2(50)");
            colIdNames.put("VIEWBY", "VIEWBY");
            columnLengths.put("VIEWBY", "100");

            selectedCols.add("T_DATE");
            colsNamesTypes.put("T_DATE", "DATE");
            colIdNames.put("T_DATE", "T_DATE");

            selectedCols.add("T_MONTH");
            colsNamesTypes.put("T_MONTH", "VARCHAR2(20)");
            colIdNames.put("T_MONTH", "T_MONTH");
            columnLengths.put("T_MONTH", "20");

            selectedCols.add("T_QTR");
            colsNamesTypes.put("T_QTR", "VARCHAR2(20)");
            colIdNames.put("T_QTR", "T_QTR");
            columnLengths.put("T_QTR", "20");


            selectedCols.add("T_YEAR");
            colsNamesTypes.put("T_YEAR", "VARCHAR2(20)");
            colIdNames.put("T_YEAR", "T_YEAR");
            columnLengths.put("T_YEAR", "20");

            selectedCols.add("SELECTED_MEASURE");
            colsNamesTypes.put("SELECTED_MEASURE", "VARCHAR2(30)");
            colIdNames.put("SELECTED_MEASURE", "SELECTED_MEASURE");
            columnLengths.put("SELECTED_MEASURE", "30");

            selectedCols.add("SECVIEWBY");
            colsNamesTypes.put("SECVIEWBY", "VARCHAR2(255)");
            colIdNames.put("SECVIEWBY", "VIEWBY");
            columnLengths.put("SECVIEWBY", "255");
        }


          // add the columns of the parameters
        for (int m = 0; m < dimObj.getRowCount(); m++) {
            String colId = dimObj.getFieldValueString(m, "BUSS_COLUMN_ID");
            String colType = dimObj.getFieldValueString(m, "COLUMN_TYPE");
            colName = dimObj.getFieldValueString(m, "COLUMN_NAME");
            if (paramColsKey.contains(colId)) {
                selectedCols.add(colId);
                colIdNames.put(colId, colName);
                // colsNamesTypes.put(colName,colType);
                // change on 24th oct
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    colsNamesTypes.put(colName, "varchar(100)");
                } else {
                    colsNamesTypes.put(colName, "varchar2(100)");
                }

                ArrayList val = new ArrayList();
                val.add(0, colId);
                val.add(1, colType);
                colIdsMembers.put(colName, val);
                if (!colNameParameters.contains(colName)) {
                    colNameParameters.add(colName);
                }
            }

        }
        String createQuery = "create table " + apTableName + "(";
//        String middleClause = "";
        StringBuilder middleClause = new StringBuilder();
        String endClause = ")";
        String finalCreateTable = "";
        Set hmSetCol = colsNamesTypes.keySet();
        
        // to find the type and lengh of the columns and make the query for creating the target table
        String keysCol[] = (String[]) hmSetCol.toArray(new String[0]);
        for (int z = 0; z < keysCol.length; z++) {
            String key = keysCol[z];
            String type = colsNamesTypes.get(key).toString();
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                if (type.equalsIgnoreCase("NUMBER")) {
                    type = "NUMERIC";
                }
            }
            if (z == 0) {
//                middleClause = middleClause + key + " " + type;
                middleClause.append( key ).append( " " ).append( type);
            } else {
//                middleClause = middleClause + "," + key + " " + type;
                middleClause.append(",").append(key).append(" " ).append( type);
            }
        }
        finalCreateTable = createQuery + middleClause + endClause;
        // allQ.add(finalCreateTable);
        createTableList.add(finalCreateTable);
        Object tarColDet[] = null;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            tarColDet = new Object[selectedCols.size()];
        } else {
            tarColDet = new Object[selectedCols.size()];
        }



        // to enter in the db Table master. First get the connection id of the dragged measures fact table
        String conQuery = "select CONNECTION_ID,USER_SCHEMA  from prg_db_master_table where table_id=(select db_table_id from prg_grp_buss_table where buss_table_id=" + tabIds.get(0) + ")";
        String connectionId = "";
        String userSchema = "";
        PbReturnObject ConOb = execSelectSQL(conQuery);
        for (int p = 0; p < ConOb.getRowCount(); p++) {
            connectionId = ConOb.getFieldValueString(0, "CONNECTION_ID");
            userSchema = ConOb.getFieldValueString(0, "USER_SCHEMA");
        }

        String addTargetTableInDbMaster = getResourceBundle().getString("addTargetTableInDbMaster");
        String finaddTargetTableInDbMaster = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            Object TargetDbMObj[] = new Object[10];
            TargetDbMObj[0] = connectionId;
            TargetDbMObj[1] = userSchema;
//            TargetDbMObj[2] = target_table_id;
            TargetDbMObj[2] = apTableName;
            TargetDbMObj[3] = apTableName;
            TargetDbMObj[4] = "Target Table";
            TargetDbMObj[5] = "null";
            TargetDbMObj[6] = "null";
            TargetDbMObj[7] = "null";
            TargetDbMObj[8] = "null";
            TargetDbMObj[9] = "null";
            finaddTargetTableInDbMaster = buildQuery(addTargetTableInDbMaster, TargetDbMObj);
        } else {
            Object TargetDbMObj[] = new Object[11];
            TargetDbMObj[0] = connectionId;
            TargetDbMObj[1] = userSchema;
            TargetDbMObj[2] = target_table_id;
            TargetDbMObj[3] = apTableName;
            TargetDbMObj[4] = apTableName;
            TargetDbMObj[5] = "Target Table";
            TargetDbMObj[6] = "";
            TargetDbMObj[7] = "";
            TargetDbMObj[8] = "";
            TargetDbMObj[9] = "";
            TargetDbMObj[10] = "";
            finaddTargetTableInDbMaster = buildQuery(addTargetTableInDbMaster, TargetDbMObj);
        }
        allQ.add(finaddTargetTableInDbMaster);
        // inserttion in db_Table_master over
        //db_tableId is the target_table_id
        int db_tableId = target_table_id;


        // to enter in the PRG_GRP_BUSS_TABLE //
        //PRG_GRP_BUSS_TABLE_SEQ

        String addTargetTabInBussTable = getResourceBundle().getString("addTargetTabInBussTable");
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//            bussTableObj[0] = Integer.valueOf(bussTabId);
            bussTableObj[0] = apTableName;
            bussTableObj[1] = apTableName;
            bussTableObj[2] = "Target Table";
            bussTableObj[3] = "1";
            bussTableObj[4] = "N";
            bussTableObj[5] = "null";
            bussTableObj[6] = Integer.valueOf(db_tableId);
            bussTableObj[7] = busGroup;
            bussTableObj[8] = "null";
        } else {
            bussTableObj[0] = Integer.valueOf(bussTabId);
            bussTableObj[1] = apTableName;
            bussTableObj[2] = apTableName;
            bussTableObj[3] = "Target Table";
            bussTableObj[4] = "1";
            bussTableObj[5] = "";
            bussTableObj[6] = "";
            bussTableObj[7] = Integer.valueOf(db_tableId);
            bussTableObj[8] = busGroup;
            bussTableObj[9] = "";
        }


        String finaddTargetTabInBussTable = buildQuery(addTargetTabInBussTable, bussTableObj);
        allQ.add(finaddTargetTabInBussTable);
        //to enter in the PRG_GRP_BUSS_TABLE over

        // to add in the PRG_GRP_BUSS_TABLE_SRC  PRG_GRP_BUSS_TABLE_SRC_SEQ
        String addTargetTabInBussSrcTable = getResourceBundle().getString("addTargetTabInBussSrcTable");
        String finaddTargetTabInBussSrcTable = "";
        int bussSrcId = 0;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//            Object bussTabSrcObj[] = new Object[1];
//            bussTabSrcObj[0] = "PRG_GRP_BUSS_TABLE_SRC_SEQ";
//            String bussTabSrcQuery = buildQuery(factQuery, bussTabSrcObj);
//            int bussSrcId = getSequenceNumber(bussTabSrcQuery);
            Object bussSrcOb[] = new Object[6];
//            bussSrcOb[0] = Integer.valueOf(bussSrcId);
            bussSrcOb[0] = Integer.valueOf(bussTabId);
            bussSrcOb[1] = connectionId;
            bussSrcOb[2] = Integer.valueOf(db_tableId);
            bussSrcOb[3] = "Table";
            bussSrcOb[4] = "null";
            bussSrcOb[5] = "null";
            finaddTargetTabInBussSrcTable = buildQuery(addTargetTabInBussSrcTable, bussSrcOb);
        } else {
            Object bussTabSrcObj[] = new Object[1];
            bussTabSrcObj[0] = "PRG_GRP_BUSS_TABLE_SRC_SEQ";
            String bussTabSrcQuery = buildQuery(factQuery, bussTabSrcObj);
            bussSrcId = getSequenceNumber(bussTabSrcQuery);
            Object bussSrcOb[] = new Object[7];
            bussSrcOb[0] = Integer.valueOf(bussSrcId);
            bussSrcOb[1] = Integer.valueOf(bussTabId);
            bussSrcOb[2] = connectionId;
            bussSrcOb[3] = Integer.valueOf(db_tableId);
            bussSrcOb[4] = "Table";
            bussSrcOb[5] = "";
            bussSrcOb[6] = "";
            finaddTargetTabInBussSrcTable = buildQuery(addTargetTabInBussSrcTable, bussSrcOb);
        }


        //finaddTargetTabInBussSrcTable = buildQuery(addTargetTabInBussSrcTable, bussSrcOb);
        allQ.add(finaddTargetTabInBussSrcTable);
        // to add in the PRG_GRP_BUSS_TABLE_SRC over
        // to add in the buss_table_details
        String addBussTableDetails = getResourceBundle().getString("addBussTableDetails");
        String addTargetTabInBussSrcDetailsTable = getResourceBundle().getString("addTargetTabInBussSrcDetailsTable");
        String bussDetQuery = "";
        String srcBussDetQuery = "";
        String finalMasterColDetQuery = "";
        String addTargetTableInDbDetails = getResourceBundle().getString("addTargetTableInDbDetails");
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
        } else {
            Object bussTabDetailsObj[] = new Object[1];
            bussTabDetailsObj[0] = "PRG_GRP_BUSS_TABLE_DETAILS_SEQ";
            bussDetQuery = buildQuery(factQuery, bussTabDetailsObj);
            Object srcBussDetailsObj[] = new Object[1];
            srcBussDetailsObj[0] = "PRG_GRP_BUSS_TAB_SRC_DTLS_SEQ";
            srcBussDetQuery = buildQuery(factQuery, srcBussDetailsObj);
            Object masterColDetObj[] = new Object[1];
            tarColDetObj[0] = "PRG_DB_MASTER_TABLE_DTLS_SEQ";
            finalMasterColDetQuery = buildQuery(factQuery, tarColDetObj);
        }
        //to add in the prg_db_master_table_details and addTargetTabInBussSrcDetailsTable

        String finaddTargetMeasureColDetails = "";
        for (int g = 0; g < selectedCols.size(); g++) {
            int tar_col_det = 0;
            int newColId = 0;
            String colId = selectedCols.get(g).toString();
            String colType = "";
            String colName2 = "";
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                tar_col_det = getSequenceNumber(finalTarColQuery);
//                  newColId = getSequenceNumber(finalMasterColDetQuery);
                if (colIdNames.containsKey(colId)) {
                    colName2 = colIdNames.get(colId).toString();
                }
                if (colsNamesTypes.containsKey(colName2.trim())) {
                    colType = colsNamesTypes.get(colName2.trim()).toString();
                }
                String selectedMeasuresSt = "N";
                if (selectedMeasures.contains(colId)) {
                    selectedMeasuresSt = "Y";
                }
                tarColDet[0] = "ident_current('PRG_TARGET_MASTER_DETAILS')";
                tarColDet[1] = "ident_current('target_master_col_details')";
//                tarColDet[2] = Integer.valueOf(newColId);//colId;
                tarColDet[2] = colName2;
                tarColDet[3] = colType;
                tarColDet[4] = Integer.valueOf(bussTabId);//tabIds.get(0).toString();
                tarColDet[5] = "Target Table";
                tarColDet[6] = Integer.valueOf(target_table_id);
                tarColDet[7] = selectedMeasuresSt;
                finaddTargetMeasureColDetails = buildQuery(addTargetMeasureColDetails, tarColDet);
            } else {
                tar_col_det = getSequenceNumber(finalTarColQuery);
                newColId = getSequenceNumber(finalMasterColDetQuery);
                if (colIdNames.containsKey(colId)) {
                    colName2 = colIdNames.get(colId).toString();
                }
                if (colsNamesTypes.containsKey(colName2.trim())) {
                    colType = colsNamesTypes.get(colName2.trim()).toString();
                }
                String selectedMeasuresSt = "N";
                if (selectedMeasures.contains(colId)) {
                    selectedMeasuresSt = "Y";
                }
                tarColDet[0] = Integer.valueOf(tar_master_det);
                tarColDet[1] = Integer.valueOf(tar_col_det);
                tarColDet[2] = Integer.valueOf(newColId);//colId;
                tarColDet[3] = colName2;
                tarColDet[4] = colType;
                tarColDet[5] = Integer.valueOf(bussTabId);//tabIds.get(0).toString();
                tarColDet[6] = "Target Table";
                tarColDet[7] = Integer.valueOf(target_table_id);
                tarColDet[8] = selectedMeasuresSt;
                finaddTargetMeasureColDetails = buildQuery(addTargetMeasureColDetails, tarColDet);
            }
   allQ.add(finaddTargetMeasureColDetails);

            // to add in prg_db_master_table_details
            Object masterDetailsObj[] = null;
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                masterDetailsObj = new Object[7];
            } else {
                masterDetailsObj = new Object[8];
            }

            String colL = "";
            String primKey = "N";
            if (colId.equalsIgnoreCase("FACT_ID")) {
                primKey = "Y";
            }
            if (columnLengths.containsKey(colId)) {
                colL = columnLengths.get(colId).toString();
            }
            String finAddTargetTableInDbDetails = "";
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                masterDetailsObj[0] = Integer.valueOf(newColId);
                masterDetailsObj[0] = Integer.valueOf(target_table_id);
                masterDetailsObj[1] = colName2;
                masterDetailsObj[2] = colName2;
                masterDetailsObj[3] = colType;
                if (colL.equalsIgnoreCase("") || colId == null) {
                    masterDetailsObj[4] = "null";
                } else {
                    masterDetailsObj[4] = colL;
                }

                masterDetailsObj[5] = primKey;
                masterDetailsObj[6] = "Y";
                finAddTargetTableInDbDetails = buildQuery(addTargetTableInDbDetails, masterDetailsObj);
            } else {
                masterDetailsObj[0] = Integer.valueOf(newColId);
                masterDetailsObj[1] = Integer.valueOf(target_table_id);
                masterDetailsObj[2] = colName2;
                masterDetailsObj[3] = colName2;
                masterDetailsObj[4] = colType;
                masterDetailsObj[5] = colL;
                masterDetailsObj[6] = primKey;
                masterDetailsObj[7] = "Y";
                finAddTargetTableInDbDetails = buildQuery(addTargetTableInDbDetails, masterDetailsObj);
            }

            allQ.add(finAddTargetTableInDbDetails);

            // to add in addTargetTabInBussSrc Table
            String finaddTargetTabInBussSrcDetailsTable = "";
            int bussSrcDetId = 0;
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                int bussSrcDetId = getSequenceNumber(srcBussDetQuery);
                Object srcDetailsObj[] = new Object[6];
//                srcDetailsObj[0] = Integer.valueOf(bussSrcDetId);
                srcDetailsObj[0] = "ident_current(' PRG_GRP_BUSS_TABLE_SRC')";
                srcDetailsObj[1] = Integer.valueOf(db_tableId);
                srcDetailsObj[2] = Integer.valueOf(bussTabId);
                srcDetailsObj[3] = "ident_current('target_master_col_details')";
                srcDetailsObj[4] = colName2;
                srcDetailsObj[5] = colType;
                finaddTargetTabInBussSrcDetailsTable = buildQuery(addTargetTabInBussSrcDetailsTable, srcDetailsObj);
            } else {
                bussSrcDetId = getSequenceNumber(srcBussDetQuery);
                Object srcDetailsObj[] = new Object[7];
                srcDetailsObj[0] = Integer.valueOf(bussSrcDetId);
                srcDetailsObj[1] = Integer.valueOf(bussSrcId);
                srcDetailsObj[2] = Integer.valueOf(db_tableId);
                srcDetailsObj[3] = Integer.valueOf(bussTabId);
                srcDetailsObj[4] = Integer.valueOf(newColId);
                srcDetailsObj[5] = colName2;
                srcDetailsObj[6] = colType;
                finaddTargetTabInBussSrcDetailsTable = buildQuery(addTargetTabInBussSrcDetailsTable, srcDetailsObj);
            }

             allQ.add(finaddTargetTabInBussSrcDetailsTable);
            // addTargetTabInBussSrcDetailsTable

            // add in buss_table_details
            String defAggr = "";
            if (colType.equalsIgnoreCase("NUMBER")) {
                defAggr = "SUM";
            }
            int bussSeqId = 0;
            String finaddBussTableDetails = "";
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                if (draggedCol.equalsIgnoreCase(colId)) {
                    draggedColNewId = Integer.toString( bussSeqId );
                    draggedColName = colName2;
                }
                Object bussTableDetaOb[] = new Object[12];
//                bussTableDetaOb[0] = Integer.valueOf(bussSeqId);
                bussTableDetaOb[0] = Integer.valueOf(bussTabId);
                bussTableDetaOb[1] = colName2;
                bussTableDetaOb[2] = Integer.valueOf(db_tableId);
                bussTableDetaOb[3] = "ident_current('PRG_DB_MASTER_TABLE_DETAILS')";
                bussTableDetaOb[4] = "ident_current('PRG_GRP_BUSS_TABLE_SRC_DETAILS')";
                bussTableDetaOb[5] = "N";
                bussTableDetaOb[6] = colType;
                bussTableDetaOb[7] = "null";
                bussTableDetaOb[8] = "null";
                if (defAggr.equalsIgnoreCase("")) {
                    defAggr = "null";
                }
                bussTableDetaOb[9] = defAggr;
                bussTableDetaOb[10] = "null";
                bussTableDetaOb[11] = colName2;
                finaddBussTableDetails = buildQuery(addBussTableDetails, bussTableDetaOb);
            } else {
                bussSeqId = getSequenceNumber(bussDetQuery); //addBussTableDetails
                if (draggedCol.equalsIgnoreCase(colId)) {
//                    draggedColNewId = "" + bussSeqId + "";
                    draggedColNewId = Integer.toString( bussSeqId );
                    draggedColName = colName2;
                    Object bussTableDetaOb[] = new Object[13];
                    bussTableDetaOb[0] = Integer.valueOf(bussSeqId);
                    bussTableDetaOb[1] = Integer.valueOf(bussTabId);
                    bussTableDetaOb[2] = colName2;
                    bussTableDetaOb[3] = Integer.valueOf(db_tableId);
                    bussTableDetaOb[4] = Integer.valueOf(newColId);
                    bussTableDetaOb[5] = Integer.valueOf(bussSrcDetId);
                    bussTableDetaOb[6] = "N";
                    bussTableDetaOb[7] = colType;
                    bussTableDetaOb[8] = "";
                    bussTableDetaOb[9] = "";
                    bussTableDetaOb[10] = defAggr;
                    bussTableDetaOb[11] = "";
                    bussTableDetaOb[12] = colName2;
                    finaddBussTableDetails = buildQuery(addBussTableDetails, bussTableDetaOb);
                }
            }

            //added by susheela on 22-12-09
               allQ.add(finaddBussTableDetails);
        }



        // insert in the PRG_GRP_BUSS_TABLE_RLT_MASTER and PRG_GRP_BUSS_TABLE_RLT_DETAILS
         if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//            String addBussTabRelMaster = getResourceBundle().getString("addBussTabRelMaster");
//            Object relMasterObj[] = new Object[1];
//            relMasterObj[0] = "PRG_GRP_BUSS_TAB_RLT_MSTR_SEQ";
//            String relMasterQuery = buildQuery(factQuery, relMasterObj);
//            String addBussTabRelDetails = getResourceBundle().getString("addBussTabRelDetails");
//            Object relDetailsObj[] = new Object[1];
//            relDetailsObj[0] = "PRG_GRP_BUSS_TAB_RLT_DTLS_SEQ";
//            String relDetailsQuery = buildQuery(factQuery, relDetailsObj);
        } else {
//            String addBussTabRelMaster = getResourceBundle().getString("addBussTabRelMaster");
            Object relMasterObj[] = new Object[1];
            relMasterObj[0] = "PRG_GRP_BUSS_TAB_RLT_MSTR_SEQ";
//            String relMasterQuery = buildQuery(factQuery, relMasterObj);

//            String addBussTabRelDetails = getResourceBundle().getString("addBussTabRelDetails");
            Object relDetailsObj[] = new Object[1];
            relDetailsObj[0] = "PRG_GRP_BUSS_TAB_RLT_DTLS_SEQ";
//            String relDetailsQuery = buildQuery(factQuery, relDetailsObj);
        }


//        String commaTabs2 = "";
//        ArrayList al2 = new ArrayList();
        // commented on 13th nov
     /*
         * for(int n=0;n<allTabs.size();n++) { HashMap hm = new HashMap();
         * String tId = allTabs.get(n).toString(); al2.add(tId);
         * BusinessTablePaths bt = new BusinessTablePaths(); hm =
         * getpathFromBusinessTable(tId);//bt.getpathFromBusinessTable(tId); Set
         * key5 = (Set)hm.keySet(); String keys[] = (String[])key5.toArray(new
         * String[0]); for(int u=0;u<keys.length;u++){ String kVal = keys[u];
         * if(!allTabs.contains(kVal)){ allTabs.add(kVal); } } }
         */

        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" allTabs... .. / / "+allTabs);
     /*
         * for(int m=0;m<al2.size();m++) { commaTabs2 =
         * commaTabs2+","+al2.get(m).toString(); } commaTabs2 =
         * commaTabs2.substring(1);
         * ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("
         * commaTabs2. "+commaTabs2); Object tabObj[]=new Object[1]; tabObj[0] =
         * commaTabs2; String finMeasureTableCols =
         * buildQuery(getMeasureTableCols,tabObj);
         * ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("
         * finMeasureTableCols ."+finMeasureTableCols); PbReturnObject tabRetObj
         * = execSelectSQL(finMeasureTableCols); for(int
         * p=0;p<tabRetObj.getRowCount();p++) { String bussTabName =
         * tabRetObj.getFieldValueString(p,"BUSS_TABLE_NAME"); String
         * bussColName = tabRetObj.getFieldValueString(p,"COLUMN_NAME"); String
         * bussColId2 = tabRetObj.getFieldValueString(p,"BUSS_COLUMN_ID");
         * String bussTabId2 = tabRetObj.getFieldValueString(p,"BUSS_TABLE_ID");
         * String bussColId =""; if(colNameParameters.contains(bussColName)) {
         * // to insert in buss table relation master ArrayList details =
         * (ArrayList) colIdsMembers.get(bussColName); String bussTabId1 =
         * ""+bussTabId+""; bussColId = details.get(0).toString();
         * if(!selectedMeasures.contains(bussColId)){ int reSeq =
         * getSequenceNumber(relMasterQuery); String clause =
         * apTableName+"."+bussColName+"="+bussTabName+"."+bussColName;
         *
         * Object insertRelOb[] = new Object[7]; insertRelOb[0] =
         * Integer.valueOf(reSeq); insertRelOb[1] = bussTabId1; insertRelOb[2] =
         * bussTabId2; insertRelOb[3] = "1"; insertRelOb[4] = clause;
         * insertRelOb[5] = ""; insertRelOb[6] = "inner"; String
         * finaddBussTabRelMaster = buildQuery(addBussTabRelMaster,insertRelOb);
         * ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("
         * finaddBussTabRelMaster. "+finaddBussTabRelMaster);
         * allQ.add(finaddBussTabRelMaster);
         *
         * // to insert in buss table relation details int reDetSeq =
         * getSequenceNumber(relDetailsQuery); Object relDetObj[] = new
         * Object[11]; relDetObj[0] = Integer.valueOf(reDetSeq); relDetObj[1] =
         * Integer.valueOf(reSeq); relDetObj[2] = bussTabId1; relDetObj[3] =
         * bussColId; relDetObj[4] = bussTabId2; relDetObj[5] = bussColId2;
         * relDetObj[6] = "inner"; relDetObj[7] = "="; relDetObj[8] = clause;
         * relDetObj[9] = ""; relDetObj[10] = ""; String finaddBussTabRelDetails
         * = buildQuery(addBussTabRelDetails,relDetObj);
         * finaddBussTabRelDetails ."+finaddBussTabRelDetails);
         * allQ.add(finaddBussTabRelDetails); } } }
         */
      

        try {
            Connection con = ProgenConnection.getInstance().getConnectionByGroupId(busGroup);
            PreparedStatement st = con.prepareStatement(finalCreateTable);
            boolean status = st.execute();
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }

            //createTableList
            executeMultiple(allQ);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        PbReturnObject returnObject = null;
        ArrayList uList = new ArrayList();
        String updateQ = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            returnObject = execSelectSQL("select ident_current('PRG_GRP_BUSS_TABLE_DETAILS')");
            draggedColNewId = "" + returnObject.getFieldValue(0, 0) + "";
            updateQ = "update PRG_TARGET_MASTER set TARGET_COLID=" + draggedColNewId + ",ACTUAL_COLID=" + draggedCol + " where prg_measure_id=" + tar_master;
        } else {
            updateQ = "update PRG_TARGET_MASTER set TARGET_COLID=" + draggedColNewId + ",ACTUAL_COLID=" + draggedCol + " where prg_measure_id= ident_current('PRG_TARGET_MASTER')";
        }

        uList.add(updateQ);
        executeMultiple(uList);

        // addTargetActualFormula(tabColumns,busGroup,draggedCol,draggedColNewId,draggedColName,tar_master);
    }

    // to show the facts from which measure is to be created
    public String getMeasureTables(String grpId) {
        String finalView = "";
        String sql = getResourceBundle().getString("getBusinessGroupFactsList");
        // sql = "select grp_id, grp_name  from prg_grp_master where grp_Id="+grpId;
        Object grpOb[] = new Object[2];
        grpOb[0] = grpId;
        grpOb[1] = grpId;
        StringBuffer outterBuffer = new StringBuffer();

        String finSql = buildQuery(sql, grpOb);
        PbReturnObject retObj = null;
        try {
            retObj = execSelectSQL(finSql);
            for (int i = 0; i < retObj.getRowCount(); i++) {
                String tabName = retObj.getFieldValueString(i, "BUSS_TABLE_NAME");
                String tabNId = retObj.getFieldValueString(i, "BUSS_TABLE_ID");
                outterBuffer.append("<li class='closed' onclick=\"dragTarget(this)\" id=\"" + tabNId + "\">");
                outterBuffer.append("<img src=\"images/treeViewImages/database_table.png\"></img> ");
                outterBuffer.append("<span id=" + tabName + " class=\"tarMeasureTableName\" onclick=\"dragTarget(this)\">" + tabName + "</span>");
                outterBuffer.append("<ul id=\"TargetTabName\">");

                outterBuffer.append(getMeasureColList(tabName, tabNId, grpId));

                outterBuffer.append("</ul>");
                outterBuffer.append("</li>");

            }
        } catch (SQLException e) {
            logger.error("Exception:", e);
        }

        return outterBuffer.toString();
    }
    //01-oct-09 added by susheela

    // to display the measures which are needed to be dragged for measure creation
    public String getMeasureColList(String tabName, String tabId, String grpId) {
        StringBuffer str = new StringBuffer();
        PbReturnObject pbro = new PbReturnObject();
        try {
            String query = getResourceBundle().getString("getMeasureCols");
            Object[] obj = new Object[1];
            obj[0] = tabId;

            String finalQry = buildQuery(query, obj);
            String selecMeasures = "select MEASURE_NAME from prg_target_master where business_group=" + grpId;
            PbReturnObject pbroMea = execSelectSQL(selecMeasures);
            pbro = execSelectSQL(finalQry);
            HashMap tabs = new HashMap();
            for (int i = 0; i < pbroMea.getRowCount(); i++) {
                String tName = pbroMea.getFieldValueString(i, "MEASURE_NAME");
                // tName = tName.substring(8);
                tabs.put(tName, "");
            }
            int count = pbro.getRowCount();
            for (int i = 0; i < count; i++) {
                String columnName = pbro.getFieldValueString(i, "COLUMN_NAME");
                String columnId = pbro.getFieldValueString(i, "BUSS_COLUMN_ID");
                String mColName = "TARGETM_" + columnName;
                if (mColName.length() > 32) {
                    mColName = mColName.substring(0, 31);
                }
                if (!tabs.containsKey(mColName)) {
                    str.append("<li id=\"" + tabId + "." + columnId + "\" value=\"" + tabId + "." + columnId + "." + tabName + "\">");
                    str.append("<span id=\"" + tabId + "~" + columnId + "~" + tabName + "~" + columnName + "\" class=\"dragTargetMeasures\">" + columnName + "</span>");
                    str.append("</li>");
                }
            }

        } catch (SQLException e) {
            logger.error("Exception:", e);
        }

        return str.toString();


    }

    public PbReturnObject getMeasureTargetParametersGroup(String busGroup) throws SQLException, Exception {
        String finalStr = "";
        PbReturnObject TargetObjNew = new PbReturnObject();
        String cols[] = {"MEMBER_NAME", "MEMBER_ID", "ELEMENT_ID"};
        TargetObjNew.setColumnNames(cols);
        String getTargetDimMembers = getResourceBundle().getString("getTargetDimMembersGroup");
        Object grpOb[] = new Object[1];
        grpOb[0] = busGroup;
        String fingetTargetDimMembers = buildQuery(getTargetDimMembers, grpOb);
        ////.println(" fingetTargetDimMembers . / / ." + fingetTargetDimMembers);
        PbReturnObject TargetObj = execSelectSQL(fingetTargetDimMembers);
        // String oldDimId=TargetObj.getFieldValueString(0,"KEY_DIM_ID");
//        String oldDimId = TargetObj.getFieldValueString(0, "DIM_ID");
//        Vector elementIds = new Vector();
//        Vector dims = new Vector();
        /*
         * for(int g=0;g<TargetObj.getRowCount();g++) { String dimId =
         * TargetObj.getFieldValueString(g,"KEY_BUSS_COL_NAME"); String eleId =
         * "";//TargetObj.getFieldValueString(g,"VAL_ELEMENT_ID");
         * if(!dims.contains(dimId)) { elementIds.add(eleId); dims.add(dimId); }
         *
         * }
         */
        for (int g = 0; g < TargetObj.getRowCount(); g++) {
            String eleId = "";//TargetObj.getFieldValueString(g,"VAL_ELEMENT_ID");
            TargetObjNew.setFieldValueString("MEMBER_ID", TargetObj.getFieldValueString(g, "MEMBER_ID"));
            TargetObjNew.setFieldValueString("MEMBER_NAME", TargetObj.getFieldValueString(g, "MEMBER_NAME"));
            TargetObjNew.setFieldValueString("ELEMENT_ID", "");

            // if(elementIds.contains(eleId))
            // {
            // //TargetObjNew.setFieldValueString("MEMBER_ID",TargetObj.getFieldValueString(g,"INFO_MEMBER_ID"));
            // TargetObjNew.setFieldValueString("MEMBER_NAME",TargetObj.getFieldValueString(g,"VAL_MEMBER_NAME"));
            // TargetObjNew.setFieldValueString("ELEMENT_ID",TargetObj.getFieldValueString(g,"VAL_ELEMENT_ID"));

            TargetObjNew.addRow();
            // }
        }
        TargetObjNew.writeString();

        return TargetObjNew;
    }

    public PbReturnObject getMoreMeasureParametersGroup(String busGroup, String measureId) throws SQLException, Exception {
        String finalStr = "";
        PbReturnObject moreObj = new PbReturnObject();
        String cols[] = {"MEMBER_ID", "MEMBER_NAME"};
        moreObj.setColumnNames(cols);
        String getTargetDimMembers = getResourceBundle().getString("getTargetDimMembersGroup");
        Object grpOb[] = new Object[1];
        grpOb[0] = busGroup;
        String fingetTargetDimMembers = buildQuery(getTargetDimMembers, grpOb);
        PbReturnObject TargetObj = execSelectSQL(fingetTargetDimMembers);

        //String alreadyMembersIdQ = " select * from prg_target_measure_members where measure_id="+measureId;
        String alreadyMembersIdQ = "select * from prg_target_measure_members where measure_id=(select prg_measure_id from prg_target_master where prg_target_master.business_group=" + busGroup + " and measure_name='" + measureId + "')";

        String measureIdQ = "select prg_measure_id from prg_target_master where prg_target_master.business_group=" + busGroup + " and measure_name='" + measureId + "'";
        PbReturnObject measureIdObj = execSelectSQL(measureIdQ);
        PbReturnObject extraM = execSelectSQL(alreadyMembersIdQ);
        Vector memIds = new Vector();
        for (int g = 0; g < extraM.getRowCount(); g++) {
            memIds.add(extraM.getFieldValueString(g, "MEMBER_ID"));
        }
        for (int u = 0; u < TargetObj.getRowCount(); u++) {
            String memId = TargetObj.getFieldValueString(u, "MEMBER_ID");
            if (!memIds.contains(memId)) {
                moreObj.setFieldValueString("MEMBER_ID", memId);
                moreObj.setFieldValueString("MEMBER_NAME", TargetObj.getFieldValueString(u, "MEMBER_NAME"));
                moreObj.addRow();
            }
        }
        moreObj.writeString();
        PbReturnObject allObj = new PbReturnObject();
        allObj.setObject("Measure", measureIdObj);
        allObj.setObject("MeasureMembers", moreObj);

        return allObj;
    }
}
