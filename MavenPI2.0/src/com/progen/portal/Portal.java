/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.portal;

import com.google.common.base.Predicate;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author progen
 */
public class Portal implements Serializable {

    private int portalID;
    private String portalName;
    private String portalDes;
    private int portalOrder;
    private List<PortLet> portlets;
    private boolean filterflag;
    private boolean savePortalFilterFlag;
    private PortletRuleHelper portletRuleHelper;

    public Portal(int portalID, String portalName) {
        this.portalID = portalID;
        this.portalName = portalName;
        this.portlets = new ArrayList<PortLet>();
    }

    public void addPortlet(PortLet portLet) {
        getPortlets().add(portLet);
    }

    public void setPortalDes(String portalDes) {
        this.portalDes = portalDes;
    }

    public void setPortalOrder(int portalOrder) {
        this.portalOrder = portalOrder;
    }

    public int getPortalID() {
        return portalID;
    }

    public String getPortalName() {
        return portalName;
    }

    public String getPortalDes() {
        return portalDes;
    }

    public int getPortalOrder() {
        return portalOrder;
    }

    public List<PortLet> getPortlets() {
        return portlets;
    }

    public static Predicate<Portal> getAccessPortalPredicate(final int portalID) {
        Predicate<Portal> predicate = new Predicate<Portal>() {

            public boolean apply(Portal input) {
                if (input.portalID == portalID) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return predicate;

    }

    public void setPortlets(List<PortLet> portlets) {
        this.portlets = portlets;
    }

    public boolean isExistingPotlet(int portletId) {
        boolean status = false;
        for (PortLet portLet : this.getPortlets()) {
            if (portLet.getPortLetId() == portletId) {
                status = true;
                break;
            }
        }
        return status;
    }

    /**
     * @return the filterflag
     */
    public boolean isFilterflag() {
        return filterflag;
    }

    /**
     * @param filterflag the filterflag to set
     */
    public void setFilterflag(boolean filterflag) {
        this.filterflag = filterflag;
    }

    /**
     * @return the portletRuleHelper
     */
    public PortletRuleHelper getPortletRuleHelper() {
        return portletRuleHelper;
    }

    /**
     * @param portletRuleHelper the portletRuleHelper to set
     */
    public void setPortletRuleHelper(PortletRuleHelper portletRuleHelper) {
        this.portletRuleHelper = portletRuleHelper;
    }

    /**
     * @return the savePortalFilterFlag
     */
    public boolean isSavePortalFilterFlag() {
        return savePortalFilterFlag;
    }

    /**
     * @param savePortalFilterFlag the savePortalFilterFlag to set
     */
    public void setSavePortalFilterFlag(boolean savePortalFilterFlag) {
        this.savePortalFilterFlag = savePortalFilterFlag;
    }
}
