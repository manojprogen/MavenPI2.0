<%-- 
    Document   : createOrganisation
    Created on : Dec 23, 2009, 5:22:30 PM
    Author     : Saurabh
--%>

<%@page import="java.util.Calendar" contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
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
        <title>Organisation Page</title>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
<!--        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />-->
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/ui.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/ui.datepicker.js"></script>
        <style type="text/css">
            *{
                font:12px verdana;
            }
            .leftcol {
                width:60%;
            }
            .fontsty{
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                background-color:#bdbdbd;
            }
            .ui-corner-all{
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
            }
        </style>
       
        <%
        Calendar calendar =Calendar.getInstance();
        String accountFolderId =request.getParameter("accountFolderId");
        ////////////////////////////////////////////////////////.println.println(" accountFolderId in jsp "+accountFolderId);
%>
    
</head>
<body>
    <form name="myForm" method="post" >

       
        <div class="ui-corner-all"  style="width:100%;border:0px solid silver" align="center">
            <br><br><br><br>
            <center>
                <div id="leftcol" class='leftcol'  align="center">
                    <center>
                        <font size="1px"> Fields marked <span style="color:red">*</span> are MANDATORY </font></center>
                        <br><br>
                    <table width="100%">
                        <tr width="100%">
                        <table width="100%"><tr>
                                <td width="50%"><label class="label"><span style="color:red">*</span>Account Name</label></td>
                                <td width="50%"><input type="text" name="orgName" id="orgName" style="width:90%" title="Enter Account Name(Min. 6 Characters)"></td>
                            </tr></table>
                        </tr>

                        <tr width="100%">
                        <table width="100%"><tr>
                                <td width="50%">&nbsp;&nbsp;<label class="label">Description</label></td>
                                <td width="50%"><input type="text" style="width:90%" name="orgDesc"  id="orgDesc" title="Enter Organisation Description"></td>
                            </tr></table>
                        </tr>

                        <tr width="100%">
                        <table width="100%"><tr>
                                <td width="50%">&nbsp;&nbsp;<label class="label">Start Date</label></td>
                                <td width="50%"><input type="text" readonly style="width:90%" name="strtDate" id="startDate" value="<%=(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.DATE)+"/"+calendar.get(Calendar.YEAR)%>"></td>
                            </tr></table>
                        </tr>
                        <tr width="100%">
                        <table width="100%"><tr>
                                <td width="50%"><label class="label"><span style="color:red">*</span>End Date</label></td>
                                <td width="50%"><input type="text" readonly style="width:90%" name="endDate" id="endDate" ></td>
                            </tr></table>
                        </tr>
                    </table>
                    <br><br>
                    <table align="center">
                        <tr>
                           <%-- <td>
                                <input type="button" class="navtitle-hover" style="width:auto"   id="cancel" value="Cancel" onclick="goBack();" >
                            </td>--%>
                            <td>
                                <input type="button" class="navtitle-hover" style="width:auto" id="Save" value="Save" onclick="return validateOrg();" >
                            </td>

                            <td>
                                <input type="reset" class="navtitle-hover" style="width:auto"  id="reset" value="Reset"  >
                            </td>
                        </tr>
                    </table>

                </div>
            </center>
        </div>
                            <INPUT type="hidden" name="accountFolderId" id="accountFolderId" value="<%=accountFolderId%>">
    </form>
    <br>
    
 <script>
            $(document).ready(function(){
                $("#endDate").datepicker();
            });
            function logout(){
                document.forms.myForm.action="baseAction.do?param=logoutApplication";
                document.forms.myForm.submit();
            }
            function gohome(){
                document.forms.myForm.action="baseAction.do?param=goHome";
                document.forms.myForm.submit();
            }
            function validateOrg(){
                var orgName = document.myForm.orgName.value;
                var endDate = document.myForm.endDate.value;
                if(orgName == null ||orgName =="")
                {
                    alert("Please Enter Organisation Name");
                    return false;
                }
                else if(endDate == null ||endDate =="")
                {
                    alert("Please Enter End Date");
                    return false;
                }
                else{
                    parent.closeAccount();
                    document.myForm.action = "organisationDetails.do?param=saveOrg";
                    document.myForm.submit();
                    refreshAccount();
                }
            }
            function refreshAccount()
            {
                parent.refreshAccountParent();
            }


        </script>
</body>
</html>
