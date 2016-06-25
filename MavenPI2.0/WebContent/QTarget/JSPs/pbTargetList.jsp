<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="prg.targetparam.qdparams.PbTargetParamParams"%>
<%@page import="prg.targetparam.qdclient.PbTargetParamManager"%>
<%@page import="prg.db.Session"%>
<%@page import="prg.db.PbReturnObject"%>
<%@ page import="utils.db.*"%>
<%@ page import="java.util.ArrayList"%>


<html>
    <head>


        <script type="text/javascript" src="<%=request.getContextPath()%>/QTarget/JS/myScripts.js"></script>

        <%--  <script src="<%=request.getContextPath()%>/jQuery/jquery/jquery-1.3.2.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.tabs.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.tablesorter.js"></script>

        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/demos.css" rel="stylesheet" />--%>
        <link href="<%=request.getContextPath()%>/stylesheets/StyleSheet.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/style.css" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <script type="text/javascript" src="javascript/ui.dialog.js"></script>


        <style type="text/css">
            .fontsty{
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                background-color:#bdbdbd;
            }
            .tabsty{
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
            }
            a {font-family:Verdana;cursor:pointer;}

            .white_content {
                position: absolute;
                top: 22%;
                left: 37%;
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
                -moz-opacity: 0.8;
                opacity:.50;
                filter:alpha(opacity=60);
                overflow:auto;
            }

        </style>

        <script type="text/javascript">
            function initialogTypeTarget(){
                if ($.browser.msie == true){
                    $("#targetDialog").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 550,
                        position: 'justify',
                        modal: true
                    });
                    $("#editTarget").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                }
                else{
                    $("#targetDialog").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 550,
                        position: 'justify',
                        modal: true
                    });
                    $("#editTarget").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                }
            }

            function updateClosePublish()
            {
                document.ec.action = '<%=request.getContextPath()%>'+"/home.jsp#Targets";
                document.ec.submit();
                alert("Target published updated successfuly");
            }
            function updateCloseEdit()
            {
                document.ec.action = '<%=request.getContextPath()%>'+"/home.jsp#Targets";
                document.ec.submit();
                alert("Target updated successfuly")
            }

            function createTarget()
            {
                //document.getElementById("fade").style.display = 'block';
                //document.getElementById("createTargetDisp").style.display = 'block';

                //document.getElementById("createTargetDisp").src = "pbTargetMaster.jsp";
                initialogTypeTarget();
                var frameObj = document.getElementById("createTargetDisp");
                frameObj.src="<%=request.getContextPath()%>/QTarget/JSPs/pbTargetMasterFromList.jsp";
                $("#targetDialog").dialog('open');

            }
            function cancelTarget()
            {
                 $("#targetDialog").dialog('close');
               // document.getElementById("fade").style.display = 'none';
               // document.getElementById("createTargetDisp").style.display = 'none';
            }
            function cancelEditTarget()
            {
                document.getElementById("fade").style.display = 'none';
                document.getElementById("editTargetDisp").style.display = 'none';
            }
            function cancelCopyTarget()
            {
                document.getElementById("fade").style.display = 'none';
                document.getElementById("copyTargetDisp").style.display = 'none';
            }



            $(function() {
                // $("#tablesorter").tablesorter({widthFixed: true, widgets: ['zebra']})
                $("#tablesorter").tablesorter({headers : {0:{sorter:false}}})
            });


            function gouser(){
                //alert('hi')
                document.forms.myFormH.action="userList.jsp";
                document.forms.myFormH.submit();
            }
            function goTest(){
                alert("hello")

            }
            function logout(){
                var path = '<%=request.getContextPath()%>';
                document.forms.ec.action=path+"/baseAction.do?param=logoutApplication";
                document.forms.ec.submit();
            }
            function gohome(){
                var path = '<%=request.getContextPath()%>';
                document.forms.ec.action=path+"/baseAction.do?param=goHome";
                document.forms.ec.submit();
            }
        </script>
    </head>
    <%

        int rowCount = 0;

        PbTargetParamParams targetParams = new PbTargetParamParams();
        PbTargetParamManager targetClient = new PbTargetParamManager();
        Session targetSession = new Session();
        String userId = request.getParameter("userId");
        if (userId == null) {
            userId = (String) session.getAttribute("USERID");
        //userId="41";
        }
        // userId="41";
        ////////////////////////.println("userId //-- " + userId);
        targetParams.setUserId(userId); //get it from User Session
        session.setAttribute("userId", userId);
        targetSession.setObject(targetParams);
        PbReturnObject targetList = targetClient.getTargetsListU(targetSession);
        //////////////////////////////////////////.println(targetList.getRowCount()+" in list  userId =-= -- "+userId);

        rowCount = targetList.getRowCount();
    %>
    <Center>
        <form action=""  name="ec" method="post">
            <%--  <table>
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
            </table> --%>
            <%--<table width="100%" class="ui-corner-all">
                <tr>
                    <td valign="top" style="height:10px;width:10%" align="right">
                        <a href="javascript:void(0)" onclick="javascript:gohome()" style="font-size:10px;color:#369;font-weight:bold;text-decoration: none;font-family:Georgia"> Home </a> |
                        <a href="#" style="font-size:10px;color:#369;font-weight:bold;text-decoration: none;font-family:Georgia"> Help </a> |
                        <a href="javascript:void(0)" onclick="javascript:logout()" style="font-size:10px;color:#369;font-weight:bold;text-decoration: none;font-family:Georgia"> Logout </a>
                    </td>
                </tr>
            </table> --%>
             <%--<div class="ui-tabs ui-widget ui-widget-content ui-corner-all" style="width:100%;min-height:500px;max-height:100%">
               <table style="width:100%">
                    <tr>
                        <td>
                            <div style="height:33px" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all ui-sortable">
                                Target List
                            </div>
                        </td>
                    </tr>
            </table>--%>

            <center>
                <Table id="tablesorter" STYLE="width:95%" CLASS="tablesorter">
                    <%--
                       <colgroup>
                           <col style="width:30px">
                           <col style="width:300px">
                           <col style="width:200px">
                           <col style="width:200px">
                           <col style="width:200px">
                           <col style="width:200px">
                    </colgroup>--%>
                    <thead>
                        <tr>
                            <th style="width:10px"></th>
                            <th >Target Name</th>

                            <th >Target Starts On</th>
                            <th >Target Ends On</th>
                            <th >Target Status</th>

                            <th >Top Time Level</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
        String str = "";
        for (int p = 0; p < targetList.getRowCount(); p++) {
            String viewUrl = "";//pbViewTargetData.jsp?targetId="+targetList.getFieldValueString(p,"TARGET_ID");
            String targetId = targetList.getFieldValueString(p, "TARGET_ID");
            String timeLevel = targetList.getFieldValueString(p, "MIN_TIME_LEVEL");
            viewUrl = request.getContextPath() + "/targetView.do?targetParams=viewTargetForView&targetId=" + targetId + "&minTimeLevel=" + timeLevel;
            if ((p % 2) == 0) {
                str = "even";
            } else {
                str = "odd";
            }
                        %>

                        <Tr CLASS="<%=str%>">
                            <Td><Input type="checkbox" name="chk1" VALUE="<%=targetList.getFieldValueString(p, "TARGET_ID")%>"></Td>
                            <Td><a href=<%=viewUrl%> style="color:black;font-size:11px;font-family:arial;color:#3D3D3D;"><%=targetList.getFieldValueString(p, "TARGET_NAME")%></a></Td>

                            <td><%=targetList.getFieldValueString(p, "ST_DATE")%></td>
                            <td><%=targetList.getFieldValueString(p, "END_DATE")%></td>
                            <td><%=targetList.getFieldValueString(p, "TARGET_STATUS")%></td>

                            <td><%=targetList.getFieldValueString(p, "MIN_TIME_LEVEL")%></td>
                        </Tr>
                        <%}%>
                    </tbody>
                </Table>
                <Br>
                <%
        if (rowCount == 0) {
                %>
                <table>
                    <%--  <tr>
                            <td>
                                <Input class="btn" type="button" value="Create" onclick="javascript:createTarget();">
                            </td>
                    </tr> --%>
                </table>
                <%                } else {
                %>
                <Table>
                    <Tr>
                        <%--<Td><Input class="btn" type="button" value="Home" onclick="javascript:goToUserHome();"></Td>--%>
                        <Td><Input type="button" class="navtitle-hover" value="Create" onclick="javascript:createTarget();"></Td>
                        <Td><Input type="button" class="navtitle-hover" value="Edit" onclick="javascript:editTarget();"></Td>
                        <Td><Input type="button" class="navtitle-hover" value="Delete" onclick="javascript:deleteTarget();"></Td>
                        <Td><Input type="button" class="navtitle-hover" value="Enter Data" onclick="javascript:defineTarget('<%=request.getContextPath()%>');"></Td>
                    </Tr>
                </Table>
                <Table>
                    <Tr>
                        <%--<Td><Input class="btn" type="button" value="Update" onclick="javascript:updateTarget();"></Td>--%>
                        <%--<Td><Input class="btn" type="button" value="Delete" onclick="javascript:deleteTarget();"></Td>--%>
                        <%--<Td><Input type="button" class="navtitle-hover" value="Copy" onclick="javascript:copyTarget();"></Td>--%>
                        <Td><Input type="button"  class="navtitle-hover" value="Publish" onclick="javascript:publishTarget();"></Td>
                        <Td><Input type="button" class="navtitle-hover" value="Expire" onclick="javascript:expireTarget();"></Td>
                    </Tr>
                    <input type="hidden" name="targetNames" id="targetNames">
                </Table>
                <%
        }
                %>
            </center>

            <%

        try {

            int measureId = targetList.getFieldValueInt(0, "MEASURE_ID");

            session.setAttribute("measureId", Integer.valueOf(measureId));
            if (session.getAttribute("measureId") != null) ////////////////////////////////////////////////////////////////////////.println(" --- "+((Integer)session.getAttribute("measureId")).intValue());
            {
                targetClient.deleteLock(userId);
            }


            rowCount = targetList.getRowCount();
        ////////////////////////////////////////////////////////////////////////.println("RowCount is: "+rowCount);

        ////////////////////////////////////////////////////////////////////////.println("context path is: "+request.getContextPath());


            %>



            <%

        } catch (Exception ex) {
            ////////////////////////////////////////////////////////////////////////.println(ex.getMessage());
        }
            %>


            <br>

            <%--<table width="100%" class="fontsty">
                <tr style="height:10px;width:100%;max-height:100%;background-color:#bdbdbd">
                    <td style="height:10px;width:100%;background-color:#bdbdbd">
                        <center ><font  style="color:#fff;font-size:10px;font-family:verdana;" align="center">Copyright © 2009-12 <a href="http://www.progenbusiness.com" style="color:red;font-weight:bold">Progen Business Solutions.</a> All Rights Reserved</font></center>
                    </td>
                </tr>
            </table>--%>

            <div id="fade" class="black_overlay" style="display:none"></div>
            <div id="targetDialog">
                <iframe  id="createTargetDisp" NAME='createTargetDisp'  width="100%" height="100%" frameborder="0" SRC=''></iframe>
            </div>

           <div id="editTarget" title="Edit Target" style="display:none">
                <iframe  id="editTargetDisp" NAME='editTargetDisp'  width="100%" height="100%" frameborder="0" SRC=''></iframe>
            </div> 

           <%-- <div id="editTargetDisp" title="Edit Target" style="display:none">
                <iframe  id="createTargetDisp" NAME='createTargetDisp'  width="100%" height="100%" frameborder="0" SRC=''></iframe>
            </div> --%>

            <div id="copyTarget" style="display:none">
                <iframe  id="copyTargetDisp" NAME='copyTargetDisp' width="100%" height="100%" frameborder="0" SRC=''></iframe>
            </div>
            <input type="hidden" name="path" id="path" value="<%=request.getContextPath()%>">
        </form>
    </Center>


    </body>
</html>
