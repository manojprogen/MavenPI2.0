/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.Createtable;

import java.sql.*;
import org.apache.log4j.Logger;

public class CheckDSNname {

    public static Logger logger = Logger.getLogger(CheckDSNname.class);

    public static void main(String args[]) {
        String dsnName = "progentest";
        Connection connection = null;
        try {
            logger.info("dsn\t" + dsnName);
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            connection = DriverManager.getConnection("jdbc:odbc:" + dsnName + "");
            logger.info("jdbc:odbc:" + dsnName + "");
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("Select * from [orders$]");
            ResultSetMetaData rsmd = rs.getMetaData();
            int numberOfColumns = rsmd.getColumnCount();
            logger.info("numberOfColumns: " + numberOfColumns);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }
}
