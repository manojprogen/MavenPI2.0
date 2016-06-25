<%--
    Document   : UploadExcelTemplate
    Created on :Jul 15, 2015,
    Author     : Krishan Pratap
--%>


<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.i18n.TranslaterHelper,java.util.Locale,java.util.List,java.io.File,java.sql.Connection,utils.db.ProgenConnection,prg.db.PbDb,prg.db.PbReturnObject,java.util.ArrayList"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%

   String reportId=request.getParameter("reportid");
  

 
    String qry = "";
    qry = "select * from PRG_USER_CONNECTIONS";
    PbDb pbdb = new PbDb();
    PbReturnObject list = null;
    list = pbdb.execSelectSQL(qry);
    int vals = 0;
    vals = list.getRowCount();

                     String folderPath=(String)session.getAttribute("reportAdvHtmlFileProps")+"/importTemplate"+"/"+reportId;
                     File folderDir=new File(folderPath);
                     if(!folderDir.exists()){
                         folderDir.mkdir();
                         }
                     File[] allFilesAndDirs = folderDir.listFiles();
                     String fileStr="";
		     for (File f1 : allFilesAndDirs) {
                         fileStr=fileStr+","+f1.getName();
                     }
                  //added by Mohit Gupta for default locale
                   Locale cL=null;
                   cL=(Locale)session.getAttribute("UserLocaleFormat");
                  //ended By Mohit Gupta


      
%>


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
             <h1 align="center" style="color: #4F4F4F;font-family:verdana;font-size:12px;font-weight:bold"><%=TranslaterHelper.getTranslatedInLocale("Upload_Template_Excel_Sheet", cL)%> </h1>
        </table>
        <html:form  action="createtableAction.do?param=uploadExcelFileTemplate" method="post" enctype="multipart/form-data">

            <table width="40%" align="center" class="migrate">
                <tr style="width:100%">
                    <td align="left" colspan="2"><font color="red"><html:errors/></font>
                </tr>
                 <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr style="width:100%">
                    <td align="left" colspan="1" class="migrate"><%=TranslaterHelper.getTranslatedInLocale("Template_Name", cL)%></td>
                    <td align="left" colspan="1" class="migrate"><html:text property="tablename" styleId="tablename" style="background-color:#dcdcdc; color:black;width: 190px;"/></td>
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
                    <td align="left" colspan="1" class="migrate"><%=TranslaterHelper.getTranslatedInLocale("File_Name", cL)%>: <label style="color:Red">(.xls <%=TranslaterHelper.getTranslatedInLocale("files_only", cL)%>)</label></td>
                    <td align="left" colspan="1" class="migrate"><html:file property="filename" styleId="filename" style="background-color:#dcdcdc; color:black;"/></td>
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
                     <td align="right">
                         <html:button property="testconnection" onclick="CheckFileToUpload()"><%=TranslaterHelper.getTranslatedInLocale("Upload_File", cL)%></html:button>
                    </td>
                </tr>
                 <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
            </table>
                  <input type="hidden" value='<%=reportId%>' name="reportId" id="reportId"/>
        </html:form>

  <script type="text/javascript">
            function CheckFileToUpload()
            {

                var fileString ='<%= fileStr%>';
               
                var fileList = fileString.split(",");

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
                       
                        }
                    }
                    document.forms[0].action='createtableAction.do?param=uploadExcelFileTemplate';
                    document.forms[0].submit();
                    parent.$("#importExcelFileDiv123").dialog('close');
                   
                }
                else
                {

                    alert("Please Upload .xls files only.");
                   
                }

            }


        </script>
    </body>



