/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.util.sort;

import com.google.common.base.Predicate;
import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.in;
import com.google.common.collect.Iterables;
import com.progen.report.SearchFilter;
import com.progen.report.SearchFilterColumn;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author arun
 */
public class DataSetFilter extends DataSetSorter {

    SearchFilter filter;

    public void setSearchFilter(SearchFilter filter) {
        this.filter = filter;
    }

    public ArrayList<Integer> searchDataSet() {
        Iterator<RowData> filteredData = rowDataList.iterator();
        Iterable<RowData> srchDataSet = rowDataList;
        int column = 1;
        Object srchValue;
        int i = 0;
        for (String col : filter.getFilterColumns()) {
            SearchFilterColumn filterColumn = filter.getFilterColumn(col);
            srchValue = filterColumn.getValue();
            Predicate<RowData> srchPredicate = this.getSearchPredicateForColumn(filterColumn.getCondition(), srchValue, column);
            srchDataSet = Iterables.filter(srchDataSet, srchPredicate);
            column++;
            i++;
            filteredData = srchDataSet.iterator();
            if (!filteredData.hasNext()) {
                break;
            }
        }


        ArrayList<Integer> rowSeq = new ArrayList<Integer>();
        while (filteredData.hasNext()) {
            rowSeq.add(filteredData.next().getRowSequence());
        }
        return rowSeq;
    }

    public ArrayList<Integer> filterDuplicates() {
        LinkedHashSet<RowData> nonDuplicateSet = new LinkedHashSet<RowData>();
        ArrayList<Integer> filterSequence = new ArrayList<Integer>();
        nonDuplicateSet.addAll(rowDataList);

        ArrayList<RowData> filteredDuplicateRowDataLst = new ArrayList<RowData>();

        for (RowData nonDuplicateData : nonDuplicateSet) {
            Iterator<RowData> rowDataIter = Iterables.filter(rowDataList, RowData.getRowDataPredicate(nonDuplicateData)).iterator();
            int count = 0;
            while (rowDataIter.hasNext()) {
                rowDataIter.next();
                count++;
            }
            if (count > 1) {
                filteredDuplicateRowDataLst.add(nonDuplicateData);
            }
        }



        if (filteredDuplicateRowDataLst.isEmpty()) {
            for (RowData rowData : rowDataList) {
                filterSequence.add(rowData.getRowSequence());
            }
        } else {
            Iterator<RowData> duplicateRecordsIter = Iterables.filter(rowDataList, and(in(filteredDuplicateRowDataLst))).iterator();
            while (duplicateRecordsIter.hasNext()) {
                RowData rowData = duplicateRecordsIter.next();
                filterSequence.add(rowData.getRowSequence());
            }
        }
        return filterSequence;
    }

    public Set<Object> getUniqueValuesInColumn(int columnIndex) {
        assert (columnIndex <= noOfColumns);
        LinkedHashSet<Object> uniqueValues = new LinkedHashSet<Object>();
        uniqueValues.addAll(getData(columnIndex));
        return uniqueValues;
    }

    private ArrayList<Object> getData(int columnIndex) {
        ArrayList<Object> columnData = new ArrayList<Object>();
        for (RowData data : rowDataList) {
            if (columnIndex == 1) {
                columnData.add(data.getData1());
            } else if (columnIndex == 2) {
                columnData.add(data.getData2());
            } else if (columnIndex == 3) {
                columnData.add(data.getData3());
            } else if (columnIndex == 4) {
                columnData.add(data.getData4());
            } else if (columnIndex == 5) {
                columnData.add(data.getData5());
            } else if (columnIndex == 6) {
                columnData.add(data.getData6());
            } else if (columnIndex == 7) {
                columnData.add(data.getData7());
            } else if (columnIndex == 8) {
                columnData.add(data.getData8());
            } else if (columnIndex == 9) {
                columnData.add(data.getData9());
            }

        }
        return columnData;
    }

    public ArrayList<Integer> searchDataSet(ArrayList<String> srchConditions, ArrayList<Object> srchValues) {
        Iterator<RowData> filteredData = rowDataList.iterator();
        Iterable<RowData> srchDataSet = rowDataList;
        int column = 1;
        Object srchValue;
        int i = 0;
        for (String srchCondition : srchConditions) {
            srchValue = srchValues.get(i);
            Predicate<RowData> srchPredicate = this.getSearchPredicateForColumn(srchCondition, srchValue, column);
            srchDataSet = Iterables.filter(srchDataSet, srchPredicate);
            column++;
            i++;
            filteredData = srchDataSet.iterator();
            if (!filteredData.hasNext()) {
                break;
            }
        }


        ArrayList<Integer> rowSeq = new ArrayList<Integer>();
        while (filteredData.hasNext()) {
            rowSeq.add(filteredData.next().getRowSequence());
        }
        return rowSeq;
    }

    private Predicate<RowData> getSearchPredicateForColumn(final String srchCondition, final Object srchValue, final int column) {
        Predicate<RowData> rowDataPredicate = new Predicate<RowData>() {

            @Override
            public boolean apply(RowData input) {
                if (column == 1) {
                    return evaluateDataWithSrchPattern(input.getData1(), srchCondition, srchValue);
                } else if (column == 2) {
                    return evaluateDataWithSrchPattern(input.getData2(), srchCondition, srchValue);
                } else if (column == 3) {
                    return evaluateDataWithSrchPattern(input.getData3(), srchCondition, srchValue);
                } else if (column == 4) {
                    return evaluateDataWithSrchPattern(input.getData4(), srchCondition, srchValue);
                } else if (column == 5) {
                    return evaluateDataWithSrchPattern(input.getData5(), srchCondition, srchValue);
                } else if (column == 6) {
                    return evaluateDataWithSrchPattern(input.getData6(), srchCondition, srchValue);
                } else if (column == 7) {
                    return evaluateDataWithSrchPattern(input.getData7(), srchCondition, srchValue);
                } else if (column == 8) {
                    return evaluateDataWithSrchPattern(input.getData8(), srchCondition, srchValue);
                } else if (column == 9) {
                    return evaluateDataWithSrchPattern(input.getData9(), srchCondition, srchValue);
                } else {
                    return false;
                }
            }
        };
        return rowDataPredicate;
    }

    private boolean evaluateDataWithSrchPattern(Object data, String srchPattern, Object srchValue) {
        if (data == null || srchValue == null) {
            return false;
        } else {
            if (srchPattern.equals("GT")) {
                if (data instanceof BigDecimal && srchValue instanceof BigDecimal) {
                    if (((BigDecimal) data).compareTo((BigDecimal) srchValue) > 0) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (data instanceof String && srchValue instanceof String) {
                    if (((String) data).compareTo((String) srchValue) > 0) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (data instanceof BigDecimal && srchValue instanceof Integer) {
                    if (((BigDecimal) data).compareTo(new BigDecimal((Integer) srchValue)) > 0) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else if (srchPattern.equals("LT")) {
                if (data instanceof BigDecimal && srchValue instanceof BigDecimal) {
                    if (((BigDecimal) data).compareTo((BigDecimal) srchValue) < 0) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (data instanceof String && srchValue instanceof String) {
                    if (((String) data).compareTo((String) srchValue) < 0) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (data instanceof BigDecimal && srchValue instanceof Integer) {
                    if (((BigDecimal) data).compareTo(new BigDecimal((Integer) srchValue)) < 0) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else if (srchPattern.equals("GE")) {
                if (data instanceof BigDecimal && srchValue instanceof BigDecimal) {
                    if (((BigDecimal) data).compareTo((BigDecimal) srchValue) >= 0) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (data instanceof String && srchValue instanceof String) {
                    if (((String) data).compareTo((String) srchValue) >= 0) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (data instanceof BigDecimal && srchValue instanceof Integer) {
                    if (((BigDecimal) data).compareTo(new BigDecimal((Integer) srchValue)) >= 0) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else if (srchPattern.equals("LE")) {
                if (data instanceof BigDecimal && srchValue instanceof BigDecimal) {
                    if (((BigDecimal) data).compareTo((BigDecimal) srchValue) <= 0) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (data instanceof String && srchValue instanceof String) {
                    if (((String) data).compareTo((String) srchValue) <= 0) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (data instanceof BigDecimal && srchValue instanceof Integer) {
                    if (((BigDecimal) data).compareTo(new BigDecimal((Integer) srchValue)) <= 0) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else if (srchPattern.equals("EQ")) {
                if (data instanceof BigDecimal && srchValue instanceof BigDecimal) {
                    if (((BigDecimal) data).compareTo((BigDecimal) srchValue) == 0) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (data instanceof String && srchValue instanceof String) {
                    if (((String) data).compareTo((String) srchValue) == 0) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (data instanceof BigDecimal && srchValue instanceof Integer) {
                    if (((BigDecimal) data).compareTo(new BigDecimal((Integer) srchValue)) == 0) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else if (srchPattern.equals("Exclude")) {
                if (data instanceof BigDecimal && srchValue instanceof BigDecimal) {
                    if (((BigDecimal) data).compareTo((BigDecimal) srchValue) != 0) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (data instanceof String && srchValue instanceof String) {
                    if (((String) data).compareTo((String) srchValue) != 0) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (data instanceof BigDecimal && srchValue instanceof Integer) {
                    if (((BigDecimal) data).compareTo(new BigDecimal((Integer) srchValue)) != 0) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else if (srchPattern.equals("BT")) {
                if (data instanceof BigDecimal && srchValue instanceof ArrayList) {
                    BigDecimal srchValue1, srchValue2;
                    srchValue1 = ((ArrayList<BigDecimal>) srchValue).get(0);
                    srchValue2 = ((ArrayList<BigDecimal>) srchValue).get(1);
                    if (srchValue1.signum() == -1 && srchValue2.signum() == -1) {  //code Modified By amar on aug 25,2015
                        if (((BigDecimal) data).compareTo((BigDecimal) srchValue1) < 0
                                && ((BigDecimal) data).compareTo((BigDecimal) srchValue2) >= 0) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        if (((BigDecimal) data).compareTo((BigDecimal) srchValue1) >= 0
                                && ((BigDecimal) data).compareTo((BigDecimal) srchValue2) < 0) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                } else if (data instanceof String && srchValue instanceof ArrayList) {
                    String st1, st2;
                    st1 = ((ArrayList<String>) srchValue).get(0);
                    st2 = ((ArrayList<String>) srchValue).get(1);
                    String dataStr = (String) data;
                    if (dataStr.toUpperCase().compareTo((String) st1.toUpperCase()) >= 0
                            && dataStr.toUpperCase().compareTo((String) st2.toUpperCase()) < 0) {
                        return true;
                    } else if (data instanceof BigDecimal && srchValue instanceof ArrayList) {
                        int srchValue1, srchValue2;
                        srchValue1 = ((ArrayList<Integer>) srchValue).get(0);
                        srchValue2 = ((ArrayList<Integer>) srchValue).get(1);
                        if (((BigDecimal) data).compareTo(new BigDecimal((Integer) srchValue1)) >= 0
                                && ((BigDecimal) data).compareTo(new BigDecimal((Integer) srchValue2)) < 0) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } // add code to instanceof string
                else {
                    return false;
                }
            } else if (srchPattern.equals("LIKE")) {
                if (data instanceof String && srchValue instanceof String) {
                    Pattern pattern = Pattern.compile(((String) srchValue).replace("*", ".*"));
                    Matcher matcher = pattern.matcher((String) data);
                    if (matcher.matches()) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else if (srchPattern.equals("IN")) {
                List searchList = (List) srchValue;
                boolean ret = false;
                for (int i = 0; i < searchList.size(); i++) {
                    if (data.equals(searchList.get(i))) {
                        ret = true;
                        break;
                    }
                }
                return ret;
            } else {
                return true;
            }
        }


    }
}
