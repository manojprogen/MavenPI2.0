
<%@page import="java.util.ArrayList"%>
<%@page import="prg.scenario.param.PbScenarioParamVals"%>
<%@page import="prg.db.Session"%>
<%@page import="prg.scenario.client.PbScenarioManager"%>
<%@page import="com.progen.scenarion.PbScenarioCollection"%>
<%@page import="prg.db.PbReturnObject"%>
<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%

            //response.setHeader("Cache-Control", "no-store");
            //response.setHeader("Pragma", "no-cache");
            //response.setDateHeader("Expires", 0);
          
            String path = request.getContextPath();
            String loguserId = String.valueOf(session.getAttribute("USERID"));
            String scenarioName = "";
            String scenarioId = "";
            String scenarioParamSectionDisplay = "";
            String scenarioTableScetionDisplay = "";
            String GraphDisplay = "";
            ArrayList timeDetailsArray = new ArrayList();

            try {
                if (request.getAttribute("scenarioId") != null) {
                    scenarioId = String.valueOf(request.getAttribute("scenarioId"));
                }
                if (request.getAttribute("scenarioName") != null) {
                    scenarioName = String.valueOf(request.getAttribute("scenarioName"));
                }
                if (request.getAttribute("scenarioParamSectionDisplay") != null) {
                    scenarioParamSectionDisplay = String.valueOf(request.getAttribute("scenarioParamSectionDisplay"));
                }
                if (request.getAttribute("scenarioTableScetionDisplay") != null) {
                    scenarioTableScetionDisplay = String.valueOf(request.getAttribute("scenarioTableScetionDisplay"));
                }
                if (session.getAttribute("graphdisplay") != null) {
                    GraphDisplay = String.valueOf(session.getAttribute("graphdisplay"));
                }


                if (request.getAttribute("timeDetailsArray") != null) {
                    timeDetailsArray = (ArrayList) (request.getAttribute("timeDetailsArray"));
                }

                ArrayList histMonths = new ArrayList();
                if (request.getAttribute("histMonths") != null) {
                    histMonths = (ArrayList) request.getAttribute("histMonths");
                }
                String modelId = "";
                if (request.getAttribute("modelId") != null) {
                    modelId = (String) request.getAttribute("modelId");
                }
                String viewByName = "";
                if (request.getAttribute("viewByName") != null) {
                    viewByName = (String) request.getAttribute("viewByName");
                }
                String dimensionId = "";
                if (request.getAttribute("dimensionId") != null) {
                    dimensionId = (String) request.getAttribute("dimensionId");
                }
                String NONALLCOMBO = "";
                if (request.getAttribute("NONALLCOMBO") != null) {
                    NONALLCOMBO = (String) request.getAttribute("NONALLCOMBO");
                }
                String NewUrl = "";
                String completeURL = "";
                if (request.getAttribute("completeURL") != null) {
                    completeURL = (String) request.getAttribute("completeURL");
                }

                session.setAttribute("viewByName", viewByName);
                session.setAttribute("modelId", modelId);
                session.setAttribute("modelName", modelId);
                session.setAttribute("dimensionId", dimensionId);
                session.setAttribute("NONALLCOMBO", NONALLCOMBO);
                session.setAttribute("scenarioId", scenarioId);
                session.setAttribute("NewUrl", NewUrl);
                session.setAttribute("completeURL", completeURL);


                PbScenarioParamVals scenarioParams = new PbScenarioParamVals();
                PbScenarioManager scenarioClient = new PbScenarioManager();
                Session scenarioSession = new Session();
                scenarioParams.setScenarioId(scenarioId);
                scenarioSession.setObject(scenarioParams);
                PbReturnObject pbro = scenarioClient.getAllSavedSeededModels(scenarioSession);
                String savedSeededModel = "";
                for (int i = 0; i < pbro.getRowCount(); i++) {
                    if (savedSeededModel.equalsIgnoreCase("")) {
                        savedSeededModel = pbro.getFieldValueString(i, "SCN_MODEL_NAME");
                    } else {
                        savedSeededModel = savedSeededModel + "," + pbro.getFieldValueString(i, "SCN_MODEL_NAME");
                    }
                }

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>pi 1.0</title>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.datepicker.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/external/bgiframe/jquery.bgiframe.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/Scenario/JS/myScripts.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/Scenario/JS/hashMapScript.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>

        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.datepicker.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/demos.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/jq.css" type="text/css" media="print, projection, screen">
        <link type="text/css" href="<%=request.getContextPath()%>/css/css.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" />
        <link href="<%=request.getContextPath()%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/stylesheets/css.css" type="text/css" rel="stylesheet">
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" type="text/css">
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/style.css" type="text/css" media="print, projection, screen">

        <script type="text/javascript">
            var budgedispName="";
            <%--function changeBudgetExpGrowth(obj,model,exparray){
               var rowid=obj.id.replace("expGrowth","");
               initDialog();
               $('#budgetModel').dialog('open');
               var frameObj = document.getElementById("budgetModelFrame");
               frameObj.src =  '<%=path%>'+"/Scenario/JSPs/pbCreateBudgetModel.jsp?scenarioId="+scenarioId+"&rowId="+rowid+"&exparray="+exparray.toString();
               return true;
           }--%>

               function changeBudgetExpGrowth(obj,model,exparray){
                   var path=document.getElementById('path').value;
                   var rowid=obj.id.replace("expGrowth","");
                   var objValue=obj.value;
                   var tdval=document.getElementById("tdId2_"+rowid).innerHTML;
                   tdval=tdval.toString().replace(",", "", "gi");
                   var expGrowthTotal=document.getElementById("expGrowthTotal").innerHTML;
                   var forecastTotal=document.getElementById("forecastTotal").innerHTML;
                   <%--alert('forecasttotal is :'+forecastTotal)--%>
                   var table=document.getElementById("tablesorter");
                   var rowCount=table.rows.length
                   rowCount=rowCount-2;
                   var sum=0;
                   var sum1=0;
                   for(var i=0;i<rowCount;i++){
                       sum=sum +parseInt(document.getElementById("expGrowth"+i).value);
                   }
                   var avgExpGrowth=eval(sum/rowCount);
                   avgExpGrowth= Math.round(avgExpGrowth*Math.pow(10,2))/Math.pow(10,2);
                   <%--alert('avgExpGrowth is : '+avgExpGrowth)--%>

                   $.ajax({
                       url: path+'/ScenarioViewerAction.do?scenarioParam=calcuForMethod&objVal='+objValue+'&tdval='+tdval,
                 success: function(data){
                     if(data!=""){
                               document.getElementById("finalYear_"+rowid).innerHTML="";
                               document.getElementById("finalYear_"+rowid).innerHTML=data;
                               document.getElementById("finalGrowth_"+rowid).value=data;
                               document.getElementById("finalexpGrowth"+rowid).value=objValue;
                               document.getElementById("expGrowthTotal").innerHTML=avgExpGrowth;
                               for(var j=0;j<rowCount;j++){
                                   <%--alert('document.getElementById("finalYear_"+j).innerHTML is :'+document.getElementById("finalYear_"+j).innerHTML)--%>
                                   sum1=parseInt(sum1) +parseInt(document.getElementById("finalYear_"+j).innerHTML.toString().replace(",", "", "gi"));
                                   <%--alert('sum1 is : '+sum1)--%>
                     }
                               document.getElementById("forecastTotal").innerHTML=sum1;
                 }
                 }
                   });
               }

                 function showViewerGraph(){
                     <%--alert('in showviewergraph')--%>
                     var path=document.getElementById('path').value;
                     var scenarioName=document.getElementById("scenarioName").value;
                     var scenarioId=document.getElementById("scenarioId").value;
                     $.ajax({
                         url: path+'/ScenarioViewerAction.do?scenarioParam=addGraph&scenarioName='+scenarioName+'&scenarioId='+scenarioId,
                         success: function(data){
                             if(data!=""){
                                 alert('Model Saved Successfully');
                             }
                         }
                     });

                 }
                 function gohome(){
                     document.forms.myForm2.action="baseAction.do?param=goHome";
                     document.forms.myForm2.submit();
                 }
                 function logout(){
                     document.forms.myForm2.action="baseAction.do?param=logoutApplication";
                     document.forms.myForm2.submit();
                 }
                 function goToScenarioHome(){
                     var path='<%=request.getContextPath()%>';
                     document.forms.myForm2.action = path+"/AdminTab.jsp#Scenarios";
                     document.forms.myForm2.submit();
                 }

                 function saveScenario(){
                     var selectedModelName = document.getElementById("secondaryViewBy").value;
                   
                     if(selectedModelName=="0") {
                         selectedModelName = "--select--";
                     }else if(selectedModelName=="1") {
                         selectedModelName = "Last Two Months Average";
                     }else if(selectedModelName=="2") {
                         selectedModelName = "Last Three Months Average";
                     }else if(selectedModelName=="3") {
                         selectedModelName = "Last Six Months Average";
                     }else if(selectedModelName=="4") {
                         selectedModelName = "Last Nine Months Average";
                     }else if(selectedModelName=="5") {
                         selectedModelName = "Last Twelve Months Average";
                     }else if(selectedModelName=="6") {
                         selectedModelName = "Last Two Months Average Growth";
                     }else if(selectedModelName=="7") {
                         selectedModelName = "Last Three Months Average Growth";
                     }else if(selectedModelName=="8") {
                         selectedModelName = "Last Four Months Average Growth";
                     }
                     else if(selectedModelName=="9") {
                         selectedModelName = "Last Two Years Average";
                     }else if(selectedModelName=="10") {
                         selectedModelName = "Last Three Years Average";
                     }else if(selectedModelName=="11") {
                         selectedModelName = "Last Six Years Average";
                     }else if(selectedModelName=="12") {
                         selectedModelName = "Last Nine Years Average";
                     }else if(selectedModelName=="13") {
                         selectedModelName = "Last Twelve Years Average";
                     }else if(selectedModelName=="14") {
                         selectedModelName = "Last Two Years Average Growth";
                     }else if(selectedModelName=="15") {
                         selectedModelName = "Last Three Years Average Growth";
                     }else if(selectedModelName=="16") {
                         selectedModelName = "Last Four Years Average Growth";
                     }else if(selectedModelName=="17") {
                         selectedModelName = "Budgeting";
                     }
                     if(selectedModelName=='Budgeting'){
                         initDialog()
                         $('#budgetModelName').dialog('open');
                     }else{
                         saveScenarioIndb('<%=savedSeededModel%>')
                     }
                   
                 
                 }
                 function saveForNowFun(){
                     var path=document.getElementById('path').value;
                     var scenarioName=document.getElementById("scenarioName").value;
                     var selectedModelName = document.getElementById("secondaryViewBy");
                     var selText = selectedModelName.options[selectedModelName.selectedIndex].text;
                     var bpVarStr=document.getElementById("CBOARP76605").value
                     var anlyName3 = document.getElementById("primaryViewBy");
                     var anlyName3Text = anlyName3.options[anlyName3.selectedIndex].text;
                     <%--alert("anlyName3Text =="+anlyName3Text)--%>
                               
                     var funName="saveForNowFun"
                     budgedispName="";
                     if(selText=="Budgeting"){
                         var  bpNames = new Array()
                         var  expectedGrowth=new Array()
                         var forecastFinalGrowth=new Array()

                         var tableObj=document.getElementById("tablesorter");
                         var tbodyObj=tableObj.getElementsByTagName("tbody");
                         var trObj=tbodyObj[0].getElementsByTagName("tr")
                         var tdObj="";
                         for(var trCount=0;trCount<trObj.length-1;trCount++){
                             tdObj=trObj[trCount].getElementsByTagName("td");
                             bpNames[trCount] =tdObj[0].innerHTML;
                             expectedGrowth[trCount]=document.getElementById("expGrowth"+trCount).value
                             forecastFinalGrowth[trCount]=document.getElementById("finalGrowth_"+trCount).value

                         }
                         $.ajax({
                             url: path+'/ScenarioViewerAction.do?scenarioParam=addScenarioModelMaster&scenarioName='+scenarioName+'&bpNames='+bpNames.toString()+'&expectedGrowth='+expectedGrowth.toString()+'&forecastFinalGrowth='+forecastFinalGrowth+'&budgedispName='+budgedispName+"&funName="+funName+"&bpVarStr="+bpVarStr+"&anlyName="+anlyName3Text,
                             success: function(data){

                             }
                         });

                     }




                 }
                 function savebudgemodelName(savedSeededModels){
                     if(document.getElementById("budgeModelName").value !=""){
                         budgedispName= document.getElementById("budgeModelName").value
                         $('#budgetModelName').dialog('close');
                     }else{
                         alert("please Enter Budget Name..")
                     }

                     saveScenarioIndb(savedSeededModels)
                 }
                 function saveScenarioIndb(savedSeededModels){

                     var bpNames =null;
                     var expectedGrowth=null;
                     var forecastFinalGrowth=null;
                     var path=document.getElementById('path').value;
                     var scenarioName=document.getElementById("scenarioName").value;
                     var bpVarStr=document.getElementById("CBOARP76605").value
                     var selectedModelName = document.getElementById("secondaryViewBy").value;
                     if(selectedModelName=="0") {
                         selectedModelName = "--select--";
                     }else if(selectedModelName=="1") {
                         selectedModelName = "Last Two Months Average";
                     }else if(selectedModelName=="2") {
                         selectedModelName = "Last Three Months Average";
                     }else if(selectedModelName=="3") {
                         selectedModelName = "Last Six Months Average";
                     }else if(selectedModelName=="4") {
                         selectedModelName = "Last Nine Months Average";
                     }else if(selectedModelName=="5") {
                         selectedModelName = "Last Twelve Months Average";
                     }else if(selectedModelName=="6") {
                         selectedModelName = "Last Two Months Average Growth";
                     }else if(selectedModelName=="7") {
                         selectedModelName = "Last Three Months Average Growth";
                     }else if(selectedModelName=="8") {
                         selectedModelName = "Last Four Months Average Growth";
                     }
                     else if(selectedModelName=="9") {
                         selectedModelName = "Last Two Years Average";
                     }else if(selectedModelName=="10") {
                         selectedModelName = "Last Three Years Average";
                     }else if(selectedModelName=="11") {
                         selectedModelName = "Last Six Years Average";
                     }else if(selectedModelName=="12") {
                         selectedModelName = "Last Nine Years Average";
                     }else if(selectedModelName=="13") {
                         selectedModelName = "Last Twelve Years Average";
                     }else if(selectedModelName=="14") {
                         selectedModelName = "Last Two Years Average Growth";
                     }else if(selectedModelName=="15") {
                         selectedModelName = "Last Three Years Average Growth";
                     }else if(selectedModelName=="16") {
                         selectedModelName = "Last Four Years Average Growth";
                     }else if(selectedModelName=="17") {
                         selectedModelName = "Budgeting";
                     }
                   
                     var seededModelArray = savedSeededModels.split(",");
                     var flag = true;
                     if(selectedModelName=="--select--") {
                         alert("Please select Model");
                         return false;
                     }
                     if(selectedModelName!="Budgeting"){
                         for(var i=0;i<seededModelArray.length;i++) {
                             if(selectedModelName==seededModelArray[i]) {
                                 flag = false;
                             }
                         }
                     }else{
                         flag = true;
                     }

                     bpNames = new Array()
                     expectedGrowth=new Array()
                     forecastFinalGrowth=new Array()
                     if( selectedModelName == "Budgeting"){
                         var tableObj=document.getElementById("tablesorter");
                         var tbodyObj=tableObj.getElementsByTagName("tbody");
                         var trObj=tbodyObj[0].getElementsByTagName("tr")
                         var tdObj="";
                         for(var trCount=0;trCount<trObj.length-1;trCount++){
                             tdObj=trObj[trCount].getElementsByTagName("td");
                             bpNames[trCount] =tdObj[0].innerHTML;
                             expectedGrowth[trCount]=document.getElementById("expGrowth"+trCount).value
                             forecastFinalGrowth[trCount]=document.getElementById("finalGrowth_"+trCount).value

                         }
                     }

            <%--alert("bpNames"+bpNames.toString())--%>
                          


                    if(flag==true) {
                        $.ajax({
                            url: path+'/ScenarioViewerAction.do?scenarioParam=addScenarioModelMaster&scenarioName='+scenarioName+'&bpNames='+bpNames.toString()+'&expectedGrowth='+expectedGrowth.toString()+'&forecastFinalGrowth='+forecastFinalGrowth+'&budgedispName='+budgedispName+'&bpVarStr='+bpVarStr,
                            success: function(data){
                                if(data!=""){
                                    alert('Model Saved Succecssfully');
                                }
                            }
                        });
                        return true;
                    }else {
                        alert("The selected model is already saved on this scenario.");
                        return false;
                    }

                }

                function submitDrill(drillurl)
                {
                   <%-- var anlyName3 = document.getElementById("primaryViewBy");
                    var anlyName3Text = anlyName3.options[anlyName3.selectedIndex].text;--%>
                    var scenarioName=document.getElementById("scenarioName").value;
                    var scenarioId=document.getElementById("scenarioId").value;
                    document.forms.frmParameter.action = "ScenarioViewerAction.do?scenarioParam=analyzeScenario&flag=analyzeScenario&scenarioId="+scenarioId+"&scenarioName="+scenarioName+drillurl;
                    document.forms.frmParameter.submit();
                }
                function numberOfMonthsBetweenTwoMonths(fromMonth,toMonth) {
                    var number;
                    var myArray = new Array(12);
                    myArray['JAN'] = "1";
                    myArray['FEB'] = "2";
                    myArray['MAR'] = "3";
                    myArray['APR'] = "4";
                    myArray['MAY'] = "5";
                    myArray['JUN'] = "6";
                    myArray['JUL'] = "7";
                    myArray['AUG'] = "8";
                    myArray['SEP'] = "9";
                    myArray['OCT'] = "10";
                    myArray['NOV'] = "11";
                    myArray['DEC'] = "12";
                    // alert(fromMonth)
                    //  alert(toMonth)

                    var fMonth = fromMonth.substr(0,3);
                    var fYear = fromMonth.substr(4,fromMonth.length)
                    var tMonth = toMonth.substr(0,3);
                    var tYear = toMonth.substr(4,toMonth.length)
               
                    if(fYear==tYear) {
                        number = myArray[tMonth];
                    }
                    return number;
                }
                function createCustomModel() {
                    var model = document.getElementById("secondaryViewBy").value;
                    var historicalStartMonth = document.getElementById("historicalStartYear").value;
                    var historicalEndMonth = document.getElementById("historicalEndYear").value;
                    var scenarioId = document.getElementById("scenarioId").value;
                    var modelId = document.getElementById("modelId").value;
                    var tempName = "";
                    var selectedMonth;
           
                    var fromMonth = document.getElementById("CBO_AS_OF_YEAR").value;
                    var toMonth = document.getElementById("CBO_AS_OF_YEAR1").value;
                    var number;

                        
                    if(model=="-1") {
                        initDialog();
                        $('#customModel').dialog('open');
                        var frameObj = document.getElementById("customModelFrame");
                        frameObj.src =  '<%=path%>'+"/Scenario/JSPs/pbCreateCustomModel.jsp?historicalStartMonth="+historicalStartMonth+"&historicalEndMonth="+historicalEndMonth+"&scenarioId="+scenarioId;
                        return true;
                    }else {
                        if(model=="1") {
                            tempName = "Last Two Years Average";
                            selectedMonth = 2;
                            //number = numberOfMonthsBetweenTwoMonths(fromMonth,toMonth);
                            number=toMonth*1-fromMonth*1;
                            if(selectedMonth > number) {
                                alert("Less months are available for the selected model. Please select more months.");
                                document.forms.frmParameter.Submit.disabled=true;
                                return false;
                            }else {
                                document.forms.frmParameter.Submit.disabled=false;
                                return true;;
                            }
                        }else if(model=="2") {
                            tempName = "Last Three Years Average";
                            selectedMonth = 3;
            <%--number = numberOfMonthsBetweenTwoMonths(fromMonth,toMonth);--%>
                            number=toMonth*1-fromMonth*1;
                            if(selectedMonth > number) {
                                alert("Less months are available for the selected model. Please select more months.");
                                document.forms.frmParameter.Submit.disabled=true;
                                return false;
                            }else {
                                document.forms.frmParameter.Submit.disabled=false;
                                return true;;
                            }
                        }else if(model=="3") {
                            tempName = "Last Six Years Average";
                            selectedMonth = 6;
            <%--number = numberOfMonthsBetweenTwoMonths(fromMonth,toMonth);--%>
                            number=toMonth*1-fromMonth*1;
                            if(selectedMonth > number) {
                                alert("Less months are available for the selected model. Please select more months.");
                                document.forms.frmParameter.Submit.disabled=true;
                                return false;
                            }else {
                                document.forms.frmParameter.Submit.disabled=false;
                                return true;;
                            }
                        }else if(model=="4") {
                            tempName = "Last Nine Years Average";
                            selectedMonth = 9;
            <%--number = numberOfMonthsBetweenTwoMonths(fromMonth,toMonth);--%>
                            number=toMonth*1-fromMonth*1;
                            if(selectedMonth > number) {
                                alert("Less months are available for the selected model. Please select more months.");
                                document.forms.frmParameter.Submit.disabled=true;
                                return false;
                            }else {
                                document.forms.frmParameter.Submit.disabled=false;
                                return true;;
                            }
                        }else if(model=="5") {
                            tempName = "Last Twelve Years Average";
                            selectedMonth = 12;
            <%--number = numberOfMonthsBetweenTwoMonths(fromMonth,toMonth);--%>
                            number=toMonth*1-fromMonth*1;
                            if(selectedMonth > number) {
                                alert("Less months are available for the selected model. Please select more months.");
                                document.forms.frmParameter.Submit.disabled=true;
                                return false;
                            }else {
                                document.forms.frmParameter.Submit.disabled=false;
                                return true;;
                            }
                        }else if(model=="6") {
                            tempName = "Last Two Years Average Growth";
                            return false;
                        }else if(model=="7") {
                            tempName = "Last Three Years Average Growth";
                            return false;
                        }else if(model=="17") {
                            initDialog();
                            tempName = "Budgeting";
                            $('#budgetModel').dialog('open');
                            var frameObj = document.getElementById("budgetModelFrame");
                            <%--alert(scenarioId);--%>
                            frameObj.src =  '<%=path%>'+"/Scenario/JSPs/pbCreateBudgetModel.jsp?scenarioId="+scenarioId+"&modelName="+tempName;
                            return true;
                        }
                        return true;
                    }
                    
                }
                function initDialog(){
                    if ($.browser.msie == true){
                        $("#customModel").dialog({
                            autoOpen: false,
                            height: 250,
                            width: 400,
                            position: 'justify',
                            modal: true
                        });
                        $("#budgetModel").dialog({
                            autoOpen: false,
                            height: 250,
                            width: 400,
                            position: 'justify',
                            modal: true
                        });
                        $("#budgetModelName").dialog({
                            autoOpen: false,
                            height: 100,
                            width: 300,
                            position: 'justify',
                            modal: true
                        });
                    }
                    else{
                        $("#customModel").dialog({
                            autoOpen: false,
                            height: 400,
                            width: 400,
                            position: 'justify',
                            modal: true
                        });
                        $("#budgetModel").dialog({
                            autoOpen: false,
                            height: 400,
                            width: 400,
                            position: 'justify',
                            modal: true
                        });
                        $("#budgetModelName").dialog({
                            autoOpen: false,
                            height: 100,
                            width: 300,
                            position: 'justify',
                            modal: true
                        });
                        //
                    }
                }
                function closePopUpWindow(newCustomModelId) {
                    $('#customModel').dialog('close');
                    document.forms.frmParameter.action = '<%=path%>'+"/ScenarioViewerAction.do?scenarioParam=analyzeScenario&flag=analyzeScenario&scenarioId="+document.forms.frmParameter.scenarioId.value+"&scenarioName="+document.forms.frmParameter.scenarioName.value+"&newCustomModelId="+newCustomModelId;
                    document.forms.frmParameter.submit();
                }
                $(document).ready(function(){
                    if ($.browser.msie == true){
                        $("#viewerMeasures").dialog({
                            autoOpen: false,
                            draggable: false,
                            height: 400,
                            width: 600,
                            position: 'justify',
                            modal: true
                        });
                    }
                    else{
                        $("#viewerMeasures").dialog({
                            autoOpen: false,
                            height: 400,
                            width: 720,
                            position: 'justify',
                            modal: true
                        });
                    }
                });
                function totaSumFinalGrowth(obj,val){

                    var idS=obj.id.split("_")

                    var expGrowth=document.getElementById("expGrowth"+idS[1]).value
                    var res = (obj.value *expGrowth)/val
                    res= Math.round(res*Math.pow(10,2))/Math.pow(10,2);
            <%-- alert("res "+res)--%>
                    document.getElementById("finalexpGrowth"+idS[1]).value="";
                    document.getElementById("finalexpGrowth"+idS[1]).value=res
                    document.getElementById("expGrowth"+idS[1]).value=res
                    var table=document.getElementById("tablesorter");
                    var rowCount=table.rows.length
                    rowCount=rowCount-2;
                    var sum=0;
                    for(var i=0;i<rowCount;i++){
                        sum=sum +parseInt(document.getElementById("finalGrowth_"+i).value)
                    
                        
                    }
                    document.getElementById("totalFinalGrowth").innerHTML=sum;
                  
                }
                function sleep(milliseconds) {
                    var start = new Date().getTime();
                    while ((new Date().getTime() - start) < milliseconds){
                        // Do nothing
                    }
                }
               
                try{

                    function downLaodScenario(){
                        <%--alert("in function")--%>
                        var displayLabels=new Array();
                        var tableObj=document.getElementById("tablesorter")

                        var trobj=tableObj.getElementsByTagName("tr");
                   
                        var thobj=trobj[0].getElementsByTagName("th");
                        for(var i=0;i<thobj.length;i++){
                            var tempDisp = thobj[i].innerHTML
                            displayLabels[i]= tempDisp.toString().replace("%","^" ).replace("(","-").replace(")","_");

                        }

                        var tabDetails=new Array();
                        for(var j=1;j<trobj.length;j++){
                            var tdobj=trobj[j].getElementsByTagName("td");
                            var tdStr="";
                            for(var k=0;k<tdobj.length;k++){
                                var inpuobj=tdobj[k].getElementsByTagName("input")
                                if(inpuobj.length==0){

                                    tdStr+="~"+tdobj[k].innerHTML
                                    tdStr=tdStr.toString().replace(",","^")

                                }else{
                                    tdStr+="~"+inpuobj[0].value.toString()
                                    tdStr=tdStr.toString().replace(",","^")
                                }

                            }

                            tabDetails[j-1]=tdStr.substring(1);
               
                            tdStr=""

                        }
                        if(trobj.length>100){
                            sleep(1000)
                        }else if(trobj.length>200){
                             sleep(1500)
                        }else if(trobj.length>300){
                                sleep(2000)
                        }else if(trobj.length>400){
                             sleep(3000)

                        }else{
                            sleep(10000)
                        }
                        sleep(2600)
                        var displaL=displayLabels.toString();

                        var frameObj=document.getElementById("downloadExcelFrame")
                        frameObj.src='<%=path%>'+"/Scenario/JSPs/downloadScenario.jsp?displayLabels="+displaL+"&tabDetails="+tabDetails.toString()+"&scenarioName="+'<%=scenarioName%>';

                    }
                }catch(err){
                    alert("err"+err)
                }

            <%-- function bodyOnloadFun(){
                 var tableObj=document.getElementById("tablesorter")

                    var trobj=tableObj.getElementsByTagName("tr");

                    var thobj=trobj[0].getElementsByTagName("th");
                    for(var i=0;i<thobj.length;i++){
                          if(thobj[i].innerHTML=='Forecast Growth'){
                              document.getElementById("secondaryViewBy").selectedIndex=9
                          }

                    }

                }--%>
        
        </script>
        <style type="text/css">
            .myTextbox10
            {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-weight: normal;
                font-size: 8pt;
                color:#000000;
                padding: 0px;
                width:170px;
                margin-left: 5px;
                border-top: 1px outset #848484;
                border-right: 1px outset #999999;
                border-bottom: 1px outset #999999;
                border-left: 1px outset #848484;
                background-color:#FFFFFF;
            }
            .suggestLink { position: relative;
                           background-color: #FFFFFF;
                           border: 0px solid #000000;
                           border-top-width: 0px;
                           padding: 2px 6px 2px 6px;
                           left:3px;
                           min-width: 20px;
                           max-width: 150px;
            }
            .innerDiv{
                overflow:auto;
                height:95%;
                width:95%;
            }

            .suggestLinkOver {
                background-color: #0099CC;
                padding: 2px 6px 2px 6px;
            }
            #cboRegionsuggestList {
                position: absolute;
                background-color: #FFFFFF;
                text-align: left;
                border: 1px solid #000000;
                border-top-width: 0px;
                width: 160px;
            }

            .imageStyle{
                position: absolute;
                width:12px;
                height:16px;
                display:inline;
            }
            .ajaxboxstyle {
                position: absolute;
                background-color: #FFFFFF;
                text-align: left;
                border: 1px solid #000000;
                border-top-width:1px;
                min-height:40px;
                min-width:112px;
                max-width:300px;
                overflow:auto;
                overflow-x:hidden;
                max-height:100px;
                margin:0em 0.5em;

            }
            .fontsty{
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                background-color:#bdbdbd;
            }
            .myAjaxTable {

                table-layout:fixed;
                background-color: #FFFFFF;
                text-align:left;
                border: 0px solid #000000;

                font-size:10px;
                left:4px;

                width:inherit;
                border-collapse:separate;
                border-spacing:5px;
            }

            .myAjaxTable td {
                min-width:30px;
                max-width:100px;
                text-align:left;
            }
            #wrapper { display: inline;}
            #cbostate { width: 160px; }
            #cboRegion { width: 160px; }
            .black_overlay{
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 100%;
                height: 200%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                filter:alpha(opacity=50);
                overflow:auto;
            }
            .white_content {
                display: none;
                position: absolute;
                top: 25%;
                left: 25%;
                width: 25%;
                padding: 16px;
                border: 16px solid #308dbb;
                background-color: white;
                z-index:1002;
                overflow: auto;
                -moz-border-radius-bottomleft:5px;
                -moz-border-radius-bottomright:5px;
                -moz-border-radius-topleft:5px;
                -moz-border-radius-topright:5px;
            }
            white_contentcolor{
                display: none;
                position: absolute;
                top: 25%;
                left: 25%;
                width: 25%;
                padding: 16px;
                border: 16px solid #308dbb;
                background-color: white;
                z-index:1002;
                overflow: auto;
                -moz-border-radius-bottomleft:5px;
                -moz-border-radius-bottomright:5px;
                -moz-border-radius-topleft:5px;
                -moz-border-radius-topright:5px;
            }
            .white_content1 {
                position: absolute;
                top: 50px;
                left: 25%;
                width: 700px;
                height:400px;
                padding: 16px;
                border: 16px solid silver;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:5px;
                -moz-border-radius-bottomright:5px;
                -moz-border-radius-topleft:5px;
                -moz-border-radius-topright:5px;
            }
            .white_content2 {
                position: absolute;
                top: 30%;
                left: 38%;
                width: 500px;
                height:300px;
                padding: 16px;
                border: 16px solid silver;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
            }
            .ParamRegion{
                background-color:#e6e6e6;
            }
            .inputbox
            {
                font-size: 10px;
                background-color:#fff;

                border-right:#000000 1px outset;
                border-right:#000000 1px inset;
                border-top: white 1px inset;
                border-left: white 1px inset;
                border-bottom: #000000 1px inset;
                font-family: verdana, arial;
                -moz-border-radius-bottomleft:2px;
                -moz-border-radius-bottomright:2px;
                -moz-border-radius-topleft:2px;
                -moz-border-radius-topright:2px;
            }
            .test
            {
                width : 150px;
                height : 150px;
                border : 1px solid #ffff99;
                background-color: #ffff99;
            }
            <%--
            .paramTable
            {
                width : 70%;
                height : auto;
            }
            --%>
            .colorButton
            {
                width:20px;
                height:20px;
                background-color:green;
            }

            a {font-family:Verdana;cursor:pointer;}
            a:link {color:#369}
            .leftcol {
                clear:left;
                float:left;
                width:100%;
            }
            *{
                font: 11px verdana;
            }
            .label{
                background-position:8px center;
                background-repeat:no-repeat;
                border:0 solid #252525;
                clear:both;
                height:auto;
                width:auto;
                cursor:pointer;
                display:block;
                margin-bottom:0;
                margin-right:0;
                padding:0 0.3em 0.3em 22px;
                font-family:verdana;
                font-size:100.01%;
            }
        </style>
    </head>
    <body id="mainBody" >

        <div id="light" align="center" class="white_content"><img  alt="Page is Loading" src='images/ajax.gif'></div>
        <form name="myForm2" method="post"></form>
        <table width="100%">
            <tr>
                <td>
                    <table style="width:100%">
                        <tr valign="top">
                            <td valign="top" style="height:30px;width:10%;">
                                <img alt="pi" width="40px" height="30px"  title="pi" src="<%=request.getContextPath()%>/images/pi_logo.png"/>
                            </td>
                            <td valign="top" style="height:30px;width:80%" >

                            </td>
                            <td valign="top" style="height:30px;width:10%;" align="right">
                                <img alt="ProGen Business Solutions" width="150px" height="30px"  title="Progen Business Solutions" src="<%=request.getContextPath()%>/images/prgLogo.gif"/>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr style="height:15px;width:100%;max-height:100%">
                <td>
                    <table width="100%" class="ui-corner-all">
                        <tr>
                            <td style="height:10px;width:10%" >
                                <font  style="color:#369;font-family:verdana;font-size:15px;font-weight:bold"  title="<%=scenarioName%>"><%=scenarioName%></font>
                            </td>
                            <td style="height:10px;width:20%" align="right">
                                <a href="javascript:void(0)" onclick="javascript:goGlobe()" style="font-size:10px;color:#2191C0;font-weight:bold;text-decoration: none;font-family:Georgia"> Navigation </a> |
                                <a href="javascript:void(0)" onclick="javascript:gohome('<%=loguserId%>')" style="font-size:10px;color:#2191C0;font-weight:bold;text-decoration: none;font-family:Georgia"> Home </a> |
                                <a href="#" style="font-size:10px;color:#2191C0;font-weight:bold;text-decoration: none;font-family:Georgia"> Help </a> |
                                <a href="javascript:void(0)" onclick="javascript:logout()" style="font-size:10px;color:#2191C0;font-weight:bold;text-decoration: none;font-family:Georgia"> Logout </a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr style="width:100%;height:544px;max-height:100%">
                <td>
                    <table width="100%" class="ui-corner-all" style="height:100%" border="1px solid black" cellpadding="0" cellspacing="0">
                        <tr class="ui-corner-all">
                            <td valign="top" style="width:99%" class="ui-corner-all">
                                <form name="frmParameter" action="reportViewer.jsp" method="post" >
                                    <input type="hidden" name="h" id="h" value="<%=request.getContextPath()%>">
                                    <input type="hidden" name="scenarioId" id="scenarioId" value="<%=scenarioId%>">
                                    <input type="hidden" name="scenarioName" id="scenarioName" value="<%=scenarioName%>">
                                    <input type="hidden" name="path" id="path" value="<%=path%>">
                                    <input type="hidden" name="viewByName" id="viewByName" value="<%=viewByName%>">
                                    <input type="hidden" name="modelId" id="modelId" value="<%=modelId%>">

                                    <input type="hidden" name="historicalStartMonth" id="historicalStartMonth" value="<%=(String) timeDetailsArray.get(0)%>">
                                    <input type="hidden" name="historicalEndMonth" id="historicalEndMonth" value="<%=(String) timeDetailsArray.get(1)%>">
                                    <input type="hidden" name="historicalStartYear" id="historicalStartYear" value="<%=(String) timeDetailsArray.get(0)%>">
                                    <input type="hidden" name="historicalEndYear" id="historicalEndYear" value="<%=(String) timeDetailsArray.get(1)%>">
                                    <!-- Begin of Parameters Region-->

                                    <table style="width:99%" valign="top">
                                        <tr valign="top" class="ParamRegion">
                                            <td valign="top" width="100%">
                                                <div class="navsection"  style="height:auto;width:100%">
                                                    <div class="navtitle1" style="width:100%">&nbsp;<b style="font-family:verdana">Parameters Region</b></div>
                                                    <div id="tabParameters">
                                                        <table  class="paramTable" width="70%"  >
                                                            <tr>
                                                                <td valign="top" align="left" width="100%">
                                                                    <%=scenarioParamSectionDisplay%>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </div>
                                                </div>
                                            </td>
                                        </tr>

                                        <tr valign="top">
                                            <td valign="top" width="100%">
                                                <div class="navsection" >
                                                    <div class="navtitle1" style="width:100%">&nbsp;<b style="font-family:verdana">Table Region</b>
                                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a id="Measure" href="javascript:showViewerMeasures()" title="Click to Select Measures">Select Measures</a>&nbsp;
                                                        <%--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a id="viewGraph" href="javascript:showViewerGraph()" title="Click to View Graph">View Graph</a>&nbsp;--%>
                                                    </div>
                                                    <div id="tabTable">
                                                        <table style="height:auto" width="100%">
                                                            <tr>
                                                                <td valign="top" width="100%">
                                                                    <%=scenarioTableScetionDisplay%>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td valign="top" width="100%">
                                                                    <%=GraphDisplay%>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </div>
                                                </div>
                                            </td>
                                        </tr>
                                        <%--
                                        <tr valign="top">
                                            <td valign="top" width="100%">
                                                <div class="navsection" >
                                                    <div class="navtitle1" style="width:100%;">&nbsp;<b style="font-family:verdana">Graph Region</b></div>
                                                    <div id="tabGraph">
                                                        <table   style="height:auto" width:="100%">
                                                            <tr>
                                                                <td valign="top" width="100%">

                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </div>
                                                </div>
                                            </td>
                                        </tr>
                                        --%>
                                    </table>
                                </form>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <Table align="center">
                        <Tr>
                            <Td><Input type="button" class="navtitle-hover" style="width:auto"  value="Scenario Home" onclick="goToScenarioHome();"></Td>
                            <Td><Input type="button" class="navtitle-hover" style="width:auto"  value="Save Scenario" onclick="return saveScenario();"></Td>
                            <Td><Input type="button" class="navtitle-hover" style="width:auto"  value="Download To Excel " onclick="downLaodScenario()"></Td>
                        </Tr>
                    </Table>
                </td>
            </tr>
        </table>
        <br/>
        <table width="100%" class="fontsty">
            <tr class="fontsty" style="height:10px;width:100%;max-height:100%;background-color:#bdbdbd">
                <td style="height:10px;width:100%;background-color:#bdbdbd">
                    <center style="background-color:#bdbdbd"><font  style="color:#fff;font-family:verdana;font-size:10px;font-weight:normal" align="center">Copyright © 2009-12 <a href="http://www.progenbusiness.com" style="color:red;font-weight:bold;font-size:10px;font-family:verdana">Progen Business Solutions.</a> All Rights Reserved</font></center>
                </td>
            </tr>
        </table>
        <div id="viewerMeasures" style="display:none">
            <iframe id="viewerMsrs" NAME='viewerMsrs' style="height:100%;width:100%;overflow:auto" frameborder="0"  SRC='#'></iframe>
        </div>
        <div id="fade" class="black_overlay"></div>
        <div id="customModel">
            <iframe src="#" id="customModelFrame" style="height:100%;width:100%" frameborder="0"></iframe>
        </div>
        <div id="budgetModel">
            <iframe src="#" id="budgetModelFrame" style="height:100%;width:100%" frameborder="0"></iframe>
        </div>
        <div id="budgetModelName" title="Budget Name" style="display: none">
            <br><br>
            <table id="budgetModelTable" align="center">
                <tr>
                    <td>
                        BudgeModel Name:
                    </td>
                    <td >
                        <input type="text" id="budgeModelName" name="BudgeModel Name" value="">
                    </td>
                </tr>
                <table align="center">
                    <tr>
                        <td>
                            <input type="button" name="save" value="Save" onclick="savebudgemodelName('<%=savedSeededModel%>')"  class="navtitle-hover" style="width:auto" >
                        </td>
                    </tr>
                </table>

            </table>

        </div>
        <IFRAME NAME="downloadExcelFrame" ID="downloadExcelFrame" STYLE="display:none;width:0px;height:0px"  frameborder="0"></IFRAME>

        <%          PbScenarioCollection scnCollect = new PbScenarioCollection();
                        scnCollect.setDrillurlTemp(null);
                        scnCollect.setCombinedRetObj(new PbReturnObject());
                        scnCollect.setNonViewByList(new ArrayList());
                        scnCollect.setNonViewByMap(new HashMap());
                        scnCollect.setNormalHm(new HashMap());
                        scnCollect.setScenarioModelsTemp(new LinkedHashMap());
                        scnCollect.setScenarioParamsTemp(new HashMap());
                        scnCollect.setScenarioViewbyMainTemp(new LinkedHashMap());
                        scnCollect.setTimeDetsArrayList(new ArrayList());
                        scnCollect.setTimeDetsMapTemp(new LinkedHashMap());
        %>
    </body>
</html>

<%
            } catch (Exception exp) {
                exp.printStackTrace();
            }

%>
