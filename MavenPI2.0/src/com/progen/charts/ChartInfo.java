/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.charts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.Writer;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import prg.db.PbReturnObject;

/**
 *
 * @author arun
 */
public class ChartInfo {

    public static Logger logger = Logger.getLogger(ChartInfo.class);

    public JFreeChart generateChartDataSet(PbReturnObject retObj, String measure, int measColumn,
            String xAxisLabel, String yAxisLabel, boolean displayXAxisTicks, boolean displayYAxisTicks) {
        JFreeChart chart = null;
        Color[] colors = {
            new Color(100, 178, 255),
            new Color(181, 249, 80),
            new Color(51, 230, 250),
            new Color(239, 230, 98),
            new Color(225, 117, 95),
            new Color(170, 199, 73),
            new Color(134, 25, 86),
            new Color(201, 160, 220),
            new Color(0, 153, 204),
            new Color(142, 84, 84),
            new Color(255, 153, 0)
        };

//        TimeSeries measSeries;
//        TimeSeriesCollection dataSet = new TimeSeriesCollection();
//        Calendar calendar = Calendar.getInstance();
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//        Date measDate = new Date();
//        calendar.setTime(measDate);
//
//        measSeries = new TimeSeries(measure, Day.class);
//        for ( int i=0; i < retObj.getRowCount(); i++ )
//        {
//            try {
//                
//                measDate = dateFormat.parse(retObj.getFieldValueString(i, 0));
//                calendar.setTime(measDate);
////                calendar.add(Calendar.DATE, 1);
////                measDate = calendar.getTime();
//            } catch (Exception ex) {
//                
//                measDate = new Date();
//            }
//            
//            measSeries.add(new Day(calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.YEAR)),retObj.getFieldValueBigDecimal(i,measColumn).doubleValue());
//        }
//        dataSet.addSeries(measSeries);

        DefaultCategoryDataset dataSetLine = new DefaultCategoryDataset();
        for (int i = 0; i < retObj.getRowCount(); i++) {
            dataSetLine.addValue(retObj.getFieldValueBigDecimal(i, measColumn).doubleValue(), measure, retObj.getFieldValueString(i, 0));
        }



        chart = ChartFactory.createLineChart(
                " ", // title
                xAxisLabel, // x-axis label
                yAxisLabel, // y-axis label
                dataSetLine, // data
                PlotOrientation.VERTICAL,
                false, // create legend?
                false, // generate tooltips?
                false // generate URLs?
                );

        chart.setBackgroundPaint(Color.white);
        chart.setBorderVisible(false);


        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);//use setter and getter to change bk color later
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        plot.setForegroundAlpha(1f);
        plot.setBackgroundAlpha(0f);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);
        //plot.setDomainGridlinePaint(Color.WHITE);
        plot.setOutlineVisible(false);
        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        plot.setRenderer(renderer);
        CategoryItemRenderer itemRenderer = (CategoryItemRenderer) plot.getRenderer();
        itemRenderer.setSeriesPaint(0, colors[0]);
        ((LineAndShapeRenderer) itemRenderer).setSeriesFillPaint(0, Color.RED);
//        for ( int j=0; j<1; j++ )
//        {
//            xyItemRenderer.setSeriesItemLabelsVisible(j, true); //true to display symbols
//            if (j < colors.length)
//                xyItemRenderer.setSeriesPaint(j, colors[j]);
//        }
        Font font = new Font("Verdana", Font.PLAIN, 9);

        plot.getDomainAxis().setTickLabelsVisible(displayXAxisTicks);
        plot.getDomainAxis().setVisible(true);

        plot.getRenderer().setSeriesStroke(0,
                new BasicStroke(
                1.8f, BasicStroke.CAP_SQUARE, BasicStroke.CAP_SQUARE,
                0.5f, new float[]{10.0f, 2.0f}, 0.0f));
//        DateAxis timeAxis = (DateAxis) plot.getDomainAxis();
//        timeAxis.setTickLabelsVisible(false);
//        timeAxis.setVisible(true);
//        timeAxis.setTickUnit(new DateTickUnit(DateTickUnit.MONTH, 1));
//        timeAxis.setTickLabelsVisible(true);
//        timeAxis.setTickLabelFont(font);
//        timeAxis.setTickLabelPaint(Color.BLACK);
//        timeAxis.setAutoRange(true);
//        //timeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
//        timeAxis.setLabelFont(font);
//        timeAxis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));
//        timeAxis.setTickUnit(new DateTickUnit(DateTickUnit.MONTH, 1));

        ValueAxis axis2 = plot.getRangeAxis();
        axis2.setTickLabelsVisible(displayYAxisTicks);
        axis2.setTickMarksVisible(true);
        axis2.setVisible(true);
//        axis2.setTickLabelFont(font);
//        axis2.setTickLabelPaint(Color.BLACK);
//        axis2.setAutoRange(true);
//        //axis2.setStandardTickUnits(NumberAxis.createStandardTickUnits());
//        axis2.setLabelFont(font);




        return chart;
    }

    public String generateChartPath(HttpSession session, Writer out, JFreeChart chart) {
        String chartPath = null;
        ProgenChartDisplay chartDisplay = new ProgenChartDisplay(240, 140);
        chartDisplay.chart = chart;
        try {
            chartPath = chartDisplay.getchartPath(session, null, out);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return chartPath;

    }

    public String generateChartPath(HttpSession session, Writer out, JFreeChart chart, int width, int height) {
        String chartPath = null;
        ProgenChartDisplay chartDisplay = new ProgenChartDisplay(width, height);
        chartDisplay.chart = chart;
        try {
            chartPath = chartDisplay.getchartPath(session, null, out);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return chartPath;
    }
//    public String generateChart(HttpSession session, Writer out)
//    {
//        String chartPath = null;
//
//        Color[] colors = {
//        new Color(100, 178, 255),
//        new Color(181, 249, 80),
//        new Color(51, 230, 250),
//        new Color(239, 230, 98),
//        new Color(225, 117, 95),
//        new Color(170, 199, 73),
//        new Color(134, 25, 86),
//        new Color(201, 160, 220),
//        new Color(0, 153, 204),
//        new Color(142, 84, 84),
//        new Color(255, 153, 0)
//        };
//
//        TimeSeries s1 = new TimeSeries("L&G European Index Trust", Month.class);
//        s1.add(new Month(2, 2001), 181.8);
//        s1.add(new Month(3, 2001), 167.3);
//        s1.add(new Month(4, 2001), 153.8);
//        s1.add(new Month(5, 2001), 167.6);
//        s1.add(new Month(6, 2001), 158.8);
//        s1.add(new Month(7, 2001), 148.3);
//        s1.add(new Month(8, 2001), 153.9);
//        s1.add(new Month(9, 2001), 142.7);
//        s1.add(new Month(10, 2001), 123.2);
//        s1.add(new Month(11, 2001), 131.8);
//        s1.add(new Month(12, 2001), 139.6);
//        s1.add(new Month(1, 2002), 142.9);
//        s1.add(new Month(2, 2002), 138.7);
//        s1.add(new Month(3, 2002), 137.3);
//        s1.add(new Month(4, 2002), 143.9);
//        s1.add(new Month(5, 2002), 139.8);
//        s1.add(new Month(6, 2002), 137.0);
//        s1.add(new Month(7, 2002), 132.8);
//
//        TimeSeriesCollection dataset = new TimeSeriesCollection();
//        dataset.addSeries(s1);
//
//        JFreeChart chart = ChartFactory.createTimeSeriesChart(
//                    " ", // title
//                    "", // x-axis label
//                    "", // y-axis label
//                    dataset, // data
//                    false, // create legend?
//                    false, // generate tooltips?
//                     false // generate URLs?
//                    );
//
//        chart.setBackgroundPaint(Color.white);
//        XYPlot plot = (XYPlot) chart.getPlot();
//        plot.setBackgroundPaint(Color.white);//use setter and getter to change bk color later
//        plot.setDomainCrosshairVisible(true);
//        plot.setRangeCrosshairVisible(true);
//        plot.setForegroundAlpha(1f);
//        plot.setBackgroundAlpha(0f);
//        plot.setRangeGridlinesVisible(false);
//        plot.setDomainGridlinesVisible(false);
//        plot.setDomainGridlinePaint(Color.GRAY);
//        XYLineAndShapeRenderer xyItemRenderer = (XYLineAndShapeRenderer) plot.getRenderer();
//        for ( int j=0; j<1; j++ )
//        {
//            xyItemRenderer.setSeriesItemLabelsVisible(j, false); //true to display symbols
//            if (j < colors.length)
//                xyItemRenderer.setSeriesPaint(j, colors[j]);
//        }
//        Font font = new Font("Verdana", Font.PLAIN, 9);
//        DateAxis timeAxis = (DateAxis) plot.getDomainAxis();
//        timeAxis.setTickLabelsVisible(false);
////        timeAxis.setTickLabelFont(font);
////        timeAxis.setTickLabelPaint(Color.BLACK);
////        timeAxis.setAutoRange(true);
////        //timeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
////        timeAxis.setLabelFont(font);
////        timeAxis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));
////        timeAxis.setTickUnit(new DateTickUnit(DateTickUnit.MONTH, 1));
//
//        ValueAxis axis2 = plot.getRangeAxis();
//        axis2.setTickLabelsVisible(false);
////        axis2.setTickLabelFont(font);
////        axis2.setTickLabelPaint(Color.BLACK);
////        axis2.setAutoRange(true);
////        //axis2.setStandardTickUnits(NumberAxis.createStandardTickUnits());
////        axis2.setLabelFont(font);
//
//
//        ProgenChartDisplay chartDisplay = new ProgenChartDisplay(100,100);
//        chartDisplay.chart = chart;
//        try {
//            chartPath = chartDisplay.getchartPath(session, null, out);
//            
//        } catch (IOException ex) {
//            logger.error("Exception:",ex);
//        }
//        return chartPath;
//    }
}
