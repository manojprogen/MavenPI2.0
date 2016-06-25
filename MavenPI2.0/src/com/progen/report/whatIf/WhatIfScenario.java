/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.whatIf;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.math.BigDecimal;
import java.util.*;

public class WhatIfScenario {

    String reportId;
    private ArrayList<WhatIfMeasure> whatIfmeasureList;
    private ArrayList<WhatIfTarget> whatIfTargetList;
    private WhatIfSensitivity whatIfSensitivity;
    private ArrayList<WhatIfDependantMeasure> whatifDependantMeasuresList;
    private HashMap<String, String> stdnonstddetailsMap = new HashMap<String, String>();

    public WhatIfScenario(String reportId) {
        this.reportId = reportId;
        this.whatIfmeasureList = new ArrayList<WhatIfMeasure>();
        this.whatIfTargetList = new ArrayList<WhatIfTarget>();
        this.whatifDependantMeasuresList = new ArrayList<WhatIfDependantMeasure>();
    }

    public ArrayList<String> getWhatIfMeasureList() {
        List<String> wfMeasLst = Lists.transform(this.whatIfmeasureList, this.convertWhatIfMeasureToStringFunction());
        ArrayList<String> measEleLst = new ArrayList<String>();
        measEleLst.addAll(wfMeasLst);
        return measEleLst;
    }

    public ArrayList<String> getWhatIfTargetMeasureList() {
        List<String> wfTargetMeasLst = new ArrayList<String>();
        if (!this.whatIfTargetList.isEmpty()) {
            wfTargetMeasLst = Lists.transform(this.whatIfTargetList, this.convertTargetMeasureToStringFunction());
        }
        ArrayList<String> measEleLst = new ArrayList<String>();
        measEleLst.addAll(wfTargetMeasLst);
        return measEleLst;
    }

    public ArrayList<Double> getWhatIfMeasureSliderValues() {
        ArrayList<Double> whatIfSilderValues = new ArrayList<Double>();
        for (WhatIfMeasure wfMeasure : whatIfmeasureList) {
            whatIfSilderValues.add(wfMeasure.getSliderValue());
        }
        return whatIfSilderValues;
    }

    public void addWhatIfMeasures(ArrayList<String> whatIfMeasureEleLst) {

        List<WhatIfMeasure> measList = Lists.transform(Lists.newArrayList(whatIfMeasureEleLst), this.convertMeasToWhatIfMeasureFunction());
        this.whatIfmeasureList.addAll(measList);
    }

    public void addDependantMeasures(ArrayList<String> dependantMeasureList) {

        List<WhatIfDependantMeasure> dependantList = Lists.transform(Lists.newArrayList(dependantMeasureList), this.convertMeasToDependantMeasureFunction());
        this.whatifDependantMeasuresList.addAll(dependantList);

    }

    public ArrayList<String> getDependantMeasureList() {
        List<String> depenMeasLst = Lists.transform(this.whatifDependantMeasuresList, this.convertdependantMeasureToStringFunction());
        ArrayList<String> depenmeasEleLst = new ArrayList<String>();
        depenmeasEleLst.addAll(depenMeasLst);
        return depenmeasEleLst;
    }

    private Function<String, WhatIfMeasure> convertMeasToWhatIfMeasureFunction() {
        return new Function<String, WhatIfMeasure>() {

            public WhatIfMeasure apply(String whatIfMeasEle) {
                WhatIfMeasure whatIfMeasure = new WhatIfMeasure(whatIfMeasEle);
                if (!stdnonstddetailsMap.isEmpty()) {
                    whatIfMeasure.setIsStandard(Boolean.parseBoolean(stdnonstddetailsMap.get(whatIfMeasEle)));
                }
                return whatIfMeasure;
            }
        };
    }

    private Function<String, WhatIfDependantMeasure> convertMeasToDependantMeasureFunction() {
        return new Function<String, WhatIfDependantMeasure>() {

            public WhatIfDependantMeasure apply(String depenDantMeasEle) {
                WhatIfDependantMeasure whatIfDependantMeasure = new WhatIfDependantMeasure(depenDantMeasEle, whatIfmeasureList.get(0).getMeasure());
                if (!stdnonstddetailsMap.isEmpty()) {
                    whatIfDependantMeasure.setIsStandard(Boolean.parseBoolean(stdnonstddetailsMap.get(whatIfmeasureList.get(0).getMeasure())));
                }
                return whatIfDependantMeasure;
            }
        };
    }

    private Function<WhatIfMeasure, String> convertWhatIfMeasureToStringFunction() {
        return new Function<WhatIfMeasure, String>() {

            public String apply(WhatIfMeasure wfMeasure) {
                return wfMeasure.getMeasure();
            }
        };
    }

    private Function<WhatIfDependantMeasure, String> convertdependantMeasureToStringFunction() {
        return new Function<WhatIfDependantMeasure, String>() {

            public String apply(WhatIfDependantMeasure depenMeasure) {
                return depenMeasure.getDependantMeasure();
            }
        };
    }

    private Function<WhatIfTarget, String> convertTargetMeasureToStringFunction() {
        return new Function<WhatIfTarget, String>() {

            public String apply(WhatIfTarget target) {
                return target.getTargetMeasureId();
            }
        };
    }

    public boolean updateMeasures(ArrayList<String> measLst) {
        boolean needToUpdateWFMeasures;
        String measureType = "WhatifMeasure";
        Set<String> measToAdd = this.findMeasuresToAdd(measLst, measureType);
        Set<String> measToRemove = this.findMeasuresToRemove(measLst, measureType);
        needToUpdateWFMeasures = (!measToAdd.isEmpty()) || (!measToRemove.isEmpty());

        if (!measToAdd.isEmpty()) {
            this.addWhatIfMeasures(measToAdd);

        }

        if (!measToRemove.isEmpty()) {
            this.removeWhatIfMeasures(measToRemove);
        }

        return needToUpdateWFMeasures;
    }

    public boolean updateDependantMeasure(ArrayList<String> depeMesList) {
        boolean needToupdateDepenMeasures;
        String measureType = "DependantMeasure";
        Set<String> depenMeasToAdd = this.findMeasuresToAdd(depeMesList, measureType);
        Set<String> depenMeasToRemove = this.findMeasuresToRemove(depeMesList, measureType);
        needToupdateDepenMeasures = (!depenMeasToAdd.isEmpty()) || (!depenMeasToRemove.isEmpty());

        if (!depenMeasToAdd.isEmpty()) {
            this.addDependantMeasure(depenMeasToAdd);
        }
        if (!depenMeasToRemove.isEmpty()) {
            this.removeDependantMeasure(depenMeasToRemove);
        }
        return needToupdateDepenMeasures;
    }

    private Set<String> findMeasuresToAdd(ArrayList<String> measLst, String measureType) {
        Set<String> measToAdd = new HashSet<String>();
        for (String wfMeasure : measLst) {
            if (this.hasMeasure(wfMeasure, measureType)) {
                continue;
            } else {
                measToAdd.add(wfMeasure);
            }
        }
        return measToAdd;
    }

    private Set<String> findMeasuresToRemove(ArrayList<String> measLst, String measureType) {
        Set<String> measListForWF = new HashSet<String>();
        Set<String> currMeasList = new HashSet<String>();
        ArrayList<String> MeasureList;
        for (String wfMeasure : measLst) {
            measListForWF.add(wfMeasure);
        }
        if (measureType.equalsIgnoreCase("WhatifMeasure")) {
            MeasureList = this.getWhatIfMeasureList();
        } else {
            MeasureList = this.getDependantMeasureList();
        }
        for (String wfMeasure : MeasureList) {
            currMeasList.add(wfMeasure);
        }

        for (String wfMeasure : measListForWF) {
            currMeasList.remove(wfMeasure);
        }

        return currMeasList;
    }

    private boolean hasMeasure(String measure, String measureType) {

        if (measureType.equalsIgnoreCase("WhatifMeasure")) {
            for (WhatIfMeasure wfMeasure : whatIfmeasureList) {
                if (measure.equalsIgnoreCase(wfMeasure.getMeasure())) {
                    if (Boolean.parseBoolean(this.stdnonstddetailsMap.get(measure)) == wfMeasure.isIsStandard()) {
                        return true;
                    } else {
                        whatIfmeasureList.remove(wfMeasure);
                        return false;
                    }
                }
            }
        } else {
            for (WhatIfDependantMeasure dependantMeasure : whatifDependantMeasuresList) {
                if (measure.equalsIgnoreCase(dependantMeasure.getDependantMeasure())) {
                    if (Boolean.parseBoolean(this.stdnonstddetailsMap.get(measure)) == dependantMeasure.isIsStandard()) {
                        return true;
                    } else {
                        whatifDependantMeasuresList.remove(dependantMeasure);
                        return false;
                    }
                }
            }
        }


        return false;
    }

    private void addWhatIfMeasures(Set<String> measLst) {
        for (String measure : measLst) {
            WhatIfMeasure wfMeasure = new WhatIfMeasure(measure);
            wfMeasure.setIsStandard(Boolean.parseBoolean(stdnonstddetailsMap.get(measure)));
            this.whatIfmeasureList.add(wfMeasure);
        }
    }

    private void removeWhatIfMeasures(Set<String> measLst) {
        for (String measure : measLst) {


            for (int i = 0; i < whatIfTargetList.size(); i++) {
                boolean check = whatIfTargetList.get(i).isMeasurePresentInFormula(measure);
                if (check) {
                    whatIfTargetList.remove(i);
                    i = 0;
                }
            }
            WhatIfMeasure wfMeasure = new WhatIfMeasure(measure);
            this.whatIfmeasureList.remove(wfMeasure);

        }
    }

    public ArrayList<BigDecimal> performWhatIfOnMeasure(String measure, ArrayList<BigDecimal> originalData) {
        WhatIfMeasure wfMeasure = this.findWhatIfMeasure(measure);
        return wfMeasure.performWhatIfOnData(originalData);
    }

    public void updateSlider(String measure, double sliderValue) {
        WhatIfMeasure wfMeasure = this.findWhatIfMeasure(measure);
        wfMeasure.setSliderValue(sliderValue);
    }

    public WhatIfMeasure findWhatIfMeasure(String measure) {
        WhatIfMeasure wfMeasure = new WhatIfMeasure(measure);
        int measIndex = this.whatIfmeasureList.indexOf(wfMeasure);
        wfMeasure = this.whatIfmeasureList.get(measIndex);
        wfMeasure.setIsStandard(Boolean.parseBoolean(stdnonstddetailsMap.get(measure)));
        return wfMeasure;
    }

    public String toXML() {
        StringBuilder whatIfXML = new StringBuilder();
        whatIfXML.append("<WhatIfScenario><WhatIfMeasures>");
        for (WhatIfMeasure wfMeasure : whatIfmeasureList) {
            whatIfXML.append(wfMeasure.toXML());
        }
        whatIfXML.append("</WhatIfMeasures>");
        whatIfXML.append("<WhatIfTarget>");
        for (WhatIfTarget whatIfTarget : whatIfTargetList) {
            whatIfXML.append(whatIfTarget.toXML());
        }
        whatIfXML.append("</WhatIfTarget>");
        whatIfXML.append("<WhatIfDependant>");
        for (WhatIfDependantMeasure dependantMeasure : whatifDependantMeasuresList) {
            whatIfXML.append(dependantMeasure.toXML());
        }
        whatIfXML.append("</WhatIfDependant>");
        if (whatIfSensitivity != null && whatIfSensitivity.getDimensionId() != null) {
            whatIfXML.append("<WhatIfSensitivity>");
            whatIfXML.append(this.whatIfSensitivity.toXML());
            whatIfXML.append("</WhatIfSensitivity>");
        }
        whatIfXML.append("</WhatIfScenario>");
//        
        return whatIfXML.toString();
    }

    public void addWhatIfTarget(String whatIfTargetMeasuresname, String whatIftrgtFormula) {

        WhatIfTarget ifTarget = new WhatIfTarget(whatIfTargetMeasuresname);
        int checkTindex = whatIfTargetList.indexOf(ifTarget);
//        
        if (checkTindex != -1) {
            whatIfTargetList.get(whatIfTargetList.indexOf(ifTarget)).upDateWhatIfTarget(whatIftrgtFormula);

        } else {
            int elmentIdCount = whatIfTargetList.size() + 1;
            WhatIfTarget whatIfTarget = new WhatIfTarget(elmentIdCount, whatIfTargetMeasuresname, whatIftrgtFormula);
            whatIfTargetList.add(whatIfTarget);
        }
    }

    public ArrayList<WhatIfTarget> getWhatIfTargets() {
        return this.whatIfTargetList;
    }

    public void addWhatIfTarget(String targetEleId, String whatIfTargetMeasurename, String whatIftrgtFormula) {
        this.addWhatIfTarget(whatIfTargetMeasurename, whatIftrgtFormula);

    }

    public void updateSensitivity(String dimId, String dimValue, double sensitivity) {
        if (whatIfSensitivity == null) {
            whatIfSensitivity = new WhatIfSensitivity(dimId);
        }

        whatIfSensitivity.updateSensitivity(dimValue, sensitivity);


    }

    public WhatIfSensitivity getWhatIfSensitivity() {
        return whatIfSensitivity;
    }

    private void addDependantMeasure(Set<String> depenMeasToAdd) {
        for (String measure : depenMeasToAdd) {
            WhatIfDependantMeasure dependantMeasure = new WhatIfDependantMeasure(measure, getWhatIfMeasureList().get(0));
            this.whatifDependantMeasuresList.add(dependantMeasure);
        }
    }

    private void removeDependantMeasure(Set<String> depenMeasToRemove) {
        for (String measure : depenMeasToRemove) {


            for (int i = 0; i < whatIfTargetList.size(); i++) {
                boolean check = whatIfTargetList.get(i).isMeasurePresentInFormula(measure);
                if (check) {
                    whatIfTargetList.remove(i);
                    i = 0;
                }
            }

            WhatIfDependantMeasure dependantMeasure = new WhatIfDependantMeasure(measure, getWhatIfMeasureList().get(0));
            this.whatifDependantMeasuresList.remove(dependantMeasure);
        }

    }

    public WhatIfDependantMeasure findDependantMeasure(String measure) {
        WhatIfDependantMeasure dependantWhatifMeasure = new WhatIfDependantMeasure(measure);
        int measIndex = this.whatifDependantMeasuresList.indexOf(dependantWhatifMeasure);
        dependantWhatifMeasure = this.whatifDependantMeasuresList.get(measIndex);
        dependantWhatifMeasure.setIsStandard(Boolean.parseBoolean(stdnonstddetailsMap.get(measure)));
        return dependantWhatifMeasure;
    }

    public String getDependantMeasure(String measure) {
        WhatIfDependantMeasure dependantWhatifMeasure = new WhatIfDependantMeasure(measure);
        int measIndex = this.whatifDependantMeasuresList.indexOf(dependantWhatifMeasure);
        WhatIfDependantMeasure whatIfDependantMeasure = this.whatifDependantMeasuresList.get(measIndex);
        return whatIfDependantMeasure.getRelatedMeasure();
    }

    public Iterable<String> removeTargetAndDependentTargetMeas(String trgtMeasId) {

        ArrayList<String> measIdsForDel = new ArrayList<String>();
        Iterable<WhatIfTarget> trgtMeasLst = Iterables.filter(whatIfTargetList, WhatIfTarget.getTargetMeasurePredicate(trgtMeasId));
        for (WhatIfTarget trgt : trgtMeasLst) {
            measIdsForDel.add(trgt.getTargetMeasureId());
        }

        for (String measure : measIdsForDel) {
            this.removeTargetMeasure(measure);
        }
        return measIdsForDel;
    }

    private void removeTargetMeasure(String trgtMeasId) {
        for (int i = 0; i < whatIfTargetList.size(); i++) {
            if (whatIfTargetList.get(i).getTargetMeasureId().equalsIgnoreCase(trgtMeasId)) {
                whatIfTargetList.remove(i);
                break;
            }
        }
    }

    public void renameTargetMeasure(String measId, String measName) {
        for (int i = 0; i < whatIfTargetList.size(); i++) {
            if (whatIfTargetList.get(i).getTargetMeasureId().equalsIgnoreCase(measId)) {
                whatIfTargetList.get(i).setTargetMeasName(measName);
                break;
            }
        }
    }

    public HashMap<String, String> getStdnonstddetailsMap() {
        return stdnonstddetailsMap;
    }

    public void setStdnonstddetailsMap(HashMap<String, String> stdnonstddetailsMap) {
        this.stdnonstddetailsMap = stdnonstddetailsMap;
    }
}
