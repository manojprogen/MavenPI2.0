package prg.db;

import com.progen.studio.StudioAction;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import prg.reportscheduler.LoadSchedulerJob;
import utils.db.ProgenConnection;

public class SourceConn {

    public static Logger logger = Logger.getLogger(SourceConn.class);
    Connection connection = null;
    String connDetails = null;
//    public String compId = "";
//    public String loadType = "";
//    public String userName = "";
//    public String password = "";
//    public String server = "";
//    public String serviceId = "";
//    public String port = "";
    PbDb pbdb = null;
    StudioAction sa = new StudioAction();
    LoadSchedulerJob lsj = new LoadSchedulerJob();

    public Connection getConnection(String dbType, String connName, String userName, String password, String compId, String server, String serviceId, String port, String databaseName) {

//    String password=DeEncrypter.getInstance().decrypt(password1);
        //Sealink ETL Connection Details
        if (dbType.equalsIgnoreCase("quickTravel")) {
            connDetails = "jdbc:mysql://" + server + ":" + port + "/" + databaseName + "" + userName + "" + password;
            try {
                Class.forName("com.mysql.jdbc.Driver");
//            connection = DriverManager.getConnection("jdbc:mysql://10.0.1.86:3306/sealink_production", "reports", "s2yukb42j4");
                connection = DriverManager.getConnection("jdbc:mysql://" + server + ":" + port + "/" + databaseName, userName, password);

            } catch (Exception e) {

//            lsj.sendSchedulerConnError(connDetails);
                connection = null;
                logger.error("Exception:", e);
            }
        } else if (dbType.equalsIgnoreCase("seaLinkdw")) {
            connDetails = "jdbc:mysql://localhost:3306/sealinkdw,root,report2012";
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                DriverManager.registerDriver(new net.sourceforge.jtds.jdbc.Driver());
//        connection = DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.0.126:1433/ProutilityABBAnalytical", "sa", "welcome");
//          connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sealinkdw", "root", "report2012");
                connection = DriverManager.getConnection("jdbc:mysql://bi.csi8cqnuprpf.ap-southeast-2.rds.amazonaws.com:3306/sealinkdw", "progen", "regen440");
            } catch (Exception e) {

//            lsj.sendSchedulerConnError(connDetails);
                connection = null;
                logger.error("Exception:", e);
            }
        } else if (dbType.equalsIgnoreCase("ctServer")) {
            connDetails = "jdbc:jtds:sqlserver://" + server + ":" + port + "/" + databaseName + "" + userName + "" + password;
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                DriverManager.registerDriver(new net.sourceforge.jtds.jdbc.Driver());
//            if(!connName.isEmpty() && !userName.isEmpty() && !password.isEmpty()){
//                
//                
//                String dbUrl = connName;
//                connection =DriverManager.getConnection(dbUrl, userName, password);
//            }
//            else {
//                
//                

//            String dbUrl = "jdbc:jtds:sqlserver://ipfxserver.sealink.com.au:1433/CTSERVER";
//            connection =DriverManager.getConnection(dbUrl, "report", "Sealink$2012");

//          connection = DriverManager.getConnection("jdbc:jtds:sqlserver://ipfxserver.sealink.com.au:1433/CTSERVER", "report", "Sealink$2012");
                connection = DriverManager.getConnection("jdbc:jtds:sqlserver://" + server + ":" + port + "/" + databaseName, userName, password);

//            }
            } catch (Exception e) {

//            lsj.sendSchedulerConnError(connDetails);
                connection = null;
                logger.error("Exception:", e);
            }
        } else if (dbType.equalsIgnoreCase("accpac")) {
            connDetails = "jdbc:jtds:sqlserver://" + server + ":" + port + "/" + databaseName + "" + userName + "" + password;
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                DriverManager.registerDriver(new net.sourceforge.jtds.jdbc.Driver());
//             String dbUrl = "jdbc:jtds:sqlserver://sqlserver.sealink.com.au:1433/KISEA";
//          connection =DriverManager.getConnection(dbUrl, "report", "Sealink$2012");

//          connection = DriverManager.getConnection("jdbc:jtds:sqlserver://sqlserver.sealink.com.au:1433/KISEA", "report", "Sealink$2012");
                connection = DriverManager.getConnection("jdbc:jtds:sqlserver://" + server + ":" + port + "/" + databaseName, userName, password);
            } catch (Exception e) {

//            lsj.sendSchedulerConnError(connDetails);
                connection = null;
                logger.error("Exception:", e);
            }
        } else if (dbType.equalsIgnoreCase("oracle1")) {
            connDetails = "jdbc:oracle:thin:@localhost:1521/XE,metalink1,metalink1";
            try {
                connection = ProgenConnection.getInstance().getConnection();

//        try{
//            Class.forName("oracle.jdbc.driver.OracleDriver");
//            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/XE", "metalink1", "metalink1");
////          connection = DriverManager.getConnection("jdbc:mysql://122.169.241.230:3306/quick", "root", "progen1976");
//             }
//        catch(Exception e){
//            
//            connection = null;
//            logger.error("Exception:",e);
//        }
            } catch (SQLException ex) {
//            lsj.sendSchedulerConnError(connDetails);
                logger.error("Exception:", ex);
            }
        } //used for testing of CCC
        else if (dbType.equalsIgnoreCase("cccDw")) {
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
//            connection = DriverManager.getConnection("jdbc:oracle:thin:@83.111.210.93:1521/ORCL", "metastc", "metastc");
                connection = DriverManager.getConnection("jdbc:mysql://122.169.241.230:3306/quick", "root", "progen1976");
            } catch (Exception e) {

                connection = null;
                logger.error("Exception:", e);
            }
        } else if (dbType.equalsIgnoreCase("localDb")) {
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "metalink1", "metalink1");
            } catch (Exception e) {

                connection = null;
                logger.error("Exception:", e);
            }
        } //End conn for test for CCC
        /////////////////////////////PROGEN CONNECTIONS////////////////////////////////////
        else if (dbType.equalsIgnoreCase("ctKPWBI")) {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.0.105:1433/KPWBI", "sa", "welcome");
            } catch (Exception e) {

                connection = null;
                logger.error("Exception:", e);
            }
        } else if (dbType.equalsIgnoreCase("sqlserver")) {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                DriverManager.registerDriver(new net.sourceforge.jtds.jdbc.Driver());

                if (!connName.isEmpty() && !userName.isEmpty() && !password.isEmpty()) {


                    String dbUrl = connName;
                    connection = DriverManager.getConnection(dbUrl, userName, password);
                } else {


                    String dbUrl = "jdbc:jtds:sqlserver://192.168.0.105:1433/ctserver";
                    connection = DriverManager.getConnection(dbUrl, "sa", "welcome");
                }

            } catch (Exception e) {

                connection = null;
                logger.error("Exception:", e);
            }
        } else if (dbType.equalsIgnoreCase("sqlserver_acc")) {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                DriverManager.registerDriver(new net.sourceforge.jtds.jdbc.Driver());
                String dbUrl = "jdbc:jtds:sqlserver://192.168.0.105:1433/Accpac";
                connection = DriverManager.getConnection(dbUrl, "sa", "welcome");
//            }
            } catch (Exception e) {

                connection = null;
                logger.error("Exception:", e);
            }
        } else if (dbType.equalsIgnoreCase("sqlserver1")) {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                DriverManager.registerDriver(new net.sourceforge.jtds.jdbc.Driver());
                String dbUrl = "jdbc:jtds:sqlserver://122.169.241.230:1433/ctserver";
                connection = DriverManager.getConnection(dbUrl, "sa", "progen1976");
            } catch (Exception e) {

                connection = null;
                logger.error("Exception:", e);
            }
        } else if (dbType.equalsIgnoreCase("oracle")) {
            try {

                Class.forName("oracle.jdbc.driver.OracleDriver");
                connection = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.133:1521:xe", "metaminda", "metaminda");
            } catch (Exception e) {

                connection = null;
                logger.error("Exception:", e);
            }
        } else if (dbType.equalsIgnoreCase("mySql")) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://192.168.0.105:3306/quick", "root", "welcome");
//            connection = DriverManager.getConnection("jdbc:mysql://122.169.241.230:3306/quick", "root", "progen1976");
            } catch (Exception e) {

                connection = null;
                logger.error("Exception:", e);
            }
        } //    else if(dbType.equalsIgnoreCase("mySql_sl")){
        //        try{
        //            Class.forName("com.mysql.jdbc.Driver");
        ////            connection = DriverManager.getConnection("jdbc:mysql://192.168.0.125:3306/quick", "root", "welcome");
        //            connection = DriverManager.getConnection("jdbc:mysql://122.169.241.230:3306/sealinkdw", "root", "progen1976");
        //             }
        //        catch(Exception e){
        //            logger.error("Exception:",e);
        //        }
        //    }
        else if (dbType.equalsIgnoreCase("mySql_sl")) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
//            connection = DriverManager.getConnection("jdbc:mysql://192.168.0.125:3306/quick", "root", "welcome");
                connection = DriverManager.getConnection("jdbc:mysql://192.168.0.105:3306/sealinkdw1", "root", "welcome");
            } catch (Exception e) {

                connection = null;
                logger.error("Exception:", e);
            }
        } else if (dbType.equalsIgnoreCase("Df_oracle")) {
            try {

                Class.forName("oracle.jdbc.driver.OracleDriver");
                connection = DriverManager.getConnection("jdbc:oracle:thin:@10.20.1.196:1521:dvsorcl", "ebmsro", "ebmsro");
//            

            } catch (Exception e) {

                connection = null;
                logger.error("Exception:", e);
            }
        } else if (dbType.equalsIgnoreCase("Df_mySql")) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
//            connection = DriverManager.getConnection("jdbc:mysql://192.168.0.125:3306/quick", "root", "welcome");
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/staging", "root", "India@123");
            } catch (Exception e) {

                connection = null;
                logger.error("Exception:", e);
            }
        } else if (dbType.equalsIgnoreCase("pgmas")) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
//            connection = DriverManager.getConnection("jdbc:mysql://192.168.0.125:3306/quick", "root", "welcome");
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pgmas_bi", "root", "pgmas#123");
            } catch (Exception e) {

                connection = null;
                logger.error("Exception:", e);
            }
        } else if (dbType.equalsIgnoreCase("metapgmas")) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
//            connection = DriverManager.getConnection("jdbc:mysql://192.168.0.125:3306/quick", "root", "welcome");
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/metapgmas", "root", "pgmas#123");
            } catch (Exception e) {

                connection = null;
                logger.error("Exception:", e);
            }
        } else if (dbType.equalsIgnoreCase("Mumbai_Test_Schema_183")) {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
//            connection = DriverManager.getConnection("jdbc:mysql://192.168.0.125:3306/quick", "root", "welcome");
                connection = DriverManager.getConnection("jdbc:jtds:sqlserver://183.82.3.61/Mumbai_Test_Schema", "sa", "ProGen2008");
            } catch (Exception e) {

                connection = null;
                logger.error("Exception:", e);
            }
        } else if (dbType.equalsIgnoreCase("Mumbai_Test_Schema_BI")) {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
//            connection = DriverManager.getConnection("jdbc:mysql://192.168.0.125:3306/quick", "root", "welcome");
                connection = DriverManager.getConnection("jdbc:jtds:sqlserver://localhost/Mumbai_Test_Schema", "sa", "ProGen2008");
            } catch (Exception e) {

                connection = null;
                logger.error("Exception:", e);
            }
        } else if (dbType.equalsIgnoreCase("NS5")) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
//            connection = DriverManager.getConnection("jdbc:mysql://192.168.0.125:3306/quick", "root", "welcome");
//            connection = DriverManager.getConnection("jdbc:mysql://10.0.1.86:3308/SAFENET", "ns5odbc", "88x2*S0yS7");
                connection = DriverManager.getConnection("jdbc:mysql://CSNAS-SP-ABSProGen:3308/SAFENET", "ns5odbc", "88x2*S0yS7");

            } catch (Exception e) {

                connection = null;
                logger.error("Exception:", e);
            }
        } else if (dbType.equalsIgnoreCase("ACCPAC_CAMPBELL")) {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
//            connection = DriverManager.getConnection("jdbc:mysql://192.168.0.125:3306/quick", "root", "welcome");
//            connection = DriverManager.getConnection("jdbc:jtds:sqlserver://10.0.1.94/ERP_CS_COMPANY_DATA", "erpsa", "S@g32014.)(");
//           connection = DriverManager.getConnection("jdbc:jtds:sqlserver://10.0.1.213/ERP_CS_COMPANY_DATA", "erpsa", "S@g32014.)(");
                connection = DriverManager.getConnection("jdbc:jtds:sqlserver://CSNAS-SP-PROGAC/ERP_CS_COMPANY_DATA", "erpsa", "S@g32014.)(");

            } catch (Exception e) {

                connection = null;
                logger.error("Exception:", e);
            }
        }else if (dbType.equalsIgnoreCase("VR_PROD")) {
            try {

                Class.forName("oracle.jdbc.driver.OracleDriver");
                connection = DriverManager.getConnection("jdbc:oracle:thin:@db-prd-04-clone.mem-tw.irondata.com:1521:AVPROD","bi_data","bi_data");
//            

            } catch (Exception e) {

                connection = null;
                logger.error("Exception:", e);
        }
        }else if (dbType.equalsIgnoreCase("VR_BETA")) {
            try {

                Class.forName("oracle.jdbc.driver.OracleDriver");
//                connection = DriverManager.getConnection("jdbc:oracle:thin:@//db-tst-21.mem-tw.veraction.net:1521/BI_BETA","bi_data","bi_data");
                connection = DriverManager.getConnection("jdbc:oracle:thin:@db-tst-21.mem-tw.veraction.net:1521:BIBETA","bi_data","bi_data");
//            

            } catch (Exception e) {

                connection = null;
                logger.error("Exception:", e);
            }
        }
        return connection;
    }
}
//            Class.forName("net.sourceforge.jtds.jdbc.Driver");
//            DriverManager.registerDriver(new net.sourceforge.jtds.jdbc.Driver());
//             String dbUrl = "jdbc:jtds:sqlserver://192.168.0.121:1433/ctserver";
//          connection =DriverManager.getConnection(dbUrl, "sa", "welcome");

