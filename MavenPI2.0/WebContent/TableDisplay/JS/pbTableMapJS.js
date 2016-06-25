/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



var sortColName;
var sortDescend;
var prevSortObj;
var index;
var percentValue;
var goalValue;
var grandTotal;
var jsonVaradhoc;
var colname;
var discolname;
var reportid;
var ctxpath;
var textAlign;
//var  jsonVarindval;
//var total;
//var mesval;

var initialText = "ENTER YOUR NOTES HERE";
var prevTxtValue = "";
var insertedNew = null;

isIE=document.all;
isNN=!document.all&&document.getElementById;
isN4=document.layers;
isHot=false;

document.onmousedown=ddInit;
document.onmouseup=xy;

//stickNoteCount = " =rowCount ";
stickNoteCount = 1;
var ctxtPathrep = '';
//stickNoteCount++;

function goUp(fromRow,tabId){
    if(fromRow > 1)    {
        var source = "TableDisplay/pbDisplay.jsp?source=L&tabId="+tabId;
        var dSrc = document.getElementById("iframe1");
        dSrc.src = source;

    }
}
function goDown(toRow,rowCount,tabId){
    if(toRow < rowCount)    {
        var source = "TableDisplay/pbDisplay.jsp?source=R&tabId="+tabId;
        var dSrc = document.getElementById("iframe1");
        dSrc.src = source;
    }
}
function goFirst(fromRow,tabId){
    if(fromRow > 1)    {
        var source = "TableDisplay/pbDisplay.jsp?source=S&tabId="+tabId;
        var dSrc = document.getElementById("iframe1");
        dSrc.src = source;
    }
}
function goLast(toRow,rowCount,tabId){
    if(toRow < rowCount)    {
        var source = "TableDisplay/pbDisplay.jsp?source=E&tabId="+tabId;
        var dSrc = document.getElementById("iframe1");
        dSrc.src = source;
    }
}
function doSetFirstRow(obj,evt,maxRow,currStart,tabId){
    if(evt.keyCode == 13 && obj.value != "" && obj.value > 0 && obj.value <= maxRow && obj.value != currStart)    {
        var sVal = obj.value;
        sVal = sVal - 1;
        var source = "TableDisplay/pbDisplay.jsp?rowSearch="+sVal+"&tabId="+tabId;
        var dSrc = document.getElementById("iframe1");
        dSrc.src = source;
    }
}
function setRecordSize(sVal,tabId){
    var source = "TableDisplay/pbDisplay.jsp?slidePages="+sVal+"&tabId="+tabId;
       var dSrc = document.getElementById("iframe1");
        dSrc.src = source;
}
function doSetRecordSize(obj,evt,maxRow,currSize,tabId,reportId){
 $.post(ctxPath+'/reportViewer.do?reportBy=viewReportPageSize&tabId='+tabId+'&sVal='+sVal+'&reportid='+reportId, $("#myForm").serialize() ,
         function(data){
         });
    if(evt.keyCode == 13 && obj.value != "" && obj.value > 0 && obj.value <= maxRow && obj.value != currSize)    {
        var sVal = obj.value;
        setRecordSize(sVal,tabId);
    }

}
function goRefresh(tabId){
    sortColName = "";
    sortDescend = "";

    var source = "TableDisplay/pbDisplay.jsp?source=S&refresh=Y&tabId="+tabId;
    var dSrc = document.getElementById("iframe1");

    dSrc.src = source;
    parent.isGraphExists();
}
function doOpenMenu(obj){
    //    var doOpenMenuObj=document.forms.myForm.SRCHMENU_;

    //SRCHMENU_
    if(obj.style.display == '')
        obj.style.display = 'none';
    else
        obj.style.display = '';
}
function doCallOperation(parentObj,colName,sortType,dataType,tabId){ //sortType=0(Ascend),1(Descend)
    // doOpenMenu(parentObj);
    if(sortType == 0 || sortType == 1)    {
        doSort(colName,dataType,tabId,sortType);
    }

}
function doNewSrchOperation(symbol,colName,tabId,ctxPath){
     var srchValue = "";
     var srchCondition = "";
    if ( symbol == '!=')
        {
      srchCondition = 'Exclude';
    srchValue="0";
    srchValue= encodeURIComponent(srchValue);
    doSearch(colName,srchCondition,srchValue,tabId,ctxPath);
}
if (symbol == 'Clear'){    
    doClear(colName,tabId,ctxPath);    
}
}
function doClear(colName,tabId,ctxPath){
 $.ajax({
        url:ctxPath+'/reportTable.do?reportBy=clearDataSet&srchCol='+colName+'&reportId='+tabId,
        success:function(data)
        {   
            if ( data == 'refresh')
            {
                var HeaderColor="Progen";
                 var srchConditionnew="false";
                var source ="TableDisplay/pbDisplay.jsp?slidePages="+25+"&tabId="+tabId+"&HeaderColorNew="+HeaderColor+"&elemntIdNew="+colName+"&srchConditionnew="+srchConditionnew;
                var dSrc = document.getElementById("iframe1");
                dSrc.src = source;
            }
           
        }
    });   
}

function doSrchOperation(obj,symbol,colName,evt,tabId,ctxPath,secObj){
//    alert("hello");
//    var logicalvalue=logicalArth.value;
//    alert(symbol)
//    alert("obj="+obj+"symbol="+symbol+"colName="+colName+"evt="+evt+"tabId="+tabId+"secObj="+"ctxPath="+ctxPath);

if(symbol=='Clear')
    {
       
        doNewSrchOperation(symbol,colName,tabId,ctxPath);
        
    }
    if(evt.keyCode == 13 && obj.value != "" )    {
        var srchValue = "";
        var srchCondition = "";

        if ( symbol == '=')
            srchCondition = 'EQ';
        else if ( symbol == '>')
            srchCondition = 'GT';
        else if ( symbol == '<')
            srchCondition = 'LT';
        else if ( symbol == '>=')
            srchCondition = 'GE';
        else if ( symbol == '<=')
            srchCondition = 'LE';
        else if ( symbol == 'BT')
            srchCondition = 'BT';
        else if( symbol == 'TOP')
            srchCondition = 'TOP';
        else if( symbol == 'BTM')
            srchCondition = 'BTM';
        else if ( symbol == '*')
            srchCondition = 'LIKE';


        if(symbol == 'BT')
        {
            var id1=obj.id;
            var firstVal = "";
            var secondVal = "";

            if(id1.substring(0,1) == 1)
            {
                firstVal = obj.value;
                secondVal = secObj.value;
}
            else
            {
                firstVal = secObj.value;
                secondVal = obj.value;
            }
            srchValue = firstVal + "," + secondVal;
        }
        else
        {
            srchValue = obj.value;
        }
        srchValue= encodeURIComponent(srchValue);


        doSearch(colName,srchCondition,srchValue,tabId,ctxPath);
    }
}
function doSearch(colName,srchCondition, srchValue,tabId,ctxPath)
{
//            alert("colName="+colName+"srchCondition="+srchCondition+"srchValue="+srchValue+"tabId="+tabId+"ctxPath="+ctxPath);
    $.ajax({
        url:ctxPath+'/reportTable.do?reportBy=searchDataSet&search='+srchCondition+'&srchValue='+srchValue+'&srchCol='+colName+'&reportId='+tabId,
        success:function(data)
        {
            if ( data == 'invalid')
                alert("Invalid Data Format");
            else
            {
                var HeaderColor="Progen";
                var srchConditionnew="true";
                var source ="TableDisplay/pbDisplay.jsp?tabId="+tabId+"&HeaderColorNew="+HeaderColor+"&elemntIdNew="+colName+"&srchConditionnew="+srchConditionnew;
                var dSrc = document.getElementById("iframe1");
                dSrc.src = source;
            }
        }
    });
//    var source = "TableDisplay/pbDisplay.jsp?source=S&search="+srchCondition+"&srchValue="+srchValue+"&srchCol="+colName+"&tabId="+tabId;
//    var dSrc = document.getElementById("iframe1");
//    dSrc.src = source;
}

moveFromCol = "";
moveToCol   = "";
function doSwap(moveType,colName,tabId){

    if(moveType == 1)
        moveFromCol =colName;
    else if(moveType == 2)
        moveToCol = colName;
    if(moveFromCol != "" && moveToCol != "")    {

        var frc = moveFromCol;
        var toc = moveToCol;

        moveFromCol = "";
        moveToCol = "";

        var source = "TableDisplay/pbDisplay.jsp?swapFrom="+frc+"&swapTo="+toc+"&tabId="+tabId;
        var dSrc = document.getElementById("iframe1");
        dSrc.src = source;
    }
}
//function doSort(obj,colName,dataType,tabId)
function doSort(colName,dataType,tabId,sortType){
    sortDescend = sortType;
 var HeaderColor="Progen";
   var srchConditionnew="true";
    var source = "TableDisplay/pbDisplay.jsp?source=S&sort="+sortDescend+"&sortColumn="+colName+"&sortDataType="+dataType+"&tabId="+tabId+"&HeaderColorNew="+HeaderColor+"&elemntIdNew="+colName+"&srchConditionnew="+srchConditionnew;
    var dSrc = document.getElementById("iframe1");
    dSrc.src = source;
    parent.isGraphExists();
}
function chkChecked(obj){
    var idObj = "";
    var chkType = "";
    var fname = "TableDisplay/pbSetData.jsp";
    if(obj.checked)
    {
        chkType = "selected";
    }
    else
    {
        chkType = "unselected";
    }

    fname = fname + "?" + chkType + "=" + obj.value;
    var srcFrame = document.getElementById("iframe2");
    srcFrame.src = fname;

}
function mouseOn(iObj){
    iObj.className = "highlight";
}
function mouseOut(oObj){
    oObj.className = "trcls";
}
function mouseOut1(oObj){
    oObj.className = "trcls1";
}
function doDownload(dType,tabId,displayType,Obj){
    var source = "TableDisplay/pbDownload.jsp?dType="+dType+"&tabId="+tabId+"&displayType="+displayType;
    var dSrc = document.getElementById("dFrame");
    dSrc.src = source;

    if(Obj.style.display=='none'){
        Obj.style.display="block";
    }else{
        Obj.style.display="none";
    }
}
/*
 function showPdfdownload(Obj){
    if(Obj.style.display=='none'){
        Obj.style.display="block";
    }else{
        Obj.style.display="none";
    }
}
function showExceldownload(Obj){
    if(Obj.style.display=='none'){
        Obj.style.display="block";
    }else{
        Obj.style.display="none";
    }
}
 */
function showDownload(Obj){
    if(Obj.style.display=='none'){
        Obj.style.display="block";
    }else{
        Obj.style.display="none";
    }
}
function refreshGraphs(source,tabId)
{
    source = source + "?tabId="+tabId;
   if($.browser.safari==true){

        frames['iframe4'].location = source;
    }else{
        var gSrc = document.getElementById("iframe4");
        gSrc.src = source;

    }
}
function modifyColumns(columnId,checkBoxObj,tableObj,colName){
    var thObj=tableObj.getElementsByTagName('th');
    var trObj=tableObj.getElementsByTagName('tr');
    for(i=0;i<thObj.length;i++){
        if(thObj[i].id==columnId){
            if(checkBoxObj.checked)
                thObj[i].style.display=''
            else
                thObj[i].style.display='none';
        }
    }
    for(i=0;i<trObj.length;i++){
        var tdObj=trObj[i].getElementsByTagName('td');
        for(j=0;j<tdObj.length;j++){
            if(tdObj[j].id==columnId){
                if(checkBoxObj.checked)
                    tdObj[j].style.display=''
                else
                    tdObj[j].style.display='none';
            }
        }
    }
}
function showColumnList(modifyColumnsObj,prescolsListObj,prevShownCols,tabId){
    var presShownCols="";
    var cnt=0;
    if(modifyColumnsObj.style.display=='none'){
        modifyColumnsObj.style.display='';
    }
    else{
        modifyColumnsObj.style.display='none';
        for(var i=0;i<prescolsListObj.length;i++)
        {
            if(prescolsListObj[i].checked){
                presShownCols=presShownCols+","+prescolsListObj[i].value;
                cnt++;
            }
        }
        presShownCols=presShownCols.substring(1);



        if(presShownCols==prevShownCols){
        }
        else{
            var source = "TableDisplay/pbDisplay.jsp?presShownCols="+presShownCols+"&tabId="+tabId;
            var dSrc = document.getElementById("iframe1");
            dSrc.src = source;
        }
    }
}
function showTable(tableObj,tabId){

    //var hideTable='false;'

    if(tableObj.style.display=='' || tableObj.style.display=='block'){
        tableObj.style.display='none';
        var hideTable='true';

        //var source = "TableDisplay/pbDisplay.jsp?hideTable="+hideTable+"&tabId="+tabId+"&fh1=80&fh2=620";
        var source = "TableDisplay/pbDisplay.jsp?hideTable="+hideTable+"&tabId="+tabId;
        var dSrc = document.getElementById("iframe1");

        dSrc.src = source;
    }
    else{
        tableObj.style.display='';
        hideTable='false';
        source = "TableDisplay/pbDisplay.jsp?hideTable="+hideTable+"&tabId="+tabId;
        dSrc = document.getElementById("iframe1");

        dSrc.src = source;

    //document.getElementById('frameHgt1').style.height='350px';
    //document.getElementById('frameHgt2').style.height='350px';
    }
}
function changeColSeq(dispColSeqObj){
    if(dispColSeqObj.style.display=='none'){
        dispColSeqObj.style.display='';
    }
    else{
        dispColSeqObj.style.display='none';
    }
}
function showColumnTotal(dispColTotalObj,tabId,nettotreqobj,grdtotreqobj,avgtotreqobj,overAllMaxObj,overAllMinObj,catMaxObj,catMinObj){
    var nettotreq;
    var grdtotreq;
    var avgtotreq;

    var overAllMax;
    var overAllMin;
    var catMax;
    var catMin;


    if(dispColTotalObj.style.display=='none'){
        dispColTotalObj.style.display='';
    }
    else{
        dispColTotalObj.style.display='none';

        if(nettotreqobj.style.display=='')
            nettotreq='true';
        else
            nettotreq='false';
        if(grdtotreqobj.style.display=='')
            grdtotreq='true';
        else
            grdtotreq='false';
        if(avgtotreqobj.style.display=='')
            avgtotreq='true';
        else
            avgtotreq='false';

        if(overAllMaxObj.style.display=='')//overAllMaxObj
            overAllMax='true';
        else
            overAllMax='false';

        if(overAllMinObj.style.display=='')//overAllMinObj
            overAllMin='true';
        else
            overAllMin='false';

        if(catMaxObj.style.display=='')//catMaxObj
            catMax='true';
        else
            catMax='false';

        if(catMinObj.style.display=='')//catMinObj
            catMin='true';
        else
            catMin='false';

        var source = "TableDisplay/pbDisplay.jsp?nettotreq="+nettotreq+"&grdtotreq="+grdtotreq+"&avgtotreq="+avgtotreq+"&overAllMax="+overAllMax+"&overAllMin="+overAllMin+"&catMax="+catMax+"&catMin="+catMin+"&tabId="+tabId;
        var dSrc = document.getElementById("iframe1");
        dSrc.src = source;
    }
}
function modifyColumnTotal(totalRowObj,checkBoxObj){
    if(checkBoxObj.checked)
        totalRowObj.style.display=''
    else
        totalRowObj.style.display='none';
}
function addMeasure(reportId,lastSeq,path){
    window.open("pbFormula.jsp?reportId="+reportId+"&lastSeq="+lastSeq+"&path="+path,"Measures", "scrollbars=1,address=no");
}
function submiturls(path){
    parent.submiturls1(path);
}


function setFrameHeight(FrameHgt1,FrameHgt2){
    var iframeObj1=document.getElementById("iframe1");
    var iframeObj2=document.getElementById("iframe4");

    //frameHgt1 is for Table and frameHgt2 is for Graph
    iframeObj1.style.height=FrameHgt1;
    iframeObj2.style.height=FrameHgt2;
}
function reportColumnDrill(colIds,repId,tableId){
    window.open("pbColumnDrill.jsp?COLIDS="+colIds+"&REPORTID="+repId+"&TABLEID="+tableId,"DrillacrossColumn","scrollbars=1,width=550,height=350,address=no");
}

function modifyDispColumn(columnId,checkBoxObj,tableObj,colName){
    var thObj=tableObj.getElementsByTagName('th');
    var trObj=tableObj.getElementsByTagName('tr');
    for(i=0;i<thObj.length;i++){
        if(thObj[i].id==columnId){
            if(checkBoxObj.checked)
                thObj[i].style.display=''
            else
                thObj[i].style.display='none';
        }
    }
    for(i=0;i<trObj.length;i++){
        var tdObj=trObj[i].getElementsByTagName('td');
        for(j=0;j<tdObj.length;j++){
            if(tdObj[j].id==columnId){
                if(checkBoxObj.checked)
                    tdObj[j].style.display=''
                else
                    tdObj[j].style.display='none';
            }
        }
    }
}

function transposeTable(tranposeTable,tabId){
    var source = "TableDisplay/pbDisplay.jsp?tranposeTable="+tranposeTable+"&tabId="+tabId;
    var dSrc = document.getElementById("iframe1");
    dSrc.src = source;
}
function formatNumber(colName,disColName,nbrSymbol,REPORTID,ctxPath){

    $.ajax({
        url:ctxPath+'/reportViewer.do?reportBy=tableChanges&tableChange=numberformat&colName='+colName+'&disColName='+disColName+'&nbrSymbol='+nbrSymbol+'&reportid='+REPORTID,
        success:function(data){
            var source =ctxPath+"/TableDisplay/pbDisplay.jsp?tabId="+REPORTID;
            var dSrc = document.getElementById("iframe1");
            dSrc.src = source;
        }
    });
}
function setgraphFrameHeight(hgt){
    parent.document.getElementById("iframe4").height=hgt;
}

function ifrmesizedynamic(){
    var ifrmesizes = document.getElementById("iframe1");
//    alert(screen.width)
    if ($.browser.msie == true || $.browser.safari == true){
//        if(screen.width > 1400){
//            ifrmesizes.style.width='1200px';
//            ifrmesizes.style.position='relative';
//        //ifrmesizes.style.overflow='auto';
//
//        }
//        else if(screen.width > 1280 && screen.width <=1400 ){
//            ifrmesizes.style.width='1100px';
//            ifrmesizes.style.position='relative';
//        //ifrmesizes.style.overflow='auto';
//        }
//        else if(screen.width == 1280 ){
//            ifrmesizes.style.width='1000px';
//            ifrmesizes.style.position='relative';
//        //ifrmesizes.style.overflow='auto';
//        }
//        else if(screen.width <1280 && screen.width >= 1024 ){
//            ifrmesizes.style.width='785px';
//            ifrmesizes.style.position='relative';
//        //ifrmesizes.style.overflow='auto';
//        }
//        else if(screen.width < 1024 && screen.width>=1000 ){
//            ifrmesizes.style.width='700px';
//            ifrmesizes.style.position='relative';
//        //ifrmesizes.style.overflow='auto';
//        }else{
//            alert("in < 1000")
//            alert(ifrmesizes.style.width)
//            alert((ifrmesizes.style.width -400)+ 'px')
//            ifrmesizes.style.width=(ifrmesizes.style.width -400)+ 'px';
//        }
//    alert(screen.width-270 +'px')
            ifrmesizes.style.width=screen.width-270+'px';
            ifrmesizes.style.position='relative';

    }else{
        if(screen.width > 1400){
            ifrmesizes.style.width='465%';
            ifrmesizes.style.position='relative';
            //ifrmesizes.style.overflow='auto';
        }
        else if(screen.width > 1280 && screen.width <=1400 ){
            ifrmesizes.style.width='447%';
            ifrmesizes.style.position='relative';
            //ifrmesizes.style.overflow='auto';
        }
        else if(screen.width == 1280 ){
            ifrmesizes.style.width='407%';
            ifrmesizes.style.position='relative';
            //ifrmesizes.style.overflow='auto';
        }
        else if(screen.width < 1280 && screen.width >= 1024 ){
            ifrmesizes.style.width='320%';
            ifrmesizes.style.position='relative';
        //ifrmesizes.style.overflow='auto';
        }
        else if(screen.width < 1024 && screen.width >= 1000 ){
            ifrmesizes.style.width='300%';
            ifrmesizes.style.position='relative';
        //ifrmesizes.style.overflow='auto';
        }else{
            ifrmesizes.style.width='270%';
            ifrmesizes.style.position='relative';
        }        
    }
}

function ifrmesizedynamicResize(){
    var ifrmesizes = document.getElementById("iframe1");
    var newwidth=document.getElementById("tabTable").offsetWidth;
    if ($.browser.msie == true || $.browser.safari == true){
//        if(screen.width >= 1440){
//            ifrmesizes.style.width='1400px';
//            ifrmesizes.style.position='relative';
//        //ifrmesizes.style.overflow='auto';
//        }
//        else if(screen.width > 1280 && screen.width <=1400 ){
//            ifrmesizes.style.width='1200px';
//            ifrmesizes.style.position='relative';
//        //ifrmesizes.style.overflow='auto';
//        }
//        else if(screen.width == 1280 ){
//            ifrmesizes.style.width='1200px';
//            ifrmesizes.style.position='relative';
//        //ifrmesizes.style.overflow='auto';
//        }
//        else if(screen.width < 1280 && screen.width >= 1024 ){
//            ifrmesizes.style.width='950px';
//            ifrmesizes.style.position='relative';
//        //ifrmesizes.style.overflow='auto';
//        }
//        else if(screen.width < 1024  && screen.width >= 1000 ){
//            ifrmesizes.style.width='900px';
//            ifrmesizes.style.position='relative';
//        //ifrmesizes.style.overflow='auto';
//        }else{
//            ifrmesizes.style.width=(ifrmesizes.style.width -400)+ 'px';
//        }
//            alert(screen.width-50 +'px')
            ifrmesizes.style.width=screen.width-50 +'px';
            ifrmesizes.style.position='relative';

    }else{
        if(screen.width > 1400){
//            ifrmesizes.style.width='465%';
ifrmesizes.style.width=newwidth+'px';
            ifrmesizes.style.position='relative';
            //ifrmesizes.style.overflow='auto';
        }
        else if(screen.width > 1280 && screen.width <=1400 ){
//            ifrmesizes.style.width='100%';
 ifrmesizes.style.width=newwidth+'px';
            ifrmesizes.style.position='relative';
            //ifrmesizes.style.overflow='auto';
        }
        else if(screen.width == 1280 ){
//            ifrmesizes.style.width='407%';
 ifrmesizes.style.width=newwidth+'px';
            ifrmesizes.style.position='relative';
            //ifrmesizes.style.overflow='auto';
        }
        else if(screen.width < 1280 && screen.width >= 1024 ){
//            ifrmesizes.style.width='320%';
 ifrmesizes.style.width=newwidth+'px';
            ifrmesizes.style.position='relative';
        //ifrmesizes.style.overflow='auto';
        }
        else if(screen.width < 1024 && screen.width >= 1000 ){
//            ifrmesizes.style.width='300%';
ifrmesizes.style.width=newwidth+'px';
            ifrmesizes.style.position='relative';
        //ifrmesizes.style.overflow='auto';
        }else{
//            ifrmesizes.style.width='270%';
ifrmesizes.style.width=newwidth+'px';
            ifrmesizes.style.position='relative';
        }
    }
}

function addRuntimeColumn(measType,coltype,coltypeDisp,colName,disColName,REPORTID,ctxPath){
    var isColumnPresent = false;
    if(colName.indexOf(coltype)!=-1){
        isColumnPresent=true;
    }
coltypeDisp=coltypeDisp.toString().replace("-"," ");
 if((coltypeDisp.trim()=="(Running Total)")||(coltypeDisp.trim()=="(Rank)") ||(coltypeDisp.trim()=="(QTD Rank)")||(coltypeDisp.trim()=="(YTD Rank)")||(coltypeDisp.trim()=="(Rank ST)")||(coltypeDisp.trim()=="(PMTD Rank)")||(coltypeDisp.trim()=="(PQTD Rank)")||(coltypeDisp.trim()=="(PYTD Rank)")){
    if(!isColumnPresent){
        var targetVal=document.getElementById("goailId").value;

        $.ajax({
            url:ctxPath+'/reportViewer.do?reportBy=tableChanges&tableChange='+measType+'&colName='+colName+'&disColName='+disColName+'&reportid='+REPORTID+"&targetVal="+targetVal,
            success:function(data){
                if(data=='Exists'){
                    alert(disColName+ " "+coltypeDisp+" already exists")

                }else if(data=='add relavant measure'){
                    var flagvalue=coltypeDisp.toString().replace("Rank","");
                    alert("Please add " +disColName+""+flagvalue+ "measure")
                }
                else{
                    var HeaderColor="Progen";
                    var source =ctxPath+"/TableDisplay/pbDisplay.jsp?tabId="+REPORTID+"&HeaderColorNew="+HeaderColor+"&elemntIdNew="+colName;
                    var dSrc = document.getElementById("iframe1");
                    dSrc.src = source;
                }
            }
        });
    }
    else
    {
        alert("You cannot add "+coltypeDisp+" to a "+coltypeDisp +" column");
    }
// divObj.style.display='none';
 }
 else if(coltypeDisp.trim()=='(GoalSeek)'){
     var targetVal=document.getElementById("goailId").value;
    //alert("targetVal\t:"+targetVal);
    var colName=parent.$("#colNameId").val();
  var disColName=parent.$("#discolId").val();
      if(!isColumnPresent){
        $.ajax({
            url:ctxPath+'/reportViewer.do?reportBy=tableChanges&tableChange='+measType+'&colName='+colName+'&disColName='+disColName+'&reportid='+REPORTID+"&targetVal="+targetVal,
            success:function(data){

                if(data=='Exists'){
                    alert(disColName+ " "+coltypeDisp+" already exists")

                }else{

                    var source =ctxPath+"/TableDisplay/pbDisplay.jsp?tabId="+REPORTID;
                    var dSrc = document.getElementById("iframe1");
                    dSrc.src = source;
                }
            }

        });
    }
    else
    {
        alert("You cannot add "+coltypeDisp+" to a "+coltypeDisp +" column");
    }
// divObj.style.display='none';
 }
  $("#goailId").val("");
  $("#goalseakId").dialog("close");
}
//added by santhosh.kumar@progenbusiness.com on 18-02-2010 for pagination in column display

function goColumnUp(fromColumn,tabId){
    if(fromColumn > 1)    {
        var source = "TableDisplay/pbDisplay.jsp?columnsource=LC&tabId="+tabId;
        var dSrc = document.getElementById("iframe1");
        dSrc.src = source;

    }
}
function goColumnDown(toColumn,ColumnCount,tabId){
    if(toColumn < ColumnCount)    {
        var source = "TableDisplay/pbDisplay.jsp?columnsource=RC&tabId="+tabId;
        var dSrc = document.getElementById("iframe1");
        dSrc.src = source;
    }
}
function goColumnFirst(fromColum,tabId){
    if(fromColum > 1)    {
        var source = "TableDisplay/pbDisplay.jsp?columnsource=SC&tabId="+tabId;
        var dSrc = document.getElementById("iframe1");
        dSrc.src = source;
    }
}
function goColumnLast(toColumn,ColumnCount,tabId){
    if(toColumn < ColumnCount)    {
        var source = "TableDisplay/pbDisplay.jsp?columnsource=EC&tabId="+tabId;
        var dSrc = document.getElementById("iframe1");
        dSrc.src = source;
    }
}
function doSetFirstColumn(obj,evt,maxColumn,currStart,tabId){
    if(evt.keyCode == 13 && obj.value != "" && obj.value > 0 && obj.value <= maxColumn && obj.value != currStart)    {
        var sVal = obj.value;
        sVal = sVal - 1;
        var source = "TableDisplay/pbDisplay.jsp?columnSearch="+sVal+"&tabId="+tabId;
        var dSrc = document.getElementById("iframe1");
        dSrc.src = source;
    }
}
function doSetColumnSize(obj,evt,maxColumn,currSize,tabId){
    if(evt.keyCode == 13 && obj.value != "" && obj.value > 0 && obj.value <= maxColumn && obj.value != currSize)    {
        var sVal = obj.value;
        var source = "TableDisplay/pbDisplay.jsp?columnslidePages="+sVal+"&tabId="+tabId;
        var dSrc = document.getElementById("iframe1");
        dSrc.src = source;
    }
}

function openColumnSettingsDIV(divObj){
    if(divObj.style.display=='none')   {
        divObj.style.display='block';
    }
    else{
        divObj.style.display='none';
    }
}


function statistics(columnName,disColumnName,tabId,type,ctxPath){
    var HeaderColor="Progen";
    var source = "TableDisplay/pbDisplay.jsp?sourceValue="+type+"&source=S&columnName="+columnName+"&disColumnName="+disColumnName+"&tabId="+tabId+"&HeaderColorNew="+HeaderColor+"&elemntIdNew="+columnName;
    var dSrc = document.getElementById("iframe1");
    dSrc.src = source;


}

function getCorrelation(columnName,correlationColID,disColumnName,disColumnName2,tabId,type){
//    var source = "TableDisplay/pbDisplay.jsp?sourceValue="+type+"&source=S&columnName="+columnName+"&correlationColName="+correlationColID+"&disColumnName="+disColumnName+"&tabId="+tabId;
//    var dSrc = document.getElementById("iframe1");
    $.ajax({
        url:"reportViewer.do?reportBy=getCorrelation&columnName="+columnName+"&correlationColName="+correlationColID+"&reportId="+tabId,
        success:function(data){
            var html="<br><br><br><br><table align='center'><tr><td><input class='navtitle-hover' type='button' value='Done' onclick='closeCorrelationDiv()'></td></tr></table>"
            $("#correlationId").html("The Correlation coefficient between "+disColumnName+" and "+disColumnName2+" is "+data+html)
    $("#correlationId").dialog('open');
        }
    })


}
function closeCorrelationDiv()
{
     $("#correlationId").dialog('close');
}

function changeNumformat(colname,dispColName,REPORTID,ctxPath,numberformat){
        $.ajax({
        url:ctxPath+'/reportViewer.do?reportBy=tableChanges&tableChange=columnProperties&colName='+colname+'&reportid='+REPORTID+'&nbrSymbol='+numberformat+'&dispColName='+dispColName,
        success:function(data){
            var source =ctxPath+"/TableDisplay/pbDisplay.jsp?tabId="+REPORTID;
            var dSrc = document.getElementById("iframe1");
            dSrc.src = source;
        }

    });
}

   function sort(colName,sortType,dataType,tabId,ctxPath)
   {

               if(sortType == 0 || sortType == 1){
                doSort(colName,dataType,tabId,sortType);
                }


           }

//   function addPercentColumn(colName,disColName,REPORTID,ctxPath){
//    var isPercentColumn=false;
//    if(colName.indexOf('_percentwise')!=-1){
//        isPercentColumn=true;
//    }
//
//    if(!isPercentColumn){
//        $.ajax({
//            url:ctxPath+'/reportViewer.do?reportBy=tableChanges&tableChange=PercentColumn&colName='+colName+'&disColName='+disColName+'&reportid='+REPORTID,
//            success:function(data){
//                if(data=='Exists'){
//                    alert(disColName+" (% wise)  already exists")
//
//                }else{
//                    var source =ctxPath+"/TableDisplay/pbDisplay.jsp?tabId="+REPORTID;
//                    var dSrc = document.getElementById("iframe1");
//                    dSrc.src = source;
//                }
//            }
//        });
//    }else{
//        alert("You cannot add (% wise column) to an (% wise column)")
//    }
//// divObj.style.display='none';
//}
//function addPercentSubtotalColumnwithAbsolute(colName,disColName,REPORTID,ctxPath){
//    var isPercentColumn=false;
//    if(colName.indexOf('_percentwise')!=-1){
//        isPercentColumn=true;
//    }
//
//    if(!isPercentColumn){
//        $.ajax({
//             url:ctxPath+'/reportViewer.do?reportBy=tableChanges&tableChange=PercentSubtotalColumnwithAbsolute&colName='+colName+'&disColName='+disColName+'&reportid='+REPORTID,
//            success:function(data){
//                if(data=='Exists'){
//                    alert(disColName+" (% wise)  already exists")
//
//                }else{
//                    var source =ctxPath+"/TableDisplay/pbDisplay.jsp?tabId="+REPORTID;
//                    var dSrc = document.getElementById("iframe1");
//                    dSrc.src = source;
//                }
//            }
//        });
//    }else{
//        alert("You cannot add (% wise column) to an (% wise column)")
//    }
//
//
//}
//function addPercentSubtotalColumn(colName,disColName,REPORTID,ctxPath){
//    var isPercentColumn=false;
//    if(colName.indexOf('_percentwise')!=-1){
//        isPercentColumn=true;
//    }
//
//    if(!isPercentColumn){
//        $.ajax({
//             url:ctxPath+'/reportViewer.do?reportBy=tableChanges&tableChange=PercentSubtotalColumn&colName='+colName+'&disColName='+disColName+'&reportid='+REPORTID,
//            success:function(data){
//                if(data=='Exists'){
//                    alert(disColName+" (% wise)  already exists")
//
//                }else{
//                    var source =ctxPath+"/TableDisplay/pbDisplay.jsp?tabId="+REPORTID;
//                    var dSrc = document.getElementById("iframe1");
//                    dSrc.src = source;
//                }
//            }
//        });
//    }else{
//        alert("You cannot add (% wise column) to an (% wise column)")
//    }
//
//
//}
//
//function addPercentColumnwithAbsolute(colName,disColName,REPORTID,ctxPath){
//
//    var isPercentColumn=false;
//    if(colName.indexOf('_percentwise')!=-1){
//        isPercentColumn=true;
//    }
//
//    if(!isPercentColumn){
//        $.ajax({
//            url:ctxPath+'/reportViewer.do?reportBy=tableChanges&tableChange=PercentColumnwithAbsolute&colName='+colName+'&disColName='+disColName+'&reportid='+REPORTID,
//            success:function(data){
//                if(data=='Exists'){
//                    alert(disColName+" (% wise)  already exists")
//
//                }else{
//                    var source =ctxPath+"/TableDisplay/pbDisplay.jsp?tabId="+REPORTID;
//                    var dSrc = document.getElementById("iframe1");
//                    dSrc.src = source;
//                }
//            }
//        });
//    }else{
//        alert("You cannot add (% wise column) to an (% wise column)")
//    }
//// divObj.style.display='none';
//}

 function addPercentColumn(colName,disColName,REPORTID,ctxPath,onSubtotal,absoluteVariable)
 {

     var isPercentColumn=false;
    var percentColumn="Percent";
    if(onSubtotal=='true')
        percentColumn=percentColumn+"Subtotal";

    percentColumn=percentColumn+"Column";

    if(absoluteVariable=='true')
        percentColumn=percentColumn+"withAbsolute";

    if(colName.indexOf('_percentwise')!=-1){
        isPercentColumn=true;
    }

    if(!isPercentColumn){
      $.post(ctxPath+'/reportViewer.do?reportBy=tableChanges&tableChange='+percentColumn+'&colName='+colName+'&disColName='+disColName+'&reportid='+REPORTID, $("#myForm").serialize() ,
         function(data){
             var basicGrand=eval('('+data+')');
                if(basicGrand.DimViewByNames[0]=='Exists'){
                    alert(disColName+" (% wise)  already exists")

                }else{
                    var HeaderColor="Progen";
                   var colNameNew=colName+"_percentwise";
                    var source =ctxPath+"/TableDisplay/pbDisplay.jsp?tabId="+REPORTID+'&HeaderColorNew='+HeaderColor+'&elemntIdNew='+colNameNew;
                    var dSrc = document.getElementById("iframe1");
                    dSrc.src = source;
                }
      });
//    if(!isPercentColumn){
//        $.ajax({
//            url:ctxPath+'/reportViewer.do?reportBy=tableChanges&tableChange='+percentColumn+'&colName='+colName+'&disColName='+disColName+'&reportid='+REPORTID,
//            success:function(data){
//                if(data=='Exists'){
//                    alert(disColName+" (% wise)  already exists")
//
//                }else{
//                    var source =ctxPath+"/TableDisplay/pbDisplay.jsp?tabId="+REPORTID;
//                    var dSrc = document.getElementById("iframe1");
//                    dSrc.src = source;
//                }
//            }
//        });
            }
    else{
        alert("You cannot add (% wise column) to an (% wise column)")
    }

// divObj.style.display='none';
}

function addParamFilter(columnName,disColumnName,REPORTID,path){
    if($.browser.msie== true)
    {
        $("#paramFilterDiv").dialog({
        autoOpen: false,
        height: 300,
        width: 350,
        position: 'justify',
        modal: true
    });

    }
    else
    {
        $("#paramFilterDiv").dialog({
        autoOpen: false,
        height: 250,
        width: 350,
        position: 'justify',
        modal: true
    });

    }
    var count=0;

    $.ajax({
       url:path+'/reportViewer.do?reportBy=checkforMeasureType&columnName='+columnName,
       success:function(data){
           //alert(data)
            if(data=='0')
                alert("Cannot apply ParameterFilter On this Measure")
            else{
               $("#paramFilterDiv").dialog('open');

    $.ajax({
        url:path+'/reportViewer.do?reportBy=showViewBy&REPORTID='+REPORTID,
        success:function(data){
            var frameObj=parent.document.getElementById("paramFilter");
            var source =path+'/TableDisplay/pbParameterFilter.jsp?reportId='+REPORTID+'&columnName='+columnName+'&disColumnName='+disColumnName;
            frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
            frameObj.src=source;
        }
    });
            }
       }
    });


}

function toprows(columnName,disColumnName,REPORTID,noofRows,ctxPath){
  var HeaderColor="Progen";
  
    var source = "TableDisplay/pbDisplay.jsp?sourceValue=toprows&source=S&columnName="+columnName+"&topbottomCount="+noofRows+"&disColumnName="+disColumnName+"&tabId="+REPORTID+"&HeaderColorNew="+HeaderColor+"&elemntIdNew="+columnName;
    var dSrc = document.getElementById("iframe1");
    dSrc.src = source;
    parent.isGraphExists();
    }

function subTotalTopRows(columnName,disColumnName,REPORTID,noofRows,ctxPath){

    var source = "TableDisplay/pbDisplay.jsp?sourceValue=SubTotaltoprows&source=S&columnName="+columnName+"&topbottomCount="+noofRows+"&disColumnName="+disColumnName+"&tabId="+REPORTID;
    var dSrc = document.getElementById("iframe1");
    dSrc.src = source;
    parent.isGraphExists();
           }

function subTotalBottomRows(columnName,disColumnName,REPORTID,noofRows,ctxPath){

    var source = "TableDisplay/pbDisplay.jsp?sourceValue=subTotalBottomRows&source=S&columnName="+columnName+"&topbottomCount="+noofRows+"&disColumnName="+disColumnName+"&tabId="+REPORTID;
    var dSrc = document.getElementById("iframe1");
    dSrc.src = source;
    parent.isGraphExists();
}

 function toprowsWithOthers(columnName,disColumnName,REPORTID,noofRows,ctxPath){
     //alert("toprowsothers")
var HeaderColor="Progen";
    var source = "TableDisplay/pbDisplay.jsp?sourceValue=toprowsWithOthers&source=S&columnName="+columnName+"&topbottomCount="+noofRows+"&disColumnName="+disColumnName+"&tabId="+REPORTID+"&HeaderColorNew="+HeaderColor+"&elemntIdNew="+columnName;
    var dSrc = document.getElementById("iframe1");
    dSrc.src = source;
    parent.isGraphExists();
}

function rounding(columnName,disColumnName,REPORTID,precision){
    var HeaderColor="Progen";
    var source = "TableDisplay/pbDisplay.jsp?sourceValue=rounding&source=S&columnName="+columnName+"&precision="+precision+"&disColumnName="+disColumnName+"&tabId="+REPORTID+"&HeaderColorNew="+HeaderColor+"&elemntIdNew="+columnName;
    var dSrc = document.getElementById("iframe1");
    dSrc.src = source;
}

function bottomrows(columnName,disColumnName,REPORTID,noofRows,ctxPath){
var HeaderColor="Progen";
  var source = "TableDisplay/pbDisplay.jsp?sourceValue=bottomrows&source=S&columnName="+columnName+"&topbottomCount="+noofRows+"&disColumnName="+disColumnName+"&tabId="+REPORTID+"&HeaderColorNew="+HeaderColor+"&elemntIdNew="+columnName;
    var dSrc = document.getElementById("iframe1");
    dSrc.src = source;
    parent.isGraphExists();

           }
function topRowsPercentWise(columnName,disColumnName,REPORTID,noofRows,ctxPath){
    var HeaderColor="Progen";
    var source = "TableDisplay/pbDisplay.jsp?sourceValue=toprowsPercent&source=S&columnName="+columnName+"&topbottomCount="+noofRows+"&disColumnName="+disColumnName+"&tabId="+REPORTID+"&HeaderColorNew="+HeaderColor+"&elemntIdNew="+columnName;
    var dSrc = document.getElementById("iframe1");
    dSrc.src = source;

           }
function bottomRowsPercentWise(columnName,disColumnName,REPORTID,noofRows,ctxPath){
var HeaderColor="Progen";
    var source = "TableDisplay/pbDisplay.jsp?sourceValue=bottomrowsPercent&source=S&columnName="+columnName+"&topbottomCount="+noofRows+"&disColumnName="+disColumnName+"&tabId="+REPORTID+"&HeaderColorNew="+HeaderColor+"&elemntIdNew="+columnName;
    var dSrc = document.getElementById("iframe1");
    dSrc.src = source;

           }

function addPriorRuntimeColumn(coltype,coltypeDisp,colName,disColName,REPORTID,ctxPath){
    var isColumnPresent = false;
    if(colName.indexOf(coltype)!=-1){
        isColumnPresent=true;
    }
    if(!isColumnPresent){
        $.ajax({
            url:ctxPath+'/reportViewer.do?reportBy=tableChanges&tableChange=RankColumnWithPrior&colName='+colName+'&disColName='+disColName+'&reportid='+REPORTID,
            success:function(data){
                if(data=='Exists')
                {
                    alert(disColName+ " "+coltypeDisp+" already exists")
                }
                else if(data=='Prior Does Not Exist')
                {
                    alert("Prior does not exist for "+disColName)

                }
                else if(data=='Prior Not Selected')
                {
                    alert("Please add Prior for "+disColName +" through Edit Table")

                }
                else
                {
                    var HeaderColor="Progen"
                    var source =ctxPath+"/TableDisplay/pbDisplay.jsp?tabId="+REPORTID+"&HeaderColorNew="+HeaderColor+"&elemntIdNew="+colName;
                    var dSrc = document.getElementById("iframe1");
                    dSrc.src = source;
                }

            }
        });
    }
    else
    {
        alert("You cannot add "+coltypeDisp+" to a "+coltypeDisp +" column");
    }
// divObj.style.display='none';
}
function openCreateSegmentDialog(reportId,ctxPath){
    var measureId=$("#SegmentMeasures").val();
   var measureName=$("#SegmentMeasures option:selected").text();
    $("#createSegmentDialogDiv").dialog('close');
    openMeasureSegmentDialog(measureId,measureName,reportId,ctxPath);

}

function openMeasureSegmentDialog(measure,disColumnName,reportId,path)
{
          $.ajax({
         url:path+'/reportViewer.do?reportBy=getMeasureSegmentDialog&reportId='+reportId+'&disColumnName='+disColumnName+'&measure='+measure,
        success:function(data) {
            var html="";
            var dataJson = eval('('+data+')');
            var allow=(dataJson.SegmentAllowed);
            var max=(dataJson.maximum);
            var min=(dataJson.minimum);
            var avg=(dataJson.average);
            if(allow=="updated"){
                var segmentName=(dataJson.SegmentName);
                var lowerlimit=(dataJson.LowerLimit);
                var upperlimit=(dataJson.UpperLimit);
            }

             if(allow=="updated"){
                for(var i=0;i<segmentName.length;i++){
                    html=html+"<tr>"+
                    "<td>"+
                    "<input type= text name=segmentInput"+i+" value="+segmentName[i]+" id=segmentInput"+i+" style=text-align:left;>"+
                    "</td>"+
                    "<td>"+
                    "<input type=text name=minInput0"+i+" value="+lowerlimit[i]+" id=minInput"+i+" style=text-align:right; onkeypress=javascript:return isNumberKey(event)>"+
                    "</td>"+
                    "<td>"+
                    "<input type=text name=maxInput0"+i+" value="+upperlimit[i]+" id=maxInput"+i+" style=text-align:right; onkeypress=javascript:return isNumberKey(event) onblur=javascript:displayMinLimit(this)>"+
                    "</td>"+
                    "</tr>";
                }
            }

      if((allow=="initialize")||(allow=="updated")){
            parent.$("#createSegmentDiv").dialog('open')
            parent.$("#segmentReportId").val(reportId);
            parent.$("#measurename").html(disColumnName);
            parent.$("#segmentMeasureId").val(measure);
            parent.$("#maximumvalue").html(max)
            parent.$("#minimumvalue").html(min)
            parent.$("#averagevalue").html(avg)
           if(allow=="updated")
            parent.$("#segmentvalues").html(html)
           }
        }
    });
}

function columnProperties(columnName,disColumnName,reportId){
    frameObj = document.getElementById("columnPropertiesframe");
    frameObj.src= "TableDisplay/PbColumnProperties.jsp?colmnname="+columnName+"&dispcolmname="+disColumnName+"&reportId="+reportId;
    $("#columnPropertiesdiv").dialog('open');
}

function applycolor(columnName,disColumnName,reportId,ctxPath)
{
          $.post("reportViewer.do?reportBy=getColorRulesForMeasure&colmnname="+columnName+"&dispcolmname="+disColumnName+"&reportid="+reportId, $("#mytabmapForm").serialize() ,
         function(data){
         data=encodeURIComponent(data);
         frameObj = document.getElementById("applycolorframe");
         frameObj.src= "TableDisplay/PbApplyColors.jsp?colmnname="+columnName+"&dispcolmname="+disColumnName+"&reportid="+reportId+"&colorData="+data;
         $("#applycolrdiv").dialog('open');
        });

//    $.ajax({
//        url:"reportViewer.do?reportBy=getColorRulesForMeasure&colmnname="+columnName+"&dispcolmname="+disColumnName+"&reportid="+reportId,
//        success:function(data){
//         data=encodeURIComponent(data);
//         frameObj = document.getElementById("applycolorframe");
//         frameObj.src= "TableDisplay/PbApplyColors.jsp?colmnname="+columnName+"&dispcolmname="+disColumnName+"&reportid="+reportId+"&colorData="+data;
//         $("#applycolrdiv").dialog('open');
//        }
//    });

}
function resetcolor(columnName,disColumnName,reportId)
{
    $.post("reportViewer.do?reportBy=resetColorRulesForMeasure&colmnname="+columnName+"&dispcolmname="+disColumnName+"&reportid="+reportId, $("#mytabmapForm").serialize() ,
         function(data){
         var     HeaderColor="Progen";
         
         parent.document.getElementById("iframe1").src="TableDisplay/pbDisplay.jsp?tabId="+reportId+"&HeaderColorNew="+HeaderColor;
    });
//    $.ajax({
//        url:"reportViewer.do?reportBy=resetColorRulesForMeasure&colmnname="+columnName+"&dispcolmname="+disColumnName+"&reportid="+reportId,
//        success:function(data){
//         parent.document.getElementById("iframe1").src="TableDisplay/pbDisplay.jsp?tabId="+reportId;
//        }
//    });

}
function priorEnable(elementid,priorEnable,ReportId,ctxPath1){//alert("vnfn")
     parent.document.getElementById("iframe1").src="TableDisplay/pbDisplay.jsp?tabId="+ReportId+"&priorEnable=priorEnable";
}

function priorResetcolor(elementid,priorEnable,ReportId,ctxPath1){//alert("vnfn")
     parent.document.getElementById("iframe1").src="TableDisplay/pbDisplay.jsp?tabId="+ReportId+"&priorEnableReset=priorEnableReset";
}
function addRowWisePercent(colName,reportID,path)
{

        $.ajax({
            url:path+'/reportViewer.do?reportBy=tableChanges&tableChange=RowWisePercent&colName='+colName+'&reportid='+reportID,
            success:function(data){
                if(data=='Exists')
                {
                    alert("Row wise % already exists");
                }
                else if ( data == 'AddGT' )
                {
                    alert("Please add Grand Total Column to the Crosstab from Properties")
                }
                else
                {
                    var source =path+"/TableDisplay/pbDisplay.jsp?tabId="+reportID;
                    var dSrc = document.getElementById("iframe1");
                    dSrc.src = source;
                }
            }
        });

}

function showDuplicate(elementId,reportId,ctxPath){
    $.ajax({
            url:ctxPath+'/reportViewer.do?reportBy=findDuplicate&elementId='+elementId+'&reportId='+reportId,
            success:function(data){
                if(data=='success')
                {
                    var source =ctxPath+"/TableDisplay/pbDisplay.jsp?tabId="+reportId;
                    var dSrc = document.getElementById("iframe1");
                    dSrc.src = source;
                }
                else
                    alert("No Duplicates Exist");
            }
   });
}

function lockdataset(elementId,reportId,ctxPath,locked){
    //alert(locked)
    $.ajax({
            url:ctxPath+'/reportViewer.do?reportBy=lockDataset&elementId='+elementId+'&reportId='+reportId+'&islockset='+locked,
            success:function(data){
                   //alert(locked)
                    if(locked=='true')
                        {
                    alert("Dataset Locked Successfully");
                    //window.location.href=window.location.href
            }
                else{
                    alert("Locked Dataset Released Successfully")
                    //window.location.href=window.location.href
                }
            }
   });
}

function selectMeasureForSegmentation(reportId,ctxPath)
{
    $.ajax({
        url:ctxPath+'/reportViewer.do?reportBy=getMeasuresInContainer&reportId='+reportId,
        success:function(data){
             var html="";
             var dataJson = eval('('+data+')');
             var Measures=(dataJson.Measures);
             var MeasureNames=(dataJson.MeasureNames);
             for(var i=0;i<Measures.length;i++)
             {
                 html=html+"<option value=\""+Measures[i]+"\">"+MeasureNames[i]+"</option>";
             }
              parent.$("#SegmentMeasures").html(html);
        }
     })
  parent.$("#createSegmentDialogDiv").dialog('open');

}

function createGroupByAnalysis(reportId,viewbyId,viewbyName,ctxPath){
    //'isDimSeg' added by Nazneen
    var isDimSeg = "false";
    parent. $("#dispgrpAnalysis").dialog('open');
    var frameObj=document.getElementById("dispgrpAnalysisFrame");
    var source =ctxPath+'/TableDisplay/createParentParameter.jsp?reportId='+reportId+'&viewbyId='+viewbyId+'&isDimSeg='+isDimSeg;
    frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
    frameObj.src=source;
}

function getSegmentDefinition(reportId,ctxPath){

    $.ajax({
        url:ctxPath+'/reportViewer.do?reportBy=getSegmentValues&reportId='+reportId,
        success:function(data) {
            var dataJson = eval('('+data+')');
            var SegmentType=(dataJson.SegmentType);
            if(SegmentType=='MeasureBasedSegment'){

                var segmentMeasureId=(dataJson.segmentMeasureId);
                var segmentMeasureName=(dataJson.segmentMeasureName);

                openMeasureSegmentDialog(segmentMeasureId,segmentMeasureName,reportId,ctxPath);
            }
            else{
                var segmentDimensionId=(dataJson.segmentDimensionId);
                var segmentDimensionName=(dataJson.segmentDimensionName);

                createGroupByAnalysis(reportId,segmentDimensionId,segmentDimensionName,ctxPath);
            }

        }

    });
}


function resetRuntimeColumn(colName,disColName,REPORTID,ctxPath){
    var AOId=parent.document.getElementById("AOId").value; 
    var removeComparision = false;
    if(!(AOId==null || typeof AOId ==="undefined" || AOId=="" )){
    $.ajax({
            url:ctxPath+'/reportViewerAction.do?reportBy=getComparedWithRT&colName='+colName+'&disColName='+disColName+'&reportid='+REPORTID,
            success:function(data){
                if(data=="true"){
                    var confirmAlert = confirm("Removing this meaure will remove comparision(s) regarding this measure. Do you want to proceed?");
                    if(confirmAlert){
                        removeComparision = true;
                        $.ajax({
                        url:ctxPath+'/reportViewer.do?reportBy=tableChanges&tableChange=resetRuntimeColumn&colName='+colName+'&disColName='+disColName+'&reportid='+REPORTID+'&removeComparision='+removeComparision,
                        success:function(data){
                        var source = ctxPath+"/TableDisplay/pbDisplay.jsp?tabId="+REPORTID;
                        var dSrc = document.getElementById("iframe1");
                        dSrc.src = source;
                        }
                     });
                    }
                }else{
                    $.ajax({
            url:ctxPath+'/reportViewer.do?reportBy=tableChanges&tableChange=resetRuntimeColumn&colName='+colName+'&disColName='+disColName+'&reportid='+REPORTID,
            success:function(data){
                    var source = ctxPath+"/TableDisplay/pbDisplay.jsp?tabId="+REPORTID;
                    var dSrc = document.getElementById("iframe1");
                    dSrc.src = source;
            }
        });
}
            }
        }); 
    }else{
        $.ajax({
            url:ctxPath+'/reportViewer.do?reportBy=tableChanges&tableChange=resetRuntimeColumn&colName='+colName+'&disColName='+disColName+'&reportid='+REPORTID,
            success:function(data){
                var source = ctxPath+"/TableDisplay/pbDisplay.jsp?tabId="+REPORTID;
                var dSrc = document.getElementById("iframe1");
                dSrc.src = source;
            }
        });
    }    
}

function renameMeasure(colName,disColName,REPORTID,ctxPath){
    $("#editMsrName").dialog({
        bgiframe: true,
        autoOpen: false,
        height: 150,
        width: 330,
        modal: true,
        title:'Edit Measure Name'
    });
   // alert("display column"+disColName)
    document.getElementById("oldMsrName").value=disColName;
    document.getElementById("colName").value=colName;
    document.getElementById("ctxPath").value=ctxPath;
    document.getElementById("newMsrName").value="";
    $("#editMsrName").dialog('open');
}

function updateMsrName(REPORTID){
    $("#editMsrName").dialog('close');
    var newDispName=document.getElementById("newMsrName").value;
    var colName=document.getElementById("colName").value;
    var ctxPath=document.getElementById("ctxPath").value;
    $.ajax({
        url:ctxPath+'/reportViewer.do?reportBy=tableChanges&tableChange=renameMeasure&colName='+encodeURIComponent(colName)+'&disColName='+encodeURIComponent(newDispName)+'&reportid='+REPORTID,
        success:function(data){
                var source = ctxPath+"/TableDisplay/pbDisplay.jsp?tabId="+REPORTID;
                var dSrc = document.getElementById("iframe1");
                dSrc.src = source;
        }
    });
}

function editMeasures(elementId,displayLable,reportId)
{

    var msrElementId=elementId.replace("A_","");
  $.ajax({
      url:"reportViewer.do?reportBy=getFormulaForMeasure&elementId="+msrElementId,
      success:function(data){
          var dataArray=data.split(",");
          var formula=dataArray[0];
          var ctxPath=dataArray[1];
          var bussColName=dataArray[2];
          var aggType=dataArray[3];
          var prePostVal=dataArray[4];
          var userColType=dataArray[5];
          if(userColType=='summarized' || userColType=='SUMMARIZED'){
             $.ajax({
                            url:ctxPath+'/reportViewer.do?reportBy=loadDialogs&loadDialogs=true&reportId='+reportId+'&from=viewer',
                            success:function(data) {
//                                editMeasure(msrElementId,formula,displayLable,ctxPath,reportId);
                                editMeasure(msrElementId,formula,bussColName,ctxPath,reportId,aggType,prePostVal);
                            }
                        });
          }
          else {
              alert('This operation can be performed only for summarized measures.')
          }

      }
  })
}
    function setColourGroupForMap(ctxPath,reportId){
        var html = "";
        $.ajax({
            url:ctxPath+'/reportViewer.do?reportBy=getMapMainMeasures&reportId='+reportId,
            success:function(data){
                 data = eval('('+data+')');
                 var mainMeasureIds = data.MainMeasureIds;
                 var mainMeasArray = mainMeasureIds.split(",");
                 var mainMeasureLabels = data.MainMeasureLabels;
                 var mainMEasureLabelArray = mainMeasureLabels.split(",");
                 if(mainMeasArray.length==1){
                     applycolor("A_"+mainMeasArray,'', reportId);
                 }else{
             for(var i=0;i<mainMeasArray.length;i++)
             {
                 html=html+"<option value=\""+mainMeasArray[i]+"\">"+mainMEasureLabelArray[i]+"</option>";
             }
              parent.$("#mainMeasures").html(html);
               parent.$("#mapColorGrouping").dialog("open");
                 }
            }
    });

    }
function applyColorGroupToMap(reportId){
    var obj = document.getElementById("mainMeasures");
    var mainMeasure = obj.options[obj.selectedIndex].value;
    mainMeasure = "A_"+mainMeasure;
    parent.$("#mapColorGrouping").dialog("close");
    applycolor(mainMeasure,'', reportId);

}
function applyCustomSequence(elementId,reportId,ctxPath)
{
   var flag="";
    $.post(ctxPath+'/reportViewer.do?reportBy=applyCustomSequence&reportId='+reportId,
    function(data){
            if(data=='true')
                {
                flag=confirm("Data already sorted. If you want to sort again, existing sequence will be lost. Do you want to continue?")
                if(flag==true)
                    addCustomSequence(elementId,reportId,ctxPath)
                }
                else
                    {
                        addCustomSequence(elementId,reportId,ctxPath)
                    }
    });
}
function addCustomSequence(elementId,reportId,ctxPath)
{
   $.post(ctxPath+'/reportViewer.do?reportBy=getViewByValues&elementId='+elementId+'&reportId='+reportId,
            function(data){
                 var jsonVar=eval('('+data+')');
                 var html="<br><table align='center'><tr><td><input type='button' value='Done' class='navtitle-hover' onclick=\"setCustomViewSeq('"+reportId+"','"+elementId+"','"+ctxPath+"')\"/></td></tr></table>";
                parent.$("#sequenceDiv").html(jsonVar.htmlStr+html);

                 $("#myList3").treeview({
                        animated:"slow",
                        persist: "cookie"
                    });

                    $('ul#myList3 li').quicksearch({
                        position: 'before',
                        attached: 'ul#myList3',
                        loaderText: '',
                        delay: 100
                    })
                    $(".myDragTabs").draggable({
                        helper:"clone",
                        effect:["", "fade"]
                    });
                    $("#dropTabs").droppable({
                        activeClass:"blueBorder",
                        accept:'.myDragTabs',
                        drop: function(ev, ui) {
                            createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                        }
                    }
                );
                     grpColArray=jsonVar.memberValues
                    $(".sortable").sortable();
                parent.$("#sequenceDiv").dialog('open');

            });

}

function setCustomViewSeq(reportId,elementId,ctxPath)
{

    parent.$("#sequenceDiv").dialog('close');
     var viewByVals="";
                var colsUl=document.getElementById("sortable");
                if(colsUl!=undefined || colsUl!=null){
                    var colIds=colsUl.getElementsByTagName("li");
                    if(colIds!=null && colIds.length!=0){
                        for(var i=0;i<colIds.length;i++){
                            //cols = cols+","+colIds[i].id.replace("GrpCol","");
                            viewByVals=viewByVals+","+colIds[i].id.replace("_li","");
                        }
                        if(viewByVals!=""){
                            viewByVals = viewByVals.substring(1);
                        }

                    }
                }
                $.post(ctxPath+'/reportViewer.do?reportBy=setCustomViewSeq&viewByVals='+viewByVals+'&reportId='+reportId+'&elementId='+elementId,
            function(data){
                        var source =ctxPath+'/TableDisplay/pbDisplay.jsp?tabId='+reportId+'&sourceValue=customSeq'+'&custElementId='+elementId;
                                    var dSrc = document.getElementById("iframe1");
                                    dSrc.src = source;
                });

}


function customKpi(colName,disColName,reportId){
  //frameObj = document.getElementById("customKpi");
  parent.$("#measElementId").val(disColName);
  parent.$("#reportKpiMeasId").val(colName);
  parent.$("#customKpidiv").dialog('open');
}


function applygoalseek(colName,disColName,REPORTID,ctxPath,onSubtotal,absoluteVariable) {

     parent.$("#colNameId").val(colName);
     parent.$("#discolId").val(disColName);
    var isPercentColumn=false;
    var percentColumn="Percent";
    if(onSubtotal=='true')
        percentColumn=percentColumn+"Subtotal";

    percentColumn=percentColumn+"Column";
    if(absoluteVariable=='true')
        percentColumn=percentColumn+"withAbsolute";

    if(colName.indexOf('_percentwise')!=-1){
        isPercentColumn=true;
    }

    if(!isPercentColumn){
      $.post(ctxPath+'/reportViewer.do?reportBy=tableChanges&tableChange='+percentColumn+'&colName='+colName+'&disColName='+disColName+'&reportid='+REPORTID, $("#myForm").serialize() ,
         function(data){
             var basicGrand=eval('('+data+')');
             $("#grandTotalId").val(basicGrand.DimViewByNames[1]);
                if(basicGrand.DimViewByNames[0]=='Exists'){
                    alert(disColName+" (% wise)  already exists")

                }else{
                    var source =ctxPath+"/TableDisplay/pbDisplay.jsp?tabId="+REPORTID;
                    var dSrc = document.getElementById("iframe1");
                    dSrc.src = source;
                    //parent.openGroupMeasureList(ctxPath,REPORTID);
                }

      });
            }
    else{
        alert("You cannot add (% wise column) to an (% wise column)")
    }

     parent.$("#goalseakId").dialog("open");

           }

function applygoalseekadhoc(colName,disColName,REPORTID,ctxPath,onSubtotal,absoluteVariable) {
//        $.ajax({
//       url:ctxPath+'/reportViewer.do?reportBy=chkforUserPrevilages',
//       success:function(data){
//            var jsonVar=eval('('+data+')')
//            var userType=jsonVar.userType;
//            var isPowerAnalyserEnableforUser=jsonVar.isPAEnableforUser;
//           if(userType=="Admin" || isPowerAnalyserEnableforUser=="true"){
    var isPercentColumn=false;
    var percentColumn="Percent";
    if(onSubtotal=='true')
        percentColumn=percentColumn+"Subtotal";

    percentColumn=percentColumn+"Column";

    if(absoluteVariable=='true')
        percentColumn=percentColumn+"withAbsolute";

    if(colName.indexOf('_percentwise')!=-1){
        isPercentColumn=true;
    }

    if(!isPercentColumn){
      $.post(ctxPath+'/reportViewer.do?reportBy=tableChanges&tableChange='+percentColumn+'&colName='+colName+'&disColName='+disColName+'&reportid='+REPORTID, $("#myForm").serialize() ,
         function(data){
                if(data=='Exists'){
                    alert(disColName+" (% wise)  already exists")

                }else{
                    var source =ctxPath+"/TableDisplay/pbDisplay.jsp?tabId="+REPORTID;
                    var dSrc = document.getElementById("iframe1");
                    dSrc.src = source;
                }
                adhocFunction(colName,ctxPath,REPORTID,disColName);
      });

            }
    else{
        alert("You cannot add (% wise column) to an (% wise column)")
    }
//    }else{
//              alert("You do not have the sufficient previlages")
//           }
//       }});
           }

function adhocFunction(colName,ctxPath,REPORTID,disColName){

  alert("Applying Goal Seek Adhoc");
     $.post(ctxPath+'/reportViewer.do?reportBy=getViewByValuesadhoc&elementId='+colName+'&reportId='+REPORTID,
            function(data){
                jsonVaradhoc=eval('('+data+')');
                var sum=0;
                var html1
                 var html2="<table><thead><tr><td width='16%'><input type='text' class='myTextbox3' name='' value='Dimension Values'  readonly></td><td  width='10%' align='right'><input type='text' class='myTextbox3' name='' value='Existing %' size='15' readonly></td><td  width='10%' align='right'>\n\
                                 <input type='text' class='myTextbox3' name='' value='UserDefined %' size='15' readonly></td></tr></thead></table><br>";
                 var html3="";
                 for(index=0; index<jsonVaradhoc.DimViewByNames.length;index++){
                 html3=html3+"<table><tr><td width='16%'><input type='text' style='backgroundColor:#f3f3f3' name='' value='"+jsonVaradhoc.DimViewByNames[index]+"' readonly></td><td width='10%' align='right'><input type='text' size='15' name='' value='"+jsonVaradhoc.MeasuresValues[index]+"' readonly></td>\n\
                            <td width='10%' align='right'><input type='text' id='percentClomn"+index+"'  value='"+jsonVaradhoc.MeasuresValues[index].replace("%","")+"'  name='formName' onkeypress='return isNumberKey(event)' onchange=\"calculateTotal(this.id)\" size='15'></input></td></tr></table>";
                sum=sum+parseFloat(jsonVaradhoc.MeasuresValues[index].replace("%",""));
                 }
                 var html4="<table align='left'><tr><td><font style='color:blue;size:15'><b>Check For 100%</b><font><input type='checkbox' id='checkedValId' name='' value=''></td></tr></table><table align='right'><tr><td><input type='button' value='Done' class='navtitle-hover' onclick=\"percentCloumn('"+REPORTID+"','"+colName+"','"+ctxPath+"','"+jsonVaradhoc.DimViewByNames.length+"','"+disColName+"')\"/></td></tr></table><br>";

                 if(Math.round(sum*100)/100==99.95||Math.round(sum*100)/100==99.96||Math.round(sum*100)/100==99.97||Math.round(sum*100)/100==99.98||Math.round(sum*100)/100==99.99||Math.round(sum*100)/100==100||Math.round(sum*100)/100==100.01||Math.round(sum*100)/100==100.02||Math.round(sum*100)/100==100.03||Math.round(sum*100)/100==100.04||Math.round(sum*100)/100==100.05)
                   html1="<table align='right'><tr><td ><font style='color:blue;size:30'><b>Total Percentage</b></font></td ><td></td><input type='text' id='totalId' name='' value='"+Math.round(sum)+"' readonly size='15' ></td></tr></table><br><br>";
                 else
                 html1="<table align='right'><tr><td ><font style='color:blue;size:30'><b>Total Percentage</b></font></td ><td></td><input type='text' id='totalId' name='' value='"+Math.round(sum*100)/100+"' readonly size='15'></td></tr></table><br><br>";

                var html5="<div id='startgoalAdhocId'  title=Table GoalSeekAdhoc><center><table><tr><td width='50%'>Absolute:<input type='radio' id='absoluteId' name='goalName' value='' onclick=\"absoluteBase()\" checked></td>\n\
                             <td width='40%'>Percentage:<input type='radio' id='percentId' name='goalName' value='' onclick=\"percentageBase()\"></td></table></center><center><div id='goaLId'><tr><td>Grand Total<input type='text' name='' value='"+jsonVaradhoc.GrandTotal+"' size='12' readonly></td>\n\
                             <td>Goal Value<input type='text' name='goalValue' id='goalValueId' value='' size='15' ></td></tr></table></div><div id='percentagId' style='display: none'><table><tr>\n\
                             <td >Increase %<input type='text' name='goalValue' id='percentValueId' value=''   size='15' ></td></tr></table></div></center></div><br>";
               //var html6="<table align='left'><tr><td><font style='color:blue;size:15'><b>Check For 100%</b><font><input type='checkbox' id='checkedValId' name='' value=''></td></tr></table><br>";

                  parent.$("#goalAdhocId").dialog('open');
                  $("#percentColmnId").html(html4+html5+html2+html3+html1+html4);
            });
}

  function percentCloumn(REPORTID,colName,ctxPath,length,disColName){

                     var sum=0;
                      var hunder=100;
                        for(index=0;index<length;index++){
                         var mesval=$("#percentClomn"+index).val();
                         sum=sum+parseFloat(mesval);
                        }
                 var checkpercent=document.getElementById("checkedValId");

                 if(checkpercent.checked){
                    if(Math.round(sum*100)/100==99.95||Math.round(sum*100)/100==99.96||Math.round(sum*100)/100==99.97||Math.round(sum*100)/100==99.98||Math.round(sum*100)/100==99.99||Math.round(sum*100)/100==100||Math.round(sum*100)/100==100.01||Math.round(sum*100)/100==100.02||Math.round(sum*100)/100==100.03||Math.round(sum*100)/100==100.04||Math.round(sum*100)/100==100.05){
                       $.post(ctxPath+'/reportViewer.do?reportBy=tableChanges&tableChange=userGoalSeekColumn&goaluserPer=userGoalPercent&colName='+colName+'&disColName='+disColName+'&reportid='+REPORTID,$("#percentColmnId").serialize(),
                         function(data){
                                  if(data=='Exists'){
                               alert(disColName+ "(Goal Seek)  already exists")

                                 }
                                 else{
                                     var source =ctxPath+"/TableDisplay/pbDisplay.jsp?tabId="+REPORTID;
                                     var dSrc = document.getElementById("iframe1");
                                      dSrc.src = source;
                                 }
                          }
                       );
                           parent.$("#goalAdhocId").dialog('close');
                     }
                       else if(sum<100){
                            alert("Total Percentage Should be\t:"+hunder);
                        }
                         else if(sum>100){
                            alert("Total Percentage  Should be\t:"+hunder);
                        }
                 }
                        else{
                             $.post(ctxPath+'/reportViewer.do?reportBy=tableChanges&tableChange=userGoalSeekColumn&goaluserPer=userGoalPercent&colName='+colName+'&disColName='+disColName+'&reportid='+REPORTID,$("#percentColmnId").serialize(),
                               function(data){
                                  if(data=='Exists'){
                               alert(disColName+ "(Goal Seek)  already exists")

                                 }
                                 else{
                                     var source =ctxPath+"/TableDisplay/pbDisplay.jsp?tabId="+REPORTID;
                                     var dSrc = document.getElementById("iframe1");
                                      dSrc.src = source;
                                 }
                          }
                       );
                            parent.$("#goalAdhocId").dialog('close');
                  }
             //Math.round((100-(parseFloat(sum)))*100)/100 place this code in place of sum in above else block
             //Math.round(((parseFloat(sum))-100)*100)/100
  }
    function isNumberKey(evt)
       {
          var charCode = (evt.which) ? evt.which : event.keyCode
          if (charCode != 46 && charCode > 31
            && (charCode < 48 || charCode > 57))
             return false;

          return true;
       }

       function absoluteBase(){
           parent.$("#goaLId").show();
           parent.$("#percentagId").hide();
           parent.$("#goalValueId").val('');

       }
       function percentageBase(){
            parent.$("#percentagId").show();
           parent.$("#goaLId").hide();
           parent.$("#percentValueId").val('');
       }

       function calculateTotal(id){
          var total=0;
           for(index=0;index<jsonVaradhoc.DimViewByNames.length;index++){

                        var mesval=$("#percentClomn"+index).val();
                       if(mesval!="")
                            total=total+parseFloat(mesval);
                        }
                        total=Math.round(total*100)/100
                         $("#totalId").val(total);
       }


function saveReportKPI(ctxPath,PbReportId)
{
    var timeDet = "";
    var otherTimeDet = "";
    var totals = "";
    if(document.getElementById("static") != null && document.getElementById("static").checked){
                    $("#selectTimeDet").hide();
                }else if(document.getElementById("dynamic") != null && document.getElementById("dynamic").checked){
                    $("#selectTimeDet").show();
                }
    if(document.getElementById("static") != null && document.getElementById("static").checked){
        timeDet = "static";
    }else if(document.getElementById("dynamic") != null && document.getElementById("dynamic").checked){
        timeDet = "dynamic";
        if(document.getElementById("repTimeDet") != null && document.getElementById("repTimeDet").checked){
            otherTimeDet = "repTimeDet";
        }else if(document.getElementById("otherTimeDet") != null && document.getElementById("otherTimeDet").checked){
            otherTimeDet = "otherTimeDet";
        }
    }
    if(document.getElementById("subtotalDet") != null && document.getElementById("subtotalDet").checked){
            totals = "subtotalDet";
        }else if(document.getElementById("grandtotalDet") != null && document.getElementById("grandtotalDet").checked){
            totals = "grandtotalDet";
        }
     parent.$("#customKpidiv").dialog('close');
    var kpiElementId=$("#reportKpiMeasId").val();
    var measName=$("#measElementId").val();
    var kpiName=$("#measKPIName").val();
    var aggType=$("select#aggType").val();
     $.post(ctxPath+'/reportViewer.do?reportBy=saveCreateKPIMeasure&kpiElementId='+kpiElementId+'&measName='+encodeURIComponent(measName)+'&kpiName='+encodeURIComponent(kpiName)+'&aggType='+aggType+'&PbReportId='+PbReportId+'&timeDet='+timeDet+'&otherTimeDet='+otherTimeDet+'&totals='+totals, 
function(data){
    alert("Kpi Created")

});
}

function rowWiseColorGroup(colName,reportID,path)
{

      $.post(path+'/reportViewer.do?reportBy=getViewByValues&elementId='+colName+'&reportId='+reportID,
            function(data){
                 var jsonVar=eval('('+data+')');
                 var grpname=jsonVar.groupName;
                 var html="";

                 if(grpname!='null'){
                  html+="<center><table align='center'><tr><td>EnterGroup Name:<input type='text' value='"+grpname+"' id='groupId' ></td></tr></table></center><br>";
                 }
                  else{
                  html+="<center><table align='center'><tr><td>EnterGroup Name:<input type='text' value='' id='groupId' ></td></tr></table></center><br>";
                  }
                 var html1="<br><center><table align='center'><tr><td><input type='button' value='Next' class='navtitle-hover' onclick=\"applyCrossColorGrp('"+reportID+"','"+colName+"','"+path+"')\" /></td></tr></table></center>";
               // parent.$("#sequenceDiv").html(jsonVar.htmlStr+html);

                parent.$("#rowWiseClrId").html(html+jsonVar.htmlStr+html1);

                 $("#myList3").treeview({
                        animated:"slow",
                        persist: "cookie"
                    });

                    $('ul#myList3 li').quicksearch({
                        position: 'before',
                        attached: 'ul#myList3',
                        loaderText: '',
                        delay: 100
                    })
                    $(".myDragTabs").draggable({
                        helper:"clone",
                        effect:["", "fade"]
                    });
                    $("#dropTabs").droppable({
                        activeClass:"blueBorder",
                        accept:'.myDragTabs',
                        drop: function(ev, ui) {
                            createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                        }
                    }
                );
                     grpColArray=jsonVar.memberValues
                    $(".sortable").sortable();
                parent.$("#rowWiseClrId").dialog('open');

            });

}
     function applyCrossColorGrp(reportId,elementId,ctxPath)
       {
           var groupName=document.getElementById("groupId").value;
           if(groupName==""){
               alert("Please Enter GroupName")
           }
         else{
          parent.$("#rowWiseClrId").dialog('close');
//          var groupName=document.getElementById("groupId").value;
             var viewByVals="";
                var colsUl=document.getElementById("sortable");
                if(colsUl!=undefined || colsUl!=null){
                    var colIds=colsUl.getElementsByTagName("li");
                    if(colIds!=null && colIds.length!=0){
                        for(var i=0;i<colIds.length;i++){
                            //cols = cols+","+colIds[i].id.replace("GrpCol","");
                            viewByVals=viewByVals+","+colIds[i].id.replace("_li","");
                        }
                        if(viewByVals!=""){
                            viewByVals = viewByVals.substring(1);
                        }

                    }
                }
                $.post(ctxPath+'/reportViewer.do?reportBy=multRowColorGroup&viewByVals='+viewByVals+'&reportId='+reportId+'&elementId='+elementId+"&groupName="+groupName,
            function(data){
//                        var source =ctxPath+'/TableDisplay/pbDisplay.jsp?tabId='+reportId+'&sourceValue=customSeq'+'&custElementId='+elementId;
//                                    var dSrc = document.getElementById("iframe1");
//                                    dSrc.src = source;
             //if(data==null)
                applycolor(elementId,ctxPath,reportId);
                });
         }
}



   function applygoalseekTimeBase(colName,disColName,REPORTID,ctxPath){
//     $.ajax({
//       url:ctxPath+'/reportViewer.do?reportBy=chkforUserPrevilages',
//       success:function(data){
//            var jsonVar=eval('('+data+')')
//            var userType=jsonVar.userType;
//            var isPowerAnalyserEnableforUser=jsonVar.isPAEnableforUser;
//           if(userType=="Admin" || isPowerAnalyserEnableforUser=="true"){
              $("#productId").val("");
              individualValues(colName,disColName,REPORTID,ctxPath);
//           }else{
//              alert("You do not have the sufficient previlages")
//           }
//       }});

   }

   function individualValues(colName,disColName,REPORTID,ctxPath){

               // $("#loading").show();
                var productName=$("#productId").val();
                var viewByVals="";
                if(productName!=null){
                var colsUl=document.getElementById("sortable");
                var colIds=colsUl.getElementsByTagName("li");
                            viewByVals=viewByVals+","+colIds[0].id.replace("_li","");
                            if(viewByVals!=""){
                            viewByVals = viewByVals.substring(1);
                        }
                }
//                   reportViewer.do?reportBy=getViewByValuesTimeBaseIndvl&elementId='+colName+'&reportId='+REPORTID+'&productName='+productName+'&viewByVals='+viewByVals,

          $.post(ctxPath+'/reportViewer.do?reportBy=getViewByValuesTimeBaseIndvl&elementId='+colName+'&reportId='+REPORTID+'&productName='+productName+'&viewByVals='+viewByVals,
              function(data){


                  if(data=='Prior Not Here'){
                       // $("#loading").hide();
                        alert("Prior Or Changed % Measures are Not there in Table");
                        alert("Please Add Prior and Changed % Measures to the Table");

                       }
                 else if(data=='Not Exist'){
                     //$("#loading").hide();
                       alert("Apply only On Current Measure")
                      }
                else{
                 var  jsonVarindval=eval('('+data+')');
                   var html1="";
                   var html2="";
                   var html3="";
                   var html4="";
                   var html5="";

                   if(jsonVarindval.DimensionName[5]!=undefined)
                   html1+="<center><table><tr><td><font size='1' color='red'>*</font><b>Model Name:</b><input type='text' name='goalName' id='goalValueId'></td><td>Save As Type:</td><td><select name='dType' id='prodseletionId'><option value='html'>HTML</option><option value='excel'>Excel</option></select></td><td><input type='submit' value='Go' class='navtitle-hover' onclick=\"javascript:newProdDivFun('"+REPORTID+"','"+colName+"','"+ctxPath+"','"+disColName+"')\"/></td><td></td></tr></table></center><br>";
                   else
                   html1+="<center><table><tr><td><input type='button' value='Done' class='navtitle-hover' onclick=\"timeBasedDiv('"+REPORTID+"','"+colName+"','"+ctxPath+"','"+disColName+"')\"/></td></tr></table></center><br>";


                  if(jsonVarindval.DimensionName.length>5) {
                    html5+="<table><tr><td><input type='text' id='productValId' name='vieByname'  value='"+jsonVarindval.DimensionName[5]+"' style='display:none'></td></tr></table>"
                  }

                  html4+="<table><tr><td width='20%'><font style='color:blue;size:10'><b>Grand Total</b></font></td>\n\
                            <td width='10%' align='center'><input type='text' id='percentClomnd' value='"+jsonVarindval.MeasureGrandTotal[1]+"' size='15' readonly name='formName'></td>\n\
                            <td width='10%' align='right'><input type='text' id='percentClomnsValsId' value='"+jsonVarindval.MeasureGrandTotal[0]+"' name=''  size='15' readonly></input></td>\n\
                            <td width='10%' align='right'><input type='text' name=''  id='hiddensIds' value='"+jsonVarindval.MeasureGrandTotal[2]+"'  readonly size='15'></td>\n\
                            <td width='10%' align='right'><input type='text' name='' id='goalseekChangesId' value='"+jsonVarindval.MeasureGrandTotal[2]+"' size='15'  readonly ></td>\n\
                            <td width='10%' align='right'><input type='text' id='grandTotaValuelId'  name='' value='"+jsonVarindval.GrandTotalValue+"' readonly size='15'></td></tr></table><br>";



                html2+="<table><thead><tr><td width='20%'><input type='text' class='myTextbox3' name='vieByname' value='"+jsonVarindval.DimensionName[0]+"' readonly></td><td  width='10%' align='center'><input type='text' class='myTextbox3' name='vieByname' value='"+jsonVarindval.DimensionName[1]+"' size='15' readonly></td><td  width='10%' align='right'>\n\
                       <input type='text' class='myTextbox3' name='vieByname' value='"+jsonVarindval.DimensionName[2]+"' size='15' readonly></td><td  width='10%' align='right'><input type='text' class='myTextbox3' name='vieByname' value='"+jsonVarindval.DimensionName[4]+"' size='15' readonly></td><td width='10%' align='right'><input type='text' class='myTextbox3' name='vieByname' value='Proj. Change%' size='15' readonly></td>\n\
                       <td width='10%' align='right'><input type='text' class='myTextbox3' name='vieByname' value='Proj. Value' size='15' readonly></td></tr></thead></table><br>";

                 if(jsonVarindval.ExistingChangedPer.length=='0'){
                 for(index=0; index<jsonVarindval.DimViewByNames.length;index++){
                 html3=html3+"<table id='measuresIds"+index+"'><tr id='rowVlaus"+index+"'><td width='20%'><input type='text' name='viewBys' id='viewByIds"+index+"' value='"+jsonVarindval.DimViewByNames[index]+"' size='20' readonly></td>\n\
                            <td width='10%' align='center'><input type='text' id='percentClomnId"+index+"' value='"+jsonVarindval.currentValues[index]+"' size='15' readonly  onclick=\"changingDinamic(this.id)\"  name='currvalues'></td>\n\
                            <td width='10%' align='right'><input type='text' id='percentClomnValId"+index+"' value='"+jsonVarindval.PriorValues[index]+"' name='measureValues' size='15' readonly></input></td>\n\
                            <td width='10%' align='right'><input type='text' name='changedVals'  id='hiddenIds"+index+"' value='"+jsonVarindval.ChangedValues[index]+"'  readonly size='15'></td>\n\
                            <td width='10%' align='right'><input type='text' name='formperent' id='goalseekChangeId"+index+"' value='"+jsonVarindval.ChangedValues[index]+"' size='15' onchange=\"calculateTime(this.id,'"+jsonVarindval.DimViewByNames.length+"')\" ></td>\n\
                            <td width='10%' align='right'><input type='text' name='goalTimeIndividual' id='goalSeekValueId"+index+"' value='"+jsonVarindval.GoalSeekChangedValue[index]+"'  size='15' readonly></td></tr></table>";

                  }
                 }
                 else{
                      for(index=0; index<jsonVarindval.DimViewByNames.length;index++){
                     html3=html3+"<table id='measuresIds"+index+"'><tr id='rowVlaus"+index+"'><td width='20%'><input type='text' name='viewBys' id='viewByIds"+index+"' value='"+jsonVarindval.DimViewByNames[index]+"' size='20' readonly></td>\n\
                            <td width='10%' align='center'><input type='text' id='percentClomnId"+index+"' value='"+jsonVarindval.currentValues[index]+"' size='15' readonly  onclick=\"changingDinamic(this.id)\"  name='currvalues'></td>\n\
                            <td width='10%' align='right'><input type='text' id='percentClomnValId"+index+"' value='"+jsonVarindval.PriorValues[index]+"' name='measureValues' size='15' readonly></input></td>\n\
                            <td width='10%' align='right'><input type='text' name='changedVals'  id='hiddenIds"+index+"' value='"+jsonVarindval.ChangedValues[index]+"'  readonly size='15'></td>\n\
                            <td width='10%' align='right'><input type='text' name='formperent' id='goalseekChangeId"+index+"' value='"+jsonVarindval.ExistingChangedPer[index]+"' size='15' onchange=\"calculateTime(this.id,'"+jsonVarindval.DimViewByNames.length+"')\" ></td>\n\
                            <td width='10%' align='right'><input type='text' name='goalTimeIndividual' id='goalSeekValueId"+index+"' value='"+jsonVarindval.GoalSeekChangedValue[index]+"'  size='15' readonly></td></tr></table>";

                 }
                 }

                  parent.$("#goalTimeIndviId").dialog('open');
                   //$("#loading").hide();
                  $("#goalTimeChangeId").html(html1+html2+html3+html4+html1+html5);
             }

              });
   }
   function timeBasedDiv(REPORTID,colName,ctxPath,disColName){

              $.post(ctxPath+'/reportViewer.do?reportBy=tableChanges&tableChange=goalTimeIndividual&percentValue=goalTimeChangedPer&colName='+colName+'&disColName='+disColName+'&reportid='+REPORTID,$("#goalTimeChangeId").serialize(),
                         function(data){
                                  if(data=='Exists'){
                               alert(disColName+ "(Time Based Goal Seek)  already exists")
                              // $("#loading").hide();

                                 }
                                 else{
                                     var source =ctxPath+"/TableDisplay/pbDisplay.jsp?tabId="+REPORTID;
                                     var dSrc = document.getElementById("iframe1");
                                      dSrc.src = source;
                                      //parent.openGroupMeasureList(ctxPath,REPORTID);
                                      //$("#loading").hide();
                                 }
                          }
                   );


                           parent.$("#goalTimeIndviId").dialog('close');
   }

   function calculateTime(id,length){
      var changePer=$("#"+id).val();
      var leng=id.length
      var values=id.substring(16, leng);
      var currentValue=$("#percentClomnId"+values).val();
      var currValue=currentValue.replace(/\,/g,'');
      var calval=(currValue*changePer)/100;
      var result=parseFloat(currValue)+parseFloat(Math.round(calval*100)/100);
      var finalResut=Math.round(result*100)/100;
      $("#goalSeekValueId"+values).val(finalResut.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
      var total=0;
      var total1=0;
      for(index=0;index<length;index++){
                        var mesval=$("#goalseekChangeId"+index).val();
                        var measValue=mesval.replace(/\,/g,'');
                       if(measValue!="")
                            total=total+parseFloat(measValue);
                        var mesval1=$("#goalSeekValueId"+index).val();
                        var measValue1=mesval1.replace(/\,/g,'');
                       if(measValue1!="")
                            total1=total1+parseFloat(measValue1);
                        }
                        total=Math.round(total*100)/100
                         $("#goalseekChangesId").val(total.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
                         total1=Math.round(total1*100)/100
                         $("#grandTotaValuelId").val(total1.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));

   }

     function goalseekNewProductIntroduction(colName,disColName,REPORTID,ctxPath){
//          $.ajax({
//       url:ctxPath+'/reportViewer.do?reportBy=chkforUserPrevilages',
//       success:function(data){
//            var jsonVar=eval('('+data+')')
//            var userType=jsonVar.userType;
//            var isPowerAnalyserEnableforUser=jsonVar.isPAEnableforUser;
//           if(userType=="Admin" || isPowerAnalyserEnableforUser=="true"){
         $.post(ctxPath+'/reportViewer.do?reportBy=getNewProdItroViewBys&elementId='+colName+'&reportId='+REPORTID,
            function(data){
                 var jsonVar=eval('('+data+')');
                 var html1="<center><table><tr><td>Member Name<input type='text' name='' id='productId' value=''></td></tr></table></center><br>"
                 var html="<br><center><table><tr><td><input type='button' value='Done' class='navtitle-hover' onclick=\"newProduIntroduction('"+REPORTID+"','"+colName+"','"+ctxPath+"','"+disColName+"')\"/></td></tr></table></center>";
                 var html2="<center><table><tr><td><font size='1' color='red'>*</font>Select only One Element</td></tr></table></center>"
                parent.$("#newProductintroId").html(html1+jsonVar.htmlStr+html+html2);

                 $("#myList3").treeview({
                        animated:"slow",
                        persist: "cookie"
                    });

                    $('ul#myList3 li').quicksearch({
                        position: 'before',
                        attached: 'ul#myList3',
                        loaderText: '',
                        delay: 100
                    })
                    $(".myDragTabs").draggable({
                        helper:"clone",
                        effect:["", "fade"]
                    });
                    $("#dropTabs").droppable({
                        activeClass:"blueBorder",
                        accept:'.myDragTabs',
                        drop: function(ev, ui) {
                            createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                        }
                    }
                );
                     grpColArray=jsonVar.memberValues
                    $(".sortable").sortable();
                parent.$("#newProductintroId").dialog('open');

            });

//           }else{
//              alert("You do not have the sufficient previlages")
//           }
//       }});
           }
     function newProduIntroduction(REPORTID,colName,ctxPath,disColName){

       var oneDragble=$("ul#sortable li").size();
       var productName=$("#productId").val();

      if(oneDragble=='1'){
          if(productName==""){
              alert("Please Enter Member Name")
          }
          else{
              parent.$("#newProductintroId").dialog('close');
              individualValues(colName,disColName,REPORTID,ctxPath);
          }
      }
      else if(oneDragble=='0'){
          alert("Select Existing Member");
      }
      else{
          alert("Select  Only One Member ");
      }

     }
     function changingDinamic(id){
         var productName=$("#productValId").val();
         var length=id.length;
         var values=id.substring(14, length);
         var currentValue=$("#viewByIds"+values).val();
         if(productName==currentValue){
             $("#"+id).removeAttr("readonly");

//         var priorval=$("#percentClomnValId"+values).val();
//         var priorReplace=priorval.replace(/\,/g,'');
//         var curreVl=$("#"+id).val();
//         var currentVal=curreVl.replace(/\,/g,'');
//         var result=((parseFloat(currentVal)-parseFloat(priorReplace))/priorReplace)*100;
//         var goalVal=parseFloat(currentVal)+(parseFloat(currentVal)*result)/100;
//         $("#goalseekChangeId"+values).val(Math.round(result*100)/100);
//         $("#goalSeekValueId"+values).val(Math.round(goalVal*100)/100);
         }
     }

     function newProdDivFun(REPORTID,colName,ctxPath,disColName){
         parent.$("#goalTimeIndviId").dialog('close');
         //var goalValue=$("#goalValueId").val();
         var htmltype=$("#prodseletionId").val();
               //percentColmnId.submit();
//               var source = ctxPath+"/TableDisplay/pbDownload.jsp?dType="+htmltype+"&tabId="+REPORTID;
//               var dSrc = document.getElementById("downFrame");
//              dSrc.src = source;
          $("#loading").hide();

     }

     function goalSeekNPIPercentBase(colName,disColName,REPORTID,ctxPath){
//           $.ajax({
//       url:ctxPath+'/reportViewer.do?reportBy=chkforUserPrevilages',
//       success:function(data){
//            var jsonVar=eval('('+data+')')
//            var userType=jsonVar.userType;
//            var isPowerAnalyserEnableforUser=jsonVar.isPAEnableforUser;
//           if(userType=="Admin" || isPowerAnalyserEnableforUser=="true"){
          $.post(ctxPath+'/reportViewer.do?reportBy=getNewProdItroViewBys&elementId='+colName+'&reportId='+REPORTID,
            function(data){
                 var jsonVar=eval('('+data+')');
                 var html1="<center><table><tr><td>Enter ProductName<input type='text' name='' id='productValId' value=''></td></tr></table></center><br>"
                 var html="<br><center><table><tr><td><input type='button' value='Done' class='navtitle-hover' onclick=\"newProduIntroductionPerBase('"+REPORTID+"','"+colName+"','"+ctxPath+"','"+disColName+"')\"/></td></tr></table></center>";
                 var html2="<center><table><tr><td><font size='1' color='red'>*</font>Select only One Element</td></tr></table>"
                parent.$("#newProductintroId").html(html1+jsonVar.htmlStr+html+html2);

                 $("#myList3").treeview({
                        animated:"slow",
                        persist: "cookie"
                    });

                    $('ul#myList3 li').quicksearch({
                        position: 'before',
                        attached: 'ul#myList3',
                        loaderText: '',
                        delay: 100
                    })
                    $(".myDragTabs").draggable({
                        helper:"clone",
                        effect:["", "fade"]
                    });
                    $("#dropTabs").droppable({
                        activeClass:"blueBorder",
                        accept:'.myDragTabs',
                        drop: function(ev, ui) {
                            createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                        }
                    }
                );
                     grpColArray=jsonVar.memberValues
                    $(".sortable").sortable();
                parent.$("#newProductintroId").dialog('open');

            });
//            }else{
//              alert("You do not have the sufficient previlages")
//           }
//       }});
           }

     function newProduIntroductionPerBase(REPORTID,colName,ctxPath,disColName){

          var oneDragble=$("ul#sortable li").size();
       var productName=$("#productValId").val();

      if(oneDragble=='1'){
          if(productName==""){
              alert("Please Enter Product Name")
          }
          else{
              parent.$("#newProductintroId").dialog('close');
              npiPercentBase(colName,disColName,REPORTID,ctxPath);
          }
      }
      else if(oneDragble=='0'){
          alert("Select  Element");
      }
      else{
          alert("Select  Only One Element");
      }
     }
  function npiPercentBase(colName,disColName,REPORTID,ctxPath){

          var productName=$("#productValId").val();
                var viewByVals="";
                var colsUl=document.getElementById("sortable");
                var colIds=colsUl.getElementsByTagName("li");
                            viewByVals=viewByVals+","+colIds[0].id.replace("_li","");
                            viewByVals = viewByVals.substring(1);
         $.post(ctxPath+'/reportViewer.do?reportBy=newProductPercentBase&elementId='+colName+'&reportId='+REPORTID+'&disColName='+disColName+'&productName='+productName+'&viewByVals='+viewByVals,
              function(data){
                  var jsonVarindval=eval('('+data+')');

                   var html1="";
                   var html2="";
                   var html3="";
                   var html4="";
                   var html5="";
                   var html6="";

                 html1+="<center><table><tr><td><font size='1' color='red'>*</font><b>Model Name:</b><input type='text' name='goalName' id='modelValueId'></td><td>Save As Type:</td><td><select name='dType' id='productseletionId'><option value='html'>HTML</option><option value='excel'>Excel</option></select></td><td><input type='submit' value='Done' class='navtitle-hover' onclick=\"npiPercentBaseFunction('"+REPORTID+"','"+colName+"','"+ctxPath+"','"+disColName+"')\"/></td></tr></table></center>";
                 html5+="<table><tr><td align='right'><font style='color:blue;size:15'><b>Check For 100%</b><font><input type='checkbox' id='hunderpercheckId' name='' value=''></td></tr></table><br><br>";
                 html6+="<table><tr><td><input type='text' id='productValId' name='vieByname'  value='"+jsonVarindval.MeasuresAndGrandTotal[5]+"' style='display:none'></td></tr></table>"
//                 html2+="<table><thead><tr><td width='15%'><font style='color:blue;size:15'><b><u>"+jsonVarindval.MeasuresAndGrandTotal[0]+"</u></b></font></td><td  width='10%' align='center'><font style='color:blue;size:15'><b><u>"+jsonVarindval.MeasuresAndGrandTotal[1]+"</u></b></font></td><td  width='10%' align='right'>\n\
//                       <font style='color:blue;size:15'><b><u>//"+jsonVarindval.MeasuresAndGrandTotal[2]+"</u></b></font></td><td width='10%' align='right'><font style='color:blue;size:15'><b><u>Projected Changed%</u></b></font></td>\n\
//                       <td width='10%' align='right'><font style='color:blue;size:30'><b><u>Projected Value</u></b></font></td></tr></thead></table><br>//";

                  html2+="<table><thead><tr><td width='20%'><input type='text' class='myTextbox3' name='vieByname' value='"+jsonVarindval.MeasuresAndGrandTotal[0]+"' readonly></td><td  width='10%' align='center'><input type='text' class='myTextbox3' name='vieByname' value='"+jsonVarindval.MeasuresAndGrandTotal[1]+"' size='15' readonly></td><td  width='10%' align='right'>\n\
                      <input type='text'  name='vieByname' value='"+jsonVarindval.MeasuresAndGrandTotal[2]+"' class='myTextbox3' size='15' readonly></td><td width='10%' align='right'><input type='text' class='myTextbox3' name='vieByname' value='Proj. Change%' size='15' readonly></td>\n\
                       <td width='10%' align='right'><input type='text' class='myTextbox3' name='vieByname' value='Proj. Value' size='15' readonly></td></tr></thead></table><br>";

                 for(index=0; index<jsonVarindval.DimViewByNames.length;index++){
                        html3=html3+"<table id='measuresIds"+index+"'><tr id='rowVlaus"+index+"'><td width='20%'><input type='text' name='viewBys' id='viewByValuesIds"+index+"' value='"+jsonVarindval.DimViewByNames[index]+"' size='20' readonly></td>\n\
                            <td width='10%' align='center'><input type='text'  name='currvalues' id='measureValuesId"+index+"' value='"+jsonVarindval.MeasureValues[index]+"' size='15' readonly  ></td>\n\
                            <td width='10%' align='right'><input type='text'  name='measureValues' id='percentValuesId"+index+"' value='"+jsonVarindval.PercentValues[index]+"'  size='15' readonly></input></td>\n\
                            <td width='10%' align='right'><input type='text' name='changedVals'  id='goalChangePerId"+index+"' value='"+jsonVarindval.GoalChangePercent[index]+"' onchange=\"npiGoalChangePer(this.id,"+jsonVarindval.GoalChangePercent.length+")\"  size='15'></td>\n\
                            <td width='10%' align='right'><input type='text' name='formperent'  id='goalChangeValId"+index+"' value='"+jsonVarindval.MeasureValues[index]+"'  readonly size='15'></td></tr></table>";
                 }

                 html4+="<table><tr><td width='20%'><font style='color:blue;size:10'><b>Grand Total</b></font></td>\n\
                            <td width='10%' align='center'><input type='text' name='measureGTId' id='measGrandTotId' value="+jsonVarindval.GrandTotal+" size='15' readonly ></td>\n\
                            <td width='10%' align='right'><input type='text'  name='perentGTID' id='perCentGrandTotId' value='100%'   size='15' readonly></input></td>\n\
                            <td width='10%' align='right'><input type='text' name='goalChangeGTID'  id='projectdPerValId' value="+jsonVarindval.MeasuresAndGrandTotal[3]+"  readonly size='15'></td>\n\
                            <td width='10%' align='right'><input type='text' name='goalValueGTID'  id='projectdValId' value="+jsonVarindval.MeasuresAndGrandTotal[4]+"  readonly size='15'></td></tr></table><br>";

                  parent.$("#goalTimePercentBaseId").dialog('open');
                   //$("#loading").hide();
                  $("#goalPercentBaseId").html(html1+html5+html2+html3+html4+html1+html6);
             }

      );
  }

  function npiGoalChangePer(id,length){
      var measreGT=$("#measGrandTotId").val().replace(/\,/g,'');
      var changePer=$("#"+id).val();
      var leng=id.length
      var values=id.substring(15, leng);
      var existingPer=$("#percentValuesId"+values).val().replace(/\%/g,'');
      var measurVal=$("#measureValuesId"+values).val();
      var measerValue=measurVal.replace(/\,/g,'');
      //var calval=(measerValue*changePer)/100;
      var calval=(measreGT*changePer)/100;
      //var result=parseFloat(measerValue)+parseFloat(Math.round(calval*100)/100);
      var result=parseFloat(Math.round(calval*100)/100);
      var finalResut=Math.round(result*100)/100;
      $("#goalChangeValId"+values).val(finalResut.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
      var total=0;
      var total1=0;
                       for(index=0;index<length;index++){

                            var mesval=$("#goalChangePerId"+index).val();
                           var measValue=mesval.replace(/\,/g,'');
                             if(measValue!="")
                                 total=total+parseFloat(measValue);
                           var mesval1=$("#goalChangeValId"+index).val();
                           var measValue1=mesval1.replace(/\,/g,'');
                             if(measValue1!="")
                                 total1=total1+parseFloat(measValue1);
                      }
                        total=Math.round(total*100)/100
                         $("#projectdPerValId").val(total.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
                        total1=Math.round(total1*100)/100
                         $("#projectdValId").val(total1.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));

  }

  function npiPercentBaseFunction(REPORTID,colName,ctxPath,disColName){
      var percentCheck=document.getElementById("hunderpercheckId");
      if(percentCheck.checked){
          var changePer=$("#projectdPerValId").val();
           if(changePer==100){
               parent.$("#goalTimePercentBaseId").dialog('close');
           }
           else{
               alert("Total Percent Should be 100")
           }
      }
      else{
          parent.$("#goalTimePercentBaseId").dialog('close');
      }
  }
function scriptAlign(colName,disColName,REPORTID,ctxPath)
{
   $("#editScriptAlign").dialog({
        bgiframe: true,
        autoOpen: false,
        height: 150,
        width: 330,
        modal: true,
        title:'Edit Script Align'
    });
    colname=colName
    discolname=disColName
    reportid=REPORTID
    ctxpath=ctxPath
    $("#saveAlign").html("<input type='button' value='Done' onclick='saveScriptAlign()'>")
            $("#editScriptAlign").dialog('open');

           }
function saveScriptAlign()
{
   $("#editScriptAlign").dialog('close');
    var alignment = $("#ScrpitAlign").val()
        var ctxPath=document.getElementById("ctxPath").value;
   $.ajax({
        url:ctxpath+'/reportViewer.do?reportBy=tableChanges&tableChange=scriptAlign&colName='+colname+'&reportid='+reportid+'&textAlign='+alignment,
        success:function(data){
                var source = ctxpath+"/TableDisplay/pbDisplay.jsp?tabId="+reportid;
                var dSrc = document.getElementById("iframe1");
                dSrc.src = source;
        }
    });

}
function saveScriptAlignnew(alignment,ctxpath,reportid,colname)
{
//   $("#editScriptAlign").dialog('close');
//    var alignment = $("#ScrpitAlign").val()
        var ctxPath=document.getElementById("ctxPath").value;
   $.ajax({
        url:ctxpath+'/reportViewer.do?reportBy=tableChanges&tableChange=scriptAlign&colName='+colname+'&reportid='+reportid+'&textAlign='+alignment,
        success:function(data){
            var HeaderColor="Progen";
                var source = ctxpath+"/TableDisplay/pbDisplay.jsp?tabId="+reportid+"&HeaderColorNew="+HeaderColor+"&elemntIdNew="+colname;
                var dSrc = document.getElementById("iframe1");
                dSrc.src = source;
        }
    });

}
function measureAlign(colName,disColName,REPORTID,ctxPath)
{
   $("#editmeasureAlign").dialog({
        bgiframe: true,
        autoOpen: false,
        height: 150,
        width: 330,
        modal: true,
        title:'Edit Measure Align'
    });
    colname=colName
    discolname=disColName
    reportid=REPORTID
    ctxpath=ctxPath
    $("#savemeasureAlign").html("<input type='button' value='Done' onclick='saveMeasureAlign()'>")
            $("#editmeasureAlign").dialog('open');

           }
function saveMeasureAlign()
{
   $("#editmeasureAlign").dialog('close');
    var alignment = $("#MeasureAlign").val()
        var ctxPath=document.getElementById("ctxPath").value;
   $.ajax({
        url:ctxpath+'/reportViewer.do?reportBy=tableChanges&tableChange=measureAlign&colName='+colname+'&reportid='+reportid+'&measureAlign='+alignment,
        success:function(data){
                var source = ctxpath+"/TableDisplay/pbDisplay.jsp?tabId="+reportid;
                var dSrc = document.getElementById("iframe1");
                dSrc.src = source;
        }
    });

}
function saveMeasureAlignnew(alignment,ctxpath,reportid,colname)
{
//   $("#editmeasureAlign").dialog('close');
//    var alignment = $("#MeasureAlign").val()
        var ctxPath=document.getElementById("ctxPath").value;
   $.ajax({
        url:ctxpath+'/reportViewer.do?reportBy=tableChanges&tableChange=measureAlign&colName='+colname+'&reportid='+reportid+'&measureAlign='+alignment,
        success:function(data){
            var HeaderColor="Progen";
                var source = ctxpath+"/TableDisplay/pbDisplay.jsp?tabId="+reportid+"&HeaderColorNew="+HeaderColor+"&elemntIdNew="+colname;
                var dSrc = document.getElementById("iframe1");
                dSrc.src = source;
        }
    });

}
  function drillMeasure(REPORTID,ctxpath,elementId,dimName){

                      var elemetId=elementId.replace(/\A_/g,'')
                        var frameObj=document.getElementById("drillmeasFrame");
                        var source="EnableMeasureDrill.jsp?tableIds="+elemetId+'&REPORTID='+REPORTID+'&dimName='+dimName;
                        frameObj.src=source;
                       $("#drillMeasuresId").dialog('open')
  }
  function relatedMeasures(REPORTID,ctxpath,elementId,dimName){
      $("#relatedMeasuresId").html("");
                    $("#relatedMeasuresId").dialog('open')
                      var elemetId=elementId.replace(/\A_/g,'')
                      var relatedMeasuresId = document.getElementById("relatedMeasuresId");
                    $("#relatedMeasuresId").html('<center><img id="imgId" src=\"images/ajax.gif\" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>');
                        $.ajax({
        url:ctxpath+'/reportViewer.do?reportBy=getRelatedMeasureDetails&reportId='+REPORTID+'&elementId='+elementId,
        success:function(data){
            if(data == ""){
                $("#relatedMeasuresId").dialog('close')
                alert("No related measures for this measure")
            }else{
                $("#relatedMeasuresId").html(data);
            }
        }
                        });
  }
  function saveDimensionValues(selectedParam,dimvalue,paramFilterName,reportId,measEleId,dimtype,cxtPath){
              parent.$("#dimTypeDiv").dialog('close');
            var mbrs="''"+$("#"+dimvalue).val()+"''";
           $.ajax({
        url:cxtPath+'/reportViewer.do?reportBy=getDimenssionDetails&selectedParam='+selectedParam,
        success:function(data){
            var jsonVar=eval('('+data+')')
            var dimBussTabId=jsonVar.dimBussTabId;
            var dimElementId=jsonVar.dimElementId;
            var dimTabColName=jsonVar.busstabName;
            parent.$("#paramFilterDiv").dialog('close');
             $.ajax({
                    url: encodeURI(cxtPath+'/reportViewer.do?reportBy=getParamFilterMbrs&mbrs='+mbrs+'&paramFilterName='+paramFilterName+'&dimBussTabId='+dimBussTabId+'&reportId='+reportId+'&measEleId='+measEleId+'&dimTabColName='+dimTabColName+'&dimElementId='+dimElementId+'&dimType='+dimtype),
                    success: function(data){
                        parent.$("#paramFilterMemberDiv").dialog('close');
                        $.ajax({
                            url:cxtPath+'/reportViewer.do?reportBy=tableMeasureChanges&tableMsrs='+data+'&REPORTID='+reportId,
                            success: function(data){
//                                if(ViewFrom=="Designer")
//                                    parent.dispTable("measChange");
//                                else
                                    parent.submitFormMeasChange();
                            }
                        });
                    }
                });
        }


        });
    }
         function addMeasure(condition,id){

             var table = document.getElementById("mTable");
             var rowCount = table.rows.length;
            var firstTr=$("#mTable tr:first").html()
            if(firstTr!=null){

                if(condition=='FactFilter'){
                    firstTr=firstTr.toString().replace("checkcmpMsr0", "checkcmpMsr"+rowCount,"gi")
                    firstTr=firstTr.toString().replace("cmpMsr0", "cmpMsr"+rowCount,"gi")
                    firstTr=firstTr.toString().replace("cmpMsrName0", "cmpMsrName"+rowCount,"gi")
                    firstTr=firstTr.toString().replace("endcmpMsr0", "endcmpMsr"+rowCount,"gi")
                    firstTr=firstTr.toString().replace("endcmpMsrName0", "endcmpMsrName"+rowCount,"gi")
                }
              firstTr=firstTr.toString().replace("Cond0", "Cond"+rowCount,"gi")
              firstTr=firstTr.toString().replace("mName0", "mName"+rowCount,"gi")
              firstTr=firstTr.toString().replace("mCond0", "mCond"+rowCount,"gi")
              firstTr=firstTr.toString().replace("mvaluesTD0", "mvaluesTD"+rowCount,"gi")
              firstTr=firstTr.toString().replace("mValues0", "mValues"+rowCount,"gi")
              firstTr=firstTr.toString().replace("endValue0", "endValue"+rowCount,"gi")
              firstTr=firstTr.toString().replace("eValues0", "eValues"+rowCount,"gi")
              firstTr=firstTr.toString().replace("add0", "add"+rowCount,"gi")
              firstTr=firstTr.toString().replace("delete0", "delete"+rowCount,"gi")
              $('#mTable tr:last').after('<tr>'+firstTr+'</tr>')
               $("#eValues"+rowCount).hide();
              $("#Cond"+rowCount).show();
              if(condition=='FactFilter'){

                  $("#mName"+rowCount+" option[value='@PROGENTIME@@ST_DATE']").remove();
                  $("#mName"+rowCount+" option[value='@PROGENTIME@@ED_DATE']").remove();
                  $("#cmpMsrName"+rowCount+" option[value='@PROGENTIME@@ST_DATE']").remove();
                  $("#cmpMsrName"+rowCount+" option[value='@PROGENTIME@@ED_DATE']").remove();
                  $("#endcmpMsrName"+rowCount+" option[value='@PROGENTIME@@ST_DATE']").remove();
                  $("#endcmpMsrName"+rowCount+" option[value='@PROGENTIME@@ED_DATE']").remove();

                    $("#cmpMsr"+rowCount).hide();
                    $("#endcmpMsrName"+rowCount).hide();
                    $("#mvaluesTD"+rowCount).show();
                    $("#endValue"+rowCount).show();
                    $('#mCond'+rowCount).empty();
                var selectValues={"in":"in","not in":"not in","like":"like","not like":"not like","=":"=",">":">","<":"<",">=":">=","<=":"<=","<>":"<>","between":"between","is null":"is null","is not null":"is not null"};
                $.each(selectValues, function(key, value) {
                    $('#mCond'+rowCount)
                        .append($("<option></option>")
                        .attr("value",key)
                        .text(value));
                            });

                }
                }
//                if(rowCount=='1'){
//                $("#Cond0").hide();
//                }
            // $("#mTable").append('<tr>'+$("#mTable tr").html()+'</tr>');

          }
          function deleteMeasure(id){
              var id=id.replace("delete","");
              var table = document.getElementById("mTable");
              var rowCount = table.rows.length;
              if(rowCount>1)
                  $('#mTable tr:last').remove();
              else
              alert("you can not delete all the Rows");
          }
          function changeMeasureVal(cxtPath,factName,id){
             // alert("changeMeasureVal")
              var id1;
                 if (id.indexOf("mValues")!=-1)
                 id1=id.replace("mValues","");
                 else
                 id1=id.replace("eValues","");
                var measureId=$("#mName"+id1).val();
              var measureCond=$("#mCond"+id1).val();

//              if(measureCond=="in" || measureCond=="not in" || measureCond=="between"){ // modified by Nazneen on 30/10/14
              if(measureCond=="in" || measureCond=="not in"){
                  parent.$("#paramFilterMemberDiv").dialog({
                        autoOpen: false,
                        height: 600,
                        width: 720,
                        position: 'justify',
                        modal: true
                    });
                     parent.$("#paramFilterMemberDiv").dialog('open');
                     $("#paramFilterMember").html("")
                    var frameObj=parent.document.getElementById("paramFilterMember");
                     var source =cxtPath+'/TableDisplay/pbParamFilterMembers.jsp?&selectedParam='+measureId+'&type=facts'+'&Id='+id+'&factName='+factName;
                    var encodedSource = encodeURI(source);
                    frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
                    frameObj.src=encodedSource;

              }
               if(measureCond=="is null")
                      {
                           $("#mvaluesTD"+id1).hide();
            }
                       else if(measureCond=="is not null")
                           {
                          $("#mvaluesTD"+id1).hide();
                           }
                        else
                            {
                            $("#mvaluesTD"+id1).show();
                            }
                     parent.$("#paramFilterMemberDiv").dialog('open');
                     $("#paramFilterMember").html("")
                    var frameObj=parent.document.getElementById("paramFilterMember");
                     var source =cxtPath+'/TableDisplay/pbParamFilterMembers.jsp?&selectedParam='+measureId+'&type=facts'+'&Id='+id+'&factName='+factName;
                    var encodedSource = encodeURI(source);
                    frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
                    frameObj.src=encodedSource;
            }
            function saveFactDetails(cxtPath,reportId,measEleId,paramFilterName,folderId,subfolderId){
                //alert("save"+reportId+","+measEleId+paramFilterName+dimBussTabId+bussTableName)
                $.post(cxtPath+'/reportViewer.do?reportBy=getParamFilterMbrs&paramFilterName='+paramFilterName+'&folderId='+folderId+'&reportId='+reportId+'&measEleId='+measEleId+'&subfolderId='+subfolderId+'&Type=facts'+'&checkCmpMsrids='+checkCmpMsrids,$("#paramFilterMeasureValFrm").serialize(),
                    function(data){
                        parent.$("#paramFilterMeasureValues").dialog('close');
                        $.ajax({
                            url:cxtPath+'/reportViewer.do?reportBy=tableMeasureChanges&tableMsrs='+data+'&REPORTID='+reportId,
                            success: function(data){
//                                if(ViewFrom=="Designer")
//                                    parent.dispTable("measChange");
//                                else
                                    parent.submitFormMeasChange();
                            }
                        });
                    }
                );
            }
            function changeMsrOption(id){
                var id1=id.replace("mCond","")
                 var measureCond=$("#"+id).val();
                  if($("#checkcmpMsr"+id1).is(':checked')){
                       if(measureCond=="between")
                           $("#endcmpMsrName"+id1).show();
                       else
                          $("#endcmpMsrName"+id1).hide();
                  }else{
                       if(measureCond=="between")
                         $("#eValues"+id1).show();
                        else
                      $("#eValues"+id1).hide();
                  }
                   if(measureCond=="is null")
                      {
                           $("#mvaluesTD"+id1).hide();
            }
                       else if(measureCond=="is not null")
                           {
                          $("#mvaluesTD"+id1).hide();
                           }
                        else
                            {
                              if($("#checkcmpMsr"+id1).is(':checked'))
                             $("#mvaluesTD"+id1).hide();
                             else
                            $("#mvaluesTD"+id1).show();
                            }
            }
function addConversionFormula(columnName,disColumnName,REPORTID,path){
     if($.browser.msie== true)
    {
    $("#conversionFormula").dialog({
                autoOpen: false,
                height: 280,
                width: 400,
                position: 'justify',
                modal: true,
                resizable:false
                });
    }else{
       $("#conversionFormula").dialog({
                autoOpen: false,
                height: 280,
                width: 400,
                position: 'justify',
                modal: true,
                resizable:false
                });
    }
    colname=columnName;
    discolname=disColumnName;
    reportid=REPORTID;
    $("#newMsr").val("");
    $("#oldMsr").val(discolname);
    $.ajax({
       url:path+'/reportViewer.do?reportBy=chkMsrforConversionFormula&columnName='+columnName,
       success:function(data){
           //alert(data)
            if(data=='0')
                alert("Conversion Formula Applied For Base Measure")
            else{
                $("#conversionFormula").dialog('open');
            }
       }
   });

}

 function addSignConversion(columnName,disColumnName,REPORTID,path){
     if($.browser.msie== true)
    {
    $("#SignConversion").dialog({
                autoOpen: false,
                height: 280,
                width: 400,
                position: 'justify',
                modal: true,
                resizable:false
                });
    }else{
       $("#SignConversion").dialog({
                autoOpen: false,
                height: 280,
                width: 400,
                position: 'justify',
                modal: true,
                resizable:false
                });
    }
    colname=columnName;
    discolname=disColumnName;
    reportid=REPORTID;
    $("#newMsr1").val("");
    $("#oldMsr1").val(discolname);
    $.ajax({
       url:path+'/reportViewer.do?reportBy=chkMsrforConversionFormula&columnName='+columnName,
       success:function(data){
           //alert(data)
            if(data=='0')
                alert("Sign Conversion  Applied For Base Measure")
            else{
                $("#SignConversion").dialog('open');
            }
       }
   });

}
  function saveConversion(cxtPath){
      //alert(cxtPath)

      if(trim(parent.$("#newMsr").val())!=""){
        var paramFilterName=parent.$("#newMsr").val();
        var mbrs=parent.$("#divideby").val();
        $.ajax({
         url: encodeURI(cxtPath+'/reportViewer.do?reportBy=getParamFilterMbrs&mbrs='+mbrs+'&paramFilterName='+paramFilterName+'&reportId='+reportid+'&measEleId='+colname+'&Type=ConversionFormula'),
                    success: function(data){
                        $("#conversionFormula").dialog('close');
                         $.ajax({
                            url:cxtPath+'/reportViewer.do?reportBy=tableMeasureChanges&tableMsrs='+data+'&REPORTID='+reportid,
                            success: function(data){
                                    parent.submitFormMeasChange();
                            }
                        });
                    }
                });
      }else{
          alert("Plz enter New MeasureName");
      }
  }
  function SaveSignConversion(cxtPath){
//      alert(cxtPath)
    if(trim(parent.$("#newMsr1").val())!=""){
    var paramFilterName=parent.$("#newMsr1").val();
    var mbrs=-1;
        $.ajax({
         url: encodeURI(cxtPath+'/reportViewer.do?reportBy=getParamFilterMbrs&mbrs='+mbrs+'&paramFilterName='+paramFilterName+'&reportId='+reportid+'&measEleId='+colname+'&Type=ConversionFormula&FromSignConversion=true'),
                    success: function(data){
                        $("#SignConversion").dialog('close');
                         $.ajax({
                            url:cxtPath+'/reportViewer.do?reportBy=tableMeasureChanges&tableMsrs='+data+'&REPORTID='+reportid,
                            success: function(data){
                                    parent.submitFormMeasChange();
                            }
                        });
                    }
                });
                }else{
          alert("Plz enter New MeasureName");
      }
  }
  function isZeroKey(id){
    if(parent.$("#divideby").val()=="0"){
          alert("Plz enter the number don't start with zero")
          parent.$("#divideby").val("")
      }
  }
  function advanceParamter(reportId,ctxPath){
//       $.ajax({
//       url:ctxPath+'/reportViewer.do?reportBy=chkforUserPrevilages',
//       success:function(data){
//            var jsonVar=eval('('+data+')')
//            var userType=jsonVar.userType;
//            var isPowerAnalyserEnableforUser=jsonVar.isPAEnableforUser;
//           if(userType=="Admin" || isPowerAnalyserEnableforUser=="true"){

      $.post(ctxPath+'/reportViewer.do?reportBy=getViewbysRelatedReport&reportid='+reportId,
         function(data){
             if(data!='null'){
            var jsonVar=eval('('+data+')')
             var viewbyIdName=jsonVar.ViewByIdNames;
             var viewbyIds=jsonVar.ViewByIds;
             var reports=jsonVar.reports;
             var dependent = jsonVar.assignedIds;
//             alert("dependent"+dependent)
//             alert("viewbyIds"+viewbyIds.length)
             var conditions=jsonVar.conditions;
             var p=0;
             var html;
             var checkedList=new Array();
//             alert("viewbyIdName.length:"+viewbyIdName.length);
             html="<table align='center'>"
             for(var i=0;i<viewbyIdName.length;i++){
                 var viewArr=new Array();
                 var checked="";
                 var showSlected="none";
                 var isAndSelected="selected";
                 var isOrSelected="";
                 if(dependent[i]!='')
                     {

                         checked="checked";
                         showSlected="block";
//                         alert("checked"+checked)
//                         alert("showSlected"+showSlected)
                     }
                 if(conditions[i]=='and' || conditions[i]=='')
                     {
                         isAndSelected="selected";
                     }
                     else
                         {
                             isOrSelected="selected";
                         }

                 html+="<tr>";
                 viewArr=viewbyIdName[i].split("&");
                 html+="<td><input type='text' name='viewByName' id='viewByName"+viewArr[1]+"' style='width:100px;' value='"+viewArr[0]+"' readonly></td>"
                 html+="<td><input type='hidden' name='viewByID' id='viewByID"+viewArr[1]+"' style='width:100px;' value='"+viewArr[1]+"'></td>"
                 html+="<td><select id='condition"+viewArr[1]+"' name='condition' style='width:100px;'><option value='and' "+isAndSelected+">and</option><option value='or' "+isOrSelected+">or</option></td>";
                 html+="<td><input type='checkbox' name='checkReport' id='checkReport"+viewArr[1]+"' "+checked+" onclick=\"shwViewByrltdReports('"+viewArr[1]+"')\"></td>";
                 checkedList.push(viewArr[1]);
                 html+="<td><select id='Report"+viewArr[1]+"' name='reportName' style='width:180px;display:"+showSlected+";'>";
                 for(var j=0;j<viewbyIds.length;j++){

                     if(viewArr[1]==viewbyIds[j])
                         {
                             for(p;p<reports.length;p++){
                             var isSelected="";
                             if(reports[p]!="a12ab"){
                             var reportsIdName=reports[p].split("&&");
                             if(dependent[i]==reportsIdName[1])
                                 {
//                                     alert("dependent[i]"+dependent[i])
//                                     alert("reportsIdName[1]"+reportsIdName[1])
                                     isSelected='Selected';
                                 }
                            html+="<option  "+isSelected+"  value='"+reportsIdName[1]+"'>"+reportsIdName[0]+"</option>";
                             }else{
                                 p++;
                                 break;

                             }
                                                 }
                         }

                 }
                 html+="</select></td></tr>";
             }
            html+="<tr><td>&nbsp</td></tr><br><br>";
            html+="<tr><td align='center' colspan='4'><input type='button' value='save' class='navtitle-hover' onclick=\"saveAdvanceParamters('"+reportId+"','"+ctxPath+"','"+checkedList+"')\"></td></tr>";
            html+="</table>";
//            alert("checkedList.length"+checkedList.length);
            parent.$("#advanceParamFrm").html(html);
        }
         });
         $("#advanceParamDiv").dialog('open');
//          }else{
//              alert("You do not have the sufficient previlages")
//           }
//       }});

  }
  var testvalue=new Array();
  var checkViewbyId=new Array();
  var uncheckViewbyId=new Array();
  var checkdepentReportIds=new Array();
   var checkdepentReportIds1=new Array();
  function shwViewByrltdReports(id){
      if($("#checkReport"+id).is(':checked')){
            $("#Report"+id).show();
             testvalue.push("checkReport"+id);

      }
      else{
           checkViewbyId.pop(id);
           testvalue.pop("checkReport"+id);
           checkdepentReportIds.pop($("#Report"+id).val())
            $("#Report"+id).hide();
      }
  }
  function saveAdvanceParamters(reportId,ctxPath,checkedList){

             var viewbyIds=checkedList.split(",");
             for(var i=0;i<viewbyIds.length;i++){
                  if($("#checkReport"+viewbyIds[i]).is(':checked')){
                      checkViewbyId.push(viewbyIds[i]);
                      checkdepentReportIds.push($("#Report"+viewbyIds[i]).val());
                  }
                 if(checkViewbyId==""  )
                     {
                           uncheckViewbyId.push(viewbyIds[i]);
                           checkdepentReportIds1.push($("#Report"+viewbyIds[i]).val());
             }
             }
//             alert(checkdepentReportIds.toString())
//            for(var i=0;i<checkViewbyId.length;i++){
//                checkdepentReportIds.push($("#Report"+checkViewbyId[i]).val())
//            }

            $.post(ctxPath+'/reportViewer.do?reportBy=saveAdvanceParameters&reportId='+reportId+'&CheckViewbyId='+checkViewbyId+'&checkdepentReportIds='+checkdepentReportIds+'&checkdepentReportIds1='+checkdepentReportIds1+'&uncheckViewbyId='+uncheckViewbyId, $("#advanceParamFrm").serialize() ,
             function(data){
                $("#advanceParamDiv").dialog('close');
                var path=ctxPath+"/reportViewer.do?reportBy=viewReport&REPORTID="+reportId;
                parent.submiturls1(path);

             });



  }
  function viewAdhocDrill(reportId,ctxPath,nextViewById,measureName,viewById,presentViewById,adhocDrillType,url,paramurl){
      var presentViewBy=presentViewById.replace(/\A_/g,'')
     // alert(presentViewBy)
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
             //alert(url)
            // var path=ctxPath+"/reportViewer.do?reportBy=viewReport&action=ChangeViewBy&adhocChangeViewby=true&REPORTID="+reportId +"&CBOARP" + presentViewBy + "="+measureName;
             var path=ctxPath+"/reportViewer.do?reportBy=viewReport&action=ChangeViewBy&adhocChangeViewby=true&REPORTID="+reportId +url+"&adhocviewby=true";
           // alert(path)
            // var path=ctxPath+"/reportViewer.do?reportBy=viewReport&action=ChangeViewBy&adhocChangeViewby=true&REPORTID="+reportId +"&DDrill=Y&DrillMonth=Apr-2011";
             parent.submiturls1(path);

                     });
      }else{
          var paramUrl=paramurl;
       //alert("123"+paramUrl+".."+presentViewById+",,,,,"+nextViewById)
          if(paramUrl.indexOf(presentViewBy)!=-1)
         {
             if(nextViewById=="Day" || nextViewById=="Week" || nextViewById=="Month" || nextViewById=="Qtr" || nextViewById=="Year")
               paramUrl=paramUrl.replace(presentViewBy,"TIME");
               else
               paramUrl=paramUrl.replace(presentViewBy,nextViewById);
         }
          var path;
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
            //  alert(path)
                //path=ctxPath+'/reportViewer.do?reportBy=viewReport&REPORTID='+reportId+"&CBO_PRG_PERIOD_TYPE="+nextViewById+"&DDrill=Y";
           }
          else
          path=url+measureName+"&DDrillAcross=Y&DDrill=Y"+paramUrl+"&adhocviewby=true";
         // path=url+measureName+"&DDrillAcross=Y&CBOVIEW_BY" + viewById + "=" + nextViewById ;
       }
            else{
          var cboarpArray=new Array();
          cboarpArray.push(measureName);
          if(nextViewById=="Day" || nextViewById=="Week" || nextViewById=="Month" || nextViewById=="Qtr" || nextViewById=="Year")
          path=ctxPath+'/reportViewer.do?reportBy=viewReport&REPORTID='+reportId+"&CBO_PRG_PERIOD_TYPE="+nextViewById+"&DDrill=Y&CBOARP" + presentViewBy+ "="+JSON.stringify(cboarpArray) +paramUrl+"&adhocviewby=true";
         // path=ctxPath+'/reportViewer.do?reportBy=viewReport&REPORTID='+reportId+"&CBO_PRG_PERIOD_TYPE="+nextViewById+"&DDrill=Y&CBOVIEW_BY" + viewById + "=TIME&CBOARP" + presentViewBy + "="+measureName;
          else
          path=ctxPath+'/reportViewer.do?reportBy=viewReport&REPORTID='+reportId+"&CBOARP" +presentViewBy+ "="+JSON.stringify(cboarpArray) +paramUr+"&adhocviewby=true";
         // path=ctxPath+'/reportViewer.do?reportBy=viewReport&REPORTID='+reportId+"&CBOVIEW_BY" + viewById + "=" + nextViewById +"&CBOARP" + presentViewBy + "="+measureName;
        // alert(path)
            }
        submiturls(path);
        }
      }

//  function viewAdhocDrillforTableHeader(reportId,ctxPath,nextViewById,viewById,presentViewById,adhocDrillType){
//       var presentViewBy=presentViewById.replace(/\A_/g,'')
//      if(adhocDrillType=="drillside"){
//      $.post(ctxPath+"/reportViewer.do?reportBy=adhocChangeViewBy&newViewById="+nextViewById+"&reportId="+reportId,
//          function(data){
//                   var path=ctxPath+"/reportViewer.do?reportBy=viewReport&action=ChangeViewBy&adhocChangeViewby=true&REPORTID="+reportId;
//                   parent.submiturls1(path);
//
//                        });
//      }else{
//       var path=ctxPath+'/reportViewer.do?reportBy=viewReport&REPORTID='+reportId+"&CBOVIEW_BY" + viewById + "=" + nextViewById;
//       submiturls(path);
//      }
//  }
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
                 var path=ctxPath+"/reportViewer.do?reportBy=viewReport&action=ChangeViewBy&adhocChangeViewby=true&REPORTID="+reportId+"&adhocviewby=true";
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
           var path=ctxPath+'/reportViewer.do?reportBy=viewReport&REPORTID='+reportId+url+"&adhocviewby=true";
             submiturls(path);
          }
     }
     

  }

  function displayRepSpanTimeInfo(repId){
//      alert("in display Rep timem span")
     $.post(
                    'reportViewer.do?reportBy=getRepTimeSpanDisplay&repId='+repId+'&from=frombutton',
                     function(data){
                         $('#reptimeDetails').html(data);
              document.getElementById('reptimeDetails').style.display="";
                     });
//                   var htmldata = "<div id='timeinfo' style='position:absolute;width:150px;left:600px;top:200px;'><table border='0' width='100px' class='mycls' cellspacing='0' cellpadding='0' ><tr onmousemove=divMove('timeinfo') class='navtitle1' id='"+repId+"tr'><td id='titleBar'  style='cursor:move' width='80%' id='"+repId+"td'>Hai</td></tr></table></div>";


               }


function setNewId(did)
{
    insertedNew.lang = did;
}
function divMove(objId){
    whichDog=document.getElementById(objId);
}

function ddInit(e){
    topDog=isIE ? "BODY" : "HTML";
    hotDog=isIE ? event.srcElement : e.target;
    while (hotDog.id!="titleBar"&&hotDog.tagName!=topDog){
        hotDog=isIE ? hotDog.parentElement : hotDog.parentNode;
    }

    if (hotDog.id=="titleBar"){

        offsetx=isIE ? event.clientX : e.clientX;
        offsety=isIE ? event.clientY : e.clientY;
        nowX=parseInt(whichDog.style.left);
        nowY=parseInt(whichDog.style.top);
        ddEnabled=true;
        document.onmousemove=dd;

    }
}
function dd(e){
    if (!ddEnabled) return;
    whichDog.style.left=isIE ? nowX+event.clientX-offsetx : nowX+e.clientX-offsetx;
    whichDog.style.top=isIE ? nowY+event.clientY-offsety : nowY+e.clientY-offsety;
    xpost=isIE ? nowX+event.clientX-offsetx : nowX+e.clientX-offsetx;
    ypost=isIE ? nowY+event.clientY-offsety : nowY+e.clientY-offsety;

    whichDog.style.left = xpost+"px";
    whichDog.style.top = ypost+"px";

    return false;
}
function xy(){
    ddEnabled=false;
}
function closeRepSpan(repId){
    document.getElementById('reptimeDetails').style.display="none";
}
function addFactFilter(columnName,disColumnName,REPORTID,path){
       if($.browser.msie== true)
    {
        $("#paramFilterDiv").dialog({
        autoOpen: false,
        height: 300,
        width: 350,
        position: 'justify',
        modal: true
    });

    }
    else
    {
        $("#paramFilterDiv").dialog({
        autoOpen: false,
        height: 200,
        width: 350,
        position: 'justify',
        modal: true
    });

    }
     $("#paramFilterDiv").dialog('option','title','Fact Filter');
     $("#paramFilterDiv").dialog('open');

    $.ajax({
        url:path+'/reportViewer.do?reportBy=showViewBy&REPORTID='+REPORTID,
        success:function(data){
            var frameObj=parent.document.getElementById("paramFilter");
            var source =path+'/TableDisplay/pbParameterFilter.jsp?reportId='+REPORTID+'&columnName='+columnName+'&disColumnName='+disColumnName+'&FactFilter=FactFilter';
            frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
            frameObj.src=source;
        }
    });

  }
  var checkCmpMsrids=new Array()
  function checkforcompareMsr(id){
      var id1=id.replace("checkcmpMsr","")
      if($("#"+id).is(':checked')){
      checkCmpMsrids.push(id1)
      $("#mvaluesTD"+id1).hide();
      $("#endValue"+id1).hide();
      $("#cmpMsr"+id1).show();
      $("select#mCond"+id1+" option[value='in']").remove();
      $("select#mCond"+id1+" option[value='not in']").remove();
      $("select#mCond"+id1+" option[value='like']").remove();
      $("select#mCond"+id1+" option[value='not like']").remove();
      if($("#DateId").is(':checked')){
          $("#mName"+id1).append('<option value="@PROGENTIME@@ST_DATE">$Progen_Start_Date</option><option value="@PROGENTIME@@ED_DATE">$Progen_End_Date</option>');
          $("#cmpMsrName"+id1).append('<option value="@PROGENTIME@@ST_DATE">$Progen_Start_Date</option><option value="@PROGENTIME@@ED_DATE">$Progen_End_Date</option>');
          $("#endcmpMsrName"+id1).append('<option value="@PROGENTIME@@ST_DATE">$Progen_Start_Date</option><option value="@PROGENTIME@@ED_DATE">$Progen_End_Date</option>');
      }

      }else{
      checkCmpMsrids.pop(id1);
      $("#mvaluesTD"+id1).show();
      $("#endValue"+id1).show();
      $("#cmpMsr"+id1).hide();
      $("#endcmpMsrName"+id1).hide();
      $('#mCond'+id1).empty();
       var selectValues={"in":"in","not in":"not in","like":"like","not like":"not like","=":"=",">":">","<":"<",">=":">=","<=":"<=","<>":"<>","between":"between"};
      $.each(selectValues, function(key, value) {
        $('#mCond'+id1)
         .append($("<option></option>")
         .attr("value",key)
         .text(value));
        });
                 $("#mName"+id1+" option[value='@PROGENTIME@@ST_DATE']").remove();
                  $("#mName"+id1+" option[value='@PROGENTIME@@ED_DATE']").remove();
                  $("#cmpMsrName"+id1+" option[value='@PROGENTIME@@ST_DATE']").remove();
                  $("#cmpMsrName"+id1+" option[value='@PROGENTIME@@ED_DATE']").remove();
                  $("#endcmpMsrName"+id1+" option[value='@PROGENTIME@@ST_DATE']").remove();
                  $("#endcmpMsrName"+id1+" option[value='@PROGENTIME@@ED_DATE']").remove();

      }
      }
      function saveFactFilterDtls(cxtPath,reportId,measEleId,paramFilterName,folderId,subfolderId){
         //added by Nazneen
         var priority = "";
         var dimVal = "";
         if($("#dependentDims").is(':checked')){
             dimVal = parent.$("#dependentDimsName").val()
             priority = parent.$("#priority").val()
          }

//        alert("checkCmpMsrids"+checkCmpMsrids.toString())

                $.post(cxtPath+'/reportViewer.do?reportBy=saveFactFilterDetails&factFilterName='+paramFilterName+'&folderId='+folderId+'&reportId='+reportId+'&measEleId='+measEleId+'&subfolderId='+subfolderId+'&checkCmpMsrids='+checkCmpMsrids+'&dimVal='+dimVal+'&priority='+priority,$("#paramFilterMeasureValFrm").serialize(),
                    function(data){
                        parent.$("#paramFilterMeasureValues").dialog('close');
                        window.location.href = window.location.href;
                    }
                );

      }
      // modified by krishan
      function deleteFormula(ctxtPath,reportId){
         // alert(ctxtPath+reportId)
          var filterId=new Array();
          $(".filetrIdClass:checked").each(
                 function() {
                   var a=$(this).val();
                   if(a=="true"){
                  }
                  else
                  filterId.push(a)

//                   filterId.push(a)
               });
               $.ajax({
        url:ctxtPath+'/reportViewer.do?reportBy=deleteFactFormulas&reportId='+reportId+'&filterId='+filterId,
        success:function(data){
                    if(data=='success'){
                        parent.$("#viewFactformulaDiv").dialog('close');
                         window.location.href = window.location.href;
                    }
                 }
             });
      }
  function reportDrillAssignment(ctxtPath,reportId,multireport){
      
    $.post(ctxtPath+'/reportViewer.do?reportBy=reportDrillAssignment&reportId='+reportId,
                 function(data){
                if(data!='null'){
                    var jsonVar=eval('('+data+')')
                    var roleName=jsonVar.roleName;
                    var MsrIds=jsonVar.MsrIds;
                    var MsrNames=jsonVar.MsrNames;
                    var reportIds=jsonVar.reportIds;
                    var reportNames=jsonVar.reportNames;
                    var assignRepIds=jsonVar.assignRepIds;
                    var htmlVar="";

                     var displayRepo="";
                     var displayRepo1="";
                        if(multireport=='multi report'){
                        displayRepo="none";
                    htmlVar+="<table width='100%'><tr><td width='40%'><input type='radio' name='reportselection' id='singlereport' class='gFontFamily fFontSize12' onclick=\"selectSingleReport('"+MsrIds+"')\" value='single report'>Single Report</td>";
                    htmlVar+="<td colspan='2' width='60%'><input type='radio' name='reportselection' id='multireport' class='gFontFamily fFontSize12' onclick=\"selectMultieReport('"+MsrIds+"')\" value='multi report' checked>Multi Report</td></tr></br>";
                            }
                         else{
                       displayRepo1="none";
                    htmlVar+="<table width='100%'><tr><td width='40%'><input type='radio' name='reportselection' id='singlereport' class='gFontFamily gFontSize12' onclick=\"selectSingleReport('"+MsrIds+"')\" value='single report' checked>Single Report</td>";
                    htmlVar+="<td colspan='2' width='60%'><input type='radio' name='reportselection' id='multireport' class='gFontFamily gFontSize12' onclick=\"selectMultieReport('"+MsrIds+"')\" value='multi report'>Multi Report</td></tr></br>";
                       }
                    htmlVar+="<tr><td class='gFontFamily gFontSize12' align='center' colspan='3'><b>"+roleName[0]+" Role</b></td></tr><tr><td width='50%' align='center' class='gFontFamily gFontSize12' style='background-color:#b4d9ee;'>Measure Name</td><td>&nbsp;</td>";
                    htmlVar+="<td width='90%' align='center' class='gFontFamily gFontSize12' style='background-color:#b4d9ee;'>ReportName</td></tr>";
                    for(var i=0;i<MsrIds.length;i++){
                        var reportNames1="";

                   htmlVar+="<tr><td ><input type='text' value='"+MsrNames[i]+"' class='gFontFamily gFontSize12' style='background-color:white;width:90%' readonly=''id='"+MsrIds[i]+"' name='msrName'></td><td><input type='hidden' value='"+MsrIds[i]+"' style='background-color:white' id='"+MsrIds[i]+"' name='msrId'></td>";

                     htmlVar+="<td id='singleReportTd"+MsrIds[i]+"'  style='display:"+displayRepo+"'><select id='MsrReport"+MsrIds[i]+"' class='gFontFamily gFontSize12' style='width:90%;' name='MsrReport'>";
                    if(assignRepIds[i]=='0'){
                    htmlVar+="<option class='gFontFamily gFontSize12' selected value='0'>NOT_SELECTED</option>";
                }else{
                    htmlVar+="<option class='gFontFamily gFontSize12'  value='0'>NOT_SELECTED</option>";
                }
                       for(var j=0;j<reportIds.length;j++){
                          if(assignRepIds[i]==reportIds[j])
                           htmlVar+="<option class='gFontFamily gFontSize12' selected value='"+reportIds[j]+"'>"+reportNames[j]+"</option>";
                           else
                            htmlVar+="<option class='gFontFamily gFontSize12' value='"+reportIds[j]+"'>"+reportNames[j]+"</option>";
                       }
                       htmlVar+="</select></td>";
                   $.post(ctxtPath+'/reportViewer.do?reportBy=getMultiSelectReportNames&msrId='+MsrIds[i]+"&reportId="+reportId,
                   function(data){
                   var jsonVar=eval('('+data+')');
                   reportNames1=jsonVar.reportNames;
                   var msrId=jsonVar.msrId;
                   $("#multireport"+msrId).val(reportNames1);
                     });
                       htmlVar+="<td id='multiReportTd"+MsrIds[i]+"' style='display:"+displayRepo1+"'><input type='text' value='' onclick=\"getmultiReportIds('"+ctxtPath+"','"+reportId+"',this.id)\" style='background-color:white;width:90%;' id='multireport"+MsrIds[i]+"' name='multireportNames' ></td>";

                       htmlVar+="</tr>";
                       htmlVar+="<tr><td colspan='3'><input type='hidden' value='0' style='background-color:white' id='multireportId"+MsrIds[i]+"' name='multireportIds'></td></tr>";
                       }
                       htmlVar+="<tr><td>&nbsp;</td></tr><tr><td colspan='3'  align='center'><input type='button' value='Submit' class='navtitle-hover gFontFamily gFontSize12'  style='width:auto' onclick=\"saveDrillAssignReports('"+reportId+"','"+ctxtPath+"')\"></td></tr>";
                       htmlVar+="</table>";
                       parent.$("#reportDrillFrm").html(htmlVar);
                       parent.$("#reportDrillDiv").dialog('open');
                   }

                    });

        }
  function saveDrillAssignReports(reportId,ctxtPath){
    $.post(ctxtPath+'/reportViewer.do?reportBy=saveDrillAssignReports&reportId='+reportId,$("#reportDrillFrm").serialize() ,
             function(data){
                parent.$("#reportDrillDiv").dialog('close');
                window.location.href = window.location.href;
//                var path=ctxtPath+"/reportViewer.do?reportBy=viewReport&REPORTID="+reportId;
//                parent.submiturls1(path);

             });
  }
function twoRowViewGrouping(colName,dataType,tabId,ctxPath){
    var source = "TableDisplay/pbDisplay.jsp?source=Grouping&groupColumn="+colName+"&sortDataType="+dataType+"&tabId="+tabId;
    var dSrc = document.getElementById("iframe1");
    dSrc.src = source;
    parent.isGraphExists();
}
function paramViewFilterEdit(ctxtPath,reportId){
    parent.$("#viewFilterEdit").dialog('open');            
                 var frameObj=parent.document.getElementById("viewFilterEditFrame");
                    var source="ParameterViewFilterEdit.jsp?reportId="+reportId;
                    frameObj.src=source;
             
}
function addGraphInViewer(ctxtPath,reportId){
    ctxtPathrep = ctxtPath;
    if ($.browser.msie == true){
        parent.$("#designerGraphList").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 300,
            width: 600,
            modal: true
        });
    }else{
        parent.$("#designerGraphList").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 300,
            width: 600,
            modal: true
        });
    }
    var htmlVar = "<table>"
    $.post(ctxtPath+'/reportViewer.do?reportBy=designGraphInViewer&reportId='+reportId,
             function(data){
                 htmlVar += data+"</table>";
                   parent.$("#designerGraphList").html(htmlVar);
                   parent.$("#designerGraphList").dialog('open');
             });
}
    function getGraphName1(name,typeValue,currGrpId){
    var graphIds ='';
    var graphCount=0;
    var parentDiv = '';
    parent.$("#designerGraphList").dialog('close');
   $("#graphList").dialog('close');
    graphCount++;
    graphIds = graphIds+","+graphCount;

    graphIds=currGrpId;


    parentDiv = parent.document.getElementById('tabGraphs1');
    parentDiv.style.height = '250'
//    parentDiv.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';

//    $.ajax({
//        url: 'reportTemplateAction.do?templateParam=buildGraphs&gid='+graphIds+'&grptypid='+typeValue+'&graphChange=default&grpIds='+graphIds+'&REPORTID='+document.getElementById("REPORTID").value,
//        success: function(data){
//            if(data != ""){
//            $.ajax({
//        url: 'reportTemplateAction.do?templateParam=graphInReportViewer&gid='+graphIds+'&graphtype='+typeValue+'&graphChange=default&graphid='+graphIds+'&REPORTID='+document.getElementById("REPORTID").value,
//        success: function(data){
//            parentDiv.innerHTML=data;
//            parentDiv.style.height = 'auto'
//}
//    });
//            }
//}
//    });

    $.ajax({
        type: 'GET',
            async: false,
            cache: false,
            timeout: 30000,
        url: ctxtPathrep+'/reportViewer.do?reportBy=graphChanges&REPORTID='+document.getElementById("REPORTID").value+'&gid='+currGrpId+'&grptypid='+typeValue+'&graphChange=AddGraph&grpIds='+graphIds,
        success: function(data){
            refreshGraphsViewer(ctxtPathrep,document.getElementById("REPORTID").value);
            parentDiv.style.height = 'auto'
        }
    });    
    parent.$("#tabGraphs1").show();
    parent.$("#headerGraph").show();
    var previewGraphObj=document.getElementById("tabGraphs1");
    previewGraphObj.style.display='';
    var previewGraphObj1=document.getElementById("headerGraph");
    previewGraphObj1.style.display='';
    alert("Please Press Global Save after the graph is getting displayed")
    parent.dispGraphs();
}

function refreshGraphsViewer(ctxPath,tabId){
    var source = ctxPath+"/TableDisplay/pbGraphDisplayRegion.jsp?tabId="+tabId;
    document.getElementById("iframe5").src= source;
}

var initTestvalue=new Array();
var initCheckViewbyId=new Array();
var initUncheckViewbyId=new Array();
var initCheckdepentReportIds=new Array();
var initCheckdepentReportIds1=new Array();
function initilizeParameters(ctxPath,reportId){
    initTestvalue=new Array();
  initCheckViewbyId=new Array();
  initUncheckViewbyId=new Array();
  initCheckdepentReportIds=new Array();
  initCheckdepentReportIds1=new Array();
    $.post(ctxPath+'/reportViewer.do?reportBy=getViewbysRelatedReport&reportid='+reportId+'&callFrom=initializeReport',
         function(data){
             if(data!='null'){
            var jsonVar=eval('('+data+')')
             var viewbyIdName=jsonVar.ViewByIdNames;
             var viewbyIds=jsonVar.ViewByIds;
             var reports=jsonVar.reports;
             var dependent = jsonVar.assignedIds;
//             alert("dependent"+dependent)
//             alert("viewbyIds"+viewbyIds.length)
             var p=0;
             var html;
             var checkedList=new Array();
//             alert("viewbyIdName.length:"+viewbyIdName.length);
             html="<table align='center'>"
             for(var i=0;i<viewbyIdName.length;i++){
                 var viewArr=new Array();
                 var checked="";
                 var showSlected="none";
                 if(dependent[i]!='')
                     {

                         checked="checked";
                         showSlected="block";
//                         alert("checked"+checked)
//                         alert("showSlected"+showSlected)
                     }
                 html+="<tr>";
                 viewArr=viewbyIdName[i].split("&");
                 html+="<td><input type='text' name='initviewByName' id='initviewByName"+viewArr[1]+"' style='width:100px;margin-right:3px' value='"+viewArr[0]+"' readonly></td>"
                 html+="<td><input type='hidden' name='initviewByID' id='initviewByID"+viewArr[1]+"' style='width:100px;' value='"+viewArr[1]+"'></td>"
                 html+="<td><input type='checkbox' name='initcheckReport' id='initcheckReport"+viewArr[1]+"' "+checked+"  style='margin-right:3px' onclick=\"ViewByrltdReportsForInitialize('"+viewArr[1]+"')\"></td>";
                 checkedList.push(viewArr[1]);
                 html+="<td><select id='initReport"+viewArr[1]+"' name='initreportName' style='width:180px;display:"+showSlected+";'>";
                 for(var j=0;j<viewbyIds.length;j++){

                     if(viewArr[1]==viewbyIds[j])
                         {
                             for(p;p<reports.length;p++){
                             var isSelected="";
                             if(reports[p]!="a12ab"){
                             var reportsIdName=reports[p].split("&&");
                             if(dependent[i]==reportsIdName[1])
                                 {
//                                     alert("dependent[i]"+dependent[i])
//                                     alert("reportsIdName[1]"+reportsIdName[1])
                                     isSelected='Selected';
                                 }
                            html+="<option  "+isSelected+"  value='"+reportsIdName[1]+"'>"+reportsIdName[0]+"</option>";
                             }else{
                                 p++;
                                 break;

                             }
                                                 }
                         }

                 }
                 html+="</select></td></tr>";
             }
            html+="<tr><td>&nbsp</td></tr><br><br>";
            html+="<tr><td align='center' colspan='4'><input type='button' value='save' class='navtitle-hover' onclick=\"saveInitializeReport('"+reportId+"','"+ctxPath+"','"+checkedList+"')\"></td></tr>";
            html+="</table>";
//            alert("checkedList.length"+checkedList.length);
            parent.$("#initializeRepForm").html(html);
        }
         });
          $("#initializeRepDiv").dialog({
                     autoOpen: false,
                     height: 550,
                     width: 450,
                     position: 'justify',
                     modal: true,
                     resizable:true
                });
         $("#initializeRepDiv").dialog('open');
}

function ViewByrltdReportsForInitialize(id){
    if($("#initcheckReport"+id).is(':checked')){
            $("#initReport"+id).show();
             initTestvalue.push("initcheckReport"+id);
      }
      else{
           initCheckViewbyId.pop(id);
           initTestvalue.pop("initcheckReport"+id);
           initCheckdepentReportIds.pop($("#initReport"+id).val())
            $("#initReport"+id).hide();
      }

}
function saveInitializeReport(reportId,ctxPath,checkedList){
             var viewbyIds=checkedList.split(",");
             for(var i=0;i<viewbyIds.length;i++){
                  if($("#initcheckReport"+viewbyIds[i]).is(':checked')){
                      initCheckViewbyId.push(viewbyIds[i]);
                      initCheckdepentReportIds.push($("#initReport"+viewbyIds[i]).val());
                  }
                 if(initCheckViewbyId==""  )
                     {
                           initUncheckViewbyId.push(viewbyIds[i]);
                           initCheckdepentReportIds1.push($("#initReport"+viewbyIds[i]).val());
             }
             }
//             alert('initCheckViewbyId'+initCheckViewbyId);
//             alert('initCheckdepentReportIds'+initCheckdepentReportIds);
//             alert('initUncheckViewbyId'+initUncheckViewbyId);
//             alert('initCheckdepentReportIds1'+initCheckdepentReportIds1);
            $.post(ctxPath+'/reportViewer.do?reportBy=saveInitializeReport&reportId='+reportId+'&CheckViewbyId='+initCheckViewbyId+'&checkdepentReportIds='+initCheckdepentReportIds, $("#initializeRepForm").serialize() ,
             function(data){
                $("#initializeRepDiv").dialog('close');
                var path=ctxPath+"/reportViewer.do?reportBy=viewReport&REPORTID="+reportId+"&action=reset";
                parent.submiturls1(path);

             });
  }
  function selectSingleReport(msrids){
            var msrIds=msrids.split(",");
             for(var i=0;i<msrIds.length;i++){
                // alert(msrIds[i])
                 $("#singleReportTd"+msrIds[i]).show();
                 $("#multiReportTd"+msrIds[i]).hide();

             }

        }
        function selectMultieReport(msrids){
           //  alert(msrIds.toString())
             var msrIds=msrids.split(",");
            for(var i=0;i<msrIds.length;i++){
                 //alert(msrIds[i])
                 $("#singleReportTd"+msrIds[i]).hide();
                 $("#multiReportTd"+msrIds[i]).show();

             }
        }

        // modified by krishan pratap
        function getmultiReportIds(ctxtPath,reportId,id){
           // alert("@@@getmultiReportIds"+id)
             parent.$("#multipleReportMsrDrill").dialog({
      
                        autoOpen: false,
                        height: 500,
                        width: 500,
                        position: 'justify',
                        modal: true
    });
            
            $.post(ctxtPath+'/reportViewer.do?reportBy=getReportsforMsrDrill&reportId='+reportId,
             function(data){
               var jsonVar = eval('('+data+')');
                var ul = document.getElementById("sortable");
                parent.$("#multipleReportMsrDrillFrm").html("");
                var htmlVar="<table width= 90%><tr></tr><tr align='center' colspan='3'><td ><input type=\"button\" name=\"Done\" class=\"navtitle-hover\" value=\"Done\"  onclick=\"savemultiReportIds('"+ctxtPath+"','"+id+"')\"></td></tr></table>";
                 parent.$("#multipleReportMsrDrillFrm").html(jsonVar.htmlStr+htmlVar)
                 isMemberUseInOtherLevel=jsonVar.isMemberUseInOtherLevel;
                $("#myList3").treeview({
                            animated:"slow",
                            persist: "cookie"
                        });

                        $('ul#myList3 li').quicksearch({
                            position: 'before',
                            attached: 'ul#myList3',
                            loaderText: '',
                            delay: 100
                        })

                $(".myDragTabs").draggable({
                            helper:"clone",
                            effect:["", "fade"]
                        });


                $("#dropTabs").droppable({
                            activeClass:"blueBorder",
                            accept:'.myDragTabs',
                            drop: function(ev, ui) {
                                createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                            }
                        }
            );
                grpColArray=jsonVar.memberValues
                $(".sortable").sortable();

                parent.$("#multipleReportMsrDrill").dialog('open');

             });
        }
     function savemultiReportIds(ctxtPath,id){
         parent.$("#multipleReportMsrDrill").dialog('close');
            var ul = document.getElementById("sortable");
            var repIdsArray = new Array();
            var repNamesArray = new Array();
            var paramIdsArray = new Array();
            var colIds;
            if(ul!=undefined || ul!=null){
                colIds=ul.getElementsByTagName("li");
                  if(colIds!=null && colIds.length!=0){
                    for(var i=0;i<colIds.length;i++){
                       var val=colIds[i].id.split("_")[0].split("###");
                       repIdsArray.push(val[0]);
                       repNamesArray.push(val[1]);

                       }
                   }
              }
            //  alert(repIdsArray+"...."+repNamesArray+"...."+id)
              $("#"+id).val(repNamesArray);
              $("#"+id.replace("multireport","multireportId")).val(repIdsArray);

     }
  function saveDrillAssignReports(reportId,ctxtPath){
    $.post(ctxtPath+'/reportViewer.do?reportBy=saveDrillAssignReports&reportId='+reportId,$("#reportDrillFrm").serialize() ,
             function(data){
                parent.$("#reportDrillDiv").dialog('close');
                window.location.href = window.location.href;
//                var path=ctxtPath+"/reportViewer.do?reportBy=viewReport&REPORTID="+reportId;
//                parent.submiturls1(path);

             });
  }


function twoRowViewGrouping(colName,dataType,tabId,ctxPath){
    var source = "TableDisplay/pbDisplay.jsp?source=Grouping&groupColumn="+colName+"&sortDataType="+dataType+"&tabId="+tabId;
    var dSrc = document.getElementById("iframe1");
    dSrc.src = source;
    parent.isGraphExists();
}
function paramViewFilterEdit(ctxtPath,reportId){
    parent.$("#viewFilterEdit").dialog('open');
                 var frameObj=parent.document.getElementById("viewFilterEditFrame");
                    var source="ParameterViewFilterEdit.jsp?reportId="+reportId;
                    frameObj.src=source;

}
function subTotalSort(colName,sortType,dataType,tabId,ctxPath){
//     alert('subTotSort');
//     alert(colName+''+sortType+''+dataType+''+tabId)
     sortDescend = sortType;
    var source = "TableDisplay/pbDisplay.jsp?source=S&sort="+sortDescend+"&sortColumn="+colName+"&sortDataType="+dataType+"&tabId="+tabId+"&sortType=SUBTOTALSORT";
    var dSrc = document.getElementById("iframe1");
    dSrc.src = source;
    parent.isGraphExists();

 }
 var winWidth=0;
 var winHight=0;
 var level=parseInt(0);
 var isreportdrillPopUp=false;
  var drillAcrossElements=new Array();
 function reportDrillPopUp(reportId,ctxPath,reportUrl){
     if( winWidth ==0 && winHight==0){
         winWidth=parseInt(window.innerWidth);
         winHight=parseInt(window.innerHeight);
     }else{
         winWidth=winWidth-100;
         winHight=winHight-80;
         level=level+1;
     }
//     alert(reportUrl);
     var msrNamesUrl="CBOARP";
    var drillElems=new Array();
    for(var i=0;i<drillAcrossElements.length;i++){
        var temp=drillAcrossElements[i].replace("A_","");
        var tempArr=temp.split(":");
        drillElems.push(tempArr[1]);
        if(msrNamesUrl.indexOf(tempArr[0]) == -1){
            msrNamesUrl+=tempArr[0]+"=";
        }

      /*  if(msrNamesUrl.indexOf(tempArr[0])!=-1)
            msrNamesUrl+=","+encodeURIComponent(tempArr[1]);
        else
            msrNamesUrl+=tempArr[0]+"="+encodeURIComponent(tempArr[1]);*/
    }
    msrNamesUrl +=JSON.stringify(drillElems);
    if(drillAcrossElements.length> 0){
        var urlSplit=reportUrl.substr(reportUrl.indexOf("CBOARP"))

        urlSplit=urlSplit.substr(0,urlSplit.indexOf("&"));

        if(reportUrl.indexOf("CBOARP") != -1){
            reportUrl=reportUrl.replace(urlSplit,msrNamesUrl);
        }
    }

     var frameObj=document.getElementById("ReportDrillPopUpiframe");
     $("#ReportDrillPopUpDiv").dialog({
                        autoOpen: false,
                        height: (winHight-80),
                        width: (winWidth-100),
                        position: 'justify',
                        modal: true,
                        resizable:true,
                        close: function(){
                            level=level-1;
                            winWidth=winWidth+100;
                            winHight=winHight+80;
                        }
             });
             $("#ReportDrillPopUpDiv").dialog('open');
              var loader="<center><img id='imgId' src='images/ajax1.gif' align='top'  width='100px' height='100px  style='position:absolute' /></center>";
             $("#ReportDrillPopUpDiv").html(loader);
             var path=reportUrl+'&parentReportId='+reportId
//             alert(path);
             $.post(path,
             function(data){
                 $("#imgId").remove();
                 var jsonValue=eval('('+data+')');
                 var presReportId=jsonValue.ReportId;
                 var repName=jsonValue.RepName;
                 var dialogeTitle=repName+'&nbsp;&nbsp;-&nbsp;&nbsp;'+jsonValue.DrillDim+'&nbsp; > &nbsp;'+jsonValue.DrillMasVal;
                 $("#ReportDrillPopUpDiv").dialog('option','title',dialogeTitle);

                 isreportdrillPopUp=true;
                 var source=ctxPath+'/TableDisplay/pbReportDrillPopUp.jsp?reportId='+presReportId+'&width='+(winWidth-110)+'&height='+(winHight-50)+'&isreportdrillPopUp=true';
                 var iframecontent='<iframe id="ReportDrillPopUpiframe" src="'+source+'" border="0" width="100%" height="100%" scrolling="no"></iframe>'
                 $("#ReportDrillPopUpDiv").html(iframecontent);
             });


 }

 function NbrFormat(id){
 var nbrFormat=$("#NbrFormat_"+id).val();
 //alert(nbrFormat)
}

function applymutliReportMsrDrill(path){
      $("#reportDrillDiv").dialog('close');
      submiturls(path);
  }
 function reportDrill(url){
    var finalurl=decodeURIComponent(url);
    var msrNamesUrl="CBOARP";
    var drillElems=new Array();
    for(var i=0;i<drillAcrossElements.length;i++){
        var temp=drillAcrossElements[i].replace("A_","");
        var tempArr=temp.split(":");
        drillElems.push(tempArr[1]);
        if(msrNamesUrl.indexOf(tempArr[0]) == -1){
            msrNamesUrl+=tempArr[0]+"=";
        }

      /*  if(msrNamesUrl.indexOf(tempArr[0])!=-1)
            msrNamesUrl+=","+encodeURIComponent(tempArr[1]);
        else
            msrNamesUrl+=tempArr[0]+"="+encodeURIComponent(tempArr[1]);*/
    }
    msrNamesUrl +=JSON.stringify(drillElems);
    if(drillAcrossElements.length> 0){
        var urlSplit=finalurl.substr(finalurl.indexOf("CBOARP"))
        urlSplit=urlSplit.substr(0,urlSplit.indexOf("&"));
        if(finalurl.indexOf("CBOARP") != -1){
            //finalurl=finalurl.replace(urlSplit,msrNamesUrl);
            finalurl=finalurl.replace(urlSplit,msrNamesUrl);
        }
    }
    submiturls(finalurl);

}
 function multireportMsrDrill(reportId,ctxPath,path,msrId){
       var msrid=msrId.replace(/\A_/g,'');
    var finalurl=decodeURIComponent(path);


    var msrNamesUrl="CBOARP";
    var drillElems=new Array();
    for(var i=0;i<drillAcrossElements.length;i++){
        var temp=drillAcrossElements[i].replace("A_","");
        var tempArr=temp.split(":");
        drillElems.push(tempArr[1]);
        if(msrNamesUrl.indexOf(tempArr[0]) == -1){
            msrNamesUrl+=tempArr[0]+"=";
        }

      /*  if(msrNamesUrl.indexOf(tempArr[0])!=-1)
            msrNamesUrl+=","+encodeURIComponent(tempArr[1]);
        else
            msrNamesUrl+=tempArr[0]+"="+encodeURIComponent(tempArr[1]);*/
    }
    msrNamesUrl +=JSON.stringify(drillElems);
    if(drillAcrossElements.length> 0){
        var urlSplit=finalurl.substr(finalurl.indexOf("CBOARP"))
        urlSplit=urlSplit.substr(0,urlSplit.indexOf("&"));
        if(finalurl.indexOf("CBOARP") != -1){
            //finalurl=finalurl.replace(urlSplit,msrNamesUrl);
            finalurl=finalurl.replace(urlSplit,msrNamesUrl);
        }
    }

    path=finalurl;

      $.post(ctxPath+'/reportViewer.do?reportBy=getMultiSelectReportNames&path='+encodeURIComponent(path)+"&msrId="+msrid+"&reportId="+reportId,
             function(data){
                    var jsonVar=eval('('+data+')')
                    var reportIds=jsonVar.reportIds;
                    var reportNames=jsonVar.reportNames;
                    var htmlStr='';
                    htmlStr="<table align='center'width='100%'><tr><td>&nbsp;&nbsp;</td></tr></table>";
                    htmlStr+="<div id='multireportMsrDrillId' align='left'>";
                    htmlStr+="<ul align='center' id=\"selectable\">";
                    for(var i=0;i<reportIds.length;i++){
                        var path1=path+"&REPORTID="+reportIds[i];
                        htmlStr+="<li id=\"selectable\"onClick=\"applymutliReportMsrDrill('"+encodeURI(path1)+"')\">"+reportNames[i]+"</li>";
                    }
                    htmlStr+="</ul></div>";
                     $("#reportDrillFrm").html(htmlStr);
                     $("#reportDrillDiv").dialog('open');
             });
   //   alert(ctxPath+"......"+path+"......."+msrId)
  }
//add by sumeet start
function modifyMeasure(ctxPath,reportId)
{
    $.post(ctxPath+'/reportViewer.do?reportBy=modifyMeasure&reportid='+reportId,
         function(data){
             if(data!='null'){
            var jsonVar=eval('('+data+')')
             var measureName=jsonVar.measureName;
             var measureId=jsonVar.measureId;
             var colors=jsonVar.colors;
             var symbols=jsonVar.symbols;
             var alignment=jsonVar.alignment;
             var fontcolor=jsonVar.fontcolor;
             var bgcolor=jsonVar.bgcolor;
             var negative_val=jsonVar.negative_val;
             var no_format=jsonVar.no_format;
             var round=jsonVar.round;
             var Symbols=["","$","Rs","Yen","Euro","%","AED"];
             var Alignment=["","Left","Center","Right"]
             var Negative_value=["","Bracket","Red","Bracket&Red"]
             var No_format=["","K","M","L","Cr"]
             var No_format_D=["Absolute","Thousands(K)","Millions(M)","Lakhs(L)","Crores(C)"]
             var Round=["","1","2","3","4","5"]
             var Round_D=["No Decimal","One Decimal","Two Decimal","Three Decimal","Four Decimal","Five Decimal"]

             var colorIds=jsonVar.colorIds;

             var html;
             html="<table align='center'><thead style=\"background-color:#b4d9ee;font-size:12px;font-family:verdana\">"
//             html+="<th><input type=\"checkbox\" id=\"checkformata\" name=\"checkformatselect\" style=\"width:30px;\" onclick=\"\"></th>"
             html+="<th>ElementName</th>"
             html+="<th>Symbols</th>"
             html+="<th>Alignment</th>"
             html+="<th>Font Color</th>"
             html+="<th>Row BackGround Color</th>"
             html+="<th>Negative value</th>"
             html+="<th>Number Format</th>"
             html+="<th>Round</th></thead>"
             for(var i=0;i<measureName.length;i++){
               html+="<tr>"
//               html+="<td style=\"background-color:#b4d9ee;\"><input type=\"checkbox\" id=\"CheckMeasure_"+measureId[i]+"\" name=\"checkformatselect\" style=\"width:30px;background-color:#b4d9ee;\" onclick=\"\"></td>"
               html+="<td><input type='text' name='measure_name' id='measure_"+measureId[i]+"' align='center' style='width:100px;' value='"+measureName[i]+"' readonly></td>"
               html+="<td valign=\"center\" style=\"width:30%\" id=\"headerSymbol_"+measureId[i]+"\"><select name=\"Symbols\" id=\"Symbols_"+measureId[i]+"\" style=\"width:120px;\">"
               for(var j=0;j<Symbols.length;j++){
                   if(j==0){
                       html+="<option  value=\"\" >---Select---</option>"
                   }else{
                   if(symbols[i]==Symbols[j]){
                       html+="<option  value=\""+Symbols[j]+"\" selected=\"\">"+Symbols[j]+"</option>"
                   }else{
                       html+="<option  value=\""+Symbols[j]+"\" >"+Symbols[j]+"</option>"
                   }
                   }
               }
               html+="</select></td>"
               html+="<td valign=\"center\" style=\"width:30%\" id=\"headerAlignId_"+measureId[i]+"\"><select name=\"Alignment\" id=\"Alignment_"+measureId[i]+"\" style=\"width:120px;\">"
               for(var j=0;j<Alignment.length;j++){
                   if(j==0){
                       html+="<option  value=\"\" >---Select---</option>"
                   }else{
                   if(alignment[i]==Alignment[j]){
                       html+="<option  value=\""+Alignment[j]+"\" selected=\"\">"+Alignment[j]+"</option>"
                   }else{
                       html+="<option  value=\""+Alignment[j]+"\" >"+Alignment[j]+"</option>"
                   }
                   }
               }
               html+="</select></td>"
               html+="<td valign=\"center\" style=\"width:30%\" id=\"headerFontId_"+measureId[i]+"\"><select id=\"Font_"+measureId[i]+"\" name=\"Font\" style=\"width:120px;background-color: white\">"
               for(var j=0;j<colors.length;j++){
                 if(colorIds[j]!="#FFFFFF"){
                 if(fontcolor[i]==colorIds[j]){
                 html+="<option style=\"color:"+colorIds[j]+"\" value=\""+colorIds[j]+"\" selected=\"\">"+colors[j]+"</option>"
                 }else{
                   html+="<option style=\"color:"+colorIds[j]+"\" value=\""+colorIds[j]+"\">"+colors[j]+"</option>"
                 }
                 }else{
                 html+="<option style=\"color:#000000\" value=\""+colorIds[j]+"\">"+colors[j]+"</option>"
                 }
               }
               html+="</select></td>"
               html+="<td valign=\"center\" style=\"width:30%\" id=\"BgId_"+measureId[i]+"\"><select id=\"BG_"+measureId[i]+"\" name=\"BG\" style=\"width:120px;background-color: white\">"
               for(var j=0;j<colors.length;j++){
                 if(colorIds[j]!="#FFFFFF"){
                 if(bgcolor[i]==colorIds[j]){
                 html+="<option style=\"color:"+colorIds[j]+"\" value=\""+colorIds[j]+"\" selected=\"\">"+colors[j]+"</option>"
                 }else{
                   html+="<option style=\"color:"+colorIds[j]+"\" value=\""+colorIds[j]+"\">"+colors[j]+"</option>"
                 }
                 }else{
                 html+="<option style=\"color:#000000\" value=\""+colorIds[j]+"\">"+colors[j]+"</option>"
                 }
               }
               html+="</select></td>"
               html+="<td valign=\"center\" style=\"width:30%\" id=\"NegativevalueId_"+measureId[i]+"\"><select name=\"Negativevalue\" id=\"Negativevalue_"+measureId[i]+"\" style=\"width:120px;\">"
               for(var j=0;j<Negative_value.length;j++){
                   if(j==0){
                       html+="<option  value=\"\" >---Select---</option>"
                   }else{
                   if(negative_val[i]==Negative_value[j]){
                       html+="<option  value=\""+Negative_value[j]+"\" selected=\"\">"+Negative_value[j]+"</option>"
                   }else{
                       html+="<option  value=\""+Negative_value[j]+"\" >"+Negative_value[j]+"</option>"
                   }
                   }
               }
               html+="</select></td>"
               html+="<td valign=\"center\" style=\"width:30%\" Id=\"headerNbrFormatId_"+measureId[i]+"\"><select name=\"NbrFormat\" id=\"NbrFormat_"+measureId[i]+"\" style=\"width:120px\">"
               for(var j=0;j<No_format.length;j++){

                   if(no_format[i]==No_format[j]){
                       html+="<option  value=\""+No_format[j]+"\" selected=\"\">"+No_format_D[j]+"</option>"
                   }else{
                       html+="<option  value=\""+No_format[j]+"\" >"+No_format_D[j]+"</option>"
                   }

               }
               html+="</select></td>"
               html+="<td valign=\"center\" style=\"width:30%\"><select name=\"Round\"id=\"RoundId_"+measureId[i]+"\" style=\"width:120px;\">"
               for(var j=0;j<Round.length;j++){
                   if(round[i]==Round[j]){
                       html+="<option  value=\""+Round[j]+"\" selected=\"\">"+Round_D[j]+"</option>"
                   }else{
                       html+="<option  value=\""+Round[j]+"\" >"+Round_D[j]+"</option>"
                   }
               }
               html+="</select></td>"
               html+="</tr>"
             }
             html+="<tr><td>&nbsp</td></tr><br><br>";
             html+="<tr style=\"width:100%\" align='center'><td align='center' colspan='10' style=\"width:100%\"><input type='button' value='     save     ' align='center' class='navtitle-hover' onclick=\"saveModifyMeasures('"+ctxPath+"','"+reportId+"')\">&nbsp;&nbsp;<input type='button' value='     Cancel     ' class='navtitle-hover' onclick=\"\"></td></tr>";
             html+="</table>";
             parent.$("#modifyMeasureForm").html(html);
             }
         });
         $("#allParametersTab").hide("fast");
         $("#modifyMeasureDiv").dialog('open');

}
function saveModifyMeasures(ctxPath,reportId){
    $.post(ctxPath+'/reportViewer.do?reportBy=saveModifyMeasures&reportId='+reportId,$("#modifyMeasureForm").serialize() ,
             function(data){
                parent.$("#modifyMeasureDiv").dialog('close');
                window.location.href = window.location.href;
//                var path=ctxtPath+"/reportViewer.do?reportBy=viewReport&REPORTID="+reportId;
//                parent.submiturls1(path);

             });
    }
//add by sumeet end

function addGraphInDesigner(ctxtPath,reportId){
    $.post(ctxtPath+'/reportViewer.do?reportBy=designGraphInDesigner&reportId='+reportId,
             function(data){
            var jsonVar=eval('('+data+')')
            var graphIds ='';
            var graphCount=0;
            var parentDiv = '';
            graphCount++;
            graphIds = graphIds+","+graphCount;

            graphIds=jsonVar;
            parent.$("#graphlist").hide();
            parentDiv = parent.document.getElementById('tabGraphs');
            parentDiv.style.height = '250'
            $.ajax({
//                url: 'reportTemplateAction.do?templateParam=buildGraphs&gid='+graphIds+'&grptypid=Bar&graphChange=default&grpIds='+graphIds+'&REPORTID='+document.getElementById("REPORTID").value,
                url: 'reportTemplateAction.do?templateParam=buildGraphs&gid='+graphIds+'&grptypid=Bar&graphChange=default&grpIds='+graphIds+'&REPORTID='+reportId,
                success: function(data){
                    if(data != ""){
                        $.ajax({
//                            url:ctxtPath+"/TableDisplay/pbGraphDisplayRegion.jsp?tabId="+document.getElementById("REPORTID").value,
                            url:ctxtPath+"/TableDisplay/pbGraphDisplayRegion.jsp?tabId="+reportId,
                            success: function(data){
//                                var source = ctxtPath+"/TableDisplay/pbGraphDisplayRegion.jsp?tabId="+document.getElementById("REPORTID").value;
                                var source = ctxtPath+"/TableDisplay/pbGraphDisplayRegion.jsp?tabId="+reportId;
                                parent.document.getElementById("iframe4").src= source;
                                parentDiv.style.height = 'auto'
                            }
             });
                }
}
            });
            parent.$("#tabGraphs").show();
            parent.$("#headerGraph").show();
            var previewGraphObj=document.getElementById("tabGraphs");
            previewGraphObj.style.display='';
            if(document.getElementById("headerGraph")!=null){
                var previewGraphObj1=document.getElementById("headerGraph");
                previewGraphObj1.style.display='';
            }

        })
}
    function getDesignGraph(name,typeValue,currGrpId){
    var graphIds ='';
    var graphCount=0;
    var parentDiv = '';
    graphCount++;
    graphIds = graphIds+","+graphCount;

    graphIds=currGrpId;
parent.$("#graphlist").hide();
    parentDiv = parent.document.getElementById('tabGraphs1');
    parentDiv.style.height = '250'
    $.ajax({
       url: 'reportTemplateAction.do?templateParam=buildGraphs&gid='+graphIds+'&grptypid='+typeValue+'&graphChange=default&grpIds='+graphIds+'&REPORTID='+document.getElementById("REPORTID").value,
        success: function(data){
            if(data != ""){
            $.ajax({
        url:ctxtPathrep+"/TableDisplay/pbGraphDisplayRegion.jsp?tabId="+document.getElementById("REPORTID").value,

        success: function(data){

            var source = ctxtPathrep+"/TableDisplay/pbGraphDisplayRegion.jsp?tabId="+document.getElementById("REPORTID").value;
   parent.document.getElementById("iframe5").src= source;
            parentDiv.style.height = 'auto'
            }
            });
        }
        }
    });
    parent.$("#tabGraphs1").show();
    parent.$("#headerGraph").show();
    var previewGraphObj=document.getElementById("tabGraphs1");
    previewGraphObj.style.display='';
    if(document.getElementById("headerGraph")!=null){
    var previewGraphObj1=document.getElementById("headerGraph");
    previewGraphObj1.style.display='';
    }
    alert("Please Press Global Save after the graph is getting displayed")
    parent.dispGraphs();

}
//added by Nazneen
function isDividebyProgenDateDiff(){
      if(parent.$("#dividebyProgenDateDiff").attr('checked')){
        parent.$("#divideby").val("@PROGENTIME@@ST_DATE - @PROGENTIME@@ED_DATE")
        parent.$("#divideby").attr("disabled", "disabled");
      }
      else {
        parent.$("#divideby").removeAttr("disabled");
        parent.$("#divideby").val("");
      }

  }

function modifycolmMeasure(elementid,displayLabel,reportId,ctxPath){
  var eleid=elementid.toString().replace("A_", "");
 //alert(eleid)
 var html1="";
          var htmlVar="";
     $.post(ctxPath+'/reportViewer.do?reportBy=modifyMeasureAttr&changeattr=true&elementid='+eleid+'&reportid='+reportId,
    function(data){
       // alert(data)
        if(data!='null'){

          var attronchange=eval('('+data+')')
          var symbols=attronchange.Symbol;
          var datatyp=attronchange.datatype;
          var msrType=attronchange.msrType;
          var Aggregation=attronchange.Aggregation;
          var numfrm=attronchange.numfrmt;
           var suffix=attronchange.numfrmt;
//            alert(suffix)
          var Rounding=attronchange.Rounding;
          var defDateFrmt=attronchange.defDateFrmt;



             var connType=attronchange. connType;
             var Symbols=["","$","Rs","Yen","Euro","%","AED"];
             var No_format=["","K","M","L","Cr"]
             var No_format_D=["Absolute","Thousands(K)","Millions(M)","Lakhs(L)","Crores(C)"]
             var Round=["","1","2","3","4","5"]
             var Round_D=["No Decimal","One Decimal","Two Decimal","Three Decimal","Four Decimal","Five Decimal"]
             var Aggr=["SUM","MIN","MAX","AVG","COUNT","COUNTDISTINCT","NONE"]
             if(connType=="oracle")
                var Dtype=["NUMBER","CHAR","VARCHAR","VARCHAR2","DATE","CALCULATED","SUMMARIZED","TIMECALUCULATED"]
             else if (connType=="mysql")
                 var Dtype=["NUMBER","VARCHAR","VARCHAR2","DATE","DATETIME","DECIMAL","CALCULATED","SUMMARIZED","TIMECALUCULATED"]
             else
                 var Dtype=["NUMBER","CHAR","VARCHAR","VARCHAR2","DATE","CALCULATED","SUMMARIZED","TIMECALUCULATED"]

             if(datatyp=='date' || datatyp=='DATE'){
                var dateFormat=["--SELECT--","DD-MM-YY","DD-MMM-YY","MM-DD-YY","MMM-DD-YY","DD-YY-MM","DD-YY-MMM","YY-MMM-DD","YY-MM-DD","DD-MM-YYYY","DD-MMM-YYYY","MM-DD-YYYY","MMM-DD-YYYY","DD-YYYY-MM","DD-YYYY-MMM","YYYY-MMM-DD","YYYY-MM-DD"]
                var dateFormatValues=["","dd-MM-yy","dd-MMM-yy","MM-dd-yy","MMM-dd-yy","dd-yy-MM","dd-yy-MMM","yy-MMM-dd","yy-MM-dd","dd-MM-yyyy","dd-MMM-yyyy","MM-dd-yyyy","MMM-dd-yyyy","dd-yyyy-MM","dd-yyyy-MMM","yyyy-MMM-dd","yyyy-MM-dd"];

                htmlVar+="<td>Date Format</td><td><select id=\"dateFrmt\" name=\"dateFrmt\" style=\"width: 130px\">"
                      for(var j=0;j<dateFormat.length;j++){
                          if(dateFormat[j]==defDateFrmt)
                                htmlVar+="<option selected value=\""+dateFormatValues[j]+"\" >"+dateFormat[j]+"</option>"
                          else
                               htmlVar+="<option  value=\""+dateFormatValues[j]+"\" >"+dateFormat[j]+"</option>"
                      }
               htmlVar+="</select></td>";
           }
        if(datatyp=='summarized' || datatyp=='SUMMARIZED' || datatyp=='calculated' || datatyp=='CALCULATED' || datatyp=='timecalculated' || datatyp=='TIMECALUCULATED'){
            parent.$("#Dtype").attr("disabled", true);
        }
        if(Aggregation=='SUM' || Aggregation=='AVG' || Aggregation=='MIN' || Aggregation=='MAX' || Aggregation=='COUNT' || Aggregation=='COUNTDISTINCT' || Aggregation=='NONE'){
            html1+="<td> Default Aggregation</td><td><select id=\"Aggr\" name=\"Aggr\" style=\"width: 130px\">"
              for(var j=0;j<Aggr.length;j++){
                       html1+="<option  value=\""+Aggr[j]+"\" >"+Aggr[j]+"</option>"
                   }
                html1+="</select></td>"
        }
        else {
            html1+="<td> Default Aggregation</td><td><select id=\"Aggr\" name=\"Aggr\" style=\"width: 130px\">"
                html1+="<option  value=\""+Aggregation+"\" >"+Aggregation+"</option>"
                html1+="</select></td>"
                parent.$("#AggrVal").attr("disabled", true);
        }


             var MsrType=["NONE","STANDARD","NON-STANDARD"]
             var html="";
             html="<table class='ui-dialog-content ui-widget-content'>"
             html+="<tr><td>measurename</td><td><select id=\"msrname\" style=\"width: 130px\" name=\"msrname\" onchange=\"getChangedAttr('"+ctxPath+"','"+reportId+"')\">"
//            html= "<td><select id=\"msrname\" name=\"msrname\">"

                       html+="<option  value=\""+eleid+"\"  selected=\"\">"+displayLabel+"</option>"


             html+="</select></td></tr>"
             html+="<tr><td>Rename</td><td><input type=\"text\" style=\"width: 130px\" id=\"rename\" name=\"rename\"></td><tr>"

             html+="<tr><td> Datatype</td><td><select id=\"Dtype\" name=\"Dtype\" style=\"width: 130px\">"
              for(var j=0;j<Dtype.length;j++){
                   if(Dtype[j]==datatyp){
                       html+="<option  value=\""+datatyp+"\" selected=\"\">"+datatyp+"</option>"
                   }else{
                       html+="<option  value=\""+Dtype[j]+"\" >"+Dtype[j]+"</option>"
                   }
                   }
                html+="</select></td></tr>"
                html+="<tr id=\"AggrVal\"><td> Default Aggregation</td><td><select id=\"Aggr\" name=\"Aggr\" style=\"width: 130px\">"
              for(var j=0;j<Aggr.length;j++){
                   if(Dtype[j]==Aggregation){
                       html+="<option  value=\""+Aggregation+"\" selected=\"\">"+Aggregation+"</option>"
                   }else{
                       html+="<option  value=\""+Aggr[j]+"\" >"+Aggr[j]+"</option>"
                   }
                   }
                html+="</select></td></tr>"

              html+="<tr><td>Rounding</td><td><select id=\"Round\" name=\"Round\" style=\"width: 130px\">"
              for(var j=0;j<Round_D.length;j++){
                   if(Round_D[j]==Rounding){
                       html+="<option  value=\""+Round[j]+"\" selected=\"\">"+Rounding+"</option>"
                   }else{
                       html+="<option  value=\""+Round[j]+"\" >"+Round_D[j]+"</option>"
                   }
                   }
                html+="</select></td></tr>"
                 html+="<tr><td>Number format</td><td><select id=\"Nfrmt\" name=\"Nfrmt\" style=\"width: 130px\">"
              for(var j=0;j<No_format_D.length;j++){
                   if(No_format_D[j]==numfrm){
                       html+="<option  value=\""+No_format[j]+"\" selected=\"\">"+numfrm+"</option>"
                   }else{
                       html+="<option  value=\""+No_format[j]+"\" >"+No_format_D[j]+"</option>"
                   }
                   }
                html+="</select></td></tr>"
                 html+="<tr><td>Preffix</td><td><select id=\"preffix\" name=\"preffix\" style=\"width: 130px\">"
              for(var j=0;j<Symbols.length;j++){
                   if(Symbols[j]==symbols){
                       html+="<option  value=\""+symbols+"\" selected=\"\">"+symbols+"</option>"
                   }else{
                       html+="<option  value=\""+Symbols[j]+"\" >"+Symbols[j]+"</option>"
                   }
                   }
                html+="</select></td></tr>"
                 html+="<tr><td>Suffix</td><td><select id=\"suffix\" name=\"suffix\" style=\"width: 130px\">"
              for(var j=0;j<No_format.length;j++){
                   if(No_format[j]==suffix){
                       html+="<option  value=\""+suffix+"\" selected=\"\">"+suffix+"</option>"
                   }else{
                       html+="<option  value=\""+No_format[j]+"\" >"+No_format[j]+"</option>"
                   }
                   }
                html+="</select></td></tr>"
                html+="<tr><td>Measure type</td><td><select id=\"msrtyp\" name=\"msrtyp\" style=\"width: 130px\">"
              for(var j=0;j<MsrType.length;j++){
                   if(MsrType[j]==msrType){
                       html+="<option  value=\""+msrType+"\" selected=\"\">"+msrType+"</option>"
                   }else{
                       html+="<option  value=\""+MsrType[j]+"\" >"+MsrType[j]+"</option>"
                   }
                   }
                html+="</select></td></tr>"
                html+="<tr id=\"dateFrmtTr\"></tr>"
                html+="<tr><td><span><font color=\"red\">*</font><span>Modify other reports also</td><td><input type=\"checkbox\" id=\"oldrep\" name=\"oldrep\" value=\"N\" onclick=\"getcheckboxvalue()\"></td></tr>"
                html+="<tr style=\"width:100%\" align='center'><td align='center' colspan='10' style=\"width:100%\"><input class=\"navtitle-hover\" type=\"button\" value=\"Done\" onclick=\"saveModifyMeasuresAttr('"+ctxPath+"','"+reportId+"')\"></td></tr>"
                 html+="</table>";


             parent.$("#modifyMeasureAttrForm").html(html);
             parent.$("#dateFrmtTr").html(htmlVar);
            parent.$("#AggrVal").html(html1);
           parent.$("#rename").val(displayLabel);
          parent.$("#Dtype").val(datatyp);
           parent.$("#Aggr").val(Aggregation);
           parent.$("#Round").val(Rounding);
           parent.$("#Nfrmt").val(numfrm);
           parent.$("#preffix").val(symbols);
           parent.$("#msrtyp").val(msrType);
            parent.$("#suffix").val(suffix);
             }
         });


         $("#allParametersTab").hide("fast");
         $("#modifyMeasureAttrDiv").dialog('open');


}

  function getChangedAttr(ctxPath,reportId)
{  
    var Aggr=["SUM","MIN","MAX","AVG","COUNT","COUNTDISTINCT","NONE"] 
    var sel = document.getElementById('msrname');
    var sv = sel.options[sel.selectedIndex].value;
    var text=sel.options[sel.selectedIndex].text;
    var htmlVar ="";
    var html1 = "";
    parent.$("#Dtype").removeAttr('disabled');
    parent.$("#Aggr").removeAttr('disabled');
    parent.$("#rename").val(text);
//    alert(sv)
    $.post(ctxPath+'/reportViewer.do?reportBy=modifyMeasureAttr&changeattr=true&elementid='+sv+'&reportid='+reportId,
    function(data){
//        alert(data)
        if(data!='null'){
          
          var attronchange=eval('('+data+')')
          var symbols=attronchange.Symbol;
          var datatyp=attronchange.datatype;
          var msrType=attronchange.msrType;
          var Aggregation=attronchange.Aggregation;
          var numfrm=attronchange.numfrmt;
           var suffix=attronchange.numfrmt;
//            alert(suffix)
          var Rounding=attronchange.Rounding;
          var defDateFrmt=attronchange.defDateFrmt;   
           

            if(datatyp=='date' || datatyp=='DATE'){
                var dateFormat=["--SELECT--","DD-MM-YY","DD-MMM-YY","MM-DD-YY","MMM-DD-YY","DD-YY-MM","DD-YY-MMM","YY-MMM-DD","YY-MM-DD","DD-MM-YYYY","DD-MMM-YYYY","MM-DD-YYYY","MMM-DD-YYYY","DD-YYYY-MM","DD-YYYY-MMM","YYYY-MMM-DD","YYYY-MM-DD"] 
                var dateFormatValues=["","dd-MM-yy","dd-MMM-yy","MM-dd-yy","MMM-dd-yy","dd-yy-MM","dd-yy-MMM","yy-MMM-dd","yy-MM-dd","dd-MM-yyyy","dd-MMM-yyyy","MM-dd-yyyy","MMM-dd-yyyy","dd-yyyy-MM","dd-yyyy-MMM","yyyy-MMM-dd","yyyy-MM-dd"];
                  
                htmlVar+="<td>Date Format</td><td><select id=\"dateFrmt\" name=\"dateFrmt\" style=\"width: 130px\">"
                      for(var j=0;j<dateFormat.length;j++){
                          if(dateFormat[j]==defDateFrmt)
                                htmlVar+="<option selected value=\""+dateFormatValues[j]+"\" >"+dateFormat[j]+"</option>"
                          else 
                               htmlVar+="<option  value=\""+dateFormatValues[j]+"\" >"+dateFormat[j]+"</option>"
                      }
               htmlVar+="</select></td>";               
           }
        if(datatyp=='summarized' || datatyp=='SUMMARIZED' || datatyp=='calculated' || datatyp=='CALCULATED' || datatyp=='timecalculated' || datatyp=='TIMECALUCULATED'){
            parent.$("#Dtype").val(datatyp);
            parent.$("#Dtype").attr("disabled", true);
        }    
        if(Aggregation=='SUM' || Aggregation=='AVG' || Aggregation=='MIN' || Aggregation=='MAX' || Aggregation=='COUNT' || Aggregation=='COUNTDISTINCT' || Aggregation=='NONE'){  
            html1+="<td> Default Aggregation</td><td><select id=\"Aggr\" name=\"Aggr\" style=\"width: 130px\">"
              for(var j=0;j<Aggr.length;j++){
                       html1+="<option  value=\""+Aggr[j]+"\" >"+Aggr[j]+"</option>"
                   }
                html1+="</select></td>"            
        }
        else {
            html1+="<td> Default Aggregation</td><td><select id=\"Aggr\" name=\"Aggr\" style=\"width: 130px\">"
                html1+="<option  value=\""+Aggregation+"\" >"+Aggregation+"</option>"
                html1+="</select></td>"
                 parent.$("#Aggr").val(Aggregation);
                parent.$("#AggrVal").attr("disabled", true);
        }
        parent.$("#dateFrmtTr").html(htmlVar);        
            parent.$("#AggrVal").html(html1);       
            
           parent.$("#Dtype").val(datatyp);
           parent.$("#Aggr").val(Aggregation);
           parent.$("#Round").val(Rounding);
           parent.$("#Nfrmt").val(numfrm);
           parent.$("#preffix").val(symbols);
           parent.$("#msrtyp").val(msrType);
            parent.$("#suffix").val(suffix);
          
                  
        }
    });
    
}
function getcheckboxvalue(){
    var showGridLinesObj=document.getElementById("oldrep");
                if(showGridLinesObj.checked){
                    var r=confirm("Are you sure that you want to modify other reports also");
                   if (r==true){
                    showGridLinesObj.value="Y"
//                    alert("Y")
                }else{
                          
                                 document.getElementById("oldrep").checked = false;
                               }
                }else{
                    showGridLinesObj.value="N";
//                    alert("N")
                }
                document.getElementById("oldrep").value=showGridLinesObj.value;
            }


function modifyMeasureAttr(ctxPath,reportId)
{
    alert("modify measure called");
    return;
    $.post(ctxPath+'/reportViewer.do?reportBy=modifyMeasureAttr&reportid='+reportId,
         function(data){
//             alert(data)
             if(data!='null'){
            var attrchange=eval('('+data+')')
//            alert(attrchange)
             var measureName=attrchange.measureName;
             var length=(measureName.length-1);
             var measureId=attrchange.measureId;
             var symbols=attrchange.symbols;
             var no_format=attrchange.no_format;
             var round=attrchange.round;
             var aggr=attrchange. Aggregation;
             var datatype=attrchange.col_type;
             var msrtyp=attrchange. measureType;
             var connType=attrchange. connType;
             var Symbols=["","$","Rs","Yen","Euro","%","AED"];
             var No_format=["","K","M","L","Cr"]
             var No_format_D=["Absolute","Thousands(K)","Millions(M)","Lakhs(L)","Crores(C)"]
             var Round=["","1","2","3","4","5"]
             var Round_D=["No Decimal","One Decimal","Two Decimal","Three Decimal","Four Decimal","Five Decimal"]
             var Aggr=["SUM","MIN","MAX","AVG","COUNT","COUNTDISTINCT","NONE"] 
             if(connType=="oracle")
                var Dtype=["NUMBER","CHAR","VARCHAR","VARCHAR2","DATE","CALCULATED","SUMMARIZED","TIMECALUCULATED"]
             else if (connType=="mysql")
                 var Dtype=["NUMBER","VARCHAR","VARCHAR2","DATE","DATETIME","DECIMAL","CALCULATED","SUMMARIZED","TIMECALUCULATED"]
             else 
                 var Dtype=["NUMBER","CHAR","VARCHAR","VARCHAR2","DATE","CALCULATED","SUMMARIZED","TIMECALUCULATED"]
             
             var MsrType=["NONE","STANDARD","NON-STANDARD"]
             var html="";
             html="<table class='ui-dialog-content ui-widget-content'>"
             html+="<tr><td>measurename</td><td><select id=\"msrname\" style=\"width: 130px\" name=\"msrname\" onchange=\"getChangedAttr('"+ctxPath+"','"+reportId+"')\">"
//            html= "<td><select id=\"msrname\" name=\"msrname\">"
             html+="<option  value=\"\" >---Select---</option>"
             for(var j=0;j<measureName.length;j++){
               
                   
                       html+="<option  value=\""+measureId[j]+"\" >"+measureName[j]+"</option>"
               
              }
             html+="</select></td></tr>"
             html+="<tr><td>Rename</td><td><input type=\"text\" style=\"width: 130px\" id=\"rename\" name=\"rename\"></td><tr>" 
             html+="<tr><td> Datatype</td><td><select id=\"Dtype\" name=\"Dtype\" style=\"width: 130px\">"
              for(var j=0;j<Dtype.length;j++){
//                   if(datatype[length]==Dtype[j]){
//                       html+="<option  value=\""+Dtype[j]+"\" selected=\"\">"+Dtype[j]+"</option>"
//                   }else{
                       html+="<option  value=\""+Dtype[j]+"\" >"+Dtype[j]+"</option>"
                  // }
                   }
                html+="</select></td></tr>"
                html+="<tr id=\"AggrVal\"><td> Default Aggregation</td><td><select id=\"Aggr\" name=\"Aggr\" style=\"width: 130px\">"
              for(var j=0;j<Aggr.length;j++){
//                   if(aggr[length]==Dtype[j]){
//                       html+="<option  value=\""+Aggr[j]+"\" selected=\"\">"+Aggr[j]+"</option>"
                   //}else{
                       html+="<option  value=\""+Aggr[j]+"\" >"+Aggr[j]+"</option>"
                  // }
                   }
                html+="</select></td></tr>"
            
              html+="<tr><td>Rounding</td><td><select id=\"Round\" name=\"Round\" style=\"width: 130px\">"
              for(var j=0;j<Round.length;j++){
//                   if(round[length]==Round[j]){
//                       html+="<option  value=\""+Round[j]+"\" selected=\"\">"+Round_D[j]+"</option>"
//                   }else{
                       html+="<option  value=\""+Round[j]+"\" >"+Round_D[j]+"</option>"
                 //  }
                   }
                html+="</select></td></tr>"
                 html+="<tr><td>Number format</td><td><select id=\"Nfrmt\" name=\"Nfrmt\" style=\"width: 130px\">"
              for(var j=0;j<No_format.length;j++){
//                   if(no_format[length]==No_format[j]){
//                       html+="<option  value=\""+No_format[j]+"\" selected=\"\">"+No_format_D[j]+"</option>"
//                   }else{
                       html+="<option  value=\""+No_format[j]+"\" >"+No_format_D[j]+"</option>"
                  // }
                   }
                html+="</select></td></tr>"
                 html+="<tr><td>Preffix</td><td><select id=\"preffix\" name=\"preffix\" style=\"width: 130px\">"
              for(var j=0;j<Symbols.length;j++){
//                   if(symbols[length]==No_format[j]){
//                       html+="<option  value=\""+Symbols[j]+"\" selected=\"\">"+Symbols[j]+"</option>"
//                   }else{
                       html+="<option  value=\""+Symbols[j]+"\" >"+Symbols[j]+"</option>"
                  // }
                   }
                html+="</select></td></tr>"
                 html+="<tr><td>Suffix</td><td><select id=\"suffix\" name=\"suffix\" style=\"width: 130px\">"
              for(var j=0;j<No_format.length;j++){
//                   if(no_format[length]==No_format[j]){
//                       html+="<option  value=\""+No_format[j]+"\" selected=\"\">"+No_format[j]+"</option>"
//                   }else{
                       html+="<option  value=\""+No_format[j]+"\" >"+No_format[j]+"</option>"
                  // }
                   }
                html+="</select></td></tr>"
                html+="<tr><td>Measure type</td><td><select id=\"msrtyp\" name=\"msrtyp\" style=\"width: 130px\">"
              for(var j=0;j<MsrType.length;j++){
//                   if(msrtyp[length]==No_format[j]){
//                       html+="<option  value=\""+MsrType[j]+"\" selected=\"\">"+MsrType[j]+"</option>"
//                   }else{
                       html+="<option  value=\""+MsrType[j]+"\" >"+MsrType[j]+"</option>"
                  // }
                   }
                html+="</select></td></tr>"
                html+="<tr id=\"dateFrmtTr\"></tr>"
                html+="<tr><td><span><font color=\"red\">*</font><span>Modify other reports also</td><td><input type=\"checkbox\" id=\"oldrep\" name=\"oldrep\" value=\"N\" onclick=\"getcheckboxvalue()\"></td></tr>"
                html+="<tr style=\"width:100%\" align='center'><td align='center' colspan='10' style=\"width:100%\"><input class=\"navtitle-hover\" type=\"button\" value=\"Done\" onclick=\"saveModifyMeasuresAttr('"+ctxPath+"','"+reportId+"')\"></td></tr>"
                 html+="</table>";
             
             parent.$("#modifyMeasureAttrForm").html(html);
             }
         });
         $("#allParametersTab").hide("fast");
         $("#modifyMeasureAttrDiv").dialog('open');

}
function saveModifyMeasuresAttr(ctxPath,reportId){
    
    parent.$("#Dtype").removeAttr('disabled');
    parent.$("#Aggr").removeAttr('disabled');
      //parent.$("#modifyMeasureDiv").dialog('close');//commented for modify div not close
            parent.$("#modifyMeasureAttrDiv").dialog('close');
     $.post(ctxPath+'/reportViewer.do?reportBy=saveModifyMeasuresAttr&reportId='+reportId,$("#modifyMeasureAttrForm").serialize() ,
             function(data){
            window.location.href=window.location.href;
});
}
function openDimDialog(ctxPath,elementId,reportId){
      //alert(eId);
      var eId=elementId.replace(/\A_/g,'');
       $.post(ctxPath+'/reportViewer.do?reportBy=reportDrillAssignment&reportId='+reportId,
                 function(data){
                if(data!='null'){
                    var jsonVar=eval('('+data+')')
                    var roleName=jsonVar.roleName;                    
                    var reportIds=jsonVar.reportIds;
                    var reportNames=jsonVar.reportNames;
                    var htmlVar="<table><tbody>";
                     htmlVar+="<tr><td>Select Report</td><td><select id='dimReportId' style='width:90%;' name='dimReportId'>";
                     for(var j=0;j<reportIds.length;j++){                         
                        htmlVar+="<option value='"+reportIds[j]+"'>"+reportNames[j]+"</option>";
                       }
                       htmlVar+="</select></td></tr>";
                       htmlVar+="<tr><td>&nbsp;</td></tr><tr><td colspan='2'  align='center'><input type='button' value='submit' class='navtitle-hover'  style='width:auto' onclick=\"saveDimDrillReport('"+eId+"','"+reportId+"','"+ctxPath+"')\"></td></tr>";
                       htmlVar+="</table>";
                       parent.$("#DimenssionDrillFrm").html(htmlVar);
                       parent.$("#DimenssionDrillDiv").dialog('open');
                }
            });
  }

function saveDimDrillReport(eId,reportId,ctxPath){
    //alert("dimrepId..."+$("#dimReportId").val()+"eId..."+eId+"currrepId..."+reportId)
    var drillrep = $("#dimReportId").val();
    parent.$("#DimenssionDrillDiv").dialog('close');
    var path = ctxPath+'/reportViewer.do?reportBy=viewReport&REPORTID='+drillrep+'&action=open'+'&fromlockedrep='+reportId+'&lockedElem='+eId;
    window.location = path;
    //parent.viewReportDrill(path);
//    $.post(ctxPath+'/reportViewer.do?reportBy=viewReport&REPORTID='+reportId+'&action=open'+'&lockedrepId='+reportId+'&lockedelemId='+eId,
//                 function(data){
//                     
//                 });

}
//added by Nazneen
function editBuckets(ctxPath,roleId){
   var tempBuck = 'yes';
    $.post(ctxPath+'/reportViewer.do?reportBy=getRoleIds&roleId='+roleId,
    function(data){
        if(data!='null'){          
          var attronchange=eval('('+data+')')
          var grpId=attronchange.grpId;     
          var source = "editBucketDetails.jsp?busGroupID="+grpId+"&tempBuck="+tempBuck;
          var frameObj=parent.document.getElementById("editBucketframe");
          frameObj.src=source;
          parent.$("#editBucketDiv").dialog('open');    
    }
    });

}
//added by Nazneen for dependent Dimensions
function getDependentDims(){
  if($("#dependentDims").is(':checked')){
    parent.$('#dependentDimsDiv').show();
  }
  else {
      parent.$('#dependentDimsDiv').hide();
  }
}
function getPriority(){
    var dimVal = parent.$("#dependentDimsName").val()
    if(dimVal=='1'){
         parent.$("#priority").val("1")
         $("#priority").attr("disabled", "disabled");
    }
    else if(dimVal=='NULL'){
         parent.$("#priority").val("NULL")
         $("#priority").attr("disabled", "disabled");
    }
    else {
        $("#priority").removeAttr("disabled");
    }
}
function hideMeasures(ctxPath,reportId){   
   parent.$("#hideMeasureDiv").dialog('open');
  $.post(ctxPath+'/reportViewer.do?reportBy=hideMeasuresinTable&reportid='+reportId,
         function(data){
           var jsonVar = eval('('+data+')');
           var ul = document.getElementById("sortable");
           parent.$("#hideMsrFrm").html("");
           var htmlVar="<table><tr><td align=\"center\"><input type=\"button\" name=\"Done\" class=\"navtitle-hover\" value=\"Done\"  onclick=\"savehideMsrs('"+ctxPath+"','"+reportId+"')\"></td></tr></table>";
                 parent.$("#hideMsrFrm").html(jsonVar.htmlStr+htmlVar)
                 isMemberUseInOtherLevel=jsonVar.isMemberUseInOtherLevel;
                  $("#myList3").treeview({
                            animated:"slow",
                            persist: "cookie"
                        });
                        
                        $('ul#myList3 li').quicksearch({
                            position: 'before',
                            attached: 'ul#myList3',
                            loaderText: '',
                            delay: 100
                        });
                        
                $(".myDragTabs").draggable({
                            helper:"clone",
                            effect:["", "fade"]
                        });


                $("#dropTabs").droppable({
                            activeClass:"blueBorder",
                            accept:'.myDragTabs',
                            drop: function(ev, ui) {
                                createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                            }
                        }
            );
                grpColArray=jsonVar.memberValues
                $(".sortable").sortable();

                
    });

}
function savehideMsrs(ctxPath,reportId){    
            var ul = document.getElementById("sortable");
            var msrIdsArray = new Array();
            var colIds;
            if(ul!=undefined || ul!=null){
                colIds=ul.getElementsByTagName("li");
                  if(colIds!=null && colIds.length!=0){
                    for(var i=0;i<colIds.length;i++){
                       var val=colIds[i].id.split("_")[0];
                       msrIdsArray.push(val);
                       }
                   }
              }
//              alert(msrIdsArray.toString())
           $.post(ctxPath+'/reportViewer.do?reportBy=saveHideTableMeasures&reportId='+reportId+'&hideMsrs='+msrIdsArray,
             function(data){
               parent.$("#hideMeasureDiv").dialog('close');
                 var source = ctxPath+"/TableDisplay/pbDisplay.jsp?tabId="+reportId;
                var dSrc = document.getElementById("iframe1");
                dSrc.src = source;
             });
              
}
function reportSelfDrill(reportId,CntxtPath,drillurl,viewbyid){
      //alert("drillurl"+drillurl)
      $("#selfDrill").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 200,
                        position: 'justify',
                        modal: true
             });
    if(drillAcrossElements.length> 0){         
        var msrNamesUrl="&CBOARP";
        var drillElems=new Array();
        for(var i=0;i<drillAcrossElements.length;i++){
            var temp=drillAcrossElements[i].replace("A_","");
            var tempArr=temp.split(":");
            drillElems.push(tempArr[1]);
            if(msrNamesUrl.indexOf(tempArr[0]) == -1){
                msrNamesUrl+=tempArr[0]+"=";
            }
        }
        msrNamesUrl +=JSON.stringify(drillElems);
        drillurl=msrNamesUrl;
    }
//    alert("CntxtPath"+drillurl)
    $.post(CntxtPath+'/reportViewer.do?reportBy=getViewbysfromReport&drillrepId='+reportId+'&CntxtPath='+CntxtPath+'&drillurl='+encodeURIComponent(drillurl)+'&cboviewbyid='+viewbyid,
                   function(data){
                        $("#selfDrill").html(data);   

                  });
             $("#selfDrill").dialog('open');

             
}
function hideViewbys(ctxPath,reportId){
    parent.$("#hideViewByDiv").dialog('open');
    parent.document.getElementById("hideViewbyFrame").src=ctxPath+"/pbHideViewbyCols.jsp?loadDialogs=true&reportid="+reportId;
}
//start of code by Nazneen on Feb14 for Dimension Segment(Grouping)
function createDimensionSegment(elementId,reportId,viewbyId,viewbyName,ctxPath){
    var isDimSeg = "true";
    parent.$("#dispgrpAnalysis").dialog('open');
    var frameObj=document.getElementById("dispgrpAnalysisFrame");
    var source =ctxPath+'/TableDisplay/createParentParameter.jsp?reportId='+reportId+'&viewbyId='+viewbyId+'&elementId='+elementId+'&isDimSeg='+isDimSeg;
    frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
    frameObj.src=source;
}
//end of code by Nazneen on Feb14 for Dimension Segment(Grouping)

//Start of code By Govardhan.P For SubTotal Filtering
function SubTotalSearch(colName,disColName,REPORTID,ctxPath){
    $("#SubTotalSrch").dialog({
        bgiframe: true,
        autoOpen: false,
        height: 150,
        width: 330,
        modal: true,
        title:'SubTotal Search'
    });
    $.ajax({
        url:ctxPath+'/reportTable.do?reportBy=getFilterConditionIfAny&srchCol='+colName+'&reportId='+REPORTID,
        success:function(data)
        {
             if(data=="not applied" || data==""){
                 document.getElementById("SubTtlSrch").value="";
             }else
            {
                var html="";
                var dataJson = eval('('+data+')');
                var Searchcondition=(dataJson.Searchcondition);
                var SearchValue=(dataJson.SearchValue);
               document.getElementById("SubTtlSrch").value=SearchValue;
               document.getElementById("SubTotalSrchOption").value=Searchcondition;

            }
        }
    });
    document.getElementById("colName").value=colName;
    document.getElementById("ctxPath").value=ctxPath;
    $("#SubTotalSrch").dialog('open');
}
function subTtlSerach(repId){

     $("#SubTotalSrch").dialog('close');
      var ctxPath=document.getElementById("ctxPath").value;
      var colName= document.getElementById("colName").value;
      var srchValue=document.getElementById("SubTtlSrch").value;
      var e = document.getElementById("SubTotalSrchOption");
     var srchCondition = e.options[e.selectedIndex].value;
      doSubTotalSearch(colName,srchCondition,srchValue,repId,ctxPath);

}

function clearfilter(repid){
      $("#SubTotalSrch").dialog('close');
      var ctxPath=document.getElementById("ctxPath").value;
      var colName= document.getElementById("colName").value;
      clearSubTtlFilter(colName,repid,ctxPath);

}
function doSubTotalSearch(repId){
     $("#SubTotalSrch").dialog('close');
      var ctxPath=document.getElementById("ctxPath").value;
      var colName= document.getElementById("colName").value;
      var srchValue=document.getElementById("SubTtlSrch").value;
      var e = document.getElementById("SubTotalSrchOption");
     var srchCondition = e.options[e.selectedIndex].value;
      doSubTotalSearch(colName,srchCondition,srchValue,repId,ctxPath);
}
function doSubTotalSearch(colName,srchCondition,srchValue,repId,ctxPath){
 $.ajax({
        url:ctxPath+'/reportTable.do?reportBy=searchDataSetonSubToTal&search='+srchCondition+'&srchValue='+srchValue+'&srchCol='+colName+'&reportId='+repId,
        success:function(data)
        {
            if ( data == 'invalid')
                alert("Invalid Data Format");
            else
            {
                var source ="TableDisplay/pbDisplay.jsp?tabId="+repId;
                var dSrc = document.getElementById("iframe1");
                dSrc.src = source;
            }
        }
    });

}
function clearSubTtlFilter(colName,repId,ctxPath){

 $.ajax({
        url:ctxPath+'/reportTable.do?reportBy=ClearSearchFilterOnSubTotal&srchCol='+colName+'&reportId='+repId,
        success:function(data)
        {
            if ( data == 'invalid')
                alert("Invalid Data Format");
            else
            {
                var source ="TableDisplay/pbDisplay.jsp?tabId="+repId;
                var dSrc = document.getElementById("iframe1");
                dSrc.src = source;
            }
        }
    });

}//end of Code By Govardhan.P
//added by sruthi for multicalendar
var selectedcheck;
function MultiCalendar(ctxpath,reportid,elements,tableid,factdetails){
 
    parent.$("#ReportMultiCalendar").dialog('close');
    var factsNames=factdetails.replace("[","").replace("","").replace("]","");
      var parameters1=factsNames.split(",");
       var parameters=[];
       var checkedfacts;
       var daydenom;
      var countval=0
      var selectedfact=[];
      var factvaluearr=[];
      var radioarr=[];
      for(var p=0;p<parameters1.length;p++){
           parameters[countval] = parameters1[p].trim();
              countval++;
      }
      for(var ft=0;ft<parameters.length;ft++){
            checkedfacts=$("#factsdetails_"+parameters[ft]).is(':checked') ? 1 : 0;
            if(checkedfacts==1)
            selectedcheck=checkedfacts;
         if(checkedfacts==1){
             var factvalue=document.getElementById('factsdetails_'+parameters[ft]).value
             factvaluearr.push(factvalue);
              var factvaluedata=","+document.getElementById('factsdatatime_'+parameters[ft]).value
               selectedfact.push(factvaluedata);
              var radiodatano=$("#N_"+parameters[ft]).is(':checked') ? 1 : 0;
              //alert("radiodatano...."+radiodatano)
               var radiodatayes=$("#Y_"+parameters[ft]).is(':checked') ? 1 : 0;
              if(radiodatano==1){
                 var nodata= ","+document.getElementById('N_'+parameters[ft]).value;
                // alert("nodata....."+nodata)
                 radioarr.push(nodata);
              }
             // alert("radiodatayes...."+radiodatayes)
              if(radiodatayes==1){
                  var yesdata= ","+document.getElementById('Y_'+parameters[ft]).value;
                 //  alert("yesdata....."+yesdata)
                  radioarr.push(yesdata);
              }
         }
      }
      if(selectedcheck==1){
         daydenom=$('select#daydenom option:selected').val();
         $.post(ctxpath+'/reportTable.do?reportBy=getMultiCalender&reportId='+reportid+'&elements='+elements+'&tableid='+tableid+'&factvalue='+factvaluearr+'&selectedfact='+selectedfact+'&radioarr='+radioarr+'&checkedfacts='+checkedfacts+'&daydenom='+daydenom,
            function(data){
               submitform(reportid);
            });
   }else{
       alert("Please select any of the checkboxes");
       parent.$("#ReportMultiCalendar").dialog('open');
   }
}
//method added by anitha for MTD,QTD,YTD on measures in AO report
function addRunTimeDateJoin(measType,coltype,coltypeDisp,colName,disColName,REPORTID,ctxPath){
        var isColumnPresent = false;
    if(colName.indexOf(coltype)!=-1){
        isColumnPresent=true;
    }
coltypeDisp=coltypeDisp.toString().replace("-"," ");
    if(!isColumnPresent){
        $.ajax({
            url:ctxPath+'/reportViewer.do?reportBy=tableChanges&tableChange='+measType+'&colName='+colName+'&disColName='+disColName+'&reportid='+REPORTID,
            success:function(data){
                if(data=='Exists'){
                    alert(disColName+ " "+coltypeDisp+" already exists")

                }else{
                    var path=ctxPath+"/reportViewer.do?reportBy=viewReport&action=measChange&REPORTID="+REPORTID;
                    parent.submiturls1(path);
                }
            }
        });
    }
    else
    {
        alert("You cannot add "+coltypeDisp+" to a "+coltypeDisp +" column");
    }
}
//end of method by anitha for MTD,QTD,YTD on measures in AO report