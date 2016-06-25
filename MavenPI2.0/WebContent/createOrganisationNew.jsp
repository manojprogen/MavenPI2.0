

<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.*,java.util.Calendar"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%
        String themeColor="blue";
        if(session.getAttribute("theme")==null)
                session.setAttribute("theme",themeColor);
            else
                themeColor=String.valueOf(session.getAttribute("theme"));
        String contextPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Organisation Page</title>
         
         <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
         <script type="text/javascript" src="<%=contextPath%>/javascript/ui.dialog.js"></script>
         <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
<!--        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />-->
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/ui.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/ui.datepicker.js"></script>
        <style type="text/css">
            *{
                font:11px verdana;
            }
            .leftcol {
                width:60%;
            }
            .fontsty{
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                background-color:#bdbdbd;
            }
            .ui-corner-all{
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
            }
        </style>
      
        <%
        Calendar calendar = Calendar.getInstance();
        PbReturnObject orgObj=new PbReturnObject();
        PbDb pbdb=new PbDb();
        orgObj=pbdb.execSelectSQL("select folder_id,folder_name from prg_user_folder");

        ////////////////////////////////////////////////////////.println.println(" accountFolderId in jsp "+accountFolderId);
%>

    </head>
    <body>
        <form name="myForm" method="post" action="">


            <div class="ui-corner-all"  style="width:100%;border:0px solid silver" align="center">
                <br><br><br><br>
                <center>
                    <div id="leftcol" class='leftcol'  align="center">
                        <center>
                        <font size="1px"> Fields marked <span style="color:red">*</span> are MANDATORY </font></center>
                        <br><br>
                        <table width="100%">
                            <tr width="100%">
                                <td>
                                <table width="100%"><tr>
                                        <td width="50%"><label class="label"><span style="color:red">*</span>Company Name</label></td>
                                        <td width="50%"><input type="text" name="orgName" id="orgName" style="width:90%" title="Enter Company Name(Min. 6 Characters)"></td>
                                </tr></table>
                                </td>
                            </tr>

                            <tr width="100%">
                                <td>
                                <table width="100%"><tr>
                                        <td width="50%">&nbsp;&nbsp;<label class="label">Description</label></td>
                                        <td width="50%"><input type="text" style="width:90%" name="orgDesc"  id="orgDesc" title="Enter Company Description"></td>
                                </tr></table>
                                </td>
                            </tr>

                        <%--    <tr width="100%">
                                <td>
                                <table width="100%"><tr>
                                        <td width="50%">&nbsp;&nbsp;<label class="label">Business Role</label></td>
                                        <td width="50%">
                                        <select name="accountFolderId" id="accountFolderId" style="width:92%">

                                          <option value="">--Select-</option>

                                            <%  for(int i=0;i<orgObj.getRowCount();i++){%>
                                            <option value="<%=orgObj.getFieldValueString(i,"FOLDER_ID")%>"><%=orgObj.getFieldValueString(i,"FOLDER_NAME")%></option>

                                            <%}%>
                                        </select>
                                        </td>
                                </tr></table>
                                </td>
                            </tr>
                          --%>

                            <tr width="100%">
                                <td>
                                <table width="100%"><tr>
                                        <td width="50%">&nbsp;&nbsp;<label class="label">Start Date</label></td>
                                        <td width="50%"><input type="text" readonly style="width:90%" name="strtDate" id="startDate" value="<%=(calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DATE) + "/" + calendar.get(Calendar.YEAR)%>"></td>
                                </tr></table>
                                </td>
                            </tr>
                            <tr width="100%">
                                <td>
                                <table width="100%"><tr>
                                        <td width="50%"><label class="label">End Date</label></td>
                                        <td width="50%"><input type="text" readonly style="width:90%" name="endDate" id="endDate"></td>
                                </tr></table>
                                </td>
                            </tr>
                        </table>
                                         <input type="hidden" name="accountFolderId" id="accountFolderId" value="">
                        <br><br>
                        <table align="center">
                            <tr>
                                <%-- <td>
                                <input type="button" class="navtitle-hover" style="width:auto"   id="cancel" value="Cancel" onclick="goBack();" >
                                </td>--%>
                                <td>
                                    <input type="button" class="navtitle-hover" style="width:auto" id="Save" value="Save" onclick="return validateOrg();" >
                                </td>

                                <td>
                                    <input type="reset" class="navtitle-hover" style="width:auto"  id="reset" value="Reset"  >
                                </td>
                            </tr>
                        </table>

                        <div id="accNewRole"  title="Select Role" style="display:none">
                           <iframe  id="accNewRoleData" NAME='accNewRoleData'  frameborder="0" height="100%" width="100%" STYLE='overflow:auto;display:none' frameborder="0"   SRC='#'></iframe>
                        </div>

                    </div>
                </center>
            </div>
        </form>
        <br>
          <script>
            $(document).ready(function(){
                $("#endDate").datepicker();
            });
            function validateOrganisationName(){
                var orgName = document.myForm.orgName.value;
                alert('in validateOrganisationName '+orgName)
                $.ajax({
                    url: 'organisationDetails.do?param=checkOrganisationName&orgName='+orgName,
                    success: function(data){
                        if(data!=""){
                            document.getElementById('duplicate').innerHTML = "Company Name already exists";
                            document.getElementById('Save').disabled = true;
                        }
                        else if(data==''){
                            validateOrg();
                        }
                    }
                });
            }
            function validateOrg(){
            <%--var validOrgName=validateOrganisationName();--%>
                var orgName = document.myForm.orgName.value;
                var orgDesc = document.getElementById("orgDesc").value;
                var strtDate = document.getElementById("startDate").value;
                var endDate = document.myForm.endDate.value;
                    var startDate = new Date(document.getElementById("startDate").value);
                    var expireDate = new Date(document.myForm.endDate.value);
                var accountFolderId = document.getElementById("accountFolderId").value;

                if(orgName == null ||orgName =="")
                {
                    alert("Please Enter Company Name");
                    return false;
                }
                   else if(startDate>=expireDate){
                        alert('End date should be greater than the Start Date')
                    }else{
            <%--if(validOrgName==true){--%>
                    $.ajax({
                        url: "organisationDetails.do?param=saveNewOrg&orgName="+orgName+"&orgDesc="+orgDesc+"&strtDate="+strtDate+"&endDate="+endDate+"&accountFolderId="+accountFolderId,
                        success: function(data){
                            parent.document.forms.myFormH.action = "<%=request.getContextPath()%>/AdminTab.jsp#User_Accounts";
                            parent.document.forms.myFormH.submit();
                }
                    });
            <%--}--%>
            <%--document.myForm.action = "organisationDetails.do?param=saveNewOrg";
                        document.myForm.submit();
            refreshAccount();--%>
                 }
            }

             function initialogRole(){
                if ($.browser.msie == true){
                    $("#accNewRole").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 500,
                        width: 700,
                        position: 'justify',
                        modal: true

                         });

                           }
                else{
                    $("#accNewRole").dialog({
                            autoOpen: false,
                            height: 400,
                            width: 620,
                            position: 'justify',
                            modal: true
                        });

                  }
                }
            function refreshAccount()
            {
                parent.refreshAccountParent();
            }
                function tabmsg1(){
                    document.getElementById('orgDesc').value = document.getElementById('orgName').value;
                }


        </script>
    </body>
</html>
