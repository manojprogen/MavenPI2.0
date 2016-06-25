/*
 * PbExcelDriver.java
 *
 * Created on June 28, 2009, 3:46 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package prg.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Calendar;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import prg.db.PbReturnObject;

/*
 *
 * @author Administrator
 */
public class PbExcelDriver {

    public static Logger logger = Logger.getLogger(PbExcelDriver.class);

    /**
     * Creates a new instance of PbExcelDriver
     */
    public PbExcelDriver() {
    }

    public void createExcel(PbReturnObject ret, String fileName, HttpServletResponse response, String[] types) {
        this.createExcel(ret, fileName, response, types, ret.getColumnNames());
    }

    public void createExcel(PbReturnObject ret, String fileName, HttpServletResponse response, String[] types, String[] columns) {
        try {
            String exists = "";
            String colsAll = "";
            StringBuilder colCells = new StringBuilder();
            String rowValues = "";
            String tabNamePattern = "tabPattern";
            StringBuffer sb = new StringBuffer();

            Calendar cal = Calendar.getInstance();
//            fileName = "" + cal.getTimeInMillis() + "_" + fileName;
            fileName = Long.toString( cal.getTimeInMillis() )+ "_" + fileName;

            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            String driver = "Driver=Microsoft Excel Driver (*.xls);ReadOnly=1;DBQ=" + fileName;
            Connection connection = DriverManager.getConnection("jdbc:odbc:" + driver);
            Statement statement = connection.createStatement();

            if (connection.isReadOnly()) {
                connection.setReadOnly(false);
            }

            DatabaseMetaData databasemetadata = connection.getMetaData();

            for (ResultSet rs = databasemetadata.getTables(null, null, tabNamePattern, new String[]{"TABLE"}); rs.next();) {
                exists = "exists";
            }

            if (!exists.equals("exists")) {
                int rowCount = ret.getRowCount();
                int colCount = columns.length;
                for (int j = 0; j < colCount; j++) {
                    if ("C".equals(types[j])) {
//                        colCells = colCells + columns[j] + " TEXT,";
                        colCells.append( columns[j] ).append( " TEXT,");
                    } else if ("N".equals(types[j])) {
//                        colCells = colCells + columns[j] + " NUMBER,";
                        colCells.append( columns[j] ).append(" NUMBER,");
                    } else if ("D".equals(types[j])) {
//                        colCells = colCells + columns[j] + " DATE,";
                        colCells.append(columns[j] ).append(" DATE,");
                    }
                }

                colCells = new StringBuilder(colCells.substring(0, colCells.length() - 1));
                sb.append("create table " + tabNamePattern);
                sb.append("(").append( colCells ).append( ")");

                 statement.execute(sb.toString());

                for (int i = 0; i < rowCount; i++) {
                    for (int j = 0; j < colCount; j++) {
                        if ("C".equals(types[j]) || "N".equals(types[j])) {
                            rowValues = rowValues + "'" + ret.getFieldValueString(i, columns[j]) + "',";
                        } else if ("D".equals(types[j])) {
                            rowValues = rowValues + "'" + ret.getFieldValueDateString(i, columns[j]) + "',";
                        }
                    }

                    rowValues = rowValues.substring(0, rowValues.length() - 1);
                    rowValues = "insert into [" + tabNamePattern + "$] values (" + rowValues + ")";
                    statement.executeUpdate(rowValues);
                    rowValues = "";
                }
            }

            connection.close();
        } catch (ClassNotFoundException e) {
            logger.error("Exception: ", e);
        } catch (SQLException e) {
            logger.error("Exception: ", e);
        }

        //Start Creating file
        try {
            File file = new File(fileName);
            String fName = file.getName();
            fName = fName.substring(fName.indexOf("_") + 1, fName.length());
            response.setContentType("application/x-download");
            response.setHeader("Content-Disposition", "attachment;filename=" + fName);
            FileInputStream fis = new FileInputStream(file);
            ServletOutputStream outputstream = response.getOutputStream();
            int i = 256;
            boolean flag = false;
            while (i >= 0) {
                i = fis.read();
                outputstream.write(i);
            }
            outputstream.flush();
            outputstream.close();
            fis.close();
            file.delete();
        } catch (IOException e) {
            logger.error("Exception: ", e);
        }
    }

    public void createExcel(PbReturnObject ret, String fileName, HttpServletResponse response, String[] types, String[] columns, String[] disColumns) {
        try {
            String exists = "";
            String colsAll = "";
            String colCells = "";
            String rowValues = "";
            String tabNamePattern = "tabPattern";
            StringBuffer sb = new StringBuffer();

            Calendar cal = Calendar.getInstance();
            fileName = "" + cal.getTimeInMillis() + "_" + fileName;
            fileName = Long.toString(cal.getTimeInMillis()) + "_" + fileName;

            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            String driver = "Driver=Microsoft Excel Driver (*.xls);ReadOnly=1;DBQ=" + fileName;
            Connection connection = DriverManager.getConnection("jdbc:odbc:" + driver);
            Statement statement = connection.createStatement();

            if (connection.isReadOnly()) {
                connection.setReadOnly(false);
            }

            DatabaseMetaData databasemetadata = connection.getMetaData();

            for (ResultSet rs = databasemetadata.getTables(null, null, tabNamePattern, new String[]{"TABLE"}); rs.next();) {
                exists = "exists";
            }

            //for (int i = 0; i < disColumns.length; i++) {
            //disColumns[i] = disColumns[i].trim().replace("%", "_PERC");
            //}
            if (!exists.equals("exists")) {
                int rowCount = ret.getRowCount();
                int colCount = columns.length;
                for (int j = 0; j < colCount; j++) {
                    if ("C".equals(types[j])) {
                        colCells = colCells + disColumns[j].trim().replace("%", "_PERC").replace(" ", "_") + " TEXT,";
                    } else if ("N".equals(types[j])) {
                        colCells = colCells + disColumns[j].trim().replace("%", "_PERC").replace(" ", "_") + " NUMBER,";
                    } else if ("D".equals(types[j])) {
                        colCells = colCells + disColumns[j].trim().replace("%", "_PERC").replace(" ", "_") + " DATE,";
                    }
                }

                colCells = colCells.substring(0, colCells.length() - 1);
  sb.append("create table " + tabNamePattern);
                sb.append("(" + colCells + ")");

                boolean flag = statement.execute(sb.toString());

                for (int i = 0; i < rowCount; i++) {
                    for (int j = 0; j < colCount; j++) {
                        if ("C".equals(types[j]) || "N".equals(types[j])) {
                            rowValues = rowValues + "'" + ret.getFieldValueString(i, columns[j]) + "',";
                        } else if ("D".equals(types[j])) {
                            rowValues = rowValues + "'" + ret.getFieldValueDateString(i, columns[j]) + "',";
                        }
                    }

                    rowValues = rowValues.substring(0, rowValues.length() - 1);
                    rowValues = "insert into [" + tabNamePattern + "$] values (" + rowValues + ")";
                   statement.executeUpdate(rowValues);
                    rowValues = "";
                }
            }

            connection.close();
        } catch (ClassNotFoundException e) {
            logger.error("Exception: ", e);
        } catch (SQLException e) {
            logger.error("Exception: ", e);
        }

        //Start Creating file
        try {
            File file = new File(fileName);
            String fName = file.getName();
            fName = fName.substring(fName.indexOf("_") + 1, fName.length());
            response.setContentType("application/x-download");
            response.setHeader("Content-Disposition", "attachment;filename=" + fName);
            FileInputStream fis = new FileInputStream(file);
            ServletOutputStream outputstream = response.getOutputStream();
            int i = 256;
            boolean flag = false;
            while (i >= 0) {
                i = fis.read();
                outputstream.write(i);
            }
            outputstream.flush();
            outputstream.close();
            fis.close();
            file.delete();
        } catch (IOException e) {
             logger.error("Exception: ", e);
        }
    }
}
