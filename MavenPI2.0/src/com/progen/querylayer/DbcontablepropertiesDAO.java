/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.querylayer;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 *
 * @author Administrator
 */
public class DbcontablepropertiesDAO extends PbDb {

    public static Logger logger = Logger.getLogger(DbcontablepropertiesDAO.class);
    DbcontablepropertiesResourcbundle resBundle = new DbcontablepropertiesResourcbundle();
    PbReturnObject pbretobj = new PbReturnObject();

    public ArrayList gettabledata(String tableid) {

        ArrayList tabledata = new ArrayList();
        try {

            Object[] tableidobj = new Object[2];
            tableidobj[0] = tableid;
            tableidobj[1] = tableid;

            String gettabquery = resBundle.getString("gettabledata");

            String buildquery = buildQuery(gettabquery, tableidobj);
            ////////////////////////////////////////////////////////////////////////////////////.println.println("builded query is" + buildquery);
            pbretobj = execSelectSQL(buildquery);

            for (int i = 0; i < pbretobj.getRowCount(); i++) {
                Tablepropertiesbean tablebean = new Tablepropertiesbean();
                tablebean.setTabledesc(pbretobj.getFieldValueString(i, "TABLE_ALIAS"));
                tablebean.setTablename(pbretobj.getFieldValueString(i, "TABLE_NAME"));

                tabledata.add(tablebean);
            }

        } catch (Exception e) {
            ////////////////////////////////////////////////////////////////////////////////////.println.println("Exception caught in gettabledata of dbcontablepropertiesdao class");
            logger.error("Exception:", e);
        }
        return tabledata;
    }

    public String gettablename(String tableid) {
        try {

            Object[] tableidobj = new Object[1];
            tableidobj[0] = tableid;

            String gettabquery = resBundle.getString("gettablename");

            String buildquery = buildQuery(gettabquery, tableidobj);
            ////////////////////////////////////////////////////////////////////////////////////.println.println("builded query is" + buildquery);
            pbretobj = execSelectSQL(buildquery);

            for (int i = 0; i < pbretobj.getRowCount(); i++) {
                tableid = pbretobj.getFieldValueString(i, "TABLE_NAME");
            }

        } catch (Exception e) {
            ////////////////////////////////////////////////////////////////////////////////////.println.println("Exception caught in gettablename of dbcontablepropertiesdao class");
            logger.error("Exception:", e);
        }
        return tableid;
    }

    public PbReturnObject gettabledescription(String tableid) {

        PbReturnObject tabledescription = new PbReturnObject();

        try {

            Object[] obj = new Object[1];
            obj[0] = tableid;

            String query = resBundle.getString("gettalbedescription");

            String buildquery = buildQuery(query, obj);
            ////////////////////////////////////////////////////////////////////////////////////.println.println("bulidedquery is"+buildquery);

            tabledescription = execSelectSQL(buildquery);

        } catch (Exception e) {
            ////////////////////////////////////////////////////////////////////////////////////.println.println("Exception caught in gettabledescription method of dbcontalbepropertiesdao class");
            logger.error("Exception:", e);
        }

        return tabledescription;
    }
}
