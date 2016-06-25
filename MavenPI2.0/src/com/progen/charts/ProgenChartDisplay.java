/*
 * ProgenChartDisplay.java
 *
 * Created on April 4, 2009, 4:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.progen.charts;

import com.progen.charts.util.ProgenCategoryURLGenerator;
import com.progen.charts.util.ProgenStandardPieURLGenerator;
import com.progen.db.ProgenDataSet;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.*;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.servlet.ChartDeleter;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.chart.urls.StandardPieURLGenerator;
import org.jfree.data.DataUtilities;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.KeyedValues;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.jdbc.JDBCPieDataset;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.*;
import org.jfree.util.SortOrder;
import org.jfree.util.TableOrder;
import utils.db.ProgenConnection;

public class ProgenChartDisplay implements Serializable {

    public static Logger logger = Logger.getLogger(ProgenChartDisplay.class);
    private static final long serialVersionUID = 753264711589787L;
    private ProgenConnection pg;
    private Connection con;
    private CallableStatement st;
    private ResultSet rs;
    public JFreeChart chart;
    public CategoryItemRenderer renderer;
    public BarRenderer rendererBar;
    public CategoryItemRenderer renderer1;
    public String mapname;
    public static String LINE_CHART = "line";
    public static String BAR_CHART = "bar";
    public static String BAR3D_CHART = "Bar";
    public static String LAYERED_CHART = "layered";
    public static String COLUMN = "column";
    public static String COLUMN3D = "column3d";
    public static String LINE3D_CHART = "bar3d";
    public static String STACKED_CHART = "stacked";
    public static String WATERFALL_CHART = "waterfall";
    public static String STACKED3D_CHART = "stacked3d";
    public String chartDisplay = "";
    public String dashboardChartDisplay = "";
    public String passUrl = null;
    private String ctxPath = null;
    private int px;
    private int py;
    public String chartDisplayZoom = null;
    //added on 30/07/09 by santhosh.kumar@progenbusiness.com for swapping graph columns
    private boolean swapColumn = true;
    //added by santhosh.kumar@progenbusiness.com on 25/11/2009
    private ArrayList alist = null;
    private ProgenChartDatasets graph = null;
    private ProgenChartDatasets graph1 = null;
    private ProgenChartDatasets graph2 = null;
    private String chartType = null;
    private String funName = null;
    private HttpSession session = null;
    private HttpServletResponse response = null;
    private Writer out = null;
    //added by santhosh.kumar@progenbusiness.com on 25/11/2009 for graph details
    private String grplegend = "Y";
    private String grplegendloc = "Bottom";
    private String grpshox = "Y";
    private String grpshoy = "Y";
    private String grplyaxislabel;
    private String grpryaxislabel;
    private String grpbDomainaxislabel;
    private String measurePosition = null;
    private String pieMeasureLabel = "";
    //addec by srikanth.p for olap graph in oneView
    public boolean isOLAPGraph;
    public String olapFunction;

    public String getPieMeasureLabel() {
        return (pieMeasureLabel);
    }

    public void setPieMeasureLabel(String measureName) {
        pieMeasureLabel = measureName;
    }

    public String getMeasurePosition() {
        return measurePosition;
    }

    public void setMeasurePosition(String measurePosition) {
        this.measurePosition = measurePosition;
    }

    public String getGrpbDomainaxislabel() {
        return grpbDomainaxislabel;
    }

    public void setGrpbDomainaxislabel(String grpbDomainaxislabel) {
        this.grpbDomainaxislabel = grpbDomainaxislabel;
    }
    private String grpdrill = "Y";
    private String grpbcolor = "";
    private String grpfcolor = "";
    private String grpdata = "Y";
    private File tempImgFile = null;
    private ArrayList RowValuesList = null;
    private String prefixSymbol = "";
    private String timeLevel = null;
    private int seriesCount = 0;
    private String graphGridLines = "Y";
    private ProgenDataSet retObj = null;
    private Color[] colors = {
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
//    private Color[] colors;
    private Color[] colors1 = {
        new Color(181, 249, 80),
        new Color(239, 230, 98),
        new Color(225, 117, 95),
        new Color(100, 178, 255),
        new Color(51, 230, 250),
        new Color(170, 199, 73),
        new Color(134, 25, 86),
        new Color(201, 160, 220),
        new Color(0, 153, 204),
        new Color(142, 84, 84),
        new Color(255, 153, 0)
    };
    private Color[] Spidercolors = {
        new Color(100, 178, 255),
        new Color(134, 25, 86),
        new Color(170, 199, 73),
        new Color(225, 117, 95),
        new Color(239, 230, 98),
        new Color(181, 249, 80),
        new Color(51, 230, 250),
        new Color(201, 160, 220),
        new Color(0, 153, 204),
        new Color(142, 84, 84),
        new Color(255, 153, 0)
    };
    // g new Color(181,249,80),
    // y new Color(239,230,98),
    //r  new Color(225,117,95),
//     private Color1[] colors = {
//
//        new Color(100,178,255),
//        new Color(181,249,80),
//        new Color(51,230,250),
//        new Color(239,230,98),
//        new Color(225,117,95),
//        new Color(170,199,73),
//        new Color(134,25,86),
//        new Color(201,160,220),
//        new Color(0, 153, 204),
//        new Color(142, 84, 84),
//        new Color(255, 153, 0)
//
////        new Color(87,157,228),
////        new Color(124,171,80),
////        new Color(44,200,218),
////        new Color(216,208,89),
////        new Color(199,98,77),
////        new Color(149,169,83),
////        new Color(189,34,112),
////        new Color(174,120,198),
////        new Color(0, 153, 204),
////        new Color(142, 84, 84),
////        new Color(255, 153, 0)
//
//    };
    private String colorchange = "";
    private Font font = new Font("Verdana", Font.PLAIN, 9);
    private boolean urlgen = false;
    private boolean showLegend = false;
    private GraphProperty graphProperty;
    private boolean showGridLines = false;
    private boolean showToolTips = true;
    //added by santhosh.k on 06-02-2010 for defining target range line on graph
//    private String targetRange = "Range";
//    private String startValue = "15000";
//    private String endValue = "20000";
//    private String showMinMaxRange = "Y";
    private String targetRange = null;
    private String startValue = null;
    private String endValue = null;
    private String maxValue = null;
    private String minValue = null;
    private String avgValue = null;
    private String showMinMaxRange = "N";
    private String formatMode = "##,###";
    private int maxFractionDigits = 0;
    private int minFractionDigits = 0;
    private String maxRangeTitle = "Max Value =";
    private String minRangeTitle = "Min Value = ";
    private String avgRangeTitle = "Avg Value = ";
    private String discreteTitle = "Target = ";
    private String rangeTitle = "Range ";
    private String firstChartType = null;
    private PieSectionLabelGenerator generator = null;
    private String stackedType = null;
    private String[] backgroundColor = null;

    public void initializeGraphColors(String theme) {
//        
        if (theme == null || theme.equalsIgnoreCase("blue")) {
            colors = new Color[]{
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
        } else if (theme.equalsIgnoreCase("green")) {
            colors = new Color[]{
                new Color(181, 249, 80),
                new Color(51, 230, 250),
                new Color(239, 230, 98),
                new Color(225, 117, 95),
                new Color(170, 199, 73),
                new Color(134, 25, 86),
                new Color(201, 160, 220),
                new Color(0, 153, 204),
                new Color(142, 84, 84),
                new Color(255, 153, 0),
                new Color(100, 178, 255),};

        } else {
            colors = new Color[]{
                new Color(255, 153, 0),
                new Color(100, 178, 255),
                new Color(181, 249, 80),
                new Color(51, 230, 250),
                new Color(239, 230, 98),
                new Color(225, 117, 95),
                new Color(170, 199, 73),
                new Color(134, 25, 86),
                new Color(201, 160, 220),
                new Color(0, 153, 204),
                new Color(142, 84, 84)
            };

        }
    }

    public void setUrl(String url) {
        this.passUrl = url;
    }

    public void setChartDisplay(String map, String path) {
        chartDisplay = "<SCRIPT LANGUAGE=\"JavaScript\" SRC=\"overlib.js\">  </SCRIPT>";
        //chartDisplay += "<div align='center' id=\"overDiv\" style=\"position:absolute; visibility:hidden; z-index:1000;\"></div>";
        chartDisplay += "<div align='center' id=\"overDiv\" style=\"position:absolute;z-index:1000;\"></div>";
        if (isOLAPGraph) {
            chartDisplay += "<center><a href='" + olapFunction + "'><img align='top' usemap=\"#" + map + "\"  src=\"" + ctxPath + "/" + path + "\" border='0' /></a></center>";
        } else {
            chartDisplay += "<center><img align='top' usemap=\"#" + map + "\"  src=\"" + ctxPath + "/" + path + "\" border='0' /></center>";
        }
        isOLAPGraph = false;
    }

    public void setDashboardChartDisplay(String map, String path) {
        //chartDisplay = "<SCRIPT LANGUAGE=\"JavaScript\" SRC=\"overlib.js\">  </SCRIPT>";
        //chartDisplay += "<div id=\"overDiv\" style=\"position:absolute; visibility:hidden; z-index:1000;\"></div>";
        dashboardChartDisplay = "<img align='top' usemap=\"#" + map + "\"  src=\"" + ctxPath + "/" + path + "\" border=0> </img>";


    }

    public void setChartDisplayZoom(String map, String path) {
        chartDisplayZoom = "<SCRIPT LANGUAGE=\"JavaScript\" SRC=\"overlib.js\">  </SCRIPT>";
        //chartDisplayZoom += "<div id=\"overDivZoom\" style=\"position:absolute; visibility:show; z-index:1000;\"></div>";
        chartDisplayZoom += "<div id=\"overDivZoom\" style=\"position:absolute; z-index:1000;\"></div>";
        chartDisplayZoom += "<img usemap=\"#" + map + "\"  src=\"" + ctxPath + "/" + path + "\" width='700' height='350' border=0 > </img>";

    }

    public ProgenChartDisplay() {
        pg = null;
        con = null;
        st = null;
        rs = null;
        px = 400;
        py = 300;


    }

    public ProgenChartDisplay(int width, int height, String ctxPath) {
        pg = null;
        con = null;
        st = null;
        rs = null;
        px = width;
        py = height;
        this.ctxPath = ctxPath;

    }

    public ProgenChartDisplay(int width, int height) {
        pg = null;
        con = null;
        st = null;
        rs = null;
        px = width;
        py = height;


    }

    public String GetDualAxisChart(String sqlstr1, String sqlstr2, String yAxisName, String SecondAxisLabel, String funName, HttpSession session, HttpServletResponse response, Writer out) throws SQLException, IOException {
        ProgenChartDatasets graph = new ProgenChartDatasets(sqlstr1);


        final DefaultCategoryDataset dataset1 = graph.getCategoryDataset();

        if (funName != null && !(funName.equals(""))) {
            urlgen = true;
            this.setUrl(funName);
        }
        if (chartType.equalsIgnoreCase("bar")) {
            chart = ChartFactory.createBarChart(
                    "", // chart title
                    "", // domain axis label
                    yAxisName, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    true, // include legend
                    true, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (chartType.equalsIgnoreCase("bar3d")) {
            chart = ChartFactory.createBarChart(
                    "", // chart title
                    "", // domain axis label
                    yAxisName, // range axis label
                    dataset1, // datachartType
                    PlotOrientation.VERTICAL,
                    true, // include legend
                    true, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (chartType.equalsIgnoreCase("line")) {
            chart = ChartFactory.createBarChart(
                    "", // chart title
                    "", // domain axis label
                    yAxisName, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    true, // include legend
                    true, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (chartType.equalsIgnoreCase("line3d")) {
            chart = ChartFactory.createBarChart(
                    "", // chart title
                    "", // domain axis label
                    yAxisName, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    true, // include legend
                    true, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        }
        ProgenChartDatasets graph1 = new ProgenChartDatasets(sqlstr2);

        final DefaultCategoryDataset dataset2 = graph1.getCategoryDataset();
        //graph1.generateToolTip(dataset2,1,1);

//        chart = ChartFactory.createBarChart(
//                "", // chart title
//                "", // domain axis label
//                yAxisName, // range axis label
//                dataset1, // data
//                PlotOrientation.VERTICAL,
//                true, // include legend
//                true, // tooltips?
//                urlgen // URL generator?  Not required...
//                );


        chart.setBackgroundPaint(Color.white);
        final CategoryItemRenderer renderer = new BarRenderer();
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(0xEE, 0xEE, 0xFF));
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
        plot.setForegroundAlpha(1f);
        plot.setBackgroundAlpha(0f);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.GRAY);



        plot.setDataset(1, dataset2);
        plot.mapDatasetToRangeAxis(1, 1);

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        final ValueAxis axis2 = new NumberAxis(SecondAxisLabel);
        plot.setRangeAxis(1, axis2);

        final LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();
        StandardCategoryToolTipGenerator Std = new StandardCategoryToolTipGenerator();
        StandardCategoryURLGenerator url1 = null;
        if (urlgen) {
            url1 = new ProgenCategoryURLGenerator("javascript:parent.submiturls1(", ")", this.passUrl);
            renderer2.setItemURLGenerator(url1);
            renderer.setItemURLGenerator(url1);
        }


        //Std.generateToolTip(dataset2,1,1);
        renderer.setToolTipGenerator(Std);
        renderer2.setToolTipGenerator(Std);


        plot.setRenderer(renderer);
        plot.setRenderer(1, renderer2);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        chart.setBorderVisible(false);

        String path = getchartPath(session, response, out);
        setChartDisplay(this.mapname, path);
        return path;
    }

    public String GetSingleAxisChart(String sqlstr1, String sqlstr2, String yAxisName, String funName, HttpSession session, HttpServletResponse response, Writer out) throws SQLException, IOException {
        ProgenChartDatasets graph = new ProgenChartDatasets(sqlstr1);

        final DefaultCategoryDataset dataset1 = graph.getCategoryDataset();

        if (funName != null && !(funName.equals(""))) {
            urlgen = true;
            this.setUrl(funName);
        }

        ProgenChartDatasets graph1 = new ProgenChartDatasets(sqlstr2);

        final DefaultCategoryDataset dataset2 = graph1.getCategoryDataset();
        //graph1.generateToolTip(dataset2,1,1);

        chart = ChartFactory.createBarChart(
                "", // chart title
                "", // domain axis label
                yAxisName, // range axis label
                dataset1, // data
                PlotOrientation.VERTICAL,
                true, // include legend
                true, // tooltips?
                urlgen // URL generator?  Not required...
                );


        //chart.setBackgroundPaint(Color.white);
        backgroundColor = graphProperty.getRgbColorArr();
        //
        if (backgroundColor != null && backgroundColor.length > 0) {
            chart.setBackgroundPaint(new Color(Integer.parseInt(backgroundColor[0].trim()), Integer.parseInt(backgroundColor[1].trim()), Integer.parseInt(backgroundColor[2].trim())));
        } else {
            chart.setBackgroundPaint(Color.white);
        }
        final CategoryItemRenderer renderer = new BarRenderer();
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(0xEE, 0xEE, 0xFF));
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
        plot.setForegroundAlpha(1f);
        plot.setBackgroundAlpha(0f);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.GRAY);



        plot.setDataset(1, dataset2);
        //plot.mapDatasetToRangeAxis(1, 1);

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        //final ValueAxis axis2 = new NumberAxis(SecondAxisLabel);
        //plot.setRangeAxis(1, axis2);

        final LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();
        StandardCategoryToolTipGenerator Std = new StandardCategoryToolTipGenerator();
        StandardCategoryURLGenerator url1 = null;
        if (urlgen) {
            url1 = new ProgenCategoryURLGenerator("javascript:parent.submiturls1(", ")", this.passUrl);
            renderer2.setItemURLGenerator(url1);
            renderer.setItemURLGenerator(url1);
        }


        //Std.generateToolTip(dataset2,1,1);
        renderer.setToolTipGenerator(Std);
        renderer2.setToolTipGenerator(Std);


        plot.setRenderer(renderer);
        plot.setRenderer(1, renderer2);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        chart.setBorderVisible(false);





        String path = getchartPath(session, response, out);
        setChartDisplay(this.mapname, path);
        return path;
    }

    public String GetCategoryAxisChart(String sqlstr1, String chartType, String yAxisName, String funName, HttpSession session, HttpServletResponse response, Writer out) throws SQLException, IOException {
        ProgenChartDatasets graph = new ProgenChartDatasets(sqlstr1);
        final DefaultCategoryDataset dataset1 = graph.getCategoryDataset();


        if (funName != null && !(funName.equals(""))) {
            urlgen = true;
            this.setUrl(funName);
        }

        if (chartType.equalsIgnoreCase("bar")) {
            chart = ChartFactory.createBarChart(
                    "", // chart title
                    "", // domain axis label
                    yAxisName, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    true, // include legend
                    true, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (chartType.equalsIgnoreCase("bar3d")) {
            chart = ChartFactory.createBarChart(
                    "", // chart title
                    "", // domain axis label
                    yAxisName, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    true, // include legend
                    true, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (chartType.equalsIgnoreCase("line")) {
            chart = ChartFactory.createBarChart(
                    "", // chart title
                    "", // domain axis label
                    yAxisName, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    true, // include legend
                    true, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (chartType.equalsIgnoreCase("line3d")) {
            chart = ChartFactory.createBarChart(
                    "", // chart title
                    "", // domain axis label
                    yAxisName, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    true, // include legend
                    true, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (chartType.equalsIgnoreCase("stacked")) {
            chart = ChartFactory.createStackedBarChart(
                    "", // chart title
                    "", // domain axis label
                    yAxisName, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    true, // include legend
                    true, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (chartType.equalsIgnoreCase("stackedarea")) {
            chart = ChartFactory.createStackedAreaChart(
                    "", // chart title
                    "", // domain axis label
                    yAxisName, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    true, // include legend
                    true, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (chartType.equalsIgnoreCase("stacked3d")) {
            chart = ChartFactory.createStackedBarChart3D(
                    "", // chart title
                    "", // domain axis label
                    yAxisName, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    true, // include legend
                    true, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (chartType.equalsIgnoreCase("waterfall")) {
            chart = ChartFactory.createWaterfallChart(
                    "", // chart title
                    "", // domain axis label
                    yAxisName, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    true, // include legend
                    true, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        }

        //chart.setBackgroundPaint(Color.white);
        backgroundColor = graphProperty.getRgbColorArr();
        //
        if (backgroundColor != null && backgroundColor.length > 0) {
            chart.setBackgroundPaint(new Color(Integer.parseInt(backgroundColor[0].trim()), Integer.parseInt(backgroundColor[1].trim()), Integer.parseInt(backgroundColor[2].trim())));
        } else {
            chart.setBackgroundPaint(Color.white);
        }
        if (chartType.equalsIgnoreCase("bar")) {
            rendererBar = new BarRenderer();
        } else if (chartType.equalsIgnoreCase("bar3d")) {
            renderer = new BarRenderer3D();
        } else if (chartType.equalsIgnoreCase("line")) {
            renderer = new LineAndShapeRenderer();
        } else if (chartType.equalsIgnoreCase("line3d")) {
            renderer = new LineRenderer3D();

        } else if (chartType.equalsIgnoreCase("stacked")) {
            renderer = new StackedBarRenderer();
        } else if (chartType.equalsIgnoreCase("stackedarea")) {
            renderer = new StackedBarRenderer();
        } else if (chartType.equalsIgnoreCase("stacked3d")) {
            renderer = new StackedBarRenderer3D();
        } else if (chartType.equalsIgnoreCase("waterfall")) {
            renderer = new WaterfallBarRenderer();
        }






        //BarRenderer renderer11 = new BarRenderer();
        //rendererBar.setBarPainter(new StandardBarPainter());
        //chart.getCategoryPlot().setRenderer(renderer11);

        // get a reference to the plot for further customisation...
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        plot.setForegroundAlpha(1f);
        plot.setBackgroundAlpha(0f);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.mapDatasetToRangeAxis(1, 1);

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        StandardCategoryToolTipGenerator Std = new StandardCategoryToolTipGenerator();
        StandardCategoryURLGenerator url1 = null;
        if (urlgen) {
            url1 = new ProgenCategoryURLGenerator("javascript:parent.submiturls1(", ")", this.passUrl);
            renderer.setItemURLGenerator(url1);
            rendererBar.setItemURLGenerator(url1);
        }


        if (chartType.equalsIgnoreCase("bar")) {


            //Std.generateToolTip(dataset2,1,1);
            rendererBar.setBarPainter(new StandardBarPainter());
            rendererBar.setToolTipGenerator(Std);

            //renderer.setSeriesPaint(0,new Color(255,153,0));
            rendererBar.setSeriesPaint(0, new Color(48, 110, 255));
            rendererBar.setSeriesPaint(1, new Color(255, 102, 0));
            rendererBar.setSeriesPaint(2, Color.RED);
            rendererBar.setSeriesPaint(3, new Color(51, 153, 0));
            rendererBar.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
            rendererBar.setNegativeItemLabelPosition(new ItemLabelPosition());
            //renderer.setItemLabelsVisible(true);
            //renderer.setBaseItemLabelsVisible(true);
            plot.setRenderer(rendererBar);



        } else {
            //////.println("other than bar ");
            //Std.generateToolTip(dataset2,1,1);

            renderer.setToolTipGenerator(Std);

            //renderer.setSeriesPaint(0,new Color(255,153,0));
            renderer.setSeriesPaint(0, new Color(48, 110, 255));
            renderer.setSeriesPaint(1, new Color(255, 102, 0));
            renderer.setSeriesPaint(2, Color.RED);
            renderer.setSeriesPaint(3, new Color(51, 153, 0));
            renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
            renderer.setNegativeItemLabelPosition(new ItemLabelPosition());
            //renderer.setItemLabelsVisible(true);
            //renderer.setBaseItemLabelsVisible(true);
            plot.setRenderer(renderer);



        }





        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        chart.setBorderVisible(false);
        String path = getchartPath(session, response, out);
        setChartDisplay(this.mapname, path);

        return path;
    }

    public String GetPieAxisChart(String sqlstr1, String chartType, String funName, HttpSession session, HttpServletResponse response, Writer out) throws SQLException, IOException {
        ProgenChartDatasets graph = new ProgenChartDatasets(sqlstr1);

        if (funName != null && !(funName.equals(""))) {
            urlgen = true;
            this.setUrl(funName);
        }
        final JDBCPieDataset dataset1 = graph.getPieDataset();

        if (chartType.equalsIgnoreCase("pie")) {
            chart = ChartFactory.createPieChart("",
                    dataset1,
                    true,
                    true,
                    true);
        } else if (chartType.equalsIgnoreCase("pie3d")) {
            chart = ChartFactory.createPieChart3D("",
                    dataset1,
                    true,
                    true,
                    true);
        } else if (chartType.equalsIgnoreCase("ring")) {
            chart = ChartFactory.createRingChart("",
                    dataset1,
                    true,
                    true,
                    true);
        }

        //

        PiePlot p = null;
        chart.setBackgroundPaint(Color.white);
//        chart.getLegend().setAnchor(Legend.SOUTH);
        //final CategoryItemRenderer renderer = new CustomRenderer(p1);
        if (chartType.equalsIgnoreCase("pie")) {
            p = (PiePlot) chart.getPlot();
            p.setForegroundAlpha(1f);
        } else if (chartType.equalsIgnoreCase("pie3d")) {
            p = (PiePlot3D) chart.getPlot();
            p.setForegroundAlpha(0.7f);
        } else if (chartType.equalsIgnoreCase("ring")) {
            p = (RingPlot) chart.getPlot();
            p.setForegroundAlpha(1f);
        }
        p.setBackgroundAlpha(0f);
        p.setCircular(false);
        p.setIgnoreZeroValues(true);
        p.setStartAngle(150f);
        p.setInteriorGap(.01);

        StandardPieURLGenerator url1 = null;
        if (urlgen) {
            url1 = new ProgenStandardPieURLGenerator("javascript:submiturls1(", ")", this.passUrl, 0);
            p.setURLGenerator(url1);
        }

        //p.setLabelGenerator(null);
        chart.removeLegend();
        chart.setBorderVisible(false);
        String path = getchartPath(session, response, out);
        setChartDisplay(this.mapname, path);
        return path;
    }

    public String getchartPath(HttpSession session, HttpServletResponse response, Writer out) throws IOException {

//        ProgenLog.log(ProgenLog.FINE,this,"getchartpath","Enter: "+System.currentTimeMillis());
        logger.info("Enter: " + System.currentTimeMillis());
        ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());

//        ProgenLog.log(ProgenLog.FINE,this,"getchartpath","Save Chart as PNG: "+System.currentTimeMillis());
        logger.info("Save Chart as PNG: " + System.currentTimeMillis());
        String fileName = ServletUtilities.saveChartAsPNG(chart, px, py, info, session);
        this.mapname = fileName;
        this.registerChartForDelete(session, fileName);
        //tempImgFile=File.

        PrintWriter pw = new PrintWriter(out);
        //  Write the image map to the PrintWriter
//        ProgenLog.log(ProgenLog.FINE,this,"getchartpath","Write Image Map: "+System.currentTimeMillis());
        logger.info("Write Image Map: " + System.currentTimeMillis());
        ChartUtilities.writeImageMap(pw, fileName, info, true);
        pw.flush();
        //String path = response.encodeURL("servlet/DisplayChart?filename=" + fileName);
        String path = "servlet/DisplayChart?filename=" + fileName;
//        ProgenLog.log(ProgenLog.FINE,this,"getchartpath","Path: "+path);
        logger.info("Path: " + path);
//        ProgenLog.log(ProgenLog.FINE,this,"getchartpath","Return: "+System.currentTimeMillis());
        logger.info("Return: " + System.currentTimeMillis());
        return path;

    }

    private void registerChartForDelete(HttpSession session, String fileName) {
        ChartDeleter chartDeleter = (ChartDeleter) session.getAttribute("JFreeChart_Deleter");
        if (chartDeleter == null) {
            chartDeleter = new ChartDeleter();
            session.setAttribute("JFreeChart_Deleter", chartDeleter);
        }
        chartDeleter.addChart(fileName);
    }

    //overloaded GetDualAxisChart method by santhosh.kumar@progenbusiness.com
    public String GetDualAxisChart() throws SQLException, IOException {
        //final DefaultCategoryDataset dataset = graph.getCategoryDataset(getRetObj());
        final DefaultCategoryDataset dataset1 = graph1.getCategoryDataset(getRetObj());
        final DefaultCategoryDataset dataset2 = graph2.getCategoryDataset(getRetObj());
        String dualfirstChartType;
        if (funName != null && !(funName.equals(""))) {
            urlgen = true;
            this.setUrl(funName);
        }
        if (getGrplegend().equalsIgnoreCase("Y")) {
            showLegend = true;
        } else {
            showLegend = false;
        }
        if (getGraphGridLines().equalsIgnoreCase("Y")) {
            showGridLines = true;
        } else {
            showGridLines = false;
        }
        if (grpbDomainaxislabel == null || grpbDomainaxislabel.equalsIgnoreCase("null")) {
            grpbDomainaxislabel = "";
        }

        if (grplyaxislabel == null || grplyaxislabel.equalsIgnoreCase("null")) {
            grplyaxislabel = "";
        }
        if (grpryaxislabel == null || grpryaxislabel.equalsIgnoreCase("null")) {
            grpryaxislabel = "";
        }
        grplyaxislabel = getGrplyaxislabel();
        grpryaxislabel = getGrpryaxislabel();
        grpbDomainaxislabel = getGrpbDomainaxislabel();
        if (!graphProperty.getFirstChartType().equalsIgnoreCase("null") && graphProperty.getFirstChartType() != "") {
            dualfirstChartType = graphProperty.getFirstChartType();
            //  

        } else {
            dualfirstChartType = "bar"; // Comment once vale is supplied from front End
        }      //  firstChartType = "stacked"; // Comment once vale is supplied from front End       
        if (dualfirstChartType.equalsIgnoreCase("null") || dualfirstChartType.equalsIgnoreCase("bar")) {
            chart = ChartFactory.createBarChart(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    showLegend, // include legend
                    showToolTips, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (dualfirstChartType.equalsIgnoreCase("bar3d")) {
            chart = ChartFactory.createBarChart(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    showLegend, // include legend
                    showToolTips, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (dualfirstChartType.equalsIgnoreCase("line")) {
            chart = ChartFactory.createLineChart(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    showLegend, // include legend
                    showToolTips, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (dualfirstChartType.equalsIgnoreCase("line3d")) {
            chart = ChartFactory.createLineChart3D(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    showLegend, // include legend
                    showToolTips, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (dualfirstChartType.equalsIgnoreCase("stacked")) {
            chart = ChartFactory.createStackedBarChart(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    true, // include legend
                    true, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (dualfirstChartType.equalsIgnoreCase("stackedarea")) {
            chart = ChartFactory.createStackedAreaChart(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    true, // include legend
                    true, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (dualfirstChartType.equalsIgnoreCase("stacked3d")) {
            chart = ChartFactory.createStackedBarChart3D(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    true, // include legend
                    true, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (dualfirstChartType.equalsIgnoreCase("Area")) {
            chart = ChartFactory.createAreaChart(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    showLegend, // include legend
                    showToolTips, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        }


//        chart = ChartFactory.createLineChart(
//                "", // chart title
//                "", // domain axis label
//                grplyaxislabel, // range axis label
//                dataset1, // data
//                PlotOrientation.VERTICAL,
//                showLegend, // include legend
//                showToolTips, // tooltips?
//                urlgen // URL generator?  Not required...
//                );

//        chart.setBackgroundPaint(Color.white);
//        renderer = new LineAndShapeRenderer();
//        //added by k
//        LineAndShapeRenderer rend = new LineAndShapeRenderer();
//        //rend.setLiPainter(new StandardBarPainter());
//        renderer = (LineAndShapeRenderer) rend;


        // chart.setBackgroundPaint(Color.white);
        backgroundColor = graphProperty.getRgbColorArr();
        //
        if (backgroundColor != null && backgroundColor.length > 0) {
            chart.setBackgroundPaint(new Color(Integer.parseInt(backgroundColor[0].trim()), Integer.parseInt(backgroundColor[1].trim()), Integer.parseInt(backgroundColor[2].trim())));
        } else {
            chart.setBackgroundPaint(Color.white);
        }

        if (dualfirstChartType.equalsIgnoreCase("line")) {

            renderer = new LineAndShapeRenderer();
        } else if (dualfirstChartType.equalsIgnoreCase("line3d")) {

            renderer = new LineRenderer3D();
        } else if (dualfirstChartType.equalsIgnoreCase("bar")) {

            renderer = new BarRenderer();
            //added by k
            BarRenderer rend = new BarRenderer();
            //rend.setLiPainter(new StandardBarPainter());
            renderer = (BarRenderer) rend;
        } else if (dualfirstChartType.equalsIgnoreCase("bar3d")) {

            renderer = new BarRenderer3D();
            //added by k
            BarRenderer3D rend = new BarRenderer3D();
            //rend.setLiPainter(new StandardBarPainter());
            renderer = (BarRenderer3D) rend;
        } else if (dualfirstChartType.equalsIgnoreCase("stacked")) {
            renderer = new StackedBarRenderer();
        } else if (dualfirstChartType.equalsIgnoreCase("stackedarea")) {
            renderer = new StackedBarRenderer();
        } else if (dualfirstChartType.equalsIgnoreCase("stacked3d")) {
            renderer = new StackedBarRenderer3D();
        } else if (dualfirstChartType.equalsIgnoreCase("Area")) {
            renderer = new AreaRenderer();
            //renderer = (AreaRenderer) plot.getRenderer();
        }
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(0xEE, 0xEE, 0xFF));
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
        LegendTitle legend = chart.getLegend();
        if (showLegend) {
            if (legend != null) {
                legend.setItemFont(font);
                if (grplegendloc.equalsIgnoreCase("Bottom")) {
                    legend.setPosition(RectangleEdge.BOTTOM);
                } else if (grplegendloc.equalsIgnoreCase("Top")) {
                    legend.setPosition(RectangleEdge.TOP);
                } else if (grplegendloc.equalsIgnoreCase("Left")) {
                    legend.setPosition(RectangleEdge.LEFT);
                } else if (grplegendloc.equalsIgnoreCase("Right")) {
                    legend.setPosition(RectangleEdge.RIGHT);
                } else {
                    legend.setPosition(RectangleEdge.BOTTOM);
                }
            }
        }

        if (dualfirstChartType.equalsIgnoreCase("Area")) {
            plot.setForegroundAlpha(0.5f);
        } else {
            plot.setForegroundAlpha(1f);
        }
        plot.setBackgroundAlpha(0f);
        plot.setRangeGridlinesVisible(showGridLines);
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.setDomainGridlinesVisible(showGridLines);
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setDataset(1, dataset2);
        plot.mapDatasetToRangeAxis(1, 1);
        //plot.setRange(0.00,5.00);
        final CategoryAxis domainAxis = plot.getDomainAxis();
        if (graphProperty.getAxisLabelPosition() != null) {
            if (graphProperty.getAxisLabelPosition().equalsIgnoreCase("Horizontal")) {
                domainAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
            } else if (graphProperty.getAxisLabelPosition().equalsIgnoreCase("Vertical")) {
                domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
            } else {
                domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
            }
        } else {
            domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        }
        domainAxis.setTickLabelFont(font);
        domainAxis.setTickLabelPaint(Color.BLACK);
        domainAxis.setLabelFont(font);

        List ColumnKeys = dataset1.getColumnKeys();
        if (ColumnKeys != null) {
            for (int i = 0; i < ColumnKeys.size(); i++) {
                domainAxis.addCategoryLabelToolTip(dataset1.getColumnKey(i), ColumnKeys.get(i).toString());
            }
        }

        DecimalFormat formatter1 = null;
        DecimalFormat formatter2 = null;

        NumberAxis axis1 = (NumberAxis) plot.getRangeAxis();
        axis1.setInverted(false);
        axis1.setTickLabelFont(font);
        axis1.setTickLabelPaint(Color.BLACK);
        axis1.setAutoRange(true);
        if (graphProperty.getCalibration() != null && graphProperty.getCalibration() != "") {
            if (graphProperty.getCalibration().equalsIgnoreCase("Integer")) {
                axis1.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); // display integer values in y axis label
            } else {
                maxFractionDigits = 5;
                minFractionDigits = 2;
                axis1.setStandardTickUnits(NumberAxis.createStandardTickUnits());
            }
        } else {
            axis1.setStandardTickUnits(NumberAxis.createStandardTickUnits());
        }
        //axis1.setStandardTickUnits(NumberAxis.createStandardTickUnits());
        axis1.setLabelFont(font);
        if (graph1.getPrefixSymbol() == null || graph1.getPrefixSymbol().equalsIgnoreCase("null")) {
            graph1.setPrefixSymbol("");
        }
        if (graph1.getSufffixSymbol() == null || graph1.getSufffixSymbol().equalsIgnoreCase("null")) {
            graph1.setSuffixSymbol("");
        }
        formatter1 = new DecimalFormat(graph1.getPrefixSymbol() + formatMode + graph1.getSufffixSymbol());
        formatter1.setMaximumFractionDigits(maxFractionDigits);
        formatter1.setMinimumFractionDigits(minFractionDigits);
        //      formatter1.setTickUnit(NumberTickUnit(.05, DecimalFormat("#.00")));
        //    formatter1.setTickUnit(NumberTickUnit(.00005, DecimalFormat("#.00000")));
        axis1.setNumberFormatOverride(formatter1);//to display numbers in terms of K


        NumberAxis axis2 = new NumberAxis(grpryaxislabel);
        axis2.setInverted(false);
        axis2.setTickLabelFont(font);
        axis2.setTickLabelPaint(Color.BLACK);
        axis2.setAutoRange(true);
        if (graphProperty.getCalibration() != null && graphProperty.getCalibration() != "") {
            if (graphProperty.getCalibration().equalsIgnoreCase("Integer")) {
                axis2.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); // display integer values in y axis label
            } else {
                maxFractionDigits = 5;
                minFractionDigits = 2;
                axis2.setStandardTickUnits(NumberAxis.createStandardTickUnits());
            }
        } else {
            axis2.setStandardTickUnits(NumberAxis.createStandardTickUnits());
        }
        //axis2.setStandardTickUnits(NumberAxis.createStandardTickUnits());
        axis2.setLabelFont(font);

        if (graph2.getPrefixSymbol() == null || graph2.getPrefixSymbol().equalsIgnoreCase("null")) {
            graph2.setPrefixSymbol("");
        }
        if (graph2.getSufffixSymbol() == null || graph2.getSufffixSymbol().equalsIgnoreCase("null")) {
            graph2.setSuffixSymbol("");
        }

        formatter2 = new DecimalFormat(graph2.getPrefixSymbol() + formatMode + graph2.getSufffixSymbol());
        formatter2.setMaximumFractionDigits(maxFractionDigits);
        formatter2.setMinimumFractionDigits(minFractionDigits);
        axis2.setNumberFormatOverride(formatter2);//to display numbers in terms of K
        plot.setRangeAxis(1, axis2);

        if (graphProperty.getSecondChartType() != null && graphProperty.getSecondChartType() != "") {
            //   
            if (graphProperty.getSecondChartType().equals("line")) {
                renderer1 = new LineAndShapeRenderer();
            } else {
                renderer1 = new LineRenderer3D();  // second chart type line 3d
            }
        } else {
            renderer1 = new LineAndShapeRenderer();
        }



        StandardCategoryToolTipGenerator Std = new StandardCategoryToolTipGenerator();
        StandardCategoryURLGenerator url1 = null;

        if (urlgen) {
            url1 = new ProgenCategoryURLGenerator("javascript:parent.submiturls1(", ")", this.passUrl);
            renderer.setItemURLGenerator(url1);//bar
            renderer1.setItemURLGenerator(url1);//line
        }
        renderer.setToolTipGenerator(Std);
        renderer1.setToolTipGenerator(Std);
//        
        for (int i = 0; i < colors.length; i++) {
            renderer.setSeriesPaint(i, colors[i]);//r
        }
        for (int i = (colors.length - 1); i >= 0; i--) {
            renderer1.setSeriesPaint((colors.length - 1 - i), colors[i]);//r
        }
        if (graphProperty.isLabelsDisplayed()) {
            renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", NumberFormat.getInstance()));
            // renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE9, TextAnchor.BOTTOM_RIGHT));
            renderer.setBaseItemLabelsVisible(true);
        }
        plot.setRenderer(0, renderer, true);
        plot.setRenderer(1, renderer1, true);

        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);


        chart.setBorderVisible(false);
        String path = getchartPath(session, response, out);
        setChartDisplay(this.mapname, path);
        return path;
    }

    //overloaded GetSingleAxisChart method by santhosh.kumar@progenbusiness.com
    //modified by kalyan
    public String GetOverlaidBar() throws SQLException, IOException {
        final DefaultCategoryDataset dataset1 = graph1.getCategoryDataset(getRetObj());
        final DefaultCategoryDataset dataset2 = graph2.getCategoryDataset(getRetObj());
        String dualfirstChartType = null;
        if (funName != null && !(funName.equals(""))) {
            urlgen = true;
            this.setUrl(funName);
        }

        if (getGrplegend().equalsIgnoreCase("Y")) {
            showLegend = true;
        } else {
            showLegend = false;
        }

        if (getGraphGridLines().equalsIgnoreCase("Y")) {
            showGridLines = true;
        } else {
            showGridLines = false;
        }
        if (grpbDomainaxislabel == null || grpbDomainaxislabel.equalsIgnoreCase("null")) {
            grpbDomainaxislabel = "";
        }

        if (grplyaxislabel == null || grplyaxislabel.equalsIgnoreCase("null")) {
            grplyaxislabel = "";
        }
        grpbDomainaxislabel = getGrpbDomainaxislabel();
        grplyaxislabel = getGrplyaxislabel();
        if (!graphProperty.getFirstChartType().equalsIgnoreCase("null") && graphProperty.getFirstChartType() != "") {
            dualfirstChartType = graphProperty.getFirstChartType();
            //     

        } else {
            dualfirstChartType = "bar"; // Comment once vale is supplied from front End
        }      //  firstChartType = "stacked"; // Comment once vale is supplied from front End
        if (dualfirstChartType.equalsIgnoreCase("null") || dualfirstChartType.equalsIgnoreCase("bar")) {
            chart = ChartFactory.createBarChart(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    showLegend, // include legend
                    showToolTips, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (dualfirstChartType.equalsIgnoreCase("bar3d")) {
            chart = ChartFactory.createBarChart(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    showLegend, // include legend
                    showToolTips, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (dualfirstChartType.equalsIgnoreCase("line")) {
            chart = ChartFactory.createLineChart(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    showLegend, // include legend
                    showToolTips, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (dualfirstChartType.equalsIgnoreCase("line3d")) {
            chart = ChartFactory.createLineChart3D(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    showLegend, // include legend
                    showToolTips, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (dualfirstChartType.equalsIgnoreCase("stacked")) {
            chart = ChartFactory.createStackedBarChart(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    true, // include legend
                    true, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (dualfirstChartType.equalsIgnoreCase("stackedarea")) {
            chart = ChartFactory.createStackedAreaChart(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    true, // include legend
                    true, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (dualfirstChartType.equalsIgnoreCase("stacked3d")) {
            chart = ChartFactory.createStackedBarChart3D(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    true, // include legend
                    true, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (dualfirstChartType.equalsIgnoreCase("Area")) {
            chart = ChartFactory.createAreaChart(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    showLegend, // include legend
                    showToolTips, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        }


//        chart = ChartFactory.createLineChart(
//                "", // chart title
//                "", // domain axis label
//                grplyaxislabel, // range axis label
//                dataset1, // data
//                PlotOrientation.VERTICAL,
//                showLegend, // include legend
//                showToolTips, // tooltips?
//                urlgen // URL generator?  Not required...
//                );

//        chart.setBackgroundPaint(Color.white);
//        renderer = new LineAndShapeRenderer();
//        //added by k
//        LineAndShapeRenderer rend = new LineAndShapeRenderer();
//        //rend.setLiPainter(new StandardBarPainter());
//        renderer = (LineAndShapeRenderer) rend;


        //chart.setBackgroundPaint(Color.white);
        backgroundColor = graphProperty.getRgbColorArr();
        //
        if (backgroundColor != null && backgroundColor.length > 0) {
            chart.setBackgroundPaint(new Color(Integer.parseInt(backgroundColor[0].trim()), Integer.parseInt(backgroundColor[1].trim()), Integer.parseInt(backgroundColor[2].trim())));
        } else {
            chart.setBackgroundPaint(Color.white);
        }
        if (dualfirstChartType.equalsIgnoreCase("line")) {

            renderer = new LineAndShapeRenderer();
        } else if (dualfirstChartType.equalsIgnoreCase("line3d")) {

            renderer = new LineRenderer3D();
        } else if (dualfirstChartType.equalsIgnoreCase("bar")) {

            renderer = new BarRenderer();
            //added by k
            BarRenderer rend = new BarRenderer();
            //rend.setLiPainter(new StandardBarPainter());
            renderer = (BarRenderer) rend;
        } else if (dualfirstChartType.equalsIgnoreCase("bar3d")) {

            renderer = new BarRenderer3D();
            //added by k
            BarRenderer3D rend = new BarRenderer3D();
            //rend.setLiPainter(new StandardBarPainter());
            renderer = (BarRenderer3D) rend;
        } else if (dualfirstChartType.equalsIgnoreCase("stacked")) {
            renderer = new StackedBarRenderer();
        } else if (dualfirstChartType.equalsIgnoreCase("stackedarea")) {
            renderer = new StackedBarRenderer();
        } else if (dualfirstChartType.equalsIgnoreCase("stacked3d")) {
            renderer = new StackedBarRenderer3D();
        } else if (dualfirstChartType.equalsIgnoreCase("Area")) {
            renderer = new AreaRenderer();
            //renderer = (AreaRenderer) plot.getRenderer();
        }
        chart = ChartFactory.createBarChart(
                "", // chart title
                grpbDomainaxislabel, // domain axis label
                grplyaxislabel, // range axis label
                dataset1, // data
                PlotOrientation.VERTICAL,
                showLegend, // include legend
                showToolTips, // tooltips?
                urlgen // URL generator?  Not required...
                );

        chart.setBackgroundPaint(Color.white);
        //chart.setBackgroundPaint(new Color(230, 230, 230));
//        CategoryItemRenderer renderer = new BarRenderer();
        //aedded by k
//        BarRenderer rend = new BarRenderer();
//        rend.setBarPainter(new StandardBarPainter());
//        renderer = (BarRenderer) rend;


        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(0xEE, 0xEE, 0xFF));
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);

        LegendTitle legend = chart.getLegend();
        if (showLegend) {
            if (legend != null) {
                legend.setItemFont(font);
                if (grplegendloc.equalsIgnoreCase("Bottom")) {
                    legend.setPosition(RectangleEdge.BOTTOM);
                } else if (grplegendloc.equalsIgnoreCase("Top")) {
                    legend.setPosition(RectangleEdge.TOP);
                } else if (grplegendloc.equalsIgnoreCase("Left")) {
                    legend.setPosition(RectangleEdge.LEFT);
                } else if (grplegendloc.equalsIgnoreCase("Right")) {
                    legend.setPosition(RectangleEdge.RIGHT);
                } else {
                    legend.setPosition(RectangleEdge.BOTTOM);
                }
            }
        }

        plot.setForegroundAlpha(1f);
        plot.setBackgroundAlpha(0f);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setDataset(1, dataset2);

        //plot.mapDatasetToRangeAxis(1, 1);

        final CategoryAxis domainAxis = plot.getDomainAxis();
        if (graphProperty.getAxisLabelPosition() != null) {
            if (graphProperty.getAxisLabelPosition().equalsIgnoreCase("Horizontal")) {
                domainAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
            } else if (graphProperty.getAxisLabelPosition().equalsIgnoreCase("Vertical")) {
                domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
            } else {
                domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
            }
        } else {
            domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        }
        domainAxis.setTickLabelFont(font);
        domainAxis.setTickLabelPaint(Color.BLACK);
        domainAxis.setLabelFont(new Font("Verdana", 0, 9));

        java.util.List ColumnKeys = dataset1.getColumnKeys();
        if (ColumnKeys != null) {
            for (int i = 0; i < ColumnKeys.size(); i++) {
                domainAxis.addCategoryLabelToolTip(dataset1.getColumnKey(i), ColumnKeys.get(i).toString());
            }
        }
        NumberAxis axis1 = (NumberAxis) plot.getRangeAxis();
        axis1.setInverted(false);
        axis1.setTickLabelFont(new Font("Verdana", 0, 9));
        axis1.setTickLabelPaint(Color.BLACK);
        axis1.setAutoRange(true);
        if (graphProperty.getCalibration() != null && graphProperty.getCalibration() != "") {
            if (graphProperty.getCalibration().equalsIgnoreCase("Integer")) {
                axis1.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); // display integer values in y axis label
            } else {
                maxFractionDigits = 5;
                minFractionDigits = 2;
                axis1.setStandardTickUnits(NumberAxis.createStandardTickUnits());
            }
        } else {
            axis1.setStandardTickUnits(NumberAxis.createStandardTickUnits());
        }
        //axis1.setStandardTickUnits(NumberAxis.createStandardTickUnits());
        axis1.setLabelFont(new Font("Verdana", 0, 9));


        ///
        if (graph.getPrefixSymbol() == null || graph.getPrefixSymbol().equalsIgnoreCase("null")) {
            graph.setPrefixSymbol("");
        }
        if (graph.getSufffixSymbol() == null || graph.getSufffixSymbol().equalsIgnoreCase("null")) {
            graph.setSuffixSymbol("");
        }

        DecimalFormat formatter1 = null;
        formatter1 = new DecimalFormat(graph.getPrefixSymbol() + formatMode + graph.getSufffixSymbol());
        formatter1.setMaximumFractionDigits(maxFractionDigits);
        formatter1.setMinimumFractionDigits(minFractionDigits);
        axis1.setNumberFormatOverride(formatter1);//to display numbers in terms of K

        if (graphProperty.getSecondChartType() != null && graphProperty.getSecondChartType() != "") {
            //   
            if (graphProperty.getSecondChartType().equals("line")) {
                renderer1 = new LineAndShapeRenderer();
            } else {
                renderer1 = new LineRenderer3D();  // second chart type line 3d
            }
        } else {
            renderer1 = new LineAndShapeRenderer();
        }

//        final LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();
        StandardCategoryToolTipGenerator Std = new StandardCategoryToolTipGenerator();
        StandardCategoryURLGenerator url1 = null;
        if (urlgen) {
            url1 = new ProgenCategoryURLGenerator("javascript:parent.submiturls1(", ")", this.passUrl);
            renderer1.setItemURLGenerator(url1);
            renderer.setItemURLGenerator(url1);
        }

        //Std.generateToolTip(dataset2,1,1);
        renderer.setToolTipGenerator(Std);
        renderer1.setToolTipGenerator(Std);


        //rendering colors to bar chart in overlaid
        for (int i = 0; i < colors.length; i++) {
            renderer.setSeriesPaint(i, colors[i]);//r
        }

        for (int i = (colors.length - 1); i >= 0; i--) {
            renderer1.setSeriesPaint((colors.length - 1) - i, colors[i]);//r
        }
        //end of code for rendering
        if (graphProperty.isLabelsDisplayed()) {
            String measureFormat = graphProperty.getMeasureFormat();
            String measureValueRounding = graphProperty.getMeasureValueRounding();
            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
            if (measureValueRounding != null && measureValueRounding != "") {
                numberFormat.setMaximumFractionDigits(Integer.parseInt(measureValueRounding));
            }
            // numberFormat.setMaximumFractionDigits(1);
            renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", numberFormat));
            // renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE9, TextAnchor.BOTTOM_RIGHT));
            renderer.setBaseItemLabelsVisible(true);
        }
        plot.setRenderer(renderer);
        plot.setRenderer(1, renderer1);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        chart.setBorderVisible(false);
        String path = getchartPath(session, response, out);
        setChartDisplay(this.mapname, path);
        return path;
    }

    public String GetFeverChart() throws SQLException, IOException {
        final DefaultCategoryDataset dataset1 = graph1.getCategoryDataset(getRetObj());
        final DefaultCategoryDataset dataset2 = graph2.getCategoryDataset(getRetObj());
        if (funName != null && !(funName.equals(""))) {
            urlgen = true;
            this.setUrl(funName);
        }

        if (getGrplegend().equalsIgnoreCase("Y")) {
            showLegend = true;
        } else {
            showLegend = false;
        }

        if (getGraphGridLines().equalsIgnoreCase("Y")) {
            showGridLines = true;
        } else {
            showGridLines = false;
        }
        if (grpbDomainaxislabel == null || grpbDomainaxislabel.equalsIgnoreCase("null")) {
            grpbDomainaxislabel = "";
        }

        if (grplyaxislabel == null || grplyaxislabel.equalsIgnoreCase("null")) {
            grplyaxislabel = "";
        }
        grpbDomainaxislabel = getGrpbDomainaxislabel();
        grplyaxislabel = getGrplyaxislabel();
        chart = ChartFactory.createStackedAreaChart(
                "", // chart title
                grpbDomainaxislabel, // domain axis label
                grplyaxislabel, // range axis label
                dataset1, // data
                PlotOrientation.VERTICAL,
                showLegend, // include legend
                showToolTips, // tooltips?
                urlgen // URL generator?  Not required...
                );

        //chart.setBackgroundPaint(Color.white);
        backgroundColor = graphProperty.getRgbColorArr();
        //
        if (backgroundColor != null && backgroundColor.length > 0) {
            chart.setBackgroundPaint(new Color(Integer.parseInt(backgroundColor[0].trim()), Integer.parseInt(backgroundColor[1].trim()), Integer.parseInt(backgroundColor[2].trim())));
        } else {
            chart.setBackgroundPaint(Color.white);
        }
        //chart.setBackgroundPaint(new Color(230, 230, 230));
        //CategoryItemRenderer renderer = new BarRenderer();
        //aedded by k
//        BarRenderer rend = new BarRenderer();
//        rend.setBarPainter(new StandardBarPainter());
//        renderer = (BarRenderer) rend;

        StackedAreaRenderer renderer = new StackedAreaRenderer();


        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(0xEE, 0xEE, 0xFF));
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);

        LegendTitle legend = chart.getLegend();
        if (showLegend) {
            if (legend != null) {
                legend.setItemFont(font);
                if (grplegendloc.equalsIgnoreCase("Bottom")) {
                    legend.setPosition(RectangleEdge.BOTTOM);
                } else if (grplegendloc.equalsIgnoreCase("Top")) {
                    legend.setPosition(RectangleEdge.TOP);
                } else if (grplegendloc.equalsIgnoreCase("Left")) {
                    legend.setPosition(RectangleEdge.LEFT);
                } else if (grplegendloc.equalsIgnoreCase("Right")) {
                    legend.setPosition(RectangleEdge.RIGHT);
                } else {
                    legend.setPosition(RectangleEdge.BOTTOM);
                }
            }
        }

        plot.setForegroundAlpha(1f);
        plot.setBackgroundAlpha(0f);
        plot.setRangeGridlinesVisible(showGridLines);
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.setDomainGridlinesVisible(showGridLines);
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setDataset(1, dataset2);

        //plot.mapDatasetToRangeAxis(1, 1);

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        domainAxis.setTickLabelFont(font);
        domainAxis.setTickLabelPaint(Color.BLACK);
        domainAxis.setLabelFont(new Font("Verdana", 0, 9));

        java.util.List ColumnKeys = dataset1.getColumnKeys();
        if (ColumnKeys != null) {
            for (int i = 0; i < ColumnKeys.size(); i++) {
                domainAxis.addCategoryLabelToolTip(dataset1.getColumnKey(i), ColumnKeys.get(i).toString());
            }
        }
        NumberAxis axis1 = (NumberAxis) plot.getRangeAxis();
        axis1.setInverted(false);
        axis1.setTickLabelFont(new Font("Verdana", 0, 9));
        axis1.setTickLabelPaint(Color.BLACK);
        axis1.setAutoRange(true);
        axis1.setStandardTickUnits(NumberAxis.createStandardTickUnits());
        axis1.setLabelFont(new Font("Verdana", 0, 9));

        ///
        if (graph.getPrefixSymbol() == null || graph.getPrefixSymbol().equalsIgnoreCase("null")) {
            graph.setPrefixSymbol("");
        }
        if (graph.getSufffixSymbol() == null || graph.getSufffixSymbol().equalsIgnoreCase("null")) {
            graph.setSuffixSymbol("");
        }

        DecimalFormat formatter1 = null;
        formatter1 = new DecimalFormat(graph.getPrefixSymbol() + formatMode + graph.getSufffixSymbol());
        formatter1.setMaximumFractionDigits(maxFractionDigits);
        formatter1.setMinimumFractionDigits(minFractionDigits);
        axis1.setNumberFormatOverride(formatter1);//to display numbers in terms of K


        final LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();
        StandardCategoryToolTipGenerator Std = new StandardCategoryToolTipGenerator();
        StandardCategoryURLGenerator url1 = null;
        if (urlgen) {
            url1 = new ProgenCategoryURLGenerator("javascript:parent.submiturls1(", ")", this.passUrl);
            renderer2.setItemURLGenerator(url1);
            renderer.setItemURLGenerator(url1);
        }

        //Std.generateToolTip(dataset2,1,1);
        renderer.setToolTipGenerator(Std);
        renderer2.setToolTipGenerator(Std);


        //rendering colors to area chart in overlaid
        for (int i = 0; i < colors1.length; i++) {
            renderer.setSeriesPaint(i, colors1[i]);//r
        }

        for (int i = (colors.length - 1); i >= 0; i--) {
            renderer2.setSeriesPaint((colors.length - 1) - i, colors[i]);//r
        }
        //end of code for rendering
        if (graphProperty.isLabelsDisplayed()) {
            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
            String measureFormat = graphProperty.getMeasureFormat();
            String measureValueRounding = graphProperty.getMeasureValueRounding();
            if (measureValueRounding != null && measureValueRounding != "") {
                numberFormat.setMaximumFractionDigits(Integer.parseInt(measureValueRounding));
            }
            // numberFormat.setMaximumFractionDigits(1);
            renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", numberFormat));
            // renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE9, TextAnchor.BOTTOM_RIGHT));
            renderer.setBaseItemLabelsVisible(true);
        }
        plot.setRenderer(renderer);
        plot.setRenderer(1, renderer2);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        chart.setBorderVisible(false);
        String path = getchartPath(session, response, out);
        setChartDisplay(this.mapname, path);
        return path;
    }

    //added by kalyan
    public String GetOverlaidArea() throws SQLException, IOException {
        final DefaultCategoryDataset dataset1 = graph1.getCategoryDataset(getRetObj());
        final DefaultCategoryDataset dataset2 = graph2.getCategoryDataset(getRetObj());
        String AreaFirstChartType = null;
        if (funName != null && !(funName.equals(""))) {
            urlgen = true;
            this.setUrl(funName);
        }

        if (getGrplegend().equalsIgnoreCase("Y")) {
            showLegend = true;
        } else {
            showLegend = false;
        }

        if (getGraphGridLines().equalsIgnoreCase("Y")) {
            showGridLines = true;
        } else {
            showGridLines = false;
        }
        if (grpbDomainaxislabel == null || grpbDomainaxislabel.equalsIgnoreCase("null")) {
            grpbDomainaxislabel = "";
        }

        if (grplyaxislabel == null || grplyaxislabel.equalsIgnoreCase("null")) {
            grplyaxislabel = "";
        }
        grpbDomainaxislabel = getGrpbDomainaxislabel();
        grplyaxislabel = getGrplyaxislabel();
        if (!graphProperty.getFirstChartType().equalsIgnoreCase("null") && graphProperty.getFirstChartType() != "") {
            AreaFirstChartType = graphProperty.getFirstChartType();
            //  

        } else {
            AreaFirstChartType = "Area"; // Comment once vale is supplied from front End
        }      //  firstChartType = "stacked"; // Comment once vale is supplied from front End
        if (AreaFirstChartType.equalsIgnoreCase("null") || AreaFirstChartType.equalsIgnoreCase("bar")) {
            chart = ChartFactory.createBarChart(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    showLegend, // include legend
                    showToolTips, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (AreaFirstChartType.equalsIgnoreCase("bar3d")) {
            chart = ChartFactory.createBarChart(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    showLegend, // include legend
                    showToolTips, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (AreaFirstChartType.equalsIgnoreCase("line")) {
            chart = ChartFactory.createLineChart(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    showLegend, // include legend
                    showToolTips, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (AreaFirstChartType.equalsIgnoreCase("line3d")) {
            chart = ChartFactory.createLineChart3D(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    showLegend, // include legend
                    showToolTips, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (AreaFirstChartType.equalsIgnoreCase("stacked")) {
            chart = ChartFactory.createStackedBarChart(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    true, // include legend
                    true, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (AreaFirstChartType.equalsIgnoreCase("stackedarea")) {
            chart = ChartFactory.createStackedAreaChart(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    true, // include legend
                    true, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (AreaFirstChartType.equalsIgnoreCase("stacked3d")) {
            chart = ChartFactory.createStackedBarChart3D(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    true, // include legend
                    true, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (AreaFirstChartType.equalsIgnoreCase("Area")) {
            chart = ChartFactory.createAreaChart(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    showLegend, // include legend
                    showToolTips, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        }


//        chart = ChartFactory.createLineChart(
//                "", // chart title
//                "", // domain axis label
//                grplyaxislabel, // range axis label
//                dataset1, // data
//                PlotOrientation.VERTICAL,
//                showLegend, // include legend
//                showToolTips, // tooltips?
//                urlgen // URL generator?  Not required...
//                );

//        chart.setBackgroundPaint(Color.white);
//        renderer = new LineAndShapeRenderer();
//        //added by k
//        LineAndShapeRenderer rend = new LineAndShapeRenderer();
//        //rend.setLiPainter(new StandardBarPainter());
//        renderer = (LineAndShapeRenderer) rend;


        //chart.setBackgroundPaint(Color.white);
        backgroundColor = graphProperty.getRgbColorArr();
        //
        if (backgroundColor != null && backgroundColor.length > 0) {
            chart.setBackgroundPaint(new Color(Integer.parseInt(backgroundColor[0].trim()), Integer.parseInt(backgroundColor[1].trim()), Integer.parseInt(backgroundColor[2].trim())));
        } else {
            chart.setBackgroundPaint(Color.white);
        }
        if (AreaFirstChartType.equalsIgnoreCase("line")) {

            renderer = new LineAndShapeRenderer();
        } else if (AreaFirstChartType.equalsIgnoreCase("line3d")) {

            renderer = new LineRenderer3D();
        } else if (AreaFirstChartType.equalsIgnoreCase("bar")) {

            renderer = new BarRenderer();
            //added by k
            BarRenderer rend = new BarRenderer();
            //rend.setLiPainter(new StandardBarPainter());
            renderer = (BarRenderer) rend;
        } else if (AreaFirstChartType.equalsIgnoreCase("bar3d")) {

            renderer = new BarRenderer3D();
            //added by k
            BarRenderer3D rend = new BarRenderer3D();
            //rend.setLiPainter(new StandardBarPainter());
            renderer = (BarRenderer3D) rend;
        } else if (AreaFirstChartType.equalsIgnoreCase("stacked")) {
            renderer = new StackedBarRenderer();
        } else if (AreaFirstChartType.equalsIgnoreCase("stackedarea")) {
            renderer = new StackedBarRenderer();
        } else if (AreaFirstChartType.equalsIgnoreCase("stacked3d")) {
            renderer = new StackedBarRenderer3D();
        } else if (AreaFirstChartType.equalsIgnoreCase("Area")) {
            renderer = new AreaRenderer();
            //renderer = (AreaRenderer) plot.getRenderer();
        }
//        chart = ChartFactory.createAreaChart(
//                "", // chart title
//                grpbDomainaxislabel, // domain axis label
//                grplyaxislabel, // range axis label
//                dataset1, // data
//                PlotOrientation.VERTICAL,
//                showLegend, // include legend
//                showToolTips, // tooltips?
//                urlgen // URL generator?  Not required...
//                );
//
//        chart.setBackgroundPaint(Color.white);
//        //chart.setBackgroundPaint(new Color(230, 230, 230));
//        CategoryItemRenderer renderer = new AreaRenderer();
        //aedded by k
//        BarRenderer rend = new BarRenderer();
//        rend.setBarPainter(new StandardBarPainter());
//        renderer = (BarRenderer) rend;



        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(0xEE, 0xEE, 0xFF));
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);

        LegendTitle legend = chart.getLegend();
        if (showLegend) {
            if (legend != null) {
                legend.setItemFont(font);
                if (grplegendloc.equalsIgnoreCase("Bottom")) {
                    legend.setPosition(RectangleEdge.BOTTOM);
                } else if (grplegendloc.equalsIgnoreCase("Top")) {
                    legend.setPosition(RectangleEdge.TOP);
                } else if (grplegendloc.equalsIgnoreCase("Left")) {
                    legend.setPosition(RectangleEdge.LEFT);
                } else if (grplegendloc.equalsIgnoreCase("Right")) {
                    legend.setPosition(RectangleEdge.RIGHT);
                } else {
                    legend.setPosition(RectangleEdge.BOTTOM);
                }
            }
        }

        plot.setForegroundAlpha(0.7f);
        plot.setBackgroundAlpha(0f);
        plot.setRangeGridlinesVisible(showGridLines);
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.setDomainGridlinesVisible(showGridLines);
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setDataset(1, dataset2);

        //plot.mapDatasetToRangeAxis(1, 1);

        final CategoryAxis domainAxis = plot.getDomainAxis();
        if (graphProperty.getAxisLabelPosition() != null) {
            if (graphProperty.getAxisLabelPosition().equalsIgnoreCase("Horizontal")) {
                domainAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
            } else if (graphProperty.getAxisLabelPosition().equalsIgnoreCase("Vertical")) {
                domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
            } else {
                domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
            }
        } else {
            domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        }
        domainAxis.setTickLabelFont(font);
        domainAxis.setTickLabelPaint(Color.BLACK);
        domainAxis.setLabelFont(new Font("Verdana", 0, 9));

        java.util.List ColumnKeys = dataset1.getColumnKeys();
        if (ColumnKeys != null) {
            for (int i = 0; i < ColumnKeys.size(); i++) {
                domainAxis.addCategoryLabelToolTip(dataset1.getColumnKey(i), ColumnKeys.get(i).toString());
            }
        }
        NumberAxis axis1 = (NumberAxis) plot.getRangeAxis();
        axis1.setInverted(false);
        axis1.setTickLabelFont(new Font("Verdana", 0, 9));
        axis1.setTickLabelPaint(Color.BLACK);
        axis1.setAutoRange(true);
        if (graphProperty.getCalibration() != null && graphProperty.getCalibration() != "") {
            if (graphProperty.getCalibration().equalsIgnoreCase("Integer")) {
                axis1.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); // display integer values in y axis label
            } else {
                maxFractionDigits = 5;
                minFractionDigits = 2;
                axis1.setStandardTickUnits(NumberAxis.createStandardTickUnits());
            }
        } else {
            axis1.setStandardTickUnits(NumberAxis.createStandardTickUnits());
        }
        // axis1.setStandardTickUnits(NumberAxis.createStandardTickUnits());
        axis1.setLabelFont(new Font("Verdana", 0, 9));

        ///
        if (graph.getPrefixSymbol() == null || graph.getPrefixSymbol().equalsIgnoreCase("null")) {
            graph.setPrefixSymbol("");
        }
        if (graph.getSufffixSymbol() == null || graph.getSufffixSymbol().equalsIgnoreCase("null")) {
            graph.setSuffixSymbol("");
        }

        DecimalFormat formatter1 = null;
        formatter1 = new DecimalFormat(graph.getPrefixSymbol() + formatMode + graph.getSufffixSymbol());
        formatter1.setMaximumFractionDigits(maxFractionDigits);
        formatter1.setMinimumFractionDigits(minFractionDigits);
        axis1.setNumberFormatOverride(formatter1);//to display numbers in terms of K

        if (graphProperty.getSecondChartType() != null && graphProperty.getSecondChartType() != "") {
            //
            if (graphProperty.getSecondChartType().equals("line")) {
                renderer1 = new LineAndShapeRenderer();
            } else {
                renderer1 = new LineRenderer3D();  // second chart type line 3d
            }
        } else {
            renderer1 = new LineAndShapeRenderer();
        }
//        final LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();
        StandardCategoryToolTipGenerator Std = new StandardCategoryToolTipGenerator();
        StandardCategoryURLGenerator url1 = null;
        if (urlgen) {
            url1 = new ProgenCategoryURLGenerator("javascript:parent.submiturls1(", ")", this.passUrl);
            renderer1.setItemURLGenerator(url1);
            renderer.setItemURLGenerator(url1);
        }

        //Std.generateToolTip(dataset2,1,1);
        renderer.setToolTipGenerator(Std);
        renderer1.setToolTipGenerator(Std);


        //rendering colors to bar chart in overlaid
        for (int i = 0; i < colors.length; i++) {
            renderer.setSeriesPaint(i, colors[i]);//r
        }

        for (int i = (colors.length - 1); i >= 0; i--) {
            renderer1.setSeriesPaint((colors.length - 1) - i, colors[i]);//r
        }
        //end of code for rendering
        if (graphProperty.isLabelsDisplayed()) {
            String measureFormat = graphProperty.getMeasureFormat();
            String measureValueRounding = graphProperty.getMeasureValueRounding();
            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
            if (measureValueRounding != null && measureValueRounding != "") {
                numberFormat.setMaximumFractionDigits(Integer.parseInt(measureValueRounding));
            }
            // numberFormat.setMaximumFractionDigits(1);
            renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", numberFormat));
            // renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE9, TextAnchor.BOTTOM_RIGHT));
            renderer.setBaseItemLabelsVisible(true);
        }
        plot.setRenderer(renderer);
        plot.setRenderer(1, renderer1);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        chart.setBorderVisible(false);
        String path = getchartPath(session, response, out);
        setChartDisplay(this.mapname, path);
        return path;
    }

    //overloaded GetCategoryAxisChart method by santhosh.kumar@progenbusiness.com
    public String GetCategoryAxisChart() throws SQLException, IOException {
        graph.setGraphType(chartType);
        IntervalMarker TargetRangeMarker = null;
        IntervalMarker MinRangeMarker = null;
        IntervalMarker MaxRangeMarker = null;
        final DefaultCategoryDataset dataset1 = graph.getCategoryDataset(getRetObj());
        //final DefaultCategoryDataset dataset2=getStackedCategoryDataset(dataset1);


        CategoryPlot plot = null;

        if (getGrplegend().equalsIgnoreCase("Y")) {
            showLegend = true;
        } else {
            showLegend = false;
        }
        if (getGraphGridLines().equalsIgnoreCase("Y")) {
            showGridLines = true;
        } else {
            showGridLines = false;
        }
        if (funName != null && !(funName.equals(""))) {
            urlgen = true;
            this.setUrl(funName);
        }


        if (grpbDomainaxislabel == null || grpbDomainaxislabel.equalsIgnoreCase("null")) {
            grpbDomainaxislabel = "";
        }

        if (grplyaxislabel == null || grplyaxislabel.equalsIgnoreCase("null")) {
            grplyaxislabel = "";
        }
        grpbDomainaxislabel = getGrpbDomainaxislabel();
        grplyaxislabel = getGrplyaxislabel();
        if (chartType.equalsIgnoreCase("stacked") || chartType.equalsIgnoreCase("StackedArea") || chartType.equalsIgnoreCase("stacked3d") || chartType.equalsIgnoreCase("HorizontalStacked") || chartType.equalsIgnoreCase("HorizontalStacked3D")) {
            stackedType = getStackedType();
        }
        if (chartType.equalsIgnoreCase("bar")) {
            chart = ChartFactory.createBarChart(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    showLegend, // include legend
                    showToolTips, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (chartType.equalsIgnoreCase("bar3d")) {
            chart = ChartFactory.createBarChart(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    showLegend, // include legend
                    showToolTips, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (chartType.equalsIgnoreCase("column")) {
            chart = ChartFactory.createBarChart(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.HORIZONTAL,
                    showLegend, // include legend
                    showToolTips, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (chartType.equalsIgnoreCase("column3d")) {
            chart = ChartFactory.createBarChart(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.HORIZONTAL,
                    showLegend, // include legend
                    showToolTips, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (chartType.equalsIgnoreCase("line")) {
            chart = ChartFactory.createBarChart(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    showLegend, // include legend
                    showToolTips, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (chartType.equalsIgnoreCase("line3d")) {
            chart = ChartFactory.createBarChart(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    showLegend, // include legend
                    showToolTips, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (chartType.equalsIgnoreCase("stacked")) {
            if (stackedType.equalsIgnoreCase("absStacked")) {
                chart = ChartFactory.createStackedBarChart(
                        "", // chart title
                        grpbDomainaxislabel, // domain axis label
                        grplyaxislabel, // range axis label
                        dataset1, // data
                        PlotOrientation.VERTICAL,
                        showLegend, // include legend
                        showToolTips, // tooltips?
                        urlgen // URL generator?  Not required...
                        );
            } else {
                chart = ChartFactory.createStackedBarChart(
                        "", // chart title
                        grpbDomainaxislabel, // domain axis label
                        grplyaxislabel, // range axis label
                        getStackedCategoryDataset(dataset1), // data
                        PlotOrientation.VERTICAL,
                        showLegend, // include legend
                        showToolTips, // tooltips?
                        urlgen // URL generator?  Not required...
                        );
            }
        } else if (chartType.equalsIgnoreCase("StackedArea")) {
            if (stackedType.equalsIgnoreCase("absStacked")) {
                chart = ChartFactory.createStackedAreaChart(
                        "", // chart title
                        grpbDomainaxislabel, // domain axis label
                        grplyaxislabel, // range axis label
                        dataset1, // data
                        PlotOrientation.VERTICAL,
                        showLegend, // include legend
                        showToolTips, // tooltips?
                        urlgen // URL generator?  Not required...
                        );
            } else {
                chart = ChartFactory.createStackedAreaChart(
                        "", // chart title
                        grpbDomainaxislabel, // domain axis label
                        grplyaxislabel, // range axis label
                        getStackedCategoryDataset(dataset1), // data
                        PlotOrientation.VERTICAL,
                        showLegend, // include legend
                        showToolTips, // tooltips?
                        urlgen // URL generator?  Not required...
                        );
            }
        } else if (chartType.equalsIgnoreCase("stacked3d")) {
            if (stackedType.equalsIgnoreCase("absStacked")) {
                chart = ChartFactory.createStackedBarChart3D(
                        "", // chart title
                        grpbDomainaxislabel, // domain axis label
                        grplyaxislabel, // range axis label
                        dataset1, // data
                        PlotOrientation.VERTICAL,
                        showLegend, // include legend
                        showToolTips, // tooltips?
                        urlgen // URL generator?  Not required...
                        );
            } else {
                chart = ChartFactory.createStackedBarChart3D(
                        "", // chart title
                        grpbDomainaxislabel, // domain axis label
                        grplyaxislabel, // range axis label
                        getStackedCategoryDataset(dataset1), // data
                        PlotOrientation.VERTICAL,
                        showLegend, // include legend
                        showToolTips, // tooltips?
                        urlgen // URL generator?  Not required...
                        );
            }
        } else if (chartType.equalsIgnoreCase("waterfall")) {
            chart = ChartFactory.createWaterfallChart(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    showLegend, // include legend
                    showToolTips, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (chartType.equalsIgnoreCase("Area")) {
            chart = ChartFactory.createAreaChart(
                    "", // chart title
                    grpbDomainaxislabel, // domain axis label
                    grplyaxislabel, // range axis label
                    dataset1, // data
                    PlotOrientation.VERTICAL,
                    showLegend, // include legend
                    showToolTips, // tooltips?
                    urlgen // URL generator?  Not required...
                    );
        } else if (chartType.equalsIgnoreCase("HorizontalStacked")) {
            if (stackedType.equalsIgnoreCase("absStacked")) {
                chart = ChartFactory.createStackedBarChart(
                        "", // chart title
                        grpbDomainaxislabel, // domain axis label
                        grplyaxislabel, // range axis label
                        dataset1, // data
                        PlotOrientation.HORIZONTAL,
                        showLegend, // include legend
                        showToolTips, // tooltips?
                        urlgen // URL generator?  Not required...
                        );
            } else {
                chart = ChartFactory.createStackedBarChart(
                        "", // chart title
                        grpbDomainaxislabel, // domain axis label
                        grplyaxislabel, // range axis label
                        getStackedCategoryDataset(dataset1), // data
                        PlotOrientation.HORIZONTAL,
                        showLegend, // include legend
                        showToolTips, // tooltips?
                        urlgen // URL generator?  Not required...
                        );
            }
        } else if (chartType.equalsIgnoreCase("HorizontalStacked3D")) {
            if (stackedType.equalsIgnoreCase("absStacked")) {
                chart = ChartFactory.createStackedBarChart3D(
                        "", // chart title
                        grpbDomainaxislabel, // domain axis label
                        grplyaxislabel, // range axis label
                        dataset1, // data
                        PlotOrientation.HORIZONTAL,
                        showLegend, // include legend
                        showToolTips, // tooltips?
                        urlgen // URL generator?  Not required...
                        );
            } else {
                chart = ChartFactory.createStackedBarChart3D(
                        "", // chart title
                        grpbDomainaxislabel, // domain axis label
                        grplyaxislabel, // range axis label
                        getStackedCategoryDataset(dataset1), // data
                        PlotOrientation.HORIZONTAL,
                        showLegend, // include legend
                        showToolTips, // tooltips?
                        urlgen // URL generator?  Not required...
                        );
            }
        }


        // chart.setBackgroundPaint(Color.white);
        backgroundColor = graphProperty.getRgbColorArr();

        if (backgroundColor != null && backgroundColor.length > 0) {
            chart.setBackgroundPaint(new Color(Integer.parseInt(backgroundColor[0].trim()), Integer.parseInt(backgroundColor[1].trim()), Integer.parseInt(backgroundColor[2].trim())));
        } else {
            chart.setBackgroundPaint(null);
        }
        plot = chart.getCategoryPlot();
        if (chartType.equalsIgnoreCase("bar")) {
            renderer = (BarRenderer) plot.getRenderer();
            renderer = new BarRenderer();
            //added by k
//            LayeredBarRenderer rend = new LayeredBarRenderer();
//            rend.setBarPainter(new StandardBarPainter());
//            renderer = (LayeredBarRenderer) rend;
            //renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
            //renderer.setItemLabelsVisible(true);
        } else if (chartType.equalsIgnoreCase("bar3d")) {
            renderer = new BarRenderer3D();
            //renderer = (BarRenderer3D) plot.getRenderer();
            //renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
            //renderer.setItemLabelsVisible(true);
        } else if (chartType.equalsIgnoreCase("column")) {
            renderer = new BarRenderer();
            BarRenderer rend = new BarRenderer();
            rend.setBarPainter(new StandardBarPainter());
            renderer = (BarRenderer) rend;
            //renderer = (BarRenderer) plot.getRenderer();
        } else if (chartType.equalsIgnoreCase("column3d")) {
            renderer = new BarRenderer3D();
            //renderer = (BarRenderer3D) plot.getRenderer();
        } else if (chartType.equalsIgnoreCase("line")) {
            renderer = new LineAndShapeRenderer();
            //renderer = (LineAndShapeRenderer) plot.getRenderer();
        } else if (chartType.equalsIgnoreCase("line3d")) {
            renderer = new LineRenderer3D();
            //renderer = (LineRenderer3D) plot.getRenderer();
        } else if (chartType.equalsIgnoreCase("stacked")) {
            renderer = new StackedBarRenderer();
            StackedBarRenderer rend = new StackedBarRenderer();
            rend.setBarPainter(new StandardBarPainter());
            renderer = (StackedBarRenderer) rend;

            //renderer = (StackedBarRenderer) plot.getRenderer();
        } else if (chartType.equalsIgnoreCase("StackedArea")) {
            renderer = new StackedAreaRenderer();


            //renderer = (StackedBarRenderer) plot.getRenderer();
        } else if (chartType.equalsIgnoreCase("stacked3d")) {
            renderer = new StackedBarRenderer3D();
            //renderer = (StackedBarRenderer3D) plot.getRenderer();
        } else if (chartType.equalsIgnoreCase("waterfall")) {
            renderer = new WaterfallBarRenderer();
            //renderer = (WaterfallBarRenderer) plot.getRenderer();
        } else if (chartType.equalsIgnoreCase("Area")) {
            renderer = new AreaRenderer();
            //renderer = (AreaRenderer) plot.getRenderer();
        } else if (chartType.equalsIgnoreCase("HorizontalStacked")) {
            renderer = new StackedBarRenderer();
            StackedBarRenderer rend = new StackedBarRenderer();
            rend.setBarPainter(new StandardBarPainter());
            renderer = (StackedBarRenderer) rend;
        } else if (chartType.equalsIgnoreCase("HorizontalStacked3d")) {
            renderer = new StackedBarRenderer3D();
        }

        if (getTargetRange() != null && !"".equalsIgnoreCase(getTargetRange())) {
            if (getTargetRange().equalsIgnoreCase("Discrete")) {
                if (getStartValue() != null && !"".equalsIgnoreCase(getStartValue())) {
                    TargetRangeMarker = new IntervalMarker(Double.parseDouble(getStartValue()) / graph.getDividend(), Double.parseDouble(getStartValue()) / graph.getDividend());
                    TargetRangeMarker.setLabel(discreteTitle + formatDoubleAsString(Double.parseDouble(getStartValue()) / graph.getDividend()));
                    TargetRangeMarker.setLabelAnchor(RectangleAnchor.CENTER);
                }
            } else if (getTargetRange().equalsIgnoreCase("Range")) {
                if (getStartValue() != null && !"".equalsIgnoreCase(getStartValue()) && getEndValue() != null && !"".equalsIgnoreCase(getEndValue())) {
                    TargetRangeMarker = new IntervalMarker(Double.parseDouble(getStartValue()) / graph.getDividend(), Double.parseDouble(getEndValue()) / graph.getDividend());
                    TargetRangeMarker.setLabel(rangeTitle + " Target Min =" + formatDoubleAsString(Double.parseDouble(getStartValue()) / graph.getDividend()) + " and Target Max = " + formatDoubleAsString(Double.parseDouble(getEndValue()) / graph.getDividend()) + "");
                    TargetRangeMarker.setLabelAnchor(RectangleAnchor.CENTER);
                }
            }
            if (TargetRangeMarker != null) {
                TargetRangeMarker.setLabelFont(new Font("Verdana", Font.BOLD, 10));
                TargetRangeMarker.setLabelTextAnchor(TextAnchor.BOTTOM_LEFT);
                TargetRangeMarker.setLabelAnchor(RectangleAnchor.BOTTOM_LEFT);
                TargetRangeMarker.setOutlinePaint(colors[4]);
//                TargetRangeMarker.setLabelPaint(colors[9]);
//                TargetRangeMarker.setPaint(colors[9]);
                plot.addRangeMarker(TargetRangeMarker);
            }
        }
        //if (getMaxValue() != null && !"".equalsIgnoreCase(getMaxValue())) {
        if (getShowMinMaxRange() != null && "Y".equalsIgnoreCase(getShowMinMaxRange())) {
            setMaxValue(graph.getMaximumValue() + "");
            setMinValue(graph.getMinimumValue() + "");
            setAvgValue(graph.getAverageValue() + "");
            //.println("graph.avg"+graph.getAverageValue());

            if (getMaxValue() != null && !"".equalsIgnoreCase(getMaxValue())) {
                MaxRangeMarker = new IntervalMarker(Double.parseDouble(getMaxValue()), Double.parseDouble(getMaxValue()));
                MaxRangeMarker.setLabel(maxRangeTitle + formatDoubleAsString(Double.parseDouble(getMaxValue())));
                //MaxRangeMarker.setLabel(maxRangeTitle + formatStringAsNumber(getMaxValue()));

                MaxRangeMarker.setLabelFont(font);
                //MaxRangeMarker.setLabelAnchor(RectangleAnchor.CENTER);
                MaxRangeMarker.setLabelAnchor(RectangleAnchor.CENTER);
                //MaxRangeMarker.setLabelPaint(colors[9]);
                //MaxRangeMarker.setPaint(colors[9]);
                plot.addRangeMarker(MaxRangeMarker);
            }
            if (getMinValue() != null && !"".equalsIgnoreCase(getMinValue())) {
                MinRangeMarker = new IntervalMarker(Double.parseDouble(getMinValue()), Double.parseDouble(getMinValue()));
                MinRangeMarker.setLabel(minRangeTitle + formatDoubleAsString(Double.parseDouble(getMinValue())));
                //MaxRangeMarker.setLabel(minRangeTitle + formatStringAsNumber(getMinValue()));
                MinRangeMarker.setLabelFont(font);
                //MinRangeMarker.setLabelAnchor(RectangleAnchor.CENTER);
                MinRangeMarker.setLabelAnchor(RectangleAnchor.CENTER);

                //MinRangeMarker.setLabelPaint(colors[9]);
                //MinRangeMarker.setPaint(colors[9]);
                plot.addRangeMarker(MinRangeMarker);

            }
            if (getAvgValue() != null && !"".equalsIgnoreCase(getAvgValue())) {
                MinRangeMarker = new IntervalMarker(Double.parseDouble(getAvgValue()), Double.parseDouble(getAvgValue()));
                MinRangeMarker.setLabel(avgRangeTitle + formatDoubleAsString(Double.parseDouble(getAvgValue())));
                MinRangeMarker.setLabelFont(font);
                MinRangeMarker.setLabelAnchor(RectangleAnchor.CENTER);
                plot.addRangeMarker(MinRangeMarker);

            }





        }
        plot.setBackgroundPaint(Color.white);//use setter and getter to change bk color later
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);

        LegendTitle legend = chart.getLegend();
        if (showLegend) {
            if (legend != null) {
                legend.setItemFont(font);
                if (grplegendloc.equalsIgnoreCase("Bottom")) {
                    legend.setPosition(RectangleEdge.BOTTOM);
                } else if (grplegendloc.equalsIgnoreCase("Top")) {
                    legend.setPosition(RectangleEdge.TOP);
                } else if (grplegendloc.equalsIgnoreCase("Left")) {
                    legend.setPosition(RectangleEdge.LEFT);
                } else if (grplegendloc.equalsIgnoreCase("Right")) {
                    legend.setPosition(RectangleEdge.RIGHT);
                } else {
                    legend.setPosition(RectangleEdge.BOTTOM);
                }
            }
        }
        if (chartType.equalsIgnoreCase("Area") || chartType.equalsIgnoreCase("StackedArea")) {
            plot.setForegroundAlpha(0.5f);
        } else {
            plot.setForegroundAlpha(1f);
        }

        plot.setBackgroundAlpha(0f);
        plot.setRangeGridlinesVisible(showGridLines);
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.setDomainGridlinesVisible(showGridLines);
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.mapDatasetToRangeAxis(1, 1);
//
//        CategoryAxis label = null;
//        if(leftYAxis!=null ){
//            if(grplyaxislabel!=null && !grplyaxislabel.equalsIgnoreCase("null")){
//                
//
//                label.setLabelFont(font);
//            
//            if(leftYAxis.equalsIgnoreCase("LEFT"))
//                label.setCategoryLabelPositions(CategoryLabelPositions.replaceLeftPosition(CategoryLabelPositions.UP_90, null));
//
//         }
//        }
        CategoryAxis domainAxis = plot.getDomainAxis();
        if (graphProperty.getAxisLabelPosition() != null) {
            if (graphProperty.getAxisLabelPosition().equalsIgnoreCase("Horizontal")) {
                domainAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
            } else if (graphProperty.getAxisLabelPosition().equalsIgnoreCase("Vertical")) {
                domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
            } else {
                domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
            }
        } else {
            domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        }
        domainAxis.setTickLabelFont(font);
        domainAxis.setLabelFont(font);
        java.util.List ColumnKeys = dataset1.getColumnKeys();
        if (ColumnKeys != null) {
            for (int i = 0; i < ColumnKeys.size(); i++) {
                domainAxis.addCategoryLabelToolTip(dataset1.getColumnKey(i), ColumnKeys.get(i).toString());
            }
        }
        NumberAxis axis1 = (NumberAxis) plot.getRangeAxis();
        axis1.setInverted(false);
        axis1.setTickLabelFont(new Font("Verdana", 0, 9));
        axis1.setTickLabelPaint(Color.BLACK);
        axis1.setLabelFont(font);
        axis1.getLowerBound();
        axis1.getUpperBound();
        if (graphProperty.getCalibration() != null && graphProperty.getCalibration() != "") {
            if (graphProperty.getCalibration().equalsIgnoreCase("Integer")) {
                maxFractionDigits = 0;
                minFractionDigits = 0;
                if (axis1.getUpperBound() - axis1.getLowerBound() < 10 && (axis1.getUpperBound() - axis1.getLowerBound()) > 1d) {
                    axis1.setAutoRangeMinimumSize(axis1.getUpperBound() - axis1.getLowerBound());
                } else {
                    axis1.setAutoRange(true);
                }
            } else {
                maxFractionDigits = 5;
                minFractionDigits = 2;
                if (axis1.getUpperBound() - axis1.getLowerBound() < 10 && (axis1.getUpperBound() - axis1.getLowerBound()) > 1d) {
                    axis1.setAutoRangeMinimumSize(axis1.getUpperBound() - axis1.getLowerBound());
                } else {
                    axis1.setAutoRange(true);
                }

            }
        } else {
            if (axis1.getUpperBound() - axis1.getLowerBound() < 10 && (axis1.getUpperBound() - axis1.getLowerBound()) > 1d) {
                //
                axis1.setAutoRangeMinimumSize(axis1.getUpperBound() - axis1.getLowerBound());
                maxFractionDigits = 0;
                minFractionDigits = 0;

            } else {
                axis1.setAutoRange(true);
            }
        }
        //axis1.setLabelAngle(90d);

        if (graph.getPrefixSymbol() == null || graph.getPrefixSymbol().equalsIgnoreCase("null")) {
            graph.setPrefixSymbol("");
        }
        if (graph.getSufffixSymbol() == null || graph.getSufffixSymbol().equalsIgnoreCase("null")) {
            graph.setSuffixSymbol("");
        }

        DecimalFormat formatter1 = null;
        formatter1 = new DecimalFormat(graph.getPrefixSymbol() + formatMode + graph.getSufffixSymbol());
//        
        formatter1.setMaximumFractionDigits(maxFractionDigits);
        formatter1.setMinimumFractionDigits(minFractionDigits);
        axis1.setNumberFormatOverride(formatter1);//to display numbers in terms of K
        axis1.setAutoRange(true);
        if (graphProperty.getCalibration() != null && graphProperty.getCalibration() != "") {
            if (graphProperty.getCalibration().equalsIgnoreCase("Integer")) {
                axis1.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); // display integer values in y axis label
            } else {
                axis1.setStandardTickUnits(NumberAxis.createStandardTickUnits());
            }
        } else {
            axis1.setStandardTickUnits(NumberAxis.createStandardTickUnits());
        }

        // code commented to display values on top of bar.
//    renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", formatter1));
//    renderer.setItemLabelsVisible(true);
//    renderer.setBaseItemLabelsVisible(true);




        if (chartType.equalsIgnoreCase("column")) {
            plot.setDomainAxisLocation(AxisLocation.TOP_OR_LEFT);//for domainAxis left
            plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);//for range axis to
        }
        if (chartType.equalsIgnoreCase("column3d")) {
            plot.setDomainAxisLocation(AxisLocation.TOP_OR_LEFT);//for domainAxis left
            plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);//for range axis to
        }


        //added by k on may -19-2010.
        //overriding series paint colors
        for (int i = 0; i < colors.length; i++) {
            renderer.setSeriesPaint(i, colors[i]);//r
        }


        if (chartType.equalsIgnoreCase("waterfall")) {
            WaterfallBarRenderer rend = new WaterfallBarRenderer();
            rend.setBarPainter(new StandardBarPainter());

            rend.setFirstBarPaint(new Color(100, 178, 255));
            rend.setLastBarPaint(new Color(225, 117, 95));
            rend.setPositiveBarPaint(new Color(181, 249, 80));
            renderer = (WaterfallBarRenderer) rend;
        }






        ProGenStandardCategoryToolTipGenerator Std = new ProGenStandardCategoryToolTipGenerator();
        if (chartType.equalsIgnoreCase("stacked") || chartType.equalsIgnoreCase("StackedArea") || chartType.equalsIgnoreCase("stacked3d") || chartType.equalsIgnoreCase("HorizontalStacked") || chartType.equalsIgnoreCase("HorizontalStacked3D")) {
            if (stackedType.equalsIgnoreCase("prcStacked")) {
                Std.setPostFix("%");
                renderer.setToolTipGenerator(Std);
            }
        } else {
            Std.setPostFix("");
            renderer.setToolTipGenerator(Std);
        }
        StandardCategoryURLGenerator url1 = null;

        if (urlgen) {
            url1 = new ProgenCategoryURLGenerator("javascript:parent.submiturls1(", ")", this.passUrl);
            renderer.setItemURLGenerator(url1);
        }
        renderer.setToolTipGenerator(Std);
        /*
         * if (chartType.equalsIgnoreCase("stacked")) {
         * renderer.setToolTipGenerator(Std); } else {
         * renderer.setToolTipGenerator(Std); }
         */


        //renderer.setItemLabelAnchorOffset(-90d);

        renderer.setNegativeItemLabelPosition(new ItemLabelPosition());
        if (graphProperty.isLabelsDisplayed()) {
            String measureFormat = graphProperty.getMeasureFormat();
            String measureValueRounding = graphProperty.getMeasureValueRounding();
            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
            //
            if (measureValueRounding != null && measureValueRounding != "" && !measureValueRounding.equalsIgnoreCase("null")) {
                numberFormat.setMaximumFractionDigits(Integer.parseInt(measureValueRounding));
            }
            renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", numberFormat));
//            if(chartType.equalsIgnoreCase("column") && plot.getOrientation()==PlotOrientation.HORIZONTAL)
            renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.TOP_CENTER, TextAnchor.TOP_CENTER, Math.PI * 2));
            renderer.setBaseItemLabelsVisible(true);
        }
        plot.setRenderer(renderer);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        chart.setBorderVisible(false);
        String path = getchartPath(session, response, out);
        setChartDisplay(this.mapname, path);
        return path;
    }

    //overloaded GetPieAxisChart method by santhosh.kumar@progenbusiness.com
    public String GetPieAxisChart() throws SQLException, IOException {
        final DefaultPieDataset dataset1 = graph.getPieDataset(getRetObj());
        graph.setGraphType(chartType);
        DefaultCategoryDataset dataset2 = null;
        PiePlot p = null;
        TableOrder tableOrder = null;
        String path = "";
        if (getGrplegend().equalsIgnoreCase("Y")) {
            showLegend = true;
        } else {
            showLegend = false;
        }

        if (getGraphGridLines().equalsIgnoreCase("Y")) {
            showGridLines = true;
        } else {
            showGridLines = false;
        }
        tableOrder = TableOrder.BY_ROW;

        if (funName != null && !(funName.equals(""))) {
            urlgen = true;
            this.setUrl(funName);
        }


        if (measurePosition == null || measurePosition.trim().equalsIgnoreCase("") || measurePosition.trim().equalsIgnoreCase("Not Display")) {
            pieMeasureLabel = "";
        }
//       // 
//        
//        
        if (chartType.equalsIgnoreCase("pie")) {
            chart = ChartFactory.createPieChart(pieMeasureLabel,
                    dataset1,
                    showLegend,
                    showToolTips,
                    urlgen);
        } else if (chartType.equalsIgnoreCase("pie3d")) {
            chart = ChartFactory.createPieChart3D(pieMeasureLabel,
                    dataset1,
                    showLegend,
                    showToolTips,
                    urlgen);
        } else if (chartType.equalsIgnoreCase("ring")) {
            chart = ChartFactory.createRingChart(pieMeasureLabel,
                    dataset1,
                    showLegend,
                    showToolTips,
                    urlgen);
        } //code for column pie added by santhosh.kumar@progenbusiness.com on 22-12-2009
        else if (chartType.equalsIgnoreCase("columnPie")) {

            graph.setRowValues(getRowValuesList());
            dataset2 = graph.getCategoryDataset(getRetObj());
            chart = ChartFactory.createMultiplePieChart(
                    "",
                    dataset2,
                    tableOrder,
                    showLegend,
                    showToolTips,
                    urlgen);
        } else if (chartType.equalsIgnoreCase("columnPie3D")) {
            graph.setRowValues(getRowValuesList());
            dataset2 = graph.getCategoryDataset(getRetObj());
            chart = ChartFactory.createMultiplePieChart3D(
                    "",
                    dataset2,
                    tableOrder,
                    showLegend,
                    showToolTips,
                    urlgen);
        }
        //end of code on 22/12/2009


        if (chart != null) {
            //chart.setBackgroundPaint(Color.white);
            backgroundColor = graphProperty.getRgbColorArr();
            //
            if (backgroundColor != null && backgroundColor.length > 0) {
                chart.setBackgroundPaint(new Color(Integer.parseInt(backgroundColor[0].trim()), Integer.parseInt(backgroundColor[1].trim()), Integer.parseInt(backgroundColor[2].trim())));
            } else {
                chart.setBackgroundPaint(Color.white);
            }
            if (chartType.equalsIgnoreCase("pie")) {
                p = (PiePlot) chart.getPlot();
                p.setForegroundAlpha(1f);
            } else if (chartType.equalsIgnoreCase("pie3d")) {
                p = (PiePlot3D) chart.getPlot();
                p.setForegroundAlpha(0.7f);
            } else if (chartType.equalsIgnoreCase("ring")) {
                p = (RingPlot) chart.getPlot();
                p.setForegroundAlpha(1f);
                ((RingPlot) p).setSeparatorsVisible(false);
            } //added by santhosh.kumar@progenbusiness.com on 23/12/2009 for column pie chart
            else if (chartType.equalsIgnoreCase("ColumnPie")) {
            } else if (chartType.equalsIgnoreCase("ColumnPie3D")) {
            }
            //end oc code on 23/12/2009

            if (showLegend) {
                LegendTitle legend = chart.getLegend();
                if (legend != null) {
                    legend.setItemFont(font);
                    if (grplegendloc.equalsIgnoreCase("Bottom")) {
                        chart.getLegend().setPosition(RectangleEdge.BOTTOM);
                    } else if (grplegendloc.equalsIgnoreCase("Top")) {
                        chart.getLegend().setPosition(RectangleEdge.TOP);
                    } else if (grplegendloc.equalsIgnoreCase("Left")) {
                        chart.getLegend().setPosition(RectangleEdge.LEFT);
                    } else if (grplegendloc.equalsIgnoreCase("Right")) {
                        chart.getLegend().setPosition(RectangleEdge.RIGHT);
                    } else {
                        chart.getLegend().setPosition(RectangleEdge.BOTTOM);
                    }
                }
            }

            /*
             * axis1.setTickLabelFont(new Font("Verdana", 0, 9));
             * axis1.setTickLabelPaint(Color.BLACK); axis1.setLabelFont(font);
             */

            //TextTitle txtTitle= chart.getTitle();
            //txtTitle.setFont(new Font("Verdana", 0, 9));
            //txtTitle.setVerticalAlignment(VerticalAlignment.CENTER);
            //txtTitle.setTextAlignment(HorizontalAlignment.CENTER);
            //txtTitle.setPosition(RectangleEdge.RIGHT);
            //txtTitle.setVisible(true);

            //chart.setTitle(txtTitle);
            TextTitle txtTitle = null;
            if (measurePosition != null && !measurePosition.equalsIgnoreCase("null")) {
                if (pieMeasureLabel != null) {
                    txtTitle = chart.getTitle();
                    //
                    txtTitle.setFont(new Font("Verdana", 0, 9));
                    // 
                    measurePosition = measurePosition.trim();
                    //
                    if (measurePosition.equalsIgnoreCase("BOTTOM")) {
                        //
                        txtTitle.setPosition(RectangleEdge.BOTTOM);
                    } else if (measurePosition.equalsIgnoreCase("TOP")) {
                        //
                        txtTitle.setPosition(RectangleEdge.TOP);
                    } else if (measurePosition.equalsIgnoreCase("LEFT")) {
                        //
                        txtTitle.setPosition(RectangleEdge.LEFT);
                    } else if (measurePosition.equalsIgnoreCase("RIGHT")) {
                        //
                        txtTitle.setPosition(RectangleEdge.RIGHT);
                    }
                }
                txtTitle.setVerticalAlignment(VerticalAlignment.CENTER);
                txtTitle.setTextAlignment(HorizontalAlignment.CENTER);

                txtTitle.setVisible(true);
                chart.setTitle(txtTitle);

            }

            //added by kalyan.krishna@progenbusiness.com 18-11-09
            if (!chartType.equalsIgnoreCase("columnPie3D") && !chartType.equalsIgnoreCase("columnPie")) {
                if (chartType.equalsIgnoreCase("ring")) {
                    for (int colIndex = 0; colIndex < colors.length; colIndex++) {
                        if (colIndex < colors.length) {
                            p.setSectionPaint(colIndex, colors[colIndex]);
                            p.setCircular(true);
                        }
                    }
                } else {
                    for (int colIndex = 0; colIndex < colors.length; colIndex++) {
                        if (colIndex < colors.length) {
                            //p.setSectionPaint(colIndex, new GradientPaint(0, 0, colors[colIndex], 0, 350, Color.white, true));
                            p.setSectionPaint(colIndex, colors[colIndex]);
                            p.setCircular(true);
                            //p.setCircular(true);

                        }
                    }
                }

                //code to dsiplay % value in pie tails
                BigDecimal totalValue = new BigDecimal(BigInteger.ZERO);
                if (dataset1 != null) {
                    for (int i = 0; i < dataset1.getItemCount(); i++) {
                        // 
                        totalValue = totalValue.add(new BigDecimal(dataset1.getValue(i).doubleValue()));
                    }
                }
                p.setLabelGenerator(new PieLabelGenerator("{2}", NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance(), totalValue));
//               p.setLabelGenerator(new StandardPieSectionLabelGenerator("{2}", NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance()));
//               PieSectionLabelGenerator generator = new StandardPieSectionLabelGenerator("{2}", new DecimalFormat("0"), new DecimalFormat("0%"));
//               p.setLabelGenerator(generator);
            } else {
                final MultiplePiePlot plot = (MultiplePiePlot) chart.getPlot();
                final JFreeChart pieChart = plot.getPieChart();
                p = (PiePlot) pieChart.getPlot();
                if (chartType.equalsIgnoreCase("columnPie3D")) {
                    p.setForegroundAlpha(0.7f);
                } else {
                    p.setForegroundAlpha(1f);
                }


                p.setCircular(false);
                for (int i = 0; i < dataset2.getColumnCount(); i++) {
                    if (i < colors.length) {
                        p.setSectionPaint(dataset2.getColumnKey(i).toString(), colors[i]);
                    }
                }
                //need to modify label font
                p.setLabelFont(font);
                if (pieChart.getLegend() != null) {
                    pieChart.getLegend().setItemFont(font);
                }
                TextTitle title = pieChart.getTitle();
                title.setFont(font);
                TextTitle subtitle;
                for (int ix = 0; ix < pieChart.getSubtitleCount(); ix++) {
                    subtitle = (TextTitle) pieChart.getSubtitle(ix);
                    subtitle.setFont(font);
                }

            }
            p.setBackgroundAlpha(0f);
            if (graphProperty.isLabelsDisplayed()) {
                BigDecimal totalValue = new BigDecimal(BigInteger.ZERO);
                if (dataset1 != null) {
                    for (int i = 0; i < dataset1.getItemCount(); i++) {
                        //
                        totalValue = totalValue.add(new BigDecimal(dataset1.getValue(i).doubleValue()));
                    }
                }
                // plot.setLabelPaint(Color.GRAY);
                p.setLabelGenerator(generator);
                p.setLabelLinksVisible(false);
                p.setLabelOutlineStroke(null);
                p.setLabelBackgroundPaint(null);
                p.setLabelOutlinePaint(null);
                p.setLabelShadowPaint(null);
                p.setSimpleLabels(true);
                String measureFormat = null;
                String measureValueRounding = null;
                measureFormat = graphProperty.getMeasureFormat();
                measureValueRounding = graphProperty.getMeasureValueRounding();
                NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
                NumberFormat percentFormat = NumberFormat.getPercentInstance();
                // 
                if (measureValueRounding != null && measureValueRounding != "" && !measureValueRounding.equalsIgnoreCase("null")) {
                    numberFormat.setMaximumFractionDigits(Integer.parseInt(measureValueRounding));
                    numberFormat.setMinimumFractionDigits(Integer.parseInt(measureValueRounding));
                    percentFormat.setMaximumFractionDigits(Integer.parseInt(measureValueRounding));
                    percentFormat.setMinimumFractionDigits(Integer.parseInt(measureValueRounding));
                }
                if (measureFormat != null && measureFormat.equalsIgnoreCase("value")) {
                    p.setLabelGenerator(new StandardPieSectionLabelGenerator("{1}"));

                } else {
                    p.setLabelGenerator(new StandardPieSectionLabelGenerator("{2}", numberFormat, percentFormat));
                    p.setLabelGenerator(new PieLabelGenerator("{2}", numberFormat, percentFormat, totalValue));
                }
                // p.setLabelGenerator(new StandardPieSectionLabelGenerator("{2}", NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance()));
                // p.setLabelGenerator(new PieLabelGenerator("{2}", NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance(), totalValue));
                // PieSectionLabelGenerator generator = new StandardPieSectionLabelGenerator("{2}", new DecimalFormat("0"), new DecimalFormat("0%"));
                // p.setLabelGenerator(generator);
            } else {
                p.setLabelGenerator(generator);
            }
            p.setIgnoreZeroValues(false);
            p.setStartAngle(150f);
            p.setInteriorGap(.01);
            p.setLabelFont(font);//added by santosh.kumar@progenbusiness.com on 12/08/09 for reducing label font size
            p.setOutlineVisible(false);
            StandardPieURLGenerator url1 = null;
            if (urlgen) {
                url1 = new ProgenStandardPieURLGenerator("javascript:parent.submiturls1(", ")", this.passUrl, 0);
                p.setURLGenerator(url1);
            }
            //chart.removeLegend();

            chart.setBorderVisible(false);
            path = getchartPath(session, response, out);
            setChartDisplay(this.mapname, path);
        }
        return path;
    }

    public String GetMeterChart(double startRange, double endRange, double firstBreak, double secondBreak, double NeddleValue, String unitName, HttpSession session, HttpServletResponse response, Writer out) throws SQLException, IOException {
        DefaultValueDataset data4 = new DefaultValueDataset(NeddleValue);


        MeterPlot plot = new MeterPlot(data4);

        chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, false);



        if (getGrplegend().equalsIgnoreCase("Y")) {
            showLegend = true;
        } else {
            showLegend = false;
        }

        if (getGraphGridLines().equalsIgnoreCase("Y")) {
            showGridLines = true;
        } else {
            showGridLines = false;
        }

        if (showLegend) {
            LegendTitle legend = chart.getLegend();
            if (legend != null) {
                legend.setItemFont(font);

                if (grplegendloc.equalsIgnoreCase("Bottom")) {
                    chart.getLegend().setPosition(RectangleEdge.BOTTOM);
                } else if (grplegendloc.equalsIgnoreCase("Top")) {
                    chart.getLegend().setPosition(RectangleEdge.TOP);
                } else if (grplegendloc.equalsIgnoreCase("Left")) {
                    chart.getLegend().setPosition(RectangleEdge.LEFT);
                } else if (grplegendloc.equalsIgnoreCase("Right")) {
                    chart.getLegend().setPosition(RectangleEdge.RIGHT);
                } else {
                    chart.getLegend().setPosition(RectangleEdge.BOTTOM);
                }
            }

        }

        //chart.setBackgroundPaint(new Color(230, 230, 230));
        //chart.setBackgroundPaint(Color.white);



        /*
         * You need to set the range of the complete dial, in this case our
         * range is from 0 to 14000
         */

        plot.setUnits(unitName);

        plot.setRange(new Range(startRange, endRange));

        /*
         * The next step is to add an interval in the Meter, our first range is
         * from 0 to 5000 and we label this section to be critical
         *
         * we also indicate the color of this section using the RGB value new
         * Color(255, 0, 0, 128)
         *
         */


        // g new Color(181,249,80),
        // b new Color(100,178,255),
        //r  new Color(225,117,95),

        if (getColorchange().equalsIgnoreCase("no")) {
            plot.addInterval(new MeterInterval("Low", new Range(startRange, firstBreak),
                    Color.WHITE, new BasicStroke(2.0f),
                    new Color(255, 91, 91)));

            /*
             * similarly you can add as many range or section but the final
             * range should be ending in 14000 and all the ranges should be
             * defined in sequential order
             *
             * for e.g. {0, 5000} , { 5000, 7000} , { 7000,14000}
             *
             */

            plot.addInterval(new MeterInterval("Fine", new Range(firstBreak, secondBreak),
                    Color.WHITE, new BasicStroke(2.0f), new Color(239, 230, 98)));

            plot.addInterval(new MeterInterval("High", new Range(secondBreak, endRange),
                    Color.WHITE, new BasicStroke(2.0f),
                    new Color(181, 249, 80)));
        } else {

            plot.addInterval(new MeterInterval("Low", new Range(startRange, firstBreak),
                    Color.WHITE, new BasicStroke(2.0f),
                    new Color(181, 249, 80)));

            /*
             * similarly you can add as many range or section but the final
             * range should be ending in 14000 and all the ranges should be
             * defined in sequential order
             *
             * for e.g. {0, 5000} , { 5000, 7000} , { 7000,14000}
             *
             */

            plot.addInterval(new MeterInterval("Fine", new Range(firstBreak, secondBreak),
                    Color.WHITE, new BasicStroke(2.0f), new Color(239, 230, 98)));

            plot.addInterval(new MeterInterval("High", new Range(secondBreak, endRange),
                    Color.WHITE, new BasicStroke(2.0f),
                    new Color(255, 91, 91)));





        }




        /*
         * remaining properties are self explanatory
         */

        plot.setNeedlePaint(Color.darkGray);
        plot.setDialBackgroundPaint(Color.white);
        plot.setDialOutlinePaint(Color.BLACK);
        plot.setDialShape(DialShape.CHORD);
        plot.setMeterAngle(180);
        plot.setTickLabelsVisible(true);
        plot.setTickLabelFont(new Font("Dialog", Font.BOLD, 10));
        plot.setTickLabelPaint(Color.darkGray);
        plot.setTickSize(500.0);
        plot.setTickPaint(Color.lightGray);
        plot.setValuePaint(Color.black);

        // GradientPaint gradientPaint = new GradientPaint(0.0F, 10.0F, Color.WHITE, h, w, Color.green.darker());
        //plot.setBackgroundPaint(gradientPaint);

        //chart.setBackgroundPaint(Color.BLUE);        
        chart.getTitle().setPaint(Color.white);
        if (graphProperty != null && graphProperty.getRgbColorArr() != null) {
            backgroundColor = graphProperty.getRgbColorArr();
        }
        //
        if (backgroundColor != null && backgroundColor.length > 0) {
            chart.setBackgroundPaint(new Color(Integer.parseInt(backgroundColor[0].trim()), Integer.parseInt(backgroundColor[1].trim()), Integer.parseInt(backgroundColor[2].trim())));
        } else {
            chart.setBackgroundPaint(Color.white);
        }
        // chart.setBackgroundPaint(Color.white);
        //chart.setBackgroundPaint(new Color(230, 230, 230));
        plot.setValueFont(new Font("Dialog", Font.BOLD, 14));






        String path = getchartPath(session, response, out);
        setChartDisplay(this.mapname, path);
        return path;
    }

    public String GetThermChart(double startRange, double endRange, double firstBreak, double secondBreak, double mercuryValue, HttpSession session, HttpServletResponse response, Writer out) throws SQLException, IOException {
        DefaultValueDataset data4 = new DefaultValueDataset(mercuryValue);
        ThermometerPlot plot = new ThermometerPlot(data4);
        chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, false);
        plot.setThermometerStroke(new BasicStroke(2.0f));

        plot.setOutlineVisible(false);

        //plot.setThermometerPaint(Color.lightGray);
        plot.setThermometerPaint(Color.WHITE);

        plot.setUnits(ThermometerPlot.UNITS_NONE);
        plot.setRange(startRange + (startRange / 5), endRange);
        //plot.setBackgroundPaint(gradientPaint);
        // chart.setBackgroundPaint(Color.white);
        if (graphProperty != null && graphProperty.getRgbColorArr() != null) {
            backgroundColor = graphProperty.getRgbColorArr();
        }
        //
        if (backgroundColor != null && backgroundColor.length > 0) {
            chart.setBackgroundPaint(new Color(Integer.parseInt(backgroundColor[0].trim()), Integer.parseInt(backgroundColor[1].trim()), Integer.parseInt(backgroundColor[2].trim())));
        } else {
            chart.setBackgroundPaint(Color.white);
        }
        if (getGrplegend().equalsIgnoreCase("Y")) {
            showLegend = true;
        } else {
            showLegend = false;
        }

        if (getGraphGridLines().equalsIgnoreCase("Y")) {
            showGridLines = true;
        } else {
            showGridLines = false;
        }

        if (showLegend) {
            LegendTitle legend = chart.getLegend();
            if (legend != null) {
                legend.setItemFont(font);
                if (grplegendloc.equalsIgnoreCase("Bottom")) {
                    legend.setPosition(RectangleEdge.BOTTOM);
                } else if (grplegendloc.equalsIgnoreCase("Top")) {
                    legend.setPosition(RectangleEdge.TOP);
                } else if (grplegendloc.equalsIgnoreCase("Left")) {
                    legend.setPosition(RectangleEdge.LEFT);
                } else if (grplegendloc.equalsIgnoreCase("Right")) {
                    legend.setPosition(RectangleEdge.RIGHT);
                } else {
                    legend.setPosition(RectangleEdge.BOTTOM);
                }
            }
        }

        plot.setMercuryPaint(new Color(199, 92, 92));
        plot.setValuePaint(new Color(111, 165, 115)); //Change the color of the number inside the bulb
        plot.setThermometerPaint(new Color(116, 131, 192)); //Change the outside paint of the thermometer

        plot.setSubrange(0, startRange, firstBreak);
        plot.setSubrange(1, firstBreak + 0.1, secondBreak);
        plot.setSubrange(2, secondBreak + 0.1, endRange);

        //     plot.setSubrangePaint(0, new Color(199, 92, 92));//r
        //    plot.setSubrangePaint(1, new Color(111, 165, 115));//g
        // plot.setSubrangePaint(2, new Color(116, 131, 192));//b

//        plot.setSubrangePaint(0, new Color(255, 91, 91));//r
//        plot.setSubrangePaint(1, new Color(181, 249, 80));//g
//        plot.setSubrangePaint(2, new Color(100, 178, 255));//b
        plot.setSubrangePaint(0, Color.red);
        plot.setSubrangePaint(1, Color.yellow);
        plot.setSubrangePaint(2, Color.green);



        // g new Color(181,249,80),
        // b new Color(100,178,255),
        //r  new Color(225,117,95),


        String path = getchartPath(session, response, out);
        setChartDisplay(this.mapname, path);
        return path;
    }

    public String getCtxPath() {
        return ctxPath;
    }

    public void setCtxPath(String ctxPath) {
        this.ctxPath = ctxPath;
    }

    public boolean getSwapColumn() {
        return swapColumn;
    }

    public void setSwapColumn(boolean swapColumn) {
        this.swapColumn = swapColumn;
    }

    public ArrayList getAlist() {
        return alist;
    }

    public void setAlist(ArrayList alist) {
        this.alist = alist;
    }

    public ProgenChartDatasets getGraph() {
        return graph;
    }

    public void setGraph(ProgenChartDatasets graph) {
        this.graph = graph;
    }

    public ProgenChartDatasets getGraph1() {
        return graph1;
    }

    public void setGraph1(ProgenChartDatasets graph1) {
        this.graph1 = graph1;
    }

    public ProgenChartDatasets getGraph2() {
        return graph2;
    }

    public void setGraph2(ProgenChartDatasets graph2) {
        this.graph2 = graph2;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public String getFunName() {
        return funName;
    }

    public void setFunName(String funName) {
        this.funName = funName;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public Writer getOut() {
        return out;
    }

    public void setOut(Writer out) {
        this.out = out;
    }

    public String getGrplegend() {
        return grplegend;
    }

    public void setGrplegend(String grplegend) {
        this.grplegend = grplegend;
    }

    public String getGrplegendloc() {
        return grplegendloc;
    }

    public void setGrplegendloc(String grplegendloc) {
        this.grplegendloc = grplegendloc;
    }

    public String getGrpshox() {
        return grpshox;
    }

    public void setGrpshox(String grpshox) {
        this.grpshox = grpshox;
    }

    public String getGrpshoy() {
        return grpshoy;
    }

    public void setGrpshoy(String grpshoy) {
        this.grpshoy = grpshoy;
    }

    public String getGrplyaxislabel() {
        return grplyaxislabel;
    }

    public void setGrplyaxislabel(String grplyaxislabel) {
        this.grplyaxislabel = grplyaxislabel;
    }

    public String getGrpryaxislabel() {
        return grpryaxislabel;
    }

    public void setGrpryaxislabel(String grpryaxislabel) {
        this.grpryaxislabel = grpryaxislabel;
    }

    public String getGrpdrill() {
        return grpdrill;
    }

    public void setGrpdrill(String grpdrill) {
        this.grpdrill = grpdrill;
    }

    public String getGrpbcolor() {
        return grpbcolor;
    }

    public void setGrpbcolor(String grpbcolor) {
        this.grpbcolor = grpbcolor;
    }

    public String getGrpfcolor() {
        return grpfcolor;
    }

    public void setGrpfcolor(String grpfcolor) {
        this.grpfcolor = grpfcolor;
    }

    public String getGrpdata() {
        return grpdata;
    }

    public void setGrpdata(String grpdata) {
        this.grpdata = grpdata;
    }

    public ArrayList getRowValuesList() {
        return RowValuesList;
    }

    public void setRowValuesList(ArrayList RowValuesList) {
        this.RowValuesList = RowValuesList;
    }

    public String getPrefixSymbol() {
        return prefixSymbol;
    }

    public void setPrefixSymbol(String prefixSymbol) {
        this.prefixSymbol = prefixSymbol;
    }

    public String GetBubbleChart() throws SQLException, IOException {
        //final DefaultXYZDataset dataset1 = graph.getBubbleDataset(getAlist());
        final DefaultXYZDataset dataset1 = graph.getBubbleDataset(getRetObj());



        if (getGrplegend().equalsIgnoreCase("Y")) {
            showLegend = true;
        } else {
            showLegend = false;
        }

        if (getGraphGridLines().equalsIgnoreCase("Y")) {
            showGridLines = true;
        } else {
            showGridLines = false;
        }
//        if (funName != null && !(funName.equals(""))) {
//            urlgen = true;
//            this.setUrl(funName);
//        }

        if (grpbDomainaxislabel == null || grpbDomainaxislabel.equalsIgnoreCase("null")) {
            grpbDomainaxislabel = "";
        }

        if (grplyaxislabel == null || grplyaxislabel.equalsIgnoreCase("null")) {
            grplyaxislabel = "";
        }
        grpbDomainaxislabel = getGrpbDomainaxislabel();
        grplyaxislabel = getGrplyaxislabel();
        chart = ChartFactory.createBubbleChart(
                "",
                grpbDomainaxislabel,
                grplyaxislabel,
                dataset1,
                PlotOrientation.VERTICAL,
                true,
                showLegend,
                urlgen);

        //chart.setBackgroundPaint(Color.white);
        backgroundColor = graphProperty.getRgbColorArr();
        //
        if (backgroundColor != null && backgroundColor.length > 0) {
            chart.setBackgroundPaint(new Color(Integer.parseInt(backgroundColor[0].trim()), Integer.parseInt(backgroundColor[1].trim()), Integer.parseInt(backgroundColor[2].trim())));
        } else {
            chart.setBackgroundPaint(Color.white);
        }
        LegendTitle legend = chart.getLegend();
        if (showLegend) {
            if (legend != null) {
                legend.setItemFont(font);
                if (grplegendloc.equalsIgnoreCase("Bottom")) {
                    legend.setPosition(RectangleEdge.BOTTOM);
                } else if (grplegendloc.equalsIgnoreCase("Top")) {
                    legend.setPosition(RectangleEdge.TOP);
                } else if (grplegendloc.equalsIgnoreCase("Left")) {
                    legend.setPosition(RectangleEdge.LEFT);
                } else if (grplegendloc.equalsIgnoreCase("Right")) {
                    legend.setPosition(RectangleEdge.RIGHT);
                } else {
                    legend.setPosition(RectangleEdge.BOTTOM);
                }
            }
        }

        final XYPlot plot = chart.getXYPlot();
        plot.setForegroundAlpha(1f);
        plot.setBackgroundAlpha(0f);
        plot.setRangeGridlinesVisible(showGridLines);
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.setDomainGridlinesVisible(showGridLines);
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.mapDatasetToRangeAxis(1, 1);

        ValueAxis axis1 = plot.getDomainAxis();
        axis1.setTickLabelFont(font);
        axis1.setTickLabelPaint(Color.BLACK);
        axis1.setAutoRange(true);
        axis1.setStandardTickUnits(NumberAxis.createStandardTickUnits());
        axis1.setLabelFont(font);


        ValueAxis axis2 = plot.getRangeAxis();
        axis2.setTickLabelFont(font);
        axis2.setTickLabelPaint(Color.BLACK);
        axis2.setAutoRange(true);
        axis2.setStandardTickUnits(NumberAxis.createStandardTickUnits());
        axis2.setLabelFont(font);

        XYBubbleRenderer bubbleRenderer = (XYBubbleRenderer) plot.getRenderer();
        XYToolTipGenerator Std = new StandardXYToolTipGenerator();
        XYItemLabelGenerator url1 = null;
        for (int i = 0; i < colors.length; i++) {
            bubbleRenderer.setSeriesPaint(i, colors[i]);
        }
        plot.setRenderer(bubbleRenderer);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        if (urlgen) {
            //url1 = (XYItemLabelGenerator) new ProgenCategoryURLGenerator("javascript:parent.submiturls1(", ")", this.passUrl);
            //bubbleRenderer.setBaseItemLabelGenerator(url1);
        }
        //bubbleRenderer.setToolTipGenerator(Std);

        chart.setBorderVisible(false);
        String path = getchartPath(session, response, out);
        setChartDisplay(this.mapname, path);
        return path;
    }

    public String GetScatterChart() throws SQLException, IOException {
        //final XYDataset dataset = graph.getScatterDataset(getAlist());
        final XYDataset dataset = graph.getScatterDataset(getRetObj());



        if (getGrplegend().equalsIgnoreCase("Y")) {
            showLegend = true;
        } else {
            showLegend = false;
        }
        if (getGraphGridLines().equalsIgnoreCase("Y")) {
            showGridLines = true;
        } else {
            showGridLines = false;
        }
//        if (funName != null && !(funName.equals(""))) {
//            urlgen = true;
//            this.setUrl(funName);
//        }
        chart = ChartFactory.createScatterPlot(
                "",
                "",
                "",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                showLegend,
                urlgen);
        //chart.setBackgroundPaint(Color.white);
        backgroundColor = graphProperty.getRgbColorArr();
        //
        if (backgroundColor != null && backgroundColor.length > 0) {
            chart.setBackgroundPaint(new Color(Integer.parseInt(backgroundColor[0].trim()), Integer.parseInt(backgroundColor[1].trim()), Integer.parseInt(backgroundColor[2].trim())));
        } else {
            chart.setBackgroundPaint(Color.white);
        }
        LegendTitle legend = chart.getLegend();
        if (showLegend) {
            if (legend != null) {
                legend.setItemFont(font);
                if (grplegendloc.equalsIgnoreCase("Bottom")) {
                    legend.setPosition(RectangleEdge.BOTTOM);
                } else if (grplegendloc.equalsIgnoreCase("Top")) {
                    legend.setPosition(RectangleEdge.TOP);
                } else if (grplegendloc.equalsIgnoreCase("Left")) {
                    legend.setPosition(RectangleEdge.LEFT);
                } else if (grplegendloc.equalsIgnoreCase("Right")) {
                    legend.setPosition(RectangleEdge.RIGHT);
                } else {
                    legend.setPosition(RectangleEdge.BOTTOM);
                }
            }
        }

        final XYPlot plot = chart.getXYPlot();
        plot.setForegroundAlpha(1f);
        plot.setBackgroundAlpha(0f);
        plot.setRangeGridlinesVisible(showGridLines);
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.setDomainGridlinesVisible(showGridLines);
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.mapDatasetToRangeAxis(1, 1);

        ValueAxis domainAxis = plot.getDomainAxis();
        domainAxis.setTickLabelFont(font);
        domainAxis.setTickLabelPaint(Color.BLACK);


        NumberAxis axis1 = (NumberAxis) plot.getRangeAxis();
        axis1.setInverted(false);
        axis1.setTickLabelFont(new Font("Verdana", 0, 9));
        axis1.setTickLabelPaint(Color.BLACK);
        axis1.setAutoRange(true);
        axis1.setStandardTickUnits(NumberAxis.createStandardTickUnits());

        if (graph.getPrefixSymbol() == null || graph.getPrefixSymbol().equalsIgnoreCase("null")) {
            graph.setPrefixSymbol("");
        }
        if (graph.getSufffixSymbol() == null || graph.getSufffixSymbol().equalsIgnoreCase("null")) {
            graph.setSuffixSymbol("");
        }

        DecimalFormat formatter1 = null;
        formatter1 = new DecimalFormat(graph.getPrefixSymbol() + formatMode + graph.getSufffixSymbol());
        formatter1.setMaximumFractionDigits(maxFractionDigits);
        formatter1.setMinimumFractionDigits(minFractionDigits);
        axis1.setNumberFormatOverride(formatter1);//to display numbers in terms of K


        XYToolTipGenerator Std = new StandardXYToolTipGenerator();
        XYItemLabelGenerator url1 = null;

        XYLineAndShapeRenderer xyItemRenderer = (XYLineAndShapeRenderer) plot.getRenderer();
        for (int j = 0; j < getSeriesCount(); j++) {
            xyItemRenderer.setSeriesItemLabelsVisible(j, false); //true to display symbols
            if (j < colors.length) {
                xyItemRenderer.setSeriesPaint(j, colors[j]);
            }
        }
        if (urlgen) {
            //url1 = new ProgenXYURLGenerator();
            //xyItemRenderer.setBaseItemLabelGenerator(url1);
        }
        xyItemRenderer.setBaseToolTipGenerator(Std);
        chart.setBorderVisible(false);

        String path = getchartPath(session, response, out);
        setChartDisplay(this.mapname, path);
        return path;
    }

    public String GetTimeSeriesChart() {

        ////.println("entered in GetTimeSeriesChart() ");
        //XYDataset dataset = graph.getTimeSeriesdataSet(alist);
        XYDataset dataset = graph.getTimeSeriesdataSet(getRetObj());
        setTimeLevel(graph.getTimeLevel());
        setSeriesCount(graph.getBarChartColumnNames().length - graph.getViewByColumns().length);

        ////.println("graph.getTimeLevel()="+graph.getTimeLevel());


        String path = "";
        if (getGrplegend().equalsIgnoreCase("Y")) {
            showLegend = true;
        } else {
            showLegend = false;
        }
        if (getGraphGridLines().equalsIgnoreCase("Y")) {
            showGridLines = true;
        } else {
            showGridLines = false;
        }
        try {
            /*
             * if (funName != null && !(funName.equals(""))) { urlgen = true;
             * this.setUrl(funName); }
             */
            if (grpbDomainaxislabel == null || grpbDomainaxislabel.equalsIgnoreCase("null")) {
                grpbDomainaxislabel = "";
            }

            if (grplyaxislabel == null || grplyaxislabel.equalsIgnoreCase("null")) {
                grplyaxislabel = "";
            }
            grpbDomainaxislabel = getGrpbDomainaxislabel();
            grplyaxislabel = getGrplyaxislabel();
            chart = ChartFactory.createTimeSeriesChart(
                    " ", // title
                    grpbDomainaxislabel, // x-axis label
                    grplyaxislabel, // y-axis label
                    dataset, // data
                    showLegend, // create legend?
                    showToolTips, // generate tooltips?
                    urlgen // generate URLs?
                    );
            //chart.setBackgroundPaint(Color.white);
            backgroundColor = graphProperty.getRgbColorArr();
            //
            if (backgroundColor != null && backgroundColor.length > 0) {
                chart.setBackgroundPaint(new Color(Integer.parseInt(backgroundColor[0].trim()), Integer.parseInt(backgroundColor[1].trim()), Integer.parseInt(backgroundColor[2].trim())));
            } else {
                chart.setBackgroundPaint(Color.white);
            }
            if (showLegend) {
                LegendTitle legend = chart.getLegend();
                if (legend != null) {
                    legend.setItemFont(font);
                    if (grplegendloc.equalsIgnoreCase("Bottom")) {
                        legend.setPosition(RectangleEdge.BOTTOM);
                    } else if (grplegendloc.equalsIgnoreCase("Top")) {
                        legend.setPosition(RectangleEdge.TOP);
                    } else if (grplegendloc.equalsIgnoreCase("Left")) {
                        legend.setPosition(RectangleEdge.LEFT);
                    } else if (grplegendloc.equalsIgnoreCase("Right")) {
                        legend.setPosition(RectangleEdge.RIGHT);
                    } else {
                        legend.setPosition(RectangleEdge.BOTTOM);
                    }
                }
            }
            XYPlot plot = (XYPlot) chart.getPlot();
            plot.setBackgroundPaint(Color.white);//use setter and getter to change bk color later
            plot.setDomainCrosshairVisible(true);
            plot.setRangeCrosshairVisible(true);
            plot.setForegroundAlpha(1f);
            plot.setBackgroundAlpha(0f);
            plot.setRangeGridlinesVisible(showGridLines);
            plot.setDomainGridlinesVisible(showGridLines);
            plot.setDomainGridlinePaint(Color.GRAY);
            //XYItemRenderer xyItemRenderer;
            XYLineAndShapeRenderer xyItemRenderer = (XYLineAndShapeRenderer) plot.getRenderer();
            xyItemRenderer.setBaseShapesVisible(true);
            xyItemRenderer.setBaseShapesFilled(true);


            XYToolTipGenerator Std = new StandardXYToolTipGenerator();
            XYItemLabelGenerator url1 = null;

            for (int j = 0; j < getSeriesCount(); j++) {
                xyItemRenderer.setSeriesItemLabelsVisible(j, true); //true to display symbols
                if (j < colors.length) {
                    xyItemRenderer.setSeriesPaint(j, colors[j]);
                }
            }
            if (urlgen) {
                //url1 = new ProgenXYURLGenerator();
                //xyItemRenderer.setBaseItemLabelGenerator(url1);
            }
            xyItemRenderer.setBaseToolTipGenerator(Std);
            //renderer.setDefaultShapesFilled(true);
            DateAxis timeAxis = (DateAxis) plot.getDomainAxis();

            timeAxis.setTickLabelFont(font);
            timeAxis.setTickLabelPaint(Color.BLACK);
            timeAxis.setAutoRange(true);
            timeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
            timeAxis.setLabelFont(font);


            ValueAxis axis2 = plot.getRangeAxis();
            axis2.setTickLabelFont(font);
            axis2.setTickLabelPaint(Color.BLACK);
            axis2.setAutoRange(true);
            axis2.setStandardTickUnits(NumberAxis.createStandardTickUnits());
            axis2.setLabelFont(font);


            /*
             * if (ColumnKeys != null) { for (int i = 0; i < ColumnKeys.size();
             * i++) {
             * domainAxis.addCategoryLabelToolTip(dataset1.getColumnKey(i),
             * ColumnKeys.get(i).toString()); } }
             *
             * for(int i=0;i<dataset.getSeriesCount();i++){
             *
             * }
             */
            //dataset.getSeriesCount();


            if (getTimeLevel() != null) {
                if (getTimeLevel().equalsIgnoreCase("YEAR")) {
                    timeAxis.setDateFormatOverride(new SimpleDateFormat("yyyy"));
                    timeAxis.setTickUnit(new DateTickUnit(DateTickUnit.YEAR, 1));
                } else if (getTimeLevel().equalsIgnoreCase("QUARTER")) {
                    timeAxis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));
                    timeAxis.setTickUnit(new DateTickUnit(DateTickUnit.MONTH, 3));
                } else if (getTimeLevel().equalsIgnoreCase("MONTH")) {
                    timeAxis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));
                    timeAxis.setTickUnit(new DateTickUnit(DateTickUnit.MONTH, 1));
                } else if (getTimeLevel().equalsIgnoreCase("WEEK")) {
                    timeAxis.setDateFormatOverride(new SimpleDateFormat("DD-MM-yyyy"));
                    timeAxis.setTickUnit(new DateTickUnit(DateTickUnit.DAY, 7));
                } else if (getTimeLevel().equalsIgnoreCase("DAY")) {
                    timeAxis.setDateFormatOverride(new SimpleDateFormat("hh:mm a"));
                    timeAxis.setTickUnit(new DateTickUnit(DateTickUnit.DAY, 1));
                } else if (getTimeLevel().equalsIgnoreCase("HOUR")) {
                    timeAxis.setDateFormatOverride(new SimpleDateFormat("mm:ss a"));
                    timeAxis.setTickUnit(new DateTickUnit(DateTickUnit.HOUR, 1));
                } else if (getTimeLevel().equalsIgnoreCase("MINUTE")) {
                    timeAxis.setDateFormatOverride(new SimpleDateFormat("mm:ss"));
                    timeAxis.setTickUnit(new DateTickUnit(DateTickUnit.MINUTE, 1));
                } else if (getTimeLevel().equalsIgnoreCase("SECONDS")) {
                    timeAxis.setDateFormatOverride(new SimpleDateFormat("mm:ss"));
                    timeAxis.setTickUnit(new DateTickUnit(DateTickUnit.SECOND, 1));
                } else if (getTimeLevel().equalsIgnoreCase("MILLISECONDS")) {
                    timeAxis.setDateFormatOverride(new SimpleDateFormat("mm:ss"));
                    timeAxis.setTickUnit(new DateTickUnit(DateTickUnit.MILLISECOND, 1));
                }

            }
            timeAxis.setVerticalTickLabels(false);
            timeAxis.setTickLabelsVisible(true);
            plot.setDomainAxis(timeAxis);

            chart.setBorderVisible(false);

            path = getchartPath(session, response, out);
            setChartDisplay(this.mapname, path);
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
        return path;
    }

    //added by k
    public String GetKPITimeSeriesChart() {

        XYDataset dataset = graph.getTimeSeriesdataSet(getRetObj());
        setTimeLevel(graph.getTimeLevel());
        setSeriesCount(graph.getBarChartColumnNames().length - graph.getViewByColumns().length);
        String path = "";
        try {
            chart = ChartFactory.createTimeSeriesChart(
                    "", // title
                    "", // x-axis label
                    "", // y-axis label
                    dataset, // data
                    false, // create legend?
                    false, // generate tooltips?
                    false // generate URLs?
                    );
            chart.setBackgroundPaint(Color.white);
            chart.setBorderVisible(false);
            chart.getXYPlot().setOutlineVisible(false);
            chart.getXYPlot().getDomainAxis().setTickLabelsVisible(false);
            chart.getXYPlot().getRangeAxis().setTickLabelsVisible(false);
            chart.getXYPlot().getDomainAxis().setAxisLineVisible(false);
            chart.getXYPlot().getRangeAxis().setAxisLineVisible(false);
            chart.getXYPlot().getDomainAxis().setTickMarksVisible(false);
            chart.getXYPlot().getRangeAxis().setTickMarksVisible(false);

            XYPlot plot = (XYPlot) chart.getPlot();
            plot.setForegroundAlpha(1f);
            plot.setRangeGridlinesVisible(false);
            plot.setDomainCrosshairVisible(false);
            plot.setDomainMinorGridlinesVisible(false);
            plot.setDomainZeroBaselineVisible(false);
            plot.setOutlineVisible(false);
            plot.setRangeCrosshairVisible(false);
            plot.setRangeZeroBaselineVisible(false);
            plot.setBackgroundAlpha(0f);
            plot.setRangeGridlinesVisible(false);
            plot.setRangeGridlinePaint(Color.WHITE);
            plot.setDomainGridlinesVisible(true);
            plot.setDomainGridlinePaint(Color.WHITE);
            plot.mapDatasetToRangeAxis(1, 1);
            plot.getRenderer().setSeriesStroke(0,
                    new BasicStroke(
                    1.8f, BasicStroke.CAP_SQUARE, BasicStroke.CAP_SQUARE,
                    0.5f, new float[]{10.0f, 2.0f}, 0.0f));

            XYLineAndShapeRenderer xyItemRenderer = (XYLineAndShapeRenderer) plot.getRenderer();
            xyItemRenderer.setSeriesPaint(0, new Color(100, 178, 255));
            xyItemRenderer.setBaseShapesVisible(true);
            xyItemRenderer.setBaseShapesFilled(true);



            path = getchartPath(session, response, out);
            setChartDisplay(this.mapname, path);
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
        return path;
    }

    public String GetKPITimeSeriesChartZoom(String[] viewbycolumnsarr, String[] barchartcolumntitlesarr, String[] keyvaluesarr, String[] datavaluesarr) {

        //XYDataset dataset = graph.getTimeSeriesdataSet(alist);
        XYDataset dataset = graph.getTimeSeriesKPIZoom(viewbycolumnsarr, barchartcolumntitlesarr, keyvaluesarr, datavaluesarr);
        setTimeLevel("Day");
        setSeriesCount(1);


        String path = "";
        if (getGrplegend().equalsIgnoreCase("Y")) {
            showLegend = true;
        } else {
            showLegend = false;
        }
        if (getGraphGridLines().equalsIgnoreCase("Y")) {
            showGridLines = true;
        } else {
            showGridLines = false;
        }
        try {

            chart = ChartFactory.createTimeSeriesChart(
                    " ", // title
                    "", // x-axis label
                    getGrplyaxislabel(), // y-axis label
                    dataset, // data
                    showLegend, // create legend?
                    showToolTips, // generate tooltips?
                    urlgen // generate URLs?
                    );
            chart.setBackgroundPaint(Color.white);
            if (showLegend) {
                LegendTitle legend = chart.getLegend();
                if (legend != null) {
                    legend.setItemFont(font);
                    if (grplegendloc.equalsIgnoreCase("Bottom")) {
                        legend.setPosition(RectangleEdge.BOTTOM);
                    } else if (grplegendloc.equalsIgnoreCase("Top")) {
                        legend.setPosition(RectangleEdge.TOP);
                    } else if (grplegendloc.equalsIgnoreCase("Left")) {
                        legend.setPosition(RectangleEdge.LEFT);
                    } else if (grplegendloc.equalsIgnoreCase("Right")) {
                        legend.setPosition(RectangleEdge.RIGHT);
                    } else {
                        legend.setPosition(RectangleEdge.BOTTOM);
                    }
                }
            }
            XYPlot plot = (XYPlot) chart.getPlot();
            plot.setBackgroundPaint(Color.white);//use setter and getter to change bk color later
            plot.setDomainCrosshairVisible(true);
            plot.setRangeCrosshairVisible(true);
            plot.setForegroundAlpha(1f);
            plot.setBackgroundAlpha(0f);
            plot.setRangeGridlinesVisible(showGridLines);
            plot.setDomainGridlinesVisible(showGridLines);
            plot.setDomainGridlinePaint(Color.GRAY);

            XYLineAndShapeRenderer xyItemRenderer = (XYLineAndShapeRenderer) plot.getRenderer();


            XYToolTipGenerator Std = new StandardXYToolTipGenerator();
            XYItemLabelGenerator url1 = null;

            for (int j = 0; j < getSeriesCount(); j++) {
                xyItemRenderer.setSeriesItemLabelsVisible(j, false); //true to display symbols
                if (j < colors.length) {
                    xyItemRenderer.setSeriesPaint(j, colors[j]);
                }
            }
            if (urlgen) {
                //url1 = new ProgenXYURLGenerator();
                //xyItemRenderer.setBaseItemLabelGenerator(url1);
            }
            xyItemRenderer.setBaseToolTipGenerator(Std);
            //renderer.setDefaultShapesFilled(true);
            DateAxis timeAxis = (DateAxis) plot.getDomainAxis();

            timeAxis.setTickLabelFont(font);
            timeAxis.setTickLabelPaint(Color.BLACK);
            timeAxis.setAutoRange(true);
            timeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
            timeAxis.setLabelFont(font);


            ValueAxis axis2 = plot.getRangeAxis();
            axis2.setTickLabelFont(font);
            axis2.setTickLabelPaint(Color.BLACK);
            axis2.setAutoRange(true);
            axis2.setStandardTickUnits(NumberAxis.createStandardTickUnits());
            axis2.setLabelFont(font);


            if (getTimeLevel() != null) {
                if (getTimeLevel().equalsIgnoreCase("YEAR")) {
                    timeAxis.setDateFormatOverride(new SimpleDateFormat("yyyy"));
                    timeAxis.setTickUnit(new DateTickUnit(DateTickUnit.YEAR, 1));
                } else if (getTimeLevel().equalsIgnoreCase("QUARTER")) {
                    timeAxis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));
                    timeAxis.setTickUnit(new DateTickUnit(DateTickUnit.MONTH, 3));
                } else if (getTimeLevel().equalsIgnoreCase("MONTH")) {
                    timeAxis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));
                    timeAxis.setTickUnit(new DateTickUnit(DateTickUnit.MONTH, 1));
                } else if (getTimeLevel().equalsIgnoreCase("WEEK")) {
                    timeAxis.setDateFormatOverride(new SimpleDateFormat("DD-MM-yyyy"));
                    timeAxis.setTickUnit(new DateTickUnit(DateTickUnit.DAY, 7));
                } else if (getTimeLevel().equalsIgnoreCase("DAY")) {
                    timeAxis.setDateFormatOverride(new SimpleDateFormat("hh:mm a"));
                    timeAxis.setTickUnit(new DateTickUnit(DateTickUnit.DAY, 1));
                }/*
                 * else if (getTimeLevel().equalsIgnoreCase("HOUR")) {
                 * timeAxis.setDateFormatOverride(new SimpleDateFormat("mm:ss
                 * a")); timeAxis.setTickUnit(new
                 * DateTickUnit(DateTickUnit.HOUR, 1)); } else if
                 * (getTimeLevel().equalsIgnoreCase("MINUTE")) {
                 * timeAxis.setDateFormatOverride(new
                 * SimpleDateFormat("mm:ss")); timeAxis.setTickUnit(new
                 * DateTickUnit(DateTickUnit.MINUTE, 1)); } else if
                 * (getTimeLevel().equalsIgnoreCase("SECONDS")) {
                 * timeAxis.setDateFormatOverride(new
                 * SimpleDateFormat("mm:ss")); timeAxis.setTickUnit(new
                 * DateTickUnit(DateTickUnit.SECOND, 1)); } else if
                 * (getTimeLevel().equalsIgnoreCase("MILLISECONDS")) {
                 * timeAxis.setDateFormatOverride(new
                 * SimpleDateFormat("mm:ss")); timeAxis.setTickUnit(new
                 * DateTickUnit(DateTickUnit.MILLISECOND, 1)); }
                 */
            }
            timeAxis.setVerticalTickLabels(false);
            timeAxis.setTickLabelsVisible(true);
            plot.setDomainAxis(timeAxis);

            chart.setBorderVisible(false);

            path = getchartPath(session, response, out);
            setChartDisplay(this.mapname, path);
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
        return path;
    }

    public String getTimeLevel() {
        return timeLevel;
    }

    public void setTimeLevel(String timeLevel) {
        this.timeLevel = timeLevel;
    }

    public int getSeriesCount() {
        return seriesCount;
    }

    public void setSeriesCount(int seriesCount) {
        this.seriesCount = seriesCount;
    }

    public String getGraphGridLines() {

        return graphGridLines;
    }

    public void setGraphGridLines(String graphGridLines) {
        this.graphGridLines = graphGridLines;
    }
//GetParetoChart

    public String GetParetoChart() throws SQLException, IOException {
        final DefaultKeyedValues data = getGraph().getDefaultKeyedValues(getRetObj());
        data.sortByValues(SortOrder.DESCENDING);
        final CategoryDataset dataset = DatasetUtilities.createCategoryDataset(getGraph().getGrplyaxislabel(), data);
        final KeyedValues cumulative = DataUtilities.getCumulativePercentages(data);
        final CategoryDataset dataset2 = DatasetUtilities.createCategoryDataset("Cumulative", cumulative);
        //boolean urlgen = false;
        if (getFunName() != null && !(getFunName().equals(""))) {
            urlgen = true;
            this.setUrl(getFunName());
        }
        if (getGrplegend().equalsIgnoreCase("Y")) {
            showLegend = true;
        } else {
            showLegend = false;
        }
        if (getGraphGridLines().equalsIgnoreCase("Y")) {
            showGridLines = true;
        } else {
            showGridLines = false;
        }
        if (grpbDomainaxislabel == null || grpbDomainaxislabel.equalsIgnoreCase("null")) {
            grpbDomainaxislabel = "";
        }

        if (grplyaxislabel == null || grplyaxislabel.equalsIgnoreCase("null")) {
            grplyaxislabel = "";
        }
        if (grpryaxislabel == null || grpryaxislabel.equalsIgnoreCase("null")) {
            grpryaxislabel = "";
        }
        grpbDomainaxislabel = getGrpbDomainaxislabel();
        grplyaxislabel = getGrplyaxislabel();
        chart = ChartFactory.createBarChart(
                "", // chart title
                grpbDomainaxislabel, // domain axis label
                grplyaxislabel, // range axis label
                dataset, // data
                PlotOrientation.VERTICAL,
                showLegend, // include legend
                showToolTips, // tooltips?
                urlgen // URL generator?  Not required...
                );

        chart.setBackgroundPaint(Color.white);
        renderer = new BarRenderer();
        //added by k
        BarRenderer rend = new BarRenderer();
        rend.setBarPainter(new StandardBarPainter());
        renderer = (BarRenderer) rend;

        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(0xEE, 0xEE, 0xFF));
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
        LegendTitle legend = chart.getLegend();
        if (showLegend) {
            if (legend != null) {
                legend.setItemFont(font);
                if (getGrplegendloc().equalsIgnoreCase("Bottom")) {
                    legend.setPosition(RectangleEdge.BOTTOM);
                } else if (getGrplegendloc().equalsIgnoreCase("Top")) {
                    legend.setPosition(RectangleEdge.TOP);
                } else if (getGrplegendloc().equalsIgnoreCase("Left")) {
                    legend.setPosition(RectangleEdge.LEFT);
                } else if (getGrplegendloc().equalsIgnoreCase("Right")) {
                    legend.setPosition(RectangleEdge.RIGHT);
                } else {
                    legend.setPosition(RectangleEdge.BOTTOM);
                }
            }
        }

        plot.setForegroundAlpha(1f);
        plot.setBackgroundAlpha(0f);
        plot.setRangeGridlinesVisible(showGridLines);
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.setDomainGridlinesVisible(showGridLines);
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setDataset(1, dataset2);
        plot.mapDatasetToRangeAxis(1, 1);

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        domainAxis.setTickLabelFont(font);
        domainAxis.setTickLabelPaint(Color.BLACK);
        domainAxis.setLabelFont(font);

        java.util.List ColumnKeys = dataset.getColumnKeys();
        if (ColumnKeys != null) {
            for (int i = 0; i < ColumnKeys.size(); i++) {
                domainAxis.addCategoryLabelToolTip(dataset.getColumnKey(i), ColumnKeys.get(i).toString());
            }
        }

        DecimalFormat formatter1 = null;
        DecimalFormat formatter2 = null;

        NumberAxis axis1 = (NumberAxis) plot.getRangeAxis();
        axis1.setInverted(false);
        axis1.setTickLabelFont(font);
        axis1.setTickLabelPaint(Color.BLACK);
        axis1.setAutoRange(true);
        if (graphProperty.getCalibration() != null && graphProperty.getCalibration() != "") {
            if (graphProperty.getCalibration().equalsIgnoreCase("Integer")) {
                axis1.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); // display integer values in y axis label
            } else {
                maxFractionDigits = 5;
                minFractionDigits = 2;
                axis1.setStandardTickUnits(NumberAxis.createStandardTickUnits());
            }
        } else {
            axis1.setStandardTickUnits(NumberAxis.createStandardTickUnits());
        }
        //axis1.setStandardTickUnits(NumberAxis.createStandardTickUnits());
        axis1.setLabelFont(font);
        if (getGraph1().getPrefixSymbol() == null || getGraph1().getPrefixSymbol().equalsIgnoreCase("null")) {
            getGraph1().setPrefixSymbol("");
        }
        if (getGraph1().getSufffixSymbol() == null || getGraph1().getSufffixSymbol().equalsIgnoreCase("null")) {
            getGraph1().setSuffixSymbol("");
        }
        formatter1 = new DecimalFormat(getGraph1().getPrefixSymbol() + formatMode + getGraph1().getSufffixSymbol());
        formatter1.setMaximumFractionDigits(maxFractionDigits);
        formatter1.setMinimumFractionDigits(minFractionDigits);
        axis1.setNumberFormatOverride(formatter1);//to display numbers in terms of K

        NumberAxis axis2 = new NumberAxis(grpryaxislabel);
        axis2.setInverted(false);
        axis2.setTickLabelFont(font);
        axis2.setTickLabelPaint(Color.BLACK);
        axis2.setAutoRange(true);
        if (graphProperty.getCalibration() != null && graphProperty.getCalibration() != "") {
            if (graphProperty.getCalibration().equalsIgnoreCase("Integer")) {
                axis2.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); // display integer values in y axis label
            } else {
                maxFractionDigits = 5;
                minFractionDigits = 2;
                axis2.setStandardTickUnits(NumberAxis.createStandardTickUnits());
            }
        } else {
            axis2.setStandardTickUnits(NumberAxis.createStandardTickUnits());
        }
        //axis2.setStandardTickUnits(NumberAxis.createStandardTickUnits());
        axis2.setLabelFont(font);

        if (getGraph2().getPrefixSymbol() == null || getGraph2().getPrefixSymbol().equalsIgnoreCase("null")) {
            getGraph2().setPrefixSymbol("");
        }
        if (getGraph2().getSufffixSymbol() == null || getGraph2().getSufffixSymbol().equalsIgnoreCase("null")) {
            getGraph2().setSuffixSymbol("");
        }

        formatter2 = new DecimalFormat(getGraph2().getPrefixSymbol() + formatMode + getGraph2().getSufffixSymbol());
        formatter2.setMaximumFractionDigits(maxFractionDigits);
        formatter2.setMinimumFractionDigits(minFractionDigits);
        axis2.setNumberFormatOverride(formatter2);//to display numbers in terms of K


        axis2.setNumberFormatOverride(NumberFormat.getPercentInstance());
        plot.setRangeAxis(1, axis2);

        renderer1 = new LineAndShapeRenderer();

        StandardCategoryToolTipGenerator Std = new StandardCategoryToolTipGenerator();
        StandardCategoryURLGenerator url1 = null;

        if (urlgen) {
            url1 = new ProgenCategoryURLGenerator("javascript:parent.submiturls1(", ")", this.passUrl);
            renderer.setItemURLGenerator(url1);//bar
            renderer1.setItemURLGenerator(url1);//line
        }
        renderer.setToolTipGenerator(Std);
        renderer1.setToolTipGenerator(Std);

        for (int i = 0; i < colors.length; i++) {
            renderer.setSeriesPaint(i, colors[i]);//r
        }
        for (int i = (colors.length - 1); i >= 0; i--) {
            renderer1.setSeriesPaint((colors.length - 1 - i), colors[i]);//r
        }
        plot.setRenderer(0, renderer, true);
        plot.setRenderer(1, renderer1, true);

        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);


        chart.setBorderVisible(false);
        String path = getchartPath(session, response, out);
        setChartDisplay(this.mapname, path);
        return path;
    }

    public String GetSpiderChart() throws SQLException, IOException {
        //final DefaultKeyedValues data = getGraph().getDefaultKeyedValues(getRetObj());

        graph.setGraphType(chartType);
        graph.setRowValues(getRowValuesList());
        final DefaultCategoryDataset dataset1 = graph.getCategoryDataset(getRetObj());

        //final DefaultPieDataset dataset = graph.getPieDataset(getRetObj());
        chart = createChart(dataset1);
        chart.setBackgroundPaint(Color.white);
        chart.setBorderVisible(false);
        String path = getchartPath(session, response, out);
        setChartDisplay(this.mapname, path);
        return path;
    }

    public ProgenDataSet getRetObj() {
        return retObj;
    }

    public void setRetObj(ProgenDataSet retObj) {
        this.retObj = retObj;
    }

    public String getTargetRange() {
        return targetRange;
    }

    public void setTargetRange(String targetRange) {
        this.targetRange = targetRange;
    }

    public String getStartValue() {
        return startValue;
    }

    public void setStartValue(String startValue) {
        this.startValue = startValue;
    }

    public String getEndValue() {
        return endValue;
    }

    public void setEndValue(String endValue) {
        this.endValue = endValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public String getShowMinMaxRange() {
        return showMinMaxRange;
    }

    public void setShowMinMaxRange(String showMinMaxRange) {
        this.showMinMaxRange = showMinMaxRange;
    }

    public String GetProGenChart(String ProGenImgPath, int width, int height) throws Exception {
        graph.setGraphType(chartType);
        ArrayList ProGenChartList = graph.ProGenChart(getRetObj(), width, height);
        String path = buildProGenChart(ProGenChartList, ProGenImgPath);
        //setProGenChartDisplay(ChartDisp, path);
        setProGenChartDisplay("_ProGen", path, width, height);
        return path;
    }

    public void setProGenChartDisplay(String map, String path, int width, int height) {
        chartDisplay = "<SCRIPT LANGUAGE=\"JavaScript\" SRC=\"overlib.js\">  </SCRIPT>";
        //chartDisplay += "<div align='center' id=\"overDiv\" style=\"position:absolute; visibility:hidden; z-index:1000;\"></div>";
        chartDisplay += "<div align='center' id=\"overDiv\" style=\"position:absolute;z-index:1000;\"></div>";
        chartDisplay += "<img align='top' usemap=\"#" + map + "\"  src=\"" + ctxPath + "/" + path + "\"  width=\"" + width + "px\" height=\"" + height + "px\"  border='0' > </img>";
        //chartDisplay += "<img align='top' usemap=\"#" + map + "\"  src=\"" + path + "\" border='0' > </img>";
    }

    public void setProGenChartDisplayZoom(String map, String path, int width, int height) {
        chartDisplay = "<SCRIPT LANGUAGE=\"JavaScript\" SRC=\"overlib.js\">  </SCRIPT>";
        //chartDisplay += "<div align='center' id=\"overDiv\" style=\"position:absolute; visibility:hidden; z-index:1000;\"></div>";
        chartDisplay += "<div align='center' id=\"overDiv\" style=\"position:absolute;z-index:1000;\"></div>";
        chartDisplay += "<img align='top' usemap=\"#" + map + "\"  src=\"" + ctxPath + "/" + path + "\" width=\"" + width + "px\" height=\"" + height + "px\"  border='0' > </img>";
        //chartDisplay += "<img align='top' usemap=\"#" + map + "\"  src=\"" + path + "\" border='0' > </img>";
    }

    public String buildProGenChart(ArrayList ProGenChartList, String ProGenImgPath) throws Exception {
        if (getGrplegend().equalsIgnoreCase("Y")) {
            showLegend = true;
        } else {
            showLegend = false;
        }
        FileOutputStream fos = null;
        File inputFile = null;
        File outputFile = null;
        File tempFolderImgFile = null;
        long lngTime = Calendar.getInstance().getTimeInMillis();
        String inputFilePath = ProGenImgPath + "jfreechart_" + lngTime + ".html";
        String outputFilePath = ProGenImgPath + "jfreechart_" + lngTime + ".png";
        String tempFolderImgPath = System.getProperty("java.io.tmpdir") + "/jfreechart_" + lngTime + ".png";

        ////////////////.println("inputFilePath is " + inputFilePath);
        ////////////////.println("outputFilePath is " + outputFilePath);


        inputFile = new File(inputFilePath);
        outputFile = new File(outputFilePath);
        tempFolderImgFile = new File(tempFolderImgPath);
        inputFile.createNewFile();
        outputFile.createNewFile();
        tempFolderImgFile.createNewFile();
        fos = new FileOutputStream(inputFile);
        if (showLegend) {
            fos.write((ProGenChartList.get(0).toString() + ProGenChartList.get(1).toString()).getBytes());
        } else {
            fos.write((ProGenChartList.get(0).toString()).getBytes());
        }
        fos.close();
        JEditorPane pane = new JEditorPane();
        pane.setContentType("text/html");

        pane.setPage("file:/" + inputFilePath);
        final JFrame frame = new JFrame();
        frame.pack();
        Thread.sleep(100);//100 working fine
        frame.add(pane);
        frame.pack();
        Dimension prefSize = pane.getPreferredSize();
        pane.setSize(prefSize);
        BufferedImage img = new BufferedImage(prefSize.width, prefSize.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) img.getGraphics();
        SwingUtilities.paintComponent(g, pane, frame, 0, 0, prefSize.width, prefSize.height);
        ImageIO.write(img, "png", outputFile);
        ImageIO.write(img, "png", tempFolderImgFile);
        inputFile.delete();
        String str = outputFilePath.substring(outputFilePath.lastIndexOf("tempFolder"), outputFilePath.length());
        return str;
    }

    public String formatStringAsNumber(String value) {

        DecimalFormat nFormat = new DecimalFormat("##,###");
        nFormat.setMaximumFractionDigits(0);
        nFormat.setMinimumFractionDigits(0);

        if (value != null && !"".equalsIgnoreCase(value)) {
            value = nFormat.format(value);
        }
        return value;
    }

    public String formatDoubleAsString(double value) {

        NumberFormat nFormat = NumberFormat.getNumberInstance();
        nFormat.setMaximumFractionDigits(maxFractionDigits);
        nFormat.setMinimumFractionDigits(minFractionDigits);
        return nFormat.format(value);
    }

//added by kalyan
    private JFreeChart createChart(CategoryDataset categorydataset) {


        ProgenSpiderChart spiderchart1 = new ProgenSpiderChart(categorydataset);
        spiderchart1.setStartAngle(90D);
        spiderchart1.setOutlineVisible(false);
        spiderchart1.setInteriorGap(0.2D);
        for (int i = 0; i < Spidercolors.length; i++) {
            spiderchart1.setSeriesPaint(i, Spidercolors[i]);//r
        }
        //spiderchart1.setInteriorGap(0.40000000000000002D);
        spiderchart1.setToolTipGenerator(new StandardCategoryToolTipGenerator());
        JFreeChart jfreechart = new JFreeChart(" ", TextTitle.DEFAULT_FONT, spiderchart1, false);
        LegendTitle legendtitle = new LegendTitle(spiderchart1);
        Font font = new Font("Verdana", Font.PLAIN, 10);
        legendtitle.setItemFont(font);

        legendtitle.setPosition(RectangleEdge.BOTTOM);
        legendtitle.setBorder(1, 1, 1, 1);
        //legendtitle.set
        jfreechart.addSubtitle(legendtitle);
        return jfreechart;
    }

    public static void main(String[] args) {
        double str = 38387.83;
        DecimalFormat formatter = new DecimalFormat("##,###");

        //Date date = new Date();
        //String strDateFormat = "WEEK-W";
        //SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

        //strDateFormat = "w";
        //sdf = new SimpleDateFormat(strDateFormat);

        formatter.setMaximumFractionDigits(1);
        formatter.setMinimumFractionDigits(1);

        ////////////.println("formatter.format(str) is "+formatter.format(str));


    }

    public String getColorchange() {
        return colorchange;
    }

    public void setColorchange(String colorchange) {
        this.colorchange = colorchange;
    }

    public String getAvgValue() {
        return avgValue;
    }

    public void setAvgValue(String avgValue) {
        this.avgValue = avgValue;
    }

    public GraphProperty getGraphProperty() {
        return graphProperty;
    }

    public void setGraphProperty(GraphProperty graphProperty) {
        this.graphProperty = graphProperty;
    }

    public DefaultCategoryDataset getStackedCategoryDataset(DefaultCategoryDataset dataSet) {


        Double[] totalList = new Double[dataSet.getColumnKeys().size()];
        for (int k = 0; k < totalList.length; k++) {
            totalList[k] = 0.0;
        }
        for (int i = 0; i < dataSet.getRowKeys().size(); i++) {
            for (int j = 0; j < dataSet.getColumnKeys().size(); j++) {
                if (dataSet.getValue(i, j) != null) {
                    totalList[j] = totalList[j] + dataSet.getValue(i, j).doubleValue();
                } else {
                    totalList[j] = totalList[j] + 0.0;
                }
            }
        }
        for (int m = 0; m < dataSet.getRowKeys().size(); m++) {
            for (int n = 0; n < dataSet.getColumnKeys().size(); n++) {
                double value = 0.0;
                if (dataSet.getValue(m, n) != null) {
                    value = dataSet.getValue(m, n).doubleValue();
                } else {
                    value = 0.0;
                }
                dataSet.setValue((value / totalList[n]) * 100, (Comparable) dataSet.getRowKeys().get(m), (Comparable) dataSet.getColumnKeys().get(n));

            }
        }


        return dataSet;
    }

    public String getStackedType() {
        return stackedType;
    }

    public void setStackedType(String stackedType) {
        this.stackedType = stackedType;
    }

    public void overWriteColorSeries(String[] colorSeries) {
        if (colorSeries != null && colorSeries.length != 0) {
            String temprgb = "";
            for (int i = 0; i < colorSeries.length; i++) {
                if (colorSeries[i] != null && !colorSeries[i].equalsIgnoreCase("null") && !colorSeries[i].equalsIgnoreCase("Default") && !colorSeries[i].contains("#")) {
                    temprgb = colorSeries[i].substring(colorSeries[i].indexOf("(") + 1, colorSeries[i].indexOf(")"));
                    String[] rgbArray = temprgb.split(",");
                    colors[i] = new Color(Integer.parseInt(rgbArray[0].trim()), Integer.parseInt(rgbArray[1].trim()), Integer.parseInt(rgbArray[2].trim()));
                }
            }
        }
    }
}
