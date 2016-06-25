<%--
    Document   : Report Bug Mail
    Created on : Sep 14, 2015,
    Author     : Dinanath Parit
--%>

<%@page import="java.sql.Connection,utils.db.ProgenConnection,prg.db.PbDb,prg.db.PbReturnObject,java.util.ArrayList,java.io.File,java.util.List,prg.db.Container,java.util.HashMap,java.util.Locale,com.progen.i18n.TranslaterHelper"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%

String reportId=request.getParameter("reportid");
    String qry = "";
                     //added by Mohit Gupta for default locale
                     Locale cL=null;
                     cL=(Locale)session.getAttribute("UserLocaleFormat");
                     //ended By Mohit Gupta
   
                     String folderPath=(String)session.getAttribute("reportAdvHtmlFileProps")+"/importTemplate";
                     File folderDir=new File(folderPath);
                     if(!folderDir.exists()){
                         folderDir.mkdir();
                         }
                     File[] allFilesAndDirs = folderDir.listFiles();
                     String fileStr="";
		     for (File f1 : allFilesAndDirs) {
                         fileStr=fileStr+","+f1.getName();
                     }

%>
  <%
           
         
            String tabName1=request.getParameter("tabName");
            
             String bugReportName1=null;
                     try {
                                Container bugcontainer = null;
                                HashMap bugmap = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
                                if (bugmap != null) {
                                    bugcontainer = (Container) bugmap.get(tabName1);
                                  
                                   bugReportName1=bugcontainer.getReportName();
                   

                                } else {
                                   
                                }
                            } catch (Exception exp) {
                                exp.printStackTrace();
                            }
    %>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title>Upload File</title>

        <html:base/>
       
    </head>
    <body>
         <table  align="center">
             <h1 align="center" style="color:#27408B;font-size: 20px;"><%=TranslaterHelper.getTranslatedInLocale("Customer_Report_Bug_Mail", cL)%></h1>
        </table>
        <html:form  action="createtableAction.do?param=uploadExcelFile1" method="post" enctype="multipart/form-data">
            <input type="hidden"  name="thisReportName" id="thisReportName" value="<%=bugReportName1%>" />
            <table width="40%" align="center" class="migrate">
                <tr style="width:100%">
                    <td align="left" colspan="2"><font color="red"><html:errors/></font>
                </tr>
                 <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
              
                <tr style="width:100%">
                    <td align="left" colspan="1" class="migrate"><%=TranslaterHelper.getTranslatedInLocale("Subject", cL)%> :</td>
                    <td align="left" colspan="1" class="migrate"><input type="text"  name="subject" id="subject" style="width:100%;-webkit-border-radius: 5px;-moz-border-radius: 5px;border-radius: 5px;border-color: #A2B5CD;border-style: solid;border-width: 1px;height: 21px;" /></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                 <tr style="width:100%">
                    <td align="left" colspan="1" class="migrate"><%=TranslaterHelper.getTranslatedInLocale("Send_To", cL)%>:</td>
                    <td align="left" colspan="1" class="migrate"><textarea  class="form-control" id="sendToEmailId" name="sendToEmailId" rows="6" cols="40" placeholder="Ex: amit@progenbusiness.com,tushar.gujar@progenbusiness.com" style="-webkit-border-radius: 5px;-moz-border-radius: 5px;border-radius: 5px;border-color: #A2B5CD;border-style: solid;border-width: 1px;"></textarea></td>
                </tr>
                 <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                 <tr style="width:100%">
                    <td align="left" colspan="1" class="migrate"><%=TranslaterHelper.getTranslatedInLocale("Bug_Description", cL)%>:</td>
                    <td align="left" colspan="1" class="migrate"><textarea  class="form-control" id="bugDescription" name="bugDescription" rows="6" cols="40" style="-webkit-border-radius: 5px;-moz-border-radius: 5px;border-radius: 5px;border-color: #A2B5CD;border-style: solid;border-width: 1px;"></textarea></td>
                </tr>

                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr style="width:100%">
                    <td align="left" colspan="1" class="migrate"> <%=TranslaterHelper.getTranslatedInLocale("Snapshot_File", cL)%>: </td>
                    <td align="left" colspan="1" class="migrate"><html:file property="filename" styleId="filename" style="background-color:#B0C4DE; color:black;"/></td>
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
                     <td >
                         <center>
                         <html:button property="testconnection"  onclick="Checkfiles()"  style="-moz-border-radius-bottomleft:4px;
    -moz-border-radius-bottomright:4px;
    -moz-border-radius-topleft:4px;
    -moz-border-radius-topright:4px;
    background:#79C9EC url(images/ui-bg_glass_75_79c9ec_1x400.png) repeat-x scroll 50% 50%;
    border:1px solid #448DAE;
    color:#000;
    FONT-SIZE: 11px;
    FONT-FAMILY: Verdana;
    VERTICAL-ALIGN: middle;
    HEIGHT: 20px;
    WIDTH: auto;
    cursor:pointer;"><%=TranslaterHelper.getTranslatedInLocale("Send", cL)%></html:button>
                         </center>

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
            function Checkfiles()
            {

                var fileString ='<%= fileStr%>';
                var fileList = fileString.split(",");
                
                var subject = document.getElementById('subject').value;
                if(subject==""){
                  alert("Subject can't be empty");
                  return false;
                }
                var sendToEmailId = document.getElementById('sendToEmailId').value;
                if(sendToEmailId==""){
                  alert("Send To can't be empty");
                  return false;
                }
                var bugDescription = document.getElementById('bugDescription').value;
                if(bugDescription==""){
                  alert("Bug Description can't be empty");
                  return false;
                }

                var fup = document.getElementById('filename');
                var fileName = fup.value;
                var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
                if( fileName!=null && fileName!="")
                {
                    document.forms[0].action='createtableAction.do?param=newCustomerBugReportMail';
                    document.forms[0].submit();
                    parent.$("#CustomerReportBugMail").dialog('close');

                }
                else
                {
                    alert("please select one file only.");
                    return false;
                }

            }
function validateEmail(field) {
    var regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,5}$/;
    return (regex.test(field)) ? true : false;
    }


   function validateMultipleEmailsCommaSeparated(emailcntl, seperator) {
    var value = emailcntl.value;
    if (value != '') {
        var result = value.split(seperator);
        for (var i = 0; i < result.length; i++) {
            if (result[i] != '') {
                if (!validateEmail(result[i])) {
                    emailcntl.focus();
                    alert('Please check, `' + result[i] + '` email addresses not valid!');
                    return false;
                }
            }
        }
    }
    return true;
}

        </script>
        <style type="text/css">
            *{font:11px verdana;}
            .navtitle-hover{

}
        </style>
    </body>



