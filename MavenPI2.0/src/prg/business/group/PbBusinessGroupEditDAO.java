package prg.business.group;

import com.google.gson.Gson;
import java.sql.SQLException;
import java.util.*;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

public class PbBusinessGroupEditDAO extends PbDb {

    public static Logger logger = Logger.getLogger(PbBusinessGroupEditDAO.class);
    ResourceBundle resourceBundle;
    ResourceBundle resBundle;
    StringBuffer sb = new StringBuffer();

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new PbBussGrpResBundleSqlServer();
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                resourceBundle = new PbBussGrpResBundleMysql();
            } else {
                resourceBundle = new PbBussGrpResourceBundle();
            }

        }
        return resourceBundle;
    }

    private ResourceBundle getResBundle() {
        if (this.resBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resBundle = new PbEditGroupResourceBundleSqlServer();
            } else {
                resBundle = new PbEditGroupResourceBundle();
            }
        }

        return resBundle;
    }

//    PbBussGrpResourceBundle resourceBundle = new PbBussGrpResourceBundle();
//    PbEditGroupResourceBundle repBundle = new PbEditGroupResourceBundle();
    public String getGroupDimDeleteStatus(String delGrpId, String delDimId) throws Exception {
        String status = "";
        String getGroupDimDeleteStatus = getResourceBundle().getString("getGroupDimDeleteStatus");
        Object grpDetails[] = new Object[2];
        grpDetails[0] = delGrpId;
        grpDetails[1] = delDimId;
        String fingetGroupDimDeleteStatus = buildQuery(getGroupDimDeleteStatus, grpDetails);
        PbReturnObject pbro = execSelectSQL(fingetGroupDimDeleteStatus);
        if (pbro.getRowCount() > 0) {
            status = "true";
        } else {
            status = "false";
        }

        return status;
    }

    public String getRelatedReportsForGroup(String delGrpId, String delDimId) throws Exception {
        String status = "";
        String getRelatedReportsForGroup = getResourceBundle().getString("getRelatedReportsForGroup");
        Object delObj[] = new Object[1];
        delObj[0] = delGrpId;
        String fingetRelatedReportsForGroup = buildQuery(getRelatedReportsForGroup, delObj);
        PbReturnObject delGrpRep = execSelectSQL(fingetRelatedReportsForGroup);
        if (delGrpRep.getRowCount() > 0) {
            status = "Exists";
        } else {
            status = "Not Exists";
        }
        return status;
    }

    public String deleteGroupDim(String delGrpId, String delDimId) throws Exception {
        String status = "";
        Object grpObj[] = new Object[1];
        grpObj[0] = delGrpId;
        Object dimObj[] = new Object[1];
        dimObj[0] = delDimId;

        Object grpDimObj[] = new Object[2];
        grpDimObj[0] = delDimId;
        grpDimObj[1] = delGrpId;

        Object triObj[] = new Object[3];
        triObj[0] = delGrpId;
        triObj[1] = delDimId;
        triObj[2] = delGrpId;
        ArrayList delList = new ArrayList();
        ArrayList drilDelList = new ArrayList();



        String deleteCustomRoleDrillForDim = getResourceBundle().getString("deleteCustomRoleDrillForDim");
        String findeleteCustomRoleDrillForDim = buildQuery(deleteCustomRoleDrillForDim, triObj);
        //////////////////////////////////////////////////////////////////////////////////////.println.println(" findeleteCustomRoleDrillForDim "+findeleteCustomRoleDrillForDim);
        drilDelList.add(findeleteCustomRoleDrillForDim);
        try {
            executeMultiple(drilDelList);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        String getUpdatableDrill = getResourceBundle().getString("getUpdatableDrill");
        String fingetUpdatableDrill = buildQuery(getUpdatableDrill, triObj);
        PbReturnObject pbro = execSelectSQL(fingetUpdatableDrill);
        String getDeletedDimsMemberForDrill = getResourceBundle().getString("getDeletedDimsMemberForDrill");
        String getDeletedMem = buildQuery(getDeletedDimsMemberForDrill, grpDimObj);
        ArrayList deletedMem = new ArrayList();
        PbReturnObject memObj = execSelectSQL(getDeletedMem);
        drilDelList = new ArrayList();
        for (int m = 0; m < memObj.getRowCount(); m++) {
            deletedMem.add(memObj.getFieldValueString(m, "INFO_MEMBER_ID"));
        }
        //for updating child ids of deleted members
        String upadteQ = "update prg_grp_role_custom_drill set child_member_id=& where custom_drill_id=&";
        String drillId = "";
        String memId = "";
        for (int m = 0; m < pbro.getRowCount(); m++) {
            drillId = pbro.getFieldValueString(m, "DRILL_ID");
            memId = pbro.getFieldValueString(m, "MEMBER_ID");
            Object drillObj[] = new Object[2];
            drillObj[0] = memId;
            drillObj[1] = drillId;
            String finupadteQ = buildQuery(upadteQ, drillObj);
            //////////////////////////////////////////////////////////////////////////////////////.println.println(" finupadteQ "+finupadteQ);
            drilDelList.add(finupadteQ);
        }
        try {
            executeMultiple(drilDelList);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        // to delete from adim tables for role
        String deleteAdimKeyValEle = getResourceBundle().getString("deleteAdimKeyValEle");
        String findeleteAdimKeyValEle = buildQuery(deleteAdimKeyValEle, grpDimObj);
        delList.add(findeleteAdimKeyValEle);
        String deleteUserAllAdimDetails = getResourceBundle().getString("deleteUserAllAdimDetails");
        String findeleteUserAllAdimDetails = buildQuery(deleteUserAllAdimDetails, grpDimObj);
        delList.add(findeleteUserAllAdimDetails);
        String deleteUserAllAdimMaster = getResourceBundle().getString("deleteUserAllAdimMaster");
        String findeleteUserAllAdimMaster = buildQuery(deleteUserAllAdimMaster, grpDimObj);
        delList.add(findeleteUserAllAdimMaster);

        // to delete from dDim related tables for role
        String deleteDDimKeyValEle = getResourceBundle().getString("deleteDDimKeyValEle");
        String findeleteDDimKeyValEle = buildQuery(deleteDDimKeyValEle, grpDimObj);
        delList.add(findeleteDDimKeyValEle);
        String deleteDDimDetails = getResourceBundle().getString("deleteDDimDetails");
        String findeleteDDimDetails = buildQuery(deleteDDimDetails, grpDimObj);
        delList.add(findeleteDDimDetails);
        String deleteDdimMaster = getResourceBundle().getString("deleteDdimMaster");
        String findeleteDdimMaster = buildQuery(deleteDdimMaster, grpDimObj);
        delList.add(findeleteDdimMaster);

        // to delete uaer_all_info_details table for role
        String bussTableQ = "select * from prg_grp_buss_table where buss_table_id in(select tab_id from prg_grp_dim_tables where dim_id=&) and grp_id=&";
        String finbussTableQ = buildQuery(bussTableQ, grpDimObj);
        //////////////////////////////////////////////////////////////////////////////////////.println.println(" finbussTableQ "+finbussTableQ);
        PbReturnObject tabObj = execSelectSQL(finbussTableQ);
        String bussTabIds = "";
        for (int y = 0; y < tabObj.getRowCount(); y++) {
            bussTabIds = bussTabIds + ",'" + tabObj.getFieldValueString(y, "BUSS_TABLE_ID") + "'";
        }
        if (bussTabIds.length() > 0) {
            bussTabIds = bussTabIds.substring(1);
        }

        String deleteUserAllInfoDetails = getResourceBundle().getString("deleteUserAllInfoDetails");
        Object delOb[] = new Object[3];
        delOb[0] = bussTabIds;
        delOb[1] = delGrpId;
        delOb[2] = delGrpId;
        String findeleteUserAllInfoDetails = buildQuery(deleteUserAllInfoDetails, delOb);

        delList.add(findeleteUserAllInfoDetails);

        // to delete from subfolder_elements and subfolder_tables
        String deleteUserSubFolderElements = getResourceBundle().getString("deleteUserSubFolderElements");
        Object bussTabObj[] = new Object[1];
        bussTabObj[0] = bussTabIds;
        String findeleteUserSubFolderElements = buildQuery(deleteUserSubFolderElements, bussTabObj);
        delList.add(findeleteUserSubFolderElements);

        String deleteUserFolderTableDim = getResourceBundle().getString("deleteUserFolderTableDim");
        String findeleteUserFolderTableDim = buildQuery(deleteUserFolderTableDim, bussTabObj);
        delList.add(findeleteUserFolderTableDim);

        String deleteUserFolderTableDimMem = getResourceBundle().getString("deleteUserFolderTableDimMem");
        String findeleteUserFolderTableDimMem = buildQuery(deleteUserFolderTableDimMem, grpDimObj);
        delList.add(findeleteUserFolderTableDimMem);

        // to delete from bussiness level tables
        String getBussSrcForDimTable = getResourceBundle().getString("getBussSrcForDimTable");
        String fingetBussSrcForDimTable = buildQuery(getBussSrcForDimTable, grpDimObj);
        PbReturnObject srcObject = execSelectSQL(fingetBussSrcForDimTable);
        String srcIds = "";
        for (int g = 0; g < srcObject.getRowCount(); g++) {
            srcIds = srcIds + ",'" + srcObject.getFieldValueString(g, "BUSS_SOURCE_ID") + "'";
        }
        if (srcIds.length() > 0) {
            srcIds = srcIds.substring(1);
        }

        Object srcIdsObj[] = new Object[1];
        srcIdsObj[0] = srcIds;
        String deleteDimTabSrcMaster = getResourceBundle().getString("deleteDimTabSrcMaster");
        String findeleteDimTabSrcMaster = buildQuery(deleteDimTabSrcMaster, srcIdsObj);
        delList.add(findeleteDimTabSrcMaster);
        String deleteDimTabSrcDetails = getResourceBundle().getString("deleteDimTabSrcDetails");
        String findeleteDimTabSrcDetails = buildQuery(deleteDimTabSrcDetails, srcIdsObj);
        delList.add(findeleteDimTabSrcDetails);

        String deleteGrpBussTabDetails = getResourceBundle().getString("deleteGrpBussTabDetails");
        String findeleteGrpBussTabDetails = buildQuery(deleteGrpBussTabDetails, bussTabObj);
        delList.add(findeleteGrpBussTabDetails);

        String deleteGrpDimBussTable = getResourceBundle().getString("deleteGrpDimBussTable");
        String findeleteGrpDimBussTable = buildQuery(deleteGrpDimBussTable, grpDimObj);
        delList.add(findeleteGrpDimBussTable);

        String deleteGrpDimMemberDetails = getResourceBundle().getString("deleteGrpDimMemberDetails");
        String findeleteGrpDimMemberDetails = buildQuery(deleteGrpDimMemberDetails, dimObj);
        delList.add(findeleteGrpDimMemberDetails);

        String deleteGrpDimMember = getResourceBundle().getString("deleteGrpDimMember");
        String findeleteGrpDimMember = buildQuery(deleteGrpDimMember, dimObj);
        delList.add(findeleteGrpDimMember); //

        String deleteGrpDimTabDetails = getResourceBundle().getString("deleteGrpDimTabDetails");
        String findeleteGrpDimTabDetails = buildQuery(deleteGrpDimTabDetails, dimObj);
        delList.add(findeleteGrpDimTabDetails);

        String deleteGrpDimTable = getResourceBundle().getString("deleteGrpDimTable");
        String findeleteGrpDimTable = buildQuery(deleteGrpDimTable, dimObj);
        delList.add(findeleteGrpDimTable); //

        String deleteGrpDimension = getResourceBundle().getString("deleteGrpDimension");
        String findeleteGrpDimension = buildQuery(deleteGrpDimension, grpDimObj);
        delList.add(findeleteGrpDimension);

        for (int m = 0; m < delList.size(); m++) {
            //////////////////////////////////////////////////////////////////////////////////////.println.println(" ........... "+delList.get(m).toString());
        }
        try {
            executeMultiple(delList);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return status;
    }

    public String getGrpDimRoles(String grpId) throws Exception {
        String status = "";
        String grpQuery = "select * from prg_user_all_adim_master where info_folder_id in(select folder_id from prg_user_folder where grp_id=" + grpId + ")";
        //////////////////////////////////////////////////////////////////////////////////////.println.println(" grpQuery "+grpQuery);
        PbReturnObject pbro = execSelectSQL(grpQuery);
        if (pbro.getRowCount() > 0) {
            status = "Exists";
        } else {
            status = "Not Exists";
        }
        return status;
    }

    public void addExtraDimensionsToRole(String grpId, String queryLayerdimIds) throws Exception {
        String dbLevelDimsIds[] = queryLayerdimIds.split(",");
        String dimIdsQ = "select * from prg_grp_dimensions WHERE qry_dim_id in(" + queryLayerdimIds + ") and grp_id=" + grpId;
        PbReturnObject pbro = execSelectSQL(dimIdsQ);
        HashMap bussDims = new HashMap();
        for (int g = 0; g < pbro.getRowCount(); g++) {
            bussDims.put(pbro.getFieldValueString(g, "QRY_DIM_ID"), pbro.getFieldValueString(g, "DIM_ID"));
        }
        String getDetailsToInsertDimTab = "SELECT DISTINCT gd.dim_id,gdm.member_name  as disp_name,gdt.dim_tab_id,gdt.tab_id,"
                + "gd.dim_name,gdm.member_id,gdm.member_name , gdm.use_denom_table ,  gd.default_hierarchy_id, "
                + "gdm.denom_tab_id         FROM prg_grp_dimensions gd,   prg_grp_dim_tables gdt     ,  prg_grp_dim_member gdm   WHERE gd.dim_id =gdt.dim_id AND gdm.dim_id    = gd.dim_id AND gdm.dim_tab_id= gdt.dim_tab_id AND gd.grp_id     ='&' ORDER BY gd.dim_id";
        String insertSubFolderTables = "insert into PRG_USER_SUB_FOLDER_TABLES(SUB_FOLDER_TAB_ID,SUB_FOLDER_ID, BUSS_TABLE_ID, IS_DIMENSION, IS_FACT, IS_BUCKET, "
                + "DISP_NAME, DIM_ID,DIM_TAB_ID ,DIM_NAME,MEMBER_ID,MEMBER_NAME,USE_DENOM_TABLE,DEFAULT_HIERARCHY_ID,DENOM_TAB_ID,USE_REPORT_MEMBER,USE_REPORT_DIM_MEMBER)"
                + " values ('&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','Y','Y')";
    }

    public String checkGroupNameForConn(String grpName, String connId) throws Exception {
        String status = "true";
        String query = "select * from prg_grp_master where connection_id=" + connId;
        PbReturnObject pbro = execSelectSQL(query);
        String localName = "";
        for (int m = 0; m < pbro.getRowCount(); m++) {
            localName = pbro.getFieldValueString(m, "GRP_NAME");
            if (status.equalsIgnoreCase("false")) {
                break;
            }
            if (status.equalsIgnoreCase("true")) {
                if (localName.equalsIgnoreCase(grpName)) {
                    status = "false";
                }
            }

        }
        //////////////////////////////////////////////////////////////////////////////////////.println.println(" status "+status);
        return status;
    }

    public PbReturnObject getGroupDetails(String grpId) throws Exception {
        PbReturnObject all = new PbReturnObject();
        String grpDimQ = "select * from prg_grp_dimensions where grp_id=" + grpId + "  and dim_name not in('Time') and qry_dim_id is not null order by dim_name";
        String grpFactQ = "select * from prg_grp_dim_tables where dim_id in(select dim_id from prg_grp_dimensions where grp_id=" + grpId + ") ";
        PbReturnObject dimObj = execSelectSQL(grpDimQ);
        //////////////////////////////////////////////////////////////////////////////////.println.println(" grpDimQ "+grpDimQ);
        //PbReturnObject factObj=execSelectSQL(grpFactQ);
        String factNameQ = "select * from prg_grp_buss_table where buss_table_id in(select tab_id from prg_grp_dim_tables "
                + " where dim_id in(select dim_id from prg_grp_dimensions where grp_id=" + grpId + "))";
        PbReturnObject factObj = execSelectSQL(factNameQ);
        all.setObject("dimObj", dimObj);
        all.setObject("factObj", factObj);
        return all;
    }

    public PbReturnObject getGroupDetailsWithBucket(String grpId) throws Exception {
        PbReturnObject all = new PbReturnObject();
        String grpDimQ = "select * from prg_grp_dimensions where grp_id=" + grpId + "  and dim_name not in('Time') order by dim_name";
        String grpFactQ = "select * from prg_grp_dim_tables where dim_id in(select dim_id from prg_grp_dimensions where grp_id=" + grpId + ")";
        PbReturnObject dimObj = execSelectSQL(grpDimQ);
        //////////////////////////////////////////////////////////////////////////////////.println.println(" grpDimQ "+grpDimQ);
        //PbReturnObject factObj=execSelectSQL(grpFactQ);
        String factNameQ = "select * from prg_grp_buss_table where buss_table_id in(select tab_id from prg_grp_dim_tables "
                + " where dim_id in(select dim_id from prg_grp_dimensions where grp_id=" + grpId + "))";
        PbReturnObject factObj = execSelectSQL(factNameQ);
        all.setObject("dimObj", dimObj);
        all.setObject("factObj", factObj);
        return all;
    }

    public PbReturnObject getGroupRoles(String grpId) throws Exception {
        PbReturnObject all = new PbReturnObject();
        String grpRoleQ = "select * from prg_user_folder where grp_id=" + grpId;
        ////.println("grpRoleQ\t"+grpRoleQ);

        PbReturnObject grpRoleObj = execSelectSQL(grpRoleQ);
        all.setObject("grpRoleObj", grpRoleObj);
        return all;
    }

    public HashMap getGroupRoleExtraDims(String grpId) throws Exception {
        PbReturnObject all = new PbReturnObject();
        String grpRoleQ = "select * from prg_user_folder where grp_id=" + grpId;

        PbReturnObject grpRoleObj = execSelectSQL(grpRoleQ);
        HashMap details = new HashMap();
        HashMap allMap = new HashMap();
        for (int g = 0; g < grpRoleObj.getRowCount(); g++) {
            String folderId = grpRoleObj.getFieldValueString(g, 0);
            String extraDimQ = "";
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                extraDimQ = "select DIM_ID,DIM_NAME from prg_grp_dimensions where dim_id in(select dim_id from prg_grp_dimensions where grp_id=" + grpId + " except select distinct dim_id from "
                        + " prg_user_sub_folder_tables t where sub_folder_id in(select sub_folder_id from prg_user_folder_detail where"
                        + " folder_id=" + folderId + ") and is_dimension='Y' )";
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                extraDimQ = "select DIM_ID,DIM_NAME from prg_grp_dimensions where dim_id in(select dim_id from prg_grp_dimensions where grp_id=" + grpId + " and dim_id not in(select distinct dim_id from "
                        + " prg_user_sub_folder_tables t where sub_folder_id in(select sub_folder_id from prg_user_folder_detail where"
                        + " folder_id='" + folderId + "') and is_dimension='Y' ))";
            } else {
                extraDimQ = "select dim_id,dim_name from prg_grp_dimensions where dim_id in(select dim_id from prg_grp_dimensions where grp_id=" + grpId + " minus select distinct dim_id from "
                        + " prg_user_sub_folder_tables t where sub_folder_id in(select sub_folder_id from prg_user_folder_detail where"
                        + " folder_id=" + folderId + ") and is_dimension='Y' )";
            }

            //////////////////////////////////////////////////////////////////////////////////////.println.println(" extraDimQ "+extraDimQ);
            PbReturnObject pbro = execSelectSQL(extraDimQ);
            details.put(folderId, pbro);
        }
        all.setObject("grpRoleObj", grpRoleObj);
        allMap.put("Dim", details);
        return allMap;
    }

    public ArrayList getGrpRoles(String grpId) throws Exception {
        String roleQ = "select * from prg_user_folder where grp_id=" + grpId;
        //////////////////////////////////////////////////////////////////////////////////.println.println(" roleQ "+roleQ);
        PbReturnObject pbro = execSelectSQL(roleQ);
        ArrayList roles = new ArrayList();
        for (int t = 0; t < pbro.getRowCount(); t++) {
            roles.add(pbro.getFieldValueString(t, "FOLDER_ID"));
        }
        return roles;
    }

//added by susheela start 19-12-09
    public void updateGrpDimMemberStatus(String memberId) throws Exception {
        String stQuery = "select * from prg_grp_dim_member where member_id=" + memberId;
        ArrayList al = new ArrayList();
        String oldSt = "";
        String newSt = "";
        PbReturnObject prObj = execSelectSQL(stQuery);
        oldSt = prObj.getFieldValueString(0, "MEMBER_USE_NEXTLEVEL");
        if (oldSt.equalsIgnoreCase("Y")) {
            newSt = "N";
        } else {
            newSt = "Y";
        }
        String updateQ = "update prg_grp_dim_member set MEMBER_USE_NEXTLEVEL='" + newSt + "' where member_id=" + memberId;
        //////////////////////////////////////////////////////////////////////////////////.println.println(" updateQ "+updateQ);
        al.add(updateQ);
        try {
            executeMultiple(al);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public void updateGrpDimStatus(String dimId) throws Exception {
        String stQuery = "select * from prg_grp_dimensions where dim_id=" + dimId;
        //////////////////////////////////////////////////////////////////////////////////.println.println(" stQuery "+stQuery);
        ArrayList al = new ArrayList();
        String oldSt = "";
        String newSt = "";
        PbReturnObject prObj = execSelectSQL(stQuery);
        oldSt = prObj.getFieldValueString(0, "DIM_USE_NEXTLEVEL");
        if (oldSt.equalsIgnoreCase("Y")) {
            newSt = "N";
        } else {
            newSt = "Y";
        }
        String updateQ = "update prg_grp_dimensions set DIM_USE_NEXTLEVEL='" + newSt + "' where dim_id=" + dimId;
        // String updateQMember = "update prg_grp_dim_member set MEMBER_USE_NEXTLEVEL='"+newSt+"' where dim_id="+dimId;
        //////////////////////////////////////////////////////////////////////////////////.println.println(" updateQ "+updateQ);
        // //////////////////////////////////////////////////////////////////////////////////.println.println(" updateQMember "+updateQMember);
        // al.add(updateQMember);
        al.add(updateQ);
        try {
            executeMultiple(al);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public void updateGrpFactStatus(String bussTabId) throws Exception {
        String stQuery = "select * from prg_grp_buss_table where buss_table_id=" + bussTabId;
        //////////////////////////////////////////////////////////////////////////////////.println.println(" stQuery "+stQuery);
        ArrayList al = new ArrayList();
        String oldSt = "";
        String newSt = "";
        PbReturnObject prObj = execSelectSQL(stQuery);
        oldSt = prObj.getFieldValueString(0, "TAB_USE_NEXT_LEVEL");
        if (oldSt.equalsIgnoreCase("Y")) {
            newSt = "N";
        } else {
            newSt = "Y";
        }
        String updateQ = "update prg_grp_buss_table set TAB_USE_NEXT_LEVEL='" + newSt + "' where buss_table_id=" + bussTabId;
        // String updateQMember = "update prg_grp_dim_member set MEMBER_USE_NEXTLEVEL='"+newSt+"' where dim_id="+dimId;
        //////////////////////////////////////////////////////////////////////////////////.println.println(" updateQ "+updateQ);
        al.add(updateQ);
        try {
            executeMultiple(al);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public HashMap checkFactAndDimOnRoleCreation(String grpId) throws Exception {
        HashMap details = new HashMap();
        String status = "";
        String message = "";
        //to find the selected facts
        String allDefaultFactQ = "select buss_table_id,buss_table_name from prg_grp_buss_table where grp_id='" + grpId + "' and buss_type not in('Target Table')";
        PbReturnObject allFObj = execSelectSQL(allDefaultFactQ);
        ArrayList allF = new ArrayList();
        for (int m = 0; m < allFObj.getRowCount(); m++) {
            allF.add(allFObj.getFieldValueString(m, "BUSS_TABLE_ID"));
        }

        String allFactQ = "select buss_table_id from prg_grp_buss_table where grp_id='" + grpId + "' and tab_use_next_level='Y'";
        // To get all the relation facts
        String relTabQ = "select buss_table_id from(select buss_table_id from prg_grp_buss_table where grp_id='" + grpId + "' and buss_table_id "
                + " in(select buss_table_id from prg_grp_buss_table where grp_id='" + grpId + "')  union select distinct buss_table_id2 from prg_grp_buss_table_rlt_master"
                + " where buss_table_id2 in (select buss_table_id from prg_grp_buss_table where grp_id='" + grpId + "') )";

        String relQ = "select * from prg_grp_buss_table_rlt_master where buss_table_id in (select buss_table_id from "
                + " prg_grp_buss_table where grp_id='" + grpId + "') union select * from prg_grp_buss_table_rlt_master where "
                + " buss_table_id2 in (select buss_table_id from prg_grp_buss_table where grp_id='" + grpId + "')";
        //////////////////////////////////////////////////////////////////////////////////.println.println(" relQ "+relQ);
        // all dimension tables
        String dimTabQ = "select distinct buss_table_id from prg_grp_buss_table where grp_id='" + grpId + "' and buss_table_id in( select "
                + " tab_id from prg_grp_dim_tables where dim_id in( select dim_id from prg_grp_dimensions where grp_id='" + grpId + "' and"
                + " prg_grp_dimensions.dim_use_nextlevel='Y'))";
        PbReturnObject relObj = execSelectSQL(relTabQ);

        ArrayList relatedFactsList = new ArrayList();
        for (int m = 0; m < relObj.getRowCount(); m++) {
            relatedFactsList.add(relObj.getFieldValueString(m, "BUSS_TABLE_ID"));
        }
        PbReturnObject factsObj = execSelectSQL(allFactQ);
        ArrayList facts = new ArrayList();
        PbReturnObject dimTabs = execSelectSQL(dimTabQ);
        for (int i = 0; i < dimTabs.getRowCount(); i++) {
            facts.add(dimTabs.getFieldValueString(i, "BUSS_TABLE_ID"));
        }
        ArrayList onlyFacts = new ArrayList();
        for (int m = 0; m < factsObj.getRowCount(); m++) {
            String onlyF = factsObj.getFieldValueString(m, "BUSS_TABLE_ID");
            if (!facts.contains(onlyF)) {
                onlyFacts.add(onlyF);
            }
        }
        //////////////////////////////////////////////////////////////////////////////////.println.println(" facts - dim "+facts);
        //////////////////////////////////////////////////////////////////////////////////.println.println(" onlyFacts "+onlyFacts);
        PbReturnObject allRelOfDimAndFactObj = execSelectSQL(relQ);
        //////////////////////////////////////////////////////////////////////////////////.println.println(" relQ "+relQ);
        String tab1 = "";
        String tab2 = "";
        //////////////////////////////////////////////////////////////////////////////////.println.println(" allF "+allF);
        boolean st = false;
        String msgTab = "";
        for (int m = 0; m < allRelOfDimAndFactObj.getRowCount(); m++) {
            tab1 = allRelOfDimAndFactObj.getFieldValueString(m, "BUSS_TABLE_ID");
            tab2 = allRelOfDimAndFactObj.getFieldValueString(m, "BUSS_TABLE_ID2");
            //////////////////////////////////////////////////////////////////////////////////.println.println(" tab1 "+tab1+" tab2 "+tab2);
            if (allF.contains(tab1) && allF.contains(tab2)) {
                if (facts.contains(tab1)) {
                    if (!onlyFacts.contains(tab2)) {
                        st = true;
                        msgTab = tab2;
                    }
                }
            }
            if (allF.contains(tab1) && allF.contains(tab2)) {
                if (facts.contains(tab2)) {
                    if (!onlyFacts.contains(tab1)) {
                        st = true;
                        msgTab = tab1;
                    }

                }
            }
            if (st == true) {
                break;
            }
        }
        //////////////////////////////////////////////////////////////////////////////////.println.println(" msgTab "+msgTab);
        for (int m = 0; m < allFObj.getRowCount(); m++) {
            if (!msgTab.equalsIgnoreCase("")) {
                if (msgTab.equalsIgnoreCase(allFObj.getFieldValueString(m, "BUSS_TABLE_ID"))) {
                    message = "Select the fact '" + allFObj.getFieldValueString(m, "BUSS_TABLE_NAME") + "' to create business role.";
                }
            }
        }
        //////////////////////////////////////////////////////////////////////////////////.println.println(" st= "+st);
        if (st == true) {
            status = "true";
        }
        if (st == false) {
            status = "false";
        }
        details.put("status", status);
        details.put("message", message);
        //////////////////////////////////////////////////////////////////////////////////.println.println(" details "+details);
        return details;
    }

    public String checkTimeDimForBusGroup(String grpId) throws Exception {
        String getTimeDimForGrp = "select * from PRG_GRP_DIMENSIONS where grp_id=" + grpId + " and dim_name in('Time') ";
        PbReturnObject timeDimObj = execSelectSQL(getTimeDimForGrp);
        String status = "";
        if (timeDimObj.getRowCount() > 0) {
            status = "exists";
        }
        return status;
    }

    public ArrayList getReportsForRole(String roleId) throws Exception {
        String roleQ = "select * from prg_ar_report_details where folder_id in(" + roleId + ")";
        //////////////////////////////////////////////////////////////////////////////////.println.println(" repQ "+roleQ);
        PbReturnObject pbro = execSelectSQL(roleQ);
        ArrayList reports = new ArrayList();
        for (int t = 0; t < pbro.getRowCount(); t++) {
            reports.add(pbro.getFieldValueString(t, "REPORT_ID"));
        }
        return reports;
    }

    public void checkDeleteReports(ArrayList reports, String delDimId) throws Exception {
        String deleteArGraphMaster = getResBundle().getString("deleteArGraphMaster");
        String deleteArPersonRep = getResBundle().getString("deleteArPersonRep");
        String deleteArQueryDet = getResBundle().getString("deleteArQueryDet");
        String deleteArParDetails = getResBundle().getString("deleteArParDetails");
        String deleteRepTimeDetail = getResBundle().getString("deleteRepTimeDetail");
        String deleteRepTimeMaster = getResBundle().getString("deleteRepTimeMaster");
        String deleteReportViewDetails = getResBundle().getString("deleteReportViewDetails");
        String deleteRepViewMaster = getResBundle().getString("deleteRepViewMaster");
        String deleteUserReports = getResBundle().getString("deleteUserReports");
        String deleteRepDetails = getResBundle().getString("deleteRepDetails");
        String deleteReportMaster = getResBundle().getString("deleteReportMaster");

        ArrayList delList = new ArrayList();

        String repId = "";
        for (int m = 0; m < reports.size(); m++) {
            repId = reports.get(m).toString();
            String getDimQ = "select * from prg_ar_report_param_details where report_id=" + repId + " and dim_id=" + delDimId;
            PbReturnObject repParaObj = execSelectSQL(getDimQ);
            Object repObj[] = new Object[1];
            repObj[0] = repId;
            if (repParaObj.getRowCount() > 0) {
                String findeleteArGraphMaster = buildQuery(deleteArGraphMaster, repObj);
                delList.add(findeleteArGraphMaster);
                String findeleteArPersonRep = buildQuery(deleteArPersonRep, repObj);
                delList.add(findeleteArPersonRep);
                String findeleteArQueryDet = buildQuery(deleteArQueryDet, repObj);
                delList.add(findeleteArQueryDet);

                String findeleteArParDetails = buildQuery(deleteArParDetails, repObj);
                delList.add(findeleteArParDetails);

                String findeleteRepTimeDetail = buildQuery(deleteRepTimeDetail, repObj);
                delList.add(findeleteRepTimeDetail);

                String findeleteRepTimeMaster = buildQuery(deleteRepTimeMaster, repObj);
                delList.add(findeleteRepTimeMaster);

                String findeleteReportViewDetailst = buildQuery(deleteReportViewDetails, repObj);
                delList.add(findeleteReportViewDetailst);

                String findeleteRepViewMaster = buildQuery(deleteRepViewMaster, repObj);
                delList.add(findeleteRepViewMaster);

                String findeleteUserReports = buildQuery(deleteUserReports, repObj);
                delList.add(findeleteUserReports);

                String findeleteRepDetails = buildQuery(deleteRepDetails, repObj);
                delList.add(findeleteRepDetails);

                String findeleteReportMaster = buildQuery(deleteReportMaster, repObj);
                delList.add(findeleteReportMaster);
            }
        }
        for (int m = 0; m < delList.size(); m++) //////////////////////////////////////////////////////////////////////////////////.println.println(" delList "+ delList.get(m).toString());
        {
            try {
                executeMultiple(delList);
            } catch (Exception e) {
                logger.error("Exception: ", e);
            }
        }
    }

    public PbReturnObject getExtraDimForGrp(String grpId) throws Exception {
        PbReturnObject dimObj = new PbReturnObject();
        String allConnDimQ = "select DIMENSION_ID, DIMENSION_NAME from prg_qry_dimensions where connection_id "
                + " in(select connection_id from prg_grp_master where grp_id=" + grpId + ")";
        String grpDimsQ = "select * from prg_grp_dimensions where grp_id=" + grpId;
        PbReturnObject grpDims = execSelectSQL(grpDimsQ);
        PbReturnObject conDims = execSelectSQL(allConnDimQ);
        dimObj.setObject("conDims", conDims);
        dimObj.setObject("grpDims", grpDims);

        return dimObj;
    }

    public String timebasedFormula(ArrayList selvalueslist, String bustableid, ArrayList columnalias, String buscolid, String colName, String tablename) {
        String status = "failure";
        PbDb pbdb = new PbDb();
        PbReturnObject pbretobj = new PbReturnObject();
        try {
            ArrayList queryieslist = new ArrayList();
            String columnvalue = null;
            String buildquery = null;
            String dispcolname = null;
            String timqry = getResBundle().getString("insrtingbussrcdtails");
            String timenxtqry = getResBundle().getString("insrtingprgbstbldtls");
            String column_disply_desc = getResBundle().getString("getcolumndisplaydes");
            Object[] columndisply = new Object[1];
            columndisply[0] = buscolid;
            String buildquer = pbdb.buildQuery(column_disply_desc, columndisply);
            pbretobj = pbdb.execSelectSQL(buildquer);
            String columndisplydesc = pbretobj.getFieldValueString(0, 13);
            Object[] timebasedobj = new Object[3];
            Object[] timenxtobj = new Object[7];
            Object[] getcolobj = new Object[1];

            for (int i = 0; i < selvalueslist.size(); i++) {
                timebasedobj[0] = bustableid;
                timebasedobj[1] = bustableid;
                columnvalue = columnalias.get(i).toString();//selvalueslist.get(i).toString().substring(0,(selvalueslist.get(i).toString().indexOf("(")));
                columnvalue.replace("-", " ");
                timebasedobj[2] = columnalias.get(i) + "(" + colName + ")";
                buildquery = pbdb.buildQuery(timqry, timebasedobj);
                queryieslist.add(buildquery);
                dispcolname = columnvalue.replace("-", " ");
                String timebased = null;
                String columndesc = null;
                String timecal = null;
                String timecals = null;
                String st = null;
                String timecase = null;
                String timest = null;

                int x = 0;
                if (dispcolname.substring(0, 5).equals("count")) {
                    columndesc = dispcolname.substring(0, 5);
                    timebased = dispcolname.substring(5);
                } else {
                    columndesc = dispcolname.substring(0, 4);
                    timebased = dispcolname.substring(4);
                }
                int len = timebased.length();
                if (timebased.endsWith("s")) {
                    x = timebased.lastIndexOf("days");
                    timecal = timebased.substring(0, x);
                    timecals = timebased.substring(x);
                    st = timecals.substring(0, 1);
                    timecase = st.toUpperCase() + "" + timecals.substring(1);
                    timest = timecal + " " + timecase;
                } else if (timebased.endsWith("r")) {
                    x = timebased.lastIndexOf("year");
                    timecal = timebased.substring(0, x);
                    timecals = timebased.substring(x);
                    st = timecals.substring(0, 1);
                    timecase = st.toUpperCase() + "" + timecals.substring(1);
                    timest = timecal + " " + timecase;
                } else if (timebased.endsWith("h")) {
                    x = timebased.lastIndexOf("month");
                    timecal = timebased.substring(0, x);
                    timecals = timebased.substring(x);
                    st = timecals.substring(0, 1);
                    timecase = st.toUpperCase() + "" + timecals.substring(1);
                    if (timecal.trim().equals("1")) {
                        timest = timecal + " " + timecase;
                    } else {
                        timest = timecal + " " + timecase + "s";
                    }
                } else if (timebased.startsWith("m") || timebased.startsWith("y") || timebased.startsWith("q") || timebased.startsWith("l") || timebased.startsWith("w")) {
                    timest = timebased.substring(0, 3);
                } else if (timebased.startsWith("p") && len == 4) {
                    timest = timebased.substring(0, 4);
                } else {
                    timest = timebased.substring(0, 5);
                }





                String column_display_desc = null;
                if (columndesc.trim().equals("sum")) {
                    column_display_desc = columndisplydesc.concat("(" + timest + ")");
                } else {
                    column_display_desc = columndesc + " " + columndisplydesc.concat("(" + timest + ")");
                }
                timenxtobj[0] = bustableid;
                timenxtobj[1] = (columnalias.get(i).toString().replace("-", "_")) + "(" + colName + ")";//This column value is same as columnname in database.
                timenxtobj[2] = tablename + "." + colName;//selvalueslist.get(i);
                timenxtobj[3] = columnvalue.replace("-", "#");//columnvalue.substring(0,columnvalue.indexOf("-"));//Here column value is used for column display name
                String dispcolnameqry = getResBundle().getString("getdisplayname");
                getcolobj[0] = buscolid;
                String buildqry = pbdb.buildQuery(dispcolnameqry, getcolobj);
                pbretobj = pbdb.execSelectSQL(buildqry);
                String dicolname = pbretobj.getFieldValueString(0, "COLUMN_DISP_NAME");
                timenxtobj[4] = column_display_desc;
                timenxtobj[5] = buscolid;
                timenxtobj[6] = buscolid;
                buildquery = pbdb.buildQuery(timenxtqry, timenxtobj);
                queryieslist.add(buildquery);
            }

            pbdb.executeMultiple(queryieslist);
            status = "success";
        }
        catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }  catch (Exception e) {
            logger.error("Exception: ", e);
        }       return status;
        }

    public ArrayList getTableid(String buscolid) {

        PbReturnObject returnObject = null;
        ArrayList<String> tablelist = new ArrayList<String>();
        try {
            String busstableid = getResBundle().getString("gettableid");
            returnObject = new PbReturnObject();
            PbDb pbDb = new PbDb();
            Object ojb[] = new Object[1];
            ojb[0] = buscolid;
            String finalQuery = buildQuery(busstableid, ojb);
            returnObject = pbDb.execSelectSQL(finalQuery);

            for (int i = 0; i < returnObject.getRowCount(); i++) {
                tablelist.add(returnObject.getFieldValueString(i, 1));
                tablelist.add(returnObject.getFieldValueString(i, 2));
            }

        }
        catch (SQLException ex) {
             logger.error("Exception: ", ex);
        }  catch (Exception e) {
        }       return tablelist;
    }

    public String getAllFields(String tabId) {
        String jsonString = null;
        PbReturnObject returnObject = null;
        try {
            String Query = getResBundle().getString("getDetails");
            returnObject = new PbReturnObject();
            PbDb pbDb = new PbDb();
            Object ojb[] = new Object[1];
            ojb[0] = tabId;

            String finalQuery = buildQuery(Query, ojb);
            returnObject = pbDb.execSelectSQL(finalQuery);
//            ArrayList<String> displayNameIds=new ArrayList<String>();

            HashMap defaultaggr = new HashMap();
            for (int i = 0; i < returnObject.getRowCount(); i++) {
                ArrayList<String> agr = new ArrayList<String>();
                agr.add(returnObject.getFieldValueString(i, 14));

                if ((returnObject.getFieldValueString(i, 5)).equals("Y")) {
                    agr.add("sum");
                } else {
                    agr.add("");
                }
                if ((returnObject.getFieldValueString(i, 2)).equals("Y")) {
                    agr.add("avg");
                } else {
                    agr.add("");
                }
                if ((returnObject.getFieldValueString(i, 3)).equals("Y")) {
                    agr.add("min");
                } else {
                    agr.add("");
                }
                if ((returnObject.getFieldValueString(i, 4)).equals("Y")) {
                    agr.add("max");
                } else {
                    agr.add("");
                }
                if ((returnObject.getFieldValueString(i, 6)).equals("Y")) {
                    agr.add("count");
                } else {
                    agr.add("");
                }
                if ((returnObject.getFieldValueString(i, 7)).equals("Y")) {
                    agr.add("COUNTDISTINCT");
                } else {
                    agr.add("");
                }
                if (returnObject.getFieldValueString(i, 15).equals("Y")) {
                    agr.add("roleflag");
                } else {
                    agr.add("");
                }
                agr.add(returnObject.getFieldValueString(i, 1));
                defaultaggr.put(returnObject.getFieldValueString(i, 0), agr);
            }
            Gson gson = new Gson();
            jsonString = gson.toJson(defaultaggr);

        } 
        catch (SQLException ex) {
             logger.error("Exception: ", ex);
        }catch (Exception e) {
            logger.error("Exception: ", e);
        }        return jsonString;
        }

    /////////////////////////////given by bharathi
    public HashMap getGroupRoleExtraFacts(String grpId) throws Exception {
        PbReturnObject all = new PbReturnObject();
        String grpRoleQ = "select * from prg_user_folder where grp_id=" + grpId;

        PbReturnObject grpRoleObj = execSelectSQL(grpRoleQ);
        HashMap details = new HashMap();
        HashMap allMap = new HashMap();
        for (int g = 0; g < grpRoleObj.getRowCount(); g++) {
            String folderId = grpRoleObj.getFieldValueString(g, 0);
            String extrafactsQuery = "";
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                extrafactsQuery = "select buss_table_id,buss_table_name from  prg_grp_buss_table where buss_table_id in("
                        + " select distinct buss_table_id from  prg_grp_buss_table where buss_table_id in"
                        + " (select buss_table_id from prg_grp_buss_table where grp_id=" + grpId + "  and buss_type!='Query'"
                        + " except SELECT DISTINCT TAB_ID FROM PRG_GRP_DIM_TABLES gdt,PRG_GRP_DIMENSIONS gd where"
                        + " gdt.dim_id= gd.dim_id and gd.grp_id=" + grpId + ") and buss_table_name!='Calculated Facts'"
                        + " except"
                        + " select buss_table_id from prg_user_sub_folder_tables"
                        + " where sub_folder_id=(select SUB_FOLDER_ID FROM PRG_USER_FOLDER_DETAIL where SUB_FOLDER_TYPE='Facts' and folder_id=" + folderId + ")"
                        + " )";
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                extrafactsQuery = "select buss_table_id,buss_table_name from  prg_grp_buss_table where buss_table_id in("
                        + " select distinct buss_table_id from  prg_grp_buss_table where buss_table_id in"
                        + " (select buss_table_id from prg_grp_buss_table where grp_id=" + grpId + "  and buss_type!='Query'"
                        + " not in (SELECT DISTINCT TAB_ID FROM PRG_GRP_DIM_TABLES gdt,PRG_GRP_DIMENSIONS gd where"
                        + " gdt.dim_id= gd.dim_id and gd.grp_id=" + grpId + ") and buss_table_name!='Calculated Facts'"
                        + " not in"
                        + " (select buss_table_id from prg_user_sub_folder_tables"
                        + " where sub_folder_id=(select SUB_FOLDER_ID FROM PRG_USER_FOLDER_DETAIL where SUB_FOLDER_TYPE='Facts' and folder_id='" + folderId + "')"
                        + " )))";
            } else {
                extrafactsQuery = "select buss_table_id,buss_table_name from  prg_grp_buss_table where buss_table_id in("
                        + " select distinct buss_table_id from  prg_grp_buss_table where buss_table_id in"
                        + " (select buss_table_id from prg_grp_buss_table where grp_id=" + grpId + "  and buss_type!='Query'"
                        + " minus SELECT DISTINCT TAB_ID FROM PRG_GRP_DIM_TABLES gdt,PRG_GRP_DIMENSIONS gd where"
                        + " gdt.dim_id= gd.dim_id and gd.grp_id=" + grpId + ") and buss_table_name!='Calculated Facts'"
                        + " Minus"
                        + " select buss_table_id from prg_user_sub_folder_tables"
                        + " where sub_folder_id=(select SUB_FOLDER_ID FROM PRG_USER_FOLDER_DETAIL where SUB_FOLDER_TYPE='Facts' and folder_id=" + folderId + ")"
                        + " )";
            }


            PbReturnObject pbro = execSelectSQL(extrafactsQuery);
            details.put(folderId, pbro);
        }
        all.setObject("grpRoleObj", grpRoleObj);
        allMap.put("Facts", details);
        return allMap;
    }

    public HashMap getGroupRoleExtraFactsCols(String grpId) throws Exception {
        PbReturnObject all = new PbReturnObject();
        PbReturnObject grpRoleObj = null;
        String grpRoleQ = "select * from prg_user_folder where grp_id=" + grpId;

        grpRoleObj = execSelectSQL(grpRoleQ);
        HashMap details = new HashMap();
        HashMap allFactsMap = new HashMap();
        HashMap extraFactsForRole = new HashMap();
        HashMap allMap = new HashMap();
        int count = 0;
        StringBuffer strbuf = new StringBuffer();
        String allFacts = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            allFacts = "select distinct BUSS_TABLE_ID,BUSS_TABLE_NAME from  prg_grp_buss_table where BUSS_TABLE_ID in"
                    + " (select BUSS_TABLE_ID from prg_grp_buss_table where grp_id=" + grpId + "  and buss_type!='Query'"
                    + " except SELECT DISTINCT TAB_ID FROM PRG_GRP_DIM_TABLES gdt,PRG_GRP_DIMENSIONS gd where "
                    + " gdt.dim_id= gd.dim_id and gd.grp_id=" + grpId + ") and BUSS_TABLE_NAME!='Calculated Facts' ";
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            allFacts = "select distinct BUSS_TABLE_ID,BUSS_TABLE_NAME from  prg_grp_buss_table where BUSS_TABLE_ID in"
                    + " (select BUSS_TABLE_ID from prg_grp_buss_table where grp_id=" + grpId + "  and buss_type!='Query' and BUSS_TABLE_ID "
                    + " not in (SELECT DISTINCT TAB_ID FROM PRG_GRP_DIM_TABLES gdt,PRG_GRP_DIMENSIONS gd where "
                    + " gdt.dim_id= gd.dim_id and gd.grp_id=" + grpId + ")) and BUSS_TABLE_NAME!='Calculated Facts' ";
        } else {
            allFacts = "select distinct buss_table_id,buss_table_name from  prg_grp_buss_table where buss_table_id in"
                    + " (select buss_table_id from prg_grp_buss_table where grp_id='" + grpId + "'  and buss_type!='Query'"
                    + " minus SELECT DISTINCT TAB_ID FROM PRG_GRP_DIM_TABLES gdt,PRG_GRP_DIMENSIONS gd where "
                    + " gdt.dim_id= gd.dim_id and gd.grp_id=" + grpId + ") and buss_table_name!='Calculated Facts' ";
        }

        //////////////////////////////.println(" allFacts --0 "+allFacts);
        PbReturnObject allFactspbro = execSelectSQL(allFacts);

        for (int g = 0; g < grpRoleObj.getRowCount(); g++) {
            ArrayList factList = new ArrayList();
            String folderId = grpRoleObj.getFieldValueString(g, 0);
            String extraFacts = "";
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                extraFacts = "select BUSS_TABLE_ID from prg_grp_buss_table where grp_id in(select grp_id "
                        + " from prg_user_folder where folder_id =" + folderId + " ) except select distinct buss_table_id from "
                        + " prg_user_sub_folder_tables where sub_folder_id in(select sub_folder_id from"
                        + " prg_user_folder_detail where folder_id =" + folderId + " and sub_folder_type in('Facts','Dimensions'))";
                //////////////////////////////.println(" extraFacts "+extraFacts);
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                extraFacts = "select BUSS_TABLE_ID from prg_grp_buss_table where grp_id in(select grp_id "
                        + " from prg_user_folder where folder_id ='" + folderId + "') and BUSS_TABLE_ID not in(select distinct buss_table_id from "
                        + " prg_user_sub_folder_tables where sub_folder_id in(select sub_folder_id from"
                        + " prg_user_folder_detail where folder_id ='" + folderId + "' and sub_folder_type in('Facts','Dimensions')))";
                //////////////////////////////.println(" extraFacts "+extraFacts);
            } else {
                extraFacts = "select BUSS_TABLE_ID from prg_grp_buss_table where grp_id in(select grp_id "
                        + " from prg_user_folder where folder_id =" + folderId + " ) minus select distinct buss_table_id from "
                        + " prg_user_sub_folder_tables where sub_folder_id in(select sub_folder_id from"
                        + " prg_user_folder_detail where folder_id =" + folderId + " and sub_folder_type in('Facts','Dimensions'))";
                //////////////////////////////.println(" extraFacts "+extraFacts);
            }

            PbReturnObject tabObj = execSelectSQL(extraFacts);
            for (int m = 0; m < tabObj.getRowCount(); m++) {
                factList.add(tabObj.getFieldValueString(m, 0));
            }
            extraFactsForRole.put(folderId, factList);

            for (int j = 0; j < allFactspbro.getRowCount(); j++) {
                String extracols = "";
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    extracols = "select distinct BUSS_COLUMN_ID,COLUMN_NAME from prg_grp_buss_table_details where buss_column_id in(select distinct buss_column_id from prg_grp_buss_table_details where buss_table_id in(" + allFactspbro.getFieldValueInt(j, 0) + "))"
                            + " except "
                            + " select distinct  buss_col_id,buss_col_name from prg_user_sub_folder_elements where buss_table_id in(" + allFactspbro.getFieldValueInt(j, 0) + ")"
                            + " and sub_folder_id=(select SUB_FOLDER_ID FROM PRG_USER_FOLDER_DETAIL where SUB_FOLDER_TYPE='Facts' and folder_id=" + folderId + ")";
                    //////////////////////////////.println(" extracols-=-= "+extracols);
                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    extracols = "select distinct BUSS_COLUMN_ID,COLUMN_NAME from prg_grp_buss_table_details where buss_column_id in(select distinct buss_column_id from prg_grp_buss_table_details where buss_table_id in(" + allFactspbro.getFieldValueInt(j, 0) + "))"
                            + " and (buss_column_id,column_name) not in"
                            + " (select distinct  buss_col_id,buss_col_name from prg_user_sub_folder_elements where buss_table_id in(" + allFactspbro.getFieldValueInt(j, 0) + ")"
                            + " and sub_folder_id in (select SUB_FOLDER_ID FROM PRG_USER_FOLDER_DETAIL where SUB_FOLDER_TYPE='Facts' and folder_id=" + folderId + "))";
                    //////////////////////////////.println(" extracols-=-= "+extracols);
                } else {
                    extracols = "select distinct buss_column_id,column_name from prg_grp_buss_table_details where buss_column_id in(select distinct buss_column_id from prg_grp_buss_table_details where buss_table_id in(" + allFactspbro.getFieldValueInt(j, 0) + "))"
                            + " Minus "
                            + " select distinct  buss_col_id,buss_col_name from prg_user_sub_folder_elements where buss_table_id in(" + allFactspbro.getFieldValueInt(j, 0) + ")"
                            + " and sub_folder_id=(select SUB_FOLDER_ID FROM PRG_USER_FOLDER_DETAIL where SUB_FOLDER_TYPE='Facts' and folder_id=" + folderId + ")";
                    //////////////////////////////.println(" extracols-=-= "+extracols);
                }

                PbReturnObject extracolspbro = execSelectSQL(extracols);
                details.put(folderId + "-" + allFactspbro.getFieldValueInt(j, 0), extracolspbro);

            }
            if (allFactspbro.getRowCount() > 0) {
                allFactsMap.put(folderId, allFactspbro);
            }
        }
        //////////////////////////////.println(" allFactsMap= "+allFactsMap);
        allMap.put("FactCols", details);
        allMap.put("Facts", allFactsMap);
        allMap.put("extraFactsForRole", extraFactsForRole);
        return allMap;
    }

    public HashMap getFactColumnsNames(String tabid, String groupId) {
        PbReturnObject retobj = new PbReturnObject();
        ArrayList<String> factcollist = new ArrayList<String>();
        ArrayList<String> factcolid = new ArrayList<String>();
        HashMap map = new HashMap();
//    String query="SELECT BUSS_COLUMN_ID, COLUMN_NAME, DB_COLUMN_ID,column_type,role_flag,REFFERED_ELEMENTS,display_formula FROM PRG_GRP_BUSS_TABLE_DETAILS where buss_table_id=&";
        String query = "SELECT BUSS_COLUMN_ID,COLUMN_NAME FROM PRG_GRP_BUSS_TABLE_DETAILS  "
                + "WHERE buss_table_id=& AND BUSS_COLUMN_ID NOT IN( select PARENT_ID from PRG_GRP_GROUP_HIERARCHY_DETAIL where GROUP_ID=&)  "
                + "And BUSS_COLUMN_ID NOT IN(select CHILD_ID from PRG_GRP_GROUP_HIERARCHY_DETAIL where GROUP_ID=& and CHILD_ID is not null)";
        Object obj[] = new Object[3];
        obj[0] = tabid;
        obj[1] = groupId;
        obj[2] = groupId;
        String finalquery = super.buildQuery(query, obj);

        try {
            retobj = super.execSelectSQL(finalquery);
            if (retobj != null) {
                for (int i = 0; i < retobj.getRowCount(); i++) {
                    factcolid.add(retobj.getFieldValueString(i, 0));
                    factcollist.add(retobj.getFieldValueString(i, 1));
                }
            }
            map.put("bussid", factcolid);
            map.put("bussname", factcollist);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return map;
    }
    //Started by Nazneen

    public HashMap getChildList(String level, String grp_id) {
        PbReturnObject retobj = new PbReturnObject();
        ArrayList<String> childId = new ArrayList<String>();

        HashMap map = new HashMap();
        String query = "";
        Object obj[] = new Object[2];
        obj[0] = level;
        obj[1] = grp_id;
        String finalquery = super.buildQuery(query, obj);
        try {
            retobj = super.execSelectSQL(finalquery);
            if (retobj != null) {
                for (int i = 0; i < retobj.getRowCount(); i++) {
                    childId.add(retobj.getFieldValueString(i, 0));
                }
            }
            map.put("childId", childId);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return map;
    }

    public HashMap getHierarchyColumnsNames(String groupTabId) {

        PbReturnObject retobj = new PbReturnObject();
        ArrayList<String> parentcollist = new ArrayList<String>();
        ArrayList<String> childcollist = new ArrayList<String>();
        HashMap map = new HashMap();
        String query = "SELECT PARENT_ID, CHILD_ID FROM PRG_GRP_GROUP_HIERARCHY_DETAIL where GROUP_ID='&'";
        Object obj[] = new Object[1];
        obj[0] = groupTabId;
        String finalquery = super.buildQuery(query, obj);

        try {
            retobj = super.execSelectSQL(finalquery);
            if (retobj != null) {
                for (int i = 0; i < retobj.getRowCount(); i++) {
                    parentcollist.add(retobj.getFieldValueString(i, 0));
                    childcollist.add(retobj.getFieldValueString(i, 1));
                }
            }
            map.put("parentcollist", parentcollist);
            map.put("childcollist", childcollist);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return map;
    }

//    public String saveGroupHierarchy(String busstabid,String perentmeasures,String childmeasures)
//    {
//        insertGroupHierarchyMasterValues(perentmeasures);
//        for(String chilId:childmeasures.split(","))
//        {
//           insertGroupHierarchyValues(perentmeasures,chilId);
//        }
//        return null;
//    }
    public String saveGroupHierarchy(String groupId, String busstabid, String perentmeasures, String childmeasures) {
        int grpId = 0;
        PbReturnObject returnObject = new PbReturnObject();
        Object obj[] = new Object[1];
        obj[0] = groupId;
        String query = "select GRP_ID from PRG_GRP_GROUP_HIERARCHY_MASTER where GROUP_ID='&'";
        String finalquery = super.buildQuery(query, obj);
        try {
            returnObject = super.execSelectSQL(finalquery);
            if (returnObject.getRowCount() > 0) {
                grpId = returnObject.getFieldValueInt(0, 0);

            }
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        for (String parentid : perentmeasures.split(",")) {
            String lev = null;
            int level = 1;
            returnObject = new PbReturnObject();
            Object obj1[] = new Object[2];
            obj1[0] = groupId;
            obj1[1] = parentid;
            query = "select level_no from PRG_GRP_GROUP_HIERARCHY_DETAIL where GROUP_ID='&' AND CHILD_ID=&";
            finalquery = super.buildQuery(query, obj1);
            try {
                returnObject = super.execSelectSQL(finalquery);
                if (returnObject.getRowCount() > 0) {
                    level = returnObject.getFieldValueInt(0, 0);
                    level++;
                }
            } catch (SQLException ex) {
                logger.error("Exception: ", ex);
            }
            for (String chilId : childmeasures.split(",")) {
                insertGroupHierarchyValues(groupId, parentid, chilId, level, grpId);
            }
        }
        return null;
    }

    public String saveChildGroupHierarchy(String childMeasures, String tabId, String parentId, String groupId) {
        int grpId = 0;
        PbReturnObject returnObject = new PbReturnObject();
        Object obj[] = new Object[1];
        obj[0] = groupId;
        String query = "select GRP_ID from PRG_GRP_GROUP_HIERARCHY_MASTER where GROUP_ID='&'";
        String finalquery = super.buildQuery(query, obj);
        try {
            returnObject = super.execSelectSQL(finalquery);
            if (returnObject.getRowCount() > 0) {
                grpId = returnObject.getFieldValueInt(0, 0);

            }
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }

        String lev = null;
        int level = 1;
        returnObject = new PbReturnObject();
        Object obj1[] = new Object[2];
        obj1[0] = groupId;
        obj1[1] = parentId;
        query = "select level_no from PRG_GRP_GROUP_HIERARCHY_DETAIL where GROUP_ID='&' AND CHILD_ID=&";
        finalquery = super.buildQuery(query, obj1);
        try {
            returnObject = super.execSelectSQL(finalquery);
            if (returnObject.getRowCount() > 0) {
                level = returnObject.getFieldValueInt(0, 0);
                level++;
            }
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        for (String chilId : childMeasures.split(",")) {
            insertGroupHierarchyValues(groupId, parentId, chilId, level, grpId);
        }

        return null;
    }

    public String getBussColumnName(String busstabid, String perentid) {
        String busscolname = null;
        PbReturnObject returnObject = new PbReturnObject();
        Object obj[] = new Object[2];
        obj[0] = perentid;
        obj[1] = busstabid;
        String query = "select COLUMN_NAME from PRG_GRP_BUSS_TABLE_DETAILS where BUSS_COLUMN_ID='&' AND buss_table_id=&";
        String finalquery = super.buildQuery(query, obj);
        try {
            returnObject = super.execSelectSQL(finalquery);
            busscolname = returnObject.getFieldValueString(0, 0);

        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return busscolname;
    }
//public void insertGroupHierarchyMasterValues(String perentid)
//{
//    ArrayList perentidlist=getPerentHierarchyId();
//   for(int i=0;i<perentidlist.size();i++)
//   {
//      String perentcolid=(String)perentidlist.get(i);
//      if(!perentcolid.equals(perentid))
//      {
//         String sql="insert into PRG_GRP_HIERARCHY_MASTER_TABLE values(PRG_GRP_HIERARCHY_MASTER_SEQ.nextval,&)";
//          Object obj[]=new Object[1];
//          obj[0]=perentid;
//           String finalquery=super.buildQuery(sql, obj);
//                try {
//           int insert = super.execUpdateSQL(finalquery);
//                } catch (Exception ex) {
//                    logger.error("Exception: ",ex);
//                }
//      }
//   }
//   
//    
//      
// }

//
//  public void insertGroupHierarchyValues(String perentbusscolid,String childbussid)
//  {
//
//      String query="insert into PRG_GRP_HIERARCHY_TABLE (HIERARCHY_ID,PERENT_BUSS_COL_ID,CHILD_BUSS_COL_ID,HIERARCHY_NAME) values(PRG_GRP_HIERARCHY_SEQ.nextval,'&','&','H_' || CAST((PRG_GRP_HIERARCHY_SEQ.CURRVAL) AS VARCHAR(256)))";
//      Object obj[]=new Object[2];
//      obj[0]=perentbusscolid;
//      obj[1]=childbussid;
//      String finalquery=super.buildQuery(query, obj);
//      
//        try {
//            int insert = super.execUpdateSQL(finalquery);
//        } catch (Exception ex) {
//            logger.error("Exception: ",ex);
//        }
//  }
    public void insertGroupHierarchyValues(String groupId, String perentbusscolid, String childbussid, int level, int grp_id) {
        String query = "";
        String finalquery = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            query = "insert into PRG_GRP_GROUP_HIERARCHY_DETAIL(GROUP_ID,GRP_ID,PARENT_ID,CHILD_ID,level_no)values('&',&,'&','&',&)";
        } else {
            query = "insert into PRG_GRP_GROUP_HIERARCHY_DETAIL(GROUP_DET_ID,GROUP_ID,GRP_ID,PARENT_ID,CHILD_ID,level_no)values(PRG_GRP_GROUP_DETAIL_SEQ.nextval,'&',&,'&','&',&)";
        }
        Object obj[] = new Object[5];
        obj[0] = groupId;
        obj[1] = grp_id;
        obj[2] = perentbusscolid;
        obj[3] = childbussid;
        obj[4] = level;
        finalquery = super.buildQuery(query, obj);


        try {
            int insert = super.execUpdateSQL(finalquery);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

//  public String getGroupHierarchy(String hierid) throws Exception
//  {
//     String str=null;
//     StringBuffer sb=new StringBuffer();
////     sb.append("<ul><li class='closed'><span class='folder' id=16389><font size='1px' face='verdana'> &nbsp;AVG_ORDER_PRICE </font></span><ul><li class='last'><span class='parentGrpMenu' id=16397>&nbsp;AVG_RETURN_PRICE</span><ul><li class='last'><span class='parentGrpMenu' id=16405>&nbsp;AVG_PRICE</span></li></ul></li></ul></li></ul><ul><li class='closed'><span class='folder' id=16397><font size='1px' face='verdana'>&nbsp;AVG_RETURN_PRICE</font></span><ul><li class='last'><span class='parentGrpMenu' id=16409>&nbsp;RETURN_VALUE    </span><ul><li class='last'><span class='parentGrpMenu' id=16523>&nbsp;AVG_RETURN_UNIT </span></li></ul></li></ul></li></ul>");
//     PbReturnObject returnObject=new PbReturnObject();
//     ArrayList list=new ArrayList();
//     ArrayList alist=new ArrayList();
//     ArrayList<String> chkList=new ArrayList<String>();
//     String sql="select PERENT_BUSS_COL_ID from PRG_GRP_HIERARCHY_MASTER_TABLE";
//     returnObject=super.execSelectSQL(sql);
//
//     for(int i=0;i<returnObject.getRowCount();i++)
//       {
//         list.add(returnObject.getFieldValueString(i,0));
//       }
//      for(int i=0;i<list.size();i++)
//      {
//          String perentid=(String)list.get(i);
//          String perentbussname=getPerentBussName(perentid);
//          String busstableid=getBussTableId(perentid);
//            sb.append("<ul id='grouphierarchy'>");
//            sb.append("<li class='folder' id='"+perentid+","+busstableid+"'><span class='grouphierarchyid' id="+perentid+"><font size='1px' face='verdana'>&nbsp;"+perentbussname+"</font></span>");
//          ArrayList childlist=getChildColId(perentid);
//          for(int j=0;j<childlist.size();j++)
//          {
//            String childid=(String)childlist.get(j);
//            String childbussname=getChildBussName(childid);
//            sb.append("<ul><li class='last'><span class='open' id="+childid+">&nbsp;"+childbussname+"</span>");
//          }
//          for(int k=0;k<childlist.size();k++)
//          {
//         sb.append("</li>");
//         sb.append("</ul>");
//          }
//         sb.append("</li>");
//         sb.append("</ul>");
//      }
//     String stre=sb.toString();
//      return stre;
//  }
//  public ArrayList getChildColId(String perentid)
//  {
//      ArrayList<String> childlist=new ArrayList<String>();
//      PbReturnObject returnObject=new PbReturnObject();
//      String qry="select CHILD_BUSS_COL_ID from PRG_GRP_HIERARCHY_TABLE where PERENT_BUSS_COL_ID=&";
//      Object obj[]=new Object[1];
//      obj[0]=perentid;
//      String finalquery=super.buildQuery(qry, obj);
//        try {
//            returnObject = super.execSelectSQL(finalquery);
//            for(int i=0;i<returnObject.getRowCount();i++)
//            {
//                childlist.add(returnObject.getFieldValueString(i,0));
//            }
//
//        } catch (SQLException ex) {
//            logger.error("Exception: ",ex);
//        }
//      return childlist;
//  }
//  public String getChildBussName(String childid)
//  {
//       PbReturnObject returnObject=new PbReturnObject();
//       String qry="select COLUMN_NAME from PRG_GRP_BUSS_TABLE_DETAILS where BUSS_COLUMN_ID=&";
//       Object obj[]=new Object[1];
//       obj[0]=childid;
//       String finalquery=super.buildQuery(qry, obj);
//       String childbussname=null;
//        try {
//            returnObject = super.execSelectSQL(finalquery);
//         childbussname=returnObject.getFieldValueString(0,0);
//
//        } catch (SQLException ex) {
//            logger.error("Exception: ",ex);
//        }
//      return childbussname;
//  }
//  public String getPerentBussName(String perentId)
//  {
//       PbReturnObject returnObject=new PbReturnObject();
//       String qry="select COLUMN_NAME from PRG_GRP_BUSS_TABLE_DETAILS where BUSS_COLUMN_ID=&";
//       Object obj[]=new Object[1];
//       obj[0]=perentId;
//       String finalquery=super.buildQuery(qry, obj);
//       String perentbussname=null;
//        try {
//            returnObject = super.execSelectSQL(finalquery);
//          perentbussname=returnObject.getFieldValueString(0,0);
//        } catch (SQLException ex) {
//            logger.error("Exception: ",ex);
//        }
//      return perentbussname;
//  }
//  public ArrayList getPerentHierarchyId()
//  {
//       PbReturnObject returnObject=new PbReturnObject();
//       ArrayList list=new ArrayList();
//        String sql="select PERENT_BUSS_COL_ID from PRG_GRP_HIERARCHY_MASTER_TABLE";
//        try {
//            returnObject = super.execSelectSQL(sql);
//            for(int i=0;i<returnObject.getRowCount();i++)
//            {
//                list.add(returnObject.getFieldValueString(i,0));
//            }
//
//
//        } catch (SQLException ex) {
//            logger.error("Exception: ",ex);
//        }
//      return list;
//  }
    public String getBussTableId(String busscolid) {
        PbReturnObject returnObject = new PbReturnObject();
        String qry = "select BUSS_TABLE_ID from PRG_GRP_BUSS_TABLE_DETAILS where BUSS_COLUMN_ID=&";
        Object obj[] = new Object[1];
        obj[0] = busscolid;
        String finalquery = super.buildQuery(qry, obj);
        String busstableid = null;
        try {
            returnObject = super.execSelectSQL(finalquery);
            for (int i = 0; i < returnObject.getRowCount(); i++) {
                busstableid = returnObject.getFieldValueString(i, 0);
            }
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }

        return busstableid;
    }
    //Group Hierarchy code started

    public String getFolderidDetails(String groupid) {
        PbReturnObject returnObject = new PbReturnObject();
        String query = "select FOLDER_ID from PRG_USER_FOLDER where GRP_ID=&";
        Object obj[] = new Object[1];
        obj[0] = groupid;
        String finalquery = super.buildQuery(query, obj);
        try {
            returnObject = super.execSelectSQL(finalquery);
            String folderid = returnObject.getFieldValueString(0, 0);
            HashMap map = getPerentAndChildDetails(folderid);
            LinkedList childelementids = (LinkedList) map.get("childelementids");
            LinkedList childbusscolids = (LinkedList) map.get("childbusscolids");
            LinkedList perentelementids = (LinkedList) map.get("perentelementids");
            LinkedList perentbusscolids = (LinkedList) map.get("perentbusscolids");
            LinkedList groupids = (LinkedList) map.get("groupids");
            LinkedList groupname = (LinkedList) map.get("groupname");
            LinkedList folderids = (LinkedList) map.get("folderids");
            LinkedList levelno = (LinkedList) map.get("levelno");

            query = "delete from PRG_USER_ELEMENTS_HIERARCHY where FOLDER_ID=" + folderid;
            try {
                super.execUpdateSQL(query);
            } catch (Exception ex) {
                logger.error("Exception: ", ex);
            }

            for (int i = 0; i < childelementids.size(); i++) {
                String childelementid = (String) childelementids.get(i);
                String childbusscolid = (String) childbusscolids.get(i);
                String perentelementid = (String) perentelementids.get(i);
                String perentbusscolid = (String) perentbusscolids.get(i);
                String groupids1 = (String) groupids.get(i);
                String groupname1 = (String) groupname.get(i);
                String folderid1 = (String) folderids.get(i);
                String levelnos = (String) levelno.get(i);
                insertHierarechyValues(childelementid, childbusscolid, perentelementid, perentbusscolid, groupids1, groupname1, folderid1, levelnos);
            }
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return null;
    }

    public LinkedHashMap getPerentAndChildDetails(String folderid) {
        PbReturnObject returnObject = new PbReturnObject();
        String sqlquery = "";

//     String sqlquery="SELECT a1.element_id child_element_id,p1.child_buss_col_id,a2.element_id parent_element_id,p1.perent_buss_col_id,p1.hierarchy_id,p1.hierarchy_name,d1.folder_id FROM PRG_USER_SUB_FOLDER_ELEMENTS a1,PRG_USER_SUB_FOLDER_ELEMENTS a2,PRG_GRP_HIERARCHY_TABLE p1,prg_user_folder_detail d1 where a1.BUSS_COL_ID || '' =p1.CHILD_BUSS_COL_ID and a2.BUSS_COL_ID || ''=p1.PERENT_BUSS_COL_ID and a1.REF_ELEMENT_TYPE =1 and a2.REF_ELEMENT_TYPE =1 and a1.sub_folder_id =d1.sub_folder_id and d1.folder_id =& order by 4,2";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            sqlquery = "SELECT a1.element_id child_element_id,p1.CHILD_ID,a2.element_id parent_element_id,p1.PARENT_ID,p1.GROUP_ID,p2.GROUP_NAME,"
                    + "d1.folder_id,p1.LEVEL_NO FROM PRG_USER_SUB_FOLDER_ELEMENTS a1,PRG_USER_SUB_FOLDER_ELEMENTS a2,PRG_GRP_GROUP_HIERARCHY_DETAIL p1,"
                    + "PRG_GRP_GROUP_HIERARCHY_MASTER p2,prg_user_folder_detail d1,prg_user_folder_detail d2 "
                    + "where a1.BUSS_COL_ID  =p1.CHILD_ID and a2.BUSS_COL_ID =p1.PARENT_ID "
                    + "and a1.REF_ELEMENT_TYPE =1 and a2.REF_ELEMENT_TYPE =1 "
                    + "and a1.sub_folder_id =d1.sub_folder_id and a2.sub_folder_id    =d2.sub_folder_id "
                    + "and d1.folder_id =& and d2.folder_id =& "
                    + "and p2.GROUP_ID=p1.GROUP_ID order by 4,2 ";
        } else {
            sqlquery = "SELECT a1.element_id child_element_id,p1.CHILD_ID,a2.element_id parent_element_id,p1.PARENT_ID,p1.GROUP_ID,p2.GROUP_NAME,"
                    + "d1.folder_id,p1.LEVEL_NO FROM PRG_USER_SUB_FOLDER_ELEMENTS a1,PRG_USER_SUB_FOLDER_ELEMENTS a2,PRG_GRP_GROUP_HIERARCHY_DETAIL p1,"
                    + "PRG_GRP_GROUP_HIERARCHY_MASTER p2,prg_user_folder_detail d1 ,prg_user_folder_detail d2 "
                    + "where a1.BUSS_COL_ID || '' =p1.CHILD_ID and a2.BUSS_COL_ID || ''=p1.PARENT_ID "
                    + "and a1.REF_ELEMENT_TYPE =1 and a2.REF_ELEMENT_TYPE =1 "
                    + "and a1.sub_folder_id =d1.sub_folder_id and  a2.sub_folder_id    =d2.sub_folder_id "
                    + "and d1.folder_id =& and d2.folder_id =& "
                    + "and p2.GROUP_ID=p1.GROUP_ID order by 4,2 ";
        }
        Object obj[] = new Object[2];
        obj[0] = folderid;
        obj[1] = folderid;
        String finalquery = super.buildQuery(sqlquery, obj);
        LinkedList<String> childelementids = new LinkedList<String>();
        LinkedList<String> childbusscolids = new LinkedList<String>();
        LinkedList<String> perentelementids = new LinkedList<String>();
        LinkedList<String> perentbusscolids = new LinkedList<String>();
        LinkedList<String> groupids = new LinkedList<String>();
        LinkedList<String> groupname = new LinkedList<String>();
        LinkedList<String> folderids = new LinkedList<String>();
        LinkedList<String> levelno = new LinkedList<String>();
        LinkedHashMap allMap = new LinkedHashMap();
        try {
            returnObject = super.execSelectSQL(finalquery);
            if (returnObject.getRowCount() != 0 && returnObject != null) {
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    childelementids.add(returnObject.getFieldValueString(i, 0));
                    childbusscolids.add(returnObject.getFieldValueString(i, 1));
                    perentelementids.add(returnObject.getFieldValueString(i, 2));
                    perentbusscolids.add(returnObject.getFieldValueString(i, 3));
                    groupids.add(returnObject.getFieldValueString(i, 4));
                    groupname.add(returnObject.getFieldValueString(i, 5));
                    folderids.add(returnObject.getFieldValueString(i, 6));
                    levelno.add(returnObject.getFieldValueString(i, 7));
                }
            }
            allMap.put("childelementids", childelementids);
            allMap.put("childbusscolids", childbusscolids);
            allMap.put("perentelementids", perentelementids);
            allMap.put("perentbusscolids", perentbusscolids);
            allMap.put("groupids", groupids);
            allMap.put("groupname", groupname);
            allMap.put("folderids", folderids);
            allMap.put("levelno", levelno);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return allMap;
    }

    public void insertHierarechyValues(String childelementid, String childbusscolid, String perentelementid, String perentbusscolid, String groupids1, String groupname1, String folderid1, String levelnos) {
        String finaquery = "";
        String query = "";



        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            query = "INSERT INTO PRG_USER_ELEMENTS_HIERARCHY(CHILD_ELEMENT_ID,CHILD_ID,PERENT_ELEMENT_ID,PERENT_ID,GROUP_ID,GROUP_NAME,FOLDER_ID,LEVEL_NO) VALUES(&,&,&,&,&,'&',&,&)";
        } else {
            query = "INSERT INTO PRG_USER_ELEMENTS_HIERARCHY(ELEMENT_HIERARCHY_ID,CHILD_ELEMENT_ID,CHILD_ID,PERENT_ELEMENT_ID,PERENT_ID,GROUP_ID,GROUP_NAME,FOLDER_ID,LEVEL_NO) VALUES(PRG_USER_ELEMENTS_HIERA_SEQ.nextval,&,&,&,&,&,'&',&,&)";
        }
        Object obj[] = new Object[8];
        obj[0] = childelementid;
        obj[1] = childbusscolid;
        obj[2] = perentelementid;
        obj[3] = perentbusscolid;
        obj[4] = groupids1;
        obj[5] = groupname1;
        obj[6] = folderid1;
        obj[7] = levelnos;
        finaquery = super.buildQuery(query, obj);
        try {
            int insert = super.execUpdateSQL(finaquery);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }
    //started by Nazneen

    public int saveHierarchyGroups(String tabId, String GroupName, String GroupId) {
        int insert = 0;
        String finaquery = "";
        String query = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            query = "INSERT INTO PRG_GRP_GROUP_HIERARCHY_MASTER(GROUP_NAME,TABLE_ID,GRP_ID)VALUES('&','&','&')";
        } else {
            query = "INSERT INTO PRG_GRP_GROUP_HIERARCHY_MASTER(GROUP_ID,GROUP_NAME,TABLE_ID,GRP_ID)VALUES(PRG_GRP_GROUP_MASTER_SEQ.nextval,'&','&','&')";
        }
        Object obj[] = new Object[3];
        obj[0] = GroupName;
        obj[1] = tabId;
        obj[2] = GroupId;
        finaquery = super.buildQuery(query, obj);
        try {
            insert = super.execUpdateSQL(finaquery);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return insert;
    }

    public String getGroups(String hierid) throws Exception {
        String str = null;
        StringBuffer sb = new StringBuffer();
        PbReturnObject returnObject = new PbReturnObject();
        ArrayList list = new ArrayList();
        ArrayList alist = new ArrayList();
        ArrayList<String> chkList = new ArrayList<String>();
        ArrayList groupIds = getGroupIds(hierid);

        for (int i = 0; i < groupIds.size(); i++) {
            String groupid = (String) groupIds.get(i);
            String groupName = getGroupName(groupid);

            String tabId = getTabID(groupid);
            int level = 1;

            sb.append("<ul id='grouphierarchy'>");
            sb.append("<li class='expandable' id='" + groupid + "'><span style='font-family:verdana;font-size:12px' class='grouphierarchyid' id=" + groupid + "><font size='1px' face='verdana'><img src='images/treeViewImages/bullet_star_1.png'>&nbsp;" + groupName + "</font></span>");
            ArrayList parentlist = getGroupParentId(groupid, level);

            for (int j = 0; j < parentlist.size(); j++) {
                String parentid = (String) parentlist.get(j);

                ArrayList hasChild = gethasChild(groupid, parentid);
                String grpparentname = getGroupParentName(parentid);
                if (!hasChild.contains("")) {
                    sb.append("<ul id='grouphierarchyid1'>");
                    sb.append("<li class='expandable' id='" + parentid + ":" + groupid + ":" + level + "' onclick=getChildId(this.id)><span style='font-family:verdana;font-size:12px' class='grouphierarchyid1' id='" + parentid + ":" + groupid + ":" + tabId + "'>&nbsp;" + grpparentname + "</span>");
                    sb.append("<ul id='" + parentid + "" + groupid + "'>");
                    sb.append("</ul>");
                } else {
                    sb.append("<ul id='grouphierarchyid1'>");
                    sb.append("<li class='last' id='" + parentid + ":" + groupid + ":" + level + "'><span style='font-family:verdana;font-size:12px' class='grouphierarchyid1' id='" + parentid + ":" + groupid + ":" + tabId + "'>&nbsp;" + grpparentname + "</span>");
                }
                sb.append("</li>");
                sb.append("</ul>");
            }
            sb.append("</li>");
            sb.append("</ul>");
        }
        String stre = sb.toString();
        return stre;
    }

    public String getChildsId(String id) {
        StringBuffer sb1 = new StringBuffer();
        String[] ids = id.split(":");
        String parentId = ids[0];
        String groupId = ids[1];
        int level = Integer.parseInt(ids[2]);

        String tabId = getTabID(groupId);
        ArrayList childlist = getGroupChildId(parentId, level, groupId);
        level++;
        ArrayList parentlist = getGroupParentId(groupId, level);
        for (int k = 0; k < childlist.size(); k++) {
            String childid = (String) childlist.get(k);
            String grpchildname = getGroupChildName(childid);
            if (parentlist.contains(childid)) {
                sb1.append("<ul id='grouphierarchyid1'>");
                sb1.append("<li class='expandable' id='" + childid + ":" + groupId + ":" + level + "' onclick=getChildId(this.id)><span style='font-family:verdana;font-size:12px' class='grouphierarchyid1' id='" + childid + ":" + groupId + ":" + tabId + "'>&nbsp;" + grpchildname + "</span>");
                sb1.append("<ul id='" + childid + "" + groupId + "'>");
                sb1.append("</ul>");
            } else {
                sb1.append("<ul id='grouphierarchyid1'>");
                sb1.append("<li class='last' id='" + childid + ":" + groupId + ":" + level + "'><span style='font-family:verdana;font-size:12px' class='grouphierarchyid1' id='" + childid + ":" + groupId + ":" + tabId + "'>&nbsp;" + grpchildname + "</span>");
            }
            sb1.append("</li>");
            sb1.append("</ul>");
        }
        String stre = sb1.toString();
        return stre;
    }

    public ArrayList getGroupIds(String grpId) {
        PbReturnObject returnObject = new PbReturnObject();
        ArrayList list = new ArrayList();
        String sql = "select GROUP_ID from PRG_GRP_GROUP_HIERARCHY_MASTER where GRP_ID=&";
        Object obj[] = new Object[1];
        obj[0] = grpId;

        String finalquery = super.buildQuery(sql, obj);
        try {
            returnObject = super.execSelectSQL(finalquery);
            for (int i = 0; i < returnObject.getRowCount(); i++) {
                list.add(returnObject.getFieldValueString(i, 0));
            }
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return list;
    }

    public String getGroupName(String groupid) {
        PbReturnObject returnObject = new PbReturnObject();
        String qry = "select GROUP_NAME from PRG_GRP_GROUP_HIERARCHY_MASTER where GROUP_ID=&";
        Object obj[] = new Object[1];
        obj[0] = groupid;
        String finalquery = super.buildQuery(qry, obj);
        String perentbussname = null;
        try {
            returnObject = super.execSelectSQL(finalquery);
            perentbussname = returnObject.getFieldValueString(0, 0);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return perentbussname;
    }

    public String getTabID(String groupid) {
        PbReturnObject returnObject = new PbReturnObject();
        String qry = "select TABLE_ID from PRG_GRP_GROUP_HIERARCHY_MASTER where GROUP_ID=&";
        Object obj[] = new Object[1];
        obj[0] = groupid;
        String finalquery = super.buildQuery(qry, obj);
        String tabId = null;
        try {
            returnObject = super.execSelectSQL(finalquery);
            tabId = returnObject.getFieldValueString(0, 0);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return tabId;
    }

    public ArrayList getGroupParentId(String groupid, int level) {
        PbReturnObject returnObject = new PbReturnObject();
        ArrayList list = new ArrayList();
        String sql = "select distinct(PARENT_ID) from PRG_GRP_GROUP_HIERARCHY_DETAIL where GROUP_ID=& and LEVEL_NO=&";
        Object obj[] = new Object[2];
        obj[0] = groupid;
        obj[1] = level;
        String finalquery = super.buildQuery(sql, obj);
        try {
            returnObject = super.execSelectSQL(finalquery);
            for (int i = 0; i < returnObject.getRowCount(); i++) {
                list.add(returnObject.getFieldValueString(i, 0));
            }
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return list;
    }

    public ArrayList gethasChild(String groupid, String parentId) {
        PbReturnObject returnObject = new PbReturnObject();
        ArrayList list = new ArrayList();
        String sql = "select CHILD_ID from PRG_GRP_GROUP_HIERARCHY_DETAIL where PARENT_ID=& and GROUP_ID=&";
        Object obj[] = new Object[2];
        obj[0] = parentId;
        obj[1] = groupid;
        String finalquery = super.buildQuery(sql, obj);
        try {
            returnObject = super.execSelectSQL(finalquery);
            for (int i = 0; i < returnObject.getRowCount(); i++) {
                list.add(returnObject.getFieldValueString(i, 0));
            }
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return list;
    }

    public String getGroupParentName(String parentid) {
        PbReturnObject returnObject = new PbReturnObject();
        String qry = "select COLUMN_NAME from PRG_GRP_BUSS_TABLE_DETAILS where BUSS_COLUMN_ID=&";
        Object obj[] = new Object[1];
        obj[0] = parentid;
        String finalquery = super.buildQuery(qry, obj);
        String perentbussname = null;
        try {
            returnObject = super.execSelectSQL(finalquery);
            perentbussname = returnObject.getFieldValueString(0, 0);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return perentbussname;
    }

    public String getLevel(String parentid, String groupId) {
        PbReturnObject returnObject = new PbReturnObject();
        String qry = "select LEVEL_NO from PRG_GRP_GROUP_HIERARCHY_DETAIL where PARENT_ID=& AND GROUP_ID=&";
        Object obj[] = new Object[2];
        obj[0] = parentid;
        obj[1] = groupId;
        String finalquery = super.buildQuery(qry, obj);
        String level = null;
        try {
            returnObject = super.execSelectSQL(finalquery);
            level = returnObject.getFieldValueString(0, 0);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return level;
    }

    public ArrayList getGroupChildId(String parentid, int level, String groupId) {
        PbReturnObject returnObject = new PbReturnObject();
        ArrayList list = new ArrayList();
        String sql = "select CHILD_ID from PRG_GRP_GROUP_HIERARCHY_DETAIL where PARENT_ID=& and LEVEL_NO=& and GROUP_ID=& and child_id is not null";
        Object obj[] = new Object[3];
        obj[0] = parentid;
        obj[1] = level;
        obj[2] = groupId;
        String finalquery = super.buildQuery(sql, obj);
        try {
            returnObject = super.execSelectSQL(finalquery);
            for (int i = 0; i < returnObject.getRowCount(); i++) {
                list.add(returnObject.getFieldValueString(i, 0));
            }
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return list;
    }

    public String getGroupChildName(String childid) {
        PbReturnObject returnObject = new PbReturnObject();
        String qry = "select COLUMN_NAME from PRG_GRP_BUSS_TABLE_DETAILS where BUSS_COLUMN_ID=&";
        Object obj[] = new Object[1];
        obj[0] = childid;
        String finalquery = super.buildQuery(qry, obj);
        String childbussname = null;
        try {
            returnObject = super.execSelectSQL(finalquery);
            childbussname = returnObject.getFieldValueString(0, 0);

        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return childbussname;
    }

    public String getGroupsName(String grpid) {
        PbReturnObject returnObject = new PbReturnObject();
        String qry = "select GROUP_NAME from PRG_GRP_GROUP_HIERARCHY_MASTER where GROUP_ID=&";
        Object obj[] = new Object[1];
        obj[0] = grpid;
        String finalquery = super.buildQuery(qry, obj);
        String grpName = null;
        try {
            returnObject = super.execSelectSQL(finalquery);
            grpName = returnObject.getFieldValueString(0, 0);

        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return grpName;
    }

    public String getGroupTabId(String grpid) {
        PbReturnObject returnObject = new PbReturnObject();
        String qry = "select TABLE_ID from PRG_GRP_GROUP_HIERARCHY_MASTER where GROUP_ID=&";
        Object obj[] = new Object[1];
        obj[0] = grpid;
        String finalquery = super.buildQuery(qry, obj);
        String tabId = null;
        try {
            returnObject = super.execSelectSQL(finalquery);
            tabId = returnObject.getFieldValueString(0, 0);

        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return tabId;
    }
//end of group hierarchy code
//Ended by Nazneen 
}
