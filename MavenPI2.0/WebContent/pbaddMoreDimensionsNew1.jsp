<%--
    Document   : pbaddMoreDimensions  // for adding more dimensions.
    Created on : 23 Jun, 2012, 3:37:40 PM
    Author     : veenadhari.g
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.report.PbReportCollection,com.progen.reportdesigner.db.ReportTemplateDAO,java.util.*,java.text.*,java.math.*,prg.db.Container,com.progen.report.display.DisplayParameters,prg.db.PbReturnObject,utils.db.ProgenConnection,prg.db.PbDb"%>

<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }
            String contextPath=request.getContextPath();
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.8.3.min.js" type="text/javascript"></script>
<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>-->
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.8.23-custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/jquery-1.8.23-custom.min.css" rel="stylesheet" />

        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/reportDesign.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <link href="<%=contextPath%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script  type="text/javascript" src="<%=contextPath%>/javascript/jquery.contextMenu.js" ></script>
        <script  type="text/javascript" src="<%=contextPath%>/javascript/pbReportViewerJS.js" ></script>
        <%



                    String DimData = "";
                    String reportId = "";
                    String userId = "";
                    String roleId = "";
                    String IsrepAdhoc2 = "";
                    HashMap map = null;
                    Container container = null;
                    HashMap ParametersHashMap = null;
                    ArrayList Parameters = null;
                    ArrayList ParametersNames = null;
                    String ParamRegion = "";
                    ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
                    StringBuffer paramStr = new StringBuffer();
                    StringBuffer paramNameStr = new StringBuffer();
                    String prevCols = "";
                    String prevColNames = "";
                    String ctxPath=request.getContextPath();
                    if (request.getAttribute("dims") != null) {
                        DimData = String.valueOf(request.getAttribute("dims"));
                    }

                    if (request.getAttribute("reportId") != null) {
                        reportId = String.valueOf(request.getAttribute("reportId"));
                        if (session.getAttribute("PROGENTABLES") != null) {
                            map = (HashMap) session.getAttribute("PROGENTABLES");
                            if (map.get(reportId) != null) {
                                container = (Container) map.get(reportId);
                                if (container.getReportType().equalsIgnoreCase("I")) {
                                    ParametersHashMap = container.getParametersHashMap();
                                    Parameters = (ArrayList) ParametersHashMap.get("Parameters");
                                    ParametersNames = (ArrayList) ParametersHashMap.get("ParametersNames");
                                    
                                    if (Parameters != null) {
                                        ParamRegion = reportTemplateDAO.getParameterDisplayData((String[]) Parameters.toArray(new String[0]), (String[]) ParametersNames.toArray(new String[0]));
                                        for (int i = 0; i < Parameters.size(); i++) {
                                            paramStr.append(",").append(Parameters.get(i).toString());
                                            paramNameStr.append(",").append(ParametersNames.get(i).toString());
                                        }
                                        prevCols = paramStr.substring(1);
                                        prevColNames = paramNameStr.substring(1);
                                    } else {
                                        prevCols = "";
                                        prevColNames = "";
                                    }
                                }
                            }
                        }
                        if (String.valueOf(request.getAttribute("isOneview")) != null && String.valueOf(request.getAttribute("isOneview")).equalsIgnoreCase("true")) {
                            request.setAttribute("session", session);
                            ParamRegion = reportTemplateDAO.getParameterForOneView(reportId, session);

                        }
                    }

                    PbReportCollection collect = new PbReportCollection();
                    collect = container.getReportCollect();
                    ArrayList<String> timeinfo = collect.timeDetailsArray;
                    String[] vals1= null;
                    String sytm = "No";
                     if (collect.timeDetailsArray != null) {
                                     String autoDate = "";
                                     String vals = " ";
                                     vals = timeinfo.toString();
                                     vals = vals.replace("[", "");
                                     vals = vals.replace("]", "");
                                     vals1 = vals.split(",");

                                     container.evaluateReportDateHeaders();
                                 }
                    if (request.getAttribute("USERID") != null) {
                        userId = String.valueOf(request.getAttribute("USERID"));
                    }
                    if (request.getAttribute("roleId") != null) {
                        roleId = String.valueOf(request.getAttribute("roleId"));
                    }
                    if (request.getAttribute("IsrepAdhoc1") != null) {
                        IsrepAdhoc2 = String.valueOf(request.getAttribute("IsrepAdhoc1"));
                    }
        %>
        <script type="text/javascript">
             $(document).ready(function() {

             if ('#dragDiv3') {
                alert("Blank")
    }else{alert("Not Blank")}
               $(function () {
                    $("input[type='checkbox']").change(function () {
                        //alert('@');
                        $(this).siblings('ul')
                        .find("input[type='checkbox']")
                        .attr('checked', this.checked);
                        $('#dragDiv3').append("<ul id='newList'></ul>");
                        $('#newList').empty();
                        var list;
                        $('.childNodes').each(function( index ) {
                            var current=$(this);
                            var list_id=current
                            .find('input[type=\'checkbox\']:checked').map(function() {
                                return $(this).attr('id');
                            }).get();
                            var list_name=current
                            .find('input[type=\'checkbox\']:checked').map(function() {
                                return $(this).val();
                            }).get();
                            var list=current
                            .find('input[type=\'checkbox\']:checked').map(function() {
                                return $(this).parent().text();
                            }).get();
                            for (cnt = 0; cnt < list.length; cnt++) {
                                $("#newList").append("<li id='"+list[cnt]+"^"+list_id[cnt]+"'>"+list[cnt]+"</li>");
                            }
                        });
                        alert("sgdh========")
                        var someList=$(this).siblings('ul')
                        .find('input[type=\'checkbox\']:checked').map(function() {
                            return $(this).parent().text();
                        }).get();

                        //var someList = $('input[type=\'checkbox\']:checked')
                        //alert(checkValues);

                        for (cnt = 0; cnt < someList.length; cnt++) {
//                            $("#newList").append("<li>"+someList[cnt]+"</li>");
                        }
//                        saveDims(<%= reportId%>, "true")
if(document.getElementById("dragDiv").style.display=="none")
    {
        document.getElementById("dragDiv").style.display="block";
    }
                    });
                });
                showListInDesigner('<%=ctxPath%>','');
                $("#DimTree").treeview({
                    animated: "normal",
                    unique:true
                });

                //addeb by bharathi reddy fro search option
                $('ul#DimTree li').quicksearch({
                    position: 'before',
                    attached: 'ul#DimTree',
                    loaderText: '',
                    delay: 100
                })
                $(".formulaViewMenu").contextMenu({
                    menu: 'formulaViewListMenu',
                    leftButton: true },
                function(action, el, pos) {
                    contextMenuWorkFormulaView(action, el, pos);
                });
                $("#formulaViewDiv").dialog({
                    //bgiframe: true,
                    autoOpen: false,
                    height: 150,
                    width: 250,
                    position: 'absolute',
                    modal: true
                });
            });

            $(function() {
                var dragKpi=$('#kpis > li > ul > li >ul > li > span,#kpis > li > ul > li >  span')
                var dropKpis=$('#draggableKpis');

                $(dragKpi).draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });

                $(dropKpis).droppable(
                {
                    activeClass:"blueBorder",
                    accept:'#kpis > li > ul > li >ul > li > span,#kpis > li > ul > li >  span',
                    drop: function(ev, ui) {
                        var kpi=ui.draggable.html();
                        createDims(ui.draggable.html(),ui.draggable.attr('id'));
                    }
                });
            });

            $(function() {
                $("#sortable").sortable();
                $("#sortable").disableSelection();
            });
        </script>
      

        <style type="text/css">
            .myhead
            {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 8pt;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                width:50%;
                background-color:#EAF2F7;
                border:0px;
                /*apply this class to a Headings of servicestable only*/
            }
            .prgtableheader
            {   font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 11px;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                background-color:#EAF2F7;
                height:100%;
            }
            .btn {
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
            }
            *{font:11px verdana}
        </style>
    </head>
    <body>

        <form name="myForm2" method="post">
            <table width="98%" style="height: 100%; margin-top: 5px;border-width: 2px; border-style: groove; border-color: skyblue;  ">
                <tr style="height: 8%">
                    <td>
<!--                        <div style="height: 100%;border-width: 0px; border-style: solid; border-color: grey;  ">
                        </div>-->
                    </td>
                </tr>
                <tr style="height: 5%">
                    <td>
                        <div style="height: 100%;border-width: 0px; border-style: solid; border-color: grey;  ">
<!--                            <div style="float: right"><label for="reportName" style="font-weight: bold">Enter Report Name<font color="red">*</font> : </label><input type="text" id="reportName" size="24" /></div>-->
                        </div>
                    </td>
                </tr>
                <tr style="height: 87%">
                    <td>

                        <div style="height: 100%;border-width: 1px; border-style: solid; border-color: grey;  ">
                            <div style="height: 100%;width: 100%;border-width: 0px;  border-style: none; border-color: white; border-collapse: collapse; ">
                                <table style="width:100%;height:85%;margin-left:  0px;margin-top: 5px;margin-bottom: 5px; margin-right: 5px;"  cellspacing="15" >
                                    <tr>
                                        <td width="33%" valign="top" class="draggedTable1" style="border: 1px solid black;">
                                            <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold;">Select Report Filter</font></div>
                                            <div class="masterDiv" style="height:400px;overflow-y:auto">
                                                <ul id="DimTree" class="filetree treeview-famfamfam">
                                                    <li class="open" style="background-image:url('images/treeViewImages/plus.gif')">
                                                        <ul id="kpis">


                                                            <%=DimData%>
                                                        </ul>
                                                    </li>
                                                </ul>
                                            </div>
                                        </td>
                                        <td width="33%" valign="top"  id="draggableKpis" valign="top"style="border: 1px solid black; ">
                                            <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                                <font size="2" style="font-weight:bold">Select Report Column</font>
                                            </h3>
                                            <div id="dragDiv" style="height:400px;display: none;">
                                                <iframe id="dataDispmem" NAME='dataDispmem' frameborder="0" width="100%" height="100%" SRC='#'></iframe>

                                            </div>
                                        </td>
                                        <td width="34%" valign="top"  id="draggableKpis" valign="top" >

<!--                                                <ul id="sortable">
                                                </ul>-->
<!--            <iframe  id="editViewByFrame" name='editViewFrame' width="100%" height="100%" frameborder="0"   src='about:blank'></iframe>-->

                                            <div id="dragDiv3" style="height:210px;overflow-y:auto;border: 1px solid black; margin-bottom: 10px">
                                            <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                                <font size="2" style="font-weight:bold">Selected Report Filter </font>
                                            </h3>
                                           
                            <ul id="sortable">
                                <%=ParamRegion%>
                            </ul>
                     
                                            </div>

                                            <div id="dragDiv1" style="height:210px;overflow-y:auto;border: 1px solid black;">
                                            <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                                <font size="2" style="font-weight:bold">Selected Report column </font>
                                            </h3>
 <ul id="sortable">
                                <%=ParamRegion%>
                            </ul>
                                            </div>
                                        </td>
                                    </tr>
                                </table>

                                <!--                                <div style=" float: left;height: 100%;width: 33%;border-width: 1px;  border-style: solid; border-color: grey;  ">
                                                                </div>
                                                                <div style="float: left;height: 100%;width: 33%;border-width: 1px;  border-style: solid; border-color: grey;  ">
                                                                </div>
                                                                <div style="float: left;height: 100%;width: 33%;border-width: 1px;  border-style: solid; border-color: grey;  ">-->

                            </div>
                        </div>
                    </td>

                </tr>
            </table>
       <input type="button"  class="navtitle-hover" onclick="createreport1()" style="margin-top: 10px;width:auto; position: fixed;left: 620px;" value="Done" >

        </form>



        <br/>
        <center>
            <!--
            <%
                        if (String.valueOf(request.getAttribute("isOneview")) != null && String.valueOf(request.getAttribute("isOneview")).equalsIgnoreCase("true")) {%>
            <input type="button"  class="navtitle-hover" style="width:auto" value="Done" onclick="saveDimsForTrend('<%=reportId%>','<%=roleId%>')">
            <% } else {%>
            <input type="button"  class="navtitle-hover" style="width:auto" value="Done" onclick="saveDims('<%=reportId%>','<%=IsrepAdhoc2%>')">
            <% }%>
            -->

            <%--<input type="button"  class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelKpi()">--%>
        </center>
        <ul id="formulaViewListMenu" class="contextMenu" style="width:100px">
            <li class="view"><a href="#view">View</a></li>
        </ul>
        <div id="formulaViewDiv" title="View Custom Measure" style="display:none">
            <table>
                <tr>
                    <td id="value"></td>
                </tr>
            </table>
        </div>
      <script type="text/javascript">


            var flag='y';
            var y="";
            var xmlHttp;
            var ctxPath;
            var prevColsStr="<%=prevCols%>"
            var prevColNames="<%=prevColNames%>"
            var prevCols=prevColsStr.split(",");
            var preNames=prevColNames.split(",")
            var From=window.parent.$("#Designer").val();

            for(var k=0;k<prevCols.length;k++){
                var pr= dimArray.toString();
                //alert("pr"+pr)
                if(pr.match(prevCols[k])==null){
                    dimArray.push(preNames[k]+"^elmnt-"+prevCols[k])
                }
            }

            function measureSelect(selectedDimId)
            {
//                if(flag=='y')
//                {
//                    alert("yes");
//                    flag='n';
//                }
//                else
//                {
//                    alert("no");
//                }
            }
                    function saveDims(reportId,IsrepAdhoc2){
                //                alert("rep id : "+reportId+":"+IsrepAdhoc2);
//                alert("@@@:saveDims1");
                if(IsrepAdhoc2!=="true"){
                    //modified by Aditi on 12th May 14
                    alert("Changes Will Be Reflected Once You Reset The Report");
                }
                var From = "";
                var action=null;
                if(parent.document.getElementById("Designer") != null)
                    From=parent.document.getElementById("Designer").value;
                //                    alert("From : "+From);
                if(From=="fromDesigner"){
                    dispDesignerDims1(reportId)
                }else if(From=="fromInsightDesigner"){
                    // alert("fromInsightDesigner")ParamViewBychange
                    action=parent.document.getElementById("action").value;
                    if(action!=null && action=="ParamViewBychange"){
                        dispInsightDims(reportId)
                    }else
                        dispInsightDesiignerDims(reportId)
                } if(From=="busTemplateView"){
                    dispTemplateDims(reportId)
                }
                else{
//                    alert("@@@:dispdims1");
 if(From=="fromDesigner"){

                  }else{
                    dispDims1(reportId);
                }
                }
                //                alert("iam in pbaddmoreDimensions.jsp")
            }
            function dispTemplateDims(reportId){
                alert("called...");
                var roleId='<%=roleId%>';
                var dims="";
                var dimName="";
                var dimUl=document.getElementById("sortable");
                var dimIds=dimUl.getElementsByTagName("li");
                var oneviewType=parent.document.getElementById("oneviewType").value;
                var dimsize=5;
                if(oneviewType=="Business TemplateView")
                    dimsize=10;
                if(dimIds.length!=0 && dimIds.length>dimsize){
                    alert("Please select only "+dimsize+" dimenssions");
                }else{
                    //                   parent.$("#AddMoreParamsDiv").dialog('close');
                    for(var i=0;i<dimIds.length;i++){
                        var dkpiIds=(dimIds[i].id).split("^");
                        dims=dims+","+dkpiIds[1];
                        dimName=dimName+","+dimIds[i].id.toString().substr(0, dimIds[i].id.toString().lastIndexOf("^"));
                    }
                    if(dimIds.length!=0){
                        dims=dims.substr(1).replace("elmnt-", "","gi");
                        dimName=dimName.substr(1);
                        dimName=dimName.replace("%","%".charCodeAt(0),"gi");
                        //                  if(oneviewType=="Measure Based Business Template"){
                        //                      dims+="TIME";
                        //                     dimName+="Time"
                        //                 }
                    }
                    var timeDetail = "Time-Period Basis";
                    var timeDim='AS_OF_DATE,PRG_PERIOD_TYPE,PRG_COMPARE';
                    $.ajax({
                        url:'reportTemplateAction.do?templateParam=buildParams&REPORTID='+reportId+'&params='+dims+'&paramNames='+dimName+'&foldersIds='+roleId+'&dimId='+timeDetail+'&timeparams='+timeDim+'&dispDesignerDims='+"dispDesignerDims"+'&fromDesigner=insightDesigner',
                        success:function(data)
                        {
                            //alert("parameters")
                            parent.cancelDim();
                            parent.$("#measuresDialog").dialog('open');
                            var frameObj=parent.document.getElementById("dataDispmem");
                            var source="reportTemplateAction.do?templateParam=getMeasures1&foldersIds="+roleId+'&REPORTID='+reportId;
                            //            var source="reportTemplateAction.do?templateParam=getMeasures&foldersIds="+roleId+'&paramNames='+dimName+'&isdimensionbtns=true&REPORTID='+reportId;

                            frameObj.src=source;
                        }});
                }
            }
            function saveDimsForTrend(oneViewId,roleId){
                $("#AddMoreParamsDiv").dialog('close');
                var dims="";
                var dimName="";
                var dimUl=document.getElementById("sortable");
                var dimIds=dimUl.getElementsByTagName("li");
                parent.saveDimsForTrend1(dimUl,dimIds,oneViewId,roleId);
            }

            //            function cancelKpi(){
            //                parent.cancelRepKpi();
            //            }
            function setValueToContainerInDesigner(ctxpath,repId,bizRoles,tabLst){
//    alert("Ctx:"+ctxpath+"RepID:"+repId+"role:"+bizRoles);
       var frameObj=document.getElementById("dataDispmem");
            $.post(ctxpath+"/reportViewer.do?reportBy=setTableListToContainer&repId="+repId+'&tabLst='+tabLst,
                function(data){
                    var source="reportTemplateAction.do?templateParam=getMeasures1&foldersIds="+bizRoles+'&REPORTID='+repId+'&tableList=true';
//                    alert(source);
                    frameObj.src=source;
                });
   }
            function showListInDesigner(ctxpath,paramslist){

//        alert("Params : "+paramslist+"ctx:"+ctxPath);
//        alert(<%=roleId%>);
           $.post(ctxpath+"/reportViewer.do?reportBy=getTableList&paramslist="+paramslist+'&currentBizRoles='+<%=roleId%>,
                function(data){
//                    alert(data);
                    var jsonVar=eval('('+data+')')
                    var json1 = jsonVar.idsList.split(",");
                    var jsonname = jsonVar.namesList.split(",");
                    var tablist='';
                    for(var i=0;i<json1.length;i++)
                    {
                        tablist=tablist+json1[i];
                        if(i!=json1.length-1)
                            {
                                tablist+=",";
                            }
                    }
//                    alert(tablist);
                    for(var i=0;i<json1.length;i++){
//                        alert(jsonname[i]+":"+json1[i]);
                        setValueToContainerInDesigner(ctxpath,<%=reportId%>,<%=roleId%>,tablist);
                    }
//                    $("#paramVals").html(htmlVar);

                });
    }
           
            function contextMenuWorkFormulaView(action, el, pos){

                document.getElementById("value").innerHTML=$(el).attr('title');
                $("#formulaViewDiv").dialog('open');


            }
//
            function dispMeasures(){
//                alert("@@@:disp measures");
                var msrs="";
                var msrUl=document.getElementById("newList1");
                var msrIds=msrUl.getElementsByTagName("li");
                 if(msrIds==null){
                    alert("Please Select the Measure!")
                }
                for(var i=0;i<msrIds.length;i++){
                    var measureIds=(msrIds[i].id).replace("Msr","");
                    msrs=msrs+","+measureIds;
                }
//                alert("!!!!:Measures"+msrs);
                var action=null;
                if(From != null && From=="fromInsightDesigner")
                    action=parent.document.getElementById("action").value;
                if(From != null && From=="busTemplateView")
                    action=parent.document.getElementById("action1").value;
                if(msrIds.length!=0){
                    msrs=msrs.substring(1);
                    parent.document.getElementById("MsrIds").value=msrs;
                    parent.document.getElementById("Measures").value=msrs;

                    if(action!=null && action=="measChange"){
                    $.ajax({
                            url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=tableMeasureChanges&tableMsrs='+msrs+'&REPORTID=<%=reportId%>',
                            success: function(data){
                                parent.PreviewTable();
                            }
                            });
                    }else{
                    $.ajax({
                        url: 'reportTemplateAction.do?templateParam=buildTable&buildTableChange=Measures&Msrs='+msrs+'&MsrsNames='+msrs+'&REPORTID=<%=reportId%>',
                        success: function(data) {
//                            alert("Measures")
                            parent.PreviewTable();
                            if(data!=""){
                            }
                        }
                    });
                }
//                    parent.cancelTabMeasure();
                    window.parent.$("#GraphsRegOfReport").show();
                }
                else{
                    alert("Please Select Measures");
                    window.parent.$("#measuresDialog").dialog('open');
                }
            }
            function createreport1()
            {
//                alert("@@@:create report");
                saveDims(<%=reportId%>, "true");
               // alert("@@@@:"+window.parent.document.getElementById('REPORTNAMEbeforeSave').value);
                setTimeout(function()
                {
                saveMeasures();
                window.parent.document.getElementById('submitReportForm').style.display='block';


                }, 1000);
                window.parent.document.getElementById('desginerDiv11').style.display='none';
            }

            function saveMeasures(){
//                alert("####:saveMeasures");
                if(From != null && From=="busTemplateView"){
                    dispBusTemplateMesures();
                }else{
//                 parent.cancelTabMeasure();
                dispMeasures();
                }
                if(From != null && From=="fromInsightDesigner" && From=="busTemplateView"){
//                if(From != null && From=="fromInsightDesigner"){
                }else{
                <%  if(collect.timeDetailsArray!=null && timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE") && collect.timeDetailsMap!=null  && !collect.timeDetailsMap.isEmpty()){%>

                      var html="";
                      html+=parent.$("#dateregionRange").html();
                  parent.$("#dateRegion").html(html);
                  parent.$("#field1").html("<%=container.fullName0%>");
                  parent.$("#field2").html("<%=container.dated%>");
                  parent.$("#field3").html("<%=container.day0%>");
                  parent.$("#tdfield1").html("<%=container.fullName%>");
                  parent.$("#tdfield2").html("<%=container.date%>");
                  parent.$("#tdfield3").html("<%=container.day%>");
                  parent.$("#cffield1").html("<%=container.cffullName%>");
                  parent.$("#cffield2").html("<%=container.cfdate%>");
                  parent.$("#cffield3").html("<%=container.cfday%>");
                  parent.$("#ctfield1").html("<%=container.ctfullName%>");
                  parent.$("#ctfield2").html("<%=container.ctdate%>");
                  parent.$("#ctfield3").html("<%=container.ctday%>");

                parent.$( "#fromdate" ).datepicker({
                showOn: "button",
                buttonImage: "images/calendar_18x16.gif",
                buttonImageOnly: true,
                showButtonPanel: true,
                changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1,
                dateFormat: "D,d,M,yy"
            }).datepicker("setDate", new Date(('<%=vals1[2]%>')) );
                parent.$( "#todate" ).datepicker({
                showOn: "button",
                buttonImage: "images/calendar_18x16.gif",
                buttonImageOnly: true,
                showButtonPanel: true,
                changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1,
                dateFormat: "D,d,M,yy"
            }).datepicker("setDate", new Date(('<%=vals1[3]%>')) );
                parent.$( "#comparefrom" ).datepicker({
                showOn: "button",
                buttonImage: "images/calendar_18x16.gif",
                buttonImageOnly: true,
                showButtonPanel: true,
                dateFormat: "D,d,M,yy",
                 changeYear: true,
                  changeMonth: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1

            }).datepicker("setDate", new Date(('<%=vals1[4]%>')) );
                 parent.$( "#compareto" ).datepicker({
                showOn: "button",
                buttonImage: "images/calendar_18x16.gif",
                buttonImageOnly: true,
                showButtonPanel: true,
                changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1,
                dateFormat: "D,d,M,yy"

            }).datepicker("setDate", new Date(('<%=vals1[5]%>')) );
            parent.$("#dateregionRange").show();
//            parent.document.getElementById("saveadhoc").style.display='none';

               <% } else if(collect.timeDetailsArray!=null && timeinfo.get(1).equalsIgnoreCase("PRG_STD") && collect.timeDetailsMap!=null  && !collect.timeDetailsMap.isEmpty()) { %>

                var html="";
                html+=parent.$("#dateregionDiv").html();
               // alert("hhhhhhhhh"+html)
                <% if( sytm.equalsIgnoreCase("No")) { %>   //for year vs year by mohit
                  parent.$("#pfield1").html("<%=(container.fullName0).substring(0,4)+(container.fullName0).substring(6)%>");
                  parent.$("#pfield2").html("<%=container.dated%>");
                  parent.$("#pfield3").html("<%=container.day0%>");
                parent.$( "#perioddate" ).datepicker({
                showOn: "button",
                buttonImage: "images/calendar_18x16.gif",
                buttonImageOnly: true,
                showButtonPanel: true,
                changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1,
                dateFormat: "D,d,M,yy"
            }).datepicker("setDate", new Date(('<%=vals1[2]%>')) );
            <%}else {

        //System.out.print("?mohit>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+container.fullName0);%>
                    <% String calndrYear = (container.fullName0).substring(4);
                    String s="";
                            for(int year=2005;year<=2020;year++){
                            if(calndrYear.equalsIgnoreCase(Integer.toString(year))){

                             s+="<option selected value="+year+">"+year+"</option>";
                           }else {
                                s+="<option value="+year+">"+year+"</option>";
                                }}%>

            //parent.$("#pfield1").html("<%=(container.fullName0).substring(4)%>");
            parent.$("#calPeriodYear").append("<%=s%>");
            //alert("<%=s%>")
            //parent.$('#calPeriodYear option:selected').val(<%=(container.fullName0).substring(4)%>)
            <%}%>

           <%
              DisplayParameters dur=new DisplayParameters();
              String duration1= String.valueOf(dur.displayTime(collect.timeDetailsMap)) ;
           %>
                  parent.$("#timebasistd").html('<%=duration1%>');
                  parent.$("#dateregionDiv").show();
                  parent.document.getElementById("saveadhoc").style.display='none';
                  <% }
                  if(collect.timeDetailsArray!=null){
                   container.evaluateReportDateHeaders();}
                  %>
            }
            }
        </script>
    </body>
</html>