/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.data;

import java.util.ArrayList;

/**
 *
 * @author progen
 */
public class TableDataRow extends TableRow {

    int rowNum;
    ArrayList<Object> rowData;
    ArrayList<String> cssClass;
    ArrayList<String> imagePath;
//    int viewCount;
    boolean printSubTotals = false;
    boolean isSTFoundForFirstViewBy = false;
    int subtotalCount = 0;
    ArrayList<String> colorList;
    ArrayList<String> anchors;
    ArrayList<String> fontColors;
    ArrayList<String> commentList;
    ArrayList<Boolean> editableList;
    ArrayList<String> formulaList;
    String drillAcrossData;
    ArrayList<String> textAlign;
    public ArrayList<String> reportDrillList;
    private ArrayList<String> selfreportDrillList;

    //    HashMap<String, String> drillMap = new HashMap<String, String>();
//
//    public HashMap<String, String> getDrillMap() {
//        return drillMap;
//    }
//
//    public void setDrillMap(HashMap<String, String> drillMap) {
//        this.drillMap = drillMap;
//    }
    public String getDrillAcrossData() {
        return drillAcrossData;
    }

    public void setDrillAcrossData(String drillAcrossData) {
        this.drillAcrossData = drillAcrossData;
    }

    public void setRowNumber(int rowNum) {
        this.rowNum = rowNum;
    }

    public int getRowNumber() {
        return this.rowNum;
    }

    @Override
    public String getID(int column) {
        return rowDataIds.get(column) + "_D_" + rowNum;
    }

    @Override
    public String getRowData(int column) {
        String val = "";
        if (this.rowData.get(column) != null) {
            return this.rowData.get(column).toString();
        } else {
            return val;
        }
    }

    public ArrayList<Object> getRowData() {

        return this.rowData;
    }

    public void setRowData(ArrayList<Object> rowData) {
        this.rowData = rowData;
    }

    public boolean printSubTotals() {
        return this.printSubTotals;
    }

    public void setPrintSubTotals(boolean printSubTotals) {
        this.printSubTotals = printSubTotals;
    }

    public boolean isSubTotalFoundForFirstViewBy() {
        return this.isSTFoundForFirstViewBy;
    }

    public void setSubTotalFoundForFirstViewBy(boolean isSTFoundForFirstViewBy) {
        this.isSTFoundForFirstViewBy = isSTFoundForFirstViewBy;
    }

    public int getSubtotalCount() {
        return this.subtotalCount;
    }

    public void setSubtotalCount(int subtotalCount) {
        this.subtotalCount = subtotalCount;
    }

    public int getColumnCount() {
        return this.rowData.size();
    }

    public String getColor(int column) {
        if (colorList.get(column) != null) {
            return colorList.get(column).toString();
        } else {
            return "";
        }
    }

    public void setColorList(ArrayList<String> colorList) {
        this.colorList = colorList;
    }

    /**
     * @return the cssClass
     */
    public String getCssClass(int index) {
        return cssClass.get(index);
    }

    /**
     * @param cssClass the cssClass to set
     */
    public void setCssClass(ArrayList<String> cssClass) {
        this.cssClass = cssClass;
    }

    public String getAnchors(int column) {
        return anchors.get(column).toString();
    }

    public void setAnchors(ArrayList<String> anchors) {
        this.anchors = anchors;
    }

    public String getFontColors(int column) {
        return fontColors.get(column);
    }

    public void setFontColors(ArrayList<String> fontColors) {
        this.fontColors = fontColors;
    }

    public String getComment(int index) {
        if (commentList == null || index >= commentList.size()) {
            return null;
        }
        return commentList.get(index);
    }

    public void setCommentList(ArrayList<String> commentList) {
        this.commentList = commentList;
    }

    public Boolean isEditable(int index) {
        if (editableList == null || index >= editableList.size()) {
            return null;
        }
        return editableList.get(index);
    }

    public void setEditableList(ArrayList<Boolean> editableList) {
        this.editableList = editableList;
    }

    public ArrayList<String> getFormulaList() {
        return formulaList;
    }

    public void setFormulaList(ArrayList<String> formulaList) {
        this.formulaList = formulaList;
    }

    public String getFormula(int index) {
        String formula = null;
        if (formulaList != null && index < formulaList.size()) {
            formula = formulaList.get(index);
        }
        return formula;
    }

    public ArrayList<String> getImagePath() {
        return imagePath;
    }

    public void setImagePath(ArrayList<String> imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath(int col) {
        if (this.imagePath == null) {
            return null;
        }
        return this.imagePath.get(col);
    }

    public String getTextAlign(int col) {
        if (this.textAlign == null) {
            return null;
        }
        return this.textAlign.get(col);
    }

    public void setTextAlign(ArrayList<String> textAlign) {
        this.textAlign = textAlign;
    }

    public String getMeasrId(int column) {
        return rowDataIds.get(column);
    }

    public String getReportDrillList(int column) {
        String val = "";
        if (this.reportDrillList.get(column) != null) {
            return this.reportDrillList.get(column).toString();
        } else {
            return val;
        }
    }

    public void setReportDrillList(ArrayList<String> reportDrillList) {
        this.reportDrillList = reportDrillList;
    }

    /**
     * @return the selfreportDrillList
     */
    public String getSelfreportDrillList(int column) {
        String val = "";
        if (this.selfreportDrillList != null && !this.selfreportDrillList.isEmpty() && this.selfreportDrillList.get(column) != null) {
            return this.selfreportDrillList.get(column).toString();
        } else {
            return val;
        }
    }

    /**
     * @param selfreportDrillList the selfreportDrillList to set
     */
    public void setSelfreportDrillList(ArrayList<String> selfreportDrillList) {
        this.selfreportDrillList = selfreportDrillList;
    }
}
