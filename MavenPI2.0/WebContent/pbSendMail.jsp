<%@page pageEncoding="UTF-8" contentType="text/html"%>
<%@page import="prg.db.Session,prg.db.PbDb,prg.db.PbReturnObject,utils.db.*,java.sql.*" %>


<%  PbDb pbdb = new PbDb();
//////////////////////////////////////////////////////////////////////////////////////////////.println.println("in send mail page");
        String toAddress = request.getParameter("toAddress");
        String subject = request.getParameter("subject");
        String bodyText = request.getParameter("bodyText");


        java.io.File attFile = null;
        try {
            //attFile = new java.io.File(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        prg.util.PbMailParams params = new prg.util.PbMailParams();
        // String fileName  = request.getParameter("imgSrc");

        // params.setAttachFile(System.getProperty("java.io.tmpdir")+"\\"+attFile.getName()); //fileName
        params.setBodyText(bodyText);
        params.setToAddr(toAddress);
        params.setSubject(subject);
        params.setHasAttach(true);



        prg.util.PbMail mailer = new prg.util.PbMail(params);
        boolean status = mailer.sendMail();
        if (status == true) {

            String email = toAddress;
            //////////////////////////////////////////////////////////////////////////////////////////////.println.println("Obj is "+email);
            String[] emails = email.split(",");
            for (int i = 0; i < emails.length; i++) {
                emails[i] = "'" + emails[i] + "'";
                out.println(emails[i]);
            }

            String csvEmails = null;
            for (int i = 0; i < emails.length; i++) {
                if (csvEmails == null) {
                    csvEmails = "" + emails[i];
                } else {
                    csvEmails = csvEmails + "," + emails[i];
                }
            }

            //remove it later


            String query = " select PU_ID,PU_EMAIL from PRG_AR_USERS where PU_EMAIL in ('" + toAddress + "')";
            //////////////////////////////////////////////////////////////////////////////////////////////.println.println("query in sendmail"+query);
            PbReturnObject pbro = pbdb.execSelectSQL(query);
            for (int i = 0; i < pbro.getRowCount(); i++) {

                String Query = "insert into PRG_MESSAGE_BOARD (PMB_FROM,PMB_TO,PMB_SUBJECT,PMB_MESSAGE,PMB_ID,USERID) values ('noreply@progenbusiness.com'," + pbro.getFieldValueInt(i, "PU_EMAIL") + ",'" + subject + "','" + bodyText + "',PRG_MESSAGE_BOARD_SEQ.nextval," + pbro.getFieldValueInt(i, "PU_ID") + ")";
              //  ////////////////////////////////////////////////////////////.println.println("Query is " + Query);
                pbdb.execModifySQL("insert into PRG_MESSAGE_BOARD (PMB_FROM,PMB_TO,PMB_SUBJECT,PMB_MESSAGE,PMB_ID,USERID) values ('noreply@progenbusiness.com','" + pbro.getFieldValueInt(i, "PU_EMAIL") + "','" + subject + "','" + bodyText + "',PRG_MESSAGE_BOARD_SEQ.nextval," + pbro.getFieldValueInt(i, "PU_ID") + ")");

                Query = null;
            }
        //end of removal
        }
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>

        <h1>Mail Sent</h1>


    </body>
</html>
