<%--
    Document   : startPage
    Created on : Oct 22, 2009, 10:23:16 PM
    Author     : Chiranjeevi
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.Session,java.util.*,prg.db.PbDb,java.sql.*,prg.db.PbReturnObject,com.progen.reportdesigner.db.ReportTemplateDAO" %>

<html>
    <head>
        <title>Start Page</title>
        <style>
            .white_content {
                display:block;
                position: absolute;
                top: 15%;
                left: 24%;
                width:800px;
                height:450px;
                padding: 5px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:6px;
                -moz-border-radius-bottomright:6px;
                -moz-border-radius-topleft:6px;
                -moz-border-radius-topright:6px;
            }
            .black_overlay{
                position: absolute;
                top: 0%;
                left: 0%;
                width: 100%;
                height: 100%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                overflow:auto;
            }
            .upperTab{
                position: absolute;
                top: 1%;
                left: 0%;
            }
            a {font-family:Verdana;cursor:pointer;text-decoration:none;font-size:12px;color:black}

            a:hover{text-decoration:inherit;font-weight:bold}
            font {
                font-family:verdana;font-size:12px;color:#336699
            }
        </style>
        <script type="text/javascript">
            /* function viewDashboardG(path){
                parent.viewDashboardG(path);
            }
            function viewReportG(path){
                parent.viewReportG(path);
            }*/
            function viewDashboardG(dbrdId,dbrdName){
                var divId=parent.document.getElementById("divId").value;
                parent.document.getElementById("userframe").style.display='none';
                parent.document.getElementById("startPagePriv").style.display='none';
                parent.document.getElementById("fadestart").style.display='none';
                document.forms.myFormH.action=window.open('dashboardViewer.do?reportBy=viewDashboard&REPORTID='+dbrdId+'&pagename='+dbrdName+'&startFlag=Y&divId='+divId,'DashboardView','width=1200,height=1000');
                document.forms.myFormH.submit();

            }
            function viewReportG(reportId){
                var divId=parent.document.getElementById("divId").value;

                var frameObj=parent.document.getElementById("userframe");
                frameObj.src="reportViewer.do?reportBy=viewReport&REPORTID="+reportId+"&startFlag=Y&divId="+divId;
                parent.document.getElementById("startPagePriv").style.top='0';
                parent.document.getElementById("startPagePriv").style.left='0';
                parent.document.getElementById("startPagePriv").style.width='100%';
                parent.document.getElementById("startPagePriv").style.height='100%';
                parent.document.getElementById("userframe").height='100%';
                parent.document.getElementById("userframe").width='100%';

                //parent.document.getElementById("fadestart").style.display='none';
                //document.forms.myFormH.action=window.open();
                //document.forms.myFormH.submit();
            }
            function gohome(){
                parent.gohome();
            }
            function closeStart(){
                parent.closeStartDiv();
            }
            function goPaths(path){
                parent.goPaths(path);
            }


            function saveStart(){
                var startVar = document.forms.myFormH.startlogin;
                // alert(startVar.length);
                var count=0;
                for(var i=0;i<startVar.length;i++){
                    if(startVar[i].checked){
                        count=1;
                        break;
                    }
                }
                if(count==0)
                {
                    alert('Please Select Atleastone')

                }else{
                    document.forms.myFormH.action="saveStartPage.do";
                    document.forms.myFormH.submit();
                    parent.cancelStart();
                }
            }

            function captureImage(path){

            }
        </script>
    </head>
    <%
        PbDb pbdb = new PbDb();
        String userId = "";

        userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
        //////////////////////////////////////////////////////////.println.println("userid in startwebpage is:: " + request.getSession(false).getAttribute("USERID"));
        String userprivis = "SELECT USER_ID, PRIVELEGE_ID FROM PRG_AR_USER_PRIVELEGES where USER_ID in(" + userId + ") and PRIVELEGE_ID in('Query Studio')";

        PbReturnObject userprivispbro = pbdb.execSelectSQL(userprivis);
        userprivispbro.writeString();
        Vector privis = new Vector();
        for (int i = 0; i < userprivispbro.getRowCount(); i++) {
            privis.add(userprivispbro.getFieldValueString(i, 1));
        }
        PbReturnObject Dlist = new ReportTemplateDAO().getAllDashs();
        PbReturnObject Rlist = new ReportTemplateDAO().getAllreps();
    %>
    <div id="reportstart" >
        <form name="myFormH" method="post">
            <table width="100%" height="4%">
                <tr style="width:100%">
                    <td style="height:10%;width:20%"><img src="<%=request.getContextPath()%>/icons pinvoke/homeB1.png">
                        <a href="javascript:void(0)" onclick="javascript:gohome()">&nbsp;Home</a>
                    </td>
                    <td style="height:10%;width:20%" align="right" valign="top"><img src="<%=request.getContextPath()%>/icons pinvoke/cross-circle.png">
                        <a href="javascript:void(0)" onclick="javascript:closeStart()">&nbsp;Close</a>
                    </td>
                </tr>
            </table>
            <table border="1px solid " class="tabsty" align="center" width="100%" style="height:320px">
                <tr style="width:100%;">
                    <td  bgcolor="silver" width="22%">
                        <center>
                            <div class="prgtableheader2" style="background-color:silver;color:black"><font><b>Tabs</b></font></div>
                        </center>
                    </td>
                    <td bgcolor="silver" width="39%">
                        <center>
                            <div class="prgtableheader2" style="background-color:silver;color:black"><font><b>Reports</b></font></div>
                        </center>
                    </td>
                    <td bgcolor="silver" width="39%">
                        <center>
                            <div class="prgtableheader2" style="background-color:silver;color:black"><font><b>Dashboards</b></font></div>
                        </center>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <div  id="Tabs" style="overflow-y:auto;overflow-x:hidden;height:320px;">
                            <table >
                                <tr><td><img src="<%=request.getContextPath()%>/icons pinvoke/arrow.png"><a href="javascript:void(0)" onclick="goPaths('pbBase.jsp#Database_Connections')" id="dataCon">&nbsp;Database Connec..</a></td></tr>
                                <tr><td><img src="<%=request.getContextPath()%>/icons pinvoke/arrow.png"><a href="javascript:void(0)" onclick="goPaths('pbBase.jsp#Dimensions')">&nbsp;Dimensions</a></td></tr>
                                <tr><td><img src="<%=request.getContextPath()%>/icons pinvoke/arrow.png"><a href="javascript:void(0)" onclick="goPaths('pbBase.jsp#Time_SetUp')">&nbsp;Time SetUp</a></td></tr>
                                <tr><td><img src="<%=request.getContextPath()%>/icons pinvoke/arrow.png"><a href="javascript:void(0)" onclick="goPaths('pbBase.jsp#Business_Groups')">&nbsp;Business Groups</a></td></tr>
                                <tr><td><img src="<%=request.getContextPath()%>/icons pinvoke/arrow.png"><a href="javascript:void(0)" onclick="goPaths('pbBase.jsp#Business_Roles')">&nbsp;Business Roles</a></td></tr>

                            </table>
                        </div>
                    </td>
                    <td valign="top">
                        <div id="Reports"   style="overflow-y:auto;overflow-x:hidden;height:320px;">
                            <table>
                                <%for (int i1 = 0; i1 < Rlist.getRowCount(); i1++) {%>
                                <tr>
                                    <td valign="top">
                                        <img src="<%=request.getContextPath()%>/icons pinvoke/report.png"><a href="javascript:void(0)" onclick="javascript:viewReportG('<%=Rlist.getFieldValueString(i1, 0)%>')"> <%=Rlist.getFieldValueString(i1, 1)%></a>
                                    </td>
                                </tr>
                                <%}%>
                            </table>
                        </div>

                    </td>
                    <td valign="top">
                        <div id="Dashs"   style="overflow-y:auto;overflow-x:hidden;height:320px">
                            <table style="overflow:auto">
                                <%for (int i2 = 0; i2 < Dlist.getRowCount(); i2++) {%>
                                <tr>
                                    <td>
                                        <img src="<%=request.getContextPath()%>/icons pinvoke/map.png"><a href="javascript:void(0)" onclick="javascript:viewDashboardG('<%=Dlist.getFieldValueString(i2, 0)%>','<%=Dlist.getFieldValueString(i2, 1)%>')"><%=Dlist.getFieldValueString(i2, 1)%></a>
                                    </td>
                                </tr>
                                <%}%>
                            </table>
                        </div>
                    </td>
                </tr>
            </table>
            <input type="hidden" value="<%=userId%>" id="userId" name="userId">
            <center><input type="button" value="save" onclick="saveStart()"></center>
        </form>
    </div>
    </body>
</html>
