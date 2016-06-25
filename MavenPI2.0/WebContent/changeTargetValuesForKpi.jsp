
<%--
    Document   : changeTargetValuesForKpi
    Created on : Feb 5, 2010, 8:23:11 PM
    Author     : Saurabh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String contextPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
         <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/ui.core.js"></script>
          <script type="text/javascript" src="<%=contextPath%>/javascript/dashboardDesign.js"></script>

        <link href="<%=contextPath%>/reportDesigner.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/dashBoardViewer.css" type="text/css" rel="stylesheet">
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
            
       
    </head>
    <body>
        <%
        String elementId=request.getParameter("elementId");
        String masterKpiId=request.getParameter("masterKpiId");
        String targetValue=request.getParameter("targetValue");
        if(targetValue.equalsIgnoreCase("null") || targetValue.equalsIgnoreCase("--"))
            targetValue="";
        String reportId=request.getParameter("reportId");
        String timeLevel=request.getParameter("timeLevel");
        String dashletId = request.getParameter("dashletId");
        String kpiType = request.getParameter("typeKpi");
         %>
        <Form name="kpiTargetForm" id="kpiTargetForm">
            <INPUT type="HIDDEN" name="elementId" id="elementId" value="<%=elementId%>">
            <INPUT type="HIDDEN" name="masterKpiId" id="masterKpiId" value="<%=masterKpiId%>">
            <INPUT type="HIDDEN" name="timeLevel" id="timeLevel" value="<%=timeLevel%>">
            <INPUT type="HIDDEN" name="reportId" id="reportId" value="<%=reportId%>">
            <INPUT type="HIDDEN" name="dashletId" id="dashletId" value="<%=dashletId%>">
            <INPUT type="HIDDEN" name="kpiType" id="kpiType" value="<%=kpiType%>">
                <Br/><Br/>
            <Table>
                <Tr>
                    <Td>
                        Enter Target Values
                    </Td>
               
                      
                    <Td><Input type="text" onkeypress="return isNumberKey(event)" name="kpiTargetargetVal" value="<%=targetValue%>"></Td>
                    

                </Tr>
            </Table><Br/><Br/>
            <Table width="100%">
                <Tr width="100%">
                    <Td width="50%" align="right">
                    <Input type="BUTTON" class="navtitle-hover" name="Save" value="Save" onclick="saveTargetVal()"></Td>
                    <Td width="50%" align="left">
                        <input type="Button" class="navtitle-hover" name="Clear" value="Clear" onclick="ClearTargetVal()">   
                    </Td>
                </Tr>
            </Table>
        </Form>
                     <script type="text/javascript">
        function isNumberKey(evt)
       {
          var charCode = (evt.which) ? evt.which : event.keyCode
          if (charCode != 46 && charCode > 31
            && (charCode < 48 || charCode > 57))
             return false;

          return true;
       }
        function saveTargetVal()
        {
            var tVal=document.forms.kpiTargetForm.kpiTargetargetVal.value;
            var masterKpiId=document.forms.kpiTargetForm.masterKpiId.value;
            var elementId=document.forms.kpiTargetForm.elementId.value;
            var timeLevel=document.forms.kpiTargetForm.timeLevel.value;
            var reportId=document.forms.kpiTargetForm.reportId.value;
            var dashletId=document.forms.kpiTargetForm.dashletId.value;
            var kpiType=document.forms.kpiTargetForm.kpiType.value;
            $.ajax({
                 url: 'dashboardTemplateViewerAction.do?templateParam2=saveTargetForKpiTable&masterKpiId='+masterKpiId+'&elementId='+elementId+'&tVal='+tVal+'&timeLevel='+timeLevel+'&reportId='+reportId+'&dashletId='+dashletId+'&kpiType='+kpiType,
                success: function(data) {
                    if(data==1)
                    {
                         parent.$("#kpiTarget").dialog('close');
                         sleep(100);
                         parent.displayKPI(dashletId, reportId, "","",masterKpiId, "","","","false");
                    }
                }
            });
        }
        
        function ClearTargetVal()
        {
            var masterKpiId=document.forms.kpiTargetForm.masterKpiId.value;
            var elementId=document.forms.kpiTargetForm.elementId.value;
            var reportId=document.forms.kpiTargetForm.reportId.value;
            var dashletId=document.forms.kpiTargetForm.dashletId.value;
            var delConfirm=confirm('Target For This will be deleted,want to continue');
            if(delConfirm==true){
            $.ajax({
                 url: 'dashboardTemplateViewerAction.do?templateParam2=clearTargetForKpiTable&masterKpiId='+masterKpiId+'&elementId='+elementId+'&reportId='+reportId+'&dashletId='+dashletId,
                success: function(data) {
                   
                                             parent.$("#kpiTarget").dialog('close');
                         sleep(100);
                         parent.displayKPI(dashletId, reportId, "","",masterKpiId, "","","","false");
                   
                }
            });
            }
        }

            function sleep(milliseconds) {
            var start = new Date().getTime();
            while ((new Date().getTime() - start) < milliseconds){
            // Do nothing
            }
            }


        function saveTargetInDatabase()
        {
              var tVal=document.forms.kpiTargetForm.kpiTargetargetVal.value;
              var masterKpiId=document.forms.kpiTargetForm.masterKpiId.value;
              var elementId=document.forms.kpiTargetForm.elementId.value;
              var timeLevel=document.forms.kpiTargetForm.timeLevel.value;
              var reportId=document.forms.kpiTargetForm.reportId.value;

             $.ajax({
                                url: 'dashboardTemplateViewerAction.do?templateParam2=saveTargetKpiDetails&elementId='+elementId+'&masterId='+masterKpiId+'&REPORTID='+parent.document.getElementById("REPORTID").value,

                                success: function(data1){
                                    sleep(100)
                                    var kpiData=data1.substring(1).split("|");

                                    for(var i=0;i<kpiData.length;i++){
                                        var kpiDisp=kpiData[i].split(",");
                                    parent.displayKPI(kpiDisp[0],kpiDisp[1],kpiDisp[2],kpiDisp[3],kpiDisp[4],kpiDisp[5],kpiDisp[6],kpiDisp[7]);
                                    }

                                }
                            });

        }


        </script>
    </body>
</html>
