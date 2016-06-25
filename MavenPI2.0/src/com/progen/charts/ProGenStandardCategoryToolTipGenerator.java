/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.charts;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.data.category.CategoryDataset;

/**
 *
 * @author santhosh.kumar@progenbusiness.com
 */
public class ProGenStandardCategoryToolTipGenerator extends StandardCategoryToolTipGenerator {

    private String postFix = "";

    public ProGenStandardCategoryToolTipGenerator() {
        super(DEFAULT_TOOL_TIP_FORMAT_STRING, NumberFormat.getInstance());
    }

    public ProGenStandardCategoryToolTipGenerator(String labelFormat, NumberFormat formatter) {
        super(labelFormat, formatter);
    }

    public ProGenStandardCategoryToolTipGenerator(String labelFormat, DateFormat formatter) {
        super(labelFormat, formatter);
    }

    @Override
    public String generateToolTip(CategoryDataset dataset, int row, int column) {
        return generateLabelString(dataset, row, column);
    }

    @Override
    public String generateLabelString(CategoryDataset dataset, int row, int column) {
        if (dataset == null) {
            throw new IllegalArgumentException("Null 'dataset' argument.");
        }
        String result = null;
        Object[] items = createItemArray(dataset, row, column);
        result = MessageFormat.format(getLabelFormat(), items);
//        result = getFormatedToolTip(items);
        return result + this.getPostFix();
    }

    public String getFormatedToolTip(Object[] items) {
        StringBuffer result = new StringBuffer("");
        if (items != null) {
            if (getNumberFormat() != null) {
                result.append("(" + items[0] + "," + items[1] + ")=(" + items[2] + "," + items[3] + ")");
//                result.append("(" + items[0] + "," + items[1] + ")=(" + getNumberFormat().format(items[2]) + "," + items[3] + ")");
            } else {
                result.append("(" + items[0] + "," + items[1] + ")=(" + items[2] + "," + items[3] + ")");
            }
        }
        return result.toString();
    }

    //added for appending % symbol in percentageStackedGraph.
    public String getPostFix() {
        return postFix;
    }

    public void setPostFix(String postFix) {
        this.postFix = postFix;
    }
    /*
     * public Object[] createItemArray(CategoryDataset dataset,int row, int
     * column) { Object[] result = new Object[4]; result[0] =
     * dataset.getRowKey(row).toString(); result[1] =
     * dataset.getColumnKey(column).toString(); Number value =
     * dataset.getValue(row, column); if (value != null) { if
     * (this.getNumberFormat() != null) { result[2] =
     * this.getNumberFormat().format(value); } else if (this.getDateFormat() !=
     * null) { result[2] = this.getDateFormat().format(value); } } else {
     * result[2] = this.nullValueString; } if (value != null) { double total =
     * DataUtilities.calculateColumnTotal(dataset, column); double percent =
     * value.doubleValue() / total; result[3] =
     * this.percentFormat.format(percent); } return result; }
     */
}
