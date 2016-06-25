package prg.grp.business.tables;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import prg.business.group.MeasurePropertyBuilder;
import prg.business.group.MeasurePropertySet;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

public class GetBusinessTables extends org.apache.struts.action.Action {

    public static Logger logger = Logger.getLogger(GetBusinessTables.class);
    Connection con = null;
    //st3,rs3 relatedTables added by susheela
    Statement st, st1, st3;
    ResultSet rs, rs1, rs3;
    String table_name;
    String table_disp_name;
    String table_tooltip_name;
    String table_description;
    ArrayList columns, relatedTables;
    HashMap sourceMap = new HashMap();
    private final static String SUCCESS = "success";
    PbDb pbDb = new PbDb();

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        PbReturnObject pbReturnObject = new PbReturnObject();
        InputStream measureXmlStream = null;
        MeasurePropertySet measureProperty = null;
        try {
            //Get table_id from Right Click Menu Of List Of Tables
            //int table_id = 139;
            ArrayList sources = new ArrayList();
            int table_id = Integer.parseInt(request.getParameter("tableId"));


            //Get The master table
            String Q1 = "";
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                Q1 = "select BUSS_TABLE_NAME,BUSS_DESC,isnull(TABLE_DISP_NAME,BUSS_TABLE_NAME) TABLE_DISP_NAME,isnull(TABLE_TOOLTIP_NAME,BUSS_TABLE_NAME) TABLE_TOOLTIP_NAME from prg_grp_buss_table where buss_table_id=" + table_id;

            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                Q1 = "select BUSS_TABLE_NAME,BUSS_DESC,ifnull(TABLE_DISP_NAME,BUSS_TABLE_NAME) TABLE_DISP_NAME,ifnull(TABLE_TOOLTIP_NAME,BUSS_TABLE_NAME) TABLE_TOOLTIP_NAME from prg_grp_buss_table where buss_table_id=" + table_id;

            } else {
                Q1 = "select buss_table_name,buss_desc,nvl(table_disp_name,buss_table_name) table_disp_name,nvl(table_tooltip_name,buss_table_name) table_tooltip_name from prg_grp_buss_table where buss_table_id=" + table_id;

            }
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Q1 is-->"+Q1);

            pbReturnObject = pbDb.execSelectSQL(Q1);

            table_name = pbReturnObject.getFieldValueString(0, "BUSS_TABLE_NAME");
            table_description = pbReturnObject.getFieldValueString(0, "BUSS_DESC");
            table_disp_name = pbReturnObject.getFieldValueString(0, "TABLE_DISP_NAME");
            table_tooltip_name = pbReturnObject.getFieldValueString(0, "TABLE_TOOLTIP_NAME");

            request.setAttribute("table_name", table_name);
            request.setAttribute("table_id", Integer.valueOf(table_id));
            request.setAttribute("table_description", table_description);
            request.setAttribute("table_disp_name", table_disp_name);
            request.setAttribute("table_tooltip_name", table_tooltip_name);

            String Q2 = "select buss_table_name from prg_grp_buss_table_master_view where buss_table_id=" + table_id;
            //added by susheela start
            String tableRltQuery = "select buss_table_id,buss_table_name from prg_grp_buss_table where buss_table_id "
                    + " in(select buss_table_id2 from(select buss_table_id2 from prg_grp_buss_table_rlt_master where "
                    + " buss_table_id=" + table_id + " union select buss_table_id from prg_grp_buss_table_rlt_master"
                    + " where buss_table_id2=" + table_id + ") O1 ) and grp_id=(select grp_id from prg_grp_buss_table where buss_table_id=" + table_id + ")";
            ////.println(" tableRltQuery " + tableRltQuery);
            con = ProgenConnection.getInstance().getConnection();
            st3 = con.createStatement();
            rs3 = st3.executeQuery(tableRltQuery);
            relatedTables = new ArrayList();
            while (rs3.next()) {
                // ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("................. "+rs3.getString("buss_table_name"));
                if (!relatedTables.contains(rs3.getString("buss_table_name"))) {
                    relatedTables.add(rs3.getString("buss_table_name"));
                }
            }
            //  ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" relatedTables -.../ "+relatedTables);
            request.setAttribute("relatedTables", relatedTables);
            //added by susheela over
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Q2 is-->"+Q2);
            // st1 = con.createStatement();
            con.close();
            con = null;
            PbReturnObject rs1 = pbDb.execSelectSQL(Q2);
            for (int i = 0; i < rs1.getRowCount(); i++) {
                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("--Added Sources--"+rs1.getString(1));
                sources.add(rs1.getFieldValueString(i, 0));
            }
            sourceMap.put(table_id, sources);


            request.getSession().setAttribute("sources", sources);
            /*
             * String Q3 = "select
             * buss_column_id,column_name,column_disp_name,column_type from
             * prg_grp_buss_table_details where buss_table_id="+table_id;
             */
            // query modified by susheela start
            String Q3 = "select buss_column_id,column_name,column_disp_name,column_type,column_display_desc,default_aggregation,role_flag,COLUMN_PROPERTIES from prg_grp_buss_table_details where buss_table_id=" + table_id + " and column_type not in('TIMECALCULATED','TIMECALUCULATED','TIMESUMMARISED','TIMESUMMARIZED') order by column_name";
            // query modified by susheela over


            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Q3 is-->"+Q3);


            PbReturnObject returnObject = new PbReturnObject();
            returnObject = pbDb.execSelectSQL(Q3);
            request.setAttribute("columndetails", returnObject);


            measureXmlStream = request.getSession().getServletContext().getResourceAsStream("/WEB-INF/classes/prg/business/group/measureProperties.xml");
            MeasurePropertyBuilder measurePropertyBuilder = new MeasurePropertyBuilder();
            measureProperty = measurePropertyBuilder.buildMeasureProperties(measureXmlStream);
            request.setAttribute("measureProperty", measureProperty);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return mapping.findForward(SUCCESS);
    }
}
