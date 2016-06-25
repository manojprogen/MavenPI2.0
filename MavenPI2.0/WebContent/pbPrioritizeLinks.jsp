
<%@page contentType="text/html" pageEncoding="UTF-8" import="utils.db.ProgenConnection,prg.db.*,com.progen.wigdets.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
       String themeColor="blue" ;
       if(session.getAttribute("theme")==null)
           session.setAttribute("theme",themeColor);
       else
           themeColor=String.valueOf(session.getAttribute("theme"));

String contextPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/jquery-latest.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/jquery.tablesorter.js"></script>
<!--        <link rel="stylesheet" href="<%=request.getContextPath()%>/tablesorter/docs/css/jq.css" type="text/css" media="print, projection, screen" />-->
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/themes/<%= themeColor%>/style.css" type="text/css" media="print, projection, screen" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%= themeColor%>/metadataButton.css" rel="stylesheet" />
        <script>

            $(document).ready(function()
            {

                $("#tablesorter").tablesorter();
            }
        );

            $(function() {
                $("table")
                .tablesorter({widthFixed: true, widgets: ['']})

            });

            function savePriorLinks()
            {
                $.post("<%=request.getContextPath()%>/savePrioritizeLinks.do", $("#favouriteForm").serialize() ,
              function(data){
                window.location.reload(true);
                parent.cancelFavLinks();
                parent.refreshFavLinks();
              });
            }
        </script>
    </head>
    <body>

        <form action=""  name="favouriteForm" id="favouriteForm">
            <table height="30px" width="100%" class="test">
                <tr>
                    <td  align="right">
                        <input type="button" class="navtitle-hover" value="Save" onclick="savePriorLinks()">
                    </td>
                </tr>
            </table>
            <div style="height:420px;overflow-y:auto" class="test">


                <table class="tablesorter" id="tablesorter" border="0">
                    <thead>
                        <tr>
                            <th>Report Name</th>
                            <th>Report Custom Name</th>
                            <th>Report Sequence</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% String dbType = "";
                                    if (session.getAttribute("MetadataDbType") != null) {
                                        dbType = (String) session.getAttribute("MetadataDbType");
                                    }
                                    String[] values;
                                    ////////////////////////////////////////////////////////////////////////////.println.println("------------------>"+request.getParameterValues("chk1"));
                                    values = request.getParameterValues("chk1");
                                    //////////////////////////////.println.println("--->"+values);
                                    int length = values.length;
                                    String valLists = "";
                                    for (int i = 0; i < values.length; i++) {
                                        valLists += "," + values[i];
                                    }
                                    if (!valLists.equalsIgnoreCase("")) {
                                        valLists = valLists.substring(1);
                                    }
                                    PbDb pbdb = new PbDb();
                                    String userId = String.valueOf(session.getAttribute("USERID"));
                                    String sql = "";
                                    ////////////////////////////////////////////////////////////////////////////.println.println("userId--inp ri->"+userId);
                                    if (dbType.equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                                        sql = "SELECT DISTINCT isnull(b.PUR_CUST_REPORT_NAME,a.report_name),b.report_id,b.pur_report_sequence,a.report_type,a.report_name FROM prg_ar_report_master a,prg_ar_user_reports b "
                                                + "WHERE b.user_id=" + userId + " AND a.report_id=b.report_id and a.report_id in(" + valLists + ") ORDER BY b.pur_report_sequence";
                                    }
                                    else if(dbType.equalsIgnoreCase(ProgenConnection.MYSQL)){
                                    sql = "SELECT DISTINCT ifnull(b.PUR_CUST_REPORT_NAME,a.report_name),b.report_id,b.pur_report_sequence,a.report_type,a.report_name FROM prg_ar_report_master a,prg_ar_user_reports b "
                                                + "WHERE b.user_id=" + userId + " AND a.report_id=b.report_id and a.report_id in(" + valLists + ") ORDER BY b.pur_report_sequence";
                                    }
                                    else {
                                        sql = "SELECT DISTINCT nvl(b.PUR_CUST_REPORT_NAME,a.report_name),b.report_id,b.pur_report_sequence,a.report_type,a.report_name FROM prg_ar_report_master a,prg_ar_user_reports b "
                                                + "WHERE b.user_id=" + userId + " AND a.report_id=b.report_id and a.report_id in(" + valLists + ") ORDER BY b.pur_report_sequence";
                                    }

                                    //////////////////////////////.println.println("sql=="+sql);
                                    PbReturnObject pbro = pbdb.execSelectSQL(sql);
                                    //  PbReturnObject pbro = new ProgenWidgetsDAO().getFavReports1(userId);
                                    pbro.writeString();
                                    for (int i = 0; i < pbro.getRowCount(); i++) {
                        %>

                        <tr>
                            <td><%=pbro.getFieldValueString(i, 4)%></td>
                            <td><input type="text" name="custname<%=pbro.getFieldValueString(i, 1)%>" value="<%=pbro.getFieldValueString(i, 0)%>" style="width:200px"></td>
                            <td><select name="favouriteSelect" id="favouriteSelect" style="width:200px">
                                    <%for (int j = 1; j <= length; j++) {
                                                        if (j == (i + 1)) {
                                    %>

                                    <option value="<%=j + "~" + pbro.getFieldValueString(i, 1)%>" selected><%=j%></option>
                                    <%} else {%>
                                    <option value="<%=j + "~" + pbro.getFieldValueString(i, 1)%>"><%=j%></option>
                                    <%}%>
                                    <%}%>
                                </select></td>
                        </tr>

                        <%}%>
                    </tbody>
                </table>
                <center><input type="button"  class="navtitle-hover" value="Save" onclick="savePriorLinks()"></center>
            </div>
            <input type="hidden" id="userId" name="userId" value="<%=userId%>">

        </form>
    </body>
</html>
