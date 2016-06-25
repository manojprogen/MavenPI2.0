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
var xmlHttp;
var ctxPath;



function sample()
{
   // alert("Main");
  document.getElementById("emaildiv").style.display = "none";
  //document.getElementById("suggestList1").style.display = "none";
  //mymailsendForm.puLoginId.focus();
  document.getElementById("dateSelect").style.display='none';
document.getElementById("timeselect").style.display='';
document.getElementById("onlyDateSelect").style.display='none';
document.getElementById("doubleRisk").style.display='none';
document.getElementById("singleRisk").style.display='';
//document.getElementById("mediumdoubleRisk").style.display='';
//document.getElementById("mediumsingleRisk").style.display='none';
//document.getElementById("lowdoubleRisk").style.display='none';
 //document.mymailsendForm.onsubmit = function(){return false;};
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
 if(tid=='cboRegion')
 {
   preservedvalue="";
 }
// alert("values set are "+tid+ "  " +did+"   "+id3)
 document.getElementById(did).style.display = "none";
  document.getElementById(tid).onkeyup = function(e)
  {
      //alert("value is "+document.getElementById(tid).value);
      if(document.getElementById(tid).value.indexOf('ALL')<0)
          {
      checkKey(e, this);
          }

  };
  //alert(1);
  document.onclick = checkClick;

}


function sendRequest(url, payload)
{
   //alert("url is "+url+"payload is "+payload);
    xmlHttp=GetXmlHttpObject();
if (xmlHttp==null)
{
alert ("Your browser does not support AJAX!");
return;
}

var mainUrl=url+"?"+payload;
//alert("mainUrl "+mainUrl);
xmlHttp.onreadystatechange=stateChanged;
xmlHttp.open("GET",mainUrl,true);
xmlHttp.send(null);

    /*var options = {method:"GET",
                     payload:payload,
                     onSuccess: handleResponse
                  };*/

    //AjaxTCR.comm.sendRequest(url, options);
    //alert("sendRequest")
}


function handleResponse1(response)
{
    //alert("in handleResponse1");
    var suggestList = document.getElementById(did);
    suggestList.innerHTML = "";
      var names1=new Array();
   // var names = response.xhr.responseText.split("\n");
        var names = response.split("\n");
 //  alert("length "+names.length)

    for(var j=0;j<names.length-1;j++)
    {
     // alert("total names "+names[j]+" "+j)
        if(document.getElementById(tid).value.indexOf(names[j])<0)
        {
         //alert("deleted "+names.splice(j,1))
         names1.push(names[j]);

          //alert("in if")
        }
    }
  // alert("length is "+names1.length)

    for(var i=0; i < names1.length; i++)
    {

      var suggestItem = document.createElement("div");
      suggestItem.id = "resultlist" + i;
      suggestItem.onmouseover = function(){selectItem(this);};
      suggestItem.onmouseout = function(){unselectItem(this);};
      suggestItem.onclick = function(){setCountry(this.innerHTML);};
      suggestItem.className = "suggestLink";
      suggestItem.appendChild(document.createTextNode(names1[i]));
      suggestList.appendChild(suggestItem);
     // alert("In For");
    }

    if (names1.length >= 1)
        suggestList.style.display = "";
    else
        suggestList.style.display = "none";

}

function getSuggestions(country)
{
    //alert("In getSuggestions "+country.value+"tid is "+tid);
    var url;
    ctxPath=document.getElementById("h").value;
    if(tid=='cboRegion'|| tid=='cboState' || tid=='toAddress')
    {
  // alert("url is "+url);
    var payload = "q="+country.value+"&"+"query="+query;
    url=ctxPath+"/getEmails";
 //alert("PayLoad is "+payload);
//alert("Context Path is "+document.getElementById("h").value);
  }

  else if(tid=='basis')
      {
          var payload = "q="+country.value+"&"+"query="+query;
           url=ctxPath+"/dsrv";
      }
       else if(tid=='2dbasis')
      {
          var payload = "q="+country.value+"&"+"query="+query;
           url=ctxPath+"/dsrv";
      }

 /* else if(tid=='cboState')
  {
    var payload = "state="+document.getElementById("cboRegion").value+"&city="+country.value;;
    url=ctxPath+"/surl";
  }*/
    sendRequest(url, payload);
}

function checkKey(e, obj)
{

    var country = document.getElementById(tid);
    //alert("In CheckKey")
    //alert("Country value is "+document.getElementById(tid).value)
    //alert("country is "+country.value)


    //alert(country.length)
      if(country.value.length==0)
      {
      //alert("text box is empty")
       preservedvalue="";
       }


    if(country.value.length!=0)//if text field is not empty
    {



    if(country.value.lastIndexOf(',')==(country.value.length -1 ))
    {
      preservedvalue=country.value;
      //alert("preserved value is "+preservedvalue)
    }
    }

    /* get key pressed */
    var code = (e && e.which) ? e.which : window.event.keyCode;
// alert("code "+code)
    /* if up or down move thru the suggestion list */
    if (code == KEYDOWN || code == KEYUP)
    {
        var index = gSelectedIndex;
        if (code ==  KEYDOWN)
            index++;
        else
            index--;
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

     if(preservedvalue.length==0)
          {
           country.value = selectedItem.innerHTML;
           //alert("in if pv is zero"+country.value)
          }
          else
          {
           //alert("value in text field is "+country.value)
            if(country.value.indexOf(',')==-1)
            {

              country.value = selectedItem.innerHTML;
             // alert("In if value is "+country.value)
            }
            else{
            country.value=preservedvalue+selectedItem.innerHTML;
           //alert("in else preservedvalue "+preservedvalue);
           }}

    clearList();

    }

    else if (code == BACKSPACE)
    {
      var temp=country.value;
      //alert("temp is "+temp)
      temp=temp.substring(0,(temp.lastIndexOf(',')+1));
     // alert("new temp is "+temp)
      preservedvalue=temp;
      //alert("new preserved value is "+preservedvalue)
        gSelectedIndex = -1;
        getSuggestions(obj);
     //alert(hai);


    }


    else if (country == obj) /* otherwise get more suggestions */
    {
        //alert("In obj otherwise get more suggestions")
        gSelectedIndex = -1;
        getSuggestions(obj);
    }


}

function selectItem(selectedItem)
{
    var lastItem = document.getElementById("resultlist" + gSelectedIndex);
  // alert("resultlist" + gSelectedIndex)
    if (lastItem != null)
        unselectItem(lastItem);

    selectedItem.className = 'suggestLinkOver';
    gSelectedIndex = parseInt(selectedItem.id.substring(10));
}

function unselectItem(selectedItem)
{
    selectedItem.className = 'suggestLink';
}

function setCountry(value)
{
    /* alert("country.value is"+document.getElementById('country').value)
   if(document.getElementById(tid).value.lastIndexOf(',')==(document.getElementById(tid).value.length -2 )){
    document.getElementById(tid).value =document.getElementById(tid).value.substring(0,document.getElementById(tid).value.lastIndexOf(',')+1)+value;
    alert("in if")
    }
    else{
      document.getElementById(tid).value=value;
      alert("in else")
      }*/

      if(document.getElementById(tid).value.lastIndexOf(',')==-1)
      {
        document.getElementById(tid).value=value;

      }

      else
      {
        var temp=document.getElementById(tid).value.substring(0,(document.getElementById(tid).value.lastIndexOf(',')+1))
         document.getElementById(tid).value=temp+value;
      }




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
  //alert("Main")
  //document.getElementById("suggestList").style.display = "none";
  //document.getElementById("suggestList1").style.display = "none";
  //document.requestForm.country1.focus();
  /*document.getElementById(tid).onkeyup = function(e){checkKey(e, this);};
  document.onclick = checkClick;*/

  /* kill default submit of a single field form */
  //document.mymailsendForm.onsubmit = function(){return false;};
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

function stateChanged()
{

if (xmlHttp.readyState==4)
{
handleResponse1(xmlHttp.responseText);
//alert("In state Changed ready state is 4"+xmlHttp.responseText);
}
}