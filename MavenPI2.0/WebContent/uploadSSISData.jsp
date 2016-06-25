<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.i18n.TranslaterHelper,java.util.Locale,prg.db.PbReturnObject,java.util.ArrayList,prg.db.PbDb,utils.db.ProgenConnection,java.sql.Connection"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%--
    Document   : SSIS upload Data
    Created on : Oct 6 2012
    Author     : Nazneen
--%>
<%
    String qry = "";
    qry = "select * from PRG_USER_CONNECTIONS";
    PbDb pbdb = new PbDb();
    PbReturnObject list = null;
    list = pbdb.execSelectSQL(qry);
    int vals = 0;
    vals = list.getRowCount();

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
            .migrate{
                font-family: inherit;
                font-size: 10pt;
                color: #000;
                padding-left:12px;
                background-color:#8BC34A;
                border:0px;
            }
        </style>
    </head>
    <body>
        <script type="text/javascript">
     
            function Checkfiles()
            {
                var fup = document.getElementById('filename');
                var fileName = fup.value;
                var ext = fileName.substring(fileName.lastIndexOf('.') + 1);

                if( fileName!=null && ext == "xls")
                {
                    return true;
                }
                else
                {                 
                    alert("Upload .xls files only","Uplode");
                   
                    return false;
                }           
            }
           
          
        </script>
        <table border="1" align="center">
            <h3 align="center"><%=TranslaterHelper.getTranslatedInLocale("upload_ssis_data", cle)%> </h3>
        </table>  
        <div align="center" style=" width: 100%"> 
        <html:form  action="createtableAction.do?param=uploadSSISFile" method="post" enctype="multipart/form-data">
            <table  align="center" cellpadding="1" cellspacing="1" class="migrate">
             <tr><td> <br><br>
            <table align="center">
                <tr style="width:100%">
                    <td align="left" colspan="2" class="migrate"><font color="red"><html:errors/></font>
                </tr>
                <tr style="width:100%">
                    <td align="left" colspan="1" class="migrate"><%=TranslaterHelper.getTranslatedInLocale("table_name", cle)%> : </td>
                    <td align="left" colspan="1" class="migrate"><html:text property="tablename" styleId="tablename" value="MeriTrac_Excel_STG" style="background-color:lightgoldenrodyellow; color:black;"/></td>
                </tr>
                
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td align="left" colspan="1" class="migrate"><%=TranslaterHelper.getTranslatedInLocale("Connection_Name", cle)%> : </td>
                    <td align="left" colspan="1" class="migrate">
                        <select name="connid" id="connid" >
                            <% for (int i = 0; i < list.getRowCount(); i++) {
                            %>
                            <option value="<%=list.getFieldValueInt(i, 0)%>"><%=list.getFieldValueString(i, 1)%></option> 
                            <%}%>
                        </select>
                    </td>                   
                </tr> 
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td align="left" colspan="1" class="migrate"><%=TranslaterHelper.getTranslatedInLocale("Date_Format", cle)%> : </td>
                    <td align="left" colspan="1" class="migrate">
                        <select name="dateFormat" id="dateFormat" >
                            <option value="dd-MMM-yy">dd-MMM-yy (21-Jan-89)</option>
                            <option value="MMM-dd-yy">MMM-dd-yy (Jan-21-89)</option>
                            <option value="MMM-yy-dd">MMM-yy-dd (Jan-89-21)</option>                            
                            <option value="dd-yy-MMM">dd-yy-MMM (21-89-Jan)</option>
                            <option value="yy-MMM-dd">yy-MMM-dd (89-Jan-21)</option>
                            <option value="yy-dd-MMM">yy-dd-MMM (89-21-Jan)</option>
                            <option value="MMM/dd/yy">MMM/dd/yy (Jan/21/89)</option>
                            <option value="MMM/yy/dd">MMM/yy/dd (Jan/89/21)</option>
                            <option value="dd/MMM/yy">dd/MMM/yy (21/Jan/89)</option>
                            <option value="dd/yy/MMM">dd/yy/MMM (21/89/Jan)</option>
                            <option value="yy/MMM/dd">yy/MMM/dd (89/Jan/21)</option>
                            <option value="yy/dd/MMM">yy/dd/MMM (89/21/Jan)</option>
                            
                            
                            <option value="MM/dd/yy">MM/dd/yy (01/21/89)</option>
                            <option value="MM/yy/dd">MM/yy/dd (01/89/21)</option>
                            <option value="dd/MM/yy">dd/MM/yy (21/01/89)</option>
                            <option value="dd/yy/MM">dd/yy/MM (21/89/01)</option>
                            <option value="yy/MM/dd">yy/MM/dd (89/01/21)</option>
                            <option value="yy/dd/MM">yy/dd/MM (89/21/01)</option>
                            <option value="MM-dd-yy">MM-dd-yy (01-21-89)</option>
                            <option value="MM-yy-dd">MM-yy-dd (01-89-21)</option>
                            <option value="dd-MM-yy">dd-MM-yy (21-01-89)</option>
                            <option value="dd-yy-MM">dd-yy-MM (21-89-01)</option>
                            <option value="yy-MM-dd">yy-MM-dd (89-01-21)</option>
                            <option value="yy-dd-MM">yy-dd-MM (89-21-01)</option>                            
                            
                            
                             <option value="MM/dd/yyyy">MM/dd/yyyy (01/21/1989)</option>
                            <option value="MM/yyyy/dd">MM/yyyy/dd (01/1989/21)</option>
                            <option value="dd/MM/yyyy">dd/MM/yyyy (21/01/1989)</option>
                            <option value="dd/yyyy/MM">dd/yyyy/MM (21/1989/01)</option>
                            <option value="yyyy/MM/dd">yyyy/MM/dd (1989/01/21)</option>
                            <option value="yyyy/dd/MM">yyyy/dd/MM (1989/21/01)</option>
                            <option value="MM-dd-yyyy">MM-dd-yyyy (01-21-1989)</option>
                            <option value="MM-yyyy-dd">MM-yyyy-dd (01-1989-21)</option>
                            <option value="dd-MM-yyyy">dd-MM-yyyy (21-01-1989)</option>
                            <option value="dd-yyyy-MM">dd-yyyy-MM (21-1989-01)</option>
                            <option value="yyyy-MM-dd">yyyy-MM-dd (1989-01-21)</option>
                            <option value="yyyy-dd-MM">yyyy-dd-MM (1989-21-01)</option>
                            
                            <option value="MMM/dd/yyyy">MMM/dd/yyyy (Jan/21/1989)</option>
                            <option value="MMM/yyyy/dd">MMM/yyyy/dd (Jan/1989/21)</option>
                            <option value="dd/MMM/yyyy">dd/MMM/yyyy (21/Jan/1989)</option>
                            <option value="dd/yyyy/MMM">dd/yyyy/MMM (21/1989/Jan)</option>
                            <option value="yyyy/MMM/dd">yyyy/MMM/dd (1989/Jan/21)</option>
                            <option value="yyyy/dd/MMM">yyyy/dd/MMM (1989/21/Jan)</option>
                            <option value="MMM-dd-yyyy">MMM-dd-yyyy (Jan-21-1989)</option>
                            <option value="MMM-yyyy-dd">MMM-yyyy-dd (Jan-1989-21)</option>
                            <option value="dd-MMM-yyyy">dd-MMM-yyyy (21-Jan-1989)</option>
                            <option value="dd-yyyy-MMM">dd-yyyy-MMM (21-1989-Jan)</option>
                            <option value="yyyy-MMM-dd">yyyy-MMM-dd (1989-Jan-21)</option>
                            <option value="yyyy-dd-MMM">yyyy-dd-MMM (1989-21-Jan)</option>
                           
                        </select>
                    </td>                   
                </tr> 
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr style="width:100%">
                    <td align="left" colspan="1" class="migrate"><%=TranslaterHelper.getTranslatedInLocale("File_Name", cle)%> : <label style="color:Red">(.xls files only)</label> </td>
                    <td align="left" colspan="1" class="migrate"><html:file property="filename" styleId="filename" style="background-color:lightgoldenrodyellow; color:black;"/></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr style="width:100%">
                    <td align="center" colspan="2" class="migrate"><html:submit onclick ="return Checkfiles()" styleClass="navtitle-hover" ><%=TranslaterHelper.getTranslatedInLocale("Upload_File", cle)%></html:submit>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
            </table></td></tr>
            </table>
        </html:form>
        </div>
    </body>
</html:html>
