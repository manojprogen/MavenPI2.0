<%-- 
    Document   : pbColumnDrill
    Created on : Jul 29, 2009, 5:43:31 PM
    Author     : santhosh.kumar@progenbusiness.com
--%>

<%@page contentType="text/html" pageEncoding="windows-1252" import="prg.db.PbReturnObject,prg.graphs.client.*,java.util.ArrayList"%>


<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
            <link href="css/myStyles.css" rel="stylesheet" type="text/css">
        <title>Drill across Column</title>
        <script>
            function test(col,rep,tab){

            }
            function colDrillDownSave(){
                document.forms.colDrillForm.action='pbSaveColumnDrill.jsp';
                document.forms.colDrillForm.submit();
            }
            function windowClose(){ 
                 window.close();
            }
        </script>
        <script>
            function handleUnload()
            {
                //ev=ev || window.event;
                //ev.returnValue = "Click cancel to return to the blah";
                //alert('window closed')
                //alert(window.event)
               // alert(event)
            }
        </script> 
    </head>
    <%
        String colNames = request.getParameter("COLIDS");
        String reportId = request.getParameter("REPORTID");
        String tableId = request.getParameter("TABLEID");

        PbGraphsManager client = new PbGraphsManager();
        ArrayList ColumnDrillDownPreReq = client.getColumnDrillDownPreReq(reportId, tableId, colNames.split(","));
        PbReturnObject pbro1 = (PbReturnObject) ColumnDrillDownPreReq.get(0);//display names
        PbReturnObject pbro2 = (PbReturnObject) ColumnDrillDownPreReq.get(1);//report ids and names

        String[] tableColumnNames = null;
        String[] reportTableColumnNames = null;
    %>

    <body onunload="handleUnload(event);">
        <center>
            <br>
            <form name="colDrillForm" method="post">
                <input type="hidden" name="COLIDS" id="COLIDS" value="<%=colNames%>">
                <input type="hidden" name="REPORTID" id="REPORTID" value="<%=reportId%>">
                <input type="hidden" name="TABLEID" id="TABLEID" value="<%=tableId%>">
                <Table border='1' class="prgtable1" id="drillDownTable" width="50%">
                    <Tr>
                        <th nowrap class="prgtableheader">Column Name</th>
                        <th nowrap class="prgtableheader">Drill Down Structure</th>
                    </Tr>
                    <%
        tableColumnNames = pbro1.getColumnNames();
        reportTableColumnNames = pbro2.getColumnNames();
        for (int i = 0; i < pbro1.getRowCount(); i++) {
                    %>
                    <Tr>
                        <Td  NOWRAP id="column1" class="myTextbox5"><%=pbro1.getFieldValueString(i, tableColumnNames[1])%></Td>
                        <Td NOWRAP>
                            <select class="myTextbox5" name="<%=pbro1.getFieldValueString(i, tableColumnNames[0])%>">
                                <option value="">No Drill Down</option>
                                <%
                        for (int j = 0; j < pbro2.getRowCount(); j++) {
                           if ((pbro1.getFieldValueString(i, tableColumnNames[2])!=null) && (pbro1.getFieldValueString(i, tableColumnNames[2]).equalsIgnoreCase("Y")) && (pbro1.getFieldValueString(i, tableColumnNames[3]).equalsIgnoreCase(pbro2.getFieldValueString(j, reportTableColumnNames[1])))) {
                                %>
                                <option selected value="<%=pbro2.getFieldValueString(j, reportTableColumnNames[1])%>"><%=pbro2.getFieldValueString(j, reportTableColumnNames[0])%></option>
                                <%} else {
                                %>
                                <option  value="<%=pbro2.getFieldValueString(j, reportTableColumnNames[1])%>"><%=pbro2.getFieldValueString(j, reportTableColumnNames[0])%></option>
                                <%
                            }
                        }
                                %>
                            </select>
                        </Td>
                    </Tr>
                    <%
        }
                    %>
                </Table>
            </form>
            <br>
            <Table>
                <Tr>
                    <Td><input class="btn" type="button" value="Save" onclick="javascript:colDrillDownSave();"></Td>
                    <Td><input class="btn" type="button" value="Close Window" onclick="javascript:windowClose();"></Td>
                </Tr>
            </Table>
        </center>
    </body>
</html>
