package org.querydesigner;

public class SaveDimensionForm extends org.apache.struts.action.ActionForm {

    private String dimensionName;
    private String dimensionDescription;
    private String connId;

    public SaveDimensionForm() {
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("In Cons of SaveDim Form");
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
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("dim name is "+dimensionName);
    }

    /**
     * @return the dimensionDescription
     */
    public String getDimensionDescription() {
        return dimensionDescription;
    }

    /**
     * @param dimensionDescription the dimensionDescription to set
     */
    public void setDimensionDescription(String dimensionDescription) {
        this.dimensionDescription = dimensionDescription;
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("dim ddesc is "+dimensionDescription);
    }

    /**
     * @return the connId
     */
    public String getConnId() {
        return connId;
    }

    /**
     * @param connId the connId to set
     */
    public void setConnId(String connId) {
        this.connId = connId;
    }
}
