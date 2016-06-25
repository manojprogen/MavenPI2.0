
<%@page  contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbDb,com.progen.reportdesigner.db.ReportTemplateDAO,com.progen.reportdesigner.action.ReportTemplateAction,prg.db.PbReturnObject,java.util.ArrayList" %>

<html>
    <head>
        <title>pi EE</title>

        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />


        

        <style type="text/css">
            .fontsty{
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                background-color:#bdbdbd;
            }
            .white_content {
                display: none;
                position: absolute;
                top: 30%;
                left: 35%;
                width: 50%;
                height:50%;
                padding: 16px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
            }
            *{
                font:11px verdana;
            }
        </style>

    </head>
    <%
            PbDb pbdb = new PbDb();
            String query = "select * from prg_ar_users where pu_login_id not in ('PROGEN')";
            PbReturnObject list = pbdb.execSelectSQL(query);
            list.writeString();
            //added by uday
            String allUserNames = "";
    %>

    <body id="mainBody" onload="loadpage()">

        <div id="light" align="center" class="white_content"><img  alt="Page is Loading" src='images/ajax.gif'></div>
        <form name="myForm"  method="post">
            <table align="center" id="tablesorter" class="tablesorter"  style="width:90%" >
                <thead>
                    <tr>
                        <th>&nbsp;</th>
                        <th>User Id</th>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Email</th>
                    </tr>
                </thead>
                <tbody>
                    <%int i = 0;
            for (i = 0; i < list.getRowCount(); i++) {
                //added by uday
                if (allUserNames.equalsIgnoreCase("")) {
                    allUserNames = list.getFieldValueString(i, "PU_LOGIN_ID");
                } else {
                    allUserNames = allUserNames + "," + list.getFieldValueString(i, "PU_LOGIN_ID");
                }
                    %>
                    <tr>
                        <td style="width:20px">
                            <input type="checkbox" id="chkusers" name="chkusers" value="<%=list.getFieldValueInt(i, 0)%>">
                        </td>
                        <td valign="top" style="width:100px">
                            <%=list.getFieldValueString(i, 1)%>
                        </td>

                        <td valign="top" style="width:100px">
                            <%=list.getFieldValueString(i, 2)%>
                        </td>
                        <td valign="top" style="width:100px">
                            <%=list.getFieldValueString(i, 4)%>
                        </td>
                        <td valign="top" style="width:100px">
                            <%=list.getFieldValueString(i, 7)%>
                        </td>
                    </tr>
                    <%}%>
                <input type="hidden" name="limit" id="limit" value="<%=i%>">

                </tbody>
            </table>
            <input type="hidden" id="selecteduser" name="selecteduser">
            <input type="hidden" name="userId" id="userId">
            <br><br>



            <%if (list.getRowCount() == 0) {
            %>
            <table>
                <tr>
                    <td>No Users Available</td>
                </tr>
                <tr>
                    <Td><input  class="navtitle-hover" style="width:auto" type="button" value="Create User" onclick="javascript:gohome();"><Td>
                </tr>
            </table>
            <%} else {%>


            <!-- added by uday -->
            <center>
                <table>
                    <tr>
                        <td>
                            <input class="navtitle-hover" style="width:auto" type="button" value="Copy User" onclick="javascript:copyUser('<%=allUserNames%>')">
                        </td>
                    </tr>
                </table>
            </center>

            <%}%>

            <%--</div>--%>
            <br>

        </form>
<script type="text/javascript">

            //added by uday
            function copyUser(allUserNames) {
                var user = "";
                var i=0;
                var obj = document.forms.myForm.chkusers;
                if(isNaN(obj.length))
                {
                    if(document.forms.myForm.chkusers.checked)
                    {
                        parent.$('#copyUser').dialog('open');
                        user = document.forms.myForm.chkusers.value;
                        var frameObj = parent.document.getElementById("copyUserFrame");
                        frameObj.src =  "pbCopyUser.jsp?checkUser="+user+"&allUserNames="+allUserNames;
                    }
                    else
                    {
                        alert("Please select user to copy")
                    }
                }
                else
                {
                    for(var j=0;j<obj.length;j++)
                    {
                        if(document.forms.myForm.chkusers[j].checked==true)
                        {
                            i++;
                            user=document.forms.myForm.chkusers[j].value;
                        }
                    }

                    if(i>1)
                    {
                        alert("Please select only one user to copy");
                    }
                    else if(i==0)
                    {
                        alert("Please select user to copy");
                    }
                    else
                    {
                        parent.$('#copyUser').dialog('open');
                        var frameObj = parent.document.getElementById("copyUserFrame");
                        frameObj.src =  "pbCopyUser.jsp?checkUser="+user+"&allUserNames="+allUserNames;
                    }
                }
            }

            function saveNewUser(oldUserId,userName,password) {
                parent.$('#copyUser').dialog('close');
                var path = '<%=request.getContextPath()%>'
                $.ajax({
                        url: path+"/userLayerAction.do?userParam=copyUser&selectedUserId="+oldUserId+"&newUserName="+userName+"&newUserPassword="+password,
                        success: function(data){
                            document.forms.myForm.action = path+"/home.jsp#Admin";
                            document.forms.myForm.submit()
                        }
                    });                
            }
            ///end of uday


        </script>
    </body>
</html>