<%--
    Document   : showAllMessages
    Created on : Oct 13, 2009, 5:24:45 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.awt.*,java.io.*,java.util.*,utils.db.ProgenConnection,prg.db.PbDb,prg.db.PbReturnObject" %>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
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
         <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
         <script type="text/javascript" src="<%=contextPath%>/javascript/pbReportViewerJS.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/ReportCss.css" rel="stylesheet"/>

      
        <style type="text/css">
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
          .white_content1 {
                position: absolute;
                top: 50px;
                left: 25%;
                width: 700px;
                height:400px;
                padding: 16px;
                border: 16px solid silver;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:5px;
                -moz-border-radius-bottomright:5px;
                -moz-border-radius-topleft:5px;
                -moz-border-radius-topright:5px;
            }
            a {text-decoration:none;cursor:pointer;color:#369}
            a:hover{text-decoration:underline}
            *{font:11px verdana}
        </style>
    </head>
    <body>
        <% String loguserId = String.valueOf(session.getAttribute("USERID"));
        //////////////////////////////////////////////////////////////////////////////////////////.println.println("userId--->" + loguserId);
        PbDb pbdb = new PbDb();

        PbReturnObject pbro = pbdb.execSelectSQL("select * from prg_ar_personalized_reports where prg_user_id="+ loguserId+" AND PRG_REPORT_CUST_NAME is not null and scheduler_details is null");

        pbro.writeString();
        %>
        <table border="1px solid #bdbdbd"  width="80%" align="center">
            <tr>
                <td>
                    <div style="width:100%;height:220px;overflow-y:auto">
        <table id="messages" cellpadding="2"   cellspacing="0"  align="center" class="ui-corner-all" width="100%" border="0">
                        <tr>
                            <th class="bgcolor" align="center" colspan="2">Snapshots</th>
                        </tr>
                        <%for (int i = 0; i < pbro.getRowCount(); i++) {
                String message = "";
               if(!pbro.getFieldValueString(i,"MODULE_TYPE").equalsIgnoreCase("dynamicHeadline") && !pbro.getFieldValueString(i,"MODULE_TYPE").equalsIgnoreCase("scheduler")){
                if(pbro.getFieldValueString(i, 3).length() > 20){
                   message= pbro.getFieldValueString(i, 3).substring(0, 20) + "......";
                    }else{
                   message= pbro.getFieldValueString(i, 3);
                    }
                 String snapurl = "";
               if(!pbro.getFieldValueString(i, "PRG_REPORT_CONTEXT_PATH").equalsIgnoreCase("") && !(pbro.getFieldValueString(i, "PRG_REPORT_CONTEXT_PATH")==null))
                               {
                                snapurl = pbro.getFieldValueClobString(i, "SNAP_URL").replace(";", "&") + "&isSnapShot=true&SnapShotId=" + pbro.getFieldValueString(i, 0);
                           }else{
                            snapurl = pbro.getFieldValueString(i, "PRG_REPORT_CONTEXT_PATH")+"/"+pbro.getFieldValueClobString(i, "SNAP_URL").replace(";", "&") + "&isSnapShot=true&SnapShotId=" + pbro.getFieldValueString(i, 0);
                            }
                String snapshotid = pbro.getFieldValueString(i, 0);
                        %>                <tr>
                                <td style="border-right:black" align="left"><a href="<%=snapurl%>" target="_blank" style="font-weight:bold"><%=pbro.getFieldValueString(i, 3)%></a></td>
                                <td style="border-right:black" align="center"><input type="button" class="navtitle-hover" onclick="deletesnapshot(<%=snapshotid%>)" style="width:auto" value="Delete"></td>
            </tr>
                        <%//////////////////////////////////////////////////.println.println("snapshotid is"+snapshotid);
                        }
                }%>

        </table>
                    </div>
        </td>
        </tr>
        </table>
        <br>
        <Table align="center">
            <Tr>
                <td>
                <%-- <Td><input class="btn" type="button" value="Cancel"  onclick="checkCancel('pbPersonalisedReportsList.jsp')"; ></Td> --%>
                    <center>
                        <input type="button" value="Cancel" class="navtitle-hover" onclick="parent.cancelSnap()"></center>
                </td>

            </Tr>
        </Table>
        <div id="replyMessageDialog" title="replymessages"  style="display:none">

            </div>
        <div id="fade" class="black_overlay"  style="display:none"></div>
          <script type="text/javascript">

            function deletesnapshot(snapid){
                if(confirm('Are You Sure You Want To Delete?'))
                    deletesnapshotForSure(snapid);
            }
            function deletesnapshotForSure(snapid){
                        $.ajax({
                            url:'reportViewer.do?reportBy=deleteSnapshot&snapshotid='+snapid,
                            success:function(data)
                            {
                                if ( String.toLowerCase(data.toString()) == String.toLowerCase("success") )
                                {
                                    alert("Snapshot Deleted Successfully");
//                                    var source = "/pi/snapShotdisplay.jsp?REPORTID=25";
//                                    var dSrc = parent.document.getElementById("sanpFrame");
//                                    dSrc.src = source;
                        parent.document.getElementById('sanpFrame').contentWindow.location.reload();

                        parent.document.getElementById('replyMessageFrame').contentWindow.location.reload();
                              }
                                else
                                {
                                    alert("The Snapshot could not be deleted. Please contact System Administrator");
                                }
                               <%-- parent.cancelSnap();
                                <%--window.location.reload(true);
                                var frameObj =  parent.document.getElementById("sanpFrame");
                                frameObj.src = frameObj.src;--%>

                            }
                        });
                    }
            function getMessage1(id,from,sub,message)
            {
               // parent.document.getElementById('replyMessageDialog').style.display='none';
                src = "pbTakeMailAddress.jsp?from="+from+"&subject="+sub+"&message="+message+"&sample=sample";
                  document.getElementById('replyMessageDialog').innerHTML = "";
                    document.getElementById('replyMessageDialog').innerHTML = "<iframe src="+src+" frameborder=\"0\" width=600px height=250px></iframe>";
                document.getElementById('fade').style.display='block';
                document.getElementById('replyMessageDialog').style.display='block';
            }
            function cancelMessage()
            {
                parent.cancelMessage();
            }
              function viewReport(path){
                //parent.document.forms[0].action=path;
                //parent.document.forms[0].submit();
                parent.submiturls1(path);
            }
        </script>
    </body>
</html>
