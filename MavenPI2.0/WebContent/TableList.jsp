
<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.test.*,prg.util.GetPostgresqlTables,utils.db.SqlConnection,utils.db.*,java.util.*,prg.db.PbDb,java.sql.*,prg.db.PbReturnObject"%>



<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String contextPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
         <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />

        <script language="JavaScript" src="<%=contextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>
<!--        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.droppable.js"></script>-->
        <link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />

       
        <style type="text/css">

            table tr th {
                background-color: #b4d9ee;
                padding: 3px;
            }
            table tr.rowb { background-color:#b4d9ee; }

            table tr.filterColumns td { padding:2px; }

            body { padding-bottom:150px; }
        </style>

    </head>



    <body>
        <form name="myForm" method="get">
            <%
                        Vector v = new Vector();
                        String userName = "";
                        String password = "";
                        String dsn_name = "";
                        String dbName = "";
                        String server = "";
                        String port = "";
                        String serviceId = "";
                        String databaseName = "";
                        String connection = request.getParameter("connection");
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("active connection" + connection);

                        PbReturnObject pbReturnObject = new PbReturnObject();
                        // Class.forName("oracle.jdbc.driver.OracleDriver");
                        PbDb pbDb = new PbDb();
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("connection==" + con1);

                        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("statement =" + st1);
                        String sql1 = "select * from prg_user_connections where CONNECTION_ID=" + connection;
                        //.println("sql fror source id==" + sql1);
                        pbReturnObject = pbDb.execSelectSQL(sql1);
                        int sourceId = 0;
                        for (int i = 0; i < pbReturnObject.getRowCount(); i++) {
                            sourceId = pbReturnObject.getFieldValueInt(i, 0);
                            databaseName = pbReturnObject.getFieldValueString(i, 11);
                            userName = pbReturnObject.getFieldValueString(i, 2);
                            password = pbReturnObject.getFieldValueString(i, 3);
                            dsn_name = pbReturnObject.getFieldValueString(i, 10);
                            dbName = pbReturnObject.getFieldValueString(i, 12);
                            server = pbReturnObject.getFieldValueString(i, 4);
                            port = pbReturnObject.getFieldValueString(i, 7);
                            serviceId = pbReturnObject.getFieldValueString(i, 5);
                            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("DSN NAME IS " + dsn_name);
                        }

                        //.println("databaseName\t" + databaseName);

                        PbReturnObject dupRsObject = pbDb.execSelectSQL("select table_name from prg_db_master_table where connection_id=" + sourceId);
                        for (int j = 0; j < dupRsObject.getRowCount(); j++) {
                            v.add(dupRsObject.getFieldValueString(j, 0));
                        }


                        if (databaseName.equalsIgnoreCase("ORACLE")) {
                            if (dsn_name == null || dsn_name =="" ) {
                                try {

                                    Connection con = null;

                                    PbReturnObject rsObject = new PbReturnObject();
                                    PbReturnObject rsObjectTab = new PbReturnObject();
                                    //con= new ProgenConnection().getConnection();

                                    con = ProgenConnection.getInstance().getConnectionByConId(Integer.toString(sourceId));
                                    String sql = "select tname from tab";
                                    rsObject = pbDb.execSelectSQL(sql, con);
                                    String sql2="select table_name from prg_db_master_table where connection_id="+ connection;
                                    rsObjectTab=pbDb.execSelectSQL(sql2);

                                    con=null;



            %>

            <!--<iframe src="aaa.jsp" width="100%" height="400" frameborder="0"></iframe>-->

            <div id="type" align="center" style="height:90%;width:100%" class="white_content">

                <input type="hidden" name="activeConnection" id="activeConnection" value="<%=sourceId%>"/>
                <table style="width:50%">

                    <tr>
                        <td class="myHead" style="width:20%" align="center">
                            <font style="font-family:verdana;font-size:12px">Select</font>
                        </td>
                        <td style="width:58%">
                            <select id="tvtype" name="tvtype" onchange="getTableSet()" style="width:146px">

                                <option value="Tables">Tables</option>
                                <option value="Views">Views</option>
                            </select>
                        </td></tr>
                </table>

                <table border="1" width="100%">
                    <tr><td width="50%">

                            <div id="tableList" align="left" style="height:300px;width:100%;overflow:auto;" >


                                <table id="filterTable2" cellspacing="0" cellpadding="0" >
                                    <thead>
                                        <tr>
                                            <th></th><th>Name</th>
                                        </tr>
                                    </thead>
                                    <tfoot></tfoot>
                                    <tbody>

                                        <%//session.setAttribute("tabType","Table");

                                                                            for (int k = 0; k < rsObject.getRowCount(); k++) {
                                                                                int flag=0;
                                                                                if (v.contains(rsObject.getFieldValueString(k, 0)) != true) {                                                                                     
                                        %>

                                        <tr>

                                            <td></td>
                                            <td class="myDragTabs" width="50%" style="font-family:verdana;font-size:12px" id="<%=rsObject.getFieldValueString(k, 0)%>,Table"><%=rsObject.getFieldValueString(k, 0)%></td>

                                        </tr>

                                        <%                                          
                                                                            }
                                                                       }
                                        %>
                                    </tbody>
                                </table></div>
                            <div id="viewList" align="center" style="height:300px;width:100%;overflow:auto;display:none" >
                                <table id="filterview2" cellspacing="0" cellpadding="0">
                                    <thead>
                                        <tr>
                                            <th></th><th>Name</th>
                                        </tr>
                                    </thead>
                                    <tfoot></tfoot>
                                    <tbody>

                                        <%
                                                                            //session.setAttribute("tabType","View");
                                                          con = ProgenConnection.getInstance().getConnectionByConId(Integer.toString(sourceId));

                                                                            String viewsql = "select view_name from all_views where upper(owner)=upper('" + userName + "')";
                                                                            //.println("rowcount===" + viewsql);
                                                                           PbReturnObject viewrsObject = pbDb.execSelectSQL(viewsql,con);

                                                                           con=null;
                                                                           for(int vk=0;vk<viewrsObject.getRowCount();vk++) {
                                                                               int flag=0;                                                                                
                                        %>

                                        <tr>

                                            <td ></td>
                                            <td  id="<%=viewrsObject.getFieldValueString(vk,0)%>,Table" class="myDragTabs" width="50%" style="font-family:verdana;font-size:12px"><%=viewrsObject.getFieldValueString(vk,0)%></td>
                                        </tr>

                                        <%
                                                                            }
                                        %>
                                    </tbody>
                                </table>
                            </div>









                        </td>
                        <td  width="50%">
                            <div id="dropTabs" style="height:300px;width:100%;overflow:auto;">
                                <table id="Dropped" cellspacing="0" cellpadding="0">
                                    <tbody>
                                <%ArrayList oldtables = new ArrayList();
                                            con = ProgenConnection.getInstance().getConnectionByConId(Integer.toString(sourceId));
                                                                    sql = "select tname from tab";
                                                                    rsObject = pbDb.execSelectSQL(sql, con);

                                                                    con=null;
                                                                  for(int loop=0;loop<rsObject.getRowCount();loop++) {
                                                                        if (v.contains(rsObject.getFieldValueString(loop,0)) == true) {
                                                                            ////////////////////////////////////////////////////////////.println.println("v.get(i)" + rs.getString(1));
                                                                            oldtables.add(rsObject.getFieldValueString(loop,0));
                                                                            out.println(rsObject.getFieldValueString(loop,0)+ "<br/>");
                                    }
                                                                    }
                                %>
                                </tbody>
                                </table>
                            </div>
                            <input type="hidden" name="oldtables" id="oldtables" value="<%=oldtables%>">
                        </td>
                    </tr><tr>

                    </tr>
                </table>
                    <table>
                        <tr>
                            <td><input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveTables('Table')"></td>
                            <td><input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelTableFade()"></td>
                        </tr>
                    </table>
                <%--<div style="border:100px;height:50px;width:50px" id="dropTabs">
                Hello Div

</div>--%>

                <%--
                <div id="viewList" align="center" style="height:300px;width:340px;overflow:auto;display:none" >
                <table id="filterview2" cellspacing="0" cellpadding="0">
                <thead>
                <tr>
                <th></th><th>Name</th>
                </tr>
                </thead>
                <tfoot></tfoot>
                <tbody>

<%
//session.setAttribute("tabType","View");
Statement viewst=con.createStatement();
String viewsql="select view_name from all_views where upper(owner)=upper('"+userName+"')";
ResultSet viewrs=viewst.executeQuery(viewsql);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("rowcount==="+viewrs);

while(viewrs.next()){%>

<tr>

<td ><input type="checkbox" name="chk1" value="<%=viewrs.getString(1)%>,View"></td>
<td width:340px><%=viewrs.getString(1)%></td>
</tr>

<%
}
%>
</tbody>
</table> --%>
                <%-- <table>
                <tr>
                <td><input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveTables('View')"></td>
                <td><input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelFade()"></td>
                </tr>
                </table>
                </div>--%>


            </div>



            <%
                                                if (con != null) {
                                                    con.close();
                                                }


                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }

                                    } else if (databaseName.equalsIgnoreCase("SqlServer")) {
                                        /*
                                        SqlConnection tables = new SqlConnection();
                                        String url = " jdbc:jtds:sqlserver://" + server + ":" + port + "/" + dbName;
                                        userName = userName;
                                        password=password;
                                        HashMap tablesList = tables.getTableNames(url, userName, password);
                                        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("URL is "+tablesList);
                                        Set keytables= tablesList.keySet();
                                        Iterator it = keytables.iterator();
                                        session.setAttribute("sqlTables", tablesList);
                                         */

                                        Connection con = utils.db.ProgenConnection.getInstance().getConnectionByConId(Integer.toString(sourceId));
                                        Statement dupStmt = con.createStatement();
                                        Statement viewStmt = con.createStatement();
                                        ResultSet dupReSet = dupStmt.executeQuery("select * from sys.tables ");
                                        ResultSet viewReSet = viewStmt.executeQuery("SELECT * FROM sys.views ");

                                        //.println("rs"+dupReSet);
                                        //.println("rs"+viewReSet);


            %>

            <div id="type" align="center" style="height:450px;width:600px" class="white_content">

                <input type="hidden" name="activeConnection" id="activeConnection" value="<%=sourceId%>"/>
                <table style="width:50%">

                    <tr>
                        <td class="myHead" style="width:20%">
                            &nbsp;  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                            &nbsp; &nbsp; &nbsp; &nbsp;<font size="5px">Select</font>
                        </td>
                        <td style="width:58%">
                            <select id="tvtype" onchange="getTableSet()" style="width:146px">

                                <option value="Tables">Tables</option>
                                <option value="Views">Views</option>
                            </select>
                        </td></tr>
                </table>

                <table  border="1"><tr><td width="50%">

                            <div id="tableList" align="left" style="height:250px;width:200px;overflow:auto;" >


                                <table id="filterTable2" cellspacing="0" cellpadding="0" >
                                    <thead>
                                        <tr>
                                            <th></th><th>Name</th>
                                        </tr>
                                    </thead>
                                    <tfoot></tfoot>
                                    <tbody>

                                        <%//session.setAttribute("tabType","Table");
                                                                    int i = 0;
                                                                    while (dupReSet.next()) {

                                                                        if (v != null || v.contains(dupReSet.getString(1)) != true) {
                                        %>

                                        <tr>

                                            <td></td>
                                             <td class="myDragTabs" width="50%" style="font-family:verdana;font-size:12px" id="<%=dupReSet.getString(1)%>,Table"><%=dupReSet.getString(1)%></td>


                                        </tr>

                                        <%
                                                                        }
                                                                    }
                                        %>
                                    </tbody>
                                </table></div>
                            <div id="viewList" align="center" style="height:250px;width:200px;overflow:auto;display:none" >
                                <table id="filterview2" cellspacing="0" cellpadding="0">
                                    <thead>
                                        <tr>
                                            <th></th><th>Name</th>
                                        </tr>
                                    </thead>
                                    <tfoot></tfoot>
                                    <tbody>

                                        <%//session.setAttribute("tabType","Table");
                                                                    int j = 0;
                                                                    while (viewReSet.next()) {

                                                                        if (v != null || v.contains(viewReSet.getString(1)) != true) {
                                        %>

                                        <tr>

                                            <td ></td>
                                            <td  class="myDragTabs" width="50%" id="<%=viewReSet.getString(1)%>,Table"><%=viewReSet.getString(1)%></td>
                                        </tr>

                                        <%                                                                         }
                                                                    }
                                        %>
                                    </tbody>
                                </table>


                        </td>
                        <td id="dropTabs"  width="50%"></td>

                    </tr><tr>
                    <table>
                        <tr>
                            <td><input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveTables('Table')"></td>
                            <td><input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelTableFade()"></td>
                        </tr>
                    </table>
                    </tr>
                </table>



            </div>


            <%} else if (databaseName.equalsIgnoreCase("EXCEL")) {
                                        try {
                                            ExcelTest et = new ExcelTest();
                                            HashMap details = et.getDetails();
                                            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("hm----->"+details);
                                            session.setAttribute("hm", details);

                                            Set hs = details.keySet();
                                            Iterator it = hs.iterator();


            %>




            <div id="type" align="center" style="height:450px;width:600px" class="white_content">

                <input type="hidden" name="activeConnection" id="activeConnection" value="<%=sourceId%>"/>
                <table style="width:50%">

                    <tr>
                        <td class="myHead" style="width:20%">
                            &nbsp;  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                            &nbsp; &nbsp; &nbsp; &nbsp;<font size="5px">Select</font>
                        </td>
                        <td style="width:58%">
                            <select id="tvtype" onchange="getTableSet()" style="width:146px">

                                <option value="Tables">Tables</option>

                            </select>
                        </td></tr>
                </table>

                <table  border="1"><tr><td width="50%">

                            <div id="tableList" align="left" style="height:250px;width:200px;overflow:auto;" >


                                <table id="filterTable2" cellspacing="0" cellpadding="0" >
                                    <thead>
                                        <tr>
                                            <th></th><th>Name</th>
                                        </tr>
                                    </thead>
                                    <tfoot></tfoot>
                                    <tbody>

                                        <%//session.setAttribute("tabType","Table");

                                                                                    while (it.hasNext()) {
                                                                                        String tab = (String) it.next();
                                                                                        if (tab.indexOf("type") < 0 && tab.indexOf("length") < 0) {
                                                                                            if (v.contains(tab) != true) {
                                        %>

                                        <tr>

                                            <td></td>
                                            <td  class="myDragTabs" width="50%" id="<%=tab%>,Table"><%=tab%></td>

                                        </tr>

                                        <%
                                                                                            }
                                                                                        }
                                                                                    }
                                        %>
                                    </tbody>
                                </table></div>
                            <div id="viewList" align="center" style="height:250px;width:200px;overflow:auto;display:none" >
                                <table id="filterview2" cellspacing="0" cellpadding="0">
                                    <thead>
                                        <tr>
                                            <th></th><th>Name</th>
                                        </tr>
                                    </thead>
                                    <tfoot></tfoot>
                                    <tbody>



                                        <tr>

                                            <td ></td>
                                            <td class="myDragTabs" width="50%"><%--<%=viewrs.getString(1)%>--%></td>
                                        </tr>

                                        <%

                                        %>
                                    </tbody>
                                </table>









                        </td>
                        <td id="dropTabs"  width="50%"></td>

                    </tr><tr>
                    <table>
                        <tr>
                            <td><input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveTables('Table')"></td>
                            <td><input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelTableFade()"></td>
                        </tr>
                    </table>
                    </tr>
                </table>



            </div>


            <%} catch (Exception e) {

                                            e.printStackTrace();
                                        }

                                    } else if (databaseName.equalsIgnoreCase("mysql")) {
                                        MysqlTableName tables = new MysqlTableName();
                                        String url = "jdbc:mysql://" + server + ":" + port + "/" + dbName + "";
                                        tables.setRequest(request);
                                        HashMap tablesList = tables.getTableNames(url, userName, password);
                                        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("URL is "+tablesList);
                                        Set<String> keytables = tablesList.keySet();
                                       // 
                                       // 
                                        session.setAttribute("mysqlTables", tablesList);
            %>

            <div id="type" align="center" style="height:450px;width:600px" class="white_content">

                <input type="hidden" name="activeConnection" id="activeConnection" value="<%=sourceId%>"/>
                <table style="width:50%">

                    <tr>
                        <td class="myHead" style="width:20%">
                            &nbsp;  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                            &nbsp; &nbsp; &nbsp; &nbsp;<font size="5px">Select</font>
                        </td>
                        <td style="width:58%">
                            <select id="tvtype" onchange="getTableSet()" style="width:146px">

                                <option value="Tables">Tables</option>
                                <option value="Views">Views</option>
                            </select>
                        </td></tr>
                </table>

                <table  border="1"><tr><td width="50%">

                            <div id="tableList" align="left" style="height:250px;width:200px;overflow:auto;" >


                                <table id="filterTable2" cellspacing="0" cellpadding="0" >
                                    <thead>
                                        <tr>
                                            <th></th><th>Name</th>
                                        </tr>
                                    </thead>
                                    <tfoot></tfoot>
                                    <tbody>

                                        <%//session.setAttribute("tabType","Table");


                                                                        for(String str:keytables)

                                                                            if (!v.contains(str)) {
                                        %>

                                        <tr>

                                            <td></td>
                                            <td  class="myDragTabs" width="50%" id="<%=str%>,Table"><%=str%></td>

                                        </tr>

                                        <%
                                                                            }


                                        %>
                                    </tbody>
                                </table></div>
                            <div id="viewList" align="left" style="height:250px;width:200px;overflow:auto;display:none" >
                                <table id="filterview2" cellspacing="0" cellpadding="0" align="left">
                                    <thead>
                                        <tr>
                                            <th></th><th>Name</th>
                                        </tr>
                                    </thead>
                                    <tfoot></tfoot>
                                    <tbody>



                                        <tr>

                                            <td ></td>
                                            <td class="myDragTabs" width="50%"><%--<%=viewrs.getString(1)%>--%></td>
                                        </tr>

                                        <%

                                        %>
                                    </tbody>
                                </table>

                        </td>
                        <td id="dropTabs"  width="50%"></td>

                    </tr><tr>
                    <table>
                        <tr>
                            <td><input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveTables('Table')"></td>
                            <td><input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelTableFade()"></td>
                        </tr>
                    </table>
                    </tr>
                </table>



            </div>


            <%} else if (databaseName.equalsIgnoreCase("postgreSQL")) {
                                        GetPostgresqlTables tables = new GetPostgresqlTables();

                                        ArrayList prvtablist = tables.getPreviousTables(connection);

                                        HashMap tablesList = tables.getTableNames(server, dbName, userName, password, port, connection);
                                        Set<String> keytables = tablesList.keySet();

                                        session.setAttribute("postgreTables", tablesList);
            %>

            <div id="type" align="center" style="height:450px;width:600px" class="white_content">

                <input type="hidden" name="activeConnection" id="activeConnection" value="<%=sourceId%>"/>
                <table style="width:50%">

                    <tr>
                        <td class="myHead" style="width:20%">
                            &nbsp;  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                            &nbsp; &nbsp; &nbsp; &nbsp;<font size="5px">Select</font>
                        </td>
                        <td style="width:58%">
                            <select id="tvtype" onchange="getTableSet()" style="width:146px">

                                <option value="Tables">Tables</option>
                                <option value="Views">Views</option>
                            </select>
                        </td></tr>
                </table>

                <table  border="1"><tr><td width="50%">

                            <div id="tableList" align="left" style="height:250px;width:200px;overflow:auto;" >


                                <table id="filterTable2" cellspacing="0" cellpadding="0" >
                                    <thead>
                                        <tr>
                                            <th></th><th>Name</th>
                                        </tr>
                                    </thead>
                                    <tfoot></tfoot>
                                    <tbody>

                                        <%//session.setAttribute("tabType","Table");

                                                                   for(String str:keytables) {


                                                                            if (!v.contains(str) ) {
                                                                                if (!(prvtablist.contains(str))) {
                                        %>

                                        <tr>

                                            <td></td>
                                            <td  class="myDragTabs" width="50%" id="<%=str%>,Table"><%=str%></td>

                                        </tr>

                                        <%
                                                                                }
                                                                            }

                                                                    }
                                        %>
                                    </tbody>
                                </table></div>
                            <div id="viewList" align="center" style="height:250px;width:200px;overflow:auto;display:none" >
                                <table id="filterview2" cellspacing="0" cellpadding="0">
                                    <thead>
                                        <tr>
                                            <th></th><th>Name</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                                                    HashMap viewsList = tables.getTableViews(server, dbName, userName, password, port);
                                                                    Set viewkeytables = viewsList.keySet();
                                                                    ////.println("keytables size is" + viewkeytables.size());
                                                                    Iterator itrateViews = viewkeytables.iterator();
                                                                    session.setAttribute("postgreViews", itrateViews);
                                                                    //session.setAttribute("tabType","View");
                        /*Statement viewst = con.createStatement();
                                                                    String viewsql = "select view_name from all_views where upper(owner)=upper('" + userName + "')";
                                                                    ResultSet viewrs = viewst.executeQuery(viewsql);
                                                                    while (viewrs.next()) {*/

                                                                    while (itrateViews.hasNext()) {
                                                                        String table = itrateViews.next().toString();
                                                                        if (!(prvtablist.contains(table))) {
                                        %>
                                        <tr>
                                            <td ></td>
                                            <td class="myDragTabs" width="50%" id="<%=table%>,Table" style="font-family:verdana;font-size:12px"><%=table%></td>
                                        </tr>
                                        <%}
                                                                    }%>
                                    </tbody>
                                </table>
                        </td>
                        <td id="dropTabs"  width="50%"></td>
                    </tr><tr>
                    <table>
                        <tr>
                            <td><input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveTables('Table')"></td>
                            <td><input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelTableFade()"></td>
                        </tr>
                    </table>
                    </tr>
                </table>
            </div>
            <%}
            %>
            <input type="hidden" id="tables" name="tables">
        </form>
             <script>
            var y="";
            var standardTimeDimTable = "";
            var UserDefTimeDimTable = "";
            $(document).ready(function() {
                $('table#filterTable2').columnFilters({alternateRowClassNames:['rowa','rowb'], excludeColumns:[0]});
//                getTableSet();

            });
            $(document).ready(function() {
                $('table#filterview2').columnFilters({alternateRowClassNames:['rowa','rowb'], excludeColumns:[0]});

            });
            function tableList(span){
                document.getElementById("activeConnection").value=(span.innerHTML);
                document.getElementById('type').style.display='block';
                document.getElementById('fade').style.display='block';
                //alert('list');
                //document.getElementById("type").style.display='';
            }


            function createConnection(){

                document.getElementById('connection').style.display='block';
                document.getElementById('fade').style.display='block';
                //alert('list');
                //document.getElementById("type").style.display='';
            }


            function getTableSet()
            {
                var denomFlag = "false";
                var infoFlag = "false";
                var oldTableArray=new Array;
                // alert(document.getElementById('tvtype').value);
                if(document.getElementById('tvtype').value=="Tables")
                {
                    document.getElementById('tableList').style.display='';
                    document.getElementById('viewList').style.display='none';
                    var oldtablesVar= document.getElementById("oldtables").value
                    if(oldtablesVar!=null){}
                    oldtablesVar=oldtablesVar.replace("[","").replace("]","").toString()
                    var oldTableArray=oldtablesVar.split(",")
                }
                    var Str="";
                    for(var i =0;i<oldTableArray.length;i++){
                        Str+=oldTableArray[i]+"<br/>"
                        if(oldTableArray[i]==' PR_DAY_DENOM'){
                            denomFlag = "true";
                        }
                        else if(oldTableArray[i]=='PR_DAY_INFO'){
                            infoFlag = "true";
                        }
                        }
                        if(denomFlag=="true"){
                            //alert("denom flag false")
                            standardTimeDimTable = standardTimeDimTable+'PR_DAY_DENOM,Table~';
                        }
                        if(infoFlag=="true"){
                            //alert("info flag false")
                            UserDefTimeDimTable = UserDefTimeDimTable+'PR_DAY_INFO,Table~';
                        }
                    document.getElementById('dropTabs').innerHTML=Str;


                if(document.getElementById('tvtype').value=="Views")
                {
                    document.getElementById('tableList').style.display='none';
                    document.getElementById('viewList').style.display='';
                    document.getElementById('dropTabs').innerHTML='';

                }
            }

            function saveTables()
            {
              
                var tabNames = new Array();
           
                tabNames = y.split("~");
              
                var names = new Array();
                var type = new Array();

                for(var i=0;i<tabNames.length;i++){
                    var s = tabNames[i].split(",");
                    names[i] = s[0];
                   
                    type[i] = s[1];
                }
                var flag = '0';
                var dayInfoFlag = '0';

                for(var i=0;i<names.length;i++){
                    if(names[i]=='PR_DAY_DENOM'){
                        flag = '1';
                    }
                    if(names[i]=='PR_DAY_INFO'){
                        dayInfoFlag = '1';
                    }
                }
                if(flag=='0'){
                    if(standardTimeDimTable==""){
                    y = y+'PR_DAY_DENOM,Table~';
                    }
                }
                if(dayInfoFlag=='0'){
                    if(UserDefTimeDimTable==""){
                    y = y+'PR_DAY_INFO,Table~';
                    }
                }
              
                document.getElementById("tables").value = y;
                document.myForm.action="pbSaveTables.jsp";
                document.myForm.submit();

                //window.opener.document.getElementById('fade').style.display='none';

            }
            function temp(){
                // alert('Hello');
            }
            function cancelTableFade()
            {
                // window.opener.document.getElementById('fade').style.display='none';
                parent.cancelTableList1();
                //window.close();
            }

            $(function() {

                $(".myDragTabs").draggable({

                    helper:"clone",
                    effect:["", "fade"]
                });


                $("#dropTabs").droppable(
                {

                    activeClass:"blueBorder",
                    accept:'.myDragTabs',

                    drop: function(ev, ui) {
                        /* for(a in ui.draggable){
    //alert(ui.draggable.html());
    //alert(a)
    }*/
                        //$draggableTables.html(ui.draggable.html());
                        //alert(ui.draggable.html());
                        var x=$("#dropTabs").html();
                       
                        y = y+ui.draggable.attr('id')+"~";
                    
                        $("#dropTabs").html(x+ui.draggable.html()+"<br>");
                        var z = ui.draggable.attr('id');
//                        alert("z is "+z)
//                        document.getElementById(z).style.display='none';

                    }

                }
            );

            });

        </script>
    </body>
</html>