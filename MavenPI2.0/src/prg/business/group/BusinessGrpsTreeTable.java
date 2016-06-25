/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.business.group;

import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 *
 * @author Bharathi reddy
 */
public class BusinessGrpsTreeTable {

    public static Logger logger = Logger.getLogger(BusinessGrpsTreeTable.class);
    private String dimTableName;
    private String dimTableId;
    private String dimColumnId;
    private String dimColumnName;
    private String dimMemberName;
    private String dimMemberId;
    private String dimRelationName;
    private String dimRelationId;
    private String dimRelColumnId;
    private String dimRelColumnName;
    private String dimensionName;
    private String dimensionId;
    private ArrayList dimTableList;
    private ArrayList dimMembersList;
    private ArrayList dimHierarchyList;
    private String isPk;
    private String isAvailable;
    private String colType;
    private String allTableColType;
    private String factColType;
    private String allTableName;
    private String allTableId;
    private String allTableColName;
    private String allTableColId;
    private String actTabId;//Added By Santhosh
    private String factTableName;
    private String factTableId;
    private String factTableColName;
    private String factTableColId;
    private String bucketTableName;
    private String bucketTableId;
    private String bucketColName;
    private String bucketColId;
    private String bucketcolTableName;
    private String endTable;
    private String bucketStartLimit;
    private String bucketEndLimit;
    private String bussTblId;
    private ArrayList allSrcTableList;
    private String startTbl;
    private String roleName;
    private String roleId;
    private String endColumn;
    //added by susheela on 05-oct-09 start
    private String targetFactTableName;
    private String targetFactTableId;
    private String targetMeasureId;
    private String targetMeasureName;
    private String roleFlag;
    //susheela start 10-12-09
    private String preColFactType;
    private String preTableColType;
    private String roleColor;
    private String timeFlag;
    //added bys susheela on 28-12-09
    private String targetMeasId;
    private String targetMeasName;
    private String targetId;
    private String targetName;
    //added by susheela on 11jan-10
    private String targetMinTimeLevel;

    /**
     * @return the endColumn
     */
    public String getEndColumn() {
        return endColumn;
    }

    /**
     * @param endColumn the endColumn to set
     */
    public void setEndColumn(String endColumn) {
        this.endColumn = endColumn;
    }

    /**
     * @return the dimTableName
     */
    public String getDimTableName() {
        return dimTableName;
    }

    /**
     * @param dimTableName the dimTableName to set
     */
    public void setDimTableName(String dimTableName) {
        this.dimTableName = dimTableName;
    }

    /**
     * @return the dimTableId
     */
    public String getDimTableId() {
        return dimTableId;
    }

    /**
     * @param dimTableId the dimTableId to set
     */
    public void setDimTableId(String dimTableId) {
        this.dimTableId = dimTableId;
    }

    /**
     * @return the dimColumnId
     */
    public String getDimColumnId() {
        return dimColumnId;
    }

    /**
     * @param dimColumnId the dimColumnId to set
     */
    public void setDimColumnId(String dimColumnId) {
        this.dimColumnId = dimColumnId;
    }

    /**
     * @return the dimColumnName
     */
    public String getDimColumnName() {
        return dimColumnName;
    }

    /**
     * @param dimColumnName the dimColumnName to set
     */
    public void setDimColumnName(String dimColumnName) {
        this.dimColumnName = dimColumnName;
    }

    /**
     * @return the dimMemberName
     */
    public String getDimMemberName() {
        return dimMemberName;
    }

    /**
     * @param dimMemberName the dimMemberName to set
     */
    public void setDimMemberName(String dimMemberName) {
        this.dimMemberName = dimMemberName;
    }

    /**
     * @return the dimMemberId
     */
    public String getDimMemberId() {
        return dimMemberId;
    }

    /**
     * @param dimMemberId the dimMemberId to set
     */
    public void setDimMemberId(String dimMemberId) {
        this.dimMemberId = dimMemberId;
    }

    /**
     * @return the dimRelationName
     */
    public String getDimRelationName() {
        return dimRelationName;
    }

    /**
     * @param dimRelationName the dimRelationName to set
     */
    public void setDimRelationName(String dimRelationName) {
        this.dimRelationName = dimRelationName;
    }

    /**
     * @return the dimRelationId
     */
    public String getDimRelationId() {
        return dimRelationId;
    }

    /**
     * @param dimRelationId the dimRelationId to set
     */
    public void setDimRelationId(String dimRelationId) {
        this.dimRelationId = dimRelationId;
    }

    /**
     * @return the dimRelColumnId
     */
    public String getDimRelColumnId() {
        return dimRelColumnId;
    }

    /**
     * @param dimRelColumnId the dimRelColumnId to set
     */
    public void setDimRelColumnId(String dimRelColumnId) {
        this.dimRelColumnId = dimRelColumnId;
    }

    /**
     * @return the dimRelColumnName
     */
    public String getDimRelColumnName() {
        return dimRelColumnName;
    }

    /**
     * @param dimRelColumnName the dimRelColumnName to set
     */
    public void setDimRelColumnName(String dimRelColumnName) {
        this.dimRelColumnName = dimRelColumnName;
    }

    /**
     * @return the endTable
     */
    public String getEndTable() {
        return endTable;
    }

    /**
     * @param endTable the endTable to set
     */
    public void setEndTable(String endTable) {
        this.endTable = endTable;
    }

    /**
     * @return the dimensionName
     */
    public String getDimensionName() {
        return dimensionName;
    }

    /**
     * @param dimensionName the dimensionName to set
     */
    public void setDimensionName(String dimensionName) {
        this.dimensionName = dimensionName;
    }

    /**
     * @return the dimensionId
     */
    public String getDimensionId() {
        return dimensionId;
    }

    /**
     * @param dimensionId the dimensionId to set
     */
    public void setDimensionId(String dimensionId) {
        this.dimensionId = dimensionId;
    }

    /**
     * @return the dimTableList
     */
    public ArrayList getDimTableList() {
        return dimTableList;
    }

    /**
     * @param dimTableList the dimTableList to set
     */
    public void setDimTableList(ArrayList dimTableList) {
        this.dimTableList = dimTableList;
    }

    /**
     * @return the dimMembersList
     */
    public ArrayList getDimMembersList() {
        return dimMembersList;
    }

    /**
     * @param dimMembersList the dimMembersList to set
     */
    public void setDimMembersList(ArrayList dimMembersList) {
        this.dimMembersList = dimMembersList;
    }

    /**
     * @return the dimHierarchyList
     */
    public ArrayList getDimHierarchyList() {
        return dimHierarchyList;
    }

    /**
     * @param dimHierarchyList the dimHierarchyList to set
     */
    public void setDimHierarchyList(ArrayList dimHierarchyList) {
        this.dimHierarchyList = dimHierarchyList;
    }

    /**
     * @return the isPk
     */
    public String getIsPk() {
        return isPk;
    }

    /**
     * @param isPk the isPk to set
     */
    public void setIsPk(String isPk) {
        this.isPk = isPk;
    }

    /**
     * @return the isAvailable
     */
    public String getIsAvailable() {
        return isAvailable;
    }

    /**
     * @param isAvailable the isAvailable to set
     */
    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }

    /**
     * @return the allTableName
     */
    public String getAllTableName() {
        return allTableName;
    }

    /**
     * @param allTableName the allTableName to set
     */
    public void setAllTableName(String allTableName) {
        this.allTableName = allTableName;
    }

    /**
     * @return the allTableId
     */
    public String getAllTableId() {
        return allTableId;
    }

    /**
     * @param allTableId the allTableId to set
     */
    public void setAllTableId(String allTableId) {
        this.allTableId = allTableId;
    }

    /**
     * @return the allTableColName
     */
    public String getAllTableColName() {
        return allTableColName;
    }

    /**
     * @param allTableColName the allTableColName to set
     */
    public void setAllTableColName(String allTableColName) {
        this.allTableColName = allTableColName;
    }

    /**
     * @return the allTableColId
     */
    public String getAllTableColId() {
        return allTableColId;
    }

    /**
     * @param allTableColId the allTableColId to set
     */
    public void setAllTableColId(String allTableColId) {
        this.allTableColId = allTableColId;
    }

    /**
     * @return the factTableName
     */
    public String getFactTableName() {
        return factTableName;
    }

    /**
     * @param factTableName the factTableName to set
     */
    public void setFactTableName(String factTableName) {
        this.factTableName = factTableName;
    }

    /**
     * @return the factTableId
     */
    public String getFactTableId() {
        return factTableId;
    }

    /**
     * @param factTableId the factTableId to set
     */
    public void setFactTableId(String factTableId) {
        this.factTableId = factTableId;
    }

    /**
     * @return the factTableColName
     */
    public String getFactTableColName() {
        return factTableColName;
    }

    /**
     * @param factTableColName the factTableColName to set
     */
    public void setFactTableColName(String factTableColName) {
        this.factTableColName = factTableColName;
    }

    /**
     * @return the factTableColId
     */
    public String getFactTableColId() {
        return factTableColId;
    }

    /**
     * @param factTableColId the factTableColId to set
     */
    public void setFactTableColId(String factTableColId) {
        this.factTableColId = factTableColId;
    }

    /**
     * @return the colType
     */
    public String getColType() {
        return colType;
    }

    /**
     * @param colType the colType to set
     */
    public void setColType(String colType) {
        this.colType = colType;
    }

    /**
     * @return the allTableColType
     */
    public String getAllTableColType() {
        return allTableColType;
    }

    /**
     * @param allTableColType the allTableColType to set
     */
    public void setAllTableColType(String allTableColType) {
        this.allTableColType = allTableColType;
    }

    /**
     * @return the factColType
     */
    public String getFactColType() {
        return factColType;
    }

    /**
     * @param factColType the factColType to set
     */
    public void setFactColType(String factColType) {
        this.factColType = factColType;
    }

    /**
     * @return the bucketTableName
     */
    public String getBucketTableName() {
        return bucketTableName;
    }

    /**
     * @param bucketTableName the bucketTableName to set
     */
    public void setBucketTableName(String bucketTableName) {
        this.bucketTableName = bucketTableName;
    }

    /**
     * @return the bucketTableId
     */
    public String getBucketTableId() {
        return bucketTableId;
    }

    /**
     * @param bucketTableId the bucketTableId to set
     */
    public void setBucketTableId(String bucketTableId) {
        this.bucketTableId = bucketTableId;
    }

    /**
     * @return the bucketColName
     */
    public String getBucketColName() {
        return bucketColName;
    }

    /**
     * @param bucketColName the bucketColName to set
     */
    public void setBucketColName(String bucketColName) {
        this.bucketColName = bucketColName;
    }

    /**
     * @return the bucketColId
     */
    public String getBucketColId() {
        return bucketColId;
    }

    /**
     * @param bucketColId the bucketColId to set
     */
    public void setBucketColId(String bucketColId) {
        this.bucketColId = bucketColId;
    }

    /**
     * @return the bucketcolTableName
     */
    public String getBucketcolTableName() {
        return bucketcolTableName;
    }

    /**
     * @param bucketcolTableName the bucketcolTableName to set
     */
    public void setBucketcolTableName(String bucketcolTableName) {
        this.bucketcolTableName = bucketcolTableName;
    }

    /**
     * @return the actTabId
     */
    public String getActTabId() {
        return actTabId;
    }

    /**
     * @param actTabId the actTabId to set
     */
    public void setActTabId(String actTabId) {
        this.actTabId = actTabId;
    }

    /**
     * @return the bucketStartLimit
     */
    public String getBucketStartLimit() {
        return bucketStartLimit;
    }

    /**
     * @param bucketStartLimit the bucketStartLimit to set
     */
    public void setBucketStartLimit(String bucketStartLimit) {
        this.bucketStartLimit = bucketStartLimit;
    }

    /**
     * @return the bucketEndLimit
     */
    public String getBucketEndLimit() {
        return bucketEndLimit;
    }

    /**
     * @param bucketEndLimit the bucketEndLimit to set
     */
    public void setBucketEndLimit(String bucketEndLimit) {
        this.bucketEndLimit = bucketEndLimit;
    }

    /**
     * @return the bussTblId
     */
    public String getBussTblId() {
        return bussTblId;
    }

    /**
     * @param bussTblId the bussTblId to set
     */
    public void setBussTblId(String bussTblId) {
        this.bussTblId = bussTblId;
    }

    /**
     * @return the allSrcTableList
     */
    public ArrayList getAllSrcTableList() {
        return allSrcTableList;
    }

    /**
     * @param allSrcTableList the allSrcTableList to set
     */
    public void setAllSrcTableList(ArrayList allSrcTableList) {
        this.allSrcTableList = allSrcTableList;
    }

    /**
     * @return the startTbl
     */
    public String getStartTbl() {
        return startTbl;
    }

    /**
     * @param startTbl the startTbl to set
     */
    public void setStartTbl(String startTbl) {
        this.startTbl = startTbl;
    }

    /**
     * @return the roleName
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * @param roleName the roleName to set
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * @return the roleId
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * @param roleId the roleId to set
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getTargetFactTableName() {
        return targetFactTableName;
    }

    /**
     * @param targetFactTableName the targetFactTableName to set
     */
    public void setTargetFactTableName(String targetFactTableName) {
        this.targetFactTableName = targetFactTableName;
    }

    /**
     * @return the targetFactTableId
     */
    public String getTargetFactTableId() {
        return targetFactTableId;
    }

    /**
     * @param targetFactTableId the targetFactTableId to set
     */
    public void setTargetFactTableId(String targetFactTableId) {
        this.targetFactTableId = targetFactTableId;
    }

    /**
     * @return the targetMeasureId
     */
    public String getTargetMeasureId() {
        return targetMeasureId;
    }

    /**
     * @param targetMeasureId the targetMeasureId to set
     */
    public void setTargetMeasureId(String targetMeasureId) {
        this.targetMeasureId = targetMeasureId;
    }

    /**
     * @return the targetMeasureName
     */
    public String getTargetMeasureName() {
        return targetMeasureName;
    }

    /**
     * @param targetMeasureName the targetMeasureName to set
     */
    public void setTargetMeasureName(String targetMeasureName) {
        this.targetMeasureName = targetMeasureName;
    }

    /**
     * @return the roleFlag
     */
    public String getRoleFlag() {
        return roleFlag;
    }

    /**
     * @param roleFlag the roleFlag to set
     */
    public void setRoleFlag(String roleFlag) {
        this.roleFlag = roleFlag;
    }

    /**
     * @return the preColFactType
     */
    public String getPreColFactType() {
        return preColFactType;
    }

    /**
     * @param preColFactType the preColFactType to set
     */
    public void setPreColFactType(String preColFactType) {
        this.preColFactType = preColFactType;
    }

    /**
     * @return the preTableColType
     */
    public String getPreTableColType() {
        return preTableColType;
    }

    /**
     * @param preTableColType the preTableColType to set
     */
    public void setPreTableColType(String preTableColType) {
        this.preTableColType = preTableColType;
    }

    /**
     * @return the roleColor
     */
    public String getRoleColor() {
        return roleColor;
    }

    /**
     * @param roleColor the roleColor to set
     */
    public void setRoleColor(String roleColor) {
        this.roleColor = roleColor;
    }

    /**
     * @return the timeFlag
     */
    public String getTimeFlag() {
        return timeFlag;
    }

    /**
     * @param timeFlag the timeFlag to set
     */
    public void setTimeFlag(String timeFlag) {
        this.timeFlag = timeFlag;
    }

    public String getTargetMeasId() {
        return targetMeasId;
    }

    /**
     * @param targetMeasId the targetMeasId to set
     */
    public void setTargetMeasId(String targetMeasId) {
        this.targetMeasId = targetMeasId;
    }

    public String getTargetMeasName() {
        return targetMeasName;
    }

    /**
     * @param targetMeasName the targetMeasName to set
     */
    public void setTargetMeasName(String targetMeasName) {
        this.targetMeasName = targetMeasName;
    }

    /**
     * @return the targetId
     */
    public String getTargetId() {
        return targetId;
    }

    /**
     * @param targetId the targetId to set
     */
    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTargetName() {
        return targetName;
    }

    /**
     * @param targetName the targetName to set
     */
    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    /**
     * @return the targetMinTimeLevel
     */
    public String getTargetMinTimeLevel() {
        return targetMinTimeLevel;
    }

    /**
     * @param targetMinTimeLevel the targetMinTimeLevel to set
     */
    public void setTargetMinTimeLevel(String targetMinTimeLevel) {
        this.targetMinTimeLevel = targetMinTimeLevel;
    }
}
