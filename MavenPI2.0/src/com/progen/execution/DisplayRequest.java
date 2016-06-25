package com.progen.execution;

import com.google.gson.Gson;
import com.progen.userlayer.db.LogReadWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.db.SourceConn;

public class DisplayRequest {

    public static Logger logger = Logger.getLogger(DisplayRequest.class);
    LogReadWriter logrw = new LogReadWriter();
    String detailQuery = null;
    String sourceString = null;
    String masterQuery = null;
    PbReturnObject retObj = null;
    PbDb pbdb = new PbDb();

    public void setSourceString(String sourceConnectString) {
        this.sourceString = sourceConnectString;
    }

    public String runUiDetailQuery(String masterId) {

        try {
            logrw.fileWriter("in detail display");
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        setSourceString("oracle1");
        SourceConn sc = new SourceConn();

        String Jsonstring = null;
        Connection con1 = sc.getConnection(sourceString, "null", "null", "null", "null", "null", "null", "null", "null");
        String query = getUiDetailQuery(masterId);
        List<String> requestIdList = new ArrayList<String>();
        List<String> requestNameList = new ArrayList<String>();
        List<String> requestTypeList = new ArrayList<String>();
        List<String> requestStDateList = new ArrayList<String>();
        List<String> requestLtDateList = new ArrayList<String>();
        List<String> requestStatusList = new ArrayList<String>();
        HashMap map = new HashMap();
        try {
            retObj = pbdb.execSelectSQL(query, con1);
            for (int i = 0; i < retObj.getRowCount(); i++) {
                requestIdList.add(retObj.getFieldValueString(i, 0));
                requestNameList.add(retObj.getFieldValueString(i, 1));
                requestTypeList.add(retObj.getFieldValueString(i, 2));
                requestStDateList.add(retObj.getFieldValueString(i, 3));
                requestLtDateList.add(retObj.getFieldValueString(i, 4));
                requestStatusList.add(retObj.getFieldValueString(i, 5));
            }

            map.put("requestIdList", requestIdList);
            map.put("requestNameList", requestNameList);
            map.put("requestTypeList", requestTypeList);
            map.put("requestStDateList", requestStDateList);
            map.put("requestLtDateList", requestLtDateList);
            map.put("requestStatusList", requestStatusList);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        Gson gson = new Gson();
        Jsonstring = gson.toJson(map);
        if (con1 != null) {
            try {
                con1.close();
                con1 = null;
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }

        }
        return Jsonstring;

    }

    public String runUiMasterQuery() {
        setSourceString("oracle1");
        SourceConn sc = new SourceConn();

        String Jsonstring = null;
        Connection con = sc.getConnection(sourceString, "null", "null", "null", "null", "null", "null", "null", "null");
        String query = getUiMasterQuery();
        List<String> requestIdList = new ArrayList<String>();
        List<String> requestNameList = new ArrayList<String>();
        List<String> requestTypeList = new ArrayList<String>();
        List<String> requestStatusList = new ArrayList<String>();
        HashMap map = new HashMap();
        try {
            retObj = pbdb.execSelectSQL(query, con);
            for (int i = 0; i < retObj.getRowCount(); i++) {
                requestIdList.add(retObj.getFieldValueString(i, 0));
                requestNameList.add(retObj.getFieldValueString(i, 1));
                requestTypeList.add(retObj.getFieldValueString(i, 2));
                requestStatusList.add(retObj.getFieldValueString(i, 3));
            }
            map.put("requestIdList", requestIdList);
            map.put("requestNameList", requestNameList);
            map.put("requestTypeList", requestTypeList);
            map.put("requestStatusList", requestStatusList);


        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        Gson gson = new Gson();
        Jsonstring = gson.toJson(map);
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }

        }
        return Jsonstring;
    }

    public String getUiDetailQuery(String masterId) {
        SelectQuery si = new SelectQuery();
        detailQuery = si.SelectDetailQuery(masterId);
        return detailQuery;
    }

    public String getUiMasterQuery() {
        SelectQuery si = new SelectQuery();
        masterQuery = si.SelectMasterQuery();
        return masterQuery;
    }
}
