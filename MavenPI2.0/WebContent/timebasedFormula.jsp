<%--
    Document   : timebasedFormula
    Created on : Jan 16, 2010, 6:01:54 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>TimeBased Formula Page</title>
        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <style type="text/css">
            *{font:11px verdana;}
        </style>
       
    </head>
    <body>

        <%
            //groupdetails(Action class parmater)
            String connId = request.getParameter("connId");
            //////////////////////////////////////////.println.println("connid" + connId);
            String bussTableId = request.getParameter("bussTableId");
            // ////////////////////////////////////////.println.println("bussTableId" + bussTableId);
            String grpId = request.getParameter("grpId");
            // ////////////////////////////////////.println.println("grpId" + grpId);
            String bussColId = request.getParameter("bussColId");
            String colName=request.getParameter("colName");
            ////////////////////////////.println.println("colname"+colName);
            String tablename = request.getParameter("tabName");
            //////////////////////////.println.println("tablename"+tablename);
%>

<form action="" method="post" name="timebasedform" style="width:700px;height:450px;overflow-y:auto;overflow-x:auto">
    <form>
    <center>
                <table align="center" border="1px thin" width="100%">
                    <tr>
                        <td align="center">
                            Last
                        </td>
                      <td><input type="checkbox" name="sum" id="sum" onclick="selectall(this.id)">Sum</td>
                        <td><input type="checkbox" name="Avg" id="Avg" onclick="selectall(this.id)">Avg</td>
                        <td><input type="checkbox" name="Count" id="Count" onclick="selectall(this.id)">Count</td>
                        <td><input type="checkbox" name="Max" id="Max" onclick="selectall(this.id)">Max</td>
                        <td><input type="checkbox" name="Min" id="Min" onclick="selectall(this.id)">Min</td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="30days" id="30days" onclick="selectrow(this.id)">30days</td>
                        <td><input type="checkbox" name="sum1" id="sum1" value="sum-30days"></td>
                        <td><input type="checkbox" name="Avg1" id="Avg1" value="avg-30days"></td>
                        <td><input type="checkbox" name="Count1" id="Count1" value="count-30days"></td>
                        <td><input type="checkbox" name="Max1" id="Max1" value="max-30days"></td>
                        <td><input type="checkbox" name="Min1" id="Min1" value="min-30days"></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="45days" id="45days" onclick="selectrow(this.id)">45days</td>
                        <td><input type="checkbox" name="sum2" id="sum2" value="sum-45days"></td>
                        <td><input type="checkbox" name="Avg2" id="Avg2" value="avg-45days"></td>
                        <td><input type="checkbox" name="Count2" id="Count2" value="count-45days"></td>
                        <td><input type="checkbox" name="Max2" id="Max2" value="max-45days"></td>
                        <td><input type="checkbox" name="Min2" id="Min2" value="min-45days"></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="60days" id="60days" onclick="selectrow(this.id)">60days</td>
                        <td><input type="checkbox" name="sum3" id="sum3" value="sum-60days"></td>
                        <td><input type="checkbox" name="Avg3" id="Avg3" value="avg-60days"></td>
                        <td><input type="checkbox" name="Count3" id="Count3" value="count-60days"></td>
                        <td><input type="checkbox" name="Max3" id="Max3" value="max-60days"></td>
                        <td><input type="checkbox" name="Min3" id="Min3" value="min-60days"></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="90days" id="90days" onclick="selectrow(this.id)">90days</td>
                        <td><input type="checkbox" name="sum4" id="sum4" value="sum-90days"></td>
                        <td><input type="checkbox" name="Avg4" id="Avg4" value="avg-90days"></td>
                        <td><input type="checkbox" name="Count4" id="Count4" value="count-90days"></td>
                        <td><input type="checkbox" name="Max4" id="Max4" value="max-90days"></td>
                        <td><input type="checkbox" name="Min4" id="Min4" value="min-90days"></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="180days" id="180days" onclick="selectrow(this.id)">180days</td>
                        <td><input type="checkbox" name="sum5" id="sum5" value="sum-180days"></td>
                        <td><input type="checkbox" name="Avg5" id="Avg5" value="avg-180days"></td>
                        <td><input type="checkbox" name="Count5" id="Count5" value="count-180days"></td>
                        <td><input type="checkbox" name="Max5" id="Max5" value="max-180days"></td>
                        <td><input type="checkbox" name="Min5" id="Min5" value="min-180days"></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="366days" id="366days" onclick="selectrow(this.id)">366days</td>
                        <td><input type="checkbox" name="sum6" id="sum6" value="sum-366days"></td>
                        <td><input type="checkbox" name="Avg6" id="Avg6" value="avg-366days"></td>
                        <td><input type="checkbox" name="Count6" id="Count6" value="count-366days"></td>
                        <td><input type="checkbox" name="Max6" id="Max6" value="max-366days"></td>
                        <td><input type="checkbox" name="Min6" id="Min6" value="min-366days"></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="1month" id="1month" onclick="selectrow(this.id)">Last 1 Month</td>
                        <td><input type="checkbox" name="sum7" id="sum7" value="sum-1month"></td>
                        <td><input type="checkbox" name="Avg7" id="Avg7" value="avg-1month"></td>
                        <td><input type="checkbox" name="Count7" id="Count7" value="count-1month"></td>
                        <td><input type="checkbox" name="Max7" id="Max7" value="max-1month"></td>
                        <td><input type="checkbox" name="Min7" id="Min7" value="min-1month"></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="2month" id="2month" onclick="selectrow(this.id)">Last 2 Months</td>
                        <td><input type="checkbox" name="sum8" id="sum8" value="sum-2month"></td>
                        <td><input type="checkbox" name="Avg8" id="Avg8" value="avg-2month"></td>
                        <td><input type="checkbox" name="Count8" id="Count8" value="count-2month"></td>
                        <td><input type="checkbox" name="Max8" id="Max8" value="max-2month"></td>
                        <td><input type="checkbox" name="Min8" id="Min8" value="min-2month"></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="3month" id="3month" onclick="selectrow(this.id)">Last 3 Months</td>
                        <td><input type="checkbox" name="sum9" id="sum9" value="sum-3month"></td>
                        <td><input type="checkbox" name="Avg9" id="Avg9" value="avg-3month"></td>
                        <td><input type="checkbox" name="Count9" id="Count9" value="count-3month"></td>
                        <td><input type="checkbox" name="Max9" id="Max9" value="max-3month"></td>
                        <td><input type="checkbox" name="Min9" id="Min9" value="min-3month"></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="6month" id="6month" onclick="selectrow(this.id)">Last 6 Months</td>
                        <td><input type="checkbox" name="sum10" id="sum10" value="sum-6month"></td>
                        <td><input type="checkbox" name="Avg10" id="Avg10" value="avg-6month"></td>
                        <td><input type="checkbox" name="Count10" id="Count10" value="count-6month"></td>
                        <td><input type="checkbox" name="Max10" id="Max10" value="max-6month"></td>
                        <td><input type="checkbox" name="Min10" id="Min10" value="min-6month"></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="1year" id="1year" onclick="selectrow(this.id)">Last 1 Year</td>
                        <td><input type="checkbox" name="sum11" id="sum11" value="sum-1year"></td>
                        <td><input type="checkbox" name="Avg11" id="Avg11" value="avg-1year"></td>
                        <td><input type="checkbox" name="Count11" id="Count11" value="count-1year"></td>
                        <td><input type="checkbox" name="Max11" id="Max11" value="max-1year"></td>
                        <td><input type="checkbox" name="Min11" id="Min11" value="min-1year"></td>
                    </tr>
                    <%--While uncommenting the belwo code you have to change the number after the name and id to 15--%>
                   <%-- <tr>
                        <td><input type="checkbox" name="ptd" id="ptd" onclick="selectrow(this.id)">PTD(Period Till Date)</td>
                        <td><input type="checkbox" name="sum12" id="sum12" value="sum-ptd"></td>
                        <td><input type="checkbox" name="Avg12" id="Avg12" value="avg-ptd"></td>
                        <td><input type="checkbox" name="Count12" id="Count12" value="count-ptd"></td>
                        <td><input type="checkbox" name="Max12" id="Max12" value="max-ptd"></td>
                        <td><input type="checkbox" name="Min12" id="Min12" value="min-ptd"></td>
                    </tr>--%>

                   <tr>
                        <td><input type="checkbox" name="wtd" id="wtd" onclick="selectrow(this.id)">WTD(Week Till Date)</td>
                        <td><input type="checkbox" name="sum12" id="sum12" value="sum-wtd"></td>
                        <td><input type="checkbox" name="Avg12" id="Avg12" value="avg-wtd"></td>
                        <td><input type="checkbox" name="Count12" id="Count12" value="count-wtd"></td>
                        <td><input type="checkbox" name="Max12" id="Max12" value="max-wtd"></td>
                        <td><input type="checkbox" name="Min12" id="Min12" value="min-wtd"></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="pwtd" id="pwtd" onclick="selectrow(this.id)">PWTD(Prior Week Till Date)</td>
                        <td><input type="checkbox" name="sum13" id="sum13" value="sum-pwtd"></td>
                        <td><input type="checkbox" name="Avg13" id="Avg13" value="avg-pwtd"></td>
                        <td><input type="checkbox" name="Count13" id="Count13" value="count-pwtd"></td>
                        <td><input type="checkbox" name="Max13" id="Max13" value="max-pwtd"></td>
                        <td><input type="checkbox" name="Min13" id="Min13" value="min-pwtd"></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="pywtd" id="pywtd" onclick="selectrow(this.id)">PYWTD(Prior Year Week Till Date)</td>
                        <td><input type="checkbox" name="sum14" id="sum14" value="sum-pywtd"></td>
                        <td><input type="checkbox" name="Avg14" id="Avg14" value="avg-pywtd"></td>
                        <td><input type="checkbox" name="Count14" id="Count14" value="count-pywtd"></td>
                        <td><input type="checkbox" name="Max14" id="Max14" value="max-pywtd"></td>
                        <td><input type="checkbox" name="Min14" id="Min14" value="min-pywtd"></td>
                    </tr>
                   <tr>
                        <td><input type="checkbox" name="mtd" id="mtd" onclick="selectrow(this.id)">MTD(Month Till Date)</td>
                        <td><input type="checkbox" name="sum15" id="sum15" value="sum-mtd"></td>
                        <td><input type="checkbox" name="Avg15" id="Avg15" value="avg-mtd"></td>
                        <td><input type="checkbox" name="Count15" id="Count15" value="count-mtd"></td>
                        <td><input type="checkbox" name="Max15" id="Max15" value="max-mtd"></td>
                        <td><input type="checkbox" name="Min15" id="Min15" value="min-mtd"></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="pmtd" id="pmtd" onclick="selectrow(this.id)">PMTD(Prior Month Till Date)</td>
                        <td><input type="checkbox" name="sum16" id="sum16" value="sum-pmtd"></td>
                        <td><input type="checkbox" name="Avg16" id="Avg16" value="avg-pmtd"></td>
                        <td><input type="checkbox" name="Count16" id="Count16" value="count-pmtd"></td>
                        <td><input type="checkbox" name="Max16" id="Max16" value="max-pmtd"></td>
                        <td><input type="checkbox" name="Min16" id="Min16" value="min-pmtd"></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="pymtd" id="pymtd" onclick="selectrow(this.id)">PYMTD(Prior Year Month Till Date)</td>
                        <td><input type="checkbox" name="sum17" id="sum17" value="sum-pymtd"></td>
                        <td><input type="checkbox" name="Avg17" id="Avg17" value="avg-pymtd"></td>
                        <td><input type="checkbox" name="Count17" id="Count17" value="count-pymtd"></td>
                        <td><input type="checkbox" name="Max17" id="Max17" value="max-pymtd"></td>
                        <td><input type="checkbox" name="Min17" id="Min17" value="min-pymtd"></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="qtd" id="qtd" onclick="selectrow(this.id)">QTD(Quarter Till Date)</td>
                        <td><input type="checkbox" name="sum18" id="sum18" value="sum-qtd"></td>
                        <td><input type="checkbox" name="Avg18" id="Avg18" value="avg-qtd"></td>
                        <td><input type="checkbox" name="Count18" id="Count18" value="count-qtd"></td>
                        <td><input type="checkbox" name="Max18" id="Max18" value="max-qtd"></td>
                        <td><input type="checkbox" name="Min18" id="Min18" value="min-qtd"></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="pqtd" id="pqtd" onclick="selectrow(this.id)">PQTD(Prior Quarter Till Date)</td>
                        <td><input type="checkbox" name="sum19" id="sum19" value="sum-pqtd"></td>
                        <td><input type="checkbox" name="Avg19" id="Avg19" value="avg-pqtd"></td>
                        <td><input type="checkbox" name="Count19" id="Count19" value="count-pqtd"></td>
                        <td><input type="checkbox" name="Max19" id="Max19" value="max-pqtd"></td>
                        <td><input type="checkbox" name="Min19" id="Min19" value="min-pqtd"></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="pyqtd" id="pyqtd" onclick="selectrow(this.id)">PYQTD(Prior Year Quarter Till Date)</td>
                        <td><input type="checkbox" name="sum20" id="sum20" value="sum-pyqtd"></td>
                        <td><input type="checkbox" name="Avg20" id="Avg20" value="avg-pyqtd"></td>
                        <td><input type="checkbox" name="Count20" id="Count20" value="count-pyqtd"></td>
                        <td><input type="checkbox" name="Max20" id="Max20" value="max-pyqtd"></td>
                        <td><input type="checkbox" name="Min20" id="Min20" value="min-pyqtd"></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="ytd" id="ytd" onclick="selectrow(this.id)">YTD(Year Till Date)</td>
                        <td><input type="checkbox" name="sum21" id="sum21" value="sum-ytd"></td>
                        <td><input type="checkbox" name="Avg21" id="Avg21" value="avg-ytd"></td>
                        <td><input type="checkbox" name="Count21" id="Count21" value="count-ytd"></td>
                        <td><input type="checkbox" name="Max21" id="Max21" value="max-ytd"></td>
                        <td><input type="checkbox" name="Min21" id="Min21" value="min-ytd"></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="pytd" id="pytd" onclick="selectrow(this.id)">PYTD(Prior Year Till Date)</td>
                        <td><input type="checkbox" name="sum22" id="sum22" value="sum-pytd"></td>
                        <td><input type="checkbox" name="Avg22" id="Avg22" value="avg-pytd"></td>
                        <td><input type="checkbox" name="Count22" id="Count22" value="count-pytd"></td>
                        <td><input type="checkbox" name="Max22" id="Max22" value="max-pytd"></td>
                        <td><input type="checkbox" name="Min22" id="Min22" value="min-pytd"></td>
                    </tr>
                     <tr>
                        <td><input type="checkbox" name="ltd" id="ltd" onclick="selectrow(this.id)">LTD(Life Till Date)</td>
                        <td><input type="checkbox" name="sum23" id="sum23" value="sum-ltd"></td>
                        <td><input type="checkbox" name="Avg23" id="Avg23" value="avg-ltd"></td>
                        <td><input type="checkbox" name="Count23" id="Count23" value="count-ltd"></td>
                        <td><input type="checkbox" name="Max23" id="Max23" value="max-ltd"></td>
                        <td><input type="checkbox" name="Min23" id="Min23" value="min-ltd"></td>
                    </tr>
                </table>
                <br/>

<!--                <input type="submit" value="Save" class="navtitle-hover" onclick="saveSelected()">-->
                <input type="hidden" name="selectedvalues" id="selectedvalues" value="">
                <input type="hidden" name="bussTableId" id="bussTableId" value="<%=bussTableId%>">
                <input type="hidden" name="bussColId" id="bussColId" value="<%=bussColId%>">
                    <input type="hidden" name="colName" id="colName" value="<%=colName%>">
                <input type="hidden" name="tablename" id="tablename" value="<%=tablename%>">
            </center>
    </form>
            <br>
            <table width="100%">

                <tr align="center"><td><input type="submit" value="Save" class="navtitle-hover" onclick="saveSelected()"></td></tr>
            </table>
    </form>
             <script type="text/javascript">
            function saveSelected()
            {
                var selectedarr="";
                var chkboxnames = ["sum", "Avg", "Count", "Max", "Min"];
                var chk = 0;
                for(var i = 1; i <= 23; i ++ )
                {
                    if(document.getElementById(chkboxnames[0]+i).checked==true)
                    {
                        selectedarr = selectedarr+chkboxnames[0]+i+",";
                        chk=1;
                    }
                    if(document.getElementById(chkboxnames[1]+i).checked==true)
                    {
                        selectedarr = selectedarr+chkboxnames[1]+i+",";
                        chk=1;
                    }
                    if(document.getElementById(chkboxnames[2]+i).checked==true)
                    {
                        selectedarr = selectedarr+chkboxnames[2]+i+",";
                        chk=1;
                    }
                    if(document.getElementById(chkboxnames[3]+i).checked==true)
                    {
                        selectedarr = selectedarr+chkboxnames[3]+i+",";
                        chk=1;
                    }
                    if(document.getElementById(chkboxnames[4]+i).checked==true)
                    {
                        selectedarr = selectedarr+chkboxnames[4]+i+",";
                        chk=1;
                    }
                }
                //alert("This is saveselected function");
                selectedarr = selectedarr.substring(0,selectedarr.length-1);
                // alert(selectedarr);
                if(chk==1)
                {
                    document.getElementById("selectedvalues").value=selectedarr;
                    document.forms.timebasedform.action="businessgroupeditaction.do?groupdetails=timeBasedFormula";
                    document.forms.timebasedform.submit();
                    parent.window.location.href = parent.window.location.href;
                }
                else{
                    alert("Please Select Any Check Box To Save");
                }
            <%--return false;--%>


            <%-- $.ajax({
                 url:'businessgroupeditaction.do?groupdetails=timeBasedFormula&selectedvalues='+selectedarr,
                 sucess:function(data){

                 }
             });--%>

                 }
                 function selectall(checkid)
                 {
                     //  alert(checkid);
                     for(var i = 1; i <= 23; i ++ )
                     {
                         if(document.getElementById(checkid).checked == true)
                         {
                             document.getElementById(checkid + i).checked = true;
                         }
                         else if(document.getElementById(checkid).checked == false)
                         {
                             document.getElementById(checkid + i).checked = false;
                         }
                     }
                 }
                 function selectrow(rowids)
                 {
                     var rowwise = ["30days", "45days", "60days", "90days", "180days", "366days", "1month", "2month", "3month", "6month", "1year", 
                         /*"ptd",*/"wtd","pwtd","pywtd","mtd","pmtd","pymtd","qtd","pqtd","pyqtd","ytd","pytd","ltd"];
                     var rownames = ["sum", "Avg", "Count", "Max", "Min"];
                     var  j = 0;
                     for(var i = 0; i < rowwise.length; i ++ )
                     {
                         //  alert(rowwise[i]);
                         if(rowids == rowwise[i])
                         {
                             break;
                         }
                     }
                     // alert(rowids);
                     var num = rowwise[i];
                     // alert(num);
                     // return false;
                     i = i + 1;
                     for(var j = 0; j < 5; j ++ )
                     {
                         // alert(document.getElementById(rowids).checked);
                         if(document.getElementById(rowids).checked == true)
                         {
                             // alert(rownames[j] + i);
                             document.getElementById(rownames[j] + i).checked = true;
                         }
                         else
                         {
                             document.getElementById(rownames[j] + i).checked = false;
                         }
                     }
                 }
        </script>
    </body>

</html>
