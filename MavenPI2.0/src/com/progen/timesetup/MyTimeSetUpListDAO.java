/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.timesetup;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 *
 * @author Saurabh
 */
public class MyTimeSetUpListDAO {

    public static Logger logger = Logger.getLogger(MyTimeSetUpListDAO.class);

    public static ArrayList getTimeSetUps(String connId) {
        ArrayList list = new ArrayList();
        TimeSetUpResourceBundle resBundle = new TimeSetUpResourceBundle();
        try {
            PbDb pbdb = new PbDb();
            String query = resBundle.getString("getCalList");
            //////////////////////////////////////////////////////////////////////////////.println("connId in Dao --in time->" + connId);
            Object obj[] = new Object[1];
            obj[0] = connId;
            String finalQuery = pbdb.buildQuery(query, obj);
            //////////////////////////////////////////////////////////////////////////////.println("query---" + finalQuery);
            PbReturnObject allcalObject = pbdb.execSelectSQL(finalQuery);
            String[] colNames = null;
            colNames = allcalObject.getColumnNames();
            //////////////////////////////////////////////////////////////////////////////////.println("hi---------------------"+allcalObject.getRowCount());
            allcalObject.writeString();
            for (int i = 0; i < allcalObject.getRowCount(); i++) {
                CalenderFormTable table = new CalenderFormTable();
                table.setCalName(allcalObject.getFieldValueString(i, colNames[1]));
                table.setCalId(String.valueOf(allcalObject.getFieldValueInt(i, colNames[0])));
                list.add(table);
            }


        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return list;


    }
}
