/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var rowId=0;
var count=0;
var tabName="";
var bussTableNamesDrag="";
var bussTabNameArray=new  Array();
var bussTabIdArray=new Array();
var bussTabDetailsArray=new Array();
var columnArr=new Array();
var columnNameArr=new Array();
var grpId="";

var srcTabNameArr=new Array();
var srcTabIdArr=new Array();
var srcColIdArr=new Array();
var srcColNameArr=new Array();
var dimensionIdArr=new Array();
var dimFactArray=new Array();
var srcRowId=0;
var relationSrcArray=new Array();
var restRelArr=new Array();
var relationString="";
var relationCodedStr="";
var noAutoSrcRelSugg=true;
var srcTableGrpID="";
var relTableIds=new Array();
var relTableNames=new Array();
var addRelTabs=new Array();
var relTypeArr=new Array();
var roleName="";
$(function() {

    $(".addGrpTables").contextMenu({
        menu: 'groupTableMenu',
        leftButton: true
    }, function(action, el, pos) {
        // alert($(el).parent().parent().attr('id'))
        addGrpTabMenu(action, el.parent("li"), pos);
    });
    $(".addGrpDims").contextMenu({
        menu: 'groupDimMenu',
        leftButton: true
    }, function(action, el, pos) {
        // alert($(el).parent().parent().attr('id'))
        addGrpDimensionMenu(action, el.parent("li"), pos);
    });

    $(".addNewRelTabs").contextMenu({
        menu: 'addTabsToSrc',
        leftButton: true
    }, function(action, el, pos) {
        // alert('------in ready-----'+el.parent().attr('id'))
        addTabsToScr(action, el, pos);
    });

    $("#draggedDims").accordion({


        });

    $("#warning").dialog({
        bgiframe: true,
        autoOpen: false,
        modal: true,
        buttons: {
            Ok: function() {
                $(this).dialog('close');
            }
        }
    });

    $("#dialog").dialog({
        bgiframe: true,
        autoOpen: false,
        resizable: false,
        height:140,
        modal: true,
        overlay: {
            backgroundColor: '#000',
            opacity: 0.5
        },
        buttons: {
            Cancel: function() {
                $(this).dialog('close');
            },
            'Clear all items': function() {
                $(this).dialog('close');
                clearAllTables();

            }
        }
    });

    $("#cartesianDialog").dialog({
        bgiframe: true,
        autoOpen: false,
        resizable: false,
        height:140,
        modal: true,
        overlay: {
            backgroundColor: '#000',
            opacity: 0.5
        },
        buttons: {
            Cancel: function() {
                $(this).dialog('close');
            },
            'Save': function() {
                $(this).dialog('close');
                saveSrcrelations();

            }
        }
    });
    $("#createGroup").dialog({
        bgiframe: true,
        autoOpen: false,
        height:250,
        width:300,
        modal: true,
        buttons: {
            Cancel: function() {
                $(this).dialog('close');
            },
            'Create Group': function() {
                createBussGroup();

            }
        },
        close: function() {

        }
    });
//      $("#GroupEditdiv").dialog({
//                        autoOpen: false,
//                        height: 400,
//                        width: 400,
//                        position: 'justify',
//                        modal: true
//                    });
    $(".bussGroupMenu").contextMenu({
        menu: 'groupListMenu',
        leftButton: true
    }, function(action, el, pos) {
        // alert($(el).parent().parent().attr('id'))
        bussCtxMenu(action, el.parent("li"), pos);
    });


});
//Right Click

function addGrpTabMenu(action, el, pos) {



    switch (action) {

        case "addTables":
        {
            grpId=el.attr("id");
            //  alert('addTasbles=='+grpId);
            showBussTabsArea();
            break;
        }
    }
}
function addGrpDimensionMenu(action, el, pos) {



    switch (action) {

        case "addDimensions":
        {
            grpId=el.attr("id");
            showDimensionLayer();
            break;
        }
        case "addTimeDimensions":
        {

            break;
        }
    }
}

function addTabsToScr(action, el, pos) {
    switch (action) {

        case "addTabsToSrc":
        {  // alert('hi')
            var temp=el.parent("li")
            //  alert('-     -'+el.parent("li").parent("li"))
            //  alert('add tabs to src id'+temp+' buss tab id second element--'+el.parent("li").parent("li"))
            srcTableGrpID=temp.attr("id");
            addTablesToSrc(temp.attr("id"),el.attr("id"));



            break;
        }
    }
}

function bussCtxMenu(action, el, pos) {

    switch (action) {
        case "deleteDim":
        {
            var msg = "Delete " + $(el).find("#contactname").text() + "?";
            $("#HiddenFieldRowId").val($(el).find("#customerid").text());
            confirm(msg);
            break;
        }
        case "createGroup":
        {
            addBussinessGroup();
            break;
        }



        case "edit":
        {
            alert(
                'Action: ' + action + '\n\n' +
                'Element ID: ' + $(el).attr('id') + '\n\n' +
                'X: ' + pos.x + '  Y: ' + pos.y + ' (relative to element)\n\n' +
                'X: ' + pos.docX + '  Y: ' + pos.docY + ' (relative to document)'
                );
        }
    }
}

  
// End of Rt Click

function getBussTableDetails(tableName,tableId,parAppId){
//    alert("tableName\t"+tableName)
//    alert("tableId"+tableId)
//    alert("parAppId"+parAppId)
    var table=document.getElementById("Rel-"+tableName);
    var cols=table.getElementsByTagName("li");
    createBussFact(cols,tableName,tableId,parAppId);
}


function createBussFact(cols,tableName,tableId,parAppId){
    //    alert("in createBussFact")
    // var h3=document.createElement("h3");
    // h3.className=""
    var flag=true;
    if(parAppId=="draggedFacts"){
        var factIds=dimFactArray.toString();
        if(factIds.match(tableId)==null)
            flag=true;
        else{
            alert("Fact Already Added");
            return;
        }

    }
    if(flag){
        var str="<h3 id='h3-"+tableName+"' align='left' style='border:0px' class='ui-accordion-header ui-helper-reset ui-state-default ui-corner-all' role='tab' aria-expanded='true' tabindex='0'>";
        // str=str+"<span class='ui-icon ui-icon-triangle-1-e'/>";
        if(parAppId=="draggedFacts")
            str=str+"<img title='Remove Fact' href=\"javascript:removeTable('"+tableId+"')\" class='ui-icon ui-icon-close' /><a title='Click To See Columns' href=\"javascript:showMyDiv('"+tableId+"')\" style='color: black; text-decoration: none;'>"+ tableName+"</a></h3>";
        else
            str=str+"<img title='Remove Fact' onclick=\"javascript:removeFactTable('"+tableId+"')\" class='ui-icon ui-icon-close' /><a title='Click To See Columns' href=\"javascript:showMyDiv('"+tableId+"')\" style='color: black; text-decoration: none;'>"+ tableName+"</a></h3>";
        var divDraggableTables=document.getElementById(parAppId);
        var parentDiv=document.createElement("li");
        parentDiv.id="mainDiv-"+tableId;
        parentDiv.className="ui-state-default draggedDivs ui-corner-all"
        parentDiv.innerHTML=parentDiv.innerHTML+str;

        var div=document.createElement("div");
        div.id="tabDet-"+tableName;

        div.className="myDraggedDiv";
        var p=document.createElement("p");
        for(i=0;i<cols.length;i++){

            var colspan=cols[i].getElementsByTagName("span");
            var img=cols[i].getElementsByTagName("img");
            var span=document.createElement("span");
            span.className="tableColumns";
           // var xy=(img[0].id).substr(6,(img[0].id).length);
            span.id=tableName;
            span.innerHTML=colspan[0].innerHTML+"<br>";
            p.appendChild(span);
        //str=str+"<span class='tableColumns'>"+sp[1]+"</span><br>";

        }
        div.appendChild(p)
        parentDiv.appendChild(div);
        divDraggableTables.appendChild(parentDiv);
        /*// str=str+"</p></div>"
                str=str+"</p>";
                str=str+"</div>";*/
        if(parAppId!="draggedFacts"){
            //            alert("hiiiiii")
            //            alert("tableName--"+tableName)
            //            alert("tableId--"+tableId)
            getRelatedTablesBG(tableName,tableId);
        //    alert("end")
        }
        else{
            //            alert("else")
            dimFactArray.push(tableId);
        }
    }
}

function showMyDiv(tableId){
    var mainDivID=document.getElementById("mainDiv-"+tableId);
    var divId=mainDivID.getElementsByTagName("div");
    var $div=$(divId[0]);
    var options = {};
    $div.toggle("explode",options,500);
}
function getRelatedTablesBG(tableName,tabId){
    //    alert("hhhhhhhhhhhhhhhhhhhh")
    $.ajax({
        url: 'businesGroup.do?action=getRelTables&tableId='+tabId,
        success: function(data) {
            if(data!="noData")
                addRelatedTables(tableName,data,tabId);

        }

    });
}


function addRelatedTables(parentTable,data,parId){
    //    alert("in addRelatedTables")
    var relatedTable=data.getElementsByTagName("relatedTable");
    var branches;
    var tables;
    var columns;
    var tableName;
    var columnName;
    var tableId;
    var id;
    var str="<li id='Rel-"+parId+"'><span class='folder'>"+parentTable+"</span><ul>" ;
    for(i=0;i<relatedTable.length;i++){
        tables=relatedTable[i].getElementsByTagName("table");
        tableId=relatedTable[i].getElementsByTagName("table-id");
        tableName=tables[0].childNodes[0].nodeValue;
        id=tableId[0].childNodes[0].nodeValue;
        columns=relatedTable[i].getElementsByTagName("column-name");
        str=str+"<li class='closed'><img src='images/treeViewImages/database_table.png'><span id='RelTab-"+id+"'>"+tableName+"</span>";
        str=str+"<ul id='Rel-"+tableName+"' class='closed'id='folder212'>";
        for(k=0;k<columns.length;k++){
            columnName=columns[k].childNodes[0].nodeValue;
            str=str+"<li><span class='file'>"+columnName+"</span></li>";
        }
        str=str+"</ul></li>";
    }

    str=str+"</ul></li>";
    branches = $(str).appendTo("#bussRelatedTable");
    $("#bussRelatedTable").treeview({
        add: branches
    });
    var dragRelTables=$('#bussRelatedTable > li > ul > li >span')
    // var $draggableTables=$('#draggableTables');

    $(dragRelTables).draggable({
        helper:"clone",
        effect:["", "fade"]
    });

}

function delTables(){
    if(count>0){
        $('#dialog').dialog('open');
        count=0;
    }
    else{
        $('#warning').dialog('open');
        $("#alert").html("There are no tables to clear")
    }
}

function clearAllTables(){
    var divDraggableTables=document.getElementById("divDraggableTables");
    var childs=divDraggableTables.childNodes;
    for(i=-childs.length+1;i<childs.length;i++){
        divDraggableTables.removeChild(childs[0]);


    }
    bussTabIdArray.length=0;
    bussTabNameArray.length=0;

}


function addBussinessGroup(){
    var $grpName=$("#grpName");
    var $grpDesc=$("#grpDesc");
    $grpName.val("");
    $grpDesc.val("");
    $('#createGroup').dialog('open');
}


function createBussGroup(){
    $('#createGroup').dialog('close');
    var groupName=$("#grpName").val();
    var grpDesc=$("#grpDesc").val();
    var connId=document.getElementById("connId").value;
    //alert("businesGroup.do?action=addGroup&grpName="+groupName+"&grpDesc="+grpDesc+"&connId="+connId);
    groupName=groupName.replace("&","'|| chr(38) ||'","gi");
    grpDesc=grpDesc.replace("&","'|| chr(38) ||'","gi");
    // alert(groupName);
    $.ajax({
        url: 'businesGroup.do?action=addGroup&grpName='+groupName+'&grpDesc='+grpDesc+"&connId="+connId,
        success: function(data) {
            if(data=="0")
                alert("Unable to Add Grp")
            else{
                alert("Business Group created successfully");
                groupName=groupName.replace("'|| chr(38) ||'","&","gi");
                createGrpTree(groupName,data);
            }
        }

    });
}

function createGrpTree(groupName,groupId){
    var branches = $("<li><img src='images/treeViewImages/bricks.png' id='Grp-"+groupId+"'><span class='createFolderMenu'>&nbsp;"+groupName+"</span><ul>" +

        "<li id='dimGrp-"+groupId+"' class='closed' ><img src='images/treeViewImages/Dim.gif'> <span class='addGrpDims'>&nbsp;Dimensions</span>" +
        "<ul class='closed'></ul></li>"+
        "<li class='closed'><span class='folder'>&nbsp;Facts</span>"+
        "<ul></ul></li>"+
        "<li class='closed'><img src='images/beaker-empty.png'><span >&nbsp;Buckets</span>"+
        "<ul></ul></li>"+
        "<li id='AllTab-"+groupId+"'><span  class='addGrpTables'>&nbsp;All Tables</span>"+
        "<ul id='ul-"+groupId+"'></ul></li>"+
        "<li class='closed'><img src='images/treeViewImages/folder-closed.gif'><span>&nbsp;Business Roles</span>"+
        "<ul></ul></li>"+
        "</li></ul></li>").appendTo("#bussGrps");
    $("#bussGrps").treeview({
        add: branches
    });
    $(".addGrpTables").contextMenu({
        menu: 'groupTableMenu',
        leftButton: true
    }, function(action, el, pos) {
        // alert($(el).parent().parent().attr('id'))
        addGrpTabMenu(action, el.parent("li"), pos);
    });
    $(".addGrpDims").contextMenu({
        menu: 'groupDimMenu',
        leftButton: true
    }, function(action, el, pos) {
        // alert($(el).parent().parent().attr('id'))
        addGrpDimensionMenu(action, el.parent("li"), pos);
    });
    refreshPage()
}

function showBussTabsArea(){

    var ob=document.getElementById("myDbTableList");
    ob.style.visibility="visible";
    ob.style.display="table-cell";
    var ob1=document.getElementById("draggableTables");
    ob1.style.visibility="visible";
    ob1.style.display="table-cell";
    var ob2=document.getElementById("relDBTables");
    ob2.style.visibility="visible";
    ob2.style.display="table-cell";
}

function hideBussTabsArea(){
    var ob=document.getElementById("myDbTableList");
    ob.style.visibility="hidden";
    ob.style.display="none";
    var ob1=document.getElementById("draggableTables");
    ob1.style.visibility="hidden";
    ob1.style.display="none";
    var ob2=document.getElementById("relDBTables");
    ob2.style.visibility="hidden";
    ob2.style.display="none";
}
function addBusstablesToGrp(bussTabIds,groupId){
    // alert("add tables------>"+bussTabIds+"----grpIds--->"+groupId);
    var tabId;
    var parentLi=document.getElementById(grpId);
    var ul=parentLi.getElementsByTagName("ul");
    var ulId=ul[0].id;
    var str="";
    var bussDbIds=bussTabIds.split("-");
    for(i=0;i<bussTabIdArray.length;i++){
        tabId=document.getElementById(bussTabNameArray[i]);
        var li=tabId.getElementsByTagName("li");
        str="<li id='"+bussDbIds[i]+","+bussTabIdArray[i]+","+groupId+"'><img src='images/treeViewImages/database_table.png'><span class='openmenubusgrp'>"+bussTabNameArray[i]+"</span><ul>"
        str+="<li id='dbTabId-"+bussTabIdArray[i]+"-"+groupId+"' class='closed'><span id='bussTb-"+bussDbIds[i]+"' class='addNewRelTabs'>source</span><ul id='src-"+bussTabIdArray[i]+"-"+groupId+"'>"
        str+="<li class='closed'><span>"+bussTabNameArray[i]+"</span><ul>";

        for(k=0;k<li.length;k++){

            var span=li[k].getElementsByTagName("span");
            str=str+"<li class='closed'><span class='file'>"+span[0].innerHTML+"</span></li>";
        }
        str=str+"</li></ul></li></ul></li></ul></li>";
        var branches = $(str).appendTo("#"+ulId);
        $("#"+ulId).treeview({
            add: branches
        });
    }
    $(".addNewRelTabs").contextMenu({
        menu: 'addTabsToSrc',
        leftButton: true
    }, function(action, el, pos) {
        addTabsToScr(action, el, pos);
    });
    $(".openmenubusgrp").contextMenu(
    {
        menu: 'myMenubusgrp',
        leftButton: true
    }, function(action, el, pos) {
        bustableId=$(el).parent().attr('id');
        contextMenuWork(action, el.parent("li"), pos);

    });
    var ob=document.getElementById("myDbTableList");
    ob.style.visibility="hidden";
    ob.style.display="none";
    var ob1=document.getElementById("draggableTables");
    ob1.style.visibility="hidden";
    ob1.style.display="none";
    var ob2=document.getElementById("relDBTables");
    ob2.style.visibility="hidden";
    ob2.style.display="none";
// alert('reload')
// window.location.reload();
}

/*
function addBusstablesToGrp(){
    var tabId;
    var parentLi=document.getElementById(grpId);
    var ul=parentLi.getElementsByTagName("ul");
    var ulId=ul[0].id;
    var str="";
    for(i=0;i<bussTabIdArray.length;i++){
        tabId=document.getElementById(bussTabNameArray[i]);
        var li=tabId.getElementsByTagName("li");

        str="<li class='closed'><img src='images/treeViewImages/database_connect.png'><span>"+bussTabNameArray[i]+"</span><ul>";

        for(k=0;k<li.length;k++){

            var span=li[k].getElementsByTagName("span");
            str=str+"<li class='closed'><span class='file'>"+span[0].innerHTML+"</span></li>";
        }
        str=str+"</li></ul></li>";
        var branches = $(str).appendTo("#"+ulId);
        $("#"+ulId).treeview({
            add: branches
        });
    }
    var ob=document.getElementById("myDbTableList");
    ob.style.visibility="hidden";
    ob.style.display="none";
    var ob1=document.getElementById("draggableTables");
    ob1.style.visibility="hidden";
    ob1.style.display="none";
    var ob2=document.getElementById("relDBTables");
    ob2.style.visibility="hidden";
    ob2.style.display="none";
}
*/
function addDimension(ui){
    var dimensionDrag=document.getElementById("dimensionDrag");
    var draggedDims=document.getElementById("draggedDims");
    var dragId=ui.draggable.attr("id").split("-");
    var dimensionId=dragId[1];
    var allDimIds=dimensionIdArr.toString();
    if(allDimIds.match(dimensionId)==null)
        flag=true
    else{
        alert("Dim Already Added");
        return;
    }


    if(flag){
        var dimName=ui.draggable.html();
        var str="<h3 id='h3-Dim-"+dimensionId+"' align='left' style='border:0px' class='ui-accordion-header ui-helper-reset ui-state-default ui-corner-all' role='tab' aria-expanded='true' tabindex='0'>";
        // str=str+"<span class='ui-icon ui-icon-triangle-1-e'/>";
        str=str+"<img title='Remove Dimension' onclick=\"javascript:removeDimension('"+dimensionId+"')\" class='ui-icon ui-icon-close'/><a title='Show Dimension Tables' href=\"javascript:showDimension('"+dimensionId+"')\" style='color: black; text-decoration: none;'>"+dimName+"</a></h3>";
        /* var accordion=document.createElement("div");
    accordion.id="mainDiv-"+tabId;
    accordion.innerHTML=accordion.innerHTML+str;
    accordion.className="ui-state-default draggedDivs ui-corner-all"

     */
        var parentDiv=document.createElement("li");
        parentDiv.id="dimDiv-"+dimensionId;
        //parentDiv.style.height="100px";
        // parentDiv.style.overflow="scroll";
        parentDiv.className="ui-state-default draggedDivs ui-corner-all"
        parentDiv.innerHTML +=str;
        //parentDiv.style.height="25px";

        var subDiv=document.createElement("div");
        subDiv.className="myDraggedDiv";
        subDiv.id="subDiv-"+dimensionId;
        var ulObj=document.getElementById("ul-"+ui.draggable.attr("id"));
        var liObjs=ulObj.getElementsByTagName("li");
        var tabName;
        var tabId;
        var tempTabIdArr=new Array();
        var tempTabNameArr=new Array();
        for(i=0;i<liObjs.length;i++){

            var temp=liObjs[i].id.split("-");
            tempTabIdArr.push(temp[1]);
            var span=liObjs[i].getElementsByTagName("span");
            tempTabNameArr.push(span[0].innerHTML);
            var divSpan=document.createElement("span");
            divSpan.innerHTML=span[0].innerHTML+"<br>"
            divSpan.className="tableColumns";
            subDiv.appendChild(divSpan);
        }
        parentDiv.appendChild(subDiv);
        draggedDims.appendChild(parentDiv);
        getRelatedDimTables(ui.draggable.html(), tempTabIdArr.toString(),dimensionId);
        dimensionIdArr.push(dimensionId);
    }

}

function showDimension(dimId){
    var divId=document.getElementById("subDiv-"+dimId);
    var $div=$(divId);
    var options = {};
    $div.toggle("explode",options,500);
}
function getRelatedDimTables(dimTableName,tabId,dimensionId){
    $.ajax({
        url: 'businesGroup.do?action=getRelDimTables&tableId='+tabId+'&dimensionId='+dimensionId,
        success: function(data) {
            if(data!="noData")
                addRelatedTables(dimTableName,data,dimensionId);

        }

    });
}

function removeAllDims(){
    for(i=0;i<dimensionIdArr.length;i=0){
        removeDimension(dimensionIdArr[0]);
    }
}
function removeDimension(dimId){


    var dimensionDrag=document.getElementById("dimensionDrag");

    var $TabID=$("#Rel-"+dimId+" >ul >li >span");
    if($TabID.attr("id")!=undefined){
        var tabIdRemove=($TabID.attr("id")).split("-");
        removeTable(tabIdRemove[1]);
    }
    var bussRelatedTable=document.getElementById("bussRelatedTable");
    var liChild=document.getElementById("Rel-"+dimId);
    if(liChild!=undefined)
        bussRelatedTable.removeChild(liChild);
    var divChild=document.getElementById("dimDiv-"+dimId);
    dimensionDrag.removeChild(divChild);
    for(i=0;i<dimensionIdArr.length;i++){
        if(dimensionIdArr[i]==dimId)
            dimensionIdArr.splice(i, 1);
    }

}

function removeTable(tableId){
    var flag=false;
    for(i=0;i<dimFactArray.length;i++){
        if(dimFactArray[i]==tableId){
            dimFactArray.splice(i, 1);
            flag=true;
        }
    }
    if(flag){
        var draggedFacts=document.getElementById("draggedFacts");
        var divChild=document.getElementById("mainDiv-"+tableId);
        draggedFacts.removeChild(divChild);
    }
}
/*function addBussTables(){
    alert(bussTabIdArray.toString());
    var group=grpId.split("-");
    $.ajax({
        url: 'businesGroup.do?action=saveData&tableIds='+bussTabIdArray.toString()+'&groupId=1',
        success: function(data) {
            if(data=="success")
                addBusstablesToGrp();

        }

    });
}
*/
function addBussTables(){
    var group=grpId.split("-");
    // alert('businesGroup.do?action=saveData&tableIds='+bussTabIdArray.toString()+'&groupId='+group[1])
    $.ajax({
        url: 'businesGroup.do?action=saveData&tableIds='+bussTabIdArray.toString()+'&groupId='+group[1],
        success: function(data) {
            if(data!="success")
            { // alert('call bustables')
                addBusstablesToGrp(data,group[1]);

            }
            hideBussTabsArea();
            clearAllTables();
            //alert('hi');
            window.location.reload(true);
        }

    });
}
function addDimensions(){
    //alert(dimensionIdArr.toString())
    // alert(dimFactArray.toString())
    var group=grpId.split("-");
    //    alert("dimFactArray--"+dimFactArray.toString())
    //alert(group[1].toString())
    //'businesGroup.do?action=saveDimensions&dimIds='+bussTabIdArray.toString()+'&groupId='+group[1]+'&factIds='+dimFactArray.toString(
    // alert('businesGroup.do?action=saveDimensions&dimIds='+dimensionIdArr.toString()+'&groupId='+group[1]+'&factIds='+dimFactArray.toString())
   $("#loadingmetadata").show();
    $.ajax({
        url: 'businesGroup.do?action=saveDimensions&dimIds='+dimensionIdArr.toString()+'&groupId='+group[1]+'&factIds='+dimFactArray.toString(),

        success: function(data) {
            if(data=="success"){
                alert('Dimensions Added Successfully.')
                $("#loadingmetadata").hide();
                window.location.reload(true);
                hideDimensionLayer();
                removeAllDims();
                checkbusinessgrp();
            }
            else {
                alert('Error! Dimensions not added Successfully ')
                $("#loadingmetadata").hide();
            }

        }

    });
}

function removeFactTable(tableId){
    var flag=false;
    for(i=0;i<bussTabIdArray.length;i++){
        if(bussTabIdArray[i]==tableId){
            bussTabIdArray.splice(i,1);
            bussTabNameArray.splice(i,1);
            bussTableNamesDrag=bussTabNameArray.toString()
            flag=true;
        }
    }
    if(flag){
        var divDragTables=document.getElementById("divDraggableTables");
        var divChild=document.getElementById("mainDiv-"+tableId);
        divDragTables.removeChild(divChild);
    }
}

function groupDesc(){
    var $grpName=$("#grpName");
    var $grpDesc=$("#grpDesc");
    $grpDesc.val($grpName.val());
}


function showDimensionLayer(){
    var ob=document.getElementById("dbDimensions");
    ob.style.visibility="visible";
    ob.style.display="table-cell";
    var ob1=document.getElementById("addDbDims");
    ob1.style.visibility="visible";
    ob1.style.display="table-cell";
    var ob2=document.getElementById("relDBTables");
    ob2.style.visibility="visible";
    ob2.style.display="table-cell";
}
function hideDimensionLayer(){
    var ob=document.getElementById("dbDimensions");
    ob.style.visibility="hidden";
    ob.style.display="none";
    var ob1=document.getElementById("addDbDims");
    ob1.style.visibility="hidden";
    ob1.style.display="none";
    var ob2=document.getElementById("relDBTables");
    ob2.style.visibility="hidden";
    ob2.style.display="none";
}
function hideRelated()
{

    var ob=document.getElementById("relatedTablesPanel");
    ob.style.visibility="hidden";
    ob.style.display="none";
    ob.style.display="hideTableCell";
}
function hideEdit()
{

    var ob=document.getElementById("editRelationPanel");
    ob.style.visibility="hidden";
    ob.style.display="none";
    ob.style.display="hideTableCell";

}

function hideTarget()
{

    var ob=document.getElementById("TargetMeasuresDisp");
    ob.style.visibility="hidden";
    ob.style.display="none";
    ob.style.display="hideTableCell";
    var ob4=document.getElementById("TargetMeasuresDisp");
    ob4.style.visibility="hidden";
    ob4.style.display="hideTableCell"; //dargTargetMeasuresPanel
    var ob3=document.getElementById("dargTargetMeasuresPanel");
    ob3.style.visibility="hidden"; //hidden
    ob3.style.display="hideTableCell";// hideTableCell
}
function hideDragTarget()
{

    var ob=document.getElementById("dargTargetMeasuresPanel");
    ob.style.visibility="hidden";
    ob.style.display="none";
    ob.style.display="hideTableCell";
}


function hideAddSrcTblLayer(){
    document.getElementById("addTabsSrc-1").style.visibility="hidden";
    document.getElementById("addTabsSrc-1").style.display="none"
    document.getElementById("addTabsSrc-2").style.visibility="hidden";
    document.getElementById("addTabsSrc-2").style.display="none"
    document.getElementById("addTabsSrc-3").style.visibility="hidden";
    document.getElementById("addTabsSrc-3").style.display="none"
}
function hideRelatedTablesPanel()
{
    document.getElementById("relatedTablesPanel").style.visibility="hidden";
    document.getElementById("relatedTablesPanel").style.display="none"
    document.getElementById("editRelationPanel").style.visibility="hidden";
    document.getElementById("editRelationPanel").style.display="none"
}
function addTablesToSrc(tabGrpId,bussTabId){
    hideDimensionLayer();
    hideBussTabsArea();

    //added by uday
    hideRelatedTablesPanel();

    document.getElementById("addTabsSrc-1").style.visibility="visible";
    document.getElementById("addTabsSrc-1").style.display="table-cell"
    document.getElementById("addTabsSrc-2").style.visibility="visible";
    document.getElementById("addTabsSrc-2").style.display="table-cell"
    document.getElementById("addTabsSrc-3").style.visibility="visible";
    document.getElementById("addTabsSrc-3").style.display="table-cell"

    var tempbussId=bussTabId.split("-");
    var temp=tabGrpId.split("-");
    //  alert('in addTables To Src---'+'businesGroup.do?action=addTabsToSrc&tableId='+temp[1]+'&groupId='+temp[2]+'&bussTabId='+tempbussId[1]);
    $.ajax({
        url: 'businesGroup.do?action=addTabsToSrc&tableId='+temp[1]+'&groupId='+temp[2]+'&bussTabId='+tempbussId[1],
        success: function(data) {
            $("#addTabsToSrcTree").html("");
            var str= "<li style='background-image:url('images/treeViewImages/plus.gif')'><span>Tables</span><ul id='newTablesUL'>";
            str=str+data+"</ul></li>";
            var branches = $(str).appendTo("#addTabsToSrcTree");
            $("#addTabsToSrcTree").treeview({
                add: branches
            });

            var $dimensions=$(".dragTableName");
            $dimensions.draggable({
                helper:"clone",
                effect:["", "fade"]
            });
        /* var parentUL=document.getElementById("newTablesUL");
            var li=parentUL.getElementsByTagName("li");
            var span=li[0].getElementsByTagName("span");
            addDragTableToScr($(span[0]),"noDel");*/
        }//End of Success

    });

}

function addDragTableToScr(spanObj,type){
    var spanId=spanObj.attr("id");
    var temp=spanId.split("-");
    var tableId=temp[1];
    var draggedSrcTabs=document.getElementById("draggedSrcTabs");

    var tableName=spanObj.html();
    var ulObj=document.getElementById("srcTabUL-"+temp[1]);
    var cols=ulObj.getElementsByTagName("li");
    var str="<h3 align='left' style='border:0px' class='ui-accordion-header ui-helper-reset ui-state-default ui-corner-all' role='tab' aria-expanded='true' tabindex='0'>";
    // str=str+"<table><tr>";
    if(type=="canDel"){
        str=str+"<img title='Remove Table' onclick=\"javascript:removeSrcDragTable('"+tableId+"')\" class='ui-icon ui-icon-close'/>";
        relTableIds.push(tableId);
        relTableNames.push(tableName);

    }
    else{
        draggedSrcTabs.innerHTML="";
    }
    str=str+"<a title='Click To See Columns' href=\"javascript:showMySrcDiv('"+tableId+"')\" style='color: white; text-decoration: none;'>"+tableName+"</a></h3>"
    var parentDiv=document.createElement("li");
    parentDiv.id="srcTab-"+type+"-"+tableId;
    parentDiv.className="ui-state-default draggedDivs ui-corner-all"
    parentDiv.innerHTML=parentDiv.innerHTML+str;

    var div=document.createElement("div");
    div.id="tabSrcDet-"+tableId;

    div.className="myDraggedDiv";
    var p=document.createElement("p");
    p.id="para-"+tableId;
    for(i=0;i<cols.length;i++){

        var colspan=cols[i].getElementsByTagName("span");
        var tempId=(colspan[0].id).split("-");
        var span=document.createElement("span");
        span.className="srcTableColumns";
        var xy=tempId[1];
        span.id=""+xy+"-"+tableName;
        span.innerHTML=colspan[0].innerHTML+"<br>";
        p.appendChild(span);
    //str=str+"<span class='tableColumns'>"+sp[1]+"</span><br>";

    }
    div.appendChild(p)
    parentDiv.appendChild(div);
    draggedSrcTabs.appendChild(parentDiv);
    $(".srcTableColumns").draggable(
    {
        helper:"clone",
        effect:["", "fade"]
    }
    );

}


function showMySrcDiv(tableId){
    var mainDivID=document.getElementById("tabSrcDet-"+tableId);
    var $div=$(mainDivID);
    var options = {};
    $div.toggle("explode",options,500);
    $test=$(".srcTableColumns");
    //alert($test.length);
    $test.hover(
        function(){
            this.style.color="red";
        },
        function(){
            this.style.color="black";
        }

        );
}


function removeSrcDragTable(tableId){
    var draggedSrcTabs=document.getElementById("draggedSrcTabs");
    var child=document.getElementById("srcTab-canDel-"+tableId);
    draggedSrcTabs.removeChild(child);
    var i=0;

    for(i=0;i<relTableIds.length;i++){
        if(relTableIds[i]==tableId){
            relTableIds.splice(i,1);
            relTableNames.splice(i,1);
            break;
        }
    }

}



function createSrcRelation(colVal,relCode){
    var relationTable=document.getElementById("srcDragColRelTab");
    var x=relationTable.rows.length
    var tabRows=relationTable.rows;
    var y=colVal.split(".");
    var srcRelFlag=false;
    if(x==0){
        insertIntoNewRow(colVal,relCode);
        return true;
    }
    else{
        if(srcRowId!=0){
            x=srcRowId;
        }
        var cells=tabRows[x-1].cells;
        if(cells[2].innerHTML=="&nbsp;"){
            var tempVal=cells[0].innerHTML;
            var tabName=tempVal.split(".");
            var temprel=tabName[0]+"-"+y[0];
            var revTempRel=y[0]+"-"+tabName[0];
            var restString=restRelArr.toString();
            //alert(restRelArr.toString()+"****"+temprel)
            //alert(restRelArr.toString().match(temprel))
            //alert(restRelArr.toString().match(revTempRel))
            if((restString.match(temprel)==null) && (restString.match(revTempRel)==null))
            {

                if(tabName[0]!=y[0]){

                    //checkCircularJoin(temprel);
                    cells[2].innerHTML=colVal;
                    cells[2].onclick=function(){
                        editThisCell(this);
                    }
                    cells[2].id=relCode;
                    var tempRelStr=relationSrcArray.toString();
                    if(tempRelStr.match(temprel)==null){
                        relationSrcArray.push(temprel);
                        buildRestRelations();
                    }
                }
                else
                    alert("Invalid Entry");
                return true;
            }
            else
            {
                if(noAutoSrcRelSugg)
                    alert("Invalid RelationShip");
                deleteSrcSingleRel(x-1,"noBulid");
                return true;
            }
        }

        else{
            var selctBox=cells[1].getElementsByTagName("select");

            if(selctBox[0].value=="BETWEEN"){
                if(cells[4].innerHTML=="&nbsp;"){
                    var tempValue=cells[2].innerHTML;
                    var tempTableName=tempValue.split(".");
                    if(tempTableName[0]==y[0])
                    {

                        cells[4].innerHTML=colVal;
                        cells[4].onclick=function(){
                            editThisCell(this);
                        }
                        cells[4].id=relCode;
                        srcRowId=0;
                    }
                    else
                        alert("Invalid Entry");
                    return true;
                }
                else
                    srcRelFlag=true;
            }
            else
                srcRelFlag=true;
        }
    }
    if(srcRelFlag)
        insertIntoNewRow(colVal,relCode);

}


//start by uday
function addColumnRelation(names,ids)
{
    var tableObj = document.getElementById("showRelatedTableColumnsRelation");
    //alert(tableObj)
    var x=tableObj.rows.length;
    var tabRows=tableObj.rows;
    //alert("existed rows are::: "+x)
    var strNames = names.split(".");
    var strIds = ids.split(".");

    if(x==0)
    {
        insertNewRelation(names,ids);
    }
    else
    {
        var cells;
        var flag = true;
        for(var q=0;q<tabRows.length;q++)
        {
            //alert("in forloop")
            var getCells = tabRows[q].cells;
            var temp = getCells[1].getElementsByTagName("select")[0].value;
            //alert("temp is:: "+temp)
            if(temp=="BETWEEN" && getCells[4].innerHTML=="&nbsp;")
            {
                x = q;
                flag = false;
            }
        }
        //alert("x is:: "+x)
        if(flag==false)
        {
            cells = tabRows[x].cells;
        }
        else
        {
            cells = tabRows[x-1].cells;
        }
        if(cells[2].innerHTML=="&nbsp;")
        {
            var tempVal=cells[0].innerHTML;
            var tabName=tempVal.split(".");
            //alert("tabName[0] is:: "+tabName[0]+" and strNames[0] is:: "+strNames[0])
            if(tabName[0]!=strNames[0])
            {
                if(tabName[1]==strNames[1])
                {
                    //checkCircularJoin(temprel);
                    cells[2].innerHTML=names;
                    cells[2].id=ids;
                    cells[2].onclick=function()
                    {
                        editThisCell(this);
                    }
                }
                else
                {
                    alert("currently circular join is not supported")
                }
            }
            else
            {
                alert("Invalid Entry");
            }
        }
        else
        {
            var selctBox=cells[1].getElementsByTagName("select");
            if(selctBox[0].value=="BETWEEN")
            {
                if(cells[4].innerHTML=="&nbsp;")
                {
                    var tempValue=cells[2].innerHTML;
                    var relIds = cells[2].id;
                    var tempTableName=tempValue.split(".");
                    if(tempTableName[0]==strNames[0])
                    {
                        cells[4].innerHTML=names;
                        cells[4].id=relIds;
                        cells[4].onclick=function(){
                            editThisCell(this);
                        }
                    }
                    else
                    {
                        alert("Invalid Entry");
                    }
                }
                else
                {
                    insertNewRelation(names,ids);
                }
            }
            else
            {
                //alert("final else block")
                insertNewRelation(names,ids);
            }
        }
    }
}
function insertNewRelation(names,ids)
{
    //alert("names are:: "+names+" and idsa are:: "+ids)
    //alert("inse4rt")
    var tableObj = document.getElementById("showRelatedTableColumnsRelation");
    //alert(tableObj)
    var x=tableObj.rows.length;
    var tabRows=tableObj.rows;
    var row = tableObj.insertRow(x);
    row.id="relationship:"+x;

    var cell0= row.insertCell(0);
    cell0.style.border="0.5px solid black";
    cell0.className="ui-corner-all";
    cell0.style.width="20%";
    cell0.innerHTML=names;
    cell0.id=ids;
    cell0.onclick=function(){
        editThisCell(this);
    }

    var cell1= row.insertCell(1);
    cell1.id="sel:"+x;
    cell1.style.width="10%";
    cell1.style.border="0.5px solid black";
    cell1.className="ui-corner-all";
    var xyz=createSelectBoxForSrc();
    cell1.appendChild(xyz);
    xyz.style.width="80px";

    var cell2= row.insertCell(2);
    cell2.style.border="0.5px solid black";
    cell2.style.width="20%";
    cell2.className="ui-corner-all";
    cell2.innerHTML="&nbsp;"

    var cell3= row.insertCell(3);
    cell3.style.width="2%";
    cell3.innerHTML="&nbsp;&nbsp;";
    cell3.style.visibility="hidden";
    cell3.style.display="none";
    cell3.className="ui-corner-all";

    var cell4= row.insertCell(4);
    cell4.style.border="0.5px solid black";
    cell4.style.width="20%";
    cell4.innerHTML="&nbsp;&nbsp;"
    cell4.style.visibility="hidden";
    cell4.style.display="none"
    cell4.className="ui-corner-all";

    var cell5= row.insertCell(5);
    cell5.id="sel:"+x;
    cell5.style.width="10%";
    cell5.style.border="0.5px solid black";
    cell5.className="ui-corner-all";
    var joinSel1=createSrcJoinSelectBox();
    cell5.appendChild(joinSel1);
    joinSel1.style.width="80px";

    var cell6= row.insertCell(6);
    cell6.style.width="1%";
    cell6.style.cursor="pointer";
    cell6.id=""+x;
    cell6.innerHTML="<img class='ui-icon ui-icon-close' title='Delete' alt='Delete'> ";
    cell6.onclick=function(){
        deleteColumnRelation(this);
    }
}
//end by uday
function insertIntoNewRow(colVal,relCode){
    var relationTable=document.getElementById("srcDragColRelTab");
    var x=relationTable.rows.length
    var tabRows=relationTable.rows;
    var row = relationTable.insertRow(x);
    row.id="mySrcRow-"+x;
    var cell0= row.insertCell(0);
    cell0.style.border="0.5px solid black";
    cell0.className="ui-corner-all";
    cell0.style.width="20%";
    cell0.innerHTML=colVal;
    cell0.id=relCode;
    cell0.onclick=function(){
        editThisCell(this);
    }
    var cell1= row.insertCell(1);
    cell1.id="sel-"+x;
    cell1.style.width="10%";
    cell1.style.border="0.5px solid black";
    cell1.className="ui-corner-all";
    var xyz=createSelectBoxForSrc();
    cell1.appendChild(xyz);
    xyz.style.width="80px";
    var cell2= row.insertCell(2);
    cell2.style.border="0.5px solid black";
    cell2.style.width="20%";
    cell2.className="ui-corner-all";
    cell2.innerHTML="&nbsp;"
    var cell3= row.insertCell(3);
    cell3.style.width="2%";
    cell3.innerHTML="&nbsp;&nbsp;";
    cell3.style.visibility="hidden";
    cell3.style.display="none";
    cell3.className="ui-corner-all";
    var cell4= row.insertCell(4);
    cell4.style.border="0.5px solid black";
    cell4.style.width="20%";
    cell4.innerHTML="&nbsp;&nbsp;"
    cell4.style.visibility="hidden";
    cell4.style.display="none"
    cell4.className="ui-corner-all";

    var cell5= row.insertCell(5);
    cell5.id="sel-"+x;
    cell5.style.width="10%";
    cell5.style.border="0.5px solid black";
    cell5.className="ui-corner-all";
    var joinSel1=createSrcJoinSelectBox();
    cell5.appendChild(joinSel1);
    joinSel1.style.width="80px";

    var cell6= row.insertCell(6);
    cell6.style.width="1%";
    cell6.style.cursor="pointer";
    cell6.id=""+x;
    cell6.innerHTML="<img class='ui-icon ui-icon-close' title='Delete' alt='Delete'> ";
    cell6.onclick=function(){
        deleteSrcSingleRel(this,"Build");
    }
/*  var cell7= row.insertCell(7);
    cell7.style.width="1%";
    cell7.style.cursor="pointer";
    cell7.id="create-"+x;
    cell7.innerHTML="<img class='ui-icon ui-icon-pencil' title='Edit' alt='Delete'> ";
    cell7.onclick=function(){
        createDupRow(this);
    }*/


}
//start by uday
function deleteColumnRelation(index)
{
    //alert("delete")
    var relationTable=document.getElementById("showRelatedTableColumnsRelation");
    var tabRow=relationTable.rows;
    //alert(index.id)
    relationTable.deleteRow(index.id);

}
function Val()
{
    var relationTable=document.getElementById("showRelatedTableColumnsRelation");
    var tabRow=relationTable.rows.length;
    var trObjs = relationTable.getElementsByTagName("Tr");
    // alert('v '+v);
    // alert("rows are:: "+tabRow);
    var values="";
    for(var i=0;i<tabRow;i++)
    {
        var tt =trObjs[i].getAttribute("id");
        if(tt!=null){
            if(i==v){
                values=values+","+tt;
                g=g+","+tt;
            }
        }
    }
// alert('values  '+values);
}
//end by uday
function editThisCell(cellObj){
    // alert(cellObj.innerHTML);
    var temp=cellObj.innerHTML;
    var txt=document.createElement("input");
    txt.type="text"
    txt.value=temp;
    txt.onblur=function(){
        changeCellVal(this);
    }
    txt.style.width = "98.1%"
    txt.claasName="ui-corner-all";
    cellObj.innerHTML="";
    cellObj.appendChild(txt);
    txt.focus();

}
function changeCellVal(txtObj){
    var cellObj=txtObj.parentNode;
    cellObj.innerHTML="";
    cellObj.innerHTML=txtObj.value;
}


function buildRestRelations(){
    restRelArr.length=0;
    for(i=0;i<relationSrcArray.length;i++){
        var temp=relationSrcArray[i].split("-");
        for(j=i+1;j<relationSrcArray.length;j++){

            var temp1=relationSrcArray[j].split("-");
            if(temp[0].match(temp1[0])){
                if(temp[1]!=temp1[1])
                    restRelArr.push(temp[1]+"-"+temp1[1])
            }
            else if(temp[0].match(temp1[1])){
                if(temp[1]!=temp1[0])
                    restRelArr.push(temp[1]+"-"+temp1[0])
            }
            else if(temp[1].match(temp1[0])){
                if(temp[0]!=temp1[1])
                    restRelArr.push(temp[0]+"-"+temp1[1])
            }
            else if(temp[1].match(temp1[1])){
                if(temp[0]!=temp1[0])
                    restRelArr.push(temp[0]+"-"+temp1[0])
            }

        }
    }
//  alert(restRelArr);
}
function checkCircularJoin(relation){

}
function createSelectBoxForSrc(){

    var weightElement = document.createElement("select");
    weightElement.name = "selectBox";
    weightElement.id = "selectBox";
    weightElement.onchange=function(){
        checkBetweenForSrc(this);
    }
    var optn = document.createElement("OPTION");
    optn.text = "=";
    optn.value = "=";
    weightElement.options.add(optn);
    var optn1 = document.createElement("OPTION");
    optn1.text = "<";
    optn1.value = "<";
    weightElement.options.add(optn1);
    var optn2 = document.createElement("OPTION");
    optn2.text = "<=";
    optn2.value = "<=";
    weightElement.options.add(optn2);
    var optn3 = document.createElement("OPTION");
    optn3.text = ">";
    optn3.value = ">";
    weightElement.options.add(optn3);
    var optn4 = document.createElement("OPTION");
    optn4.text = ">=";
    optn4.value = ">=";
    weightElement.options.add(optn4);
    var optn5 = document.createElement("OPTION");
    optn5.text = "BETWEEN";
    optn5.value = "BETWEEN";
    weightElement.options.add(optn5);
    return weightElement;
}



function checkBetweenForSrc(selObject){
    var rowObj=(selObject.parentNode.parentNode);
    var cells=rowObj.cells;
    if(selObject.value=="BETWEEN"){
        cells[3].style.visibility="visible";
        cells[3].style.display="table-cell"
        cells[3].innerHTML=" AND "
        cells[3].style.width="2%"
        cells[4].style.visibility="visible";
        cells[4].style.display="table-cell";
        cells[4].style.width="20%"
        cells[4].innerHTML="&nbsp;"
        var x=rowObj.id.split("-");
        srcRowId=parseInt(x[1])+1;
    }
    else{
        cells[3].style.visibility="hidden";
        cells[3].style.display="none"
        cells[3].innerHTML="&nbsp;&nbsp;"
        cells[4].style.visibility="hidden";
        cells[4].style.display="none";
        cells[4].style.width="20%"
        cells[4].innerHTML="&nbsp;&nbsp;"
    }
}

function createSrcJoinSelectBox(){
    var weightElement = document.createElement("select");
    weightElement.name = "joinSelectBox";
    weightElement.id = "joinSelectBox";
    weightElement.onchange=function(){
        changeSrcRel(this);
    }
    var optn = document.createElement("OPTION");
    optn.text = "Equi Join";
    optn.value = "equijoin";
    weightElement.options.add(optn);
    var optn1 = document.createElement("OPTION");
    optn1.text = "Left Outer";
    optn1.value = "leftouter";
    weightElement.options.add(optn1);
    var optn2 = document.createElement("OPTION");
    optn2.text = "Right Outer";
    optn2.value = "rightouter";
    weightElement.options.add(optn2);
    return weightElement;
}
function changeSrcRel(obj){
    var rowObj=obj.parentNode.parentNode;
    var cellObj=rowObj.cells;
    var selBox=cellObj[1].getElementsByTagName("select")
    var flag=false;
    if(selBox[0].value=="BETWEEN"){
        flag=true;
    }
    if(obj.value=="leftouter"){
        var t1=((cellObj[0].innerHTML).split("<br>")).join("");
        cellObj[0].innerHTML=t1+"(+)"
        cellObj[2].innerHTML=(cellObj[2].innerHTML).replace("(+)","");
        cellObj[4].innerHTML=(cellObj[4].innerHTML).replace("(+)","");
    }
    else if(obj.value=="rightouter"){
        var t2=((cellObj[2].innerHTML).split("<br>")).join("");
        cellObj[2].innerHTML=t2+"(+)"
        cellObj[0].innerHTML=(cellObj[0].innerHTML).replace("(+)","");
        if(flag)
            if(cellObj[4].innerHTML!="&nbsp;"){
                var t3=((cellObj[4].innerHTML).split("<br>")).join("");
                cellObj[4].innerHTML=t3+"(+)"


            }

    }
    else if(obj.value=="equijoin"){
        cellObj[0].innerHTML=(cellObj[0].innerHTML).replace("(+)","");
        cellObj[2].innerHTML=(cellObj[2].innerHTML).replace("(+)","");
        cellObj[4].innerHTML=(cellObj[4].innerHTML).replace("(+)","");
    }
}

function deleteSrcSingleRel(index,type){
    var relationTable=document.getElementById("srcDragColRelTab");
    var tabRow=relationTable.rows;
    if(type.match("Build")!=null){
        for(k=0;k<tabRow.length;k++){
            var cell=tabRow[k].cells;
            if(cell[6].id==index.id){
                break;
            }
        }
        relationTable.deleteRow(k);
        buildRelationArray();
        buildRestRelations();
    }
    else{
        relationTable.deleteRow(index);
    }

}

function buildRelationArray(){
    var relationTable=document.getElementById("srcDragColRelTab");
    relationSrcArray.length=0;
    var tabRows=relationTable.rows;
    for(i=0;i<tabRows.length;i++){
        var cells=tabRows[i].cells;
        var cellZero=(cells[0].innerHTML).split(".");
        var cellTwo=(cells[2].innerHTML).split(".");
        relationSrcArray.push(cellZero[0]+"-"+cellTwo[0]);

    }
}

function checkDuplicateTabEntry(dragTableId){
    var temp=dragTableId.split("-");
    var checkId="srcTab-canDel-"+temp[1];
    var draggedSrcTabs=document.getElementById("draggedSrcTabs");
    var childs=draggedSrcTabs.getElementsByTagName("div");
    for(i=0;i<childs.length;i++){
        if(checkId.match(childs[i].id)!=null){
            alert("Duplicate Entry");
            return false;
        }

    }
    return true;
}

function autoSuggSrcRls(type){
    var draggedSrcTabs=document.getElementById("draggedSrcTabs");
    var childs=draggedSrcTabs.getElementsByTagName("div");
    var rel=true;
    var ab=0;
    var bc=0;
    var str="";
    if(childs.length>1){
        var srcCols=$(".srcTableColumns");
        for(ab=0;ab<srcCols.length;ab++){
            for(bc=ab+1;bc<srcCols.length && bc!=ab;bc++){
                // alert(bc+"**"+ab)
                if((srcCols.get(ab).innerHTML)==(srcCols.get(bc).innerHTML)){
                    if((srcCols.get(ab).id)!=(srcCols.get(bc).id)){
                        rel=false;
                        noAutoSrcRelSugg=false;
                        var id=(srcCols.get(ab).id).split("-");
                        str=((srcCols.get(ab).innerHTML).split("<br>")).join("");
                        var p=((srcCols.get(ab).parentNode).id).split("-");
                        var relCode=p[1]+"."+id[0];
                        createSrcRelation(id[1]+"."+(str),relCode);
                        var id2=(srcCols.get(bc).id).split("-");
                        str=((srcCols.get(bc).innerHTML).split("<br>")).join("");
                        var p1=((srcCols.get(bc).parentNode).id).split("-");
                        var relCode1=p1[1]+"."+id2[0];
                        createSrcRelation(id2[1]+"."+(str),relCode1);

                    }
                }
            }
        }
    }

    noAutoSrcRelSugg=true;
}

//start by uday
//modified by susheela start 04-12-09
var allSelectTypes="";

function updateRelatedTablesRelations()
{
    //alert("update")
    var relationTable=document.getElementById("showRelatedTableColumnsRelation");
    var tabRows=relationTable.rows;
    var i=0;
    var cells;
    var str="";
    var strIds="";
    var selectBox;
    var selectVal="";
    relationString="";
    relationCodedStr="";
    relTypeArr.length=0;

    for(i=0;i<tabRows.length;i++)
    {
        cells=tabRows[i].cells
        str+=cells[0].innerHTML+" ";
        strIds+=cells[0].id+" ";
        selectBox=cells[1].getElementsByTagName("select");
        selectVal=selectBox[0].value;
        allSelectTypes=allSelectTypes+","+selectVal;
        str+=selectVal+" ";
        str+=cells[2].innerHTML+" ";
        strIds+=selectVal+" ";
        strIds+=cells[2].id+" ";
        if(selectVal=="BETWEEN")
        {
            str+=" AND "+cells[4].innerHTML+" ";
            strIds+=" AND "+cells[4].id+" ";
        }
        var joinSelBox=cells[5].getElementsByTagName("select");
        relTypeArr.push(joinSelBox[0].value);
        if(i+1!=tabRows.length){
            //str+=" AND <br> "
            str+=" <br> "
            strIds+=" next "
        }
    }
    relationString=str;
    relationCodedStr=strIds;
    $("#commitRelationTxt").html(str);
    //alert("commitRelationTxt is:: "+str)
    //alert(relationCodedStr)
    relationCodedStr=(relationCodedStr.split(":")).join("-");
//alert(relationCodedStr)


}
function addSrcRelations(){

    var relationTable=document.getElementById("srcDragColRelTab");
    var tabRows=relationTable.rows;
    var i=0;
    var cells;
    var str="";
    var strIds="";
    var selectBox;
    var selectVal="";
    relationString="";
    relationCodedStr="";
    relTypeArr.length=0;
    for(i=0;i<tabRows.length;i++){
        cells=tabRows[i].cells
        str+=cells[0].innerHTML+" ";
        strIds+=cells[0].id+" ";
        selectBox=cells[1].getElementsByTagName("select");
        selectVal=selectBox[0].value;
        str+=selectVal+" ";
        str+=cells[2].innerHTML+" ";
        strIds+=selectVal+" ";
        strIds+=cells[2].id+" ";
        if(selectVal=="BETWEEN"){

            str+=" AND "+cells[4].innerHTML+" ";
            strIds+=" AND "+cells[4].id+" ";
        }
        var joinSelBox=cells[5].getElementsByTagName("select");
        relTypeArr.push(joinSelBox[0].value);
        if(i+1!=tabRows.length){
            str+=" AND <br>"
            strIds+=" next "
        }

    }
    relationString=str;
    relationCodedStr=strIds;
    $("#relationTxt").html(str);
    // relationString=(relationString.split("<br>")).join("");
    relationCodedStr=(relationCodedStr.split(".")).join("-");
// alert(relationCodedStr)
}

function saveSrcTblRelations(){
    // alert('hi'+srcTableGrpID.split("-"))
    var srcId=srcTableGrpID.split("-");
    var ulId="src-"+srcId[1]+"-"+srcId[2];
    var cnt=0;
    var liOBJ;
    var spanVal;
    var str="";
    var ulObj;
    var ulChilds;
    var ulObj1=document.getElementById(ulId);
    var liObjects=ulObj1.getElementsByTagName("li");
    var j=0;
    var tempStr="";
    for(j=0;j<liObjects.length;j++){
        var spanObj=liObjects[j].getElementsByTagName("span");
        tempStr +=spanObj[0].innerHTML+",";
    }
    for(cnt=0;cnt<relTableIds.length;cnt++){

        liOBJ=document.getElementById("srcTabLi-"+relTableIds[cnt]);
        spanVal=liOBJ.getElementsByTagName("span");

        if(tempStr.match(spanVal[0].innerHTML)==null){
            ulObj=liOBJ.getElementsByTagName("ul");
            ulChilds=ulObj[0].getElementsByTagName("li");
            str="";
            str+="<li class='closed'><span>"+spanVal[0].innerHTML+"</span>";
            str+="<ul>"
            var k=0;
            for(k=0;k<ulChilds.length;k++){
                var colSpan=ulChilds[k].getElementsByTagName("span");
                str+="<li><span>"+colSpan[0].innerHTML+"</span></li>"
            }
            str+="</ul></li>";
            var branches = $(str).appendTo("#"+ulId);
            $("#"+ulId).treeview({
                add: branches
            });

        }
    }
    // alert('hidelayer')
    hideAddSrcTblLayer();
    window.location.reload(true);
}


function saveSrcrelations(){
    var li=document.getElementById(srcTableGrpID);
    var sp=li.getElementsByTagName("span");
    var id=(sp[0].id).split("-");

    // relationString=relationString.split("<br>").join("");
    // alert('businesGroup.do?action=saveSrcTblRlts&bussTabId='+id[1]+'&relClauseStr='+relationString+'&relCodedClause='+relationCodedStr+'&noOfNodes='+relTableIds.length+'&relTableIds='+relTableIds.toString()+'&relTypes='+relTypeArr.toString())
    $.ajax({
        url: 'businesGroup.do?action=saveSrcTblRlts&bussTabId='+id[1]+'&relClauseStr='+relationString+'&relCodedClause='+relationCodedStr+'&noOfNodes='+relTableIds.length+'&relTableIds='+relTableIds.toString()+'&relTypes='+relTypeArr.toString(),
        success: function(data) {
            //  alert('save src relations')
            saveSrcTblRelations();
        }

    });

}

//added by uday modified by susheela start 04-12-09
function hideAllRelTdsOther()
{
    // alert('in alert ');
    document.getElementById("relatedTablesPanel").style.visibility = 'hidden';
    document.getElementById("relatedTablesPanel").style.display='none';
    document.getElementById("editRelationPanel").style.visibility = 'hidden';
    document.getElementById("editRelationPanel").style.display='none';
    window.location.reload(true);
}
function saveRelatedTablesRelations(relIds,relDetailsIds,commitRelationTxt,joinTypeStr)
{
    //alert("in finalmethod")
    var commitRelationText = commitRelationTxt;
    //alert("commitRelationText is 1:: "+commitRelationText)
    commitRelationText = commitRelationText.replace("+","@","gi");
    //alert("commitRelationText is 2:: "+commitRelationText)
    //alert("joinTypeStr is 2:: "+joinTypeStr)
    var relationIds = relIds;
    var relationDetailsIds = relDetailsIds;
    //Val();

    $.ajax({
        //url: "relatedTablesList.do?relTables=updateRelations&relationIds="+relationIds+"&relationDetailsIds="+relationDetailsIds+"&commitRelationTxt="+commitRelationText+"&joinTypeStr="+joinTypeStr,
        url: "relatedTablesList.do?relTables=updateRelations&relationIds="+relationIds+"&relationDetailsIds="+relationDetailsIds+"&commitRelationTxt="+commitRelationText+"&joinTypeStr="+joinTypeStr+'&allSelectTypes='+allSelectTypes,


        success: function(data) {
            alert("Relation Saved");
            hideAllRelTdsOther();
        //saveSrcTblRelations();
        }

    });

}
function checkCommitRelatedTableRelations()
{
    var commitRelationTxt = document.getElementById("commitRelationTxt").innerHTML;
    commitRelationTxt = commitRelationTxt.replace("&lt;","<","gi");
    commitRelationTxt = commitRelationTxt.replace("&gt;",">","gi");
    //alert("commitRelationTxt in fn is:: "+commitRelationTxt)
    var str=relationCodedStr.split("next");
    var relTblArr=new Array();
    var relTblDetailsArr=new Array();
    var i=0;

    for(i=0;i<str.length;i++){
        var temp=relTblArr.toString();
        var temp2 = relTblDetailsArr.toString();
        //alert("temp is:: "+temp+" and str is:: "+str[i]);

        if(str[i].match("<=")){
            var a=str[i].split("<=");
            var a1=a[0].split("-");
            var a2=a[1].split("-");
            a1[0]=(a1[0].split(' ')).join("");
            a2[0]=(a2[0].split(' ')).join("")
            a1[1]=(a1[1].split(' ')).join("");
            a2[1]=(a2[1].split(' ')).join("")
            if(temp.match(a1[0])==null)
                relTblArr.push(a1[0])
            if(temp.match(a2[0])==null)
                relTblArr.push(a2[0])
            if(temp2.match(a1[1])==null)
                relTblDetailsArr.push(a1[1])
            if(temp2.match(a2[1])==null)
                relTblDetailsArr.push(a2[1])
            temp=relTblArr.toString();
            temp2=relTblDetailsArr.toString();
        }
        else if(str[i].match(">=")){
            var b=str[i].split(">=");
            var b1=b[0].split("-");
            var b2=b[1].split("-");
            b1[0]=(b1[0].split(' ')).join("");
            b2[0]=(b2[0].split(' ')).join("");
            b1[1]=(b1[1].split(' ')).join("");
            b2[1]=(b2[1].split(' ')).join("")
            if(temp.match(b1[0])==null)
                relTblArr.push(b1[0])
            if(temp.match(b2[0])==null)
                relTblArr.push(b2[0])
            if(temp.match(b1[1])==null)
                relTblDetailsArr.push(b1[1])
            if(temp.match(b2[1])==null)
                relTblDetailsArr.push(b2[1])
            temp=relTblArr.toString();
            temp2=relTblDetailsArr.toString();
        }
        else if(str[i].match("<")){
            var c=str[i].split("<");
            var c1=c[0].split("-");
            var c2=c[1].split("-");
            c1[0]=(c1[0].split(' ')).join("");
            c2[0]=(c2[0].split(' ')).join("");
            c1[1]=(c1[1].split(' ')).join("");
            c2[1]=(c2[1].split(' ')).join("")
            if(temp.match(c1[0])==null)
                relTblArr.push(c1[0])
            if(temp.match(c2[0])==null)
                relTblArr.push(c2[0])
            if(temp.match(c1[1])==null)
                relTblDetailsArr.push(c1[1])
            if(temp.match(c2[1])==null)
                relTblDetailsArr.push(c2[1])
            temp=relTblArr.toString();
            temp2=relTblDetailsArr.toString();
        }
        else if(str[i].match(">")){
            var d=str[i].split(">");
            var d1=d[0].split("-");
            var d2=d[1].split("-");
            d1[0]=(d1[0].split(' ')).join("");
            d2[0]=(d2[0].split(' ')).join("");
            d1[1]=(d1[1].split(' ')).join("");
            d2[1]=(d2[1].split(' ')).join("")
            if(temp.match(d1[0])==null)
                relTblArr.push(d1[0])
            if(temp.match(d2[0])==null)
                relTblArr.push(d2[0])
            if(temp.match(d1[1])==null)
                relTblDetailsArr.push(d1[1])
            if(temp.match(d2[1])==null)
                relTblDetailsArr.push(d2[1])
            temp=relTblArr.toString();
            temp2=relTblDetailsArr.toString();
        }
        else if(str[i].match("=")){
            var e=str[i].split("=");
            var e1=e[0].split("-");
            var e2=e[1].split("-");
            e1[0]=(e1[0].split(' ')).join("");
            e2[0]=(e2[0].split(' ')).join("");
            e1[1]=(e1[1].split(' ')).join("");
            e2[1]=(e2[1].split(' ')).join("")
            if(temp.match(e1[0])==null)
                relTblArr.push(e1[0])
            if(temp.match(e2[0])==null)
                relTblArr.push(e2[0])
            if(temp.match(e1[1])==null)
                relTblDetailsArr.push(e1[1])
            if(temp.match(e2[1])==null)
                relTblDetailsArr.push(e2[1])
            temp=relTblArr.toString();
            temp2=relTblDetailsArr.toString();

        }
        else if(str[i].match("BETWEEN")){
            var f=str[i].split("BETWEEN");
            var f1=f[0].split("-");
            var xy=f[1].split("AND");
            var f2=xy[0].split("-");
            f1[0]=(f1[0].split(' ')).join("");
            f2[0]=(f2[0].split(' ')).join("");
            f1[1]=(f1[1].split(' ')).join("");
            f2[1]=(f2[1].split(' ')).join("")
            if(temp.match(f1[0])==null)
                relTblArr.push(f1[0])
            if(temp.match(f2[0])==null)
                relTblArr.push(f2[0])
            if(temp.match(f1[1])==null)
                relTblArr.push(f1[1])
            if(temp.match(f2[1])==null)
                relTblArr.push(f2[1])
            temp=relTblArr.toString();
            temp2=relTblDetailsArr.toString();
        }
    }

    var relIds=(relTblArr.toString());
    var relDetailsIds = relTblDetailsArr.toString()
    //alert("relIds length is4532:: "+relIds);
    //alert("relDetailsIds length is4532:: "+relDetailsIds);
    var relIdsStrArray = relIds.split(",");
    var relDetailsIdsArray = relDetailsIds.split(",");
    var newRelIds="";
    var newRelDetailsIds="";
    for(var z=0;z<relIdsStrArray.length;z++)
    {
        //alert("relIdsStrArray[z] is:: "+relIdsStrArray[z])
        if(relIdsStrArray[z] != '')
        {
            if((z%2)!=0)
            {
                if((z+1)%4==0)
                {
                    if(newRelIds == "")
                    {
                        newRelIds = relIdsStrArray[z];
                    }
                    else
                    {
                        newRelIds = newRelIds+","+relIdsStrArray[z]+":";
                    }
                }
                else
                {
                    if(newRelIds == "")
                    {
                        newRelIds = relIdsStrArray[z];
                    }
                    else
                    {
                        newRelIds = newRelIds+","+relIdsStrArray[z];
                    }
                }
            }
        }
    }

    /* for(var z=0;z<relIdsStrArray.length;z++)
    {
        //alert("relIdsStrArray[z] is:: "+relIdsStrArray[z])
        if(relIdsStrArray[z] != '')
        {
            if((z%2)!=0)
            {
                if(newRelIds == "")
                {
                    newRelIds = relIdsStrArray[z];
                }
                else
                {
                    newRelIds = newRelIds+","+relIdsStrArray[z];
                }
            }
        }
    } */
    for(var h=0;h<relDetailsIdsArray.length;h++)
    {
        //alert("relIdsStrArray[z] is:: "+relIdsStrArray[z])
        if(relDetailsIdsArray[h] != '')
        {
            if((h%2)!=0)
            {
                if(newRelDetailsIds == "")
                {
                    newRelDetailsIds = relDetailsIdsArray[h];
                }
                else
                {
                    newRelDetailsIds = newRelDetailsIds+","+relDetailsIdsArray[h];
                }
            }
        }
    }

    var joinTypeStr='';
    //alert('newRelIds  '+newRelIds)
    var relIdsStr = newRelIds.split(":");
    var relDetailsIdsStr = newRelDetailsIds.split(",");
    //alert(relIdsStr.length)
    for(var w=0;w<relIdsStr.length-1;w++)
    {
        var name = "sel:"+w;
        //alert(name)
        var joinSelObj = document.getElementsByName(name)[0];
        var joinType = joinSelObj.getElementsByTagName("select")[0].value;
        //alert("joinType is:: "+joinType)
        //alert("jointypeStr is:: "+joinTypeStr)
        if(joinTypeStr=='')
        {
            //alert('in if')
            joinTypeStr = joinType;
        }
        else
        {
            //alert("in else")
            joinTypeStr = joinTypeStr+","+joinType;
        //alert("joinTypeStr is:: "+joinTypeStr)
        }
    }

    saveRelatedTablesRelations(newRelIds,newRelDetailsIds,commitRelationTxt,joinTypeStr);
}

function checkCartesianTbl(){
    var str=relationCodedStr.split("next");
    var relTblArr=new Array();
    var i=0;

    for(i=0;i<str.length;i++){
        var temp=relTblArr.toString();
        if(str[i].match("<=")){
            var a=str[i].split("<=");
            var a1=a[0].split("-");
            var a2=a[1].split("-");
            a1[0]=(a1[0].split(' ')).join("");
            a2[0]=(a2[0].split(' ')).join("")
            if(temp.match(a1[0])==null)
                relTblArr.push(a1[0])
            if(temp.match(a2[0])==null)
                relTblArr.push(a2[0])
            temp=relTblArr.toString();
        }
        else if(str[i].match(">=")){
            var b=str[i].split(">=");
            var b1=b[0].split("-");
            var b2=b[1].split("-");
            b1[0]=(b1[0].split(' ')).join("");
            b2[0]=(b2[0].split(' ')).join("")
            if(temp.match(b1[0])==null)
                relTblArr.push(b1[0])
            if(temp.match(b2[0])==null)
                relTblArr.push(b2[0])
            temp=relTblArr.toString();
        }
        else if(str[i].match("<")){
            var c=str[i].split("<");
            var c1=c[0].split("-");
            var c2=c[1].split("-");
            c1[0]=(c1[0].split(' ')).join("");
            c2[0]=(c2[0].split(' ')).join("")
            if(temp.match(c1[0])==null)
                relTblArr.push(c1[0])
            if(temp.match(c2[0])==null)
                relTblArr.push(c2[0])
            temp=relTblArr.toString();
        }
        else if(str[i].match(">")){
            var d=str[i].split(">");
            var d1=d[0].split("-");
            var d2=d[1].split("-");
            d1[0]=(d1[0].split(' ')).join("");
            d2[0]=(d2[0].split(' ')).join("")
            if(temp.match(d1[0])==null)
                relTblArr.push(d1[0])
            if(temp.match(d2[0])==null)
                relTblArr.push(d2[0])
            temp=relTblArr.toString();
        }
        else if(str[i].match("=")){
            var e=str[i].split("=");
            var e1=e[0].split("-");
            var e2=e[1].split("-");
            e1[0]=(e1[0].split(' ')).join("");
            e2[0]=(e2[0].split(' ')).join("")
            if(temp.match(e1[0])==null)
                relTblArr.push(e1[0])
            if(temp.match(e2[0])==null)
                relTblArr.push(e2[0])
        }
        else if(str[i].match("BETWEEN")){
            var f=str[i].split("BETWEEN");
            var f1=f[0].split("-");
            var xy=f[1].split("AND");
            var f2=xy[0].split("-");
            f1[0]=(f1[0].split(' ')).join("");
            f2[0]=(f2[0].split(' ')).join("")
            if(temp.match(f1[0])==null)
                relTblArr.push(f1[0])
            if(temp.match(f2[0])==null)
                relTblArr.push(f2[0])
            temp=relTblArr.toString();
        }
    }
    // alert(relTblArr+"---"+relTableIds);
    var addedTbls=(relTblArr.toString())
    var k=0;
    for(k=0;k<relTableIds.length;k++){
        if(addedTbls.match(relTableIds[k])==null){
            $('#cartesianDialog').dialog('open');
            $("#cartesianTabs").html(relTableNames[k]+" Is Not Related And May Lead To Caresian Join");
            break;
        }
    }
    // alert('in check cart'+k)
    if(k==relTableIds.length){
        saveSrcrelations();
    }

}

function removeAllSrcTables(){
    var draggedSrcTabs=document.getElementById("draggedSrcTabs");
    relTableIds.length=0;
    relTableNames.length=0;
    removeAllRelations();
}

//added by uday
function removeAllRelatedTablesRelations()
{
    var tab=document.getElementById("showRelatedTableColumnsRelation");
    var len = (tab.rows).length
    for(var i=0;i<len;i++)
    {
        tab.deleteRow(i);
    }
}
function removeAllRelations(){
    var tab=document.getElementById("srcDragColRelTab");
    for(i=0;i<(tab.rows).length;i++){
        tab.deleteRow(0);
    }
}


function check(){
    alert(dimensionIdArr)
    alert(dimFactArray)
}



//These functions are written by praveen for building dynamic Busines Groups.

function getdimensions(busgrpid){

    busgrpid = busgrpid.substr(busgrpid.indexOf('-')+1,busgrpid.length);
    //    alert("getdimensions\t"+busgrpid)
    var dimsfor = "dimsfor-"+busgrpid;
    var chkdata = document.getElementById(dimsfor).innerHTML;
    if(chkdata == null || chkdata == "" || chkdata.length == 0){
        var branches1 = "";
        var str = "";
        $.ajax({
            url:'getAllBusinessGroups.do?method=builddimensions&grpid='+busgrpid,
            success:function(data)
            {
                $("#"+dimsfor).html("");
                str=data;
                branches1 = $(str).appendTo("#"+dimsfor);
                $("#"+dimsfor).treeview({
                    add: branches1
                });
                //code for building context menu
                $(".BgDimensionDiv").contextMenu({
                    menu: 'BgDimensionMenu',
                    leftButton: true
                }, function(action,el,pos) {
                    var val=el.attr("id");
                    var allV=val.split("~");
                    delGrpId=allV[0];
                    delDimId =allV[1];
                    contextMenuWork(action,el.parent("li"), pos);
                });

            }
        });
    }
    else{
        return false;
    }
}

function getBuckets(bucketid){

    //    alert(bucketid);

    var bktid =  bucketid.substr(bucketid.indexOf('-')+1,bucketid.length);
    var bktname = "bckt-"+bktid;
    //alert(bktname)
    var chkbkt = document.getElementById(bktname).innerHTML;
    if(chkbkt == null || chkbkt == "" || chkbkt.length == 0){
        var branches1 = "";
        var str = "";
        //alert("hello");
        $.ajax({
            url:'getAllBusinessGroups.do?method=buildbuckets&bktid='+bktid,
            success:function(data)
            {
                //alert(data);
                $("#"+bktname).html("");
                str=data;
                branches1 = $(str).appendTo("#"+bktname);
                $("#"+bktname).treeview({
                    add: branches1
                });
            }
        });
    }
    else{
        return false;
    }
}

function getFacts(factid){
    //    alert(factid);

    factid =  factid.substr(factid.indexOf('-')+1,factid.length);
    var factname = "facts-"+factid;
    //alert(factname)
    var chkfct = document.getElementById(factname).innerHTML;
    if(chkfct == null || chkfct == "" || chkfct.length == 0){
        var branches1 = "";
        var str = "";
        //alert("hello");
        $.ajax({
            url:'getAllBusinessGroups.do?method=buildfacts&factid='+factid,
            success:function(data)
            {
                //alert(data);
                $("#"+factname).html("");
                str=data;

                branches1 = $(str).appendTo("#"+factname);
                $("#"+factname).treeview({
                    add: branches1
                });
                $(".tableProperty").contextMenu({
                    menu: 'cTableProp',
                    leftButton: true
                }, function(action, el, pos) {
                    contextMenuWork3(action, el.parent("li"), pos);
                });
                $(".bucketMenu").contextMenu({
                    menu: 'grpbucket',
                    leftButton: true
                }, function(action, el, pos) {
                    contextMenubucket(action,el.parent("li"), pos);
                });
            }
        });
    }
    else{
        return false;
    }
}

function addTrgetMsr(trgtmsrid){
    // alert(trgtmsrid);

    trgtmsrid =  trgtmsrid.substr(trgtmsrid.indexOf('-')+1,trgtmsrid.length);
    var trgetname = "trgtmsr-"+trgtmsrid;
    //alert(trgetname)
    var chktrgt = document.getElementById(trgetname).innerHTML;
    if(chktrgt == null || chktrgt == "" || chktrgt.length == 0){
        var branches1 = "";
        var str = "";
        //alert("hello");
        $.ajax({
            url:'getAllBusinessGroups.do?method=buildtrgetmeausers&trgtmsrid='+trgtmsrid,
            success:function(data)
            {
                //alert(data);
                $("#"+trgetname).html("");
                str=data;
                branches1 = $(str).appendTo("#"+trgetname);
                $("#"+trgetname).treeview({
                    add: branches1
                });
            }
        });
    }
    else{
        return false;
    }
}

function getAllTables(alltablesid){

    //alert(alltablesid);

    alltablesid =  alltablesid.substr(alltablesid.indexOf('-')+1,alltablesid.length);
    var alltablename = "ul-"+alltablesid;
    //alert(alltablename )
    var chkalltables = document.getElementById(alltablename).innerHTML;
    if(chkalltables == null || chkalltables == "" || chkalltables.length == 0){
        var branches1 = "";
        var str = "";
        //alert("hello");
        $.ajax({
            url:'getAllBusinessGroups.do?method=buildAddtables&alltablesid='+alltablesid,
            success:function(data)
            {
                //alert(data);
                $("#"+alltablename).html("");
                str=data;
                branches1 = $(str).appendTo("#"+alltablename);
                $("#"+alltablename).treeview({
                    add: branches1
                });
                $(".openmenubusgrp").contextMenu(
                {
                    menu: 'myMenubusgrp',
                    leftButton: true
                }, function(action, el, pos) {
                    bustableId=$(el).parent().attr('id');
                    contextMenuWork(action, el.parent("li"), pos);

                });
                $(".addNewRelTabs").contextMenu({
                    menu: 'addTabsToSrc',
                    leftButton: true
                }, function(action, el, pos) {
                    addTabsToScr(action, el, pos);
                });
            }
        });
    }
    else{
        return false;
    }
}

function getbusroles(bussroleid)
{
    var bussrolename = "busroles-"+bussroleid;
    //    alert(bussrolename )
    var chkbusroles = document.getElementById(bussrolename).innerHTML;
    if(chkbusroles == null || chkbusroles == "" || chkbusroles.length == 0){
        var branches1 = "";
        var str = "";
        //alert("hello");
        $.ajax({
            url:'getAllBusinessGroups.do?method=buildbussroles&bussroleid='+bussroleid,
            success:function(data)
            {
                $("#"+bussrolename).html("");
                str=data;
                branches1 = $(str).appendTo("#"+bussrolename);
                $("#"+bussrolename).treeview({
                    add: branches1
                });
            }
        });
    }
    else{
        return false;
    }
}
function getGroupHierarchy(hierarghyid)
{
    roleName = "busgroup-"+hierarghyid;
    var chkbusroles = document.getElementById(roleName).innerHTML;
    if(chkbusroles == null || chkbusroles == "" || chkbusroles.length == 0){
        var branches1 = "";
        var str = "";
   $.ajax({
            url:'businessgroupeditaction.do?groupdetails=getGroups&hid='+hierarghyid,
            success:function(data)
            {
                $("#"+roleName).html("");
                str=data;
                branches1 = $(str).appendTo("#"+roleName);
                $("#"+roleName).treeview({
                    add: branches1
                });
                $(".grouphierarchyid").contextMenu({menu: 'grophierarchy',leftButton: true}, function(action, el, pos) {
                    contextEditGroupHierarchyMenu(action,el, pos);
                });
                $(".grouphierarchyid1").contextMenu({menu: 'groupsChildMenu',leftButton: true}, function(action, el, pos) {
                    contextEditChildHierarchyMenu(action,el, pos);
                });              
            }
   });
 }
}

function getChildId(id){    

            var childId=id.split(':')[0];
            var grpId = id.split(':')[1];
            var branches1 = "";
            var str = "";
            var selectedMeasures="";
            $.ajax({
               url:'businessgroupeditaction.do?groupdetails=getChildsId&id='+id,
            success:function(data)
            {                
             $("#"+childId+""+grpId).html("");
                str=data;                
               branches1 = $(str).appendTo("#"+childId+""+grpId); 
                $("#"+childId+""+grpId).treeview({
                    add: branches1
                });
                $(".grouphierarchyid1").contextMenu({menu: 'groupsChildMenu',leftButton: true}, function(action, el, pos) {
                    contextEditChildHierarchyMenu(action,el, pos);
                });
            }
   });
}
function getEditgroupvalues(id,id2)
{

 var frameObj=document.getElementById("grouphiid");
alert('frameObj-->'+frameObj)
var source="groupHierarchy1.jsp?perentcolid="+id;
alert('frameObj-->'+frameObj)
frameObj.src=source;
frameObj.style.display='block';
document.getElementById('fade').style.display='block';
}
function cancelgrouplist()
{
    alert('am enter')
 document.getElementById("grouphiid").style.display='none';
document.getElementById('fade').style.display='';
    
}
//function contextEditChildHierarchyMenu(action, el, pos)
//{
//    
//}
//function contextEditChildHierarchyMenu(action, el, pos) {
//
//    switch (action) {
//        case "addChilds":
//        {
//            var props = el.attr('id');       
//            var connId=document.getElementById("connId").value;
//            var tabId = props.split(',')[0];       
//            document.getElementById("groupHierarchyDialog").innerHTML = "<iframe width='100%' height='100%'  frameborder='0' src=groupHierarchy.jsp?connId="+connId+"&grpId="+props+"&tabId="+tabId+"></iframe>";
//            $('#groupHierarchyDialog').dialog('open');
//            break;
//        }
//    }
//}
                                             
//function getGroupHierarchy(hierarghyid)
//{
//
//    var bussrolename = "busgroup-"+hierarghyid;
//    //    alert(bussrolename )
//    var chkbusroles = document.getElementById(bussrolename).innerHTML;
//    if(chkbusroles == null || chkbusroles == "" || chkbusroles.length == 0){
//        var branches1 = "";
//        var str = "";
//        var selectedMeasures="";
//   $.ajax({
//            url:'businessgroupeditaction.do?groupdetails=getGroupHierarchy&hid='+hierarghyid,
//            success:function(data)
//            {
//                $("#"+bussrolename).html("");
//                str=data;
//                alert('str---'+str)
//                branches1 = $(str).appendTo("#"+bussrolename);
//                $("#"+bussrolename).treeview({
//                    add: branches1
//                });
//                $(".grouphierarchyid").contextMenu({
//                    menu: 'grophierarchy',
//                    leftButton: true
//                }, function(action, el, pos) {
//
//                    editcontextgrouphierarchy(action, el, pos);
//
////                     $("#grouphierarchy").children().each(
////                        function(index, value){
////                     var perentid=$(this).attr('id').replace("perentid","")
////                     alert('perentid-------------------'+perentid)
////                 $.ajax({
////              url:'editgrouphierarchy.do?&perentid='+perentid,
////               success:function(data)
////                {
////
////
////                        }
////                 });
////
////
////                        });
//                });
//            }
//   });
//
//}
//}

                                             

