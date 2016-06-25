<%--
    Document   : pbViewTable
    Created on : Aug 17, 2009, 5:22:59 PM
    Author     : santhosh.kumar@progenbusiness.com
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Vector,java.util.ArrayList,prg.business.group.BusinessGroupDAO,com.progen.datadisplay.client.PbDataDisplayManager,prg.db.PbReturnObject"%>

<%      //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String tableId = request.getParameter("tableIds");
            String grpId = request.getParameter("grpId");
            String relTableIds = request.getParameter("relTableIds");
            String prevTabNames = request.getParameter("prevrelTabNames");

            String[] prevRelTabIds = null;
            ArrayList alist1 = new ArrayList();
            alist1.add(tableId);
            Vector prevTabVector = new Vector();
            if (relTableIds != null) {
                prevRelTabIds = relTableIds.split(",");
                for (int i = 0; i < prevRelTabIds.length; i++) {
                    alist1.add(prevRelTabIds[i]);
                    prevTabVector.add(prevRelTabIds[i]);
                }
            }

            prg.business.group.BusinessGroupDAO manager = new prg.business.group.BusinessGroupDAO();
            PbReturnObject retObj1 = null;
            PbReturnObject retObj2 = null;
            ArrayList alist = null;

            if (relTableIds != null) {

                alist = manager.viewBussData(tableId, alist1, grpId);

            } else {

                alist = manager.viewBussData(tableId, grpId);

            }


            retObj1 = (PbReturnObject) alist.get(0);

            retObj2 = (PbReturnObject) alist.get(1);


            String[] tableColumnNames = retObj2.getColumnNames();
            String contPath=request.getContextPath();


%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="<%=contPath%>/tablesorter/docs/css/jq.css" type="text/css" media="print, projection, screen" />
        <link rel="stylesheet" href="<%=contPath%>/tablesorter/themes/blue/style.css" type="text/css" media="print, projection, screen" />
        <script type="text/javascript" src="<%=contPath%>/tablesorter/jquery-latest.js"></script>
        <script type="text/javascript" src="<%=contPath%>/tablesorter/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script language="JavaScript" src="<%=contPath%>/tablesorter/jquery.columnfilters.js"></script>
        <script type="text/javascript" src="<%=contPath%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=contPath%>/tablesorter/docs/js/docs.js"></script>
        <script type="text/javascript" src="<%=contPath%>/tablesorter/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=contPath%>/tablesorter/dragTable.js"></script>

        <style>
            .text_classstyle1 {
                color:#336699;
                font-size:10px;
                line-height:16px;
                font-family:verdana;
            }

        </style>

       
    </head>
    <body>
        <form>

            <img id="imgId" src="images/ajax.gif" width="100px" height="100px" style="left:25%;top:25%">
            <table align="right"><tr><td> <img id="imgId" src="images/sign_cancel.png" style="position:inherit" onclick="parent.cancelTableList()"></td></tr></table>
            <div class="drag" id="main" >
                <script>
                    var divObj=document.getElementById("main");
                    divObj.style.visibility="hidden"
                </script>

                <div style="width:100px">
                    <a  href="javascript:dispRelTables()" style='text-decoration:none;width:auto;height:auto;left:25px' class="text_classstyle1" title="Related Tables">Related Tables</a>
                    <!-- -->
                    <div  style="display:none;width:auto;height:100px;background-color:#ffffff;overflow:auto;position: absolute;text-align: left;border: 1px solid #000000;border-top-width: 0px;"  id="RelTabList">
                        <Table BORDER="0" STYLE="width:inherit">
                            <%
            String[] colNames = retObj1.getColumnNames();
            for (int i = 0; i < retObj1.getRowCount(); i++) {
                            %>
                            <Tr>
                                <%if (prevTabVector.contains(retObj1.getFieldValueString(i, colNames[0]))) {%>
                                <Td NOWRAP ALIGN="left"><input type="checkbox" checked  name="relTabNames" class="text_classstyle1" id="checkBox1"  value="<%=retObj1.getFieldValueString(i, colNames[0])%>"></Td>
                                <Td NOWRAP ALIGN="left"  class="text_classstyle1"><%=retObj1.getFieldValueString(i, colNames[1])%></Td>
                                <%} else {%>
                                <Td NOWRAP ALIGN="left"><input type="checkbox"  name="relTabNames" class="text_classstyle1" id="checkBox1"  value="<%=retObj1.getFieldValueString(i, colNames[0])%>"></Td>
                                <Td NOWRAP ALIGN="left"  class="text_classstyle1"><%=retObj1.getFieldValueString(i, colNames[1])%></Td>
                                <%}%>
                            </Tr>
                            <%}%>
                            <tr>
                                <td></td>
                            </tr>
                        </Table>
                    </div>
                </div>
                <div id="pager" class="pager" align="right" >

                    <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/first.png" class="first"/>
                    <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/prev.png" class="prev"/>
                    <input type="text" class="pagedisplay"/>
                    <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/next.png" class="next"/>
                    <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/last.png" class="last"/>
                    <select class="pagesize">
                        <option value="5">5</option>
                        <option selected value="10">10</option>
                    </select>
                </div>
                <table cellspacing="1" class="tablesorter" id="tablesorter" width="100px" style="border:1px thin silver">
                    <thead>
                        <tr valign="top">
                            <%for (int i = 0; i < tableColumnNames.length; i++) {%>
                            <th><%=tableColumnNames[i]%></th>
                            <%}%>
                        </tr>
                    </thead>
                    <tfoot>
                    </tfoot>
                    <tbody>
                        <%for (int rowId = 0; rowId < retObj2.getRowCount(); rowId++) {%>
                        <tr valign="top">
                            <%for (int colId = 0; colId < tableColumnNames.length; colId++) {%>
                            <td><%=retObj2.getFieldValue(rowId, tableColumnNames[colId])%></td>
                            <%}%>
                        </tr>
                        <%}%>
                    </tbody>
                </table>
                <script>
                    $(document).ready(function() {
                        $('table#tablesorter').columnFilters();
                    });
                    divObj.style.visibility="visible";
                    document.getElementById("imgId").style.visibility="hidden";
                    document.getElementById("imgId").style.display="none";
                </script>
            </div>
            <div id="fade" class="black_overlay"></div>
            <%
            String temp = "";

            for (int i = 0; i < prevTabVector.size(); i++) {
                temp = temp + "," + prevTabVector.get(i);
            }
            if (!(temp.equalsIgnoreCase(""))) {
                temp = temp.substring(1);
            }

            %>
            <input type="hidden" id="prevrelTabNames" name="prevrelTabNames"value="<%=temp%>">

        </form>
             <script type="text/javascript">
            $(function() {
                $("table")
                .tablesorter({widthFixed: true})
                .tablesorterPager({container: $("#pager")});
            });

            function dispRelTables(){
                var presColStr="";
                var prevColStr="";
                var cnt=0;
                var obj=document.getElementById('RelTabList');
                // alert(obj);
                if(obj.style.display=='none'){
                    obj.style.display='';
                    // alert('in if');
                }
                else{
                    // alert('in else')
                    obj.style.display='none';
                    var relTabNamesObj=document.getElementsByName("relTabNames");
                    var prevrelTabNamesObj=document.getElementById("prevrelTabNames");
                    var tableId=parent.document.getElementById("bustableId").value;
                    var grpId=parent.document.getElementById("grpId").value;
                    var source="businessGroupViewTable.jsp?tableIds="+tableId+"&prevrelTabNames="+document.getElementById("prevrelTabNames").value+"&grpId="+grpId;
                    // alert(source);
                    prevColStr=prevrelTabNamesObj.value;


                    for(var i=0;i<relTabNamesObj.length;i++){
                        if(relTabNamesObj[i].checked){
                            presColStr=presColStr+","+relTabNamesObj[i].value;
                            cnt++;
                        }
                    }
                    if(presColStr!=""){
                        presColStr=presColStr.substring(1);
                    }
                    if(cnt==0){
                        if(prevColStr!=presColStr){
                            parent.refreshIframe(source)
                        }
                    }
                    else{
                        if(prevColStr!=presColStr){
                            source=source+"&relTableIds="+presColStr;
                            parent.refreshIframe(source);
                        }
                    }
                }

            }

        </script>
    </body>
</html>
