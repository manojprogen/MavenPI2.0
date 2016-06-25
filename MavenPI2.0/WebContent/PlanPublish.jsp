<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.metadata.Cube"%>
<%--
    Document   : PlanPublish
    Created on : Mar 9, 2013, 1:00:01 PM
    Author     : Nazneen Khan
--%>
<!DOCTYPE html>

         <% String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }
         String userId = String.valueOf(session.getAttribute("USERID"));
         String contexPath=request.getContextPath();

         %>
         <html>
    <head>
       <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="<%=contexPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contexPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contexPath%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <script src="<%=contexPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contexPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contexPath%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=contexPath%>/javascript/quicksearch.js"></script>
        <link type="text/css" href="<%=contexPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contexPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contexPath%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <script type="text/javascript" src="<%=contexPath%>//dragAndDropTable.js"></script>

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

         <table align="center" style=" width:40% ">
             <tr><td>
                <form id="planPublish" name="planPublish" method="post" action="">
                    <table align ="center" id="secDetTab" class="tablesorter"  border="0" cellpadding="0" cellspacing="0" style=" width: 100%">
                       <tr><td align="left"><label>Select Connection : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                           <td align="left"><select name="connections" id="connections" onchange="getPlantCode()"></select></td>
                       </tr>
                       <tr>
                           <td colspan="2">&nbsp;</td>
                       </tr>
                       <tr><td align="left"><label>Plant Code : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                           <td align="left"><select name="plantCode" id="plantCode" onchange="getPlanScenario()"></select></td>
                       </tr>
                       <tr>
                           <td colspan="2">&nbsp;</td>
                       </tr>
                        <tr><td align="left"><label>Planning Scenario : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                           <td align="left"><select name="planScenario" id="planScenario" onchange="getPlantPeriod()"></select></td>
                       </tr>
                       <tr>
                           <td colspan="2">&nbsp;</td>
                       </tr>
                       <tr><td align="left"><label>Plant Period : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                           <td align="left"><select name="plantPeriod" id="plantPeriod" onchange="getPlanVersion()"></select></td>
                       </tr>
                       <tr>
                           <td colspan="2">&nbsp;</td>
                       </tr>
                       <tr><td align="left"><label>Plan Version : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                           <td align="left"><select name="planVersion" id="planVersion"></select></td>
                       </tr>
                       <tr>
                           <td colspan="2">&nbsp;</td>
                       </tr>
                       <tr><td colspan="2"><center><input type="button" class="migrate" style="width:auto;color:black" value="Publish Plan" id="btnn" onclick="publishPlan()"/>
                       </center></td></tr>
                   </table>
                </form>
                 </td></tr>
        </table>

 <div id='loadingmetadata' class='loading_image' style="display:none;">
                <img alt=""  id='imgId' src='images/help-loading.gif'  border='0px' style='position:absolute; left: 450px; top: 150px;'/>
</div>
<script type="text/javascript">
             $(document).ready(function(){
                $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getAllConnectionForModifyMeasure",function(data){
                          $("#connections").html(data)
                });
            });

            function getPlantCode(){
                var connectionID= $("#connections").val()
                $("#plantCode").html("")
                $("#plantPeriod").html("")
                $("#plantVersion").html("")
                 $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getPlantCodeDetails&connectionID="+connectionID,function(data){
                    $("#plantCode").html(data)
                });

            }
            function getPlanScenario(){
                var connectionID= $("#connections").val()
                var plantCode= $("#plantCode").val()
                $("#planScenario").html("")
                $("#plantPeriod").html("")
                $("#plantVersion").html("")
                $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getPlanScenarioDetails&connectionID="+connectionID+"&plantCode="+plantCode,function(data){
                    $("#planScenario").html(data)
                });

            }
            function getPlantPeriod(){
                 var connectionID= $("#connections").val()
                 var plantCode= $("#plantCode").val()
                 var planScenario= $("#planScenario").val()
                $("#plantPeriod").html("")
                $("#plantVersion").html("")
                $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getPlantPeriodDetails&connectionID="+connectionID+"&plantCode="+plantCode+"&planScenario="+planScenario,function(data){
                    $("#plantPeriod").html(data)
                });
            }
            function getPlanVersion(){
                 var connectionID= $("#connections").val()
                 var plantCode= $("#plantCode").val()
                 var planScenario= $("#planScenario").val()
                 var plantPeriod = $("#plantPeriod").val()
                $("#planVersion").html("")
                $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getPlanVersionDetails&connectionID="+connectionID+"&plantCode="+plantCode+"&planScenario="+planScenario+"&plantPeriod="+plantPeriod,function(data){
                    $("#planVersion").html(data)
                });
            }
            function publishPlan(){
                var connectionID= $("#connections").val()
                var plantCode= $("#plantCode").val()
                var planScenario= $("#planScenario").val()
                var plantPeriod= $("#plantPeriod").val()
                var planVersion= $("#planVersion").val()

                if(plantCode=='' || plantPeriod=='' || connectionID=='--SELECT--')
                    alert('Select Complete Details')
                else {

                    $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getcheckIsPublished&connectionID="+connectionID+"&plantCode="+plantCode+"&plantPeriod="+plantPeriod+"&planVersion="+planVersion+"&planScenario="+planScenario,function(data){
                        var jsonVar=eval('('+data+')')
                        var isPublished=jsonVar.isPublished[0]

                    if(isPublished=='Y'){

                        var retVal1 = confirm("This Plan already published. Do you want to republish");
                        if (retVal1 == true){
                             $("#loadingmetadata").show();
                             $.post('userLayerAction.do?userParam=publishPlan&connectionID='+connectionID+'&plantCode='+plantCode+'&plantPeriod='+plantPeriod+'&planVersion='+planVersion+"&planScenario="+planScenario,function(data)  {
                                            if(data==1)
                                                    alert('Plan Published Successfully')
                                            else
                                                    alert('Error ! Plan Publish Failed ! ')
                                                $( "#loadingmetadata").hide();
                            });

                        }
                    }
                    else {
                        var retVal = confirm("Are you sure you want publish plan !");
                            if (retVal == true){
                             $("#loadingmetadata").show();
                             $.post('userLayerAction.do?userParam=publishPlan&connectionID='+connectionID+'&plantCode='+plantCode+'&plantPeriod='+plantPeriod+'&planVersion='+planVersion+"&planScenario="+planScenario,function(data)  {
                                            if(data==1)
                                                    alert('Plan Published Successfully')
                                            else
                                                    alert('Error ! Plan Publish Failed ! ')
                                                 $( "#loadingmetadata").hide();
                            });

                        }
                    }
                     });
                }

            }

       </script>
    </body>

</html>