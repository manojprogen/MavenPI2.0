/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.metadata;

import com.google.common.base.Joiner;
import com.progen.userlayer.db.UserLayerDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 *
 * @author progen
 */
public class MetadataDAO extends PbDb {

    public static Logger logger = Logger.getLogger(MetadataDAO.class);
    ResourceBundle resourceBundle;
    List<Cube> cubesList = new ArrayList<Cube>();
    private int roleID;

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {

            resourceBundle = new MetadataResourceBundle();

        }

        return resourceBundle;
    }
    UserLayerDAO userLayerDAO = new UserLayerDAO();

    public List<Cube> getUserFolderList(String connectionId) {
        Object object[] = new Object[1];
        object[0] = connectionId;
        String getUserFolderList = getResourceBundle().getString("getUserFolderList");
        String finalQuery = super.buildQuery(getUserFolderList, object);
        PbReturnObject folderListObject = new PbReturnObject();
        try {
            folderListObject = super.execSelectSQL(finalQuery);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

        for (int i = 0; i < folderListObject.getRowCount(); i++) {

            Cube cube = (Cube) buildCube(folderListObject.getFieldValueInt(i, "FOLDER_ID"));
            cubesList.add(cube);
        }
        return cubesList;
    }
    //by sunita

    public List<Cube> getUserFolderList2(String connectionId, String folderId) {
        Object object[] = new Object[1];
        object[0] = connectionId;




        Cube cube = (Cube) buildCube(Integer.parseInt(folderId));
        cubesList.add(cube);

        return cubesList;
    }

    public String getUserFolderList1(String connectionId) {
        String Query = "SELECT f.FOLDER_ID, f.FOLDER_NAME, f.FOLDER_DESC, g.GRP_NAME,G.GRP_ID FROM PRG_USER_FOLDER  f, PRG_GRP_MASTER g where g.grp_id=f.grp_id and g.grp_id in(select grp_id from prg_grp_master where connection_id in (" + connectionId + "))  order by f.FOLDER_CREATED_ON, f.folder_updated_by";
        PbReturnObject pbro = new PbReturnObject();
        try {
            pbro = new PbDb().execSelectSQL(Query);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<option value='0000'>--All--</option>");
        for (int i = 0; i < pbro.getRowCount(); i++) {
            sb.append("<option value='" + pbro.getFieldValueInt(i, 0) + "'>");
            sb.append(pbro.getFieldValueString(i, 1));
            sb.append("</option>");
        }
        return sb.toString();
    }

    public Cube buildCube(int roleId) {
        roleID = roleId;
        Object object[] = new Object[1];
        String getUserSubFolderList = getResourceBundle().getString("getUserSubFolderList");
        object[0] = roleId;
        String finalQuery = super.buildQuery(getUserSubFolderList, object);
        PbReturnObject subFolderObject = new PbReturnObject();
        Cube cube = null;
        try {
            subFolderObject = super.execSelectSQL(finalQuery);
            cube = new Cube(subFolderObject.getFieldValueInt(0, "SUB_FOLDER_ID"), subFolderObject.getFieldValueString(0, "SUB_FOLDER_NAME"), roleId);
            buildDimension(subFolderObject.getFieldValueInt(0, "SUB_FOLDER_ID"), cube);

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return cube;
    }

    public void buildDimension(int subFolderID, Cube cube) {
        String getUserFolderDims = getResourceBundle().getString("getUserFolderDims");
        Object object[] = new Object[1];
        object[0] = subFolderID;
        PbReturnObject dimensionObject = new PbReturnObject();
        String finalQuery = super.buildQuery(getUserFolderDims, object);
        try {
            dimensionObject = super.execSelectSQL(finalQuery);
            for (int i = 0; i < dimensionObject.getRowCount(); i++) {
                Dimension dimension = new Dimension(dimensionObject.getFieldValueInt(i, "DIM_ID"), dimensionObject.getFieldValueString(i, "DIM_NAME"));
                cube.addDimension(dimension);
                buildDimensionMember(subFolderID, dimensionObject.getFieldValueInt(i, "DIM_ID"), dimension);
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

    }

    public void buildDimensionMember(int subFolderID, int dimensionID, Dimension dimension) {
        String getUserFolderDimMbrs = getResourceBundle().getString("getUserFolderDimMbrs");
        String getMemberDetails = getResourceBundle().getString("getMemberDetails");
        Object[] objects = new Object[2];
        objects[0] = subFolderID;
        objects[1] = dimensionID;
        String finalQuery = super.buildQuery(getUserFolderDimMbrs, objects);
        PbReturnObject memberObject = new PbReturnObject();
        Object memberDetails[] = new Object[3];
        try {
            memberObject = super.execSelectSQL(finalQuery);
            for (int i = 0; i < memberObject.getRowCount(); i++) {
                memberDetails[0] = roleID;
                memberDetails[1] = roleID;
                memberDetails[2] = memberObject.getFieldValueString(i, "MEMBER_ID");
                finalQuery = super.buildQuery(getMemberDetails, memberDetails);
                PbReturnObject memberDetailsObject = new PbReturnObject();
                memberDetailsObject = super.execSelectSQL(finalQuery);
                ArrayList<Integer> elementIds = new ArrayList<Integer>();
                int busTableId = 0;
                String busTableName = "";
                ArrayList<Integer> bussColIds = new ArrayList<Integer>();
                ArrayList<String> bussColNames = new ArrayList<String>();
                for (int j = 0; j < memberDetailsObject.getRowCount(); j++) {
                    elementIds.add(memberDetailsObject.getFieldValueInt(j, "KEY_ELEMENT_ID"));
                    busTableId = memberDetailsObject.getFieldValueInt(j, "KEY_BUSS_TABLE_ID");
                    busTableName = memberDetailsObject.getFieldValueString(j, "BUSS_TABLE_NAME");
                    bussColIds.add(memberDetailsObject.getFieldValueInt(j, "KEY_BUSS_COL_ID"));
                    bussColNames.add(memberDetailsObject.getFieldValueString(j, "KEY_BUSS_COL_NAME"));

                }
                DimensionMembers dimensionMembers = new DimensionMembers(elementIds, busTableId, busTableName, bussColIds, bussColNames, memberObject.getFieldValueInt(i, "MEMBER_ID"));
                buildMemberSecurity(memberObject.getFieldValueInt(i, "MEMBER_ID"), dimensionID, elementIds, dimensionMembers);
                buildRoleSecurity(memberObject.getFieldValueInt(i, "MEMBER_ID"), subFolderID, dimensionMembers);
                dimension.addDimensionMember(dimensionMembers);
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

    }

    public void buildRoleSecurity(int memberId, int subFolderID, DimensionMembers dimensionMembers) {
        PbReturnObject pbReturnObject = new PbReturnObject();
        String memValues = "";
        int userSceId = 0;
        try {
            String filterRoleMembersValues = getResourceBundle().getString("filterRoleMembersValues");
            Object[] objects = new Object[2];
            objects[0] = subFolderID;
            objects[1] = memberId;
            String finalQuery = super.buildQuery(filterRoleMembersValues, objects);
            pbReturnObject = super.execSelectSQL(finalQuery);
            RoleSecurity roleSecurity = null;
            if (pbReturnObject.getRowCount() > 0) {
                memValues = pbReturnObject.getFieldValueClobString(0, "MEMBER_VALUE");
                userSceId = pbReturnObject.getFieldValueInt(0, "ROLE_FILTER_ID");

            }
            roleSecurity = new RoleSecurity(userSceId, userLayerDAO.fromXML(memValues));


            dimensionMembers.add(roleSecurity);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

    }

    public void buildMemberSecurity(int memberID, int dimensionID, ArrayList<Integer> elementIds, DimensionMembers dimensionMembers) {
        String filterMembersValues = getResourceBundle().getString("filterMembersValues");
        Object[] objects = new Object[2];
        objects[0] = dimensionID;
        objects[1] = memberID;
        String finalQuery = "";
//        for(Integer elementId:elementIds){
        finalQuery = super.buildQuery(filterMembersValues, objects);
        PbReturnObject memberObject = new PbReturnObject();

        ArrayList<String> memValues = new ArrayList<String>();
        int userSceId = 0;
        int userId = 0;

        try {
            memberObject = super.execSelectSQL(finalQuery);

            if (memberObject.getRowCount() > 0) {
                for (int i = 0; i < memberObject.getRowCount(); i++) {
                    memValues = userLayerDAO.fromXML(memberObject.getFieldValueClobString(i, "MEMBER_VALUE"));
                    userSceId = memberObject.getFieldValueInt(i, "USER_FILTER_ID");
                    userId = memberObject.getFieldValueInt(i, "USER_ID");
//                 
//                 
//                 
                    MemberSecurity memberSecurity = new MemberSecurity(memValues, userSceId, userId);
                    dimensionMembers.add(memberSecurity);

                }
            }



        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
//        }
    }

    public String bulidWhereCondition(String tableName, String columnName, ArrayList<String> memberValues) {
        String whereCondtion = "";

        whereCondtion = " AND " + tableName + "." + columnName + " IN ('" + Joiner.on("','").join(memberValues) + "')";


        return whereCondtion;
    }
}
