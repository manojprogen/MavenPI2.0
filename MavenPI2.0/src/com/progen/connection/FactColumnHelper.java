package com.progen.connection;

import com.progen.userlayer.db.SavePublishUserFolder;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

public class FactColumnHelper extends PbDb {

    public static Logger logger = Logger.getLogger(FactColumnHelper.class);
    ConnectionResourceBundle resBundle = new ConnectionResourceBundle();

    public boolean insertColumnToDbTable(int tableid, String[] tabColumns, String[] tabColtype, String[] tabColLength) throws Exception {
        String insertColumnToDbTable = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            insertColumnToDbTable = resBundle.getString("insertColumnToDbTableforSQL");
        } else {
            insertColumnToDbTable = resBundle.getString("insertColumnToDbTable");
        }
        ArrayList queryList = new ArrayList();
        boolean result = false;
        try {

            Object[] object = new Object[7];
            String finalQuery = "";
            object[0] = tableid;
            for (int i = 0; i < tabColumns.length; i++) {
                object[1] = tabColumns[i];
                object[2] = tabColumns[i];
                object[3] = tabColtype[i];
//                object[4] = tabColLength[i];
                if (tabColtype[i].equalsIgnoreCase("datetime") || tabColLength[i].equalsIgnoreCase("")) {
                    object[4] = "null";
                } else {
                    object[4] = tabColLength[i];
                }
                object[5] = "N";
                object[6] = "Y";
                finalQuery = super.buildQuery(insertColumnToDbTable, object);
                queryList.add(finalQuery);
            }
            result = super.executeMultiple(queryList);
            return result;
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return result;
    }

    public PbReturnObject checkInBussGrp(int tableId) {
        PbReturnObject resultReturnObject = new PbReturnObject();
        String checkInBussGrp = resBundle.getString("checkInBussGrp");
        String finalquery = "";
        Object object[] = new Object[1];
        object[0] = tableId;
        finalquery = super.buildQuery(checkInBussGrp, object);
        try {
            resultReturnObject = super.execSelectSQL(finalquery);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return resultReturnObject;
    }

    public PbReturnObject checkInBussSrc(int bussTabID) {
        PbReturnObject resultReturnObject = new PbReturnObject();
        String checkInBussGrp = resBundle.getString("checkInBussSrc");
        String finalquery = "";
        Object object[] = new Object[1];
        object[0] = bussTabID;
        finalquery = super.buildQuery(checkInBussGrp, object);
        try {
            resultReturnObject = super.execSelectSQL(finalquery);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return resultReturnObject;
    }

    public HashMap getColumnDetails(String[] columns, String tabId) {
        String getColumnDetails = resBundle.getString("getColumnDetails");
        String finalQuery = "";
        PbReturnObject pbReturnObject = new PbReturnObject();
        Object object[] = new Object[2];
//        String tempString = "";
        StringBuilder tempString = new StringBuilder(200);
        HashMap resultMap = new HashMap();
        for (int i = 0; i < columns.length; i++) {
            tempString.append("'").append(columns[i]).append("'");
//            tempString += "'" + columns[i] + "'";
            if (i < columns.length - 1) {
//                tempString += ",";
                tempString.append(",");
            }
        }
        object[0] = tabId;
        object[1] = tempString;
        finalQuery = super.buildQuery(getColumnDetails, object);
        try {
            pbReturnObject = super.execSelectSQL(finalQuery);
            if (pbReturnObject.getRowCount() > 0) {
                for (int j = 0; j < pbReturnObject.getRowCount(); j++) {
                    resultMap.put(pbReturnObject.getFieldValueString(j, 2), pbReturnObject.getFieldValueString(j, 0));
                }
            }

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return resultMap;
    }

    public boolean insertBussGrpSrc(FactColumn factColumn) {
        boolean result = false;
        String insertBussGrpSrc = "";
        String insertInGrpDetails = "";
        String updateBussTableDetails = resBundle.getString("updateBussTableDetails");
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            insertBussGrpSrc = resBundle.getString("insertBussGrpSrcforSQL");
            insertInGrpDetails = resBundle.getString("insertInGrpDetailsforSQL");
            updateBussTableDetails = resBundle.getString("updateBussTableDetails2");
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            insertBussGrpSrc = resBundle.getString("insertBussGrpSrcforSQL");
            insertInGrpDetails = resBundle.getString("insertInGrpDetailsforMYSQL");
            updateBussTableDetails = resBundle.getString("updateBussTableDetails1");
        } else {
            insertBussGrpSrc = resBundle.getString("insertBussGrpSrc");
            insertInGrpDetails = resBundle.getString("insertInGrpDetails");
        }

        String finalQuery = "";
        ArrayList queryList = new ArrayList();
        String columnNamesArray[] = factColumn.getColsNames();
        String columnTypesArray[] = factColumn.getColsTypes();
        Object object[] = new Object[6];
        Object object1[] = new Object[9];
        for (int i = 0; i < columnNamesArray.length; i++) {
            object[0] = factColumn.getBussGrpSrcId();
            object[1] = factColumn.getDbTableid();
            object[2] = factColumn.getBussTableId();
            object[3] = factColumn.getColumnId(columnNamesArray[i]);
            object[4] = columnNamesArray[i];
            object[5] = columnTypesArray[i];
            finalQuery = super.buildQuery(insertBussGrpSrc, object);
            queryList.add(finalQuery);
            object1[0] = factColumn.getBussTableId();
            object1[1] = columnNamesArray[i];
            object1[2] = columnTypesArray[i];
            object1[3] = factColumn.getDbTableid();
            object1[4] = factColumn.getColumnId(columnNamesArray[i]);
            object1[5] = "N";
            object1[6] = columnNamesArray[i];
            object1[7] = columnNamesArray[i].replaceAll("_", " ");
            object1[8] = "Y";
            finalQuery = super.buildQuery(insertInGrpDetails, object1);
            queryList.add(finalQuery);
        }
        Object object2[] = new Object[1];
        object2[0] = factColumn.getBussTableId();
        finalQuery = super.buildQuery(updateBussTableDetails, object2);
        queryList.add(finalQuery);
        try {

            result = super.executeMultiple(queryList);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return result;
    }

    public PbReturnObject checkInBussRole(int bussTabID) {
        PbReturnObject resultReturnObject = new PbReturnObject();
        String checkInBussRole = resBundle.getString("checkInBussRole");
        String finalquery = "";
        Object object[] = new Object[1];
        object[0] = bussTabID;
        finalquery = super.buildQuery(checkInBussRole, object);
        try {
            resultReturnObject = super.execSelectSQL(finalquery);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return resultReturnObject;
    }

    public boolean insertInBusRole(FactColumn factColumn) {
        String insertInBusRole = "";
        String finalQuery = "";
        ArrayList queryList = new ArrayList();
        Object object[] = new Object[12];
        boolean returnrB = false;
        ArrayList folderArrayList = factColumn.getSubFolderIds();
        String columnnames[] = factColumn.getColsNames();
        String columntypes[] = factColumn.getColsTypes();
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            insertInBusRole = resBundle.getString("insertInBusRoleforSQL");
            try {


                for (int i = 0; i < folderArrayList.size(); i++) {
                    for (int j = 0; j < columnnames.length; j++) {
                        // int seq = super.getSequenceNumber("select PRG_USER_SUB_FLDR_ELEMENTS_SEQ.nextval from dual");
                        //object[0] = seq;
                        object[0] = folderArrayList.get(i);
                        object[1] = factColumn.getBussTableId();
                        object[2] = factColumn.getColumnId(columnnames[j]);
                        object[3] = columnnames[j];
                        object[4] = columnnames[j];
                        object[5] = columnnames[j].replaceAll("_", " ");
                        object[6] = columntypes[j];
                        //object[8] = seq;
                        object[7] = 1;
                        object[8] = factColumn.getSubFolderTabId(folderArrayList.get(i).toString());
                        object[9] = "Y";
                        finalQuery = super.buildQuery(insertInBusRole, object);
                        queryList.add(finalQuery);
                    }
                }

                returnrB = super.executeMultiple(queryList);
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }

        } else {
            insertInBusRole = resBundle.getString("insertInBusRole");


            try {


                for (int i = 0; i < folderArrayList.size(); i++) {
                    for (int j = 0; j < columnnames.length; j++) {
                        int seq = super.getSequenceNumber("select PRG_USER_SUB_FLDR_ELEMENTS_SEQ.nextval from dual");
                        object[0] = seq;
                        object[1] = folderArrayList.get(i);
                        object[2] = factColumn.getBussTableId();
                        object[3] = factColumn.getColumnId(columnnames[j]);
                        object[4] = columnnames[j];
                        object[5] = columnnames[j];
                        object[6] = columnnames[j].replaceAll("_", " ");
                        object[7] = columntypes[j];
                        object[8] = seq;
                        object[9] = 1;
                        object[10] = factColumn.getSubFolderTabId(folderArrayList.get(i).toString());
                        object[11] = "Y";
                        finalQuery = super.buildQuery(insertInBusRole, object);
                        queryList.add(finalQuery);
                    }
                }

                returnrB = super.executeMultiple(queryList);
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }

        }
        return returnrB;
    }

    public String getFolderDetails(FactColumn factColumn) {
        String getFolderDetails = resBundle.getString("getFolderDetails");
        String queryFromAllInfo = resBundle.getString("queryFromAllInfo");
        String resultString = "";
        String finalQuery = "";
        HashMap flodedrDetails = new HashMap();
        PbReturnObject pbReturnObject = new PbReturnObject();
        Object object[] = new Object[1];
        ArrayList subFolderIds = factColumn.getSubFolderIds();
        object[0] = subFolderIds.toString().replace("[", "").replace("]", "");
        finalQuery = super.buildQuery(getFolderDetails, object);

        int[] folderIds;
        String[] folderNames;
        HashMap<Integer, Boolean> publishDetails = new HashMap<Integer, Boolean>();

        try {
            pbReturnObject = super.execSelectSQL(finalQuery);
            folderIds = new int[pbReturnObject.getRowCount()];
            folderNames = new String[pbReturnObject.getRowCount()];
            for (int i = 0; i < pbReturnObject.getRowCount(); i++) {
                folderIds[i] = pbReturnObject.getFieldValueInt(i, 0);
                folderNames[i] = pbReturnObject.getFieldValueString(i, 1);
                ArrayList arrayList = new ArrayList();
                arrayList.add(pbReturnObject.getFieldValueString(i, 11));
                arrayList.add(pbReturnObject.getFieldValueString(i, 12));
                flodedrDetails.put(pbReturnObject.getFieldValueString(i, 10), arrayList);
            }
            factColumn.setFolderID(folderIds);
            factColumn.setFoldername(folderNames);
            factColumn.setFlodedrDetails(flodedrDetails);
//            String tempString1 = "";
//            String tempString2 = "";
            StringBuilder tempString1 = new StringBuilder();
            StringBuilder tempString2 = new StringBuilder();
            for (int j = 0; j < folderIds.length; j++) {
                object[0] = folderIds[j];
                finalQuery = super.buildQuery(queryFromAllInfo, object);
//                tempString1 += "\"" + folderNames[j] + "\"";
                tempString1.append("\"").append(folderNames[j]).append("\"");
                pbReturnObject = super.execSelectSQL(finalQuery);
                if (pbReturnObject.getRowCount() > 0) {
                    publishDetails.put(folderIds[j], Boolean.TRUE);
//                    tempString2 += "\"True\"";
                    tempString2.append("\"True\"");
                } else {
                    publishDetails.put(folderIds[j], Boolean.FALSE);
//                    tempString2 += "\"False\"";
                    tempString2.append("\"False\"");
                }
                if (j < folderIds.length - 1) {
//                    tempString1 += ",";
//                    tempString2 += ",";
                    tempString1.append(",");
                    tempString2.append(",");
                }
            }

            resultString += "{folderName:[" + tempString1.toString() + "],isPublish:[" + tempString2.toString() + "],busstableName:[\"" + factColumn.getBussTableName() + "\"]}";

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return resultString;

    }

    public String getGrpDetails(int bussTabID) {
        String query = "select * from prg_grp_master where grp_id=(select grp_id from prg_grp_buss_table where buss_table_id =" + bussTabID + ")";
        String resultStr = "";
//        String tempString1 = "";
//        String tempString2 = "";
        StringBuilder tempString1 = new StringBuilder(200);
        StringBuilder tempString2 = new StringBuilder(200);
        try {
            PbReturnObject pbReturnObject = super.execSelectSQL(query);
            if (pbReturnObject.getRowCount() > 0) {

                for (int i = 0; i < pbReturnObject.getRowCount(); i++) {
                    tempString1.append("\"").append(pbReturnObject.getFieldValueString(i, "GRP_ID")).append("\"");
//                    tempString1 += "\"" + pbReturnObject.getFieldValueString(i, "GRP_ID") + "\"";
                    tempString1.append("\"").append(pbReturnObject.getFieldValueString(i, "GRP_NAME")).append("\"");
//                    tempString2 += "\"" + pbReturnObject.getFieldValueString(i, "GRP_NAME") + "\"";
                    if (i < pbReturnObject.getColumnCount() - 1) {
//                        tempString1 += ",";
//                        tempString2 += ",";
                        tempString1.append(",");
                        tempString2.append(",");
                    }
                }
            }
            resultStr = "{grpId:[" + tempString1 + "],grpName:[" + tempString2 + "]}";
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return resultStr;
    }

    public boolean saveAndPublishRole(FactColumn factColumn, String rolename) {
//        String saveAndPublishRole = resBundle.getString("saveAndPublishRole");
        boolean check = false;
        String[] roleNameList = factColumn.getFoldername();
        int floderId[] = factColumn.getFolderID();
        SavePublishUserFolder savePublishUserFolder = new SavePublishUserFolder();
        for (int i = 0; i < roleNameList.length; i++) {
            if (roleNameList[i].equalsIgnoreCase(rolename)) {

                check = savePublishUserFolder.publishUserFolder(Integer.toString(floderId[i]));
            }
        }

        return check;
    }
//   public  void  getBussGrpDetails(FactColumn factColumn) {
//       String getBussGrpDetails=resBundle.getString("getBussGrpDetails");
//
//    }
}
