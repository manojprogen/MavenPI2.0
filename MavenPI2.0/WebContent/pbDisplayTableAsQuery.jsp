<%-- 
    Document   : pbDisplayTableAsQuery
    Created on : Nov 24, 2009, 12:40:20 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbReturnObject"%>

<%
        PbReturnObject tabDetails = null;
      
        if (session.getAttribute("tabDetails") != null) {
            tabDetails = ((PbReturnObject) session.getAttribute("tabDetails"));

            String cols[] = tabDetails.getColumnNames();
            String colTypes[] = tabDetails.getColumnTypes();
            int colsSizes[] = tabDetails.getColumnSizes();
            %>

<html>
    <head>
        <script>
            function saveTableList(){
                document.forms.dispForm2.action='editconn.do?parameter=saveDetails';
                document.forms.dispForm2.submit();
                parent.refreshAddQTable();

            }
        </script>
    </head>
    <body>
   
        <form name="dispForm2" action=''  method="post" >
            <h3>Query View TAble</h3>
                      <%-- <div style="width:100%;height:auto">
                   </div> --%>
                <Table border='1' width='100%'>
                    <tr  valign= "top">
                        <th class=header bgcolor= '#b4d9ee' align= 'center' > <b class='label'> Col_Name </b> </th>
                        <th class= 'header' bgcolor= '#b4d9ee' align= 'center'> <b class='label'> Col_type </b> </th>
                        <th class= 'header' bgcolor= '#b4d9ee' align= 'center'> <b class='label'> Col_Size </b></th>
                    </tr>
                    <%for (int l = 0; l < cols.length; l++) {%>
                    <Tr class='even' valign='top'>
                        <Td class='_filterCol'><%=cols[l]%></Td>
                        <Td class='_filterCol'><%=colTypes[l]%></Td>
                        <Td class='_filterCol'><%=colsSizes[l]%></Td>
                    </Tr>
                    <% }%>
                    <%--
                    <input type="hidden" name="connectionId" id="connectionId" value="<%=String.valueOf(request.getAttribute("connectionId"))%>">
                    <input type="hidden" name="tableName" id="tableName" value="<%=String.valueOf(request.getAttribute("tableName"))%>">
                    <input type="hidden" name="query" id="query" value="<%=String.valueOf(request.getAttribute("query"))%>">
                        --%>
                    <center>
                        <table>
                            <Tr>
                                <td colspan="3">
                                    <input type= "button" value= "Save" onclick='saveTableList()'>&nbsp;
                                    <input type= 'button' value= 'Cancel' onclick= 'parent.refreshAddQTable();'>
                                </td>
                            </Tr>
                        </table>
                    </center>

                </Table>
         

        </form>
    </body>
</html>
<%}%>
