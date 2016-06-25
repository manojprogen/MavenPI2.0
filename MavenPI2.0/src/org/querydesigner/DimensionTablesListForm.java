package org.querydesigner;

public class DimensionTablesListForm extends org.apache.struts.action.ActionForm {

    private String connectionId;

    /**
     * @return the connectionId
     */
    public String getConnectionId() {
        return connectionId;
    }

    /**
     * @param connectionId the connectionId to set
     */
    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("connection id set is "+connectionId);
    }
}
