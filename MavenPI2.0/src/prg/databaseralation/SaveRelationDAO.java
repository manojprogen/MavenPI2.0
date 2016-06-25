/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.databaseralation;

import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author Administrator @modified by santhosh.kumar@progenbusiness.com on
 * 03/09/09
 */
public class SaveRelationDAO extends PbDb {

    public static Logger logger = Logger.getLogger(SaveRelationDAO.class);
    ResourceBundle resourceBundle;//= new SaveRelationResourceBundle();

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {

                resourceBundle = new SaveRelationResourceBundleSqlSever();
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {

                resourceBundle = new SaveRelationResourceBundleMysql();
            } else {
                resourceBundle = new SaveRelationResourceBundle();
            }
        }

        return resourceBundle;
    }

    public synchronized boolean insertRelations(String[] relId, String[] relation) {
        String table1 = null;
        String column1 = null;
        String table2 = null;
        String column2 = null;
        String table3 = null;
        String column3 = null;
        String join = null;
        String sql = null;
        boolean flag = true;

        try {
            String temp1;
            String relMasterId = "";
            String[] idString = null;
            String[] idString2 = null;
            for (int i = 0; i < relId.length; i++) {

                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("relId[i] is" + relId[i]);
                if (relId[i].indexOf(">=") != -1) {
                    join = ">=";
                } else if (relId[i].indexOf("<=") != -1) {
                    join = "<=";
                } else if (relId[i].indexOf(">") != -1) {
                    join = ">";
                } else if (relId[i].indexOf("<") != -1) {
                    join = "<";
                } else if (relId[i].indexOf("=") != -1) {
                    join = "=";
                } else if (relId[i].indexOf("BETWEEN") != -1) {
                    join = "BETWEEN";
                    relId[i] = relId[i].replaceFirst("AND", "BETWEEN");
                }
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Join is " + join);
                idString = ((relId[i].trim()).split(join));
                ////.println("" + idString.length);
                for (int k = 0; k < idString.length; k++) {
                    idString[k] = idString[k].trim();
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("idString  " + idString[k]);
                    temp1 = idString[k];
                    ////.println("temp1\t"+temp1);
                    idString2 = temp1.split("-");

                    if (k == 0) {
                        table1 = idString2[0];
                        column1 = idString2[1];
                    } else if (k == 1) {
                        table2 = idString2[0];
                        column2 = idString2[1];
                    } else {
                        table3 = idString2[0];
                        column3 = idString2[1];
                    }
                }

                ////.println("table3\t"+table3+"\ncolumn3\t"+column3);
                relMasterId = getRltMasterId(flag, table1, table2, relation[i], i);

                //please  don,t delete this code
                ///DON>t delete
                //to remove old details when added relation once again on relation tables
               /*
                 * if(i==0){ String deleteQuery="delete from
                 * PRG_DB_TABLE_RLT_DETAILS where RELATIONSHIP_ID="+relMasterId;
                 *
                 * execModifySQL(deleteQuery); }
                 *
                 */
                // ends

                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("sql is" + sql);
                String updateSQl = " select * from PRG_DB_TABLE_RLT_DETAILS where RELATIONSHIP_ID='" + relMasterId + "' and ";
                String insertRelationsQuery = getResourceBundle().getString("insertRelationsQuery");

                String finalQuery = "";

                Object[] obj = new Object[10];
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    obj[0] = relMasterId;
                    obj[1] = table1;
                    obj[2] = column1;
                    obj[3] = table2;
                    obj[4] = column2;
                    obj[5] = "inner";
                    obj[6] = join;
                    obj[7] = relation[i];
                    if (table3 == null) {
                        obj[8] = "null";
                    } else {
                        obj[8] = table3;
                    }
                    if (column3 == null) {
                        obj[9] = "null";
                    } else {
                        obj[9] = column3;
                    }



                } else {

                    obj[0] = relMasterId;
                    obj[1] = table1;
                    obj[2] = column1;
                    obj[3] = table2;
                    obj[4] = column2;
                    obj[5] = "inner";
                    obj[6] = join;
                    obj[7] = relation[i];
                    if (table3 == null) {
                        obj[8] = "";
                    } else {
                        obj[8] = table3;
                    }
                    if (column3 == null) {
                        obj[9] = "";
                    } else {
                        obj[9] = column3;
                    }
                }

                finalQuery = buildQuery(insertRelationsQuery, obj);

                execModifySQL(finalQuery);
                //////////////////////////////////////////////////////////////////////////////////////.println.println("details relation final query"+finalQuery);

            }
        } catch (Exception e) {
            flag = false;
        }

        return flag;
    }

    public String getRltMasterId(boolean seqFlag, String table1, String table2, String relation, int k) {
        String relId = "";
        PbReturnObject retObj = null;
        String finalQuery = "";
        String relationdb = "";
        String[] tableColNames = null;
        try {
            boolean relFlag = true;

            //  String getRltMasterIdQuery = resBundle.getString("getRltMasterIdQuery");getRltMasterIdQueryexist
            String getRltMasterIdQuery = getResourceBundle().getString("getRltMasterIdQueryexist");
            Object[] Obj = new Object[4];
            Obj[0] = table1;
            Obj[1] = table2;
            Obj[2] = table1;
            Obj[3] = table2;
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                if (!table1.equalsIgnoreCase("") && !table2.equalsIgnoreCase("")) {
                    finalQuery = buildQuery(getRltMasterIdQuery, Obj);
                    retObj = execSelectSQL(finalQuery);
                    tableColNames = retObj.getColumnNames();
                }

            } else {

                finalQuery = buildQuery(getRltMasterIdQuery, Obj);
                retObj = execSelectSQL(finalQuery);
                tableColNames = retObj.getColumnNames();
            }

            // //////////////////////////////////////////////////////////////////////////////////////.println.println("getRltMasterIdQuery===="+finalQuery);


            if (retObj != null) {
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    relFlag = false;
                    relId = retObj.getFieldValueString(i, tableColNames[0]);
                    relationdb = retObj.getFieldValueString(i, tableColNames[1]);
                }
            }

            if (relFlag) {
                String insertDbTabRelMasterQuery = getResourceBundle().getString("insertDbTabRelMasterQuery");
                Obj = new Object[3];
                if (table1.equalsIgnoreCase("")) {
                    Obj[0] = "null";
                } else {
                    Obj[0] = table1;
                }
                if (table1.equalsIgnoreCase("")) {
                    Obj[1] = "null";
                } else {
                    Obj[1] = table2;
                }


                Obj[2] = relation;
                finalQuery = buildQuery(insertDbTabRelMasterQuery, Obj);
                ////.println("in if finalQuery--"+finalQuery);

                execModifySQL(finalQuery);
                String relationIdquery = "";
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    relationIdquery = getResourceBundle().getString("relationIdquery1");
                } else {
                    relationIdquery = getResourceBundle().getString("relationIdquery");
                }
                PbReturnObject pbr = execSelectSQL(relationIdquery);
                relId = String.valueOf(pbr.getFieldValueInt(0, 0));
            } else {
                String updateDbTabRelMaster = getResourceBundle().getString("updateDbTabRelMaster");
                Obj = new Object[2];
                //please  don,t delete this code
                ///DON>t delete
             /*
                 * if(k==0){
                 *
                 * Obj[0] =relation; }else{
                 *
                 * Obj[0] = relationdb+" AND "+relation; }
                 *
                 */
                Obj[0] = relationdb + " AND  " + relation; //for this line above condition when added relation on same tables onceagin remove that and add it newly with same id
                Obj[1] = relId;
                finalQuery = buildQuery(updateDbTabRelMaster, Obj);
                ////.println("in update----"+finalQuery);
                execModifySQL(finalQuery);
            }


        } catch (Exception e) {
            logger.error("Exception: ", e);
            seqFlag = false;
        } finally {
        }
        return relId;
    }

    public String getSavedRelations(String tableIds) {
        String[] tabId = tableIds.split(",");
        StringBuffer relations = new StringBuffer();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("tableIds are =" + tabId.length);
        Object[] Obj = null;
        String finalQuery = "";
        PbReturnObject retObj = null;
        String[] tableColNames = null;
        try {
            String getSavedRelationsQuery = getResourceBundle().getString("getSavedRelationsQuery");

            Obj = new Object[2];
            Obj[0] = tableIds;
            Obj[1] = tableIds;
            finalQuery = buildQuery(getSavedRelationsQuery, Obj);

            retObj = execSelectSQL(finalQuery);
            tableColNames = retObj.getColumnNames();

            relations.append("<root>");

            for (int i = 0; i < retObj.getRowCount(); i++) {
                relations.append("<relId>" + retObj.getFieldValueString(i, tableColNames[0]) + "</relId>");
                relations.append("<relClause>" + retObj.getFieldValueString(i, tableColNames[1]) + "</relClause>");
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("" + retObj.getFieldValueString(i,tableColNames[0]));
            }
            relations.append("</root>");
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return relations.toString();
    }
}
