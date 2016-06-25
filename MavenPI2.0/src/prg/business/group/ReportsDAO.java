/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.business.group;

/**
 *
 * @author Administrator
 */
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;

public class ReportsDAO {

    public static Logger logger = Logger.getLogger(ReportsDAO.class);
//Connection con = null;
//PreparedStatement pstmt = null;
//PreparedStatement secPstmt = null;
//PreparedStatement thirdPstmt = null;
//Statement stmt = null;
//Statement stmt_1 = null;
//Statement stmt_2 = null;
//ResultSet rs = null;
//ResultSet rs_1 = null;
//ResultSet rs_2 = null;
//ProgenConnection progenConnection = null;
//public boolean istrendSupport =false;
//PbBussGrpResourceBundle resourceBundle = new PbBussGrpResourceBundle();

    public PbReturnObject roles(String roleid) {
        PbDb pbdb = new PbDb();
        PbReturnObject rolereppbro = null;
        try {
//    con = ProgenConnection.getInstance().getConnection();
            String rolerepdashs = "SELECT REPORT_ID, REPORT_NAME FROM PRG_AR_REPORT_MASTER where REPORT_ID in(SELECT REPORT_ID FROM PRG_AR_REPORT_DETAILS where folder_id=" + roleid + ")";
            rolereppbro = pbdb.execSelectSQL(rolerepdashs);

            // for(int i=0;i<rolereppbro.getRowCount();i++){
            // int repId=rolereppbro.getFieldValueInt(i,0);
            // String repName=rolereppbro.getFieldValueString(i,0);
            // reports.put(repId, repName);
            // }

        } catch (Exception e) {
////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Exception caught in ReportsDAO in roles method");
            logger.error("Exception:", e);
        }
        return rolereppbro;
    }
}
