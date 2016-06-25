
<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbDb,prg.db.PbReturnObject,utils.db.*,java.sql.*,utils.db.ProgenConnection"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
%>
<html>
    <head>

        <link href="StyleSheet.css" rel="stylesheet" type="text/css" />
 <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <link href="myStyles.css" rel="stylesheet" type="text/css">
     

    </head>
    <body>
        <%
        String grpId = request.getParameter("grpId");
        String bussTableId = request.getParameter("bussTableId");
        String bussColId = request.getParameter("bussColId");
        String tabName = request.getParameter("tabName");
        String colName = request.getParameter("colName");
        String colType = request.getParameter("colType");


        //con =  ProgenConnection.getInstance().getConnection();
        PbDb pbdb = new PbDb();
          PbReturnObject pbroday =new PbReturnObject();
       String QueryDay = "SELECT BUSS_COLUMN_ID,COLUMN_NAME FROM PRG_GRP_BUSS_TABLE_DETAILS "
                + "where upper(column_type)in ('DATETIME','DATETIME2','DATE','TIMESTAMP','TIMESTAMP WITHOUT TIME ZONE')"
               + " and  BUSS_TABLE_ID=" + bussTableId;
        //.println("QueryDay\t"+QueryDay);
        pbroday = pbdb.execSelectSQL(QueryDay);
        if(pbroday.getRowCount()==0){
         String QueryDay1 = "SELECT BUSS_COLUMN_ID,COLUMN_NAME FROM PRG_GRP_BUSS_TABLE_DETAILS where upper(column_type)='datetime2' and  BUSS_TABLE_ID=" + bussTableId;
        pbroday = pbdb.execSelectSQL(QueryDay1);
        }
        String colNamesday[] = pbroday.getColumnNames();
        String Query = "SELECT BUSS_COLUMN_ID,COLUMN_NAME FROM PRG_GRP_BUSS_TABLE_DETAILS where  BUSS_TABLE_ID=" + bussTableId;
        //.println("Query\t"+Query);
        PbReturnObject pbro = pbdb.execSelectSQL(Query);
        String colNames[] = pbro.getColumnNames();
        //for knowing howmany dimension for this bus group uaed to solve tree view problem only
        String Querydims = "SELECT dim_id from PRG_GRP_DIMENSIONS where GRP_ID="+grpId;
        //.println("Querydims\t"+Querydims);
        PbReturnObject pbrodims = pbdb.execSelectSQL(Querydims);

      String calenderQuery = "SELECT CALENDER_ID, DENOM_TABLE, CALENDER_NAME FROM PRG_CALENDER_SETUP order by CALENDER_ID";
             //.println("calenderQuery\t"+calenderQuery);

            PbReturnObject pbrocalenders = pbdb.execSelectSQL(calenderQuery);
        // pbrocalenders.writeString();

        %>
        <center>
            <form name="myForm" id="myForm" method="post">
                <table >

                    <tr><td>&nbsp;</td></tr>
                    <tr><td>&nbsp;</td></tr>
                    <tr><td>&nbsp;</td></tr>
                     <tr>

                        <td>Calender</td>
                        <td>
                              <select id="calender" name="calender" class="myTextbox5" style="width:150px">
                            <%
                            for(int i=0;i<pbrocalenders.getRowCount();i++){
                            %>
                           <option value="<%=pbrocalenders.getFieldValueInt(i, 0)%>~<%=pbrocalenders.getFieldValueString(i, 1)%>"><%=pbrocalenders.getFieldValueString(i,2)%></option>

                            <%}%>
                              </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="myHead" style="width:150px" >
                            Minimum Time Level
                        </td>


                        <td style="width:58%">
                            <select id="minTimeLevel" name="minTimeLevel" class="myTextbox5" style="width:150px" onchange="changeLevel()">

                                <option value="5">Day</option>
                                <option value="4">Week</option>
                                <option value="3">Month</option>
                                <option value="2">Quarter</option>
                                <option value="1">Year</option>
                            </select>

                        </td>

                    </tr>

                </table>
                <div id="dayDiv">
                    <table>

                        <tr>
                            <td style="width:150px">
                                Date
                            </td>
                            <td style="width:58%">
                                <select id="timeDimKeyValueDay" name="timeDimKeyValueDay" class="myTextbox5" style="width:150px">
                                    <%for (int i = 0; i < pbroday.getRowCount(); i++) {%>
                                    <option value="<%=pbroday.getFieldValueInt(i, colNamesday[0])%>~<%=pbroday.getFieldValueString(i, colNamesday[1])%>"><%=pbroday.getFieldValueString(i, colNamesday[1])%></option>
                                    <%}%>
                                </select>
                            </td>
                        </tr>


                    </table>
                </div>

                <div id="weekDiv" style="display:none;">
                    <table>

                        <tr>
                            <td style="width:150px">
                                Join Type
                            </td>
                            <td style="width:58%">
                                <select id="weekJoinType" name="weekJoinType" class="myTextbox5" style="width:150px" onchange="changeWeekJoin()">

                                    <option value="S">Single</option>
                                    <option value="M">Multiple</option>

                                </select>
                            </td>
                        </tr>
                    </table>
                    <div id="weekSingleDiv"  style="display:none;">
                        <table>

                            <tr>
                                <td style="width:150px">
                                    Week
                                </td>
                                <td style="width:58%">
                                    <select id="timeDimKeyValuesingleWeekno" name="timeDimKeyValuesingleWeekno" class="myTextbox5" style="width:150px">
                                        <%for (int i = 0; i < pbro.getRowCount(); i++) {%>
                                        <option value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                        <%}%>
                                    </select>
                                </td>
                            </tr>
                        </table>

                    </div>


                    <div id="weekMultipleDiv" style="display:none;">

                        <table>

                            <tr>
                                <td style="width:150px">
                                    Week No
                                </td>
                                <td style="width:58%">
                                    <select id="timeDimKeyValueWeekno" name="timeDimKeyValueWeekno" class="myTextbox5" style="width:150px">
                                        <%for (int i = 0; i < pbro.getRowCount(); i++) {%>
                                        <option value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                        <%}%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td style="width:150px">
                                    Week Year
                                </td>
                                <td style="width:58%">
                                    <select id="timeDimKeyValueWeekyr" name="timeDimKeyValueWeekyr" class="myTextbox5" style="width:150px">
                                        <%for (int i = 0; i < pbro.getRowCount(); i++) {%>
                                        <option value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                        <%}%>
                                    </select>
                                </td>
                            </tr>


                        </table>
                    </div>
                </div>
                <div id="monthDiv" style="display:none;">


                    <table>

                        <tr>
                            <td style="width:150px">
                                Join Type
                            </td>
                            <td style="width:58%">
                                <select id="monthJoinType" name="monthJoinType" class="myTextbox5" style="width:150px" onchange="monthFormatChange()">

                                    <option value="S">Single</option>
                                    <option value="M">Multiple</option>
                                    <option value="F">Format</option>

                                </select>
                            </td>
                        </tr>
                    </table>

                    <div id="monthSingleDiv"  style="display:none;">
                        <table>

                            <tr>
                                <td style="width:150px">
                                    Month
                                </td>
                                <td style="width:58%">
                                    <select id="timeDimKeyValuesingleMonthno" name="timeDimKeyValuesingleMonthno" class="myTextbox5" style="width:150px">
                                        <%for (int i = 0; i < pbro.getRowCount(); i++) {%>
                                        <option value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                        <%}%>
                                    </select>
                                </td>
                            </tr>
                        </table>

                    </div>


                    <%--
                    <table>
                        <tr>
                            <td style="width:150px">
                                Format
                            </td>
                            <td style="width:58%">
                                <input type="checkbox" checked id="monthFormatyn" name="monthFormatyn" value="Y" onchange="monthFormatChange()">
                            </td>
                        </tr>
                    </table>
                    --%>
                    <div id="monthFormatDiv" style="display:none;">
                        <table align="center">
                            <tr>
                                <td style="width:150px">
                                    Month
                                </td>
                                <td style="width:58%">
                                    <select id="timeDimKeyValuemonth" name="timeDimKeyValuemonth" class="myTextbox5" style="width:150px">
                                        <%for (int i = 0; i < pbro.getRowCount(); i++) {%>
                                        <option value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                        <%}%>
                                    </select>
                                </td>
                            </tr>
                        </table>
                        <table>
                            <tr>
                                <td style="width:60px">
                                    Pre
                                </td>

                                <td style="width:150px">
                                    &nbsp;&nbsp;  Format
                                </td>
                                <td style="width:60px">
                                    &nbsp;&nbsp; &nbsp;&nbsp;
                                    Post
                                </td>
                            </tr>
                            <tr>
                                <td >
                                    <input type="text" checked id="monthPre" name="monthPre" style="width:70px">
                                </td>
                                <td>
                                    <input type="text"  id="monthFormat" name="monthFormat">
                                </td>
                                <td>
                                    <input type="text"  id="monthPost" name="monthPost" style="width:70px">
                                </td>

                            </tr>


                        </table>
                    </div>

                    <div id="monthNormalDiv" style="display:none;">
                        <table>

                            <tr>
                                <td style="width:150px">
                                    Month No
                                </td>
                                <td style="width:58%">
                                    <select id="timeDimKeyValueMonthno" name="timeDimKeyValueMonthno" class="myTextbox5" style="width:150px">
                                        <%for (int i = 0; i < pbro.getRowCount(); i++) {%>
                                        <option value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                        <%}%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td style="width:150px">
                                    Month Year
                                </td>
                                <td style="width:58%">
                                    <select id="timeDimKeyValueMonthyr" name="timeDimKeyValueMonthyr" class="myTextbox5" style="width:150px">
                                        <%for (int i = 0; i < pbro.getRowCount(); i++) {%>
                                        <option value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                        <%}%>
                                    </select>
                                </td>
                            </tr>

                        </table>
                    </div>


                </div>

                <div id="quarterDiv" style="display:none;">

                    <table>

                        <tr>
                            <td style="width:150px">
                                Join Type
                            </td>
                            <td style="width:58%">
                                <select id="quarterJoinType" name="quarterJoinType" class="myTextbox5" style="width:150px" onchange="quarterFormatChange()">

                                    <option value="S">Single</option>
                                    <option value="M">Multiple</option>

                                </select>
                            </td>
                        </tr>
                    </table>


                    <div id="quarterSingleDiv"  style="display:none;">
                        <table>

                            <tr>
                                <td style="width:150px">
                                    Week
                                </td>
                                <td style="width:58%">
                                    <select id="timeDimKeyValuesingleQuaterno" name="timeDimKeyValuesingleQuaterno" class="myTextbox5" style="width:150px">
                                        <%for (int i = 0; i < pbro.getRowCount(); i++) {%>
                                        <option value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                        <%}%>
                                    </select>
                                </td>
                            </tr>
                        </table>

                    </div>


                    <div id="quarterMultipleDiv" style="display:none;">
                        <table>

                            <tr>
                                <td style="width:150px">
                                    Quarter No
                                </td>
                                <td style="width:58%">
                                    <select id="timeDimKeyValueQuaterno" name="timeDimKeyValueQuaterno" class="myTextbox5" style="width:150px">
                                        <%for (int i = 0; i < pbro.getRowCount(); i++) {%>
                                        <option value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                        <%}%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td style="width:150px">
                                    Quarter Year
                                </td>
                                <td style="width:58%">
                                    <select id="timeDimKeyValueQuateryr" name="timeDimKeyValueQuateryr" class="myTextbox5" style="width:150px">
                                        <%for (int i = 0; i < pbro.getRowCount(); i++) {%>
                                        <option value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                        <%}%>
                                    </select>
                                </td>
                            </tr>


                        </table>
                    </div>
                </div>
                <div id="yearDiv" style="display:none;">
                    <table>

                        <tr>
                            <td style="width:150px">
                                Year
                            </td>
                            <td style="width:58%">
                                <select id="timeDimKeyValueYear" name="timeDimKeyValueYear" class="myTextbox5" style="width:150px">
                                    <%for (int i = 0; i < pbro.getRowCount(); i++) {%>
                                    <option value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                    <%}%>
                                </select>
                            </td>
                        </tr>


                    </table>
                </div>

                <table >
                    <tr>
                        <td align="center"><input type="button" class="btn" value="Save" onclick="saveTimeDim('<%=pbrodims.getRowCount()%>')"></td>
                        <td align="center"><input type="button" class="btn" value="Cancel" onclick="cancelTimeDim()"></td>
                    </tr>
                </table>
                <input type="hidden" name="h" id="h" value="<%=request.getContextPath()%>">
                <input type="hidden" name="grpId" id="grpId" value="<%=grpId%>">
                <input type="hidden" name="bussTableId" id="bussTableId" value="<%=bussTableId%>">
                <input type="hidden" name="bussColId" id="bussColId" value="<%=bussColId%>">
                <input type="hidden" name="tabName" id="tabName" value="<%=tabName%>">
                <input type="hidden" name="colName" id="colName" value="<%=colName%>">
                <input type="hidden" name="colType" id="colType" value="<%=colType%>">
                <input type="hidden" name="monthcheck" id="monthcheck" >
            </form>
        </center>

   <script>
            function saveTimeDim(dimscount)
            {   var minTimeLevel=document.getElementById("minTimeLevel").value;
                if(minTimeLevel==3){
                    if(document.getElementById("monthJoinType").value=="F"){
                        if(document.getElementById("monthFormat").value!=""){

                            var n = document.getElementById("monthFormat").value.length;
                            var numlen=0;
                            for (i=0;i<n; i++){
                                cchar=document.getElementById("monthFormat").value.charAt(i);
                                if ((cchar=='0') ||(cchar=='1')||(cchar=='2')||(cchar=='3')||(cchar=='4')||(cchar=='5')||(cchar=='6')||(cchar=='7') ||(cchar=='8')||(cchar=='9'))
                                {    numlen=1;

                                }
                            }
                                if(numlen==1){
                               alert('Please don\'t enter numeric values  for Format');
                                }
                                else
                                {
                                    if(document.getElementById("monthPre").value=="" && document.getElementById("monthPost").value==""){
                                            alert('Please Select Atleast one format type Pre or Post');
                                        }
                                    else{
                                    document.getElementById("monthcheck").value='Y';
                                    // alert( document.getElementById("monthcheck").value);
                                    $.post("saveTimeDim.do",$("#myForm").serialize(),
                                       function(data){
                                        parent.parentCancelTimeDim1(minTimeLevel,dimscount);

                                      });
//                                    document.myForm.action="saveTimeDim.do";
//                                    document.myForm.submit();
                                   // parent.parentCancelTimeDim1(minTimeLevel,dimscount);
                                }
                                }

                        }else if(document.getElementById("monthPre").value=="" && document.getElementById("monthPost").value==""){
                            alert('Please Enter Atleast one format type Pre or Post');
                        }
                        else{
                            alert(document.getElementById("monthPre").value+'-------'+document.getElementById("monthPost").value)
                            alert('Please enter Format');
                        }
                    }else{
                        document.getElementById("monthcheck").value='N';
                        // alert( document.getElementById("monthcheck").value);
                         $.post("saveTimeDim.do",$("#myForm").serialize(),
                                      function(data){
                                        parent.parentCancelTimeDim1(minTimeLevel,dimscount);

                                      });
//                        document.myForm.action="saveTimeDim.do";
//                        document.myForm.submit();
                       // parent.parentCancelTimeDim1(minTimeLevel,dimscount);
                    }
                }else{
                    if(minTimeLevel==5){

                            if(document.getElementById("timeDimKeyValueDay").value==""){
                                alert('This Fact does not support this Minimum Time Level \nPlease Select other Minnimum Time Level')
                            }else{
                                 $.post("saveTimeDim.do",$("#myForm").serialize(),
                                      function(data){
                                        parent.parentCancelTimeDim1(minTimeLevel,dimscount);
                                  
                                      });
//                                document.myForm.action="saveTimeDim.do";
//
//                                document.myForm.submit();
                               
                            }
                    }else{
                     $.post("saveTimeDim.do",$("#myForm").serialize(),
                                       function(data){
                                        parent.parentCancelTimeDim1(minTimeLevel,dimscount);

                                      });
//                    document.myForm.action="saveTimeDim.do";
//
//                    document.myForm.submit();
//                    parent.parentCancelTimeDim1(minTimeLevel,dimscount);
                    }
                }
            }
            function cancelTimeDim()
            {
                parent.parentCancelTimeDim();
            }

            function changeLevel()
            {
                var minTimeLevel=document.getElementById("minTimeLevel").value;
                if(minTimeLevel==5){
                    document.getElementById("dayDiv").style.display='';
                    document.getElementById("weekDiv").style.display='none';
                    document.getElementById("monthDiv").style.display='none';
                    document.getElementById("quarterDiv").style.display='none';
                    document.getElementById("yearDiv").style.display='none';
                }
                else if(minTimeLevel==4){
                    document.getElementById("weekDiv").style.display='';
                    document.getElementById("weekSingleDiv").style.display='';
                    document.getElementById("dayDiv").style.display='none';
                    document.getElementById("monthDiv").style.display='none';
                    document.getElementById("quarterDiv").style.display='none';
                    document.getElementById("yearDiv").style.display='none';
                }
                else if(minTimeLevel==3){
                    document.getElementById("dayDiv").style.display='none';
                    document.getElementById("monthDiv").style.display='';
                    document.getElementById("monthSingleDiv").style.display='';
                    document.getElementById("weekDiv").style.display='none';
                    document.getElementById("quarterDiv").style.display='none';
                    document.getElementById("yearDiv").style.display='none';
                }
                else if(minTimeLevel==2){
                    document.getElementById("quarterDiv").style.display='';
                    document.getElementById("quarterSingleDiv").style.display='';
                    document.getElementById("dayDiv").style.display='none';
                    document.getElementById("monthDiv").style.display='none';
                    document.getElementById("yearDiv").style.display='none';
                    document.getElementById("weekDiv").style.display='none';
                }
                else if(minTimeLevel==1){
                    document.getElementById("yearDiv").style.display='';
                    document.getElementById("dayDiv").style.display='none';
                    document.getElementById("monthDiv").style.display='none';
                    document.getElementById("quarterDiv").style.display='none';
                    document.getElementById("weekDiv").style.display='none';
                }
            }
            function monthFormatChange(){
                if(document.getElementById("monthJoinType").value=="S"){
                    document.getElementById("monthSingleDiv").style.display='';
                    document.getElementById("monthFormatDiv").style.display='none';
                    document.getElementById("monthNormalDiv").style.display='none';
                }else if(document.getElementById("monthJoinType").value=="M"){
                    document.getElementById("monthFormatDiv").style.display='none';
                    document.getElementById("monthNormalDiv").style.display='';
                    document.getElementById("monthSingleDiv").style.display='none';
                }
                else if(document.getElementById("monthJoinType").value=="F"){
                    document.getElementById("monthFormatDiv").style.display='';
                    document.getElementById("monthNormalDiv").style.display='none';
                    document.getElementById("monthSingleDiv").style.display='none';
                }
            }

            function changeWeekJoin(){
                if(document.getElementById("weekJoinType").value=="S"){
                    document.getElementById("weekSingleDiv").style.display='';
                    document.getElementById("weekMultipleDiv").style.display='none';
                }else{
                    document.getElementById("weekSingleDiv").style.display='none';
                    document.getElementById("weekMultipleDiv").style.display='';
                }

            }


            function quarterFormatChange(){
                if(document.getElementById("quarterJoinType").value=="S"){
                    document.getElementById("quarterSingleDiv").style.display='';
                    document.getElementById("quarterMultipleDiv").style.display='none';
                }else{
                    document.getElementById("quarterSingleDiv").style.display='none';
                    document.getElementById("quarterMultipleDiv").style.display='';
                }

            }




            var xmlHttp2;

            function timeDimKey(str)
            {


                if (str.length==0)
                {
                    document.getElementById("txtHint").innerHTML="";

                    return;
                }
                xmlHttp2=GetXmlHttpObject();
                if (xmlHttp2==null)
                {
                    alert ("Your browser does not support AJAX!");
                    return;
                }
                ctxPath=document.getElementById("h").value;
                var minTimeLevel=document.getElementById("minTimeLevel").value;
                var url=ctxPath+"/TimeDimKeyValue";
                url=url+"?bussTableId="+str+"&minTimeLevel="+minTimeLevel;
                alert('url--->'+url);
                xmlHttp2.onreadystatechange=stateChangedTimeDimKey;
                xmlHttp2.open("GET",url,true);
                xmlHttp2.send(null);
            }


            function stateChangedTimeDimKey()
            {
                // alert('hi in target')

                if (xmlHttp2.readyState==4)
                {
                    var output=xmlHttp2.responseText;
                    alert("output is "+output);
                    var timeKeyLevel=output.split("\n");
                    obj=document.myForm.timeDimKeyValue;
                    document.getElementById('timeDimKeyValue').options.length=0;

                    for(var i=obj.length-1;i>=0;i--)
                    {
                        obj.options[i] = null;

                    }



                    for(var j=0;j<timeKeyLevel.length-1;j++)
                    {


                        obj.options[j] = new Option(timeKeyLevel[j].split(",")[1],timeKeyLevel[j].split(",")[0]);

                    }

                }
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


        </script>
    </body>
</html>