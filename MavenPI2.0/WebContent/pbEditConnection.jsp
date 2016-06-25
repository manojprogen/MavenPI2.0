<%-- 
    Document   : pbEditConnection
    Created on : Nov 14, 2009, 5:13:16 PM
    Author     : Administrator
--%>


<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.datadisplay.client.*,com.progen.datadisplay.bean.*,prg.test.*,utils.db.SqlConnection,prg.db.PbReturnObject,utils.db.*,java.util.*,java.sql.*" %>

<link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />
<link href="stylesheets/StyleSheet.css" rel="stylesheet" type="text/css" />

<%
        String connectionId = null;
        if (request.getParameter("connectionId") != null) {
            connectionId = request.getParameter("connectionId");
        }

%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Edit Connection</title>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />

        <script language="javascript">
            function UpdateConnection(){
                /*
                 $.ajax({
                    url: 'editconn.do?parameter=updateUserConnection',
                    success: function(data){
                        if(data!=""){

                            paramDisp.innerHTML=data;
                        }
                    }
                });
                 */
                document.forms.myForm.action="editconn.do?parameter=updateUserConnection";//ConnectionAction
                document.forms.myForm.submit();
                parent.refreshEditConnection();
            }
            function cancelConnection(){
                parent.refreshEditConnection();
            }
        </script>
        <style>
            h3{
                color:#336699;
                font-size:14px;
                text-transform:uppercase;
            }
            .label{
                font-family:verdana;
                font-size:12px;
                font-weight:normal;
            }
            .ui-corner-all{
                -moz-border-radius-bottomleft:6px;
                -moz-border-radius-bottomright:6px;
                -moz-border-radius-topleft:6px;
                -moz-border-radius-topright:6px;
            }
        </style>
    </head>
    <body>
        <%

        String connection = connectionId;//request.getParameter("connection");
        //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("active connection" + connection);

        PbDataDisplayManager client = new PbDataDisplayManager();
        PbReturnObject pbro = client.getNetworkConnDetails(connectionId);
        // pbro.writeString();
        String dbConnectionName = pbro.getFieldValueString(0, "CONNECTION_NAME");
        String dbUserName = pbro.getFieldValueString(0, "USER_NAME");
        String dbPASSWORD = pbro.getFieldValueString(0, "PASSWORD");
        String dbSERVER = pbro.getFieldValueString(0, "SERVER");
        String dbSERVICEID = pbro.getFieldValueString(0, "SERVICE_ID");
        String dbSERVICENAME = pbro.getFieldValueString(0, "SERVICE_NAME");
        String dbPORT = pbro.getFieldValueString(0, "PORT");
        String dbDSNNAME = pbro.getFieldValueString(0, "DSN_NAME");
        String dbCONNECTIONTYPE = pbro.getFieldValueString(0, "DB_CONNECTION_TYPE");
        String dbDBNAME = pbro.getFieldValueString(0, "DBNAME");
       // String dbCLIENT = pbro.getFieldValueString(0, "CLIENT");
       // String dbSYSTEMNUM = pbro.getFieldValueString(0, "SYSTEMNUM");
        //pbro.writeString();
        // con = new ProgenConnection().getCustomerConn();

        %>
        <center>
            <Form name="myForm" method="post">
                <h3>Edit Network Connection</h3>
                <div class="ui-corner-all"  style="height:auto;width:60%;border:1px solid silver" align="center">
                <Table>
                        <tr>
                            <td style="height:4px">

                            </td>
                        </tr>
                    <Tr>

                    <Td>
                            <label class="label" >CONNECTION NAME</label>
                    </Td>
                    <Td>
                        <Input type="text" name="conName" value=<%=dbConnectionName%>>
                    </Td>
                        </Tr>
                    <tr>
                        <td>
                                <label class="label" >USER NAME</label>
                        </td>
                        <td>
                            <Input type="text" name="usrename" value=<%=dbUserName%>>
                        </td>
                    </tr><tr>
                        <td>
                                <label class="label" >PASSWORD</label>
                        </td>
                        <td>
                            <Input type="password" name="password" value=<%=dbPASSWORD%>>
                    </td></tr>
                    <tr>
                        <td>
                                <label class="label" >SERVER NAME</label>
                        </td>
                        <td>
                            <Input type="text" name="servername" value=<%=dbSERVER%>>
                    </td></tr>
                    <tr>
                        <td>
                                <label class="label" >SERVICE ID</label>
                        </td>
                        <td>
                            <Input type="text" name="serid" value=<%=dbSERVICEID%>>
                    </td></tr>
                    <tr>
                        <td>
                                <label class="label" >SERVICE NAME</label>
                        </td>
                        <td>
                            <Input type="text" name="servnane" value=<%=dbSERVICENAME%>>
                    </td></tr>
                    <tr>
                        <td>
                                <label class="label" >PORT</label>
                        </td>
                        <td>
                            <Input type="text" name="port" value='<%=dbPORT%>'>
                    </td></tr>
                    <tr>
                        <td>
                                <label class="label" >DSN NAME</label>
                        </td>
                        <td>
                            <Input type="text" name="DSNname" value=<%=dbDSNNAME%>>
                    </td></tr>
                    <tr>
                        <td>
                                <label class="label" >CONNECTION TYPE</label>
                        </td>
                        <td>
                            <Input type="text" name="conntype" value=<%=dbCONNECTIONTYPE%>>
                    </td></tr>
                    <tr>
                        <td>
                                <label class="label" >DBNAME</label>
                        </td>
                        <td>
                            <Input type="text" name="DBname" value=<%=dbDBNAME%>>
                    </td></tr>
                  <%--  <tr>
                        <td>
                                <label class="label" >CLIENT</label>
                        </td>
                        <td>
                            <Input type="text" name="clint" value=<%=dbCLIENT%>>
                    </td></tr>
                    <tr>
                        <td>
                                <label class="label" >SYSTEMNUM</label>
                        </td>
                        <td>
                            <Input type="text" name="sysnum" value=<%= dbSYSTEMNUM%>>
                        </td>
                        </tr>--%>
                        <tr>
                            <td style="height:4px">
                                <input type="hidden" value="<%=connectionId %>" name="connectionId" >
                            </td>
                        </tr>
                </Table>
                </div>
                <center><br><br>
                    <Table>
                        <Tr>
                            <Td><input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="UpdateConnection()"></Td>
                            <Td><input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelConnection()"></Td>
                        </Tr>

                    </Table>

                </center>
        </Form></center>
    </body>
</html>
