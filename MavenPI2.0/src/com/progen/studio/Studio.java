/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.studio;

import com.google.common.base.Predicate;
import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 *
 * @author progen
 */
public class Studio {

    LinkedHashSet<StudioItem> itemLst = new LinkedHashSet<StudioItem>();
    private ArrayList<String> labels = new ArrayList<String>();

    public Studio() {
        itemLst = new LinkedHashSet<StudioItem>();
        labels = new ArrayList<String>();
    }

    public void addItem(StudioItem item) {
        itemLst.add(item);
    }

    public Iterable<StudioItem> getList() {
        return this.itemLst;
    }

    public static Predicate<StudioItem> getStudioItemPredicate(final String reportName) {
        Predicate<StudioItem> predicate = new Predicate<StudioItem>() {

            public boolean apply(StudioItem input) {
                if (input.getAttribute2().toUpperCase().trim().equalsIgnoreCase(reportName.toUpperCase().trim())) {

                    return true;
                } else {
                    return false;
                }
            }
        };
        return predicate;

    }

    public static Predicate<StudioItem> getStudioItemsPredicate(final String reportName) {
        Predicate<StudioItem> predicate = new Predicate<StudioItem>() {

            public boolean apply(StudioItem input) {


                if (input.getAttribute2().toUpperCase().trim().startsWith(reportName.toUpperCase())) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return predicate;

    }

    public ArrayList<String> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<String> labels) {
        this.labels = labels;
    }

    public void addLabel(String label) {
        this.labels.add(label);
    }
}
