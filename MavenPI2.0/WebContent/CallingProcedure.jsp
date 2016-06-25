<%--
    Document   : CallingProcedure
    Created on : Nov 5, 2015, 7:37:52 PM
    Author     : DINANATH
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.i18n.TranslaterHelper,java.util.Locale,java.sql.CallableStatement,prg.db.SourceConn,java.sql.Connection"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%

//added by Dinanath dec 2015
            Locale cle = null;
            cle = (Locale) session.getAttribute("UserLocaleFormat");
            String contextPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="<%=contextPath%>/JS/jquery-1.11.1.js"></script>
        <title>JSP Page</title>

    </head>
    <body>
        <br>
        <br>
        <br>
        <label style="font-size: 14px;font-weight: 700;color: #3F5D7D;"><%=TranslaterHelper.getTranslatedInLocale("FOR_PROCEDURE_CALLING", cle)%><br/><br/></label>
        <center>
            <table ><tr>
                </tr>
                <td><strong><%=TranslaterHelper.getTranslatedInLocale("proc_name", cle)%></strong></td>
                <td><input type="text" value="" name="procedureName" id="procedureName" size="50"></td>
            <tr>
                    <td colspan="2"><br></td>
                </tr>
                <tr>
                    <td colspan="2"><center><input type="button" class="navtitle-hover"  onclick="callProcedure()" value="<%=TranslaterHelper.getTranslatedInLocale("Execute_Procedure", cle)%>"></center><br></td>
                </tr>
            </table>


        </center>

         <div id='loadingmetadata' class='loading_image' style="display:none;">
                <img alt=""  id='imgId' src='images/ajax.gif'  border='0px' style='position:absolute; left: 318px; top: 120px;width: 51%; height: 40%;opacity: 0.6;filter: alpha(opacity=60);'/>
</div>
         <script type="text/javascript">
            function callProcedure(){
                var procedureName=$("#procedureName").val();
                alert(procedureName)
                $("#loadingmetadata").show();

                  $.ajax({
                        async:false,
//                        data:{'sheetname':sheetname},
                        url:'createtableAction.do?param=ExecuteAnyProcedure&procedureName='+procedureName,
                        success:function(data) {
                            $("#loadingmetadata").hide();
                            alert(""+data);
//
                        }
                  });
            }

            </script>
    </body>
</html>
