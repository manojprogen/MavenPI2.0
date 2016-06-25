package com.progen.Createtable;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

class TruncateTableDAO extends PbDb {

    public static Logger logger = Logger.getLogger(TruncateTableDAO.class);
    CreateTableResourceBundle resBundle = new CreateTableResourceBundle();
    private String inputFile = " ";

    public boolean truncateTable(String delTableIds, String filepath, String sheetnum) {
        String selectTableNames = resBundle.getString("selectTableNames");
        String truncateQuery = resBundle.getString("truncateQuery");
        String selectmasTableDetails = resBundle.getString("selectmasTableDetails1");
        Object[] objects = new Object[1];
        PbReturnObject pbSelDetails = null;
        String finalQuery = "";
        Connection connection = null;
        ArrayList queryList = new ArrayList();
        String[] strColn = null;
        String sheetName = "";
        boolean checkboo = false;
        try {
            objects[0] = delTableIds;
            finalQuery = buildQuery(selectTableNames, objects);
            //
            pbSelDetails = execSelectSQL(finalQuery);
            //////
            for (int tcount = 0; tcount < pbSelDetails.getRowCount(); tcount++) {
                objects[0] = pbSelDetails.getFieldValue(tcount, 0);
                finalQuery = buildQuery(truncateQuery, objects);
                // finalQuery1 = buildQuery(dropSequence, objects);
                queryList.add(finalQuery);
            }
            connection = ProgenConnection.getInstance().getCustomerConn();
            //
            boolean check = executeMultiple(queryList, connection);
            //
            CreatetableDAO truncateTableDAO = new CreatetableDAO();


            String[] strIds = delTableIds.split(",");

            for (int selCount = 0; selCount < strIds.length; selCount++) {
                objects[0] = strIds[selCount];
                finalQuery = buildQuery(selectmasTableDetails, objects);
                PbReturnObject tempReturnObject = execSelectSQL(finalQuery);
                if (tempReturnObject.getFieldValue(0, "IS_FACT").toString().equalsIgnoreCase("Y")) {
                    strColn = new String[tempReturnObject.getRowCount()];
                    for (int i = 0; i < tempReturnObject.getRowCount(); i++) {
                        strColn[i] = tempReturnObject.getFieldValue(i, "COLUMN_NAMES").toString();
                        sheetName = tempReturnObject.getFieldValue(0, "TABLE_NAME").toString().toUpperCase();
                    }

                } else {
                }


            }

            int sheetnum1 = Integer.parseInt(sheetnum) - 1;
            truncateTableDAO.setInputFile(filepath);
            truncateTableDAO.alertinsertData(strColn, sheetName, sheetnum1);
            TruncateTableDAO truncateTableDAO1 = new TruncateTableDAO();
            truncateTableDAO1.createDimTable(delTableIds, sheetName);

            checkboo = true;
        } catch (Exception e) {
            logger.error("Exception:", e);
            checkboo = false;
        }
        return checkboo;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getInputFile() {
        return this.inputFile;
    }

    private void createDimTable(String delTableIds, String sheetName) throws Exception {
        //
        ArrayList queryList = new ArrayList();
        String selectTableNames = resBundle.getString("selectTableNames");
//        String SelDBTableDetails = resBundle.getString("SelDBTableDetails");
        String colmsFromdb = resBundle.getString("colmsFromdb");
        Object[] object = new Object[1];
        String finalQuery = "";
        String deleteSeq = resBundle.getString("dropSequence");
        String createSqquence = resBundle.getString("createSqquence");
        Connection connection = null;
        ArrayList dimTabNames = null;
        HashMap tableColmsHm = new HashMap();
        try {
            object[0] = delTableIds;
            finalQuery = buildQuery(selectTableNames, object);
            PbReturnObject pbRetur = execSelectSQL(finalQuery);

            String strtabNames = "";
            dimTabNames = new ArrayList();
            for (int check = 0; check < pbRetur.rowCount; check++) {
                if (!pbRetur.getFieldValue(check, "TABLE_NAME").toString().equalsIgnoreCase(sheetName)) {
                    strtabNames += ",'" + pbRetur.getFieldValue(check, "TABLE_NAME").toString() + "'";
                    dimTabNames.add(pbRetur.getFieldValue(check, "TABLE_NAME").toString());
                    object[0] = pbRetur.getFieldValue(check, "TABLE_NAME").toString() + "_SEQ";
                    finalQuery = buildQuery(deleteSeq, object);
                    queryList.add(finalQuery);

                }
            }
            strtabNames = strtabNames.substring(1);
            object[0] = strtabNames;
            finalQuery = buildQuery(colmsFromdb, object);
            connection = ProgenConnection.getInstance().getCustomerConn();
            PbReturnObject pbRetuselDe = execSelectSQL(finalQuery, connection);
            connection.close();
            ArrayList tempArrayList = null;
            for (int counArr = 0; counArr < dimTabNames.size(); counArr++) {
                object[0] = dimTabNames.get(counArr).toString().trim() + "_SEQ";
                finalQuery = buildQuery(createSqquence, object);
                queryList.add(finalQuery);
                tempArrayList = new ArrayList();
                for (int inseTab = 0; inseTab < pbRetuselDe.getRowCount(); inseTab++) {
                    if (pbRetuselDe.getFieldValue(inseTab, "TABLE_NAME").toString().equalsIgnoreCase(dimTabNames.get(counArr).toString().trim())) {
                        tempArrayList.add(pbRetuselDe.getFieldValue(inseTab, "COLUMN_NAME"));

                    }
                }
                tableColmsHm.put(dimTabNames.get(counArr), tempArrayList);

            }
            connection = ProgenConnection.getInstance().getCustomerConn();

            boolean check1 = executeMultiple(queryList, connection);
            //

            TruncateTableDAO truncateTableDAO = new TruncateTableDAO();
            truncateTableDAO.insertDimTables(tableColmsHm, sheetName);
            connection.close();
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }

    private void insertDimTables(HashMap tableColmsHm, String sheetName) throws Exception {
        ArrayList queryList = new ArrayList();
        String[] keySet = (String[]) tableColmsHm.keySet().toArray(new String[0]);
        PbReturnObject selQueryDetails = null;
        String finalQuery = "";
        ArrayList tempArray = null;
        Connection connection = ProgenConnection.getInstance().getCustomerConn();
        connection.close();
        StringBuffer sbinsert = null;
        String selectQuery = resBundle.getString("selectQuery");
        try {
            for (int kCou = 0; kCou < keySet.length; kCou++) {
                tempArray = (ArrayList) tableColmsHm.get(keySet[kCou]);
                tempArray.remove(keySet[kCou] + "_ID");
                sbinsert = new StringBuffer("");
                sbinsert.append("INSERT INTO  ");
                sbinsert.append(keySet[kCou].trim());
                sbinsert.append(" (");
                sbinsert.append(keySet[kCou].trim() + "_id ," + tempArray.toString().replace("[", "").replace("]", ""));
                sbinsert.append(" )");
                sbinsert.append(" SELECT " + keySet[kCou].trim() + "_seq.nextval ," + tempArray.toString().replace("[", "").replace("]", ""));
                sbinsert.append(" FROM");
                sbinsert.append("( SELECT DISTINCT " + tempArray.toString().replace("[", "").replace("]", ""));
                sbinsert.append(" FROM " + sheetName.toUpperCase().trim() + ")");
                queryList.add(sbinsert.toString());

            }

            connection = ProgenConnection.getInstance().getCustomerConn();
            executeMultiple(queryList, connection);
            connection.close();
        } catch (Exception e) {
            logger.error("Exception:", e);
        }



    }

    boolean addDatainDb(String delTableIds, String filepath, String sheetnum) throws Exception {
        PbReturnObject pbSelDetails = null;
        String truncateDimTabs = resBundle.getString("truncateDimTabs");
        String selectTableNames = resBundle.getString("selectTableNames");
        String truncateQuery = resBundle.getString("truncateQuery");
        String selectmasTableDetails = resBundle.getString("selectmasTableDetails1");
        Object[] objects = new Object[1];
        String finalQuery = "";
        Connection connection = null;
        ArrayList queryList = new ArrayList();
        String[] strColn = null;
        String sheetName = "";
        boolean checkboo = false;
        logger.info("delTableIds\t" + delTableIds);
        pbSelDetails = execSelectSQL(truncateDimTabs);
        String[] tabIds = delTableIds.split(",");
        ArrayList dimTabids = new ArrayList();
        for (int intstrcount = 0; intstrcount < tabIds.length; intstrcount++) {
            for (int retCount = 0; retCount < pbSelDetails.getRowCount(); retCount++) {
                if (!tabIds[intstrcount].equalsIgnoreCase(pbSelDetails.getFieldValue(retCount, "DBMASTER_TABLE_ID").toString())) {
                    dimTabids.add(tabIds[intstrcount]);
                }
            }
        }
        logger.info("dimTabids\t" + dimTabids);
        try {
            objects[0] = dimTabids.toString().replace("[", "").replace("]", "");
            finalQuery = buildQuery(selectTableNames, objects);
            logger.info("finalQuery:\t" + finalQuery);
            pbSelDetails = execSelectSQL(finalQuery);
            ////
            for (int tcount = 0; tcount < pbSelDetails.getRowCount(); tcount++) {
                objects[0] = pbSelDetails.getFieldValue(tcount, 0);
                finalQuery = buildQuery(truncateQuery, objects);
                // finalQuery1 = buildQuery(dropSequence, objects);
                queryList.add(finalQuery);
            }
            connection = ProgenConnection.getInstance().getCustomerConn();
            logger.info("queryList:\t" + queryList);
            boolean check = executeMultiple(queryList, connection);
            logger.info("check:\t" + check);
            CreatetableDAO truncateTableDAO = new CreatetableDAO();


            String[] strIds = delTableIds.split(",");

            for (int selCount = 0; selCount < strIds.length; selCount++) {
                objects[0] = strIds[selCount];
                finalQuery = buildQuery(selectmasTableDetails, objects);
                PbReturnObject tempReturnObject = execSelectSQL(finalQuery);
                if (tempReturnObject.getFieldValue(0, "IS_FACT").toString().equalsIgnoreCase("Y")) {
                    strColn = new String[tempReturnObject.getRowCount()];
                    for (int i = 0; i < tempReturnObject.getRowCount(); i++) {
                        strColn[i] = tempReturnObject.getFieldValue(i, "COLUMN_NAMES").toString();
                        sheetName = tempReturnObject.getFieldValue(0, "TABLE_NAME").toString().toUpperCase();
                    }

                } else {
                }


            }

            int sheetnum1 = Integer.parseInt(sheetnum) - 1;
            truncateTableDAO.setInputFile(filepath);
            truncateTableDAO.alertinsertData(strColn, sheetName, sheetnum1);
            TruncateTableDAO truncateTableDAO1 = new TruncateTableDAO();
            truncateTableDAO1.createDimTable(delTableIds, sheetName);

            checkboo = true;
        } catch (Exception e) {
            logger.error("Exception:", e);
            checkboo = false;
        }
        return checkboo;

    }
}
