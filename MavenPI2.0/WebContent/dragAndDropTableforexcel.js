/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var grpColArray=new Array();
var repNames = new Array();



function createColumn(elmntId,elementName,tarLoc){

    if(isMemberUseInOtherLevel=="true"){
     alert("You can not drag this member,this Dimention Restricted in UserLevel")
    }else{
        var parentUL=document.getElementById(tarLoc);
        var x=grpColArray.toString();
        if(x.match(elmntId)==null){
            grpColArray.push(elmntId);
            repNames.push(elementName);
           // var childLI=document.createElement("li");
            //childLI.id=elmntId+"_li";
            //childLI.style.width='180px';
            //childLI.style.height='auto';
            //childLI.style.color='white';
           // childLI.className='navtitle-hover';
            var table=document.createElement("table");
            table.id=elmntId+"_table";
            var row=table.insertRow(0);
            row.style.width='100%';
            row.style.height='auto';
            row.style.color='white';
              row.className='navtitle-hover';
            var cell1=row.insertCell(0);

                   cell1.style.width='5%';
            cell1.style.backgroundColor="#e6e6e6";
            var a=document.createElement("a");
            var deleteElement = elmntId;
            a.href="javascript:deleteColumn('"+deleteElement+"')";
            a.innerHTML="a";
            a.className="ui-icon ui-icon-close";
            cell1.appendChild(a);
            var cell2=row.insertCell(1);
            cell2.style.backgroundColor="#e6e6e6";
            cell2.style.color='black';
            cell2.style.width='55%';
            cell2.innerHTML=elementName;
//            var cell3=row.insertCell(2);
//            cell3.style.width='12%';
//            var input=document.createElement("input");
//            input.type="number";
//            input.style.width='100%';
//            input.name=elmntId+"_sheetName";
//            input.id="sheetNo";
//            input.min='0';
//            input.value='Sheet No.';
//           // input.onclick="document.sortable.elmntId+"_sheetName".value=''";
//            cell3.appendChild(input);
//            var cell4=row.insertCell(3);
//            cell4.style.width='12%';
//            var input1=document.createElement("input");
//            input1.type="number";
//            input1.style.width='100%';
//            input1.name=elmntId+"_lineName";
//            input1.id="lineNo";
//            input1.min='0';
//            input1.value='Line No.';
//           // input1.onclick='document.sortable.elmntId+"_lineName".value=""';
//            cell4.appendChild(input1);
//            //added by Amar to get col no. and header value
//            var cell5=row.insertCell(4);
//            cell5.style.width='12%';
//            var input2=document.createElement("input");
//            input2.type="number";
//            input2.style.width='100%';
//            input2.name=elmntId+"_colName";
//            input2.id="colNo";
//            input2.min='0';
//            input2.value='Col No.';
//           // input1.onclick='document.sortable.elmntId+"_lineName".value=""';
//            cell5.appendChild(input2);
//            var cell6=row.insertCell(5);
//            cell6.style.width='12%';
//            var input3=document.createElement("input");
//            input3.type="checkbox";
//            input3.style.width='100%';
//            input3.name=elmntId+"_headerName";
//            input3.id="headNo";
//            input3.value='';
//           // input1.onclick='document.sortable.elmntId+"_lineName".value=""';
//            cell6.appendChild(input3);
//            cell6.
            parentUL.appendChild(table);
        }else{
            alert("Already assigned");
        }

        $(".sortable").sortable();
    /*$(".sortable").disableSelection();
                 */

    }
}
function deleteColumn(index)
{
   // alert("index\t"+index)
    //alert("grpColArray\t"+grpColArray.length)
    var LiObj=document.getElementById(index+"_table");
    var parentUL=document.getElementById(LiObj.parentNode.id);
    parentUL.removeChild(LiObj);

    var i=0;
    for(i=0;i<grpColArray.length;i++){
        if(grpColArray[i]===index){
            grpColArray.splice(i,1);
            repNames.splice(i,1);
        }
    }
 whatifMesdelete(index);

}

