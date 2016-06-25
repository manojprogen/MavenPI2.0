
<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String contxtpath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!--        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>-->
        <script type="text/javascript" src="<%=contxtpath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
<!--        <script src="javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/treeview/demo.js"></script>-->

        <!--<script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.droppable.js"></script>-->
<!--        <link type="text/css" href="stylesheets/ui.all.css" rel="stylesheet" />-->
       <!-- <script type="text/javascript" src="javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="javascript/ui.sortable.js"></script>-->
<!--        <script type="text/javascript" src="javascript/effects.core.js"></script>
        <script type="text/javascript" src="javascript/effects.explode.js"></script>-->
<!--        <link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />-->
<!--        <script language="JavaScript" src="<%=request.getContextPath()%>/querydesigner/JS/jquery.columnfilters.js"></script>-->
<!--        <link href="stylesheets/StyleSheet.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/confirm.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <script src="javascript/jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>-->
        <link type="text/css" href="<%=contxtpath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <title>JSP Page</title>
       
    </head>
    <body>
        <%
           String reportName=request.getParameter("reportName");
           String REPORTID =request.getParameter("REPORTID");
           ////////////////////////.println(reportName+"- //REPORTID "+REPORTID);
        %>
        <Form name="reportNameFrm" id="reportNameFrm">
            <Br/>
            <Table>
                <Tr>
                    <Td>Report Name</Td>
                    <Td><Input type="text" READONLY value="<%=reportName%>"></Td>
                </Tr>
                <Tr>
                    <Td>New Report Name</Td>
                    <Td><Input type="text" name="newRepName" id="newRepName" value=""></Td>
                </Tr>
            </Table>
            <Br/>
            <Table>
                <Tr>
                    <Td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                     </Td>
                    <Td><Input type="button" name="" value="Save" ONCLICK="saveReportName()"></Td>
                </Tr>
            </Table>
                <Input TYPE="hidden" name="REPORTID" id="REPORTID" value="<%=REPORTID%>">
        </Form>
          <script type="text/javascript">
        function saveReportName()
        {
           
            //checkReportNameBeforeUpdate
            var repId=document.getElementById("REPORTID").value;
            var newRepName =document.getElementById("newRepName").value;
            $.ajax({
                      url: "reportTemplateAction.do?templateParam=checkReportNameBeforeUpdate&newRepName="+newRepName+"&REPORTID="+repId,
                                success: function(data)
                                {
                                     if(data==1)
                                         {
                                          parent.closeChangeName(newRepName);
                                         }
                                         else
                                         {
                                         alert(data);
                                         }
                                }
                });
        }
        </script>
    </body>
</html>
