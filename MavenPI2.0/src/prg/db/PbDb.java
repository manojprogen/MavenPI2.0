package prg.db;
//import pb.parametermaster.params.PbParameterMasterParams;
import com.progen.db.POIDataSet;
import com.progen.db.ProgenDataSet;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import utils.db.ProgenConnection;

public class PbDb implements Serializable {

    private static final long serialVersionUID = 75264711556227L;//added by Dinanath for serialization and derialization
    //method to build the query from the query and object array of params variables
    public static Logger logger = Logger.getLogger(PbDb.class);

    public String buildQuery(String query, Object params[]) {
        String finalQuery = "";
        String var = null;

        //Split the String sql command on basis of & character
        String str[] = query.split("&");

        //Loop through the String sql command and iconcatenate the parameters in place of & to build query
        for (int counter = 0; counter < str.length; counter++) {
            var = "";
            if (counter < params.length) {
                if (params[counter] != null) {
                    var = (params[counter]).toString();
                } else {
                    var = "";
                }
            }
            finalQuery += str[counter] + var.toString();
        }
        return finalQuery;
    }

    //method to  insert,update and delete the user details
    public void execModifySQL(String finalBuildQuery) throws Exception {
        Connection con = getConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(finalBuildQuery);
            logger.info("Final query Executing: " + finalBuildQuery);
            boolean status = ps.execute();
            logger.info("Final query Executed Successfully.");
            ps.close();
            ps = null;
            con.close();
            con = null;
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    //method to  display the user details and all user details
    public PbReturnObject execSelectSQL(String finalBuildQuery) throws SQLException {
        PbReturnObject pbro = null;
        Connection con = this.getConnection();

        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(finalBuildQuery);
            logger.info("Final query Executing: " + finalBuildQuery);
            rs = ps.executeQuery();
            logger.info("Final query Executed Successfully.");
            pbro = new PbReturnObject(rs);
            rs.close();
            rs = null;
            ps.close();
            ps = null;
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                try {
                    con.commit();
                } catch (java.sql.SQLException e) {
                }

            }
            con.close();
            con = null;
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (con != null) {
                con.close();
            }


        }
        return pbro;
    }
    //this method works for updating clob written by swathi

    public PbReturnObject executeSelectSQL(String finalBuildQuery) throws SQLException {
        PbReturnObject pbro = null;
        Connection con = this.getConnection();

        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            logger.info("Final query Executing: " + finalBuildQuery);
            ps = con.prepareStatement(finalBuildQuery);
            rs = ps.executeQuery();
            logger.info("Final query Executed Successfully.");
            pbro = new PbReturnObject(rs);
            rs.close();
            rs = null;
            ps.close();
            ps = null;
            //  con.commit();
            con.close();
            con = null;
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (con != null) {
                con.close();
            }


        }
        return pbro;
    }

    public Connection getConnection() throws SQLException {
//        ProgenConnection pg = new ProgenConnection();
        Connection con = ProgenConnection.getInstance().getConnection();
        return con;
    }

    public int getSequenceNumber(String finalBuildQuery) throws Exception {
//        int id = 0;
////        ProgenConnection pg = new ProgenConnection();
//        Connection con =  this.getConnection();
//        ResultSet rs = null;
//        PreparedStatement ps = null;
//        try {
//            ps = con.prepareStatement(finalBuildQuery);
//            rs = ps.executeQuery();
//            PbReturnObject pbro = new PbReturnObject(rs);
//            id = pbro.getFieldValueInt(0, 0);
//            rs.close();
//            rs = null;
//            ps.close();
//            ps = null;
//            con.close();
//            con = null;
//        } catch (Exception ex) {
//            logger.error("Exception:",ex);
//        } finally {
//            if ( ps != null )
//                ps.close();
//            if ( rs != null)
//                rs.close();
//            if ( con != null )
//                con.close();
//
//
//        }
        Connection con = this.getConnection();
        return getSequenceNumber(finalBuildQuery, con);
    }

    public int getSequenceNumber(String finalQuery, Connection con) throws Exception {
        int id = 0;
//        ProgenConnection pg = new ProgenConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(finalQuery);
            logger.info("Final query Executing: " + finalQuery);
            rs = ps.executeQuery();
            logger.info("Final query Executed Successfully.");
            PbReturnObject pbro = new PbReturnObject(rs);
            id = pbro.getFieldValueInt(0, 0);
            rs.close();
            rs = null;
            ps.close();
            ps = null;
            con.close();
            con = null;
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (con != null) {
                con.close();
            }


        }
        return id;
    }

    public int getCurrentSequenceNumber(String finalBuildQuery) throws Exception {
        int id = 0;
//        ProgenConnection pg = new ProgenConnection();
        Connection con = this.getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(finalBuildQuery);
            logger.info("Final query Executing: " + finalBuildQuery);
            rs = ps.executeQuery();
            logger.info("Final query Executed Successfully.");
            PbReturnObject pbro = new PbReturnObject(rs);
            id = pbro.getFieldValueInt(0, 0);
            rs.close();
            rs = null;
            ps.close();
            ps = null;
            con.close();
            con = null;
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (con != null) {
                con.close();
            }


        }
        return id;
    }

    public boolean executeMultiple(ArrayList queries) {
//        boolean flag = true;
//        Connection con = null;
//        Statement st = null;
//        try {
//            con = getConnection();
//            st = con.createStatement();
//            con.setAutoCommit(false);
//            for (int i = 0; i < queries.size(); i++) {
//                try {
//                    st.executeUpdate(String.valueOf(queries.get(i)));
//                } catch (SQLException e) {
//                    logger.error("Exception:",e);
//                    flag = false;
//                    con.rollback();
//                    st.close();
//                    st = null;
//                    con.close();
//                    con = null;
//                    break;
//                }
//            }
//            if (flag) {
//                //con.commit();
//            }
//            if ( st != null )
//            {
//                st.close();
//                st = null;
//            }
//            if ( con != null )
//            {
//                con.close();
//                con = null;
//            }
//
//        } catch (Exception e) {
//            flag = false;
//        } finally {
//            try {
//                if (st != null)
//                    st.close();
//
//                if (con != null)
//                    con.close();
//            } catch(SQLException se){}
//        }
//        return flag;


        // new executemultipal
        try {
            Connection con = getConnection();
            int flag = 0;
            PreparedStatement ps = null;
            try {
                for (int i = 0; i < queries.size(); i++) {
                    ps = con.prepareStatement(String.valueOf(queries.get(i)));
                    logger.info("Final query Executing: " + String.valueOf(queries.get(i)));
                    flag = ps.executeUpdate();
                    logger.info("Final query Executed Successfully.");
                }
                ps.close();
                ps = null;
                con.close();
                con = null;
            } catch (Exception ex) {
                flag = 0;
                logger.error("Exception:", ex);
            } finally {
                if (con != null) {
                    con.close();
                }
            }
            //modified by Nazneen
//        if(flag==1){
//        return true;
//            }
//        else{
//            return false;
//            }
            if (flag == 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception ee) {
            return false;
        }

    }
    //added by santhosh.kumar@progenbusiness.com on 27/08/09 for executing multiple queries  from dynamic connection  which is passed as argument

    public boolean executeMultiple(ArrayList queries, Connection con) throws Exception {
        boolean flag = true;
        Statement st = null;
        try {
            st = con.createStatement();
            con.setAutoCommit(false);
            for (int i = 0; i < queries.size(); i++) {
                try {
                    logger.info("Final query Executing: " + String.valueOf(queries.get(i)));
                    st.executeUpdate(String.valueOf(queries.get(i)));
                    logger.info("Final query Executed Successfully.");
                } catch (SQLException e) {
                    logger.error("Exception:", e);

                    flag = false;
                    con.rollback();

                    st.close();
                    st = null;
                    break;
                }
            }
            if (flag) {
                con.commit();
            }
            if (st != null) {
                st.close();
            }
            st = null;
            con.close();
            con = null;

        } catch (Exception e) {
            flag = false;
        } finally {
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return flag;
    }

//added by santhosh.kumar@progenbusiness.com on 27/08/09 for executing select queries  from dynamic connection  which is passed as argument
    public PbReturnObject execSelectSQL(String finalBuildQuery, Connection con) throws Exception {
        PbReturnObject pbro = null;
        ResultSet rs = null;
        try {
            PreparedStatement ps = con.prepareStatement(finalBuildQuery);
            logger.info("Final query Executing: " + finalBuildQuery);
            rs = ps.executeQuery();
            logger.info("Final query Executed Successfully.");
            pbro = new PbReturnObject(rs);
            ps.close();
            ps = null;
            rs.close();
            rs = null;
            con.close();
            con = null;
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return pbro;
    }

    public ProgenDataSet execSelectSQL(String finalBuildQuery, Connection con, boolean poiDataSet) throws Exception {
        ProgenDataSet dataSet = null;
        ResultSet rs = null;
        try {

            PreparedStatement ps = con.prepareStatement(finalBuildQuery);
            logger.info("Final query Executing: " + finalBuildQuery);
            rs = ps.executeQuery();
            logger.info("Final query Executed Successfully.");
            if (poiDataSet) {
                dataSet = new POIDataSet(rs, true);
            } else {
                dataSet = new PbReturnObject(rs, true);
            }
            ps.close();
            ps = null;
            rs.close();
            rs = null;
            con.close();
            con = null;
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return dataSet;
    }

    public PbReturnObject execSelectSQLWithFlag(String finalBuildQuery, Connection con) throws Exception {
        PbReturnObject pbro = null;
        ResultSet rs = null;
        try {
            PreparedStatement ps = con.prepareStatement(finalBuildQuery);
            logger.info("Final query Executed Successfully.");
            rs = ps.executeQuery();
            logger.info("Final query Executed Successfully.");
            pbro = new PbReturnObject(rs, true);
            ps.close();
            ps = null;
            rs.close();
            rs = null;
            con.close();
            con = null;
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return pbro;
    }

    public int execUpdateSQL(String finalBuildQuery) throws Exception {
        Connection con = getConnection();
        int flag = 0;
        try {
            PreparedStatement ps = con.prepareStatement(finalBuildQuery);
            logger.info("Final query Executing: " + finalBuildQuery);
            flag = ps.executeUpdate();
            logger.info("Final query Executed Successfully.");
            ps.close();
            ps = null;
            if (!con.getAutoCommit()) {
                con.commit();
            }
            con.close();
            con = null;
        } catch (Exception ex) {
            flag = 0;
            logger.error("Exception:", ex);
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return flag;
    }

    public int execUpdateSQL(String finalBuildQuery, Connection con) throws Exception {
        int flag = 0;
        try {
            PreparedStatement ps = con.prepareStatement(finalBuildQuery);
            logger.info("Final query Executing: " + finalBuildQuery);
            flag = ps.executeUpdate();
            logger.info("Final query Executed Successfully.");
            ps.close();
            ps = null;
            if (!con.getAutoCommit()) {
                con.commit();
            }
            con.close();
            con = null;
        } catch (Exception ex) {
            logger.error("Exception:", ex);
            flag = 0;
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return flag;
    }
    //added by Mohit
    public int execUpdateSQLWithFlag(String finalBuildQuery, Connection con) throws Exception {
        int flag = 0;
        try {
            PreparedStatement ps = con.prepareStatement(finalBuildQuery);
            logger.info("Final query Executing: " + finalBuildQuery);
            flag = ps.executeUpdate();
            logger.info("Final query Executed Successfully.");
            ps.close();
            ps = null;
            if (!con.getAutoCommit()) {
                con.commit();
            }
            con.close();
            con = null;
        } catch (Exception ex) {
            logger.error("Exception:", ex);
            flag = -1;
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return flag;
    }
    //added by bharathi reddy for executing multiple queries of customer connection

    public boolean executeMultipleCustomer(ArrayList queries) throws Exception {
        boolean flag = true;

        Connection con = null;
        Statement st = null;
        try {
            con = getCustomerConnection();
            st = con.createStatement();

            con.setAutoCommit(false);
            for (int i = 0; i < queries.size(); i++) {
                try {
                    logger.info("Final query Executing: " + String.valueOf(queries.get(i)));
                    st.executeUpdate(String.valueOf(queries.get(i)));
                    logger.info("Final query Executed Successfully.");
                } catch (SQLException e) {
                    logger.error("Exception:", e);
                    flag = false;
                    con.rollback();
                    st.close();
                    con.close();
                    con = null;
                    break;
                }
            }
            if (flag) {
                con.commit();
            }
            if (con != null) {
                con.close();
                con = null;
            }
        } catch (Exception e) {
            flag = false;
        } finally {
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return flag;
    }

    public Connection getCustomerConnection() throws Exception {
//        ProgenConnection pg = new ProgenConnection();
        Connection con = ProgenConnection.getInstance().getCustomerConn();
        return con;
    }

    //added by uday on 12-Apr-2010
    public ResultSet execSelectSQLWithFlagForWhatIf(String finalBuildQuery, Connection con) throws Exception {
        ResultSet rs = null;
        try {
            PreparedStatement ps = con.prepareStatement(finalBuildQuery);
            logger.info("Final query Executing: " + finalBuildQuery);
            rs = ps.executeQuery();
            logger.info("Final query Executed Successfully.");
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return rs;
    }

    public int insertAndGetSequenceInSQLSERVER(String finalBuildQuery, String tableName) throws Exception {
        Connection con = null;
        int flag = 0;
        try {
            con = getConnection();
            PreparedStatement ps = con.prepareStatement(finalBuildQuery);
            logger.info("Final query Executing: " + finalBuildQuery);
            flag = ps.executeUpdate();
            logger.info("Final query Executed Successfully.");
            ps.close();
            ps = null;
            con.close();
            con = null;
        } catch (Exception e) {
            logger.error("Exception:", e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    logger.error("Exceptiloggeron:" + ex);
                }
            }
        }
        return getSequenceNumber("select IDENT_CURRENT(" + "\'" + tableName + "\'" + ")");
    }
//by g

    public int insertAndGetSequenceInMySql(String finalBuildQuery, String tableName, String columnName) throws Exception {
        Connection con = null;
        int flag = 0;
        try {
            con = getConnection();
            PreparedStatement ps = con.prepareStatement(finalBuildQuery);
            logger.info("Final query Executing: " + finalBuildQuery);
            flag = ps.executeUpdate();
            logger.info("Final query Executed Successfully.");
            ps.close();
            ps = null;
            con.close();
            con = null;
        } catch (Exception e) {
            logger.error("Exception:", e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    logger.error("Exceptiloggeron:" + ex);
                }
            }
        }
        return getSequenceNumber("select LAST_INSERT_ID(" + "" + columnName + "" + " ) from " + "" + tableName + "" + " order by 1 desc limit 1");
    }

    public static void main(String[] args) throws SQLException {
        ProgenConnection pg;
    }

    public int execInsertSqlReturnSEQ(String finalQuery, String tableName) throws SQLException, Exception {//for Oracle who wants current seq of give table when insert time
        int seq = 0;
        try {
            //for Oracle who wants current seq of give table when insert time
            Connection con = getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                ps = con.prepareStatement(finalQuery);
                logger.info("Final query Executing: " + finalQuery);
                boolean status = ps.execute();
                logger.info("Final query Executed Successfully.");
                ps.close();
                ps = null;
                ps = con.prepareStatement("SELECT " + tableName + ".CURRVAL FROM DUAL");
                logger.info("Final query Executing: " + "SELECT " + tableName + ".CURRVAL FROM DUAL");
                rs = ps.executeQuery();
                logger.info("Final query Executed Successfully.");
                PbReturnObject pbro = new PbReturnObject(rs);
                seq = pbro.getFieldValueInt(0, 0);
                rs.close();
                rs = null;
                ps.close();
                ps = null;
                con.close();
                con = null;

            } catch (Exception ex) {
                logger.error("Exception:", ex);
            } finally {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            }
        } catch (Exception ex) {
            logger.error("Exception:" + ex);
        }
        return seq;
    }

    public String execInsert(String finalQuery) throws SQLException, Exception {

        try {
            //for Oracle who wants current seq of give table when insert time
            Connection con = getConnection();
            PreparedStatement ps = null;

            try {
                ps = con.prepareStatement(finalQuery);
                logger.info("Final query Executing: " + finalQuery);
                ps.execute();
                logger.info("Final query Executed Successfully.");
                ps.close();
                con.close();
                con = null;

            } catch (Exception ex) {
                logger.error("Exception:", ex);
                return null;
            } finally {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            }
        } catch (Exception ex) {
            logger.error("Exception:" + ex);
            return null;
        }
        return null;
    }

    public boolean execMultiple(ArrayList queries) {
        boolean flag = true;
        Connection con = null;
        Statement st = null;
        try {
            con = getConnection();
            st = con.createStatement();
            con.setAutoCommit(false);
            for (int i = 0; i < queries.size(); i++) {
                try {
                    logger.info("Final query Executing: " + String.valueOf(queries.get(i)));
                    st.executeUpdate(String.valueOf(queries.get(i)));
                    logger.info("Final query Executed Successfully.");
                } catch (SQLException e) {
                    logger.error("Exception:", e);
                    flag = false;
                    con.rollback();
                    st.close();
                    st = null;
                    con.close();
                    con = null;
                    break;
                }
            }
            if (flag) {
                //con.commit();
            }
            if (st != null) {
                st.close();
                st = null;
            }
            if (con != null) {
                con.close();
                con = null;
            }

        } catch (Exception e) {
            flag = false;
        } finally {
            try {
                if (st != null) {
                    st.close();
                }

                if (con != null) {
                    con.close();
                }
            } catch (SQLException se) {
            }
        }
        return flag;

    }
//    public void execonnCommit(){
//        Connection con = null;
//        try {
//              con = this.getConnection();
//              con.commit();
//              con.close();
//            
//         } catch (Exception ex) {
//            logger.error("Exception: ",ex);
//        }
//    }

    public ProgenDataSet execSelectORACLE(String finalBuildQuery, Connection con, boolean poiDataSet, ArrayList<String> sessionQueryLst) throws Exception {
        ProgenDataSet dataSet = null;
        ResultSet rs = null;
        try {

            //added by anitha
            try {
                if (sessionQueryLst != null && !sessionQueryLst.isEmpty()) {
                    for (int i = 0; i < sessionQueryLst.size(); i++) {

                        PreparedStatement ps1 = con.prepareStatement(sessionQueryLst.get(i));
                        logger.info("Final query Executing: " + sessionQueryLst.get(i));
                        rs = ps1.executeQuery();
                        logger.info("Final query Executed Successfully.");
                        ps1.close();
                        ps1 = null;
                        rs.close();
                        rs = null;
                    }
                }
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
            PreparedStatement ps = con.prepareStatement(finalBuildQuery);
            logger.info("Final query Executing: " + finalBuildQuery);
            rs = ps.executeQuery();
            logger.info("Final query Executed Successfully.");
            if (poiDataSet) {
                dataSet = new POIDataSet(rs, true);
            } else {
                dataSet = new PbReturnObject(rs, true);
            }
            ps.close();
            ps = null;
            rs.close();
            rs = null;
            con.close();
            con = null;
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return dataSet;
    }

    //method execSelectORCL added by anitha
    public PbReturnObject execSelectORCL(String finalBuildQuery, Connection con, ArrayList<String> sessionQueryLst) throws Exception {
        PbReturnObject pbro = null;
        ResultSet rs = null;
        try {

            try {
                if (sessionQueryLst != null && !sessionQueryLst.isEmpty()) {
                    for (int i = 0; i < sessionQueryLst.size(); i++) {

                        PreparedStatement ps1 = con.prepareStatement(sessionQueryLst.get(i));
                        logger.info("Final query Executing: " + sessionQueryLst.get(i));
                        rs = ps1.executeQuery();
                        logger.info("Final query Executed Successfully.");
                        ps1.close();
                        ps1 = null;
                        rs.close();
                        rs = null;
                    }
                }
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }

            PreparedStatement ps = con.prepareStatement(finalBuildQuery);
            logger.info("Final query Executing: " + finalBuildQuery);
            rs = ps.executeQuery();
            logger.info("Final query Executing: " + finalBuildQuery);
            pbro = new PbReturnObject(rs);
            ps.close();
            ps = null;
            rs.close();
            rs = null;
            con.close();
            con = null;
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return pbro;
    }
}
