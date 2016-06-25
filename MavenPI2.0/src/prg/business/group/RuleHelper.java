/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.business.group;

import java.util.List;

/**
 *
 * @author arun
 */
public class RuleHelper {

    private String connectionid;
    private String groupmember;
    private String rule;
    private List<String> groupvalues;
    private String tableName;
    private String baseColmnName;
    private String dependColumnName;

    public String getConnectionid() {
        return connectionid;
    }

    /**
     * @param connectionid the connectionid to set
     */
    public void setConnectionid(String connectionid) {
        this.connectionid = connectionid;
    }

    public List<String> getGroupvalues() {
        return groupvalues;
    }

    /**
     * @param groupvalues the groupvalues to set
     */
    public void setGroupvalues(List<String> groupvalues) {
        this.groupvalues = groupvalues;
    }

    /**
     * @return the groupmember
     */
    public String getGroupmember() {
        return groupmember;
    }

    /**
     * @param groupmember the groupmember to set
     */
    public void setGroupmember(String groupmember) {
        this.groupmember = groupmember;
    }

    /**
     * @return the rule
     */
    public String getRule() {
        return rule;
    }

    /**
     * @param rule the rule to set
     */
    public void setRule(String rule) {
        this.rule = rule;
    }

    /**
     * @return the baseColmnName
     */
    public String getBaseColmnName() {
        return baseColmnName;
    }

    /**
     * @param baseColmnName the baseColmnName to set
     */
    public void setBaseColmnName(String baseColmnName) {
        this.baseColmnName = baseColmnName;
    }

    /**
     * @return the dependColumnName
     */
    public String getDependColumnName() {
        return dependColumnName;
    }

    /**
     * @param dependColumnName the dependColumnName to set
     */
    public void setDependColumnName(String dependColumnName) {
        this.dependColumnName = dependColumnName;
    }

    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
