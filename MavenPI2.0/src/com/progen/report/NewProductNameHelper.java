/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report;

import java.util.ArrayList;

/**
 *
 * @author progen
 */
public class NewProductNameHelper {

    private ArrayList<String> goalNameType = new ArrayList<String>();
    private ArrayList<String> measures = new ArrayList<String>();
    private ArrayList<String> viewByNameValue = new ArrayList<String>();
    private ArrayList<String> currentValue = new ArrayList<String>();
    private ArrayList<String> priorValue = new ArrayList<String>();
    private ArrayList<String> changedpercent = new ArrayList<String>();
    private ArrayList<String> goalChangePernt = new ArrayList<String>();
    private ArrayList<String> goalChangeValue = new ArrayList<String>();

    public ArrayList<String> getGoalNameType() {
        return goalNameType;
    }

    public void setGoalNameType(ArrayList<String> goalNameType) {
        this.goalNameType = goalNameType;
    }

    public ArrayList<String> getMeasures() {
        return measures;
    }

    public void setMeasures(ArrayList<String> measures) {
        this.measures = measures;
    }

    public ArrayList<String> getViewByNameValue() {
        return viewByNameValue;
    }

    public void setViewByNameValue(ArrayList<String> viewByNameValue) {
        this.viewByNameValue = viewByNameValue;
    }

    public ArrayList<String> getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(ArrayList<String> currentValue) {
        this.currentValue = currentValue;
    }

    public ArrayList<String> getPriorValue() {
        return priorValue;
    }

    public void setPriorValue(ArrayList<String> priorValue) {
        this.priorValue = priorValue;
    }

    public ArrayList<String> getChangedpercent() {
        return changedpercent;
    }

    public void setChangedpercent(ArrayList<String> changedpercent) {
        this.changedpercent = changedpercent;
    }

    public ArrayList<String> getGoalChangePernt() {
        return goalChangePernt;
    }

    public void setGoalChangePernt(ArrayList<String> goalChangePernt) {
        this.goalChangePernt = goalChangePernt;
    }

    public ArrayList<String> getGoalChangeValue() {
        return goalChangeValue;
    }

    public void setGoalChangeValue(ArrayList<String> goalChangeValue) {
        this.goalChangeValue = goalChangeValue;
    }
}
