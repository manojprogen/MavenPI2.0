
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>

        <link href="../css/myStyles.css" rel="stylesheet" type="text/css">
        <script>
            function getconnection()
            {
                document.myForm.action="pbCheckConnection.jsp";
                document.myForm.submit();
            }

            function getdatabase()
            {
                // alert(document.getElementById('dbname').value);
                if(document.getElementById('dbname').value=="oracle")
                {
                    document.getElementById('oraclediv').style.display='';
                    document.getElementById('exceldiv').style.display='none';
                    document.getElementById('dbcode').value = '1';
                    alert("dbcode-->"+document.getElementById('dbcode').value);
                }

                if(document.getElementById('dbname').value=="excel")
                {
                    document.getElementById('oraclediv').style.display='none';
                    document.getElementById('exceldiv').style.display='';
                    document.getElementById('dbcode').value = '2';
                    alert("dbcode-->"+document.getElementById('dbcode').value);
                }
                if(document.getElementById('dbname').value=="none")
                    {
                        document.getElementById('oraclediv').style.display='none';
                        document.getElementById('exceldiv').style.display='none';
                    }

            }
        </script>
    </head>
    <body>




        <div id="connection" style="background-color:#66CCFF;width:700px">
            <form name="myForm" method="post">
                <input type="hidden" id="dbcode" name="dbcode">
                <br><br><br><br><br>
                <center>

                    <table style="width:37%">
                        <tr>
                            <td class="myHead" style="width:58%">
                                Connection Name
                            </td>
                            <td  style="width:58%">
                                <input type="text" name="connectionname">
                            </td>
                        </tr>
                        <tr>
                            <td class="myHead" style="width:58%">
                                Database
                            </td>
                            <td style="width:58%">
                                <select id="dbname" onchange="getdatabase()" style="width:146px">
                                    <option value="none">----select----</option>
                                    <option value="oracle">Oracle</option>
                                    <option value="excel">Excel</option>
                                </select>
                    </td></tr></table>
                    <div id="oraclediv" style="display:none;width:700px;border-width:thick">
                        <table style="width:37%">
                            <tr>
                                <td class="myHead" style="width:58%">
                                    Username
                                </td>
                                <td style="width:58%">
                                    <input type="text" name="username">
                                </td>
                            </tr>
                            <tr>
                                <td class="myHead" style="width:58%">
                                    Password
                                </td>
                                <td style="width:58%">
                                    <input type="text" name="password">
                                </td>
                            </tr>
                            <tr>
                                <td class="myHead" style="width:58%">
                                    Server
                                </td>
                                <td style="width:58%">
                                    <input type="text" name="server" value="localhost">
                                </td>
                            </tr>
                            <tr>
                                <td class="myHead" style="width:58%">
                                    ServiceId
                                </td>
                                <td style="width:58%">
                                    <input type="text" name="Serviceid">
                                </td>
                            </tr>
                            <tr>
                                <td class="myHead" style="width:58%">
                                    Port
                                </td>
                                <td style="width:58%">
                                    <input type="text" name="Port" value="1521">
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div id="exceldiv" style="display:none;width:700px">
                        <table style="width:37%">  <tr>
                                <td class="myHead" style="width:58%">
                                    UserName
                                </td>
                                <td style="width:58%">
                                    <input type="text" name="excelUserName">
                                </td>
                            </tr>
                            <tr>
                                <td class="myHead" style="width:58%">
                                    Password
                                </td>
                                <td style="width:58%">
                                    <input type="text" name="excelPassword">
                                </td>
                        </tr>
                        <tr>
                                <td class="myHead" style="width:58%">
                                    DataSourceName
                                </td>
                                <td style="width:58%">
                                    <input type="text" name="exceldsn">
                                </td>
                        </tr>
                        <tr>
                                <td class="myHead" style="width:58%">
                                    Select File
                                </td>
                                <td style="width:58%">
                                    <input type="file" name="excelfile">
                                </td>
                        </tr>
                        </table>
                    </div>

                    <table>
                        <tr>
                            <td><input type="button" class="btn" value="connect" onclick="getconnection()"></td>
                        </tr>
                    </table>


                </center>
            </form>
        </div>
    </body>
</html>
