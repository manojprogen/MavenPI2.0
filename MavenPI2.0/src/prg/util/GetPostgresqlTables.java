package prg.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

public class GetPostgresqlTables {

    public static Logger logger = Logger.getLogger(GetPostgresqlTables.class);

    public void InsertPostGresqlTables() throws SQLException {

        String username = "postgres";
        String password = "welcome";
        String server = "localhost";
        String port = "5432";
        String databasename = "ameyodbdump";

        Connection con = ProgenConnection.postgresqlConnection(server, port, databasename, username, password);
        //////.println("connction==" + con);
        Statement st = con.createStatement();
        Statement st2 = con.createStatement();
        String sql = "SELECT tablename FROM pg_tables  where schemaname='public'";
        ResultSet rs = st2.executeQuery(sql);
        PbReturnObject pbro = new PbReturnObject(rs);
        //////.println("obro===" + pbro.getRowCount());//pbro.getRowCount()

        for (int i = 0; i < pbro.getRowCount(); i++) {
            try {
                String tabName = pbro.getFieldValueString(i, 0);
                if (tabName.length() > 31) {
                    tabName = pbro.getFieldValueString(i, 0).substring(0, 28) + (i + 1);
                }
                String cretesql = "CREATE TABLE " + tabName + "(";
                String insertsql = "insert into " + pbro.getFieldValueString(i, 0) + "(";
                sql = "SELECT *  FROM " + pbro.getFieldValueString(i, 0);
                //////.println("rs.getString(1)==" + pbro.getFieldValueString(i, 0));
                //////.println("sql is" + sql);
                ResultSet rs1 = st.executeQuery(sql);
                ResultSet rs2 = rs1;
                PbReturnObject pbro1 = new PbReturnObject(rs2);
                ResultSetMetaData rs1metadata = rs1.getMetaData();
                //////.println("rsaa--" + rs1metadata);
                int columnCount = rs1metadata.getColumnCount();
                //////.println("column count==" + columnCount);
                //String numTypes={"int"};
                Connection con1 = ProgenConnection.getInstance().getConnection();
                String dattypearr[] = new String[columnCount];
                for (int i1 = 1; i1 <= columnCount; i1++) {
                    //////.println("i==" + i1);
                    //////.println(i + "--rs1==" + rs1metadata.getColumnName(i1) + "===" + rs1metadata.getColumnTypeName(i1) + "===" + rs1metadata.getPrecision(i1));
                    String columnName = rs1metadata.getColumnName(i1);
                    if (columnName.length() > 31) {
                        columnName = columnName.substring(0, 28) + (i1 + 1);
                    }
                    int len = 255;
                    if (rs1metadata.getPrecision(i1) > 4000) {
                        len = 4000;
                    }
                    String odatatype = "varchar2(" + len + ")";
                    String otype = rs1metadata.getColumnTypeName(i1);
                    if (otype.equalsIgnoreCase("text")) {

                        odatatype = "clob";

                    } else if (otype.contains("int") || otype.contains("oid") || otype.contains("float") || otype.contains("serial") || otype.contains("real")) {
                        odatatype = "number";
                    } else if (otype.contains("time")) {

                        odatatype = "timestamp";

                    } else if (otype.contains("date")) {
                        odatatype = "date";
                    } else {
                        odatatype = "varchar2(" + len + ")";
                    }
                    dattypearr[i1 - 1] = odatatype.trim();
                    cretesql += " " + columnName + " " + odatatype;
                    insertsql += "" + rs1metadata.getColumnName(i1);
                    if (i1 < columnCount) {
                        cretesql += " , ";
                        insertsql += " , ";
                    }

                }
                //////.println("-----------------------------------------------------");
                cretesql += " ) ";
                insertsql += " )values(";


                String droptab = " drop table " + pbro.getFieldValueString(i, 0);
                //////.println("cretesql===" + cretesql);
                //////.println("insertsql==" + insertsql);
                //////.println("droptab==" + droptab);
                Statement st1 = con1.createStatement();
                Statement st3 = con1.createStatement();
                //st3.executeQuery(droptab);
                //////.println("====");
                st1.executeQuery(cretesql);
                //////.println("pbro1==" + pbro1.getRowCount());
                // pbro1.getRowCount()

                for (int j = 0; j < pbro1.getRowCount(); j++) {
                    String insertsqlnew = insertsql;
                    for (int k = 0; k < columnCount; k++) {
                        //////.println("pbro1.getFieldValueString(j,k)==" + pbro1.getFieldValueString(j, k) + "===" + dattypearr[k]);
                        String val = pbro1.getFieldValueString(j, k);
                        if (val.equalsIgnoreCase(",")) {
                            val = "chr(44)";
                        }
                        if (dattypearr[k].contains("number")) {
                            //////.println("dattypearr[k]==" + dattypearr[k]);
                            insertsqlnew += "" + val + ",";
                        } else if (dattypearr[k].contains("timestamp")) {
                            //////.println("dattypearr[k]==" + dattypearr[k]);
                            insertsqlnew += "" + "TO_DATE('" + val + "','Month dd, YYYY, HH:MI A.M.')" + ",";
                        } else if (dattypearr[k].contains("date")) {
                            //////.println("dattypearr[k]==" + dattypearr[k]);
                            insertsqlnew += "" + "TO_DATE('" + val + "','dd-MON-YYYY')" + ",";
                        } else {
                            //////.println("dattypearr[k]==" + dattypearr[k]);
                            if (val.equalsIgnoreCase("chr(44)")) {
                                insertsqlnew += "" + val + ",";
                            } else {
                                insertsqlnew += "'" + val + "',";
                            }
                        }

                    }
                    if (!insertsqlnew.equalsIgnoreCase("")) {
                        if (insertsqlnew.trim().endsWith(",")) {
                            insertsqlnew = insertsqlnew.substring(0, (insertsqlnew.trim().length() - 1));
                            insertsqlnew += ")";
                        }
                        Statement st4 = con1.createStatement();
                        st4.executeUpdate(insertsqlnew);
                        //////.println("insersql===" + insertsqlnew);
                    }
                }
            } catch (Exception e) {
                logger.error("Exception: ", e);
                continue;
            }
        }
    }

    public HashMap getTableNames(String hostname, String dbName, String userName, String password, String port, String connection) {
        Connection con = null;


        ArrayList tables = new ArrayList();
        String tabarr = "";
        HashMap list = new HashMap();
        try {

            con = ProgenConnection.getInstance().getConnectionByConId(connection);


            //////.println("connction==" + con);
            Statement st = con.createStatement();
            Statement st2 = con.createStatement();
//            String sql = "SELECT viewname FROM pg_views where schemaname='public' order by 1 ";
            String sql = "SELECT tablename FROM pg_tables  where schemaname='public'";
//            String sql = " SELECT tablename FROM pg_tables where schemaname='public'";
            //sql += " union all SELECT viewname FROM pg_views where schemaname='public' ";
            ResultSet rs = st2.executeQuery(sql);
            PbReturnObject pbro = new PbReturnObject(rs);
            ////.println("obro===" + pbro.getRowCount());//pbro.getRowCount()

            String tabName = "";



            ResultSet rs1, rs2 = null;
            PbReturnObject pbro1 = null;
            ResultSetMetaData rs1metadata = null;
            int columnCount = 0;
// //////.println("arr size " + arr.length);
            //Connection con1 = ProgenConnection.getCustomerConn1();
            for (int i = 0; i < pbro.getRowCount(); i++) {
                tabName = pbro.getFieldValueString(i, 0);
                ArrayList columns = new ArrayList();
                ArrayList dataTypes = new ArrayList();
                ArrayList colLengths = new ArrayList();

                sql = "SELECT * FROM " + tabName + " where 1=2";
//                ////.println("Sql for select :: " + sql);
                rs1 = st.executeQuery(sql);
                ////////.println("sql is\t" + sql);
                rs2 = rs1;
                pbro1 = new PbReturnObject(rs2);
                rs1metadata = rs1.getMetaData();
                ////////.println("rsaa--" + rs1metadata);
                columnCount = rs1metadata.getColumnCount();

                for (int i1 = 1; i1 <= columnCount; i1++) {
                    ////////.println("i==" + i1);
                    ////////.println(i + "--rs1==" + rs1metadata.getColumnName(i1) + "===" + rs1metadata.getColumnTypeName(i1) + "===" + rs1metadata.getPrecision(i1));
                    String columnName = rs1metadata.getColumnName(i1);

                    int len = 255;
                    if (rs1metadata.getPrecision(i1) > 4000) {
                        len = 4000;
                    }
                    String odatatype = "varchar2";
                    String otype = rs1metadata.getColumnTypeName(i1);
                    /*
                     * if (otype.equalsIgnoreCase("text")) {
                     *
                     * odatatype = " clob ";
                     *
                     * } else
                     */
                    if (otype.contains("int") || otype.contains("oid") || otype.contains("float") || otype.contains("serial") || otype.contains("real")) {
                        odatatype = " number ";
                    } else if (otype.contains("time")) {

                        odatatype = "date";

                    } else if (otype.contains("date")) {
                        odatatype = "date";
                    } else {
                        odatatype = "varchar2";
                    }
                    columns.add(columnName);
                    dataTypes.add(odatatype);
                    colLengths.add(len);
                }

                list.put(tabName, columns);
                list.put(tabName + "type", dataTypes);
                list.put(tabName + "length", colLengths);
            }

        }
        catch (SQLException ex) {
             logger.error("Exception: ", ex);
        }  catch (Exception ex) {
             logger.error("Exception: ", ex);
        }       return list;
    }

    public HashMap getTableViews(String hostname, String dbName, String userName, String password, String port) {
        HashMap viewsmap = new HashMap();
        Connection con = null;
        //SELECT viewname FROM pg_views where schemaname='public'
        try {
//            ProgenConnection prgcon = new ProgenConnection();
            con = ProgenConnection.postgresqlConnection(hostname, port, dbName, userName, password);
            //////.println("connction==" + con);
            Statement st = con.createStatement();
            Statement st2 = con.createStatement();
            String sql = "SELECT viewname FROM pg_views where schemaname='public'";
//            String sql = "SELECT tablename FROM pg_tables where schemaname='public'";
//            sql += " union all SELECT viewname FROM pg_views where schemaname='public'";
            ResultSet rs = st2.executeQuery(sql);
            PbReturnObject pbro = new PbReturnObject(rs);
            ////////.println("obro===" + pbro.getRowCount());//pbro.getRowCount()
            ////.println("pbro===" + pbro.getRowCount());
            String tabName = "";



            ResultSet rs1, rs2 = null;
            PbReturnObject pbro1 = null;
            ResultSetMetaData rs1metadata = null;
            int columnCount = 0;
// //////.println("arr size " + arr.length);
            //Connection con1 = ProgenConnection.getCustomerConn1();
            for (int i = 0; i < pbro.getRowCount(); i++) {
                tabName = pbro.getFieldValueString(i, 0);
                ArrayList columns = new ArrayList();
                ArrayList dataTypes = new ArrayList();
                ArrayList colLengths = new ArrayList();

                sql = "SELECT * FROM " + tabName + " where 1=2";
                //////.println("Sql for select :: "+ sql);
                rs1 = st.executeQuery(sql);
                ////////.println("sql is\t" + sql);
                rs2 = rs1;
                pbro1 = new PbReturnObject(rs2);
                rs1metadata = rs1.getMetaData();
                ////////.println("rsaa--" + rs1metadata);
                columnCount = rs1metadata.getColumnCount();

                for (int i1 = 1; i1 <= columnCount; i1++) {
                    ////////.println("i==" + i1);
                    ////////.println(i + "--rs1==" + rs1metadata.getColumnName(i1) + "===" + rs1metadata.getColumnTypeName(i1) + "===" + rs1metadata.getPrecision(i1));
                    String columnName = rs1metadata.getColumnName(i1);

                    int len = 255;
                    if (rs1metadata.getPrecision(i1) > 4000) {
                        len = 4000;
                    }
                    String odatatype = "varchar2";
                    String otype = rs1metadata.getColumnTypeName(i1);
                    /*
                     * if (otype.equalsIgnoreCase("text")) {
                     *
                     * odatatype = " clob ";
                     *
                     * } else
                     */
                    if (otype.contains("int") || otype.contains("oid") || otype.contains("float") || otype.contains("serial") || otype.contains("real")) {
                        odatatype = " number ";
                    } else if (otype.contains("time")) {

                        odatatype = "date";

                    } else if (otype.contains("date")) {
                        odatatype = "date";
                    } else {
                        odatatype = "varchar2";
                    }
                    columns.add(columnName);
                    dataTypes.add(odatatype);
                    colLengths.add(len);
                }

                viewsmap.put(tabName, columns);
//                viewsmap.put(tabName + "type", dataTypes);
//                viewsmap.put(tabName + "length", colLengths);
            }

        }
        catch (SQLException ex) {
             logger.error("Exception: ", ex);
        } catch (Exception ex) {
             logger.error("Exception: ", ex);
        }       return viewsmap;
    }

    public HashMap getTableDetails(Connection con, String tableName) {

        ArrayList tables = new ArrayList();
        String tabarr = "";
        String colNames[] = null;
        String colTypes[] = null;
        String colLength[] = null;
        String tableInfromation[] = new String[3];
        HashMap list = new HashMap();
        try {

            Statement st = con.createStatement();
            String sql = "SELECT tablename FROM pg_tables where schemaname='public' and tablename=?" ;
            PreparedStatement st2 = con.prepareStatement(sql);
            st2.setString(1, tableName);
            ResultSet rs = st2.executeQuery(sql);
            PbReturnObject pbro = new PbReturnObject(rs);

            String tabName = "";

            ResultSet rs1, rs2 = null;
            PbReturnObject pbro1 = null;
            ResultSetMetaData rs1metadata = null;
            int columnCount = 0;
          Connection con1 = ProgenConnection.getInstance().getConnection();
            for (int i = 0; i < pbro.getRowCount(); i++) {
                tabName = pbro.getFieldValueString(i, 0);
                ArrayList columns = new ArrayList();
                ArrayList dataTypes = new ArrayList();
                ArrayList colLengths = new ArrayList();

                sql = "SELECT * FROM " + tabName;
                rs1 = st.executeQuery(sql);
                rs2 = rs1;
                pbro1 = new PbReturnObject(rs2);
                rs1metadata = rs1.getMetaData();
                columnCount = rs1metadata.getColumnCount();
                colNames = new String[columnCount];
                colLength = new String[columnCount];
                colTypes = new String[columnCount];

                for (int i1 = 1; i1 <= columnCount; i1++) {
                    String columnName = rs1metadata.getColumnName(i1);
                    int len = 255;
                    if (rs1metadata.getPrecision(i1) > 4000) {
                        len = 4000;
                    }
                    String odatatype = "varchar2";
                    String otype = rs1metadata.getColumnTypeName(i1);

                    if (otype.contains("int") || otype.contains("oid") || otype.contains("float") || otype.contains("serial") || otype.contains("real")) {
                        odatatype = " number ";
                    } else if (otype.contains("time")) {

                        odatatype = "date";

                    } else if (otype.contains("date")) {
                        odatatype = "date";
                    } else {
                        odatatype = "varchar2";
                    }
                    colNames[i1] = columnName;
                    colTypes[i1] = odatatype;
                    colLength[i1] = String.valueOf(len);
                    columns.add(columnName);
                    dataTypes.add(odatatype);
                    colLengths.add(len);
                }

                list.put(tabName, colNames);
                list.put("datatype", colTypes);
                list.put("datalength", colLength);
//                tableInfromation[0] = colNames.toString();
//                tableInfromation[1] = colTypes.toString();
//                tableInfromation[2] = colLength.toString();
            }

        } catch (SQLException e) {
            logger.error("Exception: ", e);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return list;
    }

    public ArrayList getPreviousTables(String connId) {
        ArrayList prvstables = new ArrayList();
        try {
            String chkqry = "select connection_id,user_schema from prg_db_master_table where connection_id=" + connId;
            PbDb pbdb = new PbDb();
            PbReturnObject pbretchktabobj = new PbReturnObject();
            pbretchktabobj = pbdb.execSelectSQL(chkqry);
            for (int i = 0; i < pbretchktabobj.getRowCount(); i++) {
                prvstables.add(pbretchktabobj.getFieldValueString(i, "USER_SCHEMA"));
            }
        } catch (SQLException e) {
            logger.error("Exception: ", e);
        }catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return prvstables;
    }

    public static void main(String args[]) throws Exception {
        new GetPostgresqlTables().InsertPostGresqlTables();
    }
}
