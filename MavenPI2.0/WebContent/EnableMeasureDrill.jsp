<%--
    Document   : pbViewTable
    Created on : Aug 17, 2009, 5:22:59 PM
    Author     : santhosh.kumar@progenbusiness.com
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.util.screenDimensions,com.progen.db.ProgenDataSet,prg.db.Container,java.util.HashMap,com.progen.reportdesigner.db.ReportTemplateDAO,prg.db.PbReturnObject"%>
<%@page import="com.progen.datadisplay.client.PbDataDisplayManager,java.util.ArrayList,java.util.Vector"%>

<%

         String tableId = request.getParameter("tableIds");
         String reportId = request.getParameter("REPORTID");
         String dimName = request.getParameter("dimName");

          HashMap map = null;
          Container container = null;
          if (session.getAttribute("PROGENTABLES") != null) {
          map = (HashMap) session.getAttribute("PROGENTABLES");
          container = (Container) map.get(reportId);
          }
          ReportTemplateDAO reportdao=new ReportTemplateDAO();
            PbReturnObject retObj1 = null;
            ArrayList alist = null;
            
                alist = reportdao.viewTableData(tableId,container,dimName);
           

            //retObj2 = (PbReturnObject) alist.get(0);
            retObj1 = (PbReturnObject) alist.get(0);

            String[] tableColumnNames = retObj1.getColumnNames();
            String contextPath=request.getContextPath();
           


%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link rel="stylesheet" href="<%=contextPath%>/tablesorter/docs/css/jq.css" type="text/css" media="print, projection, screen" />
        <link rel="stylesheet" href="<%=contextPath%>/tablesorter/themes/blue/style.css" type="text/css" media="print, projection, screen" />
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/jquery-latest.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script language="JavaScript" src="<%=request.getContextPath()%>/tablesorter/jquery.columnfilters.js"></script>-->
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>-->
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/docs/js/docs.js"></script>-->
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/dragTable.js"></script>-->

        <style>
            .text_classstyle1 {
                color:#336699;
                font-size:10px;
                line-height:16px;
                font-family:verdana;
            }

        </style>
 <script>
                    $(document).ready(function() {
                        $('table#tablesorter').columnFilters();
                    });
                    divObj.style.visibility="visible";
                    document.getElementById("imgId").style.visibility="hidden";
                    document.getElementById("imgId").style.display="none";
                </script>
       
    </head>
    <body>
        <form id="enableMeasId" name="enableMeasId" action="<%=request.getContextPath()%>/TableDisplay/pbDownload.jsp" method="post">
            
            <center><input type="submit" name="Done" id="doneId" value="DownLoadInExcel">
             <input type="button" name="Done" id="doneId" value="Close" onclick="getInExcelSheet()">
            </center>

            <img id="imgId" src="images/ajax.gif" width="100px" height="100px" style="left:25%;top:25%">

            <div class="drag" id="main" >
                <input type="text" name="dType" value="MeasDrill" style="display: none">
            <input type="text" name="tableId" value='<%=tableId%>' style="display: none">
            <input type="text" name="REPORTID" value='<%=reportId%>' style="display: none">
            <input type="text" name="dimName" value='<%=dimName%>' style="display: none">
<!--                <table align="right">
                    <tr>
                        <td>
                            <img id="imgId" src="images/sign_cancel.png" style="position:inherit" onclick="parent.cancelTableList()">
                        </td>
                    </tr>
                </table>-->
                <script>
                    var divObj=document.getElementById("main");
                    divObj.style.visibility="hidden"
                </script>

   
<!--                <div id="pager" class="pager" align="right" >

                    <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/first.png" class="first"/>
                    <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/prev.png" class="prev"/>
                    <input type="text" class="pagedisplay"/>
                    <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/next.png" class="next"/>
                    <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/last.png" class="last"/>
                    <select class="pagesize">
                        <option value="5">5</option>
                        <option selected value="10">10</option>
                    </select>
                </div>-->
                <table cellspacing="1" class="tablesorter" id="tablesorter" width="100px" border="1px">
                    <thead>
                        <tr valign="top">
                            <%for (int i = 0; i < tableColumnNames.length; i++) {%>
                            <th  align="center"><b> <%=tableColumnNames[i]%></b><input type="text" name="headerS" value="<%=tableColumnNames[i]%>" style="display: none"></th>
                            <%}%>
                        </tr>
                    </thead>
                    <tfoot>
                    </tfoot>
                    <tbody>
                        <%for (int rowId = 0; rowId < retObj1.getRowCount(); rowId++) {%>
                        <tr valign="top">
                            <%for (int colId = 0; colId < tableColumnNames.length; colId++) {%>
                            <td><%=retObj1.getFieldValue(rowId, tableColumnNames[colId])%><input type="text" name="bodyData" value="<%=retObj1.getFieldValue(rowId, tableColumnNames[colId])%>" id="" style="display: none"></td>
                            <%}%>
                        </tr>
                        <%}%>
                    </tbody>
                </table>
                 
            </div>

        </form>
                   
                <script type="text/javascript">
            $(function() {
                $("table")
                .tablesorter({widthFixed: true})
                .tablesorterPager({container: $("#pager")});
            });
           
function getInExcelSheet(){
           parent.$("#drillMeasuresId").dialog('close');

}
        </script>     
    </body>
</html>
