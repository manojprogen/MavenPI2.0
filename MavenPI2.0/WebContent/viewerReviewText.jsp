<%--
    Document   : viewerReviewText
    Created on : Nov 27, 2009, 11:42:56 AM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.chating.progenchatbean,java.sql.*,prg.db.PbReturnObject,prg.db.Session,prg.db.PbDb,prg.db.Container,com.progen.db.*,java.util.*,utils.db.*,java.awt.*,java.io.*"%>

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
        <title>pi EE</title>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.3.2.min.js"></script>-->
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
         <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/ReportCss.css" rel="stylesheet"/>

<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/jquery-1.3.2.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.core.js"></script>-->

       
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.core.js"></script>-->

        <%
        String username = String.valueOf(session.getAttribute("LOGINID"));
        String REPORTID = request.getParameter("REPORTID");
        %>
       
    </head>
    <body onload="dispdata()" style="width:100%;border:0;margin:0;height:100%">
        <form name="dispForm1" method="post" action="">
            <table id="tabText" width="99%"  class="textstyle" align="center">
                <tr>
                    <td>
                        <div id="dispText"  class="textcolor"></div>
                    </td>
                </tr>
                <tr >
                    <td style="height:30px;">
                        <textarea   id="msgText" name="msgText" maxrows="1" style="width:99%;background-color:#fff;height:18px;font-size:12px;font-family:verdana;"   onkeypress="findKeyCode(event)"></textarea>
                    </td>
                </tr>
            </table>
        </form>
         <script type="text/javascript">
            function showdata(){
                var disp = document.getElementById("dispText");              
                    $.ajax({
                        url: 'progentalkaction.do?parameter=chatdata&reprtid=<%=REPORTID%>&chtmsg='+document.getElementById("msgText").value+'&usrname=<%=username%>',
                        success: function(data){
                            disp.innerHTML=data;
                            disp.value="";
                            document.getElementById("msgText").value='';
                        }
                    });  
            }
            function dispdata(){
                var disp = document.getElementById("dispText");               
                $.ajax({
                    url: 'progentalkaction.do?parameter=dispdata&reprtid=<%=REPORTID%>',
                    success: function(data){
                        disp.innerHTML=data;
                    }
                });
                setTimeout('dispdata()',1000000);// 1000 represents 1 second
            }
            function findKeyCode(evt){
                var charCode = (evt.which) ? evt.which : evt.keyCode;
                if(charCode == '13'){
                    
                    if(document.getElementById("msgText").value!='' &&  document.getElementById("msgText").value!=undefined){
                        showdata()
                    }
                }
            }
        </script>
    </body>
</html>
