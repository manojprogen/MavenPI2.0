/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.entities;

import com.progen.report.KPIElement;
import java.util.List;

/**
 *
 * @author progen
 */
public abstract class Report {

    private boolean persisted;

    public boolean isPersisted() {
        return persisted;
    }

    public void setPersisted(boolean persisted) {
        this.persisted = persisted;
    }

    public List<QueryDetail> getQueryDetails() {
        return null;
    }

    public List<KPIElement> getKPIElements() {
        return null;
    }

    public List<KPIElement> gettargetKpiElements() {
        return null;
    }
}
