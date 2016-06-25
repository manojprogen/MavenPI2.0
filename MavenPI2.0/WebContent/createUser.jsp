<%--sreekanth--%>


<%@page  contentType="text/html" pageEncoding="UTF-8" import="java.sql.Connection,utils.db.ProgenConnection,prg.db.PbDb,prg.db.PbReturnObject,com.progen.bugDetails.BugDetailsDAO"%>

<%
            PbReturnObject buglist = new PbReturnObject();
            Connection con = ProgenConnection.getInstance().getBugConn();
            PbDb pbdb = new PbDb();
            String query = "SELECT * FROM customer_master";
            PbReturnObject list = pbdb.execSelectSQL(query, con);
            //////////////////////.println("=========" + list.getRowCount());
            String userId = "";
            userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
            String contextPath=request.getContextPath();

%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="javascript/ui.tabs.js"></script>
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/jquery_facebook.alert.js"></script>
        <link href="<%=contextPath%>/jQuery/jquery/themes/base/jquery.alerts.css" rel="stylesheet" type="text/css">
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/jquery.alerts.js"></script>
        <title></title>
       
    </head>
    <body>
        <div>
            <form name="createuser" action="" method="post">
                <table>
                    <tbody>
                        <tr>
                            <td>
                                User Name:
                            </td><td>
                                <input type="text" name="username" id="username"style="width:100%" >
                            </td>

                        </tr>
                        <tr><td>
                                Company Name:
                            </td>

                            <td>
                                <select name="companyname" id="companyname" >
                                    <option value="">---select---</option>
                                    <%for (int i = 0; i < list.getRowCount(); i++) {%>
                                    <option value="<%=list.getFieldValue(i, "CUSTOMER_ID")%>"><%=list.getFieldValue(i, "CUSTOMER_NAME")%> </option>
                                    <%}%>
                                </select>
                            </td>
                        </tr>
                        <tr><td>
                                Email ID:
                            </td>

                            <td>
                                <input type="text" name="mailid" id="mailid" style="width:100%">
                            </td>
                        </tr>
                        <tr><td>

                            </td>

                            <td>
                                <input type="button" name="save" value="Save" class="navtitle-hover" onclick="saveusers()">
                            </td>
                        </tr>


                    </tbody>

                </table>

            </form>
        </div>
 <script language = "Javascript">
            function saveusers(){
                var mailid =document.getElementById("mailid").value;
                var check =echeck(mailid);
                if(check==true){
                    document.forms.createuser.action='bugDetailsAction.do?param=saveUserDetails'
                    document.forms.createuser.submit();
                    parent.$('#createuserDialog').dialog('close');
                }else{
                  
                }

            

            }
            function echeck(str) {

                var at="@"
                var dot="."
                var lat=str.indexOf(at)
                var lstr=str.length
                var ldot=str.indexOf(dot)
                if (str.indexOf(at)==-1){
                    jAlert("Invalid E-mail ID")
                    return false
                }

                if (str.indexOf(at)==-1 || str.indexOf(at)==0 || str.indexOf(at)==lstr){
                      jAlert("Invalid E-mail ID")
                    return false
                }

                if (str.indexOf(dot)==-1 || str.indexOf(dot)==0 || str.indexOf(dot)==lstr){
                    jAlert("Invalid E-mail ID")
                    return false
                }

                if (str.indexOf(at,(lat+1))!=-1){
                     jAlert("Invalid E-mail ID")
                    return false
                }

                if (str.substring(lat-1,lat)==dot || str.substring(lat+1,lat+2)==dot){
                    jAlert("Invalid E-mail ID")
                    return false
                }

                if (str.indexOf(dot,(lat+2))==-1){
                     jAlert("Invalid E-mail ID")
                    //alert("Invalid E-mail ID")
                    return false
                }

                if (str.indexOf(" ")!=-1){
                    jAlert("Invalid E-mail ID")
                    // alert("Invalid E-mail ID")
                    return false
                }

                return true
            }

            function ValidateForm(){
                var emailID=document.frmSample.txtEmail

                if ((emailID.value==null)||(emailID.value=="")){
                    alert("Please Enter your Email ID")
                    emailID.focus()
                    return false
                }
                if (echeck(emailID.value)==false){
                    emailID.value=""
                    emailID.focus()
                    return false
                }
                return true
            }
        </script>
    </body>
</html>
