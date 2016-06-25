<%@page import="java.io.*"%>
<%@page import="prg.target.sheetschceduler.ExcelScheduler"%>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
                //////////////////////////////////////////.println(" in new upload=-=-.,.,");
                String targetId=(String)session.getAttribute("targetId");
                String UserId=(String)session.getAttribute("UserId");
                String myFile = (String)session.getAttribute("myFile");
                File tempFile=(File)session.getAttribute("tempFile");
                
                //////////////////////////////////////////.println(tempFile+" targetId "+targetId+" UserId "+UserId+" myFile ");
                //////////////////////////////////////////.println(" tempFile 0-0=-0= "+tempFile.toString());
                //////////////////////////////////////////.println(" myFile.toString() "+myFile.toString());
         
                //////////////////////////////////////////.println(" in upload in new ------------");
                ExcelScheduler es = new ExcelScheduler(targetId,UserId,myFile.toString(),tempFile.toString());
                es.readExcel(targetId,UserId,myFile.toString(),tempFile.toString());
                out.println("Your Request has been submitted. To view the data go to target UI.");
        %>
    </body>
</html>
