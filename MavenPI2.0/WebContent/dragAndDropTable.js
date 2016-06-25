/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var grpColArray=new Array();


 
function createColumn(elmntId,elementName,tarLoc){
    
    if(isMemberUseInOtherLevel=="true"){
     alert("You can not drag this member,this Dimention Restricted in UserLevel")
    }else{
        var parentUL=document.getElementById(tarLoc);
        var x=grpColArray.toString();
        if(x.match(elmntId)==null){
            grpColArray.push(elmntId);

            var childLI=document.createElement("li");
            childLI.id=elmntId+"_li";
            childLI.style.width='180px';
            childLI.style.height='auto';
            childLI.style.color='white';
            childLI.className='navtitle-hover';
            var table=document.createElement("table");
            table.id=elmntId+"_table";
            var row=table.insertRow(0);
            var cell1=row.insertCell(0);
            //cell1.style.backgroundColor="#e6e6e6";
            var a=document.createElement("a");
            var deleteElement = elmntId;
            a.href="javascript:deleteColumn('"+deleteElement+"')";
            a.innerHTML="a";
            a.className="ui-icon ui-icon-close";
            cell1.appendChild(a);
            var cell2=row.insertCell(1);
            // cell2.style.backgroundColor="#e6e6e6";
            cell2.style.color='black';
            cell2.innerHTML=elementName;
            childLI.appendChild(table);
            parentUL.appendChild(childLI);
        }else{
            alert("Already assigned")
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
    var LiObj=document.getElementById(index+"_li");
    var parentUL=document.getElementById(LiObj.parentNode.id);
    parentUL.removeChild(LiObj);

    var i=0;
    for(i=0;i<grpColArray.length;i++){
        if(grpColArray[i]==index)
            grpColArray.splice(i,1);
    }
 whatifMesdelete(index)

}

