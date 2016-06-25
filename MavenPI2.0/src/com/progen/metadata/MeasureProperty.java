package com.progen.metadata;

import java.util.HashMap;
import java.util.HashSet;

public class MeasureProperty {

    private String measureCategory;
    private String measureType;
    private String measureUnits;
    private String measureLable;
    private String measureRoundingType;
    private int measureRounding;
    private String measurePrefixDisplay;
    private String measuresuffixDisplay;
    private String valueIn;
    private String defaultDisplayin;
    private String measureDisplay;

    public String getMeasureCategory() {
        return measureCategory;
    }

    public void setMeasureCategory(String measureCategory) {
        this.measureCategory = measureCategory;
    }

    public String getMeasureType() {
        return measureType;
    }

    public void setMeasureType(String measureType) {
        this.measureType = measureType;
    }

    public String getMeasureLable() {
        return measureLable;
    }

    public void setMeasureLable(String measureLable) {
        this.measureLable = measureLable;
    }

    public String getMeasureRoundingType() {
        return measureRoundingType;
    }

    public void setMeasureRoundingType(String measureRoundingType) {
        this.measureRoundingType = measureRoundingType;
    }

    public String getMeasurePrefixDisplay() {
        return measurePrefixDisplay;
    }

    public void setMeasurePrefixDisplay(String measurePrefixDisplay) {
        this.measurePrefixDisplay = measurePrefixDisplay;
    }

    public String getMeasuresuffixDisplay() {
        return measuresuffixDisplay;
    }

    public void setMeasuresuffixDisplay(String measuresuffixDisplay) {
        this.measuresuffixDisplay = measuresuffixDisplay;
    }

    public String getValueIn() {
        return valueIn;
    }

    public void setValueIn(String valueIn) {
        this.valueIn = valueIn;
    }

    public String getDefaultDisplayin() {
        return defaultDisplayin;
    }

    public void setDefaultDisplayin(String defaultDisplayin) {
        this.defaultDisplayin = defaultDisplayin;
    }

    public String getMeasureDisplay() {
        return measureDisplay;
    }

    public void setMeasureDisplay(String measureDisplay) {
        this.measureDisplay = measureDisplay;
    }

    public String getMeasureUnits() {
        return measureUnits;
    }

    public void setMeasureUnits(String measureUnits) {
        this.measureUnits = measureUnits;
    }

    public int getMeasureRounding() {
        return measureRounding;
    }

    public void setMeasureRounding(int measureRounding) {
        this.measureRounding = measureRounding;
    }

    public boolean ismeasureUnit(HashMap measureUnitDetails) {
        boolean check = false;
        HashSet<String> unitsSet = (HashSet<String>) measureUnitDetails.get(measureType);
        if (!unitsSet.isEmpty()) {
            check = true;
        }
        return check;
    }
}
