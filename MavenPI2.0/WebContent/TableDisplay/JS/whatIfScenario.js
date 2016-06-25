/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var pbReportId="";
var trgtIdsArray=new Array
var trgtNamesArray=new Array
var trgtformulaArray=new Array
var actualFormlaArray=new Array
var contextvar
var sensitivityVar = "";
var sensitivityname="";


function LTrim( value ) {

    var re = /\s*((\S+\s*)*)/;
    return value.replace(re, "$1");

}

// Removes ending whitespaces
function RTrim( value ) {

    var re = /((\s*\S+)*)\s*/;
    return value.replace(re, "$1");

}

// Removes leading and ending whitespaces
function trim( value ) {

    return LTrim(RTrim(value));
}
function openWhatIfContainer(whatIfMes,reportId,Measures,MeasuresNames,conPath,sliderValues){
    pbReportId=reportId;
    contextpath=conPath;
    try{
        $('#demoContainer').mb_open();
        if(whatIfMes.length <= 4){
            $('#demoContainer').mb_resizeTo(260,whatIfMes.length *245);
        }else if(whatIfMes.length >4 && whatIfMes <=10){
            $('#demoContainer').mb_resizeTo(450,whatIfMes.length *245);
        }
   
        var innerHtmlVar="";
        innerHtmlVar+="<div><br/></br>"
        innerHtmlVar+="<table align='center'>"
        innerHtmlVar+="<tr>"
        for(var i=0;i<whatIfMes.length;i++){
            innerHtmlVar+="<td>"
            innerHtmlVar+="<input type='text' id='"+trim(whatIfMes[i].toString())+"'  class='amount' style='border:0; color:#f6931f; font-weight:bold;' value='"+sliderValues[i]+"'/>"
            innerHtmlVar+="<div id='"+trim(whatIfMes[i].toString())+"S' class='slider-range-min'></div>"
            innerHtmlVar+="</td>"

        }

        innerHtmlVar+="</tr>"
        innerHtmlVar+="<tr>"
        for(var j=0;j<whatIfMes.length;j++){
                  
            for(var k=0;k<Measures.length;k++){
                if( trim(Measures[k].toString()) == trim(whatIfMes[j].toString()) ){
                    innerHtmlVar+="<td>  <label><font style='font-family:Verdana;color: black;'>"+MeasuresNames[k]+"</font></label></td>"
                    break;
                }
            }

        }
        innerHtmlVar+="</tr>"
        innerHtmlVar+="</table><br/><br/><br/><br/>"
        innerHtmlVar+="</div><br/><br/>"
        innerHtmlVar+="<div style='vertical-align: bottom' align='center'>"
        innerHtmlVar+="<table align='center'>"
        innerHtmlVar+="<tr>"
        innerHtmlVar+="<td width='50%'>"
        innerHtmlVar+=" <input type='button' id='whatifPreview' name='whatifPreview' value='Preview'  style='width: 100px' class='navtitle-hover' onclick='whatIfPreViewAndSave(\""+whatIfMes+"\",\""+conPath+"\",\"preview\")'>"
        innerHtmlVar+="</td>"
        innerHtmlVar+="<td>"
        innerHtmlVar+="</td>"
        innerHtmlVar+="<td width='50%'> "
        innerHtmlVar+=" <input type='button' id='whatifSave' name='whatifSave' value='Done' style='width: 100px' class='navtitle-hover' onclick='whatIfPreViewAndSave(\""+whatIfMes+"\",\""+conPath+"\",\"done\")'>"
        innerHtmlVar+="</td>"
        innerHtmlVar+="</tr>"
        innerHtmlVar+="</table>"
        innerHtmlVar+="</div>"
        $('#innerContainer').html(innerHtmlVar)
              
        $(".slider-range-min").slider({
            range: "min",
            value: 0,
            min: -100,
            max: 100,
            step: 1,


            slide: function(event, ui) {

                var tesxtboxid=$(this).attr('id')
                tesxtboxid=tesxtboxid.replace("S","")
                setWhatIfSliderValu(tesxtboxid)
                      
                       
            }
        });
    } catch(errr){
        alert(errr)
    }
// document.getElementById(demoContainer).innerHTML=innerHtmlVar
}
function closeWhatIfContainer(){
    $('#demoContainer').mb_close();

// document.getElementById("open").style.display=''
//document.getElementById("close").style.display='none'
// $('#actions').fadeOut();
// $(this).hide();


}
function setWhatIfSliderValu(tesxtboxid){
              
    $("#"+tesxtboxid).val( $("#"+tesxtboxid+"S").slider("value"));
//  $("#slidertollTip").title
//    document.getElementById("slidertollTip").title=$("#"+tesxtboxid+"S").slider("value")

}


function whatIfPreViewAndSave(whatIfMes,conPath,buttCheck){
  
    var whatIfMesVar=whatIfMes.split(",")
    var rangeValues="";
         
    for(var i=0;i<whatIfMesVar.length;i++){
        rangeValues+=","+$("#"+trim(whatIfMesVar[i].toString())).val();
        whatIfMesVar[i]=trim(whatIfMesVar[i].toString())
    }
    rangeValues=rangeValues.substring(1)
              
    $.ajax({
        url:'whatIfScenerioAction.do?whatIfParam=whatIfPreViewAndSave&PbReportId='+pbReportId+'&whatIfMeasuresliast='+whatIfMesVar.toString()+'&whatIfRanges='+rangeValues+'&buttCheck='+buttCheck,
        success: function(data){
           
            var source =conPath+"/TableDisplay/pbDisplay.jsp?tabId="+pbReportId
            var dSrc = document.getElementById("iframe1");
            dSrc.src = source;
            if(data!="1"){
                $('#demoContainer').mb_close();
            }

        }
    });
}

function setTargetMeasure(trgtMeasurename,trgtMeasures){
    if($("#trgtMesNameDisp").is("input")){
        $('#whatIfTrgtMesArea').val( $('#whatIfTrgtMesArea').val()+trim(trgtMeasurename))
        $('#whatIfTrgtmesHidden').val(trim($('#whatIfTrgtmesHidden').val())+trim(trgtMeasures))
    }else{
        if($("#trgtMesNameDisp").val()!=null && $("#trgtMesNameDisp").val()!=""){
            
            $('#whatIfTrgtMesArea').val( $('#whatIfTrgtMesArea').val()+trim(trgtMeasurename))
            $('#whatIfTrgtmesHidden').val(trim($('#whatIfTrgtmesHidden').val())+trim(trgtMeasures))
        }
    }
  
    
}
function setWhatifTarget(selectedMeasures,selectedMeasuresnames){
    //alert("selectedMeasures\t"+selectedMeasures)
    //alert("selectedMeasuresnames\t"+selectedMeasuresnames)
    $("#DefineTarget").dialog('open')
    buildWhatifUL(selectedMeasures,selectedMeasuresnames)
    //var trgtMeasures="sree"

    return ""
}

function buildWhatifTrgt(selectedMeasures,selectedMeasuresnames,ftype,conPath){

    //var dataArray=data.split("~");
    //var measuresArray=dataArray[0].split(",")
    //var measuresNameArray=dataArray[1].split(",");
    $("#DefineTarget").dialog('open')
    $("#trgtMesNameDisp").val("");
    clearTextArea()
    $("#trgtMeasuresUL").html("")
    var ulobj=document.getElementById("trgtMeasuresUL");

   
    //  alert("selectedMeasuresnames"+selectedMeasuresnames)
    for(var i=0;i<selectedMeasures.length;i++){
        var liObj=document.createElement("li")
        var tempid=trim(selectedMeasuresnames[i]);
        liObj.id=tempid.replace(" ","_", "gi")
        liObj.className="navtitle-hover"
        liObj.style.width='220px';
        liObj.style.height='22px';
        liObj.style.color='white';
        liObj.title="Click to select target measure "
        var spanStr="<a id='whatifTrgtMesA' href='javascript:parent.setTargetMeasure(\""+selectedMeasuresnames[i].toString()+"\",\""+selectedMeasures[i].toString()+"\")' ><span>"+selectedMeasuresnames[i]+"</span></a>"
        liObj.innerHTML=spanStr;
        ulobj.appendChild(liObj);

    }
    if(ftype=='Edit'){
        var reportId=$("#REPORTID").val()
        editTargetMes(reportId,conPath)
        $("#saveEditTarget").show()
    }else{
        rebuildingWhatIf()
    }
//  ulobj.appendChild(ulobj)
      

}
function WhatifOperators(smbl){
    $('#whatIfTrgtMesArea').val( trim($('#whatIfTrgtMesArea').val())+trim(smbl))
    $('#whatIfTrgtmesHidden').val(trim($('#whatIfTrgtmesHidden').val())+trim(smbl))
    $('#numberInput').val("")
}

function saveWhatIfTargetMes(reportId,contextpath){
    //document.getElementById("whatIfTrgtmesHidden").value
    var trgtMesVar=""
    if($("#trgtMesNameDisp").is("input")){
        trgtMesVar= $('#trgtMesNameDisp').val()
    }else{
        trgtMesVar= $('#trgtMesNameDisp :selected').text()
    }
    if($('#trgtMesNameDisp').val()!=""){
        if($('#whatIfTrgtmesHidden').val()!="" ){
            var trgtMesName=trgtMesVar
            var trgtFormula=$('#whatIfTrgtmesHidden').val().replace("+","~", "gi")
            trgtFormula=trgtFormula.replace("%","^","gi" )
            $.ajax({
                url:'whatIfScenerioAction.do?whatIfParam=addWhatIfTargetMeasure&PbReportId='+reportId+'&whatIfTargetMeasuresname='+trim(trgtMesName.toString())+'&whatIftrgtFormula='+trim(trgtFormula.toString()),
                success: function(data){
                    // $("#DefineTarget").dialog('close')
                    if(!$("#trgtMesNameDisp").is("input")){
                        
                        $("#trgtMesNameDisp").val($("#trgtMesNameDisp option:first").val());
                        $("#deleteTrgt").hide()
                        $("#reNameTrgt").hide()
                        clearTextArea()
                        $("#trgtMeasuresUL li").each(function(){
                            $(this).show()
                        })
                    }else{
                        $("#DefineTarget").dialog('close')
                    }
                    var source =contextpath+"/TableDisplay/pbDisplay.jsp?tabId="+reportId
                    var dSrc = document.getElementById("iframe1");
                    dSrc.src = source;
                    if(data!="1"){
                        $('#demoContainer').mb_close();
                        var dataJson=eval("("+data+")")
                        trgtIdsArray=dataJson.trgtIDs
                        trgtNamesArray=dataJson.trgtNames
                        trgtformulaArray=dataJson.trgtFormulas
                        actualFormlaArray=dataJson.actualFormula
                    }
                       
                }
            });
          
        }else{
            alert("please enter measure formula")
        }
        

    }else{
        if($("#trgtMesNameDisp").is("input"))
            alert("Please enter target measure name  ")
    }
    
    var source =contextpath+"/pbWhatIfscenario.jsp?PbReportId="+reportId
    var dSrc = document.getElementById("performWhatIfFrame");
    dSrc.src = source;

}
function saveEditTargetMes(reportId,contextpath){
    saveWhatIfTargetMes(reportId,contextpath)
    $("#DefineTarget").dialog('close')
}

function editTargetMes(reportId,contextpath)
{
    contextvar=contextpath
    $.ajax({
        url:'whatIfScenerioAction.do?whatIfParam=editWhatIfTargetMeasure&reportId='+reportId,
        success:function(data){
              
            var trgtList="<td align='right' >Target measure name</td><td align='left' width='10%'> <select name='tegtMesNameDisp' id='trgtMesNameDisp'\n\
            onchange='displayFormula()'></select></td> <td align='left' width='10%'><input type='button' id='deleteTrgt' name='deleteTrgt' value='Delete' class='navtitle-hover' onclick=deleteTrgt('"+reportId+"') style='display: none'></td>\n\
                    <td align='left' width='20%'><input type='button' id='reNameTrgt' name='reNameTrgt' value='Rename' class='navtitle-hover' onclick=renameTarget('"+reportId+"') style='display: none'></td>"
            $("#trgtMesNameDisp").parent().parent().html(trgtList)
            var dataJson=eval("("+data+")")
            trgtIdsArray=dataJson.trgtIDs
            trgtNamesArray=dataJson.trgtNames
            trgtformulaArray=dataJson.trgtFormulas
            actualFormlaArray=dataJson.actualFormula
            // alert("actualFormlaArray\t"+actualFormlaArray)
            var optionVar='';
            optionVar="<option value=''>---select---</option>";
            for(var i=0;i<dataJson.trgtIDs.length;i++){
                optionVar+="<option value='"+dataJson.trgtIDs[i]+"'>"+dataJson.trgtNames[i]+"</option>"

            }
            $("#trgtMesNameDisp").html(optionVar)
        }

    });
   
}
function renameTarget(reportID){
    var oldTrgtName=$("#trgtMesNameDisp").val()
    var newTrgtName = prompt("Enter new target measure name ");
    //alert("newTrgtName--"+newTrgtName+"---")
    if(newTrgtName !=null && newTrgtName != "" ){
        renameTargetMeasure(reportID,oldTrgtName,newTrgtName)
    }
        
   

}
function renameTargetMeasure(reportID,oldTrgtName,newTrgtName){
    $.ajax({
        url:'whatIfScenerioAction.do?whatIfParam=renameWhatIfTargetMeasure&reportId='+reportID+'&oldTrgtName='+oldTrgtName+'&newTrgtName='+newTrgtName,
        success:function(data){
            $('#trgtMesNameDisp :selected').text(newTrgtName);
            
            //alert("dg\t"+$("#trgtMeasuresUL li").get())
            var indexVar= jQuery.inArray(oldTrgtName,trgtIdsArray )
            //alert("==="+trgtNamesArray[indexVar]+"====")
            $("#trgtMeasuresUL > li > a > span ").each( function(){
               
                if(jQuery.trim($(this).html())==trgtNamesArray[indexVar]){
                    $(this).html(newTrgtName)
                    $('#'+trgtNamesArray[indexVar]).attr("id",newTrgtName);
                    trgtNamesArray[indexVar]=newTrgtName
                    return false;
                }
               
              

            })
            var source =contextvar+"/TableDisplay/pbDisplay.jsp?tabId="+reportID
            var dSrc = document.getElementById("iframe1");
            dSrc.src = source;
        }
    });
}
function deleteTrgt(reportID){
    var trgtName=$("#trgtMesNameDisp").val()
    var confirmVar=confirm("Deleting a target measure will also remove the measures dependant on this measure. ")
    if(confirmVar){
        $.ajax({
            url:'whatIfScenerioAction.do?whatIfParam=deleteWhatIfTargetMeasure&trgtName='+trgtName+'&reportID='+reportID,
            success:function(data){
                // alert("data\t"+data)
                var dataArray=data.split(",")
                var source =contextvar+"/TableDisplay/pbDisplay.jsp?tabId="+reportID
                var dSrc = document.getElementById("iframe1");
                dSrc.src = source;
                if(data==""){
                    $("#DefineTarget").dialog('close')
                    $("#performWhatIfDiv").dialog('close')
                }else{
                    for(var i=0;i<trgtIdsArray.length;i++)
                    {
                        var indexofTrgtNames= jQuery.inArray(trgtIdsArray[i],dataArray )
                        if(indexofTrgtNames==-1){
                            $("#trgtMesNameDisp option[value="+trgtIdsArray[i]+"]").remove();
                            $("#"+trgtNamesArray[i]).remove();
                        }
                     
                    }
                    $("#deleteTrgt").hide()
                    $("#reNameTrgt").hide()
                    clearTextArea()
                }
 
            }
        });
    }
  
}
function displayFormula(){
    var seleTrgt=$('#trgtMesNameDisp :selected').text();
    //    alert("oooooo\t"+jQuery.inArray(seleTrgt,trgtIdsArray ))
    //    alert("trgtNamesArray\t"+trgtIdsArray)
    $('#whatIfTrgtMesArea').val("")
    $('#whatIfTrgtmesHidden').val("")
    $("#whatIfTrgtMesArea").val(trgtformulaArray[jQuery.inArray(seleTrgt,trgtNamesArray )])
    $("#whatIfTrgtmesHidden").val(actualFormlaArray[jQuery.inArray(seleTrgt,trgtNamesArray )])
    $("#trgtMeasuresUL li").each(function(){
        // alert("seleTrgt\t"+seleTrgt)
        if($(this).attr("id")== seleTrgt){
            $(this).hide()
            $("#deleteTrgt").show()
            $("#reNameTrgt").show()
        }else if(seleTrgt=="---select---"){
            $("#deleteTrgt").hide()
            $("#reNameTrgt").hide()
            $("#trgtMeasuresUL li").each(function(){
                $(this).show()
            })
        }else{
            $(this).show()
            $("#deleteTrgt").show()
            $("#reNameTrgt").show()
        }
    })

}
function clearTextArea(){
    $('#whatIfTrgtMesArea').val("")
    $('#whatIfTrgtmesHidden').val("")
}
function isNumberevent(evt)
{
   
    var charCode = (evt.which) ? evt.which : evt.keyCode
    if(charCode==46 || charCode==44)
        return true;
    if (charCode > 31 && (charCode < 48 || charCode > 57))
        return false;

    return true;
}
function openSenSitivity(measureid,measureName,reportid,path){
    //alert("hi")
     sensitivityVar = null
    sensitivityname = null
    $("#sensitivityDiv").dialog('open')
    //      goNext(reportid,measureid,measureName)
    var sensitobj=document.getElementById("sensitivityiFrame");
    var source =path+'/TableDisplay/pbParamFilterMembers.jsp?reportId='+reportid+'&measElement='+measureid+'&disColumnName='+measureName +'&openSenSitivity=openSenSitivity&selectedParam='+measureid.replace("A_","");
    sensitobj.src=source
}
function setSensitivityFactor(value){
    //alert("value\t"+value)
    if(value.length !=0){
        $("#sensitivityFactorid").val($("#sensitivityFactorid").val()+","+ value)
        $("#sensitivityDiv").dialog('close')
    }else{
        $("#sensitivityDiv").dialog('close')
    }

}
function openEditSenSitivity(repID)
{
    $.ajax({
        url:'whatIfScenerioAction.do?whatIfParam=editSenSitivity&repID='+repID,
        success:function(data){
             //alert("data\t"+data)
            $("#editSensitivityFactor").html(data)
        }
    });
    $("#editSensitivityDiv").dialog('open')
}
function changeEditSensitivity(path,reportId){
   
    //      goNext(reportid,measureid,measureName)
    var measureidVal=$("#editSensitivityFactor").val().split("-")
    var measureid=measureidVal[0]
    sensitivityVar = measureidVal[1];
    sensitivityname =  measureidVal[2]
    if($("#editSensitivityFactor").val()!="none"){
         $("#sensitivityDiv").dialog('open')
        var sensitobj=document.getElementById("sensitivityiFrame");
        var source =path+'/TableDisplay/pbParamFilterMembers.jsp?reportId='+reportId+'&measElement=&disColumnName=&openSenSitivity=openSenSitivity&selectedParam='+measureid.replace("A_","");
        sensitobj.src=source
        $("#editSensitivityDiv").dialog('close')
    }else
        alert("Please select  Sensitivity Factor")
}
  
function cancelEditSensitivit(){
    $("#editSensitivityDiv").dialog('close')
}
