package com.progen.dashboard;

import com.google.common.base.Predicate;
import java.util.List;

public class DashboardTableColorGroupHelper {

    private String elementId;
    private String elementName;
    private String measureMaxValue;
    private String measureMinValue;
    private String measureType;
    private boolean isgradiantBase = false;
    private List<String> colorVal;
    private List<String> colorCondOper;
    private List<String> condStartValue;
    private List<String> condEndValue;

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getMeasureMaxValue() {
        return measureMaxValue;
    }

    public void setMeasureMaxValue(String measureMaxValue) {
        this.measureMaxValue = measureMaxValue;
    }

    public String getMeasureMinValue() {
        return measureMinValue;
    }

    public void setMeasureMinValue(String measureMinValue) {
        this.measureMinValue = measureMinValue;
    }

    public boolean isIsgradiantBase() {
        return isgradiantBase;
    }

    public void setIsgradiantBase(boolean isgradiantBase) {
        this.isgradiantBase = isgradiantBase;
    }

    public List<String> getColorVal() {
        return colorVal;
    }

    public void setColorVal(List<String> colorVal) {
        this.colorVal = colorVal;
    }

    /**
     * @return the colorCondOper
     */
    public List<String> getColorCondOper() {
        return colorCondOper;
    }

    public void setColorCondOper(List<String> colorCondOper) {
        this.colorCondOper = colorCondOper;
    }

    public List<String> getCondStartValue() {
        return condStartValue;
    }

    public void setCondStartValue(List<String> condStartValue) {
        this.condStartValue = condStartValue;
    }

    public List<String> getCondEndValue() {
        return condEndValue;
    }

    public void setCondEndValue(List<String> condEndValue) {
        this.condEndValue = condEndValue;
    }

    public String getMeasureType() {
        return measureType;
    }

    public void setMeasureType(String measureType) {
        this.measureType = measureType;
    }

    public static Predicate<DashboardTableColorGroupHelper> getElementPredicatForColorGrp(final String elementID) {
        Predicate<DashboardTableColorGroupHelper> predicate = new Predicate<DashboardTableColorGroupHelper>() {

            @Override
            public boolean apply(DashboardTableColorGroupHelper input) {
                if (elementID.equalsIgnoreCase(input.getElementId())) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return predicate;
    }
}
