/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class SqlConnection {

    public static Logger logger = Logger.getLogger(SqlConnection.class);

    public HashMap getTableNames(String url, String username, String password) {
        Connection con = null;
        String db = "metadata";
        String url_Db = url;
        String driver = "net.sourceforge.jtds.jdbc.Driver";
        String user = username;
        String pass = password;
        ArrayList tables = new ArrayList();
        String tabarr = "";
        HashMap list = new HashMap();
        ////.println("url\t"+url+"\nuser\t"+user+"\npass\t"+pass);
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, pass);
            try {
                DatabaseMetaData dbm = con.getMetaData();
                ////.println("con . "+con);
                String[] types = {"TABLE"};
                ResultSet rs = dbm.getTables(null, null, "%", types);
                String table = "";
                ////.println("Table name:");
                while (rs.next()) {

                    table = rs.getString("TABLE_NAME");
                    ////////////////////////////////////////////////////////////////////////////////////////////////.println(table);
                    tables.add(table);
                    tabarr += "," + table;
                }
                ////////////////////////////////////////////////////////////////////////////////////////////////.println(" tabarr "+tabarr);
                tabarr = tabarr.substring(1);
                String arr[] = tabarr.split(",");

                PreparedStatement st = null;
                ResultSet rs1 = null;
                ////////////////////////////////////////////////////////////////////////////////////////////////.println("arr  size "+arr.length);
                for (int k1 = 0; k1 < arr.length; k1++) {
                    ////////////////////////////////////////////////////////////////////////////////////////////////.println("con --. "+con);
                    ArrayList columns = new ArrayList();
                    ArrayList dataTypes = new ArrayList();
                    ArrayList colLengths = new ArrayList();
                    ////////////////////////////////////////////////////////////////////////////////////////////////.println("before get columns -- "+arr[k1]);
                    // ResultSet rs1 = dbm.getColumns(null, null, arr[k1], "%");
                    String tabQuery = "select column_name,data_type,character_maximum_length from information_schema.columns where table_name = '" + arr[k1] + "'";
                    ////////////////////////////////////////////////////////////////////////////////////////////////.println(" tabQuery "+tabQuery);
                    st = con.prepareStatement(tabQuery);
                    rs1 = null;
                    rs1 = st.executeQuery();

                    while (rs1.next()) {
                        ////////////////////////////////////////////////////////////////////////////////////////////////.println("----k----------------------"+k1);

                        String column_name = rs1.getString("column_name");
                        String data_type = rs1.getString("data_type");
                        String character_maximum_length = "";
                        ////////////////////////////////////////////////////////////////////////////////////////////////.println("Column Name --  " + column_name);
                        ////////////////////////////////////////////////////////////////////////////////////////////////.println("Column Type --  " + data_type);
                        ////////////////////////////////////////////////////////////////////////////////////////////////.println("column size ---  " + character_maximum_length);

                        /*
                         * if(rs1.getString("character_maximum_length")!=null ||
                         * (!rs1.getString("character_maximum_length").equalsIgnoreCase(""))){
                *
                         */
                        character_maximum_length = rs1.getString("character_maximum_length");
                        //   ////////////////////////////////////////////////////////////////////////////////////////////////.println("...in fi ");
               /*
                         * }
                         * else{ character_maximum_length = "";
                         * ////////////////////////////////////////////////////////////////////////////////////////////////.println("........in
                         * else..... ;; ");
                         */
                        // }
                        ////////////////////////////////////////////////////////////////////////////////////////////////.println("............. ;; ");
                        columns.add(column_name);
                        dataTypes.add(data_type);
                        colLengths.add(character_maximum_length);


                    }


                    list.put(arr[k1], columns);
                    list.put(arr[k1] + "type", dataTypes);
                    list.put(arr[k1] + "length", colLengths);

                }

                // ////////////////////////////////////////////////////////////////////////////////////////////////.println("columns "+columns+" dataTypes "+dataTypes+" colLengths "+colLengths);
            } catch (Exception ex) {
                ////////////////////////////////////////////////////////////////////////////////////////////////.println(" s. mss "+s.getMessage());
                logger.error("Exception:", ex);
                ////////////////////////////////////////////////////////////////////////////////////////////////.println("No any table in the database");
            }

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        return list;
    }

    public static void main(String[] args) {
//        String url = "jdbc:odbc:SQL_SERVER";
//        new SqlConnection().getTableNames(url, "","");
    }
}
