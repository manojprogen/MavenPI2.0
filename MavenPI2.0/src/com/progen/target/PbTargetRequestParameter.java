package com.progen.target;

import java.util.Enumeration;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

public class PbTargetRequestParameter {

    public HttpServletRequest request;
    public HashMap requestParamValues = new HashMap();

    public PbTargetRequestParameter() {
    }

    public PbTargetRequestParameter(HttpServletRequest request) {
        this.request = request;
    }

    public void setParametersHashMap() {
        Enumeration xy = request.getParameterNames();
        String temp;
        while (xy.hasMoreElements()) {
            temp = (String) xy.nextElement();
            requestParamValues.put(temp, request.getParameter(temp));
            //////////////////////////////////////////////////////////.println("Search Key is Search Key"+xy.nextElement());
        }
    }
}
