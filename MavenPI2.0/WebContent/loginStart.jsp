<%--
    Document   : LoginStart
    Created on : Dec 29, 2015, 2:53:27 PM
    Author     : Prabal Pratap Singh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.i18n.TranslaterHelper,java.sql.*,com.progen.users.PrivilegeManager,prg.db.PbReturnObject,prg.db.Session,java.util.*,prg.db.PbDb,com.progen.reportdesigner.db.ReportTemplateDAO"%>
<html>
    <head>
        <title>Login Start Page</title>
<!--        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />-->
        <link rel="stylesheet" href="css/font-awesome.min.css" type="text/css"/>
        <style type="text/css">
            a {font-family:inherit;cursor:pointer;text-decoration:none;font-size:12px;color:black}
            a:hover{text-decoration:inherit;font-weight:bold}
            *{font:12px inherit;}
            .prgtableheader2{background-color:#8BC34A;color:white;font-weight:bold;font-size:12px}
            .navtitle-hoverGreen{background-color:#d1d1d1;color:black;}
            .navtitle-hoverGreen:hover{background-color:#8BC34A;color:white;}
            .startpage-Header{margin-bottom: 5px;width: auto;text-align: center;background-color: rgb(139, 195, 74);color: white;font-weight: bold;}
        </style>
        <script type="text/javascript">
            function viewDashboardG(path){
                parent.viewDashboardG(path);
            }
            function viewReportG(path){
                parent.viewReportG(path);
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
            function saveStart(from){
                  var startVar = document.forms.myFormH.startlogin;
                   var count=0;
                   for(var i=0;i<startVar.length;i++){
                       if(startVar[i].checked){
                           count=1;
                           break;
                       }
                     }                     
                if(count==0)
                {
                    alert('Please Select Atleast one ')
                }else{                    
                    var checkId = document.getElementById("checkId").value;
                    document.forms.myFormH.action="saveStartPage.do?checkId="+checkId+"&from="+from;
                    document.forms.myFormH.submit();
                    parent.$("#startPagePriv").dialog('close');  
                    parent.$("#startPagePriv1").dialog('close');
                }                               
            }
            function showChosePage()
            {                  
                var items = document.getElementsByName("startlogin");
                var chosePage = document.getElementById("chosePage").value;
                for(var i=0;i<items.length;i++)
                {
                    if(chosePage==items[i].value)
                    {                        
                        document.getElementsByName("startlogin")[i].checked = true;
                    }
                }
            }

        </script>
    </head>
    <%
        //added by Dinanath
        Locale cle = null;
        cle = (Locale) session.getAttribute("UserLocaleFormat");
        PbDb pbdb = new PbDb();
        String userId = "";
        String  checkId = "";
        checkId = request.getParameter("checkUser");
        String from=request.getParameter("fromPage");
        userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
        String userprivis = "SELECT USER_ID, PRIVELEGE_ID FROM PRG_AR_USER_PRIVELEGES where USER_ID in(" + userId + ") and PRIVELEGE_ID in('Query Studio')";

        PbReturnObject userprivispbro = pbdb.execSelectSQL(userprivis);
        userprivispbro.writeString();
        Vector privis = new Vector();
        for (int i = 0; i < userprivispbro.getRowCount(); i++) {
            privis.add(userprivispbro.getFieldValueString(i, 1));
        }
        PbReturnObject Dlist = new ReportTemplateDAO().getAllDashsNavi(checkId);
        PbReturnObject Rlist = new ReportTemplateDAO().getAllrepsNavi(checkId);
        PbReturnObject Olist = new ReportTemplateDAO().getAllOneViewBys(checkId);
        PbReturnObject Ilist = new ReportTemplateDAO().getAllIcals(checkId);

        String getStartPageDetails = "select start_page from prg_ar_users where PU_ID in ("+checkId+")";
        PbReturnObject pbro = pbdb.execSelectSQL(getStartPageDetails);

        String chosePage = pbro.getFieldValueString(0,0);
        // Added by Prabal for checking Varection start Page
//        boolean startPageflag=true;
              String status = String.valueOf(session.getAttribute("status"));
//        startPageflag=  Boolean.parseBoolean((String)session.getAttribute("SSOAuthenticateFlag"));
    %>
    <body onload="showChosePage()"> 
        <% if (status.equalsIgnoreCase("OK")) {%>
        
         <div id="reportstart" >
         <form name="myFormH" method="post">
             <div style="height: 30px;">
                  
                  <a href="javascript:void(0)" onclick="javascript:gohome()" title="Go To Homepage"><i class="fa fa-home"></i>&nbsp;&nbsp;&nbsp;<%=TranslaterHelper.getTranslatedInLocale("home", cle)%></a>
             </div>
             <div class="tabsty" style="height:380px;width:100%">
                 <div class="startpage-Header" >
                     <%=TranslaterHelper.getTranslatedInLocale("Reports", cle)%>
                 </div>
                 <div style="border: 2px dotted green;">
                     <div id="Reports"   style="overflow-y:auto;overflow-x:hidden;height:350px;padding: 0px 10px;">
                                <%for (int i1 = 0; i1 < Rlist.getRowCount(); i1++) {
                                    if(i1%2==0){
                                %>
                                <div style="width: 49%;float: left;">
                                 <input type="radio" name="startlogin" id="startlogin" value="reportViewer.do?reportBy=viewReport&REPORTID=<%=Rlist.getFieldValueString(i1, 0)%>"> <font > <%=Rlist.getFieldValueString(i1, 1)%></font>
                               </div>
                                   <%}else{%> 
                                <div style="width: 49%;float: left;">
                                 <input type="radio" name="startlogin" id="startlogin" value="reportViewer.do?reportBy=viewReport&REPORTID=<%=Rlist.getFieldValueString(i1, 0)%>"> <font > <%=Rlist.getFieldValueString(i1, 1)%></font>
                                </div>
                                <%}}%>
                           
                        </div>
                 </div>
             </div>  
            <input type="hidden" value="<%=userId%>" id="userId" name="userId">
            <input type="hidden" value="<%=checkId%>" id="checkId" name="checkId">
            <input type="hidden" value="landingPage.jsp" id="chosePage" name="chosePage">
            <div style="height: 30px ;padding: 5px; text-align: center;margin-top: 10px;">
                <input type="button" class="navtitle-hoverGreen" style="width:auto" value="save" onclick="saveStart('<%=from%>')">
            </div>  
        </form>
    </div>
        
        <%}else{%>
    <div id="reportstart" >
        <form name="myFormH" method="post">
            <table width="100%" height="4%">
                <tr style="width:100%">
                    <td style="height:10%;width:20%">
                        <i class="fa fa-home"></i>
                        <a href="javascript:void(0)" onclick="javascript:gohome()">&nbsp;<%=TranslaterHelper.getTranslatedInLocale("home", cle)%></a>
                    </td>
                </tr>
            </table>
            <table border="1px solid " class="tabsty" align="center" width="100%" style="height:320px">
                <tr style="width:auto;">
                    <td  bgcolor="silver" width="auto">
                        <center>
                            <div class="prgtableheader2" ><font><b><%=TranslaterHelper.getTranslatedInLocale("Tabs", cle)%></b></font></div>
                        </center>
                    </td>
                    <td bgcolor="silver" width="auto">
                        <center>
                            <div class="prgtableheader2" ><font><b><%=TranslaterHelper.getTranslatedInLocale("Reports", cle)%></b></font></div>
                        </center>
                    </td>
                    <td bgcolor="silver" width="auto">
                        <center>
                            <div class="prgtableheader2" ><font><b><%=TranslaterHelper.getTranslatedInLocale("Dashboards", cle)%></b></font></div>
                        </center>
                    </td>
                    <td bgcolor="silver" width="auto">
                        <center>
                            <div class="prgtableheader2" ><font><b><%=TranslaterHelper.getTranslatedInLocale("One_Views", cle)%></b></font></div>
                        </center>
                    </td>
                    <td bgcolor="silver" width="auto">
                        <center>
                            <div class="prgtableheader2" ><font><b><%=TranslaterHelper.getTranslatedInLocale("I_cals", cle)%></b></font></div>
                        </center>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <div  id="Tabs" style="overflow-y:auto;overflow-x:hidden;height:320px;">
                            <table>
                                <!--<tr><td><input type="radio" name="startlogin" id="startlogin" value="home.jsp#RolesTab">&nbsp;<font >Assigned Business Roles</font></td></tr>
                                <tr><td><input type="radio" name="startlogin" id="startlogin" value="reportTemplateAction.do?templateParam=getAllReportshome">&nbsp;<font >All Reports</font></td></tr>
                                <tr><td><input type="radio" name="startlogin" id="startlogin" value="home.jsp#AdminTab">&nbsp;<font >Administrator</font></td></tr>
                                <tr><td><input type="radio" name="startlogin" id="startlogin" value="reportTemplateAction.do?templateParam=getAllDashs">&nbsp;<font >Dashboard Studio</font></td></tr>
                                <tr><td><input type="radio" name="startlogin" id="startlogin" value="reportTemplateAction.do?templateParam=getAllreps">&nbsp;<font >Report Studio</font></td></tr>
                                <tr><td><input type="radio" name="startlogin" id="startlogin" value="home.jsp#QryTab">&nbsp;<font >Query Studio</font></td></tr>
                                <tr><td><input type="radio" name="startlogin" id="startlogin" value="home.jsp#MsgTab">&nbsp;<font >Messages</font></td></tr>
                                <tr><td><input type="radio" name="startlogin" id="startlogin" value="home.jsp#AlrtTab">&nbsp;<font >Alerts</font></td></tr>-->
                                <tr><td><input type="radio" name="startlogin" id="startlogin" value="home.jsp">&nbsp;<font >Home</font></td></tr>
                                <tr><td><input type="radio" name="startlogin" id="startlogin" value="landingPage.jsp">&nbsp;<font >Landing Page</font></td></tr>
                                <tr><td><input type="radio" name="startlogin" id="startlogin" value="pbBase.jsp#Database_Connection">&nbsp;<font >Database Connec..</font></td></tr>
                                <tr><td><input type="radio" name="startlogin" id="startlogin" value="pbBase.jsp#Dimensions">&nbsp;<font >Dimensions</font></td></tr>
                                <tr><td><input type="radio" name="startlogin" id="startlogin" value="pbBase.jsp#Time_SetUp">&nbsp;<font >Time SetUp</font></td></tr>
                                <tr><td><input type="radio" name="startlogin" id="startlogin" value="pbBase.jsp#Business_Groups">&nbsp;<font >Business Groups</font></td></tr>
                                <tr><td><input type="radio" name="startlogin" id="startlogin" value="pbBase.jsp#Business_Roles">&nbsp;<font >Business Roles</font></td></tr>
                                <tr><td><input type="radio" name="startlogin" id="startlogin" value="newHome.jsp">&nbsp;<font >Favourite Home Page</font></td></tr>

                            </table>
                        </div>
                    </td>
                    <td valign="top">
                        <div id="Reports"   style="overflow-y:auto;overflow-x:hidden;height:320px;">
                            
                            <table>
                                <%for (int i1 = 0; i1 < Rlist.getRowCount(); i1++) {%>
                                <tr>
                                    <td valign="top">
                                        <input type="radio" name="startlogin" id="startlogin" value="reportViewer.do?reportBy=viewReport&REPORTID=<%=Rlist.getFieldValueString(i1, 0)%>"> <font > <%=Rlist.getFieldValueString(i1, 1)%></font>
                                    </td>
                                </tr>
                                <%}%>
                            </table>
                        </div>

                    </td>
                    <td valign="top">
                        <div id="Dashs"   style="overflow-y:auto;overflow-x:hidden;height:320px">
                        
                            <table style="overflow:auto">
                                 <%if(Dlist!=null){
                                for (int i2 = 0; i2 < Dlist.getRowCount(); i2++) {%>
                                <tr>
                                    <td>
                                        <input type="radio" name="startlogin" id="startlogin" value="dashboardViewer.do?reportBy=viewDashboard&REPORTID=<%=Dlist.getFieldValueString(i2, 0)%>&pagename=<%=Dlist.getFieldValueString(i2, 1)%>"> <font > <%=Dlist.getFieldValueString(i2, 1)%></font>
                                    </td>
                                </tr>
                               <%}}%>
                            </table>
                        </div>
                    </td>
                    <td>
                        <div id="Dashs"   style="overflow-y:auto;overflow-x:hidden;height:320px">
                        <table>
                                <%if(Olist!=null){
                                for (int i1 = 0; i1 < Olist.getRowCount(); i1++) {%>
                                <tr>
                                    <td valign="top">
                                        <input type="radio" name="startlogin" id="startlogin" value="srchQueryAction.do?srchParam=oneViewBy&homeFlag=true&oneViewId=<%=Olist.getFieldValueString(i1, 0)%>&oneviewname=<%=Olist.getFieldValueString(i1, 4)%>"> <font > <%=Olist.getFieldValueString(i1, 4)%></font>
                                    </td>
                                </tr>
                                <%}}%>
                            </table>
                            </div>
                    </td>
                    <td>
                        <div id="Dashs"   style="overflow-y:auto;overflow-x:hidden;height:320px">
                        <table>
                                <%if(Ilist!=null){
                               for (int i1 = 0; i1 < Ilist.getRowCount(); i1++) {%>
                                <tr>
                                    <td valign="top">
                                        <input type="radio" name="startlogin" id="startlogin" value="srchQueryAction.do?srchParam=icalPage&icalhomeFlag=true&icalId=<%=Ilist.getFieldValueString(i1, 0)%>&icalName=<%=Ilist.getFieldValueString(i1, 1)%>"> <font > <%=Ilist.getFieldValueString(i1, 1)%></font>
                                    </td>
                                </tr>
                                <%}}%>
                            </table>
                            </div>
                    </td>
                </tr>
            </table>
            <input type="hidden" value="<%=userId%>" id="userId" name="userId">
            <input type="hidden" value="<%=checkId%>" id="checkId" name="checkId">
                <input type="hidden" value="<%=chosePage%>" id="chosePage" name="chosePage">
                <br/>
            <center><input type="button" class="navtitle-hoverGreen" style="width:auto" value="save" onclick="saveStart('<%=from%>')"></center>
        </form>
    </div>
            <%}%>
    </body>
</html>
