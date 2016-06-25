/*
 * ProgenChartDatasets.java
 *
 * Created on April 4, 2009, 6:45 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.progen.charts;

import com.progen.db.ProgenDataSet;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.*;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.jdbc.JDBCCategoryDataset;
import org.jfree.data.jdbc.JDBCPieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.*;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/*
 * @author Santhosh.kumar@progenbusiness.com
 */
public class ProgenChartDatasets implements Serializable {

    private static final long serialVersionUID = 753264711987281L;

    /*
     * public static HttpSession getSession() { return session; }
     *
     * public static void setSession(HttpSession aSession) { session = aSession;
    }
     */
    private ProgenConnection pg;
    private Connection con;
    private String sql;
    // start of variables definitions  added by santhosh.kumar@progenbusiness.com
    private boolean swapColumn = true;
    private String[] barChartColumnNames;
    private String[] viewByColumns;
    private String[] pieChartColumns;
    private String[] barChartColumnTitles;
    // end of variables definitions  added by santhosh.kumar@progenbusiness.com
    //variables added by santhosh.kumar@progenbusiness.com on 24/12/2009
    private String graphType = "";
    private ArrayList rowValues = new ArrayList();
    private double minimumValue = 0.0;
    private double maximumValue = 0.0;
    private double averageValue = 0.0;
    private String suffixSymbol = "";
    private String prefixSymbol = "";
    //end of variables on 24/12/2009
    final static int noOfColumns = 100000;
    final static int noOfRows = 100000;
    private String timeLevel = null;
    private String grplyaxislabel = "";
    private String grpryaxislabel = "";
    private String scatterViewBy = null;
    //private String displayGraphRows = "10";
    private String displayGraphRows = "10";
    private PbReturnObject retObj = null;
    private String showGT = "N";
    private double overAllMinimumValue = 0.0;
    private double overAllMaximumValue = 0.0;
    private String[] COLORS = {"#64B2FF", "#B5F950", "#33E6FA", "#EFE662", "#E1755F", "#AAC749", "#861956", "#C9A0DC", "#0099CC", "#8E5252", "#FF9900"};
//    private String[] COLORS = {"#008B8B", "#FEFE33", "#9ACD32", "#0095B6", "#C2B280", "#78866B", "#D1E231", "#DAA520", "#ECEBBD", "#C9A0DC"};
    private BigDecimal multiplier = new BigDecimal("100");
    //added by santhosh on 01-03-2010 foir getting information for entire dataset
    private TreeMap columnOverAllMaximums = null;
    private TreeMap columnOverAllMinimums = null;
    private TreeMap columnAverages = null;
    private TreeMap columnGrandTotals = null;
    private double dividend = 1;
    //private static HttpSession session = null;
    //added by k
    private String startindex = "";
    private String endindex = "";
    private String graphid = "";
    private GraphProperty graphProperty = new GraphProperty();
    private ArrayList<Integer> viewSequence;
    private boolean isCrosstab;
    private ArrayList<String> sortColumns = new ArrayList<String>();

    public ProgenChartDatasets() {
        pg = null;
        con = null;

    }

    public ProgenChartDatasets(String sqlstr) {
        pg = null;
        con = null;
        sql = sqlstr;

    }

    public void setSql(String sqlstr) {
        sql = sqlstr;
    }

    public String getSql() {
        return this.sql;
    }

    public String[] getBarChartColumnTitles() {
        return barChartColumnTitles;
    }

    public void setBarChartColumnTitles(String[] barChartColumnTitles) {
        this.barChartColumnTitles = barChartColumnTitles;
    }

    public JDBCPieDataset getPieDataset() throws SQLException {

        JDBCPieDataset dataset = null;
        // Get a connection from the DB connection
        pg = ProgenConnection.getInstance();
        con = pg.getConnection();


        //Create a dataset for Graph
        dataset = new JDBCPieDataset(con);

        dataset.executeQuery(getSql());
        if (con != null) {
            con.close();
        }

        dataset.getGroup();
        return dataset;
    }

    public JDBCCategoryDataset getCategoryDataset() throws SQLException {

        JDBCCategoryDataset dataset = null;
        // Get a connection from the DB connection
        pg = ProgenConnection.getInstance();
        con = pg.getConnection();


        //Create a dataset for Graph
        dataset = new JDBCCategoryDataset(con);


        dataset.executeQuery(getSql());
        if (con != null) {
            con.close();
        }

        dataset.getRowCount();
        return dataset;
    }

    // start of getter and setter methods for variable  added by santhosh.kumar@progenbusiness.com
    public String[] getBarChartColumnNames() {
        return barChartColumnNames;
    }

    public void setBarChartColumnNames(String[] barChartColumnNames) {
        this.barChartColumnNames = barChartColumnNames;
    }

    public boolean getSwapColumn() {
        return swapColumn;
    }

    public void setSwapColumn(boolean swapColumn) {
        this.swapColumn = swapColumn;
    }

    public void setViewByColumns(String[] viewByColumns) {
        this.viewByColumns = viewByColumns;
    }

    public String[] getViewByColumns() {
        return viewByColumns;
    }

    public String[] getPieChartColumns() {
        return pieChartColumns;
    }

    public void setPieChartColumns(String[] pieChartColumns) {
        this.pieChartColumns = pieChartColumns;
    }

    public String getViewBy1(HashMap hmap, String[] viewByColumns) {
        int colCount = viewByColumns.length;
        String viewBy = "";
        Object obj = null;
        String temp = null;

        for (int i = 0; i < colCount; i++) {
            if (viewByColumns[i].contains("A_") || viewByColumns[i].equalsIgnoreCase("TIME")) {
                obj = hmap.get(viewByColumns[i]);

            } else {
                obj = hmap.get("A_" + viewByColumns[i]);

            }
            /*
             * if (obj == null) { obj = hmap.get("A_" + viewByColumns[i]); }
             */
            temp = obj.toString();
            //temp = modifyValue(obj);
            if (i == (colCount - 1)) {
                viewBy = viewBy + temp;
            } else {
                viewBy = viewBy + temp + " - ";
            }
        }
        return viewBy;
    }

    public String getViewBy(HashMap hmap, String[] viewByColumns) {
        int colCount = viewByColumns.length;
        String viewBy = "";
        Object obj = null;
        String temp = null;
        for (int i = 0; i < colCount; i++) {
            obj = hmap.get(viewByColumns[i]);
            temp = obj.toString();
            if (i == (colCount - 1)) {
                viewBy = viewBy + temp;
            } else {
                viewBy = viewBy + temp + " - ";
            }
        }
        return viewBy;
    }

    public MatrixSeriesCollection getMatrixSeriesCollection(ArrayList alist) {
        MatrixSeriesCollection dataset = new MatrixSeriesCollection();
        HashMap hashMap;
        //NormalizedMatrixSeries
        int ColumnSize = barChartColumnNames.length - viewByColumns.length;
        if (ColumnSize >= 3) {
            for (int index = 0; index < alist.size(); index++) {

                int actualRow = retObj.getViewSequence().get(index);
                hashMap = (HashMap) alist.get(actualRow);

                final NormalizedMatrixSeries series = new NormalizedMatrixSeries(getViewBy(hashMap, viewByColumns), noOfRows, noOfColumns);

                final Double d1 = new Double(hashMap.get(barChartColumnNames[viewByColumns.length + 0]).toString());
                final Double d2 = new Double(hashMap.get(barChartColumnNames[viewByColumns.length + 1]).toString());
                final Double d3 = new Double(hashMap.get(barChartColumnNames[viewByColumns.length + 2]).toString());


                series.update(d1.intValue(), d2.intValue(), d3.doubleValue());
                series.setScaleFactor(series.getItemCount());
                dataset.addSeries(series);
                /*
                 * if (getSwapColumn()) {
                 *
                 * }else{
                 *
                 * }
                 */
            }
        }
        return (dataset);
    }

    public DefaultCategoryDataset getCategoryDataset(ArrayList alist) {
        DefaultCategoryDataset dataset = null;
        if (getGraphType() != null && (getGraphType().equalsIgnoreCase("ColumnPie") || getGraphType().equalsIgnoreCase("ColumnPie3D"))) {
            dataset = buildColumnPieDataSet(alist);
        } else if (getGraphType().equalsIgnoreCase("Spider")) {
            dataset = buildSpiderDataSet(alist);
        } else {
            dataset = buildDataSet(alist);
        }
        return dataset;
    }

    public DefaultPieDataset getPieDataset(ArrayList alist) {
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        HashMap hashMap;
        String temp = null;
        int RowSize = Integer.parseInt(getDisplayGraphRows());
        if (alist.size() < RowSize) {
            RowSize = alist.size();
        }
        for (int index = 0; index < RowSize; index++) {//1 st for
            int actualRow = retObj.getViewSequence().get(index);
            hashMap = (HashMap) alist.get(actualRow);

            Object obj = hashMap.get(pieChartColumns[viewByColumns.length]);
            temp = obj.toString();
            if ("".equalsIgnoreCase(temp)) {
                temp = "0";
            }
            pieDataset.setValue(getViewBy(hashMap, viewByColumns), new Double(temp));
        }
        return pieDataset;
    }

    public String modifyValue(Object obj) {
        String temp = obj.toString();
        int questionI;
        int equalI;
        if (obj != null) {
            questionI = temp.lastIndexOf("?");
            if (questionI == -1) {
                equalI = temp.lastIndexOf("=");
                if (equalI != -1) {
                    temp = temp.substring(equalI + 2);
                    int end = temp.lastIndexOf("'");
                    temp = temp.substring(0, end);
                }
            } else if (questionI != -1) {
                equalI = temp.lastIndexOf("=");
                if (equalI != -1) {
                    temp = temp.substring(equalI + 1);
                    int end = temp.lastIndexOf("'");
                    temp = temp.substring(0, end);
                }
            }
        }
        return temp;
    }

    public boolean memberValueExists(ArrayList alist) {
        HashMap hashMap;
        boolean exists = false;
        for (int index = 0; index < alist.size(); index++) {
            int actualRow = retObj.getViewSequence().get(index);
            hashMap = (HashMap) alist.get(actualRow);

            for (int j = 0; j < getRowValues().size(); j++) {
                if (hashMap.containsValue(getRowValues().get(j).toString())) {
                    exists = true;
                }
            }
        }
        return exists;
    }

    public boolean checkViewByExists(String viewBy) {
        boolean exists = false;
        for (int j = 0; j < getRowValues().size(); j++) {
            if (viewBy.contains(getRowValues().get(j).toString())) {
                exists = true;
            }
        }
        return exists;
    }

    public DefaultCategoryDataset buildDataSet(ArrayList alist) {
        //   
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        HashMap hashMap;
        String temp = null;
        double value = 0.0;
        int RowSize = Integer.parseInt(getDisplayGraphRows());
        if (alist.size() < RowSize) {
            RowSize = alist.size();
        }
        int ColumnSize = barChartColumnNames.length - viewByColumns.length;
        /// 
        ColumnSize = ColumnSize > 10 ? 10 : ColumnSize;//to restrict no fo graph columns
        for (int index = 0; index < RowSize; index++) {
            int actualRow = retObj.getViewSequence().get(index);
            hashMap = (HashMap) alist.get(actualRow);

            Object obj = null;

            for (int i = viewByColumns.length; i < (viewByColumns.length + ColumnSize); i++) {
                obj = hashMap.get(barChartColumnNames[i]);
                temp = obj.toString();
                if ("".equalsIgnoreCase(temp)) {
                    temp = "0";
                }
                value = new Double(temp);
                if (index == 0) {
                    setMaximumValue(value);
                    setMinimumValue(value);
                } else {
                    if (value > getMaximumValue()) {
                        setMaximumValue(value);
                    }
                    if (value < getMinimumValue()) {
                        setMinimumValue(value);
                    }
                }
                if (getSwapColumn()) {
                    dataset.setValue(value, barChartColumnTitles[i], getViewBy(hashMap, viewByColumns));
                } else {
                    dataset.setValue(value, getViewBy(hashMap, viewByColumns), barChartColumnTitles[i]);
                }
            }
        }
        //setSuffixSymbol();

        if (getSufffixSymbol() != null && !getSufffixSymbol().equalsIgnoreCase("")) {
            double dividend = 1;
            if (getSufffixSymbol().equalsIgnoreCase("K") || getSufffixSymbol().contains("K")) {
                dividend = getPowerOfTen(3);
            } else if (getSufffixSymbol().equalsIgnoreCase("M") || getSufffixSymbol().contains("M")) {
                dividend = getPowerOfTen(6);
            } else if (getSufffixSymbol().equalsIgnoreCase("B") || getSufffixSymbol().contains("B")) {
                dividend = getPowerOfTen(9);
            } else if (getSufffixSymbol().equalsIgnoreCase("T") || getSufffixSymbol().contains("T")) {
                dividend = getPowerOfTen(12);
            }
            List columnKeys = dataset.getColumnKeys();
            List rowKeys = dataset.getRowKeys();
            if (dividend != 1) {
                for (int j = 0; j < dataset.getRowCount(); j++) {
                    for (int k = 0; k < dataset.getColumnCount(); k++) {
                        dataset.setValue(dataset.getValue(j, k).doubleValue() / dividend, String.valueOf(rowKeys.get(j)), String.valueOf(columnKeys.get(k)));
                    }
                }
            }
        }
        //   
        return dataset;
    }

    public DefaultCategoryDataset buildColumnPieDataSet(ArrayList alist) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        HashMap hashMap;
        String temp = null;
        double value = 0.0;

        boolean exists = memberValueExists(alist);

        int rowCount = alist.size();
        if (!exists) {
            if (alist.size() == 0) {
                rowCount = 0;
            } else {
                rowCount = 1;
            }
        }
        if (exists) {
            for (int index = 0; index < rowCount; index++) {
                int actualRow = retObj.getViewSequence().get(index);
                hashMap = (HashMap) alist.get(actualRow);

                Object obj = null;
                int ColumnSize = barChartColumnNames.length - viewByColumns.length;
                if (ColumnSize > 10) {
                    ColumnSize = 10;
                }

                for (int i = viewByColumns.length; i < (viewByColumns.length + ColumnSize); i++) {
                    obj = hashMap.get(barChartColumnNames[i]);
                    temp = obj.toString();

                    if ("".equalsIgnoreCase(temp)) {
                        temp = "0";
                    }
                    value = new Double(temp);
                    if (index == 0) {
                        setMaximumValue(value);
                        setMinimumValue(value);
                    } else {
                        if (value > getMaximumValue()) {
                            setMaximumValue(value);
                        }
                        if (value < getMinimumValue()) {
                            setMinimumValue(value);
                        }
                    }
                    if (getSwapColumn()) {
                        if (checkViewByExists(getViewBy(hashMap, viewByColumns))) {
                            dataset.setValue(value, barChartColumnTitles[i], getViewBy(hashMap, viewByColumns));
                        }
                    } else {
                        if (checkViewByExists(getViewBy(hashMap, viewByColumns))) {
                            dataset.setValue(value, getViewBy(hashMap, viewByColumns), barChartColumnTitles[i]);
                        }
                    }
                }
            }
        } else {
            for (int index = 0; index < rowCount; index++) {
                int actualRow = retObj.getViewSequence().get(index);
                hashMap = (HashMap) alist.get(actualRow);

                Object obj = null;
                int ColumnSize = barChartColumnNames.length - viewByColumns.length;
                if (ColumnSize > 10) {
                    ColumnSize = 10;
                }

                for (int i = viewByColumns.length; i < (viewByColumns.length + ColumnSize); i++) {
                    obj = hashMap.get(barChartColumnNames[i]);
                    temp = obj.toString();

                    if ("".equalsIgnoreCase(temp)) {
                        temp = "0";
                    }
                    value = new Double(temp);
                    if (index == 0) {
                        setMaximumValue(value);
                        setMinimumValue(value);
                    } else {
                        if (value > getMaximumValue()) {
                            setMaximumValue(value);
                        }
                        if (value < getMinimumValue()) {
                            setMinimumValue(value);
                        }
                    }
                    if (getSwapColumn()) {
                        dataset.setValue(value, barChartColumnTitles[i], getViewBy(hashMap, viewByColumns));
                    } else {
                        dataset.setValue(value, getViewBy(hashMap, viewByColumns), barChartColumnTitles[i]);
                    }
                }
            }
        }
        //setMaximumValue(maxValue);
        //setMinimumValue(minValue);
        //setSuffixSymbol();


        return dataset;
    }

    public DefaultCategoryDataset buildSpiderDataSet(ArrayList alist) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        HashMap hashMap;
        String temp = null;
        double value = 0.0;

        boolean exists = memberValueExists(alist);

        int rowCount = alist.size();
        if (!exists) {
            if (alist.size() == 0) {
                rowCount = 0;
            } else {
                rowCount = 1;
            }
        }
        if (exists) {
            for (int index = 0; index < rowCount; index++) {
                int actualRow = retObj.getViewSequence().get(index);
                hashMap = (HashMap) alist.get(actualRow);

                Object obj = null;
                int ColumnSize = barChartColumnNames.length - viewByColumns.length;
                if (ColumnSize > 10) {
                    ColumnSize = 10;
                }

                for (int i = viewByColumns.length; i < (viewByColumns.length + ColumnSize); i++) {
                    obj = hashMap.get(barChartColumnNames[i]);
                    temp = obj.toString();

                    if ("".equalsIgnoreCase(temp)) {
                        temp = "0";
                    }
                    value = new Double(temp);
                    if (index == 0) {
                        setMaximumValue(value);
                        setMinimumValue(value);
                    } else {
                        if (value > getMaximumValue()) {
                            setMaximumValue(value);
                        }
                        if (value < getMinimumValue()) {
                            setMinimumValue(value);
                        }
                    }
                    if (getSwapColumn()) {
                        if (checkViewByExists(getViewBy(hashMap, viewByColumns))) {
                            dataset.setValue(value, barChartColumnTitles[i], getViewBy(hashMap, viewByColumns));
                        }
                    } else {
                        if (checkViewByExists(getViewBy(hashMap, viewByColumns))) {
                            dataset.setValue(value, getViewBy(hashMap, viewByColumns), barChartColumnTitles[i]);
                        }
                    }
                }
            }
        } else {
            for (int index = 0; index < rowCount; index++) {
                int actualRow = retObj.getViewSequence().get(index);
                hashMap = (HashMap) alist.get(actualRow);

                Object obj = null;
                int ColumnSize = barChartColumnNames.length - viewByColumns.length;
                if (ColumnSize > 10) {
                    ColumnSize = 10;
                }

                for (int i = viewByColumns.length; i < (viewByColumns.length + ColumnSize); i++) {
                    obj = hashMap.get(barChartColumnNames[i]);
                    temp = obj.toString();

                    if ("".equalsIgnoreCase(temp)) {
                        temp = "0";
                    }
                    value = new Double(temp);
                    if (index == 0) {
                        setMaximumValue(value);
                        setMinimumValue(value);
                    } else {
                        if (value > getMaximumValue()) {
                            setMaximumValue(value);
                        }
                        if (value < getMinimumValue()) {
                            setMinimumValue(value);
                        }
                    }
                    if (getSwapColumn()) {
                        dataset.setValue(value, barChartColumnTitles[i], getViewBy(hashMap, viewByColumns));
                    } else {
                        dataset.setValue(value, getViewBy(hashMap, viewByColumns), barChartColumnTitles[i]);
                    }
                }
            }
        }
        //setMaximumValue(maxValue);
        //setMinimumValue(minValue);
        //setSuffixSymbol();


        return dataset;
    }

    public DefaultXYZDataset getBubbleDataset(ArrayList alist) {
        DefaultXYZDataset dataset = new DefaultXYZDataset();
        HashMap hashMap;
        int ColumnSize = barChartColumnNames.length - viewByColumns.length;
        if (ColumnSize >= 3) {
            for (int index = 0; index < alist.size(); index++) {
                int actualRow = retObj.getViewSequence().get(index);
                hashMap = (HashMap) alist.get(actualRow);



                double d1[] = new double[1];
                double d2[] = new double[1];
                double d3[] = new double[1];
                if (hashMap.get(barChartColumnNames[viewByColumns.length + 0]).toString().equalsIgnoreCase("")) {
                    d1[0] = new Double("0");
                } else {
                    d1[0] = new Double(hashMap.get(barChartColumnNames[viewByColumns.length + 0]).toString());
                }
                if (hashMap.get(barChartColumnNames[viewByColumns.length + 1]).toString().equalsIgnoreCase("")) {
                    d2[0] = new Double("0");
                } else {
                    d2[0] = new Double(hashMap.get(barChartColumnNames[viewByColumns.length + 1]).toString());
                }
                if (hashMap.get(barChartColumnNames[viewByColumns.length + 2]).toString().equalsIgnoreCase("")) {
                    d3[0] = new Double("0");
                } else {
                    d3[0] = new Double(hashMap.get(barChartColumnNames[viewByColumns.length + 2]).toString());
                }
                double ad3[][] = {d1, d2, d3};
                dataset.addSeries(getViewBy(hashMap, viewByColumns), ad3);

            }
        }
        return (dataset);
    }

    public XYDataset getTimeSeriesdataSet(ArrayList alist) {
        TimeSeries[] timeSeries = new TimeSeries[barChartColumnNames.length - viewByColumns.length];
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        boolean buildGraph = false;
        Class timeLevelClass = null;
        HashMap hashMap;
        String dataValue = null;
        String keyValue = null;
        int count = 0;

        int timeViewByIndex = 0;

        if (viewByColumns != null && barChartColumnNames != null && barChartColumnTitles != null) {
            for (int str = 0; str < viewByColumns.length; str++) {
                if (viewByColumns[str].contains("Time") || barChartColumnTitles[str].equalsIgnoreCase("Time")) {
                    buildGraph = true;
                    timeViewByIndex = str;
                }
            }
            if (buildGraph) {
                for (int index = 0; index < alist.size(); index++) {
                    int actualRow = retObj.getViewSequence().get(index);
                    hashMap = (HashMap) alist.get(actualRow);


                    ++count;//to increase the month count.
                    for (int i = viewByColumns.length; i < barChartColumnNames.length; i++) {

                        dataValue = String.valueOf(hashMap.get(barChartColumnNames[i]));
                        keyValue = String.valueOf(hashMap.get(viewByColumns[timeViewByIndex]));

                        keyValue = keyValue.replaceAll("-", "/");
                        Date date = new Date(keyValue);

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);

                        int milliSeconds = calendar.get(Calendar.MILLISECOND);
                        int seconds = calendar.get(Calendar.SECOND);
                        int minute = calendar.get(Calendar.MINUTE);
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        int week = calendar.get(Calendar.WEEK_OF_MONTH);
                        int month = calendar.get(Calendar.MONTH) + 1;
                        //quarter = (calendar.get(Calendar.MONTH) / 3) + 1;
                        int year = calendar.get(Calendar.YEAR);


                        if (dataValue == null || dataValue.equalsIgnoreCase("")) {
                            dataValue = "0";
                        }
                        timeLevelClass = Day.class;
                        if (timeSeries[i - viewByColumns.length] == null) {
                            timeSeries[i - viewByColumns.length] = new TimeSeries(barChartColumnTitles[i], timeLevelClass);
                        }

                        timeSeries[i - viewByColumns.length].add((new Day(day, month, year)), new Double(dataValue));

                        /*
                         * //if (getTimeLevel() != null) { if
                         * (getTimeLevel().equalsIgnoreCase("YEAR")) {
                         * timeLevelClass = Year.class; if (timeSeries[i -
                         * viewByColumns.length] == null) { timeSeries[i -
                         * viewByColumns.length] = new
                         * TimeSeries(barChartColumnTitles[i], timeLevelClass);
                         * } timeSeries[i - viewByColumns.length].add(new
                         * Year(year), new Double(dataValue)); //
                         * timeSeries[i].add(new
                         * Year(Integer.parseInt(keyValue)), new
                         * Double(dataValue)); } else if
                         * (getTimeLevel().equalsIgnoreCase("QUARTER")) {
                         * timeLevelClass = Quarter.class;
                         *
                         * if (count > 4) { count = 1; }
                         *
                         * if (timeSeries[i - viewByColumns.length] == null) {
                         * timeSeries[i - viewByColumns.length] = new
                         * TimeSeries(barChartColumnTitles[i], timeLevelClass);
                         * }
                         *
                         * timeSeries[i - viewByColumns.length].add(new
                         * Quarter(count, year), new Double(dataValue));
                         *
                         *
                         * //timeSeries[i].add(new Month(month, year), new
                         * Double(dataValue)); //timeSeries[i].add(new
                         * Quarter(Integer.parseInt(keyValue)), new
                         * Double(dataValue)); } else if
                         * (getTimeLevel().equalsIgnoreCase("MONTH")) {
                         * timeLevelClass = Month.class; if (timeSeries[i -
                         * viewByColumns.length] == null) { timeSeries[i -
                         * viewByColumns.length] = new
                         * TimeSeries(barChartColumnTitles[i], timeLevelClass);
                         * }
                         *
                         * timeSeries[i - viewByColumns.length].add(new
                         * Month(month, year), new Double(dataValue)); } else if
                         * (getTimeLevel().equalsIgnoreCase("WEEK")) {
                         * timeLevelClass = Week.class; if (timeSeries[i -
                         * viewByColumns.length] == null) { timeSeries[i -
                         * viewByColumns.length] = new
                         * TimeSeries(barChartColumnTitles[i], timeLevelClass);
                         * }
                         *
                         * timeSeries[i - viewByColumns.length].add(new Day(day,
                         * month, year), new Double(dataValue)); } else if
                         * (getTimeLevel().equalsIgnoreCase("DAY")) {
                         * timeLevelClass = Day.class; if (timeSeries[i -
                         * viewByColumns.length] == null) { timeSeries[i -
                         * viewByColumns.length] = new
                         * TimeSeries(barChartColumnTitles[i], timeLevelClass);
                         * }
                         *
                         * timeSeries[i - viewByColumns.length].add(new Day(day,
                         * month, year), new Double(dataValue)); } else if
                         * (getTimeLevel().equalsIgnoreCase("HOUR")) {
                         * timeLevelClass = Day.class; if (timeSeries[i -
                         * viewByColumns.length] == null) { timeSeries[i -
                         * viewByColumns.length] = new
                         * TimeSeries(barChartColumnTitles[i], timeLevelClass);
                         * }
                         *
                         * timeSeries[i - viewByColumns.length].add(new
                         * Hour(hour, day, month, year), new Double(dataValue));
                         * } else if (getTimeLevel().equalsIgnoreCase("MINUTE"))
                         * { timeLevelClass = Day.class; if (timeSeries[i -
                         * viewByColumns.length] == null) { timeSeries[i -
                         * viewByColumns.length] = new
                         * TimeSeries(barChartColumnTitles[i], timeLevelClass);
                         * }
                         *
                         * timeSeries[i - viewByColumns.length].add(new
                         * Minute(minute, hour, day, month, year), new
                         * Double(dataValue)); } else if
                         * (getTimeLevel().equalsIgnoreCase("SECONDS")) {
                         * timeLevelClass = Day.class; if (timeSeries[i -
                         * viewByColumns.length] == null) { timeSeries[i -
                         * viewByColumns.length] = new
                         * TimeSeries(barChartColumnTitles[i], timeLevelClass);
                         * }
                         *
                         * timeSeries[i - viewByColumns.length].add(new
                         * Second(seconds, minute, hour, day, month, year), new
                         * Double(dataValue)); } else if
                         * (getTimeLevel().equalsIgnoreCase("MILLISECONDS")) {
                         * timeLevelClass = Day.class; if (timeSeries[i -
                         * viewByColumns.length] == null) { timeSeries[i -
                         * viewByColumns.length] = new
                         * TimeSeries(barChartColumnTitles[i], timeLevelClass);
                         * }
                         *
                         * timeSeries[i - viewByColumns.length].add(new
                         * Millisecond(milliSeconds, seconds, minute, hour, day,
                         * month, year), new Double(dataValue)); }
                         *
                         * //}//if timelevel
                         */
                    }// int i view by coloms
                }//int index
                for (int i = 0; i < timeSeries.length; i++) {
                    dataset.addSeries(timeSeries[i]);
                }
            }
        }
        return dataset;
    }

    public XYSeriesCollection getScatterDataset(ArrayList alist) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        //TimeSeriesCollection dataset1 = new TimeSeriesCollection();
        int ColumnSize = barChartColumnNames.length - viewByColumns.length;
        if (ColumnSize > 10) {
            ColumnSize = 10;
        }
        TimeSeries[] timeSeries = new TimeSeries[ColumnSize];
        XYSeries[] seriesList = new XYSeries[ColumnSize];


        boolean buildGraph = false;
        Class timeLevelClass = null;
        HashMap hashMap = null;
        String dataValue = null;
        String keyValue = null;
        int count = 0;

        int timeViewByIndex = 0;
        for (int str = 0; str < viewByColumns.length; str++) {
            if (viewByColumns[str].contains("Time") || barChartColumnTitles[str].equalsIgnoreCase("Time")) {
                buildGraph = true;
                timeViewByIndex = str;
            }
        }
        if (buildGraph) {
            for (int index = 0; index < alist.size(); index++) {
                int actualRow = retObj.getViewSequence().get(index);
                hashMap = (HashMap) alist.get(actualRow);

                ++count;//to increase the month count.
                for (int i = viewByColumns.length; i < viewByColumns.length + ColumnSize; i++) {
                    dataValue = String.valueOf(hashMap.get(barChartColumnNames[i]));
                    keyValue = String.valueOf(hashMap.get(viewByColumns[timeViewByIndex]));
                    keyValue = keyValue.replaceAll("-", "/");
                    Date date = new Date(keyValue);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);

//                    int day = calendar.get(Calendar.DAY_OF_MONTH);
//                    int month = calendar.get(Calendar.MONTH) + 1;
//                    int year = calendar.get(Calendar.YEAR);


                    if (dataValue == null || dataValue.equalsIgnoreCase("")) {
                        dataValue = "0";
                    }
                    timeLevelClass = Day.class;
                    if (seriesList[i - viewByColumns.length] == null) {
                        seriesList[i - viewByColumns.length] = new XYSeries(barChartColumnTitles[i]);
                    }
                    seriesList[i - viewByColumns.length].add(calendar.getTimeInMillis(), new Double(dataValue));


                }// int i view by coloms
            }//int index
            for (int i = 0; i < seriesList.length; i++) {
                dataset.addSeries(seriesList[i]);
            }
            return dataset;
        } else {
            setScatterViewBy(viewByColumns[1]);//need to be commented later
            boolean scatterExists = false;
            setScatterViewBy(viewByColumns[1]);
            //check whether scatter view by is exists in the current viewbys
            if (getScatterViewBy() != null) {
                for (int i = 0; i < viewByColumns.length; i++) {
                    if (getScatterViewBy().equalsIgnoreCase(viewByColumns[i])) {
                        scatterExists = true;
                    }
                }
            }
            if (scatterExists) {
                for (int index = 0; index < alist.size(); index++) {
                    int actualRow = retObj.getViewSequence().get(index);
                    hashMap = (HashMap) alist.get(actualRow);

                    for (int i = viewByColumns.length; i < (viewByColumns.length + ColumnSize); i++) {
                        if (seriesList[i - viewByColumns.length] == null) {
                            seriesList[i - viewByColumns.length] = new XYSeries(barChartColumnTitles[i]);
                        }
                        double x = Double.parseDouble(hashMap.get(getScatterViewBy()).toString());
                        double y = Double.parseDouble(hashMap.get(barChartColumnNames[i]).toString());
                        if (x != 0.0 || y != 0.0) {
                            seriesList[i - viewByColumns.length].add(x, y);
                        }
                    }
                }
                for (XYSeries series : seriesList) {
                    dataset.addSeries(series);
                }
            }
            return dataset;
        }
    }

    public DefaultKeyedValues getDefaultKeyedValues(ArrayList alist) {
        DefaultKeyedValues data = new DefaultKeyedValues();
        //DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        HashMap hashMap;
        String temp = null;
        double value = 0.0;


        for (int index = 0; index < alist.size(); index++) {
            int actualRow = retObj.getViewSequence().get(index);
            hashMap = (HashMap) alist.get(actualRow);
            Object obj = null;
            int ColumnSize = barChartColumnNames.length - viewByColumns.length;
            if (ColumnSize > 10) {
                ColumnSize = 10;//to restrict no fo graph columns
            }
            //for (int i = viewByColumns.length; i < (viewByColumns.length + ColumnSize); i++) {
            //obj = hashMap.get(barChartColumnNames[i]);
            obj = hashMap.get(barChartColumnNames[viewByColumns.length]);
            temp = obj.toString();
            if ("".equalsIgnoreCase(temp)) {
                temp = "0";
            }
            value = new Double(temp);
            if (index == 0) {
                setMaximumValue(value);
                setMinimumValue(value);
            } else {
                if (value > getMaximumValue()) {
                    setMaximumValue(value);
                }
                if (value < getMinimumValue()) {
                    setMinimumValue(value);
                }
            }
            if (getSwapColumn()) {
                data.addValue(getViewBy(hashMap, viewByColumns), value);
                //dataset.setValue(value, barChartColumnTitles[i], getViewBy(hashMap, viewByColumns));
            } else {
                data.addValue(getViewBy(hashMap, viewByColumns), value);
                //dataset.setValue(value, getViewBy(hashMap, viewByColumns), barChartColumnTitles[i]);
            }
            //}
        }


        //setSuffixSymbol();

        if (getSufffixSymbol() != null && !getSufffixSymbol().equalsIgnoreCase("")) {
            double dividend = 1;
            if (getSufffixSymbol().equalsIgnoreCase("K") || getSufffixSymbol().contains("K")) {
                dividend = getPowerOfTen(3);
            } else if (getSufffixSymbol().equalsIgnoreCase("M") || getSufffixSymbol().contains("M")) {
                dividend = getPowerOfTen(6);
            } else if (getSufffixSymbol().equalsIgnoreCase("B") || getSufffixSymbol().contains("B")) {
                dividend = getPowerOfTen(9);
            } else if (getSufffixSymbol().equalsIgnoreCase("T") || getSufffixSymbol().contains("T")) {
                dividend = getPowerOfTen(12);
            }
            List keys = data.getKeys();

            //List columnKeys = dataset.getColumnKeys();
            //List rowKeys = dataset.getRowKeys();

            if (dividend != 1) {
                for (int j = 0; j < data.getItemCount(); j++) {
                    for (int k = 0; k < keys.size(); k++) {
                        data.addValue(String.valueOf(keys.get(j)), data.getValue(k).doubleValue() / dividend);
                        //dataset.setValue(dataset.getValue(j, k).doubleValue() / dividend, String.valueOf(rowKeys.get(j)), String.valueOf(columnKeys.get(k)));
                    }
                }
            }
        }
        StringBuffer sbuffer = new StringBuffer();
        for (int k = 0; k < viewByColumns.length; k++) {
            sbuffer.append("," + barChartColumnTitles[k]);
        }

        setGrplyaxislabel(sbuffer.substring(1));
        return data;
    }

    public String getGraphType() {
        return graphType;
    }

    public void setGraphType(String graphType) {
        this.graphType = graphType;
    }

    public ArrayList getRowValues() {
        if (rowValues == null) {
            rowValues = new ArrayList();
        }
        return rowValues;
    }

    public void setRowValues(ArrayList rowValues) {
        this.rowValues = rowValues;
    }

    public void setSuffixSymbol(String suffixSymbol) {
        this.suffixSymbol = suffixSymbol;
    }

    public void setSuffixSymbol() {
        /*
         * if ((Math.abs(getMinimumValue()) / getPowerOfTen(12)) > 0.99) {
         * suffixSymbol = "#Tn"; } else if ((Math.abs(getMinimumValue()) /
         * getPowerOfTen(9)) > 0.99) { suffixSymbol = "#Bn"; }
         */
        if ((Math.abs(getMinimumValue()) / getPowerOfTen(6)) > 0.99) {
            suffixSymbol = "#Mn";
        } else if ((Math.abs(getMinimumValue()) / getPowerOfTen(3)) > 0.99) {
            suffixSymbol = "#K";
        } else {
            suffixSymbol = "";
        }


    }

    public String getSufffixSymbol() {
        return suffixSymbol;
    }

    public long getPowerOfTen(int num) {
        long bd = 1;
        for (int i = 0; i < num; i++) {
            bd = bd * 10;
        }
        return bd;
    }

    public double getMinimumValue() {
        return minimumValue;
    }

    public void setMinimumValue(double minimumValue) {
        this.minimumValue = minimumValue;
    }

    public double getMaximumValue() {
        return maximumValue;
    }

    public void setMaximumValue(double maximumValue) {
        this.maximumValue = maximumValue;
    }

    public String getScatterViewBy() {
        return scatterViewBy;
    }

    public void setScatterViewBy(String scatterViewBy) {
        this.scatterViewBy = scatterViewBy;
    }

    public String getTimeLevel() {
        return timeLevel;
    }

    public void setTimeLevel(String timeLevel) {
        this.timeLevel = timeLevel;
    }

    public String getPrefixSymbol() {
        return prefixSymbol;
    }

    public void setPrefixSymbol(String prefixSymbol) {
        this.prefixSymbol = prefixSymbol;
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

    public String getDisplayGraphRows() {

        if (displayGraphRows == null || "null".equalsIgnoreCase(displayGraphRows)) {
            displayGraphRows = "10";
        }
        return displayGraphRows;
    }

    public void setDisplayGraphRows(String displayGraphRows) {
        this.displayGraphRows = displayGraphRows;
    }

    public PbReturnObject getRetObj() {
        return retObj;
    }

    public void setRetObj(PbReturnObject retObj) {
        this.retObj = retObj;
    }

    /*
     * New methods as per Return Onject
     */
    public MatrixSeriesCollection getMatrixSeriesCollection(PbReturnObject retObj) {
        MatrixSeriesCollection dataset = new MatrixSeriesCollection();

        //NormalizedMatrixSeries
        if (retObj != null && !retObj.getViewSequence().isEmpty()) {
            int ColumnSize = barChartColumnNames.length - viewByColumns.length;
            if (ColumnSize >= 3) {
                for (int index = 0; index < retObj.getViewSequence().size(); index++) {
                    int actualRow = retObj.getViewSequence().get(index);
                    retObj.getFieldValueString(actualRow, barChartColumnNames[viewByColumns.length + 0]);
                    final NormalizedMatrixSeries series = new NormalizedMatrixSeries(getViewBy(retObj, viewByColumns, actualRow), noOfRows, noOfColumns);

                    final Double d1 = new Double(retObj.getFieldValueString(actualRow, barChartColumnNames[viewByColumns.length + 0]));
                    final Double d2 = new Double(retObj.getFieldValueString(actualRow, barChartColumnNames[viewByColumns.length + 1]));
                    final Double d3 = new Double(retObj.getFieldValueString(actualRow, barChartColumnNames[viewByColumns.length + 2]));
                    series.update(d1.intValue(), d2.intValue(), d3.doubleValue());
                    series.setScaleFactor(series.getItemCount());
                    dataset.addSeries(series);
                    /*
                     * if (getSwapColumn()) {
                     *
                     * }else{
                     *
                     * }
                     */
                }
            }
        }

        return (dataset);
    }

    public DefaultCategoryDataset getCategoryDataset(ProgenDataSet retObj) {
        DefaultCategoryDataset dataset = null;
        if (getGraphType() != null && (getGraphType().equalsIgnoreCase("ColumnPie") || getGraphType().equalsIgnoreCase("ColumnPie3D"))) {
            dataset = buildColumnPieDataSet(retObj);
        } else if (getGraphType().equalsIgnoreCase("Spider")) {
            ////////////////.println("spider else if entered");
            dataset = buildSpiderDataSet(retObj);
        } else {
            ////////////////.println("entered in else part");
            dataset = buildDataSet(retObj);
        }
        return dataset;
    }

    public DefaultPieDataset getPieDataset(ProgenDataSet retObj) {

        int sindex = 0;
        int eindex = 0;

        ArrayList<Integer> graphViewSeq = retObj.getViewSequence();
        if (this.viewSequence != null && !this.viewSequence.isEmpty()) {
            graphViewSeq = this.viewSequence;
        }

        if (getStartindex() != null && getEndindex() != null && !getStartindex().equalsIgnoreCase("null") && !getEndindex().equalsIgnoreCase("null") && !getStartindex().equalsIgnoreCase("") && !getEndindex().equalsIgnoreCase("")) {

            if (getEndindex().equalsIgnoreCase("All")) {
                if (sindex < graphViewSeq.size()) {
                    sindex = Integer.parseInt(getStartindex());
                }
                if (eindex > graphViewSeq.size()) {
                    eindex = graphViewSeq.size();
                }
            } else {
                sindex = Integer.parseInt(getStartindex());
                eindex = Integer.parseInt(getEndindex());
            }

        }

        DefaultPieDataset pieDataset = new DefaultPieDataset();
        int RowSize = 10;
        String RowSizeStatus = "";
        if (!getDisplayGraphRows().equalsIgnoreCase("All")) {
            if (getDisplayGraphRows().equalsIgnoreCase("")) {
                RowSize = 10;
            } else {
                RowSize = Integer.parseInt(getDisplayGraphRows());
            }
        } else {
            RowSizeStatus = getDisplayGraphRows();
        }


        if (retObj != null && !graphViewSeq.isEmpty()) {


            if (RowSizeStatus.equalsIgnoreCase("All")) {
                RowSize = graphViewSeq.size();
            } else {
                RowSize = (graphViewSeq.size() < RowSize) ? graphViewSeq.size() : RowSize;
            }

            // //.println("check  sindex="+sindex);
            // //.println("check  eindex="+eindex);

            if (sindex == 0 && eindex > graphViewSeq.size() && sindex > graphViewSeq.size()) {
                sindex = 0;
                eindex = graphViewSeq.size();
            }
//added for portlets
            if (sindex == 0 && eindex > graphViewSeq.size()) {
                sindex = 0;
                eindex = graphViewSeq.size();
            }

            if (sindex == 0 && eindex == 0) {
                sindex = 0;
                if (graphViewSeq.size() > 10) {
                    eindex = 10;
                } else {
                    eindex = graphViewSeq.size();
                }
            }


            for (int index = sindex; index < eindex; index++) {
                int actualRow = graphViewSeq.get(index);
                if (graphProperty.getMeasureValueRounding() != null && graphProperty.getMeasureFormat() != null) {
                    if (graphProperty.getMeasureFormat().equalsIgnoreCase("value")) {
                        NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
                        String measureValueRounding = graphProperty.getMeasureValueRounding();
                        nf.setMaximumFractionDigits(Integer.parseInt(measureValueRounding));
                        nf.setMinimumFractionDigits(Integer.parseInt(measureValueRounding));
                        String value = nf.format(new Double(retObj.getFieldValueString(actualRow, pieChartColumns[viewByColumns.length])));
                        pieDataset.setValue(getViewBy(retObj, viewByColumns, actualRow), Double.parseDouble(value.replace(",", "")));
                    } else {
                        pieDataset.setValue(getViewBy(retObj, viewByColumns, actualRow), new Double(retObj.getFieldValueString(actualRow, pieChartColumns[viewByColumns.length])));
                    }
                } else {
                    pieDataset.setValue(getViewBy(retObj, viewByColumns, actualRow), new Double(retObj.getFieldValueString(actualRow, pieChartColumns[viewByColumns.length])));
                }
                // pieDataset.setValue(getViewBy(retObj, viewByColumns, actualRow), new Double(retObj.getFieldValueString(actualRow, pieChartColumns[viewByColumns.length])));
                // 
            }




        }

        return pieDataset;
    }

    public DefaultCategoryDataset buildDataSet(ProgenDataSet retObj) {
        //added by k
        int sindex = 0;
        int eindex = 0;

        Double average = 0.0;
        Double total = 0.0;
        int count = 0;


        ArrayList<Integer> graphViewSeq = retObj.getViewSequence();
        if (this.viewSequence != null && !this.viewSequence.isEmpty()) {
            graphViewSeq = this.viewSequence;
        }

        if (getStartindex() != null && getEndindex() != null && !getStartindex().equalsIgnoreCase("null") && !getEndindex().equalsIgnoreCase("null") && !getStartindex().equalsIgnoreCase("") && !getEndindex().equalsIgnoreCase("")) {

            if (getEndindex().equalsIgnoreCase("All")) {
                sindex = Integer.parseInt(getStartindex());
                eindex = graphViewSeq.size();
            } else {
                ////.println("--------");
                sindex = Integer.parseInt(getStartindex());
                eindex = Integer.parseInt(getEndindex());
            }

        }

        ////.println("check  sindex="+sindex);
        ////.println("check  eindex="+eindex);

        ////.println("");

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String temp = null;
        BigDecimal[] grandTotals = null;
        ////////////////.println("RowSize=="+getDisplayGraphRows());
        int RowSize = 10;
        String RowSizeStatus = "";
        if (!getDisplayGraphRows().equalsIgnoreCase("All")) {
            if (getDisplayGraphRows().equalsIgnoreCase("")) {
                RowSize = 10;
            } else {
                RowSize = Integer.parseInt(getDisplayGraphRows());
            }

        } else {
            RowSizeStatus = getDisplayGraphRows();
        }


        int ColumnSize = barChartColumnNames.length - viewByColumns.length;
        if (retObj != null && graphViewSeq.size() != 0) {
            if (RowSizeStatus.equalsIgnoreCase("All")) {
                RowSize = graphViewSeq.size();
                ////////////////.println("in if=="+RowSize);
            } else {
                RowSize = (graphViewSeq.size() < RowSize) ? graphViewSeq.size() : RowSize;
                ////////////////.println("in if==else"+RowSize);
            }
            ColumnSize = (ColumnSize > 10) ? 10 : ColumnSize;
            if (graphType != null && graphType.equalsIgnoreCase("Waterfall")) {
                ColumnSize = 1;
            } else {
                ColumnSize = ColumnSize;
            }
            grandTotals = new BigDecimal[ColumnSize];
            Double value = 0.0;
            //code to build min max values

            for (int i = viewByColumns.length; i < (viewByColumns.length + ColumnSize); i++) {
                if (i == viewByColumns.length) {
                    buildMaxValues(barChartColumnNames[i], true);
                    buildMinValues(barChartColumnNames[i], true);
                } else {
                    buildMaxValues(barChartColumnNames[i], false);
                    buildMinValues(barChartColumnNames[i], false);
                }
            }

            ///this condition is placed on behalf of dashboard graph
            if (sindex == 0 && eindex > graphViewSeq.size()) {
                sindex = 0;
                eindex = graphViewSeq.size();
            }
            if (sindex == 0 && eindex == 0) {
                sindex = 0;
                if (graphViewSeq.size() > 10) {
                    eindex = 10;
                } else {
                    eindex = graphViewSeq.size();
                }
            }


            if (!isCrosstab && sortColumns.contains(viewByColumns[0]) && viewByColumns.length == 2 && !graphType.equalsIgnoreCase("Waterfall")) {

                ArrayList<Integer> sortList = new ArrayList<Integer>();
                sortList = retObj.getViewSequence();


                String[] currDimValue = null;
                int seqNo = -1;
                for (int index = sindex; index < eindex; index++) {

                    if (viewByColumns.length < barChartColumnNames.length) {
                        int loopCount = 0;
                        for (int n = 0; n < sortList.size(); n++) {
                            if (seqNo == sortList.size()) {
                                break;
                            }
                            if (seqNo == -1) {
                                seqNo = n;
                            }
                            int actualRow = sortList.get(seqNo);


                            String[] tempArray = new String[viewByColumns.length - 1];
                            for (int k = 0; k < viewByColumns.length - 1; k++) {
                                tempArray[k] = retObj.getFieldValueString(actualRow, viewByColumns[k]);
                            }


                            if (currDimValue == null) {
                                currDimValue = new String[tempArray.length];
                                for (int l = 0; l < tempArray.length; l++) {
                                    currDimValue[l] = tempArray[l];
                                }
                            }
                            boolean flag = compareStringArray(tempArray, currDimValue);
                            if (flag) {
                                temp = retObj.getFieldValueString(actualRow, barChartColumnNames[viewByColumns.length]) != null ? retObj.getFieldValueString(actualRow, barChartColumnNames[viewByColumns.length]) : "0";
                                value = new Double(temp);
                                String viewby = getViewBy(retObj, viewByColumns, actualRow);

                                if (getSwapColumn()) {
                                    dataset.setValue(value, viewby.substring(viewby.lastIndexOf(",") + 1, viewby.length()), viewby.substring(0, viewby.lastIndexOf(",")));

                                } else {
                                    dataset.setValue(value, viewby.substring(0, viewby.lastIndexOf(",")), viewby.substring(viewby.lastIndexOf(",") + 1, viewby.length()));
                                }
                                for (int l = 0; l < tempArray.length; l++) {
                                    currDimValue[l] = tempArray[l];
                                }
                                loopCount = loopCount + 1;
                                seqNo = seqNo + 1;
                                if (loopCount >= 10) {
                                    break;
                                }
                            } else {
                                for (int l = 0; l < tempArray.length; l++) {
                                    currDimValue[l] = tempArray[l];
                                }
                                seqNo = seqNo;
                                break;
                            }


                        }
                    }

                }

            } // //.println("sindex="+sindex);
            //  //.println("eindex="+eindex);
            //  //.println("retObj.getRowCount()"+retObj.getRowCount());
            else {
                for (int index = sindex; index < eindex; index++) {
                    int actualRow = graphViewSeq.get(index);

                    for (int i = viewByColumns.length; i < (viewByColumns.length + ColumnSize); i++) {

                        temp = retObj.getFieldValueString(actualRow, barChartColumnNames[i]) != null ? retObj.getFieldValueString(actualRow, barChartColumnNames[i]) : "0";

                        value = new Double(temp);
                        if (grandTotals[i - viewByColumns.length] == null) {
                            grandTotals[i - viewByColumns.length] = new BigDecimal("0.0");
                        }
                        grandTotals[i - viewByColumns.length] = grandTotals[i - viewByColumns.length].add(new BigDecimal(temp));
                        //for average
                        count++;
                        total = total + value;

                        if (getColumnOverAllMaximums() == null && getColumnOverAllMinimums() == null) {
                            if (actualRow == 0) {
                                setMaximumValue((double) value);
                                setMinimumValue((double) value);
                                setAverageValue((double) value);
                            } else {
                                // //.println("hereeee------");
                                if (value > getMaximumValue()) {
                                    setMaximumValue((double) value);
                                }
                                if (value < getMinimumValue()) {
                                    setMinimumValue((double) value);
                                }

                            }
                        }

                        String viewby = getViewBy(retObj, viewByColumns, actualRow);
                        // //.println("viewby="+viewby);
                  /*
                         * if(graphType.equalsIgnoreCase("column")||graphType.equalsIgnoreCase("column3d"))
                         * { if(viewby.length()>10) { viewby=viewby.substring(0,
                         * 9); viewby=viewby.concat(".."); }
                         *
                         * }
                         */


                        if (getSwapColumn()) {
                            dataset.setValue(value, barChartColumnTitles[i], viewby);
                            ////.println("getViewBy(retObj, viewByColumns, index)"+getViewBy(retObj, viewByColumns, index));

                        } else {
                            dataset.setValue(value, viewby, barChartColumnTitles[i]);
                            // //.println("getViewBy(retObj, viewByColumns, index)"+getViewBy(retObj, viewByColumns, index));
                        }
                    }
                }

                if (count != 0) {
                    average = total / count;
                    setAverageValue(average);

                }
                //build GrandTotal

                if (getShowGT() != null && getShowGT().equalsIgnoreCase("Y")) {
                    for (int i = viewByColumns.length; i < (viewByColumns.length + ColumnSize); i++) {
                        value = grandTotals[i - viewByColumns.length].doubleValue();
                        if (getSwapColumn()) {
                            // //.println("barChartColumnTitles[i]="+barChartColumnTitles[i]);
                            dataset.setValue(value, barChartColumnTitles[i], "Grand Total");
                            if (graphType != null && !(graphType.equalsIgnoreCase("Waterfall"))) {
                                dataset.setValue(average, barChartColumnTitles[i], "Average");
                            }
                        } else {
                            // //.println("barChartColumnTitles[i]="+barChartColumnTitles[i]);
                            dataset.setValue(value, "Grand Total", barChartColumnTitles[i]);
                            if (graphType != null && !(graphType.equalsIgnoreCase("Waterfall"))) {
                                dataset.setValue(average, "Average", barChartColumnTitles[i]);
                            }
                        }
                    }
                }
                //end of grand Total

                if (getSufffixSymbol() != null && !getSufffixSymbol().equalsIgnoreCase("")) {

                    if (getSufffixSymbol().equalsIgnoreCase("K") || getSufffixSymbol().contains("K")) {
                        dividend = getPowerOfTen(3);
                    } else if (getSufffixSymbol().equalsIgnoreCase("M") || getSufffixSymbol().contains("M")) {
                        dividend = getPowerOfTen(6);
                    } else if (getSufffixSymbol().equalsIgnoreCase("B") || getSufffixSymbol().contains("B")) {
                        dividend = getPowerOfTen(9);
                    } else if (getSufffixSymbol().equalsIgnoreCase("T") || getSufffixSymbol().contains("T")) {
                        dividend = getPowerOfTen(12);
                    }
                    List columnKeys = dataset.getColumnKeys();
                    List rowKeys = dataset.getRowKeys();
                    if (dividend != 1) {
                        for (int j = 0; j < dataset.getRowCount(); j++) {
                            for (int k = 0; k < dataset.getColumnCount(); k++) {
                                dataset.setValue(dataset.getValue(j, k).doubleValue() / dividend, String.valueOf(rowKeys.get(j)), String.valueOf(columnKeys.get(k)));

                            }
                        }
                        setMaximumValue(getMaximumValue() / dividend);
                        setMinimumValue(getMinimumValue() / dividend);
                        setAverageValue(getAverageValue() / dividend);
                    }
                }



            }
        }
        return dataset;
    }

    public DefaultCategoryDataset buildColumnPieDataSet(ProgenDataSet retObj) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        double value = 0.0;
        int rowCount = 0;
        int ColumnSize;
        boolean exists;
        if (retObj != null && retObj.getViewSequence().size() != 0) {
            exists = memberValueExists(retObj);
            rowCount = retObj.getViewSequence().size();
            ColumnSize = barChartColumnNames.length - viewByColumns.length;
            if (exists) {
                for (int index = 0; index < rowCount; index++) {
                    int actualRow = retObj.getViewSequence().get(index);
                    for (int i = viewByColumns.length; i < (viewByColumns.length + ColumnSize); i++) {

                        value = new Double(retObj.getFieldValueString(actualRow, barChartColumnNames[i]) == null && "".equalsIgnoreCase(retObj.getFieldValueString(actualRow, barChartColumnNames[i])) ? "0" : retObj.getFieldValueString(actualRow, barChartColumnNames[i]));

                        if (actualRow == 0) {
                            setMaximumValue(value);
                            setMinimumValue(value);
                        } else {
                            if (value > getMaximumValue()) {
                                setMaximumValue(value);
                            }
                            if (value < getMinimumValue()) {
                                setMinimumValue(value);
                            }
                        }
                        if (getSwapColumn()) {
                            if (checkViewByExists(getViewBy(retObj, viewByColumns, actualRow))) {
                                dataset.setValue(value, barChartColumnTitles[i], getViewBy(retObj, viewByColumns, actualRow));
                            }
                        } else {
                            if (checkViewByExists(getViewBy(retObj, viewByColumns, actualRow))) {
                                dataset.setValue(value, getViewBy(retObj, viewByColumns, actualRow), barChartColumnTitles[i]);
                            }
                        }
                    }
                }
            } else {
                rowCount = (retObj.getViewSequence().size() == 0) ? 0 : 1;
                for (int index = 0; index < rowCount; index++) {
                    int actualRow = retObj.getViewSequence().get(index);
                    for (int i = viewByColumns.length; i < (viewByColumns.length + ColumnSize); i++) {
                        value = new Double(retObj.getFieldValueString(actualRow, barChartColumnNames[i]) == null && "".equalsIgnoreCase(retObj.getFieldValueString(actualRow, barChartColumnNames[i])) ? "0" : retObj.getFieldValueString(actualRow, barChartColumnNames[i]));

                        if (actualRow == 0) {
                            setMaximumValue(value);
                            setMinimumValue(value);
                        } else {
                            if (value > getMaximumValue()) {
                                setMaximumValue(value);
                            }
                            if (value < getMinimumValue()) {
                                setMinimumValue(value);
                            }
                        }
                        if (getSwapColumn()) {
                            dataset.setValue(value, barChartColumnTitles[i], getViewBy(retObj, viewByColumns, actualRow));
                        } else {
                            dataset.setValue(value, getViewBy(retObj, viewByColumns, actualRow), barChartColumnTitles[i]);
                        }
                    }
                }
            }
        }
        return dataset;
    }

    public DefaultCategoryDataset buildSpiderDataSet(ProgenDataSet retObj) {

        ////////////////.println("buildSpiderDataSet");
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        double value = 0.0;
        int rowCount = 0;
        int ColumnSize;
        boolean exists;
        if (retObj != null && retObj.getViewSequence().size() != 0) {
            exists = memberValueExists(retObj);
            rowCount = retObj.getViewSequence().size();
            ColumnSize = barChartColumnNames.length - viewByColumns.length;
            if (exists) {
                for (int index = 0; index < rowCount; index++) {
                    int actualRow = retObj.getViewSequence().get(index);
                    for (int i = viewByColumns.length; i < (viewByColumns.length + ColumnSize); i++) {

                        value = new Double(retObj.getFieldValueString(actualRow, barChartColumnNames[i]) == null && "".equalsIgnoreCase(retObj.getFieldValueString(actualRow, barChartColumnNames[i])) ? "0" : retObj.getFieldValueString(actualRow, barChartColumnNames[i]));

                        if (actualRow == 0) {
                            setMaximumValue(value);
                            setMinimumValue(value);
                        } else {
                            if (value > getMaximumValue()) {
                                setMaximumValue(value);
                            }
                            if (value < getMinimumValue()) {
                                setMinimumValue(value);
                            }
                        }
                        if (getSwapColumn()) {
                            if (checkViewByExists(getViewBy(retObj, viewByColumns, actualRow))) {
                                dataset.setValue(value, barChartColumnTitles[i], getViewBy(retObj, viewByColumns, actualRow));
                            }
                        } else {
                            if (checkViewByExists(getViewBy(retObj, viewByColumns, actualRow))) {
                                dataset.setValue(value, getViewBy(retObj, viewByColumns, actualRow), barChartColumnTitles[i]);
                            }
                        }
                    }
                }
            } else {
                rowCount = (retObj.getViewSequence().size() == 0) ? 0 : 1;
                for (int index = 0; index < rowCount; index++) {

                    int actualRow = retObj.getViewSequence().get(index);
                    for (int i = viewByColumns.length; i < (viewByColumns.length + ColumnSize); i++) {
                        value = new Double(retObj.getFieldValueString(actualRow, barChartColumnNames[i]) == null && "".equalsIgnoreCase(retObj.getFieldValueString(actualRow, barChartColumnNames[i])) ? "0" : retObj.getFieldValueString(actualRow, barChartColumnNames[i]));

                        if (actualRow == 0) {
                            setMaximumValue(value);
                            setMinimumValue(value);
                        } else {
                            if (value > getMaximumValue()) {
                                setMaximumValue(value);
                            }
                            if (value < getMinimumValue()) {
                                setMinimumValue(value);
                            }
                        }
                        if (getSwapColumn()) {
                            dataset.setValue(value, barChartColumnTitles[i], getViewBy(retObj, viewByColumns, actualRow));
                        } else {
                            dataset.setValue(value, getViewBy(retObj, viewByColumns, actualRow), barChartColumnTitles[i]);
                        }
                    }
                }
            }
        }
        return dataset;
    }

    public DefaultXYZDataset getBubbleDataset(ProgenDataSet retObj) {
        DefaultXYZDataset dataset = new DefaultXYZDataset();

        int ColumnSize = barChartColumnNames.length - viewByColumns.length;
        if (ColumnSize >= 3) {
            for (int index = 0; index < retObj.getViewSequence().size(); index++) {

                int actualRow = retObj.getViewSequence().get(index);
                double d1[] = new double[1];
                double d2[] = new double[1];
                double d3[] = new double[1];

                d1[0] = new Double((retObj.getFieldValueString(actualRow, barChartColumnNames[viewByColumns.length + 0]) == null || "".equalsIgnoreCase(retObj.getFieldValueString(actualRow, barChartColumnNames[viewByColumns.length + 0]))) ? "0" : retObj.getFieldValueString(actualRow, barChartColumnNames[viewByColumns.length + 0]));
                d2[0] = new Double((retObj.getFieldValueString(actualRow, barChartColumnNames[viewByColumns.length + 1]) == null || "".equalsIgnoreCase(retObj.getFieldValueString(actualRow, barChartColumnNames[viewByColumns.length + 0]))) ? "0" : retObj.getFieldValueString(actualRow, barChartColumnNames[viewByColumns.length + 0]));
                d3[0] = new Double((retObj.getFieldValueString(actualRow, barChartColumnNames[viewByColumns.length + 2]) == null || "".equalsIgnoreCase(retObj.getFieldValueString(actualRow, barChartColumnNames[viewByColumns.length + 0]))) ? "0" : retObj.getFieldValueString(actualRow, barChartColumnNames[viewByColumns.length + 0]));

                double ad3[][] = {d1, d2, d3};
                dataset.addSeries(getViewBy(retObj, viewByColumns, actualRow), ad3);

            }
        }
        return (dataset);
    }

    public XYDataset getTimeSeriesdataSet(ProgenDataSet retObj) {
        TimeSeries[] timeSeries = new TimeSeries[barChartColumnNames.length - viewByColumns.length];


        TimeSeriesCollection dataset = new TimeSeriesCollection();
        boolean buildGraph = false;
        Class timeLevelClass = null;

        String dataValue = null;
        String keyValue = null;
        int count = 0;

        int timeViewByIndex = 0;

        if (retObj != null && retObj.getViewSequence().size() != 0) {
            if (viewByColumns != null && barChartColumnNames != null && barChartColumnTitles != null) {
                for (int str = 0; str < viewByColumns.length; str++) {
                    if (viewByColumns[str].contains("Time") || barChartColumnTitles[str].equalsIgnoreCase("Time")) {
                        buildGraph = true;
                        timeViewByIndex = str;

                    }
                }
                if (buildGraph) {
                    for (int index = 0; index < retObj.getViewSequence().size(); index++) {

                        int actualRow = retObj.getViewSequence().get(index);
                        ++count;//to increase the month count.
                        for (int i = viewByColumns.length; i < barChartColumnNames.length; i++) {
                            dataValue = retObj.getFieldValueString(actualRow, barChartColumnNames[i]);
                            keyValue = retObj.getFieldValueString(actualRow, viewByColumns[timeViewByIndex]);

                            keyValue = keyValue.replaceAll("-", "/");
                            Date date = new Date(keyValue);

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);

                            int milliSeconds = calendar.get(Calendar.MILLISECOND);
                            int seconds = calendar.get(Calendar.SECOND);
                            int minute = calendar.get(Calendar.MINUTE);
                            int hour = calendar.get(Calendar.HOUR_OF_DAY);
                            int day = calendar.get(Calendar.DAY_OF_MONTH);
                            int week = calendar.get(Calendar.WEEK_OF_MONTH);
                            int month = calendar.get(Calendar.MONTH) + 1;
                            //quarter = (calendar.get(Calendar.MONTH) / 3) + 1;
                            int year = calendar.get(Calendar.YEAR);


                            if (dataValue == null || dataValue.equalsIgnoreCase("")) {
                                dataValue = "0";
                            }
                            timeLevelClass = Day.class;
                            if (timeSeries[i - viewByColumns.length] == null) {
                                timeSeries[i - viewByColumns.length] = new TimeSeries(barChartColumnTitles[i], timeLevelClass);
                            }



                            timeSeries[i - viewByColumns.length].add((new Day(day, month, year)), new Double(dataValue));

                            /*
                             * //if (getTimeLevel() != null) { if
                             * (getTimeLevel().equalsIgnoreCase("YEAR")) {
                             * timeLevelClass = Year.class; if (timeSeries[i -
                             * viewByColumns.length] == null) { timeSeries[i -
                             * viewByColumns.length] = new
                             * TimeSeries(barChartColumnTitles[i],
                             * timeLevelClass); } timeSeries[i -
                             * viewByColumns.length].add(new Year(year), new
                             * Double(dataValue)); // timeSeries[i].add(new
                             * Year(Integer.parseInt(keyValue)), new
                             * Double(dataValue)); } else if
                             * (getTimeLevel().equalsIgnoreCase("QUARTER")) {
                             * timeLevelClass = Quarter.class;
                             *
                             * if (count > 4) { count = 1; }
                             *
                             * if (timeSeries[i - viewByColumns.length] == null)
                             * { timeSeries[i - viewByColumns.length] = new
                             * TimeSeries(barChartColumnTitles[i],
                             * timeLevelClass); }
                             *
                             * timeSeries[i - viewByColumns.length].add(new
                             * Quarter(count, year), new Double(dataValue));
                             *
                             *
                             * //timeSeries[i].add(new Month(month, year), new
                             * Double(dataValue)); //timeSeries[i].add(new
                             * Quarter(Integer.parseInt(keyValue)), new
                             * Double(dataValue)); } else if
                             * (getTimeLevel().equalsIgnoreCase("MONTH")) {
                             * timeLevelClass = Month.class; if (timeSeries[i -
                             * viewByColumns.length] == null) { timeSeries[i -
                             * viewByColumns.length] = new
                             * TimeSeries(barChartColumnTitles[i],
                             * timeLevelClass); }
                             *
                             * timeSeries[i - viewByColumns.length].add(new
                             * Month(month, year), new Double(dataValue)); }
                             * else if (getTimeLevel().equalsIgnoreCase("WEEK"))
                             * { timeLevelClass = Week.class; if (timeSeries[i -
                             * viewByColumns.length] == null) { timeSeries[i -
                             * viewByColumns.length] = new
                             * TimeSeries(barChartColumnTitles[i],
                             * timeLevelClass); }
                             *
                             * timeSeries[i - viewByColumns.length].add(new
                             * Day(day, month, year), new Double(dataValue)); }
                             * else if (getTimeLevel().equalsIgnoreCase("DAY"))
                             * { timeLevelClass = Day.class; if (timeSeries[i -
                             * viewByColumns.length] == null) { timeSeries[i -
                             * viewByColumns.length] = new
                             * TimeSeries(barChartColumnTitles[i],
                             * timeLevelClass); }
                             *
                             * timeSeries[i - viewByColumns.length].add(new
                             * Day(day, month, year), new Double(dataValue)); }
                             * else if (getTimeLevel().equalsIgnoreCase("HOUR"))
                             * { timeLevelClass = Day.class; if (timeSeries[i -
                             * viewByColumns.length] == null) { timeSeries[i -
                             * viewByColumns.length] = new
                             * TimeSeries(barChartColumnTitles[i],
                             * timeLevelClass); }
                             *
                             * timeSeries[i - viewByColumns.length].add(new
                             * Hour(hour, day, month, year), new
                             * Double(dataValue)); } else if
                             * (getTimeLevel().equalsIgnoreCase("MINUTE")) {
                             * timeLevelClass = Day.class; if (timeSeries[i -
                             * viewByColumns.length] == null) { timeSeries[i -
                             * viewByColumns.length] = new
                             * TimeSeries(barChartColumnTitles[i],
                             * timeLevelClass); }
                             *
                             * timeSeries[i - viewByColumns.length].add(new
                             * Minute(minute, hour, day, month, year), new
                             * Double(dataValue)); } else if
                             * (getTimeLevel().equalsIgnoreCase("SECONDS")) {
                             * timeLevelClass = Day.class; if (timeSeries[i -
                             * viewByColumns.length] == null) { timeSeries[i -
                             * viewByColumns.length] = new
                             * TimeSeries(barChartColumnTitles[i],
                             * timeLevelClass); }
                             *
                             * timeSeries[i - viewByColumns.length].add(new
                             * Second(seconds, minute, hour, day, month, year),
                             * new Double(dataValue)); } else if
                             * (getTimeLevel().equalsIgnoreCase("MILLISECONDS"))
                             * { timeLevelClass = Day.class; if (timeSeries[i -
                             * viewByColumns.length] == null) { timeSeries[i -
                             * viewByColumns.length] = new
                             * TimeSeries(barChartColumnTitles[i],
                             * timeLevelClass); }
                             *
                             * timeSeries[i - viewByColumns.length].add(new
                             * Millisecond(milliSeconds, seconds, minute, hour,
                             * day, month, year), new Double(dataValue)); }
                             *
                             * //}//if timelevel
                             */
                        }// int i view by coloms
                    }//int index
                    for (int i = 0; i < timeSeries.length; i++) {
                        dataset.addSeries(timeSeries[i]);
                    }
                }
            }
        }
        return dataset;
    }

    //added by k
    public XYDataset getTimeSeriesKPIZoom(String[] viewbycolumnsarr, String[] barchartcolumntitlesarr, String[] keyvaluesarr, String[] datavaluesarr) {
        TimeSeries[] timeSeries = new TimeSeries[1];
        //PbReturnObject retObj = new PbReturnObject();

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        boolean buildGraph = true;
        Class timeLevelClass = null;

        String dataValue = null;
        String keyValue = null;


        if (buildGraph) {
            for (int index = 0; index < viewbycolumnsarr.length; index++) {
                int actualRow = index;
                if (retObj.getRowCount() > index) {
                    actualRow = retObj.getViewSequence().get(index);
                }



                dataValue = datavaluesarr[actualRow];
                keyValue = keyvaluesarr[actualRow];



                keyValue = keyValue.replaceAll("-", "/");
                Date date = new Date(keyValue);



                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                int milliSeconds = calendar.get(Calendar.MILLISECOND);
                int seconds = calendar.get(Calendar.SECOND);
                int minute = calendar.get(Calendar.MINUTE);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int week = calendar.get(Calendar.WEEK_OF_MONTH);
                int month = calendar.get(Calendar.MONTH) + 1;
                //quarter = (calendar.get(Calendar.MONTH) / 3) + 1;
                int year = calendar.get(Calendar.YEAR);


                if (dataValue == null || dataValue.equalsIgnoreCase("")) {
                    dataValue = "0";
                }
                timeLevelClass = Day.class;
                if (timeSeries[0] == null) {
                    timeSeries[0] = new TimeSeries(barchartcolumntitlesarr[actualRow], timeLevelClass);
                }

                timeSeries[0].add((new Day(day, month, year)), new Double(dataValue));

            }//int index
            for (int i = 0; i < timeSeries.length; i++) {


                dataset.addSeries(timeSeries[i]);
            }
        }


        return dataset;
    }

    public XYSeriesCollection getScatterDataset(ProgenDataSet retObj) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        int ColumnSize = barChartColumnNames.length - viewByColumns.length;
        ColumnSize = (ColumnSize > 10) ? 10 : ColumnSize;
        TimeSeries[] timeSeries = new TimeSeries[ColumnSize];
        XYSeries[] seriesList = new XYSeries[ColumnSize];
        boolean buildGraph = false;
        Class timeLevelClass = null;
        String dataValue = null;
        String keyValue = null;
        int count = 0;
        int timeViewByIndex = 0;


        if (retObj != null && retObj.getViewSequence().size() != 0) {
            for (int str = 0; str < viewByColumns.length; str++) {
                if (viewByColumns[str].contains("Time") || barChartColumnTitles[str].equalsIgnoreCase("Time")) {
                    buildGraph = true;
                    timeViewByIndex = str;
                }
            }

            if (buildGraph) {
                for (int index = 0; index < retObj.getViewSequence().size(); index++) {

                    int actualRow = retObj.getViewSequence().get(index);
                    ++count;//to increase the month count.
                    for (int i = viewByColumns.length; i < viewByColumns.length + ColumnSize; i++) {
                        dataValue = retObj.getFieldValueString(actualRow, barChartColumnNames[i]);
                        keyValue = retObj.getFieldValueString(actualRow, viewByColumns[timeViewByIndex]);
                        keyValue = keyValue.replaceAll("-", "/");
                        Date date = new Date(keyValue);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);

                        if (dataValue == null || dataValue.equalsIgnoreCase("")) {
                            dataValue = "0";
                        }
                        timeLevelClass = Day.class;
                        if (seriesList[i - viewByColumns.length] == null) {
                            seriesList[i - viewByColumns.length] = new XYSeries(barChartColumnTitles[i]);
                        }
                        seriesList[i - viewByColumns.length].add(calendar.getTimeInMillis(), new Double(dataValue));
                    }
                }
                for (int i = 0; i < seriesList.length; i++) {
                    dataset.addSeries(seriesList[i]);
                }

            } else {
                setScatterViewBy(viewByColumns[1]);//need to be commented later
                boolean scatterExists = false;
                setScatterViewBy(viewByColumns[1]);
                if (getScatterViewBy() != null) {
                    for (int i = 0; i < viewByColumns.length; i++) {
                        if (getScatterViewBy().equalsIgnoreCase(viewByColumns[i])) {
                            scatterExists = true;
                        }
                    }
                }
                if (scatterExists) {
                    for (int index = 0; index < retObj.getViewSequence().size(); index++) {

                        int actualRow = retObj.getViewSequence().get(index);
                        for (int i = viewByColumns.length; i < (viewByColumns.length + ColumnSize); i++) {
                            if (seriesList[i - viewByColumns.length] == null) {
                                seriesList[i - viewByColumns.length] = new XYSeries(barChartColumnTitles[i]);
                            }
                            double x = Double.parseDouble(retObj.getFieldValueString(actualRow, getScatterViewBy()));
                            double y = Double.parseDouble(retObj.getFieldValueString(actualRow, barChartColumnNames[i]));
                            if (x != 0.0 || y != 0.0) {
                                seriesList[i - viewByColumns.length].add(x, y);
                            }
                        }
                    }
                    for (XYSeries series : seriesList) {
                        dataset.addSeries(series);
                    }
                }

            }
        }
        return dataset;
    }

    public DefaultKeyedValues getDefaultKeyedValues(ProgenDataSet retObj) {
        DefaultKeyedValues data = new DefaultKeyedValues();
        String temp = null;
        double value = 0.0;
        int ColumnSize = barChartColumnNames.length - viewByColumns.length;
        ColumnSize = (ColumnSize > 10) ? 10 : ColumnSize;
        int RowSize = 10;
        String RowSizeStatus = "";
        if (!getDisplayGraphRows().equalsIgnoreCase("All")) {
            if (getDisplayGraphRows().equalsIgnoreCase("")) {
                RowSize = 10;
            } else {
                RowSize = Integer.parseInt(getDisplayGraphRows());
            }
        } else {
            RowSizeStatus = getDisplayGraphRows();
        }

        if (retObj != null && retObj.getViewSequence().size() != 0) {
            if (RowSizeStatus.equalsIgnoreCase("All")) {
                RowSize = retObj.getViewSequence().size();
                ////////////////.println("in if=="+RowSize);
            } else {
                RowSize = (retObj.getViewSequence().size() < RowSize) ? retObj.getViewSequence().size() : RowSize;
                ////////////////.println("in if==else"+RowSize);
            }

            for (int index = 0; index < RowSize; index++) {
                int actualRow = retObj.getViewSequence().get(index);
                temp = retObj.getFieldValueString(actualRow, barChartColumnNames[viewByColumns.length]);
                if ("".equalsIgnoreCase(temp)) {
                    temp = "0";
                }
                value = new Double(temp);
                if (actualRow == 0) {
                    setMaximumValue(value);
                    setMinimumValue(value);
                } else {
                    if (value > getMaximumValue()) {
                        setMaximumValue(value);
                    }
                    if (value < getMinimumValue()) {
                        setMinimumValue(value);
                    }
                }
                if (getSwapColumn()) {
                    data.addValue(getViewBy(retObj, viewByColumns, actualRow), value);
                } else {
                    data.addValue(getViewBy(retObj, viewByColumns, actualRow), value);
                }
            }
            if (getSufffixSymbol() != null && !getSufffixSymbol().equalsIgnoreCase("")) {
                double dividend = 1;
                if (getSufffixSymbol().equalsIgnoreCase("K") || getSufffixSymbol().contains("K")) {
                    dividend = getPowerOfTen(3);
                } else if (getSufffixSymbol().equalsIgnoreCase("M") || getSufffixSymbol().contains("M")) {
                    dividend = getPowerOfTen(6);
                } else if (getSufffixSymbol().equalsIgnoreCase("B") || getSufffixSymbol().contains("B")) {
                    dividend = getPowerOfTen(9);
                } else if (getSufffixSymbol().equalsIgnoreCase("T") || getSufffixSymbol().contains("T")) {
                    dividend = getPowerOfTen(12);
                }
                List keys = data.getKeys();
                if (dividend != 1) {
                    for (int j = 0; j < data.getItemCount(); j++) {
                        for (int k = 0; k < keys.size(); k++) {
                            data.addValue(String.valueOf(keys.get(j)), data.getValue(k).doubleValue() / dividend);
                        }
                    }
                }
            }
        }
        StringBuffer sbuffer = new StringBuffer();
        sbuffer.append(barChartColumnTitles[viewByColumns.length]);
//        for (int k = 0; k < viewByColumns.length; k++) {
//            sbuffer.append("," + barChartColumnTitles[k]);
//        }
        //setGrplyaxislabel(sbuffer.substring(1));
        setGrplyaxislabel(sbuffer.toString());
        return data;
    }

    public String getViewBy(ProgenDataSet retObj, String[] viewByColumns, int rowNum) {
        StringBuffer viewBy = new StringBuffer();
        for (int i = 0; i < viewByColumns.length; i++) {
            viewBy.append("," + retObj.getFieldValueString(rowNum, viewByColumns[i]));
        }

        return viewBy.substring(1);
    }

    public boolean memberValueExists(ProgenDataSet retObj) {
        String[] dbColumns = null;
        boolean exists = false;
        dbColumns = retObj.getColumnNames();
        for (int index = 0; index < retObj.getViewSequence().size(); index++) {
            int actualRow = retObj.getViewSequence().get(index);
            for (String str : dbColumns) {
                if (getRowValues().contains(retObj.getFieldValueString(actualRow, str))) {
                    exists = true;
                }
            }
        }
        return exists;
    }

    public String getShowGT() {
        return showGT;
    }

    public void setShowGT(String showGT) {
        this.showGT = showGT;
    }

    public double getOverAllMinimumValue() {
        return overAllMinimumValue;
    }

    public void setOverAllMinimumValue(double overAllMinimumValue) {
        this.overAllMinimumValue = overAllMinimumValue;
    }

    public double getOverAllMaximumValue() {
        return overAllMaximumValue;
    }

    public void setOverAllMaximumValue(double overAllMaximumValue) {
        this.overAllMaximumValue = overAllMaximumValue;
    }

    public ArrayList ProGenChart(ProgenDataSet retObj, int width, int height) throws Exception {
        String temp = null;
        BigDecimal[] grandTotals = null;
        NumberFormat nFormat = NumberFormat.getInstance(Locale.US);
        nFormat.setMaximumFractionDigits(2);
        nFormat.setMinimumFractionDigits(1);


        ArrayList ProGenChartList = new ArrayList();
        int RowSize = Integer.parseInt(getDisplayGraphRows());
        int ColumnSize = barChartColumnNames.length - viewByColumns.length;
        ColumnSize = (ColumnSize < 5) ? ColumnSize : 5;
        if (retObj != null && retObj.getViewSequence().size() != 0) {
            RowSize = (retObj.getViewSequence().size() < RowSize) ? retObj.getViewSequence().size() : RowSize;
            grandTotals = new BigDecimal[RowSize];
            RowSize = (RowSize < 10) ? RowSize : 10;
            for (int index = 0; index < RowSize; index++) {
                int actualRow = retObj.getViewSequence().get(index);
                if (grandTotals[actualRow] == null) {
                    grandTotals[actualRow] = new BigDecimal("1.0");
                }
                for (int i = viewByColumns.length; i < (viewByColumns.length + ColumnSize); i++) {
                    temp = retObj.getFieldValueString(actualRow, barChartColumnNames[i]) != null ? retObj.getFieldValueString(actualRow, barChartColumnNames[i]) : "0";
                    grandTotals[actualRow] = grandTotals[actualRow].add(new BigDecimal(temp));
                }
            }

            StringBuffer dispChart = new StringBuffer("");
            double[] celWidth = new double[ColumnSize];
            BigDecimal[] cellValues = new BigDecimal[ColumnSize];
            for (int index = 0; index < RowSize; index++) {
                int actualRow = retObj.getViewSequence().get(index);
                String viewBy = getViewBy(retObj, viewByColumns, actualRow);
                dispChart.append("<table style=\"width:" + width + "px;\">");
                dispChart.append("<tr>");
                dispChart.append("<td style='width:100px'><font style='font-size:9px;font-family:VERDANA;color:black;font-weight:bold'>" + viewBy + "</font></td>");
                dispChart.append("<td>");
                dispChart.append("<Table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;height:20px\"><Tr style='width:100%'>");
                for (int i = viewByColumns.length; i < (viewByColumns.length + ColumnSize); i++) {
//                    cellValues[i - viewByColumns.length] = (retObj.getFieldValueBigDecimal(i, barChartColumnNames[i]).divide(grandTotals[index], MathContext.DECIMAL32)).multiply(multiplier);
                    cellValues[i - viewByColumns.length] = (retObj.getFieldValueBigDecimal(actualRow, barChartColumnNames[i]).divide(grandTotals[actualRow], MathContext.DECIMAL32)).multiply(multiplier);

//                    celWidth[i - viewByColumns.length] = (retObj.getFieldValueBigDecimal(i, barChartColumnNames[i]).divide(grandTotals[index], MathContext.DECIMAL32)).multiply(multiplier).doubleValue();
                    celWidth[i - viewByColumns.length] = (retObj.getFieldValueBigDecimal(actualRow, barChartColumnNames[i]).divide(grandTotals[actualRow], MathContext.DECIMAL32)).multiply(multiplier).doubleValue();
                    celWidth[i - viewByColumns.length] = Math.round(celWidth[i - viewByColumns.length]);
                    if (celWidth[i - viewByColumns.length] == 0.0) {
                        celWidth[i - viewByColumns.length] = 1;
                    }

//                    dispChart.append("<Td style=\"width:" + celWidth[i - viewByColumns.length] + "%;height:4px;font-size:8px;font-family:VERDANA;background-color:" + COLORS[i - viewByColumns.length] + " ;padding:8px;valign=middle\" align=\"center\" title=\" (" + viewBy + "," + cellValues[i - viewByColumns.length] + "%" + ")\" >" + nFormat.format(cellValues[i - viewByColumns.length].doubleValue()) + "</Td>");
                    dispChart.append("<Td style=\"width:" + celWidth[i - viewByColumns.length] + "%;height:4px;font-size:8px;font-family:VERDANA;background-color:" + COLORS[i - viewByColumns.length] + " ;padding:8px;valign=middle\" align=\"center\" title=\" (" + viewBy + "," + cellValues[i - viewByColumns.length] + "%" + ")\" >" + nFormat.format(retObj.getFieldValueBigDecimal(actualRow, barChartColumnNames[i]).doubleValue()) + "</Td>");
                }
                dispChart.append("</Tr>");
                dispChart.append("</Table>");
                dispChart.append("</td>");
                dispChart.append("</tr>");
                dispChart.append("</table>");
                dispChart.append("</br>");
            }
            StringBuffer dispLegend = new StringBuffer("");
            dispLegend.append("<div style=\"width:" + width + "px;border:1px #000\" >");
            dispLegend.append("<table style=\"width:auto\" align='center' >");
            dispLegend.append("<tr align=\"center\">");
            for (int l = viewByColumns.length; l < (viewByColumns.length + ColumnSize); l++) {
                dispLegend.append("<td align=\"center\" style='width:15px'>");
                dispLegend.append("<div style='background-color:" + COLORS[l - viewByColumns.length] + ";width:10px;height:5px;'></div>");
                dispLegend.append("</td>");
                dispLegend.append("<td style='font-size:8px;font-family:VERDANA;color:black;text-align:left'>");
                dispLegend.append(barChartColumnTitles[l]);
                dispLegend.append("</td>");
            }
            dispLegend.append("</tr>");
            dispLegend.append("</table>");
            dispLegend.append("</div>");

            ProGenChartList.add(dispChart.toString());
            ProGenChartList.add(dispLegend.toString());
        }


        return ProGenChartList;
    }

    public TreeMap getColumnOverAllMaximums() {
        return columnOverAllMaximums;
    }

    public void setColumnOverAllMaximums(TreeMap columnOverAllMaximums) {
        this.columnOverAllMaximums = columnOverAllMaximums;
    }

    public TreeMap getColumnOverAllMinimums() {
        return columnOverAllMinimums;
    }

    public void setColumnOverAllMinimums(TreeMap columnOverAllMinimums) {
        this.columnOverAllMinimums = columnOverAllMinimums;
    }

    public TreeMap getColumnAverages() {
        return columnAverages;
    }

    public void setColumnAverages(TreeMap columnAverages) {
        this.columnAverages = columnAverages;
    }

    public TreeMap getColumnGrandTotals() {
        return columnGrandTotals;
    }

    public void setColumnGrandTotals(TreeMap columnGrandTotals) {
        this.columnGrandTotals = columnGrandTotals;
    }

    public void buildMaxValues(String columnName, boolean flag) {
        if (getColumnOverAllMaximums() != null && getColumnOverAllMaximums().get(columnName) != null) {
            BigDecimal value = (BigDecimal) getColumnOverAllMaximums().get(columnName);
            BigDecimal currMaxValue = new BigDecimal(getMaximumValue());
            if (flag) {
                setMaximumValue(value.doubleValue());
            } else {
                value = value.max(currMaxValue);
                setMaximumValue(value.doubleValue());
            }
        }
    }

    public void buildMinValues(String columnName, boolean flag) {
        if (getColumnOverAllMinimums() != null && getColumnOverAllMinimums().get(columnName) != null) {
            BigDecimal value = (BigDecimal) getColumnOverAllMinimums().get(columnName);
            BigDecimal currMinValue = new BigDecimal(getMinimumValue());
            if (flag) {
                setMinimumValue(value.doubleValue());
            } else {
                value = value.min(currMinValue);
                setMinimumValue(value.doubleValue());
            }



        }
    }

    public static void main(String[] args) {
        ArrayList alist = new ArrayList();
        for (int i = 1; i <= 13; i++) {
            alist.add(i);
        }
        StringBuffer sbuffer = new StringBuffer();
        //sbuffer.append("");


    }

    public double getDividend() {
        return dividend;
    }

    public void setDividend(double dividend) {
        this.dividend = dividend;
    }

    public String getStartindex() {
        return startindex;
    }

    public void setStartindex(String startindex) {
        this.startindex = startindex;
    }

    public String getEndindex() {
        return endindex;
    }

    public void setEndindex(String endindex) {
        this.endindex = endindex;
    }

    public String getGraphid() {
        return graphid;
    }

    public void setGraphid(String graphid) {
        this.graphid = graphid;
    }

    public double getAverageValue() {
        return averageValue;
    }

    public void setAverageValue(double averageValue) {
        this.averageValue = averageValue;
    }

    /**
     * @return the graphProperty
     */
    public GraphProperty getGraphProperty() {
        return graphProperty;
    }

    /**
     * @param graphProperty the graphProperty to set
     */
    public void setGraphProperty(GraphProperty graphProperty) {
        this.graphProperty = graphProperty;
    }

    public ArrayList<Integer> getViewSequence() {
        return viewSequence;
    }

    public void setViewSequence(ArrayList<Integer> viewSequence) {
        this.viewSequence = viewSequence;
    }

    public boolean isIsCrosstab() {
        return isCrosstab;
    }

    public void setIsCrosstab(boolean isCrosstab) {
        this.isCrosstab = isCrosstab;
    }

    private boolean compareStringArray(String[] temp, String[] currDimValue) {
        int count = 0;
        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equalsIgnoreCase(currDimValue[i])) {
                count = count + 1;
            }
        }
        if (count == temp.length) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<String> getSortColumns() {
        return sortColumns;
    }

    public void setSortColumns(ArrayList<String> sortColumns) {
        this.sortColumns = sortColumns;
    }
}
