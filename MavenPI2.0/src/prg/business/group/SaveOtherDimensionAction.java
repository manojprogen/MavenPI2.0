/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.business.group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import utils.db.ProgenConnection;

public class SaveOtherDimensionAction extends org.apache.struts.action.Action {

    public static Logger logger = Logger.getLogger(SaveOtherDimensionAction.class);
    Connection con;
    Statement    st3;
    ResultSet rs, rs1;
    private final static String SUCCESS = "success";

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
         PreparedStatement stm =null;
        try {
            con = ProgenConnection.getInstance().getConnection();
            int connectionId = 0;
            //String colName = request.getParameter("colName");
            //String colId = request.getParameter("colId");
//            String tabId = request.getParameter("tabId");
            //String colType = request.getParameter("colType");
            //String bussTableId = request.getParameter("bussTableId");
//            String bussColId = request.getParameter("bussColId");
//            String tabName = request.getParameter("tabName");
            //String grpId = request.getParameter("grpId");

            String colName = (String) request.getSession().getAttribute("colName");
//            String colId = (String) request.getSession().getAttribute("colId");
            String colType = (String) request.getSession().getAttribute("colType");
            String bussTableId = (String) request.getSession().getAttribute("bussTableId");
            String grpId = (String) request.getSession().getAttribute("grpId");
            String memberName = request.getParameter("mName");
            String dimensionName = request.getParameter("dName");
            /////Get DimensionId///

            Statement dimSt = con.createStatement();
            ResultSet dimRs = dimSt.executeQuery("select PRG_GRP_DIMENSIONS_SEQ.nextval from dual");
            dimRs.next();
            int dimId = dimRs.getInt(1);


            ///Check If 'other' Dimension exists for that groupId///
            String checkOtherQuery = "select dim_name from prg_grp_dimensions where grp_id=?  and dim_name='other'";
            stm = con.prepareStatement(checkOtherQuery);
            stm.setString(1, grpId);
            rs = stm.executeQuery(checkOtherQuery);
            // if(!rs.next())
            // {
            String otherInsertQuery = "insert into prg_grp_dimensions (DIM_ID,DIM_NAME,DIM_DESC,DIM_ACTIVE,GRP_ID) values (?,'?','?','Y',?)";
            //String grpDimTableQuery = "insert into PRG_GRP_DIM_TABLES (DIM_TAB_ID,DIM_ID,TAB_ID) values (PRG_GRP_DIM_TABLES_SEQ.nextval,PRG_GRP_DIMENSIONS_SEQ.currval)";
              stm = con.prepareStatement(otherInsertQuery);
            stm.setInt(1,  dimId);
            stm.setString(2,   dimensionName );
            stm.setString(3,   dimensionName );
            stm.setString(4,   grpId );
            stm.executeUpdate(otherInsertQuery);
            // }
            /////Get The ConnectionId/////////
            String conIdQuery = "select puc.connection_id FROM PRG_USER_CONNECTIONS puc, prg_db_master_table pdmt, prg_grp_buss_table pgbt, prg_grp_buss_table_src pgbts where puc.connection_id= pdmt.connection_id and pgbt.buss_table_id= pgbts.buss_table_id and pgbts.db_table_id=pdmt.table_id and pgbt.buss_table_id=?";
             stm = con.prepareStatement(conIdQuery);
            stm.setString(1,  bussTableId);
            rs1 = stm.executeQuery(conIdQuery);
            if (rs1.next()) {
                connectionId = rs1.getInt(1);
            }
            if(stm!=null){
                stm.close();
            }
            //////Insert Into PRG_DB_MASTER_TABLE////////
            String DBMasterQuery = "insert into PRG_DB_MASTER_TABLE (CONNECTION_ID,USER_SCHEMA,TABLE_ID,TABLE_NAME,TABLE_ALIAS,TABLE_TYPE) values (" + connectionId + ",'" + "PRG_" + colName + "',PRG_DATABASE_MASTER_SEQ.nextval,'" + "PRG_" + colName + "','" + "PRG_" + colName + "','Table')";
            String DbDetailsQuery = "insert into PRG_DB_MASTER_TABLE_DETAILS (COLUMN_ID,TABLE_ID,TABLE_COL_NAME,TABLE_COL_CUST_NAME,COL_TYPE) values (PRG_DB_MASTER_TABLE_DTLS_SEQ.nextval,PRG_DATABASE_MASTER_SEQ.currval,'" + colName + "','" + colName + "','" + colType + "')";
            st3 = con.createStatement();
            st3.addBatch(DBMasterQuery);
            st3.addBatch(DbDetailsQuery);
            //st3.executeBatch();

            /////Inserting Into PRG_GRP_BUSS_TABLE//////////////
            String GDBMasterQuery = "insert into PRG_GRP_BUSS_TABLE (BUSS_TABLE_ID,BUSS_TABLE_NAME,BUSS_DESC,BUSS_TYPE,NO_OF_NODES,IS_PURE_QUERY,DB_TABLE_ID,GRP_ID) values (PRG_GRP_BUSS_TABLE_SEQ.nextval,'" + "PRG_" + colName + "','" + "PRG_" + colName + "','Table',1,'N',PRG_DATABASE_MASTER_SEQ.currval," + grpId + ")";
            String GDSRCQuery = "insert into PRG_GRP_BUSS_TABLE_SRC (BUSS_SOURCE_ID,BUSS_TABLE_ID,CONNECTION_ID,DB_TABLE_ID,SOURCE_TYPE) values (PRG_GRP_BUSS_TABLE_SRC_SEQ.nextval,PRG_GRP_BUSS_TABLE_SEQ.currval," + connectionId + ",PRG_DATABASE_MASTER_SEQ.currval,'Table')";
            String GDSRCDetailsQuery = "insert into PRG_GRP_BUSS_TABLE_SRC_DETAILS (BUSS_SRC_TABLE_DTL_ID,BUSS_SRC_ID,DB_TABLE_ID,BUSS_TABLE_ID,DB_COLUMN_ID,COLUMN_ALIAS,COLUMN_TYPE) values (PRG_GRP_BUSS_TAB_SRC_DTLS_SEQ.nextval,PRG_GRP_BUSS_TABLE_SRC_SEQ.currval,PRG_DATABASE_MASTER_SEQ.currval,PRG_GRP_BUSS_TABLE_SEQ.currval,PRG_DB_MASTER_TABLE_DTLS_SEQ.currval,'" + colName + "','" + colType + "')";
            String GDDetailsQuery = "insert into PRG_GRP_BUSS_TABLE_DETAILS (BUSS_COLUMN_ID,BUSS_TABLE_ID,COLUMN_NAME,DB_TABLE_ID,DB_COLUMN_ID,BUSS_SRC_TABLE_DTL_ID,IS_UNION,COLUMN_TYPE,COLUMN_DISP_NAME) values (PRG_GRP_BUSS_TABLE_DETAILS_SEQ.nextval,PRG_GRP_BUSS_TABLE_SEQ.currval,'" + colName + "',PRG_DATABASE_MASTER_SEQ.currval,PRG_DB_MASTER_TABLE_DTLS_SEQ.currval,PRG_GRP_BUSS_TAB_SRC_DTLS_SEQ.currval,'N','" + colType + "','" + colName + "')";
            String grpDimTableQuery = "insert into PRG_GRP_DIM_TABLES (DIM_TAB_ID,DIM_ID,TAB_ID) values (PRG_GRP_DIM_TABLES_SEQ.nextval," + dimId + ",PRG_GRP_BUSS_TABLE_SEQ.currval)";
            String grpDimTableDetailsQuery = "insert into PRG_GRP_DIM_TAB_DETAILS (DIM_TAB_COL_ID,DIM_TAB_ID,COL_ID,IS_AVAILABLE,IS_PK_KEY) values (PRG_GRP_DIM_TAB_DETAILS_SEQ.nextval,PRG_GRP_DIM_TABLES_SEQ.currval,PRG_GRP_BUSS_TABLE_DETAILS_SEQ.currval,'Y','N')";
            String grpDimMemberQuery = "insert into PRG_GRP_DIM_MEMBER (MEMBER_ID,MEMBER_NAME,DIM_ID,DIM_TAB_ID,MEMBER_DESC) values(PRG_GRP_DIM_MEMBER_SEQ.nextval,'" + memberName + "'," + dimId + ",PRG_GRP_DIM_TABLES_SEQ.currval,'" + memberName + "')";
            String grpDimMemDetailsQuery = "insert into PRG_GRP_DIM_MEMBER_DETAILS (MEM_DETAIL_ID,MEM_ID,COL_ID,COL_TYPE) values (PRG_GRP_DIM_MEMBER_DETAILS_SEQ.nextval,PRG_GRP_DIM_MEMBER_SEQ.currval,PRG_GRP_BUSS_TABLE_DETAILS_SEQ.currval,'KEY')";
            String grpDimMemDetailsvalQuery = "insert into PRG_GRP_DIM_MEMBER_DETAILS (MEM_DETAIL_ID,MEM_ID,COL_ID,COL_TYPE) values (PRG_GRP_DIM_MEMBER_DETAILS_SEQ.nextval,PRG_GRP_DIM_MEMBER_SEQ.currval,PRG_GRP_BUSS_TABLE_DETAILS_SEQ.currval,'VALUE')";
            String grpDimrelQuery = "insert into PRG_GRP_DIM_REL (REL_ID, DIM_ID, DESCRIPTION, REL_NAME) values(PRG_GRP_BUSS_TAB_RLT_MSTR_SEQ.nextval," + dimId + ",'" + dimensionName + "','" + dimensionName + "')";
            String grpDimrelDetailsQuery = "INSERT INTO PRG_GRP_DIM_REL_DETAILS(REL_ID, MEM_ID, REL_LEVEL)values(PRG_GRP_BUSS_TAB_RLT_MSTR_SEQ.currval,PRG_GRP_DIM_MEMBER_SEQ.currval,1)";
           
            st3.addBatch(GDBMasterQuery);
            st3.addBatch(GDSRCQuery);
            st3.addBatch(GDSRCDetailsQuery);
            st3.addBatch(GDDetailsQuery);
            st3.addBatch(grpDimTableQuery);
            st3.addBatch(grpDimTableDetailsQuery);
            st3.addBatch(grpDimMemberQuery);
            st3.addBatch(grpDimMemDetailsQuery);
            st3.addBatch(grpDimMemDetailsvalQuery);
            st3.addBatch(grpDimrelQuery);
            st3.addBatch(grpDimrelDetailsQuery);
            st3.executeBatch();
        } catch (SQLException e) {
            logger.error("Exception:", e);
        }
        if (st3 != null) {
            st3.close();
        }
        if (con != null) {
            con.close();
        }
        if (rs != null) {
            rs.close();
        }
        if (rs1 != null) {
            rs1.close();
        }
        return mapping.findForward(SUCCESS);
    }
}
