<%-- 
    Document   : UploadExcelTable
    Created on : Feb 11, 2010, 7:45:49 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<html:html>
    <head>
        <title></title>
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/ui.dialog.js"></script>
        <script type="text/javascript">
            function checkfun()
            {
                var file = document.getElementById("file").value;
                if(file.length==0){
                    alert("Please Upload A File");
                    return false;
                }
                else{
                    //alert(file)
                    parent.closeexclediv();
                }
            }
            function testconn(){
                var datasrcname = document.getElementById("datasourcename").value;
                //alert("datasrcname"+datasrcname)
                $.ajax({
                   url:'excelaction.do?exceltype=chechkExcelConnection&dsnname='+datasrcname,
                   success:function(data){
                       if(data=="Connection Successful"){
                          alert(data)
                          return false;
                       }
                       else{
                           alert(data)
                           return false;
                       }
                   }
                });
                return false;
            }
        </script>
        <html:base/>
    </head>
    <body>
        <center>
            <html:form action="excelaction.do?exceltype=saveExcelConnection" method="post"  enctype="multipart/form-data" onsubmit="checkfun()">
            <table style="width:100%" align="center">

                <tr>
                    <td align="left" colspan="2">
                        <font color="red"><html:errors/></font>
                </tr>
                <tr>
                    <td align="right">
                        Connection Name
                    </td>
                    <td>
                        <html:text property="connectionname" styleId="connectionname"></html:text>
                    </td>
                </tr>
                <tr>
                    <td align="right">
                        DataSource Name
                    </td>
                    <td>
                        <html:text property="datasourcename" styleId="datasourcename"></html:text>
                        <%--<html:text property="Datasourcename" styleId="Datasourcename"></html:text>--%>
                    </td>
                </tr>
                <tr>
                    <td align="right">
                        Select File
                    </td>
                    <td>
                        <html:file property="file" styleId="file"/>
                    </td>
                </tr>
                <tr>
                    <td align="right">
                        <html:button property="testconnection" onclick="testconn()">Test Connection</html:button>
                    </td>
                    <td align="left">
                        <html:hidden property="dbname" styleId="dbname" value="excel"></html:hidden>
                        <html:submit>Save Connection</html:submit>
                    </td>
                </tr>
            </table>
        </html:form>
            </center>
    </body>
</html:html>
