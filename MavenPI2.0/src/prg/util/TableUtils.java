/*
 * TableUtils.java
 *
 * Created on May 7, 2009, 6:56 PM
 *@author : K.Santhosh Kumar
 * 
 */
package prg.util;

/**
 *
 * @author Administrator
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import org.apache.log4j.Logger;
import prg.db.PbReturnObject;

/**
 *
 * @author Administrator
 */
public class TableUtils implements Serializable {
    //ArrayList columnsList;

    public static Logger logger = Logger.getLogger(TableUtils.class);
    private boolean tableSortable = true;
    private boolean showStatusBar = true;
    private boolean showTooltips = true;
    private boolean filterable = false;
    private boolean showExports = true;
    private boolean showTitle = true;
    private boolean showPagination = true;
    private int totalRecordsSize;
    private int rowsDisplayed = 10;
    private int medianRowsDisplayed = 15;
    private int filterRowsDisplayed = 10;
    private int columnCount = 0;
    private int width = 100;
    private String action = "";
    private String title = "";
    private String items = "Records";
    private String var = "currentList";
    private String fileName = "PrgFile";
    private String headerTitle = "Title";
    private String headerColor = "white";
    private String imagePath = "/images/extreme/*.gif";
    private String tableDesign;
    private String forwardTo;
    private StringBuffer BuildTable;
    private int[] columnWidths;
    private boolean[] columnSortable;
    private boolean[] columnFilterble;
    private boolean[] checkboxDisabled;
    private String[] columnNames;
    private String[] tableColumnsNames;
    private String[] columnsVisibility;
    private String[] columnTypes;
    private String[] columnURLS;
    private String[] columnURLSValues;
    private String[] defaultChecked;
    private String[] checkBoxNames;
    private ArrayList currentDisplayedRecords = new ArrayList();
    private ArrayList columnDetails;
    private ArrayList totalRecords;
    private HashMap singleRecordDetails;
    private PbReturnObject pbReturnObject;
    private PageContext pageContext;
    static TableUtils TableUtilsObj;

    public String[] getCheckBoxNames() {
        return checkBoxNames;
    }

    public void setCheckBoxNames(String[] checkBoxNames) {
        this.checkBoxNames = checkBoxNames;
    }

    public boolean[] getCheckboxDisabled() {
        return checkboxDisabled;
    }

    public void setCheckboxDisabled(boolean[] checkboxDisabled) {
        this.checkboxDisabled = checkboxDisabled;
    }

    public String[] getColumnURLSValues() {
        return columnURLSValues;
    }

    public void setColumnURLSValues(String[] columnURLSValues) {
        this.columnURLSValues = columnURLSValues;
    }

    public void setDefaultChecked(String[] defaultChecked) {
        this.defaultChecked = defaultChecked;
    }

    public String[] getDefaultChecked() {
        return defaultChecked;
    }

    public void setForwardTo(String forwardTo) {
        this.forwardTo = forwardTo;
    }

    public String getForwardTo() {
        if (forwardTo == null || "".equalsIgnoreCase(forwardTo)) {
            setForwardTo(action);
        }
        return forwardTo;
    }

    public void setColumnTypes(String[] columnTypes) {
        this.columnTypes = columnTypes;
    }

    public String[] getColumnTypes() {
        return columnTypes;
    }

    public void setColumnURLS(String[] columnURLS) {
        this.columnURLS = columnURLS;
    }

    public String[] getColumnURLS() {
        return columnURLS;
    }

    public void setPbReturnObject(PbReturnObject pbReturnObject) {
        this.pbReturnObject = pbReturnObject;
    }

    public PbReturnObject getPbReturnObject() {
        return pbReturnObject;
    }

    public void setTableColumnsNames(String[] tableColumnsNames) {
        this.tableColumnsNames = tableColumnsNames;
    }

    public String[] getTableColumnsNames() {
        return tableColumnsNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
        this.columnCount = columnNames.length;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnWidths(int[] columnWidths) {
        this.columnWidths = columnWidths;
    }

    public int[] getColumnWidths() {
        return columnWidths;
    }

    public void setColumnSortable(boolean[] columnSortable) {
        this.columnSortable = columnSortable;
    }

    public boolean[] getColumnSortable() {
        return columnSortable;
    }

    public void setColumnFilterble(boolean[] columnFilterble) {
        this.columnFilterble = columnFilterble;
    }

    public boolean[] getColumnFilterble() {
        return columnFilterble;
    }

    public void setPageContext(PageContext pageContext) {
        this.pageContext = pageContext;
    }

    public PageContext getPageContext() {
        return pageContext;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getItems() {
        return items;
    }

    public void setShowPagination(boolean showPagination) {
        this.showPagination = showPagination;
    }

    public boolean getShowPagination() {
        return showPagination;
    }

    public void setImagePath(String imagePath) {
        if (this.imagePath == null || this.imagePath.equalsIgnoreCase("")) {
            imagePath = ((HttpServletRequest) getPageContext().getRequest()).getContextPath() + "/images/extreme/*.gif";
            //imagePath="/images/extreme/*.gif";
        }
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-----imagePath-------"+imagePath);
        return imagePath;
    }

    public void setColumnsVisibility(String[] columnsVisibility) {
        this.columnsVisibility = columnsVisibility;
    }

    public String[] getColumnsVisibility() {
        return columnsVisibility;
    }

    public void setCurrentDisplayedRecords(ArrayList currentDisplayedRecords) {
        this.currentDisplayedRecords = currentDisplayedRecords;
    }

    public ArrayList getCurrentDisplayedRecords() {
        return currentDisplayedRecords;
    }

    public void setTotalRecords(ArrayList totalRecords) {
        this.totalRecords = totalRecords;
    }

    public ArrayList getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecordsSize(int totalRecordsSize) {
        this.totalRecordsSize = totalRecordsSize;
        // if(totalRecordsSize<=10)
        //{           
        //   setRowsDisplayed(totalRecordsSize);         
        // }
        if (totalRecordsSize > 10 && totalRecordsSize < 15) {
            setMedianRowsDisplayed(totalRecordsSize);
        }
    }

    public int getTotalRecordsSize() {
        setTotalRecordsSize(totalRecords.size());
        return totalRecords.size();
    }

    public void setRowsDisplayed(int rowsDisplayed) {
        this.rowsDisplayed = rowsDisplayed;
    }

    public int getRowsDisplayed() {
        return rowsDisplayed;
    }

    public void setMedianRowsDisplayed(int medianRowsDisplayed) {
        this.medianRowsDisplayed = medianRowsDisplayed;
    }

    public int getMedianRowsDisplayed() {
        return medianRowsDisplayed;
    }

    public void setfilterRowsDisplayed(int filterRowsDisplayed) {
        this.filterRowsDisplayed = filterRowsDisplayed;
    }

    public int getFilterRowsDisplayed() {
        return filterRowsDisplayed;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public int getColumnCount() {
        //singleRecordDetails=(HashMap)getColumnDetails().get(0);
        if (columnCount != 0) {
            setColumnCount(columnNames.length);
        }
        return columnCount;
    }

    public void setAction(String action) {
        this.action = action;

    }

    public String getAction() {
        return action;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTableSortable(boolean tableSortable) {
        this.tableSortable = tableSortable;
    }

    public boolean getTableSortable() {
        return tableSortable;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getVar() {
        return var;
    }

    public void setShowStatusBar(boolean showStatusBar) {
        this.showStatusBar = showStatusBar;
    }

    public boolean getShowStatusBar() {
        return showStatusBar;
    }

    public void setShowTooltips(boolean showTooltips) {
        this.showTooltips = showTooltips;
    }

    public boolean getShowTooltips() {
        return showTooltips;
    }

    public void setFilterable(boolean filterable) {
        this.filterable = filterable;
    }

    public boolean getFilterable() {
        return filterable;
    }

    public void setShowExports(boolean showExports) {
        this.showExports = showExports;
    }

    public boolean getShowExports() {
        return showExports;
    }

    public void setShowTitle(boolean showTitle) {
        this.showTitle = showTitle;
    }

    public boolean getShowTitle() {
        return showTitle;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setColumnDetails(ArrayList columnDetails) {
        this.columnDetails = columnDetails;
    }

    public ArrayList getColumnDetails() {
        return columnDetails;
    }

    public void setFileName(String v) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderColor(String headerColor) {
        this.headerColor = headerColor;
    }

    public String getHeaderColor() {
        return headerColor;
    }

    public void setDefaults() {

        columnWidths = new int[getColumnCount()];
        columnsVisibility = new String[getColumnCount()];
        columnSortable = new boolean[getColumnCount()];
        ;
        columnFilterble = new boolean[getColumnCount()];
        ;
        checkboxDisabled = new boolean[getColumnCount()];
        ;
        columnURLS = new String[getColumnCount()];
        columnURLSValues = new String[getColumnCount()];
        columnTypes = new String[getColumnCount()];
        checkBoxNames = new String[getColumnCount()];

        for (int colNum = 0; colNum < getColumnCount(); colNum++) {
            // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("--colNum----"+colNum);
            columnWidths[colNum] = (100 / getColumnCount());
            columnsVisibility[colNum] = "show";
            columnSortable[colNum] = true;
            columnFilterble[colNum] = true;
            checkboxDisabled[colNum] = false;
            // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-----checkboxDisabled[colNum]----- "+checkboxDisabled[colNum]);
            columnURLS[colNum] = "";
            columnURLSValues[colNum] = "";
            columnTypes[colNum] = "";
            checkBoxNames[colNum] = "";
        }
    }

    public void execute() {
        try {
            totalRecords = new ArrayList();

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("--------checkboxDisabled--------"+checkboxDisabled);

            for (int rowNo = 0; rowNo < pbReturnObject.getRowCount(); rowNo++) {
                singleRecordDetails = new HashMap();
                for (int columnNo = 0; columnNo < tableColumnsNames.length; columnNo++) {
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("---tableColumnsNames.length----"+tableColumnsNames.length);
                    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("--columnNo---"+columnNo+"-------tableColumnsNames[columnNo]---------"+tableColumnsNames[columnNo]+"-------");
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("----pbReturnObject.getFieldValue(rowNo,OTHERS)-----"+pbReturnObject.getFieldValue(rowNo,tableColumnsNames[columnNo].trim().toUpperCase())+"------");
                    Object obj = pbReturnObject.getFieldValue(rowNo, tableColumnsNames[columnNo].trim());
                    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("---obj--"+obj);

                    if (obj instanceof String) {
                        // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------------Its String------------- "+obj);
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.print("---columnTypes[columnNo]--- "+columnTypes);
                        if (columnTypes[columnNo].equalsIgnoreCase("checkbox")) {
                            if (defaultChecked.length != 0) {
                                for (int checkedIndex = 0; checkedIndex < defaultChecked.length; checkedIndex++) {
                                    if (obj.toString().equalsIgnoreCase(defaultChecked[checkedIndex])) {
                                        // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("------checkboxDisabled[columnNo]-----String------------"+checkboxDisabled[columnNo] +"------columnNo-----------"+columnNo);
                                        if (checkboxDisabled[columnNo] == true) {
                                            singleRecordDetails.put(tableColumnsNames[columnNo], "<input disabled type='checkbox' checked name='" + checkBoxNames[columnNo] + "' value='" + obj.toString() + "'>");
                                        } else {
                                            singleRecordDetails.put(tableColumnsNames[columnNo], "<input type='checkbox' checked name='" + checkBoxNames[columnNo] + "' value='" + obj.toString() + "'>");
                                        }
                                    }
                                }
                                if (singleRecordDetails.get(tableColumnsNames[columnNo]) == null) {
                                    singleRecordDetails.put(tableColumnsNames[columnNo], "<input type='checkbox' name='" + checkBoxNames[columnNo] + "' value='" + obj.toString() + "'>");
                                    if (checkboxDisabled[columnNo] == true) {
                                        singleRecordDetails.put(tableColumnsNames[columnNo], "<input disabled type='checkbox'  name='" + checkBoxNames[columnNo] + "' value='" + obj.toString() + "'>");
                                    } else {
                                        singleRecordDetails.put(tableColumnsNames[columnNo], "<input type='checkbox'  name='" + checkBoxNames[columnNo] + "' value='" + obj.toString() + "'>");
                                    }
                                }

                            } else {
                                if (checkboxDisabled[columnNo] == true) {
                                    singleRecordDetails.put(tableColumnsNames[columnNo], "<input disabled type='checkbox'  name='" + checkBoxNames[columnNo] + "' value='" + obj.toString() + "'>");
                                } else {
                                    singleRecordDetails.put(tableColumnsNames[columnNo], "<input type='checkbox'  name='" + checkBoxNames[columnNo] + "' value='" + obj.toString() + "'>");
                                }
                            }
                        } else if (columnTypes[columnNo].equalsIgnoreCase("url")) {
                            singleRecordDetails.put(tableColumnsNames[columnNo], "<a href='" + columnURLS[columnNo] + pbReturnObject.getFieldValue(rowNo, columnURLSValues[columnNo]) + "'>" + obj.toString() + "</a>");

                        } else if (columnTypes[columnNo].equalsIgnoreCase("radio")) {
                            singleRecordDetails.put(tableColumnsNames[columnNo], obj.toString());
                        } else if (columnTypes[columnNo].equalsIgnoreCase("hidden")) {
                            singleRecordDetails.put(tableColumnsNames[columnNo], "<input type='hidden' name='hidn2' value='" + obj.toString() + "'>");
                        } else {
                            singleRecordDetails.put(tableColumnsNames[columnNo], obj.toString());
                        }
                    } else if (obj instanceof java.util.Date) {
                        //   //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------------Its Date------------- "+obj);
                        if (columnTypes[columnNo].equalsIgnoreCase("checkbox")) {
                            if (defaultChecked.length != 0) {
                                for (int checkedIndex = 0; checkedIndex < defaultChecked.length; checkedIndex++) {
                                    if (obj.toString().equalsIgnoreCase(defaultChecked[checkedIndex])) {
                                        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("------checkboxDisabled[columnNo]-------Date----------"+checkboxDisabled[columnNo] +"------columnNo-----------"+columnNo);
                                        if (checkboxDisabled[columnNo] == true) {
                                            singleRecordDetails.put(tableColumnsNames[columnNo], "<input type='checkbox' disabled checked name='" + checkBoxNames[columnNo] + "' value='" + pbReturnObject.getFieldValueDateString(rowNo, tableColumnsNames[columnNo]) + "'>");
                                        } else {
                                            singleRecordDetails.put(tableColumnsNames[columnNo], "<input type='checkbox' checked name='" + checkBoxNames[columnNo] + "' value='" + pbReturnObject.getFieldValueDateString(rowNo, tableColumnsNames[columnNo]) + "'>");
                                        }
                                        // singleRecordDetails.put(tableColumnsNames[columnNo],"<input type='checkbox' checked name='"+checkBoxNames[columnNo]+"' value='"+pbReturnObject.getFieldValueDateString(rowNo,tableColumnsNames[columnNo])+"'>");
                                    }
                                }
                                if (singleRecordDetails.get(tableColumnsNames[columnNo]) == null) {
                                    //singleRecordDetails.put(tableColumnsNames[columnNo],"<input type='checkbox' name='"+checkBoxNames[columnNo]+"' value='"+pbReturnObject.getFieldValueDateString(rowNo,tableColumnsNames[columnNo])+"'>");
                                    if (checkboxDisabled[columnNo] == true) {
                                        singleRecordDetails.put(tableColumnsNames[columnNo], "<input type='checkbox' disabled  name='" + checkBoxNames[columnNo] + "' value='" + pbReturnObject.getFieldValueDateString(rowNo, tableColumnsNames[columnNo]) + "'>");
                                    } else {
                                        singleRecordDetails.put(tableColumnsNames[columnNo], "<input type='checkbox' name='" + checkBoxNames[columnNo] + "' value='" + pbReturnObject.getFieldValueDateString(rowNo, tableColumnsNames[columnNo]) + "'>");
                                    }
                                }
                            } else {
                                if (checkboxDisabled[columnNo] == true) {
                                    singleRecordDetails.put(tableColumnsNames[columnNo], "<input type='checkbox' disabled  name='" + checkBoxNames[columnNo] + "' value='" + pbReturnObject.getFieldValueDateString(rowNo, tableColumnsNames[columnNo]) + "'>");
                                } else {
                                    singleRecordDetails.put(tableColumnsNames[columnNo], "<input type='checkbox' name='" + checkBoxNames[columnNo] + "' value='" + pbReturnObject.getFieldValueDateString(rowNo, tableColumnsNames[columnNo]) + "'>");
                                }

                            }
                        } else if (columnTypes[columnNo].equalsIgnoreCase("url")) {
                            singleRecordDetails.put(tableColumnsNames[columnNo], "<a href='" + columnURLS[columnNo] + pbReturnObject.getFieldValue(rowNo, columnURLSValues[columnNo]) + "'>" + pbReturnObject.getFieldValueDateString(rowNo, tableColumnsNames[columnNo]) + "</a>");
                            //singleRecordDetails.put(tableColumnsNames[columnNo],pbReturnObject.getFieldValueDateString(rowNo,tableColumnsNames[columnNo]));
                        } else if (columnTypes[columnNo].equalsIgnoreCase("radio")) {
                            singleRecordDetails.put(tableColumnsNames[columnNo], pbReturnObject.getFieldValueDateString(rowNo, tableColumnsNames[columnNo]));
                        } else if (columnTypes[columnNo].equalsIgnoreCase("hidden")) {
                            singleRecordDetails.put(tableColumnsNames[columnNo], "<input type='hidden' name='hidn2' value='" + pbReturnObject.getFieldValueDateString(rowNo, tableColumnsNames[columnNo]) + "'>");
                        } else {
                            singleRecordDetails.put(tableColumnsNames[columnNo], pbReturnObject.getFieldValueDateString(rowNo, tableColumnsNames[columnNo]));
                        }
                    } else if (obj instanceof Integer) {
                        // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------------Its Integer------------- "+obj);
                        if (columnTypes[columnNo].equalsIgnoreCase("checkbox")) {
                            if (defaultChecked.length != 0) {
                                for (int checkedIndex = 0; checkedIndex < defaultChecked.length; checkedIndex++) {
                                    if (obj.toString().equalsIgnoreCase(defaultChecked[checkedIndex])) {
                                        // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("------checkboxDisabled[columnNo]--------Integer---------"+checkboxDisabled[columnNo] +"------columnNo-----------"+columnNo);
                                        if (checkboxDisabled[columnNo] == true) {
                                            singleRecordDetails.put(tableColumnsNames[columnNo], "<input type='checkbox' disabled checked name='" + checkBoxNames[columnNo] + "' value='" + Integer.valueOf(pbReturnObject.getFieldValueInt(rowNo, tableColumnsNames[columnNo])) + "'>");
                                        } else {
                                            singleRecordDetails.put(tableColumnsNames[columnNo], "<input type='checkbox' checked name='" + checkBoxNames[columnNo] + "' value='" + Integer.valueOf(pbReturnObject.getFieldValueInt(rowNo, tableColumnsNames[columnNo])) + "'>");
                                        }

                                    }
                                }
                                if (singleRecordDetails.get(tableColumnsNames[columnNo]) == null) {
                                    if (checkboxDisabled[columnNo] == true) {
                                        singleRecordDetails.put(tableColumnsNames[columnNo], "<input type='checkbox' disabled  name='" + checkBoxNames[columnNo] + "' value='" + Integer.valueOf(pbReturnObject.getFieldValueInt(rowNo, tableColumnsNames[columnNo])) + "'>");
                                    } else {
                                        singleRecordDetails.put(tableColumnsNames[columnNo], "<input type='checkbox'  name='" + checkBoxNames[columnNo] + "' value='" + Integer.valueOf(pbReturnObject.getFieldValueInt(rowNo, tableColumnsNames[columnNo])) + "'>");
                                    }
                                    //singleRecordDetails.put(tableColumnsNames[columnNo],"<input type='checkbox' name='"+checkBoxNames[columnNo]+"' value='"+Integer.valueOf(pbReturnObject.getFieldValueInt(rowNo,tableColumnsNames[columnNo]))+"'>"); 
                                }
                            } else {
                                if (checkboxDisabled[columnNo] == true) {
                                    singleRecordDetails.put(tableColumnsNames[columnNo], "<input type='checkbox' disabled  name='" + checkBoxNames[columnNo] + "' value='" + Integer.valueOf(pbReturnObject.getFieldValueInt(rowNo, tableColumnsNames[columnNo])) + "'>");
                                } else {
                                    singleRecordDetails.put(tableColumnsNames[columnNo], "<input type='checkbox'  name='" + checkBoxNames[columnNo] + "' value='" + Integer.valueOf(pbReturnObject.getFieldValueInt(rowNo, tableColumnsNames[columnNo])) + "'>");
                                }
                            }
                        } else if (columnTypes[columnNo].equalsIgnoreCase("url")) {
                            singleRecordDetails.put(tableColumnsNames[columnNo], "<a href='" + columnURLS[columnNo] + pbReturnObject.getFieldValue(rowNo, columnURLSValues[columnNo]) + "'>" + Integer.valueOf(pbReturnObject.getFieldValueInt(rowNo, tableColumnsNames[columnNo])) + "</a>");
                            //singleRecordDetails.put(tableColumnsNames[columnNo],Integer.valueOf(pbReturnObject.getFieldValueInt(rowNo,tableColumnsNames[columnNo]))+"'>"+tableColumnsNames[columnNo].replace('_',' '));
                        } else if (columnTypes[columnNo].equalsIgnoreCase("radio")) {
                            singleRecordDetails.put(tableColumnsNames[columnNo], Integer.valueOf(pbReturnObject.getFieldValueInt(rowNo, tableColumnsNames[columnNo])));
                        } else if (columnTypes[columnNo].equalsIgnoreCase("hidden")) {
                            singleRecordDetails.put(tableColumnsNames[columnNo], "<input type='hidden' name='hidn2' value='" + Integer.valueOf(pbReturnObject.getFieldValueInt(rowNo, tableColumnsNames[columnNo])) + "'>");
                        } else {
                            singleRecordDetails.put(tableColumnsNames[columnNo], Integer.valueOf(pbReturnObject.getFieldValueInt(rowNo, tableColumnsNames[columnNo])));
                        }
                    } else {
                        //  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------------Its Object------------- "+obj);
                        if (columnTypes[columnNo].equalsIgnoreCase("checkbox")) {
                            if (defaultChecked.length != 0) {
                                for (int checkedIndex = 0; checkedIndex < defaultChecked.length; checkedIndex++) {
                                    if (obj.toString().equalsIgnoreCase(defaultChecked[checkedIndex])) {
                                        // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("------------Its Checked------------value is "+obj.toString()+" ----checkedIndex "+checkedIndex+" ---- "+defaultChecked[checkedIndex]);
                                        // singleRecordDetails.put(tableColumnsNames[columnNo],"<input type='checkbox' checked name='"+checkBoxNames[columnNo]+"' value='"+obj.toString()+"'>");
                                        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("------checkboxDisabled[columnNo]------obj-----------"+checkboxDisabled[columnNo] +"------columnNo-----------"+columnNo);
                                        if (checkboxDisabled[columnNo] == true) {
                                            singleRecordDetails.put(tableColumnsNames[columnNo], "<input type='checkbox' checked disabled  name='" + checkBoxNames[columnNo] + "' value='" + obj.toString() + "'>");
                                        } else {
                                            singleRecordDetails.put(tableColumnsNames[columnNo], "<input type='checkbox' checked  name='" + checkBoxNames[columnNo] + "' value='" + obj.toString() + "'>");
                                        }
                                    }
                                }
                                if (singleRecordDetails.get(tableColumnsNames[columnNo]) == null) {
                                    if (checkboxDisabled[columnNo] == true) {
                                        singleRecordDetails.put(tableColumnsNames[columnNo], "<input type='checkbox' disabled  name='" + checkBoxNames[columnNo] + "' value='" + obj.toString() + "'>");
                                    } else {
                                        singleRecordDetails.put(tableColumnsNames[columnNo], "<input type='checkbox'  name='" + checkBoxNames[columnNo] + "' value='" + obj.toString() + "'>");
                                    }
                                    //singleRecordDetails.put(tableColumnsNames[columnNo],"<input type='checkbox' name='"+checkBoxNames[columnNo]+"' value='"+obj.toString()+"'>");  
                                }

                            } else {
                                //singleRecordDetails.put(tableColumnsNames[columnNo],"<input type='checkbox' name='"+checkBoxNames[columnNo]+"' value='"+obj.toString()+"'>");
                                if (checkboxDisabled[columnNo] == true) {
                                    singleRecordDetails.put(tableColumnsNames[columnNo], "<input type='checkbox' disabled  name='" + checkBoxNames[columnNo] + "' value='" + obj.toString() + "'>");
                                } else {
                                    singleRecordDetails.put(tableColumnsNames[columnNo], "<input type='checkbox'  name='" + checkBoxNames[columnNo] + "' value='" + obj.toString() + "'>");
                                }
                            }

                        } else if (columnTypes[columnNo].equalsIgnoreCase("url")) {
                            singleRecordDetails.put(tableColumnsNames[columnNo], "<a href='" + columnURLS[columnNo] + pbReturnObject.getFieldValue(rowNo, columnURLSValues[columnNo]) + "'>" + obj.toString() + "</a>");
                            //singleRecordDetails.put(tableColumnsNames[columnNo],obj);
                        } else if (columnTypes[columnNo].equalsIgnoreCase("radio")) {
                            singleRecordDetails.put(tableColumnsNames[columnNo], obj);
                        } else if (columnTypes[columnNo].equalsIgnoreCase("hidden")) {
                            singleRecordDetails.put(tableColumnsNames[columnNo], "<input type='hidden' name='hidn2' value='" + obj + "'>");
                        } else {
                            singleRecordDetails.put(tableColumnsNames[columnNo], obj);
                        }
                    }
                }//closing of inner for loop
                totalRecords.add(singleRecordDetails);
            }//closing of outer for loop
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("=========Total records======================"+totalRecords);
            setTotalRecords(totalRecords);


        } catch (Exception exception) {
            logger.error("Exception: ", exception);
        }
    }//closing of execute()

    public static TableUtils getTableUtils() {
        if (TableUtilsObj == null) {
            TableUtilsObj = new TableUtils();
            return TableUtilsObj;
        } else {
            return TableUtilsObj;
        }
    }
}