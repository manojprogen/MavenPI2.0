/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.search;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author arun
 */
public class SearchColumn<T> {

    protected String elementId;
    protected Set<T> srchValues;
    protected String colName;

    public SearchColumn(String elementId, String colName) {
        this.elementId = elementId;
        srchValues = new HashSet<T>();
        this.colName = colName;

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(elementId);
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof SearchColumn) {
            return this.elementId.equals(((SearchColumn) o).elementId);
        } else {
            return false;
        }
    }

    public void addSearchValue(T value) {
        this.srchValues.add(value);
    }

    public static Predicate<SearchColumn> getSearchColumnPredicate(final String elementId) {
        Predicate<SearchColumn> srchColPredicate = new Predicate<SearchColumn>() {

            @Override
            public boolean apply(SearchColumn input) {
                if (input.elementId.equalsIgnoreCase(elementId)) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return srchColPredicate;
    }

    public static Predicate<SearchColumn> getRowViewByColumns() {
        Predicate<SearchColumn> srchColPredicate = new Predicate<SearchColumn>() {

            @Override
            public boolean apply(SearchColumn input) {
                if (input instanceof SearchDimColumn) {
                    return ((SearchDimColumn) input).isRowViewByColumn();
                } else {
                    return false;
                }
            }
        };
        return srchColPredicate;
    }

    public static Predicate<SearchColumn> getMeasureColumns() {
        Predicate<SearchColumn> srchColPredicate = new Predicate<SearchColumn>() {

            @Override
            public boolean apply(SearchColumn input) {
                if (input instanceof SearchMeasColumn) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return srchColPredicate;
    }

    public static Predicate<SearchColumn> getTimeDimensionColumn() {
        Predicate<SearchColumn> srchColPredicate = new Predicate<SearchColumn>() {

            @Override
            public boolean apply(SearchColumn input) {
                if (input instanceof SearchTimeColumn) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return srchColPredicate;
    }

    public String getMemberValuesAsCommaSepString() {
        StringBuilder mbrValues = new StringBuilder();
        for (T value : srchValues) {
            mbrValues.append(",").append(value.toString());
        }
        return mbrValues.toString().substring(1);
    }

    public String getElementId() {
        return this.elementId;
    }
}
