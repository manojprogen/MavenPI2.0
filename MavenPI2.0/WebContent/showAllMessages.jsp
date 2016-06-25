<%-- 
    Document   : showAllMessages
    Created on : Oct 13, 2009, 5:24:45 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="prg.db.PbReturnObject,prg.db.PbDb,java.util.*,java.io.*,java.awt.*,utils.db.ProgenConnection" %>



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
             
        <title>JSP Page</title>
         <script type="text/javascript" src="<%=contextPath%>/javascript/pbReportViewerJS.js"></script>
         <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />
          <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/ReportCss.css" rel="stylesheet" />
      
        <style>

                     .ui-state-default {
                         background:#E6E6E6 none repeat-x scroll 50% 50%;
                         border:1px solid #E6E6E6;
                         color:#000000;
                         font-weight:normal;
                         height:20px;
                         outline-style:none;
                         outline-width:medium;
                     }

                     .ui-corner-all {
                         -moz-border-radius-bottomleft:6px;
                         -moz-border-radius-bottomright:6px;
                         -moz-border-radius-topleft:6px;
                         -moz-border-radius-topright:6px;
                     }
                     a {font-family:Verdana;font-size:10px;cursor:pointer;}
        </style>
    </head>
    <body>
        <% String loguserId = String.valueOf(session.getAttribute("USERID"));
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("userId--->" + loguserId);
        PbDb pbdb = new PbDb();

        PbReturnObject pbro = pbdb.execSelectSQL("select  PMB_FROM, PMB_TO, PMB_SUBJECT, nvl(PMB_MESSAGE,PMB_SUBJECT), PMB_ID, USERID from prg_message_board where USERID=" + loguserId + "  order by PMB_ID");

        pbro.writeString();
        %>

        <table border="1px solid #bdbdbd"  width="100%" style="min-height:200px" >
            <tr>
                <td valign="top">
        <table id="messages" cellpadding="2"   cellspacing="0"  align="center" class="ui-corner-all" width="100%" border="0">
                        <tr><th class="bgcolor">Messages</th></tr>
                        <%for (int i = 0; i < pbro.getRowCount(); i++) {
            String message = "";
            //  //////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("pbro.getFieldValueString(i, 3)--"+pbro.getFieldValueString(i, 3));
              //  if(pbro.getFieldValueString(i, 3).length() > 20){
              //     message= pbro.getFieldValueString(i, 3).substring(0, 20) + "......";
               //     }else{
                   message= pbro.getFieldValueString(i, 3);
                  //  }
              ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("message--"+message);
          // String messsage = (pbro.getFieldValueString(i, 3).length() > 20) ? pbro.getFieldValueString(i, 3) : pbro.getFieldValueString(i, 3).substring(0, 20) + "......";
            %>


            <tr>
                <td style="border-right:black">
                    <a href="javascript:void(0)" onclick="getMessage1('<%=pbro.getFieldValueInt(i, 4)%>','<%=pbro.getFieldValueString(i, 0)%>','<%=pbro.getFieldValueString(i, 2)%>','<%=pbro.getFieldValueString(i, 3)%>')" style="text-decoration:none">
                        <%=message%></a>
                        </td>

            </tr>
            <%}%>

        </table>
        </td>
        </tr>
        </table>
        <br>
        <Table align="center">
            <Tr>
                <%-- <Td><input class="btn" type="button" value="Cancel"  onclick="checkCancel('pbPersonalisedReportsList.jsp')"; ></Td> --%>
                <Td><Input class="navtitle-hover" type="button" value="Cancel" onclick="cancelMessage()"></Td>

            </Tr>
        </Table>
        <div id="replyMessageDialog" title="replymessages" class="white_content1" style="">

            </div>
                <div id="fade" class="black_overlay"></div>
                  <script>

            function getMessage1(id,from,sub,message)
            {
               // parent.document.getElementById('replyMessageDialog').style.display='none';
                src = "pbTakeMailAddress.jsp?from="+from+"&subject="+sub+"&message="+message+"&sample=sample";
                 src=src.replace(" ","~","gi");
               // alert(src)
                  document.getElementById('replyMessageDialog').innerHTML = "";
                document.getElementById('replyMessageDialog').innerHTML = "<iframe src="+src+" width=600px height=250px></iframe>";
                document.getElementById('fade').style.display='block';
                document.getElementById('replyMessageDialog').style.display='block';
            }
            function cancelMessage()
            {
                parent.cancelMessage();
            }
        </script>
    </body>
</html>
