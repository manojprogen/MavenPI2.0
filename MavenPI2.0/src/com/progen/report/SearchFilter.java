/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report;

import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import prg.db.ContainerConstants.SortOrder;

/**
 *
 * @author arun
 */
public class SearchFilter {

    private Set<SearchFilterColumn> filterCols;

    public SearchFilter() {
        this.filterCols = new LinkedHashSet<SearchFilterColumn>();
    }

    public void add(String column, String condition, Object value) {
        SearchFilterColumn filterColumn = new SearchFilterColumn(column, condition, value);
        this.filterCols.add(filterColumn);
    }

    public void addSortColumnsForTopBtmFilter(String filterColumn, String sortColumn, SortOrder sortOrder) {
        SearchFilterColumn column = this.getFilterColumn(filterColumn);
        if (column != null && (column.getCondition().equalsIgnoreCase("TOP") || column.getCondition().equalsIgnoreCase("BTM"))) {
            column.addSortColumn(sortColumn, sortOrder);
        }
    }

    public ArrayList<String> getFilterColumns() {
        Iterable<String> srchColumns = Iterables.transform(filterCols, SearchFilterColumn.getStringTransformFunction());
        ArrayList<String> srchColumnLst = new ArrayList<String>();
        for (String column : srchColumns) {
            srchColumnLst.add(column);
        }
        return srchColumnLst;
    }

    public SearchFilterColumn getFilterColumn(String column) {
        Iterable<SearchFilterColumn> filterColumn = Iterables.filter(filterCols, SearchFilterColumn.getFilterColumn(column));
        if (filterColumn.iterator().hasNext()) {
            return filterColumn.iterator().next();
        } else {
            return null;
        }
    }

    public ArrayList<String> getTopBtmFilterColumns() {
        Iterable<SearchFilterColumn> filterColumn = Iterables.filter(filterCols, SearchFilterColumn.getFilterColumnWithTopBtm());
        ArrayList<String> srchColumnLst = new ArrayList<String>();
        for (SearchFilterColumn column : filterColumn) {
            srchColumnLst.add(column.getColumn());
        }
        return srchColumnLst;
    }

    public String getSearchCondition(String column) {
        int index = 0;
        return "";
    }
}
