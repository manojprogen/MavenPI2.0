/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.display.table;

import com.google.common.base.Predicate;

/**
 *
 * @author progen
 */
public class TableCellSpan {

    private String cellId;
    private int rowSpan;
    private int colSpan;

    public TableCellSpan(String cellId, int rowSpan, int colSpan) {
        this.cellId = cellId;
        this.rowSpan = rowSpan;
        this.colSpan = colSpan;
    }

    public TableCellSpan(String cellId) {
        this.cellId = cellId;
    }

    public String getCellId() {
        return this.cellId;
    }

    public int getRowSpan() {
        return this.rowSpan;
    }

    public int getColumnSpan() {
        return this.colSpan;
    }

    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
    }

    public void setColumnSpan(int colSpan) {
        this.colSpan = colSpan;
    }

    @Override
    public String toString() {
        return "[" + "\"" + this.cellId + "\",\"" + this.rowSpan + "\",\"" + this.colSpan + "\"]";
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof TableCellSpan) {
            if (this.cellId.equals(((TableCellSpan) o).getCellId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.cellId != null ? this.cellId.hashCode() : 0);
        return hash;
    }

    public static Predicate getCellIdPredicate(final String cellId) {
        Predicate<TableCellSpan> cellSpanPredicate = new Predicate<TableCellSpan>() {

            public boolean apply(TableCellSpan input) {
                if (cellId.equals(input.getCellId())) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return cellSpanPredicate;
    }
}
