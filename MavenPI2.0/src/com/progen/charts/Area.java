/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.charts;

import java.awt.Color;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

/**
 * @filename Area
 *
 * @author santhosh.kumar@progenbusiness.com @date Sep 9, 2009, 1:19:47 PM
 */
public class Area {

    public static void main(String arg[]) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(4.0, "Science", "Rahul");
        dataset.addValue(3.0, "Maths", "Rahul");
        dataset.addValue(5.0, "Science", "Vinod");
        dataset.addValue(2.0, "Maths", "Vinod");
        dataset.addValue(3.0, "Science", "Prashant");
        dataset.addValue(5.0, "Maths", "Prashant");
        dataset.addValue(6.0, "Science", "Tapan");
        dataset.addValue(2.0, "Maths", "Tapan");
        dataset.addValue(3.0, "Science", "Santosh");
        dataset.addValue(5.0, "Maths", "Santosh");



        TimeSeries s1 = new TimeSeries("L&G European Index Trust", Month.class);
        s1.add(new Month(2, 2001), 181.8);
        s1.add(new Month(3, 2001), 167.3);
        s1.add(new Month(4, 2001), 153.8);
        s1.add(new Month(5, 2001), 167.6);
        s1.add(new Month(6, 2001), 158.8);
        s1.add(new Month(7, 2001), 148.3);
        s1.add(new Month(8, 2001), 153.9);
        s1.add(new Month(9, 2001), 142.7);
        s1.add(new Month(10, 2001), 123.2);
        s1.add(new Month(11, 2001), 131.8);
        s1.add(new Month(12, 2001), 139.6);
        s1.add(new Month(1, 2002), 142.9);
        s1.add(new Month(2, 2002), 138.7);
        s1.add(new Month(3, 2002), 137.3);
        s1.add(new Month(4, 2002), 143.9);
        s1.add(new Month(5, 2002), 139.8);
        s1.add(new Month(6, 2002), 137.0);
        s1.add(new Month(7, 2002), 132.8);
        TimeSeries s2 = new TimeSeries("L&G UK Index Trust", Month.class);
        s2.add(new Month(2, 2001), 129.6);
        s2.add(new Month(3, 2001), 123.2);
        s2.add(new Month(4, 2001), 117.2);
        s2.add(new Month(5, 2001), 124.1);
        s2.add(new Month(6, 2001), 122.6);
        s2.add(new Month(7, 2001), 119.2);
        s2.add(new Month(8, 2001), 116.5);
        s2.add(new Month(9, 2001), 112.7);
        s2.add(new Month(10, 2001), 101.5);
        s2.add(new Month(11, 2001), 106.1);
        s2.add(new Month(12, 2001), 110.3);
        s2.add(new Month(1, 2002), 111.7);
        s2.add(new Month(2, 2002), 111.0);
        s2.add(new Month(3, 2002), 109.6);
        s2.add(new Month(4, 2002), 113.2);
        s2.add(new Month(5, 2002), 111.6);
        s2.add(new Month(6, 2002), 108.8);
        s2.add(new Month(7, 2002), 101.6);
        TimeSeriesCollection dataset1 = new TimeSeriesCollection();
        dataset1.addSeries(s1);
        dataset1.addSeries(s2);

        JFreeChart chart1 = ChartFactory.createTimeSeriesChart(
                "Legal & General Unit Trust Prices", // title
                "Date",
                // x-axis label
                "Price Per Unit",
                // y-axis label
                dataset1,
                // data
                true,
                // create legend?
                true,
                // generate tooltips?
                false // generate URLs?
                );


        JFreeChart chart = ChartFactory.createAreaChart("Comparison between Students Marks", "Students", "Marks ", dataset, PlotOrientation.VERTICAL, true, true, false);
        chart1.setBackgroundPaint(Color.yellow);
        chart1.getTitle().setPaint(Color.blue);
//        CategoryPlot p = chart1.getCategoryPlot();
//        p.setForegroundAlpha(0.7f);
//        p.setRangeGridlinePaint(Color.red);
//        p.setDomainGridlinesVisible(true);
//        p.setDomainGridlinePaint(Color.black);
//        CategoryItemRenderer renderer = p.getRenderer();
//        renderer.setSeriesPaint(1, Color.red);
//        renderer.setSeriesPaint(0, Color.green);
        ChartFrame frame1 = new ChartFrame("Area Chart", chart1);
        frame1.setVisible(true);
        frame1.setSize(300, 300);
    }
}
