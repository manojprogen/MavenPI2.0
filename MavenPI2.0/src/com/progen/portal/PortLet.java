/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.portal;

import com.google.common.base.Predicate;
import com.progen.charts.GraphProperty;
import com.progen.dashboard.DashboardTableColorGroupHelper;
import java.io.Serializable;
import java.util.*;
import org.jdom.Document;

/**
 *
 * @author progen
 */
public class PortLet implements Serializable {

    private static final long serialVersionUID = 2276668053074987654L;
    private int portLetId;
    private String portLetName;
    private String portLetDes;
    private int columnNumber;
    private int seqnumber;
    private Document XMLDocument;
    private String portletType;
    private String isSharable;
    private String xmlString;
    private String sortOrder = "Top-10";
    private int folderId = 0;
    private String whereClause = "";
    private String[] measureID = null;
    private String[] dimensionids = null;
    private String[] aggreGationType = null;
    private String[] measureNames = null;
    private GraphProperty graphProperty = null;
    private PortletXMLHelper portletXMLHelper = null;
    private String ruleOn;
    private Map<String, String> reportParams;
    private int portletHeight = 420;
    private String portletTime;
    private String portletPeriod;
    private Set<String> relatedPortlets = new HashSet<String>();
    private PortLetTimeHelper portLetTimeHelper;
    private PortLetSorterHelper portletSorterHelper;
    private PortletKPIHelper portletKPIHelper;
    private String imgName = "";
    private List<DashboardTableColorGroupHelper> portalTableColor = new ArrayList<DashboardTableColorGroupHelper>();
    private String targetVal = null;
    private HashMap timeInfoDetails;
    private ArrayList timeDetails;
    private PortletRuleHelper portletRuleHelper;
    private String portletGolbalFilter;

    public PortLet(int portLetId, String portLetName) {
        this.portLetId = portLetId;
        this.portLetName = portLetName;

    }

    public int getPortLetId() {
        return portLetId;
    }

    public String getPortLetName() {
        return portLetName;
    }

    public String getPortLetDes() {
        return portLetDes;
    }

    public void setPortLetDes(String portLetDes) {
        this.portLetDes = portLetDes;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    public int getSeqnumber() {
        return seqnumber;
    }

    public void setSeqnumber(int seqnumber) {
        this.seqnumber = seqnumber;
    }

    public String getPortletType() {
        return portletType;
    }

    public void setPortletType(String portletType) {
        this.portletType = portletType;
    }

    public String getIsSharable() {
        return isSharable;
    }

    public void setIsSharable(String isSharable) {
        this.isSharable = isSharable;
    }

    public static Predicate<PortLet> getAccessPortletPredicate(final int portLetId) {
        Predicate<PortLet> predicate = new Predicate<PortLet>() {

            public boolean apply(PortLet input) {
                if (input.portLetId == portLetId) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return predicate;
    }

    public void setXMLDocument(Document document) {
        if (document != null) {
            setPortletXMLHelper(new PortletXMLHelper(document));
            this.XMLDocument = document;
        }
    }

    public Document getXMLDocument() {
        return XMLDocument;
    }

    public String getXmlString() {
        return xmlString;
    }

    public void setXmlString(String xmlString) {
        this.xmlString = xmlString;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void setPortLetName(String portLetName) {
        this.portLetName = portLetName;
    }

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public String getWhereClause() {
        String whereCondtion = whereClause;
        if (getMeasureID() != null) {
            for (String str : getMeasureID()) {
                if (!str.trim().equalsIgnoreCase("")) {
                    whereCondtion = whereCondtion.replace(str, "A_" + str.trim());
                }
            }
        }
        return whereCondtion;
    }

    public void setWhereClause(String whereClause) {
        this.whereClause = whereClause;
    }

    public String[] getMeasureID() {
        return measureID;
    }

    public void setMeasureID(String[] measureID) {
        this.measureID = measureID;
    }

    public String[] getDimensionids() {
        return dimensionids;
    }

    public void setDimensionids(String[] dimensionids) {
        this.dimensionids = dimensionids;
    }

    public String[] getAggreGationType() {
        return aggreGationType;
    }

    public void setAggreGationType(String[] aggreGationType) {
        this.aggreGationType = aggreGationType;
    }

    public String[] getMeasureNames() {
        return measureNames;
    }

    public void setMeasureNames(String[] measureNames) {
        this.measureNames = measureNames;
    }

    public GraphProperty getGraphProperty() {
        return graphProperty;
    }

    public void setGraphProperty(GraphProperty graphProperty) {
        this.graphProperty = graphProperty;
    }

    public PortletXMLHelper getPortletXMLHelper() {
        return portletXMLHelper;
    }

    public void setPortletXMLHelper(PortletXMLHelper portletXMLHelper) {
        this.portletXMLHelper = portletXMLHelper;
    }

    /**
     * @return the ruleOn
     */
    public String getRuleOn() {
        return ruleOn;
    }

    /**
     * @param ruleOn the ruleOn to set
     */
    public void setRuleOn(String ruleOn) {
        this.ruleOn = ruleOn;
    }

    /**
     * @return the reportParams
     */
    public Map<String, String> getReportParams() {
        return reportParams;
    }

    /**
     * @param reportParams the reportParams to set
     */
    public void setReportParams(Map<String, String> reportParams) {
        this.reportParams = reportParams;
    }

    public int getPortletHeight() {
        if (portletHeight != 0) {
            return portletHeight;
        } else {
            return 420;
        }
    }

    public void setPortletHeight(int portletHeight) {
        this.portletHeight = portletHeight;
    }

    public PortLetTimeHelper getPortLetTimeHelper() {
        return portLetTimeHelper;
    }

    public void setPortLetTimeHelper(PortLetTimeHelper portLetTimeHelper) {
        this.portLetTimeHelper = portLetTimeHelper;
    }

    public PortLetSorterHelper getPortletSorterHelper() {
        return portletSorterHelper;
    }

    public Set<String> getRelatedPortlets() {
        return relatedPortlets;
    }

    public void setRelatedPortlets(Set<String> relatedPortlets) {
        this.relatedPortlets = relatedPortlets;
    }

    public void setPortletSorterHelper(PortLetSorterHelper portletSorterHelper) {
        this.portletSorterHelper = portletSorterHelper;
    }

    public PortletKPIHelper getPortletKPIHelper() {
        return portletKPIHelper;
    }

    public void setPortletKPIHelper(PortletKPIHelper portletKPIHelper) {
        this.portletKPIHelper = portletKPIHelper;
    }

    /**
     * @return the imgName
     */
    public String getImgName() {
        return imgName;
    }

    /**
     * @param imgName the imgName to set
     */
    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public List<DashboardTableColorGroupHelper> getPortalTableColor() {
        if (portalTableColor != null) {
            return portalTableColor;
        } else {
            return new ArrayList<DashboardTableColorGroupHelper>();
        }
    }

    public void setPortalTableColor(List<DashboardTableColorGroupHelper> portalTableColor) {
        this.portalTableColor = portalTableColor;
    }

    public String getTargetVal() {
        return targetVal;
    }

    public void setTargetVal(String targetVal) {
        this.targetVal = targetVal;
    }

    public HashMap getTimeInfoDetails() {
        return timeInfoDetails;
    }

    public void setTimeInfoDetails(HashMap timeInfoDetails) {
        this.timeInfoDetails = timeInfoDetails;
    }

    public ArrayList getTimeDetails() {
        return timeDetails;
    }

    public void setTimeDetails(ArrayList timeDetails) {
        this.timeDetails = timeDetails;
    }

    public PortletRuleHelper getPortletRuleHelper() {
        return portletRuleHelper;
    }

    public void setPortletRuleHelper(PortletRuleHelper portletRuleHelper) {
        this.portletRuleHelper = portletRuleHelper;
    }

    public String getPortletGolbalFilter() {
        return portletGolbalFilter;
    }

    public void setPortletGolbalFilter(String portletGolbalFilter) {
        this.portletGolbalFilter = portletGolbalFilter;
    }
}
