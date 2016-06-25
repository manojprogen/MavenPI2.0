<%@page import="prg.targetparam.qdparams.PbTargetParamParams"%>
<%@page import="prg.targetparam.qdclient.PbTargetParamManager"%>
<%@page import="prg.db.Session"%>
<%@page import="prg.db.PbReturnObject"%>
<%@ page import="utils.db.*"%>
<%@ page import="java.util.ArrayList"%>


<html>
<Head>
            <script type="text/javascript" src="javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
            <script type="text/javascript" src="../JS/jquery.tablesorter.js"></script>
            <link rel="stylesheet" type="text/css" href="../css/style.css" />
            <script type="text/javascript" src="../JS/jquery.tablesorter.pager.js"></script>
            <link rel="stylesheet" type="text/css" href="../css/jquery.tablesorter.pager.css" />

    <script src="../JS/myScripts.js" type="text/javascript"></script>
    <link href="../css/myStyles.css" rel="stylesheet" type="text/css"/>


    
     <script language="text/javascript">
                   $(document).ready(function() {
                $("#myTable")
                .tablesorter({widthFixed: true, widgets: ['zebra']})
                .tablesorterPager({container: $("#pager")});
                    });
    </script>


</Head>
<%  

    int rowCount=0;
   
        PbTargetParamParams targetParams = new PbTargetParamParams();
        PbTargetParamManager targetClient = new PbTargetParamManager();
        Session targetSession = new Session();
        String userId = request.getParameter("userId");
        if(userId==null)
        {
            userId = (String)session.getAttribute("userId");
            //userId="41";
        }
        userId="41";
        //////////////////////////////////////////////////////////////////////////////.println("userId //-- "+userId);
        targetParams.setUserId(userId); //get it from User Session
        session.setAttribute("userId", userId);
        targetSession.setObject(targetParams);
        PbReturnObject targetList = targetClient.getTargetsListU(targetSession);
        targetList.writeString();
        int measureId = targetList.getFieldValueInt(0,"MEASURE_ID");
        //////////////////////////////////////////////////////////////////////////////.println("targetList - ");
        session.setAttribute("measureId",Integer.valueOf(measureId));
        if(session.getAttribute("measureId")!=null)
        //////////////////////////////////////////////////////////////////////////////.println(" --- "+((Integer)session.getAttribute("measureId")).intValue());

        rowCount = targetList.getRowCount();
        //////////////////////////////////////////////////////////////////////////////.println("RowCount is: "+rowCount);
        session.setAttribute("userId",userId);

        %>
   <Center>
   <Form name="myForm2" METHOD="get">
   <Table id="myTable" WIDTH="=50%" class="tablesorter">
       <thead>
            <tr>
                <th></th>
                <th>Target Name</th>
               
            </tr>
        </thead>
        <tbody>
            <%for(int p=0;p<targetList.getRowCount();p++){%>
            <Tr><Td><Input type="checkbox" name="chk1"></Td><Td><%=targetList.getFieldValueString(p,"TARGET_NAME")%></Td></Tr>
            <%}%>
        </tbody>

   </Table>
    <Br>
    <Table>
        <Tr>
            <%--<Td><Input class="btn" type="button" value="Home" onclick="javascript:goToUserHome();"></Td>--%>
            <Td><Input class="btn" type="button" value="Create" onclick="javascript:createTarget();"></Td>
            <Td><Input class="btn" type="button" value="Edit" onclick="javascript:editTarget();"></Td>
            <Td><Input class="btn" type="button" value="Delete" onclick="javascript:deleteTarget();"></Td>
            <Td><Input class="btn" type="button" value="Enter Data" onclick="javascript:defineTarget();"></Td>
        </Tr>
    </Table>
    <Table>
        <Tr>
            <%--<Td><Input class="btn" type="button" value="Update" onclick="javascript:updateTarget();"></Td>--%>
            <%--<Td><Input class="btn" type="button" value="Delete" onclick="javascript:deleteTarget();"></Td>--%>
            <Td><Input class="btn" type="button" value="Copy" onclick="javascript:copyTarget();"></Td>
            <Td><Input class="btn" type="button" value="Publish" onclick="javascript:publishTarget();"></Td>
            <Td><Input class="btn" type="button" value="Expire" onclick="javascript:expireTarget();"></Td>
        </Tr>
        <input type="hidden" name="targetNames" id="targetNames">
    </Table>
</Form>
</Center>
   

</body>
</html>
