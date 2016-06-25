/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.userType;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 *
 * @author Administrator
 */
class UserTypeprivDAO extends PbDb {

    public static Logger logger = Logger.getLogger(UserTypeprivDAO.class);

    void saveUserTypePrevileges(ArrayList queries, ArrayList delQ) throws Exception {
        boolean a = executeMultiple(delQ);
        //.println("a=="+a);
        boolean b = executeMultiple(queries);
        //.println("b=="+b);
    }

    public HashMap shwSuperPriv() throws Exception {

        PbReturnObject shwParentRO, shwChildRO, shwInChildRO = new PbReturnObject();
        HashMap returnMap = new HashMap();
        HashMap retObjMap = new HashMap();
        HashMap childObjMap = new HashMap();
        HashMap InnerchildObjMap = new HashMap();
        String[] values = {"DashBoard Viewer", "DashBoard Designer", "Report Viewer", "Report Designer"};
        String shwParentprivQry = "";
        String shwChildprivQry = "";
        String shwInChildprivQry = "";
        //  //.println("length--" + values.length);
        PbDb pbdb = new PbDb();
        try {
            for (int i = 0; i < values.length; i++) {
                shwParentprivQry = "select distinct PRIVILEGE_TYPE,PAGE_TYPE from SUPER_PRG_PRIVILEGE_MASTER where page_type='" + values[i] + "'";
                // //.println("shwParentprivQry---" + shwParentprivQry);
                shwParentRO = pbdb.execSelectSQL(shwParentprivQry);
                if (shwParentRO.getRowCount() > 0) {
                    retObjMap.put(values[i], shwParentRO);
                    for (int k = 0; k < shwParentRO.getRowCount(); k++) {
                        String privType = shwParentRO.getFieldValueString(k, "PRIVILEGE_TYPE");
                        String pageType = shwParentRO.getFieldValueString(k, "PAGE_TYPE");

                        shwChildprivQry = "select PRIVILEGE_NAME,PRIV_MASTER_ID from SUPER_PRG_PRIVILEGE_MASTER where PRIVILEGE_TYPE='" + privType + "' and page_type='" + pageType + "'";
                        //  //.println("shwChildprivQry---" + shwChildprivQry);
                        shwChildRO = pbdb.execSelectSQL(shwChildprivQry);
                        //  //.println("row---" + shwChildRO.getRowCount());
                        if (shwChildRO.getRowCount() > 0) {
                            childObjMap.put("shwChildRO" + values[i] + k, shwChildRO);
                        }
                        if (shwChildRO.getRowCount() > 0) {
                            for (int l = 0; l < shwChildRO.getRowCount(); l++) {
                                int masterId = shwChildRO.getFieldValueInt(l, "PRIV_MASTER_ID");
                                shwInChildprivQry = "select PRIV_CHILD_NAME from SUPER_PRG_PRIVILEGE_DETAILS where PRIV_MASTER_ID=" + masterId;
                                //  //.println("shwChildprivQry---" + shwInChildprivQry);
                                shwInChildRO = pbdb.execSelectSQL(shwInChildprivQry);
                                //  //.println("row---" + shwInChildRO.getRowCount());
                                if (shwInChildRO.getRowCount() > 0) {
                                    InnerchildObjMap.put("shwInChildRO" + masterId, shwInChildRO);
                                }
                            }
                        }
                    }
                }
            }
            // //.println("childObjMap--" + childObjMap);
            // //.println("retObjMap---" + retObjMap.keySet());
            // //.println("InnerchildObjMap----" + InnerchildObjMap);
        } catch (Exception e) {
            logger.error("Exception:", e);
            retObjMap = null;
        }
        returnMap.put("hashmap", retObjMap);
        returnMap.put("childObjMap", childObjMap);
        returnMap.put("InnerchildMap", InnerchildObjMap);

        return returnMap;
    }

    public void saveData(HashMap selectedData) throws Exception {
        PbReturnObject pbroMain, pbroPriv = new PbReturnObject();
        PbDb pbdb = new PbDb();
        String[] mapKeys = (String[]) selectedData.keySet().toArray(new String[0]);
        for (int i = 0; i < mapKeys.length; i++) {
            HashMap mainprevilizes = (HashMap) selectedData.get(mapKeys[i]);
            String[] mainprevKeySet = (String[]) mainprevilizes.keySet().toArray(new String[0]);
            ArrayList childsAL = null;
            ArrayList qryAL = new ArrayList();
            ArrayList innerQry = new ArrayList();
            for (int j = 0; j < mainprevKeySet.length; j++) {
                String strqry = "insert into PRG_ADMIN_PREVILAGES(PREVILAGE_ID,PREVILAGE_NAME,PRIVILAGE_TYPE) values(PRG_ADMIN_PREVILAGES_SEQ.nextval,'" + mainprevKeySet[j] + "','" + mapKeys[i] + "')";
                pbdb.execUpdateSQL(strqry);
                String privQry = "select PREVILAGE_ID from PRG_ADMIN_PREVILAGES where PREVILAGE_NAME='" + mainprevKeySet[j] + "'";
                pbroPriv = pbdb.execSelectSQL(privQry);
                ////.println("pbroPriv.getFieldValueInt-----" + pbroPriv.getFieldValueInt(0, "PREVILAGE_ID"));
                childsAL = (ArrayList) mainprevilizes.get(mainprevKeySet[j]);
                // //.println("childsHM--" + childsAL);
                if (!childsAL.contains("null")) {
                    for (int k = 0; k < childsAL.size(); k++) {
                        // //.println("childsAL.get(k)--------" + childsAL.get(k));
                        innerQry.add("insert into PRG_USER_REP_PREVILAGES(CHILD_ID,PREVILAGE_ID,USER_PREVILAGE_NAME,USER_PREVILAGE_TYPE) values(PRG_USER_REP_PREVILAGES_SEQ.nextval," + pbroPriv.getFieldValueInt(0, "PREVILAGE_ID") + ",'" + childsAL.get(k) + "','" + mainprevKeySet[j] + "')");
                    }
                }
                pbdb.executeMultiple(innerQry);
                innerQry = new ArrayList();
            }
        }
    }

    public void saveDataReportDesigner(HashMap selectedData) throws Exception {
        //.println("selectedDataforHM--------"+selectedData);
        PbReturnObject pbroMain, pbroPriv = new PbReturnObject();
        PbDb pbdb = new PbDb();
        String[] mapKeys = (String[]) selectedData.keySet().toArray(new String[0]);
        for (int i = 0; i < mapKeys.length; i++) {
            HashMap mainprevilizes = (HashMap) selectedData.get(mapKeys[i]);
            String[] mainprevKeySet = (String[]) mainprevilizes.keySet().toArray(new String[0]);
            ArrayList childsAL = null;
            ArrayList qryAL = new ArrayList();
            ArrayList innerQry = new ArrayList();
            for (int j = 0; j < mainprevKeySet.length; j++) {
                String strqry = "insert into PRG_ADMIN_PREVILAGES(PREVILAGE_ID,PREVILAGE_NAME,PRIVILAGE_TYPE) values(PRG_ADMIN_PREVILAGES_SEQ.nextval,'" + mainprevKeySet[j] + "','" + mapKeys[i] + "')";
                pbdb.execUpdateSQL(strqry);
                String privQry = "select PREVILAGE_ID from PRG_ADMIN_PREVILAGES where PREVILAGE_NAME='" + mainprevKeySet[j] + "'";
                pbroPriv = pbdb.execSelectSQL(privQry);
                ////.println("pbroPriv.getFieldValueInt-----" + pbroPriv.getFieldValueInt(0, "PREVILAGE_ID"));
                childsAL = (ArrayList) mainprevilizes.get(mainprevKeySet[j]);
                // //.println("childsHM--" + childsAL);
                if (!childsAL.contains("null")) {
                    for (int k = 0; k < childsAL.size(); k++) {
                        // //.println("childsAL.get(k)--------" + childsAL.get(k));
                        innerQry.add("insert into PRG_USER_REP_PREVILAGES(CHILD_ID,PREVILAGE_ID,USER_PREVILAGE_NAME,USER_PREVILAGE_TYPE) values(PRG_USER_REP_PREVILAGES_SEQ.nextval," + pbroPriv.getFieldValueInt(0, "PREVILAGE_ID") + ",'" + childsAL.get(k) + "','" + mainprevKeySet[j] + "')");
                    }
                }
                pbdb.executeMultiple(innerQry);
                innerQry = new ArrayList();
            }
        }
    }
}
