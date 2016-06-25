/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.db;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 *
 * @author progen
 */
public class ParentDivDetails implements Serializable {

    private OneViewLetDetails oneviewletdetails;
    private int parentheight;
    private int parentwidth;
    private float parenttop;
    private float parentleft;
    private String parentdivids;
    private float version;
    private String oneviewId;
    private String noOfViewLets;
    private String busrolename;
    private LinkedHashMap reportParameterValues;
    private String filterBusinessRole;
    private String businessroleid;
    private static final long serialVersionUID = 7526471155622776147L;

    public OneViewLetDetails getoneviewletdetails() {
        return oneviewletdetails;
    }

    public void setoneviewletdetails(OneViewLetDetails div) {
        this.oneviewletdetails = oneviewletdetails;
    }

    public int getparentHeight() {
        return parentheight;
    }

    public void setparentHeight(int parentheight) {
        this.parentheight = parentheight;
    }

    public int getparentWidth() {
        return parentwidth;
    }

    public void setparentWidth(int parentwidth) {
        this.parentwidth = parentwidth;
    }

    public String getparentDivids() {
        return parentdivids;
    }

    public void setparentDivids(String parentdivids) {
        this.parentdivids = parentdivids;
    }

    public float getparentTop() {
        return parenttop;
    }

    /**
     * @param top the top to set
     */
    public void setparentTop(float parenttop) {
        this.parenttop = parenttop;
    }

    public float getparentLeft() {
        return parentleft;
    }

    /**
     * @param left the left to set
     */
    public void setparentLeft(float parentleft) {
        this.parentleft = parentleft;
    }

    public String getOneviewId() {
        return oneviewId;
    }

    public void setOneviewId(String oneviewId) {
        this.oneviewId = oneviewId;
    }

    public String getNoOfViewLets() {
        return noOfViewLets;
    }

    public void setbusrolename(String busrolename) {
        this.busrolename = busrolename;
    }

    public String getbusrolename() {
        return busrolename;
    }

    public void setNoOfViewLets(String noOfViewLets) {
        this.noOfViewLets = noOfViewLets;
    }

    public void setversion(float version) {
        this.version = version;
    }

    public float getversion() {
        return version;
    }

    public LinkedHashMap getReportParameterValues() {
        return reportParameterValues;
    }

    public void setReportParameterValues(LinkedHashMap reportParameterValues) {
        this.reportParameterValues = reportParameterValues;
    }

    public String getFilterBusinessRole() {
        return filterBusinessRole;
    }

    public void setFilterBusinessRole(String filterBusinessRole) {
        this.filterBusinessRole = filterBusinessRole;
    }

    public void setbusroleid(String businessroleid) {
        this.businessroleid = businessroleid;
    }

    public String getbusroleid() {
        return businessroleid;
    }
}
