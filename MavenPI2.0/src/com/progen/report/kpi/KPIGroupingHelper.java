/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.kpi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class KPIGroupingHelper implements Serializable {

    private static final long serialVersionUID = 2276765074887879L;
    private List<KPISingleGroupHelper> kPISingleGroupList = new ArrayList<KPISingleGroupHelper>();
    private String KPIMasterId = "";
    private String dashLetId = "";

    public KPIGroupingHelper(String KPIMasterId) {
        this.KPIMasterId = KPIMasterId;
    }

    public void addSingleGroup(KPISingleGroupHelper singleGroupHelper) {
        getkPISingleGroupList().add(singleGroupHelper);
    }

    public List<KPISingleGroupHelper> getkPISingleGroupList() {
        return kPISingleGroupList;
    }

    public void setkPISingleGroupList(List<KPISingleGroupHelper> kPISingleGroupList) {
        this.kPISingleGroupList = kPISingleGroupList;
    }

    public String getDashLetId() {
        return dashLetId;
    }

    public void setDashLetId(String dashLetId) {
        this.dashLetId = dashLetId;
    }
}
