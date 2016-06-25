<%@page import="utils.db.ProgenConnection" %>
<%@page import="java.sql.*"%>
<html>

<head>
<title>jQuery Column Filters Demo</title>

<script language="JavaScript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
<script language="JavaScript" src="<%=request.getContextPath()%>/querydesigner/JS/jquery.columnfilters.js"></script>

<style type="text/css">

table tr th {
	background-color: #d3DADE;
	padding: 3px;
}
table tr.rowb { background-color:#EAf2FD; }

table tr.filterColumns td { padding:2px; }

body { padding-bottom:150px; }


</style>

</head>

<body>

<%
Connection con=null;
Statement st=null;
ResultSet rs=null;
//con= new ProgenConnection().getConnection();
 Class.forName("oracle.jdbc.driver.OracleDriver");
 con=con = ProgenConnection.getInstance().getCustomerConn();
st=con.createStatement();
String sql="select tname from tab";
rs=st.executeQuery(sql);


%>



<div id="tableList" style="height:495px;width:340px;overflow:auto">
<table id="filterTable2" cellspacing="0" cellpadding="0">
<thead>
<tr>
    <th></th><th>Name</th>
</tr>
</thead>
<tfoot></tfoot>
<tbody>
    
    <%
    while(rs.next()){%>

    <tr>

<td ><input type="checkbox" name="chk1" value="<%=rs.getString(1)%>"></td>
    <td width:340px><%=rs.getString(1)%></td>
</tr>

    <%
        }
    %>
<%--
<tr>

    <td><input type="checkbox" value="Tom"></td>
    <td>Table1</td>
</tr>
<tr>

    <td><input type="checkbox" value="Tom1">
        <td>Table2</td>
</tr>
<tr>

    <td><input type="checkbox" value="Tom2">
        <td>Table3</td>
</tr>
<tr>

    <td><input type="checkbox" value="Tom3">
        <td>Table11</td>
</tr>
<tr>

    <td><input type="checkbox" value="Tom4">
        <td>Table22</td>
</tr>
<tr>

    <td><input type="checkbox" value="Tom5">
        <td>Table33</td>
</tr>
--%>
</tbody>
</table>

</div>




<script>
	$(document).ready(function() {
		$('table#filterTable2').columnFilters({alternateRowClassNames:['rowa','rowb'], excludeColumns:[0]});

	});
</script>

<br />

</body>

</html>