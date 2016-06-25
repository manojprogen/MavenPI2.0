/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.reportview.db;

import java.util.List;
import java.util.Map;

/**
 *
 * @author progen
 */
public class OverlayData {

    private List<Map<String, String>> chart2;
    private Map<String, Map<String, List<Map<String, String>>>> chart1;

    public List<Map<String, String>> getChart2() {
        return chart2;
    }

    public void setChart2(List<Map<String, String>> chart2) {
        this.chart2 = chart2;
    }

    public Map<String, Map<String, List<Map<String, String>>>> getChart1() {
        return chart1;
    }

    public void setChart1(Map<String, Map<String, List<Map<String, String>>>> chart1) {
        this.chart1 = chart1;
    }
}
