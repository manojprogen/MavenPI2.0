/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.map;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author progen
 */
public class GeoMapInfo {

    private List<String> primaryMeasure;
    private List<String> primaryMeasureLabel;
    private List<String> suppMeasures = new ArrayList<String>();
    private List<String> suppMeasureLabels = new ArrayList<String>();
    private String primaryDimension;
    private String primaryDimensionLabel;
    private List<String> suppDimension = new ArrayList<String>();
    private List<String> suppDimensionLabels = new ArrayList<String>();

    public void setPrimaryMeasure(List<String> primaryMeasure, List<String> primaryMeasureLabel) {
        this.primaryMeasure = primaryMeasure;
        this.primaryMeasureLabel = primaryMeasureLabel;
    }

    public void addSupportingMeasure(List<String> suppMeasures, List<String> suppMeasureLabels) {
        this.suppMeasures = suppMeasures;
        this.suppMeasureLabels = suppMeasureLabels;

    }

    public void addPrimaryDimension(String primaryDimension, String primaryDimensionLabel) {
        this.primaryDimension = primaryDimension;
        this.primaryDimensionLabel = primaryDimensionLabel;

    }

    public String getPrimaryDimension() {
        return this.primaryDimension;
    }

    public String getPrimaryDimensionLabel() {
        return this.primaryDimensionLabel;

    }

    public void addSupportingDimension(String dimension, String dimLabel) {
        this.suppDimension.add(dimension);
        this.suppDimensionLabels.add(dimLabel);
    }

    public List<String> getPrimaryMeasureLabel() {
        return this.primaryMeasureLabel;
    }

    public List<String> getPrimaryMeasure() {
        return this.primaryMeasure;
    }

    public String getSupportingMeasureLabel(String measId) {
        int index = suppMeasures.indexOf(measId);
        return suppMeasureLabels.get(index);
    }

//    public List<String> getSupportingMeasures(){
//        return this.sup
//    }
    public String getSupportingDimensionLabel(String dimId) {
        int index = suppDimension.indexOf(dimId);
        return suppDimensionLabels.get(index);
    }

    public List<String> getSupportingDimensions() {
        return this.suppDimension;
    }

    public List<String> getSupportingMeasures() {
        return this.suppMeasures;
    }
}
