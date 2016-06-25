/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report;

import java.io.Serializable;
import prg.db.PbReturnObject;

/**
 *
 * @author progen
 */
public class MultiPeriodKPI implements Serializable {

    private PbReturnObject yearObject;
    private PbReturnObject quarterObject;
    private PbReturnObject monthObject;
    private PbReturnObject dayObject;
    private PbReturnObject weekObject;
    private static final long serialVersionUID = 752647115566543L;

    public PbReturnObject getDayObject() {
        return dayObject;
    }

    public void setDayObject(PbReturnObject dayObject) {
        this.dayObject = dayObject;
    }

    public PbReturnObject getMonthObject() {
        return monthObject;
    }

    public void setMonthObject(PbReturnObject monthObject) {
        this.monthObject = monthObject;
    }

    public PbReturnObject getQuarterObject() {
        return quarterObject;
    }

    public void setQuarterObject(PbReturnObject quarterObject) {
        this.quarterObject = quarterObject;
    }

    public PbReturnObject getYearObject() {
        return yearObject;
    }

    public void setYearObject(PbReturnObject yearObject) {
        this.yearObject = yearObject;
    }

    /**
     * @return the weekObject
     */
    public PbReturnObject getWeekObject() {
        return weekObject;
    }

    /**
     * @param weekObject the weekObject to set
     */
    public void setWeekObject(PbReturnObject weekObject) {
        this.weekObject = weekObject;
    }
}
