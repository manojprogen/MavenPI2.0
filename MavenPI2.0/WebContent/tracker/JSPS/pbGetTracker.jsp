
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="utils.db.*"%>
<%@page import="prg.db.PbReturnObject" %>
<%@page import="prg.tracker.client.PbTrackerManager"%>
<%@page import="prg.tracker.bean.PbTrackerBean"%>
<%@page import="prg.tracker.db.*"%>
<%@page import="java.util.*" %>
<%@page import="prg.db.Container"%>
<%
        String themeColor="blue";
              if(session.getAttribute("theme")==null)
                session.setAttribute("theme",themeColor);
            else
                themeColor=String.valueOf(session.getAttribute("theme"));

%>
<Html>
    <Head>
          <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.3.2.min.js"></script>-->
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
         <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
       <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/ReportCss.css" rel="stylesheet" />


      

        <Script type="text/javascript"  src="../JS/emailsajax.js"></Script>
        <Script type="text/javascript"  src="../JS/testdate.js"></Script>
        <Script type="text/javascript"  src="../JS/myScripts.js"></Script>
        <Script type="text/javascript"  src="../JS/dateSelection.js"></Script>
        <Script type="text/javascript"  src="../JS/timePicker.js"></Script>


<!--        <link href="myStyles.css" rel="stylesheet" type="text/css">
        <link href="../css/emailsajax.css" rel="stylesheet" type="text/css">
        <link href="../css/cal.css" rel="stylesheet" type="text/css">-->
<!--        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/demos.css" rel="stylesheet" />-->
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/jquery-1.3.2.js"></script>-->

<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.datepicker.js"></script>-->
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />
        <script type="text/javascript">

            $(function() {
                $('#datepicker').datepicker({
                    changeMonth: true,
                    changeYear: true
                });
                $('#datepicker1').datepicker({
                    changeMonth: true,
                    changeYear: true
                });
                $test=$(".ui-state-default ");

                $test.hover(
                function(){
                    this.style.background="#308DBB";
                    this.style.color="#000";
                },
                function(){
                    this.style.background="#E6E6E6";
                    this.style.color="#000"
                }

            );

            });

        </script>
        <script>
            var tid;
            var did;
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
            var nexrowId=0;
            var eleId="";
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


            function createViewval(id1,id2,eleId1,viewType)
            {
                eleId=eleId1;
                tid=id1;
                did=id2;


                if(tid=='country')
                {
                    preservedvalue="";
                }

                document.getElementById(did).style.display = "none";
                document.getElementById(tid).onkeyup = function(e)
                {

                    if(document.getElementById(tid).value.indexOf('ALL')<0)
                    {
                        checkKeyview(e, this);

                    }

                };


                document.onclick = checkClick;

            }
            function sendRequestview(url, payload)
            {

                xmlHttp=GetXmlHttpObject();
                if (xmlHttp==null)
                {
                    alert ("Your browser does not support AJAX!");
                    return;
                }

                var mainUrl=url+"?"+payload;
                xmlHttp.onreadystatechange=stateChangedview;
                xmlHttp.open("GET",mainUrl,true);
                xmlHttp.send(null);

            }


            function handleResponseview(response)
            {

                var suggestList = document.getElementById(did);

                suggestList.innerHTML = "";

                var names1=new Array();
                var names = response.split("\n");
                for(var j=0;j<names.length-1;j++)
                {
                    var checkkeyId=document.getElementById(tid);
                    if(checkkeyId.value.indexOf(names[j])<0)
                    {
                        names1.push(names[j]);
                    }

                }
                var suggestItem = document.createElement('table');

                var str="";
                for(var i=0; i < names1.length; i++)
                {
                    if(i%2==0){
                        var val="'"+names1[i]+"'";

                        str+='<tr>';
                        str+='<td class="suggestLink" id="resultlist'+i+'" onmouseover="selectItem('+this+')" onmouseout ="unselectItem('+this+')" onclick ="setCountryview('+val+')">';
                        str+= names1[i];
                        str+='</td>'
                        if(i==(names1.length-1)){
                           str+='</tr>'
                        }


                    }else{
                        var val="'"+names1[i]+"'";

                        str+='<td class="suggestLink" id="resultlist'+i+'" onmouseover="selectItem('+this+')" onmouseout ="unselectItem('+this+')" onclick ="setCountryview('+val+')">';
                        str+= names1[i];
                        str+='</td></tr>';

                    }


                }

               str+='</table>';
               var str1="<table>"+str;
                suggestItem.innerHTML=str1;
                suggestList.style.height="100px";
                suggestList.style.overflow="scrollHeight";
                suggestList.appendChild(suggestItem);
                if (names1.length >= 1){
                    suggestList.style.display = "";

                    }
                else
                    suggestList.style.display = "none";

            }

            function getSuggestionsview(country)
            {

                var url;
                ctxPath=document.getElementById("h").value;

                var payload = "q="+country.value+"&eleId="+eleId;
                url=ctxPath+"/RepCepViewVals";
                sendRequestview(url, payload);
            }

            function checkKeyview(e, obj)
            {
                var country = document.getElementById(tid);
                if(country.value.length==0)
                {
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
                    getSuggestionsview(obj);
                    //alert(hai);


                }


                else if (country == obj) /* otherwise get more suggestions */
                {
                    // alert("In obj")
                    gSelectedIndex = -1;
                    getSuggestionsview(obj);
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

            function setCountryview(value)
            {

                if(document.getElementById(tid).value.lastIndexOf(',')==-1)
                {   // alert('in if')
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

            function stateChangedview()
            {

                if (xmlHttp.readyState==4)
                {
                    handleResponseview(xmlHttp.responseText);
                    //alert("In state Changed ready state is 4"+xmlHttp.responseText);
                }
            }

            function changeEorM(){
                var EorMval=document.getElementById("EorM").value;

                if(EorMval=="Email"){
                    document.getElementById("mainemaildiv").style.display='';
                    document.getElementById("mainsmsdiv").style.display='none';
                }else{
                  document.getElementById("mainemaildiv").style.display='none';
                  document.getElementById("mainsmsdiv").style.display='';

                }

            }
        </script>
        <style>


/*            .myhead
            {
                            font-family: Verdana;
                font-size: 8pt;
                font-weight: bold;
                color:black;
                padding-left:12px;
                            width:auto;
                background-color:#b4d9ee;
                border:0px;
                apply this class to a Headings of servicestable only
            }*/

            .suggestLink { background-color: #FFFFFF;
                           padding: 2px 6px 2px 6px; }
            .suggestLinkOver { background-color: #0099CC;
                               padding: 2px 6px 2px 6px; }
            #suggestList { position: absolute;
                           background-color: #FFFFFF;
                           text-align: left;
                           border: 1px solid #000000;
                           border-top-width: 0px;
                           width: 160px; }

            #suggestList1 { position: absolute;
                            background-color: #FFFFFF;
                            text-align: left;
                            border: 1px solid #000000;
                            border-top-width: 0px;
                            width: 160px; }
            #wrapper { display: inline;}
            #country { width: 160px; }
            #country1 { width: 160px; }

            .suggestdiv{
                display:none;
                width:auto;
                height:auto;
                background-color:#ffffff;
                overflow:auto;
                position:absolute;
                text-align:left;
                border:1px solid #000000;
                border-top-width: 0px;
            }
        </style>
    </Head>
    <Body onload="sample()">

        <%


        

            try {

                HashMap map = new HashMap();
                Container container = null;

                ArrayList displayColumns = null;
                ArrayList displayLabels = null;

                String reportId = request.getParameter("REPORTID");
                String completeurl = request.getParameter("completeurl");
                map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
                container = (prg.db.Container) map.get(reportId);
                displayColumns = container.getDisplayColumns();
                for(int i=0;i<displayColumns.size();i++){

                
                }
                displayLabels = container.getDisplayLabels();
                for(int i=0;i<displayLabels.size();i++){
                     
                    }
                String TableId = container.getTableId();
                HashMap parameterHashMap = container.getParametersHashMap();
                HashMap tableHashMap = container.getTableHashMap();

                ArrayList parametersarr = (ArrayList) parameterHashMap.get("Parameters");
                ArrayList ParametersNamesarr = (ArrayList) parameterHashMap.get("ParametersNames");

                ArrayList CEParr = new ArrayList();
                ArrayList REParr = new ArrayList();
                ArrayList allViewarr = new ArrayList();
                ArrayList CEPNamesarr = new ArrayList();
                ArrayList REPNamesarr = new ArrayList();
                ArrayList allViewNBamesarr = new ArrayList();
                ArrayList MeasuresNamesarr = new ArrayList();
                ArrayList Measuresarr = new ArrayList();

                
                
                
                
                
                

                if (tableHashMap.get("REP") != null) {
                    REParr = (ArrayList) tableHashMap.get("REP");
                    REPNamesarr = (ArrayList) tableHashMap.get("REPNames");
                    allViewarr = (ArrayList) REParr.clone();
                    allViewNBamesarr = (ArrayList) REPNamesarr.clone();
                }
                for(int i=0;i<REParr.size();i++){
                    

                    }
                for(int i=0;i<REPNamesarr.size();i++){
                    

                    }

                 for(int i=0;i<allViewarr.size();i++){
                    

                    }
                for(int i=0;i<allViewNBamesarr.size();i++){
                    

                    }


                if (tableHashMap.get("CEP") != null) {
                    CEParr = (ArrayList) tableHashMap.get("CEP");
                    CEPNamesarr = (ArrayList) tableHashMap.get("CEPNames");
             }

                for(int i=0;i<CEParr.size();i++){
                    
                    }


                if (tableHashMap.get("MeasuresNames") != null) {

                    MeasuresNamesarr = (ArrayList) tableHashMap.get("MeasuresNames");
                }
                for(int i=0;i<MeasuresNamesarr.size();i++){
                    
                    }
                


                if (tableHashMap.get("MeasuresNames") != null) {
                    Measuresarr = (ArrayList) tableHashMap.get("Measures");
                }
                for(int i=0;i<Measuresarr.size();i++){
                    
                    }
                
                for (int v = 0; v < CEParr.size(); v++) {
                    if (!allViewarr.contains(CEParr.get(v))) {
                        allViewarr.add(CEParr.get(v));
                        allViewNBamesarr.add(CEPNamesarr.get(v));
                    }
                }
                String vieyByids = "";

                
                
                for (int i = 0; i < allViewarr.size(); i++) {
                    vieyByids += ",CBOARP" + allViewarr.get(i);
                    
                }
                if (allViewarr.size() > 0) {
                    vieyByids = vieyByids.substring(1);
                }
                
                java.sql.Date datestr = new java.sql.Date(System.currentTimeMillis());
                String newdate = datestr.toString();
                String newdatearr[] = newdate.split("-");
                String strdate = "";
                strdate += newdatearr[1] + "/" + newdatearr[2] + "/" + newdatearr[0];

        %>
        <Center>
            <Form name="myForm" method="post">

                <Br>
                    <center>
                <Table width="80%" >
                    <Br>
                    <Tr>
                        <td width="100%">
                            <table width="100%">
                                <tr>
                                    <Td class="myhead" width="40%">Tracker Name</Td>
                                    <Td width="40%"><Input type="text"  class="myTextbox5" name="trackerName" id="trackerName" maxlength=100  style="width:120px"></Td>
                                    <td width="20%"></td>
                                </tr>
                            </table>
                        </td>
                    </Tr>
                    <Tr>
                        <td width="100%">
                            <table width="100%">
                                <tr>
                                    <Td class="myhead" width="40%">Tracker Frequency</Td>
                                    <Td width="30%">
                            <Select name="frequency" id="frequency" class="myTextbox5" onchange="addDate(this)"  style="width:120px">
                                <Option value="1">Daily</Option>
                                <Option value="2">Monthly</Option>
                                <Option value="3">Quarterly</Option>
                                <Option value="4">Yearly</Option>
                            </Select>
                        </Td>
                                    <Td width="30%">
                            <div id="timeselect">
                                <input id='timepicker2' name="timepicker2" type='text' value='12:00 pm' size=8   class="myTextbox3" style="width:auto" maxlength=8 ONBLUR="validateDatePicker(this)">
                                <IMG ALIGN="middle" SRC="<%=request.getContextPath()%>/images/timepicker.gif" BORDER="0" ALT="Select Time" readonly ONCLICK="selectTime(this,timepicker2,'<%=request.getContextPath()%>/images/')" STYLE="cursor:hand">

                            </div>
                                        <div id="dateSelect" style="display:none">
                                <select onchange="correctDate(this.form,this)" name="alertMonth">
                                    <script language="JavaScript" type="text/javascript">
                                        for(i=1; i<13; i++){
                                            if(i==month){
                                                sel = "selected"
                                            }
                                            else{
                                                sel = ""
                                            }
                                            document.write("<option value="+months[i]+" "+sel+">"+months[i]+"\n")
                                        }

                                    </script>
                                </select>
                                            <select name="alertDate" style="display:none">

                                    <script language="JavaScript" type="text/javascript">
                                        var tl=(month==4 || month==6 || month==9 || month==11) ? 30 : (month==2) ? 28 : 31
                                        for(j=1; j<=tl; j++){
                                            if(j==date){
                                                sel = "selected"
                                            }
                                            else{
                                                sel = ""
                                            }
                                            document.write("<option value="+j+" "+sel+">"+j+"\n")
                                        }
                                    </script>
                                </select>


                            </div>
                                        <div id="onlyDateSelect" style="display:none">

                                <select name="alertDate">

                                    <script language="JavaScript" type="text/javascript">
                                        var tl=(month==4 || month==6 || month==9 || month==11) ? 30 : (month==2) ? 28 : 31
                                        for(j=1; j<=tl; j++){
                                            if(j==date){
                                                sel = "selected"
                                            }
                                            else{
                                                sel = ""
                                            }
                                            document.write("<option value="+j+" "+sel+">"+j+"\n")
                                        }
                                    </script>
                                </select>
                            </div>
                        </Td>
                                </tr>
                            </table>
                        </td>

                    </Tr>
                    <Tr>
                        <td width="100%">
                            <table width="100%">
                                <tr>
                                    <Td class="myhead" width="40%">Start Date</Td>
                                    <Td width="40%"><Input type="text" readonly class="myTextbox5" name="startdate" id="datepicker" maxlength=100  style="width:120px" value="<%=strdate%>" ></Td>
                                    <td width="20%"></td>
                                </tr>
                            </table>
                        </td>

                    </Tr>
                    <Tr>
                        <td width="100%">
                            <table width="100%">
                                <tr>
                                    <Td class="myhead" width="40%">End Date</Td>
                                    <Td width="40%"><Input type="text" readonly  class="myTextbox5" name="enddate" id="datepicker1" maxlength=100  style="width:120px"  ></Td>
                                    <td width="20%"></td>
                                </tr>
                            </table>
                        </td>

                    </Tr>
                    <% for (int i = 0; i < allViewarr.size(); i++) {%>
                    <tr>
                        <td width="100%">
                            <table width="100%">
                                <tr>
                                    <Td class="myhead" width="40%">View By<%=i + 1%></Td>
                                    <Td width="40%"><input type=text id="allView[<%=i%>]" name="allView[<%=i%>]"  style="width:120px" autocomplete="off" onfocus="createViewval('allView[<%=i%>]','allView[<%=i%>]suggestList1','<%=allViewarr.get(i)%>','allView')">
                                        <div id="allView[<%=i%>]suggestList1" class="viewDiv" name="viewDiv" style='display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width: 0px;'></div>
                        </Td>
                                    <td width="20%"></td>
                    </tr>
                            </table>
                        </td>
                    </tr>
                    <%}%>
                    <tr>
                        <td width="100%">
                            <table width="100%">
                                <tr>
                                    <Td class="myhead" width="40%">Measure</Td>
                                    <Td width="40%"><select name="measure" id="measure" class="myTextbox5"  style="width:120px">
                               <!-- <%  //if (CEParr != null && CEParr.size() != 0) {
                    //for (int i = REParr.size(); i < displayColumns.size(); i++) { %>
                               <!-- <option value="/<%//=displayColumns.get(i)%>;<%//=displayLabels.get(i)%>"><%//=displayLabels.get(i)%></option>
                                <%//}%>-->
                              
      <% for (int i = 0; i < MeasuresNamesarr.size(); i++) {%>
                                <option value="<%=Measuresarr.get(i)%>;<%=MeasuresNamesarr.get(i)%>"><%=MeasuresNamesarr.get(i)%></option>
                                <%}%>
                                
                        </Td>
                                    <td width="20%"></td>
                    </tr>
                            </table>
                        </td>
                    </tr>

                    <Tr>
                        <td width="100%">
                            <table width="100%">
                                <tr>
                                    <Td class="myhead" width="40%">Email/Mobile</Td>
                                    <Td width="30%">
                                <Select name="EorM" id="EorM" class="myTextbox5"  style="width:120px" onchange="changeEorM()">
                                <Option value="Email">Email</Option>
                                <Option value="Mobile">Mobile</Option>
                                </Select>
                                    </Td>
                                    <td width="30%"></td>
                                </tr>
                            </table>
                        </td>
                    </Tr>

                    <Tr>
                        <td width="100%">
                            <table width="100%">
                                <tr>
                                    <Td class="myhead" width="40%">To Whom</Td>
                                    <Td width="30%">
                                      <div id="mainemaildiv" name="emaildiv">
                                       <input type=text name=toAddress id="toAddress" class="myTextBox5" style="width:120px" autocomplete="off" onfocus="selId('toAddress','emaildiv','qq')">
                                        <div id="emaildiv" style="display:none;height:100px;overflow:auto" ></div>
                                      </div>
                                      <div  id="mainsmsdiv"style="display:none">
                                              <input type=text name="toAddresssms" id="toAddresssms" class="myTextBox5" style="width:120px" >
                                      </div>


                        </Td>
                                    <td width="30%"></td>
                                </tr>
                            </table>
                        </td>
                    </Tr>
                    <Tr>
                        <td width="100%">
                            <table width="100%">
                                <tr>
                                    <Td class="myhead" width="40%">When</Td>
                                    <Td width="30%">
                            <Select name="selectwhen" id="selectwhen" class="myTextbox5"  style="width:120px" onchange="addHighRisk(this)">
                                <Option value=">">></Option>
                                <Option value="<"><</Option>
                                <Option value=">=">>=</Option>
                                <Option value="<="><=</Option>
                                <Option value="=">=</Option>
                                <Option value="between">between</Option>
                            </Select>
                        </Td>
                                    <Td WIDTH="30%">
                                        <div id="singleRisk" >
                                <Input type="text"  class="myTextbox3" name="when" id="when" style="width:62px" >
                            </div>
                                        <div id="doubleRisk" style="display:none">
                                <Input type="text"  class="myTextbox3" name="box1" id="box1" style="width:62px" >
                                <Input type="text"  class="myTextbox3" name="box2" id="box2" style="width:62px" >
                            </div>
                        </Td>
                                </tr>
                            </table>
                        </td>



                    </Tr>

                </Table>
                </center>
                  <%  %>

                <input type="button" class="navtitle-hover" value="Save" onclick="goTrackerSave('pbSaveTrackerDetails.jsp')">
                <%--  &nbsp; &nbsp;   &nbsp;   &nbsp;   &nbsp;   &nbsp;   &nbsp;   &nbsp;   &nbsp;
                  &nbsp; &nbsp;   &nbsp;   &nbsp;   &nbsp;   &nbsp;   &nbsp;   &nbsp;   &nbsp; --%>
                <input type="hidden" name="h" id="h" value="<%=request.getContextPath()%>">
                <input type="hidden" name="url" id="url" value="<%=completeurl%>">
                <input type="hidden" name="REPORTID" id="REPORTID" value="<%=reportId%>">
                <input type="hidden" name="viewCount" id="viewCount" value="<%=allViewarr.size()%>">
                <input type="hidden" name="vieyByids" id="vieyByids" value="<%=vieyByids%>">
            </Form>
        </Center>
        <%
            } catch (Exception e) {
                e.printStackTrace();
            }

        %>

    </Body>
</Html>

















