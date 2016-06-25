/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.whatIf;

/**
 *
 * @author sreekanth
 */
public class WhatIfSlider {

    private double sliderValue;

    public WhatIfSlider() {
        this.sliderValue = 0.;
    }

    public double getSliderValue() {
        return this.sliderValue;
    }

    public void setSliderValue(double sliderValue) {
        this.sliderValue = sliderValue;
    }

    public String toXML() {
        StringBuilder whatIfXML = new StringBuilder();
        whatIfXML.append("<sliderValue>" + getSliderValue());
        whatIfXML.append("</sliderValue>");
        return whatIfXML.toString();
    }
}
