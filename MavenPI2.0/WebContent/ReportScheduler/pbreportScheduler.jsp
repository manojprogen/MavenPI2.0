 <%@page contentType="text/html" pageEncoding="windows-1252"%>
<%@page import="utils.db.*"%>
<%@page import="prg.db.*"%>
<%@page import="java.sql.*"%>

<%
        String themeColor="blue";
              if(session.getAttribute("theme")==null)
                session.setAttribute("theme",themeColor);
            else
                themeColor=String.valueOf(session.getAttribute("theme"));

  
        try {
           // ////////////////////////////////////////////////////////////////////////////////.println.println("--"+request.getParameter("repName"));
            // ////////////////////////////////////////////////////////////////////////////////.println.println("--"+request.getParameter("REPORTID"));
           String reportType=request.getParameter("ReportType");
            String reportid, reportName;
            if (request.getParameter("repName") == null) {
              reportName = String.valueOf(session.getAttribute("repName"));
                reportid = String.valueOf(session.getAttribute("REPORTID"));
            } else {
                reportid = request.getParameter("REPORTID");
                reportName = request.getParameter("repName");
                    
                    }
            session.setAttribute("repName", reportName);
            session.setAttribute("REPORTID", reportid);
            session.setAttribute("reportType", reportType);
            


%>

<html>
    <head>
        <title>JSP Page</title>

        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript"  src="<%=request.getContextPath()%>/ReportScheduler/dateSelection.js"></script>
        <script type="text/javascript"  src="<%=request.getContextPath()%>/ReportScheduler/myScripts.js"></script>
        <script type="text/javascript"  src="<%=request.getContextPath()%>/ReportScheduler/timePicker.js"></script>

        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/ReportCss.css" rel="stylesheet" />


        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />
        <script type="text/javascript">
                
            function saveSchedule()
            {
                var ctxPath = $("#h").val();
                var reportId = '<%= reportid %>';
                var frequency = $("#frequency").val();
                var time = $("#timepicker2").val();
                var toAddress = $("#toAddress").val();
                var viewbys = $("#allViewBys").attr("checked");
                var contentType = $("#contentType").val();

                $.ajax({
                    url: ctxPath+'/scheduler.do?reportBy=scheduleReport&reportId='+reportId+'&frequency='+frequency+'&time='+time+'&toAddress='+toAddress+'&viewbys='+viewbys+'&contentType='+contentType,
                    success: function(data) {
                        parent.cancelSheduler();
                    }
                });
            }
        </script>
        <script>
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
   
                  document.getElementById("emaildiv").style.display = "none";
//                  document.getElementById("dateSelect").style.display='none';
                document.getElementById("timeselect").style.display='';
//                document.getElementById("onlyDateSelect").style.display='none';
//                document.getElementById("doubleRisk").style.display='none';
  //document.getElementById("suggestList1").style.display = "none";
  //myForm.puLoginId.focus();
 //document.myForm4.onsubmit = function(){return false;};
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
 //alert("values set are "+tid+ "  " +did+"   "+id3)
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
            if(country.value.indexOf(',')==-1)
            {
              country.value = selectedItem.innerHTML;
            }
            else{
            country.value=preservedvalue+selectedItem.innerHTML;
           }}
        clearList();
    }
    else if (code == BACKSPACE)
    {
        var temp=country.value;
        temp=temp.substring(0,(temp.lastIndexOf(',')+1));
        preservedvalue=temp;
        gSelectedIndex = -1;
        getSuggestions(obj);
    }

    else if (country == obj) /* otherwise get more suggestions */
    {
        gSelectedIndex = -1;
        getSuggestions(obj);
    }
}

function selectItem(selectedItem)
{
    var lastItem = document.getElementById("resultlist" + gSelectedIndex);
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

function stateChanged()
{

if (xmlHttp.readyState==4)
{
handleResponse1(xmlHttp.responseText);
//alert("In state Changed ready state is 4"+xmlHttp.responseText);
}
}
        </script>

        <style type="text/css">


 .suggestLink { background-color: #FFFFFF;
                padding: 2px 6px 2px 6px; }
 .suggestLinkOver { background-color: #0099CC;
                    padding: 2px 6px 2px 6px; }
 #cboRegionsuggestList { position: absolute;
                background-color: #FFFFFF;
                text-align: left;
                border: 1px solid #000000;
                border-top-width: 0px;
                width: 160px; }

 #emaildiv { position: absolute;
                background-color: #FFFFFF;
                text-align: left;
                border: 1px solid #000000;
                border-top-width: 0px; }
 #wrapper { display: inline;}

/*  .myhead
            {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 8pt;
                font-weight: bold;
                color:black;
                padding-left:12px;
                width:150px;
                background-color: #b4d9ee;
                border:0px;
                apply this class to a Headings of servicestable only
            }*/

</style>


    </head>
    <body onload="sample()">



            <form action=""  name="myForm" method="post">
            <center>
                <table>
                    <Tr>
                        <Td class="myhead"><span style="color:red">*</span>Scheduler Frequency</Td>
                        <td>
                            <table><tr>
                        <Td>
                            <Select name="frequency" id="frequency" class="myTextbox3" onchange="addDate(this)">
                                <Option value="1">Daily</Option>
                                <Option value="2">Monthly</Option>
                                <Option value="3">Quarterly</Option>
                                <Option value="4">Yearly</Option>
                            </Select>
                        </Td>
                        <Td>
                            <div id="timeselect">
                                <input id='timepicker2' name="timepicker2" type='text' value='12:00 pm' size=8   class="myTextbox3" style="width:auto" maxlength=8 ONBLUR="validateDatePicker(this)">
                                <img align="middle" src="<%=request.getContextPath()%>/images/timepicker.gif" border="0" alt="Select Time"   onclick="selectTime(this,timepicker2,'<%=request.getContextPath()%>/images/')" style="cursor:hand">

                            </div>
                        </Td>
                                </tr></table>
                        </td>
                </Tr>
                <tr>
                    <Td class="myhead"><span style="color:red">*</span>Subscribers</Td>
                        <Td>
                        <input type="text" class="myTextbox3" name="toAddress" style="width:200px" id="toAddress" onfocus="selId('toAddress','emaildiv','qq')">
                            <div id="emaildiv" style="height:200px;overflow:auto"></div>
                        </Td>
                </tr>
                <tr>
                    <td class="myhead">Content Type</td>
                    <td>
                        <Select name="contentType" id="contentType" class="myTextbox3">
                            <Option value="html">HTML</Option>
                            <Option value="pdf">PDF</Option>
                            <Option value="excel">Excel</Option>
                            <Option value="xml">XML</Option>
                        </Select>
                    </td>
                </tr>
                <tr>
                    <td class="myhead">All View Bys</td>
                    <td>
                        <input type="checkbox" id="allViewBys">
                    </td>
                </tr>
                </table>
                <br>
                    <input type="button" class="navtitle-hover" style="width:auto" value="save" onclick="saveSchedule()">
                        <input type="hidden" id="h" value="<%=request.getContextPath()%>">
                        
</center>
</form>

     
    </body>
</html>
   <%} catch (Exception e) {
            e.printStackTrace();
        }
        %>
   