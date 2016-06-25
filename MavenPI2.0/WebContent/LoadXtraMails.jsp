<%-- 
    Document   : Load_Xtra_Mails
    Created on : Aug 4, 2015, 4:24:54 PM
    Author     : Mohit
--%>
<!--Only for Leela Project-->
<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.userlayer.db.LogReadWriter,com.progen.xml.UploadingXmlIntoDatabase,prg.util.PbMail"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
try{
    LogReadWriter logrw = new LogReadWriter();
    logrw.setLogFileName("XmlLoad");
UploadingXmlIntoDatabase UpXml=new UploadingXmlIntoDatabase();
//UpXml.TruncateLoadTracker();
 PbMail mailer=new PbMail();
 mailer.downloadEmailAttachments("mohit.jain@progenbusiness.com,susmitha.y@progenbusiness.com,manoj.kumar@progenbusiness.com,ram.newas@progenbusiness.com");
  out.println("Successfully loaded the data");
  }
catch(Exception e)
        {
    e.printStackTrace();
    out.println("Exception Occured........");
    }
  

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
<!--        <h1>Hello World!</h1>-->
    </body>
</html>
