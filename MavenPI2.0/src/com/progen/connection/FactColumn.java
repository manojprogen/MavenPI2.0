package com.progen.connection;

import java.util.ArrayList;
import java.util.HashMap;

public class FactColumn {

    private int dbTableid;
    private String dbTableName;
    private int bussTableId;
    private boolean isavailableBussGrp;
    private int bussGrpSrcId;
    private int bussGrpId;
    private String bussGrpName;
    private HashMap colsDetails;
    private String[] colsNames;
    private String[] colsTypes;
    private ArrayList subFolderIds;
    private boolean isavailableRole;
    private HashMap subFolderDetails = new HashMap();
    private String bussTableName;
    private int[] folderID;
    private String[] foldername;
    private HashMap<Integer, Boolean> isPublishdetails;
    private HashMap<String, ArrayList> flodedrDetails;

    public int getDbTableid() {
        return dbTableid;
    }

    public void setDbTableid(int dbTableid) {
        this.dbTableid = dbTableid;
    }

    public String getDbTableName() {
        return dbTableName;
    }

    public void setDbTableName(String dbTableName) {
        this.dbTableName = dbTableName;
    }

    public int getBussTableId() {
        return bussTableId;
    }

    public void setBussTableId(int bussTableId) {
        this.bussTableId = bussTableId;
    }

    public int getBussGrpSrcId() {
        return bussGrpSrcId;
    }

    public void setBussGrpSrcId(int bussGrpSrcTabId) {
        this.bussGrpSrcId = bussGrpSrcTabId;
    }

    public boolean getIsIsavailableBussGrp() {
        return isavailableBussGrp;
    }

    public void setIsavailableBussGrp(boolean isavailableBussGrp) {
        this.isavailableBussGrp = isavailableBussGrp;
    }

    public HashMap getColsDetails() {
        return colsDetails;
    }

    public void setColsDetails(HashMap coloumnDetails) {
        this.colsDetails = coloumnDetails;
    }

    public String[] getColsNames() {
        return colsNames;
    }

    public void setColsNames(String[] colsNames) {
        this.colsNames = colsNames;
    }

    public String[] getColsTypes() {
        return colsTypes;
    }

    public void setColsTypes(String[] colsTypes) {
        this.colsTypes = colsTypes;
    }

    public String getColumnId(String columnName) {

        return this.colsDetails.get(columnName).toString();

    }

    public ArrayList getSubFolderIds() {
        return subFolderIds;
    }

    public void setSubFolderIds(ArrayList subFolderIds) {
        this.subFolderIds = subFolderIds;
    }

    public boolean getIsIsavailableRole() {
        return isavailableRole;
    }

    public void setIsavailableRole(boolean isavailableRole) {
        this.isavailableRole = isavailableRole;
    }

    public HashMap getSubFolderDetails() {
        return subFolderDetails;
    }

    public void setSubFolderDetails(HashMap subFolderDetails) {
        this.subFolderDetails = subFolderDetails;
    }

    public String getSubFolderTabId(String folderId) {

        return subFolderDetails.get(folderId).toString();
    }

    /**
     * @return the bussTableName
     */
    public String getBussTableName() {
        return bussTableName;
    }

    public void setBussTableName(String bussTableName) {
        this.bussTableName = bussTableName;
    }

    public int[] getFolderID() {
        return folderID;
    }

    public void setFolderID(int[] folderID) {
        this.folderID = folderID;
    }

    public String[] getFoldername() {
        return foldername;
    }

    public void setFoldername(String[] foldername) {
        this.foldername = foldername;
    }

    public HashMap<Integer, Boolean> getIsPublishdetails() {
        return isPublishdetails;
    }

    public void setIsPublishdetails(HashMap<Integer, Boolean> isPublishdetails) {
        this.isPublishdetails = isPublishdetails;
    }

    public int getBussGrpId() {
        return bussGrpId;
    }

    public void setBussGrpId(int bussGrpId) {
        this.bussGrpId = bussGrpId;
    }

    public String getBussGrpName() {
        return bussGrpName;
    }

    public void setBussGrpName(String bussGrpName) {
        this.bussGrpName = bussGrpName;
    }

    public HashMap<String, ArrayList> getFlodedrDetails() {
        return flodedrDetails;
    }

    public void setFlodedrDetails(HashMap<String, ArrayList> flodedrDetails) {
        this.flodedrDetails = flodedrDetails;
    }
}
