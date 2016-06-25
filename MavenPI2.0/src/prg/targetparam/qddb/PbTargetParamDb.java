package prg.targetparam.qddb;

import java.sql.SQLException;
import java.util.*;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.db.Session;
import prg.targetparam.qdparams.PbTargetParamParams;
import prg.targetparam.qdparams.PbTargetValuesParam;
import utils.db.ProgenConnection;

public class PbTargetParamDb extends PbDb {

    public static Logger logger = Logger.getLogger(PbTargetParamDb.class);
//    private ResourceBundle resBundle = null;
    ResourceBundle resourceBundle;
    //for SQL Server insertion into PRG_USER_SUB_FOLDER_TABLES MAINTAIN PRIMARY KEY SEPARATE
//    int subFolderTabId;
    //for SQL Server insertion into PRG_USER_SUB_FLDR_ELEMENTS MAINTAIN PRIMARY KEY SEPARATE
//    int subFolderEleId;

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = ResourceBundle.getBundle("prg.targetparam.qddb.PbTargetParamResourceBundleSqlserver");
            } else {
                resourceBundle = ResourceBundle.getBundle("prg.targetparam.qddb.PbTargetParamResourceBundle");
            }
        }

        return resourceBundle;
    }
//    public PbTargetParamDb()
//      {
//       resBundle = ResourceBundle.getBundle("prg.targetparam.qddb.PbTargetParamResourceBundle");
//      }

    public PbReturnObject getTargetsListU(Session ppmp) throws Exception {
        PbReturnObject pbro = null;
        String query = getResourceBundle().getString("getTargetsListU");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("getTargetList query is: "+query);

        PbTargetParamParams targetParams = (PbTargetParamParams) ppmp.getObject("prg.targetparam.qdparams.PbTargetParamParams");
        Object obj[] = new Object[1];
        obj[0] = targetParams.getUserId();

        ////////////////////////////////////////////////////////////////////////.println.println("targetParams.getUserId() is: "+targetParams.getUserId());
        String finalQuery = null;
        finalQuery = buildQuery(query, obj);
        //////////////////////////////////////////////////////////////////////////////////.println.println("getTargetList finalQuery is: "+finalQuery);

        try {
            //pbro = execSelectSQL(finalQuery);
            pbro = execSelectSQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return pbro;
    }

    public PbReturnObject getDurationLovs(Session ppmp) throws Exception {
        PbReturnObject pbro = null;
        String query = getResourceBundle().getString("getDurationLovs");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("getDurationLovs query is: "+query);

        PbTargetParamParams targetParams = (PbTargetParamParams) ppmp.getObject("prg.targetparam.qdparams.PbTargetParamParams");
        Object obj[] = new Object[1];
        obj[0] = targetParams.getTimeLevel();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("sess.getTimeLevel() is: "+targetParams.getTimeLevel());

        String finalQuery = null;
        finalQuery = buildQuery(query, obj);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("finalQuery is: "+finalQuery);
        try {
            pbro = execSelectSQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return pbro;

    }

    public PbReturnObject getMeasureInfo(String measureId) throws Exception {
        PbReturnObject pbro = new PbReturnObject();
        ////////////////////////////////////////////////////////////////////////.println.println(" measureId "+measureId);
        String query = " select ptm.*,pgm.grp_name from prg_target_master ptm,prg_grp_master pgm where prg_measure_id=" + measureId + " and pgm.grp_id=ptm.business_group";
        pbro = execSelectSQL(query);
        return pbro;
    }

    public PbReturnObject getUserBusinessGroups(Session ppmp) throws Exception {
        PbReturnObject pbro = null;
        String query = "select DISTINCT pgum.grp_id, pgm.grp_name from prg_grp_user_assignment pgum,prg_grp_master pgm,"
                + " target_measure_folder tf where  user_id=& and pgm.grp_id=pgum.grp_id  and tf.bus_group_id=pgm.grp_id";
        //resBundle.getString("getUserBusinessGroups");
        // query = "select DISTINCT pgum.grp_id, pgm.grp_name from prg_grp_user_assignment pgum,prg_grp_master pgm where user_id=& and pgm.grp_id=pgum.grp_id";
        query = " select * from prg_grp_user_folder_assignment where user_id=&";
        ////////////////////////////.println("getUserBusinessAreas query is: "+query);

        PbTargetParamParams targetParams = (PbTargetParamParams) ppmp.getObject("prg.targetparam.qdparams.PbTargetParamParams");
        Object obj[] = new Object[1];
        obj[0] = targetParams.getUserId();

        String finalQuery = null;
        finalQuery = buildQuery(query, obj);
        //////////////////////////////////////////////////////////////////////////////////.println.println("getUserBusinessAreas finalQuery is: "+finalQuery);

        try {
            pbro = execSelectSQL(finalQuery);
            query = "  select folder_id,folder_name from prg_user_folder where grp_id "
                    + "  in(select DISTINCT pgum.grp_id from prg_grp_user_assignment pgum,prg_grp_master pgm where user_id=& and pgm.grp_id=pgum.grp_id) order by 2";

            finalQuery = buildQuery(query, obj);
            pbro = execSelectSQL(finalQuery);
            ////////////////////////.println(" finalQuery.,./, "+finalQuery);
            if (pbro.getRowCount() == 0) {
                finalQuery = buildQuery(query, obj);
                pbro = execSelectSQL(finalQuery);
            }
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return pbro;
    }

    public PbReturnObject getAllMeasures(Session ppmp) throws Exception {
        PbReturnObject pbro = null;
        String query = getResourceBundle().getString("getAllMeasures");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("getAllMeasures query is: "+query);

        PbTargetParamParams targetParams = (PbTargetParamParams) ppmp.getObject("prg.targetparam.qdparams.PbTargetParamParams");
        Object obj[] = new Object[1];
        obj[0] = targetParams.getUserId();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("targetParams.getUserId() is: "+targetParams.getUserId());

        String finalQuery = null;
        finalQuery = buildQuery(query, obj);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("getAllMeasures finalQuery is: "+finalQuery);

        try {
            pbro = execSelectSQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return pbro;
    }

    public PbReturnObject getExistedTargets(Session ppmp) throws Exception {
        PbReturnObject pbro = null;
        String query = getResourceBundle().getString("getExistedTargets");

        try {
            pbro = execSelectSQL(query);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return pbro;
    }

    public PbReturnObject getAllMonths(Session ppmp) throws Exception {
        PbReturnObject pbro = null;
        String query = getResourceBundle().getString("getAllMonths");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("getAllMonths query is: "+query);

        try {
            pbro = execSelectSQL(query);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return pbro;
    }

    public PbReturnObject getAllQtrs(Session ppmp) throws Exception {
        PbReturnObject pbro = null;
        String query = getResourceBundle().getString("getAllQtrs");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("getAllQtrs query is: "+query);

        try {
            pbro = execSelectSQL(query);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return pbro;
    }

    public PbReturnObject getAllYears(Session ppmp) throws Exception {
        PbReturnObject pbro = null;
        String query = getResourceBundle().getString("getAllYears");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("getAllYears query is: "+query);

        try {
            pbro = execSelectSQL(query);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return pbro;
    }

    public PbReturnObject getMeasureParameters(Session ppmp) throws Exception {
        PbReturnObject pbro = null;
        // String query = "select puaid.dim_tab_id,puaid.buss_col_id,puaid.buss_col_name,puaid.dim_id,puaid.member_id,puaid.member_name,puaid.buss_table_id,puaid.element_id from prg_target_measure_members ptm, prg_user_all_info_details puaid where measure_id=& and puaid.element_id= ptm.element_id order by 5";
        String query = "SELECT DISTINCT K.VAL_ELEMENT_ID, K.VAL_DISP_NAME,m.info_member_id MEMBER_ID,k.key_dim_id, k.key_buss_col_name,"
                + " k.val_buss_col_id,k.val_member_name MEMBER_NAME FROM PRG_USER_ALL_DDIM_KEY_VAL_ELE K, prg_user_all_ddim_master m ,"
                + " prg_grp_buss_table p where k.key_FOLDER_id in(select bus_folder_id from target_measure_folder where "
                + " bus_group_id in(select business_group from prg_target_master where prg_measure_id=&)) and "
                + " m.info_dim_id=K.KEY_DIM_ID and K.VAL_DISP_NAME=m.info_member_name and p.buss_table_id= k.key_buss_table_id"
                + " and p.buss_type='Table' and m.info_member_id in(select member_id from prg_target_measure_members where "
                + " measure_id=&) order by k.key_dim_id,m.info_member_id";
        //resBundle.getString("getMeasureParameters");
        query = "select ptmm.member_id, pgdm.member_name from prg_target_measure_members ptmm,prg_grp_dim_member pgdm  where measure_id=& and ptmm.member_id= pgdm.member_id order by member_id";
        //////////////////////////////////////////////////////////////////////////////////.println.println("getAllMeasures query is: "+query);

        PbTargetParamParams targetParams = (PbTargetParamParams) ppmp.getObject("prg.targetparam.qdparams.PbTargetParamParams");
        Object obj[] = new Object[1];
        obj[0] = targetParams.getMeasureId();
        // obj[1] = targetParams.getMeasureId();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("targetParams.getUserId() is: "+targetParams.getUserId());

        String finalQuery = null;
        finalQuery = buildQuery(query, obj);
        //////////////////////////////////////////////////////////////////////////////////.println.println("getAllMeasures finalQuery is:--- "+finalQuery);

        try {
            pbro = execSelectSQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return pbro;
    }

    public PbReturnObject getParameterNames(Session ppmp) throws Exception {
        PbReturnObject pbro = null;
        String query = getResourceBundle().getString("getParameterNames");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("getAllMeasures query is: "+query);

        PbTargetParamParams targetParams = (PbTargetParamParams) ppmp.getObject("prg.targetparam.qdparams.PbTargetParamParams");
        Object obj[] = new Object[1];
        obj[0] = targetParams.getParameterId();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("targetParams.getUserId() is: "+targetParams.getUserId());

        String finalQuery = null;
        finalQuery = buildQuery(query, obj);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("getAllMeasures finalQuery is: "+finalQuery);

        try {
            pbro = execSelectSQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return pbro;
    }

    public PbReturnObject addTargetMaster(Session ppmp) throws Exception {
        PbReturnObject pbro = new PbReturnObject();
        String cols[] = {"TARGET_ID"};
        ArrayList allQ = new ArrayList();
        pbro.setColumnNames(cols);
        PbTargetParamParams targetParams = (PbTargetParamParams) ppmp.getObject("prg.targetparam.qdparams.PbTargetParamParams");
        String targetName = targetParams.getTargetName();
        String targetDesc = targetParams.getTargetDescription();
        String timeLevel = targetParams.getTimeLevel();
        String measureId = targetParams.getMeasureId();
        // String primPrameter = targetParams.getPrimaryParameter();
        String userId = targetParams.getUserId();

        // String getAddTargetParamDetails = "select puaid.dim_tab_id,puaid.buss_col_id,puaid.buss_col_name,puaid.dim_id,puaid.member_id mem_id,puaid.member_name,puaid.buss_table_id,puaid.element_id from prg_target_measure_members ptm, prg_user_all_info_details puaid where puaid.member_id in(&) and measure_id=& and puaid.element_id= ptm.element_id ";
        String getAddTargetParamDetails = "SELECT DISTINCT K.VAL_ELEMENT_ID ELEMENT_ID, K.VAL_DISP_NAME,m.info_member_id MEMBER_ID,"
                + " k.key_dim_id, k.key_buss_col_name, k.val_buss_col_id, k.val_member_name MEMBER_NAME FROM "
                + " PRG_USER_ALL_DDIM_KEY_VAL_ELE K, prg_user_all_ddim_master m , prg_grp_buss_table p where  "
                + " k.key_FOLDER_id in(select bus_folder_id from target_measure_folder where  bus_group_id "
                + " in(select business_group from prg_target_master  where prg_measure_id=&)) and  "
                + " m.info_dim_id=K.KEY_DIM_ID and K.VAL_DISP_NAME=m.info_member_name and p.buss_table_id= k.key_buss_table_id"
                + "  and p.buss_type='Table' and m.info_member_id in(&"
                + " ) order by k.key_dim_id,m.info_member_id";
        //resBundle.getString("getAddTargetParamDetails");
        // Object paramOb[] = new Object[2];
        // paramOb[0] = measureId;
        // paramOb[1] = targetParams.getPrimaryParameter();

        // String fingetAddTargetParamDetails = buildQuery(getAddTargetParamDetails,paramOb);
        //////////////////////////////////////////////////////////////////////////////////.println.println(" fingetAddTargetParamDetails .. "+fingetAddTargetParamDetails);
        // PbReturnObject paramObj = execSelectSQL(fingetAddTargetParamDetails);
        // String element_id = paramObj.getFieldValueString(0,"ELEMENT_ID");
        String startDate = "";
        String endDate = "";
        String startMonth = "";
        String endMonth = "";
        String startQtr = "";
        String endQtr = "";
        String startYear = "";
        String endYear = "";
        //prg_target_master_SEQ
        String sequenceQuery = getResourceBundle().getString("getSequenceNumber");
        Object tarMasterObj[] = new Object[1];
        tarMasterObj[0] = "PRG_TARGET_MASTER_SEQ";
        String tarMasQuery = buildQuery(sequenceQuery, tarMasterObj);
        int target_id = getSequenceNumber(tarMasQuery);

        String addTargetMaster = getResourceBundle().getString("addTargetMaster");
        // //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" timeLevel "+timeLevel);

        if (timeLevel.equalsIgnoreCase("Day")) {
            String sDate = targetParams.getTargetStartDate();
            String eDate = targetParams.getTargetEndDate();

            // endDate = startDate
            String sDateQuery = "select to_char(ddate,'dd-MON-yy') from PR_DAY_DENOM where ddate=to_date('" + sDate + "','mm/dd/yyyy')";
            PbReturnObject sDObj = execSelectSQL(sDateQuery);
            //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" sDateQuery "+sDateQuery);
            startDate = sDObj.getFieldValueString(0, 0);

            String eDateQuery = "select to_char(ddate,'dd-MON-yy') from PR_DAY_DENOM where ddate=to_date('" + eDate + "','mm/dd/yyyy')";
            PbReturnObject eDObj = execSelectSQL(eDateQuery);
            //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" eDateQuery "+eDateQuery);
            endDate = eDObj.getFieldValueString(0, 0);
        } else if (timeLevel.equalsIgnoreCase("Month")) {

            startMonth = targetParams.getTargetStartMonth();
            endMonth = targetParams.getTargetEndMonth();
        } else if (timeLevel.equalsIgnoreCase("Quarter") || timeLevel.equalsIgnoreCase("Qtr")) {
            startQtr = targetParams.getTargetStartQtr();
            endQtr = targetParams.getTargetEndQtr();
        } else if (timeLevel.equalsIgnoreCase("Year")) {
            startYear = targetParams.getTargetStartYear();
            endYear = targetParams.getTargetEndYear();
        }
        // int element_id=0;
        Object addTargetMasterObj[] = new Object[16];
        addTargetMasterObj[0] = Integer.valueOf(target_id);
        addTargetMasterObj[1] = measureId;
        addTargetMasterObj[2] = targetName;
        addTargetMasterObj[3] = timeLevel;
        addTargetMasterObj[4] = userId;
        addTargetMasterObj[5] = targetDesc;

        addTargetMasterObj[6] = "0";//element_id;//primPrameter;
        addTargetMasterObj[7] = startDate;
        addTargetMasterObj[8] = endDate;
        addTargetMasterObj[9] = "Under Creation";
        addTargetMasterObj[10] = startMonth;
        addTargetMasterObj[11] = endMonth;
        addTargetMasterObj[12] = startQtr;
        addTargetMasterObj[13] = endQtr;
        addTargetMasterObj[14] = startYear;
        addTargetMasterObj[15] = endYear;

        String finaddTargetMaster = buildQuery(addTargetMaster, addTargetMasterObj);
        allQ.add(finaddTargetMaster);
        try {
            executeMultiple(allQ);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        pbro.setFieldValueString("TARGET_ID", String.valueOf(target_id));
//        pbro.setFieldValueString("TARGET_ID", "" + target_id + "");
        pbro.addRow();

        return pbro;
    }

    public void addTargetTimeLevels(Session ppmp) throws Exception {
        PbTargetParamParams targetParams = (PbTargetParamParams) ppmp.getObject("prg.targetparam.qdparams.PbTargetParamParams");
        String targetId = targetParams.getTargetId();
        ArrayList al = new ArrayList();
        String timeLevels = targetParams.getTargetTimeLevels();
        String addTargetTimeLevels = getResourceBundle().getString("addTargetTimeLevels");
        String allLevels[] = timeLevels.split(",");
        Object timeObj[] = new Object[3];
        int oldLevel = 1;
        String levelName = "";
        for (int u = 0; u < allLevels.length; u++) {
            oldLevel = u + 1;
            String level = allLevels[u];
            timeObj[0] = targetId;
            timeObj[1] = String.valueOf(oldLevel);
            if (level.equalsIgnoreCase("1")) {
                levelName = "Year";
            } else if (level.equalsIgnoreCase("2")) {
                levelName = "Qtr";
            } else if (level.equalsIgnoreCase("3")) {
                levelName = "Month";
            } else if (level.equalsIgnoreCase("4")) {
                levelName = "Day";
            }

            timeObj[2] = levelName;
            String finaddTargetTimeLevels = buildQuery(addTargetTimeLevels, timeObj);
            ////////////////////////////////////////////////////////////////////////.println.println(" finaddTargetTimeLevels "+finaddTargetTimeLevels);
            al.add(finaddTargetTimeLevels);
        }
        try {
            executeMultiple(al);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public PbReturnObject addParameterDetails(Session ppmp) throws Exception {
        PbReturnObject pbro = new PbReturnObject();
        String cols[] = {"TARGET_ID"};
        PbTargetParamParams targetParams = (PbTargetParamParams) ppmp.getObject("prg.targetparam.qdparams.PbTargetParamParams");
        String parameterIds = targetParams.getParameterIds();
        String parameterNames = targetParams.getParameterNames();
        //  String primaryParameter = targetParams.getPrimaryParameter();
        String minTimeLevel = targetParams.getTimeLevel();//getMinTimeLevel();
        String measureId = targetParams.getMeasureId();
        String targetId = targetParams.getTargetId();
        //////////////////////////////////////////////////////////////////////////////////.println.println(" parameterIds "+parameterIds);
        //  String getAddTargetParamDetails = "select puaid.dim_tab_id,puaid.buss_col_id,puaid.buss_col_name,puaid.dim_id,puaid.member_id mem_id,puaid.member_name,puaid.buss_table_id,puaid.element_id from prg_target_measure_members ptm, prg_user_all_info_details puaid where  puaid.member_id in(&) and measure_id=& and puaid.element_id= ptm.element_id order by dim_id";
        String getAddTargetParamDetails = "SELECT DISTINCT K.VAL_ELEMENT_ID ELEMENT_ID, K.VAL_DISP_NAME,m.info_member_id MEM_ID,"
                + " k.key_dim_id DIM_ID, k.key_buss_col_name, k.val_buss_col_id,k.val_member_name MEMBER_NAME FROM "
                + " PRG_USER_ALL_DDIM_KEY_VAL_ELE K, prg_user_all_ddim_master m ,prg_grp_buss_table p where k.key_FOLDER_id "
                + " in(select bus_folder_id from target_measure_folder where bus_group_id in(select business_group from "
                + " prg_target_master where prg_measure_id=&))  and  m.info_dim_id=K.KEY_DIM_ID and K.VAL_DISP_NAME=m.info_member_name and "
                + " p.buss_table_id= k.key_buss_table_id and p.buss_type='Table' and m.info_member_id in(&) "
                + " order by k.key_dim_id,m.info_member_id ";
        //resBundle.getString("getAddTargetParamDetails");

        String dimQuery = "select * from prg_user_all_info_details where element_id in(SELECT DISTINCT K.VAL_ELEMENT_ID"
                + " ELEMENT_ID FROM  PRG_USER_ALL_DDIM_KEY_VAL_ELE K, prg_user_all_ddim_master m ,prg_grp_buss_table p "
                + " where k.key_FOLDER_id  in(select bus_folder_id from target_measure_folder where bus_group_id "
                + " in(select business_group from   prg_target_master where prg_measure_id=&))  and  "
                + " m.info_dim_id=K.KEY_DIM_ID and K.VAL_DISP_NAME=m.info_member_name and  p.buss_table_id= k.key_buss_table_id and "
                + " p.buss_type='Table' and m.info_member_id in(&))";
        String sequenceQuery = getResourceBundle().getString("getSequenceNumber");
        // //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" parameterNames - "+parameterNames);
        Vector alreadyInSertedMem = new Vector();

        String parameter[] = parameterIds.split(",");
        Vector selectedMem = new Vector();
        for (int m = 0; m < parameter.length; m++) {
            selectedMem.add(parameter[m]);
        }

        Object paramOb[] = new Object[2];
        paramOb[0] = measureId;
        paramOb[1] = parameterIds;

        String fingetAddTargetParamDetails = buildQuery(getAddTargetParamDetails, paramOb);
        //////////////////////////////////////////////////////////////////////////////////.println.println(" fingetAddTargetParamDetails... "+fingetAddTargetParamDetails);
        String findimQuery = buildQuery(dimQuery, paramOb);
        //////////////////////////////////////////////////////////////////////////////////.println.println(" findimQuery "+ findimQuery);
        HashMap details = new HashMap();
        PbReturnObject detObj = execSelectSQL(findimQuery);
        for (int p = 0; p < detObj.getRowCount(); p++) {
            ArrayList al = new ArrayList();
            String memId = detObj.getFieldValueString(p, "MEMBER_ID");
            al.add(detObj.getFieldValueString(p, "DIM_TAB_ID"));
            al.add(detObj.getFieldValueString(p, "BUSS_TABLE_ID"));
            al.add(detObj.getFieldValueString(p, "DIM_ID"));
            details.put(memId, al);
        }
        PbReturnObject paramObj = execSelectSQL(fingetAddTargetParamDetails);
        Object paramDetObj[] = new Object[1];
        paramDetObj[0] = "PRG_TARGET_PARAM_DETAILS_SEQ";

        // to insert the relations
        String getRelationForMembers = getResourceBundle().getString("getRelationForMembers");
        Object relOb[] = new Object[1];
        relOb[0] = parameterIds;
        String fingetRelationForMembers = buildQuery(getRelationForMembers, relOb);
        //////////////////////////////////////////////////////////////////////////////////.println.println("  fingetRelationForMembers "+fingetRelationForMembers);
        PbReturnObject paramObjRel = execSelectSQL(fingetRelationForMembers);
        HashMap members = new HashMap();
        int relId = 0;
        relId = paramObjRel.getFieldValueInt(0, "REL_ID");
        int tempRel = relId;
        HashMap relLevels = new HashMap();
        for (int y = 0; y < paramObjRel.getRowCount(); y++) {
            //if(!relLevels.containsKey(paramObjRel.getFieldValueInt(y,"REL_ID")))
            relLevels.put(paramObjRel.getFieldValueString(y, "REL_ID"), paramObjRel.getFieldValueString(y, "REL_LEVEL"));
        }
        //////////////////////////////////////////////////////////////////////////////////.println.println(" relLevels "+relLevels);
        for (int y = 0; y < paramObjRel.getRowCount(); y++) {
            ArrayList al = new ArrayList();
            al.add(0, paramObjRel.getFieldValueString(y, "REL_LEVEL"));
            tempRel = paramObjRel.getFieldValueInt(y, "REL_ID");
            int next = y + 1;
            String tempChild = "-1";
            int toplevel = Integer.parseInt((String) relLevels.get(paramObjRel.getFieldValueString(y, "REL_ID")));
            if (next < paramObjRel.getRowCount()) {
                //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(tempRel+" in if 1 "+relId+" .. "+paramObjRel.getFieldValueString(y,"MEM_ID"));
                //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" toplevel "+toplevel+" - . . "+paramObjRel.getFieldValueInt(y,"REL_LEVEL"));
                if (toplevel > paramObjRel.getFieldValueInt(y, "REL_LEVEL")) {
                    tempChild = paramObjRel.getFieldValueString(next, "MEM_ID");
                }

            }

            al.add(1, tempChild);
            members.put(paramObjRel.getFieldValueString(y, "MEM_ID"), al);
            relId = tempRel;
        }
        //////////////////////////////////////////////////////////////////////////////////.println.println(" members.. "+members);

        String pNames[] = parameterNames.split(",");
        HashMap pDet = new HashMap();
        for (int y = 0; y < pNames.length; y++) {
            String val = pNames[y];
            String key = parameter[y];
            pDet.put(key, val);
        }

        ArrayList allQ = new ArrayList();
        // to enter the parameter details
        //  //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" pDet - "+pDet);
        String addParametDetails = getResourceBundle().getString("addParametDetails");
        // tot put the param display ids
        ArrayList paramDispIds = new ArrayList();
        HashMap elements = new HashMap();
        for (int p = 0; p < paramObj.getRowCount(); p++) {
            elements.put(paramObj.getFieldValueString(p, "MEM_ID"), paramObj.getFieldValueString(p, "ELEMENT_ID"));
        }

        //////////////////////////////////////////////////////////////////////////////////.println.println(selectedMem+" selectedMem elements ---. "+elements);
        //////////////////////////////////////////////////////////////////////////////////.println.println(" details "+details);
        String bussTabQuery = buildQuery(sequenceQuery, paramDetObj);
        String next_child = "";
        int tempDim = 0;
        for (int p = 0; p < paramObj.getRowCount(); p++) {
            //String paramId = paramObj.getFieldValueString(p, measureId);
            String dispName = "";
            String relLevel = "";
            String childElementId = "";
            childElementId = "-1";
            String key = paramObj.getFieldValueString(p, "MEM_ID");

            int dimId = 0;
            dimId = paramObj.getFieldValueInt(p, "DIM_ID");
            if (tempDim == 0) {
                tempDim = dimId;
            }
            String chMemId = "";

            if (members.containsKey(key)) {
                ArrayList det = (ArrayList) members.get(key);
                relLevel = (String) det.get(0);

                int next = p + 1;
                chMemId = (String) det.get(1);
                if (elements.containsKey(chMemId)) {
                    if (selectedMem.contains(chMemId)) {
                        childElementId = (String) elements.get(chMemId);
                    }
                }

            }
            //  //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(key+" childElementId- "+childElementId+" chMemId "+chMemId+" relLevel "+relLevel);
            // //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" pDet -- . "+pDet);
            tempDim = dimId;
            // if(pDet.containsKey(key))
            dispName = paramObj.getFieldValueString(p, "MEMBER_NAME");//pDet.get(key).toString();
            String element_id = paramObj.getFieldValueString(p, "ELEMENT_ID");
            int param_det_id = getSequenceNumber(bussTabQuery);
            paramDispIds.add(Integer.valueOf(param_det_id));
            ArrayList al = (ArrayList) details.get(key);
            String dim_id = al.get(2).toString();//paramObj.getFieldValueString(p,"DIM_ID");
            String dimTab_id = al.get(0).toString();//paramObj.getFieldValueString(p,"DIM_TAB_ID");
            String buss_tab_id = al.get(1).toString();//paramObj.getFieldValueString(p,"BUSS_TABLE_ID");
            String defaultValue = "All";
            //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(dim_id+" dim_id "+element_id);

            int dispSeqNumber = p + 1;
            String displayType = "SingleSelectBox(With All)";
            Object pDetObj[] = new Object[13];
            pDetObj[0] = Integer.valueOf(param_det_id);
            pDetObj[1] = targetId;
            pDetObj[2] = dispName;
            pDetObj[3] = element_id;
            pDetObj[4] = dim_id;
            pDetObj[5] = dimTab_id;
            pDetObj[6] = buss_tab_id;
            pDetObj[7] = childElementId;
            pDetObj[8] = Integer.valueOf(dispSeqNumber);
            pDetObj[9] = displayType;
            pDetObj[10] = defaultValue;
            pDetObj[11] = relLevel;
            // pDetObj[12] = chMemId;//next_child;

            String finaddParametDetails = buildQuery(addParametDetails, pDetObj);
            //////////////////////////////////////////////////////////////////////////////////.println.println(" finaddParametDetails ."+finaddParametDetails);
            //////////////////////////////////////////////////////////////////////////////////.println.println("  alreadyInSertedMem "+alreadyInSertedMem);
            if (!alreadyInSertedMem.contains(key)) {
                alreadyInSertedMem.add(key);
                allQ.add(finaddParametDetails);
            }

        }
        // insert in the prg_target_time
        Object paramTimeObj[] = new Object[1];
        paramTimeObj[0] = "PRG_TARGET_TIME_SEQ";
        String paramTimeQuery = buildQuery(sequenceQuery, paramTimeObj);
        int paramTimeSeq = getSequenceNumber(paramTimeQuery);

        String addParamTime = getResourceBundle().getString("addParamTime");
        Object timeOb[] = new Object[4];
        timeOb[0] = Integer.valueOf(paramTimeSeq);
        timeOb[1] = targetId;
        timeOb[2] = "";
        timeOb[3] = minTimeLevel;
        String finaddParamTime = buildQuery(addParamTime, timeOb);
        // //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" finaddParamTime . "+finaddParamTime);
        allQ.add(finaddParamTime);

        // insert in the prg_target_time_details
        Object paramTimeDetObj[] = new Object[1];
        paramTimeDetObj[0] = "PRG_TARGET_TIME_DETAIL_SEQ";
        String paramTimeDetQuery = buildQuery(sequenceQuery, paramTimeDetObj);
        String sequence = "";
        String formSequence;

        String addParamTimeDetails = getResourceBundle().getString("addParamTimeDetails");
        String time = "";
        String time1 = "";
        String compare = "PRG_COMPARE";
        String ColumnName = "";
        String ColumnName1 = "";
        if (minTimeLevel.equalsIgnoreCase("Day")) {
            time = "AS_OF_DATE";
            time1 = "AS_OF_DATE1";
            ColumnName = "From Date";
            ColumnName1 = "To Date";
        } else if (minTimeLevel.equalsIgnoreCase("Month")) {
            time = "AS_OF_MONTH";
            time1 = "AS_OF_MONTH1";
            ColumnName = "From Month";
            ColumnName1 = "To Month";
        } else if (minTimeLevel.equalsIgnoreCase("Quarter") || minTimeLevel.equalsIgnoreCase("Qtr")) {
            time = "AS_OF_QTR";
            time1 = "AS_OF_QTR1";
            ColumnName = "From Quarter";
            ColumnName1 = "To Quarter";
        } else if (minTimeLevel.equalsIgnoreCase("Year")) {
            time = "AS_OF_YEAR";
            time1 = "AS_OF_YEAR1";
            ColumnName = "From Year";
            ColumnName1 = "To Year";
        }
        // 0  as_of_date 1 as_of_date1 2 prg_period_type
        for (int i = 0; i <= 2; i++) {
            int paramTimeDetSeq = getSequenceNumber(paramTimeQuery);
            Object paramTDetOb[] = new Object[6];
            if (i == 0) {
                formSequence = "1";
                paramTDetOb[0] = Integer.valueOf(paramTimeDetSeq);
                paramTDetOb[1] = Integer.valueOf(paramTimeSeq);
                paramTDetOb[2] = ColumnName;
                paramTDetOb[3] = time;
                paramTDetOb[4] = sequence;
                paramTDetOb[5] = formSequence;
            }
            if (i == 1) {
                formSequence = "2";
                paramTDetOb[0] = Integer.valueOf(paramTimeDetSeq);
                paramTDetOb[1] = Integer.valueOf(paramTimeSeq);
                paramTDetOb[2] = ColumnName1;
                paramTDetOb[3] = time1;
                paramTDetOb[4] = sequence;
                paramTDetOb[5] = formSequence;
            }
            if (i == 2) {
                formSequence = "3";
                paramTDetOb[0] = Integer.valueOf(paramTimeDetSeq);
                paramTDetOb[1] = Integer.valueOf(paramTimeSeq);
                paramTDetOb[2] = "Period";
                paramTDetOb[3] = "PRG_PERIOD";
                paramTDetOb[4] = sequence;
                paramTDetOb[5] = formSequence;
            }
            String finaddParamTimeDetails = buildQuery(addParamTimeDetails, paramTDetOb);
            allQ.add(finaddParamTimeDetails);
            //  //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" finaddParamTimeDetails ."+finaddParamTimeDetails);
        }

        // insert in the prg_target_query_detail
        Object paramQryObj[] = new Object[1];
        paramQryObj[0] = "PRG_TARGET_QUERY_DETAIL_SEQ";
        String paramQueryDet = buildQuery(sequenceQuery, paramQryObj);
        int paramQrySeq = getSequenceNumber(paramQueryDet);

        String addTargetQuery = getResourceBundle().getString("addTargetQuery");

        // insert in the prg_target_view_master
        Object targetMasViewObj[] = new Object[1];
        targetMasViewObj[0] = "PRG_TARGET_VIEW_MASTER_SEQ";
        String tarMView = buildQuery(sequenceQuery, targetMasViewObj);

        Object primParamOb[] = new Object[2];
        // primParamOb[0] = measureId;
        // primParamOb[1] = primaryParameter;

        //  String primObjQuery = buildQuery(getAddTargetParamDetails,primParamOb);
        //////////////////////////////////////////////////////////////////////////////////.println.println(" primObjQuery .. "+primObjQuery);
        // PbReturnObject primObj = execSelectSQL(primObjQuery);
        // String primElementId = primObj.getFieldValueString(0,"ELEMENT_ID");
        int tarMViewSeq = 0;
        int tarMViewSeq1 = 0;
        int tarMViewSeq2 = 0;

        String addTargetViewMaster = getResourceBundle().getString("addTargetViewMaster");
        for (int p = 0; p < 3; p++) {
            Object paramViewMastObj[] = new Object[9];
            if (p == 0) {
                tarMViewSeq = getSequenceNumber(paramQueryDet);

                paramViewMastObj[0] = Integer.valueOf(tarMViewSeq);
                paramViewMastObj[1] = targetId;
                paramViewMastObj[2] = Integer.valueOf(p + 1);
                paramViewMastObj[3] = "Y";
                paramViewMastObj[4] = "1";
                paramViewMastObj[5] = "-1";
                paramViewMastObj[6] = "Overall Target";
                paramViewMastObj[7] = "Analyze By";
                paramViewMastObj[8] = "VIEW_BY_NAME";
            }
            if (p == 1) {
                tarMViewSeq1 = getSequenceNumber(paramQueryDet);
                //Object paramViewMastObj[] = new Object[7];
                paramViewMastObj[0] = Integer.valueOf(tarMViewSeq1);
                paramViewMastObj[1] = targetId;
                paramViewMastObj[2] = Integer.valueOf(p + 1);
                paramViewMastObj[3] = "N";
                paramViewMastObj[4] = "-1";
                paramViewMastObj[5] = "1";
                paramViewMastObj[6] = "Time";
                paramViewMastObj[7] = "Secondary Analyze By";
                paramViewMastObj[8] = "VIEW_BY_NAME1";
            }
            if (p == 2) {
                tarMViewSeq2 = getSequenceNumber(paramQueryDet);
                //Object paramViewMastObj[] = new Object[7];
                paramViewMastObj[0] = Integer.valueOf(tarMViewSeq2);
                paramViewMastObj[1] = targetId;
                paramViewMastObj[2] = Integer.valueOf(p + 1);
                paramViewMastObj[3] = "N";
                paramViewMastObj[4] = "-1";
                paramViewMastObj[5] = "-1";
                paramViewMastObj[6] = "Absolute";
                paramViewMastObj[7] = "On Basis";
                paramViewMastObj[8] = "VIEW_BY_NAME2";
            }
            String finaddTargetViewMaster = buildQuery(addTargetViewMaster, paramViewMastObj);
            ////////////////////////////////////////////////////////////////////////.println.println(" finaddTargetViewMaster.. "+finaddTargetViewMaster);
            allQ.add(finaddTargetViewMaster);
        }

        // insert in the prg_target_view_details
        Object targetDetViewObj[] = new Object[1];
        targetDetViewObj[0] = "PRG_TARGET_VIEW_DETAILS_SEQ";
        String tarDetView = buildQuery(sequenceQuery, targetDetViewObj);
        int dispSeqNumber = 1;
        int dispSeqNumber1 = 1;
        String addTargetViewDetails = getResourceBundle().getString("addTargetViewDetails");

        for (int g = 0; g < 3; g++) {
            if (g == 0) {
                for (int i = 0; i < paramDispIds.size(); i++) {
                    int tarDetViewSeq = getSequenceNumber(tarDetView);
                    int dispId = ((Integer) paramDispIds.get(i)).intValue();
                    //tarMViewSeq
                    Object targetViewDet[] = new Object[4];
                    targetViewDet[0] = Integer.valueOf(tarDetViewSeq);
                    targetViewDet[1] = Integer.valueOf(dispId);
                    targetViewDet[2] = Integer.valueOf(tarMViewSeq);
                    targetViewDet[3] = Integer.valueOf(dispSeqNumber);
                    dispSeqNumber = dispSeqNumber + 1;
                    String finaddTargetViewDetails = buildQuery(addTargetViewDetails, targetViewDet);
                    ////////////////////////////////////////////////////////////////////////.println.println(" finaddTargetViewDetails ."+finaddTargetViewDetails);
                    allQ.add(finaddTargetViewDetails);
                }
            }
            if (g == 1) {
                for (int i = 0; i < paramDispIds.size(); i++) {
                    int tarDetViewSeq = getSequenceNumber(tarDetView);
                    int dispId = ((Integer) paramDispIds.get(i)).intValue();
                    //tarMViewSeq
                    Object targetViewDet[] = new Object[4];
                    targetViewDet[0] = Integer.valueOf(tarDetViewSeq);
                    targetViewDet[1] = Integer.valueOf(dispId);
                    targetViewDet[2] = Integer.valueOf(tarMViewSeq1);
                    targetViewDet[3] = Integer.valueOf(dispSeqNumber1);
                    dispSeqNumber1 = dispSeqNumber1 + 1;
                    String finaddTargetViewDetails = buildQuery(addTargetViewDetails, targetViewDet);
                    ////////////////////////////////////////////////////////////////////////.println.println(" finaddTargetViewDetails ."+finaddTargetViewDetails);
                    allQ.add(finaddTargetViewDetails);

                }
            }
            /*
             * if(g==2){ for(int i=0;i<paramDispIds.size();i++){ int
             * tarDetViewSeq = getSequenceNumber(tarDetView); int dispId =
             * ((Integer)paramDispIds.get(i)).intValue(); //tarMViewSeq Object
             * targetViewDet[] =new Object[4]; targetViewDet[0] =
             * Integer.valueOf(tarDetViewSeq); targetViewDet[1] =
             * Integer.valueOf(dispId); targetViewDet[2] =
             * Integer.valueOf(tarMViewSeq2); targetViewDet[3] =
             * Integer.valueOf(dispSeqNumber1); dispSeqNumber1 =
             * dispSeqNumber1+1; String finaddTargetViewDetails =
             * buildQuery(addTargetViewDetails,targetViewDet);
             * ////////////////////////////////////////////////////////////////////////.println.println("
             * finaddTargetViewDetails... ."+finaddTargetViewDetails);
             * allQ.add(finaddTargetViewDetails);
             *
             * }
             * }
             */
        }
        for (int u = 0; u < allQ.size(); u++) {
            // //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(u+" allQ .. "+allQ.get(u).toString());
        }
        try {
            executeMultiple(allQ);
        } catch (Exception r) {
            logger.error("Exception: ", r);
        }
        String targetdata = " select * from prg_target_param_details where target_id=" + targetId + " order by dim_id,rel_level";
        PbReturnObject tarOb = execSelectSQL(targetdata);
        Vector dims = new Vector();
        String updateQ = "";
        ArrayList updateQueries = new ArrayList();
        int rel = 1;
        for (int p = 0; p < tarOb.getRowCount(); p++) {
            String paramDispId = tarOb.getFieldValueString(p, "PARAM_DISP_ID");
            if (!dims.contains(tarOb.getFieldValueString(p, "DIM_ID"))) {
                //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(paramDispId+" in if "+tarOb.getFieldValueString(p,"DIM_ID"));
                dims.add(tarOb.getFieldValueString(p, "DIM_ID"));
                rel = 1;
            }
            updateQ = "update PRG_TARGET_PARAM_DETAILS set rel_level=" + rel + " where PARAM_DISP_ID=" + paramDispId;
            // //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" updateQ "+updateQ);
            updateQueries.add(updateQ);
            rel = rel + 1;
        }
        try {
            executeMultiple(updateQueries);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }

        return paramObj;
    }

    //uday
    public void addLock(Session ppmp) throws Exception {
        String query = getResourceBundle().getString("addLock");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("addLock query is: "+query);
        String seqNumberQuery = getResourceBundle().getString("getMeasureSequence");
        int seqNumber = getSequenceNumber(seqNumberQuery);

        PbTargetParamParams targetParams = (PbTargetParamParams) ppmp.getObject("prg.targetparam.qdparams.PbTargetParamParams");
        Object obj[] = new Object[3];
        obj[0] = Integer.valueOf(seqNumber);
        obj[1] = targetParams.getTargetId();
        obj[2] = targetParams.getUserId();

        String finalQuery = null;
        finalQuery = buildQuery(query, obj);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("addLock final query is: "+finalQuery);

        try {
            execModifySQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

    }

    public PbReturnObject getLock(Session ppmp) throws Exception {
        PbReturnObject pbro = null;
        String query = getResourceBundle().getString("getLock");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("getLock query is: "+query);

        PbTargetParamParams targetParams = (PbTargetParamParams) ppmp.getObject("prg.targetparam.qdparams.PbTargetParamParams");
        Object obj[] = new Object[1];
        obj[0] = targetParams.getTargetId();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("targetParams.getTargetId() is: "+targetParams.getTargetId());

        String finalQuery = null;
        finalQuery = buildQuery(query, obj);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("getLock finalQuery is: "+finalQuery);

        try {
            pbro = execSelectSQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return pbro;
    }

    public void deleteLock(String userId) throws Exception {
        String query = getResourceBundle().getString("deleteLock");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("deleteLock query is: "+query);
        Object obj[] = new Object[1];
        obj[0] = userId;

        String finalQuery = null;
        finalQuery = buildQuery(query, obj);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("deleteLock final query is: "+finalQuery);

        try {
            execModifySQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public PbReturnObject getTargetMaster(Session ppmp) throws Exception {
        PbReturnObject pbro = null;
        String query = getResourceBundle().getString("getTargetMaster");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("getTargetMaster query is: "+query);

        PbTargetParamParams targetParams = (PbTargetParamParams) ppmp.getObject("prg.targetparam.qdparams.PbTargetParamParams");
        Object obj[] = new Object[1];
        obj[0] = targetParams.getTargetId();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("targetParams.getTargetId() is: "+targetParams.getTargetId());

        String finalQuery = null;
        finalQuery = buildQuery(query, obj);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("getTargetMaster finalQuery is: "+finalQuery);

        try {
            pbro = execSelectSQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return pbro;
    }

    public PbReturnObject getActiveAlerts(Session ppmp) throws Exception {
        PbReturnObject pbro = null;
        String query = getResourceBundle().getString("getActiveAlerts");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("getActiveAlerts query is: "+query);

        PbTargetParamParams targetParams = (PbTargetParamParams) ppmp.getObject("prg.targetparam.qdparams.PbTargetParamParams");
        Object obj[] = new Object[1];
        obj[0] = targetParams.getTargetId();

        String finalQuery = null;
        finalQuery = buildQuery(query, obj);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("getActiveAlerts finalQuery is: "+finalQuery);

        try {
            pbro = execSelectSQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return pbro;
    }

    public void updateTarget(Session ppmp) throws Exception {

        String query = getResourceBundle().getString("updateTarget");
        String query2 = getResourceBundle().getString("updateTarget2");
        String query3 = getResourceBundle().getString("updateTarget3");
        String query4 = getResourceBundle().getString("updateTarget4");

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("updateTarget query is: "+query);
        PbTargetParamParams targetParams = (PbTargetParamParams) ppmp.getObject("prg.targetparam.qdparams.PbTargetParamParams");
        Object obj[] = new Object[4];
        obj[0] = targetParams.getTargetName();
        obj[1] = targetParams.getTargetStartDate();
        obj[2] = targetParams.getTargetEndDate();
        obj[3] = targetParams.getTargetId();

        String targetId = targetParams.getTargetId();
        String getMinTimeLevel = "select * from target_master where target_id=" + targetParams.getTargetId();
        PbReturnObject pbro = execSelectSQL(getMinTimeLevel);
        String minTimeLevel = pbro.getFieldValueString(0, "MIN_TIME_LEVEL");

        String finalQuery = null;
        if (minTimeLevel.equalsIgnoreCase("Day")) {
            finalQuery = buildQuery(query, obj);
        } else if (minTimeLevel.equalsIgnoreCase("Month")) {
            finalQuery = buildQuery(query2, obj);
        } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
            finalQuery = buildQuery(query3, obj);
        } else if (minTimeLevel.equalsIgnoreCase("Year")) {
            finalQuery = buildQuery(query4, obj);
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("updateTarget finalQuery is: "+finalQuery);
        try {
            execModifySQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

    }

    public void deleteTarget(Session ppmp) throws Exception {
        ArrayList queries = new ArrayList();
        String query1 = getResourceBundle().getString("deleteTargetMaster");
        String query2 = getResourceBundle().getString("deleteTargetTimeMaster");
        String query3 = getResourceBundle().getString("deleteTargetTimeDetails");
        String query4 = getResourceBundle().getString("deleteTargetViewMaster");
        String query5 = getResourceBundle().getString("deleteTargetViewDetails");
        String query6 = getResourceBundle().getString("deleteAlertMaster");

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("deleteTarget query is: "+query1);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("deleteTargetMeasureParamCust query is: "+query2);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("deleteAlertMaster query is: "+query3);
        PbTargetParamParams targetParams = (PbTargetParamParams) ppmp.getObject("prg.targetparam.qdparams.PbTargetParamParams");
        Object obj[] = new Object[1];
        obj[0] = targetParams.getTargetId();

        String finalQuery1 = null;
        String finalQuery2 = null;
        String finalQuery3 = null;
        String finalQuery4 = null;
        String finalQuery5 = null;
        String finalQuery6 = null;

        finalQuery1 = buildQuery(query1, obj);
        finalQuery2 = buildQuery(query2, obj);
        finalQuery3 = buildQuery(query3, obj);
        finalQuery4 = buildQuery(query4, obj);
        finalQuery5 = buildQuery(query5, obj);
        finalQuery6 = buildQuery(query6, obj);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("deleteTarget finalQuery is: "+finalQuery1);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("deleteTarget finalQuery is: "+finalQuery2);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("deleteTarget finalQuery is: "+finalQuery3);
        queries.add(finalQuery6);
        queries.add(finalQuery5);
        queries.add(finalQuery4);
        queries.add(finalQuery3);
        queries.add(finalQuery2);
        queries.add(finalQuery1);

        try {
            executeMultiple(queries);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

    }

    //uday
    public PbReturnObject copyTarget(Session ppmp) throws Exception {
        ArrayList queries = new ArrayList();
        PbReturnObject newpbro = new PbReturnObject();
        String cols[] = new String[]{"NEW_TARGET_ID", "S_DATE", "E_DATE"};
        newpbro.setColumnNames(cols);

        String query = getResourceBundle().getString("copyTarget");
        String query2 = getResourceBundle().getString("copyTarget2");
        String query3 = getResourceBundle().getString("copyTarget3");
        String query4 = getResourceBundle().getString("copyTarget4");

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("copyTarget query is: "+query);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("copyTarget query is: "+query2);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("copyTarget query is: "+query3);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("copyTarget query is: "+query4);
        String seqNumberQuery = getResourceBundle().getString("getSequenceNumberU");
        int seqNumber = getSequenceNumber(seqNumberQuery);

        PbTargetParamParams targetParams = (PbTargetParamParams) ppmp.getObject("prg.targetparam.qdparams.PbTargetParamParams");
        String OldTargetId = targetParams.getTargetId();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Old tId is:: "+OldTargetId);

        String getOldMinTimeLevel = "select * from target_master where target_id=" + OldTargetId;
        PbReturnObject pbro = execSelectSQL(getOldMinTimeLevel);
        String minTimeLevel = pbro.getFieldValueString(0, "MIN_TIME_LEVEL");

        Object obj[] = new Object[6];
        obj[0] = Integer.valueOf(seqNumber);
        obj[1] = targetParams.getTargetName();
        obj[2] = targetParams.getTargetDescription();
        obj[3] = targetParams.getTargetStartDate();
        obj[4] = targetParams.getTargetEndDate();
        obj[5] = targetParams.getTargetId();

//        newpbro.setFieldValueString("NEW_TARGET_ID", "" + seqNumber + "");
        newpbro.setFieldValueString("NEW_TARGET_ID", String.valueOf(seqNumber));
        newpbro.setFieldValueString("S_DATE", targetParams.getTargetStartDate());
        newpbro.setFieldValueString("E_DATE", targetParams.getTargetEndDate());
        newpbro.addRow();

        String finalQuery = null;
        if (minTimeLevel.equalsIgnoreCase("Day")) {
            finalQuery = buildQuery(query, obj);
        } else if (minTimeLevel.equalsIgnoreCase("Month")) {
            finalQuery = buildQuery(query2, obj);
        } else if (minTimeLevel.equalsIgnoreCase("quarter")) {
            finalQuery = buildQuery(query3, obj);
        } else {
            finalQuery = buildQuery(query4, obj);
        }

        queries.add(finalQuery);
        try {
            executeMultiple(queries);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return newpbro;
    }

    public void copyTargetDetails(Session ppmp) throws Exception {
        ArrayList queries = new ArrayList();
        PbReturnObject pbro = new PbReturnObject();
        PbTargetValuesParam targetParams = (PbTargetValuesParam) ppmp.getObject("prg.targetparam.qdparams.PbTargetValuesParam");
        String factQuery = getResourceBundle().getString("getSequenceNumber");

        //insert paramDetails
        ArrayList paramDispIds = new ArrayList();
        String qry = getResourceBundle().getString("getTargetParamDetails");
        Object getobj[] = new Object[1];
        getobj[0] = targetParams.getTargetId();
        String finalQry = buildQuery(qry, getobj);

        pbro = execSelectSQL(finalQry);
        int rowCount = pbro.getRowCount();

        for (int i = 0; i < rowCount; i++) {
            paramDispIds.add(Integer.valueOf(pbro.getFieldValueInt(i, 0)));

            String query = getResourceBundle().getString("copyParamDetails");
            Object relSeqObj[] = new Object[1];
            relSeqObj[0] = "PRG_TARGET_PARAM_DETAILS_SEQ";

            String finalfactQuery = buildQuery(factQuery, relSeqObj);
            int seqNumber = getSequenceNumber(finalfactQuery);

            Object obj[] = new Object[4];
            obj[0] = Integer.valueOf(seqNumber);
            obj[1] = targetParams.getCopyTargetId();
            obj[2] = targetParams.getTargetId();
            obj[3] = Integer.valueOf(i + 1);

            String finalQuery = buildQuery(query, obj);
            queries.add(finalQuery);
        }

        //insert timeMaster
        String query2 = getResourceBundle().getString("copyParamTime");
        Object relSeqObj2[] = new Object[1];
        relSeqObj2[0] = "PRG_TARGET_TIME_SEQ";
        String finalfactQuery2 = buildQuery(factQuery, relSeqObj2);
        int seqNumber2 = getSequenceNumber(finalfactQuery2);
        Object obj2[] = new Object[3];
        obj2[0] = Integer.valueOf(seqNumber2);
        obj2[1] = targetParams.getCopyTargetId();
        obj2[2] = targetParams.getTargetId();

        String finalQuery2 = buildQuery(query2, obj2);
        queries.add(finalQuery2);

        //insert timeDetails
        String qry2 = getResourceBundle().getString("getTargetTimeParamDetails");
        Object getobj2[] = new Object[1];
        getobj2[0] = targetParams.getTargetId();
        String finalQry2 = buildQuery(qry2, getobj2);

        pbro = new PbReturnObject();
        pbro = execSelectSQL(finalQry2);
        int rowCount2 = pbro.getRowCount();

        for (int i = 0; i < rowCount2; i++) {
            String query3 = getResourceBundle().getString("copyParamTimeDetails");
            Object relSeqObj3[] = new Object[1];
            relSeqObj3[0] = "PRG_TARGET_TIME_DETAIL_SEQ";
            String finalfactQuery3 = buildQuery(factQuery, relSeqObj3);
            int seqNumber3 = getSequenceNumber(finalfactQuery3);
            Object obj3[] = new Object[4];
            obj3[0] = Integer.valueOf(seqNumber3);
            obj3[1] = targetParams.getCopyTargetId();
            obj3[2] = targetParams.getTargetId();
            obj3[3] = Integer.valueOf(i + 1);

            String finalQuery3 = buildQuery(query3, obj3);
            queries.add(finalQuery3);
        }

        //insert viewBy master getTargetViewByMaster
        String qry4 = getResourceBundle().getString("getTargetViewByMaster");
        Object getobj4[] = new Object[1];
        getobj4[0] = targetParams.getTargetId();
        String finalQry4 = buildQuery(qry4, getobj4);

        pbro = new PbReturnObject();
        pbro = execSelectSQL(finalQry4);
        int rowCount4 = pbro.getRowCount();

        for (int i = 0; i < rowCount4; i++) {
            String query4 = getResourceBundle().getString("copyTargetViewMaster");
            Object relSeqObj4[] = new Object[1];
            relSeqObj4[0] = "PRG_TARGET_VIEW_MASTER_SEQ";
            String finalfactQuery4 = buildQuery(factQuery, relSeqObj4);
            int seqNumber4 = getSequenceNumber(finalfactQuery4);
            Object obj4[] = new Object[4];
            obj4[0] = Integer.valueOf(seqNumber4);
            obj4[1] = targetParams.getCopyTargetId();
            obj4[2] = targetParams.getTargetId();
            obj4[3] = Integer.valueOf(i + 1);

            String finalQuery4 = buildQuery(query4, obj4);
            queries.add(finalQuery4);

        }

        //insert viewBy details
        for (int i = 0; i < rowCount4; i++) {
            String qry3 = getResourceBundle().getString("getTargetViewByDetails");
            Object getobj3[] = new Object[2];
            getobj3[0] = targetParams.getTargetId();
            getobj3[1] = Integer.valueOf(i + 1);
            String finalQry3 = buildQuery(qry3, getobj3);

            pbro = new PbReturnObject();
            pbro = execSelectSQL(finalQry3);
            int rowCount3 = pbro.getRowCount();

            for (int j = 0; j < rowCount3; j++) {
                String query5 = getResourceBundle().getString("copyTargetViewDetails");
                Object relSeqObj5[] = new Object[1];
                relSeqObj5[0] = "PRG_TARGET_VIEW_DETAILS_SEQ";
                String finalfactQuery5 = buildQuery(factQuery, relSeqObj5);
                int seqNumber5 = getSequenceNumber(finalfactQuery5);
                Object obj5[] = new Object[6];
                obj5[0] = Integer.valueOf(seqNumber5);
                obj5[1] = targetParams.getCopyTargetId();
                obj5[2] = Integer.valueOf(j + 1);
                obj5[3] = targetParams.getCopyTargetId();
                obj5[4] = Integer.valueOf(i + 1);
                obj5[5] = Integer.valueOf(j + 1);

                String finalQuery5 = buildQuery(query5, obj5);
                queries.add(finalQuery5);
            }
        }

        try {
            executeMultiple(queries);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public void updateTargetStatus(Session ppmp) throws Exception {
        String query = getResourceBundle().getString("updateTargetStatus");

        PbTargetParamParams targetParams = (PbTargetParamParams) ppmp.getObject("prg.targetparam.qdparams.PbTargetParamParams");
        Object obj[] = new Object[2];
        obj[0] = targetParams.getTargetStatus();
        obj[1] = targetParams.getTargetId();

        String finalQuery = null;
        finalQuery = buildQuery(query, obj);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("updateTargetStatus final query is: "+finalQuery);

        try {
            execModifySQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

    }

    public PbReturnObject getMonths(Session ppmp) throws Exception {
        PbReturnObject pbro = null;
        String query = getResourceBundle().getString("getMonths");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("getMonths query is: "+query);

        PbTargetParamParams targetParams = (PbTargetParamParams) ppmp.getObject("prg.targetparam.qdparams.PbTargetParamParams");
        Object obj[] = new Object[2];
        obj[0] = targetParams.getTargetStartDate();
        obj[1] = targetParams.getTargetEndDate();
        String finalQuery = null;
        finalQuery = buildQuery(query, obj);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("getMonths final query is: "+finalQuery);

        try {
            pbro = execSelectSQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return pbro;
    }

    public PbReturnObject getQuarters(Session ppmp) throws Exception {
        PbReturnObject pbro = null;
        String query = getResourceBundle().getString("getQuarters");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("getQuarters query is: "+query);

        PbTargetParamParams targetParams = (PbTargetParamParams) ppmp.getObject("prg.targetparam.qdparams.PbTargetParamParams");
        Object obj[] = new Object[2];
        obj[0] = targetParams.getTargetStartDate();
        obj[1] = targetParams.getTargetEndDate();
        String finalQuery = null;
        finalQuery = buildQuery(query, obj);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("getQuarters final query is: "+finalQuery);

        try {
            pbro = execSelectSQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return pbro;
    }

    public PbReturnObject getYears(Session ppmp) throws Exception {
        PbReturnObject pbro = null;
        String query = getResourceBundle().getString("getYears");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("getYears query is: "+query);

        PbTargetParamParams targetParams = (PbTargetParamParams) ppmp.getObject("prg.targetparam.qdparams.PbTargetParamParams");
        Object obj[] = new Object[2];
        obj[0] = targetParams.getTargetStartDate();
        obj[1] = targetParams.getTargetEndDate();
        String finalQuery = null;
        finalQuery = buildQuery(query, obj);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("getYears final query is: "+finalQuery);

        try {
            pbro = execSelectSQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return pbro;
    }

    public PbReturnObject getRangeValues(Session ppmp) throws Exception {
        PbTargetParamParams targetParams = (PbTargetParamParams) ppmp.getObject("prg.targetparam.qdparams.PbTargetParamParams");
        ArrayList queries = new ArrayList();
        String minTimeLevel = targetParams.getMinTimeLevel();
        String srcFrom = targetParams.getSrcFrom();
        String srcTo = targetParams.getSrcTo();
        String desFrom = targetParams.getDesFrom();
        String desTo = targetParams.getDesTo();

        PbReturnObject pbro = null;
        PbReturnObject pbro2 = null;
        if (minTimeLevel.equalsIgnoreCase("Day")) {
            String query = "select to_char(ddate,'dd-MON-yyyy') as view_by,ddate as view_by1,daysofyear from pr_day_denom where ddate between to_char(to_date('" + srcFrom + "','mm-dd-yyyy'),'dd-MON-yy') and "
                    + "to_char(to_date('" + srcTo + "','mm-dd-yyyy'),'dd-MON-yy') order by daysofyear";

            pbro = execSelectSQL(query);

            query = "select to_char(ddate,'dd-MON-yyyy') as view_by,ddate as view_by1,daysofyear from pr_day_denom where ddate between to_char(to_date('" + desFrom + "','mm-dd-yyyy'),'dd-MON-yy') and "
                    + "to_char(to_date('" + desTo + "','mm-dd-yyyy'),'dd-MON-yy') order by daysofyear";

            pbro2 = execSelectSQL(query);
        } else if (minTimeLevel.equalsIgnoreCase("Month")) {
            String query = "select distinct mon_name as view_by,cmon,cyear from prg_acn_mon_denom where cm_st_date between "
                    + "(select cm_st_date from prg_acn_mon_denom where mon_name='" + srcFrom + "') and "
                    + "(select cm_end_date from prg_acn_mon_denom where mon_name='" + srcTo + "') order by cyear,cmon";

            pbro = execSelectSQL(query);

            query = "select distinct mon_name as view_by,cmon,cyear from prg_acn_mon_denom where cm_st_date between "
                    + "(select cm_st_date from prg_acn_mon_denom where mon_name='" + desFrom + "') and "
                    + "(select cm_end_date from prg_acn_mon_denom where mon_name='" + desTo + "') order by cyear,cmon";

            pbro2 = execSelectSQL(query);
        } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
            String query = "select view_by,view_by as view_by1 from "
                    + "( select distinct T.CQTR || '-' || T.CQ_YEAR as view_by , T.CQ_YEAR || '-' || T.CQTR as orderbycol FROM "
                    + " pr_day_denom T where  ddate between (select DISTINCT cmq_st_date from prg_acn_mon_denom where cmqtr_code='" + srcFrom + "') "
                    + "and (select distinct cmq_end_date from prg_acn_mon_denom where cmqtr_code='" + srcTo + "')) order by orderbycol";

            pbro = execSelectSQL(query);

            query = "select view_by,view_by as view_by1 from "
                    + "( select distinct T.CQTR || '-' || T.CQ_YEAR as view_by , T.CQ_YEAR || '-' || T.CQTR as orderbycol FROM "
                    + " pr_day_denom T where  ddate between (select DISTINCT cmq_st_date from prg_acn_mon_denom where cmqtr_code='" + desFrom + "') "
                    + "and (select distinct cmq_end_date from prg_acn_mon_denom where cmqtr_code='" + desTo + "')) order by orderbycol";

            pbro2 = execSelectSQL(query);
        } else {
            String query = "select distinct cyear as view_by from prg_acn_mon_denom where cmy_start_date between "
                    + "(select distinct cmy_start_date from prg_acn_mon_denom where cyear='" + srcFrom + "') "
                    + "and (select distinct cmy_end_date from prg_acn_mon_denom where cyear='" + srcTo + "') order by cyear";

            pbro = execSelectSQL(query);

            query = "select distinct cyear as view_by from prg_acn_mon_denom where cmy_start_date between "
                    + "(select distinct cmy_start_date from prg_acn_mon_denom where cyear='" + desFrom + "') "
                    + "and (select distinct cmy_end_date from prg_acn_mon_denom where cyear='" + desTo + "') order by cyear";

            pbro2 = execSelectSQL(query);
        }

        PbReturnObject retObj = new PbReturnObject();
        retObj.setObject("SourceRange", pbro);
        retObj.setObject("DestinationRange", pbro2);

        return retObj;

    }

    public void copyRangeData(Session ppmp) throws Exception {
        PbReturnObject pbro = null;

        PbTargetValuesParam targetParams = (PbTargetValuesParam) ppmp.getObject("prg.targetparam.qdparams.PbTargetValuesParam");
        String targetId = String.valueOf(targetParams.getTargetId());
        String measureId = String.valueOf(targetParams.getMeasureId());
        String minTimeLevel = targetParams.getMinTimeLevel();
        String periodType = targetParams.getPeriodType();
        ArrayList srcRange = targetParams.getSourceDates();
        ArrayList destRange = targetParams.getDestDates();

        LinkedHashMap destHashMap = new LinkedHashMap();
        LinkedHashMap srcHashMap = new LinkedHashMap();

        ArrayList allInsertQueries = new ArrayList();

        //if(periodType.equalsIgnoreCase("Day"))
        //{
        for (int i = 0; i < destRange.size(); i++) {
            String dVal = destRange.get(i).toString();
            String detQ = null;

            if (minTimeLevel.equalsIgnoreCase("Day")) {
                detQ = "select distinct to_char(ddate,'MON-yy') as cmonth,(cqtr||'-'||cyear) as cqtr,cyear from pr_day_denom where ddate in '" + dVal + "'";
                pbro = execSelectSQL(detQ);
                ArrayList destArrayList = new ArrayList();
                destArrayList.add(pbro.getFieldValueString(0, "CMONTH"));
                destArrayList.add(pbro.getFieldValueString(0, "CQTR"));
                destArrayList.add(pbro.getFieldValueString(0, "CYEAR"));
                destHashMap.put(dVal, destArrayList);
            } else if (minTimeLevel.equalsIgnoreCase("Month")) {
                detQ = "select DISTINCT mon_name as cmonth,(cmqtr||'-'||cyear) as cqtr,cyear from prg_acn_mon_denom where mon_name in '" + dVal + "'";
                pbro = execSelectSQL(detQ);
                ArrayList destArrayList = new ArrayList();
                destArrayList.add(pbro.getFieldValueString(0, "CMONTH"));
                destArrayList.add(pbro.getFieldValueString(0, "CQTR"));
                destArrayList.add(pbro.getFieldValueString(0, "CYEAR"));
                destHashMap.put(dVal, destArrayList);
            } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                detQ = "select distinct (cmqtr||'-'||cyear) as cqtr,cyear from prg_acn_mon_denom where cmqtr_code in 'Q" + dVal + "'";
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("detQ is::: "+detQ);
                pbro = execSelectSQL(detQ);
                ArrayList destArrayList = new ArrayList();
                destArrayList.add(pbro.getFieldValueString(0, "CQTR"));
                destArrayList.add(pbro.getFieldValueString(0, "CYEAR"));
                destHashMap.put(dVal, destArrayList);
            } else {
                detQ = "select distinct cyear from prg_acn_mon_denom where cyear in '" + dVal + "'";
                pbro = execSelectSQL(detQ);
                ArrayList destArrayList = new ArrayList();
                destArrayList.add(pbro.getFieldValueString(0, "CYEAR"));
                destHashMap.put(dVal, destArrayList);
            }

        }
        for (int i = 0; i < srcRange.size(); i++) {
            String dVal = srcRange.get(i).toString();
            String detQ = null;
            if (minTimeLevel.equalsIgnoreCase("Day")) {
                detQ = "select distinct to_char(ddate,'MON-yy') as cmonth,(cqtr||'-'||cyear) as cqtr,cyear from pr_day_denom where ddate in '" + dVal + "'";
                pbro = execSelectSQL(detQ);
                ArrayList srcArrayList = new ArrayList();
                srcArrayList.add(pbro.getFieldValueString(0, "CMONTH"));
                srcArrayList.add(pbro.getFieldValueString(0, "CQTR"));
                srcArrayList.add(pbro.getFieldValueString(0, "CYEAR"));
                srcHashMap.put(dVal, srcArrayList);
            } else if (minTimeLevel.equalsIgnoreCase("Month")) {
                detQ = "select distinct mon_name as cmonth,(cmqtr||'-'||cyear) as cqtr,cyear from prg_acn_mon_denom where mon_name in '" + dVal + "'";
                pbro = execSelectSQL(detQ);
                ArrayList srcArrayList = new ArrayList();
                srcArrayList.add(pbro.getFieldValueString(0, "CMONTH"));
                srcArrayList.add(pbro.getFieldValueString(0, "CQTR"));
                srcArrayList.add(pbro.getFieldValueString(0, "CYEAR"));
                srcHashMap.put(dVal, srcArrayList);
            } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                detQ = "select distinct (cmqtr||'-'||cyear) as cqtr,cyear from prg_acn_mon_denom where cmqtr_code in 'Q" + dVal + "'";
                pbro = execSelectSQL(detQ);
                ArrayList srcArrayList = new ArrayList();
                srcArrayList.add(pbro.getFieldValueString(0, "CQTR"));
                srcArrayList.add(pbro.getFieldValueString(0, "CYEAR"));
                srcHashMap.put(dVal, srcArrayList);
            } else {
                detQ = "select distinct cyear from prg_acn_mon_denom where cyear in '" + dVal + "'";
                pbro = execSelectSQL(detQ);
                ArrayList srcArrayList = new ArrayList();
                srcArrayList.add(pbro.getFieldValueString(0, "CYEAR"));
                srcHashMap.put(dVal, srcArrayList);
            }

        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("destHashMap is::: "+destHashMap);
        //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("srcHashMap is:: "+srcHashMap);

        //get target_table
          /*
         * String tabQuery = "select distinct tmm.measure_name,tmm.target_table
         * from target_master tm, target_measure_master tmm where
         * tmm.measure_id='"+measureId+"' and target_id="+targetId;
         * ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("tabQuery
         * - "+tabQuery); PbReturnObject tabRetOb = execSelectSQL(tabQuery);
         * String measureName2 = tabRetOb.getFieldValueString(0,"MEASURE_NAME");
         * String measureName = measureName2.trim().replace(" ","_"); String
         * targetTable = tabRetOb.getFieldValueString(0,"TARGET_TABLE");
         * ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("measureName
         * - "+measureName+" targetTable -"+targetTable); String paraColsQuery =
         * "select c.parameter_name, m.prg_column_name from prg_parameter_master
         * m, target_measure_parameters_cust c where
         * c.measure_id='"+measureId+"' and target_id="+targetId+" and
         * m.ppm_param_id = c.parameter_id";
         * ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("paraColsQuery
         * "+paraColsQuery); PbReturnObject parColsRetOb =
         * execSelectSQL(paraColsQuery); HashMap paramCols = new HashMap();
         * for(int t=0;t<parColsRetOb.getRowCount();t++) {
         * paramCols.put(parColsRetOb.getFieldValueString(t,"PARAMETER_NAME").trim(),parColsRetOb.getFieldValueString(t,"PRG_COLUMN_NAME"));
         * }
         */
        String tabQuery = "select target_table_id,pgbt.buss_table_name,ptm.measure_name from prg_target_master ptm,prg_grp_buss_table pgbt where prg_measure_id in (select measure_id from target_master WHERE target_master.target_id=" + targetId + ") and pgbt.buss_table_id = ptm.target_table_id";
        PbReturnObject tabRetOb = execSelectSQL(tabQuery);
        String measureName2 = tabRetOb.getFieldValueString(0, "MEASURE_NAME");
        String measureName = measureName2.trim().replace(" ", "_");
        String targetTable = tabRetOb.getFieldValueString(0, "BUSS_TABLE_NAME");
        //String paraColsQuery = "select c.parameter_name, m.prg_column_name from prg_parameter_master m, target_measure_parameters_cust c where c.measure_id='"+measureId+"' and target_id="+targetId+" and m.ppm_param_id = c.parameter_id";
        HashMap paramCols = new HashMap();

        String fingetElementIdsBussCols = "select distinct ptpd.element_id,param_disp_name,puaid.buss_col_name,target_id from prg_target_param_details ptpd, prg_user_all_info_details puaid where target_id=" + targetId + " and ptpd.element_id = puaid.element_id";
        PbReturnObject bussColOb = execSelectSQL(fingetElementIdsBussCols);
        for (int p = 0; p < bussColOb.getRowCount(); p++) {
            paramCols.put(bussColOb.getFieldValueString(p, "ELEMENT_ID"), bussColOb.getFieldValueString(p, "BUSS_COL_NAME"));
        }

        boolean flagGetColumns = false;
        String insertQuery = "insert into " + targetTable.trim() + "(";
        int colNumbers = 0;
        String replaceVals = "";
        HashMap allValsMap = new HashMap();
        String colNames = "";
        ArrayList columns = new ArrayList();
        Object relSeqObj[] = new Object[1];
        relSeqObj[0] = "PRG_TARGET_DATA_SEQ";
        String factQuery = getResourceBundle().getString("getSequenceNumber");
        String finalfactQuery = buildQuery(factQuery, relSeqObj);

        int n = 0;
        // n = destRange.size();
        int mul = 1;
        for (int j = 0; j < destRange.size(); j++) {

            boolean skipFlag = false;
            String destD = destRange.get(j).toString();
            String destDataQuery = null;
            if (minTimeLevel.equalsIgnoreCase("Day")) {
                destDataQuery = "select * from " + targetTable.trim() + " where target_id='" + targetId + "' and t_date=to_date('" + destD + "')";
            } else if (minTimeLevel.equalsIgnoreCase("Month")) {
                destDataQuery = "select * from " + targetTable.trim() + " where target_id='" + targetId + "' and t_month='" + destD + "'";
            } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                destDataQuery = "select * from " + targetTable.trim() + " where target_id='" + targetId + "' and t_qtr='" + destD + "'";
            } else {
                destDataQuery = "select * from " + targetTable.trim() + " where target_id='" + targetId + "' and t_year='" + destD + "'";
            }

            PbReturnObject destData = execSelectSQL(destDataQuery);

            if (destData.getRowCount() > 0) {
                skipFlag = true;
            }
            if (skipFlag == false) {
                /*
                 * if(j == srcRange.size()) {
                 * >= srcRange.size()"); n = 0; }
                 */

                if (j == srcRange.size()) {
                    n = 0;
                    mul = mul + 1;
                } else if (j == (srcRange.size() * mul)) {
                    n = 0;
                    mul = mul + 1;
                }

                String sourceDate = srcRange.get(n).toString();
                n++;
                String sourceDataQ = null;
                if (minTimeLevel.equalsIgnoreCase("Day")) {
                    sourceDataQ = "select * from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date('" + sourceDate + "')";
                } else if (minTimeLevel.equalsIgnoreCase("Month")) {
                    sourceDataQ = "select * from " + targetTable.trim() + " where target_id=" + targetId + " and t_month='" + sourceDate + "'";
                } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                    sourceDataQ = "select * from " + targetTable.trim() + " where target_id=" + targetId + " and t_qtr='" + sourceDate + "'";
                } else {
                    sourceDataQ = "select * from " + targetTable.trim() + " where target_id=" + targetId + " and t_year='" + sourceDate + "'";
                }

                PbReturnObject dataOb = execSelectSQL(sourceDataQ);
                if (dataOb.getRowCount() > 0 && flagGetColumns == false) {
                    flagGetColumns = true;
                    String cols[] = dataOb.getColumnNames();
                    colNumbers = dataOb.getRowCount();

                    for (int u = 0; u < cols.length; u++) {
                        if (u == cols.length - 1) {
                            colNames = colNames + "," + cols[u];
                        } else if (u != 0) {
                            colNames = colNames + "," + cols[u];
                        } else {
                            colNames = cols[u];
                        }

                        columns.add(cols[u]);
                        //if(cols[u].equalsIgnoreCase(t_qtr))
                        allValsMap.put(cols[u], "");

                        if (u == 0 && (cols.length > 1)) {
                            replaceVals = replaceVals + "'&'" + ",";
                        } else if ((u == 0) && (cols.length == 1)) {
                            replaceVals = replaceVals + "'&'";
                        } else if (u == cols.length - 1) {
                            replaceVals = replaceVals + "'&'";
                        } else {
                            replaceVals = replaceVals + "'&'" + ",";
                        }
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("in if ------ .");
                    }

                    colNames = insertQuery + colNames + ") values(" + replaceVals + ")";
                }

                for (int p = 0; p < dataOb.getRowCount(); p++) {
                    Object insertObjDy[] = new Object[columns.size()];
                    String q = "";

                    for (int t = 0; t < columns.size(); t++) {
                        String colId = (String) columns.get(t);
                        String vall = dataOb.getFieldValueString(p, colId.trim().toUpperCase());

                        if (colId.equalsIgnoreCase(measureName)) {
                            int newVal = Integer.parseInt(vall);
//                            allValsMap.put(colId, "" + newVal + "");
                            allValsMap.put(colId, String.valueOf(newVal));
                        } else {
                            allValsMap.put(colId, vall);
                        }
                        //allValsMap.put(colId,dataOb.getFieldValueString(p,colId.trim().toUpperCase()));
                    }

                    int fact_id = getSequenceNumber(finalfactQuery);
                    // int fact_id = 0;
                    ArrayList destHashMapVal = new ArrayList();
                    if (destHashMap.containsKey(destD)) {
                        destHashMapVal = (ArrayList) destHashMap.get(destD);
                    }

                    if (minTimeLevel.equalsIgnoreCase("Day")) {
                        allValsMap.put("T_DATE", destD);
                        allValsMap.put("T_MONTH", destHashMapVal.get(0));
                        allValsMap.put("T_QTR", destHashMapVal.get(1));
                        allValsMap.put("T_YEAR", destHashMapVal.get(2));
                    } else if (minTimeLevel.equalsIgnoreCase("Month")) {
                        allValsMap.put("T_MONTH", destHashMapVal.get(0));
                        allValsMap.put("T_QTR", destHashMapVal.get(1));
                        allValsMap.put("T_YEAR", destHashMapVal.get(2));
                    } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                        allValsMap.put("T_QTR", destHashMapVal.get(0));
                        allValsMap.put("T_YEAR", destHashMapVal.get(1));
                    } else {
                        allValsMap.put("T_YEAR", destHashMapVal.get(0));
                    }

//                    allValsMap.put("FACT_ID", "" + fact_id + "");
                    allValsMap.put("FACT_ID", String.valueOf(fact_id));
                    for (int l = 0; l < columns.size(); l++) {
                        String value = (String) allValsMap.get(columns.get(l));
                        if (value == null) {
                            value = "";
                        }
                        insertObjDy[l] = value;
                    }
                    String finalQuery = buildQuery(colNames, insertObjDy);
                    allInsertQueries.add(finalQuery);
                }
                //}
            }
        }
        //}

        try {
            executeMultiple(allInsertQueries);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
    }

    public void copyRangeDataDelete(Session ppmp) throws Exception {
        PbReturnObject pbro = null;

        PbTargetValuesParam targetParams = (PbTargetValuesParam) ppmp.getObject("prg.targetparam.qdparams.PbTargetValuesParam");
        String targetId = String.valueOf(targetParams.getTargetId());
//        String measureId = String.valueOf(targetParams.getMeasureId());
        String minTimeLevel = targetParams.getMinTimeLevel();
        String periodType = targetParams.getPeriodType();
        ArrayList srcRange = targetParams.getSourceDates();
        ArrayList destRange = targetParams.getDestDates();

        LinkedHashMap destHashMap = new LinkedHashMap();
        LinkedHashMap srcHashMap = new LinkedHashMap();

        ArrayList allInsertQueries = new ArrayList();

        //if(periodType.equalsIgnoreCase("Day"))
        //{
        for (int i = 0; i < destRange.size(); i++) {
            String dVal = destRange.get(i).toString();
            String detQ = null;

            if (minTimeLevel.equalsIgnoreCase("Day")) {
                detQ = "select distinct to_char(ddate,'MON-yy') as cmonth,(cqtr||'-'||cyear) as cqtr,cyear from pr_day_denom where ddate in '" + dVal + "'";
                pbro = execSelectSQL(detQ);
                ArrayList destArrayList = new ArrayList();
                destArrayList.add(pbro.getFieldValueString(0, "CMONTH"));
                destArrayList.add(pbro.getFieldValueString(0, "CQTR"));
                destArrayList.add(pbro.getFieldValueString(0, "CYEAR"));
                destHashMap.put(dVal, destArrayList);
            } else if (minTimeLevel.equalsIgnoreCase("Month")) {
                detQ = "select DISTINCT mon_name as cmonth,(cmqtr||'-'||cyear) as cqtr,cyear from prg_acn_mon_denom where mon_name in '" + dVal + "'";
                pbro = execSelectSQL(detQ);
                ArrayList destArrayList = new ArrayList();
                destArrayList.add(pbro.getFieldValueString(0, "CMONTH"));
                destArrayList.add(pbro.getFieldValueString(0, "CQTR"));
                destArrayList.add(pbro.getFieldValueString(0, "CYEAR"));
                destHashMap.put(dVal, destArrayList);
            } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                detQ = "select distinct (cmqtr||'-'||cyear) as cqtr,cyear from prg_acn_mon_denom where cmqtr_code in 'Q" + dVal + "'";
                pbro = execSelectSQL(detQ);
                ArrayList destArrayList = new ArrayList();
                destArrayList.add(pbro.getFieldValueString(0, "CQTR"));
                destArrayList.add(pbro.getFieldValueString(0, "CYEAR"));
                destHashMap.put(dVal, destArrayList);
            } else {
                detQ = "select distinct cyear from prg_acn_mon_denom where cyear in '" + dVal + "'";
                pbro = execSelectSQL(detQ);
                ArrayList destArrayList = new ArrayList();
                destArrayList.add(pbro.getFieldValueString(0, "CYEAR"));
                destHashMap.put(dVal, destArrayList);
            }

        }
        for (int i = 0; i < srcRange.size(); i++) {
            String dVal = srcRange.get(i).toString();
            String detQ = null;
            if (minTimeLevel.equalsIgnoreCase("Day")) {
                detQ = "select distinct to_char(ddate,'MON-yy') as cmonth,(cqtr||'-'||cyear) as cqtr,cyear from pr_day_denom where ddate in '" + dVal + "'";
                pbro = execSelectSQL(detQ);
                ArrayList srcArrayList = new ArrayList();
                srcArrayList.add(pbro.getFieldValueString(0, "CMONTH"));
                srcArrayList.add(pbro.getFieldValueString(0, "CQTR"));
                srcArrayList.add(pbro.getFieldValueString(0, "CYEAR"));
                srcHashMap.put(dVal, srcArrayList);
            } else if (minTimeLevel.equalsIgnoreCase("Month")) {
                detQ = "select distinct mon_name as cmonth,(cmqtr||'-'||cyear) as cqtr,cyear from prg_acn_mon_denom where mon_name in '" + dVal + "'";
                pbro = execSelectSQL(detQ);
                ArrayList srcArrayList = new ArrayList();
                srcArrayList.add(pbro.getFieldValueString(0, "CMONTH"));
                srcArrayList.add(pbro.getFieldValueString(0, "CQTR"));
                srcArrayList.add(pbro.getFieldValueString(0, "CYEAR"));
                srcHashMap.put(dVal, srcArrayList);
            } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                detQ = "select distinct (cmqtr||'-'||cyear) as cqtr,cyear from prg_acn_mon_denom where cmqtr_code in 'Q" + dVal + "'";
                pbro = execSelectSQL(detQ);
                ArrayList srcArrayList = new ArrayList();
                srcArrayList.add(pbro.getFieldValueString(0, "CQTR"));
                srcArrayList.add(pbro.getFieldValueString(0, "CYEAR"));
                srcHashMap.put(dVal, srcArrayList);
            } else {
                detQ = "select distinct cyear from prg_acn_mon_denom where cyear in '" + dVal + "'";
                pbro = execSelectSQL(detQ);
                ArrayList srcArrayList = new ArrayList();
                srcArrayList.add(pbro.getFieldValueString(0, "CYEAR"));
                srcHashMap.put(dVal, srcArrayList);
            }

        }
        //get target_table
        String tabQuery = "select target_table_id,pgbt.buss_table_name,ptm.measure_name from prg_target_master ptm,prg_grp_buss_table pgbt where prg_measure_id in (select measure_id from target_master WHERE target_master.target_id=" + targetId + ") and pgbt.buss_table_id = ptm.target_table_id";
        PbReturnObject tabRetOb = execSelectSQL(tabQuery);
        String measureName2 = tabRetOb.getFieldValueString(0, "MEASURE_NAME");
        String measureName = measureName2.trim().replace(" ", "_");
        String targetTable = tabRetOb.getFieldValueString(0, "BUSS_TABLE_NAME");
        //String paraColsQuery = "select c.parameter_name, m.prg_column_name from prg_parameter_master m, target_measure_parameters_cust c where c.measure_id='"+measureId+"' and target_id="+targetId+" and m.ppm_param_id = c.parameter_id";
        HashMap paramCols = new HashMap();

        String fingetElementIdsBussCols = "select distinct ptpd.element_id,param_disp_name,puaid.buss_col_name,target_id from prg_target_param_details ptpd, prg_user_all_info_details puaid where target_id=" + targetId + " and ptpd.element_id = puaid.element_id";
        PbReturnObject bussColOb = execSelectSQL(fingetElementIdsBussCols);
        for (int p = 0; p < bussColOb.getRowCount(); p++) {
            paramCols.put(bussColOb.getFieldValueString(p, "ELEMENT_ID"), bussColOb.getFieldValueString(p, "BUSS_COL_NAME"));
        }

        boolean flagGetColumns = false;
        String insertQuery = "insert into " + targetTable.trim() + "(";
        int colNumbers = 0;
        String replaceVals = "";
        HashMap allValsMap = new HashMap();
        String colNames = "";
        ArrayList columns = new ArrayList();
        Object relSeqObj[] = new Object[1];
        relSeqObj[0] = "PRG_TARGET_DATA_SEQ";
        String factQuery = getResourceBundle().getString("getSequenceNumber");
        String finalfactQuery = buildQuery(factQuery, relSeqObj);

        int n = 0;
        int mul = 1;
        ArrayList deletQueries = new ArrayList();
        // n = destRange.size();
        for (int j = 0; j < destRange.size(); j++) {
            boolean deleteFlag = false;
            boolean deleteDataFlag = false;
            boolean skipFlag = false;
            String destD = destRange.get(j).toString();
            String destDataQuery = null;
            if (minTimeLevel.equalsIgnoreCase("Day")) {
                destDataQuery = "select * from " + targetTable.trim() + " where target_id='" + targetId + "' and t_date=to_date('" + destD + "')";
            } else if (minTimeLevel.equalsIgnoreCase("Month")) {
                destDataQuery = "select * from " + targetTable.trim() + " where target_id='" + targetId + "' and t_month='" + destD + "'";
            } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                destDataQuery = "select * from " + targetTable.trim() + " where target_id='" + targetId + "' and t_qtr='" + destD + "'";
            } else {
                destDataQuery = "select * from " + targetTable.trim() + " where target_id='" + targetId + "' and t_year='" + destD + "'";
            }
            PbReturnObject destData = execSelectSQL(destDataQuery);
            //  if(destData.getRowCount()>0)
            // {
            skipFlag = true;
            deleteFlag = true;
            // }
            //  if(skipFlag==false)
            //  {
            if (j == srcRange.size()) {
                n = 0;
                mul = mul + 1;
            } else if (j == (srcRange.size() * mul)) {
                n = 0;
                mul = mul + 1;
            }
            //    if(j<srcRange.size())
            //  {
            String sourceDate = srcRange.get(n).toString();
            n++;
            String sourceDataQ = null;
            if (minTimeLevel.equalsIgnoreCase("Day")) {
                sourceDataQ = "select * from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date('" + sourceDate + "')";
            } else if (minTimeLevel.equalsIgnoreCase("Month")) {
                sourceDataQ = "select * from " + targetTable.trim() + " where target_id=" + targetId + " and t_month='" + sourceDate + "'";
            } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                sourceDataQ = "select * from " + targetTable.trim() + " where target_id=" + targetId + " and t_qtr='" + sourceDate + "'";
            } else if (minTimeLevel.equalsIgnoreCase("Year")) {
                sourceDataQ = "select * from " + targetTable.trim() + " where target_id=" + targetId + " and t_year='" + sourceDate + "'";
            }

            PbReturnObject dataOb = execSelectSQL(sourceDataQ);
            if (deleteFlag == true && dataOb.getRowCount() > 0) {
                deleteDataFlag = true;
            }

            if (dataOb.getRowCount() > 0 && flagGetColumns == false) {
                flagGetColumns = true;
                String cols[] = dataOb.getColumnNames();
                colNumbers = dataOb.getRowCount();

                for (int u = 0; u < cols.length; u++) {
                    if (u == cols.length - 1) {
                        colNames = colNames + "," + cols[u];
                    } else if (u != 0) {
                        colNames = colNames + "," + cols[u];
                    } else {
                        colNames = cols[u];
                    }

                    columns.add(cols[u]);
                    //if(cols[u].equalsIgnoreCase(t_qtr))
                    allValsMap.put(cols[u], "");

                    if (u == 0 && (cols.length > 1)) {
                        replaceVals = replaceVals + "'&'" + ",";
                    } else if ((u == 0) && (cols.length == 1)) {
                        replaceVals = replaceVals + "'&'";
                    } else if (u == cols.length - 1) {
                        replaceVals = replaceVals + "'&'";
                    } else {
                        replaceVals = replaceVals + "'&'" + ",";
                    }
                }

                colNames = insertQuery + colNames + ") values(" + replaceVals + ")";
            }
            String t_month = "";
            String dDate = "";
            String t_qtr = "";
            String t_year = "";
            // if(deleteDataFlag==true){
            for (int p = 0; p < dataOb.getRowCount(); p++) {
                Object insertObjDy[] = new Object[columns.size()];
                String q = "";

                for (int t = 0; t < columns.size(); t++) {
                    String colId = (String) columns.get(t);
                    String vall = dataOb.getFieldValueString(p, colId.trim().toUpperCase());

                    if (colId.equalsIgnoreCase(measureName)) {
                        int newVal = Integer.parseInt(vall);
                        allValsMap.put(colId, String.valueOf(newVal));
                    } else {
                        allValsMap.put(colId, vall);
                    }
                    //allValsMap.put(colId,dataOb.getFieldValueString(p,colId.trim().toUpperCase()));
                }

                int fact_id = getSequenceNumber(finalfactQuery);
                // int fact_id = 0;

                ArrayList destHashMapVal = new ArrayList();
                if (destHashMap.containsKey(destD)) {
                    destHashMapVal = (ArrayList) destHashMap.get(destD);
                }

                if (minTimeLevel.equalsIgnoreCase("Day")) {
                    allValsMap.put("T_DATE", destD);
                    allValsMap.put("T_MONTH", destHashMapVal.get(0));
                    allValsMap.put("T_QTR", destHashMapVal.get(1));
                    allValsMap.put("T_YEAR", destHashMapVal.get(2));
                    dDate = destD;
                } else if (minTimeLevel.equalsIgnoreCase("Month")) {
                    allValsMap.put("T_MONTH", destHashMapVal.get(0));
                    t_month = destHashMapVal.get(0).toString();
                    allValsMap.put("T_QTR", destHashMapVal.get(1));
                    allValsMap.put("T_YEAR", destHashMapVal.get(2));
                } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                    allValsMap.put("T_MONTH", "");
                    allValsMap.put("T_QTR", destHashMapVal.get(0));
                    allValsMap.put("T_YEAR", destHashMapVal.get(1));
                    t_qtr = destHashMapVal.get(0).toString();//+"-"+destHashMapVal.get(1).toString();
                } else {
                    allValsMap.put("T_YEAR", destHashMapVal.get(0));
                    t_year = destHashMapVal.get(0).toString();
                }

//                allValsMap.put("FACT_ID", "" + fact_id + "");
                allValsMap.put("FACT_ID", String.valueOf(fact_id));
                for (int l = 0; l < columns.size(); l++) {
                    String value = (String) allValsMap.get(columns.get(l));
                    if (value == null) {
                        value = "";
                    }
                    insertObjDy[l] = value;
                }
                String finalQuery = buildQuery(colNames, insertObjDy);
                allInsertQueries.add(finalQuery);
            }
            String deletQ = "";
            if (periodType.equalsIgnoreCase("Month")) {
                deletQ = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and t_month='" + t_month + "'";
            } else if (periodType.equalsIgnoreCase("Day")) {
                deletQ = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and t_date=to_date('" + dDate + "','dd-mm-yy')";
            } else if (periodType.equalsIgnoreCase("Quarter")) {
                deletQ = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and t_qtr='" + t_qtr + "'";
            } else if (periodType.equalsIgnoreCase("Year")) {
                deletQ = "delete from " + targetTable.trim() + " where target_id=" + targetId + " and t_year='" + t_year + "'";
            }

            deletQueries.add(deletQ);
            //  }
            //   }
            //  }
        }
        //}

        try {
            executeMultiple(deletQueries);

            executeMultiple(allInsertQueries);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
    }

    //uday
    public void copyTargetValues(Session ppmp) throws Exception {
        PbReturnObject pbro = new PbReturnObject();
        PbTargetValuesParam targetParams = (PbTargetValuesParam) ppmp.getObject("prg.targetparam.qdparams.PbTargetValuesParam");
        int targetId = Integer.parseInt(targetParams.getTargetId());
        int destTargetId = targetParams.getCopyTargetId();
        int multiplier = targetParams.getMultiplier();
//        int measureId = targetParams.getMeasureId();
        String minTimeLevel = targetParams.getPeriodType();
        String destFDate = targetParams.getDestFromDate();
        String destTDate = targetParams.getDestToDate();
        String SourceFromDate = "";
        String SourceToDate = "";
        String DestFromDate = "";
        String DestToDate = "";
        String destTQuery = "";
        String destFQuery = "";
        String minimumTimeLeQ = "";
        String periodType = "";
        if (minTimeLevel.equalsIgnoreCase("Day")) {
            minimumTimeLeQ = "select min_time_level,to_char(target_start_date,'dd-MON-yy') s_start_date,to_char(target_end_date,'dd-MON-yy') s_end_date from target_master where target_id='" + targetId + "'";
            PbReturnObject targetOb = execSelectSQL(minimumTimeLeQ);
            periodType = targetOb.getFieldValueString(0, "MIN_TIME_LEVEL");
            SourceFromDate = targetOb.getFieldValueString(0, "S_START_DATE");
            SourceToDate = targetOb.getFieldValueString(0, "S_END_DATE");
        }
        if (minTimeLevel.equalsIgnoreCase("Month")) {
            minimumTimeLeQ = "select min_time_level,target_start_month s_start_date,target_end_month s_end_date from target_master where target_id='" + targetId + "'";
            PbReturnObject targetOb = execSelectSQL(minimumTimeLeQ);
            periodType = targetOb.getFieldValueString(0, "MIN_TIME_LEVEL");
            SourceFromDate = targetOb.getFieldValueString(0, "S_START_DATE");
            SourceToDate = targetOb.getFieldValueString(0, "S_END_DATE");

        } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
            minimumTimeLeQ = "select min_time_level,target_start_qtr s_start_date,target_end_qtr s_end_date from target_master where target_id='" + targetId + "'";
            PbReturnObject targetOb = execSelectSQL(minimumTimeLeQ);
            periodType = targetOb.getFieldValueString(0, "MIN_TIME_LEVEL");
            SourceFromDate = targetOb.getFieldValueString(0, "S_START_DATE");
            SourceToDate = targetOb.getFieldValueString(0, "S_END_DATE");

        } else if (minTimeLevel.equalsIgnoreCase("Year")) {
            minimumTimeLeQ = "select min_time_level,target_start_year s_start_date,target_end_year s_end_date from target_master where target_id='" + targetId + "'";
            PbReturnObject targetOb = execSelectSQL(minimumTimeLeQ);
            periodType = targetOb.getFieldValueString(0, "MIN_TIME_LEVEL");
            SourceFromDate = targetOb.getFieldValueString(0, "S_START_DATE");
            SourceToDate = targetOb.getFieldValueString(0, "S_END_DATE");

        }

        if (periodType.equalsIgnoreCase("Day")) {
            destFQuery = "select to_char(ddate,'dd-MON-yy'),cqtr,cyear,to_char(ddate,'MON-yy') mon  from pr_day_denom where ddate = to_date('" + destFDate + "','mm-dd-yy')";
            destTQuery = "select to_char(ddate,'dd-MON-yy'),cqtr,cyear,to_char(ddate,'MON-yy') mon  from pr_day_denom where ddate = to_date('" + destTDate + "','mm-dd-yy')";
            PbReturnObject destFRetOb = execSelectSQL(destFQuery);
            PbReturnObject destTRetOb = execSelectSQL(destTQuery);
            DestFromDate = destFRetOb.getFieldValueString(0, 0);
            DestToDate = destTRetOb.getFieldValueString(0, 0);
        } else if (periodType.equalsIgnoreCase("Month")) {
            DestFromDate = destFDate;
            DestToDate = destTDate;
        } else if (periodType.equalsIgnoreCase("Quarter")) {
            DestFromDate = destFDate;
            DestToDate = destTDate;
        } else if (periodType.equalsIgnoreCase("Year")) {
            DestFromDate = destFDate;
            DestToDate = destTDate;
        }
        // String minimumTimeLeQ = "select min_time_level from target_master where target_id='"+targetId+"'";

        //to get the dynamic table and column names
        String tabQuery = "select target_table_id,pgbt.buss_table_name,ptm.measure_name from prg_target_master ptm,prg_grp_buss_table pgbt where prg_measure_id in (select measure_id from target_master WHERE target_master.target_id=" + targetId + ") and pgbt.buss_table_id = ptm.target_table_id";
        PbReturnObject tabRetOb = execSelectSQL(tabQuery);
        String measureName2 = tabRetOb.getFieldValueString(0, "MEASURE_NAME");
        String measureName = measureName2.trim().replace(" ", "_");
        String targetTable = tabRetOb.getFieldValueString(0, "BUSS_TABLE_NAME");
        HashMap paramCols = new HashMap();

        String fingetElementIdsBussCols = "select distinct ptpd.element_id,param_disp_name,puaid.buss_col_name,target_id from prg_target_param_details ptpd, prg_user_all_info_details puaid where target_id=" + targetId + " and ptpd.element_id = puaid.element_id";
        PbReturnObject bussColOb = execSelectSQL(fingetElementIdsBussCols);
        for (int p = 0; p < bussColOb.getRowCount(); p++) {
            paramCols.put(bussColOb.getFieldValueString(p, "ELEMENT_ID"), bussColOb.getFieldValueString(p, "BUSS_COL_NAME"));
        }

        String allData = "";
        String sourceDates = "";
        String destDates = "";

        String insertQuery = "insert into " + targetTable.trim() + "(";
        int colNumbers = 0;
        String replaceVals = "";
        HashMap allValsMap = new HashMap();
        String colNames = "";
        ArrayList columns = new ArrayList();
        ArrayList allInsertQueries = new ArrayList();
//        ArrayList alldeleteQueries = new ArrayList();
        String sourceAllDataDates = "";

        Object relSeqObj[] = new Object[1];
        relSeqObj[0] = "PRG_TARGET_DATA_SEQ";
        String factQuery = getResourceBundle().getString("getSequenceNumber");
        String finalfactQuery = buildQuery(factQuery, relSeqObj);

        if (periodType.equalsIgnoreCase("Month")) {
            // sourceAllDataDates = "select distinct t_month from "+targetTable.trim()+" where target_id='"+targetId+"' and t_date between to_date('"+SourceFromDate+"','dd-mm-yy') and to_date('"+SourceToDate+"','dd-mm-yy')";
            //  sourceAllDataDates ="select distinct t_month from "+targetTable.trim()+" where target_id='"+targetId+"' and to_date(t_month,'mm-yy') between to_date('"+SourceFromDate.substring(3)+"','mm-yy') and to_date('"+SourceToDate.substring(3)+"','mm-yy')";
            // allData = "select * from "+targetTable.trim()+" where target_id='"+targetId+"' and t_month between  to_date('"+SourceFromDate+"','dd-mm-yy') and to_date('"+SourceToDate+"','dd-mm-yy')";
            // allData = "select * from "+targetTable.trim()+" where target_id='"+targetId+"' and to_date(t_month,'mm-yy') between to_date('"+SourceFromDate.substring(3)+"','mm-yy') and to_date('"+SourceToDate.substring(3)+"','mm-yy')";

            String sMonths = "select to_char(cm_st_date,'dd-mm-yy') from prg_acn_mon_denom where mon_name='" + SourceFromDate + "'";
            String dMonths = "select to_char(cm_st_date,'dd-mm-yy') from prg_acn_mon_denom where mon_name='" + SourceToDate + "'";
            PbReturnObject sOb = execSelectSQL(sMonths);
            PbReturnObject dOb = execSelectSQL(dMonths);
            String sD = sOb.getFieldValueString(0, 0);
            String dD = dOb.getFieldValueString(0, 0);

            String sdMonths = "select to_char(cm_st_date,'dd-mm-yy') from prg_acn_mon_denom where mon_name='" + DestFromDate + "'";
            String ddMonths = "select to_char(cm_st_date,'dd-mm-yy') from prg_acn_mon_denom where mon_name='" + DestToDate + "'";
            PbReturnObject sOb1 = execSelectSQL(sdMonths);
            PbReturnObject dOb2 = execSelectSQL(ddMonths);
            String sDdd = sOb1.getFieldValueString(0, 0);
            String dDdd = dOb2.getFieldValueString(0, 0);

            sourceDates = "select DISTINCT  to_char(ddate,'MON-yy') dd,cqtr,cyear,to_char(ddate,'mm-yy') from pr_day_denom where ddate between to_date('" + sD + "','dd-mm-yy') and to_date('" + dD + "','dd-mm-yy') ORDER by 3,4";
            destDates = "select DISTINCT  to_char(ddate,'MON-yy') dd,cqtr,cyear,to_char(ddate,'mm-yy') from pr_day_denom where ddate between to_date('" + sDdd + "','dd-mm-yy') and to_date('" + dDdd + "','dd-mm-yy') ORDER by 3,4";

            // sourceDates = "select DISTINCT  to_char(ddate,'MON-yy') dd,cqtr,cyear,to_char(ddate,'mm-yy') from pr_day_denom where ddate between to_date('"+SourceFromDate+"','dd-mm-yy') and to_date('"+SourceToDate+"','dd-mm-yy') ORDER by 3,4";
            //  destDates = "select DISTINCT  to_char(ddate,'MON-yy') dd,cqtr,cyear,to_char(ddate,'mm-yy') from pr_day_denom where ddate between to_date('"+DestFromDate+"','dd-mm-yy') and to_date('"+DestToDate+"','dd-mm-yy') ORDER by 3,4";
            PbReturnObject sourceDatesOb = execSelectSQL(sourceDates);
            PbReturnObject destDatesOb = execSelectSQL(destDates);
            HashMap sourceDatesMap = new HashMap();
            HashMap destDatesMap = new HashMap();
            ArrayList sdetails = new ArrayList();
            ArrayList destDetails = new ArrayList();
            for (int p = 0; p < sourceDatesOb.getRowCount(); p++) {
                //sdetails.add(0,sourceDatesOb.getFieldValueString(p,"CQTR"));
                sdetails.add(sourceDatesOb.getFieldValueString(p, "DD"));
                sourceDatesMap.put(sourceDatesOb.getFieldValueString(p, "DD"), sdetails);
            }
            for (int p = 0; p < destDatesOb.getRowCount(); p++) {
                ArrayList newVals = new ArrayList();
                newVals.add(0, destDatesOb.getFieldValueString(p, "CQTR"));
                newVals.add(1, destDatesOb.getFieldValueString(p, "CYEAR"));
                destDatesMap.put(destDatesOb.getFieldValueString(p, "DD"), newVals);
                destDetails.add(destDatesOb.getFieldValueString(p, "DD"));
            }
            boolean fl = false;
            for (int y = 0; y < sdetails.size(); y++) {
                String sourceD = (String) sdetails.get(y);
                String query = "Select * from " + targetTable.trim() + " where target_id='" + targetId + "' and t_month='" + sourceD + "'";
                PbReturnObject dataOb = execSelectSQL(query);

                String desDate = "";
                if (destDetails.get(y) != null) {
                    desDate = (String) destDetails.get(y);
                    ArrayList al = (ArrayList) destDatesMap.get(desDate);
                    String t_year = (String) al.get(1);
                    String t_qtr = (String) al.get(0) + "-" + (String) al.get(1);

                    if (y == 0) {
                        String cols[] = dataOb.getColumnNames();
                        colNumbers = dataOb.getRowCount();
                        for (int u = 0; u < cols.length; u++) {
                            if (u == cols.length - 1) {
                                colNames = colNames + "," + cols[u];
                            } else if (u != 0) {
                                colNames = colNames + "," + cols[u];
                            } else {
                                colNames = cols[u];
                            }
                            columns.add(cols[u]);
                            //if(cols[u].equalsIgnoreCase(t_qtr))
                            allValsMap.put(cols[u], "");

                            if (u == 0 && (cols.length > 1)) {
                                replaceVals = replaceVals + "'&'" + ",";
                            } else if ((u == 0) && (cols.length == 1)) {
                                replaceVals = replaceVals + "'&'";
                            } else if (u == cols.length - 1) {
                                replaceVals = replaceVals + "'&'";
                            } else {
                                replaceVals = replaceVals + "'&'" + ",";
                            }

                        }
                        colNames = insertQuery + colNames + ") values(" + replaceVals + ")";
                    }

                    for (int p = 0; p < dataOb.getRowCount(); p++) {
                        int fact_id = getSequenceNumber(finalfactQuery);
                        Object insertObjDy[] = new Object[columns.size()];
                        String q = "";

                        for (int t = 0; t < columns.size(); t++) {
                            String colId = (String) columns.get(t);
                            String vall = dataOb.getFieldValueString(p, colId.trim().toUpperCase());

                            if (colId.equalsIgnoreCase(measureName)) {
                                int newVal = (Integer.parseInt(vall)) * (multiplier);
//                                allValsMap.put(colId, "" + newVal + "");
                                allValsMap.put(colId, String.valueOf(newVal));
                            } else {
                                allValsMap.put(colId, vall);
                            }
                        }
                        allValsMap.put("T_DATE", "");
//                        allValsMap.put("TARGET_ID", "" + destTargetId + "");
                        allValsMap.put("TARGET_ID", String.valueOf(destTargetId));
                        allValsMap.put("T_MONTH", desDate);
                        allValsMap.put("T_QTR", t_qtr);
                        allValsMap.put("T_YEAR", t_year);
//                        allValsMap.put("FACT_ID", "" + fact_id + "");
                        allValsMap.put("FACT_ID", String.valueOf(fact_id));
                        for (int l = 0; l < columns.size(); l++) {
                            String value = (String) allValsMap.get(columns.get(l));
                            if (value == null) {
                                value = "";
                            }
                            insertObjDy[l] = value;
                        }
                        String finalQuery = buildQuery(colNames, insertObjDy);

                        //   String deleteQuery = "delete from "+targetTable.trim()+" where target_id='"+targetId+"' and t_month='"+desDate+"'";
                        //   alldeleteQueries.add(deleteQuery);
                        allInsertQueries.add(finalQuery);
                    }
                }
            }
        } else if (periodType.equalsIgnoreCase("Day")) {
            sourceAllDataDates = "select distinct t_date from " + targetTable.trim() + " where target_id='" + targetId + "' and t_date between to_date('" + SourceFromDate + "','dd-mm-yy') and to_date('" + SourceToDate + "','dd-mm-yy')";
            allData = "select * from " + targetTable.trim() + " where target_id='" + targetId + "' and t_date between  to_date('" + SourceFromDate + "','dd-mm-yy') and to_date('" + SourceToDate + "','dd-mm-yy')";
            sourceDates = "select distinct to_char(to_date(ddate,'dd-mm-yy'),'dd-MON-yy') sourcedate,to_char(ddate,'dd') sourceno,to_char(ddate,'mm') mon,to_char(ddate,'MON-yy') mon_code,to_char(ddate,'yyyy') year_code, cqtr from pr_day_denom where ddate between to_date('" + SourceFromDate + "','dd-mm-yy') and to_date('" + SourceToDate + "','dd-mm-yy') order by 5,3,1";
            destDates = "select distinct to_char(to_date(ddate,'dd-mm-yy'),'dd-MON-yy') destdate,to_char(ddate,'dd') destno,to_char(ddate,'mm') mon,to_char(ddate,'MON-yy') mon_code,to_char(ddate,'yyyy') year_code, cqtr from pr_day_denom  where ddate between to_date('" + DestFromDate + "','dd-mm-yy') and to_date('" + DestToDate + "','dd-mm-yy') order by 5,3,1";
            String monNumberQ = "select trunc(months_between(to_date('" + DestFromDate + "','dd-mm-yy'),to_date('" + SourceFromDate + "','dd-mm-yy'))) monnumber from dual";
//            PbReturnObject monNumberQOb = execSelectSQL(monNumberQ);
            PbReturnObject sourceDatesOb = execSelectSQL(sourceDates);
            PbReturnObject destDatesOb = execSelectSQL(destDates);
            HashMap sourceDatesMap = new HashMap();
            HashMap destDatesMap = new HashMap();
            ArrayList sdetails = new ArrayList();
            ArrayList smonths = new ArrayList();
            ArrayList destmonths = new ArrayList();
            ArrayList destDetails = new ArrayList();
            for (int p = 0; p < sourceDatesOb.getRowCount(); p++) {
                // sourceDatesMap.put(sourceDatesOb.getFieldValueString(p,"SOURCENO")+"-"+sourceDatesOb.getFieldValueString(p,"MON"),sourceDatesOb.getFieldValueString(p,"SOURCEDATE"));
                sourceDatesMap.put(sourceDatesOb.getFieldValueString(p, "SOURCENO") + "-" + sourceDatesOb.getFieldValueString(p, "MON"), sourceDatesOb.getFieldValueString(p, "SOURCEDATE"));
                // sdetails.add(sourceDatesOb.getFieldValueString(p,"SOURCENO")+"-"+sourceDatesOb.getFieldValueString(p,"MON")+"-"+sourceDatesOb.getFieldValueString(p,"SOURCEDATE").substring(7));
                sdetails.add(sourceDatesOb.getFieldValueString(p, "SOURCEDATE"));
                smonths.add(sourceDatesOb.getFieldValueString(p, "MON"));
            }
            for (int p = 0; p < destDatesOb.getRowCount(); p++) {
                ArrayList newVals = new ArrayList();
                newVals.add(0, destDatesOb.getFieldValueString(p, "DESTDATE"));
                newVals.add(1, destDatesOb.getFieldValueString(p, "MON_CODE"));
                newVals.add(2, destDatesOb.getFieldValueString(p, "CQTR"));
                newVals.add(3, destDatesOb.getFieldValueString(p, "YEAR_CODE"));

                destDatesMap.put(destDatesOb.getFieldValueString(p, "DESTDATE"), newVals);
                destmonths.add(destDatesOb.getFieldValueString(p, "MON"));
                // destDetails.add(destDatesOb.getFieldValueString(p,"DESTNO")+"-"+destDatesOb.getFieldValueString(p,"MON"));
                destDetails.add(destDatesOb.getFieldValueString(p, "DESTDATE"));
            }

            boolean fl = false;
            int n = 0;
            Vector copiedDates = new Vector();
            for (int m = 0; m < sdetails.size(); m++) {
                String sVal = (String) sdetails.get(m);
                String newKey = "";
                // if(destDetails.get(n)!=null){
                if (n < destDetails.size()) {
                    newKey = (String) destDetails.get(n);
                    if (sVal.substring(0, 2).equalsIgnoreCase(newKey.substring(0, 2))) {
                        String query = "Select * from " + targetTable.trim() + " where target_id='" + targetId + "' and t_date=to_date('" + sVal + "','dd-mm-yy')";
                        n++;
                    }
                    // }
                }
                if (destDatesMap.containsKey(newKey)) {
                    ArrayList val = (ArrayList) destDatesMap.get(newKey);
                    String destDate = (String) val.get(0);
                    String t_mon = (String) val.get(1);
                    String t_qtr = (String) val.get(2) + "-" + (String) val.get(3);
                    String t_year = (String) val.get(3);
                    String query = "";

                    // if(sVal.substring(0,2).equalsIgnoreCase(newKey.substring(0,2))){
                    if (!copiedDates.contains(newKey)) {

                        if (sVal.substring(0, 2).equalsIgnoreCase(newKey.substring(0, 2))) {
                            copiedDates.add(newKey);
                            query = "Select * from " + targetTable.trim() + " where target_id='" + targetId + "' and t_date=to_date('" + sVal + "','dd-mm-yy')";
                            PbReturnObject dataOb = execSelectSQL(query);
                            if (dataOb.getRowCount() > 0 && fl == false) {
                                String cols[] = dataOb.getColumnNames();

                                colNumbers = dataOb.getRowCount();

                                for (int u = 0; u < cols.length; u++) {
                                    if (u == cols.length - 1) {
                                        colNames = colNames + "," + cols[u];
                                    } else if (u != 0) {
                                        colNames = colNames + "," + cols[u];
                                    } else {
                                        colNames = cols[u];
                                    }
                                    columns.add(cols[u]);
                                    //if(cols[u].equalsIgnoreCase(t_qtr))
                                    allValsMap.put(cols[u], "");

                                    if (u == 0 && (cols.length > 1)) {
                                        replaceVals = replaceVals + "'&'" + ",";
                                    } else if ((u == 0) && (cols.length == 1)) {
                                        replaceVals = replaceVals + "'&'";
                                    } else if (u == cols.length - 1) {
                                        replaceVals = replaceVals + "'&'";
                                    } else {
                                        replaceVals = replaceVals + "'&'" + ",";
                                    }
                                }
                                colNames = insertQuery + colNames + ") values(" + replaceVals + ")";
                                fl = true;
                            }

                            for (int p = 0; p < dataOb.getRowCount(); p++) {

                                Object insertObjDy[] = new Object[columns.size()];
                                String q = "";

                                for (int t = 0; t < columns.size(); t++) {
                                    String colId = (String) columns.get(t);
                                    String vall = dataOb.getFieldValueString(p, colId.trim().toUpperCase());

                                    if (colId.equalsIgnoreCase(measureName)) {
                                        int newVal = (Integer.parseInt(vall)) * (multiplier);
//                                        allValsMap.put(colId, "" + newVal + "");
                                        allValsMap.put(colId, String.valueOf(newVal));
                                    } else {
                                        allValsMap.put(colId, vall);
                                    }
                                    //allValsMap.put(colId,dataOb.getFieldValueString(p,colId.trim().toUpperCase()));
                                }
                                int fact_id = getSequenceNumber(finalfactQuery);
                                //  int fact_id = 0;
                                allValsMap.put("T_DATE", destDate);
                                allValsMap.put("T_MONTH", t_mon);
                                allValsMap.put("T_QTR", t_qtr);
                                allValsMap.put("T_YEAR", t_year);
//                                allValsMap.put("TARGET_ID", "" + destTargetId + "");
                                allValsMap.put("TARGET_ID", String.valueOf(destTargetId));
//                                allValsMap.put("FACT_ID", "" + fact_id + "");
                                allValsMap.put("FACT_ID", String.valueOf(fact_id));
                                // allValsMap.put(measureName.trim().toUpperCase(),);
                                //String finalInsertDyn = buildQuery(finalParameters,insertObjDy);
                                for (int l = 0; l < columns.size(); l++) {
                                    String value = (String) allValsMap.get(columns.get(l));
                                    if (value == null) {
                                        value = "";
                                    }
                                    insertObjDy[l] = value;
                                }
                                String finalQuery = buildQuery(colNames, insertObjDy);
                                // String deleteQuery = "delete from "+targetTable.trim()+" where target_id='"+targetId+"' and t_date='"+destDate+"'";
                                // alldeleteQueries.add(deleteQuery);
                                allInsertQueries.add(finalQuery);
                            }
                        }
                    }
                }
            }

        } else if (periodType.equalsIgnoreCase("Quarter")) {
            // sourceAllDataDates = "select distinct t_month from "+targetTable.trim()+" where target_id='"+targetId+"' and t_date between to_date('"+SourceFromDate+"','dd-mm-yy') and to_date('"+SourceToDate+"','dd-mm-yy')";
            //  sourceAllDataDates ="select distinct t_month from "+targetTable.trim()+" where target_id='"+targetId+"' and to_date(t_month,'mm-yy') between to_date('"+SourceFromDate.substring(3)+"','mm-yy') and to_date('"+SourceToDate.substring(3)+"','mm-yy')";
            // allData = "select * from "+targetTable.trim()+" where target_id='"+targetId+"' and t_month between  to_date('"+SourceFromDate+"','dd-mm-yy') and to_date('"+SourceToDate+"','dd-mm-yy')";
            // allData = "select * from "+targetTable.trim()+" where target_id='"+targetId+"' and to_date(t_month,'mm-yy') between to_date('"+SourceFromDate.substring(3)+"','mm-yy') and to_date('"+SourceToDate.substring(3)+"','mm-yy')";
            String sMonths = "select distinct to_char(cmq_st_date,'dd-mm-yy') from prg_acn_mon_denom where cmqtr=" + SourceFromDate.substring(1, 2) + " and cyear=" + SourceFromDate.substring(3);
            String dMonths = "select distinct to_char(cmq_st_date,'dd-mm-yy') from prg_acn_mon_denom where cmqtr=" + SourceToDate.substring(1, 2) + " and cyear=" + SourceToDate.substring(3);// mon_name='"++"'";
            PbReturnObject sOb = execSelectSQL(sMonths);
            PbReturnObject dOb = execSelectSQL(dMonths);
            String sD = sOb.getFieldValueString(0, 0);
            String dD = dOb.getFieldValueString(0, 0);
            // String sdMonths = "select to_char(cm_st_date,'dd-mm-yy') from prg_acn_mon_denom where mon_name='"+DestFromDate+"'";
            // String ddMonths = "select to_char(cm_st_date,'dd-mm-yy') from prg_acn_mon_denom where mon_name='"+DestToDate+"'";
            String sdMonths = "select distinct to_char(cmq_st_date,'dd-mm-yy') from prg_acn_mon_denom where cmqtr='" + DestFromDate.substring(1, 2) + "' and cyear=" + DestFromDate.substring(3);
            String ddMonths = "select distinct to_char(cmq_st_date,'dd-mm-yy') from prg_acn_mon_denom where cmqtr='" + DestToDate.substring(1, 2) + "' and cyear=" + DestToDate.substring(3);
            PbReturnObject sOb1 = execSelectSQL(sdMonths);
            PbReturnObject dOb2 = execSelectSQL(ddMonths);
            String sDdd = sOb1.getFieldValueString(0, 0);
            String dDdd = dOb2.getFieldValueString(0, 0);

            //  sourceDates = "select DISTINCT  to_char(ddate,'MON-yy') dd,cqtr,cyear,to_char(ddate,'mm-yy') from pr_day_denom where ddate between to_date('"+sD+"','dd-mm-yy') and to_date('"+dD+"','dd-mm-yy') ORDER by 3,4";
            //  destDates = "select DISTINCT  to_char(ddate,'MON-yy') dd,cqtr,cyear,to_char(ddate,'mm-yy') from pr_day_denom where ddate between to_date('"+sDdd+"','dd-mm-yy') and to_date('"+dDdd+"','dd-mm-yy') ORDER by 3,4";
            sourceDates = "select DISTINCT  cmqtr cqtr,cyear,to_char(cmq_st_date,'mm-yy') dd from prg_acn_mon_denom where cmq_st_date between to_date('" + sD + "','dd-mm-yy') and to_date('" + dD + "','dd-mm-yy') ORDER by 2,1";
            destDates = "select DISTINCT  cmqtr cqtr,cyear,to_char(cmq_st_date,'mm-yy') dd from prg_acn_mon_denom where cmq_st_date between to_date('" + sDdd + "','dd-mm-yy') and to_date('" + dDdd + "','dd-mm-yy') ORDER by 2,1";

            // sourceDates = "select DISTINCT  to_char(ddate,'MON-yy') dd,cqtr,cyear,to_char(ddate,'mm-yy') from pr_day_denom where ddate between to_date('"+SourceFromDate+"','dd-mm-yy') and to_date('"+SourceToDate+"','dd-mm-yy') ORDER by 3,4";
            //  destDates = "select DISTINCT  to_char(ddate,'MON-yy') dd,cqtr,cyear,to_char(ddate,'mm-yy') from pr_day_denom where ddate between to_date('"+DestFromDate+"','dd-mm-yy') and to_date('"+DestToDate+"','dd-mm-yy') ORDER by 3,4";
            PbReturnObject sourceDatesOb = execSelectSQL(sourceDates);
            PbReturnObject destDatesOb = execSelectSQL(destDates);
            HashMap sourceDatesMap = new HashMap();
            HashMap destDatesMap = new HashMap();
            ArrayList sdetails = new ArrayList();
            ArrayList destDetails = new ArrayList();
            for (int p = 0; p < sourceDatesOb.getRowCount(); p++) {
                //sdetails.add(0,sourceDatesOb.getFieldValueString(p,"CQTR"));
                sdetails.add(sourceDatesOb.getFieldValueString(p, "CQTR") + "-" + sourceDatesOb.getFieldValueString(p, "CYEAR"));
                sourceDatesMap.put(sourceDatesOb.getFieldValueString(p, "CQTR") + "-" + sourceDatesOb.getFieldValueString(p, "CYEAR"), sdetails);
            }
            for (int p = 0; p < destDatesOb.getRowCount(); p++) {
                ArrayList newVals = new ArrayList();
                newVals.add(0, destDatesOb.getFieldValueString(p, "CQTR"));
                newVals.add(1, destDatesOb.getFieldValueString(p, "CYEAR"));
                destDatesMap.put(destDatesOb.getFieldValueString(p, "CQTR") + "-" + destDatesOb.getFieldValueString(p, "CYEAR"), newVals);
                destDetails.add(destDatesOb.getFieldValueString(p, "CQTR") + "-" + destDatesOb.getFieldValueString(p, "CYEAR"));
            }
            boolean fl = false;
            for (int y = 0; y < sdetails.size(); y++) {
                String sourceD = (String) sdetails.get(y);
                String query = "Select * from " + targetTable.trim() + " where target_id='" + targetId + "' and t_qtr='" + sourceD + "'";
                PbReturnObject dataOb = execSelectSQL(query);
                String desDate = "";
                if (destDetails.get(y) != null) {
                    desDate = (String) destDetails.get(y);
                    ArrayList al = (ArrayList) destDatesMap.get(desDate);
                    String t_year = (String) al.get(1);
                    String t_qtr = (String) al.get(0) + "-" + (String) al.get(1);

                    if (y == 0) {
                        String cols[] = dataOb.getColumnNames();
                        colNumbers = dataOb.getRowCount();
                        for (int u = 0; u < cols.length; u++) {
                            // ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("......"+cols[u]);
                            if (u == cols.length - 1) {
                                colNames = colNames + "," + cols[u];
                            } else if (u != 0) {
                                colNames = colNames + "," + cols[u];
                            } else {
                                colNames = cols[u];
                            }
                            columns.add(cols[u]);
                            //if(cols[u].equalsIgnoreCase(t_qtr))
                            allValsMap.put(cols[u], "");

                            if (u == 0 && (cols.length > 1)) {
                                replaceVals = replaceVals + "'&'" + ",";
                            } else if ((u == 0) && (cols.length == 1)) {
                                replaceVals = replaceVals + "'&'";
                            } else if (u == cols.length - 1) {
                                replaceVals = replaceVals + "'&'";
                            } else {
                                replaceVals = replaceVals + "'&'" + ",";
                            }

                        }
                        colNames = insertQuery + colNames + ") values(" + replaceVals + ")";
                    }

                    for (int p = 0; p < dataOb.getRowCount(); p++) {
                        int fact_id = getSequenceNumber(finalfactQuery);
                        Object insertObjDy[] = new Object[columns.size()];
                        String q = "";

                        for (int t = 0; t < columns.size(); t++) {
                            String colId = (String) columns.get(t);
                            String vall = dataOb.getFieldValueString(p, colId.trim().toUpperCase());

                            if (colId.equalsIgnoreCase(measureName)) {
                                int newVal = (Integer.parseInt(vall)) * (multiplier);
//                                allValsMap.put(colId, "" + newVal + "");
                                allValsMap.put(colId, String.valueOf(newVal));
                            } else {
                                allValsMap.put(colId, vall);
                            }
                        }
                        allValsMap.put("T_DATE", "");
//                        allValsMap.put("TARGET_ID", "" + destTargetId + "");
                        allValsMap.put("TARGET_ID", String.valueOf(destTargetId));
                        allValsMap.put("T_MONTH", desDate);
                        allValsMap.put("T_QTR", t_qtr);
                        allValsMap.put("T_YEAR", t_year);
//                        allValsMap.put("FACT_ID", "" + fact_id + "");
                        allValsMap.put("FACT_ID", String.valueOf(fact_id));
                        for (int l = 0; l < columns.size(); l++) {
                            String value = (String) allValsMap.get(columns.get(l));
                            if (value == null) {
                                value = "";
                            }
                            insertObjDy[l] = value;
                        }
                        String finalQuery = buildQuery(colNames, insertObjDy);

                        //   String deleteQuery = "delete from "+targetTable.trim()+" where target_id='"+targetId+"' and t_month='"+desDate+"'";
                        //   alldeleteQueries.add(deleteQuery);
                        allInsertQueries.add(finalQuery);
                    }
                }
            }
        } else if (periodType.equalsIgnoreCase("Year")) {
            // sourceAllDataDates = "select distinct t_month from "+targetTable.trim()+" where target_id='"+targetId+"' and t_date between to_date('"+SourceFromDate+"','dd-mm-yy') and to_date('"+SourceToDate+"','dd-mm-yy')";
            //  sourceAllDataDates ="select distinct t_month from "+targetTable.trim()+" where target_id='"+targetId+"' and to_date(t_month,'mm-yy') between to_date('"+SourceFromDate.substring(3)+"','mm-yy') and to_date('"+SourceToDate.substring(3)+"','mm-yy')";
            // allData = "select * from "+targetTable.trim()+" where target_id='"+targetId+"' and t_month between  to_date('"+SourceFromDate+"','dd-mm-yy') and to_date('"+SourceToDate+"','dd-mm-yy')";
            // allData = "select * from "+targetTable.trim()+" where target_id='"+targetId+"' and to_date(t_month,'mm-yy') between to_date('"+SourceFromDate.substring(3)+"','mm-yy') and to_date('"+SourceToDate.substring(3)+"','mm-yy')";
            String sMonths = "select distinct to_char(cmy_start_date,'dd-mm-yy') from prg_acn_mon_denom where cyear=" + SourceFromDate;
            String dMonths = "select distinct to_char(cmy_start_date,'dd-mm-yy') from prg_acn_mon_denom where cyear=" + SourceToDate;// mon_name='"++"'";
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("sMonths - "+sMonths+"dMonths - "+dMonths);
            PbReturnObject sOb = execSelectSQL(sMonths);
            PbReturnObject dOb = execSelectSQL(dMonths);
            String sD = sOb.getFieldValueString(0, 0);
            String dD = dOb.getFieldValueString(0, 0);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(DestFromDate+" DestFromDate -DestToDate - "+DestToDate);
            // String sdMonths = "select to_char(cm_st_date,'dd-mm-yy') from prg_acn_mon_denom where mon_name='"+DestFromDate+"'";
            // String ddMonths = "select to_char(cm_st_date,'dd-mm-yy') from prg_acn_mon_denom where mon_name='"+DestToDate+"'";
            String sdMonths = "select distinct to_char(cmy_start_date,'dd-mm-yy') from prg_acn_mon_denom where cyear=" + DestFromDate;
            String ddMonths = "select distinct to_char(cmy_start_date,'dd-mm-yy') from prg_acn_mon_denom where cyear=" + DestToDate;
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("sdMonths - "+sdMonths+" ddMonths - "+ddMonths);
            PbReturnObject sOb1 = execSelectSQL(sdMonths);
            PbReturnObject dOb2 = execSelectSQL(ddMonths);
            String sDdd = sOb1.getFieldValueString(0, 0);
            String dDdd = dOb2.getFieldValueString(0, 0);

            //  sourceDates = "select DISTINCT  to_char(ddate,'MON-yy') dd,cqtr,cyear,to_char(ddate,'mm-yy') from pr_day_denom where ddate between to_date('"+sD+"','dd-mm-yy') and to_date('"+dD+"','dd-mm-yy') ORDER by 3,4";
            //  destDates = "select DISTINCT  to_char(ddate,'MON-yy') dd,cqtr,cyear,to_char(ddate,'mm-yy') from pr_day_denom where ddate between to_date('"+sDdd+"','dd-mm-yy') and to_date('"+dDdd+"','dd-mm-yy') ORDER by 3,4";
            sourceDates = "select DISTINCT  cyear,to_char(cmy_start_date,'mm-yy') dd from prg_acn_mon_denom where cmy_start_date between to_date('" + sD + "','dd-mm-yy') and to_date('" + dD + "','dd-mm-yy') ORDER by 2,1";
            destDates = "select DISTINCT  cyear,to_char(cmy_start_date,'mm-yy') dd from prg_acn_mon_denom where cmy_start_date between to_date('" + sDdd + "','dd-mm-yy') and to_date('" + dDdd + "','dd-mm-yy') ORDER by 2,1";

            // sourceDates = "select DISTINCT  to_char(ddate,'MON-yy') dd,cqtr,cyear,to_char(ddate,'mm-yy') from pr_day_denom where ddate between to_date('"+SourceFromDate+"','dd-mm-yy') and to_date('"+SourceToDate+"','dd-mm-yy') ORDER by 3,4";
            //  destDates = "select DISTINCT  to_char(ddate,'MON-yy') dd,cqtr,cyear,to_char(ddate,'mm-yy') from pr_day_denom where ddate between to_date('"+DestFromDate+"','dd-mm-yy') and to_date('"+DestToDate+"','dd-mm-yy') ORDER by 3,4";
            PbReturnObject sourceDatesOb = execSelectSQL(sourceDates);
            PbReturnObject destDatesOb = execSelectSQL(destDates);
            HashMap sourceDatesMap = new HashMap();
            HashMap destDatesMap = new HashMap();
            ArrayList sdetails = new ArrayList();
            ArrayList destDetails = new ArrayList();
            for (int p = 0; p < sourceDatesOb.getRowCount(); p++) {
                //sdetails.add(0,sourceDatesOb.getFieldValueString(p,"CQTR"));
                sdetails.add(sourceDatesOb.getFieldValueString(p, "CYEAR"));
                sourceDatesMap.put(sourceDatesOb.getFieldValueString(p, "CYEAR"), sdetails);
            }
            for (int p = 0; p < destDatesOb.getRowCount(); p++) {
                ArrayList newVals = new ArrayList();
                newVals.add(0, destDatesOb.getFieldValueString(p, "CYEAR"));
                // newVals.add(1,destDatesOb.getFieldValueString(p,"CYEAR"));
                destDatesMap.put(destDatesOb.getFieldValueString(p, "CYEAR"), newVals);
                destDetails.add(destDatesOb.getFieldValueString(p, "CYEAR"));
            }
            boolean fl = false;
            for (int y = 0; y < sdetails.size(); y++) {
                String sourceD = (String) sdetails.get(y);
                String query = "Select * from " + targetTable.trim() + " where target_id='" + targetId + "' and t_year='" + sourceD + "'";
                PbReturnObject dataOb = execSelectSQL(query);
                String desDate = "";
                if (destDetails.get(y) != null) {
                    //  desDate = (String) destDetails.get(y);
                    // ArrayList al = (ArrayList) destDatesMap.get(desDate);
                    String t_year = destDetails.get(y).toString();//(String)al.get(0);
                    String t_qtr = "";//(String)al.get(0)+"-"+(String)al.get(1);
                    if (dataOb.getRowCount() > 0 && fl == false) {
                        String cols[] = dataOb.getColumnNames();
                        colNumbers = dataOb.getRowCount();
                        for (int u = 0; u < cols.length; u++) {
                            if (u == cols.length - 1) {
                                colNames = colNames + "," + cols[u];
                            } else if (u != 0) {
                                colNames = colNames + "," + cols[u];
                            } else {
                                colNames = cols[u];
                            }
                            columns.add(cols[u]);
                            //if(cols[u].equalsIgnoreCase(t_qtr))
                            allValsMap.put(cols[u], "");

                            if (u == 0 && (cols.length > 1)) {
                                replaceVals = replaceVals + "'&'" + ",";
                            } else if ((u == 0) && (cols.length == 1)) {
                                replaceVals = replaceVals + "'&'";
                            } else if (u == cols.length - 1) {
                                replaceVals = replaceVals + "'&'";
                            } else {
                                replaceVals = replaceVals + "'&'" + ",";
                            }

                        }
                        colNames = insertQuery + colNames + ") values(" + replaceVals + ")";
                        fl = true;
                    }

                    for (int p = 0; p < dataOb.getRowCount(); p++) {
                        int fact_id = getSequenceNumber(finalfactQuery);
                        Object insertObjDy[] = new Object[columns.size()];
                        String q = "";

                        for (int t = 0; t < columns.size(); t++) {
                            String colId = (String) columns.get(t);
                            String vall = dataOb.getFieldValueString(p, colId.trim().toUpperCase());

                            if (colId.equalsIgnoreCase(measureName)) {
                                int newVal = (Integer.parseInt(vall)) * (multiplier);
//                                allValsMap.put(colId, "" + newVal + "");
                                allValsMap.put(colId, String.valueOf(newVal));
                            } else {
                                allValsMap.put(colId, vall);
                            }
                        }
                        allValsMap.put("T_DATE", "");
//                        allValsMap.put("TARGET_ID", "" + destTargetId + "");
                        allValsMap.put("TARGET_ID", String.valueOf(destTargetId));
                        allValsMap.put("T_MONTH", desDate);
                        allValsMap.put("T_QTR", t_qtr);
                        allValsMap.put("T_YEAR", t_year);
//                        allValsMap.put("FACT_ID", "" + fact_id + "");
                        allValsMap.put("FACT_ID", String.valueOf(fact_id));
                        for (int l = 0; l < columns.size(); l++) {
                            String value = (String) allValsMap.get(columns.get(l));
                            if (value == null) {
                                value = "";
                            }
                            insertObjDy[l] = value;
                        }
                        String finalQuery = buildQuery(colNames, insertObjDy);

                        //   String deleteQuery = "delete from "+targetTable.trim()+" where target_id='"+targetId+"' and t_month='"+desDate+"'";
                        //   alldeleteQueries.add(deleteQuery);
                        allInsertQueries.add(finalQuery);
                    }
                }
            }
        }

        for (int y = 0; y < allInsertQueries.size(); y++) {
            try {
                executeMultiple(allInsertQueries);
            } catch (Exception ex) {
                logger.error("Exception: ", ex);
            }
        }
        ////
    }

    public PbReturnObject getAllLocks(Session ppmp) throws Exception {
        PbReturnObject pbro = null;
        String query = getResourceBundle().getString("getAllLocks");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("getAllLocks query is: "+query);

        try {
            pbro = execSelectSQL(query);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return pbro;
    }

    public PbReturnObject getUserDetails(Session ppmp) throws Exception {
        PbReturnObject pbro = null;
        String query = getResourceBundle().getString("getUserDetails");
        PbTargetParamParams targetParams = (PbTargetParamParams) ppmp.getObject("prg.targetparam.qdparams.PbTargetParamParams");
        Object obj[] = new Object[1];
        obj[0] = targetParams.getUserId();
        String finalQuery = null;
        finalQuery = buildQuery(query, obj);

        try {
            pbro = execSelectSQL(finalQuery);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }

        return pbro;
    }
}
