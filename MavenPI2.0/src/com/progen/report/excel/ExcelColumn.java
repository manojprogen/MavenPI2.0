/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.excel;

import com.google.common.base.Predicate;
import com.progen.report.ReportParameter;
import java.util.ArrayList;

/**
 *
 * @author progen
 */
public class ExcelColumn {

    ArrayList data;
    ReportParameter repParam;

    public ExcelColumn() {
        data = new ArrayList();
    }

    public ArrayList getData() {
        return data;
    }

    public void setData(ArrayList data) {
        this.data = data;
    }

    public void setData(ArrayList newData, int fromRow, int toRow) {
        if (data == null || data.isEmpty()) {
            data = new ArrayList(toRow);
            for (int i = 0; i < toRow; i++) {
                data.add("");
            }
        }
        if (data.size() < toRow) {
            for (int i = data.size(); i < toRow; i++) {
                data.add("");
            }
        }
        int index = 0;
        for (int i = fromRow; i < toRow; i++) {
            data.set(i, newData.get(index));
            index++;
        }
    }

    public void setData(Object newData, int row) {
        if (data == null || data.isEmpty()) {
            data = new ArrayList(row);
            for (int i = 0; i < row; i++) {
                data.add("");
            }
        }
        if (data.size() < row) {
            for (int i = data.size(); i < row; i++) {
                data.add("");
            }
        }
        data.set(row, newData);
    }

    public ReportParameter getRepParam() {
        return repParam;
    }

    public void setRepParam(ReportParameter repParam) {
        this.repParam = repParam;
    }

    public StringBuilder toXml() {
        StringBuilder xml = new StringBuilder();
        xml.append("<ExcelColumn>");
        for (int i = 0; i < data.size(); i++) {
            xml.append("<data>").append(data.get(i)).append("</data>");
        }
        xml.append("</ExcelColumn>");

        return xml;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ExcelColumn other = (ExcelColumn) obj;
        if (this.repParam != other.repParam && (this.repParam == null || !this.repParam.equals(other.repParam))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (this.repParam != null ? this.repParam.hashCode() : 0);
        return hash;
    }

    public static Predicate<ExcelColumn> getReportParameterPredicate(final ReportParameter currentParameter) {
        Predicate<ExcelColumn> colorCodePredicate = new Predicate<ExcelColumn>() {

            public boolean apply(ExcelColumn input) {
                if (currentParameter.equals(input.getRepParam())) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return colorCodePredicate;
    }
}