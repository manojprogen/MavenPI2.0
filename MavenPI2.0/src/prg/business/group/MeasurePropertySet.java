/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.business.group;

import java.util.HashSet;

/**
 *
 * @author progen
 */
public class MeasurePropertySet {

    private HashSet<MeasureType> measureTypes = new HashSet<MeasureType>();
    private HashSet<String> measureCategory = new HashSet<String>();

    public MeasurePropertySet() {
    }

    public void addMeasureType(MeasureType measType) {
        this.getMeasureTypes().add(measType);

    }

    public HashSet<String> getMeasureCategory() {
        return measureCategory;
    }

    public void setMeasureCategory(HashSet<String> measureCategory) {
        this.measureCategory = measureCategory;
    }

    public HashSet<MeasureType> getMeasureTypes() {
        return measureTypes;
    }

    public HashSet<String> getMeasureUnits(String measureType) {
        HashSet<String> measureUnits = new HashSet<String>();
        for (MeasureType type : measureTypes) {
            if (measureType.equalsIgnoreCase(type.getMeasureType())) {
                measureUnits = type.getMeasureUnits();
                break;
            }
        }
        return measureUnits;
    }
}
