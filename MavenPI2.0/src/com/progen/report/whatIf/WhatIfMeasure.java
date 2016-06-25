/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.whatIf;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 *
 * @author sreekanth
 */
public class WhatIfMeasure {

    private String measEleId;
    private WhatIfSlider slider;
    private boolean isStandard = true;

    public WhatIfMeasure(String measEleId) {
        this.measEleId = measEleId;
        this.slider = new WhatIfSlider();
    }

    public String getMeasure() {
        return measEleId;
    }

    @Override
    public boolean equals(Object whatIfMeasure) {
        if (whatIfMeasure != null && whatIfMeasure instanceof WhatIfMeasure) {
            if (this.getMeasure().equals(((WhatIfMeasure) whatIfMeasure).getMeasure())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return measEleId;
    }

    public void setSliderValue(double sliderValue) {
        this.slider.setSliderValue(sliderValue);
    }

    public ArrayList<BigDecimal> performWhatIfOnData(ArrayList<BigDecimal> originalData) {
        ArrayList<BigDecimal> whatIfData = new ArrayList<BigDecimal>(originalData.size());
        BigDecimal wfValue;
        BigDecimal sliderValue = new BigDecimal(this.slider.getSliderValue() / 100.);
        for (BigDecimal measValue : originalData) {
            wfValue = measValue.add(measValue.multiply(sliderValue));
            whatIfData.add(wfValue);
        }
        return whatIfData;
    }

    public String toXML() {
        StringBuilder whatIfXML = new StringBuilder();
        whatIfXML.append("<measure>");
        whatIfXML.append("<elementID>" + getMeasure());
        whatIfXML.append("</elementID>");
        whatIfXML.append("<stdNonStd>" + isStandard);
        whatIfXML.append("</stdNonStd>");
        whatIfXML.append(slider.toXML());
        whatIfXML.append("</measure>");

        return whatIfXML.toString();
    }

    public double getSliderValue() {
        return this.slider.getSliderValue();
    }

    /**
     * @return the isStandard
     */
    public boolean isIsStandard() {
        return isStandard;
    }

    /**
     * @param isStandard the isStandard to set
     */
    public void setIsStandard(boolean isStandard) {
        this.isStandard = isStandard;
    }
}
