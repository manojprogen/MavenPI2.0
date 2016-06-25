/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.charts;

import java.math.BigDecimal;
import java.text.NumberFormat;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.data.general.PieDataset;

/**
 *
 * @author progen
 */
public class PieLabelGenerator extends StandardPieSectionLabelGenerator {

    BigDecimal totalValue;

    PieLabelGenerator(String labelFormat, NumberFormat numberFormat, NumberFormat percentFormat, BigDecimal totalValue) {
        super(labelFormat, numberFormat, percentFormat);
        this.totalValue = totalValue;
    }

    @Override
    public String generateSectionLabel(PieDataset dataset, Comparable key) {
        long percent;
        percent = Math.round((dataset.getValue(key).doubleValue() / totalValue.doubleValue()) * 100);
        if (percent > 2) {
            return super.generateSectionLabel(dataset, key);
        } else {
            return "";
        }
    }
}
