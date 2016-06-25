/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author progen
 */
public class MeasureGroup {

    private String groupName = "testGrp";
    private ArrayList<MeasureGroupColumn> participatingMeasures;

    public MeasureGroup(String groupName) {
        this.groupName = groupName;
        participatingMeasures = new ArrayList<MeasureGroupColumn>();

    }

    public void addMeasureGroupColumn(MeasureGroupColumn measureGroupColumn) {
        getParticipatingMeasures().add(measureGroupColumn);
    }

    public static Predicate<MeasureGroup> getMeasureGroupPredicate(final String measureID) {
        Predicate<MeasureGroup> predicate = new Predicate<MeasureGroup>() {

            MeasureGroupColumn groupColumn = null;

            public boolean apply(MeasureGroup input) {
                Iterator<MeasureGroupColumn> moduleIter = Iterables.filter(input.getParticipatingMeasures(), MeasureGroupColumn.getMeasureGroupColumnPredicate(measureID)).iterator();
                if (moduleIter.hasNext()) {
                    groupColumn = moduleIter.next();
                }

                if (groupColumn.getMeasID().equals(measureID)) {
                    return true;
                } else {
                    return false;
                }



            }
        };
        return predicate;

    }

    public ArrayList<MeasureGroupColumn> getParticipatingMeasures() {
        return participatingMeasures;
    }

    public String getGroupName() {
        return groupName;
    }
}
