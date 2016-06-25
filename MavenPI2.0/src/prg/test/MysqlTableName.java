/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.test;

import java.sql.*;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class MysqlTableName {

    public static Logger logger = Logger.getLogger(MysqlTableName.class);
    List<TableHelper> tableHelperList = new ArrayList<TableHelper>();
    private HttpServletRequest request;

    public HashMap getTableNames(String url, String username, String password) {
        Connection con = null;

        String db = "prprogen";
        String url_Db = url;
        String driver = "com.mysql.jdbc.Driver";
        String user = username;
        String pass = password;
        ArrayList tables = new ArrayList();

        HashMap<String, TableHelper> list = new HashMap<String, TableHelper>();
        try {
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("url-------"+url+"-----------uer---"+user+"------------pwd---------"+pass);
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, pass);
            try {
                DatabaseMetaData dbm = con.getMetaData();
                String[] types = {"TABLE", "VIEW"};
                ResultSet rs = dbm.getTables(null, null, "%", types);

                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Table name:");
                TableHelper tableHelper = null;
                Set<String> columnNames = null;
                ArrayList<String> columnTypes = null;
                ArrayList columnSizes = null;
                String tableName = "";
                while (rs.next()) {

                    tableHelper = new TableHelper();
                    tableName = rs.getString("TABLE_NAME");
                    if (tableName.equalsIgnoreCase("PR_DAY_DENOM")) {
                    }
                    tableHelper.setTableName(tableName);
                    columnNames = new HashSet<String>();
                    columnTypes = new ArrayList<String>();
                    columnSizes = new ArrayList();
                    ResultSet tempRs = dbm.getColumns(null, null, tableName, "%");
                    while (tempRs.next()) {
                        columnNames.add(tempRs.getString("COLUMN_NAME"));
                        columnTypes.add(tempRs.getString("TYPE_NAME"));
                        columnSizes.add(tempRs.getString("COLUMN_SIZE"));
                    }
                    tableHelper.setColumnNames(columnNames);
                    tableHelper.setColumnTypes((ArrayList<String>) columnTypes.clone());
                    tableHelper.setColumnSizes((ArrayList) columnSizes.clone());
                    list.put(tableName, tableHelper);
//           tableHelperList.add(tableHelper);
                }

//        while (rs.next()){
//            ArrayList columns = new ArrayList();
//            ArrayList dataTypes = new ArrayList();
//            ArrayList colLengths = new ArrayList();
//          String table = rs.getString("TABLE_NAME");



                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(table);
//          tables.add(table);
//          ResultSet rs1 = dbm.getColumns(null, null, table, "%");
//          int i=0;
//           ResultSetMetaData rdmd=rs1.getMetaData();
//
//          while(rs1.next())
//          {
//
//              ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Column Of "+table+" are "+rs1.getString("COLUMN_NAME"));
//              ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Column DATA_TYPE are "+rs1.getString("DATA_TYPE"));
//              ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Column TYPE_NAME are "+rs1.getString("TYPE_NAME"));//SQL_DATA_TYPE
//              ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Column SQL_DATA_TYPE are "+rs1.getString("SQL_DATA_TYPE"));//
//              ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Column SIZE are "+rs1.getString("COLUMN_SIZE"));//
//              columns.add(rs1.getString("COLUMN_NAME"));
//              dataTypes.add(rs1.getString("TYPE_NAME"));
//              colLengths.add(rs1.getString("COLUMN_SIZE"));
//              //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("The Colums types are"+rs1.getString(""));
//             //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("COL TYPE IS-->"+rdmd.getColumnTypeName(i));
//              i++;
//          }
//          list.put(table,columns);
//          list.put(table+"type",dataTypes);
//          list.put(table+"length",colLengths);
//          colLengths  = null;
//          dataTypes = null;
//          columns = null;

//        }
                request.getSession(false).setAttribute("MYSQLTABLES", list.clone());
            } catch (SQLException s) {
                logger.error("Exception: ", s);
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("No any table in the database");
            }
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }

        return list;
    }

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/prprogen";
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(new MysqlTableName().getTableNames(url, "root","root"));
    }

    /**
     * @return the request
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * @param request the request to set
     */
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
}
