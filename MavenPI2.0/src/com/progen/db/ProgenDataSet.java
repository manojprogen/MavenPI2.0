/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.db;

import com.progen.query.RunTimeDimension;
import com.progen.query.RunTimeMeasure;
import com.progen.report.SortColumn;
import com.progen.report.bd.PbReportTableBD;
import com.progen.report.util.sort.DataSetFilter;
import com.progen.report.util.sort.DataSetSorter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.TreeMap;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import prg.db.Container;
import prg.db.ContainerConstants.SortOrder;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 *
 * @author progen
 */
public abstract class ProgenDataSet implements Serializable {

    private static final long serialVersionUID = -1111314724429119375L;
    public static Logger logger = Logger.getLogger(ProgenDataSet.class);
    //public ResultSet rset = null;
    public HashMap hMap = new HashMap();
    public HashMap hMap2 = new HashMap();
    public HashMap setHash = new HashMap();
    public HashMap secSort = new HashMap();
    public int rowCount = 0;
    public int colCount = 0;
    public PbReturnObject retSearch = null;
    public PbReturnObject retSort = null;
    public String dateFormat = "dd-MMM-yy";
    public Locale numberFormat = Locale.US;
    //added by santhosh.kumar@progenbusiness.com  for retreiving column types
    public String[] columnTypes = null;
    public int[] columnSizes = null;
    public int fromRow = 0;
    public int toRow = 0;
    protected String contextPath = null;
    protected HttpSession session = null;
    public TreeMap columnOverAllMaximums = null;
    public TreeMap columnOverAllMinimums = null;
    public TreeMap columnAverages = null;
    public TreeMap columnGrandTotals = null;
    public BigDecimal grandTotals[] = null;
    public BigDecimal avgTotals[] = null;
    public BigDecimal max[] = null;
    public BigDecimal min[] = null;
    public boolean processGT = false;
    protected TreeMap columnGrandTotalscross = null;
    protected BigDecimal crosstabmapkeys[] = null;
    //added on 18-02-2010 for the purpose of row grand total
    public TreeMap rowGrandTotals = null;
    public Integer[] columnTypesInt = null;
    public int columnCount = 0;
    protected ArrayList<Integer> viewSequence = new ArrayList<Integer>();
    //private HashMap<String,ArrayList<BigDecimal>> runTimeMeasMap = null;
    protected HashMap<String, RunTimeMeasure> rtMeasMap = null;
    protected HashMap<String, RunTimeDimension> rtDimMap = null;
    public HashMap<String, Integer> zerocountmap = new HashMap<String, Integer>();
    public int[] gtZeroCount = null;
    public TreeMap columnGrandTotalsZeroCount = null;

    public abstract int getRowCount();

    public abstract Object[][] retrieveData(ArrayList<String> sortColumns, char[] rowDataTypes);

    public abstract ArrayList<Object> retrieveData(String measEleId);

    public abstract ArrayList<BigDecimal> retrieveNumericData(String measEleId);
    public abstract ArrayList<BigDecimal> retrieveNumericDatarank(String measEleId,Container container);

    public abstract ArrayList<BigDecimal> retrieveNumericDataForMultiTime(String measeId, String groupId, String aggType);

    public abstract Object[][] retrieveDataBasedOnViewSeq(ArrayList<String> columns);

    public abstract ArrayList<BigDecimal> retrieveDataBasedOnViewSeq(String measEleId);

    public abstract void setRunTimeMeasureData(String measure, RunTimeMeasure rtMeasure);

    public abstract String getFieldValueString(int row, String colName);

    public abstract String getFieldValueString(int row, int column);

    public abstract BigDecimal getFieldValueRuntimeMeasure(int row, String measEleId);

    public abstract BigDecimal getFieldValueBigDecimal(int row, String colName);

    public abstract BigDecimal getFieldValueBigDecimal(int row, int colNo);

    public abstract String getFieldValueRuntimeDimension(int row, String dimEleId);

    public abstract String getFieldValueDateString(int row, String colName);

    public abstract String getFieldValueDateString(int row, int col);

    public abstract Object getFieldValue(int index, String string);

    public abstract BigDecimal getColumnAverageValue(String colName);

    public abstract BigDecimal getColumnAverageValue(int col);

    public abstract BigDecimal getColumnMinimumValue(int column);

    public abstract BigDecimal getColumnMinimumValue(String colName);

    public abstract BigDecimal getColumnMaximumValue(int column);

    public abstract BigDecimal getColumnMaximumValue(String colName);

    public abstract BigDecimal getColumnGrandTotalValue(String columnId);

    public abstract BigDecimal getColumnGrandTotalValue(int column);

    public abstract String[] getColumnNames();

    public abstract String[] getColumnTypes();
    
    public abstract Object[][] retrieveData(ArrayList<String> sortColumns, char[] rowDataTypes,Container container);
 public String groupColumns=null;
 public String search=null;
    /**
     * Initialize View Sequence This method initializes only once Subsequent
     * calls will not override the exisiting sequence
     */
    protected void initializeViewSequence() {
        this.viewSequence = new ArrayList<Integer>(this.rowCount);
        for (int i = 0; i < this.rowCount; i++) {
            this.viewSequence.add(i, i);
        }
    }

    public void setViewSequence(ArrayList<Integer> viewSequence) {
        this.viewSequence = viewSequence;
    }

    public ArrayList<Integer> getViewSequence() {
        return this.viewSequence;
    }

    public void resetViewSequence() {
        this.initializeViewSequence();
    }

    public ArrayList<BigDecimal> getRunTimeMeasureData(String measId) {
        RunTimeMeasure rtMeas = this.rtMeasMap.get(measId);
        if (rtMeas != null) {
            return rtMeas.getData();
        }
        return null;
    }

    public ArrayList<Integer> filterDuplicateRecords(String columnId) {
        ArrayList<Integer> filteredSequence = this.viewSequence;
        ArrayList<String> columns = new ArrayList<String>();

        columns.add(columnId);

        Object[][] data = new Object[this.rowCount][1];
        char[] dataType = new char[1];

        dataType[0] = this.getDataTypeForColumn(columnId);
        data = this.retrieveData(columns, dataType);

        DataSetFilter filter = new DataSetFilter();
        filter.setData(data, this.viewSequence);
        filteredSequence = filter.filterDuplicates();
        return filteredSequence;
    }

    private char getDataTypeForColumn(String columnId) {
        int index = 0;
        for (String columnName : this.getColumnNames()) {
            if (columnName.equals(columnId)) {
                break;
            }
            index++;
        }

        if (index < columnTypes.length && columnTypes[index].equalsIgnoreCase("NUMBER")) {
            return 'N';
        } else {
            return 'C';
        }
    }

    public void setRuntimeMeasure(String measId, ArrayList<BigDecimal> newList) {
        if (this.rtMeasMap == null) {
            this.rtMeasMap = new HashMap<String, RunTimeMeasure>();
        }

        this.rtMeasMap.put(measId, new RunTimeMeasure(newList));
    }

    public void setRuntimeMeasureData(String measId, BigDecimal value) {
        if (this.rtMeasMap == null) {
            this.rtMeasMap = new HashMap<String, RunTimeMeasure>();
        }
        if (rtMeasMap.get(measId) == null) {
            this.rtMeasMap.put(measId, new RunTimeMeasure(value));
        } else {
            rtMeasMap.get(measId).addMeasureData(value);
        }
    }

    public void setRuntimeDimension(String string, ArrayList<String> segmentNames) {
        if (this.rtDimMap == null) {
            this.rtDimMap = new HashMap<String, RunTimeDimension>();
        }

        this.rtDimMap.put(string, new RunTimeDimension(segmentNames));
    }

    public ArrayList<Integer> findTopBottom(ArrayList<SortColumn> sortColumns,
            int topBottomCount) {
        ArrayList<Integer> topBotSeq = null;
        Object[][] data;

        ArrayList<String> sortColumnNames = new ArrayList<String>();
        ArrayList<SortOrder> sortOrderLst = new ArrayList<SortOrder>();
        for (SortColumn column : sortColumns) {
            sortColumnNames.add(column.getColumn());
            sortOrderLst.add(column.getSortOrder());
        }


        data = this.retrieveDataBasedOnViewSeq(sortColumnNames);

        DataSetSorter sorter = new DataSetSorter();
        sorter.setData(data, this.viewSequence);
         sorter.groupColumns=this.groupColumns;
        topBotSeq = sorter.findTopBottom(topBottomCount, sortOrderLst);//.sortData(sortOrder);

        return topBotSeq;
    }

    public ArrayList<Integer> findTopBottomPercentWise(ArrayList<SortColumn> sortColumns,
            int topBottomCount) {
        ArrayList<Integer> topBotSeq = null;
        Object[][] data = new Object[this.rowCount][sortColumns.size()];

        ArrayList<String> sortColumnNames = new ArrayList<String>();
        ArrayList<SortOrder> sortOrderLst = new ArrayList<SortOrder>();
        for (SortColumn column : sortColumns) {
            sortColumnNames.add(column.getColumn());
            sortOrderLst.add(column.getSortOrder());
        }
        data = this.retrieveDataBasedOnViewSeq(sortColumnNames);

        DataSetSorter sorter = new DataSetSorter();
        sorter.setData(data, this.viewSequence);
        topBottomCount = (this.rowCount * topBottomCount) / 100;
        if (topBottomCount < 1) {
            topBottomCount = 1;
        }

        topBotSeq = sorter.findTopBottom(topBottomCount, sortOrderLst);

        return topBotSeq;
    }

    public ArrayList<Integer> findTopBottom(ArrayList<String> sortColumns,
            char[] sortTypes,
            int topBottomCount) {
        ArrayList<Integer> topBotSeq = null;
        Object[][] data;

        //char[] rowDataTypes = this.getSortDataTypes(sortDataTypes);
        data = this.retrieveDataBasedOnViewSeq(sortColumns);
        char[] sortOrder = getSortOrder(sortTypes);

        ArrayList<SortOrder> sortOrderLst = new ArrayList<SortOrder>();
        for (int i = 0; i < sortOrder.length; i++) {
            if (sortOrder[i] == 'A') {
                sortOrderLst.add(SortOrder.ASCENDING);
            } else {
                sortOrderLst.add(SortOrder.DESCENDING);
            }
        }

        DataSetSorter sorter = new DataSetSorter();
        sorter.setData(data, this.viewSequence);
         sorter.groupColumns=this.groupColumns;
        topBotSeq = sorter.findTopBottom(topBottomCount, sortOrderLst);//.sortData(sortOrder);

        return topBotSeq;
    }

    //modified sort and search methods as on 08-02-2010 fro the purpose of percent of column at run time
    public ArrayList<Integer> sortDataSet(ArrayList<String> sortColumns, char[] sortTypes, char[] sortDataTypes) {
        ArrayList<Integer> sortSequence = null;
        DataSetSorter sorter;// = new SortRowSets();

        Object[][] data;

        data = this.retrieveData(sortColumns, sortDataTypes);
        char[] sortOrder = getSortOrder(sortTypes);

        sorter = new DataSetSorter();
        sorter.setData(data, this.viewSequence);
            sorter.groupColumns=this.groupColumns;
        ArrayList<SortOrder> sortOrderLst = new ArrayList<SortOrder>();
        for (int i = 0; i < sortOrder.length; i++) {
            if (sortOrder[i] == 'A') {
                sortOrderLst.add(SortOrder.ASCENDING);
            } else {
                sortOrderLst.add(SortOrder.DESCENDING);
            }
        }


        sortSequence = sorter.sortData(sortOrderLst);
        return sortSequence;
    }

    public ArrayList<Integer> findTopBottomPercentWise(ArrayList<String> sortColumns,
            char[] sortTypes,
            int topBottomCount) {
        ArrayList<Integer> topBotSeq = null;
        Object[][] data = new Object[this.rowCount][sortColumns.size()];

        //char[] rowDataTypes = this.getSortDataTypes(sortDataTypes);
        data = this.retrieveDataBasedOnViewSeq(sortColumns);
        char[] sortOrder = getSortOrder(sortTypes);
        ArrayList<SortOrder> sortOrderLst = new ArrayList<SortOrder>();
        for (int i = 0; i < sortOrder.length; i++) {
            if (sortOrder[i] == 'A') {
                sortOrderLst.add(SortOrder.ASCENDING);
            } else {
                sortOrderLst.add(SortOrder.DESCENDING);
            }
        }

        DataSetSorter sorter = new DataSetSorter();
        sorter.setData(data, this.viewSequence);
        topBottomCount = (this.rowCount * topBottomCount) / 100;
        if (topBottomCount < 1) {
            topBottomCount = 1;
        }

        topBotSeq = sorter.findTopBottom(topBottomCount, sortOrderLst);

        return topBotSeq;
    }

    protected char[] getSortOrder(char[] sortTypes) {
        char[] sortOrder = new char[sortTypes.length];
        for (int i = 0; i < sortTypes.length; i++) {
            if ('0' == sortTypes[i] || 'A' == sortTypes[i]) {
                sortOrder[i] = 'A';
            } else {
                sortOrder[i] = 'D';
            }
        }
        return sortOrder;
    }

    public TreeMap getColumnOverAllMaximums() {
        return columnOverAllMaximums;
    }

    public void setColumnOverAllMaximums(TreeMap columnOverAllMaximums) {
        this.columnOverAllMaximums = columnOverAllMaximums;
    }

    public TreeMap getColumnOverAllMinimums() {
        return columnOverAllMinimums;
    }

    public void setColumnOverAllMinimums(TreeMap columnOverAllMinimums) {
        this.columnOverAllMinimums = columnOverAllMinimums;
    }

    public TreeMap getColumnAverages() {
        return columnAverages;
    }

    public void setColumnAverages(TreeMap columnAverages) {
        this.columnAverages = columnAverages;
    }

    public TreeMap getColumnGrandTotals() {
        return columnGrandTotals;
    }

    public void setColumnGrandTotals(TreeMap columnGrandTotals) {
        this.columnGrandTotals = columnGrandTotals;
    }

    public TreeMap getcolumnGrandTotalscross() {
        return columnGrandTotalscross;
    }

    public void setcolumnGrandTotalscross(TreeMap columnGrandTotalscross) {
        this.columnGrandTotalscross = columnGrandTotalscross;
    }

    public String getFieldValueStringBasedOnViewSeq(int row, String colName) {
        int actualRow = this.viewSequence.get(row);
        return getFieldValueString(actualRow, colName);
    }

    public BigDecimal getFieldValueBigDecimalBasedOnViewSeq(int row, String colName) {
        int actualRow = this.viewSequence.get(row);
        return getFieldValueBigDecimal(actualRow, colName);
    }

    public BigDecimal getFieldValueRuntimeMeasureBasedOnViewSeq(int row, String colName) {
        int actualRow = this.viewSequence.get(row);
        return getFieldValueRuntimeMeasure(actualRow, colName);
    }

    public ArrayList<Integer> twoRowviewGrouping(ArrayList<String> sortColumns, char[] sortTypes, char[] sortDataTypes) {
        ArrayList<Integer> sortSequence = null;
        DataSetSorter sorter;// = new SortRowSets();
        Object[][] data;
        data = this.retrieveDataBasedOnViewSeq(sortColumns);;

        sorter = new DataSetSorter();
        sorter.setData(data, this.viewSequence);

        sortSequence = sorter.groupData();
        return sortSequence;
    }
    /*
     *
     */

    public ArrayList<Integer> sortDataSetForSubTotal(ArrayList<String> sortColumns, char[] sortTypes, char[] sortDataTypes, Container container) {
        ArrayList<Integer> sortSequence = null;
        DataSetSorter sorter;// = new SortRowSets();
        // Added by Amar for exclude zero
        PbReportTableBD reportTableBD = new PbReportTableBD();
        reportTableBD.searchDataSetToExcludeZero(container);
        //end of code

        Object[][] data;

        data = this.retrieveData(sortColumns, sortDataTypes);
        char[] sortOrder = getSortOrder(sortTypes);

        //code added by Amar
        String eleId = sortColumns.get(sortColumns.size() - 1);
        sorter = new DataSetSorter();
        sorter.setData(data, this.viewSequence);

        ArrayList<SortOrder> sortOrderLst = new ArrayList<SortOrder>();
        for (int i = 0; i < sortOrder.length; i++) {
            if (sortOrder[i] == 'A') {
                sortOrderLst.add(SortOrder.ASCENDING);
            } else {
                sortOrderLst.add(SortOrder.DESCENDING);
            }
        }
        String refferedElements = "";
        String userColType = "";
        PbDb pbdb = new PbDb();
        PbReturnObject retobj = null;
        String elementId = eleId.replace("A_", "");
        String qry = "select  ACTUAL_COL_FORMULA,REFFERED_ELEMENTS,USER_COL_TYPE,REF_ELEMENT_TYPE,AGGREGATION_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + elementId;

        try {
            retobj = pbdb.execSelectSQL(qry);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (retobj != null && retobj.getRowCount() > 0) {
            refferedElements = retobj.getFieldValueString(0, 1);
            userColType = retobj.getFieldValueString(0, 2);
        }
        if (userColType.equalsIgnoreCase("SUMMARIZED")) {
            String[] tempStore = refferedElements.split("\\s*,\\s*");
            ArrayList<String> items = new ArrayList<String>();
            String temp;
            for (int i = 0; i < tempStore.length; i++) {
                temp = "A_" + tempStore[i];
                items.add(temp);
            }
            ArrayList<String> allMeasure = container.getTableDisplayMeasures();
            char[] sortMdataTypes = null;
            //sortMdataTypes = container.getColumnDataTypes(items);
            if (allMeasure.containsAll(items)) {


                ArrayList<String> itemList = container.setViewByList();
                itemList.addAll(items);
                sortMdataTypes = container.getColumnDataTypes(itemList);
                data = this.retrieveData(itemList, sortMdataTypes);
                sorter.setMeasuresData(data, this.viewSequence);
            }

        }
        sorter.search=this.search;
       sorter.groupColumns=this.groupColumns;
        sortSequence = sorter.sortOnSubTotal(sortOrder[(sortTypes.length - 1)], sortOrder.length, eleId, container);
        return sortSequence;
    }
    //added by anitha
        public ArrayList<Integer> sortDataSet(ArrayList<String> sortColumns, char[] sortTypes, char[] sortDataTypes,Container container) {
        ArrayList<Integer> sortSequence = null;
        DataSetSorter sorter;// = new SortRowSets();

        Object[][] data;

        data = this.retrieveData(sortColumns, sortDataTypes,container);
        char[] sortOrder = getSortOrder(sortTypes);

        sorter = new DataSetSorter();
        sorter.setData(data, this.viewSequence);
            sorter.groupColumns=this.groupColumns;
        ArrayList<SortOrder> sortOrderLst = new ArrayList<SortOrder>();
        for (int i = 0; i < sortOrder.length; i++) {
            if (sortOrder[i] == 'A') {
                sortOrderLst.add(SortOrder.ASCENDING);
            } else {
                sortOrderLst.add(SortOrder.DESCENDING);
}
        }


        sortSequence = sorter.sortData(sortOrderLst);
        return sortSequence;
    }
        //end of code by anitha
}
