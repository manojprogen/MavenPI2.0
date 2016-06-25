<%--
    Document   : pbParameterFilter
    Created on : 23 Jul, 2010, 2:43:29 PM
    Author     : mahesh
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="prg.db.Container"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.progen.reportdesigner.db.ReportTemplateDAO" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%

            String allViewbyIds = "";
            String allViewbyNames = "";
            String rowViewIdList = "";
            String colViewIdList = "";
            String rowNamesLst = "";
            String colNamesLst = "";
            HashMap map = null;
            Container container = null;
            String folderIds=null;
            HashMap ParametersHashMap = null;
            ArrayList Parameters = null;
            ArrayList ParametersNames = null;
            String factInfo=null;
            String reportId = request.getParameter("reportId");
            String disColumnName = request.getParameter("disColumnName");
            String measEleId = request.getParameter("columnName");
            String[] cond={"in","not in","like","not like","=",">","<",">=","<=","<>","between","is null","is not null"};
            if (session.getAttribute("allViewIds") != null) {
                allViewbyIds = String.valueOf(session.getAttribute("allViewIds"));
            }
            if (session.getAttribute("allViewNames") != null) {
                allViewbyNames = String.valueOf(session.getAttribute("allViewNames"));
            }
            if (session.getAttribute("rowViewIdList") != null) {
                rowViewIdList = String.valueOf(session.getAttribute("rowViewIdList"));
            }
            if (session.getAttribute("colViewIdList") != null) {
                colViewIdList = String.valueOf(session.getAttribute("colViewIdList"));
            }
            if (session.getAttribute("rowNamesLst") != null) {
                rowNamesLst = String.valueOf(session.getAttribute("rowNamesLst"));
            }
            if (session.getAttribute("colNamesLst") != null) {
                colNamesLst = String.valueOf(session.getAttribute("colNamesLst"));
            }

            String[] allViewbyIdsStr = allViewbyIds.replace("[", "").replace("]", "").split(",");
            String[] allViewbyNamesStr = allViewbyNames.replace("[", "").replace("]", "").split(",");
            String[] dimenssionType={"in","not in","like","not like","=",">","<",">=","<=","<>"};
            String[] allfactNames=null;
            String[] fInfo = null;
            String[] subfolderIds=null;
            String[] allDispNames=null;
            String busTabDetails=null;
            String busTabinfo[]=null;
            String bussTableName=null,bussTableId=null;
            ReportTemplateDAO factsDAO = new ReportTemplateDAO();
            if (session.getAttribute("PROGENTABLES") != null) {
                            map = (HashMap) session.getAttribute("PROGENTABLES");
                            if (map.get(reportId) != null) {
                                container = (Container) map.get(reportId);
                                HashMap ReportHashMap = container.getReportHashMap();
                                String[] repBisRoles = (String[]) ReportHashMap.get("ReportFolders");
                                StringBuffer sbufRoles = new StringBuffer("");
                                for (int k = 0; k < repBisRoles.length; k++) {
                                    sbufRoles.append("," + repBisRoles[k]);
                                }
                               folderIds=sbufRoles.toString().substring(1);
                               ParametersHashMap = container.getParametersHashMap();
                    if (ParametersHashMap.get("ParametersNames") != null) {
                        Parameters = (ArrayList) ParametersHashMap.get("Parameters");
                        ParametersNames = (ArrayList) ParametersHashMap.get("ParametersNames");
                    }
                              // factInfo = factsDAO.getFactNames(folderIds, Parameters,request.getContextPath());
                               String msrEleId=measEleId.substring(2);
                              // 
                               factInfo = factsDAO.fetchFactName(msrEleId);
                               fInfo=factInfo.split("&");
                               allfactNames=fInfo[0].split(",");
                               subfolderIds=fInfo[1].split(",");
                               allDispNames=fInfo[2].split(",");
                            }
                        }
         String FactFilter=null;
         FactFilter=request.getParameter("FactFilter");
         //

%>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><bean:message key="ProGen.Title"/></title>

        <style type="text/css">
            *{font : 11px verdana}
        </style>

        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script><!--
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>-->
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>-->
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
             <script type="text/javascript" src="<%= request.getContextPath()%>//dragAndDropTable.js"></script>
              <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/quicksearch.js"></script>

        <script type="text/javascript">

        var Dimkeys = [];

            function LTrim( value ) {

                var re = /\s*((\S+\s*)*)/;
                return value.replace(re, "$1");

            }

            // Removes ending whitespaces
            function RTrim( value ) {

                var re = /((\s*\S+)*)\s*/;
                return value.replace(re, "$1");

            }

            // Removes leading and ending whitespaces
            function trim( value ) {

                return LTrim(RTrim(value));
            }
            function goNext(reportId,measEleId,disColumnName){
               // alert(document.getElementById("paramFilter").value)
                if(trim($("#ParamFilterName").val())!=""){
                    <% if(FactFilter==null){%>
                    var dimType=document.getElementById("dimenssionType").value;
                    var selectedParam=document.getElementById("paramFilter").value;
                    var selectedParamName=document.getElementById("paramFilter");
                    <%}%>
                    var paramFilterName=document.getElementById("ParamFilterName").value;
//                    paramFilterName = paramFilterName.toString().replace("&","|__|","gi");
//                    paramFilterName = paramFilterName.toString().replace("+","_||_","gi");
                     var staticDimVal=null;
                     var dimCalOperator=null;
                    parent.$("#paramFilterDiv").dialog('close');
                    var ctxtPath='<%=request.getContextPath()%>';
                      if($("#dimenssion").is(':checked')){ //for dimenssion only

                        staticDimVal=$("#staticVal").val();
                        parent.$("#paramFilterMemberDiv").dialog({
                        autoOpen: false,
                        height: 600,
                        width: 720,
                        position: 'justify',
                        modal: true
                         });
                         if($("#dimCal").is(':checked')){
                        dimCalOperator=$("#dimCalOperator").val();
////                        alert(dimCalOperator)
                         $.ajax({
         url: encodeURI('<%=request.getContextPath()%>/reportViewer.do?reportBy=getParamFilterMbrs&paramFilterName='+encodeURIComponent(paramFilterName)+'&reportId='+reportId+'&measEleId='+measEleId+'&Type=dimCalculation'+'&dimCalOperator='+dimCalOperator+'&selectedParam='+selectedParam),
                    success: function(data){
                         $.ajax({
                            url:'<%=request.getContextPath()%>/reportViewer.do?reportBy=tableMeasureChanges&tableMsrs='+data+'&REPORTID='+reportId,
                            success: function(data){
                                    parent.submitFormMeasChange();
                            }
                        });
                    }
                });
                       }
                       else{
                           parent.$("#paramFilterMemberDiv").dialog('open');
                    var frameObj=parent.document.getElementById("paramFilterMember");
                    var source ='<%=request.getContextPath()%>/TableDisplay/pbParamFilterMembers.jsp?reportId='+reportId+'&paramFilterName='+encodeURIComponent(paramFilterName)+'&selectedParam='+selectedParam+'&measElement='+measEleId+'&disColumnName='+disColumnName+'&dimType='+dimType+'&staticDimVal='+staticDimVal;
                    var encodedSource = encodeURI(source);
                    frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
                    frameObj.src=encodedSource;
                  }
                    }
                    else{    //for Fact in parameterFilter and FactFilter code
//                        var factFilter='<%=FactFilter%>';
                        var factFilter = 'FactFilter';
                        var subfolderId;
                        var folderId='<%=folderIds%>';
                        var factdetails=$("#factNames").val();
                        var factar=factdetails.split("&");
                        var factNames=factar[0];
                         subfolderId=factar[1];
//                        alert(factNames+","+subfolderId)
                        parent.$("#paramFilterMeasureValues").dialog({
                        autoOpen: false,
                        height: 600,
                        width: 720,
                        position: 'justify',
                        modal: true
                        });
                        $.ajax({
                            url:'<%=request.getContextPath()%>/reportViewer.do?reportBy=getDimensionMembersForDimDep&measEleId='+measEleId,
                            success: function(data){
                                     var  jsonVar=eval('('+data+')')

                                    var jsonvalues;
                                    var html;
                                    for (var Dimkey in jsonVar) {
                                    if (jsonVar.hasOwnProperty(Dimkey)) {
                                        Dimkeys.push(Dimkey);
                                        }
                                     }
                                  //end of code by nazneen
                            
                        $.ajax({
                            url:'<%=request.getContextPath()%>/reportViewer.do?reportBy=getMeasureNamesforParamFilter&folderIds='+folderId+'&subfolderIds='+subfolderId,
                            success: function(data){

                                   var  jsonVar=eval('('+data+')')
                                    var keys = [];
                                    var jsonvalues;
                                    var html;
                                    for (var key in jsonVar) {
                                    if (jsonVar.hasOwnProperty(key)) {
                                        keys.push(key);
                                        }
                                     }
                                     //added by Nazneen for Dimension Dependent


                            
                         html='<table><thead><br><tr>'
                          <%
//                          if(FactFilter!=null){
                          %>
                            html+='<th colspan="5" align="center"><input type="checkbox" name="DateId" id="DateId" onclick="checkforDate()">Allow Date Variable</th></tr><tr>'
                          <%
//                            }
                          %>
                         <%
//                            if(FactFilter==null){
                         %>
//                             html+='<th style="width:6%" valign="left">&nbsp;</th>'
                          <%
//                            }else{
                          %>
                            html+='<th style="width:6%" valign="left">Compare With Measure</th>'
                        <%
//                            }
                         %>
                            html+='<th style="width:4%" valign="left">&nbsp;</th><th style="width:15%" valign="center">Measure Names</th><th style="width:14%" valign="center">Condition</th><th style="width:14%" valign="left">Measure Values</th><th style="width: 14%;">&nbsp;</th></tr></thead><tbody>'
                            html+='<table id="mTable"><tr>'
                         <%
//                            if(FactFilter==null){
                         %>
//                          html+='<td valign="center" style="width:5%">&nbsp;</td>'
                         <%
//                            }else{
                        %>
                            html+='<td valign="center" style="width:5%"><input type="checkbox" name="checkcmpMsr" id="checkcmpMsr0" onclick="checkforcompareMsr(this.id)"></td>'
                       <%
//                            }
                        %>
                         html+='<td valign="center" style="width:5%"><select id="Cond0" name="Cond" style="width:100px;display:none;"><option value=and>and</option><option value=or>or</option></select></td><td valign="center" style="width:12%"><select id="mName0" name="mName" style="width:160px;">'
                         for(var i=0;i<keys.length;i++)
                         {
                         //  html+='<option value='+keys[i]+'>'+jsonVar[keys[i]]+'</option>'
                           html+='<option value='+jsonVar[keys[i]]+'>'+keys[i]+'</option>'
                          }
                      html+='</select></td><td valign="center" style="width:12%"><select id="mCond0" name="mCond" style="width:100px;" onchange="changeMsrOption(this.id)">'
                        <%for(int i=0;i<cond.length;i++) {%>
                        html+='<option value="<%=cond[i]%>"><%=cond[i]%></option>'
                        <%
                        }
                        %>
                    html+='</select></td><td id="mvaluesTD0" valign="center" style="width:10%"><input type="text" id="mValues0" name="mValues" style="width:160px;" onclick="changeMeasureVal('+"'"+ctxtPath+"'"+','+"'"+factNames+"'"+',this.id)"></td>'
                    html+='<td id="endValue0" valign="center" style="width:1%;"><input type="text" id="eValues0" name="eValues" style="width:160px;display:none;" onclick="changeMeasureVal('+"'"+ctxtPath+"'"+','+"'"+factNames+"'"+',this.id)"></td> '
                    <%
//                        if(FactFilter!=null){
                    %>
                    html+='<td id="cmpMsr0" valign="center" style="width:10%;display:none"><select id="cmpMsrName0" name="cmpMsrName" style="width:160px;">'
                    for(var i=0;i<keys.length;i++)
                         {
                           html+='<option value='+jsonVar[keys[i]]+'>'+keys[i]+'</option>'
                          }
                      html+='</select></td>';
                      html+='<td id="endcmpMsr0" valign="center" style="width:1%;"><select id="endcmpMsrName0" name="endcmpMsrName" style="width:160px;display:none;">'
                    for(var i=0;i<keys.length;i++)
                         {
//                           html+='<option value='+keys[i]+'>'+jsonVar[keys[i]]+'</option>'
                            html+='<option value='+jsonVar[keys[i]]+'>'+keys[i]+'</option>'
                          }
                      html+='</select></td>';
                       <%
//                        }
                        %>
                    html+='<td width="2%"><IMG id="add0"ALIGN="middle" onclick=\"addMeasure('+"'"+factFilter+"'"+',this.id)\" SRC="<%=request.getContextPath()%>/icons pinvoke/plus-circle.png" BORDER="0" ALT="" title="Add Row" /></td><td width="14%"><IMG id="delete0" ALIGN="middle" onclick="deleteMeasure(this.id)" SRC="<%=request.getContextPath()%>/icons pinvoke/cross-circle.png" BORDER="0" ALT="" title="Delete Row" /></td></tr>'
                    <%
                    if(FactFilter==null){
                    %>
                    html+='</table><table align="center" style="width:100%"><tr><td>&nbsp;</td></tr><br/><tr><td align="center"><input type="button"  value="save" class="navtitle-hover" onclick="saveFactDetails('+"'"+ctxtPath+"'"+','+<%=reportId%>+','+"'"+measEleId+"'"+','+"'"+paramFilterName+"'"+','+folderId+','+"'"+subfolderId+"'"+')"></td></tr></table> </tbody></table>'
                    <%
                        }else{
                    %>
                         html+='</table><br><table align="center" style="width:100%"><tr><td align="center"><input type="checkbox" name="ProgenTime" id="ProgenTime" onclick="checkforProgenTime()">disable progen default time join</td></tr><tr><td>&nbsp;</td></tr><br/>\n\
                         <tr><td><input type="checkbox" name="dependentDims" id="dependentDims" onclick="getDependentDims()">&nbsp;Dependent on Dimensions</td></tr>\n\
                         <tr><td><div id="dependentDimsDiv" style="display:none"><table><tr><td>Measure Name : &nbsp;</td><td><select id="dependentDimsName" name="dependentDimsName" style="width:160px;" onchange="getPriority()">';
                          for(var i=0;i<Dimkeys.length;i++)
                          {
                           html+='<option value='+jsonVar[Dimkeys[i]]+'>'+Dimkeys[i]+'</option>'
                          }
                          html+='<option value="1">Other</option><option value="NULL">NONE</option>'
                       html+='</select></td><td>Priority : &nbsp;<input type="text" name="priority" id="priority" value="" style="width:40px;"></td></tr></table></div></td></tr>\n\
                        <tr><td>&nbsp;</td></tr><tr><td align="center"><input type="button"  value="save" class="navtitle-hover" onclick="saveFactFilterDtls('+"'"+ctxtPath+"'"+','+<%=reportId%>+','+"'"+measEleId+"'"+','+"'"+paramFilterName+"'"+','+folderId+','+"'"+subfolderId+"'"+')"></td></tr>\n\
                      <tr><td align="center">FactFilter is not applied for Time Based Formula</td></tr></table> </tbody></table>'
                     <%
                           }
                    %>
                    parent.$("#paramFilterMeasureValFrm").html(html);
                    parent.$("#paramFilterMeasureValues").dialog('open');
                           }
                      });
                    }
                            });
                    }
                }else{
                    alert("Please enter filter name")
                }

            }



           function checkforDimenssion(){
           if($("#dimenssion").is(':checked')){
               $("#facts").removeAttr("checked");
               $("#ParamName").show();
              $("#dimType").show();
               $("#fType").hide();
               $("#staticTr").show();
               $("#dimCalOp").show();
           }
          else{
               $("#dimType").hide();
           }
                }
           function changeDimenssion(id){
              var dimtype;
              dimtype=$("#dimenssionType").val();
              var paramfilter=$("#paramFilter option:selected").text();
              var paramFilterName=document.getElementById("ParamFilterName").value;
              var html=""
              var selectedParam=document.getElementById("paramFilter").value;
              var measEleId='<%=measEleId%>';
              var ctxtPath='<%=request.getContextPath()%>';
                if(dimtype=="in" || dimtype=="not in")
                   {
                      parent.$("#dimTypeDiv").dialog('close');
                    }else{
                        html="<tr><td>"+paramfilter+"</td>&nbsp;&nbsp;<td>"+dimtype+"</td>";
                        html=html+"<td><input type='text' name='dimvalue' id='dimvalue' style='width:150px'></td></tr><tr>&nbsp;&nbsp;</tr>";
                        html=html+"<tr><td colspan='4' align='center'><input type='button' name='Done' value='Done' class='navtitle-hover' onclick='saveDimensionValues("+selectedParam+","+'"dimvalue",'+'"'+paramFilterName+'"'+","+<%=reportId%>+","+'"'+measEleId+'"'+","+'"'+dimtype+'"'+","+'"'+ctxtPath+'"'+")'></td></tr>";
                        parent.$("#dimTypeDiv").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 400,
                        position: 'justify',
                        modal: true
                        });
                        if(trim($("#ParamFilterName").val())!=""){
                        parent.$("#dimTable").html(html);
                        parent.$("#dimTypeDiv").dialog('open');
                        }else{
                            alert("plz enter filter name")
                        }
                    }


            }
            function checkforFacts(){

              if($("#facts").is(':checked')){
                  $("#fType").show();
                   $("#dimenssion").removeAttr("checked");
                    $("#dimType").hide();
                    $("#ParamName").hide();
                    $("#staticTr").hide();
                    $("#dimCalOp").hide();
                   }
              else
                 $("#fType").hide();
            }
            function checkfordimStaticVal(){
                if($("#dimstaticVal").is(':checked')){
                   $("#staticVal").show();
                }else
                  $("#staticVal").hide();
            }
            function checkfordimCal(){
                if($("#dimCal").is(':checked')){
                    $("#dimCalOperator").show();
                    $("#dimType").hide();
                }
                else{
                   $("#dimCalOperator").hide();
                   $("#dimType").show();
            }
            }
        </script>
    </head>
    <body>
        <form action="" name="paramFilterForm" method="post">
            <input type="hidden" name="path"  id="path">
            <Table  width="100%" border="0">
                <% if(FactFilter==null){%>
                <tbody>
                    <tr>
                        <td><input type="checkbox" name="dimession" id="dimenssion" onclick="checkforDimenssion()" checked>Dimension</td>
                        <td><input type="checkbox" name="dimession" id="facts" onclick="checkforFacts()">Facts</td>
                    </tr>
                    <Tr>
                        <Td>Parameter Filter Name</Td>
                        <Td>
                            <input type="text" name="ParamFilterName" id="ParamFilterName" style="width:150px">
                        </Td>
                    </Tr>
                    <Tr id="ParamName">
                        <Td>Parameter</Td>
                        <Td>
                            <select name="paramFilter" id="paramFilter" style="width:150px">
                                <%
                                            for (int i = 0; i < allViewbyIdsStr.length; i++) {
                                                //if (paramFilter.equalsIgnoreCase(grpNbrFormats[i])) {%>
<!--                                <option selected value="<%//=grpNbrFormats[i]%>"><%//=grpNbrFormatsDisp[i]%></option>-->
                                <%//} else {%>
                                <option value="<%=allViewbyIdsStr[i]%>"><%=allViewbyNamesStr[i]%></option>
                                <%//}
                                            }%>
                            </select>
                        </Td>
                    </Tr>
                    <tr id="dimType">
                      <td>Dimension Type</td>
                      <td>
                          <select id="dimenssionType" name="dimenssionType" style="width:150px" onchange="changeDimenssion(this.id)">
                              <%for(int i=0;i<dimenssionType.length;i++){%>
                              <option value="<%=dimenssionType[i]%>"><%=dimenssionType[i]%></option>
                              <%}%>
                          </select>
                      </td>
                    </tr>
                    <tr id="staticTr">
                        <td><input type="checkbox" name="dimstaticVal" id="dimstaticVal" onclick="checkfordimStaticVal()">Static Value</td>
                        <td><input type="text" name="staticVal" id="staticVal" style="width:150px;display:none"></td>
                    </tr>
                    <tr id="dimCalOp">
                        <td><input type="checkbox" name="dimCal" id="dimCal" onclick="checkfordimCal()">Dim Calculation</td>
                        <td>
                            <select id="dimCalOperator" name="dimCalOperator" style="width: 150px;display:none">
                                <option value="add">+</option>
                                <option value="sub">-</option>
                                <option value="mul">*</option>
                                <option value="div">/</option>
                            </select>
                        </td>

                    </tr>
                    <tr id="fType" style="display:none">
                        <%}else{%>
                        <Tr>
                        <Td>Fact Filter Name</Td>
                        <Td>
                            <input type="text" name="ParamFilterName" id="ParamFilterName" style="width:150px">
                        </Td>
                        </Tr>
                         <tr id="fType">
                             <%}%>
                       <td>Fact Names</td>
                        <td>
                            <select id="factNames" name="factNames" style="width:150px" onchange="changeFacts(this.id)">
                               <% for(int i=1;i<allfactNames.length;i++){%>
                               <option value="<%=allfactNames[i]%>&<%=subfolderIds[i]%>"><%=allDispNames[i]%></option>
                                <%}%>
                            </select>
                        </td>

                    </tr>

                </tbody>

                <tfoot>
                    <Tr>
                        <td align="center" colspan="4">
                            <input type="button" name="Next" value="Next" class="navtitle-hover" onclick="goNext('<%=reportId%>','<%=measEleId%>','<%=disColumnName%>')">
                        </td>
                    </Tr>
                </tfoot>
            </Table>
        </form>

    </body>
</html>
