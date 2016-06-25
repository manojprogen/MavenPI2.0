/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var rowId=0;
var count=0;
var tabName="";
var tableNamesDrag="";
var tabNameArr=new  Array();
var tabIdArr=new Array();
var columnArr=new Array();
var columnNameArr=new Array();
var relationArray=new Array();
var relWithId="";
var autoSuggestArr=new Array();
var autoSuggestFlag=false;
var tabDetailsArray=new Array();
var delTabId="";
var addRelArr=new Array();
var addRelStr="";
var restRelStr="";
var restRelArr=new Array();
var tempStr="";
var tempStrRev="";
var restrictTableRow=new Array();
var relationIdStr=""

$(function() {

    $("#accordion").accordion({


        });
    $("#accordion").sortable({
        containment:"parent"
    });
    $("#accordion").disableSelection();
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
                removeAllTabs();

            }
        }
    });
    $("#removeTable").dialog({
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
            'Remove Table': function() {
                $(this).dialog('close');
                var tabId=delTabId;
                count--;
                var accordion=document.getElementById("accordion");
                var div=document.getElementById("mainDiv-"+tabId);
                accordion.removeChild(div);
                for(i=0;i<tabNameArr.length;i++){
                    if(tabNameArr[i]==tabId){
                        tabNameArr.splice(i,1);
                        tabIdArr.splice(i,1);
                    }
                }
                tableNamesDrag=tabNameArr.toString();
                delTabId="";

            }
        }
    });

    $("#successMsg").dialog({
        bgiframe: true,
        autoOpen: false,
        modal: true,
        buttons: {
            Ok: function() {
                $(this).dialog('close');
            }
        }
    });
    $("#failureMsg").dialog({
        bgiframe: true,
        autoOpen: false,
        modal: true,
        buttons: {
            Ok: function() {
                $(this).dialog('close');
            }
        }
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

});




function getTableDetails(tabId,ulid){
//    alert("tabId\t"+tabId)
    var table=document.getElementById(ulid);
    var cols=table.getElementsByTagName("li");
    table=createTable(cols,tabId);
/*var str="";
    for(i=0;i<tabIdArr.length;i++){
        for(j=i+1;j<tabIdArr.length;j++){
            str=str+""+tabIdArr[i]+"-"+tabIdArr[j]+",";

        }
    }*/
//getSavedRelations();
}
function createTable(cols,tabId){
    // var h3=document.createElement("h3");
    // h3.className=""
    var accordion1=document.getElementById("accordion");
    var str="<h3 id='h3-"+tabId+"' align='left' style='border:0px;background-color:#8BC34A;' class='themeColor ui-accordion-header ui-helper-reset ui-state-default ui-corner-all' role='tab' aria-expanded='true' tabindex='0'>";
    // str=str+"<span class='ui-icon ui-icon-triangle-1-e'/>";
    str=str+"<a style='color:gray;' title='Click To See Columns' href=\"javascript:showMyDiv('tabDet-"+tabId+"')\">"+tabId+"</a><img onclick=removeTable('"+tabId+"') title='Remove Table' class='ui-icon ui-icon-close'></h3>";
    var accordion=document.createElement("div");
    //var newtabid=
    newtabid=tabId.replace("$","")
    newtabid=newtabid.replace("[","")
    newtabid=newtabid.replace("]","")
    //alert('af show my div'+newtabid)
    accordion.id="mainDiv-"+tabId;
    accordion.innerHTML=accordion.innerHTML+str;
    accordion.className="ui-state-default draggedDivs ui-corner-all"
    var div=document.createElement("div");
    div.id="tabDet-"+newtabid;
    div.style="color:gray";
    div.className="myDraggedDiv";
    var p=document.createElement("p");
    for(i=0;i<cols.length;i++){
        var spanCol=cols[i].getElementsByTagName("span");
        var sp=(cols[i].innerHTML).split(">");
        var img=cols[i].getElementsByTagName("img");
        var span=document.createElement("span");
        span.className="tableColumns";
        var xTemp=(img[0].id).substr(6,(img[0].id).length);
        var yTemp=xTemp.split(",");
        var xy=yTemp[0];
        // alert(xy);
        span.id=""+xy+"-"+tabId;

        if(tabDetailsArray[tabId]){


            var tb="";

            for(a=0;a<tabNameArr.length;a++){
                if(tabId==tabNameArr[a])
                   // alert('td'+tabIdArr[a])
                    tb=(tabIdArr[a]);
            }

            columnNameArr.push(tabId+"."+spanCol[0].innerHTML);

            columnArr.length=columnArr.length+1;

            columnArr[tabId+"."+spanCol[0].innerHTML]=tb+"-"+xy;
        }
        span.innerHTML=spanCol[0].innerHTML+"<br>";
        p.appendChild(span);
    //str=str+"<span class='tableColumns'>"+sp[1]+"</span><br>";

    }
    div.appendChild(p)
    accordion.appendChild(div);
    accordion1.appendChild(accordion);
    /*// str=str+"</p></div>"
                str=str+"</p>";
                str=str+"</div>";*/
    $(".tableColumns").draggable(
    {
        helper:"clone",
        effect:["", "fade"]
    }
    );


}
function showMyDiv(divId){
   // alert('in show my div'+divId)
    divIdnew=divId.replace("$","")
    divIdnew=divIdnew.replace("[","")
    divIdnew=divIdnew.replace("]","")
   // alert('af show my div'+divId)
    var $div=$("#"+divIdnew)
    var options = {};
    $div.toggle("explode",options,500);
    /* if((div.style.visibility)=="visible"){
        div.style.visibility="hidden";
        div.style.display="none";

    }
    else{
        div.style.visibility="visible";
        div.style.display="block";

    }*/
    $test=$(".tableColumns");
    //alert($test.length);
    $test.hover(
        function(){
            this.style.color="black";
        },
        function(){
            this.style.color="gray";
        }

        );

}

function buildRelationTable(ui){
    var tab=document.getElementById("relationTable");
    var noOfRows=tab.rows.length;
    var tbId=ui.draggable.attr("id").split("-");
    var flag=true;
    if(count>1){

        var x=createrelation(tbId[1]+"."+ui.draggable.html());

    }

    else{
        $('#warning').dialog('open');
        $("#alert").html("Please Select Atleast Two Tables \n To Create Relations");
    }

}
function restrictRel(){
    var zx=(addRelStr).split(",");

    for(zv=0;zv<zx.length;zv++){
        if(zx[zv]!="")
            var db=zx[zv].split("-");
        for(p=zv+1;p<zx.length;p++){
            if(zx[p]!=""){
                var bc=zx[p].split("-")
                if(db[0]==bc[0]){
                    if((restRelStr.match(db[1]+"-"+bc[1]))==null)
                        if(db[1]!=bc[1]){
                            restRelArr.push((db[1]+"-"+bc[1]))
                        }
                }
                else if(db[0]==bc[1])
                {

                    if((restRelStr.match(db[1]+"-"+bc[0]))==null)
                        if(db[1]!=bc[0]){
                            restRelArr.push((db[1]+"-"+bc[0]))
                        }
                }
                else if(db[1]==bc[0])
                {

                    if((restRelStr.match(db[0]+"-"+bc[1]))==null)
                        if(db[0]!=bc[1]){
                            restRelArr.push((db[0]+"-"+bc[1]))
                        }
                }
                else if(db[1]==bc[1]){
                    if((restRelStr.match(db[0]+"-"+bc[0]))==null)
                        if(db[0]!=bc[0]){
                            restRelArr.push((db[0]+"-"+bc[0]))
                        }
                }
            }

        }
    }
    //  alert(restRelArr[0]);
    restRelStr=restRelArr.toString();

}
function createrelation(colVal){
    var relationTable=document.getElementById("relationTable");
    var x=relationTable.rows.length
    var tabRows=relationTable.rows;
    var y=colVal.split(".");
    var flag=false;
    var colFlag=true;
    var zxx=""
    for(a=0;a<tabNameArr.length;a++){
        if(y[0].match(tabNameArr[a])!=null)
            zxx= tabIdArr[a];
    // alert(tabIdArr[a]);
    }
    if(x>0){

        if(rowId!=0){
            x=(rowId);

        }
        var cells=tabRows[x-1].cells;
        var childs=cells[1].childNodes;
        if(cells[2].innerHTML=="&nbsp;"){

            var cellFirst=(cells[0].innerHTML).split(".");
            if(cellFirst[0]==y[0]){
                $('#warning').dialog('open');
                $("#alert").html("Invalid Entry");
            }
            else{

                //my New Code start
                tempStr +=zxx;
                tempStrRev=zxx+tempStrRev;
                //alert("tempStr "+tempStr+"tempStrRev "+tempStrRev+"restRelStr "+restRelStr);
                if((restRelStr.match(tempStrRev)!=null) || (restRelStr.match(tempStr)!=null)){
                    if(!(autoSuggestFlag)){
                        $('#warning').dialog('open');
                        $("#alert").html("Invalid Relationship");
                    }

                    document.getElementById('relationTable').deleteRow(x-1);
                /* for(i=0;i<autoSuggestArr.length;i++){
        if(autoSuggestArr[i]>0 && (x-1)!=autoSuggestArr[i])
            autoSuggestArr[i]=autoSuggestArr[i]-1;
        else
            autoSuggestArr[i]=-1;
    }*/
                }
                else{
                    cells[2].innerHTML=colVal;
                    addRelArr.push(tempStr);
                    addRelStr=addRelArr.toString()
                    restrictRel(x);


                //end New Code
                }
            }

            flag=false;
            return true;
        }
        else if(childs[0].value=="BETWEEN"){
            if(cells[4].innerHTML=="&nbsp;"){
                var cellSec=(cells[2].innerHTML).split(".");
                if(cellSec[0]!=y[0]){
                    $('#warning').dialog('open');
                    $("#alert").html("Invalid Entry");
                }
                else
                    cells[4].innerHTML=colVal;
                return true;
                flag=false;
            }
            else{
                flag=true;
            }
        }
        else{
            flag=true;
        }

        rowId=0;
    }
    else
        flag=true;
    if(flag){
        tempStr="";
        tempStrRev="";
        row = relationTable.insertRow(x);
        var temp1=tabNameArr.toString();
        tabName=(temp1.split(y[0])).toString();
        row.id="myRow-"+x;
        tempStr=zxx+"-";
        tempStrRev="-"+zxx;
        if(autoSuggestFlag)
            autoSuggestArr.push(x);
        var cell0= row.insertCell(0);
        cell0.style.border="0.5px solid black";
        cell0.className="ui-corner-all";
        cell0.style.width="40%";
        cell0.innerHTML=colVal;
        var cell1= row.insertCell(1);
        cell1.id="sel-"+x;
        cell1.style.width="10%";
        cell1.style.border="0.5px solid black";
        cell1.className="ui-corner-all";
        var xyz=createSelectBox();
        cell1.appendChild(xyz);
        xyz.style.width="70px";
        var cell2= row.insertCell(2);
        cell2.style.border="0.5px solid black";
        cell2.style.width="40%";
        cell2.className="ui-corner-all";
        cell2.innerHTML="&nbsp;"
        var cell3= row.insertCell(3);
        cell3.style.width="10%";
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
        cell5.style.width="5%";
        cell5.style.cursor="pointer";
        cell5.id=""+x;
        cell5.innerHTML="<img class='ui-icon ui-icon-close' title='Delete' alt='Delete'> ";
        cell5.onclick=function(){
            deleteRelation(this);
        }
        return true;
    }
}
function createSelectBox(){
    var weightElement = document.createElement("select");
    weightElement.name = "selectBox";
    weightElement.id = "selectBox";
    weightElement.onchange=function(){
        checkBetween(this);
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
function checkBetween(selObject){
    var rowObj=(selObject.parentNode.parentNode);
    var cells=rowObj.cells;
    if(selObject.value=="BETWEEN"){
        cells[3].style.visibility="visible";
        cells[3].style.display="table-cell"
        cells[3].innerHTML=" AND "
        cells[4].style.visibility="visible";
        cells[4].style.display="table-cell";
        cells[4].style.width="25%"
        cells[4].innerHTML="&nbsp;"
        var x=rowObj.id.split("-");
        rowId=parseInt(x[1])+1;
    }
    else{
        cells[3].style.visibility="hidden";
        cells[3].style.display="none"
        cells[3].innerHTML="&nbsp;&nbsp;"
        cells[4].style.visibility="hidden";
        cells[4].style.display="none";
        cells[4].style.width="25%"
        cells[4].innerHTML="&nbsp;&nbsp;"
    }
}

function deleteRelation(cellObject){
    var str="";
    var cells=(cellObject.parentNode).cells;
    if(cells[0].innerHTML!="&nbsp;")
        str=str+cells[0].innerHTML+" "
    var selOb=cells[1].childNodes;
    str=str+selOb[0].value+" ";
    if(cells[2].innerHTML!="&nbsp;")
        str=str+cells[2].innerHTML+" ";
    if(cells[4].innerHTML!="&nbsp;&nbsp;")
        str=str+cells[4].innerHTML+" ";
    str=str.replace("&nbsp;","");

    var tabRow=document.getElementById('relationTable').rows;
    for(k=0;k<tabRow.length;k++){
        var cell=tabRow[k].cells;
        if(cell[5].id==cellObject.id){
            break;
        }
    }
    document.getElementById('relationTable').deleteRow(k);
    restRelArr.length=0;
    restRelStr=restRelArr.toString();
    // alert(restRelStr);
    addRelArr.splice(k, 1);
    addRelStr=addRelArr.toString();
    restrictRel();
/* for(i=0;i<autoSuggestArr.length;i++){
        if(autoSuggestArr[i]>0 && k!=autoSuggestArr[i])
            autoSuggestArr[i]=autoSuggestArr[i]-1;
        else
            autoSuggestArr[i]=-1;
    }*/

}
function clearAllTables(){
    if(count>0){
        $('#dialog').dialog('open');
    }
    else{
        $('#warning').dialog('open');
        $("#alert").html("There are no tables to clear")
    }

}

function removeAllTabs(){

    var divOb=document.getElementById("accordion");
    var childs=divOb.childNodes;

    divOb.innerHTML="";
    count=0;
    tabNameArr.length=0;
    tabIdArr.length=0;
    //columnArr.length=0;
    //columnNameArr.length=0;
    // tabDetailsArray.length=0;//****************************
    tableNamesDrag=tabNameArr.toString();
    clearcolRels();
    document.getElementById("relSpan").innerHTML=""

}

function addAllRelations(){
    relationArray.length=0;
   // alert('--'+document.getElementById("relationTable").rows)
    var tableRows=document.getElementById("relationTable").rows;
    var str="";
    var str1="";
    for(i=0;i<tableRows.length;i++){
        var cells=tableRows[i].cells;
        for(j=0;j<cells.length-1;j++){
            //str1="";
            if(j!=1){
                if((cells[j].innerHTML.match("&nbsp;"))==null){
                    str=str+cells[j].innerHTML+" ";
                    str1 +=cells[j].innerHTML+" ";
                }
            }
            else{
                var selOb=cells[j].childNodes;
                str=str+selOb[0].value+" ";
                str1 +=selOb[0].value+" ";
            }

            if(j==4){
                var xy=str1.split("<br>");
                str1=xy.join("");
                relationArray.push(str1);
                str1="";
            }
        }

        if(i!=tableRows.length-1)
             str=str+" AND "
        }
    var x=str.split("<br>");
    str=x.join("");
    document.getElementById("relSpan").innerHTML=(str);
    relWithId=relationArray.toString();
    //alert('relWithId'+relWithId);
    var newrelId= relWithId.replace('$','','gi');
    newrelId= newrelId.replace('[','','gi');
    newrelId= newrelId.replace(']','','gi');
   // alert(newrelId);
    for(i=0;i<columnNameArr.length;i++){


        var newcol=columnNameArr[i].replace('$','','gi');
        newcol=newcol.replace(']','','gi');
          newcol=newcol.replace('[','','gi');
       // newcol=columnNameArr[i].replace("[","");
        //newcol=columnNameArr[i].replace("]","");
          //alert('rel add'+newcol+'--'+newrelId+'==='+newrelId.match(newcol))
        if(newrelId.match(newcol)){
            var z=relWithId.split(columnNameArr[i]+" ");
            relWithId=z.join(columnArr[columnNameArr[i]]);
        }
    }
}

function autoSuggestRelations(type){
    var rel=true;
    clearcolRels();
    if(count>1){
        var $colNames=$(".tableColumns");
        // alert($colNames.get(1));
        /* for(h=0;h<autoSuggestArr.length;h++){
            if(autoSuggestArr[h]>-1){
                document.getElementById('relationTable').deleteRow(autoSuggestArr[h]);

            }
            for(g=0;g<autoSuggestArr.length;g++){
                (autoSuggestArr[g]=autoSuggestArr[g]-1);
            }

        }

        autoSuggestArr.length=0;*/
        autoSuggestFlag=true;
        var xyz=0;
        if(type=="ALL"){
            xyz=$colNames.length;
        }
        else{

            var topTabId=($colNames.get(0).id).split("-");
            for(j=1;j<$colNames.length;j++){
                var nxtTabId=($colNames.get(j).id).split("-");
                if(topTabId[1]==nxtTabId[1]){
                    xyz=(j+1);
                }

            }
        }
        for(i=0;i<xyz;i++){
            for(j=i+1;j<$colNames.length && j!=i;j++){
                if(($colNames.get(i).innerHTML)==($colNames.get(j).innerHTML)){
                    if(($colNames.get(i).id)!=($colNames.get(j).id)){
                        rel=false;
                        var id=($colNames.get(i).id).split("-");
                        createrelation(id[1]+"."+($colNames.get(i).innerHTML));
                        var id2=($colNames.get(j).id).split("-");
                        createrelation(id2[1]+"."+($colNames.get(j).innerHTML));
                    }
                }
            }
        }
        // alert(rel)
        if(rel){
            $('#warning').dialog('open');
            $("#alert").html("Sorry I cant Suggest You Any Relation.");
        }
    }
    else{
        $('#warning').dialog('open');
        $("#alert").html("Please Select More Than One Table")
    }
    autoSuggestFlag=false;
}
function saveRelations(){
    var rel=document.getElementById("relSpan");
    var sp=rel.innerHTML;
    if(sp.length!=0){
        $.ajax({
            url: 'saveRelation.do?actionType=save&relWithId='+relWithId+"&relationArray="+relationArray.toString(),
            success: function(data) {
                if(data=="true"){
                    $('#successMsg').dialog('open');
                    rel.innerHTML="";
                    removeAllTabs();
                    relWithId="";
                    clearcolRels();
                }
                else
                    $('#failureMsg').dialog('open');
            }
        });
    }
    else{
        $("#alert").html("Please Add Relations To Save.");
        $('#warning').dialog('open');
    }

}
function clearRelText(){
    document.getElementById("relSpan").innerHTML="";
}
function clearcolRels(){
    var tab=document.getElementById("relationTable");
    var count=tab.rows.length;
    for(i=0;i<count;i++){
        tab.deleteRow(0);
    }
    autoSuggestArr.length=0;
    addRelArr.length=0;
    restRelArr.length=0;
    addRelStr="";
    restRelStr="";
    tempStr="";
    tempStrRev="";
    clearRelText();
}
function removeTable(tabId){

    delTabId=tabId;
    $('#removeTable').dialog('open');
}

function check(){


    $.ajax({
        url: 'getAllTables.do?actionType=temp',
        success: function(html) {
            document.getElementById("zen").innerHTML=html;

        }
    });


}

function getSavedRelations(){
    if(count>1){
        $.ajax({
            url: 'saveRelation.do?actionType=getRels&tableIDs='+tabIdArr.toString(),
            success: function(data) {


                var relId=data.getElementsByTagName("relId")
                var relClause=data.getElementsByTagName("relClause")
                var z;
                var splitRel;
                var i=0;
                var relIdArr=new Array();
                clearcolRels();
                for(i=0;i<relId.length;i++){
                    relIdArr.push(relId[i].childNodes[0].nodeValue);
                    z=relClause[i].childNodes[0].nodeValue;
                    if(z.match("<=")!=null){
                        splitRel=z.split("<=");
                    }
                    else if(z.match(">=")!=null){
                        splitRel=z.split(">=");
                    }
                    else if(z.match("<")!=null){
                        splitRel=z.split("<");
                    }
                    else if(z.match(">")!=null){
                        splitRel=z.split(">");
                    }
                    else if(z.match("=")!=null){
                        splitRel=z.split("=");
                    }
                    else if(z.match("BETWEEN")!=null){
                        splitRel=z.split("<=");
                    }
                    createrelation(splitRel[0]);
                    createrelation(splitRel[1]);
                }


            }
        });
    }
}
//Santhosh Changed