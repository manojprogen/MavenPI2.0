<%@page import="prg.targetparam.qdparams.PbTargetParamParams"%>
<%@page import="prg.targetparam.qdclient.PbTargetParamManager"%>
<%@page import="prg.db.Session"%>
<%@page import="prg.db.PbReturnObject"%>
<%@ page import="utils.db.*"%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <Script language="javascript"  src="../JS/myScripts.js"></Script>
         <link href="../css/myStyles.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/style.css" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/demos.css" rel="stylesheet" />
        <link href="<%=request.getContextPath()%>/stylesheets/StyleSheet.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/jquery.treeview.css" />

         <script type="text/javascript">
          function logout(){
                var path = '<%=request.getContextPath()%>';
                document.forms.myForm.action=path+"/baseAction.do?param=logoutApplication";
                document.forms.myForm.submit();
            }
            function gohome(){
                var path = '<%=request.getContextPath()%>';
                document.forms.myForm.action=path+"/baseAction.do?param=goHome";
                document.forms.myForm.submit();
            }
                </script>
    </head>
    <body>

<%
        PbTargetParamParams targetParams = new PbTargetParamParams();
        PbTargetParamManager targetClient = new PbTargetParamManager();
        Session targetSession = new Session();

        String targetId = request.getParameter("chk1");
        if(targetId==null)
        {
            targetId = (String)session.getAttribute("TI");
        }
        ////////////////////////////////////////////////////////////////////////.println("targetId in display warning page is: "+targetId);

        targetParams.setTargetId(targetId);
        targetSession.setObject(targetParams);
        PbReturnObject getLock = targetClient.getLock(targetSession);
        int count = getLock.getRowCount();
        ////////////////////////////////////////////////////////////////////////.println("lock count is:::: "+count);

        String userId = null;
        String userName = null;
        if(count != 0)
        {
            userId = String.valueOf(getLock.getFieldValueInt(0,"USER_ID"));
            ////////////////////////////////////////////////////////////////////////.println("userId in display warning page is: "+userId);
        
            targetParams.setUserId(userId);
            targetSession.setObject(targetParams);
            PbReturnObject getUserDetails = targetClient.getUserDetails(targetSession);
            int rowCount = getUserDetails.getRowCount();
            ////////////////////////////////////////////////////////////////////////.println("rowCount in display warning is: "+rowCount);


            userName = getUserDetails.getFieldValueString(0,"PU_LOGIN_ID");
            ////////////////////////////////////////////////////////////////////////.println("userName is: "+userName);
        }

%>
<center>
     <form name="myForm" method="post">
             <table>
                <tr>
                    <td valign="top" style="height:30px;width:8%;">
                        <img width="100%" height="100%"  title="pi " src="<%=request.getContextPath()%>/images/pi_logo.gif"/>
                    </td>
                    <td valign="top" style="height:30px;" align="right">

                    </td>
                    <td valign="top" style="height:30px;width:8%;">
                        <img width="100%" height="100%"  title="Progen Business Solutions" src="<%=request.getContextPath()%>/images/ProGen_Logo.jpg"/>
                    </td>
                </tr>
            </table>
            <table width="100%" class="ui-corner-all">
                <tr>
                    <td valign="top" style="height:10px;width:10%" align="right">
                        <a href="javascript:void(0)" onclick="javascript:gohome()" style="font-size:10px;color:#369;font-weight:bold;text-decoration: none;font-family:Georgia"> Home </a> |
                        <a href="#" style="font-size:10px;color:#369;font-weight:bold;text-decoration: none;font-family:Georgia"> Help </a> |
                        <a href="javascript:void(0)" onclick="javascript:logout()" style="font-size:10px;color:#369;font-weight:bold;text-decoration: none;font-family:Georgia"> Logout </a>
                    </td>
                </tr>
            </table>
            <div class="ui-tabs ui-widget ui-widget-content ui-corner-all" style="width:100%;min-height:500px;max-height:100%">
                <table style="width:100%">
                    <tr>
                        <td>
                            <div style="height:33px" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all ui-sortable">
                                Target List
                            </div>
                        </td>
                    </tr>
                </table>

       
            <table>
                <tr>
                    <td align="center">'<%=userName%>' is accessing.</td>
                </tr>
                <tr>
                    <td>Click 'Ok' to go to targets home page.</td>
                </tr>
            </table>
            <br>
            <table>
                <input type="button" value="Ok" onclick="goToTargetsHome2()">
            </table>
              </div>

            <table width="100%" class="fontsty">
                <tr style="height:10px;width:100%;max-height:100%;background-color:#bdbdbd">
                    <td style="height:10px;width:100%;background-color:#bdbdbd">
                        <center ><font  style="color:#fff;font-size:10px;font-family:verdana;" align="center">Copyright © 2009-12 <a href="http://www.progenbusiness.com" style="color:red;font-weight:bold">Progen Business Solutions.</a> All Rights Reserved</font></center>
                    </td>
                </tr>
            </table>
        </form>
</center>


    </body>
</html>
