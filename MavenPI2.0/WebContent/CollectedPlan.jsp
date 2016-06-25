<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.metadata.Cube"%>
<%--
    Document   : PlanPublish11
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
             $(document).ready(function(){
                $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getAllConnectionForModifyMeasure",function(data){
                          $("#connections").html(data)
                });
            });

            function getPlantCode(){
                var connectionID= $("#connections").val()
                $("#plantCode").html("")
               $("#planScenario").html("")
                 $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getPlantCodeDetails11&connectionID="+connectionID,function(data){
                    $("#plantCode").html(data)
                });

            }
            function getPlanScenario(){
                var connectionID= $("#connections").val()
                var plantCode= $("#plantCode").val()
                $("#planScenario").html("")
                $("#planRunDate").html("")
                $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getPlanScenarioDetails11&connectionID="+connectionID+"&plantCode="+plantCode,function(data){
                    $("#planScenario").html(data)
                });

            }
  function getPlanRunDate(){
                 var connectionID= $("#connections").val()
                 var plantCode= $("#plantCode").val()
                 var planScenario= $("#planScenario").val()
                 $("#planRunDate").html("")
                $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getPlantRunDateDetails&connectionID="+connectionID+"&plantCode="+plantCode+"&planScenario="+planScenario,function(data){
               alert(data);
               $("#planRunDate").html(data)
           });
            }



            function Collected(){
                var connectionID= $("#connections").val()
                var plantCode= $("#plantCode").val()
                var planScenario= $("#planScenario").val()
                var planRunDate= $("#planRunDate").val()
                if(plantCode=='' || planScenario=='' || planRunDate=='' ||connectionID=='--SELECT--')
                      {
                    alert('Select Complete Details')
                      }
                else {

                    var tem = "<%= request.getContextPath()%>/userLayerAction.do?userParam=getcheckIsCollected1111&connectionID="+connectionID+"&plantCode="+plantCode+"&planScenario="+planScenario+"&planRunDate="+planRunDate;
                     //  alert("val of tem=="+tem);
                    $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getcheckIsCollected&connectionID="+connectionID+"&plantCode="+plantCode+"&planScenario="+planScenario+"&planRunDate="+planRunDate,function(data){
                    });
                   }
                }


       </script>
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
                           <td align="left"><select name="planScenario" id="planScenario" onchange="getPlanRunDate()"></select></td>
                       </tr>
                       <tr>
                           <td colspan="2">&nbsp;</td>
                       </tr>

                       <tr><td align="left"><label>Plan Run Date : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                           <td align="left"><select name="planRunDate" id="planRunDate"></select></td>
                       </tr>
                       <tr>
                           <td colspan="2">&nbsp;</td>
                       </tr>
                       <tr><td colspan="2"><center><input type="button" class="migrate" style="width:auto;color:black" value="Collected" id="btnn" onclick="Collected()"/>
                       </center></td></tr>
                   </table>
                </form>
                 </td></tr>
        </table>

 <div id='loadingmetadata' class='loading_image' style="display:none;">
                <img alt=""  id='imgId' src='images/help-loading.gif'  border='0px' style='position:absolute; left: 450px; top: 150px;'/>
</div>

    </body>

</html>