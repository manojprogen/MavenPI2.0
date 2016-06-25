<%--
    Document   : pbParamFilterMembers
    Created on : 23 Jul, 2010, 5:28:02 PM
    Author     : mahesh
--%>
<%@page import="com.progen.i18n.TranslaterHelper"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashMap"%>
<%@page import="org.apache.poi.util.TempFile"%>
<%@page import="prg.db.PbReturnObject"%>
<%@page import="prg.db.PbDb"%>
<%@page import="utils.db.ProgenConnection"%>
<%@page import="java.sql.*" %>
<%@page import="java.util.ArrayList" %>
<%@page import="prg.business.group.BusinessGroupDAO" %>
<%@page import="com.progen.report.whatIf.WhatIfScenario" %>
<%@page  import="prg.db.Container"%>
<%@page import="com.progen.report.whatIf.WhatIfSensitivity"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%
            String reportId = request.getParameter("reportId");
            String paramFilterName = request.getParameter("paramFilterName");
            String selectedParam = request.getParameter("selectedParam");
            String disColumnName = request.getParameter("disColumnName");
            String measEleId = request.getParameter("measElement");
            String dimType=request.getParameter("dimType");
            String staticDimVal=null;
            staticDimVal=request.getParameter("staticDimVal");
            String openSenSitivity = "";

            String from = "";
            String measureId = "";
            String measName = "";
            String html = "";
            String peviousDims = "";
            String themeColor = "blue";
            String factName=request.getParameter("factName");
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }
            if (request.getParameter("openSenSitivity") != null) {
                openSenSitivity = request.getParameter("openSenSitivity");

            }
            if (request.getParameter("from") != null) {
                from = request.getParameter("from");
                measureId = request.getParameter("measureId");
                measName = request.getParameter("measureName");
                peviousDims = request.getParameter("peviousDims");

            }

            String cpath = request.getContextPath();
            PbDb pbdb = new PbDb();
            PbReturnObject vewdetspbro = null;
            PbReturnObject paramMbrsObj = null;
            String connectionId=null;
            String dimBussTabId=null;
            String dimElementId=null;
            String busstabName=null,memName=null,usercolName=null;
            String type=null;
            type=request.getParameter("type");
            String Id=request.getParameter("Id");
           if(type==null && type!="facts"){
               String vewdetssql = "SELECT GRP_ID, FOLDER_ID, FOLDER_NAME, SUB_FOLDER_ID, SUB_FOLDER_NAME, SUB_FOLDER_TYPE, SUB_FOLDER_TAB_ID, IS_DIMENSION, IS_FACT, IS_BUCKET, DISP_NAME,"
                    + " QRY_DIM_ID, DIM_ID, DIM_TAB_ID, DIM_NAME, ELEMENT_ID, BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME,"
                    + " USER_COL_NAME, USER_COL_DESC, USER_COL_TYPE, REF_ELEMENT_ID, REF_ELEMENT_TYPE, MEMBER_ID, MEMBER_NAME"
                    + ", DENOM_QUERY, BUSS_TABLE_NAME, CONNECTION_ID, AGGREGATION_TYPE, ACTUAL_COL_FORMULA, USE_REPORT_FLAG, "
                    + "REFFERED_ELEMENTS,DISPLAY_FORMULA,TABLE_DISP_NAME,TABLE_TOOLTIP_NAME FROM PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID=" + selectedParam;
            vewdetspbro = pbdb.execSelectSQL(vewdetssql);
            connectionId = String.valueOf(vewdetspbro.getFieldValueInt(0, "CONNECTION_ID"));
            dimBussTabId = String.valueOf(vewdetspbro.getFieldValueInt(0, "BUSS_TABLE_ID"));
            dimElementId = String.valueOf(vewdetspbro.getFieldValueInt(0, "ELEMENT_ID"));
            busstabName = String.valueOf(vewdetspbro.getFieldValueString(0, "BUSS_TABLE_NAME"));
            memName = String.valueOf(vewdetspbro.getFieldValueString(0, "BUSS_COL_NAME"));
            usercolName = String.valueOf(vewdetspbro.getFieldValueString(0, "USER_COL_DESC"));
            Container container = null;
            WhatIfScenario whatIfScenario = null;
            WhatIfSensitivity whatIfSensitivity = null;
            try {
                container = Container.getContainerFromSession(request, reportId);
                if (container != null) {
                    whatIfScenario = container.getWhatIfScenario();
                    if (whatIfScenario != null) {
                        whatIfSensitivity = whatIfScenario.getWhatIfSensitivity();

                    }
                }

            } catch (Exception e) {
            }
            }
            //added by Mohit Gupta for default locale
                   Locale cL=null;
                   cL=(Locale)session.getAttribute("UserLocaleFormat");
                //ended By Mohit Gupta
           // 
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><bean:message key="ProGen.Title"/></title>
        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>-->
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
         <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.8.23-custom.min.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/jquery-1.8.23-custom.min.css" rel="stylesheet" />

        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/quicksearch.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <script type="text/javascript" src="<%= request.getContextPath()%>//dragAndDropTable.js"></script>
        <style type="text/css">

            * {
                font-family: verdana;
                font-size: 11px;
                font-size-adjust: none;
                font-stretch: normal;
                font-style: normal;
                font-variant: normal;
                font-weight: normal;
                line-height: normal;
            }
        </style>
        <script type="text/javascript">

            var assinIdAndVales=new Array
            var isMemberUseInOtherLevel="false"
            
            var scHeight=0;
            var scTop=0;
            var clHgt=0;
            var startVal=1;
            var endVal=0;
            var scrollFlag=true;
            var scrollPayLoad="";
            var scrollPayLoad1="";
            var onScrollFlag=0;
            var scrollURL="";
            var mbrs1="";
            var searchText = "";
            
            $(document).ready(function() {
                $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getDimentionMembers&elementId="+<%=selectedParam%>+"&type="+'<%=type%>'+"&factName="+'<%=factName%>'+"&startVal="+startVal+"&mbrs="+mbrs1+"&searchText="+searchText,function(data){
//                    alert(data.toString())
                    var jsonVar=eval('('+data+')')
                    $("#paramFilterMbrsForm").html("")
                    $("#paramFilterMbrsForm").html(jsonVar.htmlStr)


                    isMemberUseInOtherLevel=jsonVar.isMemberUseInOtherLevel
                    $("#myList3").treeview({
                        animated:"slow",
                        persist: "cookie"
                    });

//                    $('ul#myList3 li').quicksearch({
//                        position: 'before',
//                        attached: 'ul#myList3',
//                        loaderText: '',
//                        delay: 100
//                    })
                    $(".myDragTabs").draggable({
                        helper:"clone",
                        effect:["", "fade"]
                    });
                    $("#dropTabs").droppable({
                        activeClass:"blueBorder",
                        accept:'.myDragTabs',
                        drop: function(ev, ui) {
                            createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                        }
                    }
                );
                     grpColArray=jsonVar.memberValues
                    $(".sortable").sortable();
//                    grpColArray=new Array
//                    $("#myList3 li span").each(function(){
//                        if($(this).html()!="")
//                            grpColArray.push($(this).html())
//                    })
                               if("openSenSitivity"=='<%=openSenSitivity%>')
                {                        if(parent.sensitivityname!=null){
                                      grpColArray=new Array

                             var factorsArray=parent.sensitivityname.split(",")
                             for(var i=0;i<factorsArray.length;i++){
                                 if(factorsArray[i]!=""||factorsArray[i]!=" ")
                                 createColumn(factorsArray[i],factorsArray[i],"sortable")
                                 removeColumn(factorsArray[i])

                             }
                               $("#sensitivityFactor").val(parent.sensitivityVar)

                }  }

                var from='<%=from%>';
                var privHtml='<%=peviousDims%>';
                // alert(privHtml)
                var dims=privHtml.split("||");
                if(from=="scorecard")
                {
                    document.getElementById("saveElement").style.display ='none'
                    document.getElementById("assign").style.display ='none'
                    document.getElementById("saveSensTy").style.display ='none'
                    document.getElementById("sesnsiTable").style.display ='none'
                    document.getElementById("scoreSaveTable").style.display =''
//                    isMemberUseInOtherLevel="false"
                    for(var i=0;i<dims.length;i++){
                        if(dims[i]!="") {
                            createColumn(dims[i],dims[i],"sortable");
                        }
                    }
                }

                });




            });

            $(function() {

                $(".myDragTabs").draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });

                $("#dropTabs").droppable({
                    activeClass:"blueBorder",
                    accept:'.myDragTabs',
                    drop: function(ev, ui) {
                        createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                    }
                }
            );

                $("#sortable1").droppable({
                    activeClass:"blueBorder",
                    accept:'.myDragTabs',
                    drop: function(ev, ui) {
                        createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable1");
                    }
                }
            );

                $("#sortable2").droppable({
                    activeClass:"blueBorder",
                    accept:'.myDragTabs',
                    drop: function(ev, ui) {
                        createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable2");
                    }
                }
            );
            });


           function removeColumn(id){
           $("#"+id).closest('li').remove()
           }

            function whatifMesdelete(index){


                if("openSenSitivity"=='<%=openSenSitivity%>')
                {
                    $.ajax({
                        url:'<%=cpath%>/whatIfScenerioAction.do?whatIfParam=removeSenSitivity&repID=<%=reportId%>&keyValue='+index,
                        success:function(data){
                            var livar="<li><img alt=''  src='<%=cpath%>/icons pinvoke/report.png'/><span class='myDragTabs  ui-draggable' id='"+index+"'>"+index+"</span></li>"
                            $("#myList3").append(livar)
                            $(".myDragTabs").draggable({
                                helper:"clone",
                                effect:["", "fade"]
                            });
                        }
                    });
                }
            }

            function goSave(reportId,paramFilterName,dimBussTabId,dimElementId, measEleId,dimTabColName,dimType){
                var ViewFrom="<%=session.getAttribute("ViewFrom")%>";
                var mbrIds="";
                var mbrs="";
                var ulObj=document.getElementById("sortable");
                var liObj=ulObj.getElementsByTagName("li");
                var mbrVal=""
                //alert('liObj length is : '+liObj.length)
                for(var i=0;i<liObj.length;i++){
                    mbrIds=(liObj[i].id).split("~");
                    mbrs=mbrs+","+mbrIds[0].replace("_li", "", "gi").replace(",","'''");
                }
                
                if(mbrs!="" && mbrIds.length!=0){
                    mbrVal=mbrs.substr(1);
                    mbrs=mbrs.substr(1);
                }

                var mbrsArr = mbrs.split(",");
                mbrs = "";
                for ( var i=0; i<mbrsArr.length; i++ )
                {
                    mbrsArr[i] = "''" + mbrsArr[i] + "''";
                    if ( i == mbrsArr.length - 1 )
                        mbrs = mbrs+mbrsArr[i];
                    else
                        mbrs = mbrs+mbrsArr[i]+",";
                }
                var Type=null;
                Type='<%=type%>'
                var staticDimValue=null;
                staticDimValue='<%=staticDimVal%>'
              if(Type=="null" && Type!="facts"){
                $.ajax({
                    url: encodeURI('<%=request.getContextPath()%>/reportViewer.do?reportBy=getParamFilterMbrs&mbrs='+encodeURIComponent(mbrs)+'&paramFilterName='+paramFilterName+'&dimBussTabId='+dimBussTabId+'&reportId='+reportId+'&measEleId='+measEleId+'&dimTabColName='+dimTabColName+'&dimElementId='+dimElementId+'&dimType='+dimType+'&staticDimValue='+staticDimValue),
                    success: function(data){
                        parent.$("#paramFilterMemberDiv").dialog('close');
                        $.ajax({
                            url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=tableMeasureChanges&tableMsrs='+data+'&REPORTID='+reportId,
                            success: function(data){
                                if(ViewFrom=="Designer")
                                    parent.dispTable("measChange");
                                else
                                    parent.submitFormMeasChange();
                            }
                        });
                    }
                });
            }else{
                var id='<%=Id%>'
                parent.$("#paramFilterMemberDiv").dialog('close');
                parent.$("#"+id).val(mbrVal);
            }

            }

            function onloadChanges()
            {
                var cheElement='<%=openSenSitivity%>'
                if(cheElement != "")
                {
                    document.getElementById("saveElement").style.display ='none'
                    document.getElementById("assign").style.display =''
                    document.getElementById("saveSensTy").style.display =''
                    document.getElementById("sesnsiTable").style.display =''
                    if(parent.sensitivityname !="" &&  "openSenSitivity"=='<%=openSenSitivity%>')
                    {
                        var tempmesVar=parent.sensitivityname.toString();
                        //alert("tempmesVar\t"+tempmesVar)
                        var mesVar=new Array
                        mesVar=tempmesVar.split(",")
                        for(var k=0;k<mesVar.length;k++)
                        {
                            if(parent.trim(mesVar[k])!="")
                                createColumn(mesVar[k],mesVar[k],'sortable')
                        }
                        $("#sensitivityFactor").val(parent.sensitivityVar)
                    }
                }





                //

            }
            function isNumberevent(evt)
            {

                var charCode = (evt.which) ? evt.which : evt.keyCode               
                if(charCode==46 || charCode==44 || charCode==45)
                    return true;
                if (charCode > 31 && (charCode < 48 || charCode > 57))
                    return false;

                return true;
            }
            function checkNumber(){
                var numVal =$("#sensitivityFactor").val()
                var booleanvar=false
                if(numVal<=1)
                    booleanvar= true;
                else
                    booleanvar= false;
                if(!booleanvar){

                    alert("Please enter less then 1")
                    $('#sensitivityFactor').focus();

                }

            }
            function assignSensitivity(){
                if($("#sensitivityFactor").val() != ""){

                    var ulObj=document.getElementById("sortable");
                    var liObj=ulObj.getElementsByTagName("li");
                    if(liObj.length !=0){
                        for(var i=0;i<liObj.length;i++)
                        {
                            var temp =(liObj[i].id).split("~");
                            assinIdAndVales.push($("#sensitivityFactor").val()+"~"+temp[0].replace("_li","","gi"))

                        }
                        document.getElementById("sortable").innerHTML="";
                        $("#sensitivityFactor").val("")
                    }else{
                        alert("please drag measure to Drag Columns ")
                    }

                }else{

                    alert("please enter Value in Sensitivity factor")
                }


            }
            function saveSensitivity(){
                //                    alert("assinIdAndVales\t"+assinIdAndVales.toString())
                if($("#sensitivityFactor").val() != ""){
                    var ulObj=document.getElementById("sortable");
                    var liObj=ulObj.getElementsByTagName("li");
                    if(liObj.length ==0)
                        assignSensitivity()
                    else
                        parent.setSensitivityFactor(assinIdAndVales.toString())

                }
                else
                    parent.setSensitivityFactor(assinIdAndVales.toString())
            }

            function saveDimensionsMembers()
            {
                var mbrIds="";
                var mbrs="";
                var dimHtml=$("#sortable").html();
                var ulObj=document.getElementById("sortable");
                var liObj=ulObj.getElementsByTagName("li");
                //alert('liObj length is : '+liObj.length)
                for(var i=0;i<liObj.length;i++){
                    mbrIds=(liObj[i].id).split("~");
                    mbrs=mbrs+","+mbrIds[0].replace("_li","","gi");
                }
                if(mbrs!="" && mbrIds.length!=0){
                    mbrs=mbrs.substr(1);
                }

                var mbrsArr = mbrs.split(",");
                mbrs = "";
                for ( var i=0; i<mbrsArr.length; i++ )
                {
                    //mbrsArr[i] = "''" + mbrsArr[i] + "''";
                    if ( i == mbrsArr.length - 1 )
                        mbrs = mbrs+mbrsArr[i];
                    else
                        mbrs = mbrs+mbrsArr[i]+",";
                }
                if(mbrsArr.length==0){
                    alert("Please select dimensions")
                }
                else{

                    //                $.ajax({
                    //               url:"/scorecardDesign.do?scorecardParam=saveDimensionDetailsForScard&dimHtml="+dimHtml+"&members="+mbrs+"&measId=<%=measureId%>&measName=<%=measName%>",
                    //                success: function(data){
                    //                }
                    //                });
                    parent.applyRulesForDims(mbrsArr,dimHtml);
                }
                //alert("mbrs--"+mbrs)

            }
            function onScrollDiv1(divObj){            
                var mbrIds="";
                var mbrVal="";
                mbrs1 = "";
                searchText = $("#mainSearch").val();
                var ulObj=document.getElementById("sortable");
                var liObj=ulObj.getElementsByTagName("li");
                for(var i=0;i<liObj.length;i++){
                        mbrIds=(liObj[i].id).split("~");
                        mbrs1=mbrs1+","+mbrIds[0].replace("_li", "", "gi").replace(",","'''");
                    }
                if(mbrs1!="" && mbrIds.length!=0){
                    mbrVal=mbrs1.substr(1);
                    mbrs1=mbrs1.substr(1);
                }
                var mbrsArr = mbrs1.split(",");
                mbrs1 = "";
                for ( var i=0; i<mbrsArr.length; i++ )
                {
                    if ( i == mbrsArr.length - 1 )
                        mbrs1 = mbrs1+mbrsArr[i];
                    else
                        mbrs1 = mbrs1+mbrsArr[i]+",";
                }
                //alert('I am Here in scroll div 1')
                scHeight=(divObj.scrollHeight);
                scTop=(divObj.scrollTop);
                clHgt=(divObj.clientHeight);

                var tempDrop = $("#dropDiv").val();
                if(scrollFlag){
                    if(scTop==(scHeight-clHgt)){

                    startVal=startVal+100;
                    scrollPayLoad1=scrollPayLoad+"&startValue="+startVal;
//                    sendRequest(scrollURL, scrollPayLoad1);
                    $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getDimentionMembers&elementId="+<%=selectedParam%>+"&type="+'<%=type%>'+"&factName="+'<%=factName%>'+"&startVal="+startVal+"&mbrs="+encodeURIComponent(mbrs1)+"&searchText="+searchText,function(data){
//                    alert(data.toString())
                    var jsonVar=eval('('+data+')')
                    $("#paramFilterMbrsForm").html("")
                    $("#paramFilterMbrsForm").html(jsonVar.htmlStr)

                    isMemberUseInOtherLevel=jsonVar.isMemberUseInOtherLevel
                    $("#myList3").treeview({
                        animated:"slow",
                        persist: "cookie"
                    });

//                    $('ul#myList3 li').quicksearch({
//                        position: 'before',
//                        attached: 'ul#myList3',
//                        loaderText: '',
//                        delay: 100
//                    })
                    $(".myDragTabs").draggable({
                        helper:"clone",
                        effect:["", "fade"]
                    });
                    $("#dropTabs").droppable({
                        activeClass:"blueBorder",
                        accept:'.myDragTabs',
                        drop: function(ev, ui) {
                            createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                        }
                    }
                );
                     grpColArray=jsonVar.memberValues
                    $(".sortable").sortable();
//                    grpColArray=new Array
//                    $("#myList3 li span").each(function(){
//                        if($(this).html()!="")
//                            grpColArray.push($(this).html())
//                    })
                               if("openSenSitivity"=='<%=openSenSitivity%>')
                {                        if(parent.sensitivityname!=null){
                                      grpColArray=new Array

                             var factorsArray=parent.sensitivityname.split(",")
                             for(var i=0;i<factorsArray.length;i++){
                                 if(factorsArray[i]!=""||factorsArray[i]!=" ")
                                 createColumn(factorsArray[i],factorsArray[i],"sortable")
                                 removeColumn(factorsArray[i])

                             }
                               $("#sensitivityFactor").val(parent.sensitivityVar)

                }  }

                var from='<%=from%>';
                var privHtml='<%=peviousDims%>';
                // alert(privHtml)
                var dims=privHtml.split("||");
                if(from=="scorecard")
                {
                    document.getElementById("saveElement").style.display ='none'
                    document.getElementById("assign").style.display ='none'
                    document.getElementById("saveSensTy").style.display ='none'
                    document.getElementById("sesnsiTable").style.display ='none'
                    document.getElementById("scoreSaveTable").style.display =''
//                    isMemberUseInOtherLevel="false"
                    for(var i=0;i<dims.length;i++){
                        if(dims[i]!="") {
                            createColumn(dims[i],dims[i],"sortable");
                        }
                    }
                }

                });

                    onScrollFlag=1;
                }
            }
            }
            function goSearch(){            
                 var mbrIds="";
                var mbrVal="";
                mbrs1 = "";
                startVal=0;
                searchText = $("#mainSearch").val();
                var ulObj=document.getElementById("sortable");
                var liObj=ulObj.getElementsByTagName("li");
                for(var i=0;i<liObj.length;i++){
                        mbrIds=(liObj[i].id).split("~");
                        mbrs1=mbrs1+","+mbrIds[0].replace("_li", "", "gi").replace(",","'''");
                    }
                if(mbrs1!="" && mbrIds.length!=0){
                    mbrVal=mbrs1.substr(1);
                    mbrs1=mbrs1.substr(1);
                }
                var mbrsArr = mbrs1.split(",");
                mbrs1 = "";
                for ( var i=0; i<mbrsArr.length; i++ )
                {
                    if ( i == mbrsArr.length - 1 )
                        mbrs1 = mbrs1+mbrsArr[i];
                    else
                        mbrs1 = mbrs1+mbrsArr[i]+",";
                }
                $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getDimentionMembers&elementId="+<%=selectedParam%>+"&type="+'<%=type%>'+"&factName="+'<%=factName%>'+"&startVal="+startVal+"&mbrs="+encodeURIComponent(mbrs1)+"&searchText="+searchText,function(data){
//                    alert(data.toString())
                    var jsonVar=eval('('+data+')')
                    $("#paramFilterMbrsForm").html("")
                    $("#paramFilterMbrsForm").html(jsonVar.htmlStr)


                    isMemberUseInOtherLevel=jsonVar.isMemberUseInOtherLevel
                    $("#myList3").treeview({
                        animated:"slow",
                        persist: "cookie"
                    });

//                    $('ul#myList3 li').quicksearch({
//                        position: 'before',
//                        attached: 'ul#myList3',
//                        loaderText: '',
//                        delay: 100
//                    })
                    $(".myDragTabs").draggable({
                        helper:"clone",
                        effect:["", "fade"]
                    });
                    $("#dropTabs").droppable({
                        activeClass:"blueBorder",
                        accept:'.myDragTabs',
                        drop: function(ev, ui) {
                            createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                        }
                    }
                );
                     grpColArray=jsonVar.memberValues
                    $(".sortable").sortable();
//                    grpColArray=new Array
//                    $("#myList3 li span").each(function(){
//                        if($(this).html()!="")
//                            grpColArray.push($(this).html())
//                    })
                               if("openSenSitivity"=='<%=openSenSitivity%>')
                {                        if(parent.sensitivityname!=null){
                                      grpColArray=new Array

                             var factorsArray=parent.sensitivityname.split(",")
                             for(var i=0;i<factorsArray.length;i++){
                                 if(factorsArray[i]!=""||factorsArray[i]!=" ")
                                 createColumn(factorsArray[i],factorsArray[i],"sortable")
                                 removeColumn(factorsArray[i])

                             }
                               $("#sensitivityFactor").val(parent.sensitivityVar)

                }  }

                var from='<%=from%>';
                var privHtml='<%=peviousDims%>';
                // alert(privHtml)
                var dims=privHtml.split("||");
                if(from=="scorecard")
                {
                    document.getElementById("saveElement").style.display ='none'
                    document.getElementById("assign").style.display ='none'
                    document.getElementById("saveSensTy").style.display ='none'
                    document.getElementById("sesnsiTable").style.display ='none'
                    document.getElementById("scoreSaveTable").style.display =''
//                    isMemberUseInOtherLevel="false"
                    for(var i=0;i<dims.length;i++){
                        if(dims[i]!="") {
                            createColumn(dims[i],dims[i],"sortable");
                        }
                    }
                }

                });
             }

        </script>
    </head>
        <body onload="onloadChanges()">
         <table style="width:90%;" align="center">
             <tr><td width='50%' valign='top'><%=TranslaterHelper.getTranslatedInLocale("search", cL)%> :&nbsp;<input  type="text" id="mainSearch" autocomplete="off"  style=" width:200px;height:15px;" onClick = "this.style.backgroundColor='lightgoldenrodyellow'" onfocus="goSearch()">&nbsp;&nbsp;<input type="button" onclick="goSearch()" value="<%=TranslaterHelper.getTranslatedInLocale("go", cL)%>"></td></tr>
        </table>
        <form action=""  name="paramFilterMbrsForm" method="post" id="paramFilterMbrsForm">

        </form>
        <table align="center" style="display: none" id="sesnsiTable">
            <tr>
                <td>
                    <font size="1" style="font-weight:bold"> <%=TranslaterHelper.getTranslatedInLocale("Sensitivity_Factor", cL)%> :</font> <input type="text" id="sensitivityFactor" name="sensitivityFactor" value="" onkeypress="return isNumberevent(event)" onchange=" checkNumber()">
                </td>
            </tr>
        </table>
        <br/>
        <Table style="width:90%;" align="center">
            <Tr align="center">
                <td align="center" colspan="4">
                    <input type="button" id="saveElement" name="Save" value="<%=TranslaterHelper.getTranslatedInLocale("save", cL)%>" class="navtitle-hover" onclick="goSave('<%=reportId%>','<%=paramFilterName%>','<%=dimBussTabId%>','<%=dimElementId%>','<%=measEleId%>','<%=busstabName + "." + memName%>','<%=dimType%>')">
                </td>
                <td>
                    <input type="button" id="assign" name="assign" value="<%=TranslaterHelper.getTranslatedInLocale("Assign", cL)%>" class="navtitle-hover" onclick="assignSensitivity()" style="display: none" >
                    <input type="button" id="saveSensTy" name="saveSensTy" value="<%=TranslaterHelper.getTranslatedInLocale("done", cL)%>" class="navtitle-hover" onclick="saveSensitivity()" style="display: none">
                </td>
                <td>

                </td>

            </Tr>
        </Table>
        <table align="center" id="scoreSaveTable" style="display: none;">
            <tr>
                <td>
                    <input type="button" id="saveScoreDimId" name="Back" style="" value="<%=TranslaterHelper.getTranslatedInLocale("Back", cL)%>" class="navtitle-hover" onclick="parent.goBackToDimensionValuesDiv()">
                </td>
                <td>
                    <input type="button" id="saveScoreDimId" name="Save" style="" value="<%=TranslaterHelper.getTranslatedInLocale("next", cL)%>" class="navtitle-hover" onclick="saveDimensionsMembers()">
                </td>

            </tr>
        </table>
    </body>
</html>
