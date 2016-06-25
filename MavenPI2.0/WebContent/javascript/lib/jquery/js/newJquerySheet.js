/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var selectedTdCol=-1;
var selectedTdRow=-1;
var selectedTd=new Array();
var selectedBarLeft= new Array();
var selectedBarTop = new Array();
var isCommentWindowOpen = false;
var reportId = "";
var contextPath = "";
var colNamesArray = new Array();
 var isMouseDown = false;
 var startId="";
var endId="";

function setReportId(id){
    reportId = id;
}

function setContextPath(path){
    contextPath = path;
}

function updateSelectedCell(td){
    var id = td.id;

    if (selectedTd.length == 0 || selectedTd.length == 1){
        resetCellSelection();
        selectedTd = new Array();
        selectedTd.push(id);
    }
}

function addRow(obj){
    alert(selectedTd);
}

function deleteRow(obj){
    alert(obj);
}

function openAddColumnDialog(obj){
    $("#addExcelColumnDiv").dialog('open');
}

function deleteColumn(obj){
    alert(obj);
}

function exportExcel(){
    var fileName="";
//                jQuery.sheet.instance[0].exportExcel(fileName);
    document.forms.exportForm.submit();
}

function importExcel(){
    $("#importExcelDiv").dialog('open');
}

function closeImportForm(){
    $("#importExcelDiv").dialog('close');
    sleep(2000);
    var source = contextPath+"/TableDisplay/pbDisplay.jsp?tabId="+reportId;
    document.getElementById("iframe1").src= source;
    parent.document.getElementById('loading').style.display='none';
}

function sleep(milliseconds) {
    var start = new Date().getTime();
    while ((new Date().getTime() - start) < milliseconds){
    // Do nothing
    }
}

function openMeasuresDialag(){

    $.ajax({
        url:contextPath+'/reportViewer.do?reportBy=getMeasuresInContainer&reportId='+reportId+'&addRTMeasures=N',
        success:function(data){
             var html="";
             var dataJson = eval('('+data+')');
             var Measures=(dataJson.Measures);
             var MeasureNames=(dataJson.MeasureNames);
             var MeasIndex = (dataJson.MeasureIndex);
             for(var i=0;i<Measures.length;i++)
             {
                 html=html+"<option value=\""+Measures[i]+"\" measIndex=\""+MeasIndex[i]+"\">"+MeasureNames[i]+"</option>";
             }
             $("#TargetMeasures").html(html);
        }
    });

    $("#addTargetDiv").dialog('open');
}

function insertComment(){
    isCommentWindowOpen = true;
    var selectedCellAddress = selectedTd[0];
    var td = $("#"+selectedCellAddress);
    var commentWindow = jQuery("#commentWindow");
    var commentArea = jQuery("#commentArea");
    var x = td.position().top;
    var y = td.position().left;
    commentWindow.show();
    commentWindow.offset({top:x,left:y});
    commentArea.focus();
    commentArea.attr("address", selectedCellAddress);

    if (td.attr("comment")){
        commentArea.val(td.attr("comment"));
    }
    else{
        commentArea.val("Type your comment here");
        commentArea.select();
    }
}

function addComment(){
   // $("#jSheetDebugArea").val("in addComment");
    var commentWindow = jQuery("#commentWindow");
    var commentArea = jQuery("#commentArea");
    var cellAddress = commentArea.attr("address");
   // $("#jSheetDebugArea").val(cellAddress);
    var commentText = commentArea.val();
    commentWindow.hide();
    isCommentWindowOpen = false;
    if (commentText != "Type your comment here" && commentText != ""){
        var td = jQuery("#"+cellAddress);
        var row = td.attr("rowNum");
        var col = td.attr("colNum");
        td.attr("comment", commentText);
        td.attr("title", commentText);
        td.addClass('jSheetCommentWindow');

        $.ajax({
            url:contextPath+'/excel.do?reportBy=storeComment&REPORT_ID='+reportId+'&row='+row+'&col='+col+'&comment='+commentText,
            success: function(data){
            }
        });
    }
}

function clearComment(){
    var selectedCellAddress = selectedTd[0];
    var td = $("#"+selectedCellAddress);

    if (td.attr("comment")){
        td.attr("comment", "");
        td.attr("title","");
    }
    td.removeClass('jSheetCommentWindow');
    var row = td.attr("rowNum");
    var col = td.attr("colNum");
    $.ajax({
        url:s.contextPath+'/excel.do?reportBy=clearComment&REPORT_ID='+reportId+'&row='+row+'&col='+col,
        success: function(data){
        }
    });
}

function openFormatCellDialog(obj){
    $("#formatCell").dialog('open');
}

function formatExcelSheetCell( bgColor,fontColor){
    $("#formatCell").dialog('close');

    var selectedCellAddress = selectedTd[0];
    var td = $("#"+selectedCellAddress);

    td.css("background-color",bgColor);
    td.css("color",fontColor);

    var row = td.attr("rowNum");
    var col = td.attr("colNum");

    bgColor = bgColor.substr(1, bgColor.length);
    fontColor = fontColor.substr(1, fontColor.length);

    var reqString = contextPath+'/excel.do?reportBy=storeFormat&REPORT_ID='+reportId+'&row='+row+'&col='+col+'&bgColor='+bgColor+'&fontColor='+fontColor;
    var req = encodeURI(reqString);
    $.ajax({
        //url:s.contextPath+'/reportTable.do?reportBy=storeFormat&REPORT_ID='+s.reportId+'&row='+row+'&col='+col+'&bgColor='+bgColor+'&fontColor='+fontColor,
        url: req,
        success: function(data){}
    });
}

function clearFormat(){
    var selectedCellAddress = selectedTd[0];
    var td = $("#"+selectedCellAddress);

    td.css("background-color","");
    td.css("color","");

    var row = td.attr("rowNum");
    var col = td.attr("colNum");

    $.ajax({
        url:contextPath+'/excel.do?reportBy=clearFormat&REPORT_ID='+reportId+'&row='+row+'&col='+col,
        success: function(data){}
    });

}

function dynamicAddCol(colName){
    $("#addExcelColumnDiv").dialog('close');
    var col="";
    var selectedCellAddress = selectedTd[0];
    var td = $("#"+selectedCellAddress);
    var row = td.attr("rownum");
    col = td.attr("colnum");
  //alert(td.attr("colnum"));
    /* Make ajax call to store the new column information in Container */
    $.ajax({
        url:contextPath+'/excel.do?reportBy=addRTExcelColumn&REPORT_ID='+reportId+'&col='+col+'&columnName='+colName,
        success: function(data){
            parent.refreshReportTables(contextPath,reportId);
        }
    });
}

function dynamicAddTargetCol(){
    var elementId = $("#TargetMeasures").val();
    $("#addTargetDiv").dialog('close');

    /* Make ajax call to store the new column information in Container */
    $.ajax({
        url:contextPath+'/excel.do?reportBy=addRTTargetColumn&REPORT_ID='+reportId+'&elementId='+elementId,
        success: function(data){
            if (data == ""){
                alert("Target for the selected measure is already Present");
            }
            else{
                parent.refreshReportTables(contextPath,reportId);
            }
        }
    });
}

function addBarTopMenu(menuContent){
    jQuery(menuContent).find("td").each(function(i){
    var ul = jQuery(this).children("ul");
    var ctxMenuId = "ctxMenu"+i;
    ul.addClass("jqcontextmenu");
    ul.removeClass("dropDownMenu");
    ul.attr("id", ctxMenuId);
    var id = "#barTopMenuDiv"+i;
    var barTopDiv = $(id);
    $("#dummyArea").append(ul);
    barTopDiv.addcontextmenu(ctxMenuId);
    });
}

function addToDynamicColumn(col){
    for (var i=0;i<jS.insertedCols.length;i++){
        var curr = jS.insertedCols[i];
        if (curr > col)
            jS.insertedCols[i] = curr+1;
    }
    jS.insertedCols.push(col+1);
    $("#firstRowTable").show();

    if (s.targetEntryApplicable){
        $("#targetDiv").show();
    }
    else{
        $("#targetDiv").hide();
    }
    if (jS.insertedCols.length > 0){
        $("#buttonsDiv").show();
    }
    else{
        $("#buttonsDiv").hide();
    }
}

function resetCellSelection(){
    if (isCommentWindowOpen == true)
        addComment();
    if (selectedTd != null){
        for (var i=0;i<selectedTd.length;i++){
            selectedTdAddress = selectedTd[i];
            var prevSelectedTd = $("#"+selectedTdAddress);
            prevSelectedTd.removeClass("jSheetCellHighighted");
            prevSelectedTd.removeClass("ui-state-highlight");

            var comment = prevSelectedTd.attr("comment");
            if (comment)
                prevSelectedTd.addClass("jSheetCommentWindow");
        }
    }
    selectedTd = new Array();
}

function handleCellSelect(td){
    resetCellSelection();
    var selectedTdAddress="";
    var jqTd = $(td);

    var cellAddress = jqTd.attr("cellAddress");

    selectedTdCol=jqTd.attr("colNum");
    selectedTdRow=jqTd.attr("rowNum");
    selectedTdAddress=jqTd.attr("id");
    selectedTd.push(selectedTdAddress);
    var formulaVal = jqTd.attr("formula");
    if (!formulaVal){
        formulaVal = jqTd.html();
    }

    formulaVal = jQuery.trim(formulaVal);
    formulaVal = formulaVal.replace(/&nbsp;/g, '')
        .replace(/&gt;/g, '>')
        .replace(/&lt;/g, '<')
        .replace(/\t/g, '')
        .replace(/\n/g, '')
        .replace(/<br>/g, '\r')
        .replace(/<BR>/g, '\n');

    $("#jSheetControls_loc").html(cellAddress);
    $("#jSheetControls_formula").val(formulaVal);

    var editable = jqTd.attr("editable");
//    $("#jSheetDebugArea").val(cellAddress+"**"+editable+"**"+formulaVal);

    populateFormulaBar(jqTd);

    highlightCell(jqTd);
    highlightBars(cellAddress);
}

function highlightBars(cellAddress){
    clearSelectedBars();
    var loc = parseLocation(cellAddress);
    var row = loc[0];
    var col = loc[1];

    var barTopDiv = $("#barTopMenuDiv"+(col-1));
    selectedBarTop.push(col-1);
    barTopDiv.addClass("ui-state-active");

    var barLeftDiv = $("#barLeft"+row);
    barLeftDiv.addClass("ui-state-active");
    selectedBarLeft.push(row);
}

function clearSelectedBars(){
    for (var i=0;i<selectedBarTop.length;i++){
        var divNo = selectedBarTop[i];
        var barTopDiv = $("#barTopMenuDiv"+divNo);
        barTopDiv.removeClass("ui-state-active");
    }
    selectedBarTop = new Array();

    for (var j=0;j<selectedBarLeft.length;j++){
        var leftDivNo = selectedBarLeft[j];
        var barLeftDiv = $("#barLeft"+leftDivNo);
        barLeftDiv.removeClass("ui-state-active");
    }
    selectedBarLeft = new Array();
}

function selectCellLeft(){
    var selectedCellAddress = selectedTd[0];
    selectedCellAddress = selectedCellAddress.replace("cell_","");

    var cell = $("#cell_"+selectedCellAddress);
    var colNum = cell.attr("colNum");
    if (colNum != 0){
        var loc = parseLocation(selectedCellAddress);
        var row = loc[0];
        var col = loc[1];
        if (col != 0){
            var newCol = col-1;
            var newColName = columnLabelString(newCol);

            var newCellAddress = "cell_"+newColName+row;

            var td = $("#"+newCellAddress);
            if (td){
                handleCellSelect(td);
                populateFormulaBar(td);
            }
        }
    }
}

function selectCellRight(){
    var selectedCellAddress = selectedTd[0];
    selectedCellAddress = selectedCellAddress.replace("cell_","");

    var cell = $("#cell_"+selectedCellAddress);
    var lastCol = cell.attr("lastCol");
    if (lastCol && lastCol == 'true'){
        return;
    }

    var loc = parseLocation(selectedCellAddress);
    var row = loc[0];
    var col = loc[1];
    var newCol = col+1;
    var newColName = columnLabelString(newCol);

    var newCellAddress = "cell_"+newColName+row;

    var td = $("#"+newCellAddress);
    if (td && td!=null)
        handleCellSelect(td);
}

function selectCellTop(){
    var selectedCellAddress = selectedTd[0];
    selectedCellAddress = selectedCellAddress.replace("cell_","");

    var cell = $("#cell_"+selectedCellAddress);
    var rowNum = cell.attr("rowNum");
    if (rowNum != 0){
        var loc = parseLocation(selectedCellAddress);
        var row = loc[0];
        var col = loc[1];
        if (row > 0){
            var newRow = row-1;
            var newColName = columnLabelString(col);

            var newCellAddress = "cell_"+newColName+newRow;

            var td = $("#"+newCellAddress);

            if (td)
                handleCellSelect(td);
        }
    }
}

function selectCellBottom(){
    var selectedCellAddress = selectedTd[0];
    selectedCellAddress = selectedCellAddress.replace("cell_","");

    var cell = $("#cell_"+selectedCellAddress);
    var lastRow = cell.attr("lastRow");
    if (lastRow && lastRow == 'true'){
        return;
    }

    var loc = parseLocation(selectedCellAddress);
    var row = loc[0];
    var col = loc[1];
    var newRow = row+1;
    var newColName = columnLabelString(col);

    var newCellAddress = "cell_"+newColName+newRow;

    var td = $("#"+newCellAddress);

    if (td)
        handleCellSelect(td);
}

function saveExcel(){
    $.ajax({
        url:contextPath+'/excel.do?reportBy=updateRTExcelColumn&REPORT_ID='+reportId,
        success: function(data){
            alert("Data Saved");
        }
    });
}

function handleEnterKey(){
    var val = $("#jSheetControls_formula").val();
   
    var cellAddress = selectedTd[0];
    var td = jQuery("#"+cellAddress);
    var row = td.attr("rowNum");
    var col = td.attr("colNum");
    cellAddress = cellAddress.replace("cell_", "");

    var editable = td.attr("editable");
//    $("#jSheetDebugArea").val("Enter Key for "+cellAddress+"**"+editable+"**");

    if (editable && editable=='true'){
        val = jQuery.trim(val);
        if (val != ""){
            if (val.charAt(0)=='=')
                td.attr("formula",val);
            val=encodeURIComponent(val);
            $.ajax({
                url:contextPath+'/excel.do?reportBy=storeValue&REPORT_ID='+reportId+'&row='+row+'&col='+col+'&formulaVal='+val+'&cellAddress='+cellAddress,
                success: function(data){
                 
                    if (data != ""){ 
                        var totData=data.split("~");
                        populateCellValues(totData[0]);
                        var json=eval("("+totData[1]+")");
                        $("#"+json.SubTotal[0].address).html(json.SubTotal[0].value)
                        $("#"+json.GrandTotal[0].address).html(json.GrandTotal[0].value)
                        $("#"+json.CategoryTotal[0].address).html(json.CategoryTotal[0].value)
    //                    alert(json.CategoryMax[0].address)
    //                    alert(json.CategoryMax[0].value)
                        $("#"+json.CategoryMax[0].address).html(json.CategoryMax[0].value)
                        $("#"+json.CategoryMin[0].address).html(json.CategoryMin[0].value)
                        $("#"+json.OverallMax[0].address).html(json.OverallMax[0].value)
                        $("#"+json.OverallMin[0].address).html(json.OverallMin[0].value)
                        $("#"+json.Average[0].address).html(json.Average[0].value)
                       
                    }

                    else
                    {
                       
                      td.removeAttr("formula");
                      td.html("error");
                    }
                }
            });
        }
        else{ 
            $.ajax({
                url:contextPath+'/excel.do?reportBy=clearValue&REPORT_ID='+reportId+'&row='+row+'&col='+col+'&cellAddress='+cellAddress,
                success: function(data){
                    populateCellValues(data);
                }
            });
          td.removeAttr("formula");
        }
    }
    selectCellBottom();
}

function populateCellValues(data){
    var json = eval('('+data+')');
    //alert("from nonval"+data);
    if(data!=null)
        {
    var returnVals = json.CellValues;

    for (var i=0;i<returnVals.length;i++){
        var cellValue = returnVals[i];
        //alert(cellValues.address+" "+cellValues.value+" "+cellValues.formula);
        var address = cellValue.address;
        var formula = cellValue.formula;
        var value = cellValue.value;

        var td = jQuery("#cell_"+address);
        td.html(value);
        if (formula)
            td.attr("formula", formula);
        else
            td.removeAttr("formula");
    }
    }

    
}

function handleCopy(){
    var addresses = "";

    for (var i=0;i<selectedTd.length;i++){
        var cellAddress = selectedTd[i];
        cellAddress = cellAddress.replace("cell_", "");
        addresses = addresses + "~" + cellAddress;
    }
    addresses = addresses + "~";

    $.ajax({
        url:contextPath+'/excel.do?reportBy=copyCells&REPORT_ID='+reportId+'&cellAddress='+addresses,
        success: function(data){
        }
    });
}

function debug(txt){
     $("#jSheetDebugArea").val(txt);
}

function handlePaste(){
    var cellAddress = selectedTd[0];
    var td = jQuery("#"+cellAddress);
    cellAddress = cellAddress.replace("cell_", "");

    var editable = td.attr("editable");

    if (editable && editable=='true'){
        $.ajax({
            url:contextPath+'/excel.do?reportBy=pasteCells&REPORT_ID='+reportId+'&cellAddress='+cellAddress,
            success: function(data){
                populateCellValues(data);
                handleCellSelect(td);
            }
        });
    }
}

function handleKeyPress(event){
    switch (event.keyCode) {
            case key.TAB:
            case key.RIGHT:
                selectCellRight();
                break;
            case key.ENTER:
                handleEnterKey();
                break;
            case key.DOWN:
                selectCellBottom();
                break;
            case key.LEFT:
                selectCellLeft();
                break;
            case key.UP:
                selectCellTop();
                break;
            case key.V:
                if (event.ctrlKey){
                    handlePaste();
                }
                break;
            case key.C:
                if (event.ctrlKey){
                    handleCopy();
                }
//                copyContent = ;
                break;
            /*case key.ESCAPE:
                jS.evt.cellEditAbandon();
                break;
            case key.PAGE_UP:
                return jS.evt.keyDownHandler.pageUpDown(true);
                break;
            case key.PAGE_DOWN:
                return jS.evt.keyDownHandler.pageUpDown();
                break;
            case key.V:
                return jS.evt.keyDownHandler.pasteOverCells(e);
                break;
            case key.C:
                jS.copyContent = jS.obj.copyArea().val();
                break;
            case key.Y:
                return jS.evt.keyDownHandler.redo(e);
                break;
            case key.Z:
                return jS.evt.keyDownHandler.undo(e);
                break;
            case key.F:
                return jS.evt.keyDownHandler.findCell(e);
            case key.CONTROL: //we need to filter these to keep cell state
            case key.CAPS_LOCK:
            case key.SHIFT:
            case key.ALT:
            case key.HOME:
            case key.END:jS.evt.cellSetFocusFromKeyCode(e);
                break;*/
            default:break;  //nothing to do
        }
       
        
}
function handleKeyUp(event)
{
var keycode=event.keyCode;
    if(keycode==key.UP||keycode==key.LEFT||keycode==key.RIGHT||keycode==key.DOWN||keycode==key.TAB||keycode==key.ENTER)
   //if(key==37)
       
    $("#jSheetControls_formula").select();
       
}
function handleMouseDown(event)
{
                                if (event.button == 2){   //On rightclick, update the cell information
                                updateSelectedCell(event.target);
                            }
                            else{
    startId=event.target.id;
    var startingId=startId.replace("cell_","");
     var startIndex=parseLocation(startingId);
 // $("#jSheetDebugArea").val(startIndex[0]);
     isMouseDown = true;
//     $(this).toggleClass("highlighted");
     return false; // prevent text selection
                            }
}
function handleMouseOver(event)
{

   if (isMouseDown)
   {
     resetCellSelection();
     endId=event.target.id;
     var startingId=startId.replace("cell_","");
     var endingId=endId.replace("cell_","");
     var startIndex=parseLocation(startingId);
     var endIndex=parseLocation(endingId);

      if(startIndex[0]<=endIndex[0])
          {
     for(var i=startIndex[0];i<=endIndex[0];i++)
         {
             if(startIndex[1]<=endIndex[1])
            {
             for(var j=startIndex[1];j<=endIndex[1];j++)
                 {
                   selectCells(i,j);

                 }
            }
            else if(startIndex[1]>=endIndex[1])
                {
                    for(var k=startIndex[1];k>=endIndex[1];k--)
                 {
                   selectCells(i,k);

                 }
                }
         }
          }
          if(startIndex[0]>=endIndex[0])
            {
             for(var l=startIndex[0];l>=endIndex[0];l--)
           {
               if(startIndex[1]>=endIndex[1])
              {
             for(var m=startIndex[1];m>=endIndex[1];m--)
                 {
                   selectCells(l,m);
                 }
              }
              else if(startIndex[1]<=endIndex[1])
               {
                       for(var n=startIndex[1];n<=endIndex[1];n++)
                 {
                   selectCells(l,n);
                 }
              }
           }

              }
        }
    //alert(startingId);
  
}

function selectCells(i,j)
{
    var colName = columnLabelString(j);
    cellAddress = "cell_"+colName+i;
    var cell = $("#"+cellAddress);
    highlightCell(cell);
    selectedTd.push(cellAddress);
}
function handleMouseUp(event)
{
    isMouseDown = false;
}

function populateFormulaBar(jqTd){
    var editable = jqTd.attr("editable");

    if (editable && editable=='true'){
        $("#jSheetControls_formula").removeAttr("readonly")
        .focus()
        .select();
    }
    else{
        $("#jSheetControls_formula").attr('readonly',"true")
        .focus()
        .select();
    }

//    var debugVal = $("#jSheetDebugArea").val();
//    debugVal = debugVal + "\n set the formula bar";
//    $("#jSheetDebugArea").val(debugVal);
}

function highlightCell(td){
    td.addClass("ui-state-highlight");
    td.addClass("jSheetCellHighighted");

    if (td.hasClass("jSheetCommentWindow")){
        td.removeClass("jSheetCommentWindow");
    }
}

function handleBarTopClick(td,rowCount){
    resetCellSelection();
    var jqTd = $(td);
    var colName = jqTd.attr("colName");
    var finished = false;
    var cellAddress = "";
    var i=0;
    while(finished != true){

        cellAddress = "cell_"+colName+i;
        i++;
        var cell = $("#"+cellAddress);

        if (cell != null){
            highlightCell(cell);
            selectedTd.push(cellAddress);

            var lastRow = cell.attr("lastRow");
            if (lastRow && lastRow == 'true')
                finished = true;
        }
    }
    highlightBars(cellAddress);
}

function handleBarLeftClick(td,colCount){
    resetCellSelection();
    var jqTd = $(td);
    var rowNum = jqTd.attr("rowNumber");
    var cellAddress = "";

    for (var i=1;i<=colCount;i++){
        var colName = columnLabelString(i);
        cellAddress = "cell_"+colName+rowNum;
        var cell = $("#"+cellAddress);

        if (cell != null){
            highlightCell(cell);
            selectedTd.push(cellAddress);
        }
    }
    highlightBars(cellAddress);
}

function handleSelectAll(rowCount, colCount){
    resetCellSelection();
    var cellAddress = "";
    for (var i=1;i<=colCount;i++){
        var colName = columnLabelString(i);
        var j=1;
        var finished = false;
        while(finished != true){
            cellAddress = "cell_"+colName+j;
            var cell = $("#"+cellAddress);

            if (cell != null){
                highlightCell(cell);
                selectedTd.push(cellAddress);

                var lastRow = cell.attr("lastRow");
                if (lastRow && lastRow == 'true')
                    finished = true;
            }
            j++;
        }
    }
    highlightBars(cellAddress);
}

function populateCopyArea(){

    var copyText = "";

    for (var i=0;i<selectedTd.length;i++){
        var cellAddress = selectedTd[i];
        var td = jQuery("#"+cellAddress);
        cellAddress = cellAddress.replace("cell_", "");
        var row = td.attr("rowNum");
        var col = td.attr("colNum");

        var v = td.attr('formula');

            if (!v) {
                v = manageHtmlToText(td.html());
            }

            copyText = copyText + cellAddress + "~" + v;
            if (j!=jS.highlightedLast.colEnd)
                copyText = copyText + "<NEXTCOL>";
        }
        if (i != jS.highlightedLast.rowEnd)
            copyText = copyText + "<NEXTROW>";

    jS.obj.copyArea().val(copyText);
}

function manageHtmlToText(v) {
    v = jQuery.trim(v);
    if (v.charAt(0) != "=") {
        v = v.replace(/&nbsp;/g, ' ')
            .replace(/&gt;/g, '>')
            .replace(/&lt;/g, '<')
            .replace(/\t/g, '')
            .replace(/\n/g, '')
            .replace(/<br>/g, '\r')
            .replace(/<BR>/g, '\n');
    }
    return v;
}

function columnLabelIndex(str) {
    // Converts A to 1, B to 2, Z to 26, AA to 27.
    var num = 0;
    for (var i = 0; i < str.length; i++) {
            var digit = str.toUpperCase().charCodeAt(i) - 65 + 1;	   // 65 == 'A'.
            num = (num * 26) + digit;
    }
    return num;
}

function parseLocation(locStr) { // With input of "A1", "B4", "F20",
    if (locStr != null &&		// will return [1,1], [4,2], [20,6].
        locStr.length > 0 &&
        locStr != "&nbsp;") {
        for (var firstNum = 0; firstNum < locStr.length; firstNum++) {
            if (locStr.charCodeAt(firstNum) <= 57) {// 57 == '9'
                break;
            }
        }

        return [ parseInt(locStr.substring(firstNum)),
                     columnLabelIndex(locStr.substring(0, firstNum)) ];
    } else {
        return null;
    }
}

function columnLabelString(index) {
    // The index is 1 based.  Convert 1 to A, 2 to B, 25 to Y, 26 to Z, 27 to AA, 28 to AB.
    // TODO: Got a bug when index > 676.  675==YZ.  676==YZ.  677== AAA, which skips ZA series.
    //	   In the spirit of billg, who needs more than 676 columns anyways?
    var b = (index - 1).toString(26).toUpperCase();   // Radix is 26.
    var c = [];
    for (var i = 0; i < b.length; i++) {
        var x = b.charCodeAt(i);
        if (i <= 0 && b.length > 1) {				   // Leftmost digit is special, where 1 is A.
            x = x - 1;
        }
        if (x <= 57) {								  // x <= '9'.
            c.push(String.fromCharCode(x - 48 + 65)); // x - '0' + 'A'.
        } else {
            c.push(String.fromCharCode(x + 10));
        }
    }
    return c.join("");
}

var key = {
	BACKSPACE: 			8,
	CAPS_LOCK: 			20,
	COMMA: 				188,
	CONTROL: 			17,
	ALT:				18,
	DELETE: 			46,
	DOWN: 				40,
	END: 				35,
	ENTER: 				13,
	ESCAPE: 			27,
	HOME: 				36,
	INSERT: 			45,
	LEFT: 				37,
	NUMPAD_ADD: 		107,
	NUMPAD_DECIMAL: 	110,
	NUMPAD_DIVIDE: 		111,
	NUMPAD_ENTER: 		108,
	NUMPAD_MULTIPLY: 	106,
	NUMPAD_SUBTRACT: 	109,
	PAGE_DOWN: 			34,
	PAGE_UP: 			33,
	PERIOD: 			190,
	RIGHT: 				39,
	SHIFT: 				16,
	SPACE: 				32,
	TAB: 				9,
	UP: 				38,
	F:					70,
	V:					86,
	Y:					89,
	Z:					90,
        C:                                      67
  };
