<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.metadata.Cube"%> 
 <%--
    Document   : CustomSetting
    Created on : 1 Aug, 2012, 2:47:11 PM
    Author     : Mohit
--%>
<!DOCTYPE html>

         <% String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }
         String userId = String.valueOf(session.getAttribute("USERID"));
         String contextPath=request.getContextPath();
         %>
        
         <html>
    <head>
       <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />
<!--        <link href="http://code.google.com/apis/maps/documentation/javascript/examples/standard.css" rel="stylesheet" type="text/css" />-->
         <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/ui.datepicker.js"></script>
        <script type="text/javascript" src="<%=contextPath%>//dragAndDropTable.js"></script>

     <style type="text/css">
         .migrate
            {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 10pt;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                background-color:#b4d9ee;
                border:0px;
            }
      </style>

        
    </head>
    <body>
        <script type="text/javascript">
//            $(document).ready(function(){
//               if ($.browser.msie == true){
////                    $("#addCalenderDiv").dialog({
////                        autoOpen: false,
////                        height: 230,
////                        width: 337,
////                        position: 'justify',
////                        modal: true
////                    });
////                    $('#datepicker').datepicker({
////                    changeMonth: true,
////                    changeYear: true
////                    });
////                    $('#endYear').datepicker({
////                        changeMonth: true,
////                        changeYear: true
////                    });
//               }
//               else {
////                    $("#addCalenderDiv").dialog({
////                        autoOpen: false,
////                        height: 230,
////                        width: 337,
////                        position: 'justify',
////                        modal: true
////                    });
////                    $('#datepicker').datepicker({
////                    changeMonth: true,
////                    changeYear: true
////                    });
////                    $('#endYear').datepicker({
////                        changeMonth: true,
////                        changeYear: true
////                    });
//               }
//           });
//
           function SubmitSetting(){
                var repTitle= document.getElementById("repTitle").value;
                var hideDate="";
                var sytm="";
             var fColor = document.getElementById("fColor").value;
             var dateF = document.getElementById("dateF").value;
               // alert($("#repTitle").val());

              if (document.getElementById("hideDate1").checked)
              {
               hideDate= document.getElementById("hideDate1").value;
               }
               if (document.getElementById("hideDate2").checked)
              {
               hideDate= document.getElementById("hideDate2").value;
               }
               if (document.getElementById("sytm1").checked)
              {
               sytm= document.getElementById("sytm1").value;
               }
               if (document.getElementById("sytm2").checked)
              {
               sytm= document.getElementById("sytm2").value;
               }

                         if(repTitle=='--SELECT--')
                {
                    alert('Please Select the Report Title');
                    //window.location.href='#CustomSettings.jsp';
//                  return false;

                }else if(hideDate==""){
                    alert('Please Select option for Hiding Date');
                     //window.location.href='#CustomSettings.jsp';
//                    return false;
                  
                }else if(fColor=='--SELECT--'){
                    alert('Please Select the Font color');
                    // window.location.href='#CustomSettings.jsp';
//                    return false;
                   
                }else if(dateF=='--SELECT--'){
                    alert('Please Select Your Date Format');
                     //window.location.href='#CustomSettings.jsp';
//                  return false;
                   
                }else if(sytm==""){
                    alert('Please Select option for Time Dimension ');
                     //window.location.href='#CustomSettings.jsp';
//                    return false;

                }
                else{
                    $.ajax({
                    async:false,
                    url:'userLayerAction.do?userParam=SubmitSetting&repTitle='+repTitle+'&hideDate='+hideDate+'&fColor='+fColor+'&dateF='+dateF+'&sytm='+sytm,
                    success:function(data) {
                        if(data==true || data=="true"){
                        alert('Settings Saved Successfully');}
                    else
                        {
                            alert('Settings Not Saved')
                        }
                }
                });

//                    $.post("<%=request.getContextPath()%>/userLayerAction.do?userParam=SubmitSetting&repTitle="+repTitle+"&hideDate="+hideDate+"&fColor="+fColor+"&dateF="+dateF,function(data){
//                                alert('Settings Saved Successfully');}
//
//                 )
                $('#SaveSettings').submit();
            }
             
            }

       </script>
         <table align="center" style=" width:40% ">
             <tr><td>
                <form id="SaveSettings" name=SaveSettings" method="post" action="">
                    <table align ="center" id="secDetTab" class="tablesorter"  border="0" cellpadding="0" cellspacing="0" style=" width: 100%">
                       <tr><td align="left"><label>Report Title: &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                           <td align="left"><select name="reptitle" id="repTitle"><option>--SELECT--</option>
                                                                  <option >Left</option>
                                                                  <option >Right</option>
                                                                  <option >Center</option>
                                            </select></td>
                       </tr>
                       <tr>
                           <td colspan="2">&nbsp;</td>
                       </tr>
                       <tr><td align="left"><label>Hide Report Dates: &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                           <td align="left"><input type="radio" name="hidedate" id="hideDate1" value="Yes"> Yes &nbsp;&nbsp;
                                            <input type="radio" name="hidedate" id="hideDate2" value="No"> No</td>
                       </tr>
                         <tr>
                           <td colspan="2">&nbsp;</td>
                       </tr>
                         <tr><td align="left"><label>Font Color: &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                           <td align="left"><select name="fcolor" id="fColor"><option>--SELECT--</option>
                                                                  <option >Black</option>
                                                                  <option >White</option>
                          </select></td>
                       </tr>
                        <tr>
                           <td colspan="2">&nbsp;</td>
                       </tr>
                         <tr><td align="left"><label>Date Format: &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                           <td align="left"><select name="dateformat" id="dateF"><option>--SELECT--</option>
                                                                  <option >dd/mm/yyyy</option>
                                                                  <option >mm/dd/yyyy</option>
                                                                  <option >yyyy/mm/dd</option>
<!--                                                                  <option >MM/DD/YYYY</option>-->
                                            </select></td>
                       </tr>
                        <tr>
                           <td colspan="2">&nbsp;</td>
                       </tr>
                       <tr><td align="left"><label>Show Year Time Dimension: &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                           <td align="left"><input type="radio" name="sytm" id="sytm1" value="Yes"> Yes &nbsp;&nbsp;
                                            <input type="radio" name="sytm" id="sytm2" value="No"> No</td>
                       </tr>
                       <tr>
                           <td colspan="2">&nbsp;</td>
                       </tr>
                        <tr>
                           <td colspan="2">&nbsp;</td>
                       </tr>
                       <tr><td colspan="2"><center><input type="button" style="width:auto;color:black" value="Submit" id="" onclick="SubmitSetting()"/>
                                  &nbsp;&nbsp;<input type="reset" style="width:auto;color:black" value="Reset" id="" onclick=""/></center>
                       </td></tr>
                   

                   </table>
                </form>
                 </td></tr>
        </table>

 <div id='loadingmetadata' class='loading_image' style="display:none;">
                <img alt=""  id='imgId' src='images/help-loading.gif'  border='0px' style='position:absolute; left: 450px; top: 150px;'/>
</div>

    </body>

</html>
<!--Added by Mohit jain-->