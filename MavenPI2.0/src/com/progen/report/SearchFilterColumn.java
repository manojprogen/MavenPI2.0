/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import java.util.ArrayList;
import prg.db.ContainerConstants.SortOrder;

/**
 *
 * @author arun
 */
public class SearchFilterColumn {

    private String column;
    private String condition;
    private Object value;
    private ArrayList<SortColumn> sortColumns;

    SearchFilterColumn(String column, String condition, Object value) {
        this.column = column;
        this.condition = condition;
        this.value = value;
    }

    void addSortColumn(String sortColumnId, SortOrder sortOrder) {
        if (this.sortColumns == null) {
            this.sortColumns = new ArrayList<SortColumn>();
        }
        SortColumn sortColumn = new SortColumn(sortColumnId, sortOrder);
        this.sortColumns.add(sortColumn);

    }

    public ArrayList<SortColumn> getSortColumns() {
        if (this.sortColumns == null) {
            return new ArrayList<SortColumn>();
        }
        return (ArrayList<SortColumn>) this.sortColumns.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SearchFilterColumn other = (SearchFilterColumn) obj;
        if ((this.column == null) ? (other.column != null) : !this.column.equals(other.column)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.column != null ? this.column.hashCode() : 0);
        return hash;
    }

    public String getColumn() {
        return column;
    }

    public String getCondition() {
        return condition;
    }

    public Object getValue() {
        return value;
    }

    static Function<SearchFilterColumn, String> getStringTransformFunction() {
        Function<SearchFilterColumn, String> function = new Function<SearchFilterColumn, String>() {

            @Override
            public String apply(SearchFilterColumn value) {
                return value.getColumn();
            }
        };
        return function;
    }

    static Predicate<SearchFilterColumn> getFilterColumn(final String column) {
        Predicate<SearchFilterColumn> predicate = new Predicate<SearchFilterColumn>() {

            @Override
            public boolean apply(SearchFilterColumn value) {
                return value.getColumn().equals(column) ? true : false;

            }
        };
        return predicate;
    }

    static Predicate<SearchFilterColumn> getFilterColumnWithTopBtm() {
        Predicate<SearchFilterColumn> predicate = new Predicate<SearchFilterColumn>() {

            @Override
            public boolean apply(SearchFilterColumn value) {
                return ("TOP".equalsIgnoreCase(value.getCondition()) || "BTM".equalsIgnoreCase(value.getCondition())) ? true : false;

            }
        };
        return predicate;
    }
}
