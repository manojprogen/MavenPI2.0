/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.query;

import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 *
 * @author Administrator
 */
public class GetDimFactMapping {

    public static Logger logger = Logger.getLogger(GetDimFactMapping.class);
    String ret = null;
  //  String elementId = "";
    StringBuilder elementId=new StringBuilder();
  //  String tabID = "";
    StringBuilder tabID=new StringBuilder();
    String strSel1 = null;
    // String strSel2=null;
    StringBuilder strSel2 = new StringBuilder();
    ArrayList tableList = new ArrayList();
    String finalTableList = "";
    PbDb PbDb = new PbDb();

    public String getFact(ArrayList elementList) throws SQLException {
        //public void getFact()      {

//       elementList.add(145196);
//       elementList.add(145279);

        if (elementList.isEmpty()) {
//
            ret = null;
        } else {
            for (int i = 0; i < elementList.size(); i++) {
           //     elementId = elementId + elementList.get(i).toString().replace("elmnt-", "").trim() + ",";
                elementId.append(elementList.get(i).toString().replace("elmnt-", "").trim()).append(",");
            }
            int size = elementId.length();
        //    elementId = elementId.substring(0, size - 1);
            String str=elementId.substring(0, size - 1);
            elementId=new StringBuilder(str);
//

            strSel1 = "select distinct BUSS_TABLE_ID from prg_user_all_info_details where ELEMENT_ID in(" + elementId + ")";
            PbReturnObject retObj = null;
            // try {
//
            retObj = PbDb.execSelectSQL(strSel1);


//            } catch (SQLException ex) {
//                logger.error("Exception:",ex);
//            }
            if (retObj.getRowCount() == 0) {
//
                ret = null;
                return null;
            } else {
//


                for (int i = 0; i < retObj.getRowCount(); i++) {
//
                    tableList.add(retObj.getFieldValueString(i, 0));
                }
                for (int i = 0; i < tableList.size(); i++) {
                  //  tabID = tabID + tableList.get(i) + ",";
                    tabID.append(tableList.get(i)).append(",");
                }

                size = tabID.length();
             //   tabID = tabID.substring(0, size - 1);
                String tmpstr=tabID.substring(0, size - 1);
                tabID=new StringBuilder(tmpstr);

            }

//            strSel2="Select T2 , T1 from ( select  buss_table_id as T1 , Buss_table_id2 as T2 from "
//                    + " PRG_GRP_BUSS_TABLE_RLT_MASTER where buss_table_id in ( " + tabID
//                    + " ) "
//                    + " union "
//                    + " select  buss_table_id2 as T1 , Buss_table_id as T2 from"
//                    + "  PRG_GRP_BUSS_TABLE_RLT_MASTER where buss_table_id2 in  ( " + tabID
//                    + " ) "
//                    + ""
//                    + " ) A order by T2, T1 ";
            strSel2.append("Select T2 , T1 from ( select  buss_table_id as T1 , Buss_table_id2 as T2 from PRG_GRP_BUSS_TABLE_RLT_MASTER where buss_table_id in ( ");
            strSel2.append(tabID).append(") ");
            strSel2.append(" union select  buss_table_id2 as T1 , Buss_table_id as T2 from   PRG_GRP_BUSS_TABLE_RLT_MASTER where buss_table_id2 in  ( ");
            strSel2.append(tabID).append(" ) ) A order by T2, T1 ");
            retObj = null;
            try {
//
                retObj = PbDb.execSelectSQL(strSel2.toString());
                GetFinalTableArray(retObj);
//                ArrayList mylist = GetFinalTableArray(retObj );
//                for(int i=0;i<mylist.size();i++){
//                    finalTableList+= mylist.get(i) + ",";
//                }

            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
        addTargetTable();
        if (finalTableList == null || finalTableList.length() == 0) {
            finalTableList = "0";
        } else if (finalTableList.length() > 1) {
            finalTableList += "0";
        }

//
        return finalTableList;
        //finalTableList.substring(0, finalTableList.length()-1);
    }

    public ArrayList GetFinalTableArray(PbReturnObject retObj) {
        String oldTabID = "";
        String newTabId = "";
        ArrayList attachedTables = new ArrayList();
        boolean finalTable = false;

        if (retObj != null) {
            oldTabID = retObj.getFieldValueString(0, 0);
            for (int i = 0; i < retObj.getRowCount(); i++) {
                newTabId = retObj.getFieldValueString(i, 0);
//
//
//
                if (oldTabID.equals(newTabId)) {


//
                    attachedTables.add(retObj.getFieldValueString(i, 1));
//
                    finalTable = false;
                } else {
//
                    checkAndAddTabletoList(attachedTables, oldTabID);

                    oldTabID = newTabId;
                    attachedTables = new ArrayList();
//
                    attachedTables.add(retObj.getFieldValueString(i, 1));
                    finalTable = true;
                }
            }
            if (!finalTable) {
                //oldTabID = newTabId;
                //attachedTables = new ArrayList();
                //attachedTables.add(retObj.getFieldValueString(retObj.getRowCount()-1,1));
                checkAndAddTabletoList(attachedTables, oldTabID);
                finalTable = true;
            }
            if (finalTable) {
                oldTabID = newTabId;
                //attachedTables = new ArrayList();
                //attachedTables.add(retObj.getFieldValueString(retObj.getRowCount()-1,1));
                checkAndAddTabletoList(attachedTables, oldTabID);
                finalTable = true;
            }

        }

        return attachedTables;
    }

    public void checkAndAddTabletoList(ArrayList newTableList, String MapTable) {
//
//
//
        if (newTableList.size() != tableList.size()) {
            return;
        } else {
            for (int i = 0; i < tableList.size(); i++) {
                if (!newTableList.contains(tableList.get(i))) {
                    return;
                }

            }
        }
        finalTableList += MapTable + " , ";

    }

    public void addTargetTable() {
        if (tableList.size() == 1) {
            String sqlstr = " select Trgt_bus_tab_id from PRG_GRP_TARGET_MAPPING where buss_tab_id in (" + tabID + ")";
            PbReturnObject retObj = null;
            try {
                retObj = PbDb.execSelectSQL(sqlstr);
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
            if (retObj == null || retObj.getRowCount() == 0) {
                return;
            }
            finalTableList += retObj.getFieldValueString(0, 0) + " , ";

        }

    }
//public static void main(String args[]) {
//
//    GetDimFactMapping fa = new GetDimFactMapping();
//    ArrayList elementList1=new ArrayList();
//        try {
//            String str = fa.getFact(elementList1);
//        } catch (SQLException ex) {
//
//            logger.error("Exception:",ex);
//        }
//
//}
}
