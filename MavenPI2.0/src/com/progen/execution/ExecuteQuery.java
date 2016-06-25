package com.progen.execution;

import com.progen.userlayer.db.LogReadWriter;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.db.SourceConn;
import prg.reportscheduler.LoadSchedulerJob;

public class ExecuteQuery {

    public static Logger logger = Logger.getLogger(ExecuteQuery.class);
    LogReadWriter logrw = new LogReadWriter();
    ResultSet rs = null;
    HashMap<String, List<String>> hashTable = new HashMap();
    HashMap map = new HashMap();
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Calendar cal = Calendar.getInstance();
    //Defining start and end date of load
    public String st_date = "2006-01-01 00:00:00";
    public String lt_date1 = dateFormat.format(cal.getTime());
    public String lt_date = lt_date1.substring(0, 19);
    String option = "true";
    String Current_status = "running";
    String load = "failure";
    int colCount = 0;
    int count = 0;
    String sequence = null;
    String sourceConnectString = null;
    String sourceConnectMainString = null;
    String sourceTableName = null;
    String tragetTableName = null;
    String tragetConnectionString = null;
    String sourceSelectQuery = null;
    String tragetInsertQuery = null;
    String trunQuery = null;
    String trackLoadQuery = null;
    String RunMasterInsert = null;
    String RunSeqQueryMaster = null;
    String RunSeqQueryDetail = null;
    String RunDetailInsert = null;
    String RunSelMaster = null;
    String RunQueryUpdateMaster = null;
    String master_seq = null;
    String Detail_seq = null;
    String RunSeqTrack = null;
    String RunInsNull = null;
    String RunViewInit = null;
    String RunViewUpdate = null;
    String RunQueryUpdateDetail = null;
    String RunViewIncr = null;
    String forceInit1 = null;
    String RunChkInit = null;
    String uq = null;
    String hashQuery = null;
    String detailQuery = null;
    String delBooking = null;
    String delBookingComp = null;
    String delBookingPass = null;
    String cargoQuery = null;
    String UpdateBookingPass = null;
    String UpdateBookingComp = null;
    String InsertBookRef = null;
    String InsertBookId = null;
    String InsertTills = null;
    String UpdateTillsBooking = null;
    String UpdateTillsPass = null;
    String UpdateTillsComp = null;
    String UpdateTillsVehicle = null;
    String UpdateBookingComp1 = null;
    String UpdateBookingPass1 = null;
    String InsertPassId = null;
    String InsertTempPassName = null;
    String UpdateCompFact = null;
    String QueryFact = null;
    String connName = null;
    String loadType = null;
    String compId = null;
    String userName = null;
    String password = null;
    String server = null;
    String serviceId = null;
    String port = null;
    String databaseName = null;
    String sourceTimezone = null;
    String targetTimezone = null;
    ResultSet sourceResultSet = null;
    ResultSetMetaData sourceResultMeta = null;
    PreparedStatement pstmt = null;
    PreparedStatement pstmtForTranster;
    PreparedStatement pstmtForTruncate;
    PreparedStatement pstmtForTrackLoad;
    PreparedStatement pstmtForRequestQuery;
    PreparedStatement pstmtForRunSeq;
    PreparedStatement pstmtForMasterQuery;
    PreparedStatement pstmtForUpdateMasterQuery;
    PreparedStatement pstmtForInsNull;
    PreparedStatement pstmtForBookInsert;
    PreparedStatement pstmtForViewInit;
    PreparedStatement pstmtForHashInsert;
    PreparedStatement pstmtForUpdateDetailQuery;
    PreparedStatement pstmtForDelBooking;
    PreparedStatement pstmtForDelBookingComp;
    PreparedStatement pstmtForDelBookingPass;
    PreparedStatement pstmtForCargoQuery;
    PreparedStatement pstmtForUpdateBookingComp;
    PreparedStatement pstmtForUpdateBookingPass;
    PreparedStatement pstmtForInsertBookingRef;
    PreparedStatement pstmtForInsertBookingId;
    PreparedStatement pstmtForInsertTills;
    PreparedStatement pstmtForUpdateTillsBooking;
    PreparedStatement pstmtForUpdateTillsComp;
    PreparedStatement pstmtForUpdateTillsPass;
    PreparedStatement pstmtForUpdateTillsVehicle;
    PreparedStatement pstmtForUpdateBookingComp1;
    PreparedStatement pstmtForUpdateBookingPass1;
    PreparedStatement pstmtForInsertPassId;
    PreparedStatement pstmtForInsertTempPassName;
    PreparedStatement pstmtForUpdateCompFact;
    PreparedStatement pstmtForFact;
    PbReturnObject retObj = null;
    PbDb pbdb = new PbDb();
//defining load limit
    boolean limitMysqlRow = false;
    int startLimit = 0;
    int EndLimit = 100000;
    int loopSize = 100000;
    int totalRowInserted = 0;
    String initialLoadSequence = null;
    LoadSchedulerJob lsj = new LoadSchedulerJob();

//    public String getSourceConnectMainString() {
//        return sourceConnectMainString;
//    }
    public void setSourceConnectMainString(String sourceConnectMainString, String connName, String userName, String password, String compId, String server, String serviceId, String port, String databaseName, String sourceTimezone, String targetTimezone) {

        try {
            logrw.fileWriter("COmpany Id in setSourceConnectMainString--" + compId);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        this.sourceConnectMainString = sourceConnectMainString;
        this.connName = connName;
        this.userName = userName;
        this.password = password;
        this.compId = compId;
        this.server = server;
        this.serviceId = serviceId;
        this.port = port;
        this.databaseName = databaseName;
        this.sourceTimezone = sourceTimezone;
        this.targetTimezone = targetTimezone;
    }

//    public String getSourceConnectString() {
//        return sourceConnectString;
//    }
    public void setSourceConnectString(String sourceConnectString) {
        this.sourceConnectString = sourceConnectString;
    }

//    public String getSourceSelectQuery() {
//        return sourceSelectQuery;
//    }
    public void setSourceSelectQuery(String sourceSelectQuery) {
        this.sourceSelectQuery = sourceSelectQuery;
    }

//    public String getSourceTableName() {
//        return sourceTableName;
//    }
    public void setSourceTableName(String sourceTableName) {
        this.sourceTableName = sourceTableName;
    }

//    public String getTragetConnectionString() {
//        return tragetConnectionString;
//    }
    public void setTragetConnectionString(String tragetConnectionString) {
        //     
        this.tragetConnectionString = tragetConnectionString;

        try {
            logrw.fileWriter("tragetConnectionString-->" + tragetConnectionString);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
    }

//    public String getTragetInsertQuery() {
//        return tragetInsertQuery;
//    }
    public void setTragetInsertQuery(String tragetInsertQuery) {
        this.tragetInsertQuery = tragetInsertQuery;

    }

//    public String getTragetTableName() {
//        return tragetTableName;
//    }
    public void setTragetTableName(String tragetTableName) {
        this.tragetTableName = tragetTableName;

        try {
            logrw.fileWriter("tragetTableName-->" + tragetTableName);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
    }

//    public String getTruncateQuery() {
//        return trunQuery;
//    }
//
//    public void setTruncateQuery(String trunQuery){
//        this.trunQuery = trunQuery;
//        
//    }
//
//    public String getTrackerLoad(){
//        return trackLoadQuery;
//    }
//
//    public void setTrackerLoad(String trackLoadQuery){
//        this.trackLoadQuery = trackLoadQuery;
//        
//    }
//    public String getTrackerDate(){
//        return trackDateQuery;
//    }
//
//    public void setTrackerDate(String trackDateQuery){
//        this.trackDateQuery = trackDateQuery;
//        
//    }
//
//    public String getRunMasterInsert() {
//        return RunMasterInsert;
//    }
//
//    public void setRunMasterInsert(String RunMasterInsert) {
//        this.RunMasterInsert = RunMasterInsert;
//    }// </editor-fold>
    public void exceuteQueries(String option) throws SQLException {

        runSelectQuery(option);

        if (tragetTableName.equalsIgnoreCase("product_dimension_stg") || tragetTableName.equalsIgnoreCase("client_dimension_stg")
                || tragetTableName.equalsIgnoreCase("package_dimension_stg") || tragetTableName.equalsIgnoreCase("department_dimension_stg")
                || tragetTableName.equalsIgnoreCase("prg_user_dimension_stg") || tragetTableName.equalsIgnoreCase("vendor_dimension_stg")
                || tragetTableName.equalsIgnoreCase("prg_qt_account_dim_stg")) {

            runInsertNullRecord();
        }
//      setSourceConnectString("oracle1");
        runDetailSequence();

        runTrackLoad();
//      runUpdateDetail();
//      runUpdateMaster();
    }

    public void executeDelRows() {
        //last INCR load date
    }

    public void executeHash(String forceInit, String compId) {
        if (forceInit.equalsIgnoreCase("INCR")) {
            runHash(tragetTableName, compId);
        }
    }

    public void runHash(String tabName, String compId) {

        String st_date1 = null;
        String st_date2 = null;
        String date_diff = null;
        try {
            map = runHashTable(tabName, compId);
//            List tablenames = (List) map.get("tabnamelist");
            List<String> lastupdateddate = (List) map.get("lastupdateddate");
            List<String> factlastupdateddate = (List) map.get("factlastupdateddate");
            if (!map.isEmpty()) {

                try {
                    logrw.fileWriter("Hash map not empty");
                } catch (IOException ex) {
                    logger.error("Exception:", ex);
                }
//                for (int i = 0; i < tablenames.size(); i++) {
//                     if (tablenames.get(i).equals(tragetTableName)) {
                if (!lastupdateddate.get(0).isEmpty()) {
                    st_date1 = lastupdateddate.get(0);
                    st_date = st_date1.substring(0, 19);
                }
                if (!factlastupdateddate.get(0).isEmpty()) {
                    st_date2 = factlastupdateddate.get(0);
                    if (st_date1.compareTo(st_date2) < 0) {
                        st_date = st_date2.substring(0, 19);
                    } else {
                        st_date = st_date1.substring(0, 19);
                    }
                }
            }
//                }
//            }

        } catch (SQLException ex1) {
            logger.error("Exception:", ex1);
        }
    }

    public HashMap runHashTable(String tabName, String compId) throws SQLException {
        hashTable.clear();

        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(sourceConnectString, connName, userName, password, compId, server, serviceId, port, databaseName);

        if (con == null) {

            try {
                logrw.fileWriter(" Connection is null");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        String query = runHashQuery(tabName, compId);

        try {
            logrw.fileWriter("Hash Map Query--->" + query);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        try {
            retObj = pbdb.execSelectSQL(query, con);
            List<String> tabnamelist = new ArrayList();
            List<String> lastupdateddate = new ArrayList();
            List<String> factlastupdateddate = new ArrayList();

            for (int i = 0; i < retObj.getRowCount(); i++) {
                tabnamelist.add(retObj.getFieldValueString(i, 0).toLowerCase());
                lastupdateddate.add(retObj.getFieldValueString(i, 1).toLowerCase());
                factlastupdateddate.add(retObj.getFieldValueString(i, 2).toLowerCase());
            }
            hashTable.put("tabnamelist", tabnamelist);
            hashTable.put("lastupdateddate", lastupdateddate);
            hashTable.put("factlastupdateddate", factlastupdateddate);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        if (con != null) {
            con.close();
            con = null;
        }
        return hashTable;
    }

    public void runTruncLoad() {
        try {

            try {
                logrw.fileWriter("Executing truncate and Load");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
            setSourceTableName("reservation_groups");
            setTragetTableName("reservation_groups");
            runSelectQuery(option);
            setSourceTableName("resources");
            setTragetTableName("resources");
            runSelectQuery(option);
            setSourceTableName("resource_groups");
            setTragetTableName("resource_groups");
            runSelectQuery(option);
//            runbookingInsert();
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
    }

    public void runbookingInsert() {
        try {

            try {
                logrw.fileWriter("Executing prg_new_prd_calc truncate and Load");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
            setSourceTableName("prg_new_prd_calc");
            setTragetTableName("prg_new_prd_calc");
            runTruncateQuery();
            SourceConn sc = new SourceConn();
            Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
            if (con == null) {

                try {
                    logrw.fileWriter(" Connection is null");
                } catch (IOException ex) {
                    logger.error("Exception:", ex);
                }
            }
            getTargetQuery();

            try {
                logrw.fileWriter("Traget Insert Query--->" + this.tragetInsertQuery);
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
            pstmtForBookInsert = con.prepareStatement(tragetInsertQuery);
            pstmtForBookInsert.execute();
            if (con != null) {
                con.close();
                con = null;
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
    }

//    public void runDelBooking() {
//        try {
//            
//            SourceConn sc = new SourceConn();
//            Connection con = sc.getConnection(tragetConnectionString, connName, userName, password);
//            if (con == null) {
//                
//            }
//            getqueryDelBooking();
//            
//            pstmtForDelBooking = con.prepareStatement(delBooking);
//            pstmtForDelBooking.execute();
//            if (con != null) {
//                con.close();
//                pstmtForDelBooking = null;
//            }
//        } catch (SQLException ex) {
//            logger.error("Exception:",ex);
//        }
//    }
//    public void runDelBookingBookingComp(){
//            
//            SourceConn sc = new SourceConn();
//            Connection con = sc.getConnection(tragetConnectionString, connName, userName, password);
//            if (con == null) {
//                
//            }
//            getqueryDelBookingComp();
//            
//        try {
//            pstmtForDelBookingComp = con.prepareStatement(delBookingComp);
//            pstmtForDelBookingComp.execute();
//        } catch (SQLException ex) {
//            logger.error("Exception:",ex);
//        }
//            if (con != null) {
//            try {
//                con.close();
//            } catch (SQLException ex) {
//                logger.error("Exception:",ex);
//            }
//                pstmtForDelBookingComp = null;
//            }
//    }
//    public void runDelBookingPass(){
//        try {
//            
//            SourceConn sc = new SourceConn();
//            Connection con = sc.getConnection(tragetConnectionString, connName, userName, password);
//            if (con == null) {
//                
//            }
//            getqueryDelBookingPass();
//            
//            pstmtForDelBookingPass = con.prepareStatement(delBookingPass);
//            pstmtForDelBookingPass.execute();
//            if (con != null) {
//                con.close();
//                pstmtForDelBookingPass = null;
//            }
//        } catch (SQLException ex) {
//            logger.error("Exception:",ex);
//        }
//    }
    public void runSelectQuery(String option) throws SQLException {

//        if(option.equalsIgnoreCase("true")){
//            runTruncateQuery();
//        }

        try {
            logrw.fileWriter("Executing Select Query");
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        SourceConn sc = new SourceConn();
        //

        getSourceQuery();
        if (limitMysqlRow) {
            //
            startLimit = 0;
            EndLimit = loopSize;
            boolean getNext = true;
            while (getNext) {
                totalRowInserted = 0;

                Connection con1 = sc.getConnection(sourceConnectMainString, connName, userName, password, compId, server, serviceId, port, databaseName);
                if (con1 == null) {
                    try {
                        logrw.fileWriter("sourceConnectMainString----------->" + sourceConnectMainString);
                        logrw.fileWriter(" Connection is null.. Not Getting Connection");
                    } catch (IOException ex) {
                        logger.error("Exception:", ex);
                    }
                }
                getSourceQuery();

                try {
                    logrw.fileWriter("sourceQry--->" + this.sourceSelectQuery);
                } catch (IOException ex) {
                    logger.error("Exception:", ex);
                }
                pstmtForTranster = con1.prepareStatement(sourceSelectQuery);

                try {
                    logrw.fileWriter("pstmtForTranster-->" + pstmtForTranster);
                } catch (IOException ex) {
                    logger.error("Exception:", ex);
                }
                sourceResultSet = pstmtForTranster.executeQuery();
//                  
                sourceResultMeta = sourceResultSet.getMetaData();
//                  
                runInsertQuery();
                if (totalRowInserted >= loopSize) {
                    getNext = true;
                } else {
                    getNext = false;
                }

                if (startLimit == 0) {
                    startLimit = 1;
                }
                startLimit = startLimit + loopSize;
                EndLimit = loopSize;

                try {
                    logrw.fileWriter("Inserted in Loop");
                } catch (IOException ex) {
                    logger.error("Exception:", ex);
                }

                if (con1 != null) {
                    con1.close();
                    con1 = null;
                }
            }
        } else {

            try {
                logrw.fileWriter("Limit row is false " + limitMysqlRow);
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
            Connection con1 = sc.getConnection(sourceConnectMainString, connName, userName, password, compId, server, serviceId, port, databaseName);
            getSourceQuery();

            try {
                logrw.fileWriter("sourceQry--->" + this.sourceSelectQuery);
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

            pstmtForTranster = con1.prepareStatement(sourceSelectQuery);

            try {
                logrw.fileWriter("pstmtForTranster-->" + pstmtForTranster);
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
            sourceResultSet = pstmtForTranster.executeQuery();
            sourceResultMeta = sourceResultSet.getMetaData();
            runInsertQuery();

            try {
                logrw.fileWriter(" Inserted without Loop");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
            if (con1 != null) {
                con1.close();
                con1 = null;
                pstmtForTranster = null;
            }
        }
    }
    //added for CCC, all stg will be truncated at once

    public void runAllStgTruncate(String option, String stgTableArray[]) {
        if (option.equalsIgnoreCase("true")) {

            try {
                logrw.fileWriter("Executing Truncate Query");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
            SourceConn sc = new SourceConn();
            Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);

            try {
                logrw.fileWriter("tragetConnectionString-->" + tragetConnectionString);
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
            if (con == null) {
                try {
                    logrw.fileWriter(" Connection is null");
                } catch (IOException ex) {
                    logger.error("Exception:", ex);
                }
            }
            for (int i = 0; i < stgTableArray.length; i++) {
                tragetTableName = stgTableArray[i];
                getTrunQuery();

                try {
                    logrw.fileWriter("trun Query--->" + this.trunQuery);
                } catch (IOException ex) {
                    logger.error("Exception:", ex);
                }
                try {
                    pstmtForTruncate = con.prepareStatement(trunQuery);
                    pstmtForTruncate.execute();
                } catch (SQLException ex) {
                    logger.error("Exception:", ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                    con = null;
                } catch (SQLException ex) {
                    logger.error("Exception:", ex);
                }
                pstmtForTruncate = null;
            }
        } else {
        }

    }

    public void runTruncateQuery() {

        try {
            logrw.fileWriter("Executing Truncate Query");
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);

        try {
            logrw.fileWriter("tragetConnectionString-->" + tragetConnectionString);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        if (con == null) {
            try {
                logrw.fileWriter(" Connection is null");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        getTrunQuery();

        try {
            logrw.fileWriter("trun Query--->" + this.trunQuery);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        try {
            pstmtForTruncate = con.prepareStatement(trunQuery);
            pstmtForTruncate.execute();
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
            pstmtForTruncate = null;
        }
    }

    public void runTrackLoad() {
        runqueryRunSeqtrackLoad();

        try {
            logrw.fileWriter("Executing track load");
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(sourceConnectString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter(" Connection is null");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        getTrackLoad();

        try {
            logrw.fileWriter("trackLoadQuery--->" + this.trackLoadQuery);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        try {
            pstmtForTrackLoad = con.prepareStatement(trackLoadQuery);
            pstmtForTrackLoad.execute();
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
            pstmtForTrackLoad = null;
        }
    }

    public void runMasterSequence(String forceInit, String forceReq) {

        try {
            logrw.fileWriter("Generating master Sequence");
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(sourceConnectString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        getQueryRunSeqMaster();
        try {
            retObj = pbdb.execSelectSQL(RunSeqQueryMaster, con);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        for (int i = 0; i < retObj.getRowCount(); i++) {
            master_seq = retObj.getFieldValueString(i, 0);
        }

        try {
            logrw.fileWriter("master_seq-->" + master_seq);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        runMasterQuery(forceInit, forceReq);
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
    }

    public void runDetailSequence() {

        try {
            logrw.fileWriter("Generating detail Sequence");
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(sourceConnectString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        getQueryRunSeqDetail();

        try {
            logrw.fileWriter("RunSeqQueryDetail--->" + this.RunSeqQueryDetail);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        try {
            retObj = pbdb.execSelectSQL(RunSeqQueryDetail, con);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        for (int i = 0; i < retObj.getRowCount(); i++) {
            Detail_seq = retObj.getFieldValueString(i, 0);
        }

        try {
            logrw.fileWriter("Detail_seq-->" + Detail_seq);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        runDetailQuery();
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
    }

    public void runqueryRunSeqtrackLoad() {

        try {
            logrw.fileWriter("Generating Track Sequence");
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(sourceConnectString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        getqueryRunSeqtrackLoad();

        try {
            logrw.fileWriter("RunSeqTrack--->" + this.RunSeqTrack);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        try {
            retObj = pbdb.execSelectSQL(RunSeqTrack, con);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        for (int i = 0; i < retObj.getRowCount(); i++) {
            sequence = retObj.getFieldValueString(i, 0);
        }

        try {
            logrw.fileWriter("Track sequence-->" + sequence);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
    }

    public void runMasterQuery(String forceInit, String forceReq) {

        try {
            logrw.fileWriter("Executing Master Query");
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(sourceConnectString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        getQueryRunMaster(forceInit, forceReq);

        try {
            logrw.fileWriter("RunMasterInsert--->" + this.RunMasterInsert);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        try {
            pstmtForRequestQuery = con.prepareStatement(RunMasterInsert);
            pstmtForRequestQuery.execute();
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        this.forceInit1 = forceInit;
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
            pstmtForRequestQuery = null;
        }
    }
//    public void runUpdateDetail(){
//        
//        SourceConn sc = new SourceConn();
//        Connection con = sc.getConnection(sourceConnectString,connName,userName,password);
//        if(con==null)
//        
//        getQueryRunUpdateDetail();
//        
//
//        try {
//            pstmtForUpdateDetailQuery = con.prepareStatement(RunQueryUpdateDetail);
//            pstmtForUpdateDetailQuery.execute();
//        } catch (SQLException ex) {
//            logger.error("Exception:",ex);
//        }
//        if(con!=null){
//            try {
//                con.close();
//            } catch (SQLException ex) {
//                logger.error("Exception:",ex);
//            }
//              pstmtForUpdateDetailQuery=null;
//        }
//    }

    public void runUpdateMaster() {

        try {
            logrw.fileWriter("Updating Master Query");
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(sourceConnectString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        getQueryRunUpdateMaster();

        try {
            logrw.fileWriter("Run Query Update Master--->" + this.RunQueryUpdateMaster);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        try {
            pstmtForUpdateMasterQuery = con.prepareStatement(RunQueryUpdateMaster);
            pstmtForUpdateMasterQuery.execute();
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
            pstmtForUpdateMasterQuery = null;
        }
    }

    public void runDetailQuery() {

        try {
            logrw.fileWriter("Executing Detail Query");
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(sourceConnectString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        getQueryRunDetail();

        try {
            logrw.fileWriter("RunDetailInsert--->" + this.RunDetailInsert);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        try {
            pstmtForRequestQuery = con.prepareStatement(RunDetailInsert);
            pstmtForRequestQuery.execute();
        } catch (SQLException ex) {




            try {
                logrw.fileWriter(" ______________________________");
                logrw.fileWriter(" DETAILS NOT INSERTED ..........");
                logrw.fileWriter(" ______________________________");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
            logger.error("Exception:", ex);
        }

        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException ex) {

                logger.error("Exception:", ex);
            }
            pstmtForRequestQuery = null;
        }
    }

    public void runSelMaster(String forceInit, String forceReq, String compId) {
        this.compId = compId;

        try {
            logrw.fileWriter("Selecting Master Records");
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(sourceConnectString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {

            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        getquerySelMaster();

        try {
            logrw.fileWriter("RunSelMaster--->" + RunSelMaster);
            retObj = pbdb.execSelectSQL(RunSelMaster, con);
            count = retObj.getRowCount();
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        try {
            runqueryChkInit();
            runMasterSequence(forceInit, forceReq);
        } catch (Exception ex) {




            try {
                logrw.fileWriter("__________");
                logrw.fileWriter(" Inital Load Log Issues");
                logrw.fileWriter("__________");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }

            logger.error("Exception:", ex);
        }
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException ex) {

                try {
                    logrw.fileWriter("Connection Issue");
                } catch (IOException ex1) {
                    logger.error("Exception:", ex1);
                }
                logger.error("Exception:", ex);
            }
        }
    }

    public void runInsertNullRecord() {

        try {
            logrw.fileWriter("Inserting Null Record");
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter(" Connection is null");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        getInsertNullRecord();

        try {
            logrw.fileWriter("Null Record Insert Query--->" + this.RunInsNull);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        try {
            pstmtForInsNull = con.prepareStatement(RunInsNull);
            pstmtForInsNull.execute();
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
    }

    public void runInsertViewInitRecord() {
        try {
            runTruncateQuery();
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        try {
            logrw.fileWriter("Inserting View Init Record");
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter(" Connection is null");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        InsertViewInitRecord();

        try {
            logrw.fileWriter("Run View Init Query--->" + this.RunViewInit);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        try {
            pstmtForViewInit = con.prepareStatement(RunViewInit);
            pstmtForViewInit.execute();
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
        runDetailSequence();
//        runTrackLoad();
    }

    public void runInsertViewIncrRecord() {

        try {
            logrw.fileWriter("Run View Incr Query--->" + this.RunViewIncr);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter(" Connection is null");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            InsertViewIncrRecord();

            try {
                logrw.fileWriter("Run View Incr Query--->" + this.RunViewIncr);
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
            pstmtForViewInit = con.prepareStatement(RunViewIncr);
            pstmtForViewInit.execute();

            load = "success";
        } catch (SQLException ex) {
            load = "failure";

            try {
                logrw.fileWriter("Run View Init Query--->" + this.RunViewInit);
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }

            logger.error("Exception:", ex);
        }
        runDetailSequence();
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
//        runTrackLoad();

//        runUpdateViewIncrRecord();
    }

    public void runUpdateViewIncrRecord() {

        try {
            logrw.fileWriter("Updating View Incr Record");
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter(" Connection is null");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            UpdateViewIncrRecord();

            try {
                logrw.fileWriter("Run View Update Query--->" + this.RunViewUpdate);
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
            pstmtForViewInit = con.prepareStatement(RunViewUpdate);
            pstmtForViewInit.execute();

            load = "success";
        } catch (Exception ex) {

            try {
                logrw.fileWriter("----------Failure Updating Incr Record For------------" + tragetTableName);
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
            load = "failure";
            logger.error("Exception:", ex);
        }

        runInsertViewIncrRecord();

        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }

    }

    public void runqueryChkInit() {

        try {
            logrw.fileWriter("Checking Init Records for QuickTravel");
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(sourceConnectString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getqueryChkInit();

        try {
            logrw.fileWriter("Run Chk Init--->" + RunChkInit);
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        try {
            retObj = pbdb.execSelectSQL(RunSelMaster, con);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        colCount = retObj.getRowCount();
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
    }

    public void runCargoQuery(String ids) throws Exception {

        SourceConn sc = new SourceConn();
        Connection conn = sc.getConnection("ctKPWBI", "null", "null", "null", "null", "null", "null", "null", "null");
        if (conn == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        if (ids.equalsIgnoreCase("CargoInit")) {

            try {
                logrw.fileWriter("Cargo Init Started");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
            CallableStatement cs = conn.prepareCall("{call cargo_init_load_sp}");
            cs.execute();

            try {
                logrw.fileWriter("sucesss cargoo INIT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        } else {

            try {
                logrw.fileWriter("Cargo Incr Started");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
            CallableStatement cs = conn.prepareCall("{call cargo_incr_load_sp}");
            cs.execute();

            try {
                logrw.fileWriter("sucesss cargoo INCR");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        if (conn != null) {
            conn.close();
            conn = null;
        }
    }

//      public void runqueryUpdateBookingComp(){
//        
//        try {
//                logrw.fileWriter("Updating Booking Comp ");
//            } catch (IOException ex1) {
//                logger.error("Exception:",ex1);
//            }
//        SourceConn sc = new SourceConn();
//        Connection con = sc.getConnection(tragetConnectionString,connName,userName,password,compId,server,serviceId,port,databaseName);
//        if(con==null)
//        
//        try {
//                logrw.fileWriter("Connection is null");
//            } catch (IOException ex1) {
//                logger.error("Exception:",ex1);
//            }
//        getqueryUpdateBookingComp();
//        
//        try {
//                logrw.fileWriter("UpdateBookingComp--->"+UpdateBookingComp);
//            } catch (IOException ex1) {
//                logger.error("Exception:",ex1);
//            }
//        try {
//            pstmtForUpdateBookingComp = con.prepareStatement(UpdateBookingComp);
//            pstmtForUpdateBookingComp.execute();
//        } catch (SQLException ex) {
//            logger.error("Exception:",ex);
//        }
//        if(con!=null){
//            try {
//                con.close();
//            } catch (SQLException ex) {
//                logger.error("Exception:",ex);
//            }
//        }
//      }
//       public void runqueryUpdateBookingPass(){
//        
//        try {
//                logrw.fileWriter("Updating Booking Pass ");
//            } catch (IOException ex1) {
//                logger.error("Exception:",ex1);
//            }
//        SourceConn sc = new SourceConn();
//        Connection con = sc.getConnection(tragetConnectionString,connName,userName,password,compId,server,serviceId,port,databaseName);
//        if(con==null)
//        
//        try {
//                logrw.fileWriter("Connection is null");
//            } catch (IOException ex1) {
//                logger.error("Exception:",ex1);
//            }
//        getqueryUpdateBookingPass();
//        
//        try {
//                logrw.fileWriter("UpdateBookingPass--->"+UpdateBookingPass);
//            } catch (IOException ex1) {
//                logger.error("Exception:",ex1);
//            }
//        try {
//            pstmtForUpdateBookingPass = con.prepareStatement(UpdateBookingPass);
//            pstmtForUpdateBookingPass.execute();
//        } catch (SQLException ex) {
//            logger.error("Exception:",ex);
//        }
//        if(con!=null){
//            try {
//                con.close();
//            } catch (SQLException ex) {
//                logger.error("Exception:",ex);
//            }
//        }
//      }
    public void runqueryInsertBookId() {
        runTruncateQuery();

        try {
            logrw.fileWriter("Inserting in dim_prg_booking_id ");
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getqueryqueryInsertBookId();

        try {
            logrw.fileWriter("InsertBookId--->" + InsertBookId);
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        try {
            pstmtForInsertBookingId = con.prepareStatement(InsertBookId);
            pstmtForInsertBookingId.execute();
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
    }

    public void runqueryInsertBookRef() {
        runTruncateQuery();

        try {
            logrw.fileWriter("Inserting in dim_prg_booking_reference ");
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getqueryqueryInsertBookRef();

        try {
            logrw.fileWriter("InsertBookRef--->" + InsertBookRef);
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        try {
            pstmtForInsertBookingRef = con.prepareStatement(InsertBookRef);
            pstmtForInsertBookingRef.execute();
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
    }

    public void runqueryInsertTills() {
        runTruncateQuery();

        try {
            logrw.fileWriter("Inserting in prg_tills ");
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getrunqueryInsertTills();

        try {
            logrw.fileWriter("InsertTills--->" + InsertTills);
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        try {
            pstmtForInsertTills = con.prepareStatement(InsertTills);
            pstmtForInsertTills.execute();
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
    }

    public void runqueryUpdateTillsBooking() {

        try {
            logrw.fileWriter("updating tills for prg_booking_fact");
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getrunqueryUpdateTillsBooking();

        try {
            logrw.fileWriter("Update Tills Booking--->" + UpdateTillsBooking);
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        try {
            pstmtForUpdateTillsBooking = con.prepareStatement(UpdateTillsBooking);
            pstmtForUpdateTillsBooking.execute();
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
    }

    public void runqueryUpdateTillsComp() {

        try {
            logrw.fileWriter("updating tills for prg_booking_comp_fact");
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getrunqueryUpdateTillsComp();

        try {
            logrw.fileWriter("Update Tills Comp--->" + UpdateTillsComp);
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        try {
            pstmtForUpdateTillsComp = con.prepareStatement(UpdateTillsComp);
            pstmtForUpdateTillsComp.execute();
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
    }

    public void runqueryUpdateTillsPass() {

        try {
            logrw.fileWriter("updating tills for prg_booking_pass_fact");
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getrunqueryUpdateTillsPass();

        try {
            logrw.fileWriter("Update Tills Pass--->" + UpdateTillsPass);
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        try {
            pstmtForUpdateTillsPass = con.prepareStatement(UpdateTillsPass);
            pstmtForUpdateTillsPass.execute();
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
    }

    public void runqueryUpdateTillsVehicle() {

        try {
            logrw.fileWriter("updating tills for prg_vehicle_components_fact");
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getrunqueryUpdateTillsVehicle();

        try {
            logrw.fileWriter("Update Tills Vehicle--->" + UpdateTillsVehicle);
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        try {
            pstmtForUpdateTillsVehicle = con.prepareStatement(UpdateTillsVehicle);
            pstmtForUpdateTillsVehicle.execute();
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
    }

    public void runqueryUpdateBookingComp1() {

        try {
            logrw.fileWriter("updating tills for prg_vehicle_components_fact");
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getrunqueryUpdateBookingComp1();

        try {
            logrw.fileWriter("Update Booking Comp1--->" + UpdateBookingComp1);
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        try {
            pstmtForUpdateBookingComp1 = con.prepareStatement(UpdateBookingComp1);
            pstmtForUpdateBookingComp1.execute();
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
    }

    public void runqueryInsertPassId() {
        runTruncateQuery();

        try {
            logrw.fileWriter("Inseting in dim prg passenger ID");
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getrunqueryInsertPassId();

        try {
            logrw.fileWriter("Insert Pass Id--->" + InsertPassId);
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        try {
            pstmtForInsertPassId = con.prepareStatement(InsertPassId);
            pstmtForInsertPassId.execute();
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
    }

    public void runqueryUpdateBookingPass1() {

        try {
            logrw.fileWriter("updating tills for prg_vehicle_components_fact");
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getrunqueryUpdateBookingPass1();

        try {
            logrw.fileWriter("Update Booking Pass1--->" + UpdateBookingPass1);
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        try {
            pstmtForUpdateBookingPass1 = con.prepareStatement(UpdateBookingPass1);
            pstmtForUpdateBookingPass1.execute();
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
    }

    public void runqueryqueryInsertTempPassName() {
        runTruncateQuery();

        try {
            logrw.fileWriter("Inserting in temp pass name ");
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getrunqueryInsertTempPassName();

        try {
            logrw.fileWriter("Insert Temp Pass Name--->" + InsertTempPassName);
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        try {
            pstmtForInsertTempPassName = con.prepareStatement(InsertTempPassName);
            pstmtForInsertTempPassName.execute();
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
    }

    public void runqueryUpdateCompFact() {

        try {
            logrw.fileWriter("updating components_fact for names");
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getrunqueryUpdateCompFact();

        try {
            logrw.fileWriter("Update Comp Fact--->" + UpdateCompFact);
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        try {
            pstmtForUpdateCompFact = con.prepareStatement(UpdateCompFact);
            pstmtForUpdateCompFact.execute();
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
    }

    public void runUpdateQtRecord(String compId, String sourceTimezone, String targetTimezone) throws Exception {
        this.sourceTimezone = sourceTimezone;
        this.targetTimezone = targetTimezone;

        try {
            logrw.fileWriter("Running Update for Time Zone");
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getUpdateQtRecord1();

        try {
            logrw.fileWriter("Run Chk Init--->" + uq);
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        pstmtForViewInit = null;
        pstmtForViewInit = con.prepareStatement(uq);
        pstmtForViewInit.execute();
        getUpdateQtRecord2();

        try {
            logrw.fileWriter("Run Chk Init--->" + uq);
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        pstmtForViewInit = null;
        pstmtForViewInit = con.prepareStatement(uq);
        pstmtForViewInit.execute();
        getUpdateQtRecord3();

        try {
            logrw.fileWriter("Run Chk Init--->" + uq);
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        pstmtForViewInit = null;
        pstmtForViewInit = con.prepareStatement(uq);
        pstmtForViewInit.execute();
        getUpdateQtRecord4();

        try {
            logrw.fileWriter("Run Chk Init--->" + uq);
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        pstmtForViewInit = null;
        pstmtForViewInit = con.prepareStatement(uq);
        pstmtForViewInit.execute();
        getUpdateQtRecord5();

        try {
            logrw.fileWriter("Run Chk Init--->" + uq);
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        pstmtForViewInit = null;
        pstmtForViewInit = con.prepareStatement(uq);
        pstmtForViewInit.execute();
        getUpdateQtRecord6();

        try {
            logrw.fileWriter("Run Chk Init--->" + uq);
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        pstmtForViewInit = null;
        pstmtForViewInit = con.prepareStatement(uq);
        pstmtForViewInit.execute();
        getUpdateQtRecord7();

        try {
            logrw.fileWriter("Run Chk Init--->" + uq);
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        pstmtForViewInit = null;
        pstmtForViewInit = con.prepareStatement(uq);
        pstmtForViewInit.execute();
        getUpdateQtRecord8();

        try {
            logrw.fileWriter("Run Chk Init--->" + uq);
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        pstmtForViewInit = null;
        pstmtForViewInit = con.prepareStatement(uq);
        pstmtForViewInit.execute();
        getUpdateQtRecord9();

        try {
            logrw.fileWriter("Run Chk Init--->" + uq);
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        pstmtForViewInit = null;
        pstmtForViewInit = con.prepareStatement(uq);
        pstmtForViewInit.execute();
        getUpdateQtRecord10();

        try {
            logrw.fileWriter("Run Chk Init--->" + uq);
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        pstmtForViewInit = null;
        pstmtForViewInit = con.prepareStatement(uq);
        pstmtForViewInit.execute();
        getUpdateQtRecord11();

        try {
            logrw.fileWriter("Run Chk Init--->" + uq);
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        pstmtForViewInit = null;
        pstmtForViewInit = con.prepareStatement(uq);
        pstmtForViewInit.execute();
        getUpdateQtRecord12();

        try {
            logrw.fileWriter("Run Chk Init--->" + uq);
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        pstmtForViewInit = null;
        pstmtForViewInit = con.prepareStatement(uq);
        pstmtForViewInit.execute();
//        getUpdateQtRecord13();
//        
//        try {
//                logrw.fileWriter("Run Chk Init--->"+uq);
//            } catch (IOException ex1) {
//                logger.error("Exception:",ex1);
//            }
//        pstmtForViewInit=null;
//        pstmtForViewInit = con.prepareStatement(uq);
//        pstmtForViewInit.execute();
        getUpdateQtRecord14();

        try {
            logrw.fileWriter("Run Chk Init--->" + uq);
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        pstmtForViewInit = null;
        pstmtForViewInit = con.prepareStatement(uq);
        pstmtForViewInit.execute();
        getUpdateQtRecord15();

        try {
            logrw.fileWriter("Run Chk Init--->" + uq);
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        pstmtForViewInit = null;
        pstmtForViewInit = con.prepareStatement(uq);
        pstmtForViewInit.execute();
        getUpdateQtRecord16();

        try {
            logrw.fileWriter("Run Chk Init--->" + uq);
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        pstmtForViewInit = null;
        pstmtForViewInit = con.prepareStatement(uq);
        pstmtForViewInit.execute();
        if (con != null) {
            con.close();
            con = null;
            pstmtForViewInit = null;
        }
    }

    public void runUpdateRemCommaStg() throws Exception {

        try {
            logrw.fileWriter("Running Remove Comma From Table Columns after STG load");
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getUpdateRemCommaComp();
        pstmtForViewInit = null;
        pstmtForViewInit = con.prepareStatement(uq);

        logrw.fileWriter("uq---->" + uq);
        pstmtForViewInit.execute();
        getUpdateRemCommaBooking();
        pstmtForViewInit = null;
        pstmtForViewInit = con.prepareStatement(uq);

        logrw.fileWriter("uq---->" + uq);
        pstmtForViewInit.execute();
        getUpdateRemCommaPass();
        pstmtForViewInit = null;
        pstmtForViewInit = con.prepareStatement(uq);

        logrw.fileWriter("uq---->" + uq);
        pstmtForViewInit.execute();
        getUpdateRemCommaVehicle();
        pstmtForViewInit = null;
        pstmtForViewInit = con.prepareStatement(uq);

        logrw.fileWriter("uq---->" + uq);
        pstmtForViewInit.execute();
        if (con != null) {
            con.close();
            con = null;
            pstmtForViewInit = null;
        }
    }

    public void runUpdateRemCommaFact() throws Exception {

        try {
            logrw.fileWriter("Running Remove Comma From Table Columns after ETL load");
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getUpdateRemCommaDimCountry();
        pstmtForViewInit = null;
        pstmtForViewInit = con.prepareStatement(uq);

        logrw.fileWriter("uq---->" + uq);
        pstmtForViewInit.execute();
        getUpdateRemCommaDimClient();
        pstmtForViewInit = null;
        pstmtForViewInit = con.prepareStatement(uq);

        logrw.fileWriter("uq---->" + uq);
        pstmtForViewInit.execute();
        getUpdateRemCommaDimDept();
        pstmtForViewInit = null;
        pstmtForViewInit = con.prepareStatement(uq);

        logrw.fileWriter("uq---->" + uq);
        pstmtForViewInit.execute();
        getUpdateRemCommaDimPack();
        pstmtForViewInit = null;
        pstmtForViewInit = con.prepareStatement(uq);

        logrw.fileWriter("uq---->" + uq);
        pstmtForViewInit.execute();
        getUpdateRemCommaDimProd();
        pstmtForViewInit = null;
        pstmtForViewInit = con.prepareStatement(uq);

        logrw.fileWriter("uq---->" + uq);
        pstmtForViewInit.execute();
        getUpdateRemCommaDimVendor();
        pstmtForViewInit = null;
        pstmtForViewInit = con.prepareStatement(uq);

        logrw.fileWriter("uq---->" + uq);
        pstmtForViewInit.execute();
        if (con != null) {
            con.close();
            con = null;
            pstmtForViewInit = null;
        }
    }

    public void runTrunInsrtUpdateRev() throws Exception {

        try {
            logrw.fileWriter("Running Truncate and Insert for dim_prg_reservation_id");
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getrunqueryTruncRev();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(QueryFact);

        logrw.fileWriter("QueryFact---->" + QueryFact);
        pstmtForFact.execute();
        getrunqueryInsertRev();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(QueryFact);

        logrw.fileWriter("QueryFact---->" + QueryFact);
        pstmtForFact.execute();

        if (con != null) {
            con.close();
            con = null;
            pstmtForFact = null;
        }
    }

    public void runUpdateTables() throws Exception {
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getrunqueryUpdateBookingsState();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(QueryFact);

        logrw.fileWriter("QueryFact---->" + QueryFact);
        pstmtForFact.execute();
        getrunqueryUpdateCompState();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(QueryFact);

        logrw.fileWriter("QueryFact---->" + QueryFact);
        pstmtForFact.execute();
        getrunqueryUpdatePassState();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(QueryFact);

        logrw.fileWriter("QueryFact---->" + QueryFact);
        pstmtForFact.execute();
//        getrunqueryDeleteForPassConsSplit();
//        pstmtForFact=null;
//        pstmtForFact = con.prepareStatement(QueryFact);
//        pstmtForFact.execute();
        getrunqueryUpdateVehicleState();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(QueryFact);

        logrw.fileWriter("QueryFact---->" + QueryFact);
        pstmtForFact.execute();
        getrunqueryDeleteAccountingRows();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(QueryFact);

        pstmtForFact.execute();
        getrunqueryDeleteServicesRows();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(QueryFact);

        logrw.fileWriter("QueryFact---->" + QueryFact);
        pstmtForFact.execute();
        getrunqueryDeleteAdjustmentsRows1();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(QueryFact);

        logrw.fileWriter("QueryFact---->" + QueryFact);
        pstmtForFact.execute();
        getrunqueryDeleteAdjustmentsRows2();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(QueryFact);

        logrw.fileWriter("QueryFact---->" + QueryFact);
        pstmtForFact.execute();

        if (con != null) {
            con.close();
            con = null;
            pstmtForFact = null;
        }
    }

    public void runInsertUpdatePax() throws Exception {
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        setTragetTableName("prg_comp_pax_type");
        runTruncateQuery();
        getrunqueryInsertCompPax();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(QueryFact);

        logrw.fileWriter("QueryFact---->" + QueryFact);
        pstmtForFact.execute();
        setTragetTableName("prg_booking_pax_type");
        runTruncateQuery();
        getrunqueryInsertBookPax();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(QueryFact);

        logrw.fileWriter("QueryFact---->" + QueryFact);
        pstmtForFact.execute();
        getrunqueryUpdateBookCompPax1();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(QueryFact);

        logrw.fileWriter("QueryFact---->" + QueryFact);
        pstmtForFact.execute();
        getrunqueryUpdateBookCompPax2();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(QueryFact);

        logrw.fileWriter("QueryFact---->" + QueryFact);
        pstmtForFact.execute();
        getrunqueryUpdateBookCompPax3();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(QueryFact);

        logrw.fileWriter("QueryFact---->" + QueryFact);
        pstmtForFact.execute();
        getrunqueryUpdateBookCompPax4();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(QueryFact);

        logrw.fileWriter("QueryFact---->" + QueryFact);
        pstmtForFact.execute();
        getrunqueryUpdateBookCompPax5();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(QueryFact);

        logrw.fileWriter("QueryFact---->" + QueryFact);
        pstmtForFact.execute();

        if (con != null) {
            con.close();
            con = null;
            pstmtForFact = null;
        }
    }

    public void runTrunInsertConSplit() throws Exception {
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getrunqueryTruncConSplit();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(QueryFact);

        logrw.fileWriter("QueryFact---->" + QueryFact);
        pstmtForFact.execute();
        getrunqueryInsertConSplit();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(QueryFact);

        logrw.fileWriter("QueryFact---->" + QueryFact);
        pstmtForFact.execute();

        if (con != null) {
            con.close();
            con = null;
            pstmtForFact = null;
        }
    }

    public void runTrunInsertUpdateVeh() throws Exception {
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getrunqueryTruncVeh();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(QueryFact);

        logrw.fileWriter("QueryFact---->" + QueryFact);
        pstmtForFact.execute();
        getrunqueryInsertVeh();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(QueryFact);

        logrw.fileWriter("QueryFact---->" + QueryFact);
        pstmtForFact.execute();
        getrunqueryUpdateVeh();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(QueryFact);

        logrw.fileWriter("QueryFact---->" + QueryFact);
        pstmtForFact.execute();

        if (con != null) {
            con.close();
            con = null;
            pstmtForFact = null;
        }
    }

    public void runTrunInsrtRefCode() throws Exception {

        try {
            logrw.fileWriter("Running Truncate and Insert for prg_referral_code_dim");
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getrunqueryTruncRefCode();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(QueryFact);

        logrw.fileWriter("QueryFact---->" + QueryFact);
        pstmtForFact.execute();
        getrunqueryInsertRefCode();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(QueryFact);

        logrw.fileWriter("QueryFact---->" + QueryFact);
        pstmtForFact.execute();

        if (con != null) {
            con.close();
            con = null;
            pstmtForFact = null;
        }
    }

    public void runTrunInsrtUpdateFact() throws Exception {

        try {
            logrw.fileWriter("Running Truncate, Insert And Update For Fact");
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getrunqueryTruncTemp();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(QueryFact);

        logrw.fileWriter("runqueryTruncTemp---->" + QueryFact);
        pstmtForFact.execute();
        getrunqueryInsertTemp();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(QueryFact);

        logrw.fileWriter("runqueryInsertTemp---->" + QueryFact);
        pstmtForFact.execute();
        getrunqueryUpdateTemp();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(QueryFact);

        logrw.fileWriter("runqueryUpdateTemp---->" + QueryFact);
        pstmtForFact.execute();
        if (con != null) {
            con.close();
            con = null;
            pstmtForFact = null;
        }
    }

    public void runInsertCallsDim() throws Exception {
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getqueryInsertCallsDim1();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryInsertCallsDim1---->" + uq);
        pstmtForFact.execute();
        getqueryInsertCallsDim2();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryInsertCallsDim2---->" + uq);
        pstmtForFact.execute();
        getqueryInsertCallsDim3();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryInsertCallsDim3---->" + uq);
        pstmtForFact.execute();

        if (con != null) {
            con.close();
            con = null;
            pstmtForFact = null;
        }
    }

    public void runInsertCallsFact() throws Exception {
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getqueryInsertCallsFact1();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryInsertCallsFact1---->" + uq);
        pstmtForFact.execute();
        getqueryInsertCallsFact2();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryInsertCallsFact2---->" + uq);
        pstmtForFact.execute();
        getqueryInsertCallsFact3();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryInsertCallsFact3---->" + uq);
        pstmtForFact.execute();

        if (con != null) {
            con.close();
            con = null;
            pstmtForFact = null;
        }
    }

    public void runTrunInsertUpdatePaxFName() throws Exception {
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getqueryTruncPaxFName();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryTruncPaxFName---->" + uq);
        pstmtForFact.execute();
        getqueryInsertPaxFName();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryInsertPaxFName---->" + uq);
        pstmtForFact.execute();
        getqueryUpdatePaxFName();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryUpdatePaxFName---->" + uq);
        pstmtForFact.execute();

        if (con != null) {
            con.close();
            con = null;
            pstmtForFact = null;
        }
    }

    public void runInsertClientType() throws Exception {
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getqueryClientType1();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryClientType1---->" + uq);
        pstmtForFact.execute();
        getqueryClientType2();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryClientType2---->" + uq);
        pstmtForFact.execute();
        getqueryClientType3();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryClientType3---->" + uq);
        pstmtForFact.execute();
        if (con != null) {
            con.close();
            con = null;
            pstmtForFact = null;
        }
    }

    public void runUpdateFacts() throws Exception {
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getqueryUpdateAccFacts();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryUpdateAccFacts---->" + uq);
        pstmtForFact.execute();
        getqueryUpdatePaymentsFacts();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryUpdatePaymentsFacts---->" + uq);
        pstmtForFact.execute();
//        getqueryUpdateBookingFacts();
//        pstmtForFact=null;
//        pstmtForFact = con.prepareStatement(uq);
//        
//        pstmtForFact.execute();
//        getqueryUpdateBookingPassFacts();
//        pstmtForFact=null;
//        pstmtForFact = con.prepareStatement(uq);
//        
//        pstmtForFact.execute();
        if (con != null) {
            con.close();
            con = null;
            pstmtForFact = null;
        }
    }

    public void runUpdateClient() throws Exception {
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getqueryUpdateClient1();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryUpdateClient1---->" + uq);
        pstmtForFact.execute();
        getqueryUpdateClient2();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryUpdateClient2---->" + uq);
        pstmtForFact.execute();
        getqueryUpdateClient3();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryUpdateClient3---->" + uq);
        pstmtForFact.execute();
        if (con != null) {
            con.close();
            con = null;
            pstmtForFact = null;
        }
    }

    public void runQueryMostValProdPack() throws Exception {
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getqueryTruncBookValPackTmp1();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryTruncBookValPackTmp1---->" + uq);
        pstmtForFact.execute();
        getqueryInsertBookValPackTmp1();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryInsertBookValPackTmp1---->" + uq);
        pstmtForFact.execute();
        getqueryTruncBookValPackTmp3();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryTruncBookValPackTmp3---->" + uq);
        pstmtForFact.execute();
        getqueryInsertBookValPackTmp3();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryInsertBookValPackTmp3---->" + uq);
        pstmtForFact.execute();
        getqueryTruncBookValPackTmp2();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryTruncBookValPackTmp2---->" + uq);
        pstmtForFact.execute();
        getqueryInsertBookValPackTmp2();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryInsertBookValPackTmp2---->" + uq);
        pstmtForFact.execute();
        getqueryUpdateBookingValPackId();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryUpdateBookingValPackId---->" + uq);
        pstmtForFact.execute();
        getqueryUpdateBookingValPackName();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryUpdateBookingValPackName---->" + uq);
        pstmtForFact.execute();
        getqueryTruncBookValProdTmp1();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryTruncBookValProdTmp1---->" + uq);
        pstmtForFact.execute();
        getqueryInsertBookValProdTmp1();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryInsertBookValProdTmp1---->" + uq);
        pstmtForFact.execute();
        getqueryTruncBookValProdTmp3();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryTruncBookValProdTmp3---->" + uq);
        pstmtForFact.execute();
        getqueryInsertBookValProdTmp3();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryInsertBookValProdTmp3---->" + uq);
        pstmtForFact.execute();
        getqueryTruncBookValProdTmp2();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryTruncBookValProdTmp2---->" + uq);
        pstmtForFact.execute();
        getqueryInsertBookValProdTmp2();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryInsertBookValProdTmp2---->" + uq);
        pstmtForFact.execute();
        getqueryUpdateBookingValProdId();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryUpdateBookingValProdId---->" + uq);
        pstmtForFact.execute();
        getqueryUpdateBookingValProdName();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryUpdateBookingValProdName---->" + uq);
        pstmtForFact.execute();
        if (con != null) {
            con.close();
            con = null;
            pstmtForFact = null;
        }
    }

    public void runQueryPaymentsDetails() throws Exception {
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getqueryTruncLastPayment();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("queryTruncLastPayment---->" + uq);
        pstmtForFact.execute();
        getqueryInsertLastPayment();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("queryInsertLastPayment---->" + uq);
        pstmtForFact.execute();
        getqueryTruncLastPaymentDetails();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("queryTruncLastPaymentDetails---->" + uq);
        pstmtForFact.execute();
        getqueryInsertLastPaymentDetails();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("queryInsertLastPaymentDetails---->" + uq);
        pstmtForFact.execute();
        if (con != null) {
            con.close();
            con = null;
            pstmtForFact = null;
        }
    }

    public void runqueryUpdateBookingPaymentsInfo() throws Exception {
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getqueryUpdateBookingPaymentsInfo();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("QryUpdateBookingForLastPaymentDetails---->" + uq);
        pstmtForFact.execute();
        if (con != null) {
            con.close();
            con = null;
            pstmtForFact = null;
        }
    }

    public void runqueryUpdateBookingCompPass() throws Exception {
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getqueryUpdateBookingCompfactFromBooking();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("queryUpdateBookingCompfactFromBooking---->" + uq);
        pstmtForFact.execute();
        getqueryUpdateBookingPassFromBooking();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("queryUpdateBookingPassFromBooking---->" + uq);
        pstmtForFact.execute();
        getqueryUpdateBookingPassFromBookingComp();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("queryUpdateBookingPassFromBookingComp---->" + uq);
        pstmtForFact.execute();
        if (con != null) {
            con.close();
            con = null;
            pstmtForFact = null;
        }
    }

    public void runqueryUpdatesCreatorUpdatorIdName() throws Exception {
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getqueryUpdateBookingStgWithCreatorName();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("queryUpdateBookingStgWithCreatorName---->" + uq);
        pstmtForFact.execute();
        getqueryUpdateBookingStgWithUpdatorName();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("queryUpdateBookingStgWithUpdatorName---->" + uq);
        pstmtForFact.execute();
        getqueryUpdatePaymentsStgWithUpdatorName();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryUpdatePaymentsStgWithUpdatorName---->" + uq);
        pstmtForFact.execute();
        if (con != null) {
            con.close();
            con = null;
            pstmtForFact = null;
        }
    }

    public void runqueryUpdatesBookingCompForAdjustments() throws Exception {
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getqueryTruncTempAdjustmentsCompTable();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryTruncTempAdjustmentsCompTable---->" + uq);
        pstmtForFact.execute();
        getqueryInsertTempAdjustmentsCompNames();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryInsertTempAdjustmentsCompNames---->" + uq);
        pstmtForFact.execute();
        getqueryUpdateAdjustmentsAtCompLevel();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryUpdateAdjustmentsAtCompLevel---->" + uq);
        pstmtForFact.execute();
        getqueryTruncTempAdjustmentsBookTable();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryTruncTempAdjustmentsBookTable---->" + uq);
        pstmtForFact.execute();
        getqueryInsertTempAdjustmentsBookNames();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryInsertTempAdjustmentsBookNames---->" + uq);
        pstmtForFact.execute();
        getqueryUpdateAdjustmentsAtBookingLevel();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryUpdateAdjustmentsAtBookingLevel---->" + uq);
        getqueryTruncTempAdjustmentsCompBookTable();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryTruncTempAdjustmentsCompBookTable---->" + uq);
        pstmtForFact.execute();
        getqueryInsertTempAdjustmentsCompBookNames();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryInsertTempAdjustmentsCompBookNames---->" + uq);
        pstmtForFact.execute();
        getqueryUpdateAdjustmentsAtCompBookLevel();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryUpdateAdjustmentsAtCompBookLevel---->" + uq);
        pstmtForFact.execute();
        if (con != null) {
            con.close();
            con = null;
            pstmtForFact = null;
        }
    }

    public void runUpdatebookingComments() throws Exception {
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getqueryUpdatebookingComments1();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("ueryUpdatebookingComments1---->" + uq);
        pstmtForFact.execute();
        getqueryUpdatebookingComments2();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("ueryUpdatebookingComments2---->" + uq);
        pstmtForFact.execute();
        getqueryUpdateTrafficLevel1();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("queryUpdateTrafficLevel1---->" + uq);
        pstmtForFact.execute();
        getqueryUpdateTrafficLevel2();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("queryUpdateTrafficLevel2---->" + uq);
        pstmtForFact.execute();
        //start of code  on 29Jan14
        getqueryUpdateCountryInBooking();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("queryUpdateCountryInBooking---->" + uq);
        pstmtForFact.execute();
        getqueryUpdateCountryInComp();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("queryUpdateCountryInComp---->" + uq);
        pstmtForFact.execute();
        getqueryUpdateCountryInPax();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("queryUpdateCountryInPax---->" + uq);
        pstmtForFact.execute();
        //end of code  on 29Jan14
//        getqueryUpdateBookingCompfact();
//        pstmtForFact=null;
//        pstmtForFact = con.prepareStatement(uq);
//        
//        pstmtForFact.execute();
        if (con != null) {
            con.close();
            con = null;
            pstmtForFact = null;
        }
    }

    public void runUpdateChildAdultInfo() throws Exception {
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getqueryUpdateAdultChBooking();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryUpdateAdultChBooking---->" + uq);
        pstmtForFact.execute();
        getqueryUpdateAdultChComp();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryUpdateAdultChComp---->" + uq);
        pstmtForFact.execute();
        if (con != null) {
            con.close();
            con = null;
            pstmtForFact = null;
        }
    }

    public void runPaymentUpdateForBookings() throws Exception {
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getqueryTruncFirstPaymentStg();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("queryTruncFirstPaymentStg---->" + uq);
        pstmtForFact.execute();
        getqueryInsertFirstPaymentStg();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("queryInsertFirstPaymentStg---->" + uq);
        pstmtForFact.execute();
        getqueryUpdateBookingWithPayment();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("queryUpdateBookingWithPayment---->" + uq);
        pstmtForFact.execute();
//        getqueryUpdateBookingWithPayment1();
//        pstmtForFact=null;
//        pstmtForFact = con.prepareStatement(uq);
//        
//        pstmtForFact.execute();
        if (con != null) {
            con.close();
            con = null;
            pstmtForFact = null;
        }
    }

    public void runUpdatePaymentsFactFromBookingStg() throws Exception {
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getqueryUpdatePaymentsFactFromBookingStg();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("queryUpdatePaymentsFactFromBookingStg---->" + uq);
        pstmtForFact.execute();
        if (con != null) {
            con.close();
            con = null;
            pstmtForFact = null;
        }
    }

    public void runUpdateTimeZonePaymentsStg() throws Exception {
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getqueryUpdateTimeZonePaymentsStg();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryUpdateTimeZonePaymentsStg---->" + uq);
        pstmtForFact.execute();
        if (con != null) {
            con.close();
            con = null;
            pstmtForFact = null;
        }
    }

    public void runPromoCodeUpdatesForBookings() throws Exception {
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getqueryTruncateBookingfromPromoCode();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("queryTruncateBookingfromPromoCode---->" + uq);
        pstmtForFact.execute();
        getqueryInsertBookingfromPromoCode();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryInsertBookingfromPromoCode---->" + uq);
        pstmtForFact.execute();
        getqueryTruncateTempBookingPromoCode();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("queryTruncateTempBookingPromoCode---->" + uq);
        pstmtForFact.execute();
        getqueryInsertTempBookingPromoCode();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("queryUpdatePromoCodeInBooking---->" + uq);
        pstmtForFact.execute();
        getqueryUpdatePromoCodeInBooking();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("queryInsertTempBookingPromoCode---->" + uq);
        pstmtForFact.execute();
        if (con != null) {
            con.close();
            con = null;
            pstmtForFact = null;
        }
    }

    public void runUpdatePackageProductInResService() throws Exception {
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getqueryUpdatePackageProductInResService();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryUpdatePackageProductInResService---->" + uq);
        pstmtForFact.execute();

        if (con != null) {
            con.close();
            con = null;
            pstmtForFact = null;
        }
    }

    public void runUpdateQRuleCountInComp() throws Exception {
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getqueryTruncQRuleCompCountTemp();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryTruncQRuleCompCountTemp---->" + uq);
        pstmtForFact.execute();
        getqueryInsertQRuleCompCountTemp();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryInsertQRuleCompCountTemp---->" + uq);
        pstmtForFact.execute();
        getqueryUpdateQRuleCountInComp();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryUpdateQRuleCountInComp---->" + uq);
        pstmtForFact.execute();
        getqueryUpdateQRuleCountInQRule();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("queryUpdateQRuleCountInQRule---->" + uq);
        pstmtForFact.execute();
        getqueryTruncQRuleCompCountTemp();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("getqueryTruncQRuleCompCountTemp---->" + uq);
        pstmtForFact.execute();
        getqueryInsertQRuleCompCount1Temp();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("queryInsertQRuleCompCount1Temp---->" + uq);
        pstmtForFact.execute();
        getqueryUpdateAllocatedBookingInQRule();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("queryUpdateAllocatedBookingInQRule---->" + uq);
        pstmtForFact.execute();
        getqueryUpdateCustumNumberInQRule();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("queryUpdateCustumNumberInQRule---->" + uq);
        pstmtForFact.execute();

        if (con != null) {
            con.close();
            con = null;
            pstmtForFact = null;
        }
    }

    public void runUpdatePropertyIdInComp() throws Exception {
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getqueryUpdatePropertyIdInComp();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("queryUpdatePropertyIdInComp---->" + uq);
        pstmtForFact.execute();

        if (con != null) {
            con.close();
            con = null;
            pstmtForFact = null;
        }
    }

    public void runUpdateUpdateVehicleLengthInComp() throws Exception {
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        getqueryTruncVehicleLength();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("queryTruncVehicleLength---->" + uq);
        pstmtForFact.execute();
        getqueryInsertVehicleLength();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("queryInsertVehicleLength---->" + uq);
        pstmtForFact.execute();
        getqueryUpdateVehicleLengthInComp();
        pstmtForFact = null;
        pstmtForFact = con.prepareStatement(uq);

        logrw.fileWriter("queryUpdateVehicleLengthInComp---->" + uq);
        pstmtForFact.execute();

        if (con != null) {
            con.close();
            con = null;
            pstmtForFact = null;
        }
    }

    public void runTrackLoadIncr() {
        runqueryRunSeqtrackLoad();

        try {
            logrw.fileWriter("Executing track load after INCR");
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(sourceConnectString, connName, userName, password, compId, server, serviceId, port, databaseName);
        if (con == null) {
            try {
                logrw.fileWriter("Connection is null");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            getTrackLoadIncr();

            try {
                logrw.fileWriter("trackLoadQuery--->" + this.trackLoadQuery);
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
            pstmtForTrackLoad = con.prepareStatement(trackLoadQuery);
            pstmtForTrackLoad.execute();
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
            pstmtForTrackLoad = null;
        }
    }

    public void runInsertQuery() throws SQLException {

        try {
            logrw.fileWriter("Executing Insert Query");
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        SourceConn sc = new SourceConn();
        Connection con = sc.getConnection(tragetConnectionString, connName, userName, password, compId, server, serviceId, port, databaseName);
        PreparedStatement pstmtForTranster = null;
        int i = 0;
        int rowsInst = 0;
        try {
            getTargetQuery();


            try {
                logrw.fileWriter("Insert Query--->" + this.tragetInsertQuery);
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
            pstmtForTranster = con.prepareStatement(tragetInsertQuery);
            while (sourceResultSet.next()) {
                try {

                    for (int j = 1; j <= sourceResultMeta.getColumnCount(); j++) {
                        //  
//                
                        if (sourceResultMeta.getColumnClassName(j).equalsIgnoreCase("INTEGER") || sourceResultMeta.getColumnClassName(j).equalsIgnoreCase("INT") || sourceResultMeta.getColumnClassName(j).equalsIgnoreCase("BIGINT") || sourceResultMeta.getColumnClassName(j).equalsIgnoreCase("smallint")) {
                            pstmtForTranster.setInt(j, sourceResultSet.getInt(j));
//                         Integer i=(Integer)rs.getObject(1);
//                             count=i.intValue();
                        } else if (sourceResultMeta.getColumnType(j) == Types.VARCHAR) {
                            pstmtForTranster.setString(j, sourceResultSet.getString(j));
                        } else if (sourceResultMeta.getColumnType(j) == Types.DATE) {
                            pstmtForTranster.setDate(j, sourceResultSet.getDate(j));
                        } else if (sourceResultMeta.getColumnType(j) == Types.TIME) {
                            pstmtForTranster.setTime(j, sourceResultSet.getTime(j));
                        } else if (sourceResultMeta.getColumnType(j) == Types.DECIMAL || sourceResultMeta.getColumnType(j) == Types.FLOAT || sourceResultMeta.getColumnType(j) == Types.DOUBLE) {
                            pstmtForTranster.setDouble(j, sourceResultSet.getDouble(j));
                        } else if (sourceResultMeta.getColumnType(j) == Types.INTEGER) {
                            pstmtForTranster.setInt(j, sourceResultSet.getInt(j));
                        } else {
                            pstmtForTranster.setString(j, sourceResultSet.getString(j));
                        }
                    }
                    //
                    pstmtForTranster.addBatch();
                    i++;
                    rowsInst++;
                    totalRowInserted++;
                    if (i == 5000) {
                        //
                        pstmtForTranster.executeBatch();
                        i = 0;
                        //
                    }
                    load = "success";
//                            pstmt.executeBatch();
//                     
                } catch (SQLException e) {

                    lsj.sendSchedulerConnError(this.tragetInsertQuery);

                    try {
                        logrw.fileWriter(" ERRORRRRRRRRR>>>>>>>>>>>>>>>>>>>");
                        logrw.fileWriter(" \"rowsInst \" + rowsInst");
                        lsj.sendSchedulerConnError(this.tragetInsertQuery);
                    } catch (IOException ex1) {
                        logger.error("Exception:", ex1);
                    }
                    logger.error("Exception:", e);
                    load = "failure";
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

        try {
            logrw.fileWriter("rowsInst" + rowsInst);
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        if (i < 5000) {

            try {
                logrw.fileWriter("Executing i " + i);
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
            try {
                pstmtForTranster.executeBatch();

                try {
                    logrw.fileWriter(" Remaining Batch ");
                } catch (IOException ex1) {
                    logger.error("Exception:", ex1);
                }
                load = "success";
                pstmtForTranster = null;
            } catch (Exception ex) {
                lsj.sendSchedulerConnError(this.tragetInsertQuery);

                try {
                    logrw.fileWriter("ERRRRRRRRRROoooooooooooooooooooor");
                } catch (IOException ex1) {
                    lsj.sendSchedulerConnError(this.tragetInsertQuery);
                    logger.error("Exception:", ex1);
                }
                load = "failure";
                logger.error("Exception:", ex);
            }
            i = 0;



            try {
                logrw.fileWriter("No Error");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        if (con != null) {
            con.close();
            con = null;
        }
        if (pstmtForTranster != null) {
            pstmtForTranster.close();
        }
    }

    public void getSourceQuery() {
        SelectQuery sq = new SelectQuery();
        sourceSelectQuery = sq.selecttabname(sourceTableName, st_date, lt_date, compId);
        limitMysqlRow = sq.limitMysqlRow;

        try {
            logrw.fileWriter(" limitMysqlRow " + limitMysqlRow);
        } catch (IOException ex1) {
            logger.error("Exception:", ex1);
        }
        if (limitMysqlRow == true) {
            sourceSelectQuery = sourceSelectQuery + " limit " + startLimit + " , " + EndLimit + " ";
        }
    }

    public void getTargetQuery() {
        InsertQuery iq = new InsertQuery();
        tragetInsertQuery = iq.Inserttabname(tragetTableName);
    }

    public void getTrunQuery() {
        TruncateQuery tq = new TruncateQuery();
        trunQuery = tq.truncateTabname(tragetTableName);
    }

    public void getTrackLoad() {
        RequestQuery tl = new RequestQuery();
        trackLoadQuery = tl.trackerLoad(st_date, lt_date, this.forceInit1, tragetTableName, load, sequence, compId);
    }

    public void getQueryRunMaster(String forceInit, String forceReq) {
        RequestQuery rq = new RequestQuery();
        RunMasterInsert = rq.queryRunMasterInsert(st_date, lt_date, count, colCount, forceReq, master_seq, Current_status, forceInit, compId);
    }

    public void getQueryRunSeqMaster() {
        RequestQuery rq1 = new RequestQuery();
        RunSeqQueryMaster = rq1.queryRunSeqMaster();
    }

    public void getQueryRunSeqDetail() {
        RequestQuery rq1 = new RequestQuery();
        RunSeqQueryDetail = rq1.queryRunSeqDetail();
    }

    public void getQueryRunUpdateDetail() {
        RequestQuery um = new RequestQuery();
        RunQueryUpdateDetail = um.queryUpdateDetail(Detail_seq, load);
    }

    public void getQueryRunUpdateMaster() {
        RequestQuery um = new RequestQuery();
        RunQueryUpdateMaster = um.queryUpdateMaster(master_seq, load);
    }

    public void getQueryRunDetail() {
        RequestQuery rq1 = new RequestQuery();
        RunDetailInsert = rq1.queryRunDetailsInsert(st_date, lt_date, count, this.forceInit1, master_seq, Detail_seq, tragetTableName, Current_status, load, compId);
    }

    public void getquerySelMaster() {
        RequestQuery sm = new RequestQuery();
        RunSelMaster = sm.querySelMaster(compId);
    }

    public void getqueryRunSeqtrackLoad() {
        RequestQuery sq = new RequestQuery();
        RunSeqTrack = sq.queryRunSeqtrackLoad();
    }

    public void getInsertNullRecord() {
        InsertQuery iq = new InsertQuery();
        RunInsNull = iq.InsertNullRecord(tragetTableName, compId);
    }

    public void InsertViewInitRecord() {
        InsertQuery iq = new InsertQuery();
        RunViewInit = iq.InsertViewInitRecord(tragetTableName);
    }

    public void InsertViewIncrRecord() {
        InsertQuery iq = new InsertQuery();
        RunViewIncr = iq.InsertViewIncrRecord(tragetTableName);
    }

    public void UpdateViewIncrRecord() {
        InsertQuery iq = new InsertQuery();
        RunViewUpdate = iq.UpdateViewIncrRecord(tragetTableName);
    }

    public void getqueryChkInit() {
        RequestQuery sq = new RequestQuery();
        RunChkInit = sq.queryChkInit(compId);
    }

    public void getUpdateQtRecord1() {
        InsertQuery sq = new InsertQuery();
        uq = sq.UpdateQtRecord1(sourceTimezone, targetTimezone, compId);
    }

    public void getUpdateQtRecord2() {
        InsertQuery sq = new InsertQuery();
        uq = sq.UpdateQtRecord2(sourceTimezone, targetTimezone, compId);
    }

    public void getUpdateQtRecord3() {
        InsertQuery sq = new InsertQuery();
        uq = sq.UpdateQtRecord3(sourceTimezone, targetTimezone, compId);
    }

    public void getUpdateQtRecord4() {
        InsertQuery sq = new InsertQuery();
        uq = sq.UpdateQtRecord4(sourceTimezone, targetTimezone, compId);
    }

    public void getUpdateQtRecord5() {
        InsertQuery sq = new InsertQuery();
        uq = sq.UpdateQtRecord5(sourceTimezone, targetTimezone, compId);
    }

    public void getUpdateQtRecord6() {
        InsertQuery sq = new InsertQuery();
        uq = sq.UpdateQtRecord6(sourceTimezone, targetTimezone, compId);
    }

    public void getUpdateQtRecord7() {
        InsertQuery sq = new InsertQuery();
        uq = sq.UpdateQtRecord7(sourceTimezone, targetTimezone, compId);
    }

    public void getUpdateQtRecord8() {
        InsertQuery sq = new InsertQuery();
        uq = sq.UpdateQtRecord8(sourceTimezone, targetTimezone, compId);
    }

    public void getUpdateQtRecord9() {
        InsertQuery sq = new InsertQuery();
        uq = sq.UpdateQtRecord9(sourceTimezone, targetTimezone, compId);
    }

    public void getUpdateQtRecord10() {
        InsertQuery sq = new InsertQuery();
        uq = sq.UpdateQtRecord10(sourceTimezone, targetTimezone, compId);
    }

    public void getUpdateQtRecord11() {
        InsertQuery sq = new InsertQuery();
        uq = sq.UpdateQtRecord11(sourceTimezone, targetTimezone, compId);
    }

    public void getUpdateQtRecord12() {
        InsertQuery sq = new InsertQuery();
        uq = sq.UpdateQtRecord12(sourceTimezone, targetTimezone, compId);
    }
//   public void getUpdateQtRecord13(){
//        InsertQuery sq=new InsertQuery();
//        uq = sq.UpdateQtRecord13();
//    }

    public void getUpdateQtRecord14() {
        InsertQuery sq = new InsertQuery();
        uq = sq.UpdateQtRecord14(sourceTimezone, targetTimezone, compId);
    }

    public void getUpdateQtRecord15() {
        InsertQuery sq = new InsertQuery();
        uq = sq.UpdateQtRecord15(sourceTimezone, targetTimezone, compId);
    }

    public void getUpdateQtRecord16() {
        InsertQuery sq = new InsertQuery();
        uq = sq.UpdateQtRecord16(sourceTimezone, targetTimezone, compId);
    }

    public void getUpdateRemCommaComp() {
        InsertQuery sq = new InsertQuery();
        uq = sq.UpdateRemCommaComp(compId);
    }

    public void getUpdateRemCommaBooking() {
        InsertQuery sq = new InsertQuery();
        uq = sq.UpdateRemCommaBooking(compId);
    }

    public void getUpdateRemCommaPass() {
        InsertQuery sq = new InsertQuery();
        uq = sq.UpdateRemCommaPass(compId);
    }

    public void getUpdateRemCommaVehicle() {
        InsertQuery sq = new InsertQuery();
        uq = sq.UpdateRemCommaVehicle(compId);
    }

    public void getUpdateRemCommaDimCountry() {
        InsertQuery sq = new InsertQuery();
        uq = sq.UpdateRemCommaDimCountry();
    }

    public void getUpdateRemCommaDimClient() {
        InsertQuery sq = new InsertQuery();
        uq = sq.UpdateRemCommaDimClient();
    }

    public void getUpdateRemCommaDimDept() {
        InsertQuery sq = new InsertQuery();
        uq = sq.UpdateRemCommaDimDept();
    }

    public void getUpdateRemCommaDimPack() {
        InsertQuery sq = new InsertQuery();
        uq = sq.UpdateRemCommaDimPack();
    }

    public void getUpdateRemCommaDimProd() {
        InsertQuery sq = new InsertQuery();
        uq = sq.UpdateRemCommaDimProd();
    }

    public void getUpdateRemCommaDimVendor() {
        InsertQuery sq = new InsertQuery();
        uq = sq.UpdateRemCommaDimVendor();
    }

    public String runHashQuery(String tabName, String compId) {
        SelectQuery sq = new SelectQuery();
        hashQuery = sq.SelecthashQuery(tabName, compId);
        return hashQuery;
    }
//    public String  getqueryDelBooking(){
//        RequestQuery rq = new RequestQuery();
//        delBooking = rq.queryDelBooking();
//        return delBooking;
//    }
//    public String  getqueryDelBookingComp(){
//        RequestQuery rq = new RequestQuery();
//        delBookingComp = rq.queryDelBookingComp();
//        return delBookingComp;
//    }
//    public String  getqueryDelBookingPass(){
//        RequestQuery rq = new RequestQuery();
//        delBookingPass = rq.queryDelBookingPass();
//        return delBookingPass;
//    }
//    public String getqueryUpdateBookingComp(){
//        InsertQuery rq = new InsertQuery();
//        UpdateBookingComp = rq.queryUpdateBookingComp();
//        return UpdateBookingComp;
//    }
//    public String getqueryUpdateBookingPass(){
//        InsertQuery rq = new InsertQuery();
//        UpdateBookingPass = rq.queryUpdateBookingPass();
//        return UpdateBookingPass;
//    }

    public String getqueryqueryInsertBookId() {
        InsertQuery bi = new InsertQuery();
        InsertBookId = bi.queryInsertBookId();
        return InsertBookId;
    }

    public String getqueryqueryInsertBookRef() {
        InsertQuery bf = new InsertQuery();
        InsertBookRef = bf.queryInsertBookRef();
        return InsertBookRef;
    }

    public String getrunqueryInsertTills() {
        InsertQuery tf = new InsertQuery();
        InsertTills = tf.queryInsertTills();
        return InsertTills;
    }

    public String getrunqueryUpdateTillsBooking() {
        InsertQuery tf = new InsertQuery();
        UpdateTillsBooking = tf.queryUpdateTillsBooking();
        return UpdateTillsBooking;
    }

    public String getrunqueryUpdateTillsComp() {
        InsertQuery tf = new InsertQuery();
        UpdateTillsComp = tf.queryUpdateTillsBookingComp();
        return UpdateTillsComp;
    }

    public String getrunqueryUpdateTillsPass() {
        InsertQuery tf = new InsertQuery();
        UpdateTillsPass = tf.queryUpdateTillsPass();
        return UpdateTillsPass;
    }

    public String getrunqueryUpdateTillsVehicle() {
        InsertQuery tf = new InsertQuery();
        UpdateTillsVehicle = tf.queryInsertTillsVehicle();
        return UpdateTillsVehicle;
    }

    public String getrunqueryUpdateBookingComp1() {
        InsertQuery tf = new InsertQuery();
        UpdateBookingComp1 = tf.queryUpdateBookingComp1();
        return UpdateBookingComp1;
    }

    public String getrunqueryUpdateBookingPass1() {
        InsertQuery tf = new InsertQuery();
        UpdateBookingPass1 = tf.queryUpdateBookingPass1();
        return UpdateBookingPass1;
    }

    public String getrunqueryInsertPassId() {
        InsertQuery pi = new InsertQuery();
        InsertPassId = pi.queryInsertPassId();
        return InsertPassId;
    }

    public String getrunqueryInsertTempPassName() {
        InsertQuery pi = new InsertQuery();
        InsertTempPassName = pi.queryInsertTempPassName();
        return InsertTempPassName;
    }

    public String getrunqueryUpdateCompFact() {
        InsertQuery pi = new InsertQuery();
        UpdateCompFact = pi.queryUpdateCompFact();
        return UpdateCompFact;
    }

    public String getrunqueryTruncTemp() {
        InsertQuery pi = new InsertQuery();
        QueryFact = pi.queryTruncTemp();
        return QueryFact;
    }

    public String getrunqueryInsertTemp() {
        InsertQuery pi = new InsertQuery();
        QueryFact = pi.queryInsertTemp();
        return QueryFact;
    }

    public String getrunqueryUpdateTemp() {
        InsertQuery pi = new InsertQuery();
        QueryFact = pi.queryUpdateTemp();
        return QueryFact;
    }

    public String getrunqueryTruncRev() {
        InsertQuery pi = new InsertQuery();
        QueryFact = pi.queryTruncRev();
        return QueryFact;
    }

    public String getrunqueryInsertRev() {
        InsertQuery pi = new InsertQuery();
        QueryFact = pi.queryInsertRev();
        return QueryFact;
    }

    public String getrunqueryTruncRefCode() {
        InsertQuery pi = new InsertQuery();
        QueryFact = pi.queryTruncRefCode();
        return QueryFact;
    }

    public String getrunqueryInsertRefCode() {
        InsertQuery pi = new InsertQuery();
        QueryFact = pi.queryInsertRefCode();
        return QueryFact;
    }
    //set status=delete from bookings,reservations,consumers after INCR

    public String getrunqueryUpdateBookingsState() {
        InsertQuery pi = new InsertQuery();
        QueryFact = pi.queryUpdateBookingsState();
        return QueryFact;
    }

    public String getrunqueryUpdateCompState() {
        InsertQuery pi = new InsertQuery();
        QueryFact = pi.queryUpdateCompState();
        return QueryFact;
    }

    public String getrunqueryUpdatePassState() {
        InsertQuery pi = new InsertQuery();
        QueryFact = pi.queryUpdatePassState();
        return QueryFact;
    }
//    public String getrunqueryDeleteForPassConsSplit(){
//        InsertQuery pi = new InsertQuery();
//        QueryFact = pi.queryDeleteForPassConsSplit();
//        return QueryFact;
//    }

    public String getrunqueryUpdateVehicleState() {
        InsertQuery pi = new InsertQuery();
        QueryFact = pi.queryUpdateVehicleState();
        return QueryFact;
    }

    public String getrunqueryDeleteAccountingRows() {
        InsertQuery pi = new InsertQuery();
        QueryFact = pi.queryDeleteAccountingRows();
        return QueryFact;
    }

    public String getrunqueryDeleteServicesRows() {
        InsertQuery pi = new InsertQuery();
        QueryFact = pi.queryDeleteServicesRows();
        return QueryFact;
    }

    public String getrunqueryDeleteAdjustmentsRows1() {
        InsertQuery pi = new InsertQuery();
        QueryFact = pi.queryDeleteAdjustmentsRows1();
        return QueryFact;
    }

    public String getrunqueryDeleteAdjustmentsRows2() {
        InsertQuery pi = new InsertQuery();
        QueryFact = pi.queryDeleteAdjustmentsRows2();
        return QueryFact;
    }

    public ExecuteQuery() {
    }
    //Insert and update in Pax

    public String getrunqueryInsertCompPax() {
        InsertQuery pi = new InsertQuery();
        QueryFact = pi.queryInsertCompPax();
        return QueryFact;
    }

    public String getrunqueryInsertBookPax() {
        InsertQuery pi = new InsertQuery();
        QueryFact = pi.queryInsertBookPax();
        return QueryFact;
    }

    public String getrunqueryUpdateBookCompPax1() {
        InsertQuery pi = new InsertQuery();
        QueryFact = pi.queryUpdateBookCompPax1();
        return QueryFact;
    }

    public String getrunqueryUpdateBookCompPax2() {
        InsertQuery pi = new InsertQuery();
        QueryFact = pi.queryUpdateBookCompPax2();
        return QueryFact;
    }

    public String getrunqueryUpdateBookCompPax3() {
        InsertQuery pi = new InsertQuery();
        QueryFact = pi.queryUpdateBookCompPax3();
        return QueryFact;
    }

    public String getrunqueryUpdateBookCompPax4() {
        InsertQuery pi = new InsertQuery();
        QueryFact = pi.queryUpdateBookCompPax4();
        return QueryFact;
    }

    public String getrunqueryUpdateBookCompPax5() {
        InsertQuery pi = new InsertQuery();
        QueryFact = pi.queryUpdateBookCompPax5();
        return QueryFact;
    }

    public String getrunqueryTruncConSplit() {
        InsertQuery pi = new InsertQuery();
        QueryFact = pi.queryTruncConSplit();
        return QueryFact;
    }

    public String getrunqueryInsertConSplit() {
        InsertQuery pi = new InsertQuery();
        QueryFact = pi.queryInsertConSplit(compId);
        return QueryFact;
    }

    public String getrunqueryTruncVeh() {
        InsertQuery pi = new InsertQuery();
        QueryFact = pi.queryTruncVeh();
        return QueryFact;
    }

    public String getrunqueryInsertVeh() {
        InsertQuery pi = new InsertQuery();
        QueryFact = pi.queryInsertVeh();
        return QueryFact;
    }

    public String getrunqueryUpdateVeh() {
        InsertQuery pi = new InsertQuery();
        QueryFact = pi.queryUpdateVeh();
        return QueryFact;
    }

    public void getTrackLoadIncr() {
        RequestQuery tl = new RequestQuery();
        trackLoadQuery = tl.trackerLoadIncr(st_date, lt_date, sourceTableName, load, compId, sequence);
    }

    public void getqueryInsertCallsDim1() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryInsertCallsDim1();
    }

    public void getqueryInsertCallsDim2() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryInsertCallsDim2();
    }

    public void getqueryInsertCallsDim3() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryInsertCallsDim3();
    }

    public void getqueryInsertCallsFact1() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryInsertCallsFact1();
    }

    public void getqueryInsertCallsFact2() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryInsertCallsFact2();
    }

    public void getqueryInsertCallsFact3() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryInsertCallsFact3();
    }

    public void getqueryTruncPaxFName() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryTruncPaxFName();
    }

    public void getqueryInsertPaxFName() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryInsertPaxFName(compId);
    }

    public void getqueryUpdatePaxFName() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdatePaxFName();
    }

    public void getqueryClientType1() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryClientType1();
    }

    public void getqueryClientType2() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryClientType2();
    }

    public void getqueryClientType3() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryClientType3();
    }

    public void getqueryUpdateAccFacts() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdateAccFacts(sourceTimezone, targetTimezone, compId);
    }

    public void getqueryUpdatePaymentsFacts() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdatePaymentsFacts(sourceTimezone, targetTimezone, compId);
    }
//   public void getqueryUpdateBookingFacts(){
//        InsertQuery sq=new InsertQuery();
//        uq = sq.queryUpdateBookingFacts();
//    }
//   public void getqueryUpdateBookingPassFacts(){
//        InsertQuery sq=new InsertQuery();
//        uq = sq.queryUpdateBookingPassFacts();
//    }

    public void getqueryUpdateClient1() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdateClient1();
    }

    public void getqueryUpdateClient2() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdateClient2();
    }

    public void getqueryUpdateClient3() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdateClient3();
    }

    public void getqueryUpdatebookingComments1() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdatebookingComments1();
    }

    public void getqueryUpdatebookingComments2() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdatebookingComments2();
    }

    public void getqueryUpdateTrafficLevel1() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdateTrafficLevel1();
    }

    public void getqueryUpdateTrafficLevel2() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdateTrafficLevel2();
    }
//    public void getqueryUpdateBookingCompfact(){
//        InsertQuery sq=new InsertQuery();
//        uq = sq.queryUpdateBookingCompfact();
//    }
    //valuable product and packages

    public void getqueryTruncBookValPackTmp1() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryTruncBookValPackTmp1();
    }

    public void getqueryInsertBookValPackTmp1() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryInsertBookValPackTmp1();
    }

    public void getqueryTruncBookValPackTmp3() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryTruncBookValPackTmp3();
    }

    public void getqueryInsertBookValPackTmp3() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryInsertBookValPackTmp3();
    }

    public void getqueryTruncBookValPackTmp2() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryTruncBookValPackTmp2();
    }

    public void getqueryInsertBookValPackTmp2() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryInsertBookValPackTmp2();
    }

    public void getqueryUpdateBookingValPackId() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdateBookingValPackId();
    }

    public void getqueryUpdateBookingValPackName() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdateBookingValPackName();
    }

    public void getqueryTruncBookValProdTmp1() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryTruncBookValProdTmp1();
    }

    public void getqueryInsertBookValProdTmp1() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryInsertBookValProdTmp1();
    }

    public void getqueryTruncBookValProdTmp3() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryTruncBookValProdTmp3();
    }

    public void getqueryInsertBookValProdTmp3() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryInsertBookValProdTmp3();
    }

    public void getqueryTruncBookValProdTmp2() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryTruncBookValProdTmp2();
    }

    public void getqueryInsertBookValProdTmp2() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryInsertBookValProdTmp2();
    }

    public void getqueryUpdateBookingValProdId() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdateBookingValProdId();
    }

    public void getqueryUpdateBookingValProdName() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdateBookingValProdName();
    }
    //over
    // Last payment made code.........start

    public void getqueryTruncLastPayment() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryTruncLastPayment();
    }

    public void getqueryInsertLastPayment() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryInsertLastPayment();
    }

    public void getqueryTruncLastPaymentDetails() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryTruncLastPaymentDetails();
    }

    public void getqueryInsertLastPaymentDetails() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryInsertLastPaymentDetails();
    }
    // Last payment made code.........end
    //update booking fact with payment info for last payments...start

    public void getqueryUpdateBookingPaymentsInfo() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdateBookingPaymentsInfo();
    }
    //update booking fact with payment info for last payments...end
    //update at booking,comp and pass level ............start

    public void getqueryUpdateBookingCompfactFromBooking() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdateBookingCompfactFromBooking();
    }

    public void getqueryUpdateBookingPassFromBooking() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdateBookingPassFromBooking();
    }

    public void getqueryUpdateBookingPassFromBookingComp() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdateBookingPassFromBookingComp();
    }
    //update at booking,comp and pass level ............end
    //updates in stg for creator and updator name and id from user dim......start

    public void getqueryUpdateBookingStgWithCreatorName() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdateBookingStgWithCreatorName();
    }

    public void getqueryUpdateBookingStgWithUpdatorName() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdateBookingStgWithUpdatorName();
    }

    public void getqueryUpdatePaymentsStgWithUpdatorName() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdatePaymentsStgWithUpdatorName();
    }

    public void getqueryTruncTempAdjustmentsCompTable() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryTruncTempAdjustmentsCompTable();
    }

    public void getqueryInsertTempAdjustmentsCompNames() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryInsertTempAdjustmentsCompNames();
    }

    public void getqueryUpdateAdjustmentsAtCompLevel() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdateAdjustmentsAtCompLevel();
    }

    public void getqueryTruncTempAdjustmentsBookTable() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryTruncTempAdjustmentsBookTable();
    }

    public void getqueryInsertTempAdjustmentsBookNames() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryInsertTempAdjustmentsBookNames();
    }

    public void getqueryUpdateAdjustmentsAtBookingLevel() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdateAdjustmentsAtBookingLevel();
    }

    public void getqueryTruncTempAdjustmentsCompBookTable() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryTruncTempAdjustmentsCompBookTable();
    }

    public void getqueryInsertTempAdjustmentsCompBookNames() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryInsertTempAdjustmentsCompBookNames();
    }

    public void getqueryUpdateAdjustmentsAtCompBookLevel() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdateAdjustmentsAtCompBookLevel();
    }

    public void getqueryUpdateAdultChBooking() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdateAdultChBooking();
    }

    public void getqueryUpdateAdultChComp() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdateAdultChComp();
    }

    public void getqueryTruncFirstPaymentStg() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryTruncFirstPaymentStg();
    }

    public void getqueryInsertFirstPaymentStg() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryInsertFirstPaymentStg();
    }

    public void getqueryUpdateBookingWithPayment() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdateBookingWithPayment();
    }

    public void getqueryUpdatePaymentsFactFromBookingStg() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdatePaymentsFactFromBookingStg();
    }

    public void getqueryUpdateTimeZonePaymentsStg() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdateTimeZonePaymentsStg(sourceTimezone, targetTimezone, compId);
    }

    public void getqueryTruncateBookingfromPromoCode() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryTruncateBookingfromPromoCode();
    }

    public void getqueryInsertBookingfromPromoCode() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryInsertBookingfromPromoCode();
    }

    public void getqueryTruncateTempBookingPromoCode() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryTruncateTempBookingPromoCode();
    }

    public void getqueryInsertTempBookingPromoCode() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryInsertTempBookingPromoCode();
    }

    public void getqueryUpdatePromoCodeInBooking() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdatePromoCodeInBooking();
    }

    public void getqueryUpdatePackageProductInResService() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdatePackageProductInResService();
    }

    public void getqueryUpdatePropertyIdInComp() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdatePropertyIdInComp();
    }

    public void getqueryTruncQRuleCompCountTemp() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryTruncQRuleCompCountTemp();
    }

    public void getqueryInsertQRuleCompCountTemp() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryInsertQRuleCompCountTemp();
    }

    public void getqueryUpdateQRuleCountInComp() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdateQRuleCountInComp();
    }

    public void getqueryUpdateQRuleCountInQRule() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdateQRuleCountInQRule();
    }

    public void getqueryInsertQRuleCompCount1Temp() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryInsertQRuleCompCount1Temp();
    }

    public void getqueryUpdateAllocatedBookingInQRule() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdateAllocatedBookingInQRule();
    }

    public void getqueryUpdateCustumNumberInQRule() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdateCustumNumberInQRule();
    }

    public void getqueryTruncVehicleLength() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryTruncVehicleLength();
    }

    public void getqueryInsertVehicleLength() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryInsertVehicleLength();
    }

    public void getqueryUpdateVehicleLengthInComp() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdateVehicleLengthInComp();
    }
    //start of code  on 29Jan14

    public void getqueryUpdateCountryInBooking() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdateCountryInBooking();
    }

    public void getqueryUpdateCountryInComp() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdateCountryInComp();
    }

    public void getqueryUpdateCountryInPax() {
        InsertQuery sq = new InsertQuery();
        uq = sq.queryUpdateCountryInPax();
    }
    //end of code  on 29Jan14

//    public void getqueryUpdateBookingWithPayment1(){
//        InsertQuery sq=new InsertQuery();
//        uq = sq.queryUpdateBookingWithPayment1();
//    }
    //updates in stg for creator and updator name and id from user dim......end
    public static void main(String[] args) throws Exception {
//      PreparedStatement pstmt;
//      ResultSet rs;
//      ResultSet temp;
//      String dbtype="mySql";
//      String tabname="product_dimension_stg";
//      for souce side process
//       SourceConn sc=new SourceConn();
//      Connection con =sc.getConnection(dbtype);
//      SelectQuery sq=new SelectQuery();
//      String sqlquery= sq.selecttabname(tabname);
//
//       
//       pstmt =con.prepareStatement(sqlquery);
//         rs = pstmt.executeQuery();
//          
//      ResultSetMetaData rmeta= rs.getMetaData();
//      -----------source complete-------
//         for target side connection------
//
//
//
//          dbtype="mySql_sl";
//          tabname="product_dimension_stg";
//
//
//         while(rs.next())
//         {
//              try{
//               sc=new SourceConn();
//               con =sc.getConnection(dbtype);
//               InsertQuery iq=new InsertQuery();
//               sqlquery= iq.Inserttabname(tabname);
//            pstmt = con.prepareStatement(sqlquery);
//            for(int j=1;j<=rmeta.getColumnCount();j++)
//            {
//                
//                
//                if(rmeta.getColumnClassName(j).equalsIgnoreCase("INTEGER") || rmeta.getColumnClassName(j).equalsIgnoreCase("INT")
//                        || rmeta.getColumnClassName(j).equalsIgnoreCase("BIGINT")
//                        || rmeta.getColumnClassName(j).equalsIgnoreCase("smallint")
//                        ) {
//                            pstmt.setInt(j,rs.getInt(j));
//                         Integer i=(Integer)rs.getObject(1);
//                             count=i.intValue();
//
//                            }
//                else if(rmeta.getColumnType(j) == Types.VARCHAR)
//              {
//                  pstmt.setString(j,rs.getString(j));
//              }
//                   else if(rmeta.getColumnType(j) == Types.DATE )
//              {
//                  pstmt.setDate(j,rs.getDate(j));
//              }
//                  else if(rmeta.getColumnType(j) == Types.TIME)
//              {
//                  pstmt.setTime(j,rs.getTime(j));
//              } else if(rmeta.getColumnType(j) == Types.DECIMAL
//                    || rmeta.getColumnType(j) == Types.FLOAT
//                    || rmeta.getColumnType(j) == Types.DOUBLE
//                    )
//              {
//                  pstmt.setDouble(j,rs.getDouble(j));
//              }else if(rmeta.getColumnType(j) == Types.INTEGER
//                    )
//              {
//                  pstmt.setInt(j,rs.getInt(j));
//              }
//
//
//              else{
//                   pstmt.setString(j,rs.getString(j));
//
//                  }
//
//
//
//                 }
//            
//
//         pstmt.addBatch();
//
//
//        pstmt.executeBatch();
//             
//
//      }
//
//          catch (SQLException e) {
//              
//              logger.error("Exception:",e);
//}
//
//     }
    }
}
