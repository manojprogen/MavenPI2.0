/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.map;

import com.google.common.collect.ArrayListMultimap;
import java.util.List;

/**
 *
 * @author progen
 */
public class MapContent {

    ArrayListMultimap<String, List<String>> measMap;
    ArrayListMultimap<String, String> colorMap;
    ArrayListMultimap<String, String> markerMap;

    public ArrayListMultimap<String, String> getColorMap() {
        return colorMap;
    }

    public void setColorMap(ArrayListMultimap<String, String> colorMap) {
        this.colorMap = colorMap;
    }

    public ArrayListMultimap<String, String> getMarkerMap() {
        return markerMap;
    }

    public void setMarkerMap(ArrayListMultimap<String, String> markerMap) {
        this.markerMap = markerMap;
    }

    public ArrayListMultimap<String, List<String>> getMeasMap() {
        return measMap;
    }

    public void setMeasMap(ArrayListMultimap<String, List<String>> measMap) {
        this.measMap = measMap;
    }
}
