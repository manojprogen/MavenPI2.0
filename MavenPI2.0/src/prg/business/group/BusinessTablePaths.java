/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.business.group;

/**
 * @filename BusinessTablePaths
 *
 * @author santhosh.kumar@progenbusiness.com @date Sep 25, 2009, 4:08:55 PM
 */
import java.sql.SQLException;
import java.util.*;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

public class BusinessTablePaths {

    public static Logger logger = Logger.getLogger(BusinessTablePaths.class);
    HashMap tableRelationship = new HashMap();

    public BusinessTablePaths() throws SQLException {
        ProgenConnection pg;
    }
    PbDb pbDb = new PbDb();
    PbBussGrpResourceBundle resourceBundle = new PbBussGrpResourceBundle();

    // modified by susheela
    public HashMap getpathFromBusinessTable(String businessTableId) throws Exception {
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

        // sqlstr modified by susheela
        // sqlstr = "select BUSS_TABLE_ID from PRG_GRP_BUSS_TABLE_RLT_MASTER where BUSS_TABLE_ID2 = " + businessTableId;
        // sqlstr += " union select BUSS_TABLE_ID2 from PRG_GRP_BUSS_TABLE_RLT_MASTER where BUSS_TABLE_ID = " + businessTableId;

        sqlstr = "select distinct pgbtrm.BUSS_TABLE_ID from PRG_GRP_BUSS_TABLE_RLT_MASTER pgbtrm,prg_grp_buss_table pgbt where pgbtrm.BUSS_TABLE_ID2 =" + businessTableId + " "
                + " and pgbt.buss_type='Table' and pgbt.buss_table_id = pgbtrm.buss_table_id  union select pgbtrm.BUSS_TABLE_ID2 from "
                + " PRG_GRP_BUSS_TABLE_RLT_MASTER  pgbtrm,prg_grp_buss_table pgbt where pgbtrm.BUSS_TABLE_ID =" + businessTableId + "  and "
                + " pgbt.buss_type='Table'  and pgbt.buss_table_id = pgbtrm.buss_table_id2 ";
        //finalQuery = pbDb.buildQuery(sqlstr, Obj);
        finalQuery = sqlstr;
        retObj = pbDb.execSelectSQL(finalQuery);
        colNames = retObj.getColumnNames();
        int psize = retObj.getRowCount();
        if (psize > 0) {
            for (int looper = 0; looper < psize; looper++) {
                //mainTable.add(retObj.getFieldValueString(looper, colNames[0]));
                tableRelationship.put(retObj.getFieldValueString(looper, colNames[0]), businessTableId);
                recfindTable(retObj.getFieldValueString(looper, colNames[0]), businessTableId + "," + retObj.getFieldValueString(looper, colNames[0]));
            }
        }
        return (tableRelationship);
    }

    public HashMap getpathFromBusinessTableForMap(String businessTableId, String grpId) throws Exception {
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

        // sqlstr modified by susheela
        // sqlstr = "select BUSS_TABLE_ID from PRG_GRP_BUSS_TABLE_RLT_MASTER where BUSS_TABLE_ID2 = " + businessTableId;
        // sqlstr += " union select BUSS_TABLE_ID2 from PRG_GRP_BUSS_TABLE_RLT_MASTER where BUSS_TABLE_ID = " + businessTableId;

        sqlstr = "select distinct pgbtrm.BUSS_TABLE_ID from PRG_GRP_BUSS_TABLE_RLT_MASTER pgbtrm,prg_grp_buss_table pgbt where pgbtrm.BUSS_TABLE_ID2 =" + businessTableId + " "
                + " and pgbt.buss_type='Table' and pgbt.buss_table_id = pgbtrm.buss_table_id and pgbt.grp_id=" + grpId + "  union select pgbtrm.BUSS_TABLE_ID2 from "
                + " PRG_GRP_BUSS_TABLE_RLT_MASTER  pgbtrm,prg_grp_buss_table pgbt where pgbtrm.BUSS_TABLE_ID =" + businessTableId + "  and "
                + " pgbt.buss_type='Table'  and pgbt.buss_table_id = pgbtrm.buss_table_id2 and pgbt.grp_id=" + grpId;
        //finalQuery = pbDb.buildQuery(sqlstr, Obj);
        finalQuery = sqlstr;
         retObj = pbDb.execSelectSQL(finalQuery);
        colNames = retObj.getColumnNames();
        int psize = retObj.getRowCount();
        if (psize > 0) {

            for (int looper = 0; looper < psize; looper++) {
                //mainTable.add(retObj.getFieldValueString(looper, colNames[0]));
                tableRelationship.put(retObj.getFieldValueString(looper, colNames[0]), businessTableId);
                recfindTableForMap(retObj.getFieldValueString(looper, colNames[0]), businessTableId + "," + retObj.getFieldValueString(looper, colNames[0]), grpId);
            }
        }
        return (tableRelationship);
    }

    public void recfindTableForMap(String businessTableId, String Path, String grpId) throws Exception {
        PbReturnObject retObj = null;
        String[] colNames;
        String sqlstr;
        String finalQuery;
        // changed by susheela
        //  sqlstr = " select buss_table_id from ( ";
        //  sqlstr += "select buss_table_id from PRG_GRP_BUSS_TABLE_RLT_MASTER where buss_table_id2 = " + businessTableId;
        //  sqlstr += " union select buss_table_id2 from PRG_GRP_BUSS_TABLE_RLT_MASTER where buss_table_id = " + businessTableId;
        // sqlstr += " ) where buss_table_id not in ( " + Path + " )";

        // sqlstr modified by susheela. later on by providing dimension in the buss table it needs to be modified further.

        sqlstr = "SELECT distinct pgbtrm.buss_table_id FROM PRG_GRP_BUSS_TABLE_RLT_MASTER pgbtrm,prg_grp_buss_table pgbt WHERE "
                + " pgbtrm.buss_table_id2 =" + businessTableId + " and pgbt.buss_type='Table' and pgbt.buss_table_id = pgbtrm.buss_table_id and"
                + "  pgbtrm.buss_table_id NOT IN (" + Path + ") " + "and pgbt.grp_id=" + grpId
                + " UNION SELECT distinct pgbtrm.buss_table_id2 FROM PRG_GRP_BUSS_TABLE_RLT_MASTER pgbtrm,prg_grp_buss_table pgbt "
                + " WHERE pgbtrm.buss_table_id =" + businessTableId + " and pgbt.buss_type='Table' and pgbt.buss_table_id = pgbtrm.buss_table_id2 "
                + " and pgbtrm.buss_table_id2 NOT IN (" + Path + ")" + " and pgbt.grp_id=" + grpId;
        //finalQuery = pbDb.buildQuery(sqlstr, Obj);
        finalQuery = sqlstr;
        retObj = pbDb.execSelectSQL(finalQuery);

        colNames = retObj.getColumnNames();
        int psize = retObj.getRowCount();
        if (psize > 0) {
            for (int looper = 0; looper < psize; looper++) {
                String p = Path + "," + retObj.getFieldValueString(looper, colNames[0]);
                tableRelationship.put(retObj.getFieldValueString(looper, colNames[0]), Path.substring(Path.indexOf(",") + 1));
                recfindTableForMap(retObj.getFieldValueString(looper, colNames[0]), p, grpId);
            }
        }
    }

    public HashMap getpathFromBusinessTableForTLevels(String businessTableId) throws Exception {
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

        // sqlstr modified by susheela
        // sqlstr = "select BUSS_TABLE_ID from PRG_GRP_BUSS_TABLE_RLT_MASTER where BUSS_TABLE_ID2 = " + businessTableId;
        // sqlstr += " union select BUSS_TABLE_ID2 from PRG_GRP_BUSS_TABLE_RLT_MASTER where BUSS_TABLE_ID = " + businessTableId;

        sqlstr = "select distinct pgbtrm.BUSS_TABLE_ID from PRG_GRP_BUSS_TABLE_RLT_MASTER pgbtrm,prg_grp_buss_table pgbt where pgbtrm.BUSS_TABLE_ID2 =" + businessTableId + " "
                + " and pgbt.buss_type='Table' and pgbt.buss_table_id = pgbtrm.buss_table_id  union select pgbtrm.BUSS_TABLE_ID2 from "
                + " PRG_GRP_BUSS_TABLE_RLT_MASTER  pgbtrm,prg_grp_buss_table pgbt where pgbtrm.BUSS_TABLE_ID =" + businessTableId + "  and "
                + " pgbt.buss_type='Table'  and pgbt.buss_table_id = pgbtrm.buss_table_id2 ";
        //finalQuery = pbDb.buildQuery(sqlstr, Obj);
        finalQuery = sqlstr;
         retObj = pbDb.execSelectSQL(finalQuery);
        colNames = retObj.getColumnNames();
        int psize = retObj.getRowCount();
        if (psize > 0) {
            for (int looper = 0; looper < psize; looper++) {
                //mainTable.add(retObj.getFieldValueString(looper, colNames[0]));
                tableRelationship.put(retObj.getFieldValueString(looper, colNames[0]), "");
                recfindTable(retObj.getFieldValueString(looper, colNames[0]), businessTableId + "," + retObj.getFieldValueString(looper, colNames[0]));
            }
        }
        return (tableRelationship);
    }
    // modified by susheela

    public void recfindTable(String businessTableId, String Path) throws Exception {
        PbReturnObject retObj = null;
        String[] colNames;
        String sqlstr;
        String finalQuery;
        // changed by susheela
        //  sqlstr = " select buss_table_id from ( ";
        //  sqlstr += "select buss_table_id from PRG_GRP_BUSS_TABLE_RLT_MASTER where buss_table_id2 = " + businessTableId;
        //  sqlstr += " union select buss_table_id2 from PRG_GRP_BUSS_TABLE_RLT_MASTER where buss_table_id = " + businessTableId;
        // sqlstr += " ) where buss_table_id not in ( " + Path + " )";

        // sqlstr modified by susheela. later on by providing dimension in the buss table it needs to be modified further.

        sqlstr = "SELECT distinct pgbtrm.buss_table_id FROM PRG_GRP_BUSS_TABLE_RLT_MASTER pgbtrm,prg_grp_buss_table pgbt WHERE "
                + " pgbtrm.buss_table_id2 =" + businessTableId + " and pgbt.buss_type='Table' and pgbt.buss_table_id = pgbtrm.buss_table_id and"
                + "  pgbtrm.buss_table_id NOT IN (" + Path + ") "
                + " UNION SELECT distinct pgbtrm.buss_table_id2 FROM PRG_GRP_BUSS_TABLE_RLT_MASTER pgbtrm,prg_grp_buss_table pgbt "
                + " WHERE pgbtrm.buss_table_id =" + businessTableId + " and pgbt.buss_type='Table' and pgbt.buss_table_id = pgbtrm.buss_table_id2 "
                + " and pgbtrm.buss_table_id2 NOT IN (" + Path + ")";
        //finalQuery = pbDb.buildQuery(sqlstr, Obj);
        finalQuery = sqlstr;
        retObj = pbDb.execSelectSQL(finalQuery);
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

    // 30-sept-09 added by susheela
    public void AddParentChildLevels(HashMap hm) throws Exception {
        String insertQuery = resourceBundle.getString("AddParentChildLevels");
        ArrayList insertQ = new ArrayList();
        ArrayList allKeys = new ArrayList();
        HashMap childs = new HashMap();
        Vector zeroValues = new Vector();
        Set key5 = (Set) hm.keySet();
        String keys[] = (String[]) key5.toArray(new String[0]);
        for (int p = 0; p < keys.length; p++) {
            String keyValue = keys[p];
            allKeys.add(keyValue);
            String tabValues = "";
            tabValues = hm.get(keyValue).toString();
            String arr[] = tabValues.split(",");
            boolean skipFlag = false;
            for (int y = 0; y < arr.length; y++) {
                if (arr[y].equalsIgnoreCase("")) {
                    skipFlag = true;
                }
            }
            int countLength = arr.length;
            int commaIndex = 0;
            commaIndex = tabValues.indexOf(",");
            if (commaIndex == -1 && skipFlag == true) {
                countLength = 0;
                zeroValues.add(keyValue);
            }
//            childs.put(keyValue, "" + countLength + "");
            childs.put(keyValue, Integer.toString(countLength ));
        }
        LinkedHashMap parentChild = new LinkedHashMap();
        for (int p = 0; p < keys.length; p++) {
            String keyValue = keys[p];
            String commaValues = hm.get(keyValue).toString();
            String vals[] = commaValues.split(",");
            LinkedHashMap individualLevels = new LinkedHashMap();
            int level = 0;
            level = vals.length;
            //  for(int k=vals.length-1;k>=0;k--){
            for (int k = 0; k < vals.length; k++) {
                String v = vals[k];
                if (!zeroValues.contains(keyValue)) {
//                    individualLevels.put(v, "" + level + "");
                    individualLevels.put(v,  Integer.toString(level ));
                }
                level--;
            }
            parentChild.put(keyValue, individualLevels);
        }
        Set pChilds = (Set) parentChild.keySet();
        ArrayList deleteList = new ArrayList();
        String pChildsKeys[] = (String[]) pChilds.toArray(new String[0]);
        for (int y = 0; y < pChildsKeys.length; y++) {
            String parent = pChildsKeys[y];
            LinkedHashMap childsLevels = (LinkedHashMap) parentChild.get(parent);
            Set pChildsLevel = (Set) childsLevels.keySet();
            String pChildsLevelKeys[] = (String[]) pChildsLevel.toArray(new String[0]);
            for (int u = 0; u < pChildsLevelKeys.length; u++) {
                String k = pChildsLevelKeys[u];
                String level = childsLevels.get(k).toString();
                Object valOb[] = new Object[3];
                valOb[0] = k;
                valOb[1] = parent;
                valOb[2] = level;
                String alraedyPrQ = "select * from prg_buss_table_level where child_buss_table_id=" + k + " and parent_buss_table_id=" + parent;
                PbReturnObject pbro = pbDb.execSelectSQL(alraedyPrQ);
                String detId = "";
                if (pbro.getRowCount() > 0) {
                    for (int m = 0; m < pbro.getRowCount(); m++) {
                        detId = pbro.getFieldValueString(m, "BUSS_DETAILS_ID");
                        String deleteQ = "delete from prg_buss_table_level where buss_details_id=" + detId;
                        deleteList.add(deleteQ);
                    }
                }
                String finalQ = pbDb.buildQuery(insertQuery, valOb);
                insertQ.add(finalQ);
            }
        }
       
        try {
            pbDb.executeMultiple(deleteList);
            pbDb.executeMultiple(insertQ);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }
    // 30-sept-09 added by susheela

    public ArrayList getParentTables(String childTables) throws Exception {
        ArrayList tables = new ArrayList();
        String selectQuery = resourceBundle.getString("getParentTables");
        Object tabOb[] = new Object[1];
        tabOb[0] = childTables;
        String finalQ = pbDb.buildQuery(selectQuery, tabOb);
        PbReturnObject pbro = pbDb.execSelectSQL(finalQ);
        // pbro.writeString();
        for (int p = 0; p < pbro.getRowCount(); p++) {
            String sValue = pbro.getFieldValueString(p, "PARENT_BUSS_TABLE_ID");
            if (!tables.contains(sValue)) {
                tables.add(sValue);
            }
        }
        String present[] = childTables.split(",");
        for (int p = 0; p < present.length; p++) {
            String val = present[p];
            if (!tables.contains(val) && (!val.equalsIgnoreCase(""))) {
                tables.add(val);
            }
            //tables
        }
        return tables;
    }

    public void AddPathMaps(String bussFactId, String[] bussTableIds, String[] paths) {
        try {

            Object obj[] = new Object[3];
            String finalQuery = "";
            ArrayList list = new ArrayList();
            ArrayList deleteList = new ArrayList();
            PbBussGrpResourceBundle resBundle = new PbBussGrpResourceBundle();
            String addPathMaps = resBundle.getString("addPathMaps");
            for (int i = 0; i < paths.length; i++) {
                obj[0] = bussFactId;
                obj[1] = bussTableIds[i];
                obj[2] = paths[i];
                finalQuery = pbDb.buildQuery(addPathMaps, obj);
                  String alreadyPathQ = "select * from prg_grp_buss_table_maps where buss_fact_id=" + bussFactId + " and buss_table_id=" + bussTableIds[i];
                PbReturnObject alOb = pbDb.execSelectSQL(alreadyPathQ);
                for (int m = 0; m < alOb.getRowCount(); m++) {
                    String tableMapId = alOb.getFieldValueString(m, "BUSS_TABLE_MAP_ID");
                    String deleteQ = "delete from prg_grp_buss_table_maps where BUSS_TABLE_MAP_ID=" + tableMapId;
                    deleteList.add(deleteQ);
                }
                list.add(finalQuery);
            }
            pbDb.executeMultiple(deleteList);
            pbDb.executeMultiple(list);

        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }

    public void getPathsToInsertForFact(String bussTableId, String grpId) throws Exception {
        HashMap hpaths = new HashMap();
        hpaths = getpathFromBusinessTableForMap(bussTableId, grpId);//getpathFromBusinessTable(bussTableId);
        
        if (hpaths != null) {
            if (!hpaths.isEmpty()) {
                Set s = hpaths.keySet();
                Collection c = hpaths.values();

                Iterator i = s.iterator();
                StringBuilder keyarr = new StringBuilder();
                while (i.hasNext()) {
//                    keyarr += "~" + i.next();
                    keyarr.append( "~" ).append( i.next());
                }

                if (keyarr.indexOf("~") >= 0) {
//                    keyarr = keyarr.substring(1);
                    keyarr = new StringBuilder(keyarr.substring(1));
                }


                Iterator j = c.iterator();
//                String valarr = "";
                StringBuilder valarr = new StringBuilder();
                while (j.hasNext()) {
//                    valarr += "~" + j.next();
                    valarr.append("~").append( j.next());
                }

                if (valarr.indexOf("~") >= 0) {
                    valarr =new StringBuilder( valarr.substring(1));
                }

                String keys[] = keyarr.toString().split("~");
                String values[] = valarr.toString().split("~");
                String totalPaths[] = new String[keys.length];
                for (int k = 0; k < keys.length; k++) {
                    //totalPaths[k]=keys[k];
                    if (!(values[k].equals(""))) {
                        if (values[k] != null) {
                            totalPaths[k] = values[k];
                        }
                    }
                }
                AddPathMaps(bussTableId, keys, totalPaths);
            }
        }
    }

    public static void main(String[] args) {
        try {
            HashMap h = new BusinessTablePaths().getpathFromBusinessTableForMap("1048", "661");
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }
}
