/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.excel;

import com.google.common.base.Predicate;
import com.progen.report.ReportParameter;

/**
 *
 * @author progen
 */
public class ExcelCellFormat {

    private int rowNum;
    private String measureId;
    private ExcelCellProperties cellProps;
    private ReportParameter repParam;

    public ExcelCellFormat() {
        this.cellProps = new ExcelCellProperties();
    }

    public ExcelCellFormat(int rowNum, String measureId, ReportParameter repParam) {
        this();
        this.rowNum = rowNum;
        this.measureId = measureId;
        this.repParam = repParam;
    }

    public ExcelCellProperties getCellProps() {
        return cellProps;
    }

    public void setCellProps(ExcelCellProperties cellProps) {
        this.cellProps = cellProps;
    }

    public String getMeasureId() {
        return measureId;
    }

    public void setMeasureId(String measureId) {
        this.measureId = measureId;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public ReportParameter getRepParam() {
        return repParam;
    }

    public void setRepParam(ReportParameter repParam) {
        this.repParam = repParam;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ExcelCellFormat other = (ExcelCellFormat) obj;
        if (this.rowNum == other.rowNum) {
            if (this.measureId != null && other.measureId != null && this.measureId.equals(other.measureId)) {
                if (this.repParam != null && other.repParam != null && this.repParam.equals(other.repParam)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + this.rowNum;
        hash = 61 * hash + (this.measureId != null ? this.measureId.hashCode() : 0);
        hash = 61 * hash + (this.repParam != null ? this.repParam.hashCode() : 0);
        return hash;
    }

    public String getBgColor() {
        return cellProps.getBgColor();
    }

    public void setBgColor(String bgColor) {
        this.cellProps.setBgColor(bgColor);
    }

    public String getComment() {
        return cellProps.getComment();
    }

    public void setComment(String comment) {
        this.cellProps.setComment(comment);
    }

    public String getFontColor() {
        return this.cellProps.getFontColor();
    }

    public void setFontColor(String fontColor) {
        this.cellProps.setFontColor(fontColor);
    }

    public static Predicate<ExcelCellFormat> getReportParameterPredicate(final ReportParameter currentParameter) {
        Predicate<ExcelCellFormat> colorCodePredicate = new Predicate<ExcelCellFormat>() {

            public boolean apply(ExcelCellFormat input) {
                if (currentParameter.equals(input.getRepParam())) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return colorCodePredicate;
    }

    public StringBuilder toXml() {
        if (cellProps.getBgColor() == null && cellProps.getFontColor() == null && cellProps.getComment() == null) {
            return null;
        }
        StringBuilder xml = null;
        xml = new StringBuilder();
        xml.append("<ExcelCell>");
        xml.append("<row>" + rowNum + "</row>");
        xml.append("<measure>" + measureId + "</measure>");
        if (cellProps.getComment() != null) {
            xml.append("<comment>" + cellProps.getComment() + "</comment>");
        }
        if (cellProps.getBgColor() != null) {
            xml.append("<bgColor>" + cellProps.getBgColor() + "</bgColor>");
        }
        if (cellProps.getFontColor() != null) {
            xml.append("<fontColor>" + cellProps.getFontColor() + "</fontColor>");
        }
        xml.append("</ExcelCell>");

        return xml;
    }
}
