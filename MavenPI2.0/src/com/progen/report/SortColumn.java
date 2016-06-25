/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report;

import com.google.common.base.Predicate;
import prg.db.ContainerConstants.SortOrder;

/**
 *
 * @author arun
 */
public class SortColumn {

    private String column;
    private SortOrder sortOrder;

    public String getColumn() {
        return column;
    }

    public SortColumn(String column, SortOrder sortOrder) {
        this.column = column;
        this.sortOrder = sortOrder;
    }

    public SortOrder getSortOrder() {
        return this.sortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    private SortColumn(String column) {
        this.column = column;
    }

    public static Predicate<SortColumn> getColumnPredicate(final String column) {
        Predicate<SortColumn> predicate = new Predicate<SortColumn>() {

            public boolean apply(SortColumn input) {
                if (input.getColumn().equals(column)) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return predicate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SortColumn other = (SortColumn) obj;
        if ((this.column == null) ? (other.column != null) : !this.column.equals(other.column)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + (this.column != null ? this.column.hashCode() : 0);
        return hash;
    }
}
