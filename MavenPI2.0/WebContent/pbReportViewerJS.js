/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

    
$(document).ready(function(){
    $("#msgframe").dialog({
        bgiframe: true,
        autoOpen: false,
        height: 300,
        modal: true,
        Cancel: function() {
            $(this).dialog('close');
        }
    });
    $("#cstLinksFrame").dialog({
        bgiframe: true,
        autoOpen: false,
        height: 300,
        width: 700,
        modal: true,
        Cancel: function() {
            $(this).dialog('close');
        }
    });
    $("#prtLinksFrame").dialog({
        bgiframe: true,
        autoOpen: false,
        height: 300,
        modal: true,
        Cancel: function() {
            $(this).dialog('close');
        }
    });
    $("#Scheduler").dialog({
        bgiframe: true,
        autoOpen: false,
        height: 300,
        modal: true
    });
    $("#tracker").dialog({
        bgiframe: true,
        autoOpen: false,
        height: 300,
        modal: true,
        Cancel: function() {
            $(this).dialog('close');
        }
    });

    

    $('#defineSchedulerDiv').click(function() {
        $('#reportSchedulerDialog').dialog('open');
    });
    $('#defineTrackerDiv').click(function() {
        $('#tracker').dialog('open');
    });
    $('#composeMessageDiv').click(function() {
      //  alert('hi i message')
        $('#composeMessageDialog').dialog('open');
    });

    $('#Customize').click(function() {
       
        $('#favLinksDialog').dialog('open');
    });

    $('#Prioritize').click(function() {
        $('#prtLinksFrame').dialog('open');
    });
    $('#sanpShotDiv').click(function() {
         
        $('#snapShotDialog').dialog('open');
    });

    $test=$(".navtitle");

    $test.hover(
        function(){
            this.style.background="#308DBB";
            this.style.color="#fff";
        },
        function(){
            this.style.background="#BDBDBD";
            this.style.color="#000"
        }
        );
    $test=$(".ui-state-default ");

    $test.hover(
        function(){
            this.style.background="#454545";
            this.style.color="white";
        },
        function(){
            this.style.background="#e6e6e6";
            this.style.color="#000"
        }

        );

    $("#favLinksDialog").dialog({
        //bgiframe: true,
        autoOpen: false,
        height: 520,
        width: 660,
        position: 'top',
        modal: true
    });

    $("#composeMessageDialog").dialog({
        //bgiframe: true,
        autoOpen: false,
        height: 420,
        width: 720,
        position: 'top',
        modal: true
    });

    $("#snapShotDialog").dialog({
        //bgiframe: true,
        autoOpen: false,
        height: 300,
        width: 600,
        position: 'top',
        modal: true
    });

    $("#reportSchedulerDialog").dialog({
        //bgiframe: true,
        autoOpen: false,
        height: 380,
        width: 680,
        position: 'top',
        modal: true
    });


});
       
function displayfavlink(){
    $("#favlinkcont").toggle({
        persist: "cookie"
    });
}
function displayWidgets(){
    $("#Widgets").toggle({
        persist: "cookie"
    });
}
function dispParameters(){
    $("#tabParameters").toggle({
        persist: "cookie"
    });
}
function dispGraphs(){
    $("#tabGraphs").toggle({
        persist: "cookie"
    });
}
function dispTables(){
    $("#tabTable").toggle({
        persist: "cookie"
    });
    function dispMessages(){
        $("#messages").toggle({
            persist: "cookie"
        });
    }
    function dispSnapShots(){
         $("#snapshots").toggle({
            persist: "cookie"
        });
    }

}
function kpiOverWriteReport(ctxPath,reportId){
//    var res=confirm("Do you want to over write the report ?");
//    if(res){

var reportName=document.getElementById("Name").value;
var reportDesc=document.getElementById("Desc").value;

//alert(reportName)
var Gtregion;
if($("#grandTotalDiv").is(':visible')){
    Gtregion=true;}
else{
    Gtregion=false;
}
  var autometicDate;
     if($("#autometicDate").is(':checked'))
             autometicDate=true;
        else
             autometicDate=false;

        var Date=""
        if(document.getElementById("sysDate") != null && document.getElementById("sysDate").checked){
           Date="systemDate"
        }else if(document.getElementById("reportDate") != null && document.getElementById("reportDate").checked){
            Date="reportDate"
        }if(document.getElementById("currdetails") != null && document.getElementById("currdetails").checked){
           Date="currdetails"
        }else if(document.getElementById("yestrday") != null && document.getElementById("yestrday").checked){
            Date = "yestrday"
        }else if(document.getElementById("tomorow") != null && document.getElementById("tomorow").checked){
            Date = "tomorow"
        }else if(document.getElementById("newSysDate") != null && document.getElementById("newSysDate").checked){
            var sysSign = $("#sysSign").val();
            var newSysVal = $("#newSysVal").val();
            if(newSysVal==""){
                newSysVal="0";
            }
            Date = "newSysDate,".concat(sysSign,",", newSysVal)
            Date = Date.toString().replace(" ", "+", "gi");
        }else if(document.getElementById("globalDate") != null && document.getElementById("globalDate").checked){
            var globalSign = $("#globalSign").val();
            var newGlobVal = $("#newGlobVal").val();
            if(newGlobVal==""){
                newGlobVal="0";
            }
            Date = "globalDate,".concat(globalSign,",", newGlobVal)
            Date = Date.toString().replace(" ", "+", "gi");
        }
        if(document.getElementById("customDate") != null && document.getElementById("customDate").checked){
            var frmDate = "";
            var toDate = "";
            var cmpfrmDate = "";
            var cmptoDate = "";
        if(document.getElementById("fromyestrday") != null && document.getElementById("fromyestrday").checked){
//            var fromToSign = $("#fromToSign").val();
//            var fromToVal = $("#fromToVal").val();
//            //Date = "fromToDate,".concat(fromToSign,",", fromToVal)
//           // Date = Date.toString().replace(" ", "+", "gi");
           Date = "fromyestrday";
        }else if(document.getElementById("fromtomorow") != null && document.getElementById("fromtomorow").checked){
            Date="fromtomorow"
        }else if(document.getElementById("fromSysDate") != null && document.getElementById("fromSysDate").checked){
            var fromSysSign = $("#fromSysSign").val();
            var fromSysVal = $("#fromSysVal").val();
            if(fromSysVal=="")
               fromSysVal = "0";
            Date = "fromSysDate,".concat(fromSysSign,",", fromSysVal)
            Date = Date.toString().replace(" ", "+", "gi");
        }else if(document.getElementById("fromglobalDate") != null && document.getElementById("fromglobalDate").checked){
            var fromglobalSign = $("#fromglobalSign").val();
            var fromGlobVal = $("#fromGlobVal").val();
            if(fromGlobVal=="")
               fromGlobVal = "0";
            Date = "fromglobalDate,".concat(fromglobalSign,",", fromGlobVal)
            Date = Date.toString().replace(" ", "+", "gi");
        }
        frmDate = Date;
        if(document.getElementById("toyestrday") != null && document.getElementById("toyestrday").checked){
           Date = frmDate+"@toyestrday";
        }else if(document.getElementById("totomorow") != null && document.getElementById("totomorow").checked){
           Date = frmDate+"@totomorow";
        }else if(document.getElementById("toSystDate") != null && document.getElementById("toSystDate").checked){
            var toSysSign = $("#toSysSign").val();
            var toSysVal = $("#toSysVal").val();
            if(toSysVal==""){
            toSysVal = "0"
            }
            Date = "toSystDate,".concat(toSysSign,",", toSysVal)
           Date = frmDate+"@"+Date.toString().replace(" ", "+", "gi");
        }else if(document.getElementById("toglobalDdate") != null && document.getElementById("toglobalDdate").checked){
            var toglobalSign = $("#toglobalSign").val();
            var toGlobVal = $("#toGlobVal").val();
            if(toGlobVal=="")
               toGlobVal = "0";
            Date = "toglobalDdate,".concat(toglobalSign,",", toGlobVal)
           Date = frmDate+"@"+Date.toString().replace(" ", "+", "gi");
        }
        toDate = Date;
        if(document.getElementById("CmpFrmyestrday") != null && document.getElementById("CmpFrmyestrday").checked){
           Date = toDate+"@CmpFrmyestrday";
        }else if(document.getElementById("CmpFrmtomorow") != null && document.getElementById("CmpFrmtomorow").checked){
            Date = toDate+"@CmpFrmtomorow"
        }else if(document.getElementById("CmpFrmSysDate") != null && document.getElementById("CmpFrmSysDate").checked){
            var CmpFrmSysSign = $("#CmpFrmSysSign").val();
            var CmpFrmSysVal = $("#CmpFrmSysVal").val();
            if(CmpFrmSysVal==""){
                CmpFrmSysVal = "0";
            }
            Date = "CmpFrmSysDate,".concat(CmpFrmSysSign,",", CmpFrmSysVal)
            Date = toDate+"@"+Date.toString().replace(" ", "+", "gi");
        }else if(document.getElementById("CmpFrmglobalDate") != null && document.getElementById("CmpFrmglobalDate").checked){
            var CmpFrmglobalSign = $("#CmpFrmglobalSign").val();
            var CmpFrmGlobVal = $("#CmpFrmGlobVal").val();
            if(CmpFrmGlobVal=="")
               CmpFrmGlobVal = "0";
            Date = "CmpFrmglobalDate,".concat(CmpFrmglobalSign,",", CmpFrmGlobVal)
            Date = toDate+"@"+Date.toString().replace(" ", "+", "gi");
        }
        cmpfrmDate = Date;
        if(document.getElementById("cmptoyestrday") != null && document.getElementById("cmptoyestrday").checked){
           Date = cmpfrmDate+"@cmptoyestrday";
        }else if(document.getElementById("cmptotomorow") != null && document.getElementById("cmptotomorow").checked){
            Date = cmpfrmDate+"@cmptotomorow"
        }else if(document.getElementById("cmptoSysDate") != null && document.getElementById("cmptoSysDate").checked){
            var cmptoSysSign = $("#cmptoSysSign").val();
            var cmptoSysVal = $("#cmptoSysVal").val();
            if(cmptoSysVal==""){
                cmptoSysVal = "0";
            }
            Date = "cmptoSysDate,".concat(cmptoSysSign,",", cmptoSysVal)
            Date = cmpfrmDate+"@"+Date.toString().replace(" ", "+", "gi");
        }else if(document.getElementById("cmptoglobalDate") != null && document.getElementById("cmptoglobalDate").checked){
            var cmptoglobalSign = $("#cmptoglobalSign").val();
            var cmptoGlobVal = $("#cmptoGlobVal").val();
             if(cmptoGlobVal=="")
               cmptoGlobVal = "0";
            Date = "cmptoglobalDate,".concat(cmptoglobalSign,",", cmptoGlobVal)
            Date = cmpfrmDate+"@"+Date.toString().replace(" ", "+", "gi");
        }
        cmptoDate = Date;
        Date = cmptoDate;
    }
        if(flag=='overWrite'){
//      added by manik
//      document.forms.frmParameter.action = ctxPath+"/reportViewer.do?reportBy=saveAsNewReport&newReportName="+encodeURIComponent(reportName)+"&REPORTDESC="+reportDesc+"&autometicDate="+autometicDate+"&overWriteExistingReport=true&Date="+Date+"&Gtregion="+Gtregion;
        document.forms.submitReportForm.action = ctxPath+"/reportViewer.do?reportBy=saveAsNewReport&newReportName="+encodeURIComponent(reportName)+"&REPORTDESC="+reportDesc+"&autometicDate="+autometicDate+"&overWriteExistingReport=true&Date="+Date+"&Gtregion="+Gtregion+"&REPORTID="+reportId+"&isKPIDashboard=true";
        document.forms.submitReportForm.method="POST";
        document.forms.submitReportForm.submit();
        }
        else if(flag=='time'){
//            document.forms.frmParameter.action=ctxPath+"/reportViewer.do?reportBy=saveTimeDetails&newReportName="+encodeURIComponent(reportName)+"&REPORTDESC="+reportDesc+"&overWriteExistingReport=true&Date="+Date;
//            document.forms.frmParameter.method="POST";
            //document.forms.frmParameter.submit();
             $.post(
                  ctxPath+"/reportViewer.do?reportBy=saveTimeDetails&newReportName="+encodeURIComponent(reportName)+"&REPORTDESC="+reportDesc+"&overWriteExistingReport=true&Date="+Date+"&reportId="+reportId,
             function(data){
                 if(data){
                     alert("TimeDetails Saved Successfully");
                 }

             });
        }
        $("#overWriteReport").dialog('close');


//    }
}
