<%--
   sreekanth
--%>


<%@page contentType="text/html" pageEncoding="UTF-8" import="java.text.SimpleDateFormat,java.text.DateFormat,java.util.Date,java.sql.Connection,utils.db.ProgenConnection,prg.db.Session,prg.db.PbDb,com.progen.bugDetails.BugDetailsDAO,prg.db.PbReturnObject"%>


<%

            PbDb pbdb = new PbDb();
            PbReturnObject bugpbr = new PbReturnObject();
            // String userId = "";
            // userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
            String uIdquery = "select pu_login_id  FROM prg_ar_users WHERE pu_id = &";
            // Object[] obj1 = new Object[1];
            //  obj1[0] = userId;
            //   String finaluIdquery = pbdb.buildQuery(uIdquery, obj1);
            // PbReturnObject usename = pbdb.execSelectSQL(finaluIdquery);
            BugDetailsDAO bugDao = new BugDetailsDAO();
            Connection con = ProgenConnection.getInstance().getBugConn();
            String bugid = request.getParameter("bugid");
            //////////////////////.println("bugid is" + bugid);
            bugpbr = bugDao.bugEditdetails(bugid);
            Object[] obj = new Object[1];
            obj[0] = bugid;
            //"select BUG_DESC,created_date from bug_details WHERE bug_id= & ";
            String query = "select * from bug_details WHERE bug_id=& ORDER by created_time DESC";
            String finalquery = pbdb.buildQuery(query, obj);
            PbReturnObject list = pbdb.execSelectSQL(finalquery, con);
            //////////////////////.println(list.getFieldValueClobString(0, "BUG_DESC"));
            String query1 = "SELECT VERSION_NAME, text_version_id  FROM pi_version WHERE NUMERIC_VERSION_ID = (SELECT PI_ID FROM CUSTOMER_MASTER WHERE CUSTOMER_NAME = 'INDICUS')";
            PbReturnObject list1 = pbdb.execSelectSQL(query1, con);
            con.close();
            con = ProgenConnection.getInstance().getBugConn();
            String finalstatusquery = "select DISTINCT STATUS_ID,STATUS_NAME from BUG_STATUS";
            PbReturnObject statuslist = pbdb.execSelectSQL(finalstatusquery, con);
            con.close();
            PbReturnObject bugpri = bugDao.bugPriority();
            //  //////////////////////.println("bugpbr.getFieldValueString" + bugpbr.getFieldValueString(0, "PRIORITY_NAME"));

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd  hh:mm");
            //   Date date = new Date();

            //  //////////////////////.println("dateform in jsp is: "+dateFormat.format(date));
String contextPath=request.getContextPath();
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="javascript/ui.tabs.js"></script>
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <title>Company Details </title>
        
        <style>
            *{font:11px verdana}
            div.comment {
                background-color: #f7f7fa; font-size: 12px; margin: 5px; padding: 5px; border: 1px solid #369;width:500px;height:auto;
            }
            div.comment1 {
                background-color: #f7f7fa; font-size: 12px;  padding: 5px; border: 1px solid #369;width:auto;overflow:auto;height:260px;
            }
            div.comment p.comment-details, p.comment-details {
                color:#999999;
                font-size:11px;
                margin:0;
                padding-top:10px;
            }

        </style>

    </head>
    <body onload="getstatus()">
        <center>
            <form name="compdet" action="" method="post">

                <%--<div style="width: 35%; min-height: 100%; max-height: 100%;"   class="ui-tabs ui-widget ui-widget-content ui-corner-all">--%>

                <table style="width:100%">
                    <tr>
                        <td colspan="1">
                            Bug No:
                        </td>
                        <td colspan="3">
                            <input type="text" id="bugno" name="bugno" value="<%=bugpbr.getFieldValue(0, "BUG_ID")%>" readonly  style="width: 39.8%" >
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1">
                            Company Name:
                        </td>
                        <td colspan="1">
                            <input type="text" id="compName" name="compName" value="<%=bugpbr.getFieldValue(0, "CUSTOMER_NAME")%>" readonly  style="width: 100%">
                        </td>
                        <td colspan="1">
                            Pi.Version:
                        </td>
                        <td colspan="1">
                            <input type="text" id="piversion" name="piversion" value="<%=bugpbr.getFieldValue(0, "VERSION_NAME")%>" >
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1">
                            Bug Status:
                        </td>
                        <td colspan="1">
                            <select id="bugstatus" name="bugstatus" style="width:100%">
                                <%for (int j = 0; j < statuslist.getRowCount(); j++) {

                if (statuslist.getFieldValueString(j, "STATUS_NAME").equalsIgnoreCase(bugpbr.getFieldValueString(0, "STATUS_NAME"))) {%>
                                <option value="<%=statuslist.getFieldValueString(j, "STATUS_ID")%>" selected ><%=statuslist.getFieldValueString(j, "STATUS_NAME")%> </option>

                                <% } else {%>

                                <option value="<%=statuslist.getFieldValueString(j, "STATUS_ID")%>"><%=statuslist.getFieldValueString(j, "STATUS_NAME")%> </option>
                                <% }
                                %>


                                <%}%>
                            </select>
                        </td>
                        <td colspan="1">
                            Priority:
                        </td>
                        <td colspan="1">
                            <select id="bugpriority" name="bugpriority" style="width:134px">

                                <%for (int j = 0; j < bugpri.getRowCount(); j++) {

                if (bugpri.getFieldValueString(j, "PRIORITY_NAME").equalsIgnoreCase(bugpbr.getFieldValueString(0, "PRIORITY_NAME"))) {%>
                                <option value="<%=bugpri.getFieldValueString(j, "BUG_PRIORITY_ID")%>" selected ><%=bugpri.getFieldValueString(j, "PRIORITY_NAME")%> </option>

                                <% } else {%>

                                <option value="<%=bugpri.getFieldValueString(j, "BUG_PRIORITY_ID")%>"><%=bugpri.getFieldValueString(j, "PRIORITY_NAME")%> </option>
                                <% }
                                %>


                                <%}%>

                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1">
                            Bug Subject:
                        </td>
                        <td colspan="3">
                            <input type="text" id="bugsubject" name="bugsubject" style="width:100%" value="<%=bugpbr.getFieldValue(0, "SUBJECT")%>">
                        </td>
                    </tr>
                    <%--  <tr>

                        <td colspan="1">
                            Changed By:
                        </td>
                        <td colspan="1">
                            <input type="text" id="changedby" name="changedby" value="<%=usename.getFieldValue(0,"PU_LOGIN_ID" ) %>" readonly>
                        </td>
                        <td colspan="1">
                            Chang On:
                        </td>
                        <td colspan="1">
                            <input type="text" id="changedon" name="changeon" value="<%=dateFormat.format(date)%>" readonly>
                        </td>

                    </tr>--%>

                    <tr>
                        <td valign="top" colspan="1" >
                            Old Info:
                        </td>

                        <td colspan="3"><div class="comment1" >
                                <%for (int info = 0; info < list.getRowCount(); info++) {
                if (info > 2) {
                    break;
                }

                                %>
                                <div class="comment" id="oldinfo" >
                                     <p class="comment-details">
                                        posted On <%=list.getFieldValue(info,"CREATED_TIME")%>
                                    </p>
                                    <p>
                                        <%=list.getFieldValueClobString(info, "BUG_DESC")%>
                                    </p>
                                   
                                </div>
                                   


                                <% }
            if (list.getRowCount() > 2) {%>
             
            <div  id="oldinfo1" style="display:none;border:0px;width:"  >   <%
                                for (int info1 = 2; info1 < list.getRowCount(); info1++) {
                      %>
                                <div class="comment" id="oldinfo" >
                                     <p class="comment-details">
                                        posted On <%=list.getFieldValue(info1,"CREATED_TIME")%>
                                    </p>
                                    <p>
                                        <%=list.getFieldValueClobString(info1, "BUG_DESC")%>
                                    </p>
                                   
                                </div>


            <% }%> </div>
            <table style="width:100%" id="seemoreTable">
                                        <tr>
                                            <td align="right">
                                                <a id="seeMore" onclick="seeMore()">See More..</a>
                                            </td>
                                        </tr>
                                    </table>

            <%}%>
                            </div>

                        </td>

                    </tr>

                    <tr> <td valign="top" colspan="1" >
                            Updates:
                        </td>
                        <td colspan="3">
                            <textarea id="setpstofollow" name="setpstofollow" style="width:100%;height:90" ></textarea>

                        </td>

                    </tr>
                    <%-- <tr>

                        <td colspan="1">
                            Updated By:
                        </td>
                        <td colspan="1">
                            <input type="text" id="updatedby" name="updatedby" value="<%=bugpbr.getFieldValue(0, "UPDATED_BY")%>" readonly>
                        </td>
                        <td colspan="1">
                            Updated Date:
                        </td>
                        <td colspan="1">
                            <input type="text" id="updatedate" name="updatedate" value="<%=bugpbr.getFieldValue(0, "UPDATED_DATE")%>" readonly>
                        </td>

                    </tr>--%>



                </table>
                <br/>
                <center>
                    <table>
                        <tr>
                            <td>
                                <input type="button"   class="navtitle-hover" name="save" value="Save" onclick="saveEditDetail()">

                                <input type="reset"   class="navtitle-hover" name="save" value="Reset" >
                            </td>
                        </tr>
                    </table>
                </center>
                <%--</div>--%>

            </form>
        </center>
                <script>


            function saveEditDetail(){
                //  alert("in save")
                document.forms.compdet.action='bugDetailsAction.do?param=saveEditBugdetails'
                document.forms.compdet.submit();
                parent.$('#bugeditDialog').dialog('close');
                window.location.reload(true);
            }
            function getstatus(){


            }
            function CancelDil(){

                parent.$('#bugeditDialog').dialog('close');
            }
            function seeMore(){
                document.getElementById("oldinfo1").style.display = 'block';
                document.getElementById("seemoreTable").style.display = 'none';
            }
        </script>
    </body>
</html>
