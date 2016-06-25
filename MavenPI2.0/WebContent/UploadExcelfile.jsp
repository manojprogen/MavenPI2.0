<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.i18n.TranslaterHelper,java.util.Locale,java.util.List,java.io.File,prg.db.PbReturnObject,java.util.ArrayList,prg.db.PbDb,utils.db.ProgenConnection,java.sql.Connection"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%-- 
    Document   : UploadExcelfile
    Created on : Jun 27, 2014, 7:28:37 AM
    Author     : Amar
--%>
<%--<%@page import="org.apache.tomcat.util.http.fileupload.FileUtils"%>--%>
<%
    String qry = "";
    qry = "select * from PRG_USER_CONNECTIONS";
    PbDb pbdb = new PbDb();
    PbReturnObject list = null;
    list = pbdb.execSelectSQL(qry);
    int vals = 0;
    vals = list.getRowCount();
                    
                     String folderPath=(String)session.getAttribute("reportAdvHtmlFileProps")+"/importExcel";
                     File folderDir=new File(folderPath);
                     File[] allFilesAndDirs = folderDir.listFiles();
                     String fileStr="";
		     for (File f1 : allFilesAndDirs) {
                         
                         fileStr=fileStr+","+f1.getName();
                         
                     }
                    
            //added by Dinanath dec 2015
            Locale cle = null;
            cle = (Locale) session.getAttribute("UserLocaleFormat");
%>

<html:html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title>Upload File</title>

        <html:base/>
       
        <style type="text/css">
            *{font:10px verdana;}
        </style>
    </head>
    <body>
         <table  align="center">
             <h1 align="center" style="color:blue"><%=TranslaterHelper.getTranslatedInLocale("Upload_Template_Sheet_Data", cle)%></h1>
        </table>
        <html:form  action="createtableAction.do?param=uploadExcelFile" method="post" enctype="multipart/form-data">

            <table width="40%" align="center" class="migrate">
                <tr style="width:100%">
                    <td align="left" colspan="2"><font color="red"><html:errors/></font>
                </tr>
                 <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr style="width:100%">
                    <td align="left" colspan="1" class="migrate"><%=TranslaterHelper.getTranslatedInLocale("Template_Name", cle)%></td>
                    <td align="left" colspan="1" class="migrate"><html:text property="tablename" styleId="tablename" style="background-color:lightgoldenrodyellow; color:black;"/></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
<!--                <tr>
                    <td align="left" colspan="1" class="migrate">Connection Name</td>
                    <td align="left" colspan="1" class="migrate">
                        <select name="connid" id="connid" >
                            <% for (int i = 0; i < list.getRowCount(); i++) {
                            %>
                            <option value="<%=list.getFieldValueInt(i, 0)%>"><%=list.getFieldValueString(i, 1)%></option> 
                            <%}%>
                        </select>
                    </td>                   
                </tr> -->
             
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr style="width:100%">
                    <td align="left" colspan="1" class="migrate"><%=TranslaterHelper.getTranslatedInLocale("File_Name", cle)%>: <label style="color:Red">(<%=TranslaterHelper.getTranslatedInLocale("xlsonly", cle)%></label></td>
                    <td align="left" colspan="1" class="migrate"><html:file property="filename" styleId="filename" style="background-color:lightgoldenrodyellow; color:black;"/></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr style="width:100%">
                    <td align="center" colspan="2" class="migrate"><html:submit onclick ="return Checkfiles()" style="background-color:#47A3DA; color:white;" ><%=TranslaterHelper.getTranslatedInLocale("Upload_File", cle)%></html:submit>
                    </td>
                </tr>
                 <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
            </table>

        </html:form>
        
 <script type="text/javascript">
            function Checkfiles()
            {
                 
                var fileString ='<%= fileStr%>';
               // alert(fileString);
                var fileList = fileString.split(",");
//                alert(fileList);
                var fup = document.getElementById('filename');
                var tempName = document.getElementById('tablename');
                var tempFileName = tempName.value;
                tempFileName = tempFileName+".xls";
                var fileName = fup.value;
                var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
                if( fileName!=null && ext == "xls" )
                {
                    for(var count=0;count<fileList.length;count++){
                        if(fileList[count] === tempFileName){
                         alert("This template name has been used already. please give other name!");   
                         return false;
                        }
                    }
                    return true;
                }
                else
                {
                  
                    alert("Upload .xls files only.");
                    // fup.focus();
                    // document.getElementById("checkDiv").style.display ='block'
                   
                    return false;
                }
             
            }
           
          
        </script>
    </body>
</html:html>

