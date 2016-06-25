/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.whatIf;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 *
 * @author progen
 */
public class WhatIfSensitivity {

    String dimensionId;
    HashSet<SensitivityFactor> sensitivityFactors;

    public WhatIfSensitivity(String dimId) {
        this.dimensionId = dimId;
        this.sensitivityFactors = new HashSet<SensitivityFactor>();
    }

    public void updateSensitivity(String dimValue, double sensitivity) {
        SensitivityFactor factor;
        try {
            factor = this.findSensitivityFactor(dimValue);
            factor.updateSensitivity(sensitivity);
        } catch (NoSuchElementException nse) {
            factor = new SensitivityFactor(dimValue, sensitivity);
        }


        sensitivityFactors.add(factor);
    }

    public String getDimensionId() {
        return this.dimensionId;
    }

    public double getSensitivity(String value) throws NoSuchElementException {
        SensitivityFactor factor = this.findSensitivityFactor(value);
        return factor.getSensitivity();
    }

    public boolean initializeWhatIfSensitivity() {
        if (dimensionId != null) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isApplicable(String dimValue) {
        SensitivityFactor factor;
        try {
            factor = this.findSensitivityFactor(dimValue);
            return true;
        } catch (NoSuchElementException nse) {
            return false;
        }
    }

    public String toXML() {

        StringBuilder sensiBuilder = new StringBuilder();
        if (!sensitivityFactors.isEmpty()) {
            sensiBuilder.append("<measure>");
            sensiBuilder.append("<dimensionId>").append(this.getDimensionId());
            sensiBuilder.append("</dimensionId>");
            sensiBuilder.append("</measure>");
        }


        for (SensitivityFactor factor : sensitivityFactors) {
            sensiBuilder.append("<measure>");
            sensiBuilder.append("<dimensionId>").append(this.getDimensionId());
            sensiBuilder.append("</dimensionId>");
            sensiBuilder.append("<measuresAndvalues>");
            sensiBuilder.append("<key>").append(factor.getDimensionValue());
            sensiBuilder.append("</key>");
            sensiBuilder.append("<value>").append(factor.getSensitivity());
            sensiBuilder.append("</value>");
            sensiBuilder.append("</measuresAndvalues>");
            sensiBuilder.append("</measure>");
        }

        return sensiBuilder.toString();
    }

    public void removeSensitivity(String dimValue) throws NoSuchElementException {
        SensitivityFactor factor = this.findSensitivityFactor(dimValue);
        this.sensitivityFactors.remove(factor);
    }

    private SensitivityFactor findSensitivityFactor(String dimValue) throws NoSuchElementException {
        SensitivityFactor factor = Iterables.find(sensitivityFactors, SensitivityFactor.getSensitivityPredicateForValue(dimValue));
        return factor;
    }

    public ArrayListMultimap<Double, String> getDistinctSensitivityKeys() {
        HashSet<Double> uniqueSensitivityFactors = new HashSet<Double>();
        ArrayListMultimap<Double, String> sensitivityMap = ArrayListMultimap.create();
        for (SensitivityFactor factor : sensitivityFactors) {
            uniqueSensitivityFactors.add(factor.getSensitivity());
        }

        for (double sensitivity : uniqueSensitivityFactors) {
            Iterable<SensitivityFactor> factors = Iterables.filter(sensitivityFactors, SensitivityFactor.getSensitivityPredicateForSensitivity(sensitivity));

            for (SensitivityFactor factor : factors) {
                sensitivityMap.put(factor.getSensitivity(), factor.getDimensionValue());
            }
        }
        return sensitivityMap;
    }

    public Set<String> getDimensionValues() {
        Set<String> dimValues = new HashSet<String>();
        for (SensitivityFactor factor : sensitivityFactors) {
            dimValues.add(factor.getDimensionValue());
        }
        return dimValues;
    }

    public boolean isSensitivitySetForDimension() {

        return !this.sensitivityFactors.isEmpty();
    }
}
