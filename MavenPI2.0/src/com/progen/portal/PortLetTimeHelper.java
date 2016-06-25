package com.progen.portal;

import java.io.Serializable;

public class PortLetTimeHelper implements Serializable {

    private String portletTime = "";
    private String portletPeriod = "";
    private String stdComprePeriod = "";

    public String getPortletPeriod() {
        return portletPeriod;
    }

    public void setPortletPeriod(String portletPeriod) {
        this.portletPeriod = portletPeriod;
    }

    public String getPortletTime() {
        return portletTime;
    }

    public void setPortletTime(String portletTime) {
        this.portletTime = portletTime;
    }

    public String getStdComprePeriod() {
        return stdComprePeriod;
    }

    public void setStdComprePeriod(String stdComprePeriod) {
        this.stdComprePeriod = stdComprePeriod;
    }
}
