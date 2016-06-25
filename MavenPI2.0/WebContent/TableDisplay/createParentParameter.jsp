
<%@page import="prg.business.group.BusinessGroupDAO,prg.db.Container,java.util.HashMap,java.util.Set,com.progen.report.PbReportCollection"%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="java.sql.*,utils.db.*,utils.db.ProgenConnection,prg.db.PbReturnObject,prg.db.PbDb,com.progen.datadisplay.db.PbDataDisplayBeanDb"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
            String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }

  String reportId = request.getParameter("reportId");
                    String viewbyId = request.getParameter("viewbyId");
                                        String cpath = request.getContextPath();
            String elementId = request.getParameter("elementId");
            String isDimSeg = request.getParameter("isDimSeg");
                    PbReturnObject parentvaluespbro = null;
                    PbDb pbdb = new PbDb();
                    String GroupName="";
                            Container container = Container.getContainerFromSession(request, reportId);
        String vewdetssql = "SELECT GRP_ID, FOLDER_ID, FOLDER_NAME, SUB_FOLDER_ID, SUB_FOLDER_NAME, SUB_FOLDER_TYPE, SUB_FOLDER_TAB_ID, IS_DIMENSION, IS_FACT, IS_BUCKET, DISP_NAME," +
                " QRY_DIM_ID, DIM_ID, DIM_TAB_ID, DIM_NAME, ELEMENT_ID, BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME," +
                " USER_COL_NAME, USER_COL_DESC, USER_COL_TYPE, REF_ELEMENT_ID, REF_ELEMENT_TYPE, MEMBER_ID, MEMBER_NAME" +
                ", DENOM_QUERY, BUSS_TABLE_NAME, CONNECTION_ID, AGGREGATION_TYPE, ACTUAL_COL_FORMULA, USE_REPORT_FLAG, " +
                "REFFERED_ELEMENTS FROM PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID=" + viewbyId;
        PbReturnObject vewdetspbro = pbdb.execSelectSQL(vewdetssql);
        String memId = String.valueOf(vewdetspbro.getFieldValueInt(0, "MEMBER_ID"));
        String dimId = String.valueOf(vewdetspbro.getFieldValueInt(0, "DIM_ID"));
        String connectionId = String.valueOf(vewdetspbro.getFieldValueInt(0, "CONNECTION_ID"));
        String tableName = vewdetspbro.getFieldValueString(0, "BUSS_TABLE_NAME");
        String colName = vewdetspbro.getFieldValueString(0, "BUSS_COL_NAME");
        String colId = String.valueOf(vewdetspbro.getFieldValueInt(0, "BUSS_COL_ID"));
        String tabId = String.valueOf(vewdetspbro.getFieldValueInt(0, "BUSS_TABLE_ID"));
        String colType = vewdetspbro.getFieldValueString(0, "USER_COL_TYPE");
        String contextPath=request.getContextPath();

%>
<html>
    <head>

        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
<!--         <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.3.2.min.js"></script>-->
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/ReportCss.css" rel="stylesheet" />
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/querydesigner/JS/jquery.columnfilters.js"></script>-->
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/reportDesign.js"></script>-->
<!--        <script src="<%=contextPath%>/jquery.simplemodal-1.1.1.js" type="text/javascript"></script>-->
        <script src="<%=contextPath%>/jquery.contextMenu.js" type="text/javascript"></script>
<!--        <link href="<%=request.getContextPath()%>/myStyles.css" rel="stylesheet" type="text/css">
        <link href="<%=request.getContextPath()%>/StyleSheet.css" rel="stylesheet" type="text/css" />
        <link href="<%=request.getContextPath()%>/confirm.css" rel="stylesheet" type="text/css" />-->
        <link href="<%=contextPath%>/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        
        <style>
            /*            .btn {
                 font-family: Verdana, Arial, tahoma, sans-serif;
                 font-size:11px;
                 background-color: #B5B5B5;
                 color:#454545;
                 font-weight:600;
                 height:22px;
                 text-decoration:none;
                 cursor: pointer;
                 border-bottom: 1px solid #999999;
                 border-right: 1px solid #999999;
                 border-left: 1px solid #F5F5F5;
                 border-top:1px solid #F5F5F5;
                 margin:2px;
             }*/

            .black_overlay{
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 200%;
                height: 200%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                filter:alpha(opacity=50);
                overflow:auto;
            }




            .white_content1 {
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 700px;
                height:600px;
                padding: 16px;
                border: 0px white;
                background-color: white;
                z-index:1002;


            }
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
            /*            #wrapper { display: inline;}*/
            #country { width: 160px; }
            /*            #country1 { width: 160px; }*/

            /*            .suggestdiv{
                            display:none;
                            width:auto;
                            height:auto;
                            background-color:#ffffff;
                            overflow:auto;
                            position:absolute;
                            text-align:left;
                            border:1px solid #000000;
                            border-top-width: 0px;
                        }*/
        </style>

    </head>
    <body id="mainBody">
        <center>
            <form name="myForm" id="myForm" method="post">
               <br>
                <br>
                <div id="headings"  align="center" border="0">
<!--                    <div>-->
                        <table>
                            <tr>
                                <td><b>Dimension Segment Name:</b>&nbsp;&nbsp;</td>
                                <td> <input type="text" name="segParmVal" id="segParamVal" value="" style="width:120px" class="myTextbox5"/></td>
                                <td>&nbsp;&nbsp;</td>
                            </tr>
                        </table>
<!--                    </div>-->
                    <br>
                    <table align="center" border="0" id="addTable">

                        <tr>

                            <th class="prgtableheader" >Group Members</th>
                            <th class="prgtableheader">Member Values</th>

                        </tr>
                       <%for(int i=0;i<2;i++){%>

                        <tr>
                            <td><input type="text"  id="parent[<%=i%>]" name="parent[<%=i%>]" class="myTextbox5" style="width:120px" maxlength="255" value=""></td>

                            <Td>
                                <input type="text" class="myTextbox5" id="keyValue[<%=i%>]" name="keyValue[<%=i%>]"  autocomplete="off" onkeyup="reChange(event,'keyValue[<%=i%>]','keyValue[<%=i%>]suggestList1','<%=colName%>','<%=tableName%>','<%=connectionId%>','selectName<%=i%>','<%=cpath%>')" value="" size="45px">

                            </Td>
                            <td>
                                 <a href="javascript:void(0)" onclick="createParentnew('keyValue[<%=i%>]','keyValue[<%=i%>]suggestList1','<%=colName%>','<%=tableName%>','<%=connectionId%>')">Edit/Add</a>
                                <div id="tbox"></div>
                            </td>
                        </tr>

                        <%}%>


                    </table></div><br>
                <div id="buttons"><table><tr>
                            <td><input type="button"  value="Save" class="navtitle-hover" onclick="saveGrp('addTable','<%=colName%>','<%=tableName%>','<%=connectionId%>','<%=cpath%>','<%=reportId%>')"></td>
                            <td><input type="button"  value="Add Row" class="navtitle-hover" onclick="addParentSingleRow('addTable','<%=colName%>','<%=tableName%>','<%=connectionId%>','<%=cpath%>')"/></td>
                            <td><input type="button"   value="Delete Row" class="navtitle-hover" onclick="deleteParentRow('addTable')" /></td>
                        </tr>
                    </table>
                </div>
                <input type="hidden" id="tabRowCount" name="tabRowCount" class="myTextbox5"  maxlength="255" >
                <input type="hidden" id="tableName" name="tableName" class="myTextbox5"  maxlength="255" value="<%=tableName%>">
                <input type="hidden" id="colName" name="colName" class="myTextbox5"  maxlength="255" value="<%=colName%>">
                <input type="hidden" id="colType" name="colType" class="myTextbox5"  maxlength="255" value="<%=colType%>">
                <input type="hidden" id="tabId" name="tabId" class="myTextbox5"  maxlength="255" value="<%=tabId%>">
                <input type="hidden" id="colId" name="colId" class="myTextbox5"  maxlength="255" value="<%=colId%>">
                <input type="hidden" id="connectionId" name="connectionId" class="myTextbox5"  maxlength="255" value="<%=connectionId%>">
                <input type="hidden" id="memId" name="memId" class="myTextbox5"  maxlength="255" value="<%=memId%>">
                <input type="hidden" id="dimId" name="dimId" class="myTextbox5"  maxlength="255" value="<%=dimId%>">
                <input type="hidden" id="viewbyId" name="viewbyId" class="myTextbox5"  maxlength="255" value="<%=viewbyId%>">
                <input type="hidden" id="REPORTID" name="REPORTID" class="myTextbox5"  maxlength="255" value="<%=reportId%>">
                <input type="hidden" id="elementId" name="elementId" class="myTextbox5"  maxlength="255" value="<%=elementId%>">
                <input type="hidden" id="isDimSeg" name="isDimSeg" class="myTextbox5"  maxlength="255" value="<%=isDimSeg%>">
                <input type="hidden" value="<%=cpath%>" id="h">
                <div>
                    <iframe  id="dataDispparentcols" NAME='dataDispparentcols'  class="white_content1" SRC='#'></iframe>
                </div>
                <div id="fade" class="black_overlay"></div>

            </form>

        </center>
<!-- <div id='loadingmetadata' class='loading_image' style="display:none;">
  <img alt=""  id='imgId' src='images/help-loading.gif'  border='0px' style='position:absolute; left: 450px; top: 150px;'/>
</div>-->
<script  type="text/javascript" >
            var tevname="";
            var selIndexValue="";
            var extposition="";
            var html="";
            $(document).ready(function() {
               var  ctxPath='<%=request.getContextPath()%>';
                reportId='<%=reportId %>';
                $.get(ctxPath+'/reportViewer.do?reportBy=getValueSegmentDialog&reportId='+reportId,
                function(data) {
                    var dataJson = eval('('+data+')');
//                    var {"SegmentAllowed":"updated","SegmentName":["seg1","seg2"],"LowerLimit":["AP1;AP2","AS1"]}
                if(dataJson.SegmentAllowed=="updated"){

                    var segmentName=(dataJson.SegmentName);
                    var limit=(dataJson.LowerLimit);
                    html = html+"<tr>"+

                            "<th class='prgtableheader' >'Group Members'</th>"+
                            "<th class='prgtableheader'>Member Values</th>"+

                        "</tr>";
                   for(var i=0;i<dataJson.SegmentName.length;i++){

                   var temp='keyValue['+i+']';
                   var temp2='keyValue['+i+']suggestList1'
                   html=html+"<tr>"+
                        "<td>"+
                            "<input type= text name='parent["+i+"]' value='"+segmentName[i]+"' id='parent["+i+"]' class='myTextbox5' style='width:120px' maxlength='255' >"+
                        "</td>"+
                        "<td>"+
                            "<input type=text name='keyValue["+i+"]' value='"+limit[i]+"' id='keyValue["+i+"]' class='myTextbox5' autocomplete='off' onkeyup='reChange('event','keyValue["+i+"]','keyValue["+i+"]suggestList1','<%=colName%>','<%=tableName%>','<%=connectionId%>','selectName"+i+",'<%=cpath%>')' value=''>"+
                        "</td>"+
                        "<td>"+
                            "<a onclick=createParentnew('"+temp+"','"+temp2+"','<%=colName%>','<%=tableName%>','<%=connectionId%>') href='javascript:void(0)' > Edit/Add </a>"+
                            "<div id='tbox'></div>"+
                        "</td>"+
                    "</tr>";
                }
              $("#addTable").html(html);
                }

                });

            });

//
//            function gogrpDesc(name){
//                //alert('desc')
//                document.getElementById('grpDesc').value = name.value;
//            }

            function addParentSingleRow(tableID,colName1,tableName1,connectionId1,cpath)
            {
                var table = document.getElementById(tableID);

                var rowCount = table.rows.length;
                totalRowCount = rowCount;
                var row = table.insertRow(rowCount);

                row.id = rowCount;


                var cell1 = row.insertCell(0);
                var element1 = document.createElement("input");
                element1.type = "text";
                extposition=(rowCount-1);
                element1.name = "parent["+extposition+"]";
                element1.id = "parent["+extposition+"]";
                element1.className="myTextbox5";
                element1.style.width="120px";
                cell1.appendChild(element1);
                var obj=document.getElementById('keyValue[0]');


                var cell3 = row.insertCell(1);
                var lengthElement2 = document.createElement("input");
                lengthElement2.type = "text";
                lengthElement2.name = "keyValue["+extposition+"]";
                lengthElement2.id = "keyValue["+extposition+"]";
                lengthElement2.className="myTextbox5";
                lengthElement2.style.width="";
                lengthElement2.autocomplete="off";
                lengthElement2.onkeyup=function(event){
                    reChange(event,'keyValue['+extposition+']','keyValue['+extposition+']suggestList1',colName1,tableName1,connectionId1,'selectName'+extposition,cpath);

                }
                cell3.appendChild(lengthElement2);
                var cell2 = row.insertCell(2);


                var Str=" <a href=\"javascript:Void(0)\" onClick=\"createParentnew('keyValue["+extposition+"]','keyValue["+extposition+"]suggestList1','"+colName1+"','"+tableName1+"','"+connectionId1+"')\">Edit/Add</a>";
                /*  "\ <select id=\"selectName"+extposition+"\" name=\"selectName"+extposition+"\" onChange=\"createParentnew(this.id,'keyValue["+extposition+"]','keyValue["+extposition+"]suggestList1','"+colName1+"','"+tableName1+"','"+connectionId1+"')\">"+
                    "<option >----Select-----</option>"+
                    "<option  value=\"Like\"   autocomplete=\"off\" >Like </option>"+
                    "<option value=\"Distinct\"    autocomplete=\"off\" >Distinct</option>"+
                    "<option  value=\"StartsWith\"   autocomplete=\"off\" >StartsWith </option>"+
                    "<option value=\"EndsWith\"    autocomplete=\"off\" >EndsWith</option>"+

                    "</select>";*/
                cell2.innerHTML=Str;

//                var rown = table.insertRow(rowCount+1);
//                var cell1n = rown.insertCell(0);
//
//                var cell2n = rown.insertCell(1);
//                var ajaxDiv = document.createElement('div');
//
//                ajaxDiv.id = "keyValue["+extposition+"]suggestList1";
//                ajaxDiv.style.display="none";
//                ajaxDiv.style.width="auto";
//                ajaxDiv.style.height="auto";
//                ajaxDiv.style.backgroundColor="#ffffff";
//                ajaxDiv.style.overflow="auto";
//                ajaxDiv.style.position="absolute";
//                ajaxDiv.style.border="1px";
//                ajaxDiv.style.textAlign="left";
//                ajaxDiv.style.border="1px solid #000000";
//                ajaxDiv.style.borderTop="0px";
//
//                cell2n.appendChild(ajaxDiv);

            }

            function deleteParentRow(tableID) {
                try {
                    var table = document.getElementById(tableID);
                    var rowCount = table.rows.length;
                    //alert(rowCount+' in delete ')
                    if(rowCount > 2) {
                        table.deleteRow(rowCount - 1);
                    }

                }catch(e) {
                    alert(e);
                }
            }
            function cancelGrp()
            {
                parent.cancelGrp();
            }


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
            var tablename="";
            var colName="";
            var connectionId="";
            var nexrowId=0;

            window.onload = function()
            {
                //alert("Main");
                //document.getElementById("suggestList").style.display = "none";
                // document.getElementById("suggestList1").style.display = "none";
                //  document.myForm.onsubmit = function(){return false;};
            };

            function sample()
            {

                document.getElementById("keyValue[0]").style.display = "none";

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


            function selId(id1,id2,actcolName,tabName,conId)
            {

                tid=id1;
                did=id2;
                tablename=tabName;
                colName=actcolName;
                connectionId=conId;
                if(tid=='country')
                {
                    preservedvalue="";
                }
                //alert("values set are "+tid+ "  " +did)
                document.getElementById(did).style.display = "none";
                document.getElementById(tid).onkeyup = function(e)
                {
                    //alert("value is "+document.getElementById(tid).value);
                    if(document.getElementById(tid).value.indexOf('ALL')<0)
                    {
                        checkKey(e, this);

                        // checkKey(e, document.getElementById(tid));
                    }

                };

                // checkKey(e, document.getElementById(tid));
                //alert(1);
                document.onclick = checkClick;

            }
            function selId1(id1,id2,actcolName,tabName,conId,imgId)
            {

                tid=id1;
                did=id2;
                tablename=tabName;
                colName=actcolName;
                connectionId=conId;
                if(tid=='country')
                {
                    preservedvalue="";
                }
                //alert("values set are "+tid+ "  " +did)
                document.getElementById(did).style.display = "none";
                document.getElementById(tid).onclick = function(e)
                {
                    //alert("value is "+document.getElementById(tid).value);
                    if(document.getElementById(imgId).value.indexOf('ALL')<0)
                    {
                        checkKey(e, this);

                        checkKey(e, document.getElementById(tid));
                    }

                };

                // checkKey(e, document.getElementById(tid));
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
                // alert("total names "+names[j]+" "+j)names.length-1
                var tablenew = document.getElementById('addTable');

                var rowCountnew = tablenew.rows.length-1;
                var checkcount=rowCountnew/2;
                // alert(checkcount)
                for(var j=0;j<names.length-1;j++)
                {
                    var g=0;
                    for(var i=0;i<checkcount;i++){

                        var checkkeyId=document.getElementById('keyValue['+i+']');
                        //  alert(checkkeyId.value+"---"+names[j])
                        if(checkkeyId.value.indexOf(names[j])<0)
                        {

                            g=g+1;


                        }

                    }
                    if(g==checkcount){
                        names1.push(names[j]);
                    }
                }
                // alert("length is "+names1.length)
                var suggestItem = document.createElement('table');
                // var table=document.getElementById(suggestItem);
                var str="";
                for(var i=0; i < names1.length; i++)
                {
                    if(i%2==0){
                        var val="'"+names1[i]+"'";

                        str+='<tr>';
                        str+='<td class="suggestLink" id="resultlist'+i+'" onmouseover="selectItem('+this+')" onmouseout ="unselectItem('+this+')" onclick ="setCountry('+val+')">';
                        str+= names1[i];
                        str+='</td>'


                    }else{
                        var val="'"+names1[i]+"'";

                        str+='<td class="suggestLink" id="resultlist'+i+'" onmouseover="selectItem('+this+')" onmouseout ="unselectItem('+this+')" onclick ="setCountry('+val+')">';
                        str+= names1[i];
                        str+='</td></tr>'

                    }

                }

                str+='</table>'

                suggestItem.innerHTML=str;
                // alert('str---'+suggestItem.innerHTML)
                suggestList.style.height="100px";
                suggestList.style.overflow="scrollHeight";
                suggestList.appendChild(suggestItem);
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

                //alert("url is "+url);
                var payload = "q="+country.value+"&tableName="+tablename+"&colName="+colName+"&connectionId="+connectionId;
                url=ctxPath+"/ParentGrpValuesajax";
                // alert("PayLoad is "+payload);

                sendRequest(url, payload);
            }

            function checkKey(e, obj)
            {

                var country = document.getElementById(tid);
                // alert("In CheckKey")
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
                    // alert("In obj")
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

            function stateChanged()
            {

                if (xmlHttp.readyState==4)
                {
                    handleResponse1(xmlHttp.responseText);
                    //alert("In state Changed ready state is 4"+xmlHttp.responseText);
                }
            }
            function saveGrp(tableId1,colName1,tableName1,connectionId1,cpath,reportId){


                var table = document.getElementById(tableId1);
                var rcount1 = table.rows.length;
                var dupcount=0;
                var colcount=0;
                var cname="";
                var cname1="";
                // alert(rcount1);
                var extrowcount=(rcount1-1);
                //alert(extrowcount)
                // alert(document.getElementById('grpName'));
//                if(document.getElementById('grpName').value==''){
//                    alert('Please enter Group Name')
//                }
//                else{


                    var countall=0;
                    for(var v=0;v<extrowcount;v++)
                    {
                        if(document.getElementById('parent['+v+']').value=="" ||document.getElementById('keyValue['+v+']').value==""){
                            countall=1;
                        }

                    }
                    if(countall==0){
                        //  alert(extrowcount)
                        for(var i=0;i<extrowcount;i++)
                        {
                            // alert(document.getElementById('key['+i+']').value.split(',')[0])

                            cname=document.getElementById('parent['+i+']').value;
                            // alert(cname)
                            for(var k=i+1;k<extrowcount;k++)
                            {
                                cname1=document.getElementById('parent['+k+']').value;
                                if(cname==cname1){
                                    dupcount++;
                                    break;
                                }
                                else
                                    colcount++;

                            }
                        }
                        var validEntry = true;

                        if(extrowcount>1 && dupcount>0 )
                                validEntry = false;

                        if ( validEntry )
                            {
                                parent.$("#dispgrpAnalysis").dialog('close');
                                parent.$("#loadingmetadata").show();
                               document.getElementById('tabRowCount').value=extrowcount;
                                document.myForm.action=cpath+'/reportViewer.do?reportBy=createSegmentByValue';
                                // parent.document.getElementById("iframe1").src="<%=request.getContextPath()%>/TableDisplay/pbDisplay.jsp?tabId=reportId"

                                //document.myForm.submit();
                                $.post(cpath+'/reportViewer.do?reportBy=createSegmentByValue', $("#myForm").serialize() ,
                                function(data){
                                    if(data=='true'){
                                     alert("Segment created successfully. Please add as dimension from parameter region to use it")
                                    }
                                    else {
                                        alert("Error in Creation !!")
                                    }
                                    parent.$("#loadingmetadata").hide();
                                    parent.savecancelGrp(reportId);
                                    parent.document.getElementById("iframe1").src="<%=request.getContextPath()%>/TableDisplay/pbDisplay.jsp?tabId="+reportId;
                            });

                            }
                            else
                                alert('Please select different names for the Groups');

<%--                        if((extrowcount)>1){
                            if(dupcount>0){
                                alert('No Two Names are Same Please Select Different Name');
                            }
                            else{
                                document.getElementById('tabRowCount').value=extrowcount;
                                document.myForm.action=cpath+'/reportViewer.do?reportBy=createSegmentByValue';
                                // parent.document.getElementById("iframe1").src="<%=request.getContextPath()%>/TableDisplay/pbDisplay.jsp?tabId=reportId"

                                //document.myForm.submit();
                                $.post(cpath+'/reportViewer.do?reportBy=createSegmentByValue', $("#myForm").serialize() ,
                                function(data){
                                    parent.savecancelGrp(reportId);
                                    parent.document.getElementById("iframe1").src="<%=request.getContextPath()%>/TableDisplay/pbDisplay.jsp?tabId="+reportId;
                                });


                            }
                        }else{
                            document.getElementById('tabRowCount').value=extrowcount;
                            document.myForm.action=cpath+'/reportViewer.do?reportBy=createSegmentByValue';
                            // parent.document.getElementById("iframe1").src="<%=request.getContextPath()%>/TableDisplay/pbDisplay.jsp?tabId=reportid"
                            //document.myForm.submit();
                            $.post(cpath+'/reportViewer.do?reportBy=createSegmentByValue', $("#myForm").serialize() ,
                                function(data){
                                    parent.savecancelGrp(reportId);
                                    parent.document.getElementById("iframe1").src="<%=request.getContextPath()%>/TableDisplay/pbDisplay.jsp?tabId="+reportId;
                                });

                        }
--%>
        }else{
                        alert('Please enter all Parent and Key values');
                    }
                }
           // }
            function savecancelGrp()
            { //alert('in jsp');
                parent.savecancelGrp();
            }

//            function duplicateCheck(grpName,tabId,cpath,connectionId,tableName){
//                var gname=grpName.value;
//                // alert(cpath+'/DuplicateGroup?grpName='+gname+'&tabId='+tabId)
//                $.ajax({
//                    url: cpath+'/DuplicateGroup?grpName='+gname+'&tabId='+tabId+"&connectionId="+connectionId+"&tableName="+tableName,
//                    success: function(data){
//                        //alert(data)
//                        if(data==0){
//
//                        }else{
//                            alert('Please Enter Different Group Name');
//                        }
//
//                    }
//                });
//
//            }




            //sreekanth start
            function createParentnew(id1,id2,actcolName,tabName,conId){
                // alert("hghghg")
                var tablenew = document.getElementById('addTable');
                var rowCountnew = tablenew.rows.length-1;
               var checkcount=rowCountnew/2;
                //selIndexValue = document.getElementById(selObj).value;
                //   var num = "selectName".length;
                //var textboxnum=selObj.substring(num,selObj.length);
                // alert('hi jsp')
                var colList="";
                // if(selIndexValue =="Distinct"){
                for(var k=0;k<rowCountnew;k++){
                    var checkkeyId=document.getElementById('keyValue['+k+']');
                    if(document.getElementById(id1)!=checkkeyId){
                        if(checkkeyId.value!=""){
                            colList+=";"+checkkeyId.value;

                        }
                    }

                }
                if(colList!=""){
                    colList=colList.substring(1);
                }
                var orival=document.getElementById(id1).value;
                var colType=document.getElementById("colType").value;
                var f=document.getElementById('dataDispparentcols');
                var eleId = <%=viewbyId%>;
                var s="pbgroupingparentParameterColumns.jsp?tableName="+tabName+"&colName="+actcolName+"&connectionId="+conId+"&id1="+id1+"&id2="+id2+"&colList="+colList+"&orival="+orival+"&colType="+colType+"&eleId="+eleId;
                f.src=s;
                document.getElementById('dataDispparentcols').style.display='block';
                document.getElementById('fade').style.display='block';
                document.getElementById('mainBody').style.overflow='hidden';
                /*  }else if(selIndexValue == "Like"){
                    document.getElementById('keyValue['+textboxnum+']').value = "";


                }else if(selIndexValue == "StartsWith"){
                    document.getElementById('keyValue['+textboxnum+']').value = "";


                }else if(selIndexValue == "EndsWith"){
                    document.getElementById('keyValue['+textboxnum+']').value = "";


                }*/

            }

            function reChange(evt,id1,id2,actcolName,tabName,conId,selId,cpath) {

                var selIndexValue=document.getElementById(selId).value;
                var charCode = (evt.which) ? evt.which : evt.keyCode;

                if(charCode == '13'){
                    /*
                    if(selIndexValue =="Like"){

                        var tablenew = document.getElementById('addTable');
                        var rowCountnew = tablenew.rows.length-1;
                        var colList="";
                        var checkcount=rowCountnew/2;
                        for(var k=0;k<checkcount;k++){
                            var checkkeyId=document.getElementById('keyValue['+k+']');
                            if(document.getElementById(id1)!=checkkeyId){
                                if(checkkeyId.value!=""){
                                    colList+=","+checkkeyId.value;
                                }
                                var orival=document.getElementById(id1).value;
                                var strReplaceAll = orival;
                                var intIndexOfMatch = strReplaceAll.indexOf( "%" );
                                while (intIndexOfMatch != -1){
                                    strReplaceAll = strReplaceAll.replace( "%", "~" )
                                    intIndexOfMatch = strReplaceAll.indexOf( "%" );
                                }
                                orival =strReplaceAll
                                var tvalue=document.getElementById('keyValue['+k+']').value;

                                var colType=document.getElementById("colType").value;
                            }

                        }
                      //  alert('reportViewer.do?reportBy=testQuery&tableName='+tabName+'&colName='+actcolName+'&connectionId='+conId+'&id1='+id1+'&id2='+id2+'&colList='+colList+'&orivalue='+orival+'&colType='+colType)
                        $.ajax({
                            url: ''+cpath+'/reportViewer.do?reportBy=testQuery&tableName='+tabName+'&colName='+actcolName+'&connectionId='+conId+'&id1='+id1+'&id2='+id2+'&colList='+colList+'&orivalue='+orival+'&colType='+colType,
                            success: function(data) {
                                var retlist=document.getElementById(id1).value="";
                                var sx= data;
                                sx=sx.replace('[' ,"" );
                                sx=sx.replace(']' ,"" );
                               // alert('sx'+sx);
                                document.getElementById(id1).value=sx;
                            }
                        });
                    }*/
                    // else if(selIndexValue =="In"){
                    var selectId=id1.split('[');
                    createParentnew(id1,id2,actcolName,tabName,conId);
                    /* }
                     else if(selIndexValue =="StartsWith"){
                         var tablenew = document.getElementById('addTable');
                        var rowCountnew = tablenew.rows.length-1;
                        var colList="";
                        var checkcount=rowCountnew/2;
                        for(var k=0;k<checkcount;k++){
                            var checkkeyId=document.getElementById('keyValue['+k+']');
                            if(document.getElementById(id1)!=checkkeyId){
                                if(checkkeyId.value!=""){
                                    colList+=","+checkkeyId.value;
                                }
                                var orival=document.getElementById(id1).value;
                                var strReplaceAll = orival;
                                var intIndexOfMatch = strReplaceAll.indexOf( "%" );
                                while (intIndexOfMatch != -1){
                                    strReplaceAll = strReplaceAll.replace( "%", "~" )
                                    intIndexOfMatch = strReplaceAll.indexOf( "%" );
                                }
                                orival =strReplaceAll+"~"
                                var tvalue=document.getElementById('keyValue['+k+']').value;

                                var colType=document.getElementById("colType").value;
                            }

                        }
                       // alert('reportViewer.do?reportBy=testQuery&tableName='+tabName+'&colName='+actcolName+'&connectionId='+conId+'&id1='+id1+'&id2='+id2+'&colList='+colList+'&orivalue='+orival+'&colType='+colType)
                        $.ajax({
                            url: ''+cpath+'/reportViewer.do?reportBy=testQuery&tableName='+tabName+'&colName='+actcolName+'&connectionId='+conId+'&id1='+id1+'&id2='+id2+'&colList='+colList+'&orivalue='+orival+'&colType='+colType,
                            success: function(data) {
                                var retlist=document.getElementById(id1).value="";
                                var sx= data;
                                sx=sx.replace('[' ,"" );
                                sx=sx.replace(']' ,"" );
                               // alert('sx'+sx);
                                document.getElementById(id1).value=sx;
                            }
                        });
                    }      else if(selIndexValue =="EndsWith"){
                         var tablenew = document.getElementById('addTable');
                        var rowCountnew = tablenew.rows.length-1;
                        var colList="";
                        var checkcount=rowCountnew/2;
                        for(var k=0;k<checkcount;k++){
                            var checkkeyId=document.getElementById('keyValue['+k+']');
                            if(document.getElementById(id1)!=checkkeyId){
                                if(checkkeyId.value!=""){
                                    colList+=","+checkkeyId.value;
                                }
                                var orival=document.getElementById(id1).value;
                                var strReplaceAll = orival;
                                var intIndexOfMatch = strReplaceAll.indexOf( "%" );
                                while (intIndexOfMatch != -1){
                                    strReplaceAll = strReplaceAll.replace( "%", "~" )
                                    intIndexOfMatch = strReplaceAll.indexOf( "%" );
                                }
                                orival ="~"+strReplaceAll;
                                var tvalue=document.getElementById('keyValue['+k+']').value;

                                var colType=document.getElementById("colType").value;
                            }

                        }
                       // alert('reportViewer.do?reportBy=testQuery&tableName='+tabName+'&colName='+actcolName+'&connectionId='+conId+'&id1='+id1+'&id2='+id2+'&colList='+colList+'&orivalue='+orival+'&colType='+colType)
                        $.ajax({
                            url: ''+cpath+'/reportViewer.do?reportBy=testQuery&tableName='+tabName+'&colName='+actcolName+'&connectionId='+conId+'&id1='+id1+'&id2='+id2+'&colList='+colList+'&orivalue='+orival+'&colType='+colType,
                            success: function(data) {
                                var retlist=document.getElementById(id1).value="";
                                var sx= data;
                                sx=sx.replace('[' ,"" );
                                sx=sx.replace(']' ,"" );
                              //  alert('sx'+sx);
                                document.getElementById(id1).value=sx;
                            }
                        });
                    }*/

                }
            }




            function cancelGrpnew()
            {
                document.getElementById("dataDispparentcols").style.display='none';
                document.getElementById('fade').style.display='';
                document.getElementById('mainBody').style.overflow='auto';

            }
            function savecancelGrpnew(valList,newid1,newid2)
            {   document.getElementById("dataDispparentcols").style.display='none';
                document.getElementById('fade').style.display='';
                //alert(valList+"--"+newid1+"--"+newid2);
                document.getElementById(newid1).value=valList;

            }
            function savecancelGrpwaitnew(){
                window.location.reload(true);
                document.getElementById('fade1').style.display='';
            }


        </script>
    </body>
</html>

