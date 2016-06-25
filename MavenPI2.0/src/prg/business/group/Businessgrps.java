package prg.business.group;

import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 *
 * @author Bharathi reddy this class is for setter and gettter methos used in
 * displaying businessgrps list
 */
public class Businessgrps {

    private String businessGrpName;
    private String businessGrpId;
    private ArrayList factsList;
    private ArrayList allTablesList;
    private ArrayList bucketsList;
    private ArrayList dimensionList;
    private ArrayList roleList;
    public static Logger logger = Logger.getLogger(Businessgrps.class);
//added by susheela on 05-oct-09 start
    private ArrayList targetFactsList; //TargetFactsList
//added by susheela on 28-12-09 start
    private ArrayList grpTargetMeasuresList;
    private String grpTimeFlag;

    /**
     * @return the businessGrpName
     */
    public String getBusinessGrpName() {
        return businessGrpName;
    }

    /**
     * @param businessGrpName the businessGrpName to set
     */
    public void setBusinessGrpName(String businessGrpName) {
        this.businessGrpName = businessGrpName;
    }

    /**
     * @return the businessGrpId
     */
    public String getBusinessGrpId() {
        return businessGrpId;
    }

    /**
     * @param businessGrpId the businessGrpId to set
     */
    public void setBusinessGrpId(String businessGrpId) {
        this.businessGrpId = businessGrpId;
    }

    /**
     * @return the factsList
     */
    public ArrayList getFactsList() {
        return factsList;
    }

    /**
     * @param factsList the factsList to set
     */
    public void setFactsList(ArrayList factsList) {
        this.factsList = factsList;
    }

    /**
     * @return the allTablesList
     */
    public ArrayList getAllTablesList() {
        return allTablesList;
    }

    /**
     * @param allTablesList the allTablesList to set
     */
    public void setAllTablesList(ArrayList allTablesList) {
        this.allTablesList = allTablesList;
    }

    /**
     * @return the bucketsList
     */
    public ArrayList getBucketsList() {
        return bucketsList;
    }

    /**
     * @param bucketsList the bucketsList to set
     */
    public void setBucketsList(ArrayList bucketsList) {
        this.bucketsList = bucketsList;
    }

    /**
     * @return the dimensionList
     */
    public ArrayList getDimensionList() {
        return dimensionList;
    }

    /**
     * @param dimensionList the dimensionList to set
     */
    public void setDimensionList(ArrayList dimensionList) {
        this.dimensionList = dimensionList;
    }

    /**
     * @return the roleList
     */
    public ArrayList getRoleList() {
        return roleList;
    }

    /**
     * @param roleList the roleList to set
     */
    public void setRoleList(ArrayList roleList) {
        this.roleList = roleList;
    }

    public ArrayList getTargetFactsList() {
        return targetFactsList;
    }

    /**
     * @param targetFactsList the targetFactsList to set
     */
    public void setTargetFactsList(ArrayList targetFactsList) {
        this.targetFactsList = targetFactsList;
    }

    /**
     * @return the grpTimeFlag
     */
    public String getGrpTimeFlag() {
        return grpTimeFlag;
    }

    /**
     * @param grpTimeFlag the grpTimeFlag to set
     */
    public void setGrpTimeFlag(String grpTimeFlag) {
        this.grpTimeFlag = grpTimeFlag;
    }

    /**
     * @return the grpTargetMeasuresList
     */
    public ArrayList getGrpTargetMeasuresList() {
        return grpTargetMeasuresList;
    }

    /**
     * @param grpTargetMeasuresList the grpTargetMeasuresList to set
     */
    public void setGrpTargetMeasuresList(ArrayList grpTargetMeasuresList) {
        this.grpTargetMeasuresList = grpTargetMeasuresList;
    }
}
