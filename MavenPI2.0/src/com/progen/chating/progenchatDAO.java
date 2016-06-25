package com.progen.chating;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;

public class progenchatDAO extends PbDb {

    public static Logger logger = Logger.getLogger(progenchatDAO.class);
    progechatResourceBundle chatbundle = new progechatResourceBundle();
    PbReturnObject pbretobj = new PbReturnObject();

    public String saveMessages(String userid, String chatmsg, String repid, String username) {
        String status = "failure";

        try {
            Object[] chatobj = new Object[6];
            chatobj[0] = userid;
            chatobj[1] = username;
            chatobj[2] = repid;
            chatobj[3] = chatmsg;
            chatobj[4] = userid;
            chatobj[5] = userid;

            String query = chatbundle.getString("reviewMessages");

            String finalquery = buildQuery(query, chatobj);
//            ////////////////////////////////////////////////////////////////////.println.println("finalquery in chat" + finalquery);

            execModifySQL(finalquery);

            status = "success";

        } catch (Exception e) {
            //////////////////////////////////////////////////////////////////////////////////.println.println("Exception caught in savemessages method of progenchatdao class");
            logger.error("Exception:", e);
        }
        return status;
    }

    public ArrayList GetMessages(String repid) {

        ArrayList userchatdata = new ArrayList();

        try {
            String query = chatbundle.getString("getreviewMessages");
            Object[] obj = new Object[1];
            obj[0] = repid;

            String finalquery = buildQuery(query, obj);
//            ////////////////////////////////////////////////////////////////////.println.println("final query is get"+finalquery);


            pbretobj = execSelectSQL(finalquery);
            String msgdate[] = null;
            String date, month, year, datefrmt;
            for (int i = 0; i < pbretobj.getRowCount(); i++) {

                progenchatbean chatbean = new progenchatbean();

                chatbean.setUsername(pbretobj.getFieldValueString(i, "USER_NAME"));
                chatbean.setMessage(pbretobj.getFieldValueString(i, "MESSAGES"));
                msgdate = pbretobj.getFieldValueString(i, "CREATED_ON").split("-");
                year = msgdate[0];
                month = msgdate[1];
                date = msgdate[2];
                datefrmt = month + "/" + date + "/" + year;
                ////////////////////////////////////////////////////////////////////.println.println("datefrmt"+datefrmt);
                chatbean.setMsgdate(datefrmt);
                //////////////////////////////////////////////////////////////////////////////////.println.println(pbretobj.getFieldValueString(i, "USER_NAME"));
                //////////////////////////////////////////////////////////////////////////////////.println.println(pbretobj.getFieldValueString(i, "MESSAGES"));

                userchatdata.add(chatbean);
            }
        } catch (Exception e) {
            //////////////////////////////////////////////////////////////////////////////////.println.println("Exception caught in savemessages method of progenchatdao class");
            logger.error("Exception:", e);
        }
        return userchatdata;
    }
}
