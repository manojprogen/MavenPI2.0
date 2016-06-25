<%-- 
   sreekanth
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.sql.Connection,utils.db.ProgenConnection,prg.db.PbReturnObject,prg.db.PbDb,com.progen.bugDetails.BugDetailsDAO"%>


<%      //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

        PbReturnObject bugpbr = new PbReturnObject();
        String[] statusstr;
        PbDb pbdb = new PbDb();
        Connection con = ProgenConnection.getInstance().getBugConn();
        String query1 = "select max (bug_id) from bug_master";
        PbReturnObject list = pbdb.execSelectSQL(query1, con);
        con.close();
        Connection connection = ProgenConnection.getInstance().getBugConn();
        String prlistquery = "select * FROM product_master";
        PbReturnObject productlist = pbdb.execSelectSQL(prlistquery, connection);
        //////////////////////.println("productlist"+productlist.getRowCount());
        String comName = request.getParameter("compName");
        String verName = request.getParameter("versionName");
        BugDetailsDAO bugdao = new BugDetailsDAO();
        bugpbr = bugdao.bugPriority();
        PbReturnObject bugpbr1 = bugdao.bugstatus();
String contextPath=request.getContextPath();


%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="javascript/ui.tabs.js"></script>
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
         <link href="<%=contextPath%>/jQuery/jquery/themes/base/jquery.alerts.css" rel="stylesheet" type="text/css">
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/jquery.alerts.js"></script>
        <title>Company Details </title>
       
        <style>
            *{font:11px verdana}
        </style>
    </head>
    <body onload="getstatus()">
        <center>
            <form name="compdet" action="" method="post">

                <%--<div style="width: 35%; min-height: 100%; max-height: 100%;"   class="ui-tabs ui-widget ui-widget-content ui-corner-all">--%>

                <table style="width:100%">
                    <tr>
                        <td colspan="1" >
                            Bug No:
                        </td>
                        <td colspan="1" >
                            <input type="text" id="bugno" name="bugno" value="<%=Integer.parseInt(list.getFieldValueString(0, 0))+1%>">
                        </td>
                        <td colspan="1">
                            Company Name:
                        </td>
                        <td colspan="1">
                            <input type="text" id="compName" name="compName" value="<%=comName%>">
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1">
                           Module :
                        </td>
                        <td colspan="1">
                            <select name="module" id="module"  style="width: 90%;" >
                                <option value="">--select--</option>
                                <% for(int plist=0;plist<productlist.getRowCount();plist++){%>
                                <option value="<%= productlist.getFieldValue(plist, "PRODUCT_ID")%>"><%= productlist.getFieldValue(plist, "PRODUCT_NAME")%> </option>

                                    <%}%>
                            </select>
                        </td>
                        <td colspan="1">
                            Product.Version:
                        </td>
                        <td colspan="1">
                            <input type="text" id="piversion" name="piversion" value="<%=verName%>" >
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1">
                            Bug Status:
                        </td>
                        <td colspan="1">
                            <select id="bugstatus" name="bugstatus" style="width:90%">
                                <%for (int j = 0; j < bugpbr1.getRowCount(); j++) {%>
                                <option value="<%=bugpbr1.getFieldValueString(j, "STATUS_ID")%>"><%=bugpbr1.getFieldValueString(j, "STATUS_NAME")%> </option>
                                <%}%>
                            </select>
                        </td>
                        <td colspan="1">
                            Priority:
                        </td>
                        <td colspan="1">
                            <select id="bugpriority" name="bugpriority" style="width:134px">
                                <%for (int i = 0; i < bugpbr.getRowCount(); i++) {
            if (bugpbr.getFieldValueString(i, "PRIORITY_NAME").equalsIgnoreCase("MEDIUM")) {
                                %>
                                <option value="<%=bugpbr.getFieldValueString(i, "BUG_PRIORITY_ID")%>" selected ><%=bugpbr.getFieldValueString(i, "PRIORITY_NAME")%> </option>
                                <%} else {
                                %>

                                <option value="<%=bugpbr.getFieldValueString(i, "BUG_PRIORITY_ID")%>"><%=bugpbr.getFieldValueString(i, "PRIORITY_NAME")%> </option>
                                <%}
        }%>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1">
                            Bug Subject:
                        </td>
                        <td colspan="3">
                            <input type="text" id="bugsubject" name="bugsubject" style="width:100%">
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1" valign="top">
                            Setps to follow:
                        </td>
                        <td valign="top"  colspan="3">
                            <textarea id="setpstofollow" name="setpstofollow" style="width:100%;height:150">

                            </textarea>

                        </td>
                    </tr>
                </table>
                <br/>
                <center>
                    <table>
                        <tr>
                            <td>
                                <input type="button"   class="navtitle-hover" name="save" value="Save" onclick="saveDetail()">
                                <input type="button"   class="navtitle-hover" name="cancel" value="Cancel" onclick="CancelDi()">
                                <input type="reset"   class="navtitle-hover" name="save" value="Reset" >
                            </td>
                        </tr>
                    </table>
                </center>
                <%--</div>--%>

            </form>
        </center>
                 <script>
            function saveDetail(){
                //alert("in save")

        var moduid=document.getElementById("module").value;
        if(moduid==null || moduid==0)
            {
               jAlert("Please Select  Module")
            }else{
                  document.forms.compdet.action='bugDetailsAction.do?param=savebugdetails'
                document.forms.compdet.submit();
                parent.$('#userDialog').dialog('close');
                parent.window.location.reload(true);
            }
              
            }
            function getstatus(){


            }
            function CancelDi(){

                parent.$('#userDialog').dialog('close');
            }
        </script>
    </body>
</html>
