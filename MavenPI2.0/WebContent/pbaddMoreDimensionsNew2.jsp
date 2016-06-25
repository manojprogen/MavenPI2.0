<%--
    Document   : pbaddMoreDimensions  // for adding more dimensions.
    Created on : 23 Jun, 2012, 3:37:40 PM
    Author     : veenadhari.g
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbReturnObject,utils.db.ProgenConnection,prg.db.PbDb,prg.db.Container,java.math.*,java.text.*,java.util.*,com.progen.reportdesigner.db.ReportTemplateDAO,com.progen.report.display.DisplayParameters,com.progen.report.PbReportCollection"%>
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
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom_new.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery.editablediv.js"></script>
        <script type="text/javascript" src="<%=contextPath%>https://code.jquery.com/jquery-1.11.2.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.8.23-custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/jquery-1.8.23-custom.min.css" rel="stylesheet" />
        <link rel="stylesheet" type="text/css" href="<%=contextPath%>/css/global.css"/>
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
                    String ctxPath = request.getContextPath();
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
                    String[] vals1 = null;
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
               // showListInDesigner('<%=ctxPath%>','');
               // alert("jkghfjhjhl")
                if(document.getElementById("measureTree").style.display=="none")
                        {
                            document.getElementById("measureTree").style.display="block";
            }
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
                       function saveDims1(reportId,IsrepAdhoc2){
//                                alert("rep id : "+reportId+":"+IsrepAdhoc2);
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
                    dispDesignerDims2(reportId)
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
                                   //   alert("@@@:dispdims1");
                    if(From=="fromDesigner"){
 
                  }else{
                    dispDims2(reportId);
                }
                }
                //                alert("iam in pbaddmoreDimensions.jsp")
            }
               var reportId;
       function dispDesignerDims2(reportId){
//    alert("***");
//alert("@@@:dispdes");
    var dims="";
    var dimName="";
    var dimUl=document.getElementById("sortable");
    if(dimUl==null){
        alert("Please Select the Dimension!")
    }
    //alert(dimUl);
    var dimIds=dimUl.getElementsByTagName("li");

//    alert(JSON.stringify(dimIds));
    reportId = parent.document.getElementById("REPORTID").value;
    var roleId=parent.document.getElementById("roleid").value;
      var ReportLayout = parent.document.getElementById("ReportLayout").value;
   // alert(ReportLayout);

    for(var i=0;i<dimIds.length;i++){
        var dkpiIds=(dimIds[i].id).split("^");
        dims=dims+","+dkpiIds[1];
//        alert(dims);
        dimName=dimName+","+dimIds[i].id.toString().substr(0, dimIds[i].id.toString().lastIndexOf("^"));
    }
    if(dimIds.length!=0){
        dims=dims.substr(1).replace("elmnt-", "","gi");
        dimName=dimName.substr(1);
        dimName=dimName.replace("%","%".charCodeAt(0),"gi");
    }
  var timeDetail = parent.document.getElementById("time").value
    //var timeDetail = parent.document.getElementsByName("time");
//           var selectdDetails = "";
            var timeDim="";
//           if(timeDetail.length!=0){
//                    for(var i=0;i<timeDetail.length;i++){
//                        if(timeDetail[i].checked){
//                            selectdDetails=selectdDetails+","+timeDetail[i].id;
//                        }
//                    }
//                }
//                if (selectdDetails!=""){
//                    selectdDetails=selectdDetails.substr(1,selectdDetails.length);
//                }
                if(timeDetail == "StandardTime"){
                    timeDetail = "Time-Period Basis";
                     timeDim='AS_OF_DATE,PRG_PERIOD_TYPE,PRG_COMPARE';
                    var timeDimension=timeDim.split(",");
                    for(var i=0;i<timeDimension.length;i++){
                        timeDimArray.push(timeDimension[i]);
                    }
                } else if(timeDetail == "RangeBasis"){
                    timeDetail = "Time-Range Basis";
                    timeDim='AS_OF_DATE1,AS_OF_DATE2,CMP_AS_OF_DATE1,CMP_AS_OF_DATE2';
                    timeDimension=timeDim.split(",");
                    for(var i=0;i<timeDimension.length;i++){
                        timeDimArray.push(timeDimension[i]);
                    }
                }
                else{
                     timeDetail = "";
                    timeDimension=timeDim.split(",");
                    for(var i=0;i<timeDimension.length;i++){
                        timeDimArray.push(timeDimension[i]);
                    }
                }
//    alert("dims"+dims)
//var ctxPath=document.getElementById("h").value;S
//alert("@@@:first call");
$.ajax({
        async:false,
        url:'reportTemplateAction.do?templateParam=buildParams&REPORTID='+reportId+'&params='+dims+'&paramNames='+dimName+'&foldersIds='+roleId+'&dimId='+timeDetail+'&timeparams='+timeDim+'&dispDesignerDims='+"dispDesignerDims"+'&ReportLayout='+ReportLayout,
        success:function(data)
        {
//            alert("DDDDDD : "+JSON.stringify(data));
            var REPORTID = parent.document.getElementById("REPORTID").value;
            var ctxPath=parent.document.getElementById("h").value;
            var frameObj = parent.document.getElementById("editViewByFrame");
            var designer = parent.document.getElementById("Designer").value;
//            alert("1.report id : "+REPORTID +"ctxpath : "+ctxPath+"designer : "+designer);
//            alert("@@@:second call");
            $.post(ctxPath+'/reportViewer.do?reportBy=showViewBy&REPORTID='+REPORTID+'&ctxPath='+ctxPath+'&fromdesigner='+designer,
                function(data){
//                alert(JSON.stringify(data));

                    if(data=='NoViewBys'){
                        alert("Please Select Paramters From Plus Icon")
                    }
                    else{
////                        alert("@@@:Here!"+frameObj)
                        //parent.$("#editViewByDiv").dialog('open'); // comment by mohit for create report
//                        var source = ctxPath+"/Report/Viewer/ChangeViewByAORep.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath;
//                        frameObj.src = source;
////
        }
});
        }
});

//      parent.cancelDim();
completeTest()
 }
  var allViewNamesNew;
 var  allViewIdsNew;
 function completeTest()
 {
     
      var ctxPath=parent.document.getElementById("h").value;
      
      
      $.ajax({
                        async:false,
                         url: "reportViewerAction.do?reportBy=getAllViewByIdandName&REPORTID="+reportId,
  success: function(data){
                           var jsonVar=eval('('+data+')');
         allViewNamesNew=jsonVar.allViewNamesNew;
          allViewIdsNew=jsonVar.allViewIdsNew;
       
             }
             
             
                });  
             var From="";
                if(parent.document.getElementById("Designer") != null)
                From=parent.document.getElementById("Designer").value;
          var reportId = parent.document.getElementById("REPORTID").value;
            var allViewNamesNew1=allViewNamesNew[0];
            var allViewIdsNew1=allViewIdsNew[0];
//            alert(allViewNamesNew1+"------------------------allViewNamesNew1   --------------"  +allViewIdsNew1 + "-----------------------allViewIdsNew1-------");
            var colViewByArray="";
            var colViewNamesArr="";
             var ctxpath='<%=request.getContextPath()%>'
             if(From=="fromDesigner")
                 {
                     $.ajax({
                        async:false,
                           url: "reportTemplateAction.do?templateParam=designerViewbys&RowViewByArray="+allViewIdsNew1+"&ColViewByArray="+colViewByArray+"&ChangeView=ChangeViewBy&reportId="+reportId+"&ctxpath="+ctxpath+"&rowViewNamesArr="+allViewNamesNew1+"&colViewNamesArr="+colViewNamesArr, 
                          success: function(data){
//                   $.post(ctxpath+"/reportTemplateAction.do?templateParam=designerViewbys&RowViewByArray="+allViewIdsNew1+"&ColViewByArray="+colViewByArray+"&ChangeView=ChangeViewBy&reportId="+reportId+"&ctxpath="+ctxpath+"&rowViewNamesArr="+allViewNamesNew1+"&colViewNamesArr="+colViewNamesArr, 
//                             function(data){//
                          
                    var prevREPIds=parent.document.getElementById("REPIds").value
//                    alert(prevREPIds)
                    var frameObj=parent.document.getElementById("dataDispmem");
                    var prevCEPIds=parent.document.getElementById("CEPIds").value
                    var roleid=parent.document.getElementById("roleid").value
                    var RepIdsArray = new Array()
                    var CepIdsArray = new Array()
                    RepIdsArray = prevREPIds.split(",")
                    CepIdsArray = prevCEPIds.split(",")
//                    alert("rowViewByArray::@@@"+rowViewByArray)
                    parent.document.getElementById("REPIds").value = allViewIdsNew1;
                    parent.document.getElementById("CEPIds").value = colViewByArray;
//                    alert(allViewIdsNew1+"allViewIdsNew1");
//                    alert(colViewByArray+"colViewByArray");
                    var flag = 0;
                    for(var i=0;i<allViewIdsNew1.length;i++){
                        for(var j=0;j<colViewByArray.length;j++){
                            if(RepIdsArray[i] == CepIdsArray[j]){
                                flag=flag+1;
                            }
                        }
                    }
//                    alert(rowViewByArray+"=======rowViewByArray")
                    if(allViewIdsNew1!=""){
                        if(flag==0){
                            //alert("mohit");
//                            parent.$("#measuresDialog").dialog('open');
                            var source="reportTemplateAction.do?templateParam=getMeasures&foldersIds="+roleid+'&REPORTID='+parent.document.getElementById("REPORTID").value;
                            //frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
                            frameObj.src=source;
                        }else{
                            alert("Please select different Row Edge and Col Edge")
                        }

                    }
                    else{
                        if(allViewIdsNew1=="" || prevREPIds==undefined ){
                            alert("Please select Row Edge 4")
                        }
                    }
                          }
                        });
                 }
 }
 
            function dispTemplateDims(reportId){
             //   alert("called...");
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
//            function setValueToContainerInDesigner(ctxpath,repId,bizRoles,tabLst){
//                //    alert("Ctx:"+ctxpath+"RepID:"+repId+"role:"+bizRoles);
//                var frameObj=document.getElementById("dataDispmem");
//                $.post(ctxpath+"/reportViewer.do?reportBy=setTableListToContainer&repId="+repId+'&tabLst='+tabLst,
//                function(data){
//                    var source="reportTemplateAction.do?templateParam=getMeasures1&foldersIds="+bizRoles+'&REPORTID='+repId+'&tableList=true';
//                    //                    alert(source);
//                    frameObj.src=source;
//                });
//            }




                        function setValueToContainerInDesigner(ctxpath,repId,bizRoles,tabLst){
                                    //alert("Ctx:"+ctxpath+"RepID..:"+repId+"role....:"+bizRoles);
                            $("#loading").show();
                           // var frameObj=document.getElementById("dataDispmem");
            $.post(ctxpath+"/reportViewer.do?reportBy=setTableListToContainer&repId="+repId+'&tabLst='+tabLst,
                function(data){

                     $.post(ctxpath+"/reportTemplateAction.do?templateParam=getMeasures2&foldersIds="+bizRoles+'&REPORTID='+repId+'&tableList=true',
                       function(data1){

                      // alert("hhhhhhhh"+data1)
                     $("#measures").html(data1);
                     $("#measureTree").treeview({
                    animated: "normal",
                    unique:true
                });


                   //    $(function() {
                var dragMeasure=$('#measures > li > ul > li > ul > li > span,#measures > li > ul > li >  span')
                var dropMeasures=$('#draggableMeasures');

                $(dragMeasure).draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });

                   $('ul#measureTree li').quicksearch({
                    position: 'before',
                    attached: 'ul#measureTree',
                    loaderText: '',
                    delay: 100
                });
                $("#loading").hide();
                });
        });
   }
            function showListInDesigner(ctxpath,paramslist){

                   //  alert("Params : "+paramslist+"ctx:"+ctxPath);
                       var tablist='';
                $.post(ctxpath+"/reportViewer.do?reportBy=getTableList&paramslist="+paramslist+'&currentBizRoles='+<%=roleId%>,
                function(data){
                    //                    alert(data);
                    var jsonVar=eval('('+data+')')
                    var json1 = jsonVar.idsList.split(",");
                    var jsonname = jsonVar.namesList.split(",");
                    
                    for(var i=0;i<json1.length;i++)
                    {
                        tablist=tablist+json1[i];
                        if(i!=json1.length-1)
                        {
                            tablist+=",";
                        }
                    }
                    
                   

                });

                 setValueToContainerInDesigner(ctxpath,<%=reportId%>,<%=roleId%>,tablist);
            }




            $(document).ready(function() {

              showListInDesigner('<%=ctxPath%>','');
             
//                $(function () {
//                    $("input[type='checkbox']").change(function () {
//                        //alert('@');
//                        $(this).siblings('ul')
//                        .find("input[type='checkbox']")
//                        .attr('checked', this.checked);
//                        $('#dragDiv3').append("<ul id='newList'></ul>");
//                        $('#newList').empty();
//                        var list;
//                        $('.childNodes').each(function( index ) {
//                            var current=$(this);
//                            var list_id=current
//                            .find('input[type=\'checkbox\']:checked').map(function() {
//                                return $(this).attr('id');
//                            }).get();
//                            var list_name=current
//                            .find('input[type=\'checkbox\']:checked').map(function() {
//                                return $(this).val();
//                            }).get();
//                            var list=current
//                            .find('input[type=\'checkbox\']:checked').map(function() {
//                                return $(this).parent().text();
//                            }).get();
//                            for (cnt = 0; cnt < list.length; cnt++) {
//                                $("#newList").append("<li id='"+list[cnt]+"^"+list_id[cnt]+"'>"+list[cnt]+"</li>");
//                            }
//                        });
//                       // alert("sgdh========")
//                        var someList=$(this).siblings('ul')
//                        .find('input[type=\'checkbox\']:checked').map(function() {
//                            return $(this).parent().text();
//                        }).get();
                //
//                        //var someList = $('input[type=\'checkbox\']:checked')
//                        //alert(checkValues);
//
//                        for (cnt = 0; cnt < someList.length; cnt++) {
//                            //                            $("#newList").append("<li>"+someList[cnt]+"</li>");
//                        }
//
//                        if(document.getElementById("dragDiv").style.display=="none")
//                        {
//                            document.getElementById("dragDiv").style.display="block";
//                        }
//                    });
//                });


                        
//                     $('ul#measureTree li').quicksearch({
//                    position: 'before',
//                    attached: 'ul#measureTree',
//                    loaderText: '',
//                    delay: 100
//                })
                  $(".formulaViewMenu").contextMenu({
                    menu: 'formulaViewListMenu',
                    leftButton: true },
                function(action, el, pos) {
                    contextMenuWorkFormulaView1(action, el, pos);
            });
                $("#formulaViewDiv1").dialog({
                    //bgiframe: true,
                    autoOpen: false,
                    height: 150,
                    width: 250,
                    position: 'absolute',
                    modal: true
                });

                

                       $(function() {
                //var dragMeasure=$('#measures > li > ul > li > ul > li > span,#measures > li > ul > li >  span')
                var dropMeasures=$('#draggableMeasures');

//                $(dragMeasure).draggable({
//                    helper:"clone",
//                    effect:["", "fade"]
//                });

                $(dropMeasures).droppable(
                {
                    activeClass:"blueBorder",
                    accept:'#measures > li > ul > li > ul > li > span,#measures > li > ul > li >  span',
                    drop: function(ev, ui) {
                        var d = document.getElementById("sortable1").childNodes;
                 if(d.length==1){
                   document.getElementById('dropimage1').style.display='none';
                                      document.getElementById("dropmeasures").style.backgroundColor='#F2F2F2';
}
                        var measure=ui.draggable.html();
                        createMeasures1(measure,ui.draggable.attr('id'));
                    }
                });

        
            });

         $(function() {
                $("#sortable1").sortable();
                $("#sortable1").disableSelection();
            });
      




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
               // alert("kkkk")
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
   //Added By Ram

      var c = document.getElementById("sortable").childNodes;
       if(c.length==1){
          document.getElementById('dropimage').style.display='none';
          document.getElementById("demo").style.backgroundColor='#F2F2F2';
}

                        var kpi=ui.draggable.html();
                        createDims1(ui.draggable.html(),ui.draggable.attr('id')); //Modified by Ram 06Aug15
                    }
                });
            });

            $(function() {
                $("#sortable").sortable();
                $("#sortable").disableSelection();
            });
            function contextMenuWorkFormulaView(action, el, pos){

                document.getElementById("value").innerHTML=$(el).attr('title');
                $("#formulaViewDiv").dialog('open');


            }

                  function prevMeasures(){
                var prevMsrs=parent.document.getElementById("Measures").value;
                if(prevMsrs.length!=0){
                    prevMsrs=prevMsrs.split(",");
                    for(var m=0;m<prevMsrs.length;m++){
                        var msrElmnts=prevMsrs[m].split("-");
                        createMeasures1(msrElmnts[0],"elmnt-"+msrElmnts[1]);
                    }
                }
            }
                 function contextMenuWorkFormulaView1(action, el, pos){

                document.getElementById("value1").innerHTML=$(el).attr('title');
                $("#formulaViewDiv1").dialog('open');
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

             function dispMeasures2(){
                //                alert("@@@:disp measures");
                var msrs="";
                var msrUl=document.getElementById("sortable1");
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
            function createreport2()
            {
            var dimUl=document.getElementById("sortable");
                  var msrUl=document.getElementById("sortable1");
                if(dimUl.getElementsByTagName("li").length == 0 || msrUl.getElementsByTagName("li").length == 0){
                    alert("Please Drag Dimension and Measure !!");
                    return;
                }
                else{                          
                // alert("dimIds..."+dimIds+"msrIds..."+msrIds)
               $("#loading1").show();        //   alert("@@@:create report");
//                                alert("@@@:create report");
                saveDims1(<%=reportId%>, "true");
                // alert("@@@@:"+window.parent.document.getElementById('REPORTNAMEbeforeSave').value);
                setTimeout(function()
                {
                    saveMeasures1();
                    window.parent.document.getElementById('submitReportForm').style.display='block';


                }, 1200);
                window.parent.document.getElementById('desginerDiv11').style.display='none';
            }
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
            <%  if (collect.timeDetailsArray != null && timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE") && collect.timeDetailsMap != null && !collect.timeDetailsMap.isEmpty()) {%>

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

            <% } else if (collect.timeDetailsArray != null && timeinfo.get(1).equalsIgnoreCase("PRG_STD") && collect.timeDetailsMap != null && !collect.timeDetailsMap.isEmpty()) {%>

                           var html="";
                           html+=parent.$("#dateregionDiv").html();
                           // alert("hhhhhhhhh"+html)
            <% if (sytm.equalsIgnoreCase("No")) {%>   //for year vs year by mohit
                            parent.$("#pfield1").html("<%=(container.fullName0).substring(0, 4) + (container.fullName0).substring(6)%>");
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
            <%} else {

                 //System.out.print("?>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+container.fullName0);%>
            <% String calndrYear = (container.fullName0).substring(4);
                         String s = "";
                         for (int year = 2005; year <= 2020; year++) {
                             if (calndrYear.equalsIgnoreCase(Integer.toString(year))) {

                                 s += "<option selected value=" + year + ">" + year + "</option>";
                             } else {
                                 s += "<option value=" + year + ">" + year + "</option>";
                             }
                         }%>

                                     //parent.$("#pfield1").html("<%=(container.fullName0).substring(4)%>");
                                     parent.$("#calPeriodYear").append("<%=s%>");
                                     //alert("<%=s%>")
                                     //parent.$('#calPeriodYear option:selected').val(<%=(container.fullName0).substring(4)%>)
            <%}%>

            <%
                 DisplayParameters dur = new DisplayParameters();
                 String duration1 = String.valueOf(dur.displayTime(collect.timeDetailsMap));
            %>
                       parent.$("#timebasistd").html('<%=duration1%>');
                       parent.$("#dateregionDiv").show();
                       parent.document.getElementById("saveadhoc").style.display='none';
            <% }
                              if (collect.timeDetailsArray != null) {
                                  container.evaluateReportDateHeaders();
                              }
            %>
                          }
                      }


                      function saveMeasures1(){
                //                alert("####:saveMeasures");
                if(From != null && From=="busTemplateView"){
                    dispBusTemplateMesures1();
                }else{
                    //                 parent.cancelTabMeasure();
                    dispMeasures2();
                }
                if(From != null && From=="fromInsightDesigner" && From=="busTemplateView"){
                    //                if(From != null && From=="fromInsightDesigner"){
                }else{
            <%  if (collect.timeDetailsArray != null && timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE") && collect.timeDetailsMap != null && !collect.timeDetailsMap.isEmpty()) {%>

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

            <% } else if (collect.timeDetailsArray != null && timeinfo.get(1).equalsIgnoreCase("PRG_STD") && collect.timeDetailsMap != null && !collect.timeDetailsMap.isEmpty()) {%>

                           var html="";
                           html+=parent.$("#dateregionDiv").html();
                           // alert("hhhhhhhhh"+html)
            <% if (sytm.equalsIgnoreCase("No")) {%>   //for year vs year by mohit
                            parent.$("#pfield1").html("<%=(container.fullName0).substring(0, 4) + (container.fullName0).substring(6)%>");
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
            <%} else {

                 //System.out.print("?mohit>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+container.fullName0);%>
            <% String calndrYear = (container.fullName0).substring(4);
                         String s = "";
                         for (int year = 2005; year <= 2020; year++) {
                             if (calndrYear.equalsIgnoreCase(Integer.toString(year))) {

                                 s += "<option selected value=" + year + ">" + year + "</option>";
                             } else {
                                 s += "<option value=" + year + ">" + year + "</option>";
                             }
                         }%>

                                     //parent.$("#pfield1").html("<%=(container.fullName0).substring(4)%>");
                                     parent.$("#calPeriodYear").append("<%=s%>");
                                     //alert("<%=s%>")
                                     //parent.$('#calPeriodYear option:selected').val(<%=(container.fullName0).substring(4)%>)
            <%}%>

            <%
                 DisplayParameters dur = new DisplayParameters();
                 String duration1 = String.valueOf(dur.displayTime(collect.timeDetailsMap));
            %>
                       parent.$("#timebasistd").html('<%=duration1%>');
                       parent.$("#dateregionDiv").show();
                       parent.document.getElementById("saveadhoc").style.display='none';
            <% }
                              if (collect.timeDetailsArray != null) {
                                  container.evaluateReportDateHeaders();
                              }
            %>
                          }
                      }


                      function dispBusTemplateMesures1(){
                var msrs="";
                var msrUl=document.getElementById("sortable1");
                var msrIds=msrUl.getElementsByTagName("li");
                for(var  i=0;i<msrIds.length;i++){
                    var measureIds=(msrIds[i].id).replace("Msr","");
                    msrs=msrs+","+measureIds;
                }
                var action=null;
//               if(From != null && From=="busTemplateView")
                action=parent.document.getElementById("action1").value;
               var oneviewType=parent.document.getElementById("oneviewType").value;
               var msrSize=10;
//               if(oneviewType=="Business TemplateView")
//                   msrSize=10;
               if(msrIds.length!=0 && msrIds.length>msrSize){
                   alert("please select only "+msrSize+" measure")
               }else if(msrIds.length!=0){
                    parent.cancelTabMeasure();
                    msrs=msrs.substring(1);
                    parent.document.getElementById("MsrIds").value=msrs;
                    parent.document.getElementById("Measures").value=msrs;

                    if(action!=null && action=="measChange"){
                    $.ajax({
                            url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=tableMeasureChanges&tableMsrs='+msrs+'&REPORTID=<%=reportId%>',
                            success: function(data){
                                 if(oneviewType=="Business TemplateView")
                                    parent.PreviewTable();
                                 else{
                                     parent.PreviewMsrTemplateTable();
                                 }
                            }
                            });
                    }else{
                    $.ajax({
                        url: 'reportTemplateAction.do?templateParam=buildTable&buildTableChange=Measures&Msrs='+msrs+'&MsrsNames='+msrs+'&REPORTID=<%=reportId%>',
                        success: function(data) {
//                            alert("Measures")
                             if(oneviewType=="Business TemplateView")
                                    parent.PreviewTable();
                                 else{
                                     parent.PreviewMsrTemplateTable();
                                 }

                            if(data!=""){
                            }
                        }
                    });
                }
                    parent.cancelTabMeasure();

                }
                else{
                    alert("Please Select Measures")
                }
            }


        </script>

        <style type="text/css">


      .loading_image{
                display: none;
                position: absolute;
                top: 12%;
                left: 76%;
                width:24%;
                height: 72%;
                background-color: white;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                filter:alpha(opacity=50);
                z-index:1001;

            }
            .loading_image1{
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width:100%;
                height: 100%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                filter:alpha(opacity=50);
                z-index:1001;

            }

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

       #dragDiv5 {
/* //background: -webkit-linear-gradient(white,#0078AE)*/
    background: -webkit-linear-gradient(white,#D8D8D8  ); /* For Safari 5.1 to 6.0 */
    background: -o-linear-gradient(white, #D8D8D8 ); /* For Opera 11.1 to 12.0 */
    background: -moz-linear-gradient(white, #D8D8D8 ); /* For Firefox 3.6 to 15 */
    background: linear-gradient(white, #D8D8D8 ); /* Standard syntax (must be last) */
}

     #dragDiv6 {

    background: -webkit-linear-gradient(#D8D8D8 ,white);
    background: -o-linear-gradient(#D8D8D8 ,white);
    background: -moz-linear-gradient(#D8D8D8 ,white);
    background: linear-gradient(#D8D8D8 ,white);
}
        </style>
    </head>
    <body>
        <form name="myForm2" method="post">
            <table width="100%" style="height: 500px; margin-top: 1px;border-width: 2px; border-style: groove; z-index: 10000;border-color: skyblue;  ">
                 <tr style="height: 87%;z-index: 10000;">
                    <td>

                        <div style="height: 100%;border-width: 1px; border-style: solid;z-index: 10000; border-color: grey;  ">
                            <div style="height: 100%;width: 100%;border-width: 0px;  border-style: none; border-color: white; border-collapse: collapse; ">
                                <table style="width:100%;height:85%;margin-left:  0px;margin-top: 5px;margin-bottom: 5px; margin-right: 5px;"  cellspacing="15" >
                                    <tr>
                                        <td width="25%" valign="top" class="draggedTable1" style="border: 1px solid gray;">
                                            <div style="height:20px" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all ui-sortable"><font size="2" style="font-weight:bold;">Drag Dimension from below</font></div>
                                            <div class="masterDiv" style="height:440px;overflow-y:auto">
                                                <ul id="DimTree" class="filetree treeview-famfamfam">
                                                    <li class="open" style="background-image:url('images/treeViewImages/plus.gif')">
                                                        <ul id="kpis">


                                                            <%=DimData%>
                                                        </ul>
                                                    </li>
                                                </ul>
                                            </div>
                                        </td>
                                          
                                                <td  width="40%" valign="top"  id="draggableKpis1" valign="top">

                                                   <div id="dragDiv6" style="height:60px;width:650px;overflow-y:auto;border: 1px solid gray;margin-bottom: 1px;">
                                                     <table><tr>
                                                      <td>
                                                         <font size=88>Report Creation Time</font>
                                                      </td>
                                                           
                                                      <td style=" ">
                                                       <select style="width:123px;height:30px;" id="time" name="time">
                                                       <option id="StandardTime" value="StandardTime11" onclick="savestandredtime()" name="time">Standard Time</option>
                                                       </select>
                                                       </td>
                                                       </tr></table>
                                                   </div>
                                            <div style="width:49%; float: left" valign="top"  id="draggableKpis" valign="top" >
                                                   <div  id="demo"  style="height:350px;overflow-y:auto;border: 1px solid gray; margin-bottom: 5px;margin-top:2px;">
                                                                                                        
                                                    <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all ui-sortable">
                                                        <font size="2" style="font-weight:bold">Drop Dimension Here </font>
                                                    </h3>
                                                      <img id="dropimage" src="images/dropdimensions_1.png" alt="Drop" style="width:304px;height:228px;">

                                                    <ul id="sortable">
                                                      
                                                    </ul>
                                                   
                                                </div>
<!--                                                <div id="dragDiv5" style="height:60px;width:650px;overflow-y:auto;border: 1px solid black;margin-bottom: 5px">
                                                 <table width="100%" >
                                                    <tr> 
                                                        <td>   Default Report View</td>
                                                   
                                                  <td>
                                                    <tr> <input type="radio" name="ontime" value="time" >Default report on time</tr>
                                                   <tr>  <input  type="radio" name="ontime" value="parameter" >First parameter select</tr>
                                                    </td>
                                                    </tr>
                                                 </table> </div>-->

                                                   <div id="dragDiv5" style="height:60px;width:650px;overflow-y:auto;border: 1px solid gray;margin-bottom: 5px;">
                                                    <div style="margin-left:90px;margin-top:25px;width:200px;float: left;z-index:10000 " >
                                                        Default Report View
                                                    </div>
                                                    <div style="margin-left:300px;margin-top:10px;width:350px;z-index:10000  ">
                                                    <input style="margin-left: 5px" type="radio" name="ontime" value="parameter" checked >First Parameter Selected<br>
                                               <!--         <input type="radio" name="ontime" value="time"  >Default Report on Time-->
                                                    
                                                    </div>
                                                </div>


                                              
                                            </div>
                                            <div style="width:49%; float: right" valign="top"  id="draggableMeasures" valign="top">
                                                <div id="dropmeasures" style="height:350px;overflow-y:auto;border: 1px solid gray;margin-top:3px;">
                                                    <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all ui-sortable">
                                                        <font size="2" style="font-weight:bold">Drop Measures Here</font>
                                                    </h3>
                                                    <img id="dropimage1" src="images/dropmeasures_1.png" alt="Drop" style="width:304px;height:228px;">
                                                    <ul id="sortable1">
                                                       
                                                    </ul>
                                                </div>


                                            </div>
                                        </td>
                                         <td width="25%" valign="top" class="draggedTable1" style="border: 1px solid gray;">
                                            <div style="height:20px" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all ui-sortable"><font size="2" style="font-weight:bold;">Drag Measures from below</font></div>
                                             <div id="loading" class="loading_image">
                                             <img id="imgId" src="images/help-loading.gif"  border="0px" style="position:absolute;">
                                             </div>
                                             <div id="loading1" class="loading_image1">
                                             <center><img id="imgId1" src="images/help-loading.gif" align="middle" border="0px" style="position:absolute;"></center>
                                             </div>
                                            <div class="masterDiv" style="height:440px;overflow-y:auto">
                                                <ul id="measureTree" class="filetree treeview-famfamfam" style="display: none;">
                                                    <li class="open" style="background-image:url('images/treeViewImages/plus.gif')">
                                                        <ul id="measures" >

                                                        </ul>
                                                    </li>
                                                </ul>
                                            </div>
                                        </td>
<!--                                        <td width="25%" valign="top"  id="draggableKpis" valign="top"style="border: 1px solid black; ">
                                            <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                                <font size="2" style="font-weight:bold">Drag Measures from below </font>
                                            </h3>
                                            <div id="dragDiv" style="height:440px;display: none;">
                                                <iframe id="dataDispmem" NAME='dataDispmem' frameborder="0" width="100%" height="100%" SRC='#'></iframe>

                                            </div>
                                        </td>-->
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </td>
                </tr>
            </table>
            <input type="button"  class="prgBtn" onclick="createreport2()" style="margin-top: 10px;width:auto; position: fixed;left: 620px;" value="Done" >
        </form>
    </body>
</html>
