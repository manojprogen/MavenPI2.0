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
//stickNoteCount++;

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
function divMove(objName){
    whichDog=document.getElementById(objName);
}

//function addNew(){
//    var str;
//    var divid = "NOTE_"+stickNoteCount;
//    while(document.getElementById(divid)!= null)
//    {
//        stickNoteCount++;
//        divid = "NOTE_"+stickNoteCount;
//    }
//
//    var noteLabel = prompt("Enter note label","Note");
//
//    if(noteLabel != null)
//    {
//        str="<div id='"+divid+"' style='position:absolute;width:150px;left:600px;top:200px;'>";
//        str=str+"<table border='0' width='100px' class='mycls' cellspacing='0' cellpadding='0' >";
//        str=str+"<tr onmousemove=divMove('"+divid+"') class='navtitle1'>";
//        str=str+"<td id='titleBar' onMouseUp=funUp('" + divid + "') style='cursor:move' width='80%'>";
//        str=str+"<ilayer width='100%' onSelectStart='return false'>";
//
//        str=str+"<layer id='xyz' width='100%' onMouseover='isHot=true;if (isN4) ddN4(theLayer)' onMouseout='isHot=false'  >";
//
//        str=str+"<font><input type=text maxlength=15 size=20 class='inputlabel' value='"+ noteLabel +"' readonly style='border:0px'></font>";
//        str=str+"</layer>";
//        str=str+"</ilayer>";
//        str=str+"</td>";
//        str=str+"<td style='cursor:hand' valign='top' width='10%' title=\"Save Note\">";
//        str=str+"<a href='#' onClick=\"javascript:saveNote('txt"+divid+"','" + divid + "','')\" class='ui-icon ui-icon-disk'></a>";
//        str=str+"</td>";
//        str=str+"<td style='cursor:hand' valign='top' width='10%' title=\"Delete Note\">";
//        str=str+"<a href='#' onClick=\"Close('" + divid + "');return false;\" class='ui-icon ui-icon-trash'></a>";
//        str=str+"</td>";
//        str=str+"</tr>";
//        str=str+" <tr>";
//        str=str+"<td width='100%' valign='top' bgcolor='#d7faff' colspan='3'>";
//
//        str=str+"<Textarea name='sticknote'  cols='18' rows='8' class='mycls' style='overflow:none' id='txt"+divid+"' lang='NEWID'";
//        str=str+" onfocus='javascript:tAreaFocus(this)' ";
//        str=str+" onblur=javascript:updateNote(this,'" + divid + "','')>";
//        str=str+initialText;
//        str=str+"</Textarea>";
//
//        str=str+"</td>";
//        str=str+"</tr>";
//        str=str+" </table>";
//        str=str+"</div>";
//
//        var innerData = document.getElementById("MyStick").innerHTML;
//        document.getElementById("MyStick").innerHTML = innerData + str;
//
//        stickNoteCount++;
//
//    }
//}

function createStickyNote(){
    var noteLabel = prompt("Enter note label","Note");
    if(noteLabel != null)
    {
        $.ajax({
            url: 'stickyNoteAction.do?stickyNoteParam=createStickyNote&noteLabel='+noteLabel+'&intText='+initialText,
            success: function(data){
                var innerData =$('#StickNoteSpan').html();
                $('#StickNoteSpan').html(innerData+data);
            }

        });
        document.getElementById('StickNoteSpan').style.display="";
    }
}
function saveStickyNote(txtAreaID,divID,mode){
    var divObj = document.getElementById(divID);
    var txtAreaObj=document.getElementById(txtAreaID);
    var trObj=divObj.getElementsByTagName("tr");
    var tdObj=trObj[1].getElementsByTagName("td");
    var trbgcolor=trObj[1].style.backgroundColor;
    var snote = txtAreaObj.value;
    var noteLabel = "";
    snote=snote.replace('&','|__|').replace('+','|_|');
    snote=snote.replace(/^\s+/,'').replace(/\s+$/,'');
   
    snote=snote.replace('\n',"<br>","gi");
   
    if(snote==null||snote==""){
        snote=initialText;
    }
    else if(prevTxtValue != snote){
        var inputID = divObj.getElementsByTagName("input");
        if(inputID.length > 0)
            noteLabel = inputID[0].value;
        var topp    = divObj.offsetTop;
        var leftp   = divObj.offsetLeft;
        var widthp  = divObj.offsetWidth;
        var heightp = divObj.offsetHeight;
        $.ajax({
            url: 'stickyNoteAction.do?stickyNoteParam=saveStickyNote&REPORTID='+document.getElementById("REPORTID").value+'&completeURL='+document.getElementById("txtcompleteurl").value
            +"&topp="+topp+"&leftp="+leftp+
            "&widthp="+widthp+"&heightp="+heightp+
            "&snote="+snote+"&noteID="+divID+"&trbgcolor="+trbgcolor+
            "&mode="+mode+"&noteLabel="+noteLabel+"&parameters="+document.getElementById("parameters").value+"&TimeLevelstr="+document.getElementById("TimeLevelstr").value,

            success: function(data){
                if(data!=null&&data!=""){
                    if(mode=="save"){
                        var hrefObj = divObj.getElementsByTagName("a")
                        var hrefID=hrefObj[0].id
                        var  aObj = document.getElementById(hrefID);
                        aObj.setAttribute("onclick","saveStickyNote('"+txtAreaID+"','"+divID+"','update')");

                        
                        var stickyHTMLBuffer="";

                        stickyHTMLBuffer=stickyHTMLBuffer+"<Td style='cursor:hand' valign='top' width='10%' title='Alarm'>";
                        stickyHTMLBuffer=stickyHTMLBuffer+"<img src='images/bell.png' onclick='openAlarm(" + divID + ")'></Td>";
                        stickyHTMLBuffer=stickyHTMLBuffer+"<Td style='cursor:hand' valign='top' width='10%' title='Hide For Now'>";
                        stickyHTMLBuffer=stickyHTMLBuffer+"<a href='javascript:void(0)' onclick='hideStickTextNow(" + divID + ")' class='ui-icon ui-icon-gear'></a> </Td>";

                        

                       var td0= $("#"+divID+"tr").find("td").eq(0);
                       var td1= $("#"+divID+"tr").find("td").eq(1);
                       var td2= $("#"+divID+"tr").find("td").eq(2);
                       $("#"+divID+"tr").html("<td Td id='titleBar'  style='cursor:move' width='60%'>"+td0.html()+"</td>"+stickyHTMLBuffer+"<td Td style='cursor:hand' valign='top' width='10%' title='Update Note'>"+td1.html()+"</td><td Td style='cursor:hand' valign='top' width='10%' title='Delete Note'>"+td2.html()+"</td>");
                     }
                    document.getElementById('manageStickyFrame').contentWindow.location.reload()
                    $("#"+txtAreaID).html(snote);
                }
            }
        });
    }

}

function Close(obj){
    var obj1 = document.getElementById(obj);
    var tid=obj1.getElementsByTagName("textarea");
    if (isIE||isNN) obj1.style.visibility="hidden";
    else if (isN4) document.theLayer.visibility='hide';
    if(tid[0].value != initialText)
        updateNote(tid[0],obj,"D"); //Delete entry
     
// window.location.href="manageStickyNote.jsp";
}

//function saveNote(tobj,divid,operation){
//    //  alert("save")
//    var tid = document.getElementById(tobj);
//    var xmlData="";
//    var divObj = document.getElementById(divid);
//    if(divObj == null)
//        divObj = divid;
//
//    var snote = tid.value;
//    var noteId = tid.lang;
//    var noteLabel = "";
//
//    snote=snote.replace('&','|__|').replace('+','|_|');
//    if(snote.replace(/^\s+/,'').replace(/\s+$/,'') == "" || snote.replace(/^\s+/,'').replace(/\s+$/,'') == null)
//    {
//        tid.value = initialText;
//    //tid.innerHTML = initialText;
//    }
//    else if(prevTxtValue != snote)
//    {
//        tid.innerHTML = tid.value;
//
//        var txtid = divObj.getElementsByTagName("input");
//        if(txtid.length > 0)
//            noteLabel = txtid[0].value;
//
//        var topp    = divObj.offsetTop;
//        var leftp   = divObj.offsetLeft;
//        var widthp  = divObj.offsetWidth;
//        var heightp = divObj.offsetHeight;
//        $.ajax({
//            url: 'reportTemplateAction.do?templateParam=saveSticky&REPORTID='+document.getElementById("REPORTID").value+'&completeURL='+document.getElementById("txtcompleteurl").value,
//            success: function(data){
//                if( data!= null){
//                    xmlData = data;
//                    snote=encodeURI(snote);
//                    var source = "pbUpdateNotes.jsp?topp="+topp+"&leftp="+leftp+
//                    "&widthp="+widthp+"&heightp="+heightp+
//                    "&snote="+snote+"&noteId="+noteId+
//                    "&operation="+operation+"&noteLabel="+noteLabel+"&parameters="+document.getElementById("parameters").value+"&TimeLevelstr="+document.getElementById("TimeLevelstr").value+
//                    "&REPORTID="+document.getElementById("REPORTID").value+"&xmlData="+xmlData;
//                    var dSrc = document.getElementById("updateFrame");
//                    dSrc.src =source;
//                    // var code=document.getElementById("manageStickyFrame");
//                    //  code.src=code.src;
//                    // alert("in ajax");
//                    document.getElementById('manageStickyFrame').contentWindow.location.reload();
//                }
//            }
//        });
//        insertedNew = tid;
//
//    }
//
//}

//function updateNote(tobj,divid,operation){
//    // alert("update operation"+operation)
//    var xmlData="";
//    var divObj = document.getElementById(divid);
//    if(divObj == null)
//        divObj = divid;
//
//    var snote = tobj.value;
//    //    alert("tobj.lang---"+tobj.lang)
//    var noteId = tobj.lang;
//    var noteLabel = "";
//    snote=snote.replace('&','|__|').replace('+','|_|');
//    if(snote.replace(/^\s+/,'').replace(/\s+$/,'') == "" || snote.replace(/^\s+/,'').replace(/\s+$/,'') == null)
//    {
//        tobj.value = initialText;
//    //tobj.innerHTML = initialText;
//    }
//    else if(prevTxtValue != snote)
//    {
//        tobj.innerHTML = tobj.value;
//
//        var txtid = divObj.getElementsByTagName("input");
//        if(txtid.length > 0)
//            noteLabel = txtid[0].value;
//
//        var topp    = divObj.offsetTop;
//        var leftp   = divObj.offsetLeft;
//        var widthp  = divObj.offsetWidth;
//        var heightp = divObj.offsetHeight;
//        $.ajax({
//            url: 'reportTemplateAction.do?templateParam=saveSticky&REPORTID='+document.getElementById("REPORTID").value+'&completeURL='+document.getElementById("txtcompleteurl").value,
//            success: function(data){
//
//                if( data!= null){
//                    xmlData = data;
//                    snote=encodeURI(snote)
//                    var source = "pbUpdateNotes.jsp?topp="+topp+"&leftp="+leftp+
//                    "&widthp="+widthp+"&heightp="+heightp+
//                    "&snote="+snote+"&noteId="+noteId+
//                    "&operation="+operation+"&noteLabel="+noteLabel+"&parameters="+document.getElementById("parameters").value+"&TimeLevelstr="+document.getElementById("TimeLevelstr").value+
//                    "&REPORTID="+document.getElementById("REPORTID").value+"&xmlData="+xmlData;
//                    var dSrc = document.getElementById("updateFrame");
//                    dSrc.src =source;
//                    //                    var code=document.getElementById("manageStickyFrame");
//                    //                    code.src=code.src;
//                    //  alert(divid);
//                    //  alert($('#'+divid).attr('name'));
//                    //alert("in ajax");
//                    if(operation=="D"){
//                        $('#manageStickyFrame').contents().find('#'+divid).remove();
//                    //                        parent.window.location.reload(true);
//                    }
//                    document.getElementById('manageStickyFrame').contentWindow.location.reload();
//                }
//            }
//        });
//        insertedNew = tobj;
//
//    }
//
//}
//function funUp(divname)
//{
//    var divid = document.getElementById(divname);
//    var tagid = divid.getElementsByTagName("textarea");
//    if(tagid[0].value != initialText)
//        updateNote(tagid[0],divid,'');
//}
function tAreaFocus(tobj){
    prevTxtValue = "";
    if(tobj.value == initialText)
        tobj.value = "";
    else
        prevTxtValue = tobj.value;
}

function hideStickText(id,text){
    var hideConfirm = confirm("Hides Text For Session");
    if(hideConfirm ==  true){
        var frameObj = document.getElementById("widgetframe");
        var source = 'divPersistent.jsp?method=forhideStickText&disp=block&stickListId='+id+'&stkText='+text;
        frameObj.src = source;
     
        document.getElementById(id).style.display = "none";
    }
}

function shwSticky(obj){
    var stkid = obj.id;
    var LiObj=document.getElementById(stkid);
    var parentUL=document.getElementById(LiObj.parentNode.id);
    parentUL.removeChild(LiObj);
    $.ajax({
        url: 'reportTemplateAction.do?templateParam=forshowStickText&disp=none&stickListId='+stkid,
        success: function(data){
            document.getElementById(stkid).style.display = data;
        }
    });

}
function hideDiv(){
    $("#showStickyNote").html("");
//    $("#showStickyNote").remove();
//    divObj.style.display="none";
}



