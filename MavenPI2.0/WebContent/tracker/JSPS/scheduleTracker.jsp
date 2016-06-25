<%--
    Document   : scheduleTracker
    Created on : 13 Jan, 2011, 2:35:51 PM
    Author     : progen
--%>

<%@page import="java.util.Locale"%>
<%@page import="com.progen.i18n.TranslaterHelper"%>
<%@page import="prg.db.PbReturnObject"%>
<%@page import="prg.db.PbDb"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
            String themeColor = "blue";
            Locale locale = null;
            String paramHtml="";
            String reportName="";
  //          String fromReport=String.valueOf(request.getAttribute("fromReport"));
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }
            locale = (Locale) session.getAttribute("userLocale");
            int userId = Integer.parseInt((String) session.getAttribute("USERID"));
            String trackerId = request.getParameter("trackerId");
            String isEdit=request.getParameter("isEdit");
             if (isEdit != null && "false".equalsIgnoreCase(isEdit))
                    session.removeAttribute("scheduler");
            String[] strOprtrs = {"<", ">", "<=", ">=", "=", "<>"};
 //           String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
            String[] days = {"First Day Of Week", "Last Day Of Week"};
//            if (fromReport != "null" && !(fromReport.equalsIgnoreCase("")))
 //           {
 //               if (fromReport.equalsIgnoreCase("true")) {
 //                   paramHtml = String.valueOf(request.getAttribute("paramHtml"));
         //           trackerId = String.valueOf(request.getAttribute("reportId"));
 //                   reportName = String.valueOf(request.getAttribute("reportName"));
  //                  isEdit = String.valueOf(request.getAttribute("isEdit"));
 //               }
  //          }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
         <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
         <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
       <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/ReportCss.css" rel="stylesheet" />
<!--       <script  type="text/javascript" src="<%=request.getContextPath()%>/tracker/JSPS/testTimePicker.js" ></script>-->
<!--       <Script type="text/javascript"  src="../JS/timePicker.js"></Script>-->
        <Script type="text/javascript"  src="../JS/emailsajax.js"></Script>
        <Script type="text/javascript"  src="../JS/testdate.js"></Script>
<!--        <Script type="text/javascript"  src="../JS/myScripts.js"></Script>-->
        <Script type="text/javascript"  src="../JS/dateSelection.js"></Script>

        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
         <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/quicksearch.js"></script>
         <script  type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.contextMenu.js" ></script>


    </head>
    <style type="text/css">
            *{
               -x-system-font: none;
               font-family: verdana;
               font-size: 11px;
               font-size-adjust: none;
               font-stretch: normal;
               font-style: normal;
               font-variant: normal;
               font-weight: normal;
               line-height: normal;
            }
             .ajaxboxstyle {
                position: absolute;
                background-color: #FFFFFF;
                text-align: left;
                border: 1px solid #000000;
                border-top-width:1px;
                height:80px;
                width:180px;
                overflow:auto;
                overflow:hidden;
                margin:0em 0.5em;
            }
            .myAjaxTable {
                table-layout:fixed;
                background-color: #FFFFFF;
                text-align:left;
                border: 0px solid #000000;
                font-size:10px;
                left:4px;
                height:auto;
                border-collapse:separate;
                border-spacing:5px;
            }
             .suggestLink {
                position: relative;
                background-color: #FFFFFF;
                border: 0px solid #000000;
                border-top-width: 0px;
                padding: 2px 6px 2px 6px;
                left:3px;
                min-width: 20px;
                max-width: 150px;
            }
    </style>
    <script type="text/javascript">
                var busFolderId;
        var grpColArray=new Array();
        var grpSuppColArray=new Array();
        var dimSelectedArr=new Array();
        var condIdx;
        var rowId;
        var tableCondition="";
        $(document).ready(function(){
            if($.browser.msie==true)
            {
                $("#measuresDiv").dialog({
                    autoOpen: false,
                    height:520,
                    width: 700,
                    position: 'justify',
                    modal: true
                });
                $("#schedulerHistoryDialog").dialog({
                    autoOpen: false,
                    height:560,
                    width: 590,
                    position: 'justify',
                    modal: true
                });
                 $("#suppMeasuresDiv").dialog({
                    autoOpen: false,
                    height:520,
                    width: 700,
                    position: 'justify',
                    modal: true
                });
                $("#trackerConditions").dialog({
                    autoOpen: false,
                    height:480,
                    width: 700,
                    position: 'justify',
                    modal: true
                });

//                 $("#trackerConditionsTable").dialog({
//                    autoOpen: false,
//                    height:480,
//                    width: 700,
//                    position: 'justify',
//                    modal: true
//                });
            }
            else{
                $("#measuresDiv").dialog({
                    autoOpen: false,
                    height:420,
                    width: 700,
                    position: 'justify',
                    modal: true
                });
                $("#schedulerHistoryDialog").dialog({
                    autoOpen: false,
                    height:430,
                    width: 590,
                    position: 'justify',
                    modal: true
                });
                $("#suppMeasuresDiv").dialog({
                    autoOpen: false,
                    height:430,
                    width: 590,
                    position: 'justify',
                    modal: true
                });
                $("#trackerConditions").dialog({
                    autoOpen: false,
                    height:380,
                    width: 700,
                    position: 'justify',
                    modal: true
                });
//
//                $("#trackerConditionsTable").dialog({
//                    autoOpen: false,
//                    height:380,
//                    width: 700,
//                    position: 'justify',
//                    modal: true
//                });
          }
           if($("#frequency").val()=="1")
               $("#timeselect").show();

           if('<%=isEdit%>'!='readOnly')
            {
                 $('#stDatepicker').datepicker({
                        changeMonth: true,
                        changeYear: true
                    });
                $('#edDatepicker').datepicker({
                    changeMonth: true,
                    changeYear: true
                });
            }

                $.ajax({
                    url:"<%=request.getContextPath()%>/scheduler.do?reportBy=getTrackerDetails&trackerId=<%=trackerId%>",
                    success: function(data)
                    {
//                        alert(data)

                        var completeJson=data.split("~");
                         var json=eval('('+completeJson[0]+')');
                         populateTrackerRoles(completeJson[1]);
                         populateTrackerConditions(completeJson[2],completeJson[3]);
//                         trackCondJson=completeJson[2].concat("~").concat(completeJson[3]);
                         var paramXml=json.parameterXml;
                        $("#paramXml").val(paramXml);
                        $("#paramsDiv").html(json.parameter);
                        $("#trackerName").val(json.trackerName);
                        var freq = "";
                        if (json.frequency == "Daily"){
                            freq = "1";
                        }
                        else if (json.frequency == "Monthly"){
                            freq = "2";
                        }
                        else if (json.frequency == "Weekly"){
                            freq = "3";
                        }
                        $("#frequency").val(freq);
                        var freqSelect = document.getElementById("frequency");
                        addDate(freqSelect);
                         if(json.scheduledTime!=undefined)
                         {
                            var scheduledTime=json.scheduledTime;
                            var time=scheduledTime.split(":");
                            $("#hrs").val(time[0]);
                            $("#mins").val(time[1]);
                            var sd = new Date(json.startDate)
                            var ed = new Date(json.endDate)
                            var strtDate=1+(sd.getMonth())+"/"+sd.getDate()+"/"+sd.getFullYear();
                            var endDate=1+(ed.getMonth())+"/"+ed.getDate()+"/"+ed.getFullYear();

                         }
                         if(json.sendAnyWay==true)
                            {
                                $('#sendAnywayCheck').attr('checked','checked');
                                $("#sendAnywayInput").val(true);
                            }
                            else
                            {
                                $("#sendAnywayInput").val(false);
                            }
                             $("#stDatepicker").val(strtDate);
                             $("#edDatepicker").val(endDate);
                             $("select#selectBusRoles").attr("value",json.folderId);
                             $("select#mode").attr("value",json.mode);
                             $("#toAddress").val(json.subscribers);
                             $("#measId").val(json.measureId);
                             $("#folderId").val(json.folderId);
                             $("#conditionValue").val(json.condValue);
                            $("#operatorValue").val(json.condOperator);

                             busFolderId=json.folderId;
                             if(json.measureName!=undefined)
                                createColumn(json.measureId,json.measureName,"sortable");
                             var suppMsrNames=json.supportingMsrNames;
                             if(suppMsrNames!=undefined)
                             {
                                 $("#suppMeasId").val(json.supportingMsr);
                                 $("#suppMeasName").val(suppMsrNames);
                                var suppMsrNamesArr=suppMsrNames.split(",");
                                var suppMsrIdsArr=json.supportingMsr.split(",");
                                for(var i=0;i<suppMsrNamesArr.length;i++){
                                        createColumnSuppMsr(suppMsrIdsArr[i], suppMsrNamesArr[i],'sortable1');
                                    }
                             }

                             $("input#suppMeasureSelected").val(suppMsrNames);
                             if(busFolderId!=undefined)
                             {
                                 $.ajax({
                                url:"<%=request.getContextPath()%>/scheduler.do?reportBy=getTrackerDimensions&userId=<%=userId%>&folderId="+busFolderId,
                                success: function(data)
                                {
                                    var jsonDim=eval('('+data+')');
                                    var optionsHtml="";
                                    for(var k=0;k<jsonDim.length;k++)
                                    {
                                         optionsHtml+="<option id=\""+jsonDim[k].dimensionId+"\" value=\""+jsonDim[k].dimensionId+"\">"+jsonDim[k].dimensionName+"</option>";
                                    }
                                    if(optionsHtml!=""){
                                        var html=$("#allDim").html();
                                        $("#allDim").html(html+optionsHtml);
                                    }
                                    $("select#allDim").attr("value",json.viewById);
                                    loadDimDetails();
                                 if(json.isAutoIdentifier==true)
                                    {
                                        $('#autoTracker').attr('checked','checked');
                                        $("#autoChckInput").val(true);
                                        checkUsersTracker();
                                    }
                                }
                              });
                             }

                             $("input#measureSelected").val(json.measureName);
                             var trackerConditions=json.trackerConditions;
                             if(trackerConditions!=undefined)
                             {
                                 for(var k=0;k<trackerConditions.length;k++)
                                 {
                                     if(k>0)
                                       addDimRow();
                                    $("#"+(k+2)+"dimDetail").val(trackerConditions[k].viewByValue);
                                    $("#"+(k+2)+"mail").val(trackerConditions[k].mailIds);
        //                            $("select#"+(k+2)+"operator").attr("value",trackerConditions[k].operator);
        //                            $("#"+(k+2)+"sValues").val(trackerConditions[k].measureStartValue);
        //                            if(trackerConditions[k].operator=="<>")
        //                                $("#"+(k+2)+"eValues").val(trackerConditions[k].measureEndValue).show();
        //                            else
        //                                $("#"+(k+2)+"eValues").hide();

                                 }
                             }

                            if('<%=isEdit%>'=='readOnly')
                            {
                                $("#saveButton").hide();
                                $("#trackerName").attr("readonly", true);
                                $('#frequency').attr("disabled", true);
                                $('#hrs').attr("disabled", true);
                                $('#mins').attr("disabled", true);
                                $("#selectBusRoles").attr("disabled",true);
                                $("#mode").attr("disabled",true);
                                $("#measureSelected").attr("disabled",true);
                                $("#allDim").attr("disabled",true);
                                $('#autoTracker').attr("disabled", 'disabled');
                                $('#sendAnywayCheck').attr("disabled", 'disabled');
                                for(var j=0;j<trackerConditions.length;j++)
                                 {
//                                    $("#"+(j+2)+"dimDetail").attr("onkeyup",false);
                                    $("#"+(j+2)+"dimDetail").attr("readonly",true);
                                    $("#"+(j+2)+"mail").attr("readonly",true);
                                 }
                            }

              }


                });



        });



        function getSelectedDim(id,count)
        {
             var dimValue=$("#"+id).html();
             $("#"+count+"dimDetail").val(dimValue);
             closeDiv();
        }
        function addMeasures()
        {
            if('<%=isEdit%>'!='readOnly')
            {
                var selValue=document.getElementById("selectBusRoles");
            var id=selValue.id;
            var folderId=$("#"+id).val();
            $("#folderId").val(folderId);
            busFolderId=folderId;

            if(folderId!="select"){
                $.ajax({
                    url:"<%=request.getContextPath()%>/scheduler.do?reportBy=getTrackerMeasures&userId=<%=userId%>&folderId="+folderId+"&ctxPath=<%=request.getContextPath()%>",
                    success: function(data){
                        if(data!=""){
                            $("#quicksearch").remove();
                            $("#measures").html(data);
                                initClasses();
                            document.getElementById("quicksearch").style.height='10px';
                             $("#measuresDiv").dialog('open');
                        }

                    }
                });
            }

            }

        }
        function addSuppMeasures()
        {
            if('<%=isEdit%>'!='readOnly')
            {
                var selValue=document.getElementById("selectBusRoles");
                var id=selValue.id;
                var folderId=$("#"+id).val();
                $("#folderId").val(folderId);
                busFolderId=folderId;
                if(folderId!="select"){
                    $.ajax({
                        url:"<%=request.getContextPath()%>/scheduler.do?reportBy=getTrackerMeasures&userId=<%=userId%>&folderId="+folderId+"&ctxPath=<%=request.getContextPath()%>",
                        success: function(data){
                            if(data!=""){
                                $("#quicksearch").remove();
                                $("#suppMeasures").html(data);
                                    initClassesSuppMsrs();
                                document.getElementById("quicksearch").style.height='10px';
                                 $("#suppMeasuresDiv").dialog('open');
                            }

                        }
                    });
                }
            }

        }

        function initClasses(){

        $("#myList3").treeview({
            animated:"slow"
            //persist: "cookie"
        });

        $('ul#myList3 li').quicksearch({
            position: 'before',
            attached: 'ul#myList3',
            loaderText: '',
            delay: 100
        })

        $("#sortable").sortable();

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


        var dragMeasure=$('#measures > li > ul > li > ul > li > span,#measures > li > ul > li >  span')
        var dropMeasures=$('#selectedMeasures');

        $(dragMeasure).draggable({
            helper:"clone",
            effect:["", "fade"]
        });

        $(dropMeasures).droppable(
        {
            activeClass:"blueBorder",
            accept:'#measures > li > ul > li > ul > li > span,#measures > li > ul > li >  span',
            drop: function(ev, ui) {
                var measure=ui.draggable.html();
                createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
            }
        });


        $("#dropTabs").droppable({
            activeClass:"blueBorder",
            accept:'.myDragTabs',
            drop: function(ev, ui) {
                createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
            }
        });
        }


        function initClassesSuppMsrs(){

        $("#suppList3").treeview({
            animated:"slow"
            //persist: "cookie"
        });

        $('ul#suppList3 li').quicksearch({
            position: 'before',
            attached: 'ul#suppList3',
            loaderText: '',
            delay: 100
        })

        $("#sortable1").sortable();


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


        var dragMeasure=$('#suppMeasures > li > ul > li > ul > li > span,#suppMeasures > li > ul > li >  span')
        var dropMeasures=$('#selectedSuppMeasures');

        $(dragMeasure).draggable({
            helper:"clone",
            effect:["", "fade"]
        });

        $(dropMeasures).droppable(
        {
            activeClass:"blueBorder",
            accept:'#suppMeasures > li > ul > li > ul > li > span,#suppMeasures > li > ul > li >  span',
            drop: function(ev, ui) {
                var measure=ui.draggable.html();
                createColumnSuppMsr(ui.draggable.attr('id'),ui.draggable.html(),"sortable1")
            }
        });


        $("#dropTabs").droppable({
            activeClass:"blueBorder",
            accept:'.myDragTabs',
            drop: function(ev, ui) {
                createColumnSuppMsr(ui.draggable.attr('id'),ui.draggable.html(),"sortable1");
            }
        }

    );
        }


        function createColumn(elmntId,elementName,tarLoc){
            var ulobj=document.getElementById("sortable");
            var liobj=ulobj.getElementsByTagName("li");
                    if(liobj.length<1)
                {
                    var parentUL=document.getElementById(tarLoc);
                    var x=grpColArray.toString();
                    if(x.match(elmntId)==null){
                        grpColArray.push(elmntId+","+elementName);
                        var childLI=document.createElement("li");
                        childLI.id='GrpCol'+elmntId+","+elementName;
                        childLI.style.width='220px';
                        childLI.style.height='auto';
                        childLI.style.color='white';
                        childLI.style.align='center';
                        childLI.className='navtitle-hover';
                        var table=document.createElement("table");
                        table.id="GrpTab"+elmntId;
                        var row=table.insertRow(0);
                        var cell1=row.insertCell(0);

                        var a=document.createElement("a");
                        var deleteElement = 'GrpCol'+elmntId+","+elementName;
                        a.href="javascript:deleteColumn('"+deleteElement+"')";
                        a.innerHTML="a";
                        a.className="ui-icon ui-icon-close";
                        cell1.appendChild(a);
                        var cell2=row.insertCell(1);

                        cell2.style.color='black';
                        cell2.innerHTML=elementName;
                        childLI.appendChild(table);
                        parentUL.appendChild(childLI);
                    }
                }
                else{
                      alert("You cannot select more than one measure");
                }


                }

 function createColumnSuppMsr(elmntId,elementName,tarLoc){
            var ulobj=document.getElementById("sortable1");
            var liobj=ulobj.getElementsByTagName("li");
            var parentUL=document.getElementById(tarLoc);
            var x=grpSuppColArray.toString();
            if(x.match(elmntId)==null){
                grpSuppColArray.push(elmntId+","+elementName);
                var childLI=document.createElement("li");
                childLI.id='GrpCol'+elmntId+","+elementName;
                childLI.style.width='220px';
                childLI.style.height='auto';
                childLI.style.color='white';
                childLI.style.align='center';
                childLI.className='navtitle-hover';
                var table=document.createElement("table");
                table.id="GrpTab"+elmntId;
                var row=table.insertRow(0);
                var cell1=row.insertCell(0);

                var a=document.createElement("a");
                var deleteElement = 'GrpCol'+elmntId+","+elementName;
                a.href="javascript:deleteColumn('"+deleteElement+"')";
                a.innerHTML="a";
                a.className="ui-icon ui-icon-close";
                cell1.appendChild(a);
                var cell2=row.insertCell(1);

                cell2.style.color='black';
                cell2.innerHTML=elementName;
                childLI.appendChild(table);
                parentUL.appendChild(childLI);
                    }
                }


        function deleteColumn(index){
            var LiObj=document.getElementById(index);
            var parentUL=document.getElementById(LiObj.parentNode.id);
            parentUL.removeChild(LiObj);
            var x=index.replace("GrpCol","");
            var i=0;

            for(i=0;i<grpColArray.length;i++){
                if(grpColArray[i]==x){
                    grpColArray.splice(i,1);
                }

            }
        }


        function deleteSuppColumn(index){
            var LiObj=document.getElementById(index);
            var parentUL=document.getElementById(LiObj.parentNode.id);
            parentUL.removeChild(LiObj);
            var x=index.replace("GrpCol","");
            var i=0;

            for(i=0;i<grpSuppColArray.length;i++){
                if(grpSuppColArray[i]==x){
                    grpSuppColArray.splice(i,1);
                }

            }
        }
         var idx;

         function addDimRow()
        {
                var table = document.getElementById("dimRows");
                var rowCount = table.rows.length;
                 idx = rowCount ;
                var row = table.insertRow(rowCount);
                row.id="row"+idx;
                var tdhtml="<td width='100%'> <table width='60%' id='dimTable'> <tr><td width='23%'>";
                tdhtml+="<%=TranslaterHelper.getTranslatedString("DIMENSION_DETAILS", locale)%> <input type='text' id=\""+idx+"dimDetail\" class='dimDetail' value='All' name='dimDetail' onkeyup=\"getSuggesions('"+idx+"')\">";
    //            tdhtml+="Dimension Details <input type='text' id=\""+idx+"dimDetail\" class='dimDetail' value='All' name='dimDetail'>";
                tdhtml+="<img alt='' style='vertical-align:inline;display:inline;width:12px;height:15px;cursor:pointer' onclick=\"showDiv('"+idx+"')\"  src='../../images/include.png'/>";
                tdhtml+="<div id=\""+idx+"selectDimHtml\"  class='ajaxboxstyle' style='display: none;overflow: auto;'></div>";
                tdhtml+="</td>";
                tdhtml+=" <td width='14%'><input type='text' readonly id=\""+idx+"mail\" class='myTextbox3' name='mail' style='width: auto'></td>";
                 tdhtml+="<td width='1%'><img  align='middle' SRC='<%=request.getContextPath()%>/icons pinvoke/plus-circle.png' BORDER='0' ALT=''  onclick='addDimRow()' title='Add Row' /></td>";
                tdhtml+="<td width='1%'><img  align='middle' title='Delete Row' src='<%=request.getContextPath()%>/icons pinvoke/cross-circle.png' BORDER='0' ALT=''  onclick=\" deleteDimRow('"+row.id+"')\"  /></td>";
               tdhtml+=" <Td width='1%'><IMG ALIGN='middle' onclick=\"applyConditions('"+idx+"')\" SRC='<%=request.getContextPath()%>/icons pinvoke/tables-stacks.png' BORDER='0' ALT=''   title='Apply Conditions' /> </td>";
//                row.innerHTML=tdhtml;
                $("#"+row.id).append(tdhtml)
            }

        function deleteDimRow(rowId)
        {
                var rowId=rowId.substr(3);
                try {
                    var table = document.getElementById("dimRows");
                    var rowCount = table.rows.length;
                    if(rowCount > 3) {
                        table.deleteRow(rowId);
                        idx--;
                    }
                    else{
                        alert("You cannot delete all the rows");
                    }
                }catch(e) {
        //                alert(e);
                }
            }
        

// for targetBasis
     function targetValue(){
         document.getElementById("viewDeviationValue").style.dispaly=""
         //$.("#viewDeviationValue").show();
     }

               function isNumberKey(evt)
       {
          var charCode = (evt.which) ? evt.which : event.keyCode
          if (charCode != 46 && charCode > 31
            && (charCode < 48 || charCode > 57))
             return false;

          return true;
       }

     function deviationValue(){

                     var targetVal=document.getElementById("trgetVal").value;
                    // alert("targetVal\t"+targetVal);
                     var currVal=document.getElementById("currVal").value;
                    // alert("currVal\t"+currVal);
                    
                     var devPercent=  ((currVal-targetVal)/targetVal)*100;

                     var result=Math.round(devPercent*100)/100
           //alert("devPercent\t"+result);

                              document.getElementById("deviationPer").style.display="";
                              $("#deviationPercent").val(result);


     }
  //for targetBasis The above three Funs

        function saveTrackerMsrs()
        {

            $("#measuresDiv").dialog('close');
            var colsUl=document.getElementById("sortable");
            var measureName = "";
            var measArr=new Array();
            var measr="";
            var measId="";
            if(colsUl!=undefined || colsUl!=null)
            {
                var colIds=colsUl.getElementsByTagName("li");
                     measr = colIds[0].id.replace("GrpCol","");
                    measArr = measr.split(",");
                    measId=measArr[0];
                    measureName =measArr[1];
                    $("#measureSelected").val(measureName);
                    $("#measId").val(measId);
//                    $("#measName").val(measId);
                    uploadMeasures();

            }
                else
                {
                    alert("Please select  Measure")
                }
            }


            function saveSuppMsrs()
            {
                $("#suppMeasuresDiv").dialog('close');
            var colsUl=document.getElementById("sortable1");
            var measArr=new Array();
            var suppMeasures = "";
            var suppMeasureIdString = "";
            var suppMeasureLabelString = "";

            if(colsUl!=undefined || colsUl!=null)
            {
                var colIds=colsUl.getElementsByTagName("li");

                for(var i=0;i<colIds.length;i++)
                {
                    suppMeasures = colIds[i].id.replace("GrpCol","");
                    measArr = suppMeasures.split(",");
                    suppMeasureIdString = suppMeasureIdString+","+measArr[0];
                    suppMeasureLabelString = suppMeasureLabelString+","+measArr[1];
                }
                suppMeasureLabelString=suppMeasureLabelString.substr(1);
                suppMeasureIdString=suppMeasureIdString.substr(1);
                $("#suppMeasureSelected").val(suppMeasureLabelString);
                $("#suppMeasName").val(suppMeasureLabelString);
                $("#suppMeasId").val(suppMeasureIdString);
            }
                else
                {
                    alert("Please select  Measure")
                }
            }

            function uploadMeasures()
            {

              $.ajax({
                    url:"<%=request.getContextPath()%>/scheduler.do?reportBy=getTrackerDimensions&userId=<%=userId%>&folderId="+busFolderId,
                    success: function(data){
                        var json=eval('('+data+')');
                        var optionsHtml="";
                        for(var k=0;k<json.length;k++)
                        {
                             optionsHtml+="<option id=\""+json[k].dimensionId+"\" value=\""+json[k].dimensionId+"\">"+json[k].dimensionName+"</option>";
                        }
                        if(optionsHtml!=""){
                            var html=$("#allDim").html();
                            $("#allDim").html(html+optionsHtml);
                        }
                        $("select#allDim").attr("value","75778");
                    }
                });

            }
            function loadDimDetails()
            {
                var dimSelected=$("#allDim").val();
                $("#dimId").val(dimSelected);
                var dimName=$("#allDim option:selected").text();
                $("#dimName").val(dimName);
                 $.ajax({
                    url:"<%=request.getContextPath()%>/scheduler.do?reportBy=getTrackerDimensionsDetails&dimSelected="+dimSelected,
                    success: function(data){
                        var dimArr=data.split("~");
                        dimSelectedArr=dimArr;
                    }
                });
            }

            function getSuggesions(id)
            {
                var val=$("#"+id+"dimDetail").val();
                var suggArry=new Array();
                var dimHtml="";
                for(var k=0;k<dimSelectedArr.length;k++)
                 {
                     if(dimSelectedArr[k].substr(0,val.length)==val)
                         {
                             suggArry.push(dimSelectedArr[k]);
                         }
                 }

                dimHtml+="<table cellspacing='1' cellpadding='1'  id='dimSelectedTable' class='myAjaxTable'> <tr><td width='20%'>";
                dimHtml+="<tr><td onclick=\"getSelectedDim('all',"+id+")\"><div id='all' class='suggestLink'>All</div></td></tr>";
                 for(var k=0;k<suggArry.length;k++)
                 {

                     dimHtml+="<tr><td  onclick=\"getSelectedDim('"+id+"resultlist"+k+"',"+id+")\"><div id=\""+id+"resultlist"+k+"\" class='suggestLink'>";
                     dimHtml+=suggArry[k];
                     dimHtml+="</div></td>";
                     if((k+1) < suggArry.length)
                     {
                         dimHtml+="<td onclick=\"getSelectedDim('"+id+"resultlist"+(k+1)+"',"+id+")\" ><div id=\""+id+"resultlist"+(k+1)+"\" class='suggestLink' >";
                         dimHtml+=suggArry[k+1];
                         dimHtml+="</div></td>";
                     }
                     dimHtml+="</tr>";
                     dimHtml+="<tr></tr>";
                     k++;
                 }
                 dimHtml+="</table>";
                  $("#"+id+"selectDimHtml").html("")
                 $("#"+id+"selectDimHtml").html(dimHtml);
                  $('#'+id+'selectDimHtml').show();

            }
            function buildHtml(count)
            {
                var dimHtml="";
                dimHtml+="<table cellspacing='1' cellpadding='1'  id='dimSelectedTable' class='myAjaxTable'> <tr><td width='20%'>";
                dimHtml+="<tr><td onclick=\"getSelectedDim('all',"+count+")\"><div id='all' class='suggestLink'><%=TranslaterHelper.getTranslatedString("ALL", locale)%></div></td></tr>";
                 for(var k=0;k<dimSelectedArr.length;k++)
                 {

                     dimHtml+="<tr><td  onclick=\"getSelectedDim('"+count+"resultlist"+k+"',"+count+")\"><div id=\""+count+"resultlist"+k+"\" class='suggestLink'>";
                     dimHtml+=dimSelectedArr[k];
                     dimHtml+="</div></td>";
                     if((k+1) < dimSelectedArr.length)
                     {
                         dimHtml+="<td onclick=\"getSelectedDim('"+count+"resultlist"+(k+1)+"',"+count+")\" ><div id=\""+count+"resultlist"+(k+1)+"\" class='suggestLink' >";
                         dimHtml+=dimSelectedArr[k+1];
                         dimHtml+="</div></td>";
                     }
                     dimHtml+="</tr>";
                     dimHtml+="<tr></tr>";
                     k++;
                 }
                 dimHtml+="</table>";
                  $("#"+count+"selectDimHtml").html("")
                 $("#"+count+"selectDimHtml").html(dimHtml);

            }
            function addTextBox(symbol,rowId)
            {
                var open = document.getElementById(rowId+"eCondVal");
               if(symbol.value=="<>")
                  {
                      open.style.display='';
                 }
                  else{
                       open.style.display='none';
                  }
            }
         function goPaths(path){
                parent.closeStart();
                document.forms.schedulerForm.action="<%=request.getContextPath()%>/"+path;
                document.forms.schedulerForm.submit();
            }
            function viewReportG(path){
                document.forms.schedulerForm.action="<%=request.getContextPath()%>/"+path;
                document.forms.schedulerForm.submit();
            }
            function viewDashboardG(path){
                document.forms.schedulerForm.action="<%=request.getContextPath()%>/"+path;
                document.forms.schedulerForm.submit();
            }

            function runTracker(){
                var trackerId = '<%=trackerId%>';
                $("#schedulerHistory").show();
                $.ajax({
                    url:'<%=request.getContextPath()%>/scheduler.do?reportBy=runTracker&trackerId='+trackerId+'&fromStudio='+false,
                    success:function(data){
                         $("#schedulerHistory").hide();
                        //alert('Tracker Completed');
                        document.forms.schedulerForm.action = "<%=request.getContextPath()%>/home.jsp#Scheduler";
                        document.forms.schedulerForm.submit();
                    }
                });

            }

                  function targetCond(){
                  $("#targetConditions").show();
                      tableCondition="trgtBasic";
                  var rowCount=$('#condTable tr').length;
                   var tempID =""
                  for(var i=0;i<rowCount;i++){
                     tempID="#condTD"+i
                   //  alert("tempID--"+tempID+"-----")
                      $(tempID).html('');
                      $(tempID).html("When Deviation%");
                  }

                  // $("#trackerCondition").hide();
                 }
                 function absoluteCond(){
                  $("#targetConditions").hide();
                  $("#trackerCondition").show();

                  var rowCount=$('#condTable tr').length;
                  for(var i=0; i<rowCount; i++){
                      $("#condTD"+i).html('');
                      $("#condTD"+i).html("When Value");
                  }


                  // $("#targetConditions").hide();
                 }

               //Target Basis The above two funcs

            function saveTracker(runFlag)
            {
                var trackerName=document.getElementById("trackerName").value;
                var strdate=document.getElementById("stDatepicker").value;
                var enddate=document.getElementById("edDatepicker").value;
//                var toAddress=document.getElementById("toAddress").value;
                var mode=$("select#mode").attr("value");
                var dimName=$("#allDim option:selected").text();
                $("#dimName").val(dimName);
                 var dimSelected=$("#allDim").val();
                $("#dimId").val(dimSelected);
                var OperatorArry=new Array();
                var stValueArry=new Array();
                var endValueArry=new Array();
                var dimArray=new Array();
                var dim;
                var op;
                var stValue;
                var endValue;
                for(var i=2;i<idx;i++)
                {
                    dim=$("#"+i+"dimDetail").val();
                    op=$("#"+i+"operator").val();
                    stValue=$("#"+i+"sValues").val();
                    endValue=$("#"+i+"eValues").val();

                    dimArray.push(dim);
                    OperatorArry.push(op);
                    stValueArry.push(stValue);
//                    if(op.trim()!="<>")
//                        endValueArry.push("null");
//                    else
//                        endValueArry.push(endValue);
                }
                if(trackerName==""){
                    alert('Please enter Tracker Name');
                }else if(strdate==""){
                    alert('Please enter Start Date');
                }else if(enddate==""){
                    alert('Please enter End Date');
//                }
//                else if(toAddress==""){
//                    alert('Please enter To Whom You want to send mail or SMS');
                }
                 else if(document.getElementById("measureSelected").value==""){
                    alert('Please Select Measure');
                 }
                 else if(document.getElementById("suppMeasureSelected").value==""){
                    alert('Please Select Supporting Measures');
                 }
                  else if(document.getElementById("2mail").value==""){
                    alert('Please Apply Condition and enter Email');
                 }
                else{
//                    if(mode=="Email")
//                        var emailFlag=validateEmail(toAddress);
//                    else
//                        var mobileFlag=validateMobileNo(toAddress);

//                     if(emailFlag==true || mobileFlag==true)
//                     {
                  if('<%=isEdit%>'=='readOnly')
                  {
                       $("#schedulerHistory").show();
                        $.ajax({
                            url:'<%=request.getContextPath()%>/scheduler.do?reportBy=runTracker&trackerId='+'<%=trackerId%>'+'&fromStudio='+true,
                            success:function(data){
                                 $("#schedulerHistory").hide();
                                //alert('Tracker Completed');
                                document.forms.schedulerForm.action = "<%=request.getContextPath()%>/home.jsp#Scheduler";
                                document.forms.schedulerForm.submit();
                            }
                        });
                  }
                  else
                  {
                     $.ajax({
                        url:'<%=request.getContextPath()%>/calenderSetUpAction.do?timeCalenders=valiDateCalnder&startYear='+strdate+'&endYear='+enddate+'&scheduler='+"scheduler",
                        success:function(data){
                            if(data=='true'){
//                                var isNumberValid=validateDimensionValues(dimArray,OperatorArry,stValueArry,endValueArry);
//                                if(isNumberValid==true)
//                                {
                                    $.post("<%=request.getContextPath()%>/scheduler.do?reportBy=scheduleTracker", $("#schedulerForm").serialize() ,
                                    function(data){
                                         if(runFlag=="true")
                                            runTracker();
                                        document.forms.schedulerForm.action = "<%=request.getContextPath()%>/home.jsp#Scheduler";
                                        document.forms.schedulerForm.submit();
                                    });

//                                }
                            }else{
                                alert("Please select End Year correctly.")
                            }

                        }
                    });

                  }


//                     }

                }
            }

            function validateDimensionValues(sVal,oper,stVal,endVal){

                var measure=$("#measureSelected").val();
                if(measure=="select"){
                    alert("Please select measure")
                    return false
                }

                if(sVal.length>0){

                    for(var i=0;i<sVal.length;i++){
                        if(sVal[i]=="" || stVal[i]=="" || stVal[i]==""){
                            alert("Please enter all the values");
                            return false;
                        }else if(oper[i].trim()=="<>" && (parseInt(stVal[i]))>=(parseInt(endVal[i]))){
                            alert("End value should be greater then the start value")
                            return false;
                        }else if(endVal[i]!="null" && (isNaN(stVal[i])|| isNaN(endVal[i]))){
                            alert("Please enter numbers only!")
                            return false;
                        }else if(endVal[i]=="null" &&  isNaN(stVal[i])){
                            alert("Please enter numbers only")
                            return false;
                        }else if(oper[i].trim()=="select"){
                            alert("Please select an operator")
                            return false;
                        }

                    }
                }
                return true;

            }

            function validateMobileNo(number)
            {
                if(isNaN(number))
                {
                    alert("Please enter only numbers for SMS");
                    return false;
                }
                else if (number.length!=10 )
                {
                    alert("Please enter less than 10 numbers for SMS");
                    return false;
                }
                return true;
            }


            function validateEmail(email)
            {
                 var ids=email.split(",");
                for(var i=0;i<ids.length;i++)
                {
                var c=0;
                 invalidChars = "' /:,;";
                    for (var k = 0; k< invalidChars.length; k++)
                    {
                        badChar = invalidChars.charAt(i)
                        if (ids[i].indexOf(badChar,0) != -1)
                        {
                            c=1;
                            alert("You can't use following characters " + invalidChars +" in your Email address.");
                         document.forms.schedulerForm.ids[i].focus();
                         return false;
                        }
                    }
                    atPos = ids[i].indexOf("@",1);
                    if (atPos == -1)
                    {
                        c=1;
                        alert("You need to provide your Email UserId. i.e  your email should be in this format info@ezcommerceinc.com");
                     document.forms.schedulerForm.ids[i].focus();
                     return false;
                    }
                    if (ids[i].indexOf("@",atPos+1) != -1)
                    {
                        c=1;
                        alert("The Email address you have provided does not have '@' symbol. Please enter valid Email address.");
                     document.forms.schedulerForm.ids[i].focus();
                     return false;
                    }
                    periodPos = ids[i].lastIndexOf(".")
                    if (periodPos == -1)
                    {
                        c=1;
                        alert("The Email address you have provided does not have .com or .net etc. Please provide a valid Email address.");
                     document.forms.schedulerForm.ids[i].focus();
                    return false;
                    }
                    if (! ( (periodPos+3 == ids[i].length) || (periodPos+4  == ids[i].length) ))
                    {
                        c=1;
                        alert("The Email address you have provided does not have .com or .net etc. Please provide a valid Email address.");
                      document.forms.schedulerForm.ids[i].focus();
                     return false;
                    }
                }
                    return true;

            }

            function showDiv(rowId)
            {
                if('<%=isEdit%>'!='readOnly')
                {
                    buildHtml(rowId);
                    $('#'+rowId+'selectDimHtml').show();
                    var divId=document.getElementById(rowId+'selectDimHtml');
                    divId.style.display = (divId.style.display != 'none' ? 'none' : '' );
                }
            }
            function closeDiv(){
                $('.ajaxboxstyle').hide();
            }
            function goSchedulerHome()
            {
                document.forms.schedulerForm.action = "<%=request.getContextPath()%>/home.jsp#Scheduler";
                document.forms.schedulerForm.submit();
            }
            function showHistory()
            {
                 if('<%=isEdit%>'!='readOnly')
                 {
                      var ctxPath = '<%= request.getContextPath() %>';
                        var timeLevel="month" ;
        //                var dimMemId= $("#"+id+"dimDetail").val();
                        var dimMemId= "All";
                        var measureId=$("#measId").val();
                        var folderId=$("#folderId").val();
                        var dimId=$("#allDim").val();
                        var dimName=$("#dimName").val();
                        var measureName=$("#measureSelected").val();
                        var frequency = $("#frequency").val();
                        if(measureId=="")
                            alert("Please Select measure");
                        else
                        {
                            $("#schedulerHistoryDialog").dialog('open');
                            $.ajax({
                                url: ctxPath+"/scheduler.do?reportBy=scheduleHistory&timeLevel="+timeLevel+"&memberId="+dimMemId+"&frequency="+frequency+
                                    "&measureId="+measureId+"&folderId="+folderId+"&dimId="+dimId+"&dimName="+dimName+"&msrName="+measureName+"&fromMeasure="+"true",
                                success: function(data){
                                    $("#schedulerHistory").html(data);
                                   $("select#condOperator").attr("value",$("#operatorValue").val());
                                   $("input#condVal").val($("#conditionValue").val());
                                }
                            });
                        }
                 }


            }
            function closeHistory()
            {
                var condOperator=$("#condOperator").val();
                var condVal=$("#condVal").val();
                $("#conditionValue").val(condVal);
                $("#operatorValue").val(condOperator);
                $("#schedulerHistoryDialog").dialog('close');
            }

            function checkUsersTracker()
            {
                 var selectedDimension=$("#allDim").val();
               if(selectedDimension!=null)
                {
                     var autoCheck=document.getElementById("autoTracker");
                     var dimId=$("#dimId").val();
//                     alert(dimId)
                    if(autoCheck.checked)
                    {
                        $.ajax({
                            url:"<%=request.getContextPath()%>/scheduler.do?reportBy=checkUserAvailibility&reportViewBy="+dimId,
                            success: function(data){
                                if(data=='NotExists')
                                {
                                    alert("This View by is not applicable");
//                                   document.getElementById('addressTab').style.display="";
                                   $('#autoTracker').removeAttr("checked");
                                   $("#autoChckInput").val(false);
//                                   document.getElementById('dimRows').style.display="";
                                }
                                else
                                {
//                                    document.getElementById('addressTab').style.display="none";
//                                     document.getElementById('dimRows').style.display="none";
                                     $("#autoChckInput").val(true);
                                }
                            }
                        });
                    }
                   else
                   {
//                         document.getElementById('addressTab').style.display="";
//                          document.getElementById('dimRows').style.display="";
                         $("#autoChckInput").val(false);

                   }
               }
               else
               {
                    alert("Please Select Dimension");
                    $('#autoTracker').removeAttr("checked");
               }
            }

            function populateTrackerRoles(data)
        {
             var json=eval('('+data+')');
             var roleId=json.roleId;
             var roleHtml="";
             for(var i=0;i<roleId.length;i++)
                 {
                         roleHtml=roleHtml+"<option value=\""+roleId[i]+"\">"+json.roleName[i]+"</option>";
                 }
                  var html=$("#selectBusRoles").html();
                    $("#sortable").html("")
                    $("#selectBusRoles").html(html+roleHtml);
                     $("#loading").hide();

        }
        function sendTrackerAnyway()
        {
             var sendCheck=document.getElementById("sendAnywayCheck");
            if(sendCheck.checked)
            {
                $("#sendAnywayInput").val(true);
                $("#applyconditions").hide();
                $("#2mail").attr("readonly",false);
            }
            else
            {
                $("#sendAnywayInput").val(false);
                $("#applyconditions").show();
                $("#2mail").attr("readonly",true);
            }
        }

        function applyConditions(id)
        {
                    $("#trgetVal").val("");                                                  //target Basis
                    $("#deviationPercent").val("");
                    $("#targetConditions").hide();
                    $('input:radio[name=trackerTest]')[1].checked = false;
                    $('input:radio[name=trackerTest]')[0].checked = true;           //target Basis
            if('<%=isEdit%>'!='readOnly')
            {
                  var timeLevel="month" ;
                  var dimDetailObj=document.getElementsByName("dimDetail");
                  var dimDetailArr=new Array();
                  for(var k=0;k<dimDetailObj.length;k++)
                  {
                    dimDetailArr.push(dimDetailObj[k].value);
                  }
                  var dimMemId= $("#"+id+"dimDetail").val();
                  var index=$.inArray(dimMemId, dimDetailArr)
//                  var index=dimDetailArr.indexOf(dimMemId);
                  dimDetailArr.splice(index,1);
                  if($.inArray(dimMemId, dimDetailArr)!=-1)
                      alert("Please Select different Dimension value");
                  else
                  {
                        var measureId=$("#measId").val();
                        var folderId=$("#folderId").val();
                        var dimId=$("#allDim").val();
                        var dimName=$("#dimName").val();
                        var measureName=$("#measureSelected").val();
                        var frequency=$("#frequency").val();
                        $.ajax({
                        url: "<%=request.getContextPath()%>/scheduler.do?reportBy=scheduleHistory&timeLevel="+timeLevel+"&memberId="+dimMemId+"&frequency="+frequency+
                        "&measureId="+measureId+"&folderId="+folderId+"&dimId="+dimId+"&dimName="+dimName+"&msrName="+measureName+"&fromMeasure="+"false",
                        success: function(data){
                            $("#schdHistoryTable").html(data);
                             $("#trackerConditions").dialog('open');
                            rowId=id;
                            $("#dimValue").val($("#"+id+"dimDetail").val())
                                createConditionTable();
                        }
                       });
                  }


            }

        }
        function addCondRow()
        {
            var rowCount =  document.getElementById("condTable").rows.length;
             condIdx = rowCount ;
            var rowID="cond"+condIdx
             var condHtml="";
            var temTrgtType="";
            if(tableCondition=="trgtBasic"){
                    temTrgtType="When Deviation%";
            }else{
                temTrgtType="When Value";
            }
            var condHtml="";
            condHtml+="<tr id='"+rowID+"'><td align='left'><span id='condTD"+condIdx+"'>"+temTrgtType+"</span></td>";
             condHtml+="<td>";
             condHtml+="<select name=\"condOp\" id=\""+condIdx+"condOp\" onchange=\"addTextBox(this,'"+condIdx+"')\">"+
               <%for (String Str : strOprtrs) {%>
                      "<option value='<%=Str%>'><%=Str%></option>"+
              <%}%>
                      "</select></td>";
             condHtml+="<td><input type='text' style='width: 125px;' id=\""+condIdx+"sCondVal\" name='sCondVal' class='myTextbox3'></td>\n\
         <td><input type='text' style='width: 125px; display: none;' id=\""+condIdx+"eCondVal\" name='eCondVal' class='myTextbox3'></td>\n\
            <td>Send Mail to </td><td><input type='text' id=\""+condIdx+"condMail\" class='myTextbox3' name='condMail' style='width: auto'> </td>\n\
                    <td><img border='0' align='middle' title='Add Row' alt='' src='<%=request.getContextPath()%>/icons pinvoke/plus-circle.png' onclick='addCondRow()'></td>\n\
            <td><img border='0' align='middle' title='Delete Row' alt='' src='<%=request.getContextPath()%>/icons pinvoke/cross-circle.png' onclick=\"deleteCondRow('"+rowID+"')\"></td></tr>";

                        //<td>Send Anyway<input type='checkbox'  id=\""+condIdx+"sendCheck\" class='myTextbox3' name='sendCheck' style='width: auto'></td>\n\

             if(rowCount > 0){
                   $('#condTable tr:last').after(condHtml);

             }else{
               $("#condTable").html(condHtml)
             }

        }
        function deleteCondRow(rowId)
        {
             var rowId=rowId.substr(4);
            var table = document.getElementById("condTable");    
            var rowCount = table.rows.length;
            if(rowCount> 1) {
                table.deleteRow(rowId);
                condIdx--;
            }
            else{
                alert("You cannot delete all the rows");
            }
        }

    function saveEachCondition()
    {
        var mailIdObj=document.getElementsByName("condMail");
       // var sendChckObj=document.getElementsByName("sendCheck");
        var mailArr=new Array();
        var sendArr=new Array();
        var trgtval=document.getElementById("trgetVal").value;                                          //for targetBasis
        var devtionval=document.getElementById("deviationPercent").value;
        for(var i=0;i<mailIdObj.length;i++)
        {
            mailArr.push(mailIdObj[i].value);
          //  var sendCheck=document.getElementById(i+"sendCheck");
//            if(sendCheck.checked)
//                sendArr.push(true);
//            else
              //  sendArr.push(false);
        }
        $("#"+rowId+"mail").val(mailArr);
        $("#trackerConditions").dialog('close');
        if(document.getElementById("absolute").checked){
         $.post("<%=request.getContextPath()%>/scheduler.do?reportBy=saveTrackerConditions&sendChckArr="+sendArr+"&fromFlag="+false, $("#trackerConditionsForm").serialize() ,
        function(data)
        {

//            document.forms.schedulerForm.action = "<%=request.getContextPath()%>/home.jsp#Scheduler";
//            document.forms.schedulerForm.submit();
        });
        }
         else if(document.getElementById("target").checked) {
        $.post("<%=request.getContextPath()%>/scheduler.do?reportBy=saveTrackerConditions&sendChckArr="+sendArr+"&targetVal="+trgtval+"&deviationVal="+devtionval+"&fromFlag="+true, $("#trackerConditionsForm").serialize() ,
        function(data)
        {
//            document.forms.schedulerForm.action = "<%=request.getContextPath()%>/home.jsp#Scheduler";
//            document.forms.schedulerForm.submit();
        });
         }
    }
    function populateTrackerConditions(data,dimData)
    {
        if (data != "" && dimData != ""){
            var trackerCondJson=eval("("+data+")");
            var dimVal=eval("("+dimData+")");
            var dimensionVal=dimVal.dimValues;
            var mailArr=new Array();
            for(var i=0;i<dimensionVal.length;i++)
            {
                if(i>0)
                   addDimRow();
                $("#"+(i+2)+"dimDetail").val(dimensionVal[i]);
                if('<%=isEdit%>'=='readOnly')
                {
                     $("#"+(i+2)+"dimDetail").attr("onkeyup",false);
                     $("#"+(i+2)+"dimDetail").attr("readonly",true);
                }

            }
            for(var j=0;j<dimensionVal.length;j++)
            {
                var dimDetails=trackerCondJson[dimensionVal[j]];
                mailArr=new Array();
               for(var k=0;k<dimDetails.length;k++)
               {
                   mailArr.push(dimDetails[k].mailIds);
               }
               $("#"+(j+2)+"mail").val(mailArr);

            }
        }
    }
    function createConditionTable()
    {
        var trackerCondJson="";
         $.ajax({
        url: "<%=request.getContextPath()%>/scheduler.do?reportBy=getTrckerCondFrmSession",
        success: function(data)
        {
             var completeDetails=data.split("~");
             var dimDetails = "";
             if (completeDetails[0] != "" && completeDetails[1] != ""){
                var dimValue=eval("("+completeDetails[1]+")");
                trackerCondJson=eval("("+completeDetails[0]+")");
                var presentDimVal=$("#dimValue").val();
                var index ="";
                var dimArr=dimValue.dimValues;
                index=dimArr.indexOf(presentDimVal);
                dimDetails=trackerCondJson[dimArr[index]];
             }
            var table = document.getElementById("condTable");
            table.innerHTML="";
            if(dimDetails!=undefined && dimDetails != "")
            {
                 for(var k=0;k<dimDetails.length;k++)
               {
                   addCondRow();
                   $("#"+(k)+"condMail").val(dimDetails[k].mailIds);
                   $("select#"+(k)+"condOp").attr("value",dimDetails[k].operator);
                   $("#"+(k)+"sCondVal").val(dimDetails[k].measureStartValue);
                    if(dimDetails[k].operator=="<>")
                        $("#"+(k)+"eCondVal").val(dimDetails[k].measureEndValue).show();
                    else
                        $("#"+(k)+"eCondVal").hide();
                 if(dimDetails[k].isSendAnywayCheck==true)
                      $("#"+(k)+"sendCheck").attr('checked', true);
//                     $("#"+(k)+"isSendAnywayCheck").attr('checked','checked');


               }

            }
        else
           addCondRow();
        }
        });


    }
</script>
    <body>
         <table style="width:100%">
                <tr>
                    <td valign="top" style="width:50%;">
                        <jsp:include page="../../Headerfolder/headerPage.jsp"/>
                    </td>
                </tr>
            </table>
        <center>
            <form action=""  name="schedulerForm" id="schedulerForm" method="post">
                <div class="navtitle1" style=" max-width: 100%; cursor: auto; height: 20px;"align="left">
                    <span> <font size="2" style="font-weight: bolder">Tracker </font><b> </b></span>
            </div>
                <Br><table align="right"><tr><td>
            <input id="saveButton" type="button" class="navtitle-hover" value="<%=TranslaterHelper.getTranslatedString("SAVE", locale)%>" onclick='saveTracker("false")'>
            <input  type="button" class="navtitle-hover" value="Run/Execute" onclick='saveTracker("true")'>
            <input type="button" value="<%=TranslaterHelper.getTranslatedString("SCHEDULER_HOME", locale)%>" onclick="javascript:goSchedulerHome()" class="navtitle-hover" style="width:auto"  style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia">
                    </td></tr></table>
                    <center>
                        <div style="width: 75%" align="center">
                            <table style='width:100%' align='center'>
                                <tr>
                                    <td colspan='2' style='height:15px'></td>
                                </tr>
                            </table>
                            <div id="paramsDiv"></div>
                            <table style='width:100%' align='center'>
                                <tr>
                                    <td colspan='2' style='height:15px'></td>
                                </tr>
                            </table>

                <table width="80%" >
                    <Br>
                    <Tr>
                        <td width="100%">
                            <table width="100%">
                                <tr>
                                    <Td class="myhead" width="40%"><%=TranslaterHelper.getTranslatedString("TRACKER_NAME", locale)%></Td>
                                    <Td width="20%" align="left"><Input type="text"  class="myTextbox5" name="trackerName" id="trackerName" maxlength=100  style="width:auto"></Td>
                                    <td width="40%"></td>
                                </tr>
                            </table>
                        </td>
                    </Tr>
                    <Tr>
                        <td width="100%">
                            <table width="100%">
                                <tr>
                                    <Td class="myhead" width="40%"><%=TranslaterHelper.getTranslatedString("TRACKER_FREQUENCY", locale)%></Td>
                                    <Td width="10%">
                                        <Select name="frequency" id="frequency" class="myTextbox5" onchange="addDate(this)"  style="width:120px">
                                            <Option value="1"><%=TranslaterHelper.getTranslatedString("DAILY", locale)%></Option>
                                            <Option value="2"><%=TranslaterHelper.getTranslatedString("MONTHLY", locale)%></Option>
                                            <Option value="3">Weekly</Option>
                                        </Select>
                                    </Td>
                                    <td>
                                <div id="dateSelect" style="display:none">
                                    Month
                                    <select onchange="correctDate(this.form,this)" name="alertMonth">
                                        <script language="JavaScript" type="text/javascript">
                                            for(i=1; i<13; i++){
                                                if(i==month){
                                                    sel = "selected"
                                                }
                                                else{
                                                    sel = ""
                                                }
                                                document.write("<option value="+[i]+" "+sel+">"+months[i]+"\n")
                                            }

                                        </script>
                                    </select>
                                    <select name="alertDate" style="display:none" id="alertDate">

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
                            </td>
                            <td>
                                <div id="onlyDateSelect" style="display:none">
                                Day
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
                                            document.write("<option value='last'>Last\n")
                                        </script>
                                    </select>
                                </div>
                            </td>
                            <td>
                                <div id="dayOfWeek" style="display:none;width: auto">
                                    <select name="alertDate">
                                    <%for (int i=0;i<days.length;i++) {
                                        %>
                                        <option value='<%=i%>'><%=days[i]%></option>
                                     <%}%>
                                    </select>
                                </div>
                            </td>
                             <Td>
                                <div id="timeselect" style="display: none">
                                    <%=TranslaterHelper.getTranslatedString("HOURS", locale)%>
                                    <select name="hrs" id="hrs" >
                                        <%for (int i = 00; i < 24; i++) {%>
                                        <option  value="<%=i%>"><%=i%></option>
                                        <%}%>
                                    </select>
                                   <%=TranslaterHelper.getTranslatedString("MINUTES", locale)%>
                                   <select name="mins" id="mins">
                                        <%for (int i = 00; i < 60; i++) {%>
                                        <option  value="<%=i%>"><%=i%></option>
                                        <%}%>
                                    </select>
                                </div>
                            </Td>
<!--                                    </Td>-->
                                </tr>
                            </table>
                        </td>

                    </Tr>
                    <tr><td width="100%">
                <table id="dataSelection" width="100%" ><tr>
                    <td width="40%" class="myhead">Data</td>
                    <td width="25%" align="left" id="dailyDataTD" >
                        <select id="dailyData" name="dailyData">
                            <option value="current">Current Day</option>
                            <option value="last">Last Day</option>
                        </select>
                    </td>
                    <td id="monthlyDataTD">
                        <select id="monthlyData" name="monthlyData">
                            <option value="last">Last Month</option>
                            <option value="mtd">MTD</option>
                            <option value="both">Last Month + MTD</option>
                        </select>
                    </td>
                    <td width="35%"></td>
                    </tr>
                    </table></td>
                </tr>
                    <Tr>
                        <td width="100%">
                            <table width="100%">
                                <tr>
                                    <Td class="myhead" width="40%"><%=TranslaterHelper.getTranslatedString("START_DATE", locale)%></Td>
                                    <Td width="40%" align="left"><Input type="text" readonly class="myTextbox5" name="startdate" id="stDatepicker" maxlength=100  style="width:120px" value="" ></Td>
                                    <td width="20%"></td>
                                </tr>
                            </table>
                        </td>

                    </Tr>
                    <Tr>
                        <td width="100%">
                            <table width="100%">
                                <tr>
                                    <Td class="myhead" width="40%"><%=TranslaterHelper.getTranslatedString("END_DATE", locale)%></Td>
                                    <Td width="40%" align="left"><Input type="text" readonly  class="myTextbox5" name="enddate" id="edDatepicker" maxlength=100  style="width:120px"  ></Td>
                                    <td width="20%"></td>
                                </tr>
                            </table>
                        </td>

                    </Tr>
                    <tr>
                        <td width="100%">
                            <table width="100%">
                                <tr>
                                    <Td class="myhead" width="40%"><%=TranslaterHelper.getTranslatedString("BUSINESS_ROLES", locale)%></Td>
                                    <Td width="40%" align="left">
                                        <select id="selectBusRoles" >
<!--                                             <option id="select" >select</option>-->
                                        </select>
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
                                    <Td class="myhead" width="40%"><%=TranslaterHelper.getTranslatedString("EMAIL_SMS", locale)%></Td>
                                    <Td width="30%" align="left">
<!--                                <Select name="mode" id="mode" class="myTextbox5"  style="width:120px" onchange="getMode()">-->
                                <Select name="mode" id="mode" class="myTextbox5"  style="width:120px">
                                <Option value="Email"><%=TranslaterHelper.getTranslatedString("EMAIL", locale)%></Option>
                                <Option value="Mobile"><%=TranslaterHelper.getTranslatedString("SMS", locale)%></Option>
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
                                    <Td class="myhead" width="40%"><%=TranslaterHelper.getTranslatedString("MEASURES", locale)%></Td>
                                    <Td width="10%"><Input type="text" readonly  class="myTextbox5" name="measureSelected" id="measureSelected" maxlength=100  style="width:120px" onclick="javascript:addMeasures()"  ></Td>
                                    <td width="12%"><a href="javascript:void(0)" onclick="javascript:addMeasures()" style="font-size:12px;font-weight:bold;text-decoration: none;font-family:Georgia"></a> </td>
                                    <td><a href="javascript:void(0)" onclick="javascript:showHistory()" style="font-size:12px;font-weight:bold;text-decoration: none;font-family:Georgia"></a></td>
                                </tr>
                            </table>
                        </td>

                    </Tr>
                    <Tr>
                        <td width="100%">
                            <table width="100%">
                                <tr>
                                    <Td class="myhead" width="40%">Supporting Measures</Td>
                                    <Td width="5%"><Input type="text" onclick="javascript:addSuppMeasures()" readonly  class="myTextbox5" name="suppMeasureSelected" id="suppMeasureSelected" maxlength=100  style="width:auto"  ></Td>
                                    <td><a href="javascript:void(0)"  style="font-size:12px;font-weight:bold;text-decoration: none;font-family:Georgia"></a> </td>
                                </tr>
                            </table>
                        </td>

                    </Tr>
                     <Tr>
                        <td width="100%">
                            <table width="100%">
                                <tr>
                                    <Td class="myhead" width="40%"><%=TranslaterHelper.getTranslatedString("SELECT_DIMENSION", locale)%></Td>
                                    <Td width="40%" align="left"><select id="allDim" name="allDim" onchange="loadDimDetails()">
<!--                                <option id="selDimension" >select</option>-->
                            </select>
                                        </Td>

                                    <td width="20%"></td>
                                </tr>
                            </table>
                        </td>

                    </Tr>
                    <Tr>
                        <td width="100%">
                            <table  width="100%" id="autoTrackerTable" >
                                <tr>
                                    <Td style="font-weight: bolder" class="myhead"><%=TranslaterHelper.getTranslatedString("SUBSCRIBERS", locale)%>  </Td>
                                    <td align="left"><input type="checkbox" name="autoCheck" onclick="checkUsersTracker()" id="autoTracker"><br></td>
                                    <td><font> Auto Identifier</font></td>
                                    <td width="30%"></td>
                                </tr>
                            </table>
                        </td>
                    </Tr>
                    <Tr>
                        <td width="100%">
                            <table  width="100%" id="sendanywayTab" >
                                <tr>
                                    <Td style="font-weight: bolder" class="myhead">Send Me Anyway  </Td>
                                    <td align="left"><input type="checkbox" name="sendAnywayCheck" onclick="sendTrackerAnyway()" id="sendAnywayCheck"><br></td>
                                </tr>
                            </table>
                        </td>
                    </Tr>

<!--                     <Tr>
                        <td width="100%">
                            <table width="100%" id="addressTab">
                                <tr>
                                    <Td class="myhead" width="40%">Email Id</Td>
                                    <Td width="30%">
                                        <div id="mainemaildiv">
                                            <input type=text name=toAddress id="toAddress" class="myTextBox5" style="width:auto">
                                        </div>
                                    </Td>
                                    <td width="30%"></td>
                                </tr>
                            </table>
                        </td>
                    </Tr>-->
<!--                     <Tr>
                        <td width="100%">
                            <table width="100%">
                                <tr>
                                    <Td class="myhead" width="40%"><%=TranslaterHelper.getTranslatedString("TO_WHOM", locale)%></Td>
                                    <Td width="30%">
                                        <div id="mainemaildiv">
                                            <input type=text name=toAddress id="toAddress" class="myTextBox5" style="width:auto">
                                        </div>
                                    </Td>
                                    <td width="30%"></td>
                                </tr>
                            </table>
                        </td>
                    </Tr>-->
                </table>

                                               <Table id="dimRows" width="80%">
                    <Tr>
                        <td width="100%">
                            <table width="100%">
                             <Tr>
                                <td  align="center" colspan="4" height="10px" ></td>
                             </Tr>
                            </table>
                        </td>

                    </Tr>
                    <Tr>
                        <td width="100%">
                            <table id="conditionTable"  width="100%">
                                <tr>
                                    <Td width="30%" style="font-weight: bolder">Select Dimension Values  :</Td>
                                     <Td width="40%"></Td>
                                    <td width="20%"></td>
                                </tr>
                            </table>
                        </td>
                    </Tr>
                    <Tr id="row2">
                        <td width="100%">
                            <table id="dimTable"  width="60%">
                                <tr>
                                    <td width="23%">
                                        <%=TranslaterHelper.getTranslatedString("DIMENSION_DETAILS", locale)%>
                                        <input type="text" id="2dimDetail" class="dimDetail" value="All" onkeyup='getSuggesions("2")' name="dimDetail">
<!--                                        <input type="text" id="2dimDetail" class="dimDetail" value="All" name="dimDetail">-->
                                        <img alt="" style='vertical-align:inline;display:inline;width:12px;height:15px;cursor:pointer' onclick='showDiv("2")'  src='../../images/include.png'/>
                                        <div id="2selectDimHtml" class="ajaxboxstyle" style="display: none;overflow: auto;"></div>

                                    </td>
                                    <td width="14%">
                                        <input type="text" id="2mail" readonly class="myTextbox3" name="mail" style="width: auto">
                                    </td>
                                    <Td width="1%">
                                        <IMG ALIGN="middle" onclick='addDimRow()' SRC="<%=request.getContextPath()%>/icons pinvoke/plus-circle.png" BORDER="0" ALT=""   title="Add Row" />
                                    </Td>
                                    <Td width="1%">
                                        <IMG ALIGN="middle" onclick='deleteDimRow("row2")' SRC="<%=request.getContextPath()%>/icons pinvoke/cross-circle.png" BORDER="0" ALT=""   title="Delete Row" />
                                    </Td>
                                    <Td width="1%" align="left">
                                        <IMG ALIGN="middle" onclick='applyConditions("2")' SRC="<%=request.getContextPath()%>/icons pinvoke/tables-stacks.png" BORDER="0" ALT=""   title="Apply Conditions" />
                                    </Td>
<!--                                    <Td width="3%">
                                        <IMG ALIGN="middle" onclick='deleteDimRow("row2")' SRC="<%=request.getContextPath()%>/icons pinvoke/cross-circle.png" BORDER="0" ALT=""   title="Delete Row" />
                                    </Td>-->

<!--                                    <Td>
                                        <IMG ALIGN="middle" onclick='showHistory("2")' SRC="<%=request.getContextPath()%>/icons pinvoke/tables-stacks.png" BORDER="0" ALT=""   title="Show History" />
                                    </Td>-->
                                </tr>
                            </table>
                        </td>
                    </Tr>
            </Table>


<!--           <Table id="dimRows" width="80%">
                    <Tr>
                        <td width="100%">
                            <table width="100%">
                             <Tr>
                                <td  align="center" colspan="4" height="10px" ></td>
                             </Tr>
                            </table>
                        </td>

                    </Tr>
                    <Tr>
                        <td width="100%">
                            <table id="conditionTable"  width="100%">
                                <tr>
                                    <Td width="30%" style="font-weight: bolder"><%=TranslaterHelper.getTranslatedString("ADD_CONDITION", locale)%>  :</Td>
                                     <Td width="40%"></Td>
                                    <td width="20%"></td>
                                </tr>
                            </table>
                        </td>
                    </Tr>
                    <Tr id="row2">
                        <td width="100%">
                            <table id="dimTable"  width="100%">
                                <tr>
                                    <td width="23%">
                                        <%=TranslaterHelper.getTranslatedString("DIMENSION_DETAILS", locale)%>
                                        <input type="text" id="2dimDetail" class="dimDetail" value="All" onkeyup='getSuggesions("2")' name="dimDetail">
                                        <input type="text" id="2dimDetail" class="dimDetail" value="All" name="dimDetail">
                                        <img alt="" style='vertical-align:inline;display:inline;width:12px;height:15px;cursor:pointer' onclick='showDiv("2")'  src='../../images/include.png'/>
                                        <div id="2selectDimHtml" class="ajaxboxstyle" style="display: none;overflow: auto;"></div>

                                    </td>
                                    <td width="10%" align="left"> <%=TranslaterHelper.getTranslatedString("WHEN_VALUE", locale)%></td>
                                    <td width="5%">
                                        <select name="operators" id="2operator" onchange='addTextBox(this,"2")'>
                                            <option value="none">-Select-</option>
                                            <%for (String Str : strOprtrs) {%>
                                            <option  value="<%=Str%>"><%=Str%></option>
                                            <%}%>
                                        </select>
                                    </td>
                                    <Td WIDTH="5%">
                                        <Input type="text"  class="myTextbox3" name="sValues" id="2sValues"  style="width:70px" >
                                    </Td>
                                    <Td WIDTH="5%">
                                        <Input type="text"  class="myTextbox3" name="eValues" id="2eValues" style="width:70px;display: none" >
                                    </Td>
                                    <Td width="1%">
                                        <IMG ALIGN="middle" onclick='addDimRow()' SRC="<%=request.getContextPath()%>/icons pinvoke/plus-circle.png" BORDER="0" ALT=""   title="Add Row" />
                                    </Td>
                                    <Td width="3%">
                                        <IMG ALIGN="middle" onclick='deleteDimRow("row2")' SRC="<%=request.getContextPath()%>/icons pinvoke/cross-circle.png" BORDER="0" ALT=""   title="Delete Row" />
                                    </Td>

                                    <Td>
                                        <IMG ALIGN="middle" onclick='showHistory("2")' SRC="<%=request.getContextPath()%>/icons pinvoke/tables-stacks.png" BORDER="0" ALT=""   title="Show History" />
                                    </Td>
                                </tr>
                            </table>
                        </td>
                    </Tr>
            </Table>-->

</div>
                                     <br><br><br>
         </center>
<!--                    <input id="saveButton" type="button" class="navtitle-hover" value="<%=TranslaterHelper.getTranslatedString("SAVE", locale)%>" onclick='saveTracker("false")'>
                    <input  type="button" class="navtitle-hover" value="Run/Execute" onclick='saveTracker("true")'>-->

                <input type="hidden" name="measId" id="measId" value="">
                <input type="hidden" name="dimId" id="dimId" value="">
                <input type="hidden" name="dimName" id="dimName" value="">
                <input type="hidden" name="folderId" id="folderId" value="">
                <input type="hidden" name="trackerId" id="trackerId" value="<%=trackerId%>">
                <input type="hidden" id="isEdit" name="isEdit" value="<%=isEdit%>">
                <input type="hidden" id="autoChckInput" name="autoChckInput" value="">
                <input type="hidden" id="sendAnywayInput" name="sendAnywayInput" value="">
                <input type="hidden" id="conditionValue" name="conditionValue" value="">
                <input type="hidden" id="operatorValue" name="operatorValue" value="">

                <input type="hidden" id="paramXml" name="paramXml" value="">
                <input type="hidden" id="suppMeasId" name="suppMeasId" value="">
                <input type="hidden" id="suppMeasName" name="suppMeasName" value="">




            </form>

        </center>
          <div id="measuresDiv" title="<%=TranslaterHelper.getTranslatedString("ADD_MEASURES", locale)%>" style="display:none" >

                <table style="width:100%;height:250px" border="solid black 1px">
                    <tr>
                        <td width="50%" valign="top" class="draggedTable1">
                            <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold"><%=TranslaterHelper.getTranslatedString("SELECT_MEASURES", locale)%></font></div>
                            <div style="height:250px;overflow:scroll">
                                <ul id="myList3" class="filetree treeview-famfamfam">
                                    <ul id="measures" ></ul>
                                </ul>
                            </div>
                        </td>
                        <td id="selectedMeasures" width="50%" valign="top">
                            <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold"><%=TranslaterHelper.getTranslatedString("DRAG_MEASURES", locale)%></font></div>
                            <div style="height:250px;overflow:auto">
                                <ul id="sortable"></ul>
                            </div>
                        </td>
                    </tr>

                </table>
                <table style="width:100%" align="center">
                    <tr>
                        <td colspan="2" style="height:10px"></td>
                    </tr>
                    <tr>
                        <td colspan="2" align="center">
                            <input type="button" class="navtitle-hover" style="width:auto" value="<%=TranslaterHelper.getTranslatedString("SAVE", locale)%>" onclick="saveTrackerMsrs()">
                        </td>
                    </tr>
                </table>
            </div>


            <div id="suppMeasuresDiv" title="Add Supporting Measures" style="display:none" >

                <table style="width:100%;height:250px" border="solid black 1px">
                    <tr>
                        <td width="50%" valign="top" class="draggedTable1">
                            <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold"><%=TranslaterHelper.getTranslatedString("SELECT_MEASURES", locale)%></font></div>
                            <div style="height:250px;overflow:scroll">
                                <ul id="suppList3" class="filetree treeview-famfamfam">
                                    <ul id="suppMeasures" ></ul>
                                </ul>
                            </div>
                        </td>
                        <td id="selectedSuppMeasures" width="50%" valign="top">
                            <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold"><%=TranslaterHelper.getTranslatedString("DRAG_MEASURES", locale)%></font></div>
                            <div style="height:250px;overflow:auto">
                                <ul id="sortable1"></ul>
                            </div>
                        </td>
                    </tr>

                </table>
                <table style="width:100%" align="center">
                    <tr>
                        <td colspan="2" style="height:10px"></td>
                    </tr>
                    <tr>
                        <td colspan="2" align="center">
                            <input type="button" class="navtitle-hover" style="width:auto" value="<%=TranslaterHelper.getTranslatedString("SAVE", locale)%>" onclick="saveSuppMsrs()">
                        </td>
                    </tr>
                </table>
            </div>
            <div id="schedulerHistoryDialog" title="Apply Filter" style="display:none">
                <div id="schedulerHistory">
                    <center><img alt=""  id='imgId' src='../../images/ajax.gif' align='middle'  width='75px' height='75px'  style='position:absolute' /></center>
                </div>
            </div>
            <div id="trackerConditions" title="Apply Conditions" style="display: none">
                <form action="" id="trackerConditionsForm" >
                    <div id="schdHistoryTable">

                    </div>
                         <table> <tr>
                              <Td width="40%"></Td>
                             <td width="20%"></td>
                              </tr>
                              <tr>
                                 <Td width="40%"></Td>
                                 <td width="20%"></td>
                              </tr>
                        </table>
                    <table>
                        <tr>
                      <td> <div id="absoluteBasis">
                          <table>
                            <tr>
                                <td style="font-weight: bolder">
                                   <font style='font-size:12px;font-weight:bold;text-decoration: none;font-family:Georgia;color:#336699'>
                                            <%=TranslaterHelper.getTranslatedString("ABSOLUTE_BASIS", locale)%>  :</font>
                                <input type="radio" id="absolute" class="navtitle-hover" name="trackerTest"   value="absolute Basis" style="width:auto" onclick="absoluteCond()" checked>
                                </td>
                                
                            </tr>
                        </table>
                    </div>
                    </td>
                    <td> <div id="targetBasis">
                        <table>
                            <tr>
                                <td style="font-weight: bolder">
                                    <font style='font-size:12px;font-weight:bold;text-decoration: none;font-family:Georgia;color:#336699'>
                                            <%=TranslaterHelper.getTranslatedString("TARGET_BASIS", locale)%>  :</font>
                               <input type="radio" id="target" class="navtitle-hover" name="trackerTest" value="target Basis" style="width:auto" onclick="targetCond()" >
                                </td>
                                
                            </tr>
                        </table>
                    </div>
                    </td>
                    </tr>
                    </table>

                  <div id="targetConditions" style="display:none">
                    <table id="targetConditionsTable">
                          <tr>
                            <td colspan="2" style="height:20px"></td>
                          </tr>
                        <Tr>
                        <td width="100%">
                            <table  width="100%">
                                <tr>
                                <table>
                                    <tr>
                                    <Td style="font-weight: bolder">
                                         <font style='font-size:12px;font-weight:bold;text-decoration: none;font-family:Georgia;color:#336699'>
                                            <%=TranslaterHelper.getTranslatedString("ENTER_TARGET", locale)%>  :</font>
                                            <Input type="text"  class="myTextbox3" id="trgetVal" value="" style="width:100px" onkeypress="return isNumberKey(event)"  onblur="targetValue()">
                                    </Td>
                                    <Td id="viewDeviationValue" style="display:block"><a href="javascript:void(0)" onclick="deviationValue()" style="font-size:12px;font-weight:bold;text-decoration: none;font-family:Georgia;" title="Deviation Value">View Deviation%</a></Td>
                                    <td style="width:10%"></td>
                                    <Td id="deviationPer" style="display:none">Deviation%   <input type="text" value=""  id="deviationPercent" readonly=""</Td>
                                    </tr>
                                </table>
                                </tr>
                                <tr>
                                <Td width="40%"></Td>
                                    <td width="20%"></td>
                                </tr>
<!--                                <tr>
                                    <Td style="font-weight: bolder"><font style='font-size:12px;font-weight:bold;text-decoration: none;font-family:Georgia;color:#336699'>
                                            <%=TranslaterHelper.getTranslatedString("ADD_CONDITION", locale)%>  :</font></Td>
                                     <Td width="40%"></Td>
                                    <td width="20%"></td>
                                </tr>

                        <td width="100%">-->
<!--                            <table id="targetTable"  width="100%">
                                <tr id="cond01" >
                                    <td align="left"> <%=TranslaterHelper.getTranslatedString("WHEN_DEVIATION_%", locale)%></td>
                                    <td>
                                        <select name="condOp" id="0condOp1" onchange='addTextBox(this,"0")'>
                                            <option value="none">-Select-</option>
                                            <%for (String Str : strOprtrs) {%>
                                            <option  value="<%=Str%>"><%=Str%></option>
                                            <%}%>
                                        </select>
                                    </td>
                                    <Td>
                                        <Input type="text"  class="myTextbox3" name="sCondVal" id="0sCondVal1"  style="width:120px" >
                                    </Td>
                                    <Td>
                                        <Input type="text"  class="myTextbox3" name="eCondVal" id="0eCondVal1" style="width:120px;display: none" >
                                    </Td>
                                    <td>
                                        Send Mail to
                                    </td>
                                    <td
                                        <input type="text"  id="0condMail" class="myTextbox3" name="condMail1" style="width: auto"  >
                                    </td>
                                    <td>
                                        Send Anyway
                                        <input type="checkbox"  id="0sendCheck" class="myTextbox3" name="sendCheck1" style="width: auto"  >
                                    </td>
                                    <Td>
                                        <IMG ALIGN="middle" onclick='addCondRow("targetTable")' SRC="<%=request.getContextPath()%>/icons pinvoke/plus-circle.png" BORDER="0" ALT=""   title="Add Row" />
                                    </Td>
                                    <Td>
                                        <IMG ALIGN="middle" onclick='deleteCondRow("cond01","targetTable")' SRC="<%=request.getContextPath()%>/icons pinvoke/cross-circle.png" BORDER="0" ALT=""   title="Delete Row" />
                                    </Td>
                                </tr>
                            </table>-->
<!--                            <table style="width:100%" align="center">
                                <tr>
                                    <td colspan="2" style="height:10px"></td>
                                </tr>
                                <tr>
                                    <td colspan="2" align="center">
                                        <input type="button" class="navtitle-hover" style="width:auto" value="Done" onclick="saveEachCondition("targetTable")">
                                    </td>
                                </tr>
                            </table>-->
                            </table>
                        </td>
                    </Tr>

                    </table>
            </div>

                 <div id="trackerCondition" style="display:block">
                    <table id="trackerConditionsTable">
                        <tr>
                            <td colspan="2" style="height:20px"></td>
                        </tr>


                        <Tr>
                        <td width="100%">
                            <table  width="100%">
                                <tr>
                                    <Td style="font-weight: bolder"><font style='font-size:12px;font-weight:bold;text-decoration: none;font-family:Georgia;color:#336699'>
                                            <%=TranslaterHelper.getTranslatedString("ADD_CONDITION", locale)%>  :</font></Td>
                                     <Td width="40%"></Td>
                                    <td width="20%"></td>
                                </tr>
                            </table>
                        </td>
                    </Tr>
                    <Tr>
                        <td width="100%">
                            <table id="condTable"  width="100%">
                                <tr id="cond0">
                                    <td align="left" > <span id="condTD0"><%=TranslaterHelper.getTranslatedString("WHEN_VALUE", locale)%></span></td>
                                    <td width="10%">
                                        <select name="condOp" id="0condOp" onchange='addTextBox(this,"0")'>
<!--                                            <option value="none">-Select-</option>-->
                                            <%for (String Str : strOprtrs) {%>
                                            <option  value="<%=Str%>"><%=Str%></option>
                                            <%}%>
                                        </select>
                                    </td>
                                    <Td>
                                        <Input type="text"  class="myTextbox3" name="sCondVal" id="0sCondVal"  style="width:125px" >
                                    </Td>
                                    <Td>
                                        <Input type="text"  class="myTextbox3" name="eCondVal" id="0eCondVal" style="width:125px;display: none" >
                                    </Td>
                                    <td width="30%">
                                        Send Mail to
                                    </td>
                                    <td width="20%">
                                        <input type="text"  id="0condMail" class="myTextbox3" name="condMail" style="width: auto">
                                    </td>
<!--                                    <td width="25%">
                                        Send Anyway
                                        <input type="checkbox"  id="0sendCheck" class="myTextbox3" name="sendCheck" style="width: auto">
                                    </td>-->
                                    <Td width="1%">
                                        <IMG ALIGN="middle" onclick='addCondRow()' SRC="<%=request.getContextPath()%>/icons pinvoke/plus-circle.png" BORDER="0" ALT=""   title="Add Row" />
                                    </Td>
                                    <Td width="1%">
                                        <IMG ALIGN="middle" onclick='deleteCondRow("cond0")' SRC="<%=request.getContextPath()%>/icons pinvoke/cross-circle.png" BORDER="0" ALT=""   title="Delete Row" />
                                    </Td>
                                    <td width="17%"></td>
                                </tr>
                            </table>                              
                            <table style="width:100%" align="center">
                                <tr>
                                    <td colspan="2" style="height:10px"></td>
                                </tr>
                                <tr>
                                    <td colspan="2" align="center">
                                        <input type="button" class="navtitle-hover" style="width:auto" value="Done" onclick="saveEachCondition()">
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </Tr>
                    </table>
                 </div>
                    <input type="hidden" id="dimValue" name="dimValue" value="">
                </form>
            </div>
    </body>

</html>