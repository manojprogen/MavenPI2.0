package com.progen.scenarion;

import java.util.Enumeration;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

public class PbScenarioRequestParameters {

    public HttpServletRequest request;
    public HashMap requestParamValues = new HashMap();

    public PbScenarioRequestParameters() {
    }

    public PbScenarioRequestParameters(HttpServletRequest request) {
        this.request = request;
    }

    public void setScenarioParametersHashMap() {
        Enumeration xy = request.getParameterNames();
        String temp;
        while (xy.hasMoreElements()) {
            temp = (String) xy.nextElement();
            requestParamValues.put(temp, request.getParameter(temp));
        }
    }
}
