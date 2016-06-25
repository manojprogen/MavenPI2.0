/*
function checkSum()
{
    var tableObj = document.getElementById('target');
    //alert(tableObj)
    var trObj = tableObj.getElementsByTagName("tr");
    var tdObj = trObj[0].getElementsByTagName("td");
    var rowName;
    var columnName;
    //var tds = tdObj.length;

    var rowTemp = new Array();
    for(var b=0;b<trObj.length;b++)
    {
        rowTemp[b] = 0;
    }


    for(var y=1;y<trObj.length;y++)
    {
        tdObj = trObj[y].getElementsByTagName("td");
        for(var z=1;z<tdObj.length;z++)
        {
            if(z != parseInt(tdObj.length)-1)
            {
                var value = tdObj[z].getElementsByTagName("input")[0].value;
                //alert(value);
                rowTemp[y] = parseInt(rowTemp[y] + parseInt(value));
                //alert(rowTemp[y]);
            }
            else
            {
                var rowTotal = parseInt(tdObj[z].getElementsByTagName("input")[0].value);
                //alert(rowTotal);
                rowName = tdObj[z].parentNode.getElementsByTagName("td")[0].innerHTML;
                var rowSum = parseInt(rowTemp[y]);
                //alert(rowSum)
                if(rowSum != rowTotal)
                {
                    alert("Row Total at "+rowName+" not matched");
                    return false;
                }
                else
                {
                    return true;
                }
            }
        }
    }

    var columnTemp = new Array();
    for(var a=0;a<tdObj.length;a++)
    {
        columnTemp[a] = 0;
    }

    for(var i=1;i<trObj.length;i++)
    {
        if(i != parseInt(trObj.length)-1)
        {
            tdObj = trObj[i].getElementsByTagName("td");
            for(var j=1;j<tdObj.length;j++)
            {
                var val = tdObj[j].getElementsByTagName("input")[0].value;
                columnTemp[j] = parseInt(columnTemp[j] + parseInt(val));
                flag = columnTemp[j];
            }
        }
        else
        {
            tdObj = trObj[i].getElementsByTagName("td");
            for(var k=1;k<tdObj.length;k++)
            {
                var columTotal = parseInt(tdObj[k].getElementsByTagName("input")[0].value);
                columnName = trObj[0].getElementsByTagName("td")[k].innerHTML;
                var columnSum = parseInt(columnTemp[k])
                if(columnSum != columTotal)
                {
                    alert("Column Total at "+columnName+" not matched");
                    return false;
                }
                else
                {
                    return true;
                }

            }
        }
    }
}
function saveTarget()
{
    //alert('here');
    if(checkSum())
    {
        document.myForm.action="pbSaveTarget.jsp";
        document.myForm.submit();
        return true;
    }
    else
    {
        return false;
    }

}

function isNumberKey(evt)
{
    var charCode = (evt.which) ? evt.which : event.keyCode
    if (charCode > 31 && (charCode < 48 || charCode > 57))
    return false;

    return true;
}
*/

/* function initialogTypeTarget()
 {
        if ($.browser.msie == true){
            $("#editTarget").dialog({
                autoOpen: false,
                height: 400,
                width: 450,
                position: 'justify',
                modal: true
            });
        }
        else{
            $("#editTarget").dialog({
                autoOpen: false,
                 height: 400,
                width: 400,
                position: 'justify',
                modal: true
            });
        }
} */

var xmlHttp;
var flag;
//var primDim = document.getElementById("primaryDimension").value;
function showHint()
{ //alert('kkkkk ');
    if(document.getElementById("country1").value.length==0 || document.getElementById("country1").value=="" || document.getElementById("country1").value==null)
    {
        document.getElementById("measure").options.length = 0;
        var obj6 = document.getElementById("measure");
        obj6.options[0] = new Option("--Select--","--Select--");

      //  document.getElementById("primaryTargetParameter").options.length = 0;
      //  var obj7 = document.getElementById("primaryTargetParameter");
      //  obj7.options[0] = new Option("--Select--","--Select--");
    }
    // alert("1");
    //alert(document.getElementById("country1").value);
    //alert(document.myForm.country1.value);
    str = document.getElementById("country1").value;
    if (str.length==0)
    {
        document.getElementById("txtHint").innerHTML="";
        return;
    }
    xmlHttp=GetXmlHttpObject();
    if (xmlHttp==null)
    {
        alert ("Your browser does not support AJAX!");
        return;
    }
    var url="../../measure";
    url=url+"?q="+str;
    //alert('url ' +url);

    xmlHttp.onreadystatechange=stateChanged1;
    xmlHttp.open("GET",url,true);
    xmlHttp.send(null);
}
function stateChanged1()
{
    if (xmlHttp.readyState==4)
    {
        //alert("Hiii")
        //document.getElementById("txtHint").innerHTML=xmlHttp.responseText;
        var output=xmlHttp.responseText
        //alert("output is: "+output)
        var measure=output.split("\n");
        //alert("columns "+columns);

        var obj = document.myForm.measure;

        for(var i=obj.length-1;i>=0;i--)
        {
            obj.options[i] = null;
        }
        obj.options[0] = new Option("--Select--","--Select--");

        for(var j=0;j<measure.length-1;j++)
        {
            var measureName = measure[j].split(",",1);
            var measureId = splitString(measure[j],",");
            //var measureId = measure[j].split(",",2);
            //alert(measureName);
            //alert(measureId);
            //obj.options[j+1] = new Option(measureName,measureId);

            //obj.options[j].selected=true;

            //alert(durationVal);
            addOption(document.getElementById('measure'),measureName,measureId);
        }
        showPrimaryParameters();
    }
}
function splitString(stringToSplit,separator)
{
    var arrayOfStrings = stringToSplit.split(separator);
    var temp = arrayOfStrings[1];
    return temp;
}
function addOption(selectbox,text,value)
{
    var optn = document.createElement("OPTION");
    optn.text = text;
    optn.value = value;
    //alert("In addOPtions--> "+value);
    selectbox.options.add(optn);
}


function isNumberKey(evt)
{
    var charCode = (evt.which) ? evt.which : event.keyCode
    if (charCode > 31 && (charCode < 48 || charCode > 57))
        return false;

    return true;
}

/*function saveTarget()
{
    document.myForm.action="pbSaveTarget.jsp";
    document.myForm.submit();
} */
function populateRest(textBoxName)
{
    //alert(textBoxName)
    var data = document.getElementsByName(textBoxName)[0].value;
    //alert(data);
    pNode1 = document.getElementsByName(textBoxName)[0].parentNode;
    //alert(pNode1);
    pNode2 = pNode1.parentNode;
    //alert(pNode2)
    mytds = pNode2.getElementsByTagName("td");
    //alert(mytds.length);

    for(i=2;i<mytds.length;i++)
    {
        //alert(mytds[i].getElementsByTagName("input")[0].getAttribute("name"));
        var varName = mytds[i].getElementsByTagName("input")[0].getAttribute("name");
        document.getElementsByName(varName)[0].value = data;
    }
}

function checkTargetName()
{
    var targetName = document.myForm.targetName.value;
    if(targetName=="" || targetName==null)
    {
        alert("Please Enter Target Name");
        document.myForm.targetName.focus();
        return false;
    }
    else
    {
        return true;
    }
}
function checkParameterIds()
{
    var parameterIds = document.myForm.parameterIds.value;
    if(parameterIds=="" || parameterIds==null)
    {
        alert("Please Select Parameters.");
        return false;
    }
    else
    {
        return true;
    }
}
function checktargetStartDate()
{
    // var minTimeVal = document.getElementById("timeLevel").value;
    var allLevels=document.getElementById("timeLevels").value;
    //alert('jjk allLevels '+allLevels)
    var time=allLevels.split(",");
    var m=allLevels[0];
    var minTimeVal ="";
    if(m=="1")
        minTimeVal="Year";
    else if(m=="2")
        minTimeVal="Qtr";
    else if(m=="3")
        minTimeVal="Month";
    else if(m=="4")
        minTimeVal="Day";
    if(minTimeVal=="Day" && document.myForm.targetStartDate.value=="")
    {
        alert("Please Select Target Start Date");
        document.myForm.targetStartDate.focus();
        return false;
    }
    else
    {
        return true;
    }
}
function checktargetEndDate()
{
    //var minTimeVal = document.getElementById("timeLevel").value;
    var allLevels=document.getElementById("timeLevels").value;
    var time=allLevels.split(",");
    var m=allLevels[0];
    var minTimeVal ="";
    if(m=="1")
        minTimeVal="Year";
    else if(m=="2")
        minTimeVal="Qtr";
    else if(m=="3")
        minTimeVal="Month";
    else if(m=="4")
        minTimeVal="Day";
    if(minTimeVal=="Day" && document.myForm.targetEndDate.value=="")
    {
        alert("Please Select Target End Date");
        document.myForm.targetEndDate.focus();
        return false;
    }
    else
    {
        return true;
    }
}
function checktargetStartMonth()
{
    //var minTimeVal = document.getElementById("timeLevel").value;
    var allLevels=document.getElementById("timeLevels").value;
    var time=allLevels.split(",");
    var m=allLevels[0];
    var minTimeVal ="";
    if(m=="1")
        minTimeVal="Year";
    else if(m=="2")
        minTimeVal="Qtr";
    else if(m=="3")
        minTimeVal="Month";
    else if(m=="4")
        minTimeVal="Day";
    if(minTimeVal=="Month" && document.myForm.targetStartMonth.value=="")
    {
        alert("Please Select Target Start Month");
        document.myForm.targetStartMonth.focus();
        return false;
    }
    else
    {
        return true;
    }
}
function checktargetEndMonth()
{
    // var minTimeVal = document.getElementById("timeLevel").value;
    var allLevels=document.getElementById("timeLevels").value;
    var time=allLevels.split(",");
    var m=allLevels[0];
    var minTimeVal ="";
    if(m=="1")
        minTimeVal="Year";
    else if(m=="2")
        minTimeVal="Qtr";
    else if(m=="3")
        minTimeVal="Month";
    else if(m=="4")
        minTimeVal="Day";
    if(minTimeVal=="Month" && document.myForm.targetEndMonth.value=="")
    {
        alert("Please Select Target End Month");
        document.myForm.targetEndMonth.focus();
        return false;
    }
    else
    {
        return true;
    }
}
function checktargetStartQtr()
{
    // var minTimeVal = document.getElementById("timeLevel").value;
    var allLevels=document.getElementById("timeLevels").value;
    var time=allLevels.split(",");
    var m=allLevels[0];
    var minTimeVal ="";
    if(m=="1")
        minTimeVal="Year";
    else if(m=="2")
        minTimeVal="Qtr";
    else if(m=="3")
        minTimeVal="Month";
    else if(m=="4")
        minTimeVal="Day";
    if(minTimeVal=="Quarter" && document.myForm.targetStartQtr.value=="")
    {
        alert("Please Select Target Start Quarter");
        document.myForm.targetStartQtr.focus();
        return false;
    }
    else
    {
        return true;
    }
}
function checktargetEndQtr()
{
    // var minTimeVal = document.getElementById("timeLevel").value;
    var allLevels=document.getElementById("timeLevels").value;
    var time=allLevels.split(",");
    var m=allLevels[0];
    var minTimeVal ="";
    if(m=="1")
        minTimeVal="Year";
    else if(m=="2")
        minTimeVal="Qtr";
    else if(m=="3")
        minTimeVal="Month";
    else if(m=="4")
        minTimeVal="Day";
    if(minTimeVal=="Quarter" && document.myForm.targetEndQtr.value=="")
    {
        alert("Please Select Target End Quarter");
        document.myForm.targetEndQtr.focus();
        return false;
    }
    else
    {
        return true;
    }
}
function checktargetStartYear()
{
    //var minTimeVal = document.getElementById("timeLevel").value;
    var allLevels=document.getElementById("timeLevels").value;
    var time=allLevels.split(",");
    var m=allLevels[0];
    var minTimeVal ="";
    if(m=="1")
        minTimeVal="Year";
    else if(m=="2")
        minTimeVal="Qtr";
    else if(m=="3")
        minTimeVal="Month";
    else if(m=="4")
        minTimeVal="Day";
    if(minTimeVal=="Year" && document.myForm.targetStartYear.value=="")
    {
        alert("Please Select Target Start Year");
        document.myForm.targetStartYear.focus();
        return false;
    }
    else
    {
        return true;
    }
}
function checktargetEndYear()
{
    // var minTimeVal = document.getElementById("timeLevel").value;
    var allLevels=document.getElementById("timeLevels").value;
    var time=allLevels.split(",");
    var m=allLevels[0];
    var minTimeVal ="";
    if(m=="1")
        minTimeVal="Year";
    else if(m=="2")
        minTimeVal="Qtr";
    else if(m=="3")
        minTimeVal="Month";
    else if(m=="4")
        minTimeVal="Day";
    if(minTimeVal=="Year" && document.myForm.targetEndYear.value=="")
    {
        alert("Please Select Target End Year");
        document.myForm.targetEndYear.focus();
        return false;
    }
    else
    {
        return true;
    }
}
function checkBusinessAreas()
{
    if(document.myForm.country1.value=="")
    {
        alert("Please Select Business Area");
        document.myForm.country1.focus();
        return false;
    }
    else
    {
        return true;
    }
}
function checkMeasure()
{
    var measure = document.myForm.measure.value;
    if(measure=="" || measure==null || measure=="--Select--")
    {
        alert("Please select Measure");
        document.myForm.measure.focus();
        return false;
    }
    else
    {
        return true;
    }
}
function checkTimeLevel()
{
    var timeLevel = document.myForm.timeLevels.value;
    if(timeLevel=="" || timeLevel==null || timeLevel=="--Select--")
    {
        alert("Please select Time Levels");
        //document.myForm.timeLevel.focus();
        return false;
    }
    else
    {
        return true;
    }
}
function checkPrimaryDimension()
{
    var primaryDimension = document.myForm.primaryDimension.value;
    if(primaryDimension=="" || primaryDimension==null || primaryDimension=="--Select--")
    {
        alert("Please select Primary Dimension");
        document.myForm.primaryDimension.focus();
        return false;
    }
    else
    {
        return true;
    }
}
function checkPrimaryParameter()
{
    var primaryParameter = document.getElementById("primaryTargetParameter").value;
    if(primaryParameter=="" || primaryParameter==null || primaryParameter=="--Select--")
    {
        alert("Please select Primary Target Parameter");
        document.myForm.primaryTargetParameter.focus();
        return false;
    }
    else
    {
        return true;
    }
}

/*function checkSecondaryParameter()
{
    var targetTypeObj = document.myForm.targetType;
    var secondaryParameter = document.getElementById("secondaryTargetParameter").value;
    //alert(targetTypeObj)
    if((targetTypeObj[1].checked==true) && (secondaryParameter=="" || secondaryParameter==null || secondaryParameter=="--Select--"))
    {
        alert("Please select Secondary Target Parameter");
        document.myForm.secondaryTargetParameter.focus();
        return false;
    }
    else
    {
        return true;
    }
}
*/
/*
function targetType()
{
    var mn = 0;
    var targetType = document.myForm.targetType;
    for(var n=0;n<targetType.length;n++)
    {
        if(targetType[n].checked==true)
        {
            mn++;
        }
    }
    if(mn<1)
    {
        return false;
    }
    else
    {
        return true;
    }

}*/
function formFieldArray(){
    var myArray = new Array(10);
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

    return myArray;
}

function quarterArray(){
    var myArray = new Array(4);
    myArray['Q1'] = "1";
    myArray['Q2'] = "2";
    myArray['Q3'] = "3";
    myArray['Q4'] = "4";

    return myArray;
}
/*
    function validateTargetMaster(names,mIds,sDates,eDates)
    {
        var minTimeLevel = document.getElementById("timeLevel").value;

        //alert(names)
        //alert(mIds)
        //alert(sDates)
        //alert(eDates)
        var count = 0;
        var targetNamesList = names.split(",");
        var measureIds = mIds.split(",");
        var startDates = sDates.split(",");
        var endDates = eDates.split(",");

        var targetName = document.getElementById("targetName").value;
        var measureId = document.getElementById("measure").value;

        var targetStartDateStr;
        var targetEndDateStr;

        if(minTimeLevel=="Day")
        {
            targetStartDateStr = document.getElementById("targetStartDate").value;
            targetEndDateStr = document.getElementById("targetEndDate").value;

            targetStartDateStr = new Date(targetStartDateStr);
            targetEndDateStr = new Date(targetEndDateStr);
        }
        else if(minTimeLevel=="Month")
        {
            targetStartDateStr = document.getElementById("targetStartMonth").value;
            targetEndDateStr = document.getElementById("targetEndMonth").value;
        }
        else if(minTimeLevel=="Quarter")
        {
            targetStartDateStr = document.getElementById("targetStartQtr").value;
            targetEndDateStr = document.getElementById("targetEndQtr").value;
        }
        else if(minTimeLevel=="Year")
        {
            targetStartDateStr = document.getElementById("targetStartYear").value;
            targetEndDateStr = document.getElementById("targetEndYear").value;
        }


        //var now = new Date();
        //now.format("m/dd/yy");


        if(checkTargetName() && checkBusinessAreas() && checkMeasure() && checkTimeLevel() && checktargetStartDate() && checktargetEndDate()
            && checktargetStartMonth() && checktargetEndMonth() && checktargetStartQtr() && checktargetEndQtr() && checktargetStartYear() && checktargetEndYear()
            && checkPrimaryParameter() )
        {
            for(var i=0;i<targetNamesList.length;i++)
            {
                if(targetName==targetNamesList[i])
                {
                    alert("Target name already exists");
                    document.myForm.targetName.focus();
                    count++;
                    return false;
                }
                if(((measureIds[i]-measureId)==0) && targetStartDateStr==(startDates[i]) && targetEndDateStr==(endDates[i]))
                {
                    //alert("in if block")
                    alert("A Measure can not have more than one target for same time range");
                    document.myForm.targetName.focus();
                    return false;
                }
            }

            if(targetEndDateStr < targetStartDateStr)
            {
                alert("Target End-Date must be greater than Target Start-Date");
                document.myForm.targetEndDate.focus();
                return false;
            }

            //if(count==0)
            //{
                parent.document.getElementById("fade").style.display = 'none';
                parent.document.getElementById("createTargetDisp").style.display = 'none';
                document.myForm.action = "pbTargetParametersDisplay.jsp";
                document.myForm.submit();

                return true;
            //}
        }
        else
        {
            return false;
        }
    }
*/
function validateTargetMaster(names,mIds,sDates,eDates)
{
    var minTimeLevel = "";//document.getElementById("timeLevel").value;
    var allTimeLevels = document.getElementById("timeLevels").value;
    var m=allTimeLevels.split(",");
    var max=m[0];
    if(max=="1")
        minTimeLevel="Year";
    else if(m==2)
        minTimeLevel="Qtr";
    else if(m==3)
        minTimeLevel="Month";
    else if(m==4)
        minTimeLevel="Day";

    var count = 0;
    var targetNamesList = names.split(",");
    var measureIds = mIds.split(",");
    var startDates = sDates.split(",");
    var endDates = eDates.split(",");

    var targetName = document.getElementById("targetName").value;
    var measureId = document.getElementById("measure").value;

    var targetStartDateStr;
    var targetEndDateStr;

    if(minTimeLevel=="Day")
    {
        targetStartDateStr = document.getElementById("targetStartDate").value;
        targetEndDateStr = document.getElementById("targetEndDate").value;
    }
    else if(minTimeLevel=="Month")
    {
        targetStartDateStr = document.getElementById("targetStartMonth").value;
        targetEndDateStr = document.getElementById("targetEndMonth").value;
    }
    else if(minTimeLevel=="Quarter"||minTimeLevel=="Qtr")
    {
        targetStartDateStr = document.getElementById("targetStartQtr").value;
        targetEndDateStr = document.getElementById("targetEndQtr").value;
    }
    else if(minTimeLevel=="Year")
    {
        targetStartDateStr = document.getElementById("targetStartYear").value;
        targetEndDateStr = document.getElementById("targetEndYear").value;
    }


    //var now = new Date();
    //now.format("m/dd/yy");


    if(checkTargetName()&&checkParameterIds() && checkTimeLevel() && checktargetStartDate() && checktargetEndDate()
        && checktargetStartMonth() && checktargetEndMonth() && checktargetStartQtr() && checktargetEndQtr() && checktargetStartYear() && checktargetEndYear()
        )
        {
        for(var i=0;i<targetNamesList.length;i++)
        {
            if(targetName==targetNamesList[i])
            {
                alert("Target name already exists");
                document.myForm.targetName.focus();
                count++;
                return false;
            }
            if(((measureIds[i]-measureId)==0) && targetStartDateStr==(startDates[i]) && targetEndDateStr==(endDates[i]))
            {
                //alert("in if block")
                alert("A Measure can not have more than one target for same time range");
                document.myForm.targetName.focus();
                return false;
            }
        }

        if(minTimeLevel=="Day")
        {
            targetStartDateStr = new Date(targetStartDateStr);
            targetEndDateStr = new Date(targetEndDateStr);
            if(targetEndDateStr < targetStartDateStr)
            {
                alert("Target End-Date must be greater than Target Start-Date");
                document.myForm.targetEndDate.focus();
                return false;
            }
        }
        else if(minTimeLevel=="Month")
        {
            var getStartMonthYear = targetStartDateStr.substring(4, targetStartDateStr.length);
            var getEndMonthYear = targetEndDateStr.substring(4, targetEndDateStr.length);

            if(getEndMonthYear < getStartMonthYear)
            {
                alert("Target End-Month must be greater than Target Start-Month");
                document.myForm.targetEndMonth.focus();
                return false;
            }

            if(getEndMonthYear == getStartMonthYear)
            {
                //12-SEPT-09
                var getStartMonth = targetStartDateStr.substring(0,3);
                var getEndMonth = targetEndDateStr.substring(0,3);
                //alert(getStartMonth);
                //alert(getEndMonth);
                var theFormFieldArray = formFieldArray();
                for (key in theFormFieldArray)
                {
                    if(eval(theFormFieldArray[getEndMonth] + ' < '+theFormFieldArray[getStartMonth]))
                    {
                        alert('Target End-Month must be greater than Target Start-Month');
                        document.myForm.targetEndMonth.focus();
                        return false;
                    }
                }
            }
        }
        else if(minTimeLevel=="Quarter"||minTimeLevel=="Qtr")
        {
            var getStartQtrYear = targetStartDateStr.substring(3, targetStartDateStr.length);
            var getEndQtrYear = targetEndDateStr.substring(3, targetEndDateStr.length);

            if(getEndQtrYear < getStartQtrYear)
            {
                alert("Target End-Quarter must be greater than Target Start-Quarter");
                document.myForm.targetEndQtr.focus();
                return false;
            }

            if(getEndQtrYear == getStartQtrYear)
            {
                var getStartQtr = targetStartDateStr.substring(0,2);
                var getEndQtr = targetEndDateStr.substring(0,2);
                //alert(getStartMonth);
                //alert(getEndMonth);
                var theQuarterArray = quarterArray();
                for (key in theQuarterArray)
                {
                    if(eval(theQuarterArray[getEndQtr] + ' < '+theQuarterArray[getStartQtr]))
                    {
                        alert('Target End-Quarter must be greater than Target Start-Quarter');
                        document.myForm.targetEndQtr.focus();
                        return false;
                    }
                }
            }
        }

        else if(minTimeLevel=="Year")
        {
            if(targetEndDateStr < targetStartDateStr)
            {
                alert("Target End-Year must be greater than Target Start-Year");
                document.myForm.targetEndYear.focus();
                return false;
            }
        }


        //if(count==0)
        //{
       // parent.document.getElementById("fade").style.display = 'none';
      //  parent.document.getElementById("targetDialog").style.display = 'none';
         parent.$("#targetDialog").dialog('close');
        document.myForm.action = "pbTargetParametersDisplay.jsp";
        document.myForm.submit();
        return true;
    //}
    }
    else
    {
        return false;
    }
}
/*
function validateCopyTarget(names,sDate,eDate,minTime)
{
    var startDate;
    var endDate;
    var tsDate;
    var targetSDate;
    var teDate;
    var targetEDate;

    //alert(names)
    var namesList = names.split(",");
    var minTimeLevel = minTime;
    var targetStartDate = sDate;
    var targetEndDate = eDate;

    var tarName = document.getElementById("targetName").value;

    if(minTimeLevel == "Day")
    {
        startDate = document.getElementById("targetStartDate").value;
        endDate = document.getElementById("targetEndDate").value;
    }
    else if(minTimeLevel == "Month")
    {
        startDate = document.getElementById("targetStartMonth").value;
        endDate = document.getElementById("targetEndMonth").value;
    }
    else if(minTimeLevel == "Quarter")
    {
        startDate = document.getElementById("targetStartQtr").value;
        endDate = document.getElementById("targetEndQtr").value;
    }
    else if(minTimeLevel == "Year")
    {
        startDate = document.getElementById("targetStartYear").value;
        endDate = document.getElementById("targetEndYear").value;
    }

    if(checkTargetName() && checktargetStartDate() && checktargetEndDate() && checktargetStartMonth() && checktargetEndMonth() &&
            checktargetStartQtr() && checktargetEndQtr() && checktargetStartYear())
    {
        for(var i=0;i<namesList.length;i++)
        {
            if(tarName==namesList[i])
            {
                alert("Target name already exists");
                document.myForm.targetName.focus();
                return false;
            }
        }

        if(minTimeLevel == "Day")
        {
            tsDate = new Date(targetStartDate);
            targetSDate = new Date(startDate);
            teDate = new Date(targetEndDate);
            targetEDate = new Date(endDate);

            if((tsDate.getTime()==targetSDate.getTime()) && teDate.getTime()==targetEDate.getTime())
            {
                alert("Time range can not be same");
                document.myForm.targetStartDate.focus();
                return false;
            }
        }
        else if(minTimeLevel == "Month")
        {

            if(targetStartDate == startDate && targetEndDate == endDate)
            {
                alert("Time range can not be same");
                document.myForm.targetStartMonth.focus();
                return false;
            }
        }
        else if(minTimeLevel == "Quarter")
        {

            if(targetStartDate == startDate && targetEndDate == endDate)
            {
                alert("Time range can not be same");
                document.myForm.targetStartQtr.focus();
                return false;
            }
        }
        else if(minTimeLevel == "Year")
        {

            if(targetStartDate == startDate && targetEndDate == endDate)
            {
                alert("Time range can not be same");
                document.myForm.targetStartYear.focus();
                return false;
            }
        }

        var conf = confirm("Do you want to multiply the target values with some multiplier");
        if(conf)
        {
           return recursivePrompt2();
        }
        else
        {
            parent.document.getElementById("fade").style.display = 'none';
            parent.document.getElementById("copyTargetDisp").style.display = 'none';
            document.myForm.action="pbCopyTargetMaster.jsp";
            document.myForm.submit();
            return true;
        }
    }
    else
    {
        return false;
    }
}
*/
function validateCopyTarget(names,sDate,eDate,minTime)
{
    var startDate;
    var endDate;
    var tsDate;
    var targetSDate;
    var teDate;
    var targetEDate;

    //alert(names)
    var namesList = names.split(",");
    var minTimeLevel = minTime;
    var targetStartDate = sDate;
    var targetEndDate = eDate;

    var tarName = document.getElementById("targetName").value;

    if(minTimeLevel == "Day")
    {
        startDate = document.getElementById("targetStartDate").value;
        endDate = document.getElementById("targetEndDate").value;
    }
    else if(minTimeLevel == "Month")
    {
        startDate = document.getElementById("targetStartMonth").value;
        endDate = document.getElementById("targetEndMonth").value;
    }
    else if(minTimeLevel == "Quarter")
    {
        startDate = document.getElementById("targetStartQtr").value;
        endDate = document.getElementById("targetEndQtr").value;
    }
    else if(minTimeLevel == "Year")
    {
        startDate = document.getElementById("targetStartYear").value;
        endDate = document.getElementById("targetEndYear").value;
    }

    if(checkTargetName() && checktargetStartDate() && checktargetEndDate() && checktargetStartMonth() && checktargetEndMonth() &&
        checktargetStartQtr() && checktargetEndQtr() && checktargetStartYear())
        {
        for(var i=0;i<namesList.length;i++)
        {
            if(tarName==namesList[i])
            {
                alert("Target name already exists");
                document.myForm.targetName.focus();
                return false;
            }
        }

        if(minTimeLevel == "Day")
        {
            tsDate = new Date(targetStartDate);
            targetSDate = new Date(startDate);
            teDate = new Date(targetEndDate);
            targetEDate = new Date(endDate);

            if((tsDate.getTime()==targetSDate.getTime()) && teDate.getTime()==targetEDate.getTime())
            {
                alert("Time range can not be same");
                document.myForm.targetStartDate.focus();
                return false;
            }

            startDate = new Date(startDate);
            endDate = new Date(endDate);
            if(endDate < startDate)
            {
                alert("Target End-Date must be greater than Target Start-Date");
                document.getElementById("targetEndDate").focus();
                return false;
            }

        }
        else if(minTimeLevel == "Month")
        {

            if(targetStartDate == startDate && targetEndDate == endDate)
            {
                alert("Time range can not be same");
                document.myForm.targetStartMonth.focus();
                return false;
            }
            var getStartMonthYear = startDate.substring(4, startDate.length);
            var getEndMonthYear = endDate.substring(4, endDate.length);

            if(getEndMonthYear < getStartMonthYear)
            {
                alert("Target End-Month must be greater than Target Start-Month");
                document.myForm.targetEndMonth.focus();
                return false;
            }

            if(getEndMonthYear == getStartMonthYear)
            {
                //12-SEPT-09
                var getStartMonth = startDate.substring(0,3);
                var getEndMonth = endDate.substring(0,3);
                //alert(getStartMonth);
                //alert(getEndMonth);
                var theFormFieldArray = formFieldArray();
                for (key in theFormFieldArray)
                {
                    if(eval(theFormFieldArray[getEndMonth] + ' < '+theFormFieldArray[getStartMonth]))
                    {
                        alert('Target End-Month must be greater than Target Start-Month');
                        document.myForm.targetEndMonth.focus();
                        return false;
                    }
                }
            }
        }
        else if(minTimeLevel == "Quarter")
        {

            if(targetStartDate == startDate && targetEndDate == endDate)
            {
                alert("Time range can not be same");
                document.myForm.targetStartQtr.focus();
                return false;
            }

            var getStartQtrYear = startDate.substring(3, startDate.length);
            var getEndQtrYear = endDate.substring(3, endDate.length);

            if(getEndQtrYear < getStartQtrYear)
            {
                alert("Target End-Quarter must be greater than Target Start-Quarter");
                document.myForm.targetEndQtr.focus();
                return false;
            }

            if(getEndQtrYear == getStartQtrYear)
            {
                var getStartQtr = startDate.substring(0,2);
                var getEndQtr = endDate.substring(0,2);
                //alert(getStartMonth);
                //alert(getEndMonth);
                var theQuarterArray = quarterArray();
                for (key in theQuarterArray)
                {
                    if(eval(theQuarterArray[getEndQtr] + ' < '+theQuarterArray[getStartQtr]))
                    {
                        alert('Target End-Quarter must be greater than Target Start-Quarter');
                        document.myForm.targetEndQtr.focus();
                        return false;
                    }
                }
            }
        }
        else if(minTimeLevel == "Year")
        {

            if(targetStartDate == startDate && targetEndDate == endDate)
            {
                alert("Time range can not be same");
                document.myForm.targetStartYear.focus();
                return false;
            }

            if(endDate < startDate)
            {
                alert("Target End-Year must be greater than Target Start-Year");
                document.getElementById("targetEndYear").focus();
                return false;
            }

        }

        var conf = confirm("Do you want to multiply the target values with some multiplier");
        if(conf)
        {
            return recursivePrompt2();
        }
        else
        {
            parent.document.getElementById("fade").style.display = 'none';
            parent.document.getElementById("copyTargetDisp").style.display = 'none';
            document.myForm.action="pbCopyTargetMaster.jsp";
            document.myForm.submit();
            return true;
        }
    }
    else
    {
        return false;
    }
}
function clearAll()
{
    var minTimeLevel = "";//document.getElementById("timeLevel").value;

    var allTimeLevels = document.getElementById("timeLevels").value;
    var m=allTimeLevels.split(",");
    var max=m[0];
    if(max=="1")
        minTimeLevel="Year";
    else if(m==2)
        minTimeLevel="Qtr";
    else if(m==3)
        minTimeLevel="Month";
    else if(m==4)
        minTimeLevel="Day";

    document.getElementById("timeLevels").value="";
    document.getElementById("targetName").value = "";
    document.getElementById("targetDescription").value = "";
    document.getElementById("country1").value = "";
    document.getElementById("measure").options.length = 0;
 

    var obj = document.getElementById("measure");
    obj.options[0] = new Option("--Select--","");
    var obj2 = document.getElementById("timeLevel");
    obj2.options[0].selected = true;

    if(minTimeLevel=="Day")
    {
        document.getElementById("targetStartDate").value = "";
        document.getElementById("targetEndDate").value = "";
    }
    else if(minTimeLevel=="Month")
    {
        document.getElementById("targetStartMonth").value = "";
        document.getElementById("targetEndMonth").value = "";
    }
    else if(minTimeLevel=="Quarter"||minTimeLevel=="Qtr")
    {
        document.getElementById("targetStartQtr").value = "";
        document.getElementById("targetEndQtr").value = "";
    }
    else if(minTimeLevel=="Year")
    {
        document.getElementById("targetStartYear").value = "";
        document.getElementById("targetEndYear").value = "";
    }

    document.getElementById("parameters2").checked=false;

    document.getElementById("primaryTargetParameter").options.length = 0;
    var obj4 = document.getElementById("primaryTargetParameter");
    obj4.options[0] = new Option("--Select--","");
}
function clearCopyTarget(minTime)
{
    document.getElementById("targetName").value = "";
    document.getElementById("targetDescription").value = "";
    var minTimeLevel = minTime;
    if(minTimeLevel=="Day")
    {
        document.getElementById("targetStartDate").value = "";
        document.getElementById("targetEndDate").value = "";
    }
    else if(minTimeLevel=="Month")
    {
        document.getElementById("targetStartMonth").value = "";
        document.getElementById("targetEndMonth").value = "";
    }
    else if(minTimeLevel=="Quarter")
    {
        document.getElementById("targetStartQtr").value = "";
        document.getElementById("targetEndQtr").value = "";
    }
    else if(minTimeLevel=="Year")
    {
        document.getElementById("targetStartYear").value = "";
        document.getElementById("targetEndYear").value = "";
    }

}
function clearEditTarget(minTime)
{
   // alert("clear edit")
    document.getElementById("targetName").value = "";
    var minTimeLevel = minTime;
    if(minTimeLevel=="Day")
    {
        document.getElementById("targetStartDate").value = "";
        document.getElementById("targetEndDate").value = "";
    }
    else if(minTimeLevel=="Month")
    {
        document.getElementById("targetStartMonth").value = "";
        document.getElementById("targetEndMonth").value = "";
    }
    else if(minTimeLevel=="Quarter")
    {
        document.getElementById("targetStartQtr").value = "";
        document.getElementById("targetEndQtr").value = "";
    }
    else if(minTimeLevel=="Year")
    {
        document.getElementById("targetStartYear").value = "";
        document.getElementById("targetEndYear").value = "";
    }
    document.getElementById("targetName").focus()
}
function setWindow()
{
    if(checkTargetName() && checkBusinessAreas() && checkMeasure())
    {
        var selectedMeasure = document.getElementById("measure").value;
        var parameterIds=document.getElementById("parameterIds").value;

        window.open("pbSelectTargetParams.jsp?parameterIds="+parameterIds+"&selectedMeasure="+selectedMeasure,"TargetParameters","scrollbars=1,width=500,height=350,address=no");

        return true;
    }
    else
    {
        return false;
    }
}

function checkHierarchy()
{
    //alert("checkHierarchy")
    var i=0;
    var obj = document.myForm.chk1;
    if(isNaN(obj.length))
    {
        if(document.myForm.chk1.checked)
        {
            document.myForm.action = "pbCheckHierarchy.jsp";
            document.myForm.submit();
        }
        else
        {
            alert("Please select parameter(s)");
        }
    }
    else
    {
        for(var j=0;j<obj.length;j++)
        {
            if(document.myForm.chk1[j].checked==true)
            {
                i++;
            }
        }

        if(i==0)
        {
            alert("Please select parameter(s)");
        }
        else
        {
            document.myForm.action = "pbCheckHierarchy.jsp";
            document.myForm.submit();
        }
    }
}
/*function saveTargetParameters(pIds)
{
    var selectedParameters = pIds;
    var i=0;
    var paramIds=null;
    var obj = document.myForm.chk1;
    if(isNaN(obj.length))
    {
        if(document.myForm.chk1.checked)
        {
            paramIds = document.myForm.chk1.value;
            window.opener.document.myForm.parameterIds.value = paramIds;
            window.close();
        }
        else
        {
            alert("Please select parameter(s)")
        }
    }
    else
    {
        for(var j=0;j<obj.length;j++)
        {
            if(document.myForm.chk1[j].checked==true)
            {
                if(paramIds==null)
                {
                    paramIds = document.myForm.chk1[j].value;
                }
                else
                {
                    paramIds = paramIds+","+document.myForm.chk1[j].value;
                }

                i++;
            }
        }
        //alert(paramIds);

       if(i==0)
       {
           alert("Please select parameter(s)");
       }
       else
       {
           window.opener.document.myForm.parameterIds.value = paramIds;
           window.close();
       }
   }
}*/

function saveTargetParameters(pIds,names)
{
    var selectedParameters = pIds;
    var selectedParameterNames = names;
    window.opener.document.myForm.parameterIds.value = selectedParameters;
    window.opener.document.myForm.parameterNames.value = selectedParameterNames;
    window.close();
}
function saveTargetParameters2()
{
    document.myForm.action = "pbSaveTargetParameters.jsp";
    document.myForm.submit();
}

function checkSum()
{
    var tableObj = document.getElementById('target');
    var trObj = tableObj.getElementsByTagName("tr");
    var tdObj = trObj[0].getElementsByTagName("td");
    var rowName;
    var columnName;
    //var tds = tdObj.length;

    /*    var rowTemp = new Array();
    for(var b=0;b<trObj.length;b++)
    {
        rowTemp[b] = 0;
    }


    for(var y=1;y<trObj.length;y++)
    {
        tdObj = trObj[y].getElementsByTagName("td");
        for(var z=1;z<tdObj.length;z++)
        {
            if(z != parseInt(tdObj.length)-1)
            {
                var value = tdObj[z].getElementsByTagName("input")[0].value;
                //alert(value);
                rowTemp[y] = parseInt(rowTemp[y] + parseInt(value));
                //alert(rowTemp[y]);
            }
            else
            {
                var rowTotal = parseInt(tdObj[z].getElementsByTagName("input")[0].value);
                //alert(rowTotal);
                rowName = tdObj[z].parentNode.getElementsByTagName("td")[0].innerHTML;
                var rowSum = parseInt(rowTemp[y]);
                //alert(rowSum)
                if(rowSum != rowTotal)
                {
                    alert("Row Total at "+rowName+" not matched");
                }
            }
        }
    } */

    var columnTemp = new Array();
    for(var a=0;a<tdObj.length;a++)
    {
        columnTemp[a] = 0;
    }

    for(var i=1;i<trObj.length;i++)
    {
        if(i != parseInt(trObj.length)-1)
        {
            tdObj = trObj[i].getElementsByTagName("td");
            for(var j=1;j<tdObj.length;j++)
            {
                var val = tdObj[j].getElementsByTagName("input")[0].value;
                columnTemp[j] = parseInt(columnTemp[j] + parseInt(val));
                flag = columnTemp[j];
            }
        }
        else
        {
            tdObj = trObj[i].getElementsByTagName("td");
            for(var k=1;k<tdObj.length;k++)
            {
                var columTotal = parseInt(tdObj[k].getElementsByTagName("input")[0].value);
                columnName = trObj[0].getElementsByTagName("td")[k].innerHTML;
                var columnSum = parseInt(columnTemp[k])
                if(columnSum != columTotal)
                {
                    alert("Column Total at "+columnName+" not matched");
                }
            }
        }
    }
}
function checkParameterSection()
{
    var paramSectionObj = document.myForm.parameters;
    var paramSection;
    for(var m=0;m<paramSectionObj.length;m++)
    {
        if(document.myForm.parameters[m].checked)
        {
            paramSection = document.myForm.parameters[m].value;
        }
    }
    //alert(paramSection)
    if(paramSection=="all")
    {
        //alert("in if block")
        showPrimaryParameters();
    }
    else
    {
        //alert("in else block")
        var primParamObj = document.getElementById("primaryTargetParameter")
        primParamObj.options.length=0;
        primParamObj.options[0] = new Option("--Select--","");
        var paramNames = document.getElementById("parameterNames").value;
        var paramIds = document.getElementById("parameterIds").value;
        //alert(paramNames)
        var selectedParamNames = paramNames.split(",");
        var selectedParamIds = paramIds.split(",");
        for(var n=0;n<selectedParamNames.length;n++)
        {
            primParamObj.options[n+1] = new Option(selectedParamNames[n],selectedParamIds[n]);
        }
    }
}
function showPrimaryParameters()
{
    //alert("showPrimaryParameters")

    var str = document.getElementById("country1").value;
    if (str.length==0)
    {
        document.getElementById("txtHint").innerHTML="";
        return;
    }
    xmlHttp=GetXmlHttpObject();
    if (xmlHttp==null)
    {
        alert ("Your browser does not support AJAX!");
        return;
    }
    var url="../../getPrimaryParameters";
    url=url+"?q="+str;
    xmlHttp.onreadystatechange=stateChanged4;
    xmlHttp.open("GET",url,true);
    xmlHttp.send(null);

}
function stateChanged4()
{
    if (xmlHttp.readyState==4)
    {
        //document.getElementById("txtHint").innerHTML=xmlHttp.responseText;
        var output=xmlHttp.responseText
        var primTargetParam=output.split("\n");
        //alert("columns "+columns);

        var obj = document.myForm.primaryTargetParameter;

        for(var i=obj.length-1;i>=0;i--)
        {
            obj.options[i] = null;
        }
        obj.options[0] = new Option("--Select--","--Select--");

        for(var j=0;j<primTargetParam.length-1;j++)
        {
            var paramName = primTargetParam[j].split(",",1);
            var paramId = splitString(primTargetParam[j],",");
            //var measureId = measure[j].split(",",2);
            //alert(measureName);
            //alert(measureId);
            //obj.options[j+1] = new Option(measureName,measureId);

            //obj.options[j].selected=true;

            //alert(durationVal);
            addOption(document.getElementById('primaryTargetParameter'),paramName,paramId);
        }

    //alert(output);
    }

}

function showParameters(primDim)
{
    var str = primDim;
    if (str.length==0)
    {
        document.getElementById("txtHint").innerHTML="";
        return;
    }
    xmlHttp=GetXmlHttpObject();
    if (xmlHttp==null)
    {
        alert ("Your browser does not support AJAX!");
        return;
    }
    var url="../../getPrimaryDimensionParameters";
    url=url+"?q="+str;
    xmlHttp.onreadystatechange=stateChanged2;
    xmlHttp.open("GET",url,true);
    xmlHttp.send(null);

}
function stateChanged2()
{

    if (xmlHttp.readyState==4)
    {
        //document.getElementById("txtHint").innerHTML=xmlHttp.responseText;
        var output=xmlHttp.responseText
        var primTargetParam=output.split("\n");
        //alert("columns "+columns);

        var obj = document.myForm.primaryTargetParameter;

        for(var i=obj.length-1;i>=0;i--)
        {
            obj.options[i] = null;
        }
        obj.options[0] = new Option("--Select--","--Select--");

        for(var j=0;j<primTargetParam.length-1;j++)
        {
            var paramName = primTargetParam[j].split(",",1);
            var paramId = splitString(primTargetParam[j],",");
            //var measureId = measure[j].split(",",2);
            //alert(measureName);
            //alert(measureId);
            //obj.options[j+1] = new Option(measureName,measureId);

            //obj.options[j].selected=true;

            //alert(durationVal);
            addOption(document.getElementById('primaryTargetParameter'),paramName,paramId);
        }

    //alert(output);
    }

}
function showSecondaryParameters()
{

    var paramSectionObj = document.myForm.parameters;
    var selectedPrimParam = document.getElementById("primaryTargetParameter").value;
    var paramSection;
    for(var m=0;m<paramSectionObj.length;m++)
    {
        if(document.myForm.parameters[m].checked)
        {
            paramSection = document.myForm.parameters[m].value;
        }
    }

    document.getElementById("secondaryTargetParameter").options.length = 0;
    var secParamObj = document.getElementById("secondaryTargetParameter");
    secParamObj.options[0] = new Option("--Select--","--Select--");

    showSecondaryParameters2(selectedPrimParam,paramSection);
}

function showSecondaryParameters2(primParameter1,paramSec)
{

    var paramIds = document.getElementById("parameterIds").value;
    var str = document.getElementById("country1").value;
    var str1 = primParameter1;
    var str2 = paramSec;
    if (str1.length==0)
    {
        document.getElementById("txtHint").innerHTML="";
        return;
    }
    xmlHttp=GetXmlHttpObject();
    if (xmlHttp==null)
    {
        alert ("Your browser does not support AJAX!");
        return;
    }
    var url="../../getSecondaryParameters";
    url=url+"?q1="+str1+"&p1="+str+"&r1="+str2+"&s1="+paramIds;
    xmlHttp.onreadystatechange=stateChanged3;
    xmlHttp.open("GET",url,true);
    xmlHttp.send(null);
}
function stateChanged3()
{
    if (xmlHttp.readyState==4)
    {
        //document.getElementById("txtHint").innerHTML=xmlHttp.responseText;
        var output=xmlHttp.responseText

        var secondaryParams=output.split("\n");
        //alert("columns "+columns);

        var obj1 = document.myForm.secondaryTargetParameter;

        for(var i=obj1.length-1;i>=0;i--)
        {
            obj1.options[i] = null;
        }
        obj1.options[0] = new Option("--Select--","--Select--");

        for(var j=0;j<secondaryParams.length-1;j++)
        {
            var paramName1 = secondaryParams[j].split(",",1);
            var paramId1 = splitString(secondaryParams[j],",");
            //var measureId = measure[j].split(",",2);
            //alert(measureName);
            //alert(measureId);
            //obj.options[j+1] = new Option(measureName,measureId);

            //obj.options[j].selected=true;

            //alert(durationVal);

            addOption(document.getElementById('secondaryTargetParameter'),paramName1,paramId1);
        }
    }
}


/*function showSecondaryDimensions(primDim1)
{
    var str1 = primDim1;
    if (str1.length==0)
    {
    document.getElementById("txtHint").innerHTML="";
    return;
    }
    xmlHttp=GetXmlHttpObject();
    if (xmlHttp==null)
    {
    alert ("Your browser does not support AJAX!");
    return;
    }
    var url="../../getSecondaryDimensions";
    url=url+"?q1="+str1;
    xmlHttp.onreadystatechange=stateChanged3;
    xmlHttp.open("GET",url,true);
    xmlHttp.send(null);
}
function stateChanged3()
{
    if (xmlHttp.readyState==4)
    {
    //document.getElementById("txtHint").innerHTML=xmlHttp.responseText;
    var output=xmlHttp.responseText
    var secondaryDim=output.split("\n");
    //alert("columns "+columns);

    var obj1 = document.myForm.secondaryDimension;

    for(var i=obj1.length-1;i>=0;i--)
    {
    obj1.options[i] = null;
    }
    obj1.options[0] = new Option("--Select--","--Select--");

    for(var j=0;j<secondaryDim.length-1;j++)
    {
    var paramName1 = secondaryDim[j].split(",",1);
    var paramId1 = splitString(secondaryDim[j],",");
    //var measureId = measure[j].split(",",2);
    //alert(measureName);
    //alert(measureId);
    //obj.options[j+1] = new Option(measureName,measureId);

    //obj.options[j].selected=true;

    //alert(durationVal);
    addOption(document.getElementById('secondaryDimension'),paramName1,paramId1);
    }

    //alert(output);
    }
}*/
function showSecondary()
{
    var targetObj = document.myForm.targetType;
    //alert(targetObj.length);
    if(targetObj[1].checked)
    {
        //document.getElementById("secondary1").style.display = '';
        document.getElementById("secondary2").style.display = '';
    }
    else
    {
        //document.getElementById("secondary1").style.display = 'none';
        document.getElementById("secondary2").style.display = 'none';
    }
}
function CheckAll(chk)
{
    if(document.myForm.checkCtr.checked==true)
    {
        if(isNaN(chk.length))
        {
            chk.checked=true;
        }
        else
        {
            for (i = 0; i < chk.length; i++)
                chk[i].checked = true ;
        }
    }
    else
    {
        if(isNaN(chk.length))
        {
            chk.checked=false;
        }
        else
        {
            for (i = 0; i < chk.length; i++)
                chk[i].checked = false ;
        }
    }
}
function CheckTop2(ctr)
{
    var all = document.myForm.chk1.length;
    var m=0;
    var n=0;
    for(j=0;j<all;j++)
    {
        if(document.myForm.chk1[j].checked==false)
        {
            m++;
        }
        if(document.myForm.chk1[j].checked==true)
        {
            n++
        }
    }
    if(m>=1)
    {
        ctr.checked=false;
    }
    if(n==all)
    {
        ctr.checked=true;
    }
}
function copyToDates()
{
    window.open("pbCopyTarget.jsp","CopyTarget","status=1,width=400,height=250");
}
function saveFromToDates()
{
    var pw = window.opener;
    var fromDate = document.getElementById("fromDate").value;
    alert(fromDate)
    var toDate = document.getElementById("toDate").value;
    alert(toDate)
    pw.document.myForm.fromDate.value = fromDate;
    pw.document.myForm.toDate.value = toDate;
    window.close();
}
/*
function createTarget()
{
    document.forms.ec.action = "pbTargetMaster.jsp";
    document.forms.ec.submit();
}
*/

function closeEditTarget()
{
     $("#editTarget").dialog('close');
}
function editTarget()
{
   // alert('her -')
    var path=document.getElementById("path").value;
    var chkId;
    var tdObj;
    var trObj;
    var name;
    var status;
    var flag = false;
    var i=0;
    var obj = document.forms.ec.chk1;
    if(isNaN(obj.length))
    {
        if(document.forms.ec.chk1.checked)
        {
            chkId = document.forms.ec.chk1.value;
            tdObj = document.forms.ec.chk1.parentNode;
            trObj = tdObj.parentNode;
            name = trObj.getElementsByTagName("td")[1].getElementsByTagName("a")[0].innerHTML;
            status = trObj.getElementsByTagName("td")[4].innerHTML;

            if(status=="Published")
            {
                alert("Published target is not editable");
            }
            else
            {
                //alert('kdskk ');
                var frameObj = document.getElementById("editTargetDisp");
                frameObj.src=path+"/QTarget/JSPs/pbCheckActiveAlerts.jsp?chk1="+chkId;

            }
        }
        else
        {
            alert('Please select Target');
        }
    }
    else
    {
        for(var j=0;j<obj.length;j++)
        {
            if(document.forms.ec.chk1[j].checked==true)
            {
                chkId = document.forms.ec.chk1[j].value;
                tdObj = document.forms.ec.chk1[j].parentNode;
                trObj = tdObj.parentNode;
                name = trObj.getElementsByTagName("td")[1].getElementsByTagName("a")[0].innerHTML;
                status = trObj.getElementsByTagName("td")[4].innerHTML;

                if(status=="Published")
                {
                    flag = true;
                }

                i++;
            // alert(document.forms.ec.chk2[j].value)

            }
        }

        if(i>1)
        {
            alert('Please select only one Target');
        }
        else if(i==0)
        {
            alert('Please select Target');
        }
        else
        {
            if(flag==true)
            {
                alert("Published target is not editable");
            }
            else
            {
                //alert('kdskk ');
                frameObj = document.getElementById("editTargetDisp");
                frameObj.src=path+"/QTarget/JSPs/pbCheckActiveAlerts.jsp?chk1="+chkId;

            }

        }
    }
}

function goToTargetsHome()
{
    var confm = confirm("Any unsaved data will be lost");
    if(confm==true)
    {
        document.myForm.action = "pbTargetList.jsp";
        document.myForm.submit();
    }
}
function goBack()
{
    parent.cancelTarget();

}
function goBack2()
{
    parent.cancelEditTarget();
}
function goBack3()
{
    parent.cancelCopyTarget();
}
function goToTargetsHome2()
{
    document.myForm.action = "pbTargetList.jsp";
    document.myForm.submit();
}

/*function deleteTarget()
{
    var i=0;
    var obj = document.forms.ec.chk1;
     if(isNaN(obj.length))
      {
         if(document.forms.ec.chk1.checked)
          {
               var x=confirm('Are you sure you want to Delete Target');
               if(x==true)
               {
               document.forms.ec.action="pbDeleteTarget.jsp";
               document.forms.ec.submit();
               }
          }
          else
           {
            alert('Please select Target(s)')
           }
       }
    else
    {
       for(var j=0;j<obj.length;j++)
        {
          if(document.forms.ec.chk1[j].checked==true)
            {
             i++;
             //alert(document.forms.ec.chk2[j].value)
            }
         }
         if(i==0)
         {

          alert('Please select Target(s)')
         }
         else
         {
           var y=confirm('Are you sure you want to Delete Target(s)');
           if(y==true)
           {
             document.forms.ec.action="pbDeleteTarget.jsp";
             document.forms.ec.submit();
           }
         }
    }
}
*/
function deleteTarget()
{
    var path=document.getElementById("path").value;
    var tdObj;
    var trObj;
    var name;
    var status;
    var content = "";
    var i=0;
    var flag = false;
    var obj = document.forms.ec.chk1;
    if(isNaN(obj.length))
    {
        if(document.forms.ec.chk1.checked)
        {
            tdObj = document.forms.ec.chk1.parentNode;
            trObj = tdObj.parentNode;
            name = trObj.getElementsByTagName("td")[1].getElementsByTagName("a")[0].innerHTML;
            status = trObj.getElementsByTagName("td")[4].innerHTML;

            if(status=="Published")
            {
                alert("Published target can not be deleted");
            }
            var x=confirm("Are you sure you want to Delete '"+name+"' Target");
            if(x==true)
            {
                document.forms.ec.action=path+"/QTarget/JSPs/pbDeleteTarget.jsp";
                document.forms.ec.submit();
            }
        }
        else
        {
            alert('Please select Target(s)')
        }
    }
    else
    {
        for(var j=0;j<obj.length;j++)
        {
            if(document.forms.ec.chk1[j].checked==true)
            {
                tdObj = document.forms.ec.chk1[j].parentNode;
                trObj = tdObj.parentNode;
                name = trObj.getElementsByTagName("td")[1].getElementsByTagName("a")[0].innerHTML;
                status = trObj.getElementsByTagName("td")[4].innerHTML;

                if(status=="Published")
                {
                    flag = true;
                }
                if(content=="")
                {
                    content = name;
                }
                else
                {
                    content = content+","+name;
                }

                i++;
            //alert(document.forms.ec.chk2[j].value)
            }
        }
        if(i==0)
        {

            alert('Please select Target(s)')
        }
        else if(flag==true)
        {
            alert("Published target can not be deleted");
        }
        else
        {
            var y=confirm("Are you sure you want to Delete '"+content+"' Target(s)");
            if(y==true)
            {
                document.forms.ec.action=path+"/QTarget/JSPs/pbDeleteTarget.jsp";
                document.forms.ec.submit();
            }
        }
    }
}


function defineTarget(path)
{
    //alert('-=-=-path --- '+path)
    var name;
    var status;
    var flag;
    var flag2;
    var tdObj;
    var trObj;
    var timeLevel;
    var selectedTargetId;
    var i=0;
    var obj = document.forms.ec.chk1;
    if(isNaN(obj.length))
    {
        if(document.forms.ec.chk1.checked)
        {
            selectedTargetId = document.forms.ec.chk1.value;
            tdObj = document.forms.ec.chk1.parentNode;
            trObj = tdObj.parentNode;
            timeLevel = trObj.getElementsByTagName("td")[5].innerHTML;
            name = trObj.getElementsByTagName("td")[1].getElementsByTagName("a")[0].innerHTML;
            status = trObj.getElementsByTagName("td")[4].innerHTML;
            if(status=="Published")
            {
                alert("Published target is not editable");
            }
            else if(status=="Expired")
            {
                alert("Expired target is not editable");
            }
            else
            {
                //alert(path+"/targetView.do?targetParams=viewTarget&targetId="+selectedTargetId+"&minTimeLevel="+timeLevel)
                //document.forms.ec.action=path+"/targetView.do?targetParams=viewTarget&targetId="+selectedTargetId+"&minTimeLevel="+timeLevel;
                //document.forms.ec.submit();
                //alert('path '+path+"/QTarget/JSPs/pbCheckLock.jsp?targetParams=viewTarget&targetId="+selectedTargetId+"&minTimeLevel="+timeLevel)
                document.forms.ec.action=path+"/QTarget/JSPs/pbCheckLock.jsp?targetParams=viewTarget&targetId="+selectedTargetId+"&minTimeLevel="+timeLevel;
                document.forms.ec.submit();
            }
        }
        else
        {
            alert('Please select Target');
        }
    }
    else
    {
        for(var j=0;j<obj.length;j++)
        {
            if(document.forms.ec.chk1[j].checked==true)
            {
                selectedTargetId = document.forms.ec.chk1[j].value;
                tdObj = document.forms.ec.chk1[j].parentNode;
                trObj = tdObj.parentNode;
                timeLevel = trObj.getElementsByTagName("td")[5].innerHTML;
                name = trObj.getElementsByTagName("td")[1].getElementsByTagName("a")[0].innerHTML;
                status = trObj.getElementsByTagName("td")[4].innerHTML;
                if(status=="Published")
                {
                    flag = true;
                }
                if(status=="Expired")
                {
                    flag2 = true;
                }
                i++;
            // alert(document.forms.ec.chk2[j].value)

            }
        }

        if(i>1)
        {
            alert('Please select only one Target');
        }
        else if(i==0)
        {
            alert('Please select Target');
        }
        else if(flag==true)
        {
            alert("Published target is not editable");
        }
        else if(flag2==true)
        {
            alert("Expired target is not editable");
        }
        else
        {
            //document.forms.ec.action=path+"/targetView.do?targetParams=viewTarget&targetId="+selectedTargetId+"&minTimeLevel="+timeLevel;
            //document.forms.ec.submit();
            //alert('path '+path+"/QTarget/JSPs/pbCheckLock.jsp?targetParams=viewTarget&targetId="+selectedTargetId+"&minTimeLevel="+timeLevel)
            document.forms.ec.action=path+"/QTarget/JSPs/pbCheckLock.jsp?targetParams=viewTarget&targetId="+selectedTargetId+"&minTimeLevel="+timeLevel;
            document.forms.ec.submit();
        }
    }
}
function goToUserHome()
{
    document.forms.ec.action = "/AdminProject/UserTable/JSPS/UserHome.jsp";
    document.forms.ec.submit();
}
var period;
var name;
function copyData()
{
    //alert("copy");
    period = document.frmParameter.CBOPRG_PERIOD_TYPE.value;
    var tableObj = document.getElementById("target");
    var trObj = tableObj.getElementsByTagName("tr");
    var tdObj = trObj[0].getElementsByTagName("td");
    name = tdObj[1].innerHTML;
    var conf = confirm("Do you want to copy the data of "+name+" to other "+period+"(s)");
    if(conf==true)
    {
        recursivePrompt();
    }

}
function recursivePrompt()
{
    var number = prompt("Enter number of consecutive "+period+"(s)",0);
    //alert(number)
    if(number!=null)
    {
        number = parseInt(number);
        //alert(number)
        if(isNaN(number))
        {
            alert("Please enter numbers only");
            recursivePrompt();
        }
        else
        {
            //alert(number)
            var makeSure = confirm("Do you want to proceed");
            if(makeSure==true)
            {
                //alert(number);
                document.frmParameter.copyToOthers.value = number+" "+period+" "+name;
                // document.frmParameter.action = "pbSaveTarget.jsp";
                document.frmParameter.action = "pbCopyData.jsp";
                document.frmParameter.submit();
            }
        }
    }
}
/*function finalConfirmation()
{
    document.myForm.action = "pbSaveCopyFinal.jsp";
    document.myForm.submit();
}*/
/*
function updateTarget(minTime)
{
    var minTimeLevel = minTime;
    var targetStartDateStr;
    var targetEndDateStr;

    if(checkTargetName() && checktargetStartDate() && checktargetEndDate() && checktargetStartMonth() && checktargetEndMonth() &&
            checktargetStartQtr() && checktargetEndQtr() && checktargetStartYear())
    {
        if(minTimeLevel=="Day")
        {
            targetStartDateStr = document.getElementById("targetStartDate").value;
            targetEndDateStr = document.getElementById("targetEndDate").value;

            targetStartDateStr = new Date(targetStartDateStr);
            targetEndDateStr = new Date(targetEndDateStr);

            if(targetEndDateStr < targetStartDateStr)
            {
                alert("Target End-Date must be greater than Target Start-Date");
                document.getElementById("targetEndDate").focus();
                return false;
            }
        }

        parent.document.getElementById("fade").style.display = 'none';
        parent.document.getElementById("editTargetDisp").style.display = 'none';
        document.myForm.action = "pbUpdateTarget.jsp";
        document.myForm.submit();
        return true;
    }
    else
    {
        return false;
    }
}
*/
function updateTarget(minTime)
{
    var minTimeLevel = minTime;
    var targetStartDateStr;
    var targetEndDateStr;

    if(checkTargetName() && checktargetStartDate() && checktargetEndDate() && checktargetStartMonth() && checktargetEndMonth() &&
        checktargetStartQtr() && checktargetEndQtr() && checktargetStartYear() && checktargetEndYear())
        {
        if(minTimeLevel=="Day")
        {
            targetStartDateStr = document.getElementById("targetStartDate").value;
            targetEndDateStr = document.getElementById("targetEndDate").value;

            targetStartDateStr = new Date(targetStartDateStr);
            targetEndDateStr = new Date(targetEndDateStr);

            if(targetEndDateStr < targetStartDateStr)
            {
                alert("Target End-Date must be greater than Target Start-Date");
                document.getElementById("targetEndDate").focus();
                return false;
            }
        }
        else if(minTimeLevel=="Month")
        {
            targetStartDateStr = document.getElementById("targetStartMonth").value;
            targetEndDateStr = document.getElementById("targetEndMonth").value;

            var getStartMonthYear = targetStartDateStr.substring(4, targetStartDateStr.length);
            var getEndMonthYear = targetEndDateStr.substring(4, targetEndDateStr.length);

            if(getEndMonthYear < getStartMonthYear)
            {
                alert("Target End-Month must be greater than Target Start-Month");
                document.myForm.targetEndMonth.focus();
                return false;
            }

            if(getEndMonthYear == getStartMonthYear)
            {
                //12-SEPT-09
                var getStartMonth = targetStartDateStr.substring(0,3);
                var getEndMonth = targetEndDateStr.substring(0,3);
                //alert(getStartMonth);
                //alert(getEndMonth);
                var theFormFieldArray = formFieldArray();
                for (key in theFormFieldArray)
                {
                    if(eval(theFormFieldArray[getEndMonth] + ' < '+theFormFieldArray[getStartMonth]))
                    {
                        alert('Target End-Month must be greater than Target Start-Month');
                        document.myForm.targetEndMonth.focus();
                        return false;
                    }
                }
            }
        }
        else if(minTimeLevel=="Quarter")
        {
            targetStartDateStr = document.getElementById("targetStartQtr").value;
            targetEndDateStr = document.getElementById("targetEndQtr").value;

            var getStartQtrYear = targetStartDateStr.substring(3, targetStartDateStr.length);
            var getEndQtrYear = targetEndDateStr.substring(3, targetEndDateStr.length);

            if(getEndQtrYear < getStartQtrYear)
            {
                alert("Target End-Quarter must be greater than Target Start-Quarter");
                document.myForm.targetEndQtr.focus();
                return false;
            }

            if(getEndQtrYear == getStartQtrYear)
            {
                var getStartQtr = targetStartDateStr.substring(0,2);
                var getEndQtr = targetEndDateStr.substring(0,2);
                //alert(getStartMonth);
                //alert(getEndMonth);
                var theQuarterArray = quarterArray();
                for (key in theQuarterArray)
                {
                    if(eval(theQuarterArray[getEndQtr] + ' < '+theQuarterArray[getStartQtr]))
                    {
                        alert('Target End-Quarter must be greater than Target Start-Quarter');
                        document.myForm.targetEndQtr.focus();
                        return false;
                    }
                }
            }
        }

        else if(minTimeLevel=="Year")
        {
            targetStartDateStr = document.getElementById("targetStartYear").value;
            targetEndDateStr = document.getElementById("targetEndYear").value;

            if(targetEndDateStr < targetStartDateStr)
            {
                alert("Target End-Year must be greater than Target Start-Year");
                document.getElementById("targetEndYear").focus();
                return false;
            }
        }
        var path=document.getElementById("path").value;
        parent.closeEditTarget();
        document.myForm.action = path+"/QTarget/JSPs/pbUpdateTarget.jsp";
        document.myForm.submit();
        return true;
    }
    else
    {
        return false;
    }
}
/*function copyTarget()
{
    var i=0;
       var obj = document.forms.ec.chk1;
       if(isNaN(obj.length))
        {
          if(document.forms.ec.chk1.checked)
           {
                document.forms.ec.action="pbCopyTarget.jsp";
                document.forms.ec.submit();
           }
          else
           {
            alert('Please select Target');
           }
        }
      else
          {
            for(var j=0;j<obj.length;j++)
           {
            if(document.forms.ec.chk1[j].checked==true)
            {
             i++;
            // alert(document.forms.ec.chk2[j].value)

            }
           }

          if(i>1)
           {
            alert('Please select only one Target');
           }
          else if(i==0)
           {
            alert('Please select Target');
           }
          else
           {
                document.forms.ec.action="pbCopyTarget.jsp";
                document.forms.ec.submit();
           }
       }
}
*/
function copyTarget()
{
    var chkId;
    var i=0;
    var obj = document.forms.ec.chk1;
    if(isNaN(obj.length))
    {
        if(document.forms.ec.chk1.checked)
        {
            chkId = document.forms.ec.chk1.value;
            document.getElementById("fade").style.display = 'block';
            document.getElementById("copyTargetDisp").style.display = 'block';
            document.getElementById("copyTargetDisp").src = "pbCopyTarget.jsp?chk1="+chkId;
        //document.forms.ec.action="pbCopyTarget.jsp";
        //document.forms.ec.submit();
        }
        else
        {
            alert('Please select Target');
        }
    }
    else
    {
        for(var j=0;j<obj.length;j++)
        {
            if(document.forms.ec.chk1[j].checked==true)
            {
                chkId = document.forms.ec.chk1[j].value;

                i++;
            // alert(document.forms.ec.chk2[j].value)

            }
        }

        if(i>1)
        {
            alert('Please select only one Target');
        }
        else if(i==0)
        {
            alert('Please select Target');
        }
        else
        {

            document.getElementById("fade").style.display = 'block';
            document.getElementById("copyTargetDisp").style.display = 'block';
            document.getElementById("copyTargetDisp").src = "pbCopyTarget.jsp?chk1="+chkId;
        //document.forms.ec.action="pbCopyTarget.jsp";
        //document.forms.ec.submit();
        }
    }
}


/*function copyTarget()
{
    //alert("in::::")
    var conf;
    var i=0;
       var obj = document.forms.ec.chk1;
       if(isNaN(obj.length))
        {
          if(document.forms.ec.chk1.checked)
           {
               conf = confirm("Do you want to multiply the target values with some multiplier");
               if(conf)
               {
                   recursivePrompt2();
               }
               else
               {
                    document.forms.ec.action="pbCopyTarget.jsp";
                    document.forms.ec.submit();
               }
           }
          else
           {
            alert('Please select Target');
           }
        }
      else
          {
            for(var j=0;j<obj.length;j++)
           {
            if(document.forms.ec.chk1[j].checked==true)
            {

             i++;
            // alert(document.forms.ec.chk2[j].value)

            }
           }

          if(i>1)
           {
            alert('Please select only one Target');
           }
          else if(i==0)
           {
            alert('Please select Target');
           }
          else
           {
               conf = confirm("Do you want to multiply the target values with some multiplier");
                if(conf)
                {
                   recursivePrompt2();
                }
                else
                {
                    document.forms.ec.action="pbCopyTarget.jsp";
                    document.forms.ec.submit();
                }
           }
       }
}*/

//01-aug-09
function checkMeasureName()
{
    var measureName = document.getElementById("measureName").value;
    if(measureName=="" || measureName==null)
    {
        alert("Please select Measure Name");
        document.myForm.measureName.focus();
        return false;
    }
    else
    {
        return true;
    }
}
function checkOriginalTable()
{
    var originalTable = document.getElementById("originalTable").value;
    if(originalTable=="" || originalTable==null || originalTable=="--Select--")
    {
        alert("Please select Original Table");
        document.myForm.originalTable.focus();
        return false;
    }
    else
    {
        return true;
    }
}
function checkTargetTable()
{
    var targetTable = document.getElementById("targetTable").value;
    if(targetTable=="" || targetTable==null || targetTable=="--Select--")
    {
        alert("Please select Target Table");
        document.myForm.targetTable.focus();
        return false;
    }
    else
    {
        return true;
    }
}
function checkBusinessName()
{
    var businessArea = document.getElementById("businessArea").value;
    if(businessArea=="" || businessArea==null || businessArea=="--Select--")
    {
        alert("Please select Business Area");
        document.myForm.businessArea.focus();
        return false;
    }
    else
    {
        return true;
    }
}
function validateCreateMeasure()
{
    if(checkMeasureName() && checkOriginalTable() && checkTargetTable() && checkBusinessName())
    {
        document.myForm.action = "pbSaveMeasure.jsp";
        document.myForm.submit();
        return true;
    }
    else
    {
        return false;
    }
}
function clearCreateMeasure()
{
    document.getElementById("measureName").value = "";
    document.getElementById("originalTable").options[0].selected = true;
    document.getElementById("targetTable").options[0].selected = true;
    document.getElementById("businessArea").options[0].selected = true;
}
function goToMeasuresHome()
{
    document.myForm.action = "pbMeasureList.jsp";
    document.myForm.submit();
}
function createMeasure()
{
    document.forms.ec.action = "pbCreateMeasure.jsp";
    document.forms.ec.submit();
}
function editMeasure()
{
    var i=0;
    var obj = document.forms.ec.chk1;
    if(isNaN(obj.length))
    {
        if(document.forms.ec.chk1.checked)
        {
            document.forms.ec.action="pbEditMeasure.jsp";
            document.forms.ec.submit();
        }
        else
        {
            alert('Please select Measure');
        }
    }
    else
    {
        for(var j=0;j<obj.length;j++)
        {
            if(document.forms.ec.chk1[j].checked==true)
            {
                i++;
            // alert(document.forms.ec.chk2[j].value)

            }
        }

        if(i>1)
        {
            alert('Please select only one Measure');
        }
        else if(i==0)
        {
            alert('Please select Measure');
        }
        else
        {
            document.forms.ec.action="pbEditMeasure.jsp";
            document.forms.ec.submit();
        }
    }
}
function deleteMeasure()
{
    var tdObj;
    var trObj;
    var name;
    var content = "";
    var i=0;
    var obj = document.forms.ec.chk1;
    if(isNaN(obj.length))
    {
        if(document.forms.ec.chk1.checked)
        {
            tdObj = document.forms.ec.chk1.parentNode;
            trObj = tdObj.parentNode;
            name = trObj.getElementsByTagName("td")[1].innerHTML;

            var x=confirm("Are you sure you want to Delete '"+name+"' Measure");
            if(x==true)
            {
                document.forms.ec.action="pbDeleteMeasure.jsp";
                document.forms.ec.submit();
            }
        }
        else
        {
            alert('Please select Measure(s)')
        }
    }
    else
    {
        for(var j=0;j<obj.length;j++)
        {
            if(document.forms.ec.chk1[j].checked==true)
            {
                tdObj = document.forms.ec.chk1[j].parentNode;
                trObj = tdObj.parentNode;
                name = trObj.getElementsByTagName("td")[1].innerHTML;
                if(content=="")
                {
                    content = name;
                }
                else
                {
                    content = content+","+name;
                }
                i++;
            }
        }
        if(i==0)
        {

            alert('Please select Measure(s)')
        }
        else
        {
            var y=confirm("Are you sure you want to Delete '"+content+"' Measure(s)");
            if(y==true)
            {
                document.forms.ec.action="pbDeleteMeasure.jsp";
                document.forms.ec.submit();
            }
        }
    }
}
function updateMeasure()
{
    if(checkMeasureName() && checkOriginalTable() && checkTargetTable() && checkBusinessName())
    {
        document.myForm.action = "pbUpdateMeasure.jsp";
        document.myForm.submit();
        return true;
    }
    else
    {
        return false;
    }
}
function recursivePrompt2()
{
    var val = prompt("Enter multiplier","1");
    if(isNaN(val))
    {
        alert("Please enter numbers only");
        recursivePrompt2();
    }
    else if(val==0)
    {
        alert("Enter number other than 0");
        recursivePrompt2();
    }
    else
    {
        //alert(val);
        //document.forms.myForm2.multiplierVal.value = val;
        document.myForm.action = "pbCopyTargetMaster.jsp?val="+val;
        document.myForm.submit();
        return true;
    }
}


/*
function autoSum(boxName)
{
    

    //added on 6th-Jan-2010
    var tdObj = document.getElementById(boxName).parentNode;
    var trObj = tdObj.parentNode;
    var tableObj = trObj.parentNode;
    var tdObjs = trObj.getElementsByTagName("td");
    var tdNum = tdObjs.length;
    var nextTdObj;
    var content;
    var primViewBy;
    var secViewBy;
    var terViewBy;
    var restrictingTotal;
    var enteredPercentage;
    var enteredAbsolute;
    var eachValue;
    var rowTotal=0;
    var nameMatch;
    var indexNum=0;
    var parTotal=0;
    var maxTimeLevel = document.getElementById("maxTimeLevel").value;
    var selPeriodType = document.frmParameter.CBO_PRG_PERIOD.value;
    if(maxTimeLevel!=selPeriodType) {

        var selIndex = document.getElementById("primaryViewBy").selectedIndex;
        if(selIndex==0)
        {
            primViewBy = document.getElementById("primaryViewBy").getElementsByTagName("option")[0].innerHTML;
        }
        secViewBy = document.getElementById("secondaryViewBy").value;
        terViewBy = document.getElementById("tertiaryViewBy").value;
        if(primViewBy=="Overall target" && terViewBy=="Percentage")
        {
            restrictingTotal = tdObjs[tdNum - 2].getElementsByTagName("input")[0].value;
            enteredPercentage = tdObj.getElementsByTagName("input")[0].value;
            content = (enteredPercentage / 100) * restrictingTotal;
            nextTdObj = tdObj.nextSibling;
            for(var j=1;j<tdNum-3;j++)
            {
                if((j%2)!=0)
                {
                    eachValue = tdObjs[j].getElementsByTagName("input")[0].value;
                    if(eachValue=="")
                    {
                        eachValue = parseInt(0);
                    }
                    rowTotal = parseInt(rowTotal) + parseInt(eachValue);
                }
            }
            if(rowTotal <= 100)
            {
                document.getElementById("previousValue").value = enteredPercentage;
                nextTdObj.getElementsByTagName("input")[0].value = content;
                tdObjs[tdNum - 3].getElementsByTagName("input")[0].value = rowTotal;
                tdObjs[tdNum - 1].getElementsByTagName("input")[0].value = 100 - rowTotal;
                return true;
            }
            else
            {
                for(var k=1;k<tdNum-3;k++) {
                    if((k%2)!=0)
                    {
                        nameMatch = tdObjs[k].getElementsByTagName("input")[0].getAttribute("id");
                        if(boxName==nameMatch)
                        {
                            indexNum = k;
                        }
                    }
                }
                for(var a=1;a<tdNum-3;a++) {
                    if((a%2)!=0 && a!=indexNum)
                    {
                        eachValue = tdObjs[a].getElementsByTagName("input")[0].value;
                        if(eachValue=="")
                        {
                            eachValue = parseInt(0);
                        }
                        parTotal = parseInt(parTotal) + parseInt(eachValue);
                    }
                }

                //tdObj.getElementsByTagName("input")[0].value = restrictingTotal - parTotal;


                tdObj.getElementsByTagName("input")[0].value = document.getElementById("previousValue").value;


                alert("You can not exceed Restricting Total.");
                return false;
            }

        }
        if(primViewBy=="Overall target" && terViewBy=="Absolute")
        {
            restrictingTotal = tdObjs[tdNum - 2].getElementsByTagName("input")[0].value;
            enteredAbsolute = tdObj.getElementsByTagName("input")[0].value;
            content = (enteredAbsolute / restrictingTotal) * 100;
            nextTdObj = tdObj.nextSibling;
            for(var j=1;j<tdNum-3;j++)
            {
                if((j%2)!=0)
                {
                    eachValue = tdObjs[j].getElementsByTagName("input")[0].value;
                    if(eachValue=="")
                    {
                        eachValue = parseInt(0);
                    }
                    rowTotal = parseInt(rowTotal) + parseInt(eachValue);
                }
            }
            if(rowTotal <= restrictingTotal)
            {
                document.getElementById("previousValue").value = enteredAbsolute;
                nextTdObj.getElementsByTagName("input")[0].value = content;
                tdObjs[tdNum - 3].getElementsByTagName("input")[0].value = rowTotal;
                tdObjs[tdNum - 1].getElementsByTagName("input")[0].value = restrictingTotal - rowTotal;
                return true;
            }
            else
            {
                for(var k=1;k<tdNum-3;k++) {
                    if((k%2)!=0)
                    {
                        nameMatch = tdObjs[k].getElementsByTagName("input")[0].getAttribute("id");
                        if(boxName==nameMatch)
                        {
                            indexNum = k;
                        }
                    }
                }
                for(var a=1;a<tdNum-3;a++) {
                    if((a%2)!=0 && a!=indexNum)
                    {
                        eachValue = tdObjs[a].getElementsByTagName("input")[0].value;
                        if(eachValue=="")
                        {
                            eachValue = parseInt(0);
                        }
                        parTotal = parseInt(parTotal) + parseInt(eachValue);
                    }
                }

                //tdObj.getElementsByTagName("input")[0].value = restrictingTotal - parTotal;


                tdObj.getElementsByTagName("input")[0].value = document.getElementById("previousValue").value;


                alert("You can not exceed Restricting Total.");
                return false;
            }
        }
    }
    else {

        var rowTotal = 0;
        var columnTotal = 0;
        var name = boxName;
        var indexNum = 0;
        var lastIndexNum = 0;
        var tdObj = document.getElementById(boxName).parentNode;
        var trObj = tdObj.parentNode;
        var tableObj = trObj.parentNode;
        var tdObjs = trObj.getElementsByTagName("td");
        var tdNum = trObj.getElementsByTagName("td").length;
        var val = document.getElementById(boxName).value;
        for(var m=1;m<tdNum;m++)
        {
            if(m<tdNum-1)
            {
                var content = tdObjs[m].getElementsByTagName("input")[0].value;
                if(content=="")
                {
                    content = parseInt(0);
                }
                content = parseInt(content);
                rowTotal = rowTotal + content;
            }
            else
            {
                tdObjs[tdNum-1].getElementsByTagName("input")[0].value = rowTotal;
            }
        }

        for(var n=1;n<tdNum;n++)
        {
            if(n<tdNum-1)
            {
                var nameMatch = tdObjs[n].getElementsByTagName("input")[0].getAttribute("id");
                if(boxName==nameMatch)
                {
                    indexNum = n;
                }
            }
            else
            {
                lastIndexNum = n;
            }
        }

        var trObjs = tableObj.getElementsByTagName("tr");
        var trNum = tableObj.getElementsByTagName("tr").length;

        var currTdObj;
        var currTdObj2;
        var currTdObj3;
        var grandTotal = 0;
        var tdLen;
        var lastRowTds;
        var lastSum;
        var parentParamName;
        var lastTrObj;
        var plt;
        var diff;
        var total;
        var sumOfTotal;
        var sumOfPlt;
        var sumOfDiff;
        var tot;
        var plTot;
        var diffTot;
        var secParamName;
        var secParamObj;

        for(var p=1;p<trNum;p++)
        {
            lastTrObj = trObjs[trNum-1].getElementsByTagName("td")[0].innerHTML;
        }

        if(lastTrObj=="Column Total")
        {
            for(var q=1;q<trNum;q++)
            {
                if(q<trNum-1)
                {
                    currTdObj = trObjs[q].getElementsByTagName("td")[indexNum];
                    var currContent = currTdObj.getElementsByTagName("input")[0].value;
                    if(currContent=="")
                    {
                        currContent = parseInt(0);
                    }
                    currContent = parseInt(currContent);
                    columnTotal = columnTotal + currContent;
                }
                else
                {
                    currTdObj = trObjs[q].getElementsByTagName("td")[indexNum];
                    currTdObj.getElementsByTagName("input")[0].value = columnTotal;

                    tdLen = trObjs[q].getElementsByTagName("td").length;
                    lastRowTds = trObjs[q].getElementsByTagName("td");
                    for(var s=1;s<tdLen;s++)
                    {
                        if(s<tdLen-1)
                        {
                            lastSum = lastRowTds[s].getElementsByTagName("input")[0].value;
                            lastSum = parseInt(lastSum)
                            grandTotal = grandTotal + lastSum;
                            grandTotal = parseInt(grandTotal);
                        }
                        else
                        {
                            lastRowTds[s].getElementsByTagName("input")[0].value = grandTotal;
                        }
                    }
                    var grandTotalSetTo = document.getElementById("GrandTotal").value;
                    grandTotalSetTo = parseInt(grandTotalSetTo);
                }
            }
            return true;
        }
        else
        {
            for(var r=1;r<trNum;r++)
            {
                if(r<trNum-3)
                {
                    currTdObj = trObjs[r].getElementsByTagName("td")[indexNum];
                    var currContent1 = currTdObj.getElementsByTagName("input")[0].value;
                    if(currContent1=="")
                    {
                        currContent1 = parseInt(0);
                    }
                    currContent1 = parseInt(currContent1);
                    columnTotal = columnTotal + currContent1;
                }
                else if(r==trNum-3)
                {
                    currTdObj = trObjs[trNum-3].getElementsByTagName("td")[indexNum];
                    currTdObj.getElementsByTagName("input")[0].value = columnTotal;
                    total = columnTotal;

                    tdLen = trObjs[r].getElementsByTagName("td").length;
                    lastRowTds = trObjs[r].getElementsByTagName("td");
                    for(var st=1;st<tdLen;st++)
                    {
                        if(st<tdLen-1)
                        {
                            lastSum = lastRowTds[st].getElementsByTagName("input")[0].value;
                            lastSum = parseInt(lastSum)
                            grandTotal = grandTotal + lastSum;
                            grandTotal = parseInt(grandTotal);
                        }
                        else
                        {
                            lastRowTds[st].getElementsByTagName("input")[0].value = grandTotal;
                        }
                    }
                }
                 else
                 {
                    for(var u=1;u<tdNum;u++)
                    {
                        var getTotal = trObjs[trNum-3].getElementsByTagName("td")[u].getElementsByTagName("input")[0].value;
                        getTotal = parseInt(getTotal);
                        var getPlt = trObjs[trNum-2].getElementsByTagName("td")[u].getElementsByTagName("input")[0].value;
                        getPlt = parseInt(getPlt);
                        trObjs[trNum-1].getElementsByTagName("td")[u].getElementsByTagName("input")[0].value = parseInt(getPlt - getTotal);

                        if(trObjs[trNum-1].getElementsByTagName("td")[u].getElementsByTagName("input")[0].value < 0)
                        {
                            trObjs[trNum-1].getElementsByTagName("td")[u].getElementsByTagName("input")[0].style.color = 'red';
                        }
                        else if(trObjs[trNum-1].getElementsByTagName("td")[u].getElementsByTagName("input")[0].value > 0)
                        {
                            trObjs[trNum-1].getElementsByTagName("td")[u].getElementsByTagName("input")[0].style.color = 'green';
                        }
                        else
                        {
                            trObjs[trNum-1].getElementsByTagName("td")[u].getElementsByTagName("input")[0].style.color = 'orange';
                        }

                        var getDiff = trObjs[trNum-1].getElementsByTagName("td")[u].getElementsByTagName("input")[0].value;
                        getDiff = parseInt(getDiff);
                    }
                 }
            }
        }

    }

}
*/
var checkFlag=true;
var onFocusVal;
function onFocusValue(boxName) {
    var tdObj = document.getElementById(boxName).parentNode;
    var trObj = tdObj.parentNode;
    var tableObj = trObj.parentNode;
    var tdObjs = trObj.getElementsByTagName("td");
    var tdNum = tdObjs.length;
    var nameMatch;
    var indexNum=0;
    var primViewBy;
    var secViewBy;
    var terViewBy;
    var eachValue;
    var rowTotal=0;
    var maxTimeLevel = document.getElementById("maxTimeLevel").value;
    var selPeriodType = document.frmParameter.CBO_PRG_PERIOD.value;
    if(maxTimeLevel!=selPeriodType) {
        var selIndex = document.getElementById("primaryViewBy").selectedIndex;
        if(selIndex==0)
        {
            primViewBy = document.getElementById("primaryViewBy").getElementsByTagName("option")[0].innerHTML;
        }
        secViewBy = document.getElementById("secondaryViewBy").value;
        terViewBy = document.getElementById("tertiaryViewBy").value;
        for(var k=1;k<tdNum-6;k++) {
            if((k%2)!=0)
            {
                nameMatch = tdObjs[k].getElementsByTagName("input")[0].getAttribute("id");
                if(boxName==nameMatch)
                {
                    indexNum = k;
                }
                eachValue = tdObjs[k].getElementsByTagName("input")[0].value;
                if(eachValue=="")
                {
                    eachValue = parseInt(0);
                }
                rowTotal = parseInt(rowTotal) + parseInt(eachValue);
            }
        }
        //alert("tdObjs[tdNum - 8].getElementsByTagName(input)[0].value is:: "+tdObjs[tdNum - 8].getElementsByTagName("input")[0].value)
        if(primViewBy=="Overall target" && terViewBy=="Percentage")
        {
            if(indexNum==tdNum-8) {
                if(tdObjs[tdNum - 8].getElementsByTagName("input")[0].value == "") {
                    tdObjs[tdNum - 8].getElementsByTagName("input")[0].value = tdObjs[tdNum - 4].getElementsByTagName("input")[0].value - tdObjs[tdNum - 6].getElementsByTagName("input")[0].value;
                    tdObjs[tdNum - 7].getElementsByTagName("input")[0].value = tdObjs[tdNum - 3].getElementsByTagName("input")[0].value - tdObjs[tdNum - 5].getElementsByTagName("input")[0].value;
                    tdObjs[tdNum - 6].getElementsByTagName("input")[0].value = parseInt(rowTotal) + parseInt(tdObjs[tdNum - 2].getElementsByTagName("input")[0].value);
                    tdObjs[tdNum - 5].getElementsByTagName("input")[0].value = parseInt((rowTotal / 100)*(tdObjs[tdNum - 3].getElementsByTagName("input")[0].value)) + parseInt(tdObjs[tdNum - 1].getElementsByTagName("input")[0].value);
                    tdObjs[tdNum - 2].getElementsByTagName("input")[0].value = tdObjs[tdNum - 4].getElementsByTagName("input")[0].value - tdObjs[tdNum - 6].getElementsByTagName("input")[0].value;
                    tdObjs[tdNum - 1].getElementsByTagName("input")[0].value = tdObjs[tdNum - 3].getElementsByTagName("input")[0].value - tdObjs[tdNum - 5].getElementsByTagName("input")[0].value;
                }
            }
            else {
                if(tdObjs[tdNum - 8].getElementsByTagName("input")[0].value != "") {
                    tdObjs[tdNum - 2].getElementsByTagName("input")[0].value = tdObjs[tdNum - 8].getElementsByTagName("input")[0].value;
                    tdObjs[tdNum - 1].getElementsByTagName("input")[0].value = tdObjs[tdNum - 7].getElementsByTagName("input")[0].value;
                    tdObjs[tdNum - 6].getElementsByTagName("input")[0].value = tdObjs[tdNum - 6].getElementsByTagName("input")[0].value - tdObjs[tdNum - 8].getElementsByTagName("input")[0].value;
                    tdObjs[tdNum - 5].getElementsByTagName("input")[0].value = tdObjs[tdNum - 5].getElementsByTagName("input")[0].value - tdObjs[tdNum - 7].getElementsByTagName("input")[0].value;
                    tdObjs[tdNum - 8].getElementsByTagName("input")[0].value = "";
                    tdObjs[tdNum - 7].getElementsByTagName("input")[0].value = "";
                }
            }
        }
        if(primViewBy=="Overall target" && terViewBy=="Absolute")
        {
            if(indexNum==tdNum-8) {
                if(tdObjs[tdNum - 8].getElementsByTagName("input")[0].value == "") {
                    tdObjs[tdNum - 8].getElementsByTagName("input")[0].value = tdObjs[tdNum - 4].getElementsByTagName("input")[0].value - tdObjs[tdNum - 6].getElementsByTagName("input")[0].value;
                    tdObjs[tdNum - 7].getElementsByTagName("input")[0].value = tdObjs[tdNum - 3].getElementsByTagName("input")[0].value - tdObjs[tdNum - 5].getElementsByTagName("input")[0].value;
                    tdObjs[tdNum - 6].getElementsByTagName("input")[0].value = parseInt(rowTotal) + parseInt(tdObjs[tdNum - 2].getElementsByTagName("input")[0].value);
                    tdObjs[tdNum - 5].getElementsByTagName("input")[0].value = parseInt((rowTotal / (tdObjs[tdNum - 4].getElementsByTagName("input")[0].value))*100) + parseInt(tdObjs[tdNum - 1].getElementsByTagName("input")[0].value);
                    tdObjs[tdNum - 2].getElementsByTagName("input")[0].value = tdObjs[tdNum - 4].getElementsByTagName("input")[0].value - tdObjs[tdNum - 6].getElementsByTagName("input")[0].value;
                    tdObjs[tdNum - 1].getElementsByTagName("input")[0].value = tdObjs[tdNum - 3].getElementsByTagName("input")[0].value - tdObjs[tdNum - 5].getElementsByTagName("input")[0].value;
                }
            }
            else {
                if(tdObjs[tdNum - 8].getElementsByTagName("input")[0].value != "") {
                    tdObjs[tdNum - 2].getElementsByTagName("input")[0].value = tdObjs[tdNum - 8].getElementsByTagName("input")[0].value;
                    tdObjs[tdNum - 1].getElementsByTagName("input")[0].value = tdObjs[tdNum - 7].getElementsByTagName("input")[0].value;
                    tdObjs[tdNum - 6].getElementsByTagName("input")[0].value = tdObjs[tdNum - 6].getElementsByTagName("input")[0].value - tdObjs[tdNum - 8].getElementsByTagName("input")[0].value;
                    tdObjs[tdNum - 5].getElementsByTagName("input")[0].value = tdObjs[tdNum - 5].getElementsByTagName("input")[0].value - tdObjs[tdNum - 7].getElementsByTagName("input")[0].value;
                    tdObjs[tdNum - 8].getElementsByTagName("input")[0].value = "";
                    tdObjs[tdNum - 7].getElementsByTagName("input")[0].value = "";
                }
            }
        }
    }
}
/*
function onFocusValue(boxName) {
    var tdObj = document.getElementById(boxName).parentNode;
    var trObj = tdObj.parentNode;
    var tableObj = trObj.parentNode;
    var tdObjs = trObj.getElementsByTagName("td");
    var tdNum = tdObjs.length;
    var nameMatch;
    var indexNum=0;
    var eachValue;
    var rowTotal=0;
    for(var k=1;k<tdNum-6;k++) {
        if((k%2)!=0)
        {
            nameMatch = tdObjs[k].getElementsByTagName("input")[0].getAttribute("id");
            if(boxName==nameMatch)
            {
                indexNum = k;
            }
            eachValue = tdObjs[k].getElementsByTagName("input")[0].value;
            if(eachValue=="")
            {
                eachValue = parseInt(0);
            }
            rowTotal = parseInt(rowTotal) + parseInt(eachValue);
        }
    }

    if(indexNum==tdNum-8) {
        if(tdObjs[tdNum - 8].getElementsByTagName("input")[0].value == "") {
            tdObjs[tdNum - 8].getElementsByTagName("input")[0].value = tdObjs[tdNum - 4].getElementsByTagName("input")[0].value - tdObjs[tdNum - 6].getElementsByTagName("input")[0].value;
            tdObjs[tdNum - 7].getElementsByTagName("input")[0].value = tdObjs[tdNum - 3].getElementsByTagName("input")[0].value - tdObjs[tdNum - 5].getElementsByTagName("input")[0].value;
            tdObjs[tdNum - 6].getElementsByTagName("input")[0].value = parseInt(rowTotal) + parseInt(tdObjs[tdNum - 2].getElementsByTagName("input")[0].value);
            tdObjs[tdNum - 5].getElementsByTagName("input")[0].value = parseInt((rowTotal / (tdObjs[tdNum - 4].getElementsByTagName("input")[0].value))*100) + parseInt(tdObjs[tdNum - 1].getElementsByTagName("input")[0].value);
            tdObjs[tdNum - 2].getElementsByTagName("input")[0].value = tdObjs[tdNum - 4].getElementsByTagName("input")[0].value - tdObjs[tdNum - 6].getElementsByTagName("input")[0].value;
            tdObjs[tdNum - 1].getElementsByTagName("input")[0].value = tdObjs[tdNum - 3].getElementsByTagName("input")[0].value - tdObjs[tdNum - 5].getElementsByTagName("input")[0].value;
        }
    }
    else {
        if(tdObjs[tdNum - 8].getElementsByTagName("input")[0].value != "") {
            tdObjs[tdNum - 2].getElementsByTagName("input")[0].value = tdObjs[tdNum - 8].getElementsByTagName("input")[0].value;
            tdObjs[tdNum - 1].getElementsByTagName("input")[0].value = tdObjs[tdNum - 7].getElementsByTagName("input")[0].value;
            tdObjs[tdNum - 6].getElementsByTagName("input")[0].value = tdObjs[tdNum - 6].getElementsByTagName("input")[0].value - tdObjs[tdNum - 8].getElementsByTagName("input")[0].value;
            tdObjs[tdNum - 5].getElementsByTagName("input")[0].value = tdObjs[tdNum - 5].getElementsByTagName("input")[0].value - tdObjs[tdNum - 7].getElementsByTagName("input")[0].value;
            tdObjs[tdNum - 8].getElementsByTagName("input")[0].value = "";
            tdObjs[tdNum - 7].getElementsByTagName("input")[0].value = "";
        }
    }


} */
function autoSum(boxName)
{
    //added on 6th-Jan-2010
    var tdObj = document.getElementById(boxName).parentNode;
    var trObj = tdObj.parentNode;
    var tableObj = trObj.parentNode;
    var tdObjs = trObj.getElementsByTagName("td");
    var tdNum = tdObjs.length;
    var nextTdObj;
    var content;
    var primViewBy;
    var secViewBy;
    var terViewBy;
    var restrictingTotal;
    var enteredPercentage;
    var enteredAbsolute;
    var eachValue;
    var rowTotal=0;
    var nameMatch;
    var indexNum=0;
    var parTotal=0;
    var maxTimeLevel = document.getElementById("maxTimeLevel").value;
    var selPeriodType = document.frmParameter.CBO_PRG_PERIOD.value;
    if(maxTimeLevel!=selPeriodType) {
        var selIndex = document.getElementById("primaryViewBy").selectedIndex;
        if(selIndex==0)
        {
            primViewBy = document.getElementById("primaryViewBy").getElementsByTagName("option")[0].innerHTML;
        }
        secViewBy = document.getElementById("secondaryViewBy").value;
        terViewBy = document.getElementById("tertiaryViewBy").value;
        if(primViewBy=="Overall target" && terViewBy=="Percentage")
        {
            restrictingTotal = tdObjs[tdNum - 3].getElementsByTagName("input")[0].value;
            enteredPercentage = tdObj.getElementsByTagName("input")[0].value;
            content = (enteredPercentage / 100) * restrictingTotal;
            nextTdObj = tdObj.nextSibling;
            for(var j=1;j<tdNum-6;j++)
            {
                if((j%2)!=0)
                {
                    eachValue = tdObjs[j].getElementsByTagName("input")[0].value;
                    if(eachValue=="")
                    {
                        eachValue = parseInt(0);
                    }
                    rowTotal = parseInt(rowTotal) + parseInt(eachValue);
                }
            }
            if(rowTotal <= 100)
            {
                document.getElementById("previousValue").value = enteredPercentage;
                nextTdObj.getElementsByTagName("input")[0].value = content;
                tdObjs[tdNum - 6].getElementsByTagName("input")[0].value = rowTotal;
                tdObjs[tdNum - 5].getElementsByTagName("input")[0].value = (rowTotal / 100) * restrictingTotal;
                tdObjs[tdNum - 2].getElementsByTagName("input")[0].value = tdObjs[tdNum - 4].getElementsByTagName("input")[0].value - rowTotal;
                tdObjs[tdNum - 1].getElementsByTagName("input")[0].value = tdObjs[tdNum - 3].getElementsByTagName("input")[0].value - tdObjs[tdNum - 5].getElementsByTagName("input")[0].value;
                return true;
            }
            else
            {
                for(var k=1;k<tdNum-6;k++) {
                    if((k%2)!=0)
                    {
                        nameMatch = tdObjs[k].getElementsByTagName("input")[0].getAttribute("id");
                        if(boxName==nameMatch)
                        {
                            indexNum = k;
                        }
                    }
                }
                for(var a=1;a<tdNum-6;a++) {
                    if((a%2)!=0 && a!=indexNum)
                    {
                        eachValue = tdObjs[a].getElementsByTagName("input")[0].value;
                        if(eachValue=="")
                        {
                            eachValue = parseInt(0);
                        }
                        parTotal = parseInt(parTotal) + parseInt(eachValue);
                    }
                }
                if(parTotal==restrictingTotal) {
                    tdObj.getElementsByTagName("input")[0].value = "";
                    alert("You can not exceed Restricting Total.");
                    return false;
                }
                else {
                    tdObj.getElementsByTagName("input")[0].value = document.getElementById("previousValue").value;
                    alert("You can not exceed Restricting Total.");
                    return false;
                }
            }
        }
        if(primViewBy=="Overall target" && terViewBy=="Absolute")
        {
            restrictingTotal = tdObjs[tdNum - 4].getElementsByTagName("input")[0].value;
            enteredAbsolute = tdObj.getElementsByTagName("input")[0].value;
            content = (enteredAbsolute / restrictingTotal) * 100;
            nextTdObj = tdObj.nextSibling;

            for(var j=1;j<tdNum-6;j++)
            {
                if((j%2)!=0)
                {
                    eachValue = tdObjs[j].getElementsByTagName("input")[0].value;
                    if(eachValue=="")
                    {
                        eachValue = parseInt(0);
                    }
                    rowTotal = parseInt(rowTotal) + parseInt(eachValue);
                }
            }
            if(rowTotal <= restrictingTotal)
            {
                checkFlag = true;
                document.getElementById("previousValue").value = enteredAbsolute;
                nextTdObj.getElementsByTagName("input")[0].value = content;
                tdObjs[tdNum - 6].getElementsByTagName("input")[0].value = rowTotal;
                tdObjs[tdNum - 5].getElementsByTagName("input")[0].value = (rowTotal / restrictingTotal) * 100;
                tdObjs[tdNum - 2].getElementsByTagName("input")[0].value = restrictingTotal - rowTotal;
                tdObjs[tdNum - 1].getElementsByTagName("input")[0].value = tdObjs[tdNum - 3].getElementsByTagName("input")[0].value - tdObjs[tdNum - 5].getElementsByTagName("input")[0].value;

                return true;
            }
            else
            {
                checkFlag = false;
                for(var k=1;k<tdNum-6;k++) {
                    if((k%2)!=0)
                    {
                        nameMatch = tdObjs[k].getElementsByTagName("input")[0].getAttribute("id");
                        if(boxName==nameMatch)
                        {
                            indexNum = k;
                        }
                    }
                }
                for(var a=1;a<tdNum-6;a++) {
                    if((a%2)!=0 && a!=indexNum)
                    {
                        eachValue = tdObjs[a].getElementsByTagName("input")[0].value;
                        if(eachValue=="")
                        {
                            eachValue = parseInt(0);
                        }
                        parTotal = parseInt(parTotal) + parseInt(eachValue);
                    }
                }
                if(parTotal==restrictingTotal) {
                    tdObj.getElementsByTagName("input")[0].value = "";
                    document.getElementById("previousValue").value = "";
                    alert("You can not exceed Restricting Total.");
                    return false;
                }
                else {
                    tdObj.getElementsByTagName("input")[0].value = document.getElementById("previousValue").value;
                    alert("You can not exceed Restricting Total.");
                    return false;
                }
            }
        }
    }
    else {

        var rowTotal = 0;
        var columnTotal = 0;
        var name = boxName;
        var indexNum = 0;
        var lastIndexNum = 0;
        var tdObj = document.getElementById(boxName).parentNode;
        var trObj = tdObj.parentNode;
        var tableObj = trObj.parentNode;
        var tdObjs = trObj.getElementsByTagName("td");
        var tdNum = trObj.getElementsByTagName("td").length;
        var val = document.getElementById(boxName).value;
        for(var m=1;m<tdNum;m++)
        {
            if(m<tdNum-1)
            {
                var content = tdObjs[m].getElementsByTagName("input")[0].value;
                if(content=="")
                {
                    content = parseInt(0);
                }
                content = parseInt(content);
                rowTotal = rowTotal + content;
            }
            else
            {
                tdObjs[tdNum-1].getElementsByTagName("input")[0].value = rowTotal;
            }
        }

        for(var n=1;n<tdNum;n++)
        {
            if(n<tdNum-1)
            {
                var nameMatch = tdObjs[n].getElementsByTagName("input")[0].getAttribute("id");
                if(boxName==nameMatch)
                {
                    indexNum = n;
                }
            }
            else
            {
                lastIndexNum = n;
            }
        }

        var trObjs = tableObj.getElementsByTagName("tr");
        var trNum = tableObj.getElementsByTagName("tr").length;

        var currTdObj;
        var currTdObj2;
        var currTdObj3;
        var grandTotal = 0;
        var tdLen;
        var lastRowTds;
        var lastSum;
        var parentParamName;
        var lastTrObj;
        var plt;
        var diff;
        var total;
        var sumOfTotal;
        var sumOfPlt;
        var sumOfDiff;
        var tot;
        var plTot;
        var diffTot;
        var secParamName;
        var secParamObj;

        for(var p=1;p<trNum;p++)
        {
            lastTrObj = trObjs[trNum-1].getElementsByTagName("td")[0].innerHTML;
        }

        if(lastTrObj=="Column Total")
        {
            for(var q=1;q<trNum;q++)
            {
                if(q<trNum-1)
                {
                    currTdObj = trObjs[q].getElementsByTagName("td")[indexNum];
                    var currContent = currTdObj.getElementsByTagName("input")[0].value;
                    if(currContent=="")
                    {
                        currContent = parseInt(0);
                    }
                    currContent = parseInt(currContent);
                    columnTotal = columnTotal + currContent;
                }
                else
                {
                    currTdObj = trObjs[q].getElementsByTagName("td")[indexNum];
                    currTdObj.getElementsByTagName("input")[0].value = columnTotal;

                    tdLen = trObjs[q].getElementsByTagName("td").length;
                    lastRowTds = trObjs[q].getElementsByTagName("td");
                    for(var s=1;s<tdLen;s++)
                    {
                        if(s<tdLen-1)
                        {
                            lastSum = lastRowTds[s].getElementsByTagName("input")[0].value;
                            lastSum = parseInt(lastSum)
                            grandTotal = grandTotal + lastSum;
                            grandTotal = parseInt(grandTotal);
                        }
                        else
                        {
                            lastRowTds[s].getElementsByTagName("input")[0].value = grandTotal;
                        }
                    }
                    var grandTotalSetTo = document.getElementById("GrandTotal").value;
                    grandTotalSetTo = parseInt(grandTotalSetTo);
                }
            }
            return true;
        }
        else
        {
            for(var r=1;r<trNum;r++)
            {
                if(r<trNum-3)
                {
                    currTdObj = trObjs[r].getElementsByTagName("td")[indexNum];
                    var currContent1 = currTdObj.getElementsByTagName("input")[0].value;
                    if(currContent1=="")
                    {
                        currContent1 = parseInt(0);
                    }
                    currContent1 = parseInt(currContent1);
                    columnTotal = columnTotal + currContent1;
                }
                else if(r==trNum-3)
                {
                    currTdObj = trObjs[trNum-3].getElementsByTagName("td")[indexNum];
                    currTdObj.getElementsByTagName("input")[0].value = columnTotal;
                    total = columnTotal;

                    tdLen = trObjs[r].getElementsByTagName("td").length;
                    lastRowTds = trObjs[r].getElementsByTagName("td");
                    for(var st=1;st<tdLen;st++)
                    {
                        if(st<tdLen-1)
                        {
                            lastSum = lastRowTds[st].getElementsByTagName("input")[0].value;
                            lastSum = parseInt(lastSum)
                            grandTotal = grandTotal + lastSum;
                            grandTotal = parseInt(grandTotal);
                        }
                        else
                        {
                            lastRowTds[st].getElementsByTagName("input")[0].value = grandTotal;
                        }
                    }
                }
                else
                {
                    for(var u=1;u<tdNum;u++)
                    {
                        var getTotal = trObjs[trNum-3].getElementsByTagName("td")[u].getElementsByTagName("input")[0].value;
                        getTotal = parseInt(getTotal);
                        var getPlt = trObjs[trNum-2].getElementsByTagName("td")[u].getElementsByTagName("input")[0].value;
                        getPlt = parseInt(getPlt);
                        trObjs[trNum-1].getElementsByTagName("td")[u].getElementsByTagName("input")[0].value = parseInt(getPlt - getTotal);

                        if(trObjs[trNum-1].getElementsByTagName("td")[u].getElementsByTagName("input")[0].value < 0)
                        {
                            trObjs[trNum-1].getElementsByTagName("td")[u].getElementsByTagName("input")[0].style.color = 'red';
                        }
                        else if(trObjs[trNum-1].getElementsByTagName("td")[u].getElementsByTagName("input")[0].value > 0)
                        {
                            trObjs[trNum-1].getElementsByTagName("td")[u].getElementsByTagName("input")[0].style.color = 'green';
                        }
                        else
                        {
                            trObjs[trNum-1].getElementsByTagName("td")[u].getElementsByTagName("input")[0].style.color = 'orange';
                        }

                        var getDiff = trObjs[trNum-1].getElementsByTagName("td")[u].getElementsByTagName("input")[0].value;
                        getDiff = parseInt(getDiff);
                    }
                }
            }
        }

    }

}


function showGrandTotalrow()
{
    var tableObj = document.getElementById("target");
    var trObjs = tableObj.getElementsByTagName("tr");
    var tdObjs = trObjs[0].getElementsByTagName("td");
    var trNum = trObjs.length;
    trNum = parseInt(trNum)
    var tdNum = tdObjs.length;
    var secParamObj;
    var secParamName;
    var lastRowSum = 0;
    var CTSum = 0;
    var RTSum = 0;
    var DiffSum = 0;

    var lastRowObj = trObjs[trNum-1].getElementsByTagName("td")[0].innerHTML;
    //alert(lastRowObj)
    if(lastRowObj=="Difference")
    {
        for(var u=1;u<tdNum;u++)
        {
            if(u != tdNum-1)
            {
                //alert(trNum - 3)
                //alert(trObjs[trNum-3].getElementsByTagName("td")[0].getElementsByTagName("input")[0].value)
                var getTotal = trObjs[trNum-3].getElementsByTagName("td")[u].getElementsByTagName("input")[0].value;
                getTotal = parseInt(getTotal);
                CTSum = parseInt(CTSum) + parseInt(getTotal);
                //alert(getTotal)
                var getPlt = trObjs[trNum-2].getElementsByTagName("td")[u].getElementsByTagName("input")[0].value;
                getPlt = parseInt(getPlt);
                RTSum = parseInt(RTSum) + parseInt(getPlt);
                //alert(getPlt)
                var getDiff = trObjs[trNum-1].getElementsByTagName("td")[u].getElementsByTagName("input")[0].value;
                getDiff = parseInt(getDiff);
                DiffSum = parseInt(DiffSum) + parseInt(getDiff);
                //alert(getDiff)
                trObjs[trNum-1].getElementsByTagName("td")[u].getElementsByTagName("input")[0].value = parseInt(getPlt - getTotal);

                if(trObjs[trNum-1].getElementsByTagName("td")[u].getElementsByTagName("input")[0].value < 0)
                {
                    trObjs[trNum-1].getElementsByTagName("td")[u].getElementsByTagName("input")[0].style.color = 'red';
                }
                else if(trObjs[trNum-1].getElementsByTagName("td")[u].getElementsByTagName("input")[0].value > 0)
                {
                    trObjs[trNum-1].getElementsByTagName("td")[u].getElementsByTagName("input")[0].style.color = 'green';
                }
                else
                {
                    trObjs[trNum-1].getElementsByTagName("td")[u].getElementsByTagName("input")[0].style.color = 'orange';
                }
            }
            else
            {
                trObjs[trNum-3].getElementsByTagName("td")[u].getElementsByTagName("input")[0].value = CTSum;
                trObjs[trNum-2].getElementsByTagName("td")[u].getElementsByTagName("input")[0].value = RTSum;
                trObjs[trNum-1].getElementsByTagName("td")[u].getElementsByTagName("input")[0].value = parseInt(RTSum - CTSum);

                if(trObjs[trNum-1].getElementsByTagName("td")[u].getElementsByTagName("input")[0].value < 0)
                {
                    trObjs[trNum-1].getElementsByTagName("td")[u].getElementsByTagName("input")[0].style.color = 'red';
                }
                else if(trObjs[trNum-1].getElementsByTagName("td")[u].getElementsByTagName("input")[0].value > 0)
                {
                    trObjs[trNum-1].getElementsByTagName("td")[u].getElementsByTagName("input")[0].style.color = 'green';
                }
                else
                {
                    trObjs[trNum-1].getElementsByTagName("td")[u].getElementsByTagName("input")[0].style.color = 'orange';
                }
            }
        }
    }
    else
    {
        for(var v=1;v<tdNum-1;v++)
        {
            var lastRowContent = trObjs[trNum-1].getElementsByTagName("td")[v].getElementsByTagName("input")[0].value;
            if(lastRowContent=="")
            {
                lastRowContent = parseInt(0);
            }
            lastRowSum = parseInt(lastRowSum) + parseInt(lastRowContent);
        }
        trObjs[trNum-1].getElementsByTagName("td")[tdNum-1].getElementsByTagName("input")[0].value = lastRowSum;

    }

/*
    var grandTotalSetTo = document.getElementById("GrandTotal").value;
    grandTotalSetTo = parseInt(grandTotalSetTo);

    uncomment in future
    var parentParamName = document.getElementById("primParameter").value;
    secParamObj = document.getElementById("secParameter");

    if(secParamObj==null)
    {
        document.getElementById("displayGrandTotal").innerHTML = "Over All Target set to '"+grandTotalSetTo+"' by primary parameter '"+parentParamName+"'";
    }
    else
    {
        secParamName = document.getElementById("secParameter").value;
        document.getElementById("displayGrandTotal").innerHTML = "Over All Target set to '"+grandTotalSetTo+"' by Primary parameter '"+parentParamName+"' and by Secondary parameter '"+secParamName+"'";
    }
    */

}
function autoRowSum(boxName)
{
    //var tableObj = document.getElementById('target');
    //alert(tableObj.rows.length)
    //var trObjs = tableObj.getElementsByTagName("tr");
    //var tdObjs = trObj[0].getElementsByTagName("td");
    var rowName;
    var columnName;
    var totalObj;
    var rowTotal = 0;
    var indexNum = 0;
    var lastIndexNum = 0;
    var currTdObj;
    var columnTotal = 0;
    var grandTotal = 0;
    var TotalPLT = 0;
    //var tds = tdObj.length;

    var tdObj = document.getElementById(boxName).parentNode;
    var trObj = tdObj.parentNode;
    var tableObj = trObj.parentNode;
    var trObjs = tableObj.getElementsByTagName("tr");
    var trNum = trObjs.length;
    var tdObjs = trObjs[0].getElementsByTagName("td");
    var specificRowTdObjs = trObj.getElementsByTagName("td");
    var tdNum = trObj.getElementsByTagName("td").length;

    for(var m=1;m<tdNum;m++)
    {
        if(m<tdNum-3)
        {
            var content = specificRowTdObjs[m].getElementsByTagName("input")[0].value;
            //alert(content)
            if(content=="")
            {
                content = parseInt(0);
            }
            content = parseInt(content);
            rowTotal = rowTotal + content;
        }
        else if(m == tdNum-3)
        {
            specificRowTdObjs[tdNum-3].getElementsByTagName("input")[0].value = rowTotal;
        }
    }

    for(var n=1;n<tdNum;n++)
    {
        if(n<tdNum-3)
        {
            var nameMatch = specificRowTdObjs[n].getElementsByTagName("input")[0].getAttribute("id");
            if(boxName==nameMatch)
            {
                indexNum = n;
            //alert(indexNum)
            }
        }
        else
        {
            lastIndexNum = n;
        //alert(lastIndexNum)
        }
    }

    for(var q=1;q<trNum;q++)
    {
        //lastTrObj = trObjs[trNum-1].getElementsByTagName("td")[0].innerHTML;
        //alert(lastTrObj)

        if(q<trNum-1)
        {
            currTdObj = trObjs[q].getElementsByTagName("td")[indexNum];
            var currContent = currTdObj.getElementsByTagName("input")[0].value;
            if(currContent=="")
            {
                currContent = parseInt(0);
            }
            currContent = parseInt(currContent);
            columnTotal = columnTotal + currContent;
        }
        else
        {
            currTdObj = trObjs[q].getElementsByTagName("td")[indexNum];
            currTdObj.getElementsByTagName("input")[0].value = columnTotal;
            var lastRowTds = trObjs[q].getElementsByTagName("td");
            for(var j=1;j<lastRowTds.length;j++)
            {
                if(j<(lastRowTds.length - 3))
                {
                    var lastRowTdContent = lastRowTds[j].getElementsByTagName("input")[0].value;
                    if(lastRowTdContent == '')
                    {
                        lastRowTdContent = 0;
                    }
                    lastRowTdContent = parseInt(lastRowTdContent);
                    grandTotal = parseInt(grandTotal + lastRowTdContent);
                }
                else if(j==(lastRowTds.length - 3))
                {
                    lastRowTds[j].getElementsByTagName("input")[0].value = grandTotal;
                }
            }


        }
    }

    for(var z=1;z<trNum;z++)
    {
        var rowTotalCell = trObjs[z].getElementsByTagName("td")[tdNum-3].getElementsByTagName("input")[0].value;
        var PLTCell = trObjs[z].getElementsByTagName("td")[tdNum-2].getElementsByTagName("input")[0].value;
        var DiffCell = trObjs[z].getElementsByTagName("td")[tdNum-1].getElementsByTagName("input")[0].value;
        if(DiffCell == '')
        {
            DiffCell = 0;
        }
        DiffCell = parseInt(DiffCell);

        DiffCell = parseInt(PLTCell - rowTotalCell);
        trObjs[z].getElementsByTagName("td")[tdNum-1].getElementsByTagName("input")[0].value = DiffCell;

        if(DiffCell < 0)
        {
            trObjs[z].getElementsByTagName("td")[tdNum-1].getElementsByTagName("input")[0].style.color = 'red';
        }
        else if(DiffCell > 0)
        {
            trObjs[z].getElementsByTagName("td")[tdNum-1].getElementsByTagName("input")[0].style.color = 'green';
        }
        else
        {
            trObjs[z].getElementsByTagName("td")[tdNum-1].getElementsByTagName("input")[0].style.color = 'orange';
        }
    }


/*
    for(var y=1;y<trObj.length;y++)
    {
        tdObj = trObj[y].getElementsByTagName("td");
        for(var z=1;z<tdObj.length;z++)
        {
            if(z < parseInt(tdObj.length)-3)
            {
                var content = tdObj[z].getElementsByTagName("input")[0].value;
                if(content=='')
                {
                    content = 0;
                }
                content = parseInt(content);
               // alert(content+" content");
                rowTotal = parseInt(rowTotal + parseInt(content));
               // alert(rowTotal+" total");
            }
        }
        tdObj[(tdObj.length)-3].getElementsByTagName("input")[0].value = rowTotal;

     }*/
}
function showGrandTotalColumn()
{
    var tableObj = document.getElementById("target");
    var trObjs = tableObj.getElementsByTagName("tr");
    var tdObjs = trObjs[0].getElementsByTagName("td");
    var trNum = trObjs.length;
    trNum = parseInt(trNum);
    var tdNum = tdObjs.length;
    tdNum = parseInt(tdNum);
    var TotalPLT = 0;
    var showRowTotal = 0;
    var showColumnTotal = 0;
    var grandRowTotal = 0;

    for(var k=1;k<trNum;k++)
    {
        if(k<trNum-1)
        {
            for(var l=(tdNum-2);l<(tdNum-1);l++)
            {
                var eachPLT = trObjs[k].getElementsByTagName("td")[l].getElementsByTagName("input")[0].value;
                eachPLT = parseInt(eachPLT);
                TotalPLT = TotalPLT + eachPLT;
            }
        }
        else
        {
            trObjs[k].getElementsByTagName("td")[tdNum-2].getElementsByTagName("input")[0].value = TotalPLT;
        }
    }

    for(var v=1;v<trNum;v++)
    {
        if(v < (trNum -1))
        {
            for(var w=1;w<tdNum-2;w++)
            {
                if(w < tdNum-3)
                {
                    var cellVal = trObjs[v].getElementsByTagName("td")[w].getElementsByTagName("input")[0].value;
                    if(cellVal=='')
                    {
                        cellVal = 0;
                    }
                    cellVal = parseInt(cellVal);
                    showRowTotal = showRowTotal + cellVal;
                }
                else if(w==tdNum-3)
                {
                    trObjs[v].getElementsByTagName("td")[w].getElementsByTagName("input")[0].value = showRowTotal;
                    grandRowTotal = parseInt(grandRowTotal + showRowTotal);
                    showRowTotal = 0;
                }
            }
        }
        else
        {
            trObjs[v].getElementsByTagName("td")[tdNum-3].getElementsByTagName("input")[0].value = grandRowTotal;
        }

    }

    for(var s=1;s<tdNum-3;s++)
    {
        for(var r=1;r<trNum;r++)
        {
            if(r<trNum-1)
            {
                var val = trObjs[r].getElementsByTagName("td")[s].getElementsByTagName("input")[0].value;
                if(val == '')
                {
                    val = 0;
                }
                val = parseInt(val);
                showColumnTotal = parseInt(showColumnTotal + val);
            }
            else
            {
                trObjs[r].getElementsByTagName("td")[s].getElementsByTagName("input")[0].value = showColumnTotal;
                showColumnTotal = 0;
            }
        }
    }

    for(var t=1;t<trNum;t++)
    {
        var getRowTotalVal = trObjs[t].getElementsByTagName("td")[tdNum-3].getElementsByTagName("input")[0].value;
        var getPLTVal = trObjs[t].getElementsByTagName("td")[tdNum-2].getElementsByTagName("input")[0].value;
        var getDiffVal = trObjs[t].getElementsByTagName("td")[tdNum-1].getElementsByTagName("input")[0].value;
        if(getDiffVal == '')
        {
            getDiffVal = 0;
        }
        getDiffVal = parseInt(getDiffVal);
        getDiffVal = parseInt(getPLTVal - getRowTotalVal);
        trObjs[t].getElementsByTagName("td")[tdNum-1].getElementsByTagName("input")[0].value = getDiffVal;

        if(getDiffVal < 0)
        {
            trObjs[t].getElementsByTagName("td")[tdNum-1].getElementsByTagName("input")[0].style.color = 'red';
        }
        else if(getDiffVal > 0)
        {
            trObjs[t].getElementsByTagName("td")[tdNum-1].getElementsByTagName("input")[0].style.color = 'green';
        }
        else
        {
            trObjs[t].getElementsByTagName("td")[tdNum-1].getElementsByTagName("input")[0].style.color = 'orange';
        }
    }



}
function submiturls1(pathName)
{
    //alert(pathName)
    document.forms.ec.action = pathName;
    document.forms.ec.submit();
}
function copyToTargetDesc()
{
    var tName = document.getElementById("targetName").value;
    var tDesc = document.getElementById("targetDescription");
    tDesc.value = tName;
}
function publishTarget()
{
    var path=document.getElementById("path").value;
    var tdObj;
    var trObj;
    var targetStatus;
    var name;
    var uc = false;
    var pub = false;
    var exp = false;
    var i=0;
    var obj = document.forms.ec.chk1;
    if(isNaN(obj.length))
    {
        if(document.forms.ec.chk1.checked)
        {
            tdObj = document.forms.ec.chk1.parentNode;
            trObj = tdObj.parentNode;
            targetStatus = trObj.getElementsByTagName("td")[4].innerHTML;
            name = trObj.getElementsByTagName("td")[1].getElementsByTagName("a")[0].innerHTML;

            if(targetStatus=="Under Creation")
            {
                document.forms.ec.action=path+"/QTarget/JSPs/pbUpdateTargetStatus.jsp";
                document.forms.ec.submit();
            }
            if(targetStatus=="Published")
            {
                alert("'"+name+"' Target is already Published");
            }
            if(targetStatus=="Expired")
            {
                alert("Expired target can not be published");
            }

        }
        else
        {
            alert('Please select Target');
        }
    }
    else
    {
        for(var j=0;j<obj.length;j++)
        {
            if(document.forms.ec.chk1[j].checked==true)
            {
                tdObj = document.forms.ec.chk1[j].parentNode;
                trObj = tdObj.parentNode;
                targetStatus = trObj.getElementsByTagName("td")[4].innerHTML;

                if(targetStatus=="Under Creation")
                {
                    name = trObj.getElementsByTagName("td")[1].getElementsByTagName("a")[0].innerHTML;
                    uc = true;
                }
                if(targetStatus=="Published")
                {
                    name = trObj.getElementsByTagName("td")[1].getElementsByTagName("a")[0].innerHTML;
                    pub = true;
                }
                if(targetStatus=="Expired")
                {
                    name = trObj.getElementsByTagName("td")[1].getElementsByTagName("a")[0].innerHTML;
                    exp = true;
                }

                i++;
            }
        }

        if(i>1)
        {
            alert('Please select only one Target');
        }
        else if(i==0)
        {
            alert('Please select Target');
        }
        else
        {
            if(uc==true)
            {
                document.forms.ec.action=path+"/QTarget/JSPs/pbUpdateTargetStatus.jsp";
                document.forms.ec.submit();
            }
            if(pub==true)
            {
                alert("'"+name+"' Target is already Published");
            }
            if(exp==true)
            {
                alert("Expired target can not be published");
            }

        }
    }

}

function expireTarget()
{
    var path=document.getElementById("path").value;
    var tdObj;
    var trObj;
    var targetStatus;
    var name;
    var exp = false;
    var i=0;
    var obj = document.forms.ec.chk1;
    if(isNaN(obj.length))
    {
        if(document.forms.ec.chk1.checked)
        {
            tdObj = document.forms.ec.chk1.parentNode;
            trObj = tdObj.parentNode;
            targetStatus = trObj.getElementsByTagName("td")[4].innerHTML;
            name = trObj.getElementsByTagName("td")[1].getElementsByTagName("a")[0].innerHTML;

            if(targetStatus=="Under Creation" || targetStatus=="Published")
            {
                document.forms.ec.action=path+"/QTarget/JSPs/pbExpireTarget.jsp";
                document.forms.ec.submit();
            }
            if(targetStatus=="Expired")
            {
                alert("'"+name+"' Target is already Expired");
            }

        }
        else
        {
            alert('Please select Target');
        }
    }
    else
    {
        for(var j=0;j<obj.length;j++)
        {
            if(document.forms.ec.chk1[j].checked==true)
            {
                tdObj = document.forms.ec.chk1[j].parentNode;
                trObj = tdObj.parentNode;
                targetStatus = trObj.getElementsByTagName("td")[4].innerHTML;

                if(targetStatus=="Under Creation" || targetStatus=="Published")
                {
                    name = trObj.getElementsByTagName("td")[1].getElementsByTagName("a")[0].innerHTML;
                    exp = false;
                }
                if(targetStatus=="Expired")
                {
                    name = trObj.getElementsByTagName("td")[1].getElementsByTagName("a")[0].innerHTML;
                    exp = true;
                }

                i++;
            // alert(document.forms.ec.chk2[j].value)

            }
        }

        if(i>1)
        {
            alert('Please select only one Target');
        }
        else if(i==0)
        {
            alert('Please select Target');
        }
        else
        {
            if(exp==false)
            {
                document.forms.ec.action=path+"/QTarget/JSPs/pbExpireTarget.jsp";
                document.forms.ec.submit();
            }
            else
            {
                alert("'"+name+"' Target is already Expired");
            }


        }
    }

}

function showTargetStartAndEnd()
{
    // var minTime = document.getElementById("timeLevel").value;
    //alert('in ii ');
    var  allLevels= window.opener.document.getElementById("timeLevels").value;
    var min=allLevels.split(",");
    var m=min[0];
    if(m=="1")
        minTime="Year";
    else if(m==2)
        minTime="Qtr";
    else if(m==3)
        minTime="Month";
    else if(m==4)
        minTime="Day";


    //alert(' max '+min);
    if(minTime=="Day")
    {
        window.opener.document.getElementById("startDate").style.display = '';
        window.opener.document.getElementById("endDate").style.display = '';
        window.opener.document.getElementById("startMonth").style.display = 'none';
        window.opener.document.getElementById("endMonth").style.display = 'none';
        window.opener.document.getElementById("startQtr").style.display = 'none';
        window.opener.document.getElementById("endQtr").style.display = 'none';
        window.opener.document.getElementById("startYear").style.display = 'none';
        window.opener.document.getElementById("endYear").style.display = 'none';
    }
    else if(minTime=="Month")
    {
        window.opener.document.getElementById("startMonth").style.display = '';
        window.opener.document.getElementById("endMonth").style.display = '';
        window.opener.document.getElementById("startDate").style.display = 'none';
        window.opener.document.getElementById("endDate").style.display = 'none';
        window.opener.document.getElementById("startQtr").style.display = 'none';
        window.opener.document.getElementById("endQtr").style.display = 'none';
        window.opener.document.getElementById("startYear").style.display = 'none';
        window.opener.document.getElementById("endYear").style.display = 'none';
    }
    else if(minTime=="Quarter"||minTime=="Qtr")
    {
        window.opener.document.getElementById("startQtr").style.display = '';
        window.opener.document.getElementById("endQtr").style.display = '';
        window.opener.document.getElementById("startMonth").style.display = 'none';
        window.opener.document.getElementById("endMonth").style.display = 'none';
        window.opener.document.getElementById("startDate").style.display = 'none';
        window.opener.document.getElementById("endDate").style.display = 'none';
        window.opener.document.getElementById("startYear").style.display = 'none';
        window.opener.document.getElementById("endYear").style.display = 'none';
    }
    else if(minTime=="Year")
    {
        window.opener.document.getElementById("startYear").style.display = '';
        window.opener.document.getElementById("endYear").style.display = '';
        window.opener.document.getElementById("startQtr").style.display = 'none';
        window.opener.document.getElementById("endQtr").style.display = 'none';
        window.opener.document.getElementById("startMonth").style.display = 'none';
        window.opener.document.getElementById("endMonth").style.display = 'none';
        window.opener.document.getElementById("startDate").style.display = 'none';
        window.opener.document.getElementById("endDate").style.display = 'none';
    }
    else
    {
        window.opener.document.getElementById("startDate").style.display = 'none';
        window.opener.document.getElementById("endDate").style.display = 'none';
        window.opener.document.getElementById("startMonth").style.display = 'none';
        window.opener.document.getElementById("endMonth").style.display = 'none';
        window.opener.document.getElementById("startQtr").style.display = 'none';
        window.opener.document.getElementById("endQtr").style.display = 'none';
        window.opener.document.getElementById("startYear").style.display = 'none';
        window.opener.document.getElementById("endYear").style.display = 'none';
    }
}
function checkSrcFromDate()
{
    var srcFromDate = document.getElementById("SrcFromDate").value;
    if(srcFromDate=="")
    {
        alert("Please select Source From-Date");
        document.getElementById("SrcFromDate").focus();
        return false;
    }
    else
    {
        return true;
    }
}
function checkSrcToDate()
{
    var srcToDate = document.getElementById("SrcToDate").value;
    if(srcToDate=="")
    {
        alert("Please select Source To-Date");
        document.getElementById("SrcToDate").focus();
        return false;
    }
    else
    {
        return true;
    }
}
function checkDestFromDate()
{
    var destFromDate = document.getElementById("DesFromDate").value;
    if(destFromDate=="")
    {
        alert("Please select Destination From-Date");
        document.getElementById("DesFromDate").focus();
        return false;
    }
    else
    {
        return true;
    }
}
function checkDestToDate()
{
    var destToDate = document.getElementById("DesToDate").value;
    if(destToDate=="")
    {
        alert("Please select Destination To-Date");
        document.getElementById("DesToDate").focus();
        return false;
    }
    else
    {
        return true;
    }
}
function copyRange(stDate,eDate,minTime)
{
    var startDate = stDate;
    var endDate = eDate;
    var minTimeLevel = minTime;

    var srcFromDate;
    var srcToDate;
    var destFromDate;
    var destToDate;
    if(minTimeLevel=="Day")
    {
        if(checkSrcFromDate() && checkSrcToDate() && checkDestFromDate() && checkDestToDate())
        {
            //alert("in iffff")
            srcFromDate = document.getElementById("SrcFromDate").value;
            srcToDate = document.getElementById("SrcToDate").value;
            destFromDate = document.getElementById("DesFromDate").value;
            destToDate = document.getElementById("DesToDate").value;

            startDate = new Date(startDate);
            endDate = new Date(endDate);

            srcFromDate = new Date(srcFromDate);
            srcToDate = new Date(srcToDate);
            destFromDate = new Date(destFromDate);
            destToDate = new Date(destToDate);

            if(srcFromDate.getTime() < startDate || srcFromDate > endDate)
            {
                alert("Please select Source From-Date between Target Start-Date and End-Date");
                return false;
            }
            if(srcToDate < startDate || srcToDate > endDate)
            {
                alert("Please select Source To-Date between Target Start-Date and End-Date");
                return false;
            }
            if(destFromDate < startDate || destFromDate > endDate)
            {
                alert("Please select Destination From-Date between Target Start-Date and End-Date");
                return false;
            }
            if(destToDate < startDate || destToDate > endDate)
            {
                alert("Please select Destination To-Date between Target Start-Date and End-Date");
                return false;
            }

            //12-sept
            if(srcToDate < srcFromDate)
            {
                alert("Source To-Date must be greater than Source From-Date");
                document.getElementById("SrcToDate").focus();
                return false;
            }
            if(destToDate < destFromDate)
            {
                alert("Destination To-Date must be greater than Destination From-Date");
                document.getElementById("DesToDate").focus();
                return false;
            }

        }
        else
        {
            return false;
        }
    }

    //12-sept-09
    else if(minTimeLevel=="Month")
    {
        var srcFromMonth = document.getElementById("SrcFromMonth").value;
        var srcToMonth = document.getElementById("SrcToMonth").value;
        var destFromMonth = document.getElementById("DesFromMonth").value;
        var destToMonth = document.getElementById("DesToMonth").value;

        var getSrcFromMonthYear = srcFromMonth.substring(4, srcFromMonth.length);
        var getSrcToMonthYear = srcToMonth.substring(4, srcToMonth.length);
        var getDesFromMonthYear = destFromMonth.substring(4, destFromMonth.length);
        var getDesToMonthYear = destToMonth.substring(4, destToMonth.length);

        if(getSrcToMonthYear < getSrcFromMonthYear)
        {
            alert("Source To-Month must be greater than Source From-Month");
            document.getElementById("SrcToMonth").focus();
            return false;
        }
        if(getDesToMonthYear < getDesFromMonthYear)
        {
            alert("Destination To-Month must be greater than Destination From-Month");
            document.getElementById("DesToMonth").focus();
            return false;
        }

        //12-SEPT-09
        if(getSrcToMonthYear == getSrcFromMonthYear)
        {
            var getSrcFromMonth = srcFromMonth.substring(0,3);
            var getSrcToMonth = srcToMonth.substring(0,3);
            var theFormFieldArray = formFieldArray();
            for (key in theFormFieldArray)
            {
                if(eval(theFormFieldArray[getSrcToMonth] + ' < '+theFormFieldArray[getSrcFromMonth]))
                {
                    alert('Source To-Month must be greater than Source From-Month');
                    document.getElementById("SrcToMonth").focus();
                    return false;
                }
            }
        }

        if(getDesToMonthYear == getDesFromMonthYear)
        {
            var getDesFromMonth = destFromMonth.substring(0,3);
            var getDesToMonth = destToMonth.substring(0,3);
            for (key in theFormFieldArray)
            {
                if(eval(theFormFieldArray[getDesToMonth] + ' < '+theFormFieldArray[getDesFromMonth]))
                {
                    alert('Destination To-Month must be greater than Destination From-Month');
                    document.getElementById("DesToMonth").focus();
                    return false;
                }
            }
        }
    }
    else if(minTimeLevel=="Quarter")
    {
        var srcFromQtr = document.getElementById("SrcFromQtr").value;
        var srcToQtr = document.getElementById("SrcToQtr").value;
        var destFromQtr = document.getElementById("DesFromQtr").value;
        var destToQtr = document.getElementById("DesToQtr").value;

        var getSrcFromQtrYear = srcFromQtr.substring(3, srcFromQtr.length);
        var getSrcToQtrYear = srcToQtr.substring(3, srcToQtr.length);
        var getDesFromQtrYear = destFromQtr.substring(3, destFromQtr.length);
        var getDesToQtrYear = destToQtr.substring(3, destToQtr.length);

        if(getSrcToQtrYear < getSrcFromQtrYear)
        {
            alert("Source To-Quarter must be greater than Source From-Quarter");
            document.getElementById("SrcToQtr").focus();
            return false;
        }
        if(getDesToQtrYear < getDesFromQtrYear)
        {
            alert("Destination To-Quarter must be greater than Destination From-Quarter");
            document.getElementById("DesToQtr").focus();
            return false;
        }

        //12-SEPT-09
        if(getSrcToQtrYear == getSrcFromQtrYear)
        {
            var getSrcFromQtr = srcFromQtr.substring(0,2);
            var getSrcToQtr = srcToQtr.substring(0,2);
            var theQuarterArray = quarterArray();
            for (key in theQuarterArray)
            {
                if(eval(theQuarterArray[getSrcToQtr] + ' < '+theQuarterArray[getSrcFromQtr]))
                {
                    alert('Source To-Quarter must be greater than Source From-Quarter');
                    document.getElementById("SrcToQtr").focus();
                    return false;
                }
            }
        }

        if(getDesToQtrYear == getDesFromQtrYear)
        {
            var getDesFromQtr = destFromQtr.substring(0,2);
            var getDesToQtr = destToQtr.substring(0,2);
            for (key in theQuarterArray)
            {
                if(eval(theQuarterArray[getDesToQtr] + ' < '+theQuarterArray[getDesFromQtr]))
                {
                    alert('Destination To-Quarter must be greater than Destination From-Quarter');
                    document.getElementById("DesToQtr").focus();
                    return false;
                }
            }
        }
    }
    else if(minTimeLevel=="Year")
    {
        var srcFromYear = document.getElementById("SrcFromYear").value;
        var srcToYear = document.getElementById("SrcToYear").value;
        var destFromYear = document.getElementById("DesFromYear").value;
        var destToYear = document.getElementById("DesToYear").value;

        if(srcToYear < srcFromYear)
        {
            alert("Source To-Year must be greater than Source From-Year");
            document.getElementById("SrcToYear").focus();
            return false;
        }
        if(destToYear < destFromYear)
        {
            alert("Destination To-Year must be greater than Destination From-Year");
            document.getElementById("DesToYear").focus();
            return false;
        }
    }


    var flag = false;
    var ch = document.getElementById("copyNotNull");
    if(ch.checked)
    {
        flag = true;
    }
    document.myForm.action = "pbCopyRangeData.jsp?flag="+flag;
    document.myForm.submit();
    return true;
}


