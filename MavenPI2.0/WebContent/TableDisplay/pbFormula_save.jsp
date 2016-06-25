
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>
<%@page  import="utils.db.*" %>
<%@page  import="prg.measure.param.*" %>
<%@page  import="prg.measure.client.*" %>
<%@page  import="prg.reporttable.params.*" %>
<%@page  import="prg.reporttable.client.*" %>
<%@page  import="prg.db.*" %> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        
    </head>
   
        <%
         String path=null;
        try {
            String reportId=request.getParameter("reportId");
            String lastColumnSequence=request.getParameter("lastSeq");
            lastColumnSequence="0";
            path=request.getParameter("path");
         
            path=path.replace(';','&');
           
           // path="'"+path+"'";
         
            String columnName = request.getParameter("h");
            String columnDisplayName = request.getParameter("txt1");
            String actualCalculation = request.getParameter("tArea");
            int queryId = Integer.parseInt(request.getParameter("queryId"));
            String tableId = request.getParameter("tableId");
            String columnType="";
            //int queryId = 505;
            if (actualCalculation != null) {
                columnType = "Calculated";
                actualCalculation = "(" + actualCalculation + ")";
            }
           
            PbMeasuresParams params = new PbMeasuresParams();
            PbMeasuresManager client = new PbMeasuresManager();
            params.setColumnName(columnName);
            params.setColumnDisplayName(columnDisplayName);
            params.setColumnType(columnType);
            params.setQueryId(queryId);
            params.setActualCalculation(actualCalculation);
           

          //  params.setTypeOfColumn(typeOfColumn);

            Session sess=new Session();
            sess.setObject(params);
            client.insertMeasure(sess);

            PbReportTableDetailsParams repParams = new PbReportTableDetailsParams();
            PbReportTableManager repClient = new PbReportTableManager();
            
            repParams.setColName(columnName);
            repParams.setTableId(tableId);
            repParams.setQueryId(String.valueOf(queryId));
            repParams.setLastColumnSequence(lastColumnSequence);

            sess.setObject(repParams);
            client.addMeasureDetailsTableInfo(sess);
            //response.sendRedirect("pbFormula.jsp?reportId="+reportId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        %>
        <body onload="window.close();window.opener.submiturls1('<%=path%>')">
    </body>
</html>
