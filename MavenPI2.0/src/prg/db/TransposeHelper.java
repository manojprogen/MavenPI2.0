/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.db;

import com.google.common.collect.ArrayListMultimap;
import com.progen.report.SearchFilter;
import com.progen.report.util.sort.DataSetFilter;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author arun
 */
public class TransposeHelper {

    public static Logger logger = Logger.getLogger(TransposeHelper.class);
    private PbReturnObject qryData;
    private int ctColCount;
    private int viewCount;
    private Set<ColumnGroup> colGrps;
    private String crosstabGT;
    private String crosstabST;
    private int rowCountCT;
    private int subTotalColIndex;
    private int subTotalColPos;
    private final ArrayList<String> measNames;
    private ArrayList<ArrayList<String>> displayLabels;

    TransposeHelper(PbReturnObject qryData, int ctColCount, int viewCount, String crosstabGT, String crosstabST, ArrayList<String> measNames) {
        this.qryData = qryData;
        this.ctColCount = ctColCount;
        this.viewCount = viewCount;
        this.crosstabGT = crosstabGT;
        this.crosstabST = crosstabST;
        this.measNames = measNames;
    }

    public PbReturnObject transpose() {
        PbReturnObject transData = new PbReturnObject();
        transData.setColumnNames(formulateColumnHeading());
        transData.setRowCount(qryData.getRowCount());
        rowCountCT = transData.getRowCount();

        loadColumnTypes(transData);
        //makeDisplayLabelsForColumns();


        try {
            //start loading data
            loadColumnData(transData);
        } catch (InvalidDataException ex) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "transpose","Load Column Data "+ex.getMessage());
            logger.error("Load Column Data:", ex);
        }

        combineDuplicates(transData);

        try {
            computeGT(transData);
        } catch (InvalidDataException ex) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "transpose","Compute GT "+ex.getMessage());
            logger.error("Compute GT:", ex);
        }

        try {
            computeST(transData);
        } catch (InvalidDataException ex) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "transpose","Compute GT "+ex.getMessage
            logger.error("Compute GT:", ex);
        }
        return transData;
    }

    void loadColumnData(PbReturnObject transData) throws InvalidDataException {
        String[] columnHeading = qryData.getColumnNames();
        ArrayList<Object> data;
        ArrayList<String> ctColumns = new ArrayList<String>();
        ArrayList<LinkedList<Object>> dimChain;
        DataSetFilter dsFilter = new DataSetFilter();
        SearchFilter filter;
        Object[][] dataSet;
        ArrayList<Integer> viewSequence = null;
        int colSeq = 1;
        int measCount = getMeasureCount();
        ArrayList<String> measNames = new ArrayList<String>();
        ArrayList<Object> measData;

        for (int i = 0; i < ctColCount; i++) {
            ctColumns.add(columnHeading[i]);
        }

        for (int i = ctColCount + viewCount; i < columnHeading.length; i++) {
            measNames.add(columnHeading[i]);
        }


        //load dimension data
        for (int i = ctColCount; i <= (ctColCount + viewCount - 1); i++) {
            data = qryData.retrieveData(columnHeading[i]);
            transData.setDataForColumn(columnHeading[i], data);
        }

        dataSet = qryData.retrieveDataBasedOnViewSeq(ctColumns);
        dsFilter.setData(dataSet, qryData.getViewSequence());

        //load measure data
        String measColHeading = "A_";
        int measColNum = 1;
        for (ColumnGroup group : getColumnGroups()) {
            dimChain = group.getMyChains();
            for (LinkedList<Object> oneChain : dimChain) {
                filter = new SearchFilter();
                for (int i = 0; i < oneChain.size(); i++) {
                    filter.add(ctColumns.get(i), "EQ", oneChain.get(i));
                }
                dsFilter.setSearchFilter(filter);
                viewSequence = dsFilter.searchDataSet();
                for (int i = 0; i < measCount; i++) {
                    measData = getEmptyMeasData();
                    data = qryData.retrieveData(measNames.get(i));
                    for (Integer j = 0; j < viewSequence.size(); j++) {
                        int actualRow = viewSequence.get(j);
                        measData.set(actualRow, data.get(actualRow));
                    }
                    transData.setDataForColumn(measColHeading + measColNum, measData);
                    measColNum++;
                }
            }
        }
    }

    private ArrayList<Object> getEmptyMeasData() {
        ArrayList<Object> measData = new ArrayList<Object>(rowCountCT);
        for (int i = 0; i < rowCountCT; i++) {
            measData.add(BigDecimal.ZERO);
        }
        return measData;
    }

    String[] formulateColumnHeading() {
        String[] columnHeadings = null;
        String[] currentHeadings = qryData.getColumnNames();
        columnHeadings = new String[getNumberOfColumns()];

        int index = 0;
        int measHeading = 1;
        for (int i = 0; i < columnHeadings.length; i++) {
            if (i < viewCount) {
                columnHeadings[index++] = currentHeadings[i + ctColCount];
            } else {
                columnHeadings[index++] = "A_" + measHeading++;
            }
        }
        return columnHeadings;
    }

    void loadColumnTypes(PbReturnObject transData) {
        int[] columnTypes = null;
        int[] currentColTypes = qryData.getColumnTypesInt();

        columnTypes = new int[transData.getColumnCount()];

        int index = 0;
        int measCount = getMeasureCount();
        int actualMeas = 0;
        int measIndex = 0;

        for (int i = ctColCount; i <= transData.getColumnCount(); i++) {
            if (i <= (ctColCount + viewCount - 1)) {
                columnTypes[index++] = currentColTypes[i];
            } else {
                actualMeas = measIndex + (ctColCount + viewCount);
                columnTypes[index] = currentColTypes[actualMeas];
                index++;
                if (measIndex < measCount - 1) {
                    measIndex++;
                } else {
                    measIndex = 0;
                }
            }
        }
        transData.setColumnTypesInt(columnTypes);
    }

    int getMeasureCount() {
        return qryData.getColumnCount() - (ctColCount + viewCount);
    }

    int getNumberOfColumns() {
        int columnCount = qryData.getColumnCount();
        int measCount = getMeasureCount();

        columnCount = 0;
        for (ColumnGroup group : getColumnGroups()) {
            columnCount += group.getGrandChildrenCount() * measCount;
        }

        columnCount += viewCount;
        return columnCount;
    }

    private Set<ColumnGroup> getColumnGroups() {
        if (colGrps == null) {
            colGrps = makeColumnGroups();
        }
        return colGrps;
    }

    Set<ColumnGroup> makeColumnGroups() {
        Set<ColumnGroup> columnGroups = new LinkedHashSet<ColumnGroup>();
        ArrayList<String> columnName;
        Object[][] data;
        DataSetFilter filter = new DataSetFilter();
        int index = 0;
        ArrayList<Object> chainValues;

        columnName = new ArrayList<String>();
        columnName.add(qryData.getColumnNames()[index]);
        data = qryData.retrieveDataBasedOnViewSeq(columnName);
        filter.setData(data, qryData.getViewSequence());
        Set<Object> uniqueValuesInColumn = filter.getUniqueValuesInColumn(1);
        for (Object columnValue : uniqueValuesInColumn) {
            ColumnGroup group = new ColumnGroup(columnValue);
            chainValues = new ArrayList<Object>();
            chainValues.add(columnValue);
            Set<ColumnGroup> childGroups = makeColumnGroups(chainValues, index + 1);
            if (!childGroups.isEmpty()) {
                group.addChildGroups(childGroups);
            }
            columnGroups.add(group);
        }
        return columnGroups;
    }

    Set<ColumnGroup> makeColumnGroups(ArrayList<Object> chainValue, int child) {
        ArrayList<Object> incomingChain = new ArrayList<Object>();
        Set<ColumnGroup> columnGroups = new LinkedHashSet<ColumnGroup>();
        if (child >= ctColCount) {
            return columnGroups;
        }
        ArrayList<String> columnName;
        Object[][] data;
        DataSetFilter dataSetFilter = new DataSetFilter();

        columnName = new ArrayList<String>();
        for (int i = 0; i <= child; i++) {
            columnName.add(qryData.getColumnNames()[i]);
        }

        data = qryData.retrieveDataBasedOnViewSeq(columnName);
        dataSetFilter.setData(data, qryData.getViewSequence());

        SearchFilter searchFilter = new SearchFilter();
        //add all filters
        for (int i = 0; i <= child - 1; i++) {
            searchFilter.add(qryData.getColumnNames()[i], "EQ", chainValue.get(i));
        }

        dataSetFilter.setSearchFilter(searchFilter);

        ArrayList<Integer> viewSequence = dataSetFilter.searchDataSet();
        qryData.setViewSequence(viewSequence);

        data = qryData.retrieveDataBasedOnViewSeq(columnName);
        dataSetFilter.setData(data, qryData.getViewSequence());

        Set<Object> uniqueValuesInColumn = dataSetFilter.getUniqueValuesInColumn(child + 1);
        qryData.resetViewSequence();
        incomingChain.addAll(chainValue);

        for (Object columnValue : uniqueValuesInColumn) {
            chainValue = new ArrayList<Object>();
            chainValue.addAll(incomingChain);

            ColumnGroup group = new ColumnGroup(columnValue);
            chainValue.add(columnValue);
            Set<ColumnGroup> childGroup = makeColumnGroups(chainValue, child + 1);
            if (!childGroup.isEmpty()) {
                group.addChildGroups(childGroup);
            }
            columnGroups.add(group);
        }

        return columnGroups;
    }

    void computeGT(PbReturnObject transData) throws InvalidDataException {
        int measCount = getMeasureCount();
        ArrayListMultimap<String, BigDecimal> gtData = ArrayListMultimap.create();
        String columnName;
        List<BigDecimal> measSum;
        ArrayList<Object> measData;
        if (crosstabGT.equalsIgnoreCase(ContainerConstants.CROSSTAB_GRANDTOTAL_FIRST) || crosstabGT.equalsIgnoreCase(ContainerConstants.CROSSTAB_GRANDTOTAL_LAST)) {
            addColumnsForGT(transData);
            gtData = computeGT(transData, crosstabGT);
            //insert all sums to result set
            for (int meas = 0; meas < measCount; meas++) {
                measData = new ArrayList<Object>();
                columnName = "A_GT_" + (meas + 1);
                measSum = gtData.get(columnName);
                for (BigDecimal data : measSum) {
                    measData.add(data);
                }
                transData.setDataForColumn(columnName, measData);
            }
        }
    }

    void computeST(PbReturnObject transData) throws InvalidDataException {
        List<ArrayList<Object>> chainElements = null;
        int size = 0;
        if (ctColCount > 1 && !crosstabST.equalsIgnoreCase(ContainerConstants.CROSSTAB_SUBTOTAL_NONE)) {
            subTotalColIndex = 1;
            subTotalColPos = getColumnPositionforST(transData);
            for (int depth = 0; depth < ctColCount - 1; depth++) {
                if (crosstabST.equals(ContainerConstants.CROSSTAB_SUBTOTAL_LAST)) {
                    subTotalColPos = subTotalColPos - size;
                }
                size = 0;
                for (ColumnGroup group : colGrps) {
                    chainElements = group.makeChainElements(depth);
                    computeAndSetST(chainElements, transData, depth);
                    size += chainElements.size() * getMeasureCount();
                }
            }
        }
    }

    private ArrayList<String> getMeasureNames() {
        String[] columnHeading = qryData.getColumnNames();
        ArrayList<String> measNames = new ArrayList<String>();
        for (int i = ctColCount + viewCount; i < columnHeading.length; i++) {
            measNames.add(columnHeading[i]);
        }
        return measNames;
    }

    private void computeAndSetST(List<ArrayList<Object>> chainElements, PbReturnObject transData, int depth) throws InvalidDataException {
        DataSetFilter dsFilter;
        ArrayList<String> columns;
        ArrayList<String> measNames = this.getMeasureNames();
        ArrayList<Integer> viewSequence;
        ArrayList<Object> measData;
        BigDecimal subTotal;
        String stColName;

        Object[][] data;
        depth++;

        String lastColumn = qryData.getColumnNames()[ctColCount + viewCount - 1];

        dsFilter = new DataSetFilter();

        //set Column Names to filter
        columns = new ArrayList<String>();
        for (int i = 0; i < depth; i++) {
            columns.add(qryData.getColumnNames()[i]);
        }
        columns.add(lastColumn);

        //get data for those columns
        data = qryData.retrieveDataBasedOnViewSeq(columns);
        dsFilter.setData(data, qryData.getViewSequence());
        SearchFilter filter = null;

        for (ArrayList<Object> chain : chainElements) {
            for (int meas = 0; meas < getMeasureCount(); meas++) {
                stColName = "A_ST_" + subTotalColIndex;
                measData = getEmptyMeasData();
                for (int row = 0; row < transData.getRowCount(); row++) {
                    filter = getSearchFilterForCTCols(chain, columns);
                    filter.add(lastColumn, "EQ", transData.getFieldValueString(row, lastColumn));
                    dsFilter.setSearchFilter(filter);
                    viewSequence = dsFilter.searchDataSet();
                    subTotal = addUpMeasureData(measNames.get(meas), viewSequence);
                    measData.set(row, subTotal);
                }
                transData.addColumn(subTotalColPos, stColName, Types.NUMERIC);
                transData.setDataForColumn(stColName, measData);
                subTotalColIndex++;
                subTotalColPos++;
            }
        }
    }

    private BigDecimal addUpMeasureData(String measureName, ArrayList<Integer> viewSequence) {
        BigDecimal subTotal = BigDecimal.ZERO;
        int actualRow;
        for (int row = 0; row < viewSequence.size(); row++) {
            actualRow = viewSequence.get(row);
            subTotal = subTotal.add(qryData.getFieldValueBigDecimal(actualRow, measureName));
        }
        return subTotal;
    }

    private SearchFilter getSearchFilterForCTCols(ArrayList<Object> chain, ArrayList<String> ctColumns) {
        int colIndex = 0;
        SearchFilter filter = null;
        filter = new SearchFilter();
        for (Object dimValue : chain) {
            filter.add(ctColumns.get(colIndex), "EQ", dimValue);
            colIndex++;
        }
        return filter;
    }

    private void addColumnsForGT(PbReturnObject transData) {
        String columnName;
        int colPos = crosstabGT.equals(ContainerConstants.CROSSTAB_GRANDTOTAL_FIRST) ? viewCount : transData.getColumnCount();
        int measCount = getMeasureCount();
        for (int i = 0; i < measCount; i++) {
            columnName = "A_GT_" + (measCount - i);
            transData.addColumn(colPos, columnName, Types.NUMERIC);
        }

        //update display labels
        ArrayList<String> chainLabel;
        for (int i = 0; i < measCount; i++) {
            chainLabel = new ArrayList<String>();
            for (int layer = 0; layer < ctColCount; layer++) {
                if (layer == 0) {
                    chainLabel.add("Grand Total");
                } else {
                    chainLabel.add("");
                }
            }
            chainLabel.add(measNames.get(i));
            if (colPos != 0) {
                displayLabels.add(chainLabel);
            } else {
                displayLabels.add(colPos, chainLabel);
            }

            colPos++;
        }
    }

    private int getColumnPositionforST(PbReturnObject transData) {
        int columnPos = -1;
        if (crosstabST.equals(ContainerConstants.CROSSTAB_SUBTOTAL_FIRST)) {
            if (crosstabGT.equals(ContainerConstants.CROSSTAB_GRANDTOTAL_NONE)
                    || crosstabGT.equals(ContainerConstants.CROSSTAB_GRANDTOTAL_LAST)) {
                columnPos = viewCount;
            } else if (crosstabGT.equals(ContainerConstants.CROSSTAB_GRANDTOTAL_FIRST)) {
                columnPos = viewCount + getMeasureCount();
            }
        } else if (crosstabST.equals(ContainerConstants.CROSSTAB_SUBTOTAL_LAST)) {
            if (crosstabGT.equals(ContainerConstants.CROSSTAB_GRANDTOTAL_NONE)
                    || crosstabGT.equals(ContainerConstants.CROSSTAB_GRANDTOTAL_FIRST)) {
                columnPos = transData.getColumnCount();
            } else if (crosstabGT.equals(ContainerConstants.CROSSTAB_GRANDTOTAL_LAST)) {
                columnPos = transData.getColumnCount() - getMeasureCount();
            }
        }
        return columnPos;
    }

    private ArrayListMultimap<String, BigDecimal> computeGT(PbReturnObject transData, String gtPos) {
        ArrayListMultimap<String, BigDecimal> gtData = ArrayListMultimap.create();
        String columnName;
        int measCount = getMeasureCount();
        int colsInCrosstab = transData.getColumnCount();
        String[] columnNames = transData.getColumnNames();
        BigDecimal sum;
        BigDecimal cellValue;
        int startPos;
        int endPos;
        int gtColPos;
        int measIndex;

        if (gtPos.equals(ContainerConstants.CROSSTAB_GRANDTOTAL_FIRST)) {
            startPos = viewCount + measCount;
            endPos = colsInCrosstab;
            gtColPos = viewCount;
        } else {
            startPos = viewCount;
            endPos = colsInCrosstab - measCount;
            gtColPos = endPos;
        }


        //loop thru rows
        for (int row = 0; row < transData.getRowCount(); row++) {
            measIndex = startPos;
            //for each row pick all measures
            for (int meas = 0; meas < measCount; meas++, measIndex++) {
                sum = BigDecimal.ZERO;
                for (int col = measIndex; col < endPos; col += measCount) {
                    columnName = columnNames[col];
                    cellValue = transData.getFieldValueBigDecimal(row, columnName);
                    sum = sum.add(cellValue);
                }
                columnName = columnNames[meas + gtColPos];
                gtData.put(columnName, sum);
            }
        }
        return gtData;
    }

    ArrayListMultimap<Integer, Integer> makeColumnSpans() {
        ArrayListMultimap<Integer, Integer> ctColSpans = ArrayListMultimap.create();
        int spans;

        for (int layer = 0; layer < ctColCount; layer++) {
            Set<ColumnGroup> layerColumns = getColumnGroups(layer);
            if (crosstabGT.equals(ContainerConstants.CROSSTAB_GRANDTOTAL_FIRST)) {
                ctColSpans.put(layer, getMeasureCount());
            }
            if (crosstabST.equals(ContainerConstants.CROSSTAB_SUBTOTAL_FIRST)) {
                updateColumnSpansForST(ctColSpans, layerColumns, layer);
            }
            for (ColumnGroup aGroup : layerColumns) {
                if (aGroup.getChildCount() == 0) {
                    spans = getMeasureCount();
                } else {
                    spans = aGroup.getGrandChildrenCount() * getMeasureCount();
                }
                ctColSpans.put(layer, spans);
            }
            if (crosstabST.equals(ContainerConstants.CROSSTAB_SUBTOTAL_LAST)) {
                updateColumnSpansForST(ctColSpans, layerColumns, layer);
            }
            if (crosstabGT.equals(ContainerConstants.CROSSTAB_GRANDTOTAL_LAST)) {
                ctColSpans.put(layer, getMeasureCount());
            }

        }
        return ctColSpans;
    }

    private void updateColumnSpansForST(ArrayListMultimap<Integer, Integer> ctColSpans, Set<ColumnGroup> layerColumns, int layer) {
        int cols;
        Set<ColumnGroup> child;
        Set<ColumnGroup> parents;
        if (crosstabST.equals(ContainerConstants.CROSSTAB_SUBTOTAL_FIRST)) {
            for (int j = 0; j < ctColCount - 1; j++) {

                if (j > 0) {
                    parents = getColumnGroups(j - 1);
                    for (ColumnGroup aParent : parents) {
                        child = aParent.getChildGroups();
                        if (j <= layer) {
                            for (ColumnGroup aChild : child) {
                                ctColSpans.put(layer, getMeasureCount());
                            }
                        } else {
                            ctColSpans.put(layer, child.size() * getMeasureCount());
                        }
                    }
                } else {
                    child = getColumnGroups(j);
                    for (ColumnGroup aGroup : child) {
                        ctColSpans.put(layer, getMeasureCount());
                    }

                }
            }

        } else if (crosstabST.equals(ContainerConstants.CROSSTAB_SUBTOTAL_LAST)) {
            for (int j = ctColCount - 2; j >= 0; j--) {
                if (j > 0) {
                    parents = getColumnGroups(j - 1);
                    for (ColumnGroup aParent : parents) {
                        child = aParent.getChildGroups();
                        if (j <= layer) {
                            for (ColumnGroup aChild : child) {
                                ctColSpans.put(layer, getMeasureCount());
                            }
                        } else {
                            ctColSpans.put(layer, child.size() * getMeasureCount());
                        }
                    }
                } else {
                    child = getColumnGroups(j);
                    for (ColumnGroup aGroup : child) {
                        ctColSpans.put(layer, getMeasureCount());
                    }

                }
            }
        }
    }

    private Set<ColumnGroup> getColumnGroups(int layer) {
        Set<ColumnGroup> layerColumns = new LinkedHashSet<ColumnGroup>();
        if (layer == 0) {
            return colGrps;
        } else {
            for (ColumnGroup group : colGrps) {
                layerColumns.addAll(getColumnGroups(group, layer - 1));
            }
        }
        return layerColumns;
    }

    private Set<ColumnGroup> getColumnGroups(ColumnGroup group, int layer) {
        Set<ColumnGroup> layerColumns = new LinkedHashSet<ColumnGroup>();
        if (layer == 0) {
            return group.getChildGroups();
        } else {
            for (ColumnGroup aGroup : group.getChildGroups()) {
                layerColumns.addAll(getColumnGroups(aGroup, layer - 1));
            }
        }
        return layerColumns;

    }

    private void combineDuplicates(PbReturnObject transData) {
        ArrayList<String> columnName;
        Object[][] data;
        DataSetFilter dataSetFilter = new DataSetFilter();
        SearchFilter filter;
        int actualRow;

        columnName = new ArrayList<String>();
        for (int i = 0; i < viewCount; i++) {
            columnName.add(transData.getColumnNames()[i]);
        }

        data = qryData.retrieveDataBasedOnViewSeq(columnName);
        dataSetFilter.setData(data, qryData.getViewSequence());

        ArrayList<Integer> duplicates = dataSetFilter.filterDuplicates();
        ArrayList<Integer> setOfDuplicates;

        if (duplicates.size() == qryData.getRowCount()) {
            return;
        }

        for (int i = 0; i < duplicates.size(); i++) {
            actualRow = duplicates.get(i);
            if (actualRow == -1) {
                continue;
            }
            filter = new SearchFilter();
            for (int col = 0; col < viewCount; col++) {
                filter.add(columnName.get(col), "EQ", transData.getFieldValueString(actualRow, columnName.get(col)));
            }

            dataSetFilter.setSearchFilter(filter);
            setOfDuplicates = dataSetFilter.searchDataSet();
            combineDuplicates(transData, setOfDuplicates);
            for (int row = 0; row < setOfDuplicates.size(); row++) {
                if (row == 0) {
                    continue;
                }
                for (int dup = 0; dup < duplicates.size(); dup++) {
                    if (duplicates.get(dup) == setOfDuplicates.get(row)) {
                        duplicates.set(dup, -1);
                    }
                }
            }
        }

    }

    private void combineDuplicates(PbReturnObject transData, ArrayList<Integer> setOfDuplicates) {
        int actualRow;
        ArrayList<BigDecimal> subTotal = new ArrayList<BigDecimal>(transData.getColumnCount());
        BigDecimal total;

        for (int i = 0; i < transData.getColumnCount(); i++) {
            subTotal.add(BigDecimal.ZERO);
        }

        for (int row = 0; row < setOfDuplicates.size(); row++) {
            actualRow = setOfDuplicates.get(row);
            for (int col = viewCount; col < transData.getColumnCount(); col++) {
                total = subTotal.get(col);
                total = total.add(transData.getFieldValueBigDecimal(actualRow, col));
                subTotal.set(col, total);
            }
        }

        String[] colNames = transData.getColumnNames();

        for (int col = viewCount; col < transData.getColumnCount(); col++) {
            transData.updateFieldValue(setOfDuplicates.get(0), colNames[col], subTotal.get(col));
        }

        for (int row = 0; row < setOfDuplicates.size(); row++) {
            if (row == 0) {
                continue;
            }
            actualRow = setOfDuplicates.get(row);
            transData.deleteRow(actualRow);
        }

    }

    private void makeDisplayLabelsForColumns() {
        displayLabels = new ArrayList<ArrayList<String>>();
        ArrayList<String> parents;

        for (ColumnGroup parent : colGrps) {
            parents = new ArrayList<String>();
            makeDisplayLabelsForColumns(displayLabels, parent, measNames, parents);
        }
    }

    private ArrayList<ArrayList<String>> makeDisplayLabelsForColumns(ArrayList<ArrayList<String>> displayLabels, ColumnGroup group, ArrayList<String> measureNames, ArrayList<String> parents) {
        ArrayList<ArrayList<String>> chainLabel = null;
        if (group.getChildCount() == 0) {
            chainLabel = new ArrayList<ArrayList<String>>();
            for (String measure : measureNames) {
                ArrayList<String> chain = new ArrayList<String>();
                for (String parent : parents) {
                    chain.add(parent);
                }
                chain.add(group.value.toString());
                chain.add(measure);
                chainLabel.add(chain);
                displayLabels.add(chain);
            }
            return chainLabel;
        }


        for (ColumnGroup child : group.getChildGroups()) {
            ArrayList<String> parentsClone = (ArrayList<String>) parents.clone();
            parentsClone.add(group.value.toString());
            chainLabel = makeDisplayLabelsForColumns(displayLabels, child, measureNames, parentsClone);
        }
        return chainLabel;
    }

    ArrayList getDisplayLabels(ArrayList<String> viewBys) {
        ArrayList reportDisplayLabel = new ArrayList();
        reportDisplayLabel.addAll(viewBys);
        reportDisplayLabel.addAll(displayLabels);
        return reportDisplayLabel;
    }
}
