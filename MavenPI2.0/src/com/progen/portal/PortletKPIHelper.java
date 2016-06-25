package com.progen.portal;

import java.util.HashMap;

/**
 *
 * @author veenadhari.g@progenbusiness.com
 */
public class PortletKPIHelper {

    private String portletId;
    private String portalTabId;
    private HashMap<String, String> eidMap = new HashMap<String, String>();
    private String targetVal;
    private String kpiType;
    private String kpiMasterId;
    private String elementId;

    public String getPortletId() {
        return portletId;
    }

    public void setPortletId(String portletId) {
        this.portletId = portletId;
    }

    public String getPortalTabId() {
        return portalTabId;
    }

    public void setPortalTabId(String portalTabId) {
        this.portalTabId = portalTabId;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getTargetVal() {
        return targetVal;
    }

    public void setTargetVal(String targetVal) {
        this.targetVal = targetVal;
    }

    public String getKpiType() {
        return kpiType;
    }

    public void setKpiType(String kpiType) {
        this.kpiType = kpiType;
    }

    public String getKpiMasterId() {
        return kpiMasterId;
    }

    public void setKpiMasterId(String kpiMasterId) {
        this.kpiMasterId = kpiMasterId;
    }

    public HashMap<String, String> getEidMap() {
        return eidMap;
    }

    public void setEidMap(HashMap<String, String> eidMap) {
        this.eidMap = eidMap;
    }
}
