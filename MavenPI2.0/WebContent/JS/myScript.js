var tid;
var did;
var query;
var gSelectedIndex = -1;
var preservedvalue="";
var selectedItem="";
/* key code constants */
var ENTER = 13;
var KEYUP = 38;
var KEYDOWN = 40;
var BACKSPACE = 8;
var TAB=9;
var xmlHttp;
var ctxPath;
var imageFlag=0;
var imageFlag1=0;
var tbz=0;
var imageId="";
var ajaxArr=new Array();
var suggestArr=new Array();
var fieldValArray=new Array();
var suggestArrLength=0;
var selValAjax=0;
var paramValSel=false;
var tabCode=0;
var scrollPayLoad="";
var scrollURL=""
var onScrollFlag=0;
var scHeight=0;
var scTop=0;
var clHgt=0;
startVal=1;
endVal=0;
var scrollFlag=true
var prevCount=7;
var keyPress=true;
var checkId="";
var allParamIds="";
var testArray=new Array();
window.onload = function(){
    
    //document.getElementById("suggestList").style.display = "none";
    //document.getElementById("suggestList1").style.display = "none";
    //document.requestForm.country1.focus();
    /*document.getElementById(tid).onkeyup = function(e){checkKey(e, this);};
  //document.onclick = checkClick;*/

    /* kill default submit of a single field form */
    //document.myForm.onsubmit = function(){return false;};
    };

function sample()
{    
    document.getElementById("suggestList").style.display = "none";
    document.getElementById("suggestList1").style.display = "none";
    //myForm.puLoginId.focus();
    document.myForm.onsubmit = function(){
        return false;
    };
}
document.onkeydown = function(){

    if(window.event && window.event.keyCode == 116)
    { // Capture and remap F5
        window.event.keyCode = 505;
    }

    if(window.event && window.event.keyCode == 505)
    { // New action for F5
        return false;
    // Must return false or the browser will refresh anyway
    }
}


function selId(id1,id2,id3)
{
    tid=id1;
    did=id2;
    query=id3;
    getAllImageNames();
    if(imageId=="")
        imageId=id1+"-plus";

    if(tid=='cboShade')
    {
        preservedvalue="";
    }
    
    document.getElementById(did).style.display = "none";
    document.getElementById(tid).onkeyup = function(e)
    {
        
        if(document.getElementById(tid).value.indexOf('ALL')<0)
        {
            checkKey(e, this);
        }

    };
    document.onclick = checkClick;
    hideAll();
}

/*function sendRequest(url, payload)
{
    scrollURL=url;
    var parArr=allParamIds.split(",");
    var parArrVals=new Array();
    for(i=0;i<parArr.length;i++){
        parArrVals.push(document.getElementById(parArr[i]).value);
    }
    var passVales1 = "";
    for(i=0;i<parArr.length;i++){
        parArrVals.push(document.getElementById(parArr[i]).value);
        if(i==0){
            passVales1 =document.getElementById(parArr[i]).value;
        }
        else{
            passVales1 +=";"+document.getElementById(parArr[i]).value;
        }
    }

    var mainUrl=url+"?"+payload+"&allParamIds="+allParamIds+"&parArrVals="+(passVales1)+"&REPORTID="+document.getElementById("REPORTID").value;

    $.ajax({
        url: mainUrl,
        success: function(data){
            document.getElementById('light').style.display='none';
            document.getElementById('fade').style.display='none';
            var suggestList = document.getElementById(did);
            suggestList.innerHTML = "";
            if(onScrollFlag==0){
                handleResponse1(data);
            }
            else if(onScrollFlag==1){
                handleResponse2(data);
            }
        }
    });
}
*/
function sendRequest(url, payload)
{
    scrollURL=url;   
    xmlHttp=GetXmlHttpObject();
    if (xmlHttp==null){
        alert ("Your browser does not support AJAX!");
        return;
    }
    var parArr=allParamIds.split(",");
    var parArrVals=new Array();
    for(i=0;i<parArr.length;i++){
        parArrVals.push(document.getElementById(parArr[i]).value);
    }
    //changed by susheela satrt
    var passVales1 = "";
    for(i=0;i<parArr.length;i++){
        parArrVals.push(document.getElementById(parArr[i]).value);
        if(i==0){
            passVales1 =document.getElementById(parArr[i]).value;
        }
        else{
            passVales1 +=";"+document.getElementById(parArr[i]).value;
        }
    }
    //changed by susheela over

    var REPORTID=document.forms.frmParameter.REPORTID.value;    
    var mainUrl=url+"?"+payload+"&allParamIds="+allParamIds+"&parArrVals="+(passVales1)+"&REPORTID="+REPORTID;
    getLOVS(mainUrl);
    
/*
     xmlHttp.onreadystatechange=stateChanged;
    xmlHttp.open("POST",mainUrl,true);
    xmlHttp.send(null);
    */
    
}



function handleResponse1(response){ 
    var suggestList = document.getElementById(did);
    if(response=="Search Exception"){
        document.getElementById(tid).value="";
        suggestList.innerHTML="<font style='font-size:10px;color:red'>Invalid Search Key<br> Please Use <font style='font-weight:thicker;color:black'>@</font> for Blind Search</font>";
        suggestList.style.display = "";
        ajaxArr.push(suggestList);
    }
    else{

        var tabId="divTable"+did;
        suggestList.innerHTML = "";

        var names1=new Array();
        var names = response.split("\n");
        for(var j=0;j<names.length-1;j++)
        {
            if(document.getElementById(tid).value.indexOf(names[j])<0)
            {
                names1.push(names[j]);
            }
        }
        var id323="myTable"+tid;
        
        /*var table=document.createElement("table");

table.id=id323;
table.className = "myAjaxTable";
 suggestList.appendChild(table);*/

        suggestList.innerHTML="<table id='"+id323+"' style='word-wrap:breakword;' cellspacing=1 cellpadding=1 class='myAjaxTable'></table>"
        suggestArr.length=0;
        for(var i=0; i < names1.length; i++)
        {

            var suggestItem = document.createElement("div");
            suggestItem.id = "resultlist" + i;
            suggestItem.onmouseover = function(){
                selectItem(this);
            };
            suggestItem.onmouseout = function(){
                unselectItem(this);
            };
            suggestItem.onclick = function(){
                setCountry(this.innerHTML);
            };
            suggestItem.className = "suggestLink";
            suggestItem.appendChild(document.createTextNode(names1[i]));
            suggestArr.push(suggestItem.innerHTML);
            suggestArrLength=suggestArr.length;
            //suggestList.appendChild(suggestItem);
            

            /* ****************************** */

            var tbl = document.getElementById(id323);
            var lastRow = tbl.rows.length;
            // if there's no header row in the table, then iteration = lastRow + 1
            var iteration = lastRow;
            var row = tbl.insertRow(lastRow);
            if(lastRow%2==0){
                // left cell
                var cellLeft = row.insertCell(0);
                // var textNode = document.createTextNode(iteration);
                cellLeft.appendChild(suggestItem);
            }
            else{
                var z=tbl.rows;
                var f=z.length-2;

                // right cell
                var cellRight = z[f].insertCell(1);
                // var xf=newdiv;
                cellRight.appendChild(suggestItem);

            }
        }

        
        if (names1.length >= 1){
            suggestList.style.display = "";
            suggestList.scrollTop=0;

            ajaxArr.push(suggestList);

            changePlusImage();
        }
        else{
            changeMinusImage();
            suggestList.style.display = "none";
        //document.getElementById(tid).focus();
        }

    }
}
function getSuggestions(country){    
    var url;
    ctxPath=document.getElementById("h").value;
    var searchKey=country.value;
    var payload;
    payload = "q="+searchKey+"&"+"query="+query;
    url=ctxPath+"/dsrv";
    if(imageFlag==1){
        if(searchKey==""){
            searchKey='@';
        }
        else if(searchKey.match("All")!=null){
            changePlusImage();
            searchKey="@";
        }
        else{
            changePlusImage();
            country.value=searchKey;
            var prevVal=testArray[tid].split(",");
            var currentVal=searchKey.split(",");

            for(j=0;j<prevVal.length;j++){
                if((currentVal[currentVal.length-1]==prevVal[j]) || (currentVal[currentVal.length-1]=="")){
                    searchKey=searchKey+",@";
                }
            }
        }
        payload = "q="+searchKey+"&"+"query="+query;
    }
    if(tabCode!=9){
        checkId=checkId+payload;
        if(searchKey.lastIndexOf(",")!=(searchKey.length-1)){
            sendRequest(url, payload);
        }
        scrollFlag=true;
        startVal=1;
        scrollPayLoad=payload;
    }

}

function checkKey(e, obj)
{

    var country = document.getElementById(tid);
    paramValSel=false;

    
    


    
    if(country.value.length==0)
    {
        
        preservedvalue="";
    }


    if(country.value.length!=0)//if text field is not empty
    {



        if(country.value.lastIndexOf(',')==(country.value.length -1 ))
        {
            preservedvalue=country.value;
        
        }
    }

    /* get key pressed */
    var code = (e && e.which) ? e.which : window.event.keyCode;
    var evt = e || window.event;
    var ecsKey= evt.keyCode;

    
    /* if up or down move thru the suggestion list */
    if(ecsKey==27){
        hideAll();
        resetAllAjax();
    }
    else if (code == KEYDOWN || code == KEYUP)
    {
        var x=document.getElementById(did);

        var index = gSelectedIndex;
        var countId=index+2;
        if(countId==1)
            prevCount=7;
        var divHeight=0;
        divHeight=6*((x.scrollHeight-x.clientHeight)/suggestArr.length);
        if (code ==  KEYDOWN){

            if(index<suggestArr.length-1){
                index++;

                if(x.scrollTop<=(x.scrollHeight-x.clientHeight)){
                    if(x.scrollTop<=(x.scrollHeight-x.clientHeight-10)){
                        if((prevCount-countId)==0){
                            x.scrollTop=x.scrollTop+divHeight+5;
                            prevCount=prevCount+6;
                        }
                    }
                    else
                        x.scrollTop=x.scrollTop+5;
                }
            }
            else{

                x.scrollTop=(x.scrollHeight-x.clientHeight);
            }
        }     else{
            index--;
            countId=index+1;
            if(((prevCount-7)-countId)==0){
                var g=x.scrollTop-(divHeight+5);
                if(g>0)
                    x.scrollTop=g;
                else
                    x.scrollTop=0;

                prevCount=prevCount-6;
            }
            if(index<0){
                index=0;
                x.scrollTop=0;
                prevCount=7;
            }

        }
        /* find item in suggestion list being looked at if any */
        selectedItem = document.getElementById("resultlist" + index);
        if (selectedItem)
        {

            selectItem(selectedItem);


        /* set the field to the suggestion */
        }




    }
    else if (code == ENTER)  /* clear list if enter key */
    {
        selValAjax=1;
        paramValSel=true;
        onScrollFlag=0;
        var onEnterSelVal=selectedItem.innerHTML;

        if(onEnterSelVal==undefined)
            onEnterSelVal=country.value;
        if(onEnterSelVal.match("&amp;")!=null)
            onEnterSelVal=onEnterSelVal.replace("&amp;","&");
        preservedvalue=country.value;
        var test=preservedvalue.split(",");
        if((preservedvalue.length==0) || (preservedvalue=="All") || (onEnterSelVal=="All"))
        {
            country.value = onEnterSelVal;
        
        }
        else
        {
            /* if(fieldValArray.length==0){
          for(i=0;i<test.length;i++){
           if(test[i]!="")
               fieldValArray.push(test[i]);
          }
          }
*/
            var prevValues=testArray[tid].split(",");
            var newStrValue="";
            for(i=0;i<test.length;i++){
                for(j=0;j<prevValues.length;j++){
                    if(test[i]==prevValues[j])
                        newStrValue=newStrValue+test[i]+",";
                }
            }

            if(newStrValue.charAt(newStrValue.length-1)==","){
                
                if(newStrValue.charAt(newStrValue.length-2)==",")
                    newStrValue=newStrValue.substr(0, (newStrValue.length-1));
            }
            if(newStrValue.match(onEnterSelVal)==null)
                country.value = newStrValue+onEnterSelVal;
        }
        //var tempVal=fieldValArray.toString();
        var tempVal=testArray[tid];
        if(tempVal.match(onEnterSelVal)==null){
            //fieldValArray.push(onEnterSelVal);
            testArray[tid]=testArray[tid]+onEnterSelVal+",";
        }
        changeMinusImage();
        clearList();
        imageFlag1=0;

    }

    else if (code == BACKSPACE)
    {
        var temp=country.value;
        if(country.value.indexOf(',')!=country.value.length)
            paramValSel=true;
        temp=temp.substring(0,(temp.lastIndexOf(',')+1));
        
        preservedvalue=temp;
        
        gSelectedIndex = -1;
        getSuggestions(obj);//In checkKey
        
        keyPress=false;
        onScrollFlag=0;

    }
    else if(code==39 || code==37){
        checking123();
    }
    else if (country == obj) /* otherwise get more suggestions */
    {
        
        onScrollFlag=0;
        keyPress=false;
        tabCode=0;
        if(code==TAB){
            tabCode=TAB;
            paramValSel=true;
            hideAll();
        }
        gSelectedIndex = -1;
        getSuggestions(obj);
    }

    var gd=country.value;
    var fg=gd.split(",");
    var commaFlag=0;
    var newStr;
    for(i=0;i<fg.length;i++){
        if(fg[i]=="")
            commaFlag++;
    }
    if(commaFlag>1){
        if((fg[fg.length-2]=="") &&(fg[fg.length-1]=="") )
            newStr = gd.substring(0, gd.length-(commaFlag-1));
        else
            newStr=gd;
        
        country.value=newStr;
    }

//document.getElementById(tid).focus();

}

function selectItem(selectedItem)
{
    var lastItem = document.getElementById("resultlist" + gSelectedIndex);
    
    if (lastItem != null)
        unselectItem(lastItem);

    selectedItem.className = 'suggestLinkOver';
    gSelectedIndex = parseInt(selectedItem.id.substring(10));
//imageFlag1=0;
}

function unselectItem(selectedItem)
{
    selectedItem.className = 'suggestLink';
}

function setCountry(value)
{
    if(value.match("&amp;")!=null)
        value=value.replace("&amp;","&");
    selValAjax=1;
    var textFieldId=document.getElementById(tid);
    if((textFieldId.value=="All") || (textFieldId.value=="") || (value=="All"))
    {
        textFieldId.value=value;

    }

    else
    {
        /* var test=textFieldId.value.split(",");
          if(fieldValArray.length==0){
          for(i=0;i<test.length;i++){
           if(test[i]!="")
               fieldValArray.push(test[i]);
          }
          }
       var newStrValue="";
          for(i=0;i<test.length;i++){
              for(j=0;j<fieldValArray.length;j++){
                  if(test[i]==fieldValArray[j])
                      newStrValue=newStrValue+test[i]+",";
              }
          }*/
        var test=textFieldId.value.split(",");
        var prevValues=testArray[tid].split(",");
        var newStrValue="";
        for(i=0;i<test.length;i++){
            for(j=0;j<prevValues.length;j++){
                if(test[i]==prevValues[j])
                    newStrValue=newStrValue+test[i]+",";

            }
        }
        if(newStrValue.charAt(newStrValue.length-1)==","){
            
            if(newStrValue.charAt(newStrValue.length-2)==",")
                newStrValue=newStrValue.substr(0, (newStrValue.length-1));
        }
        if(newStrValue.match(value)==null)
            textFieldId.value = newStrValue+value;

    }
    //var tempVal=fieldValArray.toString();
    var tempVal=testArray[tid];
    if(tempVal.match(value)==null){
        //fieldValArray.push(value);
        testArray[tid]=testArray[tid]+value+",";
    }
    imageFlag=0;
    imageFlag1=0;
    
    paramValSel=true;
    onScrollFlag=0;
    changeMinusImage();
    clearList();
}



function checkClick(e)
{
    var target = ((e && e.target) ||(window && window.event && window.event.srcElement));
    var tag = target.tagName;
    if (tag.toLowerCase() != "input" && tag.toLowerCase() != "div")
        clearList();
}

function clearList()
{
    var suggestList = document.getElementById(did);
    suggestList.innerHTML = '';
    suggestList.style.display = "none";

}



window.onload = function ()
{
    
    //document.getElementById("suggestList").style.display = "none";
    //document.getElementById("suggestList1").style.display = "none";
    //document.requestForm.country1.focus();
    /*document.getElementById(tid).onkeyup = function(e){checkKey(e, this);};
  document.onclick = checkClick;*/

    /* kill default submit of a single field form */
    //document.myForm.onsubmit = function(){return false;};
    };

function GetXmlHttpObject()
{
    var xmlHttp=null;
    try
    {
        // Firefox, Opera 8.0+, Safari
        xmlHttp=new XMLHttpRequest();
    }
    catch (e)
    {
        // Internet Explorer
        try
        {
            xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
        }
        catch (e)
        {
            xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
        }
    }
    return xmlHttp;
}

function stateChanged(){
    if (xmlHttp.readyState==4){
        document.getElementById('light').style.display='none';
        document.getElementById('fade').style.display='none';
        if(onScrollFlag==0){
            handleResponse1(xmlHttp.responseText);
        }
        else if(onScrollFlag==1){
            handleResponse2(xmlHttp.responseText);
        }
    }
    if (xmlHttp.readyState==3){
        document.getElementById('light').style.display='block';
        document.getElementById('fade').style.display='block';
    }
}

function excludeParameter(img,elementId,id1, id2, id3)
{
    //alert(img)
    var includeIconId = elementId+"-plus";
    $('#'+includeIconId).hide();
    changeImage(img,elementId,id1,id2,id3);
    excludeBox = true;

}

function includeParameter(img,elementId,id1, id2, id3)
{
    //alert(img)
    var excludeIconId = elementId+"-excimg";
    $('#'+excludeIconId).hide();
    changeImage(img,elementId,id1,id2,id3);
    excludeBox = false;

}


function changeImage(img,country1){
    tabCode=0;    
    /* var divLoads=document.getElementsByClassName("imageLoading");
    for(i=0;i<divLoads.length;i++){
      divLoads[i].style.visibility="hidden";
        divLoads[i].style.display="none";
    }*/
    document.getElementById(country1).focus();
    if(ajaxArr.length!=0)
        hideAll();
    if(imageFlag1==0){
        scrollPayLoad="";
        scrollURL=""
        onScrollFlag=0;
        scrollFlag=true;
        keyPress=true;
        tbz++;
        prevCount=7;
        var con=document.getElementById(country1).value;
        var con2 = con.toString().replace(/ /g, '%20');
        con2 = con2.toString().replace("&", "%26");
        con = con2;
        
        imageId=img.id;
        var img2=imageId.split("-");
        var imgId=img2[1];
        var imgSrc=img.src;
        var z;

        if((imgId=="minus")&& (con!="ALL")){            
            z=imgSrc.replace("minus","plus");
            imgId="plus";
            imageFlag=2;
            imageId=img2[0]+"-"+imgId;
            img.id=imageId;
            img.src=z;
        }
        else{
            z=imgSrc.replace("plus","minus");
            imgId="minus"
            imageFlag=1;



            imageId=img2[0]+"-"+imgId;
            img.id=imageId;

            img.src=z;
            imageFlag1=1;
            var country = document.getElementById(country1);
            
            country.focus();
            getSuggestions(country);//In Change Image

        }
    }
    else{
        imageFlag1=0;
    }

}

function changeImage(img,country1,id1, id2, id3){
    selId(id1, id2, id3)
    tabCode=0;
    /* var divLoads=document.getElementsByClassName("imageLoading");
    for(i=0;i<divLoads.length;i++){
      divLoads[i].style.visibility="hidden";
        divLoads[i].style.display="none";
    }*/
    document.getElementById(country1).focus();
    if(ajaxArr.length!=0)
        hideAll();
    if(imageFlag1==0){
        scrollPayLoad="";
        scrollURL=""
        onScrollFlag=0;
        scrollFlag=true;
        keyPress=true;
        tbz++;
        prevCount=7;
        var con=document.getElementById(country1).value;
        var con2 = con.toString().replace(/ /g, '%20');
        con2 = con2.toString().replace("&", "%26");
        con = con2;
        
        imageId=img.id;
        var img2=imageId.split("-");
        var imgId=img2[1];
        var imgSrc=img.src;
        var z;

        if((imgId=="minus")&& (con!="ALL")){
            z=imgSrc.replace("minus","plus");
            imgId="plus";
            imageFlag=2;
            imageId=img2[0]+"-"+imgId;
            img.id=imageId;
            img.src=z;
        }
        else{
            z=imgSrc.replace("plus","minus");
            imgId="minus"
            imageFlag=1;



            imageId=img2[0]+"-"+imgId;
            img.id=imageId;

            img.src=z;
            imageFlag1=1;
            var country = document.getElementById(country1);
            
            country.focus();
            getSuggestions(country);//In Change Image

        }
    }
    else{
        imageFlag1=0;
    }

}


function changeMinusImage(){
    imageId=tid+"-minus";
    var img=document.getElementById(imageId);
    if(img!=null){
        var img2=imageId.split("-");

        var src=img.src;
        var newSrc;

        newSrc=src.replace("minus","plus");
        imageId=img2[0]+"-plus";
        img.id=imageId;
        img.src=newSrc;
        imageFlag=2;
    //clearList();
    
    }
}
function changePlusImage(){

    imageId=tid+"-plus";
    var img=document.getElementById(imageId);
    if(img!=null){
        var img2=imageId.split("-");

        var src=img.src;
        var newSrc;

        newSrc=src.replace("plus","minus");
        imageId=img2[0]+"-minus";
        img.id=imageId;
        img.src=newSrc;
        imageFlag=1;
    
    }
}

function resetAllAjax(){
    //clearList();
    changeMinusImage();

}
function hideAll(){
    var ad;
    while(ajaxArr.length!=0){
        ad=ajaxArr.pop();
        ad.style.display = "none";
    }
    suggestArr.length=0;
}

zs=0;

function checkImg(tb){
    var jk=tb.id.split("-");
    var kl=imageId.split("-");
    if(imageId!=""){
        
        if(jk[0]!=kl[0]){
            
            imageFlag1=0;
            fieldValArray.length=0;
        }
        else{
            
            imageFlag1=1;
        }
    }
    if((imageFlag==2)&&(imageFlag1==1))
        imageFlag1=0;

    if(ajaxArr.length==0)
        imageFlag1=0;
    if(paramValSel)
        imageFlag1=0;



    scHeight=0;
    scTop=0;
    clHgt=0;
    startVal=1;
    endVal=0;

    gSelectedIndex = -1;
}

function onScrollDiv(divObj){
    scHeight=(divObj.scrollHeight);
    scTop=(divObj.scrollTop);
    
    
    clHgt=(divObj.clientHeight);
    if(scrollFlag){
        if(scTop==(scHeight-clHgt)){

            startVal=startVal+20;
            scrollPayLoad1=scrollPayLoad+"&startValue="+startVal;

            sendRequest(scrollURL, scrollPayLoad1);
            onScrollFlag=1;
        }

    }
}
function handleResponse2(response){

    if(response=="Search Exception"){
        var suggestList = document.getElementById(did);
        document.getElementById(tid).value="";
        suggestList.innerHTML="<font style='font-size:10px;color:red'>Invalid Search Key<br> Please Use @ for Blind Search</font>";
        suggestList.style.display = "";
        ajaxArr.push(suggestList);
    }
    else{

        var x=document.getElementById("myTable"+tid).rows;
        
        var names1=new Array();
        // var names = response.xhr.responseText.split("\n");
        var names = response.split("\n");
        

        for(var j=0;j<names.length-1;j++)
        {
            
            if(document.getElementById(tid).value.indexOf(names[j])<0)
            {
                
                names1.push(names[j]);

            
            }
        }


        suggestArrLength=suggestArr.length;

        for(var i=0; i < names1.length; i++)
        {

            var suggestItem = document.createElement("div");
            var sugId=suggestArrLength+i;
            suggestItem.id = "resultlist"+sugId;
            
            suggestItem.onmouseover = function(){
                selectItem(this);
            };
            suggestItem.onmouseout = function(){
                unselectItem(this);
            };
            suggestItem.onclick = function(){
                setCountry(this.innerHTML);
            };
            suggestItem.className = "suggestLink";
            suggestItem.appendChild(document.createTextNode(names1[i]));
            suggestArr.push(suggestItem.innerHTML);
            //suggestList.appendChild(suggestItem);
            

            /* ****************************** */


            // if there's no header row in the table, then iteration = lastRow + 1
            var tbl = document.getElementById("myTable"+tid);
            var lastRow = tbl.rows.length;
            var iteration = lastRow;
            var row = tbl.insertRow(lastRow);
            if(lastRow%2==0){
                // left cell
                var cellLeft = row.insertCell(0);
                // var textNode = document.createTextNode(iteration);
                cellLeft.appendChild(suggestItem);
            }
            else{
                var z=tbl.rows;
                var f=z.length-2;

                // right cell
                var cellRight = z[f].insertCell(1);
                // var xf=newdiv;
                cellRight.appendChild(suggestItem);

            }
        }
        if(names1.length==0){
            
            scrollFlag=false;
        }

    
    }
}
function checking123(){

}

function displayfavlink(){
    $("#favlinkcont").toggle(500);

/*
    obj =document.getElementById('favlinkcont');
    obj1 = document.getElementById('displayfavimg');
    if (obj.style.display != 'none'){
        obj.style.display ='none';
        obj1.src='images/addImg.gif';

    }
    else{
        obj.style.display ='';
        obj1.src='images/deleteImg.gif';
    }
    */
}
function parentFunction(){
    //window.location.href='sapsalesvolrep.jsp';
    //window.location.href='pbusercustlinks.jsp';
    submitform();
}
function mngFavLinks(){
    window.open("pbCustomizeLinks.jsp?userId="+document.frmParameter.hdnUserid.value,"CustomizeLinks", "scrollbars=1,width=550,height=350,address=no");
}
function seqFavLinks(){
    window.open("pbPrioritizeLinks.jsp?userId="+document.frmParameter.hdnUserid.value,"PrioritizeLinks", "scrollbars=1,width=550,height=350,address=no");
}

function submitform(){    
    document.forms.frmParameter.action = "reportViewer.do?reportBy=viewReport&REPORTID="+document.forms.frmParameter.REPORTID.value;
    document.forms.frmParameter.submit();
}
function submitformTimeSeries(isTimeSeries){
    document.forms.frmParameter.action = "reportViewer.do?reportBy=viewReport&REPORTID="+document.forms.frmParameter.REPORTID.value+'&isTimeSeries='+isTimeSeries;
    document.forms.frmParameter.submit();
}
function submitdashboard(){
    document.forms.frmParameter.action = "dashboardViewer.do?reportBy=viewDashboard&REPORTID="+document.forms.frmParameter.REPORTID.value;
    document.frmParameter.submit();
}
function copyP()
{
    document.frmParameter.action="Copy/Jsps/sample2.jsp";
    document.frmParameter.target="_blank";
    document.frmParameter.submit();

}

function submiturls1($ch)
{
    
    document.frmParameter.action = $ch;
    document.frmParameter.submit();
}
function submiturls2($ch)
{

    document.frmParameter.action = "reportViewer.do?reportBy=viewReport&REPORTID="+document.forms.frmParameter.REPORTID.value+$ch;
    document.frmParameter.submit();
}
function displayUtilities(){
    $('#UtilitiesCont').toggle(500)
/*obj =document.getElementById('UtilitiesCont');
    obj1 = document.getElementById('Utilitiesimg');
    if (obj.style.display != 'none'){
        obj.style.display ='none';
        obj1.src='images/addImg.gif';

    }
    else{
        obj.style.display ='';
        obj1.src='images/deleteImg.gif';
    }*/
}

function getAllImageNames(){

    var x=document.getElementsByTagName("img");
    var y="";
    var newArr=new Array();
    for(i=0;i<x.length;i++){
        if(x[i].id.match("plus")){
            y=x[i].id.split("-");
            newArr.push(y[0]);
        
        }
    }
    allParamIds=newArr.toString();
    testArray.length=newArr.length;
    for(j=0;j<newArr.length;j++){
        if((document.getElementById(newArr[j]).value=="") || (document.getElementById(newArr[j]).value=="All"))
            testArray[newArr[j]]="";
        else
            testArray[newArr[j]]=document.getElementById(newArr[j]).value;
    }
}

function getLOVS(mainURL){
    $.ajax({
        url: mainURL,
        success: function(data){
            if(onScrollFlag==0){
                handleResponse1(data);
            }
            else if(onScrollFlag==1){
                handleResponse2(data);
            }

        }
    });
    
}

//added by uday on 19-mar-2010
function submitform2(){
    document.forms.frmParameter.action = "reportViewer.do?reportBy=viewWhatIfScenario&REPORTID="+document.forms.frmParameter.REPORTID.value;
    document.forms.frmParameter.submit();
}
function submitform3(){
    document.forms.frmParameter.action = "reportViewer.do?reportBy=viewWhatIfScenario&REPORTID="+document.forms.frmParameter.REPORTID.value+"&hideShowColumns=true";
    document.forms.frmParameter.submit();
}
function submiturls22($ch)
{
    document.frmParameter.action = "reportViewer.do?reportBy=viewWhatIfScenario&REPORTID="+document.forms.frmParameter.REPORTID.value+$ch;
    document.frmParameter.submit();
}
