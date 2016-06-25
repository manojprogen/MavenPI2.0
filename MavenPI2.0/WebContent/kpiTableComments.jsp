<%--
    Document   : kpiTableComments
    Created on : Jan 16, 2010, 9:07:49 PM
    Author     : mahesh.sanampudi@progenbusiness.com
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.List,java.util.List,com.progen.report.entities.KPI,com.progen.reportdesigner.db.ReportTemplateDAO,com.progen.reportdesigner.db.ReportTemplateDAO,java.io.ObjectInputStream,java.io.FileInputStream"%>
<%@page import="utils.db.ProgenConnection,prg.db.*,java.util.HashMap,com.progen.report.pbDashboardCollection,com.progen.report.DashletDetail,com.progen.report.entities.KPIComment"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String contextpath=request.getContextPath();
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>

        <script type="text/javascript" src="<%=contextpath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=contextpath%>/javascript/dashboardDesign.js"></script>
        <link type="text/css" href="<%=contextpath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <style type="text/css">
            *{font:11px verdana}
        </style>
       
    </head>
    <body>
        <%
        if(!Boolean.parseBoolean(request.getParameter("formOneview"))){
            String elmntId = request.getParameter("elementId");
            String masterId = request.getParameter("masterId");
            String dashboardId = request.getParameter("dashboardId");
            String kpiComment = request.getParameter("kpiComment");
            String kpiCommentFlag = request.getParameter("commentFlag");
            String dashletId = request.getParameter("dashletId");

        if (session != null && session.getAttribute("USERID") != null && session.getAttribute("PROGENTABLES") != null) {
            HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
            Container container = (Container) map.get(dashboardId);
            pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
            DashletDetail detail = collect.getDashletDetail(dashletId);
            KPI kpiDetails = (KPI)detail.getReportDetails();
            List<KPIComment> kpiComments = kpiDetails.getKPIComments(elmntId);
            String userId = "";
            userId = (String) session.getAttribute("USERID");
        %>
        <form name="myForm" method="post" >
            <table width="100%"  >
                <%if (!kpiCommentFlag.equalsIgnoreCase("Y")) {
                if (kpiComments != null && kpiComments.size() > 0) {%>
                <tr align="right" style="width:100%">
                    <td width="100%" align="right"><a href="javascript:void(0)" style="text-decoration:none;cursor:pointer" onclick="clearComments('<%=elmntId%>','<%=masterId%>','<%=userId%>', '<%=dashletId%>')">Clear all Comments</a></td>
                </tr>
                <tr style="width:100%">
                    <td width="100%" align="center">
                        <textarea id="existCommentArea" name="existCommentArea" cols="40" rows="9" readonly style="overflow:auto">
                            <%for (int m = 0; m < kpiComments.size(); m++) {%>
<%=kpiComments.get(m).getCommentDate().toString()%>:
<%=kpiComments.get(m).getComment()%>
                                <%}%>
                        </textarea>
                    </td>
                </tr>
                <tr style="width:100%">
                    <td width="100%" align="center"><textarea id="commentArea" name="commentArea" cols="40" rows="4" style="overflow:auto"></textarea></td>
                </tr>
                <%} else {%>
                <tr style="width:100%">
                    <td width="100%" align="center"><textarea id="commentArea" name="commentArea" cols="40" rows="9" style="overflow:auto"></textarea></td>
                </tr>
                <%}
} else {%>
                <tr style="width:100%">
                    <td width="100%" align="center"><textarea id="commentArea" name="commentArea" cols="40" rows="9" style="overflow:auto"></textarea></td>
                </tr>
                <%}%>
                <tr style="width:100%" align="center">
                    <td>
                    <table align="center"  width="100%">
                        <tr>
                            <td width="50%" align="center">
                                <center>
                                    <%
            if (kpiCommentFlag.equalsIgnoreCase("Y")) {
                                    %>
                                    <input type="button" class="navtitle-hover" value="Save" onclick="saveDbrdDesignComment('<%=elmntId%>','<%=masterId%>','<%=kpiComment%>')">
                                        <%} else {%>
                                    <input type="button" class="navtitle-hover" value="Save" onclick="saveDbrdComment('<%=elmntId%>','<%=masterId%>','<%=dashletId%>')">
                                    <%}%>
                                </center>
                            </td>
                        </tr>
                    </table>
                   </td>
                </tr>
            </table>
        </form>
        <%}}else{

            String oneViewIdValue = request.getParameter("oneViewIdValue");
            int colNo = Integer.parseInt(request.getParameter("colNo"));

            OnceViewContainer onecontainer = new OnceViewContainer();

             HashMap map = null;
            map = (HashMap) session.getAttribute("ONEVIEWDETAILS");
             List<KPIComment> kpiComments = null;
            if (map != null) {
            onecontainer = (OnceViewContainer) map.get(oneViewIdValue);

            OneViewLetDetails detail = new OneViewLetDetails();
            detail = onecontainer.onviewLetdetails.get(Integer.parseInt(request.getParameter("colNo")));
            kpiComments = detail.measureComments;

          } else {
            ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
//            onecontainer = reportTemplateDAO.getOneViewData(oneViewIdValue);

           // String fileName = reportTemplateDAO.getOneviewFileName(oneViewIdValue);
            String fileName=request.getSession(false).getAttribute("tempFileName").toString();
            FileInputStream fis2 = new FileInputStream(System.getProperty("java.io.tmpdir") + "/" + fileName);
            ObjectInputStream ois2 = new ObjectInputStream(fis2);
            onecontainer = (OnceViewContainer) ois2.readObject();
            ois2.close();

            
            OneViewLetDetails detail = new OneViewLetDetails();

            List<OneViewLetDetails> dashletDetails = onecontainer.onviewLetdetails;

            detail = dashletDetails.get(colNo);
             kpiComments = detail.measureComments;

        }

        %>
        <form name="myForm" method="post" >
            <table width="100%">
               <%if (kpiComments != null && !kpiComments.isEmpty()) {%>
                <tr align="right" style="width:100%">
                    <td  id="clearButtonId" width="100%" align="right"><a href="javascript:void(0)" style="text-decoration:none;cursor:pointer" onclick="clearcomments('<%=oneViewIdValue%>','<%=colNo%>')">Clear all Comments</a></td>
                </tr>
                <tr style="width:100%">
                    <td width="100%" align="center" id="commentexitsId">
                        <textarea id="existCommentArea1" name="existCommentArea1" cols="40" rows="9" readonly style="overflow:auto">
                        <%for (int m = 0; m < kpiComments.size(); m++) {%>
  <%=kpiComments.get(m).getCommentDate().toString()%>        <%=kpiComments.get(m).getUserName()%>
  <%=kpiComments.get(m).getComment()%>
                                <%}%>
                        </textarea>
                    </td>
                </tr>
                <tr style="width:100%">
                    <td width="100%" align="center"><textarea id="usertextareaId" name="commentArea" cols="40" rows="4" style="overflow:auto"></textarea></td>
                </tr>
                <%} else {%>
                <tr style="width:100%">
                    <td width="100%" align="center"><textarea id="usertextareaId" name="commentArea" cols="40" rows="9" style="overflow:auto"></textarea></td>
                </tr>
              <%}%>
                <tr style="width:100%" align="center">
                    <td>
                    <table align="center"  width="100%">
                        <tr  style="height:10px;" >
                        <td align="center">
                            <input type="button" name="" value="Save" onclick="commentsAreaValues('<%=oneViewIdValue%>','<%=colNo%>')">
                        </td>
<!--                        <td align="left">
                            <input type="button" name="" value="Clear" onclick="clearcomments()">
                        </td>-->
                    </tr>
                    </table>
                   </td>
                </tr>
            </table>
        </form>
        <%}%>
         <script type="text/javascript">

             function commentsAreaValues(oneViewIdValue,colNumber){
                var textarea=$("#usertextareaId").val();
                //                var currentTime = new Date()
                //                var month = currentTime.getMonth() + 01
                //                var day = currentTime.getDate()
                //                var year = currentTime.getFullYear()
                //                var minuts = currentTime.getMinutes()()
                //                var secondsar = currentTime.getSeconds()()
                //                var date=day + "/" + month + "/" + year+"/"+minuts+"/"+secondsar;
                $.post('<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=commentsData&oneViewIdValue='+oneViewIdValue+'&colNo='+colNumber+'&commentData='+textarea,
                function(data){
                });
                parent.$("#commentsAreaId").dialog('close');
            }
            function clearcomments(oneViewIdValue,colNumber){
                $("#existCommentArea1").val('');
                $("#commentexitsId").hide()
                $("#clearButtonId").hide()
                //                var currentTime = new Date()
                //                var month = currentTime.getMonth() + 01
                //                var day = currentTime.getDate()
                //                var year = currentTime.getFullYear()
                //                var minuts = currentTime.getMinutes()()
                //                var secondsar = currentTime.getSeconds()()
                //                var date=day + "/" + month + "/" + year+"/"+minuts+"/"+secondsar;
                $.post('<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=removeOneviewMeasComments&oneViewIdValue='+oneViewIdValue+'&colNo='+colNumber,
                function(data){
                });
//                parent.$("#commentsAreaId").dialog('close');
            }

        </script>
    </body>
</html>
