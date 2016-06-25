<%--
    Document   : GlobalParams
    Created on : Nov 24, 2011, 4:30:19 PM
    Author     : anitha.pallothu@progenbusiness.com
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@page import="java.lang.Object"%>

<html:html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <%--<script src="<%=request.getContextPath()%>/javascript/draggable/jquery-1.3.2.js" type="text/javascript"></script>--%>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>
        <%--<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>--%>

        <link href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/jquery.alerts.css" rel="stylesheet" type="text/css">
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/jquery.alerts.js"></script>
        <script type="text/javascript"  src="<%=request.getContextPath()%>/tracker/JS/dateSelection.js"></script>
        <!--        <script language="javascript" type="text/javascript" src="datetimepicker.js"></script>-->
        <!--        <script type="text/javascript" src="jquery-1.4.2.js"></script>
                <script type="text/javascript" src="DatePicker/jquery.datepick.js"></script>
                <script type="text/javascript" src="jquery.ui.datepicker-fr.js"></script>-->
        <title>Global Params</title>
        <style type="text/css">
            #ui-datepicker-div
            {
                z-index: 9999999;
                width: 40em; padding: .2em .2em 0; font-size: 62.5%;
            }
         
.myhead {
    background-color: #B4D9EE;
    border: 0 none;
    color: #3A457C;
    font-family: Verdana,Arial,Helvetica,sans-serif;
    font-size: 8pt;
    font-weight: bold;
    padding-left: 12px;
    width: 50%;
}
        </style>
        <html:base/>
       
        <style type="text/css">
            *{font:11px verdana;}
        </style>
    </head>
    <body>
    <center>
        <div align="center" style=" height: 400px; border-width: 4px; border-style: groove; border-color: skyblue; margin-top: auto; margin-left: 200px; margin-right: 200px; margin-bottom: 200px; margin: 80px 10px 10px 10px; padding-bottom: 80px; padding-left: 150px; padding-top: 50px;"> &nbsp;
            <html:form  action="createtableAction.do?param=saveGlobalParms" styleId="globalForm" enctype="multipart/form-data" method="post" >
                <table width="66%" align="center">                    
                    <tr style="width:100%">
                        <td class="myhead" colspan="1">Report Start Date</td>
                        <td align="left"  colspan="1"><input type="text" class="ui-datepicker-div" name="dateForReport" id="dateForReport" onkeydown="this.blur();" /></td>
                    </tr> 
                </table>
                <table width="66%" align="center">                        
                    <tr style="width:100%">
                        <td class="myhead" colspan="1">Date Format</td>
                        <td align="left"><html:text property="sDateFormat" value="dd-mm-yy" onkeydown="this.blur();"></html:text></td>
                        </tr> 
                    </table>
                    <table width="66%" align="center">
                        <tr>
                            <td class="myhead" colspan="1">Session Expiry</td>
                            <td align="left"><html:text property="session" onkeypress="return numbersonly(event)" styleId="session" onchange="return test()" ></html:text></td>
                        </tr> 
                    </table>
                    <table width="66%" align="center">
                        <tr>
                            <td class="myhead" colspan="1">Default Language</td>
                            <td width="66%" align="left"><select name="Language" id="Language" value="English" >
                                    <option value="English">English</option>
                                    <option value="Hindi">Hindi</option>
                                    <option value="Telugu">Telugu</option>
                                </select></td>
                        </tr> 
                    </table>
                    <table width="66%" align="center">                   
                        <tr style="width:100%">
                            <td class="myhead" colspan="1">Left Logo</td>
                            <td align="left" colspan="1" ><html:file property="leftLogo" styleId="leftLogo" /></td>
                    </tr>
                </table>  
                <table width="66%" align="center">                   
                    <tr style="width:100%">
                        <td class="myhead" colspan="1">Right Logo</td>
                        <td align="left" colspan="1" ><html:file property="rightLogo" styleId="rightLogo" /></td>
                    </tr>
                </table>
                <table width="66%" align="center">
                    <tr style="width:100%">
                        <td class="myhead" colspan="1">Footer Text Enabled</td>
                        <Td align="left" colspan="1"><html:checkbox property="footertext" onclick ="getFooterText()"></html:checkbox></Td>
                        </tr> 
                    </table>
                    <table id="records" width="66%" align="center" style="display: none">                   
                        <tr style="width:100%">
                            <td class="myhead" colspan="1">Number of records in report table</td>
                            <Td align="left" colspan="1"><html:text property="records" onkeypress="return numbersonly(event)"></html:text></Td>
                        </tr> 
                    </table>
                    <table width="66%" align="center">
                        <tr style="width:100%">
                            <td class="myhead" colspan="1">Query Cache Enabled</td>
                            <Td align="left" colspan="1"><html:checkbox property="querycache" onclick="getQueryCache()" ></html:checkbox></Td>
                        </tr> 
                    </table>
                    <table id="querycheck" width="66%" align="center" style="display: none">                    
                        <tr style="width:100%">
                            <td class="myhead" colspan="1">Query Cache</td>
                            <Td align="left" colspan="1"><html:text property="query"></html:text></Td>
                        </tr> 
                    </table>
                    <table width="66%" align="center">                    
                        <tr style="width:100%">
                            <td class="myhead" colspan="1">Data Set Storage</td>
                            <Td align="left" colspan="1"><html:text property="dataset" onclick="getLocation()"></html:text></Td>
                        </tr> 
                    </table>
                    <table width="66%" align="center">
                        <tr style="width:100%">
                            <td class="myhead" colspan="1">Geo Spatial Analysis enabled</td>
                            <Td align="left" colspan="1"><html:checkbox property="geoenable"></html:checkbox></Td>
                        </tr> 
                    </table>
                    <table width="66%" align="center">                    
                        <tr style="width:100%">
                            <td  class="myhead" colspan="1">Debug enabled</td>
                            <td><html:radio property="debug" value="yes" >yes</html:radio> 
                            <html:radio property="debug" value="no" >no</html:radio> </td>
                        </tr> 
                    </table>
                    <table width="66%" align="center">                    
                        <tr style="width:100%">
                            <td  class="myhead" colspan="1">Enable Report cache</td>                       
                            <td><html:radio property="report" value="yes" >yes</html:radio>
                            <html:radio property="report" value="no" >no</html:radio> </td>
                        </tr> 
                    </table>
                    <table width="66%" align="center">                    
                        <tr style="width:100%">
                            <td  class="myhead" colspan="1">Duplicate Segmentation Allow</td>                       
                            <td><html:radio property="duplicateSegmentation" value="yes" >yes</html:radio>
                            <html:radio property="duplicateSegmentation" value="no" >no</html:radio> </td>
                        </tr> 
                    </table>
                    <table width="66%" align="center">                    
                        <tr style="width:100%">
                            <td  colspan="1"></td>
                            <td align="left" colspan="1"></td>
                        </tr> 
                    </table>&nbsp;
                    <table width="66%" align="center">                    
                        <tr style="width:100%">
                            <td  colspan="1"></td>
                            <td align="left" colspan="1"></td>
                        </tr> 
                    </table>&nbsp;
                    <table width="66%" align="right">
                        <tr style="width:100%">
                            <td><html:submit onclick="return Checkfiles()" styleClass="navtitle-hover" >Submit</html:submit></td>
                        </tr>
                    </table>
            </html:form>
        </div>
    </center>
     <script type="text/javascript">
            $(document).ready(function(){
                $("#dateForReport").datepicker({dateFormat: 'dd-mm-yy'});  
                $("#dateForReport").datepicker({
                    changeMonth: true,
                    changeYear: true,
                    numberOfMonths: 1,
                    stepMonths: 1                    
                });                          
                if ($.browser.msie == true){
                    $("#checkDiv").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 200,
                        position: 'justify',
                        modal: true
                    });
                }else{
                    $("#checkDiv").dialog({
                        autoOpen: false,
                        height:200,
                        width: 200,
                        position: 'justify',
                        modal: true
                    });
                }
            });            
            function getFooterText()
            {   
                if(document.getElementById("records").style.display=='none')
                {                        
                    $("#records").show();
                } 
                else
                {
                    $("#records").hide();
                }              
            }           
            function getQueryCache()
            {   
                if(document.getElementById("querycheck").style.display=='none')
                {                        
                    $("#querycheck").show();
                } 
                else
                {
                    $("#querycheck").hide();
                }
            }
            function numbersonly(evt) {
                var charCode = (evt.which) ? evt.which : event.keyCode
                if (charCode != 46 && charCode > 31
                    && (charCode < 48 || charCode > 57)){
                    alert("Numeric values will be accepted")
                    return false;
                }
                return true;
            }
            function test()
            {
                var testingvalue = document.getElementById('session').value
                if(testingvalue<0 || testingvalue>60){
                    alert("Time should be from 0:min to 60:min")
                }
            }
            function getLocation()
            {
                var LeftString = document.getElementById('leftlogo').value
                var RightString = document.getElementById('rightlogo').value 
                $('#dataset').attr('value',LeftString)
                $('#dataset').attr('value',RightString)
            }  
            function Checkfiles()
            {
                var lfup = document.getElementById('leftLogo');
                var rfup = document.getElementById('rightLogo');
                var lfileName = lfup.value;
                var rfileName = rfup.value
                var lext = lfileName.substring(lfileName.lastIndexOf('.') + 1);
                var rext = rfileName.substring(rfileName.lastIndexOf('.') + 1);
                if( (lfileName!=null && lext == "img" || lext == "png" || lext == "jpg" || lext == "jpeg" || lext == "gif") && (rfileName!=null && rext == "img" || rext == "png" || rext == "jpg" || rext == "jpeg" || rext == "gif") )
                {
                    return true;
                }
                else
                {                  
                    jAlert("Upload image files at leftlogo & rightlogo","Upload");
                    // fup.focus();
                    // document.getElementById("checkDiv").style.display ='block'
                   
                    return false;
                }
            }    
            function closeDiv(){
                parent.$(".uploadDialog").dialog('close');
            }
        </script>
</body>
</html:html>
