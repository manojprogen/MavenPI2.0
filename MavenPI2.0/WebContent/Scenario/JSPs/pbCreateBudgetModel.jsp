<%-- 
    Document   : pbCreateBudgetModel
    Created on : 5 Jun, 2010, 4:21:14 PM
    Author     : mahesh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%
            //

            String scenarioId = "";
            String rowId = "";
            String exparrayStr = "";
            ArrayList exparray = new ArrayList();
            if (request.getParameter("scenarioId") != null) {
                scenarioId = request.getParameter("scenarioId");
            }
            if (request.getParameter("rowId") != null) {
                rowId = request.getParameter("rowId");
            }
            if (request.getParameter("exparray") != null) {
                exparrayStr = request.getParameter("exparray");
            }
            exparrayStr = exparrayStr.replace("[", "").replace("]", "");


            String path = request.getContextPath();
            if (session != null) {
                String NewUrl = (String) session.getAttribute("NewUrl");

            }

            String modelName = "Budgeting";
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>pi 1.0</title>
        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <Script type="javascript"  src="<%=request.getContextPath()%>/Scenario/JS/myScripts.js"></Script>
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />

        <script type="text/javascript">
            
            function checkCreateBudgetModel()
            {      var  secondaryViewBy=parent.document.getElementById("secondaryViewBy");
                  var secondaryViewByText = secondaryViewBy.options[secondaryViewBy.selectedIndex].text;
                var anlyName3 =parent.document.getElementById("primaryViewBy");
                var anlyName3Text = anlyName3.options[anlyName3.selectedIndex].text;
                <%--alert("anlyName3Text =="+anlyName3Text)--%>
                var expGrowth = document.getElementById("expGrowth").value;
                var newrowId = "<%=rowId%>";
                var expVar="<%=exparrayStr%>";
                var expArray=expVar.split(",");
                expArray[newrowId]=expGrowth;
                var Growthapply = document.forms.myBudgetForm.applytoGrowth;
                var GrowthapplyStr = "";
                for(var i=0;i<Growthapply.length;i++) {
                    if(Growthapply[i].checked==true) {
                        if(GrowthapplyStr=="") {
                            GrowthapplyStr = Growthapply[i].value;
                        }else {
                            GrowthapplyStr = GrowthapplyStr+","+Growthapply[i].value;
                        }
                    }
                }

                
                
                document.myBudgetForm.action='<%=path%>'+"/ScenarioViewerAction.do?scenarioParam=saveBudgetModel&scenarioId="+'<%=scenarioId%>'+"&expGrowth="+expGrowth+"&newrowId="+newrowId+"&expArray="+expArray+"&GrowthapplyStr="+GrowthapplyStr+"&modelName="+'<%=modelName%>'+"&analyzeBy="+anlyName3Text+"&secondaryViewByText="+secondaryViewByText;
                <%--alert("/ScenarioViewerAction.do?scenarioParam=saveBudgetModel&scenarioId="+'<%=scenarioId%>'+"&expGrowth="+expGrowth+"&newrowId="+newrowId+"&expArray="+expArray+"&GrowthapplyStr="+GrowthapplyStr+"&modelName="+'<%=modelName%>'+"&analyzeBy="+anlyName3Text);--%>
                document.myBudgetForm.submit();
                parent.$('#budgetModel').dialog('close');
                parent.submitform2();
            
            }

            function disableAll(){
                var chk1=document.getElementById("applytoGrowth");
                var chk2=document.getElementById("applytoboth");
                var chk3=document.getElementById("applytoabove");
                var chk4=document.getElementById("applytonew");
                var Growthapply = document.forms.myBudgetForm.applytoGrowth;
                for(var i=1;i<Growthapply.length;i++) {
                    if(Growthapply[0].checked==true){
                        Growthapply[i].disabled=true;
                        Growthapply[i].checked=false;
                    }else{
                        Growthapply[0].disabled=false;
                        Growthapply[i].disabled=false;
                    }
                }
            }
        </script>
        <style type="text/css">
            *{
                font:11px verdana
            }
        </style>
    </head>
    <body onload="disableAll()">
        <br>
        <center>
            <%--<font> Fields marked <span style="color:red">*</span> are MANDATORY </font>--%>
            <br><br>
            <form name="myBudgetForm" method="post">
                <table id="t1">
                    <%-- <tr>
                         <td><span style="color:red">*</span>Model Name</td>
                         <td>Model Name</td>
                         <td>
                             <input type="text" id="modelName" name="modelName" style="width:150px">
                         </td>
                     </tr>--%>
                    <tr>
                        <td>Expected Growth</td>
                        <td>
                            <input type="text" id="expGrowth" name="expGrowth" style="width:150px">
                        </td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <td><input type="checkbox" id="applytoGrowth" name="applytoGrowth" checked onchange="disableAll()" value="applytoAll"></td>
                        <td>Apply to All</td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" id="applytoGrowth" name="applytoGrowth" value="applynotonegative"></td>
                        <td>Do not Apply where Growth is Negative in any period</td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" id="applytoGrowth" name="applytoGrowth" value="applynotonegativelast"></td>
                        <td>Do not Apply where Growth is Negative in last period</td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" id="applytoGrowth"  name="applytoGrowth" value="applynotoabove"></td>
                        <td>Do not Apply where Growth was > 50%</td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" id="applytoGrowth" name="applytoGrowth" value="applynotonew"></td>
                        <td>Do not Apply for new Products launched lastyear only</td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" id="applytoGrowth"  name="applytoGrowth" value="applytoabove"></td>
                        <td>Apply where Growth was > 50%</td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" id="applytoGrowth" name="applytoGrowth" value="applytonew"></td>
                        <td>Apply for new Products launched lastyear only</td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <td>
                            <input type="button" value="Go" onclick="return checkCreateBudgetModel()">
                        </td>
                    </tr>
                </table>
            </form>
        </center>
    </body>
</html>
