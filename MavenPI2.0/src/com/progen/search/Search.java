/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.search;

import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author arun
 */
public class Search {

    Set<SearchColumn> srchColumns;
    Integer folderId;
    int searchId;
    String searchText;
    String searchName;

    public Search(String searchText) {
        srchColumns = new LinkedHashSet<SearchColumn>();
        this.searchText = searchText;
    }

    public void setSearchId(int searchId) {
        this.searchId = searchId;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getSearchName() {
        return this.searchName;
    }

    public int getSearchId() {
        return this.searchId;
    }

    public String getSearchText() {
        return this.searchText;
    }

    public boolean isValid() {
        return (!srchColumns.isEmpty());
    }

    public void clearSearch() {
        this.srchColumns.clear();
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public int getFolderId() {
        return this.folderId;
    }

    public SearchMeasColumn addMeasureSearchColumn(String elementId, String colName, String columnTyp, String aggType, String origColType) {
//         if ( columnTyp.equalsIgnoreCase("Number")
//             || columnTyp.equalsIgnoreCase("Numeric")
//             || columnTyp.equalsIgnoreCase("float") )
        {
            SearchMeasColumn<Double> measColumn = new SearchMeasColumn<Double>(elementId, colName);
            measColumn.setAggregationType(aggType);
            measColumn.setColumnType(columnTyp);
            measColumn.setOriginalColType(origColType);
            srchColumns.add(measColumn);
            return measColumn;
        }
    }

    public SearchDimColumn addDimensionSearchColumn(String elementId, String columnTyp, String colName) {
//        if ( columnTyp.equalsIgnoreCase("VARCHAR")
//             || columnTyp.equalsIgnoreCase("VARCHAR2"))
        {
            SearchDimColumn<String> dimColumn = new SearchDimColumn<String>(elementId, colName);
            srchColumns.add(dimColumn);
            return dimColumn;
        }
    }

    public void setDimensionRowViewBy(String elementId, boolean isRowViewBy) {
        Iterator<SearchColumn> srchColIter = Iterables.filter(srchColumns, SearchColumn.getSearchColumnPredicate(elementId)).iterator();
        if (srchColIter.hasNext()) {
            ((SearchDimColumn) srchColIter.next()).isRowViewBy = isRowViewBy;
        }
    }

    public void setDimensionColViewBy(String elementId, boolean isColViewBy) {
        Iterator<SearchColumn> srchColIter = Iterables.filter(srchColumns, SearchColumn.getSearchColumnPredicate(elementId)).iterator();
        if (srchColIter.hasNext()) {
            ((SearchDimColumn) srchColIter.next()).isColViewBy = isColViewBy;
        }
    }

    public void addTimeDimensionSearchColumn(String elementId, String colName) {
        SearchTimeColumn<String> timeCol = new SearchTimeColumn<String>(elementId, colName);
        srchColumns.add(timeCol);
    }

    public void setTimeDimensionColumnDetails(String timeLevel, String timeType, ArrayList<String> columnTypes) {
        Iterator<SearchColumn> srchColIter = Iterables.filter(srchColumns, SearchColumn.getSearchColumnPredicate("Time")).iterator();
        if (srchColIter.hasNext()) {
            SearchTimeColumn timeCol = (SearchTimeColumn) srchColIter.next();
            timeCol.setTimeLevel(timeLevel);
            timeCol.setTimeType(timeType);
            for (String columnType : columnTypes) {
                timeCol.addTimeColumnType(columnType);
            }
        }
    }

    public void setTimeDimensionSearchValues(String value1, String value2) {
        Iterator<SearchColumn> srchColIter = Iterables.filter(srchColumns, SearchColumn.getSearchColumnPredicate("Time")).iterator();
        if (srchColIter.hasNext()) {
            SearchTimeColumn timeCol = (SearchTimeColumn) srchColIter.next();
            timeCol.addSearchValue(value1);
            timeCol.addSearchValue(value2);
        }
    }

    public void setDimensionSearchValues(String elementId, String value) {
        Iterator<SearchColumn> srchColIter = Iterables.filter(srchColumns, SearchColumn.getSearchColumnPredicate(elementId)).iterator();
        if (srchColIter.hasNext()) {
            ((SearchDimColumn) srchColIter.next()).addSearchValue(value);
        }
    }

    public ArrayList<String> getDimensionElementsRowViewBy() {
        ArrayList<String> rowViewBys = new ArrayList<String>();
        for (SearchColumn column : srchColumns) {
            if (column instanceof SearchDimColumn && ((SearchDimColumn) column).isRowViewBy) {
                rowViewBys.add(((SearchDimColumn) column).elementId);
            }
        }
        return rowViewBys;
    }

    public ArrayList<String> getDimensionElementsColumnViewBy() {
        ArrayList<String> colViewBys = new ArrayList<String>();
        for (SearchColumn column : srchColumns) {
            if (column instanceof SearchDimColumn && ((SearchDimColumn) column).isColViewBy) {
                colViewBys.add(((SearchDimColumn) column).elementId);
            }
        }

        return colViewBys;
    }

    public ArrayList<String> getMeasureElements() {
        ArrayList<String> measEles = new ArrayList<String>();
        for (SearchColumn column : srchColumns) {
            if (column instanceof SearchMeasColumn) {
                measEles.add(column.elementId);
            }
        }

        return measEles;
    }

    public Iterable<SearchColumn> getRowViewByDimensions() {
        Iterable<SearchColumn> rowViewCols = Iterables.filter(srchColumns, SearchColumn.getRowViewByColumns());
        return rowViewCols;
    }

    public Iterable<SearchColumn> getTimeDimension() {
        Iterable<SearchColumn> timeDimCol = Iterables.filter(srchColumns, SearchColumn.getTimeDimensionColumn());
        return timeDimCol;
    }

    public Iterable<SearchColumn> getMeasures() {
        Iterable<SearchColumn> measCol = Iterables.filter(srchColumns, SearchColumn.getMeasureColumns());
        return measCol;
    }

    public boolean isTimeDimSet() {
        Iterator timeDimIter = this.getTimeDimension().iterator();
        return timeDimIter.hasNext();
    }

    public boolean isDimensionOnlySearch() {
        return this.getMeasureElements().isEmpty();
    }
}
