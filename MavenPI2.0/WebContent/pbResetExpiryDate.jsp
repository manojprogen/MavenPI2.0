<%-- 
    Document   : pbResetExpiryDate
    Created on : Apr 1, 2010, 4:11:57 PM
    Author     : Administrator
--%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">


<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.*,prg.db.PbDb,prg.db.PbReturnObject,utils.db.ProgenConnection" %>
<%
    String themeColor="blue";
        if(session.getAttribute("theme")==null)
                session.setAttribute("theme",themeColor);
            else
                themeColor=String.valueOf(session.getAttribute("theme"));
    String contextPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Reset ExpiryDate</title>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/ui.datepicker.js"></script>
<!--        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />-->
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
        <style type="text/css">
            *{font:12px verdana;font-size:12px;color:#369}
        </style>
        <script type="text/javascript">
        $(document).ready(function(){
                $("#newExpiry").datepicker();
            });
            </script>
        
    </head>
    <body>
        <%
                    String userFlag = "";
                    PbDb pbdb = new PbDb();
                    PbReturnObject sysDateObj = null;
                    PbReturnObject oldDatedispObj = null;
                    PbReturnObject oldDateObj = null;
                    String sysDateQry = "";
                    String sysdt = "";
                    String oldDateQry = "";
                    String oldDatedispQry = "";
                    String oldDatedisp = "";
                    String oldDate = "";
                    String userid = request.getParameter("userId");
                    String dbType = "";
                    if (session.getAttribute("MetadataDbType") != null) {
                        dbType = (String) session.getAttribute("MetadataDbType");
                    }
                    //.println("dbType in jsp is" + dbType);
                    if (!request.getParameter("userFlag").equalsIgnoreCase(null) || !"".equalsIgnoreCase(request.getParameter("userFlag"))) {
                        userFlag = request.getParameter("userFlag");
                    }
                    if (dbType.equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                        sysDateQry = "select getdate()";
                    } else {
                        sysDateQry = "select sysdate from dual";
                    }
                    sysDateObj = pbdb.execSelectSQL(sysDateQry);
                    if (sysDateObj.getRowCount() > 0) {
                        sysdt = sysDateObj.getFieldValueString(0, 0);
                    }
                    if (userFlag.equalsIgnoreCase("Y")) {
                        oldDateQry = "select PU_END_DATE from prg_ar_users where pu_id=" + userid;
                        oldDateObj = pbdb.execSelectSQL(oldDateQry);
                        if (oldDateObj.getRowCount() > 0) {
                            oldDate = oldDateObj.getFieldValueDateString(0, 0);
                            if (dbType.equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                                oldDatedispQry = "select convert(varchar,convert(datetime,'" + oldDate + "'),101)";
                            } else {
                                oldDatedispQry = "select to_char(to_date('" + oldDate + "'),'mm/dd/yyyy') from dual";
                            }

                            oldDatedispObj = pbdb.execSelectSQL(oldDatedispQry);
                            if (oldDatedispObj.getRowCount() > 0) {
                                oldDatedisp = oldDatedispObj.getFieldValueString(0, 0);
                            }
                        }
                    } else {
                        oldDateQry = "select ORG_END_DATE from prg_org_master where org_id=" + userid;
                        oldDateObj = pbdb.execSelectSQL(oldDateQry);
                        if (oldDateObj.getRowCount() > 0) {
                            oldDate = oldDateObj.getFieldValueDateString(0, 0);
                            if (dbType.equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                                oldDatedispQry = "select convert(varchar,convert(datetime,'" + oldDate + "'),101)";
                            } else {
                                oldDatedispQry = "select to_char(to_date('" + oldDate + "'),'mm/dd/yyyy') from dual";
                            }

                            oldDatedispObj = pbdb.execSelectSQL(oldDatedispQry);
                            if (oldDatedispObj.getRowCount() > 0) {
                                oldDatedisp = oldDatedispObj.getFieldValueString(0, 0);
                            }
                        }
                    }
                    ////.println("userid is : " + userid);
                    ////.println("userFlag is : " + userFlag);
                    ////.println("oldDate is : " + oldDate);
                    ////.println("sysdt is : " + sysdt);
                    ////.println("oldDateObj.getFieldValueString(0, 0) is : " + oldDateObj.getFieldValueString(0, 0));
                    ////.println("oldDatedisp is : " + oldDatedisp);
%>
        <form action="javascript:void(0)" method="post" onsubmit="return validateExpiryDate()">
            <table>
                <tr>
                    <td width="50%">&nbsp;&nbsp;<label class="label">Old ExpiryDate</label></td>
                    <td width="50%"><input type="text" readonly name="oldExpiry" id="oldExpiry" value="<%=oldDatedisp%>"></td>
                </tr>
                <tr>
                    <td width="50%">&nbsp;&nbsp;<label class="label">New ExpiryDate</label></td>
                    <td width="50%"><input type="text" readonly id="newExpiry" name="newExpiry"></td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <input class="navtitle-hover" style="width:auto" type="submit" value="Reset ExpiryDate" >
                    </td>
                </tr>
            </table>
            <input type="hidden" name="userid" id="userid" value="<%=userid%>">
            <input type="hidden" name="sysdt" id="sysdt" value="<%=sysdt%>">
            <input type="hidden" name="userFlag" id="userFlag" value="<%=userFlag%>">
        </form>
        <script type="text/javascript">
            
            function validateExpiryDate(){
                var oldexpDate = document.getElementById("oldExpiry").value;
                var newexpDate = document.getElementById("newExpiry").value;
                var newexpiryDate = new Date(document.getElementById("newExpiry").value);
                var userid = document.getElementById("userid").value;
                var sysdt = new Date(document.getElementById("sysdt").value);
                var userFlag = document.getElementById("userFlag").value;
            <%--alert("newexpDate is : "+newexpDate);
            alert("sysdt is : "+sysdt);
            alert("newexpiryDate is : "+newexpiryDate);--%>
                    if(newexpiryDate<sysdt){
                        alert('Expiry date should be greater than or Equal to Current Date')
                        return false;
                    }else{
                        $.ajax({
                            url: 'organisationDetails.do?param=validateExpiryDate&userId='+userid+'&newExpDate='+newexpDate+'&userFlag='+userFlag,
                            success: function(data){
            <%--alert('in data userflag is : '+userFlag)--%>
                                if(userFlag=='Y'){
                                    parent.document.forms.myForm.action = "<%=request.getContextPath()%>/AdminTab.jsp#User_Creation";
                                    parent.document.forms.myForm.submit();
                                }else{
                                    parent.document.forms.myFormH.action = "<%=request.getContextPath()%>/AdminTab.jsp#User_Accounts";
                                    parent.document.forms.myFormH.submit();
                                }
                            }
                        });
                    }
                }
        </script>
    </body>
</html>

