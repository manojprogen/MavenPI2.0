/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.business.group;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author arun
 */
public class BussColumnFormulaHelper {

    private ResourceBundle qryBundle;
    private ArrayList<BusinessColumn> formulaColLst;
    private String summarisationList[] = {"SUM", "AVG", "COUNT", "MAX", "MIN", "ROUND"};
    private String dbType;
    private String bussGrpId;
    private String connId;
    private int calculatedFolderId = -1;
    private int calculatedFolderSrcId;
    private String calculatedFolderName = "Calculated Facts";
    public static Logger logger = Logger.getLogger(BussColumnFormulaHelper.class);

    BussColumnFormulaHelper(ResourceBundle resBundle, String dbType, String grpId, String connId) {
        this.qryBundle = resBundle;
        this.formulaColLst = new ArrayList<BusinessColumn>();
        this.dbType = dbType;
        this.bussGrpId = grpId;
        this.connId = connId;
    }

    public BussColumnFormulaHelper() {
        this.formulaColLst = new ArrayList<BusinessColumn>();
    }

    void initializeBusinessColumns(String[] elementIds) {
        String dependentEleIds = "";
        for (String elementId : elementIds) {
            dependentEleIds += "," + elementId;
        }
        dependentEleIds = dependentEleIds.substring(1);

        PbDb pbDb = new PbDb();
        String bussColQry = qryBundle.getString("getBusinessColumnDetails");
        Object[] obj = new Object[1];
        obj[0] = dependentEleIds;
        String finalQry = pbDb.buildQuery(bussColQry, obj);
        try {
            PbReturnObject retObj = pbDb.execSelectSQL(finalQry);

            for (int i = 0; i < retObj.rowCount; i++) {
                BusinessColumn busCol = new BusinessColumn.BusinessColumnBuilder().build();
                busCol.bussTableId = retObj.getFieldValueInt(i, 0); //BUSS_TABLE_ID
                busCol.bussTableName = retObj.getFieldValueString(i, 1); //BUSS_TABLE_NAME
                busCol.bussColId = retObj.getFieldValueInt(i, 2); //BUSS_COL_ID
                busCol.bussColName = retObj.getFieldValueString(i, 3); //COLUMN_NAME
                busCol.bussColType = retObj.getFieldValueString(i, 4); //COLUMN_TYPE
                busCol.actualColFormula = retObj.getFieldValueString(i, 5); //ACTUAL_COL_FORMULA
                busCol.defaultAggregation = retObj.getFieldValueString(i, 6); //DEFAULT_AGGREGATION
                busCol.colDispName = retObj.getFieldValueString(i, 7); //COL_DISP_NAME
                busCol.colDispDesc = retObj.getFieldValueString(i, 8); //COL_DISP_DESC
                busCol.refElements = retObj.getFieldValueString(i, 9); //REFERRED_ELEMENTS
                this.initializeBusinessColumn(busCol);
            }

        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public void initializeBusinessColumn(BusinessColumn bussCol) {
        this.formulaColLst.add(bussCol);
    }

    public BusinessColumn evaluateFormula(BusinessColumn target) {
        String bussColumnNameForFormula;
        String columnName;
        target.bussColType = this.getColumnTypeForFormula();
        target.bussTableName = this.getBusinessTableForFormula();
        target.bussTableId = this.getBusinessTableId();
        //Single Table Formula
        if (this.isSingleTableFormula() && !isFactsFromCalculatedFolder()) {
            for (BusinessColumn formulaColumn : formulaColLst) {
                if (target.bussColType.equalsIgnoreCase("summarized")) {
                    target.refElements = target.refElements + "," + this.getRefferedElementsForFormulaColumn(formulaColumn);
                }

                columnName = formulaColumn.colDispName;//toUpperCase();
                if (isFactsNonSummarizedNonCalculated(formulaColumn)) {
                    //Column of Type Number
                    bussColumnNameForFormula = this.formulateBusinessColumn(formulaColumn, target);
                } else {
                    //Column of Type Summarized or Calculated
                    if (formulaColumn.bussColType.equalsIgnoreCase("summarized")) {
                        bussColumnNameForFormula = ("(" + formulaColumn.actualColFormula + ")");//.toUpperCase();
                    } else {
                        //calculated
                        bussColumnNameForFormula = this.formulateBusinessColumn(formulaColumn, target);

                    }
                }
                target.actualColFormula = target.actualColFormula.replace(columnName, bussColumnNameForFormula);
            }
        } else if (this.isSingleTableFormula() && isFactsFromCalculatedFolder()) {
            //calculated folder has only summarized facts
            for (BusinessColumn formulaColumn : formulaColLst) {
                target.refElements = target.refElements + "," + this.getRefferedElementsForFormulaColumn(formulaColumn);
                columnName = formulaColumn.colDispName;//.toUpperCase();
                bussColumnNameForFormula = (formulaColumn.actualColFormula);//.toUpperCase();
                target.actualColFormula = target.actualColFormula.replace(columnName, bussColumnNameForFormula);
            }
        } else {
            //multi table facts                 
            for (BusinessColumn formulaColumn : formulaColLst) {
                if (target.bussColType.equalsIgnoreCase("summarized")) {
                    target.refElements = target.refElements + "," + this.getRefferedElementsForFormulaColumn(formulaColumn);
                }

                columnName = formulaColumn.colDispName;//.toUpperCase();
                if (isFactsNonSummarizedNonCalculated(formulaColumn)) {
                    //Column of Type Number
                    bussColumnNameForFormula = this.formulateBusinessColumn(formulaColumn, target);
                } else {
                    //Column of Type Summarized or Calculated
                    if (formulaColumn.bussColType.equalsIgnoreCase("summarized")) {
                        bussColumnNameForFormula = ("(" + formulaColumn.actualColFormula + ")");//.toUpperCase();
                    } else {
                        //calculated
                        bussColumnNameForFormula = this.formulateBusinessColumn(formulaColumn, target);

                    }
                }
                target.actualColFormula = target.actualColFormula.replace(columnName, bussColumnNameForFormula);
            }
        }
        return target;
    }

    private String getRefferedElementsForFormulaColumn(BusinessColumn formulaColumn) {
        String refElements;
        if (formulaColumn.bussColType.equalsIgnoreCase("summarized")) {
            refElements = formulaColumn.refElements;
        } else {
            refElements = ((Integer) formulaColumn.bussColId).toString();
        }

        return refElements;
    }

    public void checkAndCreateCalculatedFactFolder() {
        PbDb pbDb = new PbDb();
        String checkCalcFolderQry = qryBundle.getString("checkCalculatedFolderExists");

        Object[] bind = new Object[1];
        bind[0] = this.bussGrpId;

        String finalQuery = pbDb.buildQuery(checkCalcFolderQry, bind);
        try {
            PbReturnObject calcFolderExists = pbDb.execSelectSQL(finalQuery);
            if (calcFolderExists.rowCount > 0) {
                this.calculatedFolderSrcId = calcFolderExists.getFieldValueInt(0, 0);
                this.calculatedFolderId = calcFolderExists.getFieldValueInt(0, 1);
            } else {
                this.createCalculatedFactFolder();
            }
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

    }

    private void createCalculatedFactFolder() {
        PbDb pbDb = new PbDb();
        ArrayList<String> qryList = new ArrayList<String>();
        try {
            String addFormulaBussMater = qryBundle.getString("addFormulaBussMater");
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {

//                        seqaddFormulaBussMater = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_SEQ.nextval from dual");
                Object obj[];
                obj = new Object[7];
//                        obj[0] = seqaddFormulaBussMater;
                obj[0] = "Calculated Facts";
                obj[1] = "Calculated Facts";
                obj[2] = "Table";
                obj[3] = "1";
                obj[4] = "null";
                obj[5] = "null";
                obj[6] = this.bussGrpId;
                String finalQuery = pbDb.buildQuery(addFormulaBussMater, obj);
                qryList.add(finalQuery);
                // //////////////////////////////////////////////////////////////.println.println("finalQuery---" + finalQuery);
                String addFormulaSrc = qryBundle.getString("addFormulaSrc");
//                        seqaddFormulaSrc = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_SRC_SEQ.nextval from dual");
                Object obj1[];
                obj1 = new Object[6];
//                        obj1[0] = seqaddFormulaSrc;
                obj1[0] = "ident_current('PRG_GRP_BUSS_TABLE')";
                obj1[1] = "0";
                obj1[2] = "Table";
                obj1[3] = "null";
                obj1[4] = connId;
                obj1[5] = "null";
                finalQuery = pbDb.buildQuery(addFormulaSrc, obj1);
                qryList.add(finalQuery);
            } else {
                this.calculatedFolderId = pbDb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_SEQ.nextval from dual");
                Object obj[];
                obj = new Object[8];
                obj[0] = calculatedFolderId;
                obj[1] = "Calculated Facts";
                obj[2] = "Calculated Facts";
                obj[3] = "Table";
                obj[4] = "1";
                obj[5] = "";
                obj[6] = "";
                obj[7] = this.bussGrpId;
                String finalQuery = pbDb.buildQuery(addFormulaBussMater, obj);
                qryList.add(finalQuery);

                String addFormulaSrc = qryBundle.getString("addFormulaSrc");
                this.calculatedFolderSrcId = pbDb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_SRC_SEQ.nextval from dual");
                Object obj1[];
                obj1 = new Object[7];
                obj1[0] = calculatedFolderSrcId;
                obj1[1] = calculatedFolderId;
                obj1[2] = "0";
                obj1[3] = "Table";
                obj1[4] = "";
                obj1[5] = connId;
                obj1[6] = "";
                finalQuery = pbDb.buildQuery(addFormulaSrc, obj1);
                qryList.add(finalQuery);

                pbDb.executeMultiple(qryList);
            }
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    private String getBusinessTableForFormula() {
        if (this.isSingleTableFormula() && !isFactsFromCalculatedFolder()) {
            return formulaColLst.get(0).bussTableName;
        } else {
            return this.calculatedFolderName;
        }
    }

    public String getColumnTypeForFormula() {
        HashSet<String> aggSet = new HashSet<String>();
        HashSet<String> colTypeSet = new HashSet<String>();
        String columnType = null;

        if (this.isSingleTableFormula() && !isFactsFromCalculatedFolder()) {
            for (BusinessColumn formulaColumn : formulaColLst) {
                colTypeSet.add(formulaColumn.bussColType);
            }

            if (colTypeSet.size() == 1) {
                String formulaColType = colTypeSet.iterator().next();
                if (formulaColType.equalsIgnoreCase("Number") || formulaColType.equalsIgnoreCase("float") || formulaColType.equalsIgnoreCase("int")) {
                    //check is formula has only Sum Agg
                    for (BusinessColumn formulaColumn : formulaColLst) {
                        aggSet.add(formulaColumn.defaultAggregation);
                    }

                    if (aggSet.size() == 1) {
                        String aggType = aggSet.iterator().next();
                        if (aggType.equalsIgnoreCase("SUM") || aggType.equalsIgnoreCase("AVG")) {
                            columnType = "calculated";
                        } else {
                            columnType = "summarized";
                        }
                    }
                } else if (formulaColType.equalsIgnoreCase("summarized")) {
                    columnType = "summarized";
                } else if (formulaColType.equalsIgnoreCase("calculated")) {
                    columnType = "calculated";
                } else {
                    //Type VARCHAR or DATE
                    columnType = "summarized";
                }
            } else {

                columnType = "summarized";
            }
        } else {
            columnType = "summarized";
        }
        return columnType;
    }

    public boolean isSingleTableFormula() {
        Set<Integer> tableIdSet = this.getBusinessTablesInFormula();

        if (tableIdSet.size() > 1) {
            return false;
        } else {
            return true;
        }
    }

    private Set<Integer> getBusinessTablesInFormula() {
        Set<Integer> tableIdSet = new HashSet<Integer>();
        for (BusinessColumn busCol : formulaColLst) {
            tableIdSet.add(busCol.bussTableId);
        }
        return tableIdSet;

    }

    private boolean isFactsFromCalculatedFolder() {

        for (BusinessColumn busCol : formulaColLst) {
            if ("Calculated Facts".equals(busCol.bussTableName)) {
                return true;
            }
        }

        return false;
    }

    private boolean isFactsNonSummarizedNonCalculated(BusinessColumn busCol) {
        if (busCol.bussColType.equalsIgnoreCase("summarized")
                || busCol.bussColType.equalsIgnoreCase("summarised")
                || busCol.bussColType.equalsIgnoreCase("calculated")) {
            return false;
        } else {
            return true;
        }

    }

    private String formulateBusinessColumn(BusinessColumn formulaColumn, BusinessColumn target) {
        String bussColName = null;
        if (formulaColumn.bussColType.equalsIgnoreCase("NUMBER") || formulaColumn.bussColType.equalsIgnoreCase("INT") || formulaColumn.bussColType.equalsIgnoreCase("FLOAT")) {
            if (target.bussColType.equalsIgnoreCase("summarized")) {
                if ("COUNT".equalsIgnoreCase(formulaColumn.defaultAggregation) || "COUNTDISTINCT".equalsIgnoreCase(formulaColumn.defaultAggregation)) {
                    bussColName = ("SUM" + "(" + formulaColumn.colDispName + ")");//.toUpperCase();
                } else {
                    bussColName = (formulaColumn.defaultAggregation + "(" + formulaColumn.colDispName + ")");//.toUpperCase();
                }
            } else {
                bussColName = ("(" + formulaColumn.bussTableName + "." + formulaColumn.bussColName + ")");//.toUpperCase();
            }
        } else if (formulaColumn.bussColType.equalsIgnoreCase("calculated")) {
            if (target.bussColType.equalsIgnoreCase("summarized")) {
                bussColName = (formulaColumn.defaultAggregation + "(" + formulaColumn.colDispName + ")");//.toUpperCase();
            } else {
                bussColName = ("(" + formulaColumn.actualColFormula + ")");//.toUpperCase();
            }
        } else //Type of Varchar/Date etc.
        {
            String aggColumnName = "COUNT(" + (formulaColumn.bussTableName + "." + formulaColumn.bussColName) + ")";
            bussColName = aggColumnName;
        }
        return bussColName;
    }

    private String getInsertFormulaSrcDetailsQry(BusinessColumn target) {
        String addFormulaSrcDetails = qryBundle.getString("addFormulasrcDetails");
        String finalQuery = null;
        PbDb pbDb = new PbDb();
        if (dbType.equals(ProgenConnection.ORACLE)) {
            try {
                Object obj2[] = new Object[7];
                obj2[0] = pbDb.getSequenceNumber("select PRG_GRP_BUSS_TAB_SRC_DTLS_SEQ.nextval from dual");
                obj2[1] = target.bussSrcId;
                obj2[2] = "0";
                obj2[3] = target.bussTableId;
                obj2[4] = target.bussColName;
                obj2[5] = target.bussColType;
                obj2[6] = "0";

                finalQuery = pbDb.buildQuery(addFormulaSrcDetails, obj2);
            } catch (Exception ex) {
                logger.error("Exception: ", ex);
            }
        } else {

            Object obj2[] = new Object[6];
//                    obj2[0] = srcdetnextval;
            obj2[0] = target.bussSrcId;
            obj2[1] = "0";
            obj2[2] = target.bussTableId;
            obj2[3] = target.bussColName;
            obj2[4] = target.bussColType;
            obj2[5] = "0";

            finalQuery = pbDb.buildQuery(addFormulaSrcDetails, obj2);

        }
        return finalQuery;
    }

    private String getInsertFormulaBussTableDetailsQry(BusinessColumn target) {
        PbDb pbDb = new PbDb();
        String addFormulaBussDetails = qryBundle.getString("addFormulaBussDetails");
        String finalQuery = null;
        if (dbType.equals(ProgenConnection.ORACLE)) {
            try {
                Object obj3[] = new Object[16];
                int busDetailsSeq = pbDb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_DETAILS_SEQ.nextval from dual");
                obj3[0] = busDetailsSeq;
                obj3[1] = target.bussTableId;
                obj3[2] = target.bussColName;
                obj3[3] = "0";
                obj3[4] = pbDb.getCurrentSequenceNumber("select PRG_GRP_BUSS_TAB_SRC_DTLS_SEQ.currval from dual");
                ;
                obj3[5] = target.bussColType;// "calculated";
                obj3[6] = "0";
//                obj3[7] = target.colDispName;
                obj3[7] = "B_" + busDetailsSeq;
                obj3[8] = "0";
                obj3[9] = "N";
                obj3[10] = target.actualColFormula;
                obj3[11] = target.defaultAggregation;
                obj3[12] = target.colDispDesc;
                if ("".equals(target.refElements)) {
                    obj3[13] = target.refElements;
                } else {
                    obj3[13] = target.refElements.substring(1);
                }
                obj3[14] = "Y";
                obj3[15] = target.displayFormula;
                finalQuery = pbDb.buildQuery(addFormulaBussDetails, obj3);
            } catch (Exception e) {
                logger.error("Exception: ", e);
            }
        } else {
//                        seqaddFormulaBussDetails = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_DETAILS_SEQ.nextval from dual");
            Object obj3[] = new Object[15];
//                    obj3[0] = seqaddFormulaBussDetails;
            obj3[0] = target.bussTableId;
            obj3[1] = target.bussColName;
            obj3[2] = "0";
            obj3[3] = "IDENT_CURRENT('PRG_GRP_BUSS_TAB_SRC_DTLS')";
            obj3[4] = target.bussColType;// "calculated";
            obj3[5] = "0";
            obj3[6] = target.colDispName;
            obj3[7] = "0";
            obj3[8] = "N";
            obj3[9] = target.actualColFormula;
            obj3[10] = target.defaultAggregation;
            obj3[11] = target.colDispDesc;
            if ("".equals(target.refElements)) {
                obj3[12] = target.refElements;
            } else {
                obj3[12] = target.refElements.substring(1);
            }
            obj3[13] = "Y";
            obj3[14] = target.displayFormula;
            finalQuery = pbDb.buildQuery(addFormulaBussDetails, obj3);
        }
        return finalQuery;
    }

    public void insertFormula(BusinessColumn target) {
        ArrayList<String> insertQryLst = new ArrayList<String>();
        insertQryLst.add(this.getInsertFormulaSrcDetailsQry(target));
        insertQryLst.add(this.getInsertFormulaBussTableDetailsQry(target));
        if (!dbType.equals(ProgenConnection.ORACLE)) {

            insertQryLst.add("update  PRG_GRP_BUSS_TABLE_DETAILS   set  COLUMN_DISP_NAME ='B_'+cast(IDENT_CURRENT('PRG_GRP_BUSS_TAB_SRC_DTLS') as varchar(258)) where BUSS_COLUMN_ID= IDENT_CURRENT('PRG_GRP_BUSS_TABLE_DETAILS') ");
        }

        PbDb pbDb = new PbDb();
        try {
            pbDb.executeMultiple(insertQryLst);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

    }

    private int getTableSrcId() {
        String getBussSrcIdQry = qryBundle.getString("getBusinessTableSrcId");
        Object bind[] = new Object[1];
        String bussTables = "";
        int srcId = 0;
        PbDb pbDb = new PbDb();

        for (Integer bussTableId : this.getBusinessTablesInFormula()) {
            bussTables = bussTables + "," + bussTableId.toString();
        }
        bind[0] = bussTables.substring(1);
        String finalQuery = pbDb.buildQuery(getBussSrcIdQry, bind);

        PbReturnObject srcDetails = null;
        try {
            srcDetails = pbDb.execSelectSQL(finalQuery);
            srcId = srcDetails.getFieldValueInt(0, 0);
        } catch (Exception e) {
        }

        return srcId;
    }

    private int getBusinessTableId() {
        int bussTableId = 0;
        if (this.isSingleTableFormula() && !this.isFactsFromCalculatedFolder()) {
            bussTableId = formulaColLst.get(0).bussTableId;
        } else {
            bussTableId = this.calculatedFolderId;
        }
        return bussTableId;
    }
}
