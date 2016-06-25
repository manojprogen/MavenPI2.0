<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.metadata.Cube"%>
<%--
    Document   : PlanCollection
    Created on : Mar 8, 2013, 3:20:01 PM
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

            function collectPlan(){
                var connectionID= $("#connections").val()
                var procName= $("#procName").val()

                if(procName=='')
                    alert('enter proc name')
                else if(connectionID=='--SELECT--')
                    alert('Please Select Connection ! ')
                else {
                    var retVal = confirm("Are you sure you want collect plan data !");
                    if (retVal == true){
                     $("#loadingmetadata").show();
                     $.post('userLayerAction.do?userParam=collectPlanData&connId='+connectionID+'&procName='+procName,function(data)  {
                                    if(data==1)
                                            alert('Plan Collected Successfully')
                                    else if(data==2)
                                            alert('Error ! Plan Collection Failed ! ')
                                    else
                                            alert('No plan data to collect')

                                        $("#loadingmetadata").hide();
                    });
                    
                    }
                }

            }

       </script>
          <table align="center" style=" width:40% ">
             <tr><td>
                <form id="planCollection" name="planCollection" method="post" action="">
                   <table align ="center" id="secDetTab" class="tablesorter"  border="0" cellpadding="0" cellspacing="0">
                       <tr><td align="left"><label>Select Connection : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                            <td align="left"><select name="connections" id="connections"></select></td>
                       </tr>
                       <tr>
                           <td colspan="2">&nbsp;</td>
                       </tr>
                       <tr><td align="left"><label>Procedure Name: &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                           <td align="left"><input type="text" value="drl_load" name="procName" id="procName" disabled="true"> &nbsp;(Eg: load_proc1)</td>
                       </tr>
                       <tr>
                           <td colspan="2">&nbsp;</td>
                       </tr>
                       <tr><td colspan="2"><center><input type="button" class="migrate" style="width:auto;color:black" value="Collect Plan" id="btnn" onclick="collectPlan()"/>
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