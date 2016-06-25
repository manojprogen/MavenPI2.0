<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%

            String historicalStartMonth = "";
            String historicalEndMonth = "";
            String scenarioId = "";
           // ////.println("request.getParameter---"+request.getParameter("historicalStartMonth"));
            if (request.getParameter("historicalStartMonth") != null) {

                historicalStartMonth = request.getParameter("historicalStartMonth");

            }
            if (request.getParameter("historicalEndMonth") != null) {
                historicalEndMonth = request.getParameter("historicalEndMonth");
            }
            if (request.getParameter("scenarioId") != null) {
                scenarioId = request.getParameter("scenarioId");
            }
            ////////////////////////////////////////.println.println("historicalStartMonth is:: " + historicalStartMonth);
            ////////////////////////////////////////.println.println("historicalEndMonth is:: " + historicalEndMonth);
            ////////////////////////////////////////.println.println("scenarioId is:: " + scenarioId);

            String path = request.getContextPath();


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
            function getAllRequiredData() {
                $.ajax({
                    url: '<%=path%>'+"/ScenarioViewerAction.do?scenarioParam=getModelBasisValues",
                    success: function(data){
                        var basisValues = data.split("\n");
                        var selectBoxObj = document.getElementById("modelBasis");
                        var opt;
                        for(var i=0;i<basisValues.length;i++)
                        {
                            if(basisValues[i] != "") {
                                opt = document.createElement("option");
                                opt.text=$.trim(basisValues[i]);
                                opt.value=$.trim(basisValues[i]);
                                selectBoxObj.appendChild(opt);
                            }
                        }
                    }
                });
                 // alert("Here")
                $.ajax({
                    url: '<%=path%>'+"/ScenarioViewerAction.do?scenarioParam=getCustomModelMonthsList&historicalStartMonth="+'<%=historicalStartMonth%>'+"&historicalEndMonth="+'<%=historicalEndMonth%>',
                    success: function(data){
                        var monthsList = data.split("\n");
                        var divObj = document.getElementById("t2");
                        var tableObj = document.createElement("table");
                        divObj.appendChild(tableObj)
                        var trObj;
                        var tdObj;
                        var inputObj;
                        for(var j=0;j<monthsList.length;j++) {
                            if(monthsList[j] != "") {
                                trObj = document.createElement("tr");
                                tdObj = trObj.insertCell(0);
                                tdObj.innerHTML = monthsList[j];
                                tdObj = trObj.insertCell(1);
                                inputObj = document.createElement("input");
                                inputObj.type = "checkbox";
                                inputObj.name = "custModelMonths";
                                inputObj.id = "custModelMonths";
                                inputObj.value = monthsList[j];
                                tdObj.appendChild(inputObj);
                                tableObj.appendChild(trObj);
                            }
                        }
                    }
                });
                document.getElementById("modelName").focus();
            }
            function showWeightedAvgUI()
            {
               // alert(document.getElementById("modelBasis").value)
                var selectedBasis = document.getElementById("modelBasis").value;                
                if(selectedBasis=="Weighted Average")
                {
                   // alert("show")
                    showTextBoxes();
                }
                else
                {
                   // alert("Hide")
                    hideTextBoxes();
                }
            }
            function showTextBoxes()
            {
               //  alert(document.getElementById("t2"))
                 
                var divObj = document.getElementById("t2");
               // alert(divObj.getElementsByTagName("table"))

                var tableObj = divObj.getElementsByTagName("table");
               // alert(tableObj[0].getElementsByTagName("tr"))
                var trObjs = tableObj[0].getElementsByTagName("tr");                
                for(var i=0;i<trObjs.length;i++)
                {
                    var newTdObj = trObjs[i].insertCell(2);                    
                    var inputObj = document.createElement("input");
                    inputObj.type = 'text';
                    inputObj.name = 'weights'+i;
                    inputObj.id = 'weights'+i;                    
                    inputObj.style.width = '150px';                   
                    newTdObj.appendChild(inputObj);
                }
                document.getElementById("textMsg").style.display = 'block';
            }
            function hideTextBoxes()
            {
                var divObj = document.getElementById("t2");
                var tableObj = divObj.getElementsByTagName("table");
                var trObjs = tableObj[0].getElementsByTagName("tr");                
                for(var j=0;j<trObjs.length;j++)
                {
                    var lastTdObj = trObjs[j].getElementsByTagName("td")[2];
                    if(lastTdObj)
                    {
                        trObjs[j].deleteCell(2);
                    }
                    trObjs[j].getElementsByTagName("td")[1].getElementsByTagName("input")[0].checked = false;
                }
                document.getElementById("textMsg").style.display = 'none';
            }
            function checkModelName()
            {
                var modelName = document.getElementById("modelName").value;
                if(modelName=="" || modelName==null)
                {
                    alert("Please Enter Model Name");
                    document.getElementById("modelName").focus();
                    return false;
                }
                else
                {
                    return true;
                }
            }
            function checkBasis()
            {
                var modelBasis = document.getElementById("modelBasis").value;
                if(modelBasis=="" || modelBasis=="--Select--")
                {
                    alert("Please select Basis");
                    document.getElementById("modelBasis").focus();
                    return false;
                }
                else
                {
                    return true;
                }
            }
            function checkMonths()
            {
                var selectedBasis = document.getElementById("modelBasis").value;
                var totalWeight=0;
                var i=0;
                var matchFlag = false;
                var obj = document.getElementsByName("custModelMonths");                
                if(isNaN(obj.length))
                {                   
                    if(document.getElementsByName("custModelMonths").checked)
                    {
                        if(selectedBasis == "Weighted Average")
                        {
                            totalWeight = document.getElementsByName("weights0")[0].value;
                            totalWeight = parseFloat(totalWeight);
                            if(totalWeight != 100)
                            {
                                alert("Total must be 100");
                                document.getElementsByName("weights0")[0].focus();
                                return false;
                            }
                            else
                            {
                                return true;
                            }
                        }
                        else
                        {
                            return true;
                        }
                    }
                    else
                    {
                        alert('Please check month');
                        document.getElementsByName("custModelMonths").focus();
                        return false;
                    }
                }
                else
                {                    
                    for(var j=0;j<obj.length;j++)
                    {
                        if(document.getElementsByName("custModelMonths")[j].checked==true)
                        {
                            if(selectedBasis == "Weighted Average")
                            {
                                var weightVal = document.getElementsByName('weights'+j)[0].value;                                
                                if(weightVal == '')
                                {
                                    weightVal = 0;
                                }
                                totalWeight = totalWeight + parseFloat(weightVal);
                                i++;
                            }
                            else
                            {
                                i++;
                            }
                        }
                    }
                    if(i==0)
                    {
                        alert('Please check month');
                        document.getElementsByName("custModelMonths")[0].focus();
                        return false;
                    }
                    if(selectedBasis == "Weighted Average" && totalWeight < 100)
                    {
                        alert("Total must be 100");
                        document.getElementsByName("weights0")[0].focus();
                        return false;
                    }
                    if(selectedBasis == "Weighted Average" && totalWeight > 100)
                    {
                        alert("Total should not exceed 100");
                        document.getElementsByName("weights0")[0].focus();
                        return false;
                    }
                    return true;
                }
            }
            function checkCreateCustModel()
            {  
                if(checkModelName() && checkBasis() && checkMonths())
                {
                    var modelName = document.getElementById("modelName").value;
                    var modelBasis = document.getElementById("modelBasis").value;
                    var custModelMonths = document.forms.myForm.custModelMonths;
                    var custModelMonthsStr = "";
                    var weightsStr = "";
                    for(var i=0;i<custModelMonths.length;i++) {
                        if(custModelMonths[i].checked==true) {
                            if(custModelMonthsStr=="") {
                                custModelMonthsStr = custModelMonths[i].value;
                            }else {
                                custModelMonthsStr = custModelMonthsStr+","+custModelMonths[i].value;
                            }

                            if(modelBasis=="Weighted Average") {
                                if(weightsStr=="") {
                                    if(document.getElementById("weights"+i).value=='') {
                                        weightsStr = "0";
                                    }else {
                                        weightsStr = document.getElementById("weights"+i).value;
                                    }
                                }else {
                                    if(document.getElementById("weights"+i).value=='') {
                                        weightsStr = weightsStr+","+"0";
                                    }else {
                                        weightsStr = weightsStr+","+document.getElementById("weights"+i).value;
                                    }
                                }
                            }
                        }
                    }
                    var newCustomModelId = "";
                    $.ajax({
                        url: '<%=path%>'+"/ScenarioViewerAction.do?scenarioParam=saveCustomModel&scenarioId="+'<%=scenarioId%>'+"&modelName="+modelName+"&modelBasis="+modelBasis+"&custModelMonthsStr="+custModelMonthsStr+"&weightsStr="+weightsStr,
                        success: function(data){                            
                            var temp = data.split("\n");
                            for(var i=0;i<temp.length;i++) {
                                if(temp[i] != "") {
                                    newCustomModelId = temp[i];
                                }
                            }
                            parent.closePopUpWindow(newCustomModelId);
                        }
                    });                   
                    
                    return true;
                }
                else
                {
                    return false;
                }
            }
        </script>
        <style type="text/css">
            *{
                font:11px verdana
            }
        </style>
    </head>
    <body onload="getAllRequiredData()">
        <%

        %>
        <br>
        <center>
            <font> Fields marked <span style="color:red">*</span> are MANDATORY </font>
            <br><br>
            <form name="myForm" method="post">
                <table id="t1">
                    <tr>
                        <td><span style="color:red">*</span>Model Name</td>
                        <td>
                            <input type="text" id="modelName" name="modelName" style="width:150px">
                        </td>
                    </tr>
                    <tr>
                        <td><span style="color:red">*</span>Basis</td>
                        <td>
                            <select id="modelBasis" name="modelBasis" style="width:152px" onchange="showWeightedAvgUI()">
                                <option value="">--Select--</option>
                            </select>
                        </td>
                    </tr>
                </table>
                <div id="t2">

                </div>
                <table id="t3">
                    <tr>
                        <td><span style="color:red;display:none" id="textMsg">(Total should not exceed 100)</span></td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <td>
                            <input type="button" value="Go" onclick="return checkCreateCustModel()">
                        </td>
                    </tr>
                </table>
            </form>
        </center>
    </body>
</html>
