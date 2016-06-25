<%-- 
    Document   : addMemberDetails
    Created on : Jan 16, 2010, 12:35:14 PM
    Author     : Saurabh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
          <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />

          <script type="text/javascript">
            function saveMemberDetails(){
                                document.myForm.action = "businessgroupeditaction.do?groupdetails=addMemberDetailsDesc";
                                document.myForm.submit();
                         parent.$('#memberDescDialog').dialog('close');
                
            }
          </script>
    </head>
    <%
      String memberId=request.getParameter("memberId");
      String grpId=request.getParameter("grpId");
      String memberName=request.getParameter("memberName");


    %>
    <body>
         <form name="myForm" method="post">
            <input type="hidden" id="memberId" name="memberId" value="<%=memberId%>">
            <input type="hidden" id="grpId" name="grpId" value="<%=grpId%>">
            <input type="hidden" id="memberName" name="memberName" value="<%=memberName%>">
            <center>

                <table align="center" border="0" style="width:70%">
                    <tr>
                        <td  >
                            <label class="label" >Member Name</label>
                        </td>
                        <td>
                            <input type="text" readonly name="measure" value="<%=memberName%>">
                        </td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <td  >
                            <label class="label" >Member Description</label>
                        </td>
                    </tr><tr>
                        <td>
                         <textarea style="width:99%;height:140px;background-color:white;color:#369;border:1;overflow:auto" id="memberdesc" name="memberdesc"  cols="60"></textarea>
                        </td>
                    </tr>
                  
                </table>
                 <table align="center" width="100%" border="0">
                <tr align="center">
                    <td align="center"><center><input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveMemberDetails()"></center></td>
                </tr>
            </table>
    </body>
</html>
