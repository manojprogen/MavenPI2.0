/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author db2admin
 */
public class PbReportRequestParameter {

    public HttpServletRequest request;
    public HashMap<String, Object> requestParamValues = new HashMap<String, Object>();

    public PbReportRequestParameter() {
    }

    public PbReportRequestParameter(HttpServletRequest request) {
        this.request = request;
    }

    public void setParametersHashMap() {
        Enumeration xy = request.getParameterNames();
        String temp;
        Object paramObject = null;
        while (xy.hasMoreElements()) {
            temp = (String) xy.nextElement();
            if (temp.contains("CBOARP") && !temp.contains("_excbox")) {
                paramObject = processRequestParameter(request.getParameter(temp));
            } else {
                paramObject = request.getParameter(temp);
            }
            if (!requestParamValues.containsKey("CBOVIEW_BY"));
            requestParamValues.put(temp, paramObject);

        }

    }

    public Object processRequestParameter(String param) {
        Object returnVal = null;
        Gson gson = new Gson();
        try {
            Type typeList = new TypeToken<List<String>>() {
            }.getType();
            if (!param.isEmpty()) {
                returnVal = gson.fromJson(param, typeList);
//                
            } else {
                returnVal = param;
            }

        } catch (Exception e) {
            returnVal = param;
        }
        return returnVal;
    }
}
