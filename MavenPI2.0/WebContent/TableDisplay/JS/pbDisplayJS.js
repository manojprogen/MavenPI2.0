/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


//This function is written by praveen for applying colors in pbdisplay.jsp

var w=0;
function formatStr(EL,maxchars)
{
    var aObj=EL.getElementsByTagName("a");
    if( aObj!=null && aObj!=undefined && aObj.length > 0 ){
        strbuff=aObj[0].innerHTML;
        //strbuff=EL.innerHTML;
        newstr='';
        startI = 0;
        max=maxchars;
        str='';
        subarr=new Array(parseInt(strbuff.length/max+1));
        for (i=0;i<subarr.length;i++)
        {
            subarr[i]=strbuff.substr(startI,max);
            startI+=max;
        }
        for (i=0;i<subarr.length-1;i++)
        {
            newstr+=subarr[i]+'<br/>';
        }
        str+=subarr[subarr.length-1];
        if(subarr.length==1){
            aObj[0].innerHTML=aObj[0].innerHTML;
        }else{
            aObj[0].innerHTML=newstr;
        }
    }else{
        strbuff=EL.innerHTML;
        newstr='';
        startI = 0;
        max=maxchars;
        str='';
        subarr=new Array(parseInt(strbuff.length/max+1));
        for (i=0;i<subarr.length;i++)
        {
            subarr[i]=strbuff.substr(startI,max);
            startI+=max;
        }
        for (i=0;i<subarr.length-1;i++)
        {
            newstr+=subarr[i]+'<br/>';
        }
        str+=subarr[subarr.length-1];
        if(subarr.length==1){
            EL.innerHTML=EL.innerHTML;
        }else{
            EL.innerHTML=newstr;
        }
    }
}
$(window).load(function(){
    if(checkBrowser() == "ie")
    {
       $("#DefinecustSeqDialog").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
    }else{
        $("#DefinecustSeqDialog").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
    }
});
function ctDriil(colName){

}
function transposeTable(id) {
    var table = document.getElementById(id);
    var tbody = table.tBodies[0];
    var rows = tbody.rows;
    var numRows = rows.length;
    var numCells = rows[0].cells.length;
    var i, j, k;

    for(var r=0; r<numRows; r++) {
        var currentRow = rows[r];

        for (var c=r+1; c<numCells; c++) {

            // skip diagonal cells
            if (c != r) {

                // Get the current (upper) cell to swap
                var currentCell = currentRow.cells[c];

                // Get the swap (lower) cell
                var swapRow = rows[c];
                var swapCell = swapRow && swapRow.cells[r]


                // If both exist, swap them
                if (currentCell && swapCell) {
                    currentRow.insertBefore(swapCell, currentCell);
                    swapRow.insertBefore(currentCell, swapRow.cells[r]);
                }

                // If current exists but not swap, keep adding cells
                // from current row. Add rows if required
                else if (currentCell) {
                    k = c;

                    do {

                        if (!swapRow) {
                            swapRow = currentRow.cloneNode(false);
                            tbody.appendChild(swapRow);
                        }

                        swapRow.appendChild(currentRow.cells[k]);
                        swapRow = rows[swapRow.rowIndex + 1];

                    // Increment counter to shortcut loop when finished
                    } while (++c < numCells)
                }
            }
        }

        // If there are more rows than cells, add cells to
        // appropriate rows
        if (r > numCells - 1) {

            // Keep current row index
            j = r;

            while (currentRow) {
                i = 0;

                // Add cells from lower rows to upper rows
                do {
                    rows[i].appendChild(currentRow.cells[0]);
                } while (++i < numCells)

                // Remove empty rows along the way
                tbody.removeChild(currentRow);
                currentRow = rows[j];

                // Increment counter to shortcut loop when finished
                r++;
            }
        }
    }
}


//commented by Mayank... Please check very seriously...URGENT !!
//function showTableProperties(reportId,ctxPath,pageSize){
////alert(pageSize);
//    parent.showTableProperties(reportId,ctxPath,pageSize)
//}

function selectDrillAcrossReport(elementId, reportId, ctxPath){

    var drillAcrossSelectObj=document.getElementsByName("drillAcross");
    var applicable = false;

    for(var i=0;i<drillAcrossSelectObj.length;i++){
        if(drillAcrossSelectObj[i].checked){
            applicable = true;
            break;
        }
        else{
            drillAcrossSelectObj[i].checked=false;
        }
    }

    if (applicable == false){
        alert("Please select atleast one row");
    }
    else{
        $.ajax({
            url:ctxPath+'/reportTemplateAction.do?templateParam=getReportsForDrillAcross&reportId='+reportId,
            success:function(data){
                 var html="";
                 var dataJson = eval('('+data+')');

                 for (var i=0;i<dataJson.length;i++){
                    var reportDetails = dataJson[i];
                    var repId=reportDetails.reportId;
                    var reportName=(reportDetails.reportName);
                    html=html+"<option value=\""+repId+"\">"+reportName+"</option>";
                 }
                 $("#drillAcrossReport").html(html);
            }
        });

        $("#drillAcrossDiv").dialog('open');
    }
}

function doDrillAcross(reportId, ctxPath){
    var drillAcrossSelectObj=document.getElementsByName("drillAcross");
    var drillAcrossItems = new Array();

    for(var i=0;i<drillAcrossSelectObj.length;i++){
        if(drillAcrossSelectObj[i].checked){
            drillAcrossItems.push(drillAcrossSelectObj[i].value);
        }
        else{
            drillAcrossSelectObj[i].checked=false;
        }
    }

    var drillAcrossRpt = $("#drillAcrossReport").val();
    //alert("current Rpt : "+reportId + "Target Report :"+drillAcrossRpt+" Params : "+drillAcrossItems);

    parent.doDrillAcross(reportId, ctxPath, drillAcrossRpt, drillAcrossItems);
}

function drill(elementId, reportId, ctxPath){

    var drillAcrossSelectObj=document.getElementsByName("drillAcross");
    var drillAcrossItems = new Array();

    for(var i=0;i<drillAcrossSelectObj.length;i++){
        if(drillAcrossSelectObj[i].checked){
            drillAcrossItems.push(drillAcrossSelectObj[i].value);
        }
        else{
            drillAcrossSelectObj[i].checked=false;
        }
    }

    //alert(elementId+"  "+reportId+"   "+ctxPath+" "+drillAcrossItems);
    if (drillAcrossItems.length == 0){
        alert("Please select atleast one row");
    }
    else{
        $.ajax({
            url:ctxPath+"/reportTemplateAction.do?templateParam=drill&reportId="+reportId+"&params="+drillAcrossItems+"&elementId="+elementId,
            success : function(data){
                //alert(data);
                parent.doDrill(reportId, ctxPath, data);
            }
        });
    }
}

//function to show table scrolling
function ScrollableTable (tableEl, tableHeight, tableWidth) {

    this.initIEengine = function () {

        this.containerEl.style.overflowY = 'auto';
        if (this.tableEl.parentElement.clientHeight - this.tableEl.offsetHeight < 0) {
            this.tableEl.style.width = this.newWidth - this.scrollWidth +'px';
        } else {
            this.containerEl.style.overflowY = 'hidden';
            this.tableEl.style.width = this.newWidth +'px';
        }

        if (this.thead) {
            var trs = this.thead.getElementsByTagName('tr');
            for (x=0; x<trs.length; x++) {
                trs[x].style.position ='relative';
                trs[x].style.setExpression("top", "this.parentElement.parentElement.parentElement.scrollTop + 'px'");
            }
        }

        if (this.tfoot) {
            var trs = this.tfoot.getElementsByTagName('tr');
            for (x=0; x<trs.length; x++) {
                trs[x].style.position ='relative';
                trs[x].style.setExpression("bottom", "(this.parentElement.parentElement.offsetHeight - this.parentElement.parentElement.parentElement.clientHeight - this.parentElement.parentElement.parentElement.scrollTop) + 'px'");
            }
        }

        eval("window.attachEvent('onresize', function () { document.getElementById('" + this.tableEl.id + "').style.visibility = 'hidden'; document.getElementById('" + this.tableEl.id + "').style.visibility = 'visible'; } )");
    };


    this.initFFengine = function () {
        this.containerEl.style.overflow = 'hidden';
        this.tableEl.style.width = this.newWidth + 'px';

        var headHeight = (this.thead) ? this.thead.clientHeight : 0;
        var footHeight = (this.tfoot) ? this.tfoot.clientHeight : 0;
        var bodyHeight = this.tbody.clientHeight;
        var trs = this.tbody.getElementsByTagName('tr');
        if (bodyHeight >= (this.newHeight - (headHeight + footHeight))) {
            this.tbody.style.overflow = '-moz-scrollbars-vertical';
            for (x=0; x<trs.length; x++) {
                var tds = trs[x].getElementsByTagName('td');
                tds[tds.length-1].style.paddingRight += this.scrollWidth + 'px';
            }
        } else {
            this.tbody.style.overflow = '-moz-scrollbars-none';
        }

        var cellSpacing = (this.tableEl.offsetHeight - (this.tbody.clientHeight + headHeight + footHeight)) / 4;
        this.tbody.style.height = (this.newHeight - (headHeight + cellSpacing * 2) - (footHeight + cellSpacing * 2)) + 'px';

    };

    this.tableEl = tableEl;
    this.scrollWidth = 16;

    this.originalHeight = this.tableEl.clientHeight;
    this.originalWidth = this.tableEl.clientWidth;

    this.newHeight = parseInt(tableHeight);
    this.newWidth = tableWidth ? parseInt(tableWidth) : this.originalWidth;

    this.tableEl.style.height = 'auto';
    this.tableEl.removeAttribute('height');

    this.containerEl = this.tableEl.parentNode.insertBefore(document.createElement('div'), this.tableEl);
    this.containerEl.appendChild(this.tableEl);
    this.containerEl.style.height = this.newHeight + 'px';
    this.containerEl.style.width = this.newWidth + 'px';


    var thead = this.tableEl.getElementsByTagName('thead');
    this.thead = (thead[0]) ? thead[0] : null;

    var tfoot = this.tableEl.getElementsByTagName('tfoot');
    this.tfoot = (tfoot[0]) ? tfoot[0] : null;

    var tbody = this.tableEl.getElementsByTagName('tbody');
    this.tbody = (tbody[0]) ? tbody[0] : null;

    if (!this.tbody) return;

    if (document.all && document.getElementById && !window.opera) this.initIEengine();
    if (!document.all && document.getElementById && !window.opera) this.initFFengine();


}
//itext
function iText(disColumnName,colName){
    parent.iText(disColumnName,colName);
}

function grpByParamAnalysis(reportId,viewbyId,viewbyName,ctxPath){
    //  divmenuId.style.display='none';
    // alert('hi ')
    parent.grpByParamAnalysis(reportId,viewbyId,viewbyName,ctxPath)
}


$test1=$(".prgtable");
$test1.hover(
    function() {
        $(this).addClass('tableHover');
    },
    function() {
        $(this).removeClass('tableHover');
    }
    );
function dispChangeTableColumns(ctxPath,bizRoles,reportId){
//        $.ajax({
//       url:ctxPath+'/reportViewer.do?reportBy=chkforUserPrevilages',
//       success:function(data){
//         var jsonVar=eval('('+data+')')
//         var userType=jsonVar.userType;
//         //var isPowerAnalyserEnableforUser=jsonVar.isPAEnableforUser;
//      if(userType=="Admin"){
            $.ajax({
        url:ctxPath+'/reportViewer.do?reportBy=loadDialogs&loadDialogs=true&currentReportId='+reportId+'&currentBizRoles='+bizRoles,
        success:function(data) {
            parent.$("#tableColsDialog").dialog('open');
           parent.document.getElementById("tableColsFrame").src="TableDisplay/PbChangeTableColumnsRT.jsp?loadDialogs=true&currentReportId="+reportId+"&currentBizRoles="+bizRoles+"&from=true";

        }
            });
//    }else{
//              alert("You do not have the sufficient previlages")
//           }
//       }});
    }
function addCustomMeasure1(ctxPath,reportId)
{
//    $.ajax({
//       url:ctxPath+'/reportViewer.do?reportBy=chkforUserPrevilages',
//       success:function(data){
//            var jsonVar=eval('('+data+')')
//            var userType=jsonVar.userType;
//            var isPowerAnalyserEnableforUser=jsonVar.isPAEnableforUser;
//           if(userType=="Admin" || isPowerAnalyserEnableforUser=="true"){
                parent.addCustomMeasure(reportId);
//           }else{
//              alert("You do not have the sufficient previlages")
//           }
//       }});
           }

/*
 function dispRoundings(dispRoundings){
    var dispRoundingsObj=document.getElementById(dispRoundings);
    if(dispRoundingsObj.style.display=="none"){
        dispRoundingsObj.style.display="";
    }else{
        dispRoundingsObj.style.display="none";
    }
}
 */

function showSqlQuery(sqlStr){ //changed by sruthi to display full query
var data;
     parent.$("#showSqlStrDialog").dialog('open');
  //data=document.getElementById("showSqlBox").value;
    // var data1=parent.document.getElementById("showSqlboxfull").value;
    // alert("Hello"+data1);
    var data1=sqlStr;
    
    // alert("hiiii"+data);
     var html="";
    if(data1!=""){
          html=html+"<table align='center'><tr><td><input id='fullquery' type='checkbox' checked name='fullquery' value='fullquery' onclick='showSqlQueryfull1()'>Full Query</td>";
          html=html+"<td><input id='Aoquery' type='checkbox'  name='Aoquery' value='Aoquery'onclick='showSqlQuery1()'>Ao Query</td></tr></table>"
      parent.$("#showSqlStrDialog").html(html+data1);
     }else{
  html=html+"<table align='center'><tr><td><input id='fullquery' type='checkbox'  name='fullquery' value='fullquery' onclick='showSqlQueryfull1()'>Full Query</td>";
          html=html+"<td><input id='Aoquery' type='checkbox' checked name='Aoquery' value='Aoquery'onclick='showSqlQuery1()'>Ao Query</td></tr></table>"
      parent.$("#showSqlStrDialog").html(html+data1);
}
}
//added by sruthi to display the full query
function showSqlQueryfull1(){
      var html="";
      var query=parent.document.getElementById("showSqlboxfull").value;
          html=html+"<table align='center'><tr><td><input id='fullquery' type='checkbox' checked name='fullquery' value='fullquery' onclick='showSqlQueryfull1()'>Full Query</td>";
          html=html+"<td><input id='Aoquery' type='checkbox'   name='Aoquery' value='Aoquery'onclick='showSqlQuery1()'>Ao Query</td></tr></table>"
           parent.$("#showSqlStrDialog").html(html+query);
}
function showSqlQuery1()
{   var html="";
    var data=document.getElementById("showSqlboxquery").value
          html=html+"<table align='center'><tr><td><input id='fullquery' type='checkbox'  name='fullquery' value='fullquery' onclick='showSqlQueryfull1()'>Full Query</td>";
          html=html+"<td><input id='Aoquery' type='checkbox'  checked name='Aoquery' value='Aoquery'onclick='showSqlQuery1()'>Ao Query</td></tr></table>"
       parent.$("#showSqlStrDialog").html(html+data);
}//ended  by sruthi
function openWhatIfDiloge(ctxPath,reportId){

//      $.ajax({
//       url:ctxPath+'/reportViewer.do?reportBy=chkforUserPrevilages',
//       success:function(data){
//            var jsonVar=eval('('+data+')')
//            var userType=jsonVar.userType;
//            var isPowerAnalyserEnableforUser=jsonVar.isPAEnableforUser;
//           if(userType=="Admin" || isPowerAnalyserEnableforUser=="true"){
                parent.closeWhatIfContainer()
    parent.$("#performWhatIfDiv").dialog('open')
      parent.document.getElementById("performWhatIfFrame").src="pbWhatIfscenario.jsp?PbReportId="+reportId;
//           }else{
//              alert("You do not have the sufficient previlages")
//           }
//       }});



}

function changeDisplayStyle(ctxPath,reportId){

      $.post(ctxPath+'/reportViewer.do?reportBy=changeTableDisplayStyle&reportid='+reportId, $("#myForm").serialize() ,
         function(data){
            if (data != null && data != "")
                parent.refreshReportTables(ctxPath,reportId);
    });
//    $.ajax({
//        url:ctxPath+'/reportViewer.do?reportBy=changeTableDisplayStyle&reportid='+reportId,
//        success : function(data){
//            if (data != null && data != "")
//                parent.refreshReportTables(ctxPath,reportId);
//        }
//    });

}

function setExcelWidth(){
}
function viewAdhocDrill(reportId,ctxPath,nextViewById,measureName,viewById,presentViewById,adhocDrillType,url,paramurl){
    //alert(measureName)
    var presentViewBy=presentViewById.replace(/\A_/g,'')
    // alert(presentViewBy)
    var drillAcrossSelectObj=document.getElementsByName("drillAcross");
        var drillAcrossItems = new Array();
        for(var i=0;i<drillAcrossSelectObj.length;i++){
            if(drillAcrossSelectObj[i].checked){
               drillAcrossItems.push(drillAcrossSelectObj[i].value);
            }
            else{
               drillAcrossSelectObj[i].checked=false;
            }
        }
    if(adhocDrillType=="drillside"){
        //alert(url)
        $.post(ctxPath+"/reportViewer.do?reportBy=adhocChangeViewBy&newViewById="+nextViewById+"&reportId="+reportId+"&Type=tbody",
            function(data){
                if(nextViewById=="Day" || nextViewById=="Week" || nextViewById=="Month" || nextViewById=="Qtr" || nextViewById=="Year"){
                    if(url.indexOf("CBO_PRG_PERIOD_TYPE")!=-1)
                    {
                        var sideurl=null;
                        var n1=url.indexOf("&CBO_PRG_PERIOD_TYPE");
                        var n2=url.indexOf("&DDrill=Y");
                        sideurl+=url.substring(0,n1)+"&CBO_PRG_PERIOD_TYPE="+nextViewById+url.substr(n2);
                        url=sideurl.replace("null","");
                    }else{
                        url+="&CBO_PRG_PERIOD_TYPE="+nextViewById+"&DDrill=Y";
                    }
                }
                // var path=ctxPath+"/reportViewer.do?reportBy=viewReport&action=ChangeViewBy&adhocChangeViewby=true&REPORTID="+reportId +"&CBOARP" + presentViewBy + "="+measureName;
                if (drillAcrossItems.length == 0){
                var path=ctxPath+"/reportViewer.do?reportBy=viewReport&action=ChangeViewBy&adhocChangeViewby=true&REPORTID="+reportId+url+"&adhocviewby=true";
                // var path=ctxPath+"/reportViewer.do?reportBy=viewReport&action=ChangeViewBy&adhocChangeViewby=true&REPORTID="+reportId +"&DDrill=Y&DrillMonth=Apr-2011";
                if(isreportdrillPopUp == true || isreportdrillPopUp == 'true'){
                        parent.submiturlsPopUp(path);
                    }else{
                parent.submiturls(path);
                }
//                parent.submiturls(path);
                }else{
//                    $.post(ctxPath+"/reportViewer.do?reportBy=multiSelectAdhocDrillDown&drillAcrossItems="+encodeURIComponent(drillAcrossItems),
//                    function(data){
//                        if(data!='null'){
                             var msrNamesUrl="&CBOARP";
                             var cboarpDrill=new Array();
                             for(var i=0;i<drillAcrossItems.length;i++){
                                var temp=drillAcrossItems[i].replace("A_","");
                                var tempArr=temp.split(":");
                                cboarpDrill.push(tempArr[1]);
                                if(msrNamesUrl.indexOf(tempArr[0]) == -1){
                                    msrNamesUrl+=tempArr[0]+"=";
                                }

                             }
                             msrNamesUrl +=JSON.stringify(cboarpDrill);
//                             alert(msrNamesUrl)
                             var path=ctxPath+"/reportViewer.do?reportBy=viewReport&action=ChangeViewBy&adhocChangeViewby=true&REPORTID="+reportId+msrNamesUrl+"&adhocviewby=true";
                             if(isreportdrillPopUp == true || isreportdrillPopUp == 'true'){
                                    parent.submiturlsPopUp(path);
                                }else{
                             parent.submiturls(path);
                        }
//                             parent.submiturls(path);
//                        }

//            });

                }

            });
    }else{
        var paramUrl=paramurl;
        if(paramUrl.indexOf(presentViewBy)!=-1)
        {
            if(nextViewById=="Day" || nextViewById=="Week" || nextViewById=="Month" || nextViewById=="Qtr" || nextViewById=="Year")
                paramUrl=paramUrl.replace(presentViewBy,"TIME");
            else
                paramUrl=paramUrl.replace(presentViewBy,nextViewById);
        }else{
            paramUrl="&CBOVIEW_BY" + presentViewBy + "=" + nextViewById
        }
        var path;
        var msrNamesUrl="&CBOARP";
         var cboarpDrill=new Array();
        for(var i=0;i<drillAcrossItems.length;i++){
            var temp=drillAcrossItems[i].replace("A_","");
            var tempArr=temp.split(":");
           cboarpDrill.push(tempArr[1]);
           if(msrNamesUrl.indexOf(tempArr[0]) == -1){
                 msrNamesUrl+=tempArr[0]+"=";
        }
        }
        msrNamesUrl +=JSON.stringify(cboarpDrill);
//        alert('msrNamesUrl----'+msrNamesUrl)
        if(nextViewById=="Time Drill")
            path=url+measureName;
        else if(presentViewBy=="TIME"){
            if(nextViewById=="Day" || nextViewById=="Week" || nextViewById=="Month" || nextViewById=="Qtr" || nextViewById=="Year"){
                //alert(nextViewById)
                path=null;
                var arr=new Array;
                arr=url.split("&");
                for(var i=0;i<arr.length;i++){
                    if(arr[i].indexOf("CBO_PRG_PERIOD_TYPE")!=-1){
                        path+="&CBO_PRG_PERIOD_TYPE="+nextViewById;
                    }else
                        path+="&"+arr[i];
                }
                path+=measureName;
                // path+=measureName+"&CBOVIEW_BY" + viewById + "=TIME";
                if(path.indexOf("DrillDate")!=-1)
                    path+="&CBO_PRG_PERIOD_TYPE="+nextViewById+"&DDrill=Y";
                path+=paramUrl;
                path=path.replace("&","").replace("null","");
            //path=ctxPath+'/reportViewer.do?reportBy=viewReport&REPORTID='+reportId+"&CBO_PRG_PERIOD_TYPE="+nextViewById+"&DDrill=Y";
            }
            else
                path=url+measureName+"&DDrillAcross=Y&DDrill=Y"+paramUrl+"&adhocviewby=true&action="+adhocDrillType;
        // path=url+measureName+"&DDrillAcross=Y&CBOVIEW_BY" + viewById + "=" + nextViewById ;
         path+="&adhocTime=true";
        }
        else{
            var cboArpArray=new Array(measureName);
//            alert(cboArpArray);

            if(nextViewById=="Day" || nextViewById=="Week" || nextViewById=="Month" || nextViewById=="Qtr" || nextViewById=="Year"){
                if (drillAcrossItems.length == 0){
                    path=ctxPath+'/reportViewer.do?reportBy=viewReport&REPORTID='+reportId+"&CBO_PRG_PERIOD_TYPE="+nextViewById+"&DDrill=Y&CBOARP" + presentViewBy + "="+encodeURIComponent(JSON.stringify(cboArpArray))+paramUrl+"&AdhocDrill"+presentViewBy+"=Y"+"&adhocviewby=true&action="+adhocDrillType;
                }
                else{
                    if(msrNamesUrl.indexOf(presentViewBy)!=-1)
                        path=ctxPath+'/reportViewer.do?reportBy=viewReport&REPORTID='+reportId+"&CBO_PRG_PERIOD_TYPE="+nextViewById+"&DDrill=Y"+msrNamesUrl+paramUrl+"&AdhocDrill"+presentViewBy+"=Y"+"&adhocviewby=true&action="+adhocDrillType;
                }
            // path=ctxPath+'/reportViewer.do?reportBy=viewReport&REPORTID='+reportId+"&CBO_PRG_PERIOD_TYPE="+nextViewById+"&DDrill=Y&CBOVIEW_BY" + viewById + "=TIME&CBOARP" + presentViewBy + "="+measureName;
            }else{
                if (drillAcrossItems.length == 0)
                    path=ctxPath+'/reportViewer.do?reportBy=viewReport&REPORTID='+reportId+"&CBOARP" +presentViewBy+ "="+encodeURIComponent(JSON.stringify(cboArpArray))+paramUrl+"&AdhocDrill"+presentViewBy+"=Y"+"&adhocviewby=true&action="+adhocDrillType;
                else{
                    if(msrNamesUrl.indexOf(presentViewBy)!=-1)
                        path=ctxPath+'/reportViewer.do?reportBy=viewReport&REPORTID='+reportId+msrNamesUrl+paramUrl+"&AdhocDrill"+presentViewBy+"=Y"+"&adhocviewby=true&action="+adhocDrillType;
                }
            // path=ctxPath+'/reportViewer.do?reportBy=viewReport&REPORTID='+reportId+"&CBOVIEW_BY" + viewById + "=" + nextViewById +"&CBOARP" + presentViewBy + "="+measureName;
            }
        }
//         alert(path)
        if(isreportdrillPopUp == true || isreportdrillPopUp == 'true'){
            parent.submiturlsPopUp(path);
        }else{
        parent.submiturls(path);
    }
}
}
function saveTableRegion(ctxPath,reportId)  {
//    $.ajax({
//        url:ctxPath+'/reportViewer.do?reportBy=chkforUserPrevilages',
//        success:function(data){
//            var jsonVar=eval('('+data+')')
//            var userType=jsonVar.userType;
//            var isPowerAnalyserEnableforUser=jsonVar.isPAEnableforUser;
//            if(userType=="Admin" || isPowerAnalyserEnableforUser=="true"){
 var confirmDel=confirm("Do you want to Save The Table Changes Permanently");
                    if(confirmDel==true){
 $.ajax({
        url:ctxPath+'/reportViewer.do?reportBy=saveTableRegion&ReportId='+reportId,
        success:function(data) {
                    alert("Table changes saved")
                    var path= ctxPath+"/reportViewer.do?reportBy=viewReport&REPORTID="+reportId+"&action=reset";
                    parent.submiturls(path);

        }
            });
}
//            }else{
//                alert("You do not have the sufficient previlages")
//}
//        }});
}
function viewAdhocDrillforTableHeader(reportId,ctxPath,nextViewById,presentViewById,adhocDrillType,viewById,viewbyCnt,isCrossTab,url){
       var presentViewBy=presentViewById.replace(/\A_/g,'')
    // alert(nextViewById+"presentViewBy..."+presentViewBy)
    //alert(viewbyCnt)
//     if(viewbyCnt=="1" && adhocDrillType=="drilldown" && isCrossTab=="false"){
//         if(nextViewById=="Day" || nextViewById=="Week" || nextViewById=="Month" || nextViewById=="Qtr" || nextViewById=="Year")
//          var path=ctxPath+'/reportViewer.do?reportBy=viewReport&REPORTID='+reportId+"&CBO_PRG_PERIOD_TYPE="+nextViewById+"&CBOVIEW_BY" + viewById + "=TIME";
//         else
//          var path=ctxPath+'/reportViewer.do?reportBy=viewReport&REPORTID='+reportId+"&CBOVIEW_BY" + viewById + "=" + nextViewById;
//             submiturls(path);
//     }else
        if(adhocDrillType=="drillside"){
      $.post(ctxPath+"/reportViewer.do?reportBy=adhocChangeViewBy&newViewById="+nextViewById+"&reportId="+reportId+"&presentViewById="+presentViewBy+"&drillType="+adhocDrillType+"&Type=tableHeader",
          function(data){
//               if(nextViewById=="Day" || nextViewById=="Week" || nextViewById=="Month" || nextViewById=="Qtr" || nextViewById=="Year")
//                 var path=ctxPath+'/reportViewer.do?reportBy=viewReport&REPORTID='+reportId+"&CBO_PRG_PERIOD_TYPE="+nextViewById;
//                else
                 var path=ctxPath+"/reportViewer.do?reportBy=viewReport&action=ChangeViewBy&adhocChangeViewby=true&REPORTID="+reportId+"&adhocviewby=true"+"&adhocDrillType="+adhocDrillType;
                   parent.submiturls1(path);

                        });
     }else{
          if(url.indexOf(nextViewById)!=-1){

          }
          else if(url.indexOf(presentViewById)!=-1)
         {
             if(nextViewById=="Day" || nextViewById=="Week" || nextViewById=="Month" || nextViewById=="Qtr" || nextViewById=="Year")
               url=url.replace(presentViewById,"TIME")+"&CBO_PRG_PERIOD_TYPE="+nextViewById;
               else
               url=url.replace(presentViewById,nextViewById);

           var path=ctxPath+'/reportViewer.do?reportBy=viewReport&REPORTID='+reportId+url+"&adhocviewby=true"+"&adhocDrillType=drilldown"+"&nextViewById="+nextViewById;
//            alert(path)
            if(isreportdrillPopUp == true || isreportdrillPopUp == 'true'){
                parent.submiturlsPopUp(path);
            }else{
            parent.submiturls(path);
        }
          }
     }


  }
function addDrillAcrossVal(value,id){
      if(parent.drillAcrossElements.indexOf(value) == -1){
          parent.drillAcrossElements.push(value);
      }else{
          var index=parent.drillAcrossElements.indexOf(value);
          if(!document.getElementById(id).checked){
             parent.drillAcrossElements.splice(index, 1);
          }
      }
//      alert(parent.drillAcrossElements)
  }
function importExcelFile(ctxPath,reportId){
//    alert(reportId)
     parent.$("#importExcelFileDiv").width(250).height(200);
     var source=ctxPath+"/excelFileUpload.jsp?loadDialogs=true&reportid="+reportId;
     var iframeContent='<iframe  id="importExcelFrame" NAME="importExcelFrame" width="100%" height="100%" frameborder="0"  src="'+source+'"></iframe>'
//     alert(iframeContent)
     parent.$("#importExcelFileDiv").html(iframeContent);
     parent.$("#importExcelFileDiv").dialog('open');
//     parent.document.getElementById("importExcelFrame").src="";
//     parent.document.getElementById("importExcelFrame").src=ctxPath+"/excelFileUpload.jsp?loadDialogs=true&reportid="+reportId;
//     alert("close dialog")
//     alert("close dialog")
//     alert("close dialog")
//      parent.$("#importExcelFileDiv").dialog('close');
//   $.post(ctxPath+'/reportViewer.do?reportBy=importExcelFile&reportid='+reportId,
//         function(data){
//
//    });
}


//start of code by Govardhan for ST Filters
function showSubTotalfilters(ctxPath,repId){

$("#SubTotalSrch").dialog({
        bgiframe: true,
        autoOpen: false,
        height: 150,
        width: 330,
        modal: true,
        title:'SubTotal Search'
    });
 $.ajax({
        url:ctxPath+'/reportTable.do?reportBy=getAllFilterCondition&reportId='+repId,
        success:function(data)
        {

             if(data=="not applied" || data==""){
                 document.getElementById("SubTtlSrch").value="";
             }else
            {
              //  alert("data"+data);
                var html="";
                var dataJson = eval('('+data+')');
                var searchcolumn=(dataJson.subTotalSrchColumns);
                var Searchcondition=(dataJson.Searchcondition);

                var SearchValue=(dataJson.SearchValue);
                 var FirstViewBy=(dataJson.FirstViewByname);
                  var DistinctViewBys=(dataJson.DistinctViewBys);
                 document.getElementById("SearchColumn").innerHTML=searchcolumn
               document.getElementById("SubTtlSrch").value=SearchValue;
               document.getElementById("SubTotalSrchOption").value=Searchcondition;
                document.getElementById("DistinctviewByText").innerHTML="Number Of Distinct <span id=\"viewbytext\">"+FirstViewBy+"</span>'s Filtered are "+DistinctViewBys;
            document.getElementById("viewbytext").style.fontsize="25px";
                $("#SubTotalSrch").dialog('open');
            }
        }
    });



}


function CloseFilterdiloge(){


     $("#SubTotalSrch").dialog('close');


}
//end of code by Govardhan for ST Filters
// code added by Dinanath Parit
function reportBugMail23(ctxPath,reportId,tabName){
     parent.$("#CustomerReportBugMail").width(550).height(500);
     var source=ctxPath+"/ReportBugMail.jsp?loadDialogs=true&reportid="+reportId+"&tabName="+tabName;
     var iframeContent='<iframe  id="importExcelFrame" NAME="importExcelFrame" width="100%" height="100%" frameborder="0"  src="'+source+'"></iframe>'
     parent.$("#CustomerReportBugMail").html(iframeContent);
     parent.$("#CustomerReportBugMail").dialog('open');

}

// added by krishan pratap
function importExcelFile1(ctxPath,reportId){

    parent.$("#datalist").hide();
    parent.$("#importExcelFileDiv123").dialog({
        autoOpen: false,
        height: 273,
        width: 395,
        modal: true,
        title:' Import Template File'
    });
    parent.$("#importExcelFileDiv123").dialog('open')
     var source=ctxPath+"/UploadExcelTemplate.jsp?loadDialogs=true&reportid="+reportId;
     var iframeContent='<iframe  id="importExcelFrame" NAME="importExcelFrame" width="100%" height="100%" frameborder="0"  src="'+source+'"></iframe>'
    // alert(iframeContent)
   parent.$("#importExcelFileDiv123").html(iframeContent);

}
//added by sruthi for multicalendar for reports
function reportmulticalendar(ctxPath,reportId){
   parent.$("#datalist").hide();
   parent.$("#ReportMultiCalendar").dialog({
        autoOpen: false,
        height: 273,
        width: 395,
        modal: true,
        title:' Report Multi Calendar'
    });
    var html='';
      $.post(ctxPath+'/reportTemplateAction.do?templateParam=getReportMultiCalendar&reportId='+reportId+'&ctxPath='+ctxPath,
            function(data){
//                alert(data);
               // html=eval('('+data+')');
               // alert(html);
                parent.$("#ReportMultiCalendardata").html(data);
            });
             parent.$("#ReportMultiCalendar").dialog('open');
}
//ended by sruthi