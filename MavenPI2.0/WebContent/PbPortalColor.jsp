

<%@page import="java.util.List,com.google.common.collect.Iterables,java.util.Arrays,com.progen.portal.Portal,com.progen.portal.PortLet"%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.dashboard.DashboardTableColorGroupHelper,java.util.ArrayList,java.util.HashMap,prg.db.*,com.progen.report.display.util.NumberFormatter"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%--
    Document   : applycolors
    Created on : Jan 8, 2010, 1:00:55 PM
    Author     : Administrator
--%>

<%
            String columnName = request.getParameter("columnname");
            String labelName=request.getParameter("labelName");
            String disColumnName = request.getParameter("dispcolumname");
            String portalId = request.getParameter("portalId");
            String portletId = request.getParameter("portletId");
            String[] strOperators = {"<", ">", "<=", ">=", "=", "!=", "<>"};
            String[] StrColors = {"Red", "Green","Blue","Orange"};
            String fromModule=request.getParameter("fromModule");
            HashMap<String,ArrayList> tempdbrMap=new HashMap<String,ArrayList>();
            HashMap ColorCodeMap=null;
            if(fromModule.equalsIgnoreCase("Portal")){
                ColorCodeMap=new HashMap();
                 List<Portal> portals = (List<Portal>) session.getAttribute("PORTALS");
                 List<PortLet> portlets = null;
                 PortLet portLet = null;
                 Portal tempportal=null;
                 tempportal=Iterables.find(portals,Portal.getAccessPortalPredicate(Integer.parseInt(portalId)));
                 portLet=Iterables.find(tempportal.getPortlets(), PortLet.getAccessPortletPredicate(Integer.parseInt(portletId)));
                 List<DashboardTableColorGroupHelper> tableColorGrpList=portLet.getPortalTableColor();
                ArrayList colorCodes =new ArrayList();
                ArrayList operators =new ArrayList();
                ArrayList sValues =new ArrayList();
                ArrayList eValues = new ArrayList();
              for(int i=0;i<tableColorGrpList.size();i++){
                  if(tableColorGrpList.get(i).getElementId().equalsIgnoreCase(columnName.replace("A_",""))){
              colorCodes.addAll(tableColorGrpList.get(i).getColorVal());
              operators.addAll(tableColorGrpList.get(i).getColorCondOper());
               sValues.addAll(tableColorGrpList.get(i).getCondStartValue());
               eValues.addAll(tableColorGrpList.get(i).getCondEndValue());
              }
              }
                if(!colorCodes.isEmpty()){
               tempdbrMap.put("colorCodes",colorCodes);
                tempdbrMap.put("operators",operators);
                 tempdbrMap.put("sValues",sValues);
                 tempdbrMap.put("eValues",eValues);
                 }
                 if(!tableColorGrpList.isEmpty() && !tempdbrMap.isEmpty())

              ColorCodeMap.put(disColumnName,tempdbrMap);

            }
            
            String contextPath=request.getContextPath();
            
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Apply Color Based Grouping</title>
             <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
         <link type="text/css" href="<%=contextPath%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />

        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <style type="text/css">
            *{
                font: 11px verdana;
            }
        </style>
        <script  type="text/javascript" src="<%=contextPath%>/TableDisplay/JS/pbTableMapJS.js"></script>

        <script type="text/javascript" src="<%=contextPath%>/jQuery/farbtastic12/farbtastic/farbtastic.js"></script>
        <link rel="stylesheet" href="<%=contextPath%>/jQuery/farbtastic12/farbtastic/farbtastic.css" type="text/css" />
      
    </head>
    <body>
        <center>
            <form action="" name="colorfrm" id="colorfrm" method="post">
                <table border="0">
                    <tr align="left">
                        <td>Maximum Value:</td>
                        <td><input readonly type="text" name="max" id="max" value="" ></td>
                    </tr>
                    <tr align="left">
                        <td>Minimum Value:</td>
                        <td><input readonly type="text" name="min" id="min" value="" ></td>
                    </tr>
                    <tr align="left">
                        <td>Average Value:</td>
                        <td><input readonly type="text" name="avg" id="avg" value="" ></td>
                    </tr>
                    <tr>
                        <td>Gradient Based</td>
                        <td><input type="checkbox" name="gradientBased" id="gradientId" onclick="displayGradientColors()" value=""></td>
                    </tr>
                </table>
                <table border="0" id="colorTable">
                       <%
            if (ColorCodeMap != null && ColorCodeMap.get(disColumnName) != null) {
                HashMap tempMap = null;
                tempMap = (HashMap) ColorCodeMap.get(disColumnName);
                String[] colorCodes = null;
                String[] operators = null;
                String[] sValues = null;
                String[] eValues = null;
                if(fromModule.equalsIgnoreCase("Portal")){
                  colorCodes = Arrays.copyOf(tempdbrMap.get("colorCodes").toArray(), tempdbrMap.get("colorCodes").toArray().length, String[].class);
                  operators = Arrays.copyOf(tempdbrMap.get("operators").toArray(), tempdbrMap.get("operators").toArray().length, String[].class);
                  sValues = Arrays.copyOf(tempdbrMap.get("sValues").toArray(), tempdbrMap.get("sValues").toArray().length, String[].class);
                  eValues = Arrays.copyOf(tempdbrMap.get("eValues").toArray(), tempdbrMap.get("eValues").toArray().length, String[].class);


               }
               /* else{
                tempMap = (HashMap) ColorCodeMap.get(disColumnName);
                colorCodes = (String[]) tempMap.get("colorCodes");
                operators = (String[]) tempMap.get("operators");
                 sValues = (String[]) tempMap.get("sValues");
                 eValues = (String[]) tempMap.get("eValues");

               }*/
                if (colorCodes != null) {
                    for (int i = 0; i < colorCodes.length; i++) {
                        %>
                    <tr id="row<%=i%>">
                        <td>
                            Apply Color as
                            <input type="text" name="colorCodes" id="colorCodes<%=i%>" onclick="showColor(this.id)" style='width:50px;cursor:pointer;background-color:<%=colorCodes[i]%>' colorCode='<%=colorCodes[i]%>'>
                            When Value
                        </td>
                        <td>
                            <select name="operators" id="<%=i%>operators" onchange="onoperator(this.id,this.value)">
                                <option value="none">-select-</option>
                                <%for (String Str : strOperators) {
                            if (Str.equalsIgnoreCase(operators[i])) {%>
                                <option  selected value="<%=Str%>"><%=Str%></option>
                                <%} else {%>
                                <option  value="<%=Str%>"><%=Str%></option>
                                <%}
                        }%>
                            </select>
                        </td>
                        <td>
                            <input type="text" name="sValues" value="<%=sValues[i]%>" id="sValues<%=i%>">
                        </td>
                        <%if(operators[i].equalsIgnoreCase("<>")){%>
                        <td>
                            <input type="text" name="eValues" value="<%=eValues[i]%>"  id="<%=i%>eValues">
                        </td>
                        <%}else{%>
                         <td>
                         <input type="hidden" name="eValues" value="<%=eValues[i]%>"  id="<%=i%>eValues">
                         </td>
                        <%}%>
                    </tr>
                    <%}
                } else {
                }
            } else {%>
                       <tr id="row0">
                        <td>
                            Apply Color as
                            <input  name="colorCodes" id="colorCodes0" type="text" onclick="showColor(this.id)" value="" style='width:50px;cursor:pointer' colorCode=''>
                            When Value
                        </td>
                        <td>
                            <select name="operators" id="0operators" onchange="onoperator(this.id,this.value)">
                                <%for (String Str : strOperators) {%>
                                <option value="<%=Str%>"><%=Str%></option>
                                <%}%>
                            </select>
                        </td>
                        <td>
                            <input type="text" name="sValues" id="sValues0" >
                        </td>
                        <td>
                            <input type="hidden" name="eValues" id="0eValues" >
                        </td>

                    </tr>
                       <%}%>
                </table>
                <table border="0">
                    <tr>
                        <td colspan="3" style="height:10px">&nbsp;</td>
                    </tr>
                    <tr align="center">
                        <td align="center" colspan="3">
                            <input class="navtitle-hover" type="button" value="Add Row" onclick="addRow()">
                            <input class="navtitle-hover" type="button" value="Delete Row" onclick="deleteRow()">
                            <input class="navtitle-hover" type="button" value="Done" onclick="savecolors('colorfrm')">
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3" style="height:10px">&nbsp;</td>
                    </tr>
                    <tr>
                        <td colspan="3"><font size="1" color="red">*</font>Values displayed are in Absolute format.Please enter Absolute values only.</td>
                    </tr>
                </table>
                 <input type="hidden" name="colName" id="colName" value="<%=columnName%>">
                <input type="hidden" name="disColName" id="disColName" value="<%=disColumnName%>">
                <input type="hidden" name="portalId" id="portalId" value="<%=portalId%>">
                <input type="hidden" name="tableChange" id="tableChange" value="applycolor">
            </form>
        </center>
         <div id="colorsDiv" style="display: none" title="Select color">
                    <center>
                        <input type="text" id="color" style="" value="#12345" >
                        <div id="colorpicker" style=""></div>
                        <input type="button" align="center" value="Done" class="navtitle-hover" onclick="saveSelectedColor()">
                        <input type="button" align="center" value="Cancel" class="navtitle-hover" onclick="cancelColor()">
                        <input type="hidden" id="selectedId" value="">
                    </center>
        </div>
                  <script type="text/javascript">
            $(document).ready(function(){
                $.get("<%= request.getContextPath()%>/portalViewer.do?portalBy=getMinMaxAvgValueOfElement&portalId=<%=portalId%>&portletId=<%=portletId%>&columnname=<%=columnName%>",function(data){
                    if(data!=""){
                         var jsonVar=eval('('+data+')')
                         var maxValue=jsonVar.MaxValue;
                         var minValue=jsonVar.MinValue;
                         var avgValue=jsonVar.AvgValue;
                         //alert(maxValue+",,,,"+minValue+"......"+avgValue)
                         $("#max").val(maxValue);
                         $("#min").val(minValue);
                         $("#avg").val(avgValue);
                    }

                   });
                    $("#colorsDiv").dialog({
                            bgiframe: true,
                            autoOpen: false,
                            height:300,
                            width: 300,
                            modal: true,
                            Cancel: function() {
                                $(this).dialog('close');
                            }

                    });
                   $('#colorpicker').farbtastic('#color');

                    });
            //This is the code of validating numeric keys.
            function isNumberKey(evt)
            {
                var charCode = (evt.which) ? evt.which : event.keyCode
                if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode != 46)//Here char code is 46 (.) which allows to press dot
                    return false;
                return true;
            }
          
            function savecolors(formId){
                var colorCodeObjs=document.getElementsByName("colorCodes");
                var chkObj=document.getElementById("gradientId");
                if(!chkObj.checked)
                    for(var i=0;i<colorCodeObjs.length;i++)
                    {
                        var colorCode=$("#"+colorCodeObjs[i].id).attr('colorCode')
                        colorCodeObjs[i].value=colorCode
                    }
                    $.post("<%=request.getContextPath()%>/portalViewer.do?portalBy=savePortalColors&colmnlabelName=<%=labelName%>&portalId=<%=portalId%>&portletId=<%=portletId%>", $("#"+formId).serialize() ,
                function(data){
                     parent.$("#applyPortletcolrdiv").dialog('close');
                    var rowEdgeParams="";
                    var colEdgeParams="";
                    var rowParamIdObj=document.getElementsByName("chkREP-"+<%=portletId%>+ "-" +<%=portalId%>)
                    var columnParmObject=''
                    var CEPNames=''
                    var REPNames=''
                     columnParmObject=document.getElementsByName("chkCEP-"+<%=portletId%>+"-"+<%=portalId%>)
                     if(rowParamIdObj!=null){

                    for(var i=0;i<rowParamIdObj.length;i++){
                       if(rowParamIdObj[i].checked){
                            rowEdgeParams=rowEdgeParams+","+rowParamIdObj[i].value.split("~")[0];
                            REPNames=REPNames+","+rowParamIdObj[i].value.split("~")[1];
                        }
                    }
                    if(rowEdgeParams!=""){
                        rowEdgeParams=rowEdgeParams.substring(1);
                        REPNames=REPNames.substring(1);
                         }
                      }
                    if(columnParmObject!=null){

                            for(var j=0;j<columnParmObject.length;j++){
                             if(columnParmObject[j].checked){
                            colEdgeParams=colEdgeParams+","+columnParmObject[j].value.split("~")[0];
                            CEPNames=CEPNames+","+columnParmObject[j].value.split("~")[1];
                             }
                            }
                              if(colEdgeParams!=""){
                             colEdgeParams=colEdgeParams.substring(1);
                             CEPNames=CEPNames.substring(1);
                            }
                         }
                                         
                 parent.getPortletDetails(<%=portletId%>, rowEdgeParams, colEdgeParams,"","",<%=portalId%>,"");
                     });

            }

             function onoperator(id,symbol)
            {
                var box = parseInt(id);
                var open = document.getElementById(box+"eValues");
                if(symbol=="<>"){
                    open.type="text";
                    open.style.display="block";
                }
                else{
                    open.style.display="none";
                    open.type="hidden";
                }
            }
          function showColor(id)
          {
             $("#selectedId").val(id);
             var colorCode="";
             colorCode=$("#"+id).attr('colorCode');
              $("#colorsDiv").dialog('open')
            if(colorCode!=undefined && colorCode!="")
              {
                $("#color").val(colorCode)
                $("#color").css("background-color",colorCode)
               $.farbtastic("#color").setColor(colorCode)
              }


          }
          function saveSelectedColor()
          {
              var seletedTextId= $("#selectedId").val();
              var colorCode=$("#color").val();
              $("#"+seletedTextId).css("background-color",colorCode)
              $("#"+seletedTextId).attr('colorCode',colorCode);
              $("#colorsDiv").dialog('close')
          }
          function cancelColor()
          {
               $("#colorsDiv").dialog('close')
          }
      
            
            function addRow(){
                var table = document.getElementById("colorTable");
                var rowCount = table.rows.length;
                var idx = rowCount ;
                var chkObj=document.getElementById("gradientId");
                var tableHtml="";

                tableHtml=tableHtml+"<tr id='row"+idx+"'>";
                if(chkObj.checked)
                    {

                    tableHtml=tableHtml+"<td>Apply Color as <select name=\"colorCodes\" id=\"colorCodes"+idx+"\">";
                     <%for (String Str : StrColors) {%>
                         tableHtml=tableHtml+"<option value='<%=Str%>'><%=Str%></option>";
                      <%}%>
                      tableHtml=tableHtml+"</select>When Value</td>";

                    }else{
                        tableHtml=tableHtml+" <td>Apply Color as <input type='text' name=\"colorCodes\" id=\"colorCodes"+idx+"\" style='width:50px;cursor:pointer' onclick=\"showColor(this.id)\"  colorCode='' >When Value</td>"

                    }

                   tableHtml=tableHtml+"<td><select name=\"operators\" id=\""+idx+"operators\" onchange=onoperator(this.id,this.value)>";
            <%for (String Str : strOperators) {%>
                  tableHtml=tableHtml+"<option value='<%=Str%>'><%=Str%></option>";
            <%}%>
                     tableHtml=tableHtml+"</select></td>";
                     tableHtml=tableHtml+"<td><input type=\"text\" name=\"sValues\" value=\"\" id=\"sValues"+idx+"\"></td>";
                      tableHtml=tableHtml+"<td><input type=\"hidden\" name=\"eValues\" value=\"\" id=\""+idx+"eValues\"></td></tr>";
                    if(idx==0){

                     $('#colorTable').html(tableHtml)
                    }else{

                       $('#colorTable > tbody:last').append(tableHtml)
                }
                }
                function deleteRow(){
                    try {
                        var table = document.getElementById("colorTable");
                        var rowCount = table.rows.length;
                        if(rowCount > 1) {
                            table.deleteRow(rowCount - 1);
                        }
                    }catch(e) {
                        alert(e);
                    }
                }
             function displayGradientColors()
             {
                 var chkObj=document.getElementById("gradientId");
                 var tableObj= document.getElementById("colorTable")
                 var rowCount=tableObj.rows.length;
                 var html="";
                    $("#colorTable").html("");
                 if(chkObj.checked)
                 {
                    chkObj.value=true;
                    for(var i=0;i<rowCount;i++)
                        addRow();
                 }
                 else{
                    chkObj.value=false;
                    for(var i=0;i<rowCount;i++)
                        addRow();
                 }
             }
        </script>
    </body>
</html>
