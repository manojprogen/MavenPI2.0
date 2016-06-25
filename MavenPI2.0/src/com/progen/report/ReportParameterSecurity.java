/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report;

/**
 *
 * @author progen
 */
public class ReportParameterSecurity {

    public String getMemberId() {
        return memberId;
    }

    public Iterable<String> getParamSecureValues() {
        return paramSecureValues;
    }

    public String getParameteElementId() {
        return parameteElementId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public void setParamSecureValues(Iterable<String> paramSecureValues) {
        this.paramSecureValues = paramSecureValues;
    }

    public void setParameteElementId(String parameteElementId) {
        this.parameteElementId = parameteElementId;
    }
    String parameteElementId;
    String memberId;
    Iterable<String> paramSecureValues;
}
