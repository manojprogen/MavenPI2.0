/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.business.group;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author progen
 */
public class MeasureType {

    private HashSet<String> measureUnits = new HashSet<String>();
    private String measureType;

    public MeasureType() {
    }

    public HashSet<String> getMeasureUnits() {
        return measureUnits;
    }

    public void setMeasureUnits(Set<String> measureUnits) {

        this.measureUnits = (HashSet<String>) measureUnits;
        //
    }

    public String getMeasureType() {
        return measureType;
    }

    public void setMeasureType(String measureType) {
        this.measureType = measureType;
        // 
    }
}
