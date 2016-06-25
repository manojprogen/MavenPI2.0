<%
String themeColor = "blue";
String scoreCard = request.getParameter("scoreCardId");
String member = request.getParameter("scoreCardMemberId");
String score = request.getParameter("score");
String pastOrNew = request.getParameter("pastOrNew");
String action = request.getParameter("action");
String elementName = request.getParameter("elementName");
String contextPath=request.getContextPath();
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/ReportCss.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/pbReportViewerCSS.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/css/css.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/css/style.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/newDashboard.css" rel="stylesheet" />
        <link rel="stylesheet" type="text/css" href="<%=contextPath%>/stylesheets/tablesorterStyle.css" />
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/BreadCrumb.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />

        <link href="<%=contextPath%>/reportDesigner.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/dashBoardViewer.css" type="text/css" rel="stylesheet">

       

    </head>
    <body>
        <%

            if ("new".equalsIgnoreCase(pastOrNew)){
                if ("email".equalsIgnoreCase(action)){
                    %>
                    <table border="0" width="100%">
                         <tr>
                            <Td class="myhead" width="40%">Start Date </Td>
                            <Td width="40%"><Input type="text" readonly class="myTextbox5" name="startdate" id="stDatepicker" maxlength=100  style="width:120px" value="" ></Td>
                            <Td width="20%"></Td>
                        </tr>
                        <tr>
                            <Td class="myhead" width="40%">End Date</Td>
                            <Td width="40%"><Input type="text" readonly  class="myTextbox5" name="enddate" id="edDatepicker" maxlength=100  style="width:120px" value="" ></Td>
                            <Td width="20%"></Td>
                        </tr>
                        <tr>
                            <Td class="myhead" width="40%">To </Td>
                            <Td> <input type="text" id="toAddress" class="myTextbox5" maxlength=100 style="width:150px"></Td>
                        </tr>
                        <tr>
                            <Td class="myhead" width="40%">Subject</Td>
                            <Td><input type="text" id="subject" class="myTextbox5" maxlength=100 style="width:150px"></Td>
                        </tr>
                        <tr></tr>
                        <tr>
                            <Td colspan="2"><strong>Content</strong></Td>
                        </tr>
                        <tr>
                            <Td colspan="2"><textarea rows="10" cols="35" id="mailContent" style="width: 350px"></textarea></Td>
                        </tr>
                        <tr/>
                        <tr align="center"><Td colspan="2">
                                <input class="navtitle-hover" type="button" onclick="sendMail()" value="Send" style="width: auto">
                        </Td></tr>
                    </table>
                    <%
                }
                else if ("note".equalsIgnoreCase(action)){
                    %>
                    <table border="0" width="100%">
                        <tr style="width: 100%">
                            <Td>Type the Note</Td>
                        </tr>
                        <tr style="width: 100%">
                            <Td><textarea rows="10" cols="35" id="noteContent" style="width: 350px"></textarea></Td>
                        </tr>
                        <tr/>
                        <tr style="width: 100%">
                            <Td align="center">
                                <input class="navtitle-hover" type="button" onclick="saveNote()" value="Save" style="width: auto">
                        </Td></tr>
                    </table>
                    <%
                }
            }
            else{
                %>
                <div id="pastActions">
                    <script type="text/javascript">loadPastActions();</script>
                </div>
                <%
            }
        %>
         <script type="text/javascript">
              $(function() {
                $('#stDatepicker').datepicker({
                    changeMonth: true,
                    changeYear: true
                });
                $('#edDatepicker').datepicker({
                    changeMonth: true,
                    changeYear: true
                });

          });

            function sendMail(){
                var scoreCard = <%= scoreCard %>;
                var score = <%= score %>;
                var toAddress = $("#toAddress").val();
                var subject = $("#subject").val();
                var mailContent = $("#mailContent").val();
                var strdate = $("#stDatepicker").val();
                var enddate = $("#edDatepicker").val();
                var member = <%= member %>;
                var elementName= '<%= elementName %>';

                if(toAddress=="")
                    alert("Please specify recipient email address");
                else if(strdate=="")
                    alert("Please select start date");
                else if(enddate=="")
                    alert("Please select end date");
                else if(strdate>enddate)
                        alert ('Please enter start date less than end date');
                else
                {
                    var path = 'scoreCardViewer.do?reportBy=sendMail&toAddress='+toAddress+'&subject='+subject+'&mailContent='+mailContent+'&scoreCard='+scoreCard+'&score='+score+'&startDate='+strdate+'&endDate='+enddate+'&elementName='+elementName;
                    if (member != null && member != "null"){
                        path = path +'&memberId='+member;
                    }

                    parent.closeActionsDialog();
                    $.ajax({
                        url: path,
                        success: function(data){
                        }
                    });
                }

            }

            function saveNote(){

                var note= $("#noteContent").val();
                var scoreCard = <%= scoreCard%>;
                var member = <%= member %>;
                var score = <%= score%>;
                 var strdate = $("#stDatepicker").val();
                var enddate = $("#edDatepicker").val();
                var elementName= '<%= elementName %>';

                parent.closeActionsDialog();
                var path = 'scoreCardViewer.do?reportBy=saveNote&noteContent='+note+'&scoreCard='+scoreCard+'&score='+score+'&startDate='+strdate+'&endDate='+enddate+'&elementName='+elementName;
                if (member != null && member != "null"){
                    path = path +'&memberId='+member;
                }

                $.ajax({
                    url: path,
                    success: function(data){
                    }
                });
            }
            
            function loadPastActions(){
                var scoreCard = <%= scoreCard %>;
                var member = <%= member %>;
                var path = 'scoreCardViewer.do?reportBy=getPastActions&scoreCard='+scoreCard;
                if (member != null && member != "null")
                    path = path + "&memberId=" + member;

                $.ajax({
                    url: path,
                    success: function(data){
                        if(data!=""){
                            $("#pastActions").html(data);
                        }
                        else{
                            alert("No Past Actions");
                            parent.closePastActionsDialog();
                        }
                    }
                });
               
            }


        </script>
    </body>
</html>
