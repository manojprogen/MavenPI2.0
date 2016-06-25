package com.progen.userlayer.db;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Vector;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

public class PbUserLayerEditDAO extends PbDb {

//    PbUserLayerEditResourceBundle resBundle = new PbUserLayerEditResourceBundle();
    ResourceBundle resourceBundle;
    public static Logger logger = Logger.getLogger(PbUserLayerEditDAO.class);

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new PbUserLayerEditResourceBundleSqlServer();
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                resourceBundle = new PbUserLayerEditResourceBundleMysql();
            } else {
                resourceBundle = new PbUserLayerEditResourceBundle();
            }
        }

        return resourceBundle;
    }

    public void migrateToBussRole(String grpId, String roleIds[]) throws Exception {
//        String grpDims = getResourceBundle().getString("grpDims");
        String getSubFoldersQ = getResourceBundle().getString("getSubFoldersQ");
        String subFolderId = "";
        String subFolderName = "";
        String subFolderType = "";
        ArrayList al = new ArrayList();


        for (int m = 0; m < roleIds.length; m++) {
            String folderId = roleIds[m];
            Object roleObj[] = new Object[1];
            roleObj[0] = folderId;
            String fingetSubFoldersQ = buildQuery(getSubFoldersQ, roleObj);
            PbReturnObject folderObj = execSelectSQL(fingetSubFoldersQ);
            for (int n = 0; n < folderObj.getRowCount(); n++) {
                subFolderId = folderObj.getFieldValueString(n, "SUB_FOLDER_ID");
                subFolderName = folderObj.getFieldValueString(n, "SUB_FOLDER_NAME");
                subFolderType = folderObj.getFieldValueString(n, "SUB_FOLDER_TYPE");
                al = addUserSubFolderTables(al, grpId, subFolderType, subFolderId, folderId);
            }
        }

        try {
            executeMultiple(al);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public void migrateToBussRoleNew(String grpId, String roleIds[], String extraDims[]) throws Exception {
//        String grpDims = getResourceBundle().getString("grpDims");
        String getSubFoldersQ = getResourceBundle().getString("getSubFoldersQ");
        String subFolderId = "";
        String subFolderName = "";
        String subFolderType = "";
        ArrayList al = new ArrayList();


        for (int m = 0; m < roleIds.length; m++) {
            String folderId = roleIds[m];
            Object roleObj[] = new Object[1];
            roleObj[0] = folderId;
            String fingetSubFoldersQ = buildQuery(getSubFoldersQ, roleObj);
            PbReturnObject folderObj = execSelectSQL(fingetSubFoldersQ);
            for (int n = 0; n < folderObj.getRowCount(); n++) {
                subFolderId = folderObj.getFieldValueString(n, 1);
                subFolderName = folderObj.getFieldValueString(n, 2);
                subFolderType = folderObj.getFieldValueString(n, 3);
                al = addUserSubFolderTables(al, grpId, subFolderType, subFolderId, folderId, extraDims[m]);
            }
        }

        try {
            Connection conn = null;
            conn = ProgenConnection.getInstance().getConnection();
            executeMultiple(al, conn);
//            executeMultiple(al);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public ArrayList addUserSubFolderTables(ArrayList alist, String grpId, String folderType, String folderDtlId, String folderId, String extraDims) throws Exception {
        String addUserSubFolderTablesQuery = getResourceBundle().getString("addUserSubFolderTables");
        String addUserSubFolderTablesForDimensionsQuery = getResourceBundle().getString("addUserSubFolderTablesForDimensions");

        //  String getExtraDimsOfGrp=resBundle.getString("getExtraDimsForRole");
        String getExtraBussTable = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            getExtraBussTable = "select BUSS_TABLE_ID from prg_grp_buss_table where grp_id=& except select distinct buss_table_id from prg_user_sub_folder_tables where sub_folder_id in(&) except select distinct buss_table_id from prg_user_sub_folder_tables where sub_folder_id in(select sub_folder_id from prg_user_folder_detail where folder_id=& and sub_folder_type='Facts') ";

        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            getExtraBussTable = "select BUSS_TABLE_ID from prg_grp_buss_table where grp_id=& and  BUSS_TABLE_ID not in( select distinct buss_table_id from prg_user_sub_folder_tables where sub_folder_id in(&) and buss_table_id not in (select distinct buss_table_id from prg_user_sub_folder_tables where sub_folder_id in(select sub_folder_id from prg_user_folder_detail where folder_id=& and sub_folder_type='Facts'))) ";

        } else {
            getExtraBussTable = "select BUSS_TABLE_ID from prg_grp_buss_table where grp_id=& minus select distinct buss_table_id from prg_user_sub_folder_tables where sub_folder_id in(&) minus select distinct buss_table_id from prg_user_sub_folder_tables where sub_folder_id in(select sub_folder_id from prg_user_folder_detail where folder_id=& and sub_folder_type='Facts') ";

        }
        //resBundle.getString("getExtraBussTable");
      /*
         * Object det[]=new Object[2]; det[0]=grpId; det[1]=folderDtlId; String
         * fingetExtraDimsForRole=buildQuery(getExtraDimsOfGrp,det);
         * ////////////////////////////////////////////////////////////////////////////////////.println.println("
         * fingetExtraDimsForRole "+fingetExtraDimsForRole); String extraDim="";
         * PbReturnObject pbro = execSelectSQL(fingetExtraDimsForRole); for(int
         * y=0;y<pbro.getRowCount();y++) {
         * extraDim=extraDim+","+pbro.getFieldValueString(y,"DIM_ID"); }
         * if(extraDim.length()>1) extraDim=extraDim.substring(1);
         */
        Object folderObj[] = new Object[3];
        folderObj[0] = grpId;
        folderObj[1] = folderDtlId;
        folderObj[2] = folderId;
        String fingetExtraBussTable = buildQuery(getExtraBussTable, folderObj);
        PbReturnObject bussTabObj = execSelectSQL(fingetExtraBussTable);
//        String extraTab = "";
        StringBuilder extraTab = new StringBuilder();
        for (int t = 0; t < bussTabObj.getRowCount(); t++) {
//            extraTab = extraTab + "," + bussTabObj.getFieldValueString(t, "BUSS_TABLE_ID");
            extraTab.append(",").append( bussTabObj.getFieldValueString(t, "BUSS_TABLE_ID"));
        }
        if (bussTabObj.getRowCount() > 0) {
            extraTab =new StringBuilder( extraTab.substring(1));
        }
        String getBussDimInfoByGrpIdQuery = getResourceBundle().getString("getBussDimInfoByGrpId");
        String getBussFactsInfoByGrpIdQuery = getResourceBundle().getString("getBussFactsInfoByGrpId");
        String getBussBucketInfoByGrpIdQuery = getResourceBundle().getString("getBussBucketInfoByGrpId");



        String finalQuery = "";
        PbReturnObject retObj = null;
        String[] tableColNames = null;

        if (folderType.equalsIgnoreCase("Dimensions")) {
            Object[] Obj = new Object[2];
            Obj[0] = grpId;
            Obj[1] = extraDims;//extraDim;
            finalQuery = buildQuery(getBussDimInfoByGrpIdQuery, Obj);
            retObj = execSelectSQL(finalQuery);
            tableColNames = retObj.getColumnNames();
            int subFoldTabId = 0;
            for (int i = 0; i < retObj.getRowCount(); i++) {
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                     subFoldTabId = getSequenceNumber("select PRG_USER_SUB_FOLDER_TABLES_SEQ.NEXTVAL from dual");
                    Obj = new Object[14];
//                    Obj[0] = String.valueOf(subFoldTabId);
                    Obj[0] = folderDtlId;

                    Obj[1] = retObj.getFieldValueString(i, tableColNames[3]);
                    Obj[2] = "Y";
                    Obj[3] = "N";
                    Obj[4] = "N";
                    if (retObj.getFieldValueString(i, tableColNames[1]).equalsIgnoreCase("")) {
                        Obj[5] = "null";
                    } else {
                        Obj[5] = retObj.getFieldValueString(i, tableColNames[1]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[0]).equalsIgnoreCase("")) {
                        Obj[6] = "null";
                    } else {
                        Obj[6] = retObj.getFieldValueString(i, tableColNames[0]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[2]).equalsIgnoreCase("")) {
                        Obj[7] = "null";
                    } else {
                        Obj[7] = retObj.getFieldValueString(i, tableColNames[2]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[4]).equalsIgnoreCase("")) {
                        Obj[8] = "null";
                    } else {
                        Obj[8] = retObj.getFieldValueString(i, tableColNames[4]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[5]).equalsIgnoreCase("")) {
                        Obj[9] = "null";
                    } else {
                        Obj[9] = retObj.getFieldValueString(i, tableColNames[5]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[6]).equalsIgnoreCase("")) {
                        Obj[10] = "null";
                    } else {
                        Obj[10] = retObj.getFieldValueString(i, tableColNames[6]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[7]).equalsIgnoreCase("")) {
                        Obj[11] = "null";
                    } else {
                        Obj[11] = retObj.getFieldValueString(i, tableColNames[7]);
                    }

                    if (retObj.getFieldValueString(i, tableColNames[8]).equalsIgnoreCase("")) {
                        Obj[12] = "null";
                    } else {
                        Obj[12] = retObj.getFieldValueString(i, tableColNames[8]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[9]).equalsIgnoreCase("")) {
                        Obj[13] = "null";
                    } else {
                        Obj[13] = retObj.getFieldValueString(i, tableColNames[9]);
                    }


                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
//                     subFoldTabId = getSequenceNumber("select PRG_USER_SUB_FOLDER_TABLES_SEQ.NEXTVAL from dual");
                    Obj = new Object[14];
//                    Obj[0] = String.valueOf(subFoldTabId);
                    Obj[0] = folderDtlId;

                    Obj[1] = retObj.getFieldValueString(i, tableColNames[3]);
                    Obj[2] = "Y";
                    Obj[3] = "N";
                    Obj[4] = "N";
                    if (retObj.getFieldValueString(i, tableColNames[1]).equalsIgnoreCase("")) {
                        Obj[5] = "null";
                    } else {
                        Obj[5] = retObj.getFieldValueString(i, tableColNames[1]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[0]).equalsIgnoreCase("")) {
                        Obj[6] = "null";
                    } else {
                        Obj[6] = retObj.getFieldValueString(i, tableColNames[0]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[2]).equalsIgnoreCase("")) {
                        Obj[7] = "null";
                    } else {
                        Obj[7] = retObj.getFieldValueString(i, tableColNames[2]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[4]).equalsIgnoreCase("")) {
                        Obj[8] = "null";
                    } else {
                        Obj[8] = retObj.getFieldValueString(i, tableColNames[4]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[5]).equalsIgnoreCase("")) {
                        Obj[9] = "null";
                    } else {
                        Obj[9] = retObj.getFieldValueString(i, tableColNames[5]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[6]).equalsIgnoreCase("")) {
                        Obj[10] = "null";
                    } else {
                        Obj[10] = retObj.getFieldValueString(i, tableColNames[6]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[7]).equalsIgnoreCase("")) {
                        Obj[11] = "null";
                    } else {
                        Obj[11] = retObj.getFieldValueString(i, tableColNames[7]);
                    }

                    if (retObj.getFieldValueString(i, tableColNames[8]).equalsIgnoreCase("")) {
                        Obj[12] = "null";
                    } else {
                        Obj[12] = retObj.getFieldValueString(i, tableColNames[8]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[9]).equalsIgnoreCase("")) {
                        Obj[13] = "null";
                    } else {
                        Obj[13] = retObj.getFieldValueString(i, tableColNames[9]);
                    }


                } else {
                    subFoldTabId = getSequenceNumber("select PRG_USER_SUB_FOLDER_TABLES_SEQ.NEXTVAL from dual");
                    Obj = new Object[15];
                    Obj[0] = String.valueOf(subFoldTabId);
                    Obj[1] = folderDtlId;

                    Obj[2] = retObj.getFieldValueString(i, tableColNames[3]);
                    Obj[3] = "Y";
                    Obj[4] = "N";
                    Obj[5] = "N";
                    Obj[6] = retObj.getFieldValueString(i, tableColNames[1]);
                    Obj[7] = retObj.getFieldValueString(i, tableColNames[0]);
                    Obj[8] = retObj.getFieldValueString(i, tableColNames[2]);
                    Obj[9] = retObj.getFieldValueString(i, tableColNames[4]);

                    Obj[10] = retObj.getFieldValueString(i, tableColNames[5]);
                    Obj[11] = retObj.getFieldValueString(i, tableColNames[6]);
                    Obj[12] = retObj.getFieldValueString(i, tableColNames[7]);
                    Obj[13] = retObj.getFieldValueString(i, tableColNames[8]);
                    Obj[14] = retObj.getFieldValueString(i, tableColNames[9]);

                }
                finalQuery = buildQuery(addUserSubFolderTablesForDimensionsQuery, Obj);

                alist.add(finalQuery);
                alist = addUserSubFolderElements(alist, retObj.getFieldValueString(i, tableColNames[3]), folderDtlId, String.valueOf(subFoldTabId), grpId);
            }

            //added by susheela start. To insert in custom Drill
            String getAllMemRel = getResourceBundle().getString("getAllMemRel");
            Object grpObj[] = new Object[2];
            grpObj[0] = grpId;
            grpObj[1] = extraDims;

            Object grpObj2[] = new Object[1];
            grpObj2[0] = grpId;
            String fingetAllMemRel = buildQuery(getAllMemRel, grpObj2);
            String addRoleCustomDrill = getResourceBundle().getString("getRoleCustomDrill");

            String addRoleCustomDrillData = getResourceBundle().getString("addRoleCustomDrill");

            String finaddRoleCustomDrill = buildQuery(addRoleCustomDrill, grpObj);
            //.println("finaddRoleCustomDrill\t" + finaddRoleCustomDrill);
            PbReturnObject roleDrillobj = execSelectSQL(finaddRoleCustomDrill);
            Vector insertable = new Vector();

            String finALl = buildQuery(addRoleCustomDrill, grpObj);
//            PbReturnObject allDims = execSelectSQL(finALl);
            for (int m = 0; m < roleDrillobj.getRowCount(); m++) {
                insertable.add(roleDrillobj.getFieldValueString(m, "MEMBER_ID"));
            }
            PbReturnObject fingetAllMemRelOb = execSelectSQL(fingetAllMemRel);

            HashMap childs = new HashMap();
            String oldDimId = fingetAllMemRelOb.getFieldValueString(0, "DIM_ID");
            for (int n = 0; n < fingetAllMemRelOb.getRowCount(); n++) {

                String memId = fingetAllMemRelOb.getFieldValueString(n, "MEM_ID");
                String dimId = "";
//                String childId = "";
                int next = n + 1;
                String chElementId = fingetAllMemRelOb.getFieldValueString(n, "MEM_ID");
                if (next < fingetAllMemRelOb.getRowCount()) {
                    dimId = fingetAllMemRelOb.getFieldValueString(next, "DIM_ID");
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(chElementId+" oldDimId- "+oldDimId+" dimId "+dimId);
                    if (oldDimId.equalsIgnoreCase(dimId)) {
                        // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" in if 2");
                        chElementId = fingetAllMemRelOb.getFieldValueString(next, "MEM_ID");
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" chElementId "+chElementId);
                    }
                }
                oldDimId = dimId;
                childs.put(memId, chElementId);
            }

            HashMap dimMem = new HashMap();
            ArrayList det2 = new ArrayList();

            for (int n = 0; n < fingetAllMemRelOb.getRowCount(); n++) {

                String memId = fingetAllMemRelOb.getFieldValueString(n, "MEM_ID");
                String dimId = fingetAllMemRelOb.getFieldValueString(n, "DIM_ID");
//                String chElementId = fingetAllMemRelOb.getFieldValueString(n, "MEM_ID");
                if (dimMem.containsKey(dimId)) {
                    det2 = (ArrayList) dimMem.get(dimId);
                    det2.add(memId);
                    dimMem.put(dimId, det2);
                } else {
                    det2 = new ArrayList();
                    det2.add(memId);
                    dimMem.put(dimId, det2);
                }
            }
            ////////////////////////////////////////////////////////////////////////////////////.println.println(" childs ..."+childs);
            for (int n = 0; n < roleDrillobj.getRowCount(); n++) {
                String dimId = roleDrillobj.getFieldValueString(n, "DIM_ID");
                String memId = roleDrillobj.getFieldValueString(n, "MEMBER_ID");
                if (childs.containsKey(memId)) {
                    String chId = childs.get(memId).toString();
                    if (dimMem.containsKey(dimId)) {
                        ArrayList det = (ArrayList) dimMem.get(dimId);
                        if (!det.contains(chId)) {
                            childs.put(memId, memId);
                        }
                    }
                }
            }
            ////////////////////////////////////////////////////////////////////////////////////.println.println(dimMem+" childs "+childs);//folderDtlId
            ArrayList insertRoleDrillList = new ArrayList();
            for (int k = 0; k < insertable.size(); k++) {
                String memId = insertable.get(k).toString();
                String childId = memId;
                if (childs.containsKey(memId)) {
                    childId = childs.get(memId).toString();
                }
                Object insertOb[] = new Object[5];
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    insertOb[0] = "null";
                    insertOb[1] = folderDtlId;
                    insertOb[2] = memId;
                    insertOb[3] = childId;
                    insertOb[4] = "null";//drillId;
                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    insertOb[0] = "null";
                    insertOb[1] = folderDtlId;
                    insertOb[2] = memId;
                    insertOb[3] = childId;
                    insertOb[4] = "null";//drillId;
                } else {
                    insertOb[0] = "";
                    insertOb[1] = folderDtlId;
                    insertOb[2] = memId;
                    insertOb[3] = childId;
                    insertOb[4] = "";//drillId;
                }


                String finaddRoleCustomDrillData = buildQuery(addRoleCustomDrillData, insertOb);
                insertRoleDrillList.add(finaddRoleCustomDrillData);
            }
            if (insertRoleDrillList != null && !insertRoleDrillList.isEmpty()) {
                executeMultiple(insertRoleDrillList);
            }
        } else if (folderType.equalsIgnoreCase("Facts")) {
            Object[] Obj = new Object[1];
            // Obj[0] = grpId;
            Obj[0] = extraTab;
            finalQuery = buildQuery(getBussFactsInfoByGrpIdQuery, Obj);
            retObj = new PbReturnObject();
            if (!extraTab.toString().equalsIgnoreCase("")) {
                retObj = execSelectSQL(finalQuery);
            }
            tableColNames = retObj.getColumnNames();
            int subFoldTabId = 0;
            for (int i = 0; i < retObj.getRowCount(); i++) {

                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                    subFoldTabId = getSequenceNumber("select PRG_USER_SUB_FOLDER_TABLES_SEQ.NEXTVAL from dual");
                    Obj = new Object[9];
//                    Obj[0] = String.valueOf(subFoldTabId);
                    Obj[0] = folderDtlId;
                    Obj[1] = retObj.getFieldValueString(i, tableColNames[0]);
                    Obj[2] = "N";
                    Obj[3] = "Y";
                    Obj[4] = "N";
                    Obj[5] = retObj.getFieldValueString(i, tableColNames[1]);
                    Obj[6] = "null";
                    Obj[7] = "null";
                    Obj[8] = "null";
                    finalQuery = buildQuery(addUserSubFolderTablesQuery, Obj);
                    alist.add(finalQuery);
                    alist = addUserSubFolderElementsForFacts(alist, retObj.getFieldValueString(i, tableColNames[0]), folderDtlId, String.valueOf(subFoldTabId));

                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
//                    subFoldTabId = getSequenceNumber("select PRG_USER_SUB_FOLDER_TABLES_SEQ.NEXTVAL from dual");
                    Obj = new Object[9];
//                    Obj[0] = String.valueOf(subFoldTabId);
                    Obj[0] = folderDtlId;
                    Obj[1] = retObj.getFieldValueString(i, tableColNames[0]);
                    Obj[2] = "N";
                    Obj[3] = "Y";
                    Obj[4] = "N";
                    Obj[5] = retObj.getFieldValueString(i, tableColNames[1]);
                    Obj[6] = "null";
                    Obj[7] = "null";
                    Obj[8] = "null";
                    finalQuery = buildQuery(addUserSubFolderTablesQuery, Obj);
                    alist.add(finalQuery);
                    alist = addUserSubFolderElementsForFacts(alist, retObj.getFieldValueString(i, tableColNames[0]), folderDtlId, String.valueOf(subFoldTabId));

                } else {
                    subFoldTabId = getSequenceNumber("select PRG_USER_SUB_FOLDER_TABLES_SEQ.NEXTVAL from dual");
                    Obj = new Object[10];
                    Obj[0] = String.valueOf(subFoldTabId);
                    Obj[1] = folderDtlId;
                    Obj[2] = retObj.getFieldValueString(i, tableColNames[0]);
                    Obj[3] = "N";
                    Obj[4] = "Y";
                    Obj[5] = "N";
                    Obj[6] = retObj.getFieldValueString(i, tableColNames[1]);
                    Obj[7] = "";
                    Obj[8] = "";
                    Obj[9] = "";
                    finalQuery = buildQuery(addUserSubFolderTablesQuery, Obj);
                    alist.add(finalQuery);
                    alist = addUserSubFolderElementsForFacts(alist, retObj.getFieldValueString(i, tableColNames[0]), folderDtlId, String.valueOf(subFoldTabId));

                }


            }

        } else if (folderType.equalsIgnoreCase("Buckets")) {
            Object[] Obj = new Object[2];
            Obj[0] = grpId;
            Obj[1] = grpId;
            finalQuery = buildQuery(getBussBucketInfoByGrpIdQuery, Obj);
            retObj = execSelectSQL(finalQuery);
            tableColNames = retObj.getColumnNames();
            int subFoldTabId = 0;
            for (int i = 0; i < retObj.getRowCount(); i++) {
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                    subFoldTabId = getSequenceNumber("select PRG_USER_SUB_FOLDER_TABLES_SEQ.NEXTVAL from dual");

                    Obj = new Object[9];

//                    Obj[0] = String.valueOf(subFoldTabId);
                    Obj[0] = folderDtlId;

                    Obj[1] = retObj.getFieldValueString(i, tableColNames[0]);
                    Obj[2] = "N";
                    Obj[3] = "N";
                    Obj[4] = "Y";
                    Obj[5] = retObj.getFieldValueString(i, tableColNames[1]);
                    Obj[6] = "null";
                    Obj[7] = "null";
                    Obj[8] = "null";
                    finalQuery = buildQuery(addUserSubFolderTablesQuery, Obj);
                    alist.add(finalQuery);
                    alist = addUserSubFolderElements(alist, retObj.getFieldValueString(i, tableColNames[0]), folderDtlId, String.valueOf(subFoldTabId), grpId);
                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
//                    subFoldTabId = getSequenceNumber("select PRG_USER_SUB_FOLDER_TABLES_SEQ.NEXTVAL from dual");

                    Obj = new Object[9];

//                    Obj[0] = String.valueOf(subFoldTabId);
                    Obj[0] = folderDtlId;

                    Obj[1] = retObj.getFieldValueString(i, tableColNames[0]);
                    Obj[2] = "N";
                    Obj[3] = "N";
                    Obj[4] = "Y";
                    Obj[5] = retObj.getFieldValueString(i, tableColNames[1]);
                    Obj[6] = "null";
                    Obj[7] = "null";
                    Obj[8] = "null";
                    finalQuery = buildQuery(addUserSubFolderTablesQuery, Obj);
                    alist.add(finalQuery);
                    alist = addUserSubFolderElements(alist, retObj.getFieldValueString(i, tableColNames[0]), folderDtlId, String.valueOf(subFoldTabId), grpId);
                } else {
                    subFoldTabId = getSequenceNumber("select PRG_USER_SUB_FOLDER_TABLES_SEQ.NEXTVAL from dual");

                    Obj = new Object[10];

                    Obj[0] = String.valueOf(subFoldTabId);
                    Obj[1] = folderDtlId;

                    Obj[2] = retObj.getFieldValueString(i, tableColNames[0]);
                    Obj[3] = "N";
                    Obj[4] = "N";
                    Obj[5] = "Y";
                    Obj[6] = retObj.getFieldValueString(i, tableColNames[1]);
                    Obj[7] = "";
                    Obj[8] = "";
                    Obj[9] = "";
                    finalQuery = buildQuery(addUserSubFolderTablesQuery, Obj);
                    alist.add(finalQuery);
                    alist = addUserSubFolderElements(alist, retObj.getFieldValueString(i, tableColNames[0]), folderDtlId, String.valueOf(subFoldTabId), grpId);
                }



            }
        }
        return alist;
    }

    public ArrayList addUserSubFolderTables(ArrayList alist, String grpId, String folderType, String folderDtlId, String folderId) throws Exception {
        String addUserSubFolderTablesQuery = getResourceBundle().getString("addUserSubFolderTables");
        String addUserSubFolderTablesForDimensionsQuery = getResourceBundle().getString("addUserSubFolderTablesForDimensions");

        String getExtraDimsOfGrp = getResourceBundle().getString("getExtraDimsForRole");
        String getExtraBussTable = getResourceBundle().getString("getExtraBussTable");
        Object det[] = new Object[2];
        det[0] = grpId;
        det[1] = folderDtlId;
        String fingetExtraDimsForRole = buildQuery(getExtraDimsOfGrp, det);
//        String extraDim = "";
        StringBuilder extraDim = new StringBuilder();
        PbReturnObject pbro = execSelectSQL(fingetExtraDimsForRole);
        for (int y = 0; y < pbro.getRowCount(); y++) {
//            extraDim = extraDim + "," + pbro.getFieldValueString(y, "DIM_ID");
            extraDim.append("," ).append( pbro.getFieldValueString(y, "DIM_ID"));
        }
        if (extraDim.length() > 1) {
            extraDim =new StringBuilder( extraDim.substring(1));
        }
        Object folderObj[] = new Object[3];
        folderObj[0] = grpId;
        folderObj[1] = folderDtlId;
        folderObj[2] = folderId;
        String fingetExtraBussTable = buildQuery(getExtraBussTable, folderObj);
        PbReturnObject bussTabObj = execSelectSQL(fingetExtraBussTable);
//        String extraTab = "";
        StringBuilder extraTab = new StringBuilder();
        for (int t = 0; t < bussTabObj.getRowCount(); t++) {
            extraTab.append(",").append( bussTabObj.getFieldValueString(t, "BUSS_TABLE_ID"));
        }
        if (bussTabObj.getRowCount() > 0) {
            extraTab =new StringBuilder( extraTab.substring(1));
        }
        String getBussDimInfoByGrpIdQuery = getResourceBundle().getString("getBussDimInfoByGrpId");
        String getBussFactsInfoByGrpIdQuery = getResourceBundle().getString("getBussFactsInfoByGrpId");
        String getBussBucketInfoByGrpIdQuery = getResourceBundle().getString("getBussBucketInfoByGrpId");
        String finalQuery = "";
        PbReturnObject retObj = null;
        String[] tableColNames = null;

        if (folderType.equalsIgnoreCase("Dimensions")) {
            Object[] Obj = new Object[2];
            Obj[0] = grpId;
            Obj[1] = extraDim;
            finalQuery = buildQuery(getBussDimInfoByGrpIdQuery, Obj);
            retObj = execSelectSQL(finalQuery);
            tableColNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                int subFoldTabId = getSequenceNumber("select PRG_USER_SUB_FOLDER_TABLES_SEQ.NEXTVAL from dual");
                Obj = new Object[15];
                Obj[0] = String.valueOf(subFoldTabId);
                Obj[1] = folderDtlId;
                Obj[2] = retObj.getFieldValueString(i, tableColNames[3]);
                Obj[3] = "Y";
                Obj[4] = "N";
                Obj[5] = "N";
                Obj[6] = retObj.getFieldValueString(i, tableColNames[1]);
                Obj[7] = retObj.getFieldValueString(i, tableColNames[0]);
                Obj[8] = retObj.getFieldValueString(i, tableColNames[2]);
                Obj[9] = retObj.getFieldValueString(i, tableColNames[4]);
                Obj[10] = retObj.getFieldValueString(i, tableColNames[5]);
                Obj[11] = retObj.getFieldValueString(i, tableColNames[6]);
                Obj[12] = retObj.getFieldValueString(i, tableColNames[7]);
                Obj[13] = retObj.getFieldValueString(i, tableColNames[8]);
                Obj[14] = retObj.getFieldValueString(i, tableColNames[9]);
                finalQuery = buildQuery(addUserSubFolderTablesForDimensionsQuery, Obj);
                alist.add(finalQuery);
                alist = addUserSubFolderElements(alist, retObj.getFieldValueString(i, tableColNames[3]), folderDtlId, String.valueOf(subFoldTabId), grpId);
            }
        } else if (folderType.equalsIgnoreCase("Facts")) {
            Object[] Obj = new Object[1];
            // Obj[0] = grpId;
            Obj[0] = extraTab;
            finalQuery = buildQuery(getBussFactsInfoByGrpIdQuery, Obj);
            retObj = execSelectSQL(finalQuery);
            tableColNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                int subFoldTabId = getSequenceNumber("select PRG_USER_SUB_FOLDER_TABLES_SEQ.NEXTVAL from dual");
                Obj = new Object[10];
                Obj[0] = String.valueOf(subFoldTabId);
                Obj[1] = folderDtlId;
                Obj[2] = retObj.getFieldValueString(i, tableColNames[0]);
                Obj[3] = "N";
                Obj[4] = "Y";
                Obj[5] = "N";
                Obj[6] = retObj.getFieldValueString(i, tableColNames[1]);
                Obj[7] = "";
                Obj[8] = "";
                Obj[9] = "";
                finalQuery = buildQuery(addUserSubFolderTablesQuery, Obj);
                alist.add(finalQuery);
                alist = addUserSubFolderElementsForFacts(alist, retObj.getFieldValueString(i, tableColNames[0]), folderDtlId, String.valueOf(subFoldTabId));
            }

        } else if (folderType.equalsIgnoreCase("Buckets")) {
            Object[] Obj = new Object[2];
            Obj[0] = grpId;
            Obj[1] = grpId;
            finalQuery = buildQuery(getBussBucketInfoByGrpIdQuery, Obj);
            retObj = execSelectSQL(finalQuery);
            tableColNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                int subFoldTabId = getSequenceNumber("select PRG_USER_SUB_FOLDER_TABLES_SEQ.NEXTVAL from dual");
                Obj = new Object[10];
                Obj[0] = String.valueOf(subFoldTabId);
                Obj[1] = folderDtlId;
                Obj[2] = retObj.getFieldValueString(i, tableColNames[0]);
                Obj[3] = "N";
                Obj[4] = "N";
                Obj[5] = "Y";
                Obj[6] = retObj.getFieldValueString(i, tableColNames[1]);
                Obj[7] = "";
                Obj[8] = "";
                Obj[9] = "";
                finalQuery = buildQuery(addUserSubFolderTablesQuery, Obj);
                alist.add(finalQuery);
                alist = addUserSubFolderElements(alist, retObj.getFieldValueString(i, tableColNames[0]), folderDtlId, String.valueOf(subFoldTabId), grpId);
            }
        }
        return alist;
    }

    public ArrayList addUserSubFolderElementsForFacts(ArrayList alist, String bussTableId, String folderDtlId, String subFoldTabId) throws Exception {
        String getElementsInfoQuery = getResourceBundle().getString("getElementsInfo");
        String addUserSubFolderElementsForFactsQuery = getResourceBundle().getString("addUserSubFolderElementsForFacts");
        String insertExtraElementsForFactsQuery = getResourceBundle().getString("insertExtraElementsForFacts");
        PbReturnObject retObj = null;
        String finalQuery = "";
        String[] tableColNames = null;
        String refId = "";
        int seq = 0;
        Object[] Obj = new Object[3];
        Obj[0] = folderDtlId;
        Obj[1] = subFoldTabId;
        Obj[2] = bussTableId;
        finalQuery = buildQuery(getElementsInfoQuery, Obj);
        retObj = execSelectSQL(finalQuery);
        tableColNames = retObj.getColumnNames();
        for (int i = 0; i < retObj.getRowCount(); i++) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                seq = getSequenceNumber("select PRG_USER_SUB_FLDR_ELEMENTS_SEQ.NEXTVAL from dual");
//                refId = String.valueOf(seq);
                Obj = new Object[11];
//                Obj[0] = String.valueOf(seq);
                Obj[0] = retObj.getFieldValueString(i, tableColNames[0]);
                Obj[1] = retObj.getFieldValueString(i, tableColNames[1]);
                Obj[2] = retObj.getFieldValueString(i, tableColNames[2]);
                Obj[3] = retObj.getFieldValueString(i, tableColNames[3]);
                Obj[4] = retObj.getFieldValueString(i, tableColNames[4]);
                Obj[5] = retObj.getFieldValueString(i, tableColNames[5]);
                Obj[6] = retObj.getFieldValueString(i, tableColNames[6]);
                Obj[7] = retObj.getFieldValueString(i, tableColNames[7]);
//                Obj[8] = refId;
                Obj[8] = 0;
                Obj[9] = retObj.getFieldValueString(i, tableColNames[9]);
                Obj[10] = retObj.getFieldValueString(i, tableColNames[8]);

            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {

                seq = getSequenceNumber("select Last_Insert_Id(ELEMENT_ID) from prg_user_sub_folder_elements order by 1 desc limit 1");
                refId = String.valueOf(seq + 1);
                Obj = new Object[11];
//                Obj[0] = String.valueOf(seq);
                Obj[0] = retObj.getFieldValueString(i, tableColNames[0]);
                Obj[1] = retObj.getFieldValueString(i, tableColNames[1]);
                Obj[2] = retObj.getFieldValueString(i, tableColNames[2]);
                Obj[3] = retObj.getFieldValueString(i, tableColNames[3]);
                Obj[4] = retObj.getFieldValueString(i, tableColNames[4]);
                Obj[5] = retObj.getFieldValueString(i, tableColNames[5]);
                Obj[6] = retObj.getFieldValueString(i, tableColNames[6]);
                Obj[7] = retObj.getFieldValueString(i, tableColNames[7]);
                Obj[8] = refId;
                Obj[9] = retObj.getFieldValueString(i, tableColNames[9]);
                Obj[10] = retObj.getFieldValueString(i, tableColNames[8]);

            } else {

                seq = getSequenceNumber("select PRG_USER_SUB_FLDR_ELEMENTS_SEQ.NEXTVAL from dual");
                refId = String.valueOf(seq);
                Obj = new Object[12];
                Obj[0] = String.valueOf(seq);
                Obj[1] = retObj.getFieldValueString(i, tableColNames[0]);
                Obj[2] = retObj.getFieldValueString(i, tableColNames[1]);
                Obj[3] = retObj.getFieldValueString(i, tableColNames[2]);
                Obj[4] = retObj.getFieldValueString(i, tableColNames[3]);
                Obj[5] = retObj.getFieldValueString(i, tableColNames[4]);
                Obj[6] = retObj.getFieldValueString(i, tableColNames[10]);
                Obj[7] = retObj.getFieldValueString(i, tableColNames[6]);
                Obj[8] = retObj.getFieldValueString(i, tableColNames[7]);
                Obj[9] = refId;
                Obj[10] = retObj.getFieldValueString(i, tableColNames[9]);
                Obj[11] = retObj.getFieldValueString(i, tableColNames[8]);

            }


            finalQuery = buildQuery(addUserSubFolderElementsForFactsQuery, Obj);
//             alist.add(finalQuery);
            execUpdateSQL(finalQuery);
//            executeSelectSQL(finalQuery);

            if (retObj.getFieldValueString(i, tableColNames[8]) != null && !("".equalsIgnoreCase(retObj.getFieldValueString(i, tableColNames[8]))) && (retObj.getFieldValueString(i, tableColNames[6]) != null && !("".equalsIgnoreCase(retObj.getFieldValueString(i, tableColNames[6]))))) {

                if (!(retObj.getFieldValueString(i, tableColNames[8]).trim().equalsIgnoreCase("NULL"))
                        && !(retObj.getFieldValueString(i, tableColNames[8]).trim().equalsIgnoreCase("(NULL)"))) //  retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("CALCULATED")
                //      || retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("Number") || retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("varchar2"))
                {
                 
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        String insertExtraElementsForFactsQuerySql = getResourceBundle().getString("insertExtraElementsForFactsSql");
                        Obj = new Object[2];
                        //modified by Nazneen
                        //Obj[0] = String.valueOf(seq);
                        Obj[0] = bussTableId;
                        Obj[1] = "select ident_current('PRG_USER_SUB_FOLDER_ELEMENTS')";
                        //added by Nazneen
                        finalQuery = buildQuery(insertExtraElementsForFactsQuerySql, Obj);
                        execUpdateSQL(finalQuery);
                    } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        String insertExtraElementsForFactsQuerySql = getResourceBundle().getString("insertExtraElementsForFactsSql");
                        Obj = new Object[2];
                        //modified by Nazneen
                        //Obj[0] = String.valueOf(seq);
                        Obj[0] = bussTableId;
                        Obj[1] = getSequenceNumber("select Last_Insert_Id(ELEMENT_ID) from PRG_USER_SUB_FOLDER_ELEMENTS order by 1 desc limit 1");
                        //added by Nazneen
                        finalQuery = buildQuery(insertExtraElementsForFactsQuerySql, Obj);
                        execUpdateSQL(finalQuery);
                    } else {
                        seq = getSequenceNumber("select PRG_USER_SUB_FLDR_ELEMENTS_SEQ.NEXTVAL from dual");

                        Obj = new Object[2];
                        Obj[0] = bussTableId;
                        //Obj[0] = String.valueOf(seq);
                        Obj[1] = refId;
                        finalQuery = buildQuery(insertExtraElementsForFactsQuery, Obj);
                        alist.add(finalQuery);
                    }

//                    finalQuery = buildQuery(insertExtraElementsForFactsQuery, Obj);
//                     alist.add(finalQuery);
                    ////////////////////////////////////////////////////////////////////////////////////.println.println(" extra "+finalQuery);
//                    executeSelectSQL(finalQuery);

                }

            }
        }
        //added by nazneen
//        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
        String updatequery = "update PRG_USER_SUB_FOLDER_ELEMENTS set REF_ELEMENT_ID = ELEMENT_ID where REF_ELEMENT_TYPE = 1 and BUSS_TABLE_ID = " + bussTableId;
        execUpdateSQL(updatequery);
//        }
        return alist;

    }

    public ArrayList addUserSubFolderElements(ArrayList alist, String bussTableId, String folderDtlId, String subFoldTabId, String grpId) throws Exception {
        String addUserSubFolderElementsQuery = getResourceBundle().getString("addUserSubFolderElements");
        Object[] Obj;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            Obj = new Object[3];
            Obj[0] = folderDtlId;
            Obj[1] = "ident_current('PRG_USER_SUB_FOLDER_TABLES')";
            Obj[2] = bussTableId;
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            Obj = new Object[4];
            Obj[0] = folderDtlId;
            Obj[1] = "(select Last_Insert_Id(SUB_FOLDER_TAB_ID) from PRG_USER_SUB_FOLDER_TABLES order by 1 desc limit 1)";
            Obj[2] = String.valueOf(getSequenceNumber("select Last_Insert_Id(ELEMENT_ID) from PRG_USER_SUB_FOLDER_ELEMENTS order by 1 desc limit 1"));
            Obj[3] = bussTableId;
        } else {
            Obj = new Object[3];
            Obj[0] = folderDtlId;
            Obj[1] = subFoldTabId;//ident_current('PRG_USER_SUB_FOLDER_TABLES')
            Obj[2] = bussTableId;
        }

        String finalQuery;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            String getBusColQry = getResourceBundle().getString("getBussColsForTab");
            Obj = new Object[1];
            Obj[0] = bussTableId;

            finalQuery = buildQuery(getBusColQry, Obj);
            //.println("finalQuery is\t"+finalQuery);
            PbReturnObject busColsRetObj = execSelectSQL(finalQuery);
            for (int i = 0; i < busColsRetObj.rowCount; i++) {
                Obj = new Object[4];
                Obj[0] = folderDtlId;
                Obj[1] = "ident_current('PRG_USER_SUB_FOLDER_TABLES')";
                Obj[2] = bussTableId;
                Obj[3] = busColsRetObj.getFieldValueString(i, "BUSS_COLUMN_ID");

                finalQuery = buildQuery(addUserSubFolderElementsQuery, Obj);
                alist.add(finalQuery);
                //.println("Arun+++"+finalQuery);
                String updateQuery = "update PRG_USER_SUB_FOLDER_ELEMENTS set REF_ELEMENT_ID=ident_current('PRG_USER_SUB_FOLDER_ELEMENTS') where ELEMENT_ID=ident_current('PRG_USER_SUB_FOLDER_ELEMENTS')";
                alist.add(updateQuery);
            }
//            String updateQuery = "update PRG_USER_SUB_FOLDER_ELEMENTS set REF_ELEMENT_ID=ident_current('PRG_USER_SUB_FOLDER_ELEMENTS') where ELEMENT_ID=ident_current('PRG_USER_SUB_FOLDER_ELEMENTS')";
//            alist.add(updateQuery);
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            String getBusColQry = getResourceBundle().getString("getBussColsForTab");
            Obj = new Object[1];
            Obj[0] = bussTableId;

            finalQuery = buildQuery(getBusColQry, Obj);
            //.println("finalQuery is\t"+finalQuery);
            PbReturnObject busColsRetObj = execSelectSQL(finalQuery);
            for (int i = 0; i < busColsRetObj.rowCount; i++) {
                Obj = new Object[5];
                Obj[0] = folderDtlId;
                Obj[1] = "(select Last_Insert_Id(SUB_FOLDER_TAB_ID) from PRG_USER_SUB_FOLDER_TABLES order by 1 desc limit 1)";
                int refID = 0;
                refID = getSequenceNumber("select Last_Insert_Id(ELEMENT_ID) from PRG_USER_SUB_FOLDER_ELEMENTS order by 1 desc limit 1");
//                Obj[2] = String.valueOf(getSequenceNumber("select Last_Insert_Id(ELEMENT_ID) from PRG_USER_SUB_FOLDER_ELEMENTS order by 1 desc limit 1"));
                Obj[2] = String.valueOf(refID + 1);
                Obj[3] = bussTableId;
                Obj[4] = busColsRetObj.getFieldValueString(i, 0);

                finalQuery = buildQuery(addUserSubFolderElementsQuery, Obj);
                alist.add(finalQuery);
                //.println("Arun+++"+finalQuery);
                //commented by Nazneen
//                String updateQuery = "update PRG_USER_SUB_FOLDER_ELEMENTS set REF_ELEMENT_ID="+String.valueOf(getSequenceNumber("select Last_Insert_Id(ELEMENT_ID) from PRG_USER_SUB_FOLDER_ELEMENTS order by 1 desc limit 1"))+" where ELEMENT_ID="+String.valueOf(getSequenceNumber("select Last_Insert_Id(ELEMENT_ID) from PRG_USER_SUB_FOLDER_ELEMENTS order by 1 desc limit 1"));
//                alist.add(updateQuery);
            }
            //added by Nazneen
            String updateQuery = "update PRG_USER_SUB_FOLDER_ELEMENTS set REF_ELEMENT_ID = ELEMENT_ID where buss_table_id = " + bussTableId + " and REF_ELEMENT_TYPE = 1 ";
            alist.add(updateQuery);
//            String updateQuery = "update PRG_USER_SUB_FOLDER_ELEMENTS set REF_ELEMENT_ID=ident_current('PRG_USER_SUB_FOLDER_ELEMENTS') where ELEMENT_ID=ident_current('PRG_USER_SUB_FOLDER_ELEMENTS')";
//            alist.add(updateQuery);
        } else {
            finalQuery = buildQuery(addUserSubFolderElementsQuery, Obj);
            alist.add(finalQuery);
        }

        //added by susheela to insert the table levels start
        //  BusinessTablePaths bt = new BusinessTablePaths();
        // to insert in the business tables path maps
        // bt.getPathsToInsertForFact(bussTableId, grpId);
        // HashMap hm = bt.getpathFromBusinessTableForTLevels(bussTableId);
        // to insert in the business tables levels
        // bt.AddParentChildLevels(hm);

        return alist;

    }

    //added on 5feb
    public void migrateExtraColumnToRole(String roleId, String tabId, String colId) throws Exception {
        ArrayList al = new ArrayList();
        String eleQ = "SELECT distinct SUB_FOLDER_ID, BUSS_TABLE_ID,SUB_FOLDER_TAB_ID FROM PRG_USER_SUB_FOLDER_ELEMENTS where "
                + "sub_folder_id=(select sub_folder_id from prg_user_folder_detail where folder_id=" + roleId + ""
                + " and sub_folder_type='Facts') and buss_table_id=" + tabId;
        PbReturnObject forlderInfo = execSelectSQL(eleQ);

        String colDetQ = "select * from prg_grp_buss_table_details where buss_column_id=" + colId + " and buss_table_id=" + tabId;
        PbReturnObject colDetObj = execSelectSQL(colDetQ);

        String subFolderId = "";
        String subFolderTabId = "";
        subFolderId = forlderInfo.getFieldValueString(0, 0);
        subFolderTabId = forlderInfo.getFieldValueString(0, 2);
        Object inObj[] = new Object[4];
        inObj[0] = subFolderId;
        inObj[1] = subFolderTabId;
        inObj[2] = tabId;
        inObj[3] = colId;
        String insertQuery = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            insertQuery = "INSERT INTO PRG_USER_SUB_FOLDER_ELEMENTS ( SUB_FOLDER_ID, BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME,"
                    + " USER_COL_NAME,USER_COL_DESC, USER_COL_TYPE, SUB_FOLDER_TAB_ID,REF_ELEMENT_ID,REF_ELEMENT_TYPE,DEFAULT_AGGREGATION,"
                    + " USE_REPORT_FLAG)   SELECT  '&' as SUB_FOLDER_ID,a.BUSS_TABLE_ID, a.buss_column_id,"
                    + " a.column_name,a.column_disp_name,a.column_display_desc,a.column_type,'&' as SUB_FOLDER_TAB_ID,"
                    + " (ident_current('PRG_USER_SUB_FOLDER_ELEMENTS')),1 AS REF_ELEMENT_TYPE,a.default_aggregation,'Y' FROM PRG_GRP_BUSS_TABLE_DETAILS"
                    + " a where  a.buss_table_id='&' and a.buss_column_id='&'";
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
//     if(forlderInfo.rowCount !=0){
            insertQuery = "INSERT INTO PRG_USER_SUB_FOLDER_ELEMENTS ( SUB_FOLDER_ID, BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME,"
                    + " USER_COL_NAME,USER_COL_DESC, USER_COL_TYPE, SUB_FOLDER_TAB_ID,REF_ELEMENT_ID,REF_ELEMENT_TYPE,DEFAULT_AGGREGATION,"
                    + " USE_REPORT_FLAG)   SELECT  '&' as SUB_FOLDER_ID,a.BUSS_TABLE_ID, a.buss_column_id,"
                    + " a.column_name,a.column_disp_name,a.column_display_desc,a.column_type,'&' as SUB_FOLDER_TAB_ID,"
                    + " (select Last_Insert_Id(ELEMENT_ID) From PRG_USER_SUB_FOLDER_ELEMENTS order by 1 desc limit 1),1 AS REF_ELEMENT_TYPE,a.default_aggregation,'Y' FROM PRG_GRP_BUSS_TABLE_DETAILS"
                    + " a where  a.buss_table_id='&' and a.buss_column_id='&'";
//     }else {
//         insertQuery = "INSERT INTO PRG_USER_SUB_FOLDER_ELEMENTS ( BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME,"
//                + " USER_COL_NAME,USER_COL_DESC, USER_COL_TYPE,REF_ELEMENT_ID,REF_ELEMENT_TYPE,DEFAULT_AGGREGATION,"
//                + " USE_REPORT_FLAG)   SELECT  a.BUSS_TABLE_ID, a.buss_column_id,"
//                + " a.column_name,a.column_disp_name,a.column_display_desc,a.column_type,"
//                + " (select Last_Insert_Id(ELEMENT_ID) From PRG_USER_SUB_FOLDER_ELEMENTS order by 1 desc limit 1)),1 AS REF_ELEMENT_TYPE,a.default_aggregation,'Y' FROM PRG_GRP_BUSS_TABLE_DETAILS"
//                + " a where  a.buss_table_id='&' and a.buss_column_id='&'";
//     }
        } else {
            insertQuery = "INSERT INTO PRG_USER_SUB_FOLDER_ELEMENTS (ELEMENT_ID, SUB_FOLDER_ID, BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME,"
                    + " USER_COL_NAME,USER_COL_DESC, USER_COL_TYPE, SUB_FOLDER_TAB_ID,REF_ELEMENT_ID,REF_ELEMENT_TYPE,DEFAULT_AGGREGATION,"
                    + " USE_REPORT_FLAG) SELECT (PRG_USER_SUB_FLDR_ELEMENTS_SEQ.NEXTVAL) as ELEMENT_ID,'&' as SUB_FOLDER_ID,a.BUSS_TABLE_ID, a.buss_column_id,"
                    + "  a.column_name,a.column_disp_name,a.column_display_desc,a.column_type,'&' as SUB_FOLDER_TAB_ID,"
                    + " (PRG_USER_SUB_FLDR_ELEMENTS_SEQ.CURRVAL),1 AS REF_ELEMENT_TYPE,a.default_aggregation,'Y' FROM PRG_GRP_BUSS_TABLE_DETAILS"
                    + " a where  a.buss_table_id='&' and a.buss_column_id='&'";
        }

        String fininsertQuery = buildQuery(insertQuery, inObj);
        al.add(fininsertQuery);
        String insertExtraElementsForFactsQuery = getResourceBundle().getString("insertExtraElementsForFacts1");
        al.add(insertExtraElementsForFactsQuery);
        //added by Nazneen
        try {
            executeMultiple(al);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        al.clear();
        // 
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            //changed by Nazneen
//        String updatequery="update PRG_USER_SUB_FOLDER_ELEMENTS set REF_ELEMENT_ID=ident_current('PRG_USER_SUB_FOLDER_ELEMENTS') where ELEMENT_ID = ident_current('PRG_USER_SUB_FOLDER_ELEMENTS')";
            String updatequery = "update PRG_USER_SUB_FOLDER_ELEMENTS set REF_ELEMENT_ID = ELEMENT_ID where REF_ELEMENT_TYPE = 1 and buss_col_id=" + colId + " and buss_table_id=" + tabId;
            //.println(" fininsertQuery-al\t "+al);
            al.add(updatequery);
        }
        try {
            executeMultiple(al);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        al.clear();
    }

    public void addExtraFact(String extraTab, String folderDtlId, String folderId) throws Exception {
        String subFolderQ = "Select sub_folder_id from prg_user_folder_detail where folder_id=" + folderId + " and sub_folder_type='Facts'";

        PbReturnObject pbro = execSelectSQL(subFolderQ);

        if (pbro.getRowCount() > 0) {
            folderDtlId = pbro.getFieldValueString(0, 0);
        }

        ArrayList alist = new ArrayList();
        String getBussFactsInfoByGrpIdQuery = getResourceBundle().getString("getBussFactsInfoByGrpId");
        String addUserSubFolderTablesQuery = getResourceBundle().getString("addUserSubFolderTables");
        Object[] Obj = new Object[1];
        String subFolTabId = "";
        // Obj[0] = grpId;
        Obj[0] = extraTab;
        String finalQuery = buildQuery(getBussFactsInfoByGrpIdQuery, Obj);
        PbReturnObject retObj = new PbReturnObject();
        if (!extraTab.equalsIgnoreCase("")) {
            retObj = execSelectSQL(finalQuery);
        }
        String tableColNames[] = retObj.getColumnNames();
        for (int i = 0; i < retObj.getRowCount(); i++) {
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(folderType);

            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {


                //                    subFoldTabId = getSequenceNumber("select PRG_USER_SUB_FOLDER_TABLES_SEQ.NEXTVAL from dual");

                Obj = new Object[9];

//                    Obj[0] = String.valueOf(subFoldTabId);
                Obj[0] = folderDtlId;

                Obj[1] = retObj.getFieldValueString(i, tableColNames[0]);
                Obj[2] = "N";
                Obj[3] = "Y";
                Obj[4] = "N";
                Obj[5] = retObj.getFieldValueString(i, tableColNames[1]);
                Obj[6] = "null";
                Obj[7] = "null";
                Obj[8] = "null";
                finalQuery = buildQuery(addUserSubFolderTablesQuery, Obj);

                //modified by Nazneen
//                  alist.add(finalQuery);
                execUpdateSQL(finalQuery);
                //added by nazneen
                String subFolderTabId = "select TOP 1 SUB_FOLDER_TAB_ID from PRG_USER_SUB_FOLDER_TABLES where BUSS_TABLE_ID =" + extraTab + " order by sub_folder_tab_id desc";
                pbro = execSelectSQL(subFolderTabId);
                if (pbro.getRowCount() > 0) {
                    subFolTabId = pbro.getFieldValueString(0, 0);
                }

                //modified by nazneen
//                  alist = addUserSubFolderElementsForFacts(alist, retObj.getFieldValueString(i, tableColNames[0]), folderDtlId, String.valueOf(subFoldTabId));
                alist = addUserSubFolderElementsForFacts(alist, retObj.getFieldValueString(i, tableColNames[0]), folderDtlId, subFolTabId);
                alist.clear();

            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {


                //                    subFoldTabId = getSequenceNumber("select PRG_USER_SUB_FOLDER_TABLES_SEQ.NEXTVAL from dual");

                Obj = new Object[9];

//                    Obj[0] = String.valueOf(subFoldTabId);
                Obj[0] = folderDtlId;

                Obj[1] = retObj.getFieldValueString(i, tableColNames[0]);
                Obj[2] = "N";
                Obj[3] = "Y";
                Obj[4] = "N";
                Obj[5] = retObj.getFieldValueString(i, tableColNames[1]);
                Obj[6] = "null";
                Obj[7] = "null";
                Obj[8] = "null";
                finalQuery = buildQuery(addUserSubFolderTablesQuery, Obj);

                //modified by Nazneen
//                  alist.add(finalQuery);
                execUpdateSQL(finalQuery);
                //added by nazneen
                String subFolderTabId = "select SUB_FOLDER_TAB_ID from PRG_USER_SUB_FOLDER_TABLES where BUSS_TABLE_ID =" + extraTab + " order by sub_folder_tab_id desc limit 1";
                pbro = execSelectSQL(subFolderTabId);
                if (pbro.getRowCount() > 0) {
                    subFolTabId = pbro.getFieldValueString(0, 0);
                }

                //modified by nazneen
//                  alist = addUserSubFolderElementsForFacts(alist, retObj.getFieldValueString(i, tableColNames[0]), folderDtlId, String.valueOf(subFoldTabId));
                alist = addUserSubFolderElementsForFacts(alist, retObj.getFieldValueString(i, tableColNames[0]), folderDtlId, subFolTabId);
                alist.clear();

            } else {
                int subFoldTabId = getSequenceNumber("select PRG_USER_SUB_FOLDER_TABLES_SEQ.NEXTVAL from dual");

                Obj = new Object[10];

                Obj[0] = String.valueOf(subFoldTabId);
                Obj[1] = folderDtlId;

                Obj[2] = retObj.getFieldValueString(i, tableColNames[0]);
                Obj[3] = "N";
                Obj[4] = "Y";
                Obj[5] = "N";
                Obj[6] = retObj.getFieldValueString(i, tableColNames[1]);
                Obj[7] = "";
                Obj[8] = "";
                Obj[9] = "";

                finalQuery = buildQuery(addUserSubFolderTablesQuery, Obj);
                alist.add(finalQuery);
                alist = addUserSubFolderElementsForFacts(alist, retObj.getFieldValueString(i, tableColNames[0]), folderDtlId, String.valueOf(subFoldTabId));
            }
        }


//         for (int h = 0; h < alist.size(); h++) {
//            
//        }

        boolean flag = executeMultiple(alist);

        //added by nazneen
//        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                    String updatequery="update PRG_USER_SUB_FOLDER_ELEMENTS set REF_ELEMENT_ID = ELEMENT_ID where REF_ELEMENT_ID = 0 and REF_ELEMENT_TYPE = 1 and SUB_FOLDER_TAB_ID = "+subFolTabId;
//                    
//                    execUpdateSQL(updatequery);
//        }

    }
}
